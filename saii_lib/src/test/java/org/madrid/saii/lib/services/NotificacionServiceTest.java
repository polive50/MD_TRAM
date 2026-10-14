package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.NotificacionDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;

/**
 * Test NotificacionService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
public class NotificacionServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("notificacionService")
    private NotificacionService service;

    /**
     * Inicialización antes de empezar los test
     */
    @Before
    @Override
    public void setUp() {
        assertNotNull(this.service);
    }

    /**
     * Obtener la lista de Notificacion para coordinador
     */
    @Test
    public void obtenerNotificacionCoorEvalPorCoordinadorTest() throws ServiceException {

        String itCoorEval = Constantes.TipoCoorEval.COORDINADOR;
        String identificador = "54826624G";

        List<NotificacionDTO> lstDTO = this.service.obtenerNotificacionCoorEval(itCoorEval,
                identificador);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());
    }

    /**
     * Obtener la lista de Notificacion para evaluador
     */
    @Test
    public void obtenerNotificacionCoorEvalPorEvaluadorTest() throws ServiceException {

        String itCoorEval = Constantes.TipoCoorEval.EVALUADOR;
        String identificador = "55826624G";

        List<NotificacionDTO> lstDTO = this.service.obtenerNotificacionCoorEval(itCoorEval,
                identificador);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());
    }

    /**
     *
     * Error de la lista de Notificacion con identificador
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerNotificacionCoorEvalPorIdentificadorTest() throws ServiceException {

        String itCoorEval = null;
        String identificador = "54826624G";

        this.service.obtenerNotificacionCoorEval(itCoorEval, identificador);
    }

    /**
     * Error de la lista de Notificacion con itCoorEval
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerNotificacionCoorEvalPorItCoorEvalTest() throws ServiceException {

        String itCoorEval = Constantes.TipoCoorEval.COORDINADOR;
        String identificador = null;
        this.service.obtenerNotificacionCoorEval(itCoorEval, identificador);
    }

    /**
     * Método de exito para obtener total de notificaciones
     *
     * @throws ServiceException
     */
    @Test
    public void obtenerTotalNotificacionesTest() throws ServiceException {
        String identificador = "54826624G";

        Integer total = this.service.obtenerTotalNotificaciones(identificador);

        Assert.assertNotNull(total);

        Assert.assertTrue(total > 0);
    }

    /**
     * Método de error para obtener total de notificaciones
     *
     */
    @Test(expected = ServiceException.class)
    public void errorObtenerTotalNotificacionesTest() throws ServiceException {

        String identificador = null;

        this.service.obtenerTotalNotificaciones(identificador);
    }

}
