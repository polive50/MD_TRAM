package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio DocumentoPtfr
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_DOCUMENTO_PTFR")
@AtlasAuditarFila
@ToString
public class DocumentoPtfr implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idDocumentoPtfr del objeto de dominio
     */
    private Integer idDocumentoPtfr;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo dsReferenciaPtfr del objeto de dominio
     */
    private String dsReferenciaPtfr;

    /**
     * Representa el campo cdArchivoPtfr del objeto de dominio
     */
    private Integer cdArchivoPtfr;

    /**
     * Representa el campo fcEnvioPtfr del objeto de dominio
     */
    private Date fcEnvioPtfr;

    /**
     * Representa el campo cdArchivoPtfr del objeto de dominio
     */
    private Integer cdArchivoRespPtfr;

    /**
     * Representa el campo fcFirmaPtfr del objeto de dominio
     */
    private Date fcFirmaPtfr;

    /**
     * Representa el campo cdEstadoPtfr del objeto de dominio
     */
    private String cdEstadoPtfr;

    /**
     * Representa al objeto el dominio idTramite
     */
    private CatalogosValores tramite;

    /**
     * Representa el campo cdNote del objeto de dominio
     */
    private String cdNote;

    /**
     * Representa el campo fcEnvioNote del objeto de dominio
     */
    private Date fcEnvioNote;

    /**
     * Representa el campo fcLecturaNote del objeto de dominio
     */
    private Date fcLecturaNote;

    /**
     * Representa el campo itUsuAltaNote del objeto de dominio
     */
    private String itUsuAltaNote;

    /**
     * Representa el indicador si es una petición por envío de correo
     */
    private String itCorreo;

    /**
     * Representa la fecha de envío de correo
     */
    private Date fcEnvioCorreo;

    /**
     * Representa el campo cdUsuAlta del objeto de dominio
     */
    private String cdUsuAlta;

    /**
     * Representa el campo fcAltaFila del objeto de dominio
     */
    private Date fcAltaFila;

    /**
     * Representa el campo cdUsuModif del objeto de dominio
     */
    private String cdUsuModif;

    /**
     * Representa el campo fcModifFila del objeto de dominio
     */
    private Date fcModifFila;

    /**
     * Representa el campo cdUsuBaja del objeto de dominio
     */
    private String cdUsuBaja;

    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcBajaFila;

    /**
     * Representa el campo MotivoRechazo del objeto de dominio
     */
    private String motivoRechazo;

    /**
     * Representa el campo cdEstadoNote del objeto de dominio
     */
    private String cdEstadoNote;

    /**
     * Representa el campo fcIniPlazoSubsanacion del objeto de dominio
     */
    private Date fcIniPlazoSubsanacion;

    /**
     * Representa el campo fcFinPlazoSubsanacion del objeto de dominio
     */
    private Date fcFinPlazoSubsanacion;

    /**
     * Representa el campo itIniPlazoVencido del objeto de dominio
     */
    private String itIniPlazoVencido;

    /**
     * Representa la fecha de rechazo de NOTE subsanación
     */
    private Date fcRechazoNote;

    
    private byte[] documentoAdjunto;
    
    /**
     * Representa el campo idExpedienteJustec del objeto de dominio
     */
    private Integer idExpedienteJustec;
    
    
    /**
     * Representa el campo idPresupuestoModif del objeto de dominio
     */
    private Integer idPresupuestoModif;
    
    
    /**
     * Representa el campo idExpedienteJusEco del objeto de dominio
     */
    private Integer idExpedienteJusEco;
    
    /**
     * Representa el campo cdFicaAcuseReciboNote del objeto de dominio
     */
    private Integer cdFicaAcuseReciboNote;
    
    /** Identificador de la petición padre; cero para la primera del grupo. */
    private Integer idDocPtfrPadre;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public DocumentoPtfr() {
        super();
    }
    
    public DocumentoPtfr(Integer idDocumentoPtfr) {
        super();
        this.idDocumentoPtfr = idDocumentoPtfr;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    /**
     * Implementación de hashCode para uso de Hibernate
     *
     * @return El código hash del objeto
     *         <p>
     *         Más información en:
     *         {@link http://community.jboss.org/docs/DOC-13933}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.idDocumentoPtfr).append(this.idExpediente)
                .append(this.dsReferenciaPtfr).append(this.cdArchivoPtfr).append(this.fcEnvioPtfr)
                .append(this.cdArchivoRespPtfr).append(this.fcFirmaPtfr).append(this.cdEstadoPtfr)
                .append(this.tramite).append(this.cdNote).append(this.fcEnvioNote)
                .append(this.fcLecturaNote).append(this.itUsuAltaNote).append(this.cdUsuAlta)
                .append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).append(this.motivoRechazo)
                .append(this.cdEstadoNote).append(this.fcIniPlazoSubsanacion)
                .append(this.fcFinPlazoSubsanacion).append(this.itIniPlazoVencido)
                .append(this.idPresupuestoModif).append(this.idPresupuestoModif)
                .append(this.fcRechazoNote).toHashCode();
    }

    /**
     * Implementación de equals para uso con Hibernate
     * <p>
     * Más información en: {@link http://community.jboss.org/docs/DOC-13933}
     *
     * @param obj objeto a comparar con esta instancia.
     * @return true si el objeto pasado es igual a este; false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DocumentoPtfr)) {
            return false;
        }
        DocumentoPtfr castObj = (DocumentoPtfr) obj;
        return new EqualsBuilder().append(this.idDocumentoPtfr, castObj.idDocumentoPtfr)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.dsReferenciaPtfr, castObj.dsReferenciaPtfr)
                .append(this.cdArchivoPtfr, castObj.cdArchivoPtfr)
                .append(this.fcEnvioPtfr, castObj.fcEnvioPtfr)
                .append(this.cdArchivoRespPtfr, castObj.cdArchivoRespPtfr)
                .append(this.fcFirmaPtfr, castObj.fcFirmaPtfr)
                .append(this.cdEstadoPtfr, castObj.cdEstadoPtfr)
                .append(this.tramite, castObj.tramite)
                .append(this.motivoRechazo, castObj.motivoRechazo)
                .append(this.cdEstadoNote, castObj.cdEstadoNote)
                .append(this.fcIniPlazoSubsanacion, castObj.fcIniPlazoSubsanacion)
                .append(this.fcFinPlazoSubsanacion, castObj.fcFinPlazoSubsanacion)
                .append(this.itIniPlazoVencido, castObj.itIniPlazoVencido)
                .append(this.idPresupuestoModif, castObj.idPresupuestoModif)
                .append(this.fcRechazoNote, castObj.fcRechazoNote).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idDocumentoPtfr
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_DOCUMENTO_PTFR")
    @SequenceGenerator(name = "SAII_S_DOCUMENTO_PTFR", sequenceName = "SAII_S_DOCUMENTO_PTFR", allocationSize = 1)
    @Column(name = "ID_DOCUMENTO_PTFR")
    public Integer getIdDocumentoPtfr() {
        return this.idDocumentoPtfr;
    }

    /**
     * Establece el valor del campo idDocumentoPtfr de la entidad DocumentoPtfr
     *
     * @param idDocumentoPtfr El valor a establecer
     */
    public void setIdDocumentoPtfr(Integer idDocumentoPtfr) {
        this.idDocumentoPtfr = idDocumentoPtfr;
    }

    /**
     * @return Devuelve el valor del campo idExpediente
     */
    @Column(name = "ID_EXPEDIENTE", nullable = true)
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad DocumentoPtfr
     *
     * @param expediente El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    /**
     * @return Devuelve el valor del campo dsReferenciaPtfr
     */

    /**
     * @return Devuelve el valor del campo dsReferenciaPtfr
     */
    @Column(name = "DS_REFERENCIA_PTFR", nullable = true)
    public String getDsReferenciaPtfr() {
        return this.dsReferenciaPtfr;
    }

    /**
     * Establece el valor del campo dsReferenciaPtfr de la entidad DocumentoPtfr
     *
     * @param dsReferenciaPtfr El valor a establecer
     */
    public void setDsReferenciaPtfr(String dsReferenciaPtfr) {
        this.dsReferenciaPtfr = dsReferenciaPtfr;
    }

    /**
     * @return Devuelve el valor del campo cdArchivoPtfr
     */
    @Column(name = "CD_ARCHIVO_PTFR", nullable = true)
    public Integer getCdArchivoPtfr() {
        return this.cdArchivoPtfr;
    }

    /**
     * Establece el valor del campo cdArchivoPtfr de la entidad DocumentoPtfr
     *
     * @param cdArchivoPtfr El valor a establecer
     */
    public void setCdArchivoPtfr(Integer cdArchivoPtfr) {
        this.cdArchivoPtfr = cdArchivoPtfr;
    }

    /**
     * @return Devuelve el valor del campo fcEnvioPtfr
     */
    @Column(name = "FC_ENVIO_PTFR", nullable = true)
    public Date getFcEnvioPtfr() {
        return this.fcEnvioPtfr;
    }

    /**
     * Establece el valor del campo fcEnvioPtfr de la entidad DocumentoPtfr
     *
     * @param fcEnvioPtfr El valor a establecer
     */
    public void setFcEnvioPtfr(Date fcEnvioPtfr) {
        this.fcEnvioPtfr = fcEnvioPtfr;
    }

    /**
     * @return Devuelve el valor del campo cdArchivoRespPtfr
     */
    @Column(name = "CD_ARCHIVO_RESP_PTFR", nullable = true)
    public Integer getCdArchivoRespPtfr() {
        return this.cdArchivoRespPtfr;
    }

    /**
     * Establece el valor del campo cdArchivoRespPtfr de la entidad
     * DocumentoPtfr
     *
     * @param cdArchivoRespPtfr El valor a establecer
     */
    public void setCdArchivoRespPtfr(Integer cdArchivoRespPtfr) {
        this.cdArchivoRespPtfr = cdArchivoRespPtfr;
    }

    /**
     * @return Devuelve el valor del campo fcFirmaPtfr
     */
    @Column(name = "FC_FIRMA_PTFR", nullable = true)
    public Date getFcFirmaPtfr() {
        return this.fcFirmaPtfr;
    }

    /**
     * Establece el valor del campo fcFirmaPtfr de la entidad DocumentoPtfr
     *
     * @param fcFirmaPtfr El valor a establecer
     */
    public void setFcFirmaPtfr(Date fcFirmaPtfr) {
        this.fcFirmaPtfr = fcFirmaPtfr;
    }

    /**
     * @return Devuelve el valor del campo cdEstadoPtfr
     */
    @Column(name = "CD_ESTADO_PTFR", nullable = true)
    public String getCdEstadoPtfr() {
        return this.cdEstadoPtfr;
    }

    /**
     * Establece el valor del campo cdEstadoPtfr de la entidad DocumentoPtfr
     *
     * @param cdEstadoPtfr El valor a establecer
     */
    public void setCdEstadoPtfr(String cdEstadoPtfr) {
        this.cdEstadoPtfr = cdEstadoPtfr;
    }

    /**
     * @return Devuelve el valor del campo idTramite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE", nullable = true)
    public CatalogosValores getTramite() {
        return this.tramite;
    }

    /**
     * Establece el valor del campo idTramite de la entidad DocumentoPtfr
     *
     * @param idTramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
    }

    /**
     * @return Devuelve el valor del campo cdNote
     */
    @Column(name = "CD_NOTE", nullable = true)
    public String getCdNote() {
        return this.cdNote;
    }

    /**
     * Establece el valor del campo cdNote de la entidad DocumentoPtfr
     *
     * @param cdNote El valor a establecer
     */
    public void setCdNote(String cdNote) {
        this.cdNote = cdNote;
    }

    /**
     * @return Devuelve el valor del campo fcEnvioNote
     */
    @Column(name = "FC_ENVIO_NOTE", nullable = true)
    public Date getFcEnvioNote() {
        return this.fcEnvioNote;
    }

    /**
     * Establece el valor del campo fcEnvioNote de la entidad DocumentoPtfr
     *
     * @param fcEnvioNote El valor a establecer
     */
    public void setFcEnvioNote(Date fcEnvioNote) {
        this.fcEnvioNote = fcEnvioNote;
    }

    /**
     * @return Devuelve el valor del campo fcLecturaNote
     */
    @Column(name = "FC_LECTURA_NOTE", nullable = true)
    public Date getFcLecturaNote() {
        return this.fcLecturaNote;
    }

    /**
     * Establece el valor del campo fcLecturaNote de la entidad DocumentoPtfr
     *
     * @param fcLecturaNote El valor a establecer
     */
    public void setFcLecturaNote(Date fcLecturaNote) {
        this.fcLecturaNote = fcLecturaNote;
    }

    /**
     * @return Devuelve el valor del campo itAltaNote
     */
    @Column(name = "IT_USU_ALTA_NOTE", nullable = true)
    public String getItUsuAltaNote() {
        return this.itUsuAltaNote;
    }

    /**
     * Establece el valor del campo itAltaNote de la entidad DocumentoPtfr
     *
     * @param itAltaNote El valor a establecer
     */
    public void setItUsuAltaNote(String itUsuAltaNote) {
        this.itUsuAltaNote = itUsuAltaNote;
    }

    /**
     * @return Devuelve el valor del campo cdEstadoNote
     */
    @Column(name = "CD_ESTADO_NOTE", nullable = true)
    public String getCdEstadoNote() {
        return this.cdEstadoNote;
    }

    /**
     * Establece el valor del campo cdEstadoNote de la entidad DocumentoPtfr
     *
     * @param cdEstadoNote El valor a establecer
     */
    public void setCdEstadoNote(String cdEstadoNote) {
        this.cdEstadoNote = cdEstadoNote;
    }

    /**
     * @return Devuelve el valor del campo fcIniPlazoSubsanacion
     */
    @Column(name = "FC_INI_PLAZO_SUBSANACION", nullable = true)
    public Date getFcIniPlazoSubsanacion() {
        return this.fcIniPlazoSubsanacion;
    }

    /**
     * Establece el valor del campo fcIniPlazoSubsanacion de la entidad
     * DocumentoPtfr
     *
     * @param fcIniPlazoSubsanacion El valor a establecer
     */
    public void setFcIniPlazoSubsanacion(Date fcIniPlazoSubsanacion) {
        this.fcIniPlazoSubsanacion = fcIniPlazoSubsanacion;
    }

    /**
     * @return Devuelve el valor del campo fcFinPlazoSubsanacion
     */
    @Column(name = "FC_FIN_PLAZO_SUBSANACION", nullable = true)
    public Date getFcFinPlazoSubsanacion() {
        return this.fcFinPlazoSubsanacion;
    }

    /**
     * Establece el valor del campo itIniPlazoVencido de la entidad
     * DocumentoPtfr
     *
     * @param itIniPlazoVencido El valor a establecer
     */
    public void setItIniPlazoVencido(String itIniPlazoVencido) {
        this.itIniPlazoVencido = itIniPlazoVencido;
    }

    /**
     * @return Devuelve el valor del campo fcRechazoNote
     */
    @Column(name = "IT_INI_PLAZO_VENCIDO", nullable = true)
    public String getItIniPlazoVencido() {
        return this.itIniPlazoVencido;
    }

    /**
     * Establece el valor del campo fcFinPlazoSubsanacion de la entidad
     * DocumentoPtfr
     *
     * @param fcFinPlazoSubsanacion El valor a establecer
     */
    public void setFcFinPlazoSubsanacion(Date fcFinPlazoSubsanacion) {
        this.fcFinPlazoSubsanacion = fcFinPlazoSubsanacion;
    }

    /**
     * @return Devuelve el valor del campo fcRechazoNote
     */
    @Column(name = "FC_RECHAZO_NOTE", nullable = true)
    public Date getFcRechazoNote() {
        return this.fcRechazoNote;
    }

    /**
     * Establece el valor del campo fcRechazoNote de la entidad DocumentoPtfr
     *
     * @param fcRechazoNote El valor a establecer
     */
    public void setFcRechazoNote(Date fcRechazoNote) {
        this.fcRechazoNote = fcRechazoNote;
    }

    /**
     * @return Devuelve el valor del campo itCorreo
     */
    @Column(name = "IT_CORREO", nullable = true)
    public String getItCorreo() {
        return this.itCorreo;
    }

    /**
     * Establece el valor del campo itCorreo de la entidad DocumentoPtfr
     *
     * @param itCorreo El valor a establecer
     */
    public void setItCorreo(String itCorreo) {
        this.itCorreo = itCorreo;
    }

    /**
     * @return Devuelve el valor del campo fcEnvioNote
     */
    @Column(name = "FC_ENVIO_CORREO", nullable = true)
    public Date getFcEnvioCorreo() {
        return this.fcEnvioCorreo;
    }

    /**
     * Establece el valor del campo fcEnvioCorreo de la entidad DocumentoPtfr
     *
     * @param fcEnvioCorreo El valor a establecer
     */
    public void setFcEnvioCorreo(Date fcEnvioCorreo) {
        this.fcEnvioCorreo = fcEnvioCorreo;
    }

    
    @Lob
    @Column(name = "BL_DOCUMENTO", nullable = true)
    public byte[] getDocumentoAdjunto() {
        return documentoAdjunto;
    }

    public void setDocumentoAdjunto(byte[] documentoAdjunto) {
        this.documentoAdjunto = documentoAdjunto;
    }
    
    
    @Column(name = "ID_EXPEDIENTE_JUSTEC")
    public Integer getidExpedienteJustec() {
        return idExpedienteJustec;
    }

   public void setidExpedienteJustec(Integer idExpedienteJustec) {
       this.idExpedienteJustec = idExpedienteJustec;
   }
    
    
   @Column(name = "ID_EXP_MODIF_PRES_PLAZO_OTRA")
   public Integer getIdPresupuestoModif() {
       return idPresupuestoModif;
   }

  public void setIdPresupuestoModif(Integer idPresupuestoModif) {
      this.idPresupuestoModif = idPresupuestoModif;
  }

      /**
      *
      * @return Devuelve el valor del campo motivoRechazo
      */
     @Column(name = "DS_MOTIVO_RECHAZO_PTFR", nullable = true)
     public String getMotivoRechazo() {
         return this.motivoRechazo;
     }
    
     /**
      *
      * @param motivoRechazo El valor a establecer
      */
     public void setMotivoRechazo(String motivoRechazo) {
         this.motivoRechazo = motivoRechazo;
     }
    
     @Column(name = "ID_EXPEDIENTE_JUSECO")
     public Integer getIdExpedienteJusEco() {
         return idExpedienteJusEco;
     }
    
     public void setIdExpedienteJusEco(Integer idExpedienteJusEco) {
         this.idExpedienteJusEco = idExpedienteJusEco;
     }
     
     /**
      * @return Devuelve el valor del campo cdFica
      */

     @Column(name = "CD_FICA_ACUSE_RECIBO_NOTE", nullable = true)
     public Integer getCdFicaAcuseReciboNote() {
         return this.cdFicaAcuseReciboNote;
     }

     /**
      * Establece el valor del campo cdFica de la entidad ConvocatoriaDoc
      *
      * @param cdFica El valor a establecer
      */
     public void setCdFicaAcuseReciboNote(Integer cdFicaAcuseReciboNote) {
         this.cdFicaAcuseReciboNote = cdFicaAcuseReciboNote;
     }
     
     /** @return identificador de la petición padre */
     @Column(name = "ID_DOC_PTFR_PADRE", nullable = true)
     public Integer getIdDocPtfrPadre() {
         return this.idDocPtfrPadre;
     }

     /** @param idDocPtfrPadre identificador de la petición padre */
     public void setIdDocPtfrPadre(Integer idDocPtfrPadre) {
         this.idDocPtfrPadre = idDocPtfrPadre;
     }
   
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// AUDITORIA
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Catalogos
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad Catalogos
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad Catalogos
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Catalogos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad Catalogos
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad Catalogos
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}
