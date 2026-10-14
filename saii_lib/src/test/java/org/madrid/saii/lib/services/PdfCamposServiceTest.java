package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test PdfServiceTest
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class PdfCamposServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("pdfCamposService")
    private PdfCamposService service;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

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
    public void testRellenarCampos() {
        Map<String, String> map = new HashMap<>();
        String cdEreg = "12345";
        Boolean existe = false;

        try {

            ExpedienteDTO expediente = this.expedienteService.buscarId(4);
            expediente.setCdEreg(cdEreg);

            map = this.service.rellenarCampos(expediente);

            assertNotNull(map);

            assertTrue(map.size() > 0);

            for (String e : map.values()) {
                if (null != e && e.equals(cdEreg)) {
                    existe = true;
                }
            }

            assertTrue(existe);

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
}
