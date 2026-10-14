package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ApartadoDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class InformeEvaluacionApartadoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public InformeEvaluacionApartadoDTO() {
        super();
        this.lstApartadoDatoDTO = new ArrayList<ApartadoDatoDTO>();
        this.lstEvaluacionNotaDatoDTO = new ArrayList<EvaluacionNotaDatoDTO>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo ApartadoDatoDTO
     */
    private List<ApartadoDatoDTO> lstApartadoDatoDTO;

    /**
     * Campo EvaluacionNotaDatoDTO
     */
    private List<EvaluacionNotaDatoDTO> lstEvaluacionNotaDatoDTO;

}
