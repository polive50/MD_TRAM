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
import atlas.core.database.auditoria.AtlasAuditarFila;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import lombok.ToString;

@Entity
@Table(name = "SAII_EXPEDIENTE_CONTRATO")
@AtlasAuditarFila
@ToString
public class ExpedienteContrato implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Representa el campo idExpedienteContrato del objeto de dominio
     */
    private Integer idExpedienteContrato;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores idEstado;
    
    
    /**
     * Representa el campo idTipoContrato del objeto de dominio
     */
    private CatalogosValores idTipoContrato;
    
    /**
     * Representa el campo fcIniContrato del objeto de dominio
     */
    private Date fcIniContrato;
    
    /**
     * Representa el campo fcFinContrato del objeto de dominio
     */
    private Date fcFinContrato;
    
    /**
     * Representa el campo itIndefinido del objeto de dominio
     */
    private String itIndefinido;
     
    /**
     * Representa el campo idCausa del objeto de dominio
     */
    private CatalogosValores idCausa;
     
    /**
     * Representa el campo tlCausaDescripcion del objeto de dominio
     */
    private String tlCausaDescripcion;
    
    
    /**
     * Representa el campo tlObservaciParaCiudadano del objeto de dominio
     */
    private String tlObservaciParaCiudadano;
    
    /**
     * Representa el campo tlObservaciDelCiudadano del objeto de dominio
     */
    private String tlObservaciDelCiudadano;
   
    
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
     * Representa el campo cdEreg de la presentacion del objeto de dominio
     */
    private String cdEregPresent;
    
    /**
     * Representa el campo cdEreg de la subsanacion del objeto de dominio
     */
    private String cdEregSubsana;
    
    /**
     * Representa el campo dsDestinoEreg del objeto de dominio
     */
    private String dsDestinoEreg;

    /**
     * Representa el campo fcFirma de la presentación del objeto de dominio
     */
    private Date fcFirmaPresent;
    
    /**
     * Representa el campo fcFirma de la subsanación del objeto de dominio
     */
    private Date fcFirmaSubsana;
    
    /**
     * Representa el campo idEstadoRevAdmin del objeto de dominio
     */
    private CatalogosValores idEstadoRevAdmin;
    
    /**
     * Representa el campo fcRenuncia del objeto de dominio
     */
    private Date fcRenuncia;
    
    /**
     * Representa el campo interesado del objeto de dominio
     */
    private Interesado interesado;
    
    /**
     * Representa el campo cdEregRenuncia de la renuncia del objeto de dominio
     */
    private String cdEregRenuncia;
    
    /**
     * Representa el campo fcFirmaRenuncia de la presentación del objeto de dominio
     */
    private Date fcFirmaRenuncia;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteContrato() {
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
        return new HashCodeBuilder().append(this.idExpedienteContrato)
                .append(this.idExpediente)
                .append(this.idTipoContrato)
                .append(this.idEstado)               
                .append(this.idCausa)
                .append(this.itIndefinido)
                .append(this.tlCausaDescripcion)
                .append(this.tlObservaciParaCiudadano)
                .append(this.tlObservaciDelCiudadano)
                .append(this.fcIniContrato)
                .append(this.fcFinContrato)
                .append(this.cdUsuAlta)
                .append(this.fcAltaFila)
                .append(this.cdUsuModif)
                .append(this.fcModifFila)
                .append(this.cdUsuBaja)
                .append(this.fcBajaFila)
                .append(this.fcRenuncia)
                .append(this.interesado)
                .append(this.idEstadoRevAdmin)
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
        if (!(obj instanceof ExpedienteContrato)) {
            return false;
        }
        ExpedienteContrato castObj = (ExpedienteContrato) obj;
        return new EqualsBuilder().append(this.idExpedienteContrato, castObj.idExpedienteContrato)
                .append(this.idExpediente, castObj.idExpediente)
                .append(this.idTipoContrato, castObj.idTipoContrato)
                .append(this.idEstado, castObj.idEstado)               
                .append(this.idCausa, castObj.idCausa)
                .append(this.itIndefinido, castObj.itIndefinido)
                .append(this.tlCausaDescripcion, castObj.tlCausaDescripcion)
                .append(this.tlObservaciParaCiudadano, castObj.tlObservaciParaCiudadano)
                .append(this.tlObservaciDelCiudadano, castObj.tlObservaciDelCiudadano)
                .append(this.fcIniContrato, castObj.fcIniContrato)
                .append(this.fcFinContrato, castObj.fcFinContrato)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.fcRenuncia, castObj.fcRenuncia)
                .append(this.interesado, castObj.interesado)
                .append(this.idEstadoRevAdmin, castObj.idEstadoRevAdmin)
                .isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idExpedienteContrato
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_CONTRATO")
    @SequenceGenerator(name = "SAII_S_EXPEDIENTE_CONTRATO", sequenceName = "SAII_S_EXPEDIENTE_CONTRATO", allocationSize = 1)
    @Column(name = "ID_EXPEDIENTE_CONTRATO")
    public Integer getIdExpedienteContrato() {
        return this.idExpedienteContrato;
    }

    /**
     * Establece el valor del campo idExpedienteJustecDoc de la entidad
     * ExpedienteContrato
     *
     * @param idExpedienteContrato El valor a establecer
     */
    public void setIdExpedienteContrato(Integer idExpedienteContrato) {
        this.idExpedienteContrato = idExpedienteContrato;
    }

    /**
     * @return Devuelve el valor del campo idExpediente
     */
    @Column(name = "ID_EXPEDIENTE")
    public Integer getIdExpediente() {
        return this.idExpediente;
    }

    /**
     * Establece el valor del campo idExpediente de la entidad
     * ExpedienteContrato
     *
     * @param ExpedienteContrato El valor a establecer
     */
    public void setIdExpediente(Integer idExpediente) {
        this.idExpediente = idExpediente;
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
     * Establece el valor del campo cdUsuAlta de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo cdUsuBaja de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo cdUsuModif de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcAltaFila de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcBajaFila de la entidad ExpedienteJustecDoc
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
     * Establece el valor del campo fcModifFila de la entidad
     * ExpedienteJustecDoc
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "ID_ESTADO", nullable = false)
    public CatalogosValores getIdEstado() {
        return this.idEstado;
    }
    

    public void setIdEstado(CatalogosValores idEstado) {
        this.idEstado = idEstado;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "ID_TIPO_CONTRATO", nullable = false)
    public CatalogosValores getIdTipoContrato() {
        return this.idTipoContrato;
    }

    public void setIdTipoContrato(CatalogosValores idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    @Column(name = "FC_INI_CONTRATO", nullable = true)
    public Date getFcIniContrato() {
        return this.fcIniContrato;
    }

    public void setFcIniContrato(Date fcIniContrato) {
        this.fcIniContrato = fcIniContrato;
    }

    @Column(name = "FC_FIN_CONTRATO", nullable = true)
    public Date getFcFinContrato() {
        return this.fcFinContrato;
    }

    
    public void setFcFinContrato(Date fcFinContrato) {
        this.fcFinContrato = fcFinContrato;
    }

    @Column(name = "IT_INDEFINIDO", nullable = false)
    public String getItIndefinido() {
        return this.itIndefinido;
    }

    public void setItIndefinido(String itIndefinido) {
        this.itIndefinido = itIndefinido;
    }

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAUSA", nullable = true)
    public CatalogosValores getIdCausa() {
        return this.idCausa;
    }

    public void setIdCausa(CatalogosValores idCausa) {
        this.idCausa = idCausa;
    }

    @Column(name = "TL_CAUSA_DESCRIPCION", nullable = true)
    public String getTlCausaDescripcion() {
        return this.tlCausaDescripcion;
    }

    public void setTlCausaDescripcion(String tlCausaDescripcion) {
        this.tlCausaDescripcion = tlCausaDescripcion;
    }

    @Column(name = "TL_OBSERVACI_PARA_CIUDADANO", nullable = true)
    public String getTlObservaciParaCiudadano() {
        return this.tlObservaciParaCiudadano;
    }

    public void setTlObservaciParaCiudadano(String tlObservaciParaCiudadano) {
        this.tlObservaciParaCiudadano = tlObservaciParaCiudadano;
    }

    @Column(name = "TL_OBSERVACI_DEL_CIUDADANO", nullable = true)
    public String getTlObservaciDelCiudadano() {
        return this.tlObservaciDelCiudadano;
    }

    
    public void setTlObservaciDelCiudadano(String tlObservaciDelCiudadano) {
        this.tlObservaciDelCiudadano = tlObservaciDelCiudadano;
    }  

    @Column(name = "CD_EREG_PRESENT", nullable = true)
    public String getCdEregPresent() {
        return cdEregPresent;
    }

    public void setCdEregPresent(String cdEregPresent) {
        this.cdEregPresent = cdEregPresent;
    }

    @Column(name = "CD_EREG_SUBSANA", nullable = true)
    public String getCdEregSubsana() {
        return cdEregSubsana;
    }

    public void setCdEregSubsana(String cdEregSubsana) {
        this.cdEregSubsana = cdEregSubsana;
    }

    @Column(name = "DS_DESTINO_EREG", nullable = true)
    public String getDsDestinoEreg() {
        return dsDestinoEreg;
    }

    public void setDsDestinoEreg(String dsDestinoEreg) {
        this.dsDestinoEreg = dsDestinoEreg;
    }

    @Column(name = "FC_FIRMA_PRESENT", nullable = true)
    public Date getFcFirmaPresent() {
        return fcFirmaPresent;
    }

    public void setFcFirmaPresent(Date fcFirmaPresent) {
        this.fcFirmaPresent = fcFirmaPresent;
    }

    @Column(name = "FC_FIRMA_SUBSANA", nullable = true)
    public Date getFcFirmaSubsana() {
        return fcFirmaSubsana;
    }

    public void setFcFirmaSubsana(Date fcFirmaSubsana) {
        this.fcFirmaSubsana = fcFirmaSubsana;
    }
    
    /**
     * @return the estadoRevAdmin
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_REV_ADMIN", nullable = true)
    public CatalogosValores getIdEstadoRevAdmin() {
        return idEstadoRevAdmin;
    }

    public void setIdEstadoRevAdmin(CatalogosValores idEstadoRevAdmin) {
        this.idEstadoRevAdmin = idEstadoRevAdmin;
    }

    @Column(name = "FC_RENUNCIA", nullable = true)
    public Date getFcRenuncia() {
        return fcRenuncia;
    }

    public void setFcRenuncia(Date fcRenuncia) {
        this.fcRenuncia = fcRenuncia;
    }

    /**
     * @return the interesado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERESADO", nullable = true)
    public Interesado getInteresado() {
        return interesado;
    }

    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
    }

    @Column(name = "CD_EREG_RENUNCIA", nullable = true)
    public String getCdEregRenuncia() {
        return cdEregRenuncia;
    }

    public void setCdEregRenuncia(String cdEregRenuncia) {
        this.cdEregRenuncia = cdEregRenuncia;
    }

    @Column(name = "FC_FIRMA_RENUNCIA", nullable = true)
    public Date getFcFirmaRenuncia() {
        return fcFirmaRenuncia;
    }

    public void setFcFirmaRenuncia(Date fcFirmaRenuncia) {
        this.fcFirmaRenuncia = fcFirmaRenuncia;
    }
    
    
  
}
