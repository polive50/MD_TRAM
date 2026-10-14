package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaSubmod
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaSubmodDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaSubmodDTO() {
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
     * Campo idConvocatoriaSubmod
     */
    private Integer idConvocatoriaSubmod;

    /**
     * Campo dsSubmodalidad
     */
    private String dsSubmodalidad;

    /**
     * Campo tlSubmodalidad
     */
    private String tlSubmodalidad;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

}
