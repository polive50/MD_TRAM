package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.log4j.Log4j2;

@ContextConfiguration(classes = { ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Log4j2
public class IdentificacionServiceTest extends AtlasAbstractBaseTest {
    
    /**
     * Servicio
     */
    @Autowired
    @Qualifier("identificacionService")
    private IdentificacionService service;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
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
    public void testCalcularTipoIdentificacion() {
        
        try {
            
         // Error null
            Assert.assertEquals(Integer.valueOf(0),
                    this.service.calcularTipoIdentificacion(null));

            // Error Vacio
            Assert.assertEquals(Integer.valueOf(0),
                    this.service.calcularTipoIdentificacion(StringUtils.EMPTY));

            // Error Longitud no valida
            Assert.assertEquals(Integer.valueOf(0),
                    this.service.calcularTipoIdentificacion("123"));

            // NIF
            Assert.assertEquals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIF),
                    this.service.calcularTipoIdentificacion("28631189F"));

            // NIE
            Assert.assertEquals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.NIE),
                    this.service.calcularTipoIdentificacion("X1073205W"));

            // Error Letra no valida
            Assert.assertEquals(Integer.valueOf(0),
                    this.service.calcularTipoIdentificacion("28631189A"));
            
            
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
        
    }
    
}
