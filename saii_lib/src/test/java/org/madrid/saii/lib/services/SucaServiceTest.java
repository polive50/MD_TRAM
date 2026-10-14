package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
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
 * Test SucaProvinciaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class SucaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("sucaProvinciaService")
    private SucaProvinciaService sucaProvinciaService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("tipoViaService")
    private TipoViaService tipoViaService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("sucaMunicipioService")
    private SucaMunicipioService sucaMunicipioService;

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
        assertNotNull(this.sucaProvinciaService);
        assertNotNull(this.tipoViaService);

    }

    /**
     * Prueba el metodo obtener
     */
    @Test
    public void testObtenerTipoVia() {
        try {

            List<CatalogoDTO> lstDTO = this.tipoViaService.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<CatalogoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogoDTO dto = it.next();

                this.testAtributos(dto);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo obtener
     */
    @Test
    public void testObtenerProvincia() {
        try {

            List<CatalogoDTO> lstDTO = this.sucaProvinciaService.obtener();

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<CatalogoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                CatalogoDTO dto = it.next();

                this.testAtributos(dto);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo obtener
     */
    @Test
    public void testBuscarPorProvincia() {
        try {

            List<SucaMunicipioDTO> lstDTO = this.sucaMunicipioService
                    .buscarPorProvincia(Constantes.Suca.PROV_MADRID);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<SucaMunicipioDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                SucaMunicipioDTO dto = it.next();

                this.testAtributos(dto);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     */
    private void testAtributos(CatalogoDTO dto) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getCodigo());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDescripcion()));
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     */
    private void testAtributos(SucaMunicipioDTO dto) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdMunicipio()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdProvincia()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsMunicipio()));
    }

}
