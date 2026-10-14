package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SucaPaisDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     *
     */
    public SucaPaisDTO() {
        super();
        this.cdPais = StringUtils.EMPTY;
        this.dsPais = StringUtils.EMPTY;
        this.dsGentilicioM = StringUtils.EMPTY;
        this.dsGentilicioF = StringUtils.EMPTY;

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    public static final long serialVersionUID = 1L;

    /**
     *
     */
    private String cdPais;

    /**
     *
     */
    private String dsPais;

    /**
     *
     */
    private String dsGentilicioM;

    /**
     *
     */
    private String dsGentilicioF;


}
