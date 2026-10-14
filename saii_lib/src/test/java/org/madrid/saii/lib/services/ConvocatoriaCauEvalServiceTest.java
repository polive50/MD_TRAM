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
import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
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
 * Test ConvocatoriaCauEvalService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaCauEvalServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
//    @Autowired
//    @Qualifier("convocatoriaCauEvalService")
//    private ConvocatoriaCauEvalService service;

    /**
     * Session factory de hibernate
     */
//    @Resource
//    private SessionFactory sessionFactory;

    /**
     * Asocia la sessionFactory de Hibernate a la variable interna
     *
     * @param sessionFactory la sessionFactory
     */
//    public void setSessionFactory(final SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }

    /**
     * Inicialización antes de empezar los test
     */
    @Before
    @Override
    public void setUp() {
//        assertNotNull(this.service);
    }

    /**
     * Prueba el metodo guardar
     */
//    @Test
//    public void testAgregarNuevoConIdValor() {
//        try {
//            ConvocatoriaCauEvalDTO dto = new ConvocatoriaCauEvalDTO();
//            ConvocatoriaDTO dtoConvo = new ConvocatoriaDTO();
//            CatalogosValoresDTO dtoCata = new CatalogosValoresDTO();
//
//            dtoConvo.setIdConvocatoria(48);
//            dtoCata.setIdValor(2001);
//            dto.setCausaEvalDTO(dtoCata);
//            dto.setIdConvocatoria(dtoConvo.getIdConvocatoria());
//            dto.setFcAltaFila(new Date());
//
//            // GUARDAR
//            dto = this.service.agregar(dto.getIdConvocatoria(), dto.getCausaEvalDTO().getIdValor());
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//            // Está relleno
//            this.testAtributos(dto, Boolean.FALSE);
//
//            Assert.assertEquals(Constantes.Test.TEST_ADMIN, dto.getCdUsuAlta());
//
//            ConvocatoriaCauEval ent = this.sessionFactory.getCurrentSession()
//                    .get(ConvocatoriaCauEval.class, dto.getIdConvocatoriaCauEval());
//            this.sessionFactory.getCurrentSession().evict(ent);
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

    /**
     * Prueba el metodo guardar
     */
//    @Test
//    public void testAgregarNuevoSinIdValor() {
//        try {
//            ConvocatoriaCauEvalDTO dto = new ConvocatoriaCauEvalDTO();
//            ConvocatoriaDTO dtoConvo = new ConvocatoriaDTO();
//            CatalogosValoresDTO dtoCata = new CatalogosValoresDTO();
//
//            dtoConvo.setIdConvocatoria(49);
//            dtoCata.setIdValor(2000);
//            dto.setCausaEvalDTO(dtoCata);
//            dto.setIdConvocatoria(dtoConvo.getIdConvocatoria());
//            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
//
//            // GUARDAR
//            ConvocatoriaCauEvalDTO dtoprueba = this.service.agregar(dto.getIdConvocatoria(),
//                    dto.getCausaEvalDTO().getIdValor());
//
//            // No es nulo
//            Assert.assertNotNull(dtoprueba);
//
//            Assert.assertNotNull(dtoprueba.getIdConvocatoriaCauEval());
//            Assert.assertEquals(dtoCata.getIdValor(), dtoprueba.getCausaEvalDTO().getIdValor());
//            Assert.assertEquals(dtoConvo.getIdConvocatoria(), dtoprueba.getIdConvocatoria());
//
//            ConvocatoriaCauEval ent = this.sessionFactory.getCurrentSession()
//                    .get(ConvocatoriaCauEval.class, dtoprueba.getIdConvocatoriaCauEval());
//            this.sessionFactory.getCurrentSession().evict(ent);
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

    /**
     * método para capturar la excepción de error en el método aprobarEvaluacion
     *
     * @throws ServiceException
     */
//    @Test(expected = ServiceException.class)
//    public void tesErrorAgregadoConParametrosNulos() throws ServiceException {
//        this.service.agregar(null, null);
//    }

    /**
     * método para capturar la excepción de error en el método aprobarEvaluacion
     *
     * @throws ServiceException
     */
//    @Test(expected = ServiceException.class)
//    public void tesErrorAgregadoConFBajaNula() throws ServiceException {
//        ConvocatoriaCauEvalDTO dto = new ConvocatoriaCauEvalDTO();
//        ConvocatoriaDTO dtoConvo = new ConvocatoriaDTO();
//        CatalogosValoresDTO dtoCata = new CatalogosValoresDTO();
//
//        dtoConvo.setIdConvocatoria(48);
//        dtoCata.setIdValor(2002);
//        dto.setIdConvocatoriaCauEval(2);
//        dto.setCausaEvalDTO(dtoCata);
//        dto.setIdConvocatoria(dtoConvo.getIdConvocatoria());
//        dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
//        dto.setFcAltaFila(new Date());
//
//        this.service.agregar(dto.getIdConvocatoria(), dto.getCausaEvalDTO().getIdValor());
//    }

//    @Test
//    public void testBuscarPorId() {
//        try {
//            ConvocatoriaCauEvalDTO dto = this.service.buscarId(1);
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//            // Está relleno el id
//            Assert.assertNotNull(dto.getIdConvocatoriaCauEval());
//
//            this.testAtributos(dto, Boolean.FALSE);
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

//    @Test
//    public void testBuscar() {
//        try {
//            List<ConvocatoriaCauEvalDTO> lstDTO = this.service.buscar(0, -1, null, null);
//
//            // No es nulo
//            Assert.assertNotNull(lstDTO);
//
//            // Está relleno
//            Assert.assertFalse(lstDTO.isEmpty());
//
//            // Comprobamos los atributos obligatorios
//            // El DTO tiene que ser null safe.
//            Iterator<ConvocatoriaCauEvalDTO> it = lstDTO.iterator();
//            while (it.hasNext()) {
//                ConvocatoriaCauEvalDTO dto = it.next();
//
//                Assert.assertNotNull(dto);
//
//                this.testAtributos(dto, Boolean.FALSE);
//            }
//
//            // Búsqueda vacia
//            List<AtlasFilter> filtros = new ArrayList<>();
//            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));
//
//            lstDTO = this.service.buscar(0, -1, filtros, null);
//
//            // No es nulo
//            Assert.assertNotNull(lstDTO);
//
//            // Está vacio
//            Assert.assertTrue(lstDTO.isEmpty());
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
//    private void testAtributos(ConvocatoriaCauEvalDTO dto, Boolean bRelleno) {
//
//        log.debug(dto.toString());
//
//        // Obligatorios
//        Assert.assertNotNull(dto.getIdConvocatoriaCauEval());
//
//        Assert.assertNotNull(dto.getIdConvocatoria());
//
//        Assert.assertNotNull(dto.getCausaEvalDTO());
//
//        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
//
//        Assert.assertNotNull(dto.getFcAltaFila());
//
//        if (Boolean.TRUE.equals(bRelleno)) {
//            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
//
//            Assert.assertNotNull(dto.getFcModifFila());
//
//            Assert.assertNotNull(dto.getCdUsuModif());
//        }
//
//    }

}
