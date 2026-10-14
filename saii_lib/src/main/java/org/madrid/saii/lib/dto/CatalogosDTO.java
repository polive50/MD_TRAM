package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Catalogos
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class CatalogosDTO extends AuditoriaDTO implements Serializable {

    ///////////////////////////////////////////////
    //////////////////////// CONSTRUCTOR
    ///////////////////////////////////////////////
    public CatalogosDTO() {
        super();
        this.dsNombre = StringUtils.EMPTY;
        this.tlDescripcion = StringUtils.EMPTY;
        this.listCatalogosValores = new ArrayList<>();
        this.itMantenible = StringUtils.EMPTY;
    }

    /**
     * Constructor idCatalogo
     *
     * @param idCatalogo
     */
    public CatalogosDTO(Integer idCatalogo) {
        super();
        this.idCatalogo = idCatalogo;
        this.dsNombre = StringUtils.EMPTY;
        this.tlDescripcion = StringUtils.EMPTY;
        this.listCatalogosValores = new ArrayList<>();
        this.itMantenible = StringUtils.EMPTY;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idCatalogo
     */
    private Integer idCatalogo;

    /**
     * Campo dsNombre
     */
    private String dsNombre;

    /**
     * Campo tlDescripcion
     */
    private String tlDescripcion;

    /**
     * Campo saiiCatalogosValores
     */
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CatalogosValoresDTO> listCatalogosValores;

    /**
     * Campo itMantenible
     */
    private String itMantenible;
    
    /**
     * Campo nmConstante
     */
    private Integer nmConstante;

}
