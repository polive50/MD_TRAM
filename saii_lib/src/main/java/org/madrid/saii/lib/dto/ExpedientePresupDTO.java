package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedientePresup
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedientePresupDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedientePresupDTO() {
        super();
        this.lstExpedientePresupMontoDTO = new ArrayList<>();
        this.lstExpedientePresMontHistDTO = new ArrayList<>();
        this.lstExpedienteModifPresupMontoDTO = new ArrayList<>();
        this.estadoDTO = new CatalogosValoresDTO();
        this.pendienteObservacion = Boolean.FALSE;
    }

    // Constructor de copia
    public ExpedientePresupDTO(ExpedientePresupDTO original) {
        this.idExpedientePresup = original.getIdExpedientePresup();
        this.tlObservacion = original.getTlObservacion();
        this.tlConcepto = original.getTlConcepto();
        this.nmAyuda = original.getNmAyuda();
        this.nmAyudaSol = original.getNmAyudaSol();
        this.idExpediente = original.getIdExpediente();
        this.nmPresup = original.getNmPresup();
        this.tipoFinanciacionDTO = original.getTipoFinanciacionDTO();
        this.tematicaDTO = original.getTematicaDTO();
        this.lstExpedientePresupMontoDTO = new ArrayList<>();
        this.lstExpedientePresMontHistDTO = new ArrayList<>();
        this.lstExpedienteModifPresupMontoDTO = new ArrayList<>();
        this.estadoDTO = original.getEstadoDTO();
        this.itNuevoConceptoFinan = original.getItNuevoConceptoFinan();
        this.nmPorcentajeIntensidad = original.getNmPorcentajeIntensidad();
        //this.pendienteObservacion = original.getPendienteObservacion();
        
        for (ExpedientePresupMontoDTO monto : original.getLstExpedientePresupMontoDTO()) {
            this.lstExpedientePresupMontoDTO.add(new ExpedientePresupMontoDTO(monto));
        }
        for (ExpedientePresMontHistDTO monto : original.getLstExpedientePresMontHistDTO()) {
            this.lstExpedientePresMontHistDTO.add(new ExpedientePresMontHistDTO(monto));
        }
        for (ExpedienteModifPresupMontoDTO monto : original.getLstExpedienteModifPresupMontoDTO()) {
            this.lstExpedienteModifPresupMontoDTO.add(new ExpedienteModifPresupMontoDTO(monto));
        }
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedientePresup
     */
    private Integer idExpedientePresup;

    /**
     * Campo tlObservacion
     */
    private String tlObservacion;

    /**
     * Campo nmAyuda
     */
    private Double nmAyuda;

    /**
     * VMM 27/10/26 MD-3094: ayuda solicitada por el solicitante (nmAyuda guarda la concedida)
     */
    private Double nmAyudaSol;

    /**
     * Campo expedienteDTO
     */
    private Integer idExpediente;

    /**
     * Campo nmPresup
     */
    private Integer nmPresup;
    
    /**
     * Campo tlConcepto
     */
    private String tlConcepto;

    /**
     * Campo tipoFinanciacionDTO
     */
    private CatalogosValoresDTO tipoFinanciacionDTO;

    /**
     * Campo tematicaDTO
     */
    private CatalogosValoresDTO tematicaDTO;

    /**
     * Variable que representa la lista de montos
     */
    private List<ExpedientePresupMontoDTO> lstExpedientePresupMontoDTO;
    
    /**
     * Variable que representa la lista de montos historico
     */
    private List<ExpedientePresMontHistDTO> lstExpedientePresMontHistDTO;
    
    /**
     * Variable que representa la lista de montos temporales
     */
    private List<ExpedienteModifPresupMontoDTO> lstExpedienteModifPresupMontoDTO;
    
    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValoresDTO estadoDTO;
    
    /**
     * Representa el indicador de nuevo concepto de financiacion
     */
    private String itNuevoConceptoFinan;

    /**
     * Representa el porcentaje de intensidad de ayuda aplicado al presupuesto
     */
    private Double nmPorcentajeIntensidad;

    /**
     * Indicador transitorio (no persistido): fila con importe modificado sin observacion guardada
     */
    private Boolean pendienteObservacion;
}
