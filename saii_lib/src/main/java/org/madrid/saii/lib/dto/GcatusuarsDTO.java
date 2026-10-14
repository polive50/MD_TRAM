package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpeT046
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class GcatusuarsDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public GcatusuarsDTO() {
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
     * Campo cdusuario
     */
    private String cdusuario;

    /**
     * Campo cdunidad
     */
    private String cdunidad;

    /**
     * Campo dsnombre
     */
    private String dsnombre;

}
