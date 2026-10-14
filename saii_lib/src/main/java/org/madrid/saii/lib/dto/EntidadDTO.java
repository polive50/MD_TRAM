package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.saii.lib.domain.CatalogosValores;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Entidad
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class EntidadDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EntidadDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.estadoDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idEntidad
     */
    private Integer idEntidad;

    /**
     * Campo dsAbrev
     */
    private String dsAbrev;

    /**
     * Campo dsCentro
     */
    private String dsCentro;

    /**
     * Campo dsDepartamento
     */
    private String dsDepartamento;

    /**
     * Campo dsFax
     */
    private String dsFax;

    /**
     * Campo tlWeb
     */
    private String tlWeb;

    /**
     * Campo interesadoDTO
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;
    
    /**
     * Campo tipoEntidadDTO
     */
    private CatalogosValoresDTO tipoEntidadDTO;
}
