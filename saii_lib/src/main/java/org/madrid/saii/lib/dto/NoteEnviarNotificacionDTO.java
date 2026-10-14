package org.madrid.saii.lib.dto;

import java.io.InputStream;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteEnviarNotificacionDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public NoteEnviarNotificacionDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    private String uniAdmin;

    private String asunto;

    private String cdNumIden;

    private transient InputStream adjunto;

    private String nombreAdjunto;

    private String cuerpo;

}
