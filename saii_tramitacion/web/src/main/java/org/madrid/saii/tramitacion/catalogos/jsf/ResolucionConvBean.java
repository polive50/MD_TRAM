package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComisionImportesDTO;
import org.madrid.saii.lib.dto.ComisionSeleccionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.PropuestaResolucionDTO;
import org.madrid.saii.lib.dto.ResolucionConcesionDTO;
import org.madrid.saii.lib.dto.ResolucionExclusionDesistimientoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteService;
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
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.componentes.utils.PrimefacesUtils;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean
 *
 * @author nttdata
 *
 * @since 10-agost-2022 08:22:25
 *
 * @see ResolucionConvBean
 */
@Slf4j
@Getter
@Setter
@Component("resolucionConvBean")
@Scope("request")
public class ResolucionConvBean {
    // extends CatalogoBean<Convocatoria, Integer>
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;
    
    private Integer itContratos;
    
    /** DTO de la convocatoria **/
    private ConvocatoriaDTO convocatoriaDTO;
    
    
    /**
     * archivoResolucionProvisional
     */
    private UploadedFile archivoResolucion;

    /**
     * archivoProvisionalDefinitiva
     */
    private UploadedFile archivoResolucionDefinitiva;
    
    /** Fichero de la resolución individual de exclusión. */
    private UploadedFile archivoResolucionExclusion;

    /** Fichero de la resolución individual de desistimiento. */
    private UploadedFile archivoResolucionDesistimiento;
    
    /** PDF sin firmar de la resolución de concesión. */
    private UploadedFile archivoResolucionConcesion;
    
    /** Fichero de resolucion propuesta de resolcuion definitiva **/
    private UploadedFile archivoPropuestaDefinitiva;
    
    /*
     * expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    
    private Boolean mostrarMensajeElegirFichero = Boolean.FALSE;
    
    /** Subpestaña activa de la resolución de convocatoria. */
    private int pestanaResolucionConvActiva;
    
    /** Indica si se ha seleccionado la resolución conjunta. */
    private Boolean resolucionExclusionDesistimientoConjunta = Boolean.TRUE;

    /** Consulta de los envíos de la convocatoria, reutilizada únicamente durante esta petición. */
    private List<Integer> tramitesResolucionEnviados;
    private Integer idConvocatoriaEnviosConsultados;
    private boolean errorConsultaEnviosResolucion;
    
    /** Estados provisionales consultados una sola vez durante esta petición. */
    private List<Integer> estadosProvisionalesResolucion;
    private Integer idConvocatoriaProvisionalesConsultados;
    private boolean errorConsultaProvisionalesResolucion;
    
    /** Indicadores de activación de las botoneras de resolución. */
    private Boolean activarBotonesResolucionConjunta = Boolean.TRUE;
    private Boolean activarBotonesResolucionIndividual = Boolean.FALSE;

    /** Referencias de los botones actualizados al cambiar el tipo de resolución. */
    private String botonSubirExclusion = "convocatoriaForm:contenedorSolapas:"
            + "contenedorResolucionConvSolapas:botonSubirResolucionExclusion";
    private String botonDescargarExclusion = "convocatoriaForm:contenedorSolapas:"
            + "contenedorResolucionConvSolapas:botonDescargarResolucionExclusion";
    private String botonSubirDesistimiento = "convocatoriaForm:contenedorSolapas:"
            + "contenedorResolucionConvSolapas:botonSubirResolucionDesistimiento";
    private String botonDescargarDesistimiento = "convocatoriaForm:contenedorSolapas:"
            + "contenedorResolucionConvSolapas:botonDescargarResolucionDesistimiento";
    private String botoneraResolucionExclusionDesistimiento = "convocatoriaForm:contenedorSolapas:"
            + "contenedorResolucionConvSolapas:botoneraResolucionExclusionDesistimiento";
    
    private boolean mostrarMensajeElegirFicheroPropDef = false;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;
    
    
    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;
    
    
    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;
    
    /**
     * Servicio de documentoPtfrService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;
    
    
    /**
     * Servicio de catalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    /** InforUsuario */
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;
    
    private ConvocatoriasBean convocatoriasBean;
    
    
    /**
     * ExpedienteBean
     */
    private ExpedienteBean expedienteBean;
    
    /**
     * ExpedienteBean
     */
    private ResolucionBean resolucionBean;
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ResolucionConvBean() {
        super();
        this.inicializarDocumento();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PUBLIC ACTIONS
    // ////////////////////////////////////////////////////////
    /**
     * Setear IdConvocatoria
     *
     * @param pIdConvocatoria
     */
    public void setearIdConvocatoria(Integer pIdConvocatoria) {

        // Sólo debe ejecutarse tras la primera vez que se renderiza la pantalla
        // En las siguientes el id convocatoria está guardado con
        // AtlasGuardarEstado
        if (this.idConvocatoria == null) {
            this.idConvocatoria = pIdConvocatoria;
        }
    }

//    /**
//     * Metodo que se ejecuta después del constructor.
//     */
//    @PostConstruct
//    public void init() {
//
//        this.inicializarDocumento();
//
//    }
    
    
    /**
     * Metodo que inicializa el documento. Pone el ConvocatoriaDocDTO,nombre de
     * los archivos y el tipo de Documento como nuevo.
     */
    public void inicializarDocumento() {
        this.expedienteDocDTO = new ExpedienteDocDTO();
        this.archivoResolucion = null;
        this.archivoResolucionDefinitiva = null;
        this.archivoResolucionConcesion = null;
    }
    
    /**
     * método que resolverá la convocatoria cuando los estados de los
     * expedientes esten en terminado
     */
    public void resolverConvocatoria() {
        List<String> listMensajesError = new ArrayList<>(); 
        try {            
            if (Objects.nonNull(this.idConvocatoria)) {
                               
                ConvocatoriaDTO convocatoriaDTO = this.expedienteService
                        .resolver(this.idConvocatoria, listMensajesError);
                
                if (Boolean.TRUE.equals(this.verificarRangoFechas(convocatoriaDTO))) {

                    this.convocatoriasBean.setConvocatoriaDTO(convocatoriaDTO);
                    this.convocatoriasBean.setModoConsulta(Boolean.TRUE);
                    //esto no es necesario porque ya se setea el filtro al entrar en ADMIN cuando vea que está Resuelta la convocatoria
                    //this.convocatoriasBean.setConvocatoriasResueltaFilter(Boolean.TRUE); //esta propiedad no está en la clase padre CatalogoBean
                                        
                    //TODO VMM 11/06/25 esto se ha quedado obsoleto pero se usará, ahora mismo ese valor no existe
                    convocatoriaDTO.setIdEstadoResolucion(4);  
                    this.convocatoriaService.guardar(convocatoriaDTO);
                    
                    AtlasMessageUtils
                        .addInfoMessageFromBundle(ConstantesWeb.Mensajes.RESOLVER_CONVOCATORIA_OK);
                }
            } 
        }catch (ServiceException e) {
            if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(e.getKeyExcepcion())) {
                
                // Ahora añadir los mensajes ordenados al bundle
                AtlasMessageUtils.addErrorMessage("Debe ir al módulo de SAII_ADMINISTRACIÓN solapa: ");
                Collections.sort(listMensajesError);
                for (String mensaje : listMensajesError) {
                    AtlasMessageUtils.addErrorMessage(mensaje);
                }
            } else {
                AtlasMessageUtils
                        .addErrorMessage(Constantes.Error.ERROR_AL_INTENTAR_RESOLVER_CONVOCATORIA);
            }
        }
    }
    
    /**
     * método que resolverá la convocatoria cuando los estados de los
     * expedientes esten en terminado
     */
    public void resolverConvocatoriaProv() {
        try {
            if (Objects.nonNull(this.idConvocatoria)) {
                
                ConvocatoriaDTO convo = this.convocatoriaService.buscarId(this.idConvocatoria);
                //TODO VMM 11/06/25 esto se ha quedado obsoleto pero se usará, poner la constante correcta...
                convo.setIdEstadoResolucion(2);
                this.convocatoriaService.guardar(convo);
                
              AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.RESOLVER_CONVOCATORIA_PROV_OK);

            } 
        }catch (ServiceException e) {
            if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(e.getKeyExcepcion())) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessage(Constantes.Error.ERROR_AL_INTENTAR_RESOLVER_CONVOCATORIA);
            }
        }
    }
    

    /**
     * método para habilitar y deshabilitar el boton de resolver convocatoria
     *
     * @return
     */
    public Boolean habilitarBotonResolverConvocatoria() {
        Boolean habilitar = Boolean.FALSE;
//        Boolean documentoSubido = Boolean.FALSE;
        
        try {
            
            if (Boolean.FALSE.equals(this.convocatoriasBean.isModoConsulta())) {
                
                ConvocatoriaDTO dto = this.convocatoriaService.buscarId(this.idConvocatoria);
                this.comprobarGestoresExcep(dto);
               // dto.setEsGestorConcesion(this.convocatoriasBean.getConvocatoriaDTO().getEsGestorConcesion());
               // dto.setEsGestorJCT(this.convocatoriasBean.getConvocatoriaDTO().getEsGestorJCT());
                
                
//                  ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL);
//                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
//                    documentoSubido = Boolean.TRUE;
//                }

                if (Objects.nonNull(dto) && Objects.nonNull(dto.getIdConvocatoria())
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES).equals(dto.getIdEstado().getIdValor())
                        && !dto.isEsGestorJCT()
                        && !dto.isEsGestorConcesion()
//                        && Boolean.TRUE.equals(documentoSubido.equals(Boolean.FALSE))
                        && Boolean.FALSE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                        ) {
                    habilitar = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessage(Constantes.Error.ERROR_ID_CONVOCATORIA);
        }

        return habilitar;
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
     
     /**
     *
     * @return
     */
    private Boolean verificarRangoFechas(ConvocatoriaDTO convocatoriaDTO) {
        Boolean esValido = Boolean.TRUE;

        // si la fecha de fin de contrato es menor o igual a la de inicio de contrato --> error
        if (convocatoriaDTO != null && convocatoriaDTO.getFcIniContrato() != null && convocatoriaDTO.getFcFinContrato() != null) {
            if (convocatoriaDTO.getFcFinContrato().before(convocatoriaDTO.getFcIniContrato())
                || convocatoriaDTO.getFcIniContrato().equals(convocatoriaDTO.getFcFinContrato())) {
                    AtlasMessageUtils.addErrorMessage(Constantes.Error.ERROR_MENSAJE_FECHAS_CONTRATO_VACIO);
                    esValido = Boolean.FALSE;
            }
        }
        return esValido;
    }

    
    /**
     * 
     */
    public void enviarColaPortafirmasProvisional() {
        ExpedienteDTO exp = null;
        try {

            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            
            
            List<SolicitudDTO> listaSol = this.solicitudService.buscarPorIdConvocatoria(this.idConvocatoria);               
            if(!listaSol.isEmpty()) {
                
                for (SolicitudDTO solicitudDTO : listaSol) {
                    List<Expediente> listaexpedientes = this.expedienteDAO.recuperarExpedientesByIdSolicitud(solicitudDTO.getIdSolicitud());  
                    
                    if(!listaexpedientes.isEmpty()) {
                       exp = this.expedienteService.buscarId(listaexpedientes.get(0).getIdExpediente());    
                        break;
                    }
                }               
            }


            if (Objects.nonNull(exp) && Objects.nonNull(exp.getIdExpediente())) {
                
                ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL));
                
                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                    
                    FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR(this.convocatoriaDTO, exp,
                            exp.getIdExpediente(), 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL),
                            ficaFichero.getDatosFichero(), null, null, null);
                    
                    if (insertado.equals(Boolean.TRUE)) {
                        expeDocDTO.setEstadoDTO(this.catalogosValoresService
                                .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(expeDocDTO,
                                UserUtils.getUserName().toUpperCase());
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
                    } else {
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
                    }
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                }
            }

//            PrimeFaces.current().ajax().update(this.formularioResolucion);
//            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
//            PrimeFaces.current().ajax().update(this.solapaResolucion);

        } catch (ServiceException e) {
            if (e.getKeyExcepcion() == null) {
                AtlasMessageUtils.addErrorMessage(e.getMessage());
            } else {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        }

    }
    
    /**
     * 
     */
    public void enviarColaPortafirmasDefinitivo() {
        ExpedienteDTO exp = null;
        try {

            
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }

            List<SolicitudDTO> listaSol = this.solicitudService.buscarPorIdConvocatoria(this.idConvocatoria);               
            if(!listaSol.isEmpty()) {
                
                for (SolicitudDTO solicitudDTO : listaSol) {
                    List<Expediente> listaexpedientes = this.expedienteDAO.recuperarExpedientesByIdSolicitud(solicitudDTO.getIdSolicitud());  
                    
                    if(!listaexpedientes.isEmpty()) {
                       exp = this.expedienteService.buscarId(listaexpedientes.get(0).getIdExpediente());    
                        break;
                    }
                }               
            }


            if (Objects.nonNull(exp) && Objects.nonNull(exp.getIdExpediente())) {
                ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO));
                if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
                    FicaFichero ficaFichero = this.ficaService.recuperar(expeDocDTO.getCdFica());
                    Boolean insertado = documentoPtfrService.guardarEnvioEnDcoumentoPTFR( this.convocatoriaDTO,exp, exp.getIdExpediente(), 
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO),
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

//            PrimeFaces.current().ajax().update(this.formularioResolucion);
//            PrimeFaces.current().ajax().update(this.panelEstadoResolucion);
//            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
//            PrimeFaces.current().ajax().update(this.solapaResolucion);

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

        Boolean habilitar = Boolean.TRUE;
        Boolean tieneFica = Boolean.FALSE;
        Boolean tieneEstadosDoc = Boolean.FALSE;
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));

        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL));

        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            tieneFica = Boolean.TRUE;
        }

        if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null
                && expeDocDTO.getEstadoDTO().getIdValor() != null) {
            tieneEstadosDoc = lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor());
        }

        if (Boolean.TRUE.equals(esModoConsulta) || Boolean.FALSE.equals(tieneFica)
                || Boolean.TRUE.equals(tieneEstadosDoc)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                ) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;

    }
    
    
    /**
     * Metodo para habilitar el boton de enviar a la cola de portafirmas
     *
     * @param esModoConsulta
     * @return
     */
    public Boolean habilitarEnviarColaPortafirmasDefinitivo(Boolean esModoConsulta) {

        Boolean habilitar = Boolean.TRUE;
        Boolean tieneFica = Boolean.FALSE;
        Boolean tieneEstadosDoc = Boolean.FALSE;
        List<Integer> lstEstadosDoc = new ArrayList<>();
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIADO_A_PTFR));
        lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));

        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO));

        if (expeDocDTO != null && expeDocDTO.getCdFica() != null) {
            tieneFica = Boolean.TRUE;
        }

        if (expeDocDTO != null && expeDocDTO.getEstadoDTO() != null
                && expeDocDTO.getEstadoDTO().getIdValor() != null) {
            tieneEstadosDoc = lstEstadosDoc.contains(expeDocDTO.getEstadoDTO().getIdValor());
        }
        

        if (Boolean.TRUE.equals(esModoConsulta) || Boolean.FALSE.equals(tieneFica)
                || Boolean.TRUE.equals(tieneEstadosDoc)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno()) //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
                ) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;

    }

    
    
    
    public ExpedienteDocDTO getExpedienteDocDTO(Integer tipoDocumento) {
        
        return this.getExpedienteDocDTO(tipoDocumento, null);
    }

    /**
     * Recupera el documento de la convocatoria por tipo y, si se indica, por
     * trámite.
     *
     * @param tipoDocumento tipo documental
     * @param tramite trámite del documento
     * @return documento encontrado
     */
    private ExpedienteDocDTO getExpedienteDocDTO(Integer tipoDocumento, Integer tramite) {

        ExpedienteDocDTO expeDocDTO = new ExpedienteDocDTO();

        try {

            List<AtlasFilter> filtroExpeDocProv = new ArrayList<>();
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtroExpeDocProv
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                            tipoDocumento, Operador.EQUALS));
            
            if (tramite != null) {
                filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                        tramite, Operador.EQUALS));
            }
            
            List<SolicitudDTO> listaSol = this.solicitudService.buscarPorIdConvocatoria(this.idConvocatoria);               
            if(!listaSol.isEmpty()) {
                
                for (SolicitudDTO solicitudDTO : listaSol) {
                    List<Expediente> listaexpedientes = this.expedienteDAO.recuperarExpedientesByIdSolicitud(solicitudDTO.getIdSolicitud());  
                    
                    if(!listaexpedientes.isEmpty()) {
                        filtroExpeDocProv.add(new AtlasFilter(
                                Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                                listaexpedientes.get(0).getIdExpediente(),
                                Operador.EQUALS));
                        break;
                    }
                    
                }
               
            }

            
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            List<ExpedienteDocDTO> lstExpeDocDTO = this.expedienteDocService.buscar(0, -1,
                    filtroExpeDocProv, ordenamientoHist);
            if (lstExpeDocDTO != null && !lstExpeDocDTO.isEmpty() && lstExpeDocDTO.get(0) != null) {
                expeDocDTO = lstExpeDocDTO.get(0);
            }

        } catch (Exception e) {
            AtlasMessageUtils.addErrorMessage(e.getMessage());

        }

        return expeDocDTO;

    }
    
    /**
     * 
     */
    public void subirResolucionProvisionalDialog() {
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionProvisionalConv");
    }

    /** 
     * 
     */
    public void cerrarSubirResolucionProvisionalDialog() {
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionProvisionalConv");
    }

    
    public void cargarFicheroProvisional() {

        try {
            Integer tramite = null;

            if (this.archivoResolucion.getFileName() != null) {
                this.prepararMetadatosFica(this.archivoResolucion,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL), tramite);
                this.expedienteDocDTO = this.expedienteDocService
                        .subirDocumentoFica(this.expedienteDocDTO);
                this.guardarDocumento();

//                this.recuperarValoresVariablesExpediente(this.expedienteBean.getExpedienteDTO());
                this.mostrarMensajeElegirFichero = Boolean.FALSE;
            } else {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionProvisional");
            }
        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage());
        }
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
     * @param archivoResolucion
     * @param propuestaResolucion
     * @param tramite
     * @return
     */
    public ExpedienteDocDTO prepararMetadatosFica(UploadedFile archivoResolucion,
            Integer propuestaResolucion, Integer tramite) {

        if (Objects.nonNull(archivoResolucion)) {

            try {
                ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(propuestaResolucion, tramite);
                InputStream fileInputStream = archivoResolucion.getInputstream();
                String nombreArchivo = archivoResolucion.getFileName();
                this.expedienteDocDTO.setIdExpedienteDoc(
                        expeDocDTO != null && expeDocDTO.getIdExpedienteDoc() != null
                                ? expeDocDTO.getIdExpedienteDoc()
                                : null);
                
               
                List<SolicitudDTO> listaSol = this.solicitudService.buscarPorIdConvocatoria(this.idConvocatoria);   
                
                if(!listaSol.isEmpty()) {
                    
                    for (SolicitudDTO solicitudDTO : listaSol) {
                        List<Expediente> listaexpedientes = this.expedienteDAO.recuperarExpedientesByIdSolicitud(solicitudDTO.getIdSolicitud());  
                        
                        if(!listaexpedientes.isEmpty()) {
                            
                            ExpedienteDTO exp = this.expedienteService.buscarIdSimple(listaexpedientes.get(0).getIdExpediente(), Boolean.FALSE);                            
                            this.expedienteDocDTO.setExpedienteDTO(exp);
                            break;
                        }
                        
                    }
                   
                }

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
    
    
    
    
    public void subirResolucionDefinitivaDialog() {
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDefinitivaConv");
    }

    /** 
     * 
     */
    public void cerrarSubirResolucionDefinitivaDialog() {
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionDefinitivaConv");
    }
    
    /**
     * Abre el diálogo de subida de la resolución conjunta de exclusión y
     * desistimiento.
     */
    public void subirResolucionExclusionDesistimientoDialog() {
        this.archivoResolucionDefinitiva = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionExclusionDesistimiento");
    }

    /**
     * Cierra el diálogo de subida de la resolución conjunta de exclusión y
     * desistimiento.
     */
    public void cerrarSubirResolucionExclusionDesistimientoDialog() {
        this.archivoResolucionDefinitiva = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionExclusionDesistimiento");
    }
    
    /**
     * Abre el diálogo de subida de la resolución de concesión.
     */
    public void subirResolucionConcesionDialog() {
        this.archivoResolucionConcesion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
    }

    /**
     * Cierra el diálogo de subida de la resolución de concesión.
     */
    public void cerrarSubirResolucionConcesionDialog() {
        this.archivoResolucionConcesion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionConcesion");
    }

    /**
     * Indica si se puede subir o sustituir el PDF antes de su envío.
     */
    public boolean habilitarBotonSubirResolucionConcesion() {
        if (!this.permitirModificarResolucionConcesion()) {
            return false;
        }
        try {
            List<ResolucionConcesionDTO> expedientes =
                    this.obtenerExpedientesPendientesResolucionConcesion();
            return !expedientes.isEmpty();
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean permitirModificarResolucionConcesion() {
        return this.idConvocatoria != null
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())
                && !Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno());
    }

    /**
     * Sube el mismo PDF a los expedientes pendientes de concesión.
     */
    public void cargarFicheroResolucionConcesion() {
        if (!this.habilitarBotonSubirResolucionConcesion()) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            return;
        }
        UploadedFile archivo = this.archivoResolucionConcesion;
        if (archivo != null && archivo.getFileName() != null
                && (!"pdf".equalsIgnoreCase(FilenameUtils.getExtension(archivo.getFileName()))
                        || archivo.getSize() == 0)) {
            AtlasMessageUtils.addErrorMessageFromBundle("resolucion.concesion.documento.pdf");
            PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
            return;
        }
        this.guardarFicheroResolucionConcesion(archivo);
    }
    
    public boolean habilitarBotonDescargarResolucionConcesion() {
        if (this.idConvocatoria == null) {
            return false;
        }
        ExpedienteDocDTO documento = this.getDocumentoResolucionConcesion();
        return documento != null && documento.getCdFica() != null;
    }
    
    public void descargarFicheroResolucionConcesion() {
        if (this.idConvocatoria != null) {
            this.descargarDocumentoResolucion(this.getDocumentoResolucionConcesion());
        }
    }
    
    public boolean habilitarEnviarResolucionConcesionPortafirmas() {
        if (!this.permitirModificarResolucionConcesion()) {
            return false;
        }
        try {
            for (ResolucionConcesionDTO expediente : this.obtenerExpedientesPendientesResolucionConcesion()) {
                ExpedienteDocDTO documento = this.getDocumentoResolucionConcesion(expediente.getIdExpediente());
                if (documento != null && documento.getCdFica() != null) {
                    return true;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public void enviarResolucionConcesionPortafirmas() {
        if (!this.habilitarEnviarResolucionConcesionPortafirmas()) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            return;
        }
        this.enviarResolucionConcesionPortafirmasValidada();
    }

    /**
     * Abre el diálogo de subida de la resolución individual de exclusión.
     */
    public void subirResolucionExclusionDialog() {
        this.archivoResolucionExclusion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionExclusion");
    }

    /**
     * Cierra el diálogo de subida de la resolución individual de exclusión.
     */
    public void cerrarSubirResolucionExclusionDialog() {
        this.archivoResolucionExclusion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionExclusion");
    }

    /**
     * Abre el diálogo de subida de la resolución individual de desistimiento.
     */
    public void subirResolucionDesistimientoDialog() {
        this.archivoResolucionDesistimiento = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDesistimiento");
    }

    /**
     * Cierra el diálogo de subida de la resolución individual de desistimiento.
     */
    public void cerrarSubirResolucionDesistimientoDialog() {
        this.archivoResolucionDesistimiento = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionDesistimiento");
    }

    /**
     * Actualiza el tipo de resolución seleccionado al cambiar entre resolución
     * conjunta e individual.
     */
    public void cambiarTipoResolucionExclusionDesistimiento(AjaxBehaviorEvent event) {
        UIInput selector = (UIInput) event.getComponent();
        if (!this.isModoResolucionExclusionDesistimientoBloqueado()) {
            this.resolucionExclusionDesistimientoConjunta = Boolean.TRUE
                    .equals(selector.getValue());
        }
        // Descarta el valor local del componente para pintar el modo efectivo del modelo.
        selector.resetValue();
    }

    /** Recupera el modo enviado, con prioridad sobre el valor restaurado por guardarEstado. */
    public Boolean getResolucionExclusionDesistimientoConjunta() {
        this.consultarEnviosResolucion();
        if (this.idConvocatoria == null || this.errorConsultaEnviosResolucion
                || this.isEnviosResolucionIncompatibles()) {
            return null;
        }
        if (this.tramitesResolucionEnviados.contains(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO)) {
            return Boolean.TRUE;
        }
        if (!this.tramitesResolucionEnviados.isEmpty()) {
            return Boolean.FALSE;
        }
        return this.resolucionExclusionDesistimientoConjunta;
    }

    /** No se guarda en la vista: cada nueva petición recupera los datos persistidos. */
    private void consultarEnviosResolucion() {
        if (this.idConvocatoria == null
                || this.idConvocatoria.equals(this.idConvocatoriaEnviosConsultados)) {
            return;
        }
        this.idConvocatoriaEnviosConsultados = this.idConvocatoria;
        this.errorConsultaEnviosResolucion = false;
        this.tramitesResolucionEnviados = new ArrayList<>();
        try {
            this.tramitesResolucionEnviados = this.documentoPtfrService
                    .obtenerTramitesResolucionEnviados(this.idConvocatoria);
        } catch (ServiceException e) {
            this.errorConsultaEnviosResolucion = true;
            log.error("Error consultando los envíos de resolución de la convocatoria "
                    + this.idConvocatoria, e);
        }
    }

    /** No elige un modo cuando hay peticiones activas individuales y conjuntas. */
    public boolean isEnviosResolucionIncompatibles() {
        this.consultarEnviosResolucion();
        return this.tramitesResolucionEnviados != null
                && this.tramitesResolucionEnviados.contains(
                        Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO)
                && (this.tramitesResolucionEnviados.contains(Constantes.Tramite.EXCLUSION)
                        || this.tramitesResolucionEnviados.contains(Constantes.Tramite.DESISTIMIENTO));
    }

    /** Impide cambiar el modo enviado y bloquea la selección si no se puede determinar. */
    public boolean isModoResolucionExclusionDesistimientoBloqueado() {
        this.consultarEnviosResolucion();
        return this.idConvocatoria == null || this.errorConsultaEnviosResolucion
                || !this.tramitesResolucionEnviados.isEmpty();
    }

    public boolean isErrorConsultaEnviosResolucion() {
        this.consultarEnviosResolucion();
        return this.errorConsultaEnviosResolucion;
    }

    /**
     * Indica si se pueden utilizar los botones de las resoluciones individuales.
     *
     * @return true cuando se ha seleccionado la resolución individual
     */
    public boolean habilitarBotonesResolucionIndividual() {
        return Boolean.FALSE.equals(this.getResolucionExclusionDesistimientoConjunta());
    }

    /**
     * Indica si se puede subir una resolución definitiva individual o conjunta.
     *
     * @param tramite constante del trámite individual o conjunto
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return true cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonSubirResolucionDefinitiva(Integer tramite,
            Boolean esModoConsulta) {
        boolean modoCorrecto = Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)
                ? Boolean.TRUE.equals(this.getResolucionExclusionDesistimientoConjunta())
                : this.habilitarBotonesResolucionIndividual();
        if (!modoCorrecto || Boolean.TRUE.equals(esModoConsulta)) {
            return false;
        }
        boolean hayProvisionales = this.hayProvisionalesResolucion(tramite);
        if (hayProvisionales) {
            return false;
        }
        try {
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            List<ResolucionExclusionDesistimientoDTO> expedientes =
                    this.obtenerExpedientesResolucionDefinitiva(tramite);
            for (ResolucionExclusionDesistimientoDTO elemento : expedientes) {
                Boolean existePeticionActiva = this.documentoPtfrService
                        .consultarDocumentoPortafirmaPorTramite(elemento.getIdExpediente(), idTramite);
                if (Boolean.FALSE.equals(existePeticionActiva)) {
                    return true;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
    
    /** Bloquea el trámite correspondiente y ambos estados en la resolución conjunta. */
    private boolean hayProvisionalesResolucion(Integer tramite) {
        boolean hayExcluidos = this.isHayExcluidosProvisionales();
        boolean hayDesistidos = this.isHayDesistidosProvisionales();
        if (this.errorConsultaProvisionalesResolucion) {
            return true;
        }
        if (Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)) {
            return hayExcluidos || hayDesistidos;
        }
        return Constantes.Tramite.EXCLUSION.equals(tramite) ? hayExcluidos : hayDesistidos;
    }

    /** Indica si se puede subir la resolución individual de exclusión. */
    public boolean habilitarBotonSubirResolucionExclusion(Boolean esModoConsulta) {
        return this.habilitarBotonSubirResolucionDefinitiva(Constantes.Tramite.EXCLUSION,
                esModoConsulta);
    }

    /** Indica si se puede descargar la resolución individual de exclusión. */
    public boolean habilitarBotonDescargarResolucionExclusion() {
        return this.habilitarBotonesResolucionIndividual()
                && this.existeResolucionDefinitiva(Constantes.Tramite.EXCLUSION);
    }

    /** Indica si se puede subir la resolución individual de desistimiento. */
    public boolean habilitarBotonSubirResolucionDesistimiento(Boolean esModoConsulta) {
        return this.habilitarBotonSubirResolucionDefinitiva(Constantes.Tramite.DESISTIMIENTO,
                esModoConsulta);
    }

    /** Indica si se puede descargar la resolución individual de desistimiento. */
    public boolean habilitarBotonDescargarResolucionDesistimiento() {
        return this.habilitarBotonesResolucionIndividual()
                && this.existeResolucionDefinitiva(Constantes.Tramite.DESISTIMIENTO);
    }
    
    /** Envía a Portafirmas la resolución individual de desistimiento. */
    public void enviarResolucionDesistimientoPortafirmas() {
        this.enviarResolucionDefinitivaPortafirmas(Constantes.Tramite.DESISTIMIENTO);
    }

    /** 
     * Indica si se puede enviar una resolución individual o conjunta a Portafirmas. 
     */
    public boolean habilitarEnviarResolucionDefinitivaPortafirmas(Integer tramite,
            Boolean esModoConsulta) {
        boolean modoCorrecto;
        if (Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)) {
            modoCorrecto = Boolean.TRUE.equals(this.getResolucionExclusionDesistimientoConjunta());
        } else {
            modoCorrecto = this.habilitarBotonesResolucionIndividual();
        }
        if (!modoCorrecto || Boolean.TRUE.equals(esModoConsulta)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return false;
        }
        try {
            List<ResolucionExclusionDesistimientoDTO> expedientes =
                    this.obtenerExpedientesResolucionDefinitiva(tramite);
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            for (ResolucionExclusionDesistimientoDTO expediente : expedientes) {
                Integer idExpediente = expediente.getIdExpediente();
                ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite,
                        idExpediente);
                if (documento == null || documento.getCdFica() == null) {
                    continue;
                }
                Boolean existePeticionActiva = this.documentoPtfrService
                        .consultarDocumentoPortafirmaPorTramite(idExpediente, idTramite);
                if (Boolean.FALSE.equals(existePeticionActiva)) {
                    return true;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * Encola las resoluciones de concesión pendientes y agrupa las peticiones creadas.
     */
    private void enviarResolucionConcesionPortafirmasValidada() {
        try {
            Integer tramite = Constantes.Tramite.CONCESION_DEFINITIVA;
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            int insertados = 0;
            Integer idDocPtfrPadre = 0;
            boolean error = false;
            for (ResolucionConcesionDTO elemento
                    : this.obtenerExpedientesPendientesResolucionConcesion()) {
                Integer idExpediente = elemento.getIdExpediente();
                try {
                    if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(
                            idExpediente, idTramite)) {
                        continue;
                    }
                    ExpedienteDocDTO documento = this.getDocumentoResolucionConcesion(idExpediente);
                    if (documento == null || documento.getCdFica() == null) {
                        error = true;
                        continue;
                    }
                    FicaFichero fichero = this.ficaService.recuperar(documento.getCdFica());
                    Integer idDocumentoPtfr;
                    idDocumentoPtfr = this.documentoPtfrService.enviarResolucionConcesionDefinitiva(
                            this.convocatoriaDTO, documento.getIdExpedienteDoc(),
                            fichero.getDatosFichero(), idDocPtfrPadre,
                            UserUtils.getUserName().toUpperCase());
                    if (idDocumentoPtfr != null) {
                        if (idDocPtfrPadre == 0) {
                            idDocPtfrPadre = idDocumentoPtfr;
                        }
                        insertados++;
                    }
                } catch (ServiceException e) {
                    log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
                    error = true;
                }
            }
            if (insertados > 0 && !error) {
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            } else if (error) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
        }
    }

    public boolean habilitarEnviarResolucionExclusionPortafirmas(Boolean esModoConsulta) {
        return this.habilitarEnviarResolucionDefinitivaPortafirmas(Constantes.Tramite.EXCLUSION,
                esModoConsulta);
    }

    /** Envía a Portafirmas la resolución individual de exclusión. */
    public void enviarResolucionExclusionPortafirmas() {
        this.enviarResolucionDefinitivaPortafirmas(Constantes.Tramite.EXCLUSION);
    }

    public boolean habilitarEnviarResolucionDesistimientoPortafirmas(Boolean esModoConsulta) {
        return this.habilitarEnviarResolucionDefinitivaPortafirmas(Constantes.Tramite.DESISTIMIENTO,
                esModoConsulta);
    }

    private void enviarResolucionDefinitivaPortafirmas(Integer tramite) {
        try {
            this.validarModoResolucionSeleccionado(tramite);
            if (!this.habilitarEnviarResolucionDefinitivaPortafirmas(tramite,
                    this.convocatoriasBean.isModoConsulta())) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            int insertados = 0;
            Integer idDocPtfrPadre = 0;
            boolean error = false;
            for (ResolucionExclusionDesistimientoDTO elemento
                    : this.obtenerExpedientesResolucionDefinitiva(tramite)) {
                Integer idExpediente = elemento.getIdExpediente();
                try {
                    if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(
                            idExpediente, idTramite)) {
                        continue;
                    }
                    ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite,
                            idExpediente);
                    if (documento == null || documento.getCdFica() == null) {
                        error = true;
                        continue;
                    }
                    FicaFichero fichero = this.ficaService.recuperar(documento.getCdFica());
                    Integer idDocumentoPtfr;
                    if (Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)) {
                        idDocumentoPtfr = this.documentoPtfrService.enviarResolucionConjuntaDefinitiva(
                                this.convocatoriaDTO, documento.getIdExpedienteDoc(),
                                fichero.getDatosFichero(), idDocPtfrPadre,
                                UserUtils.getUserName().toUpperCase());
                    } else {
                        idDocumentoPtfr = this.documentoPtfrService.enviarResolucionIndividualDefinitiva(
                                this.convocatoriaDTO, documento.getIdExpedienteDoc(), tramite,
                                fichero.getDatosFichero(), idDocPtfrPadre,
                                UserUtils.getUserName().toUpperCase());
                    }
                    if (idDocumentoPtfr != null) {
                        if (idDocPtfrPadre == 0) {
                            idDocPtfrPadre = idDocumentoPtfr;
                        }
                        insertados++;
                    }
                } catch (ServiceException e) {
                    log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
                    error = true;
                }
            }
            if (insertados > 0 && !error) {
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            } else if (error) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA, e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
        } finally {
            // También tras un fallo parcial: el modo depende de lo que haya quedado registrado.
            this.idConvocatoriaEnviosConsultados = null;
        }
    }

    /**
     * Reconsulta antes de actuar para no aceptar un modo incompatible desde una vista antigua.
     */
    private void validarModoResolucionSeleccionado(Integer tramite) throws ServiceException {
        if (Constantes.Tramite.CONCESION_DEFINITIVA.equals(tramite)) {
            if (Boolean.FALSE.equals(this.permitirModificarResolucionConcesion())) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            return;
        }
        if ((Boolean.FALSE.equals(Constantes.Tramite.EXCLUSION.equals(tramite))
                && Boolean.FALSE.equals(Constantes.Tramite.DESISTIMIENTO.equals(tramite))
                && Boolean.FALSE.equals(Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)))
                || Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        this.idConvocatoriaEnviosConsultados = null;
        Boolean conjunta = this.getResolucionExclusionDesistimientoConjunta();
        boolean envioConjunto = Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO
                .equals(tramite);
        if (conjunta == null || conjunta.booleanValue() != envioConjunto) {
            throw new ServiceException(
                    "El modo de resolución no coincide con la selección o con los envíos registrados en la convocatoria.");
        }
    }
    
    /**
     * Obtiene los concedidos definitivos aceptados sin petición activa del trámite.
     */
    private List<ResolucionConcesionDTO> obtenerExpedientesPendientesResolucionConcesion()
            throws ServiceException {
        List<ResolucionConcesionDTO> resultado = new ArrayList<>();
        if (this.idConvocatoria == null) {
            return resultado;
        }
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO);
        Integer idTramite = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.CONCESION_DEFINITIVA);
        if (idEstado == null || idTramite == null) {
            return resultado;
        }
        for (ResolucionConcesionDTO expediente : this.expedienteDAO
                .listaResolucionConcesion(this.idConvocatoria, Collections.singletonList(idEstado))) {
            if (Boolean.FALSE.equals(this.documentoPtfrService
                    .consultarDocumentoPortafirmaPorTramite(expediente.getIdExpediente(), idTramite))) {
                resultado.add(expediente);
            }
        }
        return resultado;
    }

    private List<ResolucionExclusionDesistimientoDTO> obtenerExpedientesResolucionDefinitiva(
            Integer tramite) {
        if (Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)) {
            List<ResolucionExclusionDesistimientoDTO> expedientes = new ArrayList<>(
                    this.obtenerExpedientesResolucionDefinitiva(Constantes.Tramite.EXCLUSION));
            expedientes.addAll(this.obtenerExpedientesResolucionDefinitiva(
                    Constantes.Tramite.DESISTIMIENTO));
            return this.agruparMotivos(expedientes);
        }
        boolean exclusion = Constantes.Tramite.EXCLUSION.equals(tramite);
        Integer estado = exclusion
                ? Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO
                : Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO;
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);

        Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);
        Integer idEstadoProvisional = this.utilConstantes.getGlobalCatalogosValores()
                .get(exclusion ? Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL
                        : Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);

        List<ResolucionExclusionDesistimientoDTO> expedientes;
        if (exclusion) {
            expedientes = this.expedienteDAO.listaResolucionExclusionDesistimiento(
                    this.idConvocatoria, Collections.singletonList(idEstado),
                    Constantes.Cata.REQUISITOS, idEstadoPendRevisionAleg, idEstadoProvisional);
        } else {
            expedientes = this.expedienteDAO.listaResolucionDesistimiento(
                    this.idConvocatoria, Collections.singletonList(idEstado),
                    idEstadoPendRevisionAleg, idEstadoProvisional);
        }
        return this.agruparMotivos(expedientes);
    }

    /**
     * Indica si se puede subir la resolución conjunta definitiva.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return true cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonSubirResolucionConjunta(Boolean esModoConsulta) {
        return this.habilitarBotonSubirResolucionDefinitiva(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO, esModoConsulta);
    }

    /**
     * Indica si se puede descargar la resolución conjunta definitiva.
     *
     * @return true cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonDescargarResolucionConjunta() {
        return Boolean.TRUE.equals(this.getResolucionExclusionDesistimientoConjunta())
                && this.getCodigoFicaResolucionExclusionDesistimiento() != null;
    }

    /**
     * Indica si se puede enviar la resolución conjunta a Portafirmas.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return true cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonEnviarResolucionConjuntaPortafirmas(Boolean esModoConsulta) {
        return this.habilitarEnviarResolucionDefinitivaPortafirmas(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO, esModoConsulta);
    }


    /**
     * Sube a FICA el PDF sin firmar de la resolución conjunta de exclusión y
     * desistimiento y guarda su referencia documental.
     */
    public void cargarFicheroResolucionExclusionDesistimiento() {
        this.cargarFicheroResolucionDefinitiva(this.archivoResolucionDefinitiva,
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO,
                "dialogSubirResolucionExclusionDesistimiento");
    }
    
    /** Descarga la resolución conjunta de exclusión y desistimiento. */
    public void descargarFicheroResolucionExclusionDesistimiento() {
        this.descargarFicheroResolucionDefinitiva(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO);
    }

    /** Envía a Portafirmas los dos listados con el mismo trámite conjunto. */
    public void enviarResolucionConjuntaPortafirmas() {
        this.enviarResolucionDefinitivaPortafirmas(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO);
    }
    
    /** Sube a FICA la resolución individual de exclusión. */
    public void cargarFicheroResolucionExclusion() {
        this.cargarFicheroResolucionDefinitiva(this.archivoResolucionExclusion,
                Constantes.Tramite.EXCLUSION, "dialogSubirResolucionExclusion");
    }

    /** Sube a FICA la resolución individual de desistimiento. */
    public void cargarFicheroResolucionDesistimiento() {
        this.cargarFicheroResolucionDefinitiva(this.archivoResolucionDesistimiento,
                Constantes.Tramite.DESISTIMIENTO, "dialogSubirResolucionDesistimiento");
    }

    /** Descarga de FICA la resolución individual de exclusión. */
    public void descargarFicheroResolucionExclusion() {
        this.descargarFicheroResolucionDefinitiva(Constantes.Tramite.EXCLUSION);
    }

    /** Descarga de FICA la resolución individual de desistimiento. */
    public void descargarFicheroResolucionDesistimiento() {
        this.descargarFicheroResolucionDefinitiva(Constantes.Tramite.DESISTIMIENTO);
    }
    
    /**
     * Estados incluidos en pantalla, Excel y descarga de la concesión.
     */
    private List<Integer> estadosVisiblesResolucionConcesion() {
        return Collections.singletonList(this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
    }

    /**
     * Asocia el PDF exclusivamente a los expedientes pendientes de concesión.
     */
    private void guardarFicheroResolucionConcesion(UploadedFile archivo) {
        Integer tramite = Constantes.Tramite.CONCESION_DEFINITIVA;
        String dialogo = "dialogSubirResolucionConcesion";
        try {
            if (!this.habilitarBotonSubirResolucionConcesion()) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            if (archivo == null || archivo.getFileName() == null
                    || archivo.getFileName().isEmpty()) {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF(dialogo);
                return;
            }
            Integer tipoDocumento = this.obtenerTipoDocumentoResolucionDefinitiva(tramite);
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            List<ResolucionConcesionDTO> expedientes =
                    this.obtenerExpedientesPendientesResolucionConcesion();
            int guardados = 0;
            byte[] contenido;
            try (InputStream entrada = archivo.getInputstream()) {
                contenido = IOUtils.toByteArray(entrada);
            }
            if (contenido.length == 0) {
                throw new ServiceException("El documento PDF no puede estar vacío.");
            }
            for (ResolucionConcesionDTO elemento : expedientes) {
                Integer idExpediente = elemento.getIdExpediente();
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(
                        idExpediente, idTramite)) {
                    continue;
                }
                ExpedienteDocDTO documento = this.getDocumentoResolucionConcesion(idExpediente);
                if (documento == null) {
                    documento = new ExpedienteDocDTO();
                }
                documento.setExpedienteDTO(this.expedienteService.buscarIdSimple(
                        idExpediente, Boolean.FALSE));
                documento.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(tipoDocumento));
                documento.setTramiteDTO(this.catalogosValoresService.buscarId(idTramite));
                documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                        this.utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoDocumento.REVISION_OK)));
                FicaMetadatos metadatos = new FicaMetadatos();
                metadatos.setNombreFichero(archivo.getFileName());
                metadatos.setFechaFichero(new Date());
                metadatos.setExtension(Constantes.Extensiones.PDF);
                metadatos.setUsuario(UserUtils.getUserName().toUpperCase());
                metadatos.setEncriptar(false);
                documento.setFicaMetadatos(metadatos);
                documento.setContenidoFichero(contenido);
                this.expedienteDocService.guardarResolucionConcesion(this.idConvocatoria, documento,
                        UserUtils.getUserName().toUpperCase());
                guardados++;
            }
            if (guardados == 0) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                return;
            }
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
            this.mostrarMensajeElegirFichero = Boolean.FALSE;
            this.cerrarSubirResolucionConcesionDialog();
        }  catch (ServiceException | IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
        }
    }

    private void cargarFicheroResolucionDefinitiva(UploadedFile archivo, Integer tramite,
            String dialogo) {
        try {
            this.validarSubidaResolucionDefinitiva(tramite);
            if (archivo == null || archivo.getFileName() == null
                    || archivo.getFileName().isEmpty()) {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF(dialogo);
                return;
            }
            Integer tipoDocumento = this.obtenerTipoDocumentoResolucionDefinitiva(tramite);
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            List<ResolucionExclusionDesistimientoDTO> expedientes =
                    this.obtenerExpedientesResolucionDefinitiva(tramite);
            int guardados = 0;
            byte[] contenido;
            try (InputStream entrada = archivo.getInputstream()) {
                contenido = IOUtils.toByteArray(entrada);
            }
            for (ResolucionExclusionDesistimientoDTO elemento : expedientes) {
                Integer idExpediente = elemento.getIdExpediente();
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(
                        idExpediente, idTramite)) {
                    continue;
                }
                ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite,
                        idExpediente);
                if (documento == null) {
                    documento = new ExpedienteDocDTO();
                }
                documento.setExpedienteDTO(this.expedienteService.buscarIdSimple(
                        idExpediente, Boolean.FALSE));
                documento.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(tipoDocumento));
                documento.setTramiteDTO(this.catalogosValoresService.buscarId(idTramite));
                documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                        this.utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoDocumento.REVISION_OK)));
                FicaMetadatos metadatos = new FicaMetadatos();
                metadatos.setNombreFichero(archivo.getFileName());
                metadatos.setFechaFichero(new Date());
                metadatos.setExtension(Constantes.Extensiones.PDF);
                metadatos.setUsuario(UserUtils.getUserName().toUpperCase());
                metadatos.setEncriptar(false);
                documento.setFicaMetadatos(metadatos);
                documento.setContenidoFichero(contenido);
                boolean esSustitucion = documento.getCdFica() != null;
                documento = this.expedienteDocService.subirDocumentoFica(documento);
                if (esSustitucion) {
                    this.ficaService.actualizarMetadatos(documento.getCdFica(),
                            documento.getFicaMetadatos());
                }
                this.expedienteDocService.guardarConHistorico(documento,
                        UserUtils.getUserName().toUpperCase());
                guardados++;
            }
            if (guardados == 0) {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                return;
            }
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
            this.mostrarMensajeElegirFichero = Boolean.FALSE;
            if (Constantes.Tramite.EXCLUSION.equals(tramite)) {
                this.archivoResolucionExclusion = null;
            } else if (Constantes.Tramite.DESISTIMIENTO.equals(tramite)) {
                this.archivoResolucionDesistimiento = null;
            } else if (Constantes.Tramite.CONCESION_DEFINITIVA.equals(tramite)) {
                this.cerrarSubirResolucionConcesionDialog();
            } else {
                this.archivoResolucionDefinitiva = null;
            }
        }  catch (ServiceException | IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * Valida la subida para los expedientes pendientes, conservando los documentos ya enviados.
     */
    private void validarSubidaResolucionDefinitiva(Integer tramite) throws ServiceException {
        if (Constantes.Tramite.CONCESION_DEFINITIVA.equals(tramite)) {
            if (!this.habilitarBotonSubirResolucionConcesion()) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            return;
        }
        // Refresca los envíos para proteger también las subidas desde una vista antigua.
        this.validarModoResolucionSeleccionado(tramite);
        // Reconsulta antes de ejecutar la acción.
        this.idConvocatoriaProvisionalesConsultados = null;
        boolean hayProvisionales = this.hayProvisionalesResolucion(tramite);
        if (hayProvisionales) {
            throw new ServiceException(
                    "No se puede subir la resolución hasta gestionar los expedientes provisionales o comprobar sus estados.");
        }
        if (!this.habilitarBotonSubirResolucionDefinitiva(tramite,
                this.convocatoriasBean.isModoConsulta())) {
            throw new ServiceException(
                    "No hay expedientes elegibles pendientes de envío para subir la resolución.");
        }
    }

    private void descargarFicheroResolucionDefinitiva(Integer tramite) {
        this.descargarDocumentoResolucion(this.getDocumentoResolucionDefinitiva(tramite));
    }

    /**
     * Descarga desde FICA el documento seleccionado por cada resolución.
     */
    private void descargarDocumentoResolucion(ExpedienteDocDTO documento) {
        try {
            if (documento != null && documento.getCdFica() != null) {
                FicaFichero fichero = this.ficaService.recuperar(documento.getCdFica());
                UtilesWeb.openFile(fichero.getDatosFichero(), fichero.getExtension(),
                        FilenameUtils.removeExtension(fichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Obtiene el PDF más reciente de concesión de los concedidos definitivos aceptados.
     */
    private ExpedienteDocDTO getDocumentoResolucionConcesion() {
        try {
            List<AtlasFilter> filtros = this.filtrosDocumentoResolucionConcesion();
            filtros.add(new AtlasFilter("expediente.solicitud.convocatoria.idConvocatoria",
                    this.idConvocatoria, Operador.EQUALS));
            filtros.add(new AtlasFilter("expediente.estado.idValor",
                    this.estadosVisiblesResolucionConcesion(), Operador.IN));
            List<AtlasSort> orden = new ArrayList<>();
            orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, 1, filtros, orden);
            return documentos.isEmpty() ? null : documentos.get(0);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * Obtiene el documento de concesión de un expediente para su subida o envío.
     */
    private ExpedienteDocDTO getDocumentoResolucionConcesion(Integer idExpediente)
            throws ServiceException {
        List<AtlasFilter> filtros = this.filtrosDocumentoResolucionConcesion();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
        List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, 1, filtros, orden);
        return documentos.isEmpty() ? null : documentos.get(0);
    }
    
    /**
     * Filtra documentos de concesión activos de expedientes, solicitudes y convocatorias vigentes.
     */
    private List<AtlasFilter> filtrosDocumentoResolucionConcesion() {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                this.utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                this.utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.CONCESION_DEFINITIVA), Operador.EQUALS));
        filtros.add(new AtlasFilter("expediente.fcBajaFila", Operador.NULL));
        filtros.add(new AtlasFilter("expediente.solicitud.fcBajaFila", Operador.NULL));
        filtros.add(new AtlasFilter("expediente.solicitud.convocatoria.fcBajaFila", Operador.NULL));
        return filtros;
    }
    
    private boolean existeResolucionDefinitiva(Integer tramite) {
        ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite);
        return documento != null && documento.getCdFica() != null;
    }

    private ExpedienteDocDTO getDocumentoResolucionDefinitiva(Integer tramite) {
        try {
            Integer estadoPropuesta;
            Integer estadoFinal;

            if (Constantes.Tramite.EXCLUSION.equals(tramite)) {
                estadoPropuesta = Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO;
                estadoFinal = Constantes.EstadoExpediente.EXCLUIDO;
            } else {
                estadoPropuesta = Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO;
                estadoFinal = Constantes.EstadoExpediente.DESISTIDO;
            }
            List<AtlasFilter> filtros = this.filtrosDocumentoResolucionDefinitiva(tramite);
            filtros.add(new AtlasFilter("expediente.solicitud.convocatoria.idConvocatoria",
                    this.idConvocatoria, Operador.EQUALS));
            filtros.add(new AtlasFilter("expediente.fcBajaFila", Operador.NULL));
            List<Integer> estados = new ArrayList<>(Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(estadoPropuesta),
                    this.utilConstantes.getGlobalCatalogosValores().get(estadoFinal)));
            if (Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)) {
                estados.add(this.utilConstantes.getGlobalCatalogosValores().get(
                        Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO));
                estados.add(this.utilConstantes.getGlobalCatalogosValores().get(
                        Constantes.EstadoExpediente.EXCLUIDO));
            }
            filtros.add(new AtlasFilter("expediente.estado.idValor", estados, Operador.IN));
            List<AtlasSort> orden = new ArrayList<>();
            orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, 1, filtros, orden);
            return documentos.isEmpty() ? null : documentos.get(0);
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private ExpedienteDocDTO getDocumentoResolucionDefinitiva(Integer tramite,
            Integer idExpediente) throws ServiceException {
        List<AtlasFilter> filtros = this.filtrosDocumentoResolucionDefinitiva(tramite);
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
        List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, 1, filtros, orden);
        return documentos.isEmpty() ? null : documentos.get(0);
    }

    private List<AtlasFilter> filtrosDocumentoResolucionDefinitiva(Integer tramite) {
        Integer tipoDocumento = this.obtenerTipoDocumentoResolucionDefinitiva(tramite);
        Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                tipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        return filtros;
    }
    
    /** Obtiene el tipo documental según se trate de resolución individual o conjunta. */
    private Integer obtenerTipoDocumentoResolucionDefinitiva(Integer tramite) {
        return this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
    }

    /**
     * Obtiene el código FICA de la resolución conjunta de exclusión y desistimiento.
     *
     * @return código FICA del documento o {@code null} si todavía no se ha subido
     */
    public Integer getCodigoFicaResolucionExclusionDesistimiento() {
        ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(
                Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO);
        return documento != null ? documento.getCdFica() : null;
    }
    
    /**
     * Descarga en un único Excel los expedientes excluidos y desistidos de la
     * convocatoria.
     */
    public void descargarExcelResolucionExclusionDesistimiento() {
        try (ByteArrayOutputStream salida = new ByteArrayOutputStream();
                HSSFWorkbook libro = new HSSFWorkbook()) {

            // Recupera todos los estados visibles
            AtlasListAndCount<ResolucionExclusionDesistimientoDTO> resultadoExcluidos =
                    this.obtenerExcluidosDefinitivos(0, -1, null, null);
            AtlasListAndCount<ResolucionExclusionDesistimientoDTO> resultadoDesistidos =
                    this.obtenerDesistidosDefinitivos(0, -1, null, null);
            List<ResolucionExclusionDesistimientoDTO> excluidos = resultadoExcluidos.getResultados();
            List<ResolucionExclusionDesistimientoDTO> desistidos = resultadoDesistidos.getResultados();

            this.crearHojaResolucion(libro, "Expedientes excluidos", excluidos);
            this.crearHojaResolucion(libro, "Expedientes desistidos", desistidos);
            libro.write(salida);
            UtilesWeb.openFile(salida.toByteArray(), Constantes.Extensiones.XLS,
                    "Resolucion_exclusion_desistimiento");
        } catch (IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage(), e);
        }
    }
    
    /**
     * Descarga los expedientes concedidos definitivos aceptados de la convocatoria.
     */
    public void descargarExcelResolucionConcesion() {
        if (this.idConvocatoria == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            return;
        }

        try (ByteArrayOutputStream salida = new ByteArrayOutputStream();
                HSSFWorkbook libro = new HSSFWorkbook()) {
            List<ResolucionConcesionDTO> expedientes = this.expedienteDAO
                    .listaResolucionConcesion(this.idConvocatoria, this.estadosVisiblesResolucionConcesion());

            this.crearHojaResolucionConcesion(libro, expedientes);
            libro.write(salida);
            UtilesWeb.openFile(salida.toByteArray(), Constantes.Extensiones.XLS,
                    "Resolucion_concesion");
        } catch (IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Crea la hoja de expedientes concedidos con las columnas del listado.
     *
     * @param libro libro Excel
     * @param expedientes expedientes que se incluirán
     */
    private void crearHojaResolucionConcesion(HSSFWorkbook libro,
            List<ResolucionConcesionDTO> expedientes) {
        HSSFSheet hoja = libro.createSheet("Resolución de concesión");
        String[] cabeceras = { "Línea", "Nombre o razón social de la empresa",
                "Número de expediente", "Estado del expediente", "NIF" };
        HSSFRow cabecera = hoja.createRow(0);
        for (int columna = 0; columna < cabeceras.length; columna++) {
            cabecera.createCell(columna).setCellValue(cabeceras[columna]);
        }

        int numeroFila = 1;
        for (ResolucionConcesionDTO expediente : expedientes) {
            HSSFRow fila = hoja.createRow(numeroFila++);
            fila.createCell(0).setCellValue(this.valorExcel(expediente.getLinea()));
            fila.createCell(1).setCellValue(this.valorExcel(expediente.getNombreEmpresa()));
            fila.createCell(2).setCellValue(this.valorExcel(expediente.getNumeroExpediente()));
            fila.createCell(3).setCellValue(this.valorExcel(expediente.getEstadoExpediente()));
            fila.createCell(4).setCellValue(this.valorExcel(expediente.getNif()));
        }

        for (int columna = 0; columna < cabeceras.length; columna++) {
            hoja.autoSizeColumn(columna);
        }
    }

    /**
     * Crea una hoja del Excel con las columnas mostradas en pantalla.
     *
     * @param libro libro Excel
     * @param nombreHoja nombre de la hoja
     * @param expedientes expedientes que se incluirán
     */
    private void crearHojaResolucion(HSSFWorkbook libro, String nombreHoja,
            List<ResolucionExclusionDesistimientoDTO> expedientes) {
        HSSFSheet hoja = libro.createSheet(nombreHoja);
        String[] cabeceras = { "Línea", "Nombre empresa", "Expediente", "Estado expediente",
                "NIF", "Código motivo" };
        HSSFRow cabecera = hoja.createRow(0);
        for (int columna = 0; columna < cabeceras.length; columna++) {
            cabecera.createCell(columna).setCellValue(cabeceras[columna]);
        }

        int numeroFila = 1;
        for (ResolucionExclusionDesistimientoDTO expediente : expedientes) {
            HSSFRow fila = hoja.createRow(numeroFila++);
            fila.createCell(0).setCellValue(this.valorExcel(expediente.getLinea()));
            fila.createCell(1).setCellValue(this.valorExcel(expediente.getNombreEmpresa()));
            fila.createCell(2).setCellValue(this.valorExcel(expediente.getNumeroExpediente()));
            fila.createCell(3).setCellValue(this.valorExcel(expediente.getEstadoExpediente()));
            fila.createCell(4).setCellValue(this.valorExcel(expediente.getNif()));
            fila.createCell(5).setCellValue(this.valorExcel(expediente.getCodigoMotivo()));
        }

        for (int columna = 0; columna < cabeceras.length; columna++) {
            hoja.autoSizeColumn(columna);
        }
    }

    /**
     * Devuelve un valor apto para una celda de texto.
     *
     * @param valor valor original
     * @return valor original o cadena vacía si es nulo
     */
    private String valorExcel(String valor) {
        return valor == null ? "" : valor;
    }

    // VMM 27/10/26 MD-3094: pestaña "Comisión de selección" de la resolución de convocatoria.
    /** Resultado que se muestra mientras la Comisión no haya fijado un estado provisional. */
    private static final String COMISION_PENDIENTE_PROPUESTA = "Pendiente de propuesta";

    /**
     * Obtiene el listado de expedientes de la convocatoria para la Comisión de selección, con el
     * orden de prelación y el crédito acumulado ya calculados.
     *
     * @return expedientes en estado de propuesta o pendientes de la convocatoria
     */
    private List<ComisionSeleccionDTO> obtenerListadoComisionSeleccion() {
        if (this.idConvocatoria == null) {
            return new ArrayList<>();
        }

        // Estados del expediente que NO entran en el listado: los que ya se muestran en la subpestaña
        // de Resolución Exclusión/Desistimiento (excluidos: 251, 249, 214; desistidos: 252, 248, 213)
        List<Integer> idsEstadoExcluidos = new ArrayList<>();
        for (Integer estado : Arrays.asList(
                Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO,
                Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
                Constantes.EstadoExpediente.EXCLUIDO,
                Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO,
                Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
                Constantes.EstadoExpediente.DESISTIDO)) {
            idsEstadoExcluidos.add(this.utilConstantes.getGlobalCatalogosValores().get(estado));
        }

        // Estados provisionales que ya implican un resultado de la Comisión
        List<Integer> idsResultadoComision = new ArrayList<>();
        for (Integer estado : Arrays.asList(Constantes.EstadoExpediente.APROBADO_PROVISIONAL,
                Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL,
                Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)) {
            idsResultadoComision.add(this.utilConstantes.getGlobalCatalogosValores().get(estado));
        }

        List<ComisionSeleccionDTO> listado = this.expedienteDAO
                .listaComisionSeleccion(this.idConvocatoria, idsEstadoExcluidos);

        // El DAO ya los devuelve por puntuacion descendente (desempate por idExpediente)
        int orden = 1;
        BigDecimal acumulado = BigDecimal.ZERO;
        for (ComisionSeleccionDTO expediente : listado) {
            expediente.setOrden(orden++);
            if (expediente.getCuantiaPropuesta() != null) {
                acumulado = acumulado.add(BigDecimal.valueOf(expediente.getCuantiaPropuesta()));
            }
            expediente.setCreditoAcumulado(acumulado.doubleValue());
            expediente.setResultadoComision(
                    idsResultadoComision.contains(expediente.getIdEstadoProvisional())
                            ? expediente.getDsEstadoProvisional()
                            : COMISION_PENDIENTE_PROPUESTA);
            expediente.setMotivo("");
        }
        return listado;
    }

    /**
     * Listado paginado de la pestaña "Comisión de selección".
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes
     */
    public AtlasListAndCount<ComisionSeleccionDTO> obtenerComisionSeleccion(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        List<ComisionSeleccionDTO> listado = this.obtenerListadoComisionSeleccion();

        AtlasListAndCount<ComisionSeleccionDTO> resultado = new AtlasListAndCount<>();
        resultado.setTotal(listado.size());
        if (pageSize < 0) {
            resultado.setResultados(listado);
        } else if (index < listado.size()) {
            resultado.setResultados(new ArrayList<>(
                    listado.subList(index, Math.min(index + pageSize, listado.size()))));
        } else {
            resultado.setResultados(new ArrayList<>());
        }
        return resultado;
    }

    /**
     * Cabeceras de un bloque de desglose del Excel: primero el importe de cada partida por
     * anualidad (ANU1: partida 1, 2, 3...; ANU2: ...), después el total de cada anualidad y, por
     * último, el total.
     *
     * @param prefijo prefijo de las cabeceras (SOL o CONC)
     * @param cabeceraTotal cabecera de la última columna (total del bloque)
     * @param importes desglose de importes, con las partidas y anualidades presentes
     * @return cabeceras del bloque
     */
    private List<String> cabecerasDesgloseComision(String prefijo, String cabeceraTotal,
            ComisionImportesDTO importes) {
        List<String> cabeceras = new ArrayList<>();
        for (Integer anualidad : importes.getAnualidades()) {
            for (String partida : importes.getPartidas().values()) {
                cabeceras.add(prefijo + " " + partida + " ANU" + anualidad);
            }
        }
        for (Integer anualidad : importes.getAnualidades()) {
            cabeceras.add(prefijo + " TOTAL ANU" + anualidad);
        }
        cabeceras.add(cabeceraTotal);
        return cabeceras;
    }

    /**
     * Escribe en la fila un bloque de desglose (mismo orden que las cabeceras de
     * {@link #cabecerasDesgloseComision}) y devuelve la siguiente columna libre. Las celdas sin
     * importe se dejan vacías.
     *
     * @param fila fila del expediente
     * @param columnaInicio primera columna del bloque
     * @param estiloImporte estilo numérico de los importes
     * @param importes desglose de importes (partidas y anualidades)
     * @param importesExpediente importes del expediente: partida -> anualidad -> importe
     * @return columna siguiente al bloque
     */
    private int escribirDesgloseComision(HSSFRow fila, int columnaInicio,
            HSSFCellStyle estiloImporte, ComisionImportesDTO importes,
            Map<Integer, Map<Integer, Double>> importesExpediente) {
        int columna = columnaInicio;
        Map<Integer, Double> totalPorAnualidad = new LinkedHashMap<>();
        Double total = null;

        for (Integer anualidad : importes.getAnualidades()) {
            for (Integer idPartida : importes.getPartidas().keySet()) {
                Double importe = importesExpediente == null ? null
                        : importesExpediente.getOrDefault(idPartida, Collections.emptyMap())
                                .get(anualidad);
                if (importe != null) {
                    this.escribirImporte(fila, columna, importe, estiloImporte);
                    totalPorAnualidad.merge(anualidad, importe, Double::sum);
                    total = total == null ? importe : total + importe;
                }
                columna++;
            }
        }
        for (Integer anualidad : importes.getAnualidades()) {
            Double importe = totalPorAnualidad.get(anualidad);
            if (importe != null) {
                this.escribirImporte(fila, columna, importe, estiloImporte);
            }
            columna++;
        }
        if (total != null) {
            this.escribirImporte(fila, columna, total, estiloImporte);
        }
        return columna + 1;
    }

    private void escribirImporte(HSSFRow fila, int columna, Double importe,
            HSSFCellStyle estiloImporte) {
        HSSFCell celda = fila.createCell(columna);
        celda.setCellValue(importe);
        celda.setCellStyle(estiloImporte);
    }

    private HSSFFont fuenteNegrita(HSSFWorkbook libro) {
        HSSFFont fuente = libro.createFont();
        fuente.setBold(true);
        return fuente;
    }

    private HSSFCellStyle estiloCabeceraColor(HSSFWorkbook libro, IndexedColors color) {
        HSSFCellStyle estilo = libro.createCellStyle();
        estilo.setFont(this.fuenteNegrita(libro));
        estilo.setFillForegroundColor(color.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }

    /**
     * VMM 28/10/26 MD-3094: textos de los resultados que puede elegir la Comisión (estados provisionales
     * Concedido 219, Reserva 255 y Desestimado 218), tal como están en el catálogo, para el desplegable
     * de la columna Resultado Comisión del Excel.
     *
     * @return descripciones de los tres estados (vacío si no se pueden obtener o no caben en la lista)
     */
    private String[] obtenerOpcionesResultadoComision() {
        List<String> opciones = new ArrayList<>();
        try {
            for (Integer estado : Arrays.asList(Constantes.EstadoExpediente.APROBADO_PROVISIONAL,
                    Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL,
                    Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)) {
                CatalogosValoresDTO valor = this.catalogosValoresService
                        .buscarId(this.utilConstantes.getGlobalCatalogosValores().get(estado));
                if (valor != null && valor.getDsValor() != null && !valor.getDsValor().isEmpty()) {
                    opciones.add(valor.getDsValor());
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            return new String[0];
        }

        // una lista explícita de un .xls no puede pasar de 255 caracteres en total (con separadores)
        int total = 0;
        for (String opcion : opciones) {
            total += opcion.length() + 1;
        }
        if (total > 255) {
            log.warn("Las opciones de Resultado Comisión no caben en la lista desplegable del Excel ({} caracteres)",
                    total);
            return new String[0];
        }
        return opciones.toArray(new String[0]);
    }

    /**
     * Exporta a Excel los expedientes de la Comisión de selección. Las columnas Resultado Comisión
     * y Motivo / observaciones son las que debe completar la Comisión.
     */
    public void descargarExcelComisionSeleccion() {
        if (this.idConvocatoria == null) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            return;
        }

        try (ByteArrayOutputStream salida = new ByteArrayOutputStream();
                HSSFWorkbook libro = new HSSFWorkbook()) {
            List<ComisionSeleccionDTO> expedientes = this.obtenerListadoComisionSeleccion();

            List<Integer> idsExpediente = new ArrayList<>();
            for (ComisionSeleccionDTO expediente : expedientes) {
                idsExpediente.add(expediente.getIdExpediente());
            }
            ComisionImportesDTO importes = this.expedienteDAO
                    .obtenerImportesComisionSeleccion(idsExpediente);

            HSSFSheet hoja = libro.createSheet("Comisión de selección");

            HSSFCellStyle estiloImporte = libro.createCellStyle();
            estiloImporte.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
            HSSFCellStyle estiloCabecera = libro.createCellStyle();
            estiloCabecera.setFont(this.fuenteNegrita(libro));
            HSSFCellStyle estiloCabeceraSolicitado = this.estiloCabeceraColor(libro,
                    IndexedColors.YELLOW);
            HSSFCellStyle estiloCabeceraConcedido = this.estiloCabeceraColor(libro,
                    IndexedColors.LIGHT_ORANGE);

            // Cabeceras: datos del expediente, desglose de lo solicitado, desglose de lo
            // financiable/concedido y, al final, el crédito acumulado y las columnas de la Comisión
            List<String> cabeceras = new ArrayList<>(Arrays.asList("Línea",
                    "Nombre empresa / solicitante", "Expediente", "NIF", "Puntuación", "Orden"));
            int columnaInicioSolicitado = cabeceras.size();
            cabeceras.addAll(this.cabecerasDesgloseComision("SOL", "TOTAL SOLICITADO", importes));
            int columnaInicioConcedido = cabeceras.size();
            cabeceras.addAll(this.cabecerasDesgloseComision("CONC", "TOTAL CONCEDIDO", importes));
            int columnaFinConcedido = cabeceras.size();
            cabeceras.addAll(Arrays.asList("Ayuda solicitada", "Ayuda concedida", "Crédito acumulado", "Estado",
                    "Resultado Comisión", "Motivo / observaciones"));

            HSSFRow cabecera = hoja.createRow(0);
            for (int columna = 0; columna < cabeceras.size(); columna++) {
                HSSFCell celda = cabecera.createCell(columna);
                celda.setCellValue(cabeceras.get(columna));
                if (columna >= columnaInicioSolicitado && columna < columnaInicioConcedido) {
                    celda.setCellStyle(estiloCabeceraSolicitado);
                } else if (columna >= columnaInicioConcedido && columna < columnaFinConcedido) {
                    celda.setCellStyle(estiloCabeceraConcedido);
                } else {
                    celda.setCellStyle(estiloCabecera);
                }
            }

            int numeroFila = 1;
            for (ComisionSeleccionDTO expediente : expedientes) {
                HSSFRow fila = hoja.createRow(numeroFila++);
                int columna = 0;
                fila.createCell(columna++).setCellValue(this.valorExcel(expediente.getLinea()));
                fila.createCell(columna++).setCellValue(this.valorExcel(expediente.getNombreEmpresa()));
                fila.createCell(columna++).setCellValue(this.valorExcel(expediente.getNumeroExpediente()));
                fila.createCell(columna++).setCellValue(this.valorExcel(expediente.getNif()));
                if (expediente.getPuntuacion() != null) {
                    fila.createCell(columna).setCellValue(expediente.getPuntuacion());
                }
                columna++;
                fila.createCell(columna++).setCellValue(expediente.getOrden());

                columna = this.escribirDesgloseComision(fila, columna, estiloImporte, importes,
                        importes.getSolicitado().get(expediente.getIdExpediente()));
                columna = this.escribirDesgloseComision(fila, columna, estiloImporte, importes,
                        importes.getConcedido().get(expediente.getIdExpediente()));

                if (expediente.getAyudaSolicitada() != null) {
                    HSSFCell celdaAyudaSol = fila.createCell(columna);
                    celdaAyudaSol.setCellValue(expediente.getAyudaSolicitada());
                    celdaAyudaSol.setCellStyle(estiloImporte);
                }
                columna++;
                if (expediente.getCuantiaPropuesta() != null) {
                    HSSFCell celdaAyuda = fila.createCell(columna);
                    celdaAyuda.setCellValue(expediente.getCuantiaPropuesta());
                    celdaAyuda.setCellStyle(estiloImporte);
                }
                columna++;
                HSSFCell celdaCredito = fila.createCell(columna++);
                celdaCredito.setCellValue(expediente.getCreditoAcumulado());
                celdaCredito.setCellStyle(estiloImporte);
                fila.createCell(columna++).setCellValue(this.valorExcel(expediente.getDsEstado()));
                fila.createCell(columna++)
                        .setCellValue(this.valorExcel(expediente.getResultadoComision()));
                fila.createCell(columna).setCellValue(this.valorExcel(expediente.getMotivo()));
            }

            for (int columna = 0; columna < cabeceras.size(); columna++) {
                hoja.autoSizeColumn(columna);
            }

            // Resultado Comisión: desplegable en cada celda con los tres resultados posibles
            String[] opcionesResultado = this.obtenerOpcionesResultadoComision();
            if (opcionesResultado.length > 0 && !expedientes.isEmpty()) {
                int columnaResultado = cabeceras.indexOf("Resultado Comisión");
                DataValidationHelper ayudanteValidacion = hoja.getDataValidationHelper();
                DataValidationConstraint listaResultados = ayudanteValidacion
                        .createExplicitListConstraint(opcionesResultado);
                DataValidation validacion = ayudanteValidacion.createValidation(listaResultados,
                        new CellRangeAddressList(1, expedientes.size(), columnaResultado,
                                columnaResultado));
                validacion.setShowErrorBox(true);
                validacion.createErrorBox("Valor no válido",
                        "Seleccione uno de los resultados de la lista.");
                hoja.addValidationData(validacion);

                // que quepa el texto más largo de la lista
                int caracteres = 0;
                for (String opcion : opcionesResultado) {
                    caracteres = Math.max(caracteres, opcion.length());
                }
                hoja.setColumnWidth(columnaResultado, Math.min(255 * 256,
                        Math.max(hoja.getColumnWidth(columnaResultado), (caracteres + 2) * 256)));
            }

            libro.write(salida);
            UtilesWeb.openFile(salida.toByteArray(), Constantes.Extensiones.XLS,
                    "Expedientes_Comision_seleccion");
        } catch (IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error(e.getMessage(), e);
        }
    }

//    /**
//     * 
//     */
//    public void cargarFicheroDefinitivo() {
//
//        try {
//            Integer tramite = null;
//
//            if (this.archivoResolucionDefinitiva.getFileName() != null) {
//                this.resolucionBean.prepararMetadatosFica(this.archivoResolucionDefinitiva,
//                        Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO, tramite);
//                this.expedienteDocDTO = this.expedienteDocService.subirDocumentoFica(this.expedienteDocDTO);
//                this.resolucionBean.guardarDocumento();
//                this.mostrarMensajeElegirFichero = Boolean.FALSE;
//            } else {
//                this.mostrarMensajeElegirFichero = Boolean.TRUE;
//                PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDefinitiva");
//            }
//
//        } catch (ServiceException e) {
//            AtlasMessageUtils
//                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
//            log.error(e.getMessage());
//        }
//    }
//    
    public void cargarFicheroDefinitivo() {

        try {
            Integer tramite = null;

            if (this.archivoResolucionDefinitiva.getFileName() != null) {
                this.prepararMetadatosFica(this.archivoResolucionDefinitiva,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO), tramite);
                this.expedienteDocDTO = this.expedienteDocService
                        .subirDocumentoFica(this.expedienteDocDTO);
                this.guardarDocumento();

//                this.recuperarValoresVariablesExpediente(this.expedienteBean.getExpedienteDTO());
                this.mostrarMensajeElegirFichero = Boolean.FALSE;
            } else {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDefinitiva");
            }
        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage());
        }
    }
    
    
    /**
     * Obtiene los expedientes concedidos definitivos aceptados de la resolución de concesión.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de concesión
     */
    public AtlasListAndCount<ResolucionConcesionDTO> obtenerExpedientesResolucionConcesion(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ResolucionConcesionDTO> resultado = new AtlasListAndCount<>();
        List<ResolucionConcesionDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            listado = this.expedienteDAO.listaResolucionConcesion(this.idConvocatoria, this.estadosVisiblesResolucionConcesion());
        }

        resultado.setTotal(listado.size());
        if (pageSize < 0) {
            resultado.setResultados(listado);
        } else if (index < listado.size()) {
            resultado.setResultados(new ArrayList<>(
                    listado.subList(index, Math.min(index + pageSize, listado.size()))));
        } else {
            resultado.setResultados(new ArrayList<>());
        }
        return resultado;
    }
    
    
    /**
     * 
     * @return
     */
    public Boolean habilitarBotonDescargarDefinitivo() {

        Boolean habilitar = Boolean.FALSE;

        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO));
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
     * @return
     */
    public Boolean habilitarBotonDescargarProvisional() {

        Boolean habilitar = Boolean.FALSE;

        ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL));
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
     */
    public void descargaFicheroDefinitivo() {

        try {

            ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO));
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
    public void descargaFicheroProvisional() {

        try {

            ExpedienteDocDTO expeDocDTO = getExpedienteDocDTO(
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_PROVISIONAL));
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
     * Obtiene las propuestas definitivas, provisionales y los expedientes excluidos definitivos.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de exclusión
     */
    public AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerExcluidosDefinitivos(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionExclusionDesistimiento(index, pageSize,
                Arrays.asList(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO,
                        Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
                        Constantes.EstadoExpediente.EXCLUIDO));
    }

    /**
     * Obtiene las propuestas definitivas, provisionales y los expedientes desistidos definitivos.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de desistimiento
     */
    public AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerDesistidosDefinitivos(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionDesistimiento(index, pageSize,
                Arrays.asList(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO,
                        Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
                        Constantes.EstadoExpediente.DESISTIDO));
    }

    /**
     * Obtiene y pagina los expedientes de los estados visibles de la convocatoria.
     * Esta selección no se utiliza para subir documentos ni enviar a Portafirmas.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param estados constantes de los estados visibles del expediente
     * @return listado paginado de expedientes
     */
    private AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerResolucionExclusionDesistimiento(
            int index, int pageSize, List<Integer> estados) {
        AtlasListAndCount<ResolucionExclusionDesistimientoDTO> resultado = new AtlasListAndCount<>();
        List<ResolucionExclusionDesistimientoDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            List<Integer> idsEstado = new ArrayList<>();
            for (Integer estado : estados) {
                Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);
                idsEstado.add(idEstado);
            }
            
            Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);

            Integer idEstadoExclProvis = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
            
            
            
            List<ResolucionExclusionDesistimientoDTO> expedientes = this.expedienteDAO
                    .listaResolucionExclusionDesistimiento(this.idConvocatoria,
                            idsEstado, Constantes.Cata.REQUISITOS, idEstadoPendRevisionAleg, idEstadoExclProvis);
            listado = this.agruparMotivos(expedientes);
        }

        resultado.setTotal(listado.size());
        if (pageSize < 0) {
            resultado.setResultados(listado);
        } else if (index < listado.size()) {
            resultado.setResultados(new ArrayList<>(
                    listado.subList(index, Math.min(index + pageSize, listado.size()))));
        } else {
            resultado.setResultados(new ArrayList<>());
        }
        return resultado;
    }
    
    
    private AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerResolucionDesistimiento(
            int index, int pageSize, List<Integer> estados) {
        AtlasListAndCount<ResolucionExclusionDesistimientoDTO> resultado = new AtlasListAndCount<>();
        List<ResolucionExclusionDesistimientoDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            List<Integer> idsEstado = new ArrayList<>();
            for (Integer estado : estados) {
                Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);
                idsEstado.add(idEstado);
            }
            
            Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);

            Integer idEstadoExclProvis = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);
            
            
            List<ResolucionExclusionDesistimientoDTO> expedientes = this.expedienteDAO
                    .listaResolucionDesistimiento(this.idConvocatoria, idsEstado, idEstadoPendRevisionAleg, idEstadoExclProvis);
            listado = this.agruparMotivos(expedientes);
        }

        resultado.setTotal(listado.size());
        if (pageSize < 0) {
            resultado.setResultados(listado);
        } else if (index < listado.size()) {
            resultado.setResultados(new ArrayList<>(
                    listado.subList(index, Math.min(index + pageSize, listado.size()))));
        } else {
            resultado.setResultados(new ArrayList<>());
        }
        return resultado;
    }

    /**
     * Agrupa en una sola fila los distintos requisitos incumplidos por un
     * expediente.
     *
     * @param listado listado con una fila por requisito incumplido
     * @return listado con una fila por expediente
     */
    private List<ResolucionExclusionDesistimientoDTO> agruparMotivos(
            List<ResolucionExclusionDesistimientoDTO> listado) {
        Map<Integer, ResolucionExclusionDesistimientoDTO> expedientes = new LinkedHashMap<>();
        for (ResolucionExclusionDesistimientoDTO elemento : listado) {
            ResolucionExclusionDesistimientoDTO expediente = expedientes.get(elemento.getIdExpediente());
            if (expediente == null) {
                expedientes.put(elemento.getIdExpediente(), elemento);
            } else if (elemento.getCodigoMotivo() != null) {
                if (expediente.getCodigoMotivo() == null) {
                    expediente.setCodigoMotivo(elemento.getCodigoMotivo());
                } else if (!("," + expediente.getCodigoMotivo().replace(", ", ",") + ",")
                        .contains("," + elemento.getCodigoMotivo() + ",")) {
                    expediente.setCodigoMotivo(
                            expediente.getCodigoMotivo() + ", " + elemento.getCodigoMotivo());
                }
            }
        }
        return new ArrayList<>(expedientes.values());
    }
    
    /**
     * Indica si hay exclusiones provisionales o alegaciones de exclusión pendientes.
     */
    public boolean isHayExcluidosProvisionales() {
        this.consultarEstadosProvisionalesResolucion();
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
        return this.estadosProvisionalesResolucion.contains(idEstado);
    }

    /**
     * Consulta los bloqueos por provisionales y alegaciones pendientes una vez por petición.
     */
    private void consultarEstadosProvisionalesResolucion() {
        if (this.idConvocatoria == null
                || this.idConvocatoria.equals(this.idConvocatoriaProvisionalesConsultados)) {
            if (this.estadosProvisionalesResolucion == null) {
                this.estadosProvisionalesResolucion = new ArrayList<>();
            }
            return;
        }
        this.idConvocatoriaProvisionalesConsultados = this.idConvocatoria;
        this.estadosProvisionalesResolucion = new ArrayList<>();
        this.errorConsultaProvisionalesResolucion = false;
        try {
            Integer idExcluidoProvisional = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
            Integer idDesistidoProvisional = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);
            Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);
            List<Integer> idsEstado = Arrays.asList(idExcluidoProvisional, idDesistidoProvisional);
            List<Integer> estados = this.expedienteDAO.obtenerEstadosProvisionalesResolucion(
                    this.idConvocatoria, idsEstado, idEstadoPendRevisionAleg);
            this.estadosProvisionalesResolucion = estados;
        } catch (RuntimeException e) {
            this.errorConsultaProvisionalesResolucion = true;
            log.error("Error consultando los estados provisionales de la convocatoria "
                    + this.idConvocatoria, e);
        }
    }
    
    /**
     * Indica si hay desistimientos provisionales o alegaciones de desistimiento pendientes.
     */
    public boolean isHayDesistidosProvisionales() {
        this.consultarEstadosProvisionalesResolucion();
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL);
        return this.estadosProvisionalesResolucion.contains(idEstado);
    }

    public boolean isErrorConsultaProvisionalesResolucion() {
        this.consultarEstadosProvisionalesResolucion();
        return this.errorConsultaProvisionalesResolucion;
    }
    
    
    
    
    
    
    
    
    
    /**
     * Obtiene las propuestas concedido provisionales
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de exclusión
     */
    public AtlasListAndCount<PropuestaResolucionDTO> obtenerConcedidoProvisional(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionConcedidoProvisional(index, pageSize,
                Arrays.asList(
                        Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL,
                        Constantes.EstadoExpediente.APROBADO_PROVISIONAL)); // 219
    }
    
    /**
     * Obtiene y pagina los expedientes de los estados visibles de la convocatoria.
     * Esta selección no se utiliza para subir documentos ni enviar a Portafirmas.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param estados constantes de los estados visibles del expediente
     * @return listado paginado de expedientes
     */
    private AtlasListAndCount<PropuestaResolucionDTO> obtenerResolucionConcedidoProvisional(
            int index, int pageSize, List<Integer> estados) {
        AtlasListAndCount<PropuestaResolucionDTO> resultado = new AtlasListAndCount<>();
        List<PropuestaResolucionDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            List<Integer> idsEstado = new ArrayList<>();
            for (Integer estado : estados) {
                Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);
                idsEstado.add(idEstado);
            }
            
//            Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
//                    .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);
//
//            Integer idEstadoExclProvis = this.utilConstantes.getGlobalCatalogosValores()
//                    .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO
                    .listaPropuestaResolucion(this.idConvocatoria,
                            idsEstado);
            listado = this.agruparMotivosPropuestaResolucion(expedientes);
        }

        resultado.setTotal(listado.size());
        if (pageSize < 0) {
            resultado.setResultados(listado);
        } else if (index < listado.size()) {
            resultado.setResultados(new ArrayList<>(
                    listado.subList(index, Math.min(index + pageSize, listado.size()))));
        } else {
            resultado.setResultados(new ArrayList<>());
        }
        return resultado;
    }
    
    /**
     * Agrupa en una sola fila los distintos requisitos incumplidos por un
     * expediente.
     *
     * @param listado listado con una fila por requisito incumplido
     * @return listado con una fila por expediente
     */
    private List<PropuestaResolucionDTO> agruparMotivosPropuestaResolucion(
            List<PropuestaResolucionDTO> listado) {
        Map<Integer, PropuestaResolucionDTO> expedientes = new LinkedHashMap<>();
        for (PropuestaResolucionDTO elemento : listado) {
            PropuestaResolucionDTO expediente = expedientes.get(elemento.getIdExpediente());
            if (expediente == null) {
                expedientes.put(elemento.getIdExpediente(), elemento);
            } else if (elemento.getCodigoMotivo() != null) {
                if (expediente.getCodigoMotivo() == null) {
                    expediente.setCodigoMotivo(elemento.getCodigoMotivo());
                } else if (!("," + expediente.getCodigoMotivo().replace(", ", ",") + ",")
                        .contains("," + elemento.getCodigoMotivo() + ",")) {
                    expediente.setCodigoMotivo(
                            expediente.getCodigoMotivo() + ", " + elemento.getCodigoMotivo());
                }
            }
        }
        return new ArrayList<>(expedientes.values());
    }
    
    /**
     * Obtiene las propuestas desistidos provisionales
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de exclusión
     */
    public AtlasListAndCount<PropuestaResolucionDTO> obtenerDesistidoProvisional(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionConcedidoProvisional(index, pageSize,
                Arrays.asList(
                        Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL,
                        Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)); // 218
    }
    
    /**
     * Obtiene las propuestas desistidos provisionales
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes de exclusión
     */
    public AtlasListAndCount<PropuestaResolucionDTO> obtenerReservaProvisional(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionConcedidoProvisional(index, pageSize,
                Arrays.asList(
                        Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL,
                        Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL)); // 255
    }
    
    /**
     * Abre el diálogo de subida de propuesta resolución provisional
     */
    public void subirPropuestaResolucionProvisionalDialog() {
        this.archivoResolucion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirPropuestaResolucionProvisional");
    }
    
    /**
     * Indica si se puede subir la resolución provisional basándose en si existen 
     * expedientes elegibles y si ninguno ha sido enviado ya a Portafirmas.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return true cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonSubirResolucionProvisional(Boolean esModoConsulta) {
        if (this.idConvocatoria == null || Boolean.TRUE.equals(esModoConsulta)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return false;
        }
        
        try {
            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);

            List<Integer> estadosVisibles = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), // 219
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), // 218
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL) // 255
                );
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);
            
            if (expedientes == null || expedientes.isEmpty()) {
                return false;
            }

            // REGLA NUEVA: Si algún expediente ya está en firmas, deshabilitamos la opción de volver a subir archivos
            for (PropuestaResolucionDTO elemento : expedientes) {
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(elemento.getIdExpediente(), idTramiteProv)) {
                    return false; 
                }
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error al comprobar la habilitación del botón de resolución provisional", e);
            return false;
        }
    }


    /**
     * Recupera el documento provisional existente para un expediente concreto,
     * permitiendo localizarlo independientemente de su estado para poder reutilizar su ID.
     */
    private ExpedienteDocDTO getDocumentoResolucionProvisional(Integer idExpediente, Integer idTipoDocumento, Integer idTramite) {
        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO, idTipoDocumento, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
            
            List<AtlasSort> orden = new ArrayList<>();
            orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            
            List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, 1, filtros, orden);
            return documentos.isEmpty() ? null : documentos.get(0);
        } catch (ServiceException e) {
            log.error("Error al buscar documento provisional previo para expediente: " + idExpediente, e);
            return null;
        }
    }
    
    /**
     * Sube a FICA el PDF de la propuesta de resolución provisional y lo guarda 
     * asociándolo a los expedientes pertinentes de la convocatoria.
     */
    public void cargarFicheroResolucionProvisional() {
        try {
            // 1. Recuperamos dinámicamente los ID reales mapeados en BBDD
            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);
            
            Integer idTipoDocProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);

            if (this.archivoResolucion != null && this.archivoResolucion.getFileName() != null 
                    && !this.archivoResolucion.getFileName().isEmpty()) {
                
                // 2. OBTENER TODOS LOS EXPEDIENTES DE LAS 3 LISTAS DE LA PANTALLA
                List<Integer> estadosVisibles = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL)
                );
                
                // Recuperamos en bloque todos los expedientes afectados en la subpestaña
                List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);

                if (expedientes == null || expedientes.isEmpty()) {
                    AtlasMessageUtils.addErrorMessage("No hay expedientes elegibles en ninguna de las listas para asociar la resolución.");
                    return;
                }

                // Extraemos los bytes del archivo binario unificado
                byte[] contenido;
                try (InputStream entrada = this.archivoResolucion.getInputstream()) {
                    contenido = IOUtils.toByteArray(entrada);
                }

                // 3. SUBIDA FÍSICA ÚNICA A FICA (FUERA DEL BUCLE)
                ExpedienteDocDTO docFicaUnico = new ExpedienteDocDTO();
                docFicaUnico.setExpedienteDTO(this.expedienteService.buscarIdSimple(expedientes.get(0).getIdExpediente(), Boolean.FALSE));
                docFicaUnico.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(idTipoDocProv));
                docFicaUnico.setTramiteDTO(this.catalogosValoresService.buscarId(idTramiteProv));
                docFicaUnico.setEstadoDTO(this.catalogosValoresService.buscarId(
                        this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                
                FicaMetadatos metadatosComunes = new FicaMetadatos();
                metadatosComunes.setNombreFichero(this.archivoResolucion.getFileName());
                metadatosComunes.setFechaFichero(new Date());
                metadatosComunes.setExtension(Constantes.Extensiones.PDF);
                metadatosComunes.setUsuario(UserUtils.getUserName().toUpperCase());
                metadatosComunes.setEncriptar(false);
                
                docFicaUnico.setFicaMetadatos(metadatosComunes);
                docFicaUnico.setContenidoFichero(contenido);

                docFicaUnico = this.expedienteDocService.subirDocumentoFica(docFicaUnico);
                Integer nuevoCdFica = docFicaUnico.getCdFica(); 

                int guardados = 0;

                // 4. Iteramos sobre el conjunto total de expedientes de las 3 tablas
                for (PropuestaResolucionDTO elemento : expedientes) {
                    Integer idExpediente = elemento.getIdExpediente();

                    // Buscamos si ya existía registro previo (para segundas subidas/sustitución)
                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(idExpediente, idTipoDocProv, idTramiteProv);
                    
                    boolean esSustitucion = (documento != null && documento.getIdExpedienteDoc() != null);
                    if (!esSustitucion) {
                        documento = new ExpedienteDocDTO();
                    }

                    documento.setExpedienteDTO(this.expedienteService.buscarIdSimple(idExpediente, Boolean.FALSE));
                    documento.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(idTipoDocProv));
                    documento.setTramiteDTO(this.catalogosValoresService.buscarId(idTramiteProv));
                    documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                            this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                    
                    documento.setFicaMetadatos(metadatosComunes);
                    documento.setCdFica(nuevoCdFica); // Vinculamos el FICA único compartido
                    documento.setContenidoFichero(contenido);

                    if (esSustitucion) {
                        this.ficaService.actualizarMetadatos(nuevoCdFica, metadatosComunes);
                    }

                    // Se realiza INSERT la primera vez y UPDATE en sustituciones sucesivas
                    this.expedienteDocService.guardarConHistorico(documento, UserUtils.getUserName().toUpperCase());
                    guardados++;
                }

                if (guardados > 0) {
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
                    this.mostrarMensajeElegirFichero = Boolean.FALSE;
                    this.archivoResolucion = null; 
                    this.cerrarSubirResolucionProvisionalDialog(); 
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                }

            } else {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF("dialogSubirPropuestaResolucionProvisional");
            }
        } catch (Exception e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error("Error al procesar la carga integral de resolución provisional: " + e.getMessage(), e);
        }
    }





    
    /**
     * Comprueba si existe al menos un documento de resolución provisional subido a FICA
     * asociado a los expedientes activos de la pestaña para habilitar el botón de descarga.
     *
     * @return true si el botón de descarga debe estar activo
     */
    public Boolean habilitarDescargarPropuestaProv() {
        if (this.idConvocatoria == null || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return Boolean.FALSE;
        }

        try {
            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);
            Integer idTipoDocProv = this.utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);

            List<Integer> estadosVisibles = Arrays.asList(
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), // 219
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), // 218
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL) // 255
            );
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);

            if (expedientes == null || expedientes.isEmpty()) {
                return Boolean.FALSE;
            }

            for (PropuestaResolucionDTO elemento : expedientes) {
                ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(elemento.getIdExpediente(), idTipoDocProv, idTramiteProv);
                if (documento != null && documento.getCdFica() != null) {
                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            log.error("Error en habilitarDescargarPropuestaProv", e);
        }

        return Boolean.FALSE;
    }

    /**
     * Recupera la resolución provisional común desde FICA tomando como referencia 
     * el primer expediente activo de la subpestaña que disponga del identificador.
     */
    public void descargarFicheroProvisional() {
        try {
            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);
            Integer idTipoDocProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);

            List<Integer> estadosVisibles = Arrays.asList(
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), // 219
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), // 218
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL) // 255
            );
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);
            
            Integer cdFicaDescarga = null;
            
            // Localizamos el primer registro que ya contenga el CD_FICA común
            if (expedientes != null && !expedientes.isEmpty()) {
                for (PropuestaResolucionDTO elemento : expedientes) {
                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(elemento.getIdExpediente(), idTipoDocProv, idTramiteProv);
                    if (documento != null && documento.getCdFica() != null) {
                        cdFicaDescarga = documento.getCdFica();
                        break;
                    }
                }
            }

            if (cdFicaDescarga != null) {
                // Recuperamos el archivo físico desde el repositorio documental
                FicaFichero ficaFichero = this.ficaService.recuperar(cdFicaDescarga);
                
                // Forzamos la descarga del flujo binario en el navegador del usuario
                UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                        FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error("Error al descargar la propuesta de resolución provisional: " + e.getMessage(), e);
        }
    }
    
    /**
     * Encola en el Portafirmas corporativo la propuesta de resolución provisional
     * para todos los expedientes de la subpestaña, agrupando las peticiones creadas.
     */
    /**
     * Encola en el Portafirmas la propuesta de resolución provisional.
     * El primer expediente insertado se establece con id_doc_ptfr_padre = 0,
     * y los siguientes registros guardan el ID del primero en dicha columna.
     */
    public void enviarPropuestaProvPortafirmas() {
        try {
            // Recuperamos el idConvocatoria desde convocatoriasBean si se perdió en la petición request
            if (this.idConvocatoria == null && this.convocatoriasBean != null 
                    && this.convocatoriasBean.getConvocatoriaDTO() != null) {
                this.idConvocatoria = this.convocatoriasBean.getConvocatoriaDTO().getIdConvocatoria();
            }

            if (this.convocatoriaDTO == null && this.idConvocatoria != null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }


            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);
            Integer idTipoDocProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_PROVISIONAL);

            List<Integer> estadosVisibles = Arrays.asList(
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL)
            );
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);

            int insertados = 0;
            Integer idDocPtfrPadre = null; // Guardará el ID real del primer registro de la tabla
            boolean error = false;

            for (PropuestaResolucionDTO elemento : expedientes) {
                Integer idExpediente = elemento.getIdExpediente();
                try {
                    if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(idExpediente, idTramiteProv)) {
                        continue;
                    }

                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(idExpediente, idTipoDocProv, idTramiteProv);
                    if (documento == null || documento.getCdFica() == null) {
                        error = true;
                        continue;
                    }

                    FicaFichero fichero = this.ficaService.recuperar(documento.getCdFica());
                    
                    // 1. Guardar envío en documento PTFR (vuelve a devolver true/false)
                    Boolean insertadoCorrectamente = this.documentoPtfrService.guardarEnvioEnDcoumentoPTFR(
                            this.convocatoriaDTO,
                            this.expedienteService.buscarIdSimple(idExpediente, Boolean.FALSE),
                            idExpediente,
                            idTramiteProv,
                            fichero.getDatosFichero(),
                            null, null, null
                    );

                    if (Boolean.TRUE.equals(insertadoCorrectamente)) {
                        // 2. Corregimos el filtro usando la propiedad directa sin el prefijo de anidamiento
                        List<AtlasFilter> filtrosPtfr = new ArrayList<>();
                        filtrosPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        
                        // Cambiado a la propiedad plana de la entidad para solucionar el IllegalArgumentException
                        filtrosPtfr.add(new AtlasFilter("idExpediente", idExpediente, Operador.EQUALS));
                        filtrosPtfr.add(new AtlasFilter("tramite.idValor", idTramiteProv, Operador.EQUALS));
                        
                        List<AtlasSort> ordenPtfr = new ArrayList<>();
                        ordenPtfr.add(new AtlasSort("idDocumentoPtfr", false)); // El último insertado primero
                        
                        List<DocumentoPtfrDTO> listaPtfr = this.documentoPtfrService.buscar(0, 1, filtrosPtfr, ordenPtfr);
                        
                        if (listaPtfr != null && !listaPtfr.isEmpty()) {
                            DocumentoPtfrDTO registroCreado = listaPtfr.get(0);
                            
                            if (idDocPtfrPadre == null) {
                                // Es el primer registro procesado con éxito: su padre es 0
                                idDocPtfrPadre = registroCreado.getIdDocumentoPtfr();
                                registroCreado.setIdDocPtfrPadre(0);
                                this.documentoPtfrService.guardar(registroCreado); 
                            } else {
                                // Es un hijo: le asociamos el ID del primero
                                registroCreado.setIdDocPtfrPadre(idDocPtfrPadre);
                                this.documentoPtfrService.guardar(registroCreado);
                            }

                            // -----------------------------------------------------------------
                            // VINCULAR EL ID_DOCUMENTO_PTFR EN EL EXPEDIENTE_DOC CORRESPONDIENTE
                            // -----------------------------------------------------------------
                            documento.setIdDocumentoPtfr(registroCreado.getIdDocumentoPtfr());
                        }

                        // Actualizamos el estado y guardamos el idDocumentoPtfr en saii_expediente_doc
                        documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(documento, UserUtils.getUserName().toUpperCase());

                        // -----------------------------------------------------------------
                        // ACTUALIZAR ID_ESTADO E ID_ESTADO_PROVISIONAL DEL EXPEDIENTE
                        // -----------------------------------------------------------------
                        ExpedienteDTO expActualizar = this.expedienteService.buscarIdSimple(idExpediente, Boolean.TRUE);
                        if (expActualizar != null && expActualizar.getEstadoDTO() != null) {
                            Integer idEstadoActual = expActualizar.getEstadoDTO().getIdValor();
                            
                            Integer idPendConcedido = this.utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL);
                            Integer idPendDesestimado = this.utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL);
                            Integer idPendReserva = this.utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL);

                            Integer constanteNuevoEstado = null;
                            if (idPendConcedido.equals(idEstadoActual)) {
                                constanteNuevoEstado = Constantes.EstadoExpediente.APROBADO_PROVISIONAL; // Mapea a 219
                            } else if (idPendDesestimado.equals(idEstadoActual)) {
                                constanteNuevoEstado = Constantes.EstadoExpediente.DENEGADO_PROVISIONAL; // Mapea a 218
                            } else if (idPendReserva.equals(idEstadoActual)) {
                                constanteNuevoEstado = Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL; // Mapea a 255
                            }

                            if (constanteNuevoEstado != null) {
                                Integer idNuevoEstado = this.utilConstantes.getGlobalCatalogosValores().get(constanteNuevoEstado);
                                CatalogosValoresDTO nuevoEstadoDTO = this.catalogosValoresService.buscarId(idNuevoEstado);
                                
                                expActualizar.setEstadoDTO(nuevoEstadoDTO);
                                expActualizar.setEstadoProvisionalDTO(nuevoEstadoDTO);
                                this.expedienteService.guardar(expActualizar);
                            }
                        }
                        
                        insertados++;


                    }

                } catch (ServiceException e) {
                    log.error("Error al enviar a portafirmas el expediente " + idExpediente, e);
                    error = true;
                }
            }

            if (insertados > 0 && !error) {
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            } else if (error) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        } catch (Exception e) {
            log.error("Error general en el envío en bloque a Portafirmas corporativo", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
        }
    }

    /**
     * Comprueba de forma independiente si ya existen registros en saii_documento_ptfr 
     * para los expedientes de la subpestaña con el fin de forzar el deshabilitado del botón.
     *
     * @return true si ya hay envíos realizados y el botón debe deshabilitarse
     */
    public boolean bloquearBotonPorEnvioExistentePtfr() {
        if (this.idConvocatoria == null) {
            return true; // Si no hay convocatoria, se bloquea por seguridad
        }

        try {
            Integer idTramiteProv = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL);

            // Recuperamos los expedientes de las 3 listas visibles en la pantalla
            List<Integer> estadosVisibles = Arrays.asList(
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_CONCEDIDO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_DESESTIMADO_PROVISIONAL),
                this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROPUESTA_RESOLUCION_RESERVA_PROVISIONAL)
            );
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);

            if (expedientes == null || expedientes.isEmpty()) {
                return true; // Si no hay expedientes en pantalla, el botón no debe pulsarse
            }

            // Si detectamos que tan solo un expediente ya fue enviado a portafirmas, devolvemos true para deshabilitar
            for (PropuestaResolucionDTO elemento : expedientes) {
                // Si tu arquitectura maneja EntityManager de JPA directo, un this.documentoPtfrService.refresh() o limpiar caché evita lecturas sucias
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(elemento.getIdExpediente(), idTramiteProv)) {
                    return true; 
                }
            }
        } catch (Exception e) {
            log.error("Error en la comprobación independiente de bloqueo del botón Portafirmas", e);
            return true;
        }

        return false; // No hay ningún envío previo, se puede habilitar si cumple el resto de condiciones
    }

    
    public void subirPropuestaResolucionDefinitivaDialog() {
        this.mostrarMensajeElegirFicheroPropDef = false;
        this.archivoPropuestaDefinitiva = null;
        PrimefacesUtils.mostrarPopupPF("dialogSubirPropuestaResolucionDefinitiva");
    }

    public void cerrarSubirPropuestaDefinitivaDialog() {
        this.mostrarMensajeElegirFicheroPropDef = false;
        this.archivoPropuestaDefinitiva = null;
        PrimefacesUtils.ocultarPopupPF("dialogSubirPropuestaResolucionDefinitiva");
    }

    // =========================================================================
    // 1. CARGA DE LAS DOS LISTAS DE LA SUBPESTAÑA (253 Y 254)
    // =========================================================================
    public AtlasListAndCount<PropuestaResolucionDTO> obtenerConcedidoDefinitivo(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionConcedidoProvisional(index, pageSize,
                Arrays.asList(
                        Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO, // 253
                        Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO          // 246
                ));
    }

    public AtlasListAndCount<PropuestaResolucionDTO> obtenerDenegadoDefinitivo(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionConcedidoProvisional(index, pageSize,
                Arrays.asList(
                        Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO, // 254
                        Constantes.EstadoExpediente.DENEGADO_DEFINITIVO
                ));
    }
    
 // =========================================================================
    // 2. SUBIDA Y REEMPLAZO DE FICA UNIFICADO (TRÁMITE 1918)
    // =========================================================================
    public void cargarFicheroPropuestaDefinitiva() {
        try {
            // Trámite 1918 = Resolución Convocatoria Definitiva
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            // Ajusta la constante del tipo de documento correspondiente a Propuesta Resolución Definitiva
            Integer idTipoDocDef = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);

            if (this.archivoPropuestaDefinitiva != null && this.archivoPropuestaDefinitiva.getFileName() != null
                    && !this.archivoPropuestaDefinitiva.getFileName().isEmpty()) {

                List<Integer> estadosPendientes = Arrays.asList(
                        this.utilConstantes.getGlobalCatalogosValores().get(253),
                        this.utilConstantes.getGlobalCatalogosValores().get(254)
                );

                List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosPendientes);

                if (expedientes == null || expedientes.isEmpty()) {
                    AtlasMessageUtils.addErrorMessage("No hay expedientes elegibles para asociar esta propuesta definitiva.");
                    return;
                }

                byte[] contenido;
                try (InputStream entrada = this.archivoPropuestaDefinitiva.getInputstream()) {
                    contenido = IOUtils.toByteArray(entrada);
                }

                // Subida física única a FICA fuera del bucle
                ExpedienteDocDTO docFicaUnico = new ExpedienteDocDTO();
                docFicaUnico.setExpedienteDTO(this.expedienteService.buscarIdSimple(expedientes.get(0).getIdExpediente(), Boolean.FALSE));
                docFicaUnico.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(idTipoDocDef));
                docFicaUnico.setTramiteDTO(this.catalogosValoresService.buscarId(idTramiteDef));
                docFicaUnico.setEstadoDTO(this.catalogosValoresService.buscarId(
                        this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));

                FicaMetadatos metadatosComunes = new FicaMetadatos();
                metadatosComunes.setNombreFichero(this.archivoPropuestaDefinitiva.getFileName());
                metadatosComunes.setFechaFichero(new Date());
                metadatosComunes.setExtension(Constantes.Extensiones.PDF);
                metadatosComunes.setUsuario(UserUtils.getUserName().toUpperCase());
                metadatosComunes.setEncriptar(false);

                docFicaUnico.setFicaMetadatos(metadatosComunes);
                docFicaUnico.setContenidoFichero(contenido);

                docFicaUnico = this.expedienteDocService.subirDocumentoFica(docFicaUnico);
                Integer nuevoCdFica = docFicaUnico.getCdFica();

                int guardados = 0;

                for (PropuestaResolucionDTO elemento : expedientes) {
                    Integer idExpediente = elemento.getIdExpediente();

                    // Reutilizamos tu buscador genérico por expediente, tipoDoc y trámite
                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(idExpediente, idTipoDocDef, idTramiteDef);

                    boolean esSustitucion = (documento != null && documento.getIdExpedienteDoc() != null);
                    if (!esSustitucion) {
                        documento = new ExpedienteDocDTO();
                    }

                    documento.setExpedienteDTO(this.expedienteService.buscarIdSimple(idExpediente, Boolean.FALSE));
                    documento.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(idTipoDocDef));
                    documento.setTramiteDTO(this.catalogosValoresService.buscarId(idTramiteDef));
                    documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                            this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));

                    documento.setFicaMetadatos(metadatosComunes);
                    documento.setCdFica(nuevoCdFica);
                    documento.setContenidoFichero(contenido);

                    if (esSustitucion) {
                        this.ficaService.actualizarMetadatos(nuevoCdFica, metadatosComunes);
                    }

                    this.expedienteDocService.guardarConHistorico(documento, UserUtils.getUserName().toUpperCase());
                    guardados++;
                }

                if (guardados > 0) {
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
                    this.mostrarMensajeElegirFicheroPropDef = false;
                    this.archivoPropuestaDefinitiva = null;
                    this.cerrarSubirResolucionDefinitivaDialog();
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
                }
            } else {
                this.mostrarMensajeElegirFicheroPropDef = true;
                PrimefacesUtils.mostrarPopupPF("dialogSubirPropuestaResolucionDefinitiva");
            }
        } catch (Exception e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error("Error al subir la propuesta de resolución definitiva: " + e.getMessage(), e);
        }
    }

    // =========================================================================
    // 3. DESCARGA DEL DOCUMENTO DEFINITIVO
    // =========================================================================
    public void descargarFicheroPropuestaDefinitiva() {
        try {
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            Integer idTipoDocDef = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);

            List<Integer> estadosVisibles = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(253),
                    this.utilConstantes.getGlobalCatalogosValores().get(254),
                    this.utilConstantes.getGlobalCatalogosValores().get(246),
                    this.utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));

            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO
                    .listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);
            Integer cdFicaDescarga = null;

            if (expedientes != null && !expedientes.isEmpty()) {
                for (PropuestaResolucionDTO elemento : expedientes) {
                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(
                            elemento.getIdExpediente(), idTipoDocDef, idTramiteDef);
                    if (documento != null && documento.getCdFica() != null) {
                        cdFicaDescarga = documento.getCdFica();
                        break;
                    }
                }
            }

            if (cdFicaDescarga != null) {
                FicaFichero ficaFichero = this.ficaService.recuperar(cdFicaDescarga);
                UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                        FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            }
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
            log.error("Error al descargar la propuesta definitiva: " + e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // 4. ENVÍO A PORTAFIRMAS Y ACTUALIZACIÓN A ESTADO 246
    // =========================================================================
    public void enviarPropuestaDefPortafirmas(Integer idConvocatoriaParam) {
        try {
            if (this.idConvocatoria == null && idConvocatoriaParam != null) {
                this.idConvocatoria = idConvocatoriaParam;
            }
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            Integer idTipoDocDef = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            List<Integer> estadosPendientes = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO));
            
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria,
                    estadosPendientes);

            int insertados = 0;
            Integer idDocPtfrPadre = null;
            boolean error = false;
            for (PropuestaResolucionDTO elemento : expedientes) {
                Integer idExpediente = elemento.getIdExpediente();
                try {
                    if (this.documentoPtfrService
                            .consultarDocumentoPortafirmaPorTramite(idExpediente, idTramiteDef)) {
                        continue;
                    }
                    ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(
                            idExpediente, idTipoDocDef, idTramiteDef);
                    if (documento == null || documento.getCdFica() == null) {
                        error = true;
                        continue;
                    }
                    FicaFichero fichero = this.ficaService.recuperar(documento.getCdFica());
                    Boolean insertadoCorrectamente = this.documentoPtfrService
                            .guardarEnvioEnDcoumentoPTFR(this.convocatoriaDTO,
                                    this.expedienteService.buscarIdSimple(idExpediente,
                                            Boolean.FALSE),
                                    idExpediente, idTramiteDef, fichero.getDatosFichero(), null,
                                    null, null);
                    if (Boolean.TRUE.equals(insertadoCorrectamente)) {
                        List<AtlasFilter> filtrosPtfr = new ArrayList<>();
                        filtrosPtfr.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        filtrosPtfr.add(new AtlasFilter("idExpediente", idExpediente, Operador.EQUALS));
                        filtrosPtfr.add(new AtlasFilter("tramite.idValor", idTramiteDef, Operador.EQUALS));

                        List<AtlasSort> ordenPtfr = new ArrayList<>();
                        ordenPtfr.add(new AtlasSort("idDocumentoPtfr", false));

                        List<DocumentoPtfrDTO> listaPtfr = this.documentoPtfrService.buscar(0, 1, filtrosPtfr, ordenPtfr);

                        if (listaPtfr != null && !listaPtfr.isEmpty()) {
                            DocumentoPtfrDTO registroCreado = listaPtfr.get(0);
                            if (idDocPtfrPadre == null) {
                                idDocPtfrPadre = registroCreado.getIdDocumentoPtfr();
                                registroCreado.setIdDocPtfrPadre(0);
                                this.documentoPtfrService.guardar(registroCreado);
                            } else {
                                registroCreado.setIdDocPtfrPadre(idDocPtfrPadre);
                                this.documentoPtfrService.guardar(registroCreado);
                            }
                            documento.setIdDocumentoPtfr(registroCreado.getIdDocumentoPtfr());
                        }
                        documento.setEstadoDTO(this.catalogosValoresService
                                .buscarId(this.utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                        this.expedienteDocService.guardarConHistorico(documento,
                                UserUtils.getUserName().toUpperCase());
                        // Actualización de estados del expediente (253 -> 246,
                        // 254 -> Denegado definitivo)
                        ExpedienteDTO expActualizar = this.expedienteService
                                .buscarIdSimple(idExpediente, Boolean.TRUE);
                        if (expActualizar != null && expActualizar.getEstadoDTO() != null) {
                            Integer idEstadoActual = expActualizar.getEstadoDTO().getIdValor();
                            Integer idPropConcedidoDef = this.utilConstantes
                                    .getGlobalCatalogosValores().get(253);
                            Integer idPropDenegadoDef = this.utilConstantes
                                    .getGlobalCatalogosValores().get(254);
                            Integer idNuevoEstado = null;
                            if (idPropConcedidoDef != null
                                    && idPropConcedidoDef.equals(idEstadoActual)) {
                                idNuevoEstado = this.utilConstantes.getGlobalCatalogosValores()
                                        .get(246); // Concedido definitivo
                            } else if (idPropDenegadoDef != null
                                    && idPropDenegadoDef.equals(idEstadoActual)) {
                                idNuevoEstado = this.utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO);
                            }
                            if (idNuevoEstado != null) {
                                CatalogosValoresDTO nuevoEstadoDTO = this.catalogosValoresService
                                        .buscarId(idNuevoEstado);
                                if (nuevoEstadoDTO != null) {
                                    expActualizar.setEstadoDTO(nuevoEstadoDTO);
                                    expActualizar.setEstadoProvisionalDTO(nuevoEstadoDTO);
                                    this.expedienteService.guardar(expActualizar);
                                }
                            }
                        }
                        insertados++;
                    }
                } catch (ServiceException e) {
                    log.error("Error al enviar a portafirmas definitivo el expediente "
                            + idExpediente, e);
                    error = true;
                }
            }
            if (insertados > 0 && !error) {
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            } else if (error) {
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
            }
        } catch (Exception e) {
            log.error("Error general en el envío definitivo a Portafirmas", e);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_PORTAFIRMA);
        }
    }
    
    // =========================================================================
    // 5. MÉTODOS DE HABILITACIÓN / BLOQUEO DE BOTONES
    // =========================================================================
    public boolean habilitarBotonSubirPropuestaDefinitiva(Boolean esModoConsulta) {
        Integer idConv = this.obtenerIdConvocatoriaActual();
        if (idConv == null || Boolean.TRUE.equals(esModoConsulta)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return false;
        }
        try {
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            List<Integer> estadosPendientes = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO)
            );
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosPendientes);
            if (expedientes == null || expedientes.isEmpty()) {
                return false;
            }
            for (PropuestaResolucionDTO elemento : expedientes) {
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(elemento.getIdExpediente(), idTramiteDef)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Error en habilitarBotonSubirPropuestaDefinitiva", e);
            return false;
        }
    }
    
    private Integer obtenerIdConvocatoriaActual() {
        if (this.idConvocatoria != null) {
            return this.idConvocatoria;
        }
        if (this.convocatoriasBean != null && this.convocatoriasBean.getConvocatoriaDTO() != null) {
            this.idConvocatoria = this.convocatoriasBean.getConvocatoriaDTO().getIdConvocatoria();
        }
        return this.idConvocatoria;
    }


    public Boolean habilitarDescargarPropuestaDefinitiva() {
        if (this.idConvocatoria == null || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return Boolean.FALSE;
        }
        try {
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            Integer idTipoDocDef = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            List<Integer> estadosVisibles = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO)
            );
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosVisibles);
            if (expedientes == null || expedientes.isEmpty()) {
                return Boolean.FALSE;
            }
            for (PropuestaResolucionDTO elemento : expedientes) {
                ExpedienteDocDTO documento = this.getDocumentoResolucionProvisional(elemento.getIdExpediente(), idTipoDocDef, idTramiteDef);
                if (documento != null && documento.getCdFica() != null) {
                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            log.error("Error en habilitarDescargarPropuestaDefinitiva", e);
        }
        return Boolean.FALSE;
    }

    public boolean habilitarEnviarPropuestaDefPortafirmas(Boolean esModoConsulta) {
        if (this.idConvocatoria == null || Boolean.TRUE.equals(esModoConsulta)
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            return false;
        }
        try {
            Integer idTramiteDef = this.utilConstantes.getGlobalCatalogosValores().get(1918);
            Integer idTipoDocDef = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            List<Integer> estadosPendientes = Arrays.asList(
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO),
                    this.utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO)
            );
            List<PropuestaResolucionDTO> expedientes = this.expedienteDAO.listaPropuestaResolucion(this.idConvocatoria, estadosPendientes);
            if (expedientes == null || expedientes.isEmpty()) {
                return false;
            }
            boolean existeFichero = false;
            for (PropuestaResolucionDTO elemento : expedientes) {
                if (this.documentoPtfrService.consultarDocumentoPortafirmaPorTramite(elemento.getIdExpediente(), idTramiteDef)) {
                    return false;
                }
                if (!existeFichero) {
                    ExpedienteDocDTO doc = this.getDocumentoResolucionProvisional(elemento.getIdExpediente(), idTipoDocDef, idTramiteDef);
                    if (doc != null && doc.getCdFica() != null) {
                        existeFichero = true;
                    }
                }
            }
            return existeFichero;
        } catch (Exception e) {
            log.error("Error en habilitarEnviarPropuestaDefPortafirmas", e);
            return false;
        }
    }

    
}
