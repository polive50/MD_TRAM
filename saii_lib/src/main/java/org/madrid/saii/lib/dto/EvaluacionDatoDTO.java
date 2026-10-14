package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de EvaluacionDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class EvaluacionDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public EvaluacionDatoDTO() {
        super();
        this.lstAlegacionDatoDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idAlegacion
     */
    private Integer idAlegacion;

    /**
     * Campo idTipo
     */
    private Integer idTipo;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

    /**
     * Campo dsTitulo
     */
    private String dsTitulo;

    /**
     * Nif del usuario
     */
    private String identificador;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Alegaciones correpondientes a la la Evaluacion
     */
    private List<AlegacionDatoDTO> lstAlegacionDatoDTO;
}
