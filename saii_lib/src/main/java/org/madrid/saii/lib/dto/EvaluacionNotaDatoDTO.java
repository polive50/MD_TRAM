package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de EvaluacionNotaDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionNotaDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionNotaDatoDTO() {
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
     * Campo idEvaluacionNotaDato
     */
    private Integer idEvaluacionNotaDato;

    /**
     * Campo idExpedienteEval
     */
    private Integer idExpedienteEval;

    /**
     * Campo idEvaluacionNota
     */
    private Integer idEvaluacion;

    /**
     * Campo nmNota
     */
    private Integer nmNota;

}
