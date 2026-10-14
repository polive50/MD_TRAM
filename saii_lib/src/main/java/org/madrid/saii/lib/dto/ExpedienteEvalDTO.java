package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteEval
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedienteEvalDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteEvalDTO() {
        super();
        this.personaDTO = new InteresadoDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.lstEvaluacionNotaDatoDTO = new ArrayList<>();
        this.lstApartadoDatoDTO = new ArrayList<>();
        this.listaExpedienteDocEvalDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteEval
     */
    private Integer idExpedienteEval;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idPersona
     */
    private InteresadoDTO personaDTO;

    /**
     * Campo idEstado
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo itCoorEval
     */
    private String itCoorEval;

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
     * Lista de evaluacion nota dato
     */
    private List<EvaluacionNotaDatoDTO> lstEvaluacionNotaDatoDTO;
    
    private List<ExpedienteDocEvalDTO> listaExpedienteDocEvalDTO;

    /**
     * Lista de apartado dato
     */
    private List<ApartadoDatoDTO> lstApartadoDatoDTO;

    /**
     * variable que representa el indicador si el estado es Cerrado Evaludor
     */
    private Boolean esCerradoEvaludor;
    
    /**
     * variable que representa si el registro fue rechazado por el coordinador o gestor
     */
    private String itRechazado;
    
    /**
     * variable que representa motivoRechazo por el coordinador o gestor
     */
    private CatalogosValoresDTO motivoRechazo;
    
    /**
     * variable que representa si el registro fue aceptado por el coordinador o gestor
     */
    private String itAceptaEval;
    
    /**
     * variable que representa dsComentarioRechazoAceptacion 
     */
    private String dsComentarioRechazoAceptacion;

    private String dsObsRechazoInfEval;

    private InformeDTO informeDTO;

    private InformeEvalDocDTO informeEvalDocDTO;

    public List<InformeEvalDocDTO> listaInformesEvaluacion;
    
    private InformeEvalDocDTO informeActivo;

    private Integer idExpedienteEvalPadre;

}
