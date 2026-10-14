package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteModifOtra
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteModifOtraDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ExpedienteModifOtraDTO() {
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
     * Campo idExpedienteModifOtra
     */
    private Integer idExpedienteModifOtra;

    /**
     * Campo expedienteModifPresPlazoOtraDTO
     */
    private ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO;
    
    /**
     * Campo otras modificaciones
     */
    private String tlOtrasModif;
    
    /**
     * Campo otras modificaciones
     */
    private String tlOtrasModifObservaciones;
    
       
}