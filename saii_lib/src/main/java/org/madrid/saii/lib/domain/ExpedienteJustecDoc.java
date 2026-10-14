package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ExpedienteJustecDoc
 *
 * @author Quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_JUSTEC_DOC")
@AtlasAuditarFila
@ToString
public class ExpedienteJustecDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteJustecDoc del objeto de dominio
     */
    private Integer idExpedienteJustecDoc;

    /**
     * Representa el campo idExpedienteJusTec del objeto de dominio
     */
    private Integer idExpedienteJusTec;

    /**
     * Representa el campo idExpedienteDoc del objeto de dominio
     */
    private Integer idExpedienteDoc;

    /**
     * Representa el campo cumple del objeto de dominio
     */
    private String cumple;

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
    public ExpedienteJustecDoc() {
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
        return new HashCodeBuilder().append(this.idExpedienteJustecDoc)
                .append(this.idExpedienteJusTec).append(this.idExpedienteDoc).append(this.cumple)
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
        if (!(obj instanceof ExpedienteJustecDoc)) {
            return false;
        }
        ExpedienteJustecDoc castObj = (ExpedienteJustecDoc) obj;
        return new EqualsBuilder().append(this.idExpedienteJustecDoc, castObj.idExpedienteJustecDoc)
                .append(this.idExpedienteJusTec, castObj.idExpedienteJusTec)
                .append(this.idExpedienteDoc, castObj.idExpedienteDoc)
                .append(this.cumple, castObj.cumple).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteJustecDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_JUSTEC_DOC")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_JUSTEC_DOC", sequenceName = "SAII_S_EXPEDIENTE_JUSTEC_DOC", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_JUSTEC_DOC")
    public Integer getIdExpedienteJustecDoc() {
        return this.idExpedienteJustecDoc;
    }

    /**
     * Establece el valor del campo idExpedienteJustecDoc de la entidad
     * ExpedienteJustecDoc
     *
     * @param idExpedienteJustecDoc El valor a establecer
     */
    public void setIdExpedienteJustecDoc(Integer idExpedienteJustecDoc) {
        this.idExpedienteJustecDoc = idExpedienteJustecDoc;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteJusTec
     */
    @Column(name = "ID_EXPEDIENTE_JUSTEC")
    public Integer getIdExpedienteJusTec() {
        return this.idExpedienteJusTec;
    }

    /**
     * Establece el valor del campo idExpedienteJusTec de la entidad
     * ExpedienteJustecDoc
     *
     * @param idExpedienteJusTec El valor a establecer
     */
    public void setIdExpedienteJusTec(Integer idExpedienteJusTec) {
        this.idExpedienteJusTec = idExpedienteJusTec;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteDoc
     */
    @Column(name = "ID_EXPEDIENTE_DOC")
    public Integer getIdExpedienteDoc() {
        return this.idExpedienteDoc;
    }

    /**
     * Establece el valor del campo idExpedienteDoc de la entidad
     * ExpedienteJustecDoc
     *
     * @param idExpedienteDoc El valor a establecer
     */
    public void setIdExpedienteDoc(Integer idExpedienteDoc) {
        this.idExpedienteDoc = idExpedienteDoc;
    }

    /**
     * @return Devuelve el valor del campo cumple
     */
    @Column(name = "CUMPLE")
    public String getCumple() {
        return this.cumple;
    }

    /**
     * Establece el valor del campo cumple de la entidad
     * ExpedienteJustecDoc
     *
     * @param cumple El valor a establecer
     */
    public void setCumple(String cumple) {
        this.cumple = cumple;
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
     * Establece el valor del campo cdUsuAlta de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo cdUsuBaja de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo cdUsuModif de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcAltaFila de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcBajaFila de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcModifFila de la entidad
     * ExpedienteJustecDoc
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
