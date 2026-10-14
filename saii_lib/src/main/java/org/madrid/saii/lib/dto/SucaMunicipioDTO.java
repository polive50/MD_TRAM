package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de SucaMunicipio
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class SucaMunicipioDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
    *
    */
    public SucaMunicipioDTO() {
        super();
        this.cdMunicipio = StringUtils.EMPTY;
        this.dsMunicipio = StringUtils.EMPTY;
        this.cdProvincia = StringUtils.EMPTY;
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
    private String cdMunicipio;

    /**
     *
     */
    private String dsMunicipio;

    /**
     *
     */
    private String cdProvincia;

}
