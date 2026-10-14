package org.madrid.saii.lib.services.adme;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.dto.ExpedienteDTO;
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
 * Test SiexAdmeService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnReal")
@Slf4j
public class SiexAdmeServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("siexAdmeService")
    private SiexAdmeService service;

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
     * Test grabarExpteAPLI
     */
    @Test
    @Ignore
    public void testGrabarExpteAPLI() {
        try {

            ExpedienteDTO dto = new ExpedienteDTO();

            dto.setIdExpediente(1);
            dto.setCdExpediente(" ");
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            String cdExpedienteSiex = this.service.grabarExpteAPLI(dto);

            Assert.assertTrue(StringUtils.isNoneBlank(cdExpedienteSiex));

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }


    /**
     * Test grabarInteAPLI
     */
    @Test
    @Ignore
    public void testGrabarInteAPLI() {
        try {

            ExpedienteDTO dto = new ExpedienteDTO();

            dto.setIdExpediente(1);
            dto.setCdExpediente("09-READ-00001/2002");
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            dto.getInteresadoDTO().setCdNumIden("28631189F");
            dto.getInteresadoDTO().setDsApellido1("APELLIDO 1 PRUEBA");
            dto.getInteresadoDTO().setDsApellido1("APELLIDO 2 PRUEBA");
            dto.getInteresadoDTO().setDsApellido1("prueba@prueba.com");
            dto.getInteresadoDTO().setDsApellido1("NOMBRE PRUEBA");

            this.service.grabarInteAPLI(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test grabarInteAPLI
     */
    @Test
    @Ignore
    public void testGrabarHitoAPLI() {
        // TODO
    }


    /**
     * Test testObtenerDatosExpteAPLI
     */
    @Test
    @Ignore
    public void testObtenerDatosExpteAPLI() {
        try {

            ExpedienteDTO dto = new ExpedienteDTO();

            dto.setIdExpediente(1);
            dto.setCdExpediente("09-READ-00001/2002");

            this.service.obtenerDatosExpteAPLI(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test testObtenerDatosExpteAPLI
     */
    @Test
    @Ignore
    public void testObtenerDatosInteAPLI() {
        try {

            ExpedienteDTO dto = new ExpedienteDTO();
            dto.setIdExpediente(1);
            dto.setCdExpediente("09-READ-00001/2002");
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            dto.getInteresadoDTO().setCdNumIden("28631189F");

            this.service.obtenerdatosInteAPLI(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }
}
