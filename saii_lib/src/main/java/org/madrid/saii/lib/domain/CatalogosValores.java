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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.hibernate.annotations.BatchSize;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio CatalogosValores
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CATALOGOS_VALORES")
@AtlasAuditarFila
@ToString
@BatchSize(size = 25)
public class CatalogosValores implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdUsuAlta del objeto de dominio
     */
    private String cdUsuAlta;

    /**
     * Representa el campo cdUsuBaja del objeto de dominio
     */
    private String cdUsuBaja;

    /**
     * Representa el campo cdUsuModif del objeto de dominio
     */
    private String cdUsuModif;

    /**
     * Representa el campo fcAltaFila del objeto de dominio
     */
    private Date fcAltaFila;

    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcBajaFila;

    /**
     * Representa el campo fcModifFila del objeto de dominio
     */
    private Date fcModifFila;

    /**
     * Representa el campo idValor del objeto de dominio
     */
    private Integer idValor;

    /**
     * Representa el campo dsValor del objeto de dominio
     */
    private String dsValor;

    /**
     * Representa el campo cdCodigo del objeto de dominio
     */
    private String cdCodigo;

    /**
     * Representa el campo itMantenible del objeto de dominio
     */
    private String itMantenible;

    /**
     * Representa el campo catalogo del objeto de dominio
     */
    private Catalogos catalogo;
    
    /**
     * Representa la relación con la tabla TipoResolModalidad
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TipoResolModalidad tipoResolModalidad;
    
    /**
     * Representa el campo nmConstante del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Integer nmConstante;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public CatalogosValores() {
        super();
    }

    /**
     * Constructor por idValor
     */
    public CatalogosValores(Integer idValor) {
        super();
        this.idValor = idValor;
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
        return new HashCodeBuilder().append(this.cdUsuAlta).append(this.cdUsuBaja)
                .append(this.cdUsuModif).append(this.fcAltaFila).append(this.fcBajaFila)
                .append(this.fcModifFila).append(this.idValor).append(this.dsValor)
                .append(this.cdCodigo).append(this.itMantenible).toHashCode();
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
        if (!(obj instanceof CatalogosValores)) {
            return false;
        }
        CatalogosValores castObj = (CatalogosValores) obj;
        return new EqualsBuilder().append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.fcModifFila, castObj.fcModifFila).append(this.idValor, castObj.idValor)
                .append(this.dsValor, castObj.dsValor).append(this.cdCodigo, castObj.cdCodigo)
                .append(this.itMantenible, castObj.itMantenible).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
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
     * Establece el valor del campo fcModifFila de la entidad CatalogosValores
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo idValor
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CATALOGOS_VALORES")
    @SequenceGenerator(name = "SAII_S_CATALOGOS_VALORES", sequenceName = "SAII_S_CATALOGOS_VALORES", allocationSize = 1)
    @Column(name = "ID_VALOR")
    public Integer getIdValor() {
        return this.idValor;
    }

    /**
     * Establece el valor del campo idValor de la entidad CatalogosValores
     *
     * @param idValor El valor a establecer
     */
    public void setIdValor(Integer idValor) {
        this.idValor = idValor;
    }

    /**
     * @return Devuelve el valor del campo dsValor
     */

    @Column(name = "DS_VALOR")
    public String getDsValor() {
        return this.dsValor;
    }

    /**
     * Establece el valor del campo dsValor de la entidad CatalogosValores
     *
     * @param dsValor El valor a establecer
     */
    public void setDsValor(String dsValor) {
        this.dsValor = dsValor;
    }

    /**
     * @return Devuelve el valor del campo cdCodigo
     */

    @Column(name = "CD_CODIGO", nullable = true)
    public String getCdCodigo() {
        return this.cdCodigo;
    }

    /**
     * Establece el valor del campo cdCodigo de la entidad CatalogosValores
     *
     * @param cdCodigo El valor a establecer
     */
    public void setCdCodigo(String cdCodigo) {
        this.cdCodigo = cdCodigo;
    }

    /**
     * @return Devuelve el valor del campo itMantenible
     */
    @Column(name = "IT_MANTENIBLE", nullable = false)
    public String getItMantenible() {
        return this.itMantenible;
    }

    /**
     * Establece el valor del campo itMantenible de la entidad Catalogos
     *
     * @param itMantenible El valor a establecer
     */
    public void setItMantenible(String itMantenible) {
        this.itMantenible = itMantenible;
    }

    /**
     * @return the idCatalogo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATALOGO", nullable = true)
    public Catalogos getCatalogo() {
        return this.catalogo;
    }

    /**
     * @param catalogo the catalogo to set
     */
    public void setCatalogo(Catalogos catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * @return the tipoResolModalidad
     */
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "idModalidad")
    public TipoResolModalidad getTipoResolModalidad() {
        return tipoResolModalidad;
    }

    /**
     * @param tipoResolModalidad the tipoResolModalidad to set
     */
    public void setTipoResolModalidad(TipoResolModalidad tipoResolModalidad) {
        this.tipoResolModalidad = tipoResolModalidad;
    }
    
    /**
     * @return Devuelve el valor del campo nmConstante
     */

    @Column(name = "NM_CONSTANTE")
    public Integer getNmConstante() {
        return this.nmConstante;
    }
    
    /**
     * Establece el valor del campo nmConstante de la entidad CatalogosValores
     *
     * @param nmConstante El valor a establecer
     */
    public void setNmConstante(Integer nmConstante) {
        this.nmConstante = nmConstante;
    }
    
}
