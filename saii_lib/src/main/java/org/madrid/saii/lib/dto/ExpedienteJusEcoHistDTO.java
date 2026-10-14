package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteJusEcoHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteJusEcoHistDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Representa el campo idExpedienteJusEcoHist del objeto de dto
     */
    private Integer idExpedienteJusEcoHist;

    /**
     * Representa el campo idExpedienteJusEco del objeto de dto
     */
    private Integer idExpedienteJusEco;

    /**
     * Representa el campo idExpediente del objeto de dto
     */
    private Integer idExpediente;

    /**
     * Representa el campo tipoExpedienteJusEcoDTO del objeto de dto
     */
    private CatalogosValoresDTO tipoExpedienteJusEcoDTO;

    /**
     * Representa el campo nmPeriodo del objeto de dto
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiEconomica del objeto de dto
     */
    private Date fcIniJustiEconomica;

    /**
     * Representa el campo fcFinJustiEconomica del objeto de dto
     */
    private Date fcFinJustiEconomica;

    /**
     * Representa el campo tlObservacion del objeto de dto
     */
    private String tlObservacion;
    
    /**
     * Representa el campo tipoExpedienteJusEcoDTO del objeto de dto
     */
    private CatalogosValoresDTO idEstadoRevAdminDTO;
    
    /**
     * Representa el campo idEstado del objeto de dto
     */
    private CatalogosValoresDTO idEstadoDTO;
    
    
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
     * Representa el campo tlObservacionParaCiudadano del objeto de dominio
     */
    private String tlObservacionParaCiudadano;

    private String tlObservacionParaCiudadanoMod;
    
    /**
     * Representa el campo tlObservacionDelCiudadano del objeto de dominio
     */
    private String tlObservacionDelCiudadano;

}
