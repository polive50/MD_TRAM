package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ExpedienteEvalJusTec
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPE_EVAL_JUSTEC_HIST")
@AtlasAuditarFila
@ToString
public class ExpedienteEvalJusTecHist implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Representa el campo idExpedienteEvalHist del objeto de dominio
     */
    private Integer idExpedienteEvalJusTecHist;

    /**
     * Representa el campo idExpedienteEval del objeto de dominio
     */
    private ExpedienteEvalJusTec expedienteEvalJusTec;

    /**
     * Representa el campo idExpedienteJustec del objeto de dominio
     */
    private ExpedienteJusTec expedienteJusTec;

    /**
     * Representa el campo idPersona del objeto de dominio
     */
    private Interesado persona;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo causa del objeto de dominio
     */
    private CatalogosValores causa;
    
    /**
     * Representa el campo cdFica del objeto de dominio
     */
    private String cdFica;

    /**
     * Representa el campo dsComentario del objeto de dominio
     */
    private String dsComentario;

    /**
     * Representa el campo nmNotaFinal del objeto de dominio
     */
    private Integer nmNotaFinal;

    /**
     * Representa el campo itNotificado del objeto dominio
     */
    private String itNotificado;
    
    /**
     * Devuelve la lista de evaluacion nota dato justec
     */
    private List<EvaluacionJustecNotaDato> lstEvaluacionJustecNotaDato;

    /**
     * Representa el campo fcIniPlazo del objeto de dominio
     */
    private Date fcIniPlazo;

    /**
     * Representa el campo fcFinPlazo del objeto de dominio
     */
    private Date fcFinPlazo;
    /**
     * Devuelve la lista de apartado dato
     */
    private List<ApartadoJustecDato> lstApartadoJustecDato;
    
    /**
     * variable que representa si el registro fue rechazado por el  gestor
     */
    private String itRechazado;


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
     * Representa el campo obsEval del objeto de dominio
     */
    private String obsEval;
    
    /**
     * Representa el campo obsGest del objeto de dominio
     */
    private String obsGest;


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteEvalJusTecHist() {
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
        return new HashCodeBuilder().append(this.idExpedienteEvalJusTecHist).append(this.idExpedienteEvalJusTecHist)
                .append(this.expedienteEvalJusTec).append(this.expedienteEvalJusTec)
                .append(this.persona).append(this.estado)
                .append(this.cdFica).append(this.dsComentario).append(this.nmNotaFinal).append(this.fcIniPlazo)
                .append(this.fcFinPlazo).append(this.itNotificado).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja)
                .append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof ExpedienteEvalJusTecHist)) {
            return false;
        }
        ExpedienteEvalJusTecHist castObj = (ExpedienteEvalJusTecHist) obj;
        return new EqualsBuilder()
                .append(this.idExpedienteEvalJusTecHist, castObj.idExpedienteEvalJusTecHist)
                .append(this.expedienteEvalJusTec, castObj.expedienteEvalJusTec)
                .append(this.expedienteJusTec, castObj.expedienteJusTec)
                .append(this.persona, castObj.persona).append(this.estado, castObj.estado)
                .append(this.cdFica, castObj.cdFica)
                .append(this.fcIniPlazo, castObj.fcIniPlazo)
                .append(this.fcFinPlazo, castObj.fcFinPlazo)
                .append(this.dsComentario, castObj.dsComentario)
                .append(this.obsEval, castObj.obsEval)
                .append(this.obsGest, castObj.obsGest)
                .append(this.nmNotaFinal, castObj.nmNotaFinal)
                .append(this.itNotificado, castObj.itNotificado)
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
     * @return Devuelve el valor del campo idExpedienteEvalJustecHist
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXP_EVAL_JUSTEC_HIST")
    @SequenceGenerator(name = "SAII_S_EXP_EVAL_JUSTEC_HIST", sequenceName = "SAII_S_EXP_EVAL_JUSTEC_HIST", allocationSize = 1)
    @Column(name = "ID_EXPE_EVAL_JUSTEC_HIST")
    public Integer getIdExpedienteEvalJusTecHist() {
        return this.idExpedienteEvalJusTecHist;
    }   
        
    /**
     * Establece el valor del campo idExpedienteEvalJusTecHist de la entidad
     * ExpedienteEvalJusTecHist
     *
     * @param idExpedienteEvalJusTecHist El valor a establecer
     */
    public void setIdExpedienteEvalJusTecHist(Integer idExpedienteEvalJusTecHist) {
        this.idExpedienteEvalJusTecHist = idExpedienteEvalJusTecHist;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteEvalJustec
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_EVAL_JUSTEC", nullable = false)
    public ExpedienteEvalJusTec getExpedienteEvalJusTec() {
        return this.expedienteEvalJusTec;
    }
    
    /**
     * Establece el valor del campo idPersona de la entidad ExpedienteEvalJusTec
     *
     * @param idPersona El valor a establecer
     */
    public void setExpedienteEvalJusTec(ExpedienteEvalJusTec expedienteEvalJusTec) {
        this.expedienteEvalJusTec = expedienteEvalJusTec;
    }

    /**
     * @return Devuelve el valor del campo expedienteJustec
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_JUSTEC", nullable = false)
    public ExpedienteJusTec getExpedienteJusTec() {
        return this.expedienteJusTec;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad ExpedienteEvalJusTec
     *
     * @param idExpediente El valor a establecer
     */
    public void setExpedienteJusTec(ExpedienteJusTec expedienteJusTec) {
        this.expedienteJusTec = expedienteJusTec;
    }

    /**
     * @return Devuelve el valor del campo idPersona
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERSONA", nullable = false)
    public Interesado getPersona() {
        return this.persona;
    }

    /**
     * Establece el valor del campo idPersona de la entidad ExpedienteEvalJusTec
     *
     * @param idPersona El valor a establecer
     */
    public void setPersona(Interesado persona) {
        this.persona = persona;
    }

    /**
     * Establece el valor del campo fcIniPlazo de la entidad ExpedienteEvalJusTec
     *
     * @param fcIniPlazo El valor a establecer
     */
    @Column(name = "FC_INI_PLAZO")
    public Date getFcIniPlazo() {
        return fcIniPlazo;
    }

    /**
     * Establece el valor del campo fcIniPlazo de la entidad ExpedienteEvalJusTec
     *
     * @param idPersona El valor a establecer
     */
    public void setFcIniPlazo(Date fcIniPlazo) {
        this.fcIniPlazo = fcIniPlazo;
    }
    
    /**
     * Establece el valor del campo fcFinPlazo de la entidad ExpedienteEvalJusTec
     *
     * @param fcFinPlazo El valor a establecer
     */
    @Column(name = "FC_FIN_PLAZO")
    public Date getFcFinPlazo() {
        return fcFinPlazo;
    }

    /**
     * Establece el valor del campo fcFinPlazo de la entidad ExpedienteEvalJusTec
     *
     */
    public void setFcFinPlazo(Date fcFinPlazo) {
        this.fcFinPlazo = fcFinPlazo;
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
     * @return Devuelve el valor del campo idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return estado;
    }

    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return Devuelve el valor del campo causa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAUSA")
    public CatalogosValores getCausa() {
        return causa;
    }

    public void setCausa(CatalogosValores causa) {
        this.causa = causa;
    }

    /**
     * @return Devuelve el valor del campo cdFica
     */
    @Column(name = "CD_FICA", nullable = true)
    public String getCdFica() {
        return cdFica;
    }

    public void setCdFica(String cdFica) {
        this.cdFica = cdFica;
    }

    /**
     * @return Devuelve el valor del campo dsComentario
     */
    @Column(name = "DS_COMENTARIO", nullable = true)
    public String getDsComentario() {
        return dsComentario;
    }

    public void setDsComentario(String dsComentario) {
        this.dsComentario = dsComentario;
    }

    /**
     * @return Devuelve el valor del campo nmNotaFinal
     */
    @Column(name = "NM_NOTA_FINAL", nullable = true)
    public Integer getNmNotaFinal() {
        return nmNotaFinal;
    }

    public void setNmNotaFinal(Integer nmNotaFinal) {
        this.nmNotaFinal = nmNotaFinal;
    }

    /**
     * @return Devuelve el valor del campo itNotificado
     */
    @Column(name = "IT_NOTIFICADO", nullable = true)
    public String getItNotificado() {
        return itNotificado;
    }

    public void setItNotificado(String itNotificado) {
        this.itNotificado = itNotificado;
    }

    /**
     * @return the itRechazado
     */
    @Column(name = "IT_RECHAZADO")
    public String getItRechazado() {
        return itRechazado;
    }

    public void setItRechazado(String itRechazado) {
        this.itRechazado = itRechazado;
    }

    /**
     * @return the lstEvaluacionJustecNotaDato
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedienteEvalJustec", cascade = CascadeType.ALL)
    public List<EvaluacionJustecNotaDato> getLstEvaluacionJustecNotaDato() {
        return this.lstEvaluacionJustecNotaDato;
    }

    /**
     * @param lstEvaluacionJustecNotaDato the lstEvaluacionJustecNotaDato to set
     */
    public void setLstEvaluacionJustecNotaDato(List<EvaluacionJustecNotaDato> lstEvaluacionJustecNotaDato) {
        this.lstEvaluacionJustecNotaDato = lstEvaluacionJustecNotaDato;
    }

    /**
     * @return the lstApartadoDato
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedienteEvalJustec", cascade = CascadeType.ALL)
    public List<ApartadoJustecDato> getLstApartadoJustecDato() {
        return this.lstApartadoJustecDato;
    }

    /**
     * @param lstApartadoDato the lstApartadoDato to set
     */
    public void setLstApartadoJustecDato(List<ApartadoJustecDato> lstApartadoJustecDato) {
        this.lstApartadoJustecDato = lstApartadoJustecDato;
    }
    
    /**
     * @return Devuelve el valor del campo obsEval
     */
    @Column(name = "OBS_EVAL", nullable = true)
    public String getObsEval() {
        return obsEval;
    }

    public void setObsEval(String obsEval) {
        this.obsEval = obsEval;
    }
    
    /**
     * @return Devuelve el valor del campo obsGest
     */
    @Column(name = "OBS_GEST", nullable = true)
    public String getObsGest() {
        return obsGest;
    }

    public void setObsGest(String obsGest) {
        this.obsGest = obsGest;
    }


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}

