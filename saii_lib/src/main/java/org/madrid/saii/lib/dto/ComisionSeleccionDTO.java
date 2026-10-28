package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * VMM 27/10/26 MD-3094: DTO para los expedientes del listado de la pestaña "Comisión de selección"
 * de la resolución de convocatoria.
 */
@Getter
@Setter
public class ComisionSeleccionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identificador del expediente. */
    private Integer idExpediente;

    /** Línea o modalidad de ayuda (área, subárea y submodalidad concatenadas). */
    private String linea;

    /** Razón social o nombre y apellidos del solicitante. */
    private String nombreEmpresa;

    /** Número de expediente. */
    private String numeroExpediente;

    /** NIF del solicitante. */
    private String nif;

    /** Puntuación final obtenida en la fase de evaluación. */
    private Integer puntuacion;

    /** Fecha de presentación de la solicitud (desempate de la prelación). */
    private Date fcPresentacion;

    /** Posición en la prelación de expedientes conforme a la puntuación. */
    private Integer orden;

    /** Ayuda solicitada por el solicitante (SAII_EXPEDIENTE_PRESUP.NM_AYUDA_SOL). */
    private Double ayudaSolicitada;

    /** Cuantía de subvención propuesta (ayuda concedida tras la revisión de financiación). */
    private Double cuantiaPropuesta;

    /** Suma progresiva de las cuantías propuestas conforme al orden de prelación. */
    private Double creditoAcumulado;

    /** Identificador del estado provisional del expediente. */
    private Integer idEstadoProvisional;

    /** Descripción del estado provisional del expediente. */
    private String dsEstadoProvisional;

    /** Descripción del estado del expediente. */
    private String dsEstado;

    /** Resultado de la Comisión (Pendiente de propuesta o el estado provisional). */
    private String resultadoComision;

    /** Motivo u observaciones de la Comisión. */
    private String motivo;

    /** Constructor. */
    public ComisionSeleccionDTO() {
        super();
    }
}
