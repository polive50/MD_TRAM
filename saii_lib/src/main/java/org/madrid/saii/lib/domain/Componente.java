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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
 * Representa el objeto de dominio Componente.
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_COMPONENTE")
@AtlasAuditarFila
@ToString
public class Componente implements Serializable {

    /** Identificador del numero de serie para serialización. */
    private static final long serialVersionUID = 1L;

    /** Representa el campo idComponente del objeto de dominio. */
    private Integer idComponente;

    /** Representa el campo dsNombre del objeto de dominio. */
    private String dsNombre;

    /** Representa el campo nmLongitud del objeto de dominio. */
    private Integer nmLongitud;

    /** Representa el campo tipo del objeto de dominio. */
    private CatalogosValores tipo;

    /** Representa el campo subTipo del objeto de dominio. */
    private CatalogosValores subTipo;
    
    /** Representa el campo subTipoCatalogo del objeto de dominio. */
    private Catalogos subTipoCatalogo;

    /** Representa el campo tipoDocumento del objeto de dominio. */
    private CatalogosValores tipoDocumento;

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

    /** Representa el campo categoria del objeto de dominio. */
    private CatalogosValores categoria;

    /** Representa el campo regla del objeto de dominio. */
    private CatalogosValores regla;

    /**
     * Representa el campo dsValorMultiple del objeto de dominio.
     */
    private String dsValorMultiple;

    /**
     * Representa el campo tlTextoFijo del objeto de dominio.
     */
//    private String tlTextoFijo;

    /** Representa el campo v del objeto de dominio. */
    private String itEditable;
    
    /**
     * Representa el campo nmConstante del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Integer nmConstante;
    
    /**
     * Representa el exto fijo tipo CLOB
     */
    private String clTextoFijo;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos.
     */
    public Componente() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    public Componente(Integer idComponente) {
        super();
        this.idComponente = idComponente;
    }

    /**
     * Implementación de hashCode para uso de Hibernate.
     *
     * @return El código hash del objeto
     *         <p>
     *         Más información en:
     *         {@link http://community.jboss.org/docs/DOC-13933}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.idComponente).append(this.categoria)
                .append(this.dsNombre).append(this.nmLongitud).append(this.regla)
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
        if (!(obj instanceof Componente)) {
            return false;
        }
        Componente castObj = (Componente) obj;
        return new EqualsBuilder().append(this.idComponente, castObj.idComponente)
                .append(this.categoria, castObj.categoria).append(this.dsNombre, castObj.dsNombre)
                .append(this.nmLongitud, castObj.nmLongitud).append(this.regla, castObj.regla)
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
     * Gets the id componente.
     *
     * @return Devuelve el valor del campo idComponente
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_COMPONENTE")
    @SequenceGenerator(name = "SAII_S_COMPONENTE", sequenceName = "SAII_S_COMPONENTE", allocationSize = 1)
    @Column(name = "ID_COMPONENTE")
    public Integer getIdComponente() {
        return this.idComponente;
    }

    /**
     * Establece el valor del campo idComponente de la entidad Componente.
     *
     * @param idComponente El valor a establecer
     */
    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    /**
     * Gets the ds nombre.
     *
     * @return Devuelve el valor del campo dsNombre
     */

    @Column(name = "DS_NOMBRE", nullable = true)
    public String getDsNombre() {
        return this.dsNombre;
    }

    /**
     * Establece el valor del campo dsNombre de la entidad Componente.
     *
     * @param dsNombre El valor a establecer
     */
    public void setDsNombre(String dsNombre) {
        this.dsNombre = dsNombre;
    }

    /**
     * Gets the nm longitud.
     *
     * @return Devuelve el valor del campo nmLongitud
     */

    @Column(name = "NM_LONGITUD", nullable = true)
    public Integer getNmLongitud() {
        return this.nmLongitud;
    }

    /**
     * Establece el valor del campo nmLongitud de la entidad Componente.
     *
     * @param nmLongitud El valor a establecer
     */
    public void setNmLongitud(Integer nmLongitud) {
        this.nmLongitud = nmLongitud;
    }

    /**
     * Gets the tipo.
     *
     * @return the tipo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO", nullable = true)
    public CatalogosValores getTipo() {
        return this.tipo;
    }

    /**
     * Sets the id tipo.
     *
     * @param tipo the new id tipo
     */
    public void setTipo(CatalogosValores tipo) {
        this.tipo = tipo;
    }

    /**
     * Gets the id sub tipo.
     *
     * @return the id sub tipo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUB_TIPO", nullable = true)
    public CatalogosValores getSubTipo() {
        return this.subTipo;
    }

    /**
     * Sets the id sub tipo.
     *
     * @param subTipo the new id sub tipo
     */
    public void setSubTipo(CatalogosValores subTipo) {
        this.subTipo = subTipo;
    }
    
    /**
     * Gets the id sub tipo catalogo.
     *
     * @return the id sub tipo catalogo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUB_TIPO_CATALOGO", nullable = true)
    public Catalogos getSubTipoCatalogo() {
        return this.subTipoCatalogo;
    }

    /**
     * Sets the id sub tipo.
     *
     * @param subTipoCatalogo the new id sub tipo catalogo
     */
    public void setSubTipoCatalogo(Catalogos subTipoCatalogo) {
        this.subTipoCatalogo = subTipoCatalogo;
    }

    /**
     * @return the tipoDocumento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_DOCUMENTO", nullable = true)
    public CatalogosValores getTipoDocumento() {
        return this.tipoDocumento;
    }

    /**
     * @param idTipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(CatalogosValores tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * Gets the id categoria.
     *
     * @return Devuelve el valor del campo dsCategoria
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA")
    public CatalogosValores getCategoria() {
        return this.categoria;
    }

    /**
     * Establece el valor del campo dsCategoria de la entidad Componente.
     *
     * @param idCategoria the new d id categoria
     */
    public void setCategoria(CatalogosValores categoria) {
        this.categoria = categoria;
    }

    /**
     * Gets the regla.
     *
     * @return Devuelve el valor del campo regla
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REGLA")
    public CatalogosValores getRegla() {
        return this.regla;
    }

    /**
     * Establece el valor del campo idRegla de la entidad Componente.
     *
     * @param idRegla the new id regla
     */
    public void setRegla(CatalogosValores regla) {
        this.regla = regla;
    }

    /**
     * Devuelve el valor del atributo dsValorMultiple
     *
     * @return dsValorMultiple Valores múltiples para el componente
     */
    @Column(name = "DS_VALOR_MULTIPLE", nullable = true)
    public String getDsValorMultiple() {
        return this.dsValorMultiple;
    }

    /**
     * Asigna el valor del atributo dsValorMultiple
     *
     * @param dsValorMultiple Valores múltiples para el componente
     */
    public void setDsValorMultiple(String dsValorMultiple) {
        this.dsValorMultiple = dsValorMultiple;
    }

    /**
     * Devuelve el valor del atributo tlTextoFijo
     *
     * @return the tlTextoFijo
     */
//    @Column(name = "TL_TEXTO_FIJO", nullable = true)
//    public String getTlTextoFijo() {
//        return this.tlTextoFijo;
//    }

    /**
     * Asigna el valor del atributo tlTextoFijo
     *
     * @param tlTextoFijo the tlTextoFijo to set
     */
//    public void setTlTextoFijo(String tlTextoFijo) {
//        this.tlTextoFijo = tlTextoFijo;
//    }

    /**
     * @return the itEditable
     */
    @Column(name = "IT_EDITABLE", nullable = true)
    public String getItEditable() {
        return this.itEditable;
    }

    /**
     * @param itEditable the itEditable to set
     */
    public void setItEditable(String itEditable) {
        this.itEditable = itEditable;
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
     * @return Devuelve el valor del campo Componente
     */

    @Column(name = "NM_CONSTANTE")
    public Integer getNmConstante() {
        return this.nmConstante;
    }
    
    /**
     * Establece el valor del campo nmConstante de la entidad Componente
     *
     * @param nmConstante El valor a establecer
     */
    public void setNmConstante(Integer nmConstante) {
        this.nmConstante = nmConstante;
    }

    /**
     * @return the clTextoFijo
     */
    @Lob
    @Column(name = "CL_TEXTO_FIJO")
    public String getClTextoFijo() {
        return clTextoFijo;
    }

    /**
     * @param clTextoFijo the clTextoFijo to set
     */
    public void setClTextoFijo(String clTextoFijo) {
        this.clTextoFijo = clTextoFijo;
    }
    
    

}
