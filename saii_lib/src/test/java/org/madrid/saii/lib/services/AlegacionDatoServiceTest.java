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
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.AlegacionDatoDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.AlegacionDato;
import org.madrid.saii.lib.dto.AlegacionDTO;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionDatoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.log4j.Log4j2;

/**
 * Test AlegacionDatoService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Log4j2
public class AlegacionDatoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("alegacionDatoService")
    private AlegacionDatoService service;

    @Autowired
    @Qualifier("alegacionDatoDAO")
    private AlegacionDatoDAO alegDatoDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

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
    public void testGuardarDatosAlegacion() {
        try {

            // GUARDAR
            EvaluacionDatoDTO evalDTO = new EvaluacionDatoDTO();
            evalDTO.setIdExpediente(1);
            evalDTO.setIdentificador("26083209J");

            AlegacionDTO alegDTO5 = new AlegacionDTO();
            alegDTO5.setIdAlegacion(5);

            AlegacionDTO alegDTO2 = new AlegacionDTO();
            alegDTO2.setIdAlegacion(2);

            List<AlegacionDatoDTO> lstDTO = new ArrayList<>();
            AlegacionDatoDTO adDTO1 = new AlegacionDatoDTO();
            adDTO1.setIdExpediente(evalDTO.getIdExpediente());
            adDTO1.setIdAlegacion(5);
            adDTO1.setDsValor("PRUEBA");
            adDTO1.setCdUsuAlta(Constantes.Test.TEST_ADMIN);

            AlegacionDatoDTO adDTO2 = new AlegacionDatoDTO();
            adDTO1.setIdExpediente(evalDTO.getIdExpediente());
            adDTO2.setIdAlegacion(2);
            adDTO2.setDsValor("PRUEBA");
            adDTO2.setCdUsuAlta(Constantes.Test.TEST_ADMIN);
            lstDTO.add(adDTO1);
            lstDTO.add(adDTO2);
            evalDTO.setLstAlegacionDatoDTO(lstDTO);

            FicaMetadatos ficaMetadatos = new FicaMetadatos();
            ficaMetadatos.setNombreFichero(Constantes.TipoCoorEval.INFORME_ALEGACION);
            ficaMetadatos.setFechaFichero(new Date());
            ficaMetadatos.setExtension(Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION);
            ficaMetadatos.setUsuario(evalDTO.getIdentificador());
            ficaMetadatos.setEncriptar(false);
            ficaMetadatos.setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

            byte[] documentoSalida = "contenido del documento".getBytes();
            FicaFichero ficaFichero = new FicaFicheroImpl();
            FicaService ficaService = Mockito.mock(FicaService.class);
            PdfService pdfService = Mockito.mock(PdfService.class);
            AlegacionService alegacionService = Mockito.mock(AlegacionService.class);
            Mockito.when(alegacionService.buscarId(adDTO1.getIdAlegacion())).thenReturn(alegDTO5);
            Mockito.when(alegacionService.buscarId(adDTO2.getIdAlegacion())).thenReturn(alegDTO2);
            Mockito.when(
                    pdfService.crearFormularioAlegacionDato(evalDTO.getIdExpediente(), "SAIIVUSU1"))
                    .thenReturn(documentoSalida);
            Mockito.when(ficaService.guardar(documentoSalida, ficaMetadatos))
                    .thenReturn(ficaFichero);

            AlegacionDatoServiceImpl datoServiceImpl = new AlegacionDatoServiceImpl();
            datoServiceImpl.setAlegacionService(alegacionService);
            datoServiceImpl.setFicaService(ficaService);
            datoServiceImpl.setDao(this.alegDatoDAO);
            datoServiceImpl.setPdfService(pdfService);
            datoServiceImpl.setExpedienteDAO(this.expedienteDAO);
            lstDTO = datoServiceImpl.guardarDatosAlegacion(evalDTO, "batch");

            // No es nulo
            Assert.assertNotNull(lstDTO);
            Assert.assertFalse(lstDTO.isEmpty());

            lstDTO.forEach(dto -> {
                // No es nulo
                Assert.assertNotNull(dto);

                // Está relleno
                this.testAtributos(dto, Boolean.FALSE);

                AlegacionDato ent = this.sessionFactory.getCurrentSession().get(AlegacionDato.class,
                        dto.getIdAlegacionDato());
                this.sessionFactory.getCurrentSession().evict(ent);
            });

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * Prueba el método obtener
     */
    @Test
    public void testObtenerPorExpediente() {
        try {
            List<AlegacionDatoDTO> lstDTO = this.service.obtenerPorExpediente(1);

            // No es nulo
            Assert.assertNotNull(lstDTO);

            // Está relleno
            Assert.assertFalse(lstDTO.isEmpty());

            // Comprobamos los atributos obligatorios
            // El DTO tiene que ser null safe.
            Iterator<AlegacionDatoDTO> it = lstDTO.iterator();
            while (it.hasNext()) {
                AlegacionDatoDTO dto = it.next();

                this.testAtributos(dto, Boolean.FALSE);
            }

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    /**
     * método para capturar la excepción de error en el método aprobarEvaluacion
     *
     * @throws ServiceException
     */
    @Test(expected = ServiceException.class)
    public void testErrorObtenerPorExpediente() throws ServiceException {
        this.service.obtenerPorExpediente(null);
    }

    /**
     * Comprueba los atributos
     *
     * @param dto
     * @parama relleno
     */
    private void testAtributos(AlegacionDatoDTO dto, Boolean bRelleno) {

        log.debug(dto.toString());

        // Obligatorios
        Assert.assertNotNull(dto.getIdAlegacionDato());

        Assert.assertNotNull(dto.getIdExpediente());

        Assert.assertNotNull(dto.getAlegacionDTO());

        Assert.assertTrue(StringUtils.isNotBlank(dto.getDsValor()));

        Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

        Assert.assertNotNull(dto.getFcAltaFila());

        if (Boolean.TRUE.equals(bRelleno)) {
            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));

            Assert.assertNotNull(dto.getFcModifFila());

            Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));

            Assert.assertNotNull(dto.getFcBajaFila());
        }

    }

}
