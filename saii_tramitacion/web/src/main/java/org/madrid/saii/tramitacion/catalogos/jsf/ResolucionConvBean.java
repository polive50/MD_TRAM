package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
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
    
    /** Abre el diálogo de subida de la resolución de concesión. */
    public void subirResolucionConcesionDialog() {
        this.archivoResolucionConcesion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
    }

    /** Cierra el diálogo de subida de la resolución de concesión. */
    public void cerrarSubirResolucionConcesionDialog() {
        this.archivoResolucionConcesion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionConcesion");
    }

    /** Indica si se permite subir el documento de concesión. */
    public boolean habilitarBotonSubirResolucionConcesion() {
        return this.idConvocatoria != null
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta());
    }

    /**
     * Guarda el PDF sin firmar de concesión y su referencia documental.
     */
    public void cargarFicheroResolucionConcesion() {
        if (!this.habilitarBotonSubirResolucionConcesion()) {
            return;
        }
        UploadedFile archivo = this.archivoResolucionConcesion;
        if (archivo == null || archivo.getFileName() == null
                || archivo.getFileName().isEmpty()) {
            this.mostrarMensajeElegirFichero = Boolean.TRUE;
            PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
            return;
        }
        if (!"pdf".equalsIgnoreCase(FilenameUtils.getExtension(archivo.getFileName()))
                || archivo.getSize() == 0) {
            AtlasMessageUtils.addErrorMessageFromBundle("resolucion.concesion.documento.pdf");
            PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
            return;
        }

        try {
            Integer tipoDocumento = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            Integer tramite = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.CONCESION_DEFINITIVA);
            if (tipoDocumento == null || tramite == null) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter("expediente.solicitud.convocatoria.idConvocatoria",
                    this.idConvocatoria, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                    tipoDocumento, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                    tramite, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(0, -1,
                    filtros, null);
            for (ExpedienteDocDTO documento : documentos) {
                if (documento.getCdFica() != null) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            "resolucion.concesion.documento.existe");
                    PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
                    return;
                }
            }

            ExpedienteDTO expediente = null;
            List<SolicitudDTO> solicitudes = this.solicitudService
                    .buscarPorIdConvocatoria(this.idConvocatoria);
            for (SolicitudDTO solicitud : solicitudes) {
                List<Expediente> expedientes = this.expedienteDAO
                        .recuperarExpedientesByIdSolicitud(solicitud.getIdSolicitud());
                if (!expedientes.isEmpty()) {
                    expediente = this.expedienteService.buscarIdSimple(
                            expedientes.get(0).getIdExpediente(), Boolean.FALSE);
                    break;
                }
            }
            if (expediente == null || expediente.getIdExpediente() == null) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            ExpedienteDocDTO documento = new ExpedienteDocDTO();
            documento.setExpedienteDTO(expediente);
            documento.setEstadoDTO(this.catalogosValoresService.buscarId(
                    this.utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoDocumento.REVISION_OK)));
            documento.setTipoDocumentoDTO(this.catalogosValoresService.buscarId(tipoDocumento));
            documento.setTramiteDTO(this.catalogosValoresService.buscarId(tramite));
            FicaMetadatos metadatos = new FicaMetadatos();
            metadatos.setNombreFichero(archivo.getFileName());
            metadatos.setFechaFichero(new Date());
            metadatos.setExtension(Constantes.Extensiones.PDF);
            metadatos.setUsuario(UserUtils.getUserName().toUpperCase());
            metadatos.setEncriptar(false);
            documento.setFicaMetadatos(metadatos);
            try (InputStream entrada = archivo.getInputstream()) {
                documento.setContenidoFichero(IOUtils.toByteArray(entrada));
            }
            this.expedienteDocDTO = this.expedienteDocService.guardarExterno(documento);
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_OK);
            this.cerrarSubirResolucionConcesionDialog();
        } catch (ServiceException | IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
            PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionConcesion");
        }
    }
    
    /** Abre el diálogo de subida de la resolución individual de exclusión. */
    public void subirResolucionExclusionDialog() {
        this.archivoResolucionExclusion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionExclusion");
    }

    /** Cierra el diálogo de subida de la resolución individual de exclusión. */
    public void cerrarSubirResolucionExclusionDialog() {
        this.archivoResolucionExclusion = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.ocultarPopupPF("dialogSubirResolucionExclusion");
    }

    /** Abre el diálogo de subida de la resolución individual de desistimiento. */
    public void subirResolucionDesistimientoDialog() {
        this.archivoResolucionDesistimiento = null;
        this.mostrarMensajeElegirFichero = Boolean.FALSE;
        PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionDesistimiento");
    }

    /** Cierra el diálogo de subida de la resolución individual de desistimiento. */
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
        if (!modoCorrecto || Boolean.TRUE.equals(esModoConsulta)
                || this.tramitesResolucionEnviados.contains(tramite)) {
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

    /** Indica si se puede enviar una resolución individual o conjunta a Portafirmas. */
    public boolean habilitarEnviarResolucionDefinitivaPortafirmas(Integer tramite,
            Boolean esModoConsulta) {
        boolean modoCorrecto = Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO.equals(tramite)
                ? Boolean.TRUE.equals(this.getResolucionExclusionDesistimientoConjunta())
                : this.habilitarBotonesResolucionIndividual();
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

    /** Reconsulta antes de actuar para no aceptar un modo incompatible desde una vista antigua. */
    private void validarModoResolucionSeleccionado(Integer tramite) throws ServiceException {
        this.idConvocatoriaEnviosConsultados = null;
        Boolean conjunta = this.getResolucionExclusionDesistimientoConjunta();
        boolean envioConjunto = Constantes.Tramite.RESOLUCION_EXCLUSION_DESISTIMIENTO_CONJUNTO
                .equals(tramite);
        if (conjunta == null || conjunta.booleanValue() != envioConjunto) {
            throw new ServiceException(
                    "El modo de resolución no coincide con la selección o con los envíos registrados en la convocatoria.");
        }
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
        Integer estado = Constantes.Tramite.EXCLUSION.equals(tramite)
                ? Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO
                        : Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO;
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);
        
        Integer idEstadoPendRevisionAleg = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES);

        Integer idEstadoExclProvis = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
        
        List<ResolucionExclusionDesistimientoDTO> expedientes = this.expedienteDAO
                .listaResolucionExclusionDesistimiento(this.idConvocatoria,
                        Collections.singletonList(idEstado), Constantes.Cata.REQUISITOS, idEstadoPendRevisionAleg,idEstadoExclProvis);
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
            } else {
                this.archivoResolucionDefinitiva = null;
            }
        }  catch (ServiceException | IOException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
        }
    }
    
    /** Impide sustituir el PDF de un trámite con alguna petición activa registrada. */
    private void validarSubidaResolucionDefinitiva(Integer tramite) throws ServiceException {
        // Refresca los envíos para proteger también las subidas desde una vista antigua.
        this.validarModoResolucionSeleccionado(tramite);
        // Reconsulta antes de ejecutar la acción.
        this.idConvocatoriaProvisionalesConsultados = null;
        boolean hayProvisionales = this.hayProvisionalesResolucion(tramite);
        if (hayProvisionales) {
            throw new ServiceException(
                    "No se puede subir la resolución hasta gestionar los expedientes provisionales o comprobar sus estados.");
        }
        if (this.tramitesResolucionEnviados.contains(tramite)) {
            throw new ServiceException(
                    "No se puede subir la resolución porque este trámite ya se ha enviado a la cola de Portafirmas.");
        }
    }

    private void descargarFicheroResolucionDefinitiva(Integer tramite) {
        try {
            ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite);
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

    private boolean existeResolucionDefinitiva(Integer tramite) {
        ExpedienteDocDTO documento = this.getDocumentoResolucionDefinitiva(tramite);
        return documento != null && documento.getCdFica() != null;
    }

    private ExpedienteDocDTO getDocumentoResolucionDefinitiva(Integer tramite) {
        try {
            Integer estadoPropuesta = Constantes.Tramite.EXCLUSION.equals(tramite)
                    ? Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO
                    : Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO;
            Integer estadoFinal = Constantes.Tramite.EXCLUSION.equals(tramite)
                    ? Constantes.EstadoExpediente.EXCLUIDO : Constantes.EstadoExpediente.DESISTIDO;
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
            Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO);
            List<ResolucionConcesionDTO> expedientes = this.expedienteDAO
                    .listaResolucionConcesion(this.idConvocatoria, idEstado);

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
        HSSFSheet hoja = libro.createSheet("Concedido definitivo aceptado");
        String[] cabeceras = { "Línea", "Nombre o razón social de la empresa",
                "Número de expediente", "NIF" };
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
            fila.createCell(3).setCellValue(this.valorExcel(expediente.getNif()));
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
     * Obtiene los expedientes concedidos definitivos aceptados de la convocatoria.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes concedidos
     */
    public AtlasListAndCount<ResolucionConcesionDTO> obtenerConcedidosDefinitivosAceptados(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ResolucionConcesionDTO> resultado = new AtlasListAndCount<>();
        List<ResolucionConcesionDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO);
            listado = this.expedienteDAO.listaResolucionConcesion(this.idConvocatoria, idEstado);
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
    
    /** Indica si hay expedientes en propuesta de exclusión provisional en la convocatoria. */
    public boolean isHayExcluidosProvisionales() {
        this.consultarEstadosProvisionalesResolucion();
        Integer idEstado = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL);
        return this.estadosProvisionalesResolucion.contains(idEstado);
    }

    /** Reutiliza la consulta en el alcance request; no se restaura mediante guardarEstado. */
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
            List<Integer> idsEstado = Arrays.asList(idExcluidoProvisional, idDesistidoProvisional);
            List<Integer> estados = this.expedienteDAO.obtenerEstadosProvisionalesResolucion(
                    this.idConvocatoria, idsEstado);
            this.estadosProvisionalesResolucion = estados;
        } catch (RuntimeException e) {
            this.errorConsultaProvisionalesResolucion = true;
            log.error("Error consultando los estados provisionales de la convocatoria "
                    + this.idConvocatoria, e);
        }
    }
    
    /** Indica si hay expedientes en propuesta de desistimiento provisional en la convocatoria. */
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
  
}
