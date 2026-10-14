package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Dato
 *
 * @author Madrid Digital
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class DatoDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    public DatoDTO() {
        super();
        this.dsValor = StringUtils.EMPTY;
        this.esMontoTotal = Boolean.FALSE;
    }
    
    // Constructor de copia
    public DatoDTO(DatoDTO original) {
        this.idDato = original.idDato;
        this.nmColumna = original.nmColumna;
        this.dsValor = original.dsValor;
        this.nmFila = original.nmFila;
        this.expedienteDTO = original.expedienteDTO;
        this.idBloqueComponente = original.idBloqueComponente;
        this.nombreFicheroFica = original.nombreFicheroFica;
        this.dsValorMultiple = original.dsValorMultiple;
        this.esMontoTotal = original.esMontoTotal;
        this.nmImporteMax = original.nmImporteMax;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idDatos
     */
    private Integer idDato;

    /**
     * Campo nmFila
     */
    private Integer nmFila;

    /**
     * Campo nmFila
     */
    private Integer nmColumna;

    /**
     * Campo dsValor
     */
    private String dsValor;

    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;
    
    /**
     * Campo expedienteDTO
     */
    private ExpedientePresupMontoDTO expedientePresupMonto;

    /**
     * Campo componenteDTO
     */
    @JsonIgnore
    private Integer idBloqueComponente;

    /**
     * Campo nombreFicheroFica
     */
    private String nombreFicheroFica;

    /**
     * Campo para almacenar valores de componente check
     */
    @JsonIgnore
    private String[] dsValorMultiple;

    @JsonIgnore
    private Boolean esMontoTotal;

    @JsonIgnore
    private Double nmImporteMax;
    
    
    private ExpedienteJusTecDTO expedienteJustecDTO;
    
    private ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO;
    
    private ExpedienteJusEcoDTO expedienteJusEcoDTO;

}
