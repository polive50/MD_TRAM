package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio ConvocatoriaPla
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_PLA")
@AtlasAuditarFila
@ToString
public class ConvocatoriaPla implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaPla del objeto de dominio
     */
    private Integer idConvocatoriaPla;

    /**
     * Representa el campo cdPlantilla del objeto de dominio
     */
    private String cdPlantilla;

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
     * Representa el campo convocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     *
     */
    private List<ConvocatoriaPlaFir> lstConvocatoriaPlaFir;

    /**
     *
     */
    private List<ConvocatoriaPlaVer> lstConvocatoriaPlaVer;

    /**
     * Representa el campo idTramite del objeto de dominio
     */
    private CatalogosValores tramite;

    /**
     * Representa el logo de la convocatoria
     */
    private Imagen imagen;

    /**
     * Representa el campo dsAsunto del objeto de dominio
     */
    private String dsAsunto;

    /**
     * Representa el campo dsDescripcion del objeto de dominio
     */
    private String dsDescripcion;
    
    /**
     * Representa el campo dsDescripcionInf del objeto de dominio
     */
    private String dsDescripcionInf;
    
    /**
    *
    */
   private List<ConvocatoriaPlaDoc> lstConvocatoriaPlaDoc;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaPla() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaPla).append(this.idConvocatoria)
                .append(this.cdPlantilla).append(this.dsAsunto).append(this.dsDescripcion)
                .append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif)
                .append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila)
                .append(this.tramite).append(this.dsDescripcionInf).toHashCode();
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
        if (!(obj instanceof ConvocatoriaPla)) {
            return false;
        }
        ConvocatoriaPla castObj = (ConvocatoriaPla) obj;
        return new EqualsBuilder().append(this.idConvocatoriaPla, castObj.idConvocatoriaPla)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.cdPlantilla, castObj.cdPlantilla)
                .append(this.dsAsunto, castObj.dsAsunto)
                .append(this.dsDescripcion, castObj.dsDescripcion)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).append(this.tramite, castObj.tramite)
                .append(this.dsDescripcionInf, castObj.dsDescripcionInf)
                .isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoriaPla
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_PLA")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_PLA", sequenceName = "SAII_S_CONVOCATORIA_PLA", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_PLA")
    public Integer getIdConvocatoriaPla() {
        return this.idConvocatoriaPla;
    }

    /**
     * Establece el valor del campo idConvocatoriaPla de la entidad
     * ConvocatoriaPla
     *
     * @param idConvocatoriaPla El valor a establecer
     */
    public void setIdConvocatoriaPla(Integer idConvocatoriaPla) {
        this.idConvocatoriaPla = idConvocatoriaPla;
    }

    /**
     * @return Devuelve el valor del campo convocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo convocatoria de la entidad ConvocatoriaPla
     *
     * @param convocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return the cdPlantilla
     */
    @Column(name = "CD_PLANTILLA", nullable = false)
    public String getCdPlantilla() {
        return this.cdPlantilla;
    }

    /**
     * @param cdPlantilla the cdPlantilla to set
     */
    public void setCdPlantilla(String cdPlantilla) {
        this.cdPlantilla = cdPlantilla;
    }

    /**
     * @return Devuelve el valor del campo idTramite
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TRAMITE")
    public CatalogosValores getTramite() {
        return this.tramite;
    }

    /**
     * Establece el valor del campo idTramite de la entidad Catalogos
     *
     * @param idTramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
    }

    /**
     * @return Devuelve el valor del campo idImagenLogoSupIzq
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IMAGEN_LOGO_SUP_IZQ")
    public Imagen getImagen() {
        return this.imagen;
    }

    /**
     * Establece el valor del campo idImagenLogoSupIzq de la entidad Imagen
     *
     * @param idImagenLogoSupIzq El valor a establecer
     */
    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }

    /**
     * @return Devuelve el valor del campo convocatoriaPlaFir
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idConvocatoriaPla", cascade = CascadeType.ALL)
    public List<ConvocatoriaPlaFir> getLstConvocatoriaPlaFir() {
        return this.lstConvocatoriaPlaFir;
    }

    public void setLstConvocatoriaPlaFir(List<ConvocatoriaPlaFir> lstConvocatoriaPlaFir) {
        this.lstConvocatoriaPlaFir = lstConvocatoriaPlaFir;
    }

    /**
     * @return Devuelve el valor del campo convocatoriaPlaVer
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idConvocatoriaPla", cascade = CascadeType.ALL)
    public List<ConvocatoriaPlaVer> getLstConvocatoriaPlaVer() {
        return this.lstConvocatoriaPlaVer;
    }

    public void setLstConvocatoriaPlaVer(List<ConvocatoriaPlaVer> lstConvocatoriaPlaVer) {
        this.lstConvocatoriaPlaVer = lstConvocatoriaPlaVer;
    }

    /**
     * @return the dsAsunto
     */
    @Column(name = "DS_ASUNTO", nullable = false)
    public String getDsAsunto() {
        return this.dsAsunto;
    }

    /**
     * @param dsAsunto the dsAsunto to set
     */
    public void setDsAsunto(String dsAsunto) {
        this.dsAsunto = dsAsunto;
    }

    /**
     * @return the dsDescripcion
     */
    @Column(name = "DS_DESCRIPCION", nullable = false)
    public String getDsDescripcion() {
        return this.dsDescripcion;
    }

    /**
     * @param dsDescripcion the dsDescripcion to set
     */
    public void setDsDescripcion(String dsDescripcion) {
        this.dsDescripcion = dsDescripcion;
    }
    
    /**
     * @return the dsDescripcionInf
     */
    @Column(name = "DS_DESCRIPCION_INF", nullable = false)
    public String getDsDescripcionInf() {
        return this.dsDescripcionInf;
    }
    
    /**
     * @param dsDescripcionInf the dsDescripcionInf to set
     */
    public void setDsDescripcionInf(String dsDescripcionInf) {
        this.dsDescripcionInf = dsDescripcionInf;
    }
    
    
    /**
     * @return Devuelve el valor del campo convocatoriaPlaDoc
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idConvocatoriaPlaDoc", cascade = CascadeType.ALL)
    public List<ConvocatoriaPlaDoc> getLstConvocatoriaPlaDoc() {
        return this.lstConvocatoriaPlaDoc;
    }

    public void setLstConvocatoriaPlaDoc(List<ConvocatoriaPlaDoc> lstConvocatoriaPlaDoc) {
        this.lstConvocatoriaPlaDoc = lstConvocatoriaPlaDoc;
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
