package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteContratoDTO extends AuditoriaDTO implements Serializable {
    
 // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteContratoDTO() {
        super();
        this.idEstadoRevAdminDTO = new CatalogosValoresDTO();
        this.idEstadoDTO = new CatalogosValoresDTO();
        this.idTipoContratoDTO = new CatalogosValoresDTO();
        this.idCausaDTO = new CatalogosValoresDTO();
        this.pendienteIncicioPlazo = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    
    /**
     * Representa el campo idExpedienteContrato del objeto de dominio
     */
    private Integer idExpedienteContrato;

    /**
     * Representa el campo idExpediente del objeto de dominio
     */
    private Integer idExpediente;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private CatalogosValoresDTO idEstadoDTO;
    
    
    /**
     * Representa el campo idTipoContrato del objeto de dominio
     */
    private CatalogosValoresDTO idTipoContratoDTO;
    
    /**
     * Representa el campo fcIniContrato del objeto de dominio
     */
    private Date fcIniContrato;
    
    /**
     * Representa el campo fcFinContrato del objeto de dominio
     */
    private Date fcFinContrato;
    
    /**
     * Representa el campo itIndefinido del objeto de dominio
     */
    private String itIndefinido;
     
    /**
     * Representa el campo idCausa del objeto de dominio
     */
    private CatalogosValoresDTO idCausaDTO;
     
    /**
     * Representa el campo tlCausaDescripcion del objeto de dominio
     */
    private String tlCausaDescripcion;
    
    
    /**
     * Representa el campo tlObservaciParaCiudadano del objeto de dominio
     */
    private String tlObservaciParaCiudadano;
    
    /**
     * Representa el campo tlObservaciDelCiudadano del objeto de dominio
     */
    private String tlObservaciDelCiudadano;
    
    
    /**
     * Representa el campo cdUsuAlta del objeto de dominio
     */
    private String cdUsuAlta;


    /**
     * Representa el campo fcAltaFila del objeto de dominio
     */
    private Date fcAltaFila;
    
    
    /**
     * Representa el campo cdUsuModif del objeto de dominio
     */
    private String cdUsuModif;
    
    /**
     * Representa el campo fcModifFila del objeto de dominio
     */
    private Date fcModifFila;
    
    /**
     * Representa el campo cdUsuBaja del objeto de dominio
     */
    private String cdUsuBaja;

    /**
     * Representa el campo fcBajaFila del objeto de dominio
     */
    private Date fcBajaFila;
    
    /**
     * Representa el campo cdEreg de la presentacion del objeto de dominio
     */
    
    private String cdEregPresent;
    
    /**
     * Representa el campo cdEreg de la subsanacion del objeto de dominio
     */
    
    private String cdEregSubsana;

    /**
     * Representa el campo dsDestinoEreg del objeto de dominio
     */
    
    private String dsDestinoEreg;

    /**
     * Representa el campo fcFirma de la presentación del objeto de dominio
     */
    
    private Date fcFirmaPresent;
    
    /**
     * Representa el campo fcFirma de la subsanación del objeto de dominio
     */
    
    private Date fcFirmaSubsana;
    
    /**
     * Representa el campo idEstadoRevAdminDTO del objeto de dominio
     */
    
    private CatalogosValoresDTO idEstadoRevAdminDTO;
    
    /**
     * Representa el campo fcRenuncia del objeto de dominio
     */
   
    private Date fcRenuncia;
    
    /**
     * Representa el campo interesadoDTO del objeto de dominio
     */
    
    private InteresadoDTO interesadoDTO;
    
    
    private String cdEregRenuncia; 
    
    /**
     * Representa el campo fcFirmaRenuncia de la presentación del objeto de dominio
     */
    
    private Date fcFirmaRenuncia;
    
    /**
     * Flag que recoge si el contrato está pendiente de subsanar pero aún no ha llegado el plazo de inicio de subsanacion
     */
    private Boolean pendienteIncicioPlazo;

}
