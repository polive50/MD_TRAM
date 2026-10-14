package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SolicitudFiltrosDTO;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test SolicitudService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class SolicitudServiceTest extends AtlasAbstractBaseTest {

    private String cdDocumentoTest = "00000001R";

    /**
     * Servicio solicitud
     */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService service;

    /**
     * Servicio solicitud
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
    
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
     * Prueba el método buscarId
     */
    @Test
    public void testBuscarId() throws ServiceException {

        SolicitudDTO dto = this.service.buscarId(1);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno
        Assert.assertNotNull(dto.getIdSolicitud());

        // Comprobamos los atributos obligatorios
        this.testAtributos(dto, Boolean.TRUE);

    }

    /**
     * Prueba el método buscarId ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testBuscarIdError() throws ServiceException {

        this.service.buscarId(null);
    }

    @Test
    public void testFiltrarCompleto() throws ServiceException {

        List<SolicitudDTO> listaSol = new ArrayList<>();

        SolicitudFiltrosDTO solicitudFiltros = new SolicitudFiltrosDTO();

        solicitudFiltros.setAcronimo("FIS-INV");
        solicitudFiltros.setDescripcion("convocatoria");
        solicitudFiltros.setIdEstado(16);
        solicitudFiltros.setStrCdDocuSol(this.cdDocumentoTest);

        Assert.assertEquals("FIS-INV", solicitudFiltros.getAcronimo());
        Assert.assertEquals("convocatoria", solicitudFiltros.getDescripcion());

        // Hacemos el filtro de las solicitudes
        listaSol = this.service.filtrar(solicitudFiltros);

        Assert.assertNotNull(listaSol);
        // Comprobamos que la lista no esta vacía
        Assert.assertFalse(listaSol.isEmpty());

    }

    @Test
    public void testFiltrarAcronimo() throws ServiceException {

        List<SolicitudDTO> listaSol = new ArrayList<>();

        SolicitudFiltrosDTO solicitudFiltros = new SolicitudFiltrosDTO();

        solicitudFiltros.setStrCdDocuSol(this.cdDocumentoTest);

        solicitudFiltros.setAcronimo("FIS-INV");

        Assert.assertEquals("FIS-INV", solicitudFiltros.getAcronimo());
        // Hacemos el filtro de las solicitudes
        listaSol = this.service.filtrar(solicitudFiltros);

        Assert.assertNotNull(listaSol);
        // Comprobamos que la lista no esta vacía
        Assert.assertFalse(listaSol.isEmpty());

    }

    @Test
    public void testFiltrarDescripcion() throws ServiceException {

        List<SolicitudDTO> listaSol = new ArrayList<>();

        SolicitudFiltrosDTO solicitudFiltros = new SolicitudFiltrosDTO();

        solicitudFiltros.setStrCdDocuSol(this.cdDocumentoTest);
        // La busqueda de la descripcion es aproximada
        solicitudFiltros.setDescripcion("convocatoria");

        // Hacemos el filtro de las solicitudes
        listaSol = this.service.filtrar(solicitudFiltros);

        Assert.assertNotNull(listaSol);

        // Comprobamos que la lista no esta vacía
        Assert.assertFalse(listaSol.isEmpty());

    }

    @Test
    public void testFiltrarEstado() throws ServiceException {

        List<SolicitudDTO> listaSol = new ArrayList<>();

        SolicitudFiltrosDTO solicitudFiltros = new SolicitudFiltrosDTO();

        solicitudFiltros.setStrCdDocuSol(this.cdDocumentoTest);
        solicitudFiltros.setIdEstado(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

        // Hacemos el filtro de las solicitudes
        listaSol = this.service.filtrar(solicitudFiltros);

        Assert.assertNotNull(listaSol);
        // Comprobamos que la lista no esta vacía
        Assert.assertFalse(listaSol.isEmpty());

    }

    /**
     * Prueba el método crearSolicitud Error1 ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testCrearSolicitudError1() throws ServiceException {

        // Prueba sin datos de entrada
        SolicitudDTO dto = new SolicitudDTO();

        InteresadoDTO interesadoDTO = new InteresadoDTO();

        this.service.crearSolicitud(dto, interesadoDTO);
    }

    /**
     * Prueba el metodo crearSolicitud
     */
    @Test
    public void testCrearSolicitud() {
        try {

            // GUARDAR
            SolicitudDTO dto = new SolicitudDTO();

            ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            convocatoriaDTO.setIdConvocatoria(1);
            interesadoDTO.setIdInteresado(1);
            interesadoDTO.setCdNumIden("12132");
            interesadoDTO.setDsNombre("Pepe");
            interesadoDTO.setDsApellido1("Matega");
            interesadoDTO.getTipoActorDTO().setNmConstante(Constantes.Identificacion.FISICA);

            CatalogosValoresDTO tipoActorDTO = new CatalogosValoresDTO();
            tipoActorDTO.setIdValor(907);

            dto.setTipoActorDTO(tipoActorDTO);
            dto.setConvocatoriaDTO(convocatoriaDTO);
            dto.setInteresadoDTO(interesadoDTO);

            ConvocatoriaAreaDTO convocatoriaAreaDTO = new ConvocatoriaAreaDTO();
            convocatoriaAreaDTO.setIdConvocatoriaArea(15);
            convocatoriaAreaDTO.setAreaDTO(new CatalogosValoresDTO(951));
            dto.setConvocatoriaAreaDTO(convocatoriaAreaDTO);

            ConvocatoriaSubmodDTO convocatoriaSubmodDTO = new ConvocatoriaSubmodDTO();
            convocatoriaSubmodDTO.setIdConvocatoriaSubmod(26);
            dto.setConvocatoriaSubmodDTO(convocatoriaSubmodDTO);

            dto.setIdSolicitud(1);

            Assert.assertEquals("12132", dto.getInteresadoDTO().getCdNumIden());
            dto = this.service.crearSolicitud(dto, null);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Solicitud ent = this.sessionFactory.getCurrentSession().get(Solicitud.class,
                    dto.getIdSolicitud());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsTipoOrganismo("Prueba Test Mod");
            dto = this.service.crearSolicitud(dto, dto.getInteresadoDTO());

            // La modificación es correcta
            Assert.assertEquals("Prueba Test Mod", dto.getDsTipoOrganismo());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo buscarIniciada
     */
    @Test
    public void buscarIniciada() {

        SolicitudDTO dto;
        try {

            SolicitudDTO solicitud = new SolicitudDTO();
            solicitud.getInteresadoDTO().setIdInteresado(1);
            solicitud.getInteresadoDTO().setCdNumIden(this.cdDocumentoTest);
            solicitud.getConvocatoriaDTO().setIdConvocatoria(1);

            dto = this.service.buscarSolicitudIniciada(solicitud);

            // No es nulo
            Assert.assertNotNull(dto);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * TODO Error con la firma electronica no implementada ** Test Presentar
     * solicitud
     */
    @Test
    @Ignore
    public void testPresentar() {
        try {

            SolicitudDTO dto = new SolicitudDTO();

            ConvocatoriaDTO convocatoriaDTO = new ConvocatoriaDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            UnidadAdminDTO unidadAdminDTO = new UnidadAdminDTO();
            convocatoriaDTO.setIdConvocatoria(1);
            unidadAdminDTO.setIdUnidadAdmin(1697);
            convocatoriaDTO.setIdAreaAdministrativa(unidadAdminDTO);
            interesadoDTO.setIdInteresado(1);
            interesadoDTO.setCdNumIden("123456");
            interesadoDTO.setDsNombre("Luis");
            interesadoDTO.setDsApellido1("Pep");
            interesadoDTO.getTipoActorDTO().setNmConstante(Constantes.TipoActor.SOLICITANTE);

            dto.setConvocatoriaDTO(convocatoriaDTO);
            dto.setInteresadoDTO(interesadoDTO);

            RegistroEregDTO registroEregDTO = this.service.presentar(dto);

            // No es nulo
            Assert.assertNotNull(registroEregDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test Obtener Lista Valores Actor "persona fisica"
     */
    @Test
    public void testObtenerListaValoresActorFisica() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service
                    .obtenerListaValoresActor(Constantes.Identificacion.DS_FISICA);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                Assert.assertNotNull(dto.getIdValor());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Test Obtener Lista Valores Actor "persona juridica"
     */
    @Test
    public void testObtenerListaValoresActorJuridica() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.obtenerListaValoresActor("");

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                Assert.assertNotNull(dto.getIdValor());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Test Obtener Lista área científica
     */
    @Test
    public void testObtenerListaAreasCientificas() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.obtenerListaAreasCientificas();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                Assert.assertNotNull(dto.getIdValor());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Test Obtener Lista estados solicitud
     */
    @Test
    public void testObtenerListaEstadosSolicitud() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.obtenerListaEstadosSolicitud();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                Assert.assertNotNull(dto.getIdValor());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Test Buscar por expediente
     */
    @Test
    public void testBuscarPorExpediente() {
        try {
            SolicitudDTO dto = this.service.buscarPorExpediente(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Contiene id
            Assert.assertNotNull(dto.getIdSolicitud());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Test Descargar
     *
     */
    // @Test
    public void testDescargar() {
        /*
         * try { DocumentoDTO documentoDTO = service.descargar(2);
         *
         * // No es nulo Assert.assertNotNull(documentoDTO);
         *
         * } catch (final ServiceException e) { log.error(e.getMessage());
         * fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage()); }
         */
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(SolicitudDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdSolicitud());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getConvocatoriaDTO().getDsDescripcion());
            Assert.assertNotNull(dto.getConvocatoriaDTO().getIdConvocatoria());
            Assert.assertNotNull(dto.getCdNote());
        }

        Assert.assertNotNull(dto.getConvocatoriaDTO().getIdConvocatoria());
        Assert.assertNotNull(dto.getEstadoDTO().getIdValor());
        Assert.assertNotNull(dto.getEstadoDTO().getDsValor());
        Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());
    }

    @Test
    public void testBuscarSolicitudesTramitacion() {
        try {

            List<AtlasFilter> filtros1 = new ArrayList<>();
            List<SolicitudDTO> lstDTO = this.service.buscarSolicitudesTramitacion(0, -1, filtros1,
                    null, 1, true, 0);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<SolicitudDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                SolicitudDTO dto = it.next();
                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscarSolicitudesTramitacion(0, -1, filtros, null, 1, true, 0);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarDocumentosRiquisitos() {
        try {

            List<AtlasFilter> filtros1 = new ArrayList<>();
            List<SolicitudDTO> lstDTO = this.service.buscarSolicitudesTramitacion(0, -1, filtros1,
                    null, 27, true, 15);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
