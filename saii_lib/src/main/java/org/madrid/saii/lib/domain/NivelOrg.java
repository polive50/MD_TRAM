package org.madrid.saii.lib.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.transaction.annotation.Transactional;

import lombok.ToString;

/**
 * Representa el objeto de dominio NivelOrg
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_NIVEL_ORG")
@Transactional(readOnly = true)
@ToString
public class NivelOrg implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idNivelOrg del objeto de dominio
     */
    private Integer idNivelOrg;

    /**
     * Representa el campo dsNivelOrg del objeto de dominio
     */
    private String dsNivelOrg;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public NivelOrg() {
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
        return new HashCodeBuilder().append(this.idNivelOrg).append(this.dsNivelOrg).toHashCode();
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
        if (!(obj instanceof NivelOrg)) {
            return false;
        }
        NivelOrg castObj = (NivelOrg) obj;
        return new EqualsBuilder().append(this.idNivelOrg, castObj.idNivelOrg)
                .append(this.dsNivelOrg, castObj.dsNivelOrg).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idNivelOrg
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_NIVEL_ORG")
    @SequenceGenerator(name = "SAII_S_NIVEL_ORG", sequenceName = "SAII_S_NIVEL_ORG", allocationSize = 1)
    @Column(name = "ID_NIVEL_ORG")
    public Integer getIdNivelOrg() {
        return this.idNivelOrg;
    }

    /**
     * Establece el valor del campo idNivelOrg de la entidad NivelOrg
     *
     * @param idNivelOrg El valor a establecer
     */
    public void setIdNivelOrg(Integer idNivelOrg) {
        this.idNivelOrg = idNivelOrg;
    }

    /**
     * @return Devuelve el valor del campo dsNivelOrg
     */

    @Column(name = "DS_NIVEL_ORG")
    public String getDsNivelOrg() {
        return this.dsNivelOrg;
    }

    /**
     * Establece el valor del campo dsNivelOrg de la entidad NivelOrg
     *
     * @param dsNivelOrg El valor a establecer
     */
    public void setDsNivelOrg(String dsNivelOrg) {
        this.dsNivelOrg = dsNivelOrg;
    }

}
