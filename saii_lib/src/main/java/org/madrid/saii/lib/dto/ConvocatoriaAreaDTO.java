package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaArea
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaAreaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaAreaDTO() {
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
     * Campo idConvocatoriaArea
     */
    private Integer idConvocatoriaArea;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idArea
     */
    private CatalogosValoresDTO areaDTO;
    
    private List<ConvocatoriaAreaSubareaDTO> convocatoriaAreaSubareaDTO;

}
