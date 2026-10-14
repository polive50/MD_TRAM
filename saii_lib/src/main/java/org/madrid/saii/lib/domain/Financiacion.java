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
 * Representa el objeto de dominio Financiación
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_FINANCIACION")
@AtlasAuditarFila
@ToString
public class Financiacion implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idFinanciacion del objeto de dominio
     */
    private Integer idFinanciacion;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idTematica del objeto de dominio
     */
    private CatalogosValores tematica;

    /**
     * Representa el campo ittipo del objeto de dominio
     */
    private String itTipo;

    /**
     * Representa el campo listaFinanciacionDato del objeto de dominio
     */
    private List<FinanciacionDato> listaFinanciacionDato;

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
     * Representa el campo ittipo del objeto de dominio
     */
    private String itMostrarAyuda;
    
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    
    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private ConvocatoriaAct idActor;
    
    /**
     * Representa el campo nmImporteMaxActor del objeto de dominio
     */
    private Double nmImporteMaxActor;
    
    /**
     * Representa el campo nmImporteMinActor del objeto de dominio
     */
    private Double nmImporteMinActor;
    
    /**
     * Constructor sin argumentos
     */
    public Financiacion() {
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
        return new HashCodeBuilder().append(this.idFinanciacion).append(this.idConvocatoria)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila)
                .toHashCode();
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
        if (!(obj instanceof Financiacion)) {
            return false;
        }
        Financiacion castObj = (Financiacion) obj;
        return new EqualsBuilder().append(this.idFinanciacion, castObj.idFinanciacion)
                .append(this.idConvocatoria, castObj.idConvocatoria)
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
     * @return Devuelve el valor del campo idFinanciacion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_FINANCIACION")
    @SequenceGenerator(name = "SAII_S_FINANCIACION", sequenceName = "SAII_S_FINANCIACION", allocationSize = 1)
    @Column(name = "ID_FINANCIACION")
    public Integer getIdFinanciacion() {
        return this.idFinanciacion;
    }

    /**
     * Establece el valor del campo idFinanciacion de la entidad Financiacion
     *
     * @param idFinanciacion El valor a establecer
     */
    public void setIdFinanciacion(Integer idFinanciacion) {
        this.idFinanciacion = idFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad Financiacion
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo idTematica
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEMATICA", nullable = true)
    public CatalogosValores getTematica() {
        return this.tematica;
    }

    /**
     * Establece el valor del campo idTematica de la entidad Financiacion
     *
     * @param idTematica El valor a establecer
     */
    public void setTematica(CatalogosValores tematica) {
        this.tematica = tematica;
    }


    
    /**
     * @return Devuelve el valor del campo Actor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACTOR")
    public ConvocatoriaAct getIdActor() {
        return this.idActor;
    }

    /**
     * Establece el valor del campo idTipoActorFisjur de la entidad Bloque
     *
     * @param idTipoActorFisjur El valor a establecer
     */
    public void setIdActor(ConvocatoriaAct idActor) {
        this.idActor = idActor;
    }
    
    
   
    /**
     * @return Devuelve el valor del campo itTipo
     */
    @Column(name = "IT_TIPO")
    public String getItTipo() {
        return this.itTipo;
    }

    /**
     * Establece el valor del campo itTipo
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setItTipo(String itTipo) {
        this.itTipo = itTipo;
    }
    

    /**
     * @return Devuelve el valor del campo itTipo
     */
    @Column(name = "IT_MOSTRAR_AYUDA")
    public String getItMostrarAyuda() {
        return this.itMostrarAyuda;
    }

    /**
     * Establece el valor del campo itTipo
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setItMostrarAyuda(String itMostrarAyuda) {
        this.itMostrarAyuda = itMostrarAyuda;
    }
    
    
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// INICIO LISTAS
    // ////////////////////////////////////////////////////////

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idFinanciacion", cascade = CascadeType.ALL)
    public List<FinanciacionDato> getListaFinanciacionDato() {
        return this.listaFinanciacionDato;
    }

    public void setListaFinanciacionDato(List<FinanciacionDato> listaFinanciacionDato) {
        this.listaFinanciacionDato = listaFinanciacionDato;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN LISTAS
    // ////////////////////////////////////////////////////////

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
    
    /**
     * @return Devuelve el valor del campo nmImporteMaxActor
     */
    @Column(name = "NM_IMPORTE_MAX_ACTOR")
    public Double getNmImporteMaxActor() {
        return nmImporteMaxActor;
    }

    public void setNmImporteMaxActor(Double nmImporteMaxActor) {
        this.nmImporteMaxActor = nmImporteMaxActor;
    }

    /**
     * @return the nmImporteMinActor
     * Importe mínimo por actor
     */
    @Column(name = "NM_IMPORTE_MIN_ACTOR")
    public Double getNmImporteMinActor() {
        return nmImporteMinActor;
    }

    /**
     * @param nmImporteMinActor the nmImporteMinActor to set
     */
    public void setNmImporteMinActor(Double nmImporteMinActor) {
        this.nmImporteMinActor = nmImporteMinActor;
    }
    
    
    
}
