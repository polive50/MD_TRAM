package org.madrid.saii.lib.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCoorFiltrosDTO
 *
 * @author nttdata
 */

@Getter
@Setter
@ToString
public class ConvocatoriaCoorNavegacionDTO {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCoorNavegacionDTO() {
        super();
        this.lstConvocatoriaCoorFiltrosDTO = new ArrayList<ConvocatoriaCoorFiltrosDTO>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Representa el campo index del objeto de dominio
     */
    private Integer index;

    /**
     * Representa el campo pageSize del objeto de dominio
     */
    private Integer pageSize;

    /**
     * Representa el campo length del objeto de dominio
     */
    private Long length;

    /**
     * Representa el campo lstConvocatoriaCoorFiltrosDTO del objeto de dominio
     */
    List<ConvocatoriaCoorFiltrosDTO> lstConvocatoriaCoorFiltrosDTO;

}
