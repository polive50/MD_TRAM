package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaFechaTram
 *
 * @author quental
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaFechaTramDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaFechaTram
     */
    private Integer idConvocatoriaFechaTram;

    /**
     * Campo dsDescripcion
     */
    private String dsDescripcion;

    /**
     * Campo fcFechaTramite
     */
    private Date fcFechaTramite;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    public ConvocatoriaFechaTramDTO() {
        super();
        this.dsDescripcion = StringUtils.EMPTY;
    }

}
