package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Apartado
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ApartadoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ApartadoDTO() {
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
     * Campo idApartado
     */
    private Integer idApartado;

    /**
     * Campo idEvaluacion
     */
    private Integer idEvaluacion;

    /**
     * Campo dsTitulo
     */
    private String dsTitulo;

    /**
     * Campo tlDescripcion
     */
    private String tlDescripcion;

    /**
     * Campo tlTexto
     */
    private String tlTexto;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;

    /**
     * Campo nmCalificacionMax
     */
    private Integer nmCalificacionMax;
    
    /**
     * Campo idApartadoPadre
     */
    private Integer idApartadoPadre;
    
    /**
     * Campo itInformativo
     */
    private String itInformativo;
    
    private Boolean bolItInformativo;

    /**
     * Campo itObligatorio
     */
    private String itObligatorio;
    
    private Boolean bolItObligatorio;
    
    private String dsTituloPadre;

}
