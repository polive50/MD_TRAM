package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ConvocatoriaPlaFir
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_PLA_FIR")
@AtlasAuditarFila
@ToString
public class ConvocatoriaPlaFir implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaPlaFir del objeto de dominio
     */
    private Integer idConvocatoriaPlaFir;

    /**
     * Representa el campo idConvocatoriaPla del objeto de dominio
     */
    private Integer idConvocatoriaPla;

    /**
     * Representa el campo cdFirmante del objeto de dominio
     */
    private String cdFirmante;

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
    public ConvocatoriaPlaFir() {
        super();
    }

    /**
     * Constructor de la entidad ConvocatoriaPlaFir con todos los parámetros
     *
     * @param idConvocatoriaPlaFir Valor del campo IdConvocatoriaPlaFir
     * @param idConvocatoriaPla Valor del campo IdConvocatoriaPla
     * @param cdFirmante Valor del campo CdFirmante
     * @param cdUsuAlta Valor del campo CdUsuAlta
     * @param fcAltaFila Valor del campo FcAltaFila
     * @param cdUsuModif Valor del campo CdUsuModif
     * @param fcModifFila Valor del campo FcModifFila
     * @param cdUsuBaja Valor del campo CdUsuBaja
     * @param fcBajaFila Valor del campo FcBajaFila
     */
    public ConvocatoriaPlaFir(Integer idConvocatoriaPlaFir, Integer idConvocatoriaPla,
            String cdFirmante, String cdUsuAlta, Date fcAltaFila, String cdUsuModif,
            Date fcModifFila, String cdUsuBaja, Date fcBajaFila) {
        this.idConvocatoriaPlaFir = idConvocatoriaPlaFir;
        this.idConvocatoriaPla = idConvocatoriaPla;
        this.cdFirmante = cdFirmante;
        this.cdUsuAlta = cdUsuAlta;
        this.fcAltaFila = fcAltaFila;
        this.cdUsuModif = cdUsuModif;
        this.fcModifFila = fcModifFila;
        this.cdUsuBaja = cdUsuBaja;
        this.fcBajaFila = fcBajaFila;
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
        return new HashCodeBuilder().append(this.idConvocatoriaPlaFir)
                .append(this.idConvocatoriaPla).append(this.cdFirmante).append(this.cdUsuAlta)
                .append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof ConvocatoriaPlaFir)) {
            return false;
        }
        ConvocatoriaPlaFir castObj = (ConvocatoriaPlaFir) obj;
        return new EqualsBuilder().append(this.idConvocatoriaPlaFir, castObj.idConvocatoriaPlaFir)
                .append(this.idConvocatoriaPla, castObj.idConvocatoriaPla)
                .append(this.cdFirmante, castObj.cdFirmante)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoriaPlaFir
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_PLA_FIR")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_PLA_FIR", sequenceName = "SAII_S_CONVOCATORIA_PLA_FIR", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_PLA_FIR")
    public Integer getIdConvocatoriaPlaFir() {
        return this.idConvocatoriaPlaFir;
    }

    /**
     * Establece el valor del campo idConvocatoriaPlaFir de la entidad
     * ConvocatoriaPlaFir
     *
     * @param idConvocatoriaPlaFir El valor a establecer
     */
    public void setIdConvocatoriaPlaFir(Integer idConvocatoriaPlaFir) {
        this.idConvocatoriaPlaFir = idConvocatoriaPlaFir;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoriaPla
     */
    @Column(name = "ID_CONVOCATORIA_PLA")
    public Integer getIdConvocatoriaPla() {
        return this.idConvocatoriaPla;
    }

    /**
     * Establece el valor del campo idConvocatoriaPla de la entidad
     * ConvocatoriaPlaFir
     *
     * @param idConvocatoriaPla El valor a establecer
     */
    public void setIdConvocatoriaPla(Integer idConvocatoriaPla) {
        this.idConvocatoriaPla = idConvocatoriaPla;
    }

    /**
     * @return Devuelve el valor del campo cdFirmante
     */
    @Column(name = "CD_FIRMANTE")
    public String getCdFirmante() {
        return this.cdFirmante;
    }

    /**
     * Establece el valor del campo cdFirmante de la entidad ConvocatoriaPlaFir
     *
     * @param cdFirmante El valor a establecer
     */
    public void setCdFirmante(String cdFirmante) {
        this.cdFirmante = cdFirmante;
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
