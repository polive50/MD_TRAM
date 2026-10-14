package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.saii.lib.util.Constantes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Financiación
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class FinanciacionDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public FinanciacionDatoDTO() {
        super();
        this.tipoDTO = new CatalogosValoresDTO();
        this.esMontoTotal = Boolean.FALSE;
        this.itImporteFijo = Constantes.AfirmacionNegacion.N;
        this.itImporteFijoAux = Boolean.FALSE;
        this.esImporteMaxAnual = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idFinanciacion
     */
    private Integer idFinanciacionDato;

    /**
     * Campo idFinanciacion
     */
    private Integer idFinanciacion;

    /**
     * Campo idTipo
     */
    private CatalogosValoresDTO tipoDTO;

    /**
     * Campo nmImporteMax
     */
    private Double nmImporteMax;

    /**
     * Campo nmAnualidad
     */
    private Integer nmAnualidad;
    
    /**
     * Campo itImporteFijo
     */
    private String itImporteFijo;
    
    @JsonIgnore
    private Boolean itImporteFijoAux;

    @JsonIgnore
    private Boolean esMontoTotal;
    
    /**
     * Representa el campo nmImporteMaxAnual del objeto 
     */
    private Double nmImporteMaxAnual;
    
    private Boolean esImporteMaxAnual;
    
    private Integer nmOrden;

    /**
     * Representa el campo nmImporteMin del objeto 
     */
    private Double nmImporteMin;
}
