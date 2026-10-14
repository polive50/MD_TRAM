package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaAreaSubarea
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ConvocatoriaAreaSubareaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaAreaSubareaDTO() {
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
     * Campo idConvocatoriaAreaSubarea
     */
    private Integer idConvocatoriaAreaSubarea;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoriaArea;

    /**
     * Campo idSubarea
     */
    private CatalogosValoresDTO subareaDTO;

}
