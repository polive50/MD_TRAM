package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
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
 * Test ImpresoService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ImpresoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService service;

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
     * Prueba el método crearInforme
     *
     */
    @Test
    public void crearInforme() throws ServiceException {

        byte[] pdfCreado = null;

        ImpresoDTO impresoDTO = this.service.buscarId(8);

        if (impresoDTO != null) {
            pdfCreado = this.service.crearInforme(impresoDTO, null, null);
        }

        // Ponemos la ruta de salida del pdf a generar
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(
                    "C:\\Producto\\PruebaTest" + System.currentTimeMillis() + ".pdf");
            outputStream.write(pdfCreado);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

        // No es nulo
        Assert.assertNotNull(pdfCreado);

    }

    /**
     * Prueba el metodo guardar
     */

    @Test
    public void testGuardar() {
        try {

            // Instanciamos DTOs
            ImpresoDTO dto = new ImpresoDTO();
            CatalogosValoresDTO tipoDTO = new CatalogosValoresDTO();
            ConvocatoriaActDTO convocatoriaActDTO = new ConvocatoriaActDTO();

            // Poblacion de datos
            tipoDTO.setIdValor(1);
            convocatoriaActDTO.setIdActor(1);
            dto.setTipoDTO(tipoDTO);
            dto.setConvocatoriaActDTO(convocatoriaActDTO);
            dto.setIdConvocatoria(1);
            dto.setCdIdentificacion("123456");
            dto.setDsDescripcion("DEMO TEST IMPRESO");
            dto.setItGeneraImpreso("S");
            dto.setDsImpresoOriginal("Impreso_original.docx");
            dto.setDsPlantillaTecnica("Plantilla_tecnica.docx");

            /* Guardamos */
            dto = this.service.guardar(dto);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno
            this.testAtributos(dto, Boolean.TRUE);

            this.sessionFactory.getCurrentSession().clear();

            /* Modificamos */
            dto.setDsDescripcion("PRUEBA TEST MOD");
            dto = this.service.guardar(dto);
            /* La modificación es correcta */
            Assert.assertEquals("PRUEBA TEST MOD", dto.getDsDescripcion());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test Buscar Impreso
     */
    @Test
    public void testBuscar() {
        try {
            List<ImpresoDTO> lstDTO = this.service.buscar(0, -1, null, null);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ImpresoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ImpresoDTO dto = it.next();

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
     * Prueba el método obtener
     */
    @Test
    public void testBuscarPorConvTipoActor() {
        try {
            ImpresoDTO dto = this.service.buscarPorConvActor(1, 1, null);
            // No es nulo
            Assert.assertNotNull(dto);

            this.testAtributos(dto, Boolean.TRUE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener
     */
    @Test
    public void testBuscarPorConvTipoActorError() {
        try {
            ImpresoDTO dto = this.service.buscarPorConvActor(99, 905, null);

            // No es nulo
            Assert.assertNotNull(dto);

            // Es nulo
            Assert.assertNull(dto.getIdImpreso());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test Buscar por ID
     */
    @Test
    public void testBuscarPorId() {
        try {
            ImpresoDTO dto = this.service.buscarId(1);

            // No es nulo
            Assert.assertNotNull(dto);

            // Está relleno el id
            Assert.assertNotNull(dto.getIdImpreso());

            this.testAtributos(dto, Boolean.FALSE);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test descargar
     */
    // @Test
    public void testDescargar() {
        try {
            ImpresoDTO dto = this.service.descargar(668261);
            // No es nulo
            Assert.assertNotNull(dto);
            Assert.assertNotNull(dto.getFicaFichero());
            Assert.assertNotNull(dto.getContenidoFichero());
            Assert.assertNotNull(dto.getCdFica());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test subir documento fica
     */
    // @Test
    public void testSubirDocumentoFica() {
        try {
            ImpresoDTO dto = this.service.buscarId(1);

            // Datos del fichero impreso original
            String strImpOriginal = "Subir documento FICA impreso original";
            byte[] fileInputStreamImpOriginal = strImpOriginal.getBytes();
            dto.setContenidoFicheroImpOriginal(fileInputStreamImpOriginal);

            dto.setFicaMetadatosImpOriginal(new FicaMetadatos());
            // Nombre, fecha y extension del fichero
            dto.getFicaMetadatosImpOriginal().setNombreFichero("prueba_impreso_original");
            dto.getFicaMetadatosImpOriginal().setFechaFichero(new Date());
            dto.getFicaMetadatosImpOriginal().setExtension(".txt");
            dto.getFicaMetadatosImpOriginal().setUsuario("SAII");
            dto.getFicaMetadatosImpOriginal().setEncriptar(false);

            ImpresoDTO dtoImpOriginal = this.service.subirDocumentoFica(dto, 1);

            // No es nulo
            Assert.assertNotNull(dtoImpOriginal);
            Assert.assertNotNull(dtoImpOriginal.getFicaFicheroImpOriginal());
            Assert.assertNotNull(dtoImpOriginal.getCdFicaImpreso());
            Assert.assertNotNull(dtoImpOriginal.getDsImpresoOriginal());

            // Datos del fichero plantilla tecnica
            String strPlanTecnica = "Subir documento FICA";
            byte[] fileInputStreamPlanTecnica = strPlanTecnica.getBytes();
            dto.setContenidoFicheroPlanTecnica(fileInputStreamPlanTecnica);

            dto.setFicaMetadatosPlanTecnica(new FicaMetadatos());
            // Nombre, fecha y extension del fichero
            dto.getFicaMetadatosPlanTecnica().setNombreFichero("prueba_plantilla_tecnica");
            dto.getFicaMetadatosPlanTecnica().setFechaFichero(new Date());
            dto.getFicaMetadatosPlanTecnica().setExtension(".txt");
            dto.getFicaMetadatosPlanTecnica().setUsuario("SAII");
            dto.getFicaMetadatosPlanTecnica().setEncriptar(false);

            ImpresoDTO dtoPlanTecnica = this.service.subirDocumentoFica(dto, 2);

            // No es nulo
            Assert.assertNotNull(dtoPlanTecnica);
            Assert.assertNotNull(dtoPlanTecnica.getFicaFicheroImpOriginal());
            Assert.assertNotNull(dtoPlanTecnica.getCdFicaImpreso());
            Assert.assertNotNull(dtoPlanTecnica.getDsImpresoOriginal());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test buscar por id convocatoria
     */
    @Test
    public void testbuscarPorIdConvocatoria() {
        try {
            List<ImpresoDTO> lstDTO = this.service.buscarPorIdConvocatoria(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<ImpresoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                ImpresoDTO dto = it.next();

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
    private void testAtributos(ImpresoDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdImpreso());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertNotNull(dto.getIdConvocatoria());
            Assert.assertNotNull(dto.getTipoDTO().getIdValor());
            Assert.assertNotNull(dto.getConvocatoriaActDTO().getIdActor());
            Assert.assertTrue(StringUtils.isNotBlank(dto.getItGeneraImpreso()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdIdentificacion()));
            Assert.assertTrue(StringUtils.isNotBlank(dto.getDsDescripcion()));
        }

        // Null Safe
        Assert.assertNotNull(dto.getTipoDTO().getIdValor());
        Assert.assertNotNull(dto.getConvocatoriaActDTO().getIdActor());
        Assert.assertNotNull(dto.getItGeneraImpreso());
        Assert.assertNotNull(dto.getDsImpresoOriginal());
        Assert.assertNotNull(dto.getDsPlantillaTecnica());
    }

}
