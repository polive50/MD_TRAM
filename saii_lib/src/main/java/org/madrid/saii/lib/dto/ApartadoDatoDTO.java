package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ApartadoDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ApartadoDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ApartadoDatoDTO() {
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
     * Campo idApartadoDato
     */
    private Integer idApartadoDato;

    /**
     * Campo idExpedienteEval
     */
    private Integer idExpedienteEval;

    /**
     * Campo idApartado
     */
    private Integer idApartado;

    /**
     * Campo dsComentario
     */
    private String dsComentario;

    /**
     * Campo nmNota
     */
    private Integer nmNota;

}
