package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PersonaTit
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class PersonaTitDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public PersonaTitDTO() {
        super();
        this.titulacionDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idPersonaTit
     */
    private Integer idPersonaTit;

    /**
     * Campo idInteresado
     */
    private Integer idInteresado;

    /**
     * Campo idTitulacion
     */
    private CatalogosValoresDTO titulacionDTO;

    /**
     * Campo dsTitulacion
     */
    private String dsTitulacion;

}
