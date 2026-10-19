package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para los expedientes incluidos en la resolución de concesión.
 */
@Getter
@Setter
public class ResolucionConcesionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Línea de ayuda de la convocatoria. */
    private String linea;

    /** Nombre o razón social de la empresa. */
    private String nombreEmpresa;

    /** Número de expediente. */
    private String numeroExpediente;

    /** NIF de la empresa. */
    private String nif;

    /** Constructor. */
    public ResolucionConcesionDTO() {
        super();
    }

    /**
     * Constructor con los datos mostrados en el listado.
     *
     * @param linea línea de ayuda
     * @param nombreEmpresa nombre o razón social
     * @param numeroExpediente número de expediente
     * @param nif NIF de la empresa
     */
    public ResolucionConcesionDTO(String linea, String nombreEmpresa,
            String numeroExpediente, String nif) {
        this.linea = linea;
        this.nombreEmpresa = nombreEmpresa;
        this.numeroExpediente = numeroExpediente;
        this.nif = nif;
    }
}
