package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de RegistroEregDTO
 *
 * @author everis
 */
@Getter
@Setter
@ToString
public class RegistroEregDTO implements Serializable {

    ///////////////////////////////////////////////
    //////////////////////// CONSTRUCTOR
    ///////////////////////////////////////////////

    public RegistroEregDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo que representa la fecha de alta
     */
    private String fechaAlta;

    /**
     * Atributo que repreenta el código de anexo EREG
     */
    private String cdAnexoEreg;

    /**
     * Atributo que representa el código de EREG
     */
    private String cdEreg;

    /**
     * Atributo que representa el identificador del registro de lista de anexos.
     * Usado para la descarga de PDF de lista de anexos
     */
    private Integer idExpedienteDoc;

    /**
     * Atributo que representa el identificador de la solicitud. Usado para la
     * descarga de ZIP de solicitud
     */
    private Integer idSolicitud;

    /**
     * Atributo que representa si la descarga será del ZIP de solicitud
     */
    private Boolean esDescargaZipSolicitud;

    /**
     * Atributo que representa si la descarga será del ZIP de alegacion
     */
    private Boolean esDescargaZipAlegacion;

    /**
     * Atributo que representa si la descarga será del ZIP de subsanacion
     */
    private Boolean esDescargaZipSubsanacion;
    
    /**
     * Atributo que representa si la descarga será del ZIP de justificacion CT
     */
    private Boolean esDescargaZipJustec;
    
    /**
     * Atributo que representa si la descarga será del ZIP de justificacion CT
     */
    private Boolean esDescargaZipContrato;
    
    /**
     * Atributo que representa si la descarga será del ZIP de esDescargaZipModPresupPlazoOtros
     */
    private Boolean esDescargaZipModPresupPlazoOtros;
    
    /**
     * Atributo que representa si la descarga será del ZIP de gestión subsanacion requisitos
     */
    private Boolean esDescargaZipSubsanacionReq;
    
    /**
     * Atributo que representa si la descarga será del ZIP de aceptación de ayuda o su desistimiento
     */
    private Boolean esDescargaZipAceptacion;
    
    /**
     * Atributo que representa si la descarga será del ZIP de la justificación económica
     */
    private Boolean esDescargaZipJusEco;

    /**
     * Atributo que representa si la descarga será del ZIP de la notificación del ciudadano
     */
    private Boolean esDescargaZipNotificacionCiudadano;

}
