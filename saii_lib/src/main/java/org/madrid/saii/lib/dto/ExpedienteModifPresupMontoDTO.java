package org.madrid.saii.lib.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteModifPresupMonto
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class ExpedienteModifPresupMontoDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteModifPresupMontoDTO() {
        super();
        this.esMontoTotal = Boolean.FALSE;

    }
    
    // Constructor de copia
    public ExpedienteModifPresupMontoDTO(ExpedienteModifPresupMontoDTO original) {
        this.idExpedienteModifPresupMonto = original.getIdExpedienteModifPresupMonto();
        this.idExpedienteModifPresPlazoOtra = original.getIdExpedienteModifPresPlazoOtra();
        this.idExpedientePresupMonto = original.getIdExpedientePresupMonto();
        this.idExpedientePresup = original.getIdExpedientePresup();
        this.nmAnualidad = original.getNmAnualidad();
        this.nmImporte = original.getNmImporte();
        this.esMontoTotal = original.getEsMontoTotal();
        this.nmImporteConcedido = original.getNmImporteConcedido();
        this.cdObservaciones = original.getCdObservaciones();
        this.checkModifMonto = original.getCheckModifMonto();
        this.tipoFinanciacionDTO = original.getTipoFinanciacionDTO();
        }
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Campo idExpedienteModifPresupMonto
     */
    private Integer idExpedienteModifPresupMonto;

    /**
     * Campo idExpedienteModifPresPlazoOtra
     */
    private Integer idExpedienteModifPresPlazoOtra;
    
    /**
     * Campo idExpedientePresupMonto
     */
    private Integer idExpedientePresupMonto;

    /**
     * Campo idExpedientePresup
     */
    private Integer idExpedientePresup;

    /**
     * VMM 11/09/26 MD-2725: partida (tipo de financiación) a la que pertenece este monto,
     * sacada del bloque_componente del dato de origen - permite saber a que partida
     * pertenece un concepto nuevo sin necesitar todavia un ExpedientePresup real.
     */
    private CatalogosValoresDTO tipoFinanciacionDTO;

    /**
     * Campo nmAnualidad
     */
    private Integer nmAnualidad;

    /**
     * Campo nmImporte
     */
    private Double nmImporte;

    @JsonIgnore
    private Boolean esMontoTotal;
    
    /**
     * Campo nmImporteConcedido
     */
    private Double nmImporteConcedido;
    
    private String cdObservaciones;
    
    private String checkModifMonto;
    
    private String tlConcepto;
    
    
    /**
     * Campo nmImporteJustificado
     */
   // private Double nmImporteJustificado;
    
    /**
     * Campo expedienteDocDTO
     */
    // private ExpedienteDocDTO expedienteDocDTO;
    
    /**
     * Campo cdEregPresent
     */
    // private String cdEregPresent;
    
    /**
     * Campo cdEregSubsana
     */
    // private String cdEregSubsana;
    
    /**
     * Campo dsDestinoEreg
     */
    // private String dsDestinoEreg;

    /**
     * Campo fcFirmaPresent
     */
    // private Date fcFirmaPresent;
    
    /**
     * Campo fcFirmaSubsana
     */
    // private Date fcFirmaSubsana;


}
