package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCom
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ConvocatoriaComDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaComDTO() {
        super();
        this.tramiteDTO = new CatalogosValoresDTO();
        this.convocatoriaActNoteDTO = new ConvocatoriaActDTO();
        this.convocatoriaActCorreoDTO = new ConvocatoriaActDTO();
        this.lstActoresNoteDTO = new ArrayList<>();
        this.lstActoresCorreoDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idConvocatoriaCom
     */
    private Integer idConvocatoriaCom;

    /**
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo idTramite
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Campo idConvocatoriaActNote
     */
    private ConvocatoriaActDTO convocatoriaActNoteDTO;

    /**
     * Campo idConvocatoriaActCorreo
     */
    private ConvocatoriaActDTO convocatoriaActCorreoDTO;

    /**
     * Campo blNote
     */
    private Boolean blNote;

    /**
     * Campo blCorreo
     */
    private Boolean blCorreo;
    
    /**
     * Campo correosActores
     */
    private String correosActores;
    
    /**
     * Campo noteActores
     */
    private String noteActores;
    
    /**
     * Campo itNote
     */
    private String itNote;

    /**
     * Campo itCorreo
     */
    private String itCorreo;
    
    /**
     * Campo fcBajaFila
     */
    private Date fcBajaFila;

    private List<ComunicacionNoteActorDTO> lstActoresNoteDTO;
    
    private List<ComunicacionCorreoActorDTO> lstActoresCorreoDTO;
}
