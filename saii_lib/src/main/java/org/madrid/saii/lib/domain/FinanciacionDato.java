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
 * Representa el objeto de dominio Financiación
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_FINANCIACION_DATO")
@AtlasAuditarFila
@ToString
public class FinanciacionDato implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idFinanciacion del objeto de dominio
     */
    private Integer idFinanciacionDato;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idFinanciacion;

    /**
     * Representa el campo idTipo del objeto de dominio
     */
    private CatalogosValores tipo;

    /**
     * Representa el campo nmImporteMax del objeto de dominio
     */
    private Double nmImporteMax;

    /**
     * Representa el campo nmAnualidad del objeto de dominio
     */
    private Integer nmAnualidad;

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
     * Representa el campo itImporteFijo el objeto de dominio
     */
    private String itImporteFijo;
    
    /**
     * Representa el campo nmImporteMaxAnual del objeto de dominio
     */
    private Double nmImporteMaxAnual;
    
    /**
     * Representa el campo nmOrden de la partida presupuestaria
     */
    private Integer nmOrden;
    
    /**
     * Representa el campo nmImporteMin del objeto de dominio
     */
    private Double nmImporteMin;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public FinanciacionDato() {
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
        return new HashCodeBuilder().append(this.idFinanciacionDato).append(this.idFinanciacion)
                .append(this.tipo).append(this.nmImporteMax).append(this.nmAnualidad)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila)
                .append(this.itImporteFijo).append(this.nmImporteMaxAnual).append(this.nmOrden)
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
        if (!(obj instanceof FinanciacionDato)) {
            return false;
        }
        FinanciacionDato castObj = (FinanciacionDato) obj;
        return new EqualsBuilder().append(this.idFinanciacionDato, castObj.idFinanciacionDato)
                .append(this.idFinanciacion, castObj.idFinanciacion).append(this.tipo, castObj.tipo)
                .append(this.nmImporteMax, castObj.nmImporteMax)
                .append(this.nmAnualidad, castObj.nmAnualidad)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.itImporteFijo, castObj.itImporteFijo)
                .append(this.nmImporteMaxAnual, castObj.nmImporteMaxAnual)
                .append(this.nmOrden,  castObj.nmOrden)
                .isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idFinanciacion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_FINANCIACION_DATO")
    @SequenceGenerator(name = "SAII_S_FINANCIACION_DATO", sequenceName = "SAII_S_FINANCIACION_DATO", allocationSize = 1)
    @Column(name = "ID_FINANCIACION_DATO")
    public Integer getIdFinanciacionDato() {
        return this.idFinanciacionDato;
    }

    /**
     * Establece el valor del campo idFinanciacion de la entidad Financiacion
     *
     * @param idFinanciacion El valor a establecer
     */
    public void setIdFinanciacionDato(Integer idFinanciacionDato) {
        this.idFinanciacionDato = idFinanciacionDato;
    }

    @Column(name = "ID_FINANCIACION")
    public Integer getIdFinanciacion() {
        return this.idFinanciacion;
    }

    /**
     * Establece el valor del campo idFinanciacion de la entidad Financiacion
     *
     * @param idFinanciacion El valor a establecer
     */
    public void setIdFinanciacion(Integer idFinanciacion) {
        this.idFinanciacion = idFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo idTipo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO", nullable = false)
    public CatalogosValores getTipo() {
        return this.tipo;
    }

    /**
     * Establece el valor del campo idTipo de la entidad Financiacion
     *
     * @param idTipo El valor a establecer
     */
    public void setTipo(CatalogosValores tipo) {
        this.tipo = tipo;
    }

    /**
     * @return Devuelve el valor del campo nmImporteMax
     */
    @Column(name = "NM_IMPORTE_MAX", nullable = true)
    public Double getNmImporteMax() {
        return this.nmImporteMax;
    }

    /**
     * Establece el valor del campo nmImporteMax de la entidad Financiacion
     *
     * @param nmImporteMax El valor a establecer
     */
    public void setNmImporteMax(Double nmImporteMax) {
        this.nmImporteMax = nmImporteMax;
    }

    /**
     * @return Devuelve el valor del campo nmAnualidad
     */
    @Column(name = "NM_ANUALIDAD", nullable = true)
    public Integer getNmAnualidad() {
        return this.nmAnualidad;
    }

    /**
     * Establece el valor del campo nmAnualidad de la entidad Financiacion
     *
     * @param nmAnualidad El valor a establecer
     */
    public void setNmAnualidad(Integer nmAnualidad) {
        this.nmAnualidad = nmAnualidad;
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
    
    /**
     * @return the itImporteFijo
     */
    @Column(name = "IT_IMPORTE_FIJO", nullable = false)
    public String getItImporteFijo() {
        return itImporteFijo;
    }

    /**
     * @param itImporteFijo the itImporteFijo to set
     */
    public void setItImporteFijo(String itImporteFijo) {
        this.itImporteFijo = itImporteFijo;
    }
    
    
    /**
     * @return Devuelve el valor del campo nmImporteMaxAnual
     */
    @Column(name = "NM_IMPORTE_MAX_ANUAL", nullable = true)
    public Double getNmImporteMaxAnual() {
        return this.nmImporteMaxAnual;
    }

    /**
     * Establece el valor del campo nmImporteMaxAnual de la entidad FinanciacionDato
     *
     * @param nmImporteMaxAnual El valor a establecer
     */
    public void setNmImporteMaxAnual(Double nmImporteMaxAnual) {
        this.nmImporteMaxAnual = nmImporteMaxAnual;
    }

    /**
     * @return the nmOrden
     */
    @Column(name = "NM_ORDEN", nullable = true)
    public Integer getNmOrden() {
        return nmOrden;
    }

    /**
     * @param nmOrden the nmOrden to set
     */
    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
    }

    @Column(name = "NM_IMPORTE_MIN", nullable = true)
    public Double getNmImporteMin() {
        return nmImporteMin;
    }

    public void setNmImporteMin(Double nmImporteMin) {
        this.nmImporteMin = nmImporteMin;
    }

    
  
}
