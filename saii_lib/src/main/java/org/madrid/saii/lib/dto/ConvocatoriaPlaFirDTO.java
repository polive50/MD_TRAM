package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaPlaFir
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaPlaFirDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaPlaFirDTO() {
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
     * Campo idConvocatoriaPlaFir
     */
    private Integer idConvocatoriaPlaFir;

    /**
     * Campo idConvocatoriaPla
     */
    private Integer idConvocatoriaPla;

    /**
     * Campo cdFirmante
     */
    private String cdFirmante;

    @JsonIgnore
    private String dsFirmante;

    @JsonIgnore
    private String asignado;

}
