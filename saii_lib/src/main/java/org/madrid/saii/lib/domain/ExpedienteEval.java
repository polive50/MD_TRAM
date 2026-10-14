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
 * Representa el objeto de dominio ExpedienteEval
 *
 * @author nttdata
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_EVAL")
@AtlasAuditarFila
@ToString
public class ExpedienteEval implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteEval del objeto de dominio
     */
    private Integer idExpedienteEval;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

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
     * Representa el campo itCoorEval del objeto de dominio
     */
    private String itCoorEval;

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
     * Devuelve la lista de evaluacion nota dato
     */
    private List<EvaluacionNotaDato> lstEvaluacionNotaDato;

    /**
     * Devuelve la lista de apartado dato
     */
    private List<ApartadoDato> lstApartadoDato;
    
    /**
     * variable que representa si el registro fue rechazado por el coordinador o gestor
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
     * Representa el campo motivoRechazo del objeto de dominio
     */
    private CatalogosValores motivoRechazo;
    
    /**
     * variable que representa si el registro fue aceptado por el coordinador o gestor
     */
    private String itAceptaEval;
    
    /**
     * Representa el campo dsComentarioRechazoAceptacion del objeto de dominio
     */
    private String dsComentarioRechazoAceptacion;
    
    /**
     * Representa el campo dsObsRechazoInfEval del objeto de dominio
     */
    private String dsObsRechazoInfEval;

    /**
     * Representa el campo idExpedienteEvalPadre del objeto de dominio
     */
    private Integer idExpedienteEvalPadre;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteEval() {
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
        return new HashCodeBuilder().append(this.idExpedienteEval).append(this.idExpediente)
                .append(this.persona).append(this.estado).append(this.itCoorEval)
                .append(this.cdFica).append(this.dsComentario).append(this.nmNotaFinal)
                .append(this.itNotificado).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ExpedienteEval)) {
            return false;
        }
        ExpedienteEval castObj = (ExpedienteEval) obj;
        return new EqualsBuilder().append(this.idExpedienteEval, castObj.idExpedienteEval)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.persona, castObj.persona).append(this.estado, castObj.estado)
                .append(this.itCoorEval, castObj.itCoorEval).append(this.cdFica, castObj.cdFica)
                .append(this.dsComentario, castObj.dsComentario)
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
     * @return Devuelve el valor del campo idExpedienteEval
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_EVAL")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_EVAL", sequenceName = "SAII_S_EXPEDIENTE_EVAL", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_EVAL")
    public Integer getIdExpedienteEval() {
        return this.idExpedienteEval;
    }

    /**
     * Establece el valor del campo idExpedienteEval de la entidad
     * ExpedienteEval
     *
     * @param idExpedienteEval El valor a establecer
     */
    public void setIdExpedienteEval(Integer idExpedienteEval) {
        this.idExpedienteEval = idExpedienteEval;
    }

    /**
     * @return Devuelve el valor del campo idExpediente
     */
    @Column(name = "ID_EXPEDIENTE")
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad ExpedienteEval
     *
     * @param idExpediente El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
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
     * Establece el valor del campo idPersona de la entidad ExpedienteEval
     *
     * @param idPersona El valor a establecer
     */
    public void setPersona(Interesado persona) {
        this.persona = persona;
    }

    /**
     * @return Devuelve el valor del campo causa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAUSA")
    public CatalogosValores getCausa() {
        return this.causa;
    }

    /**
     * Establece el valor del campo causa de la entidad ExpedienteEval
     *
     * @param causa El valor a establecer
     */
    public void setCausa(CatalogosValores causa) {
        this.causa = causa;
    }

    /**
     * @return Devuelve el valor del campo idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    /**
     * Establece el valor del campo idEstado de la entidad ExpedienteEval
     *
     * @param idEstado El valor a establecer
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return Devuelve el valor del campo itCoorEval
     */
    @Column(name = "IT_COOR_EVAL")
    public String getItCoorEval() {
        return this.itCoorEval;
    }

    /**
     * Establece el valor del campo itCoorEval de la entidad ExpedienteEval
     *
     * @param itCoorEval El valor a establecer
     */
    public void setItCoorEval(String itCoorEval) {
        this.itCoorEval = itCoorEval;
    }

    /**
     * @return Devuelve el valor del campo cdFica
     */
    @Column(name = "CD_FICA", nullable = true)
    public String getCdFica() {
        return this.cdFica;
    }

    /**
     * Establece el valor del campo cdFica de la entidad ExpedienteEval
     *
     * @param cdFica El valor a establecer
     */
    public void setCdFica(String cdFica) {
        this.cdFica = cdFica;
    }

    /**
     * @return Devuelve el valor del campo dsComentario
     */
    @Column(name = "DS_COMENTARIO", nullable = true)
    public String getDsComentario() {
        return this.dsComentario;
    }

    /**
     * Establece el valor del campo dsComentario de la entidad ExpedienteEval
     *
     * @param dsComentario El valor a establecer
     */
    public void setDsComentario(String dsComentario) {
        this.dsComentario = dsComentario;
    }

    /**
     * @return Devuelve el valor del campo nmNotaFinal
     */
    @Column(name = "NM_NOTA_FINAL", nullable = true)
    public Integer getNmNotaFinal() {
        return this.nmNotaFinal;
    }

    /**
     * Establece el valor del campo nmNotaFinal de la entidad ExpedienteEval
     *
     * @param nmNotaFinal El valor a establecer
     */
    public void setNmNotaFinal(Integer nmNotaFinal) {
        this.nmNotaFinal = nmNotaFinal;
    }

    /**
     * @return Devuelve el valor del campo itNotificado
     */
    @Column(name = "IT_NOTIFICADO", nullable = true)
    public String getItNotificado() {
        return this.itNotificado;
    }

    /**
     * Establece el valor del campo itNotificado de la entidad ExpedienteEval
     *
     * @param itNotificado El valor a establecer
     */
    public void setItNotificado(String itNotificado) {
        this.itNotificado = itNotificado;
    }

    /**
     * @return the lstEvaluacionNotaDato
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedienteEval", cascade = CascadeType.ALL)
    public List<EvaluacionNotaDato> getLstEvaluacionNotaDato() {
        return this.lstEvaluacionNotaDato;
    }

    /**
     * @param lstEvaluacionNotaDato the lstEvaluacionNotaDato to set
     */
    public void setLstEvaluacionNotaDato(List<EvaluacionNotaDato> lstEvaluacionNotaDato) {
        this.lstEvaluacionNotaDato = lstEvaluacionNotaDato;
    }

    /**
     * @return the lstApartadoDato
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedienteEval", cascade = CascadeType.ALL)
    public List<ApartadoDato> getLstApartadoDato() {
        return this.lstApartadoDato;
    }

    /**
     * @param lstApartadoDato the lstApartadoDato to set
     */
    public void setLstApartadoDato(List<ApartadoDato> lstApartadoDato) {
        this.lstApartadoDato = lstApartadoDato;
    }
    
    /**
     * @return the itRechazado
     */
    @Column(name = "IT_RECHAZADO")
    public String getItRechazado() {
        return itRechazado;
    }

    /**
     * @param itRechazado the itRechazado to set
     */
    public void setItRechazado(String itRechazado) {
        this.itRechazado = itRechazado;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NM_MOTIVO_RECHAZO_ACPT", nullable = true)
    public CatalogosValores getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(CatalogosValores motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    @Column(name = "IT_ACEPTA_EVAL", nullable = true)
    public String getItAceptaEval() {
        return itAceptaEval;
    }

    public void setItAceptaEval(String itAceptaEval) {
        this.itAceptaEval = itAceptaEval;
    }

    @Column(name = "DS_MOTIVO_RECHAZO_ACPT", nullable = true)
    public String getDsComentarioRechazoAceptacion() {
        return dsComentarioRechazoAceptacion;
    }

    public void setDsComentarioRechazoAceptacion(String dsComentarioRechazoAceptacion) {
        this.dsComentarioRechazoAceptacion = dsComentarioRechazoAceptacion;
    }

    /**
     * @return the dsObsRechazoInfEval
     */
    @Column(name = "DS_OBS_RECHAZO_INF_EVAL", nullable = true)
    public String getDsObsRechazoInfEval() {
        return dsObsRechazoInfEval;
    }

    /**
     * @param dsObsRechazoInfEval the dsObsRechazoInfEval to set
     */
    public void setDsObsRechazoInfEval(String dsObsRechazoInfEval) {
        this.dsObsRechazoInfEval = dsObsRechazoInfEval;
    }

    @Column(name = "ID_EXPEDIENTE_EVAL_PADRE", nullable = true)
    public Integer getIdExpedienteEvalPadre() {
        return idExpedienteEvalPadre;
    }

    public void setIdExpedienteEvalPadre(Integer idExpedienteEvalPadre) {
        this.idExpedienteEvalPadre = idExpedienteEvalPadre;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
}
