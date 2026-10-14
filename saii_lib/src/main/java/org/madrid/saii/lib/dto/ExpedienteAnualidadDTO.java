package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteAnualidadDTO extends AuditoriaDTO implements Serializable {

	// ////////////////////////////////////////////////////////
	// /////////////////////////////// CONSTRUCTORES
	// ////////////////////////////////////////////////////////

	/**
	 * Constructor sin argumentos
	 */
	public ExpedienteAnualidadDTO() {
		super();
	}

	// ////////////////////////////////////////////////////////
	// /////////////////////////////// ATRIBUTOS
	// ////////////////////////////////////////////////////////

	/**
	 * Identificador del numero de serie para serialización
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Representa el campo idExpedienteAnualidad del objeto de dto
	 */
	private Integer idExpedienteAnualidad;

	/**
	 * Representa el campo idExpediente del objeto de dto
	 */
	private Integer idExpediente;

	/**
	 * Representa el campo tipoExpedienteAnualidadDTO del objeto de dto
	 */
	private CatalogosValoresDTO tipoExpedienteAnualidadDTO;

	/**
	 * Representa el campo nmAnualidad del objeto de dto
	 */
	private Integer nmAnualidad;

	/**
	 * Representa el campo fcIniEjecucion del objeto de dto
	 */
	private Date fcIniEjecucion;

	/**
	 * Representa el campo fcFinEjecucion del objeto de dto
	 */
	private Date fcFinEjecucion;

	/**
	 * Representa el campo tlObservacion del objeto de dto
	 */
	private String tlObservacion;

}
