package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Expediente Filtro
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class RepresentanteJurFiltrosDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin parametros
     */
    public RepresentanteJurFiltrosDTO() {
        super();
        this.estadoDTO = new CatalogosValoresDTO();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

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
     * Campo dsEmail
     */
    private String dsEmail;

    /**
     * Campo dsTelefono
     */
    private String dsTelefono;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo dsEntidad
     */
    private Date fcVigenciaIni;

    /**
     * Campo dsEntidad
     */
    private Date fcVigenciaFin;

    /**
     * Campo cdNumIdenEntidad
     */
    private String dsCargo;

    /**
     * Campo cdNumIdenEntidad
     */
    private String cdNumIdenEntidad;

}
