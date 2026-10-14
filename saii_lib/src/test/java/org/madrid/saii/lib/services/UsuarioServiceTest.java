package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dao.UsuarioDAO;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test GcatusuarsService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnReal")
@Slf4j
public class UsuarioServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("usuarioService")
    private UsuarioService service;

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
     * Metodo para cantidad total de usuario de saii - correcto
     */
    @Test
    public void testCantidadTotalRolUsuario() {
        try {
            Boolean dto = this.service.consultarTieneRolesUsuarioSaii("SAIIUSU1");

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertTrue(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Metodo para cantidad total de usuario de saii - error
     */
    @SuppressWarnings("unchecked")
    @Test(expected = Exception.class)
    public void testCantidadTotalRolUsuarioError() throws ServiceException {

        UsuarioService serviceMock = new UsuarioServiceImpl();

        UsuarioDAO dao = mock(UsuarioDAO.class);
        when(dao.consultarTieneRolesUsuarioSaii("ERROR")).thenThrow(DataAccessException.class);
        serviceMock.setDao(dao);

        serviceMock.consultarTieneRolesUsuarioSaii("ERROR");

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     */
    private void testAtributos(UsuarioDTO dto) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getCdUnidad());
        Assert.assertNotNull(dto.getCdUsuario());

    }

}
