package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarFila;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import lombok.ToString;

/**
 * Representa el objeto de dominio Informe
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_INFORME")
@AtlasAuditarFila
@ToString
public class Informe implements Serializable {
    
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Representa el campo idInforme del objeto de dominio
     */
    private Integer idInforme;
    
    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;
    
    /**
     * Representa el campo dsTitulo del objeto de dominio
     */
    private String dsTitulo;
    
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
     * Representa el campo idTipoInforme del objeto de dominio
     */
    private CatalogosValores tipoInforme;

    /* 
     * Representa el id de la imagen relacionada a la evaluación
     */
    private Imagen imagenLogo;
    
    /* 
     * Representa el id de la imagen2 relacionada a la evaluación 
     */
    private Imagen imagenLogo2;
    
    private List<Evaluacion> listaEvaluacion;

    /**
     * Constructor sin argumentos 
     */
    public Informe() {
        super();
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdUsuAlta, cdUsuBaja, cdUsuModif, dsTitulo, fcAltaFila, fcBajaFila,
                fcModifFila, idConvocatoria, imagenLogo, imagenLogo2, idInforme, tipoInforme);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Informe)) {
            return false;
        }
        Informe other = (Informe) obj;
        return Objects.equals(cdUsuAlta, other.cdUsuAlta)
                && Objects.equals(cdUsuBaja, other.cdUsuBaja)
                && Objects.equals(cdUsuModif, other.cdUsuModif)
                && Objects.equals(dsTitulo, other.dsTitulo)
                && Objects.equals(fcAltaFila, other.fcAltaFila)
                && Objects.equals(fcBajaFila, other.fcBajaFila)
                && Objects.equals(fcModifFila, other.fcModifFila)
                && Objects.equals(idConvocatoria, other.idConvocatoria)
                && Objects.equals(imagenLogo, other.imagenLogo)
                && Objects.equals(imagenLogo2, other.imagenLogo2)
                && Objects.equals(idInforme, other.idInforme)
                && Objects.equals(tipoInforme, other.tipoInforme);
    }

    /**
     * @return the idInforme
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_INFORME")
    @SequenceGenerator(name = "SAII_S_INFORME", sequenceName = "SAII_S_INFORME", allocationSize = 1)
    @Column(name = "ID_INFORME")
    public Integer getIdInforme() {
        return idInforme;
    }

    /**
     * @param idInforme the idInforme to set
     */
    public void setIdInforme(Integer idInforme) {
        this.idInforme = idInforme;
    }

    /**
     * @return the idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return idConvocatoria;
    }

    /**
     * @param idConvocatoria the idConvocatoria to set
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return the dsTitulo
     */
    @Column(name = "DS_TITULO")
    public String getDsTitulo() {
        return dsTitulo;
    }

    /**
     * @param dsTitulo the dsTitulo to set
     */
    public void setDsTitulo(String dsTitulo) {
        this.dsTitulo = dsTitulo;
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

    /**
     * @return the tipoInforme
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_INFORME", nullable = true)
    public CatalogosValores getTipoInforme() {
        return tipoInforme;
    }

    /**
     * @param tipoInforme the tipoInforme to set
     */
    public void setTipoInforme(CatalogosValores tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    /**
     * @return the imagenLogo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IMAGEN_LOGO", nullable = true)
    public Imagen getImagenLogo() {
        return imagenLogo;
    }

    /**
     * @param imagenLogo the imagenLogo to set
     */
    public void setImagenLogo(Imagen imagenLogo) {
        this.imagenLogo = imagenLogo;
    }

    /**
     * @return the imagenLogo2
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IMAGEN_LOGO2", nullable = true)
    public Imagen getImagenLogo2() {
        return imagenLogo2;
    }

    /**
     * @param imagenLogo2 the imagenLogo2 to set
     */
    public void setImagenLogo2(Imagen imagenLogo2) {
        this.imagenLogo2 = imagenLogo2;
    }

    /**
     * @return the listaEvaluacion
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "informe", cascade = CascadeType.ALL)
    public List<Evaluacion> getListaEvaluacion() {
        return listaEvaluacion;
    }

    /**
     * @param listaEvaluacion the listaEvaluacion to set
     */
    public void setListaEvaluacion(List<Evaluacion> listaEvaluacion) {
        this.listaEvaluacion = listaEvaluacion;
    }
    
    
    
}
