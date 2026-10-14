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
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
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
 * Test ConvocatoriaAreaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaAreaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaAreaService")
    private ConvocatoriaAreaService service;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

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

    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaAreaDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaAreaDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaAreaDTO dto = it.next();

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
    public void testObtenerAreasCientificasPorConvocatoria() {
        try {
            List<ConvocatoriaAreaDTO> lstDTO = this.service
                    .obtenerAreasCientificasPorConvocatoria(24);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaAreaDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaAreaDTO dto = it.next();

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
    public void testAgregar() {
        try {

            List<CatalogosValoresDTO> lstAreas = this.catalogosValoresService.buscarPorCatalogo(20);

            // No es nulo
            Assert.assertNotNull(lstAreas);

            // Está relleno
            Assert.assertFalse(lstAreas.isEmpty());

            // AGREGAR
            ConvocatoriaAreaDTO dto = new ConvocatoriaAreaDTO();

            dto = this.service.agregar(24, lstAreas.get(4).getIdValor());

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            ConvocatoriaArea ent = this.sessionFactory.getCurrentSession()
                    .get(ConvocatoriaArea.class, dto.getIdConvocatoriaArea());
            this.sessionFactory.getCurrentSession().evict(ent);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testAgregarAltaArea() {
        try {

            List<CatalogosValoresDTO> lstAreas = this.catalogosValoresService.buscarPorCatalogo(20);

            // No es nulo
            Assert.assertNotNull(lstAreas);

            // Está relleno
            Assert.assertFalse(lstAreas.isEmpty());

            // AGREGAR
            ConvocatoriaAreaDTO dto = new ConvocatoriaAreaDTO();

            dto = this.service.agregar(24, lstAreas.get(2).getIdValor());

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test(expected = ServiceException.class)
    public void testAgregarDuplicado() throws ServiceException {

        List<CatalogosValoresDTO> lstAreas = this.catalogosValoresService.buscarPorCatalogo(20);

        // AGREGAR
        this.service.agregar(24, lstAreas.get(0).getIdValor());

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaAreaDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaArea());

        Assert.assertNotNull(dto.getIdConvocatoria());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

            Assert.assertNotNull(dto.getFcAltaFila());

        }

    }

}
