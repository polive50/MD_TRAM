package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PersonaAreaCientifica
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class PersonaAreaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public PersonaAreaDTO() {
        super();
        this.areaDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idPersAreaCien
     */
    private Integer idPersonaArea;

    /**
     * Campo idInteresado
     */
    private Integer idInteresado;

    /**
     * Campo idAreaCientifica
     */
    private CatalogosValoresDTO areaDTO;

}
