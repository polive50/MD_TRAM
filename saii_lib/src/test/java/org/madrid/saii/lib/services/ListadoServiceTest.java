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
import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ListadoDTO;
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
 * Test ListadoService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ListadoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("listadoService")
    private ListadoService service;

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
            ListadoDTO dto = new ListadoDTO();
            dto.setIdConvocatoria(3);
            dto.setDsTitulo("prueba de ejemplo");
            dto.setCdUsuAlta("SAIUSU1");
            dto.setCdUsuBaja("SAIUSU1");
            dto.setCdUsuModif("SAIUSU1");
            dto.setFcAltaFila(new Date());
            dto.setFcBajaFila(new Date());
            dto.setFcModifFila(new Date());


            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            Listado ent = this.sessionFactory.getCurrentSession().get(Listado.class,
                    dto.getIdListado());
            this.sessionFactory.getCurrentSession().evict(ent);

            Assert.assertNotNull(dto);

            // Guardamos

            // dto.setDsConvocatoria("Prueba Test Mod");
            dto = this.service.guardar(dto);
            dto.setDsTitulo("nombre de prueba");
            dto.setFcModifFila(new Date());
            dto.setCdUsuModif("SAIUSU2");
            dto.setCdUsuBaja("SAIUSU2");
            dto.setFcBajaFila(new Date());

            Assert.assertNotNull(dto);

            // La modificación es correcta
            Assert.assertEquals("nombre de prueba", dto.getDsTitulo());
            Assert.assertEquals("SAIUSU1", dto.getCdUsuAlta());
            Assert.assertEquals("SAIUSU2", dto.getCdUsuBaja());
            Assert.assertEquals("SAIUSU2", dto.getCdUsuModif());
            // Assert.assertEquals("Prueba Test Mod", dto.getDsConvocatoria());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            ListadoDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdListado());

            Assert.assertNotNull(dto.getIdConvocatoria());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ListadoDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ListadoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ListadoDTO dto = it.next();

                Assert.assertNotNull(dto);

                this.testAtributos(dto, Boolean.TRUE);
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
    public void testObtenerListadoExcel() {
        try {
            byte[] obtenerListadoExcel = this.service.obtenerListadoExcel(1);

            // No es nulo
            Assert.assertNotNull(obtenerListadoExcel);

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
    private void testAtributos(ListadoDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdListado());
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsTitulo()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getCdUsuBaja());
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getFcModifFila());
        }

    }

}
