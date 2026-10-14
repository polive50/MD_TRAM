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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio Datos
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_DATO")
@AtlasAuditarFila
@ToString
public class Dato implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idDato del objeto de dominio
     */
    private Integer idDato;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Expediente expediente;

    /**
     * Representa el campo idBloqueComponente del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BloqueComponente bloqueComponente;

    
    private ExpedientePresupMonto expedientePresupMonto; 
    
    
    /**
     * Representa el campo nmFila del objeto de dominio
     */
    private Integer nmFila;

    /**
     * Representa el campo nmColumna del objeto de dominio
     */
    private Integer nmColumna;

    /**
     * Representa el campo dsValor del objeto de dominio
     */
    private String dsValor;

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
     * Representa el campo ExpedienteJustec del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExpedienteJusTec expedienteJustec;
    
    /**
     * Representa el campo ExpedienteModifPresPlazoOtra del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra;
    
    /**
     * Representa el campo ExpedienteJusEco del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ExpedienteJusEco expedienteJusEco;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Dato() {
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
        return new HashCodeBuilder().append(this.idDato).append(this.dsValor).append(this.cdUsuAlta)
                .append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof Dato)) {
            return false;
        }
        Dato castObj = (Dato) obj;
        return new EqualsBuilder().append(this.idDato, castObj.idDato)
                .append(this.dsValor, castObj.dsValor).append(this.cdUsuAlta, castObj.cdUsuAlta)
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
     * @return Devuelve el valor del campo idDato
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_DATO")
    @SequenceGenerator(name = "SAII_S_DATO", sequenceName = "SAII_S_DATO", allocationSize = 1)
    @Column(name = "ID_DATO")
    public Integer getIdDato() {
        return this.idDato;
    }

    /**
     * Establece el valor del campo idDato de la entidad Dato
     *
     * @param idDato El valor a establecer
     */
    public void setIdDato(Integer idDato) {
        this.idDato = idDato;
    }

    /**
     * @return Devuelve el valor del campo dsValor
     */
    @Column(name = "DS_VALOR", nullable = true)
    public String getDsValor() {
        return this.dsValor;
    }

    /**
     * Establece el valor del campo dsValor de la entidad Datos
     *
     * @param dsValor El valor a establecer
     */
    public void setDsValor(String dsValor) {
        this.dsValor = dsValor;
    }

    /**
     * @return the nmFila
     */
    @Column(name = "NM_FILA", nullable = true)
    public Integer getNmFila() {
        return this.nmFila;
    }

    /**
     * @param nmFila the nmFila to set
     */
    public void setNmFila(Integer nmFila) {
        this.nmFila = nmFila;
    }

    /**
     * @return the nmFila
     */
    @Column(name = "NM_COLUMNA", nullable = true)
    public Integer getNmColumna() {
        return this.nmColumna;
    }

    /**
     * @param nmFila the nmFila to set
     */
    public void setNmColumna(Integer nmColumna) {
        this.nmColumna = nmColumna;
    }

    /**
     * @return the BloqueComponente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_BLOQUE_COMPONENTE", nullable = false)
    public BloqueComponente getBloqueComponente() {
        return this.bloqueComponente;
    }

    /**
     * @param BloqueComponente the BloqueComponente to set
     */
    public void setBloqueComponente(BloqueComponente bloqueComponente) {
        this.bloqueComponente = bloqueComponente;
    }

    /**
     * @return the Expediente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    /**
     * @param expediente the expediente to set
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    /**
     * @return the ExpedientePresupMonto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_PRESUP_MONTO", nullable = true)
    public ExpedientePresupMonto getExpedientePresupMonto() {
        return this.expedientePresupMonto;
    }

    /**
     * @param expedientePresupMonto the expedientePresupMonto to set
     */
    public void setExpedientePresupMonto(ExpedientePresupMonto expedientePresupMonto) {
        this.expedientePresupMonto = expedientePresupMonto;
    }
    

    
    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */

    @Column(name = "CD_USU_ALTA", nullable = false, length = 12)
    @AtlasAuditarCampo(tipo = TiposAuditoria.USUALTA)
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Datos
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FC_ALTA_FILA", nullable = false)
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAALTA)
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Datos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */

    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad Datos
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FC_MODIF_FILA", nullable = true)
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad Datos
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */

    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad Datos
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FC_BAJA_FILA", nullable = true)
    @AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad Datos
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_JUSTEC", nullable = true)
    public ExpedienteJusTec getExpedienteJustec() {
        return expedienteJustec;
    }

    public void setExpedienteJustec(ExpedienteJusTec expedienteJustec) {
        this.expedienteJustec = expedienteJustec;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPE_MOD_PRE_PLA_OTRA", nullable = true)
    public ExpedienteModifPresPlazoOtra getExpedienteModifPresPlazoOtra() {
        return expedienteModifPresPlazoOtra;
    }

    public void setExpedienteModifPresPlazoOtra(
            ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra) {
        this.expedienteModifPresPlazoOtra = expedienteModifPresPlazoOtra;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE_JUSECO", nullable = true)
    public ExpedienteJusEco getExpedienteJusEco() {
        return expedienteJusEco;
    }

    public void setExpedienteJusEco(ExpedienteJusEco expedienteJusEco) {
        this.expedienteJusEco = expedienteJusEco;
    }
    
}
