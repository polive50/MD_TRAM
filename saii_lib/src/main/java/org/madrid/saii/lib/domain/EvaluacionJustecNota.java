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
 * Representa el objeto de dominio EvaluacionNota
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EVALUACION_JUSTEC_NOTA")
@AtlasAuditarFila
@ToString
public class EvaluacionJustecNota implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idEvaluacionJustecNota del objeto de dominio
     */
    private Integer idEvaluacionJustecNota;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idNota del objeto de dominio
     */
    private CatalogosValores nota;

    /**
     * Representa el campo idEvaluacionJustec del objeto de dominio
     */
    private Integer idEvaluacionJustec;

    /**
     * Representa el campo nmDe del objeto de dominio
     */
    private Integer nmDe;

    /**
     * Representa el campo nmA del objeto de dominio
     */
    private Integer nmA;

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
    public EvaluacionJustecNota() {
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
        return new HashCodeBuilder().append(this.idEvaluacionJustecNota).append(this.idConvocatoria)
                .append(this.nota).append(this.nmDe).append(this.nmA).append(this.cdUsuAlta)
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
        if (!(obj instanceof EvaluacionJustecNota)) {
            return false;
        }
        EvaluacionJustecNota castObj = (EvaluacionJustecNota) obj;
        return new EqualsBuilder()
                .append(this.idEvaluacionJustecNota, castObj.idEvaluacionJustecNota)
                .append(this.idEvaluacionJustec, castObj.idEvaluacionJustec)
                .append(this.idConvocatoria, castObj.idConvocatoria).append(this.nota, castObj.nota)
                .append(this.nmDe, castObj.nmDe).append(this.nmA, castObj.nmA)
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
     * @return Devuelve el valor del campo idEvaluacionJustecNota
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EVALUACION_JUSTEC_NOTA")
    @SequenceGenerator(name = "SAII_S_EVALUACION_JUSTEC_NOTA", sequenceName = "SAII_S_EVALUACION_JUSTEC_NOTA", allocationSize = 1)
    @Column(name = "ID_EVALUACION_JUSTEC_NOTA")
    public Integer getIdEvaluacionJustecNota() {
        return this.idEvaluacionJustecNota;
    }

    /**
     * Establece el valor del campo idEvaluacionJustecNota de la entidad
     * EvaluacionJustecNota
     *
     * @param idEvaluacionJustecNota El valor a establecer
     */
    public void setIdEvaluacionJustecNota(Integer idEvaluacionJustecNota) {
        this.idEvaluacionJustecNota = idEvaluacionJustecNota;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad EvaluacionNota
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo idNota
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NOTA")
    public CatalogosValores getNota() {
        return this.nota;
    }

    /**
     * Establece el valor del campo idNota de la entidad EvaluacionNota
     *
     * @param idNota El valor a establecer
     */
    public void setNota(CatalogosValores nota) {
        this.nota = nota;
    }

    /**
     * @return Devuelve el valor del campo idEvaluacionJustec
     */
    @Column(name = "ID_EVALUACION_JUSTEC")
    public Integer getIdEvaluacionJustec() {
        return this.idEvaluacionJustec;
    }

    /**
     * Establece el valor del campo idEvaluacionJustec de la entidad
     * EvaluacionJustecNota
     *
     * @param idEvaluacionJustec El valor a establecer
     */
    public void setIdEvaluacionJustec(Integer idEvaluacionJustec) {
        this.idEvaluacionJustec = idEvaluacionJustec;
    }

    /**
     * @return Devuelve el valor del campo nmDe
     */
    @Column(name = "NM_DE")
    public Integer getNmDe() {
        return this.nmDe;
    }

    /**
     * Establece el valor del campo nmDe de la entidad EvaluacionNota
     *
     * @param nmDe El valor a establecer
     */
    public void setNmDe(Integer nmDe) {
        this.nmDe = nmDe;
    }

    /**
     * @return Devuelve el valor del campo nmA
     */
    @Column(name = "NM_A")
    public Integer getNmA() {
        return this.nmA;
    }

    /**
     * Establece el valor del campo nmA de la entidad EvaluacionNota
     *
     * @param nmA El valor a establecer
     */
    public void setNmA(Integer nmA) {
        this.nmA = nmA;
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
