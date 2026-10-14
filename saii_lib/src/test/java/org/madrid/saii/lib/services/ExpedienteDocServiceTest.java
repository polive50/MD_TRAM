package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
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
 * Test ExpedienteDocService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ExpedienteDocServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService service;

    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService convocatoriaTipoDocService;

    /**
     * DAO
     */
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    @Autowired
    @Qualifier("convocatoriaTipoDocDAO")
    private ConvocatoriaTipoDocDAO convocatoriaTipoDocDAO;

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
     * Test buscar
     */

    @Test
    public void testBuscar() {
        try {
            List<ExpedienteDocDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ExpedienteDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ExpedienteDocDTO dto = it.next();

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
    public void testBuscarId() {
        try {
            ExpedienteDocDTO dto = this.service.buscarId(5);

            Assert.assertNotNull(dto);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testGuardarConHistorico() {
        try {
            // GUARDAR HISTORICO
            ExpedienteDocDTO dto = new ExpedienteDocDTO();
            ExpedienteDTO idExpediente = new ExpedienteDTO();
            idExpediente.setIdExpediente(1155);
            CatalogosValoresDTO estado = new CatalogosValoresDTO(117);
            CatalogosValoresDTO tipoDocumento = new CatalogosValoresDTO(117);
            ConvocatoriaCauDTO convCausa = new ConvocatoriaCauDTO();
            convCausa.setIdConvocatoriaCau(1);
            ExpedienteDTO expDTO = new ExpedienteDTO();
            expDTO.setIdExpediente(1);
            
            dto.setIdExpedienteDoc(1);
            dto.setExpedienteDTO(expDTO);
            dto.setCdFica(678439);
            dto.setEstadoDTO(estado);
            dto.setTipoDocumentoDTO(tipoDocumento);
            dto.setTlCausaDescripcion("Prueba test");
            dto.setConvocatoriaCauDTO(convCausa);
            dto.setTlObservaciones("Observaciones");
            dto.setCdUsuAlta("ADMIN");
            dto.setFcAltaFila(new Date());
            dto.setCdUsuModif("ADMON");
            dto.setFcModifFila(new Date());
            dto.setCdUsuBaja("ADMON");
            dto.setFcBajaFila(new Date());

            dto = this.service.guardarConHistorico(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ExpedienteDoc ent = this.sessionFactory.getCurrentSession().get(ExpedienteDoc.class,
                    dto.getIdExpedienteDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setCdUsuModif("ROOT");
            dto = this.service.guardarConHistorico(dto);

            // La modificación es correcta
            Assert.assertEquals("ROOT", dto.getCdUsuModif());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testGuardar() {
        try {
            // GUARDAR HISTORICO
            ExpedienteDocDTO dto = new ExpedienteDocDTO();
            ExpedienteDTO idExpediente = new ExpedienteDTO();
            idExpediente.setIdExpediente(1155);
            CatalogosValoresDTO estado = new CatalogosValoresDTO(117);
            CatalogosValoresDTO tipoDocumento = new CatalogosValoresDTO(117);
            ConvocatoriaCauDTO convCausa = new ConvocatoriaCauDTO();
            convCausa.setIdConvocatoriaCau(1);
            
            ExpedienteDTO expDTO = new ExpedienteDTO();
            expDTO.setIdExpediente(1);

            dto.setIdExpedienteDoc(1);
            dto.setExpedienteDTO(expDTO);
            dto.setCdFica(678439);
            dto.setEstadoDTO(estado);
            dto.setTipoDocumentoDTO(tipoDocumento);
            dto.setTlCausaDescripcion("Prueba test");
            dto.setConvocatoriaCauDTO(convCausa);
            dto.setTlObservaciones("Observaciones");
            dto.setCdUsuAlta("ADMIN");
            dto.setFcAltaFila(new Date());
            dto.setCdUsuModif("ADMON");
            dto.setFcModifFila(new Date());
            dto.setCdUsuBaja("ADMON");
            dto.setFcBajaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ExpedienteDoc ent = this.sessionFactory.getCurrentSession().get(ExpedienteDoc.class,
                    dto.getIdExpedienteDoc());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            //dto.setCdUsuModif("ROOT");
            dto = this.service.guardarConHistorico(dto);

            // La modificación es correcta
            Assert.assertEquals("ROOT", dto.getCdUsuModif());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    //    @Test
    //    public void testGuardarExterno() throws ServiceException {
    //
    //        ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
    //
    //        // expedienteDoc.setIdExpedienteDoc(18);
    //        CatalogosValoresDTO tipoDocumento = new CatalogosValoresDTO();
    //        tipoDocumento.setIdValor(3);
    //
    //        ConvocatoriaTipoDocDTO convocatoriaTipoDoc = new ConvocatoriaTipoDocDTO();
    //        convocatoriaTipoDoc.setTipoDocumentoDTO(tipoDocumento);
    //        convocatoriaTipoDoc.setIdConvocatoria(14);
    //        expedienteDoc.setIdExpediente(1155);
    //
    //        byte[] contenido = "contenido del documento".getBytes();
    //        expedienteDoc.setContenidoFichero(contenido);
    //        FicaMetadatos ficaMetadatos = new FicaMetadatos();
    //        ficaMetadatos.setExtension("PDF");
    //        ficaMetadatos.setFechaFichero(new Date());
    //        ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
    //        ficaMetadatos.setNombreFichero("Captura.PDF");
    //        ficaMetadatos.setUsuario("admin");
    //        expedienteDoc.setFicaMetadatos(ficaMetadatos);
    //
    //        ExpedienteDocServiceImpl serviceImpl = new ExpedienteDocServiceImpl();
    //        FicaService ficaService = mock(FicaService.class);
    //        when(ficaService.guardar(expedienteDoc.getContenidoFichero(),
    //                expedienteDoc.getFicaMetadatos())).thenReturn(new FicaFicheroImpl());
    //        serviceImpl.setFicaService(ficaService);
    //        serviceImpl.setDao(this.expedienteDocDAO);
    //        serviceImpl.setConvocatoriaTipoDocDAO(this.convocatoriaTipoDocDAO);
    //        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
    //        expedienteDoc = serviceImpl.guardarExterno(expedienteDoc);
    //
    //        Assert.assertNotNull(expedienteDoc);
    //
    //    }

    //    @Test
    //    public void testGuardarExternoExistenteTipoDoc() throws ServiceException {
    //
    //        ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
    //        CatalogosValoresDTO tipoDocumento = new CatalogosValoresDTO();
    //        tipoDocumento.setIdValor(3);
    //        expedienteDoc.setIdExpediente(1155);
    //
    //        byte[] contenido = "contenido del documento".getBytes();
    //        expedienteDoc.setContenidoFichero(contenido);
    //        FicaMetadatos ficaMetadatos = new FicaMetadatos();
    //        ficaMetadatos.setExtension("PDF");
    //        ficaMetadatos.setFechaFichero(new Date());
    //        ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
    //        ficaMetadatos.setNombreFichero("Captura.PDF");
    //        ficaMetadatos.setUsuario("admin");
    //        expedienteDoc.setFicaMetadatos(ficaMetadatos);
    //
    //        ExpedienteDocServiceImpl serviceImpl = new ExpedienteDocServiceImpl();
    //        FicaService ficaService = mock(FicaService.class);
    //        when(ficaService.guardar(expedienteDoc.getContenidoFichero(),
    //                expedienteDoc.getFicaMetadatos())).thenReturn(new FicaFicheroImpl());
    //        serviceImpl.setFicaService(ficaService);
    //        serviceImpl.setDao(this.expedienteDocDAO);
    //        serviceImpl.setConvocatoriaTipoDocDAO(this.convocatoriaTipoDocDAO);
    //        serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
    //        expedienteDoc = serviceImpl.guardarExterno(expedienteDoc);
    //
    //        Assert.assertNotNull(expedienteDoc);
    //    }

    //    @Test
    //    public void testGuardarExternoExistenteBloque() throws ServiceException {
    //        try {
    //            ExpedienteDocDTO expedienteDoc = new ExpedienteDocDTO();
    //            CatalogosValoresDTO tipoDocumento = new CatalogosValoresDTO();
    //            tipoDocumento.setIdValor(3);
    //
    //            ConvocatoriaTipoDocDTO convocatoriaTipoDoc = new ConvocatoriaTipoDocDTO();
    //            convocatoriaTipoDoc.setIdConvocatoria(14);
    //            convocatoriaTipoDoc.setTipoDocumentoDTO(tipoDocumento);
    //            expedienteDoc.setIdExpediente(1155);
    //            expedienteDoc.setFicaMetadatos(null);
    //
    //            ExpedienteDocServiceImpl serviceImpl = new ExpedienteDocServiceImpl();
    //            FicaService ficaService = mock(FicaService.class);
    //            when(ficaService.guardar(expedienteDoc.getContenidoFichero(),
    //                    expedienteDoc.getFicaMetadatos())).thenReturn(new FicaFicheroImpl());
    //            serviceImpl.setFicaService(ficaService);
    //            serviceImpl.setDao(this.expedienteDocDAO);
    //            serviceImpl.setConvocatoriaTipoDocDAO(this.convocatoriaTipoDocDAO);
    //            serviceImpl.setExpedienteDocHistDAO(this.expedienteDocHistDAO);
    //            expedienteDoc = serviceImpl.guardarExterno(expedienteDoc);
    //
    //            Assert.assertNotNull(expedienteDoc);
    //        } catch (final ServiceException e) {
    //            log.error(e.getMessage());
    //        }
    //    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ExpedienteDocDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdExpedienteDoc());
        Assert.assertNotNull(dto.getCdFica());
        Assert.assertNotNull(dto.getCdUsuAlta());
        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getCdUsuBaja());
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertNotNull(dto.getEstadoDTO());
            Assert.assertNotNull(dto.getConvocatoriaCauDTO());
            Assert.assertNotNull(dto.getTlCausaDescripcion());
            Assert.assertNotNull(dto.getExpedienteDTO().getIdExpediente());
            Assert.assertNotNull(dto.getTlObservaciones());
        }

        // Null Safe
        Assert.assertNotNull(dto.getEstilo());
    }

}
