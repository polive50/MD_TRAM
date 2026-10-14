package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ListadoConfiguracion
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ListadoConfiguracionDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */

    public ListadoConfiguracionDTO() {
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
     * Campo idConvocatoria
     */

    private Integer idConvocatoria;

    /**
     * Campo cdAcronimo
     */

    private String cdAcronimo;

    /**
     * Campo idImpreso
     */

    private Integer idImpreso;

    /**
     * Campo cdIdentificacion
     */

    private String cdIdentificacion;

    /**
     * Campo idBloque
     */

    private Integer idBloque;

    /**
     * Campo idTematica
     */

    private Integer idTematica;

    /**
     * Campo dsValor
     */

    private String dsTematica;

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
     * Campo dsNombre
     */

    private String dsComponente;

    /**
     * Campo idListadoColumna
     */

    private Integer idListadoColumna;

    /**
     * Campo nmOrdenListadoColumna
     */
    private Integer nmOrdenListadoColumna;

    /**
     *
     */
    private String dsNombreElemento;

}
