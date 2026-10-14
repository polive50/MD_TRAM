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

import atlas.core.database.auditoria.AtlasAuditarCampo;
import atlas.core.database.auditoria.AtlasAuditarCampo.TiposAuditoria;
import atlas.core.database.auditoria.AtlasAuditarFila;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa el objeto de dominio Catalogos
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_CATALOGOS")
@AtlasAuditarFila
@ToString
public class Catalogos implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo cdUsuAlta del objeto de dominio
     */
    private String cdUsuAlta;

    /**
     * Representa el campo cdUsuBaja del objeto de dominio
     */
    private String cdUsuBaja;

    /**
     * Representa el campo cdUsuModif del objeto de dominio
     */
    private String cdUsuModif;

    /**
     * Representa el campo fcAltaFila del objeto de dominio
     */
    private Date fcAltaFila;

    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcBajaFila;

    /**
     * Representa el campo fcModifFila del objeto de dominio
     */
    private Date fcModifFila;

    /**
     * Representa el campo idCatalogo del objeto de dominio
     */
    private Integer idCatalogo;

    /**
     * Representa el campo dsNombre del objeto de dominio
     */
    private String dsNombre;

    /**
     * Representa el campo tlDescripcion del objeto de dominio
     */
    private String tlDescripcion;

    /**
     * Representa el campo itMantenible del objeto de dominio
     */
    private String itMantenible;
    
    /**
     * Representa el campo nmConstante del objeto de dominio
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Integer nmConstante;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public Catalogos() {
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.idCatalogo == null ? 0 : this.idCatalogo.hashCode());
        return result;
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Catalogos other = (Catalogos) obj;
        if (this.idCatalogo == null) {
            if (other.idCatalogo != null) {
                return false;
            }
        } else if (!this.idCatalogo.equals(other.idCatalogo)) {
            return false;
        }
        return true;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// GET Y SET
    // ////////////////////////////////////////////////////////

    /**
     * @return Devuelve el valor del campo idCatalogo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_CATALOGOS")
    @SequenceGenerator(name = "SAII_S_CATALOGOS", sequenceName = "SAII_S_CATALOGOS", allocationSize = 1)
    @Column(name = "ID_CATALOGO")
    public Integer getIdCatalogo() {
        return this.idCatalogo;
    }

    /**
     * Establece el valor del campo idCatalogo de la entidad Catalogos
     *
     * @param idCatalogo El valor a establecer
     */
    public void setIdCatalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    /**
     * @return Devuelve el valor del campo dsNombre
     */

    @Column(name = "DS_NOMBRE")
    public String getDsNombre() {
        return this.dsNombre;
    }

    /**
     * Establece el valor del campo dsNombre de la entidad Catalogos
     *
     * @param dsNombre El valor a establecer
     */
    public void setDsNombre(String dsNombre) {
        this.dsNombre = dsNombre;
    }

    /**
     * @return Devuelve el valor del campo tlDescripcion
     */

    @Column(name = "TL_DESCRIPCION", nullable = true)
    public String getTlDescripcion() {
        return this.tlDescripcion;
    }

    /**
     * Establece el valor del campo tlDescripcion de la entidad Catalogos
     *
     * @param tlDescripcion El valor a establecer
     */
    public void setTlDescripcion(String tlDescripcion) {
        this.tlDescripcion = tlDescripcion;
    }

    @Column(name = "IT_MANTENIBLE", nullable = true)
    public String getItMantenible() {
        return this.itMantenible;
    }

    /**
     * Establece el valor del campo itMantenible de la entidad Catalogos
     *
     * @param itMantenible El valor a establecer
     */
    public void setItMantenible(String itMantenible) {
        this.itMantenible = itMantenible;
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
     * @return Devuelve el valor del campo nmConstante
     */

    @Column(name = "NM_CONSTANTE")
    public Integer getNmConstante() {
        return this.nmConstante;
    }
    
    /**
     * Establece el valor del campo nmConstante de la entidad CatalogosValores
     *
     * @param nmConstante El valor a establecer
     */
    public void setNmConstante(Integer nmConstante) {
        this.nmConstante = nmConstante;
    }
    
}
