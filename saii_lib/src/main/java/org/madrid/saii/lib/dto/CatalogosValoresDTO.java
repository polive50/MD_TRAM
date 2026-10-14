package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de CatalogosValores
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class CatalogosValoresDTO extends AuditoriaDTO implements Serializable {

    ///////////////////////////////////////////////
    //////////////////////// CONSTRUCTOR
    ///////////////////////////////////////////////

    public CatalogosValoresDTO() {
        super();
//        this.catalogoDTO = new CatalogosDTO();
    }

    public CatalogosValoresDTO(Integer idValor) {
        super();
        this.idValor = idValor;
//        this.catalogoDTO = new CatalogosDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idValor
     */
    private Integer idValor;

    /**
     * Campo dsValor
     */
    private String dsValor;

    /**
     * Campo cdCodigo
     */
    private String cdCodigo;

    /**
     * Campo itMantenible
     */
    @JsonIgnore
    private Boolean itMantenible;

    /**
     * Campo catalogo
     */
    private CatalogosDTO catalogoDTO;

    /**
     * Indicador si el registro esta activo y es editable
     */
    @JsonIgnore
    private Boolean registroActivoEditable;

    /**
     * Indicador si el registro esta inactivo y es editable
     */
    @JsonIgnore
    private Boolean registroInactivoEditable;
    
    private TipoResolModalidadDTO tipoResolModalidad;
    
    /**
     * Campo nmConstante
     */
    private Integer nmConstante;

    private Integer ordenPartida;

}
