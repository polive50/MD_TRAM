package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteJusEcoDocDTO
 *
 * @author Quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteJusEcoDocDTO extends AuditoriaDTO implements Serializable {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteJusEcoDoc
     */
    private Integer idExpedienteJusEcoDoc;

    /**
     * Campo expedienteJusEcoDTO
     */
    private ExpedienteJusEcoDTO expedienteJusEcoDTO;

    /**
     * Campo expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;

    /**
     * Campo cumple
     */
    private Boolean cumple;
    
    /**
     * Campo nombreFicheroFica
     */
    private String nombreFicheroFica;
    
    /**
     * Campo expedientePresupMontoDTO
     */
    private ExpedientePresupMontoDTO expedientePresupMontoDTO;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteJusEcoDocDTO() {
        super();
        this.expedienteJusEcoDTO = new ExpedienteJusEcoDTO();
        this.expedienteDocDTO = new ExpedienteDocDTO();
        this.expedientePresupMontoDTO = new ExpedientePresupMontoDTO();
    }
    
    //constructor de copia
    public ExpedienteJusEcoDocDTO(ExpedienteJusEcoDocDTO original) {
        this.idExpedienteJusEcoDoc = original.getIdExpedienteJusEcoDoc();
        this.expedienteJusEcoDTO = original.getExpedienteJusEcoDTO();
        this.expedienteDocDTO = original.getExpedienteDocDTO();
        this.expedientePresupMontoDTO = original.getExpedientePresupMontoDTO();
        this.cumple= original.getCumple();
        this.nombreFicheroFica = original.getNombreFicheroFica();
    }
}
