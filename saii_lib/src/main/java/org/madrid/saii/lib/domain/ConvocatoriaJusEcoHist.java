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
 * Representa el objeto de dominio ConvocatoriaJusEcoHist
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_JUSECO_HIST")
@AtlasAuditarFila
@ToString
public class ConvocatoriaJusEcoHist implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaJusEco del objeto de dominio
     */
    private Integer idConvocatoriaJusEcoHist;

    /**
     * Representa el campo idConvocatoriaJusEco del objeto de dominio
     */
    private Integer idConvocatoriaJusEco;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoConvocatoriaJusEco del objeto de dominio
     */
    private CatalogosValores tipoConvocatoriaJusEco;

    /**
     * Representa el campo nmPeriodo del objeto de dominio
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiEconomica del objeto de dominio
     */
    private Date fcIniJustiEconomica;

    /**
     * Representa el campo fcFinJustiEconomica del objeto de dominio
     */
    private Date fcFinJustiEconomica;

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
    public ConvocatoriaJusEcoHist() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaJusEcoHist)
                .append(this.idConvocatoriaJusEco).append(this.idConvocatoria)
                .append(this.tipoConvocatoriaJusEco).append(this.nmPeriodo)
                .append(this.fcIniJustiEconomica).append(this.fcFinJustiEconomica)
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
        if (!(obj instanceof ConvocatoriaJusEcoHist)) {
            return false;
        }
        ConvocatoriaJusEcoHist castObj = (ConvocatoriaJusEcoHist) obj;
        return new EqualsBuilder()
                .append(this.idConvocatoriaJusEcoHist, castObj.idConvocatoriaJusEcoHist)
                .append(this.idConvocatoriaJusEco, castObj.idConvocatoriaJusEco)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.tipoConvocatoriaJusEco, castObj.tipoConvocatoriaJusEco)
                .append(this.nmPeriodo, castObj.nmPeriodo)
                .append(this.fcIniJustiEconomica, castObj.fcIniJustiEconomica)
                .append(this.fcFinJustiEconomica, castObj.fcFinJustiEconomica)
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
     * @return Devuelve el valor del campo idConvocatoriaJusEcoHist
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORI_JUSECO_HIST")
    @SequenceGenerator(name = "SAII_S_CONVOCATORI_JUSECO_HIST", sequenceName = "SAII_S_CONVOCATORI_JUSECO_HIST", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_JUSECO_HIST")
    public Integer getIdConvocatoriaJusEcoHist() {
        return this.idConvocatoriaJusEcoHist;
    }

    /**
     * Establece el valor del campo idConvocatoriaJusEcoHist de la entidad
     * ConvocatoriaJusEco
     *
     * @param idConvocatoriaJusEcoHist El valor a establecer
     */
    public void setIdConvocatoriaJusEcoHist(Integer idConvocatoriaJusEcoHist) {
        this.idConvocatoriaJusEcoHist = idConvocatoriaJusEcoHist;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoriaJusEco
     */
    @Column(name = "ID_CONVOCATORIA_JUSECO")
    public Integer getIdConvocatoriaJusEco() {
        return this.idConvocatoriaJusEco;
    }

    /**
     * Establece el valor del campo idConvocatoriaJusEco de la entidad
     * ConvocatoriaJusEco
     *
     * @param idConvocatoriaJusEco El valor a establecer
     */
    public void setIdConvocatoriaJusEco(Integer idConvocatoriaJusEco) {
        this.idConvocatoriaJusEco = idConvocatoriaJusEco;
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
     * ConvocatoriaJusEco
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo tipoConvocatoriaJusEco
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CONVOCATORIA_JUSECO", nullable = false)
    public CatalogosValores getTipoConvocatoriaJusEco() {
        return this.tipoConvocatoriaJusEco;
    }

    /**
     * Establece el valor del campo idTipoConvocatoriaJusEco de la entidad
     * ConvocatoriaJusEco
     *
     * @param idTipoConvocatoriaJusEco El valor a establecer
     */
    public void setTipoConvocatoriaJusEco(CatalogosValores tipoConvocatoriaJusEco) {
        this.tipoConvocatoriaJusEco = tipoConvocatoriaJusEco;
    }

    /**
     * @return Devuelve el valor del campo nmPeriodo
     */
    @Column(name = "NM_PERIODO")
    public Integer getNmPeriodo() {
        return this.nmPeriodo;
    }

    /**
     * Establece el valor del campo nmPeriodo de la entidad ConvocatoriaJusEco
     *
     * @param nmPeriodo El valor a establecer
     */
    public void setNmPeriodo(Integer nmPeriodo) {
        this.nmPeriodo = nmPeriodo;
    }

    /**
     * @return Devuelve el valor del campo fcIniJustiEconomica
     */
    @Column(name = "FC_INI_JUSTI_ECONOMICA")
    public Date getFcIniJustiEconomica() {
        return this.fcIniJustiEconomica;
    }

    /**
     * Establece el valor del campo fcIniJustiEconomica de la entidad
     * ConvocatoriaJusEco
     *
     * @param fcIniJustiEconomica El valor a establecer
     */
    public void setFcIniJustiEconomica(Date fcIniJustiEconomica) {
        this.fcIniJustiEconomica = fcIniJustiEconomica;
    }

    /**
     * @return Devuelve el valor del campo fcFinJustiEconomica
     */
    @Column(name = "FC_FIN_JUSTI_ECONOMICA")
    public Date getFcFinJustiEconomica() {
        return this.fcFinJustiEconomica;
    }

    /**
     * Establece el valor del campo fcFinJustiEconomica de la entidad
     * ConvocatoriaJusEco
     *
     * @param fcFinJustiEconomica El valor a establecer
     */
    public void setFcFinJustiEconomica(Date fcFinJustiEconomica) {
        this.fcFinJustiEconomica = fcFinJustiEconomica;
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
     * ConvocatoriaJusEco
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
     * Establece el valor del campo cdUsuAlta de la entidad ConvocatoriaJusEco
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
     * Establece el valor del campo fcAltaFila de la entidad ConvocatoriaJusEco
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
     * Establece el valor del campo cdUsuModif de la entidad ConvocatoriaJusEco
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
     * Establece el valor del campo fcModifFila de la entidad ConvocatoriaJusEco
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
     * Establece el valor del campo cdUsuBaja de la entidad ConvocatoriaJusEco
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
     * Establece el valor del campo fcBajaFila de la entidad ConvocatoriaJusEco
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
