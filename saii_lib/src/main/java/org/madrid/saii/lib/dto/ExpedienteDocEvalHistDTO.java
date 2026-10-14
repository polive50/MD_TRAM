package org.madrid.saii.lib.dto;

import java.io.Serializable;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.domain.ExpedienteDocEval;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteDocEvalHist
 *
 * @author everis
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteDocEvalHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Contructor sin parametros
     */
    public ExpedienteDocEvalHistDTO() {
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
     * Campo idDocumentoHist
     */
    private Integer idDocumentoHist;

    /**
     * Campo cdFica
     */
    private Integer cdFica;

    /**
     * Campo idExpedienteEval
     */
    private ExpedienteEvalDTO idExpedienteEval;
    
    /**
     * Campo expedienteDocEval
     */
    private ExpedienteDocEvalDTO expedienteDocEval;

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
