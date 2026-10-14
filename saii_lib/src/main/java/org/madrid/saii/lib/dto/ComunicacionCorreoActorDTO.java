package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCom
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ComunicacionCorreoActorDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * 
     */
    private static final long serialVersionUID = 425162752606473745L;

    /**
     * Constructor sin argumentos
     */
    public ComunicacionCorreoActorDTO() {
        super();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Campo idComunicacionActorCorreo
     */
    private Integer idComunicacionActorCorreo;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoriaCom;
    
    /**
     * Campo idConvocatoria
     */
    private Integer idActor;


}
