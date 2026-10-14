package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Apartado
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionJustecApartaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionJustecApartaDTO() {
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
     * Campo idApartado
     */
    private Integer idEvaluacionJustecAparta;

    /**
     * Campo idEvaluacion
     */
    private Integer idEvaluacionJustec;

    /**
     * Campo dsTitulo
     */
    private String dsTitulo;

    /**
     * Campo tlDescripcion
     */
    private String tlDescripcion;

    /**
     * Campo tlTexto
     */
    private String tlTexto;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

    /**
     * Campo nmCalificacionMax
     */
    private Integer nmCalificacionMax;

}
