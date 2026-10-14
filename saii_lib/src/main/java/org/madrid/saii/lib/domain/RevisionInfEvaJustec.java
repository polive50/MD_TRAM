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
import atlas.core.database.auditoria.AtlasAuditarFila;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import lombok.ToString;

/**
 * Representa el objeto de dominio RepresentanteJur
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_REV_INF_EVA_JUSTEC")
@AtlasAuditarFila
@ToString
public class RevisionInfEvaJustec implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo IdRevInfEvaJustec del objeto de dominio
     */
    private Integer idRevInfEvaJustec;

    /**
     * Representa el campo expediente del objeto de dominio
     */
    private Expediente expediente;

    /**
     * Representa el campo expedienteJusTec del objeto de dominio
     */
    private ExpedienteJusTec expedienteJusTec;
    
    
    /**
     * Representa el campo expedienteDoc del objeto de dominio
     */
    private ExpedienteDoc expedienteDoc;
    
    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores idEstado;
    
    /**
     * Representa el campo tlObservaciones del objeto de dominio
     */
    private String tlObservaciones;


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
    public RevisionInfEvaJustec() {
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
        return new HashCodeBuilder().append(this.idRevInfEvaJustec).append(this.expediente).append(this.expedienteJusTec)
                .append(this.expedienteDoc).append(this.idEstado).append(this.tlObservaciones)
                .append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof RevisionInfEvaJustec)) {
            return false;
        }
        RevisionInfEvaJustec castObj = (RevisionInfEvaJustec) obj;
        return new EqualsBuilder().append(this.idRevInfEvaJustec, castObj.idRevInfEvaJustec)
                .append(this.expediente, castObj.expediente)
                .append(this.expedienteJusTec, castObj.expedienteJusTec)
                .append(this.expedienteDoc, castObj.expedienteDoc)
                .append(this.idEstado, castObj.idEstado)
                .append(this.tlObservaciones, castObj.tlObservaciones)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idRevInfEvaJustec
     */
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = 
    "SAII_S_REV_INF_EVA_JUSTEC") 
    @SequenceGenerator(name = "SAII_S_REV_INF_EVA_JUSTEC", sequenceName = "SAII_S_REV_INF_EVA_JUSTEC", allocationSize = 1) 
    @Column(name = " ID_REV_INF_EVA_JUSTEC")
    public Integer getIdRevInfEvaJustec() {
        return idRevInfEvaJustec;
    }

    /**
     * Establece el valor del campo idRevInfEvaJustec de la entidad
     * RevisionInfEvaJustec
     *
     * @param idRevInfEvaJustec El valor a establecer
     */
    public void setIdRevInfEvaJustec(Integer idRevInfEvaJustec) {
        this.idRevInfEvaJustec = idRevInfEvaJustec;
    }

    /**
     * @return Devuelve el valor del campo expediente
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = " ID_EXPEDIENTE ", nullable = false)
    public Expediente getExpediente() {
        return expediente;
    }

    /**
     * Establece el valor del campo expediente de la entidad
     * RevisionInfEvaJustec
     *
     * @param expediente El valor a establecer
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }
    
    /**
     * @return Devuelve el valor del campo expedienteJusTec
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = " ID_EXPEDIENTE_JUSTEC ", nullable = false)
    public ExpedienteJusTec getExpedienteJusTec() {
        return expedienteJusTec;
    }

    /**
     * Establece el valor del campo expedienteJusTec de la entidad
     * RevisionInfEvaJustec
     *
     * @param expedienteJusTec El valor a establecer
     */
    public void setExpedienteJusTec(ExpedienteJusTec expedienteJusTec) {
        this.expedienteJusTec = expedienteJusTec;
    }

    /**
     * @return Devuelve el valor del campo expedienteDoc
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = " ID_EXPEDIENTE_DOC ", nullable = false) 
    public ExpedienteDoc getExpedienteDoc() {
        return expedienteDoc;
    }

    /**
     * Establece el valor del campo expedienteDoc de la entidad
     * RevisionInfEvaJustec
     *
     * @param expedienteDoc El valor a establecer
     */
    public void setExpedienteDoc(ExpedienteDoc expedienteDoc) {
        this.expedienteDoc = expedienteDoc;
    }

    /**
     * @return Devuelve el valor del campo idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = " ID_ESTADO ", nullable = false) 
    public CatalogosValores getIdEstado() {
        return idEstado;
    }

    /**
     * Establece el valor del campo idEstado de la entidad
     * RevisionInfEvaJustec
     *
     * @param idEstado El valor a establecer
     */
    public void setIdEstado(CatalogosValores idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return Devuelve el valor del campo tlObservaciones
     */
    @Column(name = "TL_OBSERVACIONES", nullable = true)
    public String getTlObservaciones() {
        return tlObservaciones;
    }

    /**
     * Establece el valor del campo tlObservaciones de la entidad
     * RevisionInfEvaJustec
     *
     * @param tlObservaciones El valor a establecer
     */
    public void setTlObservaciones(String tlObservaciones) {
        this.tlObservaciones = tlObservaciones;
    }


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// AUDITORIA
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    @Column(name = "CD_USU_ALTA", nullable = true)
    public String getCdUsuAlta() {
        return cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad
     * RevisionInfEvaJustec
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    @Column(name = "FC_ALTA_FILA", nullable = true)
    public Date getFcAltaFila() {
        return fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad
     * RevisionInfEvaJustec
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad
     * RevisionInfEvaJustec
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad
     * RevisionInfEvaJustec
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad
     * RevisionInfEvaJustec
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad
     * RevisionInfEvaJustec
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////

}
