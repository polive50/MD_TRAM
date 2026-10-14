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
 * Representa el objeto de dominio ListadoColumna
 *
 * @author nttdata
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_LISTADO_COLUMNA")
@AtlasAuditarFila
@ToString
public class ListadoColumna implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idListadoColumna del objeto de dominio
     */
    private Integer idListadoColumna;

    /**
     * Representa el campo idListado del objeto de dominio
     */
    private Integer idListado;

    /**
     *
     */
    private Integer idComponente;

    /**
    *
    */
    private Integer idTipoFinanciacion;

    /**
    *
    */
    private Integer idTematica;

    /**
     * Representa el campo nmOrden del objeto de dominio
     */
    private Integer nmOrden;

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
    public ListadoColumna() {
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
        return new HashCodeBuilder().append(this.idListadoColumna).append(this.idListado)
                .append(this.nmOrden).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof ListadoColumna)) {
            return false;
        }
        ListadoColumna castObj = (ListadoColumna) obj;
        return new EqualsBuilder().append(this.idListadoColumna, castObj.idListadoColumna)
                .append(this.idListado, castObj.idListado).append(this.nmOrden, castObj.nmOrden)
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
     * @return Devuelve el valor del campo idListadoColumna
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_LISTADO_COLUMNA")
    @SequenceGenerator(name = "SAII_S_LISTADO_COLUMNA", sequenceName = "SAII_S_LISTADO_COLUMNA", allocationSize = 1)
    @Column(name = "ID_LISTADO_COLUMNA")
    public Integer getIdListadoColumna() {
        return this.idListadoColumna;
    }

    /**
     * Establece el valor del campo idListadoColumna de la entidad
     * ListadoColumna
     *
     * @param idListadoColumna El valor a establecer
     */
    public void setIdListadoColumna(Integer idListadoColumna) {
        this.idListadoColumna = idListadoColumna;
    }

    /**
     * @return Devuelve el valor del campo idListado
     */
    @Column(name = "ID_LISTADO")
    public Integer getIdListado() {
        return this.idListado;
    }

    /**
     * Establece el valor del campo idListado de la entidad ListadoColumna
     *
     * @param idListado El valor a establecer
     */
    public void setIdListado(Integer idListado) {
        this.idListado = idListado;
    }

    /**
     * @return Devuelve el valor del campo idComponente
     */
    @Column(name = "ID_COMPONENTE")
    public Integer getIdComponente() {
        return this.idComponente;
    }

    /**
     * Establece el valor del campo idComponente de la entidad ListadoColumna
     *
     * @param idComponente El valor a establecer
     */
    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
    }

    /**
     * Establece el valor del campo idTipoFinanciacion de la entidad
     * ListadoColumna
     *
     * @param idTipoFinanciacion El valor a establecer
     */
    public void setIdTipoFinanciacion(Integer idTipoFinanciacion) {
        this.idTipoFinanciacion = idTipoFinanciacion;
    }

    /**
     * @return Devuelve el valor del campo idTipoFinanciacion
     */
    @Column(name = "ID_TIPO_FINANCIACION")
    public Integer getIdTipoFinanciacion() {
        return this.idTipoFinanciacion;
    }

    /**
     * Establece el valor del campo idTematica de la entidad ListadoColumna
     *
     * @param idTematica El valor a establecer
     */
    public void setIdTematica(Integer idTematica) {
        this.idTematica = idTematica;
    }

    /**
     * @return Devuelve el valor del campo idTematica
     */
    @Column(name = "ID_TEMATICA")
    public Integer getIdTematica() {
        return this.idTematica;
    }

    /**
     * @return Devuelve el valor del campo nmOrden
     */
    @Column(name = "NM_ORDEN")
    public Integer getNmOrden() {
        return this.nmOrden;
    }

    /**
     * Establece el valor del campo nmOrden de la entidad ListadoColumna
     *
     * @param nmOrden El valor a establecer
     */
    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
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
