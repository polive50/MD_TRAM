package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para los expedientes incluidos en la resolución de exclusión o
 * desistimiento.
 *
 * @author Madrid Digital
 * @version 1.0.0
 */
@Getter
@Setter
public class ResolucionExclusionDesistimientoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public ResolucionExclusionDesistimientoDTO() {
        super();
    }
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;
    
    /** Identificador del expediente. */
    private Integer idExpediente;

    /** Línea de ayuda de la convocatoria. */
    private String linea;

    /** Nombre o razón social de la empresa. */
    private String nombreEmpresa;

    /** Número de expediente. */
    private String numeroExpediente;
    
    /** Estado del expediente. */
    private String estadoExpediente;

    /** NIF de la empresa. */
    private String nif;

    /** Código del requisito que no cumple. */
    private String codigoMotivo;

    /**
     * Constructor con los datos mostrados en el listado.
     *
     * @param idExpediente identificador del expediente
     * @param linea línea de ayuda
     * @param nombreEmpresa nombre o razón social
     * @param numeroExpediente número de expediente
     * @param estadoExpediente estado del expediente
     * @param nif NIF de la empresa
     * @param codigoMotivo código del requisito incumplido
     */
    public ResolucionExclusionDesistimientoDTO(Integer idExpediente, String linea,
            String nombreEmpresa, String numeroExpediente, String estadoExpediente, String nif,
            String codigoMotivo) {
        this.idExpediente = idExpediente;
        this.linea = linea;
        this.nombreEmpresa = nombreEmpresa;
        this.numeroExpediente = numeroExpediente;
        this.estadoExpediente = estadoExpediente;
        this.nif = nif;
        this.codigoMotivo = codigoMotivo;
    }
}
