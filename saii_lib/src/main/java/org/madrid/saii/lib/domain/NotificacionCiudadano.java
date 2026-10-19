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
 * Representa el objeto de dominio NotificacionCiudadano
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_NOTIFI_CIUD")
@AtlasAuditarFila
@ToString
public class NotificacionCiudadano implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idNotificacion del objeto de dominio
     */
    private Integer idNotificacion;

    /**
     * Representa el campo expediente del objeto de dominio
     */
    private Expediente expediente;

    /**
     * Representa el campo asunto del objeto de dominio
     */
    private CatalogosValores asunto;

    /**
     * Representa el campo tlExpone del objeto de dominio
     */
    private String tlExpone;

    /**
     * Representa el campo tlSolicita del objeto de dominio
     */
    private String tlSolicita;

    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo fcFirmaEreg del objeto de dominio
     */
    private Date fcFirmaEreg;

    /**
     * Representa el campo cdEreg del objeto de dominio
     */
    private String cdEreg;

    /**
     * Representa el campo dsDestinoEreg del objeto de dominio
     */
    private String dsDestinoEreg;

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
    public NotificacionCiudadano() {
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
        return new HashCodeBuilder().append(this.idNotificacion).append(this.expediente)
                .append(this.asunto).append(this.tlExpone).append(this.tlSolicita)
                .append(this.estado).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja)
                .append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof NotificacionCiudadano)) {
            return false;
        }
        NotificacionCiudadano castObj = (NotificacionCiudadano) obj;
        return new EqualsBuilder().append(this.idNotificacion, castObj.idNotificacion)
                .append(this.expediente, castObj.expediente)
                .append(this.asunto, castObj.asunto).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idNotificacion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_NOTIFI_CIUD")
    @SequenceGenerator(name = "SAII_S_NOTIFI_CIUD", sequenceName = "SAII_S_NOTIFI_CIUD", allocationSize = 1)
    @Column(name = "ID_NOTIFICACION")
    public Integer getIdNotificacion() {
        return this.idNotificacion;
    }

    /**
     * Establece el valor del campo idNotificacion de la entidad
     * NotificacionCiudadano
     *
     * @param idNotificacion El valor a establecer
     */
    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    /**
     * @return Devuelve el valor del campo expediente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    /**
     * Establece el valor del campo expediente de la entidad
     * NotificacionCiudadano
     *
     * @param expediente El valor a establecer
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    /**
     * @return Devuelve el valor del campo asunto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ASUNTO", nullable = false)
    public CatalogosValores getAsunto() {
        return this.asunto;
    }

    /**
     * Establece el valor del campo asunto de la entidad NotificacionCiudadano
     *
     * @param asunto El valor a establecer
     */
    public void setAsunto(CatalogosValores asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the tlExpone
     */
    @Column(name = "TL_EXPONE", nullable = true)
    public String getTlExpone() {
        return this.tlExpone;
    }

    /**
     * @param tlExpone the tlExpone to set
     */
    public void setTlExpone(String tlExpone) {
        this.tlExpone = tlExpone;
    }

    /**
     * @return the tlSolicita
     */
    @Column(name = "TL_SOLICITA", nullable = true)
    public String getTlSolicita() {
        return this.tlSolicita;
    }

    /**
     * @param tlSolicita the tlSolicita to set
     */
    public void setTlSolicita(String tlSolicita) {
        this.tlSolicita = tlSolicita;
    }

    /**
     * @return Devuelve el valor del campo estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = true)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    /**
     * Establece el valor del campo estado de la entidad NotificacionCiudadano
     *
     * @param estado El valor a establecer
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return the fcFirmaEreg
     */
    @Column(name = "FC_FIRMA_EREG", nullable = true)
    public Date getFcFirmaEreg() {
        return this.fcFirmaEreg;
    }

    /**
     * @param fcFirmaEreg the fcFirmaEreg to set
     */
    public void setFcFirmaEreg(Date fcFirmaEreg) {
        this.fcFirmaEreg = fcFirmaEreg;
    }

    /**
     * @return the cdEreg
     */
    @Column(name = "CD_EREG", nullable = true)
    public String getCdEreg() {
        return this.cdEreg;
    }

    /**
     * @param cdEreg the cdEreg to set
     */
    public void setCdEreg(String cdEreg) {
        this.cdEreg = cdEreg;
    }

    /**
     * @return the dsDestinoEreg
     */
    @Column(name = "DS_DESTINO_EREG", nullable = true)
    public String getDsDestinoEreg() {
        return this.dsDestinoEreg;
    }

    /**
     * @param dsDestinoEreg the dsDestinoEreg to set
     */
    public void setDsDestinoEreg(String dsDestinoEreg) {
        this.dsDestinoEreg = dsDestinoEreg;
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
     * NotificacionCiudadano
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
     * NotificacionCiudadano
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
     * NotificacionCiudadano
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
     * NotificacionCiudadano
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
     * NotificacionCiudadano
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
     * NotificacionCiudadano
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
