package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.core.fica.s3.FicaMetadatosUsuario;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ConvocatoriaDocService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ConvocatoriaDocServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("convocatoriaDocService")
    private ConvocatoriaDocService service;

    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /** Repositorio ExpedienteDAO */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
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
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(13);

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(1);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuBaja("prueba");
            dto.setFcBajaFila(new Date());
            dto.setCdUsuAlta("prueba2");
            dto.setFcAltaFila(new Date());
            dto.setCdUsuModif("prueba3");
            dto.setFcModifFila(new Date());
            dto.setIdDocumento(3);
            dto.setDsNombreFichero("fichero");

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaDoc ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaDoc.class,
                    dto.getIdDocumento());
            this.sessionFactory.getCurrentSession().evict(ent);

            // Guardamos
            dto.setDsNombreFichero(Constantes.Test.TEST_MOD);
            dto.setFcBajaFila(new Date(1000));
            dto.setCdUsuBaja("usuario");
            dto.setCdUsuAlta("nombre");
            dto.setFcAltaFila(new Date(500));
            dto = this.service.guardar(dto);

            // La modificación es correcta
            Assert.assertEquals(Constantes.Test.TEST_MOD, dto.getDsNombreFichero());
            Assert.assertEquals(new Date(1000), dto.getFcBajaFila());
            Assert.assertEquals("usuario", dto.getCdUsuBaja());
            Assert.assertEquals("nombre", dto.getCdUsuAlta());
            Assert.assertEquals(new Date(500), dto.getFcAltaFila());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo subirDocumentoFica
     */
    @Test
    public void testSubirDocumentoFica() {
        try {

            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();

            byte[] contenido = "contenido del documento".getBytes();
            dto.setContenidoFichero(contenido);
            FicaMetadatos ficaMetadatos = new FicaMetadatos();
            ficaMetadatos.setExtension("JPG");
            ficaMetadatos.setFechaFichero(new Date());
            ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
            ficaMetadatos.setNombreFichero("Captura.JPG");
            ficaMetadatos.setUsuario("admin");
            dto.setFicaMetadatos(ficaMetadatos);

            Integer cdFica = 1;
            ConvocatoriaDocServiceImpl serviceImpl = new ConvocatoriaDocServiceImpl();
            FicaService ficaService = mock(FicaService.class);
            when(ficaService.recuperar(cdFica)).thenReturn(null);
            FicaFichero ficaFichero = new FicaFicheroImpl();
            when(ficaService.guardar(dto.getContenidoFichero(), dto.getFicaMetadatos()))
                    .thenReturn(ficaFichero);
            serviceImpl.setFicaService(ficaService);

            dto = serviceImpl.subirDocumentoFica(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            Assert.assertNotNull(dto.getIdTipoDocumento());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el metodo subirDocumentoFica con error
     */
    @Test(expected = ServiceException.class)
    public void testSubirDocumentoFicaError() throws ServiceException {
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        dto.setCdFica(659887);
        byte[] contenido = "contenido del documento".getBytes();
        dto.setContenidoFichero(contenido);
        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        ficaMetadatos.setExtension("");
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatos.setNombreFichero("Captura.JPG");
        ficaMetadatos.setUsuario("admin");
        dto.setFicaMetadatos(ficaMetadatos);
        this.service.subirDocumentoFica(dto);
    }

    @Test(expected = ServiceException.class)
    public void testSubirDocumentoFicaErrorContenidoFichero() throws ServiceException {
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        dto.setCdFica(659805);
        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        ficaMetadatos.setExtension("JPG");
        ficaMetadatos.setFechaFichero(new Date());
        ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatos.setNombreFichero("Captura.JPG");
        ficaMetadatos.setUsuario("admin");
        dto.setFicaMetadatos(ficaMetadatos);
        this.service.subirDocumentoFica(dto);
    }

    /**
     * Prueba el metodo guardar con error
     */
    @Test(expected = ServiceException.class)
    public void testGuardarError() throws ServiceException {
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        dto.setIdConvocatoria(0);
        this.service.guardar(dto);
    }

    @Test
    /**
     * Buscar por id
     */
    public void testBuscarPorId() {
        try {
            ConvocatoriaDocDTO dto = this.service.buscarId(1);

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

    @Test(expected = ServiceException.class)
    public void testBuscarPorIdError() throws ServiceException {
        this.service.buscarId(null);
    }

    @Test
    /**
     * Test buscar
     */
    public void testBuscar() {
        try {
            List<ConvocatoriaDocDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaDocDTO dto = it.next();

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
     * Test buscar
     */
    public void testObtenerDocumentoPorConvocatoriaFicaFalse() {
        try {
            List<ConvocatoriaDocDTO> lstDTO = this.service.obtenerDocumentosConvocatoria(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaDocDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

            // Búsqueda vacia
            lstDTO = this.service.obtenerDocumentosConvocatoria(99);

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
     * Buscar por id convocatoria
     */
    public void testBuscarPorIdConvocatoria() {
        try {
            List<ConvocatoriaDocDTO> lstDTO = this.service.buscarPorIdConvocatoria(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ConvocatoriaDocDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ConvocatoriaDocDTO dto = it.next();

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

    /**
     * Prueba el metodo Descargar
     */
    @Test
    public void testDescargar() {
        try {

            Integer cdFica = 1;
            ConvocatoriaDocServiceImpl serviceImpl = new ConvocatoriaDocServiceImpl();

            FicaService ficaService = mock(FicaService.class);
            when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());

            serviceImpl.setFicaService(ficaService);
            ConvocatoriaDocDTO dto = serviceImpl.descargar(cdFica);

            // No es nulo
            Assert.assertNotNull(dto);

            // Comprobaciones
            Assert.assertEquals(dto.getCdFica(), cdFica);
            Assert.assertNotNull(dto.getFicaFichero());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * prueba si existe documento acta comisión de evaluación
     */
    @Test
    public void testExisteTipoDocumentoComision() {
        try {
            Boolean existe = this.service.existeTipoDocumento(29,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION));
            Assert.assertNotNull(existe);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * prueba si existe documento acta comisión de evaluación
     */
    @Test(expected = ServiceException.class)
    public void testExisteTipoDocumentoComisionNull() throws ServiceException {

        this.service.existeTipoDocumento(null, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION));

    }

    /**
     * prueba si existe documento acta comisión de evaluación
     */
    @Test(expected = ServiceException.class)
    public void testConvocatoriaDocNull() throws ServiceException {

        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        dto.setIdConvocatoria(null);
        dto = this.service.guardar(dto);

    }

    /**
     * Guarda por primera vez el documento Acta comision evaluacion
     */
    @Test
    public void testGuardarDocumentoComisionEvaluacion() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();
            CatalogosDTO catalogoDTO = new CatalogosDTO();
            catalogoDTO.setIdCatalogo(13);
            tipoDocDTO.setIdValor(301);
            tipoDocDTO.setCatalogoDTO(catalogoDTO);
            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(29);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuBaja("prueba");
            dto.setFcBajaFila(new Date());
            dto.setCdUsuAlta("prueba2");
            dto.setFcAltaFila(new Date());
            dto.setCdUsuModif("prueba3");
            dto.setFcModifFila(new Date());

            // dto.setIdDocumento(6);

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            ConvocatoriaDoc ent = this.sessionFactory.getCurrentSession().get(ConvocatoriaDoc.class,
                    dto.getIdDocumento());
            this.sessionFactory.getCurrentSession().evict(ent);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

    }

    /**
     * guarda el documento propuesta de resolución definitiva
     *
     */
    @Test
    public void testGuardarDocPropuestaResolucionDef() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA));

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(35);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * si existe el documento acta de comisión, guarda el documento RESOLUCIÓN
     * DE CONCESIÓN
     *
     */
    @Test
    public void testGuardarDocResolucionConcesion() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA));

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(39);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento resolución concesion OK");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * si no existe el documento propuesta resolucion definitiva y se quiere
     * guardar documento RESOLUCIÓN DE CONCESIÓN lanza una exception
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorGuardarResolucionConcesionSinPropuestaResolucionDefinitiva()
            throws ServiceException {

        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(40);

        dto = this.service.guardar(dto);

    }

    /**
     * método que lanzará una exception si ya exite el documento de resolución
     * de concesión
     *
     * @throws ServiceException
     */

    @Test(expected = ServiceException.class)
    public void testErrorGuardarResolucionConcesionExisteRegistro() throws ServiceException {

        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA));
        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(41);
        dto.setCdFica(1000);
        dto.setDsNombreFichero("documento");
        dto.setCdUsuAlta("prueba");
        dto.setFcAltaFila(new Date());
        dto = this.service.guardar(dto);
    }

    /**
     * método para el error en guardado con la fecha de alegación antes.
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErroGuardarDocPropuestaResolucionDef() throws ServiceException {

        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(36);
        dto.setCdFica(1000);
        dto.setDsNombreFichero("documento");
        dto.setCdUsuAlta("prueba");
        dto.setFcAltaFila(new Date());
        dto = this.service.guardar(dto);

    }

    /**
     * error guardar propuesta resolución definitiva con expediente
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErroGuardarDocPropuestaResolucionDefConExpediente() throws ServiceException {

        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(37);
        dto.setCdFica(1000);
        dto.setDsNombreFichero("documento");
        dto.setCdUsuAlta("prueba");
        dto.setFcAltaFila(new Date());
        dto = this.service.guardar(dto);

    }

    /**
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testExisteDocPropuestaResolucionDef() throws ServiceException {

        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(34);
        dto.setCdFica(1000);
        dto.setDsNombreFichero("documento");
        dto.setCdUsuAlta("prueba");
        dto.setFcAltaFila(new Date());

        dto = this.service.guardar(dto);

    }

    /**
     * guarda el documento con una acta de comisión acta_comision_evaluacion y
     * cae en el exception ya que tiene ni tiene expedientes en estado
     * resolucion_definitiva
     *
     */
    @Test(expected = ServiceException.class)
    public void testGuardarDocumentoResolucionProvisional() throws ServiceException {

        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(30);
        dto.setCdFica(1000);
        dto.setDsNombreFichero("documento");
        dto.setCdUsuAlta("prueba");
        dto.setFcAltaFila(new Date());

        dto = this.service.guardar(dto);

    }

    /**
     * si guarda por primera vez el tipo documento ACTA_COMISION_EVALUACION
     * guarda y actualiza la fecha fcTraComisionEval de la convocatoria
     */
    @Test
    public void testGuardarDocumentoResolucionProvisional2() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION));

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(30);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testGuardarDocumentoResolucionProvisional3() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_COMISION_EVALUACION));

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(32);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    public void testGuardarDocumentoResolucionProvisional4() {
        try {

            // GUARDAR
            ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
            CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

            tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL));

            dto.setIdTipoDocumento(tipoDocDTO);
            dto.setIdConvocatoria(38);
            dto.setCdFica(1000);
            dto.setDsNombreFichero("documento");
            dto.setCdUsuAlta("prueba");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Error al guardar el documento con el estado expediente PROPUESTA DE
     * RESOLUCION (221)
     */
    @Test(expected = ServiceException.class)
    public void testGuardarDocConEspedientePropuestaResolucion() throws ServiceException {
        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(45);
        dto = this.service.guardar(dto);
    }

    /**
     * Error al descargar archivo de fica con codigo null
     *
     */
    @Test(expected = ServiceException.class)
    public void testErrorDescargarNull() throws ServiceException {
        this.service.descargar(null);
    }

    /**
     * error al Guardar el documento de propuesta de resolucion provisional
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testGuardarDocumentoResolucionProvisionalException() throws ServiceException {

        // GUARDAR
        ConvocatoriaDocDTO dto = new ConvocatoriaDocDTO();
        CatalogosValoresDTO tipoDocDTO = new CatalogosValoresDTO();

        tipoDocDTO.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL));

        dto.setIdTipoDocumento(tipoDocDTO);
        dto.setIdConvocatoria(31);
        dto = this.service.guardar(dto);

    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(ConvocatoriaDocDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdDocumento());
        Assert.assertNotNull(dto.getIdConvocatoria());
        Assert.assertNotNull(dto.getIdTipoDocumento());
        Assert.assertNotNull(dto.getCdFica());
        Assert.assertNotNull(dto.getDsNombreFichero());
        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getCdUsuBaja());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));
            Assert.assertNotNull(dto.getFcBajaFila());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));
            Assert.assertNotNull(dto.getCdUsuAlta());
            Assert.assertNotNull(dto.getFcAltaFila());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));
            Assert.assertNotNull(dto.getCdUsuModif());
            Assert.assertNotNull(dto.getFcModifFila());
            Assert.assertNotNull(dto.getDsNombreFichero());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsNombreFichero()));
        }
    }

}
