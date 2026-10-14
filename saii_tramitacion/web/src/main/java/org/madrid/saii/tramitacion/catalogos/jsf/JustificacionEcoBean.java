package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauDTO;
import org.madrid.saii.lib.dto.ConvocatoriaCauEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpJusEcoDocConceptoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaCauEvalService;
import org.madrid.saii.lib.services.ConvocatoriaCauService;
import org.madrid.saii.lib.services.ConvocatoriaPlaService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DocumentoPtfrService;
import org.madrid.saii.lib.services.EnvioNotificacionesPortafirmasService;
import org.madrid.saii.lib.services.ExpedienteDocHistService;
import org.madrid.saii.lib.services.ExpedienteDocService;
import org.madrid.saii.lib.services.ExpedienteJusEcoDocService;
import org.madrid.saii.lib.services.ExpedienteJusEcoService;
import org.madrid.saii.lib.services.ExpedientePresupMontoService;
import org.madrid.saii.lib.services.ExpedientePresupService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.adme.EregAdmeService;
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
 * Esta clase representa el Backing Bean para las páginas de catálogo de la
 * entidad <code>{@link Solicitud}</code>
 *
 * @author quental
 *
 * @since 06-oct-2025 
 *
 * @see ExpedienteBean
 */
@Slf4j
@Getter
@Setter
@Component("justificacionEcoBean")
@Scope("request")
public class JustificacionEcoBean extends CatalogoBean<ExpedienteJusEco, Integer> {
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;

    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;

    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService expedienteJusEcoService;

    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio de convocatoriaCauService
     */
    @Autowired
    @Qualifier("convocatoriaCauService")
    private ConvocatoriaCauService convocatoriaCauService;
    
    
    /**
     * Servicio de convocatoriaCauEvalService
     */
    @Autowired
    @Qualifier("convocatoriaCauEvalService")
    private ConvocatoriaCauEvalService convocatoriaCauEvalService;
    
    /**
     * dto de formularioDocExternoDTO
     */
    private ExpedienteDocDTO formularioDocExternoDTO;

    

    /** Servicio Convocatoria */
    @Autowired
    @Qualifier("convocatoriaService")
    @Setter
    @Getter
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("EnvioNotificacionesPortafirmasService")
    private EnvioNotificacionesPortafirmasService envioNotificacionesPortafirmasService;

    /** Servicio Convocatoria Pla */
    @Autowired
    @Qualifier("convocatoriaPlaService")
    @Setter
    @Getter
    private ConvocatoriaPlaService convocatoriaPlaService;
    
    
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    

    /**
     * Servicio de documentoService
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;
    
    @Autowired
    @Qualifier("expedienteJusEcoDocService")
    private ExpedienteJusEcoDocService expedienteJusEcoDocService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;
   
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    
    private ExpedienteBean expedienteBean;
    
    @Autowired
    @Qualifier("infoUsuarioBean")
    private InfoUsuarioBean infoUsuarioBean;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    private List<ExpedienteJusEcoDTO> listaExpedientesJE;
    
    private List<ExpedienteJusEcoDocDTO> listaExpedientesJusEcoDoc;
    
    private List<ExpJusEcoDocConceptoDTO> listaExpedientesJusEcoDocDet;

    private ExpedienteJusEcoDTO expedienteJusEcoSeleccionado;
    
    private ExpedienteJusEcoDocDTO expedienteJusEcoDocSeleccionado;
    
    private ExpJusEcoDocConceptoDTO expedienteJusEcoDocDetSeleccionado;

    private ExpedienteDocDTO documentoSeleccionado;
    
    /**
     * Comentario del motivo de rechazo del documento seleccionado
     */
    private String motivoRechazoSeleccionado;
    
    /**
     * Id de Convocatoria
     */
    private Integer idConvocatoria;
    
    private ConvocatoriaDTO convocatoriaDTO;
    
    /**
     * dto de expediente doc
     */
    private ExpedienteDocDTO expedienteDocDTO;
    
    private CatalogosValoresDTO tipoDocumento;

    private DocumentoDTO documentoDTO;

    private Boolean modoLecturaSubsanacion = Boolean.FALSE;
    
    private Boolean habilitarBotonSubsanarJE = Boolean.TRUE;
    
    /**
     * Atributo para controlar si es obligatorio ingresar la descripción de
     * causa de denegación
     */
    private Boolean descripcionCausaObligatoria = Boolean.FALSE;

    private String descripcionCausaSubsanacion;
    
    private String tlObservaciones;
    
    private String  tlObservacionesParaelCiudadano;

    private ExpedienteDTO expedienteDTO;

    private Boolean existeEnvioPtfrSubsanacionJEEnProceso;
    
    private Boolean existeEnvioPtfrSubsanacionJEFirmado;
   
    private Boolean existeEnvioPtfrDesistimientoEnProceso;
        
    private String solapaJustificacion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:solapaJusEco";
    
    private String formularioJE = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:outpanelJustificacionEcoSolapa";
    
    /**
     * Atributo para almacenar la causa de denegación seleccionada
     */
    private Integer idCausaSubsanacion;
    
    
    private String textCausaSubsanacion;

    /**
     * Id de Expediente
     */
    private Integer idExpediente;

    /**
     * Atributos para el documento de envio de portafirmas
     */
    private DocumentoPtfrDTO documentoPtfrDTO;

    private List<ExpedienteDocDTO> listaDocumentosDTO;
    
    private List<ExpedienteJusEcoDocDTO> listaDocumentosJusEcoDTO;
    
    private UploadedFile archivoSubirDesistimiento;
    
    /* Estilo pestaña Jus Eco */
    //private Boolean bAvisoJusEco;
        

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    
    /**
     * Constructor por defecto.
     *
     * Se realizan las operaciones de inicialización del ManagedBean que no
     * están en el constructor padre
     */
    public JustificacionEcoBean() {
        super();
        //this.bAvisoJusEco = Boolean.FALSE;
    }

    public void cargarInformacion() throws ServiceException {
        //VMM 05/05/26 MD-2595: reseteamos los valores que habia del anterior expediente
        this.expedienteJusEcoSeleccionado = null;
        this.expedienteJusEcoDocSeleccionado = null;
        this.expedienteJusEcoDocDetSeleccionado = null;
        this.documentoSeleccionado = null;
        this.motivoRechazoSeleccionado = null;
        this.archivoSubirDesistimiento = null;
        this.documentoPtfrDTO = new DocumentoPtfrDTO();
        this.listaDocumentosDTO = new ArrayList<>();
        this.listaDocumentosJusEcoDTO = new ArrayList<>();
        this.listaExpedientesJE = new ArrayList<>();
        this.listaExpedientesJusEcoDoc = new ArrayList<>();
        this.listaExpedientesJusEcoDocDet = new ArrayList<>();
                
        if (Objects.isNull(this.idExpediente) && !Objects.isNull(this.expedienteBean)) {
            this.idExpediente = this.expedienteBean.getIdExpediente();
        }
        if (Objects.isNull(this.convocatoriaDTO) && this.idConvocatoria != null) {
            convocatoriaDTO = convocatoriaService.buscarId(this.idConvocatoria);
        }
        
        //validarEstilo();
    }
    
    
    // Valida el estilo de la pestaña de jus eco
   /* private void validarEstilo() throws ServiceException {

        this.bAvisoJusEco = Boolean.FALSE;
        
        //VMM 22/01/26 MD-2204: si el estado del exp jus eco es  pdte revision o pdte subsanacion, ponemos el aviso activo en rojo
        if (this.expedienteBean.getExpedienteDTO().getEstadoRevJusEcoDTO().getIdValor() != null && 
                (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevJusEcoDTO().getIdValor()) ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevJusEcoDTO().getIdValor()))) {
            this.bAvisoJusEco = Boolean.TRUE;
        }
    }*/

    /**
     * Crea una nueva instancia de la entidad ExpedienteJusEco
     *
     * @return entidad de tipo ExpedienteJusEco
     */
    @Override
    public ExpedienteJusEco crearEntidad() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteJusEcoDTO> obtenerListaPeriodoExp(final int index,
            final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpedienteJusEcoDTO> listaExp = new AtlasListAndCount<>();

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

                this.listaExpedientesJE = expedienteJusEcoService.buscar(index, pageSize,
                        filtrosEntrada, ordenamiento);
                
                listaExp.setResultados(this.listaExpedientesJE);
                listaExp.setTotal(this.listaExpedientesJE.size());
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

        return listaExp;
    }

    
    public void seleccionarPeriodo(Object object) {
        try {
            this.expedienteJusEcoSeleccionado = (ExpedienteJusEcoDTO) object;
            this.idExpediente = expedienteJusEcoSeleccionado.getIdExpediente();
            
            //al seleccionar un nuevo periodo reseteamos todas las listas y elementos seleccionados que teniamos
            this.listaDocumentosDTO = new ArrayList<>();
            this.listaExpedientesJusEcoDoc = new ArrayList<>();
            this.listaExpedientesJusEcoDocDet = new ArrayList<>();
            this.listaDocumentosJusEcoDTO = new ArrayList<>();
            this.expedienteJusEcoDocDetSeleccionado = null; //new ExpJusEcoDocConceptoDTO();
            this.expedienteJusEcoDocSeleccionado = null; //new ExpedienteJusEcoDocDTO();
            this.documentoSeleccionado = null; //new ExpedienteDocDTO();
            
            obtenerListaDocumentosExpedienteEco(0, -1, null, null);
            
            this.actualizarValoresFormulario();

        } catch (Exception exc) {
            log.error("Error al obtener los documentos por periodo seleccionado.", exc);
            AtlasMessageUtils
                    .addErrorMessage("Error al obtener los documentos por periodo seleccionado");
        }
    }
    
    public void seleccionarPartida(Object object) {
        try {
            this.expedienteJusEcoDocSeleccionado = (ExpedienteJusEcoDocDTO) object;
            this.idExpediente = expedienteJusEcoSeleccionado.getIdExpediente();
            
            //al seleccionar un nuevo periodo reseteamos todas las listas y elementos seleccionados que teniamos
            this.listaDocumentosDTO = new ArrayList<>();
            this.listaExpedientesJusEcoDocDet = new ArrayList<>();
            this.expedienteJusEcoDocDetSeleccionado = null; //new ExpJusEcoDocConceptoDTO();
            this.documentoSeleccionado = null; //new ExpedienteDocDTO();
            
            if (convocatoriaDTO == null) {
                convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
            }
            boolean esConvoConConceptos = convocatoriaDTO.getItConceptoPresup() != null && 
                    convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S);
            
            //si es una convocatoria con conceptos, cargamos desde cada partida su tabla de detalle de conceptos
            if (esConvoConConceptos) {
                obtenerListaDocumentosExpEcoDetalle(0, -1, null, null);
            }else {
                //si es una convoc sin conceptos, cargamos desde cada partida su documentacion
                obtenerListaDocumentacionJEPartida(0, -1, null, null);
            }
            
            this.actualizarValoresFormulario();

        } catch (Exception exc) {
            log.error("Error al obtener los documentos por partida seleccionada.", exc);
            AtlasMessageUtils
                    .addErrorMessage("Error al obtener los documentos por partida seleccionada");
        }
    }
    
    public void seleccionarConcepto(Object object) {
        try {
            this.expedienteJusEcoDocDetSeleccionado = (ExpJusEcoDocConceptoDTO) object;
            this.idExpediente = expedienteJusEcoSeleccionado.getIdExpediente();
            
            //al seleccionar un nuevo periodo reseteamos todas las listas y elementos seleccionados que teniamos
            this.listaDocumentosDTO = new ArrayList<>();
            this.documentoSeleccionado = null; //new ExpedienteDocDTO();
            
            obtenerListaDocumentacionJEPartida(0, -1, null, null);
            
            this.actualizarValoresFormulario();

        } catch (Exception exc) {
            log.error("Error al obtener los documentos por partida-concepto seleccionado.", exc);
            AtlasMessageUtils
                    .addErrorMessage("Error al obtener los documentos por partida-concepto seleccionado");
        }
    }

    public void actualizarListaDocumentosExpedienteEco(ExpedienteJusEcoDTO exp) {
        if (!Objects.isNull(exp)) {
            this.expedienteJusEcoSeleccionado = exp;
            PrimeFaces.current().ajax()
                    .update(ConstantesWeb.Referencia.EXPEDIENTE_JUSTEC_ID_CONTENEDOR_PERIODOS);
        }
        if (!Objects.isNull(this.expedienteJusEcoSeleccionado)) {
            obtenerListaDocumentosExpedienteEco(0, -1, null, null);
            PrimeFaces.current().ajax()
                    .update(ConstantesWeb.Referencia.EXPEDIENTE_JUSTEC_ID_CONTENEDOR_DOCUMENTOS);
        }
    }

    //VMM 14/10/25 MD-1586: lo comento porque parece que no se invoca desde ningun sitio el dialogo pero si esta funcion de carga cada vez que entramos y penaliza tiempo
//    @SuppressWarnings("unchecked")
//    public AtlasListAndCount<ExpedienteDocDTO> obtenerHistoricoDocEco(int index, int pageSize,
//            Object ordenacion, Object filter) {
//
//        AtlasListAndCount<ExpedienteDocDTO> lista = new AtlasListAndCount<>();
//
//        try {
//
//            List<AtlasFilter> filtros = (List<AtlasFilter>) filter;
//            List<AtlasSort> orden = (List<AtlasSort>) ordenacion;
//
//            if (orden == null) {
//                orden = new ArrayList<>();
//            }
//            orden.add(new AtlasSort("fcAltaFila", false));
//
//            // obtener los registros
//            lista.setResultados(this.expedienteDocService.buscar(index, pageSize, filtros, orden));
//
//            // obtener el total de registros (numero)
//            lista.setTotal(this.expedienteDocService.count(filtros));
//
//        } catch (ServiceException se) {
//            log.error(se.getMessage());
//        }
//
//        return lista;
//    }

    public AtlasListAndCount<ExpedienteJusEcoDocDTO> obtenerListaDocumentosExpedienteEco(
            final int index, final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpedienteJusEcoDocDTO> lista = new AtlasListAndCount<>();
        try {

            if (this.idExpediente != null && !Objects.isNull(this.expedienteJusEcoSeleccionado)) {

                //cargamos los registros de expJusEcoDoc que mostrarán las partidas (por idExpJusEco)
                List<ExpedienteJusEcoDocDTO> expJustEcoDocs = cargarExpedientesJusEcoDocSeleccionado();
                     
                //distinguir por partida (quitar repes)             
                //Preparamos un mapa de idExpedientePresup → tipoFinanciacion
                Map<Integer, Integer> mapaTipoFinanciacion = expedientePresupService.getDao().obtenerTiposFinanciacionPorIds(
                        expJustEcoDocs.stream()
                             .map(jedoc -> jedoc.getExpedientePresupMontoDTO().getIdExpedientePresup())
                             .filter(Objects::nonNull)
                             .collect(Collectors.toSet())
                );
                
                //Filtramos duplicados usando ese mapa
                List<ExpedienteJusEcoDocDTO> listaSinDuplicados = expJustEcoDocs.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
                            jedoc -> mapaTipoFinanciacion.get(jedoc.getExpedientePresupMontoDTO().getIdExpedientePresup())
                        ))),
                        ArrayList::new
                    ));
                
                this.listaDocumentosJusEcoDTO = listaSinDuplicados;
                    
                // obtener los registros
                lista.setResultados(listaSinDuplicados);

                // obtener el total de registros (numero)
                lista.setTotal(Objects.isNull(listaSinDuplicados) ? 0 : listaSinDuplicados.size());
            }

        } catch (ServiceException se) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

        this.listaDocumentosJusEcoDTO = lista.getResultados();
        
        return lista;
    }  
    
    
    public AtlasListAndCount<ExpJusEcoDocConceptoDTO> obtenerListaDocumentosExpEcoDetalle(
            final int index, final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpJusEcoDocConceptoDTO> lista = new AtlasListAndCount<>();
        try {

            if (this.idExpediente != null && !Objects.isNull(this.expedienteJusEcoDocSeleccionado)
                    && !Objects.isNull(this.expedienteJusEcoDocSeleccionado.getExpedientePresupMontoDTO())
                    && listaDocumentosJusEcoDTO != null && !listaDocumentosJusEcoDTO.isEmpty()) {
                
                Integer partidaSelecc = this.expedientePresupService.buscarId(this.expedienteJusEcoDocSeleccionado.getExpedientePresupMontoDTO().getIdExpedientePresup())
                        .getTipoFinanciacionDTO().getIdValor();
                
                //cargamos los registros de expJusEcoDoc que mostrarán los conceptos (por idExpJusEcoDoc y partida)
                //hacer una sql query en el dao
                List<ExpJusEcoDocConceptoDTO> expJustEcoDocs = expedienteJusEcoDocService.getDao().
                        cargarExpJusEcoDocPartidaSeleccionada(this.expedienteJusEcoDocSeleccionado, partidaSelecc);
                
                this.listaExpedientesJusEcoDocDet = expJustEcoDocs;
                    
                // obtener los registros
                lista.setResultados(expJustEcoDocs);

                // obtener el total de registros (numero)
                lista.setTotal(Objects.isNull(expJustEcoDocs) ? 0 : expJustEcoDocs.size());
            }

        } catch (ServiceException se) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

        this.listaExpedientesJusEcoDocDet = lista.getResultados();
        
        return lista;
    }  
    
    
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocDTO> obtenerListaDocumentacionJEPartida(
            final int index, final int pageSize, Object orderBean, Object filtros) {

        AtlasListAndCount<ExpedienteDocDTO> lista = new AtlasListAndCount<>();

        try {

            if (convocatoriaDTO == null) {
                convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
            }
            
            boolean esConvoConConceptos = convocatoriaDTO.getItConceptoPresup() != null && 
                    convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S);
            
            if (idExpediente != null && (
                    (esConvoConConceptos && expedienteJusEcoDocDetSeleccionado != null && expedienteJusEcoDocDetSeleccionado.getIdExpedienteDoc() != null) ||
                    (!esConvoConConceptos && expedienteJusEcoDocSeleccionado != null &&
                     expedienteJusEcoDocSeleccionado.getExpedienteDocDTO() != null &&
                     expedienteJusEcoDocSeleccionado.getExpedienteDocDTO().getIdExpedienteDoc() != null)
            )) {
                    List<AtlasSort> ordenamiento = orderBean != null ? (List<AtlasSort>) orderBean
                            : new ArrayList<>();
                    ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, true));

                    List<AtlasFilter> filtrosEntrada = filtros != null ? (List<AtlasFilter>) filtros
                            : new ArrayList<>();

                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                            this.idExpediente, Operador.EQUALS));

                    if (esConvoConConceptos) {
                        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_DOC,
                                this.expedienteJusEcoDocDetSeleccionado.getIdExpedienteDoc(), Operador.EQUALS));
                    }else {
                        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_DOC,
                                this.expedienteJusEcoDocSeleccionado.getExpedienteDocDTO().getIdExpedienteDoc(), Operador.EQUALS));
                    }

                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    List<ExpedienteDocDTO> documentos = this.expedienteDocService.buscar(index,
                            pageSize, filtrosEntrada, ordenamiento);

                    this.listaDocumentosDTO = documentos;
                        
                    // obtener los registros
                    lista.setResultados(documentos);

                    // obtener el total de registros (numero)
                    lista.setTotal(Objects.isNull(documentos) ? 0 : documentos.size());
            }

        } catch (ServiceException se) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }

        this.listaDocumentosDTO = lista.getResultados();
        
        return lista;
    }  
    

    private List<ExpedienteJusEcoDocDTO> cargarExpedientesJusEcoDocSeleccionado()
            throws ServiceException {
        List<AtlasFilter> filtro = new ArrayList<>();

        filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSECO_IDEXPEDIENTE_JUSECO,
                this.expedienteJusEcoSeleccionado.getIdExpedienteJusEco(),
                Operador.EQUALS));

        List<ExpedienteJusEcoDocDTO> expJustDocs = this.expedienteJusEcoService
                .buscarDocs(0, -1, filtro, null);
        
        this.listaExpedientesJusEcoDoc=expJustDocs;
        
        return expJustDocs;
    }
    

    public void inicializarFormularioSubsanacion() {
       
        this.idCausaSubsanacion = null;
        this.descripcionCausaSubsanacion = null;
        this.documentoSeleccionado = null;
        this.modoLecturaSubsanacion = Boolean.FALSE;
        this.descripcionCausaObligatoria = Boolean.FALSE;
        
    }

    /**
     * Método para cancelar y cerrar el formulario de causa de subsanación
     */
    public void cancelarCausaSubsanarDocumento() {
        this.inicializarFormularioSubsanacion();
    }
    
    
    /**
     * Método para grabar la asignación de causa de subsanación al documento
     */
    public void grabarCausaSubsanarDocumento() {

        try {

            if (this.documentoSeleccionado != null) {

                if (this.validarFormularioSubsanacion().equals(Boolean.FALSE)) {
                    ConvocatoriaCauDTO convocatoriaCauDTO = this.convocatoriaCauService.buscarId(this.idCausaSubsanacion);
                    this.documentoSeleccionado.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)));
                    
                    this.documentoSeleccionado.setIdCausa(convocatoriaCauDTO.getCausaDTO().getIdValor());
                    
                    if (convocatoriaCauDTO.getCausaDTO() != null
                            && convocatoriaCauDTO.getCausaDTO().getNmConstante() != null
                            && convocatoriaCauDTO.getCausaDTO().getNmConstante()
                                    .equals(Constantes.CausaDenegacion.CODIGO_CAUSA_ADMIN_OTROS)) {
                        this.documentoSeleccionado
                                .setTlCausaDescripcion(this.descripcionCausaSubsanacion);
                    } else {
                        this.documentoSeleccionado.setTlCausaDescripcion(
                                convocatoriaCauDTO.getCausaDTO().getDsValor());
                    }

                    
                    this.expedienteDocService.guardarConHistorico(this.documentoSeleccionado);
                    this.actualizarValoresFormulario();
                    this.inicializarFormularioSubsanacion();
                    AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
                    PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoJE').hide();");
                    
                }

            } else {
                AtlasMessageUtils.addWarnMessageFromBundle(
                        ConstantesWeb.Mensajes.DOCUMENTOS_NO_SELECCION_DOCUMENTO);
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }

    
    /**
     * Método para validar los datos del formulario de subsanación.
     *
     * @return Boolean Indicador true o false
     */
    private Boolean validarFormularioSubsanacion() {
        Boolean existeError = Boolean.FALSE;
        if (this.idCausaSubsanacion == null) {
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_CAUSA_DENEGACION_NO_SELECCIONADO);
            existeError = Boolean.TRUE;
        } else if (this.descripcionCausaObligatoria.equals(Boolean.TRUE)
                && StringUtils.isBlank(this.descripcionCausaSubsanacion)) {
            AtlasMessageUtils.addWarnMessageFromBundle(
                    ConstantesWeb.Err.ERROR_VALIDACION_DESC_CAUSA_DENEGACION_NO_INGRESADO);
            existeError = Boolean.TRUE;
        }
        return existeError;
    }
    
    /**
     * Método que se ejecuta al seleccionar una opcion en el comboBox y
     * modificamos el tipoDocumento del expedienteDoc. Creamos tambien el
     * historico del cambio
     *
     * @param item Integer con el idExpedienteDoc
     */
    public void cambiarTipoDocumento(Integer item) throws ServiceException {
        if (!Boolean.TRUE.equals(null == item)) {
            try {
                this.expedienteDocDTO = this.expedienteDocService.buscarId(item);
                this.expedienteDocDTO.setTipoDocumentoDTO(this.tipoDocumento);

                if (this.validarSeleccionDoc(this.expedienteDocDTO).equals(Boolean.FALSE)) {
                    this.expedienteDocDTO = this.expedienteDocService
                            .guardar(this.expedienteDocDTO);
                    this.expedienteDocHistService.crearHistorico(this.expedienteDocDTO,
                            UserUtils.getUserName().toUpperCase());
                } else {
                    AtlasMessageUtils
                            .addWarnMessageFromBundle(ConstantesWeb.Mensajes.TIPO_DOC_DUPLICADO);
                }
            } catch (ServiceException e) {
                log.error(e.getMessage());
                throw e;
            }
        }
    }
    
    /**
    *
    * @param doc
    * @return
    */
   public Boolean validarSeleccionDoc(ExpedienteDocDTO doc) {
       Boolean existeDoc = Boolean.FALSE;

       // obtenemos la lista de documentos registrados en el expediente
       try {
           ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(doc.getExpedienteDTO().getIdExpediente());

           if (Objects.nonNull(doc.getTipoDocumentoDTO().getIdValor())) {
               for (ExpedienteDocDTO item : expedienteDTO.getLstExpedienteDocDTO()) {
                   if (doc.getTipoDocumentoDTO().getIdValor()
                           .equals(item.getTipoDocumentoDTO().getIdValor())
                           && doc.getTipoDocumentoDTO().getIdValor()
                                   .equals(item.getTramiteDTO().getIdValor())) {
                       existeDoc = Boolean.TRUE;
                       break;
                   }
               }
           }

       } catch (ServiceException e) {
           log.error(e.getMessage());
       }

       return existeDoc;
   }
    
   /**
   *
   * @param expedienteDocDTO
   */
  public void mostrarMotivo(ExpedienteDocDTO expedienteDocDTO) {
      DocumentoPtfrDTO docPtfrDTO;
      try {

          if (expedienteDocDTO.getIdDocumentoPtfr() != null) {
              docPtfrDTO = this.documentoPtfrService
                      .buscarPorId(expedienteDocDTO.getIdDocumentoPtfr());

              if (docPtfrDTO.getMotivoRechazo() != null
                      && !StringUtils.isEmpty(docPtfrDTO.getMotivoRechazo())) {
                  this.motivoRechazoSeleccionado = docPtfrDTO.getMotivoRechazo();
              } else {
                  this.motivoRechazoSeleccionado = AtlasMessageUtils
                          .getStringFromBundle(ConstantesWeb.Mensajes.MOTIVO_RECHAZO_VACIO);
              }
          } else {
              this.motivoRechazoSeleccionado = AtlasMessageUtils
                      .getStringFromBundle(ConstantesWeb.Mensajes.MOTIVO_RECHAZO_VACIO);
          }

          PrimeFaces.current().executeScript("PF('dialogMotivoJE').show();");

      } catch (ServiceException e) {
          log.error(e.getMessage());
          AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR);
      }
  }
   
  
  /**
  *
  */
 public void cerrarMotivo() {
     this.motivoRechazoSeleccionado = null;
     PrimeFaces.current().executeScript("PF('dialogMotivoJE').close();");
 }
   
    
//    public void descargarDocumentoEcoFica(Integer codigoFica) {
//        try {
//
//            //Limpiar mensajes anteriores antes de hacer nada
//            
//            if (codigoFica != null) {
//
//                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);
//
//                if (ficaFichero != null) {
//
//                    log.info("Documento " + ficaFichero.getNombreFichero() + " recuperado");
//
//                    // Se prepara el fichero.
//                    UtilesWeb.openFile(ficaFichero.getDatosFichero(), ficaFichero.getExtension(),
//                            FilenameUtils.removeExtension(ficaFichero.getNombreFichero()));
//
//                } else {
//                    log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
//                    AtlasMessageUtils.addErrorMessageFromBundle(
//                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
//                }
//
//            } else {
//                log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
//                AtlasMessageUtils.addErrorMessageFromBundle(
//                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_FALTANTE);
//            }
//        } catch (ServiceException ex) {
//            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
//            AtlasMessageUtils
//                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
//        }
//    }

//    public void descargarDocumentoEcoEreg(ExpedienteDocDTO expedienteDocDTO) {
//        try {
//            
//            //Limpiar mensajes anteriores antes de hacer nada
//            
//            ExpedienteDTO expDTO = this.expedienteService.buscarIdMinimo(this.idExpediente);
//
//            byte[] documento = this.eregAdmeService.descargarAnexoPorCodigo(expDTO.getCdEreg(),
//                    expedienteDocDTO.getCdAnexoEreg());
//
//            DateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA_DESCARGA);
//
//            // Se prepara el fichero.
//            UtilesWeb.openFile(documento, Constantes.Extensiones.PDF,
//                    expedienteDocDTO.getCdAnexoEreg() + "_"
//                            + sdf.format(expedienteDocDTO.getFcAltaFila()));
//
////            if (!Objects.isNull(expedienteDocDTO.getCumple()) && expedienteDocDTO.getCumple()) {
////                expedienteDocDTO.setEstadoDTO(
////                        new CatalogosValoresDTO(Constantes.EstadoDocumento.REVISION_OK));
////
////            } else {
////                expedienteDTO.setEstadoBeneficiarioDTO(new CatalogosValoresDTO(
////                        Constantes.EstadoExpedienteBeneficiario.RESOLUCION_DESESTIMATORIA));
////                expedienteDocDTO.setEstadoDTO(
////                        new CatalogosValoresDTO(Constantes.EstadoDocumento.REVISION_KO));
////            }
//
//        } catch (ServiceException ex) {
//            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO, ex.getMessage());
//            AtlasMessageUtils.addErrorMessageFromBundle(
//                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
//        }
//    }

    /**
     * Método para aprobar un documento
     *
     * @param documento Datos del documento
     */
//    public void aprobarDocumentoEco(ExpedienteDocDTO documento) {
//        try {
//            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
//            this.expedienteDocService.guardarConHistorico(documento);
//            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
//      
//            this.actualizarValoresFormulario();
//
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
//        }
//    }

    /**
     * Método para desaprobar un documento
     *
     * @param documento Datos del documento
     */
//    public void desaprobarDocumentoEco(ExpedienteDocDTO documento) {
//        try {
//            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
//            this.expedienteDocService.guardarConHistorico(documento);
//            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
//                       
//            this.actualizarValoresFormulario();
//           
//        } catch (ServiceException e) {
//            log.error(e.getMessage());
//            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
//        }
//    }

    /**
     * Método para mostrar la ventana de asignación de causa de denegación al
     * tipo de documento
     *
     * @param documento Datos del documento
     */
    public void mostrarSubsanarDocumentoEco(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        this.documentoSeleccionado = documento;
        if (this.documentoSeleccionado != null) {
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoJE').show();");
        }
    }
    
    
    /**
     * Metodo para identificar si se habilita la descarga de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonDescargaDocumento(ExpedienteDocDTO expDocDTO) {
        Boolean habilitar = Boolean.TRUE;
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR).equals(expDocDTO.getEstadoDTO().getIdValor())) 
                || (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION).equals(expDocDTO.getEstadoDTO().getIdValor()) && expDocDTO.getCdFica() == null ) ) {
            habilitar = Boolean.FALSE;
        }
        return habilitar;
    }
    
    /**
     * Método que controla la descargar de fichero tipo 1
     *
     * @param imprDTO Datos de impreso
     * @param doc Datos de impreso
     */
    public void descargarDocumentoFica(Integer codigoFica) {

        try {
            
            //Limpiar mensajes anteriores antes de hacer nada

            if (codigoFica != null) {

                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);

                if (ficaFichero != null) {
                    this.documentoDTO = new DocumentoDTO();
                    this.documentoDTO.setFicaFichero(ficaFichero);
                    this.documentoDTO.setContenidoFichero(ficaFichero.getDatosFichero());
                    this.documentoDTO.setCdFica(codigoFica);

                    log.info("Documento " + this.documentoDTO.getFicaFichero().getNombreFichero()
                            + " recuperado");

                    // Se prepara el fichero.
                    UtilesWeb.openFile(this.documentoDTO.getContenidoFichero(),
                            this.documentoDTO.getFicaFichero().getExtension(),
                            FilenameUtils.removeExtension(
                                    this.documentoDTO.getFicaFichero().getNombreFichero()));

                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
                }

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO_NO_EXISTE);
            }
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO, ex.getMessage());
            AtlasMessageUtils
                    .addErrorMessageFromBundle(ConstantesWeb.Mensajes.ERROR_CODIGO_FICA_DOCUMENTO);
        }
    }
    
    
    public void revertirEvaluacionDocumentoEco(ExpedienteDocDTO documento) {

        try {

            this.documentoSeleccionado = null;

            // Obtenemos el ultimo registro historico
            List<AtlasFilter> filtroHist = new ArrayList<>();
            filtroHist
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                            documento.getIdExpedienteDoc(), Operador.EQUALS));

            filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    documento.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));

            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

            List<ExpedienteDocHistDTO> lstHistorico = this.expedienteDocHistService.buscar(0, -1,
                    filtroHist, ordenamientoHist);

            if (lstHistorico != null && !lstHistorico.isEmpty()) {
                // Recuperamos el valor historico anterior
                ExpedienteDocHistDTO registroHistoricoEstadoAnterior = lstHistorico.get(0);
                // Asignamos estado anterior
                documento.setEstadoDTO(registroHistoricoEstadoAnterior.getEstadoDTO());
                // Limpiamos valores de subsanación
                documento.setConvocatoriaCauDTO(null);
                documento.setIdCausa(null);
                documento.setTlCausaDescripcion(null);

                if (registroHistoricoEstadoAnterior.getCdFica() != null
                        && !registroHistoricoEstadoAnterior.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR))) {
                    documento.setCdFica(registroHistoricoEstadoAnterior.getCdFica());
                } else {
                    documento.setCdFica(null);
                }

                this.expedienteDocService.guardarConHistorico(documento);

              //VMM parece que esto sobra porque ya lo hace al final o si solo hacerlo cuando no hay error
//                this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
        this.actualizarValoresFormulario();
    }
    
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     * @throws ServiceException 
     */
    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
     * (aprobar/desaprobar/revertir/subsanar documentos, finalizar revisión, enviar a
     * portafirmas...), dejando habilitadas las de solo lectura (descargar, ver).
     *
     * @return true si el expediente está Rechazado Definitivo
     */
    public boolean isExpedienteRechazadoDefinitivo() {
        return this.expedienteBean != null && this.expedienteBean.getExpedienteDTO() != null
                && this.expedienteBean.getExpedienteDTO().getEstadoDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor());
    }

    public boolean habilitarBotonRevertirCalificacionDocumento(Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) throws ServiceException {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite revertir calificación
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;
        Boolean estadoPeriodo = Boolean.FALSE;
        Boolean estadoDocumentosubsanado = Boolean.FALSE;
        List<ExpedienteDocHistDTO> registrosAnteriores = null;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));

        List<AtlasFilter> filtroHist = new ArrayList<>();
        filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                expDocDto.getIdExpedienteDoc(), Operador.EQUALS));

        int totalHistorico = 0;
        try {
            totalHistorico = this.expedienteDocHistService.buscar(0, -1, filtroHist, null).size();
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        List<AtlasFilter> filtroHistUltimo = new ArrayList<>();
        filtroHistUltimo
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                        expDocDto.getIdExpedienteDoc(), Operador.EQUALS));
        filtroHistUltimo.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                expDocDto.getEstadoDTO().getIdValor(), Operador.NOT_EQUALS));

        List<AtlasSort> ordenamientoHist = new ArrayList<>();
        ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

        try {
            registrosAnteriores = this.expedienteDocHistService.buscar(0, -1, filtroHistUltimo,
                    ordenamientoHist);
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        List<Integer> lstEstadosPeriodos = new ArrayList<>();
        lstEstadosPeriodos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.REVISION_OK));
        lstEstadosPeriodos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION));

        if(this.expedienteJusEcoSeleccionado != null && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO() != null && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor() != null ) {
            /// GIM 15/11/24 agregamos la comprobacion que de si esta el periodo pediendod e subsanacion o revision ok no se activan los botones de revertir
            estadoPeriodo = lstEstadosPeriodos.contains(this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor());
            
            /// GIM 15/11/24 agregamos la comprobacion que de si esta el periodo pediente de reviosion subsanacion y el esdtado del documento revisdion ok no se activa el boton de revertir 
            if(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION).equals(this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor()) 
                    && expDocDto.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO))) {
                estadoDocumentosubsanado = Boolean.TRUE;
            }
        }

        /// GIM 01/08/24 quitamos el dieconvocatoria de la justifacion
        if (Boolean.FALSE.equals(this.habilitarCalificacionJE())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                    /// GIM 15/11/24 agregamos la comprobacion que de si esta el periodo pediendod e subsanacion o revision ok no se activan los botones de revertir
                || Boolean.TRUE.equals(estadoPeriodo)
                /// GIM 15/11/24 agregamos la comprobacion que de si esta el periodo pediente de reviosion subsanacion y el esdtado del documento revisdion ok no se activa el boton de revertir 
                || Boolean.TRUE.equals(estadoDocumentosubsanado) 
                || Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionJEEnProceso)
                || Boolean.TRUE.equals(this.existeEnvioPtfrSubsanacionJEFirmado) 
                || totalHistorico <= 1
                || registrosAnteriores == null || registrosAnteriores.isEmpty()) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    
    /**
     * Metodo para actualizar los valores del formulario
     */
    private void actualizarValoresFormulario() {

            PrimeFaces.current().ajax().update(this.formularioJE);
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECODOC_ID_CONTENEDOR_DOCUMENTOS); 
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_ID_CONTENEDOR_DOCUMENTOS);  
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_ID_CONTENEDOR_PERIODOS);
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_BOTON_SUBSANACION); 
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_BOTON_DESISTIMIENTO);    
            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_BOTON_REVISAR);
            
    }

    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion de justificación economica
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     */
    public boolean habilitarBotonEnviarSubanasarPortafirmas(Integer idExpediente,
            Integer idEstado) {
        return this.validarEnvioCorrecto(idExpediente, idEstado);
    }

    /**
     * Método para validar si el envío es posible
     * 
     * @return true si se puede mandar
     */
    private boolean validarEnvioCorrecto(Integer idExpediente, Integer idEstado) {
        Boolean valido = Boolean.FALSE;

        // Validamos si hay documento de subsanación previo y obtenemos
        // documentos
        try {
           // List<ExpedienteDocDTO> lstDoc = this.expedienteDocService.buscarPorExpediente(idExpediente);

            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA)));
            int countDocPtfr = this.documentoPtfrService.count(filtros);
            if (countDocPtfr > 0)
                return Boolean.FALSE;

            // Validamos si hay documentos calificados como "NO" cumple

//            Boolean existeNoCumple = false;
//            if (!Objects.isNull(lstDoc))
//                existeNoCumple = lstDoc.stream().anyMatch(doc -> Boolean.TRUE.equals(!doc.getCumple()));

            boolean estadosFinales = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.ACEPTADA).equals(idEstado)
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO).equals(idEstado)
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO).equals(idEstado)
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(idEstado);

            if (!isModoConsulta() && !estadosFinales ) {
                valido = Boolean.TRUE;
            }
            return valido;

        } catch (ServiceException e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

   
    
   
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonDesaprobarDocumento(Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite calificar
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados de documentos donde se permite calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

                /// GIM 01/08/24 cambiamos el tramite de presentacion a justificacion
                if (!lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                                .equals(expDocDto.getTramiteDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados de documentos donde se permite calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

              /// GIM 01/08/24 cambiamos el metodo calificacion para quitarle la convocatoria
                if (Boolean.FALSE
                        .equals(this.habilitarCalificacionJE())
                        || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        } else {

            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

            // principal
            Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

            // adicional
            /// GIM 01/08/24 cambiamos el tramite de presentacion a justificacion
            Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                    .equals(expDocDto.getTramiteDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                        || Boolean.FALSE.equals(valAdicional)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }

        return habilitar;
    }
    
    
    /**
     * Método para desaprobar un documento
     *
     * @param documento Datos del documento
     */
    public void desaprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO)));
            this.expedienteDocService.guardarConHistorico(documento);
            this.actualizarValoresFormulario();
           

            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    /**
     * Método para mostrar la ventana popup con el histórico de estados de un
     * documento
     *
     * @param documento Datos del documento
     */
    public void mostrarHistoricoDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());
            if (this.documentoSeleccionado != null) {
                PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosJE').show();");
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

    }
   
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonSubsanarDocumento(Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite subsanar
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        /// GIM 01/08/24 cambiamos el tramite de presentacion a justificacion
        if (Boolean.FALSE.equals(this.habilitarCalificacionJE())
                || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                || Boolean.FALSE.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                        .equals(expDocDto.getTramiteDTO().getIdValor()))
                || Objects.isNull(expDocDto.getCdFica())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    /**
     * Método para mostrar la ventana de asignación de causa de denegación al
     * tipo de documento
     *
     * @param documento Datos del documento
     */
    public void mostrarSubsanarDocumento(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        this.documentoSeleccionado = documento;
        if (this.documentoSeleccionado != null) {
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoJE').show();");
        }
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonAprobarDocumento(Integer idEstadoConvocatoria, ExpedienteDocDTO expDocDto) {

        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite calificar
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        if (Boolean.FALSE.equals(expDocDto.getEsDocumentoRequisito())) {
            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

                if (!lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }

            } else {

                // Lista de estados de documentos donde se permite calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

                if (Boolean.FALSE
                        .equals(this.habilitarCalificacionJE())
                        || !lstEstados.contains(expDocDto.getEstadoDTO().getIdValor())
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }
        } else {

            // Lista de estados de documentos donde se permite calificar
            List<Integer> lstEstadosDoc = new ArrayList<>();
            lstEstadosDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

            // principal
            Boolean valPrincipal = lstEstadosDoc.contains(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.N.equals(expDocDto.getItAdicional());

            // adicional
            /// GIM 01/08/24 cambiamos el tramite de presentacion a justificacion
            Boolean valAdicional = !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                    .equals(expDocDto.getTramiteDTO().getIdValor())
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)
                            .equals(expDocDto.getEstadoDTO().getIdValor())
                    && Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional());

            if (Constantes.AfirmacionNegacion.S.equals(expDocDto.getItAdicional())) {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                        || Boolean.FALSE.equals(valAdicional)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            } else {

                // Lista de estados internos requisito donde no se permite
                // calificar
                List<Integer> lstEstados = new ArrayList<>();
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

                if (lstEstados.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                        || Boolean.FALSE.equals(valPrincipal)
                        || Objects.isNull(expDocDto.getCdFica())) {
                    habilitar = Boolean.FALSE;
                }
            }

        }

        return habilitar;
    }

    /**
     * Método para aprobar un documento
     *
     * @param documento Datos del documento
     */
    public void aprobarDocumento(ExpedienteDocDTO documento) {
        try {
            this.documentoSeleccionado = null;
            documento.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            documento.setConvocatoriaCauDTO(null);
            documento.setTlCausaDescripcion(null);

            ExpedienteDocDTO expeDTO = this.expedienteDocService
                    .buscarId(documento.getIdExpedienteDoc());

            if (documento.getTipoDocumentoDTO().getIdValor() == null) {
                documento.setTipoDocumentoDTO(
                        new CatalogosValoresDTO(expeDTO.getTipoDocumentoDTO().getIdValor()));
            }
            this.expedienteDocService.guardarConHistorico(documento);
            this.actualizarValoresFormulario();
                       

            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_OK);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.GUARDAR_KO);
        }
    }
    
    
    
    /**
     * Método para obtener la lista de históricos de estados de un documento
     *
     * @param index Indice
     * @param pageSize Cantidad páginas
     * @param orderBean Ordenamiento
     * @param filtros Filtros
     * @return Lista de histórico de estados de documento
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedienteDocHistDTO> obtenerListaHistoricoDocumento(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteDocHistDTO> listadoDatos = new AtlasListAndCount<>();
        try {
            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = (List<AtlasFilter>) filtros;

                filtrosEntrada.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                this.documentoSeleccionado.getIdExpedienteDoc(), Operador.EQUALS));

                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        this.documentoSeleccionado.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));

                if (null == orderBean) {
                    orderBean = new ArrayList<AtlasSort>();
                }
                List<AtlasSort> ordenamientoHist = (List<AtlasSort>) orderBean;
                ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));

                List<ExpedienteDocHistDTO> listado = this.expedienteDocHistService.buscar(index,
                        pageSize, filtrosEntrada, ordenamientoHist);
                
                //service
                
                for (ExpedienteDocHistDTO expedienteDocHistDTO : listado) { 
                    if(expedienteDocHistDTO.getIdCausa() != null) {
                        
                       CatalogosValores ccata = catalogosValoresService.find(expedienteDocHistDTO.getIdCausa());
                       if(ccata != null) {
                           expedienteDocHistDTO.setCausaCatalogoTexto(ccata.getDsValor());
                       }
                       
                    }
                }
                
                listadoDatos.setResultados(listado);
                listadoDatos.setTotal(this.expedienteDocHistService.count(filtrosEntrada));
            }
        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listadoDatos;
    }
    
    
    public void cerrarHistoricoDocumento() {
        this.documentoSeleccionado = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosJE').hide();");
    }
    
    /**
     * Método para cerrar la ventana popup de observaciones
     */
    public void cerrarObservaciones() {
        this.tlObservaciones = null;
        PrimeFaces.current().executeScript("PF('dialogHistoricoDocumentosObservacionesJE').hide();");

        // Si el objeto documentoSeleccionado es nulo, es que el popup de
        // observaciones no se ha llamado desde el popup de historico.
        // y no entra en el flujo del if. Caso contrario, el popup observaciones
        // se ha llamado desde el popup historico, y se volverá a abrir el popup
        // historico, despues de cerrar el de observaciones.
        if (null != this.documentoSeleccionado) {
            this.mostrarHistoricoDocumento(this.documentoSeleccionado);
        }
    }
    
    
    public void mostrarHistoricoDocumentoObservaciones(String observaciones) {
        this.tlObservaciones = observaciones;
        if (this.tlObservaciones != null) {
            PrimeFaces.current()
                    .executeScript("PF('dialogHistoricoDocumentosObservacionesJE').show();");
        }
    }
    
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarObservacionJE(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        if (StringUtils.isBlank(expDocDto.getTlObservaciones())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    
    /**
     * Método para mostrar la ventana popup con la causa de denegación asociada
     * al documento
     *
     * @param documento Datos de documento
     */
    public void mostrarDenegacionDocumento(ExpedienteDocDTO documento) {
        this.inicializarFormularioSubsanacion();
        try {
            this.documentoSeleccionado = documento;
            if (Boolean.FALSE.equals(documento.getEsDocumentoRequisito())) {
                this.documentoSeleccionado = this.expedienteDocService
                        .buscarId(documento.getIdExpedienteDoc());
                if(this.documentoSeleccionado.getIdCausa() != null) {
                    this.idCausaSubsanacion = this.documentoSeleccionado.getIdCausa();
                }else if (this.documentoSeleccionado.getConvocatoriaCauDTO() != null) {
                    this.idCausaSubsanacion = this.documentoSeleccionado.getConvocatoriaCauDTO().getIdConvocatoriaCau();
                }
                
//                ConvocatoriaCauEvalDTO convocatoriaCauDTO = this.convocatoriaCauEvalService.buscarId(this.idCausaSubsanacion);
//                
//                this.textCausaSubsanacion = convocatoriaCauDTO.getCausaEvalDTO().getDsValor();
                if(Objects.equals(this.documentoSeleccionado.getIdCausa(), utilConstantes.getGlobalCatalogosValores().get(Constantes.CausaDenegacion.CODIGO_CAUSA_JUSTEC_OTROS))){
                    this.descripcionCausaSubsanacion = this.documentoSeleccionado
                            .getTlCausaDescripcion();
                }else {
                    this.descripcionCausaSubsanacion = null;
                }
                
            }
            
            this.modoLecturaSubsanacion = Boolean.TRUE;  
            PrimeFaces.current().executeScript("PF('dialogSubsanaDocumentoJE').show();");
   
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * Método para controlar el cambio de causa de denegación seleccionada
     *
     * @param event Evento del combo
     */
    public void cambioCausaSelectItem(ValueChangeEvent event) {
        try {
            this.descripcionCausaObligatoria = Boolean.FALSE;
            this.idCausaSubsanacion = null;
            if (event.getNewValue() != null) {
                this.idCausaSubsanacion = (Integer) event.getNewValue();

                ConvocatoriaCauDTO convCau = convocatoriaCauService.buscarId(this.idCausaSubsanacion);

                if (convCau != null && convCau.getCausaDTO() != null
                        && convCau.getCausaDTO().getNmConstante() != null
                        && convCau.getCausaDTO().getNmConstante()
                                .equals(Constantes.CausaDenegacion.CODIGO_CAUSA_ECO_OTROS)) {
                    this.descripcionCausaObligatoria = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessage(ConstantesWeb.Mensajes.ERROR);
        }
    }
    
    
    /**
     * Método para obtener la lista de causas de denegación asignadas a la
     * convocatoria
     *
     * @return List<SelectItem> Lista de causas de denegación
     */
    public List<SelectItem> listaCausasSubsanacion(Integer idConvocatoria) {
        List<SelectItem> listaCausas = new ArrayList<>();
        try {

            if (this.documentoSeleccionado != null) {
                List<AtlasFilter> filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        idConvocatoria, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosEntrada
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_CAUSA_DENEGACION_IDVALOR,
                                utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.TipoCausaDenegacion.CAUSA_DENEG_CONTRATOS),
                                Operador.EQUALS));

                List<AtlasSort> orden = new ArrayList<>();
                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CAUSADTO_DSVALOR, true));

                List<ConvocatoriaCauDTO> listaConvCau = this.convocatoriaCauService.buscar(0, -1,
                        filtrosEntrada, orden);
                
                if (listaConvCau != null && !listaConvCau.isEmpty()) {
                    listaConvCau.stream()
                            .forEach(convcau -> listaCausas
                                    .add(new SelectItem(convcau.getIdConvocatoriaCau(),
                                            convcau.getCausaDTO().getDsValor())));
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        return listaCausas;
    }
    
    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param dto
     */
    public boolean habilitarBotonMostrarDenegacion(ExpedienteDocDTO expDocDto) {

        Boolean habilitar = Boolean.TRUE;

        if (Boolean.TRUE.equals(expDocDto.getEsDocumentoRequisito())) {
            if (StringUtils.isBlank(expDocDto.getDsObservacionRequisito())) {
                habilitar = Boolean.FALSE;
            }
        } else {
            if (Objects.isNull(expDocDto.getIdCausa())) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
   

    /**
     * Metodo para identificar si se habilita la calificación de documentos
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     */
    private boolean habilitarCalificacionJE() {

        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();

        // Lista de estados internos donde no se permite calificar
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));

        if (lstEstadoExpInter.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                || lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())) {
            habilitar = Boolean.FALSE;
        }

        return habilitar;
    }
    
    
    /**
     * Método que descarga el documento de EREG
     *
     * @param expedienteDocDTO documento del expediente
     */
    public void descargarDocumentoEreg(ExpedienteDocDTO expedienteDocDTO) {
        try {
            
            //Limpiar mensajes anteriores antes de hacer nada
            
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdMinimo(this.idExpediente);

            byte[] documento = this.eregAdmeService.descargarAnexoPorCodigo(
                    expedienteDTO.getCdEreg(), expedienteDocDTO.getCdAnexoEreg());

            DateFormat sdf = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA_DESCARGA);

            // Se prepara el fichero.
            UtilesWeb.openFile(documento, Constantes.Extensiones.PDF,
                    expedienteDocDTO.getCdAnexoEreg() + "_"
                            + sdf.format(expedienteDocDTO.getFcAltaFila()));
        } catch (ServiceException ex) {
            log.error(ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO, ex.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.DOCUMENTO_MENSAJE_DESCARGAR_KO);
        }
    }
    
    
    /**
     * Metodo para calcular si existe un envío de subsanación a portafirmas
     *
     * @param dto
     */
    private void calcularExisteEnvioPortafirmas(ExpedienteDTO dto) {
        this.setExisteEnvioPtfrSubsanacionJEEnProceso(Boolean.FALSE);
        this.setExisteEnvioPtfrSubsanacionJEFirmado(Boolean.FALSE);
        this.setExisteEnvioPtfrDesistimientoEnProceso(Boolean.FALSE);
        

        // Quitamos los registros de baja
        List<ExpedienteDocDTO> lstExpDoc = dto.getLstExpedienteDocDTO().stream()
                .filter(x -> Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());

        // Obtenemos la lista de documentos asociados a los requerimientos
        List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
                .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Quitamos los registros documentos asociado a los requerimientos
        lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                .collect(Collectors.toList());

        // Quitamos los registros que no esten asociados a un envío a
        // portafirmas
        lstExpDoc = lstExpDoc.stream().filter(x -> Objects.nonNull(x.getIdDocumentoPtfr()))
                .collect(Collectors.toList());

        try {
            
            List<DocumentoPtfrDTO> listaEnvios = this.documentoPtfrService.buscarPorExpediente(dto.getIdExpediente());
            
            
            for (DocumentoPtfrDTO doc : listaEnvios) {
                if(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(doc.getTramiteDTO().getIdValor())) {
                    this.setExisteEnvioPtfrDesistimientoEnProceso(Boolean.TRUE);
                }
            }
            

            if (!lstExpDoc.isEmpty()) {

                // Es el mismo registro de envío para todos los documentos
                // observados o desistidos
                this.documentoPtfrDTO = this.documentoPtfrService
                        .buscarPorId(lstExpDoc.get(0).getIdDocumentoPtfr());

                if (Objects.nonNull(this.documentoPtfrDTO.getTramiteDTO())) {
                    
                    if (Constantes.EstadoDocumentoPtfr.CODIGO_PENDIENTE
                            .equals(this.documentoPtfrDTO.getCdEstadoPtfr())
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA)
                                    .equals(this.documentoPtfrDTO.getTramiteDTO().getIdValor())) {
                        this.setExisteEnvioPtfrSubsanacionJEEnProceso(Boolean.TRUE);
                    }else if(Constantes.EstadoDocumentoPtfr.CODIGO_FIRMADO.equals(this.documentoPtfrDTO.getCdEstadoPtfr()) 
                            &&  utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA).equals(
                            this.documentoPtfrDTO.getTramiteDTO().getIdValor())){
                        this.setExisteEnvioPtfrSubsanacionJEFirmado(Boolean.TRUE); 
                    }

                }
            }

        } catch (ServiceException se) {
            log.error(se.getMessage());
        }
    }

    
    
    
    
    /**
     * Método para enviar a PTFR informe subsanación de revisión de la
     * justificación economica
     *
     * @param idConvocatoria Identificador de convocatoria
     */
    public void enviarSubsanarPortafirmas(Integer idConvocatoria) {
        try {

            // Recuperamos los datos del expediente

            if (Objects.isNull(convocatoriaDTO)) {
                convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
            }
            
            cargarInformacion();

            ExpedienteDTO expediente = this.expedienteService.buscarIdSimple(this.idExpediente,
                    Boolean.FALSE);
            
            Expediente exp = this.expedienteService.getDao().find(idExpediente);

            Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convocatoriaDTO,
                    expediente, idExpediente, 
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA),
                    null, null, null, expedienteJusEcoSeleccionado.getIdExpedienteJusEco());
            
            if (insertado.equals(Boolean.TRUE)) {
                
                expedienteJusEcoSeleccionado.setTlObservacionParaCiudadano(this.tlObservacionesParaelCiudadano); 
                
                this.expedienteService.actualizarEstadoInternoJE(exp, expedienteJusEcoSeleccionado, 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM), 
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_SUBSANACION));

                List<ExpedienteDoc> listaExpedienteDoc = documentoPtfrService.obtenerDocumentosAReportar(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA),
                        exp.getIdExpediente(), null, expedienteJusEcoSeleccionado.getIdExpedienteJusEco());
                for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                    expedienteDoc.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES)));
                    expedienteDocService.update(expedienteDoc);
                    expedienteDocHistService.crearHistoricoExpedienteDoc(expedienteDoc, null);
                    for (ExpedienteDocDTO expedienteDocuDTO : this.expedienteBean.getExpedienteDTO().getLstExpedienteDocDTO()) {
                        if (expedienteDoc.getIdExpedienteDoc().equals(expedienteDocuDTO.getIdExpedienteDoc())) {
                            expedienteDocuDTO.getEstadoDTO().setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES));
                            expedienteDocuDTO.getEstadoDTO().setNmConstante(Constantes.EstadoDocumento.ENVIO_COLA_NOTIFICACIONES);
                        }
                    }
                }

                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
            }
            
            // Refrescamos los controles de formulario
            this.actualizarValoresFormulario();
            
            //validarEstilo();
           

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

//    public void actualizarEstadoCumple(ExpedienteDocDTO exp) {
//        if(!Objects.isNull(exp)) {
//            Optional<ExpedienteJusEcoDocDTO> opt = this.listaExpedientesJusEcoDoc.stream()
//            .filter(x->Objects.isNull(exp.getIdExpedienteDoc())?null:exp.getIdExpedienteDoc().equals(x.getExpedienteDocDTO().getIdExpedienteDoc())).findFirst();
//            if(opt.isPresent()) {
//                ExpedienteJusEcoDocDTO expDoc = opt.get();
//                expDoc.setCumple(exp.getCumple());
//                this.expedienteJusEcoService.guardarDocumentoJusEco(expDoc);
//            }
//            PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSTEC_BOTON_SUBSANACION);
//            PrimeFaces.current().ajax()
//                    .update(ConstantesWeb.Referencia.EXPEDIENTE_JUSTEC_BOTON_GUARDAR);
//        }
//    }
    
    /**
     * Método para actualizar la calificación de un requisito
     *
     * @param indice Indice de campo a actualizar
     * @param dto Datos de requisito
     */
    public void actualizarObservacionParaCiudadano(ExpedienteJusEcoDTO dto) {
        try {

            Boolean continuar = Boolean.FALSE;

            if (!dto.getTlObservacionParaCiudadanoMod().equals(dto.getTlObservacionParaCiudadano())) {
                continuar = Boolean.TRUE;
            }

            if (Boolean.TRUE.equals(continuar)) {
                dto.setTlObservacionParaCiudadano(dto.getTlObservacionParaCiudadanoMod());

                ExpedienteJusEcoDTO reqDTO = this.expedienteJusEcoService.guardar(dto);

                this.listaExpedientesJE.stream().forEach(e -> {
                    if (e.getIdExpedienteJusEco().equals(reqDTO.getIdExpedienteJusEco())) {
                        e = reqDTO;
                    }
                });

                // Refrescamos los controles de formulario
                this.actualizarValoresFormulario();

            }

        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_REQUISITO_KO);
        }
    }
    
    
    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion administrativa
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonEnviarSubanasarPortafirmasJE(Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
        
        if (Objects.isNull(convocatoriaDTO)) {
            convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
        }
        this.comprobarGestoresExcep(convocatoriaDTO);

        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        List<ExpedienteDocDTO> lstExpDocJE = this.getListaDocumentosDTO();
        
        if(lstExpDocJE != null && !lstExpDocJE.isEmpty()) {

            // SUBSANACION
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Boolean existeRequiereSubsanacion = lstExpDocJE.stream()
                    .anyMatch(f -> idReqSub.equals(f.getEstadoDTO().getIdValor()));
            
            // Validamos si hay documentos pendientes de revision
            Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            Boolean existenPendientesRevision = lstExpDocJE.stream()
                    .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));

            // Lista de estados finales donde no se permite enviar a portafirmas
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));

            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            

            if ( !lstEstadoExpInter.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                    && Boolean.TRUE.equals(existeRequiereSubsanacion)
                    && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                    && Boolean.FALSE.equals(existenPendientesRevision)
                    && !convocatoriaDTO.isEsGestorJCT()
                    && !convocatoriaDTO.isEsGestorConcesion()
                    && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO() != null
                    && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor() != null
                    && !this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION))
                    && !this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_SUBSANACION))
                    ) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
    
    /**
     * Metodo para validar si se habilita el boton de enviar a portafirmas
     * subsanacion administrativa
     *
     * @param esModoConsulta
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonEnviarDesistirPortafirmasJE(Integer idEstadoConvocatoria) throws ServiceException {
        Boolean habilitar = Boolean.TRUE;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
        
        if (Objects.isNull(convocatoriaDTO)) {
            convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
        }
        this.comprobarGestoresExcep(convocatoriaDTO);

        // actualizamos la validacion
        this.calcularExisteEnvioPortafirmas(dto);

        List<ExpedienteDocDTO> lstExpDocJE = this.getListaDocumentosDTO();
        
        if(lstExpDocJE != null && !lstExpDocJE.isEmpty()) {

             //SUBSANACION
            Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
            Boolean existeRequiereSubsanacion = lstExpDocJE.stream()
                    .anyMatch(f -> idReqSub.equals(f.getEstadoDTO().getIdValor()));
            
            // Validamos si hay documentos pendientes de revision
            Integer idPenRev = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            Boolean existenPendientesRevision = lstExpDocJE.stream()
                    .anyMatch(f -> idPenRev.equals(f.getEstadoDTO().getIdValor()));
            
            
            Integer idRevKo = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO);
            Boolean existenKOs = lstExpDocJE.stream()
                    .anyMatch(f -> idRevKo.equals(f.getEstadoDTO().getIdValor()));

            // Lista de estados finales donde no se permite enviar a portafirmas
            List<Integer> lstEstadoExpInter = new ArrayList<>();
            lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));

            List<Integer> lstEstadoExp = new ArrayList<>();
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            
            //VMM calculamos los estados por periodo de la justec, se comenta a falta de probar si nos vale con el estadoJusEco del expediente
           /* List<ExpedienteJusEcoDocDTO> lstExpJusEcoDocDTO = null;
            lstExpJusEcoDocDTO = expedienteJusEcoDocService.buscarPorExpedienteDoc(lstExpDocJE.get(0).getExpedienteDocDTO().getIdExpedienteDoc());
            
            ExpedienteJusEcoDTO ExpedienteJusEcoDTO = null;
            if (lstExpJusEcoDocDTO != null && !lstExpJusEcoDocDTO.isEmpty()) {
                ExpedienteJusEcoDTO = expedienteJusEcoService.buscarPorIdExpedienteJusEco(lstExpJusEcoDocDTO.get(0).getExpedienteJusEcoDTO().getIdExpedienteJusEco());
            }*/
         
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria) 
                    && !convocatoriaDTO.isEsGestorJCT()
                    && !convocatoriaDTO.isEsGestorConcesion()
                    &&
                    ( ( Boolean.FALSE.equals(this.existeEnvioPtfrDesistimientoEnProceso)
                       && Boolean.TRUE.equals(existenKOs)
                       && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())) 
                    || (!lstEstadoExpInter.contains(dto.getEstadoRevJusEcoDTO().getIdValor())
                        && Boolean.TRUE.equals(existeRequiereSubsanacion)
                        && !lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                        && Boolean.FALSE.equals(existenPendientesRevision)
                        && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO() != null
                        && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor() != null
                        && !this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION))
                        && !this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_SUBSANACION)))
                   )
                ) {
                habilitar = Boolean.FALSE;
            }
        }

        return habilitar;
    }
    
    
    
    /**
     * Metodo para validar si se habilita el boton de finalizar revisión
     * administrativa
     *
     * @param esModoConsulta
     * @param idEstadoConvocatoria
     * @return
     * @throws ServiceException 
     */
    public boolean habilitarBotonFinalizarRevisionJE(Boolean esModoConsulta,
            Integer idEstadoConvocatoria) throws ServiceException {
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite finalizar revisión
        if (this.isExpedienteRechazadoDefinitivo()) {
            return Boolean.FALSE;
        }

        Boolean habilitar = Boolean.FALSE;
        Boolean pendienteRevisadoOk = Boolean.FALSE ;

        ExpedienteDTO dto = this.expedienteBean.getExpedienteDTO();
        //VMM comento los cambios para buscar el estado por cada periodo de la justec a falta de probar si es correcto hacerlo a nivel expdte
       // List<ExpedienteJusEcoDocDTO> lstExpJusEcoDocDTO = null;
        
        if (Objects.isNull(convocatoriaDTO)) {
            convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
        }
        this.comprobarGestoresExcep(convocatoriaDTO);

        List<ExpedienteDocDTO> listaDocs = this.listaDocumentosDTO;
        
        if(listaDocs == null || listaDocs.isEmpty()) {
            // Quitamos los registros de baja
            List<ExpedienteDocDTO> lstExpDoc = dto.getLstExpedienteDocDTO().stream()
                    .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

            // Obtenemos la lista de documentos asociados a los requerimientos
            List<Integer> lstDocReq = dto.getLstExpedienteReqDTO().stream()
                    .map(ExpedienteReqDTO::getIdExpedienteDoc).filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Quitamos los registros documentos asociado a los requerimientos
            lstExpDoc = lstExpDoc.stream().filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                    .collect(Collectors.toList());

            // Quitamos los  documentos que no sean de tipo tramite presentacion de solicitud 400
            Integer idPreJusEco = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA);
            lstExpDoc = dto.getLstExpedienteDocDTO().stream().filter(x -> x.getTramiteDTO()
                    .getIdValor() != null
                    && x.getTramiteDTO().getIdValor().equals(idPreJusEco))
                    .collect(Collectors.toList());
            
            // Validamos si todos los documentos administrativos fueron calificados
            // como Revisión OK o sin subir no obligatorio.
            Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
            Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
            pendienteRevisadoOk = lstExpDoc.stream().allMatch(
                    f -> (idRevOk.equals(f.getEstadoDTO().getIdValor()) ||
                    (idSinSub.equals(f.getEstadoDTO().getIdValor()))
                    && ((f.getBloqueComponenteDTO() != null && !f.getBloqueComponenteDTO().getItObligatorio()) 
                            || f.getBloqueComponenteDTO() == null)));
             
             //lstExpJusEcoDocDTO = expedienteJusEcoDocService.buscarPorExpedienteDoc(lstExpDoc.get(0).getExpedienteDocDTO().getIdExpedienteDoc());
             
        }else {
            
            Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
            Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
            pendienteRevisadoOk = listaDocs.stream().allMatch(
                f -> (idRevOk.equals(f.getEstadoDTO().getIdValor()) ||
                (idSinSub.equals(f.getEstadoDTO().getIdValor()))
                && ((f.getBloqueComponenteDTO() != null && !f.getBloqueComponenteDTO().getItObligatorio()) 
                        || f.getBloqueComponenteDTO() == null)));
            
            //lstExpJusEcoDocDTO = expedienteJusEcoDocService.buscarPorExpedienteDoc(listaDocs.get(0).getExpedienteDocDTO().getIdExpedienteDoc());
        }


        // Lista de estados finales donde no se permite finalizar revisión
        List<Integer> lstEstadoExpInter = new ArrayList<>();
        lstEstadoExpInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK));

        List<Integer> lstEstadoExp = new ArrayList<>();
        lstEstadoExp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
        
        /*ExpedienteJusEcoDTO expedienteJusEcoDTO = null;
        if (lstExpJusEcoDocDTO != null && !lstExpJusEcoDocDTO.isEmpty()) {
            ExpedienteJusEcoDTO = expedienteJusEcoService.buscarPorIdExpedienteJusEco(lstExpJusEcoDocDTO.get(0).getExpedienteJusEcoDTO().getIdExpedienteJusEco());
        }*/
        
        if ((utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA).equals(idEstadoConvocatoria)
                || Constantes.Convocatoria.RESOL_INDIV
                        .equals(convocatoriaDTO.getTipoResolModalidadDTO().getCdTipoResolucion()))
               /* && expedienteJusEcoDTO != null
                && !lstEstadoExpInter.contains(expedienteJusEcoDTO.getIdEstadoDTO().getIdValor())*/
                && !lstEstadoExpInter.contains(dto.getEstadoRevJusEcoDTO().getIdValor())  
                && Boolean.TRUE.equals(pendienteRevisadoOk)
                && lstEstadoExp.contains(dto.getEstadoDTO().getIdValor())
                && !convocatoriaDTO.isEsGestorConcesion()
                && this.expedienteJusEcoSeleccionado != null 
                && this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO() != null 
                && !this.expedienteJusEcoSeleccionado.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.REVISION_OK))) {
            habilitar = Boolean.TRUE;
        }

        return habilitar;
    }
    
    /**
     * Método que finaliza la revisión del expediente cuando todos los
     * documentos administrativos estan con estado REVISION OK
     */
    public void finalizarRevisionOk() {

        try {
            
            cargarInformacion();
            
            List<ExpedienteDocDTO> listaDocs = this.listaDocumentosDTO;

            ExpedienteDTO dto = this.expedienteService
                    .actualizarEstadoInternoRevisionOkDocumentoJE(this.idExpediente, listaDocs , this.expedienteJusEcoSeleccionado);
         
            if (dto != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)
                    .equals(dto.getEstadoDTO().getIdValor())) {
               // this.actualizarValoresFormulario();
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_OK);

            } else {
                AtlasMessageUtils.addErrorMessageFromBundle(
                        ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
            }
            
            this.actualizarValoresFormulario();
            
        } catch (ServiceException e) {
            log.error(AtlasMessageUtils.getStringFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO), e);
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_DOCUMENTO_FINALIZAR_KO);
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
    *
    * @param doc
    * @return
    */
   public Boolean puedeComentarObservaciones(Integer estadoConvocatoria, ExpedienteJusEcoDTO expJusEco ) {
       Boolean valor = Boolean.FALSE;

           if(estadoConvocatoria.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA)) && expJusEco.getIdEstadoRevAdminDTO() != null && expJusEco.getIdEstadoRevAdminDTO().getDsValor() != null
                   &&  (expJusEco.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.REVISION_OK))  ||
                       expJusEco.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION))  ||
                       expJusEco.getIdEstadoRevAdminDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_SUBSANACION)))) {
                   valor = Boolean.TRUE;
               
           }

       return valor;
   }
   
   /**
    * Método descargar un PDF con el formulario de subsanacion a enviar a
    * portafirmas
    *
    * @param idConvocatoria Identificador de convocatoria
    */
   public void descargarBorradorSubsanarPortafirmas(ConvocatoriaDTO convDTO) {

       try {
           
           //Limpiar mensajes anteriores antes de hacer nada
           
           if(this.idExpediente == null) {
               cargarInformacion();
           }

           byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                   convDTO.getIdConvocatoria(), 
                   utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA),
                   this.idExpediente,expedienteJusEcoSeleccionado.getNmPeriodo());

           StringBuilder nombreArchivoPdf = new StringBuilder();
           nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
           nombreArchivoPdf.append("_");
           nombreArchivoPdf
                   .append("Requerimiento de Subsanación - Solicitud - Revisión Justificicación económica");
           nombreArchivoPdf.append("_");
           nombreArchivoPdf.append("BORRADOR");

           UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

       } catch (ServiceException e) {
           log.error(e.getMessage());
           AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
       }
   }
    
   /**
    * Método descargar un PDF con el formulario de desistimiento a enviar a
    * portafirmas
    *
    * @param idConvocatoria Identificador de convocatoria
    */
   public void descargarBorradorDesistirPortafirmas(ConvocatoriaDTO convDTO) {
       try {

           //Limpiar mensajes anteriores antes de hacer nada
           
           if(this.idExpediente == null) {
               cargarInformacion();
           }
           
           byte[] archivoPdf = this.documentoPtfrService.enviarPortaFirmasBorrador(
                   convDTO.getIdConvocatoria(), 
                   utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO),
                   this.idExpediente,null);

           StringBuilder nombreArchivoPdf = new StringBuilder();
           nombreArchivoPdf.append(this.expedienteBean.getExpedienteDTO().getCdExpediente());
           nombreArchivoPdf.append("_");
           nombreArchivoPdf.append("Desistimiento de un expediente");
           nombreArchivoPdf.append("_");
           nombreArchivoPdf.append("BORRADOR");

           UtilesWeb.openFile(archivoPdf, "pdf", nombreArchivoPdf.toString());

       } catch (ServiceException e) {
           log.error(e.getMessage());
           AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
       }
   }
   
   
   public void nuevoDesistimientoJEDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.mostrarPopupPF("dialogDesistimientoJE");
   }
   
   public void cerrarDesistimientoJEDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.ocultarPopupPF("dialogDesistimientoJE");
   }

   /**
    * Metodo que envía el resumen de documento de desistimiento
    *
    */
   public void enviarDesistirPortafirmas(ConvocatoriaDTO convDTO) {
       try {
           
           cargarInformacion();
           
           ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                   Boolean.FALSE);
           Expediente exp = this.expedienteService.getDao().find(idExpediente);
           
           
           Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente, 
                   utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), null, null, null, null); 
           
           
           if(insertado.equals(Boolean.TRUE)) {
               
               this.expedienteService.actualizarEstadoInternoJE(exp, expedienteJusEcoSeleccionado, 
                       utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.REVISION_ADM_KO), 
                       utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.REVISION_KO));
                                         
               this.actualizarValoresFormulario();
               
               //validarEstilo();

               AtlasMessageUtils.addInfoMessageFromBundle(
                     ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
           }else {
               AtlasMessageUtils.addErrorMessageFromBundle(
                     ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
           }
                 

       } catch (ServiceException e) {
           log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
           if (e.getKeyExcepcion() == null) {
               AtlasMessageUtils.addErrorMessage(e.getMessage());
           } else {
               AtlasMessageUtils
                       .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
           }
       } 

   }
   
   /**
    * Metodo que envía el resumen de documento de desistimiento
    *
    */
   public void enviarDesistirPortafirmasDoc(ConvocatoriaDTO convDTO, UploadedFile archivoSubirDesistimiento) {
       byte[] documentoAdjunto;
       try {
           
           if(archivoSubirDesistimiento == null) {
               AtlasMessageUtils
               .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
           }else {
               
               InputStream fileInputStream = this.archivoSubirDesistimiento.getInputstream();
               
               documentoAdjunto = IOUtils.toByteArray(fileInputStream);
               
               cargarInformacion();
               ExpedienteDTO expedienteDTO = this.expedienteService.buscarIdSimple(this.idExpediente,
                       Boolean.FALSE);
               
               
               Boolean insertado =  documentoPtfrService.guardarEnvioEnDcoumentoPTFR(convDTO, expedienteDTO, idExpediente, 
                       utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), documentoAdjunto, null,null, null); //Constantes.Tramite.DESISTIMIENTO
               
               if(insertado.equals(Boolean.TRUE)) {
                   this.actualizarValoresFormulario();
                   AtlasMessageUtils.addInfoMessageFromBundle(
                         ConstantesWeb.Mensajes.ENVIO_COLA_PORTAFIRMAS_CORRECTO);
               }else {
                   AtlasMessageUtils.addErrorMessageFromBundle(
                         ConstantesWeb.Err.ENVIO_COLA_DUPLICADO);
               }
           }
           

       } catch (ServiceException e) {
           log.error(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO, e.getMessage());
           if (e.getKeyExcepcion() == null) {
               AtlasMessageUtils.addErrorMessage(e.getMessage());
           } else {
               AtlasMessageUtils
                       .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
           }
       } catch (IOException e) {
           AtlasMessageUtils
           .addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_ENVIAR_DESISTIMIENTO);
       } 

   }
   
  
   public void nuevodialogEnvioColaSubsanacionDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.mostrarPopupPF("dialogEnvioColaSubsanacionJE");
   }
   
   public void cerrardialogEnvioColaSubsanacionDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.ocultarPopupPF("dialogEnvioColaSubsanacionJE");
   }

   
   public void nuevoDesistimientoDocDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.mostrarPopupPF("dialogDesistimientoExpedienteDocJE");
   }
   
   public void cerrarDesistimientoDocDialog() {
       this.formularioDocExternoDTO = new ExpedienteDocDTO();
       PrimefacesUtils.ocultarPopupPF("dialogDesistimientoExpedienteDocJE");
   }
   
   
   public Double calcularAyudaConcedidaConSumatorio(Integer idExpedientePresup) {
       Double ayudaConcedidaSum = 0D;
       try {
           //VMM 05/05/26 MD-2595: calculamos esta ayuda solo si tenemos un exp jus eco seleccionado
           if (this.expedienteJusEcoSeleccionado != null) {
              
               Double importeSum = calcularSumImportePartida(idExpedientePresup);
               
               if (Objects.isNull(convocatoriaDTO)) {
                   convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
               }
               
               if (convocatoriaDTO != null && convocatoriaDTO.getPorcentajeIntensidadAyuda() != null && importeSum != null) {
                   ayudaConcedidaSum = importeSum * (convocatoriaDTO.getPorcentajeIntensidadAyuda() / 100);
                   
                   ayudaConcedidaSum = BigDecimal.valueOf(ayudaConcedidaSum)
                           .setScale(2, RoundingMode.HALF_UP)
                           .doubleValue();
               }
           }
           
       } catch(Exception e) {
           log.error(e.getMessage());
       }
       return ayudaConcedidaSum;
   }
   
   
   public Double calcularAyudaConcedidaSinSumatorio(Double importe) {
       Double ayudaConcedida = 0D;
       try {
           //VMM 05/05/26 MD-2595: calculamos esta ayuda solo si tenemos un exp jus eco seleccionado
           if (this.expedienteJusEcoSeleccionado != null) {
               
               if (Objects.isNull(convocatoriaDTO)) {
                   convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);
               }
               
               if (convocatoriaDTO != null && convocatoriaDTO.getPorcentajeIntensidadAyuda() != null && importe != null) {
                   ayudaConcedida = importe * (convocatoriaDTO.getPorcentajeIntensidadAyuda() / 100);
                   
                   ayudaConcedida = BigDecimal.valueOf(ayudaConcedida)
                           .setScale(2, RoundingMode.HALF_UP)
                           .doubleValue();
               }
           }
       } catch(Exception e) {
           log.error(e.getMessage());
       }
       return ayudaConcedida;
   }
   
   
   public Double calcularSumImportePartida(Integer idExpdtePresup) {
       Double sumaImporte = 0D;

       try {      
           //VMM 05/05/26 MD-2595: calculamos esta ayuda solo si tenemos un exp jus eco seleccionado
           if (this.expedienteJusEcoSeleccionado != null) {
               
      
               // 1️. Recuperar el expediente presupuesto base
               ExpedientePresupDTO expPresupDTO = expedientePresupService.buscarId(idExpdtePresup);
               if (expPresupDTO == null || expPresupDTO.getTipoFinanciacionDTO() == null || expPresupDTO.getTematicaDTO() == null || idExpediente == null) {
                   return sumaImporte; // sin datos -> devolvemos 0
               }
    
               // 2️. Asegurar que tenemos la convocatoria cargada
               if (convocatoriaDTO == null) {
                   convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
               }
    
               // 3️. Buscar expedientes presupuesto relacionados (mismo expediente, tipoFinanciacion y temática)
               List<AtlasFilter> filtrosExpPresup = new ArrayList<>();
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_PRESU_TIPO_FINANCIACION, expPresupDTO.getTipoFinanciacionDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_TEMATICA, expPresupDTO.getTematicaDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupDTO> listaExpPresup = expedientePresupService.buscar(0, -1, filtrosExpPresup, null);
    
               if (listaExpPresup == null || listaExpPresup.isEmpty()) {
                   return sumaImporte;
               }
    
               // 4️. Obtener los IDs únicos
               List<Integer> idsExpPresupUnicos = listaExpPresup.stream()
                       .map(ExpedientePresupDTO::getIdExpedientePresup)
                       .filter(Objects::nonNull)
                       .distinct()
                       .collect(Collectors.toList());
    
               if (idsExpPresupUnicos.isEmpty()) {
                   return sumaImporte;
               }
    
               // 5️. Determinar la anualidad a filtrar
               Integer nmAnualidad = convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)
                       ? expedienteJusEcoSeleccionado.getNmPeriodo()
                       : expedienteJusEcoSeleccionado.getNmPeriodo() - 1;
    
               // 6️. Buscar los montos de esos expedientes
               List<AtlasFilter> filtrosMonto = new ArrayList<>();
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PRESUP, idsExpPresupUnicos, Operador.IN));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD, nmAnualidad, Operador.EQUALS));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupMontoDTO> listaMontos = expedientePresupMontoService.buscar(0, -1, filtrosMonto, null);
    
               // 7️. Sumar importes con precisión
               sumaImporte = listaMontos == null ? 0D :
                       listaMontos.stream()
                               .map(ExpedientePresupMontoDTO::getNmImporte)
                               .filter(Objects::nonNull)
                               .mapToDouble(Double::doubleValue)
                               .sum();
    
               // 8️. Redondear a 2 decimales
               return BigDecimal.valueOf(sumaImporte)
                       .setScale(2, RoundingMode.HALF_UP)
                       .doubleValue();
           }
       } catch (Exception e) {
           log.error("Error al calcular sumatorio de importe por partida: {}", e.getMessage(), e);
           return 0D;
       }
       return sumaImporte;
   }

   
   public Double calcularSumImporteJustifPartida(Integer idExpdtePresup) {
       Double sumaImporte = 0D;

       try {
           //VMM 05/05/26 MD-2595: calculamos esta ayuda solo si tenemos un exp jus eco seleccionado
           if (this.expedienteJusEcoSeleccionado != null) {
               
               // 1️. Recuperar el expediente presupuesto base
               ExpedientePresupDTO expPresupDTO = expedientePresupService.buscarId(idExpdtePresup);
               if (expPresupDTO == null || expPresupDTO.getTipoFinanciacionDTO() == null || expPresupDTO.getTematicaDTO() == null || idExpediente == null) {
                   return sumaImporte; // sin datos -> devolvemos 0
               }
    
               // 2️. Asegurar que tenemos la convocatoria cargada
               if (convocatoriaDTO == null) {
                   convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
               }
    
               // 3️. Buscar expedientes presupuesto relacionados (mismo expediente, tipoFinanciacion y temática)
               List<AtlasFilter> filtrosExpPresup = new ArrayList<>();
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_PRESU_TIPO_FINANCIACION, expPresupDTO.getTipoFinanciacionDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_TEMATICA, expPresupDTO.getTematicaDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupDTO> listaExpPresup = expedientePresupService.buscar(0, -1, filtrosExpPresup, null);
    
               if (listaExpPresup == null || listaExpPresup.isEmpty()) {
                   return sumaImporte;
               }
    
               // 4️. Obtener los IDs únicos
               List<Integer> idsExpPresupUnicos = listaExpPresup.stream()
                       .map(ExpedientePresupDTO::getIdExpedientePresup)
                       .filter(Objects::nonNull)
                       .distinct()
                       .collect(Collectors.toList());
    
               if (idsExpPresupUnicos.isEmpty()) {
                   return sumaImporte;
               }
    
               // 5️. Determinar la anualidad a filtrar
               Integer nmAnualidad = convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)
                       ? expedienteJusEcoSeleccionado.getNmPeriodo()
                       : expedienteJusEcoSeleccionado.getNmPeriodo() - 1;
    
               // 6️. Buscar los montos de esos expedientes
               List<AtlasFilter> filtrosMonto = new ArrayList<>();
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PRESUP, idsExpPresupUnicos, Operador.IN));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD, nmAnualidad, Operador.EQUALS));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupMontoDTO> listaMontos = expedientePresupMontoService.buscar(0, -1, filtrosMonto, null);
    
               // 7️. Sumar importes con precisión
               sumaImporte = listaMontos == null ? 0D :
                       listaMontos.stream()
                               .map(ExpedientePresupMontoDTO::getNmImporteJustificado)
                               .filter(Objects::nonNull)
                               .mapToDouble(Double::doubleValue)
                               .sum();
    
               // 8️. Redondear a 2 decimales
               return BigDecimal.valueOf(sumaImporte)
                       .setScale(2, RoundingMode.HALF_UP)
                       .doubleValue();
           }           
       } catch (Exception e) {
           log.error("Error al calcular sumatorio de importe justificado por partida: {}", e.getMessage(), e);
           return 0D;
       }
       return sumaImporte;
   }
   
   
   public Double calcularSumImporteAdmitPartida(Integer idExpdtePresup) {
       Double sumaImporte = 0D;

       try {
           //VMM 05/05/26 MD-2595: calculamos esta ayuda solo si tenemos un exp jus eco seleccionado
           if (this.expedienteJusEcoSeleccionado != null) {
               
           
               // 1️. Recuperar el expediente presupuesto base
               ExpedientePresupDTO expPresupDTO = expedientePresupService.buscarId(idExpdtePresup);
               if (expPresupDTO == null || expPresupDTO.getTipoFinanciacionDTO() == null || expPresupDTO.getTematicaDTO() == null || idExpediente == null) {
                   return null; // sin datos -> devolvemos null
               }
    
               // 2️. Asegurar que tenemos la convocatoria cargada
               if (convocatoriaDTO == null) {
                   convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
               }
    
               // 3️. Buscar expedientes presupuesto relacionados (mismo expediente, tipoFinanciacion y temática)
               List<AtlasFilter> filtrosExpPresup = new ArrayList<>();
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente, Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_PRESU_TIPO_FINANCIACION, expPresupDTO.getTipoFinanciacionDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_TEMATICA, expPresupDTO.getTematicaDTO().getIdValor(), Operador.EQUALS));
               filtrosExpPresup.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupDTO> listaExpPresup = expedientePresupService.buscar(0, -1, filtrosExpPresup, null);
    
               if (listaExpPresup == null || listaExpPresup.isEmpty()) {
                   return null;
               }
    
               // 4️. Obtener los IDs únicos
               List<Integer> idsExpPresupUnicos = listaExpPresup.stream()
                       .map(ExpedientePresupDTO::getIdExpedientePresup)
                       .filter(Objects::nonNull)
                       .distinct()
                       .collect(Collectors.toList());
    
               if (idsExpPresupUnicos.isEmpty()) {
                   return null;
               }
    
               // 5️. Determinar la anualidad a filtrar
               Integer nmAnualidad = convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)
                       ? expedienteJusEcoSeleccionado.getNmPeriodo()
                       : expedienteJusEcoSeleccionado.getNmPeriodo() - 1;
    
               // 6️. Buscar los montos de esos expedientes
               List<AtlasFilter> filtrosMonto = new ArrayList<>();
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PRESUP, idsExpPresupUnicos, Operador.IN));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMANUALIDAD, nmAnualidad, Operador.EQUALS));
               filtrosMonto.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
               List<ExpedientePresupMontoDTO> listaMontos = expedientePresupMontoService.buscar(0, -1, filtrosMonto, null);
    
               // 7️. Sumar importes con precisión
               sumaImporte = listaMontos == null ? 0D :
                       listaMontos.stream()
                               .map(ExpedientePresupMontoDTO::getNmImporteAdmitido)
                               .filter(Objects::nonNull)
                               .mapToDouble(Double::doubleValue)
                               .sum();
    
               // 8️. Redondear a 2 decimales
               return BigDecimal.valueOf(sumaImporte)
                       .setScale(2, RoundingMode.HALF_UP)
                       .doubleValue();
           }
       } catch (Exception e) {
           log.error("Error al calcular sumatorio de importe admitido por partida: {}", e.getMessage(), e);
           return null;
       }
       return sumaImporte;
   }
   
   
   public Boolean deshabilitarImporteAdmitidoConcepto(Integer idEstado) throws ServiceException{
       log.info("Entrando en deshabilitarImporteAdmitidoConcepto con idEstadoConcepto(expPresupMonto)={}", idEstado);
       
       //el importe admitido sera editable para el gestor si esta en estado pdte.Revisar o pdte.Subsanar
       if (idEstado != null && (idEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)) ||
               idEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION)))) {
           return Boolean.FALSE;
       }else {
           return Boolean.TRUE;
       }
   }
   
   
   public Boolean deshabilitarImporteAdmitidoPartida(Integer idExpdtePresup) throws ServiceException{
       ExpedientePresupDTO expPresupDTO = this.expedientePresupService.buscarId(idExpdtePresup);
       
       if (expPresupDTO.getEstadoDTO() == null || expPresupDTO.getEstadoDTO().getIdValor() == null) {
           return Boolean.TRUE;
       }
       Integer idEstado = expPresupDTO.getEstadoDTO().getIdValor();
       
       log.info("Entrando en deshabilitarImporteAdmitidoPartida con idEstadoPartida(expPresup)={}", idEstado);
       
       if (convocatoriaDTO == null) {
           convocatoriaDTO = convocatoriaService.buscarId(idConvocatoria);
       }
       
       //si es convocatoria con conceptos el importe admitido por partida es no editable
       if (convocatoriaDTO.getItConceptoPresup() != null && convocatoriaDTO.getItConceptoPresup().equals(Constantes.AfirmacionNegacion.S)) {
           return Boolean.TRUE;
       }
       //el importe admitido sera editable para el gestor si esta en estado pdte.Revisar o pdte.Subsanar
       if (idEstado != null && (idEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)) ||
               idEstado.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_SUBSANACION)))) {
           return Boolean.FALSE;
       }else {
           return Boolean.TRUE;
       }
   }
   

   public String obtenerEstadoConcepto(Integer idEstado) {
       try {
           CatalogosValoresDTO estadoDTO = this.catalogosValoresService.buscarId(idEstado);
           
           if (estadoDTO != null && estadoDTO.getDsValor() != null && !StringUtils.isBlank(estadoDTO.getDsValor())) {
               return estadoDTO.getDsValor();
           }
       } catch(Exception e) {
           log.error(e.getMessage());
       }
       return "";
   }
   
   
   public String obtenerEstadoPartida(Integer idExpdtePresup) {
       try {
           ExpedientePresupDTO expPresupDTO = this.expedientePresupService.buscarId(idExpdtePresup);
           
           if (expPresupDTO != null && expPresupDTO.getEstadoDTO() != null ) {
               return expPresupDTO.getEstadoDTO().getDsValor();
           }
       } catch(Exception e) {
           log.error(e.getMessage());
       }
       return "";
   }
   
   
   public String consultarPartidaPresupMonto(Integer idExpdtePresup) {
       try {
        ExpedientePresupDTO expPresupDTO = this.expedientePresupService.buscarId(idExpdtePresup);
        
        if (expPresupDTO != null && expPresupDTO.getTipoFinanciacionDTO() != null) {
            return expPresupDTO.getTipoFinanciacionDTO().getDsValor();
        }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
       return "";
   }
   
   
   public void actualizarImporteAdmitido(Integer idExpedientePresupMonto, Double importeAdmitido) {
       try {
           if (idExpedientePresupMonto != null && importeAdmitido != null) {
               
               //buscar el objeto presup monto actualizamos el importe admitido y guardamos
               ExpedientePresupMontoDTO expedientePresupMontoDTO = expedientePresupMontoService.buscarId(idExpedientePresupMonto);
               expedientePresupMontoDTO.setNmImporteAdmitido(importeAdmitido);
               //mirar si hacen falta aqui los campos de modif de auditoria
               
               expedientePresupMontoService.guardarConHistorico(expedientePresupMontoDTO);
               
               //this.actualizarValoresFormulario();
               PrimeFaces.current().ajax().update(ConstantesWeb.Referencia.EXPEDIENTE_JUSECO_ID_CONTENEDOR_DOCUMENTOS); 
            
               AtlasMessageUtils.addInfoMessage("Importe admitido actualizado correctamente");
           }else {
               //VMM 26/01/26 MD-2213: cambiamos el mensaje de error para no permitir nulos
               AtlasMessageUtils.addErrorMessage("El importe admitido no puede ser nulo");
           }
       } catch (ServiceException e) {
           log.error(e.getMessage());
           AtlasMessageUtils.addErrorMessage("Error al actualizar el importe admitido");
       }
   }

   public boolean habilitarBotonConsultarEstado(Integer estadoDoc, Integer estadoExp) {
       if ((infoUsuarioBean.getRolSuperAdministrador() ||
             infoUsuarioBean.getRolAdministrador() ||
             infoUsuarioBean.getRolSupervisor() ||
             infoUsuarioBean.getRolGestor())  
             && (estadoDoc.equals(Constantes.EstadoDocumento.REVISION_KO) || estadoDoc.equals(Constantes.EstadoDocumento.PENDIENTE_REVISION)) 
             && estadoExp.equals(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA)) {
           return Boolean.TRUE;
           
       }else {
           return Boolean.FALSE;
       }
   }
    
}
