package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCom
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ComunicacionNoteActorDTO extends AuditoriaDTO implements Serializable {
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
    public ComunicacionNoteActorDTO() {
        super();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Campo idComunicacionActorCorreo
     */
    private Integer idComunicacionActorNote;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoriaCom;
    
    /**
     * Campo idConvocatoria
     */
    private Integer idActor;


}
