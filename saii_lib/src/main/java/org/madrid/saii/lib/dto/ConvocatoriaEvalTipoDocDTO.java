package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaEvalTipoDoc
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaEvalTipoDocDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Contructor sin parametros
     */
    public ConvocatoriaEvalTipoDocDTO() {
        super();
        this.tipoDocumentoDTO = new CatalogosValoresDTO();
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
    private Integer idConvocatoriaEvalTipoDoc;


    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idTipoDocumento
     */
    private CatalogosValoresDTO tipoDocumentoDTO;
    
    /**
     * Campo idNivel 
     */
    private CatalogosValoresDTO nivelAsociacionDTO;
    
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
     * Campo itObligatorio 
     */
    private Boolean itObligatorio;


    /**
     * Campo cdFicaPlantilla 
     */
    private Integer cdFicaPlantilla;
    
    public List<ConvocatoriaEvalTipoDocDTO> listadoTipoDocTotalRegistros;

}
