package org.madrid.saii.tramitacion.catalogos.jsf;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.TramiteSolicitanteExpedienteDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToDTO;
import org.madrid.saii.lib.services.BloqueService;
import org.madrid.saii.lib.services.CataService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ComponenteService;
import org.madrid.saii.lib.services.ConvocatoriaComService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DatoService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioNotificacionesPortafirmasService;
import org.madrid.saii.lib.services.ExpedienteContratoDocService;
import org.madrid.saii.lib.services.ExpedienteContratoService;
import org.madrid.saii.lib.services.ExpedienteHistService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.ImpresoService;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.SolicitudService;
import org.madrid.saii.lib.services.TramiteService;
import org.madrid.saii.lib.services.TramiteSolicitanteExpedienteService;
import org.madrid.saii.lib.services.UnidadAdminService;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Solicitud}</code>
 *
 * @author everis
 *
 * @since 01-oct-2020 12:22:25
 *
 * @see ExpedienteBean
 */
@Slf4j
@Getter
@Setter
@Component("expedienteBean")
@Scope("request")
public class ExpedienteBean extends CatalogoBean<Expediente, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * dto de expediente
     */
    private ExpedienteDTO expedienteDTO;
    

    private ExpedienteJusTecDTO expedienteJustecDTO;


    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Id de Solicitud
     */
    private Integer idSolicitud;

    /**
     * Id de Expediente
     */
    private Integer idExpediente;

    /**
     * Representa el campo estadoRevAdmin
     */
    private CatalogosValoresDTO estadoRevAdminDTO;

    /**
     * Representa el campo estadoRevRequi
     */
    private CatalogosValoresDTO estadoRevRequiDTO;

    /**
     * Representa el campo estadoRevEval
     */
    private CatalogosValoresDTO estadoRevEvalDTO;

    /**
     * Atributo que indica que pestana se encuentra activa.
     */
    private int pestanaRevisionActiva;

    /**
     * Indicador para habilitar permisos de calificación de requisitos
     */
    private Boolean activarBotonesResolucion;

    /** Total de anualidades */
    private int totalAnualidades;

    /** Lista de anualidades */
    private List<Integer> listaAnualidades;

    /** Valores de presupuestos */
    private List<Integer> listaNcolPresupuesto;

    private List<Integer> listaNcolActuacion;

    private Double montoAyudaActuacion;

    private List<ExpedienteDTO> lstExpedientes = new ArrayList<>();

    /** Filtro de estado seleccionado para la tabla de expedientes */
    private Integer idEstadoFiltro;

    private List<ExpedienteDTO> lstExpedienteParticipante = new ArrayList<>();
    
    private List<ExpedienteDTO> lstExpedienteConContrato = new ArrayList<>();
    
    /** DTO de la convocatoria **/
    private ConvocatoriaDTO convocatoriaDTO;
    
    /**
     * Indicador para habilitar pestaña revision de contratos
     */
    private Boolean activarPestanaRevContrato;
    
    /**
     * Indicador para habilitar pestaña de contrato
     */
    private Boolean activarPestanaContrato;
    
    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrDesistidoEnProceso;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrDesistidoFirmado;
    
    
    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrDesistidoEnCola; 
    
    /**
     * Atributos para el documento de envio de portafirmas
     */
    private DocumentoPtfrDTO documentoPtfrDTO;
    
    /**
     * Indicador para habilitar pestaña en rojo de la JusTec
     */
    private Boolean activarPestanaRojo;
    
    /**
     * Indicador para habilitar pestaña en rojo de la JusEco
     */
    private Boolean activarPestanaRojoEco;
    
    private String cadenaNotaMaxima;
    
    private ExpedienteDocDTO expedienteDocDTO;

    private Boolean esConvocatoriaConceptoPresup;
    
    private ConvocatoriasBean convocatoriasBean;
    
    private SolicitudBean solicitudBean;
    
    private List<ExpedienteAnualidadDTO> lstPlazosEjecucion;
    
    /*
     * Lista con el número de columnas a mostrar en la tabla dinámica de
     * plazos
     */
    private List<Integer> listaNcolPlazo;
    
    /* Lista bloque plazos componente columna */
    private List<BloqueComponenteDTO> lstBloqueComponenteColumnaPlazo;

    /* Lista bloque plazos componente fila */
    private List<BloqueComponenteDTO> lstBloqueComponenteFilaPlazo;
    
    /* lista de bloque plazos solicitados */
    private List<BloqueDTO> lstBloquePlazos;
    
    /* lista de numero de interrupciones (multiregistro) */
    private List<Integer> lstNumFilasMultiregistro;

    /* VMM 15/10/26 MD-3152: filas de cada bloque multiregistro, por idBloque. Antes solo habia
     * lstNumFilasMultiregistro, compartida por todos los bloques multiregistro del formulario, asi que
     * el ultimo bloque calculado pisaba el numero de filas de los demas */
    private Map<Integer, List<Integer>> lstNumFilasMultiregistroPorBloque = new HashMap<>();

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio de expedienteHistService
     */
    @Autowired
    @Qualifier("expedienteHistService")
    private ExpedienteHistService expedienteHistService;

    /**
     * Servicio de Ereg
     */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    /**
     * Servicio de Pdf
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * Servicio de Pdf
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    @Autowired
    @Qualifier("convocatoriaComService")
    private ConvocatoriaComService convocatoriaComService;

    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;
    
    /** Servicio de Ereg. */
    @Autowired
    @Qualifier("EnvioNotificacionesPortafirmasService")
    private EnvioNotificacionesPortafirmasService envioNotificacionesPortafirmasService;
    
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;
    
    /**
     * Servicio de solicitudService
     */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;
    
    /**
     * Servicio de expedienteContratoDocService
     */
    @Autowired
    @Qualifier("expedienteContratoDocService")
    private ExpedienteContratoDocService expedienteContratoDocService;
    
    /** TipoResolConvocatoria **/
    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;
    
    /** Tramite Service **/
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;
    
    /** Bloque Service **/
    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;
    
    /** Impreso Service **/
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;
    
    @Autowired
    @Qualifier("componenteDAO")
    private ComponenteDAO componenteDAO;
    
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    
    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;
    
    
    /**
     * Servicio de expedienteHistService
     */
    @Autowired
    @Qualifier("tramiteSolicitanteExpedienteService")
    private TramiteSolicitanteExpedienteService  tramiteSolicitanteExpedienteService ;
    
    @Autowired
    @Qualifier("cataService")
    private CataService cataService;
    
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    /** InfoUsuario */
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;

    /** DocumentoBean */
    @Autowired
    @Qualifier("documentoBean")
    private DocumentoBean documentoBean;

    /** RequisitoBean */
    @Autowired
    @Qualifier("requisitoBean")
    private RequisitoBean requisitoBean;

    /** EvaluacionBean */
    @Autowired
    @Qualifier("evaluacionBean")
    private EvaluacionBean evaluacionBean;

    /** ResolucionBean */
    @Autowired
    @Qualifier("resolucionBean")
    private ResolucionBean resolucionBean;

    /** FinanciacionBean */
    @Autowired
    @Qualifier("financiacionBean")
    private FinanciacionBean financiacionBean;

    /** ContratoBean */
    @Autowired
    @Qualifier("contratoBean")
    private ContratoBean contratoBean;

    @Autowired
    @Qualifier("expedienteJusTecBean")
    private ExpedienteJusTecBean expedienteJusTecBean;
    
    
    @Autowired
    @Qualifier("evaluacionJusTecBean")
    private EvaluacionJusTecBean evaluacionJusTecBean;
    
    
    @Autowired
    @Qualifier("justificacionCieTecBean")
    private JustificacionCieTecBean justificacionCieTecBean;
    
    @Autowired
    @Qualifier("expedienteAnualidadBean")
    private ExpedienteAnualidadBean expedienteAnualidadBean;
    
    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraBean")
    private ExpedienteModifPresPlazoOtraBean expedienteModifPresPlazoOtraBean;
    
    @Autowired
    @Qualifier("expedienteJusEcoBean")
    private ExpedienteJusEcoBean expedienteJusEcoBean;
    
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("justificacionEcoBean")
    private JustificacionEcoBean justificacionEcoBean;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ExpedienteBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad Expediente
     *
     * @return entidad de tipo ConvocatoriaSol
     */
    @Override
    public Expediente crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.inicializarExpediente();
        this.documentoBean.setExpedienteBean(this);
        this.requisitoBean.setExpedienteBean(this);
        this.evaluacionBean.setExpedienteBean(this);
        this.resolucionBean.setExpedienteBean(this);       
        this.financiacionBean.setExpedienteBean(this);
        this.contratoBean.setExpedienteBean(this);
        this.expedienteJusTecBean.setExpedienteBean(this);
        this.evaluacionJusTecBean.setExpedienteBean(this);
        this.justificacionCieTecBean.setExpedienteBean(this);
        this.evaluacionJusTecBean.setIdConvocatoria(this.idConvocatoria);
        this.requisitoBean.setIdConvocatoria(this.idConvocatoria);
        this.resolucionBean.setIdConvocatoria(this.idConvocatoria);
        this.expedienteModifPresPlazoOtraBean.setExpedienteBean(this);
        this.expedienteJusEcoBean.setExpedienteBean(this);
        this.esConvocatoriaConceptoPresup = Boolean.FALSE;
        this.justificacionEcoBean.setExpedienteBean(this);
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PUBLIC ACTIONS
    // ////////////////////////////////////////////////////////
    /**
     * Setear pIdSolicitud
     *
     * @param pIdSolicitud
     */
    public void setearIdSolicitud(Integer pIdSolicitud) {

        // Sólo debe ejecutarse tras la primera vez que se renderiza la pantalla
        // En las siguientes el id convocatoria está guardado con
        // AtlasGuardarEstado
        if (this.idSolicitud == null) {
            this.idSolicitud = pIdSolicitud;
        }
    }

    /**
     * Setear pIdExpediente
     *
     * @param pIdExpediente
     */
    public void setearIdExpediente(Integer pIdExpediente) {

        // Sólo debe ejecutarse tras la primera vez que se renderiza la pantalla
        // En las siguientes el id expediente está guardado con
        // AtlasGuardarEstado
        if (this.idExpediente == null) {
            this.idExpediente = pIdExpediente;
        }
    }

    /**
     * Recupera el listado de entidades ExpedienteDTO que cumple con los
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
    public AtlasListAndCount<ExpedienteDTO> obtenerListaExpedientes(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDTO> listadoExpedientes = new AtlasListAndCount<>();
        try {
            if (this.idSolicitud != null) {
                List<AtlasFilter> filtrosEntrada = filtros != null ? (List<AtlasFilter>) filtros
                        : new ArrayList<>();

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                        this.idSolicitud, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
                SolicitudDTO solicitudDTO = this.solicitudService.buscarId(this.idSolicitud);
                Boolean solicitudEnTramitacion = !solicitudDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

                if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
                    //quitamos los exps iniciados, propuestos, no aceptados o rechazados
                    List<Integer> lstEstadoExpInterno = new ArrayList<>();
                    lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
                    lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
                    lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
                    lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
                    
                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                            Operador.NOT_IN));
                }else {
                    //VMM 09/02/26 MD-2381: si es una solicitud Iniciada entramos en modo consulta al exp
                    this.convocatoriasBean.setModoConsulta(Boolean.TRUE);
                }
                
                List<AtlasSort> ordenamiento = orderBean != null ? (List<AtlasSort>) orderBean
                        : new ArrayList<>();
                ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_IDEXPEDIENTE, true));

                List<ExpedienteDTO> listadoEx = this.expedienteService.buscar(index, pageSize,
                        filtrosEntrada, ordenamiento);

                for (ExpedienteDTO expDTO : listadoEx) {
                    int alerta = this.expedienteService.getDao().alertaFinanciacion(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 10/02/25 MD-112: aniadimos alerta de modificaciones presupuestarias con y sin actas
                    alerta = alerta + this.expedienteService.getDao().alertaModifPesupFinanciable(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 10/02/25 MD-112: aniadimos alerta de modificaciones de plazos de anualidades con y sin actas
                    alerta = alerta + this.expedienteService.getDao().alertaModifPlazosAnualidades(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 10/02/25 MD-112: aniadimos alerta de otras modificaciones con y sin actas
                    alerta = alerta + this.expedienteService.getDao().alertaOtrasModificaciones(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 10/02/25 MD-578: aniadimos alerta de JCT
                    alerta = alerta + this.expedienteService.getDao().alertaJCT(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 16/10/25 MD-1586: aniadimos alerta de JusEco
                    alerta = alerta + this.expedienteService.getDao().alertaJEco(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    //VMM 10/02/25 MD-582: aniadimos alerta de contratos
                    alerta = alerta + this.expedienteService.getDao().alertaContratos(null,
                            expDTO.getIdSolicitud(), expDTO.getIdExpediente(), solicitudEnTramitacion);
                    
                    if (alerta > 0) {
                        expDTO.setBAviso(Boolean.TRUE);
                    }
                }

                listadoExpedientes.setResultados(listadoEx);
                listadoExpedientes.setTotal(this.expedienteService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listadoExpedientes;
    }

    /**
     * Metodo que inicializa a las Expedientes
     *
     */
    public void inicializarExpediente() {
        this.expedienteDTO = new ExpedienteDTO();

    }
    
    private List<BloqueComponenteDTO> ordenadorBloqueComponentePorFinanciacionDato(
            List<BloqueComponenteDTO> lista, BloqueDTO bloque, ExpedienteDTO expedienteDTO)
            throws ServiceException {
        List<BloqueComponenteDTO> ordenada = new ArrayList<>();
        FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion(
                Constantes.Formatos.PARTIDAS_PRESUPUESTARIAS, this.idConvocatoria,
                bloque.getIdTematica().getIdValor(),
                expedienteDTO.getConvocatoriaActDTO().getIdActor());
        List<BloqueComponenteDTO> listaNoFinan = lista.stream()
                .filter(bc -> bc.getTipoFinanciacionDTO() == null).collect(Collectors.toList());
        if (!lista.isEmpty()) {
            ordenada = lista.stream().filter(bc -> bc.getTipoFinanciacionDTO() != null)
                    .sorted((c1, c2) -> this.informeHelperPdfService.compareByNumOrden(c1, c2,
                            financiacionDTO.getListaFinanciacionDatoDTO()))
                    .collect(Collectors.toList());
        }
        ordenada.addAll(listaNoFinan);
        return ordenada;
    }

    /**
     * Metodo que setea idExpediente
     *
     * @param idExpediente
     */
    public void cargarDatosRevision(Integer idExpediente) {
        try {
            if (idExpediente != null) {

                this.setIdExpediente(idExpediente);
                this.setPestanaRevisionActiva(0);

                ExpedienteDTO expediente = this.expedienteService.buscarIdSimple(idExpediente,
                        Boolean.TRUE);

                if (expediente != null) {
                    
                    if(this.idSolicitud == null && expediente.getIdSolicitud() != null) {
                        this.setIdSolicitud(expediente.getIdSolicitud());
                    }
                    if(this.idSolicitud != null && this.getSolicitudBean() != null 
                            && (this.solicitudBean.getSolicitudDTO() == null || 
                                    this.solicitudBean.getSolicitudDTO().getIdSolicitud() == null)) {
                        SolicitudDTO solDTO = this.solicitudService.buscarId(this.idSolicitud); 
                        this.solicitudBean.setSolicitudDTO(solDTO);
                    }

                    // Recuperamos los datos de la convocatoria
                    this.convocatoriaDTO = this.convocatoriaService
                            .buscarId(this.getIdConvocatoria());
                    
                    //VMM 09/10/24 MD-657: volvemos a comprobar si se activan las pestañas de concesion o de JCT segun los gestores excepcionales
                    this.comprobarGestoresExcep(convocatoriaDTO);
                    
                    //VMM 23/04/25 MD-1459: cargamos el valor de si convocatoria con conceptos presup
                    this.esConvocatoriaConceptoPresup = Constantes.AfirmacionNegacion.S
                            .equalsIgnoreCase(convocatoriaDTO.getItConceptoPresup());

                    this.totalAnualidades = this.convocatoriaDTO.getNmDurMaxAyuda();

                    // Construimos la lista de anualidades
                    int[] anualidadesArray = IntStream.range(1, this.totalAnualidades + 1)
                            .toArray();
                    this.listaAnualidades = new ArrayList<>();

                    for (int arr : anualidadesArray) {
                        this.listaAnualidades.add(arr);
                    }

                    expediente.setCdEreg(Utils.formatearNmEreg(expediente.getCdEreg()));

                    if (expediente.getImpresoDTO() != null
                            && expediente.getImpresoDTO().getLstBloqueDTO() != null) {
                        
                        Integer idPart = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PARTICIPANTES);
                        //MD-2874: cambiamos condicion porque puede haber bloques hijos con tematica como los de presupuestos
                        //VMM 20/05/25 MD-1523: quitamos de la lista de bloques padre los que sean bloques hijo o no tengan tematica
                        expediente.getImpresoDTO().setLstBloqueDTO(
                                expediente.getImpresoDTO().getLstBloqueDTO().stream()
                                        .filter(b -> !idPart.equals(b.getIdFormato().getIdValor()) &&
                                                Objects.isNull(b.getSubNmOrden()) &&
                                                Objects.isNull(b.getIdBloquePadre()))
                                        .collect(Collectors.toList()));
                        
                        // VMM 15/10/26 MD-3152: filas de cada bloque multiregistro (se rellena en agregarBloqueMultiregistro)
                        this.lstNumFilasMultiregistroPorBloque = new HashMap<>();

                        //Modificamos formato fecha para componentes subtipo fecha
                        for(BloqueDTO bloque: expediente.getImpresoDTO().getLstBloqueDTO()) {
                                   
                            //VMM 20/05/25 MD-1523: calculamos para cada bloque su lista de subBloques hijos
                            if (bloque.getItMultiBloque() != null && bloque.getItMultiBloque().equals(Constantes.AfirmacionNegacion.S) ) {  //si es bloque padre
                               /* && (Objects.nonNull(bloque.getIdBloquePadre()) 
                                        &&  this.bloqueService.buscarId(bloque.getIdBloquePadre()).getItMultiRegistro().equals(Constantes.AfirmacionNegacion.N))) {*/
                                //hacemos una consulta para ver los subbloques hijos de cada bloque
                                this.calcularSubBloques(bloque, expediente);
                            }
                            
                            //VMM 05/06/25 MD-1522: calculamos para cada bloque mutiregistro el numero de filas o interrupciones que tendrá
                            if (bloque.getItMultiRegistro() != null && bloque.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)) { //se supone que solo son bloques padres los multiregsitro
                                this.agregarBloqueMultiregistro(bloque, expediente);
                            }else {
                                //si no es multiregsitro
                                if(bloque.getLstBloqueComponenteDTO() != null && !bloque.getLstBloqueComponenteDTO().isEmpty()) {
                                    for(BloqueComponenteDTO blCompDTO : bloque.getLstBloqueComponenteDTO()) {
                                        if (blCompDTO.getComponenteDTO() != null 
                                                && blCompDTO.getComponenteDTO().getSubTipoDTO() != null
                                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor() != null 
                                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA))
                                                && blCompDTO.getDatos() != null 
                                                && !blCompDTO.getDatos().isEmpty()) {
                                            
                                            // Obtener el valor de fecha actual (formato ISO)
                                            String datoISO = blCompDTO.getDatos().get(0).getDsValor();
                                            
                                            if (datoISO != null && !datoISO.isEmpty()) {
                                                try {
                                                    // Convertir de String ISO a Date
                                                    Instant instant = Instant.parse(datoISO);
                                                    Date fecha = Date.from(instant);
                                                    
                                                    // Formatear a dd/MM/yyyy
                                                    SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                                    String fechaFormateada = sdf.format(fecha);
                                                    
                                                    // Guardar el nuevo valor (en formato dd/MM/yyyy)
                                                    blCompDTO.getDatos().get(0).setDsValor(fechaFormateada);
                                                    
                                                } catch (Exception e) {
                                                    log.error("Error al convertir fecha: " + datoISO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            
                            if (utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO)
                                    .equals(bloque.getIdFormato().getIdValor())) {
                                bloque.setLstBloqueComponenteDTO(
                                        ordenadorBloqueComponentePorFinanciacionDato(
                                                bloque.getLstBloqueComponenteDTO(), bloque,
                                                expediente));
                            }
                            
                        }
                    }
                    

                    this.setExpedienteDTO(expediente);
                   
                    if (expediente.getExpedienteContratoSustitucion() != null) {
                        this.getExpedienteDTO().setExpedienteContratoSustitucion(expediente.getExpedienteContratoSustitucion());
                    }
                    
                    //VMM 23/04/25 MD-1459: se vuelve a descomentar porque se usa para la pestaña Datos donde carga
                    //todos los bloques del formulario de solicitud             
                    this.obtenerPresupuestoPresentado();
                    this.obtenerActuacionPresentado();
                    
                    //VMM 21/05/25 MD-1523: calculamos las tablas de plazos de ejecucion si existe este formato de bloque
                    this.obtenerPlazosSolicitados(expediente.getImpresoDTO().getIdImpreso()); 

                    this.getExpedienteDTO().setEsCerradoCoordinadorTodo(
                            this.evaluacionBean.verificarTodoInformeCerradoCoordinador());

                    this.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
                            this.evaluacionBean.verificarMostrarBotoneraEvaluadoGestor());

                    this.obtenerExpedienteParticipante(expediente);
                    
                    if(expediente.getExpedienteContratoSustitucion() != null && expediente.getExpedienteContratoSustitucion().getIdExpediente() != null
                            && (expediente.getExpedienteContrato() == null || expediente.getExpedienteContrato().getIdExpediente() == null)){
                        ExpedienteContratoDTO expCont = this.expedienteContratoService.buscarContratoInicialSustitucion(expediente);
                        if (expCont != null) {
                            expediente.setExpedienteContrato(expCont);
                        } 
                    }
                    
                    this.validarEstilo();
                    
                    this.setActivarPestanaContrato(this.validarPestanaContrato());

                    ExpedienteDTO dto = this.expedienteService
                            .comprobarResolucionDefinitiva(idExpediente, this.convocatoriaDTO);

                    // asignamos los resultados de validación al expedienteDTO
                    // de expedienteBean
                    this.getExpedienteDTO()
                            .setEsExpedienteSolicitante(dto.getEsExpedienteSolicitante());
                    this.getExpedienteDTO().setBlHabilitarResolucionDefinitiva(
                            dto.getBlHabilitarResolucionDefinitiva());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivo1(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivo1());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivo2(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivo2());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivo3(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivo3());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivo4(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivo4());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivoIndividual1(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual1());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivoIndividual2(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual2());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivoIndividual3(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual3());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivoIndividual4(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual4());
                    this.getExpedienteDTO().setBlMostrarMensajeAprobarDenegarDefinitivoIndividual5(
                            dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual5());

                    
                    
                    //GIM 29/01/26 MD-2242 vamos buscar si tienen un dopcumentod e resolucion definito o provisional en la cola sin fecha de lectura, 
                    // si tiene fecha de lectura no hacemos nada, pero si no tienen fecha de lectura cambiamos el valor del aviso para que salga azul y no rojo
                    
                    List<DocumentoPtfrDTO> listadocumentosPFTR = this.documentoPtfrService.buscarPorExpediente(idExpediente);
                    
                    List<Integer> lstTramites = new ArrayList<>();
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));

                    for (DocumentoPtfrDTO documentoPTFR : listadocumentosPFTR) {
                        if (lstTramites.contains(documentoPTFR.getTramiteDTO().getIdValor())
                                && documentoPTFR.getFcLecturaNote() == null) {
                            expediente.setBAvisoResolucion(Boolean.FALSE);
                            // MD-2397 Incluimos la condición de que cuando sea
                            // RES DES PROV y esté pendiente de alegaciones y
                            // también que no se haya revisado se vea en rojo la
                            // pestaña Resolución
                            if ((utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL)
                                    .equals(documentoPTFR.getTramiteDTO().getIdValor())
                                    || utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL) // MD-2399 incluimos Exclusion Provisional
                                            .equals(documentoPTFR.getTramiteDTO().getIdValor()))
                                    && (Boolean.FALSE.equals(expediente.getModoConsulta())
                                            && expediente.getItAlegaciones() != null
                                            && Constantes.AfirmacionNegacion.S
                                                    .equals(expediente.getItAlegaciones())
                                            && Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES
                                                    .equals(expediente.getEstadoDTO()
                                                            .getNmConstante())
                                            && (Constantes.AfirmacionNegacion.N
                                                    .equals(expediente.getItAlegacionRevisada())
                                                    || (expediente
                                                            .getItAlegacionRevisada() == null)))) {
                                expediente.setBAvisoResolucion(Boolean.TRUE);
                            }
                        }
                    }

                    //VMM 11/06/25 MD-1522: 
                    //consultamos la lista de expedientes doc entera incluidos los doc asociados a esos datos de interrupciones 
                    //y los ordenamos por nmFila o por el secuencial del saii_dato
                    List<ExpedienteDocDTO> lstExpDocOrdenadaByNmFila = this.expedienteDocDAO.
                            consultarExpDocByDatoNmFila(expedienteDTO.getIdExpediente());
                    
                    expedienteDTO.setLstExpedienteDocDTO(lstExpDocOrdenadaByNmFila);

                    // Referenciamos el identificador del expediente
                    this.documentoBean.setIdExpediente(idExpediente);
                    this.requisitoBean.setIdExpediente(idExpediente);
                    this.financiacionBean.setIdExpediente(idExpediente);
                    this.financiacionBean.setIdEstadoExpediente(
                            this.getExpedienteDTO().getEstadoDTO().getIdValor());
                    this.financiacionBean.obtenerDatosFinanciacionExpediente();

                    this.expedienteJusTecBean.setIdExpediente(idExpediente);
                    this.expedienteJusTecBean.cargarInformacion();
                    
                    this.activarPestanaRojo = Boolean.FALSE;
                    
                    //Esto es para la Evaluacion de la justificacion cientifico tecnica
                    for (ExpedienteJusTecDTO expedienteJusTecDTO : this.expedienteJusTecBean.getListaExpedienteJustec()) {
                        if (expedienteJusTecDTO.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION)) &&
                                !this.convocatoriasBean.isModoConsulta()) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es un auditor
                            this.activarPestanaRojo = Boolean.TRUE;
                        }
                    }
                    
                    // MD-1585 JRFM Nueva tabla justificacion economica
                    this.expedienteJusEcoBean.setIdExpediente(idExpediente);
                    this.expedienteJusEcoBean.cargarInformacion();
                    
                    //VMM 26/01/26 MD-2204: metemos el control para poner la pestaña de la jus eco en rojo según los estados de cada periodo
                    this.activarPestanaRojoEco = Boolean.FALSE;
                    
                    for (ExpedienteJusEcoDTO expedienteJusEcoDTO : this.expedienteJusEcoBean.getListaExpedienteJuseco()) {
                        if (expedienteJusEcoDTO.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)) ||
                                expedienteJusEcoDTO.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION)) &&
                                !this.convocatoriasBean.isModoConsulta()) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es un auditor
                            this.activarPestanaRojoEco = Boolean.TRUE;
                        }
                    }
                    
                    this.expedienteAnualidadBean.setIdExpediente(idExpediente);
                    this.expedienteAnualidadBean.cargarInformacion();                    
        
                    this.evaluacionJusTecBean.cargarDatos();
                    
                    this.justificacionCieTecBean.setIdExpediente(idExpediente);
                    this.justificacionCieTecBean.cargarInformacion();
                    
                    this.justificacionEcoBean.setIdExpediente(idExpediente);
                    this.justificacionEcoBean.cargarInformacion();
                    
                    this.documentoBean.inicializarContratos();
                    
                    this.expedienteModifPresPlazoOtraBean.setIdExpediente(idExpediente);
                    this.expedienteModifPresPlazoOtraBean.cargarInformacion();
                    this.expedienteModifPresPlazoOtraBean.setIdEstadoExpediente(
                            this.getExpedienteDTO().getEstadoDTO().getIdValor());
                  
                  //VMM 06/02/26 MD-2339: no ponemos en rojo las pestañas si es modo consulta (auditor)
                  if (this.convocatoriasBean.isModoConsulta()) { 
                      expedienteDTO.setBAvisoContratos(Boolean.FALSE);
                      expedienteDTO.setBAvisoDocumento(Boolean.FALSE);
                      expedienteDTO.setBAvisoRequisitos(Boolean.FALSE);
                      expedienteDTO.setBAvisoResolucion(Boolean.FALSE);
                      expedienteDTO.setBAvisoEvaluacion(Boolean.FALSE);
                      expedienteDTO.setBAvisoJustificacion(Boolean.FALSE);
                  }
                  
                  if (Boolean.TRUE.equals(comprobarResolucionProvisionalRevisada())) {
                      expediente.setBAvisoResolucion(Boolean.TRUE);
                  }
                  
                  
                }
            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.EXPEDIENTE_MENSAJE_CARGAR_KO, e);
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.EXPEDIENTE_MENSAJE_CARGAR_KO);
        }
    }
    
    
    /**
     * Comprueba si la resolución provisional asociada al expediente ha sido leída/revisada o han vencido sus plazos.
     * Método público para poder ser invocado desde otros Beans (ej: ResolucionBean).
     * 
     * @return Boolean.TRUE si está revisada o leída; Boolean.FALSE en caso contrario.
     * @throws ServiceException si ocurre un error en las consultas de servicios.
     */
    public Boolean comprobarResolucionProvisionalRevisada() throws ServiceException {
        if (this.expedienteDTO == null) {
            return Boolean.FALSE;
        }

        // 1. Cargar convocatoria si es nula en el Bean
        if (this.convocatoriaDTO == null && this.getIdConvocatoria() != null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }

        // 2. Mapear trámites según el estado provisional
        List<Integer> tramites = new ArrayList<>();
        Integer estadoProv = this.expedienteDTO.getEstadoProvisionalDTO() != null 
            ? this.expedienteDTO.getEstadoProvisionalDTO().getIdValor() 
            : null;

        if (estadoProv != null) {
            if (estadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))) {
                tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
            } else if (estadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
            } else if (estadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))) {
                tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
            } else if (estadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))) {
                tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
            }
        }

        // Si no está en ninguno de los 4 estados provisionales, salimos inmediatamente
        if (tramites.isEmpty()) {
            return Boolean.FALSE;
        }

        // 3. Consultar documentos PTFR
        List<AtlasFilter> filtroPtfr = new ArrayList<>();
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, 
            this.expedienteDTO.getIdExpediente(), Operador.EQUALS));
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, tramites, Operador.IN));
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<DocumentoPtfrDTO> lstDocumentoPtfr = this.documentoPtfrService.buscar(0, -1, filtroPtfr, null);

        if (lstDocumentoPtfr == null || lstDocumentoPtfr.isEmpty()) {
            return Boolean.FALSE;
        }

        DocumentoPtfrDTO documentoPtfr = lstDocumentoPtfr.get(0);
        Date fcHoy = new Date();

        if (documentoPtfr == null || this.convocatoriaDTO == null) {
            return Boolean.FALSE;
        }

        // 4. Evaluar según lógica de convocatoria y NOTE
        if (Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItAlegacion())) {
            
            if (Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItEvaluacion())) {
                if (Constantes.AfirmacionNegacion.S.equals(this.expedienteDTO.getItAlegacionRevisada())) {
                    return Boolean.TRUE;
                } else if (documentoPtfr.getFcLecturaNote() != null) {
                    Date fechaFinAlegacion = this.cataService.calcularDiasHabiles(
                        documentoPtfr.getFcLecturaNote(), 11, Constantes.Suca.CD_MUNI_MADRID,
                        this.expedienteDTO.getInteresadoDTO().getCdLocalidad());
                    if (fcHoy.after(fechaFinAlegacion)) {
                        return Boolean.TRUE;
                    }
                } else if (documentoPtfr.getFcLecturaNote() == null 
                         && documentoPtfr.getFcEnvioNote() != null 
                         && documentoPtfr.getFcFinPlazoSubsanacion() != null 
                         && fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion())) {
                    return Boolean.TRUE;
                }

            } else if (Constantes.AfirmacionNegacion.N.equals(this.convocatoriaDTO.getItEvaluacion())) {
                if (documentoPtfr.getFcLecturaNote() != null) {
                    if (this.expedienteDTO.getItAlegacionRevisada() != null) {
                        return Boolean.TRUE;
                    }
                } else if (documentoPtfr.getFcLecturaNote() == null 
                         && documentoPtfr.getFcEnvioNote() != null 
                         && documentoPtfr.getFcFinPlazoSubsanacion() != null 
                         && fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion())) {
                    return Boolean.TRUE;
                }
            }

        } else if (Constantes.AfirmacionNegacion.N.equals(this.convocatoriaDTO.getItAlegacion())) {
            if (documentoPtfr.getFcLecturaNote() != null) {
                return Boolean.TRUE;
            } else if (documentoPtfr.getFcLecturaNote() == null && documentoPtfr.getFcEnvioNote() != null) {
                if ("3".equals(documentoPtfr.getCdEstadoNote()) || "4".equals(documentoPtfr.getCdEstadoNote())) {
                    return Boolean.TRUE;
                }
                Date fechaFinLecturaNote = this.cataService.calcularDiasHabiles(
                    documentoPtfr.getFcEnvioNote(), 10, Constantes.Suca.CD_MUNI_MADRID,
                    this.expedienteDTO.getInteresadoDTO().getCdLocalidad());

                if (fcHoy.after(fechaFinLecturaNote)) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }
    
    
    
    private void agregarBloqueMultiregistro(BloqueDTO bloque,
            ExpedienteDTO dto)  throws ServiceException  {
       
       List<BloqueDTO> listaSubbloqueDTO = dto.getImpresoDTO().getLstBloqueDTO().stream()
              .filter(x->(( x.getIdBloquePadre() != null && x.getIdBloquePadre().equals(bloque.getIdBloque())) 
                          || x.getIdBloque().equals(bloque.getIdBloque()) ) && Objects.isNull(x.getFcBajaFila()))
              .sorted(Comparator.comparing(BloqueDTO::getNmOrden)).collect(Collectors.toList());
       
       for (BloqueDTO bloqueDTO : listaSubbloqueDTO) {
           for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                   .getLstBloqueComponenteDTO()) {
               // Si está dado de baja lo ignoramos
               if (bloqueComponenteDTO.getFcBajaFila() != null) {
                   continue;
               }
              if(bloqueComponenteDTO.getDatos() != null && !bloqueComponenteDTO.getDatos().isEmpty()){
                  this.datoService.rellenarDatosMultiregistro(dto, bloqueDTO, bloqueComponenteDTO);
              }            
           }
       }    
       this.lstNumFilasMultiregistro = null;
       this.formatearBloqueMultiregistro(listaSubbloqueDTO);
       // VMM 15/10/26 MD-3152: guardamos las filas de ESTE bloque (formatearBloqueMultiregistro las deja
       // en lstNumFilasMultiregistro, que el siguiente bloque multiregistro sobrescribe)
       // y quitamos las filas en las que todos los campos estan vacios (el formulario de la solicitud guarda
       // una primera fila vacia, NM_FILA = 1)
       List<Integer> filasBloque = new ArrayList<>();
       if (this.lstNumFilasMultiregistro != null) {
           for (Integer fila : this.lstNumFilasMultiregistro) {
               if (this.filaMultiregistroTieneValor(listaSubbloqueDTO, fila)) {
                   filasBloque.add(fila);
               }
           }
       }
       this.lstNumFilasMultiregistroPorBloque.put(bloque.getIdBloque(), filasBloque);
   }

   /**
    * VMM 15/10/26 MD-3152: indica si algun campo de la fila (indice) del bloque multiregistro tiene valor
    */
   private boolean filaMultiregistroTieneValor(List<BloqueDTO> listaSubbloqueDTO, int fila) {
       for (BloqueDTO subBloque : listaSubbloqueDTO) {
           if (Boolean.TRUE.equals(subBloque.getEsTipoDocumento())
                   || subBloque.getLstBloqueComponenteDTO() == null) {
               continue;
           }
           for (BloqueComponenteDTO blqComp : subBloque.getLstBloqueComponenteDTO()) {
               if (blqComp.getDatos() != null && blqComp.getDatos().size() > fila
                       && blqComp.getDatos().get(fila) != null
                       && StringUtils.isNotBlank(blqComp.getDatos().get(fila).getDsValor())) {
                   return true;
               }
           }
       }
       return false;
   }


    private void calcularSubBloques(BloqueDTO bloque, ExpedienteDTO expediente) throws ServiceException {
        
        //obtenemos el listado de sus hijos
        List<BloqueDTO> lstBloquesHijos = this.bloqueService.buscarByIdBloquePadre(bloque.getIdBloque());
        
        if (lstBloquesHijos != null && !lstBloquesHijos.isEmpty()) {
            bloque.setLstBloquesHijoDTO(lstBloquesHijos);
            
            //Modificamos formato fecha para componentes subtipo fecha
            for (BloqueDTO bloqueHijo: lstBloquesHijos) {
                                    
                if(bloqueHijo.getLstBloqueComponenteDTO() != null && !bloqueHijo.getLstBloqueComponenteDTO().isEmpty()) {
                    for (BloqueComponenteDTO blCompDTO : bloqueHijo.getLstBloqueComponenteDTO()) {
                        if (blCompDTO.getComponenteDTO() != null 
                                && blCompDTO.getComponenteDTO().getSubTipoDTO() != null
                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor() != null 
                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA))
                                && blCompDTO.getDatos() != null 
                                && !blCompDTO.getDatos().isEmpty()) {
                            
                            // Obtener el valor de fecha actual (formato ISO)
                            String datoISO = blCompDTO.getDatos().get(0).getDsValor();
                            
                            if (datoISO != null && !datoISO.isEmpty()) {
                                try {
                                    // Convertir de String ISO a Date
                                    Instant instant = Instant.parse(datoISO);
                                    Date fecha = Date.from(instant);
                                    
                                    // Formatear a dd/MM/yyyy
                                    SimpleDateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA);
                                    String fechaFormateada = sdf.format(fecha);
                                    
                                    // Guardar el nuevo valor (en formato dd/MM/yyyy)
                                    blCompDTO.getDatos().get(0).setDsValor(fechaFormateada);
                                    
                                } catch (Exception e) {
                                    log.error("Error al convertir fecha de bloque hijo: " + datoISO);
                                }
                            }
                        }
                        //VMM 11/06/25 MD-1522: controlamos los documentos de las interrupciones
                        //un bloque hijo de tipo documento solo va a tener un bloque componente asociado
                        //pero ese bloque componente puede tener varios datos asociados para guardar expdteDocs del multiregistro padre
                        //estan saliendo mas datos que no corresponden al expdte seleccionado asiq filtramos y ordenamos aqui
                        if (Boolean.TRUE.equals(bloqueHijo.getEsTipoDocumento()) &&
                                expediente.getLstExpedienteDocDTO() != null && !expediente.getLstExpedienteDocDTO().isEmpty()) {

                                //ordenamos la lista de datos asociada los subbloques de tipo documento
                                List<DatoDTO> lstDatoDTO = blCompDTO.getDatos();
                                List<DatoDTO> lstDatoOrdenadaByNmFila = lstDatoDTO.stream()
                                        .filter(dato -> dato.getNmFila() != null
                                                && dato.getIdBloqueComponente() != null
                                                && dato.getExpedienteDTO() != null
                                                && dato.getExpedienteDTO().getIdExpediente() != null
                                                && dato.getExpedienteDTO().getIdExpediente().equals(expediente.getIdExpediente()))
                                         .sorted(Comparator.comparing(DatoDTO::getNmFila))
                                         .collect(Collectors.toList());
                                
                                blCompDTO.setDatos(lstDatoOrdenadaByNmFila);
                        }
                        
                        // JRFM - MD-1877 - 19/08/2025 Si el bloque es de tipo de formulario y check,
                        // filtramos los datos del check por expediente (igual que el resto de subbloques,
                        // ver el filtrado identico para MultiTexto justo debajo).
                        // VMM: ya NO reescribimos dsValorMultiple a los textos de lstCheckItems - ese
                        // parcheo (solo se aplicaba cuando dsValorMultiple[0]=="true", y ademas dejaba
                        // sin filtrar por expediente los casos en que no empezaba por "true") chocaba con
                        // el renderizado de bloque-renderer.xhtml, que interpreta dsValorMultiple como la
                        // lista true/false por POSICION (alineada con lstCheckItems), igual que en los
                        // bloques padre de tipo Formulario/Listado/Tabla.
                        if (Boolean.TRUE.equals(bloqueHijo.getEsTipoFormulario())
                                && blCompDTO.getComponenteDTO() != null
                                && blCompDTO.getComponenteDTO().getTipoDTO() != null
                                && blCompDTO.getComponenteDTO().getTipoDTO().getIdValor() != null
                                && blCompDTO.getComponenteDTO().getTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK))
                                && blCompDTO.getDatos() != null
                                && !blCompDTO.getDatos().isEmpty()) {

                            List<DatoDTO> lstDatoDTO = blCompDTO.getDatos();
                            List<DatoDTO> lstDatoOrdenadaByNmFila = lstDatoDTO.stream()
                                    .filter(dato -> dato.getIdBloqueComponente() != null
                                            && dato.getExpedienteDTO() != null
                                            && dato.getExpedienteDTO().getIdExpediente() != null
                                            && dato.getExpedienteDTO().getIdExpediente().equals(expediente.getIdExpediente())
                                            && dato.getIdBloqueComponente().equals(blCompDTO.getIdBloqueComponente()))
                                     .collect(Collectors.toList());

                            blCompDTO.setDatos(lstDatoOrdenadaByNmFila);
                        }
                        
                        //VMM 10/02/26 MD-2288: si es un subbloque de tipo formulario campo multitexto para observaciones hay que filtrar 
                        //solo los datos del expediente en concreto porque está trayéndose de más expedientes con ese mismo idBloqueComponente
                        if (Boolean.TRUE.equals(bloqueHijo.getEsTipoFormulario())
                                && blCompDTO.getComponenteDTO() != null 
                                && blCompDTO.getComponenteDTO().getTipoDTO() != null
                                && blCompDTO.getComponenteDTO().getTipoDTO().getIdValor() != null 
                                && blCompDTO.getComponenteDTO().getTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO))
                                && blCompDTO.getComponenteDTO().getSubTipoDTO() != null
                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor() != null 
                                && blCompDTO.getComponenteDTO().getSubTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO))
                                && blCompDTO.getDatos() != null 
                                && !blCompDTO.getDatos().isEmpty()) {
                            
                            List<DatoDTO> lstDatoDTO = blCompDTO.getDatos();
                            List<DatoDTO> lstDatoByExp = lstDatoDTO.stream()
                                    .filter(dato -> dato.getIdBloqueComponente() != null
                                            && dato.getExpedienteDTO() != null
                                            && dato.getExpedienteDTO().getIdExpediente() != null
                                            && dato.getExpedienteDTO().getIdExpediente().equals(expediente.getIdExpediente())
                                            && dato.getIdBloqueComponente().equals(blCompDTO.getIdBloqueComponente()))
                                     .collect(Collectors.toList());

                            blCompDTO.setDatos(lstDatoByExp);
                        }

                    }
                }
                
                if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO)
                        .equals(bloqueHijo.getIdFormato().getIdValor())) {

                    bloqueHijo.setLstBloqueComponenteDTO(
                            ordenadorBloqueComponentePorFinanciacionDato(
                                    bloqueHijo.getLstBloqueComponenteDTO(), bloqueHijo,
                                    expediente));
                }
            }
        }
    }
    
    private Boolean validarPestanaContrato() {
        Boolean resultado = Boolean.FALSE;
        if (this.convocatoriaDTO.getIdModalidadConvocatoria().getIdValor() != null) {
            TipoResolModalidad tipoResolModalidad = this.tipoResolModalidadDAO.findByIdModalidad(
                    this.convocatoriaDTO.getIdModalidadConvocatoria().getIdValor());

            if (tipoResolModalidad.getCdTipoResolucion()
                    .equals(Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA)
                    && this.convocatoriaDTO.getIdEstado().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA))) {
                resultado = Boolean.TRUE;
                return resultado;
            }

            if (tipoResolModalidad.getCdTipoResolucion()
                    .equals(Constantes.TipoResolucionModalidad.RESOLUCION_INDIVIDUAL)
                    && this.convocatoriaDTO.getIdEstado().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES))
                    && this.expedienteDTO.getEstadoDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO))) {
                resultado = Boolean.TRUE;
                return resultado;
            }

        }
        return resultado;

    }

    private void comprobarGestoresExcep(ConvocatoriaDTO conv) {
        //si tiene gestores excepcionales 
       if (conv.getListConvocatoriaGest() != null && !conv.getListConvocatoriaGest().isEmpty()) {
           
           for (ConvocatoriaGestorDTO convGest: conv.getListConvocatoriaGest()) {
               //vemos si alguno es el usuario logado y es ese caso le deshabilitamos las pestañas correspondientes
               if (convGest.getCdUsuario().equals(this.infoUsuarioBean.getLogin())) {
                   
                   if (convGest.getTipoTramite() != null && convGest.getTipoTramite() == 1 ) {  //gestor para concesion
                       conv.setEsGestorConcesion(true);
                   }else if (convGest.getTipoTramite() != null && convGest.getTipoTramite() == 2 ) {  //gestor para JCT                       
                       conv.setEsGestorJCT(true);
                   }
               }
           }
           //actualizamoslos valores de la convocatoria para activar o no las pestañas
           this.setConvocatoriaDTO(conv);
       }
        
    }

    private void validarEstilo() throws ServiceException {
        this.expedienteDTO.setBAvisoContratos(Boolean.FALSE);
        if(Boolean.FALSE.equals(this.expedienteDTO.getBAvisoContratos()) && this.getLstExpedienteParticipante() != null
                && !this.getLstExpedienteConContrato().isEmpty()) {
            
            Integer idPenRevCon = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO);
            Integer idConPre = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_PRESENTADO);
            Integer idPendRevSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION);
            Integer idSubPre = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SUBSANACION_PRESENTADA);
            Integer idRenSinSusPre = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA);
            
            Boolean algunRequiereAtencion = this.getLstExpedienteConContrato().stream()
                    .anyMatch(x -> { 
                        CatalogosValoresDTO estado = x.getExpedienteContrato().getIdEstadoDTO();
                        return estado != null && estado.getIdValor() != null &&
                            (estado.getIdValor().equals(idPenRevCon)
                            || estado.getIdValor().equals(idConPre)
                            || estado.getIdValor().equals(idPendRevSub)
                            || estado.getIdValor().equals(idSubPre)
                            || estado.getIdValor().equals(idRenSinSusPre)
                                    );
                    });
            if(Boolean.FALSE.equals(algunRequiereAtencion)) {
                algunRequiereAtencion = this.getLstExpedienteConContrato().stream()
                        .anyMatch(x -> { 
                            CatalogosValoresDTO estado = x.getExpedienteContrato().getIdEstadoRevAdminDTO();
                            return estado != null && estado.getIdValor() != null &&
                                (estado.getIdValor().equals(idPenRevCon)
                                || estado.getIdValor().equals(idPendRevSub)
                                || estado.getIdValor().equals(idRenSinSusPre)
                                        );
                        });
            }
            if(Boolean.FALSE.equals(algunRequiereAtencion)) {
                //Si los expedienteContrato no requieren atención, comprovamos sus documentos
                for(ExpedienteDTO expCont : this.getLstExpedienteConContrato()) {
                    
                    List<AtlasFilter> filtros = new ArrayList<>();
                    List<AtlasSort> orden = new ArrayList<>();

                    List<Integer> lstIdTramites = new ArrayList<>();

                    lstIdTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
                    lstIdTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL));
                    
                    List<Integer> lstIdEstadosDoc = new ArrayList<>();

                    lstIdEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                    lstIdEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                    
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_CONTRATO_IDEXPEDIENTE,
                            expCont.getIdExpediente(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_TRAMITE,
                            lstIdTramites, Operador.IN));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_ESTADO,
                            lstIdEstadosDoc, Operador.IN));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

                    orden.add(new AtlasSort(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC, true));
                    
                    try {
                        List<ExpedienteContratoDocDTO> lstExpCont = this.expedienteContratoDocService.buscar(0,
                                100, filtros, orden);

                        if(lstExpCont != null && !lstExpCont.isEmpty()) {
                            algunRequiereAtencion = Boolean.TRUE;
                            break;
                        }
                        
                    } catch (ServiceException e) {
                        log.error(e.getMessage());
                    }
                    
                }
            }
            if(Boolean.TRUE.equals(algunRequiereAtencion) 
                    && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es un auditor) {
                this.expedienteDTO.setBAvisoContratos(Boolean.TRUE);
            }
        }
        
        if(Boolean.FALSE.equals(this.getLstExpedienteConContrato().isEmpty())) {
            this.setActivarPestanaRevContrato(Boolean.FALSE);
            try {
                if(this.convocatoriaDTO == null) {
                    
                	this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
                    
                }
                
                this.setActivarPestanaRevContrato(this.validarPestanaContrato());
                
            }catch (ServiceException e) {
                log.error(ConstantesWeb.Mensajes.CONVOCATORIAS_MENSAJE_CARGAR_KO, e);
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.CONVOCATORIAS_MENSAJE_CARGAR_KO);
            }
        }
        
    }

    /**
     * Metodo para obtener los datos del presupuesto presentado
     */
    private void obtenerPresupuestoPresentado() {
        try {

	    //MD-2874: adaptamos este metodo para procesar tanto bloques de PP padre como hijos
            Integer idPrePre = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO);

            // Procesar bloques padre de tipo PP
            this.expedienteDTO.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(e -> idPrePre.equals(e.getIdFormato().getIdValor()) && e.getIdBloquePadre() == null)
                    .forEach(this::procesarBloquePresupuestoPresentado);

            // Procesar bloques hijo de tipo PP (están en lstBloquesHijoDTO de cada bloque padre)
            this.expedienteDTO.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(e -> e.getLstBloquesHijoDTO() != null && !e.getLstBloquesHijoDTO().isEmpty())
                    .flatMap(e -> e.getLstBloquesHijoDTO().stream())
                    .filter(hijo -> idPrePre.equals(hijo.getIdFormato().getIdValor()))
                    .forEach(this::procesarBloquePresupuestoPresentado);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }


   private void procesarBloquePresupuestoPresentado(BloqueDTO b) {
	
	//MD-2874: creamos este metodo para procesar tanto bloques de PP padre como hijos
        // ordenamos los registros de la lista de presupuestos
        List<BloqueComponenteDTO> lstBloqueComponenteDTO = b
                .getLstBloqueComponenteDTO().stream()
                .filter(x -> x.getFcBajaFila() == null)
                .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                .collect(Collectors.toList());

        // cargamos la lista de filas
        b.setListaBloqueAuxDTO(b.getLstBloqueComponenteDTO().stream()
                .filter(f -> f.getItFila()).collect(Collectors.toList()));

        if (!b.getListaBloqueAuxDTO().isEmpty()) {

            // Construimos la lista de columnas Presupuesto
            List<BloqueComponenteDTO> lstBloqueComponenteColumna = lstBloqueComponenteDTO
                    .stream()
                    .filter(p -> Objects.isNull(p.getFcBajaFila())
                            && Boolean.TRUE.equals(p.getItColumna()))
                    .collect(Collectors.toList());

            if (!lstBloqueComponenteColumna.isEmpty() && (this.esConvocatoriaConceptoPresup == null
                    || !this.esConvocatoriaConceptoPresup)) {
                try {
                    this.obtenerFilColPresupPresentado(b);
                } catch (ServiceException e1) {
                    log.error(e1.getMessage());
                }
            } else if (!lstBloqueComponenteColumna.isEmpty() && Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                try {
                    this.obtenerFilColPresupPresentadoConcepto(b);
                } catch (ServiceException e2) {
                    log.error(e2.getMessage());
                }
            }
        }
    }


    /**
     * Convierte a Double el valor tecleado por el ciudadano en un dato de bloque (importe/presupuesto),
     * admitiendo tanto coma decimal/punto de miles (formato español) como punto decimal (ej. "235861,59"
     * o "235.861,59" o "235861.59"). Si el valor no se puede interpretar como numero, devuelve 0 en vez
     * de lanzar NumberFormatException, para no romper el calculo de totales por fila/columna.
     *
     * @param valor cadena introducida por el ciudadano
     * @return el valor numerico, o 0 si no es un numero valido
     */
    private double parsearImporteBloque(String valor) {
        String valorNormalizado = Utils.normalizarSeparadorDecimal(valor);
        return Boolean.TRUE.equals(Utils.validarDouble(valorNormalizado))
                ? Double.parseDouble(valorNormalizado)
                : 0d;
    }

    /**
     * VMM 02/09/26 MD-3011: si por un guardado duplicado hay más de un dato vigente (sin baja) para la
     * misma fila/columna, nos quedamos solo con el más reciente (mayor fcAltaFila, desempatando
     * por idDato), para no sumar/mezclar importes duplicados en los totales de presupuesto.
     */
    private List<DatoDTO> deduplicarDatosMasRecientePorFilaColumna(List<DatoDTO> datos) {
        return new ArrayList<>(datos.stream()
                .collect(Collectors.toMap(
                        d -> d.getNmFila() + "-" + d.getNmColumna(),
                        d -> d,
                        (d1, d2) -> {
                            if (d1.getFcAltaFila() == null) {
                                return d2;
                            }
                            if (d2.getFcAltaFila() == null) {
                                return d1;
                            }
                            int cmp = d1.getFcAltaFila().compareTo(d2.getFcAltaFila());
                            if (cmp != 0) {
                                return cmp > 0 ? d1 : d2;
                            }
                            return (d1.getIdDato() != null && d2.getIdDato() != null
                                    && d1.getIdDato() > d2.getIdDato()) ? d1 : d2;
                        }))
                .values());
    }

    /**
     *
     * @param bloque
     * @throws ServiceException
     */
    private void obtenerFilColPresupPresentado(BloqueDTO bloque) throws ServiceException {

        // Construimos lista con idColumna
        int[] nColArray = IntStream.range(0, this.listaAnualidades.size() + 1).toArray();
        this.listaNcolPresupuesto = new ArrayList<>();

        for (int arr : nColArray) {
            this.listaNcolPresupuesto.add(arr);
        }

	//MD-2874: adaptamos este fragmento
        // Recuperar datos de los bloques componente fila
        Integer idExp = this.expedienteDTO != null ? this.expedienteDTO.getIdExpediente() : this.idExpediente;
        for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
            List<DatoDTO> datosBloque = this.datoService.buscarDatosByIdExpAndIdBloComp(idExp, fila.getIdBloqueComponente(), true);

            List<DatoDTO> datosDedup = this.deduplicarDatosMasRecientePorFilaColumna(datosBloque);
            datosDedup.sort(Comparator.comparing(DatoDTO::getNmColumna));

            fila.setDatos(datosDedup);
            // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
            Utils.normalizarSeparadorDecimalDatos(fila.getDatos());
        }

        // Calculamos los montos totales por fila
        for (int i = 0; i < bloque.getListaBloqueAuxDTO().size(); i++) {
            double sumaFila = bloque.getListaBloqueAuxDTO().get(i).getDatos().stream()
                    .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                            && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                    ? this.parsearImporteBloque(dato.getDsValor())
                                    : 0)
                    .sum();

            DatoDTO dto = new DatoDTO();
            dto.setDsValor(String.valueOf(sumaFila));
            dto.setEsMontoTotal(Boolean.TRUE);
            bloque.getListaBloqueAuxDTO().get(i).getDatos().add(dto);

        }

        // Preparamos espacio para totales por fila
        BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
        bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("TOTAL");
        bloqueComponenteTotalFila.setDatos(new ArrayList<>());

        // Calculamos los montos totales por columna
        for (int i = 0; i < this.listaNcolPresupuesto.size(); i++) {
            double sumaColumna = 0;

            for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
                if (!fila.getDatos().isEmpty() && fila.getDatos().size() > i
                        && StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())) {
                    sumaColumna += this.parsearImporteBloque(fila.getDatos().get(i).getDsValor());
                }
            }

            DatoDTO dto = new DatoDTO();
            dto.setDsValor(String.valueOf(sumaColumna));
            dto.setEsMontoTotal(Boolean.TRUE);
            bloqueComponenteTotalFila.getDatos().add(dto);
        }

        bloque.getListaBloqueAuxDTO().add(bloqueComponenteTotalFila);

    }

    /**
     * Metodo para obtener los datos de las actuaciones presentadas
     *
     * @param impresoDTO
     */
    private void obtenerActuacionPresentado() {

        try {

            // Obtener registros de la actuación
            Integer IdFinAct = utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES);
            List<BloqueDTO> lstBloqueActuaDTO = this.expedienteDTO.getImpresoDTO().getLstBloqueDTO()
                    .stream().filter(e -> IdFinAct.equals(e.getIdFormato().getIdValor()))
                    .collect(Collectors.toList());

            if (!lstBloqueActuaDTO.isEmpty()) {

                List<BloqueComponenteDTO> lstBloqueComponenteActuaDTO = lstBloqueActuaDTO.get(0)
                        .getLstBloqueComponenteDTO();

                List<BloqueComponenteDTO> lstBloqueComponenteFilaActua = new ArrayList<>();

                if (!lstBloqueComponenteActuaDTO.isEmpty()) {
                    
                    Integer idComFinOfe1Prov = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_PROVEEDOR);
                    
                    Integer idBloqueComp = lstBloqueComponenteActuaDTO.stream()
                            .filter(a -> Objects.isNull(a.getFcBajaFila())
                                    && Boolean.TRUE.equals(a.getItColumna())
                                    && a.getComponenteDTO().getIdComponente().equals(idComFinOfe1Prov))
                            .findFirst().orElse(new BloqueComponenteDTO()).getIdBloqueComponente();

                    List<AtlasFilter> filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                            this.idExpediente, Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                            idBloqueComp, Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    this.datoService.buscar(0, -1, filtros, null).forEach(d -> {
                        Integer nmFila = d.getNmFila() + 1;
                        lstBloqueComponenteActuaDTO.stream()
                                .filter(a -> Objects.isNull(a.getFcBajaFila())
                                        && Boolean.TRUE.equals(a.getItFila())
                                        && nmFila.equals(a.getNmOrden()))
                                .forEach(x -> x.setItSeleccionado(Boolean.TRUE));
                    });

                    // Construimos la lista de filas actuación
                    lstBloqueComponenteFilaActua = lstBloqueComponenteActuaDTO.stream()
                            .filter(p -> Objects.isNull(p.getFcBajaFila())
                                    && Boolean.TRUE.equals(p.getItFila())
                                    && Boolean.TRUE.equals(p.getItSeleccionado()))
                            .collect(Collectors.toList());

                    if (!lstBloqueComponenteFilaActua.isEmpty()) {

                        Comparator<BloqueComponenteDTO> compareByIdTipoFinanciacion = (
                                BloqueComponenteDTO c1, BloqueComponenteDTO c2) -> c1
                                        .getTipoFinanciacionDTO().getIdValor()
                                        .compareTo(c2.getTipoFinanciacionDTO().getIdValor());

                        Collections.sort(lstBloqueComponenteFilaActua, compareByIdTipoFinanciacion);
                        
                        Integer idComFinOfeSelProv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR);
                        Integer idComFinOfeSolImp = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE);
                        
                        // Construimos la lista de columnas de la actuación
                        List<BloqueComponenteDTO> lstBloqueComponenteColumnaActua = lstBloqueComponenteActuaDTO
                                .stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila())
                                        && Boolean.TRUE.equals(p.getItColumna())
                                        && Objects.nonNull(p.getComponenteDTO().getIdComponente())
                                        && (idComFinOfeSelProv.equals(p.getComponenteDTO().getIdComponente())
                                                || idComFinOfeSolImp.equals(p.getComponenteDTO().getIdComponente())))
                                .collect(Collectors.toList());

                        Comparator<BloqueComponenteDTO> compareByComponenteId = (
                                BloqueComponenteDTO c1, BloqueComponenteDTO c2) -> c1
                                        .getComponenteDTO().getIdComponente()
                                        .compareTo(c2.getComponenteDTO().getIdComponente());

                        Collections.sort(lstBloqueComponenteColumnaActua, compareByComponenteId);

                        this.obtenerFilColActPresentada(lstBloqueComponenteFilaActua,
                                lstBloqueComponenteColumnaActua);

                    }

                }

                for (BloqueDTO b : this.expedienteDTO.getImpresoDTO().getLstBloqueDTO()) {
                    if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES)
                            .equals(b.getIdFormato().getIdValor())) {
                        b.setListaBloqueAuxDTO(lstBloqueComponenteFilaActua);
                    }
                }
                
                Integer idComFinTotImpAyu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA);
                
                // Recuperamos solo el registro de importe de ayuda solicitada
                Optional<BloqueComponenteDTO> campoAyudaSolicitada = lstBloqueComponenteActuaDTO
                        .stream().filter(p -> idComFinTotImpAyu.equals(p.getComponenteDTO().getIdComponente()))
                        .findFirst();

                if (campoAyudaSolicitada.isPresent() &&  (!campoAyudaSolicitada.get().getDatos().isEmpty())
                        &&  (!campoAyudaSolicitada.get().getDatos().get(0).getDsValor().isEmpty())) {
                        this.montoAyudaActuacion = this.parsearImporteBloque(
                                campoAyudaSolicitada.get().getDatos().get(0).getDsValor());
                }

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }

    }

    /**
     * Método para armar las filas y columnas de la actuación presentada
     */
    private void obtenerFilColActPresentada(List<BloqueComponenteDTO> lstBloqueComponenteFilaActua,
            List<BloqueComponenteDTO> lstBloqueComponenteColumnaActua) throws ServiceException {
        // construimos arreglo con los id bloque componente a recuperar
        List<Integer> lstIdBloqueComponente = new ArrayList<>();

        lstBloqueComponenteColumnaActua.stream()
                .forEach(e -> lstIdBloqueComponente.add(e.getIdBloqueComponente()));

        // Recuperar datos de los bloques componente fila
        for (BloqueComponenteDTO fila : lstBloqueComponenteFilaActua) {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, this.idExpediente,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                    lstIdBloqueComponente, Operador.IN));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NM_FILA, fila.getNmOrden() - 1,
                    Operador.EQUALS));
            List<DatoDTO> datos = this.datoService.buscar(0, -1, filtros, null);
            Comparator<DatoDTO> compareByDatos = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                    .compareTo(c2.getNmColumna());
            Collections.sort(datos, compareByDatos);
            // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
            Utils.normalizarSeparadorDecimalDatos(datos);
            fila.setDatos(datos);
        }

        // Construimos lista con idColumna
        int[] nColArray = IntStream.range(0, lstBloqueComponenteColumnaActua.size()).toArray();
        this.listaNcolActuacion = new ArrayList<>();

        for (int arr : nColArray) {
            this.getListaNcolActuacion().add(arr);
        }

        // Preparamos espacio para totales por fila
        BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
        bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setCdCodigo("");
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("");
        bloqueComponenteTotalFila.setDatos(new ArrayList<>());

        // Calculamos los montos totales por columna
        double sumaColumna = 0;
        // referenciamos la posición de la última columna
        int i = this.getListaNcolActuacion().size() - 1;

        for (BloqueComponenteDTO fila : lstBloqueComponenteFilaActua) {
            if (StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())) {
                sumaColumna += this.parsearImporteBloque(fila.getDatos().get(i).getDsValor());
            }
        }

        DatoDTO dto = new DatoDTO();

        dto.setDsValor("TOTAL");

        bloqueComponenteTotalFila.getDatos().add(dto);

        dto = new DatoDTO();
        dto.setDsValor(String.valueOf(sumaColumna));
        dto.setEsMontoTotal(Boolean.TRUE);

        bloqueComponenteTotalFila.getDatos().add(dto);

        // this.lstActuacionPresentado = this.lstBloqueComponenteFilaActua;
        lstBloqueComponenteFilaActua.add(bloqueComponenteTotalFila);

    }

    /**
     * Método para controlar número de solapa revisión activa
     *
     * @param numeroSolapa Número de solapa
     */
    public void irASolapaRevision(int numeroSolapa) {
        this.pestanaRevisionActiva = numeroSolapa;
    }

    /**
     * Metodo que controla la descarga de PDF del servicio EREG
     *
     */
    public void descargarExpediente(Integer idExpediente) {

        try {
            if (idExpediente != null) {
                DocumentoDTO documentoDTO = this.pdfService.descargarZipExpediente(idExpediente);

                if (documentoDTO != null) {
                    log.info("Descargar expediente " + documentoDTO.getNombreFichero());

                    // Se prepara el fichero.
                    UtilesWeb.openFile(documentoDTO.getContenidoFichero(), "zip",
                            documentoDTO.getNombreFichero().replace(".zip", ""));
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
                    log.error("No se ha podido encontrar los documentos a descargar");
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }
    
    /**
     * Metodo que controla la descarga de PDF del servicio EREG
     *
     * @param idExpediente
     */
    public void descargarContratoInicial(Integer idExpediente) {

        try {
            if (idExpediente != null) {
                DocumentoDTO documentoDTO = this.pdfService.descargarZipDocsExpedienteContrato(idExpediente, 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));

                if (documentoDTO != null) {
                    log.info("Descargar expediente " + documentoDTO.getNombreFichero());

                    // Se prepara el fichero.
                    UtilesWeb.openFile(documentoDTO.getContenidoFichero(), "zip",
                            documentoDTO.getNombreFichero().replace(".zip", ""));
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
                    log.error("No se ha podido encontrar los documentos a descargar");
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }
    
    /**
     * Metodo que controla la descarga de PDF del servicio EREG
     *
     * @param idExpediente
     */
    public void descargarContratoSustitucion(Integer idExpediente) {

        try {
            if (idExpediente != null) {
                DocumentoDTO documentoDTO = this.pdfService.descargarZipDocsExpedienteContrato(idExpediente, 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION));

                if (documentoDTO != null) {
                    log.info("Descargar expediente " + documentoDTO.getNombreFichero());

                    // Se prepara el fichero.
                    UtilesWeb.openFile(documentoDTO.getContenidoFichero(), "zip",
                            documentoDTO.getNombreFichero().replace(".zip", ""));
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
                    log.error("No se ha podido encontrar los documentos a descargar");
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }

    /**
     * Método para obtener lista de Expediente del Participante
	 * y controlar activación de botones según estado de expedientes
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return
     */
    public AtlasListAndCount<ExpedienteDTO> obtenerTotalExpedienteParticipante(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteDTO> lista = new AtlasListAndCount<>();

        this.obtenerExpedienteParticipante(this.expedienteDTO);
        lista.setResultados(this.lstExpedienteParticipante);
        lista.setTotal(this.lstExpedienteParticipante.size());
        this.documentoBean.setActivarBotonRequerimientoSubsanacion(Boolean.FALSE);
        if(!lista.getResultados().isEmpty()) {
            Integer idPenSubReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS);
            Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK);
            Boolean algunRequiereSub = lista.getResultados().stream()
                    .anyMatch(x -> { 
                        CatalogosValoresDTO estado = x.getEstadoRevContratoDTO();
                        return estado != null && estado.getIdValor() != null &&
                            estado.getIdValor().equals(idPenSubReq);
                    });
            //Si alguno requiere subsanacion comprobamos que todos sean OK o están pendientes de subsanar
            if(Boolean.TRUE.equals(algunRequiereSub)) {
                this.documentoBean.setActivarBotonRequerimientoSubsanacion( lista.getResultados().stream()
                        .allMatch(x -> { 
                            CatalogosValoresDTO estado = x.getEstadoRevContratoDTO();
                            return estado != null && estado.getIdValor() != null &&
                                (estado.getIdValor().equals(idPenSubReq) ||
                                estado.getIdValor().equals(idRevOk));
                        })
                    );
            }
        }
        
        Integer idPenReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO);
        Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK);
        this.documentoBean.setActivarBotonFinalizacionExpediente( lista.getResultados().stream()
                .allMatch(x -> { 
                    CatalogosValoresDTO estado = x.getEstadoRevContratoDTO();
                    return estado != null && estado.getIdValor() != null &&
                        (estado.getIdValor().equals(idPenReq) ||
                        estado.getIdValor().equals(idRevOk));
                })
            );

        return lista;
    }

    /**
     * Obtiene el número total de Bloque Listado,
     *
     * @param size int
     * @return Object[].
     */
    public Object[] createArray(int size) {
        return new Object[size];
    }

    /**
     *
     * @param idBloqueComponente
     * @return
     */
    public String obtenerDatoRelacionable(Integer idBloqueComponente) {
        String valor = "";
        if (this.expedienteDTO != null) {
            valor = this.expedienteDTO.getValoresRelacionables().get(idBloqueComponente);
        }
        return valor;
    }

    /**
     * Método para obtener el historico de expedientes
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<ExpedienteHistDTO> Lista de Historico
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteHistDTO> obtenerHistorico(int index, int pageSize,
            Object ordenacion, Object filter) {

        AtlasListAndCount<ExpedienteHistDTO> lista = new AtlasListAndCount<>();

        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteDTO.getIdExpediente(), Operador.EQUALS));

            if (orden == null) {
                orden = new ArrayList<>();
            }
            orden.add(new AtlasSort("fcAltaFila", false));

            // obtener los registros
            lista.setResultados(this.expedienteHistService.buscar(index, pageSize, filtros, orden));

            // obtener el total de registros (numero)
            lista.setTotal(this.expedienteHistService.count(filtros));

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }

        return lista;
    }

    /**
     * Método para obtener el historico de expedientes
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<ExpedienteHistDTO> Lista de Historico
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<TramiteSolicitanteExpedienteDTO> obtenerHistoricoTramite(int index, int pageSize,
            Object ordenacion, Object filter) {

        AtlasListAndCount<TramiteSolicitanteExpedienteDTO> lista = new AtlasListAndCount<>();

        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    this.expedienteDTO != null ? this.expedienteDTO.getIdExpediente() : this.idExpediente, Operador.EQUALS));

            if (orden == null) {
                orden = new ArrayList<>();
            }
            orden.add(new AtlasSort("fcAltaFila", false));

            // obtener los registros
            lista.setResultados(this.tramiteSolicitanteExpedienteService.buscar(index, pageSize, filtros, orden));

            // obtener el total de registros (numero)
            lista.setTotal(this.tramiteSolicitanteExpedienteService.count(filtros));

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }

        return lista;
    }
    
    
    /**
     * Método para obtener el historico de expedientes
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<ExpedienteHistDTO> Lista de Historico
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<DocumentoPtfrDTO> obtenerHistoricoNotificacion(int index, int pageSize,
            Object ordenacion, Object filter) {

        AtlasListAndCount<DocumentoPtfrDTO> lista = new AtlasListAndCount<>();

        try {
            List<DocumentoPtfrDTO> listado = this.documentoPtfrService
                    .obtenerHistoricoNotif(this.expedienteDTO.getIdExpediente());
            List<DocumentoPtfrDTO> listaPaginada = this.documentoPtfrService
                    .obtenerPaginadoHistoricoNotif(listado, index, pageSize);

            // obtener los registros
            lista.setResultados(listaPaginada);

            // obtener el total de registros (numero)
            lista.setTotal(listado.size());

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }

        return lista;
    }

    public String metodoEnvio(DocumentoPtfrDTO docPtfrDTO) {
        String envio = "";
        try {
            if (Objects.nonNull(docPtfrDTO)) {
                if (Objects.nonNull(docPtfrDTO.getItCorreo())
                        && docPtfrDTO.getItCorreo().equals(Constantes.AfirmacionNegacion.S)) {
                    envio = Constantes.TipoEnvio.DS_CORREO;
                }
                if (Objects.nonNull(docPtfrDTO.getCdNote())) {
                    envio = Constantes.TipoEnvio.DS_NOTE;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return envio;
    }

    public Date obtenerFechaEnvio(DocumentoPtfrDTO docPtfrDTO) {
        Date fcEnvio = null;
        try {
            if (Objects.nonNull(docPtfrDTO)) {
                if (Objects.nonNull(docPtfrDTO.getItCorreo())
                        && docPtfrDTO.getItCorreo().equals(Constantes.AfirmacionNegacion.S)) {
                    fcEnvio = docPtfrDTO.getFcEnvioCorreo();
                }
                if (Objects.nonNull(docPtfrDTO.getCdNote())) {
                    fcEnvio = docPtfrDTO.getFcEnvioNote();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return fcEnvio;
    }

    public String obtenerEstadoNote(DocumentoPtfrDTO docPtfrDTO) {
        String estado = "";
        try {
            if (Objects.nonNull(docPtfrDTO)) {

                if (docPtfrDTO.getCdEstadoNote().equals(Constantes.Note.CD_PD)) {
                    estado = Constantes.Note.DS_PUESTA_DISPOSICION;
                }

                if (docPtfrDTO.getCdEstadoNote().equals(Constantes.Note.CD_L)) {
                    estado = Constantes.Note.DS_LEIDAS;
                }

                if (docPtfrDTO.getCdEstadoNote().equals(Constantes.Note.CD_R)) {
                    estado = Constantes.Note.DS_RECHAZADAS;
                }

                if (docPtfrDTO.getCdEstadoNote().equals(Constantes.Note.CD_RA)) {
                    estado = Constantes.Note.DS_RECHAZADAS_AUTO;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return estado;
    }

    /**
     *
     * @param idTramite
     */
    public void descargarPeticion(DocumentoPtfrDTO item) {

        try {

            // recuperamos los datos de la plantilla por idTramite y
            // idConvocatoria
            ConvocatoriaPlaDTO convocatoriaPlaDTO = this.convocatoriaComService
                    .obtenerConvocatoriaPla(this.idConvocatoria, item.getTramiteDTO().getIdValor());

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(item.getTramiteDTO().getIdValor())) {

                // recuperamos los datos de la convocatoria por idConvocatoria
                ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                        .buscarId(this.idConvocatoria);

                Tramite tramite = tramiteService
                        .getTramite(convocatoriaPlaDTO.getTramiteDTO().getIdValor());

                tramite.setPdfService(this.pdfService);
                tramite.setCatalogosValoresService(this.catalogosValoresService);
                tramite.setExpedienteService(this.expedienteService);

                // Obtenemos los datos de la consejeria
                String strConsejeria = this.unidadAdminService.obtenerConsejeria();

                byte[] bytesSalida = tramite.formularioPdf(convocatoriaDTO, convocatoriaPlaDTO,
                        this.expedienteDTO, strConsejeria);

                UtilesWeb.openFile(bytesSalida, "pdf",
                        convocatoriaPlaDTO.getTramiteDTO().getDsValor());

            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                    .equals(item.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                            .equals(item.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(item.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(item.getTramiteDTO().getIdValor())) {

                String codigoArchivo = String.valueOf(item.getCdArchivoPtfr());
                String codigoReferencia = item.getDsReferenciaPtfr();

                PtfrRecuperaDocFirmadoDTO dto = this.ptfrAdmeService
                        .recuperarDocumentoFirmado(codigoReferencia, codigoArchivo);

                UtilesWeb.openFile(Base64.getDecoder().decode(dto.getPdfB64()),
                        Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION,
                        convocatoriaPlaDTO.getTramiteDTO().getDsValor());
            }

        } catch (Exception e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }

    /**
     *
     */
    public void obtenerExpedientes() {
        try {
            if (this.idSolicitud != null) {
                
                List<ExpedienteDTO> lstExpedienteEstruc = new ArrayList<>();

                this.lstExpedientes = this.expedienteService
                        .obtenerExpedienteEstructurado(this.idSolicitud, Constantes.AfirmacionNegacion.N);
                
                Optional<Date> fcFirmaSolicitante = lstExpedientes.stream()
                        .filter(expediente -> Boolean.TRUE.equals(expediente.getEsExpedienteSolicitante()))
                        .filter(expediente -> expediente.getFcFirma() != null)
                        .map(ExpedienteDTO::getFcFirma)  // Extrae el campo fcFirma
                        .findFirst();                    // Obtiene el primero (si existe)

                // Usar la fecha si está presente
                Date fechaPresentacion = null;
                if (fcFirmaSolicitante.isPresent()) {
                    fechaPresentacion = fcFirmaSolicitante.get();
                }
                
                for(ExpedienteDTO expedienteDTO : this.lstExpedientes) {
                    expedienteDTO.setSolicitudDTO(this.getSolicitudBean().getSolicitudDTO());
                    expedienteDTO.setFcPresentacion(fechaPresentacion);
                    if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteCoordinador())) {
                
                        lstExpedienteEstruc = this.expedienteService
                        .buscarExpedientePorExpedientePadre(this.idSolicitud, expedienteDTO.getIdExpediente());
                
                        for(ExpedienteDTO expDTO : lstExpedienteEstruc) {
                            expDTO.setSolicitudDTO(this.getSolicitudBean().getSolicitudDTO());
                            expDTO.setFcPresentacion(fechaPresentacion);
                            if (Boolean.TRUE.equals(expDTO.getEsExpedienteContrato())) {
                                expedienteDTO.setLstExpedienteParticipante(lstExpedienteEstruc);
                            }
                    
                        }
                    }
                }
            }
         
            this.setConvocatoriaDTO(this.convocatoriaService.buscarId(this.idConvocatoria));
            this.cadenaNotaMaxima = "Nota del expediente ( 0 - " + this.convocatoriaDTO.getNmCalificacionMax() + " )" ;   
            
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

    }

    /**
     * MD-2179 y MD-2205: listAndCountMethod de la solapa de expedientes (atlasp:listaPaginada).
     * Recarga los expedientes de la solicitud y aplica el filtro de estado
     * seleccionado (idEstadoFiltro) antes de paginar en memoria.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return página de ExpedienteDTO filtrada por estado, y total de la lista filtrada
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDTO> obtenerListaExpedientesSolapa(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDTO> listado = new AtlasListAndCount<>();

        this.obtenerExpedientes();

        List<ExpedienteDTO> filtrados = this.idEstadoFiltro == null ? this.lstExpedientes
                : this.lstExpedientes.stream()
                        .filter(e -> e.getEstadoDTO() != null
                                && this.idEstadoFiltro.equals(e.getEstadoDTO().getIdValor()))
                        .collect(Collectors.toList());

        if (orderBean instanceof List && !((List<?>) orderBean).isEmpty()) {
            AtlasSort orden = ((List<AtlasSort>) orderBean).get(0);
            Comparator<ExpedienteDTO> comparador = this.obtenerComparadorExpediente(orden.getProperty());
            if (comparador != null) {
                Comparator<ExpedienteDTO> ordenado = orden.isAscendente() ? comparador : comparador.reversed();
                if ("nmNota".equals(orden.getProperty())) {
                    ordenado = Comparator.comparing((ExpedienteDTO e) -> e.getNmNota() == null)
                            .thenComparing(ordenado);
                }
                filtrados.sort(ordenado);
            }
        }

        listado.setTotal(filtrados.size());

        int desde = Math.min(index, filtrados.size());
        int hasta = pageSize > 0 ? Math.min(desde + pageSize, filtrados.size()) : filtrados.size();
        listado.setResultados(new ArrayList<>(filtrados.subList(desde, hasta)));

        return listado;
    }

    /**
     * MD-2179 y MD-2205:
     * Comparadores por columna para la ordenación en memoria de la solapa de
     * expedientes (ver obtenerListaExpedientesSolapa). Las claves coinciden
     * con el atributo 'field' de cada atlasp:columnaListaPaginada en
     * solapaExpedientes.xhtml.
     */
    private Comparator<ExpedienteDTO> obtenerComparadorExpediente(String property) {
        if (property == null) {
            return null;
        }
        switch (property) {
        case "cdExpediente":
            return Comparator.comparing(ExpedienteDTO::getCdExpediente,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "tipoActor":
            return Comparator.comparing(
                    e -> e.getConvocatoriaActDTO() != null && e.getConvocatoriaActDTO().getIdentificacionDTO() != null
                            ? e.getConvocatoriaActDTO().getIdentificacionDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "dni":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getCdNumIden() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "nombre":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsNombre() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "apellido1":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsApellido1() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "apellido2":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsApellido2() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "nombreSolicitante":
            return Comparator.comparing(
                    e -> e.getSolicitudDTO() != null && e.getSolicitudDTO().getInteresadoDTO() != null
                            ? e.getSolicitudDTO().getInteresadoDTO().getDsNombre() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "fechaPresentacion":
            return Comparator.comparing(ExpedienteDTO::getFcPresentacion,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estado":
            return Comparator.comparing(e -> e.getEstadoDTO() != null ? e.getEstadoDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estadoRevAdmin":
            return Comparator.comparing(e -> e.getEstadoRevAdminDTO() != null ? e.getEstadoRevAdminDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estadoRevRequi":
            return Comparator.comparing(e -> e.getEstadoRevRequiDTO() != null ? e.getEstadoRevRequiDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estadoRevEval":
            return Comparator.comparing(e -> e.getEstadoRevEvalDTO() != null ? e.getEstadoRevEvalDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estadoRevFinan":
            return Comparator.comparing(e -> e.getEstadoRevFinanDTO() != null ? e.getEstadoRevFinanDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "nmNota":
            return Comparator.comparing(ExpedienteDTO::getNmNota,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        default:
            return null;
        }
    }

    /**
     * MD-2179: Borra el filtro de estado de la tabla de expedientes.
     */
    public void borrarFiltroEstadoExpediente() {
        this.idEstadoFiltro = null;
    }

    /**
     * MD-2205: listAndCountMethod de la tabla "Expedientes participantes" de la solapa
     * de datos (atlasp:listaPaginada). Pagina en memoria la lista ya cargada
     * en this.lstExpedienteParticipante.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return página de ExpedienteDTO y total de lstExpedienteParticipante
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDTO> obtenerListaExpedienteParticipanteDatos(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDTO> listado = new AtlasListAndCount<>();

        List<ExpedienteDTO> lista = this.lstExpedienteParticipante != null ? this.lstExpedienteParticipante
                : new ArrayList<>();

        if (orderBean instanceof List && !((List<?>) orderBean).isEmpty()) {
            AtlasSort orden = ((List<AtlasSort>) orderBean).get(0);
            Comparator<ExpedienteDTO> comparador = this.obtenerComparadorExpedienteParticipante(orden.getProperty());
            if (comparador != null) {
                lista.sort(orden.isAscendente() ? comparador : comparador.reversed());
            }
        }

        listado.setTotal(lista.size());

        int desde = Math.min(index, lista.size());
        int hasta = pageSize > 0 ? Math.min(desde + pageSize, lista.size()) : lista.size();
        listado.setResultados(new ArrayList<>(lista.subList(desde, hasta)));

        return listado;
    }

    /**
     * MD-2205:
     * Comparadores por columna de la tabla "Expedientes participantes" de la
     * solapa de datos (ver obtenerListaExpedienteParticipanteDatos). Las
     * claves coinciden con el atributo 'field' de cada
     * atlasp:columnaListaPaginada en solapaDatos.xhtml.
     */
    private Comparator<ExpedienteDTO> obtenerComparadorExpedienteParticipante(String property) {
        if (property == null) {
            return null;
        }
        switch (property) {
        case "tipo":
            return Comparator.comparing(
                    e -> e.getInteresadoDTO() != null && e.getInteresadoDTO().getTipoIdentificacionDTO() != null
                            ? e.getInteresadoDTO().getTipoIdentificacionDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "identificador":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getCdNumIden() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "nombre":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsNombre() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "apellido1":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsApellido1() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "apellido2":
            return Comparator.comparing(e -> e.getInteresadoDTO() != null ? e.getInteresadoDTO().getDsApellido2() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "tipoActor":
            return Comparator.comparing(
                    e -> e.getConvocatoriaActDTO() != null && e.getConvocatoriaActDTO().getTipoPersonaFisJurDTO() != null
                            ? e.getConvocatoriaActDTO().getTipoPersonaFisJurDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "expediente":
            return Comparator.comparing(ExpedienteDTO::getCdExpediente,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        case "estadoExpediente":
            return Comparator.comparing(e -> e.getEstadoDTO() != null ? e.getEstadoDTO().getDsValor() : null,
                    Comparator.nullsLast(Comparator.naturalOrder()));
        default:
            return null;
        }
    }

    /**
     * Método para obtener la lista de participantes del expediente seleccionado
     *
     * @param expSel
     */
    public void obtenerExpedienteParticipante(ExpedienteDTO expSel) {
        try {
            List<ExpedienteDTO> lstExpedienteEstruc = new ArrayList<>();

            lstExpedienteEstruc = this.expedienteService
                    .obtenerExpedienteEstructurado(this.idSolicitud, Constantes.AfirmacionNegacion.N);
            this.lstExpedienteConContrato = new ArrayList<>();
            if (Boolean.TRUE.equals(expSel.getEsExpedienteSolicitante())) {
                this.lstExpedienteConContrato.addAll(lstExpedienteEstruc);
                this.lstExpedienteParticipante = lstExpedienteEstruc;
                this.lstExpedienteParticipante
                        .removeIf(x -> Boolean.FALSE.equals(x.getEsExpedienteParticipante()));
                //nueva lista para expedientes con contrato
                this.lstExpedienteConContrato
                .removeIf(x -> Boolean.FALSE.equals(x.getEsExpedienteContrato()));
                Iterator<ExpedienteDTO> iterator = this.lstExpedienteConContrato.iterator();
                while (iterator.hasNext()) {
                    ExpedienteDTO exp = iterator.next();
                    ExpedienteContratoDTO expCont = expedienteContratoService.obtenerExpedienteContratoByExpediente(exp);
                    if (expCont != null) {
                        exp.setExpedienteContrato(expCont);
                    } else {
                        iterator.remove();
                    }
                }
            } else if (Boolean.TRUE.equals(expSel.getEsExpedienteCoordinador())) {
                lstExpedienteEstruc = this.expedienteService
                        .buscarExpedientePorExpedientePadre(this.idSolicitud, expSel.getIdExpediente());
                this.lstExpedienteConContrato.addAll(lstExpedienteEstruc);
                
                this.lstExpedienteParticipante = lstExpedienteEstruc;
                
                //lista para expedientes con contrato
                this.lstExpedienteConContrato
                .removeIf(x -> Boolean.FALSE.equals(x.getEsExpedienteContrato()));
                Iterator<ExpedienteDTO> iterator = this.lstExpedienteConContrato.iterator();
                while (iterator.hasNext()) {
                    ExpedienteDTO exp = iterator.next();
                    ExpedienteContratoDTO expCont = expedienteContratoService.obtenerExpedienteContratoByExpediente(exp);
                    if (expCont != null) {
                        exp.setExpedienteContrato(expCont);
                    } else {
                        iterator.remove();
                    }
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

    }
    
    /**
     * Método para desistir un Expediente
     * Cambia estado a desistido y envia para Portafirmas
     * 
     */
    public void desistirExpediente() {
        try {
            ExpedienteDTO expDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);
                    
            //Cambiar estado a desistido
            this.expedienteService.cambiarEstado(this.idExpediente, 
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO), null);

            ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(idConvocatoria);
            
            //Enviar notificacion de desistimiento para portafirmas
            Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expDTO, idExpediente,  
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), null,null,null, null); 
            
            if(insertado.equals(Boolean.TRUE)) {
                AtlasMessageUtils.addInfoMessageFromBundle(
                      ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            }else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                      ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
            }
        
        
        }catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        }
    }
    
    /**
     * Metodo para validar si se habilita el boton de desistir Expediente
     *
     * @param ExpedienteDTO
     * @param Boolean
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonDesistirExpediente(ExpedienteDTO expediente, Boolean esModoConsulta) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;

        calcularExisteEnvioPortafirmasDesistimiento(expediente);
        
        
        if((expediente != null && expediente.getEstadoDTO() != null && expediente.getEstadoDTO().getIdValor() != null  
                &&  expediente.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)))
                || this.getExisteEnvioPtfrDesistidoFirmado() == Boolean.TRUE 
                || this.getExisteEnvioPtfrDesistidoEnProceso() == Boolean.TRUE
                || this.getExisteEnvioPtfrDesistidoEnCola() == Boolean.TRUE
                || Boolean.TRUE.equals(esModoConsulta)
                )
                {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    
    public boolean mostrarSolapaDocumentosConcesion() throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        
        if(this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }
    
        if(Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteSolicitante()) 
                || this.convocatoriaDTO.getIdTipoConcesion().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }
    
    public boolean mostrarSolapaRequisitos() throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        
        if(this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }
    
        if(Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteSolicitante()) 
                || this.convocatoriaDTO.getIdTipoConcesion().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }
    
    public boolean mostrarSolapaResolucion() throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        
        if(this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }
    
        if(Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteSolicitante()) 
                || this.convocatoriaDTO.getIdTipoConcesion().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }
    
    
    public boolean mostrarSolapaEvaluacion() throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        
        if(this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }
        
        if((Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteSolicitante()) 
                && this.convocatoriaDTO.getItEvaluacion().equals("S") 
                && this.convocatoriaDTO.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_SOLICITANTE))) 
                || (this.convocatoriaDTO.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) 
                        && this.convocatoriaDTO.getItEvaluacion().equals("S")  && !this.expedienteDTO.getEsExpedienteSolicitante())) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }
    

    public boolean mostrarSolapaSubsanacion() throws ServiceException { 
        Boolean habilitar = Boolean.FALSE; 
        
        if(this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.getIdConvocatoria());
        }
    
        if( (Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteSolicitante())  || Boolean.TRUE.equals(this.expedienteDTO.getEsExpedienteCoordinador()) ) 
                || this.convocatoriaDTO.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) ) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }
    
    
    /**
     * Metodo para calcular si existe un envío de desismiento a portafirmas
     *
     * @param dto
     */
    private void calcularExisteEnvioPortafirmasDesistimiento(ExpedienteDTO dto) {
        this.setExisteEnvioPtfrDesistidoEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistidoFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistidoEnCola(Boolean.FALSE);
        try {
                this.documentoPtfrDTO = this.documentoPtfrService.obtenerInformacionDesistido(dto.getIdExpediente());

                if (Objects.nonNull(this.documentoPtfrDTO.getTramiteDTO())) {
                    
                    this.setExisteEnvioPtfrDesistidoEnCola(Boolean.TRUE);                  
                    if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO.equals(this.documentoPtfrDTO.getCdEstadoPtfr()))
                             {
                        this.setExisteEnvioPtfrDesistidoFirmado(Boolean.TRUE);
                    }
                    if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE.equals(this.documentoPtfrDTO.getCdEstadoPtfr())) {
                        this.setExisteEnvioPtfrDesistidoEnProceso(Boolean.TRUE);
                    }
                }           

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
       
    }
    
    
    /**
     * Método para obtener lista de Expedientes con contratos
     * y controlar activación de botones según estado de expedientes
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return
     */
    public AtlasListAndCount<ExpedienteDTO> obtenerTotalExpedienteConContrato(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteDTO> lista = new AtlasListAndCount<>();

        this.obtenerExpedienteParticipante(this.expedienteDTO);
        lista.setResultados(this.lstExpedienteConContrato);
        lista.setTotal(this.lstExpedienteConContrato.size());
        this.documentoBean.setActivarBotonRequerimientoSubsanacion(Boolean.FALSE);
        if(!lista.getResultados().isEmpty()) {
            
            Integer idReqSubRen = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION_RENUNCIA);
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION);
            
            this.documentoBean.setActivarBotonRequerimientoSubsanacion( lista.getResultados().stream()
                    .anyMatch(x -> { 
                        CatalogosValoresDTO estado = null;
                        if(x.getExpedienteContrato() != null && x.getExpedienteContrato().getIdEstadoRevAdminDTO() != null) {
                            estado = x.getExpedienteContrato().getIdEstadoRevAdminDTO();
                        }  
                        return estado != null && estado.getIdValor() != null &&
                            (estado.getIdValor().equals(idReqSubRen) ||
                                    estado.getIdValor().equals(idReqSub));
                    })
                );
        }
        
        return lista;
    }
    
    public Boolean extenderFechaContrato(ExpedienteDTO expDto) throws ServiceException {
        
        if(expDto.getFcIniContrato() != null && expDto.getFcFinContrato() != null && this.idConvocatoria != null) {
            ConvocatoriaDTO conv = this.convocatoriaService
                    .buscarId(this.idConvocatoria);
            if(conv != null && conv.getNmDiasAmpliContrato() != null) {
                ExpedienteDTO expOriginal = this.expedienteService.buscarId(expDto.getIdExpediente());
                
                // Comprobar que fechaMaxNueva es posterior a fechaMax 
                if (expDto.getFcFinContrato().before(expDto.getFcIniContrato()) || expDto.getFcFinContrato().before(conv.getFcIniContrato())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CONTRATO_EXTENDER_FC_INICIAL_POSTERIOR);
                    return false; 
                } 
                // Comprobar que las fechas no son iguales 
                if (expDto.getFcFinContrato().equals(expOriginal.getFcFinContrato())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CONTRATO_EXTENDER_FC_MAXIMA_NO_MODIFICADA);
                    return false; 
                } 
                // Comprobar que la diferencia en días no es superior a NmDiasAmpliContrato 
                long diasDiferencia = ChronoUnit.DAYS.between(conv.getFcFinContrato().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
                        expDto.getFcFinContrato().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()); 
                if (diasDiferencia > conv.getNmDiasAmpliContrato()) { 
                    // Añadir NmDiasAmpliContrato a la fecha y mostrar fecha maxima posible
                    Date fechaMaxima =  Utils.fechaSumarRestarDias(
                            conv.getFcFinContrato(), conv.getNmDiasAmpliContrato());
                    AtlasMessageUtils.addErrorMessage("La fecha máxima permitida es: " + new SimpleDateFormat("dd/MM/yyyy").format(fechaMaxima));
                    

                    return false; 
                } 
                
                // Si todas las comprobaciones pasan, actualizamos
                expDto = this.expedienteService.actualizarFechaMaxContrato(expDto);
                this.expedienteDTO = expDto;
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.OK_CONTRATO_EXTENDER_INFO);

                return true;
            }            
            
        }
        AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CONTRATO_EXTENDER_INFO);

        return false;
    }
    
    public Boolean mostrarContratoSustitucion(ExpedienteDTO expedienteDTO) {
        Boolean mostrar = Boolean.FALSE;

        if (expedienteDTO != null) {
            if (Objects.nonNull(expedienteDTO.getExpedienteContratoSustitucion())) {
                mostrar = Boolean.TRUE;
            }
        }
        return mostrar;
    }

    
    /**
    *
    * @throws ServiceException
    */
   private void obtenerFilColPresupPresentadoConcepto(BloqueDTO bloque) throws ServiceException {

       List<BloqueComponenteDTO> lstFilasAux = bloque.getListaBloqueAuxDTO().stream()
               .filter(f -> f.getItFila()).collect(Collectors.toList());
       
       List<BloqueComponenteDTO> lstFilas = new ArrayList<>();
       
       for(BloqueComponenteDTO bloqueComp:lstFilasAux) {

           // Filtrar datos donde el idExpediente del DatoDTO sea igual al this.expedienteDTO.getIdExpediente(), y
           // deduplicar (VMM 02/09/26 MD-3011) ANTES de decidir la rama, para que el tamaño de la lista
           // (usado para elegir entre caso normal y multi-concepto) sea el real, sin duplicados.
           List<DatoDTO> datosFiltrados = bloqueComp.getDatos().stream()
                   .filter(dato -> dato.getExpedienteDTO() != null
                       && dato.getExpedienteDTO().getIdExpediente() != null
                       && dato.getExpedienteDTO().getIdExpediente().equals(this.expedienteDTO != null ? this.expedienteDTO.getIdExpediente() : this.idExpediente))
                   .collect(Collectors.toList());
           bloqueComp.setDatos(this.deduplicarDatosMasRecientePorFilaColumna(datosFiltrados));

           if(bloqueComp.getDatos().size()== listaAnualidades.size()+1) {

               bloqueComp.setDatos(
                       bloqueComp.getDatos().stream()
                       // Ordenar por nmColumna
                       .sorted(Comparator.comparing(DatoDTO::getNmColumna))
                       .collect(Collectors.toList())
               );
               // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
               Utils.normalizarSeparadorDecimalDatos(bloqueComp.getDatos());

               double sumaFila = bloqueComp.getDatos().stream()
                       .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                       .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                               && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                       ? this.parsearImporteBloque(dato.getDsValor())
                                       : 0)
                       .sum();

               DatoDTO dto = new DatoDTO();
               dto.setDsValor(String.valueOf(sumaFila));
               dto.setEsMontoTotal(Boolean.TRUE);
               dto.setNmColumna(bloqueComp.getDatos().size());
               bloqueComp.getDatos().add(dto);
               
               lstFilas.add(bloqueComp);
           }else if(bloqueComp.getDatos().size()> listaAnualidades.size()+2) {

               int posCelda= 0;
               bloqueComp.setDatos(
                       bloqueComp.getDatos().stream()
                       // Ordenar por nmFila
                       .sorted(Comparator.comparing(DatoDTO::getNmFila))
                       .collect(Collectors.toList())
               );
               for (int j=posCelda; j< bloqueComp.getDatos().size();j++){
                   List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                   BloqueComponenteDTO bloqueComConcepto= new BloqueComponenteDTO();
                   for (int index = 0; index < listaAnualidades.size()+1; index++) {
                       datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                       posCelda++;
                       j=posCelda;
                   }
                   
                   datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                   // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                   Utils.normalizarSeparadorDecimalDatos(datosFilaConcepto);
                   bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                   bloqueComConcepto.setDatos(datosFilaConcepto);
                   bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                   bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                   bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                   bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                   bloqueComConcepto.setItFila(bloqueComp.getItFila());
                   bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                   bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                   bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                   bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                   bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                   
                   double sumaFila = bloqueComConcepto.getDatos().stream()
                           .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                           .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                                   && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                           ? this.parsearImporteBloque(dato.getDsValor())
                                           : 0)
                           .sum();

                   DatoDTO dto = new DatoDTO();
                   dto.setDsValor(String.valueOf(sumaFila));
                   dto.setEsMontoTotal(Boolean.TRUE);
                   if(bloqueComConcepto.getDatos() != null && !bloqueComConcepto.getDatos().isEmpty()) {
                       dto.setNmColumna(bloqueComConcepto.getDatos().size()); 
                       dto.setNmFila(bloqueComConcepto.getDatos().get(0).getNmFila() != null 
                               ? bloqueComConcepto.getDatos().get(0).getNmFila() : 0); 
                   }
                   
                   bloqueComConcepto.getDatos().add(dto);
                   log.info("agregando monto total " + sumaFila + " en la fila " + dto.getNmFila());
                
                   
                   lstFilas.add(bloqueComConcepto);
                   
               }
                  
                   
           }
       }
       
       lstFilas = lstFilas.stream()
               .filter(f -> f.getItFila()).collect(Collectors.toList());
       
       if(lstFilas != null && !lstFilas.isEmpty() && lstFilas.get(0).getDatos() != null
               && !lstFilas.get(0).getDatos().isEmpty()) {
           int[] nColArray = IntStream.range(0, lstFilas.get(0).getDatos().size()).toArray();
           this.listaNcolPresupuesto = new ArrayList<>();
           
           for (int arr : nColArray) {
               this.listaNcolPresupuesto.add(arr);
           }
       }else {
           int[] nColArray = IntStream.range(0, this.listaAnualidades.size()).toArray();
           this.listaNcolPresupuesto = new ArrayList<>();
           
           for (int arr : nColArray) {
               this.listaNcolPresupuesto.add(arr);
           }
       }
       
       bloque.setListaBloqueAuxDTO(lstFilas);
       
       // Preparamos espacio para totales por columna
       BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
       bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
       bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("TOTAL");
       bloqueComponenteTotalFila.setDatos(new ArrayList<>());
       
       for (int i = 0; i < this.listaNcolPresupuesto.size(); i++) {
           double sumaColumna = 0;
           
           for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
               if (!fila.getDatos().isEmpty() && fila.getDatos().size() > i
                       && StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())
                       && fila.getDatos().get(i).getNmColumna() != 0) { // Ignorar elementos donde nmColumna es 0
                   sumaColumna += this.parsearImporteBloque(fila.getDatos().get(i).getDsValor());
               }
           }

            log.info("agregando monto total " + sumaColumna + " en la columna " + i); 

            DatoDTO dto = new DatoDTO();
            //VMM MD-1459 20/05/25: arreglo para que no haga sumatorio de totales en la columna de los conceptos
            if (i != 0) {
                dto.setDsValor(String.valueOf(sumaColumna));
            }else {
                dto.setDsValor("");
            }
            dto.setEsMontoTotal(Boolean.TRUE);
            bloqueComponenteTotalFila.getDatos().add(dto);
           
       }
       lstFilas.add(bloqueComponenteTotalFila);
   }

   /**
    * Metodo para obtener las fechas de los plazos de anualidades solicitados
    *
    * @param impresoDTO
    */
   private void obtenerPlazosSolicitados(Integer idImpreso) {
      try {

          //this.inicializarDatosPlazos();

         // if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdImpreso() != null) {
              //ImpresoDTO impresoDTO = this.impresoService.buscarId(this.selectExpedienteModifDTO.getIdImpreso());
              ImpresoDTO impresoDTO = this.impresoService.buscarId(idImpreso);

              if (Objects.nonNull(impresoDTO.getLstBloqueDTO())) { 
                   // Obtener registros de plazos
                   this.lstBloquePlazos = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
                           utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PLAZOS_EJEC_ANUAL));
       
                   List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
                   if (!this.lstBloquePlazos.isEmpty()) {
                       for (BloqueDTO bloqueDTO : this.lstBloquePlazos) {
       
                           lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
                                   .filter(x -> x.getFcBajaFila() == null)
                                   .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                                   .collect(Collectors.toList());
       
                           bloqueDTO.setListaBloqueAuxDTO(bloqueDTO.getLstBloqueComponenteDTO().stream()
                                   .filter(f -> f.getItFila()).collect(Collectors.toList()));
       
                           if (!bloqueDTO.getListaBloqueAuxDTO().isEmpty()) {
       
                               // Construimos la lista de columnas plazos que es fija siempre fecha ini y fecha fin
                              this.construirLstBloqueCompColsPlazos(bloqueDTO);
       
                               if (!this.lstBloqueComponenteColumnaPlazo.isEmpty()) {
                                   this.obtenerFilColPlazoSolicitado(bloqueDTO);
                               }
                           }
                       }
                   }
               }
           //}
       } catch (Exception e) {
           log.error(e.getMessage());
           AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
       }
   }
   
   /**
    * Método para inicializar los datos de las tablas de plazos con los valores de la convocatoria
    */
   /*private void inicializarDatosPlazos() throws ServiceException {
     
      // Recuperamos los datos de la convocatoria
      if (this.convocatoriaDTO == null) {
          this.convocatoriaDTO = this.convocatoriaService
              .buscarId(this.expedienteBean.getIdConvocatoria());
      }
      this.totalAnualidades = convocatoriaDTO.getNmDurMaxAyuda();

      this.construirLstAnualidades();
   }*/

   /**
    * Método para construir la lista de bloques componentes de las columnas de plazos para las fechas ini y fin
    */
   private void construirLstBloqueCompColsPlazos(BloqueDTO bloqueDTO) {
       this.lstBloqueComponenteColumnaPlazo = new ArrayList<>();
       
       //generamos componentes para las columnas fecha ini y fecha fin
       List<AtlasFilter> filtros = new ArrayList<>();
       List<AtlasSort> orden = new ArrayList<>();
       List<Integer> lstComponentes = new ArrayList<>();
       lstComponentes.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.FECHA_INICIO));
       lstComponentes.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.FECHA_FIN));
       filtros.add(new AtlasFilter(Constantes.Filtro.ID_COMP, lstComponentes, Operador.IN));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       orden.add(new AtlasSort(Constantes.Filtro.ID_COMP, true));
       List<Componente> listaComponente = this.componenteDAO.find(0, -1, filtros, orden);
       int i = 1;
       for (Componente componente : listaComponente) {
           BloqueComponente bloqueComponente = new BloqueComponente();
           bloqueComponente.setBloque(new Bloque());
           bloqueComponente.getBloque().setIdBloque(bloqueDTO.getIdBloque());
           bloqueComponente.setNmOrden(i);
           bloqueComponente.setItObligatorio(Constantes.AfirmacionNegacion.S);
           bloqueComponente.setItRelacion(Constantes.AfirmacionNegacion.N);
           bloqueComponente.setTipoFinanciacion(null);
           bloqueComponente.setItFila(Constantes.AfirmacionNegacion.N);
           bloqueComponente.setItColumna(Constantes.AfirmacionNegacion.S);
           bloqueComponente.setComponente(new Componente());
           bloqueComponente.getComponente().setIdComponente(componente.getIdComponente());
           this.lstBloqueComponenteColumnaPlazo.add(BloqueComponenteToDTO.INSTANCE.apply(bloqueComponente));
           i++;
       }
   }
   
   /**
    * Metodo para construir las listas de filas y columnas de la tabla de plazos ejecucion anualidades
    * @throws ServiceException
    */
   private void obtenerFilColPlazoSolicitado(BloqueDTO bloque) throws ServiceException {

       // Construimos lista con idColumna
       int[] nColArray = IntStream.range(0, 2).toArray();
       this.listaNcolPlazo = new ArrayList<>();

       for (int arr : nColArray) {
           this.listaNcolPlazo.add(arr);
       }
    
       //if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
           // Recuperar datos de los bloques componente fila
           for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
    
               //List<DatoDTO> lstDatoDTO =  this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(),
                //       this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), fila.getIdBloqueComponente(), true);
               
               List<DatoDTO> lstDatoDTO =  this.datoService.consultarDatosImpresoModifPorIdExpModif(this.getIdExpediente(),
                       null, fila.getIdBloqueComponente(), true);
               
               //filtramos por los más recientes por si se guardó mas de una modificacion en el fomulario
               if (lstDatoDTO.size() >= (this.listaNcolPlazo.size() * 2)) {              
                   lstDatoDTO = lstDatoDTO.stream()
                           .skip(Math.max(0, lstDatoDTO.size() - this.listaNcolPlazo.size())) // Saltar los primeros elementos
                           .collect(Collectors.toList());
               }
                   
               fila.setDatos(lstDatoDTO);
           }
    
           this.lstBloqueComponenteFilaPlazo = bloque.getListaBloqueAuxDTO();
       //}
    }
   

    /**
     * Método auxiliar para formatear las fechas de formato IS
     */
    public String formatFecha(String isoDate) {
        // Convierte la cadena ISO en Instant
        Instant instant = Instant.parse(isoDate); 
        
        // Ajusta a la zona horaria local 
        ZoneId zone = ZoneId.of("Europe/Madrid"); //ZoneId.systemDefault(); //este default tb esta sumando un dia de mas
       
        // VMM 16/10/26 MD-3150: ver ExpedienteModifPresPlazoOtraBean.formatFecha - atZone(Europe/Madrid) ya
        // da la fecha correcta, no hay que restar un dia (se veia 15/03 en vez de 16/03)
        LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }
    
    
    public void formatearBloqueMultiregistro(List<BloqueDTO> listaSubbloqueDTO){
 
        int totalRegistros= 0;
        int totalComponentes=0;
               
        if(!listaSubbloqueDTO.isEmpty()) {
            for(BloqueDTO subBloque:listaSubbloqueDTO) {
                if(subBloque.getEsTipoDocumento().equals(Boolean.FALSE) 
                        && !subBloque.getLstBloqueComponenteDTO().isEmpty()){
                    BloqueComponenteDTO blqComp= subBloque.getLstBloqueComponenteDTO().get(0);
                    totalComponentes = totalComponentes+subBloque.getLstBloqueComponenteDTO().size();
                    if(!blqComp.getDatos().isEmpty()) {
                        totalRegistros=blqComp.getDatos().size();
                    }
                }     
            }
            
            this.lstNumFilasMultiregistro = new ArrayList<>();
                      
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            for(int i=0;i<totalRegistros; i++) {
                this.lstNumFilasMultiregistro.add(i);
                
                for(BloqueDTO subBloque:listaSubbloqueDTO) {
                    if(subBloque.getEsTipoDocumento().equals(Boolean.FALSE) 
                            && !subBloque.getLstBloqueComponenteDTO().isEmpty()){
                        for(BloqueComponenteDTO blqComp:subBloque.getLstBloqueComponenteDTO()) {
                            try {
                                if(blqComp.getDatos() != null && !blqComp.getDatos().isEmpty() &&  blqComp.getDatos().size()> i) {
                                    String valorDato = blqComp.getDatos().get(i).getDsValor();
                                    if(utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_FECHA)
                                                .equals(blqComp.getComponenteDTO().getSubTipoDTO().getIdValor()) && valorDato != null ) {
                                        valorDato= Utils.dateToString(dateFormat.parse(valorDato), Constantes.Utiles.FORMATO_FECHA_HORA);
                                    }
                                   
                                    blqComp.getDatos().get(i).setDsValor(valorDato);
                                } 
                            } catch (ParseException e) {
                                log.error(e.getMessage());
                            }
                        }      
                    }
                }
            }
        }
    }
    
    
    public boolean comprobarCorreoInteresados(ExpedienteDTO expediente) throws ServiceException {
        Boolean mostrar = Boolean.FALSE;
        String nif = "";
        String correoNote = "";
        
        if (expediente != null && expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null && 
                expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_INTERESADO)) {
            
            if (expediente.getInteresadoDTO() != null) {
                nif = expediente.getInteresadoDTO().getCdNumIden();
                
                // VALIDACIÓN AÑADIDA: Solo llamamos si hay NIF
                if (StringUtils.isNotBlank(nif)) {
                    correoNote = noteAdmeService.recuperarEmailNote(nif);
                    if (correoNote == null || !expediente.getInteresadoDTO().getDsEmail().equalsIgnoreCase(correoNote)) {
                        mostrar = Boolean.TRUE;
                    }
                }
            }
                                    
        } else if (expediente != null && expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null && 
                expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE)) {
            
            if (expediente.getRepresentanteLegal() != null) {
                nif = expediente.getRepresentanteLegal().getCdNumIden();  
                
                // VALIDACIÓN AÑADIDA: Solo llamamos si hay NIF
                if (StringUtils.isNotBlank(nif)) {
                    correoNote = noteAdmeService.recuperarEmailNote(nif);
                    if (correoNote == null || !expediente.getRepresentanteLegal().getDsEmail().equalsIgnoreCase(correoNote)) {
                        mostrar = Boolean.TRUE;
                    }
                }
            }             
        }

        return mostrar;
    }
    
    
    public boolean isExpedienteDesistidoOExcluido() {
        if (this.getExpedienteDTO() == null) {
            return false;
        }

        ExpedienteDTO exp = this.getExpedienteDTO();

        Integer estDesistido = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO);
        Integer estExcluido = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO);
        Integer estDesistidoProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);
        Integer estExcluidoProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);

        Integer idEstadoActual = exp.getEstadoDTO().getIdValor();
        Integer idEstadoProv = exp.getEstadoProvisionalDTO() != null ? exp.getEstadoProvisionalDTO().getIdValor() : null;
        Integer idEstadoRevReq = exp.getEstadoRevRequiDTO() != null ? exp.getEstadoRevRequiDTO().getIdValor() : null;

        return Objects.equals(idEstadoActual, estDesistido)
            || Objects.equals(idEstadoActual, estExcluido)
            || Objects.equals(idEstadoProv, estDesistidoProv)
            || Objects.equals(idEstadoProv, estExcluidoProv)
            || Objects.equals(idEstadoRevReq, estExcluidoProv);
    }
    
    /** Guarda la nota introducida en la solapa de evaluación si tiene valor. */
    public void guardarNotaEvaluacion() {
        if (this.expedienteDTO == null
                || Boolean.TRUE.equals(this.expedienteDTO.getModoConsulta())
                || this.convocatoriasBean.isModoConsulta()
                || this.expedienteDTO.getNmNota() == null) {
            return;
        }
        try {
            this.expedienteService.guardarNotaEvaluacion(
                    this.expedienteDTO.getIdExpediente(), this.expedienteDTO.getNmNota());
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.EVALUACION_NOTA_GUARDAR_KO, e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_NOTA_GUARDAR_KO);
        }
    }
    
}
