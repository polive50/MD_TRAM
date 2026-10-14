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
import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ConvocatoriaCauService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaCauServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaCauService")
    private ConvocatoriaCauService service;

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
     * Prueba el metodo agregar nuevo
     */
    @Test
    public void testAgregarNuevo() {
        try {

            // Agregar
            ConvocatoriaCauDTO dto = this.service.agregar(1, 8);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Assert.assertNotNull(dto);

            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());

            Assert.assertNull(dto.getCdUsuBaja());
            Assert.assertNull(dto.getCdUsuModif());
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getFcModifFila());

            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo agregar en baja
     */
    @Test
    public void testAgregarExistenteEnBaja() {
        try {

            // Agregar nuevo
            ConvocatoriaCauDTO dto = this.service.agregar(1, 4);
            dto.setCdUsuBaja(null);
            dto.setFcBajaFila(null);
            dto.setCdUsuModif(UserUtils.getUserName());
            dto.setFcModifFila(new Date());

            // No es nulo
            Assert.assertNotNull(dto);

            dto.setFcAltaFila(new Date());
            dto.setCdUsuAlta("SAIUSU1");

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Assert.assertEquals("SAIUSU1", dto.getCdUsuAlta());
            Assert.assertEquals(UserUtils.getUserName(), dto.getCdUsuModif());
            Assert.assertNull(dto.getCdUsuBaja());
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getFcModifFila());

            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo agregar en alta
     */
    @Test
    public void testAgregarExistenteEnAlta() {
        try {

            // Agregar
            ConvocatoriaCauDTO dto = this.service.agregar(1, 3);

            dto.setFcBajaFila(null);
            dto.setCdUsuBaja(null);

            Assert.assertNotNull(dto);
            Assert.assertNotNull(dto.getIdConvocatoria());
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getCdUsuBaja());

            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            if (!Constantes.Error.MSG_WARNING.equals(e.getKeyExcepcion())) {
                fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
            }
        }
    }

    /**
     * Prueba el metodo buscar por id
     */
    @Test
    public void testBuscarPorId() {
        try {
            ConvocatoriaCauDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaCau());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo buscar
     */
    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaCauDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaCauDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaCauDTO dto = it.next();

                Assert.assertNotNull(dto);

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
     * Comprueba el actualizarNormativa
     */
    @Test
    public void testActualizarNormativa() {
        try {
            ConvocatoriaCauDTO dto = this.service.buscarId(2);
            dto.setTlNormativa("Prueba");
            dto.setIdConvocatoria(1);
            dto.setIdConvocatoriaCau(1);
            dto.setFcAltaFila(new Date());
            dto.setCdUsuAlta("SAIUSU1");
            dto.setCdUsuBaja(null);
            dto.setCdUsuModif("SAIUSU1");
            dto.setFcModifFila(new Date());
            dto.setFcBajaFila(null);

            Assert.assertNotNull(dto);

            dto = this.service.actualizarNormativa(dto.getIdConvocatoria(),
                    dto.getIdConvocatoriaCau(), dto.getTlNormativa());

            Assert.assertEquals("Prueba", dto.getTlNormativa());
            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());
            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuModif());
            Assert.assertNull(dto.getCdUsuBaja());
            Assert.assertNull(dto.getFcBajaFila());

            Assert.assertNotNull(dto.getIdConvocatoria());

            Assert.assertNotNull(dto);

            this.testAtributos(dto, Boolean.FALSE);

            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            Assert.assertNotNull(dto);

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
            ConvocatoriaCauDTO dto = this.service.buscarId(2);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            Assert.assertNotNull(dto.getIdConvocatoriaCau());

            // Limpiamos la instancia
            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un borrado logico
            dto = this.service.borradoLogico(dto);
            dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);
            dto.setCdUsuModif(Constantes.Test.TEST_ADMIN);
            dto.setFcModifFila(new Date());

            this.testAtributos(dto, Boolean.TRUE);

            Assert.assertNotNull(dto);

            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuBaja());
            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());
            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuModif());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba el borrado lógico causa otros
     */
    @Test
    public void testBorradoLogicoOtros() {

        try {
            ConvocatoriaCauDTO dto = this.service.buscarId(1);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // Limpiamos la instancia
            ConvocatoriaCau ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaCau.class,
                    dto.getIdConvocatoriaCau());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un borrado logico
            dto = this.service.borradoLogico(dto);

            Assert.assertNotNull(dto);

            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getFcAltaFila());
            Assert.assertNotNull(dto.getCdUsuAlta());
            Assert.assertNull(dto.getCdUsuBaja());
            Assert.assertNotNull(dto.getCdUsuModif());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            if (!Constantes.Error.MSG_WARNING.equals(e.getKeyExcepcion())) {
                fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
            }
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaCauDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertNotNull(dto.getCausaDTO());
        Assert.assertNotNull(dto.getIdConvocatoriaCau());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertNotNull(dto.getFcBajaFila());
        }

    }

}
