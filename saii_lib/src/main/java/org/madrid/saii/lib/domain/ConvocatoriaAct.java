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
 * Representa el objeto de dominio ConvocatoriaAct
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_ACT")
@AtlasAuditarFila
@ToString
public class ConvocatoriaAct implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idActor del objeto de dominio
     */
    private Integer idActor;

    /**
     * Representa el campo tlDescripcion del objeto de dominio
     */
    private String tlDescripcion;

    /**
     * Representa el campo itGeneraExpediente del objeto de dominio
     */
    private String itGeneraExpediente;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo tipoActor del objeto de dominio
     */
    private CatalogosValores tipoActor;

    /**
     * Representa el campo tipoActorFisJur del objeto de dominio
     */
    private CatalogosValores tipoActorFisJur;

    /**
     * Representa el campo identificacion del objeto de dominio
     */
    private CatalogosValores identificacion;

    /**
     * Representa el numero maximo de Participante que puede haber de este tipo
     * de actor en la convocatoria
     */
    private Integer nmMax;

    /**
     * Representa el numero minimo de Participante que puede haber de este tipo
     * de actor en la convocatoria
     */
    private Integer nmMin;

    /**
     * Representa el campo idActorSolicitante del objeto de dominio
     */
    private Integer idActorSolicitante;

    /**
     * Representa el identificador de actor coordinador
     */
    private Integer idActorCoordinador;

    /**
     * Representa el indicador de contrato
     */
    private String itContrato;
    
    /**
     * Representa el indicador con formulario
     */
    private String itCumplimentaFormulario;
    
    /**
     * Representa el campo itContratoIndefinido del objeto de dominio
     */
    private String itContratoIndefinido;

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
    public ConvocatoriaAct() {
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
        return new HashCodeBuilder().append(this.idActor).append(this.tlDescripcion)
                .append(this.itGeneraExpediente).append(this.idConvocatoria)
                .append(this.idActorSolicitante).append(this.idActorCoordinador)
                .append(this.itContrato).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ConvocatoriaAct)) {
            return false;
        }
        ConvocatoriaAct castObj = (ConvocatoriaAct) obj;
        return new EqualsBuilder().append(this.idActor, castObj.idActor)
                .append(this.tlDescripcion, castObj.tlDescripcion)
                .append(this.itGeneraExpediente, castObj.itGeneraExpediente)
                .append(this.idConvocatoria, castObj.getIdConvocatoria())
                .append(this.idActorSolicitante, castObj.idActorSolicitante)
                .append(this.idActorCoordinador, castObj.idActorCoordinador)
                .append(this.itContrato, castObj.itContrato)
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
     * @return Devuelve el valor del campo idActor
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_ACT")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_ACT", sequenceName = "SAII_S_CONVOCATORIA_ACT", allocationSize = 1)
    @Column(name = "ID_ACTOR")
    public Integer getIdActor() {
        return this.idActor;
    }

    /**
     * Establece el valor del campo idActor de la entidad ConvocatoriaAct
     *
     * @param idActor El valor a establecer
     */
    public void setIdActor(Integer idActor) {
        this.idActor = idActor;
    }

    /**
     * @return Devuelve el valor del campo tlDescripicion
     */

    @Column(name = "TL_DESCRIPCION", nullable = true)
    public String getTlDescripcion() {
        return this.tlDescripcion;
    }

    /**
     * Establece el valor del campo tlDescripicion de la entidad ConvocatoriaAct
     *
     * @param tlDescripicion El valor a establecer
     */
    public void setTlDescripcion(String tlDescripicion) {
        this.tlDescripcion = tlDescripicion;
    }

    /**
     * @return Devuelve el valor del campo itGeneraExpediente
     */

    @Column(name = "IT_GENERA_EXPEDIENTE", nullable = true)
    public String getItGeneraExpediente() {
        return this.itGeneraExpediente;
    }

    /**
     * Establece el valor del campo itGeneraExpediente de la entidad
     * ConvocatoriaAct
     *
     * @param itGeneraExpediente El valor a establecer
     */
    public void setItGeneraExpediente(String itGeneraExpediente) {
        this.itGeneraExpediente = itGeneraExpediente;
    }

    /**
     * @return the idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA", nullable = false)
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * @param idConvocatoria the idConvocatoria to set
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return the tipoActor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_ACTOR", nullable = true)
    public CatalogosValores getTipoActor() {
        return this.tipoActor;
    }

    /**
     * @param tipoActor the tipoActor to set
     */
    public void setTipoActor(CatalogosValores tipoActor) {
        this.tipoActor = tipoActor;
    }

    /**
     * @return the tipoActorFisJur
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_ACTOR_FISJUR", nullable = true)
    public CatalogosValores getTipoActorFisJur() {
        return this.tipoActorFisJur;
    }

    /**
     * @param tipoActorFisJur the tipoActorFisJur to set
     */
    public void setTipoActorFisJur(CatalogosValores tipoActorFisJur) {
        this.tipoActorFisJur = tipoActorFisJur;
    }

    /**
     * @return the identificacion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IDENTIFICACION", nullable = true)
    public CatalogosValores getIdentificacion() {
        return this.identificacion;
    }

    /**
     * @param identificacion the identificacion to set
     */
    public void setIdentificacion(CatalogosValores identificacion) {
        this.identificacion = identificacion;
    }

    /**
     * @return the nmMax
     */
    @Column(name = "NM_MAX", nullable = true)
    public Integer getNmMax() {
        return this.nmMax;
    }

    /**
     * @param nmMax the nmMax to set
     */
    public void setNmMax(Integer nmMax) {
        this.nmMax = nmMax;
    }

    /**
     * @return the nmMax
     */
    @Column(name = "NM_MIN", nullable = true)
    public Integer getNmMin() {
        return this.nmMin;
    }

    /**
     * @param nmMax the nmMax to set
     */
    public void setNmMin(Integer nmMin) {
        this.nmMin = nmMin;
    }

    /**
     * @return the idActorSolicitante
     */
    @Column(name = "ID_ACTOR_SOLICITANTE", nullable = true)
    public Integer getIdActorSolicitante() {
        return this.idActorSolicitante;
    }

    /**
     * @param idActorSolicitante the idActorSolicitante to set
     */
    public void setIdActorSolicitante(Integer idActorSolicitante) {
        this.idActorSolicitante = idActorSolicitante;
    }

    /**
     * @return the idActorCoordinador
     */
    @Column(name = "ID_ACTOR_COORDINADOR", nullable = true)
    public Integer getIdActorCoordinador() {
        return this.idActorCoordinador;
    }

    /**
     * @param idActorSolicitante the idActorCoordinador to set
     */
    public void setIdActorCoordinador(Integer idActorCoordinador) {
        this.idActorCoordinador = idActorCoordinador;
    }

    /**
     * @return the itContrato
     */
    @Column(name = "IT_CONTRATO", nullable = true)
    public String getItContrato() {
        return this.itContrato;
    }

    /**
     * @param itContrato the itContrato to set
     */
    public void setItContrato(String itContrato) {
        this.itContrato = itContrato;
    }
    
    /**
     * 
     * @return itFormulario
     */
    @Column(name = "IT_CUMPLIMENTA_FORMULARIO", nullable = true)
    public String getItCumplimentaFormulario() {
        return itCumplimentaFormulario;
    }
    
    /**
     * 
     * @param itFormulario
     */
    public void setItCumplimentaFormulario(String itCumplimentaFormulario) {
        this.itCumplimentaFormulario = itCumplimentaFormulario;
    }
    
       
    /**
     * @return the itContratoIndefinido
     */
    @Column(name = "IT_CONTRATO_INDEFINIDO", nullable = true)
    public String getItContratoIndefinido() {
        return itContratoIndefinido;
    }

    /**
     * @param itContratoIndefinido the itContratoIndefinido to set
     */
    public void setItContratoIndefinido(String itContratoIndefinido) {
        this.itContratoIndefinido = itContratoIndefinido;
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
