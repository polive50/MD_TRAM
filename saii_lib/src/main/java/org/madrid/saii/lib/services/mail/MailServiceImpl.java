package org.madrid.saii.lib.services.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.TramiteService;
import org.madrid.saii.lib.tramite.Tramite;
//import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service("mailService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Log4j2
public class MailServiceImpl implements MailService {

    /** wrapper de Spring sobre javax.mail */
    @Autowired
    @Qualifier("javaMailSender")
    private JavaMailSender mailSender;

    /** Plantilla del mensaje **/
    @Autowired
    @Qualifier("simpleMailMessage")
    private SimpleMailMessage mailMessage;

    /** Servicio de pdfService */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /** catalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** ExpedienteService */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    /**
     * Servicio Tramite
     */
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;

    /**
     *
     */
    @Getter
    @Setter
    @Value("${email.from}")
    private String emailFrom;

    //
    // /** wrapper para velocityEngine **/
    // @Autowired
    // @Qualifier("velocityEngine")
    // private VelocityEngine velocityEngine;

    /**
     * Metodo que se encarga de enviar el correo
     *
     * @param dto SolicjovenDTO
     * @param pdf PDF con los datos del carnet
     * @return Boolean resultado de la operación
     * @throws ServiceException excepción
     */
    @Override
    @Transactional(readOnly = false)
    public void enviarCorreo(String strEmail, ConvocatoriaPlaDTO emailContenido,
            ConvocatoriaDTO convocatoria, ExpedienteDTO expedienteDTO) throws ServiceException {

        if (StringUtils.isBlank(strEmail)) {
            log.error("La direccion de correo electronico esta vacia. No se realiza el envio");
        }

        Tramite tramite = tramiteService.getTramite(emailContenido.getTramiteDTO().getIdValor());

        tramite.setPdfService(this.pdfService);
        tramite.setCatalogosValoresService(this.catalogosValoresService);
        tramite.setExpedienteService(this.expedienteService);

        try {

            MimeMessage message = this.mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(this.emailFrom);

            String informeTramiteCorreo = tramite.formularioHtml(convocatoria, emailContenido,
                    expedienteDTO, null);
            helper.setText(informeTramiteCorreo, Boolean.TRUE);

            helper.setTo(strEmail);
            helper.setSubject(emailContenido.getDsAsunto());

            // Permite llenar el cuerpo del correo eletronico
            // this.rellenarCorreo(helper, emailContenido);

            log.info("Enviando: " + strEmail);
            this.mailSender.send(message);

        } catch (MailException e) {
            log.error("Error al enviar la información: " + e.getMessage());
            throw new ServiceException(e);
        } catch (MessagingException e) {
            log.error("Error durante la creacion del mensaje: " + e.getMessage());
            throw new ServiceException(e);
        }
    }

}
