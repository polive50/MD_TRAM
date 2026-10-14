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
 * Representa el objeto de dominio ExpedienteJusTec
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_JUSTEC_HIST")
@AtlasAuditarFila
@ToString
public class ExpedienteJusTecHist implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteJusTecHist del objeto de dominio
     */
    private Integer idExpedienteJusTecHist;

    /**
     * Representa el campo idExpedienteJusTec del objeto de dominio
     */
    private Integer idExpedienteJusTec;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo tipoExpedienteJusTecHist del objeto de dominio
     */
    private CatalogosValores tipoExpedienteJusTec;

    /**
     * Representa el campo nmPeriodo del objeto de dominio
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiCienTec del objeto de dominio
     */
    private Date fcIniJustiCienTec;

    /**
     * Representa el campo fcFinJustiCienTec del objeto de dominio
     */
    private Date fcFinJustiCienTec;

    /**
     * Representa el campo tlObservacion del objeto de dominio
     */
    private String tlObservacion;

    /**
     * Representa el campo tlObservacionParaCiudadano del objeto de dominio
     */
    private String tlObservacionParaCiudadano;

    /**
     * Representa el campo tlObservacionDelCiudadano del objeto de dominio
     */
    private String tlObservacionDelCiudadano;

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
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private CatalogosValores idEstadoRevAdmin;
    
    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores idEstado;
    
    /**
     * Representa el campo cdEreg de la presentacion del objeto de dominio
     */
    private String cdEregPresent;
    
    /**
     * Representa el campo cdEreg de la subsanacion del objeto de dominio
     */
    private String cdEregSubsana;
    
    /**
     * Representa el campo dsDestinoEreg del objeto de dominio
     */
    private String dsDestinoEreg;

    /**
     * Representa el campo fcFirma de la presentación del objeto de dominio
     */
    private Date fcFirmaPresent;
    
    /**
     * Representa el campo fcFirma de la subsanación del objeto de dominio
     */
    private Date fcFirmaSubsana;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteJusTecHist() {
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
        return new HashCodeBuilder().append(this.idExpedienteJusTecHist)
                .append(this.idExpedienteJusTec).append(this.idExpediente)
                .append(this.tipoExpedienteJusTec).append(this.nmPeriodo)
                .append(this.fcIniJustiCienTec).append(this.fcFinJustiCienTec)
                .append(this.tlObservacion)
                .append(this.tlObservacionParaCiudadano)
                .append(this.tlObservacionDelCiudadano)
                .append(this.idEstadoRevAdmin)
                .append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ExpedienteJusTecHist)) {
            return false;
        }
        ExpedienteJusTecHist castObj = (ExpedienteJusTecHist) obj;
        return new EqualsBuilder()
                .append(this.idExpedienteJusTecHist, castObj.idExpedienteJusTecHist)
                .append(this.idExpedienteJusTec, castObj.idExpedienteJusTec)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.tipoExpedienteJusTec, castObj.tipoExpedienteJusTec)
                .append(this.nmPeriodo, castObj.nmPeriodo)
                .append(this.fcIniJustiCienTec, castObj.fcIniJustiCienTec)
                .append(this.fcFinJustiCienTec, castObj.fcFinJustiCienTec)
                .append(this.tlObservacion, castObj.tlObservacion)
                .append(this.tlObservacionParaCiudadano, castObj.tlObservacionParaCiudadano)
                .append(this.tlObservacionDelCiudadano, castObj.tlObservacionDelCiudadano)
                .append(this.idEstadoRevAdmin, castObj.idEstadoRevAdmin)
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
     * @return Devuelve el valor del campo idExpedienteJusTecHist
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_JUSTEC_HIST")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_JUSTEC_HIST", sequenceName = "SAII_S_EXPEDIENTE_JUSTEC_HIST", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_JUSTEC_HIST")
    public Integer getIdExpedienteJusTecHist() {
        return this.idExpedienteJusTecHist;
    }

    /**
     * Establece el valor del campo idExpedienteJusTecHist de la entidad
     * ExpedienteJusTecHist
     *
     * @param idExpedienteJusTecHist El valor a establecer
     */
    public void setIdExpedienteJusTecHist(Integer idExpedienteJusTecHist) {
        this.idExpedienteJusTecHist = idExpedienteJusTecHist;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteJusTec
     */
    @Column(name = "ID_EXPEDIENTE_JUSTEC")
    public Integer getIdExpedienteJusTec() {
        return this.idExpedienteJusTec;
    }

    /**
     * Establece el valor del campo idExpedienteJusTec de la entidad
     * ExpedienteJusTecHist
     *
     * @param idExpedienteJusTec El valor a establecer
     */
    public void setIdExpedienteJusTec(Integer idExpedienteJusTec) {
        this.idExpedienteJusTec = idExpedienteJusTec;
    }

    /**
     * @return Devuelve el valor del campo idExpediente
     */
    @Column(name = "ID_EXPEDIENTE")
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad
     * ExpedienteJusTecHist
     *
     * @param idExpediente El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    /**
     * @return Devuelve el valor del campo tipoExpedienteJusTec
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_EXPEDIENTE_JUSTEC", nullable = false)
    public CatalogosValores getTipoExpedienteJusTec() {
        return this.tipoExpedienteJusTec;
    }

    /**
     * Establece el valor del campo tipoExpedienteJusTecHist de la entidad
     * ExpedienteJusTecHist
     *
     * @param tipoExpedienteJusTec El valor a establecer
     */
    public void setTipoExpedienteJusTec(CatalogosValores tipoExpedienteJusTec) {
        this.tipoExpedienteJusTec = tipoExpedienteJusTec;
    }

    /**
     * @return Devuelve el valor del campo nmPeriodo
     */
    @Column(name = "NM_PERIODO")
    public Integer getNmPeriodo() {
        return this.nmPeriodo;
    }

    /**
     * Establece el valor del campo nmPeriodo de la entidad
     * ExpedienteJusTecHist
     *
     * @param nmPeriodo El valor a establecer
     */
    public void setNmPeriodo(Integer nmPeriodo) {
        this.nmPeriodo = nmPeriodo;
    }

    /**
     * @return Devuelve el valor del campo fcIniJustiCienTec
     */
    @Column(name = "FC_INI_JUSTI_CIEN_TEC")
    public Date getFcIniJustiCienTec() {
        return this.fcIniJustiCienTec;
    }

    /**
     * Establece el valor del campo fcIniJustiCienTec de la entidad
     * ExpedienteJusTecHist
     *
     * @param fcIniJustiCienTec El valor a establecer
     */
    public void setFcIniJustiCienTec(Date fcIniJustiCienTec) {
        this.fcIniJustiCienTec = fcIniJustiCienTec;
    }

    /**
     * @return Devuelve el valor del campo fcFinJustiCienTec
     */
    @Column(name = "FC_FIN_JUSTI_CIEN_TEC")
    public Date getFcFinJustiCienTec() {
        return this.fcFinJustiCienTec;
    }

    /**
     * Establece el valor del campo fcFinJustiCienTec de la entidad
     * ExpedienteJusTecHist
     *
     * @param fcFinJustiCienTec El valor a establecer
     */
    public void setFcFinJustiCienTec(Date fcFinJustiCienTec) {
        this.fcFinJustiCienTec = fcFinJustiCienTec;
    }

    /**
     * @return Devuelve el valor del campo tlObservacion
     */
    @Column(name = "TL_OBSERVACION")
    public String getTlObservacion() {
        return this.tlObservacion;
    }

    /**
     * Establece el valor del campo tlObservacion de la entidad
     * ExpedienteJusTecHist
     *
     * @param tlObservacion El valor a establecer
     */
    public void setTlObservacion(String tlObservacion) {
        this.tlObservacion = tlObservacion;
    }

    @Column(name = "TL_OBSERVACI_PARA_CIUDADANO")
    public String getTlObservacionParaCiudadano() {
        return this.tlObservacionParaCiudadano;
    }

    public void setTlObservacionParaCiudadano(String tlObservacionParaCiudadano) {
        this.tlObservacionParaCiudadano = tlObservacionParaCiudadano;
    }

    @Column(name = "TL_OBSERVACI_DEL_CIUDADANO")
    public String getTlObservacionDelCiudadano() {
        return this.tlObservacionDelCiudadano;
    }

    public void setTlObservacionDelCiudadano(String tlObservacionDelCiudadano) {
        this.tlObservacionDelCiudadano = tlObservacionDelCiudadano;
    }
    
    /**
     * @return Devuelve el valor del campo idEstadoRevAdmin
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_ADMIN", referencedColumnName = "ID_VALOR")
    public CatalogosValores getIdEstadoRevAdmin() {
        return this.idEstadoRevAdmin;
    }

    /**
     * Establece el valor del campo idEstadoRevAdmin de la entidad
     * ExpedienteJusTecHist
     *
     * @param idEstadoRevAdmin El valor a establecer
     */
    public void setIdEstadoRevAdmin(CatalogosValores idEstadoRevAdmin) {
        this.idEstadoRevAdmin = idEstadoRevAdmin;
    }
    
    /**
     * @return Devuelve el valor del campo idEstado
     */
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = true)
    public CatalogosValores getIdEstado() {
        return this.idEstado;
    }
    
    /**
     * Establece el valor del campo idEstado de la entidad ExpedienteJusTecHist
     *
     * @param idEstado El valor a establecer
     */
    public void setIdEstado(CatalogosValores idEstado) {
        this.idEstado = idEstado;
    }
    
    @Column(name = "CD_EREG_PRESENT", nullable = true)
    public String getCdEregPresent() {
        return cdEregPresent;
    }

    public void setCdEregPresent(String cdEregPresent) {
        this.cdEregPresent = cdEregPresent;
    }

    @Column(name = "CD_EREG_SUBSANA", nullable = true)
    public String getCdEregSubsana() {
        return cdEregSubsana;
    }

    public void setCdEregSubsana(String cdEregSubsana) {
        this.cdEregSubsana = cdEregSubsana;
    }
    
    @Column(name = "DS_DESTINO_EREG", nullable = true)
    public String getDsDestinoEreg() {
        return dsDestinoEreg;
    }

    public void setDsDestinoEreg(String dsDestinoEreg) {
        this.dsDestinoEreg = dsDestinoEreg;
    }

    @Column(name = "FC_FIRMA_PRESENT", nullable = true)
    public Date getFcFirmaPresent() {
        return fcFirmaPresent;
    }

    public void setFcFirmaPresent(Date fcFirmaPresent) {
        this.fcFirmaPresent = fcFirmaPresent;
    }

    @Column(name = "FC_FIRMA_SUBSANA", nullable = true)
    public Date getFcFirmaSubsana() {
        return fcFirmaSubsana;
    }

    public void setFcFirmaSubsana(Date fcFirmaSubsana) {
        this.fcFirmaSubsana = fcFirmaSubsana;
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
     * Establece el valor del campo cdUsuAlta de la entidad ExpedienteJusTecHist
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
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
     * Establece el valor del campo fcAltaFila de la entidad
     * ExpedienteJusTecHist
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF")
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad
     * ExpedienteJusTecHist
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA")
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad
     * ExpedienteJusTecHist
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA")
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad ExpedienteJusTecHist
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBfcBajaFilaaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA")
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad
     * ExpedienteJusTecHist
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

}
