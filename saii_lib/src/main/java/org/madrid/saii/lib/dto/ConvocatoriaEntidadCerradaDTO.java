package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEntidadCerrada
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaEntidadCerradaDTO extends AuditoriaDTO implements Serializable {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaEntidadCerradaDTO() {
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
     * Campo idConvocatoriaEntCer
     */
    private Integer idConvocatoriaEntCer;
    
    /**
     * Campo convocatoriaDTO
     */
    private ConvocatoriaDTO convocatoriaDTO;
    
    /**
     * Campo entidadCerrada
     */
    private CatalogosValoresDTO entidadCerrada;
    
}
