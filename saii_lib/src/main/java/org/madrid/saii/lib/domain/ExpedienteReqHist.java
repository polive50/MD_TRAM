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
 * Representa el objeto de dominio ExpedienteReq
 *
 * @author nttdata
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_REQ_HIST")
@AtlasAuditarFila
@ToString
public class ExpedienteReqHist implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteReqHist del objeto de dominio
     */
    private Integer idExpedienteReqHist;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Expediente expediente;

    /**
     * Representa el campo requisito del objeto de dominio
     */
    private CatalogosValores requisito;

    /**
     * Representa el campo itCumple del objeto de dominio
     */
    private String itCumple;

    /**
     * Representa el campo tlObservaciones del objeto de dominio
     */
    private String tlObservaciones;

    private DocumentoPtfr documentoPtfr;

    private Integer idExpedienteDoc;

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
    
    private String tlObservacionesCiudadano;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteReqHist() {
        super();
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
        return new HashCodeBuilder().append(this.idExpedienteReqHist).append(this.expediente)
                .append(this.requisito).append(this.itCumple).append(this.tlObservaciones)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila)
                .toHashCode();
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
        if (!(obj instanceof ExpedienteReqHist)) {
            return false;
        }
        ExpedienteReqHist castObj = (ExpedienteReqHist) obj;
        return new EqualsBuilder().append(this.idExpedienteReqHist, castObj.idExpedienteReqHist)
                .append(this.expediente, castObj.expediente)
                .append(this.requisito, castObj.requisito).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteReq
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPE_REQ_HIST")
    @SequenceGenerator(name = "SAII_S_EXPE_REQ_HIST", sequenceName = "SAII_S_EXPE_REQ_HIST", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_REQ_HIST")
    public Integer getIdExpedienteReqHist() {
        return this.idExpedienteReqHist;
    }

    /**
     * Establece el valor del campo idExpedienteReq de la entidad ExpedienteReq
     *
     * @param idExpedienteReq El valor a establecer
     */
    public void setIdExpedienteReqHist(Integer idExpedienteReqHist) {
        this.idExpedienteReqHist = idExpedienteReqHist;
    }

    /**
     * @return Devuelve el valor del campo idExpediente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    /**
     * Establece el valor del campo Expediente de la entidad ExpedienteReqHist
     *
     * @param Expediente El valor a establecer
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    /**
     * @return Devuelve el valor del campo requisito
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REQUISITO", nullable = false)
    public CatalogosValores getRequisito() {
        return this.requisito;
    }

    /**
     * Establece el valor del campo requisito de la entidad ExpedienteReq
     *
     * @param requisito El valor a establecer
     */
    public void setRequisito(CatalogosValores requisito) {
        this.requisito = requisito;
    }

    /**
     * @return the itCumple
     */
    @Column(name = "IT_CUMPLE", nullable = true)
    public String getItCumple() {
        return this.itCumple;
    }

    /**
     * @param itCumple the itCumple to set
     */
    public void setItCumple(String itCumple) {
        this.itCumple = itCumple;
    }

    /**
     * @return the tlObservaciones
     */
    @Column(name = "TL_OBSERVACIONES", nullable = true)
    public String getTlObservaciones() {
        return this.tlObservaciones;
    }

    /**
     * @param tlObservaciones the tlObservaciones to set
     */
    public void setTlObservaciones(String tlObservaciones) {
        this.tlObservaciones = tlObservaciones;
    }

    /**
     * @return the idDocumentoPtfr
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCUMENTO_PTFR", nullable = true)
    public DocumentoPtfr getDocumentoPtfr() {
        return this.documentoPtfr;
    }

    /**
     * @param idDocumentoPtfr the idDocumentoPtfr to set
     */
    public void setDocumentoPtfr(DocumentoPtfr documentoPtfr) {
        this.documentoPtfr = documentoPtfr;
    }

    /**
     * @return the idExpedienteDoc
     */
    @Column(name = "ID_EXPEDIENTE_DOC", nullable = true)
    public Integer getIdExpedienteDoc() {
        return this.idExpedienteDoc;
    }

    /**
     * @param idExpedienteDoc the idExpedienteDoc to set
     */
    public void setIdExpedienteDoc(Integer idExpedienteDoc) {
        this.idExpedienteDoc = idExpedienteDoc;
    }
    
    @Column(name = "TL_OBSERVACIONES_CIU", nullable = true)
    public String getTlObservacionesCiudadano() {
        return tlObservacionesCiudadano;
    }

    public void setTlObservacionesCiudadano(String tlObservacionesCiudadano) {
        this.tlObservacionesCiudadano = tlObservacionesCiudadano;
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
