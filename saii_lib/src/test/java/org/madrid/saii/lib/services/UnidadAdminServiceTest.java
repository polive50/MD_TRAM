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
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test UnidadAdminService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnReal")
@Slf4j
public class UnidadAdminServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService service;

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
     * Prueba el método obtener
     */
    @Test
    @Ignore
    public void testObtenerPorFiltros() {
        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD, "010501", Operador.EQUALS));
            Assert.assertNotNull(filtros);
            Assert.assertFalse(filtros.isEmpty());
            List<UnidadAdminDTO> lstDTO = this.service.obtenerPorFiltros(filtros);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<UnidadAdminDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                UnidadAdminDTO dto = it.next();
                Assert.assertNotNull(dto);
                Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuBaja()));
                Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuModif()));
                Assert.assertNull(dto.getFcBajaFila());
                Assert.assertNull(dto.getFcModifFila());
                this.testAtributos(dto, Boolean.FALSE);
            }
            // Búsqueda vacia
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD, "D", Operador.EQUALS));

            Assert.assertNotNull(filtros);
            Assert.assertFalse(filtros.isEmpty());

            lstDTO = this.service.obtenerPorFiltros(filtros);

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
    @Ignore
    public void testBuscarPorId() {
        try {
            UnidadAdminDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdUnidadAdmin());
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getFcModifFila());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testBuscar() {
        try {
            List<UnidadAdminDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<UnidadAdminDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                UnidadAdminDTO dto = it.next();
                Assert.assertNotNull(dto);
                Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuBaja()));
                Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuModif()));
                Assert.assertNull(dto.getFcBajaFila());
                Assert.assertNull(dto.getFcModifFila());

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            Assert.assertNotNull(filtros);
            Assert.assertFalse(filtros.isEmpty());

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
    @Ignore
    public void testBuscarPorCdUnidad() {
        try {
            UnidadAdminDTO dto = this.service.buscarCdUnidad("010501");

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdUnidadAdmin());
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getFcModifFila());

            this.testAtributos(dto, Boolean.TRUE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testBuscarPorCdUnidadError() {
        try {
            UnidadAdminDTO dto = this.service.buscarCdUnidad("99999");

            // No es nulo
            Assert.assertNotNull(dto);

            // No Está relleno el id
            Assert.assertNull(dto.getIdUnidadAdmin());
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuAlta()));
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertFalse(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNull(dto.getFcBajaFila());
            Assert.assertNull(dto.getFcModifFila());

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
    private void testAtributos(UnidadAdminDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdUnidadAdmin());
        Assert.assertNotNull(dto.getCdUnidad());
        Assert.assertNotNull(dto.getDsUnidad());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getNivelOrgDTO().getDsNivelOrg());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getCdUnidadPadre());
            Assert.assertNotNull(dto.getDsUnidadPadre());
            Assert.assertNotNull(dto.getNivelOrgDTO().getIdNivelOrg());
        }

        // Null Safe
        Assert.assertNotNull(dto.getDsUnidadPadre());
        Assert.assertNotNull(dto.getNivelOrgDTO());
    }

}
