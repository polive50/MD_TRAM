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
import org.madrid.saii.lib.domain.Entidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test EntidadService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class EntidadServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("entidadService")
    private EntidadService service;

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
            EntidadDTO dto = new EntidadDTO();
            InteresadoDTO interesado = new InteresadoDTO();
            CatalogosValoresDTO estado = new CatalogosValoresDTO();

            interesado.setIdInteresado(1);
            interesado.setCdNumIden("ADMIN1234");
            estado.setIdValor(100);
            dto.setInteresadoDTO(interesado);
            dto.setEstadoDTO(estado);
            dto.setIdEntidad(2);
            dto.setFcAltaFila(new Date());
            dto.setCdUsuBaja("ADMIN");
            dto.setCdUsuModif("ADMIN");
            dto.setFcModifFila(new Date());
            dto.setFcBajaFila(new Date());
            dto.setDsAbrev("ADMIN");
            dto.setDsCentro("ADMIN");
            dto.setDsDepartamento("ADMIN");
            dto.setDsFax("ADMIN");
            dto.setTlWeb("ADMIN");

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            Entidad ent = this.sessionFactory.getCurrentSession().get(Entidad.class,
                    dto.getIdEntidad());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsCentro("Prueba Test Mod");
            dto.setTlWeb("Prueba Test Mod");
            dto.setDsFax("Prueba Test Mod");
            dto.setDsDepartamento("Prueba Test Mod");
            dto.setDsAbrev("Prueba Test Mod");
            dto.setCdUsuModif("Prueba Test Mod");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("Prueba Test Mod", dto.getDsCentro());
            Assert.assertEquals("Prueba Test Mod", dto.getTlWeb());
            Assert.assertEquals("Prueba Test Mod", dto.getDsFax());
            Assert.assertEquals("Prueba Test Mod", dto.getDsDepartamento());
            Assert.assertEquals("Prueba Test Mod", dto.getDsAbrev());
            Assert.assertEquals("Prueba Test Mod", dto.getCdUsuModif());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar pasando null por parametro para idInteresado
     */
    @Test
    public void testGuardarNull() {
        try {

            // GUARDAR
            EntidadDTO dto = new EntidadDTO();
            InteresadoDTO interesado = new InteresadoDTO();
            CatalogosValoresDTO estado = new CatalogosValoresDTO();

            interesado.setIdInteresado(null);
            interesado.setCdNumIden("ADMIN1234");
            estado.setIdValor(100);
            dto.setInteresadoDTO(interesado);
            dto.setEstadoDTO(estado);
            dto.setIdEntidad(2);
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Entidad ent = this.sessionFactory.getCurrentSession().get(Entidad.class,
                    dto.getIdEntidad());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsCentro("Prueba Test Mod");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("Prueba Test Mod", dto.getDsCentro());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener
     */
    @Test
    public void testObtener() {
        try {
            List<EntidadDTO> lstDTO = this.service.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<EntidadDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                EntidadDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            EntidadDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdEntidad());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<EntidadDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<EntidadDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                EntidadDTO dto = it.next();

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

    @Test
    public void testBuscarPorCif() {
        try {
            EntidadDTO dto = this.service.buscarCif("00000001R");

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdEntidad());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testObtenerListaEstadosEntidad() {
        try {
            List<CatalogosValoresDTO> lstEstados = this.service.obtenerListaEstadosEntidad();

            // No es nulo
            Assert.assertNotNull(lstEstados.get(0));

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba el borrado logico
     */
    @Test
    public void testBorradoLogico() {

        try {
            EntidadDTO dto = this.service.buscarId(1);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // Limpiamos la instancia
            Entidad ent = this.sessionFactory.getCurrentSession().get(Entidad.class,
                    dto.getIdEntidad());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un borrado logico
            this.service.borradoLogico(dto);

            // Comprobamos que se ha realizado
            dto = this.service.buscarId(dto.getIdEntidad());

            // se pone nombre a mano porque no detecta usuario testeando
            dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);

            Assert.assertNotNull(dto.getFcBajaFila());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba el alta logica
     */
    @Test
    public void testAltaLogica() {

        try {
            EntidadDTO dto = this.service.buscarId(1);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // lo damos de baja
            dto.setCdUsuBaja("SAIIUSU1");
            dto.setFcBajaFila(new Date());

            // Limpiamos la instancia
            Entidad ent = this.sessionFactory.getCurrentSession().get(Entidad.class,
                    dto.getIdEntidad());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos el dto con la modificación
            this.service.guardar(dto);

            // Se comprueba que esta dado de baja correctamente
            Assert.assertNotNull(dto.getFcBajaFila());

            // Limpiamos la instancia
            ent = this.sessionFactory.getCurrentSession().get(Entidad.class, dto.getIdEntidad());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un alta logica
            this.service.altaLogica(dto);

            // Comprobamos que se ha realizado el alta logica
            dto = this.service.buscarId(dto.getIdEntidad());
            Assert.assertNull(dto.getFcBajaFila());

            this.testAtributos(dto, false);

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
     *
     */
    private void testAtributos(EntidadDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdEntidad());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getInteresadoDTO());
        Assert.assertNotNull(dto.getEstadoDTO());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsAbrev()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsCentro()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsDepartamento()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsFax()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getTlWeb()));
        }
    }

}
