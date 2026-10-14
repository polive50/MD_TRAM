package org.madrid.saii.lib.services.adme;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.madrid.saii.lib.dto.NoteEnviarNotificacionDTO;
import org.madrid.saii.lib.dto.NoteObtenerNotificacionesDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.SolicitudService;
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
public class NoteAdmeServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService service;

    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

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
     * Test recuperarCdAbonado
     */
    @Test
    @Ignore
    public void testrecuperarCdAbonado() {
        try {

            String cdAbonado = this.service.recuperarCdAbonado("00000005M");

            Assert.assertTrue(StringUtils.isNoneBlank(cdAbonado));

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testEnviarNotificacion() {
        try {

            NoteEnviarNotificacionDTO inputNotifNoteDTO = new NoteEnviarNotificacionDTO();
            inputNotifNoteDTO.setUniAdmin("Unidad Administrativa");
            inputNotifNoteDTO.setAsunto(NoteAdmeService.ConstantesNote.ASUNTO_PRESENTACION);
            inputNotifNoteDTO.setCdNumIden("00000005M");

            inputNotifNoteDTO.setAdjunto(IOUtils.toInputStream("lalalalala"));
            inputNotifNoteDTO
            .setNombreAdjunto(NoteAdmeService.ConstantesNote.NOMBRE_ARCHIVO_NOTIFICACION);

            String cdNote = this.service.enviarNotificacion(inputNotifNoteDTO);

            Assert.assertTrue(StringUtils.isNoneBlank(cdNote));

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

    @Test
    @Ignore
    public void testObtenerNotificacion() {
        try {

            NoteObtenerNotificacionesDTO noteObtenerNotificacionesDTO = this.service
                    .obtenerNotificacion("165298");

            Assert.assertNotNull(noteObtenerNotificacionesDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
            fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
        }
    }

}
