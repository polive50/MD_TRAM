package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEnl
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaEnlDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idEnlace
     */
    private Integer idEnlace;

    /**
     * Campo dsDescripcion
     */
    private String dsDescripcion;

    /**
     * Campo tlEnlace
     */
    private String tlEnlace;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    public ConvocatoriaEnlDTO() {
        super();
        this.dsDescripcion = StringUtils.EMPTY;
        this.tlEnlace = StringUtils.EMPTY;
    }

}
