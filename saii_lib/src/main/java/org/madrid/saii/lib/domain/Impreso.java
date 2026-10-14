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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio Impreso
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_IMPRESO")
@AtlasAuditarFila
@ToString
public class Impreso implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idImpreso del objeto de dominio
     */
    private Integer idImpreso;

    /**
     * Representa el campo itTipo del objeto de dominio
     */
    private CatalogosValores tipo;

    /**
     * Representa el campo cdIdentificacion del objeto de dominio
     */
    private String cdIdentificacion;

    /**
     * Representa el campo convocatoriaAct del objeto de dominio
     */
    private ConvocatoriaAct convocatoriaAct;

    /**
     * Representa el campo dsDescripcion del objeto de dominio
     */
    private String dsDescripcion;

    /**
     * Representa el campo itGeneraImpreso del objeto de dominio
     */
    private String itGeneraImpreso;

    /**
     * Representa el campo dsImpresoOriginal del objeto de dominio
     */
    private String dsImpresoOriginal;

    /**
     * Representa el campo dsPlantillaTecnica del objeto de dominio
     */
    private String dsPlantillaTecnica;

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
    private Convocatoria convocatoria;

    /**
     *
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Bloque> lstBloque;

    /**
     *
     */
    private Integer cdFicaImpreso;

    /**
     *
     */
    private Integer cdFicaPlantilla;

    /* Represen el id de la imagen relacionado al impreso */
    private Imagen imagenLogo;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Impreso() {
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
        return new HashCodeBuilder().append(this.idImpreso).append(this.tipo)
                .append(this.cdIdentificacion).append(this.dsDescripcion)
                .append(this.convocatoriaAct).append(this.convocatoria)
                .append(this.itGeneraImpreso).append(this.dsImpresoOriginal)
                .append(this.lstBloque).append(cdFicaImpreso).append(cdFicaPlantilla).append(imagenLogo)
                .append(this.dsPlantillaTecnica).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof Impreso)) {
            return false;
        }
        Impreso castObj = (Impreso) obj;
        return new EqualsBuilder().append(this.idImpreso, castObj.idImpreso)
                .append(this.tipo, castObj.tipo)
                .append(this.cdIdentificacion, castObj.cdIdentificacion)
                .append(this.dsDescripcion, castObj.dsDescripcion)
                .append(this.convocatoriaAct, castObj.convocatoriaAct)
                .append(this.itGeneraImpreso, castObj.itGeneraImpreso)
                .append(this.dsImpresoOriginal, castObj.dsImpresoOriginal)
                .append(this.dsPlantillaTecnica, castObj.dsPlantillaTecnica)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.convocatoria, castObj.convocatoria)
                .append(this.lstBloque, castObj.lstBloque)
                .append(this.cdFicaImpreso, castObj.cdFicaImpreso)
                .append(this.cdFicaPlantilla, castObj.cdFicaPlantilla)
                .append(this.imagenLogo, castObj.imagenLogo).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idImpreso
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_IMPRESO")
    @SequenceGenerator(name = "SAII_S_IMPRESO", sequenceName = "SAII_S_IMPRESO", allocationSize = 1)
    @Column(name = "ID_IMPRESO")
    public Integer getIdImpreso() {
        return this.idImpreso;
    }

    /**
     * Establece el valor del campo idImpreso de la entidad Impreso
     *
     * @param idImpreso El valor a establecer
     */
    public void setIdImpreso(Integer idImpreso) {
        this.idImpreso = idImpreso;
    }

    /**
     * @return Devuelve el valor del campo itTipo
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO", nullable = true)
    public CatalogosValores getTipo() {
        return this.tipo;
    }

    /**
     * Establece el valor del campo itTipo de la entidad Impreso
     *
     * @param itTipo El valor a establecer
     */
    public void setTipo(CatalogosValores tipo) {
        this.tipo = tipo;
    }

    /** Imagenes **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IMAGEN_LOGO", nullable = true)
    public Imagen getImagenLogo() {
        return this.imagenLogo;
    }

    public void setImagenLogo(Imagen imagenLogo) {
        this.imagenLogo = imagenLogo;
    }

    /**
     * @return Devuelve el valor del campo cdIdentificacion
     */

    @Column(name = "CD_IDENTIFICACION", nullable = true)
    public String getCdIdentificacion() {
        return this.cdIdentificacion;
    }

    /**
     * Establece el valor del campo cdIdentificacion de la entidad Impreso
     *
     * @param cdIdentificacion El valor a establecer
     */
    public void setCdIdentificacion(String cdIdentificacion) {
        this.cdIdentificacion = cdIdentificacion;
    }

    /**
     * @return the convocatoriaAct
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACTOR", nullable = true)
    public ConvocatoriaAct getConvocatoriaAct() {
        return this.convocatoriaAct;
    }

    /**
     * Establece el valor del campo convocatoriaAct de la entidad Participante
     *
     * @param convocatoriaAct El valor a establecer
     */
    public void setConvocatoriaAct(ConvocatoriaAct convocatoriaAct) {
        this.convocatoriaAct = convocatoriaAct;
    }

    /**
     * @return Devuelve el valor del campo dsDescripcion
     */

    @Column(name = "DS_DESCRIPCION", nullable = true)
    public String getDsDescripcion() {
        return this.dsDescripcion;
    }

    /**
     * Establece el valor del campo dsDescripcion de la entidad Impreso
     *
     * @param dsDescripcion El valor a establecer
     */
    public void setDsDescripcion(String dsDescripcion) {
        this.dsDescripcion = dsDescripcion;
    }

    /**
     * @return Devuelve el valor del campo itGeneraImpreso
     */

    @Column(name = "IT_GENERA_IMPRESO", nullable = true)
    public String getItGeneraImpreso() {
        return this.itGeneraImpreso;
    }

    /**
     * Establece el valor del campo itGeneraImpreso de la entidad Impreso
     *
     * @param itGeneraImpreso El valor a establecer
     */
    public void setItGeneraImpreso(String itGeneraImpreso) {
        this.itGeneraImpreso = itGeneraImpreso;
    }

    /**
     * @return Devuelve el valor del campo dsImpresoOriginal
     */

    @Column(name = "DS_IMPRESO_ORIGINAL", nullable = true)
    public String getDsImpresoOriginal() {
        return this.dsImpresoOriginal;
    }

    /**
     * Establece el valor del campo dsImpresoOriginal de la entidad Impreso
     *
     * @param dsImpresoOriginal El valor a establecer
     */
    public void setDsImpresoOriginal(String dsImpresoOriginal) {
        this.dsImpresoOriginal = dsImpresoOriginal;
    }

    /**
     * @return Devuelve el valor del campo dsPlantillaTecnica
     */

    @Column(name = "DS_PLANTILLA_TECNICA", nullable = true)
    public String getDsPlantillaTecnica() {
        return this.dsPlantillaTecnica;
    }

    /**
     * Establece el valor del campo dsPlantillaTecnica de la entidad Impreso
     *
     * @param dsPlantillaTecnica El valor a establecer
     */
    public void setDsPlantillaTecnica(String dsPlantillaTecnica) {
        this.dsPlantillaTecnica = dsPlantillaTecnica;
    }

    /**
     * @return the convocatoria
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONVOCATORIA", nullable = false)
    public Convocatoria getConvocatoria() {
        return this.convocatoria;
    }

    /**
     * @param convocatoria the Convocatoria to set
     */
    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "impreso")
    public List<Bloque> getLstBloque() {
        return this.lstBloque;
    }

    public void setLstBloque(List<Bloque> lstBloque) {
        this.lstBloque = lstBloque;
    }

    @Column(name = "CD_FICA_IMPRESO", nullable = true)
    public Integer getCdFicaImpreso() {
        return this.cdFicaImpreso;
    }

    public void setCdFicaImpreso(Integer cdFicaImpreso) {
        this.cdFicaImpreso = cdFicaImpreso;
    }

    @Column(name = "CD_FICA_PLANTILLA", nullable = true)
    public Integer getCdFicaPlantilla() {
        return this.cdFicaPlantilla;
    }

    public void setCdFicaPlantilla(Integer cdFicaPlantilla) {
        this.cdFicaPlantilla = cdFicaPlantilla;
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
}
