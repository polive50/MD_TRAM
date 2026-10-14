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
 * Representa el objeto de dominio ConvocatoriaCau
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_CAU")
@AtlasAuditarFila
@ToString
public class ConvocatoriaCau implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaCau del objeto de dominio
     */
    private Integer idConvocatoriaCau;

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
     * Representa el campo convocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo causa del objeto de dominio
     */
    private CatalogosValores causa;

    /** Representa el campo tlNormativa en el objeto de dominio */
    private String tlNormativa;
    
    /**
     * 
     */
    private CatalogosValores tipoCausa;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCau() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaCau).toHashCode();
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
        if (!(obj instanceof ConvocatoriaCau)) {
            return false;
        }
        ConvocatoriaCau castObj = (ConvocatoriaCau) obj;
        return new EqualsBuilder().append(this.idConvocatoriaCau, castObj.idConvocatoriaCau)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.causa, castObj.causa).append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.tlNormativa, castObj.tlNormativa).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoriaCau
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_CAU")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_CAU", sequenceName = "SAII_S_CONVOCATORIA_CAU", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_CAU")
    public Integer getIdConvocatoriaCau() {
        return this.idConvocatoriaCau;
    }

    /**
     * Establece el valor del campo idConvocatoriaCau de la entidad
     * ConvocatoriaCau
     *
     * @param idConvocatoriaCau El valor a establecer
     */
    public void setIdConvocatoriaCau(Integer idConvocatoriaCau) {
        this.idConvocatoriaCau = idConvocatoriaCau;
    }

    /**
     * @return Devuelve el valor del campo convocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo convocatoria de la entidad ConvocatoriaCau
     *
     * @param convocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo causa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAUSA")
    public CatalogosValores getCausa() {
        return this.causa;
    }

    /**
     * Establece el valor del campo causa de la entidad ConvocatoriaCau
     *
     * @param causa El valor a establecer
     */
    public void setCausa(CatalogosValores causa) {
        this.causa = causa;
    }

    /**
     * @return Devuelve el valor del campo tlNormativa
     */
    @Column(name = "TL_NORMATIVA")
    public String getTlNormativa() {
        return this.tlNormativa;
    }

    /**
     * Establece el valor del campo tlNormativa de la entidad ConvocatoriaCau
     *
     * @param convocatoria El valor a establecer
     */
    public void setTlNormativa(String tlNormativa) {
        this.tlNormativa = tlNormativa;
    }
    
    /**
     * @return the tipoCausa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CAUSA")
    public CatalogosValores getTipoCausa() {
        return tipoCausa;
    }

    /**
     * @param tipoCausa the tipoCausa to set
     */
    public void setTipoCausa(CatalogosValores tipoCausa) {
        this.tipoCausa = tipoCausa;
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
