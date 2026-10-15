package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaEvalTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocEvalDAO;
import org.madrid.saii.lib.dao.InformeEvalDocDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCoorEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalTipoDocDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaCoorEvalService;
import org.madrid.saii.lib.services.ConvocatoriaCoorService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.ExpedienteEvalService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.SolicitudService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.primefaces.PrimeFaces;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nttdata
 *
 */

@Slf4j
@Getter
@Setter
@Component("evaluacionBean")
@Scope("request")
public class EvaluacionBean {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;

    /* Servicio catalogo valores */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

    /**
     * Servicio ConvocatoriaCoorService
     */
    @Autowired
    @Qualifier("convocatoriaCoorService")
    private ConvocatoriaCoorService convocatoriaCoorService;

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /**
     * Servicio ConvocatoriaService
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
    
    @Autowired
    @Qualifier("convocatoriaCoorEvalService")
    private ConvocatoriaCoorEvalService convocatoriaCoorEvalService;
    
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;
    
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    
    
    @Autowired
    @Qualifier("expedienteDocEvalDAO")
    private ExpedienteDocEvalDAO expedienteDocEvalDAO;
    
    
    @Autowired
    @Qualifier("informeEvalDocDAO")
    private InformeEvalDocDAO informeEvalDocDAO;
    
    
    @Autowired
    @Qualifier("convocatoriaEvalTipoDocDAO")
    private ConvocatoriaEvalTipoDocDAO convocatoriaEvalTipoDocDAO;
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /** dto de plantilla seleccionada */
    private ExpedienteEvalDTO expedienteEvalDTO;

    /**
     * Identificador de panel de formulario evaluacion a refrescar
     */
    private String formularioEvaluacion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelEvaluacionSolapa";

    /**
     * Identificador de campo estado de expediente a refrescar
     */
    private String campoEstadoExpediente = "convocatoriaForm:contenedorSolapas:dsEstadoExpedienteSel";

    /**
     * Identificador de solapa de evaluacion
     */
    private String solapaEvaluacion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";

    /**
     * Comentario del motivo de rechazo del documento seleccionado
     */
    private String motivoCausaSeleccionado;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////

    private ExpedienteBean expedienteBean;
    
    private ExpedienteEvalDTO coordinadorSeleccionado;
    
    
    private Boolean cargaBotonesEvaluacion;

    
    private List<InformeEvalDocDTO> listaInformeCoordinadores;
    
    private List<ExpedienteEvalDTO> listaInformeEvaluadores;
    
    private List<ExpedienteEvalDTO> listaCoordinadores;
    
   
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    public EvaluacionBean() {
        super();
    }

    /**
     * Obtener la lista de coordinadores asignado por idExpediente y itCoorEval
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return AtlasListAndCount<ExpedienteEvalDTO>
     */
    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaCoordinadoresAsignados(int index,
            int pageSize, Object ordenacion, Object filter) {

        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
        try {
            List<ExpedienteEvalDTO> listaCoorAsig = this.expedienteEvalService
                    .obtenerCoordinadorExpediente(this.expedienteBean.getIdExpediente(),
                            Constantes.TipoCoorEval.COORDINADOR);

            this.listaCoordinadores = listaCoorAsig;
            
            listado.setResultados(listaCoorAsig);
            listado.setTotal(listaCoorAsig.size());

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    
    public AtlasListAndCount<ConvocatoriaCoorDTO> obtenerListaCoordinadoresConvocatoria(int index,
            int pageSize, Object ordenacion, Object filter) {

        AtlasListAndCount<ConvocatoriaCoorDTO> listado = new AtlasListAndCount<>();
        try {
            if (this.expedienteBean.getIdSolicitud() != null) {
                SolicitudDTO solicitudDTO = this.solicitudService
                        .buscarId(this.expedienteBean.getIdSolicitud());

                Integer idConvocatoria = solicitudDTO.getConvocatoriaDTO().getIdConvocatoria();
                Integer idArea = solicitudDTO.getConvocatoriaAreaDTO().getAreaDTO().getIdValor();

                List<ConvocatoriaCoorDTO> listaCompleta = this.convocatoriaCoorService
                        .obtenerCoordinadorConvocatoriaArea(idConvocatoria, idArea);

                if (listaCompleta != null && !listaCompleta.isEmpty()) {
                    int total = listaCompleta.size();
                    
                    // Calcular los límites para la sublista paginada
                    int fromIndex = Math.min(index, total);
                    int toIndex = Math.min(fromIndex + pageSize, total);

                    //  CORREGIDO: Envolver la sublista en new ArrayList<>() para hacerlo Serializable
                    listado.setResultados(new ArrayList<>(listaCompleta.subList(fromIndex, toIndex)));
                    listado.setTotal(total);
                } else {
                    listado.setResultados(Collections.emptyList());
                    listado.setTotal(0);
                }
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO, exc);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    

    public AtlasListAndCount<InformeEvalDocDTO> obtenerListaInformeCoordinadores(ExpedienteEvalDTO coordinador) throws ServiceException {
        
        
        AtlasListAndCount<InformeEvalDocDTO> listado = new AtlasListAndCount<>();
        if(coordinador == null) {
            return listado;
        }
        List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(coordinador.getIdExpedienteEval());

        
        this.listaInformeCoordinadores = listaInformeEval;
        
         listado.setResultados(listaInformeEval);
         listado.setTotal(listaInformeEval.size());
         
         
         
         if(listaInformeEval.isEmpty()){
             this.cargaBotonesEvaluacion = Boolean.TRUE;
         }
         
        return listado;
    }


    
    public void seleccionarCoordinador(ExpedienteEvalDTO dto) {
        this.coordinadorSeleccionado = dto;
        //MD-2642: reseteamos los valores de la lista de documentos de la evaluacion seleccionada
        this.cargaBotonesEvaluacion = Boolean.FALSE;
        PrimeFaces.current().ajax().update(this.solapaEvaluacion);
    }
    
    
    
    
    /**
     * Metodo para verificar si todos los informes de evaluación de un
     * expediente estan en estado "cerrado por coordinador"
     *
     * @return
     */
    public Boolean verificarTodoInformeCerradoCoordinador() {
        Boolean resultado = Boolean.FALSE;
        try {
            if (Objects.nonNull(this.expedienteBean)) {
                resultado = this.expedienteEvalService
                        .esCerradoCoordinadorTodos(this.expedienteBean.getIdExpediente());
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return resultado;
    }

    /**
     * Método para verificar si ya se realizó la evaluación del expediente
     *
     * @return
     */
    public Boolean verificarMostrarBotoneraEvaluadoGestor() {
        Boolean resultado = Boolean.TRUE;

        List<Integer> lstEstadoEvaluacionNoPermitido = new ArrayList<>();
        lstEstadoEvaluacionNoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));
        lstEstadoEvaluacionNoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));

        List<Integer> lstEstadoExpedienteNoPermitido = new ArrayList<>();
        lstEstadoExpedienteNoPermitido
                .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES));
        lstEstadoExpedienteNoPermitido.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION));

        if (lstEstadoEvaluacionNoPermitido
                .contains(this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor())
                || lstEstadoExpedienteNoPermitido.contains(
                        this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())) {
            resultado = Boolean.FALSE;
        }

        return resultado;
    }

    /**
     * Método para verificar si ya se realizó la evaluación del expediente
     *
     * @return
     */
    public Boolean verificarHabilitarBotoneraEvaluadoGestor() {

        Boolean resultado = Boolean.FALSE;

        try {
            if (Objects.nonNull(this.expedienteBean)) {
                resultado = this.expedienteEvalService
                        .esCerradoGestorTodos(this.expedienteBean.getIdExpediente());

            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return resultado;
    }
    
    
    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
     * en esta solapa (aprobar/desaprobar evaluación, agregar/eliminar coordinador, aprobar/
     * rechazar/finalizar informes), dejando habilitadas las de solo lectura (descargar, ver).
     *
     * @return true si el expediente está Rechazado Definitivo
     */
    public Boolean isExpedienteRechazadoDefinitivo() {
        return this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }

    public Boolean verificarHabilitarFinalizarEvaluacion() {

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite aprobar/desaprobar la evaluación.
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        if (this.listaCoordinadores == null || this.listaCoordinadores.isEmpty()) {
            return Boolean.FALSE;
        }
        
        Integer estadoPendienteExperto = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO);      
        Integer estadoCerradoGestor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR);

        for (ExpedienteEvalDTO exp : this.listaCoordinadores) {
            
            
            if (estadoPendienteExperto.equals(exp.getEstadoDTO().getIdValor())) {
                return Boolean.FALSE;
            }
            
            // 1. Si no tiene la marca 'S' (es null o 'N'), lo ignoramos y pasamos al siguiente
            if (exp.getItAceptaEval() == null || !"S".equalsIgnoreCase(exp.getItAceptaEval())) {
                continue;
            }
            
            // 2. Si tiene 'S', comprobamos obligatoriamente el estado del EXPEDIENTE
            if (exp.getEstadoDTO() == null || exp.getEstadoDTO().getIdValor() == null || !exp.getEstadoDTO().getIdValor().equals(estadoCerradoGestor)) {
                return Boolean.FALSE; 
            }
        }

        return Boolean.TRUE;
        
    }
    
    
    
//    public Boolean habilitarBotonAgregar( ConvocatoriaDTO convo) throws ServiceException {
//
//        if(this.expedienteBean.getExpedienteDTO() == null || this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor() == null ) {
//            return Boolean.TRUE; 
//        }
//        Integer idEstadoEval = this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor();
//        Integer estadoEvaluadoOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK);
//        
//        
//
//            if (Objects.equals(idEstadoEval, estadoEvaluadoOk) ) {
//                return Boolean.TRUE; 
//            }
//        
//
//        return Boolean.FALSE;
//        
//    }
    
    public Boolean habilitarBotonAgregar(ConvocatoriaDTO convo) {
        
        if (this.expedienteBean.isExpedienteDesistidoOExcluido()) {
            return Boolean.TRUE;
        }
        
        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite agregar coordinadores (TRUE = deshabilitado, según el resto de este método).
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.TRUE;
        }

        // 1. Control de nulos por seguridad
        if (this.expedienteBean.getExpedienteDTO() == null || this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO() == null) {
            return Boolean.TRUE;
        }
        
        Integer idEstadoEval = this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO().getIdValor();
        Integer estadoEvaluadoOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK);
        Integer estadoEvaluadoKo = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO);

        // 2. Si es OK o KO -> Deshabilitamos (TRUE)
        if (Objects.equals(idEstadoEval, estadoEvaluadoOk) || Objects.equals(idEstadoEval, estadoEvaluadoKo)) {
            return Boolean.TRUE; 
        }
       
        // 3. Control de fecha fin de trámite
        if (convo != null && convo.getFcFinTramiEval() != null) {
            Date hoy = new Date(); 

            // Si hoy YA HA PASADO la fecha límite -> Deshabilitamos (TRUE)
            if (hoy.after(convo.getFcFinTramiEval())) {
                return Boolean.TRUE; 
            }
        }

        // 4. Si no es OK, no es KO, y hoy es menor o igual a la fecha límite -> ¡Habilitamos! (FALSE)
        return Boolean.FALSE;
    }
    
    
    /**
     * Método para verificar si ya se realizó la evaluación del expediente
     *
     * @return
     */
    public Boolean verificarHabilitarBotoneraCoordinador() {
        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite finalizar la revisión del coordinador.
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }
        if (this.listaInformeCoordinadores == null || this.listaInformeCoordinadores.isEmpty()) {
            return Boolean.FALSE;
        }

        Integer estadoCerradoGestor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR);

        for (InformeEvalDocDTO informe : this.listaInformeCoordinadores) {
            if ( informe.getIdEstado() == null || informe.getIdEstado().getIdValor() == null || !informe.getIdEstado().getIdValor().equals(estadoCerradoGestor)) {
                return Boolean.FALSE; 
            }
        }

        return Boolean.TRUE;
    }
    
    /**
     * Método para verificar si ya se realizó la evaluación del expediente
     *
     * @return
     */
//    public Boolean verificarHabilitarBotoneraEvaluador() {
//        if (this.listaInformeEvaluadores == null || this.listaInformeEvaluadores.isEmpty()) {
//            return Boolean.FALSE;
//        }
//
//        Integer estadoCerradoGestor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR);
//
//        for (ExpedienteEvalDTO exp : this.listaInformeEvaluadores) {
//            
//            if (exp.getEstadoDTO() == null || exp.getEstadoDTO().getIdValor() == null || !exp.getEstadoDTO().getIdValor().equals(estadoCerradoGestor)) {
//                return Boolean.FALSE; 
//            }
//        }
//
//        return Boolean.TRUE;
//    }
//    
    
    public Boolean verificarHabilitarBotoneraEvaluador(ExpedienteEvalDTO coordinador) {

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite finalizar la revisión del evaluador.
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Integer estadoCerradoGestor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR);
        Integer estadoPendienteExperto = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO);
        
        
        
        if (this.listaInformeEvaluadores == null || this.listaInformeEvaluadores.isEmpty() || estadoCerradoGestor.equals(coordinador.getEstadoDTO().getIdValor()) ) {
            return Boolean.FALSE;
        }

        
        for (ExpedienteEvalDTO exp : this.listaInformeEvaluadores) {
            
            if (estadoPendienteExperto.equals(exp.getEstadoDTO().getIdValor())) {
                return Boolean.FALSE;
            }
            
            
            // 1. Si no tiene la marca 'S', ignoramos el registro por completo
            if (exp.getItAceptaEval() == null || !"S".equalsIgnoreCase(exp.getItAceptaEval())) {
                continue; 
            }
            
            // 2. CAMBIO: Miramos el estado del INFORME ACTIVO, no del expediente
            // NOTA: Asegúrate de si en tu InformeEvalDocDTO el método se llama getEstadoDTO() o similar
            if (exp.getInformeActivo() == null || 
                exp.getInformeActivo().getIdEstado() == null || 
                exp.getInformeActivo().getIdEstado().getIdValor() == null || 
                !exp.getInformeActivo().getIdEstado().getIdValor().equals(estadoCerradoGestor) ) {
                
                return Boolean.FALSE; // Si un informe activo con 'S' no está cerrado, deshabilitamos
            }
        }

        return Boolean.TRUE;
    }
    
    
    
    

    /**
     * Obtener la lista de coordinadores por convocatoria y area
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return AtlasListAndCount<ConvocatoriaCoorDTO>
     */
//    public AtlasListAndCount<ConvocatoriaCoorDTO> obtenerListaCoordinadoresConvocatoria(int index,
//            int pageSize, Object ordenacion, Object filter) {
//
//        AtlasListAndCount<ConvocatoriaCoorDTO> listado = new AtlasListAndCount<>();
//        try {
//            SolicitudDTO solicitudDTO = this.solicitudService
//                    .buscarId(this.expedienteBean.getIdSolicitud());
//
//            Integer idConvocatoria = solicitudDTO.getConvocatoriaDTO().getIdConvocatoria();
//            Integer idArea = solicitudDTO.getConvocatoriaAreaDTO().getAreaDTO().getIdValor();
//
//            List<ConvocatoriaCoorDTO> listaCoordinadores = this.convocatoriaCoorService
//                    .obtenerCoordinadorConvocatoriaArea(idConvocatoria, idArea);
//
//            listado.setResultados(listaCoordinadores);
//            listado.setTotal(listaCoordinadores.size());
//
//        } catch (ServiceException exc) {
//            log.error(ConstantesWeb.Err.ERROR_LISTADO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
//        }
//        return listado;
//    }

    /**
     * Metodo para desasociar coordinador
     *
     * @param dto
     */
    public void desasociarCoordinadorExpediente(ExpedienteEvalDTO dto) throws ServiceException {

        try {
            Boolean existenVarios = this.expedienteEvalService
                    .existenVariosCoordinadorExpediente(this.expedienteBean.getIdExpediente());

            if (dto.getEstadoDTO().getIdValor()
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO))) {
                if (Boolean.TRUE.equals(existenVarios)) {
                    this.expedienteEvalService.deleteLogico(dto.getIdExpedienteEval());

                    ExpedienteDTO expedienteDTO = this.expedienteService
                            .buscarIdMinimo(this.expedienteBean.getIdExpediente());

                    this.recuperarValoresVariablesExpediente(expedienteDTO);

                    PrimeFaces.current().ajax().update(this.formularioEvaluacion);
                    PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                    PrimeFaces.current().ajax().update(this.solapaEvaluacion);

                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.ELIMINADO_CORRECTAMENTE_COORDINADOR_ASOCIADO);
                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.EXPEDIENTE_ASIGNADO_COODINADOR_ELIMINAR);
                }
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.EXPEDIENTE_ASIGNADO_COODINADOR_ELIMINAR_ESTADO);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_ASIGNADO_COODINADOR_ELIMINAR);
        }
    }

    /**
     * Metodo para asociar coordinador
     *
     * @param dto
     */
    public void asociarCoordinadorExpediente(ConvocatoriaCoorDTO dto) {
        try {
            ExpedienteEvalDTO expEvalCoor = new ExpedienteEvalDTO();

            Integer idExpediente = this.expedienteBean.getIdExpediente();

            expEvalCoor.setIdExpediente(idExpediente);
            expEvalCoor.setPersonaDTO(dto.getInteresadoDTO());
            expEvalCoor.getEstadoDTO()
                    .setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO));
            expEvalCoor.getEstadoDTO()
                    .setNmConstante(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO);
            expEvalCoor.setItNotificado(Constantes.AfirmacionNegacion.S);
            expEvalCoor.setItCoorEval(Constantes.TipoCoorEval.COORDINADOR);

            ExpedienteDTO expedienteDTO = this.expedienteEvalService
                    .altaCoordinadorExpediente(expEvalCoor);

            this.recuperarValoresVariablesExpediente(expedienteDTO);

            PrimeFaces.current().ajax().update(this.formularioEvaluacion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaEvaluacion);

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_ASIGNADO_CORRECTAMENTE);

        } catch (ServiceException exc) {
            if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(exc.getKeyExcepcion())) {
                AtlasMessageUtils.addErrorMessage(exc.getMessage());
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.BAJA_KO);
            }
        }
    }
    
    
    
    public Boolean calcularFecha(ConvocatoriaDTO convo) {

        if (convo == null || convo.getFcFinTramiEval() == null) {
            return Boolean.FALSE;
        }

        Date hoy = new Date();
        Date fechaFinTramite = convo.getFcFinTramiEval();

        if (hoy.before(fechaFinTramite) || hoy.equals(fechaFinTramite)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
    
    
    

    /**
     * Obtener la lista de coordinadores asignado por idExpediente y itCoorEval
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return AtlasListAndCount<ExpedienteEvalDTO>
     */
//    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaEvaluadorAsociado(int index,
//            int pageSize, Object ordenacion, Object filter) {
//
//        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
//        try {
//            List<ExpedienteEvalDTO> listaEvalAsig = this.expedienteEvalService
//                    .obtenerCoordinadorExpediente(this.expedienteBean.getIdExpediente(),
//                            Constantes.TipoCoorEval.EVALUADOR);
//
//            listado.setResultados(listaEvalAsig);
//            listado.setTotal(listaEvalAsig.size());
//
//        } catch (ServiceException exc) {
//            log.error(ConstantesWeb.Err.ERROR_LISTADO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
//        }
//        return listado;
//    }
   
    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaEvaluadorAsociado(int index,
            int pageSize, Object ordenacion, Object filter) {

        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
        try {
            List<ExpedienteEvalDTO> listaOriginal = this.expedienteEvalService
                    .obtenerCoordinadorExpediente(this.expedienteBean.getIdExpediente(),
                            Constantes.TipoCoorEval.EVALUADOR);
    
            List<ExpedienteEvalDTO> listaAplanada = new ArrayList<>();            
            for (ExpedienteEvalDTO exp : listaOriginal) {
                
                List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(exp.getIdExpedienteEval());
                        
                if (listaInformeEval != null && !listaInformeEval.isEmpty()) {
                    for (InformeEvalDocDTO informe : listaInformeEval) {
                        // Creamos una copia del expediente para no pisar las referencias
//                        ExpedienteEvalDTO expFila = clonarExpediente(exp);                         
//                        expFila.setInformeActivo(informe);                         
//                        listaAplanada.add(expFila);
                        
                        ExpedienteEvalDTO expFila = new ExpedienteEvalDTO();
                        BeanUtils.copyProperties(exp, expFila);

                        expFila.setInformeActivo(informe);
                        listaAplanada.add(expFila);
                        
                    }
                } else {
                    // Si un expediente no tiene informes, ¿debe salir en la tabla? 
                    // Si la respuesta es SÍ, lo añades tal cual:
                    listaAplanada.add(exp);
                }
            }

            // 3. Asignas la lista aplanada al listado final
            listado.setResultados(listaAplanada);
            listado.setTotal(listaAplanada.size());

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
//    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaInformesEvaluador(ExpedienteEvalDTO coordinador) {
//
//        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
//        if(coordinador == null) {
//            return listado;
//        }
//        try {
//            List<ExpedienteEvalDTO> listaOriginal = this.expedienteEvalService
//                    .obtenerCoordinadorExpediente(this.expedienteBean.getIdExpediente(),
//                            Constantes.TipoCoorEval.EVALUADOR);
//            
//            
//            List<AtlasFilter> filtros = new ArrayList<>();
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO,  coordinador.getPersonaDTO().getIdInteresado(), Operador.EQUALS));
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,  this.expedienteBean.getIdConvocatoria(), Operador.EQUALS));
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//
//              
//            List<ConvocatoriaCoorDTO> listaCoordinadoresConvocatoria = this.convocatoriaCoorService.buscar(-1, 10, filtros, null);
//            
//            //TENGO QUE HACER UN BLUCLE PARA RECORRER TODOS LOS CORDINADORES ConvocatoriaCoorDTO
//                    
//            List<ConvocatoriaCoorEvalDTO> listaCoordiEvaluadoresConvocatoria = this.convocatoriaCoorEvalService.buscarByIdConvocatoriaCoorLista(listaCoordinadoresConvocatoria.get(0).getIdConvocatoriaCoor());
//            
//            
//            if (listaCoordiEvaluadoresConvocatoria != null && !listaCoordiEvaluadoresConvocatoria.isEmpty()) {
//
//                Set<Integer> idsEvaluadoresPermitidos = listaCoordiEvaluadoresConvocatoria.stream()
//                        .map(ConvocatoriaCoorEvalDTO::getEvaluadorDTO) 
//                        .filter(Objects::nonNull)
//                        .map(InteresadoDTO::getIdInteresado)           
//                        .filter(Objects::nonNull)
//                        .collect(Collectors.toSet());
//
//                listaOriginal = listaOriginal.stream()
//                        .filter(exp -> exp.getPersonaDTO() != null && 
//                                       exp.getPersonaDTO().getIdInteresado() != null && 
//                                       idsEvaluadoresPermitidos.contains(exp.getPersonaDTO().getIdInteresado()))
//                        .collect(Collectors.toList());
//            } else {
//                listaOriginal = new ArrayList<>();
//            }
// 
//            List<ExpedienteEvalDTO> listaAplanada = new ArrayList<>();            
//            for (ExpedienteEvalDTO exp : listaOriginal) {
//                
//                List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEval(exp.getIdExpedienteEval());
//                        
//                exp.setListaInformesEvaluacion(listaInformeEval);
//                
//                if (listaInformeEval != null && !listaInformeEval.isEmpty()) {
//                    for (InformeEvalDocDTO informe : listaInformeEval) {
//                        // Creamos una copia del expediente para no pisar las referencias                      
//                        ExpedienteEvalDTO expFila = new ExpedienteEvalDTO();
//                        BeanUtils.copyProperties(exp, expFila);
//
//                        expFila.setInformeActivo(informe);
//                        listaAplanada.add(expFila);
//                        
//                    }
//                } else {
//                    listaAplanada.add(exp);
//                }
//            }
//            
//
//            this.listaInformeEvaluadores =  listaAplanada;
//            
//            // 3. Asignas la lista aplanada al listado final
//            listado.setResultados(listaAplanada);
//            listado.setTotal(listaAplanada.size());
//    
//
//        } catch (ServiceException exc) {
//            log.error(ConstantesWeb.Err.ERROR_LISTADO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
//        }
//        return listado;
//    }
//    
    
    
    
    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaInformesEvaluador(ExpedienteEvalDTO coordinador) {

        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
        if(coordinador == null) {
            return listado;
        }
        try {
            List<ExpedienteEvalDTO> listaOriginal = this.expedienteEvalService
                    .obtenerCoordinadorExpediente(this.expedienteBean.getIdExpediente(),
                            Constantes.TipoCoorEval.EVALUADOR);

            List<ExpedienteEvalDTO> listaFiltrada = new ArrayList<>();

            if (coordinador != null && coordinador.getIdExpedienteEval() != null) {
                for (ExpedienteEvalDTO eval : listaOriginal) {
                    if (coordinador.getIdExpedienteEval().equals(eval.getIdExpedienteEvalPadre())) {
                        listaFiltrada.add(eval);
                    }
                }
            }
            
            

            Integer estadoCerradoCoordinador = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR);
            Integer estadoCerradoGestor = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR); // AÑADIDO AQUÍ
            Integer estadoEvaluacionPorCcoordinador = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.EN_EVALUACION_POR_COORDINADOR);           
            Integer estadoEvaluacionPorEvaluador = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.EN_EVALUACION_POR_EVALUADOR);
            

            List<ExpedienteEvalDTO> listaAplanada = new ArrayList<>();            
            for (ExpedienteEvalDTO exp : listaFiltrada) {
                
                List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(exp.getIdExpedienteEval());
                
                exp.setListaInformesEvaluacion(listaInformeEval);

                if (listaInformeEval != null && listaInformeEval.size() > 1) {

                    // 1. Agrupamos los informes por el idInforme de su objeto interno 'informe'
                    Map<Integer, List<InformeEvalDocDTO>> informesPorTipo = listaInformeEval.stream()
                            .filter(inf -> inf.getIdInforme() != null && inf.getIdInforme().getIdInforme() != null)
                            .collect(Collectors.groupingBy(inf -> inf.getIdInforme().getIdInforme()));

                    List<InformeEvalDocDTO> informesSeleccionados = new ArrayList<>();

                    // 2. Evaluamos la prioridad de manera INDEPENDIENTE para cada tipo de informe (28, 29, etc.)
                    for (List<InformeEvalDocDTO> grupo : informesPorTipo.values()) {

                        Optional<InformeEvalDocDTO> informeValido = grupo.stream()
                                .filter(inf -> inf.getIdEstado() != null && 
                                        estadoEvaluacionPorEvaluador.equals(inf.getIdEstado().getIdValor()))
                                .findFirst();
                        
                        if (!informeValido.isPresent()) {
                            informeValido = grupo.stream()
                                    .filter(inf -> inf.getIdEstado() != null && 
                                                   estadoEvaluacionPorCcoordinador.equals(inf.getIdEstado().getIdValor()))
                                    .findFirst();
                        }

                        // Prioridad 2: Cerrado Gestor (si no cumplió la Prioridad 1)
                        if (!informeValido.isPresent()) {
                            informeValido = grupo.stream()
                                    .filter(inf -> inf.getIdEstado() != null && 
                                                   estadoCerradoGestor.equals(inf.getIdEstado().getIdValor()))
                                    .findFirst();
                        }

                        // Prioridad 3: Cerrado Coordinador y Firmado 'S' (si no cumplió las anteriores)
                        if (!informeValido.isPresent()) {
                            informeValido = grupo.stream()
                                    .filter(inf -> inf.getIdEstado() != null && 
                                                   estadoCerradoCoordinador.equals(inf.getIdEstado().getIdValor()) &&
                                                   "S".equalsIgnoreCase(inf.getItFirmado()))
                                    .findFirst();
                        }

                        // Si se encontró un informe válido para ESTE tipo, lo agregamos a la lista final
                        informeValido.ifPresent(informesSeleccionados::add);
                    }

                    // 3. Sobrescribimos la lista original con los seleccionados (1 informe por cada idInforme)
                    listaInformeEval = informesSeleccionados;
                }
                // ----------------------------------------------------------------------------------

                
                if (listaInformeEval != null && !listaInformeEval.isEmpty()) {
                    for (InformeEvalDocDTO informe : listaInformeEval) {

                        ExpedienteEvalDTO expFila = new ExpedienteEvalDTO();
                        BeanUtils.copyProperties(exp, expFila);

                        expFila.setInformeActivo(informe);
                        listaAplanada.add(expFila);
                    }
                } else {
                    listaAplanada.add(exp);
                }
            }

            this.listaInformeEvaluadores = listaAplanada;
            
            listado.setResultados(listaAplanada);
            listado.setTotal(listaAplanada.size());

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    
    /**
     * Obtener la lista de coordinadores asignado por idExpediente y itCoorEval
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return AtlasListAndCount<ExpedienteEvalDTO>
     */
    public AtlasListAndCount<ExpedienteEvalDTO> obtenerListaEvaluadorDocumentos(int index,
            int pageSize, Object ordenacion, Object filter) {

        AtlasListAndCount<ExpedienteEvalDTO> listado = new AtlasListAndCount<>();
        try {
            
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                                
            List<ExpedienteEvalDTO> listaEvalAsig = this.expedienteEvalService.buscar(-1, 10, filtros, null);
                    
            listado.setResultados(listaEvalAsig);
            listado.setTotal(listaEvalAsig.size());

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    
    /**
     * Método que controla la descargar de fichero tipo 1
     *
     * @param imprDTO Datos de impreso
     * @param doc Datos de impreso
     */
    public void descargarDocumentoFica(Integer codigoFica) {

        try {

            if (codigoFica != null) {

                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);

                if (ficaFichero != null) {

                    log.info("Documento " + ficaFichero.getNombreFichero() + " recuperado");

                    // Se prepara el fichero.
                    UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
                            FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));

                } else {
                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }

            } else {
                log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }
    

  

//    public void descargarZipDocumentosFicaInforme(ExpedienteEvalDTO expediente) {
//        if (expediente == null || expediente.getCdFica() == null) {
//            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
//            return;
//        }
//
//        try {
//            List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(expediente.getIdExpedienteEval());
//            
//            if (listaInformeEval == null || listaInformeEval.isEmpty()) {
//                // Reemplazar por una constante de "No hay documentos" si existe
//                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); 
//                return;
//            }
//
//            byte[] zipBytes;
//            int archivosAgregados = 0;
//
//            // Uso de try-with-resources para garantizar el cierre de streams automáticamente
//            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                 ZipOutputStream zos = new ZipOutputStream(baos)) {
//                
//                for (InformeEvalDocDTO informe : listaInformeEval) {
//                    // Corrección del NullPointerException invirtiendo el .equals()
//                    if ("S".equals(informe.getItFirmado()) || informe.getItFirmado() == null) {
//                        
//                        if (informe.getCdFica() != null) {
//                            int cdFica = Integer.parseInt(informe.getCdFica());
//                            FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
//                            
//                            if (ficaFichero != null && ficaFichero.getDatosFichero() != null) {
//                                String nombreFichero = this.expedienteService.toUnicodeEscaped(ficaFichero.getNombreFichero());
//                                
//                                ZipEntry zipEntry = new ZipEntry(nombreFichero+"_"+informe.getItFirmado());
//                                zos.putNextEntry(zipEntry);
//                                zos.write(ficaFichero.getDatosFichero());
//                                zos.closeEntry(); // Cierra la entrada del archivo actual
//                                archivosAgregados++;
//                            }
//                        }
//                    }   
//                }
//                
//                // Forzamos el cierre manual interno antes de extraer los bytes del ByteArrayOutputStream
//                zos.finish();
//                zipBytes = baos.toByteArray();
//            }
//
//            // Si se procesó la lista pero ningún documento cumplió los criterios
//            if (archivosAgregados == 0) {
//                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); // O un mensaje de "no hay ficheros válidos"
//                return;
//            }
//
//            // Enviar la descarga al navegador
//            UtilesWeb.openFile(zipBytes, "zip", "Expediente_" + expediente.getIdExpedienteEval() + "_documentos");
//
//        } catch (Exception ex) {
//            log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE, ex);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
//        }
//    }
    
    public void descargarZipDocumentosFicaInforme(ExpedienteEvalDTO expediente) {
        if (expediente == null || expediente.getCdFica() == null) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            return;
        }

        try {
            List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(expediente.getIdExpedienteEval());
            
            if (listaInformeEval == null || listaInformeEval.isEmpty()) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); 
                return;
            }

            byte[] zipBytes;
            int archivosAgregados = 0;

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ZipOutputStream zos = new ZipOutputStream(baos)) {
                
                for (InformeEvalDocDTO informe : listaInformeEval) {
                    if ("S".equals(informe.getItFirmado()) || informe.getItFirmado() == null) {
                        
                        if (informe.getCdFica() != null) {
                            int cdFica = Integer.parseInt(informe.getCdFica());
                            FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
                            
                            if (ficaFichero != null && ficaFichero.getDatosFichero() != null) {
                                String nombreOriginal = ficaFichero.getNombreFichero();
                                String nombreModificado = nombreOriginal;

                                // --- CONTROL DE NOMBRES SEGÚN FIRMA ---
                                if ("S".equalsIgnoreCase(informe.getItFirmado())) {
                                    if (nombreOriginal.contains(".")) {
                                        int ultimoPunto = nombreOriginal.lastIndexOf(".");
                                        String nombreSinExt = nombreOriginal.substring(0, ultimoPunto);
                                        String ext = nombreOriginal.substring(ultimoPunto);
                                        // Ejemplo: "Informe evaluación económica_firmado.pdf"
                                        nombreModificado = nombreSinExt + "_firmado" + ext;
                                    } else {
                                        nombreModificado = nombreOriginal + "_firmado";
                                    }
                                } 
                                // Si no es "S" (es null o vacío), se queda con su nombreOriginal intacto

                                String nombreFicheroFinal = this.expedienteService.toUnicodeEscaped(nombreModificado);
                                
                                ZipEntry zipEntry = new ZipEntry(nombreFicheroFinal);
                                zos.putNextEntry(zipEntry);
                                zos.write(ficaFichero.getDatosFichero());
                                zos.closeEntry(); 
                                archivosAgregados++;
                            }
                        }
                    }   
                }
                
                zos.finish();
                zipBytes = baos.toByteArray();
            }

            if (archivosAgregados == 0) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); 
                return;
            }

            UtilesWeb.openFile(zipBytes, "zip", "Expediente_" + expediente.getIdExpedienteEval() + "_documentos");

        } catch (Exception ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE, ex);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
        }
    }
    
    
    public void descargarZipDocumentosFicaInforme(InformeEvalDocDTO informeVal) {
        if (informeVal == null || informeVal.getCdFica() == null) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
            return;
        }

        try {
            List<InformeEvalDocDTO> listaInformeEval = this.informeEvalDocDAO.obtenerListaPorExpedienteEvalSinEstado(informeVal.getIdExpedienteEval());
            
            if (listaInformeEval == null || listaInformeEval.isEmpty()) {
                // Reemplazar por una constante de "No hay documentos" si existe
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); 
                return;
            }

            byte[] zipBytes;
            int archivosAgregados = 0;

            // Uso de try-with-resources para garantizar el cierre de streams automáticamente
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ZipOutputStream zos = new ZipOutputStream(baos)) {
                
                for (InformeEvalDocDTO informe : listaInformeEval) {
                    // Corrección del NullPointerException invirtiendo el .equals()
                    if ("S".equals(informe.getItFirmado()) || informe.getItFirmado() == null) {
                        
                        if (informe.getCdFica() != null) {
                            int cdFica = Integer.parseInt(informe.getCdFica());
                            FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
                            
                            if (ficaFichero != null && ficaFichero.getDatosFichero() != null) {
                                String nombreFichero = this.expedienteService.toUnicodeEscaped(ficaFichero.getNombreFichero());
                                
                                ZipEntry zipEntry = new ZipEntry(nombreFichero);
                                zos.putNextEntry(zipEntry);
                                zos.write(ficaFichero.getDatosFichero());
                                zos.closeEntry(); // Cierra la entrada del archivo actual
                                archivosAgregados++;
                            }
                        }
                    }   
                }
                
                // Forzamos el cierre manual interno antes de extraer los bytes del ByteArrayOutputStream
                zos.finish();
                zipBytes = baos.toByteArray();
            }

            // Si se procesó la lista pero ningún documento cumplió los criterios
            if (archivosAgregados == 0) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); // O un mensaje de "no hay ficheros válidos"
                return;
            }

            // Enviar la descarga al navegador
            UtilesWeb.openFile(zipBytes, "zip", "Expediente_" + informeVal.getIdExpedienteEval() + "_documentos");

        } catch (Exception ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE, ex);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
        }
    }
    
    
    
    
    
    private void empaquetarZipInformeEvaluacion(Integer cdFicaInforme,List<ExpedienteDocEvalDTO> listaDocExpEval, ZipOutputStream zos) throws ServiceException {
        // Obtenemos los documentos subidos a FICA de cada expediente
          
           try {
           
               List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
               //Recuperamos documentos de la lista facilitada
               if(!listaDocExpEval.isEmpty()) {
                   for (ExpedienteDocEvalDTO expedienteDocEvalDTO : listaDocExpEval) {
                       Integer cdFica = expedienteDocEvalDTO.getCdFica();

                       // Recuperamos de FICA el nombre del fichero
                       FicaFichero ficaFichero=null;
                       if(cdFica != null) {
                           ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                       }
                       
                       
                       //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                       String unicodeEscapedFilename = "";
                       if (ficaFichero != null) {
                           String rawFilename = ficaFichero.getNombreFichero();
                           unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                       }

                       // Guardamos el id (cdFica) y nombre del fichero en la lista
                       DocumentoDTO documentoDTO = new DocumentoDTO();

                       documentoDTO.setCdFica(cdFica);
                       documentoDTO.setNombreFichero(unicodeEscapedFilename);
                       documentoDTO.setTipoDoc(expedienteDocEvalDTO.getIdTipoDocumento().getDsValor());

                       // Guardamos el contenido del fichero
                       if (cdFica != null) {
                           documentoDTO
                                   .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
                       }

                       lstDocumentosExpediente.add(documentoDTO);

                   }
               }
              
               
               // empezar a empaquetar
               String nombreZip="informe_evaluacion";
               ZipEntry carpetaZIP = new ZipEntry(nombreZip+ "/");
               zos.putNextEntry(carpetaZIP);
              
               FicaFichero ficaFichero=null;
               if(cdFicaInforme != null) {
                   ficaFichero = this.ficaService.recuperarMetadatos(cdFicaInforme);
               }
               
               String unicodeEscapedFilename = "";
               if (ficaFichero != null) {
                   String rawFilename = ficaFichero.getNombreFichero();
                   unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
               }

               // Guardamos el id (cdFica) y nombre del fichero en la lista
               DocumentoDTO documentoDTO = new DocumentoDTO();

               documentoDTO.setCdFica(cdFicaInforme);
               documentoDTO.setNombreFichero(unicodeEscapedFilename);
               documentoDTO.setTipoDoc(nombreZip);

               // Guardamos el contenido del fichero
               if (cdFicaInforme != null) {
                   documentoDTO
                           .setContenidoFichero(this.ficaService.recuperar(cdFicaInforme).getDatosFichero());
               }
               
               if (documentoDTO != null && documentoDTO.getContenidoFichero() != null) {
                // Insertamos el impreso
                   ZipEntry impresoZIP = new ZipEntry(nombreZip+ "/" + documentoDTO.getNombreFichero());
                   zos.putNextEntry(impresoZIP);
                   zos.write(documentoDTO.getContenidoFichero());
               } else {
                   log.error("No se ha encontrado el informe relleno de evaluación");
               }

               
               if (Boolean.FALSE.equals(lstDocumentosExpediente.isEmpty())) {

                   for (DocumentoDTO fica : lstDocumentosExpediente) {
                       // Insertamos los documentos de FICA
                       ZipEntry ficaZIP = new ZipEntry(nombreZip + "/documentos/" + fica.getCdFica()
                               + "_" + fica.getNombreFichero());
                       zos.putNextEntry(ficaZIP);
                       zos.write(fica.getContenidoFichero());
                   }

               }
           } catch (IOException e) {
               log.error(e.getMessage());
               // throw new ServiceException(e.getMessage());
           }
           
       }
    
    
    

    public void descargarZipDocumentosFica(Integer idExpedienteEval, Integer tipo) {
        if (idExpedienteEval == null) {
            log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
            return;
        }

        try {
            ExpedienteEval expedienteEval = this.expedienteEvalService.find(idExpedienteEval);
            if (expedienteEval == null) {
                log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
                return;
            }

            // Estructura segura: Todo lo que se abra aquí dentro se cerrará solo automáticamente al terminar
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean tieneDocumentos = false;
            SolicitudDTO solicitudDTO = this.solicitudService.buscarId(this.expedienteBean.getIdSolicitud());
            Integer idConvocatoria = solicitudDTO.getConvocatoriaDTO().getIdConvocatoria();
            

            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                // Set para evitar errores por nombres de archivos duplicados dentro del ZIP
                Set<String> nombresAgregados = new HashSet<>();

                    List<ExpedienteDocEvalDTO> listaexpedienteEval = this.expedienteDocEvalDAO.obtenerDocumentosExpedienteDocEval(idExpedienteEval);
                    
                    if (listaexpedienteEval != null && !listaexpedienteEval.isEmpty()) {
                        tieneDocumentos = true;
                        List<Integer> listaDocuCodigos = new ArrayList<>();  
                        
                        List<ConvocatoriaEvalTipoDocDTO> listaDocu = this.convocatoriaEvalTipoDocDAO.obtenerTipoDocumentosConvocatoriaEval(idConvocatoria, Constantes.NivelInformeEvaluacion.EVALUACION_EXPEDIENTE_DOC);
                        
                        for (ConvocatoriaEvalTipoDocDTO convocatoriaEvalTipoDocDTO : listaDocu) {
                            listaDocuCodigos.add(convocatoriaEvalTipoDocDTO.getTipoDocumentoDTO().getIdValor());
                        }
                        
                        for (ExpedienteDocEvalDTO doc : listaexpedienteEval) {
                            if (doc.getIdTipoDocumento() != null && listaDocuCodigos.contains(doc.getIdTipoDocumento().getIdValor())) {
                                Integer cdFica = doc.getCdFica();
                                if (cdFica != null) {
                                    FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
                                    
                                    if (ficaFichero != null && ficaFichero.getDatosFichero() != null) {
                                        String nombreFichero = this.expedienteService.toUnicodeEscaped(ficaFichero.getNombreFichero());
                                        
                                        while (nombresAgregados.contains(nombreFichero)) {
                                            nombreFichero = "copia_" + nombreFichero;
                                        }
                                        nombresAgregados.add(nombreFichero);

                                        ZipEntry zipEntry = new ZipEntry(nombreFichero);
                                        zos.putNextEntry(zipEntry);
                                        zos.write(ficaFichero.getDatosFichero());
                                        zos.closeEntry();
                                    }
                                }
                            }
                        }
                    }
                
                
                // Forzamos el volcado final al cerrar el try-with-resources
                zos.finish();
            } 

            // 4. Procesar la descarga Web SÓLO si se añadieron documentos
            if (tieneDocumentos) {
                byte[] zipBytes = baos.toByteArray();
                UtilesWeb.openFile(zipBytes, "zip", "Expediente " + expedienteEval.getIdExpedienteEval() + "_documentos");
            } else {
                AtlasMessageUtils.addWarnMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTEN_DOCUMENTOS); //"No hay documentos asociados"
            }

        } catch (Exception ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE, ex);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_EXPEDIENTE_EVAL_NO_EXISTE);
        }
    }
    
    
    
    /**
     * método para aprobar los informes de los coordinadores (boton verde)
     */
    public void aprobarEvaluacionGestor() {

        try {
            
            this.guardarNotaExpedienteAntesDeFinalizar();
            
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .aprobarEvaluacion(this.expedienteBean.getIdExpediente());

            this.recuperarValoresVariablesExpediente(expedienteDTO);
            this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(Boolean.FALSE);

            PrimeFaces.current().ajax().update(this.formularioEvaluacion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaEvaluacion);

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_APROBADO_OK);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_APROBADO_KO);
        }

    }

    /**
     * método para aprobar los informes de los coordinadores (boton verde)
     */
    public void finalizarInforme(ExpedienteEvalDTO coordinador) {

        try {
            
            if(coordinador != null) {
                               
                ExpedienteEval expedienteEval = this.expedienteEvalService.find(coordinador.getIdExpedienteEval());               
                expedienteEval.setEstado(new CatalogosValores (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR)));                
                this.expedienteEvalService.update(expedienteEval);
                
                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
                PrimeFaces.current().ajax().update(this.solapaEvaluacion);
                                
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_APROBADO_OK);
            }  

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_APROBADO_KO);
        }

    }
    
    
    
    /**
     * método para rechazar los informes de los coordinadores (boton rojo)
     */
    public void desaprobarEvaluacionGestor() {

        try {
            
            this.guardarNotaExpedienteAntesDeFinalizar();
            
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .rechazarEvaluacion(this.expedienteBean.getIdExpediente());

            this.recuperarValoresVariablesExpediente(expedienteDTO);
            this.expedienteBean.getExpedienteDTO()
                    .setMostrarBotoneraAprobarRechazarEvaluacion(Boolean.FALSE);

            PrimeFaces.current().ajax().update(this.formularioEvaluacion);
            PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
            PrimeFaces.current().ajax().update(this.solapaEvaluacion);

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_RECHAZADO_OK);

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Err.ERROR_RECHAZAR_INFORME_COORDINADOR_KO);
        }
    }

    /**
     * método para rechazar el informe del coordinador. (icono grilla)
     *
     * @param dto
     */
//    public void rechazarInformeCoordinador(Integer idExpedienteEval) {
//        try {
//
//            if (Objects.nonNull(idExpedienteEval)) {
//                this.expedienteEvalService.rechazarInformeCoordinador(idExpedienteEval);
//
//                this.expedienteBean.getExpedienteDTO()
//                        .setEsCerradoCoordinadorTodo(this.verificarTodoInformeCerradoCoordinador());
//                this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
//                        this.verificarMostrarBotoneraEvaluadoGestor());
//
//                CatalogosValores estadoRevInterno = this.catalogosValoresService
//                        .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION));
//
//                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
//                        .setIdValor(estadoRevInterno.getIdValor());
//                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
//                        .setDsValor(estadoRevInterno.getDsValor());
//                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
//                        .setNmConstante(estadoRevInterno.getNmConstante());
//                
//                this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);
//
//                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
//                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
//                PrimeFaces.current().ajax().update(this.solapaEvaluacion);
//
//                AtlasMessageUtils.addInfoMessageFromBundle(
//                        ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_RECHAZADO_OK);
//            }
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            AtlasMessageUtils.addErrorMessageFromBundle(
//                    ConstantesWeb.Err.ERROR_RECHAZAR_INFORME_COORDINADOR_KO);
//        }
//    }
    
    public void rechazarInformeCoordinador(InformeEvalDocDTO informe) {
        try {

            
            if (Objects.nonNull(informe)) {
                this.expedienteEvalService.rechazarInformeCoordinador(informe);

                this.expedienteBean.getExpedienteDTO()
                        .setEsCerradoCoordinadorTodo(this.verificarTodoInformeCerradoCoordinador());
                this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
                        this.verificarMostrarBotoneraEvaluadoGestor());

                CatalogosValores estadoRevInterno = this.catalogosValoresService
                        .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION));

                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setIdValor(estadoRevInterno.getIdValor());
                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setDsValor(estadoRevInterno.getDsValor());
                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setNmConstante(estadoRevInterno.getNmConstante());
                
                this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);

                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                PrimeFaces.current().ajax().update(this.solapaEvaluacion);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EVALUACION_COORDINADOR_RECHAZADO_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Err.ERROR_RECHAZAR_INFORME_COORDINADOR_KO);
        }
    }
    
    
    public void rechazarInformeEvaluador(ExpedienteEvalDTO informe) {
        try {

            
            if (Objects.nonNull(informe)) {
                this.expedienteEvalService.rechazarInformeEvaluador(informe.getIdExpedienteEval(), informe.getInformeActivo().getIdInforme().getIdInforme());

                this.expedienteBean.getExpedienteDTO()
                        .setEsCerradoCoordinadorTodo(this.verificarTodoInformeCerradoCoordinador());
                this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
                        this.verificarMostrarBotoneraEvaluadoGestor());

                CatalogosValores estadoRevInterno = this.catalogosValoresService
                        .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION));

                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setIdValor(estadoRevInterno.getIdValor());
                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setDsValor(estadoRevInterno.getDsValor());
                this.expedienteBean.getExpedienteDTO().getEstadoRevEvalDTO()
                        .setNmConstante(estadoRevInterno.getNmConstante());
                
                this.expedienteBean.getExpedienteDTO().setBAvisoEvaluacion(Boolean.FALSE);

                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                PrimeFaces.current().ajax().update(this.solapaEvaluacion);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EVALUACION_EVALUADOR_RECHAZADO_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Err.ERROR_RECHAZAR_INFORME_COORDINADOR_KO);
        }
    }
    
    /**
     * método para aprobar el informe del coordinador. (icono grilla)
     *
     * @param dto
     */
    public void aprobarInformeCoordinador(InformeEvalDocDTO informe) {
        try {

            if (Objects.nonNull(informe)) {
                this.expedienteEvalService.aprobarInformeCoordinador(informe);

                this.expedienteBean.getExpedienteDTO()
                        .setEsCerradoCoordinadorTodo(this.verificarTodoInformeCerradoCoordinador());
                this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
                        this.verificarMostrarBotoneraEvaluadoGestor());

                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                PrimeFaces.current().ajax().update(this.solapaEvaluacion);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.INFORME_COORDINADOR_APROBADO_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Err.ERROR_APROBAR_INFORME_COORDINADOR_KO);
        }
    }


//    /**
//     * método para aprobar el informe del coordinador. (icono grilla)
//     *
//     * @param dto
//     */
//    public void aprobarInformeCoordinador(Integer idExpedienteEval) {
//        try {
//
//            if (Objects.nonNull(idExpedienteEval)) {
//                this.expedienteEvalService.aprobarInformeCoordinador(idExpedienteEval);
//
//                this.expedienteBean.getExpedienteDTO()
//                        .setEsCerradoCoordinadorTodo(this.verificarTodoInformeCerradoCoordinador());
//                this.expedienteBean.getExpedienteDTO().setMostrarBotoneraAprobarRechazarEvaluacion(
//                        this.verificarMostrarBotoneraEvaluadoGestor());
//
//                PrimeFaces.current().ajax().update(this.formularioEvaluacion);
//                PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
//                PrimeFaces.current().ajax().update(this.solapaEvaluacion);
//
//                AtlasMessageUtils.addInfoMessageFromBundle(
//                        ConstantesWeb.Mensajes.INFORME_COORDINADOR_APROBADO_OK);
//            }
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            AtlasMessageUtils.addErrorMessageFromBundle(
//                    ConstantesWeb.Err.ERROR_APROBAR_INFORME_COORDINADOR_KO);
//        }
//    }

    /**
     * método para mostrar el ícono rechazar, cuando el estado es cerrado
     * coordinador
     *
     * @param dto
     * @return
     */
    public Boolean mostrarIconoAprobarRechazar(InformeEvalDocDTO dto) {
        Boolean mostrar = Boolean.FALSE;

        if (Objects.nonNull(dto) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR)
                .equals(dto.getIdEstado().getIdValor())) {
            mostrar = Boolean.TRUE;
        }

        return mostrar;
    }
    
    
    public Boolean mostrarIconoAprobarRechazarInforme(InformeEvalDocDTO dto) {
        Boolean mostrar = Boolean.FALSE;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite aprobar/rechazar informes de coordinador ni de evaluador.
        if (Objects.nonNull(dto) && !this.isExpedienteRechazadoDefinitivo()) {
            mostrar = Boolean.TRUE;
        }

        return mostrar;
    }

    public Boolean cargaIconoInforme(InformeEvalDocDTO dto) {
        Boolean mostrar = Boolean.FALSE;

        if (Objects.nonNull(dto) && (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR) 
                .equals(dto.getIdEstado().getIdValor()) || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR) 
                .equals(dto.getIdEstado().getIdValor())  || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_GESTOR) 
                .equals(dto.getIdEstado().getIdValor())  )) {
            mostrar = Boolean.TRUE;
        }

        return mostrar;
    }
    
    
    
    /**
     * Metodo para mostrar el icono de desasociar cuando el estado interno de
     * evaluacion es pendiente revisión evaluacion gestor
     *
     * @return
     */
    public Boolean mostrarIconoDesasociar(ExpedienteEvalDTO exped) {
        Boolean mostrar = Boolean.FALSE;

        // VMM 08/09/26 MD-2996 (reabierta): si el expediente está Rechazado Definitivo, no se
        // permite desasociar coordinadores.
        if (Objects.nonNull(exped) && !this.isExpedienteRechazadoDefinitivo()
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.PENDIENTE_ACEPTACION_EXPERTO)
                .equals(exped.getEstadoDTO().getIdValor())) {
            mostrar = Boolean.TRUE;
        }
        return mostrar;
    }

    
    public Boolean mostrarIconoDescargar(ExpedienteEvalDTO exped) {
        Boolean mostrar = Boolean.FALSE;

        if (Objects.nonNull(exped) && exped.getItAceptaEval() != null &&  "S".equals(exped.getItAceptaEval())) {
            mostrar = Boolean.TRUE;
        }
        return mostrar;
    }
    
    /**
     * Metodo para asociar coordinador
     *
     * @param dto
     */

    public void tramiteExpediente(ExpedienteDTO expDTO) {

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)
                .equals(expDTO.getEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)
                        .equals(expDTO.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO)
                        .equals(expDTO.getEstadoRevEvalDTO().getIdValor())) {

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_TRAMITE_ADMINISTRACION);
        }

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)
                .equals(expDTO.getEstadoRevAdminDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
                        .equals(expDTO.getEstadoRevRequiDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO)
                        .equals(expDTO.getEstadoRevEvalDTO().getIdValor())) {

            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EXPEDIENTE_ESTADO_RENUNCIA_EXPRESA);
        }

    }

    /**
     * Metodo para mostrar el comentario
     *
     * @param expEvalDTO
     */
    public void mostrarComentario(ExpedienteEvalDTO expEvalDTO) {
        if (expEvalDTO != null) {
            this.expedienteEvalDTO = expEvalDTO;
            PrimeFaces.current().executeScript("PF('dialogComentario').show();");
        }
    }

    /**
     * Metodo para cerrar el popup de comentario
     *
     */
    public void cerrarComentario() {
        this.expedienteEvalDTO = null;
        PrimeFaces.current().executeScript("PF('dialogComentario').close();");
    }
    
    
    
    public void mostrarCoordinadores() {
            PrimeFaces.current().executeScript("PF('dialogAsociarCoordinador').show();");       
    }
    
    /**
     * Metodo para cerrar el popup de comentario
     *
     */
    public void cerrarCoordinador() {
        PrimeFaces.current().executeScript("PF('dialogAsociarCoordinador').close();");
    }

    /**
     * Metodo para validar la existencia de coordinadores
     *
     */
    public boolean validarExisteCoordinador() {

        Boolean existeCoor = Boolean.FALSE;

        try {

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                    Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));

            if (this.expedienteEvalService.count(filtros) > 0) {
                existeCoor = Boolean.TRUE;
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
        return existeCoor;
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
    private void recuperarValoresVariablesExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

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

        expedienteDTO.setEsCerradoCoordinadorTodo(
                this.expedienteEvalService.esCerradoCoordinadorTodos(dto.getIdExpediente()));

        expedienteDTO.setMostrarBotoneraAprobarRechazarEvaluacion(
                this.verificarMostrarBotoneraEvaluadoGestor());

        this.expedienteBean.setExpedienteDTO(expedienteDTO);

    }

    public boolean esRechazado(ExpedienteEvalDTO expedienteEvalDTO) {
        if (expedienteEvalDTO.getCausaDTO() != null
                && !expedienteEvalDTO.getCausaDTO().getDsValor().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void mostrarMotivoRechazoEval(ExpedienteEvalDTO expedienteEvalDTO) {
        if (expedienteEvalDTO.getCausaDTO() != null
                && !expedienteEvalDTO.getCausaDTO().getDsValor().isEmpty()) {
            this.motivoCausaSeleccionado = expedienteEvalDTO.getInformeActivo().getIdCausa().getDsValor() +"_"+  expedienteEvalDTO.getInformeActivo().getDsObsRechazoInfEval();
        } else {
            this.motivoCausaSeleccionado = AtlasMessageUtils
                    .getStringFromBundle(ConstantesWeb.Mensajes.MOTIVO_RECHAZO_VACIO_EVAL);
        }
        log.info("motivoCausaSeleccionado: " + this.motivoCausaSeleccionado);
        PrimeFaces.current().executeScript("PF('dialogMotivoRechazo').show();");

    }

    /**
    *
    */
    public void cerrarMotivoRechazo() {
        this.motivoCausaSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogMotivoRechazo').close();");
    }
    
    /** Garantiza que la nota del expediente quede guardada antes de finalizar la evaluación. */
    private void guardarNotaExpedienteAntesDeFinalizar() throws ServiceException {
        ExpedienteDTO expedienteDTO = this.expedienteBean.getExpedienteDTO();
        if (expedienteDTO.getNmNota() == null) {
            expedienteDTO.setNmNota(0);
        }
        this.expedienteService.guardarNotaEvaluacion(
                expedienteDTO.getIdExpediente(), expedienteDTO.getNmNota());
    }

}
