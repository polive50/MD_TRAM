package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
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
 * Test EvaluacionService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ExpedienteJusTecServiceTest extends AtlasAbstractBaseTest{
    
    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService service;
    
    @Autowired
    @Qualifier("expedienteJusTecDTO")
    private ExpedienteJusTecDTO expedienteJusTecDTO;
    
    @Autowired
    @Qualifier("expedienteJusTeccDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;

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
     * Comprueba los atributos de ExpedienteJusTecDTO
     *
     * @param dto      El objeto ExpedienteJusTecDTO a verificar
     */
    private void testAtributos(ExpedienteJusTecDTO dto, Boolean bRelleno) {
        // Verificación de atributos obligatorios
        Assert.assertNotNull(dto.getIdExpediente());
        Assert.assertNotNull(dto.getIdExpedienteJusTec());
        Assert.assertNotNull(dto.getTipoExpedienteJusTecDTO());
        Assert.assertNotNull(dto.getNmPeriodo());
        Assert.assertNotNull(dto.getFcIniJustiCienTec());
        Assert.assertNotNull(dto.getFcFinJustiCienTec());
        Assert.assertNotNull(dto.getTlObservacion());
        Assert.assertNotNull(dto.getIdEstadoRevAdminDTO());
        Assert.assertNotNull(dto.getIdEstadoDTO());
    }

    /**
     * Comprueba los atributos de ExpedienteJusTecDTO
     *
     * @param dto      El objeto ExpedienteJusTecDTO a verificar
     */
    private void testAtributosJustecDoc(ExpedienteJustecDocDTO dto) {
        // Verificación de atributos obligatorios
        Assert.assertNotNull(dto.getIdExpedienteDoc());
        Assert.assertNotNull(dto.getIdExpedienteJusTec());
        Assert.assertNotNull(dto.getIdExpedienteJustecDoc());
        Assert.assertNotNull(dto.getCumple());
        
    }
    @Test
    public void testBuscar() {
        try {
            List<ExpedienteJusTecDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteJusTecDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteJusTecDTO dto = it.next();

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
    public void testGuardar() {
        try {
            // Crear un objeto DTO para el expediente
            ExpedienteJusTecDTO dto = new ExpedienteJusTecDTO();
            dto.setIdExpediente(2);

            BloqueDTO bloqueDTO = new BloqueDTO();
            bloqueDTO.setIdBloque(1);

            dto.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            // Llamar a la función guardar
            dto = this.service.guardar(dto);

            // Verificar que el DTO devuelto no sea nulo
            Assert.assertNotNull(dto);

            // Verificar atributos del DTO
            this.testAtributos(dto, Boolean.FALSE);

            // Limpiar la instancia
            ExpedienteJusTec expedienteEntity = this.sessionFactory.getCurrentSession()
                    .get(ExpedienteJusTec.class, dto.getIdExpediente());
            this.sessionFactory.getCurrentSession().evict(expedienteEntity);

            // Modificar y guardar nuevamente

            dto = this.service.guardar(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }
    
    
    @Test
    public void testBuscarJustecDoc() {
        try {

            // Crear un objeto DTO para el expediente
            ExpedienteJusTecDTO dtoJusTec = new ExpedienteJusTecDTO();
            dtoJusTec.setIdExpediente(2);

            dtoJusTec.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            // Llamar a la función guardar
            dtoJusTec = this.service.guardar(dtoJusTec);
            
            ExpedienteJustecDocDTO dtoGuardar=new ExpedienteJustecDocDTO();
            dtoGuardar.setIdExpedienteJusTec(dtoJusTec.getIdExpedienteJusTec());
            dtoGuardar.setIdExpedienteDoc(1);
            dtoGuardar.setCumple(Boolean.TRUE);
            List<ExpedienteJustecDocDTO> lstDTO = this.service.buscarDocs(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteJustecDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteJustecDocDTO dto = it.next();

                this.testAtributosJustecDoc(dto);
            }

            // Búsqueda vacia
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

            lstDTO = this.service.buscarDocs(0, -1, filtros, null);

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
    public void testGuardarJustecDoc() {
        try {
         // Crear un objeto DTO para el expediente
            ExpedienteJusTecDTO dtoJusTec = new ExpedienteJusTecDTO();
            dtoJusTec.setIdExpediente(2);

            dtoJusTec.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            // Llamar a la función guardar
            dtoJusTec = this.service.guardar(dtoJusTec);
            
            ExpedienteJustecDocDTO dto=new ExpedienteJustecDocDTO();
            dto.setIdExpedienteJusTec(dtoJusTec.getIdExpedienteJusTec());
            dto.setIdExpedienteDoc(1);
            dto.setCumple(Boolean.TRUE);

            // Llamar a la función guardar
            this.service.guardarDocumentoJustec(dto);

            // Verificar que el DTO devuelto no sea nulo
            Assert.assertNotNull(dto);

            // Verificar atributos del DTO
            this.testAtributosJustecDoc(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
