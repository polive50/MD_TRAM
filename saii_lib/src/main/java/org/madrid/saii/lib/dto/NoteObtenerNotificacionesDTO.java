package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteObtenerNotificacionesDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public NoteObtenerNotificacionesDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String codigoOrigen;

    private String codigoOrigenInterno;

    private String numeroAbonado;

    private String codigoAbonado;

    private String fechaPuestaDisposicion;

    private String fechaLectura;

    private String fechaNotificacion;

    private String fechaRechazo;

    private String codigoEstado;

}
