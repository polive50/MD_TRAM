package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
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
 * Test ConvocatoriaTipoDocService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaTipoDocServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService service;

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
            ConvocatoriaTipoDocDTO dto = this.service.agregar(1, 956, null);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            // No es nulo
            Assert.assertNotNull(dto);

            ConvocatoriaTipoDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaTipoDoc.class, dto.getIdConvocatoriaTipoDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo agregar en alta
     */
    @Test
    public void testAgregarEnAlta() {
        try {

            // Agregar
            ConvocatoriaTipoDocDTO dto = this.service.agregar(1, 955, null);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            ConvocatoriaTipoDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaTipoDoc.class, dto.getIdConvocatoriaTipoDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            if (!Constantes.Error.MSG_WARNING.equals(e.getKeyExcepcion())) {
                fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
            }
        }
    }

    /**
     * Prueba el metodo agregar en baja
     */
    @Test
    public void testAgregarEnBaja() {
        try {

            // Agregar
            ConvocatoriaTipoDocDTO dto = this.service.agregar(1, 957, null);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            // No es nulo
            Assert.assertNotNull(dto);

            ConvocatoriaTipoDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaTipoDoc.class, dto.getIdConvocatoriaTipoDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Está relleno
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
            List<ConvocatoriaTipoDocDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaTipoDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaTipoDocDTO dto = it.next();

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
     * Prueba el metodo buscar por convocatoria
     */
    @Test
    public void testbuscarPorConvocatoria() {
        try {
            List<ConvocatoriaTipoDocDTO> lstDTO = this.service.buscarPorConvocatoria(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaTipoDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaTipoDocDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarPorConvocatoria(2);

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
     *
     */
    @Test
    public void testBuscarPorConvocatoriaTipoDocumento() {
        try {
            List<ConvocatoriaTipoDocDTO> lstDTO = this.service
                    .buscarPorConvocatoriaTipoDocumento(28, 172);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaTipoDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaTipoDocDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarPorConvocatoria(2);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (ServiceException e) {
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaTipoDocDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertNotNull(dto.getTipoDocumentoDTO());
        Assert.assertNotNull(dto.getIdConvocatoriaTipoDoc());
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
