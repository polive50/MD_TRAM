package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PersonaOrg
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class PersonaOrgDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public PersonaOrgDTO() {
        super();
        this.organismoDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idPersonaOrg
     */
    private Integer idPersonaOrg;

    /**
     * Campo idInteresado
     */
    private Integer idInteresado;

    /**
     * Campo idOrganismo
     */
    private CatalogosValoresDTO organismoDTO;

    /**
     * Campo dsOtroOrganismo
     */
    private String dsOtroOrganismo;

}
