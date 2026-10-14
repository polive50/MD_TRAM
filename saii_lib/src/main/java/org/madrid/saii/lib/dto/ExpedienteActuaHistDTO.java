package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteActuaHist
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedienteActuaHistDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteActuaHistDTO() {
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
     * Campo idExpedienteActuaHist
     */
    private Integer idExpedienteActuaHist;

    /**
     * Campo idExpedienteActua
     */
    private Integer idExpedienteActua;

    /**
     * Campo idExpediente
     */
    private ExpedienteDTO expedienteDTO;

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
     * Campo nmAyuda
     */
    private Double nmAyuda;

}
