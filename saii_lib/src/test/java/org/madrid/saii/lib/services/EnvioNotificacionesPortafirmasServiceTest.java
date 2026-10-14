package org.madrid.saii.lib.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;

import java.io.UnsupportedEncodingException;

import java.time.LocalDate;
import java.time.ZoneOffset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madrid.core.fica.FicaFicheroImpl;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.core.fica.s3.FicaMetadatosUsuario;
import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.EnvioNotificacionesPortafirmasDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.NivelOrgDTO;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.dto.UsuarioDTO;
import org.madrid.saii.lib.dto.UsuarioUnidadAdminDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("SinBaseDatos")
@RunWith(MockitoJUnitRunner.class)
public class EnvioNotificacionesPortafirmasServiceTest {
    @Mock
    private BloqueComponenteService bloqueComponenteService;

    @Mock
    private BloqueDAO bloqueDAO;

    @Mock
    private CataService cataService;

    @Mock
    private CatalogosValoresService catalogosValoresService;

    @Mock
    private ConvocatoriaActService convocatoriaActService;

    @Mock
    private ConvocatoriaDAO convocatoriaDAO;

    @Mock
    private ConvocatoriaDocService convocatoriaDocService;

    @Mock
    private ConvocatoriaPlaService convocatoriaPlaService;

    @Mock
    private ConvocatoriaReqService convocatoriaReqService;

    @Mock
    private ConvocatoriaService convocatoriaService;

    @Mock
    private ConvocatoriaSubmodService convocatoriaSubmodService;

    @Mock
    private DatoService datoService;

    @Mock
    private DocumentoPtfrDAO documentoPtfrDAO;

    @Mock
    private EntidadService entidadService;

    @Mock
    private EnvioNotificacionesPortafirmasDAO envioNotificacionesPortafirmasDAO;

    @InjectMocks
    private EnvioNotificacionesPortafirmasServiceImpl envioNotificacionesPortafirmasServiceImpl;

    @Mock
    private EregAdmeService eregAdmeService;

    @Mock
    private ExpedienteDocDAO expedienteDocDAO;

    @Mock
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    @Mock
    private ExpedienteDocService expedienteDocService;

    @Mock
    private ExpedienteEvalDAO expedienteEvalDAO;

    @Mock
    private ExpedienteHistDAO expedienteHistDAO;

    @Mock
    private ExpedienteJusTecDAO expedienteJusTecDAO;

    @Mock
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    @Mock
    private ExpedienteJusTecService expedienteJusTecService;

    @Mock
    private ExpedienteReqDAO expedienteReqDAO;

    @Mock
    private ExpedienteService expedienteService;

    @Mock
    private FicaService ficaService;

    @Mock
    private ImpresoService impresoService;

    @Mock
    private InteresadoDAO interesadoDAO;

    @Mock
    private InteresadoService interesadoService;

    @Mock
    private NoteAdmeService noteAdmeService;

    @Mock
    private PdfService pdfService;

    @Mock
    private PtfrAdmeService ptfrAdmeService;

    @Mock
    private RepresentanteJurService representanteJurService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private SolicitudDAO solicitudDAO;

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private SucaMunicipioService sucaMunicipioService;

    @Mock
    private SucaProvinciaService sucaProvinciaService;
    
    @InjectMocks
    private DocumentoPtfrServiceImpl documentoPtfrServiceimpl;

    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#guardarEnvio(ConvocatoriaDTO, ExpedienteDTO, Integer, Integer, byte[])}
     */
    @Test
    public void testGuardarEnvio() throws ServiceException, UnsupportedEncodingException {
        NivelOrgDTO nivelOrgDTO = new NivelOrgDTO();
        nivelOrgDTO.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO.setIdNivelOrg(1);
        nivelOrgDTO.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO unidadAdmin = new UnidadAdminDTO();
        unidadAdmin.setCdUnidad("Cd Unidad");
        unidadAdmin.setCdUnidadPadre("Cd Unidad Padre");
        unidadAdmin.setCdUsuAlta("Cd Usu Alta");
        unidadAdmin.setCdUsuBaja("Cd Usu Baja");
        unidadAdmin.setCdUsuModif("Cd Usu Modif");
        unidadAdmin.setDsUnidad("Ds Unidad");
        unidadAdmin.setDsUnidadPadre("Ds Unidad Padre");
        unidadAdmin.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin.setIdUnidadAdmin(1);
        unidadAdmin.setNivelOrgDTO(nivelOrgDTO);

        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setCdUnidad("Cd Unidad");
        usuario.setCdUsuAlta("Cd Usu Alta");
        usuario.setCdUsuBaja("Cd Usu Baja");
        usuario.setCdUsuModif("Cd Usu Modif");
        usuario.setCdUsuario("Cd Usuario");
        usuario.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario.setIdUsuario(1);

        UsuarioUnidadAdminDTO gestorConcesion = new UsuarioUnidadAdminDTO();
        gestorConcesion.setCdUsuAlta("Cd Usu Alta");
        gestorConcesion.setCdUsuBaja("Cd Usu Baja");
        gestorConcesion.setCdUsuModif("Cd Usu Modif");
        gestorConcesion.setDsUsuario("Ds Usuario");
        gestorConcesion
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorConcesion
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorConcesion
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorConcesion.setIdUsuarioUnidadAdmin(1);
        gestorConcesion.setUnidadAdmin(unidadAdmin);
        gestorConcesion.setUsuario(usuario);

        NivelOrgDTO nivelOrgDTO2 = new NivelOrgDTO();
        nivelOrgDTO2.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO2.setIdNivelOrg(1);
        nivelOrgDTO2.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO unidadAdmin2 = new UnidadAdminDTO();
        unidadAdmin2.setCdUnidad("Cd Unidad");
        unidadAdmin2.setCdUnidadPadre("Cd Unidad Padre");
        unidadAdmin2.setCdUsuAlta("Cd Usu Alta");
        unidadAdmin2.setCdUsuBaja("Cd Usu Baja");
        unidadAdmin2.setCdUsuModif("Cd Usu Modif");
        unidadAdmin2.setDsUnidad("Ds Unidad");
        unidadAdmin2.setDsUnidadPadre("Ds Unidad Padre");
        unidadAdmin2.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin2.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin2
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdmin2.setIdUnidadAdmin(1);
        unidadAdmin2.setNivelOrgDTO(nivelOrgDTO2);

        UsuarioDTO usuario2 = new UsuarioDTO();
        usuario2.setCdUnidad("Cd Unidad");
        usuario2.setCdUsuAlta("Cd Usu Alta");
        usuario2.setCdUsuBaja("Cd Usu Baja");
        usuario2.setCdUsuModif("Cd Usu Modif");
        usuario2.setCdUsuario("Cd Usuario");
        usuario2.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario2.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario2.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        usuario2.setIdUsuario(1);

        UsuarioUnidadAdminDTO gestorJCT = new UsuarioUnidadAdminDTO();
        gestorJCT.setCdUsuAlta("Cd Usu Alta");
        gestorJCT.setCdUsuBaja("Cd Usu Baja");
        gestorJCT.setCdUsuModif("Cd Usu Modif");
        gestorJCT.setDsUsuario("Ds Usuario");
        gestorJCT.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorJCT.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorJCT.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        gestorJCT.setIdUsuarioUnidadAdmin(1);
        gestorJCT.setUnidadAdmin(unidadAdmin2);
        gestorJCT.setUsuario(usuario2);

        NivelOrgDTO nivelOrgDTO3 = new NivelOrgDTO();
        nivelOrgDTO3.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO3.setIdNivelOrg(1);
        nivelOrgDTO3.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO idAreaAdministrativa = new UnidadAdminDTO();
        idAreaAdministrativa.setCdUnidad("Cd Unidad");
        idAreaAdministrativa.setCdUnidadPadre("Cd Unidad Padre");
        idAreaAdministrativa.setCdUsuAlta("Cd Usu Alta");
        idAreaAdministrativa.setCdUsuBaja("Cd Usu Baja");
        idAreaAdministrativa.setCdUsuModif("Cd Usu Modif");
        idAreaAdministrativa.setDsUnidad("Ds Unidad");
        idAreaAdministrativa.setDsUnidadPadre("Ds Unidad Padre");
        idAreaAdministrativa
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativa
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativa
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativa.setIdUnidadAdmin(1);
        idAreaAdministrativa.setNivelOrgDTO(nivelOrgDTO3);

        NivelOrgDTO nivelOrgDTO4 = new NivelOrgDTO();
        nivelOrgDTO4.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO4.setIdNivelOrg(1);
        nivelOrgDTO4.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO idAreaAdministrativaJCT = new UnidadAdminDTO();
        idAreaAdministrativaJCT.setCdUnidad("Cd Unidad");
        idAreaAdministrativaJCT.setCdUnidadPadre("Cd Unidad Padre");
        idAreaAdministrativaJCT.setCdUsuAlta("Cd Usu Alta");
        idAreaAdministrativaJCT.setCdUsuBaja("Cd Usu Baja");
        idAreaAdministrativaJCT.setCdUsuModif("Cd Usu Modif");
        idAreaAdministrativaJCT.setDsUnidad("Ds Unidad");
        idAreaAdministrativaJCT.setDsUnidadPadre("Ds Unidad Padre");
        idAreaAdministrativaJCT
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativaJCT
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativaJCT
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idAreaAdministrativaJCT.setIdUnidadAdmin(1);
        idAreaAdministrativaJCT.setNivelOrgDTO(nivelOrgDTO4);

        NivelOrgDTO nivelOrgDTO5 = new NivelOrgDTO();
        nivelOrgDTO5.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO5.setIdNivelOrg(1);
        nivelOrgDTO5.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO idDireccion = new UnidadAdminDTO();
        idDireccion.setCdUnidad("Cd Unidad");
        idDireccion.setCdUnidadPadre("Cd Unidad Padre");
        idDireccion.setCdUsuAlta("Cd Usu Alta");
        idDireccion.setCdUsuBaja("Cd Usu Baja");
        idDireccion.setCdUsuModif("Cd Usu Modif");
        idDireccion.setDsUnidad("Ds Unidad");
        idDireccion.setDsUnidadPadre("Ds Unidad Padre");
        idDireccion.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idDireccion.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idDireccion.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idDireccion.setIdUnidadAdmin(1);
        idDireccion.setNivelOrgDTO(nivelOrgDTO5);

        NivelOrgDTO nivelOrgDTO6 = new NivelOrgDTO();
        nivelOrgDTO6.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO6.setIdNivelOrg(1);
        nivelOrgDTO6.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO idSubdireccion = new UnidadAdminDTO();
        idSubdireccion.setCdUnidad("Cd Unidad");
        idSubdireccion.setCdUnidadPadre("Cd Unidad Padre");
        idSubdireccion.setCdUsuAlta("Cd Usu Alta");
        idSubdireccion.setCdUsuBaja("Cd Usu Baja");
        idSubdireccion.setCdUsuModif("Cd Usu Modif");
        idSubdireccion.setDsUnidad("Ds Unidad");
        idSubdireccion.setDsUnidadPadre("Ds Unidad Padre");
        idSubdireccion
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idSubdireccion
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idSubdireccion
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        idSubdireccion.setIdUnidadAdmin(1);
        idSubdireccion.setNivelOrgDTO(nivelOrgDTO6);

        NivelOrgDTO nivelOrgDTO7 = new NivelOrgDTO();
        nivelOrgDTO7.setDsNivelOrg("Ds Nivel Org");
        nivelOrgDTO7.setIdNivelOrg(1);
        nivelOrgDTO7.setListUnidadAdmin(new ArrayList<>());

        UnidadAdminDTO unidadAdminJCTSel = new UnidadAdminDTO();
        unidadAdminJCTSel.setCdUnidad("Cd Unidad");
        unidadAdminJCTSel.setCdUnidadPadre("Cd Unidad Padre");
        unidadAdminJCTSel.setCdUsuAlta("Cd Usu Alta");
        unidadAdminJCTSel.setCdUsuBaja("Cd Usu Baja");
        unidadAdminJCTSel.setCdUsuModif("Cd Usu Modif");
        unidadAdminJCTSel.setDsUnidad("Ds Unidad");
        unidadAdminJCTSel.setDsUnidadPadre("Ds Unidad Padre");
        unidadAdminJCTSel
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdminJCTSel
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdminJCTSel
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        unidadAdminJCTSel.setIdUnidadAdmin(1);
        unidadAdminJCTSel.setNivelOrgDTO(nivelOrgDTO7);

        ConvocatoriaDTO convocatoria = new ConvocatoriaDTO();
        convocatoria.setAlertaDocumentos(1);
        convocatoria.setAlertaRequisitos(1);
        convocatoria.setAlertaTramitesPendiente(3);
        convocatoria.setBlPresFueraPlazo(true);
        convocatoria.setCdAcronimo("Cd Acronimo");
        convocatoria.setCdTitulo("Cd Titulo");
        convocatoria.setCdUsuAlta("Cd Usu Alta");
        convocatoria.setCdUsuBaja("Cd Usu Baja");
        convocatoria.setCdUsuModif("Cd Usu Modif");
        convocatoria.setDsDescripcion("Ds Descripcion");
        convocatoria.setDsDescripcionCorta("Ds Descripcion Corta");
        convocatoria.setEsConvocatoriaActas(true);
        convocatoria.setEsConvocatoriaContrato(true);
        convocatoria.setEsPermiteAsociarCoordinador(true);
        convocatoria.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcFinAlegaciones(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcFinContrato(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcFinPresentacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcFinTramiEval(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria.setFcFinTramiEvalFormat("Fc Fin Trami Eval Format");
        convocatoria
                .setFcIniAlegaciones(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcIniContrato(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcIniPresentacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcIniTramiEval(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria.setFcIniTramiEvalFormat("Fc Ini Trami Eval Format");
        convocatoria
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcTraComisionEval(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcTraPropResDef(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
                .setFcTraResolucionDef(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        //VMM 18/03/25 MD-1338: añadimos fechas tramite de resoluc provisional y propuesta
        convocatoria
            .setFcTraPropResProv(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria
            .setFcTraResolucionProv(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoria.setGestorConcesion(gestorConcesion);
        convocatoria.setGestorJCT(gestorJCT);
        convocatoria.setIdAreaAdministrativa(idAreaAdministrativa);
        convocatoria.setIdAreaAdministrativaJCT(idAreaAdministrativaJCT);
        convocatoria.setIdConvocatoria(1);
        convocatoria.setIdDireccion(idDireccion);
        convocatoria.setIdEstado(new CatalogosValoresDTO());
        convocatoria.setIdLineaAyuda(new CatalogosValoresDTO());
        convocatoria.setIdModalidadConvocatoria(new CatalogosValoresDTO());
        convocatoria.setIdSubdireccion(idSubdireccion);
        convocatoria.setIdTipoModalidad(new CatalogosValoresDTO());
        convocatoria.setIsMenorFechaIniAle(true);
        convocatoria.setIsMenorFechaIniEva(true);
        convocatoria.setIsValFechaEval(true);
        convocatoria.setItAbiertaPermanente("It Abierta Permanente");
        convocatoria.setItActasComitesGestion("It Actas Comites Gestion");
        convocatoria.setItAgotarFondos("It Agotar Fondos");
        convocatoria.setItContratos("It Contratos");
        convocatoria.setItConvocatoriaActiva("It Convocatoria Activa");
        convocatoria.setItConvocatoriaCerrada("It Convocatoria Cerrada");
        convocatoria.setItFondosEuropa("It Fondos Europa");
        convocatoria.setItInicioSolOrg("It Inicio Sol Org");
        convocatoria.setItPartidasPresupuestarias("It Partidas Presupuestarias");
        convocatoria.setItPresFueraPlazo("It Pres Fuera Plazo");
        convocatoria.setItRequisitos("It Requisitos");
        convocatoria.setItfondosAgotados("Itfondos Agotados");
        convocatoria.setListConvocatoriaAct(new ArrayList<>());
        convocatoria.setListConvocatoriaArea(new ArrayList<>());
        convocatoria.setListConvocatoriaDoc(new ArrayList<>());
        convocatoria.setListConvocatoriaEnl(new ArrayList<>());
        convocatoria.setListConvocatoriaImp(new ArrayList<>());
        convocatoria.setListConvocatoriaSubmod(new ArrayList<>());
        convocatoria.setNmAno(1);
        convocatoria.setNmCalificacionMax(1);
        convocatoria.setNmDiasSubsanacion(1);
        convocatoria.setNmDurMaxAyuda(1);
        convocatoria.setNmMaxPeriodos(1);
        convocatoria.setNmImportePresAnualidad(10.0d);
        convocatoria.setNmImportePresTotal(10.0d);
        convocatoria.setTipoActorSolicitanteDTO(new CatalogosValoresDTO());
        convocatoria.setUnidadAdminJCTSel(unidadAdminJCTSel);

        ConvocatoriaActDTO convocatoriaActDTO = new ConvocatoriaActDTO();
        convocatoriaActDTO.setBolGeneraExpediente(true);
        convocatoriaActDTO.setCdUsuAlta("Cd Usu Alta");
        convocatoriaActDTO.setCdUsuBaja("Cd Usu Baja");
        convocatoriaActDTO.setCdUsuModif("Cd Usu Modif");
        convocatoriaActDTO.setEsActorCoordinador(true);
        convocatoriaActDTO.setEsActorParticipante(true);
        convocatoriaActDTO.setEsActorSolicitante(true);
        convocatoriaActDTO
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO.setIdActor(1);
        convocatoriaActDTO.setIdActorCoordinador(1);
        convocatoriaActDTO.setIdActorSolicitante(1);
        convocatoriaActDTO.setIdConvocatoria(1);
        convocatoriaActDTO.setIdentificacionDTO(new CatalogosValoresDTO());
        convocatoriaActDTO.setItContrato(true);
        convocatoriaActDTO.setItGeneraExpediente("It Genera Expediente");
        convocatoriaActDTO.setItTipoActorPadre("It Tipo Actor Padre");
        convocatoriaActDTO.setNmMax(1);
        convocatoriaActDTO.setNmMin(1);
        convocatoriaActDTO.setTipoActorDTO(new CatalogosValoresDTO());
        convocatoriaActDTO.setTipoPersonaFisJurDTO(new CatalogosValoresDTO());
        convocatoriaActDTO.setTlDescripcion("Tl Descripcion");
        convocatoriaActDTO.setTlDescripcionCorta("Tl Descripcion Corta");

        ConvocatoriaActDTO convocatoriaActDTO2 = new ConvocatoriaActDTO();
        convocatoriaActDTO2.setBolGeneraExpediente(true);
        convocatoriaActDTO2.setCdUsuAlta("Cd Usu Alta");
        convocatoriaActDTO2.setCdUsuBaja("Cd Usu Baja");
        convocatoriaActDTO2.setCdUsuModif("Cd Usu Modif");
        convocatoriaActDTO2.setEsActorCoordinador(true);
        convocatoriaActDTO2.setEsActorParticipante(true);
        convocatoriaActDTO2.setEsActorSolicitante(true);
        convocatoriaActDTO2
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO2
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO2
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO2.setIdActor(1);
        convocatoriaActDTO2.setIdActorCoordinador(1);
        convocatoriaActDTO2.setIdActorSolicitante(1);
        convocatoriaActDTO2.setIdConvocatoria(1);
        convocatoriaActDTO2.setIdentificacionDTO(new CatalogosValoresDTO());
        convocatoriaActDTO2.setItContrato(true);
        convocatoriaActDTO2.setItGeneraExpediente("It Genera Expediente");
        convocatoriaActDTO2.setItTipoActorPadre("It Tipo Actor Padre");
        convocatoriaActDTO2.setNmMax(1);
        convocatoriaActDTO2.setNmMin(1);
        convocatoriaActDTO2.setTipoActorDTO(new CatalogosValoresDTO());
        convocatoriaActDTO2.setTipoPersonaFisJurDTO(new CatalogosValoresDTO());
        convocatoriaActDTO2.setTlDescripcion("Tl Descripcion");
        convocatoriaActDTO2.setTlDescripcionCorta("Tl Descripcion Corta");

        FicaMetadatos ficaMetadatos = new FicaMetadatos();
        ficaMetadatos.setCarpeta("Bella");
        ficaMetadatos.setEncriptar(true);
        ficaMetadatos.setExtension("Extension");
        ficaMetadatos
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatos.setIdExterno("Id Externo");
        ficaMetadatos.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatos.setNmDiasBorrado(1);
        ficaMetadatos.setNombreFichero("Nombre Fichero");
        ficaMetadatos.setS3Key("S3 Key");
        ficaMetadatos.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatos.setTituloFichero("Titulo Fichero");
        ficaMetadatos.setUsuAlta("Usu Alta");
        ficaMetadatos.setUsuModif("Usu Modif");
        ficaMetadatos.setUsuario("Usuario");

        FicaMetadatos ficaMetadatosImpOriginal = new FicaMetadatos();
        ficaMetadatosImpOriginal.setCarpeta("Bella");
        ficaMetadatosImpOriginal.setEncriptar(true);
        ficaMetadatosImpOriginal.setExtension("Extension");
        ficaMetadatosImpOriginal
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatosImpOriginal.setIdExterno("Id Externo");
        ficaMetadatosImpOriginal.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatosImpOriginal.setNmDiasBorrado(1);
        ficaMetadatosImpOriginal.setNombreFichero("Nombre Fichero");
        ficaMetadatosImpOriginal.setS3Key("S3 Key");
        ficaMetadatosImpOriginal.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatosImpOriginal.setTituloFichero("Titulo Fichero");
        ficaMetadatosImpOriginal.setUsuAlta("Usu Alta");
        ficaMetadatosImpOriginal.setUsuModif("Usu Modif");
        ficaMetadatosImpOriginal.setUsuario("Usuario");

        FicaMetadatos ficaMetadatosPlanTecnica = new FicaMetadatos();
        ficaMetadatosPlanTecnica.setCarpeta("Bella");
        ficaMetadatosPlanTecnica.setEncriptar(true);
        ficaMetadatosPlanTecnica.setExtension("Extension");
        ficaMetadatosPlanTecnica
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatosPlanTecnica.setIdExterno("Id Externo");
        ficaMetadatosPlanTecnica.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatosPlanTecnica.setNmDiasBorrado(1);
        ficaMetadatosPlanTecnica.setNombreFichero("Nombre Fichero");
        ficaMetadatosPlanTecnica.setS3Key("S3 Key");
        ficaMetadatosPlanTecnica.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatosPlanTecnica.setTituloFichero("Titulo Fichero");
        ficaMetadatosPlanTecnica.setUsuAlta("Usu Alta");
        ficaMetadatosPlanTecnica.setUsuModif("Usu Modif");
        ficaMetadatosPlanTecnica.setUsuario("Usuario");

        ImpresoDTO impresoDTO = new ImpresoDTO();
        impresoDTO.setCdFica(1);
        impresoDTO.setCdFicaImpreso(1);
        impresoDTO.setCdFicaPlantilla(1);
        impresoDTO.setCdIdentificacion("Cd Identificacion");
        impresoDTO.setCdUsuAlta("Cd Usu Alta");
        impresoDTO.setCdUsuBaja("Cd Usu Baja");
        impresoDTO.setCdUsuModif("Cd Usu Modif");
        impresoDTO.setContenidoFichero("AXAXAXAX".getBytes("UTF-8"));
        impresoDTO.setContenidoFicheroImpOriginal("AXAXAXAX".getBytes("UTF-8"));
        impresoDTO.setContenidoFicheroPlanTecnica("AXAXAXAX".getBytes("UTF-8"));
        impresoDTO.setConvocatoriaActDTO(convocatoriaActDTO2);
        impresoDTO.setDsDescripcion("Ds Descripcion");
        impresoDTO.setDsImpresoOriginal("Ds Impreso Original");
        impresoDTO.setDsPlantillaTecnica("Ds Plantilla Tecnica");
        impresoDTO.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoDTO.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoDTO.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoDTO.setFicaFichero(new FicaFicheroImpl());
        impresoDTO.setFicaFicheroImpOriginal(new FicaFicheroImpl());
        impresoDTO.setFicaFicheroPlantTecnica(new FicaFicheroImpl());
        impresoDTO.setFicaMetadatos(ficaMetadatos);
        impresoDTO.setFicaMetadatosImpOriginal(ficaMetadatosImpOriginal);
        impresoDTO.setFicaMetadatosPlanTecnica(ficaMetadatosPlanTecnica);
        impresoDTO.setIdConvocatoria(1);
        impresoDTO.setIdImpreso(1);
        impresoDTO.getTipoActorDTO().setIdValor(1);
        impresoDTO.setImagenLogoDTO(new ImagenDTO());
        impresoDTO.setItGeneraImpreso("It Genera Impreso");
        impresoDTO.setLstBloqueDTO(new ArrayList<>());
        impresoDTO.setTipoDTO(new CatalogosValoresDTO());

        ConvocatoriaActDTO convocatoriaActDTO3 = new ConvocatoriaActDTO();
        convocatoriaActDTO3.setBolGeneraExpediente(true);
        convocatoriaActDTO3.setCdUsuAlta("Cd Usu Alta");
        convocatoriaActDTO3.setCdUsuBaja("Cd Usu Baja");
        convocatoriaActDTO3.setCdUsuModif("Cd Usu Modif");
        convocatoriaActDTO3.setEsActorCoordinador(true);
        convocatoriaActDTO3.setEsActorParticipante(true);
        convocatoriaActDTO3.setEsActorSolicitante(true);
        convocatoriaActDTO3
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO3
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO3
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        convocatoriaActDTO3.setIdActor(1);
        convocatoriaActDTO3.setIdActorCoordinador(1);
        convocatoriaActDTO3.setIdActorSolicitante(1);
        convocatoriaActDTO3.setIdConvocatoria(1);
        convocatoriaActDTO3.setIdentificacionDTO(new CatalogosValoresDTO());
        convocatoriaActDTO3.setItContrato(true);
        convocatoriaActDTO3.setItGeneraExpediente("It Genera Expediente");
        convocatoriaActDTO3.setItTipoActorPadre("It Tipo Actor Padre");
        convocatoriaActDTO3.setNmMax(1);
        convocatoriaActDTO3.setNmMin(1);
        convocatoriaActDTO3.setTipoActorDTO(new CatalogosValoresDTO());
        convocatoriaActDTO3.setTipoPersonaFisJurDTO(new CatalogosValoresDTO());
        convocatoriaActDTO3.setTlDescripcion("Tl Descripcion");
        convocatoriaActDTO3.setTlDescripcionCorta("Tl Descripcion Corta");

        FicaMetadatos ficaMetadatos2 = new FicaMetadatos();
        ficaMetadatos2.setCarpeta("Bella");
        ficaMetadatos2.setEncriptar(true);
        ficaMetadatos2.setExtension("Extension");
        ficaMetadatos2
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatos2.setIdExterno("Id Externo");
        ficaMetadatos2.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatos2.setNmDiasBorrado(1);
        ficaMetadatos2.setNombreFichero("Nombre Fichero");
        ficaMetadatos2.setS3Key("S3 Key");
        ficaMetadatos2.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatos2.setTituloFichero("Titulo Fichero");
        ficaMetadatos2.setUsuAlta("Usu Alta");
        ficaMetadatos2.setUsuModif("Usu Modif");
        ficaMetadatos2.setUsuario("Usuario");

        FicaMetadatos ficaMetadatosImpOriginal2 = new FicaMetadatos();
        ficaMetadatosImpOriginal2.setCarpeta("Bella");
        ficaMetadatosImpOriginal2.setEncriptar(true);
        ficaMetadatosImpOriginal2.setExtension("Extension");
        ficaMetadatosImpOriginal2
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatosImpOriginal2.setIdExterno("Id Externo");
        ficaMetadatosImpOriginal2.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatosImpOriginal2.setNmDiasBorrado(1);
        ficaMetadatosImpOriginal2.setNombreFichero("Nombre Fichero");
        ficaMetadatosImpOriginal2.setS3Key("S3 Key");
        ficaMetadatosImpOriginal2.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatosImpOriginal2.setTituloFichero("Titulo Fichero");
        ficaMetadatosImpOriginal2.setUsuAlta("Usu Alta");
        ficaMetadatosImpOriginal2.setUsuModif("Usu Modif");
        ficaMetadatosImpOriginal2.setUsuario("Usuario");

        FicaMetadatos ficaMetadatosPlanTecnica2 = new FicaMetadatos();
        ficaMetadatosPlanTecnica2.setCarpeta("Bella");
        ficaMetadatosPlanTecnica2.setEncriptar(true);
        ficaMetadatosPlanTecnica2.setExtension("Extension");
        ficaMetadatosPlanTecnica2
                .setFechaFichero(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        ficaMetadatosPlanTecnica2.setIdExterno("Id Externo");
        ficaMetadatosPlanTecnica2.setMetadatosUsuario(new FicaMetadatosUsuario());
        ficaMetadatosPlanTecnica2.setNmDiasBorrado(1);
        ficaMetadatosPlanTecnica2.setNombreFichero("Nombre Fichero");
        ficaMetadatosPlanTecnica2.setS3Key("S3 Key");
        ficaMetadatosPlanTecnica2.setSistemaAlmacenamiento("alice.liddell@example.org");
        ficaMetadatosPlanTecnica2.setTituloFichero("Titulo Fichero");
        ficaMetadatosPlanTecnica2.setUsuAlta("Usu Alta");
        ficaMetadatosPlanTecnica2.setUsuModif("Usu Modif");
        ficaMetadatosPlanTecnica2.setUsuario("Usuario");

        ImpresoDTO impresoJustecDTO = new ImpresoDTO();
        impresoJustecDTO.setCdFica(1);
        impresoJustecDTO.setCdFicaImpreso(1);
        impresoJustecDTO.setCdFicaPlantilla(1);
        impresoJustecDTO.setCdIdentificacion("Cd Identificacion");
        impresoJustecDTO.setCdUsuAlta("Cd Usu Alta");
        impresoJustecDTO.setCdUsuBaja("Cd Usu Baja");
        impresoJustecDTO.setCdUsuModif("Cd Usu Modif");
        impresoJustecDTO.setContenidoFichero("AXAXAXAX".getBytes("UTF-8"));
        impresoJustecDTO.setContenidoFicheroImpOriginal("AXAXAXAX".getBytes("UTF-8"));
        impresoJustecDTO.setContenidoFicheroPlanTecnica("AXAXAXAX".getBytes("UTF-8"));
        impresoJustecDTO.setConvocatoriaActDTO(convocatoriaActDTO3);
        impresoJustecDTO.setDsDescripcion("Ds Descripcion");
        impresoJustecDTO.setDsImpresoOriginal("Ds Impreso Original");
        impresoJustecDTO.setDsPlantillaTecnica("Ds Plantilla Tecnica");
        impresoJustecDTO
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoJustecDTO
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoJustecDTO
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        impresoJustecDTO.setFicaFichero(new FicaFicheroImpl());
        impresoJustecDTO.setFicaFicheroImpOriginal(new FicaFicheroImpl());
        impresoJustecDTO.setFicaFicheroPlantTecnica(new FicaFicheroImpl());
        impresoJustecDTO.setFicaMetadatos(ficaMetadatos2);
        impresoJustecDTO.setFicaMetadatosImpOriginal(ficaMetadatosImpOriginal2);
        impresoJustecDTO.setFicaMetadatosPlanTecnica(ficaMetadatosPlanTecnica2);
        impresoJustecDTO.setIdConvocatoria(1);
        impresoJustecDTO.setIdImpreso(1);
        impresoJustecDTO.getTipoActorDTO().setIdValor(1);
        impresoJustecDTO.setImagenLogoDTO(new ImagenDTO());
        impresoJustecDTO.setItGeneraImpreso("It Genera Impreso");
        impresoJustecDTO.setLstBloqueDTO(new ArrayList<>());
        impresoJustecDTO.setTipoDTO(new CatalogosValoresDTO());

        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setAreasCientificas("Areas Cientificas");
        interesadoDTO.setCdCodigoPostal("Cd Codigo Postal");
        interesadoDTO.setCdLocalidad("Cd Localidad");
        interesadoDTO.setCdNumIden("Cd Num Iden");
        interesadoDTO.setCdProvincia("Cd Provincia");
        interesadoDTO.setCdSexo("Cd Sexo");
        interesadoDTO.setCdTipoVia("Cd Tipo Via");
        interesadoDTO.setCdUsuAlta("Cd Usu Alta");
        interesadoDTO.setCdUsuBaja("Cd Usu Baja");
        interesadoDTO.setCdUsuModif("Cd Usu Modif");
        interesadoDTO.setCdUsuario("Cd Usuario");
        interesadoDTO.setDsApellido1("Ds Apellido1");
        interesadoDTO.setDsApellido2("Ds Apellido2");
        interesadoDTO.setDsDepartamento("alice.liddell@example.org");
        interesadoDTO.setDsDireccion("Ds Direccion");
        interesadoDTO.setDsEmail("jane.doe@example.org");
        interesadoDTO.setDsEscalera("Ds Escalera");
        interesadoDTO.setDsInteresado("Ds Interesado");
        interesadoDTO.setDsMovil("Ds Movil");
        interesadoDTO.setDsNombre("Ds Nombre");
        interesadoDTO.setDsNumero("Ds Numero");
        interesadoDTO.setDsPiso("Ds Piso");
        interesadoDTO.setDsPuerta("Ds Puerta");
        interesadoDTO.setDsTelefono("Ds Telefono");
        interesadoDTO
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        interesadoDTO
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        interesadoDTO
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        interesadoDTO
                .setFcNacimiento(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        interesadoDTO.setIdAreaCientifica(1);
        interesadoDTO.setIdInteresado(1);
        interesadoDTO.getTipoActorDTO().setNmConstante(1);
        interesadoDTO.setItCoorEval("It Coor Eval");
        interesadoDTO.setListaPersonaAreaDTO(new ArrayList<>());
        interesadoDTO.setListaPersonaEspecialidadDTO(new ArrayList<>());
        interesadoDTO.setListaPersonaOrganismoDTO(new ArrayList<>());
        interesadoDTO.setListaPersonaTitulacionDTO(new ArrayList<>());
        interesadoDTO.setNmSexenioInve(1);
        interesadoDTO.setNmSexenioTran(1);
        interesadoDTO.setTipoIdentificacionDTO(new CatalogosValoresDTO());

        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setBAviso(true);
        expediente.setBAvisoDocumento(true);
        expediente.setBAvisoEvaluacion(true);
        expediente.setBAvisoRequisitos(true);
        expediente.setBAvisoResolucion(true);
        expediente.setBlEstadoDefinitivo(true);
        expediente.setBlEstadoProvisional(true);
        expediente.setBlHabilitarResolucionDefinitiva(true);
        expediente.setBlHabilitarResolucionProvisional(true);
        expediente.setBlMostrarMensajeAprobarDenegarDefinitivo1(true);
        expediente.setBlMostrarMensajeAprobarDenegarDefinitivo2(true);
        expediente.setBlMostrarMensajeAprobarDenegarDefinitivo3(true);
        expediente.setBlMostrarMensajeAprobarDenegarProvisional1(true);
        expediente.setBlMostrarMensajeAprobarDenegarProvisional2(true);
        expediente.setCdAsunto("alice.liddell@example.org");
        expediente.setCdEreg("Cd Ereg");
        expediente.setCdExpediente("Cd Expediente");
        expediente.setCdFicaValoracionAleg(42);
        expediente.setCdNoteProvisional("Cd Note Provisional");
        expediente.setCdSiex("Cd Siex");
        expediente.setCdUnidad("Cd Unidad");
        expediente.setCdUsuAlta("Cd Usu Alta");
        expediente.setCdUsuBaja("Cd Usu Baja");
        expediente.setCdUsuModif("Cd Usu Modif");
        expediente.setColorFondoText("Color Fondo Text");
        expediente.setColorFondoTextResolucionDef("Color Fondo Text Resolucion Def");
        expediente.setConvocatoriaActDTO(convocatoriaActDTO);
        expediente.setDsDestinoEreg("Ds Destino Ereg");
        expediente.setEsCerradoCoordinadorTodo(true);
        expediente.setEsEstadoEvalEnEvaluacion(true);
        expediente.setEsExpedienteCoordinador(true);
        expediente.setEsExpedienteParticipante(true);
        expediente.setEsExpedienteSolicitante(true);
        expediente.setEstadoBeneficiarioDTO(new CatalogosValoresDTO());
        expediente.setEstadoDTO(new CatalogosValoresDTO());
        expediente.setEstadoProvisionalDTO(new CatalogosValoresDTO());
        expediente.setEstadoRevAdminDTO(new CatalogosValoresDTO());
        expediente.setEstadoRevEvalDTO(new CatalogosValoresDTO());
        expediente.setEstadoRevJusTecDTO(new CatalogosValoresDTO());
        expediente.setEstadoRevRequiDTO(new CatalogosValoresDTO());
        expediente.setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente.setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente
                .setFcFinContrato(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente.setFcFirma(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente
                .setFcIniContrato(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente.setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        expediente.setIdExpediente(1);
        expediente.setIdSolicitud(1);
        expediente.setImpresoDTO(impresoDTO);
        expediente.setImpresoJustecDTO(impresoJustecDTO);
        expediente.setInteresadoDTO(interesadoDTO);
        expediente.setItAlegaciones("It Alegaciones");
        expediente.setItSeleccionExpediente(true);
        expediente.setLstExpedienteDocDTO(new ArrayList<>());
        expediente.setLstExpedienteParticipante(new ArrayList<>());
        expediente.setLstExpedienteReqDTO(new ArrayList<>());
        expediente.setModoConsulta(true);
        expediente.setMostrarBotoneraAprobarRechazarEvaluacion(true);
        expediente.setNmNota(1);
        expediente.setNmPaso(1);
        expediente.setSePuedeDescargarInformeCoor(true);
        expediente.setValoresRelacionables(new HashMap<>());
        assertFalse(documentoPtfrServiceimpl.guardarEnvioEnDcoumentoPTFR(convocatoria, expediente, 1, 1,
                "AXAXAXAX".getBytes("UTF-8"),null,null,null));
    }
    
    

    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#buscarEnviosNotificaciones(int, int, List, List, Integer, Integer)}
     */
    @Test
    public void testBuscarEnviosNotificaciones() throws ServiceException {
        when(envioNotificacionesPortafirmasDAO.find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any())).thenReturn(new ArrayList<>());
        ArrayList<AtlasFilter> filtros = new ArrayList<>();
        assertTrue(
                envioNotificacionesPortafirmasServiceImpl.buscarEnviosNotificaciones(1, 3, filtros, new ArrayList<>(), 1, 1)
                        .isEmpty());
        verify(envioNotificacionesPortafirmasDAO).find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any());
        assertEquals(3, filtros.size());
    }
    
    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#buscarEnviosNotificaciones(int, int, List, List, Integer, Integer)}
     */
    @Test
    public void testBuscarEnviosNotificaciones2() throws ServiceException {
        EnvioNotificacionesPortaFirma envioNotificacionesPortaFirma = new EnvioNotificacionesPortaFirma();
        envioNotificacionesPortaFirma.setCdNumeroExpediente(10);
        envioNotificacionesPortaFirma.setCdUsuAlta("USUV01");
        envioNotificacionesPortaFirma.setCdUsuBaja("USUV01");
        envioNotificacionesPortaFirma.setCdUsuModif("USUV01");
        envioNotificacionesPortaFirma.setDsDestinatario("Juan palomo");
        envioNotificacionesPortaFirma.setDsFirmante("USUV01");
        envioNotificacionesPortaFirma.setDsTramite(110);
        envioNotificacionesPortaFirma.setDsVerificador("Andres iniesta");
        envioNotificacionesPortaFirma
                .setFcAltaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        envioNotificacionesPortaFirma
                .setFcBajaFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        envioNotificacionesPortaFirma
                .setFcEnvioCola(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        envioNotificacionesPortaFirma
                .setFcModifFila(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        envioNotificacionesPortaFirma.setIdCodigo(1);
        envioNotificacionesPortaFirma.setIdConvocatoria(23);
        envioNotificacionesPortaFirma.setNmEnvio("S");
        envioNotificacionesPortaFirma.setNmEstado(1);
        envioNotificacionesPortaFirma.setNmTramite("desistimiento");

        ArrayList<EnvioNotificacionesPortaFirma> envioNotificacionesPortaFirmaList = new ArrayList<>();
        envioNotificacionesPortaFirmaList.add(envioNotificacionesPortaFirma);
        when(envioNotificacionesPortafirmasDAO.find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any())).thenReturn(envioNotificacionesPortaFirmaList);
        ArrayList<AtlasFilter> filtros = new ArrayList<>();
        assertEquals(1,
                envioNotificacionesPortafirmasServiceImpl.buscarEnviosNotificaciones(1, 3, filtros, new ArrayList<>(), 1, 1)
                        .size());
        verify(envioNotificacionesPortafirmasDAO).find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any());
        assertEquals(3, filtros.size());
    }

    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#buscarEnviosNotificaciones(int, int, List, List, Integer, Integer)}
     */
    @Test
    public void testBuscarEnviosNotificaciones3() throws ServiceException {
        when(envioNotificacionesPortafirmasDAO.find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any())).thenReturn(new ArrayList<>());
        ArrayList<AtlasFilter> filtros = new ArrayList<>();

        ArrayList<AtlasSort> orden = new ArrayList<>();
        orden.add(AtlasSort.asc("idConvocatoria"));
        assertTrue(
                envioNotificacionesPortafirmasServiceImpl.buscarEnviosNotificaciones(1, 3, filtros, orden, 1, 1).isEmpty());
        verify(envioNotificacionesPortafirmasDAO).find(anyInt(), anyInt(), Mockito.<List<AtlasFilter>>any(),
                Mockito.<List<AtlasSort>>any());
        assertEquals(3, filtros.size());
    }

    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#guardar(EnvioNotificacionPortafirmasDTO)}
     */
    @Test
    public void testGuardar() throws ServiceException {
        doNothing().when(envioNotificacionesPortafirmasDAO).insertOrUpdate(Mockito.<EnvioNotificacionesPortaFirma>any());
        EnvioNotificacionPortafirmasDTO actualGuardarResult = envioNotificacionesPortafirmasServiceImpl
                .guardar(new EnvioNotificacionPortafirmasDTO());
        assertNull(actualGuardarResult.getCdNumeroExpediente());
        assertNull(actualGuardarResult.getNmTramite());
        assertNull(actualGuardarResult.getNmEstado());
        assertNull(actualGuardarResult.getNmEnvio());
        assertNull(actualGuardarResult.getIdConvocatoria());
        assertNull(actualGuardarResult.getIdCodigo());
        assertNull(actualGuardarResult.getFcModifFila());
        assertNull(actualGuardarResult.getFcEnvioCola());
        assertNull(actualGuardarResult.getFcBajaFila());
        assertNull(actualGuardarResult.getFcAltaFila());
        assertNull(actualGuardarResult.getDsVerificador());
        assertNull(actualGuardarResult.getDsTramite());
        assertNull(actualGuardarResult.getDsFirmante());
        assertNull(actualGuardarResult.getDsDestinatario());
        assertNull(actualGuardarResult.getDocumentoAdjunto());
        assertNull(actualGuardarResult.getCdUsuModif());
        assertNull(actualGuardarResult.getCdUsuBaja());
        assertNull(actualGuardarResult.getCdUsuAlta());
        verify(envioNotificacionesPortafirmasDAO).insertOrUpdate(Mockito.<EnvioNotificacionesPortaFirma>any());
    }

    /**
     * Method under test: {@link EnvioNotificacionesPortafirmasServiceImpl#guardar(EnvioNotificacionPortafirmasDTO)}
     */
    @Test
    public void testGuardar2() throws ServiceException {
        doNothing().when(envioNotificacionesPortafirmasDAO).insertOrUpdate(Mockito.<EnvioNotificacionesPortaFirma>any());
        envioNotificacionesPortafirmasServiceImpl.guardar(null);
        verify(envioNotificacionesPortafirmasDAO).insertOrUpdate(Mockito.<EnvioNotificacionesPortaFirma>any());
    }
}

