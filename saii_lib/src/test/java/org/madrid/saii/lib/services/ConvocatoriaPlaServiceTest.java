package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;

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
import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
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
 * Test ConvocatoriaPlaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaPlaServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaPlaService")
    private ConvocatoriaPlaService service;

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
    @Autowired
    @Test
    public void testGuardar() throws ServiceException {

        // GUARDAR
        ConvocatoriaPlaDTO dto = new ConvocatoriaPlaDTO();
        dto.setIdConvocatoria(13);
        dto.setCdPlantilla("SAII-Subsanacion_administrativa");
        dto.setCdUsuAlta("Admin");
        dto.setDsAsunto("Asunto1");
        dto.setFcAltaFila(new Date());
        dto.setCdUsuModif(Constantes.Test.TEST_ADMIN);
        dto.setCdUsuBaja(Constantes.Test.TEST_ADMIN);
        dto.setFcAltaFila(new Date());
        dto.setFcModifFila(new Date());
        dto.setFcBajaFila(new Date());
        dto.setTramiteDTO(new CatalogosValoresDTO(412));

        ConvocatoriaPlaDTO dto_ = this.service.guardar(dto);

        Assert.assertEquals("Admin", dto_.getCdUsuAlta());

        // No es nulo
        Assert.assertNotNull(dto_);

        // Está relleno
        this.testAtributos(dto_, Boolean.TRUE);

        ConvocatoriaPla ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaPla.class,
                dto_.getIdConvocatoriaPla());
        this.sessionFactory.getCurrentSession().evict(ent);

        // Guardamos
        dto_.setCdUsuModif("SAIIUSU1");
        ConvocatoriaPlaDTO dto3 = this.service.guardar(dto_);

        // La modificación es correcta
        Assert.assertEquals("SAIIUSU1", dto3.getCdUsuModif());

    }


    @Test
    public void testBuscarPorId() throws ServiceException {

        ConvocatoriaPlaDTO dto = this.service.buscarId(4);

        // No es nulo
        Assert.assertNotNull(dto);

        // Está relleno el id
        Assert.assertNotNull(dto.getIdConvocatoriaPla());

        this.testAtributos(dto, Boolean.FALSE);

    }

    @Test
    public void testBuscar() throws ServiceException {

        List<ConvocatoriaPlaDTO> lstDTO = this.service.buscar(0, -1, null, null);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        // Comprobamos los atributos obligatorios
        // El DTO tiene que ser null safe.
        Iterator<ConvocatoriaPlaDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            ConvocatoriaPlaDTO dto = it.next();

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

    }

    @Test
    public void testValidadAsociacionDuplicada() throws ServiceException {
        Integer idConvocatoria = 10;
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        List<ConvocatoriaPlaDTO> lst = this.service.buscar(0, -1, filtros, null);
        // No es nulo
        Assert.assertNotNull(lst);
        // Está relleno el id
        Assert.assertFalse(lst.isEmpty());

    }


    /**
     * Testobtener plantillas por convocatoria.
     *
     * @throws ServiceException the service exception
     */
    @Test
    public void testobtenerPlantillasPorConvocatoria() throws ServiceException {
        List<ConvocatoriaPlaDTO> lstDTO = this.service.obtenerPlantillasPorConvocatoria(14);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está relleno
        Assert.assertFalse(lstDTO.isEmpty());

        // Comprobamos los atributos obligatorios
        // El DTO tiene que ser null safe.
        Iterator<ConvocatoriaPlaDTO> it = lstDTO.iterator();
        while (it.hasNext()) {
            ConvocatoriaPlaDTO dto = it.next();

            this.testAtributos(dto, Boolean.FALSE);
        }

        // Busqueda vacía
        lstDTO = this.service.obtenerPlantillasPorConvocatoria(0);

        // No es nulo
        Assert.assertNotNull(lstDTO);

        // Está vacio
        Assert.assertTrue(lstDTO.isEmpty());

    }

    /**
     * Test nueva comvocatoria
     *
     * @throws ServiceException the service exception
     */
    @Test
    public void testNuevaConvocatoria() throws ServiceException {

        //TODO

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaPlaDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertNotNull(dto.getIdConvocatoriaPla());
        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
        Assert.assertNotNull(dto.getFcAltaFila());
        Assert.assertNotNull(dto.getIdConvocatoriaPla());
        Assert.assertNotNull(dto.getTramiteDTO());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getCdUsuBaja());
            Assert.assertNotNull(dto.getTramiteDTO());
        }

    }

}
