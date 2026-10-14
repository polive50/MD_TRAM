package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de EvaluacionJustecNotaDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionJustecNotaDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionJustecNotaDatoDTO() {
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
     * Campo idEvaluacionJustecNotaDato
     */
    private Integer idEvaluacionJustecNotaDato;

    /**
     * Campo idExpedienteEvalJustec
     */
    private Integer idExpedienteEvalJustec;

    /**
     * Campo idEvaluacionJustecNota
     */
    private Integer idEvaluacionJustec;

    /**
     * Campo nmNota
     */
    private Integer nmNota;

}
