package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedientePremontoHist
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedientePresMontHistDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedientePresMontHistDTO() {
        super();
        this.esMontoTotal = Boolean.FALSE;
        //this.lstExpedienteJusEcoDocDTO = new ArrayList<>();
        this.estadoDTO = new CatalogosValoresDTO();
    }
    
    // Constructor de copia
    public ExpedientePresMontHistDTO(ExpedientePresMontHistDTO original) {              
        this.idExpedientePresupMonto = original.getIdExpedientePresupMonto();
        this.idExpedientePresup = original.getIdExpedientePresup();
        this.nmAnualidad = original.getNmAnualidad();
        this.nmImporte = original.getNmImporte();
        this.esMontoTotal = original.getEsMontoTotal();
        this.nmImporteJustificado = original.getNmImporteJustificado();
        this.nmImporteAdmitido = original.getNmImporteAdmitido();
        this.estadoDTO = original.getEstadoDTO();
                
       /* this.lstExpedienteJusEcoDocDTO = new ArrayList<>();
        for (ExpedienteJusEcoDocDTO jusEcoDoc : original.getLstExpedienteJusEcoDocDTO()) {
            this.lstExpedienteJusEcoDocDTO.add(new ExpedienteJusEcoDocDTO(jusEcoDoc));
        }*/
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedientePremontoHist
     */
    private Integer idExpedientePresMontHist;

    /**
     * Campo idExpedientePresupMonto
     */
    private Integer idExpedientePresupMonto;

    /**
     * Campo idExpedientePresup
     */
    private Integer idExpedientePresup;

    /**
     * Campo nmAnualidad
     */
    private Integer nmAnualidad;

    /**
     * Campo nmImporte
     */
    private Double nmImporte;
    
    @JsonIgnore
    private Boolean esMontoTotal;
    
    /**
     * Representa el campo lstExpedienteJusEcoDoc del objeto de dominio
     */
    //private List<ExpedienteJusEcoDocDTO> lstExpedienteJusEcoDocDTO;
    
    /**
     * Representa el campo nmImporteJustificado del objeto de dominio
     */
    private Double nmImporteJustificado;
    
    /**
     * Representa el campo nmImportAdmitido del objeto de dominio
     */
    private Double nmImporteAdmitido;
    
    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValoresDTO estadoDTO;

}
