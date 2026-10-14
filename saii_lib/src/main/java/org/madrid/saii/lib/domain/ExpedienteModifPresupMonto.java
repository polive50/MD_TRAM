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
 * Representa el objeto de dominio ExpedienteModifPresupMonto
 *
 * @author quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPE_MOD_PRE_MONTO")
@AtlasAuditarFila
@ToString
public class ExpedienteModifPresupMonto implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteModifPresupMonto del objeto de dominio
     */
    private Integer idExpedienteModifPresupMonto;
    
    /**
     * Representa el campo idExpedienteModifPresPlazoOtra del objeto de dominio
     */
    private Integer idExpedienteModifPresPlazoOtra;
    
    /**
     * Representa el campo idExpedientePresupMonto del objeto de dominio
     */
    private Integer idExpedientePresupMonto;

    /**
     * Representa el campo idExpedientePresup del objeto de dominio
     */
    private Integer idExpedientePresup;

    /**
     * VMM 11/09/26 MD-2725: representa el campo idTipoFinanciacion del objeto de dominio.
     * Se rellena a partir del bloque_componente (saii_dato.id_bloque_componente) del que
     * viene el dato, para poder saber a que partida pertenece un concepto nuevo (dado de
     * alta directamente en saii_solicitud) sin tener que crear todavia su ExpedientePresup -
     * eso lo hace el batch al resolver la modificacion.
     */
    private CatalogosValores tipoFinanciacion;

    /**
     * Representa el campo nmAnualidad del objeto de dominio
     */
    private Integer nmAnualidad;

    /**
     * Representa el campo nmImporte del objeto de dominio
     */
    private Double nmImporte;
    
    /**
     * Representa el campo nmImporteConcedido del objeto de dominio
     */
    private Double nmImporteConcedido;
    
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
    private String checkModifMonto;
    
    
    private String cdObservaciones;
    
    private String tlConcepto;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteModifPresupMonto() {
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
        return new HashCodeBuilder().append(this.idExpedienteModifPresupMonto)
                .append(this.idExpedienteModifPresPlazoOtra).append(this.idExpedientePresupMonto)
                .append(this.idExpedientePresup).append(this.nmAnualidad).append(this.nmImporte)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila).append(this.cdObservaciones)
                //.append(this.nmImporteConcedido).append(this.nmImporteJustificado)
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
        if (!(obj instanceof ExpedienteModifPresupMonto)) {
            return false;
        }
        ExpedienteModifPresupMonto castObj = (ExpedienteModifPresupMonto) obj;
        return new EqualsBuilder().append(this.idExpedienteModifPresupMonto, castObj.idExpedienteModifPresupMonto)
                .append(this.idExpedienteModifPresPlazoOtra, castObj.idExpedienteModifPresPlazoOtra)
                .append(this.idExpedientePresupMonto, castObj.idExpedientePresupMonto)
                .append(this.idExpedientePresup, castObj.idExpedientePresup)
                .append(this.nmAnualidad, castObj.nmAnualidad)
                .append(this.nmImporte, castObj.nmImporte)
                .append(this.nmImporteConcedido, castObj.nmImporteConcedido)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.cdObservaciones, castObj.cdObservaciones)
                .append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedientePresupMonto
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXP_MODIF_PRESUP_MONTO")
    @SequenceGenerator(name = "SAII_S_EXP_MODIF_PRESUP_MONTO", sequenceName = "SAII_S_EXP_MODIF_PRESUP_MONTO", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_MODIF_PRESUP_MON")
    public Integer getIdExpedienteModifPresupMonto() {
        return this.idExpedienteModifPresupMonto;
    }

    /**
     * Establece el valor del campo idExpedienteModifPresupMonto de la entidad
     * ExpedienteModifPresupMonto
     *
     * @param idExpedienteModifPresupMonto El valor a establecer
     */
    public void setIdExpedienteModifPresupMonto(Integer idExpedienteModifPresupMonto) {
        this.idExpedienteModifPresupMonto = idExpedienteModifPresupMonto;
    }
    
    @Column(name = "ID_EXPE_MOD_PRE_PLA_OTRA")
    public Integer getIdExpedienteModifPresPlazoOtra() {
        return this.idExpedienteModifPresPlazoOtra;
    }

    /**
     * Establece el valor del campo idExpedienteModifPresPlazoOtra de la entidad
     * ExpedienteModifPresupMonto
     *
     * @param idExpedienteModifPresPlazoOtra El valor a establecer
     */
    public void setIdExpedienteModifPresPlazoOtra(Integer idExpedienteModifPresPlazoOtra) {
        this.idExpedienteModifPresPlazoOtra = idExpedienteModifPresPlazoOtra;
    }
    
    @Column(name = "ID_EXPEDIENTE_PRESUP_MONTO")
    public Integer getIdExpedientePresupMonto() {
        return this.idExpedientePresupMonto;
    }

    /**
     * Establece el valor del campo idExpedientePresupMonto de la entidad
     * ExpedienteModifPresupMonto
     *
     * @param idExpedientePresupMonto El valor a establecer
     */
    public void setIdExpedientePresupMonto(Integer idExpedientePresupMonto) {
        this.idExpedientePresupMonto = idExpedientePresupMonto;
    }

    /**
     * @return Devuelve el valor del campo idExpedientePresup
     */
    @Column(name = "ID_EXPEDIENTE_PRESUP")
    public Integer getIdExpedientePresup() {
        return this.idExpedientePresup;
    }

    /**
     * Establece el valor del campo idExpedientePresup de la entidad
     * ExpedienteModifPresupMonto
     *
     * @param idExpedientePresup
     */
    public void setIdExpedientePresup(Integer idExpedientePresup) {
        this.idExpedientePresup = idExpedientePresup;
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
     * Establece el valor del campo tipoFinanciacion de la entidad
     * ExpedienteModifPresupMonto
     *
     * @param tipoFinanciacion El valor a establecer
     */
    public void setTipoFinanciacion(CatalogosValores tipoFinanciacion) {
        this.tipoFinanciacion = tipoFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo nmAnualidad
     */
    @Column(name = "NM_ANUALIDAD")
    public Integer getNmAnualidad() {
        return this.nmAnualidad;
    }

    /**
     * Establece el valor del campo nmAnualidad de la entidad 
     * ExpedienteModifPresupMonto
     *
     * @param nmAnualidad El valor a establecer
     */
    public void setNmAnualidad(Integer nmAnualidad) {
        this.nmAnualidad = nmAnualidad;
    }

    /**
     * @return Devuelve el valor del campo nmImporte
     */
    @Column(name = "NM_IMPORTE")
    public Double getNmImporte() {
        return this.nmImporte;
    }
    
    /**
     * Establece el valor del campo nmImporte de la entidad 
     * ExpedienteModifPresupMonto
     *
     * @param nmImporte El valor a establecer
     */
    public void setNmImporte(Double nmImporte) {
        this.nmImporte = nmImporte;
    }

    /**
     * Establece el valor del campo nmImporteConcedido de la entidad 
     * ExpedienteModifPresupMonto
     *
     * @param nmImporteConcedido El valor a establecer
     */
    public void setNmImporteConcedido(Double nmImporteConcedido) {
        this.nmImporteConcedido = nmImporteConcedido;
    }
    
    /**
     * @return Devuelve el valor del campo nmImporteConcedido
     */
    @Column(name = "NM_IMPORTE_CONCEDIDO")
    public Double getNmImporteConcedido() {
        return this.nmImporteConcedido;
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
    

    @Column(name = "CD_OBSERVACIONES")
    public String getCdObservaciones() {
        return this.cdObservaciones;
    }

    public void setCdObservaciones(String cdObservaciones) {
        this.cdObservaciones = cdObservaciones;
    }
    
    
    @Column(name = "CD_CHECK_MODIF_MONTO")
    public String getCheckModifMonto() {
        return this.checkModifMonto;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Catalogos
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCheckModifMonto(String checkModifMonto) {
        this.checkModifMonto = checkModifMonto;
    }

    @Column(name = "TL_CONCEPTO")
    public String getTlConcepto() {
        return tlConcepto;
    }

    public void setTlConcepto(String tlConcepto) {
        this.tlConcepto = tlConcepto;
    }
    
    
}
