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
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.log4j.Log4j2;

/**
 * Test ImagenService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Log4j2
public class ImagenServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("imagenService")
    private ImagenService service;

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
    @Qualifier("serviceCatalogoValor")
    CatalogosValoresService serviceCatalogoValor;

    public static final String CONTENIDO_DEL_DOCUMENTO = "contenido del documento";

    public static final String USUARIO = "admin";

    @Test
    public void testGuardar() {
        try {

            // GUARDAR
            CatalogosValoresDTO catValor = new CatalogosValoresDTO();
            catValor.setIdValor(1233);
            ImagenDTO dto = new ImagenDTO();

            dto.setFondoEurDTO(catValor);
            dto.setCdUsuAlta("admin");
            dto.setFcAltaFila(new Date());

            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }
    }

    /**
     * Método de validación de subir con error imagen fica
     */
    @Test(expected = ServiceException.class)
    public void testSubirError() throws ServiceException {
        ImagenDTO dto = new ImagenDTO();
        dto.setCdFica(" ");

        this.service.subirImagen(dto);

    }

    /**
     * Método de validación de descargar con error imagen fica
     */
    @Test(expected = ServiceException.class)
    public void testDescargarError() throws ServiceException {
        ImagenDTO dto = new ImagenDTO();
        dto.setCdFica(" ");

        this.service.subirImagen(dto);

    }

    /**
     * Método de validación de subir con Contenido Fichero en documento fica
     */
    @Test(expected = ServiceException.class)
    public void testValidacionContenidoFicheroFica() throws ServiceException {
        ImagenDTO dto = new ImagenDTO();

        dto.setContenidoFichero(CONTENIDO_DEL_DOCUMENTO.getBytes());
        this.service.subirImagen(dto);
    }

    /**
     * Método para descargar documento Fica
     */
    @Test
    public void testDescargar() {
        try {

            String cdFica = "1";
            ImagenServiceImpl serviceImpl = new ImagenServiceImpl();

            FicaService ficaService = mock(FicaService.class);
            when(ficaService.recuperar(Integer.valueOf(cdFica))).thenReturn(new FicaFicheroImpl());

            serviceImpl.setFicaService(ficaService);
            ImagenDTO dto = serviceImpl.descargar(cdFica);

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

    /*
     * Método para subir imagen Fica
     */
    @Test
    public void testSubirImagenFica() {
        try {

            ImagenDTO dto = new ImagenDTO();

            byte[] contenido = CONTENIDO_DEL_DOCUMENTO.getBytes();
            dto.setContenidoFichero(contenido);
            FicaMetadatos ficaMetadatos = new FicaMetadatos();
            ficaMetadatos.setExtension("JPG");
            ficaMetadatos.setFechaFichero(new Date());
            ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
            ficaMetadatos.setNombreFichero("Captura.JPG");
            ficaMetadatos.setUsuario(USUARIO);

            dto.setCdFica(null);
            dto.setFicaMetadatos(ficaMetadatos);
            ImagenServiceImpl serviceImpl = new ImagenServiceImpl();
            FicaService ficaService = mock(FicaService.class);

            FicaFichero ficaFichero = new FicaFicheroImpl();

            when(ficaService.guardar(dto.getContenidoFichero(), dto.getFicaMetadatos()))
            .thenReturn(ficaFichero);

            serviceImpl.setFicaService(ficaService);

            dto = serviceImpl.subirImagen(dto);

            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Método para subir documento fica 1
     */
    @Test
    public void subirDocumentoFica2() {

        try {

            ImagenDTO dto = new ImagenDTO();

            byte[] contenido = CONTENIDO_DEL_DOCUMENTO.getBytes();
            dto.setContenidoFichero(contenido);
            FicaMetadatos ficaMetadatos = new FicaMetadatos();
            ficaMetadatos.setExtension("JPG");
            ficaMetadatos.setFechaFichero(new Date());
            ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
            ficaMetadatos.setNombreFichero("Captura.JPG");
            ficaMetadatos.setUsuario(USUARIO);
            dto.setCdFica("1");
            dto.setFicaMetadatos(ficaMetadatos);
            Integer cdFica = 1;

            ImagenServiceImpl serviceImpl = new ImagenServiceImpl();
            FicaService ficaService = mock(FicaService.class);
            when(ficaService.recuperar(cdFica)).thenReturn(new FicaFicheroImpl());
            FicaFichero ficaFichero = new FicaFicheroImpl();
            when(ficaService.actualizar(cdFica, contenido, ficaMetadatos)).thenReturn(ficaFichero);
            serviceImpl.setFicaService(ficaService);
            dto = serviceImpl.subirImagen(dto);
            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());

        }

    }

    /**
     * Test buscar por id
     */

    @Test
    public void testBuscarPorId() {
        try {
            ImagenDTO dto = this.service.buscarId(2);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdImagen());

            this.testAtributos(dto, Boolean.FALSE);
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * test buscargar
     */
    @Test
    public void testBuscar() {
        try {
            List<ImagenDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ImagenDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ImagenDTO dto = it.next();

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
    private void testAtributos(ImagenDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdImagen());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getFondoEurDTO());

            Assert.assertTrue(dto.getCdUnidad() != null);

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdFica()));
            Assert.assertNotNull(dto.getCdFica());
        }

    }

}
