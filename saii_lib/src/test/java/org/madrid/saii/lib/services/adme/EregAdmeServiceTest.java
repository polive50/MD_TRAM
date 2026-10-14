package org.madrid.saii.lib.services.adme;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.ImpresoService;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test NoteAdmeServiceTest
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class EregAdmeServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService service;

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
     * Test GenerarEntrada
     */
    @Test
    @Ignore
    public void testGenerarEntrada() {
        try {

            ExpedienteDTO dto = this.expedienteService.buscarId(1);

            dto = this.service.generarEntrada(dto, "FORMULARIO", "09DG038");

            Assert.assertNotNull(dto.getCdEreg());
            Assert.assertNotNull(dto.getDsDestinoEreg());

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test FirmarDatos
     */
    @Test
    @Ignore
    public void testFirmarDatos() {
        try {

            ImpresoDTO dto = this.impresoService.buscarId(4);

            byte[] fichero = this.impresoService.crearInforme(dto, null, null);

            byte[] respuesta = this.service.firmarDatos(fichero);

            Assert.assertNotNull(respuesta);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test obtenerPdfPlantilla
     */
    @Test
    @Ignore
    public void testObtenerPdfPlantilla() {
        try {

            byte[] obtenerPdfPlantilla = this.service.obtenerPdfPlantilla("61F1_20");

            Assert.assertNotNull(obtenerPdfPlantilla);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Test para descargar PDF del servicio de EREG
     *
     * @throws ServiceException
     */
    @Test
    @Ignore
    public void descargarPdfEreg() throws ServiceException {

        byte[] pdfEreg = null;
        FileOutputStream outputStream = null;

        pdfEreg = this.service.descargarAnexoPorCodigo("13210042349", "5656929");

        try {
            if (pdfEreg != null) {

                outputStream = new FileOutputStream(
                        "C:\\Producto\\EregPDF" + System.currentTimeMillis() + ".pdf");
                outputStream.write(pdfEreg);
            }
            {
                log.error(Constantes.Test.TEST_ERROR_PRUEBA);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

        catch (IOException e) {
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
        Assert.assertNotNull(pdfEreg);

    }

    @Test
    @Ignore
    public void errorDescargarPdfEreg() throws ServiceException {

        byte[] pdfEreg = null;
        FileOutputStream outputStream = null;

        pdfEreg = this.service.descargarAnexoPorCodigo("132100422249", "500656929");

        try {
            if (pdfEreg != null) {

                outputStream = new FileOutputStream(
                        "C:\\Producto\\ErrorEregPDF" + System.currentTimeMillis() + ".pdf");
                outputStream.write(pdfEreg);
            }
            {
                log.error(Constantes.Test.TEST_ERROR_PRUEBA);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }

        catch (IOException e) {
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
        Assert.assertNotNull(pdfEreg);

    }

}
