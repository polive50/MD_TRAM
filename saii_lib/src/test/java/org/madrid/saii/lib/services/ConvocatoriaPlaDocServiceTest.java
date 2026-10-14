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
import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
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
 * Test ConvocatoriaPlaDocService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaPlaDocServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaPlaDocService")
    private ConvocatoriaPlaDocService service;

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
            ConvocatoriaPlaDocDTO dto = new ConvocatoriaPlaDocDTO();
            dto.setIdConvocatoriaPla(4);
            dto.setCdUsuAlta("ADMIN");
            dto.setFcAltaFila(new Date());
            dto.setIdConvocatoriaPlaDoc(3);
            dto.setFcBajaFila(new Date());
            dto.setCdUsuBaja("usuario");
            dto.setCdUsuModif("usuario2");
            dto.setFcModifFila(new Date());
            dto.setDsDocumentacion("documentacion");
            dto.setDsObservaciones("observaciones");
            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaPlaDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaDoc.class, dto.getIdConvocatoriaPlaDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdUsuModif("ADMIN");
            dto.setFcModifFila(null);
            dto.setFcBajaFila(new Date(1000));
            dto.setCdUsuBaja("prueba");

            dto = this.service.guardar(dto);

            this.testAtributos(dto, Boolean.FALSE);

            // La modificación es correcta
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
            List<ConvocatoriaPlaDocDTO> lstDTO = this.service.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaPlaDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaPlaDocDTO dto = it.next();

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
            ConvocatoriaPlaDocDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaPlaDoc());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaPlaDocDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaPlaDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaPlaDocDTO dto = it.next();

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
    private void testAtributos(ConvocatoriaPlaDocDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaPlaDoc());

        Assert.assertNotNull(dto.getIdConvocatoriaPla());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertNotNull(dto.getFcBajaFila());

            Assert.assertNotNull(dto.getCdUsuModif());

            Assert.assertNotNull(dto.getCdUsuBaja());

            Assert.assertNotNull(dto.getDsDocumentacion());
            
            Assert.assertNotNull(dto.getDsObservaciones());
        }

    }

    @Test
    public void testAltaLogica() {
        try {

            ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO = this.service.buscarId(8);

            Assert.assertNotNull(convocatoriaPlaDocDTO);

            ConvocatoriaPlaDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaDoc.class, convocatoriaPlaDocDTO.getIdConvocatoriaPlaDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.altaLogica(convocatoriaPlaDocDTO);

            convocatoriaPlaDocDTO = this.service.buscarId(8);

            Assert.assertNotNull(convocatoriaPlaDocDTO);
            Assert.assertNull(convocatoriaPlaDocDTO.getFcBajaFila());
            Assert.assertNull(convocatoriaPlaDocDTO.getDsDocumentacion());
            Assert.assertNull(convocatoriaPlaDocDTO.getDsObservaciones());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBorradoLogico() {
        try {

            ConvocatoriaPlaDocDTO convocatoriaPlaDocDTO = this.service.buscarId(7);

            Assert.assertNotNull(convocatoriaPlaDocDTO);

            ConvocatoriaPlaDoc ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaPlaDoc.class, convocatoriaPlaDocDTO.getIdConvocatoriaPlaDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.borradoLogico(convocatoriaPlaDocDTO);

            convocatoriaPlaDocDTO = this.service.buscarId(7);

            Assert.assertNotNull(convocatoriaPlaDocDTO);
            Assert.assertNotNull(convocatoriaPlaDocDTO.getFcBajaFila());
            Assert.assertNull(convocatoriaPlaDocDTO.getDsDocumentacion());
            Assert.assertNull(convocatoriaPlaDocDTO.getDsObservaciones());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
