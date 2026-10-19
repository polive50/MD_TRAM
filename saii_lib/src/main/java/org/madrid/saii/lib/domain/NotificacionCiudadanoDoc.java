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
 * Representa el objeto de dominio NotificacionCiudadanoDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_NOTIFI_CIUD_DOC")
@AtlasAuditarFila
@ToString
public class NotificacionCiudadanoDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idNotificacionDoc del objeto de dominio
     */
    private Integer idNotificacionDoc;

    /**
     * Representa el campo notificacionCiudadano del objeto de dominio
     */
    private NotificacionCiudadano notificacionCiudadano;

    /**
     * Representa el campo expedienteDoc del objeto de dominio
     */
    private ExpedienteDoc expedienteDoc;

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
    public NotificacionCiudadanoDoc() {
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
        return new HashCodeBuilder().append(this.idNotificacionDoc)
                .append(this.notificacionCiudadano).append(this.expedienteDoc)
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
        if (!(obj instanceof NotificacionCiudadanoDoc)) {
            return false;
        }
        NotificacionCiudadanoDoc castObj = (NotificacionCiudadanoDoc) obj;
        return new EqualsBuilder().append(this.idNotificacionDoc, castObj.idNotificacionDoc)
                .append(this.notificacionCiudadano, castObj.notificacionCiudadano)
                .append(this.expedienteDoc, castObj.expedienteDoc).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idNotificacionDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_NOTIFI_CIUD_DOC")
    @SequenceGenerator(name = "SAII_S_NOTIFI_CIUD_DOC", sequenceName = "SAII_S_NOTIFI_CIUD_DOC", allocationSize = 1)
    @Column(name = "ID_NOTIFICACION_DOC")
    public Integer getIdNotificacionDoc() {
        return this.idNotificacionDoc;
    }

    /**
     * Establece el valor del campo idNotificacionDoc de la entidad
     * NotificacionCiudadanoDoc
     *
     * @param idNotificacionDoc El valor a establecer
     */
    public void setIdNotificacionDoc(Integer idNotificacionDoc) {
        this.idNotificacionDoc = idNotificacionDoc;
    }

    /**
     * @return Devuelve el valor del campo notificacionCiudadano
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NOTIFICACION", nullable = false)
    public NotificacionCiudadano getNotificacionCiudadano() {
        return this.notificacionCiudadano;
    }

    /**
     * Establece el valor del campo notificacionCiudadano de la entidad
     * NotificacionCiudadanoDoc
     *
     * @param notificacionCiudadano El valor a establecer
     */
    public void setNotificacionCiudadano(NotificacionCiudadano notificacionCiudadano) {
        this.notificacionCiudadano = notificacionCiudadano;
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
     * NotificacionCiudadanoDoc
     *
     * @param expedienteDoc El valor a establecer
     */
    public void setExpedienteDoc(ExpedienteDoc expedienteDoc) {
        this.expedienteDoc = expedienteDoc;
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
     * Establece el valor del campo cdUsuAlta de la entidad
     * NotificacionCiudadanoDoc
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
     * Establece el valor del campo cdUsuBaja de la entidad
     * NotificacionCiudadanoDoc
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
     * Establece el valor del campo cdUsuModif de la entidad
     * NotificacionCiudadanoDoc
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
     * Establece el valor del campo fcAltaFila de la entidad
     * NotificacionCiudadanoDoc
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
     * Establece el valor del campo fcBajaFila de la entidad
     * NotificacionCiudadanoDoc
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
     * NotificacionCiudadanoDoc
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
