package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Solicitud
 *
 * @author Madrid Digital
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class SolicitudDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public SolicitudDTO() {
        this.interesadoDTO = new InteresadoDTO();
        this.convocatoriaDTO = new ConvocatoriaDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.tipoActorDTO = new CatalogosValoresDTO();
        this.itExpeCoor = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idSolicitud
     */
    private Integer idSolicitud;

    /**
     * Representa el campo cdNote del objeto de dominio
     */
    @JsonIgnore
    private String cdNote;

    /**
     * Campo idInteresado
     */
    private InteresadoDTO interesadoDTO;

    /**
     * Campo convocatoriaDTO
     */
    private ConvocatoriaDTO convocatoriaDTO;

    /**
     * Campo idEstado
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo dsTipoOrganismo
     */
    private String dsTipoOrganismo;

    /**
     * Campo tipoActor
     */
    private CatalogosValoresDTO tipoActorDTO;

    /**
     * Código de expediente de la solicitud
     */
    private String cdExpedienteSol;

    /**
     * Campo alertaTramitesPendiente
     */
    @JsonIgnore
    private Integer alertaTramitesPendiente;

    /**
     * Campo alertaRequisitos
     */
    @JsonIgnore
    private Integer alertaRequisitos;

    /**
     * Campo alertaDocumentos
     */
    @JsonIgnore
    private Integer alertaDocumentos;

    /**
     * Submod
     */
    private ConvocatoriaSubmodDTO convocatoriaSubmodDTO;

    /**
     * Area
     */
    private ConvocatoriaAreaDTO convocatoriaAreaDTO;
    
    /**
     * Subarea
     */
    private ConvocatoriaAreaSubareaDTO convocatoriaAreaSubareaDTO;

    /**
     * Representa el indicador si la solicitud tiene un expediente coordinador
     */
    private Boolean itExpeCoor;
    
    /**
     * Campo alertaDocumentosJCT
     */
    @JsonIgnore
    private Integer alertaDocumentosJCT;
    
    /**
     * Campo alertaDocumentosContrato
     */
    @JsonIgnore
    private Integer alertaDocumentosCont;
    
    /**
     * Campo alertaDocumentosModificacionesPPO
     */
    @JsonIgnore
    private Integer alertaDocumentosModif;
    
    /**
     * Campo alertaDocumentosJEco
     */
    @JsonIgnore
    private Integer alertaDocumentosJEco;
    
    /**
     * Indica si la identificacion es representante legal
     */
    private Boolean isRepresentanteLegal;
    
    /**
     * Indica el  representante legal
     */
    private InteresadoDTO interesadoRepresentado;
    
    @JsonIgnore
    private String dniUserLogin;
    
    @JsonIgnore
    private String cifEntidadLogin;
    
    @JsonIgnore
    private Date fcPresentacion;

    @JsonIgnore
    private Integer alertaDocumentosModifExtra;
    
    /**
     * Indica el  representante legal
     */
    private InteresadoDTO interesadoAutorizado;


}
