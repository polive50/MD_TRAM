package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ExpedienteModifPresPlazoOtra
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPE_MOD_PRE_PLA_OTRA")
@AtlasAuditarFila
@ToString
public class ExpedienteModifPresPlazoOtra implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Representa el campo idExpedienteModifPresPlazoOtra del objeto de dominio
     */
    private Integer idExpedienteModifPresPlazoOtra;

    /**
     * Representa el campo expediente del objeto de dominio
     */
    private Expediente expediente;
    
    /**
     * Representa el campo idModificacion del objeto de dominio
     */
    private Integer idModificacion;
    
    /**
      * Representa el campo itModPres del objeto de dominio
      */
     private String itModPres;
     
     /**
      * Representa el campo itModPlazo del objeto de dominio
      */
     private String itModPlazo;
     
     /**
      * Representa el campo itModOtra del objeto de dominio
      */
     private String itModOtra;
     
     /**
      * Representa el campo tlObservaciones del objeto de dominio
      */
     private String tlObservaciones;
    
    /**
     * Representa el campo fcSolicitud del objeto de dominio
     */
    private Date fcSolicitud;
    
    /**
     * Representa el campo fcResolucion del objeto de dominio
     */
    private Date fcResolucion;

    /**
     * Representa el campo estadoResolucion del objeto de dominio
     */
    private CatalogosValores estadoResolucion;
    
    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;
    
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
    
    /**
     * Representa el campo idImpreso del objeto de dominio
     */
    private Integer idImpreso;

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
     * Representa el campo idEstadoRevAdmin del objeto de dominio
     */
    private CatalogosValores estadoRevAdmin;

    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteModifPresPlazoOtra() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return Objects.hash(cdUsuAlta, cdUsuBaja, cdUsuModif, estado, estadoResolucion, fcAltaFila,
                fcBajaFila, fcModifFila, fcResolucion, fcSolicitud, expediente,
                idExpedienteModifPresPlazoOtra, idModificacion, itModOtra, itModPlazo, itModPres,
                tlObservaciones, estadoRevAdmin);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpedienteModifPresPlazoOtra other = (ExpedienteModifPresPlazoOtra) obj;
        return Objects.equals(cdUsuAlta, other.cdUsuAlta)
                && Objects.equals(cdUsuBaja, other.cdUsuBaja)
                && Objects.equals(cdUsuModif, other.cdUsuModif)
                && Objects.equals(estado, other.estado)
                && Objects.equals(estadoResolucion, other.estadoResolucion)
                && Objects.equals(fcAltaFila, other.fcAltaFila)
                && Objects.equals(fcBajaFila, other.fcBajaFila)
                && Objects.equals(fcModifFila, other.fcModifFila)
                && Objects.equals(fcResolucion, other.fcResolucion)
                && Objects.equals(fcSolicitud, other.fcSolicitud)
                && Objects.equals(expediente, other.expediente)
                && Objects.equals(idExpedienteModifPresPlazoOtra,
                        other.idExpedienteModifPresPlazoOtra)
                && Objects.equals(idModificacion, other.idModificacion)
                && Objects.equals(itModOtra, other.itModOtra)
                && Objects.equals(itModPlazo, other.itModPlazo)
                && Objects.equals(itModPres, other.itModPres)
                && Objects.equals(estadoRevAdmin, other.estadoRevAdmin)
                && Objects.equals(tlObservaciones, other.tlObservaciones);
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteModifPresPlazoOtra
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXP_MOD_PRES_PLAZO_OTRA")
    @SequenceGenerator(name = "SAII_S_EXP_MOD_PRES_PLAZO_OTRA", sequenceName = "SAII_S_EXP_MOD_PRES_PLAZO_OTRA", allocationSize = 1)
    @Column(name = "ID_EXPE_MOD_PRE_PLA_OTRA", nullable = false)
    public Integer getIdExpedienteModifPresPlazoOtra() {
        return this.idExpedienteModifPresPlazoOtra;
    }

    /**
     * Establece el valor del campo idExpedienteModifPresPlazoOtra de la entidad
     *
     * @param idExpedienteModifPresPlazoOtra El valor a establecer
     */
    public void setIdExpedienteModifPresPlazoOtra(Integer idExpedienteModifPresPlazoOtra) {
        this.idExpedienteModifPresPlazoOtra = idExpedienteModifPresPlazoOtra;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    /**
     * Establece el valor del campo expediente de la entidad
     *
     * @param expediente El valor a establecer
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    /**
     * @return the idModificacion
     */
    @Column(name = "ID_MODIFICACION", nullable = false)
    public Integer getIdModificacion() {
        return this.idModificacion;
    }

    /**
     * @param idModificacion the idModificacion to set
     */
    public void setIdModificacion(Integer idModificacion) {
        this.idModificacion = idModificacion;
    }
    
    /**
     * @return the idImpreso
     */
    @Column(name = "ID_IMPRESO", nullable = false)
    public Integer getIdImpreso() {
        return this.idImpreso;
    }

    /**
     * @param idImpreso the idImpreso to set
     */
    public void setIdImpreso(Integer idImpreso) {
        this.idImpreso = idImpreso;
    }
    
    /**
     * @return the itModPres
     */
    @Column(name = "IT_MOD_PRES", nullable = false)
    public String getItModPres() {
        return this.itModPres;
    }

    /**
     * @param itModPres the itModPres to set
     */
    public void setItModPres(String itModPres) {
        this.itModPres = itModPres;
    }
    
    /**
     * @return the itModPlazo
     */
    @Column(name = "IT_MOD_PLAZO", nullable = false)
    public String getItModPlazo() {
        return this.itModPlazo;
    }

    /**
     * @param itModPlazo the itModPlazo to set
     */
    public void setItModPlazo(String itModPlazo) {
        this.itModPlazo = itModPlazo;
    }
    
    /**
     * @return the itModOtra
     */
    @Column(name = "IT_MOD_OTRA", nullable = false)
    public String getItModOtra() {
        return this.itModOtra;
    }

    /**
     * @param itModOtra the itModOtra to set
     */
    public void setItModOtra(String itModOtra) {
        this.itModOtra = itModOtra;
    }
    
    /**
     * @return Devuelve el valor del campo tlObservaciones
     */
    @Column(name = "TL_OBSERVACIONES")
    public String getTlObservaciones() {
        return this.tlObservaciones;
    }

    /**
     * Establece el valor del campo tlObservaciones de la entidad
     *
     * @param tlObservaciones El valor a establecer
     */
    public void setTlObservaciones(String tlObservaciones) {
        this.tlObservaciones = tlObservaciones;
    }
    
    /**
     * @return Devuelve el valor del campo fcSolicitud
     */
    @Column(name = "FC_SOLICITUD", nullable = false)
    public Date getFcSolicitud() {
        return this.fcSolicitud;
    }

    /**
     * Establece el valor del campo fcSolicitud de la entidad
     *
     * @param fcSolicitud El valor a establecer
     */
    public void setFcSolicitud(Date fcSolicitud) {
        this.fcSolicitud = fcSolicitud;
    }
    
    /**
     * @return Devuelve el valor del campo fcResolucion
     */
    @Column(name = "FC_RESOLUCION", nullable = true)
    public Date getFcResolucion() {
        return this.fcResolucion;
    }

    /**
     * Establece el valor del campo fcResolucion de la entidad
     *
     * @param fcResolucion El valor a establecer
     */
    public void setFcResolucion(Date fcResolucion) {
        this.fcResolucion = fcResolucion;
    }

    /**
     * @return the estadoResolucion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_RESOLUCION", nullable = true)
    public CatalogosValores getEstadoResolucion() {
        return this.estadoResolucion;
    }
    
    /**
     * @param estadoResolucion the estadoResolucion to set
     */
    public void setEstadoResolucion(CatalogosValores estadoResolucion) {
        this.estadoResolucion = estadoResolucion;
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
    
    /**
     * @return Devuelve el valor del campo idEstadoRevAdmin
     */
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_REV_ADMIN", referencedColumnName = "ID_VALOR")
    public CatalogosValores getEstadoRevAdmin() {
        return this.estadoRevAdmin;
    }
    
    /**
     * Establece el valor del campo idEstadoRevAdmin de la entidad ExpedienteJusTec
     *
     * @param idEstadoRevAdmin El valor a establecer
     */
    public void setEstadoRevAdmin(CatalogosValores idEstadoRevAdmin) {
        this.estadoRevAdmin = idEstadoRevAdmin;
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
