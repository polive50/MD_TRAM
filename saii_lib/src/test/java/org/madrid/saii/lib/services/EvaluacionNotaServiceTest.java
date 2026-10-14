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
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
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
 * Test EvaluacionNotaService.
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")

/** The Constant log. */
@Slf4j
public class EvaluacionNotaServiceTest extends AtlasAbstractBaseTest {

    /** Servicio. */
    @Autowired
    @Qualifier("evaluacionNotaService")
    private EvaluacionNotaService service;

    /** Session factory de hibernate. */
    @Resource
    private SessionFactory sessionFactory;

    /**
     * Asocia la sessionFactory de Hibernate a la variable interna.
     *
     * @param sessionFactory la sessionFactory
     */
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Inicialización antes de empezar los test.
     */
    @Before
    @Override
    public void setUp() {
        assertNotNull(this.service);
    }

    /**
     * Prueba el metodo guardar.
     */
    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            EvaluacionNotaDTO dto = new EvaluacionNotaDTO();
            dto.setIdEvaluacionNota(303);
            dto.setIdConvocatoria(26);
            dto.setIdEvaluacion(302);
            dto.setNmA(15);
            dto.setNmDe(16);
            CatalogosValoresDTO notaDTO = new CatalogosValoresDTO();
            notaDTO.setIdValor(10);
            dto.setNotaDTO(notaDTO);
            dto.setFcModifFila(new Date());
            dto.setCdUsuModif("USER");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            EvaluacionNota ent = this.sessionFactory.getCurrentSession().get(EvaluacionNota.class,
                    dto.getIdEvaluacionNota());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdUsuModif("SAIIUSU1");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("SAIIUSU1", dto.getCdUsuModif());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método ObtenerEvaluacionNotaDTOPorEvaluacion.
     */
    @Test
    public void testObtenerEvaluacionNotaDTOPorEvaluacion() {
        try {
            List<EvaluacionNotaDTO> lstDTO = this.service.obtenerPorEvaluacion(300);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test obtener evaluacion nota DTO por evaluacion error.
     *
     * @throws ServiceException the service exception
     */
    @Test(expected = ServiceException.class)
    public void testObtenerEvaluacionNotaDTOPorEvaluacionError() throws ServiceException {
        this.service.obtenerPorEvaluacion(null);
    }

    @Test
    public void testObtenerListaRangoNotasPorDefecto() {
        //CdCodigo: CUE
        List<CatalogosValoresDTO> lstRangos = new ArrayList<CatalogosValoresDTO>();
        CatalogosValoresDTO catalogoValoresDTO = new CatalogosValoresDTO(2);
        catalogoValoresDTO.setCdCodigo("CUE");
        lstRangos.add(catalogoValoresDTO);
        try {
            List<EvaluacionNotaDTO> lstDTO = this.service
                    .obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            //CdCodigo: ACE
            lstRangos = new ArrayList<CatalogosValoresDTO>();
            catalogoValoresDTO = new CatalogosValoresDTO(2);
            catalogoValoresDTO.setCdCodigo("ACE");
            lstRangos.add(catalogoValoresDTO);

            lstDTO = this.service
                    .obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            //CdCodigo: BUE
            lstRangos = new ArrayList<CatalogosValoresDTO>();
            catalogoValoresDTO = new CatalogosValoresDTO(2);
            catalogoValoresDTO.setCdCodigo("BUE");
            lstRangos.add(catalogoValoresDTO);

            lstDTO = this.service
                    .obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            //CdCodigo: EXC
            lstRangos = new ArrayList<CatalogosValoresDTO>();
            catalogoValoresDTO = new CatalogosValoresDTO(2);
            catalogoValoresDTO.setCdCodigo("EXC");
            lstRangos.add(catalogoValoresDTO);

            lstDTO = this.service
                    .obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            //CdCodigo : Diferente
            lstRangos = new ArrayList<CatalogosValoresDTO>();
            catalogoValoresDTO.setCdCodigo("EUR");
            lstRangos.add(catalogoValoresDTO);
            lstDTO = this.service.obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está rellenos
            Assert.assertFalse(lstDTO.isEmpty());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }


    @Test
    public void testObtenerListaRangoNotasPorDefectoDEF() {
        List<CatalogosValoresDTO> lstRangos = new ArrayList<CatalogosValoresDTO>();
        CatalogosValoresDTO catalogoValoresDTO = new CatalogosValoresDTO(2);
        catalogoValoresDTO.setCdCodigo("DEF");
        lstRangos.add(catalogoValoresDTO);
        try {
            List<EvaluacionNotaDTO> lstDTO = this.service
                    .obtenerListaRangoNotasPorDefecto(lstRangos, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Busqueda vacía
            lstRangos = new ArrayList<CatalogosValoresDTO>();
            lstDTO = this.service.obtenerListaRangoNotasPorDefecto(lstRangos, 1);

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
     * Prueba el método BuscarPorId.
     */

    @Test

    public void testBuscarPorId() {
        try {
            EvaluacionNotaDTO dto = this.service.buscarId(303);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdEvaluacionNota());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método Buscar.
     */

    @Test
    public void testBuscar() {
        try {
            List<EvaluacionNotaDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<EvaluacionNotaDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                EvaluacionNotaDTO dto = it.next();

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
     * Comprueba los atributos.
     *
     * @param dto the dto
     * @param bRelleno the b relleno
     * @parama relleno
     */
    private void testAtributos(EvaluacionNotaDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdEvaluacionNota());

        Assert.assertNotNull(dto.getIdConvocatoria());

        Assert.assertNotNull(dto.getNotaDTO());

        Assert.assertNotNull(dto.getNmDe());

        Assert.assertNotNull(dto.getNmA());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertNotNull(dto.getCdUsuModif());

        }

    }

}
