package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteDocHist
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteDocHistDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteDocHistDTO() {
        super();
        this.tipoDocumentoDTO = new CatalogosValoresDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.tramiteDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteDocHist
     */
    private Integer idExpedienteDocHist;

    /**
     * Campo idExpedienteDoc
     */
    private ExpedienteDocDTO expedienteDocDTO;

    /**
     * Campo cdFica
     */
    private Integer cdFica;

    /**
     * Campo cdAnexoEreg
     */
    private String cdAnexoEreg;

    /**
     * Campo cdAnexoEregPadre
     */
    private String cdAnexoEregPadre;

    /**
     * Campo idEstado
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo tlCausaDescripcion
     */
    private String tlCausaDescripcion;

    /**
     * Campo tlObservaciones
     */
    private String tlObservaciones;

    /**
     * Campo idBloqueComponente
     */
    private BloqueComponenteDTO bloqueComponenteDTO;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo idDocumentoPtfr
     */
    private Integer idDocumentoPtfr;

    /**
     * Campo idConvocatoriaCau
     */
    private ConvocatoriaCauDTO convocatoriaCauDTO;
    
    /**
     * Campo idConvocatoriaCauCont
     */
    private ConvocatoriaCauContDTO convocatoriaCauContDTO;

    /**
     * Campo auxiliar tipo de documento
     */
    private CatalogosValoresDTO tipoDocumentoDTO;

    /**
     * Indica si se muestra el boton ver observaciones
     */
    @JsonIgnore
    private Boolean mostrarBotonObservaciones;

    /**
     * Campo tramiteDTO
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Representa si es un documento adicional
     */
    private String itAdicional;
    
    /**
     * Campo idCausa
     */
    private Integer idCausa;
    
    /**
     * Campo causaCatalogoTexto
     */
    
    private String causaCatalogoTexto;
    
    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcRegistro;
    
    /**
     * Representa el campo tlObservacionesGestor del objeto de dominio
     */
    private String codigoEreg;
    
    /**
     * Representa el campo tlObservacionesGestor del objeto de dominio
     */
    private String tlObservacionesGestor;

}
