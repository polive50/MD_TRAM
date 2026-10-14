package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dto.InformeEvalDocDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Expediente Filtro
 *
 * @author Quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteFiltrosDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ExpedienteFiltrosDTO() {
        super();
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
     * Campo strCdDocu
     */
    private String strCdDocu;

    /**
     * Campo cdAcronimo
     */
    private String cdAcronimoConvocatoria;

    /**
     * Campo dsTitulo
     */
    private String dsTituloConvocatoria;

    /**
     * Campo nmAno
     */
    private Integer nmAnoConvocatoria;

    /**
     * Campo idAreaCientifica
     */
    private Integer idAreaCientifica;

    /**
     * Campo dsAreaCientifica
     */
    private String dsAreaCientifica;



	//    private Integer idEstado;
//    private String dsEstado;

    private CatalogosValoresDTO estadoDTO;


//    private Integer idEstadoBeneficiario;
//    private String dsEstadoBeneficiario;

    private CatalogosValoresDTO estadoBeneficiarioDTO;


//    private Integer idEstadoRevAdmin;
//    private String dsEstadoRevAdmin;

    private CatalogosValoresDTO estadoRevAdminDTO;


//    private Integer idEstadoRevRequi;
//    private String dsEstadoRevRequi;

    private CatalogosValoresDTO estadoRevRequiDTO;


//    private Integer idEstadoRevEva;
//    private String dsEstadoRevEva;

    private CatalogosValoresDTO estadoRevEvaDTO;

    /**
     * Campo cdExpediente
     */
    private String cdExpediente;

    /**
     * Campo idTipoInteresado
     */
    private CatalogosValoresDTO tipoInteresadoDTO;

    /**
     * Campo dsNifInteresado
     */
    private String dsNifInteresado;

    /**
     * Campo dsNombreInteresado
     */
    private String dsNombreInteresado;

    /**
     * Campo dsApellido1Interesado
     */
    private String dsApellido1Interesado;

    /**
     * Campo dsApellido2Interesado
     */
    private String dsApellido2Interesado;

    /**
     * Campo strTipoPersona
     */
    private String strTipoPersona;

    /**
     * Variable que representa el identificador de expediente evaluación
     */
    private Integer idExpedienteEval;

    /**
     * Variable que representa el indicador si es coordinador o evaluador
     */
    private String itCoorEval;


//    private Integer idEstadoCoordinadorEvaluador;
//    private String dsEstadoCoordinadorEvaluador;

    private CatalogosValoresDTO estadoCoordinadorEvaluadorDTO;

    /**
     * Variable que representa el identificador de la convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Variable que representa el código de usuario
     */
    private String cdUsuario;

    /**
     * Variable que representa la fecha de inicio de alegaciones
     */
    private Date fcIniAlegaciones;

    /**
     * Variable que representa la fecha de fin de alegaciones
     */
    private Date fcFinAlegaciones;

    /**
     * Variable que representa el indicador si es provisional
     */
    private Boolean esProvisional;

    /**
     * Variable que representa el indicador si se permite alegar
     */
    private Boolean esPermiteAlegar;
    
    /**
     * Variable que representa el indicado si se permite alegar en una resolución individual
     */
    private Boolean esPermiteAlegarIndividual;
    
    /**
     * Variable que representa el indicador que se debe 
     */
    private Boolean alertaNoteIndividual;
    
    /**
     * Variable que representa la fecha de lectura en NOTE
     */
    private Date fcLecturaNote;

    /**
     * Variable que representa la fecha de presentación EREG
     */
    private String fcPresentacionEreg;

    /**
     * Variable que representa el indice del paginado personalizado (mova)
     */
    private Integer indicePaginacion;

    /**
     * Variable que representa el cantidad de registros a mostrar del paginado
     * personalizado (mova)
     */
    private Integer registroPorPagina;

    /**
     * Variable que representa el cantidad total de registros del paginado
     * personalizado (mova)
     */
    private Integer totalRegistrosPaginacion;

    /**
     * Campo para verificar si esta en el estado "Evaluado OK" o "Evaluado KO
     */
    private Boolean sePuedeDescargarInformeCoor;

    /**
     * Variable que representa el código de fica del informe de evaluación
     */
    private String cdFicaInformeEvaluacion;

    /**
     * Variable que representa la calificación máxima para el informe de
     * evaluación de coordinador
     */
    private Integer nmCalificacionMax;

    /**
     * Variable que representa la calificación final de la evaluación
     */
    private Integer nmNotaFinal;

    /**
     * Variable que representa el comentario final de la evaluación
     */
    private String dsComentarioFinal;


//    private Integer idTipoActor;
//    private String dsTipoActor;

    private CatalogosValoresDTO tipoActorDTO;


//    private Integer idTipoIdentificacion;
//    private String dsTipoIdentificacion;

    private CatalogosValoresDTO tipoIdentificacionDTO;

    /**
     * Variable que representa si el beneficiario realizó alegación
     */
    private String itAlegaciones;

    /**
     * Variable que representa si el gestor realizó informe de alegación
     */
    private String cdFicaValoracionAleg;

    /**
     * Variable que representa si la fecha es inicio de presentación
     */
    private Date fcInicioPresentacion;

    /**
     * Variable que representa si la fecha es fin de presentación
     */
    private Date fcFinPresentacion;

    /**
     * Variable que representa si si la presentación esta fuera de plazo
     */
    private String itPresFueraPlazo;
    
    /**
     * Variable que representa la fecha de lectura en note notificacionNote
     */
    private String leidoNotificacionNote;
    
    /**
     * Variable que representa el estado de notificacion en note
     */
    private String estadoNote;

    /**
     * Variable que representa el id del docuemnto de notificacion en note
     */
    private Integer idDocumentoPtfr;
    
    /**
     * Variable que representa lista de expediente justec
     */
    private List<ExpedienteJusTecDTO> listaExpedienteJustec;
    

//    private Integer idEstadoRevCont;
//    private String dsEstadoRevCont;

    private CatalogosValoresDTO estadoRevContDTO;

    /**
     * Variable que representa lista de expediente justec pendientes de subsanacion
     */
    private List<ExpedienteJusTecDTO> listaExpedienteSubsanarJustec;
    
    /**
     * Variable que representa lista de expediente justec pendientes de evaluación
     */
    private List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJustec;

    /**
     * Variable que representa el id del estado de la convocatoria
     */
    private CatalogosValoresDTO estadoConvocatoriaDTO;

     
    /**
     * Variable que representa el id del estado de la solicitud
     */
    
    private CatalogosValoresDTO estadoSolicitudDTO;

    /**
     * Variable que representa si el expediente tiene contrato o no
     */
    private Boolean itContrato;

    /**
     * Variable que representa tipo de resolución de modalidad de convocatoria
     */
    private String tipoResolModalidadConvocatoria; 
    
    
    /**
     * Variable que representa si cumplimneta o no el formulario
     */
    private Boolean itCumplimentaFormulario;
    
    /**
     * Variable que representa si tiene evaluacion
     */
    private String itEvaluacion;

    /**
     * Variable que representa modificarPrespPlazosOtros
     */
   private Boolean itModificarPrespPlazosOtros;
   
   /**
    * Variable que representa la observacion de la evaluación
    */
   private String obsEval;
   
   /**
    * Variable que representa tipo de concesion de convocatoria
    */
   private CatalogosValoresDTO tipoConcesionConvocatoriaDTO;

   /**
    * Variable que representa la notificacion para Subsanar Contratos
    */
   private Boolean notificacionSubsanarContratos;
   
   /**
    * Variable que representa lista de expedientes pendiente subsanar req
    */
   private List<ExpedienteDTO> listaExpedientesSubsanarReq;
   
   /**
    * Variable que represneta el id del tipo de solicitud de la convocatoria
    */
   private CatalogosValoresDTO idTipoSolicitud;
   
   /**
    * Variable que representa el id de la solicitud
    */
   private Integer idSolicitud;
   
   /**
    * Varibale que representa el id de Actor
    */
   private Integer idActor;
   
   /**
    * Variable que representa si tiene subsanación administrativa en los participantes
    */
   private Boolean tieneSubsaAdminParticipante;
   
   /**
    * Variable que representa si se ha revisado o no la alegación
    */
   private String itAlegacionRevisada;
   
   /**
    * Variable que representa si existe alguna subsanación sin leer
    */
   private Boolean tieneSubsanacionNoLeida;
   
   /**
    * Indica si la identificacion es representante legal
    */
   private Boolean isRepresentanteLegal;
   
   /**
    * Indica el NIF/CIF del representante legal
    */
   private String nifEntidadRepresentanteLegal;

   private List<ExpedienteJusEcoDTO> listPeriodosExpJusEco;
   
   private String nifRepresentanteAutorizado;
   
   private Integer idPersonaSolicitante;
   
   private String nifInteresado;

   private Boolean mostrarAceptacionAyuda;
   
   /**
    * Variable que representa la fecha de inicio de aceptación de la ayuda
    */
   private Date fcIniAceptacion;

   /**
    * Variable que representa la fecha de fin de aceptación de la ayuda
    */
   private Date fcFinAceptacion;
   
   private Boolean mostrarAlertaAceptacionAyuda;

   private Boolean leidoConcedidoDefinitivoNotifNote;
   
   private ConvocatoriaDTO convocatoriaDTO;
   
   private String desistidoNotificadoLeido;
   
   private String excluidoNotificadoLeido;
   
   private List<CatalogosValoresDTO> listaTipoInformes;
   
   private List<InformeEvalDocDTO> listaInformesEvalCoor;
}
