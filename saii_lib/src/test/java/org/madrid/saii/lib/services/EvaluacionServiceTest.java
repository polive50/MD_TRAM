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
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.dto.EvaluacionDTO;
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
 * Test EvaluacionService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class EvaluacionServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("evaluacionService")
    private EvaluacionService service;

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
//    @Test
//    public void testGuardar() {
//        try {
//
//            // GUARDAR
//            EvaluacionDTO dto = new EvaluacionDTO();
//            dto.setIdEvaluacion(302);
//            dto.setIdConvocatoria(26);
//            dto.setItCalificacion("Bien");
//
//            dto = this.service.guardar(dto);
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//            // La modificación es correcta
//            // Assert.assertEquals("Prueba Test Mod", dto.getDsConvocatoria());
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

    /**
     * Prueba el método ObtenerEvaluacionDTOPorConvocatoria.
     */
    @Test
    public void testObtenerEvaluacionDTOPorConvocatoria() {
        try {
            List<EvaluacionDTO> lstDTO = this.service.obtenerPorConvocatoria(26, "C");

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
     * Prueba el método BuscarPorId.
     */

    @Test

    public void testBuscarPorId() {
        try {
            EvaluacionDTO dto = this.service.buscarId(302);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdEvaluacion());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método ObtenerEvaluacionDTOPorConvocatoria.
     */

    @Test
    public void testBuscar() {
        try {
            List<EvaluacionDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<EvaluacionDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                EvaluacionDTO dto = it.next();

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
     * Comprueba el borrado logico
     */
    @Test
    public void testBorradoLogico() {

        try {
            EvaluacionDTO dto = this.service.buscarId(302);

            // Comprobamos que no es nulo
            Assert.assertNotNull(dto);

            // Limpiamos la instancia
            Evaluacion ent = this.sessionFactory.getCurrentSession().get(Evaluacion.class,
                    dto.getIdEvaluacion());
            this.sessionFactory.getCurrentSession().evict(ent);

            // hacemos un borrado logico
            this.service.borradoLogico(dto);

            // Comprobamos que se ha realizado
            dto = this.service.buscarId(dto.getIdEvaluacion());

            Assert.assertNotNull(dto.getFcBajaFila());

        } catch (ServiceException e) {
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
    private void testAtributos(EvaluacionDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdEvaluacion());

        Assert.assertNotNull(dto.getIdConvocatoria());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsTitulo()));

        Assert.assertTrue(StringUtils.isNotBlank(dto.getItCalificacion()));

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertNotNull(dto.getCdUsuModif());

        }

    }

}
