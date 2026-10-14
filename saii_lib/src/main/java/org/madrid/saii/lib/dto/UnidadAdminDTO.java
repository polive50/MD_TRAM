package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de UnidadAdmin
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class UnidadAdminDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public UnidadAdminDTO() {
        this.nivelOrgDTO = new NivelOrgDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idUnidadAdmin
     */
    private Integer idUnidadAdmin;

    /**
     * Campo cdUnidad
     */
    private String cdUnidad;

    /**
     * Campo dsUnidad
     */
    private String dsUnidad;

    /**
     * Campo cdUnidadPadre
     */
    private String cdUnidadPadre;

    /**
     * Campo dsUnidadPadre
     */
    private String dsUnidadPadre;


    /**
     * Campo idNivelOrg
     */
    private NivelOrgDTO nivelOrgDTO;

}
