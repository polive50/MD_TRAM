package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class ExpedienteHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ExpedienteHistDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.estadoBeneficiarioDTO = new CatalogosValoresDTO();
        this.impresoDTO = new ImpresoDTO();
        this.convocatoriaActDTO = new ConvocatoriaActDTO();
        this.estadoRevAdminDTO = new CatalogosValoresDTO();
        this.estadoRevRequiDTO = new CatalogosValoresDTO();
        this.estadoRevEvalDTO = new CatalogosValoresDTO();
        this.estadoProvisionalDTO = new CatalogosValoresDTO();
        this.estadoRevFinanDTO = new CatalogosValoresDTO();
        this.autorizado = new InteresadoDTO();
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
    private Integer idExpedienteHist;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

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
     * Campo convocatoriaActDTO
     */
    private ConvocatoriaActDTO convocatoriaActDTO;

    /**
     * campo nmPASO
     */
    private Integer nmPaso;

    /**
     * Campo cdEreg
     */
    @JsonIgnore
    private String cdEreg;

    /**
     * Campo dsDestinoEreg
     */
    @JsonIgnore
    private String dsDestinoEreg;

    /**
     * Campo cdPdfFirmadoEreg
     */
    @JsonIgnore
    private String cdPdfFirmadoEreg;

    /**
     * Campo cdListadoDocEreg
     */
    @JsonIgnore
    private String cdListadoDocEreg;

    /**
     * Campo fcFirma
     */
    @JsonIgnore
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
     * campo nmNota
     */
    @JsonIgnore
    private Integer nmNota;

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
     * Campo idProvisional
     */
    private CatalogosValoresDTO estadoProvisionalDTO;

    /**
     * Representa el campo itAlegaciones
     */
    private String itAlegaciones;

    /**
     * Representa el campo cdFicaValoracionAleg
     */
    private Integer cdFicaValoracionAleg;

    /**
     * Representa la fecha de inicio del contrato
     */
    private Date fcIniContrato;

    /**
     * Representa la fecha fin del contrato
     */
    private Date fcFinContrato;
    
    /**
     * Representa el campo estadoRevCont
     */
    private CatalogosValoresDTO estadoRevContDTO;
    
    /**
     * Campo motivoRechazo
     */
    private String motivoRechazo;
    
    
    /**
     * Area
     */
    private ConvocatoriaAreaDTO convocatoriaAreaDTO;
    
    /**
     * Subarea
     */
    private ConvocatoriaAreaSubareaDTO convocatoriaAreaSubareaDTO;
    
    
    /**
     * Representa el campo estadoRevFinan
     */
    private CatalogosValoresDTO estadoRevFinanDTO;
    
    /**
     * Submod
     */
    private ConvocatoriaSubmodDTO convocatoriaSubmodDTO;
    
    /**
     * Campo cdEregAlegaciones
     */
    private String cdEregAlegaciones;

    /**
     * Campo fcFirmaAlegaciones
     */
    private Date fcFirmaAlegaciones;

    /**
     * Representa el campo itAceptaAyuda del objeto de dominio
     */
    private String itAceptaAyuda;

    /**
     * Campo autorizado
     */
    private InteresadoDTO autorizado;
    
}
