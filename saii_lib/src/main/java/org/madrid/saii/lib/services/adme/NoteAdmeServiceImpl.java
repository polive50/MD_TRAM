package org.madrid.saii.lib.services.adme;

import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dto.NoteEnviarNotificacionDTO;
import org.madrid.saii.lib.dto.NoteObtenerNotificacionesDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.note.domain.NoteEnviarNotificacionIN;
import atlas.eadm.note.domain.NoteEnviarNotificacionOut;
import atlas.eadm.note.domain.NoteExisteNifOut;
import atlas.eadm.note.domain.NoteGenerarPdfOut;
import atlas.eadm.note.domain.NoteNotificacionPendiente;
import atlas.eadm.note.domain.NoteObtenerNotificacionesOut;
import atlas.eadm.services.NoteService;
import atlas.fica.services.FicaService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio NOTE Administracion
 *
 * @author everis
 */
@Service("noteAdmeService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class NoteAdmeServiceImpl implements NoteAdmeService {

    /**
     * Servicio NOTE
     */
    @Autowired
    @Qualifier("noteService")
    private NoteService noteService;
    
    /**
     * Servicio FICA SERVICE
     */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /**
     *
     */
    @Override
    public String recuperarCdAbonado(String cdNumIden) {

        String cdAbonado = StringUtils.EMPTY;
     
        try {
            log.info("NOTE: Llamada a existe Nif con cdNumIden: " + cdNumIden);

            NoteExisteNifOut existeNif = this.noteService
                    .existeNif(NoteAdmeService.ConstantesNote.COD_APP, cdNumIden);

            if (StringUtils.isNotBlank(existeNif.getCdAbonado())) {
                log.info("NOTE: Existe abonado " + cdNumIden + " con codigo: "
                        + existeNif.getCdAbonado());
                cdAbonado = existeNif.getCdAbonado();
            }

            return cdAbonado;
        } catch ( ServiceException e) {
            
            return cdAbonado;
        }
         
    }

    @Override
    public String recuperarEmailNote(String cdNumIden) throws ServiceException {

        String email = StringUtils.EMPTY;

        NoteExisteNifOut existeNif = this.noteService
                .existeNif(NoteAdmeService.ConstantesNote.COD_APP, cdNumIden);

        if (StringUtils.isNotBlank(existeNif.getTlEmail())) {
            email = existeNif.getTlEmail();
        } else {
            email = null;
        }

        return email;
    }

    /**
     *
     */
    @Override
    public NoteObtenerNotificacionesDTO obtenerNotificacion(String codigoNote)
            throws ServiceException {

        log.info("NOTE: Llamada a obtenerNotificaciones con codigoNote: " + codigoNote);

        NoteObtenerNotificacionesDTO resultado = null;

        try {

            NoteObtenerNotificacionesOut output = this.noteService
                    .obtenerNotificaciones(Constantes.AplicativoSaii.CODIGO_APLICATIVO, codigoNote);

            if (output != null && output.getListaNotificaciones() != null
                    && !output.getListaNotificaciones().isEmpty()) {

                NoteNotificacionPendiente obj = output.getListaNotificaciones().get(0);

                if (obj != null) {
                    resultado = new NoteObtenerNotificacionesDTO();
                    resultado.setCodigoOrigen(obj.getCodigoOrigen());
                    resultado.setCodigoOrigenInterno(obj.getCodigoOrigenInterno());
                    resultado.setNumeroAbonado(obj.getNumeroAbonado());
                    resultado.setCodigoAbonado(obj.getCodigoAbonado());
                    resultado.setFechaPuestaDisposicion(obj.getFechaPuestaDisposicion());
                    resultado.setFechaLectura(obj.getFechaLectura());
                    resultado.setFechaRechazo(obj.getFechaRechazo());
                    resultado.setFechaNotificacion(obj.getFecha_notificacion());
                    resultado.setCodigoEstado(obj.getCodigo_estado());

                    log.info("NOTE: Se encuentra un resultado: " + resultado.toString());
                }

            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex);
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public String enviarNotificacion(NoteEnviarNotificacionDTO inputNotifNoteDTO)
            throws ServiceException {

        log.info("NOTE: Llamada a enviarNotificacionConRetorno");
        log.info("NOTE: Datos. cdNumIden: " + inputNotifNoteDTO.getCdNumIden());
        log.info("NOTE: Datos. UniAdmin: " + inputNotifNoteDTO.getUniAdmin());
        log.info("NOTE: Datos. Asunto: " + inputNotifNoteDTO.getAsunto());
        log.info("NOTE: Datos. Cuerpo: " + inputNotifNoteDTO.getCuerpo());

        String cdNotificacion = null;

        String cdAbonado = this.recuperarCdAbonado(inputNotifNoteDTO.getCdNumIden());

        if (StringUtils.isNotBlank(cdAbonado)) {
            log.info("NOTE: Existe el abonado.");

            NoteEnviarNotificacionIN in = new NoteEnviarNotificacionIN();
            in.setCodigoApp(NoteAdmeService.ConstantesNote.COD_APP);
            in.setCodigoOrigen(NoteAdmeService.ConstantesNote.CODIGO_ORIGEN);
            in.setCodigoOrigenInt(NoteAdmeService.ConstantesNote.CODIGO_ORIGEN_INT);
            in.setCifrada(NoteAdmeService.ConstantesNote.CIFRADA_FALSE);
            in.setUniAdmin(inputNotifNoteDTO.getUniAdmin());
            in.setServicio(NoteAdmeService.ConstantesNote.SERVICIO);
            in.setAsunto(inputNotifNoteDTO.getAsunto());
            in.setCodigoAbonado(cdAbonado);
            in.setCuerpo(inputNotifNoteDTO.getCuerpo());

            if (inputNotifNoteDTO.getAdjunto() != null) {
                in.addAdjunto(inputNotifNoteDTO.getAdjunto());
            }

            if (inputNotifNoteDTO.getNombreAdjunto() != null) {
                in.addNombreAdjunto(inputNotifNoteDTO.getNombreAdjunto());
            }

            log.info("NOTE: Se envia notificacion de presentación " + in.getCodigoOrigen());
            NoteEnviarNotificacionOut out = this.noteService.enviarNotificacionConRetorno(in);

            if (StringUtils.isNotBlank(out.getNmNotificacion())) {

                log.info("NOTE: Salida ->" + out.getCdEstadoNotificacion() + " "
                        + out.getDsEstadoNotificacion() + " Numero: " + out.getNmNotificacion());

                cdNotificacion = out.getNmNotificacion();
            } else {
                log.error(out.getDsEstadoNotificacion());
            }

        } else {
            log.info(
                    "NOTE: No existe el abonado. No se envía notificación. Tiene que estar dado de alta previamente.");
        }

        return cdNotificacion;
    }

    @Override
    public FicaFichero generarFicaAcuseReciboPDF(String nmNotificacion, String nmAbonado,
            String accion) throws ServiceException {
        log.info("NOTE: Llamada a generarFicaAcuseReciboPDF con nmAbonado: " + nmAbonado);
        FicaFichero fichero = null;
        try {
            // generamos el PDF del acuse de recibo
            if (nmAbonado != null && !nmAbonado.equals("null")) {

                NoteGenerarPdfOut salida = this.noteService.generarPDF(nmNotificacion, nmAbonado,
                        accion);
                if (salida.getPdfBase64() != null) {
                    byte[] pdfBytes = Base64.getDecoder().decode(salida.getPdfBase64());
                    // Subimos a fica el fichero
                    // Preparamos los metadatos de fica
                    FicaMetadatos fica = new FicaMetadatos();
                    String nombre = nmNotificacion + "_" + nmAbonado;
                    fica.setNombreFichero(nombre);
                    fica.setFechaFichero(new Date());
                    fica.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
                    fica.setUsuario(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                    fica.setEncriptar(false);
                    fica.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
                    // guardamos fichero fica
                    fichero = this.ficaService.guardar(pdfBytes, fica);
                    // Comprobamos que se ha creado correctamente en caso
                    // contrario
                    // lanzamos error
                    if (fichero == null) {
                        throw new ServiceException(
                                "El servicio de almacenamiento no devolvió el fichero subido");
                    }
                    log.info("NOTE: Se generó el acuse de recibo: " + nombre);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex);
        }
        return fichero;
    }

}
