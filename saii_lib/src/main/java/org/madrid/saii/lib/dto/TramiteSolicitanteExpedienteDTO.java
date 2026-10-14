package org.madrid.saii.lib.dto;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TramiteSolicitanteExpedienteDTO extends AuditoriaDTO implements Serializable {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    
    public TramiteSolicitanteExpedienteDTO() {
        super();
        this.interesadoDTO = new InteresadoDTO();
        this.tramiteDTO = new CatalogosValoresDTO();
        this.expedienteDTO = new ExpedienteDTO();
    }
    
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo Expediente
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * Campo tramite
     */
    private CatalogosValoresDTO tramiteDTO;
    
    /**
     * Campo interesado
     */
    private InteresadoDTO interesadoDTO;
}
