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
 * Representa el objeto de dominio ExpedienteJusEcoDoc
 *
 * @author Quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_JUSECO_DOC")
@AtlasAuditarFila
@ToString
public class ExpedienteJusEcoDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteJusEcoDoc del objeto de dominio
     */
    private Integer idExpedienteJusEcoDoc;

    /**
     * Representa el campo expedienteJusEco del objeto de dominio
     */
    private ExpedienteJusEco expedienteJusEco;

    /**
     * Representa el campo expedienteDoc del objeto de dominio
     */
    private ExpedienteDoc expedienteDoc;

    /**
     * Representa el campo cumple del objeto de dominio
     */
    private String cumple;
    
    /**
     * Representa el campo expedientePresupMonto del objeto de dominio
     */
    private ExpedientePresupMonto expedientePresupMonto;

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
    public ExpedienteJusEcoDoc() {
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
        return new HashCodeBuilder().append(this.idExpedienteJusEcoDoc)
                .append(this.expedienteJusEco).append(this.expedienteDoc).append(this.cumple)
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
        if (!(obj instanceof ExpedienteJusEcoDoc)) {
            return false;
        }
        ExpedienteJusEcoDoc castObj = (ExpedienteJusEcoDoc) obj;
        return new EqualsBuilder().append(this.idExpedienteJusEcoDoc, castObj.idExpedienteJusEcoDoc)
                .append(this.expedienteJusEco, castObj.expedienteJusEco)
                .append(this.expedienteDoc, castObj.expedienteDoc)
                .append(this.cumple, castObj.cumple).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteJusEcoDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_JUSECO_DOC")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_JUSECO_DOC", sequenceName = "SAII_S_EXPEDIENTE_JUSECO_DOC", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_JUSECO_DOC")
    public Integer getIdExpedienteJusEcoDoc() {
        return this.idExpedienteJusEcoDoc;
    }

    /**
     * Establece el valor del campo idExpedienteJusEcoDoc de la entidad
     * ExpedienteJusEcoDoc
     *
     * @param idExpedienteJusEcoDoc El valor a establecer
     */
    public void setIdExpedienteJusEcoDoc(Integer idExpedienteJusEcoDoc) {
        this.idExpedienteJusEcoDoc = idExpedienteJusEcoDoc;
    }

    /**
     * @return Devuelve el valor del campo expedienteJusEco
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_JUSECO", nullable = false)
    public ExpedienteJusEco getExpedienteJusEco() {
        return this.expedienteJusEco;
    }

    /**
     * Establece el valor del campo expedienteJusEco de la entidad
     * ExpedienteJusEcoDoc
     *
     * @param expedienteJusEco El valor a establecer
     */
    public void setExpedienteJusEco(ExpedienteJusEco expedienteJusEco) {
        this.expedienteJusEco = expedienteJusEco;
    }

    /**
     * @return Devuelve el valor del campo expedienteDoc
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_DOC", nullable = false)
    public ExpedienteDoc getExpedienteDoc() {
        return this.expedienteDoc;
    }

    /**
     * Establece el valor del campo expedienteDoc de la entidad
     * ExpedienteJusEcoDoc
     *
     * @param expedienteDoc El valor a establecer
     */
    public void setExpedienteDoc(ExpedienteDoc expedienteDoc) {
        this.expedienteDoc = expedienteDoc;
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
     * ExpedienteJusEcoDoc
     *
     * @param cumple El valor a establecer
     */
    public void setCumple(String cumple) {
        this.cumple = cumple;
    }

    
    /**
     * @return Devuelve el valor del campo expedientePresupMonto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_PRESUP_MONTO", nullable = false)
    public ExpedientePresupMonto getExpedientePresupMonto() {
        return this.expedientePresupMonto;
    }

    /**
     * Establece el valor del campo expedientePresupMonto de la entidad
     * ExpedienteJusEcoDoc
     *
     * @param expedientePresupMonto El valor a establecer
     */
    public void setExpedientePresupMonto(ExpedientePresupMonto expedientePresupMonto) {
        this.expedientePresupMonto = expedientePresupMonto;
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
     * Establece el valor del campo cdUsuAlta de la entidad ExpedienteJusEcoDoc
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
     * Establece el valor del campo cdUsuBaja de la entidad ExpedienteJusEcoDoc
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
     * Establece el valor del campo cdUsuModif de la entidad ExpedienteJusEcoDoc
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
     * Establece el valor del campo fcAltaFila de la entidad ExpedienteJusEcoDoc
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
     * Establece el valor del campo fcBajaFila de la entidad ExpedienteJusEcoDoc
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
     * ExpedienteJusEcoDoc
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
