package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio Bloque_Componente.
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_BLOQUE_COMPONENTE")
@AtlasAuditarFila
@ToString
public class BloqueComponente implements Serializable {

    /** Identificador del numero de serie para serialización. */
    private static final long serialVersionUID = 1L;

    private Integer idBloqueComponente;

    /** Representa el campo idBloque del objeto de dominio. */
    private Bloque bloque;

    /** Representa el campo idComponente del objeto de dominio. */
    private Componente componente;

    /** Representa el campo dsFila del objeto de dominio. */
    private String itFila;

    /** Representa el campo dsColumna del objeto de dominio. */
    private String itColumna;

    /** Representa el campo cdUsuAlta del objeto de dominio. */
    private String cdUsuAlta;

    /** Representa el campo fcAltaFila del objeto de dominio. */
    private Date fcAltaFila;

    /** Representa el campo cdUsuModif del objeto de dominio. */
    private String cdUsuModif;

    /** Representa el campo fcModifFila del objeto de dominio. */
    private Date fcModifFila;

    /** Representa el campo cdUsuBaja del objeto de dominio. */
    private String cdUsuBaja;

    /** Representa el campo fcBajaFila del objeto de dominio. */
    private Date fcBajaFila;

    /** Representa el campo nmOrden del objeto de dominio. */
    private Integer nmOrden;

    /** Representa el campo itObligatorio del objeto de dominio. */
    private String itObligatorio;

    /** Representa el campo itRelacion del objeto de dominio. */
    private String itRelacion;

    /** */
    private CatalogosValores tipoFinanciacion;

    /** Representa el campo tramite del objeto de dominio. */
    private CatalogosValores tramite;
    
    /**
     * Lista de datos
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Dato> lstDato;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos.
     */
    public BloqueComponente() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.idBloqueComponente).append(this.componente)
                .append(this.bloque).append(this.itFila).append(this.itColumna).append(this.nmOrden)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BloqueComponente)) {
            return false;
        }
        BloqueComponente castObj = (BloqueComponente) obj;
        return new EqualsBuilder().append(this.idBloqueComponente, castObj.idBloqueComponente)
                .append(this.componente, castObj.componente).append(this.bloque, castObj.bloque)
                .append(this.itFila, castObj.itFila).append(this.itColumna, castObj.itColumna)
                .append(this.nmOrden, castObj.nmOrden).append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_BLOQUE_COMPONENTE")
    @SequenceGenerator(name = "SAII_S_BLOQUE_COMPONENTE", sequenceName = "SAII_S_BLOQUE_COMPONENTE", allocationSize = 1)
    @Column(name = "ID_BLOQUE_COMPONENTE")
    public Integer getIdBloqueComponente() {
        return this.idBloqueComponente;
    }

    public void setIdBloqueComponente(Integer idBloqueComponente) {
        this.idBloqueComponente = idBloqueComponente;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_BLOQUE")
    public Bloque getBloque() {
        return this.bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COMPONENTE")
    public Componente getComponente() {
        return this.componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Column(name = "IT_FILA", nullable = true)
    public String getItFila() {
        return this.itFila;
    }

    public void setItFila(String itFila) {
        this.itFila = itFila;
    }

    @Column(name = "IT_COLUMNA", nullable = true)
    public String getItColumna() {
        return this.itColumna;
    }

    public void setItColumna(String itColumna) {
        this.itColumna = itColumna;
    }

    @Column(name = "NM_ORDEN", nullable = true)
    public Integer getNmOrden() {
        return this.nmOrden;
    }

    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
    }

    /**
     * @return the itObligatorio
     */
    @Column(name = "IT_OBLIGATORIO", nullable = true)
    public String getItObligatorio() {
        return this.itObligatorio;
    }

    /**
     * @param itObligatorio the itObligatorio to set
     */
    public void setItObligatorio(String itObligatorio) {
        this.itObligatorio = itObligatorio;
    }

    /**
     * @return the itRelacion
     */
    @Column(name = "IT_RELACION", nullable = true)
    public String getItRelacion() {
        return this.itRelacion;
    }

    /**
     * @param itRelacion the itRelacion to set
     */
    public void setItRelacion(String itRelacion) {
        this.itRelacion = itRelacion;
    }

    /**
     * @return Devuelve el valor del campo idTipoFinanciacion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FINANCIACION")
    public CatalogosValores getTipoFinanciacion() {
        return this.tipoFinanciacion;
    }

    /**
     * Establece el valor del campo idTipoFinanciacion
     *
     * @param idTramite El valor a establecer
     */
    public void setTipoFinanciacion(CatalogosValores tipoFinanciacion) {
        this.tipoFinanciacion = tipoFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo tramite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE")
    public CatalogosValores getTramite() {
        return this.tramite;
    }

    /**
     * Establece el valor del campo tramite
     *
     * @param idTramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
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
    
    /**
     * @return the lstDato
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bloqueComponente")
    public List<Dato> getLstDato() {
        return this.lstDato;
    }

    /**
     * @param lstDato the lstDato to set
     */
    public void setLstDato(List<Dato> lstDato) {
        this.lstDato = lstDato;
    }
    
}
