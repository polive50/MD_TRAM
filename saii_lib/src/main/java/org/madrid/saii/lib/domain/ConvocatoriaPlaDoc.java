
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
 * Representa el objeto de dominio ConvocatoriaPlaDoc
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_PLA_DOC")
@AtlasAuditarFila
@ToString
public class ConvocatoriaPlaDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaPlaDoc del objeto de dominio
     */
    private Integer idConvocatoriaPlaDoc;

    /**
     * Representa el campo idConvocatoriaPla del objeto de dominio
     */
    private Integer idConvocatoriaPla;

    /**
     * Representa el campo dsDocumentacion del objeto de dominio
     */
    private String dsDocumentacion;
    
    /**
     * Representa el campo dsObservaciones del objeto de dominio
     */
    private String dsObservaciones;

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
    public ConvocatoriaPlaDoc() {
        super();
    }

    /**
     * Constructor de la entidad ConvocatoriaPlaDoc con todos los parámetros
     *
     * @param idConvocatoriaPlaDoc Valor del campo IdConvocatoriaPlaDoc
     * @param idConvocatoriaPla Valor del campo IdConvocatoriaPla
     * @param dsDocumentacion Valor del campo dsDocumentacion
     * @param dsObservaciones Valor del campo dsObservaciones
     * @param cdUsuAlta Valor del campo CdUsuAlta
     * @param fcAltaFila Valor del campo FcAltaFila
     * @param cdUsuModif Valor del campo CdUsuModif
     * @param fcModifFila Valor del campo FcModifFila
     * @param cdUsuBaja Valor del campo CdUsuBaja
     * @param fcBajaFila Valor del campo FcBajaFila
     */
    public ConvocatoriaPlaDoc(Integer idConvocatoriaPlaDoc, Integer idConvocatoriaPla,
            String cdFirmante, String cdUsuAlta, Date fcAltaFila, String cdUsuModif,
            Date fcModifFila, String cdUsuBaja, Date fcBajaFila) {
        this.idConvocatoriaPlaDoc = idConvocatoriaPlaDoc;
        this.idConvocatoriaPla = idConvocatoriaPla;
//        this.dsDocumentacion = dsDocumentacion;
//        this.dsObservaciones = dsObservaciones;
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
        return new HashCodeBuilder().append(this.idConvocatoriaPlaDoc)
                .append(this.idConvocatoriaPla).append(this.dsDocumentacion)
                .append(this.dsObservaciones).append(this.cdUsuAlta)
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
        if (!(obj instanceof ConvocatoriaPlaDoc)) {
            return false;
        }
        ConvocatoriaPlaDoc castObj = (ConvocatoriaPlaDoc) obj;
        return new EqualsBuilder().append(this.idConvocatoriaPlaDoc, castObj.idConvocatoriaPlaDoc)
                .append(this.idConvocatoriaPla, castObj.idConvocatoriaPla)
                .append(this.dsDocumentacion, castObj.dsDocumentacion)
                .append(this.dsObservaciones, castObj.dsObservaciones)
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
     * @return Devuelve el valor del campo idConvocatoriaPlaDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONV_PLA_DOC")
    @SequenceGenerator(name = "SAII_S_CONV_PLA_DOC", sequenceName = "SAII_S_CONV_PLA_DOC", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_PLA_DOC")
    public Integer getIdConvocatoriaPlaDoc() {
        return this.idConvocatoriaPlaDoc;
    }

    /**
     * Establece el valor del campo idConvocatoriaPlaDoc de la entidad
     * ConvocatoriaPlaDoc
     *
     * @param idConvocatoriaPlaDoc El valor a establecer
     */
    public void setIdConvocatoriaPlaDoc(Integer idConvocatoriaPlaDoc) {
        this.idConvocatoriaPlaDoc = idConvocatoriaPlaDoc;
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
     * ConvocatoriaPlaDoc
     *
     * @param idConvocatoriaPla El valor a establecer
     */
    public void setIdConvocatoriaPla(Integer idConvocatoriaPla) {
        this.idConvocatoriaPla = idConvocatoriaPla;
    }

    /**
     * @return Devuelve el valor del campo dsDocumentacion
     */
    @Column(name = "DS_DOCUMENTACION")
    public String getDsDocumentacion() {
        return this.dsDocumentacion;
    }

    /**
     * Establece el valor del campo dsDocumentacion de la entidad ConvocatoriaPlaDoc
     *
     * @param dsDocumentacion El valor a establecer
     */
    public void setDsDocumentacion(String dsDocumentacion) {
        this.dsDocumentacion = dsDocumentacion;
    }
    
    /**
     * @return Devuelve el valor del campo dsObservaciones
     */
    @Column(name = "DS_OBSERVACIONES")
    public String getDsObservaciones() {
        return this.dsObservaciones;
    }

    /**
     * Establece el valor del campo dsObservaciones de la entidad ConvocatoriaPlaDoc
     *
     * @param dsObservaciones El valor a establecer
     */
    public void setDsObservaciones(String dsObservaciones) {
        this.dsObservaciones = dsObservaciones;
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
