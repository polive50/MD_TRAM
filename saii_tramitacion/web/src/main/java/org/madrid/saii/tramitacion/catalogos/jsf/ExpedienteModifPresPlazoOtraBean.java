package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToDTO;
import org.madrid.saii.lib.services.BloqueService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ComponenteService;
import org.madrid.saii.lib.services.ConvocatoriaCauService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DatoService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.ExpedienteAnualidadService;
import org.madrid.saii.lib.services.ExpedienteDocHistService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteEvalService;
import org.madrid.saii.lib.services.ExpedienteModfPresupPlazosOtrosDocService;
import org.madrid.saii.lib.services.ExpedienteModifOtraService;
import org.madrid.saii.lib.services.ExpedienteModifPlazoService;
import org.madrid.saii.lib.services.ExpedienteModifPresPlazoOtraService;
import org.madrid.saii.lib.services.ExpedienteModifPresupMontoService;
import org.madrid.saii.lib.services.ExpedientePresupService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.ImpresoService;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.ExportadorDatosPresupuestariosModif;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.madrid.saii.tramitacion.jsf.utils.ValidacionConceptosPresupuesto;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Solicitud}</code>
 *
 * @author quental
 *
 * @since 20-dic-2024
 *
 * @see ExpedienteModifPresPlazoOtraBean
 */
@Slf4j
@Getter
@Setter
@Component("expedienteModifPresPlazoOtraBean")
@Scope("request")
public class ExpedienteModifPresPlazoOtraBean {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * dto de expediente
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;
    
    
    /**
     * convocatoriaDTO
     */
    private ConvocatoriaDTO convocatoriaDTO;

    /**
     * Dto de ExpedienteModifPresPlazoOtra
     */
    private ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO;
    
    private Boolean mostrarMensajeElegirFichero = Boolean.FALSE;

    /**
     * Id de Expediente
     */
    private Integer idExpediente;
    
    /* Estilo pestaña ModifPresPlazoOtra */
    private Boolean bAvisoModifPresPlazoOtra;
    
    /** Lista de modificaciones P/P/O */
    private List<ExpedienteModifPresPlazoOtraDTO> listadoModificacionesPPO;
    
    private ExpedienteModifPresPlazoOtraDTO selectExpedienteModifDTO;
    
    private Boolean mostrarDetalleModifPres;
    
    private Boolean mostrarDetalleModifPlazo;
    
    private Boolean mostrarDetalleModifOtra;
    
    private Boolean mostrarDetalleModifDoc;
    
    private Boolean mostrarDetalleModifObservac;
    
    /* Id estado del expediente */
    private Integer idEstadoExpediente;

    /* Lista de anualidades */
    private List<Integer> listaAnualidades;

    /*
     * Lista con el número de columnas a mostrar en la tabla dinámica de
     * presupuesto financiable
     */
    private List<Integer> listaNcolPresupuesto;
    
    /*
     * Lista con el número de columnas a mostrar en la tabla dinámica de
     * plazos
     */
    private List<Integer> listaNcolPlazo;

    /* Total de anualidades */
    private int totalAnualidades;
    
    /* porcentaje intensidad ayuda */
    private Double porcentajeIntensidadAyuda;

    /* Lista bloque componente columna */
    private List<BloqueComponenteDTO> lstBloqueComponenteColumna;

    /* Lista bloque componente fila */
    private List<BloqueComponenteDTO> lstBloqueComponenteFila;

    private List<BloqueDTO> lstBloque;
    
    private List<ExpedienteDocDTO> documentosHistoricoSeleccionado;
    
    /**
     * archivoResolucionProvisional
     */
    private UploadedFile archivoResolucion;
    
    
    private UploadedFile archivoSubirDoc;
    
    /*
     * Variable aplicada para los titulos de las tablas cuando existe más de un
     * presupuesto
     */
    private Boolean verNumeracion;
    /**
     * Identificador de solapa de modificacion PPO
     */
    private String solapaModificacionPPO = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";

    /* Ruta de la tabla presupuesto financiable */    
    private String tablaPresupuestoFinanciableAcept = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgPresupuestoFinanciableAcept";
    
    private String tablaPresupuestoFinanciableAcept2 = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgPresupuestoFinanciableAcept2";

    private String mensajeAdvertenciaPresup = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:msgWarningPresupuesto";

    private List<ExpedientePresupDTO> lstPresupuestoFinanciable;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciableAntesMod1;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciableAcept1;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciableAcept1Modif;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciableAntesMod2;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciableAcept2;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedidaAntesMod;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedidaAcept;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedidaAntesMod2;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedidaAcept2;
    
    private List<BloqueComponenteDTO> lstPresupuestoAyudaConcedidaSolicitada;
    
    private List<BloqueComponenteDTO> lstPresupuestoAyudaConcedidaSolicitada2;
    
    private List<ExpedienteModifPresupMontoDTO> lstPresupuestoFinanciableModifAcept1;
    
    private List<ExpedienteModifPresupMontoDTO> lstPresupuestoFinanciableModifAcept2;

    private Boolean tieneErrores;

    /* modoConsulta */
    private Boolean modoConsultaModifPPO;

       
    /* ExpedienteAnualidad Antes Modif */
    private List<ExpedienteModifPlazoDTO> lstPlazosAntesMod;
    
    /* lista de bloque plazos solicitados */
    private List<BloqueDTO> lstBloquePlazos;
    
    /* Lista bloque plazos componente columna */
    private List<BloqueComponenteDTO> lstBloqueComponenteColumnaPlazo;

    /* Lista bloque plazos componente fila */
    private List<BloqueComponenteDTO> lstBloqueComponenteFilaPlazo;
    
    /* ExpedienteModifPlazo Modif Aceptada*/
    private List<ExpedienteModifPlazoDTO> lstPlazosAcept;
    
    private String tlOtrasModificaciones;
    
    private String tlOtrasModifObservaciones;
    
    private String tlObservacionesModifPlazos;

    private List<ExpedienteDocDTO> lstDocumentosModif;

    private DocumentoDTO documentoDTO;
    
    
    private Date fcResolucion;
    
    private CatalogosValoresDTO  estadoResolucionDTO;
    
    /*
     * expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;

    /**
     * Atributo para almacenar la causa de denegación seleccionada
     */
    private Integer idCausaSubsanacion;

    /**
     * Atributo para controlar si es obligatorio ingresar la descripción de
     * causa de denegación
     */
    private Boolean descripcionCausaObligatoria = Boolean.FALSE;

    private String descripcionCausaSubsanacion;

    private ExpedienteDocDTO documentoSeleccionado;

    private Boolean modoLecturaSubsanacion = Boolean.FALSE;

    private String tlObservaciones;
    
    /** Observaciones de los motivos de la modificación PPO */
    private String tlObservacionesModif;
    
   
    
    
    
    private String observacionesTablaPresupuestaria;
    
    /**
     * Comentario del motivo de rechazo del documento seleccionado
     */
    private String motivoRechazoSeleccionado;

    private Boolean existeEnvioPtfrSubsanacionActasEnProceso;
    
    private Boolean existeEnvioPtfrDesistimientoEnProceso;

    private Boolean existeEnvioPtfrSubsanacionActasFirmado;

    private DocumentoPtfrDTO documentoPtfrDTO;

    // VMM 10/09/26 MD-2725: envío a Portafirmas/NOTE de la resolución de la modificación
    // seleccionada, para mostrar su estado NOTE bajo "Datos de Resolución".
    private DocumentoPtfrDTO envioPortafirmasResolucionDTO;

    /**
     * VMM 06/10/26 MD-3110: true si en esta peticion ya se ha consultado el envio a Portafirmas/Note
     * de la resolucion (aunque no exista ninguno), para no repetir la consulta en cada acceso del
     * xhtml. El bean es de request, asi que se reinicia en cada peticion.
     */
    private Boolean envioPortafirmasResolucionCargado = Boolean.FALSE;

    private Boolean esconderBotonCargarDatoPresup;
    
    private Boolean esconderBotonCargarDatoPlazos;
    
    private Boolean esConvocatoriaConceptoPresup;
    
    
 // Variable para mantener el objeto que se está editando en la modal
    private ExpedienteModifPresupMontoDTO presupuestoSeleccionado;
    
    private BloqueComponenteDTO filaBloquePresupuestoSeleccionada;




    // Getters y Setters...

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;

    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;
        
    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;
    
    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;
        
    @Autowired
    @Qualifier("expedienteModifOtraService")
    private ExpedienteModifOtraService expedienteModifOtraService;
    
    /**
     * Servicio de CatalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;
    
    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;

    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;
    
    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    /**
     * Servicio de expedienteEvalService
     */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;

    @Autowired
    @Qualifier("convocatoriaCauService")
    private ConvocatoriaCauService convocatoriaCauService;

    /**
     * Servicio de documentoPtfrService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    @Autowired
    @Qualifier("expedienteModfPresupPlazosOtrosDocService")
    private ExpedienteModfPresupPlazosOtrosDocService expedienteModfPresupPlazosOtrosDocService;
        
    @Autowired
    @Qualifier("componenteDAO")
    private ComponenteDAO componenteDAO;

    @Autowired
    @Qualifier("expedienteModifPresupMontoService")
    private ExpedienteModifPresupMontoService expedienteModifPresupMontoService;
    
    @Autowired
    @Qualifier("expedienteModifPlazoService")
    private ExpedienteModifPlazoService expedienteModifPlazoService;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    /** Servicio de Ereg. */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;
    
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    
    private ExpedienteBean expedienteBean;
    
    @Lazy
    @Autowired
    @Qualifier("convocatoriasBean")
    private ConvocatoriasBean convocatoriasBean;
   
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor de clase
     */
    public ExpedienteModifPresPlazoOtraBean() {
        super();
        this.tieneErrores = Boolean.FALSE;
        this.verNumeracion = Boolean.FALSE;    
        this.bAvisoModifPresPlazoOtra = Boolean.FALSE;
        this.mostrarDetalleModifPres = Boolean.FALSE;
        this.mostrarDetalleModifPlazo = Boolean.FALSE;
        this.mostrarDetalleModifOtra = Boolean.FALSE;
        this.mostrarDetalleModifDoc = Boolean.FALSE;
        this.documentoDTO = new DocumentoDTO();
        this.estadoResolucionDTO = new CatalogosValoresDTO();
        this.esConvocatoriaConceptoPresup = Boolean.FALSE;
        
        this.presupuestoSeleccionado = new ExpedienteModifPresupMontoDTO();
        
        this.inicializarDocumento();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PUBLIC ACTIONS
    // ////////////////////////////////////////////////////////
    
    /**
    * Metodo que carga la información inicial de Modificaciones PPO
    * @throws ServiceException 
    */
   public void cargarInformacion() throws ServiceException {

       this.tieneErrores = Boolean.FALSE;
       this.verNumeracion = Boolean.FALSE;
       this.bAvisoModifPresPlazoOtra = Boolean.FALSE;
       //VMM 14/04/26 MD-2667: reseteamos los valores anteriores
       this.selectExpedienteModifDTO = new ExpedienteModifPresPlazoOtraDTO();
       this.listadoModificacionesPPO = new ArrayList<>();
       this.documentoSeleccionado = new ExpedienteDocDTO();
       this.lstDocumentosModif = new ArrayList<>();
       this.lstBloqueComponenteFila = new ArrayList<>();
       this.lstBloqueComponenteFilaPlazo = new ArrayList<>();
       this.mostrarDetalleModifOtra = Boolean.FALSE;
       this.mostrarDetalleModifObservac = Boolean.FALSE;
       this.mostrarDetalleModifDoc = Boolean.FALSE;
       inicializarListasModif();
       inicializarDocumento();

       this.validarEstilo();
   }

   /**
    * VMM 08/09/26 MD-2920: cargarInformacion() resetea selectExpedienteModifDTO a un DTO vacío
    * (correcto la primera vez que se entra al expediente, cuando aún no hay ninguna modificación
    * seleccionada), pero varias acciones la invocan con una modificación ya seleccionada y en uso
    * en pantalla (Finalizar Revisión, Subir Resolución, Enviar a la cola portafirmas...), dejando
    * selectExpedienteModifDTO vacío (sin expedienteDTO) y provocando que el siguiente guardado
    * falle en BD (ORA-01400 al intentar dejar ID_EXPEDIENTE a null) o que la pantalla se quede en
    * blanco hasta volver a pulsar la lupa. Este método conserva el id de la modificación activa y,
    * tras el reset, la vuelve a cargar igual que si se hubiera pulsado la lupa (verDetalleModif).
    */
   private void recargarInformacionManteniendoSeleccion() throws ServiceException {
       Integer idModifSeleccionada = this.selectExpedienteModifDTO != null
               ? this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra()
               : null;

       this.cargarInformacion();

       if (idModifSeleccionada != null) {
           ExpedienteModifPresPlazoOtraDTO dtoRecargado = this.expedienteModifPresPlazoOtraService.buscarId(idModifSeleccionada);
           if (dtoRecargado != null) {
               this.verDetalleModif(dtoRecargado);
           }
       }
   }

   public void inicializarDocumento() {
       this.expedienteDocDTO = new ExpedienteDocDTO();
       this.archivoResolucion = null;
   }
   
   private void inicializarListasModif() {
       //resetear listas de modificaciones de presupuestos
       this.lstPresupuestoFinanciable = new ArrayList<>();
       this.lstPresupuestoFinanciableAntesMod1 = new ArrayList<>();
       this.lstPresupuestoFinanciableAntesMod2 = new ArrayList<>();
       this.lstPresupuestoFinanciableAcept1 = new ArrayList<>();
       this.lstPresupuestoFinanciableAcept2 = new ArrayList<>();
       this.lstBloque = new ArrayList<>();      
       this.lstPresupuestoAyudaConcedidaAntesMod = new ArrayList<>();
       this.lstPresupuestoAyudaConcedidaAcept = new ArrayList<>();
       this.lstPresupuestoAyudaConcedidaAntesMod2 = new ArrayList<>();
       this.lstPresupuestoAyudaConcedidaAcept2 = new ArrayList<>();
       
       //resetear listas de modificaciones de plazos
       this.lstPlazosAntesMod = new ArrayList<>();
       this.lstBloquePlazos = new ArrayList<>();
       this.lstPlazosAcept = new ArrayList<>();
       this.tlObservacionesModifPlazos = "";
       
       //resetear otras modificaciones
       this.tlOtrasModificaciones = null;
       this.tlOtrasModifObservaciones = null;
       
       //resetear observaciones
       this.tlObservacionesModif = null;
       this.observacionesTablaPresupuestaria = null;
       
       //otras inicializaciones
       this.tieneErrores = Boolean.FALSE;
       this.verNumeracion = Boolean.FALSE;
       this.bAvisoModifPresPlazoOtra = Boolean.FALSE;
   }
    
   /**
    * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
    * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
    * en esta solapa (guardar presupuesto/plazos/otras/observaciones, subsanar/aprobar/desaprobar/
    * revertir documentos de actas, finalizar revisión, subir/enviar resolución, enviar a
    * portafirmas...), dejando habilitadas las de solo lectura (descargar, ver histórico).
    * No se aplica dentro de esModoConsulta()/modoConsultaModifPPO porque ese flag también
    * gobierna algún botón de descarga (comparte método con "Enviar a portafirmas"), así que se
    * aplica de forma puntual en cada botón de acción real.
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
    * VMM 19/10/26 MD-3148: modo consulta de la modificacion seleccionada. Es un valor DERIVADO (sale de la
    * modificacion seleccionada, del rol del usuario y del modo consulta de la convocatoria), asi que no debe
    * guardarse y restaurarse con atlasp:guardarEstado: al cambiar de expediente (p.ej. del 1761, con la
    * modificacion Resuelta, al 1767, en Pendiente revision) se restauraba el valor del anterior DESPUES de
    * recalcularlo en verDetalleModif, y el Guardar quedaba deshabilitado (y al reves). Cuando hay una
    * modificacion seleccionada se recalcula en cada lectura (solo compara valores en memoria); si no la hay se
    * devuelve el valor que tenga. Dentro de esModoConsulta() se usa el campo directamente, no este getter.
    */
   public Boolean getModoConsultaModifPPO() {
       if (this.selectExpedienteModifDTO != null
               && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
           this.esModoConsulta();
       }
       return this.modoConsultaModifPPO;
   }

   /**
    * Metodo que presenta la información en modo consulta
    */
   private void esModoConsulta() {

       if (Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta()) 
              // || !dto.isEsGestorJCT()
              // || !dto.isEsGestorConcesion()
               || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())){ //MD-2695 incluimos que no puedan acceder a estas acciones los gestores ext
           this.modoConsultaModifPPO = Boolean.TRUE;
       } else {
           List<Integer> lstEstadoExpediente = new ArrayList<>();
           lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
           lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
           lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));

           long existeEstado = lstEstadoExpediente.stream()
                   .filter(x -> x.equals(this.idEstadoExpediente)).count();

           this.modoConsultaModifPPO = existeEstado == 1 ? Boolean.TRUE : Boolean.FALSE;

           //comprobamos si la modificacion está en estado resuelta
           if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getEstadoDTO() != null
                   && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                   && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().
                   equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.RESUELTA))) {
               this.modoConsultaModifPPO = Boolean.TRUE;
           }

           // VMM 02/09/26 MD-2920: si el expediente está Concedido Definitivo pero la modificación
           // seleccionada sigue Pendiente de revisión, no se bloquea en modo consulta: las
           // modificaciones de presupuesto/plazo/otra se solicitan precisamente después de
           // la concesión, y deben poder revisarse/resolverse aunque el expediente ya esté concedido.
           if (Boolean.TRUE.equals(this.modoConsultaModifPPO)
                   && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)
                           .equals(this.idEstadoExpediente)
                   && this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getEstadoDTO() != null
                   && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE)
                           .equals(this.selectExpedienteModifDTO.getEstadoDTO().getIdValor())) {
               this.modoConsultaModifPPO = Boolean.FALSE;
           }
       }

       // MD-2935: Para permitir la revisión de las modificaciones (presupuestos, fechas/plazos y otras), la
       // modificación debe estar en estado "Pendiente"; si no lo está, se tratan como modo consulta los
       // botones de Cargar Datos y Guardar de las tres secciones
       if (Boolean.FALSE.equals(this.modoConsultaModifPPO)
               && (this.selectExpedienteModifDTO == null
                   || this.selectExpedienteModifDTO.getEstadoDTO() == null
                   || this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() == null
                   || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE)
                           .equals(this.selectExpedienteModifDTO.getEstadoDTO().getIdValor()))) {
           this.modoConsultaModifPPO = Boolean.TRUE;
       }
   }

   
   /**
    * VMM 06/10/26 MD-3066: true si la resolucion de la modificacion indicada ya se ha enviado a la
    * cola de portafirmas (existe su registro en SAII_DOCUMENTO_PTFR) y no fue rechazada en Portafirmas.
    * Una resolucion rechazada vuelve a requerir accion del gestor, asi que cuenta como "no enviada".
    */
   private boolean existeEnvioPortafirmasModif(Integer idModificacion) throws ServiceException {
       if (idModificacion == null) {
           return false;
       }
       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter("idPresupuestoModif", idModificacion, Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

       List<DocumentoPtfrDTO> lista = this.documentoPtfrService.buscar(0, -1, filtros, null);
       if (lista == null) {
           return false;
       }
       return lista.stream().anyMatch(envio -> !Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO.equals(envio.getCdEstadoPtfr()));
   }

   /**
    * Metodo que valida el estilo de la pestaña de modif PPO
    * @throws ServiceException 
    * 
    */
   private void validarEstilo() throws ServiceException {

       this.setBAvisoModifPresPlazoOtra(Boolean.FALSE);
      
       // si existen presup vacios los inicializo a null (se puede usar en render del front)
       if (Objects.nonNull(this.lstBloqueComponenteFila)
               && this.lstBloqueComponenteFila.isEmpty()) {
           this.lstBloqueComponenteFila = null;
       }
       
       // si existen plazos vacios los inicializo a null (se puede usar en render del front)
       if (Objects.nonNull(this.lstBloqueComponenteFilaPlazo)
               && this.lstBloqueComponenteFilaPlazo.isEmpty()) {
           this.lstBloqueComponenteFilaPlazo = null;
       }
       
       // si existe presupuesto modificado y aun no se ha revisado ni cargado (ver si esto es necesario)
       /*if (Objects.nonNull(this.lstBloqueComponenteFila)
               && this.lstPresupuestoFinanciableAcept1.isEmpty()) {
           this.setBAvisoModifPresPlazoOtra(Boolean.TRUE);
       }

       //si existen modificaciones vacias las inicializo a null (ver si esto es necesario)
       if (Objects.nonNull(this.listadoModificacionesPPO)
               && this.listadoModificacionesPPO.isEmpty()) {
           this.listadoModificacionesPPO = null;
       }*/
      
       //En Financiacion se carga todo el contenido de las tablas de presup
       //desde ExpdteBean y aqui solo se carga la tabla de modificaciones
       //por tanto, para saber si tengo avisos tendré que ver si existen 
       //modificaciones en proceso de revisión en algún estado que no sea Resuelta

       List<ExpedienteModifPresPlazoOtraDTO> lstModifAbiertas = this.expedienteModifPresPlazoOtraService.
               buscarPorExpedienteEstadoAbiertas(this.expedienteBean.getIdExpediente());
       
       // VMM 06/10/26 MD-3066: una modificacion en PENDIENTE_ENVIO_COLA solo requiere accion del gestor
       // mientras su resolucion no se haya enviado a la cola de portafirmas (boton amarillo). Tras la
       // firma el batch la deja de nuevo en ese estado, pero entonces ya existe el envio y no hay nada
       // que hacer en la solapa, que debe quedar en el color normal.
       boolean hayModifPendienteDeAccion = false;
       if (Objects.nonNull(lstModifAbiertas)) {
           Integer idPendienteEnvioCola = utilConstantes.getGlobalCatalogosValores()
                   .get(Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA);
           for (ExpedienteModifPresPlazoOtraDTO modif : lstModifAbiertas) {
               boolean esPendienteEnvioCola = modif.getEstadoDTO() != null
                       && idPendienteEnvioCola != null
                       && idPendienteEnvioCola.equals(modif.getEstadoDTO().getIdValor());
               if (!esPendienteEnvioCola || !this.existeEnvioPortafirmasModif(modif.getIdExpedienteModifPresPlazoOtra())) {
                   hayModifPendienteDeAccion = true;
                   break;
               }
           }
       }

       if (hayModifPendienteDeAccion
               && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) { //VMM 06/02/26 MD-2339: no ponemos en rojo la pestaña si es un auditor
           this.setBAvisoModifPresPlazoOtra(Boolean.TRUE);
       }

       if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA)
               .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
               || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
                       .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
               || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(
                       this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
           this.setBAvisoModifPresPlazoOtra(Boolean.FALSE);
       }
   }
   
   /**
    * Recupera el listado de modificaciones PPO que cumple con los
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
   public AtlasListAndCount<ExpedienteModifPresPlazoOtraDTO> obtenerListaModificaciones(int index, int pageSize,
           Object ordenacion, Object filter) {

       AtlasListAndCount<ExpedienteModifPresPlazoOtraDTO> lista = new AtlasListAndCount<>();
       try {

           if (this.expedienteBean != null && this.expedienteBean.getIdExpediente() != null) {
               
               List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
               List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

               if (filtros == null) {
                   filtros = new ArrayList<>();            
               }
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,  
                       this.expedienteBean.getIdExpediente(), Operador.EQUALS));
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

               if (orden == null) {
                   orden = new ArrayList<>();
               }
               orden.add(new AtlasSort(Constantes.Filtro.FILTRO_IDMODIFICACION, true));
               
               List<ExpedienteModifPresPlazoOtraDTO> listaExpedienteModifPPODTO = this.expedienteModifPresPlazoOtraService
                       .buscar(index, pageSize, filtros, orden);             

               lista.setResultados(listaExpedienteModifPPODTO);
               lista.setTotal(this.expedienteModifPresPlazoOtraService.count(filtros));
               
               this.listadoModificacionesPPO = listaExpedienteModifPPODTO;
           }
       } catch (ServiceException exc) {
           log.error("Se ha producido un error al obtener el listado de modificaciones PPO", exc);
           // Se muestra un mensaje de error al usuario
           AtlasMessageUtils
                   .addErrorMessageFromBundle("Error al obtener el listado de modificaciones presupuestarias, plazos y otras");
       }
       return lista;
   }
   
   
   /**
    * Metodo que recupera las listas de presupuestos financiables de cada modificación P/P/O del expediente
    * 
    */
    private void obtenerDatosModifPresupExpediente() {
        try {

            if (Objects.nonNull(this.expedienteBean)) {
                // Recuperamos los datos de la convocatoria
                this.inicializarValoresConvocatoria();

                // Construimos la lista de anualidades
                this.construirLstAnualidades();
                
                //VMM 08/04/26 MD-2638: ajustamos los conceptos en las tablas presupuestarias
                this.esConvocatoriaConceptoPresup = Constantes.AfirmacionNegacion.S
                        .equalsIgnoreCase(convocatoriaDTO.getItConceptoPresup());

                if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdImpreso() != null) {
                    //buscamos el formulario de modificaciones PPO asociado al registro seleccionado
                    ImpresoDTO impresoDTO = this.impresoService.buscarId(this.selectExpedienteModifDTO.getIdImpreso());
    
                    if (Objects.nonNull(impresoDTO.getLstBloqueDTO())) { 
                        //obtener Presupuesto financiable Solicitado en el formulario de modificaciones PPO (no editable)
                        this.obtenerPresupuestoSolicitado(impresoDTO);
                        this.verNumeracion = this.lstBloque.size() > 1;
                    }
    
                    //la primera vez obtener Presupuesto financiable Antes de la Modificación (no editable) 
                    this.obtenerPresupuestoFinanciable(1);
                    //this.obtenerPresupuestoFinanciable(2);
                    
                    this.validarEstilo();
                                       
                    if(this.getLstBloque() != null && !this.getLstBloque().isEmpty()) {
                        this.cargarDatosAyudaConcedidaSolicitada();
                    }
                }
            }
            
            this.esconderBotonCargarPresup();

        } catch (Exception e) {
            log.error("Error en obtenerDatosModifPresupExpediente", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    
    /**
     * Metodo para construir la lista de anualidades del expediente
     */
    private void construirLstAnualidades() {
        int[] anualidadesArray = IntStream.range(1, this.totalAnualidades + 1).toArray();
        this.listaAnualidades = new ArrayList<>();
        for (int arr : anualidadesArray) {
            this.listaAnualidades.add(arr);
        }
    }
    
    
    /**
     * Método para inicializar los valores que vienen de la convocatoria y que se usarán en las modificaciones PPO
     */
    private void inicializarValoresConvocatoria() throws ServiceException {
        if (this.convocatoriaDTO == null) {
            this.convocatoriaDTO = this.convocatoriaService
                .buscarId(this.expedienteBean.getIdConvocatoria());
        }
        this.totalAnualidades = convocatoriaDTO.getNmDurMaxAyuda();

        // VMM 03/09/26: la intensidad a aplicar es la que fijó el gestor para este expediente
        // en el presupuesto financiable (ExpedientePresup.nmPorcentajeIntensidad), no la
        // genérica de la convocatoria. Si no se encontrara (no debería pasar, ya que se valida
        // antes en la revisión de financiación), lo controlamos dejándolo a null en vez de
        // asumir un valor incorrecto; los puntos que la usan ya comprueban que no sea null.
        this.porcentajeIntensidadAyuda = null;
        List<ExpedientePresupDTO> lstPresupIntensidad = this.expedientePresupService
                .consultarExpPresupFinanPorIdExp(this.expedienteBean.getIdExpediente(), 1);
        if (lstPresupIntensidad != null) {
            this.porcentajeIntensidadAyuda = lstPresupIntensidad.stream()
                    .map(ExpedientePresupDTO::getNmPorcentajeIntensidad)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        if (this.porcentajeIntensidadAyuda == null) {
            log.warn("No se ha encontrado el porcentaje de intensidad del presupuesto financiable "
                    + "para el expediente " + this.expedienteBean.getIdExpediente()
                    + "; no se aplicará ningún porcentaje de intensidad en las modificaciones.");
        }

        this.esConvocatoriaConceptoPresup = Constantes.AfirmacionNegacion.S
                .equalsIgnoreCase(convocatoriaDTO.getItConceptoPresup());
    }
    
    
    /**
     * Metodo para obtener los datos del presupuesto financiable solicitado
     *
     * @param impresoDTO
     */
//    private void obtenerPresupuestoSolicitado(ImpresoDTO impresoDTO) {
//        try {
//
//            // Obtener registros de presupuesto
//            this.lstBloque = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE));
//
//            List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
//            if (!this.lstBloque.isEmpty()) {
//                for (BloqueDTO bloqueDTO : this.lstBloque) {
//
//                    lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
//                            .filter(x -> x.getFcBajaFila() == null)
//                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
//                            .collect(Collectors.toList());
//                    
//                    //VMM 07/04/26 MD-2639: ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
//                    FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",impresoDTO.getIdConvocatoria(), 
//                            bloqueDTO.getIdTematica().getIdValor(),impresoDTO.getConvocatoriaActDTO().getIdActor());
//                    
//                    List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
//                    
//                    List<BloqueComponenteDTO> filas = bloqueDTO.getLstBloqueComponenteDTO().stream()
//                            .filter(f -> f.getItFila()).collect(Collectors.toList());
//                    
//                 // VMM 19/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
//                    for (BloqueComponenteDTO p : filas) {
//                        if (p.getTipoFinanciacionDTO() != null) {
//                            Integer orden = listaFinancDato.stream()
//                                .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null
//                                 && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                                .map(FinanciacionDatoDTO::getNmOrden)
//                                 // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
//                                .filter(Objects::nonNull)
//                                .findFirst()
//                                .orElse(9999); // Si no tiene orden, al final
//                                
//                            p.getTipoFinanciacionDTO().setOrdenPartida(orden);
//                        }
//                    }
//                    
//                    if(!filas.isEmpty()) {
//                        filas = filas.stream()
//                                .sorted((c1, c2) -> informeHelperPdfService.compareByNumOrden(c1, c2, listaFinancDato))
//                                .collect(Collectors.toList());
//                    }
//                    
//                    bloqueDTO.setListaBloqueAuxDTO(filas);
//
//                    if (!bloqueDTO.getListaBloqueAuxDTO().isEmpty()) {
//
//                        // Construimos la lista de columnas Presupuesto
//                        this.lstBloqueComponenteColumna = lstBloqueComponenteDTO.stream()
//                                .filter(p -> Objects.isNull(p.getFcBajaFila())
//                                        && Boolean.TRUE.equals(p.getItColumna()))
//                                .collect(Collectors.toList());
//
//                        //VMM 08/04/26 MD-2638: ajustamos los conceptos en las tablas presupuestarias
//                        if (!this.lstBloqueComponenteColumna.isEmpty() && (esConvocatoriaConceptoPresup == null
//                                || !this.esConvocatoriaConceptoPresup)) {
//                            this.obtenerFilColPresupSolicitado(bloqueDTO);
//                        } else if (!this.lstBloqueComponenteColumna.isEmpty() && Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
//                            this.obtenerFilColPresupSolicitadoConcepto(bloqueDTO);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
//        }
//    }
        
//    private void obtenerPresupuestoSolicitado(ImpresoDTO impresoDTO) {
//        try {
//
//            // Obtener registros de presupuesto
//            this.lstBloque = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE));
//
//            List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
//            if (!this.lstBloque.isEmpty()) {
//                for (BloqueDTO bloqueDTO : this.lstBloque) {
//
//                    lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
//                            .filter(x -> x.getFcBajaFila() == null)
//                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
//                            .collect(Collectors.toList());
//                    
//                    //VMM 07/04/26 MD-2639: ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
//                    FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",impresoDTO.getIdConvocatoria(), 
//                            bloqueDTO.getIdTematica().getIdValor(),impresoDTO.getConvocatoriaActDTO().getIdActor());
//                    
//                    List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
//                    
//                    List<BloqueComponenteDTO> filas = bloqueDTO.getLstBloqueComponenteDTO().stream()
//                            .filter(f -> f.getItFila()).collect(Collectors.toList());
//                    
//                    // VMM 19/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
//                    for (BloqueComponenteDTO p : filas) {
//                        if (p.getTipoFinanciacionDTO() != null) {
//                            Integer orden = listaFinancDato.stream()
//                                .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null
//                                 && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                                .map(FinanciacionDatoDTO::getNmOrden)
//                                 // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
//                                .filter(Objects::nonNull)
//                                .findFirst()
//                                .orElse(9999); // Si no tiene orden, al final
//                                
//                            p.getTipoFinanciacionDTO().setOrdenPartida(orden);
//                        }
//                    }
//                    
//                    if(!filas.isEmpty()) {
//                        filas = filas.stream()
//                                .sorted((c1, c2) -> informeHelperPdfService.compareByNumOrden(c1, c2, listaFinancDato))
//                                .collect(Collectors.toList());
//                        
//                        // ====================================================================
//                        // NUEVO: Recuperamos y asignamos los documentos de SAI_EXPEDIENTE_DOC
//                        // ====================================================================
//                        for (BloqueComponenteDTO fila : filas) {
//                            if (fila.getDatos() != null && !fila.getDatos().isEmpty() && fila.getDatos().get(0) != null) {
//                                // Obtenemos el idDato mapeado de la primera posición (Concepto / Fila)
//                                // Revisa si el identificador se obtiene mediante getIdDato() o similar en DatoDTO
//                                Integer idDatoInt = fila.getDatos().get(0).getIdDato(); 
//                                
//                                if (idDatoInt != null) {
//                                    Long idDato = idDatoInt.longValue();
//                                    
//                                    List<AtlasFilter> filtros = new ArrayList<>() ;
//                                    List<AtlasSort> orden = new ArrayList<>() ;
//
//                                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DATO_ID_DATO,idDato, Operador.EQUALS));
//                                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//
//                                    List<ExpedienteDocDTO> listaDocs = this.expedienteDocService.buscar(0,-1 , filtros, orden);
//                                    
//                                    if (listaDocs != null) {
//                                        fila.setDocumentos(listaDocs);
//                                    } else {
//                                        fila.setDocumentos(new ArrayList<>());
//                                    }
//                                }
//                            }
//                        }
//                        // ====================================================================
//                    }
//                    
//                    bloqueDTO.setListaBloqueAuxDTO(filas);
//
//                    if (!bloqueDTO.getListaBloqueAuxDTO().isEmpty()) {
//
//                        // Construimos la lista de columnas Presupuesto
//                        this.lstBloqueComponenteColumna = lstBloqueComponenteDTO.stream()
//                                .filter(p -> Objects.isNull(p.getFcBajaFila())
//                                        && Boolean.TRUE.equals(p.getItColumna()))
//                                .collect(Collectors.toList());
//
//                        //VMM 08/04/26 MD-2638: ajustamos los conceptos en las tablas presupuestarias
//                        if (!this.lstBloqueComponenteColumna.isEmpty() && (esConvocatoriaConceptoPresup == null
//                                || !this.esConvocatoriaConceptoPresup)) {
//                            this.obtenerFilColPresupSolicitado(bloqueDTO);
//                        } else if (!this.lstBloqueComponenteColumna.isEmpty() && Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
//                            this.obtenerFilColPresupSolicitadoConcepto(bloqueDTO);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
//        }
//    }
    
    private void obtenerPresupuestoSolicitado(ImpresoDTO impresoDTO) {
        try {
            // 1. Obtener registros de presupuesto en bruto desde el servicio
            this.lstBloque = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE));
            
            // si algún bloque no tiene temática es porque es un subbloque y debemos indicar la temática de su padre
            for (BloqueDTO bloqueDTO : this.lstBloque) {
                if (bloqueDTO.getIdTematica() == null
                        || bloqueDTO.getIdTematica().getIdValor() == null) {
                    BloqueDTO bloquePadreDTO = this.bloqueService
                            .buscarByIdBlqPad(bloqueDTO.getIdBloquePadre());
                    if (bloquePadreDTO != null && bloquePadreDTO.getIdTematica() != null && bloquePadreDTO.getIdTematica().getIdValor() != null) {
                        bloqueDTO.setIdTematicaPadre(bloquePadreDTO.getIdTematica());
                    }
                }
            }

            List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
            if (!this.lstBloque.isEmpty()) {
                for (BloqueDTO bloqueDTO : this.lstBloque) {

                    // Filtrado y ordenación inicial por nmOrden de los componentes
                    lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                            .collect(Collectors.toList());
                    
                    // VMM 07/04/26 MD-2639: Ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
                    Integer idTematicaOrden = null;
                    if (bloqueDTO.getIdTematica() != null && bloqueDTO.getIdTematica().getIdValor() != null) {
                        idTematicaOrden = bloqueDTO.getIdTematica().getIdValor();
                    } else if (bloqueDTO.getIdTematicaPadre() != null && bloqueDTO.getIdTematicaPadre().getIdValor() != null) {
                        idTematicaOrden = bloqueDTO.getIdTematicaPadre().getIdValor();
                    }

                    FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP", impresoDTO.getIdConvocatoria(),
                            idTematicaOrden, impresoDTO.getConvocatoriaActDTO().getIdActor());

                    List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
                    
                    List<BloqueComponenteDTO> filas = bloqueDTO.getLstBloqueComponenteDTO().stream()
                            .filter(f -> f.getItFila()).collect(Collectors.toList());
                    
                    // VMM 19/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
                    for (BloqueComponenteDTO p : filas) {
                        if (p.getTipoFinanciacionDTO() != null) {
                            Integer orden = listaFinancDato.stream()
                                .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null
                                     && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                                .map(FinanciacionDatoDTO::getNmOrden)
                                // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(9999); // Si no tiene orden, al final
                                
                            p.getTipoFinanciacionDTO().setOrdenPartida(orden);
                        }
                    }
                    
                    if (!filas.isEmpty()) {
                        filas = filas.stream()
                                .sorted((c1, c2) -> informeHelperPdfService.compareByNumOrdenAndConcept(c1, c2, listaFinancDato))
                                .collect(Collectors.toList());
                    }
                    
                    // Se asigna la lista procesada al bloque
                    bloqueDTO.setListaBloqueAuxDTO(filas);

                    if (!bloqueDTO.getListaBloqueAuxDTO().isEmpty()) {

                        // Construimos la lista de columnas del Presupuesto
                        this.lstBloqueComponenteColumna = lstBloqueComponenteDTO.stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila())
                                        && Boolean.TRUE.equals(p.getItColumna()))
                                .collect(Collectors.toList());

                        // VMM 08/04/26 MD-2638: Ajustamos y pivotamos los conceptos/anualidades en las tablas presupuestarias
                        if (!this.lstBloqueComponenteColumna.isEmpty() && (esConvocatoriaConceptoPresup == null || !this.esConvocatoriaConceptoPresup)) {
                            this.obtenerFilColPresupSolicitado(bloqueDTO);
                        } else if (!this.lstBloqueComponenteColumna.isEmpty() && Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                            this.obtenerFilColPresupSolicitadoConcepto(bloqueDTO);
                        }

                     // ====================================================================
                     // NUEVO SITIO: Buscamos los documentos tras la transformación final
                     // ====================================================================
                     List<BloqueComponenteDTO> listaFilasFinales = bloqueDTO.getListaBloqueAuxDTO();
                     if (listaFilasFinales != null) {
                         for (BloqueComponenteDTO filaFinal : listaFilasFinales) {
                             
                             if (filaFinal.getDatos() != null && !filaFinal.getDatos().isEmpty() && filaFinal.getDatos().get(0) != null) {
                                 Integer idDatoInt = filaFinal.getDatos().get(0).getIdDato(); 
                                 
                                 if (idDatoInt != null) {
                                     Long idDato = idDatoInt.longValue();
                                     
                                     List<AtlasFilter> filtros = new ArrayList<>();
                                     List<AtlasSort> orden = new ArrayList<>();

                                     filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DATO_ID_DATO, idDato, Operador.EQUALS));
                                     filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                                     // Buscamos todos los documentos asociados a este idDato
                                     List<ExpedienteDocDTO> listaDocs = this.expedienteDocService.buscar(0, -1, filtros, orden);
                                     
                                     List<ExpedienteDocDTO> docsOrdenados = new ArrayList<>();
                                     
                                     if (listaDocs != null && !listaDocs.isEmpty()) {
                                         // Ordenamos de más nuevo a más antiguo por fcAltaFila
                                         docsOrdenados = listaDocs.stream()
                                                 .filter(d -> d.getFcAltaFila() != null)
                                                 // VMM 14/10/26 MD-3141: solo documentos con fichero (cdFica). Los que el
                                                 // beneficiario borro antes de presentar quedan en SAII_EXPEDIENTE_DOC sin
                                                 // cdFica (ni nombre de fichero) y no son ficheros existentes: con ellos
                                                 // se pintaban los iconos de descarga e historico del concepto y la
                                                 // descarga fallaba (DescargaFicaServlet sin cdFica) sacando del programa.
                                                 .filter(d -> d.getCdFica() != null)
                                                 .sorted((d1, d2) -> d2.getFcAltaFila().compareTo(d1.getFcAltaFila()))
                                                 .collect(Collectors.toList());
                                     }

                                     // VMM 30/09/26 MD-3070: rellenamos nombreFicheroFica (ver enriquecerNombreFicheroFica) -
                                     // expedienteDocService.buscar() usa el converter generico, que no lo informa.
                                     this.enriquecerNombreFicheroFica(docsOrdenados);

                                     // Seteamos la lista COMPLETA ordenada. Ahora la modal del histórico podrá leerlos todos
                                     filaFinal.setDocumentos(docsOrdenados);
                                 }
                             }
                         }
                     }
                     // ====================================================================
                     // ====================================================================
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }
    
    public void descargarDocumentoFica(String cdFica) {
        if (cdFica != null && !cdFica.trim().isEmpty()) {
            try {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext externalContext = facesContext.getExternalContext();
                
                // Construimos la URL hacia el servlet metiendo el parámetro cdFica
                String urlServlet = externalContext.getRequestContextPath() + "/DescargaFicaServlet?cdFica=" + cdFica;
                
                // Redirigimos la petición HTTP directamente al servlet
                externalContext.redirect(urlServlet);
                
                // Informamos a JSF de que la respuesta ya ha sido gestionada
                facesContext.responseComplete();
                
            } catch (IOException e) {
                log.error("Error al redirigir la descarga al DescargaFicaServlet: " + e.getMessage(), e);
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
            }
        }
    }
    
    /**
     * VMM 30/09/26 MD-3070: nombreFicheroFica no es un campo persistido en SAII_EXPEDIENTE_DOC (no existe en la
     * entidad), asi que el converter generico (usado por expedienteDocService.buscar()) nunca lo rellena
     * - hay que pedirselo explicitamente a FICA a partir de cdFica, igual que ya hacen otros puntos del
     * codigo (ver ExpedienteServiceImpl.recuperarNombreFicheroFica). Sin esto, la columna "Nombre de
     * Documento" del dialogo de Historico de Documentos salia siempre en blanco.
     */
    private void enriquecerNombreFicheroFica(List<ExpedienteDocDTO> documentos) {
        if (documentos == null) {
            return;
        }
        for (ExpedienteDocDTO documento : documentos) {
            try {
                documento.setNombreFicheroFica(this.expedienteService.recuperarNombreFicheroFica(documento.getCdFica()));
            } catch (ServiceException e) {
                log.error("Error recuperando nombre de fichero FICA para cdFica=" + documento.getCdFica(), e);
            }
        }
    }

    public void prepararHistorico(BloqueComponenteDTO presup) {
        if (presup != null && presup.getDocumentos() != null) {
            // 1. Guardamos directamente la lista de documentos asociados a esa fila
            this.documentosHistoricoSeleccionado = presup.getDocumentos();
        } else {
            // Si por algún motivo viniera nulo, inicializamos vacío para evitar NullPointerException
            this.documentosHistoricoSeleccionado = new ArrayList<>();
        }
        

        PrimeFaces.current().executeScript("PF('dialogHistoricoDocsWV').show();");
    }
    
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocDTO> obtenerListaHistoricoDocsBean(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        
        // Cambiamos el genérico al tipo de tu DTO: ExpedienteDocDTO
        AtlasListAndCount<ExpedienteDocDTO> listadoDatos = new AtlasListAndCount<>();
        
        try {
            if (this.documentosHistoricoSeleccionado != null && !this.documentosHistoricoSeleccionado.isEmpty()) {
                
                int totalRegistros = this.documentosHistoricoSeleccionado.size();
                
                // Calculamos los índices de corte para la paginación en memoria
                int desde = index;
                int hasta = Math.min(index + pageSize, totalRegistros);
                
                // Troceamos la lista para extraer solo los 5 registros de la página actual
                List<ExpedienteDocDTO> listadoPaginado = new ArrayList<>(
                    this.documentosHistoricoSeleccionado.subList(desde, hasta)
                );
                
                // Asignamos los datos usando los métodos de Atlas
                listadoDatos.setResultados(listadoPaginado);
                listadoDatos.setTotal(totalRegistros);
                
            } else {
                listadoDatos.setResultados(new ArrayList<>());
                listadoDatos.setTotal(0);
            }
        } catch (Exception exc) {
            log.error("Error al paginar en memoria el histórico de documentos", exc);
        }
        
        return listadoDatos;
    }
    
    
    /**
    * Método para obtener las filas y columnas de la tabla de presupuestos solicitados
    * @throws ServiceException
    */
   private void obtenerFilColPresupSolicitado(BloqueDTO bloque) throws ServiceException {

       // Construimos lista con idColumna
       this.construirLstColsPresup();

       if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
           
           // Recuperar datos de los bloques componente fila
           for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
               List<DatoDTO> lstDatoDTO = this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(), 
                       this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), fila.getIdBloqueComponente(), true);
               
               //filtramos por los más recientes por si se guardó mas de una modificacion en el fomulario
               if (lstDatoDTO.size() >= (this.totalAnualidades * 2)) {              
                   lstDatoDTO = lstDatoDTO.stream()
                           .skip(Math.max(0, lstDatoDTO.size() - this.totalAnualidades)) // Saltar los primeros elementos
                           .collect(Collectors.toList());
               }
                   
               fila.setDatos(lstDatoDTO);
           }

           // Calculamos los montos totales por fila
           this.calcularMontosTotalesPorFila(bloque);
    
           // Preparamos espacio para totales por fila
           BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
           bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
           bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("TOTAL");
           bloqueComponenteTotalFila.setDatos(new ArrayList<>());
    
           // Calculamos los montos totales por columna
           this.calcularMontosTotalesPorColumna(bloque, bloqueComponenteTotalFila);
    
           bloque.getListaBloqueAuxDTO().add(bloqueComponenteTotalFila);
    
           this.lstBloqueComponenteFila = bloque.getListaBloqueAuxDTO();
       }
   }
   
   
   /**
    * Método para obtener las filas y columnas de la tabla de presupuestos solicitados (con detalle de conceptos)
    * @throws ServiceException
    */
   private void obtenerFilColPresupSolicitadoConcepto(BloqueDTO bloque) throws ServiceException {

       if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
           
           // Recuperar datos de los bloques componente fila
           List<BloqueComponenteDTO> lstFilasAux = bloque.getListaBloqueAuxDTO().stream()
                   .filter(f -> f.getItFila()).collect(Collectors.toList());
           
           List<BloqueComponenteDTO> lstFilas = new ArrayList<>();
           
           for(BloqueComponenteDTO bloqueComp: lstFilasAux) {
               List<DatoDTO> lstDatoDTO = this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(), 
                       this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), bloqueComp.getIdBloqueComponente(), true);
                          
                   if(lstDatoDTO.size() == listaAnualidades.size()+1) {

                       bloqueComp.setDatos(
                               lstDatoDTO.stream()
                               // Filtrar datos donde el idExpediente del DatoDTO sea igual al this.idExpediente
                               .filter(dato -> dato.getExpedienteDTO() != null 
                                   && dato.getExpedienteDTO().getIdExpediente() != null
                                   && dato.getExpedienteDTO().getIdExpediente().equals(this.idExpediente))
                               // Ordenar por nmColumna
                               .sorted(Comparator.comparing(DatoDTO::getNmColumna))
                               .collect(Collectors.toList())
                       );
                       
                       double sumaFila = lstDatoDTO.stream()
                               .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                               .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                                       && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                               ? this.parseImporteDsValor(dato.getDsValor())
                                               : 0)
                               .sum();

                       DatoDTO dto = new DatoDTO();
                       dto.setDsValor(String.valueOf(sumaFila));
                       dto.setEsMontoTotal(Boolean.TRUE);
                       dto.setNmColumna(bloqueComp.getDatos().size());
                       // VMM 14/10/26 MD-3138: el total de la fila se anadia a lstDatoDTO (la lista original),
                       // no a los datos que pinta la tabla (bloqueComp.getDatos(), que es una copia filtrada),
                       // asi que en una partida con un unico concepto la fila se quedaba sin celda de total y
                       // getSumaTotalGrupoPres (que lee el indice listaAnualidades.size() + 1) devolvia 0 en
                       // el SUBTOTAL de la columna TOTAL. La rama de varios conceptos ya lo anade a los datos.
                       bloqueComp.getDatos().add(dto);

                       lstFilas.add(bloqueComp);
                       
                   }else if(lstDatoDTO.size() > listaAnualidades.size()+2) {

                       int posCelda= 0;
                       bloqueComp.setDatos(
                               lstDatoDTO.stream()
                               // Filtrar datos donde el idExpediente del DatoDTO sea igual al this.idExpediente
                               .filter(dato -> dato.getExpedienteDTO() != null 
                                   && dato.getExpedienteDTO().getIdExpediente() != null
                                   && dato.getExpedienteDTO().getIdExpediente().equals(this.idExpediente))
                               // Ordenar por nmColumna
                               .sorted(Comparator.comparing(DatoDTO::getNmFila))
                               .collect(Collectors.toList())
                       );
                       
                       for (int j=posCelda; j< lstDatoDTO.size();j++){
                           List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                           BloqueComponenteDTO bloqueConConcepto= new BloqueComponenteDTO();
                           
                           for (int index = 0; index < listaAnualidades.size()+1; index++) {
                               datosFilaConcepto.add(lstDatoDTO.get(posCelda));
                               posCelda++;
                               j=posCelda;
                           }
                           
                           datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                           bloqueConConcepto.setIdBloque(bloqueComp.getIdBloque());
                           bloqueConConcepto.setDatos(datosFilaConcepto);
                           bloqueConConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                           bloqueConConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                           bloqueConConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                           bloqueConConcepto.setNmOrden(bloqueComp.getNmOrden());
                           bloqueConConcepto.setItFila(bloqueComp.getItFila());
                           bloqueConConcepto.setItColumna(bloqueComp.getItColumna());
                           bloqueConConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                           bloqueConConcepto.setItRelacion(bloqueComp.getItRelacion());
                           bloqueConConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                           bloqueConConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                           
                           double sumaFila = datosFilaConcepto.stream()
                                   .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                                   .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                                           && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                                   ? this.parseImporteDsValor(dato.getDsValor())
                                                   : 0)
                                   .sum();

                           DatoDTO dto = new DatoDTO();
                           dto.setDsValor(String.valueOf(sumaFila));
                           dto.setEsMontoTotal(Boolean.TRUE);
                           if(bloqueConConcepto.getDatos() != null && !bloqueConConcepto.getDatos().isEmpty()) {
                               dto.setNmColumna(bloqueConConcepto.getDatos().size()); 
                               dto.setNmFila(bloqueConConcepto.getDatos().get(0).getNmFila() != null 
                                       ? bloqueConConcepto.getDatos().get(0).getNmFila() : 0); 
                           }
                           
                           bloqueConConcepto.getDatos().add(dto);
                           log.info("agregando monto total " + sumaFila + " en la fila " );
                        
                           lstFilas.add(bloqueConConcepto);                   
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
               
               // Preparamos espacio para totales por fila
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
                           sumaColumna += this.parseImporteDsValor(fila.getDatos().get(i).getDsValor());
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

               this.lstBloqueComponenteFila = lstFilas;
       }
   }
   
   
   /**
    * Método para calcular los montos totales por columna de la tabla de presupuestos solicitados
    */
    private void calcularMontosTotalesPorColumna(BloqueDTO bloque,
            BloqueComponenteDTO bloqueComponenteTotalFila) {
        for (int i = 0; i < this.listaNcolPresupuesto.size(); i++) {
           double sumaColumna = 0;

           for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
               if (!fila.getDatos().isEmpty() && fila.getDatos().size() > i
                       && StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())) {
                   sumaColumna += this.parseImporteDsValor(fila.getDatos().get(i).getDsValor());
               }
           }   
           // log.info("agregando monto total " + sumaColumna + " en la columna " + i);

           DatoDTO dto = new DatoDTO();
           dto.setDsValor(String.valueOf(sumaColumna));
           dto.setEsMontoTotal(Boolean.TRUE);
           bloqueComponenteTotalFila.getDatos().add(dto);
       }
    }
    

    /**
     * Método para calcular los montos totales por fila de la tabla de presupuestos solicitados
     */
    private void calcularMontosTotalesPorFila(BloqueDTO bloque) {
        for (int i = 0; i < bloque.getListaBloqueAuxDTO().size(); i++) {
           double sumaFila = bloque.getListaBloqueAuxDTO().get(i).getDatos().stream()
                   .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                           && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                   ? this.parseImporteDsValor(dato.getDsValor())
                                   : 0).sum();

           DatoDTO dto = new DatoDTO();
           dto.setDsValor(String.valueOf(sumaFila));
           dto.setEsMontoTotal(Boolean.TRUE);
           bloque.getListaBloqueAuxDTO().get(i).getDatos().add(dto);

           // log.info("agregando monto total " + sumaFila + " en la fila " + i);  
       }
    }
    

    /**
     * Método para construir la lista de columnas de las tablas de presupuestos
     */
    private void construirLstColsPresup() {
       int[] nColArray = IntStream.range(0, this.listaAnualidades.size() + 1).toArray();
       this.listaNcolPresupuesto = new ArrayList<>();

       for (int arr : nColArray) {
           this.listaNcolPresupuesto.add(arr);
       }
    }
   
   
   /**
    * Metodo para copiar los datos de presupuestos financiables solicitados a ayuda
    * concedida
    */
   private void cargarDatosAyudaConcedidaSolicitada() {
       try {
           // Procesar tabla 1 de presupuestos solicitados
           this.lstPresupuestoAyudaConcedidaSolicitada = this.procesarTablaModifPresupSolic(0);

           // Si hay otra tabla de presupuestos solicitados, procesarla
           if (this.getLstBloque().size() > 1) {
               this.lstPresupuestoAyudaConcedidaSolicitada2 = this.procesarTablaModifPresupSolic(1);
           }
           
           PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

       } catch (Exception e) {
           log.error(e.getMessage(), e);
           AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
       }
   }
   
   
   /**
    * Método para procesar una tabla de presupuestos solicitados por bloques y aplicar porcentaje de intensidad.
    */
   private List<BloqueComponenteDTO> procesarTablaModifPresupSolic(int index) {
       List<BloqueComponenteDTO> listaModifPresupSolic = new ArrayList<>();
       
       //Generamos lista para ayudaConcedida de la modificacion solicitada
       for (BloqueComponenteDTO bloqueComp : this.getLstBloque().get(index).getListaBloqueAuxDTO()) {
           BloqueComponenteDTO nuevoBloque = new BloqueComponenteDTO(bloqueComp);
           
           if (esConvocatoriaConceptoPresup == null
                   || !this.esConvocatoriaConceptoPresup) {
               
               for (DatoDTO dato : nuevoBloque.getDatos()) {
                   if (dato.getDsValor() != null && !dato.getDsValor().isEmpty()) {
                       //Aplicamos porcentaje Intensidad para los datos de ayuda concedida Solicitada
                       double dsValorDouble = Double.parseDouble(dato.getDsValor());
                       // VMM 03/09/26: si no se ha podido determinar la intensidad del gestor, no
                       // aplicamos ningún porcentaje en vez de lanzar una NullPointerException.
                       if (this.porcentajeIntensidadAyuda != null) {
                           dato.setDsValor(Double.toString(dsValorDouble * (porcentajeIntensidadAyuda / 100)));
                       }
                   }
               }
           }else {
               for (int i = 0; i < nuevoBloque.getDatos().size(); i++) {
                   DatoDTO dato = nuevoBloque.getDatos().get(i);
                   // Ignorar columna 0 (concepto)
                   if (i == 0) {
                       continue;
                   }
                   if (dato.getDsValor() != null && !dato.getDsValor().isEmpty()
                           /*&& Boolean.FALSE.equals(dato.getEsMontoTotal())*/) {

                       try {
                           double dsValorDouble = Double.parseDouble(dato.getDsValor());
                           if (this.porcentajeIntensidadAyuda != null) {
                               dato.setDsValor(Double.toString(dsValorDouble * (porcentajeIntensidadAyuda / 100)));
                           }
                       } catch (NumberFormatException e) {
                           // Por si acaso hay algún valor raro (extra seguridad)
                           log.warn("Valor no numérico ignorado: " + dato.getDsValor());
                       }
                   }
               }
           }
           listaModifPresupSolic.add(nuevoBloque);
       }
            
       return listaModifPresupSolic;
   }
     
    
    /**
     * MD-2920: como la tabla SAII_EXPE_MOD_PRE_MONTO guarda un histórico (inserta en vez de
     * actualizar), deduplicamos en lectura quedándonos con el registro vigente (el más reciente)
     * por cada monto original (idExpedientePresupMonto) que fue modificado
     */
    private List<ExpedienteModifPresupMontoDTO> deduplicarPresupModMasRecientePorMontoOriginal(
            List<ExpedienteModifPresupMontoDTO> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }
        // VMM 11/09/26 MD-2725: los conceptos nuevos (dados de alta directamente en
        // saii_solicitud) no tienen idExpedientePresupMonto - el filtro original los
        // descartaba aqui y nunca llegaban a pintarse en la tabla de Aceptadas. Se
        // dejan pasar tal cual (no necesitan dedup, cada fila insertada es unica) y
        // solo se aplica el dedup por "mas reciente" a los que si tienen monto original.
        List<ExpedienteModifPresupMontoDTO> conceptosNuevos = lista.stream()
                .filter(m -> Objects.isNull(m.getIdExpedientePresupMonto()))
                .collect(Collectors.toList());

        List<ExpedienteModifPresupMontoDTO> conceptosExistentes = new ArrayList<>(lista.stream()
                .filter(m -> Objects.nonNull(m.getIdExpedientePresupMonto()))
                .collect(Collectors.toMap(
                        ExpedienteModifPresupMontoDTO::getIdExpedientePresupMonto,
                        m -> m,
                        (m1, m2) -> {
                            Date f1 = m1.getFcModifFila() != null ? m1.getFcModifFila() : m1.getFcAltaFila();
                            Date f2 = m2.getFcModifFila() != null ? m2.getFcModifFila() : m2.getFcAltaFila();
                            if (f1 == null) {
                                return m2;
                            }
                            if (f2 == null) {
                                return m1;
                            }
                            int cmp = f1.compareTo(f2);
                            if (cmp != 0) {
                                return cmp > 0 ? m1 : m2;
                            }
                            return (m1.getIdExpedienteModifPresupMonto() != null && m2.getIdExpedienteModifPresupMonto() != null
                                    && m1.getIdExpedienteModifPresupMonto() > m2.getIdExpedienteModifPresupMonto()) ? m1 : m2;
                        },
                        java.util.LinkedHashMap::new))
                .values());

        conceptosExistentes.addAll(conceptosNuevos);
        return conceptosExistentes;
    }

    /**
     * Metodo que recupera los montos de presupuestos financiables concedidos junto a la ayuda concedida de cada modificación P/P/O
     *
     * la primera vez no cargamos la tabla de presupuestos a aceptar porque lo hace el boton cargar
     * copiamos los resultados temporales guardados de los presupuestos concedidos del expediente a la lista que será aceptada
     * las siguientes veces, se cogen de la tabla temporal los importes iniciales concedidos para antes mod
     * y los ultimos importes guardados por el gestor para la tabla de presup aceptados
     *
     * @param tipoModif
     * @param nmPresup
     */
   private void obtenerPresupuestoFinanciable(Integer nmPresup) {
       try {
           // Consultamos si hay ya guardados o volcados presup financiables revisados por el gestor en la tabla temporal
           List<ExpedienteModifPresupMontoDTO> lstExpModifPresupMontoDTO = this.expedienteModifPresupMontoService.
                   consultarModifPresupPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());

           lstExpModifPresupMontoDTO = this.deduplicarPresupModMasRecientePorMontoOriginal(lstExpModifPresupMontoDTO);

           // Consultamos los presupuestos financiables actuales del expediente
           this.lstPresupuestoFinanciable = this.expedientePresupService.consultarExpPresupFinanPorIdExp
                   (this.expedienteBean.getIdExpediente(), nmPresup);

           double sumaTotal = 0.0;
           int totalColumnasT1 = 0;
           int totalColumnasT3 = 0;
           double sumaTotalTempConcedido = 0.0;
           double sumaTotalTempAceptado = 0.0;
           
           List<ExpedienteModifPresupMontoDTO> montosColumnasTemp = new ArrayList<>();

           if (!this.lstPresupuestoFinanciable.isEmpty()) {

               FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                       this.lstPresupuestoFinanciable.get(0).getTematicaDTO().getIdValor(), this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());

               List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();

               for (ExpedientePresupDTO p : this.lstPresupuestoFinanciable) {
                   if (p.getTipoFinanciacionDTO() != null) {
                       Integer orden = listaFinancDato.stream()
                           .filter(e -> e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                           .map(FinanciacionDatoDTO::getNmOrden)
                           .findFirst()
                           .orElse(9999);

                       p.getTipoFinanciacionDTO().setOrdenPartida(orden);
                   }
               }

               this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciable.stream()
                       .sorted((p1, p2) -> informeHelperPdfService.compareByNumOrdenPresup(p1, p2, listaFinancDato))
                       .collect(Collectors.toList());
                                              

               //metemos monto total a la tercera tabla
               if (!lstExpModifPresupMontoDTO.isEmpty()) {

                   if (totalColumnasT3 == 0) {
                       totalColumnasT3 = lstExpModifPresupMontoDTO.size();
                   }

                   double totalTempConcedido = lstExpModifPresupMontoDTO.stream()
                           .mapToDouble(m -> m.getNmImporteConcedido() != null ? m.getNmImporteConcedido() : 0.0).sum();
                   sumaTotalTempConcedido += totalTempConcedido;

                   double totalTempAceptado = lstExpModifPresupMontoDTO.stream()
                           .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0).sum();
                   sumaTotalTempAceptado += totalTempAceptado;

                   ExpedienteModifPresupMontoDTO montoTotalTemp = new ExpedienteModifPresupMontoDTO();
                   montoTotalTemp.setEsMontoTotal(Boolean.TRUE);
                   montoTotalTemp.setNmImporteConcedido(totalTempConcedido);
                   montoTotalTemp.setNmImporte(totalTempAceptado);                     

                   lstExpModifPresupMontoDTO.add(montoTotalTemp);
               }

               // Metemos monto total a la primera tabla
               for (int i = 0; i < this.lstPresupuestoFinanciable.size(); i++) {
                   ExpedientePresupDTO presupuesto = this.lstPresupuestoFinanciable.get(i);
                                          
                   List<ExpedientePresupMontoDTO> montosPresupuesto = presupuesto.getLstExpedientePresupMontoDTO();
                   
                   if (totalColumnasT1 == 0 && montosPresupuesto != null) {
                       totalColumnasT1 = montosPresupuesto.size();
                   }
                   
                   double total = montosPresupuesto.stream()
                           .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0).sum();
                   sumaTotal += total;
                   
                   ExpedientePresupMontoDTO montoTotal = new ExpedientePresupMontoDTO();
                   montoTotal.setEsMontoTotal(Boolean.TRUE);
                   montoTotal.setNmImporte(total);

                   presupuesto.getLstExpedientePresupMontoDTO().add(montoTotal);

               }

               // metemos la fila de totales en la tercera tabla
               if (!lstExpModifPresupMontoDTO.isEmpty()) {
//                   List<ExpedienteModifPresupMontoDTO> montosColumnasTemp = new ArrayList<>();

                   // Iteramos basándonos en la lista oficial de anualidades del bean (ej: 1, 2, 3...)
                   // OJO: lstExpModifPresupMontoDTO es una lista PLANA (todas las partidas juntas, sin particionar),
                   // así que el filtrado y la suma se hacen UNA sola vez por anualidad. Antes se recorría además
                   // cada partida de lstPresupuestoFinanciable sin usarla para filtrar, así que la suma de la
                   // lista plana se repetía y se acumulaba una vez por cada partida existente.
                   for (Integer anualidad : this.listaAnualidades) { //para cada anualidad
                       //vamos sumando todos los importes para calcular los totales modificados
                       //VMM: en convocatorias con conceptos la columna 0 es el propio concepto (no un importe),
                       //por lo que las anualidades reales quedan desplazadas una posición
                       List<ExpedienteModifPresupMontoDTO> filtrados;

                       if (Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                           // Con conceptos: la columna 0 es el concepto, así que la anualidad real coincide con la anualidad del monto
                           filtrados = lstExpModifPresupMontoDTO.stream()
                                   .filter(monto -> monto != null && Objects.nonNull(monto.getNmAnualidad()))
                                   .filter(monto -> monto.getNmAnualidad() != 0)
                                   .filter(monto -> anualidad.equals(monto.getNmAnualidad()))
                                   .collect(Collectors.toList());
                       } else {
                           // Sin conceptos: la anualidad real empieza en 1 y la del monto en 0 por tanto hay que sumarle 1
                           filtrados = lstExpModifPresupMontoDTO.stream()
                                   .filter(monto -> monto != null && Objects.nonNull(monto.getNmAnualidad()))
                                   .filter(monto -> anualidad.equals(monto.getNmAnualidad() + 1))
                                   .collect(Collectors.toList());
                       }

                       double sumaColumnaTempConcedido = filtrados.stream()
                               .mapToDouble(m -> m.getNmImporteConcedido() != null ? m.getNmImporteConcedido() : 0.0).sum();
                       double sumaColumnaTempAceptado = filtrados.stream()
                               .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0).sum();

                       ExpedienteModifPresupMontoDTO columnaTotalTemp = new ExpedienteModifPresupMontoDTO();
                       columnaTotalTemp.setEsMontoTotal(Boolean.TRUE);
                       columnaTotalTemp.setNmImporteConcedido(sumaColumnaTempConcedido);
                       columnaTotalTemp.setNmImporte(sumaColumnaTempAceptado);
                       montosColumnasTemp.add(columnaTotalTemp);
                   }

                   // Celda TOTAL de TOTALES de la fila TOTAL (última posición que lee el xhtml sin conceptos).
                   // sumaTotalTempConcedido/sumaTotalTempAceptado ya están calculadas más arriba sobre toda
                   // la lista plana lstExpModifPresupMontoDTO, así que representan el gran total correcto.
                   ExpedienteModifPresupMontoDTO totalDeTotalesTemp = new ExpedienteModifPresupMontoDTO();
                   totalDeTotalesTemp.setEsMontoTotal(Boolean.TRUE);
                   totalDeTotalesTemp.setNmImporteConcedido(sumaTotalTempConcedido);
                   totalDeTotalesTemp.setNmImporte(sumaTotalTempAceptado);
                   montosColumnasTemp.add(totalDeTotalesTemp);

                   //VMM: se quita este bloque de "filaTotalTemp" porque no se estaba usando para nada útil y
                   //ademas causaba un bug: la fila TOTAL real de la tabla 3 (aceptadas) NO sale de aqui, se
                   //construye despues por separado con "filaTotalModif" a partir de montosColumnasTemp. Y el
                   //"lstExpModifPresupMontoDTO.add(montoColumnaTotalTemp)" tampoco servia porque ese monto no
                   //tiene idExpedientePresupMonto y copiarListaPresupuestosMod lo descarta al construir su mapa.
                   //Lo unico que hacia de verdad era anadir una fila "TOTAL" a medio rellenar (sin
                   //lstExpedientePresupMontoDTO) a this.lstPresupuestoFinanciable, que luego se clona TAL CUAL
                   //(sin filtrar TOTALes) para lstPresupuestoFinanciableAntesMod1 -> la tabla 1 acababa mostrando
                   //dos filas TOTAL (esta, vacia/incorrecta, y la de verdad un poco mas abajo).
//                   ExpedientePresupDTO filaTotalTemp = new ExpedientePresupDTO();
//                   filaTotalTemp.setTipoFinanciacionDTO(new CatalogosValoresDTO());
//                   filaTotalTemp.getTipoFinanciacionDTO().setDsValor("TOTAL");
//                   filaTotalTemp.setLstExpedienteModifPresupMontoDTO(montosColumnasTemp);
//
//                   ExpedienteModifPresupMontoDTO montoColumnaTotalTemp = new ExpedienteModifPresupMontoDTO();
//                   montoColumnaTotalTemp.setEsMontoTotal(Boolean.TRUE);
//                   montoColumnaTotalTemp.setNmImporteConcedido(sumaTotalTempConcedido);
//                   montoColumnaTotalTemp.setNmImporte(sumaTotalTempAceptado);
//                   filaTotalTemp.getLstExpedienteModifPresupMontoDTO().add(montoColumnaTotalTemp);
//                   lstExpModifPresupMontoDTO.add(montoColumnaTotalTemp);
//
//                   this.lstPresupuestoFinanciable.add(filaTotalTemp);

               }

               //metemos la fila de totales en la primera tabla
               List<ExpedientePresupMontoDTO> montosColumnas = new ArrayList<>();
               
               for (Integer anualidad : this.listaAnualidades) {  //para cada anualidad
                   double sumaColumna = 0.0;
                   for (ExpedientePresupDTO presupuesto : this.lstPresupuestoFinanciable) {  //para cada partida
                       if (presupuesto.getTipoFinanciacionDTO() != null && "TOTAL".equals(presupuesto.getTipoFinanciacionDTO().getDsValor())) {
                           continue;
                       }
                       
                       if (Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                           // Con conceptos: la columna 0 es el concepto, así que la anualidad real coincide con la anualidad del monto
                           sumaColumna += presupuesto.getLstExpedientePresupMontoDTO().stream()
                                   .filter(monto -> monto != null && Objects.nonNull(monto.getNmAnualidad()))
                                   .filter(monto -> monto.getNmAnualidad() != 0)
                                   .filter(monto -> anualidad.equals(monto.getNmAnualidad()))
                                   .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                       }else {
                           // Sin conceptos: la anualidad real empieza en 1 y la del monto en 0 por tanto hay que sumarle 1
                           sumaColumna += presupuesto.getLstExpedientePresupMontoDTO().stream()
                                   .filter(monto -> monto != null && Objects.nonNull(monto.getNmAnualidad()))
                                   .filter(monto -> anualidad.equals(monto.getNmAnualidad() + 1))
                                   .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                       }
                   }
                   ExpedientePresupMontoDTO columnaTotal = new ExpedientePresupMontoDTO();
                   columnaTotal.setEsMontoTotal(Boolean.TRUE);
                   columnaTotal.setNmImporte(sumaColumna);
                   montosColumnas.add(columnaTotal);
               }

               ExpedientePresupDTO filaTotal = new ExpedientePresupDTO();
               filaTotal.setTipoFinanciacionDTO(new CatalogosValoresDTO());
               filaTotal.getTipoFinanciacionDTO().setDsValor("TOTAL");
               filaTotal.setLstExpedientePresupMontoDTO(montosColumnas);

               ExpedientePresupMontoDTO montoColumnaTotal = new ExpedientePresupMontoDTO();
               montoColumnaTotal.setEsMontoTotal(Boolean.TRUE);
               montoColumnaTotal.setNmImporte(sumaTotal);
               filaTotal.getLstExpedientePresupMontoDTO().add(montoColumnaTotal);

               this.lstPresupuestoFinanciable.add(filaTotal);
              
               // Lógica de clonación de listas para el renderizado del JSF
               if (nmPresup == 1) {
                   if (lstExpModifPresupMontoDTO.isEmpty()) {  //por aqui solo entra antes de cargar Datos presup
                       this.lstPresupuestoFinanciableAntesMod1 = this.copiarListaPresupuestos(lstPresupuestoFinanciable);
                       this.lstPresupuestoAyudaConcedidaAntesMod = this.copiarListaPresupuestos(lstPresupuestoFinanciableAntesMod1);
                       this.aplicarPorcentajeIntensidad(this.lstPresupuestoAyudaConcedidaAntesMod, true);
                   } else {
                       this.lstPresupuestoFinanciableAntesMod1 = this.copiarListaPresupuestos(lstPresupuestoFinanciable);
                       this.lstPresupuestoAyudaConcedidaAntesMod = this.copiarListaPresupuestos(lstPresupuestoFinanciableAntesMod1);
                       this.aplicarPorcentajeIntensidad(this.lstPresupuestoAyudaConcedidaAntesMod, false);
                       
                       this.lstPresupuestoFinanciableAcept1Modif = this.copiarListaPresupuestosMod(lstExpModifPresupMontoDTO);  //hacer este metodo para expModPresupMonto
                       // VMM 14/09/26 MD-2725: era lstPresupuestoFinanciableAcept1 (siempre vacio en esta
                       // pantalla, ver el mismo fix en modificarTotales/edicionCeldaPresupuesto) - el resumen
                       // de observacionesTablaPresupuestaria quedaba siempre en blanco.
                       this.crearDatosObservaciones(lstPresupuestoFinanciableAcept1Modif);
                       
                       if (this.lstPresupuestoFinanciableAcept1Modif != null) {
                           ExpedientePresupDTO filaTotalModif = new ExpedientePresupDTO();
                           filaTotalModif.setTipoFinanciacionDTO(new CatalogosValoresDTO());
                           filaTotalModif.getTipoFinanciacionDTO().setDsValor("TOTAL");
                           
                           // Le asignamos los mismos montos de columnas calculados arriba para la tabla 3
                           filaTotalModif.setLstExpedienteModifPresupMontoDTO(montosColumnasTemp); 
                           
                           // La agregamos al listado modificable para que el JSF la reconozca
                           this.lstPresupuestoFinanciableAcept1Modif.add(filaTotalModif);
                       }        

                       this.lstPresupuestoAyudaConcedidaAcept = this.copiarListaPresupuestos(lstPresupuestoFinanciableAcept1Modif);
                       this.aplicarPorcentajeIntensidad(this.lstPresupuestoAyudaConcedidaAcept, false);
                   }      
               } else if (nmPresup == 2){
                   this.lstPresupuestoFinanciableAntesMod2 = this.lstPresupuestoFinanciable;
                   this.lstPresupuestoAyudaConcedidaAntesMod2 = this.lstPresupuestoAyudaConcedidaAntesMod;
               }
           }
               
          
       } catch (ServiceException e) {
           log.error("Error en obtenerPresupuestoFinanciable", e);
           AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
       }
   }




   /**
    * Copia una lista de ExpedientePresupDTO para evitar referencias compartidas.
    */
   private void crearDatosObservaciones(List<ExpedientePresupDTO> listaOriginal) {
       StringBuilder sb = new StringBuilder();
       
       if (listaOriginal != null) {
           for (ExpedientePresupDTO fila : listaOriginal) {
               
               if (fila.getLstExpedienteModifPresupMontoDTO() != null) {
                   
                   for (ExpedienteModifPresupMontoDTO monto : fila.getLstExpedienteModifPresupMontoDTO()) {
                       
                       // OJO: Solo procesamos si hay contenido en observaciones
                       if (monto.getCdObservaciones() != null && !monto.getCdObservaciones().trim().isEmpty()) {
                           
                           // Si ya había contenido previo, añadimos un separador (ej. punto y coma o salto)
                           if (sb.length() > 0) {
                               sb.append("\n"); 
                           }
                           
                           // Construimos la cadena: Tipo [+ Concepto]: Importe - Observación
                           // VMM 06/10/26 MD-3078: null-safe por si la fila no tiene partida
                           // VMM 07/10/26 MD-3119: se incluye el importe aceptado de la fila (total de la
                           // fila) y, sin concepto, ya no queda el " - : " vacio entre partida y observacion.
                           sb.append(fila.getTipoFinanciacionDTO() != null
                                   && fila.getTipoFinanciacionDTO().getDsValor() != null
                                           ? fila.getTipoFinanciacionDTO().getDsValor() : "");
                           if (StringUtils.isNotBlank(fila.getTlConcepto())) {
                               sb.append(" - ").append(fila.getTlConcepto().trim());
                           }
                           sb.append(": ")
                             .append(this.formatearImporteObservacion(fila))
                             .append(" - ")
                             .append(monto.getCdObservaciones().trim());
                       }
                   }
               }
           }
       }
       
       observacionesTablaPresupuestaria = sb.toString();           
   }
   
   /**
    * VMM 07/10/26 MD-3119: importe aceptado de la fila (el total de la fila, que es el ultimo monto de la
    * lista, igual que lo pinta la columna TOTAL de la tabla) formateado como en pantalla,
    * p.ej. "22.500,00 €". Si la fila no tiene importe informado devuelve "0,00 €".
    */
   private String formatearImporteObservacion(ExpedientePresupDTO fila) {
       Double importe = null;
       List<ExpedienteModifPresupMontoDTO> montos = fila.getLstExpedienteModifPresupMontoDTO();
       if (montos != null && !montos.isEmpty() && montos.get(montos.size() - 1) != null) {
           importe = montos.get(montos.size() - 1).getNmImporte();
       }
       java.text.NumberFormat formato = java.text.NumberFormat.getNumberInstance(new java.util.Locale("es", "ES"));
       formato.setMinimumFractionDigits(2);
       formato.setMaximumFractionDigits(2);
       return formato.format(importe != null ? importe : 0.0) + " " + (char) 0x20AC;
   }

   public String generarResumenComentarios() {
       StringBuilder sb = new StringBuilder();

       return sb.toString();
   }
   
       
   /**
    * Copia una lista de ExpedientePresupDTO para evitar referencias compartidas.
    */
   private List<ExpedientePresupDTO> copiarListaPresupuestos(List<ExpedientePresupDTO> listaOriginal) {
       List<ExpedientePresupDTO> nuevaLista = new ArrayList<>();
       for (ExpedientePresupDTO presup : listaOriginal) {
           nuevaLista.add(new ExpedientePresupDTO(presup));
       }
       return nuevaLista;
   }
      
   
   private List<ExpedientePresupDTO> copiarListaPresupuestosMod(List<ExpedienteModifPresupMontoDTO> lstExpModifPresupMontoDTO) {
       List<ExpedientePresupDTO> listaEstructurada = new ArrayList<>();

       if (this.lstPresupuestoFinanciable == null || lstExpModifPresupMontoDTO == null) {
           return listaEstructurada;
       }

       // =========================================================================
       // CAMBIO 1: Agrupamos por ID_EXPEDIENTE_PRESUP_MONTO en lugar de idExpedientePresup
       // =========================================================================
       Map<Integer, ExpedienteModifPresupMontoDTO> montosPorIdMontoOriginal = lstExpModifPresupMontoDTO.stream()
               .filter(monto -> monto != null && monto.getIdExpedientePresupMonto() != null)
               .collect(Collectors.toMap(
                       ExpedienteModifPresupMontoDTO::getIdExpedientePresupMonto, 
                       monto -> monto,
                       (m1, m2) -> m1 // Por si acaso hay duplicados, se queda con el primero
               ));
       // =========================================================================

       for (ExpedientePresupDTO presupBase : this.lstPresupuestoFinanciable) {
           if (presupBase.getTipoFinanciacionDTO() != null && "TOTAL".equals(presupBase.getTipoFinanciacionDTO().getDsValor())) {
               continue; 
           }

           ExpedientePresupDTO nuevoPresupMod = new ExpedientePresupDTO();
           nuevoPresupMod.setIdExpedientePresup(presupBase.getIdExpedientePresup());
           nuevoPresupMod.setTlConcepto(presupBase.getTlConcepto());
           nuevoPresupMod.setTipoFinanciacionDTO(presupBase.getTipoFinanciacionDTO());
           nuevoPresupMod.setIdExpediente(presupBase.getIdExpediente());
           nuevoPresupMod.setNmPresup(presupBase.getNmPresup());
           nuevoPresupMod.setTematicaDTO(presupBase.getTematicaDTO());
           
           // El xhtml con conceptos indexa directamente por el valor de la anualidad (1-based, con el
           // índice 0 reservado para el concepto) y el TOTAL en la última posición; el xhtml sin conceptos
           // (listaNcolPresupuesto) indexa en base 0 sin hueco reservado. Como el dato real de nmAnualidad
           // en BD también viene en base 0 para sin conceptos, hay que desplazar -1 el índice de seguridad
           // y la posición del TOTAL para que cuadren con lo que lee cada tabla.
           boolean conConceptos = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup);

           List<ExpedienteModifPresupMontoDTO> montosAnualidades = new ArrayList<>();
           int posicionTotalFila = conConceptos ? this.listaAnualidades.size() + 1 : this.listaAnualidades.size();
           for (int i = 0; i <= posicionTotalFila; i++) {
               montosAnualidades.add(null);
           }

           // =========================================================================
           // CAMBIO 2: Buscamos los montos modificados cruzando a través de las anualidades originales del concepto
           // =========================================================================
           boolean tieneModificacion = false;
           if (presupBase.getLstExpedientePresupMontoDTO() != null) {
               for (ExpedientePresupMontoDTO montoOriginal : presupBase.getLstExpedientePresupMontoDTO()) {
                   if (montoOriginal != null && montoOriginal.getIdExpedientePresupMonto() != null) {

                       // Buscamos en el mapa usando el ID del monto original
                       ExpedienteModifPresupMontoDTO montoMod = montosPorIdMontoOriginal.get(montoOriginal.getIdExpedientePresupMonto());

                       if (montoMod != null) {
                           tieneModificacion = true;
                           ExpedienteModifPresupMontoDTO clonMonto = new ExpedienteModifPresupMontoDTO(montoMod);

                           if (Boolean.TRUE.equals(clonMonto.getEsMontoTotal())) {
                               montosAnualidades.set(posicionTotalFila, clonMonto);
                           } else if (conConceptos && clonMonto.getNmAnualidad() != null && clonMonto.getNmAnualidad() <= this.listaAnualidades.size()) {
                               // Con conceptos: la anualidad real coincide con el índice (1-based, índice 0 = concepto)
                               montosAnualidades.set(clonMonto.getNmAnualidad(), clonMonto);
                           } else if (!conConceptos && clonMonto.getNmAnualidad() != null && clonMonto.getNmAnualidad() < this.listaAnualidades.size()) {
                               // Sin conceptos: la anualidad real ya viene en base 0 desde BD, igual que el índice del array
                               montosAnualidades.set(clonMonto.getNmAnualidad(), clonMonto);
                           }
                       }
                   }
               }
           }
           // =========================================================================

           // La tabla de Aceptadas debe reflejar la estructura de la solicitud de modificación
           // (tabla 2), no la del presupuesto financiable completo (tabla 1): si esta partida no
           // tiene ningún monto modificado asociado, es que el ciudadano no la incluyó en su
           // solicitud, así que no debe aparecer una fila para ella aquí.
           if (!tieneModificacion) {
               continue;
           }

           // CONTROL DE SEGURIDAD
           for (Integer anualidad : this.listaAnualidades) {
               // this.listaAnualidades es 1-based (1, 2, 3...); con conceptos el array usa ese mismo valor
               // como índice, pero sin conceptos el array es base 0, así que hay que restar 1
               int indiceAnualidad = conConceptos ? anualidad : anualidad - 1;
               if (montosAnualidades.get(indiceAnualidad) == null) {
                   ExpedienteModifPresupMontoDTO vacio = new ExpedienteModifPresupMontoDTO();
                   vacio.setNmAnualidad(indiceAnualidad);
                   vacio.setNmImporteConcedido(0.0);
                   vacio.setNmImporte(0.0);
                   vacio.setIdExpedientePresup(presupBase.getIdExpedientePresup());
                   vacio.setEsMontoTotal(Boolean.FALSE);
                   vacio.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                   montosAnualidades.set(indiceAnualidad, vacio);
               }
           }

           // Con conceptos el índice 0 está reservado para el concepto (no es una anualidad, por lo
           // que el CONTROL DE SEGURIDAD de arriba no lo cubre) y puede quedar sin rellenar si ningún
           // monto modificado cae ahí; lo dejamos con un valor vacío para no propagar un null.
           if (conConceptos && montosAnualidades.get(0) == null) {
               ExpedienteModifPresupMontoDTO concepto = new ExpedienteModifPresupMontoDTO();
               concepto.setEsMontoTotal(Boolean.FALSE);
               concepto.setIdExpedientePresup(presupBase.getIdExpedientePresup());
               concepto.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
               montosAnualidades.set(0, concepto);
           }

           // Cálculo del Total Horizontal de la fila
           if (montosAnualidades.get(posicionTotalFila) == null) {
               ExpedienteModifPresupMontoDTO totalFila = new ExpedienteModifPresupMontoDTO();
               totalFila.setEsMontoTotal(Boolean.TRUE);
               totalFila.setIdExpedientePresup(presupBase.getIdExpedientePresup());
               totalFila.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
               
               double sumaFilaConcedido = montosAnualidades.stream()
                       .filter(m -> m != null && m.getNmAnualidad() != null)
                       .mapToDouble(m -> m.getNmImporteConcedido() != null ? m.getNmImporteConcedido() : 0.0).sum();
               // El xhtml de la tabla ACEPTADAS (con y sin conceptos) pinta la celda TOTAL con nmImporte,
               // no con nmImporteConcedido, así que hay que calcularlo y asignarlo también
               double sumaFilaImporte = montosAnualidades.stream()
                       .filter(m -> m != null && m.getNmAnualidad() != null)
                       .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0).sum();
               totalFila.setNmImporteConcedido(sumaFilaConcedido);
               totalFila.setNmImporte(sumaFilaImporte);
               montosAnualidades.set(posicionTotalFila, totalFila);
           }

           nuevoPresupMod.setLstExpedienteModifPresupMontoDTO(montosAnualidades);
           listaEstructurada.add(nuevoPresupMod);
       }

       // =========================================================================
       // VMM 11/09/26 MD-2725: conceptos nuevos dados de alta directamente en
       // saii_solicitud, sin ExpedientePresup real todavia (eso lo crea el batch al
       // resolver la modificacion). No hay columna que agrupe explicitamente las filas de
       // un mismo concepto nuevo, asi que se reconstruyen aprovechando dos cosas ya
       // garantizadas por guardarModifPresupConcepto: (1) inserta siempre en orden - todas
       // las columnas de un concepto seguidas, antes de pasar al siguiente - y (2) tlConcepto
       // solo se rellena en la fila cabecera (nmColumna=0). Se ordena por el PK de insercion
       // (idExpedienteModifPresupMonto) y se abre un grupo nuevo cada vez que aparece una
       // fila con tlConcepto relleno; cada grupo = un concepto nuevo = una fila en la tabla,
       // usando el tipoFinanciacion guardado en el propio monto para agruparla bajo la
       // cabecera de partida correcta (la tabla ordena/agrupa por tipoFinanciacionDTO).
       // =========================================================================
       boolean conConceptos = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup);
       int posicionTotalFilaNueva = conConceptos ? this.listaAnualidades.size() + 1 : this.listaAnualidades.size();

       // ordenPartida no se persiste (se calcula en runtime en obtenerPresupuestoFinanciable /
       // obtenerFilColPresupSolicitadoConcepto) y por tanto no llega en el tipoFinanciacionDTO
       // reconstruido desde BD; lo copiamos de una partida real hermana con el mismo
       // tipoFinanciacion para que la fila nueva se agrupe bajo la cabecera correcta (sortBy).
       Map<Integer, Integer> ordenPorTipoFinanciacion = this.lstPresupuestoFinanciable.stream()
               .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null
                       && p.getTipoFinanciacionDTO().getOrdenPartida() != null)
               .collect(Collectors.toMap(
                       p -> p.getTipoFinanciacionDTO().getIdValor(),
                       p -> p.getTipoFinanciacionDTO().getOrdenPartida(),
                       (o1, o2) -> o1));

       // VMM 06/10/26 MD-3078: se excluyen los montos TOTAL. obtenerPresupuestoFinanciable anade a
       // lstExpModifPresupMontoDTO un monto auxiliar de totales (montoTotalTemp, esMontoTotal=true, sin
       // idExpedientePresupMonto, sin anualidad ni partida) para calcular la fila TOTAL; como no
       // tiene idExpedientePresupMonto este bloque lo tomaba por un "concepto nuevo" y pintaba una
       // fila fantasma (sin partida, importes a 0,00 y editable) justo antes del TOTAL de la tabla
       // de aceptadas, que ademas hacia fallar modificarTotales al editar un importe.
       List<ExpedienteModifPresupMontoDTO> nuevosConceptosOrdenados = lstExpModifPresupMontoDTO.stream()
               .filter(m -> m != null && m.getIdExpedientePresupMonto() == null
                       && !Boolean.TRUE.equals(m.getEsMontoTotal()))
               .sorted(Comparator.comparing(ExpedienteModifPresupMontoDTO::getIdExpedienteModifPresupMonto,
                       Comparator.nullsLast(Comparator.naturalOrder())))
               .collect(Collectors.toList());

       List<List<ExpedienteModifPresupMontoDTO>> gruposConceptosNuevos = new ArrayList<>();
       List<ExpedienteModifPresupMontoDTO> grupoActual = null;
       for (ExpedienteModifPresupMontoDTO monto : nuevosConceptosOrdenados) {
           if (monto.getTlConcepto() != null || grupoActual == null) {
               grupoActual = new ArrayList<>();
               gruposConceptosNuevos.add(grupoActual);
           }
           grupoActual.add(monto);
       }

       for (List<ExpedienteModifPresupMontoDTO> montosConcepto : gruposConceptosNuevos) {

           ExpedientePresupDTO nuevoConceptoMod = new ExpedientePresupDTO();
           CatalogosValoresDTO tipoFinanciacionNuevo = montosConcepto.stream()
                   .map(ExpedienteModifPresupMontoDTO::getTipoFinanciacionDTO)
                   .filter(Objects::nonNull)
                   .findFirst()
                   .orElse(null);
           if (tipoFinanciacionNuevo != null && tipoFinanciacionNuevo.getIdValor() != null) {
               Integer orden = ordenPorTipoFinanciacion.get(tipoFinanciacionNuevo.getIdValor());
               if (orden != null) {
                   tipoFinanciacionNuevo.setOrdenPartida(orden);
               }
           }
           nuevoConceptoMod.setTipoFinanciacionDTO(tipoFinanciacionNuevo);
           nuevoConceptoMod.setTlConcepto(montosConcepto.stream()
                   .map(ExpedienteModifPresupMontoDTO::getTlConcepto)
                   .filter(Objects::nonNull)
                   .findFirst()
                   .orElse(null));

           List<ExpedienteModifPresupMontoDTO> montosAnualidadesNuevo = new ArrayList<>();
           for (int i = 0; i <= posicionTotalFilaNueva; i++) {
               montosAnualidadesNuevo.add(null);
           }

           for (ExpedienteModifPresupMontoDTO monto : montosConcepto) {
               if (monto.getNmAnualidad() == null) {
                   continue;
               }
               ExpedienteModifPresupMontoDTO clon = new ExpedienteModifPresupMontoDTO(monto);
               if (conConceptos && clon.getNmAnualidad() <= this.listaAnualidades.size()) {
                   montosAnualidadesNuevo.set(clon.getNmAnualidad(), clon);
               } else if (!conConceptos && clon.getNmAnualidad() < this.listaAnualidades.size()) {
                   montosAnualidadesNuevo.set(clon.getNmAnualidad(), clon);
               }
           }

           // Rellenamos huecos igual que para las partidas existentes (CONTROL DE SEGURIDAD)
           for (Integer anualidad : this.listaAnualidades) {
               int indiceAnualidad = conConceptos ? anualidad : anualidad - 1;
               if (montosAnualidadesNuevo.get(indiceAnualidad) == null) {
                   ExpedienteModifPresupMontoDTO vacio = new ExpedienteModifPresupMontoDTO();
                   vacio.setNmAnualidad(indiceAnualidad);
                   vacio.setNmImporteConcedido(0.0);
                   vacio.setNmImporte(0.0);
                   vacio.setEsMontoTotal(Boolean.FALSE);
                   vacio.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                   montosAnualidadesNuevo.set(indiceAnualidad, vacio);
               }
           }

           if (conConceptos && montosAnualidadesNuevo.get(0) == null) {
               ExpedienteModifPresupMontoDTO concepto = new ExpedienteModifPresupMontoDTO();
               concepto.setEsMontoTotal(Boolean.FALSE);
               concepto.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
               montosAnualidadesNuevo.set(0, concepto);
           }

           if (montosAnualidadesNuevo.get(posicionTotalFilaNueva) == null) {
               ExpedienteModifPresupMontoDTO totalFila = new ExpedienteModifPresupMontoDTO();
               totalFila.setEsMontoTotal(Boolean.TRUE);
               totalFila.setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());

               double sumaFilaConcedido = montosAnualidadesNuevo.stream()
                       .filter(m -> m != null && m.getNmAnualidad() != null)
                       .mapToDouble(m -> m.getNmImporteConcedido() != null ? m.getNmImporteConcedido() : 0.0).sum();
               double sumaFilaImporte = montosAnualidadesNuevo.stream()
                       .filter(m -> m != null && m.getNmAnualidad() != null)
                       .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0).sum();
               totalFila.setNmImporteConcedido(sumaFilaConcedido);
               totalFila.setNmImporte(sumaFilaImporte);
               montosAnualidadesNuevo.set(posicionTotalFilaNueva, totalFila);
           }

           nuevoConceptoMod.setLstExpedienteModifPresupMontoDTO(montosAnualidadesNuevo);
           listaEstructurada.add(nuevoConceptoMod);
       }

       return listaEstructurada;
   }
   
   
   
   
   /**
    * Copia una lista de ExpedientePresupDTO para evitar referencias compartidas.
    */
//   private List<ExpedientePresupDTO> copiarListaPresupuestosMod(List<ExpedientePresupDTO> listaOriginal) {
//       List<ExpedientePresupDTO> nuevaLista = new ArrayList<>();
//       for (ExpedientePresupDTO presup : listaOriginal) {
//           nuevaLista.add(new ExpedientePresupDTO(presup));
//       }
//       return nuevaLista;
//   }
   
   /**
    * Aplica el porcentaje de intensidad sobre los valores de ayuda concedida.
    * 
    * @param listaPresupuestos Lista de presupuestos sobre la que aplicar la transformación.
    * @param esPrimeraCarga Indica si se usa `nmImporte` (true) o `nmImporteConcedido` (false).
    */
   private void aplicarPorcentajeIntensidad(List<ExpedientePresupDTO> listaPresupuestos, boolean esPrimeraCarga) {
       // VMM 03/09/26: si no se ha podido determinar la intensidad del gestor, no aplicamos
       // ningún porcentaje (dejamos los importes tal cual) en vez de lanzar una NullPointerException.
       if (this.porcentajeIntensidadAyuda == null) {
           return;
       }
       for (ExpedientePresupDTO presupAyudaConcedida : listaPresupuestos) {
           if (esPrimeraCarga) {
               List<ExpedientePresupMontoDTO> lstPresupMontoAC = presupAyudaConcedida.getLstExpedientePresupMontoDTO();
               
               for (ExpedientePresupMontoDTO presupMonto : lstPresupMontoAC) {
                   if (presupMonto != null && presupMonto.getNmImporte() != null) {
                       presupMonto.setNmImporte(presupMonto.getNmImporte() * (porcentajeIntensidadAyuda / 100));
                   }
               }      

           }else {
               List<ExpedienteModifPresupMontoDTO> lstPresupMontoAC = presupAyudaConcedida.getLstExpedienteModifPresupMontoDTO();

               for (ExpedienteModifPresupMontoDTO presupMonto : lstPresupMontoAC) {
                   if (presupMonto != null) {
                       if (presupMonto.getNmImporte() != null) {
                           presupMonto.setNmImporte(presupMonto.getNmImporte() * (porcentajeIntensidadAyuda / 100));
                       } 
                       if (presupMonto.getNmImporteConcedido() != null) {
                           presupMonto.setNmImporteConcedido(presupMonto.getNmImporteConcedido() * (porcentajeIntensidadAyuda / 100));
                       }
                   }
               }
           }
       }
   }
   
   
   /**
   *
   * @param presupuestoFinanciable a aceptar
   * @param nCol
   */
  public void edicionCeldaPresupuesto(ExpedientePresupDTO presupuestoFinanciable, int nCol,
          int nmPresup) {

      try {

          if (Objects.nonNull(presupuestoFinanciable)) {

              ExpedienteModifPresupMontoDTO presupMonto = presupuestoFinanciable
                      .getLstExpedienteModifPresupMontoDTO().stream()
                      .filter(monto -> monto.getNmAnualidad().equals(nCol)).findFirst()
                      .orElse(new ExpedienteModifPresupMontoDTO());

              if (Objects.isNull(presupMonto.getNmImporte())) {
                  presupMonto.setNmImporte(0.0);
              }           
                          
              //comprobamos aqui si ya se ha iniciado la anualidad, es cuyo caso no podemos editar sus valores
              //calculamos la fecha de esa anualidad y ese presupuesto
              Boolean tieneErroresAnual = Boolean.FALSE;
              // VMM 14/09/26 MD-2725: el +1 solo es correcto para "sin conceptos" (nmAnualidad 0-based
              // en BD). Con conceptos nmAnualidad ya es el numero real de anualidad (ver el mismo ajuste
              // unas lineas mas abajo al construir msgImporte) - sumarle 1 aqui consultaba la anualidad
              // siguiente (o ninguna) y dejaba sin efecto el limite de los 60 dias para el fin de ejecucion.
              Integer nmAnualidadEdit = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)
                      ? presupMonto.getNmAnualidad()
                      : presupMonto.getNmAnualidad() + 1;
              tieneErroresAnual = this.comprobarFechaAnualVencida(nmAnualidadEdit, tieneErroresAnual, null);
              
              Double importePresentado = presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO().get(nCol)
                      .getNmImporteConcedido();
              
              double importeMaximo = Objects.nonNull(importePresentado)
                      ? importePresentado
                      //: 999999.99;
                      : 9999999999.99;

              DecimalFormat nf = new DecimalFormat("0.00");
              String montoTexto = nf.format(importeMaximo).replace(".", ",");

              log.info("Monto capturado: " + presupMonto.getNmImporte());
              log.info("Importe maximo: " + montoTexto);

              double totalFinalFila = presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO()
                      .stream()
                      .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                              && Objects.nonNull(monto.getNmImporte()))
                      .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporte).sum();

              String tabla = "";
              if (nmPresup == 1) {
                  tabla = " de la tabla de modificaciones presupuestarias aceptadas " + nmPresup;
              }

              //VMM 06/05/26 MD-2691: controlamos las anualidades del mensaje de error por si hay conceptos o no
              Object[] msgImporte = null;
              if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                  msgImporte = new Object[] {
                      presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                      presupMonto.getNmAnualidad(), montoTexto + " €", tabla };
              }else {
                  msgImporte = new Object[] {
                      presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                      presupMonto.getNmAnualidad() + 1, montoTexto + " €", tabla };
              }

              //VMM 17/04/26 MD-2638: comprobamos los importes maximos de cada celda solo cuando es convocatoria de max por PP
              // VMM 14/09/26 MD-2725: los conceptos nuevos (dados de alta en saii_solicitud, sin
              // ExpedientePresup todavia) tienen nmImporteConcedido=0.0 por diseño, al no existir aun
              // un importe concedido real de referencia - no se les puede aplicar este maximo ficticio.
              if (this.convocatoriaDTO != null && !Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItImporteMaxAnual()) &&
                      !Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItImporteMaxExpdte()) &&
                      Objects.nonNull(presupuestoFinanciable.getIdExpedientePresup())) {
                  this.tieneErrores = presupMonto.getNmImporte() > importeMaximo;
              }
                            
              if (Boolean.TRUE.equals(this.tieneErrores) || Boolean.TRUE.equals(tieneErroresAnual)) {
                  int indiceTotalfila = presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO()
                          .size() - 1;
                  double montoTotalInicial = presupuestoFinanciable
                          .getLstExpedienteModifPresupMontoDTO().get(indiceTotalfila).getNmImporte();

                  double montoInicial = presupMonto.getNmImporte()
                          - (totalFinalFila - montoTotalInicial);
                  presupMonto.setNmImporte(montoInicial);

                  if (Boolean.TRUE.equals(tieneErroresAnual)) {
                       // VMM 14/09/26 MD-2725: mensaje especifico de presupuesto (bloqueo por
                       // proximidad al fin de ejecucion, no por anualidad ya iniciada)
                       AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FECHA_ANUALIDAD_PROXIMA_FIN);
                  }else{
                      AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP,
                              msgImporte);
                  }
              }
              
              if (Boolean.FALSE.equals(this.tieneErrores) && Boolean.FALSE.equals(tieneErroresAnual)) {
                  this.modificarTotales(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);

                  // VMM 11/09/26 MD-2725: al igual que en FinanciacionBean, marcamos la fila como
                  // pendiente de observacion si se ha modificado el importe y no tiene observacion
                  // informada, para bloquear el guardado hasta que se rellene (ver guardarModifPresupuesto).
                  String observacionActual = this.obtenerObservacionFila(presupuestoFinanciable);
                  if (StringUtils.isBlank(observacionActual)) {
                      presupuestoFinanciable.setPendienteObservacion(Boolean.TRUE);
                  }

                  // VMM 14/09/26 MD-2725: era lstPresupuestoFinanciableAcept1 (siempre vacio en esta
                  // pantalla, ver el mismo fix en modificarTotales) - lstPresupuestoAyudaConcedidaAcept
                  // quedaba vacio, lo que hacia fallar validarImporteTotalAceptado() en guardarModifPresupuesto
                  // con "no se encuentran los presupuestos aceptados de la modificacion" (aunque igualmente
                  // guardaba, porque esa rama no hacia return false).
                  if(nmPresup == 1 && this.lstPresupuestoFinanciableAcept1Modif != null) {

                      this.lstPresupuestoAyudaConcedidaAcept = this.copiarListaPresupuestos(this.lstPresupuestoFinanciableAcept1Modif);
                      this.aplicarPorcentajeIntensidad(this.lstPresupuestoAyudaConcedidaAcept, false);

                  }else if (nmPresup == 2 && this.lstPresupuestoFinanciableAcept2 != null)  {
                      this.lstPresupuestoAyudaConcedidaAcept2 = this.copiarListaPresupuestos(this.lstPresupuestoFinanciableAcept2);                    
                      this.aplicarPorcentajeIntensidad(this.lstPresupuestoAyudaConcedidaAcept2, false);
                  }
              }
      
               PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciableAcept);
              //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciableAcept2);
          }

      } catch (Exception e) {
          log.error(e.getMessage(), e);
          AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
      }
  }
  
  
  /**
   * Metodo para comprobar si se ha iniciado la anualidad para realizar las modificaciones o no
   * 
   */
    private Boolean comprobarFechaAnualVencida(Integer nmAnualidadEdit,
            Boolean tieneErroresAnual, Date fcIniEjecucionNew) throws ServiceException {
        Date fechaActual = new Date();
        
        //buscamos el registro de exp anualidad correspondiente a la anualidad editada para obtener la fecha de inicio
        List<ExpedienteAnualidadDTO> lstExpAnualOldDTO = this.expedienteAnualidadService.buscarPorExpediente(this.expedienteBean.getIdExpediente());
        lstExpAnualOldDTO = lstExpAnualOldDTO.stream()
                  .filter(anual -> anual.getNmAnualidad().equals(nmAnualidadEdit)).collect(Collectors.toList());
        
        
        if (lstExpAnualOldDTO != null && !lstExpAnualOldDTO.isEmpty()) {
            Date fcIniAnualidadOld = lstExpAnualOldDTO.get(0).getFcIniEjecucion();
              
            // si es una modificacion de plazos
            if (fcIniEjecucionNew != null) {
               if (fcIniAnualidadOld.getTime() != fcIniEjecucionNew.getTime()) {
                  
                  //si la que ya habia es menor o igual a la de hoy (periodo ya iniciado), error
                  if (fcIniAnualidadOld.before(fechaActual) || fcIniAnualidadOld.equals(fechaActual)) {
                      tieneErroresAnual = Boolean.TRUE;
                  }else {
                      //si la que ya habia es mayor a la de hoy (periodo no iniciado), vemos si la modificada es inferior o no
                      if (fcIniEjecucionNew.before(fechaActual) || fcIniEjecucionNew.equals(fechaActual)) {
                          tieneErroresAnual = Boolean.TRUE;
                      }
                  } 
               }
            }else {
                //es una modificacion del presupuesto de una anualidad
                // VMM 14/09/26 MD-2725: antes se bloqueaba en cuanto la anualidad ya habia iniciado
                // (fc_ini_ejecucion <= hoy), sin ningun margen. La regla correcta es permitir modificar
                // el presupuesto hasta 60 dias antes de la fecha FIN de ejecucion de esa anualidad.
                if (this.superaLimiteEdicionAnualidad(lstExpAnualOldDTO.get(0).getFcFinEjecucion())) {
                    tieneErroresAnual = Boolean.TRUE;
                }
            }
        }

        return tieneErroresAnual;
    }

    /**
     * VMM 14/09/26 MD-2725: true si hoy ya esta a 60 dias o menos de la fecha fin de ejecucion
     * indicada. Es el limite de edicion compartido tanto para el presupuesto como para el plazo
     * (fecha fin) de una anualidad - ver comprobarFechaAnualVencida y verificarLimiteEdicionFinEjecucion.
     */
    private boolean superaLimiteEdicionAnualidad(Date fcFinEjecucion) {
        if (fcFinEjecucion == null) {
            return false;
        }
        Date fechaLimiteEdicion = Utils.fechaSumarRestarDias(fcFinEjecucion, -60);
        Date fechaActual = new Date();
        return fechaLimiteEdicion.before(fechaActual) || fechaLimiteEdicion.equals(fechaActual);
    }

  /**
   *
   * @param nCol
   * @param presup
   * @param montoFinalFila
   */
  private void modificarTotales(int nCol, int nmPresup, ExpedientePresupDTO presup,
          double montoFinalFila) {
      
      // Se actualiza el total de la fila
      int indexMontoFila = presup.getLstExpedienteModifPresupMontoDTO().size() - 1;
      ExpedienteModifPresupMontoDTO montoTotal = presup.getLstExpedienteModifPresupMontoDTO()
              .get(indexMontoFila);
      montoTotal.setNmImporte(montoFinalFila);

      presup.getLstExpedienteModifPresupMontoDTO().set(indexMontoFila, montoTotal);

      // Se actualiza el total de la columna
      double sumaTotal = 0.0;
      double sumaColumna = 0.0;

      // VMM 14/09/26 MD-2725: era lstPresupuestoFinanciableAcept1, que en esta pantalla de
      // Modificaciones nunca se rellena (solo se inicializa vacio) - la tabla real que se edita
      // aqui es lstPresupuestoFinanciableAcept1Modif. Con la lista vacia, size()-1 daba -1 y
      // .get(-1) reventaba con ArrayIndexOutOfBoundsException en cuanto no habia errores de validacion.
      if (nmPresup == 1) {
          this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciableAcept1Modif;
      } else {
          this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciableAcept2;
      }

      // VMM 06/10/26 MD-3078: se comprueba tambien que tipoFinanciacionDTO no sea null - una fila sin
      // partida (la "fantasma" que se pintaba antes del TOTAL en sin conceptos, ver
      // copiarListaPresupuestosMod) reventaba aqui con NullPointerException y edicionCeldaPresupuesto
      // lo mostraba como "Error" sin actualizar totales.
      List<ExpedientePresupDTO> presupuestos = this.lstPresupuestoFinanciable.stream()
              .filter(presupuestoDTO -> Objects.nonNull(presupuestoDTO.getTipoFinanciacionDTO())
                      && Objects.nonNull(presupuestoDTO.getTipoFinanciacionDTO().getIdValor()))
              .collect(Collectors.toList());

      for (ExpedientePresupDTO presupuesto : presupuestos) {
          List<ExpedienteModifPresupMontoDTO> montosPresupuesto = presupuesto
                  .getLstExpedienteModifPresupMontoDTO().stream()
                  .filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                  .collect(Collectors.toList());

          sumaTotal += montosPresupuesto.stream()
                  .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporte).sum();

          sumaColumna += montosPresupuesto.stream()
                  .filter(monto -> nCol == monto.getNmAnualidad())
                  .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporte).sum();
      }

      int indexMontoColumna = this.lstPresupuestoFinanciable.size() - 1;
      ExpedientePresupDTO presupuestoDTO = this.lstPresupuestoFinanciable.get(indexMontoColumna);

      int indexColumnaTotal = presupuestoDTO.getLstExpedienteModifPresupMontoDTO().size() - 1;

      ExpedienteModifPresupMontoDTO montoColumna = presupuestoDTO.getLstExpedienteModifPresupMontoDTO()
              .get(nCol);
      montoColumna.setNmImporte(sumaColumna);
      presupuestoDTO.getLstExpedienteModifPresupMontoDTO().set(nCol, montoColumna);

      montoColumna = presupuestoDTO.getLstExpedienteModifPresupMontoDTO().get(indexColumnaTotal);
      montoColumna.setNmImporte(sumaTotal);
      presupuestoDTO.getLstExpedienteModifPresupMontoDTO().set(indexColumnaTotal, montoColumna);

      if (nmPresup == 1) {
          this.lstPresupuestoFinanciableAcept1Modif = this.lstPresupuestoFinanciable;
      } else {
          this.lstPresupuestoFinanciableAcept2 = this.lstPresupuestoFinanciable;
      }
  }
   
    
    /**
     * Metodo para ver el detalle de cada modificación P/P/O
     * 
     * @param selectExpedienteModifDTO
     */
    public void verDetalleModif(ExpedienteModifPresPlazoOtraDTO selectExpedienteModifDTO) {
        this.selectExpedienteModifDTO = selectExpedienteModifDTO;
        
        this.inicializarListasModif();
        
        this.esModoConsulta();

        //comprobamos las modificaciones que existen para mostrarlas
        if (selectExpedienteModifDTO.getItModPres().equals(Constantes.AfirmacionNegacion.S)){
            this.mostrarDetalleModifPres = Boolean.TRUE;
            this.obtenerDatosModifPresupExpediente();         
        }
        if(selectExpedienteModifDTO.getItModPlazo().equals(Constantes.AfirmacionNegacion.S)){
            this.mostrarDetalleModifPlazo = Boolean.TRUE;
            this.obtenerDatosModifPlazoExpediente();        
        }
        if (selectExpedienteModifDTO.getItModOtra().equals(Constantes.AfirmacionNegacion.S)) {
            this.mostrarDetalleModifOtra = Boolean.TRUE;
            this.obtenerDatosModifOtraExpediente();
        }    
        
        // VMM 30/09/26 MD-3065: this.fcResolucion/estadoResolucionDTO no se limpiaban al abrir una
        // modificacion sin resolucion aun (los if de abajo solo los sobrescriben si la modificacion
        // seleccionada SI tiene datos), asi que se quedaban con el valor de la modificacion vista
        // anteriormente (o de una modificacion de otro expediente) - persisten entre peticiones via
        // atlasp:guardarEstado, asi que un valor "pegado" de antes podia parecer un dato ya informado
        // en una modificacion que en realidad esta vacia.
        this.fcResolucion = null;
        this.estadoResolucionDTO = new CatalogosValoresDTO();

        if(selectExpedienteModifDTO.getFcResolucion() != null) {
            this.fcResolucion = selectExpedienteModifDTO.getFcResolucion();
        }

        if(selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor() != null) {
            this.estadoResolucionDTO = selectExpedienteModifDTO.getEstadoResolucionDTO();
        }
        
        this.mostrarDetalleModifObservac = Boolean.TRUE;
        this.obtenerDatosModifObservaciones();
        
        this.mostrarDetalleModifDoc = Boolean.TRUE;
        this.obtenerDatosModifDocExpediente(selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());

        this.cargarEnvioPortafirmasResolucion();

        PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
    }

    /**
     * VMM 10/09/26 MD-2725: recupera el envío a Portafirmas/NOTE de la resolución de la
     * modificación seleccionada, para mostrar su estado NOTE bajo "Datos de Resolución".
     */
    /**
     * VMM 06/10/26 MD-3110: getter con carga perezosa. El bean es de request y este DTO solo se
     * cargaba en verDetalleModif (al pulsar la lupa), asi que en cualquier otra peticion posterior
     * (p.ej. tras pasar el batch y recargar la pantalla) llegaba null y los inputs "Fecha envio a Note"
     * y "Estado Note" se pintaban vacios / "Sin Notificacion" aunque el envio ya estuviera en BD.
     */
    public DocumentoPtfrDTO getEnvioPortafirmasResolucionDTO() {
        if (!Boolean.TRUE.equals(this.envioPortafirmasResolucionCargado)) {
            this.cargarEnvioPortafirmasResolucion();
        }
        return this.envioPortafirmasResolucionDTO;
    }

    /**
     * VMM 06/10/26 MD-3110: true si la resolucion de la modificacion seleccionada ya se ha enviado a
     * la cola de portafirmas (existe su registro en SAII_DOCUMENTO_PTFR y no fue rechazado en
     * Portafirmas). Se usa para no permitir "Enviar a la cola portafirmas" otra vez: tras pasar el
     * batch de firma la modificacion vuelve a "Pendiente Envio Cola Notificaciones", el mismo estado
     * que habilita el boton antes del primer envio.
     */
    private boolean resolucionYaEnCola() {
        DocumentoPtfrDTO envio = this.getEnvioPortafirmasResolucionDTO();
        return envio != null
                && !Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO.equals(envio.getCdEstadoPtfr());
    }

    private void cargarEnvioPortafirmasResolucion() {
        this.envioPortafirmasResolucionDTO = null;
        this.envioPortafirmasResolucionCargado = Boolean.TRUE;
        try {
            if (this.selectExpedienteModifDTO != null
                    && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {

                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter("idPresupuestoModif",
                        this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<AtlasSort> orden = new ArrayList<>();
                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

                List<DocumentoPtfrDTO> lista = this.documentoPtfrService.buscar(0, -1, filtros, orden);
                if (lista != null && !lista.isEmpty()) {
                    this.envioPortafirmasResolucionDTO = lista.get(0);
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    
    /**
     * Método que recupera el texto de otras modificaciones de P/P/O de un expediente
     * 
     * @param idExpedienteModifPresPlazoOtra
     */
    private void obtenerDatosModifOtraExpediente() {
        try {
            BloqueDTO bloqueOtrasMod = null; 
            BloqueComponenteDTO bloqueComponenteOtrasMod = null; 
            
            if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdImpreso() != null && 
                    this.expedienteBean.getIdExpediente() != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {

                //consultamos si existen guardados temporales por el gestor de otras modificaciones
                ExpedienteModifOtraDTO expdteModifOtraDTO = this.expedienteModifOtraService.
                        buscarIdExpedienteModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                
                
              
                
                if (Objects.isNull(expdteModifOtraDTO)) {
                    
                    this.tlOtrasModifObservaciones = "";
                    
                    //la primera vez buscamos los valores en saii_dato
                    //buscar el/los bloque/s de texto del formulario para poder sacar el texto asociado a las otras modificaciones
                    List<BloqueDTO> lstBloquesOtrasModif = this.bloqueService.buscarPorFormato(this.selectExpedienteModifDTO.getIdImpreso(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO));
                    
                    //filtrar los bloques del impreso que sean de tematica Otras Modificaciones (1562) que es el que se crea por defecto
                    if (!lstBloquesOtrasModif.isEmpty()) {
                        
                        Integer idOtrMod = utilConstantes.getGlobalCatalogosValores().get(Constantes.TematicaBloque.OTRAS_MODIFICACIONES);
                        
                        lstBloquesOtrasModif = lstBloquesOtrasModif.stream()
                                .filter(x -> x.getIdTematica().getIdValor().equals(idOtrMod) 
                                             && x.getFcBajaFila() == null)
                                .sorted(Comparator.comparing(BloqueDTO::getNmOrden).reversed())  // Orden descendente
                                .collect(Collectors.toList());
    
                        
                        //si hay más de un bloque de otras modificaciones nos quedamos con el ultimo, mas reciente
                        if (!lstBloquesOtrasModif.isEmpty()){
                            bloqueOtrasMod = lstBloquesOtrasModif.get(0);
                            
                            if (!bloqueOtrasMod.getLstBloqueComponenteDTO().isEmpty()) {
                                //una vez tenemos el bloque actual de otras modificaciones, obtenemos su id_bloque_componente
                                bloqueComponenteOtrasMod = bloqueOtrasMod.getLstBloqueComponenteDTO().get(0);
                            }
                        }                                
                    }
                
                    if (bloqueComponenteOtrasMod != null) {
                        
                        //una vez ya tenemos el id_bloque_componente, podemos consultar el dato asociado                       
                        List<DatoDTO> lstDatoDTO = this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(), 
                                this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), bloqueComponenteOtrasMod.getIdBloqueComponente(), false);                        
                        
                        if (lstDatoDTO != null && !lstDatoDTO.isEmpty()) {
                            this.tlOtrasModificaciones = lstDatoDTO.get(0).getDsValor();
                        }      
                    }
                }else {
                    //las siguientes veces, si existen guardados temporales por parte del gestor en otras modificaciones, los recuperamos
                    this.tlOtrasModificaciones = expdteModifOtraDTO.getTlOtrasModif();
                    this.tlOtrasModifObservaciones = expdteModifOtraDTO.getTlOtrasModifObservaciones();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Método que recupera el texto de observaciones de P/P/O de un expediente
     * 
     */
    private void obtenerDatosModifObservaciones() {
        try {
            BloqueDTO bloqueObservMod = null; 
            BloqueComponenteDTO bloqueComponenteObserv = null; 
            
            if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdImpreso() != null && 
                    this.expedienteBean.getIdExpediente() != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
                
                //aqui vamos a tener un problema porque si han eliminado las observaciones posteriores de expteModifPresPlazoOtra 
                //le vamos a volver a mostrar las iniciales de saii_sol (saii_dato)
                if (Objects.isNull(this.selectExpedienteModifDTO.getTlObservaciones()) 
                        || StringUtils.isBlank(this.selectExpedienteModifDTO.getTlObservaciones())) {
                    
                    //la primera vez buscamos los valores en saii_dato
                    //buscar el/los bloque/s de texto del formulario para poder sacar el texto asociado a las observaciones
                    List<BloqueDTO> lstBloquesObservacModif = this.bloqueService.buscarPorFormato(this.selectExpedienteModifDTO.getIdImpreso(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO));
                    
                    //filtrar los bloques del impreso que sean de tematica Observaciones (1563) que es el que se crea por defecto
                    if (!lstBloquesObservacModif.isEmpty()) {
                        
                        Integer idObs = utilConstantes.getGlobalCatalogosValores().get(Constantes.TematicaBloque.OBSERVACIONES);
                        
                        lstBloquesObservacModif = lstBloquesObservacModif.stream()
                                .filter(x -> x.getIdTematica().getIdValor().equals(idObs) 
                                             && x.getFcBajaFila() == null)
                                .sorted(Comparator.comparing(BloqueDTO::getNmOrden).reversed())  // Orden descendente
                                .collect(Collectors.toList());
    
                        
                        //si hay más de un bloque de observaciones nos quedamos con el ultimo, mas reciente
                        if (!lstBloquesObservacModif.isEmpty()){
                            bloqueObservMod = lstBloquesObservacModif.get(0);
                            
                            if (!bloqueObservMod.getLstBloqueComponenteDTO().isEmpty()) {
                                //una vez tenemos el bloque actual de observaciones, obtenemos su id_bloque_componente
                                bloqueComponenteObserv = bloqueObservMod.getLstBloqueComponenteDTO().get(0);
                            }
                        }                                
                    }
                
                    if (bloqueComponenteObserv != null) {
                        
                        //una vez ya tenemos el id_bloque_componente, podemos consultar el dato asociado                       
                        List<DatoDTO> lstDatoDTO = this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(), 
                                this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), bloqueComponenteObserv.getIdBloqueComponente(), false);
                        
                        if (lstDatoDTO != null && !lstDatoDTO.isEmpty()) {
                            this.tlObservacionesModif = lstDatoDTO.get(0).getDsValor();
                        }      
                    }
                }else {
                    //las siguientes veces, si existen guardados posteriores por parte del gestor en observaciones, los recuperamos de la tabla de MODIF_PPP
                    this.tlObservacionesModif = this.selectExpedienteModifDTO.getTlObservaciones();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * Método para obtener las listas de ExpedienteAnualidad (plazos) de la modificación
     *
     */
    private void obtenerDatosModifPlazoExpediente(){
            
        //obtenerPlazosAntesModif y Aceptado cuando este exista
        this.obtenerPlazo(1);
        
        //obtenerPlazoSolicitado 
        this.obtenerPlazo(2);
    }        

    /**
     * Método que recupera las fechas de las anualidades (plazos) de modificaciones de P/P/O de un expediente
     * si tipoModif es 1 recupera los plazos antes de la modificacion y si tipoModif = 1 recupera los plazos 
     * solicitados en la modificacion
     * 
     * @param tipoModif
     */
    private void obtenerPlazo(Integer tipoModif) {
        
        try {
            
            if (tipoModif == 1 && this.expedienteBean != null && this.expedienteBean.getIdExpediente() != null
                    && this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
                
                //consultamos si hay ya guardados o volcados plazos revisados por el gestor en la tabla temporal 
                List<ExpedienteModifPlazoDTO> lstExpModifPlazoDTO = this.expedienteModifPlazoService.
                        consultarModifPlazoPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), false);

                lstExpModifPlazoDTO = this.deduplicarPlazosMasRecientePorAnualidad(lstExpModifPlazoDTO);
                
                List<ExpedienteAnualidadDTO> listaExpedienteAnualidadDTO = this.expedienteAnualidadService.
                        consultarPlazosExpAnualidadPorIdExp(this.expedienteBean.getIdExpediente());               
                
                if (!listaExpedienteAnualidadDTO.isEmpty()) {
                    if (lstExpModifPlazoDTO.isEmpty()) {
                        //la primera vez:
                        //si la lista de plazos temp no ha sido cargada, es la primera vez y cargamos los plazos actuales desde el expediente
                        //la primera vez no cargamos los datos de plazos en la tabla a aceptar porque lo hará el boton de cargar datos
                        this.lstPlazosAntesMod = this.convertirExpAnualToExpModifPlazo(listaExpedienteAnualidadDTO);
                        
                        
                        for (ExpedienteModifPlazoDTO plazo : lstPlazosAntesMod) {
                            if (this.tlObservacionesModifPlazos == null) {
                                if(plazo.getTlObservaciones() != null) {
                                    this.tlObservacionesModifPlazos = plazo.getTlObservaciones() + "\n"; 
                                } 
                            } else {
                                if(plazo.getTlObservaciones() != null) {
                                    this.tlObservacionesModifPlazos += plazo.getTlObservaciones() + "\n"; 
                                } 
                            }
                        }
                        
                    }else {
                        //las siguientes veces:                        
                        //los plazos de antes de modif serán cargados con las fechas concedidas que haya guardadas en la tabla temporal
                        this.lstPlazosAntesMod = lstExpModifPlazoDTO;
                        
                        //los plazos a aceptar serán cargados también con las ultimas fechas que haya guardadas en la tabla temporal                      
                        this.lstPlazosAcept = lstExpModifPlazoDTO;
                        
                        for (ExpedienteModifPlazoDTO plazo : lstExpModifPlazoDTO) {
                            if (this.tlObservacionesModifPlazos == null) {
                                if(plazo.getTlObservaciones() != null) {
                                    this.tlObservacionesModifPlazos = plazo.getTlObservaciones() + "\n"; 
                                } 
                            } else {
                                if(plazo.getTlObservaciones() != null) {
                                    this.tlObservacionesModifPlazos +=  plazo.getTlObservaciones() + "\n"; 
                                } 
                            }
                        }
                    }
                }
            }else  if (tipoModif == 2) {
                this.obtenerPlazosSolicitados();                    
            }
	this.tlObservacionesModifPlazos = this.tlObservacionesModifPlazos.replace("null", "");
	
	        this.esconderBotonCargarPlazos();
	        
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    
    /**
     * MD-2920: como la tabla SAII_EXPEDIENTE_MODIF_PLAZO guarda un histórico (inserta en vez de
     * actualizar), deduplicamos en lectura quedándonos con el registro vigente (el más reciente) por anualidad
     */
    private List<ExpedienteModifPlazoDTO> deduplicarPlazosMasRecientePorAnualidad(List<ExpedienteModifPlazoDTO> lista) {
        if (lista == null || lista.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(lista.stream()
                .filter(p -> Objects.nonNull(p.getNmAnualidad()))
                .collect(Collectors.toMap(
                        ExpedienteModifPlazoDTO::getNmAnualidad,
                        p -> p,
                        (p1, p2) -> {
                            Date f1 = p1.getFcModifFila() != null ? p1.getFcModifFila() : p1.getFcAltaFila();
                            Date f2 = p2.getFcModifFila() != null ? p2.getFcModifFila() : p2.getFcAltaFila();
                            if (f1 == null) {
                                return p2;
                            }
                            if (f2 == null) {
                                return p1;
                            }
                            int cmp = f1.compareTo(f2);
                            if (cmp != 0) {
                                return cmp > 0 ? p1 : p2;
                            }
                            return (p1.getIdExpedienteModifPlazo() != null && p2.getIdExpedienteModifPlazo() != null
                                    && p1.getIdExpedienteModifPlazo() > p2.getIdExpedienteModifPlazo()) ? p1 : p2;
                        },
                        java.util.LinkedHashMap::new))
                .values());
    }

    /**
     * Método para convertir una lista de ExpAnualidad a una lista de ExpModifPlazos
     *
     */
    private List<ExpedienteModifPlazoDTO> convertirExpAnualToExpModifPlazo(
            List<ExpedienteAnualidadDTO> listaExpedienteAnualidadDTO) {
        
        List<ExpedienteModifPlazoDTO> listaExpModifPlazoAux = new ArrayList<>();

        for (ExpedienteAnualidadDTO expedienteAnual: listaExpedienteAnualidadDTO) {
            
            ExpedienteModifPlazoDTO expedienteModifPlazoDTO = new ExpedienteModifPlazoDTO();
            expedienteModifPlazoDTO.setIdExpedienteModifPresPlazoOtra
                (this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
            expedienteModifPlazoDTO.setIdExpedienteAnualidad(expedienteAnual.getIdExpedienteAnualidad());
            expedienteModifPlazoDTO.setTipoExpedienteAnualidadDTO(new CatalogosValoresDTO
                (expedienteAnual.getTipoExpedienteAnualidadDTO().getIdValor()));
            expedienteModifPlazoDTO.setNmAnualidad(expedienteAnual.getNmAnualidad());
            expedienteModifPlazoDTO.setFcIniEjecucion(expedienteAnual.getFcIniEjecucion());
            expedienteModifPlazoDTO.setFcFinEjecucion(expedienteAnual.getFcFinEjecucion());
              
            listaExpModifPlazoAux.add(expedienteModifPlazoDTO);
        }
        
        return listaExpModifPlazoAux;
    }

    
    /**
     * Metodo para obtener las fechas de los plazos de anualidades solicitados
     *
     * @param impresoDTO
     */
    private void obtenerPlazosSolicitados() {
       try {

           this.inicializarDatosPlazos();

           if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdImpreso() != null) {
               ImpresoDTO impresoDTO = this.impresoService.buscarId(this.selectExpedienteModifDTO.getIdImpreso());

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
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    public void prepararSubida(BloqueComponenteDTO presup) {
        // 1. Guardamos la fila utilizando el atributo que ya existe en el Bean
        this.filaBloquePresupuestoSeleccionada = presup;
        
        // 2. Limpiamos el archivo para que la modal se abra vacía
        this.archivoSubirDoc = null;
        
        PrimeFaces.current().executeScript("PF('dialogSubirExpedienteDocPresu').show();");
    }
    
    
//    public void gestionarSubidaFichero() {
//        if (this.archivoSubirDoc != null && this.filaBloquePresupuestoSeleccionada != null) {
//            try {
//                // Aquí ya tienes acceso al idDato de la fila real de presupuestos para asociarlo en SAI_EXPEDIENTE_DOC
//                Integer idDato = this.filaBloquePresupuestoSeleccionada.getDatos().get(0).getIdDato();
//                
//                // ... Aquí integras tu código de subida a FICA y tu insert en SAI_EXPEDIENTE_DOC ...
//                
//            } catch (Exception e) {
//                log.error("Error al gestionar la subida del fichero: " + e.getMessage());
//            }
//        }
//    }
    
    
    public void gestionarSubidaFichero(ActionEvent event) {
        // 1. Validaciones básicas de archivo (reutilizando tus constantes de Atlas)
        if (this.archivoSubirDoc == null || StringUtils.isBlank(this.archivoSubirDoc.getFileName())) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_ERROR_ARCHIVO);
            return;
        }

        if (this.filaBloquePresupuestoSeleccionada != null) {
            try (InputStream fileInputStream = this.archivoSubirDoc.getInputstream()) {
                
                // 2. Recuperamos el idDato que identificaba de forma única a esta partida presupuestaria
                Integer idDato = this.filaBloquePresupuestoSeleccionada.getDatos().get(0).getIdDato();
                
                if (idDato != null) {
                    String nombreFichero = this.archivoSubirDoc.getFileName();

                    // 3. Montamos el DTO de expediente igual que el código que has encontrado
                    ExpedienteDocDTO nuevoDocDTO = new ExpedienteDocDTO();
                    nuevoDocDTO.setFicaMetadatos(new FicaMetadatos());
                    nuevoDocDTO.getFicaMetadatos().setNombreFichero(nombreFichero);
                    nuevoDocDTO.getFicaMetadatos().setFechaFichero(new Date());
                    nuevoDocDTO.getFicaMetadatos().setExtension(nombreFichero.substring(nombreFichero.lastIndexOf('.') + 1).toLowerCase());
                    nuevoDocDTO.getFicaMetadatos().setUsuario(UserUtils.getUserName().toUpperCase());
                    nuevoDocDTO.getFicaMetadatos().setEncriptar(false);
                    
                    // Pasamos los bytes del fichero a memoria
                    nuevoDocDTO.setContenidoFichero(IOUtils.toByteArray(fileInputStream));

                    // Configuramos la relación con el expediente
                    nuevoDocDTO.setExpedienteDTO(new ExpedienteDTO());
                    nuevoDocDTO.getExpedienteDTO().setIdExpediente(this.idExpediente);
                    
                    // Mapeamos el idDato del presupuesto en el BloqueComponente del DTO
                    nuevoDocDTO.setBloqueComponenteDTO(new BloqueComponenteDTO());
                    nuevoDocDTO.getBloqueComponenteDTO().setDatos(this.filaBloquePresupuestoSeleccionada.getDatos());
                    
                    nuevoDocDTO.setTramiteDTO(new CatalogosValoresDTO() );
                    nuevoDocDTO.getTramiteDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.MODIFICACION_PRESP_PLAZOS_OTROS));
                    
                    nuevoDocDTO.setTipoDocumentoDTO(new CatalogosValoresDTO());
                    nuevoDocDTO.getTipoDocumentoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.DOCUMENTO_MODIF_CONCEPTO_PRESUP));
                   

                    // 4. Llamamos a un método en tu servicio que guarde en FICA y asocie el idDato
                    this.expedienteDocService.guardarDocumentoPresupuesto(nuevoDocDTO, idDato);

                    // VMM 26/09/26: se refresca la lista de documentos de la fila recien subida - sin
                    // esto, aunque el guardado en BD es correcto, la fila seguia mostrando la lista de
                    // documentos que tenia en memoria desde antes de abrir este dialogo (el boton
                    // "Guardar" es ajax="false", pero al ser un bean de scope de vista el postback
                    // reutiliza la misma instancia y no vuelve a consultar por si mismo), asi que el
                    // icono de Descargar (que depende de "not empty presupuesto.documentos") no
                    // aparecia hasta recargar la pestaña desde cero.
                    List<AtlasFilter> filtrosDocRecien = new ArrayList<>();
                    filtrosDocRecien.add(new AtlasFilter(Constantes.Filtro.FILTRO_DATO_ID_DATO, idDato.longValue(), Operador.EQUALS));
                    filtrosDocRecien.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    List<ExpedienteDocDTO> listaDocsRecien = this.expedienteDocService.buscar(0, -1, filtrosDocRecien, null);
                    List<ExpedienteDocDTO> docsRecienOrdenados = new ArrayList<>();
                    if (listaDocsRecien != null && !listaDocsRecien.isEmpty()) {
                        docsRecienOrdenados = listaDocsRecien.stream()
                                .filter(d -> d.getFcAltaFila() != null)
                                // VMM 14/10/26 MD-3141: solo documentos con fichero, ver obtenerPresupuestoSolicitado
                                .filter(d -> d.getCdFica() != null)
                                .sorted((d1, d2) -> d2.getFcAltaFila().compareTo(d1.getFcAltaFila()))
                                .collect(Collectors.toList());
                    }
                    // VMM 30/09/26 MD-3070: mismo motivo que en prepararHistorico - sin esto el documento recien
                    // subido tampoco mostraria su nombre en el dialogo de Historico de Documentos.
                    this.enriquecerNombreFicheroFica(docsRecienOrdenados);
                    this.filaBloquePresupuestoSeleccionada.setDocumentos(docsRecienOrdenados);

                    // 5. Mensaje de éxito estándar y limpieza
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                    
                    // Forzamos el cierre de la modal desde el servidor si todo ha ido bien
                    org.primefaces.PrimeFaces.current().executeScript("PF('dialogSubirExpedienteDocPresu').hide();");
                    
                    // Limpiamos
                    this.archivoSubirDoc = null;
                }

            } catch (IOException | ServiceException e) {
                log.error("Error al procesar el archivo presupuestario: " + e.getMessage(), e);
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            }
        }
    }
    
    /**
     * Método para inicializar los datos de las tablas de plazos con los valores de la convocatoria
     */
    private void inicializarDatosPlazos() throws ServiceException {
      
       // Recuperamos los datos de la convocatoria
       if (this.convocatoriaDTO == null) {
           this.convocatoriaDTO = this.convocatoriaService
               .buscarId(this.expedienteBean.getIdConvocatoria());
       }
       this.totalAnualidades = convocatoriaDTO.getNmDurMaxAyuda();

       this.construirLstAnualidades();
    }

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
    * Metodo para construir las listas de filas y columnas de la tabla de plazos solicitados
    * @throws ServiceException
    */
   private void obtenerFilColPlazoSolicitado(BloqueDTO bloque) throws ServiceException {

       // Construimos lista con idColumna
       int[] nColArray = IntStream.range(0, 2).toArray();
       this.listaNcolPlazo = new ArrayList<>();

       for (int arr : nColArray) {
           this.listaNcolPlazo.add(arr);
       }

       if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() != null) {
           
           // Recuperar datos de los bloques componente fila
           for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {

               List<DatoDTO> lstDatoDTO =  this.datoService.consultarDatosImpresoModifPorIdExpModif(this.expedienteBean.getIdExpediente(),
                       this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), fila.getIdBloqueComponente(), true);
               
               //filtramos por los más recientes por si se guardó mas de una modificacion en el fomulario
               if (lstDatoDTO.size() >= (this.listaNcolPlazo.size() * 2)) {              
                   lstDatoDTO = lstDatoDTO.stream()
                           .skip(Math.max(0, lstDatoDTO.size() - this.listaNcolPlazo.size())) // Saltar los primeros elementos
                           .collect(Collectors.toList());
               }
                   
               fila.setDatos(lstDatoDTO);
           }

           this.lstBloqueComponenteFilaPlazo = bloque.getListaBloqueAuxDTO();
       }
   }

    /**
     * Método que recupera la documentación aportada en las modificaciones de P/P/O de un expediente
     * 
     * @param idExpediente
     */
    private void obtenerDatosModifDocExpediente (Integer idExpedienteModifPresPlazoOtra) {
        try {

            if (idExpediente != null) {

                List<ExpedienteModfPresupPlazosOtrosDocDTO> lstExpedienteModifDoc = this.expedienteModfPresupPlazosOtrosDocService
                        .buscarPorExpedienteModfPresupPlazosOtros(idExpedienteModifPresPlazoOtra);

                this.lstDocumentosModif = lstExpedienteModifDoc.stream()
                        .map(expedienteModif -> expedienteModif.getExpedienteDocDTO())
                        .collect(Collectors.toList());
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
    }
    
    
    /**
     * Metodo para identificar si se habilita la descarga de documentos
     *
     * @param expDocDTO
     */
    public boolean habilitarBotonDescargaDoc(ExpedienteDocDTO expDocDTO) {
        Boolean habilitar = Boolean.TRUE;
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR).equals(expDocDTO.getEstadoDTO().getIdValor())) ||
                (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor())
                        && expDocDTO.getCdFica() == null ) ) {
            habilitar = Boolean.FALSE;
        }
        return habilitar;
    }
    
    /**
     * Método que controla la descargar de fichero por codigo Fica
     *
     * @param codigoFica
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
                    UtilesWeb.openFile(this.documentoDTO.getContenidoFichero(),
                            this.documentoDTO.getFicaFichero().getExtension(),
                            FilenameUtils.removeExtension(
                                    this.documentoDTO.getFicaFichero().getNombreFichero()));
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex);
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }
    
    
    /**
     * Método que realiza la actualizacion de los valores temporales de modificaciones presupuestarias de un expediente
     */
//    public void guardarModifPresupuesto() {
//        try {
//            
//          //VMM 16/04/26 MD-2638: validamos que el total solicitado y revisado por el gestor (3a tabla) no 
//            //supere el total concedido en financiacion (1a tabla)
//           if (validarImporteTotalAceptado()) {
//               
//                for (int i = 0; i < this.lstBloque.size(); i++) {
//                    
//                    List<ExpedientePresupDTO> presupuestosSinTotal = new ArrayList<>();       
//                    if (i == 0) {
//                        presupuestosSinTotal = this.lstPresupuestoFinanciableAcept1.stream().filter(
//                                presupuesto -> Objects.nonNull(presupuesto.getIdExpedientePresup()))
//                                .collect(Collectors.toList());
//                    } else {
//                        presupuestosSinTotal = this.lstPresupuestoFinanciableAcept2.stream().filter(
//                                presupuesto -> Objects.nonNull(presupuesto.getIdExpedientePresup()))
//                                .collect(Collectors.toList());
//                    }
//                    
//                    List<ExpedienteModifPresupMontoDTO> montosSinTotal = new ArrayList<>();                   
//                    for (ExpedientePresupDTO expedientePresup: presupuestosSinTotal) {
//                        if (!expedientePresup.getLstExpedienteModifPresupMontoDTO().isEmpty()) {
//                            montosSinTotal = expedientePresup.getLstExpedienteModifPresupMontoDTO();
//                        
//                            montosSinTotal = montosSinTotal.stream().filter(
//                                    monto -> Objects.nonNull(monto.getIdExpedienteModifPresupMonto()))
//                                    .collect(Collectors.toList());
//                        }
//                        
//                        for (ExpedienteModifPresupMontoDTO expModifPresupMonto: montosSinTotal) {
//                            ExpedienteModifPresupMontoDTO expModifPresupMontoDTO = this.expedienteModifPresupMontoService.
//                                    buscarId(expModifPresupMonto.getIdExpedienteModifPresupMonto());
//                            if (expModifPresupMontoDTO != null) {
//                                expModifPresupMontoDTO.setNmImporte(expModifPresupMonto.getNmImporte());
//                                this.expedienteModifPresupMontoService.actualizar(expModifPresupMontoDTO);  
//                            }
//                        }
//                    }      
//                }
//    
//                AtlasMessageUtils.addInfoMessageFromBundle(
//                        ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_OK);
//           }
//        } catch (ServiceException e) {
//            AtlasMessageUtils.addErrorMessageFromBundle(
//                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_KO);
//
//        }
//    }
//    
    /**
     * VMM 11/09/26 MD-2725: devuelve la observacion informada para la fila (mismo indice que usa el
     * xhtml para el campo cdObservaciones de la tabla de Aceptadas).
     * VMM 29/09/26: el indice depende de si la convocatoria es con o sin conceptos - con conceptos el
     * array de montos es 1-based (indice 1 = primera anualidad), sin conceptos es 0-based (indice 0 =
     * primera anualidad). Antes se usaba siempre 1, que en "sin conceptos" con una sola anualidad caia
     * en la fila TOTAL en vez de en la anualidad real (ver mismo ajuste en solapaModifPresPlazoOtra.xhtml).
     */
    private String obtenerObservacionFila(ExpedientePresupDTO presupuestoFinanciable) {
        int indiceObservacion = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup) ? 1 : 0;
        if (presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO() == null
                || presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO().size() <= indiceObservacion) {
            return null;
        }
        return presupuestoFinanciable.getLstExpedienteModifPresupMontoDTO().get(indiceObservacion).getCdObservaciones();
    }

    /**
     * VMM 14/09/26 MD-2725: igual que FinanciacionBean.notificarFilasPendientesObservacion - un unico
     * mensaje de error con las partidas/conceptos cuyo importe se ha modificado sin informar observacion.
     * Se comprueba el texto de observacion actual directamente (no basta con el flag pendienteObservacion,
     * que solo se marca a TRUE al editar el importe): quitamos el listener por campo que disparaba un
     * ajax por cada observacion al perder el foco, ya que con el commandButton de Guardar procesando
     * todo el formulario los valores de los inputs ya llegan aqui sin necesidad de esa llamada aparte.
     */
    private boolean notificarFilasPendientesObservacionModif() {
        if (this.lstPresupuestoFinanciableAcept1Modif == null || this.lstPresupuestoFinanciableAcept1Modif.isEmpty()) {
            return false;
        }
        List<String> identificadores = new ArrayList<>();
        for (ExpedientePresupDTO expPresup : this.lstPresupuestoFinanciableAcept1Modif) {
            if (expPresup.getTipoFinanciacionDTO() != null
                    && "TOTAL".equalsIgnoreCase(expPresup.getTipoFinanciacionDTO().getDsValor())) {
                continue;
            }
            if (Boolean.TRUE.equals(expPresup.getPendienteObservacion())
                    && StringUtils.isBlank(this.obtenerObservacionFila(expPresup))) {
                String partida = expPresup.getTipoFinanciacionDTO() != null
                        ? expPresup.getTipoFinanciacionDTO().getDsValor() : "";
                String identificador = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)
                        && StringUtils.isNotBlank(expPresup.getTlConcepto())
                        ? partida + " - " + expPresup.getTlConcepto()
                        : partida;
                identificadores.add(identificador);
            }
        }
        if (!identificadores.isEmpty()) {
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_OBSERVACION_PRESUP_PENDIENTES,
                    new Object[]{identificadores.size(), String.join(", ", identificadores)});
            return true;
        }
        return false;
    }

    /**
     * VMM 06/10/26 MD-3113: comprueba que ningun concepto de la tabla de aceptadas tenga la
     * descripcion vacia ni este repetido dentro de su partida (solo convocatorias con conceptos).
     * Emite los mensajes de error y devuelve true si hay alguno.
     */
    private boolean notificarConceptosInvalidosModif() {
        if (!Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)
                || this.lstPresupuestoFinanciableAcept1Modif == null) {
            return false;
        }
        boolean hayError = false;
        List<String> vacios = ValidacionConceptosPresupuesto
                .partidasConConceptoVacio(this.lstPresupuestoFinanciableAcept1Modif);
        if (!vacios.isEmpty()) {
            AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_VACIO,
                    new Object[] { vacios.size(), String.join(", ", vacios) });
            hayError = true;
        }
        List<String> repetidos = ValidacionConceptosPresupuesto
                .conceptosRepetidos(this.lstPresupuestoFinanciableAcept1Modif);
        if (!repetidos.isEmpty()) {
            AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_REPETIDO,
                    new Object[] { String.join(", ", repetidos) });
            hayError = true;
        }
        return hayError;
    }

    /**
     * VMM 18/09/26: guarda el nombre de concepto editado en la 3a tabla (Aceptadas), llamado desde
     * guardarModifPresupuesto() al pulsar Guardar. Se graba en SAII_EXPE_MOD_PRE_MONTO (no en el
     * expediente presupuestario en vivo), igual que el resto de la tabla (importes/observaciones),
     * ya que no debe aplicarse al concepto real hasta que la modificacion se resuelva.
     *
     * IMPORTANTE: solo se actualiza la fila cabecera del concepto (nmAnualidad=0), nunca las demas
     * anualidades. guardarModifPresupConcepto solo rellena tlConcepto en esa fila cabecera, y tanto
     * copiarListaPresupuestosMod (para pintar la tabla) como aplicarResolucionPresupuesto/FASE 3 (para
     * dar de alta conceptos nuevos que llegan de la solicitud sin ExpedientePresup todavia) se apoyan
     * en que solo la cabecera tenga tlConcepto relleno para agrupar las filas de un mismo concepto. Si
     * aqui se escribiera en todas las anualidades, un concepto nuevo aun sin resolver quedaria partido
     * en un "concepto" por cada anualidad en vez de uno solo.
     */
    public void guardarConceptoModif(ExpedientePresupDTO presupFinanciable) {
        try {
            if (presupFinanciable == null || presupFinanciable.getLstExpedienteModifPresupMontoDTO() == null) {
                return;
            }
            ExpedienteModifPresupMontoDTO cabecera = presupFinanciable.getLstExpedienteModifPresupMontoDTO().stream()
                    .filter(monto -> monto != null && monto.getIdExpedienteModifPresupMonto() != null
                            && Integer.valueOf(0).equals(monto.getNmAnualidad()))
                    .findFirst()
                    .orElse(null);
            if (cabecera == null) {
                return;
            }
            ExpedienteModifPresupMontoDTO montoDTO = this.expedienteModifPresupMontoService
                    .buscarId(cabecera.getIdExpedienteModifPresupMonto());
            if (montoDTO != null) {
                montoDTO.setTlConcepto(presupFinanciable.getTlConcepto());
                this.expedienteModifPresupMontoService.actualizar(montoDTO);
            }
        } catch (Exception e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
            log.error("No se pudo guardar el nombre del concepto de la modificacion.", e);
        }
    }

    public String guardarModifPresupuesto() {
        try {
            // VMM 11/09/26 MD-2725: bloqueamos el guardado si hay importes modificados sin observacion,
            // igual que se exige en la solapa de financiacion
            if (this.notificarFilasPendientesObservacionModif()) {
                return null;
            }

            // VMM 06/10/26 MD-3113: igual que en financiacion, no se guardan conceptos con la
            // descripcion vacia ni repetidos dentro de su partida (aqui el concepto SI se puede editar)
            if (this.notificarConceptosInvalidosModif()) {
                return null;
            }

            // VMM 16/04/26 MD-2638: validamos que el total solicitado y revisado por el gestor (3a tabla) no supere el total concedido en financiacion (1a tabla)
            if (validarImporteTotalAceptado()) {
                
                // Evaluamos la lista real vinculada a la tabla modificable
                if (this.lstPresupuestoFinanciableAcept1Modif != null && !this.lstPresupuestoFinanciableAcept1Modif.isEmpty()) {
                         
                    for (ExpedientePresupDTO expedientePresup : this.lstPresupuestoFinanciableAcept1Modif) {
                        
                        // 1. FILTRADO: Saltamos la fila del TOTAL general para evitar errores con IDs nulos o cálculos redundantes
                        if (expedientePresup.getTipoFinanciacionDTO() != null && 
                            "TOTAL".equalsIgnoreCase(expedientePresup.getTipoFinanciacionDTO().getDsValor())) {
                            continue;
                        }
                        
                        // VMM 17/09/26: el nombre de concepto ya no se graba sobre el expediente
                        // presupuestario en vivo (SAII_EXPEDIENTE_PRESUP) - se actualiza en
                        // SAII_EXPE_MOD_PRE_MONTO, igual que el resto de esta tabla no se aplica al
                        // expediente real hasta que la modificacion se resuelve.
//                        // GUARDAR CONCEPTO: Si tiene ID válido, actualizamos el texto del concepto en el registro principal
//                        if (Objects.nonNull(expedientePresup.getIdExpedientePresup())) {
//                            // Buscamos la entidad original del presupuesto en BD para no machacar otros campos
//                            ExpedientePresupDTO presEntity = this.expedientePresupService.buscarId(expedientePresup.getIdExpedientePresup());
//                            if (presEntity != null) {
//                                presEntity.setTlConcepto(expedientePresup.getTlConcepto());
//                                this.expedientePresupService.actualizarConceptoObservac(presEntity);
//                            }
//                        }
                        this.guardarConceptoModif(expedientePresup);

                        // GUARDAR MONTOS E IMPORTES (Anualidades)
                        if (expedientePresup.getLstExpedienteModifPresupMontoDTO() != null &&
                            !expedientePresup.getLstExpedienteModifPresupMontoDTO().isEmpty()) {
                            
                            // Filtramos para procesar solo los montos que tengan ID persistido y omitimos los objetos de totales cruzados
                            List<ExpedienteModifPresupMontoDTO> montosAProcesar = expedientePresup.getLstExpedienteModifPresupMontoDTO().stream()
                                    .filter(monto -> Objects.nonNull(monto.getIdExpedienteModifPresupMonto()))
                                    .collect(Collectors.toList());
                            
                            for (ExpedienteModifPresupMontoDTO expModifPresupMonto : montosAProcesar) {
                                
                                // Recuperamos el DTO de base de datos usando el Service
                                ExpedienteModifPresupMontoDTO expModifPresupMontoDTO = this.expedienteModifPresupMontoService.
                                        buscarId(expModifPresupMonto.getIdExpedienteModifPresupMonto());
                                        
                                if (expModifPresupMontoDTO != null) {
                                    // VMM 16/09/26: 'nmImporte' es el campo real editado en el p:inputNumber
                                    // (value="...lstExpedienteModifPresupMontoDTO[anualidad].nmImporte" en el
                                    // xhtml) - sin este set, buscarId() traia el nmImporte viejo de BD y
                                    // actualizar() lo volvia a grabar tal cual, perdiendo silenciosamente
                                    // cualquier edicion del gestor.
                                    expModifPresupMontoDTO.setNmImporte(expModifPresupMonto.getNmImporte());

                                    // nmImporteConcedido tambien se persiste (aunque para conceptos nuevos
                                    // siempre vale 0, no tienen un concedido original de referencia)
                                    expModifPresupMontoDTO.setNmImporteConcedido(expModifPresupMonto.getNmImporteConcedido());

                                    // GUARDAR OBSERVACIONES: Copiamos el valor de 'cdObservaciones' al DTO de base de datos
                                    expModifPresupMontoDTO.setCdObservaciones(expModifPresupMonto.getCdObservaciones());

                                    // Guardamos los cambios de la anualidad de forma persistente
                                    this.expedienteModifPresupMontoService.actualizar(expModifPresupMontoDTO);
                                }
                            }
                        }
                    }  
                }

                // VMM 06/10/26 MD-3078: refrescamos el resumen de observaciones (textarea de abajo, que
                // lista los cambios con su observacion por concepto) con lo que acabamos de guardar;
                // antes solo se calculaba al cargar la modificacion, asi que habia que salir y volver
                // a entrar al expediente para verlo.
                this.crearDatosObservaciones(this.lstPresupuestoFinanciableAcept1Modif);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_OK);

                return null;
            }
            return "ha excedido el valor concedido maximo";
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PRESUP_GRABAR_KO);
            
            javax.faces.context.FacesContext.getCurrentInstance().addMessage(null, 
                    new javax.faces.application.FacesMessage(
                        javax.faces.application.FacesMessage.SEVERITY_ERROR, 
                        "Error al guardar: " + e.getMessage(), ""));
                
             return null; // Al devolver null, JSF no redirige y permite ver el mensaje de error
        }
    }
    
    
 
    
    
    
    /**
     * Método que realiza el guardado de otras modificaciones de un expediente
     */
    public void guardarModifOtras(){
        try {
            //buscamos si ya se han insertado algunas modificaciones de otro tipo
            ExpedienteModifOtraDTO expdteModifOtraDTO = this.expedienteModifOtraService.
                    buscarIdExpedienteModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
            
            if (Objects.isNull(expdteModifOtraDTO)) {
                expdteModifOtraDTO = new ExpedienteModifOtraDTO();
                expdteModifOtraDTO.setExpedienteModifPresPlazoOtraDTO(new ExpedienteModifPresPlazoOtraDTO());
                expdteModifOtraDTO.getExpedienteModifPresPlazoOtraDTO().
                    setIdExpedienteModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
            }

            expdteModifOtraDTO.setTlOtrasModif(this.tlOtrasModificaciones);
            expdteModifOtraDTO.setTlOtrasModifObservaciones(this.tlOtrasModifObservaciones);
            

            //en caso de existir otras modificaciones para esta solicitud de modificacion la actualizamos
            //y si no existe la insertamos por primera vez
            this.expedienteModifOtraService.guardar(expdteModifOtraDTO);

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_OTRAS_MODIF_GRABAR_OK);
            
        } catch (ServiceException ex) {
            log.error(ex.getMessage(), ex);
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_OTRAS_MODIF_GRABAR_KO);
        }
    }
    
    
    /**
     * Método que realiza el guardado de observaciones de las modificaciones de un expediente
     */
    public void guardarModifObservaciones(){
        try {
            ExpedienteModifPresPlazoOtraDTO expdteModifPPODTO = this.selectExpedienteModifDTO;

            expdteModifPPODTO.setTlObservaciones(this.tlObservacionesModif);

            this.expedienteModifPresPlazoOtraService.guardar(expdteModifPPODTO);

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_OBSERVAC_MODIF_GRABAR_OK);
            
        } catch (ServiceException ex) {
            log.error(ex.getMessage(), ex);
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_OBSERVAC_MODIF_GRABAR_KO);
        }
    }
    
    /**
     * VMM 16/09/26: comprueba si alguna anualidad de lstPlazosAcept tiene la fecha fin de
     * ejecucion distinta a la que hay en BD (obtenerAnualidadOriginal) y, si es asi, exige que la
     * observacion compartida de plazos (tlObservacionesModifPlazos, el textarea unico debajo de
     * las tres tablas de plazos) este informada. A diferencia del presupuesto no hay observacion
     * por fila/concepto aqui, es una unica observacion para todas las anualidades modificadas.
     */
    /**
     * VMM 20/10/26 MD-3121: fecha fin de ejecucion SOLICITADA por el beneficiario para la anualidad indicada
     * (la que "Cargar datos" copia a los plazos aceptados), tomada de la tabla de plazos solicitados
     * (lstBloquePlazos, ultimo dato de la fila, igual que obtenerFechaFinPlazoSolicitado). El dato se guarda como
     * instante UTC (ver MD-3150). Devuelve null si no hay fecha solicitada para esa anualidad.
     */
    private Date obtenerFechaFinSolicitada(Integer nmAnualidad) {
        if (this.lstBloquePlazos == null || this.lstBloquePlazos.isEmpty()
                || this.lstBloquePlazos.get(0).getListaBloqueAuxDTO() == null) {
            return null;
        }
        List<BloqueComponenteDTO> filas = this.lstBloquePlazos.get(0).getListaBloqueAuxDTO();
        BloqueComponenteDTO fila = null;
        for (BloqueComponenteDTO f : filas) {
            if (f != null && f.getNmOrden() != null && f.getNmOrden().equals(nmAnualidad)) {
                fila = f;
                break;
            }
        }
        if (fila == null && filas.size() == 1) {
            fila = filas.get(0); // una sola anualidad: como hace cargarDatosPlazos
        }
        if (fila == null || fila.getDatos() == null || fila.getDatos().isEmpty()) {
            return null;
        }
        String dsValor = fila.getDatos().get(fila.getDatos().size() - 1).getDsValor();
        if (StringUtils.isBlank(dsValor) || dsValor.length() < 19) {
            return null;
        }
        try {
            return Date.from(LocalDateTime.parse(dsValor.substring(0, 19)).atZone(ZoneId.of("UTC")).toInstant());
        } catch (Exception e) {
            log.error("No se pudo interpretar la fecha fin solicitada: " + dsValor, e);
            return null;
        }
    }

    private boolean notificarObservacionPlazosPendiente() throws ServiceException {
        if (this.lstPlazosAcept == null || this.lstPlazosAcept.isEmpty()) {
            return false;
        }

        boolean hayFechaFinModificada = false;
        for (ExpedienteModifPlazoDTO expModifPlazo : this.lstPlazosAcept) {
            // VMM 20/10/26 MD-3121: la referencia para saber si el gestor ha modificado la fecha es la que
            // "Cargar datos" le puso (la solicitada por el beneficiario), no la original de la anualidad: al
            // cargar los datos la fecha aceptada ya difiere de la original sin que el gestor toque nada, y se
            // pedia observacion igualmente. Igual que en presupuestos, solo se exige si la cambia el gestor.
            // Si no hay fecha solicitada se mantiene el criterio anterior (comparar con la original).
            Date fechaReferencia = this.obtenerFechaFinSolicitada(expModifPlazo.getNmAnualidad());
            if (fechaReferencia == null) {
                ExpedienteAnualidadDTO anualOriginal = this.obtenerAnualidadOriginal(expModifPlazo.getNmAnualidad());
                fechaReferencia = anualOriginal != null ? anualOriginal.getFcFinEjecucion() : null;
            }
            if (fechaReferencia != null && expModifPlazo.getFcFinEjecucion() != null
                    && fechaReferencia.getTime() / 60000 != expModifPlazo.getFcFinEjecucion().getTime() / 60000) {
                hayFechaFinModificada = true;
                break;
            }
        }

        if (hayFechaFinModificada && StringUtils.isBlank(this.tlObservacionesModifPlazos)) {
            AtlasMessageUtils.addErrorMessage("Se ha modificado la fecha fin de ejecución de algún plazo; "
                    + "es obligatorio informar las observaciones antes de guardar.");
            return true;
        }

        return false;
    }

    /**
     * Método que realiza el guardado de modificaciones de plazos de un expediente
     */
    public void guardarModifPlazos(){
        try {

            // VMM 16/09/26: si se ha modificado la fecha fin de ejecucion de alguna anualidad
            // respecto al valor original en BD, exigimos informar la observacion compartida de
            // plazos antes de guardar, igual que ya se exige para el presupuesto
            // (notificarFilasPendientesObservacionModif en guardarModifPresupuesto).
            if (this.notificarObservacionPlazosPendiente()) {
                return;
            }

            if (!this.lstPlazosAcept.isEmpty()) {
                
                ExpedienteModifPlazoDTO expModifPlazoDTO = null;
                
                for (ExpedienteModifPlazoDTO expModifPlazo: this.lstPlazosAcept) {
                    
                    if (Boolean.TRUE.equals(this.verificarFechas(expModifPlazo))) {
                        
                        expModifPlazoDTO = this.expedienteModifPlazoService.
                                buscarId(expModifPlazo.getIdExpedienteModifPlazo());
                        
                        if (expModifPlazoDTO != null) {
                            expModifPlazoDTO.setFcIniEjecucion(expModifPlazo.getFcIniEjecucion());
                            expModifPlazoDTO.setFcFinEjecucion(expModifPlazo.getFcFinEjecucion());
                            
                            expModifPlazoDTO.setTlObservaciones(tlObservacionesModifPlazos);
                        
                            //actualiza los plazos modificados por el gestor
                            this.expedienteModifPlazoService.guardar(expModifPlazoDTO);  //voy a cambiar el actualizar por el guardar a ver si funciona
                        }
                    }
                }
                
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PLAZOS_GRABAR_OK);
                
                //recargamos la datatable si validó o no el proceso
                if (expModifPlazoDTO != null) {
                    //si hay guardados temporales recargo la tabla con los valores revisados por el gestor
                    List<ExpedienteModifPlazoDTO> lstExpModifPlazoRecargado = this.expedienteModifPlazoService.
                            consultarModifPlazoPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), true);
                    this.lstPlazosAcept = this.deduplicarPlazosMasRecientePorAnualidad(lstExpModifPlazoRecargado);
                }else {
                    //si es la primera vez, y no hay valores revisados recargo la tabla con lo que hubiera en el expediente anualidad
                    List<ExpedienteAnualidadDTO> listaExpedienteAnualidadDTO = this.expedienteAnualidadService
                            .buscarPorExpediente(this.idExpediente);
                                                          
                    this.lstPlazosAcept = this.convertirExpAnualToExpModifPlazo(listaExpedienteAnualidadDTO);
                }
                    
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_MODIFPPO_MODIF_PLAZOS_GRABAR_KO);

        }
    }
    
    /**
     * Método para validar los valores de las fechas de plazos de anualidades introducidos
     *
     **/
    private Boolean verificarFechas(ExpedienteModifPlazoDTO dto) throws ServiceException {
        Boolean esValido = Boolean.TRUE;

        if (Boolean.FALSE.equals(this.verificarCamposVacias(dto))) {
            esValido = Boolean.FALSE;
        } else if (Boolean.FALSE.equals(this.verificarLimiteEdicionFinEjecucion(dto))) {
            esValido = Boolean.FALSE;
        } else {
            if (Boolean.FALSE.equals(this.verificarRangoFechas(dto))) {
                esValido = Boolean.FALSE;
            } else {
                if (Boolean.TRUE.equals(this.verificarFechasRepetidas(dto))) {
                    esValido = Boolean.FALSE;
                }
            }
        }

        return esValido;
    }

    /**
     * VMM 14/09/26 MD-2725: recupera de BD el registro original (no modificado) de la anualidad,
     * usado como referencia para validar/proteger los cambios de plazos de esa anualidad.
     */
    private ExpedienteAnualidadDTO obtenerAnualidadOriginal(Integer nmAnualidad) throws ServiceException {
        return this.expedienteAnualidadService.buscarPorExpediente(this.expedienteBean.getIdExpediente()).stream()
                .filter(anual -> anual.getNmAnualidad().equals(nmAnualidad))
                .findFirst().orElse(null);
    }

    /**
     * VMM 14/09/26 MD-2725: aplica a la modificacion de plazos la misma regla de 60 dias que al
     * presupuesto (no se puede tocar la fecha fin de ejecucion si ya estamos a 60 dias o menos de
     * ella) y, ademas, bajo ningun caso se permite cambiar la fecha inicio de ejecucion aqui - se
     * fuerza siempre al valor que ya hay en BD, como salvaguarda aunque el campo no sea editable
     * en el formulario (esta comentado en solapaModifPresPlazoOtra.xhtml).
     */
    private Boolean verificarLimiteEdicionFinEjecucion(ExpedienteModifPlazoDTO dto) throws ServiceException {
        Boolean esValido = Boolean.TRUE;

        ExpedienteAnualidadDTO anualOld = this.obtenerAnualidadOriginal(dto.getNmAnualidad());

        if (anualOld != null) {
            if (this.superaLimiteEdicionAnualidad(anualOld.getFcFinEjecucion())) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FECHA_ANUALIDAD_PROXIMA_FIN_PLAZO);
                esValido = Boolean.FALSE;
            }

            dto.setFcIniEjecucion(anualOld.getFcIniEjecucion());
        }

        return esValido;
    }

    /**
     * VMM 14/09/26 MD-2725: listener de la fecha fin de ejecucion en la tabla de plazos aceptados -
     * da el aviso inmediato del limite de 60 dias y revierte al valor de BD si no es valido (el
     * guardado real se vuelve a validar igualmente en verificarFechas al pulsar Guardar).
     */
    public void validarFechaFinEjecucionPlazo(ExpedienteModifPlazoDTO dto) {
        try {
            if (Boolean.FALSE.equals(this.verificarLimiteEdicionFinEjecucion(dto))) {
                ExpedienteAnualidadDTO anualOld = this.obtenerAnualidadOriginal(dto.getNmAnualidad());
                if (anualOld != null) {
                    dto.setFcFinEjecucion(anualOld.getFcFinEjecucion());
                }
            }
        } catch (ServiceException e) {
            log.error("Error validando el limite de edicion de la fecha fin de ejecucion", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Método para verificar que las fechas de anualidades sean informadas
     * 
     * @param ExpedienteAnualidadDTO
     * @return
     */
    private Boolean verificarCamposVacias(ExpedienteModifPlazoDTO dto) {
        Boolean esValido = Boolean.TRUE;

        if (Objects.isNull(dto.getFcIniEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_VACIO);
            esValido = Boolean.FALSE;
        }

        if (Objects.isNull(dto.getFcFinEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_VACIO);
            esValido = Boolean.FALSE;
        }
        
        return esValido;
    }

    /**
     *  Método para verificar que los rangos de fechas de anualidades sean correctos
     *  
     * @param ExpedienteAnualidadDTO
     * @return
     */
    private Boolean verificarRangoFechas(ExpedienteModifPlazoDTO dto) {
        Boolean esValido = Boolean.TRUE;

        // si la fecha de fin de ejecucion es mayor o igual a la de inicio
        if (dto.getFcFinEjecucion().before(dto.getFcIniEjecucion())
                || dto.getFcIniEjecucion().equals(dto.getFcFinEjecucion())) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_EJECUCION_ERROR);
            esValido = Boolean.FALSE;
        }

       return esValido;
    }

    /**
     * Método para verificar que si las fechas de anualidades se han repetido
     * 
     * @param ExpedienteModifPlazoDTO
     * @return
     */
    private Boolean verificarFechasRepetidas(ExpedienteModifPlazoDTO dto) {
        Boolean esRepetido = Boolean.FALSE;

        for (ExpedienteModifPlazoDTO item : this.lstPlazosAcept) {

            if (!item.getNmAnualidad().equals(dto.getNmAnualidad())) {

                if (Objects.nonNull(item.getFcIniEjecucion())
                        && item.getFcIniEjecucion().equals(dto.getFcIniEjecucion())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_INI_EJECUCION_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }

                if (Objects.nonNull(item.getFcFinEjecucion())
                        && item.getFcFinEjecucion().equals(dto.getFcFinEjecucion())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_FIN_EJECUCION_REPETIDO);
                    esRepetido = Boolean.TRUE;
                }
            }
        }

        return esRepetido;
    }
    
    
    /**
     * Método para verificar que si la anualidad se ha iniciado
     * 
     * @param ExpedienteModifPlazoDTO
     * @return
     * @throws ServiceException 
     */
    public void verificarAnualidadIniciada(ExpedienteModifPlazoDTO dto) throws ServiceException {
        
        Boolean tieneErroresAnual = Boolean.FALSE;
        Integer nmAnualidadEdit = dto.getNmAnualidad() + 1;
        
        //comprobamos que no se ha iniciado la anualidad para las modificaciones
        tieneErroresAnual = this.comprobarFechaAnualVencida(nmAnualidadEdit, tieneErroresAnual, dto.getFcIniEjecucion());
        
        if (tieneErroresAnual.equals(Boolean.TRUE)) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_FECHA_ANUALIDAD_INICIADA);
        }

        //recargamos la datatable si validó o no el proceso
        //si hay guardados temporales recargo la tabla con los valores revisados por el gestor
        List<ExpedienteModifPlazoDTO> expModifPlazoDTO =  this.expedienteModifPlazoService.
                consultarModifPlazoPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), true);

        if (expModifPlazoDTO != null) {
            this.lstPlazosAcept = this.deduplicarPlazosMasRecientePorAnualidad(expModifPlazoDTO);  //ver si esto lo copia bien
        }else {
            //si es la primera vez, y no hay valores revisados temporales recargo la tabla con lo que hubiera en el expediente anualidad
            //casi que mejor meter esta consulta la primera vez en una var global y tirar de ella para no consultar esto tantas veces
            List<ExpedienteAnualidadDTO> listaExpedienteAnualidadDTO = this.expedienteAnualidadService
                    .buscarPorExpediente(this.idExpediente);
                                                  
            this.lstPlazosAcept = this.convertirExpAnualToExpModifPlazo(listaExpedienteAnualidadDTO);
        }
    }
    
    
    /**
     * Método para grabar la asignación de causa de subsanación al documento
     */
    public void grabarCausaSubsanarDocumento() {

        try {

            if (this.documentoSeleccionado != null) {

                if (this.validarFormularioSubsanacion().equals(Boolean.FALSE)) {
                    ConvocatoriaCauDTO convocatoriaCauDTO = this.convocatoriaCauService
                            .buscarIdConvocatoriaCauDTO(this.expedienteBean.getIdConvocatoria(),
                                    this.idCausaSubsanacion);
                    this.documentoSeleccionado.setEstadoDTO(this.catalogosValoresService
                            .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)));

                    this.documentoSeleccionado
                            .setIdCausa(convocatoriaCauDTO.getCausaDTO().getIdValor());

                    if (convocatoriaCauDTO.getCausaDTO().getCdCodigo()
                            .equals(Constantes.CausaDenegacion.CODIGO_OTROS)) {
                        this.documentoSeleccionado
                                .setTlCausaDescripcion(this.descripcionCausaSubsanacion);
                    } else {
                        this.documentoSeleccionado.setTlCausaDescripcion(
                                convocatoriaCauDTO.getCausaDTO().getDsValor());
                    }

                    this.expedienteDocService.guardarConHistorico(this.documentoSeleccionado);
//                    this.actualizarValoresFormulario();
                    this.inicializarFormularioSubsanacion();
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                    PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoActa').hide();");
                    PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

                }

            } else {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTOS_NO_SELECCION_DOCUMENTO);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
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
     * Método para validar los datos del formulario de subsanación.
     *
     * @return Boolean Indicador true o false
     */
    private Boolean validarFormularioSubsanacion() {
        Boolean existeError = Boolean.FALSE;
        if (this.idCausaSubsanacion == null) {
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_CAUSA_DENEGACION_NO_SELECCIONADO);
            existeError = Boolean.TRUE;
        } else if (this.descripcionCausaObligatoria.equals(Boolean.TRUE)
                && StringUtils.isBlank(this.descripcionCausaSubsanacion)) {
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_DESC_CAUSA_DENEGACION_NO_INGRESADO);
            existeError = Boolean.TRUE;
        }
        return existeError;
    }
  

    /**
     * Metodo para identificar si se habilita la fecha de resolucion
     *
     * @param dto
     * @throws ServiceException 
     */
    public boolean habilitarFechaResolucion() throws ServiceException {
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite informar Fecha/Tipo
        // Resolución ni subir la resolución (aquí "habilitar" true == disabled, se devuelve tal cual)
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.TRUE;
        }

        Boolean habilitar = Boolean.TRUE;

//        ExpedienteDocDTO actaFinal = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ACTA_FINAL_COMITE_GESTION));
//        
//        if (this.convocatoriaDTO == null) {
//            this.convocatoriaDTO = convocatoriaService.buscarId(this.expedienteBean.getIdConvocatoria());
//        }
//        
//        
//        if(!Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItActasComitesGestion()) || 
//                ( this.convocatoriaDTO.getItActasComitesGestion().equals(Constantes.AfirmacionNegacion.S) && actaFinal != null && actaFinal.getEstadoDTO().getIdValor() != null
//                 && actaFinal.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)))) {
//            
//            if (this.selectExpedienteModifDTO != null  &&  this.selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor() ==  null 
//                    && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
//                    && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(
//                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE))) {
//                habilitar = Boolean.FALSE;
//            }
//            
//        }
        
        if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getEstadoDTO() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES))) {
            habilitar = Boolean.FALSE;
        }

        // VMM 26/09/26 MD-3065: el "capamos este input si es modo consulta" (MD-2695) hacia justo lo
        // contrario de lo que dice su comentario - ponia habilitar=FALSE (== editable, ver comentario
        // de la firma del metodo) en vez de TRUE (== deshabilitado) cuando modoConsultaModifPPO es
        // true. Como al enviar la resolucion a la cola de Portafirmas el estado deja de ser PEDIENTE
        // (regla MD-2935 en esModoConsulta()), modoConsultaModifPPO pasaba a true y esa inversion
        // volvia a dejar editables Fecha/Tipo Resolucion (y por tanto tambien el boton "Subir
        // resolucion", que depende de este mismo metodo) justo despues de enviar. Se separa en su
        // propio if con la polaridad correcta, y va despues para que el modo consulta tenga
        // prioridad si en algun caso coincidiera con el estado anterior.
        //
        // VMM 29/09/26: ese mismo catch-all de la regla MD-2935 ("si el estado no es PEDIENTE, es modo
        // consulta") tambien se activa en PEDIENTE_MODIFICACIONES, que es precisamente el estado al que
        // "Finalizar revisión" deja la modificación para que el gestor informe Fecha/Tipo Resolución y
        // suba el documento - asi que sin esta excepcion, la prioridad de arriba volvia a bloquear esos
        // campos justo cuando deben habilitarse (el gestor no podia nunca llegar a subir la resolución).
        // Se excluye ese estado del capado generico, salvo que el bloqueo sea por una razon "dura" que
        // debe aplicar siempre (convocatoria en modo consulta global o rol de gestor externo).
        boolean esPendienteModificaciones = this.selectExpedienteModifDTO != null
                && this.selectExpedienteModifDTO.getEstadoDTO() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES));
        boolean modoConsultaDuro = Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno());

        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO()) && (modoConsultaDuro || !esPendienteModificaciones)) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

    /**
     * VMM 08/09/26 MD-2920: el botón "Subir resolución" no debe habilitarse solo porque el estado
     * lo permita (igual que Fecha/Tipo Resolución): además hace falta que el gestor ya haya
     * informado ambos campos, o de lo contrario guardarDocumento() aborta con un mensaje de error
     * tras haber dejado subir el fichero (como se vio en DESA).
     *
     * @return true si el botón "Subir resolución" debe quedar deshabilitado
     */
    public boolean habilitarBotonSubirResolucion() throws ServiceException {
        // VMM 26/09/26 MD-3065: una vez subido el documento de resolucion no se debe poder volver a
        // pulsar "Subir resolución" (solo "Descargar"). Ademas de evitar confusion, esto ataja de raiz
        // la causa del enlace duplicado en SAII_EXPE_MOD_PRE_PLA_OTRA_DOC (ver
        // guardarExpedienteModfPresupPlazosOtrosDoc): si no se puede volver a enviar el formulario de
        // subida para una modificacion que ya tiene el documento, no se puede volver a crear el enlace.
        return this.habilitarFechaResolucion()
                || this.fcResolucion == null
                || this.estadoResolucionDTO == null
                || this.estadoResolucionDTO.getIdValor() == null
                || Boolean.TRUE.equals(this.habilitarBotonDescargar());
    }

    /**
     * Indica si la modificación seleccionada está en estado Iniciada. Mientras esté en este
     * estado, los datos y botones de resolución (fecha solicitud, tipo resolución, finalizar
     * revisión, subir/descargar resolución y enviar a cola portafirmas) deben permanecer
     * deshabilitados.
     *
     * @return true si la modificación está en estado Iniciada
     */
    public boolean esModificacionEnEstadoIniciada() {
        return this.selectExpedienteModifDTO != null
                && this.selectExpedienteModifDTO.getEstadoDTO() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.INICIADA));
    }



    public boolean deshabilitarBotonesGuardar() throws ServiceException {
        Boolean habilitar = Boolean.FALSE;

        if(!this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().
                equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE))) {
            habilitar = Boolean.TRUE;

        }
        
        return habilitar;
    }
    
    
    public void cambioCausaTipoResolucion() {
        
        if (this.selectExpedienteModifDTO != null ) {
           // this.selectExpedienteModifDTO.setEstadoResolucionDTO(this.estadoResolucionDTO);
        }

    }
    
    
//    public void finalizarRevision() {
//          try {
//              
//              if(this.fcResolucion != null && this.estadoResolucionDTO.getIdValor() != null) {
//                  
//                  //VMM 16/04/26 MD-2638: validamos que el total solicitado y revisado por el gestor (3a tabla) no 
//                  //supere el total concedido en financiacion (1a tabla)
//                 if (validarImporteTotalAceptado()) {
//                  
//                      //hacemos el guardado           
//                      this.selectExpedienteModifDTO.setEstadoResolucionDTO(this.estadoResolucionDTO);
//                      this.selectExpedienteModifDTO.setFcResolucion(this.fcResolucion);
//                      this.selectExpedienteModifDTO.setEstadoDTO(this.catalogosValoresService.
//                              buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_PORTAFIRMAS))); 
//        
//                      this.expedienteModifPresPlazoOtraService.guardar(this.selectExpedienteModifDTO);
//                                       
//                      cargarInformacion();
//                      
//                      PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
//                 }
//            
//              }else {
//                  AtlasMessageUtils.addErrorMessageFromBundle("Seleccione una fecha de resolucion y un tipo de resolución");
//              }
//            
//        } catch (ServiceException e) {
//            AtlasMessageUtils .addErrorMessageFromBundle("Error al Finalizar la revisión");
//        }  
//
//    }

    public boolean validarImporteTotalAceptado() {
          Double importeTotalAcept = 0.0;
          // VMM 16/09/26: era importeTotalSolic, comparando contra la tabla 2 (Presupuesto
          // Modificaciones Solicitadas / lstPresupuestoAyudaConcedidaSolicitada). El mensaje de
          // error habla de "ayuda concedida", y la regla de negocio real es que el gestor no puede
          // aceptar mas de lo que ya estaba concedido ANTES de esta modificacion (tabla 1,
          // lstPresupuestoAyudaConcedidaAntesMod), no mas de lo solicitado en la modificacion.
          Double importeTotalConcedido = 0.0;
          if (this.lstPresupuestoAyudaConcedidaAcept != null && !this.lstPresupuestoAyudaConcedidaAcept.isEmpty()) {
              
              //cogemos la fila/registro del ultimo presupuesto de la ayuda aceptada por el gestor (3a tabla)
              
              ExpedientePresupDTO presupTotalAcept = this.lstPresupuestoAyudaConcedidaAcept
                      .get(this.lstPresupuestoAyudaConcedidaAcept.size() - 1);
              
              if (presupTotalAcept.getLstExpedienteModifPresupMontoDTO() != null &&
                      !presupTotalAcept.getLstExpedienteModifPresupMontoDTO().isEmpty()) {

                      //cogemos el ultimo registro de los totales con el importe total concedido/aceptado por el gestor
                      ExpedienteModifPresupMontoDTO montoModifTotalAcept = presupTotalAcept.getLstExpedienteModifPresupMontoDTO()
                          .get(presupTotalAcept.getLstExpedienteModifPresupMontoDTO().size() - 1);

                      // VMM 16/09/26: era getNmImporteConcedido(), que en la fila TOTAL se queda congelado
                      // con el valor calculado en la carga inicial de pagina (modificarTotales solo
                      // actualiza nmImporte en cada edicion de celda, nunca nmImporteConcedido) y ademas
                      // los conceptos nuevos siempre tienen nmImporteConcedido=0 (no tienen un concedido
                      // original de referencia). nmImporte es el campo que el gestor edita de verdad y el
                      // que hay que comparar contra el total solicitado.
                      importeTotalAcept = montoModifTotalAcept.getNmImporte();
               }else {
                   AtlasMessageUtils.addErrorMessage("Error, no se encuentran los montos modificados de los presupuestos aceptados");
               }
          }else {
              AtlasMessageUtils.addErrorMessage("Error, no se encuentran los presupuestos aceptados de la modificación");
          }
          
          // Bloque anterior, comparaba contra la tabla 2 (Solicitada) en vez de la tabla 1 (Concedida).
          // Se deja comentado por si hay que volver atras.
//          if (this.lstPresupuestoAyudaConcedidaSolicitada != null && !this.lstPresupuestoAyudaConcedidaSolicitada.isEmpty()) {
//
//              //cogemos la fila/registro del ultimo presupuesto de la ayuda concedida de lo solicitado
//              BloqueComponenteDTO presupTotalSolic = this.lstPresupuestoAyudaConcedidaSolicitada
//                      .get(this.lstPresupuestoAyudaConcedidaSolicitada.size() - 1);
//
//              if (presupTotalSolic.getDatos() != null && !presupTotalSolic.getDatos().isEmpty()) {
//
//                      //cogemos el ultimo registro de los totales con el importe total solicitado en saii_sol
//                      DatoDTO montoModifTotalSolic = presupTotalSolic.getDatos()
//                          .get(presupTotalSolic.getDatos().size() - 1);
//
//                      importeTotalConcedido = Double.parseDouble(montoModifTotalSolic.getDsValor());
//               }else {
//                   AtlasMessageUtils.addErrorMessage("Error, no se encuentran los montos de los presupuestos solicitados del formulario de modificación");
//                   return false;
//               }
//          }else {
//              AtlasMessageUtils.addErrorMessage("Error, no se encuentran los presupuestos solicitados del formulario de modificación");
//              return false;
//          }

          if (this.lstPresupuestoAyudaConcedidaAntesMod != null && !this.lstPresupuestoAyudaConcedidaAntesMod.isEmpty()) {

              //cogemos la fila/registro del ultimo presupuesto de la ayuda concedida ANTES de esta modificacion (tabla 1)
              ExpedientePresupDTO presupTotalConcedido = this.lstPresupuestoAyudaConcedidaAntesMod
                      .get(this.lstPresupuestoAyudaConcedidaAntesMod.size() - 1);

              if (presupTotalConcedido.getLstExpedientePresupMontoDTO() != null
                      && !presupTotalConcedido.getLstExpedientePresupMontoDTO().isEmpty()) {

                      //cogemos el ultimo registro de los totales con el importe total ya concedido en el expediente
                      ExpedientePresupMontoDTO montoTotalConcedido = presupTotalConcedido.getLstExpedientePresupMontoDTO()
                          .get(presupTotalConcedido.getLstExpedientePresupMontoDTO().size() - 1);

                      importeTotalConcedido = montoTotalConcedido.getNmImporte();
               }else {
                   AtlasMessageUtils.addErrorMessage("Error, no se encuentran los montos del presupuesto concedido del expediente");
                   return false;
               }
          }else {
              AtlasMessageUtils.addErrorMessage("Error, no se encuentran los presupuestos concedidos del expediente");
              return false;
          }

          if (importeTotalAcept != null && importeTotalConcedido != null && importeTotalAcept > importeTotalConcedido) {
              AtlasMessageUtils.addErrorMessage("Error, el importe total de la ayuda aceptada es superior al importe total de la ayuda concedida");
              return false;
          }
          
          return true;
    }
    
    
    public void cargarFicheroProvisional() {

        try {
            Integer tramite = null;

            // VMM 26/09/26 MD-3059: si se pulsa "Guardar" sin haber elegido fichero,
            // this.archivoResolucion es directamente null (no solo su fileName) - llamar a
            // getFileName() sobre null lanzaba un NullPointerException no controlado (el catch de
            // abajo solo cubre ServiceException), que se propagaba sin capturar y sacaba al usuario
            // de la aplicacion en vez de mostrar el aviso de "selecciona un fichero".
            if (this.archivoResolucion != null && this.archivoResolucion.getFileName() != null) {
                this.prepararMetadatosFica(this.archivoResolucion, utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PRESUPUESTO_MODIF), tramite);
                this.expedienteDocDTO = this.expedienteDocService.subirDocumentoFica(this.expedienteDocDTO);
                this.guardarDocumento();

                this.recuperarValoresVariablesExpediente(this.expedienteBean.getExpedienteDTO());
                this.mostrarMensajeElegirFichero = Boolean.FALSE;

                this.recargarInformacionManteniendoSeleccion();
               PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
                
                
            } else {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                // VMM 08/09/26 MD-2920: "dialogSubirResolucionProvisional" no existe en esta pantalla
                // (es el id de solapaResolucion.xhtml); el diálogo de esta pantalla es "dialogSubirResolucion".
                PrimefacesUtils.mostrarPopupPF("dialogSubirResolucion");
            }
        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     */
    public void guardarDocumento() {

        try {

            ExpedienteDocDTO documento = this.expedienteDocService.guardar(this.expedienteDocDTO);
            
            if(documento != null) {
                
                if(this.fcResolucion != null && this.estadoResolucionDTO.getIdValor() != null) {  
                
                
              
                ExpedienteModfPresupPlazosOtrosDocDTO docRelacion =  new ExpedienteModfPresupPlazosOtrosDocDTO();
                docRelacion.setExpedienteDocDTO(documento);
                docRelacion.setExpedienteModifPresPlazoOtraDTO(this.selectExpedienteModifDTO);
                this.expedienteModfPresupPlazosOtrosDocService.guardarExpedienteModfPresupPlazosOtrosDoc(docRelacion);
                
                                
                  //VMM 16/04/26 MD-2638: validamos que el total solicitado y revisado por el gestor (3a tabla) no
                  //supere el total concedido en financiacion (1a tabla)
                  // VMM 09/09/26 MD-2725: esta validación solo aplica si la modificación es de
                  // presupuesto (itModPres='S') - si no, no hay presupuestos aceptados/solicitados
                  // que comparar y validarImporteTotalAceptado() fallaba siempre, bloqueando el
                  // guardado de Fecha/Tipo Resolución para modificaciones de plazo/otra.
                 if (!Constantes.AfirmacionNegacion.S.equals(this.selectExpedienteModifDTO.getItModPres())
                         || validarImporteTotalAceptado()) {

                      //hacemos el guardado
                      this.selectExpedienteModifDTO.setEstadoResolucionDTO(this.estadoResolucionDTO);
                      this.selectExpedienteModifDTO.setFcResolucion(this.fcResolucion);
                      this.selectExpedienteModifDTO.setEstadoDTO(this.catalogosValoresService.
                              buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA)));

                      this.expedienteModifPresPlazoOtraService.guardar(this.selectExpedienteModifDTO);

                     this.recargarInformacionManteniendoSeleccion();

                     // VMM 08/09/26 MD-2920: el mensaje de éxito estaba fuera de este bloque, así que
                     // se mostraba siempre (incluso sin fecha/tipo de resolución informados, a la vez
                     // que el mensaje de error de más abajo). Ahora solo se muestra cuando el documento
                     // quedó realmente enlazado a la modificación y esta avanzó a Pendiente Envío Cola.
                     AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
                 }

              }else {
                  AtlasMessageUtils.addErrorMessage("Seleccione una fecha de resolucion y un tipo de resolución");
              }

            }

        } catch (ServiceException e) {

            if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(e.getKeyExcepcion())) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage(), e);
            }

            this.expedienteDocDTO.setFicaMetadatos(new FicaMetadatos());

        }
    }
    
    /**
     * Metodo para recuperar variables necesarias para validaciones de
     * expediente
     *
     * @param expedienteDTO
     * @throws ServiceException
     */
    private void recuperarValoresVariablesExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        expedienteDTO.setEsCerradoCoordinadorTodo(this.expedienteEvalService
                .esCerradoCoordinadorTodos(expedienteDTO.getIdExpediente()));

        expedienteDTO.setMostrarBotoneraAprobarRechazarEvaluacion(
                this.expedienteBean.getEvaluacionBean().verificarMostrarBotoneraEvaluadoGestor());

        ExpedienteDTO dto = this.expedienteService
                .comprobarResolucionDefinitiva(expedienteDTO.getIdExpediente(), this.expedienteBean.getConvocatoriaDTO());

        // asignamos los resultados de validación al expedienteDTO
        // de expedienteBean
        expedienteDTO.setEsExpedienteSolicitante(dto.getEsExpedienteSolicitante());
        expedienteDTO.setBlHabilitarResolucionDefinitiva(dto.getBlHabilitarResolucionDefinitiva());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo1(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo1());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo2(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo2());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo3(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo3());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual1(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual1());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual2(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual2());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual3(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual3());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual4(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual4());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual5(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual5());

        this.expedienteBean.setExpedienteDTO(expedienteDTO);

    }

    
    /**
     * 
     * @return
     * @throws ServiceException 
     */
    /**
     * VMM 26/09/26 MD-3067: indica si la modificación incluye "otras modificaciones" (itModOtra=S) y,
     * si es así, si ya se ha cargado al menos una fila para revisar. Si no incluye otras
     * modificaciones, devuelve true (no aplica, no debe bloquear ni avisar).
     */
    public Boolean existeModifOtraCargada() throws ServiceException {
        if (this.selectExpedienteModifDTO == null
                || !Constantes.AfirmacionNegacion.S.equals(this.selectExpedienteModifDTO.getItModOtra())
                || this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() == null) {
            return Boolean.TRUE;
        }
        List<ExpedienteModifOtraDTO> listaModifOtras = this.expedienteModifOtraService
                .buscarPorIdExpModifPresPlazoOtra(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), false);
        return listaModifOtras != null && !listaModifOtras.isEmpty();
    }

    /**
     * VMM 26/09/26 MD-3067: igual que existeModifOtraCargada() pero para el presupuesto modificado
     * (itModPres=S).
     */
    public Boolean existeModifPresupuestariaCargada() throws ServiceException {
        if (this.selectExpedienteModifDTO == null
                || !Constantes.AfirmacionNegacion.S.equals(this.selectExpedienteModifDTO.getItModPres())
                || this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() == null) {
            return Boolean.TRUE;
        }
        List<ExpedienteModifPresupMontoDTO> listaModifPresup = this.expedienteModifPresupMontoService
                .consultarModifPresupPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
        return listaModifPresup != null && !listaModifPresup.isEmpty();
    }

    /**
     * VMM 26/09/26 MD-3067: igual que existeModifOtraCargada() pero para la modificación de plazos
     * (itModPlazo=S).
     */
    public Boolean existeModifPlazoCargada() throws ServiceException {
        if (this.selectExpedienteModifDTO == null
                || !Constantes.AfirmacionNegacion.S.equals(this.selectExpedienteModifDTO.getItModPlazo())
                || this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() == null) {
            return Boolean.TRUE;
        }
        List<ExpedienteModifPlazoDTO> listaModifPlazos = this.expedienteModifPlazoService
                .consultarModifPlazoPorIdModif(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(), false);
        return listaModifPlazos != null && !listaModifPlazos.isEmpty();
    }

    /**
     * VMM 26/09/26 MD-3067: indica si la revisión documental (administrativa) ya está en Revisión OK -
     * el tercer requisito, además de presupuesto/plazos cargados, para poder "Finalizar revisión".
     */
    public Boolean esRevisionDocumentalOk() {
        // VMM 29/09/26 MD-3067: si aun no hay ninguna modificacion seleccionada (p.ej. al entrar en la
        // solapa antes de pulsar "Ver detalle" en alguna fila del listado), no tiene sentido mostrar el
        // aviso de revision documental pendiente, ya que no se refiere a ninguna modificacion concreta -
        // igual que ya hacian existeModifOtraCargada/existeModifPresupuestariaCargada/existeModifPlazoCargada.
        // cargarInformacion() no deja selectExpedienteModifDTO a null sino a un DTO vacio (sin
        // idExpedienteModifPresPlazoOtra) mientras no se pulse la lupa, asi que hay que comprobar
        // tambien ese campo y no solo la nulidad del propio DTO.
        if (this.selectExpedienteModifDTO == null
                || this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra() == null) {
            return Boolean.TRUE;
        }
        return this.selectExpedienteModifDTO.getEstadoRevAdminDTO() != null
                && this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor().equals(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK));
    }

    public Boolean habilitarFinalizarRevision() throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite finalizar revisión
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.FALSE;
        // VMM 26/09/26 MD-3067: se extraen a metodos propios (existeModifOtraCargada/
        // existeModifPresupuestariaCargada/existeModifPlazoCargada) para poder reutilizarlos tambien
        // desde el xhtml y mostrar un aviso junto a cada apartado que aun no se haya cargado.
        Boolean tieneOtras = this.existeModifOtraCargada();
        Boolean tienePresu = this.existeModifPresupuestariaCargada();
        Boolean tienePlazos = this.existeModifPlazoCargada();

         //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        /// y tenemos que meter tambien en el if que ver si hay alguna modificacion ya hecha
        // VMM 08/09/26 MD-2920: según la tarjeta corregida, "Finalizar revisión" se habilita solo con
        // la revisión administrativa en Revisión OK; no debe exigir fecha/tipo de resolución, ya que esos
        // campos permanecen deshabilitados (ver habilitarFechaResolucion) hasta que este botón deja la
        // modificación en PEDIENTE_MODIFICACIONES, así que exigirlos aquí la dejaba imposible de habilitar.
        if (this.selectExpedienteModifDTO != null
                && Boolean.TRUE.equals(this.esRevisionDocumentalOk())
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE))
                && tienePlazos.equals(Boolean.TRUE)
                && tieneOtras.equals(Boolean.TRUE)
                && tienePresu.equals(Boolean.TRUE)
                && !Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;

    }
    
    public void finalizarRevision() {
        try {

                this.selectExpedienteModifDTO.setEstadoDTO(this.catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES)));

                this.expedienteModifPresPlazoOtraService.guardar(this.selectExpedienteModifDTO);

                this.recargarInformacionManteniendoSeleccion();

                PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

                // VMM 26/09/26 MD-3062: faltaba el mensaje de exito - el cambio de estado se hacia
                // correctamente pero no se avisaba de ello ni en zona superior ni con ningun aviso.
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.FINALIZAR_REVISION_MODIF_CORRECTO);

      } catch (ServiceException e) {
          AtlasMessageUtils .addErrorMessageFromBundle("Error al Finalizar la revisión");
      }

  }
    
    
    
    
    public Boolean habilitarBotonDescargar() {

        Boolean habilitar = Boolean.FALSE;

        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PRESUPUESTO_MODIF));
        
        if (expeDocDTO != null && expeDocDTO.getCdFica() != null && this.selectExpedienteModifDTO != null ) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    
    public ExpedienteDocDTO getExpedienteDocDTO(Integer tipoDocumento) {

        ExpedienteDocDTO expeDocDTO = new ExpedienteDocDTO();

        try {

            List<AtlasFilter> filtroExpeDocProv = new ArrayList<>();
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                            tipoDocumento, Operador.EQUALS));
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            List<ExpedienteDocDTO> lstExpeDocDTO = this.expedienteDocService.buscar(0, -1,filtroExpeDocProv, ordenamientoHist);
                        
            if (lstExpeDocDTO != null && !lstExpeDocDTO.isEmpty() && lstExpeDocDTO.get(0) != null) {

                for (ExpedienteDocDTO expedienteDoccumentoDTO : lstExpeDocDTO) {
                    
                    List<AtlasFilter> filtroDocRelacion = new ArrayList<>();
                    filtroDocRelacion.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    filtroDocRelacion.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                            expedienteDoccumentoDTO.getIdExpedienteDoc(),Operador.EQUALS));
                    filtroDocRelacion.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                            this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(),Operador.EQUALS));
                    
                    List<ExpedienteModfPresupPlazosOtrosDocDTO> documentosModificacion = 
                            this.expedienteModfPresupPlazosOtrosDocService.buscar(0, -1,filtroDocRelacion, ordenamientoHist);
                    
                    if(!documentosModificacion.isEmpty()) {
                        expeDocDTO = expedienteDoccumentoDTO;
                        break;
                    }                   
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return expeDocDTO;

    }
    
    /**
     * VMM 22/09/26 MD-3063: el atlasp:calendario de fcResolucion no tiene ningun p:ajax (no expone forma de
     * añadirle uno - ver investigacion previa del mismo componente), asi que al borrar la fecha ya
     * escrita el servidor no se entera hasta el siguiente postback de otro control - el boton
     * "Subir resolucion" puede quedar pintado como habilitado con la fecha ya borrada. Repetimos aqui
     * la misma validacion que ya hace guardarDocumento() para este mismo caso, en vez de confiar solo
     * en el atributo disabled del boton.
     */
    public void subirResolucionDialog() {
        if (this.fcResolucion == null || this.estadoResolucionDTO == null
                || this.estadoResolucionDTO.getIdValor() == null) {
            AtlasMessageUtils.addErrorMessage("Seleccione una fecha de resolucion y un tipo de resolución");
            return;
        }
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucion");
    }

    /** 
     * 
     */
    public void cerrarSubirResolucionDialog() {
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucion");
    }
    
    /**
     * 
     * @param archivoResolucion
     * @param propuestaResolucion
     * @param tramite
     * @return
     */
    private ExpedienteDocDTO prepararMetadatosFica(UploadedFile archivoResolucion,
            Integer propuestaResolucion, Integer tramite) {

        if (Objects.nonNull(archivoResolucion)) {

            try {
                ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(propuestaResolucion);
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
                log.error(e.getMessage(), e);
            } catch (ServiceException e) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage(), e);
            }

        } else {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.info(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
        }

        return this.expedienteDocDTO;

    }
    
    /**
     * 
     */
    /**
     * VMM 07/10/26 MD-2905: descarga un Excel con los datos presupuestarios de la modificacion
     * seleccionada: una hoja por cada tabla de presupuesto de la pantalla (antes de la solicitud de
     * modificacion, modificaciones solicitadas y modificaciones aceptadas). El boton esta siempre
     * activo, junto al de Guardar.
     */
    public void descargarDatosPresupuestariosExcel() {
        try {
            boolean conConceptos = Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup);
            int numAnualidades = this.listaAnualidades != null ? this.listaAnualidades.size() : 0;

            List<ExportadorDatosPresupuestariosModif.Hoja> hojas = new ArrayList<>();
            hojas.add(new ExportadorDatosPresupuestariosModif.Hoja("Antes modificacion",
                    this.filasExcelAntesModificacion(conConceptos, numAnualidades), false));
            hojas.add(new ExportadorDatosPresupuestariosModif.Hoja("Modif solicitadas",
                    this.filasExcelSolicitadas(conConceptos, numAnualidades), false));
            hojas.add(new ExportadorDatosPresupuestariosModif.Hoja("Modif aceptadas",
                    this.filasExcelAceptadas(conConceptos, numAnualidades), true));

            byte[] excel = ExportadorDatosPresupuestariosModif.generar(conConceptos, numAnualidades, hojas);

            // el "Id Modificacion" que se ve en pantalla es idModificacion (correlativo por expediente), no
            // la clave interna idExpedienteModifPresPlazoOtra
            Integer idModif = this.selectExpedienteModifDTO != null
                    ? this.selectExpedienteModifDTO.getIdModificacion() : null;
            // el nombre lleva tambien el id del expediente, porque el id de modificacion se repite entre
            // expedientes (todos tienen su "modificacion 1")
            Integer idExp = this.expedienteBean != null ? this.expedienteBean.getIdExpediente() : null;
            UtilesWeb.openFile(excel, "xlsx", "DatosPresupuestarios_Modificacion"
                    + (idExp != null ? "_" + idExp : "") + (idModif != null ? "_" + idModif : ""));

        } catch (Exception e) {
            log.error("No se pudo generar el Excel de datos presupuestarios de la modificacion.", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /** MD-2905: filas de la tabla "Presupuestos antes de solicitud de modificacion" (ver xhtml, presupFinanciableAntesMod). */
    private List<ExportadorDatosPresupuestariosModif.Fila> filasExcelAntesModificacion(boolean conConceptos,
            int numAnualidades) {
        List<ExportadorDatosPresupuestariosModif.Fila> filas = new ArrayList<>();
        if (this.lstPresupuestoFinanciableAntesMod1 == null) {
            return filas;
        }
        for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciableAntesMod1) {
            if (presup == null || esFilaTotalPresupuesto(presup)) {
                continue;
            }
            // igual que la tabla: si hay montos modificados se muestra el importe concedido; si no, el del presupuesto
            List<ExpedienteModifPresupMontoDTO> montosModif = presup.getLstExpedienteModifPresupMontoDTO();
            List<ExpedientePresupMontoDTO> montos = presup.getLstExpedientePresupMontoDTO();
            boolean usaModif = montosModif != null && !montosModif.isEmpty();

            List<Double> importes = new ArrayList<>();
            for (int anualidad = 1; anualidad <= numAnualidades; anualidad++) {
                int indice = conConceptos ? anualidad : anualidad - 1;
                importes.add(usaModif ? importeConcedidoModif(montosModif, indice)
                        : importePresup(montos, indice));
            }
            Double total = usaModif ? importeConcedidoModif(montosModif, montosModif.size() - 1)
                    : importePresup(montos, montos != null ? montos.size() - 1 : -1);

            filas.add(new ExportadorDatosPresupuestariosModif.Fila(
                    presup.getTipoFinanciacionDTO() != null ? presup.getTipoFinanciacionDTO().getDsValor() : "",
                    presup.getTipoFinanciacionDTO() != null ? presup.getTipoFinanciacionDTO().getOrdenPartida() : null,
                    presup.getTlConcepto(), importes, total, null));
        }
        return filas;
    }

    /** MD-2905: filas de la tabla "Modificaciones presupuestarias solicitadas" (lstBloque[0].listaBloqueAuxDTO). */
    private List<ExportadorDatosPresupuestariosModif.Fila> filasExcelSolicitadas(boolean conConceptos,
            int numAnualidades) {
        List<ExportadorDatosPresupuestariosModif.Fila> filas = new ArrayList<>();
        if (this.lstBloque == null || this.lstBloque.isEmpty() || this.lstBloque.get(0) == null
                || this.lstBloque.get(0).getListaBloqueAuxDTO() == null) {
            return filas;
        }
        int indiceTotal = this.listaNcolPresupuesto != null ? this.listaNcolPresupuesto.size() - 1 : -1;

        for (BloqueComponenteDTO bloque : this.lstBloque.get(0).getListaBloqueAuxDTO()) {
            if (bloque == null || bloque.getTipoFinanciacionDTO() == null
                    || "TOTAL".equals(bloque.getTipoFinanciacionDTO().getDsValor())
                    || bloque.getDatos() == null) {
                continue;
            }
            List<DatoDTO> datos = bloque.getDatos();
            List<Double> importes = new ArrayList<>();
            for (int anualidad = 1; anualidad <= numAnualidades; anualidad++) {
                int indice = conConceptos ? anualidad : anualidad - 1;
                importes.add(importeDato(datos, indice));
            }
            filas.add(new ExportadorDatosPresupuestariosModif.Fila(
                    bloque.getTipoFinanciacionDTO().getDsValor(),
                    bloque.getTipoFinanciacionDTO().getOrdenPartida(),
                    conConceptos && !datos.isEmpty() && datos.get(0) != null ? datos.get(0).getDsValor() : "",
                    importes, importeDato(datos, indiceTotal), null));
        }
        return filas;
    }

    /** MD-2905: filas de la tabla "Modificaciones presupuestarias aceptadas" (lstPresupuestoFinanciableAcept1Modif). */
    private List<ExportadorDatosPresupuestariosModif.Fila> filasExcelAceptadas(boolean conConceptos,
            int numAnualidades) {
        List<ExportadorDatosPresupuestariosModif.Fila> filas = new ArrayList<>();
        if (this.lstPresupuestoFinanciableAcept1Modif == null) {
            return filas;
        }
        for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciableAcept1Modif) {
            if (presup == null || esFilaTotalPresupuesto(presup)
                    || presup.getLstExpedienteModifPresupMontoDTO() == null) {
                continue;
            }
            List<ExpedienteModifPresupMontoDTO> montos = presup.getLstExpedienteModifPresupMontoDTO();
            List<Double> importes = new ArrayList<>();
            for (int anualidad = 1; anualidad <= numAnualidades; anualidad++) {
                int indice = conConceptos ? anualidad : anualidad - 1;
                importes.add(importeModif(montos, indice));
            }
            filas.add(new ExportadorDatosPresupuestariosModif.Fila(
                    presup.getTipoFinanciacionDTO() != null ? presup.getTipoFinanciacionDTO().getDsValor() : "",
                    presup.getTipoFinanciacionDTO() != null ? presup.getTipoFinanciacionDTO().getOrdenPartida() : null,
                    presup.getTlConcepto(), importes, importeModif(montos, montos.size() - 1),
                    this.obtenerObservacionFila(presup)));
        }
        return filas;
    }

    private boolean esFilaTotalPresupuesto(ExpedientePresupDTO presup) {
        return presup.getTipoFinanciacionDTO() == null
                || "TOTAL".equalsIgnoreCase(presup.getTipoFinanciacionDTO().getDsValor());
    }

    private Double importeConcedidoModif(List<ExpedienteModifPresupMontoDTO> montos, int indice) {
        return montos != null && indice >= 0 && indice < montos.size() && montos.get(indice) != null
                ? montos.get(indice).getNmImporteConcedido() : null;
    }

    private Double importeModif(List<ExpedienteModifPresupMontoDTO> montos, int indice) {
        return montos != null && indice >= 0 && indice < montos.size() && montos.get(indice) != null
                ? montos.get(indice).getNmImporte() : null;
    }

    private Double importePresup(List<ExpedientePresupMontoDTO> montos, int indice) {
        return montos != null && indice >= 0 && indice < montos.size() && montos.get(indice) != null
                ? montos.get(indice).getNmImporte() : null;
    }

    /** Los importes de la tabla de solicitadas se guardan como texto en DatoDTO.dsValor. */
    private Double importeDato(List<DatoDTO> datos, int indice) {
        if (datos == null || indice < 0 || indice >= datos.size() || datos.get(indice) == null
                || StringUtils.isBlank(datos.get(indice).getDsValor())) {
            return null;
        }
        String valor = datos.get(indice).getDsValor().trim();
        try {
            return Double.valueOf(valor);
        } catch (NumberFormatException e) {
            try {
                return Double.valueOf(valor.replace(".", "").replace(",", "."));
            } catch (NumberFormatException e2) {
                return null;
            }
        }
    }

    public void descargaFicheroResolucion() {

        try {

            ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PRESUPUESTO_MODIF));
            
            
            if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                        FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage(), e);

        }

    }

    public void mostrarSubsanarDocumento(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        this.documentoSeleccionado = documento;
        if (this.documentoSeleccionado != null) {
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoActa').show();");
        }
    }

    public void inicializarFormularioSubsanacion() {

        this.idCausaSubsanacion = null;
        this.descripcionCausaSubsanacion = null;
        this.documentoSeleccionado = null;
        this.modoLecturaSubsanacion = Boolean.FALSE;
        this.descripcionCausaObligatoria = Boolean.FALSE;

    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     * @throws ServiceException 
     */
    public boolean habilitarBotonSubsanarDocumento(Integer idEstadoConvocatoria,
            ExpedienteDocDTO expDocDto) throws ServiceException {

        Boolean habilitar = Boolean.TRUE;

        // VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        // VMM 08/09/26 MD-2996 (reabierta): también si el expediente está Rechazado Definitivo
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO()) || this.isExpedienteRechazadoDefinitivo()) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }
        
        if (this.convocatoriaDTO == null) {
          this.convocatoriaDTO = convocatoriaService.buscarId(this.expedienteBean.getIdConvocatoria());
        }

        if( this.convocatoriaDTO != null && this.convocatoriaDTO.getItActasComitesGestion() != null 
                &&  !Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItActasComitesGestion())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        if (Boolean.FALSE.equals(this.habilitarCalificacionActas())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || Objects.isNull(expDocDto.getCdFica())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion actas
     *
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException
     */
    public boolean habilitarBotonEnviarSubanasarPortafirmasActas(Integer idEstadoConvocatoria)
            throws ServiceException {
        Boolean habilitar = Boolean.TRUE;
        
        //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        List<ExpedienteDocDTO> lstExpDocActas = this.getLstDocumentosModif();

        if (lstExpDocActas != null && !lstExpDocActas.isEmpty()) {

            // SUBSANACION
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Boolean existeRequiereSubsanacion = lstExpDocActas.stream()
                    .anyMatch(f -> idReqSub.equals(f.getEstadoDTO().getIdValor()));

            // Validamos si hay documentos pendientes de revision
            Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            Boolean existenPendientesRevision = lstExpDocActas.stream()
                    .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));

            // Lista de estados finales donde no se permite enviar a portafirmas
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));

            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                    && !lstEstadoExpInter.contains(dto.getEstadoDTO().getIdValor())
                    && Boolean.TRUE.equals(existeRequiereSubsanacion)
                    && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                    && Boolean.FALSE.equals(existenPendientesRevision)
                    && this.selectExpedienteModifDTO.getEstadoRevAdminDTO() != null
                    && this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor() != null
                    && !this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_REVISION_SUBSANACION))
                    && !this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REQUIERE_SUBSANACION))) {
                habilitar = Boolean.FALSE;
            }
        } else {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    public List<SelectItem> listaCausasSubsanacion(Integer idConvocatoria) {
        List<SelectItem> listaCausas = new ArrayList<>();
        try {

            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ConvocatoriaCauDTO> listaConvCau = this.convocatoriaCauService
                        .buscar(0, -1, filtrosEntrada, null);

                if (listaConvCau != null && !listaConvCau.isEmpty()) {
                    listaConvCau.stream()
                            .forEach(convcau -> listaCausas
                                    .add(new SelectItem(convcau.getCausaDTO().getIdValor(),
                                            convcau.getCausaDTO().getDsValor())));
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
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

                ConvocatoriaCauDTO cauCata = this.convocatoriaCauService
                        .buscarIdConvocatoriaCauDTO(this.expedienteBean.getIdConvocatoria(),
                                this.idCausaSubsanacion);

                if (cauCata != null && cauCata.getCausaDTO() != null
                        && cauCata.getCausaDTO().getCdCodigo()
                                .equals(Constantes.CausaDenegacion.CODIGO_OTROS)) {
                    this.descripcionCausaObligatoria = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR);
        }
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     * @throws ServiceException 
     */
    public boolean habilitarBotonRevertirCalificacionDocumento(Integer idEstadoConvocatoria,
            ExpedienteDocDTO expDocDto) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite revertir calificación
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean estadoPeriodo = Boolean.FALSE;
        Boolean estadoDocumentosubsanado = Boolean.FALSE;
        List<ExpedienteDocHistDTO> registrosAnteriores = null;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));

        List<AtlasFilter> filtroHist = new ArrayList<>();
        filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                expDocDto.getIdExpedienteDoc(), Operador.EQUALS));

        //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }
        
        int totalHistorico = 0;
        try {
            totalHistorico = this.expedienteDocHistService.buscar(0, -1, filtroHist, null).size();
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
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
            log.error(e.getMessage(), e);
        }

        List<Integer> lstEstadosPeriodos = new ArrayList<>();
        lstEstadosPeriodos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK));
        lstEstadosPeriodos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REQUIERE_SUBSANACION));

        if (this.selectExpedienteModifDTO != null
                && this.selectExpedienteModifDTO.getEstadoRevAdminDTO() != null
                && this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor() != null) {
            estadoPeriodo = lstEstadosPeriodos
                    .contains(this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor());

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_REVISION_SUBSANACION)
                    .equals(this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor())
                    && expDocDto.getEstadoDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO))) {
                estadoDocumentosubsanado = Boolean.TRUE;
            }

        }

        if (Boolean.FALSE.equals(this.habilitarCalificacionActas())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || Boolean.TRUE.equals(estadoPeriodo)
                || Boolean.TRUE.equals(estadoDocumentosubsanado)
                || Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionActasEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionActasFirmado)
                || totalHistorico <= 1 || registrosAnteriores == null
                || registrosAnteriores.isEmpty()) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    public void revertirEvaluacionDocumentoActas(ExpedienteDocDTO documento) {

        try {

            this.documentoSeleccionado = null;

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
                documento.setConvocatoriaCauDTO(null);
                documento.setIdCausa(null);
                documento.setTlCausaDescripcion(null);

                if (registroHistoricoEstadoAnterior.getCdFica() != null
                        && !registroHistoricoEstadoAnterior.getEstadoDTO().getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR))) {
                    documento.setCdFica(registroHistoricoEstadoAnterior.getCdFica());
                } else {
                    documento.setCdFica(null);
                }

                this.expedienteDocService.guardarConHistorico(documento);

                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
//        this.actualizarValoresFormulario();

    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarObservacionActas(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        if (StringUtils.isBlank(expDocDto.getTlObservaciones())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
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
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.SUBSANACION_BORRADOR_ACTA_COMITE_GESTION),
                    this.expedienteBean.getIdExpediente(), null);

            StringBuilder nombreArchivoPdf = new StringBuilder();
            nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
            nombreArchivoPdf.append("_");
            nombreArchivoPdf
                    .append("Requerimiento de Subsanación - Solicitud - Revisión Borrador Actas");
            nombreArchivoPdf.append("_");
            nombreArchivoPdf.append("BORRADOR");

            UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * Metodo para calcular si existe un envío de subsanación a portafirmas
     *
     * @param dto
     */
    private void calcularExisteEnvioPortafirmas(ExpedienteDTO dto) {
        this.setExisteEnvioPtfrSubsanacionActasEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrSubsanacionActasFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistimientoEnProceso(Boolean.FALSE);

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

            List<DocumentoPtfrDTO> listaEnvios = this.documentoPtfrService
                    .buscarPorExpediente(dto.getIdExpediente());

            for (DocumentoPtfrDTO doc : listaEnvios) {
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(doc.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrDesistimientoEnProceso(Boolean.TRUE);
                }
            }

            if (!lstExpDoc.isEmpty()) {

                // Es el mismo registro de envío para todos los documentos
                // observados o desistidos
                this.documentoPtfrDTO = this.documentoPtfrService
                        .buscarPorId(lstExpDoc.get(0).getIdDocumentoPtfr());

                if (Objects.nonNull(this.documentoPtfrDTO.getTramiteDTO())) {

                    if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.SUBSANACION_BORRADOR_ACTA_COMITE_GESTION)
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrSubsanacionActasEnProceso(Boolean.TRUE);
                    } else if (Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.SUBSANACION_BORRADOR_ACTA_COMITE_GESTION)
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrSubsanacionActasFirmado(Boolean.TRUE);
                    }

                }
            }

        } catch (ServiceException se) {
            log.error(se.getMessage(), se);
        }
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @return
     */
    private boolean habilitarCalificacionActas() {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

        if (lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }

    /**
     * Método para aprobar un documento
     *
     * @param documento Datos del documento
     */
    public void aprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = documento;
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

            this.documentoSeleccionado.setEstadoDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));

            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonAprobarDocumento(Integer idEstadoConvocatoria,
            ExpedienteDocDTO expDocDto) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite calificar
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {
            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

                if (!lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }

            } else {

                // Lista de estados de documentos donde se permite calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

                if (Boolean.FALSE.equals(this.habilitarCalificacionActas())
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
                    && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

            // adicional
            Boolean valAdicional = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoDTO().getIdValor())
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

                if (lstEstados.contains(dto.getEstadoDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }

        return habilitar;
    }

    /**
     * Método para desaprobar un documento
     *
     * @param documento Datos del documento
     */
    public void desaprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = documento;
            this.documentoSeleccionado.setEstadoDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
//            documento.setEstadoDTO(new CatalogosValoresDTO(Constantes.EstadoDocumento.REVISION_KO));
            this.expedienteDocService.guardarConHistorico(documento);
//            this.actualizarValoresFormulario();

            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     * @throws ServiceException 
     */
    public boolean habilitarBotonDesaprobarDocumento(Integer idEstadoConvocatoria,
            ExpedienteDocDTO expDocDto) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite calificar
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
        
        if (this.convocatoriaDTO == null) {
          this.convocatoriaDTO = convocatoriaService.buscarId(this.expedienteBean.getIdConvocatoria());
      }

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {
            
            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
            if (!Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItActasComitesGestion())) {
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
            }
            
            
            if (Boolean.FALSE.equals(this.habilitarCalificacionActas())
                    || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                    || Objects.isNull(expDocDto.getCdFica())) {
                habilitar = Boolean.FALSE;
            }

        } else {

            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

            // principal
            Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

            // adicional
            Boolean valAdicional = Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())
                    && !Constantes.AfirmacionNegacion.S.equals(this.convocatoriaDTO.getItActasComitesGestion())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoDTO().getIdValor())
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

                if (lstEstados.contains(dto.getEstadoDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }

        return habilitar;
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
                PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosActas').show();");
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }

    }

    public void mostrarHistoricoDocumentoObservaciones(String observaciones) {
        this.tlObservaciones = observaciones;
        if (this.tlObservaciones != null) {
            PrimeFaces.current()
                    .executeScript("PF('dialogHistoricoDocumentosObservacionesActas').show();");
        }
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
                        this.documentoSeleccionado.getExpedienteDTO().getIdExpediente(),
                        Operador.EQUALS));

                if (null == orderBean) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> ordenamientoHist = (List<AtlasSort>) orderBean;
                ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

                List<ExpedienteDocHistDTO> listado = this.expedienteDocHistService.buscar(index,
                        pageSize, filtrosEntrada, ordenamientoHist);

                // service

                for (ExpedienteDocHistDTO expedienteDocHistDTO : listado) {
                    if (expedienteDocHistDTO.getIdCausa() != null) {

                        CatalogosValores ccata = this.catalogosValoresService
                                .find(expedienteDocHistDTO.getIdCausa());
                        if (ccata != null) {
                            expedienteDocHistDTO.setCausaCatalogoTexto(ccata.getDsValor());
                        }

                    }
                }

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
     * Método para cerrar la ventana popup de observaciones
     */
    public void cerrarObservaciones() {
        this.tlObservaciones = null;
        PrimeFaces.current()
                .executeScript("PF('dialogHistoricoDocumentosObservacionesActas').hide();");

        // Si el objeto documentoSeleccionado es nulo, es que el popup de
        // observaciones no se ha llamado desde el popup de historico.
        // y no entra en el flujo del if. Caso contrario, el popup observaciones
        // se ha llamado desde el popup historico, y se volverá a abrir el popup
        // historico, despues de cerrar el de observaciones.
        // VMM 16/10/26 MD-3151: documentoSeleccionado se inicializa a un ExpedienteDocDTO vacio (new, sin id) al
        // cargar la solapa, asi que en una sesion nueva (sin haber abierto antes ningun historico) no era null y
        // se llamaba a mostrarHistoricoDocumento con idExpedienteDoc null -> buscarId(null) lanzaba
        // IllegalArgumentException "id to load is required for loading" y sacaba del programa.
        if (null != this.documentoSeleccionado && this.documentoSeleccionado.getIdExpedienteDoc() != null) {
            this.mostrarHistoricoDocumento(this.documentoSeleccionado);
        }
    }

    public void enviarColaNotificaciones(ConvocatoriaDTO convDTO) {
        try {

            // Recuperamos los datos del expediente
            ExpedienteDTO expDTO = this.expedienteService
                    .buscarIdSimple(this.expedienteBean.getIdExpediente(), Boolean.FALSE);

            // Validamos si el expediente esta en estado pendiente requerimiento
            if (this.selectExpedienteModifDTO == null
                    || this.selectExpedienteModifDTO.getEstadoRevAdminDTO() == null
                    || this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor() == null) {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_DOCUMENTO_FALTA_ACTUALIZAR_ESTADO_INTERNO);
            } else {

                // Quitamos los registros de baja
                List<ExpedienteDocDTO> lstExpDoc = this.lstDocumentosModif.stream()
                        .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

                // Obtenemos la lista de documentos asociados a los
                // requerimientos
                List<Integer> lstDocReq = expDTO.getLstExpedienteReqDTO().stream()
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

                    // insertamos a la tabla SAII_DOCUMENTO_PTFR
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO,
                            expDTO, this.idExpediente,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA), null, null,
                            null,null);

                    if (insertado.equals(Boolean.TRUE)) {
//                        this.actualizarValoresFormulario();
                        List<ExpedienteDoc> listaExpedienteDoc = documentoPtfrService
                                .obtenerDocumentosAReportar(
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA),
                                        expDTO.getIdExpediente(), null,null);
                        Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
                        listaExpedienteDoc = listaExpedienteDoc.stream()
                                .filter(f -> f.getEstado().getIdValor()
                                        .equals(idReqSub))
                                .collect(Collectors.toList());
                        for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                            expedienteDoc.setEstado(new CatalogosValores(
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                            // this.expedienteBean.getExpedienteDTO().setBAvisoDocumento(false);
                            this.expedienteDocService.update(expedienteDoc);
                            this.expedienteDocHistService.crearHistoricoExpedienteDoc(expedienteDoc,
                                    null);
                            for (ExpedienteDocDTO expedienteDocuDTO : this.lstDocumentosModif) {
                                if (expedienteDoc.getIdExpedienteDoc()
                                        .equals(expedienteDocuDTO.getIdExpedienteDoc())) {
                                    expedienteDocuDTO.getEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
                                    expedienteDocuDTO.getEstadoDTO().setNmConstante(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
                                }
                            }
                        }
                        this.documentoSeleccionado.setEstadoDTO(this.catalogosValoresService
                                .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        
                        this.expedienteService.actualizarEstadoInternoActasRevision(expDTO.getIdExpediente(), 
                                this.selectExpedienteModifDTO, 
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REQUIERE_SUBSANACION));
                        
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                        PrimeFaces.current().ajax().update(this.solapaModificacionPPO);
                    } else {
                        AtlasMessageUtils
                                .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
                    }

                }

            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }

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
    public boolean habilitarBotonFinalizarRevisionActas(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite finalizar revisión
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.FALSE;
        Boolean pendienteRevisadoOk = Boolean.FALSE;

        //VMM 06/05/26 MD-2695: capamos este boton si es modo consulta
        if (Boolean.TRUE.equals(this.getModoConsultaModifPPO())) {
            habilitar = Boolean.FALSE;
            return habilitar;
        }
        
       // ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.getConvocatoriaDTO().getIdConvocatoria());

        List<ExpedienteDocDTO> listaDocs = this.lstDocumentosModif;
        
        Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
        Integer idRevKo = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO);
        Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        
        pendienteRevisadoOk = listaDocs.stream()
                .allMatch(f -> (( idRevOk.equals(f.getEstadoDTO().getIdValor()) 
                        || idRevKo.equals(f.getEstadoDTO().getIdValor()))
                        || ( idSinSub.equals(f.getEstadoDTO().getIdValor()))
                                && ((f.getBloqueComponenteDTO() != null
                                        && !f.getBloqueComponenteDTO().getItObligatorio())
                                        || f.getBloqueComponenteDTO() == null)));


        // Lista de estados finales donde no se permite finalizar revisión
        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));

        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                || Constantes.Convocatoria.RESOL_INDIV
                .equals(conv.getTipoResolModalidadDTO().getCdTipoResolucion()))
                && Boolean.TRUE.equals(pendienteRevisadoOk)
           //     && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && this.selectExpedienteModifDTO != null
                && (this.selectExpedienteModifDTO != null &&  this.selectExpedienteModifDTO.getEstadoDTO() != null &&  this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null &&
                        !this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.INICIADA)))
                && (this.selectExpedienteModifDTO.getEstadoRevAdminDTO() == null
                || (this.selectExpedienteModifDTO.getEstadoRevAdminDTO() != null
                    && !this.selectExpedienteModifDTO.getEstadoRevAdminDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK))))) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

    /**
     * Método que finaliza la revisión del expediente cuando todos los
     * documentos administrativos estan con estado REVISION OK
     */
    public void finalizarRevisionOk() {

        try {

            

            List<ExpedienteDocDTO> listaDocs = this.lstDocumentosModif;

            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoInternoRevisionOkDocumentoActas(this.idExpediente, listaDocs,
                            this.selectExpedienteModifDTO);

            ExpedienteModifPresPlazoOtraDTO expedienteModifModificado = this.expedienteModifPresPlazoOtraService.buscarId( this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());

            if (dto != null && expedienteModifModificado != null && expedienteModifModificado.getEstadoRevAdminDTO() != null
                    && (expedienteModifModificado.getEstadoRevAdminDTO().getIdValor() != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK).equals(expedienteModifModificado.getEstadoRevAdminDTO().getIdValor())
                     || expedienteModifModificado.getEstadoRevAdminDTO().getIdValor() != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_KO).equals(expedienteModifModificado.getEstadoRevAdminDTO().getIdValor()))) {
                // this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
            }

            this.recargarInformacionManteniendoSeleccion();

            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
        }

    }

    public Boolean habilitarEnviarColaPortafirmasProvisional(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite enviar a cola
        // portafirmas (aquí "habilitar" true == disabled, se devuelve tal cual)
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.TRUE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean tieneFica = Boolean.FALSE;
        Boolean tieneEstadosDoc = Boolean.FALSE;
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
        
        // VMM 08/09/26 MD-2920: se quita el atajo que devolvía "habilitado" en cuanto
        // this.modoConsultaModifPPO era true: ese campo se marca a true también en cuanto la
        // modificación deja de estar "PEDIENTE" (ver esModoConsulta), que es precisamente el
        // estado normal en este punto del flujo (tras Finalizar Revisión); el atajo se saltaba
        // por completo la comprobación de tieneFica/tieneEstadoExpediente de más abajo y
        // habilitaba "Enviar a la cola portafirmas" sin haberse subido ninguna resolución.
        // El parámetro esModoConsulta (el flag real de solo lectura) ya se usa correctamente
        // en la condición principal, así que no hace falta ningún atajo aquí.
        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PRESUPUESTO_MODIF));

        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            tieneFica = Boolean.TRUE;
        }

        if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null && expeDocDTO.getEstadoDTO().getIdValor() != null) {
            tieneEstadosDoc = lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor());
        }

        Boolean tieneEstadoExpediente = Boolean.FALSE;
        List<Integer> lstEstadosExpediente = new ArrayList<>();
        lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_ENVIO_COLA));

        if ( this.selectExpedienteModifDTO != null  && lstEstadosExpediente.contains(this.selectExpedienteModifDTO.getEstadoDTO().getIdValor())) {
            tieneEstadoExpediente = Boolean.TRUE;
        }
           
        if (Boolean.FALSE.equals(esModoConsulta) && Boolean.TRUE.equals(tieneFica) && this.selectExpedienteModifDTO != null  &&  this.selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor() !=  null
                && !this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_PORTAFIRMAS))
                && Boolean.FALSE.equals(tieneEstadosDoc)
                && Boolean.TRUE.equals(tieneEstadoExpediente)
                // VMM 06/10/26 MD-3110: no se permite enviar de nuevo si ya esta en la cola
                && !this.resolucionYaEnCola()) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    /**
     * 
     */
    public void enviarColaPortafirmasResolucion() {

        try {

            Integer tramite = 0;
            
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = convocatoriaService
                        .buscarId(this.expedienteBean.getIdConvocatoria());
            }

            
//            if(this.selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteTipoResolucion.RECHAZADA))) {
//                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_RECHAZADA);
//            }else if(this.selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteTipoResolucion.ACEPTADA))) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA);
//            }else if(this.selectExpedienteModifDTO.getEstadoResolucionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteTipoResolucion.ACEPTADA_PARCIALMENTE))) {
//                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA_PARCIAL);
//            }
            


            if (Objects.nonNull(this.expedienteBean.getExpedienteDTO().getIdExpediente())) {
                
                ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_PRESUPUESTO_MODIF));
                
                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                    
                    FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(this.convocatoriaDTO, this.expedienteBean.getExpedienteDTO(),
                            this.expedienteBean.getExpedienteDTO().getIdExpediente(), tramite,
                            ficaFichero.getDatosFichero(), null, this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra(),null);
                    
                    if (insertado.equals(Boolean.TRUE)) {
                        
                        expeDocDTO.setEstadoDTO(this.catalogosValoresService.buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(expeDocDTO,UserUtils.getUserName().toUpperCase());
                        
                        this.selectExpedienteModifDTO.setEstadoDTO(this.catalogosValoresService.
                                buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PENDIENTE_PORTAFIRMAS))); 
                        this.expedienteModifPresPlazoOtraService.guardar(this.selectExpedienteModifDTO);
                        this.recargarInformacionManteniendoSeleccion();

                        AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                    } else {
                        log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                        AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                    }
                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                    AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                }
            }

            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }

    }
    
    /**
     * Método auxiliar para formatear las fechas de formato IS
     */
    public String formatFecha(String isoDate) {
        // Convierte la cadena ISO en Instant
	if("".equals(isoDate)) {
            return isoDate;
        }else if(isoDate == null) {
            return "";
        }
        Instant instant = Instant.parse(isoDate); 
        
        // Ajusta a la zona horaria local 
        ZoneId zone = ZoneId.of("Europe/Madrid"); //ZoneId.systemDefault(); //este default tb esta sumando un dia de mas
       
        // VMM 16/10/26 MD-3150: el dato se guarda como instante UTC (p.ej. 2027-03-15T23:00:00.000Z para el
        // 16/03/2027 00:00 hora de Madrid), y atZone(Europe/Madrid) ya lo pasa a la fecha correcta. Antes se
        // restaba ademas un dia cuando la hora salia 00:00 ("el dia que esta sumando de mas"), con lo que la
        // fecha solicitada se veia un dia antes (15/03 en vez de 16/03).
        LocalDateTime localDateTime = instant.atZone(zone).toLocalDateTime();
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * VMM 16/09/26: en la tabla de "Modificaciones de Plazos Solicitadas" solo se solicita la
     * fecha fin de ejecucion (la fecha inicio no es editable, no se pide), asi que datos suele
     * traer un unico elemento. No hay que asumir un indice fijo (ej. datos[1]) porque si solo hay
     * un dato, va en el indice 0 y un indice fijo lo deja fuera de rango (se ve en blanco aunque el
     * dato exista en BD). Cogemos siempre el ultimo elemento de la lista.
     */
    public String obtenerFechaFinPlazoSolicitado(BloqueComponenteDTO plazoSolic) {
        if (plazoSolic == null || plazoSolic.getDatos() == null || plazoSolic.getDatos().isEmpty()) {
            return "";
        }
        DatoDTO ultimoDato = plazoSolic.getDatos().get(plazoSolic.getDatos().size() - 1);
        return this.formatFecha(ultimoDato.getDsValor());
    }

    public void cargarDatosPresupuestoModif() {
        try {

            Integer nmPresup = 1;

            this.lstPresupuestoFinanciable = new ArrayList<>();

            for (BloqueDTO bloques : this.lstBloque) {
                List<BloqueComponenteDTO> bloquesSinTotal = bloques.getListaBloqueAuxDTO().stream()
                        .filter(bloque -> Objects
                                .nonNull(bloque.getTipoFinanciacionDTO().getIdValor()))
                        .collect(Collectors.toList());

                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                    this.expedienteModifPresupMontoService.guardarModifPresupConcepto(bloquesSinTotal,
                            nmPresup, this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                }else {
                    this.expedienteModifPresupMontoService.guardarModifPresup(bloquesSinTotal,
                            nmPresup, this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                }
                
                nmPresup++;
            }
     
            this.obtenerPresupuestoFinanciable(1);
             
            this.validarEstilo();
                       
            this.esconderBotonCargarPresup();
            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }
    
    
    /**
     * Metodo para copiar los datos de plazos de anualidades antes de la modificación (concedidos) a la tabla de plazos
     * de anualidades después de la modificación (a aceptar) [Boton de cargar datos], esto solo se hace la primera vez 
     * que entra el gestor a la modificación seleccionada
     */
    public void cargarDatosPlazos() {
        try {    
                
           List<ExpedienteModifPlazoDTO> lstPlazosModif = this.lstPlazosAntesMod;
            List<ExpedienteModifPlazoDTO> lstModifPlazosAnualidadesAux = new ArrayList<>();
                      
            if (!lstPlazosModif.isEmpty()) {
                for (ExpedienteModifPlazoDTO expedientePlazo: lstPlazosModif) {
                    
                    ExpedienteModifPlazoDTO expedienteModifPlazoDTO = new ExpedienteModifPlazoDTO();
                    expedienteModifPlazoDTO.setIdExpedienteModifPresPlazoOtra
                        (this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
                    expedienteModifPlazoDTO.setIdExpedienteAnualidad(expedientePlazo.getIdExpedienteAnualidad());
                    expedienteModifPlazoDTO.setTipoExpedienteAnualidadDTO(new CatalogosValoresDTO
                            (expedientePlazo.getTipoExpedienteAnualidadDTO().getIdValor()));
                    expedienteModifPlazoDTO.setNmAnualidad(expedientePlazo.getNmAnualidad());
                    expedienteModifPlazoDTO.setFcIniEjecucion(expedientePlazo.getFcIniEjecucion());
                    expedienteModifPlazoDTO.setFcIniEjecConcedido(expedientePlazo.getFcIniEjecucion());
                   
                    
                    //estas fechas hay que traerlas de saiiDato
                  List<BloqueComponenteDTO> plazosAceptados =   this.lstBloquePlazos.get(0).getListaBloqueAuxDTO();
                    
                  if(!plazosAceptados.isEmpty() && plazosAceptados.get(0) != null && plazosAceptados.get(0).getDatos() != null  && plazosAceptados.get(0).getDatos().get(0) != null
                          && plazosAceptados.get(0).getDatos().get(0).getDsValor() != null) {
                     
                      String literal = plazosAceptados.get(0).getDatos().get(0).getDsValor().substring(0, 19); 
                      LocalDateTime ldt = LocalDateTime.parse(literal);

                      // VMM 16/10/26 MD-3150: el literal (sin la Z) es la hora UTC del instante guardado en SAII_DATO,
                      // no una hora local: con atZone(systemDefault) 2027-03-14T23:00 quedaba como las 23:00
                      // locales del dia 14 y se veia 14/03/2027 23:00 en vez de 15/03/2027 00:00 (Madrid)
                      Date fechaFin = Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());
                      
                      expedienteModifPlazoDTO.setFcFinEjecucion(fechaFin); 
                  }else {
                      expedienteModifPlazoDTO.setFcFinEjecucion(expedientePlazo.getFcFinEjecucion());  
                  }
                  
                  expedienteModifPlazoDTO.setFcFinEjecConcedido(expedientePlazo.getFcFinEjecucion());
                    //insertamos por primera vez para guardar los plazos iniciales concedidos por el gestor
                  expedienteModifPlazoDTO = this.expedienteModifPlazoService.guardar(expedienteModifPlazoDTO);
                    
                    lstModifPlazosAnualidadesAux.add(expedienteModifPlazoDTO);
                }
            }

            //obtenemos los plazos de antes de la modificacion de la tabla temp de plazos
            //para copiarlos a la tabla de plazos a aceptar
            this.lstPlazosAcept = lstModifPlazosAnualidadesAux;
            // VMM 16/10/26 MD-3150: la tabla "plazos antes de solicitud" muestra fcFinEjecConcedido cuando ya hay
            // plazos aceptados, pero lstPlazosAntesMod venia de convertirExpAnualToExpModifPlazo (solo informa
            // fcFinEjecucion), asi que tras "Cargar datos" salia en blanco hasta recargar. Al recargar ya se usa la
            // misma lista que los aceptados (ver obtenerPlazo), asi que se hace igual aqui.
            this.lstPlazosAntesMod = lstModifPlazosAnualidadesAux;

            this.validarEstilo();

            this.esconderBotonCargarPlazos();
            PrimeFaces.current().ajax().update(this.solapaModificacionPPO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    
    /**
     * Metodo para ocultar el boton de carga de presupuestos financiables
     */
    private void esconderBotonCargarPresup() {
        boolean esModifResuelta = false;
        
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        
        //comprobamos si la modificacion está en estado resuelta
        if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getEstadoDTO() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().
                equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.RESUELTA))) {
            esModifResuelta = true;
        }

        // VMM 14/09/26 MD-2725: era lstPresupuestoFinanciableAcept1 (siempre vacio en esta pantalla,
        // ver el mismo fix en modificarTotales/edicionCeldaPresupuesto) - esta condicion nunca ocultaba
        // el boton por "datos ya cargados", permitiendo pulsar Cargar Datos varias veces y duplicar
        // los registros insertados en saii_expe_mod_pre_monto.
        this.esconderBotonCargarDatoPresup = lstEstadoExpediente.contains(this.idEstadoExpediente)
                || esModifResuelta
                || (this.lstPresupuestoFinanciableAcept1Modif != null && !this.lstPresupuestoFinanciableAcept1Modif.isEmpty())
                || !this.lstPresupuestoFinanciableAcept2.isEmpty();

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgCargarDatosModifPresup");

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelModifPresPlazoOtra");
    }
    
    /**
     * Metodo para ocultar el boton de carga de plazos anualidades
     */
    private void esconderBotonCargarPlazos() {
        boolean esModifResuelta = false;
        
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        
        //comprobamos si la modificacion está en estado resuelta
        if (this.selectExpedienteModifDTO != null && this.selectExpedienteModifDTO.getEstadoDTO() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor() != null
                && this.selectExpedienteModifDTO.getEstadoDTO().getIdValor().
                equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.RESUELTA))) {
            esModifResuelta = true;
        }

        this.esconderBotonCargarDatoPlazos = lstEstadoExpediente.contains(this.idEstadoExpediente)
                || esModifResuelta
                || !this.lstPlazosAcept.isEmpty();

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgCargarDatosModifPLazos");

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelModifPresPlazoOtra");
    }
    
    //VMM 22/04/25 MD-1484: metodo para mostrar o no las tablas de ayuda autocaalculadas en base a la intensidad
    //de momento no se usa esto porque se han usado los dos metodos de debajo de este igual que en financiacion pero habrá que verificar
    //si usamos este metodo mas generico o el otro de isMostrarTablaAyudaFinan a ver si hay que meter la instensidad del gestor....
    public boolean isMostrarTablaAyudaModif(Integer nmPresup) throws ServiceException {
        boolean esMostrarAyuda = true;
        FinanciacionDTO financiacionDTO = null;
        
        if (nmPresup == 1 && this.lstPresupuestoFinanciable != null && !this.lstPresupuestoFinanciable.isEmpty()) {
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                this.lstPresupuestoFinanciable.get(0).getTematicaDTO().getIdValor(), this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
        }else {
            /*if (nmPresup == 2 && this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty()) {
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                    this.lstPresupuestoFinanciable1.get(0).getTematicaDTO().getIdValor(), this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());*/
        }
        if (financiacionDTO != null && !Constantes.AfirmacionNegacion.S.equals(financiacionDTO.getItMostrarAyuda())) {
            esMostrarAyuda = false;
        }
        
        return porcentajeIntensidadAyuda != null && porcentajeIntensidadAyuda != 100 && esMostrarAyuda;
    }
    
    
    public boolean isMostrarTablaAyuda(Integer nmPresup) throws ServiceException {
        boolean esMostrarAyuda = true;
        FinanciacionDTO financiacionDTO = null;
        
        Integer idTematica = this.lstBloque != null
                && this.lstBloque.get(nmPresup-1) != null
                && this.lstBloque.get(nmPresup-1).getIdTematica() != null
                && this.lstBloque.get(nmPresup-1).getIdTematica().getIdValor() != null
                        ? this.lstBloque.get(nmPresup-1).getIdTematica().getIdValor()
                        : null;
        
        // mostrar tabla de ayuda o no en caso de presup solicitados sin haber cargado al expedte
       /* if (nmPresup == 1 && this.lstBloque != null && !this.lstBloque.isEmpty()){
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                    idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
            //mostrar tabla de ayuda 2 si corresponde
        }else if (nmPresup == 2 && this.lstBloque != null && !this.lstBloque.isEmpty() && this.lstBloque.get(1) != null){
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                    idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
        }*/
        
        financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
        
        if (financiacionDTO != null && !Constantes.AfirmacionNegacion.S.equals(financiacionDTO.getItMostrarAyuda())) {
            esMostrarAyuda = false;
        }
        
        return porcentajeIntensidadAyuda != null && porcentajeIntensidadAyuda != 100 && esMostrarAyuda;
    }
    
    public boolean isMostrarTablaAyudaFinan(Integer nmPresup) throws ServiceException {
        boolean esMostrarAyuda = true;
        FinanciacionDTO financiacionDTO = null;
        Integer idTematica = null;

        //ponemos las tablas de presupuestos en base a su numeracion correcta
        if (nmPresup == 1 && this.lstPresupuestoFinanciableAntesMod1 != null && !this.lstPresupuestoFinanciableAntesMod1.isEmpty()) {
            idTematica = this.lstPresupuestoFinanciableAntesMod1.get(0) != null
                    && this.lstPresupuestoFinanciableAntesMod1.get(0).getTematicaDTO() != null
                            ? this.lstPresupuestoFinanciableAntesMod1.get(0).getTematicaDTO().getIdValor()
                            : null;
        }else if (nmPresup == 2 && this.lstPresupuestoFinanciableAntesMod2 != null && !this.lstPresupuestoFinanciableAntesMod2.isEmpty()) {
            idTematica = this.lstPresupuestoFinanciableAntesMod2.get(0) != null
                    && this.lstPresupuestoFinanciableAntesMod2.get(0).getTematicaDTO() != null
                            ? this.lstPresupuestoFinanciableAntesMod2.get(0).getTematicaDTO().getIdValor()
                            : null;
        }/*else if (nmPresup == 1 && this.lstBloque != null && !this.lstBloque.isEmpty()){
            idTematica = this.lstBloque.get(0).getIdTematica() != null
                    && this.lstBloque.get(0).getIdTematica().getIdValor() != null
                            ? this.lstBloque.get(0).getIdTematica().getIdValor()
                            : (this.lstBloque.get(0).getIdTematicaPadre() != null
                                    ? this.lstBloque.get(0).getIdTematicaPadre().getIdValor()
                                    : null);
            //VMM 01/06/26 MD-2731: mostrar tabla de ayuda 2 si corresponde
        }else if (nmPresup == 2 && this.lstBloque != null && !this.lstBloque.isEmpty() && this.lstBloque.get(1) != null){
            idTematica = this.lstBloque.get(1).getIdTematica() != null
                    && this.lstBloque.get(1).getIdTematica().getIdValor() != null
                            ? this.lstBloque.get(1).getIdTematica().getIdValor()
                            : (this.lstBloque.get(1).getIdTematicaPadre() != null
                                    ? this.lstBloque.get(1).getIdTematicaPadre().getIdValor()
                                    : null);
        }*/
        
        financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());

        if (financiacionDTO != null && !Constantes.AfirmacionNegacion.S.equals(financiacionDTO.getItMostrarAyuda())) {
            esMostrarAyuda = false;
        }

        return porcentajeIntensidadAyuda != null && porcentajeIntensidadAyuda != 100 && esMostrarAyuda;
    }
    
    
    /**
     * Método que descarga el documento de EREG
     *
     * @param expedienteDocDTO documento del expediente
     */
    public void descargarDocumentoEreg(ExpedienteDocDTO expedienteDocDTO) {
        try {
            //VMM 15/04/26 MD-2676: metodo para descargar recibop y pdf de presentacion de EREG
            //ExpedienteDTO expDTO = this.expedienteService.buscarIdMinimo(this.idExpediente);
            
            ExpedienteModifPresPlazoOtraDTO expDTO = this.expedienteModifPresPlazoOtraService.buscarId(this.selectExpedienteModifDTO.getIdExpedienteModifPresPlazoOtra());
            
            byte[] documento = this.eregAdmeService.descargarAnexoPorCodigo(
                    expDTO.getCdEregPresent(), expedienteDocDTO.getCdAnexoEreg());

            DateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA_DESCARGA);

            // Se prepara el fichero.
            UtilesWeb.openFile(documento, Constantes.Extensiones.PDF,
                    expedienteDocDTO.getCdAnexoEreg() + "_"
                            + sdf.format(expedienteDocDTO.getFcAltaFila()));
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO, ex);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }
    
    /**
     * Método para cerrar la ventana popup de históricos de estados de documento
     */
    public void cerrarHistoricoDocumento() {
        this.documentoSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosActas').hide();");
    }
    
    public void prepararObservacion(ExpedienteModifPresupMontoDTO seleccionado) {
        this.presupuestoSeleccionado = seleccionado;
    }

    public void guardarSoloObservacion() throws ServiceException {
               
        ExpedienteModifPresupMontoDTO entidad = expedienteModifPresupMontoService.buscarId(presupuestoSeleccionado.getIdExpedienteModifPresupMonto());      
        entidad.setCdObservaciones(presupuestoSeleccionado.getCdObservaciones());
        entidad.setCdUsuAlta(UserUtils.getUserName());
        // Aquí llamas a tu lógica de persistencia
        this.expedienteModifPresupMontoService.guardar(entidad);
        
        // El update de la tabla en el XHTML se encargará de refrescar los colores
    }
    /**
     * Calcula la suma vertical de todos los conceptos de una partida específica para una columna dada.
     */
//    public Double getSumaGrupo(CatalogosValoresDTO partida, int nCol, List<ExpedientePresupDTO> lstPresup) {
//        if (lstPresup == null || partida == null) {
//            return 0.0;
//        }
//        Double resultado;
//        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
//             resultado = lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
//                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        // nCol es el índice que viene del front (ya ajustado con el ternario)
//                        if (p.getLstExpedienteModifPresupMontoDTO() != null && nCol < p.getLstExpedienteModifPresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
//                            Double imp = p.getLstExpedienteModifPresupMontoDTO().get(nCol).getNmImporteConcedido();
//                            return (imp != null) ? imp : 0.0;
//                        }
//                        return 0.0;
//                    }).sum();
//        }else {
//             resultado = lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
//                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        // nCol es el índice que viene del front (ya ajustado con el ternario)
//                        if (p.getLstExpedientePresupMontoDTO() != null && nCol < p.getLstExpedientePresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
//                            Double imp = p.getLstExpedientePresupMontoDTO().get(nCol).getNmImporte();
//                            return (imp != null) ? imp : 0.0;
//                        }
//                        return 0.0;
//                    }).sum();
//        }
//        
//        return resultado;
//    }
    
    public Double getSumaGrupo(CatalogosValoresDTO partida, int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || lstPresup.isEmpty() || partida == null) {
            return 0.0;
        }
        
        // Obtenemos el número de anualidad real que corresponde a esa columna usando tu lista de anualidades del Bean
        Integer anualidadBuscada = null;
        if (this.listaAnualidades != null && nCol >= 0 && nCol < this.listaAnualidades.size()) {
            anualidadBuscada = this.listaAnualidades.get(nCol);
        }

        // Declaramos la variable final para que la Lambda la acepte sin quejarse
        final Integer anualidadFinal = anualidadBuscada;
        Double resultado;
        
        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
             resultado = lstPresup.stream()
                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                    .mapToDouble(p -> {
                        if (p.getLstExpedienteModifPresupMontoDTO() != null && p.getIdExpedientePresup() != null) {
                            
                            // Si encontramos la anualidad específica en la lista de montos, sumamos su valor beige
                            if (anualidadFinal != null) {
                                return p.getLstExpedienteModifPresupMontoDTO().stream()
                                        .filter(m -> m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                        .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
                                        .findFirst()
                                        .orElse(0.0);
                            }
                            
                            // Si nCol no corresponde a una anualidad (es el total horizontal), buscamos el objeto marcado como total
                            return p.getLstExpedienteModifPresupMontoDTO().stream()
                                    .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
                                    .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
                                    .findFirst()
                                    .orElse(0.0);
                        }
                        return 0.0;
                    }).sum();
        } else {
             resultado = lstPresup.stream()
                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                    .mapToDouble(p -> {
                        if (p.getLstExpedientePresupMontoDTO() != null && p.getIdExpedientePresup() != null) {
                            
                            if (anualidadFinal != null) {
                                return p.getLstExpedientePresupMontoDTO().stream()
                                        .filter(m -> m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                        .mapToDouble(ExpedientePresupMontoDTO::getNmImporte)
                                        .findFirst()
                                        .orElse(0.0);
                            }
                            
                            return p.getLstExpedientePresupMontoDTO().stream()
                                    .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
                                    .mapToDouble(ExpedientePresupMontoDTO::getNmImporte)
                                    .findFirst()
                                    .orElse(0.0);
                        }
                        return 0.0;
                    }).sum();
        }
        
        return resultado;
    }
    


    /**
     * Calcula el total acumulado de un grupo (Suma de todas sus anualidades).
     */
//    public Double getSumaTotalGrupo(CatalogosValoresDTO partida, List<ExpedientePresupDTO> lstPresup) {
//        // Si no hay ID de partida o no hay columnas, el total es 0
//        if (this.listaAnualidades == null || partida == null || lstPresup == null) {
//            return 0.0;
//        }
//        
//        double total = 0.0;
//        // Indice 0: Concepto (vacío), Indice 1: Anualidad 1, Indice 2: TOTAL.
//        int indexTotal = this.listaAnualidades.size() + 1;
//        
//        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
//            total = lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
//                                && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        if (p.getLstExpedienteModifPresupMontoDTO() != null && indexTotal < p.getLstExpedienteModifPresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
//                            Double imp = p.getLstExpedienteModifPresupMontoDTO().get(indexTotal).getNmImporteConcedido();
//                            return (imp != null) ? imp : 0.0;
//                        }
//                        return 0.0;
//                    }).sum();
//        }else {
//            total = lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
//                                && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        if (p.getLstExpedientePresupMontoDTO() != null && indexTotal < p.getLstExpedientePresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
//                            Double imp = p.getLstExpedientePresupMontoDTO().get(indexTotal).getNmImporte();
//                            return (imp != null) ? imp : 0.0;
//                        }
//                        return 0.0;
//                    }).sum();
//        }
//        
//        
//        return total;
//    }
    
    public Double getSumaTotalGrupo(CatalogosValoresDTO partida, List<ExpedientePresupDTO> lstPresup) {
        // Si no hay ID de partida o no hay columnas, el total es 0
        if (this.listaAnualidades == null || partida == null || lstPresup == null) {
            return 0.0;
        }
        
        double total = 0.0;
        
        // CORRECCIÓN CRÍTICA: El total horizontal añadido está exactamente en el índice correspondiente al tamaño de anualidades.
        // Ej: Si hay 1 anualidad (posición 0), el total se añade en la posición 1.
        int indexTotal = this.listaAnualidades.size(); 
        
        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
            total = lstPresup.stream()
                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                                && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                    .mapToDouble(p -> {
                        if (p.getLstExpedienteModifPresupMontoDTO() != null && indexTotal < p.getLstExpedienteModifPresupMontoDTO().size()
                                && p.getIdExpedientePresup() != null) { 
                            Double imp = p.getLstExpedienteModifPresupMontoDTO().get(indexTotal).getNmImporteConcedido();
                            return (imp != null) ? imp : 0.0;
                        }
                        return 0.0;
                    }).sum();
        } else {
            total = lstPresup.stream()
                    .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                                && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                    .mapToDouble(p -> {
                        if (p.getLstExpedientePresupMontoDTO() != null && indexTotal < p.getLstExpedientePresupMontoDTO().size()
                                && p.getIdExpedientePresup() != null) { 
                            Double imp = p.getLstExpedientePresupMontoDTO().get(indexTotal).getNmImporte();
                            return (imp != null) ? imp : 0.0;
                        }
                        return 0.0;
                    }).sum();
        }
        
        return total;
    }
    
    
//  Suma vertical de una columna (anualidad) específica para TODAS las partidas
//    public Double getGranTotalVertical(int nCol, List<ExpedientePresupDTO> lstPresup) {
//        if (lstPresup == null) return 0.0;
//        
//        Double resul;
//        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
//             resul = lstPresup.stream()
//                    .filter(p -> p.getLstExpedienteModifPresupMontoDTO() != null && nCol < p.getLstExpedienteModifPresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) //para que no coja el ultimo registro de la fila TOTAL
//                    .mapToDouble(p -> {
//                        Double imp = p.getLstExpedienteModifPresupMontoDTO().get(nCol).getNmImporteConcedido();
//                        return (imp != null) ? imp : 0.0;
//                    })
//                    .sum();
//                    
//        }else {
//             resul = lstPresup.stream()
//                    .filter(p -> p.getLstExpedientePresupMontoDTO() != null && nCol < p.getLstExpedientePresupMontoDTO().size()
//                                && p.getIdExpedientePresup() != null) //para que no coja el ultimo registro de la fila TOTAL
//                    .mapToDouble(p -> {
//                        Double imp = p.getLstExpedientePresupMontoDTO().get(nCol).getNmImporte();
//                        return (imp != null) ? imp : 0.0;
//                    })
//                    .sum();
//                    
//        }
//        
//        return resul;
//    }
    
    public Double getGranTotalVertical(int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || lstPresup.isEmpty()) {
            return 0.0;
        }
        
        // Identificamos qué anualidad real estamos sumando según el índice de columna
        Integer anualidadBuscada = null;
        if (this.listaAnualidades != null && nCol >= 0 && nCol < this.listaAnualidades.size()) {
            anualidadBuscada = this.listaAnualidades.get(nCol);
        }

        final Integer anualidadFinal = anualidadBuscada;
        Double resul;

        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
             resul = lstPresup.stream()
                    .filter(p -> p.getLstExpedienteModifPresupMontoDTO() != null && p.getIdExpedientePresup() != null) // Ignora la fila TOTALizadora de la lista
                    .mapToDouble(p -> {
                        if (anualidadFinal != null) {
                            // Buscamos el importe concedido de esta anualidad concreta (casillas beige)
                            return p.getLstExpedienteModifPresupMontoDTO().stream()
                                    .filter(m -> m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                    .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
                                    .findFirst()
                                    .orElse(0.0);
                        }
                        // Si nCol es el total horizontal, devuelve el objeto marcado como total
                        return p.getLstExpedienteModifPresupMontoDTO().stream()
                                .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
                                .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
                                .findFirst()
                                .orElse(0.0);
                    })
                    .sum();
                    
        } else {
             resul = lstPresup.stream()
                    .filter(p -> p.getLstExpedientePresupMontoDTO() != null && p.getIdExpedientePresup() != null)
                    .mapToDouble(p -> {
                        if (anualidadFinal != null) {
                            return p.getLstExpedientePresupMontoDTO().stream()
                                    .filter(m -> m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                    .mapToDouble(ExpedientePresupMontoDTO::getNmImporte)
                                    .findFirst()
                                    .orElse(0.0);
                        }
                        return p.getLstExpedientePresupMontoDTO().stream()
                                .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
                                .mapToDouble(ExpedientePresupMontoDTO::getNmImporte)
                                .findFirst()
                                .orElse(0.0);
                    })
                    .sum();
        }
        
        return resul;
    }
//    public Double getGranTotalVertical(int nCol, List<ExpedientePresupDTO> lstPresup) {
//        if (lstPresup == null || lstPresup.isEmpty()) {
//            return 0.0;
//        }
//
//        // Sumamos verticalmente la lista que están pintando los inputs beige en la pantalla
//        return lstPresup.stream()
//                .filter(p -> p.getIdExpedientePresup() != null) // Evitamos la fila de totales generales
//                .mapToDouble(p -> {
//                    if (p.getLstExpedientePresupMontoDTO() != null && nCol < p.getLstExpedientePresupMontoDTO().size()) {
//                        Double imp = p.getLstExpedientePresupMontoDTO().get(nCol).getNmImporte();
//                        return (imp != null) ? imp : 0.0;
//                    }
//                    return 0.0;
//                })
//                .sum();
//    }
//    
    
 // El Gran Total Final es simplemente la suma vertical de la columna de "TOTALES"
    public Double getGranTotalFinal(List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || this.listaAnualidades == null) {
            return 0.0;
        }
        
        // Si la anualidad es la 1 (índice 1) y el total es el índice 2...
        // El índice del total horizontal siempre es el tamaño de la lista de anualidades + (conConceptos? 1 : 0)
        int indexTotalHorizontal = this.listaAnualidades.size() + 1;  
        double granTotal = getGranTotalVertical(indexTotalHorizontal, lstPresup);
        
        return granTotal;
    }
    
//    public Double getSumaTotalGrupo(CatalogosValoresDTO partida, List<ExpedientePresupDTO> lstPresup) {
//        if (partida == null || lstPresup == null || lstPresup.isEmpty()) {
//            return 0.0;
//        }
//        
//        if (!lstPresup.get(0).getLstExpedienteModifPresupMontoDTO().isEmpty()) {
//            return lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && partida.getIdValor() != null
//                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        if (p.getLstExpedienteModifPresupMontoDTO() != null && p.getIdExpedientePresup() != null) {
//                            // Buscamos el objeto de la lista que tenga la bandera TRUE de total
//                            return p.getLstExpedienteModifPresupMontoDTO().stream()
//                                    .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
//                                    .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
//                                    .findFirst()
//                                    .orElse(0.0);
//                        }
//                        return 0.0;
//                    }).sum();
//        } else {
//            // ... misma lógica buscando por .getEsMontoTotal() para el nmImporte normal
//            return lstPresup.stream()
//                    .filter(p -> p.getTipoFinanciacionDTO() != null && partida.getIdValor() != null
//                                    && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
//                    .mapToDouble(p -> {
//                        if (p.getLstExpedientePresupMontoDTO() != null && p.getIdExpedientePresup() != null) {
//                            return p.getLstExpedientePresupMontoDTO().stream()
//                                    .filter(m -> Boolean.TRUE.equals(m.getEsMontoTotal()))
//                                    .mapToDouble(ExpedientePresupMontoDTO::getNmImporte)
//                                    .findFirst()
//                                    .orElse(0.0);
//                        }
//                        return 0.0;
//                    }).sum();
//        }
//    }
    
    
    
    /**
     * Calcula la suma vertical de todos los conceptos de una partida específica para una columna dada.
     */
    /**
     * VMM 14/10/26 MD-3123: convierte el dsValor (String) de un DatoDTO a importe, tratando
     * null/blanco/no numerico como 0.0 en vez de lanzar NullPointerException/NumberFormatException
     * (Double.parseDouble no admite null). Una modificacion presentada puede traer un concepto sin importe
     * (DS_VALOR nulo en SAII_DATO, que el informe muestra en blanco), y esas excepciones al calcular los
     * totales de la tabla de modificaciones solicitadas podian impedir que se pintara. Admite coma
     * decimal/punto de miles y punto decimal. Mismo criterio que FinanciacionBean.parseImporteDsValor.
     */
    private double parseImporteDsValor(String dsValor) {
        String valorNormalizado = Utils.normalizarSeparadorDecimal(dsValor);
        if (StringUtils.isBlank(valorNormalizado)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(valorNormalizado);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Double getSumaGrupoPres(CatalogosValoresDTO partida, int nCol, List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null || partida == null) {
            return 0.0;
        }

        Double resultado = lstPresup.stream()
        .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                        && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
        .mapToDouble(p -> {
            // nCol es el índice que viene del front (ya ajustado con el ternario)
            if (p.getDatos() != null && nCol < p.getDatos().size()
                    && p.getIdBloqueComponente() != null) { //para que no coja el ultimo registro de la fila TOTAL
                // VMM 14/10/26 MD-3123: parseImporteDsValor en vez de Double.parseDouble, que lanza
                // NullPointerException con un importe nulo (modificacion presentada con un concepto sin importe)
                Double imp = this.parseImporteDsValor(p.getDatos().get(nCol).getDsValor());
                return (imp != null) ? imp : 0.0;
            }
            return 0.0;
        }).sum();
        
        return resultado;
    }

    /**
     * Calcula el total acumulado de un grupo (Suma de todas sus anualidades).
     */
    public Double getSumaTotalGrupoPres(CatalogosValoresDTO partida, List<BloqueComponenteDTO> lstPresup) {
        // Si no hay ID de partida o no hay columnas, el total es 0
        if (this.listaAnualidades == null || partida == null || lstPresup == null) {
            return 0.0;
        }
        
        double total = 0.0;
        // Indice 0: Concepto (vacío), Indice 1: Anualidad 1, Indice 2: TOTAL.
        int indexTotal = this.listaAnualidades.size() +  1;
        
        // Filtramos la lista principal por la partida actual y sumamos el índice del total
        total = lstPresup.stream()
                .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                            && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                .mapToDouble(p -> {
                    if (p.getDatos() != null && indexTotal < p.getDatos().size()
                            && p.getIdBloqueComponente() != null) { //para que no coja el ultimo registro de la fila TOTAL
                        // VMM 14/10/26 MD-3123: ver parseImporteDsValor
                        Double imp = this.parseImporteDsValor(p.getDatos().get(indexTotal).getDsValor());
                        return (imp != null) ? imp : 0.0;
                    }
                    return 0.0;
                }).sum();
        
        return total;
    }
    
 // Suma vertical de una columna (anualidad) específica para TODAS las partidas
    public Double getGranTotalVerticalPres(int nCol, List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null) return 0.0;
        
        Double resul = lstPresup.stream()
        .filter(p -> p.getDatos() != null && nCol < p.getDatos().size()
                    && p.getIdBloqueComponente() != null) //para que no coja el ultimo registro de la fila TOTAL
        .mapToDouble(p -> {
            // VMM 16/10/26 MD-3123: Double.parseDouble(null) lanzaba NPE al pintar el pie de la tabla cuando
            // aun no hay datos cargados (dsValor null) y cortaba la pagina; ver parseImporteDsValor
            Double imp = this.parseImporteDsValor(p.getDatos().get(nCol).getDsValor());
            return (imp != null) ? imp : 0.0;
        })
        .sum();
        
        return resul;
    }
    
 // El Gran Total Final es simplemente la suma vertical de la columna de "TOTALES"
    public Double getGranTotalFinalPres(List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null || this.listaAnualidades == null) {
            return 0.0;
        }
        
        // Si la anualidad es la 1 (índice 1) y el total es el índice 2...
        // El índice del total horizontal siempre es el tamaño de la lista de anualidades + (conConceptos? 1 : 0)
        int indexTotalHorizontal = this.listaAnualidades.size() +  1;  
        double granTotal = getGranTotalVerticalPres(indexTotalHorizontal, lstPresup);
        
        return granTotal;
    }
    
    
    public Double getGranTotalFinalModif(List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || this.listaAnualidades == null || lstPresup.isEmpty()) {
            return 0.0;
        }
        
        int indexTotalHorizontal = this.listaAnualidades.size() + 1;  
        return getGranTotalVerticalModif(indexTotalHorizontal, lstPresup);
    }
    
//    public Double getGranTotalVerticalModif(int nCol, List<ExpedientePresupDTO> lstPresup) {
//        if (lstPresup == null || lstPresup.isEmpty()) {
//            return 0.0;
//        }
//        
//        Integer anualidadBuscada = null;
//        if (this.listaAnualidades != null && nCol >= 0 && nCol < this.listaAnualidades.size()) {
//            anualidadBuscada = this.listaAnualidades.get(nCol);
//        }
//
//        final Integer anualidadFinal = anualidadBuscada;
//
//        double totalidad =  lstPresup.stream()
//                // QUITADO: p.getIdExpedientePresup() != null para permitir sumar los nuevos registros con id null
//                .filter(p -> p.getLstExpedienteModifPresupMontoDTO() != null && !p.getLstExpedienteModifPresupMontoDTO().isEmpty())
//                .mapToDouble(p -> {
//                    if (anualidadFinal != null) {
//                        return p.getLstExpedienteModifPresupMontoDTO().stream()
//                                .filter(m -> m != null && m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
//                                .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
//                                .findFirst()
//                                .orElse(0.0);
//                    }
//                    return p.getLstExpedienteModifPresupMontoDTO().stream()
//                            .filter(m -> m != null && Boolean.TRUE.equals(m.getEsMontoTotal()))
//                            .mapToDouble(ExpedienteModifPresupMontoDTO::getNmImporteConcedido)
//                            .findFirst()
//                            .orElse(0.0);
//                })
//                .sum();
//        
//        return totalidad;
//    }
    
    public Double getGranTotalVerticalModif(int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || lstPresup.isEmpty()) {
            return 0.0;
        }
        
        Integer anualidadBuscada = null;
        if (this.listaAnualidades != null && nCol >= 0 && nCol < this.listaAnualidades.size()) {
            anualidadBuscada = this.listaAnualidades.get(nCol);
        }

        final Integer anualidadFinal = anualidadBuscada;

        double totalidad = lstPresup.stream()
                // 1. FILTRO CRUCIAL: Evitamos procesar la fila que sea el "TOTAL" general de la tabla
                // para que no duplique las sumas de las celdas horizontales.
                .filter(p -> p.getTipoFinanciacionDTO() == null || 
                        !"TOTAL".equalsIgnoreCase(p.getTipoFinanciacionDTO().getDsValor()))
                // Mantenemos tu filtro original para registros con montos válidos
                .filter(p -> p.getLstExpedienteModifPresupMontoDTO() != null && !p.getLstExpedienteModifPresupMontoDTO().isEmpty())
                .mapToDouble(p -> {
                    if (anualidadFinal != null) {
                        return p.getLstExpedienteModifPresupMontoDTO().stream()
                                .filter(m -> m != null && m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0)
                                .findFirst()
                                .orElse(0.0);
                    }
                    return p.getLstExpedienteModifPresupMontoDTO().stream()
                            .filter(m -> m != null && Boolean.TRUE.equals(m.getEsMontoTotal()))
                            .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0)
                            .findFirst()
                            .orElse(0.0);
                })
                .sum();
        
        return totalidad;
    }
    
    
    
    public Double getSumaTotalGrupoModif(CatalogosValoresDTO partida, List<ExpedientePresupDTO> lstPresup) {
        if (this.listaAnualidades == null || partida == null || lstPresup == null || lstPresup.isEmpty()) {
            return 0.0;
        }
        
        int indexTotal = this.listaAnualidades.size() + 1;
        
        return lstPresup.stream()
                .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                            && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                .mapToDouble(p -> {
                    // VMM 11/09/26 MD-2725: quitado p.getIdExpedientePresup() != null para poder
                    // sumar tambien los conceptos nuevos (sin ExpedientePresup real todavia),
                    // igual que ya se hizo en getGranTotalVerticalModif
                    if (p.getLstExpedienteModifPresupMontoDTO() != null && indexTotal < p.getLstExpedienteModifPresupMontoDTO().size()) {

                        ExpedienteModifPresupMontoDTO montoTotalHorizontal = p.getLstExpedienteModifPresupMontoDTO().get(indexTotal);
                        if (montoTotalHorizontal != null && montoTotalHorizontal.getNmImporte() != null) {
                            return montoTotalHorizontal.getNmImporte();
                        }
                    }
                    return 0.0;
                }).sum();
    }
    
    public Double getSumaGrupoModif(CatalogosValoresDTO partida, int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || lstPresup.isEmpty() || partida == null) {
            return 0.0;
        }
        
        Integer anualidadBuscada = null;
        if (this.listaAnualidades != null && nCol >= 0 && nCol < this.listaAnualidades.size()) {
            anualidadBuscada = this.listaAnualidades.get(nCol);
        }

        final Integer anualidadFinal = anualidadBuscada;
        
        return lstPresup.stream()
                .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                                && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                .mapToDouble(p -> {
                    // VMM 11/09/26 MD-2725: quitado p.getIdExpedientePresup() != null (ver
                    // getSumaTotalGrupoModif)
                    if (p.getLstExpedienteModifPresupMontoDTO() != null && !p.getLstExpedienteModifPresupMontoDTO().isEmpty()) {
                        if (anualidadFinal != null) {
                            return p.getLstExpedienteModifPresupMontoDTO().stream()
                                    .filter(m -> m != null && m.getNmAnualidad() != null && m.getNmAnualidad().equals(anualidadFinal) && !Boolean.TRUE.equals(m.getEsMontoTotal()))
                                    .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0)
                                    .findFirst()
                                    .orElse(0.0);
                        }
                        return p.getLstExpedienteModifPresupMontoDTO().stream()
                                .filter(m -> m != null && Boolean.TRUE.equals(m.getEsMontoTotal()))
                                .mapToDouble(m -> m.getNmImporte() != null ? m.getNmImporte() : 0.0)
                                .findFirst()
                                .orElse(0.0);
                    }
                    return 0.0;
                }).sum();
    }
}
