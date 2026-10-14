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
import javax.persistence.Lob;
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
 * Representa el objeto de dominio Convocatoria
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CONVOCATORIA")
@AtlasAuditarFila
@ToString
public class Convocatoria implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo cdAcronimo del objeto de dominio
     */
    private String cdAcronimo;

    /**
     * Representa el campo cdTitulo del objeto de dominio
     */
    private String cdTitulo;

    /**
     * Representa el campo dsDescripcion del objeto de dominio
     */
    private String dsDescripcion;

    /**
     * Representa el campo itConvocatoriaActiva del objeto de dominio
     */
    private String itConvocatoriaActiva;

    /**
     * Representa el campo nmDurMaxAyuda del objeto de dominio
     */
    private Integer nmDurMaxAyuda;

    /**
     * Representa el campo nmImportePresAnualidad del objeto de dominio
     */
    private Double nmImportePresAnualidad;

    /**
     * Representa el campo nmImportePresTotal del objeto de dominio
     */
    private Double nmImportePresTotal;

    /**
     * Representa el campo itInicioSolOrg del objeto de dominio
     */
    private String itInicioSolOrg;

    /**
     * Representa el campo fcIniPresentacion del objeto de dominio
     */
    private Date fcIniPresentacion;

    /**
     * Representa el campo fcFinPresentacion del objeto de dominio
     */
    private Date fcFinPresentacion;

    /**
     * Representa el campo itPresFueraPlazo del objeto de dominio
     */
    private String itPresFueraPlazo;

    /**
     * Representa el campo nmDiasSubsanacion del objeto de dominio
     */
    private Integer nmDiasSubsanacion;
    
    /**
     * Representa el campo nmDiasHabilesAudiencia del objeto de dominio
     */
    private Integer nmDiasHabilesAudiencia;
    
    /**
     * Representa el campo nmDiasLimmodif del objeto de dominio
     */
    private Integer nmDiasLimmodif;

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
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValores idEstado;

    /**
     * Representa el campo idLineaAyuda del objeto de dominio
     */
    private CatalogosValores idLineaAyuda;

    /**
     * Representa el campo idDireccion del objeto de dominio
     */
    private UnidadAdmin idDireccion;

    /**
     * Representa el campo idSubdireccion del objeto de dominio
     */
    private UnidadAdmin idSubdireccion;

    /**
     * Representa el campo idAreaAdministrativa del objeto de dominio
     */
    private UnidadAdmin idAreaAdministrativa;

    /**
     * Representa el campo idModalidadConvocatoria del objeto de dominio
     */
    private CatalogosValores idModalidadConvocatoria;

    /**
     * Representa el campo idTipoModalidad del objeto de dominio
     */
    private CatalogosValores idTipoModalidad;

    /**
     * Representa el campo nmAno del objeto de dominio
     */
    private Integer nmAno;

    /**
     * Representa el campo itFondosEuropa del objeto de dominio
     */
    private String itFondosEuropa;

    /**
     * Representa el campo itRequisitos del objeto de dominio
     */
    private String itRequisitos;

    /**
     * Representa el campo fcIniTramiEval del objeto de dominio
     */
    private Date fcIniTramiEval;

    /**
     * Representa el campo fcFinTramiEval del objeto de dominio
     */
    private Date fcFinTramiEval;

    /**
     * Representa el campo fcIniAlegaciones del objeto de dominio
     */
    private Date fcIniAlegaciones;

    /**
     * Representa el campo fcFinAlegaciones del objeto de dominio
     */
    private Date fcFinAlegaciones;

    /**
     * Representa el campo fcTraComisionEval del objeto de dominio
     */
    private Date fcTraComisionEval;

    /**
     * Representa el campo fcTraPropResDef del objeto de dominio
     */
    private Date fcTraPropResDef;

    /**
     * Representa el campo fcTraResolucionDef del objeto de dominio
     */
    private Date fcTraResolucionDef;

    /**
     * Representa el campo nmCalificacionMax del objeto de dominio
     */
    private Integer nmCalificacionMax;

    /**
     * Representa la fecha de inicio de contrato a nivel convocatoria
     */
    private Date fcIniContrato;

    /**
     * Representa la fecha de fin de contrato a nivel convocatoria
     */
    private Date fcFinContrato;
    
    /**
     * Representa el campo ID_UNIDAD_ADMIN_JCT del objeto de dominio
     */
    private UnidadAdmin idAreaAdministrativaJCT;
    
    /**
     * Representa el campo itContratos del objeto de dominio
     */
    private String itContratos;
    
    /**
     * Representa el campo itPartidasPresupuestarias del objeto de dominio
     */
    private String itPartidasPresupuestarias;
    
    /**
     * Representa el campo itActasComitesGestion del objeto de dominio
     */
    private String itActasComitesGestion;
    
    /**
     * Representa el campo itAgotarFondos del objeto de dominio
     */
    private String itAgotarFondos;
    
    /**
     * Representa el campo itAbiertaPermanente del objeto de dominio
     */
    private String itAbiertaPermanente;
    
    
    /**
     * Representa el indicador si la convocatoria tiene itfondosAgotados
     */
    private String itfondosAgotados;
    
    /**
     * Representa el indicador si la convocatoria tiene itConvocatoriaCerrada
     */
    private String itConvocatoriaCerrada;
    
    
    /**
     * Representa el indicador si la convocatoria tiene itEvaluacion
     */
    private String itEvaluacion;
    
    /**
     * Representa el indicador si la convocatoria tiene itAlegacion
     */
    private String itAlegacion;
    
    
    /**
     * Representa el campo nmMaxPeriodos del objeto de dominio
     */
    private Integer nmMaxPeriodos;
    
    
    /**
     * Representa el campo nmDiasAmpliContrato del objeto de dominio
     */
    private Integer nmDiasAmpliContrato;
    
    /**
     * Representa el campo porcentajeIntensidadAyuda del objeto de dominio
     */
    private  Double porcentajeIntensidadAyuda;
    
    /**
     * Representa el campo idTipoSolicitud del objeto de dominio
     */
    private CatalogosValores idTipoSolicitud;
    
    
    /**
     * Representa el campo idTipoSolicitud del objeto de dominio
     */
    private CatalogosValores idTipoConcesion;
    
    /**
     * Representa el indicador si la convocatoria tiene detalle de Presupuestos
     */
    private String itConceptoPresup;
    
    /**
     * Representa el indicador si si las fechas se han trasladado a los expedientes
     */
    private String itTrasladoFechasCont;
  
    /**
     * Campo fcTraPropResProv
     */
    private Date fcTraPropResProv;
    
    /**
     * Campo fcTraResolucionProv
     */

    private Date fcTraResolucionProv;
    
    /**
     * Representa el indicador si la convocatoria tiene 
     * prespuestos maximos por anualidad o por partida presupuestaria
     */
    private String itImporteMaxAnual;
    
    /**
     * Campo instruciones
     */
    private String instrucciones;
    
    
    /**
     * Representa el campo nmMaxPeriodos del objeto de dominio
     */
    private Integer idEstadoResolucion;
    
    /**
     * Representa el indicador si la convocatoria tiene 
     * prespuestos maximos por expediente/actor
     */
    private String itImporteMaxExpdte;
    
    /**
     * Representa el campo itDetalleJusEco del objeto de dominio
     */
    private String itDetalleJusEco;
    
    /**
     * Campo informacion sobre proteccion de datos
     */
    private String informacionRgpd;
    
    /**
     * Campo idAceptacionDesismientoAyuda
     */
    private CatalogosValores idAceptacionDesistimientoAyuda;
    
    /**
     * Campo entidades cerradas
     */
    private String itEntidadCerrada;
    
    /**
     * Representa el indicador si la convocatoria tiene itAyudaCalculada 
     */
    private String itAyudaCalculada;
    
    /**
     * Representa el indicador de Resolución Provisional de Concesión/Denegación (S/N)
     */
    private String itResProvConcDeneg;

    /**
     * Representa el indicador de Resolución Provisional de Exclusión (S/N)
     */
    private String itResProvExclusion;

    /**
     * Representa el indicador de Resolución Provisional de Desistimiento (S/N)
     */
    private String itResProvDesistimiento;
    
    
    

    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Convocatoria() {
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
        return new HashCodeBuilder().append(this.idConvocatoria).append(this.cdAcronimo)
                .append(this.cdTitulo).append(this.dsDescripcion).append(this.itConvocatoriaActiva)
                .append(this.nmDurMaxAyuda).append(this.nmImportePresAnualidad).append(this.nmImportePresTotal)
                .append(this.itInicioSolOrg).append(this.fcIniPresentacion)
                .append(this.fcFinPresentacion).append(this.fcIniTramiEval)
                .append(this.fcFinTramiEval).append(this.fcIniAlegaciones)               
                .append(this.fcFinAlegaciones).append(this.itPresFueraPlazo)
                .append(this.nmDiasSubsanacion).append(this.nmDiasHabilesAudiencia)
                .append(this.nmDiasLimmodif)
                .append(this.idTipoModalidad).append(this.idModalidadConvocatoria).append(this.idEstado)
                .append(this.idTipoSolicitud).append(this.idTipoConcesion)
                .append(this.fcTraComisionEval).append(this.fcTraPropResDef)
                .append(this.fcTraResolucionDef).append(this.nmCalificacionMax).append(this.idLineaAyuda)
                .append(this.idDireccion).append(this.idSubdireccion).append(this.idAreaAdministrativa)
                .append(this.fcIniContrato).append(this.fcFinContrato).append(this.cdUsuAlta)
                .append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
                .append(this.cdUsuBaja).append(this.fcBajaFila).append(this.nmAno)
                .append(this.itFondosEuropa).append(this.itRequisitos).append(this.itContratos)
                .append(this.itPartidasPresupuestarias).append(this.itActasComitesGestion)
                .append(this.itfondosAgotados).append(this.itAbiertaPermanente)
                .append(this.itAgotarFondos).append(this.itConvocatoriaCerrada)
                .append(this.itEvaluacion).append(this.itEvaluacion)
                .append(this.itAlegacion).append(this.idAreaAdministrativaJCT)
                .append(this.nmDiasAmpliContrato).append(this.nmDiasAmpliContrato)
                .append(this.nmMaxPeriodos).append(this.porcentajeIntensidadAyuda)
                .append(this.fcTraPropResProv).append(this.fcTraResolucionProv)
                .append(this.itImporteMaxAnual).append(this.itImporteMaxExpdte)
                .append(this.itDetalleJusEco).append(this.idAceptacionDesistimientoAyuda)
                .append(this.itEntidadCerrada).append(this.itAyudaCalculada)
                .append(this.itResProvConcDeneg).append(this.itResProvExclusion).append(this.itResProvDesistimiento)
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
        if (!(obj instanceof Convocatoria)) {
            return false;
        }
        Convocatoria castObj = (Convocatoria) obj;
        return new EqualsBuilder().append(this.idConvocatoria, castObj.idConvocatoria)
                .append(this.cdAcronimo, castObj.cdAcronimo).append(this.cdTitulo, castObj.cdTitulo)
                .append(this.dsDescripcion, castObj.dsDescripcion)
                .append(this.itConvocatoriaActiva, castObj.itConvocatoriaActiva)
                .append(this.nmDurMaxAyuda, castObj.nmDurMaxAyuda)
                .append(this.nmImportePresAnualidad, castObj.nmImportePresAnualidad)
                .append(this.itInicioSolOrg, castObj.itInicioSolOrg)
                .append(this.fcIniPresentacion, castObj.fcIniPresentacion)
                .append(this.fcFinPresentacion, castObj.fcFinPresentacion)
                .append(this.fcIniTramiEval, castObj.fcIniTramiEval)
                .append(this.fcFinTramiEval, castObj.fcFinTramiEval)
                .append(this.fcIniAlegaciones, castObj.fcIniAlegaciones)
                .append(this.fcFinAlegaciones, castObj.fcFinAlegaciones)
                .append(this.itPresFueraPlazo, castObj.itPresFueraPlazo)
                .append(this.nmDiasSubsanacion, castObj.nmDiasSubsanacion)
                .append(this.nmDiasHabilesAudiencia, castObj.nmDiasHabilesAudiencia)
                .append(this.nmDiasLimmodif, castObj.nmDiasLimmodif)
                .append(this.idEstado, castObj.idEstado)
                .append(this.idTipoSolicitud, castObj.idTipoSolicitud)
                .append(this.idTipoConcesion, castObj.idTipoConcesion)
                .append(this.idLineaAyuda, castObj.idLineaAyuda)
                .append(this.idDireccion, castObj.idDireccion)
                .append(this.idSubdireccion, castObj.idSubdireccion)
                .append(this.idAreaAdministrativa, castObj.idAreaAdministrativa)
                .append(this.idAreaAdministrativaJCT, castObj.idAreaAdministrativaJCT)
                .append(this.idModalidadConvocatoria, castObj.idModalidadConvocatoria)
                .append(this.idTipoModalidad, castObj.idTipoModalidad)
                .append(this.fcTraComisionEval, castObj.fcTraComisionEval)
                .append(this.fcTraPropResDef, castObj.fcTraPropResDef)
                .append(this.fcTraResolucionDef, castObj.fcTraResolucionDef)
                .append(this.nmCalificacionMax, castObj.nmCalificacionMax)
                .append(this.fcIniContrato, castObj.fcIniContrato)
                .append(this.fcFinContrato, castObj.fcFinContrato)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.nmImportePresTotal, castObj.nmImportePresTotal)
                .append(this.nmAno, castObj.nmAno)
                .append(this.itFondosEuropa, castObj.itFondosEuropa)
                .append(this.itRequisitos, castObj.itRequisitos)
                .append(this.itContratos, castObj.itContratos)
                .append(this.itPartidasPresupuestarias, castObj.itPartidasPresupuestarias)
                .append(this.itActasComitesGestion, castObj.itActasComitesGestion)
                .append(this.itAgotarFondos, castObj.itAgotarFondos)
                .append(this.itfondosAgotados, castObj.itfondosAgotados)
                .append(this.itConvocatoriaCerrada, castObj.itConvocatoriaCerrada)
                .append(this.itEvaluacion, castObj.itEvaluacion)
                .append(this.itAlegacion, castObj.itAlegacion)
                .append(this.itConceptoPresup, castObj.itConceptoPresup)
                .append(this.itAbiertaPermanente, castObj.itAbiertaPermanente)
                .append(this.nmDiasAmpliContrato, castObj.nmDiasAmpliContrato)
                .append(this.nmMaxPeriodos, castObj.nmMaxPeriodos)
                .append(this.porcentajeIntensidadAyuda, castObj.porcentajeIntensidadAyuda)
                .append(this.fcTraPropResProv, castObj.fcTraPropResProv)
                .append(this.fcTraResolucionProv, castObj.fcTraResolucionProv)
                .append(this.itImporteMaxAnual, castObj.itImporteMaxAnual)
                .append(this.itImporteMaxExpdte, castObj.itImporteMaxExpdte)
                .append(this.itDetalleJusEco, castObj.itDetalleJusEco)
                .append(this.idAceptacionDesistimientoAyuda, castObj.idAceptacionDesistimientoAyuda)
                .append(this.itEntidadCerrada, castObj.itEntidadCerrada)
                .append(this.itAyudaCalculada, castObj.itAyudaCalculada)
                .append(this.itResProvConcDeneg, castObj.itResProvConcDeneg)
                .append(this.itResProvExclusion, castObj.itResProvExclusion)
                .append(this.itResProvDesistimiento, castObj.itResProvDesistimiento)
                .isEquals();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idConvocatoria
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CONVOCATORIA")
    @SequenceGenerator(name = "SAII_S_CONVOCATORIA", sequenceName = "SAII_S_CONVOCATORIA", allocationSize = 1)
    @Column(name = "ID_CONVOCATORIA")
    public Integer getIdConvocatoria() {
        return this.idConvocatoria;
    }

    /**
     * Establece el valor del campo idConvocatoria de la entidad Convocatorias
     *
     * @param idConvocatoria El valor a establecer
     */
    public void setIdConvocatoria(Integer idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    /**
     * @return Devuelve el valor del campo cdAcronimo
     */

    @Column(name = "CD_ACRONIMO", nullable = true)
    public String getCdAcronimo() {
        return this.cdAcronimo;
    }

    /**
     * Establece el valor del campo cdAcronimo de la entidad Convocatorias
     *
     * @param cdAcronimo El valor a establecer
     */
    public void setCdAcronimo(String cdAcronimo) {
        this.cdAcronimo = cdAcronimo;
    }

    /**
     * @return Devuelve el valor del campo cdTitulo
     */

    @Column(name = "CD_TITULO", nullable = true)
    public String getCdTitulo() {
        return this.cdTitulo;
    }

    /**
     * Establece el valor del campo cdTitulo de la entidad Convocatorias
     *
     * @param cdTitulo El valor a establecer
     */
    public void setCdTitulo(String cdTitulo) {
        this.cdTitulo = cdTitulo;
    }

    /**
     * @return Devuelve el valor del campo dsDescripcion
     */

    @Column(name = "DS_DESCRIPCION", nullable = true)
    public String getDsDescripcion() {
        return this.dsDescripcion;
    }

    /**
     * Establece el valor del campo dsDescripcion de la entidad Convocatorias
     *
     * @param dsDescripcion El valor a establecer
     */
    public void setDsDescripcion(String dsDescripcion) {
        this.dsDescripcion = dsDescripcion;
    }

    /**
     * @return Devuelve el valor del campo itConvocatoriaActiva
     */

    @Column(name = "IT_CONVOCATORIA_ACTIVA", nullable = true)
    public String getItConvocatoriaActiva() {
        return this.itConvocatoriaActiva;
    }

    /**
     * Establece el valor del campo itConvocatoriaActiva de la entidad
     * Convocatorias
     *
     * @param itConvocatoriaActiva El valor a establecer
     */
    public void setItConvocatoriaActiva(String itConvocatoriaActiva) {
        this.itConvocatoriaActiva = itConvocatoriaActiva;
    }

    /**
     * @return Devuelve el valor del campo nmDurMaxAyuda
     */

    @Column(name = "NM_DUR_MAX_AYUDA", nullable = true)
    public Integer getNmDurMaxAyuda() {
        return this.nmDurMaxAyuda;
    }

    /**
     * Establece el valor del campo nmDurMaxAyuda de la entidad Convocatorias
     *
     * @param nmDurMaxAyuda El valor a establecer
     */
    public void setNmDurMaxAyuda(Integer nmDurMaxAyuda) {
        this.nmDurMaxAyuda = nmDurMaxAyuda;
    }

    /**
     * @return Devuelve el valor del campo nmImportePresTotal
     */

    @Column(name = "NM_IMPORTE_PRES_ANUALIDAD", nullable = true)
    public Double getNmImportePresAnualidad() {
        return this.nmImportePresAnualidad;
    }

    /**
     * Establece el valor del campo nmImportePresAnualidad de la entidad
     * Convocatorias
     *
     * @param nmImportePresAnualidad El valor a establecer
     */
    public void setNmImportePresAnualidad(Double nmImportePresAnualidad) {
        this.nmImportePresAnualidad = nmImportePresAnualidad;
    }

    /**
     * Establece el valor del campo nmImportePresAnualidad de la entidad
     * Convocatorias
     *
     * @param nmImportePresAnualidad El valor a establecer
     */
    public void setNmImportePresTotal(Double nmImportePresTotal) {
        this.nmImportePresTotal = nmImportePresTotal;
    }

    /**
     * @return Devuelve el valor del campo nmImportePresTotal
     */

    @Column(name = "NM_IMPORTE_PRES_TOTAL", nullable = true)
    public Double getNmImportePresTotal() {
        return this.nmImportePresTotal;
    }

    /**
     * @return Devuelve el valor del campo itInicioSolOrg
     */

    @Column(name = "IT_INICIO_SOL_ORG", nullable = true)
    public String getItInicioSolOrg() {
        return this.itInicioSolOrg;
    }

    /**
     * Establece el valor del campo itInicioSolOrg de la entidad Convocatorias
     *
     * @param itInicioSolOrg El valor a establecer
     */
    public void setItInicioSolOrg(String itInicioSolOrg) {
        this.itInicioSolOrg = itInicioSolOrg;
    }

    /**
     * @return Devuelve el valor del campo fcIniPresentacion
     */

    @Column(name = "FC_INI_PRESENTACION", nullable = true)
    public Date getFcIniPresentacion() {
        return this.fcIniPresentacion;
    }

    /**
     * Establece el valor del campo fcIniPresentacion de la entidad
     * Convocatorias
     *
     * @param fcIniPresentacion El valor a establecer
     */
    public void setFcIniPresentacion(Date fcIniPresentacion) {
        this.fcIniPresentacion = fcIniPresentacion;
    }

    /**
     * @return Devuelve el valor del campo fcFinPresentacion
     */

    @Column(name = "FC_FIN_PRESENTACION", nullable = true)
    public Date getFcFinPresentacion() {
        return this.fcFinPresentacion;
    }

    /**
     * Establece el valor del campo fcFinPresentacion de la entidad
     * Convocatorias
     *
     * @param fcFinPresentacion El valor a establecer
     */
    public void setFcFinPresentacion(Date fcFinPresentacion) {
        this.fcFinPresentacion = fcFinPresentacion;
    }

    /**
     * @return Devuelve el valor del campo itPresFueraPlazo
     */

    @Column(name = "IT_PRES_FUERA_PLAZO", nullable = true)
    public String getItPresFueraPlazo() {
        return this.itPresFueraPlazo;
    }

    /**
     * Establece el valor del campo itPresFueraPlazo de la entidad Convocatorias
     *
     * @param itPresFueraPlazo El valor a establecer
     */
    public void setItPresFueraPlazo(String itPresFueraPlazo) {
        this.itPresFueraPlazo = itPresFueraPlazo;
    }

    /**
     * @return the idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    public CatalogosValores getIdEstado() {
        return this.idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(CatalogosValores idEstado) {
        this.idEstado = idEstado;
    }

    
    
    /**
     * @return the idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_SOLICITUD", nullable = false)
    public CatalogosValores getIdTipoSolicitud() {
        return this.idTipoSolicitud;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdTipoSolicitud(CatalogosValores idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    
    /**
     * @return the idEstado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CONCESION", nullable = false)
    public CatalogosValores getIdTipoConcesion() {
        return this.idTipoConcesion;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdTipoConcesion(CatalogosValores idTipoConcesion) {
        this.idTipoConcesion = idTipoConcesion;
    }
    
    
    /**
     * @return the idLineaAyuda
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LINEA_AYUDA", nullable = true)
    public CatalogosValores getIdLineaAyuda() {
        return this.idLineaAyuda;
    }

    /**
     * @param idLineaAyuda the idLineaAyuda to set
     */
    public void setIdLineaAyuda(CatalogosValores idLineaAyuda) {
        this.idLineaAyuda = idLineaAyuda;
    }

    /**
     * @return the idDireccion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DIRECCION", nullable = true)
    public UnidadAdmin getIdDireccion() {
        return this.idDireccion;
    }

    /**
     * @param idSubdireccion the idSubdireccion to set
     */
    public void setIdDireccion(UnidadAdmin idDireccion) {
        this.idDireccion = idDireccion;
    }

    /**
     * @return the idSubdireccion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBDIRECCION", nullable = true)
    public UnidadAdmin getIdSubdireccion() {
        return this.idSubdireccion;
    }

    /**
     * @param idSubdireccion the idSubdireccion to set
     */
    public void setIdSubdireccion(UnidadAdmin idSubdireccion) {
        this.idSubdireccion = idSubdireccion;
    }

    /**
     * @return the idAreaAdministrativa
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UNIDAD_ADMIN", nullable = true)
    public UnidadAdmin getIdAreaAdministrativa() {
        return this.idAreaAdministrativa;
    }

    /**
     * @param idAreaAdministrativa the idAreaAdministrativa to set
     */
    public void setIdAreaAdministrativa(UnidadAdmin idAreaAdministrativa) {
        this.idAreaAdministrativa = idAreaAdministrativa;
    }

    /**
     * @return the idModalidadConvocatoria
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODALIDAD_CONVOCATORIA", nullable = true)
    public CatalogosValores getIdModalidadConvocatoria() {
        return this.idModalidadConvocatoria;
    }

    /**
     * @param idModalidadConvocatoria the idModalidadConvocatoria to set
     */
    public void setIdModalidadConvocatoria(CatalogosValores idModalidadConvocatoria) {
        this.idModalidadConvocatoria = idModalidadConvocatoria;
    }

    /**
     * @return the idTipoModalidad
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_MODALIDAD", nullable = true)
    public CatalogosValores getIdTipoModalidad() {
        return this.idTipoModalidad;
    }

    /**
     * @param idTipoModalidad the idTipoModalidad to set
     */
    public void setIdTipoModalidad(CatalogosValores idTipoModalidad) {
        this.idTipoModalidad = idTipoModalidad;
    }

    /**
     * @return Devuelve el valor del campo nmAno
     */

    @Column(name = "NM_ANO", nullable = true)
    public Integer getNmAno() {
        return this.nmAno;
    }

    /**
     * Establece el valor del campo nmAno de la entidad Convocatorias
     *
     * @param nmAno El valor a establecer
     */
    public void setNmAno(Integer nmAno) {
        this.nmAno = nmAno;
    }

    /**
     * @return Devuelve el valor del campo itFondosEuropa
     */

    @Column(name = "IT_FONDOS_EUROPA", nullable = true)
    public String getItFondosEuropa() {
        return this.itFondosEuropa;
    }

    /**
     * Establece el valor del campo itFondosEuropa de la entidad Convocatorias
     *
     * @param itFondosEuropa El valor a establecer
     */
    public void setItFondosEuropa(String itFondosEuropa) {
        this.itFondosEuropa = itFondosEuropa;
    }

    /**
     * @return Devuelve el valor del campo nmDiasSubsanacion
     */
    @Column(name = "NM_DIAS_SUBSANACION", nullable = true)
    public Integer getNmDiasSubsanacion() {
        return this.nmDiasSubsanacion;
    }

    /**
     * Establece el valor del campo nmDiasSubsanacion de la entidad
     * Convocatorias
     *
     * @param nmAno El valor a establecer
     */
    public void setNmDiasSubsanacion(Integer nmDiasSubsanacion) {
        this.nmDiasSubsanacion = nmDiasSubsanacion;
    }
    
    /**
     * @return Devuelve el valor del campo nmDiasHabilesAudiencia
     */
    @Column(name = "NM_DIAS_HABILES_AUDIENCIA", nullable = true)
    public Integer getNmDiasHabilesAudiencia() {
        return this.nmDiasHabilesAudiencia;
    }

    /**
     * Establece el valor del campo nmDiasHabilesAudiencia de la entidad
     * Convocatorias
     *
     * @param nmAno El valor a establecer
     */
    public void setNmDiasHabilesAudiencia(Integer nmDiasHabilesAudiencia) {
        this.nmDiasHabilesAudiencia = nmDiasHabilesAudiencia;
    }
    
    /**
     * @return Devuelve el valor del campo nmDiasLimmodif
     */
    @Column(name = "NM_DIAS_LIMMODIF", nullable = true)
    public Integer getNmDiasLimmodif() {
        return this.nmDiasLimmodif;
    }
    
    /**
     * Establece el valor del campo nmDiasLimmodif de la entidad
     * Convocatoria
     * 
     * @Param nmDiasLimmodif El valor a establecer
     */
    public void setNmDiasLimmodif(Integer nmDiasLimmodif) {
        this.nmDiasLimmodif = nmDiasLimmodif;
    }

    /**
     * @return Devuelve el valor del campo itRequisitos
     */

    @Column(name = "IT_REQUISITOS", nullable = true)
    public String getItRequisitos() {
        return this.itRequisitos;
    }

    /**
     * Establece el valor del campo itRequisitos de la entidad Convocatorias
     *
     * @param itRequisitos El valor a establecer
     */
    public void setItRequisitos(String itRequisitos) {
        this.itRequisitos = itRequisitos;
    }

    /**
     * @return Devuelve el valor del campo fcIniTramiEval
     */

    @Column(name = "FC_INI_TRAMI_EVAL")
    public Date getFcIniTramiEval() {
        return this.fcIniTramiEval;
    }

    /**
     * Establece el valor del campo fcIniTramiEval de la entidad Convocatorias
     *
     * @param fcIniTramiEval El valor a establecer
     */
    public void setFcIniTramiEval(Date fcIniTramiEval) {
        this.fcIniTramiEval = fcIniTramiEval;
    }

    /**
     * @return Devuelve el valor del campo fcFinTramiEval
     */

    @Column(name = "FC_FIN_TRAMI_EVAL")
    public Date getFcFinTramiEval() {
        return this.fcFinTramiEval;
    }

    /**
     * Establece el valor del campo fcFinTramiEval de la entidad Convocatorias
     *
     * @param fcFinTramiEval El valor a establecer
     */
    public void setFcFinTramiEval(Date fcFinTramiEval) {
        this.fcFinTramiEval = fcFinTramiEval;
    }

    /**
     * @return Devuelve el valor del campo fcIniAlegaciones
     */

    @Column(name = "FC_INI_ALEGACIONES")
    public Date getFcIniAlegaciones() {
        return this.fcIniAlegaciones;
    }

    /**
     * Establece el valor del campo fcIniAlegaciones de la entidad Convocatorias
     *
     * @param fcIniAlegaciones El valor a establecer
     */
    public void setFcIniAlegaciones(Date fcIniAlegaciones) {
        this.fcIniAlegaciones = fcIniAlegaciones;
    }

    /**
     * @return Devuelve el valor del campo fcFinAlegaciones
     */

    @Column(name = "FC_FIN_ALEGACIONES")
    public Date getFcFinAlegaciones() {
        return this.fcFinAlegaciones;
    }

    /**
     * Establece el valor del campo fcFinAlegaciones de la entidad Convocatorias
     *
     * @param fcFinAlegaciones El valor a establecer
     */
    public void setFcFinAlegaciones(Date fcFinAlegaciones) {
        this.fcFinAlegaciones = fcFinAlegaciones;
    }

    /**
     * @return Devuelve el valor del campo fcTraComisionEval
     */
    @Column(name = "FC_TRA_COMISION_EVAL")
    public Date getFcTraComisionEval() {
        return this.fcTraComisionEval;
    }

    /**
     * Establece el valor del campo setFcTraComisionEval de la entidad
     * Convocatorias
     *
     * @param fcTraComisionEval El valor a establecer
     */
    public void setFcTraComisionEval(Date fcTraComisionEval) {
        this.fcTraComisionEval = fcTraComisionEval;
    }

    /**
     * @return Devuelve el valor del campo fcTraPropResDef
     */
    @Column(name = "FC_TRA_PROP_RES_DEF")
    public Date getFcTraPropResDef() {
        return this.fcTraPropResDef;
    }

    /**
     * Establece el valor del campo setFcTraPropResDef de la entidad
     * Convocatorias
     *
     * @param fcTraPropResDef El valor a establecer
     */
    public void setFcTraPropResDef(Date fcTraPropResDef) {
        this.fcTraPropResDef = fcTraPropResDef;
    }

    /**
     * @return Devuelve el valor del campo fcTraResolucionDef
     */
    @Column(name = "FC_TRA_RES_DEF")
    public Date getFcTraResolucionDef() {
        return this.fcTraResolucionDef;
    }

    /**
     * Establece el valor del campo setFcTraResolucionDef de la entidad
     * Convocatorias
     *
     * @param fcTraResolucionDef El valor a establecer
     */
    public void setFcTraResolucionDef(Date fcTraResolucionDef) {
        this.fcTraResolucionDef = fcTraResolucionDef;
    }

    /**
     * @return Devuelve el valor del campo nmCalificacionMax
     */
    @Column(name = "NM_CALIFICACION_MAX")
    public Integer getNmCalificacionMax() {
        return this.nmCalificacionMax;
    }

    /**
     * Establece el valor del campo setNmCalificacionMax de la entidad
     * Convocatorias
     *
     * @param nmCalificacionMax El valor a establecer
     */
    public void setNmCalificacionMax(Integer nmCalificacionMax) {
        this.nmCalificacionMax = nmCalificacionMax;
    }

    /**
     * @return Devuelve el valor del campo fcIniContrato
     */

    @Column(name = "FC_INI_CONTRATO", nullable = true)
    public Date getFcIniContrato() {
        return this.fcIniContrato;
    }

    /**
     * Establece el valor del campo fcIniContratode la entidad Convocatorias
     *
     * @param fcIniContrato El valor a establecer
     */
    public void setFcIniContrato(Date fcIniContrato) {
        this.fcIniContrato = fcIniContrato;
    }

    /**
     * @return Devuelve el valor del campo fcFinContrato
     */

    @Column(name = "FC_FIN_CONTRATO", nullable = true)
    public Date getFcFinContrato() {
        return this.fcFinContrato;
    }

    /**
     * Establece el valor del campo fcFinContrato de la entidad Convocatorias
     *
     * @param fcFinContrato El valor a establecer
     */
    public void setFcFinContrato(Date fcFinContrato) {
        this.fcFinContrato = fcFinContrato;
    }
    
    /**
     * @return Devuelve el valor del campo itContratos
     */

    @Column(name = "IT_CONTRATOS", nullable = true)
    public String getItContratos() {
        return this.itContratos;
    }

    /**
     * Establece el valor del campo itContratos de la entidad Convocatorias
     *
     * @param itContratos El valor a establecer
     */
    public void setItContratos(String itContratos) {
        this.itContratos = itContratos;
    }
    
    /**
     * @return Devuelve el valor del campo itPartidasPresupuestarias
     */

    @Column(name = "IT_PARTIDAS_PRESUPUESTARIAS", nullable = true)
    public String getItPartidasPresupuestarias() {
        return this.itPartidasPresupuestarias;
    }

    /**
     * Establece el valor del campo itPartidasPresupuestarias de la entidad Convocatorias
     *
     * @param itPartidasPresupuestarias El valor a establecer
     */
    public void setItPartidasPresupuestarias(String itPartidasPresupuestarias) {
        this.itPartidasPresupuestarias = itPartidasPresupuestarias;
    }
    
    /**
     * @return Devuelve el valor del campo itActasComitesGestion
     */

    @Column(name = "IT_ACTAS_COMITES_GESTION", nullable = true)
    public String getItActasComitesGestion() {
        return this.itActasComitesGestion;
    }

    /**
     * Establece el valor del campo itActasComitesGestion de la entidad Convocatorias
     *
     * @param itActasComitesGestion El valor a establecer
     */
    public void setItActasComitesGestion(String itActasComitesGestion) {
        this.itActasComitesGestion = itActasComitesGestion;
    }
    
    @Column(name = "IT_AGOTAR_FONDOS", nullable = true)
    public String getItAgotarFondos() {
        return this.itAgotarFondos;
    }

    /**
     * Establece el valor del campo itAgotarFondos de la entidad Convocatorias
     *
     * @param itAgotarFondos El valor a establecer
     */
    public void setItAgotarFondos(String itAgotarFondos) {
        this.itAgotarFondos = itAgotarFondos;
    }
    
    
    @Column(name = "IT_ABIERTA_PERMANENTE", nullable = true)
    public String getItAbiertaPermanente() {
        return this.itAbiertaPermanente;
    }

    /**
     * Establece el valor del campo itAbiertaPermanente de la entidad Convocatorias
     *
     * @param itAbiertaPermanente El valor a establecer
     */
    public void setItAbiertaPermanente(String itAbiertaPermanente) {
        this.itAbiertaPermanente = itAbiertaPermanente;
    }
    
    
    @Column(name = "IT_FONDOS_AGOTADOS", nullable = true)
    public String getItfondosAgotados() {
        return this.itfondosAgotados;
    }

    /**
     * Establece el valor del campo itAbiertaPermanente de la entidad Convocatorias
     *
     * @param itAbiertaPermanente El valor a establecer
     */
    public void setItfondosAgotados(String itfondosAgotados) {
        this.itfondosAgotados = itfondosAgotados;
    }
    
    
    @Column(name = "IT_CONVOCATORIA_CERRADA", nullable = true)
    public String getItConvocatoriaCerrada() {
        return this.itConvocatoriaCerrada;
    }

    /**
     * Establece el valor del campo itAbiertaPermanente de la entidad Convocatorias
     *
     * @param itAbiertaPermanente El valor a establecer
     */
    public void setItConvocatoriaCerrada(String itConvocatoriaCerrada) {
        this.itConvocatoriaCerrada = itConvocatoriaCerrada;
    }
    
    
    @Column(name = "IT_EVALUACION")
    public String getItEvaluacion() {
        return itEvaluacion;
    }

    public void setItEvaluacion(String itEvaluacion) {
        this.itEvaluacion = itEvaluacion;
    }
    
    
    
    @Column(name = "IT_ALEGACIONES")
    public String getItAlegacion() {
        return itAlegacion;
    }

    public void setItAlegacion(String itAlegacion) {
        this.itAlegacion = itAlegacion;
    }
    
    

    /**
     * @return Devuelve el valor del campo nmMaxPeriodos
     */

    @Column(name = "NM_MAX_PERIODOS", nullable = true)
    public Integer getNmMaxPeriodos() {
        return this.nmMaxPeriodos;
    }

    /**
     * Establece el valor del campo nmMaxPeriodos de la entidad Convocatorias
     *
     * @param nmMaxPeriodos El valor a establecer
     */
    public void setNmMaxPeriodos(Integer nmMaxPeriodos) {
        this.nmMaxPeriodos = nmMaxPeriodos;
    }
    
    
    @Lob
    @Column(name = "CL_INSTRUCCIONES", nullable = true)
    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
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
     * @return Devuelve el valor del campo idUnidadAdminJCT
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UNIDAD_ADMIN_JCT", nullable = true)
    public UnidadAdmin getIdAreaAdministrativaJCT() {
        return idAreaAdministrativaJCT;
    }

    /**
     * Establece el valor del campo idUnidadAdminJCT
     *
     * @param idAreaAdministrativaJCT El valor a establecer
     */
    public void setIdAreaAdministrativaJCT(UnidadAdmin idAreaAdministrativaJCT) {
        this.idAreaAdministrativaJCT = idAreaAdministrativaJCT;
    }
    
    
    /**
     * @return Devuelve el valor del campo nmDiasAmpliContrato
     */
    @Column(name = "NM_DIAS_AMPLIA_CONTRATO")
    public Integer getNmDiasAmpliContrato() {
        return this.nmDiasAmpliContrato;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Catalogos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setNmDiasAmpliContrato(Integer nmDiasAmpliContrato) {
        this.nmDiasAmpliContrato = nmDiasAmpliContrato;
    }
    
    
    
    /**
     * @return Devuelve el valor del campo nmDiasAmpliContrato
     */
    @Column(name = "ID_ESTADO_RESOL")
    public Integer getIdEstadoResolucion() {
        return this.idEstadoResolucion;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad Catalogos
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setIdEstadoResolucion(Integer idEstadoResolucion) {
        this.idEstadoResolucion = idEstadoResolucion;
    }
    
    

    @Column(name = "PORCENTAJE_INTENSIDAD_AYUDA", nullable = false)
    public Double getPorcentajeIntensidadAyuda() {
        return porcentajeIntensidadAyuda;
    }

    public void setPorcentajeIntensidadAyuda(Double porcentajeIntensidadAyuda) {
        this.porcentajeIntensidadAyuda = porcentajeIntensidadAyuda;
    }
    
    
    @Column(name = "IT_CONCEPTO_PRESUP")
    public String getItConceptoPresup() {
        return itConceptoPresup;
    }

    public void setItConceptoPresup(String itConceptoPresup) {
        this.itConceptoPresup = itConceptoPresup;
    }
    
    @Column(name = "IT_TRASLADO_FECHAS_CONT")
    public String getItTrasladoFechasCont() {
        return itTrasladoFechasCont;
    }

    public void setItTrasladoFechasCont(String itTrasladoFechasCont) {
        this.itTrasladoFechasCont = itTrasladoFechasCont;
    }

    
    /**
     * @return Devuelve el valor del campo fcTraPropResProv
     */
    @Column(name = "FC_TRA_PROP_RES_PROV")
    public Date getFcTraPropResProv() {
        return this.fcTraPropResProv;
    }

    /**
     * Establece el valor del campo setFcTraPropResProv de la entidad
     * Convocatorias
     *
     * @param fcTraPropResProv El valor a establecer
     */
    public void setFcTraPropResProv(Date fcTraPropResProv) {
        this.fcTraPropResProv = fcTraPropResProv;
    }

    /**
     * @return Devuelve el valor del campo fcTraResolucionProv
     */
    @Column(name = "FC_TRA_RES_PROV")
    public Date getFcTraResolucionProv() {
        return this.fcTraResolucionProv;
    }

    /**
     * Establece el valor del campo setFcTraResolucionProv de la entidad
     * Convocatorias
     *
     * @param fcTraResolucionProv El valor a establecer
     */
    public void setFcTraResolucionProv(Date fcTraResolucionProv) {
        this.fcTraResolucionProv = fcTraResolucionProv;
    }
    
    
    /**
     * @return Devuelve el valor del campo itImporteMaxAnual
     */

    @Column(name = "IT_IMPORTE_MAX_ANUAL", nullable = true)
    public String getItImporteMaxAnual() {
        return this.itImporteMaxAnual;
    }

    /**
     * Establece el valor del campo itImporteMaxAnual de la entidad Convocatorias
     *
     * @param itImporteMaxAnual El valor a establecer
     */
    public void setItImporteMaxAnual(String itImporteMaxAnual) {
        this.itImporteMaxAnual = itImporteMaxAnual;
    }
    
    
    /**
     * @return Devuelve el valor del campo itImporteMaxExpdte
     */

    @Column(name = "IT_IMPORTE_MAX_EXP", nullable = true)
    public String getItImporteMaxExpdte() {
        return this.itImporteMaxExpdte;
    }

    /**
     * Establece el valor del campo itImporteMaxExpdte de la entidad Convocatorias
     *
     * @param itImporteMaxExpdte El valor a establecer
     */
    public void setItImporteMaxExpdte(String itImporteMaxExpdte) {
        this.itImporteMaxExpdte = itImporteMaxExpdte;
    }

    /**
     * @return Devuelve el valor del campo itDetalleJusEco
     */

    @Column(name = "IT_DETALLE_JUS_ECO", nullable = true)
    public String getItDetalleJusEco() {
        return itDetalleJusEco;
    }

    /**
     * Establece el valor del campo itDetalleJusEco de la entidad Convocatorias
     *
     * @param itDetalleJusEco El valor a establecer
     */
    public void setItDetalleJusEco(String itDetalleJusEco) {
        this.itDetalleJusEco = itDetalleJusEco;
    }

    @Lob
    @Column(name = "CL_PROTECCION_DATOS", nullable = true)
    public String getInformacionRgpd() {
        return informacionRgpd;
    }

    public void setInformacionRgpd(String informacionRgpd) {
        this.informacionRgpd = informacionRgpd;
    }

    /**
     * @return the idAceptacionDesistimientoAyuda
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACEPTACION_DESISTIMIE_AYUDA", nullable = true)
    public CatalogosValores getIdAceptacionDesistimientoAyuda() {
        return idAceptacionDesistimientoAyuda;
    }

    /**
     * @param idAceptacionDesistimientoAyuda the idAceptacionDesistimientoAyuda to set
     */
    public void setIdAceptacionDesistimientoAyuda(CatalogosValores idAceptacionDesistimientoAyuda) {
        this.idAceptacionDesistimientoAyuda = idAceptacionDesistimientoAyuda;
    }
    
    /**
     * 
     * @return
     */
    @Column(name = "IT_ENTIDAD_CERRADA")
    public String getItEntidadCerrada() {
        return itEntidadCerrada;
    }

    public void setItEntidadCerrada(String itEntidadCerrada) {
        this.itEntidadCerrada = itEntidadCerrada;
    }

    /**
     * 
     * @return
     */
    @Column(name = "IT_AYUDA_CALCULADA")
    public String getItAyudaCalculada() {
        return itAyudaCalculada;
    }

    public void setItAyudaCalculada(String itAyudaCalculada) {
        this.itAyudaCalculada = itAyudaCalculada;
    }
    
    /**
     * @return Devuelve el valor del campo itResProvConcDeneg
     */
    @Column(name = "IT_RES_PROV_CONC_DENEG", nullable = true)
    public String getItResProvConcDeneg() {
        return this.itResProvConcDeneg;
    }

    /**
     * Establece el valor del campo itResProvConcDeneg
     *
     * @param itResProvConcDeneg El valor a establecer
     */
    public void setItResProvConcDeneg(String itResProvConcDeneg) {
        this.itResProvConcDeneg = itResProvConcDeneg;
    }

    /**
     * @return Devuelve el valor del campo itResProvExclusion
     */
    @Column(name = "IT_RES_PROV_EXCLUSION", nullable = true)
    public String getItResProvExclusion() {
        return this.itResProvExclusion;
    }

    /**
     * Establece el valor del campo itResProvExclusion
     *
     * @param itResProvExclusion El valor a establecer
     */
    public void setItResProvExclusion(String itResProvExclusion) {
        this.itResProvExclusion = itResProvExclusion;
    }

    /**
     * @return Devuelve el valor del campo itResProvDesistimiento
     */
    @Column(name = "IT_RES_PROV_DESISTIMIENTO", nullable = true)
    public String getItResProvDesistimiento() {
        return this.itResProvDesistimiento;
    }

    /**
     * Establece el valor del campo itResProvDesistimiento
     *
     * @param itResProvDesistimiento El valor a establecer
     */
    public void setItResProvDesistimiento(String itResProvDesistimiento) {
        this.itResProvDesistimiento = itResProvDesistimiento;
    }

   
    

    
}
