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
 * Representa el objeto de dominio ExpedienteDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_DOC")
@AtlasAuditarFila
@ToString
public class ExpedienteDoc implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteDoc del objeto de dominio
     */
    private Integer idExpedienteDoc;

    /**
     * Representa el campo cdFica del objeto de dominio
     */
    private Integer cdFica;

    /**
     * Representa el campo cdAnexoEreg del objeto de dominio
     */
    private String cdAnexoEreg;

    /**
     * Representa el campo cdAnexoEregPadre del objeto de dominio
     */
    private String cdAnexoEregPadre;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo idTipoDocumento del objeto de dominio
     */
    private CatalogosValores tipoDocumento;

    /**
     * Representa el campo idBloqueComponente del objeto de dominio
     */
    private BloqueComponente bloqueComponente;

    /**
     *
     * Representa el campo tlCausaDescripcion del objeto de dominio
     */
    private String tlCausaDescripcion;

    /**
     * Representa el campo Expediente del objeto de dominio
     */
    private Expediente expediente;

    /**
     * Representa el campo idDocumentoPtfr del objeto de dominio
     */
    private Integer idDocumentoPtfr;

    /**
     * Representa el campo idConvocatoriaCau del objeto de dominio
     */
    private ConvocatoriaCau convocatoriaCau;
    
    /**
     * Representa el campo idConvocatoriaCauCont del objeto de dominio
     */
    private ConvocatoriaCauCont convocatoriaCauCont;

    /**
     * Representa el campo tlObservaciones del objeto de dominio
     */
    private String tlObservaciones;

    /**
     * Representa el campo tramite del objeto de dominio
     */
    private CatalogosValores tramite;

    /**
     * Representa si es un documento adicional
     */
    private String itAdicional;

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
     * Representa el campo itAutoriza
     */
    private String itAutoriza;
    
    
    /**
     * Representa el campo idCausa del objeto de dominio
     */
    private Integer idCausa;
    
    /**
     * Representa el campo tlObservacionesGestor del objeto de dominio
     */
    private String tlObservacionesGestor;
    
    /**
     * Representa el campo dato del objeto de dominio
     */
    private Dato dato;
    
    
    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcRegistro;
    
    /**
     * Representa el campo tlObservacionesGestor del objeto de dominio
     */
    private String codigoEreg;
    
    
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteDoc() {
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
        return new HashCodeBuilder().append(this.idExpedienteDoc).append(this.cdFica)
                .append(this.estado).append(this.tlCausaDescripcion).append(this.expediente)
                .append(this.tramite).toHashCode();
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
        if (!(obj instanceof ExpedienteDoc)) {
            return false;
        }
        ExpedienteDoc castObj = (ExpedienteDoc) obj;
        return new EqualsBuilder().append(this.idExpedienteDoc, castObj.idExpedienteDoc)
                .append(this.cdFica, castObj.cdFica).append(this.estado, castObj.estado)
                .append(this.expediente, castObj.expediente)
                .append(this.tramite, castObj.tramite).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteDoc
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_DOC")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_DOC", sequenceName = "SAII_S_EXPEDIENTE_DOC", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_DOC")
    public Integer getIdExpedienteDoc() {
        return this.idExpedienteDoc;
    }

    /**
     * Establece el valor del campo idExpedienteDoc de la entidad ExpedienteDoc
     *
     * @param idExpedienteDoc El valor a establecer
     */
    public void setIdExpedienteDoc(Integer idExpedienteDoc) {
        this.idExpedienteDoc = idExpedienteDoc;
    }

    /**
     * @return Devuelve el valor del campo cdFica
     */
    @Column(name = "CD_FICA", nullable = true)
    public Integer getCdFica() {
        return this.cdFica;
    }

    /**
     * Establece el valor del campo cdFica de la entidad ExpedienteDoc
     *
     * @param cdFica El valor a establecer
     */
    public void setCdFica(Integer cdFica) {
        this.cdFica = cdFica;
    }

    /**
     * @return Devuelve el valor del campo cdAnexoEreg
     */
    @Column(name = "CD_ANEXO_EREG", nullable = true)
    public String getCdAnexoEreg() {
        return this.cdAnexoEreg;
    }

    /**
     * Establece el valor del campo cdAnexoEreg de la entidad ExpedienteDoc
     *
     * @param cdAnexoEreg El valor a establecer
     */
    public void setCdAnexoEreg(String cdAnexoEreg) {
        this.cdAnexoEreg = cdAnexoEreg;
    }

    /**
     * @return Devuelve el valor del campo cdAnexoEregPadre
     */
    @Column(name = "CD_ANEXO_EREG_PADRE", nullable = true)
    public String getCdAnexoEregPadre() {
        return this.cdAnexoEregPadre;
    }

    /**
     * Establece el valor del campo cdAnexoEregPadre de la entidad ExpedienteDoc
     *
     * @param cdAnexoEregPadre El valor a establecer
     */
    public void setCdAnexoEregPadre(String cdAnexoEregPadre) {
        this.cdAnexoEregPadre = cdAnexoEregPadre;
    }

    /**
     * @return Devuelve el valor del campo idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getEstado() {
        return this.estado;
    }

    /**
     * Establece el valor del campo idEstado de la entidad ExpedienteDoc
     *
     * @param estado El valor a establecer
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return Devuelve el valor del campo tipoDocumento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_DOCUMENTO", nullable = true)
    public CatalogosValores getTipoDocumento() {
        return this.tipoDocumento;
    }

    /**
     * Establece el valor del campo tipoDocumento de la entidad ExpedienteDoc
     *
     * @param tipoDocumento El valor a establecer
     */
    public void setTipoDocumento(CatalogosValores tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    /**
     * @return Devuelve el valor del campo tlObservaciones
     */
    @Column(name = "TL_OBSERVACIONES", nullable = true)
    public String getTlObservaciones() {
        return this.tlObservaciones;
    }

    /**
     * Establece el valor del campo tlObservaciones de la entidad ExpedienteDoc
     *
     * @param tlObservaciones El valor a establecer
     */
    public void setTlObservaciones(String tlObservaciones) {
        this.tlObservaciones = tlObservaciones;
    }

    /**
     * @return Devuelve el valor del campo tlObservaciones
     */
    @Column(name = "CD_CODIGO_EREG", nullable = true)
    public String getCodigoEreg() {
        return this.codigoEreg;
    }

    /**
     * Establece el valor del campo tlObservaciones de la entidad ExpedienteDoc
     *
     * @param tlObservaciones El valor a establecer
     */
    public void setCodigoEreg(String codigoEreg) {
        this.codigoEreg = codigoEreg;
    }
    
    
    /**
     * @return Devuelve el valor del campo idBloqueComponente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_BLOQUE_COMPONENTE", nullable = true)
    public BloqueComponente getBloqueComponente() {
        return this.bloqueComponente;
    }

    /**
     * Establece el valor del campo idBloqueComponente de la entidad
     * ExpedienteDoc
     *
     * @param bloqueComponente El valor a establecer
     */
    public void setBloqueComponente(BloqueComponente bloqueComponente) {
        this.bloqueComponente = bloqueComponente;
    }

    /**
     * @return Devuelve el valor del campo Expediente
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EXPEDIENTE", nullable = false)
    public Expediente getExpediente() {
        return this.expediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad ExpedienteDoc
     *
     * @param Expediente El valor a establecer
     */
    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    /**
     * @return Devuelve el valor del campo idDocumentoPtfr
     */
    @Column(name = "ID_DOCUMENTO_PTFR", nullable = true)
    public Integer getIdDocumentoPtfr() {
        return this.idDocumentoPtfr;
    }

    /**
     * Establece el valor del campo idDocumentoPtfr de la entidad ExpedienteDoc
     *
     * @param idDocumentoPtfr El valor a establecer
     */
    public void setIdDocumentoPtfr(Integer idDocumentoPtfr) {
        this.idDocumentoPtfr = idDocumentoPtfr;
    }


    /**
     * @return Devuelve el valor del campo idCausa
     */
    @Column(name = "ID_CAUSA", nullable = true)
    public Integer getIdCausa() {
        return this.idCausa;
    }

    /**
     * Establece el valor del campo idCausa de la entidad ExpedienteDoc
     *
     * @param idCausa El valor a establecer
     */
    public void setIdCausa(Integer idCausa) {
        this.idCausa = idCausa;
    }
    
    
    
    /**
     * @return Devuelve el valor del campo idConvocatoriaCau
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_CAU", nullable = true)
    public ConvocatoriaCau getConvocatoriaCau() {
        return this.convocatoriaCau;
    }

    /**
     * Establece el valor del campo idConvocatoriaCau de la entidad
     * ExpedienteDoc
     *
     * @param convocatoriaCau El valor a establecer
     */
    public void setConvocatoriaCau(ConvocatoriaCau convocatoriaCau) {
        this.convocatoriaCau = convocatoriaCau;
    }
    
    /**
     * @return Devuelve el valor del campo idConvocatoriaCauCont
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_CAU_CONT", nullable = true)
    public ConvocatoriaCauCont getConvocatoriaCauCont() {
        return this.convocatoriaCauCont;
    }
    
    /**
     * Establece el valor del campo idConvocatoriaCauCont de la entidad
     * ExpedienteDoc
     *
     * @param convocatoriaCauCont El valor a establecer
     */
    public void setConvocatoriaCauCont(ConvocatoriaCauCont convocatoriaCauCont) {
        this.convocatoriaCauCont = convocatoriaCauCont;
    }

    /**
     * @return Devuelve el valor del campo tramite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE", nullable = true)
    public CatalogosValores getTramite() {
        return this.tramite;
    }

    /**
     * Establece el valor del campo tramite de la entidad ExpedienteDoc
     *
     * @param tramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
    }

    /**
     * @return Devuelve el valor del campo itAdicional
     */
    @Column(name = "IT_ADICIONAL", nullable = true)
    public String getItAdicional() {
        return this.itAdicional;
    }

    /**
     * Establece el valor del campo itAdicional de la entidad ExpedienteDoc
     *
     * @param itAdicional El valor a establecer
     */
    public void setItAdicional(String itAdicional) {
        this.itAdicional = itAdicional;
    }
    
    /**
     * @return Devuelve el valor del campo tlObservacionesGestor
     */
    @Column(name = "TL_OBSERVACIONES_GESTOR", nullable = true)
    public String getTlObservacionesGestor() {
        return this.tlObservacionesGestor;
    }

    /**
     * Establece el valor del campo tlObservacionesGestor de la entidad ExpedienteDoc
     *
     * @param tlObservacionesGestor El valor a establecer
     */
    public void setTlObservacionesGestor(String tlObservacionesGestor) {
        this.tlObservacionesGestor = tlObservacionesGestor;
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
    
    
    
    /**
     * @return the itAutoriza
     */
    @Column(name = "IT_AUTORIZA", nullable = true)
    public String getItAutoriza() {
        return itAutoriza;
    }

    /**
     * @param itAutoriza the itAutoriza to set
     */
    public void setItAutoriza(String itAutoriza) {
        this.itAutoriza = itAutoriza;
    }

    /**
     * @return Devuelve el valor del campo dato
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DATO", nullable = true)
    public Dato getDato() {
        return dato;
    }

    /**
     * Establece el valor del campo idDato de la entidad Dato
     *
     * @param dato El valor a establecer
     */
    public void setDato(Dato dato) {
        this.dato = dato;
    }
    
    
    /**
     * @return Devuelve el valor del campo fcAltaFila
     */
    @Column(name = "FC_FECHA_REGISTRO")
    public Date getFcRegistro() {
        return this.fcRegistro;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Catalogos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcRegistro(Date fcRegistro) {
        this.fcRegistro = fcRegistro;
    }
    
    
  
    
   
}
