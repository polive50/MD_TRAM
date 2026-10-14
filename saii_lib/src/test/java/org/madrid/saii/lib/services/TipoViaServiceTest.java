package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.CatalogoDTO;
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
 * Clase para pruebas unitarias del servicio TipoViaService
 *
 * @author everis
 *
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class TipoViaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("tipoViaService")
    private TipoViaService service;

    /**
     * Inicialización antes de empezar los test
     */
    @Before
    @Override
    public void setUp() {
        assertNotNull(this.service);
    }

    /**
     * Método para realizar prueba del método "obtener"
     */
    @Test
    public void testObtener() {
        try {
            List<CatalogoDTO> lstTipoVia = this.service.obtener();
            Assert.assertNotNull(lstTipoVia);
            Assert.assertFalse(lstTipoVia.isEmpty());

            if (!lstTipoVia.isEmpty()) {
                lstTipoVia.forEach(e -> {
                    this.testAtributos(e);
                    Assert.assertNotNull(e);
                    Assert.assertTrue(StringUtils.isNotBlank(e.getDescripcion()));
                    Assert.assertTrue(StringUtils.isNotBlank(e.getCodigo()));
                });
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Método para validar los datos de tipo de vía
     *
     * @param dto Datos de tipo de vía
     */
    private void testAtributos(CatalogoDTO dto) {

        log.debug(dto.toString());

        Assert.assertNotNull(dto);
        Assert.assertTrue(StringUtils.isNotBlank(dto.getDescripcion()));
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCodigo()));

    }

}
