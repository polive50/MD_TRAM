package org.madrid.saii.lib.util;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.test.base.AtlasAbstractBaseTest;

/**
 * Test Utils
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
public class UtilsTest extends AtlasAbstractBaseTest {

    /**
     * Inicialización antes de empezar los test
     */
    @Before
    @Override
    public void setUp() {
        // No se instancia ningun servicio
    }

    /**
     * Prueba el método CalcularTipoIdentificacion
     */
    @Test
    public void testIsNull() {

        // null
        Assert.assertTrue(Utils.isNull(null));
        
        // no null
        Assert.assertFalse(Utils.isNull(new Date()));

    }

}
