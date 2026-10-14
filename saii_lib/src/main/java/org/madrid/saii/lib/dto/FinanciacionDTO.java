package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class FinanciacionDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public FinanciacionDTO() {
        super();
        this.listaFinanciacionDatoDTO = new ArrayList<>();
        this.tematicaDTO = new CatalogosValoresDTO();
        this.listaFinanciacionDatoAuxDTO = new ArrayList<>();
        this.esCreadoCorrecto = Boolean.TRUE;
        this.esImpAnualPermitido = Boolean.TRUE;
        this.esImpTotalPermitido = Boolean.TRUE;
        this.esTematicaAsignada = Boolean.FALSE;
        this.esIdActorAsignada = Boolean.FALSE;
        this.esMostrarAyuda = Boolean.FALSE;
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
    private Integer idFinanciacion;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idTematica
     */
    private CatalogosValoresDTO tematicaDTO;
    
    /**
     * Campo idTematica
     */
    private ConvocatoriaActDTO idActor;
    
    /**
     * Campo idTematica
     */
    private Boolean esTematicaAsignada;
    
    /**
     * Campo idTematica
     */
    private Boolean esIdActorAsignada;

    /**
     * Campo ittipo
     */
    private String itTipo;
    
    /**
     * Campo ittipo
     */
    private String itMostrarAyuda;
    

    /**
     * Campo listaFinanciacionDatoDTO
     */
    private List<FinanciacionDatoDTO> listaFinanciacionDatoDTO;

    /**
     *
     */
    @JsonIgnore
    private List<FinanciacionDatoDTO> listaFinanciacionDatoAuxDTO;

    /**
     * Variable para almacenar la validación si la tabla esta creada
     * correctamente
     */
    @JsonIgnore
    private Boolean esCreadoCorrecto;

    /* Variable para almacenar las anualidades por presupuesto registrado */
    @JsonIgnore
    private List<Integer> listaAnualidadPresup;

    /* Variable para validar los montos por anualidades */
    @JsonIgnore
    private Boolean esImpAnualPermitido;

    /* Variable para validar los montos por el total del presupuesto */
    @JsonIgnore
    private Boolean esImpTotalPermitido;

    /* N° de anualidad donde no cuadran los montos */
    @JsonIgnore
    private String anualidadError;
    
    private Double nmImporteMaxActor;
    
    /* Variable para validar los montos por anualidades */
    @JsonIgnore
    private Boolean esMostrarAyuda;
    
    /* Importe mínimo por actor */
    private Double nmImporteMinActor;

}
