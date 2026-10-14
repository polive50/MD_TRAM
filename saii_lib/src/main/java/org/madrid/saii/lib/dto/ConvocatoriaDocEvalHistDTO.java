package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaDocEvalHist
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaDocEvalHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Contructor sin parametros
     */
    public ConvocatoriaDocEvalHistDTO() {
        super();
        this.persona= new InteresadoDTO();
        this.idTipoDocumento = new CatalogosValoresDTO();
        this.estado = new CatalogosValoresDTO();
        this.estilo = StringUtils.EMPTY;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idDocumentoHist
     */
    private Integer idDocumentoHist;
    
    /**
     * Campo idConvocatoriaDocEval;
     */
    private Integer idConvocatoriaDocEval;

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
    
    /**
     * interesado del documento
     */
    
    private InteresadoDTO persona;
    
    /**
     * itCoorEval
     */
    private String itCoorEval;
    
    /**
     * Representa el campo estado del documento
     */
    private CatalogosValoresDTO estado;
    
    /**
     * Indica el estilo del registro
     */
    @JsonIgnore
    private String estilo;
    
    /**
     * Campo tlCausaDescripcion
     */
    private String tlCausaDescripcion;

}
