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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.ToString;

/**
 * Representa el objeto de dominio Evaluacion
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EVALUACION")
@AtlasAuditarFila
@ToString
public class Evaluacion implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idEvaluacion del objeto de dominio
     */
    private Integer idEvaluacion;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo dsTitulo del objeto de dominio
     */
    private String dsTitulo;

    /**
     * Representa el campo tlDescripcion del objeto de dominio
     */
    private String tlDescripcion;

    /**
     * Representa el campo nmOrden del objeto de dominio
     */
    private Integer nmOrden;

    /**
     * Representa el campo itCalificacion del objeto de dominio
     */
    private String itCalificacion;

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
     * Lista EvaluacionNotaDTO
     */
    private List<EvaluacionNota> listaEvaluacionNota;

    /**
     * Lista ApartadoDTO
     */
    private List<Apartado> listaApartado;

    /**
     * Representa el campo itCoorEval del objeto de dominio
     */
    private String itCoorEval;

    /* Representa el id de la imagen relacionada a la evaluación */
    private Imagen imagenLogo;
    
    /* Representa el id del informe */
    private Informe informe;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Evaluacion() {
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
        return new HashCodeBuilder().append(this.idEvaluacion).append(this.idConvocatoria)
                .append(this.dsTitulo).append(this.tlDescripcion).append(this.nmOrden)
                .append(this.itCalificacion).append(this.cdUsuAlta).append(this.fcAltaFila)
                .append(this.cdUsuModif).append(this.fcModifFila).append(this.cdUsuBaja)
                .append(this.fcBajaFila).append(this.itCoorEval).toHashCode();
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
        if (!(obj instanceof Evaluacion)) {
            return false;
        }
        Evaluacion castObj = (Evaluacion) obj;
        return new EqualsBuilder().append(this.idEvaluacion, castObj.idEvaluacion)
                .append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.dsTitulo, castObj.dsTitulo)
                .append(this.tlDescripcion, castObj.tlDescripcion)
                .append(this.nmOrden, castObj.nmOrden)
                .append(this.itCalificacion, castObj.itCalificacion)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.itCoorEval, castObj.itCoorEval).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idEvaluacion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EVALUACION")
    @SequenceGenerator(name = "SAII_S_EVALUACION", sequenceName = "SAII_S_EVALUACION", allocationSize = 1)
    @Column(name = "ID_EVALUACION")
    public Integer getIdEvaluacion() {
        return this.idEvaluacion;
    }

    /**
     * Establece el valor del campo idEvaluacion de la entidad Evaluacion
     *
     * @param idEvaluacion El valor a establecer
     */
    public void setIdEvaluacion(Integer idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad Evaluacion
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo dsTitulo
     */
    @Column(name = "DS_TITULO")
    public String getDsTitulo() {
        return this.dsTitulo;
    }

    /**
     * Establece el valor del campo dsTitulo de la entidad Evaluacion
     *
     * @param dsTitulo El valor a establecer
     */
    public void setDsTitulo(String dsTitulo) {
        this.dsTitulo = dsTitulo;
    }

    /**
     * @return Devuelve el valor del campo tlDescripcion
     */
    @Column(name = "TL_DESCRIPCION", nullable = true)
    public String getTlDescripcion() {
        return this.tlDescripcion;
    }

    /**
     * Establece el valor del campo tlDescripcion de la entidad Evaluacion
     *
     * @param tlDescripcion El valor a establecer
     */
    public void setTlDescripcion(String tlDescripcion) {
        this.tlDescripcion = tlDescripcion;
    }

    /**
     * @return Devuelve el valor del campo nmOrden
     */
    @Column(name = "NM_ORDEN")
    public Integer getNmOrden() {
        return this.nmOrden;
    }

    /**
     * Establece el valor del campo nmOrden de la entidad Evaluacion
     *
     * @param nmOrden El valor a establecer
     */
    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
    }

    /**
     * @return Devuelve el valor del campo itCalificacion
     */
    @Column(name = "IT_CALIFICACION")
    public String getItCalificacion() {
        return this.itCalificacion;
    }

    /**
     * Establece el valor del campo itCalificacion de la entidad Evaluacion
     *
     * @param itCalificacion El valor a establecer
     */
    public void setItCalificacion(String itCalificacion) {
        this.itCalificacion = itCalificacion;
    }

    /**
     * @return Devuelve el valor del campo itCoorEval
     */
    @Column(name = "IT_COOR_EVAL")
    public String getItCoorEval() {
        return this.itCoorEval;
    }

    /**
     * Establece el valor del campo itCoorEval de la entidad Evaluacion
     *
     * @param itCoorEval El valor a establecer
     */
    public void setItCoorEval(String itCoorEval) {
        this.itCoorEval = itCoorEval;
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
     * @return the informe
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFORME", nullable = true)
    public Informe getInforme() {
        return informe;
    }

    /**
     * @param informe the informe to set
     */
    public void setInforme(Informe informe) {
        this.informe = informe;
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

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// INICIO LISTAS
    // ////////////////////////////////////////////////////////

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idEvaluacion", cascade = CascadeType.ALL)
    public List<EvaluacionNota> getListaEvaluacionNota() {
        return this.listaEvaluacionNota;
    }

    public void setListaEvaluacionNota(List<EvaluacionNota> listaEvaluacionNota) {
        this.listaEvaluacionNota = listaEvaluacionNota;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idEvaluacion", cascade = CascadeType.ALL)
    @OrderBy("nmOrden ASC")
    public List<Apartado> getListaApartado() {
        return this.listaApartado;
    }

    public void setListaApartado(List<Apartado> listaApartado) {
        this.listaApartado = listaApartado;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// FIN LISTAS
    // ////////////////////////////////////////////////////////
}
