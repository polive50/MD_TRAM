package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Imagen
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ImagenDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ImagenDTO() {
        super();
        this.fondoEurDTO = new CatalogosValoresDTO();

    }

    /* Constructor con argumentos */
    public ImagenDTO(Integer idImagen) {
        super();
        this.idImagen = idImagen;
        this.fondoEurDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idImagen
     */
    private Integer idImagen;

    /**
     * Campo idFondoEur
     */
    private CatalogosValoresDTO fondoEurDTO;

    /**
     * Campo cdUnidad
     */
    private Integer cdUnidad;

    /**
     * Campo cdFica
     */
    private String cdFica;

    /** campo dsNombre ***/
    private String dsNombre;

    /***********************************
     * CAMPOS FICA***
     ***********************************/
    /**
     * FicaFichero. Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient FicaFichero ficaFichero;

    /**
     * FicaMetadatos. Se obtiene de fica, no de BBDD
     *
     */
    @JsonIgnore
    private transient FicaMetadatos ficaMetadatos;

    /**
     * Contenido del fichero.Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private byte[] contenidoFichero;

}
