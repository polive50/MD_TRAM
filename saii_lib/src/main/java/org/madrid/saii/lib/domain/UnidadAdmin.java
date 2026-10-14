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
import org.springframework.transaction.annotation.Transactional;

import lombok.ToString;

/**
 * Representa el objeto de dominio UnidadAdmin
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_UNIDAD_ADMIN")
@Transactional(readOnly = true)
@ToString
public class UnidadAdmin implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idUnidadAdmin del objeto de dominio
     */
    private Integer idUnidadAdmin;

    /**
     * Representa el campo cdunidad del objeto de dominio
     */
    private String cdUnidad;

    /**
     * Representa el campo cdunidad del objeto de dominio
     */
    private String cdUnidadPadre;

    /**
     *
     */
    private String dsUnidad;

    /**
     * Representa el campo idNivelOrg del objeto de dominio
     */
    private NivelOrg nivelOrg;

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
    public UnidadAdmin() {
        this.nivelOrg = new NivelOrg();
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
        return new HashCodeBuilder().append(this.idUnidadAdmin).toHashCode();
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
        if (!(obj instanceof UnidadAdmin)) {
            return false;
        }
        UnidadAdmin castObj = (UnidadAdmin) obj;
        return new EqualsBuilder().append(this.idUnidadAdmin, castObj.idUnidadAdmin).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idUnidadAdmin
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_UNIDAD_ADMIN")
    @SequenceGenerator(name = "SAII_S_UNIDAD_ADMIN", sequenceName = "SAII_S_UNIDAD_ADMIN", allocationSize = 1)
    @Column(name = "ID_UNIDAD_ADMIN")
    public Integer getIdUnidadAdmin() {
        return this.idUnidadAdmin;
    }

    /**
     * Establece el valor del campo idUnidadAdmin de la entidad UnidadAdmin
     *
     * @param idUnidadAdmin El valor a establecer
     */

    public void setIdUnidadAdmin(Integer idUnidadAdmin) {
        this.idUnidadAdmin = idUnidadAdmin;
    }

    /**
     * @return the unidad
     */
    @Column(name = "CD_UNIDAD")
    public String getCdUnidad() {
        return this.cdUnidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setCdUnidad(String cdUnidad) {
        this.cdUnidad = cdUnidad;
    }

    /**
     * @return the unidadPadre
     */
    @Column(name = "CD_UNIDAD_PADRE")
    public String getCdUnidadPadre() {
        return this.cdUnidadPadre;
    }

    /**
     * @param unidadPadre the unidadPadre to set
     */
    public void setCdUnidadPadre(String cdUnidadPadre) {
        this.cdUnidadPadre = cdUnidadPadre;
    }

    /**
     * @return the nivelOrg
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NIVEL_ORG", nullable = true)
    public NivelOrg getNivelOrg() {
        return this.nivelOrg;
    }

    /**
     * @param nivelOrg the nivelOrg to set
     */
    public void setNivelOrg(NivelOrg nivelOrg) {
        this.nivelOrg = nivelOrg;
    }

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */

    @Column(name = "DS_UNIDAD")
    public String getDsUnidad() {
        return this.dsUnidad;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Datos
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setDsUnidad(String dsUnidad) {
        this.dsUnidad = dsUnidad;
    }

    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Datos
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */

    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Datos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */

    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad Datos
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */

    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad Datos
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */

    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad Datos
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */

    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad Datos
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
