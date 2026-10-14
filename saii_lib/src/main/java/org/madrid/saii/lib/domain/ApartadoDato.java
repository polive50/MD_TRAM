package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ApartadoDato
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_APARTADO_DATO")
@AtlasAuditarFila
@ToString
public class ApartadoDato implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idApartadoDato del objeto de dominio
     */
    private Integer idApartadoDato;

    /**
     * Representa el campo idExpedienteEval del objeto de dominio
     */
    private Integer idExpedienteEval;

    /**
     * Representa el campo idApartado del objeto de dominio
     */
    private Integer idApartado;

    /**
     * Representa el campo dsComentario del objeto de dominio
     */
    private String dsComentario;

    /**
     * Representa el campo nmNota del objeto de dominio
     */
    private Integer nmNota;

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
    public ApartadoDato() {
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
        return new HashCodeBuilder().append(this.idApartadoDato).append(this.idExpedienteEval)
                .append(this.idApartado).append(this.dsComentario).append(this.nmNota)
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
        if (!(obj instanceof ApartadoDato)) {
            return false;
        }
        ApartadoDato castObj = (ApartadoDato) obj;
        return new EqualsBuilder().append(this.idApartadoDato, castObj.idApartadoDato)
                .append(this.idExpedienteEval, castObj.idExpedienteEval)
                .append(this.idApartado, castObj.idApartado)
                .append(this.dsComentario, this.dsComentario).append(this.nmNota, castObj.nmNota)
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
     * @return Devuelve el valor del campo idApartadoDato
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_APARTADO_DATO")
    @SequenceGenerator(name = "SAII_S_APARTADO_DATO", sequenceName = "SAII_S_APARTADO_DATO", allocationSize = 1)
    @Column(name = "ID_APARTADO_DATO")
    public Integer getIdApartadoDato() {
        return this.idApartadoDato;
    }

    /**
     * Establece el valor del campo idApartadoDato de la entidad ApartadoDato
     *
     * @param idApartadoDato El valor a establecer
     */
    public void setIdApartadoDato(Integer idApartadoDato) {
        this.idApartadoDato = idApartadoDato;
    }

    /**
     * @return Devuelve el valor del campo idExpedienteEval
     */
    @Column(name = "ID_EXPEDIENTE_EVAL")
    public Integer getIdExpedienteEval() {
        return this.idExpedienteEval;
    }

    /**
     * Establece el valor del campo idExpedienteEval de la entidad ApartadoDato
     *
     * @param idExpedienteEval El valor a establecer
     */
    public void setIdExpedienteEval(Integer idExpedienteEval) {
        this.idExpedienteEval = idExpedienteEval;
    }

    /**
     * @return Devuelve el valor del campo idApartado
     */
    @Column(name = "ID_APARTADO")
    public Integer getIdApartado() {
        return this.idApartado;
    }

    /**
     * Establece el valor del campo idApartado de la entidad ApartadoDato
     *
     * @param idApartado El valor a establecer
     */
    public void setIdApartado(Integer idApartado) {
        this.idApartado = idApartado;
    }

    /**
     * @return Devuelve el valor del campo dsComentario
     */
    @Lob
    @Column(name = "DS_COMENTARIO")
    public String getDsComentario() {
        return this.dsComentario;
    }

    /**
     * Establece el valor del campo dsComentario de la entidad ApartadoDato
     *
     * @param dsComentario El valor a establecer
     */
    public void setDsComentario(String dsComentario) {
        this.dsComentario = dsComentario;
    }

    /**
     * @return Devuelve el valor del campo nmNota
     */
    @Column(name = "NM_NOTA")
    public Integer getNmNota() {
        return this.nmNota;
    }

    /**
     * Establece el valor del campo nmNota de la entidad ApartadoDato
     *
     * @param nmNota El valor a establecer
     */
    public void setNmNota(Integer nmNota) {
        this.nmNota = nmNota;
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
