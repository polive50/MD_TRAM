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
import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
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
 * Test ConvocatoriaReqService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaReqServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaReqService")
    private ConvocatoriaReqService service;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

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
            ConvocatoriaReqDTO dto = new ConvocatoriaReqDTO();
            dto.setIdConvocatoria(24);
            dto.setConvocatoriaSubmodDTO(null);
            dto.setCdUsuAlta("ADMIN");
            dto.setFcAltaFila(new Date());
            dto.setCdUsuBaja("prueba");
            dto.setFcBajaFila(new Date());
            dto.setRequisitoDTO(null);
            dto.setCdUsuModif("ADMIN");
            dto.setFcModifFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

            ConvocatoriaReq ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaReq.class,
                    dto.getIdConvocatoriaReq());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            CatalogosValoresDTO requisitoDTO = new CatalogosValoresDTO();
            requisitoDTO.setIdValor(5);
            requisitoDTO.setCdCodigo("codigo");
            requisitoDTO.setDsValor("prueba");
            dto.setRequisitoDTO(requisitoDTO);
            ConvocatoriaSubmodDTO submodDTO = new ConvocatoriaSubmodDTO();
            submodDTO.setIdConvocatoriaSubmod(27);
            dto.setConvocatoriaSubmodDTO(submodDTO);
            dto.setCdUsuModif("USER");
            dto.setFcModifFila(new Date(1000));

            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertNotNull(dto.getConvocatoriaSubmodDTO());
            Assert.assertNotNull(dto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod());
            Assert.assertEquals("USER", dto.getCdUsuModif());
            Assert.assertEquals(new Date(1000), dto.getFcModifFila());
            Assert.assertEquals("codigo", dto.getRequisitoDTO().getCdCodigo());

            this.testAtributos(dto, Boolean.TRUE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscarPorId() {
        try {
            ConvocatoriaReqDTO dto = this.service.buscarId(100);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoriaReq());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testObtenerRequisitoPorConvocatoria() {
        try {
            List<ConvocatoriaReqDTO> lstDto = this.service.obtenerRequisitoPorConvocatoria(24);

            // No es nulo
            Assert.assertNotNull(lstDto);

            // Está relleno
            Assert.assertFalse(lstDto.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaReqDTO> it = lstDto.iterator();
            while (it.hasNext()) {
                ConvocatoriaReqDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDto = this.service.obtenerRequisitoPorConvocatoria(0);

            // No es nulo
            Assert.assertNotNull(lstDto);

            // Está vacio
            Assert.assertTrue(lstDto.isEmpty());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBuscar() {
        try {
            List<ConvocatoriaReqDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaReqDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaReqDTO dto = it.next();

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
    private void testAtributos(ConvocatoriaReqDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoriaReq());

        Assert.assertNotNull(dto.getIdConvocatoria());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getCdUsuAlta());

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getConvocatoriaSubmodDTO());

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertNotNull(dto.getCdUsuModif());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));

            Assert.assertNotNull(dto.getCdUsuBaja());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));

            Assert.assertNotNull(dto.getFcBajaFila());

            Assert.assertNotNull(dto.getRequisitoDTO());

            Assert.assertNotNull(dto.getRequisitoDTO().getIdValor());

            Assert.assertNotNull(dto.getRequisitoDTO().getCdCodigo());

            Assert.assertNotNull(dto.getRequisitoDTO().getDsValor());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getRequisitoDTO().getCdCodigo()));

            Assert.assertTrue(StringUtils.isNotBlank(dto.getRequisitoDTO().getDsValor()));

            Assert.assertNotNull(dto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod());
        }

    }

    @Test
    public void testAltaLogica() {
        try {

            ConvocatoriaReqDTO convReqDTO = this.service.buscarId(102);

            Assert.assertNotNull(convReqDTO);

            ConvocatoriaReq ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaReq.class,
                    convReqDTO.getIdConvocatoriaReq());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.altaLogica(convReqDTO);

            convReqDTO = this.service.buscarId(102);

            // No es nulo
            Assert.assertNotNull(convReqDTO);

            // Esta relleno el id
            Assert.assertNotNull(convReqDTO.getIdConvocatoria());

            // La fecha esta vacia
            Assert.assertNull(convReqDTO.getFcBajaFila());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBorradoLogico() {
        try {

            ConvocatoriaReqDTO convReqDTO = this.service.buscarId(100);

            Assert.assertNotNull(convReqDTO);

            ConvocatoriaReq ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaReq.class,
                    convReqDTO.getIdConvocatoriaReq());
            this.sessionFactory.getCurrentSession().evict(ent);

            this.service.borradoLogico(convReqDTO);

            convReqDTO = this.service.buscarId(100);

            // No es nulo
            Assert.assertNotNull(convReqDTO);

            // Esta rellena la fecha de baja
            Assert.assertNotNull(convReqDTO.getFcBajaFila());

            // Esta relleno el id
            Assert.assertNotNull(convReqDTO.getIdConvocatoria());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testBorradoLogicoPorConvocatoria() {

        try {

            this.service.borradoLogicoPorConvocatoria(24);

            ConvocatoriaReqDTO convReqDTO = this.service.buscarId(100);

            // No es nulo
            Assert.assertNotNull(convReqDTO);

            // Esta rellena la fecha de baja
            Assert.assertNotNull(convReqDTO.getFcBajaFila());

            // Esta relleno el id
            Assert.assertNotNull(convReqDTO.getIdConvocatoria());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    @Test
    public void testAgregar() {

        try {

            List<CatalogosValoresDTO> lstRequisitos = this.catalogosValoresService
                    .buscarPorCatalogo(30);

            Assert.assertNotNull(lstRequisitos);

            ConvocatoriaReqDTO convReqDTO = this.service.agregar(24,
                    lstRequisitos.get(0).getIdValor());

            // No es nulo
            Assert.assertNotNull(convReqDTO);

            // Esta relleno el id
            Assert.assertNotNull(convReqDTO.getIdConvocatoriaReq());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

}
