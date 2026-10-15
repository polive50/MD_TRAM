package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EnvioDocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.converters.DocumentoPtfrToEntity;
import org.madrid.saii.lib.dto.converters.EnvioDocumentoPTFRToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaPlaService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioDocumentoPTFRService;
import org.madrid.saii.lib.services.ExpedienteJusEcoService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.InteresadoService;
import org.madrid.saii.lib.services.PdfService;
import org.madrid.saii.lib.services.TramiteService;
import org.madrid.saii.lib.services.UnidadAdminService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.InfoUsuarioBean;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.UtilesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

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
 * @see EnvioNotificacionesBean
 */
@Slf4j
@Getter
@Setter
@Component("envioNotificacionesBean")
@Scope("request")
public class EnvioNotificacionesBean extends CatalogoBean<EnvioNotificacionesPortaFirma, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * dto de solicitud
     */
    private EnvioDocumentoPtfrDTO envioDocumentoPtfrDTO;

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

    ////////////////// filtros///////////////////

    private String cdNumExpdientePen;

    private Integer cdNumExpdienteEnv;

    private Integer cdNumExpdienteFir;

    private Integer cdNumExpdienteNot;

    private String dsDestinatarioPen;

    private String dsDestinatarioEnv;

    private String dsDestinatarioFir;

    private String dsDestinatarioNot;

    private String dsFirmantePen;

    private String dsFirmanteEnv;

    private String dsFirmanteFir;

    private String dsFirmanteNot;

    private Integer dsTramitePen;

    private Integer dsTramiteEnv;

    private Integer dsTramiteFir;

    private Integer dsTramiteNot;

    private String dsVerificadorPen;

    private String dsVerificadorEnv;

    private String dsVerificadorFir;

    private String dsVerificadorNot;

    private Date fcEnvioColaDesdePen;

    private Date fcEnvioColaHastaPen;

    private Date fcEnvioColaEnv;

    private Date fcEnvioColaFir;

    private Date fcEnvioColaNot;


    private List<EnvioDocumentoPTFR> lstEnviosPendientesPortafirmas;

    private List<EnvioDocumentoPTFR> lstEnviosFirmados;

    private List<EnvioDocumentoPTFR> lstEnviosPendientesNote;

    private List<EnvioDocumentoPTFR> lstEnviosFirmadosNote;

    private List<EnvioDocumentoPTFR> lstSeleccionados;

    private List<EnvioDocumentoPTFR> lstSeleccionadosNote;

    /** Filtro para el campo nmAnoFilter */
    private Integer nmAnoFilter;

    /** Filtro para el campo dsDescripcion */
    private String dsDescripcionFilter;

    /** Filtro para el campo idModalidadConvocatoria */
    private Integer idModalidadConvocatoriaFilter;
    
    /** DTO de la convocatoria **/
    private ConvocatoriaDTO convocatoriaDTO;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de documentoService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

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

    /**
     * Servicio de expedienteService
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    
    
    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService expedienteJusEcoService;
    
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
       
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;
    
    /**
     * Servicio Tramite
     */
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;
    
    /** Servicio PdfService */
    @Lazy
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;
    
    
    /** Servicio Convocatoria Pla */
    @Autowired
    @Qualifier("convocatoriaPlaService")
    @Setter
    @Getter
    private ConvocatoriaPlaService convocatoriaPlaService;
    
    
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;
    
    
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    @Autowired
    @Qualifier("expedienteHistDAO")
    @Setter
    private ExpedienteHistDAO expedienteHistDAO;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////    

    private ExpedienteBean expedienteBean;
    
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;
    
    
    /** DocumentoBean */
    @Autowired
    @Qualifier("documentoBean")
    private DocumentoBean documentoBean;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public EnvioNotificacionesBean() {
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
        this.inicializarEnvioDocumentoPtfrDTO();

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
     * Recupera el listado de entidades EnvioDocumentoPTFR que cumple con los
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
    public AtlasListAndCount<EnvioDocumentoPTFR> obtenerListaEnviosPortafirmas(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        
        AtlasListAndCount<EnvioDocumentoPTFR> listado = new AtlasListAndCount<>();
        //VMM 11/11/24 MD-657: inicializamos las listas de elementos seleccionados al entrar
        this.lstSeleccionados = null;
        
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                    filtrosEntrada = this.getFiltros();
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));

                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_PTFR, Operador.NULL));

                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_PTFR, Operador.NULL));
                
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL));
    
                if (Objects.isNull(orderBean)) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));

                List<EnvioDocumentoPTFR> listadoPortafirmas = this.envioDocumentoPtfrService
                        .buscarEnviosDocumentoPortafirmas(index, pageSize, filtrosEntrada, orden,
                                idConvocatoria);
                
//                for (EnvioDocumentoPTFR envioDocumentoPTFR : listadoPortafirmas) {
//                    
//                    
//                    if(envioDocumentoPTFR.getidExpedienteJustec() != null) {
//                        ExpedienteJusTecDTO expJustec = this.expedienteJusTecService.buscarPorIdExpedienteJustec(envioDocumentoPTFR.getidExpedienteJustec());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJustec.getNmPeriodo() );
//                    }
//                    
//                    //VMM 20/10/25 MD-1586: metemos la comprobacion del idExpJusEco
//                    if(envioDocumentoPTFR.getidExpedienteJusEco() != null) {
//                        ExpedienteJusEcoDTO expJusEco = this.expedienteJusEcoService.buscarPorIdExpedienteJuseco(envioDocumentoPTFR.getidExpedienteJusEco());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJusEco.getNmPeriodo() );
//                    }
//                    
//                    ExpedienteDTO expediente = this.expedienteService.buscarId(envioDocumentoPTFR.getidExpediente());
//                    if(expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null 
//                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE)) {
//                        
//                        InteresadoDTO interesado = this.interesadoService.buscarId(expediente.getRepresentanteLegal().getIdInteresado());
//                        String nombreInteresado = interesado.getDsNombre() + " " + interesado.getDsApellido1() + 
//                                (interesado.getDsApellido2() != null && !interesado.getDsApellido2().isEmpty() ? " " + interesado.getDsApellido2() : "");
//                        envioDocumentoPTFR.setDsDestinatario(nombreInteresado);
//                    }
//                    
//                  
//                }

                this.lstEnviosPendientesPortafirmas = listadoPortafirmas;
                listado.setResultados(listadoPortafirmas);
                listado.setTotal(this.envioDocumentoPtfrService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
    }

    

    private void convertirEstado(EnvioDocumentoPTFR envio) {
        
       switch (envio.getCdEstadoNote()) {
                case Constantes.Note.CD_PD:
                    envio.setCdEstadoNote(Constantes.EstadoDocumentoPtfr.DS_PENDIENTE);
                    break;
                case Constantes.Note.CD_L:
                    envio.setCdEstadoNote(Constantes.EstadoDocumentoPtfr.DS_FIRMADO);
                    break;
                case Constantes.Note.CD_R:
                    envio.setCdEstadoNote(Constantes.EstadoDocumentoPtfr.DS_RECHAZADO);
                    break;
                case Constantes.Note.CD_RA:
                    envio.setCdEstadoNote(Constantes.EstadoDocumentoPtfr.DS_RECHAZADO + " Automaticamente");
                    break;
                default:
                    break;
                }
        
    }
    
    private void convertirEstadoPTFR(EnvioDocumentoPTFR envio) {
        
        switch (envio.getCdEstadoPTFR()) {
                 case Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE:
                     envio.setCdEstadoPTFR(Constantes.EstadoDocumentoPtfr.DS_PENDIENTE);
                     break;
                 case Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO:
                     envio.setCdEstadoPTFR(Constantes.EstadoDocumentoPtfr.DS_FIRMADO);
                     break;
                 case Constantes.EstadoDocumentoPtfr.CODIGO_RECHAZADO:
                     envio.setCdEstadoPTFR(Constantes.EstadoDocumentoPtfr.DS_RECHAZADO);
                     break;
                 default:
                     break;
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
        
        AtlasListAndCount<EnvioDocumentoPTFR> listado = new AtlasListAndCount<>();
        //VMM 11/11/24 MD-657: inicializamos las listas de elementos seleccionados al entrar
        this.lstSeleccionadosNote = null;
        
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                    filtrosEntrada = this.getFiltros();
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_PTFR, Operador.NOT_NULL));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_NOTE, Operador.NULL));

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_PTFR, Constantes.EstadoDocumentoPtfr.DS_FIRMADO,
                        Operador.EQUALS));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_NOTE, Operador.NULL));

                if (Objects.isNull(orderBean)) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));

                List<EnvioDocumentoPTFR> listadoPortafirmas = this.envioDocumentoPtfrService
                        .buscarEnviosDocumentoPortafirmas(index, pageSize, filtrosEntrada, orden,
                                idConvocatoria);
                
//                for (EnvioDocumentoPTFR envioDocumentoPTFR : listadoPortafirmas) {
//                    if(envioDocumentoPTFR.getidExpedienteJustec() != null) {
//                        ExpedienteJusTecDTO expJustec = this.expedienteJusTecService.buscarPorIdExpedienteJustec(envioDocumentoPTFR.getidExpedienteJustec());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJustec.getNmPeriodo() );
//                    }
//                    
//                    //VMM 20/10/25 MD-1586: metemos la comprobacion del idExpJusEco
//                    if(envioDocumentoPTFR.getidExpedienteJusEco() != null) {
//                        ExpedienteJusEcoDTO expJusEco = this.expedienteJusEcoService.buscarPorIdExpedienteJuseco(envioDocumentoPTFR.getidExpedienteJusEco());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJusEco.getNmPeriodo() );
//                    }
//                    
//                    ExpedienteDTO expediente = this.expedienteService.buscarId(envioDocumentoPTFR.getidExpediente());
//                    if(expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null 
//                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE)) {
//                        
//                        InteresadoDTO interesado = this.interesadoService.buscarId(expediente.getRepresentanteLegal().getIdInteresado());
//                        String nombreInteresado = interesado.getDsNombre() + " " + interesado.getDsApellido1() + 
//                                (interesado.getDsApellido2() != null && !interesado.getDsApellido2().isEmpty() ? " " + interesado.getDsApellido2() : "");
//                        envioDocumentoPTFR.setDsDestinatario(nombreInteresado);
//                    }
//                    
//                    if(envioDocumentoPTFR.getCdEstadoPTFR() != null) {
//                        convertirEstadoPTFR(envioDocumentoPTFR);
//                    }
//                    
//                }
                

                this.lstEnviosPendientesNote = listadoPortafirmas;
                listado.setResultados(listadoPortafirmas);
                //listado.setTotal(listadoPortafirmas.size());
                listado.setTotal(this.envioDocumentoPtfrService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
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
    public AtlasListAndCount<EnvioDocumentoPTFR> obtenerListaEnviosPortaEnviados(final int index,
            final int pageSize, Object orderBean, Object filtros) {       
        AtlasListAndCount<EnvioDocumentoPTFR> listado = new AtlasListAndCount<>();
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                    filtrosEntrada = this.getFiltros();
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_PTFR, Operador.NOT_NULL));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_PTFR, Constantes.EstadoDocumentoPtfr.DS_PENDIENTE,
                        Operador.EQUALS));

                if (Objects.isNull(orderBean)) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));

                List<EnvioDocumentoPTFR> listadoPortafirmas = this.envioDocumentoPtfrService
                        .buscarEnviosDocumentoPortafirmas(index, pageSize, filtrosEntrada, orden,
                                idConvocatoria);
                
//                for (EnvioDocumentoPTFR envioDocumentoPTFR : listadoPortafirmas) {
//                    if(envioDocumentoPTFR.getidExpedienteJustec() != null) {
//                        ExpedienteJusTecDTO expJustec = this.expedienteJusTecService.buscarPorIdExpedienteJustec(envioDocumentoPTFR.getidExpedienteJustec());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJustec.getNmPeriodo() );
//                    }
//                    
//                    //VMM 20/10/25 MD-1586: metemos la comprobacion del idExpJusEco
//                    if(envioDocumentoPTFR.getidExpedienteJusEco() != null) {
//                        ExpedienteJusEcoDTO expJusEco = this.expedienteJusEcoService.buscarPorIdExpedienteJuseco(envioDocumentoPTFR.getidExpedienteJusEco());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJusEco.getNmPeriodo() );
//                    }
//                    
//                    ExpedienteDTO expediente = this.expedienteService.buscarId(envioDocumentoPTFR.getidExpediente());
//                    if(expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null 
//                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE)) {
//                        
//                        InteresadoDTO interesado = this.interesadoService.buscarId(expediente.getRepresentanteLegal().getIdInteresado());
//                        String nombreInteresado = interesado.getDsNombre() + " " + interesado.getDsApellido1() + 
//                                (interesado.getDsApellido2() != null && !interesado.getDsApellido2().isEmpty() ? " " + interesado.getDsApellido2() : "");
//                        envioDocumentoPTFR.setDsDestinatario(nombreInteresado);
//                    }
//                    
//                }
                
                this.setLstEnviosFirmados(listadoPortafirmas);
                listado.setResultados(listadoPortafirmas);
                listado.setTotal(this.envioDocumentoPtfrService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listado;
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
    public AtlasListAndCount<EnvioDocumentoPTFR> obtenerListaEnviosNoteEnviados(final int index,
            final int pageSize, Object orderBean, Object filtros) {       
        AtlasListAndCount<EnvioDocumentoPTFR> listado = new AtlasListAndCount<>();        
        try {
            if (this.idConvocatoria != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                if (this.getFiltros() != null && !this.getFiltros().isEmpty()) {
                    filtrosEntrada = this.getFiltros();
                }

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        this.idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_PTFR, Operador.NOT_NULL));
                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_FC_ENVIO_NOTE, Operador.NOT_NULL));

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_PTFR, Constantes.EstadoDocumentoPtfr.DS_FIRMADO,
                        Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_ESTADO_NOTE,
                        Operador.NOT_NULL));

                if (Objects.isNull(orderBean)) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> orden = (List<AtlasSort>) orderBean;
                orden.add(new AtlasSort(Constantes.FC_ENVIO_COLA, Boolean.FALSE));

                List<EnvioDocumentoPTFR> listadoPortafirmas = this.envioDocumentoPtfrService
                        .buscarEnviosDocumentoPortafirmas(index, pageSize, filtrosEntrada, orden,
                                idConvocatoria);
                
//                for (EnvioDocumentoPTFR envioDocumentoPTFR : listadoPortafirmas) {
//                    if(envioDocumentoPTFR.getidExpedienteJustec() != null) {
//                        ExpedienteJusTecDTO expJustec = this.expedienteJusTecService.buscarPorIdExpedienteJustec(envioDocumentoPTFR.getidExpedienteJustec());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJustec.getNmPeriodo() );
//                    }
//                    
//                    //VMM 20/10/25 MD-1586: metemos la comprobacion del idExpJusEco
//                    if(envioDocumentoPTFR.getidExpedienteJusEco() != null) {
//                        ExpedienteJusEcoDTO expJusEco = this.expedienteJusEcoService.buscarPorIdExpedienteJuseco(envioDocumentoPTFR.getidExpedienteJusEco());
//                        envioDocumentoPTFR.setdsTramite(envioDocumentoPTFR.getdsTramite() + " Periodo: " + expJusEco.getNmPeriodo() );
//                    }
//                    
//                    ExpedienteDTO expediente = this.expedienteService.buscarId(envioDocumentoPTFR.getidExpediente());
//                    if(expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null 
//                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE)) {
//                        
//                        InteresadoDTO interesado = this.interesadoService.buscarId(expediente.getRepresentanteLegal().getIdInteresado());
//                        String nombreInteresado = interesado.getDsNombre() + " " + interesado.getDsApellido1() + 
//                                (interesado.getDsApellido2() != null && !interesado.getDsApellido2().isEmpty() ? " " + interesado.getDsApellido2() : "");
//                        envioDocumentoPTFR.setDsDestinatario(nombreInteresado);
//                    }
//                    
//                    
//                    if(envioDocumentoPTFR.getCdEstadoNote() != null) {
//                        convertirEstado(envioDocumentoPTFR);
//                    }
//                    
//                    
//                }

                this.setLstEnviosFirmadosNote(listadoPortafirmas);
                listado.setResultados(listadoPortafirmas);
                listado.setTotal(this.envioDocumentoPtfrService.count(filtrosEntrada));
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
    public void inicializarEnvioDocumentoPtfrDTO() {
        this.envioDocumentoPtfrDTO = new EnvioDocumentoPtfrDTO();
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

        if (StringUtils.isNotBlank(this.getCdNumExpdientePen())) {
            this.getFiltros().add(new AtlasFilter(Constantes.Filtro.FILTRO_CD_EXPEDIENTE,
                    "%" + this.getCdNumExpdientePen() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (StringUtils.isNotBlank(this.getDsDestinatarioPen())) {
            this.getFiltros().add(new AtlasFilter("dsDestinatario",
                    "%" + this.getDsDestinatarioPen() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (StringUtils.isNotBlank(this.getDsFirmantePen())) {
            this.getFiltros().add(new AtlasFilter("dsFirmante", "%" + this.getDsFirmantePen() + "%",
                    Operador.LIKE_INSENSITIVE));
        }

        if (this.getDsTramitePen() != null) {
            this.getFiltros().add(new AtlasFilter(Constantes.Multipart.ID_TRAMITE,
                    this.getDsTramitePen(), Operador.EQUALS));
        }

        if (StringUtils.isNotBlank(this.getDsVerificadorPen())) {
            this.getFiltros().add(new AtlasFilter("dsVerificador",
                    "%" + this.getDsVerificadorPen() + "%", Operador.LIKE_INSENSITIVE));
        }

        if (this.getFcEnvioColaDesdePen() != null && this.getFcEnvioColaHastaPen() != null) {
            this.getFiltros()
                    .add(new AtlasFilter(Constantes.FC_ENVIO_COLA, this.getFcEnvioColaDesdePen(),
                            this.getFcEnvioColaHastaPen(), Operador.BETWEEN));
        }
        
        if (this.getFcEnvioColaDesdePen() != null && this.getFcEnvioColaHastaPen() == null) {
            this.getFiltros().add(new AtlasFilter(Constantes.FC_ENVIO_COLA,
                    this.getFcEnvioColaDesdePen(), Operador.GREATER_OR_EQUAL));
        }
        
        if (this.getFcEnvioColaDesdePen() == null && this.getFcEnvioColaHastaPen() != null) {
            this.getFiltros().add(new AtlasFilter(Constantes.FC_ENVIO_COLA,
                    this.getFcEnvioColaHastaPen(), Operador.LESS_OR_EQUAL));
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
        this.inicializarEnvioDocumentoPtfrDTO();
        this.filtrar();

    }

    /**
     * Metodo para rellenar el combo fondos Europeos con los fondos aun no
     * agregados a la convocatoria
     *
     * @return lstItem the List<SelectItem> lstItem
     */
    public List<SelectItem> getComboTramites() { 

        List<SelectItem> lstItem = new ArrayList<>();
        List<CatalogosValoresDTO> lstTramites = new ArrayList<>();

        try {
            //VMM 26/09/24 MD-593: ahora solo metemos los tramites del gestor (catalogo 38), revisar esto...
            lstTramites = this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR));

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


    public void enviarAPortafirmas() {

        try {
            if (CollectionUtils.isNotEmpty(lstSeleccionados)) {
                List<EnvioDocumentoPtfrDTO> lstSolicitudes = new ArrayList<>(
                        Lists.transform(lstSeleccionados, EnvioDocumentoPTFRToDTO.INSTANCE));
                ConvocatoriaDTO convDTO = null;
                for (EnvioDocumentoPtfrDTO envio : lstSolicitudes) {
                    if (convDTO == null || (convDTO != null && !Objects
                            .equals(convDTO.getIdConvocatoria(), envio.getIdConvocatoria()))) {
                        convDTO = this.convocatoriaService.buscarId(envio.getIdConvocatoria());
                    }
                    DocumentoPtfrDTO docPtfrDTO = this.documentoPtfrService.enviarPortaFirmas(
                            convDTO, envio.getIdTramite(), envio.getIdExpediente(), envio,
                            envio.getIdDocumentoPTFR());
                    if (docPtfrDTO != null) {
                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_PORTAFIRMAS_CORRECTO);
                        
                        if(docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION))
                                ) {
                            this.documentoBean.setExisteEnvioPtfrDesistidoEnProceso(Boolean.TRUE);
                        }else {
                            this.documentoBean.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.TRUE);
                        }
                        
                        
                    } else {
                        AtlasMessageUtils.addErrorMessageFromBundle(
                                ConstantesWeb.Err.ENVIO_PORTAFIRMAS_INCORRECTO);
                        break;
                    }
                }
            }
        } catch (ServiceException exc) {
            log.error(exc.getMessage());
            if (exc.getMessage().contains("firmantes")) {
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_PORTAFIRMAS_SIN_FIRMANTES);
            } else {
                AtlasMessageUtils
                .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_PORTAFIRMAS_INCORRECTO);
            }        }

    }

    public void enviarANote() {
        try {
            if (this.idConvocatoria != null && !this.lstSeleccionadosNote.isEmpty()) {

                for (EnvioDocumentoPTFR envio : lstSeleccionadosNote) {

                    // GIM 30/08/2024 NUEVO METODO PARA LLAMAR DIRCTEAMENTE A
                    // NOTE SIN PASAR PRO CONSULTAR EL ESTADO DE LA RESPUESTA DE
                    // PORTAFIRMAS,
                    // LA RESPUESTA DEL PORTAFIRMAS SE ESTA Haciendo ahora en un
                    // proceso del btach que ya existia antes, solo que la parte
                    // del procreso que llamaba a note
                    // una vez consultaba el estado del portafirmas lo hemos
                    // cortado para que sean dos metodos indepeiendientes
                    List<String> lstCdNote = this.documentoPtfrService.procesarEnvioANote(
                            envio.getidDocumentoPTFR(), envio.getidExpediente());

                    if (lstCdNote != null && !lstCdNote.isEmpty()) {
                        
                        // MD-2147 actualizamos el estado interno a Excluido provisional
                        DocumentoPtfrDTO docPtfrDTO = this.documentoPtfrService.buscarPorId(envio.getidDocumentoPTFR());
                        Integer idValorTramite = (docPtfrDTO.getTramiteDTO() != null) ? docPtfrDTO.getTramiteDTO().getIdValor() : null;
                        Map<Integer, Integer> catalogos = utilConstantes.getGlobalCatalogosValores();
                                              
                        if (docPtfrDTO.getTramiteDTO().getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))) {
                            this.expedienteService.actualizarEstadoProvisionalExpediente(
                                    docPtfrDTO.getIdExpediente(),
                                    Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
                                    null);
                        }
                        
                        if(docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO))
                                || docPtfrDTO.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION))
                                ) {
                            this.documentoBean.setExisteEnvioPtfrDesistidoEnProceso(Boolean.TRUE); 
                        }else {
                            this.documentoBean.setExisteEnvioPtfrSubsanacionEnProceso(Boolean.TRUE);
                        }
                        
                        
                        Integer constanteEstado = null;
                        if (catalogos.get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(idValorTramite)) {
                            constanteEstado = Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO;
                        } else if (catalogos.get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(idValorTramite)) {
                            constanteEstado = Constantes.EstadoExpediente.DENEGADO_DEFINITIVO;
                        } else if (catalogos.get(Constantes.Tramite.EXCLUSION).equals(idValorTramite)) {
                            constanteEstado = Constantes.EstadoExpediente.EXCLUIDO;
                        } else if (catalogos.get(Constantes.Tramite.DESISTIMIENTO).equals(idValorTramite)) {
                            constanteEstado = Constantes.EstadoExpediente.DESISTIDO;
                        }

                        Expediente expediente = this.expedienteService.find(docPtfrDTO.getIdExpediente());
                        
                        if (constanteEstado != null) {
                            
                            expediente.setEstadoBenef(
                                ExpedienteToEntity.INSTANCE.calcularEstadoBenef(catalogos.get(constanteEstado))
                            );

                            expediente = this.expedienteDAO.merge(expediente);
                            this.expedienteHistDAO.crearHistorico(expediente, null);
                        }
   
                        
                        // MD-2148 actualizamos el estado interno a desistido
                        // provisional
                        if (docPtfrDTO.getTramiteDTO().getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL))) {             
                            this.expedienteService.actualizarEstadoProvisionalExpediente(
                                    docPtfrDTO.getIdExpediente(),
                                    Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
                                    null);
                        }
                        
                        // MD-2482 [TRAM] Cambiar estado del beneficiario al enviar al note - Tramite subsanación administrativa
                        if (docPtfrDTO.getTramiteDTO().getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))) {
                            
                            this.expedienteService.actualizarEstadoBeneficiario(
                                    docPtfrDTO.getIdExpediente(),
                                    Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS);
                            
                            DocumentoPtfr documentoPtfr = DocumentoPtfrToEntity.INSTANCE.apply(docPtfrDTO);
                            
                            // Actualizamos los estados de los documentos
                            int existeDocSub = this.documentoPtfrService.actualizarEstadoDocumentoBatchPublic(
                                    documentoPtfr.getIdDocumentoPtfr(),
                                    utilConstantes.getGlobalCatalogosValores().get(
                                            Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

                            // Si se actualizaron registros de documentos
                            // actualizamos el
                            // estado interno
                            if (existeDocSub > 0) {
                                this.documentoPtfrService.actualizarEstadoInternoExpedienteBatchPublic(expediente,
                                        utilConstantes.getGlobalCatalogosValores().get(
                                                Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS),
                                        Boolean.TRUE);
                            }
                            
                        }
                        
                        // VMM 06/10/26 MD-3066: resolucion de modificacion presupuestaria enviada a
                        // Note -> la modificacion pasa de "Pendiente Envio Cola Notificaciones" a
                        // "Notificado". La pasa a Resuelta el batch de lectura de Note cuando el
                        // beneficiario la ha leido (aplicarResolucionModificacion).
                        if (docPtfrDTO.getIdPresupuestoModif() != null
                                && catalogos.get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                                        .equals(idValorTramite)) {
                            this.documentoPtfrService.cambiarEstadoModificacionPresPlazoOtraPublic(
                                    docPtfrDTO.getIdPresupuestoModif(),
                                    Constantes.EstadoExpedienteResolucion.NOTIFICADO);
                        }

                        AtlasMessageUtils.addInfoMessageFromBundle(
                                ConstantesWeb.Mensajes.ENVIO_NOTE_CORRECTO);
                    } else {
                        AtlasMessageUtils
                                .addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_NOTE_ERROR);
                        break;
                    }
                }
            }

        } catch (ServiceException exc) {
            log.error(exc.toString());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_NOTE_INCORRECTO);
        }

    }


    public Boolean comprobarListadoPorta() throws ServiceException {
        
        // MD-1510 JRFM - Si es auditor devolvemos directamente true para desactivar
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolAuditor()) 
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            return Boolean.TRUE;
        }
        else {
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            this.comprobarGestoresExcep(this.convocatoriaDTO);
            
            //VMM 11/11/24 MD-657: cambiamos condiciones para deshabilitar el boton de envio para gestores excepcionales
            return CollectionUtils.isEmpty(this.lstSeleccionados) 
                    || (!CollectionUtils.isEmpty(this.lstSeleccionados) 
                        && (this.convocatoriaDTO.isEsGestorConcesion() || this.convocatoriaDTO.isEsGestorJCT()));
        }

    }

    public Boolean comprobarListadoNote() throws ServiceException {
        
        // MD-1510 JRFM - Si es auditor devolvemos directamente true para desactivar
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolAuditor())
                || Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
            return Boolean.TRUE;
        }
        else {
            if (this.convocatoriaDTO == null) {
                this.convocatoriaDTO = this.convocatoriaService.buscarId(this.idConvocatoria);
            }
            this.comprobarGestoresExcep(this.convocatoriaDTO);
            
            //VMM 11/11/24 MD-657: cambiamos condiciones para deshabilitar el boton de envio para gestores excepcionales
            return CollectionUtils.isEmpty(this.lstSeleccionadosNote) 
                    || (!CollectionUtils.isEmpty(this.lstSeleccionadosNote) 
                        && (this.convocatoriaDTO.isEsGestorConcesion() || this.convocatoriaDTO.isEsGestorJCT()));
        }
        
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
     * Metodo para descargar documento de PTFR enviado
     *
     * @param idDocumentoPTFR
     */
    public void descargarDocumentoPtfrEnviado(Integer idDocumentoPTFR) {
        try {

            if (idDocumentoPTFR != null) {
                
              //buscamos el expedienteDoc asociado al documentoPtrf       
                DocumentoPtfrDTO ptfrDTO = this.documentoPtfrService
                        .buscarPorId(idDocumentoPTFR);
                
                
                if (ptfrDTO != null) {

                    List<AtlasFilter> filtrosEntrada = new ArrayList<>();

                    // Obtenemos datos de la convocatoria pla
                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                            Operador.EQUALS));
                    filtrosEntrada.add(
                            new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, ptfrDTO.getTramiteDTO().getIdValor(), Operador.EQUALS));

                    List<ConvocatoriaPlaDTO> lstItem = this.convocatoriaPlaService.buscar(0, -1, filtrosEntrada,
                            null);

                    if (lstItem.isEmpty()) {
                        throw new ServiceException("No hay documentos para el tramite en el Portafirmas.");
                    }

                    ConvocatoriaPlaDTO convocatoriaPla = lstItem.get(0);

                    // Obtenemos datos de la convocatoria
                    ConvocatoriaDTO convocatoria = this.convocatoriaService.buscarId(this.idConvocatoria);

                    // Obtenemos datos del expediente
                    ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(ptfrDTO.getIdExpediente());
                    
                    
                    String strConsejeria = this.unidadAdminService.obtenerConsejeria();
                    byte[] informeTramitePdf;
                    
                    List<Integer> tramitesNoFormPdf = new ArrayList<>();
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL));
                    tramitesNoFormPdf.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO));
                    // AÑADIR TRAMITE NUEVO

                    if (tramitesNoFormPdf.contains(ptfrDTO.getTramiteDTO().getIdValor()) && ptfrDTO != null
                            && ptfrDTO.getDocumentoAdjunto() != null) {
                        informeTramitePdf = ptfrDTO.getDocumentoAdjunto();
                    } else {
                        Tramite tramite = tramiteService.getTramite(ptfrDTO.getTramiteDTO().getIdValor());
                        tramite.setPdfService(this.pdfService);
                        tramite.setCatalogosValoresService(this.catalogosValoresService);
                        tramite.setExpedienteService(this.expedienteService);
                        informeTramitePdf = tramite.formularioPdf(convocatoria, convocatoriaPla, expedienteDTO,
                                strConsejeria);
                    }
                    
          

                    if (informeTramitePdf != null) {
                        UtilesWeb.openFile(informeTramitePdf, "pdf", convocatoriaPla.getTramiteDTO().getDsValor());
                    }

                }
                
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }
    

    /**
     * Metodo para descargar documento de PTFR de respuesta
     *
     * @param idDocumentoPTFR
     */
    public void descargarDocumentoPtfrRespuesta(Integer idDocumentoPTFR) {
        try {

            if (idDocumentoPTFR != null) {
                
              //buscamos el expedienteDoc asociado al documentoPtrf       
                DocumentoPtfrDTO ptfrDTO = this.documentoPtfrService
                        .buscarPorId(idDocumentoPTFR);
                
                if (ptfrDTO != null) {

                    PtfrRecuperaDocFirmadoDTO recuperado = this.ptfrAdmeService
                            .recuperarDocumentoFirmado(ptfrDTO.getDsReferenciaPtfr(),
                                    String.valueOf(ptfrDTO.getCdArchivoPtfr()));

                    if (recuperado != null) {
                        UtilesWeb.openFile(Base64.getDecoder().decode(recuperado.getPdfB64()),
                                Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_EXTENSION,
                                Constantes.TipoDocumentoPtfr.NOMBRE_ARCHIVO_INFORME);
                    }

                }
                
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }
    
    
    public Boolean habilitarDescargarDocumento() throws ServiceException {
        Boolean mostrar = Boolean.TRUE;
        
        //MD-2563 incluimos que no puedan acceder a estas acciones los gestores ext
        if (Boolean.TRUE.equals(this.infoUsuarioBean.getRolGestorExterno())) { 
            mostrar = Boolean.FALSE;
        }
        
       return mostrar;
    }
}
