package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
 * Test UsuarioJurService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class StatusServiceTest extends AtlasAbstractBaseTest {

    /** Servicio */
    @Autowired
    @Qualifier("statusService")
    private StatusService service;

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
     * Test obtieneData
     */
    @Test
    public void testObtieneData() {
        Integer rows = 1;
        try {
            rows = this.service.obtieneData();
            // No es nulo
            Assert.assertNotNull(rows);

            // Está relleno
            Assert.assertEquals(Integer.valueOf(1), rows);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
