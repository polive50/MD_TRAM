package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ListadoColumna
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ListadoColumnaDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ListadoColumnaDTO() {
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
     * Campo idListadoColumna
     */
    private Integer idListadoColumna;

    /**
     * Campo idListado
     */
    private Integer idListado;

    /**
     * Campo idBloqueComponente
     */
    private Integer idBloqueComponente;

    /**
     * Campo idComponente
     */
    private Integer idComponente;

    /**
     * Campo idTipoFinanciacion
     */
    private Integer idTipoFinanciacion;

    /**
     * Campo idTematica
     */
    private Integer idTematica;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

}
