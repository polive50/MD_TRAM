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
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
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
 * Test ComponenteService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ComponenteServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService service;

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
            ComponenteDTO dto = new ComponenteDTO();
            CatalogosValoresDTO tipoDTO = new CatalogosValoresDTO(1696);
            CatalogosValoresDTO subTipoDTO = new CatalogosValoresDTO(1699);
            CatalogosValoresDTO categoriaDTO = new CatalogosValoresDTO(1685);

            dto.setDsNombre("Componente Nuevo");
            dto.setCategoriaDTO(categoriaDTO);
            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            dto.setFcAltaFila(new Date());

            dto.setTipoDTO(tipoDTO);
            dto.setSubTipoDTO(subTipoDTO);
            dto.setNmLongitud(50);
            dto.setItEditable(Boolean.TRUE);
            dto.setIdComponente(212);
            dto.setLngCalculoCampoPDF(7f);
            dto.setCdUsuModif(Constantes.Test.TEST_ADMIN);
            dto.setCdUsuBaja("Usuario");
            dto.setDsValorMultiple("Valor");
            dto.setEsColumna(Boolean.FALSE);
            dto.setFcBajaFila(new Date());
            dto.setLngPorcentajeCampoPDF(8.5f);
            dto.setPuedeRelacionar(Boolean.FALSE);

            dto = this.service.guardar(dto);
            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            // El campo itEditable devuelve el resultado esperado
            Assert.assertTrue(dto.getItEditable());

            Componente ent = this.sessionFactory.getCurrentSession().get(Componente.class,
                    dto.getIdComponente());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setItEditable(Boolean.FALSE);
            dto.setDsNombre("Componente Nuevo Mod");
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("Componente Nuevo Mod", dto.getDsNombre());
            Assert.assertFalse(dto.getItEditable());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            ComponenteDTO dto = this.service.buscarId(2);
            // No es nulo
            Assert.assertNotNull(dto);
            // Está relleno el id
            Assert.assertNotNull(dto.getIdComponente());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ComponenteDTO> lstDTO = this.service.buscar(0, -1, null, null);
            // No es nulo
            Assert.assertNotNull(lstDTO);
            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());
            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ComponenteDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ComponenteDTO dto = it.next();

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
     * Prueba el metodo guardar documento
     */
    @Test
    public void testGuardarDocumento() {
        try {

            ComponenteDTO dto = this.service.guardarDocumento(1, null, null);
            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar documento
     */
    @Test
    public void testGuardarDocumento2() {
        try {

            ComponenteDTO dto = this.service.guardarDocumento(1, null, new ArrayList<>());
            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar documento
     */
    @Test
    public void testGuardarDocumento3() {
        try {

            List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>();
            ComponenteDTO componente = this.service.buscarId(116);
            BloqueComponenteDTO bloqueComponente = new BloqueComponenteDTO();
            bloqueComponente.setComponenteDTO(componente);
            lstBloqueComponente.add(bloqueComponente);
            ComponenteDTO dto = this.service.guardarDocumento(1, null, lstBloqueComponente);
            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar documento
     */
    @Test
    public void testGuardarDocumento4() {
        try {

            List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>();
            ComponenteDTO componente = this.service.buscarId(116);
            BloqueComponenteDTO bloqueComponente = new BloqueComponenteDTO();
            bloqueComponente.setIdBloqueComponente(25);
            bloqueComponente.setComponenteDTO(componente);
            lstBloqueComponente.add(bloqueComponente);
            ComponenteDTO dto = this.service.guardarDocumento(1, null, lstBloqueComponente);
            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar documento para que pase por DataAccessException
     *
     * @throws ServiceException
     */
    @Test(expected = AssertionError.class)
    public void testGuardarDocumentoKO() throws ServiceException {

        List<BloqueComponenteDTO> lstBloqueComponente = new ArrayList<>();
        ComponenteDTO componente = this.service.buscarId(116);
        BloqueComponenteDTO bloqueComponente = new BloqueComponenteDTO();
        bloqueComponente.setIdBloqueComponente(26);
        bloqueComponente.setComponenteDTO(componente);
        lstBloqueComponente.add(bloqueComponente);
        this.service.guardarDocumento(1, null, lstBloqueComponente);

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ComponenteDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        Assert.assertNotNull(dto.getIdComponente());
        Assert.assertNotNull(dto.getDsNombre());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getItEditable());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getNmLongitud());
            Assert.assertNotNull(dto.getIdComponente());
            Assert.assertNotNull(dto.getLngCalculoCampoPDF());
            Assert.assertNotNull(dto.getTipoDTO());
            Assert.assertNotNull(dto.getSubTipoDTO());
            Assert.assertNotNull(dto.getCategoriaDTO());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsValorMultiple()));
            Assert.assertFalse(dto.getEsColumna());
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getLngPorcentajeCampoPDF());
            Assert.assertFalse(dto.getPuedeRelacionar());

        }
    }

    @Test
    public void buscarPorTipoDocumentoTest() {
        try {
            List<AtlasFilter> lstFiltros = new ArrayList<>();
            lstFiltros.add(new AtlasFilter("tipoDocumento.idValor", Operador.NOT_NULL));
            List<ComponenteDTO> lstComponenteDTO = this.service.buscar(0, -1, lstFiltros, null);

            // No es nulo
            Assert.assertNotNull(lstComponenteDTO);

            // esta relleno
            Assert.assertFalse(lstComponenteDTO.isEmpty());

            if (!lstComponenteDTO.isEmpty()) {
                Integer idTipoDocumento = lstComponenteDTO.get(0).getTipoDocumentoDTO()
                        .getIdValor();
                Componente componente = this.service.buscarPorTipoDocumento(idTipoDocumento);
                Assert.assertNotNull(componente);
                Assert.assertNotNull(componente.getIdComponente());
            }
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
