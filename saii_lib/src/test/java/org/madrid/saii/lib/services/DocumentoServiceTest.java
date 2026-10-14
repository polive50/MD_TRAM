package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.pdf.TextFieldCellEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ExpedienteService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class DocumentoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("documentoService")
    private DocumentoService service;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;

    /**
     * Session factory de hibernate
     */
    @Resource
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

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

    // /**
    // * Test para obtener documentos del expediente
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // public void testObtenerDocumentosExpediente() throws ServiceException {
    //
    // Document document = new Document();
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // try {
    //
    // PdfWriter.getInstance(document, stream);
    //
    // document.open();
    //
    // Paragraph parrafo = new Paragraph("Test");
    // document.add(parrafo);
    //
    // PdfPTable table = new PdfPTable(1);
    // table.setWidthPercentage(100);
    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    // PdfPCell cell = new PdfPCell(new Phrase(" "));
    // cell.setCellEvent(new TextFieldCellEvent("TL_DOCUMENTOS_PROCESAR",
    // BaseField.EDIT));
    // cell.setPadding(6);
    // table.addCell(cell);
    // document.add(table);
    //
    // document.close();
    //
    // } catch (DocumentException e) {
    // log.error(e.getMessage());
    // throw new ServiceException();
    // }
    //
    // // creamos mock para fica
    // Integer cdFica = 678439;
    // FicaService ficaService = mock(FicaService.class);
    //
    // FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
    // ficaFicheroImpl.setDatosFichero(stream.toByteArray());
    // ficaFicheroImpl.setNombreFichero("prueba.pdf");
    //
    // when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);
    // when(ficaService.recuperarMetadatos(cdFica)).thenReturn(ficaFicheroImpl);
    //
    // DocumentoServiceImpl serviceImpl = new DocumentoServiceImpl();
    // serviceImpl.setFicaService(ficaService);
    //
    // ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE
    // .apply(this.expedienteService.find(64));
    //
    // // Limpiamos la instancia
    // Expediente ent =
    // this.sessionFactory.getCurrentSession().get(Expediente.class,
    // expedienteDTO.getIdExpediente());
    // this.sessionFactory.getCurrentSession().evict(ent);
    //
    // List<DocumentoDTO> lstDTOResult =
    // serviceImpl.obtenerDocumentosExpediente(expedienteDTO,
    // Boolean.TRUE);
    //
    // Assert.assertNotNull(lstDTOResult);
    //
    // }

    // /**
    // * Test para obtener documentos de subsanacion del expediente
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // public void testObtenerDocumentosSubsanacionExpediente() throws
    // ServiceException {
    //
    // Document document = new Document();
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // try {
    //
    // PdfWriter.getInstance(document, stream);
    //
    // document.open();
    //
    // Paragraph parrafo = new Paragraph("Test");
    // document.add(parrafo);
    //
    // PdfPTable table = new PdfPTable(1);
    // table.setWidthPercentage(100);
    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    // PdfPCell cell = new PdfPCell(new Phrase(" "));
    // cell.setCellEvent(new TextFieldCellEvent("TL_DOCUMENTOS_PROCESAR",
    // BaseField.EDIT));
    // cell.setPadding(6);
    // table.addCell(cell);
    // document.add(table);
    //
    // document.close();
    //
    // } catch (DocumentException e) {
    // log.error(e.getMessage());
    // throw new ServiceException();
    // }
    //
    // // creamos mock para fica
    // Integer cdFica = 678439;
    // FicaService ficaService = mock(FicaService.class);
    //
    // FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
    // ficaFicheroImpl.setDatosFichero(stream.toByteArray());
    // ficaFicheroImpl.setNombreFichero("prueba.pdf");
    //
    // when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);
    // when(ficaService.recuperarMetadatos(cdFica)).thenReturn(ficaFicheroImpl);
    //
    // DocumentoServiceImpl serviceImpl = new DocumentoServiceImpl();
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    //
    // ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE
    // .apply(this.expedienteService.find(65));
    //
    // // Limpiamos la instancia
    // Expediente ent =
    // this.sessionFactory.getCurrentSession().get(Expediente.class,
    // expedienteDTO.getIdExpediente());
    // this.sessionFactory.getCurrentSession().evict(ent);
    //
    // List<DocumentoDTO> lstDTOResult = serviceImpl
    // .obtenerDocumentosSubsanacionExpediente(expedienteDTO, Boolean.TRUE);
    //
    // Assert.assertNotNull(lstDTOResult);
    //
    // }

    // /**
    // * Test para obtener documentos de subsanacion de la solicitud
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // public void testObtenerDocumentosSubsanacionSolicitud() throws
    // ServiceException {
    //
    // Document document = new Document();
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
    // try {
    //
    // PdfWriter.getInstance(document, stream);
    //
    // document.open();
    //
    // Paragraph parrafo = new Paragraph("Test");
    // document.add(parrafo);
    //
    // PdfPTable table = new PdfPTable(1);
    // table.setWidthPercentage(100);
    // table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
    // PdfPCell cell = new PdfPCell(new Phrase(" "));
    // cell.setCellEvent(new TextFieldCellEvent("TL_DOCUMENTOS_PROCESAR",
    // BaseField.EDIT));
    // cell.setPadding(6);
    // table.addCell(cell);
    // document.add(table);
    //
    // document.close();
    //
    // } catch (DocumentException e) {
    // throw new ServiceException();
    // }
    //
    // // creamos mock para fica
    // Integer cdFica = 678439;
    // FicaService ficaService = mock(FicaService.class);
    //
    // FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
    // ficaFicheroImpl.setDatosFichero(stream.toByteArray());
    // ficaFicheroImpl.setNombreFichero("prueba.pdf");
    //
    // when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);
    // when(ficaService.recuperarMetadatos(cdFica)).thenReturn(ficaFicheroImpl);
    //
    // DocumentoServiceImpl serviceImpl = new DocumentoServiceImpl();
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    // serviceImpl.setExpedienteService(this.expedienteService);
    //
    // List<DocumentoDTO> lstDTOResult =
    // serviceImpl.obtenerDocumentosSubsanacionSolicitud(54,
    // Boolean.TRUE);
    //
    // Assert.assertNotNull(lstDTOResult);
    //
    // }

    /**
     * Test para obtener irforme de coordinador
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @Test
    public void testObtenerDocumentosExpedienteEval() throws ServiceException {

        Document document = new Document();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, stream);

            document.open();

            Paragraph parrafo = new Paragraph("Test");
            document.add(parrafo);

            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            PdfPCell cell = new PdfPCell(new Phrase(" "));
            cell.setCellEvent(new TextFieldCellEvent("TL_DOCUMENTOS_PROCESAR", BaseField.EDIT));
            cell.setPadding(6);
            table.addCell(cell);
            document.add(table);

        } catch (DocumentException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

        document.close();

        // creamos mock para fica
        Integer cdFica = 678439;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(stream.toByteArray());
        ficaFicheroImpl.setNombreFichero("prueba.pdf");

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);
        when(ficaService.recuperarMetadatos(cdFica)).thenReturn(ficaFicheroImpl);

        DocumentoServiceImpl serviceImpl = new DocumentoServiceImpl();

        ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE
                .apply(this.expedienteService.find(64));

        // Limpiamos la instancia
        Expediente ent = this.sessionFactory.getCurrentSession().get(Expediente.class,
                expedienteDTO.getIdExpediente());
        this.sessionFactory.getCurrentSession().evict(ent);

        List<DocumentoDTO> lstDTOResult = serviceImpl
                .obtenerDocumentosExpedienteEval(expedienteDTO.getIdExpediente(), Boolean.TRUE);

        Assert.assertNotNull(lstDTOResult);

    }

}
