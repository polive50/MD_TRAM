package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteModifPresPlazoOtra
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteModifPresPlazoOtraDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public ExpedienteModifPresPlazoOtraDTO() {
        super();
        this.estadoDTO = new CatalogosValoresDTO();
        this.estadoResolucionDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteModifPresPlazoOtra
     */
    private Integer idExpedienteModifPresPlazoOtra;
    
    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;
    
    /**
     * campo idModificacion
     */
    private Integer idModificacion;

    /**
     * Campo indicador de modificacion presupuestaria
     */
    private String itModPres;
    
    /**
     * Campo indicador de modificacion plazos
     */
    private String itModPlazo;
    
    /**
     * Campo indicador de otras modificaciones
     */
    private String itModOtra;
    
    /**
     * Campo observaciones
     */
    private String tlObservaciones;
    
    /**
     * Campo fcSolicitud
     */
    private Date fcSolicitud;
    
    /**
     * Campo fcResolucion
     */
    private Date fcResolucion;
    
    /**
     * Campo estadoResolucionDTO
     */
    private CatalogosValoresDTO estadoResolucionDTO;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;
    
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
     * Representa el campo idEstadoRevAdmin del objeto de dominio
     */
    private CatalogosValoresDTO estadoRevAdminDTO;

    /**
     * Representa el campo idImpreso del objeto de dominio
     */
    private Integer idImpreso;
    
    private DocumentoPtfrDTO notificacionResolucionPtfr;
       
}