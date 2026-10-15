package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ExpedienteEvalJusTecHistDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEvalJustecDTO;
import org.madrid.saii.lib.dto.ConvocatoriaJusTecDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.services.ConvocatoriaEvalJustecService;
import org.madrid.saii.lib.services.ConvocatoriaJusTecService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteEvalJusTecHistService;
import org.madrid.saii.lib.services.ExpedienteEvalJusTecService;
import org.madrid.saii.lib.services.ExpedienteEvalService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.InteresadoService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.SolicitudService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.componentes.core.utiles.AtlasMessageUtils;
import atlas.componentes.listapaginada.AtlasListAndCount;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author quental
 *
 */

@Slf4j
@Getter
@Setter
@Component("evaluacionJusTecBean")
@Scope("request")
public class EvaluacionJusTecBean extends CatalogoBean<EvaluacionJustec, Integer>{

    /**
     * Servicio de InteresadoService
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
    
    /**
     * Servicio de ExpedienteEvalJusTecService
     */
    @Autowired
    @Qualifier("expedienteEvalJusTecService")
    private ExpedienteEvalJusTecService expedienteEvalJusTecService;
    
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;
    
    @Autowired
    @Qualifier("convocatoriaJusTecService")
    private ConvocatoriaJusTecService convocatoriaJusTecService;
    
    
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;
    
    
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJustecService;
    
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    
    @Autowired
    @Qualifier("convocatoriaEvalJustecService")
    private ConvocatoriaEvalJustecService convocatoriaEvalJustecService;
    
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;
    
    /**
     * Servicio de Pdf
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;
    
    /** Servicio de Expediente */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    /**
     * Servicio de ExpedienteEvalJusTecHistService
     */
    @Autowired
    @Qualifier("expedienteEvalJusTecHistService")
    private ExpedienteEvalJusTecHistService expedienteEvalJusTecHistService;
    
    @Autowired
    @Qualifier("expedienteEvalJusTecHistDAO")
    private ExpedienteEvalJusTecHistDAO expedienteEvalJusTecHistDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
  
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /** idConvocatoria */
    private Integer idConvocatoria;
    
    /** InteresadoSelect */
    private InteresadoDTO interesadoSelect;
    
    /** filtroEvaluadorJusTec */
    private InteresadoDTO filtroEvaluadorJusTec;
    
    /** InteresadoSelect */
    private ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO;
    
    /** listaConvJusTec */
    private List<ConvocatoriaJusTecDTO> listaConvJusTecDTO ;
    
    /** listaEvaluadoresAsignadosExpediente */
    private List<ExpedienteEvalJusTecDTO> listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
  
    /** idExpediente */
    private Integer idExpediente;
    
    /** listaExpedientes */
    private List<ExpedienteJusTecDTO> listaExpedientes;
    
    /** expedienteJusTecSeleccionado */
    private ExpedienteJusTecDTO expedienteJusTecSeleccionado;
    
    /** expedienteJusTecSeleccionado */
    private ExpedienteEvalJusTecDTO expedienteEvalJusTecSeleccionado;
    
    
    /** listaExpEvaluadoresEvaluacionJusTecDTO */
    private List<ExpedienteEvalJusTecDTO> listaExpEvaluadoresEvaluacionJusTecDTO;
    
    /** Campo dsComentario */
    private String dsComentario;
    
    /** expedienteJusTecSeleccionado */
    private ExpedienteEvalJusTecDTO evaluadorSeleccionado;
  
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////

    private ExpedienteBean expedienteBean;
    

    public EvaluacionJusTecBean() {
        super();
    }
    
    @PostConstruct
    public void init() {
        inicializarEvaluacion();
        this.filtroEvaluadorJusTec = new InteresadoDTO();
        this.setModoConsulta(Boolean.FALSE);
    }
    
    private void inicializarEvaluacion() {
        this.filtroEvaluadorJusTec = new InteresadoDTO();
        this.expedienteEvalJusTecDTO = new ExpedienteEvalJusTecDTO();
    }
    
    public void cargarDatos() throws ServiceException {

         if(this.expedienteBean.getIdExpediente() != null) {
             List<ExpedienteJusTecDTO> listExpedienteJusTecDTO = this.expedienteJustecService.buscarByIdExp(this.expedienteBean.getIdExpediente());
         }
         List<ExpedienteEvalJusTec> listEvaluadoresJCT = this.expedienteEvalJusTecService.getDao().findAll();
    }
    
    public void seleccionarPeriodo(Object object) {
        try {
            this.expedienteJusTecSeleccionado = (ExpedienteJusTecDTO) object;
            this.idExpediente = expedienteJusTecSeleccionado.getIdExpediente();
            
            this.listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
            
            if (this.expedienteJusTecSeleccionado != null) {
                Integer idExpedienteJusTec = this.expedienteJusTecSeleccionado.getIdExpedienteJusTec();
                
                List<AtlasFilter> filtros = new ArrayList<>();
                List<AtlasSort> orden = new ArrayList<>();

                List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, idExpedienteJusTec);
                
                if(!list.isEmpty()){
                    for(int a = 0; a<list.size();a++) {
                        this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                    }
                }
            }
 
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_EVALUADORES_PORPERIODO);
           
        } catch (Exception exc) {
            log.error("Error al obtener los evaluadores asignados a la Justif. CT por periodo seleccionado.", exc);
            AtlasMessageUtils
                    .addErrorMessage("Error al obtener los evaluadores asignados a la Justif. CT por periodo seleccionado");
        }
    }

    /**
     * Obtener la lista de evaluaciones justifico técnicas asignado por idExpediente y itCoorEval
     * @param listExpedienteEvalJusTecDTO 
     * 
     * @return List<ExpedienteEvalDTO>
     */
    public List<ExpedienteEvalJusTecDTO> obtenerListaEvalJusTec(List<ExpedienteEvalJusTecDTO> listExpedienteEvalJusTecDTO) {
        
        Integer idExpedienteJusTec = null;
        
        if (!listExpedienteEvalJusTecDTO.isEmpty()) {
            for(int i = 0; i<listExpedienteEvalJusTecDTO.size();i++) {
                idExpedienteJusTec = listExpedienteEvalJusTecDTO.get(i).getExpedienteJustecDTO().getIdExpedienteJusTec();
            }
        }
        
        List<AtlasFilter> filtros = new ArrayList<>();
        List<AtlasSort> orden = new ArrayList<>();
        List<ExpedienteEvalJusTecDTO> resultados = new ArrayList<>();

        try {
            if (idExpedienteJusTec != null) {
                resultados = this.expedienteEvalJusTecService.buscar(-1, 10,
                         filtros, orden, idExpedienteJusTec);
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return resultados;
    }
    
    /**
     * Obtener la lista de evaluaciones justifico técnicas asignado por idExpediente y itCoorEval
     *
     * @param index
     * @param pageSize
     * @param ordenacion
     * @param filter
     * @return AtlasListAndCount<ExpedienteEvalDTO>
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<InteresadoDTO> obtenerListaEvaluadores(int index, int pageSize,
            Object ordenacion, Object filter) {

        AtlasListAndCount<InteresadoDTO> listado = new AtlasListAndCount<>();
        try {
            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            
            if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                filtros = this.getFiltros();
            }
            
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;
            
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                    Constantes.TipoCoorEval.INTERESADO_IT_COOR_EVAL, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            
            List<InteresadoDTO> resultados= this.interesadoService.buscar(index, pageSize,
                    filtros, orden);
            listado.setResultados(resultados);
            listado.setTotal(resultados.size());
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }

    /**
     * Borra e inicializa la variable filtroEvaluadorJusTec
     */
    public void borrarFiltros() {
        this.setFiltros(new ArrayList<>());
        this.filtrar();
        this.filtroEvaluadorJusTec = new InteresadoDTO();
    }

   
    /**
     * Crea una nueva instancia de la entidad EvaluacionJustec
     *
     * @return entidad de tipo EvaluacionJustec
     */
    @Override
    public EvaluacionJustec crearEntidad() {
        return null;
    }
    
    
    /**
     * Método invocado desde el boton 'filtrar' de la página. Rellena la
     * propiedad 'filtros', que llegará como parámetro a los métodos
     * obtenerLista y obtenerTotal que alimentan la listaPaginada. <br/>
     *
     * @param convocatoriaActiva: Si este parametro esta a true, se tiene en
     *            cuenta si la convocatoria esta activa o no. Si esta a false,
     *            no se tiene en cuenta y pueden salir convocatorias activas e
     *            inactivas
     */
    
    public void filtrar() {
        this.setFiltros(new ArrayList<>());

        if ( this.filtroEvaluadorJusTec.getCdNumIden() != null) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN,
                    "%" + this.filtroEvaluadorJusTec.getCdNumIden() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if ( StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsNombre())) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_DSNOMBRE,
                    "%" + this.filtroEvaluadorJusTec.getDsNombre() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if ( StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsApellido1())) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_DSAPELLIDO1,
                    "%" + this.filtroEvaluadorJusTec.getDsApellido1() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if ( StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsApellido2())) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_DSAPELLIDO2,
                    "%" + this.filtroEvaluadorJusTec.getDsApellido2() + "%", Operador.LIKE_INSENSITIVE));
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteJusTecDTO> obtenerListaPeriodoExp(final int index,
            final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpedienteJusTecDTO> listaExp = new AtlasListAndCount<>();

        try {
            if (this.idExpediente == null)
                this.cargarInformacion();
            if (this.idExpediente != null) {
                List<AtlasFilter> filtrosEntrada = filtros != null ? (List<AtlasFilter>) filtros
                        : new ArrayList<>();

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        this.idExpediente, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<AtlasSort> ordenamiento = orderBean != null ? (List<AtlasSort>) orderBean
                        : new ArrayList<>();
                ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_IDEXPEDIENTE, true));
                ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

                this.listaExpedientes = expedienteJusTecService.buscar(index, pageSize,
                        filtrosEntrada, ordenamiento);
                listaExp.setResultados(this.listaExpedientes);
                listaExp.setTotal(this.listaExpedientes.size());
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

        return listaExp;
    }
    
    public void cargarInformacion() {
        if (Objects.isNull(this.idExpediente) && !Objects.isNull(this.expedienteBean)) {
            this.idExpediente = this.expedienteBean.getIdExpediente();
        }

    }
    
    /**
     * Método para obtener la lista personas evaluadoras asociadas a la
     * justificacion cientifico tecnica
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<ExpedienteEvalJusTecDTO> Lista Evaluadores
     * @throws ServiceException 
     */
    public AtlasListAndCount<ExpedienteEvalJusTecDTO> obtenerListaEvaluadoresJustecCt(int index, int pageSize,
            Object ordenacion, Object filter) throws ServiceException {

        AtlasListAndCount<ExpedienteEvalJusTecDTO> lista = new AtlasListAndCount<ExpedienteEvalJusTecDTO>();
  
        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            List<AtlasSort> orden = new ArrayList<>();
            List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJusTecDTO = new ArrayList<>();
            
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMPERIODO,
                    this.expedienteJusTecSeleccionado.getNmPeriodo(), Operador.EQUALS));
           
            listaExpedienteEvalJusTecDTO = this.expedienteEvalJusTecService.buscar(-1, 10,
                   filtros, orden,  this.idExpediente);
            
            this.expedienteEvalJusTecService.buscarEvaluadoresJCT(0, -1, filtros,
                    orden, listaExpedienteEvalJusTecDTO);

            // obtener los registros
            lista.setResultados(
                    this.expedienteEvalJusTecService.retornarPaginado(listaExpedienteEvalJusTecDTO, index, pageSize));

            // obtener el total de registros (numero)
            lista.setTotal(listaExpedienteEvalJusTecDTO.size());

        } catch (ServiceException se) {
            log.error(se.getMessage());
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR_CARGAR_PERSONA);
        }
        return lista;
    }
    
    /**
     * Metodo para asociar Evaluador a la evaluacion JCT
     *
     * @param dto
     */
    public void asociarEvaluadorJustecCt(ConvocatoriaEvalJustecDTO dto) {

        try {
            if (Boolean.FALSE.equals(this.validarFormularioAsociadorEvaluadorEvaluacionJCT(dto))) {

                ExpedienteEvalJusTec expedienteEvalJusTec = new ExpedienteEvalJusTec();
                
                if (this.expedienteJusTecSeleccionado.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION))){
                            
                        Integer idInteresado = dto.getInteresadoDTO().getIdInteresado();
                        Interesado interesado = new Interesado();
                        interesado.setIdInteresado(idInteresado);
                        expedienteEvalJusTec.setPersona(interesado);
                        expedienteEvalJusTec.setCdUsuAlta(UserUtils.getUserName());
                        expedienteEvalJusTec.setFcAltaFila(new Date());
                        expedienteEvalJusTec.setItNotificado(Constantes.AfirmacionNegacion.N);
                        CatalogosValores estadoAux = new CatalogosValores();
                        estadoAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION));
                        estadoAux.setNmConstante(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION);
                        expedienteEvalJusTec.setEstado(estadoAux);
                        ExpedienteJusTec expedienteJustecAux = this.expedienteJustecService.buscarIdExpJustec(this.expedienteJusTecSeleccionado.getIdExpedienteJusTec());
                        expedienteEvalJusTec.setExpedienteJustec(expedienteJustecAux);
                      
                        this.expedienteEvalJusTecService.insert(expedienteEvalJusTec);
                        
                  }else {
                            
                        Integer idInteresado = dto.getInteresadoDTO().getIdInteresado();
                        Interesado interesado = new Interesado();
                        interesado.setIdInteresado(idInteresado);
                        expedienteEvalJusTec.setPersona(interesado);
                        expedienteEvalJusTec.setCdUsuAlta(UserUtils.getUserName());
                        expedienteEvalJusTec.setFcAltaFila(new Date());
                        expedienteEvalJusTec.setItNotificado(Constantes.AfirmacionNegacion.N);
                        CatalogosValores estadoAux = new CatalogosValores();
                        estadoAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO));
                        estadoAux.setNmConstante(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO);
                        expedienteEvalJusTec.setEstado(estadoAux);
                        ExpedienteJusTec expedienteJustecAux = this.expedienteJustecService.buscarIdExpJustec(this.expedienteJusTecSeleccionado.getIdExpedienteJusTec());
                        expedienteEvalJusTec.setExpedienteJustec(expedienteJustecAux);
                  
                        this.expedienteEvalJusTecService.insert(expedienteEvalJusTec);
                     
                   }
       
                listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
                
                Integer idExpedienteJusTec = this.expedienteJusTecSeleccionado.getIdExpedienteJusTec();
                    
                 List<AtlasFilter> filtros = new ArrayList<>();
                 List<AtlasSort> orden = new ArrayList<>();
                 
                 List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, idExpedienteJusTec);
                    
                 if(!list.isEmpty()){
                     for(int a = 0; a<list.size();a++) {
                         this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                        }
                     }
            
                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);

                AtlasMessageUtils
                        .addInfoMessageFromBundle(ConstantesWeb.Mensajes.ASIGNADO_CORRECTAMENTE);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUADOR_YA_ASIGNADO);
        }

    }
    
    /**
     * Método para validarFormularioAsociadorEvaluadorEvaluacionJCT
     *
     *
     * @return Boolean existeError
     * @throws ServiceException 
     */

    private Boolean validarFormularioAsociadorEvaluadorEvaluacionJCT(ConvocatoriaEvalJustecDTO dto) throws ServiceException {
        
       Integer IdExpediente = this.expedienteJusTecSeleccionado.getIdExpediente();
      
       List<ExpedienteJusTecDTO> listPeriodosExpdienteEval = this.expedienteJusTecService.buscarByIdExp(IdExpediente);
        
        Boolean existeError = Boolean.FALSE;
        try {
            if (Boolean.TRUE.equals(this.expedienteEvalJusTecService.existePersonaExpedienteEvalJusTecPorExpedienteJustec(dto.getInteresadoDTO().getIdInteresado(), listPeriodosExpdienteEval))) {
                existeError = Boolean.TRUE;
                AtlasMessageUtils.addErrorMessageFromBundle(
                       ConstantesWeb.Mensajes.ERROR_EVALUADOR_ASOCIADO_EXPEDIENTE_PERIODO);
            }
            if (Boolean.TRUE.equals(this.expedienteEvalService.existePersonaExpedienteEval(dto.getInteresadoDTO().getIdInteresado(), IdExpediente))) {
                existeError = Boolean.TRUE;
                AtlasMessageUtils.addErrorMessageFromBundle(
                       ConstantesWeb.Mensajes.ERROR_EVALUADOR_ASOCIADO_EXPEDIENTE_PERIODO);
            }
            if (Boolean.TRUE.equals(this.expedienteEvalService.existePersonaExpedienteEvalComoCoordinador(dto.getInteresadoDTO().getIdInteresado(), IdExpediente))) {
                existeError = Boolean.TRUE;
                AtlasMessageUtils.addErrorMessageFromBundle(
                       ConstantesWeb.Mensajes.ERROR_EVALUADOR_ASOCIADO_EXPEDIENTE_PERIODO);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_ASOCIAR_EVALUADOR_ASOCIADO_EXPEDIENTE);
        }
        return existeError;
    }

    /**
     * Metodo para desasociar un evaluador de la evaluacion justif CT
     *
     * @param ExpedienteEvalJusTecDTO dto
     */

    public void desasociarEvaluadorJustecCt(ExpedienteEvalJusTecDTO dto) {
        try {
            if (dto != null) {

                this.expedienteEvalJusTecService.deleteLogico(dto.getIdExpedienteEvalJusTec());

                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);
                
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.MSG_EVALUADOR_BAJA_EVALUADOR_OK);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.MSG_EVALUADOR_BAJA_EVALUADOR_KO);
        }
    } 
    
    /**
     * Borrar filtroPersona
     */
    public void borrarFiltroPersonaEvaluador() {
        this.filtroEvaluadorJusTec = new InteresadoDTO();
    }
    
    /**
     * Método para obtener la lista persona evaluador
     *
     * @param index Indice de registro
     * @param pageSize Total de páginas del listado
     * @param orderBean Campos para ordernar el listado
     * @param filtros Filtros del listado
     * @return AtlasListAndCount<InteresadoDTO> Lista de Interesado
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ConvocatoriaEvalJustecDTO> obtenerListaPersonaEvaluadorEvaluacionJCT(int index, int pageSize,
            Object ordenacion, Object filter) {
   
        AtlasListAndCount<ConvocatoriaEvalJustecDTO> lista = new AtlasListAndCount<ConvocatoriaEvalJustecDTO>();
        
        List<ConvocatoriaEvalJustecDTO> listaFinalEvaluadoresParaAsignar = new ArrayList<>();
    
        try {

            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                    Constantes.AfirmacionNegacion.S, Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            if (StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getCdNumIden())) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN,
                        "%" + this.filtroEvaluadorJusTec.getCdNumIden().trim() + "%",
                        Operador.LIKE_INSENSITIVE));
            }
            if (StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsNombre())) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSNOMBRE,
                        "%" + this.filtroEvaluadorJusTec.getDsNombre().trim() + "%",
                        Operador.LIKE_INSENSITIVE));
            }
            if (StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsApellido1())) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSAPELLIDO1,
                        "%" + this.filtroEvaluadorJusTec.getDsApellido1().trim() + "%",
                        Operador.LIKE_INSENSITIVE));
            }

            if (StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getDsApellido2())) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSAPELLIDO2,
                        "%" + this.filtroEvaluadorJusTec.getDsApellido2().trim() + "%",
                        Operador.LIKE_INSENSITIVE));
            }

            if (StringUtils.isNotBlank(this.filtroEvaluadorJusTec.getCdUsuario())) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUSUARIO,
                        "%" + this.filtroEvaluadorJusTec.getCdUsuario().trim() + "%",
                        Operador.LIKE_INSENSITIVE));
            }
   
            List<AtlasFilter> filtroAuxCovEvalJustec = new ArrayList<>();
            filtroAuxCovEvalJustec.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                    idConvocatoria, Operador.EQUALS));
            filtroAuxCovEvalJustec.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
         
            List<ConvocatoriaEvalJustecDTO> listConvEvalJustecDTOAux = this.convocatoriaEvalJustecService.buscarPersona(index, pageSize, filtroAuxCovEvalJustec, orden , filtroEvaluadorJusTec.getIdAreaCientifica());
            
            List<InteresadoDTO> interesados = this.interesadoService.buscarPersona(0, -1, filtros,
                    orden, this.filtroEvaluadorJusTec.getIdAreaCientifica());
            
            for(int i = 0; i<interesados.size();i++) {
                for(int y = 0; y<listConvEvalJustecDTOAux.size();y++) {
                    Integer idInteresado = listConvEvalJustecDTOAux.get(y).getInteresadoDTO().getIdInteresado();
                    if (interesados.get(i).getIdInteresado().equals(idInteresado)) {
                        ConvocatoriaEvalJustecDTO convocatoriaEvalJustecDTO = listConvEvalJustecDTOAux.get(y);
                        listaFinalEvaluadoresParaAsignar.add(convocatoriaEvalJustecDTO);
                    }
                }
            }
    
            // obtener los registros
            lista.setResultados(
                    this.convocatoriaEvalJustecService.retornarPaginado(listaFinalEvaluadoresParaAsignar, index, pageSize));

            // obtener el total de registros (numero)
            lista.setTotal(listaFinalEvaluadoresParaAsignar.size());

        } catch (ServiceException se) {
            log.error(se.getMessage());
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR_CARGAR_PERSONA);
        }

        return lista;
    }
    
    /**
     * Metodo para guardar los datos editados del registro de la
     * ExpedienteEvalJusTec
     *
     * @param event
     */
    public void guardarCambios() {

        try {
       
            if (Boolean.TRUE.equals(this.verificarFechas(this.evaluadorSeleccionado))) {
            
//                Integer idExpedienteJusTec = expedienteEvalJusTecDTO.getExpedienteJustecDTO().getIdExpedienteJusTec();
            
                List<ExpedienteEvalJusTec> listRegistroEditadoGrabar = this.expedienteEvalJusTecService.getDao().buscarExpEvalJCT(this.evaluadorSeleccionado.getExpedienteJustecDTO().getIdExpedienteJusTec());
     
                for(int i = 0; i<listRegistroEditadoGrabar.size();i++) {
                
                    if (this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getInteresadoDTO().getIdInteresado().equals(this.evaluadorSeleccionado.getInteresadoDTO().getIdInteresado())){
                   
                        if (Boolean.TRUE.equals(this.verificarFechas(this.evaluadorSeleccionado))) {
                            
                            if (this.evaluadorSeleccionado.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO))) {
                                ExpedienteEvalJusTec registroEditadoGrabar = listRegistroEditadoGrabar.get(i);
                                registroEditadoGrabar.setFcFinPlazo(this.evaluadorSeleccionado.getFcFinPlazo());
                                CatalogosValores estadoAux = new CatalogosValores();
                                estadoAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION));
                                estadoAux.setNmConstante(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION);
                                registroEditadoGrabar.setFcFinPlazo(this.evaluadorSeleccionado.getFcFinPlazo());
                                registroEditadoGrabar.setEstado(estadoAux);
                                registroEditadoGrabar.setItNotificado(Constantes.AfirmacionNegacion.S);
                                this.expedienteEvalJusTecService.guardarCambiosRegistro(registroEditadoGrabar);
                                this.expedienteEvalJusTecHistDAO.crearHistoricoExpEvalJus(registroEditadoGrabar);
                                
                            }else {
                                ExpedienteEvalJusTec registroEditadoGrabar = listRegistroEditadoGrabar.get(i);
                                registroEditadoGrabar.setFcIniPlazo(this.evaluadorSeleccionado.getFcIniPlazo());
                                registroEditadoGrabar.setFcFinPlazo(this.evaluadorSeleccionado.getFcFinPlazo());
                                if (this.evaluadorSeleccionado.getItNotificado().equals(Constantes.AfirmacionNegacion.N)) {
                                    registroEditadoGrabar.setItNotificado(Constantes.AfirmacionNegacion.S);
                                }
                                this.expedienteEvalJusTecService.guardarCambiosRegistro(registroEditadoGrabar);
                                this.expedienteEvalJusTecHistDAO.crearHistoricoExpEvalJus(registroEditadoGrabar);
                         
                            }
                        }
                    }
                }
                
                listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
                    
                List<AtlasFilter> filtros = new ArrayList<>();
                List<AtlasSort> orden = new ArrayList<>();
                 
                List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, this.evaluadorSeleccionado.getExpedienteJustecDTO().getIdExpedienteJusTec());
                    
                if(!list.isEmpty()){
                    for(int a = 0; a<list.size();a++) {
                        this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                     }
               }
    
               AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                
            }   
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
    *
    * @param dto
    * @return
    */
    private Boolean verificarFechas(ExpedienteEvalJusTecDTO dto) {
       Boolean esValido = Boolean.TRUE;

       if (Boolean.FALSE.equals(this.verificarRangoFechas(dto))) {
           esValido = Boolean.FALSE;
       } 

       return esValido;
   }
   
   /**
   *
   * @param dto
   * @return
   */
   private Boolean verificarRangoFechas(ExpedienteEvalJusTecDTO dto) {
      Boolean esValido = Boolean.TRUE;

      //si la fecha de fin de ejecucion es mayor o igual a la de inicio
      if (dto.getFcFinPlazo().before(dto.getFcIniPlazo())) {
          AtlasMessageUtils.addErrorMessageFromBundle(
                  ConstantesWeb.Mensajes.EXPEDIENTE_JUSTEC_FEC_EJECUCION_ERROR);
          esValido = Boolean.FALSE;
      }
     return esValido;
  }
   
  public Boolean mostrarEvaluadoresEvaluacionJCT() {
       
       Boolean response = false;
       if (this.expedienteJusTecSeleccionado != null) {
           response = Boolean.TRUE;
       }
          
    return response;
   }
  
  /**
   * Metodo que controla la descarga de PDF del servicio EREG
   *
   *
   */
  public void descargarExpedienteEvaluacionJustecCT(Integer idExpedienteEvalJusTec) {

      try {
          if (idExpedienteEvalJusTec != null) {
              DocumentoDTO documentoDTO = this.pdfService.descargarZipExpedienteEvaluacionJustecCT(idExpedienteEvalJusTec);
              ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(this.expedienteBean.getExpedienteDTO().getIdExpediente());
              
              if (documentoDTO != null) {
                  log.info("Descargar expediente evaluador Justec. CT " + documentoDTO.getNombreFichero());

                  // Se prepara el fichero.
                  UtilesWeb.openFile(documentoDTO.getContenidoFichero(), "zip",
                          documentoDTO.getNombreFichero().replace(".zip", ""));
              } else {
                  AtlasMessageUtils.addErrorMessageFromBundle(
                          ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
                  log.error("No se ha podido encontrar los documentos a descargar");
              }
          } else {
              AtlasMessageUtils.addErrorMessageFromBundle(
                      ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
          }

      } catch (ServiceException e) {
          log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);

          AtlasMessageUtils.addErrorMessageFromBundle(
                  ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
      }
  }
  
  /**
   * Método para mostrar la ventana popup con el histórico de estados de una
   * evaluacion
   *
   * @param documento Datos del documento
 * @throws ServiceException 
   */
   public void mostrarHistoricoExpedienteEvalJustecCT(Integer idExpedienteEvalJusTec) throws ServiceException {
       
       try {  
           this.expedienteEvalJusTecSeleccionado = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
           if (this.expedienteEvalJusTecSeleccionado != null) {
               PrimeFaces.current().executeScript("PF('dialogHistoricoExpedienteEvalJustecCT').show();");
           }
       } catch (ServiceException e) {
       log.error(e.getMessage());
       }

   }
   
   /**
    * Método para cerrar la ventana popup de históricos de estados de documento
    */
   public void cerrarHistoricoExpedienteEvalJustecCT() {
       this.expedienteEvalJusTecSeleccionado = null;
       PrimeFaces.current().executeScript("PF('dialogHistoricoExpedienteEvalJustecCT').hide();");
   }

   /**
    * Método para obtener la lista de históricos de estados de un documento
    *
    * @param index Indice
    * @param pageSize Cantidad páginas
    * @param orderBean Ordenamiento
    * @param filtros Filtros
    * @return Lista de histórico de estados de documento
 * @throws ServiceException 
    */
   public AtlasListAndCount<ExpedienteEvalJusTecHistDTO> obtenerListaHistoricoExpedienteEvalJustecCT(final int index,
           final int pageSize, Object orderBean, Object filtros) throws ServiceException {
       
       AtlasListAndCount<ExpedienteEvalJusTecHistDTO> listadoDatos = new AtlasListAndCount<>();
       
       for(int i = 0; i<this.listaEvaluadoresAsignadosExpedienteJusTec.size();i++) {
           Integer idExpedienteEvalJusTec = this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getIdExpedienteEvalJusTec();
           Integer idPersona = this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getInteresadoDTO().getIdInteresado();
           this.expedienteEvalJusTecSeleccionado = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
       
           if (this.expedienteEvalJusTecSeleccionado != null) {
               
               List<AtlasFilter> filtrosAux = new ArrayList<>();
               List<AtlasSort> orden = new ArrayList<>();
       
               List<ExpedienteEvalJusTecHistDTO> listado = this.expedienteEvalJusTecHistService.buscarHistoricoEvalJCT(index, pageSize, filtrosAux, orden, idExpedienteEvalJusTec, idPersona);
               listadoDatos.setResultados(listado);
               listadoDatos.setTotal(listado.size());
           }
       }
       return listadoDatos;
   }
  
   
   
    /**
    * Método para cerrar la ventana popup de observaciones
    */
    public void cerrarObservaciones() {
           this.dsComentario = null;
           PrimeFaces.current().executeScript("PF('dialogObservaciones').hide();");

    }
    
    /**
     * método para aprobar la evaluacion justec CT 
     */
    public void aprobarEvaluacionJCT() {
        
        ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO = new ExpedienteEvalJusTecDTO();
        
        if (this.evaluadorSeleccionado != null) {
            try {
                
                expedienteEvalJusTecDTO = this.expedienteEvalJusTecService.buscarId(this.evaluadorSeleccionado.getIdExpedienteEvalJusTec());
                
                CatalogosValoresDTO estadoDTOAux = new CatalogosValoresDTO();
                estadoDTOAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR));
                estadoDTOAux.setNmConstante(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR);
                expedienteEvalJusTecDTO.setEstadoDTO(estadoDTOAux);
                expedienteEvalJusTecDTO.setItNotificado(Constantes.AfirmacionNegacion.N);
                expedienteEvalJusTecDTO.setCdUsuModif(UserUtils.getUserName());
                expedienteEvalJusTecDTO.setFcModifFila(new Date());
                expedienteEvalJusTecDTO.setDsComentario(this.evaluadorSeleccionado.getDsComentario());
                expedienteEvalJusTecDTO.setObsGest(this.evaluadorSeleccionado.getObsGest());
                
                this.expedienteEvalJusTecService.guardarExpEvalJusTecCt(expedienteEvalJusTecDTO);
                
                listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
                
                Integer idExpedienteJusTec = this.expedienteJusTecSeleccionado.getIdExpedienteJusTec();
                    
                 List<AtlasFilter> filtros = new ArrayList<>();
                 List<AtlasSort> orden = new ArrayList<>();
                 
                 List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, idExpedienteJusTec);
                    
                 if(!list.isEmpty()){
                     for(int a = 0; a<list.size();a++) {
                         this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                      }
                }
                
                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EVALUACION_EXPEDIENTE_JUSTEC_APROBADO_OK);
          
            } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.EVALUACION_EXPEDIENTE_JUSTEC_APROBADO_KO);
        }
            
      }

    }
    
    /**
     * método para desaprobar evaluacion justec CT 
     */
    public void desAprobarEvaluacionJCT() {
        
        ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO = new ExpedienteEvalJusTecDTO();
        
        if (this.evaluadorSeleccionado != null) {
            try {
                
                expedienteEvalJusTecDTO = this.expedienteEvalJusTecService.buscarId(this.evaluadorSeleccionado.getIdExpedienteEvalJusTec());
                
                CatalogosValoresDTO estadoDTOAux = new CatalogosValoresDTO();
                estadoDTOAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION));
                estadoDTOAux.setNmConstante(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION);
                expedienteEvalJusTecDTO.setEstadoDTO(estadoDTOAux);
                expedienteEvalJusTecDTO.setItNotificado(Constantes.AfirmacionNegacion.S);
                expedienteEvalJusTecDTO.setCdUsuModif(UserUtils.getUserName());
                expedienteEvalJusTecDTO.setFcModifFila(new Date());
                expedienteEvalJusTecDTO.setDsComentario(this.evaluadorSeleccionado.getDsComentario());
                
                this.expedienteEvalJusTecService.guardarExpEvalJusTecCt(expedienteEvalJusTecDTO);
                
                listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
                
                Integer idExpedienteJusTec = this.expedienteJusTecSeleccionado.getIdExpedienteJusTec();
                    
                 List<AtlasFilter> filtros = new ArrayList<>();
                 List<AtlasSort> orden = new ArrayList<>();
                 
                 List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, idExpedienteJusTec);
                    
                 if(!list.isEmpty()){
                     for(int a = 0; a<list.size();a++) {
                         this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                     }
                }
  
                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.EVALUACION_EXPEDIENTE_JUSTEC_RECHAZADO_OK);
          
            } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.ERROR_RECHAZAR_EVALUACION_EXPEDIENTE_JUSTEC_KO);
        }
            
      }

    }
    
    /**
     * Metodo para desasociar un evaluador de la evaluacion Jct
     *
     * @param idExpedienteEvalJusTec
     */
    public void eliminarEvaluadorEvaluacionJCT(Integer idExpedienteEvalJusTec) throws ServiceException {

        try {
            if (idExpedienteEvalJusTec != null) {
                
                List<ExpedienteEvalJusTec> listExpedienteEvalJusTec = new ArrayList<>();
                listExpedienteEvalJusTec = this.expedienteEvalJusTecService.getDao().buscarExpEvalJCT(this.expedienteJusTecSeleccionado.getIdExpedienteJusTec());
    
                this.expedienteEvalJusTecService.deleteLogico(idExpedienteEvalJusTec);
                
                List<ExpedienteEvalJusTecHist> listaExpEvalJusTecHist = new ArrayList<>();
     
                for(int i = 0; i<listExpedienteEvalJusTec.size();i++) {
                    
                    Integer idExpedienteJusTec = listExpedienteEvalJusTec.get(i).getExpedienteJustec().getIdExpedienteJusTec();
                    Integer idPersona = listExpedienteEvalJusTec.get(i).getPersona().getIdInteresado();
                    
                    listaExpEvalJusTecHist = this.expedienteEvalJusTecHistService.getDao().buscarPorIdExpedienteJusTecAndPersona(idExpedienteJusTec, idPersona);
                    
                    for(int y = 0; y<listaExpEvalJusTecHist.size();y++) { 
                        
                        Integer idExpedienteEvalJusTecHist = listaExpEvalJusTecHist.get(y).getIdExpedienteEvalJusTecHist();
                        this.expedienteEvalJusTecHistService.deleteLogico(idExpedienteEvalJusTecHist);
                    }
                }
                
                this.listaEvaluadoresAsignadosExpedienteJusTec = new ArrayList<>();
                List<ExpedienteEvalJusTecDTO> list = new ArrayList<>();
                
                for(int z = 0; z<listExpedienteEvalJusTec.size();z++) {
                
                    List<AtlasFilter> filtros = new ArrayList<>();
                    List<AtlasSort> orden = new ArrayList<>();
    
                    list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, listExpedienteEvalJusTec.get(z).getExpedienteJustec().getIdExpedienteJusTec());
                
                }
                
                if(!list.isEmpty()){
                    for(int a = 0; a<list.size();a++) {
                        this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
                    }
                }
                
               PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);    
               AtlasMessageUtils.addInfoMessageFromBundle(
                       ConstantesWeb.Mensajes.MSG_EVALUADOR_BAJA_EVALUADOR_OK);
                    
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.MSG_EVALUADOR_BAJA_EVALUADOR_KO);
        }
    }
    
    /**
     * método para mostrar el boton finalizar revision
     * asignado
     *
     * @param idValor
     * @return
     */
    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
     * (editar, eliminar evaluador, aprobar/desaprobar, finalizar evaluación...), dejando
     * habilitadas las de solo lectura (descargar, ver histórico).
     *
     * @return true si el expediente está Rechazado Definitivo
     */
    public boolean isExpedienteRechazadoDefinitivo() {
        return this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }

    public Boolean mostrarBotonFinalizarEvaluacion() {
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite finalizar evaluación
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean mostrar = Boolean.FALSE;

        if (!this.listaEvaluadoresAsignadosExpedienteJusTec.isEmpty()) {
            for(int i = 0; i<this.listaEvaluadoresAsignadosExpedienteJusTec.size();i++) {
               if(this.expedienteJusTecSeleccionado.getIdExpedienteJusTec().equals(this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getExpedienteJustecDTO().getIdExpedienteJusTec())){  
                    if (this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO))
                        || this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION))
                            ||this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_EVALUADOR))) {
                        mostrar = Boolean.TRUE;
                    }
                }
            }
        }
        
    
        if(this.expedienteEvalJusTecDTO != null) {
            if(this.expedienteEvalJusTecDTO.getExpedienteJustecDTO() != null && 
                    this.expedienteEvalJusTecDTO.getExpedienteJustecDTO().getIdEstadoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))) {
                    mostrar = Boolean.TRUE;
            }
        }
        return mostrar;
    }
    
    /**
     * Método que finaliza la evaluacion justec CT
     */
    public void finalizarEvaluacionOk() {

        try {
            
            for(int i = 0; i<this.listaEvaluadoresAsignadosExpedienteJusTec.size();i++) {
               
                if(this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().
                       equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR)) 
                        || this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO))) {
      
                        if (this.expedienteBean.getIdExpediente() != null) {
                            
                            List<AtlasFilter> filtrosEntrada = new ArrayList<>();

                            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                                    this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getExpedienteJustecDTO().getIdExpedienteJusTec()
                                    , Operador.EQUALS));
                            
                            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                            List<AtlasSort> ordenamiento = new ArrayList<>();
                            
                            ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC, true));
                        
                            this.listaExpedientes = expedienteJusTecService.buscar(0, -1,
                                    filtrosEntrada, ordenamiento);
                            
                            for(ExpedienteJusTecDTO expJusTecDTO : this.listaExpedientes) {
                            
                                CatalogosValoresDTO estadoDTOAux = new CatalogosValoresDTO();
                                estadoDTOAux.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO));
                                estadoDTOAux.setNmConstante(Constantes.EstadoExpedienteJustec.EVALUADO);
                                expJusTecDTO.setIdEstadoDTO(estadoDTOAux);
                                expJusTecDTO.setCdUsuModif(UserUtils.getUserName());
                                expJusTecDTO.setFcModifFila(new Date());
                            
                                this.expedienteJusTecService.update(expJusTecDTO);
                                
                                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_PERIODOS);    
                                PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);  
                             
                            }
                        }
        
                    }
                }
            
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);
 
        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
        }

    }
    
    /**
    *
    * @param doc
    * @return
    */
   public Boolean puedeComentarObservaciones(Integer estadoConvocatoria, ExpedienteJusTecDTO expJustec ) {
       Boolean valor = Boolean.FALSE;

           if(estadoConvocatoria.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA)) 
                   && (expJustec.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.REVISION_OK))  ||
                       expJustec.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION))  ||
                       expJustec.getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION)) )) {
                   valor = Boolean.TRUE;
               
           }

       return valor;
   }
   
   /**
    * Método para actualizar la calificación de un requisito
    *
    * @param indice Indice de campo a actualizar
    * @param dto Datos de requisito
    */
   public void actualizarObservacion(ExpedienteJusTecDTO dto) {
       try {

           if(dto.getTlObservacion() != null) {
               this.expedienteJusTecService.guardar(dto);
           }

           // Refrescamos los controles de formulario
           PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_PERIODOS);    
           PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);    
           
       } catch (ServiceException e) {
           log.error(AtlasMessageUtils.getStringFromBundle(
                   ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_EVAL_OBSERVACION_KO), e);
           AtlasMessageUtils.addErrorMessageFromBundle(
                   ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_EVAL_OBSERVACION_KO);
       }
   }
   
   /**
    * método para mostrar el ícono eliminar evaluador
    * asignado
    *
    * @param idValor
    * @return
    */
   public Boolean mostrarEliminar(Integer idValor) {
       // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite eliminar evaluador
       if (this.isExpedienteRechazadoDefinitivo()) {
           return Boolean.FALSE;
       }

       Boolean mostrar = Boolean.FALSE;

       if(!this.listaExpedientes.isEmpty()) {
           for(int i = 0; i<this.listaExpedientes.size();i++) {
               if(Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_EVALUADOR).equals(idValor)
                       || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR).equals(idValor)
                           || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION).equals(idValor)
                               || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR).equals(idValor)
                                   && this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))
                                       || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO).equals(idValor)
                                           && this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))){
                                               mostrar = Boolean.TRUE;
               }
           }
       }

       return mostrar;
   }
   
   /**
    * método para mostrar el ícono descargar la evaluacion JCT
    *
    * @param idExpedienteEvalJusTec
    * @return
 * @throws ServiceException 
    */
   public Boolean mostrarIconoDescargarExpedienteEvaluacionJustecCT(Integer idExpedienteEvalJusTec) throws ServiceException {
       Boolean mostrar = Boolean.FALSE;

       if (idExpedienteEvalJusTec != null) {
           expedienteEvalJusTecDTO = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
           if(expedienteEvalJusTecDTO.getCdFica() != null){
               mostrar = Boolean.TRUE;
           }
       }
       return mostrar;
   }
   
   /**
    * método para mostrar el ícono aprobar y desaprobar la evaluacion JCT
    *
    * @param idValor
    * @return
    */
   public Boolean mostrarIconoAprobarDesaprobarExpedienteEvaluacionJustecCT(Integer idValor) {
       // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite aprobar/desaprobar
       if (this.isExpedienteRechazadoDefinitivo()) {
           return Boolean.FALSE;
       }

       Boolean mostrar = Boolean.FALSE;

       if (Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_EVALUADOR)
               .equals(idValor)) {
           mostrar = Boolean.TRUE;
       }

       return mostrar;
   }
   
   /**
    * método para mostrar el ícono aprobar y desaprobar la evaluacion JCT
    *
    * @param idValor
    * @return
    */
   public Boolean mostrarEditar(Integer idValor) {
       // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite editar
       if (this.isExpedienteRechazadoDefinitivo()) {
           return Boolean.FALSE;
       }

       Boolean mostrar = Boolean.TRUE;

       if(!this.listaExpedientes.isEmpty()) {
           for(int i = 0; i<this.listaExpedientes.size();i++) {
               if(Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_EVALUADOR).equals(idValor)
                       || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR).equals(idValor)
                           || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION).equals(idValor)
                               && this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getItNotificado().equals(Constantes.AfirmacionNegacion.S)
                               || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.CERRADO_GESTOR).equals(idValor)
                                   && this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))
                                       || Objects.nonNull(idValor) && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.NO_EVALUADO).equals(idValor)
                                           && this.listaEvaluadoresAsignadosExpedienteJusTec.get(i).getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))){
                                               mostrar = Boolean.FALSE;
               }
           }
       }

       return mostrar;
   }
   
   public Boolean mostrarAsignarEvaluadores() {
       Boolean mostrar = Boolean.FALSE;

       if (!this.listaExpedientes.isEmpty()) {
           for(int i = 0; i<this.listaExpedientes.size();i++) {
               if (this.listaExpedientes.get(i).getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.NO_PRESENTADA))
                       || this.listaExpedientes.get(i).getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.REVISION_ADM_KO))
                           || this.listaExpedientes.get(i).getIdEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.EVALUADO))) {
                   mostrar = Boolean.TRUE;
               }
           }
       }

       return mostrar;
   }
   
   
   public AtlasListAndCount<ExpedienteEvalJusTecDTO> obtenerListaEvaluadoresAsignadosExpedienteJusTec(int index, int pageSize,
           Object ordenacion, Object filter) {
       
       AtlasListAndCount<ExpedienteEvalJusTecDTO> listaExp = new AtlasListAndCount<>();
       
       try {
          
           Integer idExpedienteJusTec = this.expedienteJusTecSeleccionado.getIdExpedienteJusTec();
               
           List<AtlasFilter> filtros = new ArrayList<>();
           List<AtlasSort> orden = new ArrayList<>();

           List<ExpedienteEvalJusTecDTO> list = this.expedienteEvalJusTecService.buscarEvaluadoresPorPeriodo(0, -1, filtros, orden, idExpedienteJusTec);
               
           if(!list.isEmpty()){
               for(int a = 0; a<list.size();a++) {
                   this.listaEvaluadoresAsignadosExpedienteJusTec.add(list.get(a));
               }
           }
           
           listaExp.setResultados(list);
           listaExp.setTotal(list.size());
           

           PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC);
           PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EVALUACION_EXPEDIENTE_JUSTEC_ID_CONTENEDOR_EVALUADORES_PORPERIODO);
          
       } catch (Exception exc) {
           log.error("Error al obtener los evaluadores asignados a la Justif. CT por periodo seleccionado.", exc);
           AtlasMessageUtils
                   .addErrorMessage("Error al obtener los evaluadores asignados a la Justif. CT por periodo seleccionado");
       }
    return listaExp;
   }
   
   /**
    * Método para abrir la ventana popup de AprobarEvaluacionJCT
    *
    * @param dto
    * @throws ServiceException 
    */
   public void mostrarAprobarEvaluacionJCT(Integer idExpedienteEvalJusTec) throws ServiceException {
       if (idExpedienteEvalJusTec != null) {
           this.evaluadorSeleccionado = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
           PrimeFaces.current().executeScript("PF('dialogAprobarEvaluacionJCT').show();");
       }
   }
   
   /**
    * Método para cerrar la ventana popup de AprobarEvaluacionJCT
    */
   public void cerrarAprobarEvaluacionJCT() {

       PrimeFaces.current().executeScript("PF('dialogAprobarEvaluacionJCT').hide();");
   }
   
   
   /**
    * Método para abrir la ventana popup de DesAprobarEvaluacionJCT
    *
    * @param dto
    * @throws ServiceException 
    */
   public void mostrarDesAprobarEvaluacionJCT(Integer idExpedienteEvalJusTec) throws ServiceException {
       if (idExpedienteEvalJusTec != null) {
           this.evaluadorSeleccionado = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
           PrimeFaces.current().executeScript("PF('dialogDesAprobarEvaluacionJCT').show();");
       }
   }
   
   /**
    * Método para cerrar la ventana popup de DesAprobarEvaluacionJCT
    */
   public void cerrarDesAprobarEvaluacionJCT() {

       PrimeFaces.current().executeScript("PF('dialogDesAprobarEvaluacionJCT').hide();");
   }
   
   /**
    * Método para abrir la ventana popup de editar evaluador Asignado al ExpedienteJusTec
    *
    * @param dto
    * @throws ServiceException 
    */
   public void mostrarEvaluadoresAsignadosExpedienteJusTec(Integer idExpedienteEvalJusTec) throws ServiceException {
       if (idExpedienteEvalJusTec != null) {
           this.evaluadorSeleccionado = this.expedienteEvalJusTecService.buscarId(idExpedienteEvalJusTec);
           PrimeFaces.current().executeScript("PF('dialogEditarEvaluadorAsignado').show();");
       }
   }
   
   /**
    * Método para cerrar la ventana popup de editar evaluador Asignado al ExpedienteJusTec
    */
   public void cerrarEvaluadorAsignadoExpedienteEvalJustecCT() {

       PrimeFaces.current().executeScript("PF('dialogEditarEvaluadorAsignado').hide();");
   }
   
   /**
    * Método para obtener la lista de evaluadores asignados a la evaluion JCT
    *
    * @param index Indice
    * @param pageSize Cantidad páginas
    * @param orderBean Ordenamiento
    * @param filtros Filtros
    * @return Lista de evaluadores asignados a la evaluion JCT
    * @throws ServiceException 
    */
   public AtlasListAndCount<ExpedienteEvalJusTecDTO> obtenerEvaluadorAsignadoExpedienteEvalJustecCT(int index, int pageSize,
           Object ordenacion, Object filter) throws ServiceException {
       
       AtlasListAndCount<ExpedienteEvalJusTecDTO> listadoDatosFinal = new AtlasListAndCount<>();
       List<ExpedienteEvalJusTecDTO> listadoDatos = new ArrayList<>();
     
       if (this.evaluadorSeleccionado != null && this.evaluadorSeleccionado.getIdExpedienteEvalJusTec() != null) {
           
           ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO = this.expedienteEvalJusTecService.buscarId(this.evaluadorSeleccionado.getIdExpedienteEvalJusTec());
           listadoDatos.add(expedienteEvalJusTecDTO);
           listadoDatosFinal.setResultados(listadoDatos);
           listadoDatosFinal.setTotal(listadoDatos.size());
       }
       
       return listadoDatosFinal;
   }
   
   public Boolean mostrarAlert(Integer idValor) {
       Boolean mostrar = Boolean.FALSE;

       if (this.evaluadorSeleccionado != null) {
           if (Objects.nonNull(this.evaluadorSeleccionado.getEstadoDTO().getIdValor()) 
                   && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.EVALUADOR_ASIGNADO)
               .equals(this.evaluadorSeleccionado.getEstadoDTO().getIdValor())) {
               mostrar = Boolean.TRUE;
           }
           
           if (Objects.nonNull(this.evaluadorSeleccionado.getEstadoDTO().getIdValor()) && 
                   utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadosEvaluacionJCT.PENDIENTE_EVALUACION)
                   .equals(this.evaluadorSeleccionado.getEstadoDTO().getIdValor())
                       && this.evaluadorSeleccionado.getItNotificado().equals(Constantes.AfirmacionNegacion.N)) {
                   mostrar = Boolean.TRUE;
           }
       }
       return mostrar;
   }
   
}
