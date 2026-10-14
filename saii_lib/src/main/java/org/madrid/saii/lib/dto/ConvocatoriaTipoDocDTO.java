package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaTip
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaTipoDocDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaTipoDocDTO() {
        super();
        this.tramiteDTO = new CatalogosValoresDTO();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaTip
     */
    private Integer idConvocatoriaTipoDoc;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo tipoDocumentoDTO
     */
    private CatalogosValoresDTO tipoDocumentoDTO;

    /**
     * Campo cdFicaPlantilla
     */
    private Integer cdFicaPlantilla;

    /**
     * Campo tramite
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Campo itObligatorio
     */
    private Boolean itObligatorio;
    
    /**
     * Campo itPermiDescaEval
     */
    private Boolean itPermiDescaEval;
    
    public List<ConvocatoriaTipoDocDTO> listadoTipDocTotalRegistros;
    
    private CatalogosValoresDTO tipoICDA;
    
    private Boolean itConsentimiento;
    
    private CatalogosValoresDTO codProcedimiento;
}
