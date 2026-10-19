package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Interesado;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Expediente
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ExpedienteDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.estadoBeneficiarioDTO = new CatalogosValoresDTO();
        this.impresoDTO = new ImpresoDTO();
        this.impresoAceptacionDTO= new ImpresoDTO();
        this.impresoJustecDTO= new ImpresoDTO();
        this.impresoJustecFinalDTO= new ImpresoDTO();
        this.impresoJusEcoDTO= new ImpresoDTO();
        this.impresoJusEcoFinalDTO= new ImpresoDTO();
        this.impresoContratoDTO= new ImpresoDTO();
        this.impresoRenunciaConSutitucionDTO= new ImpresoDTO();
        this.impresoRenunciaSinSutitucionDTO= new ImpresoDTO();
        this.impresoModificacionesPresupPlazosOtrosDTO= new ImpresoDTO();
        this.lstExpedienteDocDTO = new ArrayList<>();
        this.lstExpedienteReqDTO = new ArrayList<>();
        this.listaDocAceptacionAyuda = new ArrayList<>();
        this.convocatoriaActDTO = new ConvocatoriaActDTO();
        this.estadoRevAdminDTO = new CatalogosValoresDTO();
        this.estadoRevRequiDTO = new CatalogosValoresDTO();
        this.estadoRevEvalDTO = new CatalogosValoresDTO();
        this.estadoRevFinanDTO = new CatalogosValoresDTO();
        this.estadoRevJusTecDTO = new CatalogosValoresDTO();
        this.estadoRevJusEcoDTO = new CatalogosValoresDTO();
        this.esEstadoEvalEnEvaluacion = Boolean.FALSE;
        this.valoresRelacionables = new HashMap<>();
        this.estadoProvisionalDTO = new CatalogosValoresDTO();
        this.estadoRevContratoDTO = new CatalogosValoresDTO();
        this.tipoPersonaSolicitudDTO = new CatalogosValoresDTO();
        this.medioComunicacionDTO = new CatalogosValoresDTO();

        this.blHabilitarResolucionProvisional = Boolean.FALSE;
        this.blHabilitarResolucionDefinitiva = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarProvisional1 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarProvisional2 = Boolean.FALSE;
        this.sePuedeDescargarInformeCoor = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivo1 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivo2 = Boolean.FALSE;
        this.esExpedienteSolicitante = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivo3 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivo4 = Boolean.FALSE;
        this.esCerradoCoordinadorTodo = Boolean.FALSE;
        this.mostrarBotoneraAprobarRechazarEvaluacion = Boolean.TRUE;
        this.blEstadoProvisional = Boolean.FALSE;
        this.blEstadoDefinitivo = Boolean.FALSE;
        this.colorFondoText = StringUtils.EMPTY;
        this.colorFondoTextResolucionDef = StringUtils.EMPTY;

        this.esExpedienteCoordinador = Boolean.FALSE;
        this.esExpedienteParticipante = Boolean.FALSE;
        this.lstExpedienteParticipante = new ArrayList<>();
        this.expedienteContrato = new ExpedienteContratoDTO();
        this.esExpedienteContrato = Boolean.FALSE;
        
        this.blMostrarMensajeAprobarDenegarDefinitivoIndividual1 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivoIndividual2 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivoIndividual3 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivoIndividual4 = Boolean.FALSE;
        this.blMostrarMensajeAprobarDenegarDefinitivoIndividual5 = Boolean.FALSE;


    }
    
    public ExpedienteDTO(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idProvisional
     */
    private CatalogosValoresDTO estadoProvisionalDTO;

    /**
     * Campo cdExpediente
     */
    private String cdExpediente;

    /**
     * Campo idSolicitud
     */
    private Integer idSolicitud;

    /**
     * Campo interesadoDTO
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo estadoBeneficiarioDTO
     */
    private CatalogosValoresDTO estadoBeneficiarioDTO;

    /**
     * Campo impresoDTO
     */
    
    private ImpresoDTO impresoDTO;
	
	/**
     * Campo impresoJustecDTO Intermedia
     */
 
    private ImpresoDTO impresoJustecDTO;
    
    /**
     * Campo impresoJustecFinalDTO
     */
  
    private ImpresoDTO impresoJustecFinalDTO;
    
    /**
     * Campo impresoContratoDTO
     */
    
    private ImpresoDTO impresoContratoDTO;

    /**
     * Campo convocatoriaActDTO
     */
    private ConvocatoriaActDTO convocatoriaActDTO;

    /**
     * campo nmPASO
     */
    private Integer nmPaso;

    /**
     * campo nmNota
     */
    private Integer nmNota;

    /**
     * Campo cdEreg
     */
    private String cdEreg;

    /**
     * Campo dsDestinoEreg
     */
    private String dsDestinoEreg;

    /**
     * Campo fcFirma
     */
    private Date fcFirma;

    /**
     * Campo cdAsunto
     */
    @JsonIgnore
    private String cdAsunto;

    /**
     * Campo cdUnidad
     */
    @JsonIgnore
    private String cdUnidad;

    /**
     * campo cdSiex
     */
    @JsonIgnore
    private String cdSiex;

    /**
     * Lista de documentos asociados al expediente
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExpedienteDocDTO> lstExpedienteDocDTO;

    /**
     * Lista de requisitos asociados al expediente
     */
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExpedienteReqDTO> lstExpedienteReqDTO;

    /**
     * Campo indicador de seleccion de expediente
     */
    @JsonIgnore
    private Boolean itSeleccionExpediente;

    /**
     * Campo auxiliar idDocumentoPtfr
     */
    
    @Transient
    private DocumentoPtfrDTO documentoPtfrDTO;

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
     * Representa el campo estadoRevJusTec
     */
    private CatalogosValoresDTO estadoRevJusTecDTO;
    
    /**
     * Representa el campo estadoRevContrato
     */
    private CatalogosValoresDTO estadoRevContratoDTO;
    
    /**
     * Representa el campo itAlegaciones
     */
    private String itAlegaciones;

    /**
     * Representa el campo cdFicaValoracionAleg
     */
    private Integer cdFicaValoracionAleg;

    /**
     * Representa el campo cdNoteProvisional
     */
   
    private String cdNoteProvisional;

    /**
     * Variable que indica si la evaluación está en estado de evaluación
     */
    @JsonIgnore
    private Boolean esEstadoEvalEnEvaluacion;

    @JsonIgnore
    private Map<Integer, String> valoresRelacionables;

    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarProvisional1;

    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarProvisional2;

    /**
     * Campo para verificar si esta en el estado "Evaluado OK" o "Evaluado KO
     */
    private Boolean sePuedeDescargarInformeCoor;

    @JsonIgnore
    private Boolean blHabilitarResolucionProvisional;

    /** campo que indica si se habilita la resolución definitiva **/
    @JsonIgnore
    private Boolean blHabilitarResolucionDefinitiva;

    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivo1;

    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivo2;

    /**
     * Indica si el expediente es de un actor Solicitante
     */
   
    private Boolean esExpedienteSolicitante;

    /**
     *
     */
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivo3;

    /**
     *
     */
   
    private Boolean esCerradoCoordinadorTodo;

    /**
     *
     */
    
    private Boolean mostrarBotoneraAprobarRechazarEvaluacion;

    /**
     * Indica si el expediente esta en estado Aprobado Provisional o Denegado
     * Provisional
     */
    @JsonIgnore
    private Boolean blEstadoProvisional;

    /**
     * Indica si el expediente esta en estado Aprobado Definitivo o Denegado
     * Definitivo
     */
    @JsonIgnore
    private Boolean blEstadoDefinitivo;

    /**
     * Indica el color de Fondo de un campo Text donde haya un estado.
     */
    @JsonIgnore
    private String colorFondoText;

    /**
     * Indica el color de Fondo de un campo Text donde haya un estado aprobado o
     * denegado def.
     */
    @JsonIgnore
    private String colorFondoTextResolucionDef;

    /**
     * Indica si el expediente esta en estado desistido o excluido.
     */
    @JsonIgnore
    private Boolean modoConsulta;

    /**
     * Indica si el expediente esta en estado pendiente revisión, pendiente
     * requerimiento, pendiente revisión subsanación.
     */
    @JsonIgnore
    private Boolean bAvisoDocumento;

    /**
     * Indica si el expediente esta en estado pendiente revisión, pendiente
     * requerimiento, pendiente revisión subsanación requisitos.
     */
    @JsonIgnore
    private Boolean bAvisoRequisitos;

    /**
     * Indica si el expediente esta en estado pendiente asignación coordinador,
     * pendiente revisión evaluación gestor.
     */
    @JsonIgnore
    private Boolean bAvisoEvaluacion;

    /**
     * Indica si el expediente esta en estado pendiente provisional, propuesta
     * de resolución alegaciones.
     */
    @JsonIgnore
    private Boolean bAvisoResolucion;
    
    /**
     * Indica si el expediente esta en estado pendiente provisional, propuesta
     * de resolución alegaciones.
     */
    @JsonIgnore
    private Boolean bAvisoJustificacion; 

    /**
     * Indica si el expediente esta en estado pendiente de revisión de la
     * modificación de las partidas presupuestarias, los plazos de ejecución
     * u otras modificaciones
     */
    @JsonIgnore
    private Boolean bAvisoPresupuestarias;

    /**
     * Estado que requiere atención por parte del gestor
     */
    @JsonIgnore
    private Boolean bAviso;
    
    /**
     * Indica si el expediente esta en estado pendiente de revision de los contratos, 
     * pendiente de subsanacion, revision subsanacion
     */
    @JsonIgnore
    private Boolean bAvisoContratos;

    @JsonIgnore
    private Boolean esExpedienteCoordinador;

    @JsonIgnore
    private Boolean esExpedienteParticipante;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivo4;

    @JsonIgnore
    private List<ExpedienteDTO> lstExpedienteParticipante;

    /**
     * Representa la fecha de inicio del contrato
     */
    private Date fcIniContrato;

    /**
     * Representa la fecha fin del contrato
     */
    private Date fcFinContrato;
    
    
    @JsonIgnore
    private transient String estadoRevision;
    
    /**
     * Representa el campo expedienteContrato
     */
    
    private ExpedienteContratoDTO expedienteContrato;
   
    /**
     * Representa el campo expedienteContratoSustitucion
     */
    
    private ExpedienteContratoDTO expedienteContratoSustitucion;
    
    /**
     * Lista de expediente contrato
     */
    private List<ExpedienteContratoDTO> lstExpedienteContratoDTO;
    
    
    private Boolean esExpedienteContrato;

    /**
     * Campo idExpedientePadre
     */
    private Integer idExpedientePadre;
    
    /**
     * Campo itAlegacionRevisada
     */
    private String itAlegacionRevisada;
    
    /**
     * Campo cdEregSubsanaReq
     */
    private String cdEregSubsanaReq;

    /**
     * Campo fcFirmaSubsanaReq
     */
    private Date fcFirmaSubsanaReq;
    
    /**
     * Campo cdEregSubsanaAdm
     */
    private String cdEregSubsanaAdm;

    /**
     * Campo fcFirmaSubsanaAdm
     */
    private Date fcFirmaSubsanaAdm;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivoIndividual1;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivoIndividual2;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivoIndividual3;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivoIndividual4;
    
    @JsonIgnore
    private Boolean blMostrarMensajeAprobarDenegarDefinitivoIndividual5;
    
    private String leidoNotificacionNote;
    
    /**
     * Campo impresoRenunciaConSutitucionDTO
     */
    
    private ImpresoDTO impresoRenunciaConSutitucionDTO;
    
    /**
     * Campo impresoRenunciaSinSutitucionDTO
     */
    
    private ImpresoDTO impresoRenunciaSinSutitucionDTO;

    /**
     * Campo impresoModificacionesPresupPlazosOtrosDTO
     */
    private ImpresoDTO impresoModificacionesPresupPlazosOtrosDTO;
    
    /**
     * Campo listaDocumentosPresup
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExpedienteDocDTO> listaDocumentosPresup;
    
    /**
     * Campo expedienteModifOtraDTO
     */
    private ExpedienteModifOtraDTO expedienteModifOtraDTO;
    
    /**
     * Campo listaExpedientePresup
     */
    private   List<ExpedientePresupDTO> listaExpedientePresup;
    
    /**
     * Campo listaExpedientePlazos
     */
    private List<ExpedienteAnualidadDTO> listaExpedientePlazos;
    
    /**
     * Lista de documentos asociados al expediente
     */
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExpedienteDocDTO> listExpedienteDocDTO;
    
    /**
     * Campo motivoRechazo
     */
    private String motivoRechazo;
    
    /**
     * Indica si la identificacion es representante legal
     */
    private Boolean isRepresentanteLegal;
    
    /**
     * Indica el NIF/CIF del representante legal
     */
    private InteresadoDTO representanteLegal;
    
    /**
     * Area
     */
    private ConvocatoriaAreaDTO convocatoriaAreaDTO;
    
    /**
     * Subarea
     */
    private ConvocatoriaAreaSubareaDTO convocatoriaAreaSubareaDTO;
    
    private SolicitudDTO solicitudDTO;
    
    private Date fcPresentacion;
    
    /**
     * Campo impresoJusEcoDTO Intermedia
     */
 
    private ImpresoDTO impresoJusEcoDTO;
    
    /**
     * Campo impresoJusEcoFinalDTO
     */
  
    private ImpresoDTO impresoJusEcoFinalDTO;
    
    @JsonIgnore
    private Boolean bAvisoJustificacionEco;
    
    /**
     * Representa el campo estadoRevJusEco
     */
    private CatalogosValoresDTO estadoRevJusEcoDTO;

    /**
     * Representa el campo idPersonaSolicitante
     */
    private CatalogosValoresDTO tipoPersonaSolicitudDTO;

    /**
     * Representa el campo idMedioNotificacion
     */
    private CatalogosValoresDTO medioComunicacionDTO;
    
    /**
     * Representa el campo estadoRevFinan
     */
    private CatalogosValoresDTO estadoRevFinanDTO;

    private ImpresoDTO impresoAceptacionDTO;
    
    private List<ExpedienteDocDTO> listaDocAceptacionAyuda;
    
    private String itAceptaAyuda;
    
    @JsonIgnore
    private String dniUserLogin;
    
    @JsonIgnore
    private String cifEntidadLogin;

    private EntidadDTO entidadDTO;
      
    private Boolean activarConcederDenegar;
    
    /**
     * Submod
     */
    private ConvocatoriaSubmodDTO convocatoriaSubmodDTO;
    
    /**
     * Campo cdEregAlegaciones
     */
    private String cdEregAlegaciones;

    /**
     * Código de la entrada EREG generada para el trámite de Notificación
     * del ciudadano. No se persiste en SAII_EXPEDIENTE: se guarda en
     * SAII_NOTIFI_CIUD.CD_EREG.
     */
    private String cdEregNotifiCiud;

    /**
     * Campos transitorios (no persistidos en SAII_EXPEDIENTE) usados para
     * generar el PDF de recibo de presentación del trámite de Notificación
     * del ciudadano.
     */
    private String dsAsuntoNotifiCiud;

    private String tlExponeNotifiCiud;

    private String tlSolicitaNotifiCiud;

    /**
     * Campo fcFirmaAlegaciones
     */
    private Date fcFirmaAlegaciones;
    
    /**
     * Campo autorizado
     */
    private InteresadoDTO autorizado;
    
    
}