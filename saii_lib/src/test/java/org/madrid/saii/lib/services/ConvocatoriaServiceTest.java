package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPaginacionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.ResponseDTO;
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
 * Test ConvocatoriaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService service;

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
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

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
            ConvocatoriaDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Buscar por acronimo
     */
    @Test
    public void testBuscarPorAcronimo() {
        try {
            List<ConvocatoriaDTO> lstDTO = this.service.buscarPorAcronimo("ACR");

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno (depende del acrónimo que exista, utilizaremos un if
            // para que no
            // falle el test simplemente por el hecho de no existir el acrónimo)
            if (lstDTO.isEmpty()) {
                Assert.assertTrue(lstDTO.isEmpty());
            } else {
                Assert.assertFalse(lstDTO.isEmpty());
            }

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarPorAcronimo("");

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
    public void testBuscar() {
        try {
            ConvocatoriaPaginacionDTO lstDTO = this.service.buscar(0, -1, null, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO.getLstConvocatorias());

            // Está relleno
            Assert.assertFalse(lstDTO.getLstConvocatorias().isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDTO> it = lstDTO.getLstConvocatorias().iterator();
            while (it.hasNext()) {
                ConvocatoriaDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscar(0, -1, filtros, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.getLstConvocatorias().isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarConvocatoriasActivas() {
        try {
            List<ConvocatoriaDTO> lstConvAct = this.service.buscarConvocatoriasActivas();

            // No es nulo
            Assert.assertNotNull(lstConvAct);

            // Está relleno o vacío
            if (lstConvAct.isEmpty()) {
                Assert.assertTrue(lstConvAct.isEmpty());
            } else {
                Assert.assertFalse(lstConvAct.isEmpty());
            }

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDTO> it = lstConvAct.iterator();
            while (it.hasNext()) {
                ConvocatoriaDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            ConvocatoriaDTO dto = new ConvocatoriaDTO();

            CatalogosValoresDTO idEstado = new CatalogosValoresDTO();
            idEstado.setIdValor(1);

            UnidadAdminDTO unidadAdminDTO = new UnidadAdminDTO();
            unidadAdminDTO.setIdUnidadAdmin(1);
            dto.setCdAcronimo("ACR");
            dto.setCdTitulo("1");
            dto.setDsDescripcion("DESCR");
            dto.setIdEstado(idEstado);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setIdSubdireccion(unidadAdminDTO);
            dto.setNmDiasSubsanacion(11);

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Convocatoria ent = this.sessionFactory.getCurrentSession().get(Convocatoria.class,
                    dto.getIdConvocatoria());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsDescripcion("Prueba Test Mod");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("Prueba Test Mod", dto.getDsDescripcion());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Prueba el método ComprobarTipoActor ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testComprobarTipoActorError1() throws ServiceException {

        this.service.comprobarTipoActor(null, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA), 0);
    }

    /**
     * Prueba el método ComprobarTipoActor ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testComprobarTipoActorError2() throws ServiceException {

        this.service.comprobarTipoActor(10, null, 0);
    }

    /**
     * Prueba el método ComprobarTipoActor ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testComprobarTipoActorError3() throws ServiceException {

        this.service.comprobarTipoActor(10, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA), null);
    }

    /**
     * Prueba el método ComprobarTipoActor no coincidir con ninguno de los tipos
     * de persona se le denegará el acceso a la cumplimentación indicando que el
     * actor no cumple los requisitos.
     */
    @Test
    public void testComprobarTipoActorError4() throws ServiceException {

        ResponseDTO responseDTO;
        // La convocatoria no tiene actores
        responseDTO = this.service.comprobarTipoActor(99, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA), 65);
        Assert.assertTrue(responseDTO.getError());
        Assert.assertEquals("101", responseDTO.getCodigo());
    }

    /**
     * Prueba el método ComprobarTipoActor no coincidir con ninguno de los tipos
     * de persona se le denegará el acceso a la cumplimentación indicando que el
     * actor no cumple los requisitos.
     */
    // @Test
    public void testComprobarTipoActorError5() throws ServiceException {

        ResponseDTO responseDTO;
        // La convocatoria solo tiene un actor fisico
        responseDTO = this.service.comprobarTipoActor(1, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.JURIDICA), 65);
        Assert.assertTrue(responseDTO.getError());
        Assert.assertEquals("102", responseDTO.getCodigo());
    }

    /**
     * Prueba el método ComprobarTipoActor no coincidir con ninguno de los tipos
     * de persona se le denegará el acceso a la cumplimentación indicando que el
     * actor no cumple los requisitos.
     */
    @Test
    public void testComprobarTipoActorError6() throws ServiceException {

        ResponseDTO responseDTO;
        // La convocatoria tiene un actor fisico pero no 66
        responseDTO = this.service.comprobarTipoActor(1, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA), 66);
        Assert.assertTrue(responseDTO.getError());
        Assert.assertEquals("103", responseDTO.getCodigo());
    }

    /**
     * Comprueba actor fisico y actor juridico
     */
    // @Test
    public void testComprobarTipoActorOK() {
        try {

            ResponseDTO responseDTO;
            // La convocatoria tiene un tipo actor Persona Fisica / Investigador
            responseDTO = this.service.comprobarTipoActor(1, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA), 907);
            Assert.assertFalse(responseDTO.getError());

            // La convocatoria tiene un tipo actor Persona Juridica.
            responseDTO = this.service.comprobarTipoActor(1, utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.JURIDICA), 0);
            Assert.assertTrue(responseDTO.getError());

            // // La convocatoria no tiene un tipo actor Persona Fisica.
            // responseDTO = service.comprobarTipoActor(11,
            // Constantes.TipoActor.FISICA, 0);
            // Assert.assertTrue(!responseDTO.getBResultado());
            //
            // // La convocatoria no tiene un tipo actor Persona Juridica.
            // responseDTO = service.comprobarTipoActor(11,
            // Constantes.TipoActor.JURIDICA, 0);
            // Assert.assertTrue(!responseDTO.getBResultado());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     *
     * Comprueba guardarBaseOtraConvocatoria
     */
    @Test
    public void guardarBaseOtraConvocatoriaTest() {
        try {
            ConvocatoriaDTO convocatoriaDTO = this.service.buscarId(12);
            Assert.assertNotNull(convocatoriaDTO);

            int secuencia = new SecureRandom().nextInt(100);
            convocatoriaDTO.setCdAcronimo("COPY-" + secuencia);
            ConvocatoriaDTO copiaConvocatoriaDTO = this.service
                    .guardarBaseOtraConvocatoria(convocatoriaDTO, 12);

            Assert.assertNotNull(copiaConvocatoriaDTO.getIdConvocatoria());
            Assert.assertNotEquals(copiaConvocatoriaDTO.getIdConvocatoria(),
                    convocatoriaDTO.getIdConvocatoria());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarConvocatoriasTramitacion() {
        try {

            List<AtlasFilter> filtros1 = new ArrayList<>();
            ConvocatoriaPaginacionDTO lstDTO = this.service.buscarConvocatoriasTramitacion(0, -1,
                    filtros1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.getLstConvocatorias().isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDTO> it = lstDTO.getLstConvocatorias().iterator();
            while (it.hasNext()) {
                ConvocatoriaDTO dto = it.next();
                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscarConvocatoriasTramitacion(0, -1, filtros, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.getLstConvocatorias().isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarConvocatoriasDocumentoRiquisito() {
        try {

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("idConvocatoria", 27));

            ConvocatoriaPaginacionDTO lstDTO = this.service.buscarConvocatoriasTramitacion(0, -1,
                    filtros, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO.getLstConvocatorias());

            // Está relleno
            Assert.assertFalse(lstDTO.getLstConvocatorias().isEmpty());
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorIdConListaAreaSubmod() {
        try {
            ConvocatoriaDTO dto = this.service.buscarId(25);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());

            // Lista de areas no nulo
            Assert.assertNotNull(dto.getListConvocatoriaArea());

            // Lista de submodalidades no nulo
            Assert.assertNotNull(dto.getListConvocatoriaSubmod());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testActualizarIndicadorRequisitoSi() {

        try {

            ConvocatoriaDTO dto = this.service.actualizarIndicadorRequisito(25, Boolean.TRUE);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());

            // Lista de areas no nulo
            Assert.assertEquals(Constantes.AfirmacionNegacion.S, dto.getItRequisitos());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    @Test
    public void testActualizarIndicadorRequisitoNo() {
        try {
            this.service.actualizarIndicadorRequisito(25, Boolean.FALSE);

            ConvocatoriaDTO dto = this.service.actualizarIndicadorRequisito(25, Boolean.FALSE);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());

            // Lista de areas no nulo
            Assert.assertEquals(Constantes.AfirmacionNegacion.N, dto.getItRequisitos());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testConsultarDocumentoPlazoSubsanacionBatch() {
        try {
            this.service.consultarDocumentoPlazoSubsanacionBatch();
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * valida si se puede subir un documento tipo propuesta provisiona
     */
    @Test
    public void testEsPuedeSubirPropuestaResProvisional() {
        try {
            Boolean existenDatos = this.service.esPuedeSubirPropuestaResProvisional(29);
            Assert.assertNotNull(existenDatos);
            assertTrue(existenDatos);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * exception null
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorEsPuedeSubirPropuestaResProvisionalNull() throws ServiceException {

        this.service.esPuedeSubirPropuestaResProvisional(null);

    }

    /**
     * exception cuando no cumple con la consulta del DAO
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorEsPuedeSubirPropuestaResProvisional() throws ServiceException {

        this.service.esPuedeSubirPropuestaResProvisional(458);

    }

//    /**
//     * error resolver convocatoria. lanza un exception en la convocatoria si el
//     * expediente no se encuentra en unos de estos estados: concedido
//     * definitivo, excluido, desistido, renuncia expresa, iniciada
//     *
//     * @throws ServiceException
//     */
//    @Test(expected = ServiceException.class)
//    public void testErrorResolver() throws ServiceException {
//
//        this.service.resolver(38);
//
//    }
//
//    /**
//     * id de la convococatoria null
//     *
//     * @throws ServiceException
//     */
//    @Test(expected = ServiceException.class)
//    public void testErrorResolverNull() throws ServiceException {
//
//        this.service.resolver(null);
//
//    }
//
//    /**
//     * resolver convocatoria. resuelve la convocatoria si el expediente se
//     * encuentra en unos de estos estados: concedido definitivo, excluido,
//     * desistido, renuncia expresa, iniciada
//     *
//     */
//    @Test
//    public void testResolver() {
//
//        try {
//
//            ConvocatoriaDTO dto = this.service.resolver(42);
//
//            assertNotNull(dto);
//            Assert.assertEquals(Constantes.EstadoConvocatoria.RESUELTA,
//                    dto.getIdEstado().getIdValor());
//
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//
//    }
    
    /**
     * Prueba configuración de convocatoria con alegaciones
     * @param dto
     */
    @Test
    public void testSinAlegaciones() {
        try {
            ConvocatoriaDTO dto = this.service.buscarId(1);
            List<ConvocatoriaTipoDocDTO> lstConvocatoriaTipoDocDTO = this.service.alegaciones(dto);
            Assert.assertNull(lstConvocatoriaTipoDocDTO);
        } catch(Exception e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }
    
    /**
     * Prueba configuración de convocatoria con alegaciones
     * @param dto
     */
    @Test
    public void testConAlegaciones() {
        try {
            ConvocatoriaDTO dto = this.service.buscarId(2);
            List<ConvocatoriaTipoDocDTO> lstConvocatoriaTipoDocDTO = this.service.alegaciones(dto);
            Assert.assertNull(lstConvocatoriaTipoDocDTO.get(0).getCdUsuBaja());
            Assert.assertNull(lstConvocatoriaTipoDocDTO.get(0).getFcBajaFila());
        } catch(Exception e) {
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
    private void testAtributos(ConvocatoriaDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdAcronimo()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdTitulo()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsDescripcion()));
        Assert.assertNotNull(dto.getIdEstado());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getNmDiasSubsanacion());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getIdLineaAyuda());
            Assert.assertNotNull(dto.getIdSubdireccion());
            Assert.assertNotNull(dto.getIdAreaAdministrativa());
            Assert.assertNotNull(dto.getIdModalidadConvocatoria());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getItConvocatoriaActiva()));
            Assert.assertNotNull(dto.getNmDurMaxAyuda());
            Assert.assertNotNull(dto.getNmMaxPeriodos());
            Assert.assertNotNull(dto.getNmImportePresAnualidad());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getItInicioSolOrg()));
            Assert.assertNotNull(dto.getFcIniPresentacion());
            Assert.assertNotNull(dto.getFcFinPresentacion());
            Assert.assertNotNull(dto.getItPresFueraPlazo());
            Assert.assertNotNull(dto.getIdTipoModalidad());
            Assert.assertNotNull(dto.getTipoActorSolicitanteDTO().getIdValor());
            Assert.assertTrue(
                    StringUtils.isNotBlank(dto.getTipoActorSolicitanteDTO().getDsValor()));
        }

        // Null Safe
        Assert.assertNotNull(dto.getIdLineaAyuda());
        Assert.assertNotNull(dto.getIdSubdireccion());
        Assert.assertNotNull(dto.getIdAreaAdministrativa());
        Assert.assertNotNull(dto.getIdModalidadConvocatoria());
        Assert.assertNotNull(dto.getItConvocatoriaActiva());
        Assert.assertNotNull(dto.getItInicioSolOrg());
        Assert.assertNotNull(dto.getItPresFueraPlazo());
        Assert.assertNotNull(dto.getIdTipoModalidad());
        Assert.assertNotNull(dto.getTipoActorSolicitanteDTO());
        // Assert.assertNotNull(dto.getEsEstadoRevisionEvaluacion());

    }

}
