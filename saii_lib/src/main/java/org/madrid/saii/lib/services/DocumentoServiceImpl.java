package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocEvalToEntity;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.Base64;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio Documento.
 *
 * @author everis
 */
@Slf4j
@Service("documentoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class DocumentoServiceImpl implements DocumentoService {

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /** Servicio de Dato. */
    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;

    /** Servicio de Expediente. */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /** Servicio de documentoPtfr. */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    /** Servicio de ptfrAdme. */
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    /** Servicio de eregAdmeService. */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    /** DAO de ExpedienteDoc. */
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    /** DAO de ExpedienteDocHist. */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /** DAO de ExpedienteReq. */
    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;

    /** DAO de ConvocatoriaTipoDocDAO. */
    @Autowired
    @Qualifier("convocatoriaTipoDocDAO")
    private ConvocatoriaTipoDocDAO convocatoriaTipoDocDAO;

    /** Servicio de expedienteEval */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;
    
    /** Servicio de expedienteJusTecService **/
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    
    /** Servicio de expedienteJusTecService **/
    @Autowired
    @Qualifier("expedienteJusTecDocService")
    private ExpedienteJusTecDocService expedienteJusTecDocService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("convocatoriaDocEvalService")
    private ConvocatoriaDocEvalService convocatoriaDocEvalService;
    
    @Autowired
    @Qualifier("convocatoriaEvalTipoDocService")
    private ConvocatoriaEvalTipoDocService convocatoriaEvalTipoDocService;
    
    @Autowired
    @Qualifier("expedienteDocEvalService")
    private ExpedienteDocEvalService expedienteDocEvalService;
    
    @Autowired
    @Qualifier("evaluacionTipoDocService")
    private EvaluacionTipoDocService evaluacionTipoDocService;
    
    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService convocatoriaTipoDocService;
    
    /**
     *
     */
    @Override
    public DocumentoDTO descargar(Integer idExpedienteDoc) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        ExpedienteDoc expedienteDoc = this.expedienteDocDAO.find(idExpedienteDoc);

        // Si no hay cdFica, buscamos en EREG
        if (Boolean.TRUE.equals(expedienteDoc.getCdFica() != null)) {
            FicaFichero ficaFichero = this.ficaService.recuperar(expedienteDoc.getCdFica());
            documentoDTO.setFicaFichero(ficaFichero);
            documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            documentoDTO.setCdFica(expedienteDoc.getCdFica());
        } else {
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarIdMinimo(expedienteDoc.getExpediente().getIdExpediente());

            documentoDTO.setContenidoFichero(this.eregAdmeService.descargarAnexoPorCodigo(
                    expedienteDTO.getCdEreg(), expedienteDoc.getCdAnexoEreg()));
            documentoDTO.setNombreFichero("Documento-" + expedienteDoc.getCdAnexoEreg() + ".pdf");
        }
        return documentoDTO;
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarHistorico(Integer idExpedienteDocHist) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        ExpedienteDocHist expedienteDocHist = this.expedienteDocHistDAO.find(idExpedienteDocHist);

        // Si no hay cdFica, buscamos en EREG
        if (Boolean.TRUE.equals(expedienteDocHist.getCdFica() != null)) {
            FicaFichero ficaFichero = this.ficaService.recuperar(expedienteDocHist.getCdFica());
            documentoDTO.setFicaFichero(ficaFichero);
            documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            documentoDTO.setCdFica(expedienteDocHist.getCdFica());
        } else {
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarIdMinimo(expedienteDocHist.getIdExpediente());

            documentoDTO.setContenidoFichero(this.eregAdmeService.descargarAnexoPorCodigo(
                    expedienteDTO.getCdEreg(), expedienteDocHist.getCdAnexoEreg()));
            documentoDTO
                    .setNombreFichero("Documento-" + expedienteDocHist.getCdAnexoEreg() + ".pdf");
        }
        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarPlantilla(Integer idConvocatoriaTipoDoc) throws ServiceException {

        if (Objects.isNull(idConvocatoriaTipoDoc)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        DocumentoDTO documentoDTO = new DocumentoDTO();

        ConvocatoriaTipoDoc convocatoriaTipoDoc = this.convocatoriaTipoDocDAO
                .find(idConvocatoriaTipoDoc);

        if (Objects.nonNull(convocatoriaTipoDoc)
                && Objects.nonNull(convocatoriaTipoDoc.getCdFicaPlantilla())) {
            FicaFichero ficaFichero = this.ficaService
                    .recuperar(convocatoriaTipoDoc.getCdFicaPlantilla());
            if (Objects.nonNull(ficaFichero) && Objects.nonNull(ficaFichero.getDatosFichero())) {
                documentoDTO.setFicaFichero(ficaFichero);
                documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
                documentoDTO.setCdFica(convocatoriaTipoDoc.getCdFicaPlantilla());
            }
        }
        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpediente(ExpedienteDTO expedienteDTO,
            Boolean bFica, Integer idTramite, List<ExpedienteDocDTO> lstExpDocDTO) throws ServiceException {

        List<ExpedienteDocDTO> lstExpedienteDocDTO = this.obtenerListaExpedientes(expedienteDTO, idTramite);

        return obtenerListaDocumentoDTO(lstExpDocDTO, lstExpedienteDocDTO, bFica);
    }
    
    /**
    *
    */
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpedienteEval(ExpedienteDTO expedienteDTO,
            Boolean bFica, Integer idTramite, List<ExpedienteDocDTO> lstExpDocDTO)
            throws ServiceException {

        List<ExpedienteDocDTO> lstExpedienteDocDTO = this.obtenerListaExpedientes(expedienteDTO,
                idTramite);

        Integer idConvocatoria = expedienteDTO.getConvocatoriaActDTO().getIdConvocatoria();
        List<ConvocatoriaTipoDocDTO> lstConvTipoDoc = this.convocatoriaTipoDocService
                .buscarPorIdConvocatoria(idConvocatoria);
        for (ConvocatoriaTipoDocDTO convTipoDoc : lstConvTipoDoc) {
            lstExpedienteDocDTO.removeIf(
                    // Los documentos adicionales (IT_ADICIONAL = 'S') se pueden descargar
                    // siempre desde evaluación, independientemente de si su tipo de
                    // documento está configurado para permitir la descarga.
                    x -> !Constantes.AfirmacionNegacion.S.equals(x.getItAdicional())
                            && (x.getTipoDocumentoDTO().getIdValor() == null || x.getTipoDocumentoDTO()
                                    .getIdValor().equals(convTipoDoc.getTipoDocumentoDTO().getIdValor()))
                            && (convTipoDoc.getItPermiDescaEval() == null
                                    || convTipoDoc.getItPermiDescaEval().equals(Boolean.FALSE)));
        }

        return obtenerListaDocumentoDTO(lstExpDocDTO, lstExpedienteDocDTO, bFica);

    }
    
    /**
     * 
     * @param expedienteDTO
     * @param idTramite
     * @return
     */
    private List<ExpedienteDocDTO> obtenerListaExpedientes(ExpedienteDTO expedienteDTO, Integer idTramite) {
        

        //VMM 30/09/24 MD-610: filtrar aqui por el id tramite en caso de que se pase por param segun venga
        // Nos quedamos con los que tienen cdFica y no están dados de baja 
        List<ExpedienteDocDTO> lstExpedienteDocDTO = null;
        if (idTramite != null) {
            lstExpedienteDocDTO = expedienteDTO.getLstExpedienteDocDTO().stream()
                    .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null && x.getTramiteDTO() != null
                    && x.getTramiteDTO().getIdValor() != null && x.getTramiteDTO().getIdValor().equals(idTramite))
                    .collect(Collectors.toList());
        }else {
            lstExpedienteDocDTO = expedienteDTO.getLstExpedienteDocDTO().stream()
                    .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                    .collect(Collectors.toList());
        }
        
        Integer idPendPre = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR);
        Integer idReaSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
        Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
        Integer idEnTraSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
        
        // Quitamos los estados:
        // - Pendiente de presentar -> subido nuevo
        lstExpedienteDocDTO.removeIf(x -> idPendPre.equals(x.getEstadoDTO().getIdValor()));
        // - Requiere subsanacion -> registros marcados como requiere
        // subsanacion
        lstExpedienteDocDTO.removeIf(x -> idReaSub.equals(x.getEstadoDTO().getIdValor()));
        // - Sin subir -> registros habilitados para subsanar requisitos
        lstExpedienteDocDTO.removeIf(x -> idSinSub.equals(x.getEstadoDTO().getIdValor()));
        // - Requiere subsanacion notificado -> registros notificados para
        // subsanar
        lstExpedienteDocDTO.removeIf(x -> idReqSubNot.equals(x.getEstadoDTO().getIdValor()));
        // - En tramite subsanación -> se ha subido una subsanación pero aún no
        // se ha presentado
        lstExpedienteDocDTO.removeIf(x -> idEnTraSub.equals(x.getEstadoDTO().getIdValor()));
        
        return lstExpedienteDocDTO;
    }
    
    /**
     * 
     * @param lstExpDocDTO
     * @param lstExpedienteDocDTO
     * @param bFica
     * @return
     * @throws ServiceException
     */
    private List<DocumentoDTO> obtenerListaDocumentoDTO(List<ExpedienteDocDTO> lstExpDocDTO,
            List<ExpedienteDocDTO> lstExpedienteDocDTO, Boolean bFica) throws ServiceException {
        
        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
        
        if(lstExpDocDTO == null || lstExpDocDTO.isEmpty()) {
            for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {
                Integer cdFica = expedienteDocDTO.getCdFica();

                // Recuperamos de FICA el nombre del fichero
//                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
//                int borrar = 1;
//                String unicodeEscapedFilename = "test"+borrar;
//                borrar++;
////                if (ficaFichero != null) {
////                    String rawFilename = ficaFichero.getNombreFichero();
////                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
////                }
//
//                // Guardamos el id (cdFica) y nombre del fichero en la lista
//                DocumentoDTO documentoDTO = new DocumentoDTO();
//
//                documentoDTO.setCdFica(cdFica);
//                documentoDTO.setNombreFichero(unicodeEscapedFilename);
//                documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());
//
//                // Guardamos el contenido del fichero
//                if (Boolean.TRUE.equals(bFica)) {
//                    documentoDTO
//                            .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
//                }
//
//                // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
//                // los listados de anexos
//                documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
//                        || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));
//
//                lstDocumentosExpediente.add(documentoDTO);

            }
        }else {
            //Recuperamos documentos de la lista facilitada
            for (ExpedienteDocDTO expedienteDocDTO : lstExpDocDTO) {
                Integer cdFica = expedienteDocDTO.getCdFica();

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero=null;
                if(cdFica != null) {
//                    ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                }
                
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }

                // Guardamos el id (cdFica) y nombre del fichero en la lista
                DocumentoDTO documentoDTO = new DocumentoDTO();

                documentoDTO.setCdFica(cdFica);
                documentoDTO.setNombreFichero(unicodeEscapedFilename);
                documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());

                // Guardamos el contenido del fichero
                if (Boolean.TRUE.equals(bFica) && cdFica != null) {
                    documentoDTO
                            .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
                }

                // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
                // los listados de anexos
                documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
                        || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));

                lstDocumentosExpediente.add(documentoDTO);

            }
        }

        return lstDocumentosExpediente;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> obtenerDocumentosSubsanacionSolicitud(Integer idSolicitud,
            Boolean bFica) throws ServiceException {
        List<DocumentoDTO> lstDocumentoDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        List<ExpedienteDTO> lstExpedienteDTO = this.expedienteService.buscar(0, -1, filtros, null);

        for (ExpedienteDTO expedienteDTO : lstExpedienteDTO) {
            lstDocumentoDTO
                    .addAll(this.obtenerDocumentosSubsanacionExpediente(expedienteDTO, bFica));
        }

        return lstDocumentoDTO;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> obtenerDocumentosSubsanacionExpediente(ExpedienteDTO expedienteDTO,
            Boolean bFica) throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();

        List<AtlasFilter> filtrosDocs = new ArrayList<>();
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                expedienteDTO.getIdExpediente(), Operador.EQUALS));
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NOT_NULL));
        filtrosDocs.add(new AtlasFilter("estado.idValor",
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION), Operador.EQUALS));

        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(0, -1, filtrosDocs, null);

        if (Boolean.FALSE.equals(lstExpedienteDoc.isEmpty())) {
            ExpedienteDoc expedienteDoc = lstExpedienteDoc.get(0);

            // Existe el documento en base de datos
            if (expedienteDoc.getCdFica() != null) {
                Integer cdFica = expedienteDoc.getCdFica();

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }

                // Guardamos el id (cdFica) y nombre del fichero
                // en la lista
                DocumentoDTO documentoDTO = new DocumentoDTO();

                documentoDTO.setCdFica(cdFica);
                documentoDTO.setNombreFichero(unicodeEscapedFilename);

                // Guardamos el contenido del fichero
                if (Boolean.TRUE.equals(bFica)) {
                    documentoDTO.setContenidoFichero(
                            this.ficaService.recuperar(cdFica).getDatosFichero());
                }

                lstDocumentosExpediente.add(documentoDTO);
            }
        }

        return lstDocumentosExpediente;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public DocumentoDTO descargarNotificacionSubsanacion(Integer idDocumentoPtfr)
            throws ServiceException {
        DocumentoDTO documentoDTO = null;

        DocumentoPtfrDTO documentoPtfrDTO = this.documentoPtfrService.buscarPorId(idDocumentoPtfr);

        if (documentoPtfrDTO != null) {

            PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService.recuperarDocumentoFirmado(
                    documentoPtfrDTO.getDsReferenciaPtfr(),
                    String.valueOf(documentoPtfrDTO.getCdArchivoPtfr()));

            if (recuperado != null) {

                documentoDTO = new DocumentoDTO();
                documentoDTO.setContenidoFichero(Base64.decode(recuperado.getPdfB64()));
                documentoDTO
                        .setNombreFichero("Notificacion-" + recuperado.getReferencia() + ".pdf");

            }

        }

        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarNotificacionDesistidoExcluido(Integer idExpediente)
            throws ServiceException {

        DocumentoDTO documentoDTO = null;

        ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(idExpediente,
                Boolean.FALSE);

        DocumentoPtfrDTO documentoPtfrDTO = null;

        if (expedienteDTO.getEstadoDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO))) {

            for (ExpedienteDocDTO doc : expedienteDTO.getLstExpedienteDocDTO()) {
                if (doc.getIdDocumentoPtfr() != null) {
                    documentoPtfrDTO = this.documentoPtfrService
                            .buscarPorId(doc.getIdDocumentoPtfr());
                    if (documentoPtfrDTO != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO)
                            .equals(documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        break;
                    }
                }
            }

        } else if (expedienteDTO.getEstadoDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO))) {
            
            Integer idExcl = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION);
            
            Optional<ExpedienteReqDTO> opExpedienteReqDTO = expedienteDTO.getLstExpedienteReqDTO()
                    .stream()
                    .filter(f -> !Constantes.AfirmacionNegacion.S.equals(f.getItCumple())
                            && f.getDocumentoPtfrDTO() != null
                            && f.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null
                            && idExcl.equals(f.getDocumentoPtfrDTO().getTramiteDTO().getIdValor()))
                    .findFirst();

            if (opExpedienteReqDTO.isPresent()) {
                documentoPtfrDTO = opExpedienteReqDTO.get().getDocumentoPtfrDTO();
            }

        }

        if (documentoPtfrDTO != null) {

            PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService.recuperarDocumentoFirmado(
                    documentoPtfrDTO.getDsReferenciaPtfr(),
                    String.valueOf(documentoPtfrDTO.getCdArchivoPtfr()));

            if (recuperado != null) {

                documentoDTO = new DocumentoDTO();
                documentoDTO.setContenidoFichero(Base64.decode(recuperado.getPdfB64()));
                documentoDTO
                        .setNombreFichero("Notificacion-" + recuperado.getReferencia() + ".pdf");

            }

        }

        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpedienteEval(Integer idExpediente, Boolean bFica)
            throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();

        List<ExpedienteEvalDTO> lstCoordinador = this.expedienteEvalService
                .obtenerCoordinadorExpediente(idExpediente, Constantes.TipoCoorEval.COORDINADOR);

        // Nos quedamos con los que tienen cdFica y no están dados de baja
        List<ExpedienteEvalDTO> lstExpedienteEvalDTO = lstCoordinador.stream()
                .filter(x -> x.getFcBajaFila() == null && StringUtils.isNoneBlank(x.getCdFica()))
                .collect(Collectors.toList());

        log.info("Numero de coordinadores con FICA: " + lstExpedienteEvalDTO.size());

        // Si no hay informe de coordinador se recupera los de los evaluadores
        if (lstExpedienteEvalDTO.isEmpty()) {

            List<ExpedienteEvalDTO> lstEvaluador = this.expedienteEvalService
                    .obtenerCoordinadorExpediente(idExpediente, Constantes.TipoCoorEval.EVALUADOR);

            // Nos quedamos con los que tienen cdFica y no están dados de baja
            lstExpedienteEvalDTO = lstEvaluador.stream().filter(
                    x -> x.getFcBajaFila() == null && StringUtils.isNoneBlank(x.getCdFica()))
                    .collect(Collectors.toList());

            log.info("Numero de evaluadores con FICA: " + lstExpedienteEvalDTO.size());
        }

        // Recorremos
        for (ExpedienteEvalDTO expedienteEvalDTO : lstExpedienteEvalDTO) {
            String cdFica = expedienteEvalDTO.getCdFica();

            log.info("cdFica: " + cdFica);

            if (StringUtils.isNotBlank(cdFica)) {

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero = this.ficaService
                        .recuperarMetadatos(Integer.parseInt(cdFica));
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }

                // Guardamos el id (cdFica) y nombre del fichero en la lista
                DocumentoDTO documentoDTO = new DocumentoDTO();

                documentoDTO.setCdFica(Integer.parseInt(cdFica));
                documentoDTO.setNombreFichero(unicodeEscapedFilename);

                log.info("ficaFichero.getNombreFichero(): " + unicodeEscapedFilename);

                // Guardamos el contenido del fichero
                if (Boolean.TRUE.equals(bFica)) {
                    documentoDTO.setContenidoFichero(
                            this.ficaService.recuperar(Integer.parseInt(cdFica)).getDatosFichero());
                    log.info("Recuperamos contenido documento");
                }

                lstDocumentosExpediente.add(documentoDTO);
            }
        }

        log.info("Numero de documentos de salida con FICA: " + lstDocumentosExpediente.size());
        return lstDocumentosExpediente;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> obtenerDocExpedienteEval(Integer idExpedienteEval, String itCoorEva,
            Boolean bFica) throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();

        ExpedienteEvalDTO dto = this.expedienteEvalService
                .obtenerCoorEvalExpedienteEval(idExpedienteEval, itCoorEva);

        // Nos quedamos con los que tienen cdFica y no están dados de baja
        if (dto.getFcBajaFila() == null && StringUtils.isNotBlank(dto.getCdFica())) {

            String cdFica = dto.getCdFica();

            // Recuperamos de FICA el nombre del fichero
            FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(Integer.parseInt(cdFica));
            
            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Guardamos el id (cdFica) y nombre del fichero en la lista
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setCdFica(Integer.parseInt(cdFica));
            documentoDTO.setNombreFichero(unicodeEscapedFilename);

            // Guardamos el contenido del fichero
            if (Boolean.TRUE.equals(bFica)) {
                documentoDTO.setContenidoFichero(
                        this.ficaService.recuperar(Integer.parseInt(cdFica)).getDatosFichero());
            }

            lstDocumentosExpediente.add(documentoDTO);
        }
        return lstDocumentosExpediente;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarFicheroFica(String cdFica) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        if (StringUtils.isBlank(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        try {

            log.info("cdFica: " + cdFica);
            FicaFichero ficaFichero = this.ficaService.recuperar(Integer.parseInt(cdFica));

            documentoDTO.setFicaFichero(ficaFichero);
            documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            documentoDTO.setCdFica(Integer.parseInt(cdFica));

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

        return documentoDTO;
    }

    @Override
    public DocumentoDTO obtenerDocAleg(Integer cdFica) throws ServiceException {
        // Recuperamos de FICA el nombre del fichero
        try {
            DocumentoDTO documentoDTO = new DocumentoDTO();
            FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
            
            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            
                documentoDTO.setCdFica(cdFica);
                documentoDTO.setNombreFichero(unicodeEscapedFilename);
                documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            }
            
            return documentoDTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }
        
    }
    
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpedienteJustec(ExpedienteDTO expedienteDTO,
            Integer idExpedienteJustec, Boolean bFica, Boolean subsanar) throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
        List<ExpedienteDocDTO> lstExpedienteDocDTO = null;
        // Nos quedamos con los que tienen cdFica y no están dados de baja
        if (subsanar.equals(Boolean.TRUE)) {
            lstExpedienteDocDTO = expedienteDTO.getLstExpedienteDocDTO().stream()
                    .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                    .collect(Collectors.toList());
        } else {
            lstExpedienteDocDTO = this.expedienteService.obtenerDocJusTec(idExpedienteJustec)
                    .stream().filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                    .collect(Collectors.toList());

            // Quitamos los estados:
            // VMM MD-913 30/10/24: comentamos esto porque no aparecen los
            // nombres de los docs Pdtes de presentar JCT en el recibo de
            // presentacion
            // - Pendiente de presentar -> subido nuevo
//               lstExpedienteDocDTO.removeIf(x -> Constantes.EstadoDocumento.PENDIENTE_PRESENTAR
//                       .equals(x.getEstadoDTO().getIdValor()));
            
            
            Integer idReaSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
            Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
            Integer idEnTraSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
            
            // - Requiere subsanacion -> registros marcados como requiere
            // subsanacion
            lstExpedienteDocDTO.removeIf(x -> idReaSub.equals(x.getEstadoDTO().getIdValor()));
            // - Sin subir -> registros habilitados para subsanar requisitos
            lstExpedienteDocDTO.removeIf(x -> idSinSub.equals(x.getEstadoDTO().getIdValor()));
            // - Requiere subsanacion notificado -> registros notificados para subsanar
            lstExpedienteDocDTO.removeIf(x -> idReqSubNot.equals(x.getEstadoDTO().getIdValor()));
            // - En tramite subsanación -> se ha subido una subsanación pero aún no se ha presentado
            lstExpedienteDocDTO.removeIf(x -> idEnTraSub.equals(x.getEstadoDTO().getIdValor()));
        }

        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {

            Integer cdFica = expedienteDocDTO.getCdFica();

            // Recuperamos de FICA el nombre del fichero
            FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
            
            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Guardamos el id (cdFica) y nombre del fichero en la lista
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setCdFica(cdFica);
            documentoDTO.setNombreFichero(unicodeEscapedFilename);
            documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());
            log.info("Guardamos el contenido del fichero con bFica:" + bFica);
            // Guardamos el contenido del fichero
            if (Boolean.TRUE.equals(bFica)) {
                documentoDTO
                        .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
            }

            // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
            // los listados de anexos
            documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
                    || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));

            lstDocumentosExpediente.add(documentoDTO);

        }

        log.info(" Total ficheros anexos al expediente a descargar fichero:"
                + lstDocumentosExpediente.size());
        return lstDocumentosExpediente;
    }
    
    @SuppressWarnings("unused")
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpedienteContrato(ExpedienteDTO expedienteDTO,
            Boolean bFica,List<ExpedienteDocDTO> lstExpedienteDocDTO) throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
        
     // Nos quedamos con los que tienen cdFica y no están dados de baja
        if(lstExpedienteDocDTO != null && !lstExpedienteDocDTO.isEmpty()) {
            List<ExpedienteDocDTO> lstExpedienteDocDTOv2 = lstExpedienteDocDTO.stream()
                    .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                    .collect(Collectors.toList());
            
            for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTOv2) {
                Integer cdFica = expedienteDocDTO.getCdFica();

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }

                // Guardamos el id (cdFica) y nombre del fichero en la lista
                DocumentoDTO documentoDTO = new DocumentoDTO();

                documentoDTO.setCdFica(cdFica);
                documentoDTO.setNombreFichero(unicodeEscapedFilename);
                documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());

                // Guardamos el contenido del fichero
                if (Boolean.TRUE.equals(bFica)) {
                    documentoDTO.setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
                    
                }

                // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
                // los listados de anexos
                documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
                        || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));

                lstDocumentosExpediente.add(documentoDTO);

            }
            
        }
        return lstDocumentosExpediente;

    }

    @Override
    public DocumentoDTO descargarExpedienteEvalJustec(Integer cdFica) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        if (Boolean.TRUE.equals(cdFica != null)) {
            FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
            documentoDTO.setFicaFichero(ficaFichero);
            documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            documentoDTO.setCdFica(cdFica);
        }
        return documentoDTO;
    }

    @Override
    public DocumentoDTO descargarExpedienteDocJustec(Integer idExpedienteDoc)
            throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        ExpedienteDoc expedienteDoc = this.expedienteDocDAO.find(idExpedienteDoc);

        // Si no hay cdFica, buscamos en EREG
        if (Boolean.TRUE.equals(expedienteDoc.getCdFica() != null)) {
            FicaFichero ficaFichero = this.ficaService.recuperar(expedienteDoc.getCdFica());
            documentoDTO.setFicaFichero(ficaFichero);
            documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
            documentoDTO.setCdFica(expedienteDoc.getCdFica());
        } else {
            List<ExpedienteJustecDocDTO> expedienteJusTecDocDTO = expedienteJusTecDocService
                    .buscarPorExpedienteDoc(idExpedienteDoc);
            ExpedienteJusTec expedienteJusTec = expedienteJusTecService
                    .buscarIdExpJustec(expedienteJusTecDocDTO.get(0).getIdExpedienteJusTec());
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                    .equals(expedienteDoc.getTramite().getIdValor())) {
                documentoDTO.setContenidoFichero(this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteJusTec.getCdEregPresent(), expedienteDoc.getCdAnexoEreg()));
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA)
                    .equals(expedienteDoc.getTramite().getIdValor())) {
                documentoDTO.setContenidoFichero(this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteJusTec.getCdEregSubsana(), expedienteDoc.getCdAnexoEreg()));
            }
            documentoDTO.setNombreFichero("Documento-" + expedienteDoc.getCdAnexoEreg() + ".pdf");
        }
        return documentoDTO;
    }
    
    
    @Override
    public List<DocumentoDTO> obtenerDocumentosExpModifPresupPlazosOtros(
            ExpedienteDTO expedienteDTO, List<ExpedienteDocDTO> lstExpedienteDocDTO,
            Boolean subsanar) throws ServiceException {

        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
        lstExpedienteDocDTO = lstExpedienteDocDTO.stream()
                .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                .collect(Collectors.toList());
        
        Integer idReaSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
        Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
        Integer idEnTraSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
        
        // Nos quedamos con los que tienen cdFica y no están dados de baja
        if (subsanar.equals(Boolean.FALSE)) {
            lstExpedienteDocDTO.removeIf(x -> idReaSub.equals(x.getEstadoDTO().getIdValor()));
            // - Sin subir -> registros habilitados para subsanar requisitos
            lstExpedienteDocDTO.removeIf(x -> idSinSub.equals(x.getEstadoDTO().getIdValor()));
            // - Requiere subsanacion notificado -> registros notificados para subsanar
            lstExpedienteDocDTO.removeIf(x -> idReqSubNot.equals(x.getEstadoDTO().getIdValor()));
            // - En tramite subsanación -> se ha subido una subsanación pero aún no se ha presentado
            lstExpedienteDocDTO.removeIf(x -> idEnTraSub.equals(x.getEstadoDTO().getIdValor()));
        }

        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {

            Integer cdFica = expedienteDocDTO.getCdFica();

            // Recuperamos de FICA el nombre del fichero
            FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
            
            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Guardamos el id (cdFica) y nombre del fichero en la lista
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setCdFica(cdFica);
            documentoDTO.setNombreFichero(unicodeEscapedFilename);
            documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());
            log.info("Guardamos el contenido del fichero con bFica:" + cdFica);
            // Guardamos el contenido del fichero
            if (Boolean.TRUE.equals(cdFica != null)) {
                documentoDTO
                        .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
            }

            // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
            // los listados de anexos
            documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
                    || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));

            lstDocumentosExpediente.add(documentoDTO);

        }

        log.info(" Total ficheros anexos al expediente a descargar fichero:"
                + lstDocumentosExpediente.size());
        return lstDocumentosExpediente;
    }

    @Override
    public DocumentoDTO descargarNotificacionResolucionProvisional(Integer idExpediente)
            throws ServiceException {
       
        DocumentoDTO documentoDTO = null;
        DocumentoPtfrDTO documentoPtfrDTO = null;
  
        List<Integer> lstTramites = new ArrayList<>();
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL));
        List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = this.documentoPtfrService.getDao()
                .obtenerPorExpedienteYTramites(idExpediente,
                        lstTramites);
        if(!lstDocumentoPtfrDTO.isEmpty()) {
            for(DocumentoPtfrDTO docPTFR:lstDocumentoPtfrDTO) {
                if(docPTFR.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL))) {
                    documentoPtfrDTO=docPTFR;
                    break;
                }
            }
            
            if(documentoPtfrDTO == null) {
                documentoPtfrDTO=lstDocumentoPtfrDTO.get(0);
            }
            
        }

        if (documentoPtfrDTO != null) {

            PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService.recuperarDocumentoFirmado(
                    documentoPtfrDTO.getDsReferenciaPtfr(),
                    String.valueOf(documentoPtfrDTO.getCdArchivoPtfr()));

            if (recuperado != null) {

                documentoDTO = new DocumentoDTO();
                documentoDTO.setContenidoFichero(Base64.decode(recuperado.getPdfB64()));
                documentoDTO
                        .setNombreFichero("Notificacion-" + recuperado.getReferencia() + ".pdf");

            }

        }

        return documentoDTO;
    }

    @Override
    public DocumentoDTO descargarNotificacionResolucionDefinitiva(Integer idExpediente)
            throws ServiceException {
        DocumentoDTO documentoDTO = null;
        DocumentoPtfrDTO documentoPtfrDTO = null;
  
        List<Integer> lstTramites = new ArrayList<>();
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO));
        List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = this.documentoPtfrService.getDao()
                .obtenerPorExpedienteYTramites(idExpediente,
                        lstTramites);
        if(!lstDocumentoPtfrDTO.isEmpty()) {
            for(DocumentoPtfrDTO docPTFR:lstDocumentoPtfrDTO) {
                if(docPTFR.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO))) {
                    documentoPtfrDTO=docPTFR;
                    break;
                }
            }
            
            if(documentoPtfrDTO == null) {
                documentoPtfrDTO=lstDocumentoPtfrDTO.get(0);
            }
            
        }

        if (documentoPtfrDTO != null) {

            PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService.recuperarDocumentoFirmado(
                    documentoPtfrDTO.getDsReferenciaPtfr(),
                    String.valueOf(documentoPtfrDTO.getCdArchivoPtfr()));

            if (recuperado != null) {

                documentoDTO = new DocumentoDTO();
                documentoDTO.setContenidoFichero(Base64.decode(recuperado.getPdfB64()));
                documentoDTO
                        .setNombreFichero("Notificacion-" + recuperado.getReferencia() + ".pdf");

            }

        }

        return documentoDTO;
    }

    @Override
    public List<DocumentoDTO> obtenerDocumentosExpedienteJusEco(List<ExpedienteDocDTO> listaDocs) throws ServiceException {
        List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
        List<ExpedienteDocDTO> lstExpedienteDocDTO = null;
        // Nos quedamos con los que tienen cdFica y no están dados de baja
        
            lstExpedienteDocDTO = listaDocs
                    .stream().filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                    .collect(Collectors.toList());

            Integer idReaSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
            Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
            Integer idEnTraSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION);
            
            // - Requiere subsanacion -> registros marcados como requiere
            // subsanacion
            lstExpedienteDocDTO.removeIf(x -> idReaSub.equals(x.getEstadoDTO().getIdValor()));
            // - Sin subir -> registros habilitados para subsanar requisitos
            lstExpedienteDocDTO.removeIf(x -> idSinSub.equals(x.getEstadoDTO().getIdValor()));
            // - Requiere subsanacion notificado -> registros notificados para subsanar
            lstExpedienteDocDTO.removeIf(x -> idReqSubNot.equals(x.getEstadoDTO().getIdValor()));
            // - En tramite subsanación -> se ha subido una subsanación pero aún no se ha presentado
            lstExpedienteDocDTO.removeIf(x -> idEnTraSub.equals(x.getEstadoDTO().getIdValor()));
        

        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {

            Integer cdFica = expedienteDocDTO.getCdFica();

            // Recuperamos de FICA el nombre del fichero
            FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
            
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Guardamos el id (cdFica) y nombre del fichero en la lista
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setCdFica(cdFica);
            documentoDTO.setNombreFichero(unicodeEscapedFilename);
            documentoDTO.setTipoDoc(expedienteDocDTO.getTipoDocumentoDTO().getDsValor());
            documentoDTO
            .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());

            // Marcamos si está en EREG. Nos sirve para filtrar luego al generar
            // los listados de anexos
            documentoDTO.setEnEreg(StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEreg())
                    || StringUtils.isNotBlank(expedienteDocDTO.getCdAnexoEregPadre()));

            lstDocumentosExpediente.add(documentoDTO);

        }

        log.info(" Total ficheros anexos al expediente a descargar fichero:"
                + lstDocumentosExpediente.size());
        return lstDocumentosExpediente;
    }

    @Override
    public DocumentoDTO subir(DocumentoDTO documentoDTO) throws ServiceException {
        // Validacion
        if (Objects.isNull(documentoDTO) || Objects.isNull(documentoDTO.getContenidoFichero())
                || Objects.isNull(documentoDTO.getFicaMetadatos())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // Subimos el fichero a FICA
        documentoDTO.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        FicaFichero fichero = this.ficaService.guardar(documentoDTO.getContenidoFichero(),
                documentoDTO.getFicaMetadatos());
        documentoDTO.setFicaFichero(fichero);
        documentoDTO.setCdFica(fichero.getIdInterno());
        documentoDTO.setNombreFichero(fichero.getNombreFichero());

        return documentoDTO;
       
    }

    @Override
    public List<ConvocatoriaDocEvalDTO> obtenerDocumentoConvocatoria(
            ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException {
        return this.convocatoriaDocEvalService.obtenerDocumentosConvocatoriaEval(convocatoriaDocEvalDTO);
        
    }

    @Override
    public ConvocatoriaDocEvalDTO guardarDocumentoConvocatoria(
            ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO) throws ServiceException {
        return this.convocatoriaDocEvalService.guardar(convocatoriaDocEvalDTO);
    }

    @Override
    public List<ConvocatoriaEvalTipoDocDTO> obtenerTipoDocumentos(Integer nivel, Integer idConvocatoria)
            throws ServiceException {
        return this.convocatoriaEvalTipoDocService.obtenerListaConvocatoriaEvalTipoDocPorNivel(nivel, idConvocatoria);
    }
    
    @Override
    public DocumentoDTO descargarPlantillaConEvalTipoDoc(Integer idConvocatoriaTipoDoc) throws ServiceException {

        if (Objects.isNull(idConvocatoriaTipoDoc)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        DocumentoDTO documentoDTO = new DocumentoDTO();

        ConvocatoriaEvalTipoDoc convocatoriaTipoDoc = this.convocatoriaEvalTipoDocService
                .find(idConvocatoriaTipoDoc);

        if (Objects.nonNull(convocatoriaTipoDoc)
                && Objects.nonNull(convocatoriaTipoDoc.getCdFicaPlantilla())) {
            FicaFichero ficaFichero = this.ficaService
                    .recuperar(convocatoriaTipoDoc.getCdFicaPlantilla());
            if (Objects.nonNull(ficaFichero) && Objects.nonNull(ficaFichero.getDatosFichero())) {
                documentoDTO.setFicaFichero(ficaFichero);
                documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
                documentoDTO.setCdFica(convocatoriaTipoDoc.getCdFicaPlantilla());
            }
        }
        return documentoDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalDTO guardarDocumentoExpedienteEval(
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException {
        return this.expedienteDocEvalService.guardar(expedienteEvalDTO);
        
    }

    @Override
    public List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException {
        return this.expedienteDocEvalService.obtenerDocumentoExpedienteEval(expedienteEvalDTO);
    }

    @Override
    public List<EvaluacionTipoDocDTO> obtenerEvalTipoDocumentosInforme(Integer idInforme)
            throws ServiceException {
       return this.evaluacionTipoDocService.getDao().obtenerTipoDocumentosEvaluacion(idInforme);
    }

    @Override
    @Transactional(readOnly = false)
    public List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(
            List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO) throws ServiceException {
       
        if(listExpedienteDocEvalDTO != null && !listExpedienteDocEvalDTO.isEmpty()) {
            for(ExpedienteDocEvalDTO doc: listExpedienteDocEvalDTO ) {
                this.expedienteDocEvalService.getDao().insertOrUpdate(ExpedienteDocEvalToEntity.INSTANCE.apply(doc));
                
            }
        }
        
       return listExpedienteDocEvalDTO; 
        
    }

   
}
