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
 * Representa el objeto de dominio Expediente
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_HIST")
@AtlasAuditarFila
@ToString
public class ExpedienteHist implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteHist del objeto de dominio
     */
    private Integer idExpedienteHist;

    /**
     * Representa el campo idExpedienteHist del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo cdExpediente del objeto de dominio
     */
    private String cdExpediente;

    /**
     * Representa el campo idSolicitud del objeto de dominio
     */
    private Integer idSolicitud;

    /**
     * Representa el campo interesado del objeto de dominio
     */
    private Interesado interesado;

    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estadoBenef;

    /**
     * Representa el id del campo convocatoriaAct del expediente asociado
     */
    private Integer idConvocatoriaAct;

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
     * Representa el campo cdEreg del objeto de dominio
     */
    private String cdEreg;

    /**
     * Representa el campo dsDestinoEreg del objeto de dominio
     */
    private String dsDestinoEreg;

    /**
     * Representa el campo fcFirma del objeto de dominio
     */
    private Date fcFirma;

    /**
     * Representa el campo nmPASO del objeto de dominio
     */
    private Integer nmPaso;

    /**
     * Representa el campo cdSiex del objeto de dominio
     */
    private String cdSiex;

    /**
     * Representa el campo nmNota del objeto de dominio
     */
    private Integer nmNota;

    /**
     * Representa el campo estadoRevAdmin del objeto de dominio
     */
    private CatalogosValores estadoRevAdmin;

    /**
     * Representa el campo estadoRevRequi del objeto de dominio
     */
    private CatalogosValores estadoRevRequi;

    /**
     * Representa el campo estadoRevEval del objeto de dominio
     */
    private CatalogosValores estadoRevEval;

    /**
     * Representa el campo idProvisional del objeto de dominio
     */
    private CatalogosValores estadoProvisional;

    /**
     * Representa el campo itAlegaciones del objeto de dominio
     */
    private String itAlegaciones;

    /**
     * Representa el campo cdFicaValoracionAleg del objeto de dominio
     */
    private Integer cdFicaValoracionAleg;

    /**
     * Representa el campo fcIniContrato del objeto de dominio
     */
    private Date fcIniContrato;

    /**
     * Representa el campo fcFinContrato del objeto de dominio
     */
    private Date fcFinContrato;
    
    
    /**
     * Representa el campo cdEreg de la subsanacion de requisitos del objeto de dominio
     */
    private String cdEregSubsanaReq;
    
    /**
     * Representa el campo fcFirma de la subsanación de requisitos del objeto de dominio
     */
    private Date fcFirmaSubsanaReq;
    
    /**
     * Representa el campo motivoRechazo  del objeto de dominio
     */
    private String motivoRechazo;
    
    /**
     * Representa el campo convocatoriaArea del objeto de dominio
     */
    private ConvocatoriaArea convocatoriaArea;
    
    /**
     * Representa el campo convocatoriaAreaSubarea del objeto de dominio
     */
    private ConvocatoriaAreaSubarea convocatoriaAreaSubarea;
    
    /**
     * Representa el campo estadoRevFinan del objeto de dominio
     */
    private CatalogosValores estadoRevFinan;
    
    
    /**
     * Representa el campo representanteLegal del objeto de dominio
     */
    private Interesado representanteLegal;

    /**
     * Representa el campo estadoRevJusTec del objeto de dominio
     */
    private CatalogosValores estadoRevJusTec;

    /**
     * Representa el campo estadoRevContrato del objeto de dominio
     */
    private CatalogosValores estadoRevContrato;

    /**
     * Representa el campo convocatoriaAct del objeto de dominio
     */
    private ConvocatoriaAct convocatoriaAct;
    
    /**
     * Representa el campo itAlegacionRevisada
     */
    private String itAlegacionRevisada;
    
    
    /**
     * Representa el campo idMedioNotificacion del objeto de dominio
     */
    private CatalogosValores idMedioNotificacion;
    
    
    /**
     * Representa el campo idExpedientePadre del objeto de dominio
     */
    private Integer idExpedientePadre;
    
    /**
     * Representa el campo cdEreg de la subsanacion administrativa del objeto de dominio
     */
    
    private String cdEregSubsanaAdm;
    
    
    /**
     * Representa el campo fcFirma de la subsanación administrativa del objeto de dominio
     */
    private Date fcFirmaSubsanaAdm;
    
    
    /**
     * Representa el campo estadoRevJusEco del objeto de dominio
     */
    private CatalogosValores estadoRevJusEco;
    
    
    /**
     * Representa el campo idPersonaSolicitante del objeto de dominio
     */
    private CatalogosValores idPersonaSolicitante;   
    
    /**
     * Representa el campo convocatoriaSubmod del objeto de dominio
     */
    private ConvocatoriaSubmod convocatoriaSubmod;
    
    /**
     * Representa el campo cdEreg de la alegaciones del objeto de dominio
     */
    private String cdEregAlegaciones;
    
    /**
     * Representa el campo fcFirma de la alegaciones del objeto de dominio
     */
    private Date fcFirmaAlegaciones;
    
    /**
     * Representa el campo itAceptaAyuda del objeto de dominio
     */
    private String itAceptaAyuda;

    /**
     * Representa el campo autorizado del objeto de dominio
     */
    private Interesado autorizado;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteHist() {
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
        return new HashCodeBuilder().append(this.idExpedienteHist).append(this.cdExpediente)
                .append(this.cdUsuAlta).append(this.fcAltaFila).toHashCode();
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
        if (!(obj instanceof ExpedienteHist)) {
            return false;
        }
        ExpedienteHist castObj = (ExpedienteHist) obj;
        return new EqualsBuilder().append(this.idExpedienteHist, castObj.idExpedienteHist)
                .append(this.cdExpediente, castObj.cdExpediente).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpediente
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_HIST")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_HIST", sequenceName = "SAII_S_EXPEDIENTE_HIST", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_HIST")
    public Integer getIdExpedienteHist() {
        return this.idExpedienteHist;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad Expediente
     *
     * @param idExpediente El valor a establecer
     */
    public void setIdExpedienteHist(Integer idExpedienteHist) {
        this.idExpedienteHist = idExpedienteHist;
    }

    @Column(name = "ID_EXPEDIENTE")
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad Expediente
     *
     * @param idExpediente El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    /**
     * @return Devuelve el valor del campo cdExpediente
     */

    @Column(name = "CD_EXPEDIENTE", nullable = true)
    public String getCdExpediente() {
        return this.cdExpediente;
    }

    /**
     * Establece el valor del campo cdExpediente de la entidad Expediente
     *
     * @param cdExpediente El valor a establecer
     */
    public void setCdExpediente(String cdExpediente) {
        this.cdExpediente = cdExpediente;
    }

    /**
     * @return the idSolicitud
     */
    @Column(name = "ID_SOLICITUD", nullable = false)
    public Integer getIdSolicitud() {
        return this.idSolicitud;
    }

    /**
     * @param idSolicitud the idSolicitud to set
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    /**
     * @return the convocatoriaAct
     */
    @Column(name = "ID_ACTOR", nullable = true)
    public Integer getIdConvocatoriaAct() {
        return this.idConvocatoriaAct;
    }

    /**
     * Establece el valor del campo convocatoriaAct de la entidad Participante
     *
     * @param convocatoriaAct El valor a establecer
     */
    public void setIdConvocatoriaAct(Integer idConvocatoriaAct) {
        this.idConvocatoriaAct = idConvocatoriaAct;
    }

    /**
     * @return the interesado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    public Interesado getInteresado() {
        return this.interesado;
    }

    /**
     * @param interesado the interesado to set
     */
    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
    }

    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return the estadoBenef
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_BENEF", nullable = false)
    public CatalogosValores getEstadoBenef() {
        return this.estadoBenef;
    }

    /**
     * @param estadoBenef the estadoBenef to set
     */
    public void setEstadoBenef(CatalogosValores estadoBenef) {
        this.estadoBenef = estadoBenef;
    }

    /**
     * @return Devuelve el valor del campo cdEreg
     */
    @Column(name = "CD_EREG", nullable = true)
    public String getCdEreg() {
        return this.cdEreg;
    }

    /**
     * Establece el valor del campo cdEreg de la entidad Expediente
     *
     * @param cdEreg El valor a establecer
     */
    public void setCdEreg(String cdEreg) {
        this.cdEreg = cdEreg;
    }

    /**
     * @return Devuelve el valor del campo dsDestinoEreg
     */
    @Column(name = "DS_DESTINO_EREG", nullable = true)
    public String getDsDestinoEreg() {
        return this.dsDestinoEreg;
    }

    /**
     * Establece el valor del campo dsDestinoEreg de la entidad Expediente
     *
     * @param dsDestinoEreg El valor a establecer
     */
    public void setDsDestinoEreg(String dsDestinoEreg) {
        this.dsDestinoEreg = dsDestinoEreg;
    }

    /**
     * @return Devuelve el valor del campo fcFirma
     */
    @Column(name = "FC_FIRMA", nullable = true)
    public Date getFcFirma() {
        return this.fcFirma;
    }

    /**
     * Establece el valor del campo fcFirma de la entidad Expediente
     *
     * @param fcFirma El valor a establecer
     */
    public void setFcFirma(Date fcFirma) {
        this.fcFirma = fcFirma;
    }

    /**
     * @return the nmPaso
     */
    @Column(name = "NM_PASO", nullable = true)
    public Integer getNmPaso() {
        return this.nmPaso;
    }

    /**
     * @param nmPaso the nmPaso to set
     */
    public void setNmPaso(Integer nmPaso) {
        this.nmPaso = nmPaso;
    }

    /**
     * @return the nmNota
     */

    @Column(name = "NM_NOTA", nullable = true)
    public Integer getNmNota() {
        return this.nmNota;
    }

    public void setNmNota(Integer nmNota) {
        this.nmNota = nmNota;
    }

    /**
     * @return the cdSiex
     */
    @Column(name = "CD_SIEX", nullable = true)
    public String getCdSiex() {
        return this.cdSiex;
    }

    /**
     * @param cdSiex the cdSiex to set
     */
    public void setCdSiex(String cdSiex) {
        this.cdSiex = cdSiex;
    }

    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_ADMIN", nullable = true)
    public CatalogosValores getEstadoRevAdmin() {
        return this.estadoRevAdmin;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstadoRevAdmin(CatalogosValores estadoRevAdmin) {
        this.estadoRevAdmin = estadoRevAdmin;
    }

    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_REQUI", nullable = true)
    public CatalogosValores getEstadoRevRequi() {
        return this.estadoRevRequi;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstadoRevRequi(CatalogosValores estadoRevRequi) {
        this.estadoRevRequi = estadoRevRequi;
    }

    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_EVA", nullable = true)
    public CatalogosValores getEstadoRevEval() {
        return this.estadoRevEval;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstadoRevEval(CatalogosValores estadoRevEval) {
        this.estadoRevEval = estadoRevEval;
    }

    /**
     * @return the estadoProvisional
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROVISIONAL", nullable = true)
    public CatalogosValores getEstadoProvisional() {
        return this.estadoProvisional;
    }

    /**
     * @param estadoProvisional the estadoProvisional to set
     */
    public void setEstadoProvisional(CatalogosValores estadoProvisional) {
        this.estadoProvisional = estadoProvisional;
    }

    /**
     * @return the itAlegaciones
     */
    @Column(name = "IT_ALEGACIONES", nullable = true)
    public String getItAlegaciones() {
        return this.itAlegaciones;
    }

    /**
     * @param itAlegaciones the itAlegaciones to set
     */
    public void setItAlegaciones(String itAlegaciones) {
        this.itAlegaciones = itAlegaciones;
    }

    /**
     * @return the cdFicaValoracionAleg
     */
    @Column(name = "CD_FICA_VALORACION_ALEG", nullable = true)
    public Integer getCdFicaValoracionAleg() {
        return this.cdFicaValoracionAleg;
    }

    /**
     * @param cdFicaValoracionAleg the cdFicaValoracionAleg to set
     */
    public void setCdFicaValoracionAleg(Integer cdFicaValoracionAleg) {
        this.cdFicaValoracionAleg = cdFicaValoracionAleg;
    }

    /**
     * @return the fcIniContrato
     */
    @Column(name = "FC_INI_CONTRATO", nullable = true)
    public Date getFcIniContrato() {
        return this.fcIniContrato;
    }

    /**
     * @param the fcIniContrato to set
     */
    public void setFcIniContrato(Date fcIniContrato) {
        this.fcIniContrato = fcIniContrato;
    }

    /**
     * @return the fcFinContrato
     */
    @Column(name = "FC_FIN_CONTRATO", nullable = true)
    public Date getFcFinContrato() {
        return this.fcFinContrato;
    }

    /**
     * @param the fcFinContrato to set
     */
    public void setFcFinContrato(Date fcFinContrato) {
        this.fcFinContrato = fcFinContrato;
    }
    
    @Column(name = "CD_EREG_SUBSANA_REQ", nullable = true)
    public String getCdEregSubsanaReq() {
        return this.cdEregSubsanaReq;
    }

    public void setCdEregSubsanaReq(String cdEregSubsanaReq) {
        this.cdEregSubsanaReq = cdEregSubsanaReq;
    }
    
    @Column(name = "FC_FIRMA_SUBSANA_REQ", nullable = true)
    public Date getFcFirmaSubsanaReq() {
        return this.fcFirmaSubsanaReq;
    }

    public void setFcFirmaSubsanaReq(Date fcFirmaSubsanaReq) {
        this.fcFirmaSubsanaReq = fcFirmaSubsanaReq;
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
    
    
    @Column(name = "MOTIVO_RECHAZO", nullable = true)
    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBAREA", nullable = true)
    public ConvocatoriaAreaSubarea getConvocatoriaAreaSubarea() {
        return convocatoriaAreaSubarea;
    }

    public void setConvocatoriaAreaSubarea(ConvocatoriaAreaSubarea convocatoriaAreaSubarea) {
        this.convocatoriaAreaSubarea = convocatoriaAreaSubarea;
    }
    
    /**
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AREA", nullable = true)
    public ConvocatoriaArea getConvocatoriaArea() {
        return this.convocatoriaArea;
    }

    /**
     *
     * @param convocatoriaArea
     */
    public void setConvocatoriaArea(ConvocatoriaArea convocatoriaArea) {
        this.convocatoriaArea = convocatoriaArea;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_FINAN", nullable = true)
    public CatalogosValores getEstadoRevFinan() {
        return estadoRevFinan;
    }

    public void setEstadoRevFinan(CatalogosValores estadoRevFinan) {
        this.estadoRevFinan = estadoRevFinan;
    }


    /**
     * @return the representanteLegal
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTER_REPLEG")
    public Interesado getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(Interesado representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEDIO_COMUN", nullable = true)
    public CatalogosValores getIdMedioNotificacion() {
        return idMedioNotificacion;
    }

    public void setIdMedioNotificacion(CatalogosValores idMedioNotificacion) {
        this.idMedioNotificacion = idMedioNotificacion;
    }
    
    
    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_JUSTEC", nullable = true)
    public CatalogosValores getEstadoRevJusTec() {
        return this.estadoRevJusTec;
    }

    /**
     * @param estadoRevJusTec the estado to set
     */
    public void setEstadoRevJusTec(CatalogosValores estadoRevJusTec) {
        this.estadoRevJusTec = estadoRevJusTec;
    }

    
    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_CONT", nullable = true)
    public CatalogosValores getEstadoRevContrato() {
        return this.estadoRevContrato;
    }

    /**
     * @param setEstadoRevContrato the estado to set
     */
    public void setEstadoRevContrato(CatalogosValores estadoRevContrato) {
        this.estadoRevContrato = estadoRevContrato;
    }
 
    
    /**
     * @return the itAlegacionRevisada
     */
    @Column(name = "IT_ALEGACION_REVISADA", nullable = true)
    public String getItAlegacionRevisada() {
        return itAlegacionRevisada;
    }

    /**
     * @param itAlegacionRevisada the itAlegacionRevisada to set
     */
    public void setItAlegacionRevisada(String itAlegacionRevisada) {
        this.itAlegacionRevisada = itAlegacionRevisada;
    }
    
    /**
     * @return the idExpedientePadre
     */
    @Column(name = "ID_EXPEDIENTE_PADRE", nullable = true)
    public Integer getIdExpedientePadre() {
        return this.idExpedientePadre;
    }

    /**
     * @param idExpedientePadre the idExpedientePadre to set
     */
    public void setIdExpedientePadre(Integer idExpedientePadre) {
        this.idExpedientePadre = idExpedientePadre;
    }
    

    /**
     * @return the cdEregSubsanaAdm
     */
    @Column(name = "CD_EREG_SUBSANA_ADM", nullable = true)
    public String getCdEregSubsanaAdm() {
        return cdEregSubsanaAdm;
    }
    
    /**
     * @param cdEregSubsanaAdm the cdEregSubsanaAdm to set
     */
    public void setCdEregSubsanaAdm(String cdEregSubsanaAdm) {
        this.cdEregSubsanaAdm = cdEregSubsanaAdm;
    }
    
    /**
     * @return the fcFirmaSubsanaAdm
     */
    @Column(name = "FC_FIRMA_SUBSANA_ADM", nullable = true)
    public Date getFcFirmaSubsanaAdm() {
        return fcFirmaSubsanaAdm;
    }

    /**
     * @param fcFirmaSubsanaAdm the fcFirmaSubsanaAdm to set
     */
    public void setFcFirmaSubsanaAdm(Date fcFirmaSubsanaAdm) {
        this.fcFirmaSubsanaAdm = fcFirmaSubsanaAdm;
    }
    
    /**
     * @return the estadoRevJusEco
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_JUSECO", nullable = true)
    public CatalogosValores getEstadoRevJusEco() {
        return this.estadoRevJusEco;
    }

    /**
     * @param estadoRevJusEco the estado to set
     */
    public void setEstadoRevJusEco(CatalogosValores estadoRevJusEco) {
        this.estadoRevJusEco = estadoRevJusEco;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_PERS_SOL", nullable = true)
    public CatalogosValores getIdPersonaSolicitante() {
        return idPersonaSolicitante;
    }

    public void setIdPersonaSolicitante(CatalogosValores idPersonaSolicitante) {
        this.idPersonaSolicitante = idPersonaSolicitante;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_SUBMOD", nullable = true)
    public ConvocatoriaSubmod getConvocatoriaSubmod() {
        return this.convocatoriaSubmod;
    }

    /**
     *
     * @param convocatoriaSubmod
     */
    public void setConvocatoriaSubmod(ConvocatoriaSubmod convocatoriaSubmod) {
        this.convocatoriaSubmod = convocatoriaSubmod;
    }

    
    @Column(name = "CD_EREG_ALEGACIONES", nullable = true)
     public String getCdEregAlegaciones() {
         return cdEregAlegaciones;
     }
     
     public void setCdEregAlegaciones(String cdEregAlegaciones) {
         this.cdEregAlegaciones = cdEregAlegaciones;
     }
     
     @Column(name = "FC_FIRMA_ALEGACIONES", nullable = true)
     public Date getFcFirmaAlegaciones() {
         return fcFirmaAlegaciones;
     }
     
     public void setFcFirmaAlegaciones(Date fcFirmaAlegaciones) {
         this.fcFirmaAlegaciones = fcFirmaAlegaciones;
     }
     
     @Column(name = "IT_ACEPTA_AYUDA", nullable = true)
     public String getItAceptaAyuda() {
         return itAceptaAyuda;
     }

     public void setItAceptaAyuda(String itAceptaAyuda) {
         this.itAceptaAyuda = itAceptaAyuda;
     }

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "ID_INTER_AUTORIZADO")
     public Interesado getAutorizado() {
         return autorizado;
     }

     public void setAutorizado(Interesado autorizado) {
         this.autorizado = autorizado;
     }
    
}
