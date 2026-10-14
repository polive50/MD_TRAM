package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.saii.lib.dao.ConvocatoriaPlaDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
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
 * Test DocumentoPtfrService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class DocumentoPtfrServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService service;

    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;

    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    @Autowired
    @Qualifier("convocatoriaPlaDAO")
    private ConvocatoriaPlaDAO convocatoriaPlaDAO;

    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

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

    /**
     * Prueba el metodo guardar
     */
    @Test
    @Ignore
    public void testGuardar() throws ServiceException {

        // GUARDAR
        DocumentoPtfrDTO dto = new DocumentoPtfrDTO();
        dto.setIdExpediente(9);
        dto.setDsReferenciaPtfr("06/004547.9/21");
        dto.setCdArchivoPtfr(5657640);
        dto.setFcEnvioPtfr(new Date());
        dto.setCdEstadoPtfr("P");
        dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
        dto.setFcAltaFila(new Date());

        dto = this.service.guardar(dto);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno
        this.testAtributos(dto, Boolean.FALSE);

        DocumentoPtfr ent = this.sessionFactory.getCurrentSession().get(DocumentoPtfr.class,
                dto.getIdDocumentoPtfr());
        this.sessionFactory.getCurrentSession().evict(ent);

        // Guardamos
        dto.setCdEstadoPtfr("F");
        dto.setCdUsuModif(Constantes.Test.TEST_ADMIN);
        dto.setFcModifFila(new Date());
        dto = this.service.guardar(dto);

        // La modificación es correcta
        Assert.assertEquals("F", dto.getCdEstadoPtfr());
        this.testAtributos(dto, Boolean.TRUE);
    }

    @Test
    @Ignore
    public void testEnviarPortafirmasSubsanar() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(18);

        // ejecucion correcta subsanacion
        DocumentoPtfrDTO dto = serviceImpl.enviarPortaFirmas(convocatoriaDTO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 16,null, null);

        Assert.assertNotNull(dto);

    }

    @Test
    @Ignore
    public void testEnviarPortafirmasSubSanacionRequisito() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteReqDAO(this.expedienteReqDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(26);

        // ejecucion correcta subsanacion requisito
        DocumentoPtfrDTO dto = serviceImpl.enviarPortaFirmas(convocatoriaDTO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO), 28,null, null);

        Assert.assertNotNull(dto);

    }

    @Test
    @Ignore
    public void testEnviarPortafirmasExclusionRequisito() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteReqDAO(this.expedienteReqDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(26);

        // ejecucion correcta subsanacion requisito
        DocumentoPtfrDTO dto = serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION), 28,null, null);

        Assert.assertNotNull(dto);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarErrorArchivo() throws ServiceException {

        try {
            Document document = new Document();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, stream);

            // creamos mock para fica
            Integer cdFica = 678021;
            FicaService ficaService = mock(FicaService.class);

            FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
            ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

            when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

            // inyectamos los servicio necesarios para el proceso a ejecutar
            DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

            serviceImpl.setDao(this.documentoPtfrDAO);
            serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
            serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
            serviceImpl.setExpedienteDAO(this.expedienteDAO);
            serviceImpl.setSolicitudDAO(this.solicitudDAO);

            serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
            serviceImpl.setNoteAdmeService(this.noteAdmeService);
            
            ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
            convocatoriaDTO.setIdConvocatoria(18);

            // ejecucion correcta subsanacion
            serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 16,null, null);
        } catch (DocumentException e) {
            throw new ServiceException();
        }

    }

    @Test
    @Ignore
    public void testEnviarPortafirmasSubsanarNoExistePlantillaPtfr() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(18);

        // ejecucion correcta subsanacion
        DocumentoPtfrDTO dto = serviceImpl.enviarPortaFirmas(convocatoriaDTO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 16,null, null);

        Assert.assertNotNull(dto);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarSinDocumentos() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(18);

        serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 1155,null, null);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarSinPlantillasAsociadas() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(19);

        serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 17,null, null);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarNoExisteDocumentoAEnviar() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(20);

        serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 18,null, null);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarNoExisteFirmantePtfr() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(21);

        serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), 19,null, null);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasSubsanarNoExistePlantilla() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(22);

        serviceImpl.enviarPortaFirmas(convocatoriaDTO, 0, 20,null, null);
    }

    @Test
    @Ignore
    public void testEnviarPortafirmasDesistir() throws ServiceException {

        // creamos mock para fica
        Integer cdFica = 678021;
        FicaService ficaService = mock(FicaService.class);

        FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
        ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

        when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);

        // inyectamos los servicio necesarios para el proceso a ejecutar
        DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

        serviceImpl.setDao(this.documentoPtfrDAO);
        serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
        serviceImpl.setExpedienteDAO(this.expedienteDAO);
        serviceImpl.setSolicitudDAO(this.solicitudDAO);

        serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
        serviceImpl.setNoteAdmeService(this.noteAdmeService);
        
        ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
        convocatoriaDTO.setIdConvocatoria(23);

        // ejecucion correcta desistimiento
        DocumentoPtfrDTO dto = serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO),
                21,null, null);
        Assert.assertNotNull(dto);

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasErrorDocumento() throws ServiceException {

        try {
            Document document = new Document();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, stream);
            // creamos mock para fica
            Integer cdFica = 678021;
            FicaService ficaService = mock(FicaService.class);

            FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
            ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

            when(ficaService.recuperar(cdFica)).thenThrow(new ServiceException());

            // inyectamos los servicio necesarios para el proceso a ejecutar
            DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

            serviceImpl.setDao(this.documentoPtfrDAO);
            serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
            serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
            serviceImpl.setExpedienteDAO(this.expedienteDAO);
            serviceImpl.setSolicitudDAO(this.solicitudDAO);

            serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
            serviceImpl.setNoteAdmeService(this.noteAdmeService);
            
            ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
            convocatoriaDTO.setIdConvocatoria(23);

            // ejecucion correcta desistimiento
            serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), 21,null, null);
        } catch (DocumentException e) {
            throw new ServiceException();
        }

    }

    @Test(expected = ServiceException.class)
    @Ignore
    public void testEnviarPortafirmasNoExisteDocumento() throws ServiceException {

        try {
            Document document = new Document();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, stream);
            // creamos mock para fica
            Integer cdFica = 678021;
            FicaService ficaService = mock(FicaService.class);

            FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
            ficaFicheroImpl.setDatosFichero(this.stream().toByteArray());

            when(ficaService.recuperar(cdFica)).thenReturn(null);

            // inyectamos los servicio necesarios para el proceso a ejecutar
            DocumentoPtfrServiceImpl serviceImpl = new DocumentoPtfrServiceImpl();

            serviceImpl.setDao(this.documentoPtfrDAO);
            serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
            serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
            serviceImpl.setExpedienteDAO(this.expedienteDAO);
            serviceImpl.setSolicitudDAO(this.solicitudDAO);

            serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
            serviceImpl.setNoteAdmeService(this.noteAdmeService);
            
            ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
            convocatoriaDTO.setIdConvocatoria(23);

            // ejecucion correcta desistimiento
            serviceImpl.enviarPortaFirmas(convocatoriaDTO, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), 21,null, null);
        } catch (DocumentException e) {
            throw new ServiceException();
        }

    }

    /**
     * Prueba el metodo actualizar la fecha de lectura NOTE de los documentos
     * enviados a portafirmas
     */
    @Test
    @Ignore
    public void testActualizarNoteExpedientes() throws ServiceException {

        // Cargamos objeto
        List<DocumentoPtfrDTO> lstDTO = this.service.buscarPorExpediente(1155);
        // No es nulo
        Assert.assertNotNull(lstDTO);
        // Actualizamosla fecha de lectura de todo los documentos
        Iterator<DocumentoPtfrDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            DocumentoPtfrDTO dto = new DocumentoPtfrDTO();
            DocumentoPtfrDTO objectoDTO = it.next();
            this.testAtributos(objectoDTO, Boolean.FALSE);
            objectoDTO.setFcLecturaNote(new Date());
            objectoDTO.setCdUsuModif(Constantes.Test.TEST_ADMIN);
            objectoDTO.setFcModifFila(new Date());

            DocumentoPtfr ent = this.sessionFactory.getCurrentSession().get(DocumentoPtfr.class,
                    objectoDTO.getIdDocumentoPtfr());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.guardar(objectoDTO);

            // La modificación es correcta
            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuModif());
        }

    }

    /**
     * Prueba el metodo actualizar la fecha de lectura NOTE de los documentos
     * enviados a portafirmas
     */
    @Test
    public void testBuscarPorExpediente() throws ServiceException {

        // Cargamos objeto
        List<DocumentoPtfrDTO> lstDTO = this.service.buscarPorExpediente(1155);
        // No es nulo
        Assert.assertNotNull(lstDTO);
        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        Iterator<DocumentoPtfrDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            DocumentoPtfrDTO objectoDTO = it.next();
            this.testAtributos(objectoDTO, Boolean.FALSE);
        }

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(DocumentoPtfrDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdDocumentoPtfr());
        // Assert.assertNotNull(dto.getCdFica());
        Assert.assertNotNull(dto.getDsReferenciaPtfr());
        Assert.assertNotNull(dto.getCdArchivoPtfr());
        Assert.assertNotNull(dto.getFcEnvioPtfr());
        Assert.assertNotNull(dto.getCdEstadoPtfr());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            // Assert.assertNotNull(dto.getFcRespuestaPtfr());
            // Assert.assertNotNull(dto.getCdNote());
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertNotNull(dto.getFcModifFila());
            // Assert.assertNotNull(dto.getCdUsuBaja());
            // Assert.assertNotNull(dto.getFcBajaFila());
        }
    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    private ByteArrayOutputStream stream() throws ServiceException {

        Document document = new Document();

        ByteArrayOutputStream stream;
        try {
            stream = new ByteArrayOutputStream();
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

            document.close();
        } catch (DocumentException e) {
            throw new ServiceException();
        }

        return stream;
    }

    @Test
    @Ignore
    public void consultarRespuestaPortafirmasTest() throws ServiceException {
        this.service.consultarRespuestaPortafirmasBatch();
    }
    
//    @Test
//    @Ignore
//    public void consultarRespuestaPortafirmas(Integer idConvocatoria, Integer idTramite, Integer idExpediente ) throws ServiceException {
//        this.service.consultarRespuestaPortafirmas(idConvocatoria,  idTramite,  idExpediente);
//    }
    

    @Test
    @Ignore
    public void consultarLecturaNotificacionSubsanacionTest() throws ServiceException {
        this.service.consultarLecturaNotificacionNoteBatch();
    }

}
