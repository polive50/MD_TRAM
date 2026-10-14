package org.madrid.saii.lib.services.adme;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudDatosDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudResultadoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesTipoDocDTO;
import org.madrid.saii.lib.dto.PtfrConsultaTiposDocumentoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.ptfr.domain.PtfrEstadoDocOut;
import atlas.test.base.AtlasAbstractBaseTest;

@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
public class PtfrAdmeServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService service;

    /**
     * Session factory de hibernate
     */
    @Resource
    private SessionFactory sessionFactory;

    /**
     * Asocia la sessionFactory de Hibernate a la variable interna
     *
     * @param sessionFactory la sessionFactory
     */
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Inicialización antes de empezar los test
     */

    @Before
    @Override
    public void setUp() {
        assertNotNull(this.service);
    }

    @Test
    @Ignore
    public void testCrearAltaSolicitud() throws ServiceException {

        try {
            Document document = new Document();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, stream);

            document.open();

            Paragraph parrafo = new Paragraph("Test");
            document.add(parrafo);

            document.close();

            PtfrAltaSolicitudDatosDTO datosEntrada = new PtfrAltaSolicitudDatosDTO();
            datosEntrada.setCodigoTipoDocumento(903);
            datosEntrada.setDocumento(stream.toByteArray());
            datosEntrada.setModoRespuesta(2);
            // datosEntrada.setListaCodigoFirmante(new ArrayList<>());
            // datosEntrada.getListaCodigoFirmante().add("ADMON");
            datosEntrada.setReferenciaSolicitante("ADMON");
            datosEntrada.setTitulo("TITULO PRUEBA NOTE ADMON " + System.currentTimeMillis());
            datosEntrada.setCove("S");

            PtfrAltaSolicitudResultadoDTO out = this.service.altaSolicitud(datosEntrada);

            Assert.assertNotNull(out);
        } catch (DocumentException e) {
            throw new ServiceException();
        }

    }

    @Test
    @Ignore
    public void testConsultaFirmantes() throws ServiceException {
        List<PtfrConsultaFirmantesDTO> lstDTO;

        lstDTO = this.service.consultaFirmantes();
        // No es nulo
        Assert.assertNotNull(lstDTO);
        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        Iterator<PtfrConsultaFirmantesDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            PtfrConsultaFirmantesDTO dto = it.next();
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCodUsu()));
            Assert.assertTrue(StringUtils.isEmpty(dto.getCargo()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getNombre()));
        }

    }

    @Test
    @Ignore
    public void testConsultaTiposDocumento() throws ServiceException {
        List<PtfrConsultaTiposDocumentoDTO> lstDTO;

        lstDTO = this.service.consultaTiposDocumento();
        // No es nulo
        Assert.assertNotNull(lstDTO);
        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        Iterator<PtfrConsultaTiposDocumentoDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            PtfrConsultaTiposDocumentoDTO dto = it.next();
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCodigo()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDescripcion()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getModulo()));
        }

    }

    @Test
    @Ignore
    public void testConsultaFirmantesTiposDocumento() throws ServiceException {
        List<PtfrConsultaFirmantesTipoDocDTO> lstDTO;

        lstDTO = this.service.consultaFirmantesTipoDocumento(903);
        // No es nulo
        Assert.assertNotNull(lstDTO);
        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        Iterator<PtfrConsultaFirmantesTipoDocDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            PtfrConsultaFirmantesTipoDocDTO dto = it.next();
            Assert.assertTrue(StringUtils.isNotBlank(dto.getTipo()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCargo()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getEtapa()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getRol()));
        }

    }

    @Test
    @Ignore
    public void testConsultaEstadoDoc() throws ServiceException {
        PtfrEstadoDocOut estado = this.service.consultaEstadoDoc("06/004578.9/21", "5658916");
        Assert.assertNotNull(estado);
        Assert.assertEquals("R", estado.getDocumento().getEstado());
    }

    @Test
    @Ignore
    public void testRecuperarDocumentoFirmado() throws ServiceException {
        PtfrRecuperaDocFirmadoDTO dto = this.service.recuperarDocumentoFirmado("06/004566.9/21",
                "5658866");
        Assert.assertNotNull(dto);
        Assert.assertEquals("F", dto.getEstado());
    }

    @Test
    @Ignore
    public void testConsultaTiposDocumentoPorCodigoAlias() throws ServiceException {
        PtfrConsultaTiposDocumentoDTO dto = this.service
                .consultaTiposDocumentoPorCodigoAlias("SAII-Subsanacion_administrativa");
        Assert.assertNotNull(dto);
    }
}
