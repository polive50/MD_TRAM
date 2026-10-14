package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.util.Constantes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Convocatorias
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ConvocatoriaDTO() {
        super();

        this.idEstado = new CatalogosValoresDTO();
        this.idTipoSolicitud = new CatalogosValoresDTO();
        this.idTipoConcesion = new CatalogosValoresDTO();
        this.idLineaAyuda = new CatalogosValoresDTO();
        this.idDireccion = new UnidadAdminDTO();
        this.idSubdireccion = new UnidadAdminDTO();
        this.idAreaAdministrativa = new UnidadAdminDTO();
        this.idAreaAdministrativaJCT = new UnidadAdminDTO();
        this.idModalidadConvocatoria = new CatalogosValoresDTO();
        this.tipoResolModalidadDTO = new TipoResolModalidadDTO();
        this.idTipoModalidad = new CatalogosValoresDTO();
        this.tipoActorSolicitanteDTO = new CatalogosValoresDTO();
        this.listConvocatoriaAct = new ArrayList<>();
        this.listConvocatoriaDoc = new ArrayList<>();
        this.listConvocatoriaEnl = new ArrayList<>();
        this.listConvocatoriaSubmod = new ArrayList<>();
        this.listConvocatoriaArea = new ArrayList<>();
        this.listConvocatoriaImp = new ArrayList<>();
        this.listConvocatoriaGest = new ArrayList<>();
        this.esPermiteAsociarCoordinador = Boolean.FALSE;
        this.nmDiasSubsanacion = Constantes.Notificacion.NM_DIAS_SUBSANACION;
        this.esConvocatoriaContrato = Boolean.FALSE;
        this.esEvalucionMarcado = Boolean.FALSE;
        this.esAlegacionMarcado = Boolean.FALSE;
        this.esConvocatoriaActas = Boolean.FALSE;
        this.unidadAdminJCTSel = new UnidadAdminDTO();
        this.gestorConcesion = new UsuarioUnidadAdminDTO();
        this.gestorJCT = new UsuarioUnidadAdminDTO();
        this.esGestorConcesion = false;
        this.esGestorJCT = false;
        this.porcentajeIntensidadAyuda=100.0;
        this.esPermiteAsociarArea = Boolean.FALSE;
        this.idAceptacionDesistimientoAyuda = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo cdAcronimo
     */
    private String cdAcronimo;

    /**
     * Campo cdTitulo
     */
    private String cdTitulo;

    /**
     * Campo dsDescripcion
     */
    private String dsDescripcion;

    /**
     * Campo itConvocatoriaActiva
     */
    private String itConvocatoriaActiva;

    /**
     * Campo nmDurMaxAyuda
     */
    private Integer nmDurMaxAyuda;
    
    /**
     * Campo nmAnualJusEco
     */
    @JsonIgnore
    private Integer nmAnualJusEco;

    /**
     * Campo nmImportePresAnualidad
     */
    private Double nmImportePresAnualidad;

    /**
     * Campo nmImportePresTotal
     */
    private Double nmImportePresTotal;

    /**
     * Campo itInicioSolOrg
     */
    private String itInicioSolOrg;

    /**
     * Campo fcIniPresentacion
     */
    private Date fcIniPresentacion;

    /**
     * Campo fcFinPresentacion
     */
    private Date fcFinPresentacion;

    /**
     * Campo itPresFueraPlazo
     */
    private String itPresFueraPlazo;

    /**
     * Representa indicador itPresFueraPlazo
     */
    @JsonIgnore
    private Boolean blPresFueraPlazo;

    /**
     * Campo nmDiasSubsanacion
     */
    private Integer nmDiasSubsanacion;
    
    /**
     * Campo nmDiasHabilesAudiencia
     */
    private Integer nmDiasHabilesAudiencia;
    
    /**
     * Campo nmDiasLimmodif
     */
    private Integer nmDiasLimmodif;

    /**
     * Representa el campo fcIniTramiEval
     */
    private Date fcIniTramiEval;

    /**
     * Representa el campo fcFinTramiEval
     */
    private Date fcFinTramiEval;

    /**
     * Representa la fecha de inicio de tramite evaluacion formateado
     */
    @JsonIgnore
    private String fcIniTramiEvalFormat;

    /**
     * Representa la fecha de inicio de tramite evaluacion formateado
     */
    @JsonIgnore
    private String fcFinTramiEvalFormat;

    /**
     * Representa el campo isMenorFechaIniEva
     */
    private Boolean isMenorFechaIniEva;

    /**
     * Representa el campo isValFechaEval
     */
    private Boolean isValFechaEval;

    /**
     * Representa el campo fcIniAlegaciones
     */
    private Date fcIniAlegaciones;

    /**
     * Representa el campo fcFinAlegaciones
     */
    private Date fcFinAlegaciones;

    /**
     * Campo idEstado
     */
    private CatalogosValoresDTO idEstado;
    
    /**
     * Campo idTipoSolicitud
     */
    private CatalogosValoresDTO idTipoSolicitud;
    
    /**
     * Campo idTipoSolicitud
     */
    private CatalogosValoresDTO idTipoConcesion;
    
    /**
     * Campo idLineaAyuda
     */
    private CatalogosValoresDTO idLineaAyuda;

    /**
     * Campo idDireccion
     */
    private UnidadAdminDTO idDireccion;

    /**
     * Campo idSubdireccion
     */
    private UnidadAdminDTO idSubdireccion;

    /**
     * Campo idAreaAdministrativa
     */
    private UnidadAdminDTO idAreaAdministrativa;
    
    /**
     * Campo idAreaAdministrativaJCT
     */
    private UnidadAdminDTO idAreaAdministrativaJCT;

    /**
     * Campo idModalidadConvocatoria
     */
    private CatalogosValoresDTO idModalidadConvocatoria;
    
    /**
     * Campo tipoResolModalidad
     */
    private TipoResolModalidadDTO tipoResolModalidadDTO;
    
    /**
     * Campo idTipoModalidad
     */
    private CatalogosValoresDTO idTipoModalidad;

    /**
     * Campo ConvocatoriaAct
     */
    private List<ConvocatoriaActDTO> listConvocatoriaAct;

    /**
     * Campo ConvocatoriaDoc
     */
    private List<ConvocatoriaDocDTO> listConvocatoriaDoc;

    /**
     * Campo ConvocatoriaEnl
     */
    private List<ConvocatoriaEnlDTO> listConvocatoriaEnl;

    /**
     * Campo ConvocatoriaSubmod
     */
    private List<ConvocatoriaSubmodDTO> listConvocatoriaSubmod;

    /**
     * Campo ConvocatoriaArea
     */
    private List<ConvocatoriaAreaDTO> listConvocatoriaArea;
    
    /**
     * Campo ConvocatoriaImp
     */
    @JsonIgnore
    private List<ImpresoDTO> listConvocatoriaImp;
    
    /**
     * Campo ConvocatoriaGest
     */
    private List<ConvocatoriaGestorDTO> listConvocatoriaGest;

    /**
     * Campo nmAno
     */
    private Integer nmAno;

    /**
     * Campo dsDescripcionCorta
     */
    private String dsDescripcionCorta;

    /**
     * Campo tipoActorSolicitanteDTO Indica el valor del tipo de actor del unico
     * solicitante.
     */
    private CatalogosValoresDTO tipoActorSolicitanteDTO;

    /**
     * Campo itFondosEuropa
     */
    private String itFondosEuropa;

    /**
     * Campo alertaTramitesPendiente
     */
    @JsonIgnore
    private Integer alertaTramitesPendiente;

    /**
     * Campo alertaDocumentos
     */
    @JsonIgnore
    private Integer alertaDocumentos;

    /**
     * Campo alertaRequisitos
     */
    @JsonIgnore
    private Integer alertaRequisitos;

    /**
     * Campo itRequisitos
     */
    private String itRequisitos;

    /**
     * Variable que indica si se permite agregar coordinadores a convocatoria
     */
    private Boolean esPermiteAsociarCoordinador;

    /**
     * Campo fcTraComisionEval
     */
    private Date fcTraComisionEval;

    /**
     * Campo fcTraPropResDef
     */
    private Date fcTraPropResDef;

    /**
     * Campo isMenorFechaIniAle
     */

    private Boolean isMenorFechaIniAle;
    
    /**
     * Campo isMenorFechaIniTramEval
     */

    private Boolean isMenorFechaIniTramEval;

    /**
     * Campo nmCalificacionMax
     */

    private Integer nmCalificacionMax;

    /**
     * Campo fcTraResolucionDef
     */

    private Date fcTraResolucionDef;

    /**
     * Representa la fecha de inicio de contrato a nivel convocatoria
     */
    private Date fcIniContrato;

    /**
     * Representa la fecha de fin de contrato a nivel convocatoria
     */
    private Date fcFinContrato;

    /**
     * Representa el indicador si la convocatoria tiene contrato
     */
    @JsonIgnore
    private Boolean esConvocatoriaContrato;
    
    /**
     * Representa el indicador si la convocatoria tiene contratos
     */
    private String itContratos;
    
    /**
     * Representa el indicador si la convocatoria tiene partidas presupuestarias
     */
    private String itPartidasPresupuestarias;
    
    /**
     * Representa el indicador si la convocatoria tiene actas de comites de gestion
     */
    private String itActasComitesGestion;
    
    /**
     * Representa el indicador si la convocatoria tiene actas de comites de gestion
     */
    @JsonIgnore
    private Boolean esConvocatoriaActas;
    
    private UnidadAdminDTO unidadAdminJCTSel;
    
    private UsuarioUnidadAdminDTO gestorConcesion;
    
    private UsuarioUnidadAdminDTO gestorJCT;

    
    /**
     * Representa el indicador si la convocatoria tiene itAbiertaPermanente
     */
    private String itAbiertaPermanente;
    
    /**
     * Representa el indicador si la convocatoria tiene itAgotarFondos
     */
    private String itAgotarFondos;
    
    
    /**
     * Representa el indicador si la convocatoria tiene itfondosAgotados
     */
    private String itfondosAgotados;
    
    /**
     * Representa el indicador si la convocatoria tiene itConvocatoriaCerrada
     */
    private String itConvocatoriaCerrada;
    
    /**
     * Representa el indicador si la convocatoria tiene itEvaluacion
     */
    private String itEvaluacion;
    
    /**
     * Representa el indicador si la convocatoria tiene el check Evaluacion marcado
     */
    @JsonIgnore
    private Boolean esEvalucionMarcado;
    
    
    /**
     * Representa el indicador si la convocatoria tiene itAlegacion
     */
    private String itAlegacion;
    
    /**
     * Representa el indicador si la convocatoria tiene el check Alegacion marcado
     */
    @JsonIgnore
    private Boolean esAlegacionMarcado;
    
    /**
     * Campo nmMaxPeriodos
     */
    private Integer nmMaxPeriodos;
    
    private boolean esGestorConcesion;
    
    private boolean esGestorJCT;
    
    /**
     * Campo nmDiasAmpliContrato
     */
    private Integer nmDiasAmpliContrato;
    
    /**
     * Campo porcentajeIntensidadAyuda
     */
    private Double porcentajeIntensidadAyuda;
    
    /**
     * Representa el indicador si la convocatoria tiene detalle de Presupuestos
     */
    private String itConceptoPresup;
    
    /**
     * Representa el indicador si las fechas se han trasladado a los expedientes
     */
    private String itTrasladoFechasCont;
    
    /**
     * Campo alertaDocumentosJCT
     */
    @JsonIgnore
    private Integer alertaDocumentosJCT;
    
    /**
     * Campo alertaDocumentosContrato
     */
    @JsonIgnore
    private Integer alertaDocumentosCont;
    
    /**
     * Campo alertaDocumentosModificacionesPPO
     */
    @JsonIgnore
    private Integer alertaDocumentosModif;
    
    /**
     * Campo alertaDocumentosJEco
     */
    @JsonIgnore
    private Integer alertaDocumentosJEco;
    
    /**
     * Variable que indica si se permite agregar area a convocatoria
     */
    private Boolean esPermiteAsociarArea;
    
    private List<CatalogosValoresDTO> listaTipoEntidades;
    
    /**
     * Campo fcTraPropResProv
     */
    private Date fcTraPropResProv;
    
    /**
     * Campo fcTraResolucionProv
     */
    private Date fcTraResolucionProv;
    
    /**
     * Representa el indicador si la convocatoria tiene 
     * prespuestos maximos por anualidad o por partida presupuestaria
     */
    private String itImporteMaxAnual;
    
    /**
     * Campo instrucciones
     */
    private String instrucciones;
    
    private Integer idEstadoResolucion;
    
    private List<ConfiguracionDTO> listaConfiguracion;
    
    /**
     * Representa el indicador si la convocatoria tiene 
     * prespuestos maximos por expediente/actor
     */
    private String itImporteMaxExpdte;
    
    /**
     * Representa el campo itDetalleJusEco del objeto de dominio
     */
    private String itDetalleJusEco;
    
    /**
     * Campo informacion sobre proteccion de datos
     */
    private String informacionRgpd;
    
    /**
     * Campo id Aceptacion Desistimiento Ayuda
     */
    private CatalogosValoresDTO idAceptacionDesistimientoAyuda;
    
    /**
     * Campo id Entidad Cerrada
     */
    private String itEntidadCerrada;
    
    /**
     * Campo itAyudaCalculada
     */
    private String itAyudaCalculada;
    
    
    private Boolean itResProvConcDeneg;
    
    private Boolean itResProvExclusion;
    
    private Boolean itResProvDesistimiento;
    
   
}
