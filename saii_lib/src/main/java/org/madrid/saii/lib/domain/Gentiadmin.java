package org.madrid.saii.lib.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

import atlas.core.annotation.AtlasLegacy;

/**
 * Representa el objeto de dominio <code>{@link Gentiadmin}</code>
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "GENTIADMIN")
@Transactional(readOnly = true)
@AtlasLegacy
public class Gentiadmin implements java.io.Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdUnidad del objeto de dominio
     */
    private String cdUnidad;

    /**
     * Representa el campo dsExten del objeto de dominio
     */
    private String dsExten;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public Gentiadmin() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.cdUnidad == null ? 0 : this.cdUnidad.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Gentiadmin other = (Gentiadmin) obj;
        if (this.cdUnidad == null) {
            if (other.cdUnidad != null) {
                return false;
            }
        } else if (!this.cdUnidad.equals(other.cdUnidad)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////
    /**
     * @return the cdUnidad
     */
    @Id
    @Column(name = "CDUNIDAD", nullable = false)
    public String getCdUnidad() {
        return this.cdUnidad;
    }

    /**
     * @param cdUnidad the cdUnidad to set
     */
    public void setCdUnidad(String cdUnidad) {
        this.cdUnidad = cdUnidad;
    }

    /**
     * @return the dsExten
     */
    @Column(name = "DSEXTEN", nullable = false)
    public String getDsExten() {
        return this.dsExten;
    }

    /**
     * @param dsExten the dsExten to set
     */
    public void setDsExten(String dsExten) {
        this.dsExten = dsExten;
    }

}
