package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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

import lombok.ToString;

/**
 * Representa el objeto de dominio Solicitud
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_SOLICITUD")
@ToString
public class Solicitud implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idSolicitud del objeto de dominio
     */
    private Integer idSolicitud;

    /**
     * Representa el campo idInteresado del objeto de dominio
     */
    private Interesado interesado;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Convocatoria convocatoria;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo tipoActor del objeto de dominio
     */
    private CatalogosValores tipoActor;

    /**
     * Representa el campo dsTipoOrganismo del objeto de dominio
     */
    private String dsTipoOrganismo;

    /**
     * Representa el campo cdNote del objeto de dominio
     */
    private String cdNote;

    /**
     * Representa el campo cdExpedienteSol del objeto de dominio
     */
    private String cdExpedienteSol;

    /**
     * Representa el campo convocatoriaSubmod del objeto de dominio
     */
    private ConvocatoriaSubmod convocatoriaSubmod;

    /**
     * Representa el campo convocatoriaArea del objeto de dominio
     */
    private ConvocatoriaArea convocatoriaArea;

    /**
     * Representa el indicador si la solicitud tiene un expediente coordinador
     */
    private String itExpeCoor;

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
    
    private ConvocatoriaAreaSubarea convocatoriaAreaSubarea;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Solicitud() {
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
        return new HashCodeBuilder().append(this.idSolicitud).toHashCode();
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
        if (!(obj instanceof Solicitud)) {
            return false;
        }
        Solicitud castObj = (Solicitud) obj;
        return new EqualsBuilder().append(this.idSolicitud, castObj.idSolicitud).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idSolicitud
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_SOLICITUD")
    @SequenceGenerator(name = "SAII_S_SOLICITUD", sequenceName = "SAII_S_SOLICITUD", allocationSize = 1)
    @Column(name = "ID_SOLICITUD")
    public Integer getIdSolicitud() {
        return this.idSolicitud;
    }

    /**
     * Establece el valor del campo idSolicitud de la entidad Solicitud
     *
     * @param idSolicitud El valor a establecer
     */
    public void setIdSolicitud(Integer idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    public Interesado getInteresado() {
        return this.interesado;
    }

    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA", nullable = false)
    public Convocatoria getConvocatoria() {
        return this.convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    /**
     * @return Devuelve el valor del campo idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return the tipoActor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_ACTOR")
    public CatalogosValores getTipoActor() {
        return this.tipoActor;
    }

    /**
     * @param tipoActor the tipoActor to set
     */
    public void setTipoActor(CatalogosValores tipoActor) {
        this.tipoActor = tipoActor;
    }

    /**
     * @return the dsTipoOrganismo
     */
    @Column(name = "DS_TIPO_ORGANISMO", nullable = true, length = 400)
    public String getDsTipoOrganismo() {
        return this.dsTipoOrganismo;
    }

    /**
     * @param dsTipoOrganismo the dsTipoOrganismo to set
     */
    public void setDsTipoOrganismo(String dsTipoOrganismo) {
        this.dsTipoOrganismo = dsTipoOrganismo;
    }

    /**
     * @return the cdNote
     */
    @Column(name = "CD_NOTE", nullable = true)
    public String getCdNote() {
        return this.cdNote;
    }

    /**
     * @param cdNote the cdNote to set
     */
    public void setCdNote(String cdNote) {
        this.cdNote = cdNote;
    }

    /**
     * @return the cdExpedienteSol
     */
    @Column(name = "CD_EXPEDIENTE_SOL", nullable = true)
    public String getCdExpedienteSol() {
        return this.cdExpedienteSol;
    }

    /**
     * @param cdExpedienteSol the cdExpedienteSol to set
     */
    public void setCdExpedienteSol(String cdExpedienteSol) {
        this.cdExpedienteSol = cdExpedienteSol;
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_AREA", nullable = true)
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

    /**
     * @return Devuelve el valor del campo itExpeCoor
     */

    @Column(name = "IT_EXPE_COOR")
    public String getItExpeCoor() {
        return this.itExpeCoor;
    }

    /**
     * Establece el valor del campo itExpeCoor de la entidad Interesado
     *
     * @param itExpeCoor El valor a establecer
     */
    public void setItExpeCoor(String itExpeCoor) {
        this.itExpeCoor = itExpeCoor;
    }

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */

    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad Interesado
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */

    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Interesado
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
     * Establece el valor del campo cdUsuModif de la entidad Interesado
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */

    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad Interesado
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
     * Establece el valor del campo cdUsuBaja de la entidad Interesado
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */

    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad Interesado
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_AREA_SUBAREA", nullable = true)
    public ConvocatoriaAreaSubarea getConvocatoriaAreaSubarea() {
        return convocatoriaAreaSubarea;
    }

    public void setConvocatoriaAreaSubarea(ConvocatoriaAreaSubarea convocatoriaAreaSubarea) {
        this.convocatoriaAreaSubarea = convocatoriaAreaSubarea;
    }
    
    

}
