package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaAct
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaActDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ConvocatoriaActDTO() {
        super();
        this.tipoActorDTO = new CatalogosValoresDTO();
        this.identificacionDTO = new CatalogosValoresDTO();
        this.tipoPersonaFisJurDTO = new CatalogosValoresDTO();
        this.itContrato = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idActor
     */
    private Integer idActor;

    /**
     * Campo tlDescripcion
     */
    private String tlDescripcion;

    /**
     * Campo itGeneraExpediente
     */
    private String itGeneraExpediente;

    /**
     * Campo bolGeneraExpediente
     */
    private Boolean bolGeneraExpediente;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo tipoActorDTO
     */
    private CatalogosValoresDTO tipoActorDTO;

    /**
     * Campo tipoPersonaFisJurDTO
     */
    private CatalogosValoresDTO tipoPersonaFisJurDTO;

    /**
     * Campo identificacionDTO
     */
    private CatalogosValoresDTO identificacionDTO;

    /**
     * Campo tlDescripcionCorta
     */
    private String tlDescripcionCorta;

    /**
     * Campo nmMax
     */
    private Integer nmMax;

    /**
     * Campo nmMin
     */
    private Integer nmMin;

    /**
     * Campo idActorSolicitante
     */
    private Integer idActorSolicitante;

    /**
     * Campo idActorCoordinador
     */
    private Integer idActorCoordinador;

    /**
     * Indicador si el actor es solicitante
     */
    //@JsonIgnore
    private Boolean esActorSolicitante;

    /**
     * Indicador si el actor es coordinador
     */
    @JsonIgnore
    private Boolean esActorCoordinador;

    /**
     * Indicador si el actor es participante
     */
    @JsonIgnore
    private Boolean esActorParticipante;

    /**
     * Indicador si el actor padre
     */
    @JsonIgnore
    private String itTipoActorPadre;

    /**
     * Indicador de contrato
     */
    private Boolean itContrato;
    
    /**
     * Indicador de Particpante sin contrato
     */
    private Boolean itCumplimentaFormulario;
    
    /**
     * Indicador si el nombre del tipo juridico
     */

    private String tipoPersonaFisJurNombre;
    
    /**
     * Porpoiedad dsValor
     */
    private String dsValor;
    
    /**
     * Indicador de contratoindefinido
     */
    private Boolean itContratoIndefinido;

}
