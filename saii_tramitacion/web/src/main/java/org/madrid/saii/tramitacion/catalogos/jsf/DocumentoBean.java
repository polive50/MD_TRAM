package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.core.icda.services.CoreIcdaRestService;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.services.BloqueService;
import org.madrid.saii.lib.services.CataService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaCauContService;
import org.madrid.saii.lib.services.ConvocatoriaCauService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ConvocatoriaTipoDocService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioNotificacionesPortafirmasService;
import org.madrid.saii.lib.services.ExpedienteContratoDocService;
import org.madrid.saii.lib.services.ExpedienteContratoService;
import org.madrid.saii.lib.services.ExpedienteDocHistService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteJusEcoService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.services.ExpedienteReqService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.AtlasCommonConfigurationUtils;
import atlas.core.util.UserUtils;
import atlas.eadm.icda.ClienteIcdaCmWSService;
import atlas.eadm.icda.ClienteIcdaTgssWSService;
import atlas.eadm.icda.ClienteIcdaWSService;
import atlas.eadm.icda.domain.IcdaDatosAppPeticionaria;
import atlas.eadm.icda.domain.IcdaDatosBasicosTitular;
import atlas.eadm.icda.domain.IcdaSolicitarCertificadoCenoOUT;
import atlas.eadm.icda.domain.IcdaSolicitarCertificadoORVAOUT;
import atlas.eadm.icda.domain.IcdaSolicitarCertificadoOUT;
import atlas.eadm.icda.domain.IcdaSolicitudCertificadoIAEIN;
import atlas.eadm.icda.domain.IcdaSolicitudCertificadoIAEOUT;
import atlas.eadm.icda.domain.IcdaSolicitudCertificadoSituacionDeudaOUT;
import atlas.eadm.icda.domain.IcdaSolicitudConsultarIdentidadOUT;
import atlas.eadm.icda.domain.IcdaSolicitudConsultarIdentidadV3OUT;
import atlas.eadm.icda.domain.IcdaTitularCertificadoSituacionDeuda;
import atlas.eadm.icda.domain.IcdaTitularConsultarIdentidad;
import atlas.eadm.icda.domain.IcdaTitularSolicitarCertificado;
import atlas.eadm.note.domain.NoteExisteNifOut;
import atlas.eadm.services.IcdaCmService;
import atlas.eadm.services.IcdaService;
import atlas.eadm.services.IcdaTgssService;
import atlas.eadm.services.NoteService;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Documento}</code>
 *
 * @author everis
 *
 * @since 01-oct-2020 12:22:25
 *
 * @see DocumentoBean
 */
@Slf4j
@Getter
@Setter
@Component("documentoBean")
@Scope("request")
public class DocumentoBean extends CatalogoBean<ExpedienteDoc, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * dto de documento
     */
    private DocumentoDTO documentoDTO;

    /**
     * dto de expediente doc
     */
    private ExpedienteDocDTO expedienteDocDTO;

    /**
     * dto de formularioDocExternoDTO
     */
    private ExpedienteDocDTO formularioDocExternoDTO;
    
    /**
     * dto de formularioDocExternoDTO
     */
    private ExpedienteDocDTO formularioDocExternoReqDTO;
    
    
    /**
     * dto de formularioDocExternoDTO
     */
    private ExpedienteDocDTO formularioDocExternoSubirDTO;
    

    /**
     * Archivo a subir
     */
    private UploadedFile archivoSubir;
    
    
    /**
     * Archivo a subir
     */
    private UploadedFile archivoSubirDoc;
    
    /**
     * Archivo a subir
     */
    private UploadedFile contratoSubir;
    
    private UploadedFile archivoSubirDesistimiento;
    
    
    private UploadedFile archivoSubirSubsanacionADM;

    /** Campo idTipoDocumento. */
    private CatalogosValoresDTO estadoDocumento;

    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Cache de la convocatoria de idConvocatoria, para evitar recuperarla varias veces
     * en la misma petición (el bean es de scope "request", así que no puede quedar obsoleta).
     */
    private ConvocatoriaDTO convocatoriaDTO;

    private CatalogosValoresDTO tipoDocumento;

    /**
     * Archivo a subir en el editar.
     */
    private UploadedFile archivoSubirEditar;

    /**
     * Archivo temporal
     *
     */
    private String nombreArchivoTemporal;

    /**
     * Identificador de expediente seleccionado
     */
    private Integer idExpediente;

    /**
     * Indicador de habilitación de proceso de carga de archivo
     */
    private Boolean flgCarga;

    /** Propiedad de filtrado */
    private List<AtlasFilter> filtrosValores;

    /**
     * Atributos proceso subsanacion
     */
    private ExpedienteDocDTO documentoSeleccionado;

    /**
     * Atributos para el documento de envio de portafirmas
     */
    private DocumentoPtfrDTO documentoPtfrDTO;

    /**
     * Campo observaciones de los documentos
     */
    private String tlObservaciones;
    
    
    /**
     * Campo observaciones de los documentos
     */
    private String tlObservacionesGestor;
    
    /**
     * Identificador del documento cuyas observaciones se están editando
     */
    private Integer idDocumentoObservaciones;
    
    /**
     * Campo observaciones para el ciudadano de los documentos
     */
    private String tlObservacionesParaCiu;

    /**
     * Atributo para controlar el modo lectura del formulario subsanar
     * documentos
     */
    private Boolean modoLecturaSubsanacion = Boolean.FALSE;

    /**
     * Atributo para almacenar la causa de denegación seleccionada
     */
    private Integer idCausaSubsanacion;

    /**
     * Atributo para almacenar la descripción de la causa de denegación
     * seleccionada
     */
    private String descripcionCausaSubsanacion;

    /**
     * Atributo para controlar si es obligatorio ingresar la descripción de
     * causa de denegación
     */
    private Boolean descripcionCausaObligatoria = Boolean.FALSE;

    /**
     * Indicador para consultar si existe un envío a PTFR subsanacion en proceso
     */
    private Boolean existeEnvioPtfrSubsanacionEnProceso;

    /**
     * Indicador para consultar si existe un envío a PTFR subsanacion en proceso
     */
    private Boolean existeEnvioPtfrSubsanacionFirmado;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrDesistidoEnProceso;

    /**
     * Indicador para consultar si existe un envío a PTFR exclusion
     */
    private Boolean existeEnvioPtfrDesistidoFirmado;

    /**
     * Comentario del motivo de rechazo del documento seleccionado
     */
    private String motivoRechazoSeleccionado;

    /**
     * Indicador para verificar si el usuario esta dado de alta
     */
    private Boolean usuarioDadoAlta;

    /**
     * VMM: evita repetir la llamada a noteService.existeNif en estaDadoAltaUsuario()
     * dentro de la misma carga, ya que solo se invoca desde los metodos de carga inicial
     * de los listados de documentos/contratos y el resultado no cambia en esa carga.
     */
    private boolean usuarioDadoAltaCalculado = false;

    /**
     * Indicador para determinar el colo de fondo del plazo de subsanacion
     */
    private Boolean plazoSuperadoTramitePendiente;

    /**
     * Identificador de panel de formulario requisitos a refrescar
     */
    private String formularioDocumentos = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelDocumentosSolapa";

    /**
     * Identificador de campo estado de expediente a refrescar
     */
    private String campoEstadoExpediente = "convocatoriaForm:contenedorSolapas:dsEstadoExpedienteSel";

    /**
     * Identificador de solapa de documentos
     */
    private String solapaDocumentos = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";
    
    
    /**
     * Identificador de solapa de contratos
     */
    private String solapaRevisionContratos = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:solapaRevisionContratos";

    /**
     * Campo para comprobar el numero de puntos en el nombre del archivo a subir
     */
    private long nombrePuntosTotal = 0;
    
    private String textoObservacionesSubsanacion;
    
    /** 
     * Indicador para habilitar boton de requerimiento subsanacion 
    */
    private Boolean activarBotonRequerimientoSubsanacion = Boolean.FALSE;
    
    /** 
     * Indicador para habilitar boton de finalizacion revision contratos
    */
    private Boolean activarBotonFinalizacionContratos = Boolean.FALSE;
    
    /** 
     * Indicador para habilitar boton de finalizacion revision expediente
     */
    private Boolean activarBotonFinalizacionExpediente = Boolean.FALSE;
    
    /** 
     * Indicador para habilitar boton de desistir expediente
     */
    private Boolean activarBotonDesistirExpediente = Boolean.FALSE;
    
    
    private Boolean activarBotonEnvioANotificaciones;
    
    private Boolean activarBotonfinalizarRevision; 
    
    private Boolean activarBotonDesistir; 
    
    private Boolean mostrarBotonDocumento; 
    
    private Boolean activarBotonDocumento; 
    
    
    
    /**
     * Identificador de expediente contratos seleccionado
     */
    private Integer idExpedienteContratos;
    
    /**
     * dto de expediente contrato doc
     */
    private ExpedienteDTO expedientePartiDTO;
    
    private Boolean habilitarObservacionesContrato = Boolean.FALSE;
    
    private Boolean autorizaConsulta = Boolean.TRUE;
    
    private Boolean renderRev1 = Boolean.TRUE;  //  OK / SUBSANAR disabled
    private Boolean renderRev2 = Boolean.FALSE; //  OK / SUBSANAR enabled
    private Boolean renderRev3 = Boolean.FALSE; //  OK / KO       enabled
    private Boolean renderRev4 = Boolean.FALSE; //  OK            enabled
    private Boolean renderRevObs = Boolean.FALSE; // Observaciones para ciudadano
    
    
    private List<ExpedienteDocDTO> listadoDocumentos;

    // Lista COMPLETA (sin paginar) de documentos del expediente, usada solo por los métodos
    // habilitarBotonXxx para comprobar reglas de negocio que necesitan ver todos los documentos,
    // no solo la página actual de la tabla (listadoDocumentos sí está paginado)
    private List<ExpedienteDocDTO> listadoDocumentosCompleto;


    
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
     * Servicio de expedienteDocService
     */
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;
    
    /**
     * Servicio de expedienteContratoDocService
     */
    @Autowired
    @Qualifier("expedienteContratoDocService")
    private ExpedienteContratoDocService expedienteContratoDocService;

    /**
     * Servicio de expedienteReqServices
     */
    @Autowired
    @Qualifier("expedienteReqService")
    private ExpedienteReqService expedienteReqService;

    /**
     * Servicio de expedienteDocService
     */
    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;

    /**
     * Servicio de convocatoriaCauService
     */
    @Autowired
    @Qualifier("convocatoriaCauService")
    private ConvocatoriaCauService convocatoriaCauService;
    
    /**
     * Servicio de convocatoriaCauContService
     */
    @Autowired
    @Qualifier("convocatoriaCauContService")
    private ConvocatoriaCauContService convocatoriaCauContService;

    /**
     * Servicio de documentoService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /** Servicio de PtfrAdmeService. */
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    /**
     * Servicio de CatalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** Servicio de Ereg. */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;
    
    
    /** Servicio de Ereg. */
    @Autowired
    @Qualifier("EnvioNotificacionesPortafirmasService")
    private EnvioNotificacionesPortafirmasService envioNotificacionesPortafirmasService;
    
    /**
     * Servicio ExpedienteJusTecService
     */
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    
//    @Autowired
//    @Qualifier("icdaCmWSService") 
//    private IcdaCmWSService icdaCmWSService; 
//        
//    @Autowired
//    @Qualifier("icdaTgssWSService") 
//    private IcdaTgssWSService icdaTgssWSService; 
//    
//    @Autowired
//    @Qualifier("icdaWSService") 
//    private IcdaWSService icdaWSService; 
       
    private ClienteIcdaTgssWSService clienteIcdaTgssWSService;

    private ClienteIcdaWSService clienteIcdaWSService;

    private ClienteIcdaCmWSService clienteIcdaCmWSService;

	@Autowired
    @Qualifier("icdaService") /*Anotación opcional depende de escaneo de apl*/
    private IcdaService icdaService;

    @Autowired
    @Qualifier("icdaCmService") /*Anotación opcional depende de escaneo de apl*/
    private IcdaCmService icdaCmService;

    @Autowired
    @Qualifier("icdaTgssService") /*Anotación opcional depende de escaneo de apl*/
    private IcdaTgssService icdaTgssService;
    
    @Autowired
    @Qualifier("icdaRestService") 
    private CoreIcdaRestService icdaRestService; 
    
    
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
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("cataService")
    private CataService cataService;

    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;
    
    /** DAO de ExpedienteContrato. */
    @Autowired
    @Qualifier("expedienteContratoDAO")
    private ExpedienteContratoDAO expedienteContratoDAO;
    
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;
    
    
    /**
     * Servicio ExpedienteJusTecService
     */
    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService expedienteJusEcoService;
    
    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService convocatoriaTipoDocService;
    
    
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    
    private ExpedienteBean expedienteBean;
    
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;
    
    
    @Autowired
    @Qualifier("comboBean")
    private ComboBean combobean;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public DocumentoBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad Dato
     *
     * @return entidad de tipo Dato
     */
    @Override
    public ExpedienteDoc crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.inicializarDocumento();
    }

    /**
     * Metodo que inicializa a las Documentos
     *
     */
    public void inicializarDocumento() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        this.formularioDocExternoReqDTO = new ExpedienteDocDTO();
        this.formularioDocExternoSubirDTO = new ExpedienteDocDTO();
        this.documentoDTO = new DocumentoDTO();
        this.expedienteDocDTO = new ExpedienteDocDTO();
        this.estadoDocumento = new CatalogosValoresDTO();
        this.flgCarga = Boolean.FALSE;
        this.expedientePartiDTO = new ExpedienteDTO();
        this.tlObservacionesParaCiu = null;
        
        //VMM 12/08/08
//        this.tlObservacionesParaCiu = "";
//        this.listadoDocumentos = new AtlasListAndCount<>();
//        this.setExisteEnvioPtfrSubsanacionFirmado(Boolean.FALSE);
//        this.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.FALSE);
//        this.setExisteEnvioPtfrDesistidoEnProceso(Boolean.FALSE);
//        this.setExisteEnvioPtfrDesistidoFirmado(Boolean.FALSE);
//        this.documentoPtfrDTO = new DocumentoPtfrDTO();
//        this.usuarioDadoAlta = Boolean.FALSE;
//        this.plazoSuperadoTramitePendiente = Boolean.FALSE;
//        this.formularioDocExternoDTO = new ExpedienteDocDTO();
    }
    
    /**
     * Metodo que inicializa los contratos
     *
     */
    public void inicializarContratos() {
        
        this.activarBotonFinalizacionContratos = Boolean.FALSE;
        this.idExpedienteContratos = null;
        this.expedientePartiDTO = new ExpedienteDTO();
    }

    /**
     * Recupera el listado de objetos ExpedienteDocDTO que cumple con los
     * criterios de búsqueda. Devuelve las entidades situadas entre las
     * posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param ordenacion - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filter - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Componente
     */
    /**
     * Recupera la ConvocatoriaDTO de this.idConvocatoria, cacheándola en el bean para
     * no repetir la consulta (con todas sus subconsultas) varias veces en la misma
     * carga de la pestaña de documentos.
     */
    private ConvocatoriaDTO obtenerConvocatoriaCacheada() throws ServiceException {
        if (this.convocatoriaDTO == null
                || !Objects.equals(this.convocatoriaDTO.getIdConvocatoria(), this.idConvocatoria)) {
            this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
        }
        return this.convocatoriaDTO;
    }

    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocDTO> obtenerListaDocumentoExpediente(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();

        try {
            if (this.idExpediente != null) {
                
                List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
                List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

                this.estaDadoAltaUsuario();
                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());

                /*filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                        lstIdExpediente, Operador.IN));*/

//                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
//                        this.idExpediente, Operador.EQUALS));
//
//                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                listado = busquedaDocumentosSolicitud(this.idExpediente, orden, pageSize, index);

                this.listadoDocumentos = listado.getResultados();

                //sirve para en enviar a la cola y para el borrador
                activarBotonEnvioANotificaciones = habilitarBotonEnviarSubanasarPortafirmas(this.isModoConsulta(), this.expedienteBean.getConvocatoriaDTO().getIdEstado().getIdValor());

                activarBotonfinalizarRevision =  habilitarBotonFinalizarRevision(this.isModoConsulta(), this.expedienteBean.getConvocatoriaDTO().getIdEstado().getIdValor());

                //sirve para en enviar a la cola y para el borrador
                activarBotonDesistir = habilitarBotonEnviarDesistirPortafirmas(this.isModoConsulta(), this.expedienteBean.getConvocatoriaDTO().getIdEstado().getIdValor());

                mostrarBotonDocumento = mostrarBotonAgregarDocumentoExterno(this.isModoConsulta());
                activarBotonDocumento = deshabilitarBotonAgregarDocumentoExterno(this.isModoConsulta());

            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        
        return listado;
    }
    

    
    
    public AtlasListAndCount<ExpedienteDocDTO> busquedaDocumentosSolicitud(Integer idExpediente, List<AtlasSort> orden, Integer pageSize, Integer index) throws ServiceException{

        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();
        List<AtlasFilter> filtros = new ArrayList<AtlasFilter>();
        
        Expediente expediente = ExpedienteToEntity.INSTANCE.apply(this.expedienteBean.getExpedienteDTO());
        
        List<Integer> lstTramites = new ArrayList<>();
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_SOLICITUD_IDSOLICITUD,
                expediente.getSolicitud().getIdSolicitud(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, lstTramites, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        ConvocatoriaDTO convocatoria = this.obtenerConvocatoriaCacheada();
        if (Objects.equals(convocatoria.getIdTipoConcesion().getIdValor(), utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
        } 

        if (orden == null) {
            orden = new ArrayList<>();
        }
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CDANEXOEREG, true));
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC, true));
        
        if (filtros == null || filtros.isEmpty()) {
            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.idExpediente, Operador.EQUALS));
        }
        
        List<ExpedienteDocDTO> listadoDatos;
        
        if (Objects.equals(convocatoria.getIdTipoConcesion().getIdValor(), utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) { 
            listadoDatos = this.expedienteDocService.buscarDocumentos(index,pageSize, expediente.getSolicitud().getIdSolicitud(),lstTramites, idExpediente, orden);  
        } else {
            listadoDatos = this.expedienteDocService.buscarDocumentos(index,pageSize, expediente.getSolicitud().getIdSolicitud(),lstTramites, null, orden);  
        }
        
        StringBuilder cadenaobservacion = new StringBuilder();
        Integer idEstDocuPendiRevSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);

        // -----------------------------------------------------------------------------------
        // NUEVO: Preparamos los parámetros globales necesarios para verificar la subsanación
        // -----------------------------------------------------------------------------------
        Boolean esModoConsulta = this.isModoConsulta();
        Integer idEstadoConv = (convocatoria != null && convocatoria.getIdEstado() != null) 
                ? convocatoria.getIdEstado().getIdValor() : null;
        // -----------------------------------------------------------------------------------
        
        // Obtenemos todos los datos del expdteDTO de cada documento que vamos a listar
        if (Objects.equals(convocatoria.getIdTipoConcesion().getIdValor(), utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
            for (ExpedienteDocDTO expDocDTO: listadoDatos) {
                
                // NUEVO: Asignamos el permiso de subsanar en cada iteración del bucle existente
                expDocDTO.setPuedeSubsanar(this.habilitarBotonSubsanarDocumento(esModoConsulta, idEstadoConv, expDocDTO));
                expDocDTO.setPuedeDescargarICDA(this.habilitarDescargaIDCA(expDocDTO));

                if (expDocDTO.getEstadoDTO().getIdValor().equals(idEstDocuPendiRevSub) && expDocDTO.getTlObservaciones() != null) {
                    cadenaobservacion.append(expDocDTO.getTlObservaciones()+ "\n ");
                }
                
                if (expDocDTO.getFcRegistro() != null) {
                    String fechaFormateada = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(expDocDTO.getFcRegistro());              
                    expDocDTO.setFechaRegistroString(fechaFormateada);
                } else {
                    expDocDTO.setFechaRegistroString("");
                }
                
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK).equals(expDocDTO.getEstadoDTO().getIdValor())) {
                    expDocDTO.setEstilo("colorVerde");
                }
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        ) {
                    expDocDTO.setEstilo("colorRojo");
                }
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION).equals(expDocDTO.getEstadoDTO().getIdValor())) {
                    expDocDTO.setEstilo("colorAmarillo");
                }
            }           
        } else {
            // CONCESION_SOLICITUD
            for (ExpedienteDocDTO expDocDTO: listadoDatos) {
                
                // NUEVO: Asignamos el permiso de subsanar en cada iteración del bucle existente
                expDocDTO.setPuedeSubsanar(this.habilitarBotonSubsanarDocumento(esModoConsulta, idEstadoConv, expDocDTO));
                expDocDTO.setPuedeDescargarICDA(this.habilitarDescargaIDCA(expDocDTO));

                if (expDocDTO.getEstadoDTO().getIdValor().equals(idEstDocuPendiRevSub) && expDocDTO.getTlObservaciones() != null) {
                    cadenaobservacion.append(expDocDTO.getTlObservaciones()+ "\n ");
                }
                
                if (expDocDTO.getFcRegistro() != null) {
                    String fechaFormateada = new java.text.SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(expDocDTO.getFcRegistro());              
                    expDocDTO.setFechaRegistroString(fechaFormateada);
                } else {
                    expDocDTO.setFechaRegistroString("");
                }
                
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK).equals(expDocDTO.getEstadoDTO().getIdValor())) {
                    expDocDTO.setEstilo("colorVerde");
                }
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO).equals(expDocDTO.getEstadoDTO().getIdValor())
                        ) {
                    expDocDTO.setEstilo("colorRojo");
                }
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION).equals(expDocDTO.getEstadoDTO().getIdValor())) {
                    expDocDTO.setEstilo("colorAmarillo");
                }
            }
        }
        
        if (cadenaobservacion != null && !cadenaobservacion.toString().isEmpty()) {
            this.textoObservacionesSubsanacion = cadenaobservacion.toString();
        }

        listado.setResultados(listadoDatos);
        listado.setTotal(this.expedienteDocService.count(filtros)); 
        
        return listado;
    }
    
    private boolean habilitarDescargaIDCA(ExpedienteDocDTO expDocDTO) {
        if (expDocDTO == null || expDocDTO.getEstadoDTO() == null) {
            return false;
        }

        // 1. Validar que esté autorizado
        boolean autorizado = "S".equalsIgnoreCase(expDocDTO.getItAutoriza());

        // 2. Validar estados permitidos (SIN_SUBIR o PENDIENTE_REVISION)
        Integer estadoActual = expDocDTO.getEstadoDTO().getIdValor();
        Integer idSinSubir = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        Integer idPendienteRevision = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);

        boolean estadoValido = Objects.equals(estadoActual, idSinSubir) || Objects.equals(estadoActual, idPendienteRevision);

        return autorizado && estadoValido;
    }
    
    
    /*private List<ExpedienteDocDTO> recuperarExpediente(List<ExpedienteDocDTO> lstExpedienteDocDTO, List<ExpedienteDTO> lstExpedienteDTO) {
        ArrayList<ExpedienteDocDTO> lista = new ArrayList<>();
        for (ExpedienteDocDTO expedienteDocuDTO : lstExpedienteDocDTO) {
            for (ExpedienteDTO expedienteDTO : lstExpedienteDTO) {
                if (expedienteDocuDTO.getExpedienteDTO().getIdExpediente().equals(expedienteDTO.getIdExpediente())) {
                    expedienteDocuDTO.setExpedienteDTO(expedienteDTO);
                    
                    //si el iden que vas a meter ya esixte en la lista no lo metas, por que se esta duplicando
                    if(!existeYa(expedienteDocuDTO,lista)) {
                        lista.add(expedienteDocuDTO);
                    }
                }
            }
        }
        return lista;
    }*/
    
    
    /*private boolean existeYa(ExpedienteDocDTO cliente,  ArrayList<ExpedienteDocDTO> lista ){
        boolean existe = false;
        for(int i = 0; i < lista.size(); i++){
           if(lista.get(i).getIdExpedienteDoc().equals(cliente.getIdExpedienteDoc())){
              existe = true;
              break;
           }
        }
        return existe;
     }*/
    
    
    /*private List<ExpedienteDocDTO> filtrarDocumentosByExpediente(Integer idExpediente, List<ExpedienteDocDTO> listadoDatos){
        
        List<ExpedienteDocDTO> listaAuxiliar = new ArrayList<>();
        
            for (ExpedienteDocDTO documento : listadoDatos) {
                if(documento.getExpedienteDTO().getIdExpediente().equals(idExpediente)) {
                    listaAuxiliar.add(documento);
                }
            }
        
        return listaAuxiliar;
    }*/
    
    private void habilitarBotonFinalizarRevisionContratos(AtlasListAndCount<ExpedienteContratoDocDTO> listaExpDoc) throws ServiceException {
        // VMM 08/09/26 MD-2996 (reabierta): si el expediente del participante está Rechazado
        // Definitivo, no se permite finalizar la revisión de sus contratos.
        if (this.expedientePartiDTO != null && this.expedientePartiDTO.getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedientePartiDTO.getEstadoDTO().getIdValor())) {
            this.setActivarBotonFinalizacionContratos(Boolean.FALSE);
            return;
        }
        if(this.expedientePartiDTO != null && this.expedientePartiDTO.getExpedienteContrato() != null
                && ((this.expedientePartiDTO.getExpedienteContrato().getIdEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO).equals(this.expedientePartiDTO.getExpedienteContrato().getIdEstadoDTO().getIdValor()))
                || (this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO).equals(this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())))) {
            this.setActivarBotonFinalizacionContratos(Boolean.FALSE);
            return;
        }
        Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
        Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
        Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        this.setActivarBotonFinalizacionContratos( listaExpDoc.getResultados().stream()
                .allMatch(x -> (x.getExpedienteDocDTO().getEstadoDTO().getIdValor().equals(idRevOk))
                        || (x.getExpedienteDocDTO().getEstadoDTO().getIdValor().equals(idReqSub))
                        || (idSinSub.equals(x.getExpedienteDocDTO().getEstadoDTO().getIdValor()))
                        && ((x.getExpedienteDocDTO().getBloqueComponenteDTO() != null && !x.getExpedienteDocDTO().getBloqueComponenteDTO().getItObligatorio()) 
                                || x.getExpedienteDocDTO().getBloqueComponenteDTO() == null)));
                                
        //Si se trata RENUNCIA SIN SUSCTITUCION
        if(this.expedientePartiDTO != null && this.expedientePartiDTO.getExpedienteContrato() != null
                && ((this.expedientePartiDTO.getExpedienteContrato().getIdEstadoDTO() != null
                && this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA).equals(this.expedientePartiDTO.getExpedienteContrato().getIdEstadoDTO().getIdValor())))) {
            this.setActivarBotonFinalizacionContratos(Boolean.TRUE);
        }
        
        if(Boolean.TRUE.equals(this.activarBotonFinalizacionContratos) && this.expedientePartiDTO != null 
                && StringUtils.isBlank(this.expedientePartiDTO.getEstadoRevision())) {
            //Si no se ha marcado check ok/subsanacion mantenemos el botón deshabilitado
            this.setActivarBotonFinalizacionContratos(Boolean.FALSE);
        }
        //Si alguno esta está KO también lo activmos
        Integer idRevKo = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO);
        Boolean existeKO = listaExpDoc.getResultados().stream()
                .anyMatch(x -> (x.getExpedienteDocDTO().getEstadoDTO().getIdValor().equals(idRevKo)));
        if(Boolean.TRUE.equals(existeKO)) {
            this.setActivarBotonFinalizacionContratos(Boolean.TRUE);
        }
        
    }
    
    public Boolean habilitarTextoObservacionesContrato() {
        Boolean habilitar = Boolean.FALSE;
        if(this.expedientePartiDTO.getEstadoRevision() != null 
                && (this.expedientePartiDTO.getEstadoRevision().equals(Constantes.RevisionContratos.SUBSANAR) 
                    || this.expedientePartiDTO.getEstadoRevision().equals(Constantes.RevisionContratos.KO))) {
            habilitar = Boolean.TRUE;
        }
        return habilitar;
    }
    
    public void habilitaCarga() {
        this.flgCarga = Boolean.TRUE;
    }

    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void nuevoExpedienteDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogNuevoExpedienteDocBIS");
    }

    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void cerrarExpedienteDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogNuevoExpedienteDocBIS");
    }
    
    public void nuevoDesistimientoDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        // PrimefacesUtils.mostrarPopupPF("dialogDesistimientoDoc");
        PrimefacesUtils.mostrarPopupPF("dialogDesistimientoExpedienteDoc");
    }
    
    public void cerrarDesistimientoDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        // PrimefacesUtils.ocultarPopupPF("dialogDesistimientoDoc");
        PrimefacesUtils.ocultarPopupPF("dialogDesistimientoExpedienteDoc");
    }
    
    public void nuevoDesistimientoDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogDesistimientoExpedienteDoc");
    }
    
    public void cerrarDesistimientoDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogDesistimientoExpedienteDoc");
    }
    
    public void desistimientoRevisionDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogDesistimientoRevisionDoc");
    }
    
    public void cerrarDesistimientoRevisionDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogDesistimientoRevisionDoc");
    }
    
    public void nuevoDesistimientoRevisionDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogDesistimientoRevisionExpedienteDoc");
    }
    
    public void cerrarDesistimientoRevisionDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogDesistimientoRevisionExpedienteDoc");
    }
    
    public void nuevoDesistimientoExpDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogDesistimiento");
    }
    
    public void cerrarDesistimientoExpDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogDesistimiento");
    }
    
 
    public void nuevoDesistimientoDocGeneralDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogDesistimientoExpedienteDocGeneral");
    }
    
    public void cerrarDesistimientoDocGeneralDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogDesistimientoExpedienteDocGeneral");
    }
    
    
    //dialogs para subsanacion administrativa 
    
    public void abrirSubsanacionAdmDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogSubsanacionADMDoc");
    }
    
    public void cerrarSubsanacionAdmDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogSubsanacionADMDoc");
    }
    
    
    public void abrirSubsanacionAdmDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogSubsanarADMDocumento");
    }
    
    public void cerrarSubsanacionAdmDocDialog() {
        this.formularioDocExternoDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogSubsanarADMDocumento");
    }
    
    
    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void subirExpedienteDocDialog(ExpedienteDocDTO documento) {
        this.formularioDocExternoSubirDTO = documento;
        //VMM 06/02/26 MD-2449: cuando se sube otra version del doc poner la fecha y el EREG vacios
        //VMM 22/01/2026 MD-2278: formateamos el codigo EREG en caso de tenerlo para que luego lo valide bien al subir doc
        if (documento.getCodigoEreg() != null) {
            this.formularioDocExternoSubirDTO.setCodigoEreg(null);
        }
        if (documento.getFcRegistro() != null) {
            this.formularioDocExternoSubirDTO.setFcRegistro(null);
        }
        PrimefacesUtils.mostrarPopupPF("dialogSubirExpedienteDoc");
    }

    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void cerrarSubirExpedienteDocDialog() {
        this.formularioDocExternoSubirDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogSubirExpedienteDoc");
    }
    
    
    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void nuevoExpedienteDocRequerirDialog() {
        this.formularioDocExternoReqDTO = new ExpedienteDocDTO();
        PrimefacesUtils.mostrarPopupPF("dialogNuevoExpedienteDocRequerir");
    }

    /**
     * Metodo que agrega un nuevo expediente Documento
     *
     */
    public void cerrarExpedienteDocRequerirDialog() {
        this.formularioDocExternoReqDTO = new ExpedienteDocDTO();
        PrimefacesUtils.ocultarPopupPF("dialogNuevoExpedienteDocRequerir");
    }
    

    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     *
     * @param convocatoriaDTO
     */
    public void guardarDocumentoExterno() {

        if (this.formularioDocExternoDTO.getTramiteDTO() == null
                || this.formularioDocExternoDTO.getTramiteDTO().getIdValor() == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TRAMITE);
        } else if (this.formularioDocExternoDTO.getTipoDocumentoDTO() == null
                || this.formularioDocExternoDTO.getTipoDocumentoDTO().getIdValor() == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TIPO_DOCUMENTO);
        } else if( this.validarCodigoEreg(this.formularioDocExternoDTO.getCodigoEreg()) == Boolean.FALSE  ){
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_CODIGO_EREG_FORMATO);
        } else if (this.formularioDocExternoDTO.getTipoDocumentoDTO() != null
                && this.formularioDocExternoDTO.getTipoDocumentoDTO().getIdValor() == -1
                && this.formularioDocExternoDTO.getTlObservaciones() != null
                && this.formularioDocExternoDTO.getTlObservaciones().trim().length() <= 0) { // validamos si ha seleccionado Otros, también tiene que completar el campo observaciones
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_OTROS_DOC_OBS_OBLIGATORIO);
        } else if (this.archivoSubir == null
                || StringUtils.isBlank(this.archivoSubir.getFileName())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_ARCHIVO);
        } else {

             try (InputStream fileInputStream = this.archivoSubir.getInputstream()) {
                this.nombreArchivoTemporal = this.archivoSubir.getFileName();

                this.formularioDocExternoDTO.setFicaMetadatos(new FicaMetadatos());
                // Nombre, fecha y extension del fichero
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setNombreFichero(this.nombreArchivoTemporal);
                this.formularioDocExternoDTO.getFicaMetadatos().setFechaFichero(new Date());
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setExtension(this.nombreArchivoTemporal.substring(
                                this.nombreArchivoTemporal.lastIndexOf('.') + 1,
                                this.nombreArchivoTemporal.length()));
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setUsuario(UserUtils.getUserName().toUpperCase());
                this.formularioDocExternoDTO.getFicaMetadatos().setEncriptar(false);
                // Datos del fichero
                this.formularioDocExternoDTO
                        .setContenidoFichero(IOUtils.toByteArray(fileInputStream));

                if(this.formularioDocExternoDTO.getExpedienteDTO() == null) {
                    this.formularioDocExternoDTO.setExpedienteDTO(new ExpedienteDTO());
                }
                this.formularioDocExternoDTO.getExpedienteDTO().setIdExpediente(this.idExpediente);
                
                if(this.formularioDocExternoDTO.getCodigoEreg() != null && !this.formularioDocExternoDTO.getCodigoEreg().isEmpty() ) {
                    formularioDocExternoDTO.setCodigoEreg(Utils.desformatearNmEreg(this.formularioDocExternoDTO.getCodigoEreg()));
                }

                this.expedienteDocService.guardarExterno(this.formularioDocExternoDTO);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

                this.actualizarValoresFormulario();

                this.inicializarDocumento();

            } catch (IOException | ServiceException e) {
                log.error(e.getMessage());

                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);

            }

        }
    }

    private Boolean validarCodigoEreg(String codigoEreg) {
        if(codigoEreg == null || codigoEreg.isEmpty() ) {
            return true;
        }else {
            String formatoEeeg = "^\\d{2}/\\d{6}\\.\\d/\\d{2}$";
           return  codigoEreg.matches(formatoEeeg);    
        }
    }
    
    
    public String codigoEregFormateado(String codigoEreg) {
       
       return  Utils.formatearNmEreg(codigoEreg);    
    }
    
 
    public List<SelectItem> getComboTipoDocumentoConvocatoriaFiltrado(Integer idConvocatoria) {
        List<SelectItem> listadoTipoDocumentos = new ArrayList<>();

         listadoTipoDocumentos = this.combobean.getComboTipoDocumentoConvocatoria(idConvocatoria);

        // Igual que en los habilitarBotonXxx: necesitamos TODOS los documentos del expediente
        // (no solo la página actual de la tabla) para filtrar bien los tipos ya subidos
        if (Objects.isNull(this.listadoDocumentosCompleto)) {
            try {
                AtlasListAndCount<ExpedienteDocDTO> listado = busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);
                this.listadoDocumentosCompleto = listado.getResultados();
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }
        }

        // Verificar si listadoDocumentosCompleto es nulo antes de iterar
        if (this.listadoDocumentosCompleto != null && !this.listadoDocumentosCompleto.isEmpty()) {
            Iterator<SelectItem> iterator = listadoTipoDocumentos.iterator();
            while (iterator.hasNext()) {
                SelectItem tipoDoc = iterator.next();

                // Adicionalmente, podrías verificar si tipoDoc o su valor son nulos
                if (tipoDoc != null && tipoDoc.getValue() != null) {
                    for (ExpedienteDocDTO expdoc : this.listadoDocumentosCompleto) {
                        // Verificar si expdoc y sus propiedades no son nulas
                        if (expdoc != null && expdoc.getTipoDocumentoDTO() != null && expdoc.getTipoDocumentoDTO().getIdValor() != null &&
                                (expdoc.getTipoDocumentoDTO().getIdValor().equals(tipoDoc.getValue()))) {
                                iterator.remove();
                                break;
                            
                        }
                    }
                }
            }
        }
          
        return listadoTipoDocumentos;
    }
    
    
    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     *
     * @param convocatoriaDTO
     */
    public void subirDocumentoExterno() {

        if (this.archivoSubirDoc == null
                || StringUtils.isBlank(this.archivoSubirDoc.getFileName())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_ARCHIVO);
          //VMM 06/02/26 MD-2449: comprobar que no sea nulo el EREG cuando lo vaciamos en varias versiones del doc
        }else if(this.formularioDocExternoSubirDTO.getCodigoEreg() != null &&
                this.validarCodigoEreg(this.formularioDocExternoSubirDTO.getCodigoEreg()) == Boolean.FALSE  ){
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_CODIGO_EREG_FORMATO);
        }else {

            try (InputStream fileInputStream = this.archivoSubirDoc.getInputstream()) {
                this.nombreArchivoTemporal = this.archivoSubirDoc.getFileName();


                //ExpedienteDocDTO formularioDocDTO = this.expedienteDocService.buscarId(this.formularioDocExternoSubirDTO.getIdExpedienteDoc());
                
                if(formularioDocExternoSubirDTO != null) {
                    formularioDocExternoSubirDTO.setFicaMetadatos(new FicaMetadatos());
                    // Nombre, fecha y extension del fichero
                    formularioDocExternoSubirDTO.getFicaMetadatos()
                            .setNombreFichero(this.nombreArchivoTemporal);
                    formularioDocExternoSubirDTO.getFicaMetadatos().setFechaFichero(new Date());
                    formularioDocExternoSubirDTO.getFicaMetadatos()
                            .setExtension(this.nombreArchivoTemporal.substring(
                                    this.nombreArchivoTemporal.lastIndexOf('.') + 1,
                                    this.nombreArchivoTemporal.length()));
                    formularioDocExternoSubirDTO.getFicaMetadatos()
                            .setUsuario(UserUtils.getUserName().toUpperCase());
                    formularioDocExternoSubirDTO.getFicaMetadatos().setEncriptar(false);
                    // Datos del fichero
                    formularioDocExternoSubirDTO.setContenidoFichero(IOUtils.toByteArray(fileInputStream));

                    if(formularioDocExternoSubirDTO.getExpedienteDTO() == null) {
                        formularioDocExternoSubirDTO.setExpedienteDTO(new ExpedienteDTO());
                    }
                    formularioDocExternoSubirDTO.getExpedienteDTO().setIdExpediente(this.idExpediente);
                    
                    if(this.formularioDocExternoSubirDTO.getCodigoEreg() != null && !this.formularioDocExternoSubirDTO.getCodigoEreg().isEmpty() ) {
                        formularioDocExternoSubirDTO.setCodigoEreg(Utils.desformatearNmEreg(this.formularioDocExternoSubirDTO.getCodigoEreg()));
                    }
                    //VMM 11/02/26 MD-2471: quitamos el guardado de las observaciones del gestor al subir documento ya que estan en el propio objeto
                    //VMM 06/02/26 MD-2449: comprobar que no sea nula la fecha cuando la vaciamos en varias versiones del doc
                    if (this.formularioDocExternoSubirDTO.getFcRegistro() != null) {
                        formularioDocExternoSubirDTO.setFcRegistro(this.formularioDocExternoSubirDTO.getFcRegistro());
                    }
                                        
                    this.expedienteDocService.guardarExternoExistente(formularioDocExternoSubirDTO, this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor());

                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

                    this.actualizarValoresFormulario();

                    this.inicializarDocumento();
                }

            } catch (IOException | ServiceException e) {
                log.error(e.getMessage());

                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO); 

            }
        }
    }
    
    

    
    /**
     * Método que controla la descargar de fichero tipo 1
     *
     * @param imprDTO Datos de impreso
     * @param doc Datos de impreso
     */
    public void descargarDocumentoFica(Integer codigoFica) {

        try {

            if (codigoFica != null) {

                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);

                if (ficaFichero != null) {
                    this.documentoDTO = new DocumentoDTO();
                    this.documentoDTO.setFicaFichero(ficaFichero);
                    this.documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
                    this.documentoDTO.setCdFica(codigoFica);

                    log.info("Documento " + this.documentoDTO.getFicaFichero().getNombreFichero()
                            + " recuperado");

                    // Se prepara el fichero.
//                    UtilesWeb.openFile(this.documentoDTO.getContenidoFichero(),
//                            this.documentoDTO.getFicaFichero().getExtension(),
//                            FilenameUtils.removeExtension(
//                                    this.documentoDTO.getFicaFichero().getNombreFichero()));
                    
                    UtilesWeb.verDocumentoFica(this.documentoDTO.getContenidoFichero(),
                            this.documentoDTO.getFicaFichero().getExtension(),
                            FilenameUtils.removeExtension(this.documentoDTO.getFicaFichero().getNombreFichero()));
                    
                    
                    

                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * Método que descarga el documento de EREG
     *
     * @param expedienteDocDTO documento del expediente
     */
    public void descargarDocumentoEreg(ExpedienteDocDTO expedienteDocDTO) {
        try {
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdMinimo(this.idExpediente);

            byte[] documento = this.eregAdmeService.descargarAnexoPorCodigo(
                    expedienteDTO.getCdEreg(), expedienteDocDTO.getCdAnexoEreg());

            DateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA_DESCARGA);

            // Se prepara el fichero.
            UtilesWeb.openFile(documento, Constantes.Extensiones.PDF,
                    expedienteDocDTO.getCdAnexoEreg() + "_"
                            + sdf.format(expedienteDocDTO.getFcAltaFila()));
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO, ex.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }

    /**
     * Método para inicializar formulario de subsanación
     */
    private void inicializarFormularioSubsanacion() {
        this.idCausaSubsanacion = null;
        this.descripcionCausaSubsanacion = null;
        this.documentoSeleccionado = null;
        this.modoLecturaSubsanacion = Boolean.FALSE;
        this.descripcionCausaObligatoria = Boolean.FALSE;
    }

    /**
     * Método para mostrar la ventana de asignación de causa de denegación al
     * tipo de documento
     *
     * @param documento Datos del documento
     */
    public void mostrarSubsanarDocumento(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        //documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
        this.documentoSeleccionado = documento;
        if (this.documentoSeleccionado != null) {
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumento').show();");
        }        
    }
    
   
    
    public List<SelectItem> getListaCausasDenegacionAdministrativa() {
        Integer causaDenegacion = Constantes.TipoCausaDenegacion.CAUSA_DENEG_ADM;
        return listaCausaDenegacion(causaDenegacion);
    }
    
    /**
     * Método para obtener la lista de causas de denegación asignadas a la
     * convocatoria
     *
     * @return List<SelectItem> Lista de causas de denegación
     */
    public List<SelectItem> listaCausaDenegacion(Integer causaDenegacion) {
        List<SelectItem> listaCausas = new ArrayList<>();
        try {

            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosEntrada
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_CAUSA_DENEGACION_IDVALOR,
                                utilConstantes.getGlobalCatalogosValores().get(causaDenegacion),
                                Operador.EQUALS));

                List<AtlasSort> orden = new ArrayList<>();
                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CAUSADTO_DSVALOR, true));
                
                List<ConvocatoriaCauDTO> listaConvCau = this.convocatoriaCauService.buscar(0, -1,
                        filtrosEntrada, orden);

                if (listaConvCau != null && !listaConvCau.isEmpty()) {
                    listaConvCau.stream()
                            .forEach(convcau -> listaCausas
                                    .add(new SelectItem(convcau.getIdConvocatoriaCau(),
                                            convcau.getCausaDTO().getDsValor())));
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        return listaCausas;
    }
    
    /**
     * Método para obtener la lista de causas de denegación asignadas a la
     * convocatoria y contrato
     *
     * @return List<SelectItem> Lista de causas de contratos
     */
    public List<SelectItem> listaCausasSubsanacionContratos(Integer idConvocatoria) {
        List<SelectItem> listaCausas = new ArrayList<>();
        try {

            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosEntrada
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_CAUSA_DENEGACION_IDVALOR,
                                utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_CONTRATOS),
                                Operador.EQUALS));
                
                List<AtlasSort> orden = new ArrayList<>();
                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CAUSADTO_DSVALOR, true));

                List<ConvocatoriaCauDTO> listaConvCau = this.convocatoriaCauService.buscar(0, -1,
                        filtrosEntrada, orden);

                if (listaConvCau != null && !listaConvCau.isEmpty()) {
                    listaConvCau.stream()
                            .forEach(convcau -> listaCausas
                                    .add(new SelectItem(convcau.getIdConvocatoriaCau(),
                                            convcau.getCausaDTO().getDsValor())));
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        return listaCausas;
    }

    /**
     * Método para controlar el cambio de causa de denegación seleccionada
     *
     * @param event Evento del combo
     */
    public void cambioCausaSelectItem(ValueChangeEvent event) {
        try {
            this.descripcionCausaObligatoria = Boolean.FALSE;
            this.idCausaSubsanacion = null;
            if (event.getNewValue() != null) {
                this.idCausaSubsanacion = (Integer) event.getNewValue();
                ConvocatoriaCauDTO convCau = this.convocatoriaCauService
                        .buscarId(this.idCausaSubsanacion);
                if (convCau != null && convCau.getCausaDTO() != null
                        && convCau.getCausaDTO().getNmConstante() != null
                        && convCau.getCausaDTO().getNmConstante()
                                .equals(Constantes.CausaDenegacion.CODIGO_CAUSA_ADMIN_OTROS)) {
                    this.descripcionCausaObligatoria = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR);
        }
    }
    
    
    /**
     * Método para controlar el cambio de causaCont de denegación seleccionada
     *
     * @param event Evento del combo
     */
    public void cambioCausaContSelectItem(ValueChangeEvent event) {
        try {
            this.descripcionCausaObligatoria = Boolean.FALSE;
            this.idCausaSubsanacion = null;
            if (event.getNewValue() != null) {
                this.idCausaSubsanacion = (Integer) event.getNewValue();
                ConvocatoriaCauDTO cauCont = this.convocatoriaCauService
                        .buscarId(this.idCausaSubsanacion);
                if (cauCont != null && cauCont.getCausaDTO() != null
                        && cauCont.getCausaDTO().getNmConstante() != null
                        && cauCont.getCausaDTO().getNmConstante()
                                .equals(Constantes.CausaDenegacion.CODIGO_CAUSA_CONTRATO_OTROS)) {
                    this.descripcionCausaObligatoria = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR);
        }
    }

    /**
     * Método para validar los datos del formulario de subsanación.
     *
     * @return Boolean Indicador true o false
     */
//    private Boolean validarFormularioSubsanacion() {
//        Boolean existeError = Boolean.FALSE;
//        if (this.idCausaSubsanacion == null) {
//            AtlasMessageUtils.addWarnMessageFromBundle(
//                    ConstantesWeb.Err.ERROR_VALIDACION_CAUSA_DENEGACION_NO_SELECCIONADO);
//            existeError = Boolean.TRUE;
//        } else if (this.descripcionCausaObligatoria.equals(Boolean.TRUE)
//                && StringUtils.isBlank(this.descripcionCausaSubsanacion)) {
//            AtlasMessageUtils.addWarnMessageFromBundle(
//                    ConstantesWeb.Err.ERROR_VALIDACION_DESC_CAUSA_DENEGACION_NO_INGRESADO);
//            existeError = Boolean.TRUE;
//        }
//        return existeError;
//    }
    
    
    private Boolean validarFormularioSubsanacion() {
        Boolean existeError = Boolean.FALSE;
        
        if (this.idCausaSubsanacion == null) {
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_CAUSA_DENEGACION_NO_SELECCIONADO);
            existeError = Boolean.TRUE;
        } else if (Boolean.TRUE.equals(this.descripcionCausaObligatoria)
                && StringUtils.isBlank(this.descripcionCausaSubsanacion)) {
            // Solo lanzará error si la causa exija descripción Y el texto esté vacío
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_DESC_CAUSA_DENEGACION_NO_INGRESADO);
            existeError = Boolean.TRUE;
        }
        
        return existeError;
    }

    /**
     * Método para grabar la asignación de causa de subsanación al documento
     */
    public void grabarCausaSubsanarDocumento() {

        try {

            if (this.documentoSeleccionado != null) {
                
                documentoSeleccionado =  this.expedienteDocService.buscarId(documentoSeleccionado.getIdExpedienteDoc());
                
                if (this.validarFormularioSubsanacion().equals(Boolean.FALSE)) {
                    ConvocatoriaCauDTO convocatoriaCauDTO = this.convocatoriaCauService
                            .buscarId(this.idCausaSubsanacion);
                    this.documentoSeleccionado.setEstadoDTO(new CatalogosValoresDTO(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)));
                    this.documentoSeleccionado.setConvocatoriaCauDTO(convocatoriaCauDTO);
                    this.documentoSeleccionado
                            .setTlCausaDescripcion(this.descripcionCausaSubsanacion);
                    this.expedienteDocService.guardarConHistorico(this.documentoSeleccionado);
                    this.actualizarValoresFormulario();
                    this.inicializarFormularioSubsanacion();
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                    PrimeFaces.current().executeScript("PF('dialogSubsanaDocumento').hide();");
                }

            } else {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTOS_NO_SELECCION_DOCUMENTO);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
     * Método para grabar la asignación de causa de subsanación de contratos
     */
    public void grabarCausaSubsanarDocContratos() {

        try {

            if (this.documentoSeleccionado != null) {

                if (this.validarFormularioSubsanacion().equals(Boolean.FALSE)) {
                    
                    documentoSeleccionado =  this.expedienteDocService.buscarId(documentoSeleccionado.getIdExpedienteDoc());

                    this.documentoSeleccionado.setEstadoDTO(new CatalogosValoresDTO(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)));
                    ConvocatoriaCauDTO cauCont = this.convocatoriaCauService
                            .buscarId(this.idCausaSubsanacion);
                    this.documentoSeleccionado.setConvocatoriaCauDTO(cauCont);
                    if(StringUtils.isBlank(this.descripcionCausaSubsanacion) && 
                            (cauCont != null && cauCont.getCausaDTO() != null)) {
                        this.documentoSeleccionado
                            .setTlCausaDescripcion(cauCont.getCausaDTO().getDsValor());
                    }else {
                        this.documentoSeleccionado
                            .setTlCausaDescripcion(this.descripcionCausaSubsanacion);
                    }
                    
                    this.expedienteDocService.guardarConHistorico(this.documentoSeleccionado);
                    this.actualizarValoresFormulario();
                    this.inicializarFormularioSubsanacion();
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                    PrimeFaces.current().executeScript("PF('dialogSubsanaDocContratos').hide();");
                }

            } else {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTOS_NO_SELECCION_DOCUMENTO);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Método para cancelar y cerrar el formulario de causa de subsanación
     */
    public void cancelarCausaSubsanarDocumento() {
        this.inicializarFormularioSubsanacion();
    }

    /**
     * Método para desaprobar un documento
     *
     * @param documento Datos del documento
     */
    public void desaprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
            this.expedienteDocService.guardarConHistorico(documento);
            this.actualizarValoresFormulario();
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
     * Método para desaprobar un documento
     *
     * @param documento Datos del documento
     */
    public void desaprobarDocumentoDetalleParticipantes(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
            
            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
            this.expedienteDocService.guardarConHistorico(documento);
            this.actualizarValoresFormulario();
            if(this.expedientePartiDTO != null && this.expedientePartiDTO.getIdExpediente() != null) {
                this.idExpedienteContratos = this.expedientePartiDTO.getIdExpediente();
            }
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Método para aprobar un documento
     *
     * @param documento Datos del documento
     */
    public void aprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());

            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            documento.setConvocatoriaCauDTO(null);
            documento.setTlCausaDescripcion(null);

            ExpedienteDocDTO expeDTO = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());

            if (documento.getTipoDocumentoDTO().getIdValor() == null) {
                documento.setTipoDocumentoDTO(
                        new CatalogosValoresDTO(expeDTO.getTipoDocumentoDTO().getIdValor()));
            }
            this.expedienteDocService.guardarConHistorico(documento);
            this.actualizarValoresFormulario();
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Método para revertir operación de evaluación de documento
     *
     * @param documento Datos de documento
     */
    public void revertirEvaluacionDocumento(ExpedienteDocDTO documento) {

        try {

            this.documentoSeleccionado = null;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());

            // Obtenemos el ultimo registro historico
            List<AtlasFilter> filtroHist = new ArrayList<>();
            filtroHist
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                            documento.getIdExpedienteDoc(), Operador.EQUALS));

            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    documento.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));

            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

            List<ExpedienteDocHistDTO> lstHistorico = this.expedienteDocHistService.buscar(0, -1,
                    filtroHist, ordenamientoHist);

            if (lstHistorico != null && !lstHistorico.isEmpty()) {
                // Recuperamos el valor historico anterior
                ExpedienteDocHistDTO registroHistoricoEstadoAnterior = lstHistorico.get(0);
                // Asignamos estado anterior
                documento.setEstadoDTO(registroHistoricoEstadoAnterior.getEstadoDTO());
                // Recuperamos valores de subsanación
                documento.setConvocatoriaCauDTO(registroHistoricoEstadoAnterior.getConvocatoriaCauDTO());
                documento.setTlCausaDescripcion(registroHistoricoEstadoAnterior.getTlCausaDescripcion());

                if (registroHistoricoEstadoAnterior.getCdFica() != null
                        && !registroHistoricoEstadoAnterior.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR))) {
                    documento.setCdFica(registroHistoricoEstadoAnterior.getCdFica());
                } else {
                    documento.setCdFica(null);
                }

                this.expedienteDocService.guardarConHistorico(documento);

                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
     * Método para revertir operación de evaluación de documento contrato
     *
     * @param documento Datos de documento
     */
    public void revertirEvaluacionDocumentoContrato(ExpedienteDocDTO documento) {
        
        try {
            
            this.documentoSeleccionado = null;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
            
            // Obtenemos el ultimo registro historico
            List<AtlasFilter> filtroHist = new ArrayList<>();
            filtroHist
            .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                    documento.getIdExpedienteDoc(), Operador.EQUALS));
            
            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    documento.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));
            
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            
            List<ExpedienteDocHistDTO> lstHistorico = this.expedienteDocHistService.buscar(0, -1,
                    filtroHist, ordenamientoHist);
            
            if (lstHistorico != null && !lstHistorico.isEmpty()) {
                // Recuperamos el valor historico anterior
                ExpedienteDocHistDTO registroHistoricoEstadoAnterior = lstHistorico.get(0);
                // Asignamos estado anterior
                documento.setEstadoDTO(registroHistoricoEstadoAnterior.getEstadoDTO());
                // Limpiamos valores de subsanación
                documento.setConvocatoriaCauContDTO(null);
                documento.setTlCausaDescripcion(null);
                
                if (registroHistoricoEstadoAnterior.getCdFica() != null
                        && !registroHistoricoEstadoAnterior.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR))) {
                    documento.setCdFica(registroHistoricoEstadoAnterior.getCdFica());
                } else {
                    documento.setCdFica(null);
                }
                
                this.expedienteDocService.guardarConHistorico(documento);
                
                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Método para mostrar la ventana popup con el histórico de estados de un
     * documento
     *
     * @param documento Datos del documento
     */
    public void mostrarHistoricoDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());
            if (this.documentoSeleccionado != null) {
                PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentos').show();");
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * Método para mostrar la ventana popup con el histórico de observaciones de
     * un documento
     *
     * @param documento Datos del documento
     */
    public void mostrarHistoricoDocumentoObservaciones(String observaciones,String observacionesGestor ) {
        this.idDocumentoObservaciones = null;
        this.tlObservaciones = observaciones;
        this.tlObservacionesGestor = observacionesGestor;
        
        //VMM 11/02/26 MD-2471: Mostramos el dialogo si existen observaciones del ciudadano o del gestor
        if (this.tlObservaciones != null || this.tlObservacionesGestor != null) {
            PrimeFaces.current()
                    .executeScript("PF('dialogHistoricoDocumentosObservaciones').show();");
        }
    }
    
    public void mostrarHistoricoDocumentoObservacionesContrato(String observaciones) {
        this.tlObservaciones = observaciones;
        if (this.tlObservaciones != null) {
            PrimeFaces.current()
            .executeScript("PF('dialogHistoricoDocumentosObservacionesContrato').show();");
        }
    }
    
    public void cerrarObservacionesContrato() {
        this.tlObservaciones = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosObservacionesContrato').hide();");

        // Si el objeto documentoSeleccionado es nulo, es que el popup de
        // observaciones no se ha llamado desde el popup de historico.
        // y no entra en el flujo del if. Caso contrario, el popup observaciones
        // se ha llamado desde el popup historico, y se volverá a abrir el popup
        // historico, despues de cerrar el de observaciones.
        if (null != this.documentoSeleccionado) {
            this.mostrarHistoricoDocumentoContrato(this.documentoSeleccionado);
        }
    }
    
    public void mostrarHistoricoDocumentoContrato(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());
            if (this.documentoSeleccionado != null) {
                PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosContrato').show();");
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

    }
    
    public void cerrarHistoricoDocumentoContrato() {
        this.documentoSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosContrato').hide();");
    }

    /**
     * Método para obtener la lista de históricos de estados de un documento
     *
     * @param index Indice
     * @param pageSize Cantidad páginas
     * @param orderBean Ordenamiento
     * @param filtros Filtros
     * @return Lista de histórico de estados de documento
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocHistDTO> obtenerListaHistoricoDocumento(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDocHistDTO> listadoDatos = new AtlasListAndCount<>();
        try {
            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                this.documentoSeleccionado.getIdExpedienteDoc(), Operador.EQUALS));

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        this.documentoSeleccionado.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));

                if (null == orderBean) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> ordenamientoHist = (List<AtlasSort>) orderBean;
                ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

                List<ExpedienteDocHistDTO> listado = this.expedienteDocHistService.buscar(index,
                        pageSize, filtrosEntrada, ordenamientoHist);
                listadoDatos.setResultados(listado);
                listadoDatos.setTotal(this.expedienteDocHistService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listadoDatos;
    }

    /**
     * Método para cerrar la ventana popup de históricos de estados de documento
     */
    public void cerrarHistoricoDocumento() {
        this.documentoSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentos').hide();");
    }

    /**
     * Método para cerrar la ventana popup de observaciones
     */
    public void cerrarObservaciones() {
        this.tlObservaciones = null;
        //VMM 11/02/26 MD-2471: reseteamos observaciones del gestor
        this.tlObservacionesGestor = null;
        this.idDocumentoObservaciones = null;
        
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosObservaciones').hide();");

        // Si el objeto documentoSeleccionado es nulo, es que el popup de
        // observaciones no se ha llamado desde el popup de historico.
        // y no entra en el flujo del if. Caso contrario, el popup observaciones
        // se ha llamado desde el popup historico, y se volverá a abrir el popup
        // historico, despues de cerrar el de observaciones.
        if (null != this.documentoSeleccionado) {
            this.mostrarHistoricoDocumento(this.documentoSeleccionado);
        }
    }

    /**
     * Método para mostrar la ventana popup con la causa de denegación asociada
     * al documento
     *
     * @param documento Datos de documento
     */
    public void mostrarDenegacionDocumento(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        try {
            this.documentoSeleccionado = documento;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
            if (Boolean.FALSE.equals(documento.getEsDocumentoRequisito())) {
                this.documentoSeleccionado = this.expedienteDocService
                        .buscarId(documento.getIdExpedienteDoc());
                this.idCausaSubsanacion = this.documentoSeleccionado.getConvocatoriaCauDTO()
                        .getIdConvocatoriaCau();
                this.descripcionCausaSubsanacion = this.documentoSeleccionado
                        .getTlCausaDescripcion();
            }
            this.modoLecturaSubsanacion = Boolean.TRUE;
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumento').show();");
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Metodo que envía el resumen de documento de subsanación
     *
     */
    public void enviarColaNotificaciones(ConvocatoriaDTO convDTO) {
        try {

            // Recuperamos los datos del expediente
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);
            
            // Quitamos los registros de baja
            List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                    .filter(Objects::nonNull).collect(Collectors.toList());
            // Existe algún requisito registrado como qu eno cumple
            Boolean existeNoCumple = lstExpReq.stream()
                    .anyMatch(x -> Constantes.AfirmacionNegacion.N.equals(x.getItCumple()));
            if (Boolean.TRUE.equals(existeNoCumple) && expedienteDTO.getEstadoRevRequiDTO()
                    .getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO))) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITOS_FALTA_CREAR_DOCUMENTO_SUBSANACION_REQUISITOS);
            } else
            
            // MD-1979. Incuimos la validación de que el estado de revisión de requisitos no esté en el estado de pendiente de evisión
            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                    .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_REVISAR);
            } else {

                // Validamos si el expediente esta en estado pendiente
                // requerimiento
                if (!utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(expedienteDTO.getEstadoRevAdminDTO().getIdValor())) {
                    AtlasMessageUtils.addWarnMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
                } else {

                    // Quitamos los registros de baja
                    List<ExpedienteDocDTO> lstExpDoc = expedienteDTO.getLstExpedienteDocDTO()
                            .stream().filter(x -> x.getFcBajaFila() == null)
                            .collect(Collectors.toList());

                    // Obtenemos la lista de documentos asociados a los
                    // requerimientos
//                    List<Integer> lstDocReq = expedienteDTO.getLstExpedienteReqDTO().stream()
//                            .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
//                            .collect(Collectors.toList());

                    // Quitamos los registros documentos asociado a los
                    // requerimientos
                    // MD-1979. Ahora no se quitan los documentos asociados a los requerimientos
//                    lstExpDoc = lstExpDoc.stream()
//                            .filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
//                            .collect(Collectors.toList());

                    // Validamos si existe algún registro pendiente de revisión
                    Integer idPenRev = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
                    Boolean existePendienteRevision = lstExpDoc.stream()
                            .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));

                    if (Boolean.TRUE.equals(existePendienteRevision)) {
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.REVISION_CALIFICACION_DOCUMENTOS_INCOMPLETO);
                    } else {

                        // insertamos a la tabla SAII_DOCUMENTO_PTFR

                        Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
                                convDTO, expedienteDTO, idExpediente,
                                utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                                null, null, null, null);

                        if (insertado.equals(Boolean.TRUE)) {
                            this.actualizarValoresFormulario();
                            List<ExpedienteDoc> listaExpedienteDoc = documentoPtfrService
                                    .obtenerDocumentosAReportar(utilConstantes
                                            .getGlobalCatalogosValores()
                                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                                            expedienteDTO.getIdExpediente(), null, null);
                            
                            // MD-1979. Recuperamos los documentos con el estado requerimiento de subsanación de requisitos 402 
                            List<ExpedienteDoc> listaExpedienteDocReq = documentoPtfrService
                                    .obtenerDocumentosAReportar(utilConstantes
                                            .getGlobalCatalogosValores()
                                            .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                                            expedienteDTO.getIdExpediente(), null, null);

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
                                expedienteDoc.setEstado(new CatalogosValores(utilConstantes
                                        .getGlobalCatalogosValores()
                                        .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                                this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(false);
                                expedienteDocService.update(expedienteDoc);
                                expedienteDocHistService.crearHistoricoExpedienteDoc(expedienteDoc,
                                        null);
                                for (ExpedienteDocDTO expedienteDocuDTO : this.expedienteBean
                                        .getExpedienteDTO().getLstExpedienteDocDTO()) {
                                    if (expedienteDoc.getIdExpedienteDoc()
                                            .equals(expedienteDocuDTO.getIdExpedienteDoc())) {
                                        expedienteDocuDTO.getEstadoDTO().setIdValor(utilConstantes
                                                .getGlobalCatalogosValores()
                                                .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
                                        expedienteDocuDTO.getEstadoDTO().setNmConstante(
                                                Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
                                    }
                                }
                            }
                            
                            // MD-2096 Actualizamos los estados del expediente
                            expedienteDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes
                                    .getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                            expedienteDTO.setEstadoRevAdminDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                            if (expedienteDTO.getEstadoRevRequiDTO().getNmConstante()
                                    .equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)) {
                                expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                            }
                            this.expedienteService.guardar(expedienteDTO);
                            // Actualizamos el DTO para que se vea en el frontal
                            CatalogosValoresDTO pendienteSubsanacionRequisitos = this.catalogosValoresService
                                    .buscarId(expedienteDTO.getEstadoDTO().getIdValor());
                            this.expedienteBean.getExpedienteDTO()
                                    .setEstadoDTO(pendienteSubsanacionRequisitos);
                            this.expedienteBean.getExpedienteDTO()
                                    .setEstadoRevAdminDTO(pendienteSubsanacionRequisitos);
                            
                            AtlasMessageUtils.addInfoMessageFromBundle(
                                    ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                        } else {
                            AtlasMessageUtils.addErrorMessageFromBundle(
                                    ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                        }

                    }

                }

                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                PrimeFaces.current().ajax().update(this.formularioDocumentos);
                PrimeFaces.current().ajax().update(this.solapaDocumentos);
                PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
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
     * Metodo que envía el resumen de documento de subsanación
     *
     */
    public void enviarColaNotificaciones(ConvocatoriaDTO convDTO, UploadedFile archivoSubirSubsanacionADM ) {
       
        byte[] documentoAdjunto;
        try {
            
            if(archivoSubirSubsanacionADM == null) {
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }else {

                // Recuperamos los datos del expediente
                ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                        Boolean.FALSE);
                
                // Quitamos los registros de baja
                List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                        .filter(Objects::nonNull).collect(Collectors.toList());
                // Existe algún requisito registrado como qu eno cumple
                Boolean existeNoCumple = lstExpReq.stream()
                        .anyMatch(x -> Constantes.AfirmacionNegacion.N.equals(x.getItCumple()));
                if (Boolean.TRUE.equals(existeNoCumple) && expedienteDTO.getEstadoRevRequiDTO()
                        .getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO))) {
                    AtlasMessageUtils.addWarnMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_REQUISITOS_FALTA_CREAR_DOCUMENTO_SUBSANACION_REQUISITOS);
                } else
                
                if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                        .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                    AtlasMessageUtils.addWarnMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_REVISAR);
                } else if (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                        .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                    AtlasMessageUtils.addWarnMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
                } else {
                    
                    InputStream fileInputStream = this.archivoSubirSubsanacionADM.getInputstream();
                    
                    documentoAdjunto = IOUtils.toByteArray(fileInputStream);
        
                    // Validamos si el expediente esta en estado pendiente requerimiento
                    if (!utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO)
                            .equals(expedienteDTO.getEstadoRevAdminDTO().getIdValor())) {
                        AtlasMessageUtils.addWarnMessageFromBundle(
                                ConstantesWeb.Mensajes.REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
                    } else {
        
                        // Quitamos los registros de baja
                        List<ExpedienteDocDTO> lstExpDoc = expedienteDTO.getLstExpedienteDocDTO().stream()
                                .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
        
                        // Obtenemos la lista de documentos asociados a los
                        // requerimientos
                        List<Integer> lstDocReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                                .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                                .collect(Collectors.toList());
        
                        // Quitamos los registros documentos asociado a los
                        // requerimientos
                        lstExpDoc = lstExpDoc.stream()
                                .filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                                .collect(Collectors.toList());
        
                        // Validamos si existe algún registro pendiente de revisión
                        Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
                        Boolean existePendienteRevision = lstExpDoc.stream()
                                .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));
        
                        if (Boolean.TRUE.equals(existePendienteRevision)) {
                            AtlasMessageUtils.addErrorMessageFromBundle(
                                    ConstantesWeb.Err.REVISION_CALIFICACION_DOCUMENTOS_INCOMPLETO);
                        } else {
                            
                            
                           //insertamos a la tabla  SAII_DOCUMENTO_PTFR
                            
                          Boolean insertado =   documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente,  
                                  utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA), documentoAdjunto, null,null,null);
                            
                          if(insertado.equals(Boolean.TRUE)) {
                              this.actualizarValoresFormulario();
                              
                              List<ExpedienteDoc> listaExpedienteDoc = documentoPtfrService
                                      .obtenerDocumentosAReportar(utilConstantes
                                              .getGlobalCatalogosValores()
                                              .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                                              expedienteDTO.getIdExpediente(), null, null);
                              // MD-1979. Recuperamos los documentos con el estado requerimiento de subsanación de requisitos 402 
                              List<ExpedienteDoc> listaExpedienteDocReq = documentoPtfrService
                                      .obtenerDocumentosAReportar(utilConstantes
                                              .getGlobalCatalogosValores()
                                              .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                                              expedienteDTO.getIdExpediente(), null, null);
                              
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

                              // VMM 24/09/26 MD-2359: si algun documento a reportar se ha dejado sin
                              // tipo de documento asignado (el desplegable se queda mostrando el
                              // primer valor de la lista, "Otros", sin que el gestor haya llegado a
                              // seleccionar nada realmente), no se debe permitir el envio a
                              // Portafirmas - hay que especificar el tipo de documento antes de
                              // enviar la subsanacion.
                              Boolean faltaTipoDocumento = mapaUnicos.values().stream()
                                      .anyMatch(doc -> doc.getTipoDocumento() == null
                                              || doc.getTipoDocumento().getIdValor() == null);
                              if (Boolean.TRUE.equals(faltaTipoDocumento)) {
                                  AtlasMessageUtils.addErrorMessageFromBundle(
                                          ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TIPO_DOCUMENTO);
                                  return;
                              }

                              for (ExpedienteDoc expedienteDoc : mapaUnicos.values()) {
                                  expedienteDoc.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                                  this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(false);
                                  expedienteDocService.update(expedienteDoc);
                                  expedienteDocHistService.crearHistoricoExpedienteDoc(expedienteDoc, null);
                                  for (ExpedienteDocDTO expedienteDocuDTO : this.expedienteBean.getExpedienteDTO().getLstExpedienteDocDTO()) {
                                      if (expedienteDoc.getIdExpedienteDoc().equals(expedienteDocuDTO.getIdExpedienteDoc())) {
                                          expedienteDocuDTO.getEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
                                          expedienteDocuDTO.getEstadoDTO().setNmConstante(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
                                      }
                                  }
                              }
                              
                              // MD-2096 Actualizamos los estados del expediente
                              expedienteDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes
                                      .getGlobalCatalogosValores()
                                      .get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                              expedienteDTO.setEstadoRevAdminDTO(new CatalogosValoresDTO(
                                      utilConstantes.getGlobalCatalogosValores().get(
                                              Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                              expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(
                                      utilConstantes.getGlobalCatalogosValores().get(
                                              Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)));
                              this.expedienteService.guardar(expedienteDTO);
                              // Actualizamos el DTO para que se vea en el frontal
                              CatalogosValoresDTO pendienteSubsanacionRequisitos = this.catalogosValoresService
                                      .buscarId(expedienteDTO.getEstadoDTO().getIdValor());
                              this.expedienteBean.getExpedienteDTO()
                                      .setEstadoDTO(pendienteSubsanacionRequisitos);
                              this.expedienteBean.getExpedienteDTO()
                                      .setEstadoRevAdminDTO(pendienteSubsanacionRequisitos);
                              
                              AtlasMessageUtils.addInfoMessageFromBundle(
                                    ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                          }else {
                              AtlasMessageUtils.addErrorMessageFromBundle(
                                    ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                          }
                              
                        }
        
                    }
                    
                    PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                    PrimeFaces.current().ajax().update(this.formularioDocumentos);
                    PrimeFaces.current().ajax().update(this.solapaDocumentos);
                    PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
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
            AtlasMessageUtils
            .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
        }

    }
    
    
    
    
    
    /**
     * Metodo que envía el resumen de documento de subsanación
     *
     */
    public void enviarSubsanarContratosPortafirmas(ConvocatoriaDTO convDTO) {
        try {
            
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION);
            Boolean existePendienteSubsanar = this.expedienteBean.getLstExpedienteConContrato().stream()
                    .anyMatch(f -> f.getExpedienteContrato() != null && f.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor() != null && 
                        idReqSub.equals(f.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor()));
            List<ExpedienteDTO> lstContratoPendSub = this.expedienteBean.getLstExpedienteConContrato().stream()
                    .filter(f -> f.getExpedienteContrato() != null && f.getExpedienteContrato().getIdEstadoRevAdminDTO() != null 
                    && f.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor() != null && 
                    idReqSub.equals(f.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())).collect(Collectors.toList());
            
            // Validamos si el expediente esta en estado pendiente requerimiento

            if(Boolean.FALSE.equals(existePendienteSubsanar)){
                AtlasMessageUtils.addWarnMessageFromBundle(
                ConstantesWeb.Mensajes.REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
            
            }else {

                for(ExpedienteDTO expSubsan : lstContratoPendSub) {
                    Boolean subsanadoDoc = false;
                    //Quitamos docs dados de baja
                    List<ExpedienteDocDTO> lstExpDoc = expSubsan.getLstExpedienteDocDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
                    for(ExpedienteDocDTO expDocCont : lstExpDoc) {
                        if(Boolean.FALSE.equals(subsanadoDoc) &&
                                expDocCont.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION))) {
                            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(expSubsan.getIdExpediente(),
                                    Boolean.FALSE);
                            //insertamos a la tabla  SAII_ENVIO_NOTIFIC_PORTAFIRMAS
                            Boolean insertado;
                            if(expSubsan.getExpedienteContrato().getIdTipoContratoDTO() != null && 
                                    expSubsan.getExpedienteContrato().getIdTipoContratoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_SUSTITUCION))) {
                                insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, expSubsan.getIdExpediente(), 
//                                        Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION, null,null,null);
                                        //POM Modificado temporalmente pruebaS Luis
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL), null,null,null,null);
                                if(Boolean.TRUE.equals(insertado))log.debug("insertado");//sonar borrar
                            }else {
                                insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, expSubsan.getIdExpediente(), 
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL), null,null,null,null);
                            }

                            if(insertado.equals(Boolean.TRUE)) {
                                expDocCont.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                                this.expedienteDocService.guardarConHistorico(expDocCont);

                                ExpedienteContratoDTO expContrato = this.expedienteContratoService.cambiarEstadoContrato(expSubsan.getExpedienteContrato(), expSubsan.getIdExpediente(), 
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION), 
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
                                
                                expContrato.setIdEstadoRevAdminDTO(catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES)));
                                this.getExpedientePartiDTO().setExpedienteContrato(expContrato);
                                
                    
                                subsanadoDoc = true;
                            
                            }else {
                                AtlasMessageUtils.addErrorMessageFromBundle(
                                        ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                            }
                        }
                    }
                    
                    //Si no hubiera documentos que subsanar enviamos a subsanar el expediente contrato
                    if(Boolean.FALSE.equals(subsanadoDoc)) {
                        ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(expSubsan.getIdExpediente(),
                                Boolean.FALSE);
                        //insertamos a la tabla  SAII_ENVIO_NOTIFIC_PORTAFIRMAS
                        Boolean insertado;
                        if(expSubsan.getExpedienteContrato().getIdTipoContratoDTO() != null && 
                                expSubsan.getExpedienteContrato().getIdTipoContratoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_SUSTITUCION))) {
                            insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, expSubsan.getIdExpediente(), 
//                                    Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION, null,null,null);
                                    //POM Modificado temporalmente pruebaS Luis
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL), null,null,null,null);
                            if(Boolean.TRUE.equals(insertado))log.debug("insertado");//sonar borrar
                        }else {
                            insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, expSubsan.getIdExpediente(), 
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL), null,null,null,null);
                        }

                        if(insertado.equals(Boolean.TRUE)) {
                            ExpedienteContratoDTO expContrato = this.expedienteContratoService.cambiarEstadoContrato(expSubsan.getExpedienteContrato(), expSubsan.getIdExpediente(), 
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION), 
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
                            
                            expContrato.setIdEstadoRevAdminDTO(catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES)));
                            this.getExpedientePartiDTO().setExpedienteContrato(expContrato);
                
                            subsanadoDoc = true;
                        
                        }else {
                            AtlasMessageUtils.addErrorMessageFromBundle(
                                    ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                        }
                    }

                }

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
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
     * Método descargar un PDF con el formulario de subsanacion a enviar a
     * portafirmas
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void descargarBorradorSubsanarPortafirmas(ConvocatoriaDTO convDTO) {

        try {

            byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                    convDTO.getIdConvocatoria(), 
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                    this.idExpediente,null);

            StringBuilder nombreArchivoPdf = new StringBuilder();
            nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
            nombreArchivoPdf.append("_");
            nombreArchivoPdf
                    .append("Requerimiento de Subsanación - Solicitud - Revisión administrativa");
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("BORRADOR");

            UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Metodo que envía el resumen de documento de desistimiento
     *
     */
    public void enviarDesistirPortafirmas(ConvocatoriaDTO convDTO) {
        try {
            
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);
            
         // MD-2148 quitamos el desistimiento
//            Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente,  
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), null, null,null,null); 
         // MD-2148 ponemos desisitmiento provisional
            
             Integer tramite;
            
            if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL);
            }else {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO);
            }
            
            
            
            Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                    expedienteDTO, idExpediente,
                    tramite,
                    null, null, null, null);
            
            if(insertado.equals(Boolean.TRUE)) {
             // MD-2148 quitamos las actualizaciones del totoal del formulario
//                this.actualizarValoresFormulario();
             // MD-2148 sólo actualizamos el cambio del estado de la rev admin a desistido provisional
                
                Integer estado;
                
                if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                    estado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);
                }else {
                    estado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO);
                }
                             
                ExpedienteDTO expeDTO = this.expedienteService.guardarCambioEstadoRevAdmin(
                        this.idExpediente, estado);
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
                }
                
                
                AtlasMessageUtils.addInfoMessageFromBundle(
                      ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            }else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                      ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
            }
                  

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        } 

    }
    
    
    
 public void elegirDesistimiento(Boolean provisional,ConvocatoriaDTO convDTO ) {
        
        if(provisional != null && provisional.equals(Boolean.TRUE)) {
            nuevoDesistimientoDialog();
        }else if(provisional != null && provisional.equals(Boolean.FALSE)){
           desistirDefinitivo(convDTO);
        }
        
        
    }
  
    
    public void desistirDefinitivo(ConvocatoriaDTO convDTO) {
        try {

                Integer estado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO);
            
                ExpedienteDTO expeDTO = this.expedienteService.guardarCambioEstadoRevAdmin(
                        this.idExpediente, estado);
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
                }
                
                
                AtlasMessageUtils.addInfoMessageFromBundle(
                      ConstantesWeb.Mensajes.DESISTIDO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DESISTIDO_DEFINITIVO_KO);
            }
        } 

    }
    
    
    
    public void enviarDesistirContratoPortafirmas(ConvocatoriaDTO convDTO) {
        try {


            List<ExpedienteDTO> lstContratoDesistir = this.expedienteBean.getLstExpedienteConContrato();

            if(lstContratoDesistir == null || lstContratoDesistir.isEmpty()){
                AtlasMessageUtils.addWarnMessageFromBundle(
                ConstantesWeb.Mensajes.REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
            
            }else {
                if(this.expedientePartiDTO != null && this.expedientePartiDTO.getIdExpediente() != null
                        && this.expedientePartiDTO.getExpedienteContrato() != null && this.expedientePartiDTO.getExpedienteContrato().getIdExpedienteContrato() != null) {
                    
                    if (StringUtils.isBlank(this.tlObservacionesParaCiu)) {
                        AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_VALIDAR_OBS_PARA_CIU);
                        return;
                    }else if(StringUtils.isNotBlank(this.tlObservacionesParaCiu) && this.expedientePartiDTO.getExpedienteContrato() != null) {
                        this.expedientePartiDTO.getExpedienteContrato().setTlObservaciParaCiudadano(this.tlObservacionesParaCiu);
                    }
                    ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.expedientePartiDTO.getIdExpediente(),
                            Boolean.FALSE);
                    //insertamos a la tabla  SAII_ENVIO_NOTIFIC_PORTAFIRMAS
                    Boolean insertado =  this.documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, expedienteDTO.getIdExpediente(), 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), null,null,null,null);
                    if(insertado.equals(Boolean.TRUE)) {

                        ExpedienteContratoDTO expContrato = this.expedienteContratoService.cambiarEstadoContrato(this.expedientePartiDTO.getExpedienteContrato(), this.expedientePartiDTO.getIdExpediente(), 
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO), 
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
                        expContrato.setIdEstadoRevAdminDTO(catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES)));
                        this.getExpedientePartiDTO().setExpedienteContrato(expContrato);
                        PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
            
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);                    
                    }else {
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                    }
                }
                
            }
            
            
        } catch (ServiceException e) {
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
     * Metodo que envía el resumen de documento de desistimiento
     *
     */
    public void enviarDesistirPortafirmas(ConvocatoriaDTO convDTO, UploadedFile archivoSubirDesistimiento) {
        byte[] documentoAdjunto;
        try {
            
            if(archivoSubirDesistimiento == null) {
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FALTA_DOC_EXCLUSION);
            }else {
                
                InputStream fileInputStream = this.archivoSubirDesistimiento.getInputstream();
                
                documentoAdjunto = IOUtils.toByteArray(fileInputStream);
                
                
                ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                        Boolean.FALSE);
                Integer idTipoDocumento;
                Integer idTipoDoc;
                
                if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                    // MD-2422 Creamos el expedientedoc
                     idTipoDocumento = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_DESISTIDA);
                     idTipoDoc = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL);
                }else {
                    // MD-2422 Creamos el expedientedoc
                     idTipoDocumento = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
                     idTipoDoc = utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.DESISTIMIENTO);
                }

                this.prepararMetadatosFica(archivoSubirDesistimiento, idTipoDocumento, idTipoDoc);
                this.expedienteDocDTO = this.expedienteDocService
                        .subirDocumentoFica(this.expedienteDocDTO);
                this.expedienteDocDTO = this.expedienteDocService.guardar(this.expedienteDocDTO);
                
             // MD-2148 quitamos el desistimiento
//                Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente,  
//                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), documentoAdjunto, null,null,null); //Constantes.Tramite.DESISTIMIENTO
             // MD-2148 incluimos la inserción del desistimiento provisional
                
                

                Integer tramite;
               
               if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                   tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL);
               }else {
                   tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO);
               }
                
                
                Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                        expedienteDTO, idExpediente,tramite,
                        documentoAdjunto, null, null, null);
                
                if(insertado.equals(Boolean.TRUE)) {
                    
                    
                    Integer tramiteNm;
                    
                    if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                        tramiteNm = Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL;
                    }else {
                        tramiteNm = Constantes.Tramite.DESISTIMIENTO;
                    }
                    
                    // MD-2422 Recuperamos el documento y registramos el idDocumentoPtfr
                    List<DocumentoPtfrDTO> docPtfrDto = this.documentoPtfrService
                            .buscarPorExpedienteTramite(idExpediente,
                                    tramiteNm);
                    this.expedienteDocDTO.setIdDocumentoPtfr(docPtfrDto.get(0).getIdDocumentoPtfr());
                    this.expedienteDocService.guardar(this.expedienteDocDTO);
                    
                 // MD-2148 quitamos las actualizacion del os valores del formualrio
//                    this.actualizarValoresFormulario();
                 // MD-2148 añadimos el guardado sólo del cambio de estado de la rev adm
//                    this.expedienteService.guardarCambioEstadoRevAdmin(
//                            this.idExpediente, utilConstantes.getGlobalCatalogosValores()
//                                    .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
                    
                    // MD-2422 Actualizamos el estado provisional
//                    ExpedienteDTO expeDTO = this.expedienteService.actualizarEstadoProvisionalExpediente(
//                            idExpediente, Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
//                            UserUtils.getUserName().toUpperCase());
                    
                    // MD-2422 Actualizamos el estado provisional
                    
                    Integer estado;
                    
                    if(Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())) {
                        estado = Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL;
                    }else {
                        estado = Constantes.EstadoExpediente.DESISTIDO;
                    }
                    
                    ExpedienteDTO expeDTO = this.expedienteService
                            .actualizarCambioEstEstRevAdmEstPro(idExpediente,
                                    estado,
                                    UserUtils.getUserName().toUpperCase());
                    

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
                    }
                    
                    
                    AtlasMessageUtils.addInfoMessageFromBundle(
                          ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                }else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                          ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                }
            }
            

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
            }
        } catch (IOException e) {
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
     * Método descargar un PDF con el formulario de desistimiento a enviar a
     * portafirmas
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void descargarBorradorDesistirPortafirmas(ConvocatoriaDTO convDTO) {
        try {

            // VMM 22/10/26 MD-3169: el borrador usa la misma plantilla que el envio a portafirmas: la de
            // resolucion de desistimiento provisional (418) si la convocatoria tiene el check de resolucion
            // provisional de desistimiento y la de desistimiento (411) si no
            Integer tramiteBorrador = Boolean.TRUE.equals(convDTO.getItResProvDesistimiento())
                    ? utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL)
                    : utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO);

            byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                    convDTO.getIdConvocatoria(), tramiteBorrador,
                    this.idExpediente,null);

            StringBuilder nombreArchivoPdf = new StringBuilder();
            nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("Desistimiento de un expediente");
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("BORRADOR");

            UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Método que finaliza la revisión del expediente cuando todos los
     * documentos administrativos estan con estado REVISION OK
     */
    public void finalizarRevisionOk() {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteBean.getExpedienteDTO();
            // Quitamos los registros de baja
            List<ExpedienteReqDTO> lstExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                    .filter(Objects::nonNull).collect(Collectors.toList());
            // Existe algún requisito registrado como qu eno cumple
            Boolean existeNoCumple = lstExpReq.stream()
                    .anyMatch(x -> Constantes.AfirmacionNegacion.N.equals(x.getItCumple()));

            if (Boolean.TRUE.equals(existeNoCumple) && expedienteDTO.getEstadoRevRequiDTO()
                    .getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO))) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITOS_FALTA_CREAR_DOCUMENTO_SUBSANACION_REQUISITOS);
            } else if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION)
                    .equals(expedienteDTO.getEstadoRevRequiDTO().getIdValor())) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_REQUISITO_FALTA_REVISAR_REQUISITOS);
            } else {

                ExpedienteDTO dto = this.expedienteService
                        .actualizarEstadoInternoRevisionOkDocumento(this.idExpediente,
                                expedienteDTO);

//                if (dto != null && utilConstantes.getGlobalCatalogosValores()
//                        .get(Constantes.EstadoExpediente.REVISION_OK)
//                        .equals(dto.getEstadoRevRequiDTO().getIdValor())) {
                if (dto != null && utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.REVISION_OK)
                        .equals(dto.getEstadoRevAdminDTO().getIdValor())) {
                    this.actualizarValoresFormulario();
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);

                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
                }
//                } else {
//                    AtlasMessageUtils.addErrorMessageFromBundle(
//                            ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
//                }
            }
        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
        }

    }
    
    /**
     * Método que finaliza la revisión de expediente con contratos
     * @throws ServiceException 
     */
    public void finalizarRevisionExpContratosOk() throws ServiceException {
        
        try {

            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoInternoRevisionExpContratosOK(this.idExpediente);

            if (dto != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)
                    .equals(dto.getEstadoRevContratoDTO().getIdValor())) {
                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
            }
        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
        }
       
    }
    
    /**
     * Método que finaliza la revisión de los contratos
     * @throws ServiceException 
     */
    public void finalizarRevisionContratosOk(ExpedienteContratoDTO expDTO) throws ServiceException {
        
        try {
            
            Boolean contratoValidado = this.validarDatosContrato(this.expedientePartiDTO.getExpedienteContrato(), this.expedientePartiDTO.getEstadoRevision());
            if(Boolean.FALSE.equals(contratoValidado)) {
                //Si no ha sido validado devolvemos el error
                return;
            }
            if(this.tlObservacionesParaCiu != null && !this.tlObservacionesParaCiu.isEmpty()
                    && this.expedientePartiDTO.getExpedienteContrato() != null) {
                this.expedientePartiDTO.getExpedienteContrato().setTlObservaciParaCiudadano(this.tlObservacionesParaCiu);
            }
            ExpedienteContratoDTO expContratoDTO = this.expedienteService
                    .actualizarRevisionContratos(this.expedientePartiDTO.getExpedienteContrato(), this.expedientePartiDTO.getEstadoRevision());
 
            if(expContratoDTO != null) {
                expContratoDTO.setIdEstadoDTO(this.catalogosValoresService.buscarId(expContratoDTO.getIdEstadoDTO().getIdValor()));
                expContratoDTO.setIdEstadoRevAdminDTO(this.catalogosValoresService.buscarId(expContratoDTO.getIdEstadoRevAdminDTO().getIdValor()));
                this.expedientePartiDTO.setExpedienteContrato(expContratoDTO);
                this.expedientePartiDTO.setEstadoRevision(null);
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);
            }else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
            }
        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
        }
       
    }

    private Boolean validarDatosContrato(ExpedienteContratoDTO expedienteContrato, String estadoRevision) throws ServiceException {
        Boolean valido = Boolean.TRUE;
        ConvocatoriaDTO convDto = this.obtenerConvocatoriaCacheada();
        
        if(expedienteContrato.getItIndefinido() != null && 
                expedienteContrato.getItIndefinido().equals(Constantes.AfirmacionNegacion.S)) {
            //Si es contrato indefinido, no debe tener fecha fin
            expedienteContrato.setFcFinContrato(null);
        }else if(expedienteContrato.getItIndefinido() != null && 
                expedienteContrato.getItIndefinido().equals(Constantes.AfirmacionNegacion.N)
                && expedienteContrato.getFcFinContrato() == null
                && !estadoRevision.contentEquals(Constantes.RevisionContratos.SUBSANAR)) {
            //Si NO es contrato indefinido, SI debe tener fecha fin
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_CONTRATO_FECHA_FIN_KO);
            valido = Boolean.FALSE;
            return valido;
        }
        if(expedienteContrato.getFcIniContrato() != null && expedienteContrato.getFcFinContrato() != null
                && expedienteContrato.getFcIniContrato().after(expedienteContrato.getFcFinContrato())
                && !estadoRevision.contentEquals(Constantes.RevisionContratos.SUBSANAR)){
            //La fecha fin de contrato debe ser posterior a la fecha inicio
            valido = Boolean.FALSE;
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_CONTRATO_FECHA_FIN_KO);
            return valido;
        }
        if(convDto.getFcTraPropResDef() != null && expedienteContrato.getFcIniContrato().before(convDto.getFcTraPropResDef()) 
                && !estadoRevision.contentEquals(Constantes.RevisionContratos.SUBSANAR)) {
            //La fecha de inicio de contrato no puede ser anterior a la Fecha propuesta resolución definitiva
            valido = Boolean.FALSE;
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_CONTRATO_FECHA_INICIO_KO);
            return valido;
        }
        if(estadoRevision != null 
                && (estadoRevision.contentEquals(Constantes.RevisionContratos.SUBSANAR) || estadoRevision.contentEquals(Constantes.RevisionContratos.KO))
                && (this.getTlObservacionesParaCiu() == null || this.getTlObservacionesParaCiu().isEmpty())){
            //Si se ha seleccionado KO/Subsanar se requieren observaciones para ciudadano
            valido = Boolean.FALSE;
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_CONTRATO_OBSERVACIONES_CIUDADANO_KO);
            return valido;
        }else if(estadoRevision != null 
                && (estadoRevision.contentEquals(Constantes.RevisionContratos.SUBSANAR) || estadoRevision.contentEquals(Constantes.RevisionContratos.KO))) {
            expedienteContrato.setTlObservaciParaCiudadano(this.getTlObservacionesParaCiu());
            this.expedientePartiDTO.setExpedienteContrato(expedienteContrato);
        }

        return valido;
    }

    /**
     * Método para cargar el catálogo de tipos de documentos en el combo de la
     * celda editable
     */
    public List<CatalogosValoresDTO> getObtenerListaAltaTipoDocumento() {
        List<CatalogosValoresDTO> listadoAltaTipoDocumento = new ArrayList<>();

        this.filtrosValores = new ArrayList<>();
        try {

            // Los documentos no asignables no deben aparecer en esta tabla
            this.filtrosValores.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_MANTENIBLE,
                    Constantes.AfirmacionNegacion.S, Operador.EQUALS));
            this.filtrosValores.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
                    utilConstantes.getGlobalCatalogos().get(Constantes.Cata.DOCUMENTACION_SOLICITUD), Operador.EQUALS));
            this.filtrosValores
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            listadoAltaTipoDocumento = this.catalogosValoresService
                    .obtenerPorFiltros(this.filtrosValores);

        } catch (ServiceException exc) {
            log.error(AtlasMessageUtils
                    .getStringFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS));
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
        }
        return listadoAltaTipoDocumento;
    }

    /**
     * Método que se ejecuta al seleccionar una opcion en el comboBox y devuelve
     * el valor seleccionado, se usa para recuperar el tipoDocumento nuevo.
     *
     * @param event
     */

    public void changeListener(ValueChangeEvent event) throws ServiceException {
        if (!Boolean.TRUE.equals(event.getNewValue() == event.getOldValue())) {
            try {
                this.tipoDocumento = this.catalogosValoresService
                        .buscarId(Integer.valueOf(event.getNewValue().toString()));

            } catch (NumberFormatException | ServiceException e) {
                log.error(e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Método que se ejecuta al seleccionar una opcion en el comboBox y
     * modificamos el tipoDocumento del expedienteDoc. Creamos tambien el
     * historico del cambio
     *
     * @param item Integer con el idExpedienteDoc
     */
    public void cambiarTipoDocumento(Integer item) throws ServiceException {
        if (!Boolean.TRUE.equals(null == item)) {
            try {
                this.expedienteDocDTO = this.expedienteDocService.buscarId(item);
                this.expedienteDocDTO.setTipoDocumentoDTO(this.tipoDocumento);

                if (this.validarSeleccionDoc(this.expedienteDocDTO).equals(Boolean.FALSE)) {
                    this.expedienteDocDTO = this.expedienteDocService
                            .guardar(this.expedienteDocDTO);
                    this.expedienteDocHistService.crearHistorico(this.expedienteDocDTO,
                            UserUtils.getUserName().toUpperCase());
                } else {
                    AtlasMessageUtils
                            .addWarnMessageFromBundle(ConstantesWeb.Mensajes.TIPO_DOC_DUPLICADO);
                }
                
                this.actualizarValoresFormulario();
            } catch (ServiceException e) {
                log.error(e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Metodo para descargar documento de informe a PTFR
     *
     * @param dto
     */
    public void descargarDocumentoInformePtfr(Integer idExpediente, Boolean esSubsanacion) {

        try {
            List<ExpedienteDocDTO> lista = this.expedienteDocService
                    .buscarPorExpediente(idExpediente);

            DocumentoPtfrDTO opDoc = null;

            for (ExpedienteDocDTO expDTO : lista) {

                if (expDTO.getIdDocumentoPtfr() != null) {

                    DocumentoPtfrDTO ptfrDTO = this.documentoPtfrService
                            .buscarPorId(expDTO.getIdDocumentoPtfr());

                    if (Boolean.TRUE.equals(esSubsanacion)
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                                    .equals(ptfrDTO.getTramiteDTO().getIdValor())
                            || Boolean.FALSE.equals(esSubsanacion)
                                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO)
                                            .equals(ptfrDTO.getTramiteDTO().getIdValor())) {

                        opDoc = ptfrDTO;
                        break;

                    }

                }

            }

            if (opDoc != null) {

                PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService
                        .recuperarDocumentoFirmado(opDoc.getDsReferenciaPtfr(),
                                String.valueOf(opDoc.getCdArchivoPtfr()));

                if (recuperado != null) {
                    UtilesWeb.openFile(Base64.getDecoder().decode(recuperado.getPdfB64()),
                            Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION,
                            Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_INFORME);
                }

            }
        } catch (

        ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * Metodo para validar si se muestra el boton de agregar documento externo
     *
     * @return
     */
    public Boolean mostrarBotonAgregarDocumentoExterno(Boolean esModoConsulta) {
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        // MD-2423 Incluimos los estados desisitido y excluidos provisionales
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));

        return Boolean.FALSE.equals(esModoConsulta)
                && !lstEstadoExpInter.contains(
                        this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor())
                && !lstEstadoExp.contains(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }
    
    /**
     * Metodo para validar si se muestra el boton de agregar documento externo
     *
     * @return
     */
    public Boolean deshabilitarBotonAgregarDocumentoExterno(Boolean esModoConsulta) {

        Boolean deshabilitar = Boolean.TRUE;

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));

        if(Boolean.FALSE.equals(esModoConsulta) && lstEstadoExp.contains(this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor()) ) {
            deshabilitar = Boolean.FALSE;
        }

        // VMM 02/09/26 MD-2996: Rechazada por la entidad solicitante: al igual que en
        // habilitarBotonSubirDocumento, no se permite agregar/requerir nuevos documentos.
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            deshabilitar = Boolean.FALSE;
        }

        return deshabilitar;

    }

    /**
     * VMM 02/09/26 MD-3010: Metodo para validar si ya se ha enviado un requerimiento de
     * subsanación de requisitos (el expediente queda en estadoRevAdminDTO = Pendiente
     * subsanación de requisitos). Se usa para deshabilitar únicamente el botón "Nuevo Doc a
     * Requerir" mientras esa subsanación esté en curso, sin afectar al botón "Agregar".
     *
     * @return
     */
    public boolean isPendienteSubsanacionRequisitos() {
        return utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor());
    }
    
    

    /**
     * Metodo para validar si se habilita el boton de finalizar revisión
     * administrativa
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonFinalizarRevision(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();

        if (Objects.isNull(this.listadoDocumentosCompleto)) {
            listado  = busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);
            this.listadoDocumentosCompleto = listado.getResultados();
        }

        if (this.listadoDocumentosCompleto != null && !this.listadoDocumentosCompleto.isEmpty()) {

            ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

            ConvocatoriaDTO conv = this.obtenerConvocatoriaCacheada();
            this.comprobarGestoresExcep(conv);

            //VMM 05/06/25 MD-1629: ponemos como pageSize -1 para que coja todos los documentos de la lista y no solo los 10 primeros
    //        AtlasListAndCount<ExpedienteDocDTO> listadoAtlas = this.listadoDocumentosCompleto; // busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);

            List<ExpedienteDocDTO> lstExpDoc =  this.listadoDocumentosCompleto;

            // Quitamos los registros de baja
            lstExpDoc = lstExpDoc.stream().filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
    
            // Obtenemos la lista de documentos asociados a los requerimientos
            List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
                    .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                    .collect(Collectors.toList());
    
            // Quitamos los registros documentos asociado a los requerimientos
            lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                    .collect(Collectors.toList());
    
            // Quitamos los  documentos que no sean de tipo tramite presentacion de solicitud 400
            Integer idPres = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION);
            Integer idReqSubAdm = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);
            Integer idReqSubReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
            lstExpDoc = lstExpDoc.stream()
                    .filter(x -> (x.getTramiteDTO().getIdValor() != null
                    && (x.getTramiteDTO().getIdValor().equals(idPres)
                            || x.getTramiteDTO().getIdValor().equals(idReqSubAdm)
                            || x.getTramiteDTO().getIdValor().equals(idReqSubReq))))
                    .collect(Collectors.toList());
            
            // POM 16/08/24 MD-547: incluir estado Sin subir (no puede llegar a este punto un Sin Subir con Obligatoriedad = 'S')
            // Validamos si todos los documentos administrativos fueron calificados
            // como Revisión OK 
            Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
            Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
            //MD-2866: Error al consultar las solicitudes iniciadas
            Boolean pendienteRevisadoOk = lstExpDoc.stream().allMatch(
                    f -> f.getEstadoDTO() != null && f.getEstadoDTO().getIdValor() != null &&
                         (idRevOk.equals(f.getEstadoDTO().getIdValor()) ||
                          (idSinSub.equals(f.getEstadoDTO().getIdValor()) &&
                           ( (f.getBloqueComponenteDTO() != null && !Boolean.TRUE.equals(f.getBloqueComponenteDTO().getItObligatorio())) ||
                             (f.getBloqueComponenteDTO() != null && Boolean.TRUE.equals(f.getBloqueComponenteDTO().getItObligatorio()) 
                             && "S".equals(f.getItAutoriza()) ) || (f.getBloqueComponenteDTO() == null)
                           )
                          )
                         )
                );
            
            lstExpDoc =  this.listadoDocumentosCompleto;

            //MD-1979. Incluimos validación de requisitos para activar el botón
         // Quitamos los registros de baja
            lstExpDoc = lstExpDoc.stream().filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
            
         // Obtenemos la lista de documentos asociados a los requerimientos
            List<Integer> lstDocRequi = dto.getLstExpedienteReqDTO().stream()
                    .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
         // Dejamos solo los registros documentos asociado a los requerimientos
            lstExpDoc = lstExpDoc.stream().filter(x -> lstDocRequi.contains(x.getIdExpedienteDoc()))
                    .collect(Collectors.toList());
            
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Integer idColaNotificaciones = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
            Integer idRequiereSubsanacionNotificado = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
            Integer idEnviadoPortafirmas = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.ENVIADO_A_PTFR);
            Integer pendienteRevisionSubsanacion = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
            Integer revisionSubsanacionKO = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.REVISION_KO);
            Boolean requiereSubsanacionReq = Boolean.FALSE;
            if (lstExpDoc.stream()
                    .anyMatch(f -> (idReqSub.equals(f.getEstadoDTO().getIdValor())
                            || idColaNotificaciones.equals(f.getEstadoDTO().getIdValor())
                            || idRequiereSubsanacionNotificado.equals(f.getEstadoDTO().getIdValor())
                            || idEnviadoPortafirmas.equals(f.getEstadoDTO().getIdValor())
                            || pendienteRevisionSubsanacion.equals(f.getEstadoDTO().getIdValor())
                            || revisionSubsanacionKO.equals(f.getEstadoDTO().getIdValor())))) {
                requiereSubsanacionReq = Boolean.TRUE;
            }
    
            // Lista de estados finales donde no se permite finalizar revisión
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
    
            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            // Rechazada por la entidad solicitante: solo modo consulta, no se permite finalizar revisión
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));

            if (Boolean.FALSE.equals(esModoConsulta)
                    && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                    && !lstEstadoExpInter.contains(dto.getEstadoRevAdminDTO().getIdValor())
                    && Boolean.TRUE.equals(pendienteRevisadoOk)
                    && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                    && !conv.isEsGestorJCT()
                    && Boolean.FALSE.equals(requiereSubsanacionReq)) {
                habilitar = Boolean.TRUE;
            }
        }
        return habilitar;
    }
    
    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion administrativa
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonEnviarSubanasarPortafirmas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;

        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();

        if (Objects.isNull(this.listadoDocumentosCompleto)) {
            listado  = busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);
            this.listadoDocumentosCompleto = listado.getResultados();
        }

        if (this.listadoDocumentosCompleto != null && !this.listadoDocumentosCompleto.isEmpty()) {

            ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

            ConvocatoriaDTO conv = this.obtenerConvocatoriaCacheada();
            this.comprobarGestoresExcep(conv);

            // actualizamos la validacion
            this.calcularExisteEnvioPortafirmas(dto);

            //VMM 05/06/25 MD-1629: ponemos como pageSize -1 para que coja todos los documentos de la lista y no solo los 10 primeros
            //AtlasListAndCount<ExpedienteDocDTO> listadoAtlas =  this.listadoDocumentosCompleto;//busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);

            List<ExpedienteDocDTO> lstExpDoc =  this.listadoDocumentosCompleto;

            // Quitamos los registros de baja
            lstExpDoc = lstExpDoc.stream().filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
            
            // Obtenemos la lista de documentos asociados a los requerimientos
//            List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
//                    .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
//                    .collect(Collectors.toList());
    
            // Quitamos los registros documentos asociado a los requerimientos
//            lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
//                    .collect(Collectors.toList());
            
            // Quitamos los  documentos que no sean de tipo tramite presentacion de solicitud 400
            Integer idPres = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION);
            Integer idReqSubAdm = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);
            Integer idReqSubReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
            lstExpDoc = lstExpDoc.stream()
                    .filter(x -> (x.getTramiteDTO().getIdValor() != null
                    && (x.getTramiteDTO().getIdValor().equals(idPres)
                            || x.getTramiteDTO().getIdValor().equals(idReqSubAdm)
                            || x.getTramiteDTO().getIdValor().equals(idReqSubReq))))
                    .collect(Collectors.toList());

            // Validamos si hay documentos administrativos en estado PENDIENTE
            // SUBSANACION
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Boolean existeRequiereSubsanacion = lstExpDoc.stream()
                    .anyMatch(f -> idReqSub.equals(f.getEstadoDTO().getIdValor()));
            
            // Validamos si hay documentos pendientes de revision
            Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION); 
            Boolean existenPendientesRevision = lstExpDoc.stream()
                    .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));
            
            // Lista de estados finales donde no se permite enviar a portafirmas
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
    
            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            // Rechazada por la entidad solicitante: solo modo consulta, no se permite enviar a portafirmas subsanación
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));

            Boolean existeDesistimientoEnviado = this.documentoPtfrService
                    .consultarDocumentoPortafirmaPorTramite(dto.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));

         // MD-2148 determinamos si está en el estado desistido provisional la rev adm
            Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                    .getEstadoRevAdminDTO().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
            
            // MD-2147 está en estado escluido provisional la rev req
            Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                    .getEstadoRevRequiDTO().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
            
            if (Boolean.FALSE.equals(esModoConsulta)
                    && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrSubsanacionFirmado)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoEnProceso)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoFirmado)
                    && !lstEstadoExpInter.contains(dto.getEstadoRevAdminDTO().getIdValor())
                    && Boolean.TRUE.equals(existeRequiereSubsanacion)
                    && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                    && Boolean.FALSE.equals(existenPendientesRevision)
                    && !conv.isEsGestorJCT()
                    && !conv.isEsGestorConcesion()
                    && Boolean.FALSE.equals(existeDesistimientoEnviado)
                    && Boolean.FALSE.equals(excluidoProvisionalEnviado) // MD-2147 incluir condicion desisitido provisional
                    && Boolean.FALSE.equals(desisitidoProvisonalEnviado) // MD-2148 incluimos el estado desistido provisional
                    && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2958 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.TRUE;
            }
        }
        return habilitar;
    }

    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * desistimiento
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonEnviarDesistirPortafirmas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        AtlasListAndCount<ExpedienteDocDTO> listado = new AtlasListAndCount<>();

        if (Objects.isNull(this.listadoDocumentosCompleto)) {
            listado  = busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);
            this.listadoDocumentosCompleto = listado.getResultados();
        }

        if (this.listadoDocumentosCompleto != null && !this.listadoDocumentosCompleto.isEmpty()) {

            ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

            ConvocatoriaDTO conv = this.obtenerConvocatoriaCacheada();
            this.comprobarGestoresExcep(conv);

            // actualizamos la validacion
            this.calcularExisteEnvioPortafirmas(dto);

            //VMM 05/06/25 MD-1629: ponemos como pageSize -1 para que coja todos los documentos de la lista y no solo los 10 primeros
            //AtlasListAndCount<ExpedienteDocDTO> listadoAtlas = this.listadoDocumentosCompleto; //busquedaDocumentosSolicitud(this.idExpediente, null, -1, 0);

            List<ExpedienteDocDTO> lstExpDoc =  this.listadoDocumentosCompleto;

            // Quitamos los registros de baja
            lstExpDoc = lstExpDoc.stream().filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());
    
            // Obtenemos la lista de documentos asociados a los requerimientos
            List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
                    .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                    .collect(Collectors.toList());
    
            // Quitamos los registros documentos asociado a los requerimientos
            lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                    .collect(Collectors.toList());
    
            // Lista de estados finales donde no se permite enviar a portafirmas
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
    
            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            // Rechazada por la entidad solicitante: solo modo consulta, no se permite enviar a portafirmas desistimiento
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));

            // Validamos si hay documentos pendientes de revision
            Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            Boolean existenPendientesRevision = lstExpDoc.stream()
                    .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));
            
            Boolean existeDesistimientoEnviado = this.documentoPtfrService
                    .consultarDocumentoPortafirmaPorTramite(dto.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));
            
            // MD-2147 está en estado escluido provisional la rev req
            Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                    .getEstadoRevRequiDTO().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
            
            // MD-2148 determinamos si se encuantra en desistido provisional el estado de la rev admin
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
                    && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoEnProceso)
                    && Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoFirmado)
                    && !lstEstadoExpInter.contains(dto.getEstadoRevAdminDTO().getIdValor())
                    && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                    && Boolean.FALSE.equals(existenPendientesRevision)
                    && !conv.isEsGestorJCT()
                    && !conv.isEsGestorConcesion()
                    && Boolean.FALSE.equals(existeDesistimientoEnviado)
                    && Boolean.FALSE.equals(excluidoProvisionalEnviado) // MD-2147 incluir condicion desisitido provisional
                    && Boolean.FALSE.equals(desisitidoProvisonalEnviado) // MD-2148 incluimos la condicion desisitido provisional
                    && Boolean.FALSE.equals(existeDesistimientoExclusionProvisionalEnviado) // MD-2484 Incluimos el método que consulta si existe desistimiento y/o exclusion en PTFR
                    && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2958 incluimos que no puedan acceder a estas acciones los gestores ext
                    ) { 
                habilitar = Boolean.TRUE;
            }
        }
        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     */
    private boolean habilitarCalificacion(Boolean esModoConsulta, Integer idEstadoConvocatoria) {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        // Lista de estados internos donde no se permite calificar
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)); // MD-2147 incluimos excluido provisional
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)); // MD-2148 incluimos desistido provisional


        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        // Rechazada por la entidad solicitante: solo modo consulta, no se permite calificar
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));

        // MD-2399 listado de id_provisional
        List<Integer> lstIdProvisional = new ArrayList<>();
        lstIdProvisional.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        lstIdProvisional.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        Boolean estadosProvisionales = Boolean.FALSE;
        if (dto.getEstadoProvisionalDTO().getIdValor() != null && lstIdProvisional.contains(dto.getEstadoProvisionalDTO().getIdValor())) {
            estadosProvisionales = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(lstEstadoExpInter.contains(dto.getEstadoRevAdminDTO().getIdValor())
                || lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                || Boolean.TRUE.equals(estadosProvisionales) // MD-2399 incluimos la condicion del idProvisional
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria))
                || Boolean.TRUE.equals(esModoConsulta)) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la descarga de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonDescargaDocumento(ExpedienteDocDTO expDocDTO) {
        Boolean habilitar = Boolean.TRUE;
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR)
                .equals(expDocDTO.getEstadoDTO().getIdValor()))
                || (utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(expDocDTO.getEstadoDTO().getIdValor())
                        && expDocDTO.getCdFica() == null)
                || (expDocDTO.getCdFica() == null)) {
            habilitar = Boolean.FALSE;
        }
        return habilitar;
    }
    
    
    
    /**
     * Metodo para identificar si se habilita la descarga de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonSubirDocumento(ExpedienteDocDTO expDocDTO, Boolean esModoConsulta) {
        Boolean habilitar = Boolean.FALSE;
        
        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
        
        //VMM 20/01/2026 MD-2278: validamos estados para subir nueva version de los documentos
        List<Integer> lstEstadoVersionDoc = new ArrayList<>();
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO));
        lstEstadoVersionDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
        
        // MD-2423 validamos que el expediente no esté en desistido o excluido provisional
        List<Integer> lstEstadoDesExcPro = new ArrayList<>();
        lstEstadoDesExcPro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstEstadoDesExcPro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
               
        if (lstEstadoVersionDoc.contains(expDocDTO.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.TRUE;
            
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR).equals(expDocDTO.getEstadoDTO().getIdValor())
                    && !lstEstadoExp.contains(this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor())) {
                habilitar = Boolean.FALSE;
            }
            
            if (expDocDTO.getTipoDocumentoDTO() != null && expDocDTO.getTipoDocumentoDTO().getIdValor() != null &&
                    (expDocDTO.getTipoDocumentoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION)) ||
                    expDocDTO.getTipoDocumentoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_EXPEDIENTE_FIRMADO)))) {
                habilitar = Boolean.FALSE;
            }
        }
        
        //VMM 09/02/26 MD-2338: deshabilitamos el boton si es modo consulta
        // Rechazada por la entidad solicitante: solo modo consulta, no se permite subir documentos
        if (Boolean.TRUE.equals(esModoConsulta) || lstEstadoDesExcPro
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }


    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
//    public boolean habilitarBotonSubsanarDocumento(Boolean esModoConsulta,
//            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {
//
//        Boolean habilitar = Boolean.TRUE;
//
//        List<Integer> lstEstados = new ArrayList<>();
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
//
//        if (Boolean.FALSE.equals(this.habilitarCalificacion(esModoConsulta, idEstadoConvocatoria))
//                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
//                || Boolean.FALSE.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(expDocDto.getTramiteDTO().getIdValor()))
//                || Objects.isNull(expDocDto.getCdFica())) {
//            habilitar = Boolean.FALSE;
//        }
//
//        return habilitar;
//    }

    public boolean habilitarBotonSubsanarDocumento(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

            ExpedienteDTO exp = this.expedienteBean.getExpedienteDTO();
        
        
            // Si el expediente o la convocatoria no permiten la acción,
            // el botón no se habilita en ningún caso.
            if (Boolean.FALSE.equals(this.habilitarCalificacion(esModoConsulta, idEstadoConvocatoria))) {
                return Boolean.FALSE;
            }
            
            // Condición para el estado PENDIENTE_REVISION (lógica original)
            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                    .equals(expDocDto.getEstadoDTO().getIdValor())) {
                return Boolean.TRUE.equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.PRESENTACION)
                        .equals(expDocDto.getTramiteDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                                .equals(expDocDto.getTramiteDTO().getIdValor()))
                        && Objects.nonNull(expDocDto.getCdFica());
            }
            
            // Condición para el nuevo estado SIN_SUBIR
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR).equals(expDocDto.getEstadoDTO().getIdValor()) 
                    && (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION).equals(exp.getEstadoRevAdminDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO).equals(exp.getEstadoRevAdminDTO().getIdValor()))) {
            // En este caso, no se requiere el cdFica, ya que el documento no ha sido subido.
            return Boolean.TRUE.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(expDocDto.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(expDocDto.getTramiteDTO().getIdValor()));
            }
            
            //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { 
                return Boolean.FALSE;
            }
            // Si el estado del documento no es ninguno de los anteriores,
            // el botón no se habilita.
            return Boolean.FALSE;
       }
    
    
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonAprobarDocumento(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                
                // MD-2399 listado de id_provisional
                List<Integer> lstIdProvisional = new ArrayList<>();
                lstIdProvisional.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
                lstIdProvisional.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
                Boolean estadosProvisionales = Boolean.FALSE;
                if (dto.getEstadoProvisionalDTO().getIdValor() != null && lstIdProvisional.contains(dto.getEstadoProvisionalDTO().getIdValor())) {
                    estadosProvisionales = Boolean.TRUE;
                }

                if (!lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Boolean.TRUE.equals(estadosProvisionales) // MD-2399 incluimos la condicion del idProvisional
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }

            } else {

                // Lista de estados de documentos donde se permite calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

                if (Boolean.FALSE
                        .equals(this.habilitarCalificacion(esModoConsulta, idEstadoConvocatoria))
                        || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }
        } else {

            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

            // principal
            Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                    && (Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional())
                            || expDocDto.getItAdicional() == null);

            // adicional
            Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)
                    .equals(expDocDto.getTramiteDTO().getIdValor())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevRequiDTO().getIdValor())
                        || Boolean.FALSE.equals(valAdicional)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados internos requisito donde no se permite
                // calificar
                // VMM 21/10/26 MD-3158: cuando se manda a subsanar un documento y el solicitante lo subsana queda en
                // PENDIENTE_REVISION_SUBSANACION y hay que poder aprobarlo/rechazarlo igual que el resto de documentos,
                // aunque venga de requisitos, sin depender del estado de la revision de requisitos del expediente
                // (antes exigia REVISION_OK / PENDIENTE_REVISION_SUBSANACION_REQUISITOS y excluia
                // PENDIENTE_SUBSANACION_REQUISITOS). valPrincipal ya exige que el documento este en
                // PENDIENTE_REVISION_SUBSANACION.
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevRequiDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }
        
        // VMM 21/10/26 MD-3158: un documento de requisito pendiente de revision de subsanacion siempre se puede
        // aprobar/rechazar (si tiene fichero), independientemente de IT_ADICIONAL, del tramite y del estado del
        // expediente. Siguen aplicando el modo consulta y el rechazo definitivo de mas abajo.
        if (!Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(expDocDto.getEstadoDTO().getIdValor())
                && Objects.nonNull(expDocDto.getCdFica())) {
            habilitar = Boolean.TRUE;
        }

        //VMM 09/02/26 MD-2338: deshabilitamos el boton si es modo consulta
        // Rechazada por la entidad solicitante: solo modo consulta, no se permite aprobar
        if (Boolean.TRUE.equals(esModoConsulta)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(dto.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonDesaprobarDocumento(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados de documentos donde se permite desaprobar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                //VMM 03/02/26 MD-2478: se comenta esta linea porque no se puede desaprobar hasta que al menos subsane el documento alguna vez
                //lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

                if (!lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        //VMM 03/02/26 MD-2478: se comenta esta linea porque no muestra el botón cuando es un trámite de presentación de solicitud
                        //y al no ser documento de requisitos, siempre será trámite de presentación porque eso no se cambia
                        //|| utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(expDocDto.getTramiteDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados de documentos donde se permite desaprobar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

                if (Boolean.FALSE
                        .equals(this.habilitarCalificacion(esModoConsulta, idEstadoConvocatoria))
                        || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        } else {

            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

            // principal
            Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                    && (Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional())
                            || expDocDto.getItAdicional() == null);

            // adicional
            Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)
                    .equals(expDocDto.getTramiteDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite desaprobar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevRequiDTO().getIdValor())
                        || Boolean.FALSE.equals(valAdicional)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados internos requisito donde no se permite desaprobar
                // VMM 21/10/26 MD-3158: mismo criterio que habilitarBotonAprobarDocumento, ya no depende del estado de la
                // revision de requisitos del expediente (el documento tiene que estar en PENDIENTE_REVISION_SUBSANACION,
                // lo exige valPrincipal).
                List<Integer> lstEstados = new ArrayList<>();
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevRequiDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }

        // VMM 21/10/26 MD-3158: igual que en habilitarBotonAprobarDocumento, un documento de requisito pendiente de
        // revision de subsanacion siempre se puede rechazar (si tiene fichero).
        if (!Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(expDocDto.getEstadoDTO().getIdValor())
                && Objects.nonNull(expDocDto.getCdFica())) {
            habilitar = Boolean.TRUE;
        }

        // Rechazada por la entidad solicitante: solo modo consulta, no se permite desaprobar
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                .equals(dto.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonRevertirCalificacionDocumento(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) throws ServiceException {
        
        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        Boolean habilitar = Boolean.TRUE;
        List<ExpedienteDocHistDTO> registrosAnteriores = null;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));

        List<AtlasFilter> filtroHist = new ArrayList<>();
        filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                expDocDto.getIdExpedienteDoc(), Operador.EQUALS));

        int totalHistorico = 0;
        try {
            totalHistorico = this.expedienteDocHistService.buscar(0, -1, filtroHist, null).size();
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        List<AtlasFilter> filtroHistUltimo = new ArrayList<>();
        filtroHistUltimo
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                        expDocDto.getIdExpedienteDoc(), Operador.EQUALS));
        filtroHistUltimo.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                expDocDto.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));

        List<AtlasSort> ordenamientoHist = new ArrayList<>();
        ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

        try {
            registrosAnteriores = this.expedienteDocHistService.buscar(0, -1, filtroHistUltimo,
                    ordenamientoHist);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            
        }
        
        Boolean existeDesistimientoEnviado = this.documentoPtfrService
                .consultarDocumentoPortafirmaPorTramite(dto.getIdExpediente(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));

        
        //Si el documento ya ha firmado subsanacion en ptfr y ha sido subsanado
        Boolean existeSubsanPendienteRev = Boolean.FALSE;
        if(Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionFirmado) && 
                registrosAnteriores != null && !registrosAnteriores.isEmpty() &&
                registrosAnteriores.get(0).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION))) {
            existeSubsanPendienteRev = Boolean.TRUE;
        }
        
        // MD-2147 está en estado escluido provisional la rev req
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
     // MD-2148 determinamos si se encuantra en desistido provisional la rev admin
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        
        // VMM 21/10/26 MD-3158: un documento de requisito que se ha aprobado/rechazado estando pendiente de revision
        // de subsanacion (su estado anterior en el historico es PENDIENTE_REVISION_SUBSANACION) tambien se puede
        // revertir, las veces que haga falta; el resto de documentos de requisito siguen sin poder revertirse aqui.
        Boolean requisitoCalificadoDesdeSubsanacion = Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())
                && registrosAnteriores != null && !registrosAnteriores.isEmpty()
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(registrosAnteriores.get(0).getEstadoDTO().getIdValor());

        if (Boolean.FALSE.equals(this.habilitarCalificacion(esModoConsulta, idEstadoConvocatoria))
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || (Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())
                        && Boolean.FALSE.equals(requisitoCalificadoDesdeSubsanacion))
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                || (Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionFirmado)
                        && Boolean.FALSE.equals(existeSubsanPendienteRev))
                || Boolean.TRUE.equals(this.existeEnvioPtfrDesistidoEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrDesistidoFirmado) || totalHistorico <= 1
                || registrosAnteriores == null || registrosAnteriores.isEmpty()
                || Boolean.TRUE.equals(existeDesistimientoEnviado)
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 incluir condicion desisitido provisional
                || Boolean.TRUE.equals(desisitidoProvisonalEnviado)) { // MD-2148 incluimos la condición desistidio provisional la rev adm
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarDenegacion(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        if (Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())) {
            if (StringUtils.isBlank(expDocDto.getDsObservacionRequisito())) {
                habilitar = Boolean.FALSE;
            }
        } else {
            if (Objects.isNull(expDocDto.getConvocatoriaCauDTO())
                    || Objects.isNull(expDocDto.getConvocatoriaCauDTO().getIdConvocatoriaCau())) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
    
    public boolean habilitarBotonMostrarDenegacionContrato(ExpedienteDocDTO expDocDto) {
        
        Boolean habilitar = Boolean.TRUE;
        
        if (StringUtils.isBlank(expDocDto.getTlCausaDescripcion())) {
                habilitar = Boolean.FALSE;
        } else {
            if (Objects.isNull(expDocDto.getConvocatoriaCauContDTO())
                    || Objects.isNull(expDocDto.getConvocatoriaCauContDTO().getIdConvocatoriaCauCont())) {
                habilitar = Boolean.FALSE;
            }
        }
        
        return habilitar;
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarObservacion(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.FALSE;

        //VMM 11/02/26 MD-2471: Habilitamos boton si existen observaciones del ciudadano o del gestor
        if (Objects.nonNull(expDocDto.getTlObservaciones()) || Objects.nonNull(expDocDto.getTlObservacionesGestor()) ) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos del historico
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarObservacionHist(ExpedienteDocHistDTO expDocHistDto) {

        Boolean habilitar = Boolean.FALSE;

        //VMM 11/02/26 MD-2471: Habilitamos boton si existen observaciones del ciudadano o del gestor
        if (Objects.nonNull(expDocHistDto.getTlObservaciones()) || Objects.nonNull(expDocHistDto.getTlObservacionesGestor()) ) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos contrato
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException 
     */
    private boolean habilitarCalificacionContrato() {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
        
        ExpedienteContratoDTO contratoDTO = this.expedientePartiDTO.getExpedienteContrato();

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        
        // Lista de estados contrato donde no se permite calificar
        List<Integer> lstEstadoContrato = new ArrayList<>();
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION));
        
        List<Integer> lstEstadoRevContrato = new ArrayList<>();
        lstEstadoRevContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA));
        lstEstadoRevContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA));
        lstEstadoRevContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
        lstEstadoRevContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
        lstEstadoRevContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_DESISTIDO));

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente del participante está Rechazado
        // Definitivo, no se permite calificar/subsanar/revertir su contrato.
        if (lstEstadoContrato.contains(contratoDTO.getIdEstadoDTO().getIdValor())
                || lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                || lstEstadoRevContrato.contains(contratoDTO.getIdEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedientePartiDTO.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    public boolean habilitarBotonFinalizarRevisionExp(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        
        Integer idPenReq = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION);
        Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK);
        this.setActivarBotonFinalizacionExpediente( this.expedienteBean.getLstExpedienteConContrato().stream()
                .allMatch(x -> { 
                    CatalogosValoresDTO estado = x.getEstadoRevContratoDTO();
                    return estado != null && estado.getIdValor() != null &&
                        (estado.getIdValor().equals(idPenReq) ||
                        estado.getIdValor().equals(idRevOk));
                })
            );
        
        ConvocatoriaDTO conv = this.obtenerConvocatoriaCacheada();
        this.comprobarGestoresExcep(conv);

        if (Boolean.FALSE.equals(esModoConsulta)
                && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                && Boolean.TRUE.equals(this.activarBotonFinalizacionExpediente)
                && !conv.isEsGestorJCT()) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    /**
     * Metodo para identificar si se habilita el botón desaprobar doc Contrato
     *
     * @param dto
     */
    public boolean habilitarBotonDesaprobarDocContrato(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedientePartiDTO;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente del participante está Rechazado
        // Definitivo, no se permite calificar su contrato.
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                .equals(dto.getEstadoDTO().getIdValor())) {
            return Boolean.FALSE;
        }

        // Lista de estados de documentos donde se permite calificar
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

        // principal
        Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

        // adicional
        Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL)
                .equals(expDocDto.getTramiteDTO().getIdValor())
                && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(expDocDto.getEstadoDTO().getIdValor());

		// Lista de estados contrato donde no se permite calificar
        List<Integer> lstEstadoContrato = new ArrayList<>();
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_DESISTIDO));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO));
            
        if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

            // Lista de estados internos requisito donde no se permite
            // calificar
            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

            if (lstEstados.contains(dto.getEstadoRevContratoDTO().getIdValor())
                    || lstEstadoContrato.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())
                    || Boolean.FALSE.equals(valAdicional)
                    || Objects.isNull(expDocDto.getCdFica())) {
                habilitar = Boolean.FALSE;
            }
        } else {

            // Lista de estados internos requisito donde no se permite
            // calificar
            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_SUBSANACION));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO));

            if (lstEstados.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())
                    || lstEstadoContrato.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())
                    || Boolean.FALSE.equals(valPrincipal)
                    || Objects.isNull(expDocDto.getCdFica())) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonAprobarDocContrato(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedientePartiDTO;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente del participante está Rechazado
        // Definitivo, no se permite calificar su contrato.
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                .equals(dto.getEstadoDTO().getIdValor())) {
            return Boolean.FALSE;
        }

        // Lista de estados de documentos donde se permite calificar
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        // principal
        Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

        // adicional
        Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)
                .equals(expDocDto.getTramiteDTO().getIdValor())
                && (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                        .equals(expDocDto.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)
                        .equals(expDocDto.getEstadoDTO().getIdValor()))
                && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional());

        // Lista de estados contrato donde no se permite calificar
        List<Integer> lstEstadoContrato = new ArrayList<>();
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIADO_PORTAFIRMAS));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_DESISTIDO));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO));
        
        if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

            // Lista de estados internos requisito donde no se permite
            // calificar
            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

            if (lstEstados.contains(dto.getEstadoRevContratoDTO().getIdValor())
                    || lstEstadoContrato.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())
                    || Boolean.FALSE.equals(valAdicional)
                    || Objects.isNull(expDocDto.getCdFica())) {
                habilitar = Boolean.FALSE;
            }
        } else {

            // Lista de estados internos requisito donde no se permite
            // calificar
            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

            if (lstEstados.contains(dto.getEstadoRevContratoDTO().getIdValor())
                    || lstEstadoContrato.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())
                    || Boolean.FALSE.equals(valPrincipal)
                    || Objects.isNull(expDocDto.getCdFica())) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
    
    /**
     * Método para aprobar un documento
     *
     * @param documento Datos del documento
     */
    public void aprobarDocContratos(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            documento = this.expedienteDocService.buscarId(documento.getIdExpedienteDoc());
            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            documento.setConvocatoriaCauDTO(null);
            documento.setTlCausaDescripcion(null);

            ExpedienteDocDTO expeDTO = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());

            if (documento.getTipoDocumentoDTO().getIdValor() == null) {
                documento.setTipoDocumentoDTO(
                        new CatalogosValoresDTO(expeDTO.getTipoDocumentoDTO().getIdValor()));
            }
            this.expedienteDocService.guardarConHistorico(documento);
            
            PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.formularioDocumentos);
            PrimeFaces.current().ajax().update(this.solapaDocumentos);

            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     * @throws ServiceException 
     */
    public boolean habilitarBotonRevertirCalificacionDocContrato(ExpedienteDocDTO expDocDto) throws ServiceException {

        Boolean habilitar = Boolean.TRUE;
        List<ExpedienteDocHistDTO> registrosAnteriores = null;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));

        List<AtlasFilter> filtroHist = new ArrayList<>();
        filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                expDocDto.getIdExpedienteDoc(), Operador.EQUALS));

        int totalHistorico = 0;
        try {
            totalHistorico = this.expedienteDocHistService.buscar(0, -1, filtroHist, null).size();
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        List<AtlasFilter> filtroHistUltimo = new ArrayList<>();
        filtroHistUltimo
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                        expDocDto.getIdExpedienteDoc(), Operador.EQUALS));
        filtroHistUltimo.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                expDocDto.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));

        List<AtlasSort> ordenamientoHist = new ArrayList<>();
        ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

        try {
            registrosAnteriores = this.expedienteDocHistService.buscar(0, -1, filtroHistUltimo,
                    ordenamientoHist);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
        
        List<Integer> lstEstadosContrato = new ArrayList<>();
        lstEstadosContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO));
        lstEstadosContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION));
        lstEstadosContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA));
        Boolean contratoPendienteRev = Boolean.FALSE;
        if(this.expedientePartiDTO != null && this.expedientePartiDTO.getExpedienteContrato() != null 
                && this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO() != null 
                && this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor() != null ) {
            contratoPendienteRev = lstEstadosContrato.contains(this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor());
        }

        if (Boolean.FALSE.equals(this.habilitarCalificacionContrato())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || Boolean.FALSE.equals(contratoPendienteRev)
                || Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionFirmado)
                || Boolean.TRUE.equals(this.existeEnvioPtfrDesistidoEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrDesistidoFirmado) || totalHistorico <= 1
                || registrosAnteriores == null || registrosAnteriores.isEmpty()) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos de contratos
     *
     * @param dto
     */
    public boolean habilitarBotonSubsanarDocContratos(Boolean esModoConsulta,
            Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
        
        List<Integer> lstEstadosContrato = new ArrayList<>();
        lstEstadosContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
        
        List<Integer> lstTramites = new ArrayList<>();
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_RENUNCIA));

        if (Boolean.FALSE.equals(this.habilitarCalificacionContrato())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || !lstTramites.contains(expDocDto.getTramiteDTO().getIdValor())
                || Objects.isNull(expDocDto.getCdFica())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    /**
     * Método para mostrar la ventana popup con la causa de denegación asociada
     * al documento de contratos
     *
     * @param documento Datos de documento
     */
    public void mostrarDenegacionDocContratos(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        try {
            this.documentoSeleccionado = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());
            this.idCausaSubsanacion = this.documentoSeleccionado.getConvocatoriaCauContDTO()
                        .getIdConvocatoriaCauCont();
            this.descripcionCausaSubsanacion = this.documentoSeleccionado
                        .getTlCausaDescripcion();
            this.modoLecturaSubsanacion = Boolean.TRUE;
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocContratos').show();");
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * desistimiento
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public Boolean habilitarBotonEnviarDesistirContratosPortafirmas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;
        if(this.getExpedientePartiDTO() == null || this.getExpedientePartiDTO().getIdExpediente() == null) {
            return habilitar;
        } 

        ExpedienteDTO dto = this.getExpedientePartiDTO();
        
        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        // Lista de estados finales donde no se permite enviar a portafirmas
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
        
        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
        
        List<Integer> lstEstadoContrato = new ArrayList<>();
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_SUBSANACION));
        lstEstadoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO));
        
        List<Integer> lstEstadoDocPermiteDesistir = new ArrayList<>();
        lstEstadoDocPermiteDesistir.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstadoDocPermiteDesistir.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));

        Boolean permitirDesistir = dto.getLstExpedienteDocDTO().stream()
            .filter(Objects::nonNull)
            .filter(x -> x.getEstadoDTO() != null)
            .filter(x -> x.getEstadoDTO().getIdValor() != null)
            .anyMatch(x -> lstEstadoDocPermiteDesistir.contains(x.getEstadoDTO().getIdValor()));
                
        
        if (Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoEnProceso)
                && Boolean.FALSE.equals(this.existeEnvioPtfrDesistidoFirmado)
                && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && (Boolean.TRUE.equals(this.habilitarTextoObservacionesContrato())
                        || StringUtils.isNotBlank(this.tlObservacionesParaCiu))
                && (permitirDesistir.equals(Boolean.TRUE)
                        || (dto.getExpedienteContrato().getIdEstadoRevAdminDTO() != null 
                        && dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor() != null
                        && lstEstadoContrato.contains(dto.getExpedienteContrato().getIdEstadoRevAdminDTO().getIdValor())))) {
            habilitar = Boolean.TRUE;
        }
        
        return habilitar;
    }


    /**
     * Metodo para calcular si existe un envío de subsanación a portafirmas
     *
     * @param dto
     */
    private void calcularExisteEnvioPortafirmas(ExpedienteDTO dto) {
        this.setExisteEnvioPtfrSubsanacionFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistidoEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistidoFirmado(Boolean.FALSE);

        // Quitamos los registros de baja
        List<ExpedienteDocDTO> lstExpDoc = dto.getLstExpedienteDocDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());

        // Obtenemos la lista de documentos asociados a los requerimientos
        List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
                .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Quitamos los registros documentos asociado a los requerimientos
        lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                .collect(Collectors.toList());

        // Quitamos los registros que no esten asociados a un envío a
        // portafirmas
        lstExpDoc = lstExpDoc.stream().filter(x -> Objects.nonNull(x.getIdDocumentoPtfr()))
                .collect(Collectors.toList());

        try {

            if (!lstExpDoc.isEmpty()) {

                // Es el mismo registro de envío para todos los documentos
                // observados o desistidos
                this.documentoPtfrDTO = this.documentoPtfrService
                        .buscarPorId(lstExpDoc.get(0).getIdDocumentoPtfr());

                if (Objects.nonNull(this.documentoPtfrDTO.getTramiteDTO())) {
                    if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrSubsanacionFirmado(Boolean.TRUE);
                    }

                    if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.TRUE);
                    }

                    if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL) // MD-2148 cambiamos a desistido provisional
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrDesistidoFirmado(Boolean.TRUE);
                    }

                    if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL) // MD-2148 cambiamos a desisitido provisional
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrDesistidoEnProceso(Boolean.TRUE);
                    }
                }
            }

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
    }

    /**
     * Metodo para actualizar los valores del formulario
     */
    private void actualizarValoresFormulario() {
        try {
            //VMM 07/04/26 MD-1944: guardamos el impresoDTO porque se machaca al instanciar el ExpDTO como una entidad Expediente
            ImpresoDTO impresoDTO = this.expedienteBean.getExpedienteDTO().getImpresoDTO();
            
            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoExpedienteRevisionDocumento(this.idExpediente);

            this.calcularExisteEnvioPortafirmas(dto);

            dto.setImpresoDTO(impresoDTO);
            this.expedienteBean.setExpedienteDTO(dto);

            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.formularioDocumentos);
            PrimeFaces.current().ajax().update(this.solapaDocumentos);
            PrimeFaces.current().ajax().update(this.solapaRevisionContratos);

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
    }

    /**
     *
     * @param expedienteDocDTO
     */
    public void mostrarMotivo(ExpedienteDocDTO expedienteDocDTO) {
        DocumentoPtfrDTO docPtfrDTO;
        try {

            if (expedienteDocDTO.getIdDocumentoPtfr() != null) {
                docPtfrDTO = this.documentoPtfrService
                        .buscarPorId(expedienteDocDTO.getIdDocumentoPtfr());

                if (docPtfrDTO.getMotivoRechazo() != null
                        && !StringUtils.isEmpty(docPtfrDTO.getMotivoRechazo())) {
                    this.motivoRechazoSeleccionado = docPtfrDTO.getMotivoRechazo();
                } else {
                    this.motivoRechazoSeleccionado = AtlasMessageUtils
                            .getStringFromBundle(ConstantesWeb.Mensajes.MOTIVO_RECHAZO_VACIO);
                }
            } else {
                this.motivoRechazoSeleccionado = AtlasMessageUtils
                        .getStringFromBundle(ConstantesWeb.Mensajes.MOTIVO_RECHAZO_VACIO);
            }

            PrimeFaces.current().executeScript("PF('dialogMotivo').show();");

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR);
        }
    }

    /**
     *
     */
    public void cerrarMotivo() {
        this.motivoRechazoSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogMotivo').close();");
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
    public void estaDadoAltaUsuario() {
        if (this.usuarioDadoAltaCalculado) {
            return;
        }
        Boolean estaDadoAlta = Boolean.FALSE;
        try {
            ExpedienteDTO expeDTO = this.expedienteBean.getExpedienteDTO();
            if (expeDTO == null) {
                expeDTO = this.expedienteService.buscarIdSimple(this.idExpediente, false);
            }
            if (expeDTO.getRepresentanteLegal() != null
                    && expeDTO.getRepresentanteLegal().getCdNumIden() != null
                    && expeDTO.getMedioComunicacionDTO() != null
                    && expeDTO.getMedioComunicacionDTO().getIdValor() != null
                    && expeDTO.getMedioComunicacionDTO().getIdValor().equals(utilConstantes
                            .getGlobalCatalogosValores()
                            .get(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))) {
                NoteExisteNifOut existeNif = this.noteService.existeNif(
                        NoteAdmeService.ConstantesNote.COD_APP,
                        expeDTO.getRepresentanteLegal().getCdNumIden());
                estaDadoAlta = StringUtils.isNotBlank(existeNif.getCdAbonado());
            } else if (Objects.nonNull(this.idExpediente)) {
                NoteExisteNifOut existeNif = this.noteService.existeNif(
                        NoteAdmeService.ConstantesNote.COD_APP,
                        expeDTO.getInteresadoDTO().getCdNumIden());
                estaDadoAlta = StringUtils.isNotBlank(existeNif.getCdAbonado());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        this.setUsuarioDadoAlta(estaDadoAlta);
        this.usuarioDadoAltaCalculado = true;
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
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS)
                .equals(expeDTO.getEstadoRevAdminDTO().getIdValor())) {
            LocalDate fechaPlazoSub = fechaPlazo.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            plazoSuperado = fechaHoy.compareTo(fechaPlazoSub) < 0;
        }

        return plazoSuperado;
    }

    /**
     *
     * @param doc
     * @return
     */
    public Boolean validarSeleccionDoc(ExpedienteDocDTO doc) {
        Boolean existeDoc = Boolean.FALSE;

        // obtenemos la lista de documentos registrados en el expediente
        try {
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(doc.getExpedienteDTO().getIdExpediente());

            if (Objects.nonNull(doc.getTipoDocumentoDTO().getIdValor())) {
                for (ExpedienteDocDTO item : expedienteDTO.getLstExpedienteDocDTO()) {
                    if (doc.getTipoDocumentoDTO().getIdValor()
                            .equals(item.getTipoDocumentoDTO().getIdValor())
                            && doc.getTipoDocumentoDTO().getIdValor()
                                    .equals(item.getTramiteDTO().getIdValor())) {
                        existeDoc = Boolean.TRUE;
                        break;
                    }
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        return existeDoc;
    }
    
    /**
     * Método para mostrar la ventana de asignación de causa de subsanación al
     * tipo de documento
     *
     * @param documento Datos del documento
     */
    public void mostrarSubsanarDocContrato(ExpedienteDocDTO documento) {

        this.inicializarFormularioSubsanacion();        
        this.documentoSeleccionado = documento;
        if (this.documentoSeleccionado != null) {
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocContratos').show();");
        }
    }
    
    
    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     *
     * @param convocatoriaDTO
     * @throws ServiceException 
     */
    public void guardarContratoExterno() throws ServiceException {

        
        ExpedienteDTO dto = this.expedienteService.buscarId(this.idExpediente);
        
        if(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO).equals(dto.getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS).equals(dto.getEstadoDTO().getIdValor())) {
        
            if (this.contratoSubir == null
                    || StringUtils.isBlank(this.contratoSubir.getFileName())) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_ARCHIVO);
            } else {

                try (InputStream fileInputStream = this.contratoSubir.getInputstream()) {
                    this.nombreArchivoTemporal = this.contratoSubir.getFileName();


                    this.formularioDocExternoDTO.setFicaMetadatos(new FicaMetadatos());
                    // Nombre, fecha y extension del fichero
                    this.formularioDocExternoDTO.getFicaMetadatos()
                            .setNombreFichero(this.nombreArchivoTemporal);
                    this.formularioDocExternoDTO.getFicaMetadatos().setFechaFichero(new Date());
                    this.formularioDocExternoDTO.getFicaMetadatos()
                            .setExtension(this.nombreArchivoTemporal.substring(
                                    this.nombreArchivoTemporal.lastIndexOf('.') + 1,
                                    this.nombreArchivoTemporal.length()));
                    this.formularioDocExternoDTO.getFicaMetadatos()
                            .setUsuario(UserUtils.getUserName().toUpperCase());
                    this.formularioDocExternoDTO.getFicaMetadatos().setEncriptar(false);
                    // Datos del fichero
                    this.formularioDocExternoDTO
                            .setContenidoFichero(IOUtils.toByteArray(fileInputStream));

                    
                    if(this.formularioDocExternoDTO.getExpedienteDTO() == null) {
                        this.formularioDocExternoDTO.setExpedienteDTO(new ExpedienteDTO());
                    }
                    this.formularioDocExternoDTO.getExpedienteDTO().setIdExpediente(this.idExpediente);

                    this.expedienteDocService.guardarContratoExterno(this.formularioDocExternoDTO);
                    
                    this.expedienteContratoService.cambiarEstadoContrato(null, this.idExpediente, 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO), 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO));
                    
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

                    this.actualizarValoresFormulario();

                } catch (IOException | ServiceException e) {
                    log.error(e.getMessage());

                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);


                }

            }
        }else {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
        }
    }
    
    /**
     * Metodo que se invoca tras aceptar el popup de actualizar contrato.
     *
     */
    public void actualizarContratoExterno() {

        if (this.formularioDocExternoDTO.getTipoDocumentoDTO() == null
                || this.formularioDocExternoDTO.getTipoDocumentoDTO().getIdValor() == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TIPO_DOCUMENTO);
        } else if (this.archivoSubir == null
                || StringUtils.isBlank(this.archivoSubir.getFileName())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_ARCHIVO);
        } else {

            try (InputStream fileInputStream = this.archivoSubir.getInputstream()) {
                this.nombreArchivoTemporal = this.archivoSubir.getFileName();


                this.formularioDocExternoDTO.setFicaMetadatos(new FicaMetadatos());
                // Nombre, fecha y extension del fichero
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setNombreFichero(this.nombreArchivoTemporal);
                this.formularioDocExternoDTO.getFicaMetadatos().setFechaFichero(new Date());
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setExtension(this.nombreArchivoTemporal.substring(
                                this.nombreArchivoTemporal.lastIndexOf('.') + 1,
                                this.nombreArchivoTemporal.length()));
                this.formularioDocExternoDTO.getFicaMetadatos()
                        .setUsuario(UserUtils.getUserName().toUpperCase());
                this.formularioDocExternoDTO.getFicaMetadatos().setEncriptar(false);
                // Datos del fichero
                this.formularioDocExternoDTO
                        .setContenidoFichero(IOUtils.toByteArray(fileInputStream));

                if(this.expedientePartiDTO != null && this.expedientePartiDTO.getIdExpediente() != null) {
                    this.idExpedienteContratos = this.expedientePartiDTO.getIdExpediente();
                }else if(this.idExpedienteContratos != null && (this.expedientePartiDTO == null || this.expedientePartiDTO.getIdExpediente() == null)) {
                    this.expedientePartiDTO = expedienteService.buscarId(this.idExpedienteContratos);
                }
                if(this.formularioDocExternoDTO.getExpedienteDTO() == null) {
                    this.formularioDocExternoDTO.setExpedienteDTO(new ExpedienteDTO());
                }
                this.formularioDocExternoDTO.getExpedienteDTO().setIdExpediente(this.idExpedienteContratos);
                
                this.expedienteDocService.actualizarContratoExterno(this.formularioDocExternoDTO, UserUtils.getUserName().toUpperCase());
                if(this.expedientePartiDTO != null && this.expedientePartiDTO.getExpedienteContrato() != null
                        && this.expedientePartiDTO.getExpedienteContrato().getIdExpedienteContrato() != null) {
                    this.expedienteContratoService.cambiarEstadoContrato(this.expedientePartiDTO.getExpedienteContrato(), this.expedientePartiDTO.getExpedienteContrato().getIdExpediente(), null, 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.ENVIO_COLA_NOTIFICACIONES));
                }
                
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

                this.actualizarValoresFormulario();

                this.inicializarDocumento();

            } catch (IOException | ServiceException e) {
                log.error(e.getMessage());

                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            }
        }
    }
    
    /**
     * Método que asigna el expediente de contrato seleccionado sobre el que trabajaremos
     *
     * @param exp - Expediente sobre el que mostraremos la tabla con sus documentos
     * @throws ServiceException 
     */
    public List<ExpedienteDocDTO> obtenerListaDocumentosExpedienteContratos(
            ExpedienteDTO exp) throws ServiceException {
        
        List<ExpedienteDocDTO> listado = new ArrayList<>();
        
        try {
            this.idExpedienteContratos = exp.getIdExpediente();
            this.expedientePartiDTO = exp;
            this.tlObservacionesParaCiu = "";
            ExpedienteContratoDTO expContratoDto = this.expedienteContratoService.obtenerExpedienteContratoByExpediente(exp);
            if(expContratoDto != null) {
                if((expContratoDto.getIdEstadoRevAdminDTO() == null || expContratoDto.getIdEstadoRevAdminDTO().getIdValor() == null)
                        && expContratoDto.getIdTipoContratoDTO() != null && expContratoDto.getIdTipoContratoDTO().getIdValor() != null
                        && expContratoDto.getIdTipoContratoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_INICIAL))){
                    expContratoDto.setIdEstadoRevAdminDTO(this.catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SIN_SUBIR)));
                }else if((expContratoDto.getIdEstadoRevAdminDTO() == null || expContratoDto.getIdEstadoRevAdminDTO().getIdValor() == null)
                        && expContratoDto.getIdTipoContratoDTO() != null && expContratoDto.getIdTipoContratoDTO().getIdValor() != null
                        && expContratoDto.getIdTipoContratoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_SUSTITUCION))) {
                    expContratoDto.setIdEstadoRevAdminDTO(this.catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SIN_SUBIR_SUSTITUCION)));
                }
                this.expedientePartiDTO.setExpedienteContrato(expContratoDto);
                this.tlObservacionesParaCiu = expContratoDto.getTlObservaciParaCiudadano();
                
            }
            /**
            List<ExpedienteContrato> lstExpedienteContra = this.expedienteContratoDAO.findByIdExpediente(exp.getIdExpediente());
            if(lstExpedienteContra != null && !lstExpedienteContra.isEmpty()) {
                
                List<ExpedienteContratoDTO> expContraDTO = new ArrayList<>( Lists.transform(lstExpedienteContra,
                        ExpedienteContratoToDTO.INSTANCE));
                this.expedientePartiDTO.setExpedienteContrato(expContraDTO.get(0));
            }
            */
        }catch(Exception ex) {
            log.error(ex.getMessage());
                AtlasMessageUtils.addErrorMessageFromBundle(
                        "Error cargando Contrato");
            return listado;
        }

        
        return listado;
    }
    
    public Boolean habilitarRevisionContrato(Integer rev) throws ServiceException {
        Boolean habilitar = Boolean.FALSE;

        // Validaciones iniciales
        if (rev == null || this.expedientePartiDTO == null || this.expedientePartiDTO.getExpedienteContrato() == null 
                || this.expedientePartiDTO.getExpedienteContrato().getIdEstadoRevAdminDTO() == null) {
            return habilitar;
        }

        ExpedienteContratoDTO expContratoDTO = this.expedientePartiDTO.getExpedienteContrato();

        // Lógica para establecer los valores de renderRev según el estado del contrato
        if (expContratoDTO.getIdEstadoRevAdminDTO() != null && expContratoDTO.getIdEstadoRevAdminDTO().getIdValor() != null) {
            Integer idValorEstado = expContratoDTO.getIdEstadoRevAdminDTO().getIdValor();
            if (idValorEstado != null) {
                if (idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO))
                        || idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_PRESENTADO))
                        || idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_CON_SUST_PRESENTADA_TIPO_C_INICIAL))
                        || idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA))) {
                    this.setRenderRev1(Boolean.FALSE);
                    this.setRenderRev2(Boolean.TRUE);
                    this.setRenderRev3(Boolean.FALSE);
                    this.setRenderRev4(Boolean.FALSE);
                    this.setRenderRevObs(Boolean.TRUE);
                } else if (idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION))) {
                    this.setRenderRev1(Boolean.FALSE);
                    this.setRenderRev2(Boolean.FALSE);
                    this.setRenderRev3(Boolean.TRUE);
                    this.setRenderRev4(Boolean.FALSE);
                    this.setRenderRevObs(Boolean.TRUE);
                } else if (idValorEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA))) {
                    this.setRenderRev1(Boolean.FALSE);
                    this.setRenderRev2(Boolean.FALSE);
                    this.setRenderRev3(Boolean.FALSE);
                    this.setRenderRev4(Boolean.TRUE);
                    this.setRenderRevObs(Boolean.TRUE);
                } else {
                    this.setRenderRev1(Boolean.TRUE);
                    this.setRenderRev2(Boolean.FALSE);
                    this.setRenderRev3(Boolean.FALSE);
                    this.setRenderRev4(Boolean.FALSE);
                    this.setRenderRevObs(Boolean.FALSE);
                }
            }
        }

        // Devolver el renderRev correspondiente al valor de rev
        switch (rev) {
            case 1:
                return this.renderRev1;
            case 2:
                return this.renderRev2;
            case 3:
                return this.renderRev3;
            case 4:
                return this.renderRev4;
            case 5:
                return this.renderRevObs;
            default:
                return habilitar; // Si rev no es válido, devolver FALSE
        }
    }
    
    
    /**
     * Método que llama al anterior adaptado para solo obtener los Contratos de
     * un expediente concreto
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param ordenacion - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filter - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Componente
     * @throws ServiceException 
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteContratoDocDTO> obtenerListaDocumentoExpedienteParticipante(
            int index, int pageSize, Object ordenacion, Object filter) throws ServiceException {
        
        AtlasListAndCount<ExpedienteContratoDocDTO> listado = new AtlasListAndCount<>();
        
        this.setActivarBotonFinalizacionContratos(Boolean.FALSE);
        if(this.idExpedienteContratos == null) return listado;

        List<AtlasFilter> filtro = filter != null ? (List<AtlasFilter>) filter : new ArrayList<>();

        listado = this.obtenerListaDocumentoExpedienteContratos(index, pageSize, ordenacion, filtro);

        habilitarBotonFinalizarRevisionContratos(listado);
        
        return listado;
    }
    
    public void cargarExpedienteContrato() throws ServiceException {
        if((this.idExpedienteContratos != null) && (this.expedientePartiDTO == null || this.expedientePartiDTO.getIdExpediente() == null)) {
            this.expedientePartiDTO = this.expedienteService.buscarId(this.idExpedienteContratos);
        }
        
    }
    
    /**
     * Recupera el listado de objetos ExpedienteContratoDocDTO que cumple con los
     * criterios de búsqueda. Devuelve las entidades situadas entre las
     * posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param ordenacion - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filter - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Componente
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteContratoDocDTO> obtenerListaDocumentoExpedienteContratos(int index,
            int pageSize, Object ordenacion, Object filter) {
        AtlasListAndCount<ExpedienteContratoDocDTO> listado = new AtlasListAndCount<>();
        
        try {
            if (this.idExpedienteContratos != null) {

                List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
                List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

                List<Integer> lstIdEstados = new ArrayList<>();

                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL));
                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION));
                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_SUSTITUCION));
                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_RENUNCIA));
                lstIdEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_RENUNCIA));
                
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_CONTRATO_IDEXPEDIENTE,
                        this.idExpedienteContratos, Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_TRAMITE,
                        lstIdEstados, Operador.IN));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                if (orden == null) {
                    orden = new ArrayList<>();
                }

                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC, true));
                
                List<ExpedienteContratoDocDTO> listadoDatos = this.expedienteContratoDocService.buscar(index,
                        pageSize, filtros, orden);

                listado.setResultados(listadoDatos);
                listado.setTotal(this.expedienteContratoDocService.count(filtros));
                
                this.calcularExisteEnvioPortafirmas(this.expedienteBean.getExpedienteDTO());
                this.estaDadoAltaUsuario();

                StringBuilder cadenaobservacion = new StringBuilder();

                // VMM: en vez de consultar por cada documento (N+1) si esta asociado a un requisito,
                // hacemos una sola consulta con todos los idExpedienteDoc de la pagina y los agrupamos en memoria
                List<Integer> lstIdExpedienteDoc = listadoDatos.stream()
                        .map(dto -> dto.getExpedienteDocDTO().getIdExpedienteDoc())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                Map<Integer, ExpedienteReqDTO> mapaExpedienteReqPorDoc = new HashMap<>();
                if (!lstIdExpedienteDoc.isEmpty()) {
                    List<AtlasFilter> filtrosExpReq = new ArrayList<>();
                    filtrosExpReq.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_DOC,
                            lstIdExpedienteDoc, Operador.IN));
                    filtrosExpReq.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    List<ExpedienteReqDTO> lstTodosExpedienteReqDTO = this.expedienteReqService.buscar(0,
                            -1, filtrosExpReq, null);
                    for (ExpedienteReqDTO expReqDTO : lstTodosExpedienteReqDTO) {
                        mapaExpedienteReqPorDoc.putIfAbsent(expReqDTO.getIdExpedienteDoc(), expReqDTO);
                    }
                }

                // Recorremos los ExpedienteDoc para ver si cambia alguna de sus
                // propiedades
                for (ExpedienteContratoDocDTO dto : listadoDatos) {

                    ExpedienteReqDTO expedienteReqDTO = mapaExpedienteReqPorDoc
                            .get(dto.getExpedienteDocDTO().getIdExpedienteDoc());

                    if(dto.getExpedienteDocDTO().getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION))
                            && dto.getExpedienteDocDTO().getTlObservaciones() != null) {
                        cadenaobservacion.append(dto.getExpedienteDocDTO().getTlObservaciones()+ "\n ");

                    }

                    if (expedienteReqDTO != null) {
                        dto.getExpedienteDocDTO().setEsDocumentoRequisito(Boolean.TRUE);
                        dto.getExpedienteDocDTO().setDsDocumentoRequisito(
                                expedienteReqDTO.getRequisitoDTO().getDsValor());
                        dto.getExpedienteDocDTO().setDsObservacionRequisito(
                                expedienteReqDTO.getTlObservaciones());

                    }

                }
                
                if(!cadenaobservacion.toString().isEmpty()) {
                    this.textoObservacionesSubsanacion = cadenaobservacion.toString();
                }
               
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }

    public String autorizarConsulta(ExpedienteDocDTO doc) {
        String rpta = "";
        try {
            if (doc.getBloqueComponenteDTO() != null && doc.getBloqueComponenteDTO().getIdBloque() != null) {
                BloqueDTO bloqueDTO = this.bloqueService.buscarId(doc.getBloqueComponenteDTO().getIdBloque());
                if (bloqueDTO.getItAutorizaConsulta()!=null) {
                    rpta = Boolean.TRUE.equals(bloqueDTO.getItAutorizaConsulta()) ? "SI" : "NO";
                }
            }
        } catch(Exception e) {
            log.error(e.getMessage());
        }
        return rpta;
    }
    
    /**
     * Método para actualizar la calificación de un documento en estado pendiente revision subsanacion
     * Indice 1 = REVISION 
     *
     * @param indice Indice de campo a actualizar
     * @param dto Datos de requisito
     */
    public void actualizarCalificacion(Integer indice) {
        //Recargar solapa para habilitar o no botonoes
        
        PrimeFaces.current().ajax().update(this.solapaRevisionContratos);
    }
    
    
    public void descargardocumentoIDCA(ExpedienteDocDTO doc) throws ServiceException {
        try {
            byte[] contenidoPdf = null;

            if (doc != null) {

                ExpedienteDocDTO documento = this.expedienteDocService.buscarId(doc.getIdExpedienteDoc());
                
                Integer tipodoc = documento.getTipoDocumentoDTO().getIdValor();

                List<ConvocatoriaTipoDocDTO> tipoConvocatoriaDoc = this.convocatoriaTipoDocService.buscarPorConvocatoriaTipoDocumento(idConvocatoria, tipodoc);
                
                //aqui tengo que meter el nuevo valor de la columna ICDA
                
                if(tipoConvocatoriaDoc.get(0).getTipoICDA() != null) {
                    
                    Integer idValorDto = tipoConvocatoriaDoc.get(0).getTipoICDA().getIdValor();
                    
                    ConvocatoriaTipoDocDTO tipoDocConvocatoria =  tipoConvocatoriaDoc.get(0);


                    // 1. Evaluamos qué tipo de documento ICDA es y obtenemos los bytes
                    if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.IAE10))) {
    
                        contenidoPdf = consultarCertificadoIAE(documento, tipoDocConvocatoria);
    
                    } else if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.SVDDGPCIWS02))) {
    
                        contenidoPdf = consultarCertificadoIdentidad(documento, tipoDocConvocatoria);
    
                    } else if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.codeCENO))) {
    
                        contenidoPdf = consultarCertificadoCeno(documento, idValorDto, tipoDocConvocatoria);
    
                    } else if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.ECOT103))) {
    
                        contenidoPdf = consultarCertificadoEcot(documento, tipoDocConvocatoria);
    
                    } else if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.Q2827003ATGSS001))) {
    
                         contenidoPdf = consultarCertificadoTgss(documento, tipoDocConvocatoria);
    
                    } else if (Objects.equals(idValorDto, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumentoICDA.ORVASUB))) {
    
                        contenidoPdf = consultarCertificadoOrvasub(documento, idValorDto, tipoDocConvocatoria);
    
                    } else {
                        throw new ServiceException("Tipo de documento ICDA no contemplado o no soportado: " + idValorDto);
                    }
                
                 // 2. Si se han obtenido los bytes del PDF, preparamos metadatos, guardamos/actualizamos en FICA y descargamos
                    if (contenidoPdf != null && contenidoPdf.length > 0) {

                        String nombreArchivo = "Documento_ICDA_" + idValorDto + ".pdf";

                        // A) Construcción de los metadatos de FICA sobre el DTO existente
                        FicaMetadatos metadatos = new FicaMetadatos();
                        metadatos.setNombreFichero(nombreArchivo);
                        metadatos.setExtension("pdf");
                        metadatos.setFechaFichero(new Date());
                        metadatos.setUsuario(UserUtils.getUserName().toUpperCase());
                        metadatos.setEncriptar(false);

                        // Asignamos metadatos y contenido en el DTO
                        documento.setFicaMetadatos(metadatos);
                        documento.setContenidoFichero(contenidoPdf);

                        // Asignamos el estado 'REVISION_OK' por defecto si aplica en tu lógica
                        if (documento.getEstadoDTO() == null) {
                            documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                        }else{
                            documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)));
                        }
//                        else {
//                            documento.setEstadoDTO(this.catalogosValoresService.buscarId(
//                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)));
//                        }

                        // B) Subida/Actualización en FICA (crea o sobrescribe según si cdFica existe)
                        documento = this.expedienteDocService.subirDocumentoFica(documento);

                        // C) Guardado/Actualización de los cambios en BBDD
                        documento = this.expedienteDocService.guardar(documento);

                        // D) Descarga directa mediante UtilesWeb.openFile
                        UtilesWeb.openFile(
                                contenidoPdf,
                                metadatos.getExtension(),
                            FilenameUtils.removeExtension(metadatos.getNombreFichero())
                        );
                        
                        //PrimeFaces.current().ajax().update(this.solapaDocumentos);

                    } else {
                        AtlasMessageUtils.addWarnMessageFromBundle("No se ha podido obtener el contenido en bytes del documento.");
                    }
                }else {
                    AtlasMessageUtils.addWarnMessageFromBundle("No hay un codigo ICDA asociado a este tipo de documento.");
                    //throw new ServiceException("No hay un codigo ICDA asociado a este tipo de documento.");
                }

            } else {
                AtlasMessageUtils.addWarnMessageFromBundle("El documento recibido o sus datos de identificación son nulos.");
                //throw new ServiceException("El documento recibido o sus datos de identificación son nulos.");
            }

        } catch (ServiceException e) {
            log.error("Error ServiceException en descargardocumentoIDCA: " + e.getMessage(), e);
            //throw e;
            AtlasMessageUtils.addWarnMessageFromBundle("Error ServiceException en descargardocumentoIDCA: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error no controlado en descargardocumentoIDCA: " + e.getMessage(), e);
            //throw new ServiceException("Error al procesar, guardar en FICA o descargar el documento ICDA: " + e.getMessage(), e);
            AtlasMessageUtils.addWarnMessageFromBundle("Error al procesar, guardar en FICA o descargar el documento ICDA: " + e.getMessage());
        }
        
        PrimeFaces.current().ajax().update(this.solapaDocumentos);
    }

    
    /**
     * Construye los datos comunes del peticionario (aplicacion + usuario logado) que necesitan
     * todos los servicios ICDA, sacando el NIF y nombre del usuario de infoUsuarioBean segun si
     * es usuario de intranet o de internet.
     *
     * @param finalidad finalidad especifica de la consulta (varia segun el certificado solicitado)
     * @param tipoDocConvocatoria 
     */
    private IcdaDatosAppPeticionaria construirDatosAppPeticionaria(String finalidad, ConvocatoriaTipoDocDTO tipoDocConvocatoria) {
        IcdaDatosAppPeticionaria datosPeticionaria = new IcdaDatosAppPeticionaria();
        datosPeticionaria.setCdApp("SAII"); //SAII
        datosPeticionaria.setCdPeticionOrigen("1");
        datosPeticionaria.setCdOrganismoApp("ICM");
        datosPeticionaria.setFinalidad(finalidad);
        if(tipoDocConvocatoria.getItConsentimiento() != null && Boolean.TRUE.equals(tipoDocConvocatoria.getItConsentimiento())) {
            datosPeticionaria.setConsentimiento("Ley");
        }else {
            datosPeticionaria.setConsentimiento("Si");
        }
        datosPeticionaria.setCdUsuarioApp(this.infoUsuarioBean.getLogin());

        if (Boolean.TRUE.equals(this.infoUsuarioBean.getUsuarioIntranet())
                && this.infoUsuarioBean.getUsuPackUsuarioDto() != null) {
            datosPeticionaria.setNifUsuarioApp(this.infoUsuarioBean.getUsuPackUsuarioDto().getDni());
            datosPeticionaria.setNombreUsuarioApp(this.construirNombreCompleto(
                    this.infoUsuarioBean.getUsuPackUsuarioDto().getDsNombre(),
                    this.infoUsuarioBean.getUsuPackUsuarioDto().getDsApellido1(),
                    this.infoUsuarioBean.getUsuPackUsuarioDto().getDsApellido2()));
        } else if (Boolean.TRUE.equals(this.infoUsuarioBean.getUsuarioInternet())
                && this.infoUsuarioBean.getUsuiPackUsuarioDto() != null) {
            datosPeticionaria.setNifUsuarioApp(this.infoUsuarioBean.getUsuiPackUsuarioDto().getCdNif());
            datosPeticionaria.setNombreUsuarioApp(this.construirNombreCompleto(
                    this.infoUsuarioBean.getUsuiPackUsuarioDto().getDsNombre(),
                    this.infoUsuarioBean.getUsuiPackUsuarioDto().getDsApellido1(),
                    this.infoUsuarioBean.getUsuiPackUsuarioDto().getDsApellido2()));
        }else {
            datosPeticionaria.setNifUsuarioApp("00000002W");
            datosPeticionaria.setNombreUsuarioApp("ADMON");
        }

        return datosPeticionaria;
    }

    /**
     * Rellena los datos comunes del titular (tipo doc / num doc / nombre completo / nombre /
     * apellidos) a partir del interesado del expediente asociado al documento.
     *
     * @param titular objeto IcdaDatosBasicosTitular (o subclase) a rellenar
     * @param documento documento ICDA del que se cuelga el expediente
     * @throws ServiceException 
     */
    private void rellenarDatosBasicosTitular(IcdaDatosBasicosTitular titular, ExpedienteDocDTO documento, Integer idValorDto) throws ServiceException {
        if (documento == null || documento.getExpedienteDTO() == null
                || documento.getExpedienteDTO().getInteresadoDTO() == null) {
            return;
        }
        
        //ExpedienteDTO exp = this.expedienteService.buscarId(documento.getExpedienteDTO().getIdExpediente());
        
        ExpedienteDTO exp = this.expedienteService.buscarIdExpediente(documento.getExpedienteDTO().getIdExpediente());
        
        InteresadoDTO interesado = exp.getInteresadoDTO();
  
           if( interesado.getTipoActorDTO() != null && interesado.getTipoActorDTO().getIdValor() != null &&
                   interesado.getTipoActorDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Identificacion.JURIDICA))) {
               
               titular.setTipoDocTitular("CIF");
           }else{
               if (interesado.getTipoIdentificacionDTO() != null && interesado.getTipoIdentificacionDTO().getDsValor() != null) {
                   
                   String tipodocumento = interesado.getTipoIdentificacionDTO().getDsValor()
                           .replace(".", "")
                           .trim()
                           .toUpperCase();
                   
                   titular.setTipoDocTitular(tipodocumento);
                   
               }else {
                   titular.setTipoDocTitular("NIF");
               }
           }

        
        titular.setDocumentacionTitular(interesado.getCdNumIden());
        titular.setNombreTitular(interesado.getDsNombre());
        titular.setApellido1Titular(interesado.getDsApellido1());
        titular.setApellido2Titular(interesado.getDsApellido2());
        titular.setNombreCompletoTitular(StringUtils.isNotBlank(interesado.getDsInteresado())
                ? interesado.getDsInteresado()
                : this.construirNombreCompleto(interesado.getDsNombre(), interesado.getDsApellido1(),
                        interesado.getDsApellido2()));
    }
    
    
    private void rellenarDatosBasicosTitularRL(IcdaDatosBasicosTitular titular, ExpedienteDocDTO documento) throws ServiceException {
        if (documento == null || documento.getExpedienteDTO() == null
                || documento.getExpedienteDTO().getInteresadoDTO() == null) {
            return;
        }
        
        ExpedienteDTO exp = this.expedienteService.buscarId(documento.getExpedienteDTO().getIdExpediente());
        
        InteresadoDTO interesado = exp.getRepresentanteLegal();

        if (interesado.getTipoIdentificacionDTO() != null && interesado.getTipoIdentificacionDTO().getDsValor() != null) {
            
            String tipodocumento = interesado.getTipoIdentificacionDTO().getDsValor()
                    .replace(".", "")
                    .trim()
                    .toUpperCase();
            
            titular.setTipoDocTitular(tipodocumento);
            
        }else {
            titular.setTipoDocTitular("NIF");
        }
        titular.setDocumentacionTitular(interesado.getCdNumIden());
        titular.setNombreTitular(interesado.getDsNombre());
        titular.setApellido1Titular(interesado.getDsApellido1());
        titular.setApellido2Titular(interesado.getDsApellido2());
        titular.setNombreCompletoTitular(StringUtils.isNotBlank(interesado.getDsInteresado())
                ? interesado.getDsInteresado()
                : this.construirNombreCompleto(interesado.getDsNombre(), interesado.getDsApellido1(),
                        interesado.getDsApellido2()));
    }
    

    /**
     * Concatena nombre y apellidos ignorando los que vengan en blanco.
     */
    private String construirNombreCompleto(String nombre, String apellido1, String apellido2) {
        return Stream.of(nombre, apellido1, apellido2)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(" "));
    }

    public byte[] consultarCertificadoIAE(ExpedienteDocDTO documento, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("Pruebas", tipoDocConvocatoria);
            // unidadTramitadora es obligatorio segun el contrato de solicitudCertificadoIAE (a diferencia
            // del resto de metodos ICDA, que no lo usan), asi que no forma parte del builder comun.
            if (this.infoUsuarioBean.getUnidadAdmin() != null) {
                datosPeticionaria.setUnidadTramitadora(this.infoUsuarioBean.getUnidadAdmin().getDsUnidad());
            }

            IcdaDatosBasicosTitular datosBasicosTitular = new IcdaDatosBasicosTitular();
            this.rellenarDatosBasicosTitular(datosBasicosTitular, documento, null);

            IcdaSolicitudCertificadoIAEIN datosEntrada = new IcdaSolicitudCertificadoIAEIN();
            datosEntrada.setDatosAppPeticionaria(datosPeticionaria);
            datosEntrada.setDatosBasicosTitular(datosBasicosTitular);
            
            if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
                datosEntrada.setCdProcedimiento(tipoDocConvocatoria.getCodProcedimiento().getDsValor()); 
            }else {
                datosEntrada.setCdProcedimiento("PA2601"); 
            }            

            IcdaSolicitudCertificadoIAEOUT resultado = this.icdaService.solicitudCertificadoIAE(datosEntrada);

            String xmlOriginal = resultado.getXmlOriginal();
            this.comprobarErroresIcda(xmlOriginal);
            return this.generarPdfCertificadoIae(xmlOriginal);
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado de IAE: " + e.getMessage(), e);
        }
    }
    
    
    public byte[] consultarCertificadoTgss(ExpedienteDocDTO documento, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaSolicitudCertificadoSituacionDeudaOUT resultado =
                    this.consultarCertificadoSituacionDeudaTgss(documento,tipoDocConvocatoria );
            String rptaSituacionDeuda = resultado.getXmlOriginal();
            //log.info("solicitudCertificadoSituacionDeuda -> " + rptaSituacionDeuda);

            this.comprobarErroresIcda(rptaSituacionDeuda);

            return this.generarPdfCertificadoIcda("Corriente de pago en TGSS.", rptaSituacionDeuda);
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado de situación de deuda TGSS: "
                    + e.getMessage(), e);
        }
    }

    /**
     * Solicita el certificado de situación de deuda con la TGSS usando el envoltorio tipado
     * icdaTgssService (misma forma que consultarCertificadoIAE), en vez del cliente SOAP en crudo.
     * @param tipoDocConvocatoria 
     */
    public IcdaSolicitudCertificadoSituacionDeudaOUT consultarCertificadoSituacionDeudaTgss(
            ExpedienteDocDTO documento, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws Exception {

        IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("Pruebas", tipoDocConvocatoria);

        IcdaTitularCertificadoSituacionDeuda titular = new IcdaTitularCertificadoSituacionDeuda();
        this.rellenarDatosBasicosTitular(titular, documento, null);

        if(!titular.getNombreCompletoTitular().equals("") || titular.getNombreCompletoTitular() != null) {
            titular.setNombreTitular(null);
            titular.setApellido1Titular(null);
            titular.setApellido2Titular(null);
        }
        
        String procedimiento;
        if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
            procedimiento = tipoDocConvocatoria.getCodProcedimiento().getDsValor(); 
        }else {
            procedimiento = "PA2601"; 
        }  
        
        return this.icdaTgssService.solicitudCertificadoSituacionDeuda(
                datosPeticionaria, titular, procedimiento, null, null);
    }

    /**
     * Consulta el certificado de Identidad (DGP, SVDDGPCIWS02) usando el envoltorio tipado icdaService.
     * @param tipoDocConvocatoria 
     */
    public byte[] consultarCertificadoIdentidad(ExpedienteDocDTO documento, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("Pruebas", tipoDocConvocatoria );

            datosPeticionaria.setUnidadTramitadora("ICM");
            
            IcdaTitularConsultarIdentidad titular = new IcdaTitularConsultarIdentidad();
            this.rellenarDatosBasicosTitularRL(titular, documento);
            
            if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
                titular.setCdProcedimiento(tipoDocConvocatoria.getCodProcedimiento().getDsValor()); 
            }else {
                titular.setCdProcedimiento("PA2601"); 
            }    


            IcdaSolicitudConsultarIdentidadV3OUT resultado = this.icdaService.solicitudConsultarIdentidadv3(
                    datosPeticionaria, titular); 

            String xmlOriginal = resultado.getXmlOriginal();
            this.comprobarErroresIcda(xmlOriginal);
            return this.generarPdfCertificadoIdentidad(xmlOriginal);
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado de Identidad: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta el certificado CENO (Censo Único de Contribuyentes, identificación fiscal de
     * personas físicas o jurídicas) usando el envoltorio tipado icdaCmService.
     * @param tipoDocConvocatoria 
     */
    public byte[] consultarCertificadoCeno(ExpedienteDocDTO documento, Integer idValorDto, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("Pruebas ICM", tipoDocConvocatoria);

            // CENO no tiene un objeto titular propio: reutilizamos IcdaDatosBasicosTitular solo para
            // extraer tipoDoc/documentacion del interesado del expediente con la misma logica comun.
            IcdaDatosBasicosTitular titular = new IcdaDatosBasicosTitular();
            this.rellenarDatosBasicosTitular(titular, documento, idValorDto);
            
            
            String procedimiento;
            if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
                procedimiento = tipoDocConvocatoria.getCodProcedimiento().getDsValor(); 
            }else {
                procedimiento = "PA2601"; 
            }  

            IcdaSolicitarCertificadoCenoOUT resultado = this.icdaCmService.solicitarCertificadoCENO(
                    datosPeticionaria,
                    titular.getTipoDocTitular(),
                    titular.getDocumentacionTitular(),
                    procedimiento);        

            String xmlOriginal = resultado.getXmlOriginal();
            this.comprobarErroresIcda(xmlOriginal);
            return this.generarPdfCertificadoCeno(xmlOriginal);
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado CENO: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta el certificado de estar al corriente con la AEAT para subvenciones y ayudas
     * (ECOT103, de la familia ECOT101-104), usando el metodo generico solicitarCertificado de
     * icdaService. Mapeo de parametros confirmado contra el contrato del servicio: 3er parametro
     * es cdCertificado, 4o es cdProcedimiento.
     * @param tipoDocConvocatoria 
     */
    public byte[] consultarCertificadoEcot(ExpedienteDocDTO documento, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("PRUEBAS DE CORRIENTE DE PAGO", tipoDocConvocatoria);

            IcdaTitularSolicitarCertificado titular = new IcdaTitularSolicitarCertificado();
            this.rellenarDatosBasicosTitular(titular, documento, null);
            
            String procedimiento;
            if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
                procedimiento = tipoDocConvocatoria.getCodProcedimiento().getDsValor(); 
            }else {
                procedimiento = "PA2601"; 
            }  

            IcdaSolicitarCertificadoOUT resultado = this.icdaService.solicitarCertificado(
                    datosPeticionaria, titular,
                    "ECOT103",                       // cdCertificado
                    procedimiento);         

            String xmlOriginal = resultado.getXmlOriginal();
            this.comprobarErroresIcda(xmlOriginal);
            return this.generarPdfCertificadoEcot(xmlOriginal);
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado ECOT103: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta el certificado de estar al corriente de pago con la Comunidad de Madrid para
     * subvenciones (ORVASUB), usando el envoltorio tipado icdaCmService.
     * @param idValorDto 
     * @param tipoDocConvocatoria 
     */
    public byte[] consultarCertificadoOrvasub(ExpedienteDocDTO documento, Integer idValorDto, ConvocatoriaTipoDocDTO tipoDocConvocatoria) throws ServiceException {
        try {
            IcdaDatosAppPeticionaria datosPeticionaria = this.construirDatosAppPeticionaria("Pruebas", tipoDocConvocatoria);

            // ORVA tampoco tiene un objeto titular propio: mismo truco que en CENO.
            IcdaDatosBasicosTitular titular = new IcdaDatosBasicosTitular();
            this.rellenarDatosBasicosTitular(titular, documento, idValorDto);

            String procedimiento;
            if(tipoDocConvocatoria.getCodProcedimiento() != null && tipoDocConvocatoria.getCodProcedimiento().getIdValor() != null || !tipoDocConvocatoria.getCodProcedimiento().getDsValor().isEmpty()){
                procedimiento = tipoDocConvocatoria.getCodProcedimiento().getDsValor(); 
            }else {
                procedimiento = "PA2601"; 
            }  
            
            // Confirmado contra el contrato de ICDA_WS_CM: el wrapper llama a
            // solicitarCertificadoORVA(cdApp, nif, cdCertificado, cdPeticionOrigen, cdUsuarioApp,
            // nifUsuarioApp, nombreUsuarioApp, cdOrganismoApp, tipoDoc, finalidad, consentimiento,
            // cdProcedimiento), y estos 4 parametros del wrapper mapean, en este orden, a:
            // tipoDoc, nif (documentacionTitular), cdCertificado, cdProcedimiento.
            IcdaSolicitarCertificadoORVAOUT resultado = this.icdaCmService.solicitarCertificadoORVA(
                    datosPeticionaria,
                    titular.getTipoDocTitular(),
                    titular.getDocumentacionTitular(),
                    "ORVASUB",                        // cdCertificado: ORVASUB (subvenciones) u ORVACON (contratacion)
                    procedimiento);        

            String xmlOriginal = resultado.getXmlOriginal();
            this.comprobarErroresIcda(xmlOriginal);
            return this.generarPdfCertificadoOrvasub(xmlOriginal,titular.getNombreCompletoTitular());
        } catch (Exception e) {
            throw new ServiceException("Error al consultar el certificado ORVA: " + e.getMessage(), e);
        }
    }

    /**
     * Comprueba el bloque "certificado" de la respuesta de un metodo ICDA: si
     * codigo_error_certificado viene distinto de 0, clasifica error_certificado segun las
     * excepciones documentadas en los contratos de servicio ICDA (procedimiento no existe, sin
     * privilegios, aplicacion/certificado no dado de alta, tipo de documentacion no encontrado,
     * numero de documento/soporte no especificado o con formato erroneo, parametros incorrectos o
     * no especificados) y lanza una ServiceException.
     *
     * @param xmlRespuesta XML devuelto por el metodo ICDA (solicitud_sincrona / xmlOriginal)
     * @throws ServiceException si la respuesta indica un error de la peticion
     */
    private void comprobarErroresIcda(String xmlRespuesta) throws ServiceException {
        if (StringUtils.isBlank(xmlRespuesta)) {
            throw new ServiceException("La respuesta obtenida del servicio ICDA está vacía.");
        }

        // Extraer datos clave del XML
        String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
        if (StringUtils.isBlank(codigoCertificado)) {
            codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodigoCertificado");
        }

        String codigoErrorCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_error_certificado");
        String errorCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "error_certificado");
        
        String codigoEstado = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodigoEstado");
        String literalError = this.obtenerValorEtiquetaXml(xmlRespuesta, "LiteralError");

        // Construimos un mensaje general
        String mensajeError = StringUtils.isNotBlank(literalError) ? literalError : errorCertificado;
        String mensajeErrorLower = StringUtils.defaultString(mensajeError).toLowerCase();

        // -----------------------------------------------------------------------------------------
        // CASOS EXCEPCIONALES DE NEGOCIO (No se consideran fallos técnicos/de SOAP)
        // -----------------------------------------------------------------------------------------

        // A) Certificado de Estar al Corriente con la AEAT / Seguridad Social / Hacienda
        boolean esResultadoNegativoCorriente = 
                (mensajeErrorLower.contains("denegado") 
                || mensajeErrorLower.contains("no esta al corriente")
                || mensajeErrorLower.contains("no está al corriente")
                || mensajeErrorLower.contains("deuda")
                || mensajeErrorLower.contains("sin datos"));

        // B) Certificado de CENO (Censo Único AEAT)
        boolean esResultadoNegativoCeno = ("codeCENO".equalsIgnoreCase(codigoCertificado) || "CENO".equalsIgnoreCase(codigoCertificado))
                && (mensajeErrorLower.contains("no existe el contribuyente")
                    || mensajeErrorLower.contains("mas de un contribuyente")
                    || mensajeErrorLower.contains("más de un contribuyente")
                    || mensajeErrorLower.contains("no figura") 
                    || mensajeErrorLower.contains("no se encuentra"));

        // C) Certificado de Identidad (DGP / SVN)
        boolean esResultadoNegativoIdentidad = (codigoCertificado != null && (codigoCertificado.startsWith("SVD") || codigoCertificado.startsWith("DGP")))
                && (mensajeErrorLower.contains("no existe")
                    || mensajeErrorLower.contains("no identificado")
                    || mensajeErrorLower.contains("incorrecto")
                    || mensajeErrorLower.contains("no se ha podido realizar")
                    || "81".equals(codigoEstado)
                    || "02".equals(codigoEstado));

        // Si coincide con alguna respuesta funcional/de negocio esperada, NO lanzamos excepción
        if (esResultadoNegativoCorriente || esResultadoNegativoCeno || esResultadoNegativoIdentidad) {
            return;
        }

        // -----------------------------------------------------------------------------------------
        // CONTROL DE ERRORES TÉCNICOS / SOAP
        // -----------------------------------------------------------------------------------------
        if (("2".equalsIgnoreCase(codigoErrorCertificado) || "3".equalsIgnoreCase(codigoErrorCertificado)) 
                && StringUtils.isNotBlank(errorCertificado)) {
            throw new ServiceException("Error devuelto por la Plataforma de Intermediación: " + errorCertificado);
        }

        if (xmlRespuesta.contains("<Fault>") || xmlRespuesta.contains("<soapenv:Fault>")) {
            String faultString = this.obtenerValorEtiquetaXml(xmlRespuesta, "faultstring");
            if (StringUtils.isBlank(faultString)) {
                faultString = "Error SOAP genérico en la llamada al servicio ICDA.";
            }
            throw new ServiceException("Error SOAP en ICDA: " + faultString);
        }
    }
    
    
    /**
     * Extrae el contenido de texto de la primera etiqueta con el nombre indicado en un XML plano.
     *
     * @param xml XML de entrada
     * @param etiqueta nombre de la etiqueta a buscar (sin namespace)
     * @return el contenido de texto de la etiqueta, o null si no se encuentra o el XML no es valido
     */
    private String obtenerValorEtiquetaXml(String xml, String etiqueta) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
            NodeList nodos = doc.getElementsByTagName(etiqueta);
            if (nodos.getLength() > 0) {
                return nodos.item(0).getTextContent();
            }
        } catch (Exception e) {
            log.error("No se ha podido parsear la respuesta XML de ICDA_WS_TGSS: " + e.getMessage());
        }
        return null;
    }

    /**
     * Genera un PDF con el resultado de una consulta ICDA, con un formato similar al de la
     * herramienta oficial de casos de uso de ICDA. Valido para cualquier servicio ICDA cuya
     * respuesta siga la estructura comun "solicitud_sincrona" (certificado/emisor/solicitante/titular).
     *
     * @param tituloSeccion titulo especifico del tipo de consulta (ej. "Corriente de pago en TGSS")
     * @param xmlRespuesta XML devuelto por el metodo ICDA (solicitud_sincrona / xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
    private byte[] generarPdfCertificadoIcda(String tituloSeccion, String xmlRespuesta) throws ServiceException {
        try {
            // 1. Extracción de datos del XML
            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
            String fechaEmision = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_generacion");
            String idSolicitud = this.obtenerValorEtiquetaXml(xmlRespuesta, "identificador_solicitud");
            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");
            String tipoDocTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "tipo_doc");
            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");

            // Datos del nodo Retorno (TGSS)
            String resultado = this.obtenerValorEtiquetaXml(xmlRespuesta, "Resultado");
            String descripcionRetorno = this.obtenerValorEtiquetaXml(xmlRespuesta, "Descripcion");
            
            // Si no viene Descripción en Retorno, buscar el error genérico
            if (StringUtils.isBlank(descripcionRetorno)) {
                descripcionRetorno = this.obtenerValorEtiquetaXml(xmlRespuesta, "error_certificado");
            }

            // 2. Fuentes
            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7.5f, Font.ITALIC);

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Encabezado
            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Datos de la consulta. " + tituloSeccion, fuenteSeccion));
            documento.add(new Paragraph(" "));

            // Bloque 1: Datos de la Consulta
            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
            
            // Si hay error o no viene 'S'/'N', mostramos la fila "Respuesta" directamente
            if (!"S".equalsIgnoreCase(resultado) && !"N".equalsIgnoreCase(resultado)) {
                documento.add(this.crearFilaDato("Respuesta:", descripcionRetorno, fuenteEtiqueta, fuenteValor));
            }
            
            documento.add(this.crearFilaDato("Fecha Emisión:", fechaEmision, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Id. Solicitud:", idSolicitud, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 2: Datos del Emisor
            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 3: Datos del Titular
            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
            documento.add(this.crearFilaDato("Tipo Documentación:", tipoDocTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Documentación:", documentacionTitular, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 4: Párrafo legal del Certificado (si el resultado es S o N)
            if ("S".equalsIgnoreCase(resultado) || "N".equalsIgnoreCase(resultado)) {
                boolean esSi = "S".equalsIgnoreCase(resultado);
                String estadoTexto = esSi ? "SI" : "NO";

                Paragraph pCert1 = new Paragraph();
                pCert1.setFont(fuenteValor);
                pCert1.add("El emisor CERTIFICA que el titular arriba referenciado ");
                pCert1.add(new Chunk(estadoTexto, fuenteEtiqueta)); // "SI" o "NO" en negrita
                pCert1.add(" está al corriente de pago de sus obligaciones con la Tesorería General de la Seguridad Social.");
                pCert1.setSpacingAfter(8f);
                documento.add(pCert1);

                Paragraph pCert2 = new Paragraph();
                pCert2.setFont(fuenteValor);
                pCert2.add("El presente certificado no originará derechos ni expectativas de derechos a favor del solicitante o de terceros, ni podrá ser invocado para interrupción o paralización de plazos de caducidad o prescripción ni servirá de medio de notificación de los expedientes a que pudiera hacer referencia, no afectando a lo que pudiere resultar de actuaciones posteriores de comprobación o investigación al respecto.");
                pCert2.setSpacingAfter(15f);
                documento.add(pCert2);
            }

            // Bloque 5: Nota legal y Solicitante
            Paragraph pNota = new Paragraph();
            pNota.setFont(fuenteNota);
            pNota.add("Esta consulta de datos se realiza en cumplimiento del ");
            pNota.add(new Chunk("artículo 28.2 de la ley 39/2015", new Font(FontFamily.HELVETICA, 7.5f, Font.BOLDITALIC)));
            pNota.add(" del Procedimiento Administrativo Común de las Administraciones Públicas previa no oposición del interesado");
            pNota.setSpacingAfter(10f);
            documento.add(pNota);

            documento.add(this.crearFilaDato("Nombre del Solicitante", nombreSolicitante, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha de consulta",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado ICDA (TGSS): " + e.getMessage());
        }
    }

    /**
     * Crea una fila de dos columnas (etiqueta + valor) sin bordes, para el PDF del certificado.
     */
    private PdfPTable crearFilaDato(String etiqueta, String valor, Font fuenteEtiqueta, Font fuenteValor) {
        PdfPTable tabla = new PdfPTable(new float[] { 1f, 2f });
        tabla.setWidthPercentage(100);

        PdfPCell celdaEtiqueta = new PdfPCell(new Phrase(etiqueta, fuenteEtiqueta));
        celdaEtiqueta.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaEtiqueta);

        PdfPCell celdaValor = new PdfPCell(new Phrase(StringUtils.defaultString(valor), fuenteValor));
        celdaValor.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaValor);

        return tabla;
    }

    /**
     * Genera el PDF del certificado CENO (Censo Único de Contribuyentes / "NIF de Empresa"), con un
     * formato distinto al generico (sin Id. Solicitud, con Nombre/Razón Social, Domicilio fiscal y
     * Fecha Alta del titular, y un texto legal distinto: artículo 9 de la ley 11/2007, no el 28.2 de
     * la 39/2015 que usan TGSS/resto).
     *
     * TODO: confirmar contra una respuesta real de CENO las etiquetas XML de "estado_solicitud"
     * (Respuesta: "TRAMITADA"), "razon_social", "domicilio_fiscal"/"localidad_fiscal" y "fecha_alta"
     * - de momento son una suposicion razonable siguiendo la convencion snake_case del resto de tags
     * ya confirmados (nif_emisor, nombre_emisor, tipo_doc, documentacion, nombre_solicitante).
     *
     * @param xmlRespuesta XML devuelto por solicitarCertificadoCENO (xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
//    private byte[] generarPdfCertificadoCeno(String xmlRespuesta) throws ServiceException {
//        try {
//            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
//            String respuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "estado_solicitud");
//            String fechaEmision = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_generacion");
//            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
//            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");
//            String tipoDocTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "tipo_doc");
//            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
//            String razonSocialTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "razon_social");
//            String domicilioFiscal = this.obtenerValorEtiquetaXml(xmlRespuesta, "domicilio_fiscal");
//            String localidadFiscal = this.obtenerValorEtiquetaXml(xmlRespuesta, "localidad_fiscal");
//            String fechaAlta = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_alta");
//            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");
//
//            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
//            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
//            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
//            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
//            Font fuenteNota = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);
//
//            ByteArrayOutputStream salida = new ByteArrayOutputStream();
//            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
//            PdfWriter.getInstance(documento, salida);
//            documento.open();
//
//            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
//            documento.add(new Paragraph(" "));
//            documento.add(new Paragraph("Datos de la consulta. NIF de Empresa.", fuenteSeccion));
//            documento.add(new Paragraph(" "));
//            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Respuesta:", respuesta, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha Emisión:", fechaEmision, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
//            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
//            documento.add(this.crearFilaDato("Tipo Documentación:", tipoDocTitular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Documentación:", documentacionTitular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre/Razón Social:", razonSocialTitular, fuenteEtiqueta, fuenteValor));
//            String domicilioCompleto = Stream.of(domicilioFiscal, localidadFiscal)
//                    .filter(StringUtils::isNotBlank)
//                    .collect(Collectors.joining("\n"));
//            documento.add(this.crearFilaDato("Domicilio fiscal:", domicilioCompleto, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha Alta:", fechaAlta, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Esta consulta de datos se realiza en cumplimiento del artículo 9 de la ley "
//                    + "11/2007 de acceso electrónico de los ciudadanos a los servicios públicos previo el "
//                    + "consentimiento del interesado", fuenteNota));
//            documento.add(new Paragraph(" "));
//            documento.add(this.crearFilaDato("Nombre del Solicitante:", nombreSolicitante, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha de consulta:",
//                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));
//
//            documento.close();
//            return salida.toByteArray();
//        } catch (Exception e) {
//            throw new ServiceException("No se ha podido generar el PDF del certificado CENO: " + e.getMessage());
//        }
//    }
    
    private byte[] generarPdfCertificadoCeno(String xmlRespuesta) throws ServiceException {
        try {
            // 1. Metadatos del Certificado
            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
            
            // Respuesta / Estado (en este XML viene en <Message>Correcto</Message>)
            String respuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "Message");
            if (StringUtils.isBlank(respuesta)) {
                respuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "error_certificado");
            }
            if ("Correcto".equalsIgnoreCase(respuesta) || "Información correcta".equalsIgnoreCase(respuesta)) {
                respuesta = "TRAMITADA";
            }

            // Fecha Emisión
            String fechaEmisionRaw = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_generacion");
            String fechaEmision = formatearFechaIso(fechaEmisionRaw);

            // 2. Datos del Emisor (Valores por defecto de la AEAT / DGT)
            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
            if (StringUtils.isBlank(nifEmisor)) {
                nifEmisor = "S-7800003-A";
            }
            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");
            if (StringUtils.isBlank(nombreEmisor)) {
                nombreEmisor = "Dirección General de Tributos";
            }

            // 3. Datos del Titular
            String tipoDoc = this.obtenerValorEtiquetaXml(xmlRespuesta, "tipo_doc");
            String documentacion = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");

            // Nombre / Razón Social: 
            // Si RazonSocial viene vacía (persona física), concatenamos Nombre + Apellido1 + Apellido2
            String razonSocial = this.obtenerValorEtiquetaXml(xmlRespuesta, "RazonSocial");
            if (StringUtils.isBlank(razonSocial)) {
                String nom = this.obtenerValorEtiquetaXml(xmlRespuesta, "Nombre");
                String ap1 = this.obtenerValorEtiquetaXml(xmlRespuesta, "Apellido1");
                String ap2 = this.obtenerValorEtiquetaXml(xmlRespuesta, "Apellido2");
                
                // Si Nombre traía la cadena completa (como en AEATDatosMaestros), usamos esa, 
                // sino construimos la combinación de los 3 campos.
                if (StringUtils.isNotBlank(nom) && StringUtils.isNotBlank(ap1)) {
                    razonSocial = (nom + " " + ap1 + " " + ap2).replaceAll("\\s+", " ").trim();
                } else if (StringUtils.isNotBlank(nom)) {
                    razonSocial = nom.trim();
                }
            }

            // 4. Domicilio Fiscal (desde AEATDomicilioNacional)
            String tipoVia = this.obtenerValorEtiquetaXml(xmlRespuesta, "TiposVia");
            String nombreVia = this.obtenerValorEtiquetaXml(xmlRespuesta, "NombreVia");
            String numero = this.obtenerValorEtiquetaXml(xmlRespuesta, "NumeroVia");
            String piso = this.obtenerValorEtiquetaXml(xmlRespuesta, "Piso");
            String puerta = this.obtenerValorEtiquetaXml(xmlRespuesta, "Puerta");
            String escalera = this.obtenerValorEtiquetaXml(xmlRespuesta, "Escalera");
            String codPostal = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodigoPostal");
            String municipio = this.obtenerValorEtiquetaXml(xmlRespuesta, "Municipio");
            String provincia = this.obtenerValorEtiquetaXml(xmlRespuesta, "Provincia");

            // Construcción de la dirección en 2 líneas
            StringBuilder sbDom1 = new StringBuilder();
            if (StringUtils.isNotBlank(tipoVia)) sbDom1.append(tipoVia).append(" ");
            if (StringUtils.isNotBlank(nombreVia)) sbDom1.append(nombreVia).append(" ");
            if (StringUtils.isNotBlank(numero)) sbDom1.append(numero).append(" ");
            if (StringUtils.isNotBlank(escalera)) sbDom1.append("Esc. ").append(escalera).append(" ");
            if (StringUtils.isNotBlank(piso)) sbDom1.append(piso).append(" ");
            if (StringUtils.isNotBlank(puerta)) sbDom1.append(puerta);
            String domicilioLinea1 = sbDom1.toString().replaceAll("\\s+", " ").trim();

            StringBuilder sbDom2 = new StringBuilder();
            if (StringUtils.isNotBlank(codPostal)) sbDom2.append(codPostal).append(" ");
            if (StringUtils.isNotBlank(municipio)) sbDom2.append(municipio).append(" ");
            if (StringUtils.isNotBlank(provincia)) sbDom2.append(provincia);
            String domicilioLinea2 = sbDom2.toString().replaceAll("\\s+", " ").trim();

            // Fecha Alta
            String fechaAlta = formatearFechaIso(this.obtenerValorEtiquetaXml(xmlRespuesta, "FechaAlta"));

            // 5. Generación del Documento PDF
            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Encabezado
            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Datos de la consulta. NIF de Empresa.", fuenteSeccion));
            documento.add(new Paragraph(" "));
            
            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Respuesta:", respuesta, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha Emisión:", fechaEmision, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Datos del Emisor
            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Datos del Titular
            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
            documento.add(this.crearFilaDato("Tipo Documentación:", tipoDoc, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Documentación:", documentacion, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre/Razón Social:", razonSocial, fuenteEtiqueta, fuenteValor));

            // Domicilio Fiscal
            if (StringUtils.isNotBlank(domicilioLinea1)) {
                documento.add(this.crearFilaDato("Domicilio fiscal:", domicilioLinea1, fuenteEtiqueta, fuenteValor));
                if (StringUtils.isNotBlank(domicilioLinea2)) {
                    documento.add(this.crearFilaDato("", domicilioLinea2, fuenteEtiqueta, fuenteValor));
                }
            }

            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Fecha Alta:", fechaAlta, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Pie Legal
            documento.add(new Paragraph("Esta consulta de datos se realiza en cumplimiento del artículo 28.2 de la ley "
                    + "39/2015 del Procedimiento Administrativo Común de las Administraciones Públicas previa no oposición del interesado", fuenteNota));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado de CENO: " + e.getMessage());
        }
    }
    

    /**
     * Genera el PDF del certificado ECOT103 (AEAT, corriente de pago para subvenciones públicas).
     * Layout distinto al resto: incluye Id. Solicitud + "Cód Respuesta" (código y descripción
     * combinados), un campo "Referencia", el desglose Nombre Completo/Nombre/Apellidos del titular,
     * y un parrafo largo de certificación (el texto "El Emisor CERTIFICA...") antes del disclaimer
     * final, que en este caso SÍ coincide con el de TGSS (artículo 28.2 de la ley 39/2015).
     *
     * TODO: confirmar contra una respuesta real de ECOT103 las etiquetas XML de "codigo_respuesta"/
     * "descripcion_respuesta" (Cód Respuesta: "1000 - Correcto"), "referencia" y
     * "nombre_completo_titular" - de momento son una suposicion siguiendo la convencion del resto de
     * tags ya confirmados. El parrafo largo de certificación reutiliza la etiqueta "Descripcion" que
     * ya usamos para el resultado de negocio de TGSS (Retorno/Descripcion).
     *
     * @param xmlRespuesta XML devuelto por solicitarCertificado (xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
    private byte[] generarPdfCertificadoEcot(String xmlRespuesta) throws ServiceException {
        try {
            // 1. Extracción de datos del XML
            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
            String idSolicitud = this.obtenerValorEtiquetaXml(xmlRespuesta, "identificador_solicitud");
            String codRet = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodRet");
            String descError = this.obtenerValorEtiquetaXml(xmlRespuesta, "DescripcionError");
            String codRespuesta = Stream.of(codRet, descError)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(" - "));

            String referencia = this.obtenerValorEtiquetaXml(xmlRespuesta, "Referencia");
            String fechaEmision = this.obtenerValorEtiquetaXml(xmlRespuesta, "FechaEmision");
            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");

            String tipoDocTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "tipo_doc");
            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
            String nombreCompletoTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_completo_titular");
            String nombreTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_titular");
            String apellido1Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "apellido1_titular");
            String apellido2Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "apellido2_titular");

            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");
            
            // Datos de la certificación (POSITIVO / NEGATIVO)
            String resultadoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodigoCertificado");
            String causaNegatividad = this.obtenerValorEtiquetaXml(xmlRespuesta, "CausaNegatividad");
            boolean esPositivo = "POSITIVO".equalsIgnoreCase(resultadoCertificado);

            // 2. Definición de fuentes
            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteSubtitulo = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7.5f, Font.ITALIC);

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 30, 30);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Cabecera
            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
            Paragraph sub = new Paragraph("Datos de la consulta. Corriente de pago en AEAT. Subvenciones públicas.", fuenteSubtitulo);
            sub.setSpacingAfter(10f);
            documento.add(sub);

            // Bloque 1: Datos de la Consulta
            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Id. Solicitud:", idSolicitud, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Cód Respuesta:", codRespuesta, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Referencia:", referencia, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha Emisión:", fechaEmision, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 2: Datos del Emisor
            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 3: Datos del Titular
            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
            documento.add(this.crearFilaDato("Tipo Documentación:", tipoDocTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Documentación:", documentacionTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre Completo:", nombreCompletoTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Primer apellido:", apellido1Titular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Segundo apellido:", apellido2Titular, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque 4: Párrafos de la certificación (Dinámico POSITIVO / NEGATIVO)
            String estadoTexto = esPositivo ? "SI" : "NO";
            String caracterTexto = esPositivo ? "POSITIVO" : "NEGATIVO";

            Paragraph pCert1 = new Paragraph();
            pCert1.setFont(fuenteValor);
            pCert1.add("El Emisor CERTIFICA: Que conforme a los datos que obran en este, el titular arriba referenciado ");
            pCert1.add(new Chunk(estadoTexto, fuenteEtiqueta)); // "SI" o "NO" en negrita
            pCert1.add(" se encuentra al corriente de sus obligaciones tributarias de conformidad con lo dispuesto en el artículo 74 del Reglamento General de las actuaciones y los procedimientos de gestión e inspección tributaria y de desarrollo de las normas comunes de los procedimientos de aplicación de los tributos, aprobado por el Real Decreto 1065/2007, de 27 de julio.");
            pCert1.setSpacingAfter(8f);
            documento.add(pCert1);

            Paragraph pCert2 = new Paragraph();
            pCert2.setFont(fuenteValor);
            pCert2.add("El presente certificado se expide a petición del interesado, tiene carácter ");
            pCert2.add(new Chunk(caracterTexto, fuenteEtiqueta)); // "POSITIVO" o "NEGATIVO" en negrita
            pCert2.add(" y una validez de seis meses contados desde la fecha de su expedición, se expide al efecto exclusivo mencionado y no origina derechos ni expectativas de derechos a favor del solicitante ni de terceros, no pudiendo ser invocado a efectos de interrupción o de paralización de los plazos de caducidad o prescripción, ni servirá de medio de notificación de los expedientes a los que pudiera hacer referencia, sin que su contenido pueda afectar al resultado de actuaciones posteriores de comprobación o investigación. Todo ello, de conformidad con lo dispuesto en la norma citada.");
            
            // Si es negativo y hay causa, se puede añadir a continuación
            if (!esPositivo && StringUtils.isNotBlank(causaNegatividad)) {
                pCert2.add("\nCausa: " + causaNegatividad);
            }
            pCert2.setSpacingAfter(15f);
            documento.add(pCert2);

            // Bloque 5: Nota legal y Solicitante
            Paragraph pNota = new Paragraph();
            pNota.setFont(fuenteNota);
            pNota.add("Esta consulta de datos se realiza en cumplimiento del ");
            pNota.add(new Chunk("artículo 28.2 de la ley 39/2015", new Font(FontFamily.HELVETICA, 7.5f, Font.BOLDITALIC)));
            pNota.add(" del Procedimiento Administrativo Común de las Administraciones Públicas previa no oposición del interesado");
            pNota.setSpacingAfter(10f);
            documento.add(pNota);

            documento.add(this.crearFilaDato("Nombre del Solicitante", nombreSolicitante, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha de consulta",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado ECOT101/103: " + e.getMessage());
        }
    }
    
    
    /**
     * Extrae todos los elementos con el nombre de etiqueta indicado de un XML plano (para
     * estructuras repetidas, ej. los epígrafes del certificado de IAE).
     *
     * @param xml XML de entrada
     * @param etiqueta nombre de la etiqueta a buscar (sin namespace)
     * @return la lista de elementos encontrados (vacía si no hay ninguno o el XML no es válido)
     */
    private List<Element> obtenerElementosXml(String xml, String etiqueta) {
        List<Element> elementos = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));
            NodeList nodos = doc.getElementsByTagName(etiqueta);
            for (int i = 0; i < nodos.getLength(); i++) {
                if (nodos.item(i) instanceof Element) {
                    elementos.add((Element) nodos.item(i));
                }
            }
        } catch (Exception e) {
            log.error("No se ha podido parsear la respuesta XML de ICDA_WS_TGSS: " + e.getMessage());
        }
        return elementos;
    }

    /**
     * Extrae el contenido de texto de la primera etiqueta hija con el nombre indicado, dentro de
     * un elemento concreto (para leer los campos de un elemento repetido, ej. un epígrafe de IAE).
     */
    private String obtenerValorEtiquetaXml(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        if (nodos.getLength() > 0) {
            return nodos.item(0).getTextContent();
        }
        return null;
    }

    /**
     * Crea una fila con dos pares etiqueta/valor uno junto al otro (ej. "Nacionalidad" y "Sexo" en
     * la misma línea), sin bordes, para el PDF del certificado.
     */
    private PdfPTable crearFilaDobleDato(String etiqueta1, String valor1, String etiqueta2, String valor2,
            Font fuenteEtiqueta, Font fuenteValor) {
        PdfPTable tabla = new PdfPTable(new float[] { 1f, 2f, 1f, 2f });
        tabla.setWidthPercentage(100);

        PdfPCell celdaEtiqueta1 = new PdfPCell(new Phrase(etiqueta1, fuenteEtiqueta));
        celdaEtiqueta1.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaEtiqueta1);

        PdfPCell celdaValor1 = new PdfPCell(new Phrase(StringUtils.defaultString(valor1), fuenteValor));
        celdaValor1.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaValor1);

        PdfPCell celdaEtiqueta2 = new PdfPCell(new Phrase(etiqueta2, fuenteEtiqueta));
        celdaEtiqueta2.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaEtiqueta2);

        PdfPCell celdaValor2 = new PdfPCell(new Phrase(StringUtils.defaultString(valor2), fuenteValor));
        celdaValor2.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celdaValor2);

        return tabla;
    }

    /**
     * Genera el PDF del certificado de Identidad (SVDDGPCIWS02, DGP). Layout distinto al resto:
     * "Id. Petición" en vez de "Id. Solicitud", Respuesta recuadrada ("INFORMACION CORRECTA"),
     * datos del titular ampliados (número de soporte, apellidos, nacionalidad y sexo en la misma
     * fila, nombre del padre/madre, fecha de caducidad) y un bloque adicional de datos de
     * nacimiento (fecha, localidad, provincia). El texto legal es el mismo que en CENO: artículo 9
     * de la ley 11/2007.
     *
     * TODO: confirmar contra una respuesta real de Identidad las etiquetas XML de
     * "identificador_peticion", "numero_soporte", "nacionalidad", "sexo", "nombre_padre",
     * "nombre_madre", "fecha_caducidad", "fecha_nacimiento", "localidad_nacimiento" y
     * "provincia_nacimiento" - de momento son una suposición razonable siguiendo la convención
     * snake_case del resto de tags ya confirmados.
     *
     * @param xmlRespuesta XML devuelto por solicitudConsultarIdentidad (xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
//    private byte[] generarPdfCertificadoIdentidad(String xmlRespuesta) throws ServiceException {
//        try {
//            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
//            String idPeticion = this.obtenerValorEtiquetaXml(xmlRespuesta, "identificador_peticion");
//            String fechaEmision = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_generacion");
//            String respuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "estado_solicitud");
//            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
//            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");
//            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
//            String numeroSoporte = this.obtenerValorEtiquetaXml(xmlRespuesta, "numero_soporte");
//            String nombreTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_titular");
//            String apellido1Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "apellido1_titular");
//            String apellido2Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "apellido2_titular");
//            String nacionalidad = this.obtenerValorEtiquetaXml(xmlRespuesta, "nacionalidad");
//            String sexo = this.obtenerValorEtiquetaXml(xmlRespuesta, "sexo");
//            String nombrePadre = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_padre");
//            String nombreMadre = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_madre");
//            String fechaCaducidad = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_caducidad");
//            String fechaNacimiento = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_nacimiento");
//            String localidadNacimiento = this.obtenerValorEtiquetaXml(xmlRespuesta, "localidad_nacimiento");
//            String provinciaNacimiento = this.obtenerValorEtiquetaXml(xmlRespuesta, "provincia_nacimiento");
//            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");
//
//            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
//            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
//            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
//            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
//            Font fuenteNota = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);
//
//            ByteArrayOutputStream salida = new ByteArrayOutputStream();
//            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
//            PdfWriter.getInstance(documento, salida);
//            documento.open();
//
//            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
//            documento.add(new Paragraph(" "));
//            documento.add(new Paragraph("Datos de la consulta de Identidad.", fuenteSeccion));
//            documento.add(new Paragraph(" "));
//            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Id. Petición:", idPeticion, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha:", fechaEmision, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Respuesta:", respuesta, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
//            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
//            documento.add(this.crearFilaDato("Identificador:", documentacionTitular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Número de soporte:", numeroSoporte, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre:", nombreTitular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Primer apellido:", apellido1Titular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Segundo apellido:", apellido2Titular, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDobleDato("Nacionalidad:", nacionalidad, "Sexo:", sexo,
//                    fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre del Padre:", nombrePadre, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Nombre de la Madre:", nombreMadre, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha Caducidad:", fechaCaducidad, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Datos Nacimiento del Titular", fuenteSeccion));
//            documento.add(this.crearFilaDato("Fecha:", fechaNacimiento, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Localidad:", localidadNacimiento, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Provincia:", provinciaNacimiento, fuenteEtiqueta, fuenteValor));
//            documento.add(new Paragraph(" "));
//
//            documento.add(new Paragraph("Esta consulta de datos se realiza en cumplimiento del artículo 9 de la ley "
//                    + "11/2007 de acceso electrónico de los ciudadanos a los servicios públicos previo el "
//                    + "consentimiento del interesado", fuenteNota));
//            documento.add(new Paragraph(" "));
//            documento.add(this.crearFilaDato("Nombre del Solicitante:", nombreSolicitante, fuenteEtiqueta, fuenteValor));
//            documento.add(this.crearFilaDato("Fecha de consulta:",
//                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));
//
//            documento.close();
//            return salida.toByteArray();
//        } catch (Exception e) {
//            throw new ServiceException("No se ha podido generar el PDF del certificado de Identidad: " + e.getMessage());
//        }
//    }
    
    private byte[] generarPdfCertificadoIdentidad(String xmlRespuesta) throws ServiceException {
        try {
            // 1. Metadatos del Certificado
            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
            if (StringUtils.isBlank(codigoCertificado)) {
                codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodigoCertificado");
            }

            String idPeticion = this.obtenerValorEtiquetaXml(xmlRespuesta, "IdTransmision");
            if (StringUtils.isBlank(idPeticion)) {
                idPeticion = this.obtenerValorEtiquetaXml(xmlRespuesta, "IdSolicitud");
            }

            String fechaEmisionRaw = this.obtenerValorEtiquetaXml(xmlRespuesta, "FechaGeneracion");
            String fechaEmision = formatearFechaIso(fechaEmisionRaw);

            // 2. Estado / Respuesta
            String respuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "LiteralError");
            if (StringUtils.isBlank(respuesta) || "INFORMACION CORRECTA".equalsIgnoreCase(respuesta)) {
                String errCert = this.obtenerValorEtiquetaXml(xmlRespuesta, "error_certificado");
                respuesta = StringUtils.isNotBlank(errCert) ? errCert : "INFORMACION CORRECTA";
            }

            // 3. Datos del Emisor
            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "NifEmisor");
            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "NombreEmisor");

            // 4. Datos del Titular
            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "Documentacion");
            String numeroSoporte = this.obtenerValorEtiquetaXml(xmlRespuesta, "NumeroSoporte");

            String nombreTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "Nombre");
            String apellido1Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "Apellido1");
            String apellido2Titular = this.obtenerValorEtiquetaXml(xmlRespuesta, "Apellido2");

            String nacionalidad = this.obtenerValorEtiquetaXml(xmlRespuesta, "Nacionalidad");
            String sexo = this.obtenerValorEtiquetaXml(xmlRespuesta, "Sexo");
            String nombrePadre = this.obtenerValorEtiquetaXml(xmlRespuesta, "NombrePadre");
            String nombreMadre = this.obtenerValorEtiquetaXml(xmlRespuesta, "NombreMadre");

            // Fechas de Caducidad y Nacimiento (vienen como AAAAMMDD)
            String fechaCaducidadRaw = this.obtenerValorEtiquetaXml(xmlRespuesta, "FechaCaducidad");
            String fechaCaducidad = ("00000000".equals(fechaCaducidadRaw) || StringUtils.isBlank(fechaCaducidadRaw)) 
                    ? "" 
                    : formatearFechaAAAAMMDD(fechaCaducidadRaw);

            String fechaNacimientoRaw = this.obtenerValorEtiquetaXml(xmlRespuesta, "Fecha"); 
            String fechaNacimiento = formatearFechaAAAAMMDD(fechaNacimientoRaw);

            String localidadNacimiento = this.obtenerValorEtiquetaXml(xmlRespuesta, "Localidad");
            String provinciaNacimiento = this.obtenerValorEtiquetaXml(xmlRespuesta, "Provincia");

            // 5. Solicitante
            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "NombreSolicitante");

            // 6. Construcción del PDF
            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Encabezado y Metadatos
            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Datos de la consulta de Identidad.", fuenteSeccion));
            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Id. Petición:", idPeticion, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha:", fechaEmision, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Respuesta:", respuesta, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Datos del Emisor
            documento.add(new Paragraph("Datos del Emisor", fuenteSeccion));
            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Datos del Titular
            documento.add(new Paragraph("Datos del Titular", fuenteSeccion));
            documento.add(this.crearFilaDato("Identificador:", documentacionTitular, fuenteEtiqueta, fuenteValor));
            if (StringUtils.isNotBlank(numeroSoporte)) {
                documento.add(this.crearFilaDato("Número de soporte:", numeroSoporte, fuenteEtiqueta, fuenteValor));
            }
            documento.add(this.crearFilaDato("Nombre:", nombreTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Primer apellido:", apellido1Titular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Segundo apellido:", apellido2Titular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDobleDato("Nacionalidad:", nacionalidad, "Sexo:", sexo, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre del Padre:", nombrePadre, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre de la Madre:", nombreMadre, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha Caducidad:", fechaCaducidad, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Datos de Nacimiento
            documento.add(new Paragraph("Datos Nacimiento del Titular", fuenteSeccion));
            documento.add(this.crearFilaDato("Fecha:", fechaNacimiento, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Localidad:", localidadNacimiento, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Provincia:", provinciaNacimiento, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Pie Legal y Solicitante
            documento.add(new Paragraph("Esta consulta de datos se realiza en cumplimiento del artículo 9 de la ley "
                    + "11/2007 de acceso electrónico de los ciudadanos a los servicios públicos previo el "
                    + "consentimiento del interesado", fuenteNota));
            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Nombre del Solicitante:", nombreSolicitante, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha de consulta:",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado de Identidad: " + e.getMessage());
        }
    }
    
    

    private String formatearFechaAAAAMMDD(String fechaRaw) {
        if (StringUtils.isNotBlank(fechaRaw) && fechaRaw.trim().length() == 8) {
            try {
                String anio = fechaRaw.substring(0, 4);
                String mes = fechaRaw.substring(4, 6);
                String dia = fechaRaw.substring(6, 8);
                return dia + "/" + mes + "/" + anio;
            } catch (Exception e) {
                return fechaRaw;
            }
        }
        return fechaRaw;
    }
    
 // Limpia valores nulos o cadenas "null" del XML
    private String obtenerValorLimpio(String xml, String etiqueta) {
        String valor = this.obtenerValorEtiquetaXml(xml, etiqueta);
        if (StringUtils.isBlank(valor) || "null".equalsIgnoreCase(valor.trim())) {
            return "";
        }
        return valor.trim();
    }

    // Formatea fechas ISO "2015-10-23T13:44:54.790+02:00" a "23/10/2015"
    private String formatearFechaIso(String fechaIso) {
        if (StringUtils.isNotBlank(fechaIso)) {
            try {
                if (fechaIso.contains("T")) {
                    String fechaSolo = fechaIso.split("T")[0];
                    String[] partes = fechaSolo.split("-");
                    if (partes.length == 3) {
                        return partes[2] + "/" + partes[1] + "/" + partes[0];
                    }
                }
            } catch (Exception e) {
                return fechaIso;
            }
        }
        return fechaIso;
    }


    
    /**
     * Genera el PDF del certificado de IAE (Impuesto de Actividades Económicas, AEAT). Layout
     * distinto al resto: incluye "Cód Respuesta" ("TRAMITADA"), un campo "Referencia", el párrafo
     * introductorio de la AEAT ("...figura en el censo... siguientes epígrafes:") y una tabla
     * repetida con los epígrafes del titular (Sección, Epígrafe + Descripción, Fecha de Inicio). El
     * texto legal es el mismo que en TGSS/ECOT: artículo 28.2 de la ley 39/2015.
     *
     * TODO: confirmar contra una respuesta real de IAE10 las etiquetas XML de "codigo_respuesta"/
     * "descripcion_respuesta", "referencia" y, dentro de cada elemento repetido "epigrafe":
     * "seccion", "codigo_epigrafe", "descripcion_epigrafe" y "fecha_inicio" - de momento son una
     * suposición razonable siguiendo la convención del resto de tags ya confirmados.
     *
     * @param xmlRespuesta XML devuelto por solicitudCertificadoIAE (xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
    private byte[] generarPdfCertificadoIae(String xmlRespuesta) throws ServiceException {
        try {
            // 1. Extracción de datos del XML
            String codigoCertificado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_certificado");
            
            // Mapeo del estado/respuesta
            String codigoRespuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "CodRet");
            String descripcionRespuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "LiteralError");
            if (StringUtils.isBlank(descripcionRespuesta)) {
                descripcionRespuesta = this.obtenerValorEtiquetaXml(xmlRespuesta, "DescripcionError");
            }
            String codRespuesta = Stream.of(codigoRespuesta, descripcionRespuesta)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(" - "));
            if (StringUtils.containsIgnoreCase(codRespuesta, "Tramitado") || "0003".equals(codigoRespuesta)) {
                codRespuesta = "TRAMITADA";
            }

            String fechaEmision = this.obtenerValorEtiquetaXml(xmlRespuesta, "fecha_generacion");
            String idSolicitud = this.obtenerValorEtiquetaXml(xmlRespuesta, "identificador_solicitud");
            String referencia = this.obtenerValorEtiquetaXml(xmlRespuesta, "Referencia");
            String nifEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif_emisor");
            String nombreEmisor = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_emisor");
            String tipoDocTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "tipo_doc");
            String documentacionTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");

            // Lectura de la lista de epígrafes/actividades
            List<Element> epigrafes = this.obtenerElementosXml(xmlRespuesta, "DatosActividad");

            // 2. Estilos de Fuentes
            Font fuenteTitulo = new Font(FontFamily.HELVETICA, 13, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9, Font.NORMAL);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7, Font.ITALIC);

            // 3. Creación del documento PDF
            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // Cabecera principal
            documento.add(new Paragraph("Comunidad de Madrid", fuenteTitulo));
            documento.add(new Paragraph(" "));
            documento.add(new Paragraph("Datos de la consulta. Información del IAE (Impuesto Actividades Económicas)", fuenteSeccion));
            documento.add(new Paragraph(" "));

            // Bloque: Datos de la consulta
            documento.add(this.crearFilaDato("Código certificado:", codigoCertificado, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Cód Respuesta:", codRespuesta, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Fecha emisión:", fechaEmision, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Id. Solicitud:", idSolicitud, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Referencia:", referencia, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque: Emisor
            documento.add(new Paragraph("Datos del emisor:", fuenteSeccion));
            documento.add(this.crearFilaDato("Nif:", nifEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Nombre:", nombreEmisor, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque: Titular
            documento.add(new Paragraph("Datos del titular:", fuenteSeccion));
            documento.add(this.crearFilaDato("Tipo Documentación:", tipoDocTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Documentación:", documentacionTitular, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // Texto legal previo a epígrafes
            documento.add(new Paragraph("La Agencia Estatal de Administración Tributaria conforme a los datos que obran en la AEAT CERTIFICA que el titular arriba referenciado figura en el censo del Impuesto de Actividades Económicas en los siguientes epígrafes:", fuenteValor));
            documento.add(new Paragraph(" "));

            // Bloque: Iteración y maquetación visual de Epígrafes (Formato igual a la plantilla)
            for (Element epigrafe : epigrafes) {
                String seccionNum = this.obtenerValorEtiquetaXml(epigrafe, "Seccion");
                String codigoEpigrafe = this.obtenerValorEtiquetaXml(epigrafe, "CodigoEpigrafe");
                String descripcionEpigrafe = this.obtenerValorEtiquetaXml(epigrafe, "DescripcionEpigrafe");
                String fechaInicio = this.obtenerValorEtiquetaXml(epigrafe, "FechaInicio");

                // Mapeo del código numérico de Sección a texto descriptivo
                String seccionTexto = "1".equals(seccionNum) ? "Empresarial" :
                                      "2".equals(seccionNum) ? "Profesional" :
                                      "3".equals(seccionNum) ? "Artística" : seccionNum;

                // Tabla clave-valor de 4 columnas para alinear exactamente como la imagen
                PdfPTable tablaBloque = new PdfPTable(new float[] { 2f, 2.5f, 2f, 7.5f });
                tablaBloque.setWidthPercentage(100);

                // Fila 1: Sección
                tablaBloque.addCell(crearCeldaDato("Sección:", fuenteEtiqueta));
                tablaBloque.addCell(crearCeldaDato(seccionTexto, fuenteValor, 3));

                // Fila 2: Epígrafe y Descripción en la misma línea
                tablaBloque.addCell(crearCeldaDato("Epígrafe:", fuenteEtiqueta));
                tablaBloque.addCell(crearCeldaDato(codigoEpigrafe, fuenteValor));
                tablaBloque.addCell(crearCeldaDato("Descripción:", fuenteEtiqueta));
                tablaBloque.addCell(crearCeldaDato(descripcionEpigrafe, fuenteValor));

                // Fila 3: Fecha Inicio
                tablaBloque.addCell(crearCeldaDato("FcInicio:", fuenteEtiqueta));
                tablaBloque.addCell(crearCeldaDato(fechaInicio, fuenteValor, 3));

                documento.add(tablaBloque);
                documento.add(new Paragraph(" "));
            }

            // Pie de página con nota legal y consulta
            documento.add(new Paragraph("Esta consulta de datos se realiza en cumplimiento del artículo 28.2 de la ley 39/2015 del Procedimiento Administrativo Común de las Administraciones Públicas previa no oposición del interesado", fuenteNota));
            documento.add(new Paragraph(" "));
            documento.add(this.crearFilaDato("Nombre del Solicitante", nombreSolicitante, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha de consulta",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado de IAE: " + e.getMessage());
        }
    }

    // Métodos auxiliares requeridos para maquetar las celdas sin bordes

    private PdfPCell crearCeldaDato(String texto, Font fuente) {
        return crearCeldaDato(texto, fuente, 1);
    }

    private PdfPCell crearCeldaDato(String texto, Font fuente, int colspan) {
        PdfPCell celda = new PdfPCell(new Phrase(StringUtils.defaultString(texto), fuente));
        celda.setBorder(Rectangle.NO_BORDER);
        celda.setColspan(colspan);
        celda.setPaddingBottom(3f);
        return celda;
    }

    /**
     * Genera el PDF del certificado ORVASUB (Corriente de pago en Comunidad de Madrid, Dirección
     * General de Tributos). Layout completamente distinto al resto: sin la cabecera roja
     * "Comunidad de Madrid" ni las secciones de "Código certificado"/"Datos del Emisor" - en su
     * lugar lleva el membrete oficial a dos columnas de la Dirección General de Tributos /
     * Subdirección General de Recaudación, y dos párrafos legales largos (no tener deudas en
     * periodo ejecutivo, y el carácter positivo/validez de seis meses del certificado) antes del
     * disclaimer final habitual (artículo 28.2 de la ley 39/2015).
     *
     * TODO: confirmar contra una respuesta real de ORVASUB las etiquetas XML de "razon_social"/
     * "nombre_titular" (se muestra uno u otro según sea persona jurídica o física) - de momento se
     * usan las mismas etiquetas ya confirmadas en CENO ("razon_social") y ECOT ("nombre_titular").
     *
     * @param xmlRespuesta XML devuelto por solicitarCertificadoORVA (xmlOriginal)
     * @return el PDF generado, en bytes
     * @throws ServiceException si no se ha podido generar el PDF
     */
    private byte[] generarPdfCertificadoOrvasub(String xmlRespuesta, String nombreFallback) throws ServiceException {
        try {
            // 1. Extracción de datos del XML
            String nifTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nif");
            if (StringUtils.isBlank(nifTitular)) {
                nifTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "documentacion");
            }

            String razonSocialTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "razon_social");
            String nombreTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre");
            if (StringUtils.isBlank(nombreTitular)) {
                nombreTitular = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_titular");
            }
            String nombreRazonSocial = StringUtils.isNotBlank(razonSocialTitular) ? razonSocialTitular : nombreTitular;

            // Fallback: Si es DEUDOR INEXISTENTE (01), el XML viene sin nombre -> Usamos el nombre guardado en la app
            if (StringUtils.isBlank(nombreRazonSocial)) {
                nombreRazonSocial = nombreFallback;
            }

            String nombreSolicitante = this.obtenerValorEtiquetaXml(xmlRespuesta, "nombre_solicitante");

            // -----------------------------------------------------------------------------------------
            // EVALUACIÓN DEL RESULTADO SEGÚN EL CÓDIGO DE ORVA (01, 02 u otros)
            // -----------------------------------------------------------------------------------------
            String codigoResultado = this.obtenerValorEtiquetaXml(xmlRespuesta, "codigo_resultado");
            String resultadoTexto;
            String textoDeudas;

            if ("01".equals(codigoResultado)) {
                resultadoTexto = "DEUDOR INEXISTENTE";
                textoDeudas = "la Entidad o persona arriba referenciada no tiene con la Comunidad de Madrid deudas en periodo ejecutivo y";
            } else if ("02".equals(codigoResultado)) {
                resultadoTexto = "POSITIVO";
                textoDeudas = "la Entidad o persona arriba referenciada no tiene con la Comunidad de Madrid deudas en periodo ejecutivo y";
            } else {
                resultadoTexto = "NEGATIVO";
                textoDeudas = "la Entidad o persona arriba referenciada tiene con la Comunidad de Madrid deudas en periodo ejecutivo y";
            }

            // 2. Fuentes
            Font fuenteMembrete = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteSeccion = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
            Font fuenteEtiqueta = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            Font fuenteValor = new Font(FontFamily.HELVETICA, 9);
            Font fuenteNota = new Font(FontFamily.HELVETICA, 7.5f, Font.ITALIC);

            ByteArrayOutputStream salida = new ByteArrayOutputStream();
            com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(documento, salida);
            documento.open();

            // --- ENCABEZADO / MEMBRETE ---
            PdfPTable membrete = new PdfPTable(new float[] { 1f, 1f });
            membrete.setWidthPercentage(100);

            PdfPCell celdaIzquierda = new PdfPCell(new Phrase("Dirección General de Tributos\nCONSEJERÍA DE ECONOMÍA, "
                    + "HACIENDA Y EMPLEO\nComunidad de Madrid", fuenteMembrete));
            celdaIzquierda.setBorder(Rectangle.NO_BORDER);
            membrete.addCell(celdaIzquierda);

            PdfPCell celdaDerecha = new PdfPCell(new Phrase("Subdirección General de Recaudación\nCalle Manuel "
                    + "Cortina, nº 2. 2ª planta\n28010 Madrid\nTlf: 915809343", fuenteMembrete));
            celdaDerecha.setBorder(Rectangle.NO_BORDER);
            membrete.addCell(celdaDerecha);

            documento.add(membrete);
            documento.add(new Paragraph(" "));

            // --- TÍTULO DE LA CONSULTA ---
            documento.add(new Paragraph("Datos de la consulta. Corriente de pago en Comunidad de Madrid", fuenteSeccion));
            documento.add(new Paragraph(" "));

            // --- DATOS DEL TITULAR ---
            documento.add(this.crearFilaDato("NIF:", nifTitular, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("RAZÓN SOCIAL/NOMBRE:", nombreRazonSocial, fuenteEtiqueta, fuenteValor));
            documento.add(new Paragraph(" "));

            // --- PÁRRAFO 1 DEL CUERPO (Texto condicional de deudas) ---
            Paragraph pCert1 = new Paragraph();
            pCert1.setFont(fuenteValor);
            pCert1.add("Se informa que de acuerdo con los datos obrantes en la Recaudación de la Comunidad de Madrid, "
                    + "y de conformidad con lo dispuesto en el artículo 29.5 de la Ley 9/1990, de 8 de noviembre, "
                    + "Reguladora de la Hacienda de la Comunidad de Madrid, según la redacción dada a este artículo "
                    + "por la Ley 24/1999, de 27 de diciembre, de Medidas Fiscales y Administrativas, ");
            pCert1.add(new Chunk(textoDeudas, fuenteEtiqueta));
            pCert1.add(", si las tiene, están aplazadas, fraccionadas o ha sido acordada su suspensión, o bien existen créditos "
                    + "a su favor que garantizan el cobro de las mismas, todo ello, a los efectos de solicitar una subvención "
                    + "a la Administración de la Comunidad de Madrid.");
            pCert1.setSpacingAfter(12f);
            documento.add(pCert1);

            // --- PÁRRAFO 2 DEL CUERPO (Palabra dinámica POSITIVO / DEUDOR INEXISTENTE / NEGATIVO) ---
            Paragraph pCert2 = new Paragraph();
            pCert2.setFont(fuenteValor);
            pCert2.add("La presente consulta de datos se expide con autorización expresa del interesado, tiene el carácter de ");
            pCert2.add(new Chunk(resultadoTexto, fuenteEtiqueta));
            pCert2.add(" y una validez de seis meses, contados desde la fecha de su expedición, y no origina derechos ni expectativas "
                    + "de derechos a favor de los solicitantes ni de terceros, no pudiendo ser invocado a efectos de interrupción o "
                    + "paralización de plazos de caducidad o prescripción, ni servirá de medio de notificación de los expedientes a que "
                    + "pudiera hacer referencia, sin que su contenido pueda afectar al resultado de actuaciones posteriores de comprobación o investigación.");
            pCert2.setSpacingAfter(15f);
            documento.add(pCert2);

            // --- NOTA LEGAL Y DATOS DEL SOLICITANTE ---
            Paragraph pNota = new Paragraph();
            pNota.setFont(fuenteNota);
            pNota.add("Esta consulta de datos se realiza en cumplimiento del ");
            pNota.add(new Chunk("artículo 28.2 de la ley 39/2015", new Font(FontFamily.HELVETICA, 7.5f, Font.BOLDITALIC)));
            pNota.add(" del Procedimiento Administrativo Común de las Administraciones Públicas previa no oposición del interesado");
            pNota.setSpacingAfter(10f);
            documento.add(pNota);

            documento.add(this.crearFilaDato("Nombre del Solicitante", nombreSolicitante, fuenteEtiqueta, fuenteValor));
            documento.add(this.crearFilaDato("Fecha de consulta",
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()), fuenteEtiqueta, fuenteValor));

            documento.close();
            return salida.toByteArray();
        } catch (Exception e) {
            throw new ServiceException("No se ha podido generar el PDF del certificado ORVASUB: " + e.getMessage());
        }
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
       }
        
    }
    
    
    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     *
     * @param convocatoriaDTO
     */
    public void guardarNuevoDocumentoExterno() {

        if (this.formularioDocExternoReqDTO.getTramiteDTO() == null
                || this.formularioDocExternoReqDTO.getTramiteDTO().getIdValor() == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TRAMITE);
        } else if (this.formularioDocExternoReqDTO.getTipoDocumentoDTO() == null
                || this.formularioDocExternoReqDTO.getTipoDocumentoDTO().getIdValor() == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_TIPO_DOCUMENTO);
        }else {

            try  {
                if(this.formularioDocExternoReqDTO.getExpedienteDTO() == null) {
                    this.formularioDocExternoReqDTO.setExpedienteDTO(new ExpedienteDTO());
                }
                this.formularioDocExternoReqDTO.getExpedienteDTO().setIdExpediente(this.idExpediente);
                
                this.expedienteDocService.guardarExternoSinDoc(this.formularioDocExternoReqDTO);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);

                this.actualizarValoresFormulario();

                this.inicializarDocumento();

            } catch ( ServiceException e) {
                log.error(e.getMessage());

                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);

            }

        }
    }
    
    /**
     * Método para mostrar la ventana popup con las observaciones de un documento
     *
     * @param documento Datos del documento
     */
    public void mostrarDocumentoObservaciones(ExpedienteDocDTO documento) {
        this.idDocumentoObservaciones = documento.getIdExpedienteDoc();
        this.tlObservaciones = documento.getTlObservaciones();
        this.tlObservacionesGestor = documento.getTlObservacionesGestor();
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosObservaciones').show();");
    }

    /**
     * Guarda las observaciones internas del gestor para el documento seleccionado
     */
    public void guardarObservacionesGestor() {
        try {
            ExpedienteDocDTO documento = this.expedienteDocService.buscarId(this.idDocumentoObservaciones);
            documento.setTlObservacionesGestor(this.tlObservacionesGestor);
            this.expedienteDocService.guardarConHistorico(documento);
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            PrimeFaces.current().executeScript("PF('dialogObservacionesInternasGestor').hide();");
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    

    
//  public void ejecutarProcesosJCTBatch() {
//  try {
//
//      log.info("------------------------------");
//      log.info("---- INICIO SAII_BATCH -------");
//      log.info("------------------------------");
//      
//      
//      log.info("-------------------------------------");
//      log.info("--- convocatoriaBusiness. Inicio. ---");
//      log.info("-------------------------------------");
//      this.documentoPtfrService.consultarRespuestaPortafirmasBatch();
//     
//     log.info("-------------------------------------");
//     log.info("- convocatoriaBusiness. Fin.");
//     log.info("-------------------------------------");
     
     //this.documentoPtfrService.consultarLecturaNotificacionNoteBatch();
//     expedienteBusiness.procesarConsultarLecturaNotificacionNote();
//     expedienteBusiness.procesarConsultarRespuestaPortafirmas();
//     expedienteBusiness.actualizarExpedienteConFcAlegacionNoVencidaBatch();
//     expedienteBusiness.actualizarEstadoExpedienteAlegacionBatch();
//     
//     expedienteBusiness.iniciarPlazoEvaluacionJCT();
//     expedienteBusiness.finalizarPlazoEvaluacionJCT();
//     expedienteBusiness.procesarPlazosJCT();
//      this.expedienteJusEcoService.procesarPlazosJUSECO();
//   
//     
//     documentoBusiness.procesarConsultarDocumentoPlazoSubsanacion();
//     documentoBusiness.procesarConsultarDocumentoPlazoSubsanacionJCTBatch();            
//     documentoBusiness.procesarConsultarDocumentoPlazoSubsanacionContratosBatch(); 
   //this.convocatoriaService.procesarConvocatoriasBatch()
     //this.documentoPtfrService.consultarRespuestaPortafirmasBatch();    
//      this.expedienteJusTecService.procesarPlazosJCT();
      
//      log.info("------------------------------");
//      log.info("---- FIN SAII_BATCH -------");
//      log.info("------------------------------");
//  } catch (ServiceException e) {
//      e.printStackTrace();
//  }
//}    

//    public void consultarLecturaNotificacionNoteBatch() {
//        try {
//            this.documentoPtfrService.consultarLecturaNotificacionNoteBatch();
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//    }    
//    
//    public void consultarRespuestaPortafirmasBatch() {
//        try {
//            this.documentoPtfrService.consultarRespuestaPortafirmasBatch();
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//    }
    
    /**
     * Método para mostrar la ventana popup con las observaciones  internas del gestor
     *
     * @param documento Datos del documento
     */
    public void mostrarObservacionInternaGestor(ExpedienteDocDTO documento) {
        this.idDocumentoObservaciones = documento.getIdExpedienteDoc();
        this.tlObservaciones = documento.getTlObservaciones();
        this.tlObservacionesGestor = documento.getTlObservacionesGestor();
        PrimeFaces.current().ajax().update(this.solapaDocumentos);
        PrimeFaces.current().executeScript("PF('dialogObservacionesInternasGestor').show();");
    }
    
    /**
     * Método para cerrar la ventana popup de observaciones internas gestor
     */
    public void cerrarObsIntGestorDialog() {
        this.tlObservaciones = null;
        this.tlObservacionesGestor = null;
        this.idDocumentoObservaciones = null;
        
        PrimeFaces.current().executeScript("PF('dialogObservacionesInternasGestor').hide();");

        // Si el objeto documentoSeleccionado es nulo, es que el popup de
        // observaciones no se ha llamado desde el popup de historico.
        // y no entra en el flujo del if. Caso contrario, el popup observaciones
        // se ha llamado desde el popup historico, y se volverá a abrir el popup
        // historico, despues de cerrar el de observaciones.
        if (null != this.documentoSeleccionado) {
            this.mostrarHistoricoDocumento(this.documentoSeleccionado);
        }
    }
    
    /**
     * Método para mostrar la ventana popup con el histórico de observaciones de
     * un documento
     *
     * @param documento Datos del documento
     */
    public void mostrarObservacionesInternasGestor(String observaciones,String observacionesGestor ) {
        this.idDocumentoObservaciones = null;
        this.tlObservaciones = observaciones;
        this.tlObservacionesGestor = observacionesGestor;
        
        //VMM 11/02/26 MD-2471: Mostramos el dialogo si existen observaciones del ciudadano o del gestor
        if (this.tlObservaciones != null || this.tlObservacionesGestor != null) {
            PrimeFaces.current()
                    .executeScript("PF('dialogObservacionesInternasGestor').show();");
        }
    }
    
}
