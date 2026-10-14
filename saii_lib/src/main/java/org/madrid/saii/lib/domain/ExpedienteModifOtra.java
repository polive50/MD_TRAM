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
 * Representa el objeto de dominio ExpedienteModifOtra
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_MODIF_OTRA")
@AtlasAuditarFila
@ToString
public class ExpedienteModifOtra implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Representa el campo idExpedienteModifOtra del objeto de dominio
     */
    private Integer idExpedienteModifOtra;

    /**
     * Representa el campo expedienteModifPresPlazoOtra del objeto de dominio
     */
    private ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra;
    
     /**
      * Representa el campo tlOtrasModif del objeto de dominio
      */
     private String tlOtrasModif;
     
     private String tlOtrasModifObservaciones;
    
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
    public ExpedienteModifOtra() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return Objects.hash(cdUsuAlta, cdUsuBaja, cdUsuModif, fcAltaFila,
                fcBajaFila, fcModifFila, 
                idExpedienteModifOtra, expedienteModifPresPlazoOtra,
                tlOtrasModif,tlOtrasModifObservaciones);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpedienteModifOtra other = (ExpedienteModifOtra) obj;
        return Objects.equals(cdUsuAlta, other.cdUsuAlta)
                && Objects.equals(cdUsuBaja, other.cdUsuBaja)
                && Objects.equals(cdUsuModif, other.cdUsuModif)
                && Objects.equals(fcAltaFila, other.fcAltaFila)
                && Objects.equals(fcBajaFila, other.fcBajaFila)
                && Objects.equals(fcModifFila, other.fcModifFila)
                && Objects.equals(expedienteModifPresPlazoOtra, other.expedienteModifPresPlazoOtra)
                && Objects.equals(idExpedienteModifOtra,
                        other.idExpedienteModifOtra)
                && Objects.equals(tlOtrasModif, other.tlOtrasModif)
                && Objects.equals(tlOtrasModifObservaciones, other.tlOtrasModifObservaciones);
        
        
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteModifOtra
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_MODIF_OTRA")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_MODIF_OTRA", sequenceName = "SAII_S_EXPEDIENTE_MODIF_OTRA", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_MODIF_OTRA", nullable = false)
    public Integer getIdExpedienteModifOtra() {
        return this.idExpedienteModifOtra;
    }

    /**
     * Establece el valor del campo idExpedienteModifOtra de la entidad
     *
     * @param idExpedienteModifOtra El valor a establecer
     */
    public void setIdExpedienteModifOtra(Integer idExpedienteModifOtra) {
        this.idExpedienteModifOtra = idExpedienteModifOtra;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPE_MOD_PRE_PLA_OTRA", nullable = false)
    public ExpedienteModifPresPlazoOtra getExpedienteModifPresPlazoOtra() {
        return this.expedienteModifPresPlazoOtra;
    }

    /**
     * Establece el valor del campo expedienteModifPresPlazoOtra de la entidad
     *
     * @param expedienteModifPresPlazoOtra El valor a establecer
     */
    public void setExpedienteModifPresPlazoOtra(ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra) {
        this.expedienteModifPresPlazoOtra = expedienteModifPresPlazoOtra;
    }

    /**
     * @return Devuelve el valor del campo tlOtrasModif
     */
    @Column(name = "TL_OTRAS_MODIF")
    public String getTlOtrasModif() {
        return this.tlOtrasModif;
    }

    /**
     * Establece el valor del campo tlOtrasModif de la entidad
     *
     * @param tlOtrasModif El valor a establecer
     */
    public void setTlOtrasModif(String tlOtrasModif) {
        this.tlOtrasModif = tlOtrasModif;
    }
    

    /**
     * @return Devuelve el valor del campo tlOtrasModifObservaciones
     */
    @Column(name = "TL_OBSERV_OTRAS")
    public String getTlOtrasModifObservaciones() {
        return this.tlOtrasModifObservaciones;
    }

    /**
     * Establece el valor del campo tlOtrasModifObservaciones de la entidad
     *
     * @param tlOtrasModif El valor a establecer
     */
    public void setTlOtrasModifObservaciones(String tlOtrasModifObservaciones) {
        this.tlOtrasModifObservaciones = tlOtrasModifObservaciones;
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
