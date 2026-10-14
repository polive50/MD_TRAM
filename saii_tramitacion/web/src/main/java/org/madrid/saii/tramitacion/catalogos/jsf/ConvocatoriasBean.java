package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPaginacionDTO;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.services.ConvocatoriaActService;
import org.madrid.saii.lib.services.ConvocatoriaGestorService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.UnidadAdminService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.NavigationResults;
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
 * entidad <code>{@link Convocatoria}</code>
 *
 * @author everis
 *
 * @since 01-oct-2020 12:22:25
 *
 * @see ConvocatoriasBean
 */
@Slf4j
@Getter
@Setter
@Component("convocatoriasBean")
@Scope("request")
public class ConvocatoriasBean extends CatalogoBean<Convocatoria, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private UnidadAdminDTO catalogoValoresDireccionSelec;

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private UnidadAdminDTO catalogoValoresSubDireccionSelec;

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private CatalogosValoresDTO catalogoValoresLineaAyudaSelec;

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private UnidadAdminDTO catalogoValoresAreaAdministrativaSelec;

    /** Filtro para el campo cdAcronimo */
    private String cdAcronimoFilter;

    /** Filtro para el campo nmAnoFilter */
    private Integer nmAnoFilter;

    /** Filtro para el campo dsDescripcion */
    private String dsDescripcionFilter;

    /** Filtro para el campo idModalidadConvocatoria */
    private Integer idModalidadConvocatoriaFilter;

    /**
     * listadoDireccionFiltro
     */
    private List<UnidadAdminDTO> listadoDireccionFiltro;

    /**
     * listadoSubdireccionFiltro
     */
    private List<UnidadAdminDTO> listadoSubdireccionFiltro;

    /**
     * listadoAreaAdministrativaFiltro
     */
    private List<UnidadAdminDTO> listadoAreaAdministrativaFiltro;

    // Atributos del catalogo valor elegido en los filtros

    /**
     * Dto del bean
     */
    private ConvocatoriaDTO convocatoriaDTO;
    
    
    private byte[]  documentoAdjunto;

    /**
     * Atributo que indica que pestana se encuentra activa.
     */
    private int pestanaActiva;
    
    
    private Boolean itAlegacion;
 
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    /** InfoUsuario */
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;

    /** solicitudBean */
    @Autowired
    @Qualifier("solicitudBean")
    private SolicitudBean solicitudBean;

    /** resolucionConvBean */
    @Autowired
    @Qualifier("resolucionConvBean")
    private ResolucionConvBean resolucionConvBean;

    @Autowired
    @Qualifier("expedienteBean")
    private ExpedienteBean expedienteBean;

    @Autowired
    @Qualifier("documentoBean")
    private DocumentoBean documentoBean;

    @Autowired
    @Qualifier("listadosBean")
    private ListadosBean listadosBean;
    
    @Autowired
    @Qualifier("envioNotificacionesBean")
    private EnvioNotificacionesBean envioNotificacionesBean;
    
    
    @Autowired
    @Qualifier("envioNotificacionesEnviadasBean")
    private EnvioNotificacionesEnviadasBean envioNotificacionesEnviadasBean;
    
    
    @Autowired
    @Qualifier("envioNotificacionesNoteBean")
    private EnvioNotificacionesNoteBean envioNotificacionesNoteBean;
    
    
    @Autowired
    @Qualifier("justificacionCieTecBean")
    private JustificacionCieTecBean justificacionCieTecBean;
    
    @Autowired
    @Qualifier("justificacionEcoBean")
    private JustificacionEcoBean justificacionEcoBean;
    
    
    @Autowired
    @Qualifier("envioNotificacionesEnviadasNoteBean")
    private EnvioNotificacionesEnviadasNoteBean envioNotificacionesEnviadasNoteBean;
    
    @Autowired
    @Qualifier("requisitoBean")
    private RequisitoBean requisitoBean;
    
    
    @Autowired
    @Qualifier("resolucionBean")
    private ResolucionBean resolucionBean;
    
    @Autowired
    @Qualifier("evaluacionJusTecBean")
    private EvaluacionJusTecBean evaluacionJusTecBean;
    
    /**
     * Servicio de ConvocatoriaAct
     */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;
    
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /** Servicio asociado a la entidad Convocatorias */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
    
    /** Servicio de convocatorias GESTOR */
    @Autowired
    @Qualifier("convocatoriaGestorService")
    private ConvocatoriaGestorService convocatoriaGestorService;

    /**
     * Servicio de UnidadAdminService
     */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public ConvocatoriasBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad Convocatorias
     *
     * @return entidad de tipo Convocatorias
     */
    @Override
    public Convocatoria crearEntidad() {
        return new Convocatoria();
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.infoUsuarioBean.loadUserData();
        this.resolucionConvBean.setConvocatoriasBean(this);
        this.expedienteBean.setConvocatoriasBean(this);
        this.solicitudBean.setConvocatoriasBean(this);
        this.setFiltros(new ArrayList<>());
        this.inicializarValoresFiltros();
        this.convocatoriaDTO = new ConvocatoriaDTO();
        this.setModoConsulta(Boolean.FALSE);

        //El Auditor lo puede ver todo como el SuperAdministrador pero en modo consulta
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolAuditor())) {
            this.setModoConsulta(Boolean.TRUE);
        }

        //VMM 11/11/24 MD-657: a nivel de convocatorias estos perfiles podrian ver lo que 
        //coincida con sus datos de Unidad Admin (area, subdireccion o direccion), 
        //luego ya se caparán las acciones
        //Para ver Datos de las convocatorias nos fijamos en la Unidad Admin del usuario
        //Para saber que Acciones pueden hacer nos fijamos en el perfil o grupo del usuario
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolAdministrador())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolSupervisor())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestor())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) {
            this.cargarDatos();
        }

        this.filtrarListaConvocatorias();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PUBLIC ACTIONS
    // ////////////////////////////////////////////////////////

    /**
     * Recupera el listado de entidades Convocatorias que cumple con los
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
    public AtlasListAndCount<ConvocatoriaDTO> obtenerListaConvocatorias(int index, int pageSize,
            Object ordenacion, Object filter) {
        AtlasListAndCount<ConvocatoriaDTO> listado = new AtlasListAndCount<>();

        List<AtlasFilter> filtros = (List<AtlasFilter>) filter;

        if (!this.getFiltros().isEmpty()) {
            filtros = this.getFiltros();
        }

        try {
             //VMM 11/11/24 MD-657: se descomenta este trozo para que coja los gestores extraordinarios de las unidades tramitadoras
             //y pueda ver las convocatorias asociadas, añadimos los roles de Administrador y Supervisor porque solo habia Gestores
             //el superAdmin y el Auditor siempre lo ven todo
             String cdUsuarioGestor = Boolean.TRUE.equals(this.infoUsuarioBean.getRolAdministrador()) ||
                                     Boolean.TRUE.equals(this.infoUsuarioBean.getRolSupervisor()) ||
                                     Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno()) || 
                                     Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestor()) ?
                                     this.infoUsuarioBean.getLogin() : null;


            ConvocatoriaPaginacionDTO resultado = this.convocatoriaService
                    .buscarConvocatoriasTramitacion(index, pageSize, filtros,
                            (List<AtlasSort>) ordenacion, cdUsuarioGestor);

            listado.setResultados(resultado.getLstConvocatorias());
            listado.setTotal(resultado.getTotalRegistros());

        } catch (ServiceException exc) {
            log.error("Se ha producido un error al obtener el listado de entidades", exc);
            // Se muestra un mensaje de error al usuario
            AtlasMessageUtils
                    .addErrorMessageFromBundle("Error al obtener el listado de convocatorias");
        }

        return listado;
    }

    /**
     * Obtener Lista de catalogos para elegir el filtro del catalogo Obtiene la
     * lista de catalogosValores del catalogo de tipo "Direccion"
     *
     * @param index
     * @param pageSize
     * @param orderBean
     * @param filtros
     * @return List<CatalogosValoresDTO>
     */
    public List<UnidadAdminDTO> obtenerListaDireccionFiltro(final int index, final int pageSize,
            List<AtlasSort> orderBean, List<AtlasFilter> filtros) {
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        this.listadoDireccionFiltro = new ArrayList<>();

        if (!filtros.isEmpty()) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSUNIDAD,
                    filtros.get(0).getValue(), filtros.get(0).getOperator()));
        }
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 2,
                Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                Constantes.NivelOrganizativo.CD_CONSEJERIA, Operador.NOT_EQUALS));

        this.catalogoValoresSubDireccionSelec = new UnidadAdminDTO();
        this.catalogoValoresAreaAdministrativaSelec = new UnidadAdminDTO();

        if (this.getTotalEntidades() == 0) {
            try {
                this.listadoDireccionFiltro = this.unidadAdminService.buscar(index, pageSize,
                        filtrosEntrada, orderBean);

            } catch (ServiceException exc) {
                log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
                // Se muestra un mensaje de error al usuario
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            }
        }
        return this.listadoDireccionFiltro;
    }

    /**
     * Obtener total catalogos del tipo Direccion del filtro.
     *
     * @param filtros
     * @return
     */
    public int obtenerTotalListaDireccionFiltro(List<AtlasFilter> filtros) {

        Integer iTotal = 0;

        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 2,
                Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                Constantes.NivelOrganizativo.CD_CONSEJERIA, Operador.NOT_EQUALS));

        try {
            iTotal = this.unidadAdminService.count(filtrosEntrada);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_TOTAL_CATALOGOS);
        }
        return iTotal;
    }

    /**
     * Obtener Lista de catalogos para elegir el filtro del catalogo Obtiene la
     * lista de catalogosValores del catalogo de tipo "Subdireccion"
     *
     * @param index
     * @param pageSize
     * @param orderBean
     * @param filtros
     * @return List<CatalogosValoresDTO>
     */
    public List<UnidadAdminDTO> obtenerListaSubdireccionFiltro(final int index, final int pageSize,
            List<AtlasSort> orderBean, List<AtlasFilter> filtros) {
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        this.listadoSubdireccionFiltro = new ArrayList<>();

        if (this.catalogoValoresDireccionSelec == null) {
            this.catalogoValoresDireccionSelec = new UnidadAdminDTO();
        }

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDADPADRE,
                this.catalogoValoresDireccionSelec.getCdUnidadPadre(), Operador.EQUALS));

        this.catalogoValoresAreaAdministrativaSelec = new UnidadAdminDTO();

        if (!filtros.isEmpty()) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSUNIDAD,
                    filtros.get(0).getValue(), filtros.get(0).getOperator()));
        }
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 3,
                Operador.EQUALS));
        if (this.getTotalEntidades() == 0) {
            try {
                this.listadoSubdireccionFiltro = this.unidadAdminService.buscar(index, pageSize,
                        filtrosEntrada, orderBean);

            } catch (ServiceException exc) {
                log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
                // Se muestra un mensaje de error al usuario
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            }
        }
        return this.listadoSubdireccionFiltro;
    }

    /**
     * Obtener total catalogos del tipo Subdireccion del filtro.
     *
     * @param filtros
     * @return
     */
    public int obtenerTotalListaSubdireccionFiltro(List<AtlasFilter> filtros) {

        Integer iTotal = 0;

        List<AtlasFilter> filtrosEntrada = new ArrayList<>();

        if (this.catalogoValoresDireccionSelec == null) {
            this.catalogoValoresDireccionSelec = new UnidadAdminDTO();
        }

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDADPADRE,
                this.catalogoValoresDireccionSelec.getCdUnidadPadre(), Operador.EQUALS));

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 3,
                Operador.EQUALS));
        try {
            iTotal = this.unidadAdminService.count(filtrosEntrada);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_TOTAL_CATALOGOS);
        }

        return iTotal;
    }

    /**
     * Obtener Lista de catalogos para elegir el filtro del catalogo Obtiene la
     * lista de catalogosValores del catalogo de tipo "Area Administrativa"
     *
     * @param index
     * @param pageSize
     * @param orderBean
     * @param filtros
     * @return List<CatalogosValoresDTO>
     */
    public List<UnidadAdminDTO> obtenerListaAreaAdministrativaFiltro(final int index,
            final int pageSize, List<AtlasSort> orderBean, List<AtlasFilter> filtros) {
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        this.listadoAreaAdministrativaFiltro = new ArrayList<>();

        // Si subdireccion esta vacia, lo obtenemos de la direccion
        filtrosEntrada = this.obtenerListaAreaAdministrativaFiltroSelec(filtrosEntrada);

        if (!filtros.isEmpty()) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSUNIDAD,
                    filtros.get(0).getValue(), filtros.get(0).getOperator()));
        }

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 4,
                Operador.EQUALS));
        if (this.getTotalEntidades() == 0) {
            try {

                this.listadoAreaAdministrativaFiltro = this.unidadAdminService.buscar(index,
                        pageSize, filtrosEntrada, orderBean);

            } catch (ServiceException exc) {
                log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
                // Se muestra un mensaje de error al usuario
                AtlasMessageUtils
                        .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            }
        }
        return this.listadoAreaAdministrativaFiltro;
    }

    /**
     * Obtener total catalogos del tipo Area Administrativa del filtro.
     *
     * @param filtros
     * @return
     */
    public int obtenerTotalAreaAdministrativaFiltro(List<AtlasFilter> filtros) {

        Integer iTotal = 0;

        List<AtlasFilter> filtrosEntrada = new ArrayList<>(filtros);

        if (this.catalogoValoresDireccionSelec == null) {
            this.catalogoValoresDireccionSelec = new UnidadAdminDTO();
        }

        if (this.catalogoValoresSubDireccionSelec == null) {
            this.catalogoValoresSubDireccionSelec = new UnidadAdminDTO();
        }

        // Si subdireccion no esta vacia, lo obtenemos de la subdireccion
        filtrosEntrada = this.obtenerListaAreaAdministrativaFiltroSelec(filtrosEntrada);

        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 4,
                Operador.EQUALS));
        try {
            iTotal = this.unidadAdminService.count(filtrosEntrada);
        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_TOTAL_CATALOGOS);
        }
        return iTotal;
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
    public void filtrarListaConvocatorias() {
        this.setFiltros(new ArrayList<>());
        this.filtrarCatalogoValores();

        if (StringUtils.isNotBlank(this.cdAcronimoFilter)) {
            this.getFiltros().add(new AtlasFilter("cdAcronimo", "%" + this.cdAcronimoFilter + "%",
                    Operador.LIKE_INSENSITIVE));
        }

        if (StringUtils.isNotBlank(this.dsDescripcionFilter)) {
            this.getFiltros().add(new AtlasFilter("dsDescripcion",
                    "%" + this.dsDescripcionFilter + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.idModalidadConvocatoriaFilter != null) {
            this.getFiltros().add(new AtlasFilter("idModalidadConvocatoria.idValor",
                    this.idModalidadConvocatoriaFilter, Operador.EQUALS));
        }

        this.getFiltros().add(new AtlasFilter("idEstado.idValor",
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.EN_CONSTRUCCION), Operador.NOT_EQUALS));

        if (this.nmAnoFilter != null) {
            this.getFiltros().add(new AtlasFilter("nmAno", this.nmAnoFilter, Operador.EQUALS));

        }
        this.getFiltros().add(new AtlasFilter("itConvocatoriaActiva",
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));

    }

    /**
     * Método que borra los filtros del listado.
     */
    public void borrarFiltros() {
        this.setTotalEntidades(0);
        this.setFiltros(new ArrayList<>());
        this.inicializarValoresFiltros();

        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolAdministrador())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolSupervisor())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestor())) {
            this.cargarDatos();
        }

        this.filtrarListaConvocatorias();
    }

    /**
     * Vuelve al listado
     *
     * @return Devuelve la regla de navegación hacia atrás
     */
    public String volverListado() {
        this.setEntidad(this.crearEntidad());
        this.filtrarListaConvocatorias();
        return NavigationResults.LISTADO_CONVOCATORIA;
    }

    /**
     * Método invocado desde: el botón 'editar' de cada fila de la lista
     * paginada
     *
     * Este metodo obtiene una entidad a partir de un identificador/PK obtenida
     * del evento y redirige a la página de edición
     *
     * @param idConvocatoria Identificador de la entidad Convocatorias a cargar
     * @param modoConsulta Indica si se visualiza en modo consulta o en modo
     *            edición
     * @return regla de navegación definida en NavigationResults.java
     */
    public String cargar(Integer idConvocatoria) {
        String result = null;
        if (idConvocatoria != null) {
            this.setPestanaActiva(0);
            this.cargarConvocatoria(idConvocatoria);
            result = NavigationResults.FORMULARIO_REVISION_ADM;
        } else {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.CONVOCATORIAS_MENSAJE_CARGAR_KO);
        }
        return result;
    }

    /**
     * Método para controlar número de solapa activa
     *
     * @param numeroSolapa Número de solapa
     */
    public void irASolapa(int numeroSolapa) {
        this.pestanaActiva = numeroSolapa;
    }
    
    public void irASolapaSegunTablaSeleccionada(String tablaSeleccionada) {
        if (StringUtils.isBlank(tablaSeleccionada) 
                || Constantes.RadioSelectOpcion.OPCION_SOLICITUDES.equals(tablaSeleccionada)) {
            this.irASolapa(1); // Va a solapa de solicitudes
        } else {
            this.irASolapa(0); // Va a solapa de expedientes
        }
    }

    /**
     * Asignacion de la modalidad de convocatoria cuando se cambia en el combo
     * del filtro
     *
     * @param oldValue
     * @param newValue
     */
    public void cambioModalidad(Object oldValue, Object newValue) {
        if (oldValue != newValue) {
            Integer valorNuevo = (Integer) newValue;
            this.setIdModalidadConvocatoriaFilter(valorNuevo);
        }
    }

    /**
     * Metodo que convierte los desplegables, que se encuentran en Gentiadmin, a
     * UnidadAdmin para que puedan ser guardados correctamente en base de datos
     * para la DIRECCION
     *
     */
    public void convertirGentiadminAUnidadAdminDireccion(Object selected) {

        if (selected != null) {
            UnidadAdminDTO uniAdminDTO = (UnidadAdminDTO) selected;
            this.getConvocatoriaDTO().setIdDireccion(uniAdminDTO);

            this.getConvocatoriaDTO().setIdSubdireccion(new UnidadAdminDTO());
            this.getConvocatoriaDTO().setIdAreaAdministrativa(new UnidadAdminDTO());

        } else {

            this.catalogoValoresSubDireccionSelec = new UnidadAdminDTO();
            this.catalogoValoresAreaAdministrativaSelec = new UnidadAdminDTO();

        }
    }

    /**
     * Metodo que convierte los desplegables, que se encuentran en Gentiadmin, a
     * UnidadAdmin para que puedan ser guardados correctamente en base de datos
     * para la SUBDIRECCION
     *
     */
    public void convertirGentiadminAUnidadAdminSubDireccion(Object selected) {

        if (selected != null) {
            UnidadAdminDTO unidadAdminDTO = (UnidadAdminDTO) selected;
            if (StringUtils.isNotBlank(unidadAdminDTO.getDsUnidad())) {

                this.getConvocatoriaDTO().setIdSubdireccion(unidadAdminDTO);

                this.getConvocatoriaDTO().setIdAreaAdministrativa(new UnidadAdminDTO());

            }

        } else {

            this.catalogoValoresAreaAdministrativaSelec = new UnidadAdminDTO();

        }
    }

    /**
     * Metodo que convierte los desplegables, que se encuentran en Gentiadmin, a
     * UnidadAdmin para que puedan ser guardados correctamente en base de datos
     * para la AREA ADMINISTRATIVA
     *
     */
    public void convertirGentiadminAUnidadAdminAreaAdmin(Object selected) {

        if (selected != null) {
            UnidadAdminDTO unidadAdminDTO = (UnidadAdminDTO) selected;
            this.getConvocatoriaDTO().setIdAreaAdministrativa(unidadAdminDTO);

        }
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// PRIVATE METHODS
    // ////////////////////////////////////////////////////////

    /**
     * Metodo que inicializa los catalogosValores que se van a seleccionar en
     * los filtros. Se evita null pointer
     */
    private void inicializarValoresFiltros() {
        this.catalogoValoresDireccionSelec = new UnidadAdminDTO();
        this.catalogoValoresSubDireccionSelec = new UnidadAdminDTO();
        this.catalogoValoresAreaAdministrativaSelec = new UnidadAdminDTO();
        this.catalogoValoresLineaAyudaSelec = new CatalogosValoresDTO();
        this.cdAcronimoFilter = null;
        this.nmAnoFilter = null;
        this.dsDescripcionFilter = null;
        this.idModalidadConvocatoriaFilter = null;
    }

    /**
     * Realiza la carga de los datos del supervisor
     */
    private void cargarDatos() {

        UnidadAdminDTO unidadAdmin = null;
        List<UnidadAdminDTO> listaUnidadAdmin;

        try {

            List<AtlasFilter> filter = new ArrayList<>();

            if (this.infoUsuarioBean.getDireccion() != null
                    && this.infoUsuarioBean.getDireccion().getCdUnidad() != null) {
                filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                        this.infoUsuarioBean.getDireccion().getCdUnidad(), Operador.EQUALS));

                listaUnidadAdmin = this.unidadAdminService.obtenerPorFiltros(filter);
                unidadAdmin = listaUnidadAdmin.get(0);
            }
            // Carga de la Direccion
            if (unidadAdmin != null && StringUtils.isNotBlank(unidadAdmin.getCdUnidad())) {
                this.setCatalogoValoresDireccionSelec(unidadAdmin);
                unidadAdmin.setIdUnidadAdmin(unidadAdmin.getIdUnidadAdmin());
                this.getConvocatoriaDTO().setIdDireccion(unidadAdmin);
                unidadAdmin = null;
            }

            if (this.infoUsuarioBean.getSubDireccion() != null
                    && this.infoUsuarioBean.getSubDireccion().getCdUnidad() != null) {
                filter.clear();
                filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                        this.infoUsuarioBean.getSubDireccion().getCdUnidad(), Operador.EQUALS));

                listaUnidadAdmin = this.unidadAdminService.obtenerPorFiltros(filter);
                unidadAdmin = listaUnidadAdmin.get(0);
            }
            // Carga de la Subdireccion
            if (unidadAdmin != null && StringUtils.isNotBlank(unidadAdmin.getCdUnidad())) {
                this.setCatalogoValoresSubDireccionSelec(unidadAdmin);
                unidadAdmin.setIdUnidadAdmin(unidadAdmin.getIdUnidadAdmin());
                this.getConvocatoriaDTO().setIdSubdireccion(unidadAdmin);
                unidadAdmin = null;
            }

            if (this.infoUsuarioBean.getUnidadAdmin() != null
                    && this.infoUsuarioBean.getUnidadAdmin().getCdUnidad() != null) {
                filter.clear();
                filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                        this.infoUsuarioBean.getUnidadAdmin().getCdUnidad(), Operador.EQUALS));

                listaUnidadAdmin = this.unidadAdminService.obtenerPorFiltros(filter);
                unidadAdmin = listaUnidadAdmin.get(0);
            }
            // Carga de la Unidad Administrativa
            if (unidadAdmin != null && StringUtils.isNotBlank(unidadAdmin.getCdUnidad())) {
                this.setCatalogoValoresAreaAdministrativaSelec(unidadAdmin);
                unidadAdmin.setIdUnidadAdmin(unidadAdmin.getIdUnidadAdmin());
                this.getConvocatoriaDTO().setIdAreaAdministrativa(unidadAdmin);
            }

        } catch (ServiceException e) {
            log.error("Error cargando los datos de las organizaciones");
        }
    }

    /**
     * Metodo que obtiene la convocatoria cuyo id pasamos por parametro
     *
     * @param idConvocatoria
     */
    private void cargarConvocatoria(Integer idConvocatoria) {
        try {
            this.solicitudBean.setIdConvocatoria(idConvocatoria);
            this.resolucionConvBean.setIdConvocatoria(idConvocatoria);
            this.expedienteBean.setIdConvocatoria(idConvocatoria);
            this.documentoBean.setIdConvocatoria(idConvocatoria);
            this.justificacionCieTecBean.setIdConvocatoria(idConvocatoria);
            this.justificacionEcoBean.setIdConvocatoria(idConvocatoria);
            this.requisitoBean.setIdConvocatoria(idConvocatoria);
            this.resolucionBean.setIdConvocatoria(idConvocatoria);
            this.listadosBean.setIdConvocatoria(idConvocatoria);
            this.envioNotificacionesBean.setIdConvocatoria(idConvocatoria);
            this.envioNotificacionesEnviadasBean.setIdConvocatoria(idConvocatoria);
            this.envioNotificacionesNoteBean.setIdConvocatoria(idConvocatoria);
            this.envioNotificacionesEnviadasNoteBean.setIdConvocatoria(idConvocatoria);
            this.listadosBean.verificarBloqueoDescarga();
            this.setEntidad(this.convocatoriaService.find(idConvocatoria));
            this.setConvocatoriaDTO(this.convocatoriaService.buscarId(idConvocatoria));
            this.solicitudBean.setNmDiasSubsanacion(this.convocatoriaDTO.getNmDiasSubsanacion());
            this.evaluacionJusTecBean.setIdConvocatoria(idConvocatoria);
            this.requisitoBean.setIdConvocatoria(idConvocatoria); 
            this.requisitoBean.setIdEstadoConvocatoria(this.convocatoriaDTO.getIdEstado().getIdValor());
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA)
                    .equals(this.convocatoriaDTO.getIdEstado().getIdValor())) {
                this.setModoConsulta(Boolean.TRUE);
            }
            if (StringUtils.isBlank(this.getConvocatoriaDTO().getItContratos()) || this
                    .getConvocatoriaDTO().getItContratos().equals(Constantes.AfirmacionNegacion.N)) {
                this.convocatoriaDTO.setEsConvocatoriaContrato(Boolean.FALSE);
            } else {
                this.convocatoriaDTO.setEsConvocatoriaContrato(Boolean.TRUE);
            }
            
            if (StringUtils.isBlank(this.getConvocatoriaDTO().getItAlegacion()) || this
                    .getConvocatoriaDTO().getItAlegacion().equals(Constantes.AfirmacionNegacion.N)) {
                this.setItAlegacion(Boolean.FALSE);
            } else {
                this.setItAlegacion(Boolean.TRUE);
            }
            
            
            //VMM 16/10/24 MD-657: comprobamos si el usuario logado es un gestor excepcional y de que tipo
            this.comprobarGestoresExcep(this.convocatoriaDTO);
            this.solicitudBean.setConvocatoriaDTO(this.convocatoriaDTO);
            this.expedienteBean.setConvocatoriaDTO(this.convocatoriaDTO);
            
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Mensajes.CONVOCATORIAS_MENSAJE_CARGAR_KO, e);
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.CONVOCATORIAS_MENSAJE_CARGAR_KO);
        }
    }

    /**
     * Metodo que que filtra los catalogos valores. Se usa para evitar errores
     * de sonar
     */
    private void filtrarCatalogoValores() {
        if (this.catalogoValoresDireccionSelec != null
                && StringUtils.isNotBlank(this.catalogoValoresDireccionSelec.getDsUnidad())) {
            this.getFiltros()
                    .add(new AtlasFilter("idDireccion.dsUnidad",
                            "%" + this.catalogoValoresDireccionSelec.getDsUnidad() + "%",
                            Operador.LIKE_INSENSITIVE));
        }

        if (this.catalogoValoresSubDireccionSelec != null
                && StringUtils.isNotBlank(this.catalogoValoresSubDireccionSelec.getDsUnidad())) {
            this.getFiltros()
                    .add(new AtlasFilter("idSubdireccion.dsUnidad",
                            "%" + this.catalogoValoresSubDireccionSelec.getDsUnidad() + "%",
                            Operador.LIKE_INSENSITIVE));
        }

        if (this.catalogoValoresAreaAdministrativaSelec != null && StringUtils
                .isNotBlank(this.catalogoValoresAreaAdministrativaSelec.getDsUnidad())) {
            this.getFiltros()
                    .add(new AtlasFilter("idAreaAdministrativa.dsUnidad",
                            "%" + this.catalogoValoresAreaAdministrativaSelec.getDsUnidad() + "%",
                            Operador.LIKE_INSENSITIVE));
        }
        if (this.catalogoValoresLineaAyudaSelec != null
                && StringUtils.isNotBlank(this.catalogoValoresLineaAyudaSelec.getDsValor())) {
            this.getFiltros()
                    .add(new AtlasFilter("idLineaAyuda.dsValor",
                            "%" + this.catalogoValoresLineaAyudaSelec.getDsValor() + "%",
                            Operador.LIKE_INSENSITIVE));
        }

    }

    /**
     * Metodo que se llama en caso de tener la subdireccionVacia. Recuperamos
     * todos los datos con idNivelOrg = 3 (area administrativa) y comprobamos
     * que su padre, tenga como padre aquella direccion que hemos seleccionado
     *
     * @param bListado: TRUE si estamos en el listado de filtros. False si es en
     *            el detalle.
     */
    private List<AtlasFilter> anadirFiltroPorDireccion(Boolean bListado) {
        List<UnidadAdminDTO> listaUnidadAdminDTO;
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDNIVELORG_IDNIVELORG, 4,
                Operador.EQUALS));
        try {

            // Obtenemos todas las unidad admin con idNivelOrg = 3
            listaUnidadAdminDTO = this.unidadAdminService.obtenerPorFiltros(filtrosEntrada);

            // para cada unidad admin, vemos a su padre. En aso de ser una
            // dirección, lo insertamos en el filtro. En caso de ser una
            // subdireccion, insertamos en el filtro el padre de la misma
            for (UnidadAdminDTO unidadAdminAreaDTO : listaUnidadAdminDTO) {
                UnidadAdminDTO unidadAdminPadreDTO = this.unidadAdminService
                        .buscarCdUnidad(unidadAdminAreaDTO.getCdUnidadPadre());
                // Si el padre NO tiene padre, significa que es una DIRECCION.
                // Insertamos el filtro del area
                if (StringUtils.isBlank(unidadAdminPadreDTO.getCdUnidadPadre())) {
                    // Es una direccion
                    // Si nos encontramos en la pantalla del listado
                    if (Boolean.TRUE.equals(bListado)) {
                        filtrosEntrada = this.anadirFiltroDireccionSelec(filtrosEntrada,
                                unidadAdminAreaDTO);
                    }
                }
            }

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
            // Se muestra un mensaje de error al usuario
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO_CATALOGOS);
        }
        return filtrosEntrada;
    }

    /**
     * Metodo que inserta el filtro de entrada para una direccion seleccionada,
     * es decir, en la lista de convocatorias. Se usa para evitar error en sonar
     *
     * @param filtrosEntrada
     * @param unidadAdminAreaDTO
     * @return filtrosEntrada
     */
    private List<AtlasFilter> anadirFiltroDireccionSelec(List<AtlasFilter> filtrosEntrada,
            UnidadAdminDTO unidadAdminAreaDTO) {

        // Comrpobamos que sea la direccion que hemos
        // seleccionado
        if (this.catalogoValoresDireccionSelec != null && unidadAdminAreaDTO.getCdUnidadPadre()
                .equals(this.catalogoValoresDireccionSelec.getCdUnidad())) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                    unidadAdminAreaDTO.getCdUnidad(), Operador.EQUALS));
        } else if (this.catalogoValoresDireccionSelec != null
                && this.catalogoValoresDireccionSelec.getCdUnidad() != null) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDAD,
                    unidadAdminAreaDTO.getCdUnidad(), Operador.NOT_EQUALS));
        }
        return filtrosEntrada;
    }

    /**
     * Metodo que se usa para obtener las areas administrativas en la parte del
     * detalle. Lo usamos para evitar errores de sonar
     *
     * @param filtrosEntrada
     * @return filtrosEntrada
     */
    private List<AtlasFilter> obtenerListaAreaAdministrativaFiltroSelec(
            List<AtlasFilter> filtrosEntrada) {
        if (StringUtils.isNotBlank(this.catalogoValoresSubDireccionSelec.getCdUnidadPadre())) {
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDUNIDADPADRE,
                    this.catalogoValoresSubDireccionSelec.getCdUnidad(), Operador.EQUALS));
        } else {
            filtrosEntrada = this.anadirFiltroPorDireccion(Boolean.TRUE);

        }
        return filtrosEntrada;
    }
    
    
    /**
     * Metodo para validar si se permite al usuario editar o eliminar la
     * convocatoria
     *
     * @param idConvocatoria
     *
     * @return
     */
    public Boolean mostrarEdicionConvocatoria(Integer idConvocatoria) throws ServiceException {

        Boolean permiteEditar = Boolean.FALSE;

        ConvocatoriaGestorDTO gestor = this.convocatoriaGestorService
                .obtenerPorIdConvocatoriaCodigoUsuario(idConvocatoria,
                        this.infoUsuarioBean.getLogin());
        
        if (Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA
                .equals(this.convocatoriaDTO.getTipoResolModalidadDTO().getCdTipoResolucion())) {
            if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolSuperAdministrador())
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolAdministrador())
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolSupervisor())
                    || Boolean.TRUE.equals(this.infoUsuarioBean.getRolAuditor())) {
                permiteEditar = Boolean.TRUE;
            } else if (gestor.getIdConvocatoriaGestor() != null
                    && Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestor())) {
                permiteEditar = Boolean.TRUE;
            }
        }
        return permiteEditar;
    }
    
    
    
    public Boolean compropbarAlegacion()throws ServiceException {

        Boolean estado;

        if (StringUtils.isBlank(this.getConvocatoriaDTO().getItAlegacion()) || this
                .getConvocatoriaDTO().getItAlegacion().equals(Constantes.AfirmacionNegacion.S)) {
            estado = Boolean.TRUE;
        } else {
            estado = Boolean.FALSE;
        }

        return estado;
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
           //actualizamoslos valores de la convocatoria para activar o no las pestañas
           this.setConvocatoriaDTO(conv);
       }
        
    }
    
    /**
     * método para mostrar el mensaje infotmativo debajo de resolver la convocatoria 
     *
     * @return
     */
    public Boolean mostrarMensajeInfoResolverConv()  {
        Boolean mostrar = Boolean.FALSE;

        if (this.convocatoriaDTO != null 
                && this.convocatoriaDTO.getItContratos().equals(Constantes.AfirmacionNegacion.S)) {
                mostrar = Boolean.TRUE;
        }
        return mostrar;
    }
    
    
    /**
     * método para mostrar el mensaje infotmativo debajo de resolver la convocatoria si se han traslada ya las fechas
     *
     * @return
     */
    public Boolean mostrarMensajeInfoTrasFechas()  {
        Boolean mostrar = Boolean.FALSE;

        if (this.convocatoriaDTO != null 
                && this.convocatoriaDTO.getItTrasladoFechasCont().equals(Constantes.AfirmacionNegacion.S)) {
                mostrar = Boolean.TRUE;
        }
        return mostrar;
    }
  
}
