package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.EntidadService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.SolicitudService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Solicitud}</code>
 *
 * @author everis
 *
 * @since 01-oct-2020 12:22:25
 *
 * @see SolicitudBean
 */
@Slf4j
@Getter
@Setter
@Component("solicitudBean")
@Scope("request")
public class SolicitudBean extends CatalogoBean<Solicitud, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * dto de solicitud
     */
    private SolicitudDTO solicitudDTO;

    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;

    /**
     * Numeros dias de subsanacion de Convocatoria
     */
    private Integer nmDiasSubsanacion;

    /**
     * Id de Solicitud
     */
    private Integer idSolicitud;

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private CatalogosValoresDTO areaCientificaFilter;

    /** Filtro para el campo interesado */
    private InteresadoDTO interesadoFilter;

    /** Flag para mostar solicitudes iniciadas */
    private Boolean mostrarIniciadas;

    /** Filtro por estado de la solicitud - permite seleccion multiple */
    private List<Integer> estadoFilter = new ArrayList<>();

    /** Filtro por dsNombre del interesado (texto libre - "Organismo" en la pantalla) */
    private String organismoFilter;

    /** Filtro por dsAbrev de la Entidad asociada al interesado */
    private String acronimoEntidadFilter;

    /** Filtro por codigo de expediente - cdExpedienteSol en modo solicitudes, cdExpediente en modo expedientes */
    private String codExpedienteFilter;

    /** DTO de la convocatoria **/
    private ConvocatoriaDTO convocatoriaDTO;
    
    // MD-1593 JRFM Ya no es necesario guardar el listado de expedientes
    //private List<ExpedienteDTO> lstExpedientes = new ArrayList<>();
    
    private String tablaSeleccionada = Constantes.RadioSelectOpcion.OPCION_SOLICITUDES; // Valor por defecto
    
    
 // Mapas para almacenar las cadenas de texto ya procesadas en el servidor
    private Map<Integer, String> mapaTipoActorSolicitudes = new HashMap<>();
    private Map<Integer, String> mapaDetalleActoresExpedientes = new HashMap<>();

    // Mapas idSolicitud -> dsValor del estado correspondiente del expediente SOLICITANTE
    // (el expediente con idExpedientePadre a null) de esa solicitud, para las columnas de estado
    // añadidas en solapaSolicitudes
    private Map<Integer, String> mapaEstadoExpedienteSolicitudes = new HashMap<>();
    private Map<Integer, String> mapaEstadoRevAdminSolicitudes = new HashMap<>();
    private Map<Integer, String> mapaEstadoRevReqSolicitudes = new HashMap<>();
    private Map<Integer, String> mapaEstadoRevEvaSolicitudes = new HashMap<>();
    private Map<Integer, String> mapaEstadoRevFinanSolicitudes = new HashMap<>();
    
    private Map<Integer, Integer> mapaNotaExpedienteSolicitudes = new HashMap<>();

    // VMM 30/09/26 MD-3096: mapa idInteresado -> dsAbrev de la Entidad asociada, para la columna de Acronimo
    // Entidad en modo "solicitudes" - se rellena en lote (una unica consulta IN) igual que los mapas
    // de estado de arriba, para no disparar una consulta por fila.
    private Map<Integer, String> mapaAcronimoEntidadSolicitudes = new HashMap<>();


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;
    
    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    @Autowired
    @Qualifier("entidadService")
    private EntidadService entidadService;

    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
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

    /** ExpedienteBean */
    @Autowired
    @Qualifier("expedienteBean")
    private ExpedienteBean expedienteBean;
    
    private ConvocatoriasBean convocatoriasBean;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public SolicitudBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad ConvocatoriaSol
     *
     * @return entidad de tipo ConvocatoriaSol
     */
    @Override
    public Solicitud crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.inicializarSolicitud();
      
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

    /**
     * Recupera el listado de entidades SolicitudDTO que cumple con los
     * criterios de búsqueda. Devuelve las entidades situadas entre las
     * posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Asignatura
     */
    @SuppressWarnings("unchecked")
//    public AtlasListAndCount<SolicitudDTO> obtenerListaSolicitudes(final int index,
//            final int pageSize, Object orderBean, Object filtros) {
//        AtlasListAndCount<SolicitudDTO> listado = new AtlasListAndCount<SolicitudDTO>();
////        List<ExpedienteDTO> lstExpedienteEstruc = new ArrayList<>();
////        List<ExpedienteDTO> lstExp = new ArrayList<>();
////        this.lstExpedientes = new ArrayList<>();
//        try {
//            if (this.idConvocatoria != null) {
//                //MD-2873: No filtra por identificador ni incluia los filtros de pantalla
//                List<AtlasFilter> filtrosEntrada = filtros != null ? (List<AtlasFilter>) filtros
//                        : new ArrayList<>();
//
//                filtrosEntrada
//                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
//                                this.idConvocatoria, Operador.EQUALS));
//                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//
//                List<Integer> lstEstados = new ArrayList<>();
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
//                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));
//                
//                // Si el flag de mostrar iniciadas, está a TRUE. Se agregan al
//                // filtro.
//                if (Boolean.TRUE.equals(this.mostrarIniciadas)) {
//                    lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
//                    
//                    //VMM 09/02/26 MD-2381: si es una solicitud Iniciada entramos en modo consulta al exp
//                    this.convocatoriasBean.setModoConsulta(Boolean.TRUE);
//                }
//
//                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
//                        lstEstados, Operador.IN));
//
//                //VMM 27/01/26 MD-2063: ordenamos solo con el campo que corresponda si se ordena desde el listado
//                List<AtlasSort> orden = new ArrayList<>();
//                if (Objects.isNull(orderBean)) {
//                    orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));
//                }else {
//                    orden = (List<AtlasSort>) orderBean;
//                }
//                /*if (Objects.isNull(orderBean)) {
//                    orderBean = new ArrayList<AtlasSort>();
//                }
//                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
//                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));*/
//
//                List<SolicitudDTO> listadoSolicitudes = this.solicitudService
//                        .buscarSolicitudesTramitacion(index, pageSize, filtrosEntrada, orden,
//                                this.idConvocatoria, this.mostrarIniciadas, this.nmDiasSubsanacion);
//                
//                //  MD-1593 JRFM - Ya no es necesario obtener el listado de expediente se obtiene con 
//                // la funcion obtenerListaExpedientePorConvocatoria
//                /*
//                for (SolicitudDTO solDTO : listadoSolicitudes) {
//
//                    lstExp = this.expedienteService.obtenerExpedienteEstructurado(
//                            solDTO.getIdSolicitud(), Constantes.AfirmacionNegacion.S);
//                    
//                    // Busca el expediente solicitante y obtén su fecha de firma (fcFirma)
//                    Optional<Date> fcFirmaSolicitante = lstExp.stream()
//                        .filter(expediente -> Boolean.TRUE.equals(expediente.getEsExpedienteSolicitante()))
//                        .map(ExpedienteDTO::getFcFirma)  // Extrae el campo fcFirma
//                        .findFirst();                    // Obtiene el primero (si existe)
//
//                    // Usar la fecha si está presente
//                    Date fechaPresentacion = null;
//                    if (fcFirmaSolicitante.isPresent()) {
//                        fechaPresentacion = fcFirmaSolicitante.get();
//                    }
//
//                    for (ExpedienteDTO expedienteDTO : lstExp) {
//                        expedienteDTO.setSolicitudDTO(solDTO);
//                        expedienteDTO.setFcPresentacion(fechaPresentacion);
//                    }
//
//                    this.lstExpedientes.addAll(lstExp);
//                    this.expedienteBean.getLstExpedientes().addAll(lstExp);
//
//                }
//                */
//                
////                for (SolicitudDTO sol : listadoSolicitudes) {
////                    if (sol.getCdExpedienteSol() != null && !sol.getCdExpedienteSol().isEmpty()) {
////                        Integer idExpediente = Utils
////                                .getIdExpedienteDeAcronimo(sol.getCdExpedienteSol());
////                        if (idExpediente > 0) {
////                            sol.setFcPresentacion(
////                                    this.expedienteService.obtenerFcPresentacionBySolicitud(
////                                            sol.getIdSolicitud(), idExpediente));
////                        }
////                    }
////                }
//             // 1. Recolectamos los idExpediente del lote actual de la pantalla
//                List<Integer> idsExpedienteLote = new ArrayList<>();
//                for (SolicitudDTO sol : listadoSolicitudes) {
//                    if (sol.getCdExpedienteSol() != null && !sol.getCdExpedienteSol().isEmpty()) {
//                        Integer idExpediente = Utils.getIdExpedienteDeAcronimo(sol.getCdExpedienteSol());
//                        if (idExpediente > 0) {
//                            idsExpedienteLote.add(idExpediente);
//                        }
//                    }
//                }
//
//                // 2. Una única consulta a la base de datos para todas las solicitudes de la página
//                Map<Integer, Date> mapaFechas = this.expedienteService.obtenerFcPresentacionPorLote(idsExpedienteLote);
//
//                // 3. Asignamos las fechas en memoria O(N) sin tocar la BBDD
//                for (SolicitudDTO sol : listadoSolicitudes) {
//                    Date fcPresentacion = mapaFechas.get(sol.getIdSolicitud());
//                    if (fcPresentacion != null) {
//                        sol.setFcPresentacion(fcPresentacion);
//                    }
//                }
// 
//                listado.setResultados(listadoSolicitudes);
//                listado.setTotal(this.solicitudService.count(filtrosEntrada));
//            }
//        } catch (ServiceException exc) {
//            log.error(ConstantesWeb.Err.ERROR_LISTADO);
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
//        }
//        return listado;
//    }

 // Variables de control en el Bean (deben resetearse si cambian los filtros de búsqueda manual)
    private int ultimoIndex = -1;
    private int ultimoPageSize = -1;
    private AtlasListAndCount<SolicitudDTO> listadoCacheado = null;

    public AtlasListAndCount<SolicitudDTO> obtenerListaSolicitudes(final int index, final int pageSize, Object orderBean, Object filtros) {
        // Si los parámetros son los mismos que la última vez en este ciclo de renderizado, devolvemos la caché
        if (this.listadoCacheado != null && this.ultimoIndex == index && this.ultimoPageSize == pageSize) {
            return this.listadoCacheado;
        }

        // Si es una petición nueva o una página distinta, ejecutamos la lógica real que optimizamos antes:
        AtlasListAndCount<SolicitudDTO> resultadoNuevo = ejecutarLogicaObtenerSolicitudes(index, pageSize, orderBean, filtros);

        // Guardamos en la caché del Bean para las siguientes llamadas del ciclo JSF
        this.ultimoIndex = index;
        this.ultimoPageSize = pageSize;
        this.listadoCacheado = resultadoNuevo;

        return resultadoNuevo;
    }
    

    
    // Mueve aquí el cuerpo del método completo que optimizamos previamente
    private AtlasListAndCount<SolicitudDTO> ejecutarLogicaObtenerSolicitudes(final int index, final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<SolicitudDTO> listado = new AtlasListAndCount<SolicitudDTO>();
        try {
            if (this.idConvocatoria != null) {
                // 1. Inicialización y construcción de filtros base
                List<AtlasFilter> filtrosEntrada = filtros != null ? (List<AtlasFilter>) filtros : new ArrayList<>();

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, this.idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));
                
                if (Boolean.TRUE.equals(this.mostrarIniciadas)) {
                    lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
                    this.convocatoriasBean.setModoConsulta(Boolean.TRUE);
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados, Operador.IN));

                // 2. Ejecutar el COUNT inmediatamente con los filtros limpios
                listado.setTotal(this.solicitudService.count(filtrosEntrada));

                // 3. Configuración del orden dinámico
                List<AtlasSort> orden = new ArrayList<>();
                if (Objects.isNull(orderBean)) {
                    orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));
                } else {
                    orden = (List<AtlasSort>) orderBean;
                }

                // 4. Recuperación paginada de registros (E hidratación batch de alertas interna)
                List<SolicitudDTO> listadoSolicitudes = this.solicitudService
                        .buscarSolicitudesTramitacion(index, pageSize, filtrosEntrada, orden,
                                this.idConvocatoria, this.mostrarIniciadas, this.nmDiasSubsanacion);
                
                // 5. Tratamiento batch en memoria para la fecha de presentación
                if (!listadoSolicitudes.isEmpty()) {
                    List<Integer> idsExpedienteLote = new ArrayList<>();
                    for (SolicitudDTO sol : listadoSolicitudes) {
                        if (sol.getCdExpedienteSol() != null && !sol.getCdExpedienteSol().isEmpty()) {
                            Integer idExpediente = Utils.getIdExpedienteDeAcronimo(sol.getCdExpedienteSol());
                            if (idExpediente > 0) {
                                idsExpedienteLote.add(idExpediente);
                            }
                        }
                    }

                    if (!idsExpedienteLote.isEmpty()) {

                        Map<Integer, Date> mapaFechas = this.expedienteService.obtenerFcPresentacionPorLote(idsExpedienteLote);

                        long tbucleFechas = System.currentTimeMillis();
                        for (SolicitudDTO sol : listadoSolicitudes) {
                            Date fcPresentacion = mapaFechas.get(sol.getIdSolicitud());
                            if (fcPresentacion != null) {
                                sol.setFcPresentacion(fcPresentacion);
                            }
                        }
                    }

                    // 6. Tratamiento batch en memoria de los estados del expediente SOLICITANTE
                    // (idExpedientePadre a null) de cada solicitud, para las columnas de estado
                    List<Integer> idsSolicitudLote = listadoSolicitudes.stream()
                            .map(SolicitudDTO::getIdSolicitud)
                            .collect(Collectors.toList());

                    this.mapaEstadoExpedienteSolicitudes.clear();
                    this.mapaEstadoRevAdminSolicitudes.clear();
                    this.mapaEstadoRevReqSolicitudes.clear();
                    this.mapaEstadoRevEvaSolicitudes.clear();
                    this.mapaEstadoRevFinanSolicitudes.clear();
                    this.mapaNotaExpedienteSolicitudes.clear();

                    if (!idsSolicitudLote.isEmpty()) {
                        Map<Integer, Object[]> mapaEstados = this.expedienteService
                                .obtenerEstadosExpedienteSolicitantePorLote(idsSolicitudLote);

                        for (Map.Entry<Integer, Object[]> entry : mapaEstados.entrySet()) {
                            Integer idSolicitud = entry.getKey();
                            Object[] estados = entry.getValue();
                            this.mapaEstadoExpedienteSolicitudes.put(idSolicitud, (String) estados[0]);
                            this.mapaEstadoRevAdminSolicitudes.put(idSolicitud, (String) estados[1]);
                            this.mapaEstadoRevReqSolicitudes.put(idSolicitud, (String) estados[2]);
                            this.mapaEstadoRevEvaSolicitudes.put(idSolicitud, (String) estados[3]);
                            this.mapaEstadoRevFinanSolicitudes.put(idSolicitud, (String) estados[4]);
                            this.mapaNotaExpedienteSolicitudes.put(idSolicitud, (Integer) estados[5]);
                        }
                    }

                    // VMM 30/09/26 MD-3096: mismo enfoque en lote para la columna de Acronimo Entidad - una
                    // unica consulta con IN sobre los idInteresado de esta pagina, en vez de una
                    // consulta por fila (EntidadService.buscarEntidadInteresado ya asume "primera
                    // Entidad encontrada" si un interesado tuviera mas de una, igual que aqui).
                    this.mapaAcronimoEntidadSolicitudes.clear();
                    List<Integer> idsInteresadoLote = listadoSolicitudes.stream()
                            .filter(sol -> sol.getInteresadoDTO() != null && sol.getInteresadoDTO().getIdInteresado() != null)
                            .map(sol -> sol.getInteresadoDTO().getIdInteresado())
                            .distinct()
                            .collect(Collectors.toList());

                    if (!idsInteresadoLote.isEmpty() && this.isConvocatoriaResolucionConjunta()) {
                        List<AtlasFilter> filtrosEntidadLote = new ArrayList<>();
                        filtrosEntidadLote.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        filtrosEntidadLote.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO,
                                idsInteresadoLote, Operador.IN));
                        for (EntidadDTO entidad : this.entidadService.buscar(0, -1, filtrosEntidadLote, null)) {
                            if (entidad.getInteresadoDTO() != null && entidad.getInteresadoDTO().getIdInteresado() != null) {
                                this.mapaAcronimoEntidadSolicitudes.putIfAbsent(
                                        entidad.getInteresadoDTO().getIdInteresado(), entidad.getDsAbrev());
                            }
                        }
                    }
                }

                listado.setResultados(listadoSolicitudes);
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    
    public void limpiarBusqueda() {
        this.listadoCacheado = null;
        this.ultimoIndex = -1;
        this.mapaTipoActorSolicitudes.clear();
        this.mapaDetalleActoresExpedientes.clear();
        this.mapaEstadoExpedienteSolicitudes.clear();
        this.mapaEstadoRevAdminSolicitudes.clear();
        this.mapaEstadoRevReqSolicitudes.clear();
        this.mapaEstadoRevEvaSolicitudes.clear();
        this.mapaEstadoRevFinanSolicitudes.clear();
        this.mapaNotaExpedienteSolicitudes.clear();
        this.mapaAcronimoEntidadSolicitudes.clear();
    }

    /**
     * VMM 30/09/26 MD-3096: devuelve el acronimo de la Entidad asociada al interesado indicado, ya
     * precalculado en lote por ejecutarLogicaObtenerSolicitudes() - para la columna de Acronimo
     * Entidad en modo "solicitudes".
     */
    public String obtenerAcronimoEntidad(Integer idInteresado) {
        return idInteresado != null ? this.mapaAcronimoEntidadSolicitudes.get(idInteresado) : null;
    }

    /**
     * VMM 02/10/26 MD-3096: indica si la convocatoria seleccionada es de resolucion conjunta (cdTipoResolucion
     * = "C"). La columna de Acronimo Entidad solo tiene sentido en ese tipo de convocatoria; en resolucion
     * individual no se muestra. Mismo dato que usa ConvocatoriasBean.mostrarEdicionConvocatoria
     * (convocatoriaDTO.tipoResolModalidadDTO.cdTipoResolucion).
     */
    public boolean isConvocatoriaResolucionConjunta() {
        ConvocatoriaDTO convocatoria = this.convocatoriasBean.getConvocatoriaDTO();
        return convocatoria != null && convocatoria.getTipoResolModalidadDTO() != null
                && Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA
                        .equals(convocatoria.getTipoResolModalidadDTO().getCdTipoResolucion());
    }
    

    
    /**
     * Recupera el listado de entidades ExpedienteDTO en base a las solicitudes obtenidas de la búsqueda
     * Devuelve las entidades situadas entre las posiciones index e index+pageSize según la ordenación indicada.
     *
     * @param index - Elemento inicial a devolver
     * @param pageSize - Número de elementos a devolver
     * @param orderBean - Objeto OrderBean que ha rellenado la listaPaginada
     * @param filtros - Objeto que se ha rellenado en el método filtrar
     * @return lista de entidades Asignatura
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDTO> obtenerListaExpedientePorConvocatoria(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDTO> listado = new AtlasListAndCount<>();
        List<Integer> lstIdSol = new ArrayList<>();
        Map<Integer, SolicitudDTO> mapSol = new ConcurrentHashMap<>();
        
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosSolicitud = (List<AtlasFilter>) filtros;

                filtrosSolicitud.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                                this.idConvocatoria, Operador.EQUALS));
                filtrosSolicitud.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
                //VMM 26/01/26 MD-2285: aniadimos que saque las solicitudes y exps concedidos_desistidos
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));

                // Si el flag de mostrar iniciadas, está a TRUE. Se agregan al
                // filtro.
                if (Boolean.TRUE.equals(this.mostrarIniciadas)) {
                    lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
                    
                    //VMM 09/02/26 MD-2381: si es una solicitud Iniciada entramos en modo consulta al exp
                    this.convocatoriasBean.setModoConsulta(Boolean.TRUE);
                }

                filtrosSolicitud.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                        lstEstados, Operador.IN));

                //VMM 15/09/25 MD-1914: para obtener la lista de solicitudes no ordenamos por los campos del expediente porque da error
                List<AtlasSort> ordenSolicitud = new ArrayList<>();
                //VMM 27/01/26 MD-2063: ordenamos solo con el campo que corresponda si se ordena desde el listado
                if (Objects.isNull(orderBean)) {
                    ordenSolicitud.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));
                }else {
                    //VMM MD-2063: ordenamos por los campos correspondientes de la solicitud si hemos seleccionado y ordenado los expedientes
                    if (orderBean.toString().contains(Constantes.Filtro.FILTRO_FCFIRMA)) {
                        ordenSolicitud.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));
                        
                    }else if (orderBean.toString().contains(Constantes.Filtro.FILTRO_CD_EXPEDIENTE)) {
                        ordenSolicitud.add(new AtlasSort(Constantes.Filtro.FILTRO_CD_EXPEDIENTE_SOL, Boolean.FALSE));
                        
                    }else if (orderBean.toString().contains(Constantes.Filtro.FILTRO_SOLICITUD_INTERESADO_DSNOMBRE)) {
                        ordenSolicitud.add(new AtlasSort(Constantes.Filtro.FILTRO_INTERESADO_DSNOMBRE, Boolean.FALSE));
                        
                    }else if (orderBean.toString().contains(Constantes.Filtro.FILTRO_EXP_CONVOC_ACT_IDENTIFICACION_DSVALOR)) {
                        ordenSolicitud.add(new AtlasSort(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR_DSVALOR, Boolean.FALSE));
                    }else {
                        ordenSolicitud = (List<AtlasSort>) orderBean;
                    }
                }
                List<SolicitudDTO> listadoSolicitudes = this.solicitudService
                        .buscarSolicitudesTramitacion(0, -1, filtrosSolicitud, ordenSolicitud,
                                this.idConvocatoria, this.mostrarIniciadas, this.nmDiasSubsanacion);
                
                // Mapeamos para tener el listado de solicitudes para recuperarlo despues
                for (SolicitudDTO solDTO : listadoSolicitudes) {
                    lstIdSol.add(solDTO.getIdSolicitud());
                    mapSol.put(solDTO.getIdSolicitud(), solDTO);
                }

                // VMM 24/09/26 MD-2133: filtros por estado del EXPEDIENTE y por codigo de expediente
                // (cdExpediente) en modo "expedientes". Ninguno de los dos es un campo de Solicitud, asi
                // que no pueden ir en filtrosSolicitud (que alimenta una consulta sobre Solicitud) - se
                // resuelven aqui acotando lstIdSol a las solicitudes cuyo expediente cumple los filtros,
                // antes de pasarlo a obtenerExpedienteEstructurado.
                boolean hayFiltroEstadoExpediente = this.estadoFilter != null && !this.estadoFilter.isEmpty();
                if ((hayFiltroEstadoExpediente || StringUtils.isNotBlank(this.codExpedienteFilter))
                        && !lstIdSol.isEmpty()) {
                    List<AtlasFilter> filtrosExpediente = new ArrayList<>();
                    filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                            lstIdSol, Operador.IN));
                    if (hayFiltroEstadoExpediente) {
                        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                                this.estadoFilter, Operador.IN));
                    }
                    if (StringUtils.isNotBlank(this.codExpedienteFilter)) {
                        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_EXPEDIENTE,
                                "%" + this.codExpedienteFilter + "%", Operador.LIKE_INSENSITIVE));
                    }

                    // VMM 30/09/26 MD-2133: usamos exp.getIdSolicitud() (Integer simple, siempre
                    // relleno por ExpedienteToDTO a partir del proxy lazy de Solicitud) en vez de
                    // exp.getSolicitudDTO() (el objeto completo) - este ultimo nunca viene poblado en
                    // esta consulta generica de expedienteService.buscar() (se rellena a mano mas abajo,
                    // sobre la lista final, ver mapSol/setSolicitudDTO), asi que la comprobacion
                    // "!= null" siempre era false y el filtro de Estado/codExpediente en modo
                    // Expedientes nunca encontraba nada, aunque la consulta SQL si tuviera resultados.
                    List<Integer> lstIdSolFiltrados = new ArrayList<>();
                    for (ExpedienteDTO exp : this.expedienteService.buscar(0, -1, filtrosExpediente, null)) {
                        if (exp.getIdSolicitud() != null) {
                            lstIdSolFiltrados.add(exp.getIdSolicitud());
                        }
                    }
                    lstIdSol.clear();
                    lstIdSol.addAll(lstIdSolFiltrados.isEmpty() ? Collections.singletonList(-1) : lstIdSolFiltrados);
                }

                //VMM 15/09/25 MD-1914: y ordenamos tambien si se ha metido ordenacion por alguna otra columna del expediente
                //VMM 09/06/25 MD-1644: ordenamos igual que las solicitudes por fecha de alta descendiente 
                //List<AtlasSort> ordenExpediente = (List<AtlasSort>) orderBean;
                List<AtlasSort> ordenExpediente = new ArrayList<AtlasSort>();
                if (Objects.isNull(orderBean)) {
                    ordenExpediente.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.FALSE));
                }else {
                    ordenExpediente = (List<AtlasSort>) orderBean;
                }               
                
                List<ExpedienteDTO> lstRet = this.expedienteService.obtenerExpedienteEstructurado(lstIdSol, index, pageSize, ordenExpediente, this.mostrarIniciadas);
                
                //VMM 01/04/26 MD-2041: cambiamos HashMap por LinkedHashMap para que mantenga el orden que tenia establecido
                Map<Integer, ExpedienteDTO> mapaExpedientesUnicos = new LinkedHashMap<>();
                for (ExpedienteDTO exp : lstRet) {
                    mapaExpedientesUnicos.put(exp.getIdExpediente(), exp);
                }

                // Limpiamos la lista original y la llenamos con los valores únicos del mapa.
                lstRet.clear();
                lstRet.addAll(mapaExpedientesUnicos.values());
                
                for(ExpedienteDTO exp : lstRet) {
                    exp.setSolicitudDTO(mapSol.get(exp.getIdSolicitud()));
                    exp.setFcPresentacion(this.expedienteService.obtenerFcPresentacionBySolicitud(exp.getIdSolicitud(), exp.getIdExpediente()));
                }
                
                listado.setResultados(lstRet);
                listado.setTotal(this.expedienteService.countExpedienteEstructurado(lstIdSol, this.mostrarIniciadas));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }
    
    

    /**
     * Metodo que inicializa a las Solicitudes
     *
     */
    public void inicializarSolicitud() {
        this.solicitudDTO = new SolicitudDTO();

        this.areaCientificaFilter = new CatalogosValoresDTO();
        this.interesadoFilter = new InteresadoDTO();
        this.tablaSeleccionada = Constantes.RadioSelectOpcion.OPCION_SOLICITUDES;
        this.estadoFilter = new ArrayList<>();
        this.organismoFilter = null;
        this.acronimoEntidadFilter = null;
        this.codExpedienteFilter = null;
        this.expedienteBean.setSolicitudBean(this);
    }

    /**
     * VMM 24/09/26 MD-2133: el filtro de Estado se reutiliza en los dos modos del radiobutton
     * (tablaSeleccionada), pero apunta a catalogos distintos - estado de Solicitud en modo
     * "solicitudes", estado de Expediente en modo "expedientes". Al cambiar de modo se limpia para que
     * no queden seleccionados ids de un catalogo que no tienen sentido (o que coinciden por casualidad
     * con un valor de estado distinto) en el otro.
     */
    public void alCambiarTablaSeleccionada() {
        this.estadoFilter = new ArrayList<>();
    }

    /**
     * Metodo que setea idSolicitud
     *
     * @param idSolicitud
     */
    public void cargarExpedientes(Integer idSolicitud) {
        try {
            if (idSolicitud != null) {
                this.expedienteBean.setIdSolicitud(idSolicitud);
                this.setSolicitudDTO(this.solicitudService.buscarId(idSolicitud));

                // Recuperamos la lista de expedientes
                this.expedienteBean.obtenerExpedientes();        
            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.SOLICITUD_MENSAJE_CARGAR_KO, e);
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_MENSAJE_CARGAR_KO);
        }
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

        if (this.interesadoFilter != null
                && StringUtils.isNotBlank(this.interesadoFilter.getCdNumIden())) {
            this.getFiltros().add(new AtlasFilter("interesado.cdNumIden",
                    "%" + this.interesadoFilter.getCdNumIden() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.interesadoFilter != null
                && StringUtils.isNotBlank(this.interesadoFilter.getDsNombre())) {
            this.getFiltros().add(new AtlasFilter("interesado.dsNombre",
                    "%" + this.interesadoFilter.getDsNombre() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.interesadoFilter != null
                && StringUtils.isNotBlank(this.interesadoFilter.getDsApellido1())) {
            this.getFiltros().add(new AtlasFilter("interesado.dsApellido1",
                    "%" + this.interesadoFilter.getDsApellido1() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.interesadoFilter != null
                && StringUtils.isNotBlank(this.interesadoFilter.getDsApellido2())) {
            this.getFiltros().add(new AtlasFilter("interesado.dsApellido2",
                    "%" + this.interesadoFilter.getDsApellido2() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.areaCientificaFilter != null
                && StringUtils.isNotBlank(this.areaCientificaFilter.getDsValor())) {
            this.getFiltros().add(new AtlasFilter("areaCientifica.dsValor",
                    "%" + this.areaCientificaFilter.getDsValor() + "%", Operador.LIKE_INSENSITIVE));
        }

        boolean modoExpedientes = Constantes.RadioSelectOpcion.OPCION_EXPEDIENTES.equals(this.tablaSeleccionada);

        // VMM 24/09/26 MD-2133: filtro por estado - seleccion multiple. En modo "solicitudes" es el
        // estado de la Solicitud y se puede anadir aqui directamente. En modo "expedientes" es el
        // estado del EXPEDIENTE, que no es un campo de Solicitud, asi que se resuelve aparte dentro de
        // obtenerListaExpedientePorConvocatoria (ver alli). Se combina (AND) con el filtro de estados
        // "visibles" que ya anaden obtenerListaSolicitudes/obtenerListaExpedientePorConvocatoria, asi
        // que el resultado siempre queda dentro de esos estados visibles.
        if (this.estadoFilter != null && !this.estadoFilter.isEmpty() && !modoExpedientes) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    this.estadoFilter, Operador.IN));
        }

        // VMM 24/09/26 MD-2133: filtro por Organismo - texto libre sobre el dsNombre del interesado de
        // la solicitud (el "Organismo" al que se refieren en la pantalla es el propio interesado). Solo
        // tiene sentido en modo "solicitudes".
        if (StringUtils.isNotBlank(this.organismoFilter) && !modoExpedientes) {
            this.getFiltros().add(new AtlasFilter("interesado.dsNombre",
                    "%" + this.organismoFilter + "%", Operador.LIKE_INSENSITIVE));
        }

        // VMM 23/09/26 MD-2133: filtro por Acronimo (dsAbrev) de la Entidad asociada al interesado. Mismo
        // enfoque en dos pasos que el filtro de Organismo, y con la misma restriccion a modo "solicitudes".
        if (StringUtils.isNotBlank(this.acronimoEntidadFilter) && !modoExpedientes) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO,
                    this.obtenerIdsInteresadoPorAcronimoEntidad(this.acronimoEntidadFilter), Operador.IN));
        }

        // VMM 23/09/26 MD-2133: filtro por codigo de expediente. En modo "solicitudes" es un campo propio
        // de Solicitud (cdExpedienteSol) y se puede anadir directamente aqui. En modo "expedientes"
        // el campo pertenece a Expediente, que no es una propiedad mapeada de Solicitud, asi que se
        // resuelve aparte dentro de obtenerListaExpedientePorConvocatoria (ver alli).
        if (StringUtils.isNotBlank(this.codExpedienteFilter) && !modoExpedientes) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_EXPEDIENTE_SOL,
                    "%" + this.codExpedienteFilter + "%", Operador.LIKE_INSENSITIVE));
        }
    }

    /**
     * VMM 23/09/26 MD-2133: devuelve los idInteresado de las Entidades cuyo dsAbrev coincide (LIKE) con el
     * texto indicado. Mismo motivo que obtenerIdsInteresadoPorOrganismo para el id centinela -1.
     */
    private List<Integer> obtenerIdsInteresadoPorAcronimoEntidad(String acronimo) {
        List<Integer> idsInteresado = new ArrayList<>();
        try {
            List<AtlasFilter> filtrosEntidad = new ArrayList<>();
            filtrosEntidad.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtrosEntidad.add(new AtlasFilter("dsAbrev", "%" + acronimo + "%", Operador.LIKE_INSENSITIVE));
            for (EntidadDTO entidad : this.entidadService.buscar(0, -1, filtrosEntidad, null)) {
                if (entidad.getInteresadoDTO() != null) {
                    idsInteresado.add(entidad.getInteresadoDTO().getIdInteresado());
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
        }
        if (idsInteresado.isEmpty()) {
            idsInteresado.add(-1);
        }
        return idsInteresado;
    }

    /**
     * Método que borra los filtros del listado.
     */
    public void borrarFiltros() {
        this.setMostrarBaja(false);
        this.setTotalEntidades(0);
        this.setRealizarBusqueda(Boolean.FALSE);
        this.setFiltros(new ArrayList<>());
        this.inicializarSolicitud();
        this.filtrar();

    }

}
