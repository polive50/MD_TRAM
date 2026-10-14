package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCoorEval
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaCoorEvalDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCoorEvalDTO() {
        super();
        this.evaluadorDTO = new InteresadoDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaCoorEval
     */
    private Integer idConvocatoriaCoorEval;

    /**
     * Campo idConvocatoriaCoor
     */
    private Integer idConvocatoriaCoor;

    /**
     * Campo idEvaluador
     */
    private InteresadoDTO evaluadorDTO;
    
    /**
     * Campo idArea
     */
    private CatalogosValoresDTO areaDTO;
    
    /**
     * Campo tipo de informe
     */
    private CatalogosValoresDTO tipoInformeDTO;

}
