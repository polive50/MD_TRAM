package org.madrid.saii.lib.services;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.ExpedienteReqHistDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EnvioDocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.NoteObtenerNotificacionesDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudDatosDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudResultadoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesDTO;
import org.madrid.saii.lib.dto.PtfrConsultaTiposDocumentoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToEntity;
import org.madrid.saii.lib.dto.converters.DocumentoPtfrToDTO;
import org.madrid.saii.lib.dto.converters.DocumentoPtfrToEntity;
import org.madrid.saii.lib.dto.converters.ExpedientePresupMontoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.dto.converters.SolicitudToDTO;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.Base64;
import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.eadm.ptfr.domain.PtfrConsultaEtapasOut;
import atlas.eadm.ptfr.domain.PtfrEstadoDocOut;
import atlas.eadm.ptfr.domain.PtfrEtapas;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio DocumentoPtfr
 *
 * @author everis
 */
@Slf4j
@Service("documentoPtfrService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class DocumentoPtfrServiceImpl extends
        BaseServiceImpl<DocumentoPtfr, Integer, DocumentoPtfrDAO> implements DocumentoPtfrService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param documentoPtfrDAO
     */
    @Autowired
    public DocumentoPtfrServiceImpl(@Qualifier("documentoPtfrDAO")
    DocumentoPtfrDAO documentoPtfrDAO) {
        super();
        this.setDao(documentoPtfrDAO);
    }

    /**
     * Constructor vacio
     */
    public DocumentoPtfrServiceImpl() {
        super();
    }

    /** Repositorio ExpedienteDocDAO */
    @Autowired
    @Qualifier("expedienteDocDAO")
    @Setter
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("expedienteReqDAO")
    @Setter
    private ExpedienteReqDAO expedienteReqDAO;
    
    @Autowired
    @Qualifier("expedienteReqHistDAO")
    @Setter
    private ExpedienteReqHistDAO expedienteReqHistDAO;
    

    /** Repositorio expedienteDocHistDAO */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    @Setter
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /** Servicio de Porta Firma. */
    @Autowired
    @Qualifier("ptfrAdmeService")
    @Setter
    public PtfrAdmeService ptfrAdmeService;

    @Autowired
    @Qualifier("noteAdmeService")
    @Setter
    private NoteAdmeService noteAdmeService;

    @Autowired
    @Qualifier("solicitudDAO")
    @Setter
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("convocatoriaDAO")
    @Setter
    private ConvocatoriaDAO convocatoriaDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    @Setter
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    /** Servicio Convocatoria Pla */
    @Autowired
    @Qualifier("convocatoriaPlaService")
    @Setter
    private ConvocatoriaPlaService convocatoriaPlaService;

    /** Servicio Convocatoria */
    @Autowired
    @Qualifier("convocatoriaService")
    @Setter
    private ConvocatoriaService convocatoriaService;

    /** Servicio Expediente */
    @Autowired
    @Qualifier("expedienteService")
    @Setter
    private ExpedienteService expedienteService;

    /** Servicio Expediente */
    @Autowired
    @Qualifier("expedienteJusTecService")
    @Setter
    private ExpedienteJusTecService expedienteJusTecService;
    
    @Autowired
    @Qualifier("expedienteJusEcoService")
    @Setter
    private ExpedienteJusEcoService expedienteJusEcoService;

    /** Servicio Expediente */
    @Autowired
    @Qualifier("expedienteJusTecDocService")
    @Setter
    private ExpedienteJusTecDocService expedienteJusTecDocService;

    /** Servicio CatalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** Servicio PdfService */
    @Lazy
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    @Lazy
    @Autowired
    @Qualifier("convocatoriaComService")
    private ConvocatoriaComService convocatoriaComService;

    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    @Autowired
    @Qualifier("cataService")
    private CataService cataService;

    /**
     * Servicio de expedienteContratoService
     */
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;

    @Autowired
    @Qualifier("expedienteContratoDAO")
    @Setter
    private ExpedienteContratoDAO expedienteContratoDAO;

    @Autowired
    @Qualifier("expedienteContratoHistDAO")
    private ExpedienteContratoHistDAO expedienteContratoHistDAO;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;

    // VMM 24/09/26 MD-2725: bean aparte para poder anotar su metodo con REQUIRES_NEW - ver comentario
    // en ResolucionModificacionService.
    @Autowired
    @Qualifier("resolucionModificacionService")
    private ResolucionModificacionService resolucionModificacionService;

    // VMM 26/10/26 MD-3168: referencia a este mismo bean a traves del proxy de Spring, necesaria para
    // que la llamada a procesarLecturaNotificacionNoteEnvio (REQUIRES_NEW) desde el batch aplique la
    // anotacion transaccional (una llamada this.xxx() la ignoraria).
    @Lazy
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrServiceProxy;
    
    @Autowired
    @Qualifier("expedienteModifPresupMontoService")
    private ExpedienteModifPresupMontoService expedienteModifPresupMontoService;
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteModifPlazoService")
    private ExpedienteModifPlazoService expedienteModifPlazoService;
    
    
    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;
    
    
    
    
    

    /**
     * Servicio Tramite
     */
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;

    /**
     * Servicio informe Helper PDF
     */
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;    
    
    @Autowired
    @Qualifier("expedienteJusEcoDocService")
    @Setter
    private ExpedienteJusEcoDocService expedienteJusEcoDocService;

    
    @Autowired
    @Qualifier("convocatoriaActService")
    @Setter
    private ConvocatoriaActService convocatoriaActService;
 
    
    /**
     * Guarda un/a documentoPtfr.
     *
     * @param documentoPtfrDTO the documentoPtfr DTO
     * @return DocumentoPtfrDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public DocumentoPtfrDTO guardar(DocumentoPtfrDTO documentoPtfrDTO) throws ServiceException {
        return DocumentoPtfrToDTO.INSTANCE
                .apply(this.insertOrUpdate(DocumentoPtfrToEntity.INSTANCE.apply(documentoPtfrDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = {ServiceException.class,
            RuntimeException.class })
    public DocumentoPtfrDTO enviarPortaFirmas(ConvocatoriaDTO convocatoriaDTO, Integer idTipoTramite,
            Integer idExpediente, EnvioDocumentoPtfrDTO envio, Integer idDocumentoPTFR)
            throws ServiceException {

        List<AtlasFilter> filtrosEntrada = new ArrayList<>();

        // Obtenemos datos de la convocatoria pla
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                convocatoriaDTO.getIdConvocatoria(), Operador.EQUALS));
        filtrosEntrada.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTipoTramite, Operador.EQUALS));

        List<ConvocatoriaPlaDTO> lstItem = this.convocatoriaPlaService.buscar(0, -1, filtrosEntrada,
                null);

        if (lstItem.isEmpty()) {
            throw new ServiceException("No hay documentos para el tramite en el Portafirmas.");
        }

        ConvocatoriaPlaDTO convocatoriaPla = lstItem.get(0);

        // Obtenemos datos de la convocatoria
//        ConvocatoriaDTO convocatoria = this.convocatoriaService.buscarId(idConvocatoria);

        // Obtenemos datos del expediente
        ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(idExpediente);

        Integer codigoTipoDocumento = this
                .recuperarCodigoPlantilla(convocatoriaPla.getCdPlantilla());

        // Obtenemos los datos de la consejeria
        String strConsejeria = this.unidadAdminService.obtenerConsejeria();
        byte[] informeTramitePdf;

        DocumentoPtfr documentoPtfrAux = this.find(idDocumentoPTFR);

        List<Integer> tramitesNoFormPdf = new ArrayList<>();
        tramitesNoFormPdf.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_PROVISIONAL));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_DEFINITIVA));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DENEGACION_DEFINITIVA));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA));
        tramitesNoFormPdf
                .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION));
        // MD-2147 añadimos el trámite RESOLUCION EXCLUSION PROVISIONAL
        tramitesNoFormPdf
                .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL));
        tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO));
        // AÑADIR TRAMITE NUEVO

        if (tramitesNoFormPdf.contains(idTipoTramite) && documentoPtfrAux != null
                && documentoPtfrAux.getDocumentoAdjunto() != null) {
            informeTramitePdf = documentoPtfrAux.getDocumentoAdjunto();
        } else {
            Tramite tramite = tramiteService.getTramite(idTipoTramite);
            tramite.setPdfService(this.pdfService);
            tramite.setCatalogosValoresService(this.catalogosValoresService);
            tramite.setExpedienteService(this.expedienteService);
            convocatoriaPla.setIdEstaDocuSubaAdmi(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
            informeTramitePdf = tramite.formularioPdf(convocatoriaDTO, convocatoriaPla, expedienteDTO,
                    strConsejeria);
        }

        // Enviar a Portafirmas
        PtfrAltaSolicitudDatosDTO datosEntrada = new PtfrAltaSolicitudDatosDTO();
        datosEntrada.setCodigoTipoDocumento(codigoTipoDocumento);
        datosEntrada.setDocumento(informeTramitePdf);
        datosEntrada.setModoRespuesta(Constantes.Portafirmas.MODO_RESPUESTA);

        if (Objects.nonNull(convocatoriaPla.getLstConvocatoriaPlaVerDTO())) {
            convocatoriaPla.getLstConvocatoriaPlaVerDTO().forEach(
                    e -> datosEntrada.getListaCodigoVerificadores().add(e.getCdVerificador()));
        }

//        if (Objects.nonNull(convocatoriaPla.getLstConvocatoriaPlaFirDTO())) {
//            convocatoriaPla.getLstConvocatoriaPlaFirDTO()
//                    .forEach(e -> datosEntrada.getListaCodigoFirmantes().add(e.getCdFirmante()));
//        }

        if (Objects.nonNull(convocatoriaPla.getLstConvocatoriaPlaFirDTO())) {
            convocatoriaPla.getLstConvocatoriaPlaFirDTO().stream()
                    .filter(e -> e.getFcBajaFila() == null)
                    .forEach(e -> datosEntrada.getListaCodigoFirmantes().add(e.getCdFirmante()));
        }
        
        datosEntrada.setReferenciaSolicitante(null);

        CatalogosValoresDTO cataTramiteDTO = this.catalogosValoresService.buscarId(idTipoTramite);

        String titulo = "";
        if (envio.getIdExpedienteJustec() != null) {
            ExpedienteJusTecDTO expJustec = expedienteJusTecService
                    .buscarPorIdExpedienteJustec(envio.getIdExpedienteJustec());
            titulo = "Exp: " + idExpediente + " - Plantilla: " + cataTramiteDTO.getDsValor()
                    + "- Periodo : " + expJustec.getNmPeriodo();
            
          //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco  
        } else if (envio.getIdExpedienteJusEco() != null){
            ExpedienteJusEcoDTO expJusEco = expedienteJusEcoService
                    .buscarPorIdExpedienteJuseco(envio.getIdExpedienteJusEco());
            titulo = "Exp: " + idExpediente + " - Plantilla: " + cataTramiteDTO.getDsValor()
                    + "- Periodo : " + expJusEco.getNmPeriodo();
        } else {
            titulo = "Exp: " + idExpediente + " - Plantilla: " + cataTramiteDTO.getDsValor();
        }

        datosEntrada.setTitulo(titulo);
        datosEntrada.setCove(Constantes.AfirmacionNegacion.S);

        PtfrAltaSolicitudResultadoDTO outputPtfr = this.ptfrAdmeService.altaSolicitud(datosEntrada);

        if (Objects.isNull(outputPtfr)) {
            throw new ServiceException("No se pudo realizar el envío a Portafirmas.");
        }

        if (documentoPtfrAux != null && documentoPtfrAux.getIdDocPtfrPadre() != null) {
            DocumentoPtfrDTO docPtfrDTO =  this.registrarEnvioPortafirmas(expedienteDTO, outputPtfr, idTipoTramite,
                    idDocumentoPTFR);
            this.registrarEnvioPortafirmasConjunta(expedienteDTO, outputPtfr, idTipoTramite,
                    docPtfrDTO);
            return docPtfrDTO;
        }
        
        return this.registrarEnvioPortafirmas(expedienteDTO, outputPtfr, idTipoTramite,
                idDocumentoPTFR);

    }
    
    private void registrarEnvioPortafirmasConjunta(ExpedienteDTO expedienteDTO,
            PtfrAltaSolicitudResultadoDTO outputPtfr, Integer idTipoTramite,
            DocumentoPtfrDTO docPtfrDTO) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_DOC_PTFR_PADRE,
                docPtfrDTO.getIdDocumentoPtfr(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<DocumentoPtfr> lstDocPtfr = this.find(filtros, null);
        for (DocumentoPtfr docPtfr : lstDocPtfr) {
            docPtfr.setDsReferenciaPtfr(docPtfrDTO.getDsReferenciaPtfr());
            docPtfr.setCdArchivoPtfr(docPtfrDTO.getCdArchivoPtfr());
            docPtfr.setFcEnvioPtfr(docPtfrDTO.getFcEnvioPtfr());
            docPtfr.setCdUsuModif(docPtfrDTO.getCdUsuModif());
            docPtfr.setFcModifFila(docPtfrDTO.getFcModifFila());
            docPtfr.setCdEstadoPtfr(docPtfrDTO.getCdEstadoPtfr());
            docPtfr = this.update(docPtfr);
        }
    }

    /**
    *
    */
    @Override
    public byte[] enviarPortaFirmasBorrador(Integer idConvocatoria, Integer idTipoTramite,
            Integer idExpediente, Integer nmPeriodo) throws ServiceException {

        List<AtlasFilter> filtrosEntrada = new ArrayList<>();

        // Obtenemos datos de la convocatoria pla
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtrosEntrada.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTipoTramite, Operador.EQUALS));

        List<ConvocatoriaPlaDTO> lstItem = this.convocatoriaPlaService.buscar(0, -1, filtrosEntrada,
                null);

        if (lstItem.isEmpty()) {
            throw new ServiceException("No hay documentos para el tramite en el Portafirmas.");
        }

        ConvocatoriaPlaDTO convocatoriaPla = lstItem.get(0);

        // Obtenemos datos de la convocatoria
        ConvocatoriaDTO convocatoria = this.convocatoriaService.buscarId(idConvocatoria);

        if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA))
                && nmPeriodo != null) {
            convocatoria.setCdTitulo(convocatoria.getCdTitulo() + "- Periodo: " + nmPeriodo);
        }
        
        //VMM 20/10/25 MD-1587: comprobamos si es Justif Eco
        if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA))
                && nmPeriodo != null) {
            convocatoria.setCdTitulo(convocatoria.getCdTitulo() + "- Periodo: " + nmPeriodo);
        }

        // Obtenemos datos del expediente
        ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(idExpediente);

        Tramite tramite = tramiteService.getTramite(idTipoTramite);
        tramite.setPdfService(this.pdfService);
        tramite.setCatalogosValoresService(this.catalogosValoresService);
        tramite.setExpedienteService(this.expedienteService);
        
        
        List<PtfrConsultaFirmantesDTO> lstFirmantesPtfr = this.ptfrAdmeService.consultaFirmantes();

        // Firmantes
        this.procesarFirmante(convocatoriaPla, lstFirmantesPtfr);
        

        // Obtenemos los datos de la consejeria
        String strConsejeria = this.unidadAdminService.obtenerConsejeria();
        convocatoriaPla.setIdEstaDocuSubaAdmi(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
        return tramite.formularioPdf(convocatoria, convocatoriaPla, expedienteDTO, strConsejeria);

    }
    
    private void procesarFirmante(ConvocatoriaPlaDTO plantilla, List<PtfrConsultaFirmantesDTO> lstFirmantesPtfr) {
        if (plantilla != null) {
            plantilla.setLstDsFirmantes(new ArrayList<>());

            if (lstFirmantesPtfr != null && !lstFirmantesPtfr.isEmpty() && plantilla.getLstConvocatoriaPlaFirDTO() != null) {
                plantilla.getLstConvocatoriaPlaFirDTO().stream().forEach(firmante -> {
                    if (firmante.getCdFirmante() != null && firmante.getFcBajaFila() == null) {
                        String dsFirmante = this.recuperarNombreFirmante(lstFirmantesPtfr, firmante.getCdFirmante());
                        if (StringUtils.isNotBlank(dsFirmante)) {
                            plantilla.getLstDsFirmantes().add(dsFirmante);
                        }
                    }
                });
            }
            
            this.procesarFirmantesFormateo(plantilla);
        }
    }

    private String recuperarNombreFirmante(List<PtfrConsultaFirmantesDTO> listaFirmantes, String cdFirmante) {
        if (listaFirmantes == null || StringUtils.isBlank(cdFirmante)) {
            return "";
        }

        return listaFirmantes.stream()
                .filter(f -> Objects.nonNull(f.getCodUsu()) && f.getCodUsu().equals(cdFirmante))
                .map(PtfrConsultaFirmantesDTO::getNombre)
                .findFirst()
                .orElse("");
    }

    private void procesarFirmantesFormateo(ConvocatoriaPlaDTO plantilla) {
        if (plantilla.getLstDsFirmantes() != null && !plantilla.getLstDsFirmantes().isEmpty()) {
            plantilla.setDsFirmante(StringUtils.join(plantilla.getLstDsFirmantes(), " | "));
        } else if (StringUtils.isNotBlank(plantilla.getCdPlantilla())) {
            plantilla.setDsFirmante("- SIN ASIGNAR FIRMANTE -");
        } else {
            plantilla.setDsFirmante("");
        }
    }
    
    
//
//    private void procesarFirmantes(List<ConvocatoriaPlaDTO> lstItem,
//            List<PtfrConsultaFirmantesDTO> lstFirmantesPtfr) {
//        if (lstFirmantesPtfr != null && !lstFirmantesPtfr.isEmpty()) {
//
//            lstItem.stream().forEach(plantilla -> {
//                plantilla.setLstDsFirmantes(new ArrayList<>());
//                if (plantilla.getLstConvocatoriaPlaFirDTO() != null) {
//                    plantilla.getLstConvocatoriaPlaFirDTO().stream().forEach(firmante -> {
//                        if (firmante.getCdFirmante() != null && firmante.getFcBajaFila() == null) {
//                            String dsFirmante = this.recuperarNombreFirmante(lstFirmantesPtfr,
//                                    firmante.getCdFirmante());
//                            if (!dsFirmante.equals("")) {
//                                plantilla.getLstDsFirmantes().add(dsFirmante);
//                            }
//                        }
//                    });
//                }
//                this.procesarFirmantesFormateo(plantilla);
//            });
//
//        }
//    }
//    
//    private String recuperarNombreFirmante(List<PtfrConsultaFirmantesDTO> listaFirmantes,
//            String cdFirmante) {
//
//        Optional<PtfrConsultaFirmantesDTO> opFirmante = listaFirmantes.stream()
//                .filter(f -> f.getCodUsu().equals(cdFirmante)).findFirst();
//
//        return opFirmante.isPresent() ? opFirmante.get().getNombre() : "";
//    }
//    
//    
//    private void procesarFirmantesFormateo(ConvocatoriaPlaDTO plantilla) {
//        if (!plantilla.getLstDsFirmantes().isEmpty()) {
//            plantilla.setDsFirmante(StringUtils.join(plantilla.getLstDsFirmantes(), " | "));
//        } else if (StringUtils.isNotBlank(plantilla.getCdPlantilla())
//                && !plantilla.getCdPlantilla().equals(StringUtils.EMPTY)) {
//            plantilla.setDsFirmante("- SIN ASIGNAR FIRMANTE -");
//        } else {
//            plantilla.setDsFirmante("");
//        }
//    }
    
    
    /**
     * Método para recuperar código de la plantilla PTFR
     *
     * @param cdPlantilla cógido alias de plantilla
     * @return Código de plantilla
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    private Integer recuperarCodigoPlantilla(String cdPlantilla) throws ServiceException {

        Integer codigoTipoDocumento = null;

        PtfrConsultaTiposDocumentoDTO tipoDocumento = this.ptfrAdmeService
                .consultaTiposDocumentoPorCodigoAlias(cdPlantilla);

        if (tipoDocumento.getCodigo() != null) {

            codigoTipoDocumento = Integer.valueOf(tipoDocumento.getCodigo());

        }

        return codigoTipoDocumento;

    }

    /**
     * Método para validar si el usuario esta de alta en NOTE
     *
     * @param documentoPtfr Datos de documento PTRF
     * @param idSolicitud Identificador de solicitud
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    private void validarAltaUsuarioNote(DocumentoPtfr documentoPtfr, Integer idSolicitud)
            throws ServiceException {
        documentoPtfr.setItUsuAltaNote(Constantes.AfirmacionNegacion.N);

        if (idSolicitud != null) {
            Solicitud solicitud = this.solicitudDAO.find(idSolicitud);
            String cdAbonado = this.noteAdmeService
                    .recuperarCdAbonado(solicitud.getInteresado().getCdNumIden());
            if (cdAbonado != null && StringUtils.isNotBlank(cdAbonado)) {
                documentoPtfr.setItUsuAltaNote(Constantes.AfirmacionNegacion.S);
            }
        }
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco como param
    public Boolean guardarEnvioEnDcoumentoPTFR(ConvocatoriaDTO convocatoria,
            ExpedienteDTO expediente, Integer idExpediente, Integer idtramite,
            byte[] documentoAdjunto, Integer idExpedienteJustec, Integer idPresupuestoModif, Integer idExpedienteJusEco)
            throws ServiceException {
        Boolean vuelta = Boolean.FALSE;
        try {

            DocumentoPtfr documentoPtfr = this.crearEnvioDocumentoPtfr(convocatoria, expediente,
                    idtramite, documentoAdjunto, idExpedienteJustec, idPresupuestoModif,
                    idExpedienteJusEco, null);
            return documentoPtfr.getIdDocumentoPtfr() != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /** Crea la petición y devuelve su identificador generado por la secuencia. */
    private DocumentoPtfr crearEnvioDocumentoPtfr(ConvocatoriaDTO convocatoria,
            ExpedienteDTO expediente, Integer idtramite, byte[] documentoAdjunto,
            Integer idExpedienteJustec, Integer idPresupuestoModif, Integer idExpedienteJusEco,
            Integer idDocPtfrPadre) throws ServiceException {
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();

        Integer idConvocatoria = convocatoria.getIdConvocatoria();

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));

        filtrosEntrada.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idtramite, Operador.EQUALS));

        List<ConvocatoriaPlaDTO> lstItem = this.convocatoriaPlaService.buscar(0, -1,
                filtrosEntrada, null);

        if (lstItem.isEmpty()) {
            throw new ServiceException("No hay documentos para el tramite en el Portafirmas.");
        }

        DocumentoPtfr documentoPtfr = new DocumentoPtfr();
        documentoPtfr.setIdDocPtfrPadre(idDocPtfrPadre);
        documentoPtfr.setIdExpediente(expediente.getIdExpediente());
        documentoPtfr.setDsReferenciaPtfr(null);
        documentoPtfr.setCdArchivoPtfr(null);
        documentoPtfr.setFcEnvioPtfr(null);
        documentoPtfr.setCdEstadoPtfr(null);

        if (idExpedienteJustec != null) {
            documentoPtfr.setidExpedienteJustec(idExpedienteJustec);
        }
      //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco
        if (idExpedienteJusEco != null) {
            documentoPtfr.setIdExpedienteJusEco(idExpedienteJusEco);
        }

        if (documentoAdjunto != null) {
            documentoPtfr.setDocumentoAdjunto(documentoAdjunto);
        }

        if (idPresupuestoModif != null) {
            documentoPtfr.setIdPresupuestoModif(idPresupuestoModif);
        }

        // VMM 26/09/24 MD-593: ahora solo cargamos los tramites del gestor
        // (catalogo 38), revisar esto...
        List<CatalogosValoresDTO> lstTramites = this.catalogosValoresService.buscarPorCatalogo(
                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR));

        for (CatalogosValoresDTO tramite : lstTramites) {
            if (tramite.getIdValor().equals(idtramite)) {
                documentoPtfr.setTramite(CatalogosValoresToEntity.INSTANCE.apply(tramite));
            }
        }

        // Validamos si el usuario esta de alta en NOTE
        this.validarAltaUsuarioNote(documentoPtfr, expediente.getIdSolicitud());

        // Registramos los datos del envío a portafirmas
        documentoPtfr = this.insert(documentoPtfr);

        return documentoPtfr;
    }

    /**
     * Metodo para registrar los datos del proceso de envío a portafirmas
     *
     * @param expediente
     * @param outputPtfr
     * @param esAdministrativo
     * @param listaExpedienteDoc
     * @param listaExpedienteReq
     * @param tipoPlantilla
     * @return
     * @throws ServiceException
     */
/**
      private DocumentoPtfrDTO registrarEnvioPortafirmas(ExpedienteDTO expedienteDTO,
     
            PtfrAltaSolicitudResultadoDTO outputPtfr, Integer idTipoTramite,
            Integer idDocumentoPTFR) throws ServiceException {

        // Preparamos los datos del envio a portafirmas
        DocumentoPtfr documentoPtfr = new DocumentoPtfr();

        documentoPtfr = this.find(idDocumentoPTFR);

        // documentoPtfr.setIdExpediente(expedienteDTO.getIdExpediente());
        documentoPtfr.setDsReferenciaPtfr(outputPtfr.getReferencia());
        documentoPtfr.setCdArchivoPtfr(Integer.parseInt(outputPtfr.getCodigoArchivo()));
        documentoPtfr.setFcEnvioPtfr(new Date());
        documentoPtfr.setCdEstadoPtfr(Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE);

        // Registramos los datos del envío a portafirmas
        documentoPtfr = this.update(documentoPtfr);
        
        // MD-2147 Sólo actualizar los registros de potfr si no es excl prov
        // MD-2148. Solo aplica si no es un desisitmiento provisional
//        if (!idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))
//                || !idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))) {

            Integer idTraReqSubConIni = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL);
    
            // Actualizamos las referencias del registro de portafirmas a los
            // documentos / requisitos implicados
            if (idTipoTramite
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DESISTIMIENTO))
                    || idTipoTramite.equals(idTraReqSubConIni)
                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA))
                    //VMM 17/10/25 MD-1586: incluimos el tramite de subsanacion de JusEco
                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_RECHAZADA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA_PARCIAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.CONCESION_PROVISIONAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DENEGACION_PROVISIONAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.CONCESION_DEFINITIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DENEGACION_DEFINITIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL))
                    ) 
            {
                // Actualizar estado de documentos
                CatalogosValores estado = new CatalogosValores();
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
                //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco
                List<ExpedienteDoc> listaExpedienteDoc = this.obtenerDocumentosAReportar(idTipoTramite,
                        expedienteDTO.getIdExpediente(), documentoPtfr.getidExpedienteJustec(), documentoPtfr.getIdExpedienteJusEco());
                // MD-1979. Incluimos las expedientesDoc de los requisitos
                List<ExpedienteDoc> listaExpedienteDocReq = this.obtenerDocumentosAReportar(
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                        expedienteDTO.getIdExpediente(), documentoPtfr.getidExpedienteJustec(), documentoPtfr.getIdExpedienteJusEco());
                
                Map<Integer, ExpedienteDoc> mapaUnicos = new LinkedHashMap<>();

                // Metemos la primera lista
                for (ExpedienteDoc doc : listaExpedienteDoc) {
                    mapaUnicos.put(doc.getIdExpedienteDoc(), doc);
                }

                for (ExpedienteDoc doc : listaExpedienteDocReq) {
                    mapaUnicos.put(doc.getIdExpedienteDoc(), doc);
                }

                if (mapaUnicos == null || mapaUnicos.isEmpty()) {
                    throw new ServiceException("No hay documentos disponibles para enviar a Portafirmas.");
                }

                for (ExpedienteDoc expedienteDoc : mapaUnicos.values()) {
                    expedienteDoc.setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                    expedienteDoc
                            .setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoDocumento.ENVIADO_A_PTFR)));
                    this.expedienteDocDAO.update(expedienteDoc);
                    this.expedienteDocHistDAO.crearHistorico(expedienteDoc, null);
                }
    
               
                List<Solicitud> solicitud = this.solicitudDAO.buscarPorExpediente(expedienteDTO.getIdExpediente());
                
                ConvocatoriaDTO convocatoria = this.convocatoriaService.buscarId(solicitud.get(0).getConvocatoria().getIdConvocatoria());
                
                if (convocatoria.getItRequisitos().equals(Constantes.AfirmacionNegacion.S)) {
                    List<ExpedienteReq> listaExpedienteReq = this.obtenerRequisitosAReportar(
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                            expedienteDTO.getIdExpediente());
                    
                    if(listaExpedienteReq != null && !listaExpedienteReq.isEmpty()) {
                        for (ExpedienteReq expedienteReq : listaExpedienteReq) {
                            expedienteReq.setDocumentoPtfr(new DocumentoPtfr());
                            expedienteReq.getDocumentoPtfr()
                                    .setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                            this.expedienteReqDAO.update(expedienteReq);
                        }
                    }
     
                }
     
                
                if ((idTipoTramite
                        .equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL))
                        //|| idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                        //        .get(Constantes.Tramite.DESISTIMIENTO))
                        )
                        && expedienteDTO.getExpedienteContrato() != null
                        && expedienteDTO.getExpedienteContrato().getIdExpedienteContrato() != null) {
    
                    this.expedienteContratoService.cambiarEstadoContrato(
                            expedienteDTO.getExpedienteContrato(),
                            expedienteDTO.getExpedienteContrato().getIdExpediente(), null,
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
    
                }
            } else if (idTipoTramite
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.EXCLUSION))
                    // MD-2190 quitamos la validación del trámite de alegación exclusión provisional
    //                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
    //                        .get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL))
                    ) {
                // actualizar la lista de requisitos
                List<ExpedienteReq> listaExpedienteReq = this.obtenerRequisitosAReportar(idTipoTramite,
                        expedienteDTO.getIdExpediente());
                for (ExpedienteReq expedienteReq : listaExpedienteReq) {
                    expedienteReq.setDocumentoPtfr(new DocumentoPtfr());
                    expedienteReq.getDocumentoPtfr()
                            .setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                    this.expedienteReqDAO.update(expedienteReq);
                }
            } else {
                log.error("Tramite no soportado");
            }
        //}
        return DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);

    }
*/


private DocumentoPtfrDTO registrarEnvioPortafirmas(ExpedienteDTO expedienteDTO,
     
            PtfrAltaSolicitudResultadoDTO outputPtfr, Integer idTipoTramite,
            Integer idDocumentoPTFR) throws ServiceException {

        // Preparamos los datos del envio a portafirmas
        DocumentoPtfr documentoPtfr = new DocumentoPtfr();

        documentoPtfr = this.find(idDocumentoPTFR);

        // documentoPtfr.setIdExpediente(expedienteDTO.getIdExpediente());
        documentoPtfr.setDsReferenciaPtfr(outputPtfr.getReferencia());
        documentoPtfr.setCdArchivoPtfr(Integer.parseInt(outputPtfr.getCodigoArchivo()));
        documentoPtfr.setFcEnvioPtfr(new Date());
        documentoPtfr.setCdEstadoPtfr(Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE);

        // Registramos los datos del envío a portafirmas
        documentoPtfr = this.update(documentoPtfr);
        
        // MD-2147 Sólo actualizar los registros de potfr si no es excl prov
        // MD-2148. Solo aplica si no es un desisitmiento provisional
//        if (!idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))
//                || !idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))) {

            Integer idTraReqSubConIni = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL);
    
            // Actualizamos las referencias del registro de portafirmas a los
            // documentos / requisitos implicados
            if (idTipoTramite
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DESISTIMIENTO))
                    || idTipoTramite.equals(idTraReqSubConIni)
                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA))
                    //VMM 17/10/25 MD-1586: incluimos el tramite de subsanacion de JusEco
                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_RECHAZADA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA_PARCIAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.CONCESION_PROVISIONAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DENEGACION_PROVISIONAL))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.CONCESION_DEFINITIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.DENEGACION_DEFINITIVA))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL))
                    ) 
            {
                // Actualizar estado de documentos
                CatalogosValores estado = new CatalogosValores();
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
                //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco
                List<ExpedienteDoc> listaExpedienteDoc = this.obtenerDocumentosAReportar(idTipoTramite,
                        expedienteDTO.getIdExpediente(), documentoPtfr.getidExpedienteJustec(), documentoPtfr.getIdExpedienteJusEco());
                // MD-1979. Incluimos las expedientesDoc de los requisitos
                List<ExpedienteDoc> listaExpedienteDocReq = this.obtenerDocumentosAReportar(
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                        expedienteDTO.getIdExpediente(), documentoPtfr.getidExpedienteJustec(), documentoPtfr.getIdExpedienteJusEco());
                
                Map<Integer, ExpedienteDoc> mapaUnicos = new LinkedHashMap<>();

                // Metemos la primera lista
                for (ExpedienteDoc doc : listaExpedienteDoc) {
                    mapaUnicos.put(doc.getIdExpedienteDoc(), doc);
                }

                for (ExpedienteDoc doc : listaExpedienteDocReq) {
                    mapaUnicos.put(doc.getIdExpedienteDoc(), doc);
                }

                if (mapaUnicos == null || mapaUnicos.isEmpty()) {
                    throw new ServiceException("No hay documentos disponibles para enviar a Portafirmas.");
                }

                for (ExpedienteDoc expedienteDoc : mapaUnicos.values()) {
                    expedienteDoc.setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                    expedienteDoc
                            .setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoDocumento.ENVIADO_A_PTFR)));
                    this.expedienteDocDAO.update(expedienteDoc);
                    this.expedienteDocHistDAO.crearHistorico(expedienteDoc, null);
                }
    
               
                List<Solicitud> solicitud = this.solicitudDAO.buscarPorExpediente(expedienteDTO.getIdExpediente());
                
                ConvocatoriaDTO convocatoria = this.convocatoriaService.buscarId(solicitud.get(0).getConvocatoria().getIdConvocatoria());
                
                if (convocatoria.getItRequisitos().equals(Constantes.AfirmacionNegacion.S)) {
                    List<ExpedienteReq> listaExpedienteReq = this.obtenerRequisitosAReportar(
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                            expedienteDTO.getIdExpediente());
                    
                    if(listaExpedienteReq != null && !listaExpedienteReq.isEmpty()) {
                        for (ExpedienteReq expedienteReq : listaExpedienteReq) {
                            expedienteReq.setDocumentoPtfr(new DocumentoPtfr());
                            expedienteReq.getDocumentoPtfr()
                                    .setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                            this.expedienteReqDAO.update(expedienteReq);
                        }
                    }
     
                }
     
                
                if ((idTipoTramite
                        .equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL))
                        //|| idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                        //        .get(Constantes.Tramite.DESISTIMIENTO))
                        )
                        && expedienteDTO.getExpedienteContrato() != null
                        && expedienteDTO.getExpedienteContrato().getIdExpedienteContrato() != null) {
    
                    this.expedienteContratoService.cambiarEstadoContrato(
                            expedienteDTO.getExpedienteContrato(),
                            expedienteDTO.getExpedienteContrato().getIdExpediente(), null,
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
    
                }
            } else if (idTipoTramite
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO))
//                    || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.EXCLUSION))
                    // MD-2190 quitamos la validación del trámite de alegación exclusión provisional
    //                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
    //                        .get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL))
                    ) {
                // actualizar la lista de requisitos
                List<ExpedienteReq> listaExpedienteReq = this.obtenerRequisitosAReportar(idTipoTramite,
                        expedienteDTO.getIdExpediente());
                for (ExpedienteReq expedienteReq : listaExpedienteReq) {
                    expedienteReq.setDocumentoPtfr(new DocumentoPtfr());
                    expedienteReq.getDocumentoPtfr()
                            .setIdDocumentoPtfr(documentoPtfr.getIdDocumentoPtfr());
                    this.expedienteReqDAO.update(expedienteReq);
                }
            // VMM 05/10/26 MD-3066: resolucion de modificacion presupuestaria. Al enviarla a Portafirmas
            // la modificacion pasa de "Pendiente de envio al portafirmas" a "Enviado al portafirmas".
            // Antes caia en el else ("Tramite no soportado") y el estado nunca avanzaba.
            } else if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))
                    && documentoPtfr.getIdPresupuestoModif() != null) {
                this.cambiarEstadoModificacionPresPlazoOtra(documentoPtfr.getIdPresupuestoModif(),
                        Constantes.EstadoExpedienteResolucion.ENVIADA_PORTAFIRMAS);
            } else {
                log.error("Tramite no soportado");
            }
        //}
        return DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);

    }

    /**
     * VMM 05/10/26 MD-3066: cambia el estado de una modificacion presupuestaria/plazo/otra (la que
     * referencia DocumentoPtfr.idPresupuestoModif). No retrocede una modificacion ya Resuelta.
     *
     * @param idModificacion idExpedienteModifPresPlazoOtra
     * @param nmConstanteEstado constante de Constantes.EstadoExpedienteResolucion
     * @throws ServiceException
     */
    private void cambiarEstadoModificacionPresPlazoOtra(Integer idModificacion,
            Integer nmConstanteEstado) throws ServiceException {

        ExpedienteModifPresPlazoOtra modificacion = this.expedienteModifPresPlazoOtraService
                .find(idModificacion);

        if (modificacion == null) {
            log.warn("No se encontro la modificacion idExpedienteModifPresPlazoOtra={} para cambiarle el estado.",
                    idModificacion);
            return;
        }

        Integer idEstadoResuelta = utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpedienteResolucion.RESUELTA);
        if (modificacion.getEstado() != null
                && idEstadoResuelta.equals(modificacion.getEstado().getIdValor())) {
            return;
        }

        modificacion.setEstado(new CatalogosValores(
                utilConstantes.getGlobalCatalogosValores().get(nmConstanteEstado)));
        this.expedienteModifPresPlazoOtraService.insertOrUpdate(modificacion);
    }

    /**
     * métod para cambiar el estado de la solicitud a excluida, si el expediente
     * es actor solicitante.
     *
     * @param expediente
     * @throws ServiceException
     */
    private void cambiarEstadoSolicitudPorExpediente(Expediente expediente)
            throws ServiceException {

        if (Objects.nonNull(expediente)
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                        .equals(expediente.getConvocatoriaAct().getTipoActor().getIdValor())) {

            Solicitud solicitud = this.solicitudDAO
                    .find(expediente.getSolicitud().getIdSolicitud());
            Integer idEstadoExpediente = null;

            if (Objects.isNull(solicitud)) {
                throw new ServiceException("No se encontraron datos de la solicitud.");
            }

            Integer idEstExpExc = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.EXCLUIDO);
            Integer idEstExpDes = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.DESISTIDO);

            if (idEstExpExc.equals(expediente.getEstado().getIdValor())) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoSolicitud.EXCLUIDA)));
                idEstadoExpediente = idEstExpExc;
            } else if (idEstExpDes.equals(expediente.getEstado().getIdValor())) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoSolicitud.DESISTIDA)));
                idEstadoExpediente = idEstExpDes;
            }

            solicitud.setFcModifFila(new Date());
            solicitud.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            solicitud = this.solicitudDAO.merge(solicitud);

            if (Objects.nonNull(idEstadoExpediente)) {
                // Se da de baja a los expedientes participantes de la solicitud
                List<AtlasFilter> lstFiltros = new ArrayList<>();
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        expediente.getIdExpediente(), Operador.NOT_EQUALS));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                        solicitud.getIdSolicitud(), Operador.EQUALS));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<Integer> lstEstadoFinales = new ArrayList<>();
                lstEstadoFinales.add(idEstExpExc);
                lstEstadoFinales.add(idEstExpDes);
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                        lstEstadoFinales, Operador.NOT_IN));

                List<Expediente> lstExpediente = this.expedienteDAO.find(lstFiltros, null);

                for (Expediente exp : lstExpediente) {
                    exp.setEstado(new CatalogosValores(idEstadoExpediente));
                    exp.setEstadoBenef(
                            ExpedienteToEntity.INSTANCE.calcularEstadoBenef(idEstadoExpediente));
                    exp.setFcModifFila(new Date());
                    exp.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                    exp = this.expedienteDAO.merge(exp);
                    this.expedienteHistDAO.crearHistorico(exp,
                            Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                }
            }

        }
    }

    /**
     */
    @SuppressWarnings("static-access")
    @Override
    // VMM 26/10/26 MD-3168: el batch pasa a ser de solo lectura (solo lista los envios pendientes);
    // cada envio se procesa en su propia transaccion (procesarLecturaNotificacionNoteEnvio,
    // REQUIRES_NEW). Antes todo iba en una unica transaccion y, si NOTE devolvia "No existe la
    // notificacion" para un envio, NoteAdmeServiceImpl.obtenerNotificacion (@Transactional REQUIRED)
    // marcaba la transaccion compartida como rollback-only aunque el catch de aqui capturase la
    // excepcion, y al terminar se revertia la actualizacion de TODOS los demas envios.
    @Transactional(readOnly = true)
    public void consultarLecturaNotificacionNoteBatch() throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DOCUMENTO_PTFR_FC_ENVIO_NOTE,
                Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DOCUMENTO_PTFR_FC_LECTURA_NOTE,
                Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS)
                        .or(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL)));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<DocumentoPtfr> listaEnviosPtfr = this.find(filtros, null);

        for (DocumentoPtfr envioPtfr : listaEnviosPtfr) {

            if (Objects.nonNull(envioPtfr.getCdNote())) {

                // VMM 09/09/26 MD-2725: se envuelve en try/catch por envío para que un
                // documento con problemas (p.ej. NOTE devuelve vacío) no bloquee la
                // lectura del resto de la cola.
                // VMM 26/10/26 MD-3168: se llama a traves del proxy de Spring para que se aplique
                // el REQUIRES_NEW; si falla solo se revierte este envio y se sigue con el siguiente.
                try {
                    this.documentoPtfrServiceProxy
                            .procesarLecturaNotificacionNoteEnvio(envioPtfr.getIdDocumentoPtfr());
                } catch (Exception e) {
                    log.error("Error al consultar la lectura de notificación NOTE para idDocumentoPtfr={}",
                            envioPtfr.getIdDocumentoPtfr(), e);
                }

            }
        }

    }

    /**
     * VMM 26/10/26 MD-3168: procesa un unico envio de la cola de lectura de NOTE en su propia
     * transaccion (REQUIRES_NEW), de modo que un fallo solo revierte este envio.
     */
    @SuppressWarnings("static-access")
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
            ServiceException.class, RuntimeException.class })
    public void procesarLecturaNotificacionNoteEnvio(Integer idDocumentoPtfr)
            throws ServiceException {

        DocumentoPtfr envioPtfr = this.find(idDocumentoPtfr);

        if (Objects.isNull(envioPtfr) || Objects.isNull(envioPtfr.getCdNote())) {
            return;
        }

        // VMM 09/09/26 MD-2725: se comprueba null antes de usar noteOutput (antes causaba NPE
        // al llamar noteOutput.getNumeroAbonado()).
        NoteObtenerNotificacionesDTO noteOutput = this.noteAdmeService
                .obtenerNotificacion(envioPtfr.getCdNote());

        if (Objects.nonNull(noteOutput)) {

            FicaFichero ficaAcuseRecibo = this.noteAdmeService.generarFicaAcuseReciboPDF(
                    envioPtfr.getCdNote(), noteOutput.getNumeroAbonado(),
                    Constantes.AccionFICA.CONSULTA);

            if (Constantes.Note.CD_L.equals(noteOutput.getCodigoEstado())) {

                this.consultarLecturaNotificacionNoteAceptado(envioPtfr,
                        noteOutput.getFechaLectura(), ficaAcuseRecibo);

            } else if (Constantes.Note.CD_R.equals(noteOutput.getCodigoEstado())
                    || Constantes.Note.CD_RA.equals(noteOutput.getCodigoEstado())) {

                this.consultarLecturaNotificacionNoteRechazado(envioPtfr,
                        noteOutput.getFechaRechazo(), noteOutput.getCodigoEstado(),
                        ficaAcuseRecibo);
            }
        }

        // validamos si han pasado más de 10 días sin leer la
        // notificación
        this.validarInicioPlazoSubsanacionPeriodo(envioPtfr);
    }

    // VMM 24/09/26 MD-2725: toda la logica de resolucion de una modificacion (marcarla Resuelta y
    // volcar presupuesto/plazos a las tablas finales) se movio a ResolucionModificacionServiceImpl,
    // como un bean aparte anotado con @Transactional(REQUIRES_NEW) - cada modificacion se resuelve
    // ahora en su propia transaccion independiente, asi que un fallo a mitad hace rollback limpio de
    // solo esa modificacion (sin dejar padres/hijos a medias) y ya no hace falta la logica de 3 fases
    // ni los catch de "entidad ya asociada a la sesion" que tenia esta clase. Ver
    // resolucionModificacionService.aplicarResolucionModificacion(idExpedienteModifPresPlazoOtra).

    /**
     *
     * @param envioPtfr
     * @param fechaLecturaNote
     * @throws ServiceException
     */
    private void consultarLecturaNotificacionNoteAceptado(DocumentoPtfr envioPtfr,
            String fechaLecturaNote, FicaFichero ficaAcuseRecibo) throws ServiceException {

        // VMM 24/09/26 MD-2725: guardamos el estado "de lectura" original de este envio por si hay que
        // revertirlo mas abajo (ver comentario junto a aplicarResolucionModificacion).
        Date fcLecturaNoteOriginal = envioPtfr.getFcLecturaNote();
        String cdEstadoNoteOriginal = envioPtfr.getCdEstadoNote();
        Date fcRechazoNoteOriginal = envioPtfr.getFcRechazoNote();
        Integer cdFicaAcuseReciboNoteOriginal = envioPtfr.getCdFicaAcuseReciboNote();

        envioPtfr.setFcLecturaNote(
                Utils.convertADateTime(fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
        envioPtfr.setFcRechazoNote(null);
        envioPtfr.setCdEstadoNote(Constantes.Note.CD_L);
        if (ficaAcuseRecibo != null) {
            envioPtfr.setCdFicaAcuseReciboNote(
                    ficaAcuseRecibo.getIdInterno() != null ? ficaAcuseRecibo.getIdInterno() : null);
        }

        List<Integer> lstTramiteSubsanacion = new ArrayList<>();
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(
                Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA));

        // Calculamos el plazo de subsanacion
        if (lstTramiteSubsanacion.contains(envioPtfr.getTramite().getIdValor())) {

            // Recuperamos los datos del expediente
            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());

            List<ExpedienteJusTecDTO> listExpJ = expedienteJusTecService
                    .buscarPorExpediente(envioPtfr.getIdExpediente());

            List<ExpedienteContratoDTO> listExpC = expedienteContratoService
                    .buscarPorExpediente(envioPtfr.getIdExpediente());

            Boolean pendienteSubsanacionJCT = Boolean.FALSE;
            Boolean pendienteSubsanacionContrato = Boolean.FALSE;

            if (listExpJ != null) {
                for (ExpedienteJusTecDTO expJ : listExpJ) {
                    if (expJ.getIdEstadoRevAdminDTO() != null
                            && expJ.getIdEstadoRevAdminDTO().getIdValor() != null
                            && expJ.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes
                                    .getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION))) {
                        pendienteSubsanacionJCT = Boolean.TRUE;
                    }
                }
            }

            if (listExpC != null) {
                for (ExpedienteContratoDTO expC : listExpC) {
                    if (expC.getIdEstadoDTO() != null && expC.getIdEstadoDTO().getIdValor() != null
                            && (expC.getIdEstadoDTO().getIdValor()
                                    .equals(utilConstantes.getGlobalCatalogosValores()
                                            .get(Constantes.EstadoContrato.REQUIERE_SUBSANACION))
                                    || expC.getIdEstadoDTO().getIdValor().equals(utilConstantes
                                            .getGlobalCatalogosValores()
                                            .get(Constantes.EstadoContrato.REQUIERE_SUBSANACION_RENUNCIA))
                                    || expC.getIdEstadoDTO().getIdValor().equals(utilConstantes
                                            .getGlobalCatalogosValores()
                                            .get(Constantes.EstadoContrato.PENDIENTE_SUBSANACION)))) {
                        pendienteSubsanacionContrato = Boolean.TRUE;
                    }
                }
            }

            Integer idEstExpPenSubReq = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS);

            if (idEstExpPenSubReq.equals(expediente.getEstadoRevAdmin().getIdValor())
                    || idEstExpPenSubReq.equals(expediente.getEstadoRevRequi().getIdValor())
                    || Boolean.TRUE.equals(pendienteSubsanacionJCT)
                    || Boolean.TRUE.equals(pendienteSubsanacionContrato)) {
                
                
                Date fechaLecturaLimite = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                        fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));

                if (Objects.isNull(envioPtfr.getFcIniPlazoSubsanacion())) {

                    Date fechaLectura = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                            fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
                    LocalDateTime ldtFechaDesde = Utils.convertDateToLocalDateTime(fechaLectura);
//                    fechaLectura = this.cataService.calcularDiasHabiles(
//                            Utils.convertLocalDateToDateInstant(ldtFechaDesde), 1,
//                            Constantes.Suca.CD_MUNI_MADRID,
//                            expediente.getInteresado().getCdLocalidad());
//                    
                    fechaLectura = this.cataService.calcularDiasHabilesInicio(
                            Utils.convertLocalDateToDateInstant(ldtFechaDesde),
                            Constantes.Suca.CD_MUNI_MADRID,
                            expediente.getInteresado().getCdLocalidad());
                    

                    envioPtfr.setFcIniPlazoSubsanacion(fechaLectura);
                    envioPtfr.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);
                }

                Solicitud solicitud = this.solicitudDAO
                        .find(expediente.getSolicitud().getIdSolicitud());

                LocalDateTime fcLimiteSubsanacion = this.expedienteService
                        .calcularFechaLimiteSubsanacion(
                                solicitud.getConvocatoria().getNmDiasSubsanacion(),
                                DocumentoPtfrToDTO.INSTANCE.apply(envioPtfr),fechaLecturaLimite);

                envioPtfr.setFcFinPlazoSubsanacion(
                        Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));

            }
        } else if (envioPtfr.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))
                   && envioPtfr.getIdPresupuestoModif() != null) {
            // VMM 24/09/26 MD-2725: se delega en resolucionModificacionService (transaccion propia
            // REQUIRES_NEW) - ya no hace falta cargar la entidad aqui, el servicio la busca el mismo.
            // Si falla, esa transaccion REQUIRES_NEW hace su propio rollback, pero la transaccion de
            // ESTE metodo (consultarLecturaNotificacionNoteBatch) sigue viva y comitearia igualmente el
            // marcado de "leido" de envioPtfr hecho mas arriba, dejando este envio fuera del filtro de
            // candidatos (fcLecturaNote IS NULL) para siempre aunque la resolucion nunca se aplicara.
            // Por eso aqui se revierte ese marcado antes de relanzar la excepcion, para que el envio
            // vuelva a ser candidato en el siguiente batch.
            try {
                this.resolucionModificacionService.aplicarResolucionModificacion(envioPtfr.getIdPresupuestoModif());
            } catch (ServiceException e) {
                envioPtfr.setFcLecturaNote(fcLecturaNoteOriginal);
                envioPtfr.setCdEstadoNote(cdEstadoNoteOriginal);
                envioPtfr.setFcRechazoNote(fcRechazoNoteOriginal);
                envioPtfr.setCdFicaAcuseReciboNote(cdFicaAcuseReciboNoteOriginal);
                throw e;
            }

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())) {
            
            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());
            
            Date fechaLecturaLimite = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                    fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
            
            if (Objects.isNull(envioPtfr.getFcIniPlazoSubsanacion())) {

                Date fechaLectura = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                        fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
                LocalDateTime ldtFechaDesde = Utils.convertDateToLocalDateTime(fechaLectura);
                
//                fechaLectura = this.cataService.calcularDiasHabiles(
//                        Utils.convertLocalDateToDateInstant(ldtFechaDesde), 1,
//                        Constantes.Suca.CD_MUNI_MADRID,
//                        expediente.getInteresado().getCdLocalidad());
                
                fechaLectura = this.cataService.calcularDiasHabilesInicio(
                        Utils.convertLocalDateToDateInstant(ldtFechaDesde),
                        Constantes.Suca.CD_MUNI_MADRID,
                        expediente.getInteresado().getCdLocalidad());

                envioPtfr.setFcIniPlazoSubsanacion(fechaLectura);
                envioPtfr.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);
            }

            Solicitud solicitud = this.solicitudDAO
                    .find(expediente.getSolicitud().getIdSolicitud());
            
            
            Convocatoria conv = this.convocatoriaDAO.find(solicitud.getConvocatoria().getIdConvocatoria());
            ConvocatoriaDTO convDTO =  ConvocatoriaToDTO.INSTANCE.apply(conv);
            
            Integer diasSubsanacion;
            
            if("C".equals(convDTO.getTipoResolModalidadDTO().getCdTipoResolucion())){
                
                if( conv.getNmDiasHabilesAudiencia() != null) {
                    diasSubsanacion = conv.getNmDiasHabilesAudiencia();
                }else {
                    diasSubsanacion = solicitud.getConvocatoria().getNmDiasSubsanacion();
                }
            }else {
                diasSubsanacion = solicitud.getConvocatoria().getNmDiasSubsanacion();
            }

            LocalDateTime fcLimiteSubsanacion = this.expedienteService
                    .calcularFechaLimiteSubsanacion(
                            diasSubsanacion,
                            DocumentoPtfrToDTO.INSTANCE.apply(envioPtfr), fechaLecturaLimite);

            envioPtfr.setFcFinPlazoSubsanacion( Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));
            
            
            
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(envioPtfr.getTramite().getIdValor())){
          ///donde tenemos que meter el codigo
            
            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());
            
            Date fechaLecturaLImite = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                    fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
            
            if (Objects.isNull(envioPtfr.getFcIniPlazoSubsanacion())) {

                Date fechaLectura = Utils.asignarTiempoFechaInicioDia(Utils.convertADateTime(
                        fechaLecturaNote, Constantes.Utiles.FORMATO_FECHA_HORA));
                LocalDateTime ldtFechaDesde = Utils.convertDateToLocalDateTime(fechaLectura);
//                fechaLectura = this.cataService.calcularDiasHabiles(
//                        Utils.convertLocalDateToDateInstant(ldtFechaDesde), 1,
//                        Constantes.Suca.CD_MUNI_MADRID,
//                        expediente.getInteresado().getCdLocalidad());
                
                fechaLectura = this.cataService.calcularDiasHabilesInicio(
                        Utils.convertLocalDateToDateInstant(ldtFechaDesde),
                        Constantes.Suca.CD_MUNI_MADRID,
                        expediente.getInteresado().getCdLocalidad());
                

                envioPtfr.setFcIniPlazoSubsanacion(fechaLectura);
                envioPtfr.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);
            }

            Solicitud solicitud = this.solicitudDAO
                    .find(expediente.getSolicitud().getIdSolicitud());

            LocalDateTime fcLimiteSubsanacion = this.expedienteService
                    .calcularFechaLimiteSubsanacion(
                            solicitud.getConvocatoria().getNmDiasSubsanacion(),
                            DocumentoPtfrToDTO.INSTANCE.apply(envioPtfr),fechaLecturaLImite);

            envioPtfr.setFcFinPlazoSubsanacion( Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));
            
            //cambiar estado dele xpediente
            this.expedienteService.cambiarEstado(envioPtfr.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(
                    Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST),
                    Constantes.Utiles.COD_USU_BATCH_AUDITORIA);    
                    
            // MD-2148 -- Se quita porque no se está ejecutando, no existe ALEGACIONES_EXCLUSION_PROVISIONAL
//        } else if (utilConstantes.getGlobalCatalogosValores()
//                .get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL)
//                .equals(envioPtfr.getTramite().getIdValor())) {
//
//            ExpedienteDTO expedienteAlegaciones = this.expedienteService
//                    .buscarId(envioPtfr.getIdExpediente());
//            expedienteAlegaciones.setEstadoRevRequiDTO(
//                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL)));
//            this.expedienteService.guardar(expedienteAlegaciones);
        } else if (utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL)
                .equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO)
                        .equals(envioPtfr.getTramite().getIdValor())) {

            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());
            Solicitud solicitud = this.solicitudDAO
                    .find(expediente.getSolicitud().getIdSolicitud());
            Convocatoria convo = this.convocatoriaDAO
                    .find(solicitud.getConvocatoria().getIdConvocatoria());

            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL)
                    .equals(envioPtfr.getTramite().getIdValor())) {
                // TODO VMM 11/06/25 esto se ha quedado obsoleto pero se usará,
                // ahora mismo ese valor no existe
                convo.setIdEstadoResolucion(1);
            } else if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO)
                    .equals(envioPtfr.getTramite().getIdValor())) {
                // TODO VMM 11/06/25 esto se ha quedado obsoleto pero se usará,
                // ahora mismo ese valor no existe
                convo.setIdEstadoResolucion(3);
            }

        }

        envioPtfr.setFcModifFila(new Date());
        envioPtfr.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        this.getDao().merge(envioPtfr);

    }

    /**
     *
     * @param envioPtfr
     * @param fechaRechazoNote
     * @throws ServiceException
     */
    private void consultarLecturaNotificacionNoteRechazado(DocumentoPtfr envioPtfr,
            String fechaRechazoNote, String cdEstadoNote, FicaFichero ficaAcuseRecibo) throws ServiceException {

        envioPtfr.setFcLecturaNote(Utils.convertADateTime(fechaRechazoNote, Constantes.Utiles.FORMATO_FECHA_HORA));
        envioPtfr.setFcRechazoNote(Utils.convertADateTime(fechaRechazoNote, Constantes.Utiles.FORMATO_FECHA_HORA));
        envioPtfr.setCdEstadoNote(cdEstadoNote);
        if (ficaAcuseRecibo != null) {
            envioPtfr.setCdFicaAcuseReciboNote(
                    ficaAcuseRecibo.getIdInterno() != null ? ficaAcuseRecibo.getIdInterno() : null);
        }

        List<Integer> lstTramiteSubsanacion = new ArrayList<>();
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(
                Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA));

        // Calculamos el plazo de subsanacion
        if (lstTramiteSubsanacion.contains(envioPtfr.getTramite().getIdValor()) ||
                ( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor()))) {

            
            //Comentamos MD-3135 44 BATCH
            
//            // Recuperamos los datos del expediente
//            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());
//
//            List<ExpedienteJusTecDTO> listExpJ = expedienteJusTecService
//                    .buscarPorExpediente(envioPtfr.getIdExpediente());
//
//            Boolean pendienteSubsanacionJCT = Boolean.FALSE;
//            Boolean pendienteSubsanacionContrato = Boolean.FALSE;
//
//            List<ExpedienteContratoDTO> listExpC = expedienteContratoService
//                    .buscarPorExpediente(envioPtfr.getIdExpediente());
//
//            if (listExpJ != null) {
//                for (ExpedienteJusTecDTO expJ : listExpJ) {
//                    if (expJ.getIdEstadoRevAdminDTO() != null
//                            && expJ.getIdEstadoRevAdminDTO().getIdValor() != null
//                            && expJ.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes
//                                    .getGlobalCatalogosValores()
//                                    .get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION))) {
//                        pendienteSubsanacionJCT = Boolean.TRUE;
//                    }
//                }
//            }
//
//            if (listExpC != null) {
//                for (ExpedienteContratoDTO expC : listExpC) {
//                    if (expC.getIdEstadoRevAdminDTO() != null
//                            && expC.getIdEstadoRevAdminDTO().getIdValor() != null
//                            && expC.getIdEstadoRevAdminDTO().getIdValor()
//                                    .equals(utilConstantes.getGlobalCatalogosValores().get(
//                                            Constantes.EstadoContrato.PENDIENTE_SUBSANACION))) {
//                        pendienteSubsanacionContrato = Boolean.TRUE;
//                    }
//                }
//            }
//
//            if (utilConstantes.getGlobalCatalogosValores()
//                    .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
//                    .equals(expediente.getEstadoRevAdmin().getIdValor())
//                    || utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
//                            .equals(expediente.getEstadoRevRequi().getIdValor())
//                    || Boolean.TRUE.equals(pendienteSubsanacionJCT)
//                    || Boolean.TRUE.equals(pendienteSubsanacionContrato)) {
//                
//
//                if (Objects.isNull(envioPtfr.getFcIniPlazoSubsanacion())) {
//                    
//                    envioPtfr.setFcIniPlazoSubsanacion(envioPtfr.getFcRechazoNote());
//                    envioPtfr.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);
//                }
//
//                Date dFechaRechazoNote = Utils
//                        .asignarTiempoFechaFinalDia(envioPtfr.getFcRechazoNote());
//
//                envioPtfr.setFcFinPlazoSubsanacion(dFechaRechazoNote);
//                //esto no es verdad,
//            }
//
//        }
//        // MD-2148 : ponemos el if para los tramites de resol provisionales
//         else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
//                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
//                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
//                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())) {
            
            Expediente expediente = this.expedienteDAO.find(envioPtfr.getIdExpediente());
            
            if (Objects.isNull(envioPtfr.getFcIniPlazoSubsanacion())) {
    
                Date fechaRechazo = Utils.asignarTiempoFechaInicioDia(envioPtfr.getFcRechazoNote());
                LocalDateTime ldtFechaDesde = Utils.convertDateToLocalDateTime(fechaRechazo);
//                fechaRechazo = this.cataService.calcularDiasHabiles(
//                        Utils.convertLocalDateToDateInstant(ldtFechaDesde), 1,
//                        Constantes.Suca.CD_MUNI_MADRID,
//                        expediente.getInteresado().getCdLocalidad());
                
                fechaRechazo = this.cataService.calcularDiasHabilesInicio(
                        Utils.convertLocalDateToDateInstant(ldtFechaDesde),
                        Constantes.Suca.CD_MUNI_MADRID,
                        expediente.getInteresado().getCdLocalidad());
    
                envioPtfr.setFcIniPlazoSubsanacion(fechaRechazo);
                envioPtfr.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);
            }
    
            Solicitud solicitud = this.solicitudDAO
                    .find(expediente.getSolicitud().getIdSolicitud());
            
            Convocatoria conv = this.convocatoriaDAO.find(solicitud.getConvocatoria().getIdConvocatoria());
            ConvocatoriaDTO convDTO =  ConvocatoriaToDTO.INSTANCE.apply(conv);
            
            Integer diasSubsanacion;
            
            if("C".equals(convDTO.getTipoResolModalidadDTO().getCdTipoResolucion()) && ( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(envioPtfr.getTramite().getIdValor()))){
                
                if( conv.getNmDiasHabilesAudiencia() != null) {
                    diasSubsanacion = conv.getNmDiasHabilesAudiencia();
                }else {
                    diasSubsanacion = solicitud.getConvocatoria().getNmDiasSubsanacion();
                }
                
            }else {
                diasSubsanacion = solicitud.getConvocatoria().getNmDiasSubsanacion();
            }
           
    
            LocalDateTime fcLimiteSubsanacion = this.expedienteService
                    .calcularFechaLimiteSubsanacion(
                            diasSubsanacion,
                            DocumentoPtfrToDTO.INSTANCE.apply(envioPtfr),envioPtfr.getFcRechazoNote());
    
            envioPtfr.setFcFinPlazoSubsanacion( Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));
        } 

        envioPtfr.setFcModifFila(new Date());
        envioPtfr.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        this.getDao().merge(envioPtfr);

    }

    /**
     *
     * @param peticionPortafirmas
     * @throws ServiceException
     */
    private void validarInicioPlazoSubsanacionPeriodo(DocumentoPtfr peticion)
            throws ServiceException {

        try {

            List<Integer> lstTramiteSubsanacion = new ArrayList<>();
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(
                    Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(
                    Constantes.Tramite.CONCESION_DEFINITIVA));
            // MD-2148 incluimos los tramites de resol provisonales
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.CONCESION_PROVISIONAL));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.DENEGACION_PROVISIONAL));
            lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA));
        
          
            if (Objects.nonNull(peticion)
                    && lstTramiteSubsanacion.contains(peticion.getTramite().getIdValor())
                    && Objects.isNull(peticion.getFcIniPlazoSubsanacion())) {

                peticion = this.find(peticion.getIdDocumentoPtfr());

                // Obtenermos la fecha de envio a note o fecha de firmado
                // portafirmas
                Date fechaDesde = peticion.getFcEnvioNote();
                if (Objects.isNull(fechaDesde)) {
                    fechaDesde = peticion.getFcFirmaPtfr();
                }

                if (Objects.nonNull(fechaDesde)) {
                    // Recuperamos los datos del expediente
                    Expediente expediente = this.expedienteDAO.find(peticion.getIdExpediente());

                    List<ExpedienteJusTecDTO> listExpJ = expedienteJusTecService
                            .buscarPorExpediente(peticion.getIdExpediente());

                    List<ExpedienteContratoDTO> listExpC = expedienteContratoService
                            .buscarPorExpediente(peticion.getIdExpediente());

                    Boolean pendienteSubsanacionJCT = Boolean.FALSE;
                    Boolean pendienteSubsanacionContrato = Boolean.FALSE;

                    if (listExpJ != null) {
                        for (ExpedienteJusTecDTO expJ : listExpJ) {
                            if (expJ.getIdEstadoRevAdminDTO() != null
                                    && expJ.getIdEstadoRevAdminDTO().getIdValor() != null
                                    && expJ.getIdEstadoRevAdminDTO().getIdValor()
                                            .equals(utilConstantes.getGlobalCatalogosValores().get(
                                                    Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION))) {
                                pendienteSubsanacionJCT = Boolean.TRUE;
                            }
                        }
                    }

                    if (listExpC != null) {
                        for (ExpedienteContratoDTO expC : listExpC) {
                            if (expC.getIdEstadoRevAdminDTO() != null
                                    && expC.getIdEstadoRevAdminDTO().getIdValor() != null
                                    && expC.getIdEstadoRevAdminDTO().getIdValor()
                                            .equals(utilConstantes.getGlobalCatalogosValores().get(
                                                    Constantes.EstadoContrato.PENDIENTE_SUBSANACION))) {
                                pendienteSubsanacionContrato = Boolean.TRUE;
                            }
                        }
                    }

                    Integer idEstExpPenSubReq = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS);

                    if (idEstExpPenSubReq.equals(expediente.getEstadoRevAdmin().getIdValor())
                            || idEstExpPenSubReq.equals(expediente.getEstadoRevRequi().getIdValor())
                            || Boolean.TRUE.equals(pendienteSubsanacionJCT)
                            || Boolean.TRUE.equals(pendienteSubsanacionContrato)
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA))
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL))
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL))
                            || peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))) {

                        // Recuperamos la fecha actual
                        LocalDateTime fcHoy = LocalDateTime.now();

                        // Sumamos 11 días hábiles porque en caso de no acceder
                        // dentro de los 10 días el 11vo inicia la subsanción

                        // No son días Laboralbles, son días naturales
                        fechaDesde = Utils.asignarTiempoFechaInicioDia(fechaDesde);
                        LocalDateTime fcInicioPlazo = Utils.convertDateToLocalDateTimeInstant(fechaDesde);

                        fcInicioPlazo = fcInicioPlazo.plusDays(10);
                        
                        ZoneId zoneMadrid = ZoneId.of("Europe/Madrid");
                        Date fechainicio = Date.from(fcInicioPlazo.atZone(zoneMadrid).toInstant());
                        
                        Date fechaInicioPlazo = Date.from(fcInicioPlazo.atZone(zoneMadrid).toInstant());
                        
                                              
//                        fechainicio = this.cataService.calcularDiasHabiles(
//                                fechainicio, 1, Constantes.Suca.CD_MUNI_MADRID,
//                                expediente.getInteresado().getCdLocalidad());
                        
                        fechainicio =  this.cataService.calcularDiasHabilesInicio(
                                fechainicio,Constantes.Suca.CD_MUNI_MADRID,
                                expediente.getInteresado().getCdLocalidad());
                        
                        fcInicioPlazo =  Utils.convertDateToLocalDateTimeInstant(fechainicio);

                        // Si la fecha de hoy es igual o posterior a la fecha de
                        // inicio de subsanación
                        if (Objects.nonNull(fcInicioPlazo)
                                && (fcHoy.isEqual(fcInicioPlazo) || fcHoy.isAfter(fcInicioPlazo))) {
                            
                            if(peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA))
                                    && expediente.getEstado().getNmConstante().equals( Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO)) {                
                                                              
                                    //actualizamos el campo                               
                                      // Obtenemos los datos de la solicitud
                                      Solicitud solicitud = this.solicitudDAO.find(expediente.getSolicitud().getIdSolicitud());
                                     ConvocatoriaDTO  convocatoria = this.convocatoriaService.buscarId(solicitud.getConvocatoria().getIdConvocatoria());
                                     
                                     if(convocatoria.getIdAceptacionDesistimientoAyuda() != null && convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor() != null &&
                                             utilConstantes.getGlobalCatalogosValores().get(Constantes.Cata.ID_ACEPTACION_DESISTIMIE_AYUDA_ACEPTAR).equals(convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor())) { 
                                         expediente.setItAceptaAyuda(Constantes.AfirmacionNegacion.N);
                                       //cambiamos el estado dele xpediente dependiendo del lo que tenga en aceptacion de la ayuda
                                         this.expedienteService.cambiarEstado(peticion.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(
                                                 Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO),
                                                 Constantes.Utiles.COD_USU_BATCH_AUDITORIA);   
                                         
                                         CatalogosValores estado = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));
                                         solicitud.setEstado(estado);
                                         this.solicitudDAO.insertOrUpdate(solicitud);
                                                                                 
                                         
                                     }else if ( convocatoria.getIdAceptacionDesistimientoAyuda() != null && convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor() != null &&
                                             utilConstantes.getGlobalCatalogosValores().get(Constantes.Cata.ID_ACEPTACION_DESISTIMIE_AYUDA_DESISTIR).equals(convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor())) {
                                         expediente.setItAceptaAyuda(Constantes.AfirmacionNegacion.S);
                                       //cambiamos el estado dele xpediente dependiendo del lo que tenga en aceptacion de la ayuda
                                         ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(peticion.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(
                                                 Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO),
                                                 Constantes.Utiles.COD_USU_BATCH_AUDITORIA);          
                                         
                                         ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expedienteDTO.getConvocatoriaActDTO().getIdActor());           
                                         SolicitudDTO solicitudDTO = SolicitudToDTO.INSTANCE.apply(solicitud);
                                         
                                         CatalogosValores estado = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
                                         solicitud.setEstado(estado);
                                         this.solicitudDAO.insertOrUpdate(solicitud);
                                         
                                         
                                         if (convocatoriaActDTO != null) {   
                                             this.expedienteService.crearOtrosImpresosExpdte(solicitudDTO, convocatoriaActDTO, expedienteDTO,ConvocatoriaToEntity.INSTANCE.apply(convocatoria));
                                         }
                                              
                                     }       
                                                                
                            }else if(peticion.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA))
                                    && peticion.getIdPresupuestoModif() != null){

                                // VMM 24/09/26 MD-2725: se delega en resolucionModificacionService
                                // (transaccion propia REQUIRES_NEW) - ver comentario analogo en
                                // consultarLecturaNotificacionNoteAceptado.
                                this.resolucionModificacionService
                                        .aplicarResolucionModificacion(peticion.getIdPresupuestoModif());

                            } else {
                             // Asignamos la fecha de inicio de subsanación
                                peticion.setFcIniPlazoSubsanacion(
                                        Utils.convertLocalDateToDateInstant(fcInicioPlazo));
                                // Asignamos indicador si inicio el plazo de
                                // subsanación plazo superior a los 11 días
                                peticion.setItIniPlazoVencido(Constantes.AfirmacionNegacion.S);

                                // Obtenemos los datos de la solicitud
                                Solicitud solicitud = this.solicitudDAO
                                        .find(expediente.getSolicitud().getIdSolicitud());

                                // Calculamos la fecha limite de subsanación
                                LocalDateTime fcLimiteSubsanacion = this.expedienteService
                                        .calcularFechaLimiteSubsanacion(
                                                solicitud.getConvocatoria().getNmDiasSubsanacion(),
                                                DocumentoPtfrToDTO.INSTANCE.apply(peticion),fechaInicioPlazo);

                                // Asignamos la fecha límite de subsanación
                                peticion.setFcFinPlazoSubsanacion(
                                        Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));

                                peticion.setFcModifFila(new Date());
                                peticion.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                                this.getDao().merge(peticion); 
                            }
 
                        }

                    }

                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new ServiceException(ex);
        }
    }

    /**
     *
     */
    @Override
    // VMM 26/10/26 MD-3168: el batch pasa a ser de solo lectura (solo lista los envios pendientes);
    // cada envio se procesa en su propia transaccion (procesarRespuestaPortafirmasEnvio,
    // REQUIRES_NEW). Antes todo iba en una unica transaccion y sin try/catch por envio: un fallo en
    // un documento abortaba el bucle y revertia todos los envios procesados antes en la ejecucion.
    @Transactional(readOnly = true)
    public void consultarRespuestaPortafirmasBatch() throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DOCUMENTO_PTFR_CD_ESTADO_PTFR,
                Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, Operador.NOT_NULL));

        List<DocumentoPtfr> listaEnviosPtfr = this.find(filtros, null);

        if (listaEnviosPtfr != null && !listaEnviosPtfr.isEmpty()) {
            for (DocumentoPtfr documentoPtfr : listaEnviosPtfr) {

                if (Objects.nonNull(documentoPtfr.getDsReferenciaPtfr())
                        && Objects.nonNull(documentoPtfr.getCdArchivoPtfr())) {

                    // VMM 26/10/26 MD-3168: se llama a traves del proxy de Spring para que se
                    // aplique el REQUIRES_NEW; si falla solo se revierte este envio y se sigue
                    // con el siguiente.
                    try {
                        this.documentoPtfrServiceProxy.procesarRespuestaPortafirmasEnvio(
                                documentoPtfr.getIdDocumentoPtfr());
                    } catch (Exception e) {
                        log.error("Error al consultar la respuesta de Portafirmas para idDocumentoPtfr={}",
                                documentoPtfr.getIdDocumentoPtfr(), e);
                    }
                }
            }
        }

    }

    /**
     * VMM 26/10/26 MD-3168: procesa la respuesta de Portafirmas de un unico envio en su propia
     * transaccion (REQUIRES_NEW), de modo que un fallo solo revierte este envio.
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {
            ServiceException.class, RuntimeException.class })
    public void procesarRespuestaPortafirmasEnvio(Integer idDocumentoPtfr)
            throws ServiceException {

        DocumentoPtfr documentoPtfr = this.find(idDocumentoPtfr);

        if (Objects.isNull(documentoPtfr) || Objects.isNull(documentoPtfr.getDsReferenciaPtfr())
                || Objects.isNull(documentoPtfr.getCdArchivoPtfr())) {
            return;
        }

        PtfrConsultaEtapasOut output = this.ptfrAdmeService.consultaEtapasDoc(
                documentoPtfr.getDsReferenciaPtfr(), documentoPtfr.getCdArchivoPtfr().toString());

        // Validamos si tenemos una respuesta correcta
        if (Objects.nonNull(output)
                && Constantes.Portafirmas.RESPUESTA_OK.equals(output.getCodigoRespuesta())
                && Objects.nonNull(output.getDocumento())) {

            String estadoRespuestaEnvioPtfr = output.getDocumento().getEstado();

            // Validamos si tenemos una respuesta de rechazo
            if (StringUtils.isNotBlank(output.getDocumento().getMotivorechazo())
                    && !Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                            .equals(output.getDocumento().getEstado())
                    && !Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                            .equals(output.getDocumento().getEstado())) {
                // actualizamos la respuesta a R de Rechazado
                estadoRespuestaEnvioPtfr = Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO;
                // solo si es rechazo recuperamos el motivo de rechazo
                documentoPtfr.setMotivoRechazo(output.getDocumento().getMotivorechazo());
            }

            this.procesarRespuestaPortafirmas(estadoRespuestaEnvioPtfr, documentoPtfr, output);
        }
    }

    /**
     * Método para validar la respuesta de Portafirmas (respuesta)
     *
     * @param estadoRespuestaEnvioPtfr Estado de respuesta de envío a PTFR
     * @param documentoPtfr Datos del envío a PTFR
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    private void procesarRespuestaPortafirmas(String estadoRespuestaEnvioPtfr,
            DocumentoPtfr documentoPtfr,PtfrConsultaEtapasOut output) throws ServiceException {

        Expediente expediente = this.expedienteDAO.find(documentoPtfr.getIdExpediente());

        //MD-2669 14/04/2026 GIM hemos comentado la parte de que mrie elk estado de exlusion y desistmiento por que ahora hay que enviar tambien por aqui el tramite cuando se pasa a esos estados
//        if (!utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
//                .equals(expediente.getEstado().getIdValor())
//                && !utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.EstadoExpediente.EXCLUIDO)
//                        .equals(expediente.getEstado().getIdValor())) {
        
        

            if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO.equals(estadoRespuestaEnvioPtfr)) {

                this.procesarRespuestaPortafirmasFirmado(documentoPtfr, estadoRespuestaEnvioPtfr,
                        expediente,output);

            } else if (estadoRespuestaEnvioPtfr
                    .equals(Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO)) {

                this.procesarRespuestaPortafirmasRechazado(documentoPtfr, estadoRespuestaEnvioPtfr,
                        expediente);

            }

        //}

    }

    /**
     *
     * @param documentoPtfr
     * @param estadoRespuestaEnvioPtfr
     * @param expediente
     * @throws ServiceException
     */
    private void procesarRespuestaPortafirmasFirmado(DocumentoPtfr documentoPtfr,
            String estadoRespuestaEnvioPtfr, Expediente expediente, PtfrConsultaEtapasOut etapas) throws ServiceException {

        PtfrRecuperaDocFirmadoDTO documentoPtfrFirmadoDTO = this.ptfrAdmeService
                .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                        documentoPtfr.getCdArchivoPtfr().toString());

        if (StringUtils.isNotBlank(documentoPtfrFirmadoDTO.getCodigoArchivo())) {
            documentoPtfr.setCdArchivoRespPtfr(
                    Integer.valueOf(documentoPtfrFirmadoDTO.getCodigoArchivo()));
        }

        String fechaFirma = null;

         // Validamos si el usuario está de alta en NOTE
         this.validarAltaUsuarioNote(documentoPtfr, expediente.getSolicitud().getIdSolicitud());
         for (PtfrEtapas etapa : etapas.getEtapas()) {
             if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO_TEXTO.equals(etapa.getEstado())) {
                 fechaFirma = etapa.getFecha();
             }
         }
    
         if (StringUtils.isNotBlank(fechaFirma)) {
             try {
                 SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                 Date fechaDate = formatter.parse(fechaFirma);
                 
                 documentoPtfr.setFcFirmaPtfr(fechaDate);
             } catch (ParseException e) {
                 log.error("Error al parsear la fecha de firma '{}': {}", fechaFirma, e.getMessage());
             }
         }else {
             documentoPtfr.setFcFirmaPtfr(new Date());
         }
            documentoPtfr.setCdEstadoPtfr(estadoRespuestaEnvioPtfr);
            documentoPtfr.setFcModifFila(new Date());
            documentoPtfr.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            this.update(documentoPtfr);

        if (Objects.nonNull(documentoPtfr.getTramite())
                && Objects.nonNull(documentoPtfr.getTramite().getIdValor())) {

            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DESISTIMIENTO)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.CONCESION_DEFINITIVA)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DENEGACION_PROVISIONAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DENEGACION_DEFINITIVA)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                this.procesarExpedienteAdministrativoBatch(expediente, documentoPtfr,
                        Boolean.FALSE);

            } else if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                this.procesarExpedienteRequisitoBatch(expediente, documentoPtfr, Boolean.FALSE);

            } else {
                log.info("trámite no identificado");
            }

//            if (Constantes.AfirmacionNegacion.N.equals(documentoPtfr.getItUsuAltaNote())) {
//
//                // Como no hay forma de notificar por NOTE, calculamos el plazo
//                // de subsanación
//
//                Solicitud solicitud = this.solicitudDAO
//                        .find(expediente.getSolicitud().getIdSolicitud());
//
//                documentoPtfr.setFcIniPlazoSubsanacion(documentoPtfr.getFcFirmaPtfr());
//                DocumentoPtfrDTO documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE
//                        .apply(documentoPtfr);
//
//                LocalDateTime fcFinSub = this.expedienteService.calcularFechaLimiteSubsanacion(
//                        solicitud.getConvocatoria().getNmDiasSubsanacion(), documentoPtfrDTO);
//
//                documentoPtfr
//                        .setFcFinPlazoSubsanacion(Utils.convertLocalDateToDateInstant(fcFinSub));
//                this.update(documentoPtfr);
//            }

        }

    }

    // GIM 30/08/2024 El nuevo envio a note que vamos a crear, solo lo vamos
    // allamar desde el boton, una llamada por cada registro
    @Transactional(readOnly = false, rollbackFor = {ServiceException.class,
            RuntimeException.class })
    public List<String> procesarEnvioANote(Integer idDocumentoPtfr, Integer idExpediente)
            throws ServiceException {

        List<String> lstCdNote = new ArrayList<String>();

        try {
            // pedimos el id expdiente y el id el documentoptfr
            DocumentoPtfrDTO documentoPtfrDto = this.buscarPorId(idDocumentoPtfr);
            DocumentoPtfr documentoPtfr = DocumentoPtfrToEntity.INSTANCE.apply(documentoPtfrDto);
            ExpedienteDTO expediente = expedienteService.buscarId(idExpediente);

            PtfrRecuperaDocFirmadoDTO documentoPtfrFirmadoDTO = this.ptfrAdmeService
                    .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                            documentoPtfr.getCdArchivoPtfr().toString());

            Solicitud solicitud = this.solicitudDAO.find(expediente.getIdSolicitud());

            byte[] archivoPdf = Base64.decode(documentoPtfrFirmadoDTO.getPdfB64());

            lstCdNote = this.convocatoriaComService.enviarComunicaciones(
                    solicitud.getConvocatoria().getIdConvocatoria(),
                    documentoPtfr.getIdExpediente(), documentoPtfr.getTramite().getIdValor(),
                    documentoPtfr.getIdDocumentoPtfr(), new ByteArrayInputStream(archivoPdf));
        } catch (Exception ex) {
            log.error(ex.toString());
            throw new ServiceException(ex.getMessage());
        }

        return lstCdNote;

    }
    
    public List<String> comprobarAbonados(Integer idDocumentoPtfr, Integer idExpediente)
            throws ServiceException {
        List<String> abonados = new ArrayList<>();
        Integer tipoActorComunicacion;
        Integer tipoActorExpediente;
        Integer tipoPersonaExpediente;
        Integer tipoPersonaComunicacion;
        DocumentoPtfrDTO documentoPtfrDto = this.buscarPorId(idDocumentoPtfr);
        DocumentoPtfr documentoPtfr = DocumentoPtfrToEntity.INSTANCE.apply(documentoPtfrDto);
        ExpedienteDTO expediente = expedienteService.buscarId(idExpediente);
        Solicitud solicitud = this.solicitudDAO.find(expediente.getIdSolicitud());
        ConvocatoriaComDTO convocatoriaComDTO = this.convocatoriaComService.obtener(
                solicitud.getConvocatoria().getIdConvocatoria(),
                documentoPtfr.getTramite().getIdValor());
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                solicitud.getIdSolicitud(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteDTO> listaExp = this.expedienteService.buscar(0, -1, filtros, null);
        for (ExpedienteDTO expedienteDTO : listaExp) {
            tipoActorExpediente = expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO()
                    .getIdValor();
            tipoPersonaExpediente = expedienteDTO.getConvocatoriaActDTO().getTipoPersonaFisJurDTO()
                    .getIdValor();
            if (Objects.nonNull(convocatoriaComDTO.getConvocatoriaActNoteDTO())) {
                tipoActorComunicacion = convocatoriaComDTO.getConvocatoriaActNoteDTO()
                        .getTipoActorDTO().getIdValor();
                tipoPersonaComunicacion = convocatoriaComDTO.getConvocatoriaActNoteDTO()
                        .getTipoPersonaFisJurDTO().getIdValor();
                if (Boolean.TRUE.equals(convocatoriaComDTO.getBlNote())
                        && tipoActorComunicacion.equals(tipoActorExpediente)
                        && tipoPersonaComunicacion.equals(tipoPersonaExpediente)) {
                    String cdNumIden = "";
                    if (expedienteDTO.getRepresentanteLegal() != null && (expediente
                            .getMedioComunicacionDTO() != null
                            && expediente.getMedioComunicacionDTO().getIdValor() != null
                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(
                                    Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))) {
                        cdNumIden = expedienteDTO.getRepresentanteLegal().getCdNumIden();
                    } else {
                        cdNumIden =  expedienteDTO.getInteresadoDTO().getCdNumIden();
                    }
                    String cdAbonado = this.noteAdmeService.recuperarCdAbonado(cdNumIden);
                    if (cdAbonado == null && StringUtils.isBlank(cdAbonado)) {
                        abonados.add(cdNumIden);
                    }
                    
                    List<AtlasFilter> filtrosPtfr = new ArrayList<>();
                    filtrosPtfr
                            .add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_DOC_PTFR_PADRE,
                                    documentoPtfr.getIdDocumentoPtfr(), Operador.EQUALS));
                    filtrosPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA,
                            Operador.NULL));
                    List<DocumentoPtfr> lstDocPtfr = this.find(filtrosPtfr, null);
                    for (DocumentoPtfr docuPtfr : lstDocPtfr) {
                        List<String> destinatarios = this.comprobarAbonados(
                                docuPtfr.getIdDocumentoPtfr(), docuPtfr.getIdExpediente());
                        abonados.addAll(destinatarios);
                    }
                }
            }
        }
        return abonados;
    }

    /**
     *
     * @param documentoPtfr
     * @param estadoRespuestaEnvioPtfr
     * @param expediente
     * @throws ServiceException
     */
    private void procesarRespuestaPortafirmasRechazado(DocumentoPtfr documentoPtfr,
            String estadoRespuestaEnvioPtfr, Expediente expediente) throws ServiceException {

        documentoPtfr.setCdEstadoPtfr(estadoRespuestaEnvioPtfr);
        documentoPtfr.setFcModifFila(new Date());
        documentoPtfr.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

        this.update(documentoPtfr);

        if (Objects.nonNull(documentoPtfr.getTramite())
                && Objects.nonNull(documentoPtfr.getTramite().getIdValor())) {

            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DESISTIMIENTO)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                this.procesarExpedienteAdministrativoBatch(expediente, documentoPtfr, Boolean.TRUE);

            } else if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                this.procesarExpedienteRequisitoBatch(expediente, documentoPtfr, Boolean.TRUE);

            }
        }

    }

    /**
     *
     * @param expediente
     * @param idDocumentoPtfr
     */
    private void procesarExpedienteAdministrativoBatch(Expediente expediente,
            DocumentoPtfr documentoPtfr, Boolean esRespuestaRechazado) throws ServiceException {

        if (Boolean.FALSE.equals(esRespuestaRechazado)) {

//            if (utilConstantes.getGlobalCatalogosValores()
//                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
//                    .equals(documentoPtfr.getTramite().getIdValor())) {

                // Actualizamos los estados de los documentos
//                int existeDocSub = this.actualizarEstadoDocumentoBatch(
//                        documentoPtfr.getIdDocumentoPtfr(),
//                        utilConstantes.getGlobalCatalogosValores()
//                                .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

                // Si se actualizaron registros de documentos actualizamos el
                // estado interno
//                if (existeDocSub > 0) {
//                    this.actualizarEstadoInternoExpedienteBatch(expediente,
//                            utilConstantes.getGlobalCatalogosValores().get(
//                                    Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS),
//                            Boolean.TRUE);
//                }

//            } else 
                
            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.DESISTIMIENTO)
                    .equals(documentoPtfr.getTramite().getIdValor())) {

//                // Actualizamos los estados de los documentos
//                int existeDocSub = this.actualizarEstadoDocumentoBatch(
//                        documentoPtfr.getIdDocumentoPtfr(), utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));
//
//                // Si se actualizaron registros de documentos actualizamos el
//                // estado interno
//                if (existeDocSub > 0) {
                this.actualizarEstadoInternoExpedienteBatch(expediente, utilConstantes
                        .getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO),
                        Boolean.TRUE);

                // Si es expediente solicitante, actualizamos el estado de
                // la solicitud
                this.cambiarEstadoSolicitudPorExpediente(expediente);
                // }
            } else if (utilConstantes.getGlobalCatalogosValores().get(
                    Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                // Actualizamos los estados de los documentos
                this.actualizarEstadoDocumentoBatch(documentoPtfr.getIdDocumentoPtfr(),
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

                this.actualizarEstadoContratoBatch(expediente,
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoContrato.REQUIERE_SUBSANACION_NOTIFICADO));

            } else if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                    .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.CONCESION_PROVISIONAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.CONCESION_DEFINITIVA)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DENEGACION_PROVISIONAL)
                            .equals(documentoPtfr.getTramite().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DENEGACION_DEFINITIVA)
                            .equals(documentoPtfr.getTramite().getIdValor())) {

                this.actualizarEstadoDocumentoBatch(documentoPtfr.getIdDocumentoPtfr(),
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoDocumento.RESOLUCION_NOTIFICADO));

                // VMM 05/10/26 MD-3066: resolucion de modificacion presupuestaria firmada en
                // Portafirmas -> la modificacion queda lista para el envio a Note ("Pendiente Envio
                // Cola Notificaciones"). La pasa a Resuelta mas tarde el batch de lectura de Note.
                if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                        .equals(documentoPtfr.getTramite().getIdValor())
                        && documentoPtfr.getIdPresupuestoModif() != null) {
                    this.cambiarEstadoModificacionPresPlazoOtra(documentoPtfr.getIdPresupuestoModif(),
                            Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA);
                }
            }

        } else {

            // Actualizamos los estados de los documentos
            int existeDocSub = this.actualizarEstadoDocumentoBatch(
                    documentoPtfr.getIdDocumentoPtfr(), utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

            // Si se actualizaron registros de documentos actualizamos el estado
            // interno
            if (existeDocSub > 0) {
                this.actualizarEstadoInternoExpedienteBatch(expediente, utilConstantes
                        .getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA),
                        Boolean.TRUE);

            }

        }
    }

    /**
     *
     * @param expediente
     * @param idDocumentoPtfr
     * @param revertirCalificacion
     */
    private void procesarExpedienteRequisitoBatch(Expediente expediente,
            DocumentoPtfr documentoPtfr, Boolean esRespuestaRechazado) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DOCUMENTO_PTFR_IDDOCUMENTOPTFR,
                documentoPtfr.getIdDocumentoPtfr(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteReq> listaRequisitos = this.expedienteReqDAO.find(filtros, null);

        log.info("listaRequisitos: " + listaRequisitos.size());

        if (!listaRequisitos.isEmpty()) {

            if (Boolean.FALSE.equals(esRespuestaRechazado)) {

                if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                        .equals(documentoPtfr.getTramite().getIdValor())) {

                    log.info("respuesta firmado subsanacion requisito - Exp. "
                            + expediente.getIdExpediente());

                    // Creamos los expedienteDoc para que se adjunten al
                    // subsanar
                    Date date = new Date();
                    for (ExpedienteReq expedienteReq : listaRequisitos) {
                        // Insertamos registro para subir archivo de
                        // justificación
                        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
                        expedienteDoc.setEstado(
                                new CatalogosValores(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoDocumento.SIN_SUBIR)));
                        expedienteDoc.setExpediente(expedienteReq.getExpediente());
                        expedienteDoc.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        expedienteDoc.setTramite(
                                new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)));
                        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);
                        expedienteDoc.setFcAltaFila(date);
                        expedienteDoc = this.expedienteDocDAO.insert(expedienteDoc);
                        this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                                Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

                        log.info("expedienteDoc registrado");

                        // Actualizamos referencia de documento a requisito
                        expedienteReq.setIdExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                        expedienteReq.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        expedienteReq.setFcModifFila(date);
                        this.expedienteReqDAO.update(expedienteReq);
                        this.expedienteReqHistDAO.crearHistorico(expedienteReq);
                        log.info("expedienteReq actualizado");

                    }

                    this.actualizarEstadoInternoExpedienteBatch(expediente,
                            utilConstantes.getGlobalCatalogosValores().get(
                                    Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS),
                            Boolean.FALSE);

                } else if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.EXCLUSION)
                        .equals(documentoPtfr.getTramite().getIdValor())) {

                    log.info("respuesta firmado exclusion requisito - Exp. "
                            + expediente.getIdExpediente());

                    this.actualizarEstadoInternoExpedienteBatch(expediente, utilConstantes
                            .getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO),
                            Boolean.FALSE);

                    // Si es expediente solicitante, actualizamos el estado de
                    // la solicitud

                    // Si es por Concesion de particpante no hay que cambiar el
                    // estado de la solicitud

                    ExpedienteDTO exp = this.expedienteService
                            .buscarId(expediente.getIdExpediente());
                    ConvocatoriaDTO convo = null;
                    List<Solicitud> listaSolicitudes = this.solicitudDAO
                            .buscarPorExpediente(expediente.getIdExpediente());
                    if (!listaSolicitudes.isEmpty()) {
                        convo = this.convocatoriaService.buscarId(
                                listaSolicitudes.get(0).getConvocatoria().getIdConvocatoria());
                    }

                    if (convo != null
                            && convo.getIdTipoConcesion().getIdValor()
                                    .equals(utilConstantes.getGlobalCatalogosValores()
                                            .get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))
                            && (exp.getEsExpedienteParticipante()
                                    || exp.getEsExpedienteCoordinador())) {
                        log.info(
                                "Expediente con concesion participante, no cambiamos el estado de la solicitud, solo el del expediente ");
                    } else {
                        this.cambiarEstadoSolicitudPorExpediente(expediente);
                    }

                }

            } else {

                // Se da de baja a las entradas para subir documento de
                // requisitos
                if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                        .equals(documentoPtfr.getTramite().getIdValor())) {

                    log.info("respuesta rechazado subsanacion requisito - Exp. "
                            + expediente.getIdExpediente());

                    for (ExpedienteReq expedienteReq : listaRequisitos) {
                        if (Objects.nonNull(expedienteReq.getIdExpedienteDoc())) {
                            ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                                    .find(expedienteReq.getIdExpedienteDoc());
                            expedienteDoc.setFcBajaFila(new Date());
                            expedienteDoc.setCdUsuBaja(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                            expedienteDoc = this.expedienteDocDAO.insert(expedienteDoc);
                            this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                                    Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        }
                    }
                } else if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.EXCLUSION)
                        .equals(documentoPtfr.getTramite().getIdValor())) {
                    log.info("respuesta rechazado exclusion requisito - Exp. "
                            + expediente.getIdExpediente());
                }

                this.actualizarEstadoInternoExpedienteBatch(expediente, utilConstantes
                        .getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA),
                        Boolean.FALSE);

            }

        }
    }

    /**
     * Método para actualizar los estados de los documentos cuando ya hay una
     * respuesta de portafirmas
     *
     * @param idDocumentoPtfr Identificador de informe PTFR
     * @param idEstadoFinal Identificador de estado
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    private int actualizarEstadoDocumentoBatch(Integer idDocumentoPtfr, Integer idEstadoFinal) {

        int existenDocSub = 0;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR, idDocumentoPtfr,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> listaDocumentos = this.expedienteDocDAO.find(filtros, null);

        if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
            for (ExpedienteDoc expedienteDoc : listaDocumentos) {
                if (Objects.nonNull(expedienteDoc.getTramite())
                        && Objects.nonNull(expedienteDoc.getTramite().getIdValor())) {
                    expedienteDoc.setEstado(new CatalogosValores(idEstadoFinal));
                    expedienteDoc.setFcModifFila(new Date());
                    expedienteDoc.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                    expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);

                    // Historico
                    this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                            Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

                    existenDocSub++;
                }
            }
        }

        return existenDocSub;

    }

    /**
     *
     * @param expediente
     * @param idEstadoInterno
     * @param esRevAdmin
     * @throws ServiceException
     */
    private void actualizarEstadoInternoExpedienteBatch(Expediente expediente,
            Integer idEstadoInterno, Boolean esRevAdmin) throws ServiceException {

        CatalogosValores estadoInterno = new CatalogosValores();
        estadoInterno.setIdValor(idEstadoInterno);
        estadoInterno
                .setNmConstante(catalogosValoresService.buscarId(idEstadoInterno).getNmConstante());

        if (esRevAdmin.equals(Boolean.TRUE)) {
            expediente.setEstadoRevAdmin(estadoInterno);
        } else {
            expediente.setEstadoRevRequi(estadoInterno);
        }

        Integer idEstadoExpedienteMayor = ExpedienteToEntity.INSTANCE
                .calcularEstadoMayor(expediente);

        expediente.setEstado(new CatalogosValores(idEstadoExpedienteMayor));

        expediente.setEstadoBenef(
                ExpedienteToEntity.INSTANCE.calcularEstadoBenef(idEstadoExpedienteMayor));

        expediente.setFcModifFila(new Date());
        expediente.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

        expediente = this.expedienteDAO.merge(expediente);

        this.expedienteHistDAO.crearHistorico(expediente,
                Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

    }

    /**
     *
     * @param expediente
     * @param idEstadoContratoRevAdmin
     * @throws ServiceException
     */
    private void actualizarEstadoContratoBatch(Expediente expediente,
            Integer idEstadoContratoRevAdmin) throws ServiceException {
//        CatalogosValores estadoInterno = new CatalogosValores();
//        estadoInterno.setIdValor(idEstadoContratoRevAdmin);

        List<ExpedienteContrato> lstExpedienteContrato = this.expedienteContratoDAO
                .findByIdExpediente(expediente.getIdExpediente());
        if (lstExpedienteContrato == null || lstExpedienteContrato.isEmpty())
            return;

        ExpedienteContrato expedienteContrato = this.expedienteContratoService
                .find(lstExpedienteContrato.get(0).getIdExpedienteContrato());

        expedienteContrato.setIdEstadoRevAdmin(new CatalogosValores(idEstadoContratoRevAdmin));

        expedienteContrato.setFcModifFila(new Date());
        expedienteContrato.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

        expedienteContrato = this.expedienteContratoDAO.merge(expedienteContrato);
        this.expedienteContratoHistDAO.crearHistorico(expedienteContrato,
                Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

    }

    /**
     * Obtener documentos a reportar
     *
     * @param idTipoTramite
     * @param idExpediente Identificador de expediente
     * @return Lista de documento
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco como param
    public List<ExpedienteDoc> obtenerDocumentosAReportar(Integer idTipoTramite,
            Integer idExpediente, Integer idExpedienteJustec, Integer idExpedienteJusEco) throws ServiceException {
        
        List<ExpedienteDoc> listaExpedienteDocAux = new ArrayList<>();
        List<ExpedienteDoc> listaExpedienteDocAuxJusEco = new ArrayList<>();
        List<AtlasFilter> filtros = new ArrayList<>();

        ConvocatoriaDTO convo = null;
        List<Solicitud> listaSolicitudes = this.solicitudDAO.buscarPorExpediente(idExpediente);
        if (!listaSolicitudes.isEmpty()) {
            convo = this.convocatoriaService
                    .buscarId(listaSolicitudes.get(0).getConvocatoria().getIdConvocatoria());
        }

        if (convo != null && convo.getIdTipoConcesion() != null
                && convo.getIdTipoConcesion().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoConcesion.CONCESION_SOLICITANTE))) {

            List<AtlasFilter> filtroExpedientes = new ArrayList<>();
            filtroExpedientes.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    idExpediente, Operador.EQUALS));

            filtroExpedientes
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_EXPEDIENTE_PADRE,
                            idExpediente, Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    filtroExpedientes, Operador.OR));

        } else {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    idExpediente, Operador.EQUALS));
        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (!idTipoTramite.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO))
                && (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores() // MD-1979. Se incluye este filtro para que tambien permita nulos en el fica cuando es subsanacion de requisitos.                                 
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)))) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));
        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, Operador.NULL));

        if (idTipoTramite
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))
                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)) // MD-1979.Incluimos el tramite de requerimiento de subsanacion de requisitos
                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL))
                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA))
                //VMM 17/10/25 MD-1586: incluimos el tramite de requerimiento de Subsanacion de JusEco
                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores().get(
                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA))
                || idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA))) {
            List<AtlasFilter> filtroEstados = new ArrayList<>();
            filtroEstados
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION),
                            Operador.EQUALS));
            filtroEstados.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, filtroEstados,
                    Operador.OR));
        }

        List<Integer> lstTramite = new ArrayList<>();
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_PROVISIONAL));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_DEFINITIVA));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DENEGACION_DEFINITIVA));
	lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DESISTIMIENTO));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.DESISTIMIENTO));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.EXCLUSION));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO));
        lstTramite.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO));
        if (lstTramite.contains(idTipoTramite)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTipoTramite,
                    Operador.EQUALS));
        }

        List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA))
                && idExpedienteJustec != null) {

            for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                List<ExpedienteJustecDocDTO> docJustec = this.expedienteJusTecDocService
                        .buscarPorExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                if (idExpedienteJustec.equals(docJustec.get(0).getIdExpedienteJusTec())) {
                    listaExpedienteDocAux.add(expedienteDoc);
                }
            }

            listaExpedienteDoc = listaExpedienteDocAux;
            
        }else if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA))
                && idExpedienteJusEco != null) {
            
            //VMM 17/10/25 MD-1586: incluimos el tramite de subsanacion JusEco para su gestion 

            for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                List<ExpedienteJusEcoDocDTO> docJusEco = this.expedienteJusEcoDocService
                        .buscarPorExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                if (idExpedienteJusEco.equals(docJusEco.get(0).getExpedienteJusEcoDTO().getIdExpedienteJusEco())) {
                    listaExpedienteDocAuxJusEco.add(expedienteDoc);
                }
            }

            listaExpedienteDoc = listaExpedienteDocAux;
        }

        return listaExpedienteDoc;
    }

    /**
     *
     * @param idTipoTramite
     * @param idExpediente
     * @return
     */
    private List<ExpedienteReq> obtenerRequisitosAReportar(Integer idTipoTramite,
            Integer idExpediente) throws ServiceException {

        List<AtlasFilter> filtrosDoc = new ArrayList<>();
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE_OBJ, idExpediente,
                Operador.EQUALS));
        filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (idTipoTramite.equals(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO))) {
            filtrosDoc.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCUMPLE,
                    Constantes.AfirmacionNegacion.N, Operador.EQUALS));
        }

        List<ExpedienteReq> listaExpedienteReq = this.expedienteReqDAO.find(filtrosDoc, null);

//        if (listaExpedienteReq == null || listaExpedienteReq.isEmpty()) {
//            throw new ServiceException("No hay requisitos disponibles para enviar a Portafirmas.");
//        }

        return listaExpedienteReq;
    }

    /**
     *
     */
    @Override
    public List<DocumentoPtfrDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCLECTURA_NOTE, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), DocumentoPtfrToDTO.INSTANCE));
    }

    @Override
    public List<DocumentoPtfrDTO> buscarPorExpedienteTramite(Integer idExpediente,
            Integer nmTramite) throws ServiceException {
        List<Integer> lstTramites = new ArrayList<>();

        Integer idTramite = utilConstantes.getGlobalCatalogosValores().get(nmTramite);
        lstTramites.add(idTramite);

        return this.getDao().obtenerPorExpedienteYTramites(idExpediente, lstTramites);

    }

    /**
     *
     */
    @Override
    public DocumentoPtfrDTO buscarPorId(Integer idDocumentoPtfr) throws ServiceException {
        return DocumentoPtfrToDTO.INSTANCE.apply(this.find(idDocumentoPtfr));
    }

    /**
     *
     */
    @Override
    public List<DocumentoPtfrDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                DocumentoPtfrToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<DocumentoPtfrDTO> obtenerHistoricoNotif(Integer idExpediente)
            throws ServiceException {
        return this.getDao().obtenerListaHistoricoNotif(idExpediente).stream()
                .sorted(Comparator.comparing(DocumentoPtfrDTO::getFcEnvio).reversed())
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @Override
    public List<DocumentoPtfrDTO> obtenerPaginadoHistoricoNotif(
            List<DocumentoPtfrDTO> listaHistorico, int index, int pageSize) {
        List<DocumentoPtfrDTO> listaPaginada = new ArrayList<>();

        if (!listaHistorico.isEmpty()) {
            if (pageSize != -1) {
                int lastIndex = 0;
                if (listaHistorico.size() <= 10) {
                    index = 0;
                    lastIndex = listaHistorico.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaHistorico.size() ? listaHistorico.size()
                            : lastIndex;
                }
                for (int i = index; i < lastIndex; i++) {
                    listaPaginada.add(listaHistorico.get(i));
                }
            } else {
                listaPaginada.addAll(listaHistorico);
            }
        }
        return listaPaginada;
    }

    /**
     *
     */
    @SuppressWarnings("static-access")
    @Override
    public DocumentoPtfrDTO obtenerInformacionExcluido(Integer idExpediente)
            throws ServiceException {
        DocumentoPtfrDTO documentoPtfrDTO = new DocumentoPtfrDTO();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION),
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS)
                        .or(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL)));

        List<DocumentoPtfr> listaPeticiones = this.find(filtros, null);

        if (!listaPeticiones.isEmpty()) {
            DocumentoPtfr documentoPtfr = listaPeticiones.get(0);
            documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);
        }

        return documentoPtfrDTO;
    }

    /**
     *
     */
    @SuppressWarnings("static-access")
    @Override
    public DocumentoPtfrDTO obtenerInformacionDesistido(Integer idExpediente)
            throws ServiceException {
        DocumentoPtfrDTO documentoPtfrDTO = new DocumentoPtfrDTO();

        List<Integer> estadosExpediente = new ArrayList<>();
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO),
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS)
                        .or(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL)));

        List<DocumentoPtfr> listaPeticiones = this.find(filtros, null);

        if (!listaPeticiones.isEmpty()) {
            DocumentoPtfr documentoPtfr = listaPeticiones.get(0);
            documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);
        }

        return documentoPtfrDTO;
    }

    /**
     * 
     * @param idExpediente
     * @param idTramite
     * @return
     */
    @Override
    public Boolean consultarDocumentoPortafirmaPorTramite(Integer idExpediente, Integer idTramite)
            throws ServiceException {
        Boolean rpta = Boolean.FALSE;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<DocumentoPtfr> lstDocPtfr = this.find(filtros, null);
        if (lstDocPtfr != null && !lstDocPtfr.isEmpty()) {
            rpta = Boolean.TRUE;
        }
        return rpta;
    }
    
    
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void procesarConsultarRespuestaAyuda() throws ServiceException {

       Date currentDate = new Date();
       //filtrar por tramite 448 y que tengan estado 244 el expediente, que el estado del note sea leido y la fecha de fin de plazo sea anterior a hoy
       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_NOTE, Constantes.Note.CD_L, Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA)));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FECHA_FIN_PLAZO_SUBSANACION, currentDate, Operador.LESS_THAN));
       
       List<DocumentoPtfr> listaEnviosPtfr = this.find(filtros, null);

       if (listaEnviosPtfr != null && !listaEnviosPtfr.isEmpty()) {
           
           
           for (DocumentoPtfr documentoPtfr : listaEnviosPtfr) {
               
               Expediente expediente = this.expedienteDAO.find(documentoPtfr.getIdExpediente());
               
               if(expediente.getEstado().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST))) {
                   
                   //actualizamos el campo                               
                   // Obtenemos los datos de la solicitud
                   Solicitud solicitud = this.solicitudDAO.find(expediente.getSolicitud().getIdSolicitud());
                  ConvocatoriaDTO  convocatoria = this.convocatoriaService.buscarId(solicitud.getConvocatoria().getIdConvocatoria());
                  
                  if(convocatoria.getIdAceptacionDesistimientoAyuda() != null && convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor() != null &&
                          utilConstantes.getGlobalCatalogosValores().get(Constantes.Cata.ID_ACEPTACION_DESISTIMIE_AYUDA_ACEPTAR).equals(convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor())) { 
                      expediente.setItAceptaAyuda(Constantes.AfirmacionNegacion.N);
                      
                      this.expedienteService.cambiarEstado(documentoPtfr.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(
                              Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO),
                              Constantes.Utiles.COD_USU_BATCH_AUDITORIA);    
                      
                      CatalogosValores estado = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));
                      solicitud.setEstado(estado);
                      this.solicitudDAO.insertOrUpdate(solicitud);
                      
                  }else if ( convocatoria.getIdAceptacionDesistimientoAyuda() != null && convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor() != null &&
                          utilConstantes.getGlobalCatalogosValores().get(Constantes.Cata.ID_ACEPTACION_DESISTIMIE_AYUDA_DESISTIR).equals(convocatoria.getIdAceptacionDesistimientoAyuda().getIdValor())) {
                      expediente.setItAceptaAyuda(Constantes.AfirmacionNegacion.S);
                      
                      ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(documentoPtfr.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(
                              Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO),
                              Constantes.Utiles.COD_USU_BATCH_AUDITORIA);    
                      
                      ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expedienteDTO.getConvocatoriaActDTO().getIdActor());                                                                         
                      CatalogosValores estado = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
                      solicitud.setEstado(estado);
                      this.solicitudDAO.insertOrUpdate(solicitud);
                      
                      SolicitudDTO solicitudDTO = SolicitudToDTO.INSTANCE.apply(solicitud);  
                      
                      if (convocatoriaActDTO != null) {   
                          this.expedienteService.crearOtrosImpresosExpdte(solicitudDTO, convocatoriaActDTO, expedienteDTO,ConvocatoriaToEntity.INSTANCE.apply(convocatoria));
                      }   
                      
                  }  
                                                          
               }
                     
           }
       }

   }

    @Override
    public Boolean consultarDocumentoPortafirmaPorTramites(Integer idExpediente,
            List<Integer> idTramite) throws ServiceException {
        Boolean rpta = Boolean.FALSE;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<DocumentoPtfr> lstDocPtfr = this.find(filtros, null);
        if (lstDocPtfr != null && !lstDocPtfr.isEmpty()) {
            rpta = Boolean.TRUE;
        }
        return rpta;
    }
    
    @Transactional(readOnly = false)
    public int actualizarEstadoDocumentoBatchPublic(Integer idDocumentoPtfr, Integer idEstadoFinal){
        return this.actualizarEstadoDocumentoBatch(idDocumentoPtfr, idEstadoFinal);
    }

    // VMM 06/10/26 MD-3066: version publica (con transaccion de escritura, la clase es readOnly por
    // defecto) para poder llamarla desde EnvioNotificacionesBean al enviar a Note.
    @Override
    @Transactional(readOnly = false)
    public void cambiarEstadoModificacionPresPlazoOtraPublic(Integer idModificacion,
            Integer nmConstanteEstado) throws ServiceException {
        this.cambiarEstadoModificacionPresPlazoOtra(idModificacion, nmConstanteEstado);
    }
    
    @Transactional(readOnly = false)
    public void actualizarEstadoInternoExpedienteBatchPublic(Expediente expediente,
            Integer idEstadoInterno, Boolean esRevAdmin) throws ServiceException{
        this.actualizarEstadoInternoExpedienteBatch(expediente,
                idEstadoInterno, esRevAdmin);
    }
    
    /**
     * Valida y encola la resolución de concesión definitiva sin cambiar el estado del expediente.
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { ServiceException.class, RuntimeException.class })
    public Integer enviarResolucionConcesionDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, byte[] documentoAdjunto,
            Integer idDocPtfrPadre, String usuario) throws ServiceException {
        if (convocatoria == null || convocatoria.getIdConvocatoria() == null
                || idExpedienteDoc == null || documentoAdjunto == null || documentoAdjunto.length == 0
                || idDocPtfrPadre == null || idDocPtfrPadre < 0) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        ExpedienteDoc documento = this.expedienteDocDAO.find(idExpedienteDoc);
        Integer idTramite = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_DEFINITIVA);
        if (idTramite == null || documento == null || documento.getFcBajaFila() != null
                || documento.getExpediente() == null || documento.getCdFica() == null
                || documento.getTipoDocumento() == null || documento.getTramite() == null
                || !this.utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA)
                        .equals(documento.getTipoDocumento().getIdValor())
                || !idTramite.equals(documento.getTramite().getIdValor())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        Expediente expediente = documento.getExpediente();
        if (expediente.getSolicitud() == null
                || expediente.getSolicitud().getFcBajaFila() != null
                || expediente.getSolicitud().getConvocatoria() == null
                || expediente.getSolicitud().getConvocatoria().getFcBajaFila() != null
                || expediente.getFcBajaFila() != null
                || !convocatoria.getIdConvocatoria().equals(
                        expediente.getSolicitud().getConvocatoria().getIdConvocatoria())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        if (Boolean.TRUE.equals(consultarDocumentoPortafirmaPorTramite(expediente.getIdExpediente(), idTramite))) {
            return null;
        }
        Integer estadoPropuesta = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO);
        if (estadoPropuesta == null || expediente.getEstado() == null
                || !estadoPropuesta.equals(expediente.getEstado().getIdValor())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(
                expediente.getIdExpediente(), Boolean.FALSE);
        if (idDocPtfrPadre > 0) {
            DocumentoPtfr padre = this.find(idDocPtfrPadre);
            if (padre == null || padre.getFcBajaFila() != null
                    || !Integer.valueOf(0).equals(padre.getIdDocPtfrPadre())
                    || padre.getTramite() == null
                    || !idTramite.equals(padre.getTramite().getIdValor())) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
        }
        DocumentoPtfr peticion = this.crearEnvioDocumentoPtfr(convocatoria, expedienteDTO,
                idTramite, documentoAdjunto, null, null, null, idDocPtfrPadre);
        if (peticion.getIdDocumentoPtfr() == null) {
            throw new ServiceException(
                    "No se ha podido registrar la resolución en la cola de Portafirmas.");
        }
        documento.setEstado(new CatalogosValores(this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
        documento = this.expedienteDocDAO.merge(documento);
        this.expedienteDocHistDAO.crearHistorico(documento, usuario);
        return peticion.getIdDocumentoPtfr();
    }
    
    @Override
    @Transactional(readOnly = false, rollbackFor = { ServiceException.class, RuntimeException.class })
    public Integer enviarResolucionIndividualDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, Integer tramite, byte[] documentoAdjunto,
            Integer idDocPtfrPadre, String usuario)
            throws ServiceException {
        
        if (!Constantes.Tramite.EXCLUSION.equals(tramite)
                && !Constantes.Tramite.DESISTIMIENTO.equals(tramite)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        return this.enviarResolucionDefinitiva(convocatoria, idExpedienteDoc, tramite,
                documentoAdjunto, idDocPtfrPadre, usuario);
    }

    /** Valida y encola un documento individual o conjunto y actualiza su expediente. */
    private Integer enviarResolucionDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, Integer tramite, byte[] documentoAdjunto,
            Integer idDocPtfrPadre, String usuario) throws ServiceException {
        boolean conjunta = Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite);
        boolean exclusion = Constantes.Tramite.EXCLUSION.equals(tramite);
        if ((!conjunta && !exclusion && !Constantes.Tramite.DESISTIMIENTO.equals(tramite))
                || documentoAdjunto == null || documentoAdjunto.length == 0
                || idDocPtfrPadre == null || idDocPtfrPadre < 0) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        ExpedienteDoc documento = this.expedienteDocDAO.find(idExpedienteDoc);
        Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
        if (documento == null || documento.getFcBajaFila() != null
                || documento.getExpediente() == null || documento.getCdFica() == null
                || documento.getTipoDocumento() == null || documento.getTramite() == null
                || !this.utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA)
                        .equals(documento.getTipoDocumento().getIdValor())
                || !idTramite.equals(documento.getTramite().getIdValor())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        Expediente expediente = documento.getExpediente();
        if (expediente.getFcBajaFila() != null
                || !convocatoria.getIdConvocatoria().equals(
                        expediente.getSolicitud().getConvocatoria().getIdConvocatoria())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        this.validarModoResolucionEnviado(convocatoria.getIdConvocatoria(), conjunta);
        if (this.consultarDocumentoPortafirmaPorTramite(expediente.getIdExpediente(), idTramite)) {
            return null;
        }
        if (conjunta) {
            exclusion = this.utilConstantes.getGlobalCatalogosValores().get(
                    Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO)
                    .equals(expediente.getEstado().getIdValor());
        }
        Integer estadoPropuesta = this.utilConstantes.getGlobalCatalogosValores().get(exclusion
                ? Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO
                : Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO);
        if (!estadoPropuesta.equals(expediente.getEstado().getIdValor())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(
                expediente.getIdExpediente(), Boolean.FALSE);
        if (idDocPtfrPadre > 0) {
            DocumentoPtfr padre = this.find(idDocPtfrPadre);
            if (padre == null || padre.getFcBajaFila() != null
                    || !Integer.valueOf(0).equals(padre.getIdDocPtfrPadre())
                    || padre.getTramite() == null
                    || !idTramite.equals(padre.getTramite().getIdValor())) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
        }
        DocumentoPtfr peticion = this.crearEnvioDocumentoPtfr(convocatoria, expedienteDTO,
                idTramite, documentoAdjunto, null, null, null, idDocPtfrPadre);
        if (peticion.getIdDocumentoPtfr() == null) {
            throw new ServiceException(
                    "No se ha podido registrar la resolución en la cola de Portafirmas.");
        }
        documento.setEstado(new CatalogosValores(this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
        documento = this.expedienteDocDAO.merge(documento);
        this.expedienteDocHistDAO.crearHistorico(documento, usuario);
        this.expedienteService.cambiarEstado(expediente.getIdExpediente(),
                this.utilConstantes.getGlobalCatalogosValores().get(exclusion
                        ? Constantes.EstadoExpediente.EXCLUIDO
                        : Constantes.EstadoExpediente.DESISTIDO), usuario);
        return peticion.getIdDocumentoPtfr();
    }
    
    /** Comprueba los envíos persistidos también para vistas antiguas o peticiones manipuladas. */
    private void validarModoResolucionEnviado(Integer idConvocatoria, boolean conjunta)
            throws ServiceException {
        List<Integer> tramites = this.obtenerTramitesResolucionEnviados(idConvocatoria);
        boolean individualEnviada = tramites.contains(Constantes.Tramite.EXCLUSION)
                || tramites.contains(Constantes.Tramite.DESISTIMIENTO);
        boolean conjuntaEnviada = tramites.contains(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO);
        if (individualEnviada && conjuntaEnviada) {
            throw new ServiceException(
                    "La convocatoria tiene envíos individuales y conjuntos de exclusión/desistimiento. Revise las peticiones registradas.");
        }
        if ((conjunta && individualEnviada) || (!conjunta && conjuntaEnviada)) {
            throw new ServiceException(
                    "El modo de resolución no coincide con los envíos de exclusión/desistimiento registrados en la convocatoria.");
        }
    }
    
    @Override
    @Transactional(readOnly = false, rollbackFor = { ServiceException.class, RuntimeException.class })
    public Integer enviarResolucionConjuntaDefinitiva(ConvocatoriaDTO convocatoria,
            Integer idExpedienteDoc, byte[] documentoAdjunto, Integer idDocPtfrPadre,
            String usuario) throws ServiceException {
        return this.enviarResolucionDefinitiva(convocatoria, idExpedienteDoc,
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO, documentoAdjunto,
                idDocPtfrPadre, usuario);
    }
    
    @Override
    public List<Integer> obtenerTramitesResolucionEnviados(Integer idConvocatoria)
            throws ServiceException {
        if (idConvocatoria == null) {
            throw new ServiceException(Constantes.Error.ERROR_ID_CONVOCATORIA_FALTANTE);
        }
        List<Integer> tramites = new ArrayList<>();
        tramites.add(Constantes.Tramite.EXCLUSION);
        tramites.add(Constantes.Tramite.DESISTIMIENTO);
        tramites.add(Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO);
        try {
            return this.getDao().obtenerTramitesResolucionEnviados(idConvocatoria, tramites);
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }
    }
}
