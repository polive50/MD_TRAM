package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteEvalJusTecHist
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ExpedienteEvalJusTecHistDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteEvalJusTecHistDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.lstEvaluacionJustecNotaDatoDTO = new ArrayList<>();
        this.lstApartadoJustecDatoDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Campo idExpedienteEvalHist
     */
    private Integer idExpedienteEvalJusTecHist;

    /**
     * Campo idExpedienteEval
     */
    private ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO;

    /**
     * Campo expedienteJustec
     */
    private ExpedienteJusTecDTO expedienteJusTecDTO;

    /**
     * Campo idPersona
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo fcIniPlazo
     */
    private Date fcIniPlazo;
    
    /**
     * Campo fcFinPlazo
     */
    private Date fcFinPlazo;
    
    /**
     * Representa la lista de areas cientificas asociadas a la personas
     */
    private String areasCientificas;
    
    
    /**
     * Listado de Areas Cientificas que pertenece una persona
     */
    private List<PersonaAreaDTO> listaPersonaAreaDTO;

    /**
     * Campo idEstado
     */
    private CatalogosValoresDTO estadoDTO;
    
    /**
     * Campo cdFica
     */
    private String cdFica;

    /**
     * Campo dsComentario
     */
    private String dsComentario;

    /**
     * Campo nmNotaFinal
     */
    private Integer nmNotaFinal;

    /**
     * Campo itNotificado
     */
    private String itNotificado;

    /**
     * Campo causaDTO
     */
    private CatalogosValoresDTO causaDTO;

    /**
     * Lista de evaluacion Justec nota dato
     */
    private List<EvaluacionJustecNotaDatoDTO> lstEvaluacionJustecNotaDatoDTO;

    /**
     * Lista de apartado justec dato
     */
    private List<ApartadoJustecDatoDTO> lstApartadoJustecDatoDTO;

    /**
     * variable que representa el indicador si el estado es Cerrado Evaludor
     */
    private Boolean esCerradoEvaludor;
    
    /**
     * variable que representa si el registro fue rechazado por el  gestor
     */
    private String itRechazado;

    /** mostrarEvaluadoresJCT */
    private Boolean mostrarEvaluadoresJCT = Boolean.FALSE;
    
    /**
     * Campo obsEval
     */
    private String obsEval;
    
    /**
     * Campo obsGest
     */
    private String obsGest;
}
