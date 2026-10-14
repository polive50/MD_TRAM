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
import atlas.core.database.auditoria.AtlasAuditarFila;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import lombok.ToString;

/**
 * Representa el objeto de dominio ConvocatoriaEntidadCerrada
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_ENT_CER")
@AtlasAuditarFila
@ToString
public class ConvocatoriaEntidadCerrada implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
        
    /**
     * Representa el campo idConvocatoriaEntCer del objeto de dominio
     */
    private Integer idConvocatoriaEntCer;
    
    /**
     * Representa el campo convocator del objeto de dominio
     */
    private Convocatoria convocatoria;
    
    /**
     * Representa el campo entidadcerrada del objeto de dominio
     */
    private CatalogosValores entidadCerrada;
 
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

    public ConvocatoriaEntidadCerrada() {
        super();
    }
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// HASH, EQUAL
    // ////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return Objects.hash(cdUsuAlta, cdUsuBaja, cdUsuModif, fcAltaFila, fcBajaFila, fcModifFila,
                convocatoria, idConvocatoriaEntCer, entidadCerrada);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConvocatoriaEntidadCerrada)) {
            return false;
        }
        ConvocatoriaEntidadCerrada other = (ConvocatoriaEntidadCerrada) obj;
        return Objects.equals(cdUsuAlta, other.cdUsuAlta)
                && Objects.equals(cdUsuBaja, other.cdUsuBaja)
                && Objects.equals(cdUsuModif, other.cdUsuModif)
                && Objects.equals(fcAltaFila, other.fcAltaFila)
                && Objects.equals(fcBajaFila, other.fcBajaFila)
                && Objects.equals(fcModifFila, other.fcModifFila)
                && Objects.equals(convocatoria, other.convocatoria)
                && Objects.equals(idConvocatoriaEntCer, other.idConvocatoriaEntCer)
                && Objects.equals(entidadCerrada, other.entidadCerrada);
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////
    
    /**
     * @return the idConvocatoriaEntCer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_ENT_CER")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_ENT_CER", sequenceName = "SAII_S_CONVOCATORIA_ENT_CER", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_ENT_CER")
    public Integer getIdConvocatoriaEntCer() {
        return idConvocatoriaEntCer;
    }

    /**
     * @param idConvocatoriaEntCer the idConvocatoriaEntCer to set
     */
    public void setIdConvocatoriaEntCer(Integer idConvocatoriaEntCer) {
        this.idConvocatoriaEntCer = idConvocatoriaEntCer;
    }

    /**
     * @return the convocatoria
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA")
    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    /**
     * @param idConvocatoria the convocatoria to set
     */
    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    /**
     * @return the entidadCerrada
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ENTIDAD_CERRADA")
    public CatalogosValores getEntidadCerrada() {
        return entidadCerrada;
    }

    /**
     * @param entidadCerrada the entidadCerrada to set
     */
    public void setEntidadCerrada(CatalogosValores entidadCerrada) {
        this.entidadCerrada = entidadCerrada;
    }

    /**
     * @return the cdUsuAlta
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return cdUsuAlta;
    }

    /**
     * @param cdUsuAlta the cdUsuAlta to set
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return the fcAltaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return fcAltaFila;
    }

    /**
     * @param fcAltaFila the fcAltaFila to set
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return the cdUsuModif
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return cdUsuModif;
    }

    /**
     * @param cdUsuModif the cdUsuModif to set
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return the fcModifFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return fcModifFila;
    }

    /**
     * @param fcModifFila the fcModifFila to set
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return the cdUsuBaja
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return cdUsuBaja;
    }

    /**
     * @param cdUsuBaja the cdUsuBaja to set
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return the fcBajaFila
     */
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return fcBajaFila;
    }

    /**
     * @param fcBajaFila the fcBajaFila to set
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN AUDITORIA
    // ////////////////////////////////////////////////////////
    
}