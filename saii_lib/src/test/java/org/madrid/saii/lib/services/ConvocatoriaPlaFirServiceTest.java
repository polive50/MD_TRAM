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
import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
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
 * Test ConvocatoriaPlaFirService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaPlaFirServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaPlaFirService")
    private ConvocatoriaPlaFirService service;

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
            ConvocatoriaPlaFirDTO dto = new ConvocatoriaPlaFirDTO();
            dto.setCdFirmante("PRUEBA");
            dto.setIdConvocatoriaPla(4);
            dto.setCdUsuAlta("ADMIN");
            dto.setFcAltaFila(new Date());
            dto.setIdConvocatoriaPlaFir(3);
            dto.setFcBajaFila(new Date());
            dto.setCdUsuBaja("usuario");
            dto.setCdUsuModif("usuario2");
            dto.setFcModifFila(new Date());
            dto.setDsFirmante("firmante");
            dto.setAsignado("asignado");
            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaPlaFir ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaFir.class, dto.getIdConvocatoriaPlaFir());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdFirmante("PRUEBA_2");
            dto.setCdUsuModif("ADMIN");
            dto.setFcModifFila(null);
            dto.setFcBajaFila(new Date(1000));
            dto.setCdUsuBaja("prueba");

            dto = this.service.guardar(dto);

            this.testAtributos(dto, Boolean.FALSE);

            // La modificación es correcta
            Assert.assertEquals("PRUEBA_2", dto.getCdFirmante());
            Assert.assertEquals("ADMIN", dto.getCdUsuModif());
            Assert.assertEquals(null, dto.getFcModifFila());
            Assert.assertEquals(new Date(1000), dto.getFcBajaFila());
            Assert.assertEquals("prueba", dto.getCdUsuBaja());

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
            List<ConvocatoriaPlaFirDTO> lstDTO = this.service.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaPlaFirDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaPlaFirDTO dto = it.next();

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
            ConvocatoriaPlaFirDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaPlaFir());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaPlaFirDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaPlaFirDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaPlaFirDTO dto = it.next();

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
    private void testAtributos(ConvocatoriaPlaFirDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaPlaFir());

        Assert.assertNotNull(dto.getIdConvocatoriaPla());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdFirmante()));

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertNotNull(dto.getFcBajaFila());

            Assert.assertNotNull(dto.getCdUsuModif());

            Assert.assertNotNull(dto.getCdUsuBaja());

            Assert.assertNotNull(dto.getCdFirmante());
        }

    }

    @Test
    public void testAltaLogica() {
        try {

            ConvocatoriaPlaFirDTO convocatoriaPlafirDTO = this.service.buscarId(8);

            Assert.assertNotNull(convocatoriaPlafirDTO);

            ConvocatoriaPlaFir ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaFir.class, convocatoriaPlafirDTO.getIdConvocatoriaPlaFir());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.altaLogica(convocatoriaPlafirDTO);

            convocatoriaPlafirDTO = this.service.buscarId(8);

            Assert.assertNotNull(convocatoriaPlafirDTO);
            Assert.assertNull(convocatoriaPlafirDTO.getFcBajaFila());
            Assert.assertNull(convocatoriaPlafirDTO.getDsFirmante());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBorradoLogico() {
        try {

            ConvocatoriaPlaFirDTO convocatoriaPlafirDTO = this.service.buscarId(7);

            Assert.assertNotNull(convocatoriaPlafirDTO);

            ConvocatoriaPlaFir ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaFir.class, convocatoriaPlafirDTO.getIdConvocatoriaPlaFir());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.borradoLogico(convocatoriaPlafirDTO);

            convocatoriaPlafirDTO = this.service.buscarId(7);

            Assert.assertNotNull(convocatoriaPlafirDTO);
            Assert.assertNotNull(convocatoriaPlafirDTO.getFcBajaFila());
            Assert.assertNull(convocatoriaPlafirDTO.getDsFirmante());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
