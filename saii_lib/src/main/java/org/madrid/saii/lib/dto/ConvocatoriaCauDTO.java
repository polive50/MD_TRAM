package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCau
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaCauDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCauDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaCau
     */
    private Integer idConvocatoriaCau;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo causaDTO
     */
    private CatalogosValoresDTO causaDTO;

    /** Campo tlNormativa */
    private String tlNormativa;
    
    /**
     * idTipoCausa
     */
    private CatalogosValoresDTO tipoCausa;
}
