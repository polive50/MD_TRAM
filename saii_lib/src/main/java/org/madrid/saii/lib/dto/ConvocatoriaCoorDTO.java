package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCoor
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaCoorDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCoorDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.areaDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaCoor
     */
    private Integer idConvocatoriaCoor;

    /**
     * Campo idInteresado
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idArea
     */
    private CatalogosValoresDTO areaDTO;

    /**
     * Campo cdAcronimo
     */
    private String cdAcronimo;
    
    /**
     * Campo tipo de informe
     */
    private CatalogosValoresDTO tipoInformeDTO;

}
