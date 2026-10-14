
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
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test BloqueComponenteService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class BloqueComponenteServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService service;

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
            BloqueComponenteDTO dto = new BloqueComponenteDTO();
            dto.setFcAltaFila(new Date());
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            ComponenteDTO componenteDTO = new ComponenteDTO();
            componenteDTO.setIdComponente(10);

            BloqueDTO bloqueDTO = new BloqueDTO();
            bloqueDTO.setIdBloque(10);

            dto.setIdBloque(10);
            dto.setComponenteDTO(componenteDTO);
            dto.setIdBloqueComponente(10);
            dto.setItColumna(false);

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            BloqueComponente ent = this.sessionFactory.getCurrentSession()
                    .get(BloqueComponente.class, dto.getIdBloqueComponente());
            this.sessionFactory.getCurrentSession().evict(ent);

            /* Modificamos */

            dto.setNmOrden(100);

            dto = this.service.guardar(dto);

            boolean comparacion = dto.getNmOrden().compareTo(100) == 0;

            // La modificación es correcta
            Assert.assertTrue(comparacion);

            comparacion = dto.getNmOrden().compareTo(-3) == 0;

            // La modificación es incorrecta
            Assert.assertFalse(comparacion);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<BloqueComponenteDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<BloqueComponenteDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                BloqueComponenteDTO dto = it.next();

                assertNotNull(dto.getIdBloque());

                assertNotNull(dto.getIdBloqueComponente());

                Assert.assertNotNull(dto.getComponenteDTO().getItEditable());

                Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

                Assert.assertNotNull(dto.getNmOrden());

                this.testAtributos(dto, Boolean.TRUE);
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

    @Test
    public void testBuscarPorBloqueComponente() {
        try {
            BloqueComponenteDTO bloqueComponenteDTO = this.service.buscarPorBloqueComponente(1, 2,
                    null);

            Assert.assertNotNull(bloqueComponenteDTO);

            // Otra búsqueda para que pase por el if del servicio
            bloqueComponenteDTO = this.service.buscarPorBloqueComponente(1, 109, null);

            Assert.assertNotNull(bloqueComponenteDTO.getIdBloque());

            Assert.assertNotNull(bloqueComponenteDTO.getIdBloqueComponente());

            Assert.assertNotNull(bloqueComponenteDTO.getComponenteDTO().getItEditable());

            Assert.assertTrue(StringUtils.isNotBlank(bloqueComponenteDTO.getCdUsuAlta()));

            Assert.assertNotNull(bloqueComponenteDTO.getNmOrden());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testExisteComponenteEnBloque() {
        try {
            Boolean utilizado = this.service.existeComponenteEnBloque(-3);

            // No es nulo
            Assert.assertNotNull(utilizado);

            // Es false si no esta utilizando
            Assert.assertFalse(utilizado);

            // probamos un componente que sí se está utilizando
            utilizado = this.service.existeComponenteEnBloque(107);

            // True sí es está utilizando
            Assert.assertTrue(utilizado);

            utilizado = this.service.existeComponenteEnBloque(-25);

            // Es false si el bloque no existe
            Assert.assertFalse(utilizado);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            BloqueComponenteDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdBloqueComponente());

            Assert.assertNotNull(dto.getIdBloque());

            Assert.assertNotNull(dto.getComponenteDTO().getItEditable());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

            Assert.assertNotNull(dto.getNmOrden());

            // Modificamos
            dto.setIdBloqueComponente(5);

            boolean comparacion = dto.getIdBloqueComponente().compareTo(5) == 0;

            // La modificación es correcta
            Assert.assertTrue(comparacion);

            comparacion = dto.getIdBloqueComponente().compareTo(6) == 0;

            // La modificación es incorrecta
            Assert.assertFalse(comparacion);

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void textBuscarIdBloque() {
        try {
            List<BloqueComponenteDTO> lstDTO = this.service.buscarIdBloque(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<BloqueComponenteDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                BloqueComponenteDTO dto = it.next();

                Assert.assertNotNull(dto.getComponenteDTO().getIdComponente());

                Assert.assertNotNull(dto.getIdBloque());

                Assert.assertNotNull(dto.getComponenteDTO().getItEditable());

                Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

                Assert.assertNotNull(dto.getNmOrden());

                this.testAtributos(dto, Boolean.FALSE);
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
     * @parama relleno
     */
    private void testAtributos(BloqueComponenteDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdBloque());
        Assert.assertNotNull(dto.getComponenteDTO().getIdComponente());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getIdBloqueComponente());
        Assert.assertNotNull(dto.getItColumna());
        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getItFila());
            Assert.assertNotNull(dto.getItColumna());
        }

        // Null Safe
        Assert.assertNotNull(dto.getItFila());
        Assert.assertNotNull(dto.getItColumna());
        Assert.assertNotNull(dto.getItObligatorio());
        Assert.assertNotNull(dto.getItRelacion());

    }

    @Test
    public void existePorTipoDocumentoTest() {
        try {
            List<AtlasFilter> lstFiltros = new ArrayList<>();
            lstFiltros.add(new AtlasFilter("componente.tipoDocumento.idValor", Operador.NOT_NULL));
            List<BloqueComponenteDTO> lstBloqueComponenteDTO = this.service.buscar(0, -1,
                    lstFiltros, null);

            if (!lstBloqueComponenteDTO.isEmpty()) {
                Integer idTipoDocumento = lstBloqueComponenteDTO.get(0).getComponenteDTO()
                        .getTipoDocumentoDTO().getIdValor();
                Boolean existe = this.service.existePorTipoDocumento(idTipoDocumento);
                Assert.assertTrue(existe);
            }
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
