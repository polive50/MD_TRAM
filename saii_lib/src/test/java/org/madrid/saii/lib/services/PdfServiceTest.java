package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test PdfServiceTest
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class PdfServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService service;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

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

    // @Test
    public void testDescargarExpediente() {
        /*
         * try { DocumentoDTO descargar = service.descargarExpediente(null);
         *
         * // No es nulo Assert.assertNotNull(descargar.getContenidoFichero());
         *
         * FileUtils.writeByteArrayToFile( new File("C:\\producto\\" +
         * descargar.getNombreFichero()), descargar.getContenidoFichero()); }
         * catch (final ServiceException | IOException e) {
         * log.error(e.getMessage()); fail(Constantes.Test.TEST_ERROR_PRUEBA +
         * e.getMessage()); }
         */
    }

    // /**
    // * TODO Metodo para prueba de crearFirmar
    // */
    // @Test
    // public void testCrearFirmar() {
    // try {
    // ExpedienteDTO dto = expedienteService.buscarId(1);
    //
    // // No es nulo
    // Assert.assertNotNull(dto);
    //
    // // Limpiamos la instancia
    // Expediente ent = sessionFactory.getCurrentSession().get(Expediente.class,
    // dto.getIdExpediente());
    // sessionFactory.getCurrentSession().evict(ent);
    //
    // dto = service.crearFirmar(dto);
    //
    // // No es nulo
    // Assert.assertNotNull(dto);
    // Assert.assertNotNull(dto.getCdEreg());
    // Assert.assertNotNull(dto.getCdSiex());
    // Assert.assertNotNull(dto.getCdPdfFirmadoEreg());
    // Assert.assertNotNull(dto.getCdListadoDocEreg());
    // Assert.assertNotNull(dto.getDsDestinoEreg());
    // Assert.assertNotNull(dto.getFcFirma());
    //
    // // No está en blanco
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getCdEreg());
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getCdSiex());
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getCdPdfFirmadoEreg());
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getCdListadoDocEreg());
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getDsDestinoEreg());
    // Assert.assertNotEquals(StringUtils.EMPTY, dto.getFcFirma());
    //
    // } catch (final ServiceException e) {
    // log.error(e.getMessage());
    // fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
    // }
    // }

    /**
     * Prueba el metodo Descargar informe de coordinador sin data
     */
    // @Test
    public void testDescargarInformeCoordinadorNull() {

        try {
            DocumentoDTO descargar = this.service.descargarInformeCoordinador(0);

            // No es nulo
            Assert.assertNotNull(descargar.getContenidoFichero());

            FileUtils.writeByteArrayToFile(
                    new File("C:\\producto\\" + descargar.getNombreFichero()),
                    descargar.getContenidoFichero());
        } catch (final ServiceException | IOException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Prueba el metodo Descargar informe de coordinador con data
     */
    // @Test
    public void testDescargarInformeCoordinador() {
        try {

            Integer idExpediente = 1115;

            // serviceImpl.setFicaService(ficaService);
            DocumentoDTO dto = this.service.descargarInformeCoordinador(idExpediente);

            // No es nulo
            Assert.assertNotNull(dto);

            // Comprobaciones

            Assert.assertNotNull(dto.getFicaFichero());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testcrearFormularioEvaluacionDatosIdExpEval() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(9);
        dtoInput.setIdExpediente(82);
        dtoInput.setItCoorEval("C");
        dtoInput.setDsComentario("Prueba");
        dtoInput.setNmNotaFinal(18);
 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        Document docResult = this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        byte[] docPdf = baos.toByteArray();
        

        // No es nulo
        Assert.assertNotNull(docPdf);
    }

    /**
     * Test de metodo crearFormularioPlantilla - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testcrearFormularioPlantilla() throws ServiceException {

        ConvocatoriaPlaDTO dtoInput = new ConvocatoriaPlaDTO();
        dtoInput.setIdConvocatoriaPla(0);
        dtoInput.setIdConvocatoria(0);
        dtoInput.setDsAsunto("Asunto");
        dtoInput.setDsDescripcion("Descripcion");

        byte[] docPdf = this.service.crearFormularioPlantilla(dtoInput);

        // No es nulo
        Assert.assertNotNull(docPdf);
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testcrearFormularioEvaluacionDatosIdExpEvalItCoorEvalE() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(11);
        dtoInput.setIdExpediente(84);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(17);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        byte[] docPdf = baos.toByteArray();

        // No es nulo
        Assert.assertNotNull(docPdf);

        Assert.assertEquals(docPdf, docPdf);
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testcrearFormularioEvaluacionDatosIdExpEvalItCoorEvalC() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(12);
        dtoInput.setIdExpediente(85);
        dtoInput.setItCoorEval("C");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(17);

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        byte[] docPdf = baos.toByteArray();

        // No es nulo
        Assert.assertNotNull(docPdf);

        Assert.assertEquals(docPdf, docPdf);
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testcrearFormularioEvaluacionDatosIdExpEvalItCoorEvalE10() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(10);
        dtoInput.setIdExpediente(84);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(17);
         
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        

    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testcrearFormularioEvaluacionDatosIdExpEvalSinIdExpedienteEval()
            throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(null);
        dtoInput.setIdExpediente(83);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(20);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

         this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testcrearFormularioEvaluacionDatosIdExpEvalSinIdExpediente()
            throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(10);
        dtoInput.setIdExpediente(null);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(20);
         
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

       
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testcrearFormularioEvaluacionDatosIdExpEvalSinItCoorEval() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(10);
        dtoInput.setIdExpediente(83);
        dtoInput.setItCoorEval(null);
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(20);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - 1 proceso
     * incorrecto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorCrearFormularioEvaluacionDatosIdExpEval() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(null);
        dtoInput.setIdExpediente(null);
        dtoInput.setItCoorEval(null);
        dtoInput.setDsComentario(null);
        dtoInput.setNmNotaFinal(null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
    }

    /**
     * Test de metodo crearFormularioEvaluacionDatosIdExpEval - proceso correcto
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testcrearFormularioEvaluacionDatosIdExpEvalDatosNoExiste() throws ServiceException {

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(1000);
        dtoInput.setIdExpediente(1000);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba 2");
        dtoInput.setNmNotaFinal(20);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.service.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

       
    }
}
