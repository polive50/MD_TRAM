package org.madrid.saii.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InformeEvaluacionDTO {

    /**
     * Representa el campo convIdConvocatoria del objeto de dominio
     */
    private Integer convIdConvocatoria;

    /**
     * Representa el campo convCdTitulo del objeto de dominio
     */
    private String convCdTitulo;

    /**
     * Representa el campo expCdExpediente del objeto de dominio
     */
    private String expCdExpediente;

    /**
     * Representa el campo inteDsNombre del objeto de dominio
     */
    private String inteDsNombre;

    /**
     * Representa el campo inteDsApellido1 del objeto de dominio
     */
    private String inteDsApellido1;

    /**
     * Representa el campo inteDsApellido2 del objeto de dominio
     */
    private String inteDsApellido2;

    /**
     * Representa el campo expEvaItCoorEval del objeto de dominio
     */
    private String expEvaItCoorEval;

    /**
     * Representa el campo evalIdEvaluacion del objeto de dominio
     */
    private Integer evalIdEvaluacion;

    /**
     * Representa el campo evalDsTitulo del objeto de dominio
     */
    private String evalDsTitulo;

    /**
     * Representa el campo evalTlDescripcion del objeto de dominio
     */
    private String evalTlDescripcion;

    /**
     * Representa el campo evalNmOrden del objeto de dominio
     */
    private Integer evalNmOrden;

    /**
     * Representa el campo evalItCalificacion del objeto de dominio
     */
    private String evalItCalificacion;

    /**
     * Representa el campo evanotNmNota del objeto de dominio
     */
    private Integer evanotNmNota;

    /**
     * Representa el campo aparIdApartado del objeto de dominio
     */
    private Integer aparIdApartado;

    /**
     * Representa el campo aparDsTitulo del objeto de dominio
     */
    private String aparDsTitulo;

    /**
     * Representa el campo aparTlDescripcion del objeto de dominio
     */
    private String aparTlDescripcion;

    /**
     * Representa el campo aparTlTexto del objeto de dominio
     */
    private String aparTlTexto;

    /**
     * Representa el campo apaNmOrden del objeto de dominio
     */
    private Integer apaNmOrden;

    /**
     * Representa el campo apaNmCalificacionMax del objeto de dominio
     */
    private Integer apaNmCalificacionMax;

    /**
     * Representa el campo aparNotDsComentario del objeto de dominio
     */
    private String aparNotDsComentario;

    /**
     * Representa el campo aparNotNmNota del objeto de dominio
     */
    private Integer aparNotNmNota;

    /**
     * Representa el campo expEvaIdExpedienteEval del objeto de dominio
     */
    private Integer expEvaIdExpedienteEval;

    /**
     * Representa el campo expEvaDsComentario del objeto de dominio
     */
    private String expEvaDsComentario;

    /**
     * Representa el campo expEvaNmNotaFinal del objeto de dominio
     */
    private Integer expEvaNmNotaFinal;

    /**
     * Representa el campo expNmNota del objeto de dominio
     */
    private Integer expNmNota;
    
    private InteresadoDTO solictante;
    
    private String inteCdNumIden;

}
