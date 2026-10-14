package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaGestor
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaGestorDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaGestorDTO() {
        super();
        this.cdNumIden = StringUtils.EMPTY;
        this.dsApellido1 = StringUtils.EMPTY;
        this.dsApellido2 = StringUtils.EMPTY;
        this.dsNombre = StringUtils.EMPTY;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaGestor
     */
    private Integer idConvocatoriaGestor;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo cdUsuario
     */
    private String cdUsuario;

    /**
     * Campo cdNumIden
     */
    private String cdNumIden;

    /**
     * Campo dsApellido1
     */
    private String dsApellido1;

    /**
     * Campo dsApellido2
     */
    private String dsApellido2;

    /**
     * Campo dsNombre
     */
    private String dsNombre;
    
    
    /**
     * Campo tipoTramite
     */
    private Integer tipoTramite;

}
