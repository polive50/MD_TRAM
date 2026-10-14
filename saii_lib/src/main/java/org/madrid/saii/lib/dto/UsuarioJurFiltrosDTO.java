package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsuarioJurFiltrosDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public UsuarioJurFiltrosDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    private String cdNumIden;

    private String dsNombre;

    private String dsApellido1;

    private String dsApellido2;

    private String cif;

}
