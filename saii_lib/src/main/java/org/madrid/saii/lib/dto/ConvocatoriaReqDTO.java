package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaReq
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaReqDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaReqDTO() {
        super();
        this.requisitoDTO = new CatalogosValoresDTO();
        this.convocatoriaSubmodDTO = new ConvocatoriaSubmodDTO();
        this.convocatoriaActDTO = new ConvocatoriaActDTO ();
        this.convocatoriaTipoDocDTO = new ConvocatoriaTipoDocDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaReq
     */
    private Integer idConvocatoriaReq;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idRequisito
     */
    private CatalogosValoresDTO requisitoDTO;

    /**
     * Campo idConvocatoriaSubmod
     */
    private ConvocatoriaSubmodDTO convocatoriaSubmodDTO;
    
    /**
     * Campo idActor
     */
    private ConvocatoriaActDTO convocatoriaActDTO;

    /**
     * Campo idTipoDocReq
     */
    private ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO;

}
