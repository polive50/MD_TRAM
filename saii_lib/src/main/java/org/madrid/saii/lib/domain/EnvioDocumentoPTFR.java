package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio CatalogosValores
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_V_ENVIO_NOTIFICACION")
@AtlasAuditarFila
@ToString
public class EnvioDocumentoPTFR implements Serializable {

   
    /**
     * 
     */
    private static final long serialVersionUID = 770035714450599856L;

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
     * Representa el campo cdExpediente del objeto de dominio
     */
    private String cdExpediente;
    
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
     * Representa el campo cdEstadoPTFR del objeto de dominio
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
     * Representa el campo cdEstadoPTFR del objeto de dominio
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
    
    
    private String itCorreo;

    
    /**
     * Representa el campo idExpedienteJustec del objeto de dominio
     */
    private Integer idExpedienteJustec;
    
    
    /**
     * Representa el campo fcRechazoNote del objeto de dominio
     */
    private Date fcRechazoNote;
    
    /**
     * Representa el campo idExpedienteJusEco del objeto de dominio
     */
    private Integer idExpedienteJusEco;
    
    /**
     * Representa el campo cdFicaAcuseReciboNote del objeto de dominio
     */
    private Integer cdFicaAcuseReciboNote;
    
    /**
     * Representa el campo idDocPtfrPadre del objeto de dominio
     */
    private Integer idDocPtfrPadre;
 
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EnvioDocumentoPTFR() {
        super();
    }

    public EnvioDocumentoPTFR(Integer idDocumentoPTFR, Integer idConvocatoria,
            Integer idExpediente, String cdExpediente, String dsDestinatario, String dsFirmante, Integer idTramite,
            String cdUsuAlta, Date fcAltaFila,
            String cdUsuModif, Date fcModifFila, String cdUsuBaja, Date fcBajaFila, String dsTramite, String itCorreo, Integer idExpedienteJustec, Integer idExpedienteJusEco) {
        super();
        this.idDocumentoPTFR = idDocumentoPTFR;
        this.idConvocatoria = idConvocatoria;
        this.idExpediente = idExpediente;
        this.cdExpediente = cdExpediente;
        this.dsDestinatario = dsDestinatario;
        this.dsFirmante = dsFirmante;
        this.idTramite = idTramite;
        this.dsTramite = dsTramite;
        this.itCorreo = itCorreo;
        this.idExpedienteJustec = idExpedienteJustec;
        this.idExpedienteJusEco = idExpedienteJusEco;
    }
    
    @Id
    @Column(name = "ID_DOCUMENTO_PTFR")
    public Integer getidDocumentoPTFR() {
        return this.idDocumentoPTFR;
    }
    
    public void setidDocumentoPTFR(Integer idDocumentoPTFR) {
        this.idDocumentoPTFR = idDocumentoPTFR;
    }


    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

   @Column(name = "ID_EXPEDIENTE")
    public Integer getidExpediente() {
        return idExpediente;
    }

   public void setidExpediente(Integer idExpediente) {
       this.idExpediente = idExpediente;
   }
   
   @Column(name = "CD_EXPEDIENTE")
   public String getCdExpediente() {
       return cdExpediente;
   }

   public void setCdExpediente(String cdExpediente) {
      this.cdExpediente = cdExpediente;
   }
   
   @Column(name = "DS_REFERENCIA_PTFR")
   public String getDsReferenciaPTFR() {
       return dsReferenciaPTFR;
   }

   public void setDsReferenciaPTFR(String dsReferenciaPTFR) {
       this.dsReferenciaPTFR = dsReferenciaPTFR;
   }
   
   
   
   @Column(name = "CD_ARCHIVO_PTFR")
   public Integer getCdArchivoPTFR() {
       return cdArchivoPTFR;
   }

  public void setCdArchivoPTFR(Integer cdArchivoPTFR) {
      this.cdArchivoPTFR = cdArchivoPTFR;
  }
  
  @Column(name = "FC_ENVIO_COLA")
  public Date getFcEnvioCola() {
      return fcEnvioCola;
  }
  
  public void setFcEnvioCola(Date fcEnvioCola) {
      this.fcEnvioCola = fcEnvioCola;
  } 

  @Column(name = "FC_ENVIO_PTFR")
  public Date getFcEnvioPTFR() {
      return fcEnvioPTFR;
  }

  public void setFcEnvioPTFR(Date fcEnvioPTFR) {
      this.fcEnvioPTFR = fcEnvioPTFR;
  }
  
  
  @Column(name = "FC_FIRMA_PTFR")
  public Date getFcFirmaPTFR() {
      return fcFirmaPTFR;
  }

  public void setFcFirmaPTFR(Date fcFirmaPTFR) {
      this.fcFirmaPTFR = fcFirmaPTFR;
  }
  
  
  @Column(name = "CD_ESTADO_PTFR")
  public String getCdEstadoPTFR() {
      return cdEstadoPTFR;
  }

  public void setCdEstadoPTFR(String cdEstadoPTFR) {
      this.cdEstadoPTFR = cdEstadoPTFR;
  }
  
  @Column(name = "CD_NOTE")
  public String getCdNote() {
      return cdNote;
  }

  public void setCdNote(String cdNote) {
      this.cdNote = cdNote;
  }

  @Column(name = "FC_LECTURA_NOTE")
  public Date getFcLecturaNote() {
      return fcLecturaNote;
  }

  public void setFcLecturaNote(Date fcLecturaNote) {
      this.fcLecturaNote = fcLecturaNote;
  }
  
  @Column(name = "FC_ENVIO_NOTE")
  public Date getFcEnvioNote() {
      return fcEnvioNote;
  }

  public void setFcEnvioNote(Date fcEnvioNote) {
      this.fcEnvioNote = fcEnvioNote;
  }
  

  @Column(name = "CD_ESTADO_NOTE")
  public String getCdEstadoNote() {
      return cdEstadoNote;
  }

  public void setCdEstadoNote(String cdEstadoNote) {
      this.cdEstadoNote = cdEstadoNote;
  }
   

    @Column(name = "DS_DESTINATARIO")
    public String getDsDestinatario() {
        return dsDestinatario;
    }

    public void setDsDestinatario(String dsDestinatario) {
        this.dsDestinatario = dsDestinatario;
    }

   @Column(name = "CD_FIRMANTE")
    public String getDsFirmante() {
        return dsFirmante;
    }


    public void setDsFirmante(String dsFirmante) {
        this.dsFirmante = dsFirmante;
    }
    
    
    @Column(name = "ID_TRAMITE")
    public Integer getidTramite() {
        return idTramite;
    }

    public void setidTramite(Integer idTramite) {
        this.idTramite = idTramite;
    }

  
    /**
     * @return the dsVerificador
     */
    @Column(name = "CD_VERIFICADOR")
    public String getDsVerificador() {
        return dsVerificador;
    }

    /**
     * @param dsVerificador the dsVerificador to set
     */
    public void setDsVerificador(String dsVerificador) {
        this.dsVerificador = dsVerificador;
    }
    
       
    @Column(name = "DS_TRAMITE")
    public String getdsTramite() {
        return dsTramite;
    }


    public void setdsTramite(String dsTramite) {
        this.dsTramite = dsTramite;
    }

    
    @Column(name = "IT_CORREO")
    public String getItCorreo() {
        return itCorreo;
    }


    public void setItCorreo(String itCorreo) {
        this.itCorreo = itCorreo;
    }
  
    
    @Column(name = "ID_EXPEDIENTE_JUSTEC")
    public Integer getidExpedienteJustec() {
        return idExpedienteJustec;
    }

   public void setidExpedienteJustec(Integer idExpedienteJustec) {
       this.idExpedienteJustec = idExpedienteJustec;
   }
    
   @Column(name = "FC_RECHAZO_NOTE")
   public Date getfcRechazoNote() {
       return fcRechazoNote;
   }

  public void setfcRechazoNote(Date fcRechazoNote) {
      this.fcRechazoNote = fcRechazoNote;
  }

      
      @Column(name = "ID_EXPEDIENTE_JUSECO")
      public Integer getidExpedienteJusEco() {
          return idExpedienteJusEco;
      }
    
     public void setidExpedienteJusEco(Integer idExpedienteJusEco) {
         this.idExpedienteJusEco = idExpedienteJusEco;
     }
     
     /**
      * @return the cdFicaAcuseReciboNote
      */
     @Column(name = "CD_FICA_ACUSE_RECIBO_NOTE")
     public Integer getCdFicaAcuseReciboNote() {
         return cdFicaAcuseReciboNote;
     }

     /**
      * @param cdFicaAcuseReciboNote the cdFicaAcuseReciboNote to set
      */
     public void setCdFicaAcuseReciboNote(Integer cdFicaAcuseReciboNote) {
         this.cdFicaAcuseReciboNote = cdFicaAcuseReciboNote;
     }
     
     
     /**
      * @return the idDocPtfrPadre
      */
     @Column(name = "ID_DOC_PTFR_PADRE")
     public Integer getIdDocPtfrPadre() {
         return idDocPtfrPadre;
     }

     /**
      * @param idDocPtfrPadre the idDocPtfrPadre to set
      */
     public void setIdDocPtfrPadre(Integer idDocPtfrPadre) {
         this.idDocPtfrPadre = idDocPtfrPadre;
     }
   
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return Objects.hash(cdArchivoPTFR, cdEstadoNote, cdEstadoPTFR, cdNote, dsDestinatario,
                dsFirmante, dsReferenciaPTFR, dsTramite, dsVerificador, fcEnvioCola, fcEnvioNote,
                fcEnvioPTFR,fcFirmaPTFR, fcLecturaNote, idConvocatoria, idDocumentoPTFR, idExpediente,
                idTramite,itCorreo,idExpedienteJustec, idExpedienteJusEco);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EnvioDocumentoPTFR other = (EnvioDocumentoPTFR) obj;
        return Objects.equals(cdArchivoPTFR, other.cdArchivoPTFR)
                && Objects.equals(cdEstadoNote, other.cdEstadoNote)
                && Objects.equals(cdEstadoPTFR, other.cdEstadoPTFR)
                && Objects.equals(cdNote, other.cdNote)
                && Objects.equals(dsDestinatario, other.dsDestinatario)
                && Objects.equals(dsFirmante, other.dsFirmante)
                && Objects.equals(dsReferenciaPTFR, other.dsReferenciaPTFR)
                && Objects.equals(dsTramite, other.dsTramite)
                && Objects.equals(dsVerificador, other.dsVerificador)
                && Objects.equals(fcEnvioCola, other.fcEnvioCola)
                && Objects.equals(fcEnvioNote, other.fcEnvioNote)
                && Objects.equals(fcEnvioPTFR, other.fcEnvioPTFR)
                && Objects.equals(fcFirmaPTFR, other.fcFirmaPTFR) 
                && Objects.equals(fcLecturaNote, other.fcLecturaNote)
                && Objects.equals(idConvocatoria, other.idConvocatoria)
                && Objects.equals(idDocumentoPTFR, other.idDocumentoPTFR)
                && Objects.equals(idExpediente, other.idExpediente)
                && Objects.equals(itCorreo, other.itCorreo)
                && Objects.equals(idExpedienteJustec, other.idExpedienteJustec)
                && Objects.equals(idTramite, other.idTramite)
                && Objects.equals(idExpedienteJusEco, other.idExpedienteJusEco);
    }


}
