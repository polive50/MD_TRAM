package org.madrid.saii.lib.dto;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class RepresentanteJurtTramitesDTO extends AuditoriaDTO implements Serializable {
    
    /**
     * Constructor sin argumentos
     */
    public RepresentanteJurtTramitesDTO() {
        super();
        this.expedienteDTO= new ExpedienteDTO();
        this.representanteJurDTO= new RepresentanteJurDTO();
        this.tramiteDTO= new CatalogosValoresDTO();
    }

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;
    
     private Integer idRepresentanteJurtTramites;
    
    /**
     * Representa el campo representanteJurDTO 
     */
    private RepresentanteJurDTO representanteJurDTO;
    
   
    /**
     * Representa el campo tramiteDTO 
     */
    private CatalogosValoresDTO tramiteDTO;
}
