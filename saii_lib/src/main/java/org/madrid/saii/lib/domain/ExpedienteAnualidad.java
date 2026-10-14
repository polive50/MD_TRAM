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
 * Representa el objeto de dominio ExpedienteAnualidad
 *
 * @author Quental
 * @version 1.0.0
 */
@Entity
@Table(name = "SAII_EXPEDIENTE_ANUAL")
@AtlasAuditarFila
@ToString
public class ExpedienteAnualidad implements Serializable {

	/**
	 * Identificador del numero de serie para serialización
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Representa el campo idExpedienteAnualidad del objeto de dominio
	 */
	private Integer idExpedienteAnualidad;

	/**
	 * Representa el campo idExpediente del objeto de dominio
	 */
	private Integer idExpediente;

	/**
	 * Representa el campo tipoExpedienteAnualidad del objeto de dominio
	 */
	private CatalogosValores tipoExpedienteAnualidad;

	/**
	 * Representa el campo nmAnualidad del objeto de dominio
	 */
	private Integer nmAnualidad;

	/**
	 * Representa el campo fcIniEjecucion del objeto de dominio
	 */
	private Date fcIniEjecucion;

	/**
	 * Representa el campo fcFinEjecucion del objeto de dominio
	 */
	private Date fcFinEjecucion;

	/**
	 * Representa el campo tlObservacion del objeto de dominio
	 */
	private String tlObservacion;

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
	

	// ////////////////////////////////////////////////////////
	// /////////////////////////////// CONSTRUCTORES
	// ////////////////////////////////////////////////////////

	/**
	 * Constructor sin argumentos
	 */
	public ExpedienteAnualidad() {
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
	 *         Más información en: {@link http://community.jboss.org/docs/DOC-13933}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.idExpedienteAnualidad).append(this.idExpediente)
				.append(this.tipoExpedienteAnualidad).append(this.nmAnualidad).append(this.fcIniEjecucion)
				.append(this.fcFinEjecucion).append(this.tlObservacion)
				.append(this.cdUsuAlta).append(this.fcAltaFila).append(this.cdUsuModif).append(this.fcModifFila)
				.append(this.cdUsuBaja).append(this.fcBajaFila).toHashCode();

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
		if (!(obj instanceof ExpedienteAnualidad)) {
			return false;
		}
		ExpedienteAnualidad castObj = (ExpedienteAnualidad) obj;
		return new EqualsBuilder().append(this.idExpedienteAnualidad, castObj.idExpedienteAnualidad)
				.append(this.idExpediente, castObj.idExpediente)
				.append(this.tipoExpedienteAnualidad, castObj.tipoExpedienteAnualidad)
				.append(this.nmAnualidad, castObj.nmAnualidad).append(this.fcIniEjecucion, castObj.fcIniEjecucion)
				.append(this.fcFinEjecucion, castObj.fcFinEjecucion)			
				.append(this.tlObservacion, castObj.tlObservacion).append(this.cdUsuAlta, castObj.cdUsuAlta)
				.append(this.fcAltaFila, castObj.fcAltaFila).append(this.cdUsuModif, castObj.cdUsuModif)
				.append(this.fcModifFila, castObj.fcModifFila).append(this.cdUsuBaja, castObj.cdUsuBaja)
				.append(this.fcBajaFila, castObj.fcBajaFila).isEquals();
	}

	// ////////////////////////////////////////////////////////
	// /////////////////////////////// GET Y SET
	// ////////////////////////////////////////////////////////

	/**
	 * @return Devuelve el valor del campo idExpedienteAnualidad
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAII_S_EXPEDIENTE_ANUAL")
	@SequenceGenerator(name = "SAII_S_EXPEDIENTE_ANUAL", sequenceName = "SAII_S_EXPEDIENTE_ANUAL", allocationSize = 1)
	@Column(name = "ID_EXPEDIENTE_ANUAL")
	public Integer getIdExpedienteAnualidad() {
		return this.idExpedienteAnualidad;
	}

	/**
	 * Establece el valor del campo idExpedienteAnualidad de la entidad
	 * ExpedienteAnualidad
	 *
	 * @param idExpedienteAnualidad El valor a establecer
	 */
	public void setIdExpedienteAnualidad(Integer idExpedienteAnualidad) {
		this.idExpedienteAnualidad = idExpedienteAnualidad;
	}

	/**
	 * @return Devuelve el valor del campo idExpediente
	 */
	@Column(name = "ID_EXPEDIENTE")
	public Integer getIdExpediente() {
		return this.idExpediente;
	}

	/**
	 * Establece el valor del campo idExpediente de la entidad ExpedienteAnualidad
	 *
	 * @param idExpediente El valor a establecer
	 */
	public void setIdExpediente(Integer idExpediente) {
		this.idExpediente = idExpediente;
	}

	/**
	 * @return Devuelve el valor del campo tipoExpedienteAnualidad
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_ANUALIDAD", nullable = false)
	public CatalogosValores getTipoExpedienteAnualidad() {
		return this.tipoExpedienteAnualidad;
	}

	/**
	 * Establece el valor del campo tipoExpedienteAnualidad de la entidad
	 * ExpedienteAnualidad
	 *
	 * @param tipoExpedienteAnualidad El valor a establecer
	 */
	public void setTipoExpedienteAnualidad(CatalogosValores tipoExpedienteAnualidad) {
		this.tipoExpedienteAnualidad = tipoExpedienteAnualidad;
	}

	/**
	 * @return Devuelve el valor del campo nmAnualidad
	 */
	@Column(name = "NM_ANUALIDAD")
	public Integer getNmAnualidad() {
		return this.nmAnualidad;
	}

	/**
	 * Establece el valor del campo nmAnualidad de la entidad ExpedienteAnualidad
	 *
	 * @param nmAnualidad El valor a establecer
	 */
	public void setNmAnualidad(Integer nmAnualidad) {
		this.nmAnualidad = nmAnualidad;
	}

	/**
	 * @return Devuelve el valor del campo fcIniEjecucion
	 */
	@Column(name = "FC_INI_EJECUCION")
	public Date getFcIniEjecucion() {
		return this.fcIniEjecucion;
	}

	/**
	 * Establece el valor del campo fcIniEjecucion de la entidad ExpedienteAnualidad
	 *
	 * @param fcIniEjecucion El valor a establecer
	 */
	public void setFcIniEjecucion(Date fcIniEjecucion) {
		this.fcIniEjecucion = fcIniEjecucion;
	}

	/**
	 * @return Devuelve el valor del campo fcFinEjecucion
	 */
	@Column(name = "FC_FIN_EJECUCION")
	public Date getFcFinEjecucion() {
		return this.fcFinEjecucion;
	}

	/**
	 * Establece el valor del campo fcFinEjecucion de la entidad ExpedienteAnualidad
	 *
	 * @param fcFinEjecucion El valor a establecer
	 */
	public void setFcFinEjecucion(Date fcFinEjecucion) {
		this.fcFinEjecucion = fcFinEjecucion;
	}
	
	/**
	 * @return Devuelve el valor del campo tlObservacion
	 */
	@Column(name = "TL_OBSERVACION")
	public String getTlObservacion() {
		return this.tlObservacion;
	}

	/**
	 * Establece el valor del campo tlObservacion de la entidad ExpedienteAnualidad
	 *
	 * @param tlObservacion El valor a establecer
	 */
	public void setTlObservacion(String tlObservacion) {
		this.tlObservacion = tlObservacion;
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
	 * Establece el valor del campo cdUsuAlta de la entidad ExpedienteAnualidad
	 *
	 * @param cdUsuAlta El valor a establecer
	 */
	public void setCdUsuAlta(String cdUsuAlta) {
		this.cdUsuAlta = cdUsuAlta;
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
	 * Establece el valor del campo fcAltaFila de la entidad ExpedienteAnualidad
	 *
	 * @param fcAltaFila El valor a establecer
	 */
	public void setFcAltaFila(Date fcAltaFila) {
		this.fcAltaFila = fcAltaFila;
	}

	/**
	 * @return Devuelve el valor del campo cdUsuModif
	 */
	@AtlasAuditarCampo(tipo = TiposAuditoria.USUMODIF)
	@Column(name = "CD_USU_MODIF")
	public String getCdUsuModif() {
		return this.cdUsuModif;
	}

	/**
	 * Establece el valor del campo cdUsuModif de la entidad ExpedienteAnualidad
	 *
	 * @param cdUsuModif El valor a establecer
	 */
	public void setCdUsuModif(String cdUsuModif) {
		this.cdUsuModif = cdUsuModif;
	}

	/**
	 * @return Devuelve el valor del campo fcModifFila
	 */
	@AtlasAuditarCampo(tipo = TiposAuditoria.FECHAMODIF)
	@Column(name = "FC_MODIF_FILA")
	public Date getFcModifFila() {
		return this.fcModifFila;
	}

	/**
	 * Establece el valor del campo fcModifFila de la entidad ExpedienteAnualidad
	 *
	 * @param fcModifFila El valor a establecer
	 */
	public void setFcModifFila(Date fcModifFila) {
		this.fcModifFila = fcModifFila;
	}

	/**
	 * @return Devuelve el valor del campo cdUsuBaja
	 */
	@AtlasAuditarCampo(tipo = TiposAuditoria.USUBAJA)
	@Column(name = "CD_USU_BAJA")
	public String getCdUsuBaja() {
		return this.cdUsuBaja;
	}

	/**
	 * Establece el valor del campo cdUsuBaja de la entidad ExpedienteAnualidad
	 *
	 * @param cdUsuBaja El valor a establecer
	 */
	public void setCdUsuBaja(String cdUsuBaja) {
		this.cdUsuBaja = cdUsuBaja;
	}

	/**
	 * @return Devuelve el valor del campo cdUsuBfcBajaFilaaja
	 */
	@AtlasAuditarCampo(tipo = TiposAuditoria.FECHABAJA)
	@Column(name = "FC_BAJA_FILA")
	public Date getFcBajaFila() {
		return this.fcBajaFila;
	}

	/**
	 * Establece el valor del campo fcBajaFila de la entidad ExpedienteAnualidad
	 *
	 * @param fcBajaFila El valor a establecer
	 */
	public void setFcBajaFila(Date fcBajaFila) {
		this.fcBajaFila = fcBajaFila;
	}	

}
