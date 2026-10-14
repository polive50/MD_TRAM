package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de RepresentanteJur
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class RepresentanteJurDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public RepresentanteJurDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.entidadDTO = new EntidadDTO();
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
     * Campo idRepresentanteJur
     */
    private Integer idRepresentanteJur;

    /**
     * Campo fcVigenciaIni
     */
    private Date fcVigenciaIni;

    /**
     * Campo fcVigenciaFin
     */
    private Date fcVigenciaFin;

    /**
     * Campo entidadDTO
     */
    private EntidadDTO entidadDTO;

    /**
     * Campo interesadoDTO
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo dsCargo
     */
    private String dsCargo;
}
