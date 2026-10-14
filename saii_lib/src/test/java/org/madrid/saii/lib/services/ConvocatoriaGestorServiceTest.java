package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ConvocatoriaGestorService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaGestorServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaGestorService")
    private ConvocatoriaGestorService service;

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
            ConvocatoriaGestorDTO dto = new ConvocatoriaGestorDTO();
            dto.setIdConvocatoriaGestor(2);
            dto.setIdConvocatoria(1);
            dto.setCdUsuario("ADMIN");
            dto.setCdUsuAlta("usuario");
            dto.setFcBajaFila(new Date());
            dto.setFcAltaFila(new Date());
            dto.setCdUsuModif("usuario2");
            dto.setFcModifFila(new Date());
            dto.setCdUsuBaja("usuario3");

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Esta relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaGestor ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaGestor.class, dto.getIdConvocatoriaGestor());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdUsuAlta("usuario 1");
            dto.setFcModifFila(null);
            dto.setCdUsuario("User");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("usuario 1", dto.getCdUsuAlta());
            Assert.assertEquals(null, dto.getFcModifFila());
            Assert.assertEquals("User", dto.getCdUsuario());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test obtener lista conv gestor por id conv.
     */
    @Test
    public void testObtenerListaConvGestorPorIdConv() {
        try {
            List<ConvocatoriaGestorDTO> lstDTO = this.service.obtenerListaConvGestorPorIdConv(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaGestorDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaGestorDTO dto = it.next();

                this.testAtributos(dto, null);
            }

            // Búsqueda vacia
            lstDTO = this.service.obtenerListaConvGestorPorIdConv(99);

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
     * Testo obtener por id convocatoria codigo usuario.
     */
    @Test
    public void testoObtenerPorIdConvocatoriaCodigoUsuario() {
        try {
            ConvocatoriaGestorDTO dto = this.service.obtenerPorIdConvocatoriaCodigoUsuario(1,
                    "SAIIUSU4");

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());
            Assert.assertNotNull(dto.getCdUsuario());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test buscar por usuario.
     */
    @Test
    public void testBuscarPorUsuario() {
        try {
            List<ConvocatoriaGestorDTO> lstConv = this.service.buscarPorUsuario("SAIIUSU4");

            // No es nulo
            Assert.assertNotNull(lstConv);

            // Está relleno o vacío
            Assert.assertFalse(lstConv.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaGestorDTO> it = lstConv.iterator();
            while (it.hasNext()) {
                ConvocatoriaGestorDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstConv = this.service.buscarPorUsuario("");

            // No es nulo
            Assert.assertNotNull(lstConv);

            // Está vacio
            Assert.assertTrue(lstConv.isEmpty());

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
    private void testAtributos(ConvocatoriaGestorDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaGestor());
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuario()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertNotNull(dto.getCdUsuBaja());
            Assert.assertNotNull(dto.getFcBajaFila());
        }

    }

}
