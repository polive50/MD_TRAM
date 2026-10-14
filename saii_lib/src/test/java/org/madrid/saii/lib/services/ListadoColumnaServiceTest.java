package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoColumnaDTO;
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
 * Test ListadoColumnaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ListadoColumnaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("listadoColumnaService")
    private ListadoColumnaService service;

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
            ListadoColumnaDTO dto = new ListadoColumnaDTO();
            dto.setIdListadoColumna(1);
            dto.setIdListado(2);
            dto.setIdBloqueComponente(3);
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
            this.testAtributos(dto, Boolean.TRUE);

            ListadoColumna ent = this.sessionFactory.getCurrentSession().get(ListadoColumna.class,
                    dto.getIdListadoColumna());
            this.sessionFactory.getCurrentSession().evict(ent);
            dto = this.service.guardar(dto);

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
    private void testAtributos(ListadoColumnaDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdListadoColumna());

        Assert.assertNotNull(dto.getIdListado());

        Assert.assertNotNull(dto.getIdBloqueComponente());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));

            Assert.assertNotNull(dto.getFcBajaFila());
        }
        Assert.assertNotNull(dto.getCdUsuModif());

        Assert.assertNotNull(dto.getCdUsuBaja());

    }

}
