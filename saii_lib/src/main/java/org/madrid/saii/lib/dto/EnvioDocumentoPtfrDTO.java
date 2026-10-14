package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Apartado
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EnvioDocumentoPtfrDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    private static final long serialVersionUID = -4484003691676886951L;


    /**
     * Constructor sin argumentos
     */
    public EnvioDocumentoPtfrDTO() {
        super();
    }
    
   
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    public EnvioDocumentoPtfrDTO(Integer idDocumentoPTFR, Integer idConvocatoria,
            Integer idExpediente, String dsReferenciaPTFR, Integer cdArchivoPTFR, Date fcEnvioCola, Date fcEnvioPTFR, Date fcFirmaPTFR,
            String nmEnvio, String cdEstadoPTFR, Integer idTramite, String cdNote, Date fcLecturaNote,
            Date fcEnvioNote, String cdEstadoNote, String dsDestinatario, String dsFirmante,
            String dsVerificador, String dsTramite, String dsExpediente,Integer idExpedienteJustec,Date fcRechazoNote, Integer idExpedienteJusEco) {
        super();
        this.idDocumentoPTFR = idDocumentoPTFR;
        this.idConvocatoria = idConvocatoria;
        this.idExpediente = idExpediente;
        this.dsReferenciaPTFR = dsReferenciaPTFR;
        this.cdArchivoPTFR = cdArchivoPTFR;
        this.fcEnvioCola = fcEnvioCola;
        this.nmEnvio = nmEnvio;
        this.fcEnvioPTFR = fcEnvioPTFR;
        this.fcFirmaPTFR = fcFirmaPTFR;
        this.cdEstadoPTFR = cdEstadoPTFR;
        this.idTramite = idTramite;
        this.cdNote = cdNote;
        this.fcLecturaNote = fcLecturaNote;
        this.fcEnvioNote = fcEnvioNote; 
        this.cdEstadoNote = cdEstadoNote;
        this.dsDestinatario = dsDestinatario;
        this.dsFirmante = dsFirmante;
        this.dsVerificador = dsVerificador;
        this.dsTramite = dsTramite;
        this.dsExpediente = dsExpediente;
        this.idExpedienteJustec = idExpedienteJustec;
        this.fcRechazoNote = fcRechazoNote;
        //VMM 17/10/25 MD-1586: incluimos el idExpedienteJusEco
        this.idExpedienteJusEco = idExpedienteJusEco;
    }

   
    /**
     * Representa el campo idDocumentoPTFR del objeto de dominio
     */
    private Integer idDocumentoPTFR;
    
  


    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;
    
    
    /**
     * Representa el campo dsReferenciaPTFR del objeto de dominio
     */
    private String dsExpediente;
    
    
    /**
     * Representa el campo dsReferenciaPTFR del objeto de dominio
     */
    private String dsReferenciaPTFR;
    
    /**
     * Representa el campo cdArchivoPTFR del objeto de dominio
     */
    private Integer cdArchivoPTFR;
    
    /**
     * Representa el campo fcEnvioCola del objeto de dominio
     */
    private Date fcEnvioCola;
    
    /**
     * Representa el campo nmEnvio del objeto de dominio
     */
    private String nmEnvio;
    
    /**
     * Representa el campo dcEnvioPTFR del objeto de dominio
     */
    private Date fcEnvioPTFR;
    
    /**
     * Representa el campo fcFirmaPTFR del objeto de dominio
     */
    private Date fcFirmaPTFR;
    
    /**
     * Representa el campo cdEstadoPTFR del objeto de dominio
     */
    private String cdEstadoPTFR;

    
    /**
     * Representa el campo idTramite del objeto de dominio
     */
    private Integer idTramite;
    
    
    /**
     * Representa el campo cdNote del objeto de dominio
     */
    private String cdNote;
    
    /**
     * Representa el campo dcEnvioPTFR del objeto de dominio
     */
    private Date fcLecturaNote;
    
  
    /**
     * Representa el campo dcEnvioPTFR del objeto de dominio
     */
    private Date fcEnvioNote;
    
    /**
     * Representa el campo cdEstadoNote del objeto de dominio
     */
    private String cdEstadoNote;
    /**
     * Representa el campo dsDestinatario del objeto de dominio
     */
    private String dsDestinatario;
    
    /**
     * Representa el campo dsFirmante del objeto de dominio
     */
    private String dsFirmante;
    
    /**
     * Representa el campo dsFirmante del objeto de dominio
     */
    private String dsVerificador;
   
    
    private String dsTramite;
    
    
    private Integer idExpedienteJustec;
    
    private Date fcRechazoNote;

    private Integer idExpedienteJusEco;
}
