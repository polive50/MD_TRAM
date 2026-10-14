package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConvocatoriaAnualidadDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaAnualidadDTO() {
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
     * Representa el campo idConvocatoriaAnualidad del objeto de dto
     */
    private Integer idConvocatoriaAnualidad;

    /**
     * Representa el campo idConvocatoria del objeto de dto
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoConvocatoriaAnualidad del objeto de dto
     */
    private CatalogosValoresDTO tipoConvocatoriaAnualidadDTO;

    /**
     * Representa el campo nmAnualidad del objeto de dto
     */
    private Integer nmAnualidad;

    /**
     * Representa el campo fcIniEjecucion del objeto de dto
     */
    private Date fcIniEjecucion;

    /**
     * Representa el campo fcFinEjecucion del objeto de dto
     */
    private Date fcFinEjecucion;

    /**
     * Representa el campo tlObservacion del objeto de dto
     */
    private String tlObservacion;

}
