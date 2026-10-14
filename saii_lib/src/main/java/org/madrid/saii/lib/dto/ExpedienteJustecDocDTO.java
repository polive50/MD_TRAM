package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteJustecDocDTO
 *
 * @author Quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteJustecDocDTO extends AuditoriaDTO implements Serializable {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteJustecDoc
     */
    private Integer idExpedienteJustecDoc;

    /**
     * Campo idExpedienteJusTec
     */
    private Integer idExpedienteJusTec;

    /**
     * Campo idExpedienteDoc
     */
    private Integer idExpedienteDoc;

    private Boolean cumple;
    
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
    public ExpedienteJustecDocDTO() {
        super();
    }
}
