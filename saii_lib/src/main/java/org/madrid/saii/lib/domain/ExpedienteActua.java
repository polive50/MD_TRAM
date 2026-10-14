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
 * Representa el objeto de dominio ExpedienteActua
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_ACTUA")
@AtlasAuditarFila
@ToString
public class ExpedienteActua implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteActua del objeto de dominio
     */
    private Integer idExpedienteActua;

    /**
     * Representa el campo expediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo tipoFinanciacion del objeto de dominio
     */
    private CatalogosValores tipoFinanciacion;

    /**
     * Representa el campo dsProveedor del objeto de dominio
     */
    private String dsProveedor;

    /**
     * Representa el campo nmImporte del objeto de dominio
     */
    private Double nmImporte;

    /**
     * Representa el campo tlObservacion del objeto de dominio
     */
    private String tlObservacion;

    /**
     * Representa el campo itMemoria del objeto de dominio
     */
    // private String itMemoria;

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
    private Double nmAyuda;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteActua() {
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
        return new HashCodeBuilder().append(this.idExpedienteActua).append(this.idExpediente)
                .append(this.tipoFinanciacion).append(this.dsProveedor).append(this.nmImporte)
                .append(this.tlObservacion).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja)
                .append(this.fcBajaFila).append(this.nmAyuda).toHashCode();
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
        if (!(obj instanceof ExpedienteActua)) {
            return false;
        }
        ExpedienteActua castObj = (ExpedienteActua) obj;
        return new EqualsBuilder().append(this.idExpedienteActua, castObj.idExpedienteActua)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.tipoFinanciacion, castObj.tipoFinanciacion)
                .append(this.dsProveedor, castObj.dsProveedor)
                .append(this.nmImporte, castObj.nmImporte)
                .append(this.tlObservacion, castObj.tlObservacion)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).append(this.nmAyuda, castObj.nmAyuda)
                .isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteActua
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_ACTUA")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_ACTUA", sequenceName = "SAII_S_EXPEDIENTE_ACTUA", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_ACTUA")
    public Integer getIdExpedienteActua() {
        return this.idExpedienteActua;
    }

    /**
     * Establece el valor del campo idExpedienteActua de la entidad
     * ExpedienteActua
     *
     * @param idExpedienteActua El valor a establecer
     */
    public void setIdExpedienteActua(Integer idExpedienteActua) {
        this.idExpedienteActua = idExpedienteActua;
    }

    /**
     * @return Devuelve el valor del campo id expediente
     */
    @Column(name = "ID_EXPEDIENTE")
    public Integer getidExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo expediente de la entidad ExpedienteActua
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
     * Establece el valor del campo tipoFinanciacion de la entidad
     * ExpedienteActua
     *
     * @param tipoFinanciacion El valor a establecer
     */
    public void setTipoFinanciacion(CatalogosValores tipoFinanciacion) {
        this.tipoFinanciacion = tipoFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo dsProveedor
     */
    @Column(name = "DS_PROVEEDOR")
    public String getDsProveedor() {
        return this.dsProveedor;
    }

    /**
     * Establece el valor del campo dsProveedor de la entidad ExpedienteActua
     *
     * @param dsProveedor El valor a establecer
     */
    public void setDsProveedor(String dsProveedor) {
        this.dsProveedor = dsProveedor;
    }

    /**
     * @return Devuelve el valor del campo nmImporte
     */
    @Column(name = "NM_IMPORTE")
    public Double getNmImporte() {
        return this.nmImporte;
    }

    /**
     * Establece el valor del campo nmImporte de la entidad ExpedienteActua
     *
     * @param nmImporte El valor a establecer
     */
    public void setNmImporte(Double nmImporte) {
        this.nmImporte = nmImporte;
    }

    /**
     * @return Devuelve el valor del campo tlObservacion
     */
    @Column(name = "TL_OBSERVACION", nullable = true)
    public String getTlObservacion() {
        return this.tlObservacion;
    }

    /**
     * Establece el valor del campo tlObservacion de la entidad ExpedienteActua
     *
     * @param tlObservacion El valor a establecer
     */
    public void setTlObservacion(String tlObservacion) {
        this.tlObservacion = tlObservacion;
    }

    /**
     * @return Devuelve el valor del campo itMemoria
     */
//    @Column(name = "IT_MEMORIA", nullable = true)
//    public String getItMemoria() {
//        return this.itMemoria;
//    }

    /**
     * Establece el valor del campo itMemoria de la entidad ExpedienteActua
     *
     * @param itMemoria El valor a establecer
     */
//    public void setItMemoria(String itMemoria) {
//        this.itMemoria = itMemoria;
//    }

    /**
     * @return Devuelve el valor del campo nmAyuda
     */
    @Column(name = "NM_AYUDA")
    public Double getNmAyuda() {
        return this.nmAyuda;
    }

    /**
     * Establece el valor del campo nmAyuda de la entidad ExpedienteActua
     *
     * @param nmAyuda El valor a establecer
     */
    public void setNmAyuda(Double nmAyuda) {
        this.nmAyuda = nmAyuda;
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
