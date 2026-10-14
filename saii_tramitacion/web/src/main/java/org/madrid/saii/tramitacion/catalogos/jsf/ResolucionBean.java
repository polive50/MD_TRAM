package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.services.CataService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaActService;
import org.madrid.saii.lib.services.ConvocatoriaDocService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioDocumentoPTFRService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteEvalService;
import org.madrid.saii.lib.services.ExpedienteHistService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.ImpresoService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.SolicitudService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.primefaces.PrimeFaces;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nttdada
 *
 */

@Slf4j
@Getter
@Setter
@Component("resolucionBean")
@Scope("request")
public class ResolucionBean {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * Identificador de panel de formulario resolucion a refrescar
     */
    private String formularioResolucion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelResolucionSolapa";

    /**
     * Identificador de panel de estado de resolucion
     */
    private String panelEstadoResolucion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelDsNombreEstadoResolucion";

    /**
     * Campo de estado del expediente.
     */
    private String campoEstadoExpediente = "convocatoriaForm:contenedorSolapas:dsEstadoExpedienteSel";

    /**
     * Identificador de solapa de resolucion
     */
    private String solapaResolucion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";

    /**
     * Atributo que sirve de indicador para mostrar info si no ha subido el
     * documento de propuesta de resolucion provisionals
     */
    private Boolean existeDocPropuestaResProv = Boolean.TRUE;
    
    private Integer idConvocatoria;

    private Boolean alegacionRevisada;
    
    private Boolean mostrarMensajeElegirFichero = Boolean.FALSE;
    
    
    private EnvioDocumentoPTFR tramiteDocProvisional;
    
    private EnvioDocumentoPTFR tramiteDocDefinitivo;
    
    private EnvioDocumentoPTFR tramiteDocDefinitivoEnCola;
   
    
    private Integer valorConcedidoProv;
    private Integer valorDenegadoProv;
    private Integer valorConcedidoDef;
    private Integer valorDenegadoDef;
    
    private Integer valorDesistidoProv;
    private Integer valorExcluidoProv;
    
    private Integer valorDesistidoDef;
    private Integer valorExcluidoDef;
    
    
    private Boolean activarConcederDenegar = Boolean.TRUE;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio de convocatoriaDocService
     */
    @Autowired
    @Qualifier("convocatoriaDocService")
    private ConvocatoriaDocService convocatoriaDocService;

    /**
     * Servicio de expedienteEvalService
     */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /** Servicio de convocatoriaService. */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     * Servicio de expedienteHistService
     */
    @Autowired
    @Qualifier("expedienteHistService")
    private ExpedienteHistService expedienteHistService;

    /**
     * Servicio de catalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Servicio de documentoPtfrService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    /**
     * Servicio de documentoPtfrService
     */
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;
    
    /**
     * Servicio cataService
     */
    @Autowired
    @Qualifier("cataService")
    private CataService cataService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;
    
    /** Servicio de Impreso. */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;
    
    /** Servicio de convocatoria Act. */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;
    
    
    @Autowired
    @Qualifier("envioDocumentoPtfrService")
    private EnvioDocumentoPTFRService envioDocumentoPtfrService;
    
    
    /**
     * Servicio de Pdf
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;
    
    
    @Autowired
    @Qualifier("expedienteHistDAO")
    @Setter
    private ExpedienteHistDAO expedienteHistDAO;
    
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////

    /**
     * ExpedienteBean
     */
    private ExpedienteBean expedienteBean;
    
    /**
     * financiacionBean
     */
    @Autowired
    @Qualifier("financiacionBean")
    private FinanciacionBean financiacionBean; 
    

    /**
     * convocatoriaDTO
     */
    private ConvocatoriaDTO convocatoriaDTO;

    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;

    /**
     * archivoResolucionProvisional
     */
    private UploadedFile archivoResolucion;

    /**
     * archivoProvisionalDefinitiva
     */
    private UploadedFile archivoResolucionDefinitiva;

    /*
     * expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    /*
     * expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO700;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ResolucionBean() {
        super();

    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {

        this.inicializarDocumento();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PUBLIC ACTIONS
    // ////////////////////////////////////////////////////////


    /**
     * Metodo que inicializa el documento. Pone el ConvocatoriaDocDTO,nombre de
     * los archivos y el tipo de Documento como nuevo.
     */
    public void inicializarDocumento() {
        this.expedienteDocDTO = new ExpedienteDocDTO();
        this.expedienteDocDTO700 = new ExpedienteDocDTO();
        this.tramiteDocProvisional = new EnvioDocumentoPTFR();
        this.tramiteDocDefinitivo  = new EnvioDocumentoPTFR();
	this.tramiteDocDefinitivoEnCola  = new EnvioDocumentoPTFR();

        this.archivoResolucion = null;
        this.archivoResolucionDefinitiva = null;
        
        this.valorConcedidoProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL);
        this.valorDenegadoProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL);       
        this.valorDesistidoProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL);
        this.valorExcluidoProv  =  utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL);
        
        
        this.valorConcedidoDef = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA);
        this.valorDenegadoDef  = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA);
        this.valorDesistidoDef = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO);
        this.valorExcluidoDef  =  utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION);

    }

    /**
     * Metodo para aprobar provisional al expediente
     */
    public void aprobarProvisional() {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), null);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_PROVISIONAL_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_PROVISIONAL_KO);
        }

    }

    /**
     * Metodo para denegar provisional al expediente
     */
    public void denegarProvisional() {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), null);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGAR_PROVISIONAL_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGAR_PROVISIONAL_KO);
        }

    }

    /**
     * Método para aprobar definitivo para convocatoria conjunta cambiando el estado del expediente a
     * concedido definitivo
     */
    public void aprobarDefinitivo() {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO), null);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_DEFINITIVO_KO);
        }

    }

    /**
     * Método denegar definitivo, cambia el estado del expediente a denegado
     * definitivo
     */
    public void denegarDefinitivo() {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO), null);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGADO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGADO_DEFINITIVO_KO);
        }

    }

    /**
     * Método que controla la descargar de fichero tipo alegación
     *
     * @param imprDTO Datos de impreso
     * @param doc Datos de impreso
     */
    public void descargarDocumentoAlegacion() {

        try {

            if (Objects.nonNull(this.expedienteBean.getExpedienteDTO().getCdFicaValoracionAleg())) {
                FicaFichero ficaFichero = this.ficaService.recuperar(
                        this.expedienteBean.getExpedienteDTO().getCdFicaValoracionAleg());

                if (ficaFichero != null) {
                    UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                            FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }
            }

        } catch (ServiceException ex) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }

    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar todas las acciones de esta solapa
     * (aprobar/denegar/conceder/excluir/desistir/revertir provisional y definitivo, subir/enviar
     * resolución, alegaciones...), dejando habilitadas las de solo lectura (descargar).
     *
     * @return true si el expediente está Rechazado Definitivo
     */
    public boolean isExpedienteRechazadoDefinitivo() {
        return this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }

    /**
     * Metodo para habilitar los botones de aprobar y denegar provisional
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException
     */
    public Boolean habilitarAprobarProvisional(Boolean esModoConsulta) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite aprobar/denegar provisional
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean expedienteHijosResolucionados = Boolean.TRUE;
        Boolean evaluacionTerminada = Boolean.TRUE;

        // obtenemos convocatorias doc
        List<ConvocatoriaDocDTO> lstConvocatoriaDoc = this.convocatoriaDocService
                .buscarPorIdConvocatoria(this.expedienteBean.getIdConvocatoria());

        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
        this.comprobarGestoresExcep(conv);
        
        Integer idProResPro = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);
        this.existeDocPropuestaResProv = lstConvocatoriaDoc.stream()
                .anyMatch(p -> idProResPro.equals(p.getIdTipoDocumento().getIdValor()));
        
        //Constantes.EstadoExpediente.EVALUADO_OK
        // Integer valorEva = this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor();
         
        if ("S".equals(conv.getItEvaluacion()) && 
                !(this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO)) || 
                  this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)))) {
                evaluacionTerminada = Boolean.FALSE;
            }
        
        //Si es concesion participante hay que comprobar primero el estado de los expedientes hijos si estamos en el expediente solicitante
        if (conv.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) &&
                this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getTipoActorDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {

            List<ExpedienteDTO> listaExpedientesHijos = this.expedienteService.buscarExpedientePorExpedientePadre( this.expedienteBean.getExpedienteDTO().getIdSolicitud(),  this.expedienteBean.getExpedienteDTO().getIdExpediente());

            // Validamos si existen expedientes hijos no definitivos
            expedienteHijosResolucionados = listaExpedientesHijos != null && listaExpedientesHijos.stream().allMatch(expediente -> Boolean.TRUE.equals(expediente.getBlEstadoDefinitivo()));

            if (Boolean.FALSE.equals(expedienteHijosResolucionados) || Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getBlHabilitarResolucionProvisional())
                    || Boolean.FALSE.equals(this.existeDocPropuestaResProv)
                    || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                    || conv.isEsGestorJCT()
                    || conv.isEsGestorConcesion()
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.FALSE;
            }

        } else {
            if (Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getBlHabilitarResolucionProvisional())
                    || Boolean.FALSE.equals(evaluacionTerminada)
                    || Boolean.FALSE.equals(this.existeDocPropuestaResProv)
                    || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                    || conv.isEsGestorJCT()
                    || conv.isEsGestorConcesion()
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.FALSE;
            }
        }
        
        return habilitar;
    }
    
    /**
     * Metodo para habilitar los botones de aprobar y denegar provisional
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException
     */
    public Boolean habilitarDenegarProvisional(Boolean esModoConsulta) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite denegar provisional
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean evaluacionTerminada = Boolean.TRUE;
        Boolean expedienteHijosResolucionados = Boolean.TRUE;

        // obtenemos convocatoria docs
        List<ConvocatoriaDocDTO> lstConvocatoriaDoc = this.convocatoriaDocService
                .buscarPorIdConvocatoria(this.expedienteBean.getIdConvocatoria());

        ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
        this.comprobarGestoresExcep(conv);
        
        Integer idProResPro = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);
        this.existeDocPropuestaResProv = lstConvocatoriaDoc.stream().anyMatch(p -> idProResPro.equals(p.getIdTipoDocumento().getIdValor()));

        
        //Constantes.EstadoExpediente.EVALUADO_KO
        //Integer valorEva = this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor();
        if ("S".equals(conv.getItEvaluacion()) && 
                !(this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO)) || 
                  this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)))) {
                evaluacionTerminada = Boolean.FALSE;
            }
        
        //Si es concesion participante hay que comprobar primero el estado de los expedientes hijos si estamos en el expediente solicitante
        if (conv.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) &&
                this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getTipoActorDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {

            List<ExpedienteDTO> listaExpedientesHijos = this.expedienteService.buscarExpedientePorExpedientePadre( this.expedienteBean.getExpedienteDTO().getIdSolicitud(),  this.expedienteBean.getExpedienteDTO().getIdExpediente());


            // Validamos si existen expedientes hijos no definitivos
            expedienteHijosResolucionados = listaExpedientesHijos != null && listaExpedientesHijos.stream().allMatch(expediente -> Boolean.TRUE.equals(expediente.getBlEstadoDefinitivo()));
            
            if (Boolean.FALSE.equals(expedienteHijosResolucionados) || Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getBlHabilitarResolucionProvisional())
                    || Boolean.FALSE.equals(this.existeDocPropuestaResProv)
                    || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                    || conv.isEsGestorJCT()
                    || conv.isEsGestorConcesion()
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.FALSE;
            }
            
        }else {
            if (Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getBlHabilitarResolucionProvisional())
                    || Boolean.FALSE.equals(this.existeDocPropuestaResProv)
                    || Boolean.FALSE.equals(evaluacionTerminada)
                    || !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL).equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                    || conv.isEsGestorJCT()
                    || conv.isEsGestorConcesion()
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.FALSE;
            } 
        }
        
        
        

        return habilitar;
    }

    /**
     * 
     * Método que habilita el botón de subir la resolución provisional
     * 
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public Boolean habilitarSubirResolucionProvisional(Boolean esModoConsulta) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite subir resolución provisional
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }


        this.cargarDocumentosPTFR();
        
        Boolean habilitar = Boolean.TRUE;
        
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return  Boolean.FALSE;
        }else {
           
            
            Boolean existeEstadoProvisional = Boolean.FALSE;
            Boolean expedienteHijosResolucionados = Boolean.TRUE;
            Boolean evaluacionTerminada = Boolean.TRUE;
            List<Integer> lstEstado = new ArrayList<>();
            lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
            lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
            
            
//          lstEstado.add(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION);
//          lstEstado.add(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);
            if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null
                    && lstEstado.contains(this.expedienteBean.getExpedienteDTO()
                            .getEstadoProvisionalDTO().getIdValor())) {
                existeEstadoProvisional = Boolean.TRUE;
            }
            
            //VMM 28/07/25 MD-1790: para poder conceder provisional se debe validar tambien la financiacion
            Boolean existeRevisiones = Boolean.FALSE;
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK).equals(this.expedienteBean
                            .getExpedienteDTO().getEstadoRevAdminDTO().getIdValor())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK).equals(this.expedienteBean
                            .getExpedienteDTO().getEstadoRevRequiDTO().getIdValor())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK).equals(this.expedienteBean
                            .getExpedienteDTO().getEstadoRevEvalDTO().getIdValor())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK).equals(this.expedienteBean
                            .getExpedienteDTO().getEstadoRevFinanDTO().getIdValor())) {
                existeRevisiones = Boolean.TRUE;
            }
            
                  
            ConvocatoriaDTO conv = this.convocatoriaService.buscarId(this.idConvocatoria);
            if ("S".equals(conv.getItEvaluacion()) && 
                    !(this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO)) || 
                      this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)))) {
                    evaluacionTerminada = Boolean.FALSE;
                }
            
            if (conv.getIdTipoConcesion().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) &&
                    this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getTipoActorDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {

                List<ExpedienteDTO> listaExpedientesHijos = this.expedienteService.buscarExpedientePorExpedientePadre( this.expedienteBean.getExpedienteDTO().getIdSolicitud(),  this.expedienteBean.getExpedienteDTO().getIdExpediente());

                // Validamos si existen expedientes hijos no definitivos
                expedienteHijosResolucionados = listaExpedientesHijos != null && listaExpedientesHijos.stream().allMatch(expediente -> Boolean.TRUE.equals(expediente.getBlEstadoDefinitivo()));

                if (Boolean.FALSE.equals(expedienteHijosResolucionados) || Boolean.TRUE.equals(esModoConsulta)
                        || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                        || Boolean.TRUE.equals(existeEstadoProvisional)
                        || Boolean.FALSE.equals(existeRevisiones)
                        || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                        ) {
                    habilitar = Boolean.FALSE;
                }
                
            }else {
                if (Boolean.TRUE.equals(esModoConsulta)
                        || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                        || Boolean.TRUE.equals(existeEstadoProvisional)
                        || Boolean.FALSE.equals(evaluacionTerminada)
                        || Boolean.FALSE.equals(existeRevisiones)
                        || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                        ) {
                    habilitar = Boolean.FALSE;
                }
            }
            
        }
        
        
 
        return habilitar;
    }

 
    /**
     * 
     * @return
     */
    public Boolean habilitarBotonDescargarProvisional() {

        Boolean habilitar = Boolean.FALSE;
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        
        this.cargarDocumentosPTFR();
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return  Boolean.TRUE;
        }

        ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL), 
                this.expedienteBean.getExpedienteDTO().getIdExpediente());
        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            habilitar = Boolean.TRUE;
        }
        
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext)
            habilitar = Boolean.FALSE;
        }
        return habilitar;

    }
    
    
    public Boolean getMostrarBotonConcederDenegarProvisional() {

        Boolean habilitar = Boolean.TRUE;
        
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));

        if (!lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            return  Boolean.FALSE;
        }
        
        return habilitar;

    }

    /**
     * Metodo para habilitar los botones de conceder y denegar provisional
     *
     * @param esModoConsulta
     * @return
     */
    public Boolean habilitarConcederProvisional(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite conceder/denegar provisional individual
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

//        if (this.convocatoriaDTO == null || this.convocatoriaDTO.getTipoResolModalidadDTO() != null
//                || convocatoriaDTO.getTipoResolModalidadDTO().getCdTipoResolucion() != null) {
//
//            this.convocatoriaDTO = convocatoriaService
//                    .buscarId(this.expedienteBean.getIdConvocatoria());
//
//        }
        
        
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return  Boolean.FALSE;
        }else {
            List<Integer> lstEstado = new ArrayList<>();
            lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
            lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));

//            lstEstado.add(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION);
//            lstEstado.add(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);

            Boolean habilitarPropuestaResolucionProvisional = Boolean.FALSE;
            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                habilitarPropuestaResolucionProvisional = Boolean.TRUE;
            }

            if (Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(habilitarPropuestaResolucionProvisional)
                    || lstEstado.contains(this.expedienteBean.getExpedienteDTO()
                            .getEstadoProvisionalDTO().getIdValor())
//                    || !Constantes.EstadoExpediente.REVISION_OK.equals(
//                            this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO().getIdValor())
//                    || !Constantes.EstadoExpediente.REVISION_OK.equals(
//                            this.expedienteBean.getExpedienteDTO().getEstadoRevRequiDTO().getIdValor())
//                    || !Constantes.EstadoExpediente.EVALUADO_OK.equals(this.expedienteBean
//                            .getExpedienteDTO().getEstadoRevEvalDTO().getIdValor())
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())//MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            ) {

                habilitar = Boolean.FALSE;

            }
        }
        
        return habilitar;
    }

    /**
     * Métodod conceder provisionals
     */
    public void concederProvisional() {
        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), null);

            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            expeDocDTO.setTramiteDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL)));
            this.expedienteDocService.guardar(expeDocDTO);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_PROVISIONAL_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_PROVISIONAL_KO);
        }
    }

    public void denegarProvisionalIndividual() {
        try {

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
                    this.expedienteBean.getIdExpediente(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), null);

            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            expeDocDTO.setTramiteDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL)));
            this.expedienteDocService.guardar(expeDocDTO);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGAR_PROVISIONAL_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGAR_PROVISIONAL_KO);
        }
    }

    /**
     * Metodo para habilitar el botoón Revertir aprobado porvisional
     * 
     * @Param esModoCOnsulta
     * @return
     */
    public Boolean habilitaRevertirResolucionProvisional(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite revertir resolución provisional
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)); 
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return  Boolean.FALSE;
        }else {
            
            Boolean existeProvisional = false;
            List<Integer> lstEstados = new ArrayList<>();      
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));    
            
            if (lstEstados.contains(
                    this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
                existeProvisional = true;
            }

            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.RESOLUCION_NOTIFICADO));

            Boolean existeResolucion = false;
            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            
            if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null
                    && expeDocDTO.getEstadoDTO().getIdValor() != null
                    && (lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor()))) {
                existeResolucion = true;
            }

            if (Boolean.TRUE.equals(esModoConsulta)
                    || Boolean.TRUE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                    || Boolean.FALSE.equals(existeProvisional)
                    || Boolean.TRUE.equals(existeResolucion)
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext

                habilitar = Boolean.FALSE;

            }
                  
            
        }
        return habilitar;

    }


    /**
     * Metodo para revertir el aprobado provisional al expediente
     */
    public void revertirAprobadoProvisional() {

        try {

            // Obtenemos el ultimo registro historico
            List<AtlasFilter> filtroHist = new ArrayList<>();
            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor(),
                    Operador.NOT_EQUALS));
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_HIST, false));
            List<ExpedienteHistDTO> lstHistorico = this.expedienteHistService.buscar(0, -1,
                    filtroHist, ordenamientoHist);

            if (lstHistorico != null && !lstHistorico.isEmpty()) {
                ExpedienteHistDTO registroHistoricoEstadoAnterior = lstHistorico.get(0);
                
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_CONCEDIDO_PROVISIONAL_OK);
                } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_DENEGADO_PROVISIONAL_OK);
                }
                
                ExpedienteDTO expedienteDTO = this.expedienteService.revertirEstadosExpediente(
                        this.expedienteBean.getIdExpediente(), registroHistoricoEstadoAnterior, Boolean.TRUE);
                this.expedienteBean.setExpedienteDTO(expedienteDTO);
                
                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                expeDocDTO.setTramiteDTO(null);
                this.expedienteDocService.guardar(expeDocDTO);
                
//                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
//                    this.ficaService.borrar(expeDocDTO.getCdFica());
//                }
//                if (expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null) {
//                    expeDocDTO.setCdUsuBaja(UserUtils.getUserName().toUpperCase());
//                    expeDocDTO.setFcBajaFila(new Date());
//                    this.expedienteDocService.guardar(expeDocDTO);
//                }
                this.recuperarValoresVariablesExpediente(expedienteDTO);
                
            }

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_PROVISIONAL_KO);
        }

    }

    /**
     * 
     */
    public void subirResolucionProvisionalDialog() {
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionProvisional");
    }

    /** 
     * 
     */
    public void cerrarSubirResolucionProvisionalDialog() {
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionProvisional");
    }

    /**
     * 
     */
    public void cargarFicheroProvisional() {

        try {
            Integer tramite = null;
//            if (Constantes.EstadoExpediente.APROBADO_PROVISIONAL
//                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
//                tramite = Constantes.Tramite.CONCESION_PROVISIONAL;
//            } else if (Constantes.EstadoExpediente.DENEGADO_PROVISIONAL
//                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
//                tramite = Constantes.Tramite.DENEGACION_PROVISIONAL;
//            } else {
//                AtlasMessageUtils.addErrorMessageFromBundle(
//                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
//                log.error("Error de estado de expediente : "
//                        + this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
//            }
            
            if (this.archivoResolucion == null  ) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_VACIO);
            } else {
            
                if (this.archivoResolucion.getFileName() != null) {
                    this.prepararMetadatosFica(this.archivoResolucion,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL), tramite);
                    this.expedienteDocDTO = this.expedienteDocService
                            .subirDocumentoFica(this.expedienteDocDTO);
                    this.guardarDocumento();
    
                    this.recuperarValoresVariablesExpediente(this.expedienteBean.getExpedienteDTO());
                    this.mostrarMensajeElegirFichero = Boolean.FALSE;
                } else {
                    this.mostrarMensajeElegirFichero = Boolean.TRUE;
                    PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionProvisional");
                }
            }
        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage());
        }
    }

    /**
     * 
     * @param archivoResolucion
     * @param propuestaResolucion
     * @param tramite
     * @return
     */
    public ExpedienteDocDTO prepararMetadatosFica(UploadedFile archivoResolucion,
            Integer propuestaResolucion, Integer tramite) {

        if (Objects.nonNull(archivoResolucion)) {

            try {
                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(propuestaResolucion, 
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                InputStream fileInputStream = archivoResolucion.getInputstream();
                String nombreArchivo = archivoResolucion.getFileName();
                this.expedienteDocDTO.setIdExpedienteDoc(
                        expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null
                                ? expeDocDTO.getIdExpedienteDoc()
                                : null);
                this.expedienteDocDTO.setExpedienteDTO(this.expedienteBean.getExpedienteDTO());
                this.expedienteDocDTO.setEstadoDTO(this.catalogosValoresService
                        .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                this.expedienteDocDTO.setTipoDocumentoDTO(
                        this.catalogosValoresService.buscarId(propuestaResolucion));
                this.expedienteDocDTO.setFicaMetadatos(new FicaMetadatos());
                this.expedienteDocDTO.setCdFica(expeDocDTO != null && expeDocDTO.getCdFica() != null
                        ? expeDocDTO.getCdFica()
                        : null);
                this.expedienteDocDTO.getFicaMetadatos().setNombreFichero(nombreArchivo);
                this.expedienteDocDTO.getFicaMetadatos().setFechaFichero(new Date());
                this.expedienteDocDTO.getFicaMetadatos().setExtension(nombreArchivo
                        .substring(nombreArchivo.indexOf('.') + 1, nombreArchivo.length()));
                this.expedienteDocDTO.getFicaMetadatos()
                        .setUsuario(UserUtils.getUserName().toUpperCase());
                this.expedienteDocDTO.getFicaMetadatos().setEncriptar(false);
                this.expedienteDocDTO.setContenidoFichero(IOUtils.toByteArray(fileInputStream));
                this.expedienteDocDTO
                        .setCdUsuAlta(expeDocDTO != null && expeDocDTO.getCdUsuAlta() != null
                                ? expeDocDTO.getCdUsuAlta()
                                : null);
                this.expedienteDocDTO
                        .setFcAltaFila(expeDocDTO != null && expeDocDTO.getFcAltaFila() != null
                                ? expeDocDTO.getFcAltaFila()
                                : null);
                this.expedienteDocDTO
                        .setCdUsuModif(expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null
                                ? UserUtils.getUserName().toUpperCase()
                                : null);
                this.expedienteDocDTO.setFcModifFila(
                        expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null ? new Date()
                                : null);
                this.expedienteDocDTO.setTramiteDTO(tramite == null ? new CatalogosValoresDTO()
                        : this.catalogosValoresService.buscarId(tramite));

            } catch (IOException e) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage());
            } catch (ServiceException e) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage());
            }

        } else {

            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.info(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);

        }

        return this.expedienteDocDTO;

    }

    /**
     * Metodo que se invoca tras aceptar el popup de guardado.
     */
    public void guardarDocumento() {

        try {

            this.expedienteDocService.guardar(this.expedienteDocDTO);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);

        } catch (ServiceException e) {

            if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(e.getKeyExcepcion())) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                log.error(e.getMessage());
            }

            this.expedienteDocDTO.setFicaMetadatos(new FicaMetadatos());

        }
    }

    /**
     * 
     */
    public void descargaFicheroProvisional() {

        try {

            ExpedienteDocDTO expeDocDTO ;
            
            if(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                    this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))) {
                
                expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_EXCLUIDA),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                
                
            }else if(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                    this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))) {
                
                expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL_DESISTIDA),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                  
            } else {
                expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
            }
            
          
            
            if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                        FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage());

        }

    }

    /**
     * 
     */
    public void enviarColaPortafirmasProvisional() {

        try {

            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = convocatoriaService
                        .buscarId(this.expedienteBean.getIdConvocatoria());
            }

            Integer tramite = 0;
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL);
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL);
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                log.error("Error de estado de expediente : "
                        + this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
            }

            if (Objects.nonNull(this.expedienteBean.getExpedienteDTO().getIdExpediente())) {
                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                    FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
                            this.convocatoriaDTO, this.expedienteBean.getExpedienteDTO(),
                            this.expedienteBean.getExpedienteDTO().getIdExpediente(), tramite,
                            ficaFichero.getDatosFichero(), null, null, null);
                    if (insertado.equals(Boolean.TRUE)) {
                        expeDocDTO.setEstadoDTO(this.catalogosValoresService
                                .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(expeDocDTO,
                                UserUtils.getUserName().toUpperCase());
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                    } else {
                        log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                    }
                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                }
            }
            
            this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.FALSE);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }

    }

    /**
     * Metodo para habilitar el boton de enviar a la cola de portafirmas
     *
     * @param esModoConsulta
     * @return
     */
    public Boolean habilitarEnviarColaPortafirmasProvisional(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite enviar a cola portafirmas provisional
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean tieneFica = Boolean.FALSE;
        Boolean tieneEstadosDoc = Boolean.FALSE;

        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)); 
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return Boolean.FALSE;
        }else {
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.RESOLUCION_NOTIFICADO));
//            lstEstadosDoc.add(Constantes.EstadoDocumento.REVISION_OK);

            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());

            if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                tieneFica = Boolean.TRUE;
            }

            if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null
                    && expeDocDTO.getEstadoDTO().getIdValor() != null) {
                tieneEstadosDoc = lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor());
            }

            Boolean tieneEstadoExpediente = Boolean.FALSE;
            List<Integer> lstEstadosExpediente = new ArrayList<>();
            lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
            lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
            if (lstEstadosExpediente
                    .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tieneEstadoExpediente = Boolean.TRUE;
            }

            if (Boolean.TRUE.equals(esModoConsulta) || Boolean.FALSE.equals(tieneFica)
                    || Boolean.TRUE.equals(tieneEstadosDoc)
                    || Boolean.FALSE.equals(tieneEstadoExpediente)
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                habilitar = Boolean.FALSE;
            }

           
            
        }
        
        //lamamos al metodo para tener los valores de los envios 
        this.cargarDocumentosPTFR();
        return habilitar;

    }
    
    /**
     * 
     * @param esModoConsulta
     * @return
     * @throws ServiceException
     */
    public Boolean habilitarBotonSubirResolucionDefinitiva(Boolean esModoConsulta)
            throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite subir resolución definitiva
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.FALSE;
        Date fcHoy = new Date();
        
        if (this.convocatoriaDTO == null) {
            this.convocatoriaDTO = convocatoriaService
                    .buscarId(this.expedienteBean.getIdConvocatoria());
        }
        
        Boolean revisada = Boolean.FALSE;
        List<Integer> tramites = new ArrayList<>();
        /// añadir nuevos tramites provisionels escluir y desistir
        if(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null &&  this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))) {
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
        } else if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null   && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        }else if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null  && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))) {
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
        }else if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null  && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))) {
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
        }
  
        List<AtlasFilter> filtroPtfr = new ArrayList<>();
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, tramites, Operador.IN));
        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<DocumentoPtfrDTO> lstDocumentoPtfr = this.documentoPtfrService.buscar(0, -1,
                filtroPtfr, null);
        if (lstDocumentoPtfr != null && !lstDocumentoPtfr.isEmpty()) {
            DocumentoPtfrDTO documentoPtfr = lstDocumentoPtfr.get(0);
            if (documentoPtfr != null) {
                if (this.convocatoriaDTO.getItAlegacion() != null && Constantes.AfirmacionNegacion.S
                        .equals(this.convocatoriaDTO.getItAlegacion())) {
                    
                    if (this.convocatoriaDTO.getItEvaluacion() != null
                            && Constantes.AfirmacionNegacion.S
                                    .equals(this.convocatoriaDTO.getItEvaluacion())) {
                        
                        if (this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() != null
                                && Constantes.AfirmacionNegacion.S.equals(this.expedienteBean
                                        .getExpedienteDTO().getItAlegacionRevisada())) {
                            revisada = Boolean.TRUE;
                        }
                        // MD-1472 JRFM - Si AlegacionRevisada es null o N, revisamos los tiempo
                        // en base a las fechas de note
                        else if(this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() == null
                                || Constantes.AfirmacionNegacion.N.equals(this.expedienteBean
                                        .getExpedienteDTO().getItAlegacionRevisada())) {
                            if (documentoPtfr.getFcLecturaNote() != null) {
                                Date fechaInicioAlegacion = documentoPtfr.getFcLecturaNote();
                                Date fechaFinAlegacion = this.cataService.calcularDiasHabiles(
                                        fechaInicioAlegacion, 11, Constantes.Suca.CD_MUNI_MADRID,
                                        this.expedienteBean.getExpedienteDTO().getInteresadoDTO()
                                                .getCdLocalidad());
                                if (fcHoy.after(fechaFinAlegacion)) {
                                    revisada = Boolean.TRUE;
                                }
                            }
                            else if (documentoPtfr.getFcLecturaNote() == null
                                    && documentoPtfr.getFcEnvioNote() != null &&
                                    (documentoPtfr.getFcFinPlazoSubsanacion() != null && fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion()))) {
                                    revisada = Boolean.TRUE;
                                
                            }
                        }
                    } else if (this.convocatoriaDTO.getItEvaluacion() != null
                            && Constantes.AfirmacionNegacion.N
                                    .equals(this.convocatoriaDTO.getItEvaluacion())) {
                        if (documentoPtfr.getFcLecturaNote() != null) {
                            if (this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() != null
                                    && (Constantes.AfirmacionNegacion.S.equals(this.expedienteBean
                                            .getExpedienteDTO().getItAlegacionRevisada()) || Constantes.AfirmacionNegacion.N.equals(this.expedienteBean
                                                    .getExpedienteDTO().getItAlegacionRevisada()))) {                               
                                    revisada = Boolean.TRUE;
                            } else if (this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() == null) {                             
                                    revisada = Boolean.FALSE;
                            }
                        } else if (documentoPtfr.getFcLecturaNote() == null
                                && documentoPtfr.getFcEnvioNote() != null &&
                                (documentoPtfr.getFcFinPlazoSubsanacion() != null && fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion()))) {
                                revisada = Boolean.TRUE;
                            
                        }
                    }
                } else if (this.convocatoriaDTO.getItAlegacion() != null
                        && Constantes.AfirmacionNegacion.N
                                .equals(this.convocatoriaDTO.getItAlegacion())) {
                    if (documentoPtfr.getFcLecturaNote() != null) {
                        revisada = Boolean.TRUE;
                    } else if (documentoPtfr.getFcLecturaNote() == null
                            && (documentoPtfr.getFcEnvioNote() != null)) {
                        
                        if("3".equals(documentoPtfr.getCdEstadoNote()) || "4".equals(documentoPtfr.getCdEstadoNote())) {
                            revisada = Boolean.TRUE;
                        }

                        Date fechaFinLecturaNote = this.cataService.calcularDiasHabiles(
                                documentoPtfr.getFcEnvioNote(), 10, Constantes.Suca.CD_MUNI_MADRID,
                                this.expedienteBean.getExpedienteDTO().getInteresadoDTO()
                                        .getCdLocalidad());

                        if (fcHoy.after(fechaFinLecturaNote)) {
                            revisada = Boolean.TRUE;
                        }

                    }
                }
            }
        }

        Boolean existeDefinitivo = false;
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO));
        
      //añadir los dos nuevos estados
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        
        if (lstEstados
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            existeDefinitivo = true;
        }
        
        
        
        
        Boolean existeRevisiones = Boolean.FALSE;
        
        if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                        .getIdValor() != null
                && (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))
                        || this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)))) {
            if (utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.REVISION_OK)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevAdminDTO()
                            .getIdValor())
                    && utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.REVISION_OK)
                            .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevRequiDTO()
                                    .getIdValor())
                    && utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.EVALUADO_OK)
                            .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                                    .getIdValor())
                    && utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)
                            .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO()
                                    .getIdValor())) {
                existeRevisiones = Boolean.TRUE;
            }
        } else {
            existeRevisiones = Boolean.TRUE;
        }
                       
//        if (Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
//                && Boolean.FALSE.equals(esModoConsulta)
//                && Boolean.TRUE.equals(revisada)
//                && Boolean.FALSE.equals(existeDefinitivo)
//                && Boolean.TRUE.equals(existeRevisiones)) {
//            habilitar = Boolean.TRUE;
//            
//            this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
//        }
        
        if ((Boolean.FALSE.equals(convocatoriaDTO.getItResProvConcDeneg()) 
                && Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta()) 
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_DEFINITIVO).equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) 
                || (Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                && Boolean.FALSE.equals(esModoConsulta)
                && Boolean.TRUE.equals(revisada)
                && Boolean.FALSE.equals(existeDefinitivo)
                && Boolean.TRUE.equals(existeRevisiones))
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;

                this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
            
        }
        
        
        return habilitar;
    }

    /**
     * 
     * @return
     */
    public Boolean habilitarBotonDescargarDefinitivo() {

        Boolean habilitar = Boolean.FALSE;

        ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                this.expedienteBean.getExpedienteDTO().getIdExpediente());
        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            habilitar = Boolean.TRUE;
        }
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext)
            habilitar = Boolean.FALSE;
        }

        return habilitar;

    }

    /**
     * 
     * @param esModoConsulta
     * @return
     */
    public Boolean habilitarBotonConcederDefinitivo(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite conceder/excluir/desistir/denegar definitivo
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean existeEstadoDefinitivo = false;
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO));
        //añadir los dos nuevos estados
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        
        
        if (lstEstados
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            existeEstadoDefinitivo = true;
        }

        Boolean existeExpedienteDocPropuestaResolucionDefinitiva = Boolean.FALSE;
        ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                this.expedienteBean.getExpedienteDTO().getIdExpediente());
        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            existeExpedienteDocPropuestaResolucionDefinitiva = Boolean.TRUE;
        }
        
        Boolean existeAlegacion = Boolean.FALSE;
        if (this.expedienteBean.getExpedienteDTO().getItAlegaciones() != null
                && Constantes.AfirmacionNegacion.S
                        .equals(this.expedienteBean.getExpedienteDTO().getItAlegaciones())
                && (this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() == null)) {
            existeAlegacion = Boolean.TRUE;
        }

        Boolean habilitar = Boolean.FALSE;
        if (Boolean.FALSE.equals(this.expedienteBean.getExpedienteDTO().getModoConsulta())
                && Boolean.FALSE.equals(esModoConsulta)
                && Boolean.FALSE.equals(existeEstadoDefinitivo)
                && Boolean.TRUE.equals(existeExpedienteDocPropuestaResolucionDefinitiva)
                && Boolean.FALSE.equals(existeAlegacion)
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }

//    /**
//     * 
//     * @return
//     */
//    private Boolean existePortafirmas() throws ServiceException {
//        Boolean existePtfr = false;
//        List<Integer> lstTipoTramite = new ArrayList<>();
//        lstTipoTramite.add(Constantes.Tramite.CONCESION_PROVISIONAL);
//        lstTipoTramite.add(Constantes.Tramite.DENEGACION_PROVISIONAL);
//        List<AtlasFilter> filtroPtfr = new ArrayList<>();
//        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
//                this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
//        filtroPtfr.add(
//                new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, lstTipoTramite, Operador.IN));
//        filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        List<AtlasSort> ordenPtfr = new ArrayList<>();
//        ordenPtfr.add(new AtlasSort(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR, false));
//        List<DocumentoPtfrDTO> lstDocPtfr = this.documentoPtfrService.buscar(0, -1, filtroPtfr,
//                ordenPtfr);
//        if (lstDocPtfr != null && !lstDocPtfr.isEmpty()) {
//            DocumentoPtfrDTO docPtfrDTO = lstDocPtfr.get(0);
//            Date fechaActual = new Date();
//            if (docPtfrDTO.getCdEstadoNote() != null
//                    && Constantes.Note.CD_PD.equals(docPtfrDTO.getCdEstadoNote())
//                    && docPtfrDTO.getFcEnvioNote() != null) {
//                Date fechaCaducidadEnvioNote = Utils
//                        .fechaSumarRestarDias(docPtfrDTO.getFcEnvioNote(), 10);
//                if (fechaActual.compareTo(fechaCaducidadEnvioNote) > 0) {
//                    existePtfr = true;
//                }
//            }
//            if (docPtfrDTO.getCdEstadoNote() != null
//                    && Constantes.Note.CD_L.equals(docPtfrDTO.getCdEstadoNote())
//                    && docPtfrDTO.getFcLecturaNote() != null) {
//                Date fechaCaducidadLecturaNote = Utils
//                        .fechaSumarRestarDias(docPtfrDTO.getFcLecturaNote(), 10);
//                if (fechaActual.compareTo(fechaCaducidadLecturaNote) > 0) {
//                    existePtfr = true;
//                }
//            }
//        }
//        return existePtfr;
//    }

    /**
     * Método para conceder definitivo para convocatoria individual cambiando el estado del expediente a
     * concedido definitivo
     */
    public void concederDefinitivo() {
        List<String> listMensajesError = new ArrayList<>(); 
        try {
            Boolean existeError = Boolean.FALSE;
            
            if (Objects.isNull(this.idConvocatoria) || Objects.isNull(this.expedienteBean.getIdExpediente())) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            //se supone que si encuentra una solicitud de este expdte es que esta activa
            SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(this.expedienteBean.getIdExpediente());                                      
                         
            if (Objects.nonNull(solicitudDTO) && Objects.nonNull(solicitudDTO.getConvocatoriaDTO())) {
                
                Convocatoria convocatoria = ConvocatoriaToEntity.INSTANCE.apply(solicitudDTO.getConvocatoriaDTO());
                
                //VMM 12/06/25 MD-1634: antes de poner el expediente como Concedido Definitivo
                //comprobamos que el resto de impresos de (justec, juesco, contratos, etc) tengan bloques/componentes y esten bien configurados
                List<ImpresoDTO> lstImpreso = this.impresoService.buscarPorIdConvocatoria(this.idConvocatoria);
                
                //filtramos por todos los impresos excepto los de solicitud y complementarios
                lstImpreso = lstImpreso.stream().filter(imp -> !imp.getTipoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD)) && 
                            !imp.getTipoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)))
                            .collect(Collectors.toList());
                
               //VMM 12/06/25 MD-1634: refactorizamos esta comprobación de configuración de formularios
               // validamos si existe algun formulario sin bloques
               existeError = this.expedienteService.comprobarConfigOtrosImpresos(convocatoria, existeError, lstImpreso, listMensajesError);
               
               if (existeError.equals(Boolean.FALSE)) {
               
                   //VMM 15/07/25 MD-1778: refactorizamos todo en el service para que haga la transaccion bien el rollback en caso de error
                    ExpedienteDTO expedienteDTO = this.expedienteService.conceder(solicitudDTO, this.expedienteBean.getIdExpediente());
        
                    this.recuperarValoresVariablesExpediente(expedienteDTO);
        
                    PrimeFaces.current().ajax().update(this.formularioResolucion);
                    PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                    PrimeFaces.current().ajax().update(this.solapaResolucion);
        
                    AtlasMessageUtils
                            .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_DEFINITIVO_OK);
               }
            }
        } catch (ServiceException e) {
            if (e.getKeyExcepcion().equals(Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA)) {
                //AtlasMessageUtils.addErrorMessage(e.getMessage());
                
                // Ahora añadir los mensajes ordenados al bundle
                AtlasMessageUtils.addErrorMessage("Debe ir al módulo de SAII_ADMINISTRACIÓN solapa ");
                Collections.sort(listMensajesError);
                for (String mensaje : listMensajesError) {
                    AtlasMessageUtils.addErrorMessage(mensaje);
                }
            } else {
                AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_DEFINITIVO_KO);
            }
            log.error(e.getMessage());
        }

    }

    

    /**
     * Metodo para habilitar el botón Revertir aprovado definitivo
     * 
     * @Param esModoCOnsulta
     * @return
     */
    public Boolean habilitaRevertirResolucionDefinitiva(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite revertir resolución definitiva
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        Boolean existeDefinitivo = false;
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
        if (lstEstados
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            existeDefinitivo = true;
        }

        ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                this.expedienteBean.getExpedienteDTO().getIdExpediente());

        Boolean existeEnviado = false;
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.RESOLUCION_NOTIFICADO));
        
        if (expeDocDTO.getEstadoDTO() != null && expeDocDTO.getEstadoDTO().getIdValor() != null
                && lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor())) {
            existeEnviado = true;
        }

        if (Boolean.TRUE.equals(esModoConsulta) || Boolean.FALSE.equals(existeDefinitivo)
                || Boolean.TRUE.equals(existeEnviado)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext

            habilitar = Boolean.FALSE;

        }
        
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)); 
        
        if (lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())) {
            return  Boolean.FALSE;
        }
        
        

        return habilitar;

    }
    
    /**
     * Metodo para habilitar el botón Revertir aprovado definitivo
     * 
     * @Param esModoCOnsulta
     * @return
     */
    public Boolean mostrarRevertirResolucionDefinitiva() {

        Boolean habilitar = Boolean.TRUE;
     
        List<Integer> lstExclusiones = new ArrayList<>();
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        lstExclusiones.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)); 
        
        if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null &&
                lstExclusiones.contains(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            return  Boolean.FALSE;
        }
        
        return habilitar;

    }
    
    
    /**
     * Método denegar definitivo indvidual, cambia el estado del expediente a denegado
     * definitivo
     */
    public void denegarDefinitivoIndividual() {

        try {

//            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
//                    this.expedienteBean.getIdExpediente(),
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO), null);
            
            
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarId( this.expedienteBean.getIdExpediente());
            expedienteDTO.setEstadoDTO(new CatalogosValoresDTO( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO)));            
            expedienteDTO = this.expedienteService.guardar(expedienteDTO);            
            //this.expedienteHistDAO.crearHistorico( ExpedienteToEntity.INSTANCE.apply(expedienteDTO), null);

            // también cambia el estado de la solicitud
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
               solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA)));
               this.solicitudDAO.update(solicitud);
            
            
            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            expeDocDTO.setTramiteDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA)));
            this.expedienteDocService.guardar(expeDocDTO);

            expedienteDTO.setBlEstadoDefinitivo(Boolean.TRUE);           
            expedienteDTO.setBAvisoResolucion(Boolean.TRUE);

            
            this.recuperarValoresVariablesExpediente(expedienteDTO);
            
           
            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGADO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DENEGADO_DEFINITIVO_KO);
        }

    }
    
    
    public void desistirDefinitivo() {

        try {

//            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
//                    this.expedienteBean.getIdExpediente(),
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO), null);
            
            
            //Cambiamos aqui el estado sin pàsar por el metodo de cambiarEstado pues este nos cambia el del beneificiario
            //luego habra que cambiarlo por otro lado
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarId( this.expedienteBean.getIdExpediente());
            expedienteDTO.setEstadoDTO(new CatalogosValoresDTO( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)));            
            this.expedienteService.guardar(expedienteDTO);            
            //this.expedienteHistDAO.crearHistorico( ExpedienteToEntity.INSTANCE.apply(expedienteDTO), null);

            // también cambia el estado de la solicitud
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
               solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA)));
               this.solicitudDAO.update(solicitud);

            
            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            expeDocDTO.setTramiteDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO)));
            this.expedienteDocService.guardar(expeDocDTO);

            //seteo los valores de avisos para color rojo
            expedienteDTO.setBlEstadoDefinitivo(Boolean.TRUE);           
            expedienteDTO.setBAvisoResolucion(Boolean.TRUE);
                      
            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DESISTIDO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.DESISTIDO_DEFINITIVO_KO);
        }

    }
    
    public void excluirDefinitivo() {

        try {

//            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(
//                    this.expedienteBean.getIdExpediente(),
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO), null);
            
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarId( this.expedienteBean.getIdExpediente());
            expedienteDTO.setEstadoDTO(new CatalogosValoresDTO( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO)));            
            this.expedienteService.guardar(expedienteDTO);            
            //this.expedienteHistDAO.crearHistorico( ExpedienteToEntity.INSTANCE.apply(expedienteDTO), null);

            // también cambia el estado de la solicitud
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
               solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA)));
               this.solicitudDAO.update(solicitud);
            
            
            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            expeDocDTO.setTramiteDTO(this.catalogosValoresService
                    .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION)));
            this.expedienteDocService.guardar(expeDocDTO);

            //seteo los valores de avisos para color rojo
            expedienteDTO.setBlEstadoDefinitivo(Boolean.TRUE);           
            expedienteDTO.setBAvisoResolucion(Boolean.TRUE);
            
            this.recuperarValoresVariablesExpediente(expedienteDTO);
            
            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.EXCLUIDO_DEFINITIVO_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.EXCLUIDO_DEFINITIVO_KO);
        }

    }
    
    

    /**
     * Metodo para revertir el resolución definitiva de un expediente
     */
    public void revertirResolucionDefinitiva() {

        try {

            // Obtenemos el ultimo registro historico
            List<AtlasFilter> filtroHist = new ArrayList<>();
            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor(),
                    Operador.NOT_EQUALS));
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_HIST, false));
            
            List<ExpedienteHistDTO> lstHistorico = this.expedienteHistService.buscar(0, -1,
                    filtroHist, ordenamientoHist);

            if (lstHistorico != null && !lstHistorico.isEmpty()) {
                ExpedienteHistDTO registroHistoricoEstadoAnterior = lstHistorico.get(0);
                
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_APROBADO_DEFINITIVO_OK);
                } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_DENEGADO_DEFINITIVO_OK);
                }else if(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())){
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_DESISTIDO_DEFINITIVO_OK);
                }else if(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVERTIR_EXCLUIDO_DEFINITIVO_OK);
                }
                
                //VMM 15/07/25 MD-1778: refactorizamos a un nuevo metodo para que si falla la transaccion haga el rollback correctamente en el service
                Map<String, Object> resultado = this.expedienteService.revertirExpedienteCompleto(
                    this.expedienteBean.getIdExpediente(),
                    this.expedienteBean.getIdSolicitud(),
                    registroHistoricoEstadoAnterior
                );

                // Actualizas el estado de la vista (JSF Beans)
                ExpedienteDTO expedienteDTO = (ExpedienteDTO) resultado.get("expedienteDTO");
                SolicitudDTO solDTO = (SolicitudDTO) resultado.get("solicitudDTO");
                this.expedienteBean.setExpedienteDTO(expedienteDTO);
                this.expedienteBean.getSolicitudBean().setSolicitudDTO(solDTO);
                
//                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
//                        Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA, this.expedienteBean.getExpedienteDTO().getIdExpediente());
//                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
//                    this.ficaService.borrar(expeDocDTO.getCdFica());
//                }
//                if (expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null) {
//                    expeDocDTO.setCdUsuBaja(UserUtils.getUserName().toUpperCase());
//                    expeDocDTO.setFcBajaFila(new Date());
//                    this.expedienteDocService.guardar(expeDocDTO);
//                }
                
                this.recuperarValoresVariablesExpediente(expedienteDTO);
                
            }
            
            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADO_DEFINITIVO_KO);
        }

    }

    /**
     * 
     */
    public void subirResolucionDefinitivaDialog() {
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDefinitiva");
    }

    /** 
     * 
     */
    public void cerrarSubirResolucionDefinitivaDialog() {
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionDefinitiva");
    }

    /**
     * 
     */
    public void cargarFicheroDefinitivo() {

        try {
            Integer tramite = null;
//            if (Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO
//                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
//                tramite = Constantes.Tramite.CONCESION_DEFINITIVA;
//            } else if (Constantes.EstadoExpediente.DENEGADO_DEFINITIVO
//                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
//                tramite = Constantes.Tramite.DENEGACION_DEFINITIVA;
//            } else {
//                AtlasMessageUtils.addErrorMessageFromBundle(
//                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
//                log.error("Error de estado de expediente : "
//                        + this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
            
//            }
            
            if (this.archivoResolucionDefinitiva == null  ) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_VACIO);
            } else {
                if (this.archivoResolucionDefinitiva.getFileName() != null) {
                    this.prepararMetadatosFica(this.archivoResolucionDefinitiva,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA), tramite);
                    this.expedienteDocDTO = this.expedienteDocService
                            .subirDocumentoFica(this.expedienteDocDTO);
                    this.guardarDocumento();
                    this.mostrarMensajeElegirFichero = Boolean.FALSE;
                } else {
                    this.mostrarMensajeElegirFichero = Boolean.TRUE;
                    PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDefinitiva");
                } 
            }

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage());
        }
    }

    

    /**
     * 
     */
    public void descargaFicheroDefinitivo() {

        try {

            ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                    this.expedienteBean.getExpedienteDTO().getIdExpediente());
            if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                        FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage());

        }

    }

    /**
     * Metodo para habilitar el boton de enviar a la cola de portafirmas
     *
     * @param esModoConsulta
     * @return
     */
    public Boolean habilitarEnviarColaPortafirmasDefinitivo(Boolean esModoConsulta) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite enviar a cola portafirmas definitivo
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean tieneFica = Boolean.FALSE;
        Boolean tieneEstadosDoc = Boolean.FALSE;
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.RESOLUCION_NOTIFICADO));
	//lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));

        ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                this.expedienteBean.getExpedienteDTO().getIdExpediente());

        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            tieneFica = Boolean.TRUE;
        }

        if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null
                && expeDocDTO.getEstadoDTO().getIdValor() != null) {
            tieneEstadosDoc = lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor());
        }
        
        Boolean tieneEstadoExpediente = Boolean.FALSE;
        List<Integer> lstEstadosExpediente = new ArrayList<>();
        lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO));
        lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
	lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        if (lstEstadosExpediente
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            tieneEstadoExpediente = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(esModoConsulta) || Boolean.FALSE.equals(tieneFica)
                || Boolean.TRUE.equals(tieneEstadosDoc)
		|| tramiteDocDefinitivoEnCola.getidDocumentoPTFR() != null
                || Boolean.FALSE.equals(tieneEstadoExpediente)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.FALSE;
        }

        return habilitar;

    }

    /**
     * 
     */
    public void enviarColaPortafirmasDefinitivo() {

        try {

            Integer tramite = 0;
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA);
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA);
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION);
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
                    .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
                tramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO);
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                log.error("Error de estado de expediente : "
                        + this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
            }
            
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = convocatoriaService
                        .buscarId(this.expedienteBean.getIdConvocatoria());
            }

            if (Objects.nonNull(this.expedienteBean.getExpedienteDTO().getIdExpediente())) {
                ExpedienteDocDTO expeDocDTO = this.expedienteService.getExpedienteDocDTO(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA),
                        this.expedienteBean.getExpedienteDTO().getIdExpediente());
                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                    FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
                            this.convocatoriaDTO, this.expedienteBean.getExpedienteDTO(),
                            this.expedienteBean.getExpedienteDTO().getIdExpediente(), tramite,
                            ficaFichero.getDatosFichero(), null, null, null);
                    if (insertado.equals(Boolean.TRUE)) {
                        expeDocDTO.setEstadoDTO(this.catalogosValoresService
                                .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(expeDocDTO,
                                UserUtils.getUserName().toUpperCase());
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                    } else {
                        log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                    }
                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                }
            }

            this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.FALSE);
            
            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e.getMessage());
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }

    }
    
    
    /**
     * Método para procesar la revisión de alegaciones del gestor
     */
//    public void procesarRevisionAlegacionGestor() throws ServiceException {
//        ExpedienteDTO expedienteDTO = this.expedienteService.procesarRevisionAlegacionGestor(
//                this.expedienteBean.getIdExpediente(),
//                this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada());
//        this.expedienteBean.setExpedienteDTO(expedienteDTO);
//        this.recuperarValoresVariablesExpediente(expedienteDTO);
//    }
    
    
    public void aprobarAlegacion() throws ServiceException {
        ExpedienteDTO expedienteDTO = this.expedienteService.procesarRevisionAlegacionGestor(
                this.expedienteBean.getIdExpediente(),
                Constantes.AfirmacionNegacion.S, this.activarConcederDenegar);
        this.expedienteBean.setExpedienteDTO(expedienteDTO);
        this.activarConcederDenegar = expedienteDTO.getActivarConcederDenegar();
        this.recuperarValoresVariablesExpediente(expedienteDTO);
      this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
      PrimeFaces.current().ajax().update(this.formularioResolucion);
      PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
      PrimeFaces.current().ajax().update(this.solapaResolucion);  
      this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
      this.financiacionBean.setBAvisoFinanciacion(Boolean.TRUE);          
      AtlasMessageUtils
      .addInfoMessageFromBundle(ConstantesWeb.Mensajes.APROBADA_ALEGACION);
      
    }
    
    
    public void rechazarAlegacion() throws ServiceException {
        ExpedienteDTO expedienteDTO = this.expedienteService.procesarRevisionAlegacionGestor(
                this.expedienteBean.getIdExpediente(),
                Constantes.AfirmacionNegacion.N,  this.activarConcederDenegar);
        this.expedienteBean.setExpedienteDTO(expedienteDTO);
        this.activarConcederDenegar = expedienteDTO.getActivarConcederDenegar();
        this.recuperarValoresVariablesExpediente(expedienteDTO);
        this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
        PrimeFaces.current().ajax().update(this.formularioResolucion);
        PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
        PrimeFaces.current().ajax().update(this.solapaResolucion);
        this.expedienteBean.getExpedienteDTO().setBAvisoResolucion(Boolean.TRUE);
        
        AtlasMessageUtils
        .addInfoMessageFromBundle(ConstantesWeb.Mensajes.RECHAZADA_ALEGACION);
        
        
    }
    
    
    
    
    public Boolean habilitarDesistirDef() {

        Boolean habilitar = Boolean.FALSE;
        Integer estadoDesistidoProvisional = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);

        if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null
                && this.expedienteBean.getExpedienteDTO() != null
                && estadoDesistidoProvisional != null
                && estadoDesistidoProvisional.equals(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;

    }
    
    public Boolean habilitarExcluirDef() {

        Boolean habilitar = Boolean.FALSE;
        Integer estadoExcluidoProvisional = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);

        if (this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null
                && this.expedienteBean.getExpedienteDTO() != null
                && estadoExcluidoProvisional != null
                && estadoExcluidoProvisional.equals(this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor())
                && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            habilitar = Boolean.TRUE;
        }

        return habilitar;

    }
    
    public Boolean activarConcederDenegar() {

        Boolean habilitar = Boolean.TRUE;
        
        if( Boolean.TRUE.equals(this.activarConcederDenegar)){
            
            if(habilitarExcluirDef() || habilitarDesistirDef() ) {
                habilitar = Boolean.FALSE;
            }else {
                habilitar = Boolean.TRUE;
            }
                    
        }else {
            habilitar = Boolean.FALSE;
        }
        

        return habilitar;

    }
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PRIVATE ACTIONS
    // ////////////////////////////////////////////////////////
    /**
     * Metodo para recuperar variables necesarias para validaciones de
     * expediente
     *
     * @param expedienteDTO
     * @throws ServiceException
     */
    public void recuperarValoresVariablesExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        expedienteDTO.setEsCerradoCoordinadorTodo(this.expedienteEvalService
                .esCerradoCoordinadorTodos(expedienteDTO.getIdExpediente()));

        expedienteDTO.setMostrarBotoneraAprobarRechazarEvaluacion(
                this.expedienteBean.getEvaluacionBean().verificarMostrarBotoneraEvaluadoGestor());

        ExpedienteDTO dto = this.expedienteService
                .comprobarResolucionDefinitiva(expedienteDTO.getIdExpediente(), this.expedienteBean.getConvocatoriaDTO());

        // asignamos los resultados de validación al expedienteDTO
        // de expedienteBean
        expedienteDTO.setEsExpedienteSolicitante(dto.getEsExpedienteSolicitante());
        expedienteDTO.setBlHabilitarResolucionDefinitiva(dto.getBlHabilitarResolucionDefinitiva());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo1(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo1());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo2(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo2());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo3(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivo3());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual1(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual1());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual2(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual2());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual3(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual3());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual4(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual4());
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual5(
                dto.getBlMostrarMensajeAprobarDenegarDefinitivoIndividual5());

        this.expedienteBean.setExpedienteDTO(expedienteDTO);

    }

    
    private void comprobarGestoresExcep(ConvocatoriaDTO conv) {
        //si tiene gestores excepcionales 
       if (conv.getListConvocatoriaGest() != null && !conv.getListConvocatoriaGest().isEmpty()) {
           
           for (ConvocatoriaGestorDTO convGest: conv.getListConvocatoriaGest()) {
               //vemos si alguno es el usuario logado y es ese caso le deshabilitamos las pestañas correspondientes
               if (convGest.getCdUsuario().equals(this.infoUsuarioBean.getLogin())) {
                   
                   if (convGest.getTipoTramite() != null && convGest.getTipoTramite() == 1 ) {  //gestor para concesion
                       conv.setEsGestorConcesion(true);
                   }else if (convGest.getTipoTramite() != null && convGest.getTipoTramite() == 2 ) {  //gestor para JCT                      
                       conv.setEsGestorJCT(true);
                   }
               }
           }
       }
        
    }
    
    public Boolean existeExpedienteDocDefinitiva() throws ServiceException {
        Boolean rpta = Boolean.FALSE;
        List<AtlasFilter> filtro = new ArrayList<>();
        filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO, 
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA), Operador.EQUALS));
        filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
        List<ExpedienteDocDTO> lstExpeDocDTO = this.expedienteDocService.buscar(0, -1, filtro, null);
        if (lstExpeDocDTO != null && !lstExpeDocDTO.isEmpty()) {
            rpta = Boolean.TRUE;
        }
        return rpta;
    }
    
    // MD-1237 JRFM - Validaciones para mostrar el mensaje de sin alegaciones pero dentro del tiempo
    public Boolean mostrarMensajeSinAlegaciones() throws ServiceException {
        
        Boolean mostrar = Boolean.FALSE;
        Date fcHoy = new Date();
        
        if (this.convocatoriaDTO == null) {
            this.convocatoriaDTO = convocatoriaService
                    .buscarId(this.expedienteBean.getIdConvocatoria());
        }
        
        if(this.expedienteBean.getExpedienteDTO().getItAlegaciones() == null 
                || Constantes.AfirmacionNegacion.N.equals(this.expedienteBean
                        .getExpedienteDTO().getItAlegaciones())) {
            List<Integer> tramites = new ArrayList<>();
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));

            //añadire dos nuevos tramites
            
            List<AtlasFilter> filtroPtfr = new ArrayList<>();
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, tramites, Operador.IN));
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<DocumentoPtfrDTO> lstDocumentoPtfr = this.documentoPtfrService.buscar(0, -1,
                    filtroPtfr, null);
            if (lstDocumentoPtfr != null && !lstDocumentoPtfr.isEmpty()) {
                DocumentoPtfrDTO documentoPtfr = lstDocumentoPtfr.get(0);
                if (documentoPtfr != null) {
                    if (documentoPtfr.getFcLecturaNote() != null) {
                                               
                        Date fechaInicioAlegacion = documentoPtfr.getFcLecturaNote();
                        Date fechaFinAlegacion = this.cataService.calcularDiasHabiles(
                                fechaInicioAlegacion, 11, Constantes.Suca.CD_MUNI_MADRID,
                                this.expedienteBean.getExpedienteDTO().getInteresadoDTO()
                                        .getCdLocalidad());                        
                        
                        if (!fcHoy.after(fechaFinAlegacion)) {
                            mostrar = Boolean.TRUE;
                        }
                    }
                    else if (documentoPtfr.getFcLecturaNote() == null
                            && documentoPtfr.getFcEnvioNote() != null
                            && (documentoPtfr.getFcFinPlazoSubsanacion() == null 
                                    || !fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion()))) {
                            mostrar = Boolean.TRUE;
                        
                    }
                }
            }
        }
        
        return mostrar;
        
    }
    
    // MD-1237 JRFM - Validaciones para mostrar el mensaje de sin alegaciones pero dentro del tiempo expirado
    public Boolean mostrarMensajeSinAlegacionesExpirada() throws ServiceException {
        
        Boolean mostrar = Boolean.FALSE;
        Date fcHoy = new Date();
        
        if (this.convocatoriaDTO == null) {
            this.convocatoriaDTO = convocatoriaService
                    .buscarId(this.expedienteBean.getIdConvocatoria());
        }
        
        if(this.expedienteBean.getExpedienteDTO().getItAlegaciones() == null 
                || Constantes.AfirmacionNegacion.N.equals(this.expedienteBean
                        .getExpedienteDTO().getItAlegaciones())) {
            List<Integer> tramites = new ArrayList<>();
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
            tramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
            
            
            List<AtlasFilter> filtroPtfr = new ArrayList<>();
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, tramites, Operador.IN));
            filtroPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<DocumentoPtfrDTO> lstDocumentoPtfr = this.documentoPtfrService.buscar(0, -1,
                    filtroPtfr, null);
            if (lstDocumentoPtfr != null && !lstDocumentoPtfr.isEmpty()) {
                DocumentoPtfrDTO documentoPtfr = lstDocumentoPtfr.get(0);
                if (documentoPtfr != null) {
                    if (documentoPtfr.getFcLecturaNote() != null) {
                        Date fechaInicioAlegacion = documentoPtfr.getFcLecturaNote();
                        Date fechaFinAlegacion = this.cataService.calcularDiasHabiles(
                                fechaInicioAlegacion, 11, Constantes.Suca.CD_MUNI_MADRID,
                                this.expedienteBean.getExpedienteDTO().getInteresadoDTO()
                                        .getCdLocalidad());
                        if (fcHoy.after(fechaFinAlegacion)) {
                            mostrar = Boolean.TRUE;
                        }
                    }
                    else if (documentoPtfr.getFcLecturaNote() == null
                            && documentoPtfr.getFcEnvioNote() != null) {
//                        Date fechaInicioAlegacion = documentoPtfr.getFcEnvioNote();
//                        Date fechaFinAlegacion = this.cataService.calcularDiasHabiles(
//                                fechaInicioAlegacion, 21, Constantes.Suca.CD_MUNI_MADRID,
//                                this.expedienteBean.getExpedienteDTO().getInteresadoDTO()
//                                        .getCdLocalidad());
                        
                        
                        if (documentoPtfr.getFcFinPlazoSubsanacion() != null && fcHoy.after(documentoPtfr.getFcFinPlazoSubsanacion())) {
                            mostrar = Boolean.TRUE;
                        }
                    }
                }
            }
        }
        
        return mostrar;
     
    }
    
    
    
    
    
    public Boolean cargarDocumentos() {
    
        cargarDocumentosPTFR();
        return Boolean.FALSE;
        
    }

    public void cargarDocumentosPTFR() {
        
        this.tramiteDocProvisional     = new EnvioDocumentoPTFR();
        this.tramiteDocDefinitivo      = new EnvioDocumentoPTFR();
        this.tramiteDocDefinitivoEnCola = new EnvioDocumentoPTFR();

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));
        
        Integer idexpedienteV = (this.expedienteBean != null) ? this.expedienteBean.getIdExpediente() : null;

        try {
            Integer estadosProv = null;          
            Integer estadosDef = null;
            
            // 1. MAPPING ESTADO PROVISIONAL
            if (this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null) {
                Integer idEstadoProv = this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor();
                if (idEstadoProv != null) {
                    if (idEstadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))) {
                        estadosProv = valorConcedidoProv;
                    } else if (idEstadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                        estadosProv = valorDenegadoProv;
                    } else if (idEstadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))) {
                        estadosProv = valorExcluidoProv;
                    } else if (idEstadoProv.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))) {
                        estadosProv = valorDesistidoProv;
                    }
                }
            }

            // 2. MAPPING ESTADO DEFINITIVO
            if (this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null) {
                Integer idEstadoDef = this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor();
                if (idEstadoDef != null) {
                    if (idEstadoDef.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO))) {
                        estadosDef = valorConcedidoDef;
                    } else if (idEstadoDef.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO))) {
                        estadosDef = valorDenegadoDef;
                    } else if (idEstadoDef.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO))) {
                        estadosDef = valorExcluidoDef;
                    } else if (idEstadoDef.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO))) {
                        estadosDef = valorDesistidoDef;
                    }
                }
            }

            // 3. CONSULTA PROVISIONAL (Solo si tenemos idExpediente y estadosProv determinado)
            if (idexpedienteV != null && estadosProv != null) {
                List<AtlasFilter> filtrosProvisional = new ArrayList<>();
                filtrosProvisional.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, this.idConvocatoria, Operador.EQUALS));
                filtrosProvisional.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idexpedienteV, Operador.EQUALS));
                filtrosProvisional.add(new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_NOTE, Operador.NOT_NULL));
                filtrosProvisional.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_NOTE, Operador.NOT_NULL));
                filtrosProvisional.add(new AtlasFilter(Constantes.Multipart.ID_TRAMITE, estadosProv, Operador.EQUALS));

                List<EnvioDocumentoPTFR> listaTramiteDocProvisional = this.envioDocumentoPtfrService.find(filtrosProvisional, orden);   
                if (listaTramiteDocProvisional != null && !listaTramiteDocProvisional.isEmpty() && listaTramiteDocProvisional.get(0) != null) {
                    this.tramiteDocProvisional = listaTramiteDocProvisional.get(0);
                }
            }

            // 4. CONSULTAS DEFINITIVAS (Solo si tenemos idExpediente y estadosDef determinado)
            if (idexpedienteV != null && estadosDef != null) {
                // Busqueda Definitivo
                List<AtlasFilter> filtrosDefinitivo = new ArrayList<>();
                filtrosDefinitivo.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, this.idConvocatoria, Operador.EQUALS));
                filtrosDefinitivo.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idexpedienteV, Operador.EQUALS));
                filtrosDefinitivo.add(new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_NOTE, Operador.NOT_NULL));
                filtrosDefinitivo.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_NOTE, Operador.NOT_NULL));
                filtrosDefinitivo.add(new AtlasFilter(Constantes.Multipart.ID_TRAMITE, estadosDef, Operador.EQUALS));

                List<EnvioDocumentoPTFR> listaTramiteDocDefinitivo = this.envioDocumentoPtfrService.find(filtrosDefinitivo, orden);
                if (listaTramiteDocDefinitivo != null && !listaTramiteDocDefinitivo.isEmpty() && listaTramiteDocDefinitivo.get(0) != null) {
                    this.tramiteDocDefinitivo = listaTramiteDocDefinitivo.get(0);
                }

                // Busqueda Definitivo En Cola
                List<AtlasFilter> filtrosDefinitivoEnCola = new ArrayList<>();
                filtrosDefinitivoEnCola.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, this.idConvocatoria, Operador.EQUALS));
                filtrosDefinitivoEnCola.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idexpedienteV, Operador.EQUALS));
                filtrosDefinitivoEnCola.add(new AtlasFilter(Constantes.Multipart.ID_TRAMITE, estadosDef, Operador.EQUALS));

                List<EnvioDocumentoPTFR> listaTramiteDocDefinitivoEnCola = this.envioDocumentoPtfrService.find(filtrosDefinitivoEnCola, orden);
                if (listaTramiteDocDefinitivoEnCola != null && !listaTramiteDocDefinitivoEnCola.isEmpty() && listaTramiteDocDefinitivoEnCola.get(0) != null) {
                    this.tramiteDocDefinitivoEnCola = listaTramiteDocDefinitivoEnCola.get(0);
                }
            }

            // 5. BÚSQUEDA DEL DOCUMENTO 700
            if (idexpedienteV != null) {
                this.expedienteDocDTO700 = this.expedienteDocService.buscarPorIdExpTipoDoc(
                        idexpedienteV, 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_ACEPTACION_DESIS_FIRMADO), 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA)); 
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessage(e.getMessage());
        }
    }
    
    public Boolean mostrarDatosProvisional() {
        
        Boolean habilitar = Boolean.FALSE;
             
        if( this.tramiteDocProvisional.getidTramite().equals(valorConcedidoProv) || this.tramiteDocProvisional.getidTramite().equals(valorDenegadoProv)
                || this.tramiteDocProvisional.getidTramite().equals(valorDesistidoProv) || this.tramiteDocProvisional.getidTramite().equals(valorExcluidoProv)) {
            
            habilitar = Boolean.TRUE;
        }
        
        
        
        return habilitar;

    }
    
    public Boolean mostrarDatosDefinitvo() {
        
        Boolean habilitar = Boolean.FALSE;
             
        if( this.tramiteDocDefinitivo.getidTramite().equals(valorConcedidoDef) || this.tramiteDocDefinitivo.getidTramite().equals(valorDenegadoDef)
                || this.tramiteDocDefinitivo.getidTramite().equals(valorDesistidoDef) || this.tramiteDocDefinitivo.getidTramite().equals(valorExcluidoDef)) {
            
            habilitar = Boolean.TRUE;
        }
 
        return habilitar;

    }
    
    
    /**
     * 
     */
    public void descargaFicheroAceptacion() {

        try {
            if (this.expedienteBean.getExpedienteDTO() != null && this.expedienteBean.getExpedienteDTO().getIdExpediente()!= null) {
               DocumentoDTO aceptacion = this.pdfService.descargarAceptacion(this.expedienteBean.getExpedienteDTO().getIdExpediente());

               log.info("Descargar expediente " + aceptacion.getNombreFichero());
                UtilesWeb.openFile(aceptacion.getContenidoFichero(), "zip", aceptacion.getNombreFichero().replace(".zip", ""));
                           
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }

        } catch (ServiceException e) {

            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage());

        }

    }
    
    public void finalizarRevisionAyuda() {
        try {          
            Integer estadoexp;    
            Integer estadoSol; 
            if(this.expedienteBean.getExpedienteDTO().getItAceptaAyuda().equals("S")) {
                estadoexp = Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO;
                estadoSol = Constantes.EstadoSolicitud.CONCEDIDA;
            }else {
                estadoexp = Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO;
                estadoSol = Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA;
            }

            ExpedienteDTO expedienteDTO = this.expedienteService.cambiarEstado(this.expedienteBean.getIdExpediente(),utilConstantes.getGlobalCatalogosValores().get(estadoexp), null);
            ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expedienteDTO.getConvocatoriaActDTO().getIdActor());           
            SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(this.expedienteBean.getIdExpediente());               
            this.solicitudService.cambiarEstado(solicitudDTO.getIdSolicitud(),utilConstantes.getGlobalCatalogosValores().get(estadoSol));
            
            Convocatoria convocatoria = ConvocatoriaToEntity.INSTANCE.apply(solicitudDTO.getConvocatoriaDTO());
                     
            if (convocatoriaActDTO != null && estadoexp.equals( Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)) {   
                this.expedienteService.crearOtrosImpresosExpdte(solicitudDTO, convocatoriaActDTO, expedienteDTO, convocatoria);
            }
            
            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioResolucion);
            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaResolucion);

            AtlasMessageUtils
                    .addInfoMessageFromBundle(ConstantesWeb.Mensajes.CONCEDIDO_DEFINITVO_AYUDA_OK);

        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addWarnMessageFromBundle(ConstantesWeb.Mensajes.CONCEDIDO_DEFINITVO_AYUDA_KO);
        }

    }
    
    
}
