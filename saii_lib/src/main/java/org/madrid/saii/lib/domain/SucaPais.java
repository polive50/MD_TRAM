package org.madrid.saii.lib.domain;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SUCA_PAIS")
@Transactional(readOnly = true)
public class SucaPais implements Serializable {
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdPais del objeto de dominio
     */
    @Id
    @Column(name = "CDPAIS")
    private String cdPais;

    /**
     * Representa el campo cdPais del objeto de dominio
     */
    @Column(name = "DSPAIS")
    private String dsPais;

    /**
     *
     */
    @Column(name = "FCBAJA")
    private Date fcBaja;

    /**
     *
     */
    @Column(name = "IND_UE")
    private String indUe;

    /**
     *
     */
    @Column(name = "DSPAIS_MIN")
    private String dsPaisMin;

    /**
     *
     */
    @Column(name = "CDISOALFA2")
    private String cdIsoAlfa2;

    /**
     *
     */
    @Column(name = "CDISOALFA3")
    private String cdIsoAlfa3;

    /**
     *
     */
    @Column(name = "CDISONUMERICO")
    private String cdIsoNumerico;

    /**
     *
     */
    @Column(name = "ITMIEMBRO_ONU")
    private String itMiembroOnu;

    /**
     *
     */
    @Column(name = "DSGENTILICIO_M")
    private String dsGentilicioM;

    /**
     *
     */
    @Column(name = "DSGENTILICIO_F")
    private String dsGentilicioF;

    /**
     *
     */
    @Column(name = "DSPAISNORMA")
    private String dsPaisNorma;

    /**
     *
     */
    @Column(name = "CDPAIS_INE")
    private String cdPaisIne;

    /**
     *
     */
    @Column(name = "CDALFA2_UE")
    private String cdAlfa2Ue;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public SucaPais() {
        super();
    }

    /**
     * Constructor con todos los argumentos
     */
    public SucaPais(String cdPais, String dsPais, Date fcBaja, String indUe, String dsPaisMin, String cdIsoAlfa2,
                    String cdIsoAlfa3, String cdIsoNumerico, String itMiembroOnu, String dsGentilicioM,
                    String dsGentilicioF, String dsPaisNorma, String cdPaisIne, String cdAlfa2Ue) {
        this.cdPais = cdPais;
        this.dsPais = dsPais;
        this.fcBaja = fcBaja;
        this.indUe = indUe;
        this.dsPaisMin = dsPaisMin;
        this.cdIsoAlfa2 = cdIsoAlfa2;
        this.cdIsoAlfa3 = cdIsoAlfa3;
        this.cdIsoNumerico = cdIsoNumerico;
        this.itMiembroOnu = itMiembroOnu;
        this.dsGentilicioM = dsGentilicioM;
        this.dsGentilicioF = dsGentilicioF;
        this.dsPaisNorma = dsPaisNorma;
        this.cdPaisIne = cdPaisIne;
        this.cdAlfa2Ue = cdAlfa2Ue;
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
        if (this.cdPais == null) {
            return null;
        }
        return String.valueOf(this.cdPais);
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.cdPais == null ? 0 : this.cdPais.hashCode());
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
        SucaPais other = (SucaPais) obj;
        if (this.cdPais == null) {
            if (other.cdPais != null) {
                return false;
            }
        } else if (!this.cdPais.equals(other.cdPais)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////


    public String getCdPais() {
        return cdPais;
    }

    public void setCdPais(String cdPais) {
        this.cdPais = cdPais;
    }

    public String getDsPais() {
        return dsPais;
    }

    public void setDsPais(String dsPais) {
        this.dsPais = dsPais;
    }

    public Date getFcBaja() {
        return fcBaja;
    }

    public void setFcBaja(Date fcBaja) {
        this.fcBaja = fcBaja;
    }

    public String getIndUe() {
        return indUe;
    }

    public void setIndUe(String indUe) {
        this.indUe = indUe;
    }

    public String getDsPaisMin() {
        return dsPaisMin;
    }

    public void setDsPaisMin(String dsPaisMin) {
        this.dsPaisMin = dsPaisMin;
    }

    public String getCdIsoAlfa2() {
        return cdIsoAlfa2;
    }

    public void setCdIsoAlfa2(String cdIsoAlfa2) {
        this.cdIsoAlfa2 = cdIsoAlfa2;
    }

    public String getCdIsoAlfa3() {
        return cdIsoAlfa3;
    }

    public void setCdIsoAlfa3(String cdIsoAlfa3) {
        this.cdIsoAlfa3 = cdIsoAlfa3;
    }

    public String getCdIsoNumerico() {
        return cdIsoNumerico;
    }

    public void setCdIsoNumerico(String cdIsoNumerico) {
        this.cdIsoNumerico = cdIsoNumerico;
    }

    public String getItMiembroOnu() {
        return itMiembroOnu;
    }

    public void setItMiembroOnu(String itMiembroOnu) {
        this.itMiembroOnu = itMiembroOnu;
    }

    public String getDsGentilicioM() {
        return dsGentilicioM;
    }

    public void setDsGentilicioM(String dsGentilicioM) {
        this.dsGentilicioM = dsGentilicioM;
    }

    public String getDsGentilicioF() {
        return dsGentilicioF;
    }

    public void setDsGentilicioF(String dsGentilicioF) {
        this.dsGentilicioF = dsGentilicioF;
    }

    public String getDsPaisNorma() {
        return dsPaisNorma;
    }

    public void setDsPaisNorma(String dsPaisNorma) {
        this.dsPaisNorma = dsPaisNorma;
    }

    public String getCdPaisIne() {
        return cdPaisIne;
    }

    public void setCdPaisIne(String cdPaisIne) {
        this.cdPaisIne = cdPaisIne;
    }

    public String getCdAlfa2Ue() {
        return cdAlfa2Ue;
    }

    public void setCdAlfa2Ue(String cdAlfa2Ue) {
        this.cdAlfa2Ue = cdAlfa2Ue;
    }
}
