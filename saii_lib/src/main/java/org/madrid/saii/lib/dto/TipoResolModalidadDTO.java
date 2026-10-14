package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de TipoResolucionConvocatoriaDTO
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class TipoResolModalidadDTO extends AuditoriaDTO implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6757769622328789600L;
    
    /**
     * Constructor sin parametros
     */
    public TipoResolModalidadDTO() {
        super();
    }
    
    /**
     * Representa el campo idTipoResolucionConvocatoria
     */
    private Integer idTipoResolModalidad;
    
    /**
     * Representa el campo idModalidad
     */
    private CatalogosValoresDTO idModalidad;
    
    /**
     * Representa el campo cdTipoResolucion
     */
    private String cdTipoResolucion;
    
    /**
     * Representa el campo cdUsuAlta
     */
    private String cdUsuAlta;

    /**
     * Representa el campo fcAltaFila
     */
    private Date fcAltaFila;

    /**
     * Representa el campo cdUsuModif
     */
    private String cdUsuModif;

    /**
     * Representa el campo fcModifFila
     */
    private Date fcModifFila;

    /**
     * Representa el campo cdUsuBaja
     */
    private String cdUsuBaja;

    /**
     * Representa el campo fcBajaFila
     */
    private Date fcBajaFila;

}
