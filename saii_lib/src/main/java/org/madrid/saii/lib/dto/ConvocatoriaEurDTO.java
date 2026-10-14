package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEur
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaEurDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaEurDTO() {
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
     * Campo idConvocatoriaEur
     */
    private Integer idConvocatoriaEur;

    /**
     * Campo convocatoriaDTO
     */
    private ConvocatoriaDTO convocatoriaDTO;

    /**
     * Campo fondoEuropeoDTO
     */
    private CatalogosValoresDTO fondoEuropeoDTO;
}
