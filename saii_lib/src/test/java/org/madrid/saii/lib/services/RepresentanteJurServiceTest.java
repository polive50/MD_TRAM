package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.RepresentanteJur;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.RepresentanteJurFiltrosDTO;
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
 * Test RepresentanteJurService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class RepresentanteJurServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService service;

    /**
     * Servicio de interesadoService
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;

    /**
     * Servicio de catalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
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
     *
     */
    @Test
    public void testGuardar() throws ServiceException {
        try {

            // GUARDAR
            EntidadDTO entidadJurDTO = new EntidadDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            CatalogosValoresDTO estadoDto = new CatalogosValoresDTO();
            RepresentanteJurDTO dto = new RepresentanteJurDTO();
            entidadJurDTO.setIdEntidad(1);
            interesadoDTO.setIdInteresado(1);
            estadoDto.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

            dto.setEntidadDTO(entidadJurDTO);
            dto.setInteresadoDTO(interesadoDTO);
            dto.setEstadoDTO(estadoDto);
            dto.setDsCargo("Responsable de adquisición de equipos");
            dto.setIdRepresentanteJur(15);

            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setFcModifFila(new Date());
            dto.setFcVigenciaFin(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // devuelve el valor correcto
            Assert.assertEquals("Responsable de adquisición de equipos", dto.getDsCargo());

            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());

            Assert.assertEquals(Constantes.EstadoSolicitud.INICIADA, dto.getEstadoDTO().getIdValor());

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            // Limpiamos session
            this.sessionFactory.getCurrentSession().clear();

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar con valores a null
     *
     */
    @Test
    public void testGuardarValoresNull() throws ServiceException {
        try {

            // GUARDAR
            EntidadDTO entidadJurDTO = new EntidadDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            CatalogosValoresDTO estadoDto = new CatalogosValoresDTO();
            RepresentanteJurDTO dto = new RepresentanteJurDTO();
            entidadJurDTO.setIdEntidad(1);
            interesadoDTO.setIdInteresado(null);
            estadoDto.setIdValor(null);


            dto.setEntidadDTO(entidadJurDTO);
            dto.setInteresadoDTO(interesadoDTO);
            dto.setEstadoDTO(estadoDto);
            dto.setDsCargo("Responsable de adquisición de equipos");

            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setFcModifFila(new Date());
            dto.setFcVigenciaFin(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // devuelve el valor correcto
            Assert.assertEquals("Responsable de adquisición de equipos", dto.getDsCargo());

            Assert.assertEquals(null, dto.getCdUsuAlta());

            Assert.assertEquals(null, dto.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            // Limpiamos session
            this.sessionFactory.getCurrentSession().clear();

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo buscarPorId
     *
     */
    @Test
    public void testBuscarPorId() {
        try {
            RepresentanteJurDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdRepresentanteJur());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo borrado logico
     *
     */
    @Test
    public void testBorradoLogico() {
        try {
            // Obtiene un RepresentanteJurDTO
            RepresentanteJurDTO dto = new RepresentanteJurDTO();
            dto = this.service.buscarId(1);

            // liberar instancia
            RepresentanteJur ent = this.sessionFactory.getCurrentSession()
                    .get(RepresentanteJur.class, dto.getIdRepresentanteJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Se da de baja
            this.service.borradoLogico(dto);

            // Se vuelve a buscar
            dto = this.service.buscarId(1);

            // Su fecha de baja debe no ser nula
            Assert.assertNotNull(dto.getFcBajaFila());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo alta logica
     *
     */
    @Test
    public void testAltaLogica() {
        try {
            // Obtiene un RepresentanteJurDTO
            RepresentanteJurDTO dto = new RepresentanteJurDTO();
            InteresadoDTO interesadoDTO = new InteresadoDTO();
            EntidadDTO entidadJurDTO = new EntidadDTO();
            CatalogosValoresDTO estadoDto = new CatalogosValoresDTO();
            entidadJurDTO.setIdEntidad(1);
            interesadoDTO.setIdInteresado(1);
            estadoDto.setIdValor(16);

            dto.setEntidadDTO(entidadJurDTO);
            dto.setInteresadoDTO(interesadoDTO);
            dto.setEstadoDTO(estadoDto);

            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);
            dto.setFcBajaFila(new Date());

            dto = this.service.guardar(dto);

            // liberar instancia
            RepresentanteJur ent = this.sessionFactory.getCurrentSession()
                    .get(RepresentanteJur.class, dto.getIdRepresentanteJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Se da de baja
            this.service.altaLogica(dto);

            // Se vuelve a buscar
            dto = this.service.buscarId(1);

            // Su fecha de baja debe no ser nula
            Assert.assertNull(dto.getFcBajaFila());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo filtrar
     *
     */
    @Test
    public void filtrar() {
        try {

            RepresentanteJurFiltrosDTO filtros = new RepresentanteJurFiltrosDTO();
            CatalogosValoresDTO estado = new CatalogosValoresDTO();
            estado = this.catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

            filtros.setEstadoDTO(estado);
            filtros.setCdNumIdenEntidad("28631189F");

            List<RepresentanteJurDTO> dto = this.service.filtrar(filtros);

            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el buscar
     *
     */
    @Test
    public void testBuscar() {
        try {
            List<RepresentanteJurDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<RepresentanteJurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                RepresentanteJurDTO dto = it.next();

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
     * Test obtener estado de representante legal
     *
     */
    @Test
    public void testObtenerEstadoRepresentanteLegal() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.obtenerEstadoRepresentanteLegal();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

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
    private void testAtributos(RepresentanteJurDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdRepresentanteJur());
        Assert.assertNotNull(dto.getEstadoDTO());
        Assert.assertNotNull(dto.getEntidadDTO());
        Assert.assertNotNull(dto.getEntidadDTO().getInteresadoDTO());
        Assert.assertNotNull(dto.getInteresadoDTO());
        Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());

        if (Boolean.TRUE.equals(bRelleno)) {
            // Assert.assertNotNull(dto.getFcVigenciaIni());
            //
            Assert.assertNotNull(dto.getFcVigenciaFin());
            //
            // Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            //
            Assert.assertNotNull(dto.getFcModifFila());
            //
            //Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            //
            // Assert.assertNotNull(dto.getFcBajaFila());
            //
            Assert.assertNotNull(dto.getDsCargo());
        }

    }

}
