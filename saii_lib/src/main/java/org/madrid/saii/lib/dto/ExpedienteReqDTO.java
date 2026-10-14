package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteReq
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteReqDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteReqDTO() {
        super();
        this.expedienteDTO = new ExpedienteDTO();
        this.requisitoDTO = new CatalogosValoresDTO();
        this.documentoPtfrDTO = new DocumentoPtfrDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteReq
     */
    private Integer idExpedienteReq;

    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * Campo requisitoDTO
     */
    private CatalogosValoresDTO requisitoDTO;

    /**
     * Campo itCumple
     */
    private String itCumple;

    @JsonIgnore
    private String itCumpleMod;

    /**
     * Campo tlObservaciones
     */
    private String tlObservaciones;
    

    @JsonIgnore
    private String tlObservacionesMod;

    private DocumentoPtfrDTO documentoPtfrDTO;

    private Integer idExpedienteDoc;
    
    private String tlObservacionesCiudadano;
    
    @JsonIgnore
    private Integer tipoDocumento;
    
    
    private Boolean editable;

}
