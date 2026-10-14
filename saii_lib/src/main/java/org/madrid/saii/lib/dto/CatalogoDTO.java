package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de para objetos id / descripcion.
 *
 * @author everis
 */
@Getter
@Setter
@ToString
public class CatalogoDTO implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Codigo
     */
    private String codigo;

    /**
     * Descripcion
     */
    private String descripcion;

    /**
     * Constructor
     */
    public CatalogoDTO() {
        super();
        this.codigo = StringUtils.EMPTY;
        this.descripcion = StringUtils.EMPTY;
    }

}
