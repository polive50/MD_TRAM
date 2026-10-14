package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.util.Constantes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Bloque
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class BloqueDTO extends AuditoriaDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    public BloqueDTO() {
        super();
        this.idTematica = new CatalogosValoresDTO();
        this.idFormato = new CatalogosValoresDTO();
        this.actor = new ConvocatoriaActDTO();
        this.lstBloqueComponenteDTO = new ArrayList<>();
        this.lstBloqueComponentesFilaDTO = new ArrayList<>();
        this.listaBloqueAuxDTO = new ArrayList<>();
        this.esTipoTexto = Boolean.FALSE;
        this.esTipoTextoFijo = Boolean.FALSE;
        this.esTipoListado = Boolean.FALSE;
        this.esTipoFormulario = Boolean.FALSE;
        this.esTipoFormularioContrato = Boolean.FALSE;
        this.esTipoTabla = Boolean.FALSE;
        this.esTipoDocumento = Boolean.FALSE;
        this.esTipoParticipantes = Boolean.FALSE;
        this.esTipoAyudaSolicitada = Boolean.FALSE;
        this.esTipoPresupuestoPresentado = Boolean.FALSE;
        this.esTipoActuaciones = Boolean.FALSE;
        this.esTipoPlazosEjecAnual = Boolean.FALSE;
        this.esTipoPersonalConCargo = Boolean.FALSE;
        this.esTipoPersonalSinCargo = Boolean.FALSE;
        this.esTipoPresupuestoFinanciable = Boolean.FALSE;
        this.esTipoNotificacion = Boolean.FALSE;
        this.esTipoPersonaSolicitante = Boolean.FALSE;
        this.nmMaxPalabras = 0;
        this.habilitarSeccionAgregarComponentes = Boolean.FALSE;
        this.habilitarSeccionAgregarDocumentos = Boolean.FALSE;
        this.esTipoFormularioContratoRenuncia = Boolean.FALSE;
        this.esTipoFormularioContratoSusti = Boolean.FALSE;
        this.listaBloqueAyudaSolicitadaDTO = new ArrayList<>();
        this.esTipoOtrasModificaciones = Boolean.FALSE;
        this.esTipoObservaciones = Boolean.FALSE;
        this.esTipoMultiregistro = Boolean.FALSE;
        this.itTipoSelDoc = Constantes.TipoSeleccionDocumento.C;
        this.esCuentaBancaria=Boolean.FALSE;
        this.esTipoDiscapacidad = Boolean.FALSE;
        this.esTipoIntensidadAyuda = Boolean.FALSE;
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idBloque
     */
    private Integer idBloque;

    /**
     * Campo dsTematica
     */
    private CatalogosValoresDTO idTematica;
    
    /**
     * Campo idTematicaPadre
     */
    @JsonIgnore
    private CatalogosValoresDTO idTematicaPadre;

    /**
     * Campo dsFormato
     */
    private CatalogosValoresDTO idFormato;

    /**
     * Campo Actor
     */
    private ConvocatoriaActDTO actor;

    /**
     * Campo nmFilas
     */
    private Integer nmFilas;

    /**
     * Campo nmOrden
     */
    private Integer nmOrden;
    
    /**
     * Campo nmOrden
     */
    private Integer subNmOrden;

    /**
     * Campo tlAuxiliar
     */
    private String tlAuxiliar;

    /**
     * Campo tlAuxiliarInf
     */
    private String tlAuxiliarInf;

    /**
     * Campo itAutorizaConsulta
     */
    private Boolean itAutorizaConsulta;

    /**
     * Campo idImpreso
     */
    private Integer idImpreso;

    /**
     * List BloqueComponenteDTO
     */
   
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueComponenteDTO> lstBloqueComponenteDTO;

    /**
     * Boolean tipo texto
     */
    
    private Boolean esTipoTexto;

    /**
     * Boolean tipo textoFijo
     */
    
    private Boolean esTipoTextoFijo;

    /**
     * Boolean tipoListado
     */
    
    private Boolean esTipoListado;

    /**
     * Boolean tipoFormulario
     */
    
    private Boolean esTipoFormulario;
    
    /**
     * Boolean tipoFormulario contrato inicial
     */
    
    private Boolean esTipoFormularioContrato;
    

    /**
     * Boolean tipoTabla
     */
    
    private Boolean esTipoTabla;

    /**
     * Boolean tipoDocumento
     */
    
    private Boolean esTipoDocumento;

    /**
     * Boolean tipoParticipantes
     */
    
    private Boolean esTipoParticipantes;

    /**
     * Boolean tipoPresupuestoPresentado
     */
   
    private Boolean esTipoPresupuestoPresentado;

    /**
     * Boolean tipoAyudaSolicitada
     */
    
    private Boolean esTipoAyudaSolicitada;

    /**
     * Boolean esTipoActuaciones
     */
    
    private Boolean esTipoActuaciones;
    
    /**
     * Boolean esTipoNotificacion
     */
    
    private Boolean esTipoNotificacion;
    
    
    /**
     * Boolean esTipoPersonaSolictante
     */
    
    private Boolean esTipoPersonaSolicitante;


    /**
     *
     */
   
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueComponenteDTO> lstBloqueComponentesFilaDTO;

    /**
     * Boolean para indicar si el bloque contiene algún componente relacionable
     */
    
    private Boolean tieneCamposRelacionables;

    /**
     * Integer nmMaxPalabras
     */
    private Integer nmMaxPalabras;

    /**
     *
     */
    @JsonIgnore
    private Boolean habilitarSeccionAgregarComponentes;

    /**
     *
     */
    @JsonIgnore
    private Boolean habilitarSeccionAgregarDocumentos;

    /**
     *
    */
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueComponenteDTO> listaBloqueAuxDTO;

    /**
    *
    */
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueDTO> lstBloque;
    
    /**
     * Boolean tipoPlazosEjecAnual
     */
    
    private Boolean esTipoPlazosEjecAnual;
    
    /**
     * Boolean tipoPersonalConCargo
     */
   
    private Boolean esTipoPersonalConCargo;
    
    /**
     * Boolean tipoPersonalSinCargo
     */
   
    private Boolean esTipoPersonalSinCargo;
    
    /**
     * Boolean tipoPresupuestoFinanciable
     */
    
    private Boolean esTipoPresupuestoFinanciable;
    
    /**
     * Boolean tipoFormulario contrato renuncia
     */
    
    private Boolean esTipoFormularioContratoRenuncia;
    
    /**
     * Boolean tipoFormulario contrato sustitucion
     */
   
    private Boolean esTipoFormularioContratoSusti;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BloqueComponenteDTO> listaBloqueAyudaSolicitadaDTO;

    /**
     * Boolean esTipoOtrasModificaciones
     */
    private Boolean esTipoOtrasModificaciones;
    
    /**
     * Boolean esTipoObservaciones
     */
    private Boolean esTipoObservaciones;
    
    /**
     * String itMultiBloque
     */
    private String itMultiBloque;
 
    /**
     * Campo idBloquePadre
     */
    private Integer idBloquePadre;
    
    private String itMultiRegistro;
    
    private List<BloqueDTO> lstBloquesHijoDTO;
        
    /**
     * Boolean esTipoMultiregistro
     */
    
    private Boolean esTipoMultiregistro;
    
    private Boolean esTipoFormularioActaGestionOrdinaria;
    
    /**
     * Campo itTipoSelDoc
     */
    private String itTipoSelDoc;
    
    
    private Boolean esCuentaBancaria;

    private Boolean esTipoDiscapacidad;
    
    /**
     * Campo Descripción más información
     */
    private String tlInformacion;
    
    /**
     * Camnpo de intensidad de la ayuda
     */
    private Boolean esTipoIntensidadAyuda;

}
