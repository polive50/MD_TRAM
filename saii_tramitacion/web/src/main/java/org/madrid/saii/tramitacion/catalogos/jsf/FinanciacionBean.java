package org.madrid.saii.tramitacion.catalogos.jsf;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.ExpedienteActuaDTO;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.services.BloqueComponenteService;
import org.madrid.saii.lib.services.BloqueService;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ComponenteService;
import org.madrid.saii.lib.services.ConvocatoriaService;
import org.madrid.saii.lib.services.DatoService;
import org.madrid.saii.lib.services.ExpedienteActuaHistService;
import org.madrid.saii.lib.services.ExpedienteActuaService;
import org.madrid.saii.lib.services.ExpedienteAnualidadService;
import org.madrid.saii.lib.services.ExpedienteJusTecService;
import org.madrid.saii.lib.services.ExpedientePresupHistService;
import org.madrid.saii.lib.services.ExpedientePresupMontoService;
import org.madrid.saii.lib.services.ExpedientePresupService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.FinanciacionDatoService;
import org.madrid.saii.lib.services.FinanciacionService;
import org.madrid.saii.lib.services.ImpresoService;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.madrid.saii.tramitacion.jsf.utils.ValidacionConceptosPresupuesto;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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

@Slf4j
@Getter
@Setter
@Component("financiacionBean")
@Scope("request")
public class FinanciacionBean {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /* Id del expediente */
    private Integer idExpediente;

    /* Id estado del expediente */
    private Integer idEstadoExpediente;

    /* Lista de anualidades */
    private List<Integer> listaAnualidades;
    
    /*
     * Lista con el número de columnas a mostrar en la tabla dinámica de
     * presupuesto financiable
     */
    private List<Integer> listaNcolPresupuesto;
       
    /*
     * Lista con las columnas a mostrar en la tabla dinámica de
     * ayuda solicitada
     */
    private List<Integer> listaNcolAyudaSolicitada;
    
    /*
     * Lista con las columnas a mostrar en la tabla dinámica de
     * ayuda solicitada
     */
    private List<Integer> listaNcolAyudaConcedida;

    private List<Integer> listaNcolActuacion;

    /* Total de anualidades */
    private int totalAnualidades;
    
    /* porcentaje intensidad ayuda */
    private Double porcentajeIntensidadAyuda;
    
    /* porcentaje intensidad ayuda por parte del gestor*/
    private Double porcentajeIntensidadGestor;
    
    /* porcentaje intensidad ayuda */
    private String porcentajeIntensidadAyudaStr;
    
    /* porcentaje intensidad ayuda por parte del gestor*/
    private String porcentajeIntensidadGestorStr;

    /* Lista bloque componente columna */
    private List<BloqueComponenteDTO> lstBloqueComponenteColumna;

    private List<BloqueComponenteDTO> lstBloqueComponenteColumnaActua;

    /* Lista bloque componente fila */
    private List<BloqueComponenteDTO> lstBloqueComponenteFila;

    private List<BloqueDTO> lstBloque;

    private List<BloqueComponenteDTO> lstBloqueComponenteFilaActua;

    /*
     * variable para visualizar la ayuda solicitda y concedida: caso presupuesto
     * sin ayuda solicitada
     */
    private Boolean ocultarAyuda;

    private ExpedientePresupDTO presupuestoSeleccionado;

    private ExpedienteActuaDTO actuacionSeleccionado;

    /*
     * Variable aplicada para los titulos de las tablas cuando existe más de un
     * presupuesto
     */
    private Boolean verNumeracion;

    /**
     * Identificador de solapa de financiacion
     */
    private String solapaFinanciacion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas";
    
    /* Ruta de la tabla presupuesto financiable */
    private String tablaPresupuestoFinanciable = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgPresupuestoFinanciable";

    private String tablaPresupuestoFinanciable2 = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgPresupuestoFinanciable2";

    private String mensajeAdvertenciaPresup = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:msgWarningPresupuesto";

    private String tablaActuacionFinanciable = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgActuacionFinanciable";

    private String mensajeAdvertenciaActu = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:msgWarningActuacion";
    
    private String inputMontoAyudaConcedida = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:ipnMontoConcedida";
    
    private String campoEstadoExpediente = "convocatoriaForm:contenedorSolapas:dsEstadoExpedienteSel";
    
    private String formularioFinanciacion = "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelFinanciacion";

    private List<BloqueComponenteDTO> lstPresupuestoPresentado;

    private List<ExpedientePresupDTO> lstPresupuestoFinanciable;

    private List<ExpedientePresupDTO> lstPresupuestoFinanciable1;

    private List<ExpedientePresupDTO> lstPresupuestoFinanciable2;
    
    private List<ExpedientePresupDTO> lstPresupuestoFinanciable3;

    private List<BloqueComponenteDTO> lstPresupuestoAyudaSolicitada;
    
    private List<BloqueComponenteDTO> lstPresupuestoAyudaSolicitada2;

    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedida;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedida2;
    
    private List<ExpedientePresupDTO> lstPresupuestoAyudaConcedida3;

    private List<BloqueComponenteDTO> lstActuacionPresentado;

    private List<ExpedienteActuaDTO> lstActuacionFinanciable;
    
    private List<FinanciacionDTO> lstTablasFinan;

    private Double montoAyudaPresupuesto;
    
    private Double montoAyudaPresupuestoPresentado;

    private Double montoAyudaSolicitada;
    
    private Double montoAyudaConcedida;
    
    private Double montoAyudaConcedida2;

    private Boolean pendienteGuardarObservacion;

    private Boolean tieneErrores;

    /* Monto ayuda actuación */
    private Double montoAyudaActuacion;

    /* Monto ayuda concedida actuación */
    private Double montoAyudaConcedidaActuacion;

    /* Monto total de las actuaciones */
    private Double montoTotalActuacion;

    /* Monto total auxiliar de las actuaciones */
    private Double montoTotalActuacionAux;

    /* Monto maximo actuacion concedida */
    private Double montoMaxAyudaConceAct;

    /* Monto original */
    private Double montoOriginal;

    /* Observación para expediente presupuesto */
    private String tlObservacionPresupuesto;

    /* Observación para expediente actuación */
    private String tlObservacion;

    /* Lista importes máximos permitidos en actuación */
    private List<FinanciacionDatoDTO> lstImpMaxAct;

    /* Ver mensaje Actuación tras editar una celda */
    private Boolean verMsgObsActuacion;

    /* Constante maximo caracteres observacion */
    private Integer maxLenObservacion;

    /* modoConsulta */
    private Boolean modoConsultaFinanciacion;

    /* Estilo pestaña financiacion */
    private Boolean bAvisoFinanciacion;

    /* modoConsulta */
    private Boolean esconderBotonCargarDatoPresup;

    /* modoConsulta */
    private Boolean esconderBotonCargarDatoActuacion;
    
    /* modoConsulta */
    private Boolean esconderBotonFinalizarRevision;
    
    private Boolean esConvocatoriaConceptoPresup;

    /* ExpedienteJusTec */
    //private List<ExpedienteJusTecDTO> listaExpJusTecDTO;
    
    /* ExpedienteAnualidad */
    private List<ExpedienteAnualidadDTO> listaExpAnualDTO;
    
    /* La intensidad de la ayuda es errónea */
    private Boolean esformatoErroneoIntensidad = Boolean.FALSE;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteActuaService")
    private ExpedienteActuaService expedienteActuaService;

    @Autowired
    @Qualifier("expedienteActuaHistService")
    private ExpedienteActuaHistService expedienteActuaHistService;

    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;

    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;

    @Autowired
    @Qualifier("financiacionDatoService")
    private FinanciacionDatoService financiacionDatoService;

    @Lazy
    @Autowired
    @Qualifier("financiacionService")
    private FinanciacionService financiacionService;
    
    /**
     * Servicio de expedienteDocService
     */
    @Autowired
    @Qualifier("expedientePresupHistService")
    private ExpedientePresupHistService expedientePresupHistService;

    /**
     * Servicio de expedienteJusTecService
     */
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    
    /**
     * Servicio de expedienteAnualidadService
     */
    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;
    
    /* Servicio catalogo valores */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    /**
     * Servicio de expedienteService
     */
    @Lazy
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// BEANS
    // ////////////////////////////////////////////////////////
    
    private ExpedienteBean expedienteBean;
    
    @Lazy
    @Autowired
    @Qualifier("convocatoriasBean")
    private ConvocatoriasBean convocatoriasBean;
    
    
    ///////////////////////////////DAOS/////////////////////////
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor de clase
     */
    public FinanciacionBean() {
        super();
        this.pendienteGuardarObservacion = Boolean.FALSE;
        this.verMsgObsActuacion = Boolean.FALSE;
        this.tieneErrores = Boolean.FALSE;
        this.ocultarAyuda = Boolean.FALSE;
        this.verNumeracion = Boolean.FALSE;
        this.bAvisoFinanciacion = Boolean.FALSE;
        this.esConvocatoriaConceptoPresup = Boolean.FALSE;
    }

    
    /**
     * Metodo para obtener los datos de financiación del expediente
     * @throws ServiceException 
     */
    public void obtenerDatosFinanciacionExpediente() throws ServiceException {

        this.lstPresupuestoPresentado = new ArrayList<>();
        this.montoAyudaPresupuesto = null;
        this.lstPresupuestoFinanciable = new ArrayList<>();
        this.lstPresupuestoFinanciable1 = new ArrayList<>();
        this.lstPresupuestoFinanciable2 = new ArrayList<>();
        this.lstPresupuestoFinanciable3 = new ArrayList<>();
        this.montoAyudaConcedida = null;
        this.montoAyudaConcedida2 = null;
        //VMM 28/10/26 MD-3094: faltaba reiniciarla, arrastraba la ayuda solicitada del expediente anterior
        this.montoAyudaSolicitada = null;

        this.lstActuacionPresentado = new ArrayList<>();
        this.montoAyudaActuacion = null;
        this.lstActuacionFinanciable = new ArrayList<>();
        this.montoAyudaConcedidaActuacion = null;
        this.tlObservacion = "";
        this.tlObservacionPresupuesto = "";  //esto ya no se usa eran las observaciones del final y ahora hay una por cada registro exp presup
        this.maxLenObservacion = 2600;  //si es mas de esta longitud tiene que ser un dato tipo CLOB
        this.pendienteGuardarObservacion = Boolean.FALSE;
        this.verMsgObsActuacion = Boolean.FALSE;
        this.tieneErrores = Boolean.FALSE;
        this.ocultarAyuda = Boolean.FALSE;
        this.verNumeracion = Boolean.FALSE;
        this.bAvisoFinanciacion = Boolean.FALSE;

        this.esModoConsulta();

        this.lstImpMaxAct = new ArrayList<>();

        try {

            if (Objects.nonNull(this.expedienteBean)) {
                this.lstBloque = new ArrayList<>();
                // Recuperamos los datos de la convocatoria
                ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                        .buscarId(this.expedienteBean.getIdConvocatoria());

                this.totalAnualidades = convocatoriaDTO.getNmDurMaxAyuda();
                
                //MD-2788: precargamos los valores de intensidad con lo que haya configuado en la convocatoria
                //y luego lo machacamos con lo que se haya introducido en el formulario de solicitud si es que existe ese bloque
                this.porcentajeIntensidadAyuda = convocatoriaDTO.getPorcentajeIntensidadAyuda(); 
                this.porcentajeIntensidadGestor = convocatoriaDTO.getPorcentajeIntensidadAyuda();
                
                // Si tiene intensidad de la ayuda regsitrada en SOL machacamos las intensidades
                this.recuperarIntensidadAyuda(this.expedienteBean.getIdExpediente(),
                        utilConstantes.getGlobalComponente()
                                .get(Constantes.ComponenteIntensidad.PORCENTAJE_INTENSIDAD));
                
                this.esConvocatoriaConceptoPresup = Constantes.AfirmacionNegacion.S
                		.equalsIgnoreCase(convocatoriaDTO.getItConceptoPresup());

                // Construimos la lista de anualidades
                int[] anualidadesArray = IntStream.range(1, this.totalAnualidades + 1).toArray();
                this.listaAnualidades = new ArrayList<>();
              
                for (int arr : anualidadesArray) {
                    this.listaAnualidades.add(arr);
                }
  
                List<Integer> tipoImpreso = new ArrayList<>();
                
                if(this.convocatoriasBean.getConvocatoriaDTO().getIdTipoConcesion().getIdValor().
                        equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoConcesion.CONCESION_PARTICIPANTE))) {
                    tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
                }else {
                    tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
                }

                ImpresoDTO impresoDTO = this.impresoService.buscarPorConvActor(
                        convocatoriaDTO.getIdConvocatoria(),
                        this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor(),
                        tipoImpreso);

                if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getLstBloqueDTO())
                        && !impresoDTO.getLstBloqueDTO().isEmpty()) {
                    this.obtenerPorcentajeIntensidad(impresoDTO);
                    this.obtenerPresupuestoPresentado(impresoDTO);
                    this.obtenerAyudaSolicitadaPresupuesto();

                    this.verNumeracion = this.lstBloque.size() > 1;

                    this.obtenerActuacionPresentado();
                    this.obtenerAyudaSolicitadaActuacion();
                }

                // VMM: montoAyudaSolicitada se calcula aqui, antes de obtenerPresupuestoFinanciable, porque
                // este ultimo ya usa montoAyudaSolicitada (via recalcularMinAyudaConcedida) para topar montoAyudaConcedida
                if (this.getLstBloque() != null && !this.getLstBloque().isEmpty()) {
                    this.cargarDatosAyudaSolicitada();
                }

                this.obtenerPresupuestoFinanciable(1);
                this.obtenerPresupuestoFinanciable(2);

                //VMM 04/05/26 MD-2723: mostrar solo el presupuesto provisional cuando corresponda para no machacar monto de ayuda concedida y por eficiencia
                if (mostrarPresupuestoProvisional(this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getNmConstante())) {
                    this.obtenerPresupuestoProvisional();
                }
                this.obtenerActuacionFinanciable();

                this.validarEstilo();

                //this.listaExpJusTecDTO = this.expedienteJusTecService.obtenerExpedientesJustecByIdExp(this.idExpediente);

                this.ocultarAyuda = Objects.isNull(this.montoAyudaConcedida) ? Boolean.TRUE
                        : Boolean.FALSE;                
            }

            this.esconderBotonCargarPresup();
            this.esconderBotonCargarActuaciones();
            this.esconderBotonFinalizarRevision();

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }

    }
    
    // método que recupera machaca el valor de la intensidad si la encuentra introducida en el formualrio SOL
    private void recuperarIntensidadAyuda(Integer idExpediente, Integer idComponente) throws ServiceException {
        // VMM 11/08/26: si la convocatoria no tiene configurado el bloque de "% intensidad" (a proposito,
        // en algunas convocatorias no aplica), idComponente llega null y no hay nada que buscar
        if (idComponente == null) {
            return;
        }
        DatoDTO datoDTO = this.datoService.obtenerPorIdExpedienteYIdComponente(idExpediente, idComponente);
        if (datoDTO != null && datoDTO.getDsValor() != null) {
            // el ciudadano a veces informa el porcentaje con el simbolo "%" incluido (ej. "70%" en vez de "70")
            // y con coma decimal/punto de miles en vez del formato con punto decimal (ej. "12,5" o "1.234,56")
            String valorIntensidad = Utils.normalizarSeparadorDecimal(
                    StringUtils.trim(StringUtils.remove(datoDTO.getDsValor(), "%")));
            if (Boolean.TRUE.equals(Utils.validarDouble(valorIntensidad))) {
                this.porcentajeIntensidadAyuda = Double.parseDouble(valorIntensidad);
                this.porcentajeIntensidadGestor = Double.parseDouble(valorIntensidad);
            } else {
                this.porcentajeIntensidadAyuda = null;
                this.porcentajeIntensidadGestor = null;
                if (valorIntensidad.length() > 0) {
                    this.porcentajeIntensidadAyudaStr = valorIntensidad;
                    this.porcentajeIntensidadGestorStr = valorIntensidad;
                }
            }
        }
    }


    private void obtenerPorcentajeIntensidad(ImpresoDTO impresoDTO) {
        
        // Obtener lista de bloques de la intensidad de la ayuda del ciudadano (aunque solo será un bloque)
        try {
            Integer idFormatoIntensidad = utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Formatos.ID_PORCENTAJE_INTENSIDAD);
            // VMM 11/08/26: si la convocatoria no tiene configurado el formato de "% intensidad" (a proposito,
            // en algunas convocatorias no aplica), idFormatoIntensidad llega null y no hay nada que buscar
            if (idFormatoIntensidad == null) {
                return;
            }

            List<BloqueDTO> lstBloquesIntensidad = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
                    idFormatoIntensidad);

            List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
            if (lstBloquesIntensidad != null && !lstBloquesIntensidad.isEmpty()) { //solo será un bloque componente
                for (BloqueDTO bloqueDTO : lstBloquesIntensidad) {  

                    lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .collect(Collectors.toList());
                    
                    //MD-2788: cargamos los porcentajes de la ayuda en base a lo que haya en el formulario de solicitud (si lo hay)
                    //sino se inicializó con los valores de ADM al configurar la convocatoria
                    //luego cogeremos la intensidad si tenemos expediente_presup cargado ya para que lo modifique el gestor
                    if (lstBloqueComponenteDTO != null && !lstBloqueComponenteDTO.isEmpty() 
                            && lstBloqueComponenteDTO.get(0).getDatos() != null && !lstBloqueComponenteDTO.get(0).getDatos().isEmpty()) {
                        
                        //buscamos de todos los datos con ese id_bloque_componente solo el que sea del expediente en cuestion
                        List<DatoDTO> datosFiltrados = lstBloqueComponenteDTO.get(0).getDatos().stream()
                                .filter(d-> d.getExpedienteDTO() != null  && 
                                        this.expedienteBean.getIdExpediente().equals(d.getExpedienteDTO().getIdExpediente())
                                        && d.getFcBajaFila() == null)
                                .sorted(Comparator.comparingInt(DatoDTO::getIdDato).reversed())
                                .collect(Collectors.toList());
                                
                        // VMM 29/09/26: datosFiltrados puede quedar vacio si el bloque componente de "%
                        // intensidad" no tiene ningun dato asociado a este expediente en concreto (p.ej.
                        // el ciudadano no informo ese campo) - sin esta guarda, get(0) lanzaba
                        // IndexOutOfBoundsException al entrar al expediente.
                        if (!datosFiltrados.isEmpty() && datosFiltrados.get(0).getDsValor() != null) {
                            // el ciudadano a veces informa el porcentaje con el simbolo "%" incluido (ej. "70%" en vez de "70")
                            // y con coma decimal/punto de miles en vez del formato con punto decimal (ej. "12,5" o "1.234,56")
                            String valorIntensidad = Utils.normalizarSeparadorDecimal(
                                    StringUtils.trim(StringUtils.remove(datosFiltrados.get(0).getDsValor(), "%")));
                            if (Boolean.TRUE.equals(Utils.validarDouble(valorIntensidad))) {
                                this.porcentajeIntensidadAyuda = Double.parseDouble(valorIntensidad);
                                this.porcentajeIntensidadGestor = Double.parseDouble(valorIntensidad);
                            }
                        }
                    }
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
        
    }


    // Valida el estilo de la pestaña de financiación
    private void validarEstilo() throws ServiceException {

        this.setBAvisoFinanciacion(Boolean.FALSE);

        if (Objects.nonNull(this.lstBloqueComponenteFilaActua)
                && this.lstBloqueComponenteFilaActua.isEmpty()) {
            this.lstBloqueComponenteFilaActua = null;
        }

        if (Objects.nonNull(this.lstBloqueComponenteFila)
                && this.lstBloqueComponenteFila.isEmpty()) {
            this.lstBloqueComponenteFila = null;
        }

        //VMM 05/02/26 MD-2339: no ponemos en rojo la pestaña si es un auditor en todas estas comprobaciones
        // si existe presupuesto y actuacion
        if (Objects.nonNull(this.lstBloqueComponenteFila)
                && Objects.nonNull(this.lstBloqueComponenteFilaActua)
                && this.lstPresupuestoFinanciable1.isEmpty()
                && this.lstActuacionFinanciable.isEmpty() 
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) {
            this.setBAvisoFinanciacion(Boolean.TRUE);
        }

        // solo presupuesto
        if (Objects.isNull(this.lstBloqueComponenteFilaActua)
                && Objects.nonNull(this.lstBloqueComponenteFila)
                && this.lstPresupuestoFinanciable1.isEmpty() 
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) { 
            this.setBAvisoFinanciacion(Boolean.TRUE);
        }

        // solo actuacion
        if (Objects.isNull(this.lstBloqueComponenteFila)
                && Objects.nonNull(this.lstBloqueComponenteFilaActua)
                && this.lstActuacionFinanciable.isEmpty() 
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) { 
            this.setBAvisoFinanciacion(Boolean.TRUE);
        }
        
        //VMM 28/07/25 MD-1687: si existen tablas de financiacion con estado pdte revision, ponemos el aviso de finan activo
        if (this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor() != null && 
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor()) 
                && !Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) { 
            this.setBAvisoFinanciacion(Boolean.TRUE);
        }
        
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDO)
                        .equals(this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor())
                // MD-2397 Incluimos la condición de que esté en el estado
                // provisional DESISTIDO PROVISIONAL
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)
                        .equals(expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor())
                // MD-2399 Incluimos la condición de que esté en el estado
                // provisional EXCLUIDO PROVISIONAL
                || utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)
                        .equals(expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor())) {
            this.setBAvisoFinanciacion(Boolean.FALSE);
        }
        
    }

    /**
     * Metodo para obtener los datos del presupuesto presentado
     *
     * @param impresoDTO
     */
    private void obtenerPresupuestoPresentado(ImpresoDTO impresoDTO) {
        try {

            // Obtener lista de bloques de presupuesto
            this.lstBloque = this.bloqueService.buscarPorFormato(impresoDTO.getIdImpreso(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO));
            
            // si algún bloque no tiene temática es porque es un subbloque y debemos indicar la temática de su padre
            for (BloqueDTO bloqueDTO : this.lstBloque) {
                if (bloqueDTO.getIdTematica() == null
                        || bloqueDTO.getIdTematica().getIdValor() == null) {
                    BloqueDTO bloquePadreDTO = this.bloqueService
                            .buscarByIdBlqPad(bloqueDTO.getIdBloquePadre());
                    if (bloquePadreDTO != null && bloquePadreDTO.getIdTematica() != null && bloquePadreDTO.getIdTematica().getIdValor() != null) {
                        bloqueDTO.setIdTematicaPadre(bloquePadreDTO.getIdTematica());
                    }
                }
            }

            // log.info("lstBloque:: " + this.lstBloque.size());

            List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
            if (!this.lstBloque.isEmpty()) {
                for (BloqueDTO bloqueDTO : this.lstBloque) {

                    lstBloqueComponenteDTO = bloqueDTO.getLstBloqueComponenteDTO().stream()
                            .filter(x -> x.getFcBajaFila() == null)
                            .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                            .collect(Collectors.toList());
                    
                    Integer idTematicaOrden = null;
                    if (bloqueDTO.getIdTematica() != null && bloqueDTO.getIdTematica().getIdValor() != null) {
                        idTematicaOrden = bloqueDTO.getIdTematica().getIdValor();
                    } else if (bloqueDTO.getIdTematicaPadre() != null && bloqueDTO.getIdTematicaPadre().getIdValor() != null) {
                        idTematicaOrden = bloqueDTO.getIdTematicaPadre().getIdValor();
                    }
                    
                    //VMM 01/04/26 MD-2639: ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
                    FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP", impresoDTO.getIdConvocatoria(), 
                            idTematicaOrden, impresoDTO.getConvocatoriaActDTO().getIdActor());
                    
                    List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
                    
                    List<BloqueComponenteDTO> filas = bloqueDTO.getLstBloqueComponenteDTO().stream()
                            .filter(f -> f.getItFila()).collect(Collectors.toList());
                    
                   // VMM 19/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
                    for (BloqueComponenteDTO p : filas) {
                        if (p.getTipoFinanciacionDTO() != null) {
                            Integer orden = listaFinancDato.stream()
                                .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null
                                 && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                                .map(FinanciacionDatoDTO::getNmOrden)
                                 // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
                                .filter(Objects::nonNull)
                                .findFirst()
                                .orElse(9999); // Si no tiene orden, al final
                                
                            p.getTipoFinanciacionDTO().setOrdenPartida(orden);
                        }
                    }
                    
                    if(!filas.isEmpty()) {
                        filas = filas.stream()
                                .sorted((c1, c2) -> informeHelperPdfService.compareByNumOrdenAndConcept(c1, c2, listaFinancDato))
                                .collect(Collectors.toList());
                    }
                    
                    bloqueDTO.setListaBloqueAuxDTO(filas);
                                        
                    if (!bloqueDTO.getListaBloqueAuxDTO().isEmpty()) {

                        // Construimos la lista de columnas Presupuesto
                        this.lstBloqueComponenteColumna = lstBloqueComponenteDTO.stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila())
                                        && Boolean.TRUE.equals(p.getItColumna()))
                                .collect(Collectors.toList());

                        if (!this.lstBloqueComponenteColumna.isEmpty() && (esConvocatoriaConceptoPresup == null
                                || !this.esConvocatoriaConceptoPresup)) {
                            this.obtenerFilColPresupPresentado(bloqueDTO);
                        } else if (!this.lstBloqueComponenteColumna.isEmpty() && Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                            this.obtenerFilColPresupPresentadoConcepto(bloqueDTO);
                        }

                        if (this.lstBloque != null && !this.lstBloque.isEmpty() &&  this.lstBloque.get(0).getListaBloqueAuxDTO() != null
                                && ! this.lstBloque.get(0).getListaBloqueAuxDTO().isEmpty()) {
                            
                            //Obtener el último elemento de lstPresupuestoAyudaSolicitada para recuperar el monto total
                            List<DatoDTO> ultimaLista = this.lstBloque.get(0).getListaBloqueAuxDTO().get(this.lstBloque.get(0).getListaBloqueAuxDTO().size() - 1).getDatos();
                            
                            if (!ultimaLista.isEmpty()) {
                                DatoDTO ultimoDato = ultimaLista.get(ultimaLista.size() - 1);                                   
                                if (ultimoDato != null && ultimoDato.getDsValor() != null) {                                    
                                    String valor = ultimoDato.getDsValor();
                                    if (valor != null && !valor.trim().isEmpty()
                                            && (porcentajeIntensidadAyuda != null)) {
                                        this.montoAyudaPresupuestoPresentado = this.parseImporteDsValor(
                                                valor) * (porcentajeIntensidadAyuda / 100);

                                    }
                                    //VMM 11/08/25 MD-1687: metemos que lo calcule en base a la intensidad de la ayuda
                                    //this.montoAyudaPresupuestoPresentado = Double.parseDouble(ultimoDato.getDsValor()) * (porcentajeIntensidadAyuda / 100);

                            } 
                            
                           }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    
    /**
     * VMM 02/09/26 MD-3011: si por un guardado duplicado hay más de un dato vigente (sin baja) para la
     * misma fila/columna, nos quedamos solo con el más reciente (mayor fcAltaFila, desempatando
     * por idDato), para no sumar/mezclar importes duplicados en los totales de presupuesto.
     */
    private List<DatoDTO> deduplicarDatosMasRecientePorFilaColumna(List<DatoDTO> datos) {
        return new ArrayList<>(datos.stream()
                .collect(Collectors.toMap(
                        d -> d.getNmFila() + "-" + d.getNmColumna(),
                        d -> d,
                        (d1, d2) -> {
                            if (d1.getFcAltaFila() == null) {
                                return d2;
                            }
                            if (d2.getFcAltaFila() == null) {
                                return d1;
                            }
                            int cmp = d1.getFcAltaFila().compareTo(d2.getFcAltaFila());
                            if (cmp != 0) {
                                return cmp > 0 ? d1 : d2;
                            }
                            return (d1.getIdDato() != null && d2.getIdDato() != null
                                    && d1.getIdDato() > d2.getIdDato()) ? d1 : d2;
                        }))
                .values());
    }

    /**
     *
     * @throws ServiceException
     */
    private void obtenerFilColPresupPresentado(BloqueDTO bloque) throws ServiceException {

        // Construimos lista con idColumna
        int[] nColArray = IntStream.range(0, this.listaAnualidades.size() + 1).toArray();
        this.listaNcolPresupuesto = new ArrayList<>();

        for (int arr : nColArray) {
            this.listaNcolPresupuesto.add(arr);
        }

        // Recuperar datos de los bloques componente fila
        for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
            List<DatoDTO> datosBloque = this.datoService.buscarDatosByIdExpAndIdBloComp(this.expedienteBean.getIdExpediente(), fila.getIdBloqueComponente(), true);

            List<DatoDTO> datosDedup = this.deduplicarDatosMasRecientePorFilaColumna(datosBloque);
            datosDedup.sort(Comparator.comparing(DatoDTO::getNmColumna));

            fila.setDatos(datosDedup);
            // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
            Utils.normalizarSeparadorDecimalDatos(fila.getDatos());
        }

        // Calculamos los montos totales por fila
        for (int i = 0; i < bloque.getListaBloqueAuxDTO().size(); i++) {
            double sumaFila = bloque.getListaBloqueAuxDTO().get(i).getDatos().stream()
                    .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                            && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                    ? this.parseImporteDsValor(dato.getDsValor())
                                    : 0)
                    .sum();

            DatoDTO dto = new DatoDTO();
            dto.setDsValor(String.valueOf(sumaFila));
            dto.setEsMontoTotal(Boolean.TRUE);
            bloque.getListaBloqueAuxDTO().get(i).getDatos().add(dto);

            // log.info("agregando monto total " + sumaFila + " en la fila " + i);

        }

        // Preparamos espacio para totales por fila
        BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
        bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("TOTAL");
        bloqueComponenteTotalFila.setDatos(new ArrayList<>());

        // Calculamos los montos totales por columna
        for (int i = 0; i < this.listaNcolPresupuesto.size(); i++) {
            double sumaColumna = 0;

            for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
                if (!fila.getDatos().isEmpty() && fila.getDatos().size() > i
                        && StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())) {
                    sumaColumna += this.parseImporteDsValor(fila.getDatos().get(i).getDsValor());
                }
            }

            // log.info("agregando monto total " + sumaColumna + " en la columna " + i);

            DatoDTO dto = new DatoDTO();
            dto.setDsValor(String.valueOf(sumaColumna));
            dto.setEsMontoTotal(Boolean.TRUE);
            bloqueComponenteTotalFila.getDatos().add(dto);
        }

        bloque.getListaBloqueAuxDTO().add(bloqueComponenteTotalFila);

        this.lstBloqueComponenteFila = bloque.getListaBloqueAuxDTO();
    }

    /**
     *
     * @throws ServiceException
     */
    private void obtenerFilColPresupPresentadoConcepto(BloqueDTO bloque) throws ServiceException {

        List<BloqueComponenteDTO> lstFilasAux = bloque.getListaBloqueAuxDTO().stream()
                .filter(f -> f.getItFila()).collect(Collectors.toList());
        
        List<BloqueComponenteDTO> lstFilas = new ArrayList<>();
        
        for(BloqueComponenteDTO bloqueComp:lstFilasAux) {

            // Filtrar datos donde el idExpediente del DatoDTO sea igual al this.idExpediente, y
            // deduplicar (VMM 02/09/26 MD-3011) ANTES de decidir la rama, para que el tamaño de la lista
            // (usado para elegir entre caso normal y multi-concepto) sea el real, sin duplicados.
            List<DatoDTO> datosFiltrados = bloqueComp.getDatos().stream()
                    .filter(dato -> dato.getExpedienteDTO() != null
                        && dato.getExpedienteDTO().getIdExpediente() != null
                        && dato.getExpedienteDTO().getIdExpediente().equals(this.idExpediente))
                    .collect(Collectors.toList());
            bloqueComp.setDatos(this.deduplicarDatosMasRecientePorFilaColumna(datosFiltrados));

            if(bloqueComp.getDatos().size()== listaAnualidades.size()+1) {

                bloqueComp.setDatos(
                        bloqueComp.getDatos().stream()
                        // Ordenar por nmColumna
                        .sorted(Comparator.comparing(DatoDTO::getNmColumna))
                        .collect(Collectors.toList())
                );
                // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                Utils.normalizarSeparadorDecimalDatos(bloqueComp.getDatos());

                double sumaFila = bloqueComp.getDatos().stream()
                        .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                        .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                                && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                        ? this.parseImporteDsValor(dato.getDsValor())
                                        : 0)
                        .sum();

                DatoDTO dto = new DatoDTO();
                dto.setDsValor(String.valueOf(sumaFila));
                dto.setEsMontoTotal(Boolean.TRUE);
                dto.setNmColumna(bloqueComp.getDatos().size());
                bloqueComp.getDatos().add(dto);
                
                lstFilas.add(bloqueComp);
            }else if(bloqueComp.getDatos().size()> listaAnualidades.size()+2) {

                int posCelda= 0;
                bloqueComp.setDatos(
                        bloqueComp.getDatos().stream()
                        // Ordenar por nmFila
                        .sorted(Comparator.comparing(DatoDTO::getNmFila))
                        .collect(Collectors.toList())
                );

                for (int j=posCelda; j< bloqueComp.getDatos().size();j++){
                    List<DatoDTO> datosFilaConcepto= new ArrayList<>();
                    BloqueComponenteDTO bloqueComConcepto= new BloqueComponenteDTO();
                    for (int index = 0; index < listaAnualidades.size()+1; index++) {
                        datosFilaConcepto.add(bloqueComp.getDatos().get(posCelda));
                        posCelda++;
                        j=posCelda;
                    }
                    
                    datosFilaConcepto= datosFilaConcepto.stream().sorted(Comparator.comparing(DatoDTO::getNmColumna)).collect(Collectors.toList());
                    // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                    Utils.normalizarSeparadorDecimalDatos(datosFilaConcepto);
                    bloqueComConcepto.setIdBloque(bloqueComp.getIdBloque());
                    bloqueComConcepto.setDatos(datosFilaConcepto);
                    bloqueComConcepto.setIdBloqueComponente(bloqueComp.getIdBloqueComponente());
                    bloqueComConcepto.setCdUsuAlta(bloqueComp.getCdUsuAlta());
                    bloqueComConcepto.setComponenteDTO(bloqueComp.getComponenteDTO());
                    bloqueComConcepto.setNmOrden(bloqueComp.getNmOrden());
                    bloqueComConcepto.setItFila(bloqueComp.getItFila());
                    bloqueComConcepto.setItColumna(bloqueComp.getItColumna());
                    bloqueComConcepto.setItObligatorio(bloqueComp.getItObligatorio());
                    bloqueComConcepto.setItRelacion(bloqueComp.getItRelacion());
                    bloqueComConcepto.setTipoFinanciacionDTO(bloqueComp.getTipoFinanciacionDTO());
                    bloqueComConcepto.setItSeleccionado(bloqueComp.getItSeleccionado());
                    
                    double sumaFila = bloqueComConcepto.getDatos().stream()
                            .filter(dato -> dato.getNmColumna() != 0) // Ignorar elementos donde nmColumna sea 0
                            .mapToDouble(dato -> Objects.nonNull(dato.getDsValor())
                                    && Boolean.FALSE.equals(dato.getEsMontoTotal())
                                            ? this.parseImporteDsValor(dato.getDsValor())
                                            : 0)
                            .sum();

                    DatoDTO dto = new DatoDTO();
                    dto.setDsValor(String.valueOf(sumaFila));
                    dto.setEsMontoTotal(Boolean.TRUE);
                    if(bloqueComConcepto.getDatos() != null && !bloqueComConcepto.getDatos().isEmpty()) {
                        dto.setNmColumna(bloqueComConcepto.getDatos().size()); 
                        dto.setNmFila(bloqueComConcepto.getDatos().get(0).getNmFila() != null 
                                ? bloqueComConcepto.getDatos().get(0).getNmFila() : 0); 
                    }
                    
                    bloqueComConcepto.getDatos().add(dto);
                    log.info("agregando monto total " + sumaFila + " en la fila " );
                 
                    lstFilas.add(bloqueComConcepto);                   
                }
            }
        }
        
        lstFilas = lstFilas.stream()
                .filter(f -> f.getItFila()).collect(Collectors.toList());
        
        if(lstFilas != null && !lstFilas.isEmpty() && lstFilas.get(0).getDatos() != null
                && !lstFilas.get(0).getDatos().isEmpty()) {
            int[] nColArray = IntStream.range(0, lstFilas.get(0).getDatos().size()).toArray();
            this.listaNcolPresupuesto = new ArrayList<>();
            
            for (int arr : nColArray) {
                this.listaNcolPresupuesto.add(arr);
            }
        }else {
            int[] nColArray = IntStream.range(0, this.listaAnualidades.size()).toArray();
            this.listaNcolPresupuesto = new ArrayList<>();
            
            for (int arr : nColArray) {
                this.listaNcolPresupuesto.add(arr);
            }
        }
        
        bloque.setListaBloqueAuxDTO(lstFilas);
        
        // Preparamos espacio para totales por fila
        BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
        bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("TOTAL");
        bloqueComponenteTotalFila.setDatos(new ArrayList<>());
        for (int i = 0; i < this.listaNcolPresupuesto.size(); i++) {
            double sumaColumna = 0;
            
            for (BloqueComponenteDTO fila : bloque.getListaBloqueAuxDTO()) {
                if (!fila.getDatos().isEmpty() && fila.getDatos().size() > i
                        && StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())
                        && fila.getDatos().get(i).getNmColumna() != 0) { // Ignorar elementos donde nmColumna es 0
                    sumaColumna += this.parseImporteDsValor(fila.getDatos().get(i).getDsValor());
                }
            }

             log.info("agregando monto total " + sumaColumna + " en la columna " + i); 

            DatoDTO dto = new DatoDTO();
            //VMM MD-1459 20/05/25: arreglo para que no haga sumatorio de totales en la columna de los conceptos
            if (i != 0) {
                dto.setDsValor(String.valueOf(sumaColumna));
            }else {
                dto.setDsValor("");
            }
            dto.setEsMontoTotal(Boolean.TRUE);
            bloqueComponenteTotalFila.getDatos().add(dto);
        }
        lstFilas.add(bloqueComponenteTotalFila);

        this.lstBloqueComponenteFila = lstFilas;
    }

    /**
     *
     */
    private void obtenerAyudaSolicitadaPresupuesto() {

        try {

            // Obtener registros de ayuda
            List<BloqueComponenteDTO> lstBloqueComponenteDTO = this.bloqueComponenteService
                    .obtenerPorExpediente(this.idExpediente,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA));

            // Puede que no se haya habilitado registro de ayuda para
            // presupuesto
            if (!lstBloqueComponenteDTO.isEmpty()) {

                List<BloqueComponenteDTO> bloqueComponenteImporte = lstBloqueComponenteDTO.stream()
                        .filter(x -> x.getItColumna()).collect(Collectors.toList());

                // Recuperar datos de los bloques componente fila
                bloqueComponenteImporte.get(0)
                        .setDatos(this.datoService.buscarDatosByIdExpAndIdBloComp(
                                this.expedienteBean.getIdExpediente(), bloqueComponenteImporte.get(0).getIdBloqueComponente(), false));

                //VMM 06/05/26 MD-2627: controlamos valores nulos por si el expdte esta en estado Iniciado
                // Ayuda solo tiene un registro, recuperamos el primer valor de la lista
                if (!bloqueComponenteImporte.isEmpty()
                        && !bloqueComponenteImporte.get(0).getDatos().isEmpty()
                        && bloqueComponenteImporte.get(0).getDatos().get(0).getDsValor() != null) {
                    
                    String valorTexto = bloqueComponenteImporte.get(0).getDatos().get(0).getDsValor();
                    this.montoAyudaPresupuesto = this.parseImporteDsValor(valorTexto);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }

    }

    /**
     *
     */
    private void obtenerAyudaSolicitadaActuacion() {

        try {

            // Obtener registros de ayuda
            List<BloqueComponenteDTO> lstBloqueComponenteDTO = this.bloqueComponenteService
                    .obtenerPorExpediente(this.idExpediente,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES));

            // Puede que no se haya habilitado registro de ayuda para
            // presupuesto
            if (!lstBloqueComponenteDTO.isEmpty()) {
                
                Integer idComFinTotImpAyu = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.TOTAL_IMPORTE_AYUDA);
                
                // Recuperamos solo el registro de importe de ayuda solicitada
                Optional<BloqueComponenteDTO> campoAyudaSolicitada = lstBloqueComponenteDTO.stream()
                        .filter(p -> idComFinTotImpAyu.equals(p.getComponenteDTO().getIdComponente()))
                        .findFirst();

                // Recuperar datos de los bloques componente fila
                if (campoAyudaSolicitada.isPresent()) {
                    campoAyudaSolicitada.get()
                            .setDatos(this.datoService.buscarDatosByIdExpAndIdBloComp(
                                    this.expedienteBean.getIdExpediente(), campoAyudaSolicitada.get().getIdBloqueComponente(), false));

                    if (!campoAyudaSolicitada.get().getDatos().isEmpty()
                            && campoAyudaSolicitada.get().getDatos().get(0).getDsValor() != null) {
                        this.montoAyudaActuacion = this.parseImporteDsValor(
                                campoAyudaSolicitada.get().getDatos().get(0).getDsValor());
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     * VMM 03/09/26 MD-3011: si por un guardado duplicado hay más de un
     * ExpedientePresupMonto vigente para la misma anualidad (nmAnualidad), nos quedamos solo con
     * el más reciente (por fcModifFila si existe, si no por fcAltaFila, desempatando por
     * idExpedientePresupMonto), para no sumar/mezclar importes duplicados en "Presupuesto
     * Financiable".
     */
    private List<ExpedientePresupMontoDTO> deduplicarMontosMasRecientePorAnualidad(List<ExpedientePresupMontoDTO> montos) {
        return new ArrayList<>(montos.stream()
                .filter(m -> Objects.nonNull(m.getNmAnualidad()))
                .collect(Collectors.toMap(
                        ExpedientePresupMontoDTO::getNmAnualidad,
                        m -> m,
                        (m1, m2) -> {
                            Date f1 = m1.getFcModifFila() != null ? m1.getFcModifFila() : m1.getFcAltaFila();
                            Date f2 = m2.getFcModifFila() != null ? m2.getFcModifFila() : m2.getFcAltaFila();
                            if (f1 == null) {
                                return m2;
                            }
                            if (f2 == null) {
                                return m1;
                            }
                            int cmp = f1.compareTo(f2);
                            if (cmp != 0) {
                                return cmp > 0 ? m1 : m2;
                            }
                            return (m1.getIdExpedientePresupMonto() != null && m2.getIdExpedientePresupMonto() != null
                                    && m1.getIdExpedientePresupMonto() > m2.getIdExpedientePresupMonto()) ? m1 : m2;
                        },
                        java.util.LinkedHashMap::new))
                .values());
    }

    private void obtenerPresupuestoFinanciable(Integer nmPresup) {
        try {
            
            this.lstPresupuestoFinanciable = this.expedientePresupService.bucarPresupByIdExpAndNmPresup(this.expedienteBean.getIdExpediente(), nmPresup);

            double sumaTotal = 0.0;
            int totalColumnas = 0;
            if (!this.lstPresupuestoFinanciable.isEmpty()) {
                //this.montoAyudaConcedida = this.lstPresupuestoFinanciable.get(0).getNmAyuda();

                Integer idTematica = this.lstPresupuestoFinanciable != null
                        && this.lstPresupuestoFinanciable.get(0) != null
                        && this.lstPresupuestoFinanciable.get(0).getTematicaDTO() != null
                                ? this.lstPresupuestoFinanciable.get(0).getTematicaDTO()
                                        .getIdValor()
                                : null;
                
                //VMM 01/04/26 MD-2639: ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
                FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                        idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
                
                List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
                
               // VMM 14/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
                for (ExpedientePresupDTO p : this.lstPresupuestoFinanciable) {
                    if (p.getTipoFinanciacionDTO() != null) {
                        Integer orden = listaFinancDato.stream()
                            .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null 
                                    && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                            .map(FinanciacionDatoDTO::getNmOrden)
                            // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
                            .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(9999); // Si no tiene orden, al final
                            
                        p.getTipoFinanciacionDTO().setOrdenPartida(orden);
                    }
                }
                this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciable.stream()
                        .sorted((p1, p2) -> informeHelperPdfService.compareByNumOrdenPresup(p1, p2, listaFinancDato))
                        .collect(Collectors.toList());
                
                for (ExpedientePresupDTO presupuesto : this.lstPresupuestoFinanciable) {
                    int index = this.lstPresupuestoFinanciable.indexOf(presupuesto);
                    
                    //MD-2788: seteamos aqui la intensidad de la ayuda para el gestor
                    if (presupuesto.getNmPorcentajeIntensidad() != null) {
                        this.porcentajeIntensidadGestor = presupuesto.getNmPorcentajeIntensidad();
                    }

                    List<ExpedientePresupMontoDTO> montosPresupuesto = this.deduplicarMontosMasRecientePorAnualidad(
                            this.lstPresupuestoFinanciable.get(index).getLstExpedientePresupMontoDTO());
                    this.lstPresupuestoFinanciable.get(index).setLstExpedientePresupMontoDTO(montosPresupuesto);

                    if (totalColumnas == 0) {
                        totalColumnas = montosPresupuesto.size();
                    }

                    double total = montosPresupuesto.stream()
                            .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                    sumaTotal += total;

                    ExpedientePresupMontoDTO montoTotal = new ExpedientePresupMontoDTO();
                    montoTotal.setEsMontoTotal(Boolean.TRUE);
                    montoTotal.setNmImporte(total);

                    this.lstPresupuestoFinanciable.get(index).getLstExpedientePresupMontoDTO()
                            .add(montoTotal);
                }

                int[] nColArray = IntStream.range(0, totalColumnas).toArray();

                List<ExpedientePresupMontoDTO> montosColumnas = new ArrayList<>();
                Arrays.stream(nColArray).forEach(pos -> {
                    double sumaColumna = 0.0;
                    for (ExpedientePresupDTO presupuesto : this.lstPresupuestoFinanciable) {
                        List<ExpedientePresupMontoDTO> montosPresupuesto = presupuesto
                                .getLstExpedientePresupMontoDTO().stream()
                                .filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                                .filter(monto -> pos == monto.getNmAnualidad())
                                .collect(Collectors.toList());

                        sumaColumna += montosPresupuesto.stream()
                                .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                    }
                    ExpedientePresupMontoDTO columnaTotal = new ExpedientePresupMontoDTO();
                    columnaTotal.setEsMontoTotal(Boolean.TRUE);
                    columnaTotal.setNmImporte(sumaColumna);
                    montosColumnas.add(columnaTotal);
                });

                ExpedientePresupDTO filaTotal = new ExpedientePresupDTO();
                filaTotal.setTipoFinanciacionDTO(new CatalogosValoresDTO());
                filaTotal.getTipoFinanciacionDTO().setDsValor("TOTAL");
                filaTotal.setLstExpedientePresupMontoDTO(montosColumnas);

                ExpedientePresupMontoDTO montoColumnaTotal = new ExpedientePresupMontoDTO();
                montoColumnaTotal.setEsMontoTotal(Boolean.TRUE);
                montoColumnaTotal.setNmImporte(sumaTotal);
                filaTotal.getLstExpedientePresupMontoDTO().add(montoColumnaTotal);

                this.lstPresupuestoFinanciable.add(filaTotal);
                
                //MD-2788: ahora calculamos la tabla y montos de ayuda en base al porcentaje de intensidad establecido por el gestor
                double totalPresupFinanciableXIntensidad = porcentajeIntensidadGestor != null
                        ? sumaTotal * (porcentajeIntensidadGestor / 100)
                        : sumaTotal;
                this.recalcularMinAyudaConcedida(totalPresupFinanciableXIntensidad);

                if (nmPresup == 1) {
                    //MD-2731: ojo aqui parece que está cogiendo la primera observación que haya de esa tabla de presup pero ahora pueden ser 
                    //diferentes las observaciones ya que es una por cada concepto o exdte presup sin conceptos
                    //this.tlObservacionPresupuesto = this.lstPresupuestoFinanciable.get(0).getTlObservacion();
                    
                    this.lstPresupuestoFinanciable1 = this.lstPresupuestoFinanciable;
                    
                    //Actualizamos lista para ayudaConcedida
                    this.lstPresupuestoAyudaConcedida = new ArrayList<>();
                    for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable1) { 
                        this.lstPresupuestoAyudaConcedida.add(new ExpedientePresupDTO(presup));
                    }
                    for(ExpedientePresupDTO presupAyudaConcedida : this.lstPresupuestoAyudaConcedida ) {
                        for(ExpedientePresupMontoDTO presupMonto : presupAyudaConcedida.getLstExpedientePresupMontoDTO()) {
                            if(presupMonto.getNmImporte() != null && porcentajeIntensidadGestor != null) {
                                presupMonto.setNmImporte(presupMonto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                            }
                        }
                    }
                } else {
                    //MD-2731: ojo aqui parece que está cogiendo la primera observación que haya de esa tabla de presup pero ahora pueden ser 
                    //diferentes las observaciones ya que es una por cada concepto o exdte presup sin conceptos
                    //this.tlObservacionPresupuesto = this.lstPresupuestoFinanciable.get(1).getTlObservacion();
                    
                    this.lstPresupuestoFinanciable2 = this.lstPresupuestoFinanciable;
                    
                    //Actualizamos lista para ayudaConcedida
                    this.lstPresupuestoAyudaConcedida2 = new ArrayList<>();
                    for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable2) { 
                        this.lstPresupuestoAyudaConcedida2.add(new ExpedientePresupDTO(presup));
                    }
                    for(ExpedientePresupDTO presupAyudaConcedida : this.lstPresupuestoAyudaConcedida2 ) {
                        for(ExpedientePresupMontoDTO presupMonto : presupAyudaConcedida.getLstExpedientePresupMontoDTO()) {
                            if(presupMonto.getNmImporte() != null && porcentajeIntensidadGestor != null) {
                                presupMonto.setNmImporte(presupMonto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                            }
                        }
                    }
                }
            }

        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    private void obtenerPresupuestoProvisional() {
        try {
            
            List<AtlasFilter> filtros = new ArrayList<>();
            List<AtlasSort> ordenamiento = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_PROVISIONAL), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE ,this.expedienteBean.getExpedienteDTO().getIdExpediente() ,  Operador.EQUALS));
            ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PRESUP_HIST, false));
       
            List<ExpedientePresupHistDTO> listaPresupuestoHist1 =  this.expedientePresupHistService.buscar(0, -1, filtros, null);
            
            List<ExpedientePresupDTO>  listaPresupeustos = this.expedientePresupService.bucarPresupByIdExpAndNmPresup(this.expedienteBean.getIdExpediente(), 1);
            Integer tamanio = listaPresupeustos.size();
            
            List<ExpedientePresupHistDTO> listaPresupuestoHist =   listaPresupuestoHist1.stream().limit(tamanio).collect(Collectors.toList());
                
            for (ExpedientePresupHistDTO presupuestoH : listaPresupuestoHist) {                     
                ExpedientePresupDTO presupuesto =  this.expedientePresupService.buscarId(presupuestoH.getIdExpedientePresup());                       
                this.lstPresupuestoFinanciable3.add(presupuesto);
            }
          
            double sumaTotal = 0.0;
            int totalColumnas = 0;
            if (!this.lstPresupuestoFinanciable3.isEmpty()) {
                //this.montoAyudaConcedida = this.lstPresupuestoFinanciable.get(0).getNmAyuda();
                
                //VMM 04/05/26 MD-2723: ordenamos los presupuestos por nmOrden como en financiacion ADM se configuraron
                FinanciacionDTO financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                        this.lstPresupuestoFinanciable3.get(0).getTematicaDTO().getIdValor(), this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
                
                List<FinanciacionDatoDTO> listaFinancDato = financiacionDTO.getListaFinanciacionDatoDTO();
                
                // VMM 18/05/26 MD-2731: Asignamos el orden a cada objeto para que el front sepa agrupar
                for (ExpedientePresupDTO p : this.lstPresupuestoFinanciable3) {
                    if (p.getTipoFinanciacionDTO() != null) {
                        Integer orden = listaFinancDato.stream()
                                .filter(e -> e != null && e.getTipoDTO() != null && e.getTipoDTO().getIdValor() != null 
                                && e.getTipoDTO().getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                                .map(FinanciacionDatoDTO::getNmOrden)
                                // CONTROL DE NULL: Si el nmOrden de ese objeto es nulo, lo ignoramos en el Stream
                                .filter(Objects::nonNull)
                            .findFirst()
                            .orElse(9999); // Si no tiene orden, al final
                            
                        p.getTipoFinanciacionDTO().setOrdenPartida(orden);
                    }
                }
                
                this.lstPresupuestoFinanciable3 = this.lstPresupuestoFinanciable3.stream()
                        .sorted((p1, p2) -> informeHelperPdfService.compareByNumOrdenPresup(p1, p2, listaFinancDato))
                        .collect(Collectors.toList());

                for (ExpedientePresupDTO presupuesto : this.lstPresupuestoFinanciable3) {
                    int index = this.lstPresupuestoFinanciable3.indexOf(presupuesto);

                    List<ExpedientePresupMontoDTO> montosPresupuesto = this.lstPresupuestoFinanciable3
                            .get(index).getLstExpedientePresupMontoDTO();
                    if (totalColumnas == 0) {
                        totalColumnas = montosPresupuesto.size();
                    }

                    double total = montosPresupuesto.stream()
                            .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                    sumaTotal += total;

                    ExpedientePresupMontoDTO montoTotal = new ExpedientePresupMontoDTO();
                    montoTotal.setEsMontoTotal(Boolean.TRUE);
                    montoTotal.setNmImporte(total);

                    this.lstPresupuestoFinanciable3.get(index).getLstExpedientePresupMontoDTO()
                            .add(montoTotal);
                }

                int[] nColArray = IntStream.range(0, totalColumnas).toArray();

                List<ExpedientePresupMontoDTO> montosColumnas = new ArrayList<>();
                Arrays.stream(nColArray).forEach(pos -> {
                    double sumaColumna = 0.0;
                    for (ExpedientePresupDTO presupuesto : this.lstPresupuestoFinanciable3) {
                        List<ExpedientePresupMontoDTO> montosPresupuesto = presupuesto
                                .getLstExpedientePresupMontoDTO().stream()
                                .filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                                .filter(monto -> pos == monto.getNmAnualidad())
                                .collect(Collectors.toList());

                        sumaColumna += montosPresupuesto.stream()
                                .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                    }
                    ExpedientePresupMontoDTO columnaTotal = new ExpedientePresupMontoDTO();
                    columnaTotal.setEsMontoTotal(Boolean.TRUE);
                    columnaTotal.setNmImporte(sumaColumna);
                    montosColumnas.add(columnaTotal);
                });

                ExpedientePresupDTO filaTotal = new ExpedientePresupDTO();
                filaTotal.setTipoFinanciacionDTO(new CatalogosValoresDTO());
                filaTotal.getTipoFinanciacionDTO().setDsValor("TOTAL");
                filaTotal.setLstExpedientePresupMontoDTO(montosColumnas);

                ExpedientePresupMontoDTO montoColumnaTotal = new ExpedientePresupMontoDTO();
                montoColumnaTotal.setEsMontoTotal(Boolean.TRUE);
                montoColumnaTotal.setNmImporte(sumaTotal);
                filaTotal.getLstExpedientePresupMontoDTO().add(montoColumnaTotal);

                this.lstPresupuestoFinanciable3.add(filaTotal);
                
                if(porcentajeIntensidadGestor != null) {
                    this.montoAyudaConcedida = sumaTotal * (porcentajeIntensidadGestor / 100);
                }else {
                    this.montoAyudaConcedida = sumaTotal;
                }
                
                //cargamos el presupuesto provisional del historico para la tabla
                                      
                //Actualizamos lista para ayudaConcedida
                this.lstPresupuestoAyudaConcedida3 = new ArrayList<>();
                for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable3) { 
                    this.lstPresupuestoAyudaConcedida3.add(new ExpedientePresupDTO(presup));
                }
                for(ExpedientePresupDTO presupAyudaConcedida : this.lstPresupuestoAyudaConcedida3 ) {
                    for(ExpedientePresupMontoDTO presupMonto : presupAyudaConcedida.getLstExpedientePresupMontoDTO()) {
                        if(presupMonto.getNmImporte() != null && porcentajeIntensidadGestor != null) {
                            presupMonto.setNmImporte(presupMonto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                        }
                    }
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    public void comprobarIntensidad() {
        if (this.porcentajeIntensidadAyuda == null && this.porcentajeIntensidadAyudaStr != null
                && this.porcentajeIntensidadGestor == null) {
            this.esformatoErroneoIntensidad = Boolean.TRUE;
        } else {
            this.cargarDatosPresupuesto(this.porcentajeIntensidadAyuda);
        }
    }
    
    
    /**
     * Metodo para copiar los datos de presupuestos presentados a presupuestos
     * financiables
     */
    public void cargarDatosPresupuesto(Double porcentajeIntensidadAyuda) {
        try {

            Integer nmPresup = 1;

            this.lstPresupuestoFinanciable = new ArrayList<>();

            for (BloqueDTO bloques : this.lstBloque) {
                Integer idTematica = null;
                if (bloques.getIdTematica() != null
                        && bloques.getIdTematica().getIdValor() != null) {
                    idTematica = bloques.getIdTematica().getIdValor();
                } else if (bloques.getIdTematicaPadre() != null
                        && bloques.getIdTematicaPadre().getIdValor() != null) {
                    idTematica = bloques.getIdTematicaPadre().getIdValor();
                }
                List<BloqueComponenteDTO> bloquesSinTotal = bloques.getListaBloqueAuxDTO().stream()
                        .filter(bloque -> bloque.getTipoFinanciacionDTO() != null
                                && Objects.nonNull(bloque.getTipoFinanciacionDTO().getIdValor()))
                        .collect(Collectors.toList());
                
                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                    this.expedientePresupService.guardarFinanciacionConcepto(bloquesSinTotal,
                            this.montoAyudaPresupuesto, this.montoAyudaSolicitada, nmPresup,
                            this.expedienteBean.getIdExpediente(), idTematica, porcentajeIntensidadAyuda);
                }else {
                    this.expedientePresupService.guardarFinanciacion(bloquesSinTotal,
                            this.montoAyudaPresupuesto, this.montoAyudaSolicitada, nmPresup,
                            this.expedienteBean.getIdExpediente(), idTematica, porcentajeIntensidadAyuda);
                }
                
                nmPresup++;
            }
            
             //VMM 28/07/25 MD-1687: Cuando se carguen los datos a las tablas de presupuesto el estado será:
            //“Pendiente revisión financiación” (140)
            this.expedienteService.cambiarEstadoExpFinan(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION, 
                    this.expedienteBean.getExpedienteDTO());

            this.obtenerPresupuestoFinanciable(1);
            this.obtenerPresupuestoFinanciable(2);
             
            this.validarEstilo();

            this.esconderBotonCargarPresup();
            this.esconderBotonFinalizarRevision();
            
            this.ocultarAyuda = Objects.isNull(this.montoAyudaConcedida) ? Boolean.TRUE
                    : Boolean.FALSE;
                       
            PrimeFaces.current().ajax().update(this.formularioFinanciacion);
            PrimeFaces.current().ajax().update(this.solapaFinanciacion);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }
    
    public Boolean cargaDatos() {
        
        this.esconderBotonCargarPresup();
        this.esconderBotonFinalizarRevision();
        return Boolean.FALSE;
        
    }
    
    
    /**
     * Metodo para finalizar la revisión de la financiación de los presupuestos
     * financiables
     */
    public void finalizarRevisionFinan() {
        try {
            
            Boolean noHayErrores = Boolean.TRUE;

            noHayErrores = this.validarMaximoMinimo(noHayErrores);

            // MD-2788: validacion por fila: un mensaje de error por cada fila con importe modificado sin observacion
            if (this.notificarFilasPendientesObservacion()) {
                //mostramos el mensaje o lo ocultamos en base al guardado de observaciones
                //PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);
                return;
            }

            // VMM 06/10/26 MD-3113: no se finaliza la revision con conceptos de descripcion vacia o repetidos
            if (this.notificarConceptosInvalidos()) {
                return;
            }

            //MD-2803: Guardamos concepto y observaciones de todas las filas por si el usuario tabuló
            // sin disparar los listeners intermedios de guardarConceptoObservac
            List<List<ExpedientePresupDTO>> todasLasListas = Arrays.asList(
                    this.lstPresupuestoFinanciable1, this.lstPresupuestoFinanciable2);

            //MD-2947: comprobamos que no haya quedado ningun importe nulo
            boolean hayImporteNulo = todasLasListas.stream()
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .map(ExpedientePresupDTO::getLstExpedientePresupMontoDTO)
                    .filter(Objects::nonNull)
                    .flatMap(List::stream)
                    .filter(Objects::nonNull)
                    .anyMatch(montoDTO -> Objects.isNull(montoDTO.getNmImporte()));

            if (hayImporteNulo) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_ERROR_IMPORTE_NULO);
                return;
            }
            
            if (Boolean.TRUE.equals(noHayErrores) && Boolean.FALSE.equals(validarErrorPresupFinalizar())) {

                for (List<ExpedientePresupDTO> lista : todasLasListas) {
                    if (lista != null) {
                        for (ExpedientePresupDTO presup : lista) {
                            if (presup.getIdExpedientePresup() != null) {
                                if (presup.getTlObservacion() != null && StringUtils.isNotBlank(presup.getTlObservacion())) {
                                    presup.setPendienteObservacion(Boolean.FALSE);
                                }
                                this.expedientePresupService.actualizarConceptoObservac(presup);
                            }
                        }
                    }
                }

                //VMM 28/07/25 MD-1687: Cuando el gestor termine el proceso de revisión el estado será
                //”Revisión Financiación OK” (141), una vez en este estado no se permite modificaciones.
                ExpedienteDTO dto =  this.expedienteService.cambiarEstadoExpFinan(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK, this.expedienteBean.getExpedienteDTO());   
                
                if (dto != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)
                        .equals(dto.getEstadoRevFinanDTO().getIdValor())) {
                    
                    // Calcular si todos los tramites finalizados
                    dto = this.expedienteService.calcularTramitesFinalizados(dto);
                    
                    this.expedienteBean.setExpedienteDTO(dto);
                    
                    this.validarEstilo();
        
                    this.esconderBotonFinalizarRevision();
                    
                    //VMM MD-2234 26/01/26: comprobamos si hay que pasar a modo consulta algunos campos al finalizar ok la revision finan
                    this.esModoConsulta();
                               
                    PrimeFaces.current().ajax().update(this.campoEstadoExpediente);
                    PrimeFaces.current().ajax().update(this.formularioFinanciacion);
                    PrimeFaces.current().ajax().update(this.solapaFinanciacion);
                    
                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_FINANCIACION_FINALIZAR_OK);

                } else {
                    AtlasMessageUtils.addErrorMessageFromBundle(
                            ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_FINANCIACION_FINALIZAR_KO);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.REVISION_EXPEDIENTE_ESTADO_FINANCIACION_FINALIZAR_KO);
        }
    }


    private Boolean validarErrorPresupFinalizar() throws ServiceException {
        Boolean hayErrores = Boolean.FALSE;
        Object[] msgImporte = null;     
        String tabla = "";
        
        //VMM 11/08/25 MD-1687: se supone que solo tenemos que validar los presupuestos si el gestor no ha hecho modificaciones
        //porque en tal caso hay que dar a editar celda y ahi ya lo valida según el modo.
        //y solo si el flag de IT_MOSTRAR_AYUDA = N que es cuando no se valida en SAII_SOL
        //para grabar en BBDD los cambios de los importes del gestor hay que dar al botón Guardar
        if (this.lstTablasFinan == null || this.lstTablasFinan.isEmpty()) {
            //VMM 02/06/26 MD-2783: aqui filtrar solo por tablas de tipo PP, estaba cogiendo las de tipo AS, AC...
            this.lstTablasFinan = this.financiacionService.buscarTablasFinanByIdConvocAndTipo(
                    this.expedienteBean.getIdConvocatoria(), Constantes.Formatos.PARTIDAS_PRESUPUESTARIAS);
        }                            
        
        if (this.lstTablasFinan != null && !this.lstTablasFinan.isEmpty()) { 
                        
            boolean esMostrarAyudaT1 = this.lstTablasFinan.get(0).getItMostrarAyuda() != null && 
                    this.lstTablasFinan.get(0).getItMostrarAyuda().equals(Constantes.AfirmacionNegacion.S);
            
            boolean esMostrarAyudaT2 = false;
            if (this.lstTablasFinan.size() == 2) {
                esMostrarAyudaT2 = this.lstTablasFinan.get(1).getItMostrarAyuda() != null && 
                        this.lstTablasFinan.get(1).getItMostrarAyuda().equals(Constantes.AfirmacionNegacion.S);
            }
            
            /*ExpedientePresupDTO tablaExpPresup1 = null;
            ExpedientePresupDTO tablaExpPresup2 = null;
            //obtenemos la tabla correspondiente de expediente_presup cruzando por la tematica 
            if (tablaPresupMaxFinanAdm.getTematicaDTO().getIdValor().
                    equals(this.lstPresupuestoFinanciable1.get(0).getTematicaDTO().getIdValor())) {
                //si la tematica del primer registro coincide ya sabemos que es esa tabla de presup del expediente
                tablaExpPresup1 = this.lstPresupuestoFinanciable1.get(0);
            }else if (tablaPresupMaxFinanAdm.getTematicaDTO().getIdValor().
                    equals(this.lstPresupuestoFinanciable2.get(0).getTematicaDTO().getIdValor())) {{
                tablaExpPresup2 = this.lstPresupuestoFinanciable2.get(0);
            }*/
                                               
            //si no se han realizado las validaciones en SAII_SOL y el gestor no ha hecho cambios, hay que validar todo
            //si el gestor hace cambios o edita y flag = N, se supone que el boton de edicion ya lo valida en los 3 modos
            ConvocatoriaDTO convocatoria = this.convocatoriasBean.getConvocatoriaDTO();
            String impMaxExp = convocatoria.getItImporteMaxExpdte();
            String impMaxAnual = convocatoria.getItImporteMaxAnual();
            
            //quitamos la fila de los totales en tabla1
            //OJOOO si no esta copiando bien la lista de montos que hay dentro usar el constructor de copia
            List<ExpedientePresupDTO> lstPresupFinanAux1 = new ArrayList<>();
            if (this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty()) {
                    lstPresupFinanAux1 = this.lstPresupuestoFinanciable1.stream().
                    filter(pp -> pp.getTipoFinanciacionDTO() != null && Objects.nonNull(pp.getTipoFinanciacionDTO().getIdValor()))
                    .collect(Collectors.toList());
            }
            
            //quitamos la fila de los totales en tabla2 (si existe)
            //OJOOO si no esta copiando bien la lista de montos que hay dentro usar el constructor de copia
            List<ExpedientePresupDTO> lstPresupFinanAux2 = new ArrayList<>();
            if (this.lstPresupuestoFinanciable2 != null && !this.lstPresupuestoFinanciable2.isEmpty()) {
                    lstPresupFinanAux2 = this.lstPresupuestoFinanciable2.stream().
                    filter(pp -> pp.getTipoFinanciacionDTO() != null && Objects.nonNull(pp.getTipoFinanciacionDTO().getIdValor()))
                    .collect(Collectors.toList());
            }
            
            //CASO 1: MAX POR PARTIDAS PRESUP
            if (impMaxExp != null && impMaxAnual != null) {
                if (Constantes.AfirmacionNegacion.N.equals(impMaxExp) && 
                        Constantes.AfirmacionNegacion.N.equals(impMaxAnual)) {
                    
                    //comparar monto por monto de cada celda de cada tabla que no supere el monto max de saii_finan                                            
                    //usar el edicionceldaPresup para comparar cada casilla de presupuesto con su maximo de financiacion ADMIN          
                    if (!esMostrarAyudaT1) {//si no se ha validado lo de la tabla 1, lo validamos
                        for (ExpedientePresupDTO expPresup: lstPresupFinanAux1) {
                            for (int anualidad: this.listaAnualidades) {  //lista anualidades empieza en 1
                                int nCol = anualidad - 1;
                                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                    this.edicionCeldaPresupuesto(expPresup, anualidad, 1);
                                }else {
                                    this.edicionCeldaPresupuesto(expPresup, nCol, 1);
                                }
                                
                                //comprobamos si ha habido error tras cada celda y en tal caso devolvemos error al metodo ppal
                                if (Boolean.TRUE.equals(this.tieneErrores)) {
                                    hayErrores = Boolean.TRUE;
                                    return hayErrores;
                                }
                            }
                        }
                    }
                    //no seguimos revisando la otra tabla hasta que se quiten los errores de la primera tabla
                    if (this.lstTablasFinan.size() == 2 && !esMostrarAyudaT2 && Boolean.FALSE.equals(hayErrores)) {//si no se ha validado lo de la tabla 2, lo validamos
                        for (ExpedientePresupDTO expPresup: lstPresupFinanAux2) {
                            for (int anualidad: this.listaAnualidades) {
                                int nCol = anualidad - 1;
                                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                    this.edicionCeldaPresupuesto(expPresup, anualidad, 2);
                                }else {
                                    this.edicionCeldaPresupuesto(expPresup, nCol, 2);
                                }
                                
                                //comprobamos si ha habido errores y en tal caso devolvemos error al metodo ppal
                                if (Boolean.TRUE.equals(this.tieneErrores)) {
                                    hayErrores = Boolean.TRUE;
                                    return hayErrores;
                                }
                            }
                        }
                    }
                    
                    //ponemos este indicador a false para que no salga el mensaje de guardar observaciones al finalizar ya que si hay errores 
                    //como si no los hay, se deben solventar/editar después de esta comprobación final, no es como cuando se edita una celda aislada
                    this.pendienteGuardarObservacion = Boolean.FALSE;
                    
                   //CASO 2: MAX POR EXPDTE/ACTOR
                } else if (Constantes.AfirmacionNegacion.S.equals(impMaxExp) &&
                        Constantes.AfirmacionNegacion.N.equals(impMaxAnual)) {
                    
                    //comparar el total o suma de todos los importes de cada tabla o exp (o suma de ambas tablas si mismo actor)
                    //con el importe max correspondiente por expdte de saii_financiacion
                    if (!esMostrarAyudaT1 || !esMostrarAyudaT2) {//si no se ha validado lo de la tabla 1 o 2, lo validamos
                        
                        //calculamos los importes max por actor/expdte
                        Double importeMaxActorTabla1 = this.lstTablasFinan.get(0).getNmImporteMaxActor();
                        ConvocatoriaActDTO actorTabla1 = this.lstTablasFinan.get(0).getIdActor();
                        Double importeMinActorTabla1 = this.lstTablasFinan.get(0).getNmImporteMinActor();
                        Double importeMaxActorTabla2 = null;
                        ConvocatoriaActDTO actorTabla2 = null;
                        Double importeMinActorTabla2 = null;
                        Double importeMaxTotalActor = null;
                        Double importeMinTotalActor = null;
                        Double sumatorioTotalTablas = 0D;
                        Double sumatorioTabla1 = 0D;
                        Double sumatorioTabla2 = 0D;
                                                    
                        //calcular sumatorio de tabla1                                        
                         for (ExpedientePresupDTO tablaPP: lstPresupFinanAux1) {
                             //quitamos la fila de los totales
                             //si es una convocatoria con conceptos tenemos que ignorar la columna o anualidad 0 que es donde van los conceptos
                             List<ExpedientePresupMontoDTO> lstMontosExp = null;
                             if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                 lstMontosExp = tablaPP.getLstExpedientePresupMontoDTO()
                                     .stream().filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                             && !monto.getNmAnualidad().equals(0))
                                     .collect(Collectors.toList());
                             }else {
                                 lstMontosExp = tablaPP.getLstExpedientePresupMontoDTO()
                                         .stream().filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                                         .collect(Collectors.toList());
                             }
                             
                            for (ExpedientePresupMontoDTO monto: lstMontosExp) {
                                sumatorioTabla1 = sumatorioTabla1 + (monto.getNmImporte() != null ? monto.getNmImporte() : 0.0);
                            }
                        }
                        
                        //en caso de tener 2 tablas presup
                        if (this.lstTablasFinan.size() == 2 && !esMostrarAyudaT2) {  
                            importeMaxActorTabla2 = this.lstTablasFinan.get(1).getNmImporteMaxActor();
                            actorTabla2 = this.lstTablasFinan.get(1).getIdActor();
                            importeMinActorTabla2 = this.lstTablasFinan.get(1).getNmImporteMinActor();
                             //calcular sumatorio de tabla2
                             for (ExpedientePresupDTO tablaPP: lstPresupFinanAux2) {
                                 //quitamos la fila de los totales
                                 //si es una convocatoria con conceptos tenemos que ignorar la columna o anualidad 0 que es donde van los conceptos
                                 List<ExpedientePresupMontoDTO> lstMontosExp = null;
                                 if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                     lstMontosExp = tablaPP.getLstExpedientePresupMontoDTO()
                                             .stream().filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                                     && !monto.getNmAnualidad().equals(0))
                                             .collect(Collectors.toList());
                                 }else {
                                     lstMontosExp = tablaPP.getLstExpedientePresupMontoDTO()
                                             .stream().filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                                             .collect(Collectors.toList());
                                 }
                                 
                                for (ExpedientePresupMontoDTO monto: lstMontosExp) {
                                    sumatorioTabla2 = sumatorioTabla2 + (monto.getNmImporte() != null ? monto.getNmImporte() : 0.0);
                                }
                            }
                            
                            //comprobamos si el actor de la tabla 1 coincide con el de la tabla 2, en cuyo caso se suman los maximos
                            if ( actorTabla1 != null && actorTabla1.getIdActor() != null && actorTabla2 != null
                                    && actorTabla1.getIdActor().equals(actorTabla2.getIdActor())) {
                                if (importeMaxActorTabla1 != null && importeMaxActorTabla2 != null) {
                                    importeMaxTotalActor = importeMaxActorTabla1 + importeMaxActorTabla2;
                                }
                                sumatorioTotalTablas = sumatorioTabla1 + sumatorioTabla2;
                                if (importeMinActorTabla1 != null && importeMinActorTabla2 != null) {
                                    importeMinTotalActor = importeMinActorTabla1 + importeMinActorTabla2;
                                }

                                if (importeMaxTotalActor != null && porcentajeIntensidadGestor != null && (sumatorioTotalTablas * (porcentajeIntensidadGestor / 100)) > importeMaxTotalActor && !esMostrarAyudaT1) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " ambas tablas ";
                                    msgImporte = new Object[] {tabla, importeMaxTotalActor};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }

                                //CAMBIAR MENSAJES DE MINIMOS
                                if(importeMinTotalActor != null && porcentajeIntensidadGestor != null && (sumatorioTotalTablas * (porcentajeIntensidadGestor / 100))  < importeMinTotalActor && !esMostrarAyudaT1) {
                                    hayErrores = Boolean.TRUE;
                                    tabla = " ambas tablas ";
                                    msgImporte = new Object[] {tabla, importeMinTotalActor};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MINIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }
                                
                            }else {
                                //cada tabla tiene un actor distinto y habrá que comparar contra cada uno de los sumatorios
                                if ( importeMaxActorTabla1 != null && porcentajeIntensidadGestor != null && ((sumatorioTabla1 * (porcentajeIntensidadGestor / 100)) > importeMaxActorTabla1 && !esMostrarAyudaT1)) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " la tabla 1 ";
                                    msgImporte = new Object[] {tabla, importeMaxActorTabla1};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }
                                
                                if ( importeMinActorTabla1 != null && porcentajeIntensidadGestor != null && ((sumatorioTabla1 * (porcentajeIntensidadGestor / 100)) < importeMinActorTabla1 && !esMostrarAyudaT1)) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " la tabla 1 ";
                                    msgImporte = new Object[] {tabla, importeMinActorTabla1};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MINIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }
                                
                                
                                
                                if (  importeMaxActorTabla2 != null && porcentajeIntensidadGestor != null && ((sumatorioTabla2 * (porcentajeIntensidadGestor / 100)) > importeMaxActorTabla2)) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " la tabla 2 ";
                                    msgImporte = new Object[] {tabla, importeMaxActorTabla2};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }
                                
                                if (  importeMinActorTabla2 != null && porcentajeIntensidadGestor != null && ((sumatorioTabla2 * (porcentajeIntensidadGestor / 100)) < importeMinActorTabla2)) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " la tabla 2 ";
                                    msgImporte = new Object[] {tabla, importeMinActorTabla2};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MINIMO_PRESUP_EXPDTE_FINALIZAR,
                                            msgImporte);
                                }
                                
                                
                                
                                
                            }
                        }else if (this.lstTablasFinan.size() == 1 &&  importeMaxActorTabla1 != null && porcentajeIntensidadGestor != null && ((sumatorioTabla1 * (porcentajeIntensidadGestor / 100)) > importeMaxActorTabla1 && !esMostrarAyudaT1)) {
                            //error
                            hayErrores = Boolean.TRUE;
                            tabla = " la tabla 1 ";
                            msgImporte = new Object[] {tabla, importeMaxActorTabla1};
                            AtlasMessageUtils.addErrorMessageFromBundleParams(
                                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE_FINALIZAR,
                                    msgImporte);
                        }
                    }
                   //CASO 3: MAX POR ANUALIDADES
                } else if (Constantes.AfirmacionNegacion.N.equals(impMaxExp) && 
                        Constantes.AfirmacionNegacion.S.equals(impMaxAnual)) {
                    
                    //primero saber cuantas anualidades hay en las tablas (deben ser las mismas)
                    //comparar el total de la suma por columnas de cada anualidad con el max importe anual de saii_finan
                    if (!esMostrarAyudaT1 || !esMostrarAyudaT2) {//si no se ha validado lo de la tabla 1 o 2, lo validamos
                       
                        for (int anualidad: this.listaAnualidades) {  //lista anualidades empieza en 1
                            int nmAnualidad = anualidad - 1;
                            //en caso de tener 2 tablas presup
                            Double importeMaxAnualT2 = null;
                            Double sumaAnualidadT1 = 0D;
                            Double sumaAnualidadT2 = 0D;
                            
                            //las anualidades en finan dato empiezan en 1
                            Double importeMaxAnualT1 = this.lstTablasFinan.get(0).getListaFinanciacionDatoDTO().stream()
                                                        .filter(dato -> Objects.nonNull(dato.getNmAnualidad())
                                                                && dato.getNmAnualidad().equals(anualidad))
                                                        .findFirst().orElse(new FinanciacionDatoDTO()).getNmImporteMaxAnual();
                            
                            //calcular el sumatorio de cada anualidad por columna (tabla 1)
                            for (ExpedientePresupDTO partidaPres: lstPresupFinanAux1) {
                                                                            
                                //las anualidades en exp presup monto empiezan en 0
                                //quitamos la fila de los totales
                                //si tiene conceptos se mete una anualidad 0 y empiezan en 1 los valores de importes por anualidades
                                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                    Double importeAnualidadT1 = partidaPres.getLstExpedientePresupMontoDTO().stream()
                                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                                    && monto.getNmAnualidad().equals(anualidad))
                                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                                    sumaAnualidadT1 = sumaAnualidadT1 + (importeAnualidadT1 != null ? importeAnualidadT1 : 0.0);
                                }else {
                                    Double importeAnualidadT1 = partidaPres.getLstExpedientePresupMontoDTO().stream()
                                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                                    && monto.getNmAnualidad().equals(nmAnualidad))
                                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                                    sumaAnualidadT1 = sumaAnualidadT1 + (importeAnualidadT1 != null ? importeAnualidadT1 : 0.0);
                                }
                            }
                            
                            if (importeMaxAnualT1 != null && porcentajeIntensidadGestor != null && (sumaAnualidadT1 * (porcentajeIntensidadGestor / 100)) > importeMaxAnualT1 && !esMostrarAyudaT1) {
                                //error
                                hayErrores = Boolean.TRUE;
                                tabla = " la tabla 1 ";
                                msgImporte = new Object[] {tabla, anualidad, importeMaxAnualT1};
                                AtlasMessageUtils.addErrorMessageFromBundleParams(
                                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_ANUALIDAD_FINALIZAR,
                                        msgImporte);
                            }
                            
                            if (this.lstTablasFinan.size() == 2 && !esMostrarAyudaT2) {
                                importeMaxAnualT2 = this.lstTablasFinan.get(1).getListaFinanciacionDatoDTO().stream()
                                                    .filter(dato -> Objects.nonNull(dato.getNmAnualidad())
                                                            && dato.getNmAnualidad().equals(anualidad))
                                                    .findFirst().orElse(new FinanciacionDatoDTO()).getNmImporteMaxAnual();
                                
                                //calcular el sumatorio de cada anualidad por columna (tabla 2)
                                for (ExpedientePresupDTO partidaPres: lstPresupFinanAux2) {
                                    
                                    //las anualidades en exp presup monto empiezan en 0
                                    //quitamos la fila de los totales 
                                    //si tiene conceptos se mete una anualidad 0 y empiezan en 1 los valores de importes por anualidades
                                    if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                                        Double importeAnualidadT2 = partidaPres.getLstExpedientePresupMontoDTO().stream()
                                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                                    && monto.getNmAnualidad().equals(anualidad))
                                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                                        sumaAnualidadT2 = sumaAnualidadT2 + (importeAnualidadT2 != null ? importeAnualidadT2 : 0.0);
                                    }else {
                                        Double importeAnualidadT2 = partidaPres.getLstExpedientePresupMontoDTO().stream()
                                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                                    && monto.getNmAnualidad().equals(nmAnualidad))
                                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                                        sumaAnualidadT2 = sumaAnualidadT2 + (importeAnualidadT2 != null ? importeAnualidadT2 : 0.0);
                                    }
                                }
                                                                    
                                if (importeMaxAnualT2 != null && porcentajeIntensidadGestor != null && (sumaAnualidadT2 * (porcentajeIntensidadGestor / 100)) > importeMaxAnualT2) {
                                    //error
                                    hayErrores = Boolean.TRUE;
                                    tabla = " la tabla 2 ";
                                    msgImporte = new Object[] {tabla, anualidad, importeMaxAnualT2};
                                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_ANUALIDAD_FINALIZAR,
                                            msgImporte);
                                }
                            }
                        }
                    }
                }
            }else {
                //si se han validado las cosas en SAII_SOL no hace falta nada, si el gestor hace cambios, se habran validado al editar celda
                //o si el gestor no ha hecho cambios en TRAM y el flag es S, ya vendran las cosas validadas de SAII_SOL
            }
        }     
        
        return hayErrores;
    }


    private void validarPresupFinanEditar(int nCol, int nmPresup, ExpedientePresupDTO presupuestoFinanciable,
            ExpedientePresupMontoDTO presupMonto, double importeMaximo, double totalFinalFila,
            FinanciacionDTO tablaPresupMaxFinanAdm) throws ServiceException {

        ConvocatoriaDTO convocatoria = this.convocatoriasBean.getConvocatoriaDTO();
        String impMaxExp = convocatoria.getItImporteMaxExpdte();
        String impMaxAnual = convocatoria.getItImporteMaxAnual();
        
        if (impMaxExp != null && impMaxAnual != null) {
            if (Constantes.AfirmacionNegacion.N.equals(impMaxExp) && 
                    Constantes.AfirmacionNegacion.N.equals(impMaxAnual)) {
                validarImporteMaxPorPP(nCol, nmPresup, presupuestoFinanciable, presupMonto, importeMaximo, totalFinalFila);
                
            } else if (Constantes.AfirmacionNegacion.S.equals(impMaxExp) &&
                    Constantes.AfirmacionNegacion.N.equals(impMaxAnual)) {
                //cuando editan una celda en max por Expdte (y hay varios valores que sobrepasan el max si han venido de saii_sol sin validar) 
                //no validar los importes hasta el finalizar porque si la suma de importes del expdte se sobrepasa me rechaza el valor
                //continuamente y nunca puedo llegar a arreglar los valores de la tabla porque con error me los rechaza, bucle sin fin
                validarImporteMaxPorExp(nCol, nmPresup, presupuestoFinanciable, presupMonto, totalFinalFila, tablaPresupMaxFinanAdm, importeMaximo);
                
            } else if (Constantes.AfirmacionNegacion.N.equals(impMaxExp) && 
                    Constantes.AfirmacionNegacion.S.equals(impMaxAnual)) {
                //cuando editan una celda en max por Anualidad (y hay varios valores que sobrepasan el max si han venido de saii_sol sin validar) 
                //no validar los importes hasta el finalizar porque si la suma de la anualidad se sobrepasa me rechaza el valor
                //continuamente y nunca puedo llegar a arreglar los valores de la columna porque con error me los rechaza, bucle sin fin
                validaImporteMaxPorAnual(nCol, nmPresup, presupuestoFinanciable, presupMonto, totalFinalFila,
                        tablaPresupMaxFinanAdm, importeMaximo);
            }
        }
    }

    
    private void validarImporteMaxPorPP(int nCol, int nmPresup, ExpedientePresupDTO presupuestoFinanciable, ExpedientePresupMontoDTO presupMonto,
            double importeMaximo, double totalFinalFila) throws ServiceException {

        DecimalFormat nf = new DecimalFormat("0.00");
        String montoTexto = nf.format(importeMaximo).replace(".", ",");

        log.info("Monto capturado: " + presupMonto.getNmImporte());
        log.info("Importe maximo: " + montoTexto);
        
        this.tieneErrores = presupMonto.getNmImporte() != null && presupMonto.getNmImporte() > importeMaximo;

        if (Boolean.TRUE.equals(this.tieneErrores)) {

            String tabla = " de la tabla de presupuesto " + nmPresup;

            Object[] msgImporte = null;
            if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad(), montoTexto + " €", tabla };
            }else {
              //OJO que parece que cuando es sin conceptos hay que sumar uno a la anualidad!!!!!!!!!!!!!!!!!!
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad() + 1, montoTexto + " €", tabla };
            }

            int indiceTotalfila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                    .size() - 1;
            Double montoTotalInicialObj = presupuestoFinanciable
                    .getLstExpedientePresupMontoDTO().get(indiceTotalfila).getNmImporte();
            double montoTotalInicial = montoTotalInicialObj != null ? montoTotalInicialObj : 0.0;

            double montoInicial = (presupMonto.getNmImporte() != null ? presupMonto.getNmImporte() : 0.0)
                    - (totalFinalFila - montoTotalInicial);

            presupMonto.setNmImporte(montoInicial);

            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP,
                    msgImporte);
        }else {

            calcularMontoTotalAyudaConIntensidad(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);

            //MD-2731: marcamos la fila como pendiente de observacion si no la tiene informada
            if (Objects.isNull(presupuestoFinanciable.getTlObservacion()) || StringUtils.isBlank(presupuestoFinanciable.getTlObservacion())) {
                presupuestoFinanciable.setPendienteObservacion(Boolean.TRUE);
            }
        }
        
        //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable);
        //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable2);
        PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan1)");
        PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan2)");
        PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);
        PrimeFaces.current().ajax().update(this.inputMontoAyudaConcedida);
    }
    

    private void validaImporteMaxPorAnual(int nCol, int nmPresup, ExpedientePresupDTO presupuestoFinanciable, ExpedientePresupMontoDTO presupMonto,
           double totalFinalFila, FinanciacionDTO tablaPresupMaxFinanAdm, Double importeMaximoFijo) throws ServiceException {

        String montoAnualTexto = "";
        DecimalFormat nf = new DecimalFormat("0.00");
        log.info("Monto capturado: " + presupMonto.getNmImporte());
        
        List<FinanciacionDatoDTO> lstfinanDatoPresupMaxAdm = tablaPresupMaxFinanAdm.getListaFinanciacionDatoDTO();
        
        //comprobamos primero si se trata de una celda de importe fijo en cuyo caso no miramos el total por exp sino el total de ese imp fijo
        FinanciacionDatoDTO datoFinan = lstfinanDatoPresupMaxAdm.stream()
                .filter(dato -> Objects.nonNull(dato.getNmImporteMax())
                    && dato.getTipoDTO().getIdValor().equals(presupuestoFinanciable.getTipoFinanciacionDTO().getIdValor())
                    && dato.getNmAnualidad().equals(nCol)
                    && Constantes.AfirmacionNegacion.S.equals(dato.getItImporteFijo())
                    && dato.getIdFinanciacion().equals(tablaPresupMaxFinanAdm.getIdFinanciacion()))
                .findFirst()
                .orElse(null);
 
        if (datoFinan != null){
            log.info("Importe maximo fijo: " + importeMaximoFijo);

            if (presupMonto.getNmImporte() != null && importeMaximoFijo != null && presupMonto.getNmImporte() > importeMaximoFijo) {
                montoAnualTexto = nf.format(importeMaximoFijo).replace(".", ",");
                this.tieneErrores = Boolean.TRUE;
            }
        }else {

            //de la lista de montos maximos de financiacion (ADM), nos tenemos que quedar con el de la anualidad que estoy editando
            //VMM: puede no encontrarse coincidencia o venir sin nmImporteMaxAnual informado, se controla con el default a 0.0
            Double importeMaxAnualidadObj = lstfinanDatoPresupMaxAdm.stream()
                    .filter(fila -> fila.getTipoDTO() != null && fila.getTipoDTO().getDsValor() != null
                            && presupuestoFinanciable.getTipoFinanciacionDTO() != null
                            && fila.getTipoDTO().getDsValor().equals(
                                        presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor())  //que sea misma Partida Presup
                            && Objects.equals(fila.getNmAnualidad(), presupMonto.getNmAnualidad())  //misma anualidad
                            && tablaPresupMaxFinanAdm.getTematicaDTO() != null
                            && presupuestoFinanciable.getTematicaDTO() != null
                            && Objects.equals(tablaPresupMaxFinanAdm.getTematicaDTO().getIdValor(), presupuestoFinanciable.getTematicaDTO().getIdValor())) //misma tabla por tematica que es unica
                    .findFirst().map(FinanciacionDatoDTO::getNmImporteMaxAnual).orElse(null);
            double importeMaxAnualidad = importeMaxAnualidadObj != null ? importeMaxAnualidadObj : 0.0;
            
            //calcular el sumatorio de cada anualidad por columna 
            List<ExpedientePresupDTO> presupuestosSinTotal = new ArrayList<>();
            if (nmPresup == 1) {
                if (this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty()) {
                    //volcamos una copia de la lista de presupuestos para que copie la lista de montos que tiene dentro con el constructor copia
                    for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable1) { 
                        presupuestosSinTotal.add(new ExpedientePresupDTO(presup));
                    }
                    //quitamos la fila de los totales en tabla
                    presupuestosSinTotal = presupuestosSinTotal.stream().
                            filter(pp -> pp.getTipoFinanciacionDTO() != null && Objects.nonNull(pp.getTipoFinanciacionDTO().getIdValor()))
                            .collect(Collectors.toList());
                }
            }else {
                if (this.lstPresupuestoFinanciable2 != null && !this.lstPresupuestoFinanciable2.isEmpty()) {
                    //volcamos una copia de la lista de presupuestos para que copie la lista de montos que tiene dentro con el constructor copia
                    for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable2) { 
                        presupuestosSinTotal.add(new ExpedientePresupDTO(presup));
                    }
                    //quitamos la fila de los totales en tabla
                    presupuestosSinTotal = presupuestosSinTotal.stream().
                            filter(pp -> pp.getTipoFinanciacionDTO() != null && Objects.nonNull(pp.getTipoFinanciacionDTO().getIdValor()))
                            .collect(Collectors.toList());
                }
            }
            Double sumaAnualidad = 0D;
            for (ExpedientePresupDTO partidaPres: presupuestosSinTotal) {
                                                            
                //las anualidades en exp presup monto empiezan en 0
                //quitamos la fila de los totales
                //si tiene conceptos se mete una anualidad 0 y empiezan en 1 los valores de importes por anualidades
                if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                    Double importeAnualidad = partidaPres.getLstExpedientePresupMontoDTO().stream()
                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                    && monto.getNmAnualidad().equals(nCol))
                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                    sumaAnualidad = sumaAnualidad + (importeAnualidad != null ? importeAnualidad : 0.0);
                }else {
                    Double importeAnualidad = partidaPres.getLstExpedientePresupMontoDTO().stream()
                            .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                    && monto.getNmAnualidad().equals(nCol-1))  //revisar este caso si es con -1 o no
                            .findFirst().map(ExpedientePresupMontoDTO::getNmImporte).orElse(null);
                    sumaAnualidad = sumaAnualidad + (importeAnualidad != null ? importeAnualidad : 0.0);
                }
            }
            
            montoAnualTexto = nf.format(importeMaxAnualidad).replace(".", ",");
            
            log.info("Sumatorio anualidad: " + sumaAnualidad);
            log.info("Importe maximo Anualidad: " + montoAnualTexto);
            log.info("Porcentaje intensidad ayuda: " + porcentajeIntensidadGestor);
            
            //si es modo maximos por anualidad, tenemos que calcular la suma de los importes de la anualidad que estamos editando
            //y si sobrepasa el maxImporteAnual de esa columna/anualidad mostramos mensaje de error
            this.tieneErrores = porcentajeIntensidadGestor != null
                    && (sumaAnualidad * (porcentajeIntensidadGestor / 100)) > importeMaxAnualidad;
        }
            
        //si este trozo de codigo queda igual para modo por PP y para modo por Aualidades, sacarlo a un metodo comun
        if (Boolean.TRUE.equals(this.tieneErrores)) {
                                    
            String tabla = " de la tabla de presupuesto " + nmPresup;

            Object[] msgImporte = null;                  
            if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad(), montoAnualTexto + " €", tabla };
            }else {
                //OJO que parece que cuando es sin conceptos hay que sumar uno a la anualidad!!!!!!!!!!!!!!!!!!
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad() + 1, montoAnualTexto + " €", tabla };
            }

            //de momento al editar una celda no rechazamos el importe erroneo en max por anualidad, pero mostramos un mensaje de error, 
            //lo validamos solo al Finalizar financiacion para no entrar en bucle si varios valores estan mal y permita modificar todos los que necesiten
            /*int indiceTotalfila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                    .size() - 1;
            double montoTotalInicial = presupuestoFinanciable
                    .getLstExpedientePresupMontoDTO().get(indiceTotalfila).getNmImporte();

            double montoInicial = presupMonto.getNmImporte()
                    - (totalFinalFila - montoTotalInicial);
            
            presupMonto.setNmImporte(montoInicial);*/

            if (datoFinan != null){

                //si hay error de presup fijo limpiamos el valor introducido
                int indiceTotalfila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                        .size() - 1;
                Double montoTotalInicialObj = presupuestoFinanciable
                        .getLstExpedientePresupMontoDTO().get(indiceTotalfila).getNmImporte();
                double montoTotalInicial = montoTotalInicialObj != null ? montoTotalInicialObj : 0.0;

                double montoInicial = (presupMonto.getNmImporte() != null ? presupMonto.getNmImporte() : 0.0)
                        - (totalFinalFila - montoTotalInicial);

                presupMonto.setNmImporte(montoInicial);
                
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_FIJO_PRESUP,
                        msgImporte);
                
            }else {
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_ANUALIDAD,
                        msgImporte);
            }
        }//else {

            calcularMontoTotalAyudaConIntensidad(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);

            //MD-2731: marcamos la fila como pendiente de observacion si no la tiene informada
            if (Objects.isNull(presupuestoFinanciable.getTlObservacion()) || StringUtils.isBlank(presupuestoFinanciable.getTlObservacion())) {
                presupuestoFinanciable.setPendienteObservacion(Boolean.TRUE);
            }
            
            if (datoFinan != null && Boolean.TRUE.equals(this.tieneErrores)){
                presupuestoFinanciable.setPendienteObservacion(Boolean.FALSE);
            }
            
            //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable);
            //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable2);
            PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan1)");
            PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan2)");
            PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);
            PrimeFaces.current().ajax().update(this.inputMontoAyudaConcedida);
        //}
    }

    
    private void validarImporteMaxPorExp(int nCol, int nmPresup, ExpedientePresupDTO presupuestoFinanciable, ExpedientePresupMontoDTO presupMonto,
            double totalFinalFila, FinanciacionDTO tablaPresupMaxFinanAdm, Double importeMaximoFijo) throws ServiceException {


        String montoTextoExp = "";
        DecimalFormat nf = new DecimalFormat("0.00");
        
        log.info("Monto capturado: " + presupMonto.getNmImporte());
                
        List<FinanciacionDatoDTO> lstfinanDatoPresupMaxAdm = tablaPresupMaxFinanAdm.getListaFinanciacionDatoDTO();
        
        //comprobamos primero si se trata de una celda de importe fijo en cuyo caso no miramos el total por exp sino el total de ese imp fijo
        FinanciacionDatoDTO datoFinan = lstfinanDatoPresupMaxAdm.stream()
                .filter(dato -> Objects.nonNull(dato.getNmImporteMax())
                    && dato.getTipoDTO().getIdValor().equals(presupuestoFinanciable.getTipoFinanciacionDTO().getIdValor())
                    && dato.getNmAnualidad().equals(nCol)
                    && Constantes.AfirmacionNegacion.S.equals(dato.getItImporteFijo())
                    && dato.getIdFinanciacion().equals(tablaPresupMaxFinanAdm.getIdFinanciacion()))
                .findFirst()
                .orElse(null);
 
        if (datoFinan != null){
            log.info("Importe maximo fijo: " + importeMaximoFijo);

            if (presupMonto.getNmImporte() != null && importeMaximoFijo != null && presupMonto.getNmImporte() > importeMaximoFijo) {
                montoTextoExp = nf.format(importeMaximoFijo).replace(".", ",");
                this.tieneErrores = Boolean.TRUE;
            }
        }else {  //NO ES IMPORTE FIJO

            //aqui tenemos que validar que con el importe introducido en celda, no se sobrepase el maximo por expdte
            //VMM: puede no venir informado el maximo por actor, se controla con el default a 0.0
            Double importeMaxExpObj = tablaPresupMaxFinanAdm.getNmImporteMaxActor();
            double importeMaxExp = importeMaxExpObj != null ? importeMaxExpObj : 0.0;

            montoTextoExp = nf.format(importeMaxExp).replace(".", ",");

            log.info("Importe maximo Expediente/Actor: " + montoTextoExp);
            log.info("Porcentaje intensidad ayuda: " + porcentajeIntensidadGestor);

            //si ya vemos que el monto introducido es mayor que el max x expdte no sigo calculando cosas
            if (importeMaxExpObj != null && presupMonto.getNmImporte() != null && porcentajeIntensidadGestor != null
                    && presupMonto.getNmImporte() * (porcentajeIntensidadGestor / 100) > importeMaxExp) {
                this.tieneErrores = Boolean.TRUE;
            }else {
                
                //si es modo maximos por expediente/actor, tenemos que calcular la suma de todos los importes de la tabla que estamos editando
                //esto seria lo mismo que el montoTotalAyudaConcedida - viejo importe + el nuevo importe??
                calcularMontoTotalAyudaConIntensidad(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);
                       
                //si hay mas de una tabla de presup para el mismo actor hay que recorrerlas las dos y sumar sus totales           
                double totalFinalSumTabla = 0;
        
                if (lstTablasFinan != null && lstTablasFinan.size() <= 2 && !lstTablasFinan.isEmpty()) {
        
                    boolean dosTablas = lstTablasFinan.size() == 2;
                    boolean unaTabla = lstTablasFinan.size() == 1;
        
                    // Sacar los IDs de actor de las tablas de financiacion con null-safe
                    Integer actor1 = (lstTablasFinan.get(0) != null 
                                   && lstTablasFinan.get(0).getIdActor() != null)
                                   ? lstTablasFinan.get(0).getIdActor().getIdActor()
                                   : null;
        
                    Integer actor2 = (dosTablas && lstTablasFinan.get(1) != null 
                                   && lstTablasFinan.get(1).getIdActor() != null)
                                   ? lstTablasFinan.get(1).getIdActor().getIdActor()
                                   : null;
        
                    boolean actoresIguales = actor1 != null && actor1.equals(actor2);
                    boolean actoresDistintos = actor1 != null && actor2 != null && !actor1.equals(actor2);
        
                    if (dosTablas && actoresIguales) {
                        // Ambos actores son iguales → sumamos los dos montos
                        //si el actor de la tabla 1 es igual al actor de la tabla 2 sumamos sus montos totales del expediente
                        totalFinalSumTabla = this.montoAyudaConcedida + this.montoAyudaConcedida2;
                    }
                    else if ((dosTablas && actoresDistintos && presupuestoFinanciable.getNmPresup() == 1) || unaTabla) {
                        // Primera tabla o única tabla
                        //si la tabla que estamos editando es la 1 (pero hay dos tablas presup) y no coinciden los actores cogemos solo el monto total 1
                        //o si solo hay una tabla presup, entonces es la primera y unica que estamos editando y me quedo con el monto 1
                        totalFinalSumTabla = this.montoAyudaConcedida;
                    }
                    else if (dosTablas && actoresDistintos && presupuestoFinanciable.getNmPresup() == 2) {
                        // Segunda tabla
                        //si la tabla que estamos editando es la 2 (pero hay dos tablas presup) y no coinciden los actores cogemos solo el monto total 2
                        totalFinalSumTabla = this.montoAyudaConcedida2;
                    }
                }
                
                //si sobrepasa el importeMaxExp mostramos mensaje de error
                this.tieneErrores = importeMaxExpObj != null && totalFinalSumTabla > importeMaxExp;
            }
        }
            
        //si este trozo de codigo queda igual para modo por PP y para modo por Aualidades o Expdte, sacarlo a un metodo comun
        if (Boolean.TRUE.equals(this.tieneErrores)) {
                                    
            String tabla = " de la tabla de presupuesto " + nmPresup;

            Object[] msgImporte = null;                  
            if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad(), montoTextoExp + " €", tabla };
            }else {
                msgImporte = new Object[] {
                    presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor(),
                    presupMonto.getNmAnualidad() + 1, montoTextoExp + " €", tabla };
            }

            //de momento al editar una celda no rechazamos el importe erroneo en max por expdte, pero mostramos un mensaje de error, 
            //lo validamos solo al Finalizar financiacion para no entrar en bucle si varios valores estan mal
            /*int indiceTotalfila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                    .size() - 1;
            double montoTotalInicial = presupuestoFinanciable
                    .getLstExpedientePresupMontoDTO().get(indiceTotalfila).getNmImporte();

            double montoInicial = presupMonto.getNmImporte()
                    - (totalFinalFila - montoTotalInicial);
            
            presupMonto.setNmImporte(montoInicial);*/
            
            //volvemos a dejar la tabla como estaba en caso de error
            //aqui habra que afinar o buscar la forma de no perder el resto de cambios o avisar de que se perderán si no se han guardado
            //this.obtenerPresupuestoFinanciable(nmPresup);

            if (datoFinan != null){

                //si hay error de presup fijo limpiamos el valor introducido
                int indiceTotalfila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                        .size() - 1;
                Double montoTotalInicialObj = presupuestoFinanciable
                        .getLstExpedientePresupMontoDTO().get(indiceTotalfila).getNmImporte();
                double montoTotalInicial = montoTotalInicialObj != null ? montoTotalInicialObj : 0.0;

                double montoInicial = (presupMonto.getNmImporte() != null ? presupMonto.getNmImporte() : 0.0)
                        - (totalFinalFila - montoTotalInicial);

                presupMonto.setNmImporte(montoInicial);
                
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_FIJO_PRESUP,
                        msgImporte);
            }else {
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_PRESUP_EXPDTE,
                        msgImporte);
            }
        }//else {
            //tanto si hay error y tenemos que dejar la celda editada como estaba, como si no hay errores, recalculamos de nuevo los totales
            calcularMontoTotalAyudaConIntensidad(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);

            //MD-2731: marcamos la fila como pendiente de observacion si no la tiene informada
            if (Objects.isNull(presupuestoFinanciable.getTlObservacion()) || StringUtils.isBlank(presupuestoFinanciable.getTlObservacion())) {
                presupuestoFinanciable.setPendienteObservacion(Boolean.TRUE);
            }
            
            if (datoFinan != null && Boolean.TRUE.equals(this.tieneErrores)){
                presupuestoFinanciable.setPendienteObservacion(Boolean.FALSE);
            }
            
            //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable);
            //PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable2);
            PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan1)");
            PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan2)");
            PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);
            PrimeFaces.current().ajax().update(this.inputMontoAyudaConcedida);
       // }
    }
    

    private void calcularMontoTotalAyudaConIntensidad(int nCol, int nmPresup, ExpedientePresupDTO presupuestoFinanciable,
            double totalFinalFila) throws ServiceException {

        this.modificarTotales(nCol, nmPresup, presupuestoFinanciable, totalFinalFila);
        
        if(nmPresup == 1 && this.lstPresupuestoFinanciable1 != null) {
            //Actualizamos lista para ayudaConcedida
            this.lstPresupuestoAyudaConcedida = new ArrayList<>();
            for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable1) { 
                this.lstPresupuestoAyudaConcedida.add(new ExpedientePresupDTO(presup));
            }
            for(ExpedientePresupDTO presupAyudaConcedida : this.lstPresupuestoAyudaConcedida ) {
                for(ExpedientePresupMontoDTO monto : presupAyudaConcedida.getLstExpedientePresupMontoDTO()) {
                    if(monto.getNmImporte() != null && porcentajeIntensidadGestor != null) {
                        monto.setNmImporte(monto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                    }
                }
            }
            List<ExpedientePresupMontoDTO> ultimaLista = this.lstPresupuestoAyudaConcedida.
                    get(this.lstPresupuestoAyudaConcedida.size() - 1).getLstExpedientePresupMontoDTO();
            
            // Asegúrate de que la última lista no esté vacía
            if (!ultimaLista.isEmpty()) {
                // Obtener el último elemento de la última lista
                ExpedientePresupMontoDTO ultimoDato = ultimaLista.get(ultimaLista.size() - 1);

                // Obtener el valor dsValor del último DatoDTO, y aplicarle el minimo de los 3 topes posibles
                this.recalcularMinAyudaConcedida(ultimoDato.getNmImporte());

            }
        }
        if(nmPresup == 2 && this.lstPresupuestoFinanciable2 != null) {
            //Actualizamos lista para ayudaConcedida2
            this.lstPresupuestoAyudaConcedida2 = new ArrayList<>();
            for (ExpedientePresupDTO presup2 : this.lstPresupuestoFinanciable2) { 
                this.lstPresupuestoAyudaConcedida2.add(new ExpedientePresupDTO(presup2));
            }
            for(ExpedientePresupDTO presupAyudaConcedida2 : this.lstPresupuestoAyudaConcedida2 ) {
                for(ExpedientePresupMontoDTO monto2 : presupAyudaConcedida2.getLstExpedientePresupMontoDTO()) {
                    if(monto2.getNmImporte() != null && porcentajeIntensidadGestor != null) {
                        monto2.setNmImporte(monto2.getNmImporte() * (porcentajeIntensidadGestor / 100));
                    }
                }
            }
            List<ExpedientePresupMontoDTO> ultimaLista2 = this.lstPresupuestoAyudaConcedida2.
                    get(this.lstPresupuestoAyudaConcedida2.size() - 1).getLstExpedientePresupMontoDTO();
            
            // Asegúrate de que la última lista no esté vacía
            if (!ultimaLista2.isEmpty()) {
                // Obtener el último elemento de la última lista
                ExpedientePresupMontoDTO ultimoDato2 = ultimaLista2.get(ultimaLista2.size() - 1);
                
                //Guardamos aqui el total de la tabla 2 para poder comparar en max por expdte aunque no se muestre por pantalla
                // Obtener el valor dsValor del último DatoDTO
                this.setMontoAyudaConcedida2(ultimoDato2.getNmImporte());
            } 
        }
    }
    
    public void comprobarPresupuestoFinanciable() {
        // VMM 03/09/26 MD-2933: en cuanto el gestor informa manualmente la intensidad, el aviso de
        // "formato incorrecto" de la intensidad precargada deja de tener sentido.
        if (this.porcentajeIntensidadGestor != null) {
            this.esformatoErroneoIntensidad = Boolean.FALSE;
        }
        if (this.lstPresupuestoFinanciable1 != null && this.lstPresupuestoFinanciable1.isEmpty() &&
                this.porcentajeIntensidadGestor != null) {
            this.cargarDatosPresupuesto(this.porcentajeIntensidadGestor);
        } else {
            this.actualizarIntensidadAyuda();
        }
    }

    public void actualizarIntensidadAyuda() {
        try {
            if (this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty() &&
                    this.porcentajeIntensidadGestor != null) {
                
                this.lstPresupuestoAyudaConcedida = new ArrayList<>();
                for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable1) {
                    this.lstPresupuestoAyudaConcedida.add(new ExpedientePresupDTO(presup));
                }
                for (ExpedientePresupDTO presupAyudaConcedida : this.lstPresupuestoAyudaConcedida) {
                    for (ExpedientePresupMontoDTO monto : presupAyudaConcedida.getLstExpedientePresupMontoDTO()) {
                        if (monto.getNmImporte() != null) {
                            monto.setNmImporte(monto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                        }
                    }
                }
                List<ExpedientePresupMontoDTO> ultimaLista = this.lstPresupuestoAyudaConcedida
                        .get(this.lstPresupuestoAyudaConcedida.size() - 1).getLstExpedientePresupMontoDTO();
                if (!ultimaLista.isEmpty()) {
                    this.recalcularMinAyudaConcedida(ultimaLista.get(ultimaLista.size() - 1).getNmImporte());
                }
                
                this.expedientePresupService.actualizarIntensidadAyuda(
                        this.expedienteBean.getIdExpediente(), 1, this.porcentajeIntensidadGestor,
                        this.montoAyudaConcedida, this.montoAyudaSolicitada);
            }
            
            if (this.lstPresupuestoFinanciable2 != null && !this.lstPresupuestoFinanciable2.isEmpty()
                    && this.porcentajeIntensidadGestor != null) {  //esto en un futuro seria porcentajeIntensidadGestor2
                
                this.lstPresupuestoAyudaConcedida2 = new ArrayList<>();
                for (ExpedientePresupDTO presup : this.lstPresupuestoFinanciable2) {
                    this.lstPresupuestoAyudaConcedida2.add(new ExpedientePresupDTO(presup));
                }
                for (ExpedientePresupDTO presupAyudaConcedida2 : this.lstPresupuestoAyudaConcedida2) {
                    for (ExpedientePresupMontoDTO monto : presupAyudaConcedida2.getLstExpedientePresupMontoDTO()) {
                        if (monto.getNmImporte() != null) {
                            monto.setNmImporte(monto.getNmImporte() * (porcentajeIntensidadGestor / 100));
                        }
                    }
                }
                List<ExpedientePresupMontoDTO> ultimaLista2 = this.lstPresupuestoAyudaConcedida2
                        .get(this.lstPresupuestoAyudaConcedida2.size() - 1).getLstExpedientePresupMontoDTO();
                if (!ultimaLista2.isEmpty()) {
                    this.setMontoAyudaConcedida2(ultimaLista2.get(ultimaLista2.size() - 1).getNmImporte());
                }
                
                this.expedientePresupService.actualizarIntensidadAyuda(
                        this.expedienteBean.getIdExpediente(), 2, this.porcentajeIntensidadGestor,
                        this.montoAyudaConcedida2, this.montoAyudaSolicitada);
            }
            
            AtlasMessageUtils.addInfoMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_INTENSIDAD_GRABAR_OK);
            
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }


    /**
     * Metodo para copiar los datos de presupuestos presentados a ayuda
     * solicitada
     */
    private void cargarDatosAyudaSolicitada() {
        try {
            
            //Generamos lista para ayudaSolicitada
            this.lstPresupuestoAyudaSolicitada = new ArrayList<>();
            for (BloqueComponenteDTO bloqueComp : this.getLstBloque().get(0).getListaBloqueAuxDTO()) { 
                this.lstPresupuestoAyudaSolicitada.add(new BloqueComponenteDTO(bloqueComp));
            }

            //Aplicamos porcentaje Intensidad para los datos de ayuda solicitada
            for(BloqueComponenteDTO bloqueComp : this.lstPresupuestoAyudaSolicitada ) {
               for(DatoDTO dato : bloqueComp.getDatos()) {
                   // normalizamos coma/punto (formato español o anglosajon) antes de comprobar si es numerico
                   String dsValor = Utils.normalizarSeparadorDecimal(StringUtils.trim(dato.getDsValor()));
                   if (StringUtils.isNotBlank(dsValor)) {
                       if (Boolean.TRUE.equals(Utils.validarDouble(dsValor)) && porcentajeIntensidadAyuda != null) {
                           double dsValorDouble = Double.parseDouble(dsValor);
                           double nuevoValor = dsValorDouble * (porcentajeIntensidadAyuda / 100);
                           dato.setDsValor(Double.toString(nuevoValor));
                       } else {
                           //Si no es numerico se mantiene sin modificar
                       }
                   }
               }
            }
            // Asegúrate de que la lista no esté vacía
            if (!this.lstPresupuestoAyudaSolicitada.isEmpty()) {
                // Obtener el último elemento de lstPresupuestoAyudaSolicitada
                List<DatoDTO> ultimaLista = this.lstPresupuestoAyudaSolicitada.get(this.lstPresupuestoAyudaSolicitada.size() - 1).getDatos();
                
                // Asegúrate de que la última lista no esté vacía
                if (!ultimaLista.isEmpty()) {
                    // Obtener el último elemento de la última lista
                    DatoDTO ultimoDato = ultimaLista.get(ultimaLista.size() - 1);
                    
                    //MD-2788: comprobar si existe monto de ayuda único configurado 
//                    if (this.montoAyudaPresupuesto != null) {
//                        //coger el monto de ayuda informado en el formulario de solicitud
//                        this.montoAyudaSolicitada = this.montoAyudaPresupuesto;
//                    }else {
//                        // Obtener el valor dsValor del último DatoDTO
//                        this.montoAyudaSolicitada = Double.parseDouble(ultimoDato.getDsValor());
//                    }
                    
                    if (this.montoAyudaPresupuesto != null) {
                        this.montoAyudaSolicitada = this.montoAyudaPresupuesto;
                    } else if (StringUtils.isNotBlank(ultimoDato.getDsValor())) {
                        this.montoAyudaSolicitada = this.parseImporteDsValor(ultimoDato.getDsValor());
                    }
                    
                } 
            } 

            
            if(this.getLstBloque().size() > 1){
                //Generamos lista para ayudaSolicitada2
                this.lstPresupuestoAyudaSolicitada2 = new ArrayList<>();
                for (BloqueComponenteDTO bloqueComp : this.getLstBloque().get(1).getListaBloqueAuxDTO()) { 
                    this.lstPresupuestoAyudaSolicitada2.add(new BloqueComponenteDTO(bloqueComp));
                }

                //Aplicamos porcentaje Intensidad para los datos de ayuda solicitada
                for(BloqueComponenteDTO bloqueComp : this.lstPresupuestoAyudaSolicitada2 ) {
                   for(DatoDTO dato : bloqueComp.getDatos()) {
                       // normalizamos coma/punto (formato español o anglosajon) antes de comprobar si es numerico
                       String dsValor = Utils.normalizarSeparadorDecimal(StringUtils.trim(dato.getDsValor()));
                       if (StringUtils.isNotBlank(dsValor)) {
                           if (Boolean.TRUE.equals(Utils.validarDouble(dsValor)) && porcentajeIntensidadAyuda != null) {
                               double dsValorDouble = Double.parseDouble(dsValor);
                               double nuevoValor = dsValorDouble * (porcentajeIntensidadAyuda / 100);
                               dato.setDsValor(Double.toString(nuevoValor));
                           } else {
                               //Si no es numerico se mantiene sin modificar
                           }
                       }
                   }
                }
            }
            
            PrimeFaces.current().ajax().update(this.solapaFinanciacion);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     *
     * @param presupuestoFinanciable
     * @param nCol
     */
    public void edicionCeldaPresupuesto(ExpedientePresupDTO presupuestoFinanciable, int nCol,
            int nmPresup) {

        try {

            if (Objects.nonNull(presupuestoFinanciable)) {

                // dsValor del tipo de financiación de esta fila, calculado una sola vez y de forma
                // null-safe, para evitar repetir "presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor()"
                // sin comprobar getTipoFinanciacionDTO() en cada uno de los filtros de más abajo
                String dsValorTipoFinanPresup = presupuestoFinanciable.getTipoFinanciacionDTO() != null
                        ? presupuestoFinanciable.getTipoFinanciacionDTO().getDsValor()
                        : null;

                ExpedientePresupMontoDTO presupMonto = presupuestoFinanciable
                        .getLstExpedientePresupMontoDTO().stream()
                        .filter(monto -> Objects.nonNull(monto.getNmAnualidad()) && monto.getNmAnualidad().equals(nCol)).findFirst()
                        .orElse(new ExpedientePresupMontoDTO());
                
                //VMM 05/08/25 MD-1687: comprobamos si se han validado los datos en SAII_SOL o no según el flag IT_MOSTRAR_AYUDA
                //obtenemos las tablas de presupuestos de ADMIN con los máximos permitidos y el flag a consultar
                if (Objects.isNull(this.lstTablasFinan)) {
                    this.lstTablasFinan = this.financiacionService.
                            buscaFinanciacionByIdConvocatoria(this.expedienteBean.getIdConvocatoria());
                }
                
                double importeMaximo = 0; 
                
                if (this.lstTablasFinan != null && !this.lstTablasFinan.isEmpty()) {
                                        
                    //MD-2731 28/05/26: comprobamos que la tabla sea también del mismo actor además de la misma temática
                    //obtenemos la tabla correspondiente de financiacion desde el expdte_presup cruzando por la tematica 
                    //que no se puede repetir entre tablas presupuestarias (es el unico campo con el que podemos cruzar)
                    Integer idTematicaPresupFinanciable = presupuestoFinanciable.getTematicaDTO() != null
                            ? presupuestoFinanciable.getTematicaDTO().getIdValor() : null;
                    Integer idActorExpediente = (this.expedienteBean.getExpedienteDTO() != null
                            && this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO() != null)
                                    ? this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor() : null;
                    FinanciacionDTO tablaPresupMaxFinanAdm = this.lstTablasFinan.stream()
                            .filter(fila -> Objects.equals(fila.getTematicaDTO() != null ? fila.getTematicaDTO().getIdValor() : null,
                                    idTematicaPresupFinanciable)
                                    && Objects.equals(fila.getIdActor() != null ? fila.getIdActor().getIdActor() : null,
                                            idActorExpediente))
                            .findFirst().orElse(new FinanciacionDTO());
                                    
                    List<FinanciacionDatoDTO> lstfinanDatoPresupMaxAdm = tablaPresupMaxFinanAdm.getListaFinanciacionDatoDTO();
                                       
                    if (Objects.isNull(presupMonto.getNmImporte())) {  
                        //sacar un mensaje de error al gestor diciendo que no puede introducir valores nulos o infinitos...
                        AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_ERROR_IMPORTE_NULO);
                        return;
                    } 
                    
                    double totalFinalFila = 0;
                    boolean esMostrarAyuda = tablaPresupMaxFinanAdm.getItMostrarAyuda() != null && 
                                                tablaPresupMaxFinanAdm.getItMostrarAyuda().equals(Constantes.AfirmacionNegacion.S);
                    
                    if (esMostrarAyuda) { //si se han hecho las validaciones en saii_sol
                      
                        //si flag IT_MOSTRAR_AYUDA = S, coge los datos de saii_dato que vienen de SOLICITUD como maximos
                        //no deben llegar valores/montos/importes a null porque ya se ha validado en SOLICITUD
                        if (nmPresup == 1) {
                            this.lstBloqueComponenteFila = this.lstBloque.get(0).getListaBloqueAuxDTO();
                        } else {
                            this.lstBloqueComponenteFila = this.lstBloque.get(1).getListaBloqueAuxDTO();
                        }
                        
                        //tenemos que comparar contra los importes presentados en el formulario de solicitud guardados en saii_dato (osea tabla de presup presentados)
                        //en saii_dato si hay conceptos tambien tengo que coger la siguiente columna para valores de importes ya que la primera col son los conceptos
                        String importePresentado = null;
                        if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) { 
                            
                            if (presupuestoFinanciable.getItNuevoConceptoFinan() != null 
                                    && Constantes.AfirmacionNegacion.S.equals(presupuestoFinanciable.getItNuevoConceptoFinan())) {
                                //si es un nuevo concepto no tenemos contra quien compararlo si es una convocatoria de max por PP
                                importePresentado = null;
                            }else {
                            
                                //MD-2731: controlar que si han metido nuevos conceptos esta comparación no pete 
                                //y ver si es misma partida habrá que comparar sus conceptos
                                importePresentado = this.lstBloqueComponenteFila.stream()
                                    .filter(fila -> Objects.equals(
                                            fila.getTipoFinanciacionDTO() != null ? fila.getTipoFinanciacionDTO().getDsValor() : null,
                                            dsValorTipoFinanPresup))
                                    .findFirst()
                                    //creo que el +1 es porque hay conceptos en la primera fila (0)
                                    .filter(fila -> fila.getDatos() != null && fila.getDatos().size() > nCol + 1)
                                    .map(fila -> fila.getDatos().get(nCol + 1).getDsValor())
                                    .orElse(null);
                                                            
                                if (Objects.isNull(importePresentado)) {  
                                    //sacar un mensaje de error al gestor diciendo que no puede comparar esos importes de nuevos conceptos
                                    AtlasMessageUtils.addErrorMessage("No se ha encontrado el importe maximo presentado correspondiente al importe editado");
                                    return;
                                } 
                                
                                //aqui en los montos/importes introducidos por el gestor no puede haber nulos, lo valido al ppio
                                //OJO QUE ESTE IMPORTE ES SUMA POR FILA NO POR COLUMNA
                                //si es una convocatoria con conceptos tenemos que ignorar la columna o anualidad 0 que es donde van los conceptos
                                totalFinalFila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                                    .stream()
                                    .filter(monto -> Objects.nonNull(monto.getNmAnualidad()) && !monto.getNmAnualidad().equals(0)
                                            && Objects.nonNull(monto.getNmImporte()))
                                    .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                            }
                        }else {  //SIN CONCEPTOS Y CON AYUDAS
                            importePresentado = this.lstBloqueComponenteFila.stream()
                                    .filter(fila -> Objects.equals(
                                            fila.getTipoFinanciacionDTO() != null ? fila.getTipoFinanciacionDTO().getDsValor() : null,
                                            dsValorTipoFinanPresup))
                                    .findFirst()
                                    .filter(fila -> fila.getDatos() != null && fila.getDatos().size() > nCol)
                                    .map(fila -> fila.getDatos().get(nCol).getDsValor())
                                    .orElse(null);
                            
                            //aqui en los montos/importes introducidos por el gestor no puede haber nulos, lo valido al ppio
                            //OJO QUE ESTE IMPORTE ES SUMA POR FILA NO POR COLUMNA
                            totalFinalFila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                                .stream()
                                .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                        && Objects.nonNull(monto.getNmImporte()))
                                .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
                        }
                        
                        //si llega aqui el importe presentado nulo porque es de un nuevo concepto
                        //aqui no deberian nunca llegar importes nulos del usuario
                        importeMaximo = Objects.nonNull(importePresentado)
                                ? this.parseImporteDsValor(importePresentado)
                                //: 999999.99;
                                  : 9999999999.99; //VMM 04/08/25 MD-1687
                                                         
                    }else { //no se han hecho las validaciones en SAII_SOL
                                                                     
                        //si flag IT_MOSTRAR_AYUDA = N, coge los datos de saii_financiacion que vienen de ADMIN como maximos
                        //pueden llegar montos/importes a null porque no se han validado en SOLICITUD                        
                        //tenemos que comparar contra los importes/montos maximos de saii_financiacion
                                
                        //aqui si puede haber importes maximos nulos que se tratan como 9999999999.99
                        Double importeMaxAdmin = 0.0;
                        if(Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
                            
                            if (presupuestoFinanciable.getItNuevoConceptoFinan() != null 
                                    && Constantes.AfirmacionNegacion.S.equals(presupuestoFinanciable.getItNuevoConceptoFinan())) {
                                //si es un nuevo concepto no tenemos contra quien compararlo si es una convocatoria de max por PP
                                importeMaxAdmin = null;
                            }else {
                                //MD-2731: aqui solo encontrara importe si es un importe fijo o si es una convo de MAX por PP
                                importeMaxAdmin = lstfinanDatoPresupMaxAdm.stream()
                                    .filter(fila -> Objects.equals(
                                            fila.getTipoDTO() != null ? fila.getTipoDTO().getDsValor() : null,
                                            dsValorTipoFinanPresup)
                                            && fila.getNmAnualidad() != null
                                            && fila.getNmAnualidad().equals(nCol))
                                    .findFirst().orElse(new FinanciacionDatoDTO()).getNmImporteMax();
                                                                
                                //aqui puede haber nulos ya que al cargar los presupuestos presentados no han sido validados y se copian directamente a saii_exp_presup
                                //por tanto, el sumatorio puede "desbordar" si hay algun valor nulo (9999999999.99) y mas valores 
                                //mayores que 0 o varios valores nulos
                                //si es una convocatoria con conceptos tenemos que ignorar la columna o anualidad 0 que es donde van los conceptos (da igual porque sumaria un 0)
                                totalFinalFila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                                    .stream()
                                    .filter(monto -> Objects.nonNull(monto.getNmAnualidad())
                                            && !monto.getNmAnualidad().equals(0))
                                    .mapToDouble(monto -> {
                                        Double importe = monto.getNmImporte();
                                        return importe != null ? importe : 9999999999.99;
                                    })
                                    .sum();
                            }
                        }else { //SIN CONCEPTOS Y SIN AYUDA
                            //aqui solo encontrara importe si es un importe fijo o si es una convo de MAX por PP
                            importeMaxAdmin = lstfinanDatoPresupMaxAdm.stream()
                                    .filter(fila -> Objects.equals(
                                            fila.getTipoDTO() != null ? fila.getTipoDTO().getDsValor() : null,
                                            dsValorTipoFinanPresup)
                                            && fila.getNmAnualidad() != null
                                            && fila.getNmAnualidad().equals(nCol+1))
                                    .findFirst().orElse(new FinanciacionDatoDTO()).getNmImporteMax();
                            
                            //aqui puede haber nulos ya que al cargar los presupuestos presentados no han sido validados y se copian directamente de ADM a saii_exp_presup
                            //por tanto, el sumatorio puede "desbordar" si hay algun valor nulo (9999999999.99) y mas valores 
                            //mayores que 0 o varios valores nulos
                            //si es una convocatoria con conceptos tenemos que ignorar la columna o anualidad 0 que es donde van los conceptos (da igual porque sumaria un 0)
                            totalFinalFila = presupuestoFinanciable.getLstExpedientePresupMontoDTO()
                                .stream()
                                .filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                                .mapToDouble(monto -> {
                                    Double importe = monto.getNmImporte();
                                    return importe != null ? importe : 9999999999.99;
                                })
                                .sum();
                        }
                        
                        importeMaximo = Objects.nonNull(importeMaxAdmin)
                                ? importeMaxAdmin
                                  : 9999999999.99; 

                        //Comprobación de desbordamiento
                        //En Java, el tipo double no lanza excepción por overflow, sino que retorna 
                        //Infinity si el valor es demasiado grande
                        if (Double.isInfinite(totalFinalFila)) {
                            AtlasMessageUtils.addErrorMessage("Reemplace los valores nulos en el módulo de ADMIN para poder realizar el sumatorio de los importes totales.");
                            return;
                        }
                    }
                                        
                    //comprobamos si supera el importe maximo en cada caso
                    validarPresupFinanEditar(nCol, nmPresup, presupuestoFinanciable, presupMonto, 
                            importeMaximo, totalFinalFila, tablaPresupMaxFinanAdm);
                   
                    //this.pendienteGuardarObservacion = Boolean.TRUE;

                    /*PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable);
                    PrimeFaces.current().ajax().update(this.tablaPresupuestoFinanciable2);
                    PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan1)");
                    PrimeFaces.current().ajax().update("@(.js-panel-presupuestoFinan2)");
                    PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);
                    PrimeFaces.current().ajax().update(this.inputMontoAyudaConcedida);*/
                }       
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }


    /**
     *
     * @param nCol
     * @param presup
     * @param montoFinalFila
     */
    private void modificarTotales(int nCol, int nmPresup, ExpedientePresupDTO presup,
            double montoFinalFila) {
        
        // Se actualiza el total de la fila
        int indexMontoFila = presup.getLstExpedientePresupMontoDTO().size() - 1;
        ExpedientePresupMontoDTO montoTotal = presup.getLstExpedientePresupMontoDTO()
                .get(indexMontoFila);
        montoTotal.setNmImporte(montoFinalFila);

        presup.getLstExpedientePresupMontoDTO().set(indexMontoFila, montoTotal);

        // Se actualiza el total de la columna
        double sumaTotal = 0.0;
        double sumaColumna = 0.0;

        if (nmPresup == 1) {
            this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciable1;
        } else {
            this.lstPresupuestoFinanciable = this.lstPresupuestoFinanciable2;
        }

        List<ExpedientePresupDTO> presupuestos = this.lstPresupuestoFinanciable.stream()
                .filter(presupuestoDTO -> Objects.nonNull(presupuestoDTO.getTipoFinanciacionDTO())
                        && Objects.nonNull(presupuestoDTO.getTipoFinanciacionDTO().getIdValor()))
                .collect(Collectors.toList());

        for (ExpedientePresupDTO presupuesto : presupuestos) {
            List<ExpedientePresupMontoDTO> montosPresupuesto = presupuesto
                    .getLstExpedientePresupMontoDTO().stream()
                    .filter(monto -> Objects.nonNull(monto.getNmAnualidad()))
                    .collect(Collectors.toList());

            // Antes se usaba ExpedientePresupMontoDTO::getNmImporte como mapToDouble directo:
            // si algún importe venía null, el autounboxing lanzaba NullPointerException
            sumaTotal += montosPresupuesto.stream()
                    .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();

            sumaColumna += montosPresupuesto.stream()
                    .filter(monto -> nCol == monto.getNmAnualidad())
                    .mapToDouble(monto -> monto.getNmImporte() != null ? monto.getNmImporte() : 0.0).sum();
        }

        int indexMontoColumna = this.lstPresupuestoFinanciable.size() - 1;
        ExpedientePresupDTO presupuestoDTO = this.lstPresupuestoFinanciable.get(indexMontoColumna);

        int indexColumnaTotal = presupuestoDTO.getLstExpedientePresupMontoDTO().size() - 1;

        ExpedientePresupMontoDTO montoColumna = presupuestoDTO.getLstExpedientePresupMontoDTO()
                .get(nCol);
        montoColumna.setNmImporte(sumaColumna);
        presupuestoDTO.getLstExpedientePresupMontoDTO().set(nCol, montoColumna);

        montoColumna = presupuestoDTO.getLstExpedientePresupMontoDTO().get(indexColumnaTotal);
        montoColumna.setNmImporte(sumaTotal);
        presupuestoDTO.getLstExpedientePresupMontoDTO().set(indexColumnaTotal, montoColumna);

        if (nmPresup == 1) {
            this.lstPresupuestoFinanciable1 = this.lstPresupuestoFinanciable;
        } else {
            this.lstPresupuestoFinanciable2 = this.lstPresupuestoFinanciable;
        }
    }

    /**
     *
     * @param tipoFinanciacion
     * @param nmAnualidad
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("unused")
    private double obtenerImporteMaxPresup(CatalogosValoresDTO tipoFinanciacion, int nmAnualidad,
            int nmPresup) throws ServiceException {
        
        List<FinanciacionDTO> lstFinanciacion = this.financiacionService.buscarTablasFinanByIdConvocAndTipo(
                this.expedienteBean.getIdConvocatoria(), Constantes.Formatos.PARTIDAS_PRESUPUESTARIAS);

        int anualidadAux = nmAnualidad + 1;

        //double importeMax = 999999.99;
        double importeMax = 9999999999.99;
        for (FinanciacionDTO financiacion : lstFinanciacion) {
//            FinanciacionDatoDTO datoImporteMax = financiacion.getListaFinanciacionDatoDTO().stream()
//                    .filter(dato -> dato.getTipoDTO().getIdValor()
//                            .equals(tipoFinanciacion.getIdValor())
//                            && dato.getNmAnualidad().equals(anualidadAux))
//                    .findFirst().orElse(new FinanciacionDatoDTO());

            List<FinanciacionDatoDTO> lstImporteMax = financiacion.getListaFinanciacionDatoDTO()
                    .stream()
                    .filter(dato -> dato.getTipoDTO().getIdValor()
                            .equals(tipoFinanciacion.getIdValor())
                            && dato.getNmAnualidad().equals(anualidadAux))
                    .collect(Collectors.toList());

            lstImporteMax.sort(Comparator.comparing(FinanciacionDatoDTO::getIdFinanciacion));

            FinanciacionDatoDTO datoImporteMax = new FinanciacionDatoDTO();

            if (nmAnualidad == 1) {
                if (Objects.nonNull(datoImporteMax.getNmImporteMax())) {
                    importeMax = datoImporteMax.getNmImporteMax();
                    break;
                }
            }
        }
        return importeMax;
    }


    /**
     * ESTE METODO NO SE USA...
     * @throws ServiceException
     */
    @SuppressWarnings("unused")
    public void edicionMontoConcePresup() throws ServiceException {
        try {
            if (Objects.isNull(this.montoAyudaConcedida)) {
                this.montoAyudaConcedida = 0.0;
            }

            double montoMaximo = this.montoAyudaPresupuesto;

            if (this.montoAyudaConcedida > montoMaximo) {
                
                //VMM 04/02/26 MD-2434: redondeamos a dos decimales el mensaje de importe maximo permitido
                //Object[] msgImporte = new Object[] {" la ayuda concedida en presupuestos ",
                //   montoMaximo + " € en Ayuda Concedida" };
                
                DecimalFormat df = new DecimalFormat("#0.00");
                df.setRoundingMode(RoundingMode.DOWN);

                Object[] msgImporte = new Object[] {"la ayuda concedida en presupuesto ",
                    df.format(montoMaximo) + " € en Ayuda Concedida"};

                this.montoAyudaConcedida = this.lstPresupuestoFinanciable1.get(0).getNmAyuda();
                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_KO,
                        msgImporte);
            }
            this.pendienteGuardarObservacion = Boolean.TRUE;
            PrimeFaces.current().ajax().update(this.mensajeAdvertenciaPresup);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    private Double obtenerMontoMinAyuda() throws ServiceException {
        Double montoMinimoAyudaSolicitada = 0.0;

        List<FinanciacionDTO> lstFinanciacion = this.financiacionService.buscarTablasFinanByIdConvocAndTipo(
                this.expedienteBean.getIdConvocatoria(), Constantes.Formatos.AYUDA_SOLICITADA);

        if (!lstFinanciacion.isEmpty()) {
            montoMinimoAyudaSolicitada = lstFinanciacion.get(0).getListaFinanciacionDatoDTO().get(0)
                    .getNmImporteMin();
        }
        return montoMinimoAyudaSolicitada;
    }

    /**
     *
     * @return el monto de ayuda maximo configurado en ADM para la tabla de financiacion de tipo "AS" (Ayuda Solicitada)
     * @throws ServiceException
     */
    private Double obtenerMontoMaxAyuda() throws ServiceException {
        Double montoMaximoAyudaSolicitada = null;

        List<FinanciacionDTO> lstFinanciacion = this.financiacionService.buscarTablasFinanByIdConvocAndTipo(
                this.expedienteBean.getIdConvocatoria(), Constantes.Formatos.AYUDA_SOLICITADA);

        if (!lstFinanciacion.isEmpty()) {
            montoMaximoAyudaSolicitada = lstFinanciacion.get(0).getListaFinanciacionDatoDTO().get(0)
                    .getNmImporteMax();
        }
        return montoMaximoAyudaSolicitada;
    }

    /**
     * Establece montoAyudaConcedida como el minimo entre: la ayuda solicitada, el recalculo del
     * total de la tabla de presupuesto financiable segun la intensidad del gestor, y el monto
     * maximo de ayuda configurado en ADM para la tabla de financiacion de tipo "AS".
     *
     * @param totalPresupFinanciableXIntensidad total de la tabla de presupuesto financiable ya
     *        multiplicado por la intensidad de la ayuda del gestor
     * @throws ServiceException
     */
    private void recalcularMinAyudaConcedida(double totalPresupFinanciableXIntensidad) throws ServiceException {
        List<Double> posiblesValores = new ArrayList<>();

        if (this.montoAyudaSolicitada != null) {
            posiblesValores.add(this.montoAyudaSolicitada);
        }
        posiblesValores.add(totalPresupFinanciableXIntensidad);

        Double montoMaxAyudaAS = this.obtenerMontoMaxAyuda();
        if (montoMaxAyudaAS != null) {
            posiblesValores.add(montoMaxAyudaAS);
        }

        this.montoAyudaConcedida = Collections.min(posiblesValores);
    }

    /**
     * VMM MD-2955: la intensidad de la ayuda que introduce el gestor no debe poder editarse cuando
     * la convocatoria tiene el flag itAyudaCalculada = S (el porcentaje ya viene calculado y no
     * lo debe tocar el gestor). Corregido: el check correcto es este de Convocatoria, no el
     * itMostrarAyuda de la tabla de financiacion tipo AS que se uso al principio por error.
     *
     * @return true si el campo de intensidad debe quedar en solo lectura
     */
    public boolean isIntensidadAyudaSoloLectura() {
        try {
            ConvocatoriaDTO convocatoria = this.convocatoriasBean.getConvocatoriaDTO();
            return convocatoria != null
                    && Constantes.AfirmacionNegacion.S.equals(convocatoria.getItAyudaCalculada());
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     *
     */
    public void guardarObservacionPresupuesto() {
        try {
            // MD-2788: validacion por fila: un mensaje de error por cada fila con importe modificado sin observacion
            Boolean noHayErrores = Boolean.TRUE;
            if (this.notificarFilasPendientesObservacion()) {
                noHayErrores = Boolean.FALSE;
            }

            // VMM 06/10/26 MD-3113: no se guardan conceptos con la descripcion vacia ni repetidos
            if (this.notificarConceptosInvalidos()) {
                noHayErrores = Boolean.FALSE;
            }

            noHayErrores = this.validarMaximoMinimo(noHayErrores);

            if (Boolean.TRUE.equals(noHayErrores)) { // SIN ERRORES

//                if (Boolean.TRUE.equals(
//                        this.expedientePresupService.existeModificacionesParaGuardar(this.lstBloque,
//                                this.lstPresupuestoFinanciable1, this.lstPresupuestoFinanciable2,
//                                this.montoAyudaConcedida))) {

                    for (int i = 0; i < this.lstBloque.size(); i++) {

                        List<ExpedientePresupDTO> presupuestosSinTotal = new ArrayList<>();
                        if (i == 0) {
                            presupuestosSinTotal = this.lstPresupuestoFinanciable1.stream()
                                    .filter(presupuesto -> Objects
                                            .nonNull(presupuesto.getIdExpedientePresup()))
                                    .collect(Collectors.toList());

                        } else {
                            presupuestosSinTotal = this.lstPresupuestoFinanciable2.stream()
                                    .filter(presupuesto -> Objects
                                            .nonNull(presupuesto.getIdExpedientePresup()))
                                    .collect(Collectors.toList());
                        }
                        
                        //MD-2947: comprobamos que no haya quedado ningun importe nulo
                        boolean hayImporteNulo = presupuestosSinTotal.stream()
                                .filter(Objects::nonNull)
                                .map(ExpedientePresupDTO::getLstExpedientePresupMontoDTO)
                                .filter(Objects::nonNull)
                                .flatMap(List::stream)
                                .filter(Objects::nonNull)
                                .anyMatch(montoDTO -> Objects.isNull(montoDTO.getNmImporte()));

                        if (hayImporteNulo) {
                            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_ERROR_IMPORTE_NULO);
                            return;
                        }
                        
                        this.expedientePresupService.guardar(presupuestosSinTotal,
                                this.montoAyudaConcedida, this.montoAyudaSolicitada,
                                this.expedienteBean.getExpedienteDTO());

                        // this.pendienteGuardarObservacion = Boolean.FALSE;
                        this.obtenerPresupuestoFinanciable(i + 1);
                    }

                    AtlasMessageUtils.addInfoMessageFromBundle(
                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_PRESUPUESTO_GRABAR_OK);
//                } else {
//                    AtlasMessageUtils.addErrorMessageFromBundle(
//                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_NO_MODIFICACIONES);
//                }
            }

        } catch (ServiceException e) {
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_PRESUPUESTO_GRABAR_KO);

        }
    }
    
    private Boolean validarMaximoMinimo(Boolean noHayErrores) throws ServiceException {
        if (Boolean.FALSE.equals(this.ocultarAyuda)) {
            double montoMaximo = 0.0;
            //MD-2788: cambiamos el orden de la validación de máximos, ahora si existe monto de ayuda solicitada configurado en ADM 
            //prima sobre el monto de ayuda calculado en base a la intensidad de la tabla presup de ayuda
            if(this.getMontoAyudaPresupuesto() != null) { //este es el monto del bloque con formato "AS" rellenado en el form de solicitud
                montoMaximo = this.montoAyudaPresupuesto;
            }else if(this.getMontoAyudaPresupuestoPresentado() != null) {
                montoMaximo = this.montoAyudaPresupuestoPresentado;
            }
            //MD-2788: añadimos la validación de minimo de "AS" configurada en ADM (si existe este bloque)
            Double montoMinimo = obtenerMontoMinAyuda();
            
            if (Objects.nonNull(this.montoAyudaConcedida)) {
                DecimalFormat df = new DecimalFormat("#0.00");
                df.setRoundingMode(RoundingMode.DOWN);
                
                if (this.montoAyudaConcedida > montoMaximo) {   //Si sobrepasa el maximo de la ayuda --> ERROR                       
                    noHayErrores = Boolean.FALSE;

                    //VMM 04/02/26 MD-2434: redondeamos a dos decimales el mensaje de importe maximo permitido
                    //Object[] msgImporte = new Object[] {"la ayuda concedida en presupuesto ", montoMaximo + " €" };

                    Object[] msgImporte = new Object[] {"la ayuda concedida en presupuesto ",
                        df.format(montoMaximo) + " €"};

                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_KO,
                            msgImporte);
                }
                if (montoMinimo != null && this.montoAyudaConcedida < montoMinimo) {   //Si es menor que el minimo de la ayuda --> ERROR
                    noHayErrores = Boolean.FALSE;
                        
                    Object[] msgImporte = new Object[] {"la ayuda concedida en presupuesto ",
                        df.format(montoMinimo) + " €"};

                    AtlasMessageUtils.addErrorMessageFromBundleParams(
                            ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MINIMO_KO,
                            msgImporte);
                }
            }
        }
        return noHayErrores;
    }


    /**
     * Metodo para obtener los datos de las actuaciones presentadas
     *
     * @param impresoDTO
     */
    private void obtenerActuacionPresentado() {

        try {

            // Obtener registros de la actuación
            List<BloqueComponenteDTO> lstBloqueComponenteActuaDTO = this.bloqueComponenteService
                    .obtenerPorExpediente(this.idExpediente,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES));

            // TODO - implementar logica para actualizar campo itSeleccionado
            if (!lstBloqueComponenteActuaDTO.isEmpty()) {

                Integer idComFinOfeProv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA1_PROVEEDOR);
                
                Integer idBloqueComp = lstBloqueComponenteActuaDTO.stream()
                        .filter(a -> Objects.isNull(a.getFcBajaFila())
                                && Boolean.TRUE.equals(a.getItColumna())
                                && a.getComponenteDTO().getIdComponente().equals(idComFinOfeProv))
                        .findFirst().orElse(new BloqueComponenteDTO()).getIdBloqueComponente();

                this.datoService.buscarDatosByIdExpAndIdBloComp(this.expedienteBean.getIdExpediente(), idBloqueComp, false)
                    .forEach(d -> {
                            Integer nmFila = d.getNmFila() + 1;
                            lstBloqueComponenteActuaDTO.stream()
                            .filter(a -> Objects.isNull(a.getFcBajaFila())
                                    && Boolean.TRUE.equals(a.getItFila())
                                    && nmFila.equals(a.getNmOrden()))
                            .forEach(b -> b.setItSeleccionado(Boolean.TRUE));
                });

                // Construimos la lista de filas actuación
                this.lstBloqueComponenteFilaActua = lstBloqueComponenteActuaDTO.stream()
                        .filter(p -> Objects.isNull(p.getFcBajaFila())
                                && Boolean.TRUE.equals(p.getItFila())
                                && Boolean.TRUE.equals(p.getItSeleccionado()))
                        .collect(Collectors.toList());

                Comparator<BloqueComponenteDTO> compareByIdTipoFinanciacion = (
                        BloqueComponenteDTO c1, BloqueComponenteDTO c2) -> c1
                                .getTipoFinanciacionDTO().getIdValor()
                                .compareTo(c2.getTipoFinanciacionDTO().getIdValor());

                Collections.sort(this.lstBloqueComponenteFilaActua, compareByIdTipoFinanciacion);

                if (!this.lstBloqueComponenteFilaActua.isEmpty()) {
                    
                    Integer idComFinOfeSelProv = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_PROVEEDOR);
                    Integer idComFinOfeSelImp = utilConstantes.getGlobalComponente().get(Constantes.ComponentesFinanciacion.OFERTA_SELECCIONADA_IMPORTE);
                    
                    // Construimos la lista de columnas de la actuación
                    this.lstBloqueComponenteColumnaActua = lstBloqueComponenteActuaDTO.stream()
                            .filter(p -> Objects.isNull(p.getFcBajaFila()) && Boolean.TRUE.equals(p
                                    .getItColumna()
                                    && (Objects.isNull(p.getComponenteDTO().getIdComponente()) ? 0
                                            : p.getComponenteDTO().getIdComponente()) >= idComFinOfeSelProv
                                    && (Objects.isNull(p.getComponenteDTO().getIdComponente()) ? 0
                                            : p.getComponenteDTO().getIdComponente()) <= idComFinOfeSelImp))
                            .collect(Collectors.toList());

                    Comparator<BloqueComponenteDTO> compareByComponenteId = (BloqueComponenteDTO c1,
                            BloqueComponenteDTO c2) -> c1.getComponenteDTO().getIdComponente()
                                    .compareTo(c2.getComponenteDTO().getIdComponente());

                    Collections.sort(this.lstBloqueComponenteColumnaActua, compareByComponenteId);

                    this.obtenerFilColActPresentada();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }

    }

    /* Método para armar las filas y columnas de la actuación presentada */
    private void obtenerFilColActPresentada() throws ServiceException {
        // construimos arreglo con los id bloque componente a recuperar
        List<Integer> lstIdBloqueComponente = new ArrayList<>();

        this.lstBloqueComponenteColumnaActua.stream()
                .forEach(e -> lstIdBloqueComponente.add(e.getIdBloqueComponente()));

        // Recuperar datos de los bloques componente fila
        for (BloqueComponenteDTO fila : this.lstBloqueComponenteFilaActua) {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    this.expedienteBean.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_BLOQUECOMPONENTE_IDBLOQUECOMPONENTE,
                    lstIdBloqueComponente, Operador.IN));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NM_FILA, fila.getNmOrden() - 1,
                    Operador.EQUALS));
            List<DatoDTO> datos = this.datoService.buscar(0, -1, filtros, null);
            Comparator<DatoDTO> compareByDatos = (DatoDTO c1, DatoDTO c2) -> c1.getNmColumna()
                    .compareTo(c2.getNmColumna());
            Collections.sort(datos, compareByDatos);
            // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
            Utils.normalizarSeparadorDecimalDatos(datos);
            fila.setDatos(datos);
        }

        // Construimos lista con idColumna
        int[] nColArray = IntStream.range(0, this.lstBloqueComponenteColumnaActua.size()).toArray();
        this.listaNcolActuacion = new ArrayList<>();

        for (int arr : nColArray) {
            this.getListaNcolActuacion().add(arr);
        }

        // Preparamos espacio para totales por fila
        BloqueComponenteDTO bloqueComponenteTotalFila = new BloqueComponenteDTO();
        bloqueComponenteTotalFila.setTipoFinanciacionDTO(new CatalogosValoresDTO());
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setCdCodigo("");
        bloqueComponenteTotalFila.getTipoFinanciacionDTO().setDsValor("");
        bloqueComponenteTotalFila.setDatos(new ArrayList<>());

        // Calculamos los montos totales por columna
        double sumaColumna = 0;
        // referenciamos la posición de la última columna
        int i = this.getListaNcolActuacion().size() - 1;

        for (BloqueComponenteDTO fila : this.lstBloqueComponenteFilaActua) {
            if (StringUtils.isNotBlank(fila.getDatos().get(i).getDsValor())) {
                sumaColumna += this.parseImporteDsValor(fila.getDatos().get(i).getDsValor());
            }
        }

        DatoDTO dto = new DatoDTO();

        dto.setDsValor("TOTAL");

        bloqueComponenteTotalFila.getDatos().add(dto);

        dto = new DatoDTO();
        dto.setDsValor(String.valueOf(sumaColumna));
        dto.setEsMontoTotal(Boolean.TRUE);

        bloqueComponenteTotalFila.getDatos().add(dto);

        this.lstActuacionPresentado = this.lstBloqueComponenteFilaActua;
        this.lstBloqueComponenteFilaActua.add(bloqueComponenteTotalFila);
    }

    /**
     * Método para obtener los datos de las actuaciones financiables
     */
    private void obtenerActuacionFinanciable() {

        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                    this.expedienteBean.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<AtlasSort> orden = new ArrayList<>();
            orden.add(new AtlasSort(Constantes.Filtro.FILTRO_EXPEDIENTE_PRESU_TIPO_FINANCIACION,
                    true));

            this.lstActuacionFinanciable = this.expedienteActuaService.buscar(0, -1, filtros,
                    orden);

            if (!this.lstActuacionFinanciable.isEmpty()) {
                this.calcularTotalActuacion();
                // agregamos el total
                ExpedienteActuaDTO expedienteActuaDTO = new ExpedienteActuaDTO();
                expedienteActuaDTO.setEsMontoTotal(Boolean.TRUE);
                expedienteActuaDTO.setDsProveedor("TOTAL");
                expedienteActuaDTO.setNmImporte(this.montoTotalActuacion);

                this.montoTotalActuacionAux = this.montoTotalActuacion;
                this.lstActuacionFinanciable.add(expedienteActuaDTO);

                this.montoAyudaConcedidaActuacion = this.lstActuacionFinanciable.get(0)
                        .getNmAyuda();

                this.obtenerImpMaxAct();

                // seteamos la observacion
                this.tlObservacion = this.lstActuacionFinanciable.get(0).getTlObservacion();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /* Método para cargar los datos de la actuación */
    public void cargarDatosActuacion() {
        try {
            this.lstActuacionFinanciable = new ArrayList<>();
            this.expedienteActuaService.cargarDatos(this.lstActuacionPresentado,
                    this.montoAyudaActuacion, this.expedienteBean.getIdExpediente());

            this.obtenerActuacionFinanciable();

            this.validarEstilo();

            this.esconderBotonCargarActuaciones();
            PrimeFaces.current().ajax().update(this.solapaFinanciacion);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /* Método para calcular el monto de la ayuda concedida - Actuación */
    private void calcularTotalActuacion() {

        this.montoTotalActuacion = 0.0;

        for (ExpedienteActuaDTO fila : this.lstActuacionFinanciable) {
            if (Objects.nonNull(fila.getNmImporte())
                    && Objects.nonNull(fila.getIdExpedienteActua())) {
                this.montoTotalActuacion += fila.getNmImporte();
            }
        }
    }

    /*
     * Método que captura el evento de cambiar celda
     *
     * @param ExpedienteActuaDTO filaEditada
     */
    public void edicionCeldaActuacion(ExpedienteActuaDTO filaEditada) {

        try {
            int indexTotal = this.lstActuacionFinanciable.size() - 1;
            this.calcularTotalActuacion();
            this.verMsgObsActuacion = Boolean.TRUE;

            if (Boolean.TRUE.equals(this.montoMaxPermitidoAct(filaEditada))) {

                for (ExpedienteActuaDTO act : this.lstActuacionFinanciable) {
                    if (act.getIdExpedienteActua().equals(filaEditada.getIdExpedienteActua())) {
                        act.setNmImporte(this.montoOriginal);
                        break;
                    }
                }
                this.calcularTotalActuacion();
            }

            this.lstActuacionFinanciable.get(indexTotal).setNmImporte(this.montoTotalActuacion);
            this.verMsgObsActuacion = Boolean.TRUE;
            PrimeFaces.current().ajax().update(this.tablaActuacionFinanciable);
            PrimeFaces.current().ajax().update(this.mensajeAdvertenciaActu);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    /*
     * Método que captura el evento de cambiar celda
     *
     * @param ExpedienteActuaDTO filaEditada
     */
    public void edicionMontoConceAct() {
        try {

            if (Objects.isNull(this.montoAyudaConcedidaActuacion)) {
                this.montoAyudaConcedidaActuacion = 0.0;
            }

            if (this.montoAyudaActuacion != null && this.montoAyudaConcedidaActuacion > this.montoAyudaActuacion) {

                //VMM 04/02/26 MD-2434: redondeamos a dos decimales el mensaje de importe maximo permitido
                //Object[] msgImporte = new Object[] {"la ayuda concedida en actuaciones ",
                //        this.montoAyudaActuacion + " €" };
                
                DecimalFormat df = new DecimalFormat("#0.00");
                df.setRoundingMode(RoundingMode.DOWN);

                Object[] msgImporte = new Object[] {"la ayuda concedida en actuaciones ",
                    df.format(this.montoAyudaActuacion) + " €"};

                AtlasMessageUtils.addErrorMessageFromBundleParams(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_KO,
                        msgImporte);

                if (!this.lstActuacionFinanciable.isEmpty()) {
                    this.montoAyudaConcedidaActuacion = this.lstActuacionFinanciable.get(0)
                            .getNmAyuda();
                }
            }

            this.verMsgObsActuacion = Boolean.TRUE;
            PrimeFaces.current().ajax().update(this.mensajeAdvertenciaActu);

        } catch (Exception e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }

    // obtener importe máximo actuación
    private void obtenerImpMaxAct() throws ServiceException {

        List<String> lstIdTipo = new ArrayList<>();
        lstIdTipo.add(Constantes.Formatos.FINANCIACION_ACTUACIONES);
        lstIdTipo.add(Constantes.Formatos.AYUDA_SOLICITADA);

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,
                this.expedienteBean.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_TIPO, lstIdTipo, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<FinanciacionDTO> lstFinanciacion = this.financiacionService.buscar(0, -1, filtros,
                null);

        this.montoMaxAyudaConceAct = lstFinanciacion.stream()
                .filter(p -> Objects.isNull(p.getFcBajaFila())
                        && p.getItTipo().equals(Constantes.Formatos.AYUDA_SOLICITADA))
                .collect(Collectors.toList()).get(0).getListaFinanciacionDatoDTO().get(0)
                .getNmImporteMax();

        lstFinanciacion.removeIf(x -> x.getItTipo().equals(Constantes.Formatos.AYUDA_SOLICITADA));

        this.lstImpMaxAct = lstFinanciacion.get(0).getListaFinanciacionDatoDTO().stream()
                .filter(p -> Objects.isNull(p.getFcBajaFila())).collect(Collectors.toList());

    }

    /* Método para guardar los datos de la actuación */
    public void guardarDatosActuacion() {
        try {

            if (Boolean.TRUE.equals(this.validarGuardarActuacion())) {

                this.expedienteActuaService.guardarConHistorico(this.lstActuacionFinanciable,
                        this.tlObservacion, this.montoAyudaConcedidaActuacion);

                this.verMsgObsActuacion = Boolean.FALSE;
                AtlasMessageUtils.addInfoMessageFromBundle(
                        ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_ACTUACION_GRABAR_OK);

                this.obtenerActuacionFinanciable();
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANACIACION_ACTUACION_GRABAR_KO);
        }
    }

    private Boolean validarGuardarActuacion() {

        Boolean valido = Boolean.TRUE;

        if (this.tlObservacion.isEmpty()) {
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_OBSERVACION_ACTUACION);
            valido = Boolean.FALSE;
        }
        if (this.montoAyudaConcedidaActuacion != null && this.montoAyudaActuacion != null
                && this.montoAyudaConcedidaActuacion > this.montoAyudaActuacion) {
            this.edicionMontoConceAct();
            valido = Boolean.FALSE;
        }

        for (ExpedienteActuaDTO act : this.lstActuacionFinanciable) {
            if (Boolean.FALSE.equals(act.getEsMontoTotal())
                    && Boolean.TRUE.equals(this.montoMaxPermitidoAct(act))) {
                valido = Boolean.FALSE;
                break;
            }
        }
        return valido;
    }

    /**
     * Metodo para validar si el monto ingresado es menor que el monto máximo
     * permitido
     *
     * @param ExpedienteActuaDTO filaEditada
     */
    private Boolean montoMaxPermitidoAct(ExpedienteActuaDTO filaEditada) {

        Boolean montoMax = Boolean.FALSE;

        String cdCodigoFilaEditada = filaEditada.getTipoFinanciacionDTO() != null
                ? filaEditada.getTipoFinanciacionDTO().getCdCodigo() : null;
        String importe = this.lstBloqueComponenteFilaActua.stream()
                .filter(fila -> Objects.equals(
                        fila.getTipoFinanciacionDTO() != null ? fila.getTipoFinanciacionDTO().getCdCodigo() : null,
                        cdCodigoFilaEditada))
                .findFirst()
                .filter(fila -> fila.getDatos() != null && fila.getDatos().size() > 1)
                .map(fila -> fila.getDatos().get(1).getDsValor())
                .orElse(null);

        //Double nmImporteMax = StringUtils.isNotBlank(importe) ? Double.parseDouble(importe): 999999.99;
        Double nmImporteMax = StringUtils.isNotBlank(importe) ? this.parseImporteDsValor(importe): 9999999999.99;
        
        if (Objects.isNull(filaEditada.getNmImporte())) {
            filaEditada.setNmImporte(0.0);
        }

        if (filaEditada.getNmImporte() > nmImporteMax) {
            montoMax = Boolean.TRUE;

            this.montoOriginal = filaEditada.getNmImporte()
                    - (this.montoTotalActuacion - this.montoTotalActuacionAux);

            //VMM 04/02/26 MD-2434: redondeamos a dos decimales el mensaje de importe maximo permitido
            //Object[] msgImporte = new Object[] {
             // "la actuación " + filaEditada.getTipoFinanciacionDTO().getCdCodigo(),
             // nmImporteMax + " €" };
            
            DecimalFormat df = new DecimalFormat("#0.00");
            df.setRoundingMode(RoundingMode.DOWN);

            Object[] msgImporte = new Object[] {
                "la actuación " + filaEditada.getTipoFinanciacionDTO().getCdCodigo(),
                df.format(nmImporteMax) + " €"};

            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_IMPORTE_MAXIMO_KO, msgImporte);
        }

        return montoMax;
    }

    /**
     * VMM 08/09/26 MD-2996 (reabierta): indica si el expediente está en estado Rechazado
     * Definitivo, caso en el que se deben deshabilitar las acciones que impliquen modificar datos
     * en esta solapa (cargar/guardar datos de presupuesto o actuación, añadir/eliminar concepto,
     * finalizar revisión), dejando habilitadas las de solo lectura (descargar, ver histórico).
     * Se aplica solo en los botones de acción concretos (a nivel de xhtml) para no contaminar los
     * flags compartidos (modoConsultaFinanciacion, esconderBotonXxx) que también gobiernan otros
     * elementos de la pantalla.
     * <p>
     * VMM 24/09/26 MD-3032: se amplía a Exclusión Definitiva (EXCLUIDO) y Desistimiento Definitivo
     * (DESISTIDO) - son, junto con Rechazado Definitivo, los otros dos estados terminales en los
     * que las observaciones y los botones de añadir/eliminar concepto quedaban erróneamente activos
     * (EXCLUIDO/DESISTIDO son ya las versiones definitivas, existen constantes distintas
     * EXCLUIDO_PROVISIONAL/DESISTIDO_PROVISIONAL para las provisionales).
     *
     * @return true si el expediente está Rechazado Definitivo, Excluido o Desistido
     */
    public boolean isExpedienteRechazadoDefinitivo() {
        if (this.expedienteBean == null || this.expedienteBean.getExpedienteDTO() == null
                || this.expedienteBean.getExpedienteDTO().getEstadoDTO() == null) {
            return false;
        }
        Integer idEstado = this.expedienteBean.getExpedienteDTO().getEstadoDTO().getIdValor();
        return idEstado != null
                && (idEstado.equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO))
                        || idEstado.equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.EXCLUIDO))
                        || idEstado.equals(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoExpediente.DESISTIDO)));
    }

    /* Presenta la información en modo de consulta */

    public void esModoConsulta() {

        if (Boolean.TRUE.equals(this.convocatoriasBean.isModoConsulta())) {
            this.modoConsultaFinanciacion = Boolean.TRUE;
        } else {
            List<Integer> lstEstadoExpediente = new ArrayList<>();
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo también deja la financiación en modo consulta
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
            // VMM 05/10/26 MD-2607: exclusion provisional tambien deja la financiacion en modo consulta
            // (el estado del expediente puede ser EXCLUIDOS_PROVISIONAL sin que estadoRevRequi ni
            // estadoProvisional lo sean, p.ej. si viene de la revision administrativa)
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
            // VMM 05/10/26 MD-2607: igual con desistimiento provisional
            lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

            long existeEstado = lstEstadoExpediente.stream()
                    .filter(x -> x.equals(this.idEstadoExpediente)).count();

             //VMM 28/07/25 MD-1687: ponemos el modo consulta de financiacion si: 1.el expdte está en alguno de esos estados, 
             //2. si todas las tablas presup que existen están en estado financiacion finalizada OK
             if (existeEstado == 1 ||
                 (this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor() != null && 
                 utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)
                     .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor()))) {
                 this.modoConsultaFinanciacion = Boolean.TRUE;
             }else {
                 this.modoConsultaFinanciacion = Boolean.FALSE;
             }
        }
    }

    /**
     * Metodo para ocultar el boton
     */
    public void esconderBotonCargarPresup() {
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite cargar datos de presupuesto
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        // VMM 05/10/26 MD-2607: exclusion y desistimiento provisional, no se permite cargar datos de presupuesto
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        // MD-2147 determinamos si el estado de rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));

        // MD-2397 determinamos si el estado de rev adm se encuentra en desestimado provisional
        Boolean desistidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))
                        : Boolean.FALSE;
        
        // MD-2399 determinamos so el estado de rev req se encuentra en excluido
        // provisional
        Boolean estadoPrivisionalExcluidoProvisional = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))
                        : Boolean.FALSE;
                
        this.esconderBotonCargarDatoPresup = lstEstadoExpediente.contains(this.idEstadoExpediente)
                || !this.lstPresupuestoFinanciable1.isEmpty()
                || !this.lstPresupuestoFinanciable2.isEmpty()
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 Se incluye condicion de excluido provisional de la rev req
                || Boolean.TRUE.equals(desistidoProvisionalEnviado) // MD-2397 se incluye desistido provisional en id_provisional
                || Boolean.TRUE.equals(estadoPrivisionalExcluidoProvisional) // MD-2399 Incluimos exluido provisonal del estado provisional
                || this.convocatoriasBean.isModoConsulta(); //VMM 05/02/26 MD-2293: escondemos boton cargar si es rol auditor
        
        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgCargarDatos");

        // VMM 05/10/26 MD-3098: ver comentario en esPeticionTablaHistoricoPresupuesto
        if (!this.esPeticionTablaHistoricoPresupuesto()) {
            PrimeFaces.current().ajax().update(
                    "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelFinanciacion");
        }
    }

    /**
     * VMM 05/10/26 MD-3098: indica si la peticion ajax actual la ha lanzado la tabla del Historico
     * de presupuesto (ordenar, paginar, filtrar). Este metodo (via esconderBotonCargarPresup /
     * esconderBotonFinalizarRevision) se ejecuta tambien desde cargaDatos(), que se evalua en el
     * render de panelFinanciacion en CUALQUIER ajax que pinte algo dentro de ese panel; ahi se
     * anadia panelFinanciacion entero a la respuesta, y como la tabla paginada solo manda sus
     * filas (<tr>) estas llegaban huerfanas y el navegador las descartaba (tabla rota).
     */
    private boolean esPeticionTablaHistoricoPresupuesto() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return false;
        }
        String origen = facesContext.getExternalContext().getRequestParameterMap()
                .get("javax.faces.source");
        return origen != null && origen.contains("listaHistoricoPresupuesto");
    }

    /**
     * Metodo para ocultar el boton actuaciones
     */
    private void esconderBotonCargarActuaciones() {
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite esta acción
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        // VMM 05/10/26 MD-2607: exclusion y desistimiento provisional, no se permite esta accion
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        // MD-2147 determinamos si el estado de rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
        // MD-2397 determinamos si el estado de rev adm se encuentra en desestimado provisional
        Boolean desistidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))
                        : Boolean.FALSE;
        
        // MD-2399 determinamos so el estado de rev req se encuentra en excluido
        // provisional
        Boolean estadoPrivisionalExcluidoProvisional = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))
                        : Boolean.FALSE;

        this.esconderBotonCargarDatoActuacion = lstEstadoExpediente
                .contains(this.idEstadoExpediente) || !this.lstActuacionFinanciable.isEmpty()
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 Se incluye condicion de excluido provisional de la rev req
                || Boolean.TRUE.equals(desistidoProvisionalEnviado) // MD-2397 se incluye desistido provisional el id_provisional
                || Boolean.TRUE.equals(estadoPrivisionalExcluidoProvisional); // MD-2399 Incluimos excluido provisonal del estado provisional

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgCargarDatosActu");

        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelFinanciacion");

    }
    
    /**
     * Metodo para ocultar el boton
     */
    private void esconderBotonFinalizarRevision() {
        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        // VMM 08/09/26 MD-2996 (reabierta): rechazado definitivo, no se permite esta acción
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        // VMM 05/10/26 MD-2607: exclusion y desistimiento provisional, no se permite esta accion
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));

        // MD-2147 determinamos si el estado de rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
     // MD-2148 determinamos si el estado de rev adm se encuentra en desistido provisional
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        
        // MD-2397 determinamos si el estado provisional de rev adm se encuentra en desestimado provisional
        Boolean estadoProvisionalDesistido = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))
                        : Boolean.FALSE;
        
        // MD-2399 determinamos so el estado de rev req se encuentra en excluido
        // provisional
        Boolean estadoPrivisionalExcluidoProvisional = this.expedienteBean.getExpedienteDTO()
                .getEstadoProvisionalDTO().getIdValor() != null
                        ? this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))
                        : Boolean.FALSE;

        //VMM 28/07/25 MD-1687: escondemos el boton de Finalizar Revision si: 1. el expdte este en uno de estos estados, 
        //2. no hay tablas de presup financiables, 3. hay tablas de presup finan pero ya están en estado FINALIZADO OK
        this.esconderBotonFinalizarRevision = lstEstadoExpediente.contains(this.idEstadoExpediente)
                || (this.lstPresupuestoFinanciable1.isEmpty()
                && this.lstPresupuestoFinanciable2.isEmpty())
                || (this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor() != null &&
                 utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)
                .equals(this.expedienteBean.getExpedienteDTO().getEstadoRevFinanDTO().getIdValor())
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 Se incluye condicion de excluido provisional de la rev req
                || Boolean.TRUE.equals(desisitidoProvisonalEnviado) // MD-2148 incluimos la condición de desistido provisional la rev adm
                || Boolean.TRUE.equals(estadoProvisionalDesistido) // MD-2397 determinamos si el estado provisional de rev adm se encuentra en desestimado provisional
                || Boolean.TRUE.equals(estadoPrivisionalExcluidoProvisional)); // MD-2399 Incluimos excluido provisonal del estado provisional
        PrimeFaces.current().ajax().update(
                "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:pgFinalizarRevision");

        // VMM 05/10/26 MD-3098: ver comentario en esPeticionTablaHistoricoPresupuesto
        if (!this.esPeticionTablaHistoricoPresupuesto()) {
            PrimeFaces.current().ajax().update(
                    "convocatoriaForm:contenedorSolapas:contenedorRevisionSolapas:panelFinanciacion");
        }
    }

    /**
     * Método para mostrar la ventana popup con el histórico de presupuesto
     *
     * @param presupuesto
     */
    public void mostrarHistoricoActuacion() {
        PrimeFaces.current().executeScript("PF('dialogHistoricoActuacion').show();");
    }

    /**
     * Método para cerrar la ventana popup de históricos de actuacion
     */
    public void cerrarHistoricoActuacion() {
        PrimeFaces.current().executeScript("PF('dialogHistoricoActuacion').hide();");
    }

    /**
     * Método para obtener la lista de históricos de presupuesto
     *
     * @param index Indice
     * @param pageSize Cantidad páginas
     * @param orderBean Ordenamiento
     * @param filtros Filtros
     * @return Lista de histórico de presupuesto
     */
    @SuppressWarnings("unchecked")
    public AtlasListAndCount<ExpedientePresupHistDTO> obtenerListaHistoricoPresupuesto(
            final int index, final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedientePresupHistDTO> listadoDatos = new AtlasListAndCount<>();
        try {

            if (Objects.nonNull(this.expedienteBean.getIdExpediente())) {
                List<ExpedientePresupHistDTO> listado = this.expedientePresupHistService
                        .obtenerListaPresupuestoHist(this.expedienteBean.getIdExpediente());

                // Deduplicar por fecha y observación (misma fecha+observacion con distinto estado aparecen como duplicados)
                List<ExpedientePresupHistDTO> listadoSinDuplicados = new ArrayList<>(listado.stream()
                        .collect(Collectors.toMap(
                                h -> h.getFcAltaFila() + "_" + h.getTlObservacion(),
                                h -> h,
                                (existing, replacement) -> existing,
                                java.util.LinkedHashMap::new))
                        .values());

                // VMM 06/10/26 MD-3098: el orden que pide la tabla (orderBean) se ignoraba, asi que
                // pulsar sobre la cabecera de Fecha de modificacion u Observacion no hacia nada. Se
                // ordena en memoria (la lista ya esta completa, se pagina despues) por la columna
                // pulsada; las claves coinciden con el 'field' de cada atlasp:columnaListaPaginada del
                // historico en solapaFinanciacion.xhtml. Sin orden, se respeta el de la consulta.
                if (orderBean instanceof List && !((List<?>) orderBean).isEmpty()) {
                    AtlasSort orden = ((List<AtlasSort>) orderBean).get(0);
                    Comparator<ExpedientePresupHistDTO> comparador = null;
                    if ("fcAltaFila".equals(orden.getProperty())) {
                        comparador = Comparator.comparing(ExpedientePresupHistDTO::getFcAltaFila,
                                Comparator.nullsLast(Comparator.<Date>naturalOrder()));
                    } else if ("tlObservacion".equals(orden.getProperty())) {
                        comparador = Comparator.comparing(ExpedientePresupHistDTO::getTlObservacion,
                                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
                    }
                    if (comparador != null) {
                        listadoSinDuplicados.sort(orden.isAscendente() ? comparador : comparador.reversed());
                    }
                }

                List<ExpedientePresupHistDTO> listaPaginada = this.expedientePresupHistService
                        .retornarPaginado(listadoSinDuplicados, index, pageSize);

                listadoDatos.setResultados(listaPaginada);
                listadoDatos.setTotal(listadoSinDuplicados.size());
            }

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listadoDatos;
    }

    /**
     * Método para obtener la lista de históricos de actuacion
     *
     * @param index Indice
     * @param pageSize Cantidad páginas
     * @param orderBean Ordenamiento
     * @param filtros Filtros
     * @return Lista de histórico de actuacion
     */
    public AtlasListAndCount<ExpedienteActuaHistDTO> obtenerListaHistoricoActuacion(final int index,
            final int pageSize, Object orderBean, Object filtros) {
        AtlasListAndCount<ExpedienteActuaHistDTO> listadoDatos = new AtlasListAndCount<>();

        try {

            List<ExpedienteActuaHistDTO> listado = this.expedienteActuaHistService
                    .obtenerListaActuacionHist(this.expedienteBean.getIdExpediente());
            List<ExpedienteActuaHistDTO> listaPaginada = this.expedienteActuaHistService
                    .retornarPaginado(listado, index, pageSize);

            listadoDatos.setResultados(listaPaginada);
            listadoDatos.setTotal(listado.size());

        } catch (ServiceException exc) {
            log.error(ConstantesWeb.Err.ERROR_LISTADO);
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_LISTADO);
        }
        return listadoDatos;
    }

   /* public Date obtenerFechaEjecucionInicialAnualidad(Integer anualidad) {
        if (!Objects.isNull(anualidad) && !Objects.isNull(this.listaExpAnualDTO)
                && this.listaExpAnualDTO.size() >= anualidad) {
            Optional<ExpedienteAnualidadDTO> opt = this.listaExpAnualDTO.stream()
                    .filter(x -> anualidad.equals( x.getNmAnualidad())).findFirst();
            if (opt.isPresent())
                return opt.get().getFcIniEjecucion();
        }
        return null;
    }

    public Date obtenerFechaEjecucionFinalAnualidad(Integer anualidad) {
        if (!Objects.isNull(anualidad) && !Objects.isNull(this.listaExpAnualDTO)
                && this.listaExpAnualDTO.size() >= anualidad) {
            Optional<ExpedienteAnualidadDTO> opt = this.listaExpAnualDTO.stream()
                    .filter(x ->  anualidad.equals(x.getNmAnualidad())).findFirst();
            if (opt.isPresent())
                return opt.get().getFcFinEjecucion();
        }
        return null;
    }*/

    public void guardarFechasEjecucion() {
        try {
            for (ExpedienteAnualidadDTO expediente:this.listaExpAnualDTO) {
                expedienteAnualidadService.guardar(expediente);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Err.ERROR_GENERAL);
        }
    }
    
    
    public boolean mostrarBotonGuardar(Integer idEstadoFinan) {
        Boolean mostrar = Boolean.TRUE;
        
        // MD-2147 determinamos si el estado de rev req se encuentra en excluido provisional
        Boolean excluidoProvisionalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevRequiDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL));
        
     // MD-2148 determinamos si el estado de rev admin se encuantra en el estado desisitido provisional
        Boolean desisitidoProvisonalEnviado = this.expedienteBean.getExpedienteDTO()
                .getEstadoRevAdminDTO().getIdValor()
                .equals(utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL));
        
        // añadimos que no se muestre el boton de guardar si la tabla/s de presup finan está/n sin cargar (vacia),
        // con tener la primer cargada me vale la comprobacion porque se carga todo a la vez
        if ((idEstadoFinan != null && idEstadoFinan.equals(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)) || 
                this.lstPresupuestoFinanciable1 == null || this.lstPresupuestoFinanciable1.isEmpty()
                || Boolean.TRUE.equals(excluidoProvisionalEnviado) // MD-2147 Se incluye condicion de excluido provisional de la rev req
                || Boolean.TRUE.equals(desisitidoProvisonalEnviado)) { // MD-2148 incluimos la condición de rev admin en el estado de desistido provisional
            mostrar = Boolean.FALSE;
        }
        return mostrar;
    }
    
    public boolean mostrarPresupuestoProvisional (Integer idEstadoFinan) {
        Boolean mostrar = Boolean.FALSE;
        
        if ( idEstadoFinan != null && idEstadoFinan.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION)) 
                && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO().getIdValor() != null && this.expedienteBean.getExpedienteDTO().getEstadoProvisionalDTO() != null
                && this.expedienteBean.getExpedienteDTO().getItAlegaciones().equals(Constantes.AfirmacionNegacion.S)
                && this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada() != null
                && this.expedienteBean.getExpedienteDTO().getItAlegacionRevisada().equals(Constantes.AfirmacionNegacion.S)) {
            mostrar = Boolean.TRUE;
        }
        return mostrar;
    }
    
    public boolean isMostrarTablaAyuda(Integer nmPresup) throws ServiceException {
        boolean esMostrarAyuda = true;
        FinanciacionDTO financiacionDTO = null;
        
        Integer idTematica = this.lstBloque != null
                && this.lstBloque.get(nmPresup-1) != null
                && this.lstBloque.get(nmPresup-1).getIdTematica() != null
                && this.lstBloque.get(nmPresup-1).getIdTematica().getIdValor() != null
                        ? this.lstBloque.get(nmPresup-1).getIdTematica().getIdValor()
                        : null;
        
        //VMM 18/05/26: mostrar tabla de ayuda o no en caso de presup presentados sin haber cargado al expedte
       /* if (nmPresup == 1 && this.lstBloque != null && !this.lstBloque.isEmpty()){
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                    idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
            //VMM 01/06/26 MD-2731: mostrar tabla de ayuda 2 si corresponde
        }else if (nmPresup == 2 && this.lstBloque != null && !this.lstBloque.isEmpty() && this.lstBloque.get(1) != null){
            financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                    idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
        }*/
        
        financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP", this.expedienteBean.getIdConvocatoria(),
                idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());
        
        if (financiacionDTO != null && !Constantes.AfirmacionNegacion.S.equals(financiacionDTO.getItMostrarAyuda())) {
            esMostrarAyuda = false;
        }
        
        return porcentajeIntensidadAyuda != null && porcentajeIntensidadAyuda != 100 && esMostrarAyuda;
    }
    
    public boolean isMostrarTablaAyudaFinan(Integer nmPresup) throws ServiceException {
        boolean esMostrarAyuda = true;
        FinanciacionDTO financiacionDTO = null;
        Integer idTematica = null;

        //VMM 05/05/26 MD-2722: ponemos las tablas de presupuestos en base a su numeracion correcta
        if (nmPresup == 1 && this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty()) {
            idTematica = this.lstPresupuestoFinanciable1.get(0) != null
                    && this.lstPresupuestoFinanciable1.get(0).getTematicaDTO() != null
                            ? this.lstPresupuestoFinanciable1.get(0).getTematicaDTO().getIdValor()
                            : null;
        }else if (nmPresup == 2 && this.lstPresupuestoFinanciable2 != null && !this.lstPresupuestoFinanciable2.isEmpty()) {
            idTematica = this.lstPresupuestoFinanciable2.get(0) != null
                    && this.lstPresupuestoFinanciable2.get(0).getTematicaDTO() != null
                            ? this.lstPresupuestoFinanciable2.get(0).getTematicaDTO().getIdValor()
                            : null;
        }else if (nmPresup == 3 && this.lstPresupuestoFinanciable3 != null && !this.lstPresupuestoFinanciable3.isEmpty()) {
            idTematica = this.lstPresupuestoFinanciable3.get(0) != null
                    && this.lstPresupuestoFinanciable3.get(0).getTematicaDTO() != null
                            ? this.lstPresupuestoFinanciable3.get(0).getTematicaDTO().getIdValor()
                            : null;
            //VMM 18/05/26: mostrar tabla de ayuda o no en caso de presup presentados sin haber cargado al expedte (creo que esto ya no hace falta)
        }/*else if (nmPresup == 1 && this.lstBloque != null && !this.lstBloque.isEmpty()){
            idTematica = this.lstBloque.get(0).getIdTematica() != null
                    && this.lstBloque.get(0).getIdTematica().getIdValor() != null
                            ? this.lstBloque.get(0).getIdTematica().getIdValor()
                            : (this.lstBloque.get(0).getIdTematicaPadre() != null
                                    ? this.lstBloque.get(0).getIdTematicaPadre().getIdValor()
                                    : null);
            //VMM 01/06/26 MD-2731: mostrar tabla de ayuda 2 si corresponde
        }else if (nmPresup == 2 && this.lstBloque != null && !this.lstBloque.isEmpty() && this.lstBloque.get(1) != null){
            idTematica = this.lstBloque.get(1).getIdTematica() != null
                    && this.lstBloque.get(1).getIdTematica().getIdValor() != null
                            ? this.lstBloque.get(1).getIdTematica().getIdValor()
                            : (this.lstBloque.get(1).getIdTematicaPadre() != null
                                    ? this.lstBloque.get(1).getIdTematicaPadre().getIdValor()
                                    : null);
        }*/
        
        financiacionDTO = this.informeHelperPdfService.obtenerFinanciacion("PP",this.expedienteBean.getIdConvocatoria(),
                idTematica, this.expedienteBean.getExpedienteDTO().getConvocatoriaActDTO().getIdActor());

        if (financiacionDTO != null && !Constantes.AfirmacionNegacion.S.equals(financiacionDTO.getItMostrarAyuda())) {
            esMostrarAyuda = false;
        }

        return porcentajeIntensidadGestor != null && porcentajeIntensidadGestor != 100 && esMostrarAyuda;
    }

    /**
     * Calcula la suma vertical de todos los conceptos de una partida específica para una columna dada.
     */
    public Double getSumaGrupo(CatalogosValoresDTO partida, int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || partida == null) {
            return 0.0;
        }

        Double resultado = lstPresup.stream()
        .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                        && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
        .mapToDouble(p -> {
            // nCol es el índice que viene del front (ya ajustado con el ternario)
            if (p.getLstExpedientePresupMontoDTO() != null && nCol < p.getLstExpedientePresupMontoDTO().size()
                    && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
                Double imp = p.getLstExpedientePresupMontoDTO().get(nCol).getNmImporte();
                return (imp != null) ? imp : 0.0;
            }
            return 0.0;
        }).sum();
        
        return resultado;
    }

    /**
     * Calcula el total acumulado de un grupo (Suma de todas sus anualidades).
     */
    public Double getSumaTotalGrupo(CatalogosValoresDTO partida, List<ExpedientePresupDTO> lstPresup) {
        // Si no hay ID de partida o no hay columnas, el total es 0
        if (this.listaAnualidades == null || partida == null || lstPresup == null) {
            return 0.0;
        }
        
        double total = 0.0;
        // Indice 0: Concepto (vacío), Indice 1: Anualidad 1, Indice 2: TOTAL.
        int indexTotal = this.listaAnualidades.size() + 1;
        
        // Filtramos la lista principal por la partida actual y sumamos el índice del total
        total = lstPresup.stream()
                .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                            && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                .mapToDouble(p -> {
                    if (p.getLstExpedientePresupMontoDTO() != null && indexTotal < p.getLstExpedientePresupMontoDTO().size()
                            && p.getIdExpedientePresup() != null) { //para que no coja el ultimo registro de la fila TOTAL
                        Double imp = p.getLstExpedientePresupMontoDTO().get(indexTotal).getNmImporte();
                        return (imp != null) ? imp : 0.0;
                    }
                    return 0.0;
                }).sum();
        
        return total;
    }
    
    // Suma vertical de una columna (anualidad) específica para TODAS las partidas
    public Double getGranTotalVertical(int nCol, List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null) return 0.0;
        
        Double resul = lstPresup.stream()
        .filter(p -> p.getLstExpedientePresupMontoDTO() != null && nCol < p.getLstExpedientePresupMontoDTO().size()
                    && p.getIdExpedientePresup() != null) //para que no coja el ultimo registro de la fila TOTAL
        .mapToDouble(p -> {
            Double imp = p.getLstExpedientePresupMontoDTO().get(nCol).getNmImporte();
            return (imp != null) ? imp : 0.0;
        })
        .sum();
        
        return resul;
    }
    
    // El Gran Total Final es simplemente la suma vertical de la columna de "TOTALES"
    public Double getGranTotalFinal(List<ExpedientePresupDTO> lstPresup) {
        if (lstPresup == null || this.listaAnualidades == null) {
            return 0.0;
        }
        
        // Si la anualidad es la 1 (índice 1) y el total es el índice 2...
        // El índice del total horizontal siempre es el tamaño de la lista de anualidades + (conConceptos? 1 : 0)
        int indexTotalHorizontal = this.listaAnualidades.size() + 1;  
        double granTotal = getGranTotalVertical(indexTotalHorizontal, lstPresup);
        
        return granTotal;
    }
    
    
    /**
     * Calcula la suma vertical de todos los conceptos de una partida específica para una columna dada.
     */
    public Double getSumaGrupoPres(CatalogosValoresDTO partida, int nCol, List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null || partida == null) {
            return 0.0;
        }

        Double resultado = lstPresup.stream()
        .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                        && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
        .mapToDouble(p -> {
            // nCol es el índice que viene del front (ya ajustado con el ternario)
            if (p.getDatos() != null && nCol < p.getDatos().size()
                    && p.getIdBloqueComponente() != null) { //para que no coja el ultimo registro de la fila TOTAL
                return parseImporteDsValor(p.getDatos().get(nCol).getDsValor());
            }
            return 0.0;
        }).sum();

        return resultado;
    }

    /**
     * Calcula el total acumulado de un grupo (Suma de todas sus anualidades).
     */
    public Double getSumaTotalGrupoPres(CatalogosValoresDTO partida, List<BloqueComponenteDTO> lstPresup) {
        // Si no hay ID de partida o no hay columnas, el total es 0
        if (this.listaAnualidades == null || partida == null || lstPresup == null) {
            return 0.0;
        }

        double total = 0.0;
        // Indice 0: Concepto (vacío), Indice 1: Anualidad 1, Indice 2: TOTAL.
        int indexTotal = this.listaAnualidades.size() +  1;

        // Filtramos la lista principal por la partida actual y sumamos el índice del total
        total = lstPresup.stream()
                .filter(p -> p.getTipoFinanciacionDTO() != null && p.getTipoFinanciacionDTO().getIdValor() != null && partida.getIdValor() != null
                            && partida.getIdValor().equals(p.getTipoFinanciacionDTO().getIdValor()))
                .mapToDouble(p -> {
                    if (p.getDatos() != null && indexTotal < p.getDatos().size()
                            && p.getIdBloqueComponente() != null) { //para que no coja el ultimo registro de la fila TOTAL
                        return parseImporteDsValor(p.getDatos().get(indexTotal).getDsValor());
                    }
                    return 0.0;
                }).sum();

        return total;
    }

    // Suma vertical de una columna (anualidad) específica para TODAS las partidas
    public Double getGranTotalVerticalPres(int nCol, List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null) return 0.0;

        Double resul = lstPresup.stream()
        .filter(p -> p.getDatos() != null && nCol < p.getDatos().size()
                    && p.getIdBloqueComponente() != null) //para que no coja el ultimo registro de la fila TOTAL
        .mapToDouble(p -> parseImporteDsValor(p.getDatos().get(nCol).getDsValor()))
        .sum();

        return resul;
    }

    /**
     * Convierte el dsValor (String) de un DatoDTO a importe, tratando null/blanco/no numérico como 0.0
     * en vez de lanzar NullPointerException/NumberFormatException (Double.parseDouble no admite null).
     * Admite tanto coma decimal/punto de miles (formato español) como punto decimal (ej. "235861,59",
     * "1.234,56" o "235861.59"), normalizando el separador antes de parsear.
     */
    private double parseImporteDsValor(String dsValor) {
        String valorNormalizado = Utils.normalizarSeparadorDecimal(dsValor);
        if (StringUtils.isBlank(valorNormalizado)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(valorNormalizado);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    // El Gran Total Final es simplemente la suma vertical de la columna de "TOTALES"
    public Double getGranTotalFinalPres(List<BloqueComponenteDTO> lstPresup) {
        if (lstPresup == null || this.listaAnualidades == null) {
            return 0.0;
        }
        
        // Si la anualidad es la 1 (índice 1) y el total es el índice 2...
        // El índice del total horizontal siempre es el tamaño de la lista de anualidades + (conConceptos? 1 : 0)
        int indexTotalHorizontal = this.listaAnualidades.size() +  1;  
        double granTotal = getGranTotalVerticalPres(indexTotalHorizontal, lstPresup);
        
        return granTotal;
    }
    
    
    public void addNuevoConcepto(CatalogosValoresDTO tipoFinanciacionDTO, CatalogosValoresDTO tematicaDTO, int nmPresup) {
        try {
            // 1. Crear el DTO Padre en memoria con datos iniciales por defecto
            ExpedientePresupDTO nuevoConcepto = new ExpedientePresupDTO();
            nuevoConcepto.setTipoFinanciacionDTO(tipoFinanciacionDTO);
            nuevoConcepto.setTematicaDTO(tematicaDTO);
            nuevoConcepto.setTlConcepto(""); // Nombre inicial vacío para que el usuario lo edite
            nuevoConcepto.setTlObservacion("");
            nuevoConcepto.setIdExpediente(this.expedienteBean.getIdExpediente());
            nuevoConcepto.setNmPresup(nmPresup);
            nuevoConcepto.setItNuevoConceptoFinan(Constantes.AfirmacionNegacion.S);
            nuevoConcepto.setNmAyuda(this.montoAyudaConcedida);  //esto es lo que mete al guardarObservacsPresup
            nuevoConcepto.setNmAyudaSol(this.montoAyudaSolicitada); //VMM 27/10/26 MD-3094
            nuevoConcepto.setNmPorcentajeIntensidad(porcentajeIntensidadGestor);
            nuevoConcepto.setPendienteObservacion(Boolean.TRUE);
            //nuevoConcepto.setEstadoDTO();
            
            // 2. Llamar al servicio para persistir en BD y obtener los IDs (con historico)       
            nuevoConcepto = this.expedientePresupService.crearNuevoConceptoPadre(nuevoConcepto);
            
            //volver a setearle el orden de la partida que se perdió al pasar el DTO a entity
            nuevoConcepto.getTipoFinanciacionDTO().setOrdenPartida(tipoFinanciacionDTO.getOrdenPartida());
            
             // Inicializar la lista de montos (hijos)
             List<ExpedientePresupMontoDTO> listaMontos = new ArrayList<>();
             
             // 3. Crear los montos para cada Anualidad (los que irán a Base de Datos y con historico)
             for (int i=0; i <= this.listaAnualidades.size(); i++) {
                 ExpedientePresupMontoDTO nuevoMonto = new ExpedientePresupMontoDTO();
                 nuevoMonto.setIdExpedientePresup(nuevoConcepto.getIdExpedientePresup());
                 nuevoMonto.setNmAnualidad(i);
                 nuevoMonto.setNmImporte(0.0); // Valor inicial 0
                 nuevoMonto.setEsMontoTotal(false);   
                 
                 nuevoMonto = this.expedientePresupMontoService.crearNuevoConceptoHijo(nuevoMonto);
                 
                 listaMontos.add(nuevoMonto);
             }
             
             // 4. Crear el monto para el TOTAL HORIZONTAL 
             // (REGLA: En memoria, marcado para que el servicio lo ignore)
             ExpedientePresupMontoDTO montoTotalHorizontal = new ExpedientePresupMontoDTO();
             montoTotalHorizontal.setIdExpedientePresup(nuevoConcepto.getIdExpedientePresup());
             montoTotalHorizontal.setNmImporte(0.0);
             montoTotalHorizontal.setEsMontoTotal(true); // El servicio verá esto y NO lo meterá a la BD
             
             listaMontos.add(montoTotalHorizontal);
             
             // Asignar la lista de montos al padre
             nuevoConcepto.setLstExpedientePresupMontoDTO(listaMontos);
                             
            // 5. Añadir el objeto que ya viene con IDs a la lista de la pantalla
            // Se añade justo antes del elemento 'TOTAL' general para mantener el orden visual
             ordenarNuevoConcepto(tipoFinanciacionDTO, nmPresup, nuevoConcepto);
             
            // Opcional: Mostrar un mensaje de éxito en la interfaz
            //AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_INSERTAR_OK);
                
        } catch (Exception e) {
            // Manejo de errores en caso de que falle la BD
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_INSERTAR_KO);
            log.error("Error", "No se pudo crear el concepto en BD.");
            e.printStackTrace();
        }
    }


    private void ordenarNuevoConcepto(CatalogosValoresDTO tipoFinanciacionDTO, int nmPresup,
            ExpedientePresupDTO nuevoConcepto) {
        
         List<ExpedientePresupDTO> listaDestino = (nmPresup == 1) ? this.lstPresupuestoFinanciable1 : this.lstPresupuestoFinanciable2;

         // 1. Intentamos buscar el índice del ÚLTIMO concepto que ya exista de esta misma partida
         int posicionMismaPartida = -1;
         Integer idPartidaDestino = tipoFinanciacionDTO.getIdValor();

         for (int i = 0; i < listaDestino.size(); i++) {
             ExpedientePresupDTO item = listaDestino.get(i);
             if (item.getTipoFinanciacionDTO() != null && idPartidaDestino.equals(item.getTipoFinanciacionDTO().getIdValor())) {
                 posicionMismaPartida = i; // Se quedará con el último índice del grupo
             }
         }

         // 2. LÓGICA DE INSERCIÓN CONFIGURABLE
         if (posicionMismaPartida != -1) {
             // CASO A: La partida ya tiene conceptos. Lo metemos pegado al último de su grupo.
             listaDestino.add(posicionMismaPartida + 1, nuevoConcepto);
         } else {
             // CASO B: La partida está vacía en la lista (puede que se hayan eliminado todos los conceptos de esa partida). 
             // Buscamos dónde le corresponde nacer según el 'ordenPartida' del catálogo
             int indiceTarget = -1;
             Integer ordenNuevo = tipoFinanciacionDTO.getOrdenPartida(); // Orden de la partida que queremos añadir

             for (int i = 0; i < listaDestino.size(); i++) {
                 ExpedientePresupDTO item = listaDestino.get(i);
                 
                 // Freno de seguridad: Si topamos con la fila final de TOTALES, se inserta ahí mismo
                 if (item.getTipoFinanciacionDTO() != null && "TOTAL".equals(item.getTipoFinanciacionDTO().getDsValor())) {
                     indiceTarget = i;
                     break;
                 }
                 
                 // Si encontramos cualquier elemento de una partida con un orden superior, ocupamos su lugar
                 if (item.getTipoFinanciacionDTO() != null && item.getTipoFinanciacionDTO().getOrdenPartida() != null) {
                     if (item.getTipoFinanciacionDTO().getOrdenPartida() > ordenNuevo) {
                         indiceTarget = i;
                         break; // Paramos: hemos encontrado su hueco exacto en la ordenación
                     }
                 }
             }
             
             if (indiceTarget != -1) {
                 listaDestino.add(indiceTarget, nuevoConcepto);
             } else {
                 // Salvavidas por si la lista estuviera totalmente vacía o sin totales
                 listaDestino.add(Math.max(0, listaDestino.size() - 1), nuevoConcepto);
             }
         }
    }


    public void eliminarConcepto(ExpedientePresupDTO expPresupDTO) {
        try {
            // 1. Control de seguridad: Solo borramos de BD si el objeto ya tiene un ID asignado
            if (expPresupDTO != null && expPresupDTO.getIdExpedientePresup() != null) {
                
                // 2. Borrado lógico de los hijos (Montos de Anualidades)
                if (expPresupDTO.getLstExpedientePresupMontoDTO() != null) {
                    for (ExpedientePresupMontoDTO montoDTO : expPresupDTO.getLstExpedientePresupMontoDTO()) {
                        
                        //REGLA: Saltamos el monto total o cualquier registro sin ID real en BD
                        if (Boolean.TRUE.equals(montoDTO.getEsMontoTotal()) || Objects.isNull(montoDTO.getIdExpedientePresupMonto())) {
                            continue;
                        }
                        
                        // Ejecuta el borrado logico de los hijos
                        this.expedientePresupMontoService.eliminarMonto(montoDTO);
                    }
                }
                
                // 3. Borrado lógico del registro Padre (Concepto)
                this.expedientePresupService.eliminarExpPresup(expPresupDTO);
            }
            
            // 4. Eliminación de la memoria para limpiar la interfaz visual
            // Al hacer esto, el update="tablaPresupFinanciable1" del botón la hará desaparecer de pantalla
            this.lstPresupuestoFinanciable1.remove(expPresupDTO);
            
            // 5. Notificación de éxito al usuario
            AtlasMessageUtils.addInfoMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_ELIMINAR_OK);
                
        } catch (Exception e) {
            // En caso de fallo con los DAOs o la BD, notificamos el error sin alterar la pantalla
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_ELIMINAR_KO);
            log.error("Error", "No se pudo realizar la baja en el sistema.");
            e.printStackTrace();
        }
    }
    
    
    /**
     * VMM 06/10/26 MD-3113: listener del cambio de la DESCRIPCION del concepto (la observacion sigue
     * usando guardarConceptoObservac). No se guarda un concepto con la descripcion vacia o repetida
     * dentro de su partida: se avisa y se deja el valor anterior en BD (los nuevos conceptos nacen con
     * descripcion vacia, asi que mientras no se informe no se persiste nada). El guardado y la
     * finalizacion de la revision lo vuelven a comprobar (notificarConceptosInvalidos).
     */
    public void guardarConceptoNombre(ExpedientePresupDTO presupFinanciable) {
        if (Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup) && presupFinanciable != null) {
            if (StringUtils.isBlank(presupFinanciable.getTlConcepto())) {
                AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_VACIO,
                        new Object[] { 1, presupFinanciable.getTipoFinanciacionDTO() != null
                                && presupFinanciable.getTipoFinanciacionDTO().getDsValor() != null
                                        ? presupFinanciable.getTipoFinanciacionDTO().getDsValor() : "" });
                return;
            }
            List<ExpedientePresupDTO> lista = Integer.valueOf(2).equals(presupFinanciable.getNmPresup())
                    ? this.lstPresupuestoFinanciable2 : this.lstPresupuestoFinanciable1;
            if (ValidacionConceptosPresupuesto.esConceptoRepetido(presupFinanciable, lista)) {
                AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_REPETIDO_EDICION);
                return;
            }
        }
        this.guardarConceptoObservac(presupFinanciable);
    }

    /**
     * VMM 06/10/26 MD-3113: antes de guardar o finalizar la revision, comprueba que ningun concepto
     * tenga la descripcion vacia ni este repetido dentro de su partida (solo convocatorias con
     * conceptos). Emite los mensajes de error y devuelve true si hay alguno.
     */
    private boolean notificarConceptosInvalidos() {
        if (!Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)) {
            return false;
        }
        boolean hayError = false;
        List<List<ExpedientePresupDTO>> listas = new java.util.ArrayList<>();
        listas.add(this.lstPresupuestoFinanciable1);
        listas.add(this.lstPresupuestoFinanciable2);
        for (List<ExpedientePresupDTO> lista : listas) {
            List<String> vacios = ValidacionConceptosPresupuesto.partidasConConceptoVacio(lista);
            if (!vacios.isEmpty()) {
                AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_VACIO,
                        new Object[] { vacios.size(), String.join(", ", vacios) });
                hayError = true;
            }
            List<String> repetidos = ValidacionConceptosPresupuesto.conceptosRepetidos(lista);
            if (!repetidos.isEmpty()) {
                AtlasMessageUtils.addErrorMessageFromBundleParams(ConstantesWeb.Mensajes.SOLICITUD_CONCEPTO_REPETIDO,
                        new Object[] { String.join(", ", repetidos) });
                hayError = true;
            }
        }
        return hayError;
    }

    public void guardarConceptoObservac(ExpedientePresupDTO presupFinanciable) {
        try {
            // Control de seguridad: solo actualizamos si ya existe en Base de Datos
            if (presupFinanciable != null && presupFinanciable.getIdExpedientePresup() != null) {
                
                if (presupFinanciable.getTlObservacion() != null && StringUtils.isNotBlank(presupFinanciable.getTlObservacion())){
                    //MD-2731: observacion informada, limpiamos el pendiente de esta fila
                    presupFinanciable.setPendienteObservacion(Boolean.FALSE);
                }
                
                // Llamamos al servicio para que procese el guardado del texto
                this.expedientePresupService.actualizarConceptoObservac(presupFinanciable);
                
                // Nota: Al ser un guardado automático (auto-save), es mejor no saturar al usuario 
                // con mensajes flotantes de éxito. El usuario asume que se guarda.
            }
        } catch (Exception e) {
            // En caso de error, sí es crítico avisar al usuario en pantalla
            AtlasMessageUtils.addErrorMessageFromBundle(ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_CONCEPTO_GUARDAR_KO);
            log.error("Error", "No se pudo guardar la descripción del concepto.");
            e.printStackTrace();
        }
    }

 
    /**
     * MD-2788: emite un único mensaje de error con el número de filas pendientes y sus identificadores.
     * Devuelve true si habia alguna fila pendiente.
     */
    private boolean notificarFilasPendientesObservacion() {
        List<String> identificadores = new java.util.ArrayList<>();
        List<List<ExpedientePresupDTO>> listasExpPresup = new java.util.ArrayList<>();
        if (this.lstPresupuestoFinanciable1 != null && !this.lstPresupuestoFinanciable1.isEmpty()) {
            listasExpPresup.add(this.lstPresupuestoFinanciable1);
        }
        if (this.lstPresupuestoFinanciable2 != null && !this.lstPresupuestoFinanciable2.isEmpty()) {
            listasExpPresup.add(this.lstPresupuestoFinanciable2);
        }
        for (List<ExpedientePresupDTO> lstExpPres : listasExpPresup) {
            for (ExpedientePresupDTO expPresup : lstExpPres) {
                if (Boolean.TRUE.equals(expPresup.getPendienteObservacion())) {
                    String partida = (expPresup.getTipoFinanciacionDTO() != null && expPresup.getTipoFinanciacionDTO().getDsValor() != null)
                            ? expPresup.getTipoFinanciacionDTO().getDsValor() : "";
                    String identificador;
                    if (Boolean.TRUE.equals(this.esConvocatoriaConceptoPresup)
                            && StringUtils.isNotBlank(expPresup.getTlConcepto())) {
                        identificador = partida + " - " + expPresup.getTlConcepto();
                    } else {
                        identificador = partida;
                    }
                    identificadores.add(identificador);
                }
            }
        }
        if (!identificadores.isEmpty()) {
            AtlasMessageUtils.addErrorMessageFromBundleParams(
                    ConstantesWeb.Mensajes.SOLICITUD_FINANCIACION_OBSERVACION_PRESUP_PENDIENTES,
                    new Object[]{identificadores.size(), String.join(", ", identificadores)});
            return true;
        }
        return false;
    }

}
