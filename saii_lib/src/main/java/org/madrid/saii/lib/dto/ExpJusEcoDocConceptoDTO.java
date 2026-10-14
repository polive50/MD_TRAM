/**
 * 
 */
package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpJusEcoDocConceptoDTO
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ExpJusEcoDocConceptoDTO extends AuditoriaDTO implements Serializable {
    

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpJusEcoDocConceptoDTO() {
        super();
    }
    
    private Integer idExpedienteJusEcoDoc;
    private String concepto;
    private Double importe;
    private Double importeJustificado;
    private Double importeAdmitido; // editable
    private Integer idEstado;
    private Integer idExpedienteDoc;
    private Integer idExpedientePresupMonto;

}
