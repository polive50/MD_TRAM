package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PtfrRecuperaDocFirmadoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public PtfrRecuperaDocFirmadoDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String estado;

    private String codigoArchivo;

    private String codigoArchivoOriginal;

    private String referencia;

    private String referenciaSolicitante;

    private String pdfB64;

}
