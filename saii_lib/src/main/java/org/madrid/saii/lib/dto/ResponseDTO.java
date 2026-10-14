package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * DTO de respuesta.
 *
 * @author everis
 */
@Data
public class ResponseDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    public ResponseDTO() {
        super();
        this.codigo = StringUtils.EMPTY;
        this.descripcionError = StringUtils.EMPTY;
        this.error = Boolean.FALSE;
    }

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Error
     */
    private Boolean error;

    /**
     * Codigo
     */
    private String codigo;

    /**
     * Descripcion error
     */
    private String descripcionError;

}
