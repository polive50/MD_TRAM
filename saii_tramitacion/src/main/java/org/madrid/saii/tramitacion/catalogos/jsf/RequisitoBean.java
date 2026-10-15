package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.services.CataService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaCauService;
import org.madrid.saii.lib.services.ConvocatoriaGestorService;
import org.madrid.saii.lib.services.ConvocatoriaReqService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioNotificacionesPortafirmasService;
import org.madrid.saii.lib.services.ExpedienteDocHistService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteReqService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.eadm.note.domain.NoteExisteNifOut;
import atlas.eadm.services.NoteService;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nttdada
 *
 */
@SuppressWarnings("unused")
@Slf4j
@Getter
@Setter
@Component("requisitoBean")
@Scope("request")
public class RequisitoBean extends CatalogoBean<ExpedienteReq, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * Atributos para el documento de envio de portafirmas
     */
    private DocumentoPtfrDTO documentoPtfrDTO;

    /**
     * dto de formularioDocExternoDTO
     */
    private ExpedienteDocDTO formularioDocExternoDTO;

    /**
     * Identificador de expediente seleccionado
     */
    private Integer idExpediente;

    /**
     * Lista de expedientes asociados al expediente
     */
    private List<ExpedienteReqDTO> lstExpedienteReq;

    /**
     * Indicador para consultar si existe un envío a PTFR subsanacion en proceso
     */
    private Boolean existeEnvioPtfrSubsanacionEnProceso;

    /**
     * Indicador para consultar si existe un envío a PTFR subsanacion
     */
    private Boolean existeEnvioPtfrSubsanacionFirmado;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrExclusionEnProceso;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion en cola
     */
    private Boolean existeEnvioPtfrExclusionEnCola;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrExclusionFirmado;

    /**
     * Indicador para consultar si existe un documento subsanacion
     */
    private Boolean existeDocumentoSubsanacion;
    
    /**
     * Indicador para consultar si existe un documento subsanacion cumple
     */
    private Boolean existeDocumentoSubsanacionCumple;
    
    /**
     * Indicador si existe Cumple Pendiente Rev Req
     */
    private Boolean existeCumplePendienteRevReq;

    /**
     * Indicador para consultar si existe un envío a PTFR en cola
     */
    private Boolean existeEnvioPtfrEnCola;
    
    /**
     * Indicador para requisitos que no cumple, pero no tiene observacion
     */
    private Boolean existeNoCumpleSinObs;

    /**
     * Indicador para verificar si el usuario esta dado de alta
     */
    private Boolean usuarioDadoAlta;

    /**
     * Indicador para determinar el colo de fondo del plazo de subsanacion
     */
    private Boolean plazoSuperadoTramitePendiente;

    /**
     * Cantidad total de registros de requisitos
     */
    private Integer totalListaRequisitos;

    /**
     * Identificador de panel de formulario requisitos a refrescar
     */
    private String formularioRequisitos = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelRequisitoSolapa";

    /**
     * Identificador de campo estado de expediente
     */
    private String campoEstadoExpediente = "convocatoriaForm:contenedorSolapas:dsEstadoExpedienteSel";

    /**
     * Identificador de solapa de requisitos
     */
    private String solapaRequisitos = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";

    private String botonFinalizar = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnFinalizarRevRequi";

    private String botonExclusion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnEnviarExcluirPortafirmas";
    
    private String botonExcluir = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnExcluir";
    
    private String botonDescargarExclusion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnDescargarExcluir";

    private String botonSubsanar = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:btnAgregarDocumentoSubsanar";

//    private String textoObservacionesSubsanacionReq;

    private String textoObservacionesCiu;

    private Integer idConvocatoria;
    
    private Integer idEstadoConvocatoria;

    /**
     * Lista de expedientes asociados al expediente
     */
    private List<ExpedienteDocDTO> lstExpedienteDoc;

    /**
     * Cantidad total de registros de Alegaciones
     */
    private Integer totalListaAlegaciones;

    private Boolean mostrarAceptarAlegaciones = Boolean.FALSE;

    private Boolean mostrarRechazarAlegaciones = Boolean.FALSE;

    private ExpedienteDocDTO expedienteDocDTO;

    private UploadedFile archivoSubirSubsanacionReq;

    private UploadedFile archivoSubirExclusion;
    
    
    
    private Boolean activarBotonFinalizar;
    
    private Boolean activarBotonAgregar;
    
    private Boolean activarBotonDescaregar;
    

    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio ExpedienteReqService
     */
    @Autowired
    @Qualifier("expedienteReqService")
    private ExpedienteReqService expedienteReqService;

    /**
     * Servicio ExpedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio ExpedienteDocService
     */
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;

    /**
     * Servicio ExpedienteDocHistService
     */
    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;

    /**
     * Servicio FicaService
     */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /**
     * Servicio DocumentoPtfrService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    /**
     * Servicio PtfrAdmeService
     */
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    /**
     * Servicio NOTE
     */
    @Autowired
    @Qualifier("noteService")
    private NoteService noteService;

    /** DAO de Solicitud. */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    @Autowired
    @Qualifier("cataService")
    private CataService cataService;

    @Autowired
    @Qualifier("EnvioNotificacionesPortafirmasService")
    private EnvioNotificacionesPortafirmasService envioNotificacionesPortafirmasService;

    @Autowired
    @Qualifier("convocatoriaService")
    @Setter
    @Getter
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("convocatoriaCauService")
    private ConvocatoriaCauService convocatoriaCauService;

    @Autowired
    @Qualifier("convocatoriaReqService")
    private ConvocatoriaReqService convocatoriaReqService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;
    
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    @Autowired
    @Qualifier("convocatoriaGestorService")
    private ConvocatoriaGestorService convocatoriaGestorService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////

    private ExpedienteBean expedienteBean;

    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor de la clase
     */
    public RequisitoBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad Dato
     *
     * @return entidad de tipo Dato
     */
    @Override
    public ExpedienteReq crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.inicializarExpedienteDoc();
    }

    /**
     * Metodo que inicializa a las Documentos
     *
     */
    public void inicializarExpedienteDoc() {
        this.expedienteDocDTO = new ExpedienteDocDTO();
        this.lstExpedienteReq = new ArrayList<>();
    }

    /**
     * Recupera el listado de entidades ExpedienteReqDTO que cumple con los
     * criterios de búsqueda. Devuelve las entidades situadas entre las
     * posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Asignatura
     */
  //  @SuppressWarnings("unchecked")
//    public AtlasListAndCount<ExpedienteReqDTO> obtenerListaRequisitoExpediente(final int index,
//            final int pageSize, Object orderBean, Object filtros) {
//        
//        AtlasListAndCount<ExpedienteReqDTO> listado = new AtlasListAndCount<>();
//        List<ExpedienteReqDTO> listadoAux = new ArrayList<>();
//        List<ExpedienteDTO> lstExpedienteEstruc = new ArrayList<>();
//
//        this.lstExpedienteReq = new ArrayList<>();
//        try {
//            if (this.idExpediente != null) {

                /*
                 * ConvocatoriaDTO convocatoriaDTO =
                 * this.expedienteBean.getConvocatoriaDTO(); if(convocatoriaDTO
                 * == null || convocatoriaDTO.getIdConvocatoria() == null) {
                 * convocatoriaDTO =
                 * this.convocatoriaService.buscarId(this.idConvocatoria); }
                 * 
                 * if(convocatoriaDTO != null &&
                 * convocatoriaDTO.getItAlegacion().equals(Constantes.
                 * AfirmacionNegacion.S)) { lstExpedienteEstruc =
                 * this.expedienteService
                 * .obtenerExpedienteEstructuradoSinActorParticipante(this.
                 * expedienteBean.getExpedienteDTO().getIdSolicitud(),
                 * Constantes.AfirmacionNegacion.N, this.idExpediente);
                 * 
                 * if(convocatoriaDTO.getIdTipoConcesion().getNmConstante().
                 * equals(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {
                 * lstExpedienteEstruc.removeIf(x ->
                 * Boolean.FALSE.equals(x.getEsExpedienteParticipante())); }
                 * 
                 * 
                 * for (ExpedienteDTO expDTO : lstExpedienteEstruc) {
                 * 
                 * this.setExisteEnvioPtfrEnCola(Boolean.FALSE); List<AtlasSort>
                 * orden = (List<AtlasSort>) orderBean; List<AtlasFilter>
                 * filtrosEntrada = (List<AtlasFilter>) filtros;
                 * filtrosEntrada.add(new
                 * AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                 * expDTO.getIdExpediente(), Operador.EQUALS));
                 * filtrosEntrada.add(new
                 * AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                 * this.lstExpedienteReq =
                 * this.expedienteReqService.buscar(index, pageSize,
                 * filtrosEntrada, orden);
                 * 
                 * for (ExpedienteReqDTO ExpedienteReqDTOAux :
                 * this.lstExpedienteReq) {
                 * ExpedienteReqDTOAux.getExpedienteDTO().setCdExpediente(expDTO
                 * .getCdExpediente()); listadoAux.add(ExpedienteReqDTOAux);
                 * filtrosEntrada.clear(); } }
                 * 
                 * listado.setResultados(listadoAux);
                 * listado.setTotal(listadoAux.size()); }else {
                 */
//                this.setExisteEnvioPtfrEnCola(Boolean.FALSE);
//                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
//                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;
//                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
//                        this.idExpediente, Operador.EQUALS));
//                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//                this.lstExpedienteReq = this.expedienteReqService.buscar(index, pageSize,
//                        filtrosEntrada, orden);
//
//                listado.setResultados(this.lstExpedienteReq);
//                listado.setTotal(this.expedienteReqService.count(filtrosEntrada));
////                }
//
//                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());
//
//                this.estaDadoAltaUsuario(this.expedienteBean.getExpedienteDTO());

//                StringBuilder cadenaobservacion = new StringBuilder();
//
//                for (ExpedienteReqDTO dto : listado.getResultados()) {
//                    
//                    if(dto.getItCumple() != null && dto.getItCumple().equals("N") && dto.getTlObservaciones() != null) {
//                        cadenaobservacion.append(dto.getTlObservaciones()+ "\n");    
//                    }
//                    
//                  }
//  
//                if(!cadenaobservacion.toString().isEmpty()) {
//                    this.textoObservacionesSubsanacionReq = cadenaobservacion.toString();
//                }
             // En el Bean, al cargar la lista
//                for (ExpedienteReqDTO item : listado.getResultados()) {
//                    // Ejecutas la lógica pesada una sola vez por fila
//                    item.setEditable(habilitarCalificacion(this.isModoConsulta(), this.idEstadoConvocatoria,item)); 
//                }
//               
//                
//                calcularEstadoBotonesAccion();
//                
//               
//                         
//            }
//        } catch (ServiceException exc) {
//            log.error(ConstantesWeb.Err.ERROR_LISTADO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
//        }
//        return listado;
//    }
    
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteReqDTO> obtenerListaRequisitoExpediente(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        
        AtlasListAndCount<ExpedienteReqDTO> listado = new AtlasListAndCount<>();
        
        // --- 1. BLOQUE DE CONTROL PARA AJAX / CACHE ---
        // Si ya tenemos resultados y es un clic de AJAX (radio button), devolvemos lo que hay
        boolean esAjax = javax.faces.context.FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
        
        
        
        
        
        if (esAjax && this.lstExpedienteReq != null && !this.lstExpedienteReq.isEmpty() 
                && this.idExpediente.equals(this.lstExpedienteReq.get(0).getExpedienteDTO().getIdExpediente())) {
            log.info("AJAX detectado: Devolviendo lista de memoria para evitar re-calculos de 19s");
            listado.setResultados(this.lstExpedienteReq);
            // El total lo sacamos del tamaño de la lista actual para no hacer un count() a BD
            listado.setTotal(this.lstExpedienteReq.size()); 
            // Cálculo de editabilidad fila por fila           
                try {
                    for (ExpedienteReqDTO item : listado.getResultados()) {
                    item.setEditable(habilitarCalificacion(this.isModoConsulta(), this.idEstadoConvocatoria, item));
                    calcularEstadoBotonesAccion(); 
                    }
                } catch (ServiceException e) {

            }
            return listado;
        }
        // ----------------------------------------------

        this.lstExpedienteReq = new ArrayList<>();
        try {
            if (this.idExpediente != null) {
                this.setExisteEnvioPtfrEnCola(Boolean.FALSE);
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;
                
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                        this.idExpediente, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                // Query pesada de búsqueda
                this.lstExpedienteReq = this.expedienteReqService.buscar(index, pageSize,
                        filtrosEntrada, orden);

                listado.setResultados(this.lstExpedienteReq);
                
                // Query pesada de count
                listado.setTotal(this.expedienteReqService.count(filtrosEntrada));

                // Métodos de negocio que suelen tardar
                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());
                this.estaDadoAltaUsuario(this.expedienteBean.getExpedienteDTO());

                // Cálculo de editabilidad fila por fila
                for (ExpedienteReqDTO item : listado.getResultados()) {
                    item.setEditable(habilitarCalificacion(this.isModoConsulta(), this.idEstadoConvocatoria, item)); 
                }
                
                calcularEstadoBotonesAccion();         
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    
    

    private void calcularEstadoBotonesAccion() throws ServiceException {
        // Estas funciones ahora solo se ejecutan cuando nosotros queremos
        this.activarBotonFinalizar = habilitarBotonFinalizarRevision( this.isModoConsulta(), this.idEstadoConvocatoria);        
        this.activarBotonAgregar = habilitarBotonCrearDocumentosSubsanacionRequisitos(this.isModoConsulta(), this.idEstadoConvocatoria);      
        this.activarBotonDescaregar = habilitarBotonEnviarExcluirPortafirmas(this.isModoConsulta(), this.idEstadoConvocatoria);
    }
    
    
    
    /**
     * Método para actualizar la calificación de un requisito
     *
     * @param indice Indice de campo a actualizar
     * @param dto Datos de requisito
     */
//    public void actualizarCalificacion(Integer indice, ExpedienteReqDTO dto) {
//        try {
//
//            Boolean continuar = Boolean.FALSE;
//
//            if (indice.equals(1) && !dto.getItCumpleMod().equals(dto.getItCumple())
//                    || indice.equals(2)
//                            && !dto.getTlObservacionesMod().equals(dto.getTlObservaciones())) {
//                continuar = Boolean.TRUE;
//            }
//
//            if (Boolean.TRUE.equals(continuar)) {
//                dto.setItCumple(dto.getItCumpleMod());
//                if (Constantes.AfirmacionNegacion.S.equals(dto.getItCumple())) {
//                    dto.setTlObservacionesMod(null);
//                }
//                dto.setTlObservaciones(dto.getTlObservacionesMod());

//                ExpedienteReqDTO reqDTO = this.expedienteReqService.guardar(dto);

//                ExpedienteReqDTO reqDTO = this.expedienteReqService.guardarConHistorico(dto);

//                ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
//                        .buscarId(this.idConvocatoria);
                // Esta funcionalidad no aplica en requisitos sino en la resolución provisional
//                if (convocatoriaDTO.getItAlegacion().equals(Constantes.AfirmacionNegacion.S)
//                        && dto.getItCumpleMod().equals("N")) {
//
//                    ExpedienteDTO expDTO = this.expedienteService
//                            .buscarIdExpediente(dto.getExpedienteDTO().getIdExpediente());
//                    expDTO.getEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL));
//                    expDTO.getEstadoDTO()
//                            .setNmConstante(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL);
//                    this.expedienteService.guardar(expDTO);
//
//                    List<DocumentoPtfrDTO> listaDocumentoPtfrDTO = this.documentoPtfrService
//                            .buscarPorExpediente(expDTO.getIdExpediente());
//
//                    Integer idAlgExcPro = utilConstantes.getGlobalCatalogosValores()
//                            .get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL);
//                    Boolean yaExistePotaFirmas = listaDocumentoPtfrDTO.stream()
//                            .anyMatch(f -> idAlgExcPro.equals(f.getTramiteDTO().getIdValor()));
//
//                    if (!yaExistePotaFirmas) {
//                        Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
//                                convocatoriaDTO, expDTO, expDTO.getIdExpediente(),
//                                utilConstantes.getGlobalCatalogosValores()
//                                        .get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL),
//                                null, null, null);
//                        if (insertado.equals(Boolean.TRUE)) {
//                            this.actualizarValoresFormulario();
//                            AtlasMessageUtils.addInfoMessageFromBundle(
//                                    ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
//                        } else {
//                            AtlasMessageUtils.addErrorMessageFromBundle(
//                                    ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
//                        }
//
//                    } else {
//                        AtlasMessageUtils.addErrorMessageFromBundle(
//                                ConstantesWeb.Err.ENVIO_COLA_DUPLICADO_EXISTE);
//                    }
//
//                }

//                this.lstExpedienteReq.stream().forEach(e -> {
//                    if (e.getIdExpedienteReq().equals(reqDTO.getIdExpedienteReq())) {
//                        e = reqDTO;
//                    }
//                });
//
//                this.expedienteBean.getExpedienteDTO()
//                        .setLstExpedienteReqDTO(this.lstExpedienteReq);
//
//                // Refrescamos los controles de formulario
//                this.actualizarValoresFormulario();
//            }
//
//        } catch (ServiceException e) {
//            log.error(AtlasMessageUtils.getStringFromBundle(
//                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO), e);
//            AtlasMessageUtils.addErrorMessageFromBundle(
//                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO);
//        }
//    }
    
    
    public void actualizarCalificacion(Integer indice, ExpedienteReqDTO dto) {
        try {
            this.existeNoCumpleSinObs = Boolean.FALSE;
            boolean haCambiado = (indice.equals(1) && !dto.getItCumpleMod().equals(dto.getItCumple()))
                              || (indice.equals(2) && !Objects.equals(dto.getTlObservacionesMod(), dto.getTlObservaciones()));

            if (haCambiado) {
                dto.setItCumple(dto.getItCumpleMod());
                dto.setTlObservaciones(dto.getTlObservacionesMod());

                this.expedienteReqService.guardarConHistorico(dto);
                
                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());
                
                
                this.expedienteBean.getExpedienteDTO().getLstExpedienteReqDTO().stream()
                .filter(r -> r.getIdExpedienteReq().equals(dto.getIdExpedienteReq()))
                .findFirst()
                .ifPresent(req -> {
                    req.setItCumpleMod(dto.getItCumpleMod()); 
                    req.setItCumple(dto.getItCumpleMod()); 
                    req.setTlObservaciones(dto.getTlObservacionesMod()); 
                    req.setTlObservaciones(dto.getTlObservacionesMod()); 
                    
//                    System.out.println("Sincronizado ID " + req.getIdExpedienteReq() + " a valor: " + req.getItCumple());
                });
               
                this.actualizarValoresFormularioLigero();
            }
        } catch (Exception e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                  ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO), e);
          AtlasMessageUtils.addErrorMessageFromBundle(
                  ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO);
        }
    }

    
    private void actualizarValoresFormularioLigero() {
        try {
//            ExpedienteDTO dto = this.expedienteService.actualizarEstadoInternoRequisito(this.idExpediente);
//            this.expedienteBean.setExpedienteDTO(dto);
            
            calcularEstadoBotonesAccion();

            //PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.botonFinalizar);
            PrimeFaces.current().ajax().update(this.botonExclusion);
            PrimeFaces.current().ajax().update(this.botonExcluir);
            PrimeFaces.current().ajax().update(this.botonDescargarExclusion);
            PrimeFaces.current().ajax().update(this.botonSubsanar);
            
            

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
    }
    
    
    
    

    /**
     * Método para enviar a PTFR informe de requisitos a subsanar
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void enviarSubsanarPortafirmas(Integer idConvocatoria) {
        try {

            // Recuperamos los datos del expediente
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);

            // Validamos si el expediente esta en estado pendiente requerimiento
            if (!utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                    .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())
                    && !utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
                            .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
            } else {
                // Quitamos los registros de baja
                List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

                // Validamos si existe algún registro pendiente de calificación
                Boolean existePendienteCalificar = lstExpReq.stream()
                        .anyMatch(f -> StringUtils.isBlank(f.getItCumpleMod()));

                if (Boolean.TRUE.equals(existePendienteCalificar)) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Err.REVISION_CALIFICACION_REQUISITOS_INCOMPLETO);
                } else {

                    ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                            expedienteDTO, idExpediente,
                            utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                            null, null, null, null);

                    if (insertado.equals(Boolean.TRUE)) {
                       // this.actualizarValoresFormulario();
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                    } else {
                        AtlasMessageUtils
                                .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                    }

                    this.actualizarValoresFormulario();

                    this.calcularExisteEnvioPortafirmas(expedienteDTO);

                }

            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }
    }

    /**
     * Método para enviar a PTFR informe de requisitos a subsanar
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void enviarSubsanarPortafirmas(Integer idConvocatoria,
            UploadedFile archivoSubirSubsanacionReq) {
        byte[] documentoAdjunto;
        try {

            if (archivoSubirSubsanacionReq == null) {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            } else {

                InputStream fileInputStream = this.archivoSubirSubsanacionReq.getInputstream();

                documentoAdjunto = IOUtils.toByteArray(fileInputStream);
                // Recuperamos los datos del expediente
                ExpedienteDTO expedienteDTO = this.expedienteService
                        .buscarIdSimple(this.idExpediente, Boolean.FALSE);

                // Validamos si el expediente esta en estado pendiente
                // requerimiento
                if (!utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())
                        && !utilConstantes.getGlobalCatalogosValores().get(
                                Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
                                .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                    AtlasMessageUtils.addWarnMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
                } else {
                    // Quitamos los registros de baja
                    List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO()
                            .stream().filter(x -> x.getFcBajaFila() == null)
                            .collect(Collectors.toList());

                    // Validamos si existe algún registro pendiente de
                    // calificación
                    Boolean existePendienteCalificar = lstExpReq.stream()
                            .anyMatch(f -> StringUtils.isBlank(f.getItCumpleMod()));

                    if (Boolean.TRUE.equals(existePendienteCalificar)) {
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.REVISION_CALIFICACION_REQUISITOS_INCOMPLETO);
                    } else {

                        ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

                        Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
                                convDTO, expedienteDTO, idExpediente,
                                utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                                documentoAdjunto, null, null, null);

                        if (insertado.equals(Boolean.TRUE)) {
                            this.actualizarValoresFormulario();
                            AtlasMessageUtils.addInfoMessageFromBundle(
                                    ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                        } else {
                            AtlasMessageUtils.addErrorMessageFromBundle(
                                    ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                        }

                        this.actualizarValoresFormulario();

                        this.calcularExisteEnvioPortafirmas(expedienteDTO);

                    }

                }
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        } catch (IOException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
        }
    }

    /**
     * Método descargar un PDF con el formulario de subsanacion a enviar a
     * portafirmas
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void descargarBorradorSubsanarPortafirmas(ConvocatoriaDTO convDTO) {
        try {

            byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                    convDTO.getIdConvocatoria(),
                    utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                    this.idExpediente, null);

            StringBuilder nombreArchivoPdf = new StringBuilder();
            nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
            nombreArchivoPdf.append("_");
            nombreArchivoPdf
                    .append("Requerimiento de Subsanación - Solicitud - Revisión requisitos");
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("BORRADOR");

            UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Método para enviar a PTFR informe de requisitos excluidos
     *
     * @param convDTO Datos de la convocatoria
     */
    public void enviarExcluirPortafirmasReq() {
        try {

            ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);
            // MD-2147 Quitamos Exclusion 412
//            Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
//                    expedienteDTO, idExpediente,
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION),
//                    null, null, null, null);
            // MD-2147 Y lo cambiamos por la resolución de exclusion provisional 419
            
            //si tiene marcado el check lanzamos el prosoviosoonal y si no eld efinitivo
            Integer tramite;
            
            if(Boolean.TRUE.equals(convDTO.getItResProvExclusion())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL);
            }else {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION);
            }
            
            Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                    expedienteDTO, idExpediente,tramite, null, null, null, null);

            if (insertado.equals(Boolean.TRUE)) {
                
                // MD-2147 quitamos la actualizacion de los valores del formulario
                // this.actualizarValoresFormulario();
                // MD-2147 add el guardado del cambio de estado de la rev req
                
                Integer estado;
                
                if(Boolean.TRUE.equals(convDTO.getItResProvExclusion())) {
                    estado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
                }else {
                    estado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO);
                }
                
                
                ExpedienteDTO expeDTO = this.expedienteService.guardarCambioEstadoRevReq(
                        this.idExpediente, estado);
                // actualizamos el DTO
                this.expedienteBean.setExpedienteDTO(expeDTO);
                this.expedienteBean.getFinanciacionBean().setBAvisoFinanciacion(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoRequisitos(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(Boolean.FALSE);
                
                
                if (this.expedienteBean.getEvaluacionBean() != null) {
                    EvaluacionBean evalBean = this.expedienteBean.getEvaluacionBean();     
                    // Invocamos la verificación/deshabilitación pasando el DTO de la convocatoria actual
                    evalBean.habilitarBotonAgregar(convDTO);
                }

                // 3. PROPAGACIÓN MANUAL A FINANCIACION BEAN (Si está instanciado)
                if (this.expedienteBean.getFinanciacionBean() != null) {
                    FinanciacionBean finanBean = this.expedienteBean.getFinanciacionBean();
                    finanBean.setBAvisoFinanciacion(Boolean.FALSE);
                    
                    if (expeDTO.getEstadoDTO() != null) {
                        finanBean.setIdEstadoExpediente(expeDTO.getEstadoDTO().getIdValor());
                    }
                    
                    // Recalculamos el modo consulta de financiación
                    finanBean.esModoConsulta();
                    finanBean.esconderBotonCargarPresup();
                }
                
                                
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        }
    }
    
    
    public void elegirExclusion(Boolean provisional) {
        
        if(provisional != null && provisional.equals(Boolean.TRUE)) {
            abrirExclusionDialog();
        }else if(provisional != null && provisional.equals(Boolean.FALSE)){
            exclusionDefinitiva();
        }
        
        
    }
    

    public void exclusionDefinitiva() {
        try {

            ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

                //Integer estado= utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO);
            
            Integer estado= utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO); // falta ver aqui que tipo de estado emtemos
            
               
                ExpedienteDTO expeDTO = this.expedienteService.guardarCambioEstadoRevReq(
                        this.idExpediente, estado);
                
                // actualizamos el DTO
                this.expedienteBean.setExpedienteDTO(expeDTO);
                this.expedienteBean.getFinanciacionBean().setBAvisoFinanciacion(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoRequisitos(Boolean.FALSE);
                this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(Boolean.FALSE);
                      
                if (this.expedienteBean.getEvaluacionBean() != null) {
                    EvaluacionBean evalBean = this.expedienteBean.getEvaluacionBean();     
                    // Invocamos la verificación/deshabilitación pasando el DTO de la convocatoria actual
                    evalBean.habilitarBotonAgregar(convDTO);
                }

                // 3. PROPAGACIÓN MANUAL A FINANCIACION BEAN (Si está instanciado)
                if (this.expedienteBean.getFinanciacionBean() != null) {
                    FinanciacionBean finanBean = this.expedienteBean.getFinanciacionBean();
                    finanBean.setBAvisoFinanciacion(Boolean.FALSE);
                    
                    if (expeDTO.getEstadoDTO() != null) {
                        finanBean.setIdEstadoExpediente(expeDTO.getEstadoDTO().getIdValor());
                    }
                    
                    // Recalculamos el modo consulta de financiación
                    finanBean.esModoConsulta();
                    finanBean.esconderBotonCargarPresup();
                }
                
                                
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EXCLUIDO_DEFINITIVO_OK);


        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Mensajes.EXCLUIDO_DEFINITIVO_KO);
            }
        }
    }

    
    
    /**
     * Método para enviar a PTFR informe de requisitos excluidos
     *
     * @param convDTO Datos de la convocatoria
     */
    public void enviarExcluirPortafirmasReq(UploadedFile archivoSubirExclusion) {
        byte[] documentoAdjunto;
        try {

            if (archivoSubirExclusion == null || archivoSubirExclusion.getFileName() == null) {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FALTA_DOC_EXCLUSION);
            } else {

                InputStream fileInputStream = this.archivoSubirExclusion.getInputstream();

                documentoAdjunto = IOUtils.toByteArray(fileInputStream);

                ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

                ExpedienteDTO expedienteDTO = this.expedienteService
                        .buscarIdSimple(this.idExpediente, Boolean.FALSE);
                
                Integer idTipoDocumento;
                Integer idTipoDoc;
                if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                    // MD-2422 Creamos el expedientedoc
                     idTipoDocumento = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_EXCLUIDA);
                     idTipoDoc = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL);
                }else {
                    // MD-2422 Creamos el expedientedoc
                     idTipoDocumento = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
                     idTipoDoc = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.EXCLUSION);
                }
                
                this.expedienteDocDTO  = this.prepararMetadatosFica(archivoSubirExclusion,
                        idTipoDocumento, idTipoDoc);
                this.expedienteDocDTO = this.expedienteDocService.subirDocumentoFica(this.expedienteDocDTO);
                this.expedienteDocDTO = this.expedienteDocService.guardar(this.expedienteDocDTO);
                
                // MD-2147 Quitamos la inserción del registro ptfr de EXCLUSION 412
//                Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
//                        convDTO, expedienteDTO, idExpediente, utilConstantes
//                                .getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION),
//                        documentoAdjunto, null, null, null);
                // MD-2147 Implementamos la inserción del registro ptfr de RESOLUCION_EXCLUSION_PROVISIONAL 419
                
                Integer tramite;
                
                if(Boolean.TRUE.equals(convDTO.getItResProvExclusion())) {
                    tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL);
                }else {
                    tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION);
                }
                
                
                Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                        expedienteDTO, idExpediente, tramite,documentoAdjunto, null, null, null);
                
                if (insertado.equals(Boolean.TRUE)) {
                    
                    Integer tramiteCodigo;
                    
                    if(Boolean.TRUE.equals(convDTO.getItResProvExclusion())) {
                        tramiteCodigo = Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL;
                    }else {
                        tramiteCodigo = Constantes.Tramite.EXCLUSION;
                    }
                    
                    // MD-2422 Recuperamos el documento y registramos el idDocumentoPtfr
                    List<DocumentoPtfrDTO> docPtfrDto = this.documentoPtfrService
                            .buscarPorExpedienteTramite(idExpediente,tramiteCodigo);
                    this.expedienteDocDTO.setIdDocumentoPtfr(docPtfrDto.get(0).getIdDocumentoPtfr());
                    this.expedienteDocService.guardar(this.expedienteDocDTO);
                    
                    // MD-2147 quitamos este método 
                    // this.actualizarValoresFormulario();
                    // MD-2147 y sólo cambiamos el estado de la rev req
//                    this.expedienteService.guardarCambioEstadoRevReq(this.idExpediente,
//                            utilConstantes.getGlobalCatalogosValores()
//                                    .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
                    
                    // MD-2422 Actualizamos el estado provisional
//                    ExpedienteDTO expeDTO = this.expedienteService.actualizarEstadoProvisionalExpediente(
//                            idExpediente, Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
//                            UserUtils.getUserName().toUpperCase());
                    
                    // MD-2422 ctualizamos el estado del expediente, el estado
                    // de la revisión de requisitos y el estado provisional
                    
                    
                    Integer estado;
                    
                    if(Boolean.TRUE.equals(convDTO.getItResProvExclusion())) {
                        estado = Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL;
                    }else {
                        estado = Constantes.EstadoExpediente.EXCLUIDO;
                    }
                    
                    
                    ExpedienteDTO expeDTO = this.expedienteService
                            .actualizarCambioEstEstRevReqEstPro(idExpediente, estado,
                                    UserUtils.getUserName().toUpperCase());
                    
                    // Actualizamos el DTO
                    this.expedienteBean.setExpedienteDTO(expeDTO);
                    this.expedienteBean.getFinanciacionBean().setBAvisoFinanciacion(Boolean.FALSE);
                    this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);
                    this.expedienteBean.getExpedienteDTO().setBAvisoRequisitos(Boolean.FALSE);
                    this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(Boolean.FALSE);
                    
                    if (this.expedienteBean.getEvaluacionBean() != null) {
                        EvaluacionBean evalBean = this.expedienteBean.getEvaluacionBean();     
                        // Invocamos la verificación/deshabilitación pasando el DTO de la convocatoria actual
                        evalBean.habilitarBotonAgregar(convDTO);
                    }

                    // 3. PROPAGACIÓN MANUAL A FINANCIACION BEAN (Si está instanciado)
                    if (this.expedienteBean.getFinanciacionBean() != null) {
                        FinanciacionBean finanBean = this.expedienteBean.getFinanciacionBean();
                        finanBean.setBAvisoFinanciacion(Boolean.FALSE);
                        
                        if (expeDTO.getEstadoDTO() != null) {
                            finanBean.setIdEstadoExpediente(expeDTO.getEstadoDTO().getIdValor());
                        }
                        
                        // Recalculamos el modo consulta de financiación
                        finanBean.esModoConsulta();
                        finanBean.esconderBotonCargarPresup();
                    }
                    
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);

                } else {
                    AtlasMessageUtils
                            .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                }

            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        } catch (IOException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
        }
    }
    
    /**
     * 
     * @param archivoResolucion
     * @param propuestaResolucion
     * @param tramite
     * @return
     */
    public ExpedienteDocDTO prepararMetadatosFica(UploadedFile archivoResolucion,
            Integer propuestaResolucion, Integer tramite) {

        if (Objects.nonNull(archivoResolucion)) {

            try {
                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(propuestaResolucion, 
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                InputStream fileInputStream = archivoResolucion.getInputstream();
                String nombreArchivo = archivoResolucion.getFileName();
                this.expedienteDocDTO.setIdExpedienteDoc(
                        expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null
                                ? expeDocDTO.getIdExpedienteDoc()
                                : null);
                this.expedienteDocDTO.setExpedienteDTO(this.expedienteBean.getExpedienteDTO());
                this.expedienteDocDTO.setEstadoDTO(this.catalogosValoresService
                        .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                this.expedienteDocDTO.setTipoDocumentoDTO(
                        this.catalogosValoresService.buscarId(propuestaResolucion));
                this.expedienteDocDTO.setFicaMetadatos(new FicaMetadatos());
                this.expedienteDocDTO.setCdFica(expeDocDTO != null && expeDocDTO.getCdFica() != null
                        ? expeDocDTO.getCdFica()
                        : null);
                this.expedienteDocDTO.getFicaMetadatos().setNombreFichero(nombreArchivo);
                this.expedienteDocDTO.getFicaMetadatos().setFechaFichero(new Date());
                this.expedienteDocDTO.getFicaMetadatos().setExtension(nombreArchivo
                        .substring(nombreArchivo.indexOf('.') + 1, nombreArchivo.length()));
                this.expedienteDocDTO.getFicaMetadatos()
                        .setUsuario(UserUtils.getUserName().toUpperCase());
                this.expedienteDocDTO.getFicaMetadatos().setEncriptar(false);
                this.expedienteDocDTO.setContenidoFichero(IOUtils.toByteArray(fileInputStream));
                this.expedienteDocDTO
                        .setCdUsuAlta(expeDocDTO != null && expeDocDTO.getCdUsuAlta() != null
                                ? expeDocDTO.getCdUsuAlta()
                                : null);
                this.expedienteDocDTO
                        .setFcAltaFila(expeDocDTO != null && expeDocDTO.getFcAltaFila() != null
                                ? expeDocDTO.getFcAltaFila()
                                : null);
                this.expedienteDocDTO
                        .setCdUsuModif(expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null
                                ? UserUtils.getUserName().toUpperCase()
                                : null);
                this.expedienteDocDTO.setFcModifFila(
                        expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null ? new Date()
                                : null);
                this.expedienteDocDTO.setTramiteDTO(tramite == null ? new CatalogosValoresDTO()
                        : this.catalogosValoresService.buscarId(tramite));

            } catch (IOException e) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage());
            } catch (ServiceException e) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage());
            }

        } else {

            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.info(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);

        }

        return this.expedienteDocDTO;

    }

    /**
     * Método descargar un PDF con el formulario de exclusion a enviar a
     * portafirmas
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void descargarBorradorExcluirPortafirmas(ConvocatoriaDTO convDTO) {

        try {

            byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                    convDTO.getIdConvocatoria(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION),
                    this.idExpediente, null);

            StringBuilder nombreArchivoPdf = new StringBuilder();
            nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("Exclusión de expediente");
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("BORRADOR");

            UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Método para finalizar revisión de requisitos
     */
    public void finalizarRevisionOk() {

        try {

            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoInternoRevisionOkRequisito(this.idExpediente);

            if (dto != null && utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.REVISION_OK)
                    .equals(dto.getEstadoRevRequiDTO().getIdValor())) {
                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_OK);
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_KO);
            }

        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_REQUISITO_FINALIZAR_KO);
        }
    }

    /**
     * Método para descargar documento requisito subsanado
     *
     * @param idExpedienteDoc Identificador de documento
     */
    public void descargarDocumentoRequisitoSubsanado(Integer idExpedienteDoc) {

        try {
            if (idExpedienteDoc != null) {
                ExpedienteDocDTO expedienteDocDTO = this.expedienteDocService
                        .buscarId(idExpedienteDoc);
                if (expedienteDocDTO != null) {
                    this.descargarDocumentoFica(expedienteDocDTO.getCdFica());
                }

            } else {
                log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * Método para descargar documento fica
     *
     * @param cdFica codigo de documento fica
     */
    private void descargarDocumentoFica(Integer cdFica) {
        try {
            if (Objects.nonNull(cdFica)) {
                FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);

                if (ficaFichero != null) {
                    DocumentoDTO documentoDTO = new DocumentoDTO();
                    documentoDTO.setFicaFichero(ficaFichero);
                    documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
                    documentoDTO.setCdFica(cdFica);

                    String strNombreDoc = documentoDTO.getFicaFichero().getNombreFichero();
                    // Se prepara el fichero.
                    UtilesWeb.openFile(documentoDTO.getContenidoFichero(),
                            documentoDTO.getFicaFichero().getExtension(),
                            FilenameUtils.removeExtension(strNombreDoc));

                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * Metodo para descargar documento de informe a PTFR
     *
     * @param dto
     */
    public void descargarDocumentoInformePtfr(Integer idExpediente, Boolean esSubsanacion) {

        try {
            List<ExpedienteReqDTO> lista = this.expedienteReqService
                    .buscarPorExpediente(idExpediente);

            Optional<ExpedienteReqDTO> opReq = Optional.empty();

            if (Boolean.TRUE.equals(esSubsanacion)) {
                Integer idReqSubReq = utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
                opReq = lista.stream()
                        .filter(f -> f.getDocumentoPtfrDTO() != null
                                && f.getDocumentoPtfrDTO().getTramiteDTO().getIdValor() != null
                                && idReqSubReq.equals(
                                        f.getDocumentoPtfrDTO().getTramiteDTO().getIdValor()))
                        .findFirst();
            } else {
                Integer idExc = utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.EXCLUSION);
                opReq = lista.stream()
                        .filter(f -> f.getDocumentoPtfrDTO() != null
                                && f.getDocumentoPtfrDTO().getTramiteDTO().getIdValor() != null
                                && idExc.equals(
                                        f.getDocumentoPtfrDTO().getTramiteDTO().getIdValor()))
                        .findFirst();
            }

            if (opReq.isPresent()) {

                PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService
                        .recuperarDocumentoFirmado(
                                opReq.get().getDocumentoPtfrDTO().getDsReferenciaPtfr(),
                                String.valueOf(
                                        opReq.get().getDocumentoPtfrDTO().getCdArchivoPtfr()));

                if (recuperado != null) {
                    UtilesWeb.openFile(Base64.getDecoder().decode(recuperado.getPdfB64()),
                            Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION,
                            Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_INFORME);
                }

            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
     * (subir documentos, aprobar, revertir, subsanar, finalizar revisiones, enviar a
     * portafirmas/NOTE...), dejando habilitadas las de solo lectura (descargar, ver, consultar).
     *
     * @return true si el expediente está Rechazado Definitivo
     */
    public boolean isExpedienteRechazadoDefinitivo() {
        return this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }

    /**
     * Metodo para validar si se habiltia el boton de finalizar revisión
     * requisito
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException
     */
    public boolean habilitarBotonFinalizarRevision(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        boolean habilitar = Boolean.FALSE;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite finalizar la revisión de requisitos.
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        ConvocatoriaDTO conv = new ConvocatoriaDTO();
        this.comprobarGestoresExternos(conv, idConvocatoria, this.infoUsuarioBean.getLogin());

        // Quitamos los registros de baja
        List<ExpedienteReqDTO> lstExpReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

        // Validamos si todos los requisitos administrativos fueron calificados
        // como "SI" cumple
        Boolean pendienteCalificadoSiCumple = lstExpReq.stream()
                .anyMatch(f -> !Constantes.AfirmacionNegacion.S.equals(f.getItCumpleMod()));

        // Lista de estados finales donde no se permite finalizar revisión
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.REVISION_OK));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDO));
        
        // MD-2147 determinar estado rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        // MD-2148 determinamos si el estado de rev adm se encuentra en el estado desisitido provisional
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        if (Boolean.FALSE.equals(esModoConsulta)
                && !utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                && !lstEstadoExpInter.contains(dto.getEstadoRevRequiDTO().getIdValor())
                && Boolean.FALSE.equals(pendienteCalificadoSiCumple)
                && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && !conv.isEsGestorJCT()
                && Boolean.FALSE.equals(excluidoProvisionalEnviado) // MD-2147 incluimos excluido provisional en la rev de req
                && Boolean.FALSE.equals(desisitidoProvisonalEnviado)) { // MD-2148 incluimos la condición de desistido provisional de la rev adm
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion requisitos
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException
     */
    public boolean habilitarBotonEnviarSubanasarPortafirmas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        boolean habilitar = Boolean.FALSE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

//        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
//        this.comprobarGestoresExcep(conv);
        
        ConvocatoriaDTO conv = new ConvocatoriaDTO();
        this.comprobarGestoresExternos(conv, idConvocatoria, this.infoUsuarioBean.getLogin());

        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        // Quitamos los registros de baja
        List<ExpedienteReqDTO> lstExpReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

        // Validamos si hay requisitos calificados como "NO" cumple con sus
        // respectivas observaciones
        Boolean existeNoCumple = lstExpReq.stream()
                .anyMatch(f -> Constantes.AfirmacionNegacion.N.equals(f.getItCumpleMod())
                        && StringUtils.isNotBlank(f.getTlObservaciones())); // POM
                                                                            // MD-1414
                                                                            // [TRAM]
                                                                            // No
                                                                            // se
                                                                            // está
                                                                            // pidiendo
                                                                            // las
        // observaciones para la subsanación o desistimiento de requisitos.

        // Validamos si hay requisitos sin calificar
        Boolean todosCheckeados = lstExpReq.stream().allMatch(f -> f.getItCumpleMod() != null);

        // Lista de estados finales donde no se permite enviar a portafirmas
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.REVISION_OK));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDO));

        if (Boolean.FALSE.equals(esModoConsulta)
                && !utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionFirmado)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionFirmado)
                // GIM 07/10/2024 POSIBLE ARREGLO PARA EL ENVIO A COLA
                && Boolean.FALSE.equals(this.existeEnvioPtfrEnCola)
                && !lstEstadoExpInter.contains(dto.getEstadoRevRequiDTO().getIdValor())
                && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && Boolean.TRUE.equals(existeNoCumple) && Boolean.TRUE.equals(todosCheckeados)
                && !conv.isEsGestorJCT() && !conv.isEsGestorConcesion()
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

    /**
     * 
     * @return
     */
    public boolean habilitarBotonCrearDocumentosSubsanacionRequisitos(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        boolean habilitar = Boolean.FALSE;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite agregar/requerir nuevos documentos de subsanación de requisitos.
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
//        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
//        this.comprobarGestoresExcep(conv);
        
        ConvocatoriaDTO conv = new ConvocatoriaDTO();
        this.comprobarGestoresExternos(conv, idConvocatoria, this.infoUsuarioBean.getLogin());
                
        // Quitamos los registros de baja
        List<ExpedienteReqDTO> lstExpReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
        // Validamos si hay requisitos calificados como "NO" cumple con sus
        // respectivas observaciones
        Boolean existeNoCumple = lstExpReq.stream()
                .anyMatch(f -> Constantes.AfirmacionNegacion.N.equals(f.getItCumpleMod())
                        && StringUtils.isNotBlank(f.getTlObservaciones()));
        if (Boolean.TRUE.equals(existeNoCumple)) {
            existeNoCumple = lstExpReq.stream()
                    .filter(r -> Constantes.AfirmacionNegacion.N.equals(r.getItCumpleMod()))
                    .allMatch(r -> StringUtils.isNotBlank(r.getTlObservaciones()));
        }
        // Validamos si hay requisitos sin calificar y si es N entonces debe existir observaciones
        Boolean todosCheckeados = lstExpReq.stream()
                .allMatch(f -> f.getItCumple() != null
                        && (Constantes.AfirmacionNegacion.S.equals(f.getItCumpleMod())
                                || Constantes.AfirmacionNegacion.N.equals(f.getItCumpleMod())
                                        && f.getTlObservaciones() != null));

        // Lista de estados finales donde no se permite enviar a portafirmas
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.REVISION_OK));
        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
        
        // MD-2147 determinar estado rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        // MD-2148 se encuentra en estado desisitido provisional la rev adm
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        if (Boolean.FALSE.equals(esModoConsulta)
                && !utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                && Boolean.FALSE.equals(this.existeDocumentoSubsanacion)
                && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionFirmado)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionFirmado)
                && Boolean.FALSE.equals(this.existeEnvioPtfrEnCola)
                && !lstEstadoExpInter.contains(dto.getEstadoRevRequiDTO().getIdValor())
                && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && Boolean.TRUE.equals(existeNoCumple) && Boolean.TRUE.equals(todosCheckeados)
                && !conv.isEsGestorJCT() && !conv.isEsGestorConcesion()
                && Boolean.FALSE.equals(excluidoProvisionalEnviado) // MD-2147 incluimos excluido provisional en la rev de req
                && Boolean.FALSE.equals(desisitidoProvisonalEnviado)// MD-2148 incluimos la condición de desisitido provisional del a rev adm
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * exclusion
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException
     */
    public boolean habilitarBotonEnviarExcluirPortafirmas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

//        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
//        this.comprobarGestoresExcep(conv);
        
        ConvocatoriaDTO conv = new ConvocatoriaDTO();
        this.comprobarGestoresExternos(conv, idConvocatoria, this.infoUsuarioBean.getLogin());

        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        // Quitamos los registros de baja
        List<ExpedienteReqDTO> lstExpReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());

        // Lista de estados finales donde no se permite enviar a portafirmas
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.REVISION_OK));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDO));

        Boolean existeNoCumple = lstExpReq.stream()
                .anyMatch(f -> Constantes.AfirmacionNegacion.N.equals(f.getItCumpleMod())
                        && StringUtils.isNotBlank(f.getTlObservaciones())); // POM
                                                                            // MD-1414
                                                                            // [TRAM]
                                                                            // No
                                                                            // se
                                                                            // está
                                                                            // pidiendo
                                                                            // las
        // observaciones para la subsanación o desistimiento de requisitos.

        // Validamos si hay requisitos sin calificar
        Boolean todosCheckeados = lstExpReq.stream().allMatch(f -> f.getItCumpleMod() != null);
        
        // MD-2147 determinar estado rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        // MD-2148 está en el estado desistido provisional la rev adm
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        
        // MD-2484 Incluimos el método que consulta si existe desistimiento en PTFR y/o exclusion en PTFR
        List<Integer> tramites = new ArrayList<>();
        tramites.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
        tramites.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
        Boolean existeDesistimientoExclusionProvisionalEnviado = this.documentoPtfrService
                .consultarDocumentoPortafirmaPorTramites(dto.getIdExpediente(), tramites);

        if (Boolean.FALSE.equals(esModoConsulta)
                && !utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionEnCola)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrExclusionFirmado)
                // GIM 07/10/2024 POSIBLE ARREGLO PARA EL ENVIO A COLA
                // && Boolean.FALSE.equals(this.existeEnvioPtfrEnCola)
                && !lstEstadoExpInter.contains(dto.getEstadoRevRequiDTO().getIdValor())
                && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevRequiDTO()
                                .getIdValor())
                        || Boolean.TRUE.equals(existeNoCumple))
                && Boolean.TRUE.equals(todosCheckeados) && !conv.isEsGestorJCT()
                && !conv.isEsGestorConcesion()
                && Boolean.FALSE.equals(excluidoProvisionalEnviado) // MD-2147 incluimos excluido provisional en la rev de req
                && Boolean.FALSE.equals(desisitidoProvisonalEnviado) // MD-2148 incluimos la condicion de estado de desitimiento prov la rev adm
                && Boolean.FALSE.equals(existeDesistimientoExclusionProvisionalEnviado) // MD-2484 Incluimos el método que consulta si existe desistimiento y/o exclusion en PTFR
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;

    }

    /**
     * Método para validar si se habilita la calificacion de requisitos
     *
     * @param dto Datos de expediente
     * @throws ServiceException
     */
    public boolean habilitarCalificacion(Boolean esModoConsulta, Integer idEstadoConvocatoria,
            ExpedienteReqDTO expReqDto) throws ServiceException {
        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        // actualizamos la validacion
        //this.calcularExisteEnvioPortafirmas(dto);

        // Lista de estados internos donde no se permite calificar
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.REVISION_OK));
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.INICIADA));

        Boolean permiteEditar = Boolean.TRUE;

        if (Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrExclusionEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrExclusionFirmado)) {
            permiteEditar = Boolean.FALSE;
        } else if (Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionFirmado)) {
            ExpedienteDocDTO expDocDTO = null;
            try {
                if (Objects.nonNull(expReqDto.getIdExpedienteDoc())) {
                    expDocDTO = this.expedienteDocService.buscarId(expReqDto.getIdExpedienteDoc());
                }
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }

            if (Objects.nonNull(expDocDTO)
                    && !utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoDocumento.REVISION_OK)
                            .equals(expDocDTO.getEstadoDTO().getIdValor())
                    && !dto.getEstadoRevRequiDTO().getIdValor().equals(utilConstantes
                            .getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS))
                    && !dto.getEstadoRevRequiDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpediente.EXCLUIDO_PROVISIONAL))) {
                permiteEditar = Boolean.FALSE;
            }
        }
        
        this.existeCumplePendienteRevReq = Boolean.FALSE;
        if (dto.getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS))
                && (Constantes.AfirmacionNegacion.S.equals(expReqDto.getItCumpleMod()))) {
            this.existeCumplePendienteRevReq = Boolean.TRUE;
        }
        
        // MD-2147 determinar estado rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
     // MD-2148 está en desistido provisional el estado de la rev adm
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        if (lstEstadoExpInter.contains(dto.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                || Boolean.FALSE.equals(permiteEditar) || Boolean.TRUE.equals(esModoConsulta)
                || Boolean.TRUE.equals(this.existeEnvioPtfrEnCola)
                || Boolean.TRUE.equals(this.existeDocumentoSubsanacionCumple)
                || lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                || Boolean.TRUE.equals(this.existeCumplePendienteRevReq)
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 incluimos excluido provisional en la rev de req 
                || Boolean.TRUE.equals(desisitidoProvisonalEnviado) // MD-2148 incluimos la condicion de estar en el estado de desisitod provisional la rev adm
                ) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para calcular si existe un envío de subsanación a portafirmas
     * pendiente de respuesta
     *
     * @param dto Datos de expediente
     * @throws ServiceException
     */
    private void calcularExisteEnvioPortafirmas(ExpedienteDTO dto) throws ServiceException {
        this.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrExclusionEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrExclusionEnCola(Boolean.FALSE);
        this.setExisteEnvioPtfrSubsanacionFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrExclusionFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrEnCola(Boolean.FALSE);
        this.setExisteDocumentoSubsanacion(Boolean.FALSE);
        this.setExisteDocumentoSubsanacionCumple(Boolean.FALSE);
        

        // Quitamos los registros de baja
        List<ExpedienteReqDTO> lstExpReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());

        // Quitamos los registros que no esten asociados a un envío a
        // portafirmas
        lstExpReq = lstExpReq.stream()
                .filter(x -> Objects.nonNull(x.getDocumentoPtfrDTO())
                        && Objects.nonNull(x.getDocumentoPtfrDTO().getIdDocumentoPtfr()))
                .collect(Collectors.toList());

        List<DocumentoPtfrDTO> listaPTFR = documentoPtfrService.buscarPorExpediente(idExpediente);
        /**
         * if(!listaPTFR.isEmpty()) { for (DocumentoPtfrDTO docuPTr : listaPTFR)
         * { if( docuPTr.getTramiteDTO() != null &&
         * docuPTr.getTramiteDTO().getIdValor() != null && (
         * docuPTr.getTramiteDTO().getIdValor().equals(Constantes.Tramite.EXCLUSION)
         * ||
         * docuPTr.getTramiteDTO().getIdValor().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
         * ||
         * docuPTr.getTramiteDTO().getIdValor().equals(Constantes.Tramite.SUBSANACION_REVISION_REQUISITO)
         * )) { this.setExisteEnvioPtfrEnCola(Boolean.TRUE); break; } } }
         **/
        // Revisamos si han envioPtfr en cola y exclusion
        if (!listaPTFR.isEmpty()) {

            Integer idExclu = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.EXCLUSION);
            Integer idReqSubReq = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
            Integer idSubRevReq = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.SUBSANACION_REVISION_REQUISITO);
            Integer idReqSubAdm = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);

            boolean existePtfrEnCola = listaPTFR.stream()
                    .filter(docuPtfr -> docuPtfr.getTramiteDTO() != null
                            && docuPtfr.getTramiteDTO().getIdValor() != null)
                    .anyMatch(docuPtfr -> {
                        Integer idValor = docuPtfr.getTramiteDTO().getIdValor();
                        boolean isExclusion = (idValor.equals(idExclu)
                                && docuPtfr.getCdEstadoPtfr() == null);

                        if (isExclusion) {
                            this.setExisteEnvioPtfrExclusionEnCola(Boolean.TRUE);
                        }

                        return isExclusion || idValor.equals(idReqSubReq)
                                || idValor.equals(idSubRevReq) || idValor.equals(idReqSubAdm);
                    });

            if (existePtfrEnCola) {
                this.setExisteEnvioPtfrEnCola(Boolean.TRUE);
            }
        }

        if (!lstExpReq.isEmpty()) {

            // Es el mismo registro de envío para todos los documentos
            // observados o excluidos
            this.documentoPtfrDTO = lstExpReq.get(0).getDocumentoPtfrDTO();

            if (Objects.nonNull(this.documentoPtfrDTO.getTramiteDTO())) {
                if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                        .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                        && utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                                .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrSubsanacionFirmado(Boolean.TRUE);
                }

                if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                        .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                        && utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                                .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.TRUE);
                }

                if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                        .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                        && utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.EXCLUSION)
                                .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrExclusionFirmado(Boolean.TRUE);
                }

                if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                        .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                        && utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.EXCLUSION)
                                .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrExclusionEnProceso(Boolean.TRUE);
                }

                if (this.documentoPtfrDTO.getCdEstadoPtfr() == null && utilConstantes
                        .getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION)
                        .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrExclusionEnCola(Boolean.TRUE);
                }
            }
        }

        // recuperamos ls requisitos configurados
        List<ConvocatoriaReqDTO> lstConvReqDTO = this.convocatoriaReqService
                .obtenerRequisitoPorConvocatoria(this.idConvocatoria);

        // Quitamos los registros de baja de la lista de Requisitos
        List<ExpedienteReqDTO> lstExpeReq = dto.getLstExpedienteReqDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());
        // Dejamos sólo los que no cumplen
        lstExpeReq = lstExpeReq.stream()
                .filter(x -> Constantes.AfirmacionNegacion.N.equals(x.getItCumpleMod()))
                .collect(Collectors.toList());

        // Quitamos los registros de baja de la lista de Documentos
        List<ExpedienteDocDTO> lstExpeDoc = dto.getLstExpedienteDocDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());
        // Dejamos sólo los que son del tipo de trámite Requerimiento de
        // Subsanación - Solicitud - Revisión requisitos
        lstExpeDoc = lstExpeDoc.stream()
                .filter(x -> Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO
                        .equals(x.getTramiteDTO().getNmConstante()))
                .collect(Collectors.toList());

        for (ConvocatoriaReqDTO convocatoriaReqDTO : lstConvReqDTO) {
            Integer tipoDocumento = convocatoriaReqDTO.getConvocatoriaTipoDocDTO()
                    .getTipoDocumentoDTO().getIdValor();
            Integer requisito = convocatoriaReqDTO.getRequisitoDTO().getIdValor();
            for (ExpedienteReqDTO expedienteReqDTO : lstExpeReq) {
                if (requisito.equals(expedienteReqDTO.getRequisitoDTO().getIdValor())) {
                    expedienteReqDTO.setTipoDocumento(tipoDocumento);
                }
            }
        }
        for (ExpedienteReqDTO expedienteReqDTO : lstExpeReq) {
            if (!lstExpeDoc.isEmpty() && lstExpeDoc.stream().anyMatch(x -> x.getTipoDocumentoDTO()
                    .getIdValor().equals(expedienteReqDTO.getTipoDocumento()))) {
                if (!utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                        && !utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.REVISION_OK)
                                .equals(dto.getEstadoRevRequiDTO().getIdValor())) {
                    this.setExisteDocumentoSubsanacion(Boolean.TRUE);
                }
                if (!utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(dto.getEstadoRevRequiDTO().getIdValor())
                        && !utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.REVISION_OK)
                                .equals(dto.getEstadoRevRequiDTO().getIdValor())
                        && !utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)
                                .equals(dto.getEstadoRevRequiDTO().getIdValor())) {
                    this.setExisteDocumentoSubsanacionCumple(Boolean.TRUE);
                }
            }
        }

    }

    /**
     * Metodo para actualizar los valores del formulario
     */
    private void actualizarValoresFormulario() {
        try {
            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoInternoRequisito(this.idExpediente);
//
//            this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());
//
            this.expedienteBean.setExpedienteDTO(dto);

            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.formularioRequisitos);
            PrimeFaces.current().ajax().update(this.solapaRequisitos);

            // actualizacion de botones
            PrimeFaces.current().ajax().update(this.botonFinalizar);
            PrimeFaces.current().ajax().update(this.botonExclusion);
            PrimeFaces.current().ajax().update(this.botonSubsanar);

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public String obtenerDescripcionEstadoNote() {
        Map<String, String> descripcion = new HashMap<>();
        descripcion.put(Constantes.Note.CD_PD, Constantes.Note.DS_PUESTA_DISPOSICION);
        descripcion.put(Constantes.Note.CD_L, Constantes.Note.DS_LEIDAS);
        descripcion.put(Constantes.Note.CD_R, Constantes.Note.DS_RECHAZADAS);
        descripcion.put(Constantes.Note.CD_RA, Constantes.Note.DS_RECHAZADAS_AUTO);

        return Objects.nonNull(this.documentoPtfrDTO.getCdEstadoNote())
                ? descripcion.get(this.documentoPtfrDTO.getCdEstadoNote())
                : "";
    }

    /**
     *
     */
    /**
     * POM: MD-1410 [TRAM] Al intentar acceder a la pestaña de requisitos
     * muestra un error en desarrollo Método comentado, duplicado pasando
     * expediente por parametro public void estaDadoAltaUsuario() { Boolean
     * estaDadoAlta = Boolean.FALSE; if (Objects.nonNull(this.idExpediente)) {
     * try { ExpedienteDTO expeDTO =
     * this.expedienteService.buscarId(this.idExpediente); NoteExisteNifOut
     * existeNif = this.noteService.existeNif(
     * NoteAdmeService.ConstantesNote.COD_APP,
     * expeDTO.getInteresadoDTO().getCdNumIden());
     * 
     * estaDadoAlta = StringUtils.isNotBlank(existeNif.getCdAbonado()); } catch
     * (ServiceException e) { log.error(e.getMessage()); } }
     * this.setUsuarioDadoAlta(estaDadoAlta); }
     **/

    private void estaDadoAltaUsuario(ExpedienteDTO expeDTO) {
        Boolean estaDadoAlta = Boolean.FALSE;
        if (expeDTO.getRepresentanteLegal() != null
                && expeDTO.getRepresentanteLegal().getCdNumIden() != null
                && expeDTO.getMedioComunicacionDTO() != null
                && expeDTO.getMedioComunicacionDTO().getIdValor() != null
                && expeDTO.getMedioComunicacionDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(
                                Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))) {
            try {
                NoteExisteNifOut existeNif = this.noteService.existeNif(
                        NoteAdmeService.ConstantesNote.COD_APP,
                        expeDTO.getRepresentanteLegal().getCdNumIden());
                estaDadoAlta = StringUtils.isNotBlank(existeNif.getCdAbonado());
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }
        } else if (Objects.nonNull(expeDTO) && Objects.nonNull(expeDTO.getInteresadoDTO())) {
            try {
                NoteExisteNifOut existeNif = this.noteService.existeNif(
                        NoteAdmeService.ConstantesNote.COD_APP,
                        expeDTO.getInteresadoDTO().getCdNumIden());

                estaDadoAlta = StringUtils.isNotBlank(existeNif.getCdAbonado());
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }
        }
        this.setUsuarioDadoAlta(estaDadoAlta);
    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    public Date obtenerPlazoSubsanacion() throws ServiceException {
        Date fechaPlazoSub = this.documentoPtfrDTO.getFcFinPlazoSubsanacion();

        ExpedienteDTO expeDTO = this.expedienteBean.getExpedienteDTO();

        this.setPlazoSuperadoTramitePendiente(this.pendienteSuperaFecha(fechaPlazoSub, expeDTO));

        return fechaPlazoSub;
    }

    /**
     *
     * @param fechaPlazo
     * @param expeDTO
     * @return
     */
    private Boolean pendienteSuperaFecha(Date fechaPlazo, ExpedienteDTO expeDTO) {
        Boolean plazoSuperado = Boolean.FALSE;
        LocalDate fechaHoy = LocalDate.now();
        if (utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS)
                .equals(expeDTO.getEstadoRevAdminDTO().getIdValor())) {
            LocalDate fechaPlazoSub = fechaPlazo.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            plazoSuperado = fechaHoy.compareTo(fechaPlazoSub) < 0;
        }

        return plazoSuperado;
    }
    
    private void comprobarGestoresExternos(ConvocatoriaDTO conv, Integer idConvocatoria, String login) {
        List<Integer> lstTipoTramite = this.convocatoriaGestorService
                .obtenerTiposTramite(idConvocatoria, login);
        for (Integer tipotramit : lstTipoTramite) {
            if (tipotramit != null && tipotramit == 1) {
                conv.setEsGestorConcesion(true);
            } else if (tipotramit != null && tipotramit == 2) {
                conv.setEsGestorJCT(true);
            }
        }
    }

//    private void comprobarGestoresExcep(ConvocatoriaDTO conv) {
//        // si tiene gestores excepcionales
//        if (conv.getListConvocatoriaGest() != null && !conv.getListConvocatoriaGest().isEmpty()) {
//
//            for (ConvocatoriaGestorDTO convGest : conv.getListConvocatoriaGest()) {
//                // vemos si alguno es el usuario logado y es ese caso le
//                // deshabilitamos las pestañas correspondientes
//                if (convGest.getCdUsuario().equals(this.infoUsuarioBean.getLogin())) {
//
//                    if (convGest.getTipoTramite() != null && convGest.getTipoTramite() == 1) { // gestor
//                                                                                               // para
//                                                                                               // concesion
//                        conv.setEsGestorConcesion(true);
//                    } else if (convGest.getTipoTramite() != null
//                            && convGest.getTipoTramite() == 2) { // gestor para
//                                                                 // JCT
//                        conv.setEsGestorJCT(true);
//                    }
//                }
//            }
//        }
//
//    }

    /**
     * Recupera el listado de entidades ExpedienteReqDTO que cumple con los
     * criterios de búsqueda. Devuelve las entidades situadas entre las
     * posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Asignatura
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocDTO> obtenerListaAlegacionesExpediente(final int index,
            final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();
        List<ExpedienteDocDTO> listadoAux = new ArrayList<>();
        List<ExpedienteDTO> lstExpedienteEstruc = new ArrayList<>();

        this.lstExpedienteDoc = new ArrayList<>();
        try {
            if (this.idExpediente != null) {

                lstExpedienteEstruc = this.expedienteService
                        .obtenerExpedienteEstructuradoSinActorParticipante(
                                this.expedienteBean.getExpedienteDTO().getIdSolicitud(),
                                Constantes.AfirmacionNegacion.N, this.idExpediente);

                lstExpedienteEstruc
                        .removeIf(x -> Boolean.FALSE.equals(x.getEsExpedienteParticipante()));

                for (ExpedienteDTO expDTO : lstExpedienteEstruc) {

                    this.setExisteEnvioPtfrEnCola(Boolean.FALSE);
                    List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                    List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;
                    filtrosEntrada
                            .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                                    expDTO.getIdExpediente(), Operador.EQUALS));
                    // MD-2190 Quitamos el filtro del trámite alegación exclusión provisional
//                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
//                            utilConstantes.getGlobalCatalogosValores()
//                                    .get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL),
//                            Operador.EQUALS));
                    filtrosEntrada
                            .add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    this.lstExpedienteDoc = this.expedienteDocService.buscar(index, pageSize,
                            filtrosEntrada, orden);

                    String ObsGestor = null;

                    for (ExpedienteDocDTO expedienteDocDTOAux : this.lstExpedienteDoc) {
                        expedienteDocDTOAux.getExpedienteDTO()
                                .setCdExpediente(expDTO.getCdExpediente());
                        // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional 
//                        if (expedienteDocDTOAux.getTlObservacionesGestor() != null
//                                && expedienteDocDTOAux.getTipoDocumentoDTO().getIdValor()
//                                        .equals(utilConstantes.getGlobalCatalogosValores().get(
//                                                Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL))
                    // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional                    
//                                && expedienteDocDTOAux.getTramiteDTO().getIdValor()
//                                        .equals(utilConstantes.getGlobalCatalogosValores().get(
//                                                Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL))
//                                        ) {
                            ObsGestor = expedienteDocDTOAux.getTlObservacionesGestor();
//                        }
                        this.expedienteDocDTO.setTlObservacionesGestor(ObsGestor);
                        listadoAux.add(expedienteDocDTOAux);
                        filtrosEntrada.clear();
                    }
                }

                listado.setResultados(listadoAux);
                listado.setTotal(listadoAux.size());

                this.expedienteBean.getExpedienteDTO()
                        .setLstExpedienteDocDTO(this.lstExpedienteDoc);

                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());

                this.estaDadoAltaUsuario(this.expedienteBean.getExpedienteDTO());

            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }

    /**
     * Metodo que controla la descarga de fichero ConvocatoriaDocDTO
     *
     * @param Integer convocatoriaDocDTO
     */
    public void descargarDocumentoAlegaciones(Integer cdFica) {

        ExpedienteDocDTO expDoc = null;
        try {

            if (Objects.nonNull(cdFica)) {

                // Se recupera el archivo a traves del servicio dado el idFica
                expDoc = this.expedienteDocService.descargar(cdFica);

                // Se calcula el nombre del fichero
                String strNombreDoc = expDoc.getFicaFichero().getNombreFichero();

                // Se prepara el fichero.
                UtilesWeb.openFile(expDoc.getContenidoFichero(),
                        expDoc.getFicaFichero().getExtension(),
                        FilenameUtils.removeExtension(strNombreDoc));

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }

    }

    /**
     * Método para aceptar Alegacion
     */
    public void aceptarAlegacionOk(String tlObservacionesGestor) {

        if (this.idExpediente != null) {

            try {

                ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(this.idExpediente);
                expedienteDTO.getEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REVISION));
                expedienteDTO.getEstadoDTO()
                        .setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION);
                this.expedienteService.guardar(expedienteDTO);

                if (!this.lstExpedienteReq.isEmpty()) {

                    for (ExpedienteReqDTO expedienteReqDTO : lstExpedienteReq) {

                        expedienteReqDTO.setItCumple("S");
                        this.expedienteReqService.guardar(expedienteReqDTO);
                    }

                    if (tlObservacionesGestor != null) {

                        for (ExpedienteDocDTO expDocDTO : this.lstExpedienteDoc) {
                         // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional
//                            if (expDocDTO.getTipoDocumentoDTO().getIdValor().equals(utilConstantes
//                                    .getGlobalCatalogosValores()
//                                    .get(Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL))
                                    // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional
//                                    && expDocDTO.getTramiteDTO().getIdValor().equals(utilConstantes
//                                            .getGlobalCatalogosValores()
//                                            .get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL))
//                                    ) {

                                expDocDTO.setTlObservacionesGestor(tlObservacionesGestor);
                                this.expedienteDocService.guardar(expDocDTO);
//                            }

                        }

                    }
                }

                this.mostrarAceptarAlegaciones = Boolean.TRUE;
                this.mostrarRechazarAlegaciones = Boolean.TRUE;

                this.actualizarValoresFormulario();

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_ALEGACION_ACEPTAR_OK);

            } catch (ServiceException e) {
                log.error(AtlasMessageUtils.getStringFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_ALEGACION_ACERTAR_KO), e);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_ALEGACION_ACERTAR_KO);
            }

        }
    }

    /**
     * Método para enviar a PTFR informe de requisitos excluidos
     *
     * @param convDTO Datos de la convocatoria
     */
    public void enviarExcluirPortafirmasAlegacionesExclusion(String tlObservacionesGestor) {
        try {

            ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);

            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);

            Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                    expedienteDTO, idExpediente,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION),
                    null, null, null, null);

            if (insertado.equals(Boolean.TRUE)) {
                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);

                if (tlObservacionesGestor != null) {

                    for (ExpedienteDocDTO expDocDTO : this.lstExpedienteDoc) {
                     // MD-2190 Quitamos la condición del trámite de alegación excclusión provisional
//                        if (expDocDTO.getTipoDocumentoDTO().getIdValor()
//                                .equals(utilConstantes.getGlobalCatalogosValores().get(
//                                        Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL))
                                // MD-2190 Quitamos la condición del trámite de alegación excclusión provisional
//                                && expDocDTO.getTramiteDTO().getIdValor().equals(utilConstantes
//                                        .getGlobalCatalogosValores()
//                                        .get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL))
//                                ) {

                            expDocDTO.setTlObservacionesGestor(tlObservacionesGestor);
                            this.expedienteDocService.guardar(expDocDTO);
//                        }

                    }

                }

                this.mostrarRechazarAlegaciones = Boolean.TRUE;
                this.mostrarAceptarAlegaciones = Boolean.TRUE;

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        }
    }

      // ya no se usa
//    public Boolean mostrarAlegacionExclusionProvisional() throws ServiceException {
//        Boolean mostrar = Boolean.FALSE;
//
//        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
//        ExpedienteDocDTO expedienteDocDTO = this.expedienteDocService.buscarPorIdExpTipoDoc(
//                this.idExpediente,
//                utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL),
//                utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL));
//
//        if (convocatoriaDTO.getItAlegacion().equals(Constantes.AfirmacionNegacion.S)
//                && expedienteDocDTO.getTipoDocumentoDTO() != null
//                && expedienteDocDTO.getTipoDocumentoDTO().getIdValor() != null
//                && expedienteDocDTO.getTipoDocumentoDTO().getIdValor()
//                        .equals(utilConstantes.getGlobalCatalogosValores()
//                                .get(Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL))
//                && expedienteDocDTO.getTramiteDTO().getIdValor()
//                        .equals(utilConstantes.getGlobalCatalogosValores()
//                                .get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL))) {
//            mostrar = Boolean.TRUE;
//        }
//
//        return mostrar;
//    }

    // dialogs para subsanacion administrativa

    public void abrirSubsanacionReqDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogSubsanacionReqDoc");
    }

    public void cerrarSubsanacionReqDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogSubsanacionReqDoc");
    }

    public void abrirSubsanacionReqDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogSubsanarReqDocumento");
    }

    public void cerrarSubsanacionReqDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogSubsanarReqDocumento");
    }

    public void abrirExclusionDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
//        PrimefacesUtils.mostrarPopupPF("dialogExclusionDoc");
        PrimefacesUtils.mostrarPopupPF("dialogExcluirDocumento");
    }

    public void cerrarExclusionDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
//        PrimefacesUtils.ocultarPopupPF("dialogExclusionDoc");
        PrimefacesUtils.ocultarPopupPF("dialogExcluirDocumento");
    }

    public void abrirExclusionDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogExcluirDocumento");
    }

    public void cerrarExclusionDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogExcluirDocumento");
    }

    public void abrirAgregarDocumentoReqDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogAgregarDocumentoReq");
    }

    public void cerraAgregarDocumentoReqDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogAgregarDocumentoReq");
    }

    /**
     * Método para enviar a PTFR informe de requisitos a subsanar
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void crearDocumentoSubsanacion(Integer idConvocatoria) {
        try {
            this.existeNoCumpleSinObs = Boolean.FALSE;
            // Recuperamos los datos del expediente
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);

            // Validamos si el expediente esta en estado pendiente requerimiento
            if (!utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                    .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())
                    && !utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)
                            .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())
                    && !utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                            .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
            } else {
                // Quitamos los registros de baja
                List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

                // Validamos si existe algún registro pendiente de calificación
                Boolean existePendienteCalificar = lstExpReq.stream()
                        .anyMatch(f -> StringUtils.isBlank(f.getItCumpleMod()));
                
                // Validamos si existe algún registro que no cumple y sin observacion
                Boolean existeNoCumpleSinObservacion = lstExpReq.stream()
                        .anyMatch(f -> Constantes.AfirmacionNegacion.N.equals(f.getItCumpleMod())
                                && StringUtils.isBlank(f.getTlObservaciones()));
                
                if (Boolean.TRUE.equals(existePendienteCalificar)) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Err.REVISION_CALIFICACION_REQUISITOS_INCOMPLETO);
                } else if (Boolean.TRUE.equals(existeNoCumpleSinObservacion)) {
                    this.existeNoCumpleSinObs = Boolean.TRUE;
                    PrimeFaces.current().ajax().update(this.formularioRequisitos);
                } else {
                    Date date = new Date();
                    for (ExpedienteReqDTO expeReqDTO : lstExpReq) {
                        if (Constantes.AfirmacionNegacion.N.equals(expeReqDTO.getItCumpleMod())) {

                            ConvocatoriaReqDTO convocatoriaReqDTO = this.convocatoriaReqService
                                    .buscarExistencia(idConvocatoria,
                                            expeReqDTO.getRequisitoDTO().getIdValor());
                            ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO = convocatoriaReqDTO
                                    .getConvocatoriaTipoDocDTO();
                            CatalogosValoresDTO catalogosValoresDTO = convocatoriaTipoDocDTO
                                    .getTipoDocumentoDTO();
                            Integer documento = catalogosValoresDTO.getIdValor();

                            ExpedienteDocDTO expeDocDTO = new ExpedienteDocDTO();
                            expeDocDTO.setEstadoDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores()
                                            .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)));
                            expeDocDTO.setExpedienteDTO(new ExpedienteDTO(
                                    expeReqDTO.getExpedienteDTO().getIdExpediente()));

                            expeDocDTO.setTipoDocumentoDTO(new CatalogosValoresDTO(
                                    convocatoriaReqDTO.getConvocatoriaTipoDocDTO()
                                            .getTipoDocumentoDTO().getIdValor()));

                            expeDocDTO.setTramiteDTO(new CatalogosValoresDTO(utilConstantes
                                    .getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)));
                            expeDocDTO.setItAdicional(Constantes.AfirmacionNegacion.N);
                            expeDocDTO.setFcAltaFila(date);
                            expeDocDTO = this.expedienteDocService.guardar(expeDocDTO);
                            this.expedienteDocHistService.crearHistoricoExpedienteDoc(
                                    ExpedienteDocToEntity.INSTANCE.apply(expeDocDTO), null);

                            expeReqDTO.setIdExpedienteDoc(expeDocDTO.getIdExpedienteDoc());
                            expeReqDTO.setCdUsuModif(expeDocDTO.getCdUsuAlta());
                            expeReqDTO.setFcModifFila(date);
                            this.expedienteReqService.guardarConHistorico(expeReqDTO);
                            log.info("expedienteReq actualizado");

                            expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                            expedienteDTO.setEstadoRevAdminDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)));
                            expedienteDTO.setEstadoDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)));
                            expedienteDTO.setCdUsuModif(expeDocDTO.getCdUsuAlta());
                            expedienteDTO.setFcModifFila(date);
                            this.expedienteService.guardar(expedienteDTO);
                            this.expedienteHistDAO.crearHistorico(
                                    ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                                    expeDocDTO.getCdUsuAlta());
                            log.info("expediente actualizado");

                        }
                    }

                    this.actualizarValoresFormulario();

                    this.calcularExisteEnvioPortafirmas(expedienteDTO);

                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.CREAR_DOCUMENTO_SUBSANACION_REQUISITOS);

//                    ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);
//                    
//                    Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente, 
//                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),null,null,null,null);
//                    
//                    if(insertado.equals(Boolean.TRUE)) {
//                        this.actualizarValoresFormulario();
//                        AtlasMessageUtils.addInfoMessageFromBundle(
//                              ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
//                    }else {
//                        AtlasMessageUtils.addErrorMessageFromBundle(
//                              ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
//                    }
//                        
//                    this.actualizarValoresFormulario();
//                    
//                    this.calcularExisteEnvioPortafirmas(expedienteDTO);

                }

            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }
    }

}
