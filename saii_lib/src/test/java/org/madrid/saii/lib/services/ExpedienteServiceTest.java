package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
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
public class ExpedienteServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService service;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;

    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

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
    public void testBuscarPorId() {
        try {
            ExpedienteDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdExpediente());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarIdSimple() {
        try {
            ExpedienteDTO dto = this.service.buscarIdSimple(3, Boolean.TRUE);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdExpediente());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void cambiarEstado() {
        try {

            ExpedienteDTO dto = this.service.cambiarEstado(1, 19, "28631189F");

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdExpediente());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ExpedienteDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscar(0, -1, filtros, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    // /**
    // * Prueba el método generar
    // */
    // @Test
    // public void testGenerar() {
    // try {
    //
    // SolicitudDTO solicitudDTO = new SolicitudDTO();
    // solicitudDTO.setIdSolicitud(1);
    //
    // ConvocatoriaDTO convocatoriaDTO = convocatoriaService.buscarId(1);
    // solicitudDTO.setConvocatoriaDTO(convocatoriaDTO);
    //
    // InteresadoDTO interesadoDTO = interesadoService.buscarId(1);
    //
    // solicitudDTO.setInteresadoDTO(interesadoDTO);
    //
    // List<ConvocatoriaActDTO> lstConvocatoriaActDTO = convocatoriaActService
    // .buscarPorIdConvocatoria(1);
    //
    // ExpedienteDTO dto = service.generar(solicitudDTO,
    // solicitudDTO.getInteresadoDTO(),
    // lstConvocatoriaActDTO.get(0), Boolean.TRUE);
    //
    // // No es nulo
    // Assert.assertNotNull(dto);
    //
    // testAtributos(dto, Boolean.FALSE);
    //
    // } catch (final ServiceException e) {
    // log.error(e.getMessage());
    // fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
    // }
    // }

    /**
     * Prueba el metodo guardar
     */
    @Test
    @Ignore
    public void testGuardar() {
        try {
            // GUARDAR
            ExpedienteDTO dto = new ExpedienteDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setIdInteresado(1);
            ImpresoDTO impresoDTO = new ImpresoDTO();
            impresoDTO.setIdImpreso(1);

            BloqueDTO bloqueDTO = new BloqueDTO();
            bloqueDTO.setIdBloque(1);
            bloqueDTO.getIdTematica().setIdValor(116);
            bloqueDTO.getIdFormato().setIdValor(32);
            impresoDTO.getLstBloqueDTO().add(bloqueDTO);
            dto.setIdExpediente(2);
            dto.setCdExpediente("cod-exp");
            dto.setIdSolicitud(1);
            dto.setInteresadoDTO(interesadoDTO);
            dto.getEstadoDTO().setIdValor(1);
            dto.setImpresoDTO(impresoDTO);
            dto.setNmPaso(1);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());

            BloqueDTO b1 = new BloqueDTO();
            b1.setIdBloque(1);

            dto = this.service.guardar(dto);
            // No es nulo
            Assert.assertNotNull(dto);
            // Está relleno
            // TODO
            // Descomentar y borrar la siguiente linea cuando FirmaElectronica
            // este implementada
            // testAtributos(dto, Boolean.TRUE);
            this.testAtributos(dto, Boolean.FALSE);

            // Limpiamos la instancia
            Expediente ent = this.sessionFactory.getCurrentSession().get(Expediente.class,
                    dto.getIdExpediente());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdExpediente("cod-exp-mod");
            dto = this.service.guardar(dto);
            // La modificación es correcta
            Assert.assertEquals("cod-exp-mod", dto.getCdExpediente());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método filtrar con el parametro de entrada a null
     */
    @Test
    public void testFiltrarNull() {

        try {

            List<ExpedienteFiltrosDTO> lstDTO = this.service.filtrar(null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test borrado lógico
     */
    @Test
    public void borradoLogicoTest() {
        try {
            this.service.altaLogica(2);
            this.service.borradoLogico(2);

            ExpedienteDTO dto = this.service.buscarId(2);

            Assert.assertNotNull(dto.getFcBajaFila());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test alta lógica
     */
    @Test
    public void altaLogicaTest() {
        try {
            this.service.borradoLogico(2);
            this.service.altaLogica(2);

            ExpedienteDTO dto = this.service.buscarId(2);

            Assert.assertNull(dto.getFcBajaFila());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método filtrar
     */
    @Test
    public void testFiltrar() {

        try {
            ExpedienteFiltrosDTO expedienteFiltrosDTO = new ExpedienteFiltrosDTO();

            expedienteFiltrosDTO.setStrCdDocu("00000001R");

            List<ExpedienteFiltrosDTO> lstDTO = this.service.filtrar(expedienteFiltrosDTO);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteFiltrosDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteFiltrosDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test buscar solicitud interesado
     */
    @Test
    public void testBuscarSolicitudInteresado() {
        try {
            ExpedienteDTO dto = this.service.buscarSolicitudInteresado(1, 1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdExpediente());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test buscar Porsolicitud
     */
    @Test
    public void testBuscarPorSolicitud() {
        try {
            List<ExpedienteDTO> lstDTO = this.service.buscarParticipantesPorSolicitud(1, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /** Test guardarDatosExpediente */

    @Test
    public void testGuardarDatosExpediente() {

        ExpedienteDTO dto = new ExpedienteDTO();

        try {
            dto = this.service.buscarId(1);
            Expediente ent = this.sessionFactory.getCurrentSession().get(Expediente.class,
                    dto.getIdExpediente());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto.setCdExpediente("CODIGO PRUEBA TEST");

            dto = this.service.guardarDatosExpediente(dto);

            // No devuelve nulo
            Assert.assertNotNull(dto);

            // Guarda el cambio correctamente
            Assert.assertEquals("CODIGO PRUEBA TEST", dto.getCdExpediente());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test para obtener los documentos pendientes de subsanación
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @Test
    public void testObtenerFechaLimiteSubsanacionAdminSinFechaLecutaNote() throws ServiceException {

        String fecha = this.service.obtenerFechaLimiteSubsanacionAdmin(35, "ADMIN");

        Assert.assertTrue(!fecha.equals("dd/MM/yyyy"));

    }

    @Test
    public void testObtenerFechaLimiteSubsanacionAdminConFechaLecturaNote()
            throws ServiceException {

        String fecha = this.service.obtenerFechaLimiteSubsanacionAdmin(36, "ADMIN");

        Assert.assertTrue(!fecha.equals("dd/MM/yyyy"));

    }

    /**
     * Test para obtener los documentos pendientes de subsanación
     *
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @Test
    public void testObtenerFechaLimiteSubsanacionRequiSinFechaLecutaNote() throws ServiceException {

        String fecha = this.service.obtenerFechaLimiteSubsanacionRequi(37, "ADMIN");

        Assert.assertTrue(!fecha.equals("dd/MM/yyyy"));

    }

    @Test
    public void testObtenerFechaLimiteSubsanacionRequiConFechaLecutaNote() throws ServiceException {

        String fecha = this.service.obtenerFechaLimiteSubsanacionRequi(38, "ADMIN");

        Assert.assertTrue(!fecha.equals("dd/MM/yyyy"));

    }

    // /**
    // * Test para obtener los documentos pendientes de subsanación
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // public void testObtenerDocumentosSubsanar() throws ServiceException {
    //
    // try {
    // Document document = new Document();
    //
    // ByteArrayOutputStream stream = new ByteArrayOutputStream();
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
    // // creamos mock para fica
    // Integer cdFica = 678021;
    // FicaService ficaService = mock(FicaService.class);
    //
    // FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
    // ficaFicheroImpl.setDatosFichero(stream.toByteArray());
    // ficaFicheroImpl.setNombreFichero("prueba.pdf");
    //
    // when(ficaService.recuperar(cdFica)).thenReturn(ficaFicheroImpl);
    //
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    //
    // serviceImpl.setFicaService(ficaService);
    //
    // List<ExpedienteDocDTO> listaDto =
    // serviceImpl.obtenerDocumentosSubsanar(8);
    //
    // Assert.assertNotNull(listaDto);
    // Assert.assertNotEquals(0, listaDto.size());
    // } catch (DocumentException e) {
    // throw new ServiceException();
    // }
    // }

    // /**
    // * Test para obtener los requisitos pendientes de subsanación
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // public void testObtenerRequisitosSubsanar() throws ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 678439;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // // doNothing().when(eregAdmeService).anexarFicheroAnexo("122121", null,
    // // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    // serviceImpl.setExpedienteReqDAO(this.expedienteReqDAO);
    // serviceImpl.setDocumentoPtfrDAO(this.documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
    //
    // List<ExpedienteReqDTO> listaDto =
    // serviceImpl.obtenerRequisitosSubsanar(52);
    //
    // Assert.assertNotNull(listaDto);
    // Assert.assertNotEquals(0, listaDto.size());
    //
    // }

    // /**
    // * Test para finalizar subsanación de documentos
    // *
    // * @throws ServiceException Excepción ocurrido en el servicio
    // */
    // @Test
    // @Ignore
    // public void testFinalizarSubsanacion() throws ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 5657640;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // doNothing().when(eregAdmeService).anexarFicheroAnexo("122121", null,
    // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    // serviceImpl.setDocumentoPtfrDAO(this.documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
    //
    // // ejecutamos el proceso
    // ExpedienteDTO dto = serviceImpl.finalizarSubsanacionAdmin(7);
    //
    // Assert.assertEquals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION,
    // dto.getEstadoDTO().getIdValor());
    // }
    //
    // @Test(expected = ServiceException.class)
    // public void testFinalizarSubsanacionErrorObtenerDocumento() throws
    // ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 5657640;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // doNothing().when(eregAdmeService).anexarFicheroAnexo("122121", null,
    // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // DocumentoPtfrDAO documentoPtfrDAO = mock(DocumentoPtfrDAO.class);
    // when(documentoPtfrDAO.find(3)).thenReturn(null);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    // serviceImpl.setDocumentoPtfrDAO(documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
    //
    // // ejecutamos el proceso
    // serviceImpl.finalizarSubsanacionAdmin(7);
    // }

    // @Test(expected = ServiceException.class)
    // public void testFinalizarSubsanacionErrorObtenerDocumentoFirmado() throws
    // ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 5657640;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // doNothing().when(eregAdmeService).anexarFicheroAnexo("2221", null,
    // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // PtfrAdmeService ptfrAdmeService = mock(PtfrAdmeService.class);
    // when(ptfrAdmeService.recuperarDocumentoFirmado("06/004748.9/21",
    // "5659307"))
    // .thenReturn(null);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
    // serviceImpl.setDocumentoPtfrDAO(this.documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(ptfrAdmeService);
    //
    // // ejecutamos el proceso
    // serviceImpl.finalizarSubsanacionAdmin(7);
    //
    // }

    // @Test(expected = ServiceException.class)
    // public void testFinalizarSubsanacionSinDocumentos() throws
    // ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 5657640;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // doNothing().when(eregAdmeService).anexarFicheroAnexo("122121", null,
    // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // ExpedienteDocDAO expedienteDocDAO = mock(ExpedienteDocDAO.class);
    //
    // List<Integer> listaIdEstado = new ArrayList<>();
    // listaIdEstado.add(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
    // listaIdEstado.add(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
    //
    // List<AtlasFilter> filtrosDocs = new ArrayList<>();
    // filtrosDocs.add(new
    // AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, 7,
    // Operador.EQUALS));
    // filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA,
    // Operador.NULL));
    // filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
    // listaIdEstado,
    // Operador.IN));
    // filtrosDocs
    // .add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR,
    // Operador.NOT_NULL));
    //
    // when(expedienteDocDAO.find(0, -1, filtrosDocs, null)).thenReturn(null);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(expedienteDocDAO);
    // serviceImpl.setDocumentoPtfrDAO(this.documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
    //
    // // ejecutamos el proceso
    // serviceImpl.finalizarSubsanacionAdmin(7);
    // }

    // @Test(expected = ServiceException.class)
    // @Ignore
    // public void testFinalizarSubsanacionSinDocumentosVer() throws
    // ServiceException {
    //
    // // creamos mock para fica
    // Integer cdFica = 5657640;
    // FicaService ficaService = mock(FicaService.class);
    // when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
    //
    // // creamos mock para ereg
    // EregAdmeService eregAdmeService = mock(EregAdmeService.class);
    // doNothing().when(eregAdmeService).anexarFicheroAnexo("122121", null,
    // Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
    //
    // ExpedienteDocDAO expedienteDocDAOMock = mock(ExpedienteDocDAO.class);
    //
    // List<Integer> listaIdEstado = new ArrayList<>();
    // listaIdEstado.add(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
    // listaIdEstado.add(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
    //
    // List<AtlasFilter> filtrosDocs = new ArrayList<>();
    // filtrosDocs.add(new
    // AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, 7,
    // Operador.EQUALS));
    // filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA,
    // Operador.NULL));
    // filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
    // listaIdEstado,
    // Operador.IN));
    // filtrosDocs
    // .add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR,
    // Operador.NOT_NULL));
    //
    // when(expedienteDocDAOMock.find(0, -1, filtrosDocs, null))
    // .thenReturn(this.expedienteDocDAO.find(0, -1, filtrosDocs, null));
    //
    // List<AtlasFilter> filtrosDocs2 = new ArrayList<>();
    // filtrosDocs2
    // .add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR, 3,
    // Operador.EQUALS));
    // filtrosDocs2.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA,
    // Operador.NULL));
    //
    // when(expedienteDocDAOMock.find(0, -1, filtrosDocs2,
    // null)).thenReturn(null);
    //
    // // inyectamos los servicio necesarios para el proceso a ejecutar
    // ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
    // serviceImpl.setDao(this.expedienteDAO);
    // serviceImpl.setExpedienteDocDAO(expedienteDocDAOMock);
    // serviceImpl.setDocumentoPtfrDAO(this.documentoPtfrDAO);
    // serviceImpl.setFicaService(ficaService);
    // serviceImpl.setEregAdmeService(eregAdmeService);
    // serviceImpl.setPtfrAdmeService(this.ptfrAdmeService);
    //
    // // ejecutamos el proceso
    // serviceImpl.finalizarSubsanacionAdmin(7);
    // }

    @Test
    public void testActualizarEstadoExpedienteRevisionDocumento() throws ServiceException {
        // ExpedienteDTO dto =
        // service.actualizarEstadoExpedienteRevisionDocumento(1202);
        // Assert.assertNotNull(dto);

        ExpedienteDTO dto2 = this.service.actualizarEstadoExpedienteRevisionDocumento(12);
        Assert.assertNotNull(dto2);

        ExpedienteDTO dto3 = this.service.actualizarEstadoExpedienteRevisionDocumento(13);
        Assert.assertNotNull(dto3);
    }

    /**
     * Test obtenerExpedienteSubsanacion
     *
     */
    @Test
    @Ignore
    public void testObtenerExpedienteSubsanacion() {
        try {
            String numIdentidad = "00000001R";
            List<ExpedienteDTO> listaExpediente = this.service
                    .obtenerExpedienteSubsanacion(numIdentidad);
            // No es nulo
            Assert.assertNotNull(listaExpediente);
            // Está relleno
            Assert.assertFalse(listaExpediente.isEmpty());
            // Comprobamos los atributos obligatorios
            Iterator<ExpedienteDTO> it = listaExpediente.iterator();
            while (it.hasNext()) {
                ExpedienteDTO dto = it.next();
                Assert.assertNotNull(dto.getDocumentoPtfrDTO().getIdDocumentoPtfr());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test ObtenerListaEstadosExpediente
     *
     */
    @Test
    public void testObtenerListaEstadosExpediente() throws ServiceException {

        List<CatalogosValoresDTO> lstDTO = this.service.obtenerListaEstadosExpediente();

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

    }

    /**
     * Test ObtenerListaEstadosExpedienteBenef
     *
     */
    @Test
    public void testObtenerListaEstadosExpedienteBenef() throws ServiceException {

        List<CatalogosValoresDTO> lstDTO = this.service.obtenerListaEstadosExpedienteBenef();

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

    }

    /**
     * Test crear
     *
     */
    @Test
    @Ignore
    public void testCrearInteresadoExiste() throws ServiceException {

        ExpedienteDTO dto = new ExpedienteDTO();
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setIdInteresado(1);
        interesadoDTO.setCdNumIden("00000000X");
        dto.setInteresadoDTO(interesadoDTO);
        ImpresoDTO impresoDTO = new ImpresoDTO();
        impresoDTO.setIdImpreso(1);
        ConvocatoriaActDTO convocatoriaActDTO = new ConvocatoriaActDTO();
        convocatoriaActDTO.setIdActor(1);
        dto.setConvocatoriaActDTO(convocatoriaActDTO);
        dto.setImpresoDTO(impresoDTO);
        dto.setIdSolicitud(1);

        dto = this.service.crear(dto);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno
        Assert.assertNotNull(dto.getIdExpediente());

    }

    /**
     * Test crear
     *
     */
    @Test
    public void testCrearExpedienteRequisito() throws ServiceException {

        // consultade dto solicitud
        SolicitudDTO solicitud = this.solicitudService.buscarId(51);

        CatalogosValoresDTO area = new CatalogosValoresDTO();
        area.setIdValor(12);

        ConvocatoriaSubmodDTO convocatoriaSubmod = new ConvocatoriaSubmodDTO();
        convocatoriaSubmod.setIdConvocatoriaSubmod(100);

        solicitud.getConvocatoriaAreaDTO().setAreaDTO(area);
        solicitud.setConvocatoriaSubmodDTO(convocatoriaSubmod);

        // solicitud.setConvocatoriaAreaDTO(convocatoriaArea);

        // Limpiamos la instancia
        Solicitud ent = this.sessionFactory.getCurrentSession().get(Solicitud.class,
                solicitud.getIdSolicitud());
        this.sessionFactory.getCurrentSession().evict(ent);

        // consulta de dto interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(solicitud.getInteresadoDTO().getIdInteresado());// 1

        // consulta de dto convocatoria act
        ConvocatoriaActDTO convocatoriaAct = this.convocatoriaActService.buscarId(10);

        // consulta de dto expediente
        ExpedienteDTO expediente = this.service.buscarId(24);

        expediente = this.service.generar(solicitud, interesado, convocatoriaAct, true, null);

        // No es nulo
        Assert.assertNotNull(expediente);

        // Está relleno
        Assert.assertNotNull(expediente.getLstExpedienteReqDTO());

    }

    /**
     * Test crear
     *
     */
    @Test
    @Ignore
    public void testCrearInteresadoNuevo() throws ServiceException {

        ExpedienteDTO dto = new ExpedienteDTO();
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setCdNumIden("37396655N");
        dto.setInteresadoDTO(interesadoDTO);
        ImpresoDTO impresoDTO = new ImpresoDTO();
        impresoDTO.setIdImpreso(1);
        ConvocatoriaActDTO convocatoriaActDTO = new ConvocatoriaActDTO();
        convocatoriaActDTO.setIdActor(1);
        impresoDTO.setConvocatoriaActDTO(convocatoriaActDTO);
        dto.setImpresoDTO(impresoDTO);
        dto.setIdSolicitud(1);
        dto.setConvocatoriaActDTO(convocatoriaActDTO);

        dto = this.service.crear(dto);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno
        Assert.assertNotNull(dto.getIdExpediente());

    }

    /**
     * Test actualizarEstadoExpediente 01 [documento=REVISION_OK and
     * expediente=REVISION_OK]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso01() throws ServiceException {

        // Test actualizarEstadoInternoRevisionOkDocumento y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRevisionOkDocumento(30,null);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.REVISION_OK));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.REVISION_OK));

    }

    /**
     * Test actualizarEstadoExpediente 02 [requisito=REVISION_OK and
     * expediente=REVISION_OK]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso02() throws ServiceException {

        // Test actualizarEstadoInternoRevisionOkRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRevisionOkRequisito(26);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.REVISION_OK));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.REVISION_OK));

    }

    /**
     * Test actualizarEstadoExpediente 03 [documento=REVISION_OK and
     * expediente=EN_TRAMITACION]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso03() throws ServiceException {

        // Test actualizarEstadoInternoRevisionOkDocumento y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRevisionOkDocumento(31, null);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.REVISION_OK));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.EN_TRAMITACION));

    }

    /**
     * Test actualizarEstadoExpediente 04 [requisito=REVISION_OK and
     * expediente=EN_TRAMITACION]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso04() throws ServiceException {

        // Test actualizarEstadoInternoRevisionOkRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRevisionOkRequisito(32);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.REVISION_OK));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.EN_TRAMITACION));

    }

    /**
     * Test actualizarEstadoExpediente 05 [requisito=DESISTIDO and
     * expediente=DESISTIDO]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso05() throws ServiceException {

        // Test actualizarEstadoExpedienteRevisionDocumento y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoExpedienteRevisionDocumento(33);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.DESISTIDO));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.DESISTIDO));

    }

    /**
     * Test actualizarEstadoExpediente 06 [requisito=EXCLUIDO and
     * expediente=EXCLUIDO]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso06() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(34);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.EXCLUIDO));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.EXCLUIDO));

    }

    /**
     * Test actualizarEstadoExpediente 07 [documento=PENDIENTE_REVISION and
     * expediente=PENDIENTE_REVISION]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso07() throws ServiceException {

        // Test actualizarEstadoExpedienteRevisionDocumento y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoExpedienteRevisionDocumento(39);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

    }

    /**
     * Test actualizarEstadoExpediente 08 [requisito=PENDIENTE_REQUERIMIENTO and
     * expediente=PENDIENTE_REQUERIMIENTO]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso08() throws ServiceException {

        // Test actualizarEstadoInternoPendienteRequisito
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(27);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

    }

    /**
     * Test actualizarEstadoExpediente [requisito=PENDIENTE_REQUERIMIENTO and
     * expediente=PENDIENTE_SUBSANACION_REQUISITOS]
     *
     */
    @Test
    public void testVerificarEstadoExpedienteCaso09() throws ServiceException {

        // Test actualizarEstadoInternoPendienteRequisito
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(40);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 01 [requisito=EXCLUIDO and
     * expediente=EXCLUIDO]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso01() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(41);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno requisito
        Assert.assertTrue(dto.getEstadoRevRequiDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.EN_TRAMITACION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 02 [documento=EN_TRAMITACION
     * and expediente=PENDIENTE_REQUERIMIENTO]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso02() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(42);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.EN_TRAMITACION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 03 [documento=EN_TRAMITACION
     * and expediente=PENDIENTE_SUBSANACION_REQUISITOS]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso03() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(43);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.EN_TRAMITACION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 04 [documento=EN_TRAMITACION
     * and expediente=PENDIENTE_REVISION_SUBSANACION]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso04() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(44);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.EN_TRAMITACION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 05
     * [documento=PENDIENTE_REVISION and expediente=PENDIENTE_REQUERIMIENTO]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso05() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(45);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 06
     * [documento=PENDIENTE_REVISION and
     * expediente=PENDIENTE_SUBSANACION_REQUISITOS]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso06() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(46);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 07
     * [documento=PENDIENTE_REVISION and
     * expediente=PENDIENTE_REVISION_SUBSANACION]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso07() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(47);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 08
     * [documento=PENDIENTE_REQUERIMIENTO and
     * expediente=PENDIENTE_SUBSANACION_REQUISITOS]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso08() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(48);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 09
     * [documento=PENDIENTE_REQUERIMIENTO and
     * expediente=PENDIENTE_REVISION_SUBSANACION]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso09() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(49);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 10
     * [documento=PENDIENTE_SUBSANACION_REQUISITOS and
     * expediente=PENDIENTE_REVISION_SUBSANACION]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso10() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(50);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 11
     * [documento=PENDIENTE_SUBSANACION_REQUISITOS and expediente=EXCLUIDO]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso11() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(51);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.EXCLUIDO));

    }

    /**
     * Test verificarEstadoRevAdminRequiMayor 12
     * [documento=PENDIENTE_SUBSANACION_REQUISITOS and expediente=EXCLUIDO]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso12() throws ServiceException {

        // Test actualizarEstadoInternoRevisionOkDocumento y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoExpedienteRevisionDocumento(56);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

        // Estado interno expediente
        Assert.assertTrue(
                dto.getEstadoDTO().getIdValor().equals(Constantes.EstadoExpediente.EXCLUIDO));

    }

    /**
     * Test testVerificarEstadoRevAdminRequiMayor 12
     * [documento=PENDIENTE_SUBSANACION_REQUISITOS and
     * expediente=PENDIENTE_REVISION_SUBSANACION]
     *
     */
    @Test
    public void testVerificarEstadoRevAdminRequiMayorCaso13() throws ServiceException {

        // Test actualizarEstadoInternoRequisito y expediente
        ExpedienteDTO dto = this.service.actualizarEstadoInternoRequisito(55);

        // No es nulo
        Assert.assertNotNull(dto);

        // Estado interno documento
        Assert.assertTrue(dto.getEstadoRevAdminDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

        // Estado interno expediente
        Assert.assertTrue(dto.getEstadoDTO().getIdValor()
                .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));

    }

    /**
     * método para aprobar la evaluación de todos los informes de coordinadores
     */
    @Test
    public void tesAprobarEvaluacion() {
        // ExpedienteDTO dto = new ExpedienteDTO();
        try {

            ExpedienteDTO dto = this.service.aprobarEvaluacion(67);
            // No devuelve nulo
            Assert.assertNotNull(dto);
            Assert.assertEquals(Constantes.EstadoExpediente.EVALUADO_OK,
                    dto.getEstadoRevEvalDTO().getIdValor());
        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * método para capturar la excepción de error en el método aprobarEvaluacion
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void tesErrorAprobarEvaluacion() throws ServiceException {

        this.service.aprobarEvaluacion(null);

    }

    /**
     * método para rechazar la evaluación de todos los informes de coordinadores
     * a "cerrado gestor"
     */
    @Test
    public void tesRechazarEvaluacion() {

        try {
            ExpedienteDTO dto = this.service.rechazarEvaluacion(66);
            // No devuelve nulo
            Assert.assertNotNull(dto);
            Assert.assertEquals(Constantes.EstadoExpediente.EVALUADO_KO,
                    dto.getEstadoRevEvalDTO().getIdValor());
        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * método para capturar la excepción del método rechazarEvaluación
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void tesErrorRechazarRechazarvaluacion() throws ServiceException {
        this.service.rechazarEvaluacion(null);
    }

    /**
     * método para capturar la excepción de error en el método aprobarEvaluacion
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void tesErrorComprobarResolucionDefinitiva() throws ServiceException {

        this.service.comprobarResolucionDefinitiva(null, null);

    }

    /**
     * método para comprobar el tipo documento si es resolución definitiva.
     * cuando el expediente tiene como actor "Solicitante"
     */
    @Test
    public void testComprobarResolucionDefinitiva() {
        try {
            ExpedienteDTO dto = this.service.comprobarResolucionDefinitiva(77, null);
            Assert.assertNotNull(dto);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * cuando el expediente no tiene como actor "Solicitante"
     */
    @Test
    public void testComprobarResolucionDefinitiva2() {
        try {
            ExpedienteDTO dto = this.service.comprobarResolucionDefinitiva(78, null);
            Assert.assertNotNull(dto);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ExpedienteDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdExpediente());
        Assert.assertNotNull(dto.getIdSolicitud());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdExpediente()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());
        Assert.assertNotNull(dto.getEstadoDTO().getIdValor());
        Assert.assertNotNull(dto.getEstadoBeneficiarioDTO().getIdValor());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertNotNull(dto.getFcFirma());
            Assert.assertNotNull(dto.getNmPaso());
            Assert.assertNotNull(dto.getCdEreg());
            Assert.assertNotNull(dto.getDsDestinoEreg());
            Assert.assertNotNull(dto.getCdSiex());

            // Impreso
            Assert.assertNotNull(dto.getImpresoDTO().getIdImpreso());

            // Bloques del impreso
            List<BloqueDTO> lstBloqueDTO = dto.getImpresoDTO().getLstBloqueDTO();
            Assert.assertNotNull(lstBloqueDTO);

            BloqueDTO bloqueDTO = new BloqueDTO();
            // Recorremos Bloques
            Iterator<BloqueDTO> itB = lstBloqueDTO.iterator();
            while (itB.hasNext()) {
                bloqueDTO = itB.next();
                Assert.assertNotNull(bloqueDTO);
                Assert.assertNotNull(bloqueDTO.getIdBloque());
                Assert.assertNotNull(bloqueDTO.getNmOrden());

                // Componentes del bloque
                List<BloqueComponenteDTO> lstBloqueComponenteDTO = bloqueDTO
                        .getLstBloqueComponenteDTO();
                Assert.assertNotNull(lstBloqueComponenteDTO);
                Assert.assertTrue(!lstBloqueComponenteDTO.isEmpty());

                // Recorremos Componentes
                Iterator<BloqueComponenteDTO> itC = lstBloqueComponenteDTO.iterator();
                while (itC.hasNext()) {
                    BloqueComponenteDTO bloqueCompDTO = itC.next();
                    Assert.assertNotNull(bloqueCompDTO);
                    Assert.assertNotNull(bloqueCompDTO.getComponenteDTO().getIdComponente());
                    Assert.assertNotNull(bloqueCompDTO.getComponenteDTO().getDsNombre());
                }
            }
        }

    }

    /**
     * método para capturar la excepción del método guardar alegación
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void tesErrorGuardarAlegacion() throws ServiceException {
        this.service.guardarAlegacion(null, "00000001R","00000001R","00000001R");
    }

    /**
     * método para capturar la excepción del método ERROR guardar alegación
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void tesErrorGuardarAlegacion2() throws ServiceException {
        ExpedienteDocDTO expedienteDocDTO = new ExpedienteDocDTO();
        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setIdExpediente(69);
        expedienteDocDTO.setExpedienteDTO(expediente);
        this.service.guardarAlegacion(expedienteDocDTO, "00000001R","00000001R","00000001R");
    }

    /**
     * método para guardar Alegacion
     */
    @Test
    public void testGuardarAlegacion() {

        try {

            // creamos mock para fica
            FicaService ficaService = mock(FicaService.class);

            FicaFicheroImpl ficaFicheroImpl = new FicaFicheroImpl();
            ficaFicheroImpl.setDatosFichero(null);
            ficaFicheroImpl.setNombreFichero("prueba.pdf");

            when(ficaService.recuperar(678439)).thenReturn(ficaFicheroImpl);
            when(ficaService.recuperarMetadatos(678439)).thenReturn(ficaFicheroImpl);

            // creamos mock de ereg
            EregAdmeService eregAdmeService = mock(EregAdmeService.class);
            when(eregAdmeService.anexarFicheroAnexo(any(), any(), any())).thenReturn("154861");

            ExpedienteServiceImpl serviceImpl = new ExpedienteServiceImpl();
            serviceImpl.setFicaService(ficaService);
            serviceImpl.setEregAdmeService(eregAdmeService);
            serviceImpl.setDao(this.expedienteDAO);
            serviceImpl.setExpedienteDocDAO(this.expedienteDocDAO);
            serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
            serviceImpl.setExpedienteHistDAO(this.expedienteHistDAO);

//            Integer idExpediente = 70;
//            String cdNumIden = "00000001R";
            ExpedienteDTO dto = new ExpedienteDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setCdNumIden("00000001R");

            dto.setIdExpediente(70);
            dto.setInteresadoDTO(interesadoDTO);

            ExpedienteDocDTO expedienteDocDTO = new ExpedienteDocDTO();
            
            ExpedienteDTO expediente = new ExpedienteDTO();
            expediente.setIdExpediente(dto.getIdExpediente());
            expedienteDocDTO.setExpedienteDTO(expediente);
            
            RegistroEregDTO eregDTO = serviceImpl.guardarAlegacion(expedienteDocDTO,
                    dto.getInteresadoDTO().getCdNumIden(),dto.getInteresadoDTO().getCdNumIden(),
                    dto.getInteresadoDTO().getCdNumIden());

            Assert.assertNotNull(eregDTO);
            Assert.assertNotNull(Constantes.AfirmacionNegacion.S, dto.getItAlegaciones());
            Assert.assertEquals("154861", eregDTO.getCdAnexoEreg());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * actualiza el itAlegacion a N si la fecha alegación es menor a la fecha
     * actual
     */
    @Test
    public void testActualizarExpedienteConFcAlegacionNoVencidaBatch() {

        try {

            this.service.actualizarExpedienteConFcAlegacionNoVencidaBatch();

            ExpedienteDTO dto = this.service.buscarId(80);
            Assert.assertEquals(Constantes.AfirmacionNegacion.N, dto.getItAlegaciones());

            log.info("\n==========> itAlegaciones es: " + dto.getItAlegaciones()
                    + " <=================\n");

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Actualiza el estado del espediente a propuesta de resolución
     */
    @Test
    public void testActualizarEstadoExpedienteBatch() {

        try {

            this.service.actualizarEstadoExpedienteAlegacionBatch();

            ExpedienteDTO dto = this.service.buscarId(81);
            Assert.assertEquals(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION,
                    dto.getEstadoDTO().getIdValor());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ExpedienteFiltrosDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdExpediente());
        Assert.assertNotNull(dto.getIdAreaCientifica());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsAreaCientifica()));
        Assert.assertNotNull(dto.getEstadoDTO());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getEstadoDTO().getDsValor()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsTituloConvocatoria()));
        Assert.assertNotNull(dto.getNmAnoConvocatoria());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdAcronimoConvocatoria()));

        if (Boolean.TRUE.equals(bRelleno)) {

        }

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    @Test
    public void testRellenarValoresRelacionables() throws ServiceException {

        ExpedienteDTO dto = this.service.buscarIdSimple(2, Boolean.TRUE);
        // Obligatorios
        Assert.assertNotNull(dto.getIdExpediente());
        Assert.assertNotNull(dto.getValoresRelacionables());
        Assert.assertTrue(dto.getValoresRelacionables().size() > 0);

    }

    @Test
    public void testFinalizarSubsanacionRequi() throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDoc = new ArrayList<>();
        ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setIdExpediente(52);
        expedienteDoc.setExpedienteDTO(expediente);
        lstExpedienteDoc.add(expedienteDoc);
        RegistroEregDTO dto = this.service.finalizarSubsanacion(lstExpedienteDoc, false, "ADMIN", "ADMIN", "ADMIN");
        Assert.assertNotNull(dto.getIdExpedienteDoc());
    }

    @Test(expected = ServiceException.class)
    public void testFinalizarSubsanacionRequiSinIdExpediente() throws ServiceException {
        this.service.finalizarSubsanacion(null, false, "ADMIN","ADMIN", "ADMIN");
    }

    @Test(expected = ServiceException.class)
    public void testFinalizarSubsanacionRequiSinRequisitoSubsanado() throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDoc = new ArrayList<>();
        ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setIdExpediente(53);
        expedienteDoc.setExpedienteDTO(expediente);
        lstExpedienteDoc.add(expedienteDoc);
        this.service.finalizarSubsanacion(lstExpedienteDoc, false, "ADMIN", "ADMIN", "ADMIN");
    }

    @Test(expected = ServiceException.class)
    public void testFinalizarSubsanacionRequiEstadoPendienteRevisionSubsanacion()
            throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDoc = new ArrayList<>();
        ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setIdExpediente(54);
        expedienteDoc.setExpedienteDTO(expediente);
        lstExpedienteDoc.add(expedienteDoc);
        this.service.finalizarSubsanacion(lstExpedienteDoc, false, "ADMIN", "ADMIN", "ADMIN");
    }
    
    
    
    /**
     * 
     */
    @Test
    public void testActualizarEstadoExpedienteContratoBatch() throws ServiceException {

        List<Integer> dto =  this.expedienteDAO.listaExpedienteConContrato();
        Assert.assertNotNull(dto);

    }
    
    
    

}
