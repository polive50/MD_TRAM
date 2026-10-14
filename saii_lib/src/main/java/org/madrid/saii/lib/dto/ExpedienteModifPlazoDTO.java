package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteModifPlazoDTO extends AuditoriaDTO implements Serializable {

	// ////////////////////////////////////////////////////////
	// /////////////////////////////// CONSTRUCTORES
	// ////////////////////////////////////////////////////////

	/**
	 * Constructor sin argumentos
	 */
	public ExpedienteModifPlazoDTO() {
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
     * Representa el campo idExpedienteModifPlazo del objeto de dto
     */
    private Integer idExpedienteModifPlazo;

	/**
	 * Representa el campo idExpedienteAnualidad del objeto de dto
	 */
	private Integer idExpedienteAnualidad;

	/**
	 * Representa el campo idExpedienteModifPresPlazoOtra del objeto de dto
	 */
	private Integer idExpedienteModifPresPlazoOtra;

	/**
	 * Representa el campo tipoExpedienteAnualidadDTO del objeto de dto
	 */
	private CatalogosValoresDTO tipoExpedienteAnualidadDTO;

	/**
	 * Representa el campo nmAnualidad del objeto de dto
	 */
	private Integer nmAnualidad;
	
	/**
     * Representa el campo tlObservaciones del objeto de dto
     */
	private String tlObservaciones;

	/**
	 * Representa el campo fcIniEjecucion del objeto de dto
	 */
	private Date fcIniEjecucion;

	/**
	 * Representa el campo fcFinEjecucion del objeto de dto
	 */
	private Date fcFinEjecucion;
	
	 /**
     * Representa el campo fcIniEjecucionConcedido del objeto de dto
     */
    private Date fcIniEjecConcedido;

    /**
     * Representa el campo fcFinEjecucionConcedido del objeto de dto
     */
    private Date fcFinEjecConcedido;

}
