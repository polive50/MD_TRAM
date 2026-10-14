package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.Instant;
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
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PersonaAreaDTO;
import org.madrid.saii.lib.dto.PersonaEspecDTO;
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
 * Test InteresadoService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class InteresadoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService service;
    
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
    public void testGuardar() {
        try {

            // GUARDAR

            InteresadoDTO dto = new InteresadoDTO();
            dto.setTipoIdentificacionDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIF)));
            dto.setDsNombre("Pedro");
            dto.setDsApellido1("Pork");
            dto.setDsApellido2("Raton");
            dto.setCdNumIden("123456X");
            dto.setFcNacimiento(Date.from(Instant.now()));
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(Date.from(Instant.now()));
            dto.getTipoActorDTO().setNmConstante(Constantes.Identificacion.FISICA);
            dto.setCdSexo("H");
            dto.setDsEmail("pepe@pepe.es");
            dto.setDsMovil("698551133");
            dto.setDsTelefono("954887752");
            dto.setCdProvincia("41");
            dto.setCdLocalidad("85412");
            dto.setCdTipoVia("CALLE");
            dto.setDsDireccion("Gato");
            dto.setCdCodigoPostal("28080");
            dto.setDsNumero("8");
            dto.setDsPiso("2");
            dto.setDsPuerta("7");
            dto.setDsEscalera("1");

            dto = this.service.guardarPersona(dto, Constantes.Test.TEST_ADMIN, false);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo de guardar persona del interesado.
     */
    @Test
    public void testGuardarPersona() {
        try {

            // GUARDAR

            InteresadoDTO dto = new InteresadoDTO();
            dto.setTipoIdentificacionDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIF)));
            dto.setDsNombre("Pedro");
            dto.setDsApellido1("Pork");
            dto.setDsApellido2("Raton");
            dto.setCdNumIden("123456X");
            dto.setFcNacimiento(Date.from(Instant.now()));
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(Date.from(Instant.now()));
            dto.getTipoActorDTO().setNmConstante(Constantes.Identificacion.FISICA);
            dto.setCdSexo("H");
            dto.setDsEmail("pepe@pepe.es");
            dto.setDsMovil("698551133");
            dto.setDsTelefono("954887752");
            dto.setCdProvincia("41");
            dto.setCdLocalidad("85412");
            dto.setCdTipoVia("CALLE");
            dto.setDsDireccion("Gato");
            dto.setCdCodigoPostal("28080");
            dto.setDsNumero("8");
            dto.setDsPiso("2");
            dto.setDsPuerta("7");
            dto.setDsEscalera("1");

            List<PersonaAreaDTO> listaPersonaAreaDTO = new ArrayList<PersonaAreaDTO>();
            PersonaAreaDTO personaAreaDTO = new PersonaAreaDTO();
            CatalogosValoresDTO areaDto = new CatalogosValoresDTO();

            areaDto.setIdValor(12);
            areaDto.setDsValor("OK");
            areaDto.setCdCodigo("NO");
            areaDto.setRegistroActivoEditable(true);
            areaDto.setRegistroInactivoEditable(false);
            personaAreaDTO.setAreaDTO(areaDto);
            personaAreaDTO.setCdUsuAlta("OK");
            personaAreaDTO.setCdUsuBaja("NO");
            personaAreaDTO.setCdUsuModif("OK");
            personaAreaDTO.setFcAltaFila(Date.from(Instant.now()));
            personaAreaDTO.setFcBajaFila(Date.from(Instant.now()));
            personaAreaDTO.setFcModifFila(Date.from(Instant.now()));
            personaAreaDTO.setIdInteresado(12);
            personaAreaDTO.setIdPersonaArea(12);
            dto.setListaPersonaAreaDTO(listaPersonaAreaDTO);
            List<PersonaEspecDTO> listaPersonaEspecialidadDTO = new ArrayList<PersonaEspecDTO>();
            PersonaEspecDTO personaEspecDTO = new PersonaEspecDTO();
            personaEspecDTO.setIdInteresado(15);
            personaEspecDTO.setIdPersonaEspec(16);
            personaEspecDTO.setDsEspecialidad("HISTORIA");
            dto.setListaPersonaEspecialidadDTO(listaPersonaEspecialidadDTO);

            dto = this.service.guardarPersona(dto, null, true);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo buscar por Id.
     */
    @Test
    public void testBuscarPorId() {
        try {
            InteresadoDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdInteresado());

            this.testAtributos(dto, Boolean.TRUE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo buscar.
     */

    @Test
    public void testBuscar() {
        try {
            List<InteresadoDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que sernull safe.
            Iterator<InteresadoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                InteresadoDTO dto = it.next();

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
     * Prueba el metodo buscar persona.
     */
    @Test
    public void testBuscarPersona() {
        try {
            List<InteresadoDTO> lstDTO = this.service.buscarPersona(0, -1, null, null, 1000);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que sernull safe.
            Iterator<InteresadoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                InteresadoDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscarPersona(0, -1, filtros, null, 1000);

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
     * Prueba el metodo comprobarInteresado
     */
    @Test
    public void testComprobarInteresadoOK() {
        try {

            InteresadoDTO interesadoDTO = this.service.obtenerPorIdentificacion("00000001R");

            // No es nulo
            Assert.assertNotNull(interesadoDTO);

            // Esta relleno
            Assert.assertNotNull(interesadoDTO.getIdInteresado());

            // Esta relleno
            Assert.assertNotNull(interesadoDTO.getCdNumIden());

            this.testAtributos(interesadoDTO, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }
    }

    /**
     * Prueba el metodo comprobarInteresado
     */
    @Test
    public void testComprobarInteresadoKO() {
        try {

            InteresadoDTO interesadoDTO = this.service.obtenerPorIdentificacion("99");

            // No es nulo
            Assert.assertNotNull(interesadoDTO);

            // Esta relleno
            Assert.assertEquals(StringUtils.EMPTY, interesadoDTO.getCdNumIden());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }
    }

    /**
     * Prueba el metodo comprobarInteresado
     */
    @Test
    public void testObtenerTiposIdentificacion() {
        try {
            List<CatalogosValoresDTO> lstIdent = this.service.obtenerTiposIdentificacion();

            // No es nulo
            Assert.assertNotNull(lstIdent);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }
    }

    /**
     * Obtener la lista de evaluadores
     */
    @Test
    public void obtenerEvaluadoresTest() throws ServiceException {
        String identificador = "54826624G";

        List<InteresadoDTO> lstDTO = this.service.obtenerEvaluadores(identificador, null);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());
    }

    /**
     * Error de la lista de evaluadores con identificador
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerEvaluadoresTest() throws ServiceException {

        String identificador = null;
        this.service.obtenerEvaluadores(identificador, null);
    }

    /**
     * Obtener la lista de evaluadores por coordinador
     */
    @Test
    public void obtenerEvaluadoresxCoordinadorTest() throws ServiceException {
        String identificador = "54826624G";
        Integer idConvocatoria = 1114;
        String cdCodigo = "SOC";

        List<InteresadoDTO> lstDTO = this.service.obtenerEvaluadoresxCoordinador(identificador,
                idConvocatoria, cdCodigo);
        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(InteresadoDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdInteresado());
        Assert.assertNotNull(dto.getTipoIdentificacionDTO());
        Assert.assertNotNull(dto.getTipoIdentificacionDTO().getIdValor());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdNumIden()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsNombre()));
        Assert.assertNotNull(dto.getTipoActorDTO().getNmConstante());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getTipoIdentificacionDTO().getDsValor()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsApellido1()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsApellido2()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdSexo()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsEmail()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsMovil()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsTelefono()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdProvincia()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdLocalidad()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdTipoVia()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsDireccion()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdCodigoPostal()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsNumero()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsPiso()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsPuerta()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsEscalera()));

        }

        // Null Safe
        Assert.assertNotNull(dto.getCdNumIden());
        Assert.assertNotNull(dto.getDsApellido1());
        Assert.assertNotNull(dto.getDsApellido2());
        Assert.assertNotNull(dto.getCdSexo());
        Assert.assertNotNull(dto.getDsEmail());
        Assert.assertNotNull(dto.getDsMovil());
        Assert.assertNotNull(dto.getDsTelefono());
        Assert.assertNotNull(dto.getCdProvincia());
        Assert.assertNotNull(dto.getCdLocalidad());
        Assert.assertNotNull(dto.getCdTipoVia());
        Assert.assertNotNull(dto.getDsDireccion());
        Assert.assertNotNull(dto.getCdCodigoPostal());
        Assert.assertNotNull(dto.getDsNumero());
        Assert.assertNotNull(dto.getDsPiso());
        Assert.assertNotNull(dto.getDsPuerta());
        Assert.assertNotNull(dto.getDsEscalera());

    }

}
