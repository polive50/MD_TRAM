package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    
    
    /*
     * expedienteDocDTO
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    
    private Boolean mostrarMensajeElegirFichero = Boolean.FALSE;
    
    /** Subpestaña activa de la resolución de convocatoria. */
    private int pestanaResolucionConvActiva;
    
    /** Indica si se ha seleccionado la resolución conjunta. */
    private Boolean resolucionExclusionDesistimientoConjunta = Boolean.TRUE;

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
        Object resolucionConjunta = ((UIInput) event.getComponent()).getValue();
        this.resolucionExclusionDesistimientoConjunta = Boolean.TRUE
                .equals(resolucionConjunta);
    }

    /**
     * Indica si se pueden utilizar los botones de las resoluciones individuales.
     *
     * @return {@code true} cuando se ha seleccionado la resolución individual
     */
    public boolean habilitarBotonesResolucionIndividual() {
        return Boolean.FALSE.equals(this.resolucionExclusionDesistimientoConjunta);
    }

    /**
     * Indica si se puede subir una resolución individual.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return {@code true} cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonSubirResolucionIndividual(Boolean esModoConsulta) {
        return this.habilitarBotonesResolucionIndividual()
                && !Boolean.TRUE.equals(esModoConsulta);
    }

    /** Indica si se puede subir la resolución individual de exclusión. */
    public boolean habilitarBotonSubirResolucionExclusion(Boolean esModoConsulta) {
        return this.habilitarBotonSubirResolucionIndividual(esModoConsulta)
                && !this.existeResolucionIndividual(Constantes.Tramite.EXCLUSION);
    }

    /** Indica si se puede descargar la resolución individual de exclusión. */
    public boolean habilitarBotonDescargarResolucionExclusion() {
        return this.habilitarBotonesResolucionIndividual()
                && this.existeResolucionIndividual(Constantes.Tramite.EXCLUSION);
    }

    /** Indica si se puede subir la resolución individual de desistimiento. */
    public boolean habilitarBotonSubirResolucionDesistimiento(Boolean esModoConsulta) {
        return this.habilitarBotonSubirResolucionIndividual(esModoConsulta)
                && !this.existeResolucionIndividual(Constantes.Tramite.DESISTIMIENTO);
    }

    /** Indica si se puede descargar la resolución individual de desistimiento. */
    public boolean habilitarBotonDescargarResolucionDesistimiento() {
        return this.habilitarBotonesResolucionIndividual()
                && this.existeResolucionIndividual(Constantes.Tramite.DESISTIMIENTO);
    }

    /**
     * Indica si se puede subir la resolución conjunta definitiva.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return {@code true} cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonSubirResolucionConjunta(Boolean esModoConsulta) {
        return Boolean.TRUE.equals(this.resolucionExclusionDesistimientoConjunta)
                && !Boolean.TRUE.equals(esModoConsulta)
                && this.getCodigoFicaResolucionExclusionDesistimiento() == null;
    }

    /**
     * Indica si se puede descargar la resolución conjunta definitiva.
     *
     * @return {@code true} cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonDescargarResolucionConjunta() {
        return Boolean.TRUE.equals(this.resolucionExclusionDesistimientoConjunta)
                && this.getCodigoFicaResolucionExclusionDesistimiento() != null;
    }

    /**
     * Indica si se puede enviar la resolución conjunta a Portafirmas.
     *
     * @param esModoConsulta indica si la convocatoria está en modo consulta
     * @return {@code true} cuando el botón debe estar habilitado
     */
    public boolean habilitarBotonEnviarResolucionConjuntaPortafirmas(Boolean esModoConsulta) {
        return this.habilitarBotonDescargarResolucionConjunta()
                && !Boolean.TRUE.equals(esModoConsulta);
    }


    /**
     * Sube a FICA el PDF sin firmar de la resolución conjunta de exclusión y
     * desistimiento y guarda su referencia documental.
     */
    public void cargarFicheroResolucionExclusionDesistimiento() {
        try {
            if (Objects.nonNull(this.archivoResolucionDefinitiva)
                    && Objects.nonNull(this.archivoResolucionDefinitiva.getFileName())
                    && !this.archivoResolucionDefinitiva.getFileName().isEmpty()) {
                Integer tipoDocumento = utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
                Integer tramite = utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.Tramite.EXCLUSION);
                //Comprobamos que no se haya subido ya el documento
                ExpedienteDocDTO documentoExistente = this.getExpedienteDocDTO(tipoDocumento);
                if (Objects.nonNull(documentoExistente)
                        && Objects.nonNull(documentoExistente.getCdFica())) {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.RESOLUCION_EXCLUSION_DESISTIMIENTO_EXISTE);
                    this.archivoResolucionDefinitiva = null;
                    PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionExclusionDesistimiento");
                    return;
                }
                this.prepararMetadatosFica(this.archivoResolucionDefinitiva, tipoDocumento,
                        tramite);
                this.expedienteDocDTO = this.expedienteDocService
                        .guardarExterno(this.expedienteDocDTO);
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                this.mostrarMensajeElegirFichero = Boolean.FALSE;
                this.archivoResolucionDefinitiva = null;
            } else {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF("dialogSubirResolucionExclusionDesistimiento");
            }
        } catch (ServiceException e) {
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage());
        }
    }
    
    /** Sube a FICA la resolución individual de exclusión. */
    public void cargarFicheroResolucionExclusion() {
        this.cargarFicheroResolucionIndividual(this.archivoResolucionExclusion,
                Constantes.Tramite.EXCLUSION,
                ConstantesWeb.Mensajes.RESOLUCION_EXCLUSION_EXISTE,
                "dialogSubirResolucionExclusion");
    }

    /** Sube a FICA la resolución individual de desistimiento. */
    public void cargarFicheroResolucionDesistimiento() {
        this.cargarFicheroResolucionIndividual(this.archivoResolucionDesistimiento,
                Constantes.Tramite.DESISTIMIENTO,
                ConstantesWeb.Mensajes.RESOLUCION_DESISTIMIENTO_EXISTE,
                "dialogSubirResolucionDesistimiento");
    }

    /** Descarga de FICA la resolución individual de exclusión. */
    public void descargarFicheroResolucionExclusion() {
        this.descargarFicheroResolucionIndividual(Constantes.Tramite.EXCLUSION);
    }

    /** Descarga de FICA la resolución individual de desistimiento. */
    public void descargarFicheroResolucionDesistimiento() {
        this.descargarFicheroResolucionIndividual(Constantes.Tramite.DESISTIMIENTO);
    }

    private void cargarFicheroResolucionIndividual(UploadedFile archivo, Integer tramite,
            String mensajeDocumentoExistente, String dialogo) {
        try {
            if (archivo == null || archivo.getFileName() == null
                    || archivo.getFileName().isEmpty()) {
                this.mostrarMensajeElegirFichero = Boolean.TRUE;
                PrimefacesUtils.mostrarPopupPF(dialogo);
                return;
            }
            if (this.existeResolucionIndividual(tramite)) {
                AtlasMessageUtils.addErrorMessageFromBundle(mensajeDocumentoExistente);
                PrimefacesUtils.mostrarPopupPF(dialogo);
                return;
            }
            Integer tipoDocumento = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
            Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
            this.prepararMetadatosFica(archivo, tipoDocumento, idTramite);
            this.expedienteDocDTO = this.expedienteDocService
                    .subirDocumentoFica(this.expedienteDocDTO);
            this.guardarDocumento();
            this.mostrarMensajeElegirFichero = Boolean.FALSE;
            if (Constantes.Tramite.EXCLUSION.equals(tramite)) {
                this.archivoResolucionExclusion = null;
            } else {
                this.archivoResolucionDesistimiento = null;
            }
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_SUBIDA_KO);
            log.error(e.getMessage(), e);
        }
    }

    private void descargarFicheroResolucionIndividual(Integer tramite) {
        try {
            ExpedienteDocDTO documento = this.getDocumentoResolucionIndividual(tramite);
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

    private boolean existeResolucionIndividual(Integer tramite) {
        ExpedienteDocDTO documento = this.getDocumentoResolucionIndividual(tramite);
        return documento != null && documento.getCdFica() != null;
    }

    private ExpedienteDocDTO getDocumentoResolucionIndividual(Integer tramite) {
        Integer tipoDocumento = this.utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA);
        Integer idTramite = this.utilConstantes.getGlobalCatalogosValores().get(tramite);
        return this.getExpedienteDocDTO(tipoDocumento, idTramite);
    }
    
    /**
     * Obtiene el código FICA de la resolución conjunta de exclusión y
     * desistimiento para su descarga mediante el servlet común de documentos.
     *
     * @return código FICA del documento o {@code null} si todavía no se ha subido
     */
    public Integer getCodigoFicaResolucionExclusionDesistimiento() {
        ExpedienteDocDTO documento = this.getExpedienteDocDTO(
                utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.RESOLUCION_CONVOCATORIA_DEFINITIVO));
        return documento != null ? documento.getCdFica() : null;
    }
    
    /**
     * Descarga en un único Excel los expedientes excluidos y desistidos de la
     * convocatoria.
     */
    public void descargarExcelResolucionExclusionDesistimiento() {
        try (ByteArrayOutputStream salida = new ByteArrayOutputStream();
                HSSFWorkbook libro = new HSSFWorkbook()) {
            Integer estadoExcluido = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.EXCLUIDO);
            Integer estadoDesistido = this.utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.EstadoExpediente.DESISTIDO);
            List<ResolucionExclusionDesistimientoDTO> excluidos = this.agruparMotivos(
                    this.expedienteDAO.listaResolucionExclusionDesistimiento(
                            this.idConvocatoria, estadoExcluido, Constantes.Cata.REQUISITOS));
            List<ResolucionExclusionDesistimientoDTO> desistidos = this.agruparMotivos(
                    this.expedienteDAO.listaResolucionExclusionDesistimiento(
                            this.idConvocatoria, estadoDesistido, Constantes.Cata.REQUISITOS));

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
     * Obtiene los expedientes excluidos definitivos de la convocatoria.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes excluidos definitivos
     */
    public AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerExcluidosDefinitivos(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionExclusionDesistimiento(index, pageSize,
                Constantes.EstadoExpediente.EXCLUIDO);
    }

    /**
     * Obtiene los expedientes desistidos definitivos de la convocatoria.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param orderBean orden del listado
     * @param filtros filtros del listado
     * @return listado paginado de expedientes desistidos definitivos
     */
    public AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerDesistidosDefinitivos(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        return this.obtenerResolucionExclusionDesistimiento(index, pageSize,
                Constantes.EstadoExpediente.DESISTIDO);
    }

    /**
     * Obtiene y pagina los expedientes de la convocatoria para el estado
     * indicado.
     *
     * @param index primer elemento de la página
     * @param pageSize número de elementos de la página
     * @param estado constante del estado del expediente
     * @return listado paginado de expedientes
     */
    private AtlasListAndCount<ResolucionExclusionDesistimientoDTO> obtenerResolucionExclusionDesistimiento(
            int index, int pageSize, Integer estado) {
        AtlasListAndCount<ResolucionExclusionDesistimientoDTO> resultado = new AtlasListAndCount<>();
        List<ResolucionExclusionDesistimientoDTO> listado = new ArrayList<>();

        if (this.idConvocatoria != null) {
            Integer idEstado = this.utilConstantes.getGlobalCatalogosValores().get(estado);
            listado = this.agruparMotivos(this.expedienteDAO.listaResolucionExclusionDesistimiento(
                    this.idConvocatoria, idEstado, Constantes.Cata.REQUISITOS));
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
        Map<String, ResolucionExclusionDesistimientoDTO> expedientes = new LinkedHashMap<>();
        for (ResolucionExclusionDesistimientoDTO elemento : listado) {
            ResolucionExclusionDesistimientoDTO expediente = expedientes.get(elemento.getNumeroExpediente());
            if (expediente == null) {
                expedientes.put(elemento.getNumeroExpediente(), elemento);
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
  
}
