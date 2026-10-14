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
 * Representa el objeto de dominio ConvocatoriaJusTec
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_JUSTEC")
@AtlasAuditarFila
@ToString
public class ConvocatoriaJusTec implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaJusTec del objeto de dominio
     */
    private Integer idConvocatoriaJusTec;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoConvocatoriaJusTec del objeto de dominio
     */
    private CatalogosValores tipoConvocatoriaJusTec;

    /**
     * Representa el campo nmPeriodo del objeto de dominio
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiCienTec del objeto de dominio
     */
    private Date fcIniJustiCienTec;

    /**
     * Representa el campo fcFinJustiCienTec del objeto de dominio
     */
    private Date fcFinJustiCienTec;

    /**
     * Representa el campo tlObservacion del objeto de dominio
     */
    private String tlObservacion;

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
    public ConvocatoriaJusTec() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaJusTec).append(this.idConvocatoria)
                .append(this.tipoConvocatoriaJusTec).append(this.nmPeriodo)
                .append(this.fcIniJustiCienTec).append(this.fcFinJustiCienTec)
                .append(this.tlObservacion).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ConvocatoriaJusTec)) {
            return false;
        }
        ConvocatoriaJusTec castObj = (ConvocatoriaJusTec) obj;
        return new EqualsBuilder().append(this.idConvocatoriaJusTec, castObj.idConvocatoriaJusTec)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.tipoConvocatoriaJusTec, castObj.tipoConvocatoriaJusTec)
                .append(this.nmPeriodo, castObj.nmPeriodo)
                .append(this.fcIniJustiCienTec, castObj.fcIniJustiCienTec)
                .append(this.fcFinJustiCienTec, castObj.fcFinJustiCienTec)
                .append(this.tlObservacion, castObj.tlObservacion)
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
     * @return Devuelve el valor del campo idConvocatoriaJusTec
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_JUSTEC")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_JUSTEC", sequenceName = "SAII_S_CONVOCATORIA_JUSTEC", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_JUSTEC")
    public Integer getIdConvocatoriaJusTec() {
        return this.idConvocatoriaJusTec;
    }

    /**
     * Establece el valor del campo idConvocatoriaJusTec de la entidad
     * ConvocatoriaJusTec
     *
     * @param idConvocatoriaJusTec El valor a establecer
     */
    public void setIdConvocatoriaJusTec(Integer idConvocatoriaJusTec) {
        this.idConvocatoriaJusTec = idConvocatoriaJusTec;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad
     * ConvocatoriaJusTec
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo idTipoConvocatoriaJusTec
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CONVOCATORIA_JUSTEC", nullable = false)
    public CatalogosValores getTipoConvocatoriaJusTec() {
        return this.tipoConvocatoriaJusTec;
    }

    /**
     * Establece el valor del campo idTipoConvocatoriaJusTec de la entidad
     * ConvocatoriaJusTec
     *
     * @param idTipoConvocatoriaJusTec El valor a establecer
     */
    public void setTipoConvocatoriaJusTec(CatalogosValores tipoConvocatoriaJusTec) {
        this.tipoConvocatoriaJusTec = tipoConvocatoriaJusTec;
    }

    /**
     * @return Devuelve el valor del campo nmPeriodo
     */
    @Column(name = "NM_PERIODO")
    public Integer getNmPeriodo() {
        return this.nmPeriodo;
    }

    /**
     * Establece el valor del campo nmPeriodo de la entidad ConvocatoriaJusTec
     *
     * @param nmPeriodo El valor a establecer
     */
    public void setNmPeriodo(Integer nmPeriodo) {
        this.nmPeriodo = nmPeriodo;
    }

    /**
     * @return Devuelve el valor del campo fcIniJustiCienTec
     */
    @Column(name = "FC_INI_JUSTI_CIEN_TEC")
    public Date getFcIniJustiCienTec() {
        return this.fcIniJustiCienTec;
    }

    /**
     * Establece el valor del campo fcIniJustiCienTec de la entidad
     * ConvocatoriaJusTec
     *
     * @param fcIniJustiCienTec El valor a establecer
     */
    public void setFcIniJustiCienTec(Date fcIniJustiCienTec) {
        this.fcIniJustiCienTec = fcIniJustiCienTec;
    }

    /**
     * @return Devuelve el valor del campo fcFinJustiCienTec
     */
    @Column(name = "FC_FIN_JUSTI_CIEN_TEC")
    public Date getFcFinJustiCienTec() {
        return this.fcFinJustiCienTec;
    }

    /**
     * Establece el valor del campo fcFinJustiCienTec de la entidad
     * ConvocatoriaJusTec
     *
     * @param fcFinJustiCienTec El valor a establecer
     */
    public void setFcFinJustiCienTec(Date fcFinJustiCienTec) {
        this.fcFinJustiCienTec = fcFinJustiCienTec;
    }

    /**
     * @return Devuelve el valor del campo tlObservacion
     */
    @Column(name = "TL_OBSERVACION")
    public String getTlObservacion() {
        return this.tlObservacion;
    }

    /**
     * Establece el valor del campo tlObservacion de la entidad
     * ConvocatoriaJusTec
     *
     * @param tlObservacion El valor a establecer
     */
    public void setTlObservacion(String tlObservacion) {
        this.tlObservacion = tlObservacion;
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
     * Establece el valor del campo cdUsuAlta de la entidad ConvocatoriaJusTec
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
    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad ConvocatoriaJusTec
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
    @Column(name = "CD_USU_MODIF")
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad ConvocatoriaJusTec
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
    @Column(name = "FC_MODIF_FILA")
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad ConvocatoriaJusTec
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
    @Column(name = "CD_USU_BAJA")
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad ConvocatoriaJusTec
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBfcBajaFilaaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA")
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad ConvocatoriaJusTec
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
