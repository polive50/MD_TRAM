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
public class EvaluacionDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionDTO() {
        super();
        this.listaEvaluacionNotaDTO = new ArrayList<>();
        this.listaApartadoDTO = new ArrayList<>();
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
    private Integer idEvaluacion;

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
     * Campo itCoorEval
     */
    private String itCoorEval;

    /**
     * Lista EvaluacionNotaDTO
     */
    private List<EvaluacionNotaDTO> listaEvaluacionNotaDTO;

    /**
     * Lista ApartadoDTO
     */
    private List<ApartadoDTO> listaApartadoDTO;

    /* Campo idImagenLogo */
    private ImagenDTO imagenLogoDTO;
    
    /* */
    private InformeDTO informeDTO;

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
