package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaPla
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaPlaDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaPlaDTO() {
        super();
        this.tramiteDTO = new CatalogosValoresDTO();
        this.imagenDTO = new ImagenDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaPla
     */
    private Integer idConvocatoriaPla;

    /**
     * campo cdPlantilla
     */
    private String cdPlantilla;

    /**
     * campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     *
     */
    private List<ConvocatoriaPlaFirDTO> lstConvocatoriaPlaFirDTO;

    /**
     *
     */
    private List<ConvocatoriaPlaVerDTO> lstConvocatoriaPlaVerDTO;

    @JsonIgnore
    private String dsFirmante;

    @JsonIgnore
    private String dsVerificador;

    @JsonIgnore
    private String dsPlantilla;

    @JsonIgnore
    private List<String> lstDsFirmantes;

    @JsonIgnore
    private List<String> lstDsVerificadores;

    @JsonIgnore
    private Integer numFirmantes;

    @JsonIgnore
    private Integer numVerificadores;

    /**
     * Campo tramiteDTO
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Campo imagenDTO;
     */
    private ImagenDTO imagenDTO;

    /**
     * Campo dsAsunto;
     */
    private String dsAsunto;

    /**
     * Campo dsDescripcion;
     */
    private String dsDescripcion;

    /**
     * Campo dsDescripcionInf;
     */
    private String dsDescripcionInf;
    
    /**
    *
    */
   private List<ConvocatoriaPlaDocDTO> lstConvocatoriaPlaDocDTO;
   
   /**
    * Campo para saber qué documentos poner en la subsanación administrativa
    * si es para descargar el borrador el estado de los documentos a mostrar debe ser 112 = Requeire subsanación
    * Si es para enviar al portafirmas el estado de lso documentos a mostrar debe ser 120 = Enviado a la cola de notificaciones
    */
   private Integer idEstaDocuSubaAdmi;
   
}
