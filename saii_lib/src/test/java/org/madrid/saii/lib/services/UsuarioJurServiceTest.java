package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.UsuarioJurDTO;
import org.madrid.saii.lib.dto.UsuarioJurFiltrosDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test UsuarioJurService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class UsuarioJurServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("usuarioJurService")
    private UsuarioJurService service;

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
    public void testBuscarPorId() {
        try {
            UsuarioJurDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdUsuarioJur());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<UsuarioJurDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<UsuarioJurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                UsuarioJurDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscar(0, -1, filtros, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está vacio
            Assert.assertTrue(lstDTO.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar
     *
     * @throws ServiceException
     */
    @Test
    public void testGuardar() {
        try {
            UsuarioJurDTO dto = new UsuarioJurDTO();

            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setIdInteresado(1);
            InteresadoDTO entidadJur = new InteresadoDTO();
            entidadJur.setIdInteresado(4);

            dto.setInteresadoDTO(interesadoDTO);
            dto.setEntidadJurDTO(entidadJur);
            dto.setCdUsuAlta("ADMIN-TEST1");
            dto.setFcAltaFila(Date.from(Instant.now()));

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            UsuarioJur ent = this.sessionFactory.getCurrentSession().get(UsuarioJur.class,
                    dto.getIdUsuarioJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Modificamos
            dto.setCdUsuAlta("ADMIN-MOD2");

            dto = this.service.guardar(dto);

            Assert.assertEquals("ADMIN-MOD2", dto.getCdUsuAlta());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + ex.getMessage());
        }
    }

    /**
     * Test borrado logico
     */
    @Test
    public void testBorradoLogico() {
        try {

            UsuarioJurDTO dto = new UsuarioJurDTO();

            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setIdInteresado(1);
            InteresadoDTO entidadJur = new InteresadoDTO();
            entidadJur.setIdInteresado(4);

            dto.setInteresadoDTO(interesadoDTO);
            dto.setEntidadJurDTO(entidadJur);
            dto.setCdUsuAlta("ADMIN-TEST2");
            dto.setFcAltaFila(Date.from(Instant.now()));

            dto = this.service.guardar(dto);

            Assert.assertNotNull(dto);

            // liberar instancia
            UsuarioJur ent = this.sessionFactory.getCurrentSession().get(UsuarioJur.class,
                    dto.getIdUsuarioJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.borradoLogico(dto);

            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaBaja = dto.getFcBajaFila().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Assert.assertEquals(fechaBaja, fechaHoy);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + ex.getMessage());
        }
    }

    /**
     * Test alta logica
     */
    @Test
    public void testAltaLogica() {

        try {

            UsuarioJurDTO dto = new UsuarioJurDTO();

            InteresadoDTO interesadoDTO = new InteresadoDTO();
            interesadoDTO.setIdInteresado(1);
            InteresadoDTO entidadJur = new InteresadoDTO();
            entidadJur.setIdInteresado(4);

            dto.setInteresadoDTO(interesadoDTO);
            dto.setEntidadJurDTO(entidadJur);
            dto.setCdUsuAlta("ADMIN-TEST3");
            dto.setFcAltaFila(Date.from(Instant.now()));

            dto = this.service.guardar(dto);

            Assert.assertNotNull(dto);

            // liberar instancia
            UsuarioJur ent = this.sessionFactory.getCurrentSession().get(UsuarioJur.class,
                    dto.getIdUsuarioJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.borradoLogico(dto);

            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaBaja = dto.getFcBajaFila().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Assert.assertEquals(fechaBaja, fechaHoy);

            // liberar instancia
            ent = this.sessionFactory.getCurrentSession().get(UsuarioJur.class,
                    dto.getIdUsuarioJur());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.altaLogica(dto);

            Assert.assertNull(dto.getFcBajaFila());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        } catch (final Exception ex) {
            log.error(ex.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + ex.getMessage());
        }

    }

    /**
     * Prueba el método filtrar
     */
    @Test
    public void testFiltrar() {
        try {

            UsuarioJurFiltrosDTO usuarioJurFiltroDTO = new UsuarioJurFiltrosDTO();
            usuarioJurFiltroDTO.setCif("R4353819H");
            List<UsuarioJurDTO> lstDTO = this.service.filtrar(usuarioJurFiltroDTO);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<UsuarioJurDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                UsuarioJurDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método filtrar esperando una excepción controlada de tipo
     * nullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void testFiltrarKO() {
        try {

            this.service.filtrar(null);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }
// TODO
//    /**
//     * Prueba el método registrarPersonaJur Nuevo
//     */
//    @Test
//    public void testRegistrarPersonaJur() {
//        try {
//
//            // Interesado y entidad nuevos
//            UsuarioJurDTO dto = new UsuarioJurDTO();
//
//            InteresadoDTO interesadoDTO = new InteresadoDTO();
//            interesadoDTO.setCdNumIden("00000000A");
//            interesadoDTO.setDsNombre("Interesado A");
//            interesadoDTO.setIdTipoActor(14);
//            InteresadoDTO entidadJur = new InteresadoDTO();
//            entidadJur.setCdNumIden("B00000000");
//            entidadJur.setDsNombre("Entidad B");
//            entidadJur.setIdTipoActor(13);
//
//            dto.setInteresadoDTO(interesadoDTO);
//            dto.setEntidadJurDTO(entidadJur);
//            dto.setCdUsuAlta("ADMIN-TEST1");
//            dto.setFcAltaFila(Date.from(Instant.now()));
//
//            dto = service.registrarPersonaJur(dto);
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

//    /**
//     * Prueba el método registrarPersonaJur existente
//     */
//    @Test
//    public void testRegistrarPersonaJurExistente() {
//        try {
//
//            // Interesado y entidad nuevos
//            UsuarioJurDTO dto = new UsuarioJurDTO();
//
//            InteresadoDTO interesadoDTO = new InteresadoDTO();
//            interesadoDTO.setCdNumIden("00000001R");
//            interesadoDTO.setDsNombre("Pablo");
//            interesadoDTO.setIdTipoActor(14);
//            InteresadoDTO entidadJur = new InteresadoDTO();
//            entidadJur.setCdNumIden("R4353819H");
//            entidadJur.setDsNombre("Jesús S.A.");
//            entidadJur.setIdTipoActor(13);
//
//            dto.setInteresadoDTO(interesadoDTO);
//            dto.setEntidadJurDTO(entidadJur);
//            dto.setCdUsuAlta("ADMIN-TEST1");
//            dto.setFcAltaFila(Date.from(Instant.now()));
//
//            dto = service.registrarPersonaJur(dto);
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//
//    }
//
//    /**
//     * Prueba el método registrarPersonaJur existente
//     */
//    @Test
//    public void testCrearPersonaJur() {
//        try {
//
//            // Interesado y entidad nuevos
//            UsuarioJurDTO dto = new UsuarioJurDTO();
//
//            InteresadoDTO interesadoDTO = new InteresadoDTO();
//            interesadoDTO.setCdNumIden("00000000A");
//            interesadoDTO.setDsNombre("Interesado A");
//            interesadoDTO.setIdTipoActor(14);
//            InteresadoDTO entidadJur = new InteresadoDTO();
//            entidadJur.setCdNumIden("B00000000");
//            entidadJur.setDsNombre("Entidad B");
//            entidadJur.setIdTipoActor(13);
//
//            dto.setInteresadoDTO(interesadoDTO);
//            dto.setEntidadJurDTO(entidadJur);
//            dto.setCdUsuAlta("ADMIN-TEST1");
//            dto.setFcAltaFila(Date.from(Instant.now()));
//
//            dto = service.crearPersonaJur(dto);
//
//            // No es nulo
//            Assert.assertNotNull(dto);
//
//        } catch (final ServiceException e) {
//            log.error(e.getMessage());
//            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
//        }
//    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(UsuarioJurDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdUsuarioJur());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());
        Assert.assertNotNull(dto.getEntidadJurDTO().getIdInteresado());

        if (Boolean.TRUE.equals(bRelleno)) {

            // Assert.assertNotNull(dto.getEntidadJurDTO().);

            // Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());
            // Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            // Assert.assertNotNull(dto.getFcModifFila());
            // Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            // Assert.assertNotNull(dto.getFcBajaFila());
        }
        // Assert.assertNotNull(dto.getCdUsuModif());
        // Assert.assertNotNull(dto.getCdUsuBaja());
        // Assert.assertNotNull(dto.getInteresadoDTO().getIdInteresado());
    }

}
