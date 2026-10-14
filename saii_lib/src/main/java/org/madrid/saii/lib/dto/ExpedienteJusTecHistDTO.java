package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpedienteJusTecHistDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteJusTecHistDTO() {
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
     * Representa el campo idExpedienteJusTecHist del objeto de dto
     */
    private Integer idExpedienteJusTecHist;

    /**
     * Representa el campo idExpedienteJusTec del objeto de dto
     */
    private Integer idExpedienteJusTec;

    /**
     * Representa el campo idExpediente del objeto de dto
     */
    private Integer idExpediente;

    /**
     * Representa el campo tipoExpedienteJusTecDTO del objeto de dto
     */
    private CatalogosValoresDTO tipoExpedienteJusTecDTO;

    /**
     * Representa el campo nmPeriodo del objeto de dto
     */
    private Integer nmPeriodo;

    /**
     * Representa el campo fcIniJustiCienTec del objeto de dto
     */
    private Date fcIniJustiCienTec;

    /**
     * Representa el campo fcFinJustiCienTec del objeto de dto
     */
    private Date fcFinJustiCienTec;

    /**
     * Representa el campo tlObservacion del objeto de dto
     */
    private String tlObservacion;

    /**
     * Representa el campo tlObservacionParaCiudadano del objeto de dto
     */
    private String tlObservacionParaCiudadano;

    /**
     * Representa el campo tlObservacionDelCiudadano del objeto de dto
     */
    private String tlObservacionDelCiudadano;
    
    /**
     * Representa el campo idEstadoRevAdminDTO del objeto de dto
     */
    private CatalogosValoresDTO idEstadoRevAdminDTO;
    
    /**
     * Representa el campo idEstadoDTO del objeto de dto
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

}
