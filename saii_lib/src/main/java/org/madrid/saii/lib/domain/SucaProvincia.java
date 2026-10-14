package org.madrid.saii.lib.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * Representa el objeto de dominio SucaProvincia
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SUCA_PROVINCIA")
@Transactional(readOnly = true)
public class SucaProvincia implements java.io.Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdProv del objeto de dominio
     */
    private String cdProv;

    /**
     * Representa el campo dsProv del objeto de dominio
     */
    private String dsProv;

    /**
     * Representa el campo dsProvNorma del objeto de dominio
     */
    private String dsProvNorma;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public SucaProvincia() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.cdProv == null ? 0 : this.cdProv.hashCode());
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
        SucaProvincia other = (SucaProvincia) obj;
        if (this.cdProv == null) {
            if (other.cdProv != null) {
                return false;
            }
        } else if (!this.cdProv.equals(other.cdProv)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////
    /**
     * @return the cdProv
     */
    @Id
    @Column(name = "CDPROV", nullable = false, precision = 1, scale = 0)
    public String getCdProv() {
        return this.cdProv;
    }

    /**
     * @param cdProv the cdProv to set
     */
    public void setCdProv(String cdProv) {
        this.cdProv = cdProv;
    }

    /**
     * @return the dsProv
     */
    @Column(name = "DSPROV", nullable = false, length = 30)
    public String getDsProv() {
        return this.dsProv;
    }

    /**
     * @param dsProv the dsProv to set
     */
    public void setDsProv(String dsProv) {
        this.dsProv = dsProv;
    }

    /**
     * @return the dsProvNorma
     */
    @Column(name = "DSPROVNORMA", nullable = false, length = 30)
    public String getDsProvNorma() {
        return this.dsProvNorma;
    }

    /**
     * @param dsProvNorma the dsProvNorma to set
     */
    public void setDsProvNorma(String dsProvNorma) {
        this.dsProvNorma = dsProvNorma;
    }

}
