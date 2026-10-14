package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de BloqueComponente.
 *
 * @author everis
 */
@Getter
@Setter
@ToString
public class BloqueComponenteDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor sin argumentos.
     */
    public BloqueComponenteDTO() {
        super();
        this.componenteDTO = new ComponenteDTO();
        this.datos = new ArrayList<DatoDTO>();
        this.documentos= new ArrayList<ExpedienteDocDTO>();
    }
    
    /**
     * Constructor de copia.
     */
    public BloqueComponenteDTO(BloqueComponenteDTO original) {
        this.tipoFinanciacionDTO = original.getTipoFinanciacionDTO();
        this.componenteDTO = original.getComponenteDTO();
        this.datos = new ArrayList<>();
        for (DatoDTO dato : original.datos) {
            this.datos.add(new DatoDTO(dato));
        }
        this.documentos= new ArrayList<>();
        for(ExpedienteDocDTO documento:original.documentos) {
           this.documentos.add(documento);
        }
        
        this.idBloqueComponente = original.idBloqueComponente;
        this.idBloque = original.idBloque;
        this.nmOrden = original.nmOrden;
        this.itFila = original.itFila;
        this.itColumna = original.itColumna;
        this.itObligatorio = original.itObligatorio;
        this.itRelacion = original.itRelacion;
        this.itSeleccionado = original.itSeleccionado;
        this.tramiteDTO = original.tramiteDTO;

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /** Identificador del numero de serie para serialización. */
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer idBloqueComponente;

    /** Campo idComponente. */
    private ComponenteDTO componenteDTO;

    /** Campo idBloque. */
    private Integer idBloque;

    /** Campo nmOrden. */
    private Integer nmOrden;

    /** Lista de datos */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<DatoDTO> datos;

    /** Campo itFila */
    private Boolean itFila;

    /** Campo itColumna */
    private Boolean itColumna;

    /** Campo obligatorio. */
    private Boolean itObligatorio;

    /** Campo itRelacion. */
    private Boolean itRelacion;

    /** Campo tipoFinanciacionDTO. */
    private CatalogosValoresDTO tipoFinanciacionDTO;

    /** Campo itSeleccionado. */
    private Boolean itSeleccionado;

    /** Representa el campo tramite del objeto de dominio. */
    private CatalogosValoresDTO tramiteDTO;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExpedienteDocDTO> documentos;
    

}
