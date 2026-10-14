package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ExpedienteEvalService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ExpedienteEvalServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService service;

    /**
     * Servicio de PffService
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * Servicio de PffService
     */
    @Autowired
    @Qualifier("expedienteEvalDAO")
    private ExpedienteEvalDAO expedienteEvalDAO;

    /**
     * Servicio de PffService
     */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
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
     * Obtener la lista de coordinadores expediente
     */
    @Test
    public void obtenerCoordinadorExpedienteTest() {

        Integer idExpediente = 65;
        String itCoorEval = Constantes.TipoCoorEval.COORDINADOR;

        try {
            List<ExpedienteEvalDTO> lista = this.service.obtenerCoordinadorExpediente(idExpediente,
                    itCoorEval);

            Assert.assertFalse(lista.isEmpty());

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * Obtener de lista de coordinadores expediente por IdExpediente
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerCoordinadorExpedientePorIdExpedienteTest() throws ServiceException {

        Integer idExpediente = null;
        String itCoorEval = Constantes.TipoCoorEval.COORDINADOR;

        this.service.obtenerCoordinadorExpediente(idExpediente, itCoorEval);
    }

    /**
     * Obtener la lista de coordinadores expediente por ItCoorEval
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerCoordinadorExpedientePorItCoorEvalTest() throws ServiceException {

        Integer idExpediente = 65;
        String itCoorEval = null;

        this.service.obtenerCoordinadorExpediente(idExpediente, itCoorEval);
    }

    /**
     * Prueba el método de dar altaCoordinadorExpedienteTest
     */
    @Test
    public void altaCoordinadorExpedienteTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(74);

        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(29);
        exp.getEstadoDTO()
                .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
        exp.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);
        exp.setCdUsuAlta("LATAMIM");
        exp.setFcAltaFila(new Date());

        ExpedienteDTO result = this.service.altaCoordinadorExpediente(exp);

        // No es nulo
        Assert.assertNotNull(result);

        // Está relleno
        Assert.assertEquals(exp.getIdExpediente(), result.getIdExpediente());
    }

    /**
     * Prueba el método de dar altaCoordinadorExpedienteTest
     */
    @Test
    public void altaCoordinadorExpedienteTrueTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(75);

        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(29);
        exp.getEstadoDTO()
                .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
        exp.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);
        exp.setCdUsuAlta("LATAMIM");
        exp.setFcAltaFila(new Date());
        exp.setCdUsuModif("LATAMIM");
        exp.setFcModifFila(new Date());
        ExpedienteDTO result = this.service.altaCoordinadorExpediente(exp);

        // No es nulo
        Assert.assertNotNull(result);

        // Está relleno
        Assert.assertEquals(exp.getIdExpediente(), result.getIdExpediente());
    }

    /**
     * Prueba el método de dar alta por IdExpediente, metodo
     * errorIdExpedienteAltaCoordinadorExpedienteTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdExpedienteAltaCoordinadorExpedienteTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(null);
        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(28);
        exp.getEstadoDTO()
                .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
        exp.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);

        this.service.altaCoordinadorExpediente(exp);
    }

    /**
     * Prueba el método de dar alta por IdInteresado, metodo
     * errorIdInteresadoAltaCoordinadorExpedienteTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdInteresadoAltaCoordinadorExpedienteTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(64);
        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(null);
        exp.getEstadoDTO()
                .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
        exp.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);
        this.service.altaCoordinadorExpediente(exp);
    }

    /**
     * Prueba el método de dar alta por estado, metodo
     * errorEstadoAltaCoordinadorExpedienteTest.
     */
    @Test(expected = ServiceException.class)
    public void errorEstadoAltaCoordinadorExpedienteTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(64);
        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(28);
        exp.getEstadoDTO().setIdValor(null);
        exp.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);
        this.service.altaCoordinadorExpediente(exp);
    }

    /**
     * Prueba el método de dar alta por ItCoorEval, metodo
     * errorItCoorEvalAltaCoordinadorExpedienteTest.
     */
    @Test(expected = ServiceException.class)
    public void errorItCoorEvalAltaCoordinadorExpedienteTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        exp.setIdExpediente(64);
        exp.setPersonaDTO(new InteresadoDTO());
        exp.getPersonaDTO().setIdInteresado(28);
        exp.getEstadoDTO()
                .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ASIGNACION_EVALUADORES));
        exp.setItCoorEval(null);
        this.service.altaCoordinadorExpediente(exp);
    }

    /**
     * Validar la lista de expedienteEval, metodo de existeExpedienteEvalTest
     *
     * @throws ServiceException
     */
    @Test
    public void existeExpedienteEvalTest() throws ServiceException {
        Integer idExpedienteEval = 6;

        Boolean result = this.service.existenVariosCoordinadorExpediente(idExpedienteEval);

        Assert.assertNotNull(result);
        assertTrue(result);
    }

    /**
     * Validar la lista de expediente por idExpedienteEval, metodo de
     * errorExisteExpedienteEvalTest
     */
    @Test(expected = ServiceException.class)
    public void errorExisteExpedienteEvalTest() throws ServiceException {

        Integer idExpedienteEval = null;

        this.service.existenVariosCoordinadorExpediente(idExpedienteEval);

    }

    /**
     * Validar la lista de expediente por Id que no existe
     */
    @Test
    public void validacionExisteExpedienteEvalTest() throws ServiceException {

        Integer idExpedienteEval = 100;

        this.service.existenVariosCoordinadorExpediente(idExpedienteEval);

        // No es nulo
        Assert.assertNotNull(idExpedienteEval);

        // Está relleno
        Assert.assertTrue(idExpedienteEval > 0);

    }

    /**
     * método para rechazar el informe y cambiar a estado "en evaluacion por el
     * coordinador"
     */
    @Test
    public void testRechazarInformeCoordinador() {

        try {
            ExpedienteEvalDTO dto = this.service.rechazarInformeCoordinador(1);
            Assert.assertNotNull(dto);
            Assert.assertEquals(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR,
                    dto.getEstadoDTO().getIdValor());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * método para capturar la excepción del método ErrorRechazar informe
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorRechazarInformeCoordinador() throws ServiceException {
        this.service.rechazarInformeCoordinador(0);

    }

    /**
     * @throws ServiceException
     *
     */
    @Test
    public void testEsCerradoCoordinadorTodos() throws ServiceException {
        Boolean dato = this.service.esCerradoCoordinadorTodos(65);
        assertTrue(dato);
    }

    /**
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorEsCerradoCoordinadorTodos() throws ServiceException {
        this.service.esCerradoCoordinadorTodos(null);

    }

    /**
     * Test de metodo desaprobarInformeEvaluador - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testDesaprobarInformeEvaluador() throws ServiceException {
        String cdNumIden = "USUMOD";
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        expedienteEvalDTO.setIdExpediente(66);
        expedienteEvalDTO.setIdExpedienteEval(2);
        CatalogosValoresDTO causaDTO = new CatalogosValoresDTO();
        causaDTO.setIdValor(350);
        expedienteEvalDTO.setCausaDTO(causaDTO);
        expedienteEvalDTO.setDsObsRechazoInfEval("Observación de prueba");
        ExpedienteEvalDTO dto = this.service.desaprobarInformeEvaluador(cdNumIden,
                expedienteEvalDTO);
        Assert.assertNotNull(dto);
        Assert.assertEquals(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR,
                dto.getEstadoDTO().getIdValor());
    }

    /**
     * Test de metodo desaprobarInformeEvaluador - proceso incorrecto 1 -
     * idExpediente nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testDesaprobarInformeEvaluadorSinIdExpediente() throws ServiceException {
        String cdNumIden = "USUMOD";
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        expedienteEvalDTO.setIdExpediente(null);
        expedienteEvalDTO.setIdExpedienteEval(2);
        CatalogosValoresDTO causaDTO = new CatalogosValoresDTO();
        causaDTO.setIdValor(100);
        expedienteEvalDTO.setCausaDTO(causaDTO);
        expedienteEvalDTO.setDsObsRechazoInfEval("Observación de prueba");
        this.service.desaprobarInformeEvaluador(cdNumIden, expedienteEvalDTO);
    }

    /**
     * Test de metodo desaprobarInformeEvaluador - proceso incorrecto 2 -
     * idEvaluacion nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testDesaprobarInformeEvaluadorSinIdEvaluador() throws ServiceException {
        String cdNumIden = "USUMOD";
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        expedienteEvalDTO.setIdExpediente(66);
        expedienteEvalDTO.setIdExpedienteEval(null);
        CatalogosValoresDTO causaDTO = new CatalogosValoresDTO();
        causaDTO.setIdValor(100);
        expedienteEvalDTO.setCausaDTO(causaDTO);
        expedienteEvalDTO.setDsObsRechazoInfEval("Observación de prueba");
        this.service.desaprobarInformeEvaluador(cdNumIden, expedienteEvalDTO);
    }

    /**
     * Test de metodo desaprobarInformeEvaluador - proceso incorrecto 3 -
     * cdNumIden nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testDesaprobarInformeEvaluadorSinCdNumIden() throws ServiceException {
        String cdNumIden = null;
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        expedienteEvalDTO.setIdExpediente(66);
        expedienteEvalDTO.setIdExpedienteEval(2);
        CatalogosValoresDTO causaDTO = new CatalogosValoresDTO();
        causaDTO.setIdValor(100);
        expedienteEvalDTO.setCausaDTO(causaDTO);
        expedienteEvalDTO.setDsObsRechazoInfEval("Observación de prueba");
        this.service.desaprobarInformeEvaluador(cdNumIden, expedienteEvalDTO);
    }

    /**
     * Test de metodo desaprobarInformeEvaluador - proceso incorrecto 3 -
     * idCausa nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testDesaprobarInformeEvaluadorSinIdCausa() throws ServiceException {
        String cdNumIden = "ADMIN";
        ExpedienteEvalDTO expedienteEvalDTO = new ExpedienteEvalDTO();
        expedienteEvalDTO.setIdExpediente(66);
        expedienteEvalDTO.setIdExpedienteEval(2);
        expedienteEvalDTO.setCausaDTO(null);
        expedienteEvalDTO.setDsObsRechazoInfEval("Observación de prueba");
        this.service.desaprobarInformeEvaluador(cdNumIden, expedienteEvalDTO);
    }

    /**
     * Test de metodo finalizarSinInformeEvaluacion - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarSinInformeEvaluacion() throws ServiceException {
        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(3);
        dtoInput.setIdExpediente(65);
        String cdNumIden = "55826624G";
        ExpedienteEvalDTO dto = this.service.finalizarSinInformeEvaluacion(dtoInput, cdNumIden);
        Assert.assertNotNull(dto);
        Assert.assertEquals(Constantes.EstadoCoordinador.CERRADO_COORDINADOR,
                dto.getEstadoDTO().getIdValor());
    }

    /**
     * Test de metodo finalizarSinInformeEvaluacion - proceso incorrecto 1 -
     * datos expedienteEval nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testFinalizarSinInformeEvaluacionSinExpedienteEval() throws ServiceException {
        ExpedienteEvalDTO dtoInput = null;
        String cdNumIden = "USUMOD";
        this.service.finalizarSinInformeEvaluacion(dtoInput, cdNumIden);
    }

    /**
     * Test de metodo finalizarSinInformeEvaluacion - proceso incorrecto 2 -
     * idExpedienteEval nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testFinalizarSinInformeEvaluacionSinIdExpedienteEval() throws ServiceException {
        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        String cdNumIden = "USUMOD";
        this.service.finalizarSinInformeEvaluacion(dtoInput, cdNumIden);
    }

    /**
     * Test de metodo finalizarSinInformeEvaluacion - proceso incorrecto 3 -
     * cdNumIden nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testFinalizarSinInformeEvaluacionSinCdNumIden() throws ServiceException {
        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(1);
        String cdNumIden = null;
        this.service.finalizarSinInformeEvaluacion(dtoInput, cdNumIden);
    }

    /**
     * Obtener la lista de evaluadores asignados
     */
    @Test
    public void obtenerEvaluadoresAsignadosTest() throws ServiceException {
        Integer idExpediente = 67;

        List<ExpedienteEvalDTO> lstDTO = this.service.obtenerEvaluadoresAsignados(idExpediente);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());
    }

    /**
     * Error de la lista de evaluadores asignados
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerEvaluadoresAsignadosTest() throws ServiceException {
        Integer idExpediente = null;
        this.service.obtenerEvaluadoresAsignados(idExpediente);
    }

    /**
     * Prueba el método de dar asignar Evaluador
     *
     * @throws ServiceException
     */
    @Test
    public void verificarPermisoCoordinadorExpedienteTest() throws ServiceException {

        Integer idExpediente = 67;
        String cdUsuario = "54826624G";

        Boolean verificar = this.service.verificarPermisoCoordinadorExpediente(idExpediente,
                cdUsuario);

        // No es nulo
        Assert.assertNotNull(verificar);

        // Está relleno
        Assert.assertTrue(verificar);
    }

    /**
     * Prueba el método de dar asignar Evaluador con datos que no existen
     *
     * @throws ServiceException
     */
    @Test
    public void verificarPermisoCoordinadorExpediente2Test() throws ServiceException {

        Integer idExpediente = 100;
        String cdUsuario = "54826624G";

        Boolean verificar = this.service.verificarPermisoCoordinadorExpediente(idExpediente,
                cdUsuario);

        // No es nulo
        Assert.assertNotNull(verificar);

        // Está relleno
        Assert.assertTrue(!verificar);
    }

    /**
     * Prueba el método de asignacion evaluador por IdExpediente, metodo
     * errorIdExpedienteAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdExpedienteVerificarPermisoCoordinadorExpedienteTest()
            throws ServiceException {
        Integer idExpediente = null;
        String cdUsuario = "54826624G";

        this.service.verificarPermisoCoordinadorExpediente(idExpediente, cdUsuario);
    }

    /**
     * Prueba el método de asignacion evaluador por IdExpediente, metodo
     * errorIdExpedienteAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorCdUsuarioVerificarPermisoCoordinadorExpedienteTest() throws ServiceException {
        Integer idExpediente = 67;
        String cdUsuario = null;

        this.service.verificarPermisoCoordinadorExpediente(idExpediente, cdUsuario);
    }

    /**
     * Prueba el método de dar asignar Evaluador
     *
     * @throws ServiceException
     */
    @Test
    public void asignarEvaluadorTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        Integer idExpediente = 67;
        Integer idEvaluador = 29;
        String cdUsuario = "54826624G";

        exp = this.service.asignarEvaluador(idExpediente, idEvaluador, cdUsuario);

        this.testAtributos(exp, Boolean.FALSE);

        // No es nulo
        Assert.assertNotNull(exp);

        // Está relleno
        Assert.assertEquals(idExpediente, exp.getIdExpediente());
    }

    /**
     * Prueba el método de dar asignar Evaluador, con datos que no existe
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void asignarEvaluador2Test() throws ServiceException {
        Integer idExpediente = 100;
        Integer idEvaluador = 29;
        String cdUsuario = "54826624G";

        this.service.asignarEvaluador(idExpediente, idEvaluador, cdUsuario);

    }

    /**
     * Prueba el método de asignacion evaluador por IdExpediente, metodo
     * errorIdExpedienteAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdExpedienteAsignarEvaluadorTest() throws ServiceException {
        Integer idExpediente = null;
        Integer idEvaluador = 29;
        String cdUsuario = "54826624G";

        this.service.asignarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Prueba el método de asignacion evaluador por idEvaluador, metodo
     * errorIdEvaluadorAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdEvaluadorAsignarEvaluadorTest() throws ServiceException {
        Integer idExpediente = 67;
        Integer idEvaluador = null;
        String cdUsuario = "54826624G";

        this.service.asignarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Prueba el método de asignacion evaluador por cdUsuario, metodo
     * errorCdUsuarioAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorCdUsuarioAsignarEvaluadorTest() throws ServiceException {
        Integer idExpediente = 67;
        Integer idEvaluador = 29;
        String cdUsuario = null;

        this.service.asignarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Prueba el método de desasociar Evaluador
     *
     * @throws ServiceException
     */
    @Test
    public void desasociarEvaluadorTest() throws ServiceException {
        ExpedienteEvalDTO exp = new ExpedienteEvalDTO();
        Integer idExpediente = 67;
        Integer idEvaluador = 29;
        String cdUsuario = "54826624G";

        exp = this.service.desasociarEvaluador(idExpediente, idEvaluador, cdUsuario);

        this.testAtributos(exp, Boolean.FALSE);

        // No es nulo
        Assert.assertNotNull(exp);

        // Está relleno
        Assert.assertEquals(idExpediente, exp.getIdExpediente());
    }

    /**
     * Prueba el método de desasociar evaluador por IdExpediente, metodo
     * errorIdExpedienteAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdExpedienteDesasociarEvaluadorTest() throws ServiceException {
        Integer idExpediente = null;
        Integer idEvaluador = 29;
        String cdUsuario = "54826624G";

        this.service.desasociarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Prueba el método de desasociar evaluador por idEvaluador, metodo
     * errorIdEvaluadorAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorIdEvaluadorDesasociarEvaluadorTest() throws ServiceException {
        Integer idExpediente = 67;
        Integer idEvaluador = null;
        String cdUsuario = "54826624G";

        this.service.desasociarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Prueba el método de desasociar evaluador por cdUsuario, metodo
     * errorCdUsuarioAsignarEvaluadorTest.
     */
    @Test(expected = ServiceException.class)
    public void errorCdUsuarioDesasociarEvaluadorTest() throws ServiceException {
        Integer idExpediente = 67;
        Integer idEvaluador = 29;
        String cdUsuario = null;

        this.service.desasociarEvaluador(idExpediente, idEvaluador, cdUsuario);
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso correcto con el
     * FICA GUARDAR
     *
     * @throws ServiceException
     */
    @Test
    public void testfinalizarConInformeEvaluacionFicaGuardar() throws ServiceException {
        // creamos mock para fica
        FicaService ficaMock = mock(FicaService.class);
        FicaFicheroImpl ficaFichero = new FicaFicheroImpl();
        ficaFichero.setIdInterno(111);
        when(ficaMock.guardar(any(byte[].class), any(FicaMetadatos.class))).thenReturn(ficaFichero);

        ExpedienteEvalServiceImpl serviceMock = new ExpedienteEvalServiceImpl(
                this.expedienteEvalDAO);
        serviceMock.setFicaService(ficaMock);
        serviceMock.setExpedienteEvalDAO(this.expedienteEvalDAO);
        serviceMock.setExpedienteDAO(this.expedienteDAO);

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(9);
        dtoInput.setIdExpediente(82);
        dtoInput.setItCoorEval("C");
        dtoInput.setDsComentario("Prueba");
        dtoInput.setNmNotaFinal(18);
        String cdNumIden = "55826624G";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.pdfService.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        byte[] docPdf = baos.toByteArray();
        

        ExpedienteEvalDTO dto = serviceMock.finalizarInformeEvaluacion(dtoInput, cdNumIden, docPdf);

        Assert.assertEquals(Constantes.EstadoCoordinador.CERRADO_COORDINADOR,
                dto.getEstadoDTO().getIdValor());
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso correcto con el
     * FICA ACTUALIZAR
     *
     * @throws ServiceException
     */
    // @Test
    public void testfinalizarConInformeEvaluacionFicaActualizar() throws ServiceException {

        // creamos mock para fica
        FicaService ficaMock = mock(FicaService.class);
        FicaFicheroImpl ficaFichero = new FicaFicheroImpl();

        ficaFichero.setIdInterno(707070);
        when(ficaMock.actualizar(any(Integer.class), any(byte[].class), any(FicaMetadatos.class)))
                .thenReturn(ficaFichero);

        ExpedienteEvalServiceImpl serviceMock = new ExpedienteEvalServiceImpl(
                this.expedienteEvalDAO);
        serviceMock.setFicaService(ficaMock);
        serviceMock.setExpedienteEvalDAO(this.expedienteEvalDAO);
        serviceMock.setExpedienteDAO(this.expedienteDAO);

        ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
        dtoInput.setIdExpedienteEval(11);
        dtoInput.setIdExpediente(84);
        dtoInput.setItCoorEval("E");
        dtoInput.setDsComentario("Prueba");
        dtoInput.setNmNotaFinal(18);
        String cdNumIden = "55826624G";

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        document.open();

        this.pdfService.crearFormularioEvaluacionDatosIdExpEval(
                baos, document, dtoInput, Boolean.FALSE);

        document.close();

        
        byte[] docPdf = baos.toByteArray();
        

        ExpedienteEvalDTO dto = serviceMock.finalizarInformeEvaluacion(dtoInput, cdNumIden, docPdf);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno
        Assert.assertEquals(dto.getIdExpediente(), dto.getIdExpediente());

    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso incorrecto 1 -
     * datos expedienteEval nulo
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarConInformeEvaluacionSinExpedienteEval() {
        try {
            ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
            dtoInput.setIdExpedienteEval(null);
            dtoInput.setIdExpediente(83);
            dtoInput.setItCoorEval("C");
            dtoInput.setDsComentario("Prueba");
            dtoInput.setNmNotaFinal(18);
            String cdNumIden = "55826624G";

            ExpedienteEvalDTO dto = this.service.finalizarInformeEvaluacion(dtoInput, cdNumIden,
                    any(byte[].class));
            Assert.assertNull(dto);
        } catch (ServiceException e) {

            Assert.assertNull(null);
        }
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso incorrecto 2 -
     * idExpediente nulo
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarConInformeEvaluacionSinIdExpediente() {
        try {
            ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
            dtoInput.setIdExpedienteEval(12);
            dtoInput.setIdExpediente(null);
            dtoInput.setItCoorEval("C");
            dtoInput.setDsComentario("Prueba");
            dtoInput.setNmNotaFinal(18);
            String cdNumIden = "55826624G";

            ExpedienteEvalDTO dto = this.service.finalizarInformeEvaluacion(dtoInput, cdNumIden,
                    any(byte[].class));
            Assert.assertNull(dto);
        } catch (ServiceException e) {

            Assert.assertNull(null);
        }
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso incorrecto 3 -
     * ItCoorEval nulo
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarConInformeEvaluacionSinItCoorEval() {

        try {

            ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
            dtoInput.setIdExpedienteEval(12);
            dtoInput.setIdExpediente(85);
            dtoInput.setItCoorEval(null);
            dtoInput.setDsComentario("Prueba");
            dtoInput.setNmNotaFinal(18);
            String cdNumIden = "55826624G";

            ExpedienteEvalDTO dto = this.service.finalizarInformeEvaluacion(dtoInput, cdNumIden,
                    any(byte[].class));
            Assert.assertNull(dto);
        } catch (ServiceException e) {

            Assert.assertNull(null);
        }
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso incorrecto 4 -
     * cdNumIden nulo
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarConInformeEvaluacionSinCdNumIden() {
        try {
            ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
            dtoInput.setIdExpedienteEval(12);
            dtoInput.setIdExpediente(85);
            dtoInput.setItCoorEval("C");
            dtoInput.setDsComentario("Prueba");
            dtoInput.setNmNotaFinal(18);
            String cdNumIden = null;

            ExpedienteEvalDTO dto = this.service.finalizarInformeEvaluacion(dtoInput, cdNumIden,
                    any(byte[].class));
            Assert.assertNull(dto);
        } catch (ServiceException e) {

            Assert.assertNull(null);
        }
    }

    /**
     * Test de metodo finalizarConInformeEvaluacion - proceso incorrecto -
     * ExpedienteEvalDTO nulo
     *
     * @throws ServiceException
     */
    @Test
    public void testFinalizarConInformeEvaluacionSinExpedienteEvalDTO() {
        try {

            ExpedienteEvalDTO dtoInput = new ExpedienteEvalDTO();
            dtoInput.setIdExpedienteEval(null);
            dtoInput.setIdExpediente(null);
            dtoInput.setItCoorEval(null);
            dtoInput.setDsComentario(null);
            dtoInput.setNmNotaFinal(null);
            String cdNumIden = null;

            ExpedienteEvalDTO dto = this.service.finalizarInformeEvaluacion(dtoInput, cdNumIden,
                    any(byte[].class));
            Assert.assertNull(dto);
        } catch (ServiceException e) {

            Assert.assertNull(null);
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ExpedienteEvalDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdExpedienteEval());

        Assert.assertNotNull(dto.getIdExpediente());

        Assert.assertNotNull(dto.getPersonaDTO());

        Assert.assertNotNull(dto.getEstadoDTO());

        Assert.assertNotNull(dto.getItCoorEval());

        Assert.assertNotNull(dto.getCdUsuAlta());

        Assert.assertNotNull(dto.getFcAltaFila());

        if (bRelleno) {

            Assert.assertNotNull(dto.getCdUsuModif());

            Assert.assertNotNull(dto.getFcModifFila());
        }

    }

}
