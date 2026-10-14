package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ApartadoJustecDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class InformeEvaluacionJustecApartadoDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public InformeEvaluacionJustecApartadoDTO() {
        super();
        this.lstApartadoDatoDTO = new ArrayList<ApartadoJustecDatoDTO>();
        this.lstEvaluacionNotaDatoDTO = new ArrayList<EvaluacionJustecNotaDatoDTO>();
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
    private List<ApartadoJustecDatoDTO> lstApartadoDatoDTO;

    /**
     * Campo EvaluacionNotaDatoDTO
     */
    private List<EvaluacionJustecNotaDatoDTO> lstEvaluacionNotaDatoDTO;

}
