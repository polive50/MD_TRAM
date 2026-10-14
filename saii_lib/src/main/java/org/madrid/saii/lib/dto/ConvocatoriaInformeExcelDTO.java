package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Interesado
 *
 * @author Madrid Digital
 * @version 2.0.0
 */
@Getter
@Setter
@ToString
public class ConvocatoriaInformeExcelDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaInformeExcelDTO() {
        super();
        this.listaCamposDinamicos = new LinkedHashMap<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable que representa el identificador de estado de expediente
     */
    private Integer idEstadoExpediente;

    /**
     * Variable que representa el acrónimo de la convocatoria
     */
    private String cdAcronimoConvocatoria;

    /**
     * Variable que representa el id de expediente
     */
    private Integer idExpediente;

    /**
     * Variable que representa el código de expediente
     */
    private String cdExpediente;

    /**
     * Variable que representa el nombre del interesado
     */
    private String dsNombreInteresado;

    /**
     * Variable que representa el identificador del interesado
     */
    private String cdIdentificadorInteresado;

    /**
     * Variable que representa la descripción del tipo de identificador del
     * interesado
     */
    private String dsTipoIdentificadorInteresado;

    /**
     * Variable que representa la nota final del expediente
     */
    private String nmNotaFinalExpediente;

    /**
     * Variable que representa la lista de información dinámica
     */
    private LinkedHashMap<String, String> listaCamposDinamicos;

    /**
     * Variable que representa el itAlegaciones
     */
    private String itAlegaciones;

    /**
     * Variable que representa la descripcion del estado
     */
    private String dsValor;

}
