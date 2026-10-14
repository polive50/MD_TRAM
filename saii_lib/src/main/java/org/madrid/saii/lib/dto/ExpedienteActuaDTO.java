package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteActua
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedienteActuaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteActuaDTO() {
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
     * Campo idExpedienteActua
     */
    private Integer idExpedienteActua;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idTipoFinanciacion
     */
    private CatalogosValoresDTO tipoFinanciacionDTO;

    /**
     * Campo dsProveedor
     */
    private String dsProveedor;

    /**
     * Campo nmImporte
     */
    private Double nmImporte;

    /**
     * Campo tlObservacion
     */
    private String tlObservacion;

    /**
     * Campo itMemoria
     */
    // private String itMemoria;

    /**
     * Campo nmAyuda
     */
    private Double nmAyuda;

    /* montoTotal */
    @JsonIgnore
    private Boolean esMontoTotal;

}
