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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio Convocatoria
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_TIPO_RESOL_MODALIDAD")
@AtlasAuditarFila
@ToString
public class TipoResolModalidad implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -795991972347414853L;

    /**
     * Representa el campo idTipoResolModalidad del objeto de dominio
     */
    private Integer idTipoResolModalidad;
    
    /**
     * Representa el campo idModalidad del objeto de dominio
     */
    private CatalogosValores idModalidad;

    /**
     * Representa el campo cdTipoResolucion del objeto de dominio
     */
    private String cdTipoResolucion;

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
     * Constructor sin argumentos
     */
    public TipoResolModalidad() {
        super();
    }

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
        return new HashCodeBuilder().append(this.cdTipoResolucion).append(this.cdUsuAlta)
                .append(this.cdUsuBaja).append(this.cdUsuModif).append(this.fcAltaFila)
                .append(this.fcBajaFila).append(this.fcModifFila)
                .append(this.idTipoResolModalidad).toHashCode();
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TipoResolModalidad))
            return false;
        TipoResolModalidad castObj = (TipoResolModalidad) obj;
        return new EqualsBuilder().append(this.cdTipoResolucion, castObj.cdTipoResolucion)
                .append(this.cdUsuAlta, castObj.cdUsuAlta).append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.idTipoResolModalidad, castObj.idTipoResolModalidad)
                .isEquals();
    }

    /**
     * @return the idTipoResolModalidad
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_TIPO_RESOL_MODALIDAD")
    @SequenceGenerator(name = "SAII_S_TIPO_RESOL_MODALIDAD", sequenceName = "SAII_S_TIPO_RESOL_MODALIDAD", allocationSize = 1)
    @Column(name = "ID_TIPO_RESOL_MODALIDAD")
    public Integer getIdTipoResolModalidad() {
        return idTipoResolModalidad;
    }

    /**
     * Establece el valor del campo idTipoResolModalidad de la entidad
     * TipoResolModalidad
     * 
     * @param idTipoResolModalidad the idTipoResolModalidad to
     *            set
     */
    public void setIdTipoResolModalidad(Integer idTipoResolModalidad) {
        this.idTipoResolModalidad = idTipoResolModalidad;
    }
    
    /**
     * @return the idModalidad
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODALIDAD", nullable = false)
    public CatalogosValores getIdModalidad() {
        return idModalidad;
    }

    /**
     * @param idModalidad the idModalidad to set
     */
    public void setIdModalidad(CatalogosValores idModalidad) {
        this.idModalidad = idModalidad;
    }

    /**
     * @return the cdTipoResolucion
     */
    @Column(name = "CD_TIPO_RESOLUCION", nullable = false)
    public String getCdTipoResolucion() {
        return cdTipoResolucion;
    }

    /**
     * Establece el valor del campo cdTipoResolucion de la entidad
     * cdTipoResolucion
     * 
     * @param cdTipoResolucion the cdTipoResolucion to set
     */
    public void setCdTipoResolucion(String cdTipoResolucion) {
        this.cdTipoResolucion = cdTipoResolucion;
    }

    /**
     * @return the cdUsuAlta
     */
    @Column(name = "CD_USU_ALTA", nullable = false)
    public String getCdUsuAlta() {
        return cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad
     * TipoResolModalidad
     * 
     * @param cdUsuAlta the cdUsuAlta to set
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return the fcAltaFila
     */
    @Column(name = "FC_ALTA_FILA", nullable = false)
    public Date getFcAltaFila() {
        return fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad
     * TipoResolModalidad
     * 
     * @param fcAltaFila the fcAltaFila to set
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return the cdUsuModif
     */
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad
     * TipoResolModalidad
     * 
     * @param cdUsuModif the cdUsuModif to set
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return the fcModifFila
     */
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad
     * TipoResolModalidad
     * 
     * @param fcModifFila the fcModifFila to set
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return the cdUsuBaja
     */
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad
     * TipoResolModalidad
     * 
     * @param cdUsuBaja the cdUsuBaja to set
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return the fcBajaFila
     */
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad
     * TipoResolModalidad
     * 
     * @param fcBajaFila the fcBajaFila to set
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
