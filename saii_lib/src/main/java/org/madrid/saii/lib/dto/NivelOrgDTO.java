package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de NivelOrg
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class NivelOrgDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public NivelOrgDTO() {
        this.listUnidadAdmin = new ArrayList<>();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idNivelOrg
     */
    private Integer idNivelOrg;

    /**
     * Campo dsNivelOrg
     */
    private String dsNivelOrg;

    /**
     * Campo saiiUnidadAdmin
     */
    private List<UnidadAdminDTO> listUnidadAdmin;

}
