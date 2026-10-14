package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConvocatoriaJusTecHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaJusTecHistDTO() {
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
     * Representa el campo idConvocatoriaJusTec del objeto de dto
     */
    private Integer idConvocatoriaJusTecHist;

    /**
     * Representa el campo idConvocatoriaJusTec del objeto de dto
     */
    private Integer idConvocatoriaJusTec;

    /**
     * Representa el campo idConvocatoria del objeto de dto
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoConvocatoriaJusTecDTO del objeto de dto
     */
    private CatalogosValoresDTO tipoConvocatoriaJusTecDTO;

    /**
     * Representa el campo nmPeriodo del objeto de dto
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiCienTec del objeto de dto
     */
    private Date fcIniJustiCienTec;

    /**
     * Representa el campo fcFinJustiCienTec del objeto de dto
     */
    private Date fcFinJustiCienTec;

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
