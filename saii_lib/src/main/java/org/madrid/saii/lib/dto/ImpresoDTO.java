package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Impreso
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ImpresoDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ImpresoDTO() {
        super();
        this.tipoDTO = new CatalogosValoresDTO();
        this.lstBloqueDTO = new ArrayList<>();
        this.convocatoriaActDTO = new ConvocatoriaActDTO();
        this.imagenLogoDTO = new ImagenDTO();
        this.tipoActorDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idImpreso
     */
    private Integer idImpreso;

    /**
     * Campo itTipo
     */
    private CatalogosValoresDTO tipoDTO;

    /**
     * Campo cdIdentificacion
     */
    private String cdIdentificacion;

    /**
     * Campo convocatoriaActDTO
     */
    private ConvocatoriaActDTO convocatoriaActDTO;

    /**
     * Campo dsDescripcion
     */
    private String dsDescripcion;

    /**
     * Campo itGeneraImpreso
     */
    private String itGeneraImpreso;

    /**
     * Campo dsImpresoOriginal
     */
    private String dsImpresoOriginal;

    /**
     * Campo dsPlantillaTecnica
     */
    private String dsPlantillaTecnica;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo Bloque
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueDTO> lstBloqueDTO;

    /* Campo idImagenLogo */
    private ImagenDTO imagenLogoDTO;

    /***********************************
     * CAMPOS FICA***
     ***********************************/

    /**
     * Código FICA del impreso
     */
    @JsonIgnore
    private Integer cdFicaImpreso;

    /**
     * Código FICA de la plantilla
     */
    @JsonIgnore
    private Integer cdFicaPlantilla;

    /**
     * Código de FICA
     */
    private Integer cdFica;

    /**
     * FicaFichero. Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient FicaFichero ficaFichero;

    /**
     * ficaFicheroPlantTecnica. Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient FicaFichero ficaFicheroImpOriginal;

    /**
     * ficaFicheroPlantTecnica. Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private transient FicaFichero ficaFicheroPlantTecnica;

    /**
     * ficaMetadatosImpOriginal. Se obtiene de fica, no de BBDD
     *
     */
    @JsonIgnore
    private transient FicaMetadatos ficaMetadatosImpOriginal;

    /**
     * ficaMetadatosPlanTecnina. Se obtiene de fica, no de BBDD
     *
     */
    @JsonIgnore
    private transient FicaMetadatos ficaMetadatosPlanTecnica;

    /**
     * FicaMetadatos. Se obtiene de fica, no de BBDD
     *
     */
    @JsonIgnore
    private transient FicaMetadatos ficaMetadatos;

    /**
     * Contenido del fichero.Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private byte[] contenidoFichero;

    /**
     * Contenido del contenidoFicheroImpOriginal.Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private byte[] contenidoFicheroImpOriginal;

    /**
     * Contenido del contenidoFicheroPlanTecnica.Se obtiene de fica, no de BBDD
     */
    @JsonIgnore
    private byte[] contenidoFicheroPlanTecnica;

    /**
     * Identificador de tipo de actor
     */
    @JsonIgnore
    private CatalogosValoresDTO tipoActorDTO;

}
