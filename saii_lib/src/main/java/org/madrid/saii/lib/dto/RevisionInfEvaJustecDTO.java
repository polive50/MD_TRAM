package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteJusTec;

import lombok.Data;

/**
 * DTO de RevisionInfEvaJustec
 *
 * @author quental
 * @version 1.0.0
 */
@Data
public class RevisionInfEvaJustecDTO implements Serializable {

    
    /**
     * campo IdRevInfEvaJustec
     */
    private Integer idRevInfEvaJustec;

    /**
     * campo expediente 
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * campo expedienteJusTec 
     */
    private ExpedienteJusTecDTO expedienteJusTecDTO;
    
    
    /**
     *campo expedienteDoc 
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    /**
     * campo idEstado 
     */
    private CatalogosValoresDTO idEstadoDTO;
    
    /**
     * campo tlObservaciones 
     */
    private String tlObservaciones;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */ 
    public RevisionInfEvaJustecDTO() {
        super();
        this.expedienteDocDTO=new ExpedienteDocDTO();
        this.expedienteDTO=new ExpedienteDTO();
        this.expedienteJusTecDTO= new ExpedienteJusTecDTO();
        this.idEstadoDTO=new CatalogosValoresDTO();
    }
    
    public String getNombreInforme() {
        LocalDate fecha=expedienteDocDTO.getFcAltaFila().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return "Revisión del Informe de evaluación Justificación Científico Técnica_"+fecha.getYear()+"/"
                +(fecha.getMonthValue()>9?fecha.getMonthValue():"0"+fecha.getMonthValue())
                +"/"+(fecha.getDayOfMonth()>9?fecha.getDayOfMonth():"0"+fecha.getDayOfMonth());
    }
}
