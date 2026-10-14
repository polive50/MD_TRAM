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
 * Representa el objeto de dominio ConvocatoriaCoorEval
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_COOR_EVAL")
@AtlasAuditarFila
@ToString
public class ConvocatoriaCoorEval implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaCoorEval del objeto de dominio
     */
    private Integer idConvocatoriaCoorEval;

    /**
     * Representa el campo idConvocatoriaCoor del objeto de dominio
     */
    private Integer idConvocatoriaCoor;

    /**
     * Representa el campo idEvaluador del objeto de dominio
     */
    private Interesado evaluador;

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
     * Representa el campo idArea del objeto de dominio
     */
    private CatalogosValores area;
    
    /**
     * Representa el campo 
     */
    private CatalogosValores tipoInforme;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCoorEval() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaCoorEval)
                .append(this.idConvocatoriaCoor).append(this.evaluador).append(this.cdUsuAlta)
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
        if (!(obj instanceof ConvocatoriaCoorEval)) {
            return false;
        }
        ConvocatoriaCoorEval castObj = (ConvocatoriaCoorEval) obj;
        return new EqualsBuilder()
                .append(this.idConvocatoriaCoorEval, castObj.idConvocatoriaCoorEval)
                .append(this.idConvocatoriaCoor, castObj.idConvocatoriaCoor)
                .append(this.evaluador, castObj.evaluador).append(this.cdUsuAlta, castObj.cdUsuAlta)
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
     * @return Devuelve el valor del campo idConvocatoriaCoorEval
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_COOR_EVAL")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_COOR_EVAL", sequenceName = "SAII_S_CONVOCATORIA_COOR_EVAL", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_COOR_EVAL")
    public Integer getIdConvocatoriaCoorEval() {
        return this.idConvocatoriaCoorEval;
    }

    /**
     * Establece el valor del campo idConvocatoriaCoorEval de la entidad
     * ConvocatoriaCoorEval
     *
     * @param idConvocatoriaCoorEval El valor a establecer
     */
    public void setIdConvocatoriaCoorEval(Integer idConvocatoriaCoorEval) {
        this.idConvocatoriaCoorEval = idConvocatoriaCoorEval;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoriaCoor
     */
    @Column(name = "ID_CONVOCATORIA_COOR")
    public Integer getIdConvocatoriaCoor() {
        return this.idConvocatoriaCoor;
    }

    /**
     * Establece el valor del campo idConvocatoriaCoor de la entidad
     * ConvocatoriaCoorEval
     *
     * @param idConvocatoriaCoor El valor a establecer
     */
    public void setIdConvocatoriaCoor(Integer idConvocatoriaCoor) {
        this.idConvocatoriaCoor = idConvocatoriaCoor;
    }

    /**
     * @return Devuelve el valor del campo idEvaluador
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EVALUADOR")
    public Interesado getEvaluador() {
        return this.evaluador;
    }

    /**
     * Establece el valor del campo idEvaluador de la entidad
     * ConvocatoriaCoorEval
     *
     * @param idEvaluador El valor a establecer
     */
    public void setEvaluador(Interesado evaluador) {
        this.evaluador = evaluador;
    }
    
    /**
     * @return Devuelve el valor del campo idArea
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AREA")
    public CatalogosValores getArea() {
        return this.area;
    }

    /**
     * Establece el valor del campo idArea de la entidad ConvocatoriaCoor
     *
     * @param idArea El valor a establecer
     */
    public void setArea(CatalogosValores area) {
        this.area = area;
    }
    
    /**
     * @return the tipoInforme
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_INFORME")
    public CatalogosValores getTipoInforme() {
        return tipoInforme;
    }

    /**
     * @param tipoInforme the tipoInforme to set
     */
    public void setTipoInforme(CatalogosValores tipoInforme) {
        this.tipoInforme = tipoInforme;
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
