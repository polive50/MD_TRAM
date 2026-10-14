package org.madrid.saii.lib.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Representa el objeto de dominio <code>{@link SucaMunicipio}</code>
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SUCA_MUNICIPIO", uniqueConstraints = @UniqueConstraint(columnNames = {"CDMUNI",
        "NMPROV" }))
@Transactional(readOnly = true)
public class SucaMunicipio implements java.io.Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo id del objeto de dominio
     */
    private SucaMunicipioId id;

    /**
     * Representa el campo cdPais del objeto de dominio
     */
    private String cdPais;

    /**
     * Representa el campo dsMuni del objeto de dominio
     */
    private String dsMuni;

    /**
     * Representa el campo nmProv del objeto de dominio
     */
    private Integer nmProv;

    /**
     * Representa el campo nmProv del objeto de dominio
     */
    private Integer nmMuni;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos
     */
    public SucaMunicipio() {
        super();
    }

    /**
     * Constructor con todos los argumentos
     */
    public SucaMunicipio(String cdPais, String dsMuni, Integer nmProv, Integer nmMuni) {
        super();
        this.cdPais = cdPais;
        this.dsMuni = dsMuni;
        this.nmProv = nmProv;
        this.nmMuni = nmMuni;
    }

    /**
     * Implementación de equals para uso con Hibernate
     * <p>
     * Más información en: {@link http://community.jboss.org/docs/DOC-13933}
     *
     * @param obj objeto a comparar con esta instancia.
     * @return true si el objeto pasado es igual a este; false en caso contrario
     */

    /**
     * Devuelve el valor de la Primary Key de la entidad en forma de String
     *
     * @return el valor de la Primary Key de la entidad en forma de String
     */
    @Transient
    @JsonIgnore
    public String getPKAsString() {
        if (this.id == null) {
            return null;
        }
        return String.valueOf(this.id);
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
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
        SucaMunicipio other = (SucaMunicipio) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////
    /**
     * @return the id
     */
    @EmbeddedId
    public SucaMunicipioId getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(SucaMunicipioId id) {
        this.id = id;
    }

    /**
     * @return the cdPais
     */
    @Column(name = "CDPAIS", nullable = false, length = 3)
    public String getCdPais() {
        return this.cdPais;
    }

    /**
     * @param cdPais the cdPais to set
     */
    public void setCdPais(String cdPais) {
        this.cdPais = cdPais;
    }

    /**
     * @return the dsMuni
     */
    @Column(name = "DSMUNI", nullable = false, length = 50)
    public String getDsMuni() {
        return this.dsMuni;
    }

    /**
     * @param dsMuni the dsMuni to set
     */
    public void setDsMuni(String dsMuni) {
        this.dsMuni = dsMuni;
    }

    /**
     * @return the nmProv
     */
    @Column(name = "NMPROV", nullable = false, precision = 1, scale = 0)
    public Integer getNmProv() {
        return this.nmProv;
    }

    /**
     * @param nmProv the nmProv to set
     */
    public void setNmProv(Integer nmProv) {
        this.nmProv = nmProv;
    }

    /**
     * @return the nmMuni
     */
    @Column(name = "NMMUNI", nullable = false, precision = 1, scale = 0)
    public Integer getNmMuni() {
        return this.nmMuni;
    }

    /**
     * @param nmMuni the nmMuni to set
     */
    public void setNmMuni(Integer nmMuni) {
        this.nmMuni = nmMuni;
    }

}
