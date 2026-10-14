package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Informe
 *
 * @author quental
 */
@Getter
@Setter
@ToString
public class InformeDTO extends AuditoriaDTO implements Serializable {
    
    /**
     * Constructor
     */
    public InformeDTO() {
        super();
        this.listEvaluacion = new ArrayList<>();
    }

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Campo idInforme
     */
    private Integer idInforme;
    
    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;
    
    /**
     * Campo dsTitulo
     */
    private String dsTitulo;
    
    /**
     * Campo cdUsuAlta
     */
    private String cdUsuAlta;

    /**
     * Campo fcAltaFila
     */
    private Date fcAltaFila;

    /**
     * Campo cdUsuModif
     */
    private String cdUsuModif;

    /**
     * Campo fcModifFila
     */
    private Date fcModifFila;

    /**
     * Campo cdUsuBaja
     */
    private String cdUsuBaja;

    /**
     * Campo fcBajaFila
     */
    private Date fcBajaFila;
    
    /**
     * Campo idTipoInforme
     */
    private CatalogosValoresDTO tipoInformeDTO;

    /* 
     * Campo imagenLogo
     */
    private ImagenDTO imagenLogoDTO;
    
    /* 
     * Campo imagenLogo2 
     */
    private ImagenDTO imagenLogo2DTO;
    
    @JsonIgnore
    private String dsUnidad;
    
    private List<EvaluacionDTO> listEvaluacion;
    
}
