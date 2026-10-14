package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedientePresupHist
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedientePresupHistDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedientePresupHistDTO() {
        super();
        this.lstExpedientePresMontHistDTO = new ArrayList<>();
        this.estadoDTO = new CatalogosValoresDTO();

    }
    
 // Constructor de copia
    public ExpedientePresupHistDTO(ExpedientePresupHistDTO original) {
        this.idExpedientePresupHist = original.getIdExpedientePresupHist();
        this.idExpedientePresup = original.getIdExpedientePresup();
        this.tlObservacion = original.getTlObservacion();
        this.tlConcepto = original.getTlConcepto();
        this.nmAyuda = original.getNmAyuda();
        this.idExpediente = original.getIdExpediente();
        this.nmPresup = original.getNmPresup();
        this.tipoFinanciacionDTO = original.getTipoFinanciacionDTO();
        this.tematicaDTO = original.getTematicaDTO();
        this.lstExpedientePresMontHistDTO = new ArrayList<>();
        this.estadoDTO = original.getEstadoDTO();
        this.itNuevoConceptoFinan = original.getItNuevoConceptoFinan();
        this.nmPorcentajeIntensidad = original.getNmPorcentajeIntensidad();
        
        for (ExpedientePresMontHistDTO monto : original.getLstExpedientePresMontHistDTO()) {
            this.lstExpedientePresMontHistDTO.add(new ExpedientePresMontHistDTO(monto));
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
     * Campo idExpedientePresupHist
     */
    private Integer idExpedientePresupHist;

    /**
     * Campo idExpedientePresup
     */
    private Integer idExpedientePresup;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idTipoFinanciacion
     */
    private CatalogosValoresDTO tipoFinanciacionDTO;

    /**
     * Campo idTematica
     */
    private CatalogosValoresDTO tematicaDTO;

    /**
     * Campo tlObservacion
     */
    private String tlObservacion;

    /**
     * Campo nmImporte
     */
    private Double nmAyuda;
    
    /**
     * Representa el campo nmPresup del objeto de dominio
     */
    private Integer nmPresup;
    
    /**
     * Campo tlConcepto
     */
    private String tlConcepto;
    
    /**
     * Variable que representa la lista de montos
     */
    private List<ExpedientePresMontHistDTO> lstExpedientePresMontHistDTO;
    
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

}
