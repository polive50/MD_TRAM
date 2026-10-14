package org.madrid.saii.lib.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * DTO de RepresentanteJur
 *
 * @author quental
 * @version 1.0.0
 */
@Data
public class RevisionInfEvaJustecHistDTO implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * campo IdRevInfEvaJustec 
     */
    private Integer idRevInfEvaJustecHist;

    /**
     * campo revisionInfEvaJustec 
     */
    
    private RevisionInfEvaJustecDTO revisionInfEvaJustecDTO;
    /**
     * campo expediente 
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * campo expedienteJusTec 
     */
    private ExpedienteJusTecDTO expedienteJusTecDTO;
    
    
    /**
     * campo expedienteDoc 
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    /**
     * campo idEstado 
     */
    private CatalogosValoresDTO idEstadoDTO;
    
    /**
     * campo tlObservaciones 
     */
    private String tlObservaciones;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public RevisionInfEvaJustecHistDTO() {
        super();
        this.revisionInfEvaJustecDTO=new RevisionInfEvaJustecDTO();
        this.expedienteDocDTO=new ExpedienteDocDTO();
        this.expedienteDTO=new ExpedienteDTO();
        this.expedienteJusTecDTO= new ExpedienteJusTecDTO();
        this.idEstadoDTO=new CatalogosValoresDTO();
    }
}
