package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConfiguracionDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos.
     */
    
    public ConfiguracionDTO() {
        super();
        this.idConfiguracion=null;
        this.cdParametro=null;
        this.cdValor=null;
        
    }

    /** Identificador del numero de serie para serialización. */
    private static final long serialVersionUID = 1L;
    
    /** Campo idConfiguracion. */
    private Integer idConfiguracion;
    
    /** Campo cdParametro. */
    private String cdParametro;
    
    /** Campo cdValor. */
    private String cdValor;
}
