package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PtfrConsultaFirmantesTipoDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PtfrConsultaFirmantesTipoDocDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public PtfrConsultaFirmantesTipoDocDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String tipo;

    private String cargo;

    private String etapa;

    private String codUsu;

    private String nombre;

    private String rol;

}
