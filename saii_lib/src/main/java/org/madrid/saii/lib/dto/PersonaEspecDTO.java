package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PersonaEspecialidad
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class PersonaEspecDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public PersonaEspecDTO() {
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
     * Campo idPersAreaEspe
     */
    private Integer idPersonaEspec;

    /**
     * Campo interesadoDTO
     */
    private Integer idInteresado;

    /**
     * Campo dsEspecialidad
     */
    private String dsEspecialidad;

}
