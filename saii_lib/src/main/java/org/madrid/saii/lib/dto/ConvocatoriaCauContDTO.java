package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCauCont
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ConvocatoriaCauContDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCauContDTO() {
        super();
        this.causaContDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaCauCont
     */
    private Integer idConvocatoriaCauCont;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idCausaCont
     */
    private CatalogosValoresDTO causaContDTO;

}
