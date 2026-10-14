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
import atlas.core.database.auditoria.AtlasAuditarFila;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import lombok.ToString;

@Entity
@Table(name = "SAII_EVAL_JUSTEC_NOTA_DATO")
@AtlasAuditarFila
@ToString
public class EvaluacionJustecNotaDato implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idEvaluacionJustecNotaDato del objeto de dominio
     */
    private Integer idEvaluacionJustecNotaDato;

    /**
     * Representa el campo idExpedienteEvalJustec del objeto de dominio
     */
    private Integer idExpedienteEvalJustec;

    /**
     * Representa el campo idEvaluacionJustec del objeto de dominio
     */
    private Integer idEvaluacionJustec;

    /**
     * Representa el campo nmNota del objeto de dominio
     */
    private Integer nmNota;

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
    public EvaluacionJustecNotaDato() {
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
        return new HashCodeBuilder().append(this.idEvaluacionJustecNotaDato).append(this.idExpedienteEvalJustec)
                .append(this.idEvaluacionJustec).append(this.nmNota).append(this.cdUsuAlta)
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
        if (!(obj instanceof EvaluacionNotaDato)) {
            return false;
        }
        EvaluacionJustecNotaDato castObj = (EvaluacionJustecNotaDato) obj;
        return new EqualsBuilder().append(this.idEvaluacionJustecNotaDato, castObj.idEvaluacionJustecNotaDato)
                .append(this.idExpedienteEvalJustec, castObj.idExpedienteEvalJustec)
                .append(this.idEvaluacionJustec, castObj.idEvaluacionJustec).append(this.nmNota, castObj.nmNota)
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
     * @return Devuelve el valor del campo idEvaluacionJustecNotaDato
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EVAL_JUSTEC_NOTA_DATO")
    @SequenceGenerator(name = "SAII_S_EVAL_JUSTEC_NOTA_DATO", sequenceName = "SAII_S_EVAL_JUSTEC_NOTA_DATO", allocationSize = 1)
    @Column(name = "ID_EVAL_JUSTEC_NOTA_DATO ")
    public Integer getIdEvaluacionJustecNotaDato() {
        return this.idEvaluacionJustecNotaDato;
    }

    /**
     * Establece el valor del campo idEvaluacionJustecNotaDato de la entidad
     * EvaluacionJustecNotaDato
     *
     * @param idEvaluacionJustecNotaDato El valor a establecer
     */
    public void setIdEvaluacionJustecNotaDato(Integer idEvaluacionJustecNotaDato) {
        this.idEvaluacionJustecNotaDato = idEvaluacionJustecNotaDato;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteEvalJustec
     */
    @Column(name = "ID_EXPEDIENTE_EVAL_JUSTEC")
    public Integer getIdExpedienteEvalJustec() {
        return this.idExpedienteEvalJustec;
    }

    /**
     * Establece el valor del campo idExpedienteEvalJustec de la entidad
     * EvaluacionNotaDato
     *
     * @param idExpedienteEvalJustec El valor a establecer
     */
    public void setIdExpedienteEvalJustec(Integer idExpedienteEvalJustec) {
        this.idExpedienteEvalJustec = idExpedienteEvalJustec;
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
     * EvaluacionNotaDato
     *
     * @param idEvaluacionJustec El valor a establecer
     */
    public void setIdEvaluacionJustec(Integer idEvaluacionJustec) {
        this.idEvaluacionJustec = idEvaluacionJustec;
    }

    /**
     * @return Devuelve el valor del campo nmNota
     */
    @Column(name = "NM_NOTA")
    public Integer getNmNota() {
        return this.nmNota;
    }

    /**
     * Establece el valor del campo nmNota de la entidad EvaluacionJustecNotaDato
     *
     * @param nmNota El valor a establecer
     */
    public void setNmNota(Integer nmNota) {
        this.nmNota = nmNota;
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
