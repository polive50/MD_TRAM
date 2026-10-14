package org.madrid.saii.lib.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author everis
 * @version 1.0.0
 */
@Embeddable
public class SucaMunicipioId implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdProvincia de la entidad
     */
    private String cdProv;

    /**
     * Representa el campo cdMunicipio de la entidad
     */
    private String cdMuni;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public SucaMunicipioId() {
        super();
    }

    public SucaMunicipioId(String cdProv, String cdMuni) {
        super();
        this.cdProv = cdProv;
        this.cdMuni = cdMuni;
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
        SucaMunicipioId other = (SucaMunicipioId) obj;
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
    @Column(name = "CDPROV", nullable = false)
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
     * @return the cdMuni
     */
    @Column(name = "CDMUNI", insertable = false, updatable = false, nullable = false)
    public String getCdMuni() {
        return this.cdMuni;
    }

    /**
     * @param cdMuni the cdMuni to set
     */
    public void setCdMuni(String cdMuni) {
        this.cdMuni = cdMuni;
    }

}
