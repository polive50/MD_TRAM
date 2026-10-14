package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para finalizar el informe de evaluación del coordinador desde la tabla de informes
 */
@Getter
@Setter
public class FinalizarInformeCoordinadorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ExpedienteEvalDTO del evaluador (element) */
    private ExpedienteEvalDTO expedienteEvalDTO;

    /** Informe a finalizar */
    private InformeEvalDocDTO informeEvalDocDTO;

    /** Indica si se finaliza con informe (true) o sin informe (false) */
    private Boolean conInforme;

    /** idExpedienteEval del coordinador (expedienteFiltro.idExpedienteEval) */
    private Integer idExpedienteEval;

}
