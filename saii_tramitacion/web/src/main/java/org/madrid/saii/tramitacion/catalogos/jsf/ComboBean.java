package org.madrid.saii.tramitacion.catalogos.jsf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ConvocatoriaTipoDocService;
import org.madrid.saii.lib.services.SucaMunicipioService;
import org.madrid.saii.lib.services.SucaProvinciaService;
import org.madrid.saii.lib.services.TipoViaService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.tramitacion.jsf.utils.ConstantesWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Esta clase representa el Backing Bean para el formulario de creación/edición
 * de la entidad <code>{@link Catalogos}</code>
 *
 * @author everis
 */
@Getter
@Setter
@Slf4j
@Component("comboBean")
//@Scope("request")
@Scope("session")
public class ComboBean extends CatalogoBean<Catalogos, Integer> {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de CatalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** Servicio de ConvocatioriaTip */
    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService convocatoriaTipoDocService;

    /** Servicio de SucaProvincia */
    @Autowired
    @Qualifier("sucaProvinciaService")
    private SucaProvinciaService sucaProvinciaService;

    /** Servicio de SucaProvincia */
    @Autowired
    @Qualifier("sucaMunicipioService")
    private SucaMunicipioService sucaMunicipioService;

    /** Servicio de TipoVia */
    @Autowired
    @Qualifier("tipoViaService")
    private TipoViaService tipoViaService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public ComboBean() {
        super();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// MÉTODOS
    // ////////////////////////////////////////////////////////

    /**
     * Crear Entidad
     */
    @Override
    public Catalogos crearEntidad() {
        return new Catalogos();
    }

    /**
     * Metodo que se ejecuta después del constructor.
     */
    @PostConstruct
    public void init() {
        log.info("Post Construct");
    }

    /**
     * Metodo que rellena el combo de CatalogosValoresDTO para la modalidad de
     * la convocatoria
     * @throws ServiceException 
     */
    public List<SelectItem> getComboModalidadConvocatoria() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.MODALIDAD_CONVOCATORIA));
    }
    
    /**
    *
    * @return
     * @throws ServiceException 
    */
   public List<SelectItem> getComboAreaCientifica() throws ServiceException {

       return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.AREAS_CIENTIFICAS));
   }

    /**
     * Metodo que rellena el combo de CatalogosValoresDTO para los estados de
     * expediente
     * @throws ServiceException
     */
    public List<SelectItem> getComboEstadoExpediente() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_EXPEDIENTE),
                ESTADOS_EXPEDIENTE_ASIGNABLES);
    }

    /**
     * VMM 30/09/26 MD-2133: nm_constante de los unicos EstadoExpediente que realmente se llegan a
     * asignar al campo Expediente.estado en algun punto del codigo (confirmado buscando todos los
     * .setEstado()/.setEstadoDTO() sobre Expediente/ExpedienteDTO) - el resto de valores del catalogo
     * ESTADO_EXPEDIENTE solo se usan en campos hermanos (estadoRevAdmin, estadoRevEval, etc.), en otra
     * entidad, o son codigo muerto, y no tiene sentido ofrecerlos en el filtro de Estado de Expediente.
     * Se excluyen ademas, tras revision manual, EVALUADO_KO y REVISION_OK (se asignan a estado como un
     * "espejo" del resultado interno de la revision de Contrato, ver actualizarEstadoInternoContrato -
     * incidencia aparte, no algo que el filtro deba ofrecer), y PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA
     * y PENDIENTE_PRESENTACION_CONTRATOS (confirmacion solo indirecta/dudosa).
     */
    private static final List<Integer> ESTADOS_EXPEDIENTE_ASIGNABLES = java.util.Arrays.asList(
            Constantes.EstadoExpediente.INICIADA,
            Constantes.EstadoExpediente.PROPUESTA,
            Constantes.EstadoExpediente.ACEPTADA,
            Constantes.EstadoExpediente.NO_ACEPTADA,
            Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA,
            Constantes.EstadoExpediente.EN_TRAMITACION,
            Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO,
            Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS,
            Constantes.EstadoExpediente.DESISTIDO,
            Constantes.EstadoExpediente.EXCLUIDO,
            Constantes.EstadoExpediente.DENEGADO_PROVISIONAL,
            Constantes.EstadoExpediente.APROBADO_PROVISIONAL,
            Constantes.EstadoExpediente.RENUNCIA_EXPRESA,
            Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION,
            Constantes.EstadoExpediente.DENEGADO_DEFINITIVO,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO,
            Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO,
            Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES,
            Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO,
            Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_DESISTIDO,
            Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL,
            Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL,
            Constantes.EstadoExpediente.PENDIENTE_DEFINITIVO);

    /**
     * VMM 23/09/26 MD-2133: combo de CatalogosValoresDTO para los estados de solicitud, usado en el filtro
     * de seleccion multiple de solapaSolicitudes.
     * @throws ServiceException
     */
    public List<SelectItem> getComboEstadoSolicitud() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_SOLICITUD));
    }

    /**
     * VMM 15/10/26 MD-3125: igual que getComboEstadoSolicitud() pero sin los estados que solo se listan
     * cuando esta marcado "Mostrar Iniciadas" (SolicitudBean.obtenerListaSolicitudes anade INICIADA a los
     * estados visibles solo con el check), para no ofrecer un estado que luego no devolveria resultados.
     */
    public List<SelectItem> getComboEstadoSolicitud(Boolean mostrarIniciadas) throws ServiceException {
        List<SelectItem> lstItem = this.getComboEstadoSolicitud();
        if (!Boolean.TRUE.equals(mostrarIniciadas)) {
            this.quitarEstados(lstItem, Constantes.EstadoSolicitud.INICIADA);
        }
        return lstItem;
    }

    /**
     * VMM 15/10/26 MD-3125: igual que getComboEstadoExpediente() pero sin los estados de expediente que
     * ExpedienteServiceImpl.obtenerExpedienteEstructurado/countExpedienteEstructurado excluyen cuando NO esta
     * marcado "Mostrar Iniciadas" (iniciados, propuestos, no aceptados y rechazados definitivos)
     */
    public List<SelectItem> getComboEstadoExpediente(Boolean mostrarIniciadas) throws ServiceException {
        List<SelectItem> lstItem = this.getComboEstadoExpediente();
        if (!Boolean.TRUE.equals(mostrarIniciadas)) {
            this.quitarEstados(lstItem, Constantes.EstadoExpediente.INICIADA,
                    Constantes.EstadoExpediente.PROPUESTA, Constantes.EstadoExpediente.NO_ACEPTADA,
                    Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO);
        }
        return lstItem;
    }

    /**
     * VMM 15/10/26 MD-3125: quita del combo los estados cuyo nm_constante se indica (la lista que
     * devuelve combo() es nueva en cada llamada, asi que se puede modificar)
     */
    private void quitarEstados(List<SelectItem> lstItem, Integer... nmConstantes) {
        List<Integer> idsOcultos = new ArrayList<>();
        for (Integer nmConstante : nmConstantes) {
            idsOcultos.add(utilConstantes.getGlobalCatalogosValores().get(nmConstante));
        }
        lstItem.removeIf(item -> idsOcultos.contains(item.getValue()));
    }

    /**
     * Metodo que rellena el combo de CatalogosValoresDTO para los tipos de
     * documentacion
     * @throws ServiceException
     */
    public List<SelectItem> getComboTipoDocumento() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_DOCUMENTACION_SOLICITUD));
    }
    
    
    public List<SelectItem> getComboTipoResolucion() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.CATALOGO_ESTADO_TIPO_RESOLUCION));
    }
    

    /**
     * Metodo que rellena el combo de CatalogosValoresDTO para los tipos de
     * trámite
     * @throws ServiceException 
     */
    public List<SelectItem> getComboTramiteFiltrado() throws ServiceException {

        List<SelectItem> lstItem = new ArrayList<>();

        List<Integer> lstTramitesPermitidos = new ArrayList<>();
        lstTramitesPermitidos.add(Constantes.Tramite.PRESENTACION);
        lstTramitesPermitidos.add(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);
        // MD-2067 quitamos el tramite de requerimiento de subsanación de requisitos
//        lstTramitesPermitidos.add(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
        //lstTramitesPermitidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        //VMM 26/09/24 MD-593: ahora solo metemos los tramites del gestor (catalogo 38), revisar esto...
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_NMCONSTANTE, lstTramitesPermitidos,
                Operador.IN));

        //VMM 26/09/24 MD-593: ahora solo metemos los tramites del gestor (catalogo 38), revisar esto...
        // Los catálogos de documentación no deben mostrar estos documentos
        if (utilConstantes.getGlobalCatalogos().get(Constantes.Cata.DOCUMENTACION_SOLICITUD).equals(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR))) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_MANTENIBLE,
                    Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        }

        try {
            List<CatalogosValoresDTO> lst = this.catalogosValoresService.obtenerPorFiltros(filtros);
            Iterator<CatalogosValoresDTO> it = lst.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                lstItem.add(new SelectItem(dto.getIdValor(), dto.getDsValor()));
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }

        return lstItem;

    }

    /**
     * Metodo que rellena el combo de tipos de documentos.
     *
     * @param idConvocatoria identificador de convocatoria
     * @return Lista de Tipos de Documento
     */
    public List<SelectItem> getComboTipoDocumentoConvocatoria(Integer idConvocatoria) {

        List<SelectItem> lstItem = new ArrayList<>();
        try {
            if (idConvocatoria != null) {
                List<ConvocatoriaTipoDocDTO> lst = this.convocatoriaTipoDocService
                        .buscarPorConvocatoriaNoFiltrado(idConvocatoria);
                Iterator<ConvocatoriaTipoDocDTO> it = lst.iterator();
                lstItem.add(new SelectItem(-1, "Otros"));
                while (it.hasNext()) {
                    ConvocatoriaTipoDocDTO obj = it.next();

                    // Omitimos el tipo de documento alegación porque es un
                    // documento único
                    if (!utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES)
                            .equals(obj.getTipoDocumentoDTO().getIdValor())) {
                        lstItem.add(new SelectItem(obj.getTipoDocumentoDTO().getIdValor(),
                                obj.getTipoDocumentoDTO().getDsValor()));
                    }
                }
            }
        } catch (ServiceException e) {
            log.error("Se ha producido un error al obtener las tipo de documentos", e);
        }
        return lstItem;
    }

    /**
     * Metodo para obtener todas las provincias
     *
     *
     * @return Lista de provincias
     */
    public List<SelectItem> getProvincias() {

        List<SelectItem> lstItem = new ArrayList<>();
        String provinciaNombre = "";
        try {
            List<CatalogoDTO> lst = this.sucaProvinciaService.obtener();
            Iterator<CatalogoDTO> it = lst.iterator();
            while (it.hasNext()) {
                CatalogoDTO pro = it.next();
                if (pro.getDescripcion().contains(",")) {
                    provinciaNombre = (pro.getDescripcion().split(",")[1] + " "
                            + pro.getDescripcion().split(",")[0]).replace(",", "");
                } else {
                    provinciaNombre = pro.getDescripcion();
                }

                lstItem.add(new SelectItem(pro.getCodigo(), provinciaNombre));
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }

        return lstItem;
    }

    /**
     * Metodo para obtener todos los municipios de una provincia
     *
     * @param idProvincia
     * @return Lista de municipios
     */
    public List<SelectItem> getMunicipios(Integer idProvincia) {

        List<SelectItem> lstItem = new ArrayList<>();
        String municipioNombre = "";
        try {
            List<SucaMunicipioDTO> lst = this.sucaMunicipioService.buscarPorProvincia(idProvincia);
            Iterator<SucaMunicipioDTO> it = lst.iterator();
            while (it.hasNext()) {
                SucaMunicipioDTO mu = it.next();

                if (mu.getDsMunicipio().contains(",")) {
                    municipioNombre = (mu.getDsMunicipio().split(",")[1] + " "
                            + mu.getDsMunicipio().split(",")[0]).replace(",", "");
                } else {
                    municipioNombre = mu.getDsMunicipio();
                }
                lstItem.add(new SelectItem(mu.getCdMunicipio(), municipioNombre));
            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }
        return lstItem;
    }

    /**
     * Metodo para obtener todos los tipos de via
     *
     * @return Lista de tipos de via
     */
    public List<SelectItem> getTipoVia() {

        List<SelectItem> lstItem = new ArrayList<>();
        try {
            List<CatalogoDTO> lst = this.tipoViaService.obtener();

            Iterator<CatalogoDTO> it = lst.iterator();
            while (it.hasNext()) {
                CatalogoDTO via = it.next();
                lstItem.add(new SelectItem(via.getCodigo(), via.getDescripcion()));

            }
        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }

        return lstItem;
    }

    /**
     * Metodo para obtener combo de sexo
     *
     *
     * @return Lista de sexos
     */
    public List<SelectItem> getSexo() {

        List<SelectItem> lstItem = new ArrayList<>();

        lstItem.add(new SelectItem("H", "Hombre"));
        lstItem.add(new SelectItem("O", "Otro"));
        lstItem.add(new SelectItem("M", "Mujer"));

        return lstItem;
    }

    /**
     * Metodo para obtener los tipos de identificacion
     *
     *
     * @return Lista de tipoIdentificacion
     * @throws ServiceException 
     */
    public List<SelectItem> getTipoIdentificacion() throws ServiceException {

        return this.combo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPOS_IDENTIFICACION));
    }

    // ///////////////////////////////////////////////////////////////
    // /////////////////////////////// MÉTODOS PRIVADOS
    // ///////////////////////////////////////////////////////////////

    /**
     * Obtiene el listado de SelectItem a partir de un catalogo. Guarda, en el
     * campo "Value" del select item el "IdValor" y el "DsValor" separados por
     * un ";" --> IdValor;DsValor
     *
     * Este metodo se usa para aquellos comboValores que realicen comprobaciones
     * para renderizar otros campos segun el valor seleccionado. No ponemos el
     * DTO en el campo Value porque el componente de atlas nunca selecciona
     * visualmente aquel que deseamos.
     *
     * @param strNombreCatalogo
     * @return
     * @throws ServiceException 
     */
    private List<SelectItem> combo(Integer idCatalogo) throws ServiceException {
        return this.combo(idCatalogo, null);
    }

    /**
     * VMM 30/09/26 MD-2133: variante de combo() que permite acotar los valores del catalogo a un
     * subconjunto de nm_constante concretos (p.ej. para que el combo de Estado de Expediente solo
     * muestre los estados que realmente se llegan a asignar al campo Expediente.estado, y no todo
     * el grupo de catalogo, que tambien incluye valores usados unicamente en otros campos
     * hermanos como estadoRevAdmin/estadoRevEval/etc. o en otras entidades).
     */
    private List<SelectItem> combo(Integer idCatalogo, List<Integer> nmConstantesPermitidos) throws ServiceException {

        List<SelectItem> lstItem = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, idCatalogo,
                Operador.EQUALS));

        if (nmConstantesPermitidos != null && !nmConstantesPermitidos.isEmpty()) {
            filtros.add(new AtlasFilter("nmConstante", nmConstantesPermitidos, Operador.IN));
        }

        // Los catálogos de documentación no deben mostrar estos documentos
        if (utilConstantes.getGlobalCatalogos().get(Constantes.Cata.DOCUMENTACION_SOLICITUD).equals(idCatalogo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_MANTENIBLE,
                    Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        }

        try {
            List<CatalogosValoresDTO> lst = this.catalogosValoresService.obtenerPorFiltros(filtros);
            Iterator<CatalogosValoresDTO> it = lst.iterator();
            while (it.hasNext()) {
                CatalogosValoresDTO dto = it.next();
                lstItem.add(new SelectItem(dto.getIdValor(), dto.getDsValor()));
            }

        } catch (ServiceException e) {
            log.error(ConstantesWeb.Err.ERROR_GENERAL, e);
        }

        return lstItem;
    }

}
