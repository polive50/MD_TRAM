package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class InteresadoDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public InteresadoDTO() {
        super();
        this.tipoIdentificacionDTO = new CatalogosValoresDTO();
        this.tipoActorDTO = new CatalogosValoresDTO();
        this.listaPersonaAreaDTO = new ArrayList<>();
        this.listaPersonaEspecialidadDTO = new ArrayList<>();
        this.listaPersonaOrganismoDTO = new ArrayList<>();
        this.listaPersonaTitulacionDTO = new ArrayList<>();
        this.listaPersonaAreaDTO = new ArrayList<>();
        this.listaPersonaEspecialidadDTO = new ArrayList<>();
        this.listaPersonaOrganismoDTO = new ArrayList<>();
        this.listaPersonaTitulacionDTO = new ArrayList<>();

    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idInteresado
     */
    private Integer idInteresado;

    /**
     * Campo idTIpoIdentificacion
     */
    private CatalogosValoresDTO tipoIdentificacionDTO;

    /**
     * Campo idTipoActor
     */
    private CatalogosValoresDTO tipoActorDTO;

    /**
     * Campo cdNumIden
     */
    private String cdNumIden;

    /**
     * Campo dsApellido1
     */
    private String dsApellido1;

    /**
     * Campo dsApellido2
     */
    private String dsApellido2;

    /**
     * Campo dsNombre
     */
    private String dsNombre;

    /**
     * Campo fcNacimiento
     */
    private Date fcNacimiento;

    /**
     * Campo cdSexo
     */
    private String cdSexo;

    /**
     * Campo dsEmail
     */
    private String dsEmail;

    /**
     * Campo dsMovil
     */
    private String dsMovil;

    /**
     * Campo dsTelefono
     */
    private String dsTelefono;

    /**
     * Campo cdProvincia
     */
    private String cdProvincia;

    /**
     * Campo cdLocalidad
     */
    private String cdLocalidad;

    /**
     * Campo cdTipoVia
     */
    private String cdTipoVia;

    /**
     * Campo dsDireccion
     */
    private String dsDireccion;

    /**
     * Campo cdCodigoPostal
     */
    private String cdCodigoPostal;

    /**
     * Campo dsNumero
     */
    private String dsNumero;

    /**
     * Campo dsPiso
     */
    private String dsPiso;

    /**
     * Campo dsPuerta
     */
    private String dsPuerta;

    /**
     * Campo dsEscalera
     */
    private String dsEscalera;

    /**
     * Campo auxiliar Nombre/Razon Social y apellidos
     */
    private String dsInteresado;

    /**
     * Campo itCoorEval
     */
    private String itCoorEval;

    /**
     * Campo dsDepartamento
     */
    private String dsDepartamento;

    /**
     * Campo nmSexenioInve
     */
    private Integer nmSexenioInve;

    /**
     * Campo nmSexenioTran
     */
    private Integer nmSexenioTran;

    /**
     * Listado de Areas Cientificas que pertenece una persona
     */
    private List<PersonaAreaDTO> listaPersonaAreaDTO;

    /**
     * Listado de especialidades de una persona
     */
    private List<PersonaEspecDTO> listaPersonaEspecialidadDTO;

    /**
     * Listado de Organismos que pertenece una persona
     */
    private List<PersonaOrgDTO> listaPersonaOrganismoDTO;

    /**
     * Listado de Titulaciones que pertenece una persona
     */
    private List<PersonaTitDTO> listaPersonaTitulacionDTO;

    /**
     * Campo de cdUsuario
     */
    private String cdUsuario;

    /**
     * Representa la lista de areas cientificas asociadas a la personas
     */
    @JsonIgnore
    private String areasCientificas;

    /**
     * Representa la variable de filtro por area cientifica
     */
    @JsonIgnore
    private Integer idAreaCientifica;

    @JsonIgnore
    private String dsLocalidad;
    
    @JsonIgnore
    private String dsProvincia;

    
    /**
     * Representa el campo dsNacionalidad 
     */
    private String dsNacionalidad;
    
    /**
     * Representa el campo dsPaisResidencia 
     */
    private String dsPaisResidencia;
}
