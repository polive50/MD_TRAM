package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoFicaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    public DocumentoFicaDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    private static final long serialVersionUID = 1L;

    private Integer cdFica;

    private String nombreFichero;

    private Date fechaCreacion;

    private Integer idEstado;

    private String estado;

    private ExpedienteDTO expedienteDTO;

    private Integer idExpediente;

    private CatalogosValoresDTO tipoDocDTO;

    private Integer idTipoDoc;

    /**
     * FicaFichero
     */
    @JsonIgnore
    private transient FicaFichero ficaFichero;

    @JsonIgnore
    private transient FicaMetadatos ficaMetadatos;

    @JsonIgnore
    private byte[] contenidoFichero;

}
