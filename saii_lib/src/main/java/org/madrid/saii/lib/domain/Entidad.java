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
 * Representa el objeto de dominio Entidad
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_ENTIDAD")
@AtlasAuditarFila
@ToString
public class Entidad implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idEntidad del objeto de dominio
     */
    private Integer idEntidad;

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
     * Representa el campo dsAbrev del objeto de dominio
     */
    private String dsAbrev;

    /**
     * Representa el campo dsCentro del objeto de dominio
     */
    private String dsCentro;

    /**
     * Representa el campo dsDepartamento del objeto de dominio
     */
    private String dsDepartamento;

    /**
     * Representa el campo dsFax del objeto de dominio
     */
    private String dsFax;

    /**
     * Representa el campo tlWeb del objeto de dominio
     */
    private String tlWeb;

    /**
     * Representa el campo interesado del objeto de dominio
     */
    private Interesado interesado;

    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;
    
    /**
     * Representa el campo tipoEntidad del objeto de dominio
     */
    private CatalogosValores tipoEntidad;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Entidad() {
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
        return new HashCodeBuilder().append(this.idEntidad).append(this.dsAbrev)
                .append(this.dsCentro).append(this.dsDepartamento).append(this.dsFax)
                .append(this.tlWeb).toHashCode();
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
        if (!(obj instanceof Entidad)) {
            return false;
        }
        Entidad castObj = (Entidad) obj;
        return new EqualsBuilder().append(this.idEntidad, castObj.idEntidad)
                .append(this.cdUsuAlta, castObj.cdUsuAlta).append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.fcModifFila, castObj.fcModifFila).append(this.dsAbrev, castObj.dsAbrev)
                .append(this.dsCentro, castObj.dsCentro)
                .append(this.dsDepartamento, castObj.dsDepartamento)
                .append(this.dsFax, castObj.dsFax).append(this.tlWeb, castObj.tlWeb).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idEntidad
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_ENTIDAD")
    @SequenceGenerator(name = "SAII_S_ENTIDAD", sequenceName = "SAII_S_ENTIDAD", allocationSize = 1)
    @Column(name = "ID_ENTIDAD")
    public Integer getIdEntidad() {
        return this.idEntidad;
    }

    /**
     * Establece el valor del campo idEntidad de la entidad Entidad
     *
     * @param idEntidad El valor a establecer
     */
    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    /**
     * @return Devuelve el valor del campo dsAbrev
     */
    @Column(name = "DS_ABREV", nullable = true)
    public String getDsAbrev() {
        return this.dsAbrev;
    }

    /**
     * Establece el valor del campo dsAbrev de la entidad Entidad
     *
     * @param dsAbrev El valor a establecer
     */
    public void setDsAbrev(String dsAbrev) {
        this.dsAbrev = dsAbrev;
    }

    /**
     * @return Devuelve el valor del campo dsCentro
     */
    @Column(name = "DS_CENTRO", nullable = true)
    public String getDsCentro() {
        return this.dsCentro;
    }

    /**
     * Establece el valor del campo dsCentro de la entidad Entidad
     *
     * @param dsCentro El valor a establecer
     */
    public void setDsCentro(String dsCentro) {
        this.dsCentro = dsCentro;
    }

    /**
     * @return Devuelve el valor del campo dsDepartamento
     */
    @Column(name = "DS_DEPARTAMENTO", nullable = true)
    public String getDsDepartamento() {
        return this.dsDepartamento;
    }

    /**
     * Establece el valor del campo dsDepartamento de la entidad Entidad
     *
     * @param dsDepartamento El valor a establecer
     */
    public void setDsDepartamento(String dsDepartamento) {
        this.dsDepartamento = dsDepartamento;
    }

    /**
     * @return Devuelve el valor del campo dsFax
     */
    @Column(name = "DS_FAX", nullable = true)
    public String getDsFax() {
        return this.dsFax;
    }

    /**
     * Establece el valor del campo dsFax de la entidad Entidad
     *
     * @param dsFax El valor a establecer
     */
    public void setDsFax(String dsFax) {
        this.dsFax = dsFax;
    }

    /**
     * @return Devuelve el valor del campo tlWeb
     */
    @Column(name = "TL_WEB", nullable = true)
    public String getTlWeb() {
        return this.tlWeb;
    }

    /**
     * Establece el valor del campo tlWeb de la entidad Entidad
     *
     * @param tlWeb El valor a establecer
     */
    public void setTlWeb(String tlWeb) {
        this.tlWeb = tlWeb;
    }

    /**
     * @return Devuelve el valor del campo interesado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    public Interesado getInteresado() {
        return this.interesado;
    }

    /**
     * Establece el valor del campo interesado de la entidad Entidad
     *
     * @param interesado El valor a establecer
     */
    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
    }

    /**
     * @return Devuelve el valor del campo estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    /**
     * Establece el valor del campo estado de la entidad Entidad
     *
     * @param estado El valor a establecer
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
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
     * @return Devuelve el valor del campo tipoEntidad
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO_ENTIDAD", nullable = true)
    public CatalogosValores getTipoEntidad() {
        return this.tipoEntidad;
    }

    public void setTipoEntidad(CatalogosValores tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}
