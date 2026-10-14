package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Documento
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PtfrConsultaTiposDocumentoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public PtfrConsultaTiposDocumentoDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String codigo;

    private String nombre;

    private String descripcion;

    private String modulo;

}
