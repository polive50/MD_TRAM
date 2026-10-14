package org.madrid.saii.lib.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;

/**
 * Representa el objeto de dominio <code>{@link SucaTipoVial}</code>
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SUCA_TIPO_VIAL")
@Transactional(readOnly = true)
public class SucaTipoVial implements java.io.Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdTvia del objeto de dominio
     */
    private String cdTvia;

    /**
     * Representa el campo dsTvia del objeto de dominio
     */
    private String dsTvia;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public SucaTipoVial() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.cdTvia == null ? 0 : this.cdTvia.hashCode());
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
        SucaTipoVial other = (SucaTipoVial) obj;
        if (this.cdTvia == null) {
            if (other.cdTvia != null) {
                return false;
            }
        } else if (!this.cdTvia.equals(other.cdTvia)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////
    /**
     * @return the cdTvia
     */
    @Id
    @Column(name = "CDTVIA", nullable = false)
    public String getCdTvia() {
        return this.cdTvia;
    }

    /**
     * @param cdTvia the cdTvia to set
     */
    public void setCdTvia(String cdTvia) {
        this.cdTvia = cdTvia;
    }

    /**
     * @return the dsTvia
     */
    @Column(name = "DSTVIA_INI", nullable = false)
    public String getDsTvia() {
        return this.dsTvia;
    }

    /**
     * @param dsTvia the dsTvia to set
     */
    public void setDsTvia(String dsTvia) {
        this.dsTvia = dsTvia;
    }

}
