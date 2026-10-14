package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Evaluacion
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionJustecDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionJustecDTO() {
        super();
        this.listaEvaluacionJustecNotaDTO = new ArrayList<>();
        this.listaEvaluacionJustecApartaDTO = new ArrayList<>();
        this.existeError = Boolean.FALSE;
        this.blCalificacion = Boolean.FALSE;
        this.imagenLogoDTO = new ImagenDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idEvaluacion
     */
    private Integer idEvaluacionJustec;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo dsTitulo
     */
    private String dsTitulo;

    /**
     * Campo tlDescripcion
     */
    private String tlDescripcion;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

    /**
     * Campo itCalificacion
     */
    private String itCalificacion;

    /**
     * Campo blCalificacion
     */
    @JsonIgnore
    private Boolean blCalificacion;

    /**
     * Campo itTipoEval
     */
    private String itTipoEval;

    /**
     * Lista EvaluacionNotaDTO
     */
    private List<EvaluacionJustecNotaDTO> listaEvaluacionJustecNotaDTO;

    /**
     * Lista ApartadoDTO
     */
    private List<EvaluacionJustecApartaDTO> listaEvaluacionJustecApartaDTO;

    /* Campo idImagenLogo */
    private ImagenDTO imagenLogoDTO;

    /**
     * Variable auxiliar para indicar si existe error en validación de
     * evaluación
     */
    @JsonIgnore
    private Boolean existeError;

    /**
     * Variable auxiliar para mostrar el error en la evaluación
     */
    @JsonIgnore
    private String errores;

}
