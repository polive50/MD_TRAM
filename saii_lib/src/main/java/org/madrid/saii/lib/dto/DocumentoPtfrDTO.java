package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de DocumentoPtfr
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class DocumentoPtfrDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public DocumentoPtfrDTO() {
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
     * Campo idDocumentoPtfr
     */
    private Integer idDocumentoPtfr;

    /** Identificador de la petición padre; cero para la primera del grupo. */
    private Integer idDocPtfrPadre;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo cdFica
     */
    private Integer cdFica;

    /**
     * Campo dsReferenciaPtfr
     */
    private String dsReferenciaPtfr;

    /**
     * Campo cdArchivoPtfr
     */
    private Integer cdArchivoPtfr;

    /**
     * Campo fcEnvioPtfr
     */
    private Date fcEnvioPtfr;

    /**
     * Campo cdArchivoPtfr
     */
    private Integer cdArchivoRespPtfr;

    /**
     * Campo fcFirmaPtfr
     */
    private Date fcFirmaPtfr;

    /**
     * Campo cdEstadoPtfr
     */
    private String cdEstadoPtfr;

    /**
     * Campo cdNote
     */
    private String cdNote;

    /**
     * Campo fcEnvioNote
     */
    private Date fcEnvioNote;

    /**
     * Campo fcLecturaNote
     */
    private Date fcLecturaNote;

    /**
     * Campo itUsuAltaNote
     */
    private String itUsuAltaNote;

    /**
     * Campo motivoRechazo
     */
    private String motivoRechazo;

    /**
     * Campo idTramite
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Campo código de estado NOTE
     */
    private String cdEstadoNote;

    /**
     * Campo fecha inicio de subsanación
     */
    private Date fcIniPlazoSubsanacion;

    /**
     * Campo fecha máxima de subsanación
     */
    private Date fcFinPlazoSubsanacion;

    /**
     * Campo indicador si inicio de subsanación por plazo vencido
     */
    private String itIniPlazoVencido;

    /**
     * Campo fecha rechazo de Note
     */
    private Date fcRechazoNote;

    /**
     * Representa el indicador si es una peticion de envio por correo
     */
    private String itCorreo;

    /**
     * Representa la fecha de envio de correo
     */
    private Date fcEnvioCorreo;

    @JsonIgnore
    private Date fcEnvio;
    
    private byte[] documentoAdjunto;
    
    private Integer idPresupuestoModif;
    
    /**
     * Representa el campo idExpedienteJusEco del objeto de dominio
     */
    private Integer idExpedienteJusEco;

}
