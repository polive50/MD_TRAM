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

    private Integer idExpediente;

    private String linea;

    private String nombreEmpresa;

    private String numeroExpediente;
    
    private String estadoExpediente;

    private String nif;

    /**
     * Constructor.
     */
    public ResolucionConcesionDTO() {
        super();
    }
    
    public ResolucionConcesionDTO(Integer idExpediente, String linea, String nombreEmpresa,
            String numeroExpediente, String estadoExpediente, String nif) {
        this(linea, nombreEmpresa, numeroExpediente, estadoExpediente, nif);
        this.idExpediente = idExpediente;
    }

    /**
     * Constructor con los datos mostrados en el listado.
     *
     * @param linea línea de ayuda
     * @param nombreEmpresa nombre o razón social
     * @param numeroExpediente número de expediente
     * @param estadoExpediente descripción del estado
     * @param nif NIF de la empresa
     */
    public ResolucionConcesionDTO(String linea, String nombreEmpresa,
            String numeroExpediente, String estadoExpediente, String nif) {
        this.linea = linea;
        this.nombreEmpresa = nombreEmpresa;
        this.numeroExpediente = numeroExpediente;
        this.estadoExpediente = estadoExpediente;
        this.nif = nif;
    }
}
