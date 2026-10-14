package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PtfrConsultaFirmantes
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PtfrConsultaFirmantesDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public PtfrConsultaFirmantesDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String codUsu;

    private String etapa;

    private String nombre;

    private String cargo;

    private String tipo;

}
