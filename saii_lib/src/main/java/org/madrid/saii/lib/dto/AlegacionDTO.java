package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Alegacion
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class AlegacionDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public AlegacionDTO() {
        super();
        this.tipoDTO = new CatalogosValoresDTO();
        this.imagenLogoDTO = new ImagenDTO();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idAlegacion
     */
    private Integer idAlegacion;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idTipo
     */
    private CatalogosValoresDTO tipoDTO;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

    /**
     * Campo dsTitulo
     */
    private String dsTitulo;

    /* Campo idImagenLogo */
    private ImagenDTO imagenLogoDTO;

}
