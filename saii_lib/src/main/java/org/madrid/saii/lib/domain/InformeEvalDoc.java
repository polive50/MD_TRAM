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
 * Representa el objeto de dominio InformeEvalDoc
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EVAL_INFRM_DOC")
@AtlasAuditarFila
@ToString
public class InformeEvalDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEvalInfrmDoc;

    private ExpedienteEval idExpedienteEval;

    private Informe idInforme;

    private CatalogosValores idEstado;

    private String cdFica;

    private String dsComentario;

    private Integer nmNotaFinal;

    private CatalogosValores idCausa;

    private String itRechazado;

    private String itFirmado;

    private String dsObsRechazoInfEval;

    private String cdUsuAlta;

    private Date fcAltaFila;

    private String cdUsuModif;

    private Date fcModifFila;

    private String cdUsuBaja;

    private Date fcBajaFila;

    public InformeEvalDoc() {
        super();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.idEvalInfrmDoc)
                .append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof InformeEvalDoc)) {
            return false;
        }
        InformeEvalDoc castObj = (InformeEvalDoc) obj;
        return new EqualsBuilder()
                .append(this.idEvalInfrmDoc, castObj.idEvalInfrmDoc)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .isEquals();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EVAL_INFRM_DOC")
    @SequenceGenerator(name = "SAII_S_EVAL_INFRM_DOC", sequenceName = "SAII_S_EVAL_INFRM_DOC", allocationSize = 1)
    @Column(name = "ID_EVAL_INFR_DOC")
    public Integer getIdEvalInfrmDoc() {
        return idEvalInfrmDoc;
    }

    public void setIdEvalInfrmDoc(Integer idEvalInfrmDoc) {
        this.idEvalInfrmDoc = idEvalInfrmDoc;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_EVAL", nullable = false)
    public ExpedienteEval getIdExpedienteEval() {
        return idExpedienteEval;
    }

    public void setIdExpedienteEval(ExpedienteEval idExpedienteEval) {
        this.idExpedienteEval = idExpedienteEval;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFORME", nullable = true)
    public Informe getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(Informe idInforme) {
        this.idInforme = idInforme;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(CatalogosValores idEstado) {
        this.idEstado = idEstado;
    }

    @Column(name = "CD_FICA", length = 12)
    public String getCdFica() {
        return cdFica;
    }

    public void setCdFica(String cdFica) {
        this.cdFica = cdFica;
    }

    @Column(name = "DS_COMENTARIO", length = 2000)
    public String getDsComentario() {
        return dsComentario;
    }

    public void setDsComentario(String dsComentario) {
        this.dsComentario = dsComentario;
    }

    @Column(name = "NM_NOTA_FINAL")
    public Integer getNmNotaFinal() {
        return nmNotaFinal;
    }

    public void setNmNotaFinal(Integer nmNotaFinal) {
        this.nmNotaFinal = nmNotaFinal;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAUSA", nullable = true)
    public CatalogosValores getIdCausa() {
        return idCausa;
    }

    public void setIdCausa(CatalogosValores idCausa) {
        this.idCausa = idCausa;
    }

    @Column(name = "IT_RECHAZADO", length = 1)
    public String getItRechazado() {
        return itRechazado;
    }

    public void setItRechazado(String itRechazado) {
        this.itRechazado = itRechazado;
    }

    @Column(name = "IT_FIRMADO", length = 1)
    public String getItFirmado() {
        return itFirmado;
    }

    public void setItFirmado(String itFirmado) {
        this.itFirmado = itFirmado;
    }

    @Column(name = "DS_OBS_RECHAZO_INF_EVAL", length = 200)
    public String getDsObsRechazoInfEval() {
        return dsObsRechazoInfEval;
    }

    public void setDsObsRechazoInfEval(String dsObsRechazoInfEval) {
        this.dsObsRechazoInfEval = dsObsRechazoInfEval;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    @Column(name = "CD_USU_ALTA", nullable = false)
    public String getCdUsuAlta() {
        return cdUsuAlta;
    }

    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    @Column(name = "FC_ALTA_FILA", nullable = false)
    public Date getFcAltaFila() {
        return fcAltaFila;
    }

    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return cdUsuModif;
    }

    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return fcModifFila;
    }

    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return cdUsuBaja;
    }

    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return fcBajaFila;
    }

    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
