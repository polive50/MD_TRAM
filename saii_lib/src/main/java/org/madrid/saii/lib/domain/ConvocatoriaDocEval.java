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
 * Representa el objeto de dominio ConvocatoriaDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONV_DOC_EVAL")
@AtlasAuditarFila
@ToString
public class ConvocatoriaDocEval implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idDocumento del objeto de dominio
     */
    private Integer idDocumento;

    /**
     * Representa el campo cdFica del objeto de dominio
     */
    private Integer cdFica;

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
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idTipoDocumento del objeto de dominio
     */
    private CatalogosValores idTipoDocumento;

    /**
     * Representa el campo dsNombreFichero del objeto de dominio
     */
    private String dsNombreFichero;
    
    /**
     * Representa el campo idPersona del objeto de dominio
     */
    private Interesado persona;
    
    /**
     * Representa el campo itCoorEval del objeto de dominio
     */
    private String itCoorEval;
    
    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;
    
    /**
    *
    * Representa el campo tlCausaDescripcion del objeto de dominio
    */
   private String tlCausaDescripcion;
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaDocEval() {
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
        return new HashCodeBuilder().append(this.idDocumento).append(this.cdFica)
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
        if (!(obj instanceof ConvocatoriaDocEval)) {
            return false;
        }
        ConvocatoriaDocEval castObj = (ConvocatoriaDocEval) obj;
        return new EqualsBuilder().append(this.idDocumento, castObj.idDocumento)
                .append(this.cdFica, castObj.cdFica).append(this.cdUsuAlta, castObj.cdUsuAlta)
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
     * @return Devuelve el valor del campo idDocumento
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONV_DOC_EVAL")
    @SequenceGenerator(name = "SAII_S_CONV_DOC_EVAL", sequenceName = "SAII_S_CONV_DOC_EVAL", allocationSize = 1)
    @Column(name = "ID_DOCUMENTO")
    public Integer getIdDocumento() {
        return this.idDocumento;
    }

    /**
     * Establece el valor del campo idDocumento de la entidad ConvocatoriaDoc
     *
     * @param idDocumento El valor a establecer
     */
    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    /**
     * @return Devuelve el valor del campo cdFica
     */

    @Column(name = "CD_FICA", nullable = true)
    public Integer getCdFica() {
        return this.cdFica;
    }

    /**
     * Establece el valor del campo cdFica de la entidad ConvocatoriaDoc
     *
     * @param cdFica El valor a establecer
     */
    public void setCdFica(Integer cdFica) {
        this.cdFica = cdFica;
    }

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

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA", nullable = false)
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad ConvocatoriaDoc
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_DOCUMENTO", nullable = false)
    public CatalogosValores getIdTipoDocumento() {
        return this.idTipoDocumento;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad ConvocatoriaDoc
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdTipoDocumento(CatalogosValores idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    /**
     * @return the dsNombreFichero
     */
    @Column(name = "DS_NOMBRE_FICHERO", nullable = true)
    public String getDsNombreFichero() {
        return this.dsNombreFichero;
    }

    /**
     * @param dsNombreFichero the dsNombreFichero to set
     */
    public void setDsNombreFichero(String dsNombreFichero) {
        this.dsNombreFichero = dsNombreFichero;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    public Interesado getPersona() {
        return persona;
    }

    public void setPersona(Interesado persona) {
        this.persona = persona;
    }

    /**
     * @return the dsNombreFichero
     */
    @Column(name = "IT_COOR_EVAL", nullable = false)
    public String getItCoorEval() {
        return itCoorEval;
    }

    public void setItCoorEval(String itCoorEval) {
        this.itCoorEval = itCoorEval;
    }

    /**
     * @return the estado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = true)
    public CatalogosValores getEstado() {
        return estado;
    }

    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }
    
    /**
     * @return Devuelve el valor del campo tlCausaDescripcion
     */
    @Column(name = "TL_CAUSA_DESCRIPCION", nullable = true)
    public String getTlCausaDescripcion() {
        return this.tlCausaDescripcion;
    }

    /**
     * Establece el valor del campo tlCausaDescripcion de la entidad
     * ExpedienteDoc
     *
     * @param tlCausaDescripcion El valor a establecer
     */
    public void setTlCausaDescripcion(String tlCausaDescripcion) {
        this.tlCausaDescripcion = tlCausaDescripcion;
    }

}
