package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Datos del listado de situación de evaluadores.
 */
@Getter
@Setter
public class ListadoSituacionEvaluadoresDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idExpediente;

    private String cdExpediente;

    private String nifSolicitante;

    private String solicitante;

    private String nifCoordinador;

    private String coordinador;

    private String estadoCoordinador;

    private String organismoCoordinador;

    private String nifEvaluador;

    private String evaluador;

    private String estadoEvaluador;

    private String tipoInforme;

    private String notaFinal;
}
