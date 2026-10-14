package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaPlaVer
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaPlaVerDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaPlaVerDTO() {
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
     * Campo idConvocatoriaPlaVer
     */
    private Integer idConvocatoriaPlaVer;

    /**
     * Campo idConvocatoriaPla
     */
    private Integer idConvocatoriaPla;

    /**
     * Campo cdVerificador
     */
    private String cdVerificador;

    @JsonIgnore
    private String dsVerificador;

    @JsonIgnore
    private String asignado;

}
