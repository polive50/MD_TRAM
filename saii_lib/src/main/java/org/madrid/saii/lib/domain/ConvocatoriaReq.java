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
 * Representa el objeto de dominio ConvocatoriaReq
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_REQ")
@AtlasAuditarFila
@ToString
public class ConvocatoriaReq implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaReq del objeto de dominio
     */
    private Integer idConvocatoriaReq;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idRequisito del objeto de dominio
     */
    private CatalogosValores requisito;

    /**
     * Representa el campo idConvocatoriaSubmod del objeto de dominio
     */
    private ConvocatoriaSubmod convocatoriaSubmod;

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
     * Representa el campo idActor del objeto de dominio
     */
    private ConvocatoriaAct convocatoriaAct;
    
    /**
     * Representa el campo convocatoriaTipoDoc del objeto de dominio
     */
    private ConvocatoriaTipoDoc convocatoriaTipoDoc;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaReq() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaReq).append(this.idConvocatoria)
                .append(this.requisito).append(this.convocatoriaSubmod).append(this.convocatoriaAct)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof ConvocatoriaReq)) {
            return false;
        }
        ConvocatoriaReq castObj = (ConvocatoriaReq) obj;
        return new EqualsBuilder().append(this.idConvocatoriaReq, castObj.idConvocatoriaReq)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.requisito, castObj.requisito)
                .append(this.convocatoriaSubmod, castObj.convocatoriaSubmod)
                .append(this.convocatoriaAct, castObj.convocatoriaAct)
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
     * @return Devuelve el valor del campo idConvocatoriaReq
     */
    @Id
    @Column(name = "ID_CONVOCATORIA_REQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_REQ")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_REQ", sequenceName = "SAII_S_CONVOCATORIA_REQ", allocationSize = 1)
    public Integer getIdConvocatoriaReq() {
        return this.idConvocatoriaReq;
    }

    /**
     * Establece el valor del campo idConvocatoriaReq de la entidad
     * ConvocatoriaReq
     *
     * @param idConvocatoriaReq El valor a establecer
     */
    public void setIdConvocatoriaReq(Integer idConvocatoriaReq) {
        this.idConvocatoriaReq = idConvocatoriaReq;
    }

    /**
     * @return Devuelve el valor del campo convocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo convocatoria de la entidad ConvocatoriaPla
     *
     * @param convocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo idRequisito
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REQUISITO")
    public CatalogosValores getRequisito() {
        return this.requisito;
    }

    /**
     * Establece el valor del campo idRequisito de la entidad ConvocatoriaReq
     *
     * @param idRequisito El valor a establecer
     */
    public void setRequisito(CatalogosValores requisito) {
        this.requisito = requisito;
    }

    /**
     * @return Devuelve el valor del campo idActor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACTOR", nullable = true)
    public ConvocatoriaAct getConvocatoriaAct() {
        return this.convocatoriaAct;
    }

    /**
     * Establece el valor del campo idActor de la entidad
     * ConvocatoriaReq
     *
     * @param idConvocatoriaAct El valor a establecer
     */
    public void setConvocatoriaAct(ConvocatoriaAct convocatoriaAct) {
        this.convocatoriaAct = convocatoriaAct;
    }
 
    /**
     * @return Devuelve el valor del campo idConvocatoriaSubmod
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_SUBMOD", nullable = true)
    public ConvocatoriaSubmod getConvocatoriaSubmod() {
        return this.convocatoriaSubmod;
    }

    /**
     * Establece el valor del campo idConvocatoriaSubmod de la entidad
     * ConvocatoriaReq
     *
     * @param idConvocatoriaSubmod El valor a establecer
     */
    public void setConvocatoriaSubmod(ConvocatoriaSubmod convocatoriaSubmod) {
        this.convocatoriaSubmod = convocatoriaSubmod;
    }
    
    /**
     * Establece el valor del campo convocatoriaTipoDoc
     * 
     * @return the convocatoriaTipoDoc
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_DOC_REQ", nullable = true)
    public ConvocatoriaTipoDoc getConvocatoriaTipoDoc() {
        return this.convocatoriaTipoDoc;
    }

    /**
     * Establece el valor del campo convocatoriaTipoDoc
     * 
     * @param convocatoriaTipoDoc the convocatoriaTipoDoc to set
     */
    public void setConvocatoriaTipoDoc(ConvocatoriaTipoDoc convocatoriaTipoDoc) {
        this.convocatoriaTipoDoc = convocatoriaTipoDoc;
    }
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN LISTAS
    // ////////////////////////////////////////////////////////

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
