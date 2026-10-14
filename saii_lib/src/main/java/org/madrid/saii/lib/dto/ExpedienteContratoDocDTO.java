package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteContratoDocDTO
 *
 * @author Quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteContratoDocDTO extends AuditoriaDTO implements Serializable {
    
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
    private Integer idExpedienteContratoDoc;

    /**
     * Campo idExpedienteContrato
     */
    private ExpedienteContratoDTO expedienteContratoDTO;

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
    public ExpedienteContratoDocDTO() {
        super();
    }
}
