package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de UsuarioDTO
 *
 * @author nttdata
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class UsuarioDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public UsuarioDTO() {
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
     * Campo idUsuario
     */
    private Integer idUsuario;

    /**
     * Campo cdUsuario
     */
    private String cdUsuario;

    /**
     * Campo cdUnidad
     */
    private String cdUnidad;
}
