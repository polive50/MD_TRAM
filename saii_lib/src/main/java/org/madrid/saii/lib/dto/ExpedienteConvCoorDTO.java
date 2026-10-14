package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ConvocatoriaCoor
 *
 * @author nttdata
 */
@Getter
@Setter
@ToString
public class ExpedienteConvCoorDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteConvCoorDTO() {
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
     * Campo idConvocatoria
     */
    private Integer idConvocatoria;

    /**
     * Campo cdAcronimo
     */
    private String cdAcronimo;

    /**
     * Campo cdNumIdenEval
     */
    private String cdNumIdenEval;

    /**
     * Campo idExpediente
     */
    private Integer idExpediente;

    /**
     * Campo cdExpediente
     */
    private String cdExpediente;

    /**
     * Campo idTipoPersonaFisJur
     */
    private Integer idTipoPersonaFisJur;

    /**
     * Campo dsTipoPersonaFisJur
     */
    private String dsTipoPersonaFisJur;

    /**
     * Campo cdNumIden
     */
    private String cdNumIden;

    /**
     * Campo dsNombre
     */
    private String dsNombre;

    /**
     * Campo dsApellido1
     */
    private String dsApellido1;

    /**
     * Campo dsApellido2
     */
    private String dsApellido2;

    /**
     * Campo fcFirma
     */
    private Date fcFirma;

    /**
     * Campo idEstado
     */
    private String idEstado;

    /**
     * Campo dsEstado
     */
    private String dsEstado;

    /**
     * Campo idEstadoRevEva
     */
    private String idEstadoRevEva;

    /**
     * Campo dsEstadoRevEva
     */
    private String dsEstadoRevEva;

    /**
     * Campo nmAno
     */
    private Integer nmAno;
}
