package org.madrid.saii.lib.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCoorFiltrosDTO
 *
 * @author nttdata
 */

@Getter
@Setter
@ToString
public class ConvocatoriaCoorFiltrosDTO {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ConvocatoriaCoorFiltrosDTO() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Representa el campo nmAno del objeto de dominio
     */
    private Integer nmAno;

    /**
     * Representa el campo cdAcronimo del objeto de dominio
     */
    private String cdAcronimo;

    /**
     * Representa el campo cdTitulo del objeto de dominio
     */
    private String cdTitulo;

    /**
     * Variable que representa la descripción de la convocatoria
     */
    private String dsDescripcion;

    /**
     * Representa el campo idArea del objeto de dominio
     */
    private Integer idAreaCientifica;

    /**
     * Representa el campo dsValor del objeto de dominio
     */
    private String dsAreaCientifica;

    /**
     * Representa el campo fcIniTramiEval del objeto de dominio
     */
    private Date fcIniTramiEval;

    /**
     * Representa el campo fcFinTramiEval del objeto de dominio
     */
    private Date fcFinTramiEval;

    /**
     * Representa el campo idConvocatoriaCoor del objeto de dominio
     */
    private Integer idConvocatoriaCoor;

    /**
     * Representa el campo idConvocatoria del objeto de dominio
     */
    private Integer idConvocatoria;

    /**
     * Representa el campo idInteresado del objeto de dominio
     */
    private Integer idInteresado;

    /**
     * Representa el campo cdNumIden del objeto de dominio
     */
    private String cdNumIden;

    /**
     * Representa el campo idEstado del objeto de dominio
     */
    private Integer idEstado;

    /**
     * Representa el campo dsValor del objeto de dominio
     */
    private String dsEstado;
    /**
     * Representa el campo index del objeto de dominio
     */
    private Integer index;

    /**
     * Representa el campo pageSize del objeto de dominio
     */
    private Integer pageSize;
    
    private String itCoorEval;
    
    private Boolean documentosConvocatoriaSubidos;

    private Boolean documentosConvocatoriaRevisados;

}
