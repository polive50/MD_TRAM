package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Clase DTO para controlar la paginación del listado de convocatorias
 * 
 * @author nttdata
 *
 */
@Data
public class ConvocatoriaPaginacionDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que representa la lista de registros
     */
    private List<ConvocatoriaDTO> lstConvocatorias;

    /**
     * Variable que representa la cantidad de registros
     */
    private int totalRegistros;

}
