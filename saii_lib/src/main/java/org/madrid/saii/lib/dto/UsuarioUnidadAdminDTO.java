package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsuarioUnidadAdminDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public UsuarioUnidadAdminDTO() {
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
     * Campo idUsuarioUnidadAdmin
     */
    private Integer idUsuarioUnidadAdmin;
    
    /**
     * Representa el campo usuario del objeto de dominio
     */
    private UsuarioDTO usuario;

    /**
     * Representa el campo UnidadAdmin del objeto de dominio
     */
    private UnidadAdminDTO unidadAdmin;
    
    private String dsUsuario;
    
    /**
     * Campo idDireccion
     */
    private Integer idDireccion;
    
    /**
     * Campo idSubDireccion
     */
    private Integer idSubDireccion;


}
