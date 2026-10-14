package org.madrid.saii.lib.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio Bloque
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_BLOQUE")
@AtlasAuditarFila
@ToString
public class Bloque implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idBloque del objeto de dominio
     */
    private Integer idBloque;

    /**
     * Representa el campo dsTematica del objeto de dominio
     */
    private CatalogosValores idTematica;

    /**
     * Representa el campo dsFormato del objeto de dominio
     */
    private CatalogosValores idFormato;

    /**
     * Representa el campo actor del objeto de dominio
     */
    private ConvocatoriaAct actor;

    /**
     * Representa el campo nmFilas del objeto de dominio
     */
    private Integer nmFilas;

    /**
     * Representa el campo nmOrden del objeto de dominio
     */
    private Integer nmOrden;
    
    /**
     * Representa el campo subNmOrden del objeto de dominio
     */
    private Integer subNmOrden;

    /**
     * Representa el campo tlAuxiliar del objeto de dominio
     */
    private String tlAuxiliar;

    /**
     * Representa el campo tlAuxiliarInf del dominio.
     */
    private String tlAuxiliarInf;

    /**
     * Representa el campo itAautorizaConsulta del objeto de dominio
     */
    private String itAutorizaConsulta;

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
     * Representa el campo idImpreso del objeto de dominio
     */
    private Impreso impreso;

    /**
     * Representa el campo nmMaxPalabras del objeto de dominio
     */
    private Integer nmMaxPalabras;

    /**
     * Lista de bloques componentes
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueComponente> lstBloqueComponente;
    
    /**
     * Representa el indicador si el bloque tiene multibloque
     */
    private String itMultiBloque;
    
    /**
     * Representa el campo idBloquePadre del objeto de dominio
     */
    private Integer idBloquePadre;
    
    /**
     * Representa el indicador si el bloque es multiregistro
     */
    private String itMultiRegistro;
    
    /**
     * Representa el campo itAutConDosOpc del objeto de dominio
     */
    private String itTipoSelDoc;
    
    /**
     * Representa el campo Descripción Más información
     */
    private String tlInformacion;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Bloque() {
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
        return new HashCodeBuilder().append(this.idBloque).append(this.idBloquePadre)
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
        if (!(obj instanceof Bloque)) {
            return false;
        }
        Bloque castObj = (Bloque) obj;
        return new EqualsBuilder().append(this.idBloque, castObj.idBloque)
                .append(this.idTematica, castObj.idTematica)
                .append(this.idFormato, castObj.idFormato).append(this.actor, castObj.actor)
                .append(this.nmOrden, castObj.nmOrden)
                .append(this.subNmOrden, castObj.subNmOrden)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
                .append(this.fcAltaFila, castObj.fcAltaFila)
                .append(this.cdUsuModif, castObj.cdUsuModif)
                .append(this.fcModifFila, castObj.fcModifFila)
                .append(this.cdUsuBaja, castObj.cdUsuBaja)
                .append(this.fcBajaFila, castObj.fcBajaFila)
                .append(this.itMultiBloque, castObj.itMultiBloque)
                .append(this.idBloquePadre, castObj.idBloquePadre).isEquals();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idBloque
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_BLOQUE")
    @SequenceGenerator(name = "SAII_S_BLOQUE", sequenceName = "SAII_S_BLOQUE", allocationSize = 1)
    @Column(name = "ID_BLOQUE")
    public Integer getIdBloque() {
        return this.idBloque;
    }

    /**
     * Establece el valor del campo idBloque de la entidad Bloque
     *
     * @param idBloque El valor a establecer
     */
    public void setIdBloque(Integer idBloque) {
        this.idBloque = idBloque;
    }

    /**
     * @return Devuelve el valor del campo dsTematica
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEMATICA")
    public CatalogosValores getIdTematica() {
        return this.idTematica;
    }

    /**
     * Establece el valor del campo dsTematica de la entidad Bloque
     *
     * @param dsTematica El valor a establecer
     */
    public void setIdTematica(CatalogosValores idTematica) {
        this.idTematica = idTematica;
    }

    /**
     * @return Devuelve el valor del campo dsFormato
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FORMATO")
    public CatalogosValores getIdFormato() {
        return this.idFormato;
    }

    /**
     * Establece el valor del campo dsFormato de la entidad Bloque
     *
     * @param dsFormato El valor a establecer
     */
    public void setIdFormato(CatalogosValores idFormato) {
        this.idFormato = idFormato;
    }

    /**
     * @return Devuelve el valor del campo Actor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ACTOR")
    public ConvocatoriaAct getActor() {
        return this.actor;
    }

    /**
     * Establece el valor del campo idTipoActorFisjur de la entidad Bloque
     *
     * @param idTipoActorFisjur El valor a establecer
     */
    public void setActor(ConvocatoriaAct actor) {
        this.actor = actor;
    }

    /**
     * @return the nmOrden
     */
    @Column(name = "NM_ORDEN", nullable = true)
    public Integer getNmOrden() {
        return this.nmOrden;
    }

    /**
     * @param nmOrden the nmOrden to set
     */
    public void setNmOrden(Integer nmOrden) {
        this.nmOrden = nmOrden;
    }
    
    /**
     * @return the subNmOrden
     */
    @Column(name = "NM_SUBORDEN", nullable = true)
    public Integer getSubNmOrden() {
        return this.subNmOrden;
    }

    /**
     * @param nmOrden the nmOrden to set
     */
    public void setSubNmOrden(Integer subNmOrden) {
        this.subNmOrden = subNmOrden;
    }

    /**
     * @return the nmFilas
     */
    @Column(name = "NM_FILAS", nullable = true)
    public Integer getNmFilas() {
        return this.nmFilas;
    }

    /**
     * @param nmFilas the nmFilas to set
     */
    public void setNmFilas(Integer nmFilas) {
        this.nmFilas = nmFilas;
    }

    /**
     * @return the tlAuxiliar
     */
    @Column(name = "TL_AUXILIAR", nullable = true)
    public String getTlAuxiliar() {
        return this.tlAuxiliar;
    }

    /**
     * @param tlAuxiliar the tlAuxiliar to set
     */
    public void setTlAuxiliar(String tlAuxiliar) {
        this.tlAuxiliar = tlAuxiliar;
    }

    /**
     * @return the tlAuxiliarInf
     */
    @Column(name = "TL_AUXILIAR_INF", nullable = true)
    public String getTlAuxiliarInf() {
        return this.tlAuxiliarInf;
    }

    /**
     * @param tlAuxiliarInf the tlAuxiliarInf to set
     */
    public void setTlAuxiliarInf(String tlAuxiliarInf) {
        this.tlAuxiliarInf = tlAuxiliarInf;
    }

    /**
     * @return the itAutorizaConsulta
     */
    @Column(name = "IT_AUTORIZA_CONSULTA", nullable = true)
    public String getItAutorizaConsulta() {
        return this.itAutorizaConsulta;
    }

    /**
     * @param itAutorizaConsulta the itAutorizaConsulta to set
     */
    public void setItAutorizaConsulta(String itAutorizaConsulta) {
        this.itAutorizaConsulta = itAutorizaConsulta;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_IMPRESO")
    public Impreso getImpreso() {
        return this.impreso;
    }

    /**
     * @param impreso the impreso to set
     */
    public void setImpreso(Impreso impreso) {
        this.impreso = impreso;
    }

    /**
     * @return the lstBloqueComponente
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bloque", cascade = CascadeType.ALL)
    public List<BloqueComponente> getLstBloqueComponente() {
        if (this.lstBloqueComponente != null) {
            // Filtrar la lista para excluir los elementos con fcBajaFila no nulo
            return this.lstBloqueComponente.stream()
                    .filter(bloqueComponente -> bloqueComponente.getFcBajaFila() == null)
                    .collect(Collectors.toList());
        }
        return this.lstBloqueComponente;
    }

    /**
     * @param lstBloqueComponente the lstBloqueComponente to set
     */
    public void setLstBloqueComponente(List<BloqueComponente> lstBloqueComponente) {
        this.lstBloqueComponente = lstBloqueComponente;
    }

    /**
     * @return the nmMaxPalabras
     */
    @Column(name = "NM_MAX_PALABRAS", nullable = true)
    public Integer getNmMaxPalabras() {
        return this.nmMaxPalabras;
    }

    /**
     * @param nmFilas the nmMaxPalabras to set
     */
    public void setNmMaxPalabras(Integer nmMaxPalabras) {
        this.nmMaxPalabras = nmMaxPalabras;
    }
    
    /**
     * @return the itMultiBloque
     */
    @Column(name = "IT_MULTIBLOQUE", nullable = true)
    public String getItMultiBloque() {
        return itMultiBloque;
    }
    
    /**
     * @param itMultiBloque the itMultiBloque to set
     */
    public void setItMultiBloque(String itMultiBloque) {
        this.itMultiBloque = itMultiBloque;
    }
    
    @Column(name = "IT_MULTIREGISTRO", nullable = true)
    public String getItMultiRegistro() {
        return itMultiRegistro;
    }

    public void setItMultiRegistro(String itMultiRegistro) {
        this.itMultiRegistro = itMultiRegistro;
    }
    
    
    /**
     * @return the idBloquePadre
     */
    @Column(name = "ID_BLOQUE_PADRE", nullable = true)
    public Integer getIdBloquePadre() {
        return this.idBloquePadre;
    }

    /**
     * @param idBloquePadre the idBloquePadre to set
     */
    public void setIdBloquePadre(Integer idBloquePadre) {
        this.idBloquePadre = idBloquePadre;
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

    /**
     * @return the itTipoSelDoc
     */
    @Column(name = "IT_TIPO_SEL_DOC", nullable = true)
    public String getItTipoSelDoc() {
        return itTipoSelDoc;
    }

    /**
     * @param itTipoSelDoc the itTipoSelDoc to set
     */
    public void setItTipoSelDoc(String itTipoSelDoc) {
        this.itTipoSelDoc = itTipoSelDoc;
    }

    /**
     * @return the tlInformacion
     */
    @Column(name = "TL_INFORMACION", nullable = true)
    public String getTlInformacion() {
        return tlInformacion;
    }

    /**
     * @param tlInformacion the tlInformacion to set
     */
    public void setTlInformacion(String tlInformacion) {
        this.tlInformacion = tlInformacion;
    }
    
}
