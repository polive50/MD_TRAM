package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de EvaluacionNota
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionJustecNotaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionJustecNotaDTO() {
        super();
        this.notaDTO = new CatalogosValoresDTO();
        this.esBajaBd = Boolean.FALSE;
    }

    /**
     * Constructor con argumentos
     */
    public EvaluacionJustecNotaDTO(Integer nmRangoNotaDe, Integer nmRangoNotaA) {
        this.notaDTO = new CatalogosValoresDTO();
        this.nmDe = nmRangoNotaDe;
        this.nmA = nmRangoNotaA;
        this.esBajaBd = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idEvaluacionNota
     */
    private Integer idEvaluacionJustecNota;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idNota
     */
    private CatalogosValoresDTO notaDTO;

    /**
     * Campo idEvaluacion
     */
    private Integer idEvaluacionJustec;

    /**
     * Campo nmDe
     */
    private Integer nmDe;

    /**
     * Campo nmA
     */
    private Integer nmA;

    /**
     * Variable auxiliar para indicar si el registro se dará de baja en la base
     * de datos
     */
    @JsonIgnore
    private Boolean esBajaBd;

}
