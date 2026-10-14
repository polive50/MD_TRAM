package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ApartadoJustecDato
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ApartadoJustecDatoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ApartadoJustecDatoDTO() {
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
     * Campo idApartadoJustecDato
     */
    private Integer idApartadoJustecDato;

    /**
     * Campo idExpedienteEvalJustec
     */
    private Integer idExpedienteEvalJustec;

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
