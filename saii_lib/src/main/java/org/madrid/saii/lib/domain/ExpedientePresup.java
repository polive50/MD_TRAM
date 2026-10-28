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
 * Representa el objeto de dominio ExpedientePresup
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_PRESUP")
@AtlasAuditarFila
@ToString
public class ExpedientePresup implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedientePresup del objeto de dominio
     */
    private Integer idExpedientePresup;

    /**
     * Representa el campo expediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo tipoFinanciacion del objeto de dominio
     */
    private CatalogosValores tipoFinanciacion;

    /**
     *
     */
    private CatalogosValores tematica;

    /**
     * Representa el campo tlObservacion del objeto de dominio
     */
    private String tlObservacion;

    /**
     * Representa el campo nmAyuda del objeto de dominio
     */
    private Double nmAyuda;

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
     * Representa el campo nmPresup del objeto de dominio
     */
    private Integer nmPresup;

    /**
     * Variable que representa la lista de montos
     */
    private List<ExpedientePresupMonto> lstExpedientePresupMonto;
    
    /**
     * Variable que representa la lista de montos historico
     */
    private List<ExpedientePresMontHist> lstExpedientePresMontHist;
    
    /**
     * Variable que representa la lista de montos temporales
     */
    private List<ExpedienteModifPresupMonto> lstExpedienteModifPresupMonto;
    
    /**
     * Representa el campo tlConcepto del objeto de dominio
     */
    private String tlConcepto;
    
    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;
    
    /**
     * Representa el indicador de nuevo concepto de financiacion
     */
    private String itNuevoConceptoFinan;

    /**
     * Representa el porcentaje de intensidad de ayuda aplicado al presupuesto
     */
    private Double nmPorcentajeIntensidad;

    /**
     * VMM 27/10/26 MD-3094: ayuda solicitada por el solicitante (NM_AYUDA guarda la ayuda concedida)
     */
    private Double nmAyudaSol;


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedientePresup() {
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
        return new HashCodeBuilder().append(this.idExpedientePresup).append(this.tlObservacion)
                .append(this.nmAyuda).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ExpedientePresup)) {
            return false;
        }
        ExpedientePresup castObj = (ExpedientePresup) obj;
        return new EqualsBuilder().append(this.idExpedientePresup, castObj.idExpedientePresup)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.tlObservacion, castObj.tlObservacion)
                .append(this.tlConcepto, castObj.tlConcepto)
                .append(this.nmAyuda, castObj.nmAyuda).append(this.cdUsuAlta, castObj.cdUsuAlta)
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
     * @return Devuelve el valor del campo idExpedientePresup
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_PRESUP")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_PRESUP", sequenceName = "SAII_S_EXPEDIENTE_PRESUP", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_PRESUP")
    public Integer getIdExpedientePresup() {
        return this.idExpedientePresup;
    }

    /**
     * Establece el valor del campo idExpedientePresup de la entidad
     * ExpedienteFin
     *
     * @param idExpedientePresup El valor a establecer
     */
    public void setIdExpedientePresup(Integer idExpedientePresup) {
        this.idExpedientePresup = idExpedientePresup;
    }

    /**
     * @return Devuelve el valor del campo tlObservacion
     */
    @Column(name = "TL_OBSERVACION", nullable = true)
    public String getTlObservacion() {
        return this.tlObservacion;
    }

    /**
     * Establece el valor del campo tlObservacion de la entidad ExpedienteFin
     *
     * @param tlObservacion El valor a establecer
     */
    public void setTlObservacion(String dsAyuda) {
        this.tlObservacion = dsAyuda;
    }

    /**
     * @return Devuelve el valor del campo nmAyuda
     */
    @Column(name = "NM_AYUDA")
    public Double getNmAyuda() {
        return this.nmAyuda;
    }

    /**
     * Establece el valor del campo nmAyuda de la entidad ExpedienteFin
     *
     * @param nmAyuda El valor a establecer
     */
    public void setNmAyuda(Double nmAyuda) {
        this.nmAyuda = nmAyuda;
    }

    /**
     * @return Devuelve el valor del campo expediente
     */
    @Column(name = "ID_EXPEDIENTE")
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo expediente de la entidad ExpedienteFin
     *
     * @param expediente El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
    }

    /**
     * @return Devuelve el valor del campo tipoFinanciacion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FINANCIACION")
    public CatalogosValores getTipoFinanciacion() {
        return this.tipoFinanciacion;
    }

    /**
     * Establece el valor del campo tipoFinanciacion de la entidad ExpedienteFin
     *
     * @param tipoFinanciacion El valor a establecer
     */
    public void setTipoFinanciacion(CatalogosValores tipo) {
        this.tipoFinanciacion = tipo;
    }

    /**
     * @return Devuelve el valor del campo tipoFinanciacion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEMATICA")
    public CatalogosValores getTematica() {
        return this.tematica;
    }

    /**
     * Establece el valor del campo tipoFinanciacion de la entidad ExpedienteFin
     *
     * @param tipoFinanciacion El valor a establecer
     */
    public void setTematica(CatalogosValores tematica) {
        this.tematica = tematica;
    }

    /**
     * @return Devuelve el valor del campo nmPresup
     */
    @Column(name = "NM_PRESUP")
    public Integer getNmPresup() {
        return this.nmPresup;
    }

    /**
     * Establece el valor del campo nmPresup de la entidad
     *
     * @param expediente El valor a establecer
     */
    public void setNmPresup(Integer nmPresup) {
        this.nmPresup = nmPresup;
    }
    
    /**
     * @return Devuelve el valor del campo tlConcepto
     */
    @Column(name = "TL_CONCEPTO", nullable = true)
    public String getTlConcepto() {
        return this.tlConcepto;
    }

    /**
     * Establece el valor del campo tlConcepto
     *
     * @param tlObservacion El valor a establecer
     */
    public void setTlConcepto(String tlConcepto) {
        this.tlConcepto = tlConcepto;
    }

    /**
     *
     * @return
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedientePresup", cascade = CascadeType.ALL)
    public List<ExpedientePresupMonto> getLstExpedientePresupMonto() {
        return this.lstExpedientePresupMonto;
    }

    /**
     *
     * @param lstExpedientePresupMonto
     */
    public void setLstExpedientePresupMonto(List<ExpedientePresupMonto> lstExpedientePresupMonto) {
        this.lstExpedientePresupMonto = lstExpedientePresupMonto;
    }
    
    
    /**
    *
    * @return
    */
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedientePresup", cascade = CascadeType.ALL)
   public List<ExpedientePresMontHist> getLstExpedientePresMontHist() {
       return this.lstExpedientePresMontHist;
   }

   /**
    *
    * @param lstExpedientePresMontHist
    */
   public void setLstExpedientePresMontHist(List<ExpedientePresMontHist> lstExpedientePresMontHist) {
       this.lstExpedientePresMontHist = lstExpedientePresMontHist;
   }
   
   
   /**
   *
   * @return
   */
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "idExpedientePresup", cascade = CascadeType.ALL)
   public List<ExpedienteModifPresupMonto> getLstExpedienteModifPresupMonto() {
      return this.lstExpedienteModifPresupMonto;
   }

  /**
   *
   * @param lstExpedienteModifPresupMonto
   */
   public void setLstExpedienteModifPresupMonto(List<ExpedienteModifPresupMonto> lstExpedienteModifPresupMonto) {
      this.lstExpedienteModifPresupMonto = lstExpedienteModifPresupMonto;
   }
   
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "ID_ESTADO", nullable = true)
   public CatalogosValores getEstado() {
       return estado;
   }

   public void setEstado(CatalogosValores estado) {
       this.estado = estado;
   }
   
   @Column(name = "IT_NUEVO_CONCEPTO")
   public String getItNuevoConceptoFinan() {
       return itNuevoConceptoFinan;
   }

   public void setItNuevoConceptoFinan(String itNuevoConceptoFinan) {
       this.itNuevoConceptoFinan = itNuevoConceptoFinan;
   }

   @Column(name = "NM_PORCENTAJE_INTENSIDAD", nullable = true)
   public Double getNmPorcentajeIntensidad() {
       return nmPorcentajeIntensidad;
   }

   public void setNmPorcentajeIntensidad(Double nmPorcentajeIntensidad) {
       this.nmPorcentajeIntensidad = nmPorcentajeIntensidad;
   }

   @Column(name = "NM_AYUDA_SOL", nullable = true)
   public Double getNmAyudaSol() {
       return nmAyudaSol;
   }

   public void setNmAyudaSol(Double nmAyudaSol) {
       this.nmAyudaSol = nmAyudaSol;
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
