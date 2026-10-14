package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEvalJustec
 *
 * @author Quental
 */
@Getter
@Setter
@ToString
public class ConvocatoriaEvalJustecDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaEvalJustecDTO() {
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
     * Campo idConvocatoriaEvalJustec
     */
    private Integer idConvocatoriaEvalJustec;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idEvaluador
     */
    private InteresadoDTO interesadoDTO;
    
    /**
     * Campo idArea
     */
    private CatalogosValoresDTO areaDTO;

    /**
     * Campo cdAcronimo
     */
    private String cdAcronimo;
    
    /**
     * Listado de Areas Cientificas que pertenece una persona
     */
    private List<PersonaAreaDTO> listaPersonaAreaDTO;
    
    /**
     * Representa la lista de areas cientificas asociadas a la personas
     */
    @JsonIgnore
    private String areasCientificas;

}
