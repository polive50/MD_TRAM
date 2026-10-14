package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Componente.
 *
 * @author everis
 */

@Getter
@Setter
@ToString
public class ComponenteDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos.
     */
    public ComponenteDTO() {
        super();
        this.categoriaDTO = new CatalogosValoresDTO();
        this.tipoDTO = new CatalogosValoresDTO();
        this.subTipoDTO = new CatalogosValoresDTO();
        this.subTipoCatalogoDTO = new CatalogosDTO();
        this.reglaDTO = new CatalogosValoresDTO();
        this.tipoDocumentoDTO = new CatalogosValoresDTO();
        this.utilizado = Boolean.FALSE;
        this.esFila = Boolean.FALSE;
        this.esColumna = Boolean.FALSE;
        this.itEditable = Boolean.TRUE;
        this.lngCalculoEtiquetaPDF = 0f;
        this.lngCalculoCampoPDF = 0f;
        this.lngCalculoEspacioPDF = 0f;
        this.lngPorcentajeEtiquetaPDF = 0f;
        this.lngPorcentajeCampoPDF = 0f;
        this.lngPorcentajeSobraPDF = 0f;
        this.lstComboItems = new ArrayList<>();
        this.lstRadioItems = new ArrayList<>();
        this.lstCheckItems = new ArrayList<>();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /** Identificador del numero de serie para serialización. */
    private static final long serialVersionUID = 1L;

    /** Campo idComponente. */
    private Integer idComponente;

    /** Campo dsNombre. */
    private String dsNombre;

    /** Campo nmLongitud. */
    private Integer nmLongitud;

    /** Campo idTipo. */
    private CatalogosValoresDTO tipoDTO;

    /** Campo idSubTipo. */
    private CatalogosValoresDTO subTipoDTO;
    
    /** Campo idSubTipoCatalogo */
    private CatalogosDTO subTipoCatalogoDTO;

    /** Campo idTipoDocumento. */
    private CatalogosValoresDTO tipoDocumentoDTO;

    /** Campo dsCategoria. */
    private CatalogosValoresDTO categoriaDTO;

    /** Campo idRegla. */
    private CatalogosValoresDTO reglaDTO;

    /** Representa el campo dsValorMultiple del objeto de dominio. */
    private String dsValorMultiple;

    /** Representa el campo tlTextoFijo del objeto de dominio. */
//    private String tlTextoFijo;

    /** Campo itEditable */
    @JsonIgnore
    private Boolean itEditable;

    /** Campo para indicar si se ha utilizado */
    @JsonIgnore
    private Boolean utilizado;

    /** Variable para indicar si se ha marcado como fila */
    @JsonIgnore
    private Boolean esFila;

    /** Variable para indicar si se ha marcado como columna */
    @JsonIgnore
    private Boolean esColumna;

    /** Variable cálculo etiqueta PDF. */
    @JsonIgnore
    private Float lngCalculoEtiquetaPDF;

    /** Variable cálculo campo PDF. */
    @JsonIgnore
    private Float lngCalculoCampoPDF;

    /** Variable cálculo espacio entre componente PDF. */
    @JsonIgnore
    private Float lngCalculoEspacioPDF;

    /** Porcentaje etiqueta PDF. */
    @JsonIgnore
    private Float lngPorcentajeEtiquetaPDF;

    /** Porcentaje campo PDF. */
    @JsonIgnore
    private Float lngPorcentajeCampoPDF;

    /** Porcentaje sobra PDF. */
    @JsonIgnore
    private Float lngPorcentajeSobraPDF;

    /** Indica si el componente puede relacionarse */
    @JsonIgnore
    private Boolean puedeRelacionar;

    @JsonIgnore
    private List<String> lstComboItems;

    @JsonIgnore
    private List<String> lstRadioItems;
    
    @JsonIgnore
    private List<String> lstCheckItems;
    
    /**
     * Campo nmConstante
     */
    private Integer nmConstante;
    
    private String clTextoFijo;

}
