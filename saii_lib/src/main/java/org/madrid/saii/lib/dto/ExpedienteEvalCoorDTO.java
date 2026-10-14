package org.madrid.saii.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteEvalCoorDTO {

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo cdNumIden del objeto de dominio
     */
    private String cdNumIden;

    /**
     * Representa el campo itCoorEval del objeto de dominio
     */
    private String itCoorEval;

}
