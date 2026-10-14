package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteModfPresupPlazosOtrosDocDTO
 *
 * @author Quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteModfPresupPlazosOtrosDocDTO extends AuditoriaDTO implements Serializable {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteContratoDoc
     */
    private Integer idExpedienteModfPresupPlazosOtrosDoc;

    /**
     * Campo expedienteModifPresPlazoOtraDTO
     */
    private ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO;

    /**
     * Campo idExpedienteDoc
     */
    private ExpedienteDocDTO expedienteDocDTO;

   
    
    /**
     * Campo nombreFicheroFica
     */
    private String nombreFicheroFica;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteModfPresupPlazosOtrosDocDTO() {
        super();
    }
}
