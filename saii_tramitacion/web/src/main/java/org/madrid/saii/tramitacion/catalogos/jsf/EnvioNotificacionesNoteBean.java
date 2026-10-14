package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.EnvioDocumentoPTFRService;
import org.madrid.saii.lib.services.EnvioNotificacionesPortafirmasService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
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
 * @see EnvioNotificacionesNoteBean
 */
@Slf4j
@Getter
@Setter
@Component("envioNotificacionesNoteBean")
@Scope("request")
public class EnvioNotificacionesNoteBean extends CatalogoBean<EnvioNotificacionesPortaFirma, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * dto de solicitud
     */
    private EnvioNotificacionPortafirmasDTO envioNotificacionPortafirmasDTO;

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
    private Integer idCodigo;

    /**
     * Catalogo que usamos para seleccionar el filtro.
     */
    private CatalogosValoresDTO areaCientificaFilter;

    /** Filtro para el campo interesado */
    private InteresadoDTO interesadoFilter;

    /** Flag para mostar solicitudes iniciadas */
    private Boolean mostrarIniciadas;

    //////////////////filtros///////////////////
    

    
    private String cdNumExpdienteFir;

    private String dsDestinatarioFir;

    private String dsFirmanteFir;
 
    private Integer dsTramiteFir;

    private String dsVerificadorFir;
   
    private Date fcEnvioColaDesdeFir;
    
    private Date fcEnvioColaHastaFir;
    

    
    

    /** Filtro para el campo nmAnoFilter */
    private Integer nmAnoFilter;

    /** Filtro para el campo dsDescripcion */
    private String dsDescripcionFilter;

    /** Filtro para el campo idModalidadConvocatoria */
    private Integer idModalidadConvocatoriaFilter;

    
    
    
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de SolicitudService
     */
    @Autowired
    @Qualifier("EnvioNotificacionesPortafirmasService")
    private EnvioNotificacionesPortafirmasService envioNotificacionesPortafirmasService;
    
    /**
     * Servicio de documentoService
     */
    @Autowired
    @Qualifier("envioDocumentoPtfrService")
    private EnvioDocumentoPTFRService envioDocumentoPtfrService;
    
    /**
     * Servicio de CatalogosValoresService
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
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

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public EnvioNotificacionesNoteBean() {
        super();
    }

    /**
     * Crea una nueva instancia de la entidad ConvocatoriaSol
     *
     * @return entidad de tipo ConvocatoriaSol
     */
    @Override
    public EnvioNotificacionesPortaFirma crearEntidad() {
        return null;
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        this.inicializarEnvioNotificacionPortafirmasDTO();
   
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
    public AtlasListAndCount<EnvioDocumentoPTFR> obtenerListaEnviosNote(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<EnvioDocumentoPTFR> listado = new AtlasListAndCount<EnvioDocumentoPTFR>();
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;
                
                if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                    filtrosEntrada = this.getFiltros();
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,this.idConvocatoria, Operador.EQUALS));


                if (Objects.isNull(orderBean)) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));

                List<EnvioDocumentoPTFR> listadoSolicitudes = this.envioDocumentoPtfrService
                        .buscarEnviosDocumentoPortafirmas(index, pageSize, filtrosEntrada, orden,this.idConvocatoria) ;
                listado.setResultados(listadoSolicitudes);
                listado.setTotal(this.envioNotificacionesPortafirmasService.count(filtrosEntrada));
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
    public void inicializarEnvioNotificacionPortafirmasDTO() {
        this.envioNotificacionPortafirmasDTO = new EnvioNotificacionPortafirmasDTO();


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

        if (StringUtils.isNotBlank(this.getCdNumExpdienteFir())) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_EXPEDIENTE,
                    "%" + this.getCdNumExpdienteFir() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if ( StringUtils.isNotBlank(this.getDsDestinatarioFir())) {
            this.getFiltros().add(new AtlasFilter("dsDestinatario",
                    "%" + this.getDsDestinatarioFir() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if ( StringUtils.isNotBlank(this.getDsFirmanteFir())) {
            this.getFiltros().add(new AtlasFilter("dsFirmante",
                    "%" + this.getDsFirmanteFir() + "%", Operador.LIKE_INSENSITIVE));
        }
        
        if (this.getDsTramiteFir() != null) {
            this.getFiltros().add(new AtlasFilter("dsTramite",
                     this.getDsTramiteFir() , Operador.EQUALS));
        }
        
        if ( StringUtils.isNotBlank(this.getDsVerificadorFir()) ) {
            this.getFiltros().add(new AtlasFilter("dsVerificador",
                    "%" + this.getDsVerificadorFir() + "%", Operador.LIKE_INSENSITIVE));
        }
           
        if (this.getFcEnvioColaDesdeFir() != null   && this.getFcEnvioColaHastaFir() != null) {    
            this.getFiltros().add(new AtlasFilter(Constantes.FC_ENVIO_COLA,this.getFcEnvioColaDesdeFir(),
                    this.getFcEnvioColaHastaFir(), Operador.BETWEEN));
        }
        
        if (this.getFcEnvioColaDesdeFir() != null && this.getFcEnvioColaHastaFir() == null) {
            this.getFiltros().add(new AtlasFilter(Constantes.FC_ENVIO_COLA,
                    this.getFcEnvioColaDesdeFir(), Operador.GREATER_OR_EQUAL));
        }
        
        if (this.getFcEnvioColaDesdeFir() == null && this.getFcEnvioColaHastaFir() != null) {
            this.getFiltros().add(new AtlasFilter(Constantes.FC_ENVIO_COLA,
                    this.getFcEnvioColaHastaFir(), Operador.LESS_OR_EQUAL));
        }
        
    }

    /**
     * Método que borra los filtros del listado.
     */
    public void borrarFiltros() {
        this.setMostrarBaja(false);
        this.setTotalEntidades(0);
        this.setRealizarBusqueda(Boolean.FALSE);
        this.setFiltros(new ArrayList<>());
        this.inicializarEnvioNotificacionPortafirmasDTO();
        this.filtrar();

    }
    
    /**
     * Metodo para rellenar el combo fondos Europeos con los fondos aun no
     * agregados a la convocatoria
     *
     * @return lstItem the List<SelectItem> lstItem
     */
    public List<SelectItem> getComboTramites() {  //getComboTipoFondosEuropeos

        List<SelectItem> lstItem = new ArrayList<>();
        List<CatalogosValoresDTO> lstTramites = new ArrayList<>();

        try {
            //VMM 26/09/24 MD-593: ahora solo metemos los tramites del gestor (catalogo 38), revisar esto...
            lstTramites = this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR));

            Iterator<CatalogosValoresDTO> it = lstTramites.iterator();

            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();

                if (Boolean.TRUE.equals(dto.getIdValor() != 0)) {
                    lstItem.add(new SelectItem(dto.getIdValor(), dto.getDsValor()));
                }
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }

        return lstItem;
    }
    
    

}
