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
 * Representa el objeto de dominio PersonaOrg
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_PERSONA_ORG")
@AtlasAuditarFila
@ToString
public class PersonaOrg implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idPersonaOrg del objeto de dominio
     */
    private Integer idPersonaOrg;

    /**
     * Representa el campo idInteresado del objeto de dominio
     */
    private Integer idInteresado;

    /**
     * Representa el campo idOrganismo del objeto de dominio
     */
    private CatalogosValores organismo;

    /**
     * Representa el campo dsOtroOrganismo del objeto de dominio
     */
    private String dsOtroOrganismo;

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
    public PersonaOrg() {
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
        return new HashCodeBuilder().append(this.idPersonaOrg).append(this.idInteresado)
                .append(this.dsOtroOrganismo).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja)
                .append(this.fcBajaFila).append(this.organismo).toHashCode();
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
        if (!(obj instanceof PersonaOrg)) {
            return false;
        }
        PersonaOrg castObj = (PersonaOrg) obj;
        return new EqualsBuilder().append(this.idPersonaOrg, castObj.idPersonaOrg)
                .append(this.idInteresado, castObj.idInteresado)
                .append(this.dsOtroOrganismo, castObj.dsOtroOrganismo)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.organismo, castObj.organismo).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idPersonaOrg
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_PERSONA_ORG")
    @SequenceGenerator(name = "SAII_S_PERSONA_ORG", sequenceName = "SAII_S_PERSONA_ORG", allocationSize = 1)
    @Column(name = "ID_PERSONA_ORG")
    public Integer getIdPersonaOrg() {
        return this.idPersonaOrg;
    }

    /**
     * Establece el valor del campo idPersonaOrg de la entidad PersonaOrg
     *
     * @param idPersonaOrg El valor a establecer
     */
    public void setIdPersonaOrg(Integer idPersonaOrg) {
        this.idPersonaOrg = idPersonaOrg;
    }

    /**
     * @return Devuelve el valor del campo idInteresado
     */
    @Column(name = "ID_INTERESADO")
    public Integer getIdInteresado() {
        return this.idInteresado;
    }

    /**
     * Establece el valor del campo idInteresado de la entidad PersonaOrg
     *
     * @param idInteresado El valor a establecer
     */
    public void setIdInteresado(Integer idInteresado) {
        this.idInteresado = idInteresado;
    }

    /**
     * @return Devuelve el valor del campo idOrganismo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ORGANISMO", nullable = false)
    public CatalogosValores getOrganismo() {
        return this.organismo;
    }

    /**
     * Establece el valor del campo idOrganismo de la entidad PersonaOrg
     *
     * @param idOrganismo El valor a establecer
     */
    public void setOrganismo(CatalogosValores organismo) {
        this.organismo = organismo;
    }

    /**
     * @return Devuelve el valor del campo dsOtroOrganismo
     */
    @Column(name = "DS_OTRO_ORGANISMO", nullable = true)
    public String getDsOtroOrganismo() {
        return this.dsOtroOrganismo;
    }

    /**
     * Establece el valor del campo dsOtroOrganismo de la entidad PersonaOrg
     *
     * @param dsOtroOrganismo El valor a establecer
     */
    public void setDsOtroOrganismo(String dsOtroOrganismo) {
        this.dsOtroOrganismo = dsOtroOrganismo;
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

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}
