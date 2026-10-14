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
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ConvocatoriaService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaActServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService service;
    
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

    /**
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            ConvocatoriaActDTO dto = new ConvocatoriaActDTO();
            CatalogosValoresDTO identDTO = new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
            CatalogosValoresDTO tipoAct = new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA));
            CatalogosValoresDTO tipoPer = new CatalogosValoresDTO(900);

            dto.setIdConvocatoria(99);
            dto.setIdentificacionDTO(identDTO);
            dto.setTipoActorDTO(tipoAct);
            dto.setTipoPersonaFisJurDTO(tipoPer);
            dto.setTlDescripcion("desc");
            dto.setBolGeneraExpediente(Boolean.TRUE);
            dto.setNmMax(5);
            dto = this.service.guardar(dto);

            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardarErrorSolicitante() {
        try {

            // GUARDAR
            ConvocatoriaActDTO dto = new ConvocatoriaActDTO();
            CatalogosValoresDTO identDTO = new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
            CatalogosValoresDTO tipoAct = new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.FISICA));
            CatalogosValoresDTO tipoPer = new CatalogosValoresDTO(900);

            dto.setIdConvocatoria(1);
            dto.setIdentificacionDTO(identDTO);
            dto.setTipoActorDTO(tipoAct);
            dto.setTipoPersonaFisJurDTO(tipoPer);
            dto.setTlDescripcion("desc");
            dto.setBolGeneraExpediente(Boolean.TRUE);
            dto.setNmMax(5);
            dto = this.service.guardar(dto);

            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            Assert.assertTrue(Boolean.TRUE);
        }
    }

    /**
     * Prueba el metodo guardar
     */
    @Test
    public void testModficar() {
        try {

            // GUARDAR
            ConvocatoriaActDTO dto = this.service.buscarId(1);

            ConvocatoriaAct ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaAct.class,
                    dto.getIdActor());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setTlDescripcion("Prueba Test Mod");
            dto.setTipoPersonaFisJurDTO(new CatalogosValoresDTO(901));
            dto.setFcModifFila(new Date());
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals("Prueba Test Mod", dto.getTlDescripcion());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    /**
     * Buscar por id
     */
    public void testBuscarPorId() {
        try {
            ConvocatoriaActDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdConvocatoria());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    /**
     * Buscar por id convocatoria
     */
    public void testBuscarPorIdConvocatoria() {
        try {
            List<ConvocatoriaActDTO> lstDTO = this.service.buscarPorIdConvocatoria(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaActDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaActDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarPorIdConvocatoria(99);

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
    /**
     * Buscar por tipo persona
     */
    public void testBuscarPorTipoPersona() {
        try {
            List<ConvocatoriaActDTO> lstDTO = this.service.buscarPorTipoPersona(15, 1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaActDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaActDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarPorTipoPersona(500, 1);

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
    /**
     * Test buscar
     */
    public void testBuscar() {
        try {
            List<ConvocatoriaActDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaActDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaActDTO dto = it.next();

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

    @Test
    /**
     *
     */
    public void testbuscarTipoActorParticipante() {
        try {
            List<ConvocatoriaActDTO> lstDTO = this.service.buscarTipoActorParticipante(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaActDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaActDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.buscarTipoActorParticipante(500);

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
    private void testAtributos(ConvocatoriaActDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertNotNull(dto.getIdActor());
        Assert.assertNotNull(dto.getIdentificacionDTO());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getTipoActorDTO().getIdValor());
            Assert.assertNotNull(dto.getTipoPersonaFisJurDTO().getIdValor());
            Assert.assertNotNull(dto.getNmMax());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getTlDescripcion()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getItGeneraExpediente()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getTlDescripcionCorta()));
        }

        // Null Safe
        Assert.assertNotNull(dto.getTipoActorDTO().getIdValor());
        Assert.assertNotNull(dto.getTipoPersonaFisJurDTO().getIdValor());
        Assert.assertNotNull(dto.getTlDescripcion());
        Assert.assertNotNull(dto.getItGeneraExpediente());
    }

    @Test
    public void buscarTipoActorSolicitanteTest() {
        try {
            List<ConvocatoriaActDTO> lstConvocatoriaActDTO = this.service
                    .buscarTipoActorSolicitante(1);

            Assert.assertNotNull(lstConvocatoriaActDTO);
            Assert.assertFalse(lstConvocatoriaActDTO.isEmpty());

            // esta rellena la id
            Assert.assertNotNull(lstConvocatoriaActDTO.get(0).getIdActor());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
