package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificacionDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que representa el identificador de expedienteEval
     */
    private Integer idExpedienteEval;

    /**
     * Variable que representa el identificador de expediente
     */
    private Integer idExpediente;

    /**
     * Variable que representa el cdExpediente de expediente
     */
    private String cdExpediente;

    /**
     * Variable que representa el tipo de actor
     */
    private String dsTipoActor;

    /**
     * Variable que representa el tipo de identificacion
     */
    private String dsTipoIdentificacion;

    /**
     * Variable que representa el Nif del Interesado
     */
    private String dsNifInteresado;

    /**
     * Variable que representa el Nombre del Interesado
     */
    private String dsNombreInteresado;

    /**
     * Variable que representa el Apellido1 del Interesado
     */
    private String dsApellido1Interesado;

    /**
     * Variable que representa el Apellido2 del Interesado
     */
    private String dsApellido2Interesado;

    /**
     * Variable que representa la descripción del estado del
     * coordinador/evaluador
     */
    private String dsEstadoCoordinadorEvaluador;

    /**
     * Variable que representa la constante del estado del
     * coordinador/evaluador
     */
    private Integer nmConstanteEstadoCoordinadorEvaluador;

    /**
     * Variable que representa el indicador si es coordinador o evaluador
     */
    private String itCoorEval;

    /**
     * Variable que representa la fecha de presentación EREG
     */
    private String fcPresentacionEreg;

    /**
     * Variable que representa el identificador de la convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Variable que representa el acronimo de la convocatoria
     */
    private String cdAcronimo;

    /**
     * Variable que represente la descripción de la convocatoria
     */
    private String dsDescripcion;

    /**
     * Variable que representa el área cientifica de la convocatoria a la cual
     * pertenece el expediente
     */
    private Integer idAreaCientifica;

    /**
     * Variavle que representa la calificacion máxima permitida por evaluación
     */
    private Integer nmCalificacionMax;

    /**
     * Variable que representa el identificador del estado del expediente
     */
    private Integer idEstadoExpediente;

    /**
     * Variable que representa la descripcion del estado del expediente
     */
    private String dsEstadoExpediente;

    /**
     * Variable que representa el mensaje de la notificación
     */
    private String mensajeNotificacion;

    /**
     * Variable que representa la calificación final de la evaluación
     */
    private Integer nmNotaFinal;

    /**
     * Variable que representa el comentario final de la evaluación
     */
    private String dsComentarioFinal;

    /**
     * Variable que representa el estado expedienteEval
     */
    private Integer idEstadoExpedienteEval;

    /**
     * Variable que representa la causa de denegación de la evaluación
     */
    private Integer idCausaDenegacionEvaluacion;
    
    /**
     * variable que representa si el registro fue rechazado por el coordinador o gestor
     */
    private String itRezachado;
    
    /**
     * Variable que representa el identificador de expedienteEvalJustec
     */
    private Integer idExpedienteEvalJusTec;
    
    private Integer idExpedienteJustec;

    private String  obsEval;

    private Integer idTipoInforme;

    private String dsTipoInforme;

    private Integer idInforme;

    private Integer idEstadoInforme;

}
