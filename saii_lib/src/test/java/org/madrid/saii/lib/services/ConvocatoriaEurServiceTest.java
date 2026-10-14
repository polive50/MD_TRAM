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
import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEurDTO;
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
 * Test ConvocatoriaEurService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaEurServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaEurService")
    private ConvocatoriaEurService service;

    /**
     * Servicio catalogosService
     */
    @Autowired
    @Qualifier("catalogosService")
    private CatalogosService catalogosService;

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
            ConvocatoriaEurDTO dto = new ConvocatoriaEurDTO();
            ConvocatoriaDTO convDTO = new ConvocatoriaDTO();
            CatalogosValoresDTO fondo = new CatalogosValoresDTO();

            convDTO.setIdConvocatoria(12);
            fondo.setIdValor(950);

            dto.setIdConvocatoriaEur(4);
            dto.setConvocatoriaDTO(convDTO);
            dto.setFondoEuropeoDTO(fondo);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setCdUsuModif(Constantes.Test.TEST_ADMIN);
            dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setFcModifFila(new Date());
            dto.setFcBajaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaEur ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaEur.class,
                    dto.getIdConvocatoriaEur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdUsuModif("SAIIUSU1");
            dto.setConvocatoriaDTO(null);
            dto.setCdUsuBaja("usuario");
            dto.setCdUsuAlta("usuario2");
            dto.setFondoEuropeoDTO(null);
            dto.setFcBajaFila(null);
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("SAIIUSU1", dto.getCdUsuModif());
            Assert.assertEquals(null, dto.getConvocatoriaDTO());
            Assert.assertEquals("usuario", dto.getCdUsuBaja());
            Assert.assertEquals("usuario2", dto.getCdUsuAlta());
            Assert.assertEquals(null, dto.getFondoEuropeoDTO());
            Assert.assertEquals(null, dto.getFcBajaFila());

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
            List<ConvocatoriaEurDTO> lstDTO = this.service.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaEurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaEurDTO dto = it.next();

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
            ConvocatoriaEurDTO dto = this.service.buscarId(5);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaEur());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaEurDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaEurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaEurDTO dto = it.next();

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
     * comprueba los metodos propios
     */
    @Test
    public void testBuscarIdCatalogoValores() {
        try {

            Integer idConvocatoria = 12;
            Integer idCatalogoValor = 1;
            ConvocatoriaEurDTO convEurDTO = new ConvocatoriaEurDTO();

            convEurDTO = this.service.buscarIdCatalogoValores(idCatalogoValor, idConvocatoria);

            // No es nulo
            Assert.assertNotNull(convEurDTO);

            //esta relleno el id
            Assert.assertNotNull(convEurDTO.getIdConvocatoriaEur());

            Assert.assertEquals(Integer.valueOf(5), convEurDTO.getIdConvocatoriaEur());

            this.testAtributos(convEurDTO, false);

            //Busqueda vacia
            convEurDTO = this.service.buscarIdCatalogoValores(0, 0);

            //No es nulo
            Assert.assertNotNull(convEurDTO);

            //esta vacio
            Assert.assertNull(convEurDTO.getIdConvocatoriaEur());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }

    }

    @Test
    public void testBuscarIdConvocatoria() {
        try {
            List<ConvocatoriaEurDTO> lstDTO = this.service.buscarIdConvocatoria(12);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            //no esta vacia
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaEurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaEurDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }
            // Búsqueda vacia
            lstDTO = this.service.buscarIdConvocatoria(0);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (ServiceException e) {

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
            ConvocatoriaEurDTO dto = this.service.buscarId(5);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // Limpiamos la instancia
            ConvocatoriaEur ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaEur.class,
                    dto.getIdConvocatoriaEur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un borrado logico
            dto = this.service.borradoLogico(dto);

            // se pone nombre a mano porque no detecta usuario testeando
            dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);

            this.testAtributos(dto, Boolean.TRUE);

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
            ConvocatoriaEurDTO dto = this.service.buscarId(5);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // lo damos de baja
            dto.setCdUsuBaja("SAIIUSU1");
            dto.setFcBajaFila(new Date());

            // Limpiamos la instancia
            ConvocatoriaEur ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaEur.class,
                    dto.getIdConvocatoriaEur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos el dto con la modificación
            this.service.guardar(dto);

            // Se comprueba que esta dado de baja correctamente
            Assert.assertNotNull(dto.getFcBajaFila());

            // Limpiamos la instancia
            ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaEur.class,
                    dto.getIdConvocatoriaEur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un alta logica
            dto = this.service.altaLogica(dto);

            // Comprobamos que se ha realizado el alta logica
            Assert.assertNull(dto.getFcBajaFila());

            this.testAtributos(dto, Boolean.FALSE);
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
    private void testAtributos(ConvocatoriaEurDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaEur());
        Assert.assertNotNull(dto.getConvocatoriaDTO());
        Assert.assertNotNull(dto.getFondoEuropeoDTO());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertNotNull(dto.getCdUsuBaja());

        }

    }

}
