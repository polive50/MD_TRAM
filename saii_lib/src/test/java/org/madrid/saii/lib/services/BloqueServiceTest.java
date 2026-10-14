
package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
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
 * Test BloqueService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class BloqueServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("bloqueService")
    private BloqueService service;

    /**
     * Servicio componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;

    /**
     * Servicio bloqueComponente
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

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
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            BloqueDTO dto = new BloqueDTO();
            dto.setIdImpreso(2);
            dto.setIdTematica(new CatalogosValoresDTO(1));
            dto.setIdFormato(new CatalogosValoresDTO(1));

            dto.setNmFilas(4);
            dto.setNmOrden(8);
            dto.setTlAuxiliar("Descripcion Auxiliar");

            dto.setCdUsuAlta("ADMIN-TEST1");
            dto.setFcAltaFila(new Date());

            ComponenteDTO c1 = new ComponenteDTO();
            c1.setIdComponente(1);

            dto = this.service.guardar(dto, c1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            Bloque ent = this.sessionFactory.getCurrentSession().get(Bloque.class,
                    dto.getIdBloque());
            this.sessionFactory.getCurrentSession().evict(ent);

            /* Modificamos */

            dto.setTlAuxiliar("PRUEBA TEST");

            dto = this.service.guardar(dto, c1);

            // La modificación es correcta
            Assert.assertEquals("PRUEBA TEST", dto.getTlAuxiliar());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    // /**
    // * Prueba el metodo guardar texto existente
    // */
    @Test
    public void testGuardarTextoExistente() {
        try {

            BloqueDTO dto = this.service.buscarId(5);

            this.sessionFactory.getCurrentSession().clear();

            // GUARDAR
            dto.getLstBloqueComponenteDTO().get(0).getComponenteDTO()
                    .setClTextoFijo("Cambio Texto Fijo");

            dto = this.service.guardar(dto,
                    dto.getLstBloqueComponenteDTO().get(0).getComponenteDTO());

            // No es nulo
            Assert.assertNotNull(dto);

            // El listado bloqueComponente existe
            Assert.assertEquals(Boolean.FALSE, dto.getLstBloqueComponenteDTO().isEmpty());

            // Guarda correctamente
            Assert.assertEquals("Cambio Texto Fijo",
                    dto.getLstBloqueComponenteDTO().get(0).getComponenteDTO().getClTextoFijo());

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar bloque con un solo componente
     */
    @Test
    public void testGuardarBloqueTextoExistente() {
        try {

            BloqueComponenteDTO bcDto = new BloqueComponenteDTO();
            BloqueDTO dto = new BloqueDTO();
            ComponenteDTO c1 = new ComponenteDTO();

            dto = this.service.buscarId(26);
            this.sessionFactory.getCurrentSession().clear();

            c1 = this.componenteService.buscarId(175);

            Componente ent = this.sessionFactory.getCurrentSession().get(Componente.class,
                    c1.getIdComponente());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.guardar(dto, c1);

            // No es nulo
            Assert.assertNotNull(dto);

            bcDto = this.bloqueComponenteService.buscarPorBloqueComponente(dto.getIdBloque(),
                    c1.getIdComponente(), null);

            // Se crea correctamente el bloqueComponente asociado al bloque
            Assert.assertNotNull(bcDto);

            // Guarda correctamente
            Assert.assertEquals(c1.getNmLongitud(), bcDto.getComponenteDTO().getNmLongitud());

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar bloque nuevo con un solo componente
     */
    @Test
    public void testGuardarBloqueTextoNuevo() {
        try {

            BloqueComponenteDTO bcDto = new BloqueComponenteDTO();
            BloqueDTO dto = new BloqueDTO();
            ComponenteDTO c1 = new ComponenteDTO();

            dto = this.service.buscarId(26);
            dto.setIdBloque(null);
            this.sessionFactory.getCurrentSession().clear();

            c1 = this.componenteService.buscarId(175);

            Componente ent = this.sessionFactory.getCurrentSession().get(Componente.class,
                    c1.getIdComponente());
            this.sessionFactory.getCurrentSession().evict(ent);

            dto = this.service.guardar(dto, c1);

            // No es nulo
            Assert.assertNotNull(dto);

            bcDto = this.bloqueComponenteService
                    .buscarId(dto.getLstBloqueComponenteDTO().get(0).getIdBloqueComponente());
            // Se crea correctamente el bloqueComponente asociado al bloque
            Assert.assertNotNull(bcDto);

            // Guarda correctamente
            Assert.assertEquals(c1.getNmLongitud(), bcDto.getComponenteDTO().getNmLongitud());

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            BloqueDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdBloque());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<BloqueDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<BloqueDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                BloqueDTO dto = it.next();

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
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(BloqueDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdBloque());
        Assert.assertNotNull(dto.getIdImpreso());
        Assert.assertNotNull(dto.getIdFormato().getCatalogoDTO());
        Assert.assertNotNull(dto.getIdTematica().getCatalogoDTO());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getNmFilas());
            Assert.assertNotNull(dto.getNmOrden());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getTlAuxiliar()));

            Iterator<BloqueComponenteDTO> it = dto.getLstBloqueComponenteDTO().iterator();
            while (it.hasNext()) {
                BloqueComponenteDTO bloqueComponenteDTO = it.next();
                Assert.assertNotNull(bloqueComponenteDTO);
                Assert.assertNotNull(bloqueComponenteDTO.getIdBloqueComponente());
                Assert.assertNotNull(bloqueComponenteDTO.getIdBloque());
                Assert.assertNotNull(bloqueComponenteDTO.getComponenteDTO().getIdComponente());
            }
        }

        // Null Safe
        Assert.assertNotNull(dto.getTlAuxiliar());

    }

}
