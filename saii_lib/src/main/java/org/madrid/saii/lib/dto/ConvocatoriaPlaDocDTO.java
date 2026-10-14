package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaPlaDoc
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaPlaDocDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaPlaDocDTO() {
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
     * Campo idConvocatoriaPlaDoc
     */
    private Integer idConvocatoriaPlaDoc;

    /**
     * Campo idConvocatoriaPla
     */
    private Integer idConvocatoriaPla;

    /**
     * Campo dsDocumentacion
     */
    private String dsDocumentacion;

    /**
     * Campo dsObservaciones
     */
    private String dsObservaciones;
}
