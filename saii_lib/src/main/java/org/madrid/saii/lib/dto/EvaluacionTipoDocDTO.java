package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEvalTipoDoc
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class EvaluacionTipoDocDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Contructor sin parametros
     */
    public EvaluacionTipoDocDTO() {
        super();
        this.idTipoDocumento = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idEvaluacionTipoDoc
     */
    private Integer idEvaluacionTipoDoc;

    /**
     * Campo idTipoDocumento
     */
    private CatalogosValoresDTO idTipoDocumento;
    
    /**
     * Campo idNivel 
     */
    private Integer idInforme;

    /*campo auxiliar para el orden de insercion de los documentos */
    private Integer nmOrden;
    
    /**
     * Campo itObligatorio 
     */
    private Boolean itObligatorio;

}
