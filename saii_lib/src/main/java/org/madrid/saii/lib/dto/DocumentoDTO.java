package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Documento
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class DocumentoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public DocumentoDTO() {
        super();
        this.esDocAdicional = Boolean.FALSE;
        this.esSubsanacionAdmin = Boolean.FALSE;
        this.esSubsanacionReq = Boolean.FALSE;
        this.esAlegacion = Boolean.FALSE;
        this.enEreg = Boolean.FALSE;
        this.esPresentacionContrato= Boolean.FALSE;
        this.esSubsanacionContrato = Boolean.FALSE;
        this.itAutoriza=null;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo nombreFichero
     */
    private String nombreFichero;

    /**
     * Campo cdNumIden
     */
    private String cdNumIden;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idExpedienteDoc
     */
    private Integer idExpedienteDoc;

    /**
     * Campo idTipoDoc
     */
    private Integer idTipoDoc;

    /**
     * Campo tipoDoc
     */
    private String tipoDoc;

    /**
     * Variable que representa si el documento viene el formulario de documentos
     * adicionales
     */
    private Boolean esDocAdicional;

    /**
     * Variable que representa si el documento viene el formulario de
     * subsanaciín administrativa
     */
    private Boolean esSubsanacionAdmin;

    /**
     * Variable que representa si el documento viene el formulario de
     * subsanaciín de requisitos
     */
    private Boolean esSubsanacionReq;

    /**
     * Variable que representa si el documento viene el formulario de alegación
     */
    private Boolean esAlegacion;

    /**
     * Variable que representa las observaciones asociadas a un documento
     * adjunto
     */
    private String tlObservaciones;

    /**
     * Campo que indica si un documento está en ereg
     */
    private Boolean enEreg;

    /**
     * Campo identificador del tramite
     */
    private Integer idTramite;

    /**
     * Campo identificador del asunto (trámite de notificación del ciudadano)
     */
    private Integer idAsunto;

    /***********************************
     * CAMPOS FICA***
     ***********************************/

    private Integer cdFica;

    /**
     * FicaFichero. Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient FicaFichero ficaFichero;

    /**
     * FicaMetadatos. Se obtiene de fica, no de BBDD
     *
     */
    @JsonIgnore
    private transient FicaMetadatos ficaMetadatos;

    /**
     * Contenido del fichero.Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient byte[] contenidoFichero;
    
    @JsonIgnore
    private  Boolean esSubsanacionContrato;
    
    @JsonIgnore
    private  Boolean esPresentacionContrato;

    private String itAutoriza;
    
}
