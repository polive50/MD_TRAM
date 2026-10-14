package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de UsuarioJur
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class UsuarioJurDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public UsuarioJurDTO() {
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
     * Campo idUsuarioJur
     */
    private Integer idUsuarioJur;

    /**
     * Campo entidadJur
     */
    private InteresadoDTO entidadJurDTO;

    /**
     * Campo interesadoDTO
     */
    private InteresadoDTO interesadoDTO;
}
