package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaDoc
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaDocDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Contructor sin parametros
     */
    public ConvocatoriaDocDTO() {
        super();
        this.idTipoDocumento = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idDocumento
     */
    private Integer idDocumento;

    /**
     * Campo cdFica
     */
    private Integer cdFica;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idTipoDocumento
     */
    private CatalogosValoresDTO idTipoDocumento;

    /**
     * Nombre del fichero dsNombreFichero.
     */
    private String dsNombreFichero;

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
