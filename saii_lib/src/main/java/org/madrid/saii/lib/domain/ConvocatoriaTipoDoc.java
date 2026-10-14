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
 * Representa el objeto de dominio ConvocatoriaTip
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_TIPODOC")
@AtlasAuditarFila
@ToString
public class ConvocatoriaTipoDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaTip del objeto de dominio
     */
    private Integer idConvocatoriaTipoDoc;

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

    /**
     * Representa el campo convocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoDocumento del objeto de dominio
     */
    private CatalogosValores tipoDocumento;

    /**
     * Representa el campo cdFicaPlantilla del objeto de dominio
     */
    private Integer cdFicaPlantilla;

    /**
     * Representa el campo tramite del objeto de dominio
     */
    private CatalogosValores tramite;

    /**
     * Representa el campo itObligatorio del objeto de dominio
     */
    private String itObligatorio;
    
    /**
     * Representa el campo itPermiDescaEval del objeto de dominio
     */
    private String itPermiDescaEval;

    /**
     * Representa el campo tipoDocumento del objeto de dominio
     */
    private CatalogosValores tipoICDA;
    
    /**
     * Representa el campo tipoDocumento del objeto de dominio
     */
    private CatalogosValores codProcedimiento;
    
    /**
     * Representa el campo tipoDocumento del objeto de dominio
     */
    private String itConsentimiento;
    
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaTipoDoc() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaTipoDoc).append(this.cdUsuAlta)
                .append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).append(this.idConvocatoria)
                .append(this.tipoDocumento).append(this.tramite).append(this.itObligatorio)
                .append(this.itPermiDescaEval)
                .toHashCode();
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
        if (!(obj instanceof ConvocatoriaTipoDoc)) {
            return false;
        }
        ConvocatoriaTipoDoc castObj = (ConvocatoriaTipoDoc) obj;
        return new EqualsBuilder().append(this.idConvocatoriaTipoDoc, castObj.idConvocatoriaTipoDoc)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.tipoDocumento, castObj.tipoDocumento)
                .append(this.tramite, castObj.tramite)
                .append(this.itObligatorio, castObj.itObligatorio)
                .append(this.itPermiDescaEval, castObj.itPermiDescaEval).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoriaTipoDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_TIPODOC")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_TIPODOC", sequenceName = "SAII_S_CONVOCATORIA_TIPODOC", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_TIPODOC")
    public Integer getIdConvocatoriaTipoDoc() {
        return this.idConvocatoriaTipoDoc;
    }

    /**
     * Establece el valor del campo idConvocatoriaTip de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param idConvocatoriaTipoDoc El valor a establecer
     */
    public void setIdConvocatoriaTipoDoc(Integer idConvocatoriaTipoDoc) {
        this.idConvocatoriaTipoDoc = idConvocatoriaTipoDoc;
    }

    /**
     * @return Devuelve el valor del campo convocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo convocatoria de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo tipoDocumento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_DOCUMENTO")
    public CatalogosValores getTipoDocumento() {
        return this.tipoDocumento;
    }

    /**
     * Establece el valor del campo tipoDocumento de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param tipoDocumento El valor a establecer
     */
    public void setTipoDocumento(CatalogosValores tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return Devuelve el valor del campo cdFicaPlantilla
     */
    @Column(name = "CD_FICA_PLANTILLA")
    public Integer getCdFicaPlantilla() {
        return this.cdFicaPlantilla;
    }

    /**
     * Establece el valor del campo cdFicaPlantilla de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param cdFicaPlantilla El valor a establecer
     */
    public void setCdFicaPlantilla(Integer cdFicaPlantilla) {
        this.cdFicaPlantilla = cdFicaPlantilla;
    }
    
    /**
     * @return Devuelve el valor del campo itPermiDescaEval
     */
    @Column(name = "IT_PERMI_DESCA_EVAL", nullable = true)
    public String getItPermiDescaEval() {
        return this.itPermiDescaEval;
    }

    /**
     * Establece el valor del campo itPermiDescaEval de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param itPermiDescaEval El valor a establecer
     */
    public void setItPermiDescaEval(String itPermiDescaEval) {
        this.itPermiDescaEval = itPermiDescaEval;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ICDA", nullable = true)
    public CatalogosValores getTipoICDA() {
        return this.tipoICDA;
    }

    /**
     * Establece el valor del campo tipoICDA de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param tipoDocumento El valor a establecer
     */
    public void setTipoICDA(CatalogosValores tipoICDA) {
        this.tipoICDA = tipoICDA;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCEDIMIENTO", nullable = true)
    public CatalogosValores getCodProcedimiento() {
        return this.codProcedimiento;
    }

    /**
     * Establece el valor del campo codProcedimiento de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param tipoDocumento El valor a establecer
     */
    public void setCodProcedimiento(CatalogosValores codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

   
    
    /**
     * @return Devuelve el valor del campo itConsentimiento
     */
    @Column(name = "IT_CONSULTA_LEY", nullable = true)
    public String getItConsentimiento() {
        return this.itConsentimiento;
    }

    /**
     * Establece el valor del campo itObligatorio de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param itObligatorio El valor a establecer
     */
    public void setItConsentimiento(String itConsentimiento) {
        this.itConsentimiento = itConsentimiento;
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

    /**
     * @return Devuelve el valor del campo tramite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE", nullable = true)
    public CatalogosValores getTramite() {
        return this.tramite;
    }

    /**
     * Establece el valor del campo tramite de la entidad ConvocatoriaTipoDoc
     *
     * @param tramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
    }

    /**
     * @return Devuelve el valor del campo itObligatorio
     */
    @Column(name = "IT_OBLIGATORIO", nullable = true)
    public String getItObligatorio() {
        return this.itObligatorio;
    }

    /**
     * Establece el valor del campo itObligatorio de la entidad
     * ConvocatoriaTipoDoc
     *
     * @param itObligatorio El valor a establecer
     */
    public void setItObligatorio(String itObligatorio) {
        this.itObligatorio = itObligatorio;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}
