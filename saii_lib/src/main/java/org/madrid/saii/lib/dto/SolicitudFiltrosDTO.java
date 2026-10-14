package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SolicitudFiltrosDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public SolicitudFiltrosDTO() {
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
     * Campo strCdDocuExp
     */
    private String strCdDocuExp;

    /**
     * Campo strCdDocuSol
     */
    private String strCdDocuSol;

    /**
     * Campo acronimo
     */
    private String acronimo;

    /**
     * Campo descripcion
     */
    private String descripcion;

    /**
     * Campo idEstado
     */
    private Integer idEstado;

    /**
     * Campo numeroExpediente
     */
    private String numeroExpediente;

    /**
     * Campo idTipoInteresado
     */
    private Integer idTipoInteresado;

    /**
     * Campo dsNifInteresado
     */
    private String dsNifInteresado;

    /**
     * Campo dsNombreInteresado
     */
    private String dsNombreInteresado;

    /**
     * Campo dsApellido1Interesado
     */
    private String dsApellido1Interesado;

    /**
     * Campo dsApellido2Interesado
     */
    private String dsApellido2Interesado;

    /**
     * Campo strTipoPersona
     */
    private String strTipoPersona;
    
    /**
     * Indica si la identificacion es representante legal
     */
    private Boolean isRepresentanteLegal;
    
    /**
     * Indica el NIF/CIF del representante legal
     */
    private String nifEntidadRepresentanteLegal;

}
