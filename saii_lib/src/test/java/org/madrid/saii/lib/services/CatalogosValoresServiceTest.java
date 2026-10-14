package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
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
 * Test CatalogosValoresService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class CatalogosValoresServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService service;

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

    @Test
    public void testBuscarPorId() {
        try {
            CatalogosValoresDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getCatalogoDTO());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorCatalogo() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.buscarPorCatalogo(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<CatalogosValoresDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<CatalogosValoresDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();

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
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            CatalogosValoresDTO dto = new CatalogosValoresDTO();
            dto.setCdCodigo("CODIGO");
            dto.setDsValor("VALOR");
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());
            dto.setCatalogoDTO(new CatalogosDTO(1));
            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            CatalogosValores ent = this.sessionFactory.getCurrentSession()
                    .get(CatalogosValores.class, dto.getIdValor());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsValor(Constantes.Test.TEST_MOD);
            dto = this.service.guardar(dto);

            // La modificacion es correcta
            Assert.assertEquals(Constantes.Test.TEST_MOD, dto.getDsValor());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testQuitarRepetidos() {
        try {
            List<AtlasFilter> lstFiltros = new ArrayList<>();
            lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, 16,
                    AtlasFilter.Operador.EQUALS));

            List<CatalogosValoresDTO> lstCatalogoValores = this.service.buscarNoRep(0, -1,
                    lstFiltros, null);

            Assert.assertNotNull(lstCatalogoValores);

            Assert.assertFalse(lstCatalogoValores.isEmpty());

            List<CatalogosValoresDTO> lstRepetidos = lstCatalogoValores.stream()
                    .filter(f -> Collections.frequency(lstCatalogoValores, f.getDsValor()) > 1)
                    .collect(Collectors.toList());

            Assert.assertTrue(lstRepetidos.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     *
     */
    @Test
    public void testQuitarRepetidosContador() {
        try {
            List<CatalogosValoresDTO> lstCatalogoValores = this.service.buscarNoRep(0, -1, null,
                    null);
            Integer contador = this.service.countDistinctDsValor();

            Assert.assertEquals(lstCatalogoValores.size(), contador.intValue());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscaPorFiltro() {
        try {
            List<CatalogosValoresDTO> listaCV = null;
            List<AtlasFilter> filtros = null;

            // busqueda por filtro codigo valor
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, "16",
                    AtlasFilter.Operador.EQUALS));
            filtros.add(new AtlasFilter("cdCodigo", "codPos-01", AtlasFilter.Operador.EQUALS));
            listaCV = this.service.obtenerPorFiltros(filtros);
            Assert.assertFalse(listaCV.isEmpty());

            // busqueda por filtro descripción de valor
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, "16",
                    AtlasFilter.Operador.EQUALS));
            filtros.add(new AtlasFilter("dsValor", "Código postal", AtlasFilter.Operador.EQUALS));
            listaCV = this.service.obtenerPorFiltros(filtros);
            Assert.assertFalse(listaCV.isEmpty());

            // busqueda por filtro codigo y descripción valor
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, "16",
                    AtlasFilter.Operador.EQUALS));
            filtros.add(new AtlasFilter("cdCodigo", "codPos-01", AtlasFilter.Operador.EQUALS));
            filtros.add(new AtlasFilter("dsValor", "Código postal", AtlasFilter.Operador.EQUALS));
            listaCV = this.service.obtenerPorFiltros(filtros);
            Assert.assertFalse(listaCV.isEmpty());

            // busqueda sin filtros de valores
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, "16",
                    AtlasFilter.Operador.EQUALS));
            listaCV = this.service.obtenerPorFiltros(filtros);
            Assert.assertFalse(listaCV.isEmpty());

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

    private void testAtributos(CatalogosValoresDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdValor());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getClass());
        Assert.assertNotNull(dto.getCatalogoDTO());
        Assert.assertNotNull(dto.getCdCodigo());
        Assert.assertNotNull(dto.getCatalogoDTO());
        Assert.assertNotNull(StringUtils.isNotBlank(dto.getCdCodigo()));
        Assert.assertNotNull(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(StringUtils.isNotBlank(dto.getCdUsuBaja()));
        Assert.assertNotNull(StringUtils.isNotBlank(dto.getDsValor()));
    }

    /**
     * Test de metodo obtenerCatalogoConvocatoria - proceso correcto
     *
     * @throws ServiceException
     */
    @Test
    public void testObtenerCatalogoConvocatoria() throws ServiceException {
        Integer idCatalogo = 37;
        Integer idConvocatoria = 48;

        List<CatalogosValoresDTO> dto = this.service.obtenerCatalogoConvocatoria(
                idConvocatoria);
        Assert.assertNotNull(dto);
    }

    /**
     * Test de metodo obtenerCatalogoConvocatoria - proceso incorrecto 1 -
     * idCatalogo nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testObtenerCatalogoConvocatoriaSinIdCatalogo() throws ServiceException {
        Integer idCatalogo = null;
        Integer idConvocatoria = 48;
        this.service.obtenerCatalogoConvocatoria(idConvocatoria);
    }

    /**
     * Test de metodo obtenerCatalogoConvocatoria - proceso incorrecto 2 -
     * idConvocatoria nulo
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testObtenerCatalogoConvocatoriaSinIdConvocatoria() throws ServiceException {
        Integer idCatalogo = 37;
        Integer idConvocatoria = null;
        this.service.obtenerCatalogoConvocatoria(idConvocatoria);
    }

}
