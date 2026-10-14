package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de AlegacionDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class AlegacionDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public AlegacionDatoDTO() {
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
     * Campo idAlegacionDato
     */
    private Integer idAlegacionDato;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idAlegacion
     */
    private Integer idAlegacion;

    /**
     * Campo idAlegacion
     */
    private AlegacionDTO alegacionDTO;

    /**
     * Campo dsValor
     */
    private String dsValor;

}
