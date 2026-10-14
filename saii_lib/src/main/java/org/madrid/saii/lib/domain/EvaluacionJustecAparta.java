package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio Apartado
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EVALUACION_JUSTEC_APARTA")
@AtlasAuditarFila
@ToString
public class EvaluacionJustecAparta implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idApartado del objeto de dominio
     */
    private Integer idEvaluacionJustecAparta;

    /**
     * Representa el campo idEvaluacion del objeto de dominio
     */
    private Integer idEvaluacionJustec;

    /**
     * Representa el campo dsTitulo del objeto de dominio
     */
    private String dsTitulo;

    /**
     * Representa el campo tlDescripcion del objeto de dominio
     */
    private String tlDescripcion;

    /**
     * Representa el campo tlTexto del objeto de dominio
     */
    private String tlTexto;

    /**
     * Representa el campo nmOrden del objeto de dominio
     */
    private Integer nmOrden;

    /**
     * Representa el campo nmCalificacionMax del objeto de dominio
     */
    private Integer nmCalificacionMax;

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
    public EvaluacionJustecAparta() {
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
        return new HashCodeBuilder().append(this.idEvaluacionJustecAparta)
                .append(this.idEvaluacionJustec).append(this.dsTitulo).append(this.tlDescripcion)
                .append(this.tlTexto).append(this.nmOrden).append(this.nmCalificacionMax)
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
        if (!(obj instanceof EvaluacionJustecAparta)) {
            return false;
        }
        EvaluacionJustecAparta castObj = (EvaluacionJustecAparta) obj;
        return new EqualsBuilder()
                .append(this.idEvaluacionJustecAparta, castObj.idEvaluacionJustecAparta)
                .append(this.idEvaluacionJustec, castObj.idEvaluacionJustec)
                .append(this.dsTitulo, castObj.dsTitulo)
                .append(this.tlDescripcion, castObj.tlDescripcion)
                .append(this.tlTexto, castObj.tlTexto).append(this.nmOrden, castObj.nmOrden)
                .append(this.nmCalificacionMax, castObj.nmCalificacionMax)
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
     * @return Devuelve el valor del campo idApartado
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EVALUACION_JUSTAPARTA")
    @SequenceGenerator(name = "SAII_S_EVALUACION_JUSTAPARTA", sequenceName = "SAII_S_EVALUACION_JUSTAPARTA", allocationSize = 1)
    @Column(name = "ID_EVALUACION_JUSTEC_APARTA")
    public Integer getIdEvaluacionJustecAparta() {
        return this.idEvaluacionJustecAparta;
    }

    /**
     * Establece el valor del campo idApartado de la entidad Apartado
     *
     * @param idApartado El valor a establecer
     */
    public void setIdEvaluacionJustecAparta(Integer idEvaluacionJustecAparta) {
        this.idEvaluacionJustecAparta = idEvaluacionJustecAparta;
    }

    /**
     * @return Devuelve el valor del campo idEvaluacion
     */
    @Column(name = "ID_EVALUACION_JUSTEC")
    public Integer getIdEvaluacionJustec() {
        return this.idEvaluacionJustec;
    }

    /**
     * Establece el valor del campo idEvaluacion de la entidad Apartado
     *
     * @param idEvaluacion El valor a establecer
     */
    public void setIdEvaluacionJustec(Integer idEvaluacionJustec) {
        this.idEvaluacionJustec = idEvaluacionJustec;
    }

    /**
     * @return Devuelve el valor del campo dsTitulo
     */
    @Column(name = "DS_TITULO")
    public String getDsTitulo() {
        return this.dsTitulo;
    }

    /**
     * Establece el valor del campo dsTitulo de la entidad Apartado
     *
     * @param dsTitulo El valor a establecer
     */
    public void setDsTitulo(String dsTitulo) {
        this.dsTitulo = dsTitulo;
    }

    /**
     * @return Devuelve el valor del campo tlDescripcion
     */
    @Column(name = "TL_DESCRIPCION")
    public String getTlDescripcion() {
        return this.tlDescripcion;
    }

    /**
     * Establece el valor del campo tlDescripcion de la entidad Apartado
     *
     * @param tlDescripcion El valor a establecer
     */
    public void setTlDescripcion(String tlDescripcion) {
        this.tlDescripcion = tlDescripcion;
    }

    /**
     * @return Devuelve el valor del campo tlTexto
     */
    @Column(name = "TL_TEXTO", nullable = true)
    public String getTlTexto() {
        return this.tlTexto;
    }

    /**
     * Establece el valor del campo tlTexto de la entidad Apartado
     *
     * @param tlTexto El valor a establecer
     */
    public void setTlTexto(String tlTexto) {
        this.tlTexto = tlTexto;
    }

    /**
     * @return Devuelve el valor del campo nmOrden
     */
    @Column(name = "NM_ORDEN")
    public Integer getNmOrden() {
        return this.nmOrden;
    }

    /**
     * Establece el valor del campo nmOrden de la entidad Apartado
     *
     * @param nmOrden El valor a establecer
     */
    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
    }

    /**
     * @return Devuelve el valor del campo nmCalificacionMax
     */
    @Column(name = "NM_CALIFICACION_MAX")
    public Integer getNmCalificacionMax() {
        return this.nmCalificacionMax;
    }

    /**
     * Establece el valor del campo nmCalificacionMax de la entidad Apartado
     *
     * @param nmCalificacionMax El valor a establecer
     */
    public void setNmCalificacionMax(Integer nmCalificacionMax) {
        this.nmCalificacionMax = nmCalificacionMax;
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
