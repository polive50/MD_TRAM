package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de NotificacionCiudadano
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class NotificacionCiudadanoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public NotificacionCiudadanoDTO() {
        super();
        this.expedienteDTO = new ExpedienteDTO();
        this.asuntoDTO = new CatalogosValoresDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.lstExpedienteDocDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idNotificacion
     */
    private Integer idNotificacion;

    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * Campo asuntoDTO
     */
    private CatalogosValoresDTO asuntoDTO;

    /**
     * Campo tlExpone
     */
    private String tlExpone;

    /**
     * Campo tlSolicita
     */
    private String tlSolicita;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo fcFirmaEreg
     */
    private Date fcFirmaEreg;

    /**
     * Campo cdEreg
     */
    private String cdEreg;

    /**
     * Campo dsDestinoEreg
     */
    private String dsDestinoEreg;

    /**
     * Documentos adicionales enlazados a esta notificación (SAII_NOTIFI_CIUD_DOC)
     */
    private List<ExpedienteDocDTO> lstExpedienteDocDTO;

}
