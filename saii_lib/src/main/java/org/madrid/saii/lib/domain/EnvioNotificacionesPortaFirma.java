package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio CatalogosValores
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_ENVIO_NOTIFIC_PORTAFIRMAS")
@AtlasAuditarFila
@ToString
public class EnvioNotificacionesPortaFirma implements Serializable {

   
    /**
     * 
     */
    private static final long serialVersionUID = -466948657386233689L;

    /**
     * Representa el campo idCodigo del objeto de dominio
     */
    private Integer idCodigo;
    
    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo cdNumeroExpediente del objeto de dominio
     */
    private Integer cdNumeroExpediente;

    /**
     * Representa el campo dsDestinatario del objeto de dominio
     */
    private String dsDestinatario;
    
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
     * Representa el campo dsFirmante del objeto de dominio
     */
    private String dsVerificador;
    
    /**
     * Representa el campo itMantenible del objeto de dominio
     */
    private Date fcEnvioCola;
    
    private String nmEnvio;
    
    private String nmTramite;
    
    private byte[] documentoAdjunto;

    //AUDITROIA CAMPOS
    
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
    
   
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EnvioNotificacionesPortaFirma() {
        super();
    }

    public EnvioNotificacionesPortaFirma(Integer idCodigo, Integer idConvocatoria,
            Integer cdNumeroExpediente, String dsDestinatario, String dsFirmante, Integer dsTramite,
            Integer nmEstado, Date fcEnvioCola, String cdUsuAlta, Date fcAltaFila,
            String cdUsuModif, Date fcModifFila, String cdUsuBaja, Date fcBajaFila, String nmEnvio,String nmTramite) {
        super();
        this.idCodigo = idCodigo;
        this.idConvocatoria = idConvocatoria;
        this.cdNumeroExpediente = cdNumeroExpediente;
        this.dsDestinatario = dsDestinatario;
        this.dsFirmante = dsFirmante;
        this.dsTramite = dsTramite;
        this.nmEstado = nmEstado;
        this.fcEnvioCola = fcEnvioCola;
        this.cdUsuAlta = cdUsuAlta;
        this.fcAltaFila = fcAltaFila;
        this.cdUsuModif = cdUsuModif;
        this.fcModifFila = fcModifFila;
        this.cdUsuBaja = cdUsuBaja;
        this.fcBajaFila = fcBajaFila;
        this.nmEnvio = nmEnvio;
        this.nmTramite = nmTramite;
        

    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_ENVIO_NOTI_PORTAFIR")
    @SequenceGenerator(name = "SAII_S_ENVIO_NOTI_PORTAFIR", sequenceName = "SAII_S_ENVIO_NOTI_PORTAFIR", allocationSize = 1)
    @Column(name = "ID_CODIGO")
    public Integer getIdCodigo() {
        return this.idCodigo;
    }
    
    public void setIdCodigo(Integer idCodigo) {
        this.idCodigo = idCodigo;
    }


    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

   @Column(name = "CD_NUMERO_EXPEDIENTE")
    public Integer getCdNumeroExpediente() {
        return cdNumeroExpediente;
    }

   public void setCdNumeroExpediente(Integer cdNumeroExpediente) {
       this.cdNumeroExpediente = cdNumeroExpediente;
   }
   

    @Column(name = "DS_DESTINATARIO")
    public String getDsDestinatario() {
        return dsDestinatario;
    }

    public void setDsDestinatario(String dsDestinatario) {
        this.dsDestinatario = dsDestinatario;
    }

   @Column(name = "DS_FIRMANTE")
    public String getDsFirmante() {
        return dsFirmante;
    }


    public void setDsFirmante(String dsFirmante) {
        this.dsFirmante = dsFirmante;
    }
    
    
    @Column(name = "DS_TRAMITE")
    public Integer getDsTramite() {
        return dsTramite;
    }

    public void setDsTramite(Integer dsTramite) {
        this.dsTramite = dsTramite;
    }

    
    @Column(name = "NM_ESTADO")
    public Integer getNmEstado() {
        return nmEstado;
    }

    public void setNmEstado(Integer nmEstado) {
        this.nmEstado = nmEstado;
    }

    @Column(name = "FC_ENVIO_COLA")
    public Date getFcEnvioCola() {
        return fcEnvioCola;
    }

    public void setFcEnvioCola(Date fcEnvioCola) {
        this.fcEnvioCola = fcEnvioCola;
    }

  
    /**
     * @return the dsVerificador
     */
    @Column(name = "DS_VERIFICADOR")
    public String getDsVerificador() {
        return dsVerificador;
    }

    /**
     * @param dsVerificador the dsVerificador to set
     */
    public void setDsVerificador(String dsVerificador) {
        this.dsVerificador = dsVerificador;
    }
    
    
    /**
     * @return the nmEnvio
     */
    @Column(name = "NM_ENVIO")
    public String getNmEnvio() {
        return nmEnvio;
    }


    public void setNmEnvio(String nmEnvio) {
        this.nmEnvio = nmEnvio;
    }
    
    @Column(name = "NM_TRAMITE")
    public String getNmTramite() {
        return nmTramite;
    }


    public void setNmTramite(String nmTramite) {
        this.nmTramite = nmTramite;
    }
    
    @Lob
    @Column(name = "BL_DOCUMENTO")
    public byte[] getDocumentoAdjunto() {
        return documentoAdjunto;
    }

    public void setDocumentoAdjunto(byte[] documentoAdjunto) {
        this.documentoAdjunto = documentoAdjunto;
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

    @Override
    public int hashCode() {
        return Objects.hash(cdNumeroExpediente, cdUsuAlta, cdUsuBaja, cdUsuModif, dsDestinatario,
                dsFirmante, dsTramite, dsVerificador, fcAltaFila, fcBajaFila, fcEnvioCola,
                fcModifFila, idCodigo, idConvocatoria, nmEnvio, nmEstado, nmTramite);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof EnvioNotificacionesPortaFirma))
            return false;
        EnvioNotificacionesPortaFirma other = (EnvioNotificacionesPortaFirma) obj;
        return Objects.equals(cdNumeroExpediente, other.cdNumeroExpediente)
                && Objects.equals(cdUsuAlta, other.cdUsuAlta)
                && Objects.equals(cdUsuBaja, other.cdUsuBaja)
                && Objects.equals(cdUsuModif, other.cdUsuModif)
                && Objects.equals(dsDestinatario, other.dsDestinatario)
                && Objects.equals(dsFirmante, other.dsFirmante)
                && Objects.equals(dsTramite, other.dsTramite)
                && Objects.equals(dsVerificador, other.dsVerificador)
                && Objects.equals(fcAltaFila, other.fcAltaFila)
                && Objects.equals(fcBajaFila, other.fcBajaFila)
                && Objects.equals(fcEnvioCola, other.fcEnvioCola)
                && Objects.equals(fcModifFila, other.fcModifFila)
                && Objects.equals(idCodigo, other.idCodigo)
                && Objects.equals(idConvocatoria, other.idConvocatoria)
                && Objects.equals(nmEnvio, other.nmEnvio)
                && Objects.equals(nmTramite, other.nmTramite)
                && Objects.equals(nmEstado, other.nmEstado);
    }


}
