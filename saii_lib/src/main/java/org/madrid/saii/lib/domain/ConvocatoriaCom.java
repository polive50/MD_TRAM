package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
 * Representa el objeto de dominio ConvocatoriaCom
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA_COM")
@AtlasAuditarFila
@ToString
public class ConvocatoriaCom implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoriaCom del objeto de dominio
     */
    private Integer idConvocatoriaCom;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idTramite del objeto de dominio
     */
    private CatalogosValores tramite;

    /**
     * Representa el campo idConvocatoriaActNote del objeto de dominio
     */
    private ConvocatoriaAct convocatoriaActNote;

    /**
     * Representa el campo idConvocatoriaActCorreo del objeto de dominio
     */
    private ConvocatoriaAct convocatoriaActCorreo;

    /**
     * Representa el campo itNote del objeto de dominio
     */
    private String itNote;

    /**
     * Representa el campo itCorreo del objeto de dominio
     */
    private String itCorreo;

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
    
    private List<ComunicacionNoteActor> lstActoresNote;
    
    private List<ComunicacionCorreoActor> lstActoresCorreo;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCom() {
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
        return new HashCodeBuilder().append(this.idConvocatoriaCom).append(this.idConvocatoria)
                .append(this.tramite).append(this.convocatoriaActNote).append(this.convocatoriaActCorreo)
                .append(this.itNote).append(this.itCorreo).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja).append(this.fcBajaFila).toHashCode();
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
        if (!(obj instanceof ConvocatoriaCom)) {
            return false;
        }
        ConvocatoriaCom castObj = (ConvocatoriaCom) obj;
        return new EqualsBuilder().append(this.idConvocatoriaCom, castObj.idConvocatoriaCom)
                .append(this.idConvocatoria, castObj.idConvocatoria).append(this.tramite, castObj.tramite)
                .append(this.convocatoriaActNote, castObj.convocatoriaActNote).append(this.convocatoriaActCorreo, castObj.convocatoriaActCorreo)
                .append(this.itNote, castObj.itNote).append(this.itCorreo, castObj.itCorreo).append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila).append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila).append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoriaCom
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA_COM")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA_COM", sequenceName = "SAII_S_CONVOCATORIA_COM", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA_COM")
    public Integer getIdConvocatoriaCom() {
        return this.idConvocatoriaCom;
    }

    /**
     * Establece el valor del campo idConvocatoriaCom de la entidad
     * ConvocatoriaCom
     *
     * @param idConvocatoriaCom El valor a establecer
     */
    public void setIdConvocatoriaCom(Integer idConvocatoriaCom) {
        this.idConvocatoriaCom = idConvocatoriaCom;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad ConvocatoriaCom
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
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
     * Establece el valor del campo idTramite de la entidad ConvocatoriaCom
     *
     * @param idTramite El valor a establecer
     */
    public void setTramite(CatalogosValores tramite) {
        this.tramite = tramite;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoriaActNote
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_ACT_NOTE", nullable = true)
    public ConvocatoriaAct getConvocatoriaActNote() {
        return this.convocatoriaActNote;
    }

    /**
     * Establece el valor del campo idConvocatoriaActNote de la entidad
     * ConvocatoriaCom
     *
     * @param idConvocatoriaAct El valor a establecer
     */
    public void setConvocatoriaActNote(ConvocatoriaAct convocatoriaActNote) {
        this.convocatoriaActNote = convocatoriaActNote;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoriaActCorreo
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA_ACT_CORREO", nullable = true)
    public ConvocatoriaAct getConvocatoriaActCorreo() {
        return this.convocatoriaActCorreo;
    }

    /**
     * Establece el valor del campo idConvocatoriaAct de la entidad
     * ConvocatoriaCom
     *
     * @param idConvocatoriaAct El valor a establecer
     */
    public void setConvocatoriaActCorreo(ConvocatoriaAct convocatoriaActCorreo) {
        this.convocatoriaActCorreo = convocatoriaActCorreo;
    }

    /**
     * @return Devuelve el valor del campo itNote
     */
    @Column(name = "IT_NOTE")
    public String getItNote() {
        return this.itNote;
    }

    /**
     * Establece el valor del campo itNote de la entidad ConvocatoriaCom
     *
     * @param itNote El valor a establecer
     */
    public void setItNote(String itNote) {
        this.itNote = itNote;
    }

    /**
     * @return Devuelve el valor del campo itCorreo
     */
    @Column(name = "IT_CORREO")
    public String getItCorreo() {
        return this.itCorreo;
    }

    /**
     * Establece el valor del campo itCorreo de la entidad ConvocatoriaCom
     *
     * @param itCorreo El valor a establecer
     */
    public void setItCorreo(String itCorreo) {
        this.itCorreo = itCorreo;
    }
    
    //VMM 23/06/25 MD-1696: ahora tenemos la lista de actores note y correo bien mapeada con las nuevas tablas de comunicaciones por actores
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idConvocatoriaCom")
    public List<ComunicacionNoteActor> getLstActoresNote() {
        return this.lstActoresNote;
    }

    public void setLstActoresNote(List<ComunicacionNoteActor> lstActoresNote) {
        this.lstActoresNote = lstActoresNote;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idConvocatoriaCom")
    public List<ComunicacionCorreoActor> getLstActoresCorreo() {
        return this.lstActoresCorreo;
    }

    public void setLstActoresCorreo(List<ComunicacionCorreoActor> lstActoresCorreo) {
        this.lstActoresCorreo = lstActoresCorreo;
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
