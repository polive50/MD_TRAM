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

import lombok.ToString;

/**
 * Representa el objeto de dominio RepresentanteJur
 *
 * @author everis
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_REPRESENTANTE_JUR")
@ToString
public class RepresentanteJur implements Serializable {

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idRepresentanteJur del objeto de dominio
     */
    private Integer idRepresentanteJur;

    /**
     * Representa el campo fcVigenciaIni del objeto de dominio
     */
    private Date fcVigenciaIni;

    /**
     * Representa el campo fcVigenciaFin del objeto de dominio
     */
    private Date fcVigenciaFin;

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
     * Representa el campo entidad del objeto de dominio
     */
    private Entidad entidad;

    /**
     * Representa el campo idInteresado del objeto de dominio
     */
    private Interesado interesado;

    /**
     * Representa el campo estado del objeto de dominio
     */
    private CatalogosValores estado;

    /**
     * Representa el campo dsCargo del objeto de dominio
     */
    private String dsCargo;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public RepresentanteJur() {
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
        return new HashCodeBuilder().append(this.idRepresentanteJur).append(this.fcVigenciaIni)
                .append(this.fcVigenciaFin).append(this.cdUsuAlta).append(this.fcAltaFila)
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
        if (!(obj instanceof RepresentanteJur)) {
            return false;
        }
        RepresentanteJur castObj = (RepresentanteJur) obj;
        return new EqualsBuilder().append(this.idRepresentanteJur, castObj.idRepresentanteJur)
                .append(this.fcVigenciaIni, castObj.fcVigenciaIni)
                .append(this.fcVigenciaFin, castObj.fcVigenciaFin)
                .append(this.cdUsuAlta, castObj.cdUsuAlta)
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
     * @return Devuelve el valor del campo idRepresentanteJur
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_REPRESENTANTE_JUR")
    @SequenceGenerator(name = "SAII_S_REPRESENTANTE_JUR", sequenceName = "SAII_S_REPRESENTANTE_JUR", allocationSize = 1)
    @Column(name = "ID_REPRESENTANTE_JUR")
    public Integer getIdRepresentanteJur() {
        return this.idRepresentanteJur;
    }

    /**
     * Establece el valor del campo idRepresentanteJur de la entidad
     * RepresentanteJur
     *
     * @param idRepresentanteJur El valor a establecer
     */
    public void setIdRepresentanteJur(Integer idRepresentanteJur) {
        this.idRepresentanteJur = idRepresentanteJur;
    }

    /**
     * @return Devuelve el valor del campo fcVigenciaIni
     */

    @Column(name = "FC_VIGENCIA_INI", nullable = true)
    public Date getFcVigenciaIni() {
        return this.fcVigenciaIni;
    }

    /**
     * Establece el valor del campo fcVigenciaIni de la entidad RepresentanteJur
     *
     * @param fcVigenciaIni El valor a establecer
     */
    public void setFcVigenciaIni(Date fcVigenciaIni) {
        this.fcVigenciaIni = fcVigenciaIni;
    }

    /**
     * @return Devuelve el valor del campo fcVigenciaFin
     */

    @Column(name = "FC_VIGENCIA_FIN", nullable = true)
    public Date getFcVigenciaFin() {
        return this.fcVigenciaFin;
    }

    /**
     * Establece el valor del campo fcVigenciaFin de la entidad RepresentanteJur
     *
     * @param fcVigenciaFin El valor a establecer
     */
    public void setFcVigenciaFin(Date fcVigenciaFin) {
        this.fcVigenciaFin = fcVigenciaFin;
    }

    /**
     * @return Devuelve el valor del campo cdUsuAlta
     */

    @Column(name = "CD_USU_ALTA")
    public String getCdUsuAlta() {
        return this.cdUsuAlta;
    }

    /**
     * Establece el valor del campo cdUsuAlta de la entidad RepresentanteJur
     *
     * @param cdUsuAlta El valor a establecer
     */
    public void setCdUsuAlta(String cdUsuAlta) {
        this.cdUsuAlta = cdUsuAlta;
    }

    /**
     * @return Devuelve el valor del campo fcAltaFila
     */

    @Column(name = "FC_ALTA_FILA")
    public Date getFcAltaFila() {
        return this.fcAltaFila;
    }

    /**
     * Establece el valor del campo fcAltaFila de la entidad RepresentanteJur
     *
     * @param fcAltaFila El valor a establecer
     */
    public void setFcAltaFila(Date fcAltaFila) {
        this.fcAltaFila = fcAltaFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuModif
     */

    @Column(name = "CD_USU_MODIF", nullable = true)
    public String getCdUsuModif() {
        return this.cdUsuModif;
    }

    /**
     * Establece el valor del campo cdUsuModif de la entidad RepresentanteJur
     *
     * @param cdUsuModif El valor a establecer
     */
    public void setCdUsuModif(String cdUsuModif) {
        this.cdUsuModif = cdUsuModif;
    }

    /**
     * @return Devuelve el valor del campo fcModifFila
     */

    @Column(name = "FC_MODIF_FILA", nullable = true)
    public Date getFcModifFila() {
        return this.fcModifFila;
    }

    /**
     * Establece el valor del campo fcModifFila de la entidad RepresentanteJur
     *
     * @param fcModifFila El valor a establecer
     */
    public void setFcModifFila(Date fcModifFila) {
        this.fcModifFila = fcModifFila;
    }

    /**
     * @return Devuelve el valor del campo cdUsuBaja
     */

    @Column(name = "CD_USU_BAJA", nullable = true)
    public String getCdUsuBaja() {
        return this.cdUsuBaja;
    }

    /**
     * Establece el valor del campo cdUsuBaja de la entidad RepresentanteJur
     *
     * @param cdUsuBaja El valor a establecer
     */
    public void setCdUsuBaja(String cdUsuBaja) {
        this.cdUsuBaja = cdUsuBaja;
    }

    /**
     * @return Devuelve el valor del campo fcBajaFila
     */
    @Column(name = "FC_BAJA_FILA", nullable = true)
    public Date getFcBajaFila() {
        return this.fcBajaFila;
    }

    /**
     * Establece el valor del campo fcBajaFila de la entidad RepresentanteJur
     *
     * @param fcBajaFila El valor a establecer
     */
    public void setFcBajaFila(Date fcBajaFila) {
        this.fcBajaFila = fcBajaFila;
    }

    /**
     * @return Devuelve el valor de la entidad jurídica
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ENTIDAD", nullable = false)
    public Entidad getEntidad() {
        return this.entidad;
    }

    /**
     * Establece el valor del campo entidad de la entidad RepresentanteJur
     *
     * @param entidad El valor a establecer
     */
    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    /**
     * @return Devuelve el valor del interesado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INTERESADO", nullable = false)
    public Interesado getInteresado() {
        return this.interesado;
    }

    /**
     * Establece el valor del campo interesado de la entidad RepresentanteJur
     *
     * @param interesado El valor a establecer
     */
    public void setInteresado(Interesado interesado) {
        this.interesado = interesado;
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
     * @param estado the estado to set
     */
    public void setEstado(CatalogosValores estado) {
        this.estado = estado;
    }

    /**
     * @return the dsCargo
     */
    @Column(name = "DS_CARGO")
    public String getDsCargo() {
        return this.dsCargo;
    }

    /**
     * @param dsCargo the dsCargo to set
     */
    public void setDsCargo(String dsCargo) {
        this.dsCargo = dsCargo;
    }
}
