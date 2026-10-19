package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de NotificacionCiudadanoDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class NotificacionCiudadanoDocDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public NotificacionCiudadanoDocDTO() {
        super();
        this.notificacionCiudadanoDTO = new NotificacionCiudadanoDTO();
        this.expedienteDocDTO = new ExpedienteDocDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idNotificacionDoc
     */
    private Integer idNotificacionDoc;

    /**
     * Campo notificacionCiudadanoDTO
     */
    private NotificacionCiudadanoDTO notificacionCiudadanoDTO;

    /**
     * Campo expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;

}
