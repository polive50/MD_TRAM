package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
import org.madrid.saii.lib.dao.ConvocatoriaCoorEvalDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCoor;
import org.madrid.saii.lib.domain.ConvocatoriaCoorEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorFiltrosDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorNavegacionDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ConvocatoriaCoorService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaCoorServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaCoorService")
    private ConvocatoriaCoorService service;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaCoorEvalDAO")
    private ConvocatoriaCoorEvalDAO convocatoriaCoorEvalDAO;

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
    public void testGuardar() {
        try {

            // GUARDAR
            ConvocatoriaCoorDTO dto = new ConvocatoriaCoorDTO();
            dto.setIdConvocatoria(26);
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setIdInteresado(5);
            interesadoDTO.setCdSexo("masculino");
            interesadoDTO.setCdLocalidad("sanlucar");
            interesadoDTO.setCdProvincia("cadiz");
            interesadoDTO.setCdNumIden("12345678E");
            interesadoDTO.setCdCodigoPostal("11540");
            interesadoDTO.setDsApellido1("apellido");
            interesadoDTO.setItCoorEval("coordinador");
            interesadoDTO.setFcNacimiento(new Date());
            interesadoDTO.setNmSexenioInve(12);
            List<PersonaAreaDTO> lista = new ArrayList<PersonaAreaDTO>();
            interesadoDTO.setListaPersonaAreaDTO(lista);
            List<PersonaEspecDTO> lista2 = new ArrayList<PersonaEspecDTO>();
            interesadoDTO.setListaPersonaEspecialidadDTO(lista2);
            interesadoDTO.getTipoActorDTO().setNmConstante(9);
            dto.setInteresadoDTO(interesadoDTO);
            CatalogosValoresDTO areaDto = new CatalogosValoresDTO();
            areaDto.setIdValor(10);
            areaDto.setCdCodigo("codigo");
            dto.setAreaDTO(areaDto);
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());
            dto.setFcModifFila(new Date());
            dto.setCdUsuBaja("prueba2");
            dto.setFcBajaFila(new Date());
            dto.setCdUsuModif("prueba3");
            dto.setIdConvocatoriaCoor(6);

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaCoor ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaCoor.class, dto.getIdConvocatoriaCoor());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setFcBajaFila(null);
            dto.setFcAltaFila(null);
            dto.setCdUsuBaja("usuario");
            dto.setCdUsuAlta("user");
            dto.setCdUsuModif("prueba");
            dto.getInteresadoDTO().setCdSexo("femenino");
            dto.getInteresadoDTO().setCdProvincia("granada");
            dto.getInteresadoDTO().setCdLocalidad("guadix");
            dto.getAreaDTO().setCdCodigo("codigo2");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals(null, dto.getFcBajaFila());
            Assert.assertEquals(null, dto.getFcAltaFila());
            Assert.assertEquals("usuario", dto.getCdUsuBaja());
            Assert.assertEquals("user", dto.getCdUsuAlta());
            Assert.assertEquals("prueba", dto.getCdUsuModif());
            Assert.assertEquals(false, dto.getAreaDTO().getItMantenible());
            Assert.assertEquals("femenino", dto.getInteresadoDTO().getCdSexo());
            Assert.assertEquals("codigo2", dto.getAreaDTO().getCdCodigo());
            Assert.assertEquals("granada", dto.getInteresadoDTO().getCdProvincia());
            Assert.assertEquals("guadix", dto.getInteresadoDTO().getCdLocalidad());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo GuardarErrorDatosIdConvocatoria
     */
    @Test(expected = ServiceException.class)
    public void testGuardarErrorDatosIdConvocatoria() throws ServiceException {

        // GUARDAR
        ConvocatoriaCoorDTO dto = new ConvocatoriaCoorDTO();
        dto.setIdConvocatoria(null);
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setIdInteresado(5);
        dto.setInteresadoDTO(interesadoDTO);
        CatalogosValoresDTO areaDto = new CatalogosValoresDTO();
        areaDto.setIdValor(10);
        dto.setAreaDTO(areaDto);

        this.service.guardar(dto);

    }

    /**
     * Prueba el metodo GuardarErrorDatosIdInteresado.
     */
    @Test(expected = ServiceException.class)
    public void testGuardarErrorDatosIdInteresado() throws ServiceException {

        // GUARDAR
        ConvocatoriaCoorDTO dto = new ConvocatoriaCoorDTO();
        dto.setIdConvocatoria(26);
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setIdInteresado(null);
        dto.setInteresadoDTO(interesadoDTO);
        CatalogosValoresDTO areaDto = new CatalogosValoresDTO();
        areaDto.setIdValor(10);
        dto.setAreaDTO(areaDto);

        this.service.guardar(dto);

    }

    /**
     * Prueba el metodo GuardarErrorDatosIdArea.
     */
    @Test(expected = ServiceException.class)
    public void testGuardarErrorDatosIdArea() throws ServiceException {

        // GUARDAR
        ConvocatoriaCoorDTO dto = new ConvocatoriaCoorDTO();
        dto.setIdConvocatoria(26);
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setIdInteresado(5);
        dto.setInteresadoDTO(interesadoDTO);
        CatalogosValoresDTO areaDto = new CatalogosValoresDTO();
        areaDto.setIdValor(null);
        dto.setAreaDTO(areaDto);

        this.service.guardar(dto);

    }

    /**
     * Prueba el método ObtenerCoordinadoresPorConvocatoriaOK
     */
    @Test
    public void testObtenerCoordinadoresPorConvocatoriaOK() {
        try {
            List<ConvocatoriaCoorDTO> lstDTO = this.service.obtenerCoordinadoresPorConvocatoria(26);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaCoorDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaCoorDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            lstDTO = this.service.obtenerCoordinadoresPorConvocatoria(0);

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
    public void testObtenerCoordinadorConvocatoriaArea() {
        try {
            List<ConvocatoriaCoorDTO> lstDTO = this.service.obtenerCoordinadorConvocatoriaArea(24,
                    162);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaCoorDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaCoorDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            lstDTO = this.service.obtenerCoordinadorConvocatoriaArea(25, 1000);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacío
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método ObtenerCoordinadoresPorConvocatoriaErrorDato.
     */
    @Test(expected = ServiceException.class)
    public void testObtenerCoordinadoresPorConvocatoriaErrorDato() throws ServiceException {

        this.service.obtenerCoordinadoresPorConvocatoria(null);

    }

    /**
     * Prueba el método obtener hayCoordinadoresAreasConvoFalse.
     */
    @Test
    public void testhayCoordinadoresAreasConvoFalse() {
        try {
            Boolean lstDTO = this.service.existeAreaPersonaConvocatoriaCoor(26, 1000, 5);

            Assert.assertFalse(lstDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener hayCoordinadoresAreasConvoTrue.
     *
     */
    @Test
    public void testhayCoordinadoresAreasConvoTrue() {
        try {
            Boolean lstDTO = this.service.existeConvocatoriaCoorArea(25, 1000);

            Assert.assertTrue(lstDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener hayCoordinadoresAreasConvoFalse.
     *
     */
    @Test
    public void testhayCoordinadoresAreasConvo() {
        try {
            Boolean lstDTO = this.service.existeConvocatoriaCoorArea(null, null);

            Assert.assertFalse(lstDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener hayCoordinadoresAreasConvoErrorDatos.
     */
    @Test
    public void testhayCoordinadoresAreasConvoErrorDatos() {
        try {
            Boolean lstDTO = this.service.existeAreaPersonaConvocatoriaCoor(null, null, null);

            Assert.assertTrue(lstDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo en buscar por Id.
     *
     */
    @Test
    public void testBuscarPorId() {
        try {
            ConvocatoriaCoorDTO dto = this.service.buscarId(202);

            log.info(dto.getAreaDTO().getIdValor().toString());

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaCoor());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test(expected = ServiceException.class)
    public void testBuscarPorIdError() throws ServiceException {

        this.service.buscarId(null);

    }

    /**
     * Comprueba el metodo buscar.
     *
     */
    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaCoorDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaCoorDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaCoorDTO dto = it.next();

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

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaCoorDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaCoor());

        Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());

        Assert.assertNotNull(dto.getIdConvocatoria());

        Assert.assertNotNull(dto.getAreaDTO().getIdValor());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));

            Assert.assertNotNull(dto.getFcBajaFila());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getAreaDTO().getCdCodigo()));

            Assert.assertNotNull(dto.getAreaDTO().getItMantenible());

            Assert.assertFalse(dto.getAreaDTO().getItMantenible());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getCdSexo()));

            Assert.assertNotNull(dto.getInteresadoDTO().getCdSexo());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getCdLocalidad()));

            Assert.assertNotNull(dto.getInteresadoDTO().getCdLocalidad());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getCdProvincia()));

            Assert.assertNotNull(dto.getInteresadoDTO().getCdProvincia());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getCdNumIden()));

            Assert.assertNotNull(dto.getInteresadoDTO().getCdNumIden());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getCdCodigoPostal()));

            Assert.assertNotNull(dto.getInteresadoDTO().getCdCodigoPostal());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getDsApellido1()));

            Assert.assertNotNull(dto.getInteresadoDTO().getDsApellido1());

            Assert.assertNotNull(dto.getInteresadoDTO().getTipoActorDTO().getNmConstante());

            Assert.assertNotNull(dto.getInteresadoDTO().getFcNacimiento());

            Assert.assertNotNull(dto.getInteresadoDTO().getNmSexenioInve());

            Assert.assertNotNull(dto.getInteresadoDTO().getListaPersonaAreaDTO());

            Assert.assertNotNull(dto.getInteresadoDTO().getListaPersonaEspecialidadDTO());

            Assert.assertNotNull(dto.getInteresadoDTO().getItCoorEval());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getInteresadoDTO().getItCoorEval()));
        }

    }

    /**
     * Obtención de Convocatoria Coordinador
     */
    @Test
    public void obtenerConvocatoriaCoor() {
        try {
            ConvocatoriaCoorFiltrosDTO filtros = new ConvocatoriaCoorFiltrosDTO();
            filtros.setIdConvocatoria(24);
            filtros.setIdConvocatoriaCoor(207);
            filtros.setIdInteresado(5);
            filtros.setIndex(3);
            filtros.setPageSize(2);
            filtros.setIdAreaCientifica(162);
            filtros.setCdTitulo("Prueba creación convocatoria");
            filtros.setCdAcronimo("Prueba cambio de estado 150");
            filtros.setNmAno(2020);
            ConvocatoriaCoorNavegacionDTO DTO = this.service.obtenerConvocatoriaCoor(filtros);

            // No es nulo
            Assert.assertNotNull(DTO.getLstConvocatoriaCoorFiltrosDTO());

            // Esta vacio
            Assert.assertTrue(DTO.getLstConvocatoriaCoorFiltrosDTO().isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * Eliminar coordinador
     */
    @Test
    public void eliminarCoordinadorEvaluador() {
        try {
            this.service.eliminarCoordinadorEvaluador(207);

            ConvocatoriaCoorDTO convocatoriaCoorDTO = this.service.buscarId(207);

            Assert.assertNotNull(convocatoriaCoorDTO.getFcBajaFila());

            List<AtlasFilter> filtro = new ArrayList<>();

            filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA_COOR,
                    convocatoriaCoorDTO.getIdConvocatoriaCoor(), Operador.EQUALS));

            filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ConvocatoriaCoorEval> lista = this.convocatoriaCoorEvalDAO.find(filtro, null);

            for (ConvocatoriaCoorEval convocatoriaCoorEval : lista) {

                Assert.assertNotNull(convocatoriaCoorEval.getFcBajaFila());
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
