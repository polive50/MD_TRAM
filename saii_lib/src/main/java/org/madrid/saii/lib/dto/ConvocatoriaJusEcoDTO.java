package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConvocatoriaJusEcoDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaJusEcoDTO() {
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
     * Representa el campo idConvocatoriaJusEco del objeto de dto
     */
    private Integer idConvocatoriaJusEco;

    /**
     * Representa el campo idConvocatoria del objeto de dto
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoConvocatoriaJusEco del objeto de dto
     */
    private CatalogosValoresDTO tipoConvocatoriaJusEcoDTO;

    /**
     * Representa el campo nmPeriodo del objeto de dto
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiEconomica del objeto de dto
     */
    private Date fcIniJustiEconomica;

    /**
     * Representa el campo fcFinJustiEconomica del objeto de dto
     */
    private Date fcFinJustiEconomica;

    /**
     * Representa el campo tlObservacion del objeto de dto
     */
    private String tlObservacion;

}
