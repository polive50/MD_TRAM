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
public class EnvioNotificacionNoteDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EnvioNotificacionNoteDTO() {
        super();
    }
    
   
 // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    public EnvioNotificacionNoteDTO(Integer idCodigo, Integer idConvocatoria,
            Integer cdNumeroExpediente, String dsDestinatario, String dsFirmante, Integer dsTramite,String dsVerificador,
            Integer nmEstado, Date fcEnvioCola, String nmEnvio, String nmTramite) {
        super();
        this.idCodigo = idCodigo;
        this.idConvocatoria = idConvocatoria;
        this.cdNumeroExpediente = cdNumeroExpediente;
        this.dsDestinatario = dsDestinatario;
        this.dsFirmante = dsFirmante;
        this.dsTramite = dsTramite;
        this.nmEstado = nmEstado;
        this.fcEnvioCola = fcEnvioCola;
        this.dsVerificador = dsVerificador;
        this.nmEnvio = nmEnvio;
        this.nmTramite = nmTramite;
                
    }


    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    private Integer idCodigo;
    
    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo cdNumeroExpediente del objeto de dominio
     */
    private Integer cdNumeroExpediente;
    
    private String dsExpediente;

    /**
     * Representa el campo dsDestinatario del objeto de dominio
     */
    private String dsDestinatario;
    
    /**
     * Representa el campo dsDestinatario del objeto de dominio
     */
    private String dsVerificador;
    
    
    private String nmEnvio;
    
    private String nmEnvioMod;
    
    private String nmTramite;
    
    /**
     * Representa el campo dsFirmante del objeto de dominio
     */
    private String dsFirmante;
    
    /**
     * Representa el campo dsTramite del objeto de dominio
     */
    private Integer dsTramite;

    /**
     * Representa el campo nmEstado del objeto de dominio
     */
    private Integer nmEstado;
    
    /**
     * Representa el campo itMantenible del objeto de dominio
     */
    private Date fcEnvioCola;
    

}
