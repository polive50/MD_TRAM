package org.madrid.saii.lib.services;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusEcoDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusTecDAO;
import org.madrid.saii.lib.dao.DatoDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.dao.ExpedienteModifPresPlazoOtraDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.domain.ExpedienteEval;
import org.madrid.saii.lib.domain.ExpedienteHist;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoHistDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecHistDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.FinanciacionDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.NoteEnviarNotificacionDTO;
import org.madrid.saii.lib.dto.NotificacionCiudadanoDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.ResponseDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.dto.converters.BloqueComponenteToEntity;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaCauToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.DocumentoPtfrToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusTecToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresPlazoOtraToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteReqHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteReqToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTOMin;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.dto.converters.ImpresoToDTO;
import org.madrid.saii.lib.dto.converters.InteresadoToDTO;
import org.madrid.saii.lib.dto.converters.InteresadoToEntity;
import org.madrid.saii.lib.dto.converters.SolicitudToDTO;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.util.Base64;
import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Expediente
 *
 * @author nttdata
 */
@Slf4j
@Service("expedienteService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteServiceImpl extends BaseServiceImpl<Expediente, Integer, ExpedienteDAO>
        implements ExpedienteService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /**
     * Servicio de Dato
     */
    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;

    /**
     * Servicio de Impreso
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    /**
     * Servicio de CatalogoValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Servicio de Interesado
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;

    /**
     * Servicio de Entidad
     */
    @Autowired
    @Qualifier("entidadService")
    private EntidadService entidadService;

    /**
     * Servicio de Fica
     */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /**
     * Servicio de Ptfr Adme.
     */
    @Autowired
    @Qualifier("ptfrAdmeService")
    private PtfrAdmeService ptfrAdmeService;

    /**
     * Servicio de Ereg Adme.
     */
    @Autowired
    @Qualifier("eregAdmeService")
    @Setter
    private EregAdmeService eregAdmeService;

    /**
     * Servicio de ConvocatoriaAct
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     * Servicio de TipoResolModalidad
     */
    @Autowired
    @Qualifier("tipoResolModalidadService")
    private TipoResolModalidadService tipoResolModalidadService;

    /**
     * Servicio de ConvocatoriaAct
     */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;

    /**
     * Servicio de ConvocatoriaReqService
     */
    @Autowired
    @Qualifier("convocatoriaReqService")
    private ConvocatoriaReqService convocatoriaReqService;

    /**
     * Servicio de ConvocatoriaSubmodService
     */
    @Autowired
    @Qualifier("convocatoriaSubmodService")
    private ConvocatoriaSubmodService convocatoriaSubmodService;

    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService representanteJurService;

    @Autowired
    @Qualifier("sucaMunicipioService")
    private SucaMunicipioService sucaMunicipioService;

    @Autowired
    @Qualifier("sucaProvinciaService")
    private SucaProvinciaService sucaProvinciaService;

    @Autowired
    @Qualifier("cataService")
    private CataService cataService;

    @Lazy
    @Autowired
    @Qualifier("expedienteDocService")
    private ExpedienteDocService expedienteDocService;

    /** servicio de convocatoriaDoc **/
    @Autowired
    @Qualifier("convocatoriaDocService")
    private ConvocatoriaDocService convocatoriaDocService;

    /** servicio de expedienteJusTec **/
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;
    

    /** servicio de expedienteJusEcoService **/
    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService  expedienteJusEcoService;
   
    /**
     * Servicio PdfService
     */
    @Lazy
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * NoteAdmeService
     */
    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;

    @Lazy
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

    /**
     * expedienteContratoService
     */
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;

    /**
     * evaluacionService
     */
    @Autowired
    @Qualifier("evaluacionService")
    private EvaluacionService evaluacionService;

    @Autowired
    @Qualifier("expedienteJusTecDocService")
    private ExpedienteJusTecDocService expedienteJusTecDocService;

    @Autowired
    @Qualifier("expedienteEvalJusTecService")
    private ExpedienteEvalJusTecService expedienteEvalJusTecService;

    @Autowired
    @Qualifier("expedienteContratoDocService")
    private ExpedienteContratoDocService expedienteContratoDocService;
    
    /** Servicio de Catalogos */
    @Autowired
    @Qualifier("catalogosService")
    private CatalogosService catalogosService;
    
    /**
     * Servicio asociado a la entidad Componente
     */
    @Autowired
    @Qualifier("componenteService")
    private ComponenteService componenteService;
    
    @Autowired
    @Qualifier("evaluacionJustecService")
    private EvaluacionJustecService evaluacionJustecService;
    
    /** servicio de expedienteJusTecHist **/
    @Autowired
    @Qualifier("expedienteJusTecHistService")
    private ExpedienteJusTecHistService expedienteJusTecHistService;
    

    /** servicio de expedienteJusEcoHist **/
    @Autowired
    @Qualifier("expedienteJusEcoHistService")
    private ExpedienteJusEcoHistService  expedienteJusEcoHistService;
    
    @Autowired
    @Qualifier("expedienteContratoHistService")
    private ExpedienteContratoHistService expedienteContratoHistService;
    
    @Autowired
    @Qualifier("expedienteDocHistService")
    private ExpedienteDocHistService expedienteDocHistService;
    
    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;
    
    /** Servicio de UnidadAdmin */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;
    
    @Autowired
    @Qualifier("tramiteSolicitanteExpedienteService")
    private TramiteSolicitanteExpedienteService tramiteSolicitanteExpedienteService;
    
    @Autowired
    @Qualifier("informeService")
    private InformeService informeService;

    @Autowired
    @Qualifier("informeEvalDocService")
    private InformeEvalDocService informeEvalDocService;
    
    @Lazy
    @Autowired
    @Qualifier("financiacionService")
    private FinanciacionService financiacionService;

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// DAO
    // ////////////////////////////////////////////////////////

    /**
     * DAO de DocumentoPtfr
     */
    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    /**
     * DAO de ExpedienteDoc
     */
    @Autowired
    @Qualifier("expedienteDocDAO")
    @Setter
    private ExpedienteDocDAO expedienteDocDAO;

    /**
     * DAO de ExpedienteDocHist
     */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    @Setter
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /**
     * DAO de Solicitud
     */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    /**
     * DAO de ExpedienteReq
     */
    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;

    @Autowired
    @Qualifier("expedienteReqHistService")
    private ExpedienteReqHistService expedienteReqHistService;

    /**
     * DAO de NotificacionCiudadano
     */
    @Autowired
    @Qualifier("notificacionCiudadanoDAO")
    private NotificacionCiudadanoDAO notificacionCiudadanoDAO;

    /**
     * DAO de NotificacionCiudadanoDoc
     */
    @Autowired
    @Qualifier("notificacionCiudadanoDocDAO")
    private NotificacionCiudadanoDocDAO notificacionCiudadanoDocDAO;

    /**
     * Servicio de NotificacionCiudadano
     */
    @Autowired
    @Qualifier("notificacionCiudadanoService")
    private NotificacionCiudadanoService notificacionCiudadanoService;

    /**
     * DAO para generar historicos
     */
    @Autowired
    @Qualifier("expedienteHistDAO")
    @Setter
    private ExpedienteHistDAO expedienteHistDAO;

    /**
     * DAO de InteresadoD
     */
    @Autowired
    @Qualifier("interesadoDAO")
    private InteresadoDAO interesadoDAO;

    /**
     * DAO de InteresadoD
     */
    @Autowired
    @Qualifier("expedienteEvalDAO")
    private ExpedienteEvalDAO expedienteEvalDAO;

    /**
     * DAO de Convocatoria
     */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     * DAO de Convocatoria
     */
    @Autowired
    @Qualifier("bloqueDAO")
    private BloqueDAO bloqueDAO;

    /**
     * DAO de ExpedienteJusTecHist
     */
    @Autowired
    @Qualifier("expedienteJusTecHistDAO")
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    /**
     * DAO de Expediente Justificación Técnica
     */
    @Autowired
    @Qualifier("expedienteJusTecDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;

    /**
     * DAO de Convocatoria
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;

    /**
     * Session factory de hibernate
     */
    @Resource
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteContratoDAO")
    private ExpedienteContratoDAO expedienteContratoDAO;

    @Autowired
    @Qualifier("datoDAO")
    private DatoDAO datoDAO;

    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;

    /** TipoResolConvocatoria **/
    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteModifOtraService")
    private ExpedienteModifOtraService expedienteModifOtraService;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraDAO")
    private ExpedienteModifPresPlazoOtraDAO expedienteModifPresPlazoOtraDAO;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("convocatoriaJusEcoDAO")
    private ConvocatoriaJusEcoDAO convocatoriaJusEcoDAO;
    
    @Autowired
    @Qualifier("convocatoriaJusTecDAO")
    private ConvocatoriaJusTecDAO convocatoriaJusTecDAO;
    
    @Autowired
    @Qualifier("expedienteJustecDocDAO")
    private ExpedienteJustecDocDAO expedienteJustecDocDAO;
    
    @Autowired
    @Qualifier("expedienteJusEcoDAO")
    private ExpedienteJusEcoDAO expedienteJusEcoDAO;
    
    @Autowired
    @Qualifier("expedienteJusEcoHistDAO")
    private ExpedienteJusEcoHistDAO expedienteJusEcoHistDAO;
    
    
    @Autowired
    @Qualifier("expedienteJusEcoDocDAO")
    private ExpedienteJusEcoDocDAO expedienteJusEcoDocDAO;
    
    
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;
    
    
    

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteDAO
     */
    @Autowired
    public ExpedienteServiceImpl(@Qualifier("expedienteDAO")
    ExpedienteDAO expedienteDAO) {
        super();
        this.setDao(expedienteDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteServiceImpl() {
        super();
    }

    /**
     * Buscar por id Expediente.
     *
     * @param idExpediente the id Expediente
     * @return ExpedienteDTO
     * @throws ServiceException the service exception
     */
    @Override
    public ExpedienteDTO buscarId(Integer idExpediente) throws ServiceException {

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(this.find(idExpediente));

        // Recuperamos los datos del expediente.
        dto = this.rellenarDatosExpediente(dto);

        if (dto.getLstExpedienteDocDTO().isEmpty()) {
            dto.setLstExpedienteDocDTO(
                    this.expedienteDocService.buscarPorExpediente(dto.getIdExpediente()));
        }

        // Rellenamos los documentos del expediente
        dto.setLstExpedienteDocDTO(this.rellenarDocumentosExpediente(dto.getLstExpedienteDocDTO()));

        ExpedienteModifOtraDTO expedienteModifOtraDTO = this.expedienteModifOtraService
                .obtenerExpedientePrespModifOtra(dto.getIdExpediente());
        if (expedienteModifOtraDTO != null
                && expedienteModifOtraDTO.getIdExpedienteModifOtra() != null) {
            dto.setExpedienteModifOtraDTO(expedienteModifOtraDTO);
        }

        List<ExpedientePresupDTO> listaExpedientePresup = this.expedientePresupService
                .buscarPresupIncialExpediente(dto.getIdExpediente());
        if (listaExpedientePresup != null && !listaExpedientePresup.isEmpty()) {
            dto.setListaExpedientePresup(listaExpedientePresup);
        }

        List<ExpedienteAnualidadDTO> listaExpedientePlazos = this.expedienteAnualidadService
                .buscarPorExpediente(dto.getIdExpediente());
        if (listaExpedientePlazos != null && !listaExpedientePlazos.isEmpty()) {
            dto.setListaExpedientePlazos(listaExpedientePlazos);
        }

        if (dto.getListaDocAceptacionAyuda() == null || dto.getListaDocAceptacionAyuda().isEmpty()) {

            List<ExpedienteDocDTO> listaDocExp= this.expedienteDocService.buscarPorExpediente(dto.getIdExpediente());
            if(listaDocExp != null && !listaDocExp.isEmpty()) {
                // Solo los documentos configurados en bloques del impreso de aceptación
                // (no los de la solicitud inicial cuya consulta no se autorizó, que aunque
                // compartan el mismo trámite se gestionan y pintan aparte en el frontal,
                // para no duplicarlos en ambos bloques).
                List<Integer> idsBloquesAceptacion = (dto.getImpresoAceptacionDTO() != null
                        && dto.getImpresoAceptacionDTO().getLstBloqueDTO() != null)
                        ? dto.getImpresoAceptacionDTO().getLstBloqueDTO().stream()
                                .map(BloqueDTO::getIdBloque).collect(Collectors.toList())
                        : new ArrayList<>();

                listaDocExp =listaDocExp.stream().filter(x-> x.getTramiteDTO() != null &&  x.getTramiteDTO().getIdValor() != null
                        && x.getTramiteDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA))
                        && x.getBloqueComponenteDTO() != null
                        && idsBloquesAceptacion.contains(x.getBloqueComponenteDTO().getIdBloque()) )
                        .collect(Collectors.toList());
                if(listaDocExp != null && !listaDocExp.isEmpty()) {
                    dto.setListaDocAceptacionAyuda(this.rellenarDocumentosExpediente(listaDocExp));
                }

            }


        }
        
        if(dto.getIdExpedientePadre() != null) {
            SolicitudDTO solicitudDTO= this.solicitudService.buscarId(dto.getIdSolicitud());
            EntidadDTO entidadSolicitante= this.entidadService.buscarEntidadInteresado(solicitudDTO.getInteresadoDTO().getIdInteresado());
            if(entidadSolicitante == null || entidadSolicitante.getInteresadoDTO() == null || entidadSolicitante.getInteresadoDTO().getIdInteresado() == null) {
                entidadSolicitante= new EntidadDTO();
                entidadSolicitante.setInteresadoDTO(solicitudDTO.getInteresadoDTO());
            }
            dto.setEntidadDTO(entidadSolicitante);
        }
        
        
        return dto;
    }

    /**
     * Método para obtener datos del expediente.
     *
     * @param idExpediente Identificador de expediente
     * @return ExpedienteDTO Datos del expediente
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @Override
    public ExpedienteDTO buscarIdSimple(Integer idExpediente, Boolean datosCompletos)
            throws ServiceException {
        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(this.find(idExpediente));
        if (Boolean.TRUE.equals(datosCompletos)) {
            dto = this.rellenarDatosExpediente(dto);
        }
        return dto;
    }

    /**
     *
     */
    @Override
    public ExpedienteDTO buscarIdMinimo(Integer idExpediente) throws ServiceException {

        return ExpedienteToDTOMin.INSTANCE.apply(this.find(idExpediente));
    }

    /**
     * Método que rellena los datos del expediente
     *
     * @param dto
     * @return dto
     * @throws ServiceException
     */
    public ExpedienteDTO rellenarDatosExpediente(ExpedienteDTO dto) throws ServiceException {

        ImpresoDTO impresoDTO = null;
        if (dto.getImpresoDTO() != null && dto.getImpresoDTO().getIdImpreso() != null) {
            impresoDTO = this.impresoService.buscarId(dto.getImpresoDTO().getIdImpreso());
        } else {
            List<Integer> tipoImpreso = new ArrayList<>();
            if (Boolean.TRUE.equals(dto.getEsExpedienteSolicitante())) {
                tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
            } else {
                tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
            }
            List<Impreso> lstImpresoDTO = this.impresoService
                    .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpreso);
            if (lstImpresoDTO != null && !lstImpresoDTO.isEmpty()) {
                impresoDTO = ImpresoToDTO.INSTANCE.apply(lstImpresoDTO.get(0));
            }
        }

        if (Boolean.TRUE.equals(dto.getConvocatoriaActDTO().getItCumplimentaFormulario())) {
            // Agregamos los datos del impreso recuperado
            dto.setImpresoDTO(impresoDTO);
            this.rellenarCdAsuntoExp(dto, impresoDTO);

            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueDTO = dto.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoDTO().setLstBloqueDTO(lstBloqueDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }
        
        //IMPORTANTE!!! quiza haya que poner un control aqui para que solo rellene los datos de estos formularios extra
        //si la convocatoria está resuelta (resolucion conjunta) o si se ha concedido definitivo este expdte (resolucion individual)
        
        
        List<Integer> tipoImpresoInter = new ArrayList<>();
        tipoImpresoInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
        List<Impreso> lstImpresoInter = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoInter);
        // Datos Justec Intermedia
        if (lstImpresoInter != null && !lstImpresoInter.isEmpty()) {
            ImpresoDTO impresoJustecIntermediaDTO = ImpresoToDTO.INSTANCE
                    .apply(lstImpresoInter.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoJustecDTO(impresoJustecIntermediaDTO);
            this.rellenarCdAsuntoExp(dto, impresoJustecIntermediaDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueJustecIntemedioDTO = dto.getImpresoJustecDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueJustecIntemedioDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoJustecDTO().setLstBloqueDTO(lstBloqueJustecIntemedioDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoJustecDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        // impreso Justec Final
        List<Integer> tipoImpresoFinal = new ArrayList<>();
        tipoImpresoFinal.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
        List<Impreso> lstImpresoFinal = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoFinal);
        if (lstImpresoFinal != null && !lstImpresoFinal.isEmpty()) {
            ImpresoDTO impresoJustecFinalDTO = ImpresoToDTO.INSTANCE.apply(lstImpresoFinal.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoJustecFinalDTO(impresoJustecFinalDTO);
            this.rellenarCdAsuntoExp(dto, impresoJustecFinalDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueJustecFinalDTO = dto.getImpresoJustecFinalDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueJustecFinalDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoJustecFinalDTO().setLstBloqueDTO(lstBloqueJustecFinalDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoJustecFinalDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        //VMM 26/05/25 MD-1566: Impreso Justec Economica
        List<Integer> tipoImpresoInterEco = new ArrayList<>();
        tipoImpresoInterEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
        List<Impreso> lstImpresoInterEco = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoInterEco);
        
        if (lstImpresoInterEco != null && !lstImpresoInterEco.isEmpty()) {
            ImpresoDTO impresoJustecEcoIntermediaDTO = ImpresoToDTO.INSTANCE
                    .apply(lstImpresoInterEco.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoJusEcoDTO(impresoJustecEcoIntermediaDTO);
            this.rellenarCdAsuntoExp(dto, impresoJustecEcoIntermediaDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueJustecEcoIntemedioDTO = dto.getImpresoJusEcoDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueJustecEcoIntemedioDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoJusEcoDTO().setLstBloqueDTO(lstBloqueJustecEcoIntemedioDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoJusEcoDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        // impreso Justec Economica Final
        List<Integer> tipoImpresoFinalEco = new ArrayList<>();
        tipoImpresoFinalEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
        List<Impreso> lstImpresoFinalEco = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoFinalEco);
        if (lstImpresoFinalEco != null && !lstImpresoFinalEco.isEmpty()) {
            ImpresoDTO impresoJustecEcoFinalDTO = ImpresoToDTO.INSTANCE.apply(lstImpresoFinalEco.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoJusEcoFinalDTO(impresoJustecEcoFinalDTO);
            this.rellenarCdAsuntoExp(dto, impresoJustecEcoFinalDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueJustecEcoFinalDTO = dto.getImpresoJusEcoFinalDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueJustecEcoFinalDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoJusEcoFinalDTO().setLstBloqueDTO(lstBloqueJustecEcoFinalDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoJusEcoFinalDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }
        
        Integer idExpediente = dto.getIdExpediente();
        Integer idActor = dto.getConvocatoriaActDTO().getIdActor();
        ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(idActor);
        if (convocatoriaActDTO.getItContrato().equals(Boolean.TRUE)
                && dto.getIdExpedientePadre() != null
                && (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE)
                        .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor()))) {

            idExpediente = dto.getIdExpedientePadre();
        }

        List<Integer> tipoImpresoContIni = new ArrayList<>();
        tipoImpresoContIni.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
        List<Impreso> lstImpresoContIni = this.impresoService.buscarPorExpedienteTipo(idExpediente,
                tipoImpresoContIni);
        if (lstImpresoContIni != null && !lstImpresoContIni.isEmpty()) {

            ImpresoDTO impresoContIniDTO = ImpresoToDTO.INSTANCE.apply(lstImpresoContIni.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoContratoDTO(impresoContIniDTO);
            this.rellenarCdAsuntoExp(dto, impresoContIniDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueContratoDTO = dto.getImpresoContratoDTO().getLstBloqueDTO()
                    .stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueContratoDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoContratoDTO().setLstBloqueDTO(lstBloqueContratoDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoContratoDTO().getLstBloqueDTO()) {
                
                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        List<Integer> tipoImpresoRenunciaConSutitucion = new ArrayList<>();
        tipoImpresoRenunciaConSutitucion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));
        List<Impreso> lstImpresoRenunciaConSutitucion = this.impresoService
                .buscarPorExpedienteTipo(idExpediente, tipoImpresoRenunciaConSutitucion);
        if (lstImpresoRenunciaConSutitucion != null && !lstImpresoRenunciaConSutitucion.isEmpty()) {

            ImpresoDTO impresoRenunciaConSutitucionDTO = ImpresoToDTO.INSTANCE
                    .apply(lstImpresoRenunciaConSutitucion.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoRenunciaConSutitucionDTO(impresoRenunciaConSutitucionDTO);
            this.rellenarCdAsuntoExp(dto, impresoRenunciaConSutitucionDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueContratoDTO = dto.getImpresoRenunciaConSutitucionDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueContratoDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoRenunciaConSutitucionDTO().setLstBloqueDTO(lstBloqueContratoDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO()) {
                
                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        List<Integer> tipoImpresoRenunciaSinSutitucion = new ArrayList<>();
        tipoImpresoRenunciaSinSutitucion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));
        List<Impreso> lstImpresoRenunciaSinSutitucion = this.impresoService
                .buscarPorExpedienteTipo(idExpediente, tipoImpresoRenunciaSinSutitucion);
        if (lstImpresoRenunciaSinSutitucion != null && !lstImpresoRenunciaSinSutitucion.isEmpty()) {

            ImpresoDTO impresoRenunciaSinSutitucionDTO = ImpresoToDTO.INSTANCE
                    .apply(lstImpresoRenunciaSinSutitucion.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoRenunciaSinSutitucionDTO(impresoRenunciaSinSutitucionDTO);
            this.rellenarCdAsuntoExp(dto, impresoRenunciaSinSutitucionDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueContratoDTO = dto.getImpresoRenunciaSinSutitucionDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueContratoDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoRenunciaSinSutitucionDTO().setLstBloqueDTO(lstBloqueContratoDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        List<Integer> tipoImpresoModPresup = new ArrayList<>();
        tipoImpresoModPresup.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
        List<Impreso> lstImpresoModPresup = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoModPresup);
        // Datos Justec Intermedia
        if (lstImpresoModPresup != null && !lstImpresoModPresup.isEmpty()) {
            ImpresoDTO impresoModPresup = ImpresoToDTO.INSTANCE.apply(lstImpresoModPresup.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoModificacionesPresupPlazosOtrosDTO(impresoModPresup);
            this.rellenarCdAsuntoExp(dto, impresoModPresup);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloquePrespModDTO = dto
                    .getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO().stream()
                    .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloquePrespModDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoModificacionesPresupPlazosOtrosDTO()
                    .setLstBloqueDTO(lstBloquePrespModDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()) {

                // Quitamos los registros de baja
                List<BloqueComponenteDTO> lstBloqueComponenteDTO = bloqueDTO
                        .getLstBloqueComponenteDTO().stream()
                        .filter(f -> Objects.isNull(f.getFcBajaFila()))
                        .collect(Collectors.toList());

                // Ordenamos por columna numero de orden
                lstBloqueComponenteDTO.sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                // Agregamos los datos de bloque componente
                bloqueDTO.setLstBloqueComponenteDTO(lstBloqueComponenteDTO);

                // Si es formulario, listado, tabla, presupuesto presentado,
                // ayuda
                // solicitada o texto
                for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                        .getLstBloqueComponenteDTO()) {

                    // Si el componente no es uno de los válidos, lo
                    // ignoramos
                    /*
                     * if (Boolean.FALSE.equals(bloqueDTO.
                     * getEsTipoPresupuestoPresentado()) && Boolean.FALSE
                     * .equals(this.devolverComponentePorTipo(
                     * bloqueComponenteDTO))) { continue; }
                     * 
                     * if (Boolean.FALSE.equals(bloqueDTO.
                     * getEsTipoPresupuestoFinanciable()) && Boolean.FALSE
                     * .equals(this.devolverComponentePorTipo(
                     * bloqueComponenteDTO))) { continue; }
                     */

                    if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                            && Boolean.TRUE.equals(bloqueComponenteDTO.getItRelacion())) {

                        this.rellenarValoresRelacionables(dto, bloqueComponenteDTO);

                    } else {

                        this.rellenarValoresNoRelacionables(dto, bloqueDTO, bloqueComponenteDTO);

                    }
                }

            }
        }
        
        List<Integer> tipoImpresoAceptacion = new ArrayList<>();
        tipoImpresoAceptacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));
        List<Impreso> lstImpresoAceptacion = this.impresoService
                .buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpresoAceptacion);
        // Datos Justec Intermedia
        if (lstImpresoAceptacion != null && !lstImpresoAceptacion.isEmpty()) {
            ImpresoDTO impresoAceptacionDTO = ImpresoToDTO.INSTANCE
                    .apply(lstImpresoAceptacion.get(0));
            // Agregamos los datos del impreso recuperado
            dto.setImpresoAceptacionDTO(impresoAceptacionDTO);
            this.rellenarCdAsuntoExp(dto, impresoAceptacionDTO);
            // Quitamos los registros de baja
            List<BloqueDTO> lstBloqueAceptacionDTO = dto.getImpresoAceptacionDTO()
                    .getLstBloqueDTO().stream().filter(f -> Objects.isNull(f.getFcBajaFila()))
                    .collect(Collectors.toList());

            // Ordenamos por nmOrden
            lstBloqueAceptacionDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Agregamos los datos del bloque procesados
            dto.getImpresoAceptacionDTO().setLstBloqueDTO(lstBloqueAceptacionDTO);

            // Rellenamos los valores de los componentes.
            for (BloqueDTO bloqueDTO : dto.getImpresoAceptacionDTO().getLstBloqueDTO()) {

                //VMM 10/06/25: refactorizamos en un metodo esta logica
                this.rellenarBloquesComponentesForm(dto, bloqueDTO);
            }
        }

        // ExpedienteContrato
        List<ExpedienteContrato> lstExpedienteContra = this.expedienteContratoDAO
                .findByIdExpediente(dto.getIdExpediente());
        if (lstExpedienteContra != null && !lstExpedienteContra.isEmpty()) {

            List<ExpedienteContratoDTO> expContraDTO = new ArrayList<>(
                    Lists.transform(lstExpedienteContra, ExpedienteContratoToDTO.INSTANCE));

            for (ExpedienteContratoDTO expedienteContratoDTO : expContraDTO) {

                if (expedienteContratoDTO.getIdTipoContratoDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_INICIAL))) {
                    dto.setExpedienteContrato(expedienteContratoDTO);
                    if (expedienteContratoDTO.getInteresadoDTO() != null
                            && expedienteContratoDTO.getInteresadoDTO().getIdInteresado() != null) {
                        InteresadoDTO interesadoDTO = this.interesadoService.buscarId(
                                expedienteContratoDTO.getInteresadoDTO().getIdInteresado());
                        dto.getExpedienteContrato().setInteresadoDTO(interesadoDTO);
                    }
                }

                if (expedienteContratoDTO.getIdTipoContratoDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_SUSTITUCION))) {
                    dto.setExpedienteContratoSustitucion(expedienteContratoDTO);
                    if (expedienteContratoDTO.getInteresadoDTO() != null
                            && expedienteContratoDTO.getInteresadoDTO().getIdInteresado() != null) {
                        InteresadoDTO interesadoDTO = this.interesadoService.buscarId(
                                expedienteContratoDTO.getInteresadoDTO().getIdInteresado());
                        dto.getExpedienteContratoSustitucion().setInteresadoDTO(interesadoDTO);
                    }
                }

            }

        }

        return dto;

    }

    private void rellenarBloquesComponentesForm(ExpedienteDTO dto, BloqueDTO bloqueDTO)
            throws ServiceException {
        // Quitamos los registros de baja
        List<BloqueComponenteDTO> lstBloqueComponenteDTO = bloqueDTO
                .getLstBloqueComponenteDTO().stream()
                .filter(f -> Objects.isNull(f.getFcBajaFila()))
                .collect(Collectors.toList());

        // Ordenamos por columna numero de orden
        lstBloqueComponenteDTO.sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

        // Agregamos los datos de bloque componente
        bloqueDTO.setLstBloqueComponenteDTO(lstBloqueComponenteDTO);

        // Si es formulario, listado, tabla, presupuesto presentado,
        // ayuda solicitada o texto
        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())
                || Boolean.TRUE.equals(bloqueDTO.getEsCuentaBancaria())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoSusti())
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoDiscapacidad())
                // VMM MD-2994: sin este flag, rellenarValoresNoRelacionables nunca se ejecuta para los bloques
                // de intensidad de la ayuda, y bloqueComponenteDTO.datos se queda con el valor "de fabrica"
                // de la coleccion lazy BloqueComponente.lstDato (mapeada solo por idBloqueComponente, SIN
                // filtrar por expediente - ese componente se reutiliza en el formulario de muchos expedientes
                // distintos), mostrando el dato de un expediente aleatorio en vez del de este expediente
                || Boolean.TRUE.equals(bloqueDTO.getEsTipoIntensidadAyuda())) {

            for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                    .getLstBloqueComponenteDTO()) {

                // Si el componente no es uno de los válidos, lo ignoramos
                if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                        && Boolean.FALSE.equals(
                    this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                    continue;
                }

                if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                        && Boolean.FALSE.equals(
                    this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                    continue;
                }

                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                        && Boolean.TRUE.equals(bloqueComponenteDTO.getItRelacion())) {

                    this.rellenarValoresRelacionables(dto, bloqueComponenteDTO);

                } else {

                    this.rellenarValoresNoRelacionables(dto, bloqueDTO,
                            bloqueComponenteDTO);

                }
            }
        }
    }

    /**
     * Metodo para rellenar los datos no relacionables del expediente
     *
     * @param dto
     * @param bloqueDTO
     * @param bloqueComponenteDTO
     * @throws ServiceException
     */
    private void rellenarValoresNoRelacionables(ExpedienteDTO dto, BloqueDTO bloqueDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
        // Recuperamos los datos del expediente
        List<DatoDTO> lstDatoDTO = this.datoService.obtenerPorExpediente(dto.getIdExpediente(),
                bloqueComponenteDTO.getIdBloqueComponente());

        // Obtenemos los datos relativos a este bloque
        List<DatoDTO> datosBloqueComponente = lstDatoDTO.stream().filter(
                x -> x.getIdBloqueComponente().equals(bloqueComponenteDTO.getIdBloqueComponente())
                        && x.getExpedienteDTO() != null
                        && x.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente())
                        && Objects.nonNull(x.getNmFila()))
                .sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList());

        // Quitamos todos los registros que estan de baja
        datosBloqueComponente = datosBloqueComponente.stream()
                .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

        bloqueComponenteDTO.setDatos(new ArrayList<>());

        // Si no lo hemos encontrado es porque no es un tipo de
        // dato que se guarde aquí. Lo ignoramos
        if (Boolean.FALSE.equals(datosBloqueComponente.isEmpty())) {

            List<DatoDTO> lstDatos = new ArrayList<>();

            Boolean esDatosJustec = Boolean.FALSE;
            Boolean esDatosPresup = Boolean.FALSE;

            for (DatoDTO dato : datosBloqueComponente) {
                if (dato.getExpedienteJustecDTO() != null
                        && dato.getExpedienteJustecDTO().getIdExpedienteJusTec() != null) {
                    esDatosJustec = Boolean.TRUE;
                }
            }

            for (DatoDTO dato : datosBloqueComponente) {
                if (dato.getExpedienteModifPresPlazoOtraDTO() != null
                        && dato.getExpedienteModifPresPlazoOtraDTO()
                                .getIdExpedienteModifPresPlazoOtra() != null) {
                    esDatosPresup = Boolean.TRUE;
                }
            }

            // Obtenemos cuantas filas tiene el bloque tabla
            Integer nmFilasTabla = this.datoService.obtenerFilasTabla(bloqueDTO);
            // Obtenemos cuantas filas hay en el bloque
            Integer nmFilas = this.datoService.obtenerNmRegistrosBloque(bloqueDTO, nmFilasTabla);

            if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                    && Boolean.FALSE.equals(bloqueComponenteDTO.getItRelacion()))
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoSusti())
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoPlazosEjecAnual())) {
                // Si es formulario y el componente no está
                // relacionado

                if (esDatosJustec.equals(Boolean.TRUE) || esDatosPresup.equals(Boolean.TRUE)) {
                    lstDatos.addAll(datosBloqueComponente);
                } else {
                    lstDatos.add(datosBloqueComponente.get(0));
                }

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoListado()) || Boolean.TRUE.equals(bloqueDTO.getEsCuentaBancaria())) {
                // Es un listado, creamos tantos datos como filas
                // tenga el listado

                for (Integer i = 0; i < nmFilas; i++) {
                    if (i < datosBloqueComponente.size()) {
                        lstDatos.add(datosBloqueComponente.get(i));
                    }

                }

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                    && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
                // Es una tabla, presupuesto presentado o ayuda
                // solicitada, creamos datos sólo de las columnas

                for (Integer i = 0; i < nmFilas; i++) {
                    if (i < datosBloqueComponente.size()) {
                        lstDatos.add(datosBloqueComponente.get(i));
                    }

                }
                // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                Utils.normalizarSeparadorDecimalDatos(lstDatos);
            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoOtrasModificaciones())) {

                // Es de tipo texto
                lstDatos.addAll(datosBloqueComponente);
            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoObservaciones())) {

                // Es de tipo texto
                lstDatos.addAll(datosBloqueComponente);
            } else if(Boolean.TRUE.equals(bloqueDTO.getEsTipoDiscapacidad())) {
                lstDatos.addAll(datosBloqueComponente);
            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                    && Boolean.FALSE.equals(bloqueDTO.getEsTipoOtrasModificaciones()
                            && Boolean.FALSE.equals(bloqueDTO.getEsTipoObservaciones()))) {

                // Es de tipo texto
                lstDatos.add(datosBloqueComponente.get(0));

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoRenuncia()
                            || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoSusti()))) {
                // Es de tipo formulario contrato
                if (esDatosJustec.equals(Boolean.TRUE)) {
                    lstDatos.addAll(datosBloqueComponente);
                } else {
                    lstDatos.add(datosBloqueComponente.get(0));
                }

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                    && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

                lstDatos.addAll(datosBloqueComponente);
                // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                Utils.normalizarSeparadorDecimalDatos(lstDatos);

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                    && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

                lstDatos.addAll(datosBloqueComponente);
                // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                Utils.normalizarSeparadorDecimalDatos(lstDatos);

            } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoPlazosEjecAnual())
                    && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

                lstDatos.addAll(datosBloqueComponente);

            }

            else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada()
                    || Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones()))) {
                if (Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
                    lstDatos.addAll(datosBloqueComponente);
                    // normalizamos coma/punto para que el p:inputNumber de la celda no reviente al renderizar
                    Utils.normalizarSeparadorDecimalDatos(lstDatos);
                }
            }

            else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoIntensidadAyuda())) {
                // Bloque de porcentaje de intensidad de la ayuda (formato ID_PORCENTAJE_INTENSIDAD),
                // es un unico valor, igual que el formulario generico Campo/Numero
                lstDatos.add(datosBloqueComponente.get(0));
                // normalizamos coma/punto para que el campo no muestre/falle con el formato incorrecto
                Utils.normalizarSeparadorDecimalDatos(lstDatos);
            }

            // Guardamos los datos en el bloqueComponente
            bloqueComponenteDTO.setDatos(lstDatos);
        }
    }

    /**
     *
     * @param dto
     * @param bloqueComponenteDTO
     */
    private void rellenarValoresRelacionables(ExpedienteDTO dto,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
        try {

            bloqueComponenteDTO.setDatos(new ArrayList<>());

            Integer idBloqueComponente = bloqueComponenteDTO.getIdBloqueComponente();
            Integer idComponente = bloqueComponenteDTO.getComponenteDTO().getIdComponente();

            Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

            RepresentanteJurDTO representanteJurDTO = this.representanteJurService
                    .obtenerPorInteresado(dto.getInteresadoDTO().getCdNumIden(),
                            solicitud.getInteresado().getCdNumIden());

            String razonSocial = StringUtils.EMPTY;
            String cargo = StringUtils.EMPTY;

            if (representanteJurDTO != null) {
                razonSocial = representanteJurDTO.getEntidadDTO().getInteresadoDTO().getDsNombre();
                cargo = representanteJurDTO.getDsCargo();
            }

            if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TIPO_VIA))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getCdTipoVia() != null
                                ? dto.getInteresadoDTO().getCdTipoVia()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NOMBRE_VIA))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsDireccion() != null
                                ? dto.getInteresadoDTO().getDsDireccion()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NUMERO))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsNumero() != null
                                ? dto.getInteresadoDTO().getDsNumero()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PISO))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsPiso() != null
                                ? dto.getInteresadoDTO().getDsPiso()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PUERTA))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsPuerta() != null
                                ? dto.getInteresadoDTO().getDsPuerta()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CP))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getCdCodigoPostal() != null
                                ? dto.getInteresadoDTO().getCdCodigoPostal()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.MUNICIPIO))) {
                String valorMunicipio = StringUtils.EMPTY;
                if (StringUtils.isNotBlank(dto.getInteresadoDTO().getCdProvincia())
                        && StringUtils.isNotBlank(dto.getInteresadoDTO().getCdLocalidad())) {
                    SucaMunicipioDTO sucaMonicipioDTO = this.sucaMunicipioService.obtenerMunicipio(
                            dto.getInteresadoDTO().getCdProvincia(),
                            dto.getInteresadoDTO().getCdLocalidad());
                    if (sucaMonicipioDTO != null) {
                        valorMunicipio = sucaMonicipioDTO.getDsMunicipio();
                    }
                }
                dto.getValoresRelacionables().put(idBloqueComponente, valorMunicipio);
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PROVINCIA))) {
                String valorProvincia = StringUtils.EMPTY;
                if (StringUtils.isNotBlank(dto.getInteresadoDTO().getCdProvincia())) {
                    CatalogoDTO catalogoDTO = this.sucaProvinciaService
                            .obtenerPorProvincia(dto.getInteresadoDTO().getCdProvincia());
                    if (catalogoDTO != null) {
                        valorProvincia = catalogoDTO.getDescripcion();
                    }
                }
                dto.getValoresRelacionables().put(idBloqueComponente, valorProvincia);
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NIF_NIE))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getCdNumIden() != null
                                ? dto.getInteresadoDTO().getCdNumIden()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.RAZON_SOCIAL))) {
                dto.getValoresRelacionables().put(idBloqueComponente, razonSocial);
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NOMBRE))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsNombre() != null
                                ? dto.getInteresadoDTO().getDsNombre()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.APELLIDO_1))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsApellido1() != null
                                ? dto.getInteresadoDTO().getDsApellido1()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.APELLIDO_2))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsApellido2() != null
                                ? dto.getInteresadoDTO().getDsApellido2()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TELEFONO_FIJO))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsTelefono() != null
                                ? dto.getInteresadoDTO().getDsTelefono()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TELEFONO_MOVIL))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsMovil() != null
                                ? dto.getInteresadoDTO().getDsMovil()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CORREO_ELECTRONICO))) {
                dto.getValoresRelacionables().put(idBloqueComponente,
                        dto.getInteresadoDTO().getDsEmail() != null
                                ? dto.getInteresadoDTO().getDsEmail()
                                : "");
            } else if (idComponente.equals(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CARGO))) {
                dto.getValoresRelacionables().put(idBloqueComponente, cargo);
            }

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
    }

    /**
     * Método que rellena los documentos del expediente
     *
     * @param List<ExpedienteDocDTO>
     * @return List<ExpedienteDocDTO>
     * @throws ServiceException
     */
    @Override
    public List<ExpedienteDocDTO> rellenarDocumentosExpediente(
            List<ExpedienteDocDTO> listExpedienteDocDTO) throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDocFiltrada = new ArrayList<>();

        // Recorremos la lista de documentos del expediente
        for (ExpedienteDocDTO expedienteDocDTO : listExpedienteDocDTO) {
            // Si está dado de baja lo ignoramos
            if (expedienteDocDTO.getFcBajaFila() != null) {
                continue;
            }

            // Recuperamos el nombre de FICA
            expedienteDocDTO.setNombreFicheroFica(
                    this.recuperarNombreFicheroFica(expedienteDocDTO.getCdFica()));

            // No está de baja lo podemos devolver
            lstExpedienteDocFiltrada.add(expedienteDocDTO);
        }

        return lstExpedienteDocFiltrada;
    }

    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO generar(SolicitudDTO solicitudDTO, InteresadoDTO interesadoDTO,
            ConvocatoriaActDTO convocatoriaActDTO, Boolean generaExpediente,
            Integer idExpedientePadre) throws ServiceException {
       // log.info("generar: saii_lib/interesadoDTO/ "+interesadoDTO.getCdNumIden());
        ExpedienteDTO expedienteDTO = new ExpedienteDTO();
        InteresadoDTO representanteLegal = solicitudDTO.getInteresadoRepresentado();
        InteresadoDTO autorizado = solicitudDTO.getInteresadoAutorizado();

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // (CD_USU_ALTA) debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = interesadoDTO.getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(interesadoDTO.getTipoIdentificacionDTO().getIdValor());
        try {
          //  log.info("generar: esInteresadoExtraComunitario/ "+esInteresadoExtraComunitario);
            // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga
            // la convocatoria definido ya sea Area Admin, subdireccion o
            // direccion
            // VMM 30/09/24 MD-611: controlamos si la convocatoria no tiene Area
            // Administrativa
            String cdUnidadAdmin = null;
            if (solicitudDTO.getConvocatoriaDTO().getIdAreaAdministrativa() == null || solicitudDTO
                    .getConvocatoriaDTO().getIdAreaAdministrativa().getIdUnidadAdmin() == null) {
                // buscamos en el nivel superior (subdireccion)
                if (solicitudDTO.getConvocatoriaDTO().getIdSubdireccion() == null || solicitudDTO
                        .getConvocatoriaDTO().getIdSubdireccion().getIdUnidadAdmin() == null) {
                    // establecemos el nivel superior que es obligatorio
                    // (direccion)
                    cdUnidadAdmin = solicitudDTO.getConvocatoriaDTO().getIdDireccion()
                            .getIdUnidadAdmin().toString();
                } else {
                    cdUnidadAdmin = solicitudDTO.getConvocatoriaDTO().getIdSubdireccion()
                            .getIdUnidadAdmin().toString();
                }
            } else {
                cdUnidadAdmin = solicitudDTO.getConvocatoriaDTO().getIdAreaAdministrativa()
                        .getIdUnidadAdmin().toString();
            }

            expedienteDTO.setCdUnidad(cdUnidadAdmin);

            // Asignamos el interesado
            expedienteDTO.setInteresadoDTO(interesadoDTO);
            if(Constantes.TipoActor.SOLICITANTE.equals(convocatoriaActDTO.getTipoActorDTO().getNmConstante()) 
                    && solicitudDTO.getConvocatoriaDTO() != null && solicitudDTO.getConvocatoriaDTO().getIdTipoSolicitud() != null
                    && Constantes.TipoSolicitud.INICIADA_PARTICIPANTE.equals(solicitudDTO.getConvocatoriaDTO().getIdTipoSolicitud().getNmConstante())) {
                expedienteDTO.setRepresentanteLegal(representanteLegal);
                CatalogosValoresDTO tipoSolicitante= new CatalogosValoresDTO();
                tipoSolicitante.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_REPRESENTANTE));
                expedienteDTO.setTipoPersonaSolicitudDTO(tipoSolicitante);
                
            }else {
                if(representanteLegal != null && representanteLegal.getIdInteresado() != null) {
                    expedienteDTO.setRepresentanteLegal(representanteLegal);
                    CatalogosValoresDTO tipoSolicitante= new CatalogosValoresDTO();
                    tipoSolicitante.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_REPRESENTANTE));
                    expedienteDTO.setTipoPersonaSolicitudDTO(tipoSolicitante);
                }
               
                if(autorizado != null && autorizado.getIdInteresado() != null) {
                    expedienteDTO.setAutorizado(autorizado);
                    CatalogosValoresDTO tipoSolicitante= new CatalogosValoresDTO();
                    tipoSolicitante.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_AUTORIZADO));
                    expedienteDTO.setTipoPersonaSolicitudDTO(tipoSolicitante);
                }
                
                if((autorizado == null ||autorizado.getIdInteresado() == null ) && (representanteLegal == null || representanteLegal.getIdInteresado() == null) ){
                    CatalogosValoresDTO tipoSolicitante= new CatalogosValoresDTO();
                    tipoSolicitante.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_INTERESADO));
                    expedienteDTO.setTipoPersonaSolicitudDTO(tipoSolicitante);
                }
            }
            
            

            // Asignamos la solicitud
            expedienteDTO.setIdSolicitud(solicitudDTO.getIdSolicitud());

            // Asignamos el estado interno
            // HLA 09/12/2024 implemento poner el estado aceptado para el caso
            // de permitir participante sin formulario
            if (convocatoriaActDTO != null
                    && convocatoriaActDTO.getItCumplimentaFormulario() != null
                    && Boolean.TRUE.equals(convocatoriaActDTO.getItCumplimentaFormulario())) {
                expedienteDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA)));
                expedienteDTO.getEstadoDTO().setNmConstante(Constantes.EstadoExpediente.INICIADA);
            } else {
                expedienteDTO.setEstadoDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.ACEPTADA)));
                expedienteDTO.getEstadoDTO().setNmConstante(Constantes.EstadoExpediente.ACEPTADA);
            }

            ImpresoDTO impresoDTO = null;

            List<Integer> lstTipoImpreso = new ArrayList<>();
            if (Constantes.TipoActor.SOLICITANTE.equals(convocatoriaActDTO.getTipoActorDTO().getNmConstante())) {
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
                impresoDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpreso);
            } else {
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
                impresoDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpreso);
            }

            // Asignamos el impreso
            if (impresoDTO != null && impresoDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoDTO(impresoDTO);
            } else {
                expedienteDTO.setImpresoDTO(null);
            }

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
            
            /*ImpresoDTO impresoModPresPlazosOtrosDTO = null;
            List<Integer> lstModPresPlazosOtrosDTO = new ArrayList<>();
            if (solicitudDTO.getConvocatoriaDTO().getItPartidasPresupuestarias()
                    .equals(Constantes.AfirmacionNegacion.S)) {
                lstModPresPlazosOtrosDTO.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
                impresoModPresPlazosOtrosDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstModPresPlazosOtrosDTO);
            }

            // Asignamos el impreso
            if (impresoModPresPlazosOtrosDTO != null
                    && impresoModPresPlazosOtrosDTO.getIdImpreso() != null) {
                expedienteDTO
                        .setImpresoModificacionesPresupPlazosOtrosDTO(impresoModPresPlazosOtrosDTO);
            } else {
                expedienteDTO.setImpresoModificacionesPresupPlazosOtrosDTO(null);
            }

            // Asignamos el formulario de justificacion cientifico técnica
            ImpresoDTO impresoJustecIntermediaDTO = null;
            ImpresoDTO impresoJustecFinalDTO = null;

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                    .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                            .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

                List<Integer> lstTipoImpresoI = new ArrayList<>();
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
                impresoJustecIntermediaDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoI);

                List<Integer> lstTipoImpresoF = new ArrayList<>();
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
                impresoJustecFinalDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoF);

                if (impresoJustecIntermediaDTO != null
                        && impresoJustecIntermediaDTO.getIdImpreso() != null) {
                    expedienteDTO.setImpresoJustecDTO(impresoJustecIntermediaDTO);
                } else {
                    expedienteDTO.setImpresoJustecDTO(null);
                }

                if (impresoJustecFinalDTO != null && impresoJustecFinalDTO.getIdImpreso() != null) {
                    expedienteDTO.setImpresoJustecFinalDTO(impresoJustecFinalDTO);
                } else {
                    expedienteDTO.setImpresoJustecFinalDTO(null);
                }
            }
            
            //VMM 26/05/25 MD-1566: Asignamos el formulario de justificacion Economica
            ImpresoDTO impresoJustecEcoIntermediaDTO = null;
            ImpresoDTO impresoJustecEcoFinalDTO = null;

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                    .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                            .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

                List<Integer> lstTipoImpresoIEco = new ArrayList<>();
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
                impresoJustecEcoIntermediaDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoIEco);

                List<Integer> lstTipoImpresoFEco = new ArrayList<>();
                lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
                impresoJustecEcoFinalDTO = this.impresoService.buscarPorConvActor(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoFEco);

                if (impresoJustecEcoIntermediaDTO != null
                        && impresoJustecEcoIntermediaDTO.getIdImpreso() != null) {
                    expedienteDTO.setImpresoJusEcoDTO(impresoJustecEcoIntermediaDTO);
                } else {
                    expedienteDTO.setImpresoJusEcoDTO(null);
                }

                if (impresoJustecEcoFinalDTO != null && impresoJustecEcoFinalDTO.getIdImpreso() != null) {
                    expedienteDTO.setImpresoJusEcoFinalDTO(impresoJustecEcoFinalDTO);
                } else {
                    expedienteDTO.setImpresoJusEcoFinalDTO(null);
                }
            }

            // VMM 23/07/24 MD-475: anyadimos que tambien puede ser actor
            // coordinador para contratos
            // POM 19/08/24 MD-556 Cambiamoo metodo para obtener especificamente
            // impreso de contrato
            // Asignamos el formulario de contrato
            ImpresoDTO impresoContratoDTO = null;
            ImpresoDTO impresoContratoRenunciaDTO = null;
            ImpresoDTO impresoContratoSutitucionDTO = null;
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE)
                    .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                            .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

                List<Integer> lstTipoImpresoContrato = new ArrayList<>();
                lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));

                impresoContratoDTO = this.impresoService.buscarPorConvActorContrato(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);

                lstTipoImpresoContrato = new ArrayList<>();
                lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));

                impresoContratoRenunciaDTO = this.impresoService.buscarPorConvActorContrato(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);

                lstTipoImpresoContrato = new ArrayList<>();
                lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));

                impresoContratoSutitucionDTO = this.impresoService.buscarPorConvActorContrato(
                        solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                        convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);
            }

            // Asignamos el impreso de contrato
            if (impresoContratoDTO != null && impresoContratoDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoContratoDTO(impresoContratoDTO);
            } else {
                expedienteDTO.setImpresoContratoDTO(null);
            }

            if (impresoContratoRenunciaDTO != null
                    && impresoContratoRenunciaDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoRenunciaSinSutitucionDTO(impresoContratoRenunciaDTO);
            } else {
                expedienteDTO.setImpresoRenunciaSinSutitucionDTO(null);
            }

            if (impresoContratoSutitucionDTO != null
                    && impresoContratoSutitucionDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoRenunciaConSutitucionDTO(impresoContratoSutitucionDTO);
            } else {
                expedienteDTO.setImpresoRenunciaConSutitucionDTO(null);
            }*/

            // Establecemos en nmPaso a 1
            expedienteDTO.setNmPaso(1);

            // Actor
            expedienteDTO.getConvocatoriaActDTO().setIdActor(convocatoriaActDTO.getIdActor());

            boolean tieneReq = false;
            boolean tieneEval = false;
            if (Constantes.AfirmacionNegacion.S
                    .equals(solicitudDTO.getConvocatoriaDTO().getItRequisitos())) {

                // Validamos si tiene requisitos configurados
                List<ConvocatoriaReqDTO> lstRequisitos = this.convocatoriaReqService
                        .obtenerRequisitoPorConvocatoriaIdActor(
                                solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                                convocatoriaActDTO.getIdActor());
                if (lstRequisitos != null && !lstRequisitos.isEmpty()) {
                    tieneReq = true;
                }
            }
            if (Constantes.AfirmacionNegacion.S
                    .equals(solicitudDTO.getConvocatoriaDTO().getItEvaluacion())) {

                // Validamos si tiene evaluaciones configurados
                List<InformeDTO> listInformes= this.informeService.findByIdConvocatoriaIdTipoInforme(solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(), null);
                
                if (listInformes != null && !listInformes.isEmpty()) {
                    
                    List<EvaluacionDTO> lstEvaluaciones = this.evaluacionService.obtenerPorInforme(listInformes.get(0));
                    if (lstEvaluaciones != null && !lstEvaluaciones.isEmpty()) {
                        tieneEval = true;
                    }
                
                }
                
            }

            if (convocatoriaActDTO.getTipoActorDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR))) {

                // Asignamos el estado interno de revision administrativa
                expedienteDTO.setEstadoRevAdminDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)));

                // Asignamos el estado interno de revision requisitos
                expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)));

//                // VMM 17/09/24 MD-613: Validamos si tiene activo el check de uso de requisitos
//                if (tieneReq) {
//                    expedienteDTO.setEstadoRevRequiDTO(
//                            new CatalogosValoresDTO(Constantes.EstadoExpediente.REVISION_OK));
//                }

                // Asignamos el estado interno de revision evaluación
                expedienteDTO.setEstadoRevEvalDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)));

                // VMM 17/09/24 MD-613: Validamos si tiene activo el check de
                // evaluaciones
//                if (tieneEval) {
//                    expedienteDTO.setEstadoRevEvalDTO(
//                            new CatalogosValoresDTO(Constantes.EstadoExpediente.EVALUADO_OK));
//                }
            } else {
                
                Integer idEstExpIni = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA);
                
                // Asignamos el estado interno de revision administrativa
                expedienteDTO.setEstadoRevAdminDTO(new CatalogosValoresDTO(idEstExpIni));

                // Asignamos el estado interno de revision de requisitos
                // Si no tiene requisitos la convocatoria se deja en revision_OK
                expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)));

                // Validamos si tiene activo el check de uso de requisitos
                if (tieneReq) {
                    expedienteDTO.setEstadoRevRequiDTO(new CatalogosValoresDTO(idEstExpIni));
                }

                // Asignamos el estado interno de revision evaluación
                // Si no tiene evaluaciones la convocatoria se deja en
                // revision_OK
                expedienteDTO.setEstadoRevEvalDTO(new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)));
                // Asignamos el estado interno de revision evaluación
                // VMM 17/09/24 MD-613: Validamos si tiene activo el check de
                // evaluaciones
                if (tieneEval) {
                    expedienteDTO.setEstadoRevEvalDTO(new CatalogosValoresDTO(idEstExpIni));
                }
            }

            // Auditoria

            if (representanteLegal != null
                    && representanteLegal.getCdNumIden() != null) {
                expedienteDTO.setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
            } else if(autorizado != null && autorizado.getCdNumIden() != null) {
                expedienteDTO.setCdUsuAlta(Utils.valorAuditoria(autorizado.getCdNumIden()));
            } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                expedienteDTO.setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
            }else {
                // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
                // columna (VARCHAR2(12)) aunque el catálogo no lo marque como
                // extracomunitario, se usa igualmente la constante para evitar ORA-12899.
                expedienteDTO.setCdUsuAlta(Utils.valorAuditoria(interesadoDTO.getCdNumIden()));
            }
           // log.info("generar: expedienteDTO/ getCdUsuAlta "+expedienteDTO.getCdUsuAlta());
            expedienteDTO.setFcAltaFila(new Date());

            if (idExpedientePadre != null) {
                expedienteDTO.setIdExpedientePadre(idExpedientePadre);
            } else if (solicitudDTO.getCdExpedienteSol() != null
                    && (convocatoriaActDTO.getTipoActorDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE))
                            || convocatoriaActDTO.getTipoActorDTO().getIdValor()
                                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)))) {
                idExpedientePadre = solicitudDTO.getCdExpedienteSol().contains("-")
                        ? Integer.valueOf(solicitudDTO.getCdExpedienteSol()
                                .substring(solicitudDTO.getCdExpedienteSol().lastIndexOf('-') + 1))
                        : null;
                expedienteDTO.setIdExpedientePadre(idExpedientePadre);

            }

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
           /* if (expedienteDTO.getIdExpedientePadre() != null
                    && (expedienteDTO.getImpresoContratoDTO() == null
                            || (expedienteDTO.getImpresoContratoDTO() != null
                                    && expedienteDTO.getImpresoContratoDTO().getIdImpreso() != null
                                    && expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()
                                            .isEmpty()))) {
                ExpedienteDTO expedientePadre = this.buscarId(expedienteDTO.getIdExpedientePadre());
                if (expedientePadre.getImpresoContratoDTO() != null
                        && expedientePadre.getImpresoContratoDTO().getIdImpreso() != null
                        && !expedientePadre.getImpresoContratoDTO().getLstBloqueDTO().isEmpty()) {
                    impresoContratoDTO = expedientePadre.getImpresoContratoDTO();
                    expedienteDTO.setImpresoContratoDTO(expedientePadre.getImpresoContratoDTO());
                    impresoContratoSutitucionDTO = expedientePadre
                            .getImpresoRenunciaConSutitucionDTO();
                    expedienteDTO.setImpresoRenunciaConSutitucionDTO(
                            expedientePadre.getImpresoRenunciaConSutitucionDTO());
                    impresoContratoRenunciaDTO = expedientePadre
                            .getImpresoRenunciaSinSutitucionDTO();
                    expedienteDTO.setImpresoRenunciaSinSutitucionDTO(
                            expedientePadre.getImpresoRenunciaSinSutitucionDTO());
                }
            }*/

            // añadir idArea y idAreaSubarea
            expedienteDTO.setConvocatoriaAreaDTO(solicitudDTO.getConvocatoriaAreaDTO());
            expedienteDTO
                    .setConvocatoriaAreaSubareaDTO(solicitudDTO.getConvocatoriaAreaSubareaDTO());
            // Añadir submodalidad
            expedienteDTO.setConvocatoriaSubmodDTO(solicitudDTO.getConvocatoriaSubmodDTO());

            Expediente expediente = this
                    .insertOrUpdate(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
            this.expedienteHistDAO.crearHistorico(expediente, null);
            
            //log.info("generar: expedienteDTO/ insertOrUpdate/expediente.getIdExpediente "+expedienteDTO.getCdUsuAlta()+" "+expediente.getIdExpediente());

            // Si genera expediente generamos el código de expediente
            if (Boolean.TRUE.equals(generaExpediente)) {
                String numeroExpediente = expediente.getIdExpediente().toString();
                String areaCientifica = expedienteDTO.getConvocatoriaAreaDTO() != null
                        ? expedienteDTO.getConvocatoriaAreaDTO().getAreaDTO().getCdCodigo()
                        : null;

                String acronimo = solicitudDTO.getConvocatoriaDTO().getCdAcronimo();

                String cdExpediente = acronimo;
                
                // MD-1548 se incluye la abreviatura de la entidad cuando es
                // entidad cerrada
                if (Constantes.AfirmacionNegacion.S
                        .equals(solicitudDTO.getConvocatoriaDTO().getItEntidadCerrada())) {
                    EntidadDTO entidadDTO = this.entidadService
                            .buscarCif(solicitudDTO.getInteresadoDTO().getCdNumIden());
                    cdExpediente += "/" + entidadDTO.getDsAbrev();
                    if (idExpedientePadre != null) {
                        cdExpediente += "-" + areaCientifica;
                    }
                } else {
                    if (StringUtils.isNotBlank(areaCientifica)) {
                        cdExpediente += "/" + areaCientifica;
                    }
                }
                
                cdExpediente += "-" + numeroExpediente;

                expediente.setCdExpediente(cdExpediente);

                String dsValor = this.catalogosValoresService
                        .buscarId(expedienteDTO.getEstadoDTO().getIdValor()).getDsValor();
                expediente.getEstado().setDsValor(dsValor);
            }

            expedienteDTO = ExpedienteToDTO.INSTANCE.apply(this.insertOrUpdate(expediente));

            // restaurar lista de bloques del impreso
            List<Integer> tipoImpresoSolComp = new ArrayList<>();
            if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())
                    || convocatoriaActDTO.getTipoActorDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {
                tipoImpresoSolComp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
            } else {
                tipoImpresoSolComp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
            }
            List<Impreso> lstImpresosSolComp = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoSolComp);

            if (impresoDTO != null
                    && Boolean.TRUE
                            .equals(expedienteDTO.getImpresoDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosSolComp != null && !lstImpresosSolComp.isEmpty()) {
                expedienteDTO.setImpresoDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosSolComp.get(0)));
                expedienteDTO.getImpresoDTO().setLstBloqueDTO(impresoDTO.getLstBloqueDTO());
            }

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
            
            // restaurar lista de bloques del impreso JCT
           /* List<Integer> tipoImpresoInter = new ArrayList<>();
            tipoImpresoInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
            List<Impreso> lstImpresosInter = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoInter);
            if (impresoJustecIntermediaDTO != null
                    && Boolean.TRUE
                            .equals(expedienteDTO.getImpresoJustecDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoJustecIntermediaDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosInter != null && !lstImpresosInter.isEmpty()) {
                expedienteDTO
                        .setImpresoJustecDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosInter.get(0)));
                expedienteDTO.getImpresoJustecDTO()
                        .setLstBloqueDTO(impresoJustecIntermediaDTO.getLstBloqueDTO());
            }

            // restaurar lista de bloques del impreso JCT Final
            List<Integer> tipoImpresoFinal = new ArrayList<>();
            tipoImpresoFinal.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
            List<Impreso> lstImpresosFinal = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoFinal);

            if (impresoJustecFinalDTO != null
                    && Boolean.TRUE.equals(
                            expedienteDTO.getImpresoJustecFinalDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoJustecFinalDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosFinal != null && !lstImpresosFinal.isEmpty()) {
                expedienteDTO.setImpresoJustecFinalDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosFinal.get(0)));
                expedienteDTO.getImpresoJustecFinalDTO()
                        .setLstBloqueDTO(impresoJustecFinalDTO.getLstBloqueDTO());
            }
            
            //VMM 26/05/25 MD-1566: restaurar lista de bloques del impreso Just Eco Intermedia
            List<Integer> tipoImpresoInterEco = new ArrayList<>();
            tipoImpresoInterEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
            List<Impreso> lstImpresosInterEco = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoInterEco);
            if (impresoJustecEcoIntermediaDTO != null
                    && Boolean.TRUE
                            .equals(expedienteDTO.getImpresoJusEcoDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoJustecEcoIntermediaDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosInterEco != null && !lstImpresosInterEco.isEmpty()) {
                expedienteDTO
                        .setImpresoJusEcoDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosInterEco.get(0)));
                expedienteDTO.getImpresoJusEcoDTO()
                        .setLstBloqueDTO(impresoJustecEcoIntermediaDTO.getLstBloqueDTO());
            }

            // restaurar lista de bloques del impreso Just Eco Final
            List<Integer> tipoImpresoFinalEco = new ArrayList<>();
            tipoImpresoFinalEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
            List<Impreso> lstImpresosFinalEco = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoFinalEco);

            if (impresoJustecEcoFinalDTO != null
                    && Boolean.TRUE.equals(
                            expedienteDTO.getImpresoJusEcoFinalDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoJustecEcoFinalDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosFinalEco != null && !lstImpresosFinalEco.isEmpty()) {
                expedienteDTO.setImpresoJusEcoFinalDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosFinalEco.get(0)));
                expedienteDTO.getImpresoJusEcoFinalDTO()
                        .setLstBloqueDTO(impresoJustecEcoFinalDTO.getLstBloqueDTO());
            }

            List<Integer> tipoImpresoContratoInicial = new ArrayList<>();
            tipoImpresoContratoInicial.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
            List<Impreso> lstImpresosContratoInicial = this.impresoService.buscarPorExpedienteTipo(
                    expedienteDTO.getIdExpediente(), tipoImpresoContratoInicial);

            if (impresoContratoDTO != null
                    && Boolean.TRUE.equals(
                            expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoContratoDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosContratoInicial != null && !lstImpresosContratoInicial.isEmpty()
                    && Boolean.FALSE
                            .equals(lstImpresosContratoInicial.get(0).getLstBloque().isEmpty())) {
                expedienteDTO.setImpresoContratoDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosContratoInicial.get(0)));
                expedienteDTO.getImpresoContratoDTO()
                        .setLstBloqueDTO(impresoContratoDTO.getLstBloqueDTO());
            } else if (Boolean.TRUE
                    .equals(expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO().isEmpty())) {
                expedienteDTO.setImpresoContratoDTO(impresoContratoDTO);
            }

            List<Integer> tipoImpresoContratoRenuncia = new ArrayList<>();
            tipoImpresoContratoRenuncia.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));
            List<Impreso> lstImpresosContratoRenuncia = this.impresoService.buscarPorExpedienteTipo(
                    expedienteDTO.getIdExpediente(), tipoImpresoContratoRenuncia);

            if (impresoContratoRenunciaDTO != null
                    && Boolean.TRUE.equals(expedienteDTO.getImpresoRenunciaSinSutitucionDTO()
                            .getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE.equals(impresoContratoRenunciaDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosContratoRenuncia != null && !lstImpresosContratoRenuncia.isEmpty()
                    && Boolean.FALSE
                            .equals(lstImpresosContratoRenuncia.get(0).getLstBloque().isEmpty())) {
                expedienteDTO.setImpresoRenunciaSinSutitucionDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosContratoRenuncia.get(0)));
                expedienteDTO.getImpresoRenunciaSinSutitucionDTO()
                        .setLstBloqueDTO(impresoContratoRenunciaDTO.getLstBloqueDTO());
            }

            List<Integer> tipoImpresoContratoSustitucion = new ArrayList<>();
            tipoImpresoContratoSustitucion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));
            List<Impreso> lstImpresosContratoSustitucion = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(),
                            tipoImpresoContratoSustitucion);
            if (impresoContratoSutitucionDTO != null
                    && Boolean.TRUE.equals(expedienteDTO.getImpresoRenunciaConSutitucionDTO()
                            .getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE
                            .equals(impresoContratoSutitucionDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosContratoSustitucion != null
                    && !lstImpresosContratoSustitucion.isEmpty() && Boolean.FALSE.equals(
                            lstImpresosContratoSustitucion.get(0).getLstBloque().isEmpty())) {
                expedienteDTO.setImpresoRenunciaConSutitucionDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosContratoSustitucion.get(0)));
                expedienteDTO.getImpresoRenunciaConSutitucionDTO()
                        .setLstBloqueDTO(impresoContratoSutitucionDTO.getLstBloqueDTO());
            }

            // restaurar lista de bloques del impreso Modificación
            // presupuestaria, plazos y/u otras
            List<Integer> tipoImpresoModPrespPlazosOtros = new ArrayList<>();
            tipoImpresoModPrespPlazosOtros.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
            List<Impreso> lstImpresosModPrespPlazosOtros = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(),
                            tipoImpresoModPrespPlazosOtros);
            if (impresoModPresPlazosOtrosDTO != null
                    && Boolean.TRUE
                            .equals(expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                                    .getLstBloqueDTO().isEmpty())
                    && Boolean.FALSE
                            .equals(impresoModPresPlazosOtrosDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosModPrespPlazosOtros != null
                    && !lstImpresosModPrespPlazosOtros.isEmpty()) {
                expedienteDTO.setImpresoModificacionesPresupPlazosOtrosDTO(
                        ImpresoToDTO.INSTANCE.apply(lstImpresosModPrespPlazosOtros.get(0)));
                expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                        .setLstBloqueDTO(impresoModPresPlazosOtrosDTO.getLstBloqueDTO());
            }*/

            // Historico
            if (representanteLegal != null && representanteLegal.getCdNumIden() != null) {
                this.expedienteHistDAO.crearHistorico(expediente,
                        Utils.valorAuditoria(representanteLegal.getCdNumIden()));
            } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                this.expedienteHistDAO.crearHistorico(expediente,
                        Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
            } else {
                // Salvaguarda (defensa en profundidad): ver comentario anterior.
                this.expedienteHistDAO.crearHistorico(expediente, Utils.valorAuditoria(interesadoDTO.getCdNumIden()));
            }
            expedienteDTO.setRepresentanteLegal(representanteLegal);

            // Generamos los datos del expediente
            expedienteDTO = this.generarDatosExpediente(expedienteDTO);

            // Generamos los documentos (bloque tipo documento) para el
            // expediente
            expedienteDTO = this.generarDocumentosExpediente(expedienteDTO);

            // Generamos los documentos de contratos (bloque tipo documento)
            // para el
            // expediente si tiene contratos
            if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())
                    || convocatoriaActDTO.getTipoActorDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {
                tipoImpresoSolComp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
            }

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
            
           /* if ((impresoContratoDTO != null
                    && Boolean.FALSE.equals(impresoContratoDTO.getLstBloqueDTO().isEmpty())
                    && lstImpresosContratoInicial != null && !lstImpresosContratoInicial.isEmpty())
                    || expedienteDTO.getIdExpedientePadre() != null
                            && (convocatoriaActDTO.getItContrato().equals(Boolean.TRUE))) {
                expedienteDTO = this.generarDocumentosExpedienteContrato(expedienteDTO);
            }*/

            // Generamos los requisitos del expediente
            expedienteDTO = this.generarRequisitosExpediente(solicitudDTO, expedienteDTO);

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
        return expedienteDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO guardar(ExpedienteDTO expedienteDTO) throws ServiceException {
        return this.guardar(expedienteDTO, Boolean.TRUE);
    }

    /**
     * Guarda el expediente, con la opción de no generar histórico.
     *
     * Usado por guardarDatosExpediente (el guardado genérico invocado en cada "Siguiente"
     * de los wizards del frontal) con crearHistorico=false, para no saturar
     * SAII_EXPEDIENTE_HIST con un registro por cada paso del wizard sin que haya un cambio
     * de estado/evento de negocio real. El resto de llamantes (cambios de estado, concesión,
     * etc.) siguen generando histórico a través del guardar(ExpedienteDTO) de siempre.
     */
    private ExpedienteDTO guardar(ExpedienteDTO expedienteDTO, Boolean crearHistorico) throws ServiceException {

        Expediente expediente = ExpedienteToEntity.INSTANCE.apply(expedienteDTO);
        // VMM 24/09/26 MD-3053: merge() en vez de insertOrUpdate() (saveOrUpdate) - si el llamante ya habia
        // cargado este Expediente en la sesion actual (p.ej. via buscarId() antes de llamar a guardar,
        // como hace ExpedienteServiceImpl.conceder()), ExpedienteToEntity.apply(dto) crea una instancia
        // Java NUEVA con el mismo id y saveOrUpdate() la rechaza con NonUniqueObjectException por haber
        // ya otra instancia asociada a esa fila. merge() concilia el estado sobre la instancia ya
        // gestionada (o la carga si no habia ninguna) en vez de exigir que sea la unica.
        expediente = this.getDao().merge(expediente);
        if (Boolean.TRUE.equals(crearHistorico)) {
            this.expedienteHistDAO.crearHistorico(expediente, null);
        }
        return ExpedienteToDTO.INSTANCE.apply(expediente);


    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO guardarDatosExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        try {
            InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
            Integer idExpediente = expedienteDTO.getIdExpediente();

            // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
            // (CD_USU_ALTA/CD_USU_MODIF) debe usar una constante en lugar de su identificador.
            Boolean esInteresadoExtraComunitario = expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO() != null
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                            .equals(expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

            // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
            // columna (VARCHAR2(12)) aunque el catálogo no lo marque como
            // extracomunitario, se usa igualmente la constante para evitar ORA-12899.
            String cdNumIden = Boolean.TRUE.equals(esInteresadoExtraComunitario)
                    ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                    : Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden());
            if (representanteLegal != null
                    && representanteLegal.getCdNumIden() != null) {

                cdNumIden = Utils.valorAuditoria(representanteLegal.getCdNumIden());
            }

            // caso de contrato de sustitución
            Integer idInteresado = null;
            if (expedienteDTO != null && expedienteDTO.getExpedienteContrato() != null
                    && expedienteDTO.getExpedienteContrato().getInteresadoDTO() != null) {
                idInteresado = expedienteDTO.getExpedienteContrato().getInteresadoDTO()
                        .getIdInteresado();
            }

            if (idInteresado == null && expedienteDTO.getExpedienteContrato() != null
                    && expedienteDTO.getExpedienteContrato().getIdEstadoDTO() != null
                    && expedienteDTO.getExpedienteContrato().getIdEstadoDTO().getIdValor() != null
                    && expedienteDTO.getExpedienteContrato().getIdEstadoDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SIN_SUBIR_SUSTITUCION))) {
                // Obtenemos el tipo de actor
                // Obtenemos la convocatoriaAct del participante
                Integer idActor = expedienteDTO.getConvocatoriaActDTO().getIdActor();
                ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService
                        .buscarId(idActor);
                CatalogosValoresDTO tipoActorDTO = this.catalogosValoresService
                        .buscarId(convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor());

                if (utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_SOLICITANTE)
                        .equals(tipoActorDTO.getCatalogoDTO().getIdCatalogo())
                        || utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_COORDINADOR)
                                .equals(tipoActorDTO.getCatalogoDTO().getIdCatalogo())
                        || utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_PARTICIPANTE)
                                .equals(tipoActorDTO.getCatalogoDTO().getIdCatalogo())) {
                    expedienteDTO.getExpedienteContrato().getInteresadoDTO()
                            .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.FISICA));
                } else {
                    expedienteDTO.getExpedienteContrato().getInteresadoDTO()
                            .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.JURIDICA));
                }

                // Auditoria
                Boolean esInteresadoContratoExtraComunitario = expedienteDTO.getExpedienteContrato()
                        .getInteresadoDTO().getTipoIdentificacionDTO() != null
                        && utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(expedienteDTO.getExpedienteContrato().getInteresadoDTO()
                                        .getTipoIdentificacionDTO().getIdValor());

                if (representanteLegal != null
                        && representanteLegal.getCdNumIden() != null) {
                    expedienteDTO.getInteresadoDTO()
                            .setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));

                } else if (Boolean.TRUE.equals(esInteresadoContratoExtraComunitario)) {
                    expedienteDTO.getInteresadoDTO()
                            .setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
                } else {
                    // Salvaguarda (defensa en profundidad): ver comentario anterior.
                    expedienteDTO.getInteresadoDTO().setCdUsuAlta(Utils.valorAuditoria(expedienteDTO
                            .getExpedienteContrato().getInteresadoDTO().getCdNumIden()));
                }

                expedienteDTO.getExpedienteContrato().getInteresadoDTO().setFcAltaFila(new Date());

                // Insertamos el interesado.
                Interesado interesado = this.interesadoDAO.insert(InteresadoToEntity.INSTANCE
                        .apply(expedienteDTO.getExpedienteContrato().getInteresadoDTO()));
                expedienteDTO.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(interesado));
                expedienteDTO.getExpedienteContrato()
                        .setInteresadoDTO(expedienteDTO.getInteresadoDTO());

            }
            if(representanteLegal != null && representanteLegal.getIdInteresado() == null && representanteLegal.getCdNumIden() != null) {
                List<Interesado> listaInteresado = this.interesadoDAO.obtenerInteresadosByCdNumIdem(representanteLegal.getCdNumIden());
                if (!listaInteresado.isEmpty()) {
                    expedienteDTO.setRepresentanteLegal(InteresadoToDTO.INSTANCE.apply(listaInteresado.get(0)));
                }else {
                    Interesado interesado = this.interesadoDAO.insert(InteresadoToEntity.INSTANCE
                            .apply(representanteLegal));
                    expedienteDTO.setRepresentanteLegal(InteresadoToDTO.INSTANCE.apply(interesado));
                }
            }
                        
            //VMM 16/06/25 MD-1673: controlar que se haga el guardado de impresos adicionales (Justec, Juseco, contratos, modif...)
            //solo en caso de estar el expediente en estado concedido definitivo
            if (this.esConcesionRealizada(expedienteDTO)) {
                        
                // Recuperamos la lista de bloques
                List<BloqueDTO> lstBloqueJustecIntermediaDTO = expedienteDTO.getImpresoJustecDTO()
                        .getLstBloqueDTO();
                List<BloqueDTO> lstBloqueJustecFinalDTO = expedienteDTO.getImpresoJustecFinalDTO()
                        .getLstBloqueDTO();
                List<BloqueDTO> lstBloqueContratoDTO = expedienteDTO.getImpresoContratoDTO()
                        .getLstBloqueDTO();
                List<BloqueDTO> lstBloqueRenunciaContratoDTO = expedienteDTO
                        .getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO();
                List<BloqueDTO> lstBloqueSutitContratoDTO = expedienteDTO
                        .getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO();
                List<BloqueDTO> lstBloqueModificacionPresupPlazosOtrosDTO = expedienteDTO
                        .getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO();
                List<BloqueDTO> lstBloqueJusecoIntermediaDTO = expedienteDTO.getImpresoJusEcoDTO()
                        .getLstBloqueDTO();
                List<BloqueDTO> lstBloqueJusecoFinalDTO = expedienteDTO.getImpresoJusEcoFinalDTO()
                        .getLstBloqueDTO();
                
                List<BloqueDTO> lstBloqueAceptacionDTO = expedienteDTO.getImpresoAceptacionDTO()
                        .getLstBloqueDTO();
                    
                for (BloqueDTO bloqueDTO : lstBloqueJustecIntermediaDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueJustecFinalDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
                
                //VMM 11/06/25 metemos los impresos de Justif_Economica
                for (BloqueDTO bloqueDTO : lstBloqueJusecoIntermediaDTO) {
                    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueJusecoFinalDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueContratoDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueRenunciaContratoDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueSutitContratoDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
    
                for (BloqueDTO bloqueDTO : lstBloqueModificacionPresupPlazosOtrosDTO) {
    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatosPresupuesto(bloqueComponenteDTO, expedienteDTO,
                                cdNumIden);
                        // this.guardarDatosExpedienteDatos(bloqueComponenteDTO,
                        // expedienteDTO);
                    }
                }
                
                
                for (BloqueDTO bloqueDTO : lstBloqueAceptacionDTO) {
                    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {
    
                        this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                    }
                }
            }else{
                //si aun no se ha realizado la concesion
                //VMM 11/06/25 MD-1673: controlar que solo se haga este guardado de impreso de solicitud o complementario antes de la concesión definitiva
                List<BloqueDTO> lstBloqueDTO = expedienteDTO.getImpresoDTO().getLstBloqueDTO();
                
                // Preparamos la lista de formatos de financiacion
                List<Integer> lstFormatoFinanciacion = new ArrayList<>();
                lstFormatoFinanciacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO));
                lstFormatoFinanciacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA));
                lstFormatoFinanciacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES));
                
                for (BloqueDTO bloqueDTO : lstBloqueDTO) {
                    
                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        if (lstFormatoFinanciacion.contains(bloqueDTO.getIdFormato().getIdValor())) {

                            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO)
                                    .equals(bloqueDTO.getIdFormato().getIdValor())) {

                                this.guardarDatosExpedienteDatosPresupuesto(bloqueComponenteDTO,
                                        expedienteDTO, cdNumIden);

                            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES)
                                    .equals(bloqueDTO.getIdFormato().getIdValor())
                                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA)
                                            .equals(bloqueDTO.getIdFormato().getIdValor())) {

                                this.guardarDatosExpedienteDatosActuacionesAyuda(bloqueComponenteDTO,
                                        expedienteDTO, cdNumIden,
                                        bloqueDTO.getIdFormato().getIdValor());
                            }

                        } else {

                            this.guardarDatosExpedienteDatos(bloqueComponenteDTO, expedienteDTO);
                        }
                    }
                }
                
            }//fin VMM 16/06/25 MD-1673
            
            if(expedienteDTO.getExpedienteModifOtraDTO() != null 
                    && expedienteDTO.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO() != null
                    && expedienteDTO.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra() != null) {
                ExpedienteModifPresPlazoOtraDTO  expedienteModifPresPlazoOtraDTO = expedienteDTO.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO();
                expedienteModifPresPlazoOtraService.guardar(expedienteModifPresPlazoOtraDTO);
                
            
            }
            
            // Guardamos los datos del expediente (sin histórico: este guardado genérico se
            // invoca en cada "Siguiente" del wizard, no representa un cambio de estado/evento
            // de negocio real que deba quedar registrado en SAII_EXPEDIENTE_HIST).
            this.guardar(expedienteDTO, Boolean.FALSE);

            // Liberamos transacción con cambios
            this.getDao().flush();

            // Guardamos las observaciones de documentos
            this.guardarDatosExpedienteDocumento(expedienteDTO, cdNumIden);

            // Recuperamos el expediente con todos sus datos rellenos
            return this.buscarId(idExpediente);

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    //VMM 16/06/25 MD-1673: metodo para saber si se ha realizado la concesion en una convocatoria conjunta o en un expte individual
    private boolean esConcesionRealizada(ExpedienteDTO expedienteDTO) {
        boolean esConcedido = false;
    
        //usar esto si en getConvocatoriaActDTO no tengo el idConvocatoria
        //SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(expedienteDTO.getIdExpediente()); 
        
        //if(solicitudDTO != null && solicitudDTO.getConvocatoriaDTO() != null && solicitudDTO.getConvocatoriaDTO().getIdConvocatoria() != null) {
            //Convocatoria convocatoria = this.convocatoriaService.find(solicitudDTO.getConvocatoriaDTO().getIdConvocatoria());
            Convocatoria convocatoria = this.convocatoriaDAO.find(expedienteDTO.getConvocatoriaActDTO().getIdConvocatoria());
            
            TipoResolModalidad tipoResolModalidad = null;
            if (convocatoria.getIdModalidadConvocatoria() != null && 
                    convocatoria.getIdModalidadConvocatoria().getIdValor() != null) {
                tipoResolModalidad = tipoResolModalidadDAO.findByIdModalidad(convocatoria.getIdModalidadConvocatoria().getIdValor());
            }
            
            if (tipoResolModalidad != null && tipoResolModalidad.getCdTipoResolucion() != null 
                && (
                    (tipoResolModalidad.getCdTipoResolucion().equals(Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA)
                        && convocatoria.getIdEstado() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA)
                            .equals(convocatoria.getIdEstado().getIdValor()))
                    ||
                    (tipoResolModalidad.getCdTipoResolucion().equals(Constantes.TipoResolucionModalidad.RESOLUCION_INDIVIDUAL)
                        && expedienteDTO.getEstadoDTO() != null
                        && ( expedienteDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                            .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO))
                                
                                ||expedienteDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores()
                                        .get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST) ))
                                
                                )
                )) {
                esConcedido = true;
            }
            
        return esConcedido;
    }

    private void guardarDatosExpedienteDocumento(ExpedienteDTO expedienteDTO, String cdNumIden) {

        for (ExpedienteDocDTO expedienteDocDTO : expedienteDTO.getLstExpedienteDocDTO()) {
            ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                    .find(expedienteDocDTO.getIdExpedienteDoc());

            // Cambiamos null por empty
            String tlObservacionesNuevo = Objects.toString(expedienteDocDTO.getTlObservaciones(),
                    StringUtils.EMPTY);
            String tlObservacionesOrigen = Objects.toString(expedienteDoc.getTlObservaciones(),
                    StringUtils.EMPTY);
            
            String itAtorizaNuevo= Objects.toString(expedienteDocDTO.getItAutoriza(),
                    StringUtils.EMPTY);
            String itAutorizaOrigin = Objects.toString(expedienteDoc.getItAutoriza(),
                    StringUtils.EMPTY);

            // Si no hay cambios, no guardamos
            if (Boolean.TRUE.equals(tlObservacionesNuevo.equals(tlObservacionesOrigen))
                    && Boolean.TRUE.equals(itAtorizaNuevo.equals(itAutorizaOrigin)) ) {
                continue;
            }

            expedienteDoc.setTlObservaciones(tlObservacionesNuevo);
            expedienteDoc.setItAutoriza(itAtorizaNuevo);
            expedienteDoc.setCdUsuModif(cdNumIden);
            expedienteDoc.setFcModifFila(new Date());
            this.expedienteDocDAO.merge(expedienteDoc);
            // expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);
             this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
             cdNumIden);
        }

    }

//    private void guardarDatosExpedienteDocumento(ExpedienteDTO expedienteDTONew, String cdNumIden)
//            throws ServiceException {
//        
//        if (expedienteDTONew.getLstExpedienteDocDTO() != null && !expedienteDTONew.getLstExpedienteDocDTO().isEmpty()) {
//            for (ExpedienteDocDTO expedienteDocDTONew : expedienteDTONew.getLstExpedienteDocDTO()) {
//                ExpedienteDocDTO expedienteDocDTO = this.expedienteDocService.buscarId(expedienteDocDTONew.getIdExpedienteDoc());
//    
//                // Cambiamos null por empty
//                String tlObservacionesNuevo = Objects.toString(expedienteDocDTONew.getTlObservaciones(),
//                        StringUtils.EMPTY);
//                String tlObservacionesOrigen = Objects.toString(expedienteDocDTO.getTlObservaciones(),
//                        StringUtils.EMPTY);
//    
//                // Si no hay cambios, no guardamos
//                if (Boolean.TRUE.equals(tlObservacionesNuevo.equals(tlObservacionesOrigen))) {
//                    continue;
//                }
//    
//                expedienteDocDTO.setTlObservaciones(tlObservacionesNuevo);
//                expedienteDocDTO.setCdUsuModif(cdNumIden);
//                expedienteDocDTO.setFcModifFila(new Date());
//    //            this.expedienteDocDAO.merge(expedienteDoc);
//                this.expedienteDocService.guardar(expedienteDocDTO);
//                // expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);
//                // this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
//                // cdNumIden);
//            }
//        }
//    }

    /**
     *
     * @param bloqueDTO
     * @param bloqueComponenteDTO
     * @param expedienteDTO
     * @throws ServiceException
     */
    private void guardarDatosExpedienteDatosPresupuesto(BloqueComponenteDTO bloqueComponenteDTO,
            ExpedienteDTO expedienteDTO, String cdNumIden) throws ServiceException {

        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {
            // Recuperamos la lista de datos por bloque
            // componente
            List<DatoDTO> lstDatos = bloqueComponenteDTO.getDatos();

            for (DatoDTO datoDTO : lstDatos) {

                // Convertir valor con símbolo euro a
                // numérico
               // if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                    if (StringUtils.isNotBlank(datoDTO.getDsValor()) && datoDTO.getDsValor().indexOf('€') != -1) {
                        String numero = datoDTO.getDsValor().replace("€", "").replace(".", "")
                                .replace(",", ".").replace('\u00A0', ' ');
                        datoDTO.setDsValor(numero);
                    }
                    if (datoDTO.getExpedienteDTO() == null
                            || datoDTO.getExpedienteDTO().getIdExpediente() == null) {
                        datoDTO.setExpedienteDTO(expedienteDTO);
                    }

                    datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());

                    if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO()
                                    .getIdExpedienteModifPresPlazoOtra())) {
                        datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                                .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                    }
                    if (datoDTO.getIdDato() == null) {
                        datoDTO.setCdUsuAlta(cdNumIden);
                        datoDTO.setFcAltaFila(new Date());
                    } else {
                        datoDTO.setCdUsuModif(cdNumIden);
                        datoDTO.setFcModifFila(new Date());
                    }

                    this.datoService.guardar(datoDTO);

                //}
            }
        } else if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                        .getExpedienteModifPresPlazoOtraDTO())
                && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                        .getExpedienteModifPresPlazoOtraDTO()
                        .getIdExpedienteModifPresPlazoOtra())) {
            // Recuperamos la lista de datos por bloque componente
            List<DatoDTO> lstDatos = bloqueComponenteDTO.getDatos();

            // Guardamos los datos
            for (DatoDTO datoDTO : lstDatos) {
                if (datoDTO.getIdDato() != null) {
                    if (datoDTO.getNmFila() == null) {
                        datoDTO.setNmFila(bloqueComponenteDTO.getNmOrden());
                    }

                    datoDTO.setExpedienteDTO(expedienteDTO);

                    if (Objects.isNull(datoDTO.getIdBloqueComponente())) {
                        datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                    }

                    if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO()
                                    .getIdExpedienteModifPresPlazoOtra())) {
                        datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                                .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                    }
                    datoDTO.setCdUsuModif(cdNumIden);
                    datoDTO.setFcModifFila(new Date());

                    this.datoService.guardar(datoDTO);

                } else if (datoDTO.getIdDato() == null) {
                    datoDTO.setExpedienteDTO(expedienteDTO);

                    if (datoDTO.getNmFila() == null) {
                        datoDTO.setNmFila(bloqueComponenteDTO.getNmOrden());
                    }

                    if (Objects.isNull(datoDTO.getIdBloqueComponente())) {
                        datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                    }

                    if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO())
                            && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                    .getExpedienteModifPresPlazoOtraDTO()
                                    .getIdExpedienteModifPresPlazoOtra())) {
                        datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                                .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                    }

                    if (datoDTO.getIdDato() == null) {
                        datoDTO.setCdUsuAlta(cdNumIden);
                        datoDTO.setFcAltaFila(new Date());
                    } else {
                        datoDTO.setCdUsuModif(cdNumIden);
                        datoDTO.setFcModifFila(new Date());
                    }

                    this.datoService.guardar(datoDTO);
                }

            }

        }

    }

    /**
     *
     * @param bloqueDTO
     * @param bloqueComponenteDTO
     * @param expedienteDTO
     * @throws ServiceException
     */
    private void guardarDatosExpedienteDatosActuacionesAyuda(
            BloqueComponenteDTO bloqueComponenteDTO, ExpedienteDTO expedienteDTO, String cdNumIden,
            Integer formatoBloque) throws ServiceException {

        // Actualizamos los bloque componente seleccionados
        // y no seleccionado
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FINANCIACION_ACTUACIONES).equals(formatoBloque)
                && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

            // Recupeamos los datos del bloque componente
            BloqueComponente bc = this.bloqueComponenteService
                    .find(bloqueComponenteDTO.getIdBloqueComponente());

            // Eliminamos datos de los bloques no seleccionados
            if (Boolean.FALSE.equals(bloqueComponenteDTO.getItSeleccionado())) {
                this.datoService.bajaDatosPorBloqueComponente(expedienteDTO.getIdExpediente(),
                        bc.getBloque().getIdBloque(), bc.getNmOrden() - 1,
                        cdNumIden);

            }

        }

        // Guardamos los datos asociados a las columnas
        if (Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {

            List<DatoDTO> lstDatos = bloqueComponenteDTO.getDatos();

            for (DatoDTO datoDTO : lstDatos) {

                if (StringUtils.isNotBlank(datoDTO.getDsValor())) {

                    Dato dato = new Dato();
                    if (Objects.nonNull(datoDTO.getIdDato())) {
                        dato = this.datoService.find(datoDTO.getIdDato());
                    }

                    if (datoDTO.getDsValor().indexOf('€') != -1) {
                        String numero = datoDTO.getDsValor().replace("€", "").replace(".", "")
                                .replace(",", ".").replace('\u00A0', ' ');
                        dato.setDsValor(numero);
                    } else {
                        dato.setDsValor(datoDTO.getDsValor());
                    }

                    dato.setExpediente(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
                    dato.setBloqueComponente(
                            BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));
                    dato.setNmColumna(datoDTO.getNmColumna());
                    dato.setNmFila(datoDTO.getNmFila());

                    if (datoDTO.getIdDato() == null) {
                        dato.setCdUsuAlta(cdNumIden);
                        dato.setFcAltaFila(new Date());
                    } else {
                        dato.setCdUsuModif(cdNumIden);
                        dato.setFcModifFila(new Date());
                    }

                    this.datoService.insertOrUpdate(dato);

                }
            }

        }

    }

    /**
     *
     * @param bloqueComponenteDTO
     * @param expedienteDTO
     * @throws ServiceException
     */
    private void guardarDatosExpedienteDatos(BloqueComponenteDTO bloqueComponenteDTO,
            ExpedienteDTO expedienteDTO) throws ServiceException {

        // Recuperamos la lista de datos por bloque componente
        InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // (CD_USU_ALTA/CD_USU_MODIF) debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

        List<DatoDTO> lstDatos = bloqueComponenteDTO.getDatos();

        // Guardamos los datos
        for (DatoDTO datoDTO : lstDatos) {
            if (datoDTO.getIdDato() != null && datoDTO.getExpedienteDTO() != null
                    && datoDTO.getExpedienteDTO().getIdExpediente() != null
                    && expedienteDTO.getIdExpediente() != null && datoDTO.getExpedienteDTO()
                            .getIdExpediente().equals(expedienteDTO.getIdExpediente())) {
                if (datoDTO.getNmFila() == null) {
                    datoDTO.setNmFila(bloqueComponenteDTO.getNmOrden());
                }

                if (Objects.isNull(datoDTO.getIdBloqueComponente())) {
                    datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                }

                if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO()
                                .getIdExpedienteModifPresPlazoOtra())) {
                    datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                            .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                }
                if (representanteLegal != null
                        && representanteLegal.getCdNumIden() != null) {
                    datoDTO.setCdUsuModif(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                    datoDTO.setCdUsuModif(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
                } else {
                    datoDTO.setCdUsuModif(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                }

                datoDTO.setFcModifFila(new Date());

                this.datoService.guardar(datoDTO);

            } else if (datoDTO.getIdDato() != null && datoDTO.getExpedienteDTO() != null
                    && datoDTO.getExpedienteDTO().getIdExpediente() == null
                    && expedienteDTO.getIdExpediente() != null) {

                datoDTO.setExpedienteDTO(expedienteDTO);

                if (datoDTO.getNmFila() == null) {
                    datoDTO.setNmFila(bloqueComponenteDTO.getNmOrden());
                }

                if (Objects.isNull(datoDTO.getIdBloqueComponente())) {
                    datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                }

                if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO()
                                .getIdExpedienteModifPresPlazoOtra())) {
                    datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                            .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                }
                if (representanteLegal != null
                        && representanteLegal.getCdNumIden() != null) {
                    datoDTO.setCdUsuModif(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                    datoDTO.setCdUsuModif(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
                } else {
                    datoDTO.setCdUsuModif(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                }
                datoDTO.setFcModifFila(new Date());

                this.datoService.guardar(datoDTO);

                
            
            }else if ((datoDTO.getExpedienteDTO() != null && datoDTO.getExpedienteDTO().getIdExpediente() != null && expedienteDTO.getIdExpediente() != null
                    && datoDTO.getExpedienteDTO().getIdExpediente().equals(expedienteDTO.getIdExpediente()))
                   || datoDTO.getIdDato() == null ){

           
                datoDTO.setExpedienteDTO(expedienteDTO);

                if (datoDTO.getNmFila() == null) {
                    datoDTO.setNmFila(bloqueComponenteDTO.getNmOrden());
                }

                if (Objects.isNull(datoDTO.getIdBloqueComponente())) {
                    datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                }

                if (Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO())
                        && Objects.nonNull(expedienteDTO.getExpedienteModifOtraDTO()
                                .getExpedienteModifPresPlazoOtraDTO()
                                .getIdExpedienteModifPresPlazoOtra())) {
                    datoDTO.setExpedienteModifPresPlazoOtraDTO(expedienteDTO
                            .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO());
                }

                if (datoDTO.getIdDato() == null) {
                    if (representanteLegal != null
                            && representanteLegal.getCdNumIden() != null) {
                        datoDTO.setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                    } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                        datoDTO.setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
                    } else {
                        datoDTO.setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                    }

                    datoDTO.setFcAltaFila(new Date());
                } else {
                    if (representanteLegal != null
                            && representanteLegal.getCdNumIden() != null) {
                        datoDTO.setCdUsuModif(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                    } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                        datoDTO.setCdUsuModif(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
                    } else {
                        datoDTO.setCdUsuModif(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                    }
                    datoDTO.setFcModifFila(new Date());
                }

                this.datoService.guardar(datoDTO);
            }

        }

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final Integer idExpediente) throws ServiceException {
        this.deleteLogico(idExpediente);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final Integer idExpediente) throws ServiceException {
        this.getDao().revertirBaja(idExpediente);
    }

    /**
     *
     */
    @Override
    public List<ExpedienteFiltrosDTO> filtrar(ExpedienteFiltrosDTO dto) throws ServiceException {

        List<ExpedienteFiltrosDTO> lst = null;
        List<Integer> lstTramiteSubsanacion = new ArrayList<>();
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        lstTramiteSubsanacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        Date fechaHoy = new Date();
        try {
            if (dto == null) {
                dto = new ExpedienteFiltrosDTO();
            }
            lst = this.getDao().filtrar(dto);
            for (ExpedienteFiltrosDTO expFiltroDTO : lst) {

                ExpedienteDTO expedDTO = this.buscarId(expFiltroDTO.getIdExpediente());
                String dsAreaCientifica = expedDTO.getConvocatoriaAreaDTO() != null
                        && expedDTO.getConvocatoriaAreaDTO().getIdConvocatoriaArea() != null
                                ? expedDTO.getConvocatoriaAreaDTO().getAreaDTO().getDsValor()
                                : "";
                if (expedDTO.getConvocatoriaAreaSubareaDTO() != null
                        && expedDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO() != null
                        && expedDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO()
                                .getDsValor() != null) {
                    dsAreaCientifica = dsAreaCientifica + " - "
                            + expedDTO.getConvocatoriaAreaSubareaDTO().getSubareaDTO().getDsValor();
                }
                expFiltroDTO.setDsAreaCientifica(dsAreaCientifica);
                if (expFiltroDTO.getTipoResolModalidadConvocatoria() != null
                        && (expFiltroDTO.getTipoResolModalidadConvocatoria().equalsIgnoreCase("C")
                                && expFiltroDTO.getEstadoConvocatoriaDTO().getNmConstante()
                                        .equals(Constantes.EstadoConvocatoria.RESUELTA)
                                && expFiltroDTO.getEstadoSolicitudDTO().getNmConstante()
                                        .equals(Constantes.EstadoSolicitud.CONCEDIDA))

                ) {

                    if (Boolean.TRUE.equals(this.convocatoriaActService
                            .validarConvocatoriaContratoActor(expFiltroDTO.getIdConvocatoria()))) {
                        ExpedienteDTO expContratro = this.buscarId(expFiltroDTO.getIdExpediente());
                        String itTipo = "";
                        if (expContratro.getConvocatoriaActDTO().getTipoActorDTO().getNmConstante()
                                .equals(Constantes.TipoActor.SOLICITANTE)) {
                            itTipo = "PCS";
                        } else if (expContratro.getConvocatoriaActDTO().getTipoActorDTO()
                                .getNmConstante().equals(Constantes.TipoActor.COORDINADOR)) {
                            itTipo = "PC";
                        }
                        List<ExpedienteDTO> listaExpContratoParticipantes = this
                                .buscarExpedientePorSolicitudActor(expContratro.getIdSolicitud(),
                                        expContratro.getConvocatoriaActDTO().getIdActor(), itTipo,
                                        expFiltroDTO.getIdExpediente());

                        listaExpContratoParticipantes = listaExpContratoParticipantes.stream()
                                .filter(x -> x.getConvocatoriaActDTO().getItContrato()
                                        .equals(Boolean.TRUE))
                                .collect(Collectors.toList());
                        if (listaExpContratoParticipantes != null
                                && !listaExpContratoParticipantes.isEmpty()) {
                            expFiltroDTO.setItContrato(Boolean.TRUE);
                            
                            
                            List<ExpedienteDTO> listaExpePendienteSubsanar = listaExpContratoParticipantes
                                    .stream()
                                    .filter(x -> x.getExpedienteContrato().getIdEstadoDTO() != null
                                            && x.getExpedienteContrato().getIdEstadoDTO()
                                                    .getIdValor() != null
                                            && (x.getExpedienteContrato().getIdEstadoDTO()
                                                    .getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION)
                                                    || x.getExpedienteContrato().getIdEstadoDTO()
                                                            .getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_SUBSANACION)))
                                    .collect(Collectors.toList());

                            if (listaExpePendienteSubsanar != null
                                    && !listaExpePendienteSubsanar.isEmpty()) {
                                expFiltroDTO.setNotificacionSubsanarContratos(Boolean.TRUE);
                            }

                        } else {
                            expFiltroDTO.setItContrato(Boolean.FALSE);
                            expFiltroDTO.setNotificacionSubsanarContratos(Boolean.FALSE);
                        }
                    }

                    if (expFiltroDTO.getEstadoDTO().getNmConstante()
                            .equals(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)) {
                        List<ExpedienteJusTecDTO> listPeriodosExpJustec = expedienteJusTecService
                                .buscarPeriodosPendientesJustecPorExpediente(
                                        expFiltroDTO.getIdExpediente(),
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.INICIO_PLAZO_DE_PRESENTACION));
                        expFiltroDTO.setListaExpedienteJustec(listPeriodosExpJustec);
                        List<ExpedienteJusEcoDTO> listPeriodosExpJusEco = expedienteJusEcoService.buscarPeriodosPendientesJusecoPorExpediente(expFiltroDTO.getIdExpediente());
                        expFiltroDTO.setListPeriodosExpJusEco(listPeriodosExpJusEco);
                    }
                } else if (expFiltroDTO.getTipoResolModalidadConvocatoria().equalsIgnoreCase("I")
                        && expFiltroDTO.getEstadoSolicitudDTO().getNmConstante().equals(Constantes.EstadoSolicitud.CONCEDIDA)) {
                    List<ExpedienteDocDTO> listDocExp = this
                            .obtenerDocumentos(expFiltroDTO.getIdExpediente());
                    Boolean resolucionDefinitivoNotificado = Boolean.FALSE;
                    if (expFiltroDTO.getEstadoDTO().getNmConstante()
                            .equals(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)
                            && listDocExp != null && !listDocExp.isEmpty()) {
                        for (ExpedienteDocDTO doc : listDocExp) {
                            if (doc.getTramiteDTO() != null
                                    && doc.getTramiteDTO().getIdValor() != null
                                    && doc.getTramiteDTO().getNmConstante().equals(Constantes.Tramite.CONCESION_DEFINITIVA)
                                    && doc.getIdDocumentoPtfr() != null) {

                                resolucionDefinitivoNotificado = Boolean.TRUE;
                            }
                        }

                        if (resolucionDefinitivoNotificado.equals(Boolean.TRUE)) {
                            // mostrar presentacion JCT
                            List<ExpedienteJusTecDTO> listPeriodosExpJustec = expedienteJusTecService
                                    .buscarPeriodosPendientesJustecPorExpediente(
                                            expFiltroDTO.getIdExpediente(),
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.INICIO_PLAZO_DE_PRESENTACION));
                            expFiltroDTO.setListaExpedienteJustec(listPeriodosExpJustec);
                            
                            List<ExpedienteJusEcoDTO> listPeriodosExpJusEco = expedienteJusEcoService.buscarPeriodosPendientesJusecoPorExpediente(expFiltroDTO.getIdExpediente());
                            expFiltroDTO.setListPeriodosExpJusEco(listPeriodosExpJusEco);
                            // mostrar gestion de contratos
                            if (Boolean.TRUE.equals(
                                    this.convocatoriaActService.validarConvocatoriaContratoActor(
                                            expFiltroDTO.getIdConvocatoria()))) {
                                ExpedienteDTO expContratro = this
                                        .buscarId(expFiltroDTO.getIdExpediente());
                                String itTipo = "";
                                if (expContratro.getConvocatoriaActDTO().getTipoActorDTO()
                                        .getNmConstante().equals(Constantes.TipoActor.SOLICITANTE)) {
                                    itTipo = "PCS";
                                } else if (expContratro.getConvocatoriaActDTO().getTipoActorDTO()
                                        .getNmConstante().equals(Constantes.TipoActor.COORDINADOR)) {
                                    itTipo = "PC";
                                }
                                List<ExpedienteDTO> listaExpContratoParticipantes = this
                                        .buscarExpedientePorSolicitudActor(
                                                expContratro.getIdSolicitud(),
                                                expContratro.getConvocatoriaActDTO().getIdActor(),
                                                itTipo, expFiltroDTO.getIdExpediente());

                                listaExpContratoParticipantes = listaExpContratoParticipantes
                                        .stream().filter(x -> x.getConvocatoriaActDTO()
                                                .getItContrato().equals(Boolean.TRUE))
                                        .collect(Collectors.toList());
                                if (listaExpContratoParticipantes != null
                                        && !listaExpContratoParticipantes.isEmpty()) {
                                    expFiltroDTO.setItContrato(Boolean.TRUE);

                                    List<ExpedienteDTO> listaExpePendienteSubsanar = listaExpContratoParticipantes
                                            .stream().filter(x -> {
                                                return x.getExpedienteContrato()
                                                        .getIdEstadoDTO().getNmConstante()
                                                        .equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION)
                                                        || x.getExpedienteContrato()
                                                                .getIdEstadoDTO().getNmConstante()
                                                                .equals(Constantes.EstadoContrato.PENDIENTE_SUBSANACION);
                                            }).collect(Collectors.toList());

                                    if (listaExpePendienteSubsanar != null
                                            && !listaExpePendienteSubsanar.isEmpty()) {
                                        expFiltroDTO.setNotificacionSubsanarContratos(Boolean.TRUE);
                                    }

                                } else {
                                    expFiltroDTO.setItContrato(Boolean.FALSE);
                                    expFiltroDTO.setNotificacionSubsanarContratos(Boolean.FALSE);
                                }
                            }

                        }

                    }
                }

                if (expFiltroDTO.getEstadoDTO().getNmConstante()
                        .equals(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)) {

                    if (expFiltroDTO.getTipoResolModalidadConvocatoria() != null && ((expFiltroDTO
                            .getTipoResolModalidadConvocatoria().equalsIgnoreCase("C")
                            && expFiltroDTO.getEstadoConvocatoriaDTO().getNmConstante()
                                    .equals(Constantes.EstadoConvocatoria.RESUELTA)
                            && expFiltroDTO.getEstadoSolicitudDTO().getNmConstante()
                                    .equals(Constantes.EstadoSolicitud.CONCEDIDA))
                            || (expFiltroDTO.getTipoResolModalidadConvocatoria()
                                    .equalsIgnoreCase("I")
                                    && expFiltroDTO.getEstadoSolicitudDTO().getNmConstante()
                                            .equals(Constantes.EstadoSolicitud.CONCEDIDA)))) {

                        Boolean modificarPrespPlazosOtros = Boolean.FALSE;
                        List<ExpedienteAnualidadDTO> listaExpAnualidad = this.expedienteAnualidadService
                                .buscarPorExpediente(expFiltroDTO.getIdExpediente());

                     // se modifico en la  MD-2735 para mostrar siemres aunque este fuera de plazo alertando de ello al usuario para confirmar antes de iniciar y antes de presentar
                        
                        if (listaExpAnualidad != null && !listaExpAnualidad.isEmpty()) {
                            for (ExpedienteAnualidadDTO item : listaExpAnualidad) {
                                if (Boolean.TRUE.equals(Utils.fechasInicioEntreRango(
                                        item.getFcIniEjecucion()))) {
                                    modificarPrespPlazosOtros = Boolean.TRUE;
                                }
                            }

                        }

                        expFiltroDTO.setItModificarPrespPlazosOtros(modificarPrespPlazosOtros);

                    }

                    List<ExpedienteJusTecDTO> listPeriodosExpJustecSanar = expedienteJusTecService
                            .buscarPeriodosPendientesSubsanarJustecPorExpediente(
                                    expFiltroDTO.getIdExpediente());
                    boolean mostrarSubsanacionJTC = false;
                    List<ExpedienteJusTecDTO> expedienteJustecSubsanar = new ArrayList<>();
                    for (ExpedienteJusTecDTO item : listPeriodosExpJustecSanar) {
                        mostrarSubsanacionJTC = false;
                        List<ExpedienteDocDTO> listaDocumentos = this
                                .obtenerDocPendienteSubsanarJusTec(item.getIdExpedienteJusTec());
                        if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
                            for (ExpedienteDocDTO doc : listaDocumentos) {
                                if ((doc.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO)
                                        || (doc.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION)))
                                        && doc.getIdDocumentoPtfr() != null) {
                                    DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                                            .find(doc.getIdDocumentoPtfr());
                                    if (documentoPtfr.getFcEnvioNote() != null
                                           && Utils.fechasEntreRango(
                                                    documentoPtfr.getFcIniPlazoSubsanacion(),
                                                    documentoPtfr.getFcFinPlazoSubsanacion())
                                                    .equals(Boolean.TRUE)) {
                                        mostrarSubsanacionJTC = true;
                                    }

                                }
                            }

                            if (mostrarSubsanacionJTC) {
                                expedienteJustecSubsanar.add(item);
                            }

                        }

                    }

                    expFiltroDTO.setListaExpedienteSubsanarJustec(expedienteJustecSubsanar);
                    
                   
                }
                String notificacionNoteLeido = "";
                String estadoNote = null;

                if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_ACEPTACION_DESIST)) {
                    // mostrar formulario aceptación
                    boolean mostrarAceptacionAyuda=false;
                    boolean mostrarAlertaAceptacionAyuda=false;
                    List<Integer> lstTramites = new ArrayList<>();
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
                    
                        List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = documentoPtfrDAO
                                .obtenerPorExpedienteYTramites(expFiltroDTO.getIdExpediente(),
                                        lstTramites);
                        for (DocumentoPtfrDTO docPtfrDTO : lstDocumentoPtfrDTO) {
                            if (Utils.fechasEntreRango(
                                            docPtfrDTO.getFcIniPlazoSubsanacion(),
                                            docPtfrDTO.getFcFinPlazoSubsanacion())
                                            .equals(Boolean.TRUE)) {
                                mostrarAceptacionAyuda = true;
                                expFiltroDTO.setFcIniAceptacion(docPtfrDTO.getFcIniPlazoSubsanacion());
                                expFiltroDTO.setFcFinAceptacion(docPtfrDTO.getFcFinPlazoSubsanacion());
                                
                            }else if(Objects.nonNull(docPtfrDTO.getFcEnvioNote()) && Objects.isNull(docPtfrDTO.getFcIniPlazoSubsanacion()))  {
                                notificacionNoteLeido = "N";
                                mostrarAceptacionAyuda = false;
                                estadoNote = docPtfrDTO.getCdEstadoNote();
                                mostrarAlertaAceptacionAyuda=true;
                                
                                
                            }

                        }
                    
                        expFiltroDTO.setMostrarAceptacionAyuda(mostrarAceptacionAyuda);
                        expFiltroDTO.setLeidoNotificacionNote(notificacionNoteLeido);
                        expFiltroDTO.setMostrarAlertaAceptacionAyuda(mostrarAlertaAceptacionAyuda);
                        expFiltroDTO.setEstadoNote(estadoNote);
                    
                }else if(expFiltroDTO.getEstadoDTO().getNmConstante()
                        .equals(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO)) {
                    
                    List<Integer> lstTramites = new ArrayList<>();
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
                    
                    List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = documentoPtfrDAO.obtenerPorExpedienteYTramites(expFiltroDTO.getIdExpediente(),
                                        lstTramites);
                    
                    for (DocumentoPtfrDTO docPtfrDTO : lstDocumentoPtfrDTO) {
                         if(Objects.nonNull(docPtfrDTO.getFcEnvioNote()) && Objects.isNull(docPtfrDTO.getFcLecturaNote()) && Objects.isNull(docPtfrDTO.getFcIniPlazoSubsanacion()))  {
                            expFiltroDTO.setLeidoConcedidoDefinitivoNotifNote(true);
                         }

                    }
                        
                }
                
                if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)) {
                        List<ExpedienteDocDTO> listaDocSubsanar = obtenerDocumentosSubsanar(
                            expFiltroDTO.getIdExpediente());
                    for (ExpedienteDocDTO expDoc : listaDocSubsanar) {
                        if (expDoc.getIdDocumentoPtfr() != null && (expDoc.getEstadoDTO()
                                .getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO))
                                || (expDoc.getEstadoDTO().getIdValor().equals(
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION))))) {

                            DocumentoPtfr peticionNote = this.documentoPtfrDAO
                                    .find(expDoc.getIdDocumentoPtfr());
                            if (peticionNote != null && lstTramiteSubsanacion
                                    .contains(peticionNote.getTramite().getIdValor())) {

                                
                                if (Objects.nonNull(peticionNote.getFcEnvioNote())

                                        && (Utils
                                                .fechasEntreRango(
                                                        peticionNote.getFcIniPlazoSubsanacion(),
                                                        peticionNote.getFcFinPlazoSubsanacion())
                                                .equals(Boolean.TRUE))
                                        || (peticionNote.getFcFinPlazoSubsanacion() != null
                                                && peticionNote.getFcFinPlazoSubsanacion()
                                                        .before(fechaHoy))) {
                                    notificacionNoteLeido = "S";
                                    estadoNote = peticionNote.getCdEstadoNote();
                                    break;
                                }else if (Objects.nonNull(peticionNote.getFcEnvioNote()) && Objects.isNull(peticionNote.getFcIniPlazoSubsanacion()))  {
                                    notificacionNoteLeido = "N";
                                    estadoNote = peticionNote.getCdEstadoNote();
                                    break;
                                }
                                
                            }

                        }
                    }
                    expFiltroDTO.setLeidoNotificacionNote(notificacionNoteLeido);
                    expFiltroDTO.setEstadoNote(estadoNote);

                }
                
                if (expFiltroDTO.getEstadoDTO().getNmConstante()
                        .equals(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)) {
                    notificacionNoteLeido = "N";
                    estadoNote = null;
                    List<ExpedienteDocDTO> listaDocSubsanar = this
                            .obtenerDocumentos(expFiltroDTO.getIdExpediente());
                    for (ExpedienteDocDTO expDoc : listaDocSubsanar) {
                        if (expDoc.getIdDocumentoPtfr() != null && (expDoc.getEstadoDTO()
                                .getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)))) {
                            DocumentoPtfrDTO peticionNote = this
                                    .obtenerInformacionNotificacion(expDoc.getIdDocumentoPtfr());
                            if (peticionNote != null
                                    && Objects.nonNull(peticionNote.getFcLecturaNote())) {
                                notificacionNoteLeido = "S";
                                estadoNote = peticionNote.getCdEstadoNote();
                            }
                            expFiltroDTO.setLeidoNotificacionNote(notificacionNoteLeido);
                            expFiltroDTO.setEstadoNote(estadoNote);
                        }
                    }
                }

                if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.DESISTIDO)) {
                    notificacionNoteLeido = "N";
                    estadoNote = null;
                    DocumentoPtfrDTO peticionNote = this
                            .obtenerInformacionDesistido(expFiltroDTO.getIdExpediente());

                    if (peticionNote != null && Objects.nonNull(peticionNote.getFcLecturaNote())) {
                        expFiltroDTO.setDesistidoNotificadoLeido("S");
                        expFiltroDTO.setEstadoNote(peticionNote.getCdEstadoNote());
                    }else if(peticionNote != null && Objects.nonNull(peticionNote.getFcEnvioNote())) {
                        expFiltroDTO.setDesistidoNotificadoLeido(notificacionNoteLeido);
                        expFiltroDTO.setEstadoNote(estadoNote);
                    }else {
                        expFiltroDTO.setDesistidoNotificadoLeido(null); 
                    }



                }

                if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.EXCLUIDO)) {

                    notificacionNoteLeido = "N";
                    estadoNote = null;
                    DocumentoPtfrDTO peticionNote = this
                            .obtenerInformacionExcluido(expFiltroDTO.getIdExpediente());
                    if (peticionNote != null && Objects.nonNull(peticionNote.getFcLecturaNote())) {
                        expFiltroDTO.setExcluidoNotificadoLeido("S");
                        expFiltroDTO.setEstadoNote(peticionNote.getCdEstadoNote());
                    }else if(peticionNote != null && Objects.nonNull(peticionNote.getFcEnvioNote())) {
                        expFiltroDTO.setExcluidoNotificadoLeido(notificacionNoteLeido);
                        expFiltroDTO.setEstadoNote(estadoNote);
                    }else {
                        expFiltroDTO.setExcluidoNotificadoLeido(null); 
                    }


                }

                if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)
                        || expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)
                        || expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)
                        || expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)) {

                    Date fcActual = new Date();

                    List<Integer> lstTramites = new ArrayList<>();
                    if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)) {
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
                    } else if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)) {
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
                    }else if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)) {
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL));
                    }else if (expFiltroDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)) {
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL));
                    }

                    List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = documentoPtfrDAO
                            .obtenerPorExpedienteYTramites(expFiltroDTO.getIdExpediente(),
                                    lstTramites);

                    if (lstDocumentoPtfrDTO != null && !lstDocumentoPtfrDTO.isEmpty()) {
                        DocumentoPtfrDTO documentoPtfrDTO = lstDocumentoPtfrDTO.get(0);
                        if (documentoPtfrDTO.getFcEnvioNote() != null) {
                            expFiltroDTO.setFcIniAlegaciones(documentoPtfrDTO.getFcIniPlazoSubsanacion());
                            expFiltroDTO.setFcFinAlegaciones(documentoPtfrDTO.getFcFinPlazoSubsanacion());
                            expFiltroDTO.setFcLecturaNote(documentoPtfrDTO.getFcLecturaNote());
                            expFiltroDTO.setEstadoNote(documentoPtfrDTO.getCdEstadoNote());

                            if (documentoPtfrDTO.getFcLecturaNote() != null) {
                                expFiltroDTO.setLeidoNotificacionNote(Constantes.AfirmacionNegacion.S);
                                expFiltroDTO.setAlertaNoteIndividual(Boolean.FALSE);
                            } else if (documentoPtfrDTO.getFcLecturaNote() == null) {
                                expFiltroDTO.setLeidoNotificacionNote(Constantes.AfirmacionNegacion.N);
                                if (expFiltroDTO.getFcIniAlegaciones() != null && expFiltroDTO.getFcFinAlegaciones() != null && fcActual.after(expFiltroDTO.getFcIniAlegaciones())
                                        && fcActual.before(expFiltroDTO.getFcFinAlegaciones())) {
                                    expFiltroDTO.setAlertaNoteIndividual(Boolean.FALSE);
                                } else if (expFiltroDTO.getFcIniAlegaciones() == null && fcActual.after(documentoPtfrDTO.getFcEnvioNote())) {
                                    expFiltroDTO.setAlertaNoteIndividual(Boolean.TRUE);
                                }
                            }
                           
                         }
                    }

                    ConvocatoriaDTO convocatoriaDTO = convocatoriaService.buscarId(expFiltroDTO.getIdConvocatoria());
                    TipoResolModalidadDTO tipoResolModalidadDTO = tipoResolModalidadService.buscarPorIdModalidad(convocatoriaDTO.getIdModalidadConvocatoria().getIdValor());

                    if ( Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItAlegacion()) &&
                           (( expFiltroDTO.getFcIniAlegaciones() != null
                            && expFiltroDTO.getFcFinAlegaciones() != null
                            && fcActual.after(expFiltroDTO.getFcIniAlegaciones())
                            && fcActual.before(expFiltroDTO.getFcFinAlegaciones())
                            ) || ( expFiltroDTO.getAlertaNoteIndividual() != null && expFiltroDTO.getAlertaNoteIndividual().equals(Boolean.TRUE) ))
                            && (expFiltroDTO.getItAlegacionRevisada() == null
                                    || Constantes.AfirmacionNegacion.N
                                            .equals(expFiltroDTO.getItAlegacionRevisada()))) {
                        expFiltroDTO.setEsPermiteAlegarIndividual(Boolean.TRUE);

                        // recuperamos el documento de alegación
                        List<AtlasFilter> filtros = new ArrayList<>();
                        filtros.add(
                                new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                                        expFiltroDTO.getIdExpediente(), Operador.EQUALS));
                        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES), Operador.EQUALS));
                        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES), Operador.EQUALS));
                        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PRESENTADO)));
                        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);
                        if ((Objects.nonNull(lstExpedienteDoc) && !lstExpedienteDoc.isEmpty())) {
                            expFiltroDTO.setEsPermiteAlegarIndividual(Boolean.FALSE);
                        }
                    } else if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItAlegacion()) &&
                            (expFiltroDTO.getItAlegacionRevisada() == null
                            || Constantes.AfirmacionNegacion.N.equals(expFiltroDTO.getItAlegacionRevisada()))
                            && Constantes.TipoResolucionModalidad.RESOLUCION_INDIVIDUAL.equals(tipoResolModalidadDTO.getCdTipoResolucion()) 
                            && Constantes.ModalidadConvocatoria.ADJUDICACION_DIRECTA.equals(tipoResolModalidadDTO.getIdModalidad().getNmConstante())) {
                        expFiltroDTO.setEsPermiteAlegarIndividual(Boolean.TRUE);
                    }
                }

                if (expFiltroDTO.getTipoConcesionConvocatoriaDTO() != null
                        && expFiltroDTO.getTipoConcesionConvocatoriaDTO().getNmConstante().equals(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {
                    // Buscamos hijos pendientes de subsanar
                    List<Expediente> listHijosPendSubsanarReq = this.getDao()
                            .listExpedientesHijoPorExpedientePadre(expFiltroDTO.getIdExpediente());

                    // Usar un iterador para evitar problemas de modificación
                    // concurrente
                    Iterator<Expediente> iterator = listHijosPendSubsanarReq.iterator();

                    while (iterator.hasNext()) {
                        Expediente exp = iterator.next();

                        List<Integer> tramitesPtfr = new ArrayList<>();
                        tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
                        // MD-2190 No incluimos el trámite de alegación exclusión prvisional en esta lista
                        // tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL));

                        // Obtener los documentos asociados al expediente
                        List<DocumentoPtfrDTO> docPtfr = documentoPtfrDAO
                                .obtenerPorExpedienteYTramites(exp.getIdExpediente(), tramitesPtfr);

                        // Si no hay documentos asociados, eliminar el
                        // expediente de la lista
                        if (docPtfr == null || docPtfr.isEmpty()) {
                            iterator.remove(); // Elimina el elemento actual de
                                               // la lista de manera segura
                        }
                    }

                    if (listHijosPendSubsanarReq != null && !listHijosPendSubsanarReq.isEmpty()) {
                        expFiltroDTO.setListaExpedientesSubsanarReq(Lists
                                .transform(listHijosPendSubsanarReq, ExpedienteToDTO.INSTANCE));

                    }
                }

                expFiltroDTO.setTieneSubsaAdminParticipante(Boolean.FALSE);
                expFiltroDTO.setTieneSubsanacionNoLeida(Boolean.FALSE);
                if (Constantes.TipoSolicitud.INICIADA_PARTICIPANTE.equals(expFiltroDTO.getIdTipoSolicitud().getNmConstante())
                        && Constantes.TipoActor.SOLICITANTE.equals(expFiltroDTO.getTipoActorDTO().getNmConstante())) {
                    Date fcActual = new Date();
                    List<ExpedienteDTO> lstExpedienteDTO = this.buscarExpedientePorSolicitudActor(
                            expFiltroDTO.getIdSolicitud(), expFiltroDTO.getIdActor(), "PS",
                            expFiltroDTO.getIdExpediente());
                    if (lstExpedienteDTO.stream().anyMatch(
                            e -> Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS.equals(e.getEstadoRevAdminDTO().getNmConstante()))) {
                        expFiltroDTO.setTieneSubsaAdminParticipante(Boolean.TRUE);
                    }

                    List<Integer> lstTramites = new ArrayList<>();
                    lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
                    for (ExpedienteDTO expDTO : lstExpedienteDTO) {
                        List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = documentoPtfrDAO
                                .obtenerPorExpedienteYTramites(expDTO.getIdExpediente(),
                                        lstTramites);
                        for (DocumentoPtfrDTO docPtfrDTO : lstDocumentoPtfrDTO) {
                            if (docPtfrDTO.getFcEnvioNote() != null) {
                                Date fcFinLecturaNote = this.cataService.calcularDiasHabiles(
                                        docPtfrDTO.getFcEnvioNote(), 10,
                                        Constantes.Suca.CD_MUNI_MADRID,
                                        expDTO.getInteresadoDTO().getCdLocalidad());
                                if (fcFinLecturaNote != null
                                        && (docPtfrDTO.getFcLecturaNote() == null
                                                && fcActual.before(fcFinLecturaNote))) {
                                    expFiltroDTO.setTieneSubsanacionNoLeida(Boolean.TRUE);
                                }
                            }

                        }
                    }
                }

                
            }

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return lst;
    }

    /**
     *
     */
    @Override
    public ExpedienteDTO buscarSolicitudInteresado(Integer idSolicitud, Integer idInteresado)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, idInteresado,
                Operador.EQUALS));

        List<ExpedienteDTO> listExpedienteDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

        if (Boolean.TRUE.equals(listExpedienteDTO.isEmpty())) {
            return null;
        } else {
            // Busca en la lista de bloques los bloques componentes que no están
            // dados de baja
            ExpedienteDTO expedienteDTO = listExpedienteDTO.get(0);

            List<Integer> tipoImpresoSolComp = new ArrayList<>();
            tipoImpresoSolComp.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));

            List<Impreso> lstImpresosSol = this.impresoService
                    .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoSolComp);
            if (lstImpresosSol != null && !lstImpresosSol.isEmpty()) {
                expedienteDTO.setImpresoDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosSol.get(0)));
            }
            for (Integer i = 0; i < expedienteDTO.getImpresoDTO().getLstBloqueDTO().size(); i++) {
                BloqueDTO bloqueDTO = expedienteDTO.getImpresoDTO().getLstBloqueDTO().get(i);

                // Quitamos los bloques componentes dados de baja y que no sean
                // permitidos para devolver

                List<BloqueComponenteDTO> lstBloqueComponenteDTO = new ArrayList<>();
                for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                        .getLstBloqueComponenteDTO()) {
                    if (bloqueComponenteDTO.getFcBajaFila() != null) {
                        continue;
                    }

                    // Si es un bloque de texto fijo, documento, o, sus
                    // componentes son válidos para devolver, lo añadimos a la
                    // lista
                    if (Constantes.Formatos.ID_TEXTO_FIJO.equals(bloqueDTO.getIdFormato().getNmConstante())
                            || Constantes.Formatos.ID_DOCUMENTO.equals(bloqueDTO.getIdFormato().getNmConstante())
                            || Boolean.TRUE.equals(this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                        lstBloqueComponenteDTO.add(bloqueComponenteDTO);
                    }
                }
                bloqueDTO.setLstBloqueComponenteDTO(lstBloqueComponenteDTO);

            }

            // Quitamos los bloques participantes. No se deben mostrar en MOVA
            List<BloqueDTO> lstBloques = expedienteDTO.getImpresoDTO().getLstBloqueDTO().stream()
                    .filter(x -> Boolean.FALSE.equals(Constantes.Formatos.ID_PARTICIPANTES.equals(x.getIdFormato().getNmConstante())))
                    .collect(Collectors.toList());

            expedienteDTO.getImpresoDTO().setLstBloqueDTO(lstBloques);

            // Si entró como jurídico con otro interesado, se cambia el
            // interesado del expediente por el que lo está editando
            if (Boolean.FALSE.equals(
                    expedienteDTO.getInteresadoDTO().getIdInteresado().equals(idInteresado))) {
                expedienteDTO.setInteresadoDTO(this.interesadoService.buscarId(idInteresado));
                // Se le pone el número de paso a 1 para que comience por el
                // principio del formulario
                expedienteDTO.setNmPaso(1);
            }

            expedienteDTO = this.rellenarDatosExpediente(expedienteDTO);
            expedienteDTO.setLstExpedienteDocDTO(
                    this.rellenarDocumentosExpediente(expedienteDTO.getLstExpedienteDocDTO()));
            return expedienteDTO;
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO cambiarEstado(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException {

        Expediente expediente = this.find(idExpediente);

        CatalogosValores estado = this.catalogosValoresService.find(idEstado);
        expediente.setEstado(estado);

        // Se valida si el estado aprobado provisional se setea el id del estado
        // del expediente al campo estado aprobado provisional
        if (Constantes.EstadoExpediente.APROBADO_PROVISIONAL.equals(estado.getNmConstante())) {

            expediente.setEstadoProvisional(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)));
            expediente.getEstadoProvisional().setDsValor(estado.getDsValor());
			expediente.getEstadoProvisional().setNmConstante(estado.getNmConstante());	

        }

        // Se valida si el estado pendiente de resolución provisional se setea
        // el id del estado
        // del expediente al campo estado pendiente de resolución provisional
        if (Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL.equals(estado.getNmConstante())) {

            expediente.setEstadoProvisional(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL)));
            expediente.getEstadoProvisional().setDsValor(estado.getDsValor());
			expediente.getEstadoProvisional().setNmConstante(estado.getNmConstante());	

        }      
        // Se valida si el estado denegado provisional se setea el id del estado
        // del expediente al campo estado denegado provisional
        if (Constantes.EstadoExpediente.DENEGADO_PROVISIONAL.equals(estado.getNmConstante())) {
            expediente.setEstadoProvisional(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL)));
            expediente.getEstadoProvisional().setDsValor(estado.getDsValor());
			expediente.getEstadoProvisional().setNmConstante(estado.getNmConstante());	
        }
        
        
        if (Constantes.EstadoExpediente.PROPUESTA.equals(estado.getNmConstante()) && expediente.getConvocatoriaAct() != null) {
            
            ConvocatoriaDTO convDTO = this.convocatoriaService.buscarId(expediente.getConvocatoriaAct().getIdConvocatoria());
           
            if(convDTO.getItPartidasPresupuestarias() != null && convDTO.getItPartidasPresupuestarias().equals(Constantes.AfirmacionNegacion.S)) {
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA,expediente.getConvocatoriaAct().getIdConvocatoria() ,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FINANCIACION_IT_TIPO,
                        "PP", Operador.EQUALS));
                
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ACTOR, expediente.getConvocatoriaAct().getIdActor()));
                
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<FinanciacionDTO> listaFinanciacionDTO = this.financiacionService.buscar(0, -1, filtros,
                        null);

                if (!listaFinanciacionDTO.isEmpty()) {
                   expediente.setEstadoRevFinan(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION)));
                }
                
            }

            
            
        }

        expediente.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(idEstado));

        expediente = this.getDao().merge(expediente);

        this.expedienteHistDAO.crearHistorico(expediente, dni);

        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);

        expedienteDTO.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(estado));

        // Comprobamos si es un expediente de Solicitud
        if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
            // Si un expediente de solicitud (expediente que tiene como actor un
            // solicitante)
            // cambia a estado: Desistido, Excluido, Renuncia expresa, Denegado
            // definitivo o Concedido definitivo
            // también cambia el estado de la solicitud
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
            
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA)));
            }

            this.solicitudDAO.update(solicitud);
        }
        return expedienteDTO;
    }

    /**
    * 
    */
    // VMM parece que a este metodo no le llama nadie ni del back ni del front
    // (solo faltaria revisar desde la parte de angular)
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO cambiarEstadoJusTec(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<AtlasFilter>();

        Expediente exp = this.find(idExpediente);

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC, idExpediente,
                Operador.EQUALS));

        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_DOC, "S", Operador.EQUALS));

        List<ExpedienteDocDTO> lstExpedienteDoc = this.expedienteDocService.buscar(0, -1, filtros,
                null);

        if (!lstExpedienteDoc.isEmpty()) {

            boolean todosRevisionOk = true;

            for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDoc) {

                if (!expedienteDocDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.REVISION_OK)) {
                    todosRevisionOk = false;
                    if (expedienteDocDTO.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)) {
                        // VMM 01/10/24 MD-714: comentamos esto para que no
                        // actualice el estado del beneficiario como posible
                        // solucion
//                        exp.setEstadoBenef(new CatalogosValores(
//                                Constantes.EstadoExpedienteBeneficiario.ENVIADA_SUBSANACION));

                        // comentamos esto pero habria que hacerlo por periodos
                        // aqui se deberia ir a consultar con el id_expdte_doc a
                        // la tabla justec_doc los docs de la JCT y setear ahi
                        // el estado porque va por periodos
                        exp.setEstadoRevJusTec(catalogosValoresService.find(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_SUBSANACION_CIENTIFICO_TECNICA)));
                        break;
                    }
                }
            }

            if (!todosRevisionOk) {
                // VMM 01/10/24 MD-714: comentamos esto para que no actualice el
                // estado del beneficiario como posible solucion
//                exp.setEstadoBenef(
//                        new CatalogosValores(Constantes.EstadoExpedienteBeneficiario.EN_REVISION));
                // comentamos esto pero habria que hacerlo por periodos
                exp.setEstadoRevJusTec(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            }

            if (!exp.getEstado().getIdValor().equals(idEstado))
                exp = this.getDao().merge(exp);
        }

        this.expedienteHistDAO.crearHistorico(exp, dni);

        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);

        // Comprobamos si es un expediente de Solicitud
        if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
            // Si un expediente de solicitud (expediente que tiene como actor un
            // solicitante)
            // cambia a estado: Desistido, Excluido, Renuncia expresa, Denegado
            // definitivo o Concedido definitivo
            // también cambia el estado de la solicitud
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA)));
            }
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)
                    .equals(idEstado)) {
                solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA)));
            }

            this.solicitudDAO.update(solicitud);
        }
        return expedienteDTO;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO enviarNoteProvisional(Integer idExpediente, Integer idSolicitud)
            throws ServiceException {

        Solicitud solicitud = this.solicitudDAO.find(idSolicitud);

        NoteEnviarNotificacionDTO inputNotifNoteDTO = new NoteEnviarNotificacionDTO();
        inputNotifNoteDTO.setCdNumIden(solicitud.getInteresado().getCdNumIden());
        inputNotifNoteDTO
                .setUniAdmin(solicitud.getConvocatoria().getIdAreaAdministrativa().getDsUnidad());
        inputNotifNoteDTO.setAsunto(NoteAdmeService.ConstantesNote.ASUNTO_NOTIFICION_RES_PRO);
        String cdNote = this.noteAdmeService.enviarNotificacion(inputNotifNoteDTO);

        String usuarioAuditoria = UserUtils.getUserName().toUpperCase();
        Expediente expediente = this.find(idExpediente);
        expediente.setCdUsuModif(usuarioAuditoria);
        expediente.setFcModifFila(new Date());
        this.insertOrUpdate(expediente);
        this.expedienteHistDAO.crearHistorico(expediente, null);
        return ExpedienteToDTO.INSTANCE.apply(expediente);
    }

    /**
     * Método para validar la respuesta de Portafirmas (envio a Note)
     *
     * @param numeroIdenficacion Número de identificación
     * @param dsExtendUnidad Descripcion Unidad Note
     * @param envioPtfr Datos de envío a PTFR
     * @param outputRecuDoc Datos de respuesta de documento firmado de PTFR
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @SuppressWarnings("unused")
    private String procesarEnvioNote(String numeroIdenficacion, String dsExtendUnidad)
            throws ServiceException {

        NoteEnviarNotificacionDTO inputNotifNoteDTO = new NoteEnviarNotificacionDTO();
        // TODO - El asunto esta mal, se coge de la plantilla
        inputNotifNoteDTO.setAsunto(NoteAdmeService.ConstantesNote.ASUNTO_SUBSANACION_ADM);
        inputNotifNoteDTO.setUniAdmin(dsExtendUnidad);
        inputNotifNoteDTO.setCdNumIden(numeroIdenficacion);
        return this.noteAdmeService.enviarNotificacion(inputNotifNoteDTO);

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO cambiarEstadoRevEval(Integer idExpediente, Integer idEstado, String dni)
            throws ServiceException {

        // Recuperamos el expediente
        Expediente expediente = this.find(idExpediente);

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(expediente);

        // Indica si el cambio del estado inicial del tramite evaluacion a otro
        // estado es permitido
        Boolean bCambioEstadoPermitido = Boolean.FALSE;

        // Si el tramite de evaluacion esta en Pendiente asignación
        // coordinador/es
        // Podemos cambiar a En evaluacion
        if (Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR.equals(expediente.getEstadoRevEval().getNmConstante())
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.TRAMITE_EN_EVALUACION).equals(idEstado)) {
            bCambioEstadoPermitido = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(bCambioEstadoPermitido)) {

            CatalogosValores estado = this.catalogosValoresService.find(idEstado);
            expediente.setEstadoRevEval(estado);

            dto = ExpedienteToDTO.INSTANCE.apply(this.update(expediente));

            // Historico
            this.expedienteHistDAO.crearHistorico(expediente, dni);
        }

        return dto;

    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO cambiarEstadoRevContrato(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException {

        // Recuperamos el expediente
        Expediente expediente = this.find(idExpediente);

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(expediente);

        CatalogosValores estado = this.catalogosValoresService.find(idEstado);
        expediente.setEstadoRevContrato(estado);

        dto = ExpedienteToDTO.INSTANCE.apply(this.update(expediente));

        // Historico
        this.expedienteHistDAO.crearHistorico(expediente, dni);

        return dto;

    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaEstadosExpediente() throws ServiceException {
        // Obtenemos el valor del catálogo
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_EXPEDIENTE));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaEstadosExpedienteBenef() throws ServiceException {
        // Obtenemos el valor del catálogo
        return this.catalogosValoresService
                .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_EXPEDIENTE_BENEFICIARIO));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDTO> buscarParticipantesPorSolicitud(Integer idSolicitud, Integer idActor)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (idActor != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_ID_ACTOR, idActor,
                    Operador.EQUALS));
        }

        return new ArrayList<>(Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));
    }

    /**
     * Método que genera los datos del expediente
     *
     * @param expedienteDTO
     * @return expedienteDTO con los datos
     * @throws ServiceException
     */
    private ExpedienteDTO generarDatosExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        // Ordenamos por nmOrden

        if (expedienteDTO.getImpresoDTO() != null
                && expedienteDTO.getImpresoDTO().getIdImpreso() != null
                && expedienteDTO.getImpresoDTO().getLstBloqueDTO() != null) {
            expedienteDTO.getImpresoDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));
            ImpresoDTO impresoSolicitud = expedienteDTO.getImpresoDTO();
            generarDatosImpresos(expedienteDTO, impresoSolicitud, null);

        }

        //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
        //este codigo se ha pasado al nuevo metodo generarDatosExpedienteOtros
        
        /*if (expedienteDTO.getImpresoContratoDTO() != null
                && expedienteDTO.getImpresoContratoDTO().getIdImpreso() != null
                && expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO() != null) {

            expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));
            ImpresoDTO impresoContrato = expedienteDTO.getImpresoContratoDTO();
            generarDatosImpresos(expedienteDTO, impresoContrato, null);

        }

        if (expedienteDTO.getImpresoRenunciaSinSutitucionDTO() != null
                && expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getIdImpreso() != null
                && expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO() != null) {

            expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));
            ImpresoDTO impresoContrato = expedienteDTO.getImpresoRenunciaSinSutitucionDTO();
            generarDatosImpresos(expedienteDTO, impresoContrato, null);

        }

        if (expedienteDTO.getImpresoRenunciaConSutitucionDTO() != null
                && expedienteDTO.getImpresoRenunciaConSutitucionDTO().getIdImpreso() != null
                && expedienteDTO.getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO() != null) {

            expedienteDTO.getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));
            ImpresoDTO impresoContrato = expedienteDTO.getImpresoRenunciaConSutitucionDTO();
            generarDatosImpresos(expedienteDTO, impresoContrato, null);

        }

        if (expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO() != null
                && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                        .getIdImpreso() != null
                && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                        .getLstBloqueDTO() != null) {

            expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));
            ImpresoDTO impresoPrespMod = expedienteDTO
                    .getImpresoModificacionesPresupPlazosOtrosDTO();
            generarDatosImpresos(expedienteDTO, impresoPrespMod, null);

        }*/

        return expedienteDTO;
    }

    /**
     * Método que genera los documentos del expediente
     *
     * @param expedienteDTO
     * @return expedienteDTO con los documentos
     * @throws ServiceException
     */
    private ExpedienteDTO generarDocumentosExpediente(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        // Ordenamos por nmOrden
        expedienteDTO.getImpresoDTO().getLstBloqueDTO()
                .sort(Comparator.comparing(BloqueDTO::getNmOrden));

        // Obtenemos los bloquesComponente que generen documentos
        for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoDTO().getLstBloqueDTO()) {

            bloqueDTO.getLstBloqueComponenteDTO()
                    .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

            // Si es bloque es de tipo documento
            if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                        .getLstBloqueComponenteDTO()) {

                    // Si está dado de baja lo ignoramos
                    if (bloqueComponenteDTO.getFcBajaFila() != null) {
                        continue;
                    }

                    // Creamos un expedienteDoc de este bloque
                    expedienteDTO.getLstExpedienteDocDTO()
                            .add(this.crearExpedienteDoc(expedienteDTO, bloqueComponenteDTO,
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)));
                }
            }
        }

        return expedienteDTO;
    }

    /**
     * Método que genera los requisitos del expediente
     *
     * @param expedienteDTO
     * @return expedienteDTO con los requisitos
     * @throws ServiceException
     */
    private ExpedienteDTO generarRequisitosExpediente(SolicitudDTO solicitudDTO,
            ExpedienteDTO expedienteDTO) throws ServiceException {

        if (Constantes.AfirmacionNegacion.S
                .equals(solicitudDTO.getConvocatoriaDTO().getItRequisitos())) {

            List<ConvocatoriaReqDTO> lista = this.convocatoriaReqService
                    .obtenerRequisitoPorConvocatoriaIdActor(
                            solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                            expedienteDTO.getConvocatoriaActDTO().getIdActor());

            for (ConvocatoriaReqDTO reqDTO : lista) {
                if (reqDTO.getConvocatoriaSubmodDTO() == null
                        || reqDTO.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() == null
                        || solicitudDTO.getConvocatoriaSubmodDTO() != null
                                && solicitudDTO.getConvocatoriaSubmodDTO()
                                        .getIdConvocatoriaSubmod() != null
                                && reqDTO.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod()
                                        .equals(solicitudDTO.getConvocatoriaSubmodDTO()
                                                .getIdConvocatoriaSubmod())) {

                    List<AtlasFilter> lstFiltros = new ArrayList<>();
                    lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                            expedienteDTO.getIdExpediente(), Operador.EQUALS));
                    lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_REQUISITO,
                            reqDTO.getRequisitoDTO().getIdValor(), Operador.EQUALS));
                    lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    int cantidad = this.expedienteReqDAO.count(lstFiltros);

                    if (cantidad == 0) {
                        ExpedienteReq expedienteReq = new ExpedienteReq();
                        // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
                        // columna (VARCHAR2(12)), se usa la constante de usuario extracomunitario.
                        expedienteReq.setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                        expedienteReq.setFcAltaFila(new Date());
                        expedienteReq
                                .setExpediente(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
                        expedienteReq.setRequisito(
                                CatalogosValoresToEntity.INSTANCE.apply(reqDTO.getRequisitoDTO()));

                        expedienteDTO.getLstExpedienteReqDTO().add(ExpedienteReqToDTO.INSTANCE
                                .apply(this.expedienteReqDAO.insert(expedienteReq)));
                        
                        ExpedienteReqHist expedienteReqHist = new ExpedienteReqHist();
                        expedienteReqHist.setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                        expedienteReqHist.setFcAltaFila(new Date());
                        expedienteReqHist
                                .setExpediente(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
                        expedienteReqHist.setRequisito(
                                CatalogosValoresToEntity.INSTANCE.apply(reqDTO.getRequisitoDTO()));
                        this.expedienteReqHistService.guardar(ExpedienteReqHistToDTO.INSTANCE.apply(expedienteReqHist));
                        
                    }

                }
            }
        }

        return expedienteDTO;
    }

    /**
     * Método para crear el registro dato en una fila a partir del expediente y
     * bloque componente
     *
     * @param expedienteDTO
     * @param bloqueComponenteDTO
     * @param nmFila
     * @return DatoDTO
     * @throws ServiceException
     */
    private DatoDTO crearDato(ExpedienteDTO expedienteDTO, BloqueComponenteDTO bloqueComponenteDTO,
            Integer nmFila, ExpedienteJusTecDTO expedienteJustecDTO) throws ServiceException {
        DatoDTO datoDTO = new DatoDTO();
        datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
        datoDTO.setExpedienteDTO(expedienteDTO);
        if (expedienteJustecDTO != null) {
            datoDTO.setExpedienteJustecDTO(expedienteJustecDTO);
        }

        // Se usa el cdUsuAlta del expediente
        datoDTO.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        datoDTO.setFcAltaFila(new Date());
        datoDTO.setNmFila(nmFila);

        return this.datoService.guardar(datoDTO);
    }
    
    
    /**
     * Método para crear el registro dato en una fila a partir del expediente y
     * bloque componente de JustifEconomica
     *
     * @param expedienteDTO
     * @param bloqueComponenteDTO
     * @param nmFila
     * @return DatoDTO
     * @throws ServiceException
     */
    private DatoDTO crearDatoEco(ExpedienteDTO expedienteDTO, BloqueComponenteDTO bloqueComponenteDTO,
            Integer nmFila, ExpedienteJusEcoDTO expedienteJusEcoDTO) throws ServiceException {
        DatoDTO datoDTO = new DatoDTO();
        datoDTO.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
        datoDTO.setExpedienteDTO(expedienteDTO);
        if (expedienteJusEcoDTO != null) {
            datoDTO.setExpedienteJusEcoDTO(expedienteJusEcoDTO);
        }

        // Se usa el cdUsuAlta del expediente
        datoDTO.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        datoDTO.setFcAltaFila(new Date());
        datoDTO.setNmFila(nmFila);

        return this.datoService.guardar(datoDTO);
    }

    /**
     * Método para crear el registro expedienteDoc a partir del expediente y
     * bloque componente. Un documento por bloque componente
     *
     * @param expedienteDTO
     * @param bloqueComponenteDTO
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO crearExpedienteDoc(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO, Integer tramite) throws ServiceException {
        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
        CatalogosValores estado = catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
        expedienteDoc.setEstado(estado);
        
        //VMM 17/07/25 MD-1778: error al setear catalogovalores sin generarlo antes (object references an unsaved transient instance)
        if (bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO() != null &&
                bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
            CatalogosValores tipoDoc = catalogosValoresService.find(bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor());
            expedienteDoc.setTipoDocumento(tipoDoc);
        }

        Expediente expAux = new Expediente();
        expAux.setIdExpediente(expedienteDTO.getIdExpediente());
        expedienteDoc.setExpediente(expAux);
        expedienteDoc
                .setBloqueComponente(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));

        // Agregamos los indicadores de tramite y si es o no un adicional
        expedienteDoc.setTramite(catalogosValoresService.find(tramite));
        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);

        // Se usa el cdUsuAlta del expediente
        expedienteDoc.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        expedienteDoc.setFcAltaFila(new Date());
        //expedienteDoc.setItAutoriza(null);
        return ExpedienteDocToDTO.INSTANCE.apply(this.expedienteDocDAO.insert(expedienteDoc));
    }

    /**
     * Método que filtra los componentes para Datos o MOVA Si el componente no
     * está en esta lista, no se guarda en Datos o devuelve a MOVA
     *
     * @param bloqueComponenteDTO
     * @return Boolean true -> se puede devolver o guardar
     * @throws ServiceException
     */
    private Boolean devolverComponentePorTipo(BloqueComponenteDTO bloqueComponenteDTO) {

        // Si el componente no es uno de los válidos, lo ignoramos
        List<Integer> componentesValidos = new ArrayList<>();
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO));
        componentesValidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO));
        if (Boolean.FALSE.equals(componentesValidos
                .contains(bloqueComponenteDTO.getComponenteDTO().getTipoDTO().getIdValor()))) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Devuelve el nombre del fichero subido a FICA
     *
     * @param cdFica código de FICA
     * @return nombre del fichero
     */
    @Override
    public String recuperarNombreFicheroFica(Integer cdFica) throws ServiceException {

        String nombreFichero = StringUtils.EMPTY;

        try {
            if (Objects.nonNull(cdFica)) {
                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);

                if (Objects.nonNull(ficaFichero)) {
                    //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                    String rawFilename = ficaFichero.getNombreFichero();
                    nombreFichero = this.toUnicodeEscaped(rawFilename);
                }
            }
        } catch (Exception e) {
            log.error("Error recuperando metadados de fica: " + cdFica);
            log.error(e.getMessage());
        }

        return nombreFichero;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO comprobarInteresadoExpediente(ExpedienteDTO expedienteDTO,
            String dniInteresado) throws ServiceException {

        // Comprueba si el interesado del expediente es una entidad
        EntidadDTO entidadDTO = this.entidadService
                .buscarCif(expedienteDTO.getInteresadoDTO().getCdNumIden());

        // No es null, cambiamos el interesado por el representante que ha
        // entrado por token
        if (entidadDTO.getIdEntidad() != null) {
            InteresadoDTO interesadoDTO = this.interesadoService
                    .obtenerPorIdentificacion(dniInteresado);
            expedienteDTO.setInteresadoDTO(interesadoDTO);
            expedienteDTO.setCdUsuModif(Utils.valorAuditoria(interesadoDTO.getCdNumIden()));
            expedienteDTO.setFcModifFila(new Date());
            expedienteDTO = this.guardar(expedienteDTO);
        }

        return expedienteDTO;
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocDTO> obtenerDocumentosSubsanar(Integer idExpediente)
            throws ServiceException {

        List<ExpedienteDocDTO> lstExpedienteDocDTO = null;

        List<Integer> listaEstados = new ArrayList<>();
        listaEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
        listaEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));

        List<AtlasFilter> filtrosDocs = new ArrayList<>();
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                idExpediente, Operador.EQUALS));
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, listaEstados,
                Operador.IN));
        List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(0, -1, filtrosDocs,
                null);

        if (listaExpedienteDoc != null) {

            lstExpedienteDocDTO = new ArrayList<>();

            for (ExpedienteDoc doc : listaExpedienteDoc) {
                ExpedienteDocDTO dto = ExpedienteDocToDTO.INSTANCE.apply(doc);

                dto.setNombreFicheroFica(this.recuperarNombreFicheroFica(dto.getCdFica()));

                lstExpedienteDocDTO.add(dto);
            }
        }

        return lstExpedienteDocDTO;
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocDTO> obtenerDocumentosRequisitos(
            List<ExpedienteReqDTO> lstExpedienteReqDTO) throws ServiceException {

        List<ExpedienteDocDTO> listExpedienteDocDTO = new ArrayList<>();

        for (ExpedienteReqDTO expedienteReqDTO : lstExpedienteReqDTO) {

            // Recuperamos el ExpedienteDoc asociado a cada
            // ExpedienteReq
            if (Objects.nonNull(expedienteReqDTO.getIdExpedienteDoc())) {
                ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                        .apply(this.expedienteDocDAO.find(expedienteReqDTO.getIdExpedienteDoc()));

                // Recuperamos el nombre de FICA
                expedienteDocDTO.setNombreFicheroFica(
                        this.recuperarNombreFicheroFica(expedienteDocDTO.getCdFica()));

                // Lo devolvemos
                listExpedienteDocDTO.add(expedienteDocDTO);
            }
        }

        return listExpedienteDocDTO;
    }

    /**
     *
     */
    @Override
    public List<ExpedienteReqDTO> obtenerRequisitosSubsanar(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCUMPLE,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteReqDTO> listaExpedienteReqDTO = new ArrayList<>(Lists
                .transform(this.expedienteReqDAO.find(filtros, null), ExpedienteReqToDTO.INSTANCE));

        if (Boolean.FALSE.equals(listaExpedienteReqDTO.isEmpty())) {
            Expediente exp = this.find(idExpediente);


            // Quitamos los que no estén Pendiente Subsanación Requisitos
            listaExpedienteReqDTO.removeIf(x -> Boolean.FALSE
                    .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS.equals(exp.getEstadoRevRequi().getNmConstante())));
        }

        return listaExpedienteReqDTO;
    }

    /**
    *
    */
    @Override
    public List<ExpedienteReqDTO> obtenerRequisitosNotificaciones(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.expedienteReqDAO.find(filtros, null),
                ExpedienteReqToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO finalizarSubsanacion(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            Boolean esAdministrativo, String cdNumIden, String dniUserLogin,
            String cdNumIdenFuncional) throws ServiceException {

        // validamos datos de entrada
        this.validarFinalizarSubsanacion(lstExpedienteDocDTO, cdNumIden, esAdministrativo);

        Integer idTramite = 0;

        // llamamos al proceso correspondiente
        if (Boolean.TRUE.equals(esAdministrativo)) {
            this.finalizarSubsanacionAdministrativo(lstExpedienteDocDTO, cdNumIden, Boolean.FALSE,
                    Boolean.FALSE,dniUserLogin, cdNumIdenFuncional);
            idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);
        } else {
            this.finalizarSubsanacionRequisito(lstExpedienteDocDTO, cdNumIden);
            idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO);
        }


        // retornamos respuesta ereg
        return this.prepararRespuestaEreg(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.FALSE,
                Boolean.FALSE, Boolean.TRUE, idTramite, lstExpedienteDocDTO, cdNumIdenFuncional);
    }

    /**
     * Metodo para validar los datos antes de finalizar la subsanacion
     *
     * @param lstExpedienteDocDTO
     * @param cdNumIden
     * @param esAdministrativo
     * @throws ServiceException
     */
    private void validarFinalizarSubsanacion(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            String cdNumIden, Boolean esAdministrativo) throws ServiceException {

        String msgNoProcede = "No se pudo finalizar la subsanación. Contacte con el administrador.";

        if (Objects.isNull(lstExpedienteDocDTO) || lstExpedienteDocDTO.isEmpty()
                || StringUtils.isBlank(cdNumIden)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // Todos tienen el mismo expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.FALSE);

        List<ExpedienteDocDTO> listaExpedienteDocDTO = expedienteDTO.getLstExpedienteDocDTO();

            // Validar si existen documento sin subsanar
        Boolean existeDocSinSubsanar = listaExpedienteDocDTO.stream()
                .anyMatch(f -> f.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

        if (Boolean.TRUE.equals(existeDocSinSubsanar)) {
            throw new ServiceException("Debe completar la subsanación antes de finalizar.");
        }
        
        // Validar si existen documentos subsanados
        Boolean existeDocSubsanado = listaExpedienteDocDTO.stream()
                .anyMatch(f -> Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION.equals(f.getEstadoDTO().getNmConstante()));

        if (Boolean.FALSE.equals(existeDocSubsanado) && Boolean.FALSE
                .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS.equals(expedienteDTO.getEstadoRevAdminDTO().getNmConstante()))) {
            throw new ServiceException("No se encontraron documentos subsanados a procesar");
        }

        // Validar si existen peticiones de portafirmas
        List<Integer> lstPeticionesPortafirmas = listaExpedienteDocDTO.stream()
                .map(ExpedienteDocDTO::getIdDocumentoPtfr).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (lstPeticionesPortafirmas.isEmpty()) {
            log.error("No se encontró petición a portafirmas a procesar");
            throw new ServiceException(msgNoProcede);
        }

         

        List<ExpedienteReqDTO> listaExpedienteReqDTO = expedienteDTO.getLstExpedienteReqDTO();
        if(listaExpedienteReqDTO != null && !listaExpedienteReqDTO.isEmpty()) {
            Long pendienteReq = listaExpedienteReqDTO.stream()
                    .filter(f -> Constantes.AfirmacionNegacion.N.equals(f.getItCumple())
                            && f.getIdExpedienteDoc() == null)
                    .count();

            if (pendienteReq > 0) {
                throw new ServiceException(
                        "Debe completar la subsanación de todos los requisitos.");
            }

           /* List<Integer> lstPeticionesPortafirmas = new ArrayList<>();

            for (ExpedienteReqDTO expedienteReqDTO : expedienteDTO.getLstExpedienteReqDTO()) {
                if (expedienteReqDTO.getDocumentoPtfrDTO() != null
                        && expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null) {
                    lstPeticionesPortafirmas
                            .add(expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr());
                }
            }

            if (lstPeticionesPortafirmas.isEmpty()) {
                log.error("No se encontró petición a portafirmas a procesar");
                throw new ServiceException(msgNoProcede);
            }*/

        }
       
        

    }

    private void validarFinalizarSubsanacionJustec(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            String cdNumIden, Boolean esAdministrativo) throws ServiceException {

        String msgNoProcede = "No se pudo finalizar la subsanación. Contacte con el administrador.";

        if (Objects.isNull(lstExpedienteDocDTO) || lstExpedienteDocDTO.isEmpty()
                || StringUtils.isBlank(cdNumIden)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // Todos tienen el mismo expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.FALSE);

        expedienteDTO.setLstExpedienteDocDTO(lstExpedienteDocDTO);
        List<ExpedienteDocDTO> listaExpedienteDocDTO = lstExpedienteDocDTO;

        // Validar si existen documento sin subsanar

        Boolean existeDocSinSubsanar = listaExpedienteDocDTO.stream().anyMatch(f -> f.getEstadoDTO()
                .getNmConstante().equals(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

        if (Boolean.TRUE.equals(existeDocSinSubsanar)) {
            throw new ServiceException("Debe completar la subsanación antes de finalizar.");
        }

        // POM MD-951: 17/10/2024 No debe revisar
        /*
         * // Validar si existen documentos subsanados Boolean
         * existeDocSubsanado = listaExpedienteDocDTO.stream() .anyMatch(f ->
         * Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION
         * .equals(f.getEstadoDTO().getIdValor()));
         * 
         * 
         * if (Boolean.FALSE.equals(existeDocSubsanado) && Boolean.FALSE
         * .equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS
         * .equals(expedienteDTO.getEstadoRevAdminDTO().getIdValor()))) { throw
         * new
         * ServiceException("No se encontraron documentos subsanados a procesar"
         * ); }
         */

        // Validar si existen peticiones de portafirmas
        List<Integer> lstPeticionesPortafirmas = listaExpedienteDocDTO.stream()
                .map(ExpedienteDocDTO::getIdDocumentoPtfr).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (lstPeticionesPortafirmas.isEmpty()) {
            log.error("No se encontró petición a portafirmas a procesar");
            throw new ServiceException(msgNoProcede);
        }

    }

    @Override
    @Transactional(readOnly = false)
    public Boolean iniciarSubsanacionReq(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            String cdNumIden) throws ServiceException {
        // validamos datos de entrada
        this.validarFinalizarSubsanacion(lstExpedienteDocDTO, cdNumIden, false);
        for (ExpedienteDocDTO doc : lstExpedienteDocDTO) {
            ExpedienteDoc expDoc = expedienteDocService.find(doc.getIdExpedienteDoc());
            CatalogosValores estadoExpDoc = catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR));
            expDoc.setEstado(estadoExpDoc);
            expDoc.setTlObservaciones(doc.getTlObservaciones());
            expDoc.setCdUsuModif(cdNumIden);
            expDoc.setFcModifFila(new Date());
            expDoc = this.expedienteDocDAO.merge(expDoc);
            this.expedienteDocHistDAO.crearHistorico(expDoc, cdNumIden);
        }

        Expediente expediente = this
                .find(lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente());
        expediente.setEstadoRevRequi(catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_SUBSANACION)));
        expediente.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
        expediente.setFcModifFila(new Date());
        expediente = this.getDao().merge(expediente);
        // Historico
        this.expedienteHistDAO.crearHistorico(expediente,
                Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
        // retornamos respuesta
        return true;
    }

    /**
     * Metodo para preparar la respuesta de EREG
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    private RegistroEregDTO prepararRespuestaEreg(Integer idExpediente,
            Boolean esDescargaZipSolicitud, Boolean esDescargaZipAlegacion,
            Boolean esDescargaZipSubsanacion, Integer idTramite,
            List<ExpedienteDocDTO> lstDocumentos, String cdNumIdenFuncional)
            throws ServiceException {

        // Recuperamos los datos actualizados del expediente
        ExpedienteDTO expedienteDTO = this.buscarId(idExpediente);

        // Generamos el listado de anexos en ereg
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setCdNumIden(cdNumIdenFuncional);
        expedienteDTO.setRepresentanteLegal(interesadoDTO);
        String cdListadoDocEreg = this.pdfService.generarFirmarListadoAnexos(expedienteDTO,
                idTramite, null, lstDocumentos);

        if (StringUtils.isBlank(cdListadoDocEreg)) {
            throw new ServiceException(
                    "No se pudo finalizar el proceso. Contacte con el administrador.");
        }

        // Recuperamos el registro de la lista de anexos nueva
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdListadoDocEreg,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteDocDTO> lstExpedienteDoc = this.expedienteDocService.buscar(0, -1, filtros,
                null);

        // Recuperamos el identificador del registro, necesario para la opción
        // descargar la alegacion desde formulario de respuesta EREG
        Integer idExpedienteDoc = lstExpedienteDoc.get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdListadoDocEreg);
        if(esDescargaZipSubsanacion != null && esDescargaZipSubsanacion.equals(Boolean.TRUE)) {
            String cdEreg = expedienteDTO.getCdEregSubsanaAdm() != null? expedienteDTO.getCdEregSubsanaAdm():expedienteDTO.getCdEregSubsanaReq();
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(cdEreg));
        }else if(esDescargaZipAlegacion != null && esDescargaZipAlegacion.equals(Boolean.TRUE)) {
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEregAlegaciones())); 
        }else {
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEreg()));
        }
        
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSolicitud(esDescargaZipSolicitud);
        registroEregDTO.setEsDescargaZipAlegacion(esDescargaZipAlegacion);
        registroEregDTO.setEsDescargaZipSubsanacion(esDescargaZipSubsanacion);

        return registroEregDTO;

    }

    /**
     * Metodo para preparar la respuesta de EREG para alegaciones
     *
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    private RegistroEregDTO prepararRespuestaEregAleg(ExpedienteDTO expedienteDTO,
            Boolean esDescargaZipSolicitud, Boolean esDescargaZipAlegacion,
            Boolean esDescargaZipSubsanacion, String asunto, Integer idTramite)
            throws ServiceException {
        return this.prepararRespuestaEregAleg(expedienteDTO, esDescargaZipSolicitud,
                esDescargaZipAlegacion, esDescargaZipSubsanacion, asunto, idTramite, null);
    }

    /**
     * @param lstExpDocDTO documentos adicionales a anexar al listado de
     *        anexos de la entrada EREG (puede ser null para los trámites que
     *        no los necesitan, p.ej. alegaciones)
     */
    private RegistroEregDTO prepararRespuestaEregAleg(ExpedienteDTO expedienteDTO,
            Boolean esDescargaZipSolicitud, Boolean esDescargaZipAlegacion,
            Boolean esDescargaZipSubsanacion, String asunto, Integer idTramite,
            List<ExpedienteDocDTO> lstExpDocDTO)
            throws ServiceException {

        String dniLogin = expedienteDTO.getDniUserLogin();
        String cifEntidadLogin= expedienteDTO.getCifEntidadLogin();

        if (idTramite != null
                // MD-2190 se quita la condición de ser un trámite de alegación exclusión provisional
//                && idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL))
                && expedienteDTO.getIdExpedientePadre() != null) {
            ExpedienteDTO expedientePadreDTO = this
                    .buscarIdSimple(expedienteDTO.getIdExpedientePadre(), Boolean.FALSE);
            expedienteDTO.setCdEreg(expedientePadreDTO.getCdEreg());
        }

        // Generamos el listado de anexos en ereg
        String cdListadoDocEreg = this.pdfService.generarFirmarListadoAnexos(expedienteDTO,
                idTramite, asunto, lstExpDocDTO);

        if (StringUtils.isBlank(cdListadoDocEreg)) {
            throw new ServiceException(
                    "No se pudo finalizar el proceso. Contacte con el administrador.");
        }
        
        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(idTramite,
                expedienteDTO, dniLogin, cifEntidadLogin);  

        // Recuperamos el registro de la lista de anexos nueva
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdListadoDocEreg,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteDocDTO> lstExpedienteDoc = this.expedienteDocService.buscar(0, -1, filtros,
                null);

        // Recuperamos el identificador del registro, necesario para la opción
        // descargar la alegacion desde formulario de respuesta EREG
        Integer idExpedienteDoc = lstExpedienteDoc.get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdListadoDocEreg);
        if(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES)
                .equals(idTramite)) {
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEregAlegaciones()));
        }else if(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.NOTIFICACION_CIUDADANO)
                .equals(idTramite)) {
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEregNotifiCiud()));
        }else {
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEreg()));
        }
       
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSolicitud(esDescargaZipSolicitud);
        registroEregDTO.setEsDescargaZipAlegacion(esDescargaZipAlegacion);
        registroEregDTO.setEsDescargaZipSubsanacion(esDescargaZipSubsanacion);

        return registroEregDTO;

    }

    /**
     * Metodo para finalizar la subsanacion administrativa
     *
     * @param lstExpedienteDocDTO
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    private void finalizarSubsanacionAdministrativo(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            String cdNumIden, Boolean esSubsanarContrato, Boolean esSubsanarJustec, String dniLogin,
            String cdNumIdenFuncional) throws ServiceException {

        // Guardamos las observaciones de los documentos antes de finalizar
        // la subsanación administrativa
        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {
            if (Objects.nonNull(expedienteDocDTO.getIdExpedienteDoc())) {
                ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                        .find(expedienteDocDTO.getIdExpedienteDoc());
                expedienteDoc.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
                expedienteDoc.setCdUsuModif(cdNumIden);
                expedienteDoc.setFcModifFila(new Date());
                expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);
                 this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                 cdNumIden);
            }
        }

        // Todos tienen el mismo expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.TRUE);
        
        // crear un registro de entrada nuevo y actualizar la columna en expediente (queda pendiente)
           
            expedienteDTO.setFcFirmaSubsanaAdm(new Date());
            ImpresoDTO impresoCoordinadorDTO = null;
            // Si existe impreso se recupera el asunto
            if (expedienteDTO.getImpresoDTO() != null
                    && StringUtils.isNotBlank(expedienteDTO.getImpresoDTO().getCdIdentificacion())) {
                impresoCoordinadorDTO = expedienteDTO.getImpresoDTO();
                Integer indexContains = impresoCoordinadorDTO.getCdIdentificacion().indexOf('_');
                if (indexContains < 0) {
                    expedienteDTO.setCdAsunto(impresoCoordinadorDTO.getCdIdentificacion());
                } else {
                    expedienteDTO.setCdAsunto(
                            impresoCoordinadorDTO.getCdIdentificacion().substring(0, indexContains));
                }
            }
            
         // Se recupera el asunto del dto
            String cdAsunto = expedienteDTO.getCdAsunto();

            // Se recupera el código de la unidad administrativa
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

            Integer unidadAdmin = this.pdfService.calcularUnidadAdmin(solicitud);

            UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

            String cdUnidad = unidadAdminDTO.getCdUnidad();
            // **********************************************************//
            // 1.- Registro de Entrada a EREG.
            // **********************************************************/
            log.info("[PASO 1] - Registro de entrada en EREG");
            log.info("Se va a generar la entrada en EREG de presentacion de subsanacion de administrativa");
            expedienteDTO = this.eregAdmeService.generarEntradaSubsanarAdm(expedienteDTO, cdAsunto, cdUnidad, Boolean.TRUE);
            expedienteDTO.setCdEregSubsanaAdm(expedienteDTO.getCdEregSubsanaAdm());
            expedienteDTO.setDsDestinoEreg(expedienteDTO.getDsDestinoEreg());
            log.info("Se ha generado la entrada: ");
            log.info("Código: " + expedienteDTO.getCdEregSubsanaAdm());
            log.info("Destino: " + expedienteDTO.getDsDestinoEreg());
            
            Expediente espedienteEdit = this.expedienteDAO.find(expedienteDTO.getIdExpediente());
            espedienteEdit.setCdEregSubsanaAdm(expedienteDTO.getCdEregSubsanaAdm());
            espedienteEdit.setFcFirmaSubsanaAdm(expedienteDTO.getFcFirmaSubsanaAdm());
            this.expedienteDAO.update(espedienteEdit);
            // Salvaguarda (defensa en profundidad): ver Utils.valorAuditoria.
            this.expedienteHistDAO.crearHistorico(espedienteEdit, Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
     
        
        List<Integer> lstPeticionesPortafirmas = expedienteDTO.getLstExpedienteDocDTO().stream()
                .map(ExpedienteDocDTO::getIdDocumentoPtfr).filter(Objects::nonNull)
                .collect(Collectors.toList());

        lstPeticionesPortafirmas = lstPeticionesPortafirmas.stream().distinct()
                .collect(Collectors.toList());

        for (Integer idDocumentoPtfr : lstPeticionesPortafirmas) {

            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO.find(idDocumentoPtfr);

            if (Objects.isNull(documentoPtfr)) {
                throw new ServiceException("No se encontró datos de envío a Portafirmas");
            }

            // recuperar documento firmado de PTFR
            PtfrRecuperaDocFirmadoDTO docFirmadoDTO = this.ptfrAdmeService
                    .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                            documentoPtfr.getCdArchivoPtfr().toString());

            if (Objects.isNull(docFirmadoDTO) || Objects.isNull(docFirmadoDTO.getPdfB64())) {
                throw new ServiceException(
                        "No se pudo obtener el documento firmado de Portafirmas");
            }

            byte[] ficheroRespuesta = Base64.decode(docFirmadoDTO.getPdfB64());

            // añadir documento a registro EREG existente
            this.eregAdmeService.anexarFicheroAnexo(expedienteDTO.getCdEregSubsanaAdm(), ficheroRespuesta,
                    Constantes.Ereg.LISTADO_DOC_SUBSANADOS);

            this.finalizarSubsanacionDocumentosExpediente(expedienteDTO.getIdExpediente(),
                    documentoPtfr.getIdDocumentoPtfr(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION),
                    esSubsanarContrato, esSubsanarJustec, null,expedienteDTO.getCdEregSubsanaAdm(),expedienteDTO.getFcFirmaSubsanaAdm());
            
            this.finalizarSubsanacionRequisitosExpediente(expedienteDTO.getIdExpediente(),
                    documentoPtfr.getIdDocumentoPtfr(),expedienteDTO.getCdEregSubsanaAdm(),expedienteDTO.getFcFirmaSubsanaAdm());
            String cifEntidadLogin= null;
            if(!cdNumIdenFuncional.equalsIgnoreCase(dniLogin)) {
                cifEntidadLogin=cdNumIdenFuncional;
            }
            Integer idTramite=utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA);
            if(esSubsanarContrato) {
                idTramite=utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL);
            }
            this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(idTramite,
                    expedienteDTO, dniLogin, cifEntidadLogin);

        }

    }

    /**
     * Metodo para finalizar la subsanacion requisitos
     *
     * @param lstExpedienteDocDTO
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    public void finalizarSubsanacionRequisito(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            String cdNumIden) throws ServiceException {

        // Guardamos las observaciones de los documentos antes de finalizar
        // la subsanación de requisitos
        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {
            if (Objects.nonNull(expedienteDocDTO.getIdExpedienteDoc())) {
                ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                        .find(expedienteDocDTO.getIdExpedienteDoc());
                expedienteDoc.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
                expedienteDoc.setCdUsuModif(cdNumIden);
                expedienteDoc.setFcModifFila(new Date());
                expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);
                 this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                 cdNumIden);
            }
        }

        // Todos tienen el mismo expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.TRUE);
        
     // crear un registro de entrada nuevo y actualizar la columna en expediente (queda pendiente)
        
        expedienteDTO.setFcFirmaSubsanaReq(new Date());
        ImpresoDTO impresoCoordinadorDTO = null;
        // Si existe impreso se recupera el asunto
        if (expedienteDTO.getImpresoDTO() != null
                && StringUtils.isNotBlank(expedienteDTO.getImpresoDTO().getCdIdentificacion())) {
            impresoCoordinadorDTO = expedienteDTO.getImpresoDTO();
            Integer indexContains = impresoCoordinadorDTO.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                expedienteDTO.setCdAsunto(impresoCoordinadorDTO.getCdIdentificacion());
            } else {
                expedienteDTO.setCdAsunto(
                        impresoCoordinadorDTO.getCdIdentificacion().substring(0, indexContains));
            }
        }
        
     // Se recupera el asunto del dto
        String cdAsunto = expedienteDTO.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        Integer unidadAdmin = this.pdfService.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();
        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de presentacion de subsanacion de requisitos");
        expedienteDTO = this.eregAdmeService.generarEntradaSubsanarReq(expedienteDTO, cdAsunto, cdUnidad, Boolean.TRUE);
        expedienteDTO.setCdEregSubsanaReq(expedienteDTO.getCdEregSubsanaReq());
        expedienteDTO.setDsDestinoEreg(expedienteDTO.getDsDestinoEreg());
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteDTO.getCdEregSubsanaReq());
        log.info("Destino: " + expedienteDTO.getDsDestinoEreg());
        
        Expediente espedienteEdit = this.expedienteDAO.find(expedienteDTO.getIdExpediente());
        espedienteEdit.setCdEregSubsanaReq(expedienteDTO.getCdEregSubsanaReq());
        espedienteEdit.setFcFirmaSubsanaReq(expedienteDTO.getFcFirmaSubsanaReq());
        this.expedienteDAO.update(espedienteEdit);
        // Salvaguarda (defensa en profundidad): ver Utils.valorAuditoria.
        this.expedienteHistDAO.crearHistorico(espedienteEdit, Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));

        List<Integer> lstPeticionesPortafirmas = new ArrayList<>();

        for (ExpedienteReqDTO expedienteReqDTO : expedienteDTO.getLstExpedienteReqDTO()) {
            if (expedienteReqDTO.getDocumentoPtfrDTO() != null
                    && expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null) {
                lstPeticionesPortafirmas
                        .add(expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr());
            }
        }

        lstPeticionesPortafirmas = lstPeticionesPortafirmas.stream().distinct()
                .collect(Collectors.toList());

        for (Integer idDocumentoPtfr : lstPeticionesPortafirmas) {

            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO.find(idDocumentoPtfr);

            if (Objects.isNull(documentoPtfr)) {
                throw new ServiceException("No se encontró datos de envío a Portafirmas");
            }

            // recuperar documento firmado de PTFR
            PtfrRecuperaDocFirmadoDTO docFirmadoDTO = this.ptfrAdmeService
                    .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                            documentoPtfr.getCdArchivoPtfr().toString());

            if (Objects.isNull(docFirmadoDTO) || Objects.isNull(docFirmadoDTO.getPdfB64())) {
                throw new ServiceException(
                        "No se pudo obtener el documento firmado de Portafirmas");
            }

            byte[] ficheroRespuesta = Base64.decode(docFirmadoDTO.getPdfB64());

            // añadir documento a registro EREG existente
            this.eregAdmeService.anexarFicheroAnexo(expedienteDTO.getCdEregSubsanaReq(), ficheroRespuesta,
                    Constantes.Ereg.LISTADO_DOC_SUBSANADOS);

            this.finalizarSubsanacionRequisitosExpediente(expedienteDTO.getIdExpediente(),
                    documentoPtfr.getIdDocumentoPtfr(),expedienteDTO.getCdEregSubsanaReq(),expedienteDTO.getFcFirmaSubsanaReq());

        }

    }

    /**
     * Metodo para finalizar la gestion de subsanacion requisitos
     *
     * @param lstExpedienteDocDTO
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    private void finalizarGestionSubsanacionRequisito(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            List<ExpedienteDTO> lstExpedienteDTO, String cdNumIden, ExpedienteDTO expedientePadre)
            throws ServiceException {

        // Guardamos las observaciones de los documentos antes de finalizar
        // la subsanación de requisitos
        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {
            if (Objects.nonNull(expedienteDocDTO.getIdExpedienteDoc())) {
                ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                        .find(expedienteDocDTO.getIdExpedienteDoc());
                expedienteDoc.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
                expedienteDoc.setCdUsuModif(cdNumIden);
                expedienteDoc.setFcModifFila(new Date());
                expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);
                 this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                 cdNumIden);            }
        }
        ExpedienteDTO expedienteDTO;
        if (expedientePadre != null) {
            expedienteDTO = expedientePadre;
        } else {
            // Si no hay expedientePare todos los documentos tienen el mismo
            // expediente
            expedienteDTO = this.buscarIdSimple(
                    lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(), Boolean.FALSE);
        }

        List<Integer> lstPeticionesPortafirmas = new ArrayList<>();

        for (ExpedienteDTO expHijoDTO : lstExpedienteDTO) {
            expHijoDTO = this.buscarIdSimple(expHijoDTO.getIdExpediente(), Boolean.FALSE);
            for (ExpedienteReqDTO expedienteReqDTO : expHijoDTO.getLstExpedienteReqDTO()) {
                if (expedienteReqDTO.getDocumentoPtfrDTO() != null
                        && expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null
                        && expedienteReqDTO.getDocumentoPtfrDTO().getTramiteDTO() != null
                        && expedienteReqDTO.getDocumentoPtfrDTO().getTramiteDTO().getNmConstante()
                                .equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)) {
                    lstPeticionesPortafirmas
                            .add(expedienteReqDTO.getDocumentoPtfrDTO().getIdDocumentoPtfr());
                }
            }
        }

        lstPeticionesPortafirmas = lstPeticionesPortafirmas.stream().distinct()
                .collect(Collectors.toList());

        for (Integer idDocumentoPtfr : lstPeticionesPortafirmas) {

            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO.find(idDocumentoPtfr);

            if (Objects.isNull(documentoPtfr)) {
                throw new ServiceException("No se encontró datos de envío a Portafirmas");
            }

            // recuperar documento firmado de PTFR
            PtfrRecuperaDocFirmadoDTO docFirmadoDTO = this.ptfrAdmeService
                    .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                            documentoPtfr.getCdArchivoPtfr().toString());

            if (Objects.isNull(docFirmadoDTO) || Objects.isNull(docFirmadoDTO.getPdfB64())) {
                throw new ServiceException(
                        "No se pudo obtener el documento firmado de Portafirmas");
            }

            byte[] ficheroRespuesta = Base64.decode(docFirmadoDTO.getPdfB64());

            // añadir documento a registro EREG existente
            this.eregAdmeService.anexarFicheroAnexo(expedienteDTO.getCdEreg(), ficheroRespuesta,
                    Constantes.Ereg.LISTADO_DOC_SUBSANADOS);

            this.finalizarSubsanacionRequisitosGestion(documentoPtfr.getIdExpediente(),
                    lstExpedienteDocDTO, cdNumIden);

        }

    }

    /**
     * Método para actualizar estados de documentos y expediente al finalizar la
     * subsanación administrativa
     *
     * @param listaExpedienteDoc Lista de documento
     * @param expedienteDTO Datos de expediente
     * @return Datos de expediente actualizado
     * @throws ServiceException Excepción ocasionado en el servicio
     */
    private void finalizarSubsanacionDocumentosExpediente(Integer idExpediente,
            Integer idDocumentoPtfr, Integer estado, Boolean esSubsanarContrato,
            Boolean esSubsanarJustec, ExpedienteJusTecDTO expdteJusTecDTO, String cdEregSubsanacionAdmin, Date fechaFirmaSubsanacion) throws ServiceException {

        Expediente expediente = this.find(idExpediente);

        // actualizar estados de documentos
        List<AtlasFilter> filtrosDocs = new ArrayList<>();
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR, idDocumentoPtfr,
                Operador.EQUALS));
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION), Operador.EQUALS));
        filtrosDocs.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(filtrosDocs, null);

        if (Objects.isNull(listaExpedienteDoc) || listaExpedienteDoc.isEmpty()) {
            throw new ServiceException("No se encontraron documentos a procesar");
        }

        for (ExpedienteDoc expDoc : listaExpedienteDoc) {
            CatalogosValores estadoExpDoc = new CatalogosValores();
            estadoExpDoc.setIdValor(estado);
            expDoc.setEstado(estadoExpDoc);
            expDoc.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
            expDoc.setFcModifFila(new Date());
            expDoc.setFcRegistro(fechaFirmaSubsanacion);
            expDoc.setCodigoEreg(cdEregSubsanacionAdmin);
            expDoc = this.expedienteDocDAO.merge(expDoc);
            this.expedienteDocHistDAO.crearHistorico(expDoc,
                    Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
        }

        // VMM 01/10/24 MD-714: si es un tramite de contratos o de JCT no
        // actualice este estado
        if (esSubsanarContrato.equals(Boolean.FALSE) && esSubsanarJustec.equals(Boolean.FALSE)) {
            expediente.setEstadoRevAdmin(
                    catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
        }

        if (esSubsanarContrato.equals(Boolean.TRUE)) {
            expediente.setEstadoRevContrato(
                    catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
        }

        if (esSubsanarJustec.equals(Boolean.TRUE)) {// && expdteJusTecDTO !=
                                                    // null) {
            // VMM esto hay que hacerlo por periodos
            expediente.setEstadoRevJusTec(
                    catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));

//            expdteJusTecDTO.setIdEstadoDTO(new CatalogosValoresDTO(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
//            this.expedienteJusTecService.update(expdteJusTecDTO);
        }

        // VMM 01/10/24 MD-714: comentamos esto para que no actualice el estado
        // del beneficiario como posible solucion
//        if(esSubsanarJustec.equals(Boolean.FALSE)) {
//            Integer idEstadoExpedienteMayor = ExpedienteToEntity.INSTANCE
//            .calcularEstadoMayor(expediente);
//            
//            expediente.setEstado(new CatalogosValores(idEstadoExpedienteMayor));
//            
//            expediente.setEstadoBenef(
//                new CatalogosValores(Constantes.EstadoExpediente.EN_TRAMITACION));
//        }

        expediente.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
        expediente.setFcModifFila(new Date());

        expediente = this.getDao().merge(expediente);
        // Historico
        this.expedienteHistDAO.crearHistorico(expediente,
                Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));

    }

    /**
     * Método para actualizar estados de documentos y expediente al finalizar la
     * subsanación requisitos
     *
     * @param expediente
     * @param idDocumentoPtfr
     * @throws ServiceException
     */
    private void finalizarSubsanacionRequisitosExpediente(Integer idExpediente,
            Integer idDocumentoPtfr, String cdEregSubsana,Date fcFirmaSubsana) throws ServiceException {

        Expediente expediente = this.find(idExpediente);

        // actualizar estados de documentos
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DOCUMENTO_PTFR_IDDOCUMENTOPTFR,
                idDocumentoPtfr, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteReq> listaExpedienteReq = this.expedienteReqDAO.find(filtros, null);

        if (Objects.nonNull(listaExpedienteReq) && !listaExpedienteReq.isEmpty()) {
            for (ExpedienteReq expReq : listaExpedienteReq) {
                if(expReq.getIdExpedienteDoc() != null) {
                    ExpedienteDoc expDocVer = this.expedienteDocDAO.find(expReq.getIdExpedienteDoc());
                    expReq.setTlObservacionesCiudadano(expDocVer.getTlObservaciones());
                    if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION).equals(expDocVer.getEstado().getIdValor())
                          || utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION).equals(expDocVer.getEstado().getIdValor()) ){

                        CatalogosValores estadoExpDoc = catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                        expDocVer.setEstado(estadoExpDoc);
                        expDocVer.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
                        expDocVer.setFcRegistro(fcFirmaSubsana);
                        expDocVer.setCodigoEreg(cdEregSubsana);
                        expDocVer.setFcModifFila(new Date());
                        expDocVer = this.expedienteDocDAO.merge(expDocVer);
                        this.expedienteDocHistDAO.crearHistorico(expDocVer,
                                Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
                        
                        this.expedienteReqDAO.update(expReq);
                        ExpedienteReqHist expedienteReqHist = new ExpedienteReqHist();
                        expedienteReqHist.setCdUsuAlta(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
                        expedienteReqHist.setFcAltaFila(new Date());
                        expedienteReqHist.setExpediente(expediente);
                        expedienteReqHist.setRequisito(expReq.getRequisito());
                        expedienteReqHist.setTlObservacionesCiudadano(expDocVer.getTlObservaciones());
                        this.expedienteReqHistService.guardar(ExpedienteReqHistToDTO.INSTANCE.apply(expedienteReqHist));
                    }

                }
               
            }
            
            // actualizar estado de expediente
            
            expediente.setEstadoRevRequi(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));

            // VMM ver si estos estados habria que comentarlos y no actualizarlos o
            // si...o solo los del tramite de req
            Integer idEstadoExpedienteMayor = ExpedienteToEntity.INSTANCE
                    .calcularEstadoMayor(expediente);

            expediente.setEstado(new CatalogosValores(idEstadoExpedienteMayor));

            expediente.setEstadoBenef(
                    ExpedienteToEntity.INSTANCE.calcularEstadoBenef(idEstadoExpedienteMayor));

            expediente.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
            expediente.setFcModifFila(new Date());

            expediente = this.getDao().merge(expediente);
            // Historico
            this.expedienteHistDAO.crearHistorico(expediente,
                    Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
        }
       

       

    }

    /**
     * Método para actualizar estados de documentos y expediente al finalizar la
     * subsanación requisitos
     *
     * @param expediente
     * @param idDocumentoPtfr
     * @throws ServiceException
     */
    private void finalizarSubsanacionRequisitosGestion(Integer idExpediente,
            List<ExpedienteDocDTO> lstExpedienteDoc, String cdNumIden) throws ServiceException {

        Expediente expediente = this.find(idExpediente);

        for (ExpedienteDocDTO expDocDTO : lstExpedienteDoc) {

            ExpedienteDoc expDocVer = this.expedienteDocDAO.find(expDocDTO.getIdExpedienteDoc());

            if (Constantes.EstadoDocumento.PENDIENTE_PRESENTAR.equals(expDocVer.getEstado().getNmConstante())
                    && expDocVer.getExpediente().getIdExpediente().equals(idExpediente)) {

                CatalogosValores estadoExpDoc = catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
                expDocVer.setEstado(estadoExpDoc);
                expDocVer.setCdUsuModif(Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
                expDocVer.setFcModifFila(new Date());
                expDocVer = this.expedienteDocDAO.merge(expDocVer);
                this.expedienteDocHistDAO.crearHistorico(expDocVer,
                        Utils.valorAuditoria(expediente.getInteresado().getCdNumIden()));
            }

        }

        // actualizar estado de expediente
        expediente.setEstadoRevRequi(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));


        expediente.setCdUsuModif(cdNumIden);
        expediente.setFcModifFila(new Date());

        expediente = this.getDao().merge(expediente);
        // Historico
        this.expedienteHistDAO.crearHistorico(expediente, cdNumIden);

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoExpedienteRevisionDocumento(Integer idExpediente)
            throws ServiceException {

        List<ExpedienteDoc> listaExpedienteDoc = null;

        Expediente expediente = this.find(idExpediente);

        ExpedienteDTO dto = null;

        Integer idEstadoInternoDoc = null;

        if (Constantes.EstadoExpediente.DESISTIDO.equals(expediente.getEstadoRevAdmin().getNmConstante())
                || Constantes.EstadoExpediente.REVISION_OK.equals(expediente.getEstadoRevAdmin().getNmConstante())) {
            idEstadoInternoDoc = expediente.getEstadoRevAdmin().getIdValor();

        } else {

            // Obtenemos la lista de documentos
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    idExpediente, Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            // MD-1979. Quitamos el filtro de FICA, porque los doc de req viene sin FICA
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));
            listaExpedienteDoc = this.expedienteDocDAO.find(filtros, null);
            
            listaExpedienteDoc = listaExpedienteDoc.stream()
                    .filter(x -> (x.getCdFica() != null
                            || (x.getCdFica() == null && utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                                    .equals(x.getTramite().getIdValor()))
                            || (x.getCdFica() == null && utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION)
                                    .equals(x.getEstado().getIdValor()))))
                    .collect(Collectors.toList());

         // MD-1979. Ahora ya no quitamos los documentos que son de requisitos 
            // Quitamos los documentos que son parte de revisión de requisitos
//            List<ExpedienteDoc> lstExpedienteDocFiltrado = new ArrayList<>();
//
//            for (ExpedienteDoc expDoc : listaExpedienteDoc) {
//                filtros = new ArrayList<>();
//                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
//                        idExpediente, Operador.EQUALS));
//                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC,
//                        expDoc.getIdExpedienteDoc(), Operador.EQUALS));
//                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//                /*
//                 * filtros.add(new
//                 * AtlasFilter(Constantes.Filtro.FILTRO_IT_DOC_JUSTEC, "N",
//                 * Operador.EQUALS));
//                 */
//
//                List<ExpedienteReq> listaExpedienteReq = this.expedienteReqDAO.find(filtros, null);
//
//                if (listaExpedienteReq.isEmpty()) {
//                    lstExpedienteDocFiltrado.add(expDoc);
//                }
//
//            }
//
//            idEstadoInternoDoc = ExpedienteToEntity.INSTANCE
//                    .calcularEstadoMayorDocumento(lstExpedienteDocFiltrado);
            
            idEstadoInternoDoc = ExpedienteToEntity.INSTANCE
                    .calcularEstadoMayorDocumento(listaExpedienteDoc);

        }

        if (idEstadoInternoDoc != null) {
            dto = this.actualizarEstadoInterno(expediente, idEstadoInternoDoc, Boolean.TRUE,
                    Boolean.TRUE);
        } else {
            dto = ExpedienteToDTO.INSTANCE.apply(expediente);
        }

        dto = this.calcularTramitesFinalizados(dto);

        return dto;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoExpedienteContrato(Integer idExpediente,
            Integer idEstadoNuevo) throws ServiceException {

        Expediente expediente = this.expedienteDAO.find(idExpediente);
        expediente.setEstado(new CatalogosValores(idEstadoNuevo));

        this.expedienteDAO.merge(expediente);

        this.expedienteHistDAO.crearHistorico(expediente, null);

        return ExpedienteToDTO.INSTANCE.apply(expediente);

    }

    /**
     *
     */
    @Override
    public List<ExpedienteDTO> obtenerExpedienteSubsanacion(String numIdentidad)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_CDNUMIDEN, numIdentidad,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDTO> lstExpedienteDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

        List<ExpedienteDTO> lstExpediente = new ArrayList<>();

        if (!lstExpedienteDTO.isEmpty()) {

            for (ExpedienteDTO exp : lstExpedienteDTO) {
                SolicitudDTO solicitud = this.solicitudService.buscarId(exp.getIdSolicitud());
                if (solicitud != null && solicitud.getConvocatoriaDTO() != null
                        && solicitud.getConvocatoriaDTO().getIdTipoConcesion() != null
                        && solicitud.getConvocatoriaDTO().getIdTipoConcesion().getNmConstante()
                                .equals(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {
                    // Buscamos hijos pendientes de subsanar
                    List<Expediente> listHijosPendSubsanarReq = this.getDao()
                            .listExpedientesHijoPorExpedientePadre(exp.getIdExpediente());

                    // Usar un iterador para evitar problemas de modificación
                    // concurrente
                    Iterator<Expediente> iterator = listHijosPendSubsanarReq.iterator();
                    while (iterator.hasNext()) {
                        Expediente expHijo = iterator.next();

                        List<Integer> tramitesPtfr = new ArrayList<>();
                        tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
                        // MD-2190 No añadimos en esta lista el trámite de alegación exclusión provisional
//                        tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL));

                        // Obtener los documentos asociados al expediente
                        List<DocumentoPtfrDTO> docPtfr = documentoPtfrDAO
                                .obtenerPorExpedienteYTramites(expHijo.getIdExpediente(),
                                        tramitesPtfr);

                        // Si no hay documentos asociados, eliminar el
                        // expediente de la lista
                        if (docPtfr == null || docPtfr.isEmpty()) {
                            iterator.remove(); // Elimina el elemento actual de
                                               // la lista de manera segura
                        }
                    }
                    if (listHijosPendSubsanarReq != null && !listHijosPendSubsanarReq.isEmpty()) {
                        lstExpediente.addAll(Lists.transform(listHijosPendSubsanarReq,
                                ExpedienteToDTO.INSTANCE));
                    }

                }
            }


            List<ExpedienteDTO> lstExpedienteSubsanar = lstExpedienteDTO.stream()
                    .filter(x -> Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS.equals(x.getEstadoBeneficiarioDTO().getNmConstante())
                            && (Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS.equals(x.getEstadoRevAdminDTO().getNmConstante())
                                    || Constantes.EstadoExpedienteBeneficiario.PENDIENTE_SUBSANACION_REQUISITOS.equals(x.getEstadoRevRequiDTO().getNmConstante())))
                    .collect(Collectors.toList());

            if (lstExpedienteSubsanar != null && !lstExpedienteSubsanar.isEmpty()) {
                lstExpediente.addAll(lstExpedienteSubsanar);
            }

        }

        return lstExpediente;
    }

    /**
     *
     * @param expedienteDTO
     * @throws ServiceException
     */
    private void validarCrearExpediente(ExpedienteDTO expedienteDTO) throws ServiceException {
        if (Objects.isNull(expedienteDTO)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (Objects.isNull(expedienteDTO.getImpresoDTO())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (Objects.isNull(expedienteDTO.getConvocatoriaActDTO())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (Objects.isNull(expedienteDTO.getConvocatoriaActDTO().getIdActor())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (Objects.isNull(expedienteDTO.getIdSolicitud())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (Objects.isNull(expedienteDTO.getInteresadoDTO())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // El interesado es nuevo y no tiene DNI.
        if (Objects.nonNull(expedienteDTO.getInteresadoDTO().getIdInteresado())
                && StringUtils.isBlank(expedienteDTO.getInteresadoDTO().getCdNumIden())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
    }

    /**
     *
     */
    @Override
    public ExpedienteDTO crear(ExpedienteDTO expedienteDTO) throws ServiceException {
        InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
        // Validaciones entrada
        this.validarCrearExpediente(expedienteDTO);

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // (CD_USU_ALTA) debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(expedienteDTO.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

        Integer idActor = expedienteDTO.getConvocatoriaActDTO().getIdActor();
        Integer idSolicitud = expedienteDTO.getIdSolicitud();
        Integer idInteresado = expedienteDTO.getInteresadoDTO().getIdInteresado();

        // Obtenemos la convocatoriaAct del participante
        ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(idActor);

        // Obtenemos el número máximo de expediente de ese Actor permitidos
        Integer nmMax = convocatoriaActDTO.getNmMax();

        if (nmMax == null) {
            throw new ServiceException(Constantes.Error.ERROR_ACTOR_SIN_CONVOCATORIA);
        }

        // Obtenemos el total de expediente de ese tipo en esa solicitud
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_EXP_ID_ACTOR, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        // Sumamos 1 para saber si el nuevo podría añadirse
        Integer total = this.count(filtros) + 1;

        /*if (total > nmMax) {
            throw new ServiceException(Constantes.Error.ERROR_MAXIMO_PARTICIPANTES_CONVOCATORIA);
        }*/

        // El interesado es nuevo
        if (idInteresado == null) {
            // Obtenemos el tipo de actor
            CatalogosValoresDTO tipoActorDTO = this.catalogosValoresService
                    .buscarId(convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor());

            if (Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_SOLICITANTE.equals(tipoActorDTO.getCatalogoDTO().getNmConstante())
                    || Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_COORDINADOR.equals(tipoActorDTO.getCatalogoDTO().getNmConstante())
                    || Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_PARTICIPANTE.equals(tipoActorDTO.getCatalogoDTO().getNmConstante())) {
                expedienteDTO.getInteresadoDTO()
                        .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.FISICA));
            } else {
                expedienteDTO.getInteresadoDTO()
                        .setTipoActorDTO(catalogosValoresService.buscarNmConstante(Constantes.Identificacion.JURIDICA));
            }

            // Auditoria
            if (Boolean.TRUE.equals(expedienteDTO.getIsRepresentanteLegal())
                    && expedienteDTO.getRepresentanteLegal() != null

                    && expedienteDTO.getRepresentanteLegal()
                            .getCdNumIden() != null) {
                expedienteDTO.getInteresadoDTO().setCdUsuAlta(
                        Utils.valorAuditoria(expedienteDTO.getRepresentanteLegal().getCdNumIden()));
            } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                expedienteDTO.getInteresadoDTO()
                        .setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
            } else {
                // Salvaguarda (defensa en profundidad): ver comentario en el cálculo de
                // esInteresadoExtraComunitario más arriba.
                expedienteDTO.getInteresadoDTO()
                        .setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
            }

            expedienteDTO.getInteresadoDTO().setFcAltaFila(new Date());

            // Insertamos el interesado.
            Interesado interesado = this.interesadoDAO
                    .insert(InteresadoToEntity.INSTANCE.apply(expedienteDTO.getInteresadoDTO()));
            expedienteDTO.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(interesado));

        } else {
            expedienteDTO.setInteresadoDTO(this.interesadoService.buscarId(idInteresado));
        }

        // Auditoria
        if (representanteLegal != null
                && representanteLegal.getCdNumIden() != null) {
            expedienteDTO.setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));

        } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
            expedienteDTO.setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
        } else {
            // Salvaguarda (defensa en profundidad): ver comentario anterior.
            expedienteDTO.setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
        }

        expedienteDTO.setFcAltaFila(new Date());

        // Obtenemos la solicitud
        SolicitudDTO solicitudDTO;
        try {
            solicitudDTO = SolicitudToDTO.INSTANCE.apply(this.solicitudDAO.find(idSolicitud));
            solicitudDTO.setInteresadoRepresentado(representanteLegal);
            // Comprobamos si genera código de expediente
            Boolean generaCdExpediente = convocatoriaActDTO.getBolGeneraExpediente();

            if (expedienteDTO.getConvocatoriaAreaDTO() != null
                    && expedienteDTO.getConvocatoriaAreaDTO().getIdConvocatoriaArea() != null) {
                solicitudDTO.setConvocatoriaAreaDTO(expedienteDTO.getConvocatoriaAreaDTO());
            }
            if (expedienteDTO.getConvocatoriaAreaSubareaDTO() != null && expedienteDTO
                    .getConvocatoriaAreaSubareaDTO().getIdConvocatoriaAreaSubarea() != null) {
                solicitudDTO.setConvocatoriaAreaSubareaDTO(
                        expedienteDTO.getConvocatoriaAreaSubareaDTO());
            }
            // Creamos el expediente
            expedienteDTO = this.generar(solicitudDTO, expedienteDTO.getInteresadoDTO(),
                    convocatoriaActDTO, generaCdExpediente, expedienteDTO.getIdExpedientePadre());

            // Historico

            if (representanteLegal != null
                    && representanteLegal.getCdNumIden() != null) {
                this.expedienteHistDAO.crearHistorico(
                        ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                        Utils.valorAuditoria(representanteLegal.getCdNumIden()));
            } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                this.expedienteHistDAO.crearHistorico(
                        ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                        Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
            } else {
                // Salvaguarda (defensa en profundidad): ver comentario anterior.
                this.expedienteHistDAO.crearHistorico(
                        ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                        Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
            }

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return expedienteDTO;
    }

    /**
     *
     */
    @Override
    public ResponseDTO comprobarParticipantes(Integer idExpediente) throws ServiceException {

        ResponseDTO responseDTO = new ResponseDTO();

        // Obtenemos el expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);

        // Obtenemos la solicitud
        SolicitudDTO solicitudDTO = SolicitudToDTO.INSTANCE
                .apply(this.solicitudDAO.find(expedienteDTO.getIdSolicitud()));

        Integer idSolicitud = solicitudDTO.getIdSolicitud();

        // Obtenemos la lista de participantes de la solicitud
        List<ExpedienteDTO> lstExpedienteParticipantesDTO = this
                .buscarParticipantesPorSolicitud(expedienteDTO.getIdSolicitud(), null);

        // Comprobamos si hay algún participante obligatorio en la convocatoria
        // para el solicitante
        List<ConvocatoriaActDTO> listConvocatoriaActDTO = this.convocatoriaActService
                .buscarTipoActorParticipante(solicitudDTO.getConvocatoriaDTO().getIdConvocatoria());

        // Contamos cuantos son obligatorios. Contamos cuantos tienen el nmMin
        // diferente a 0
        Long participantesObligatorios = listConvocatoriaActDTO.stream()
                .filter(x -> x.getNmMin() != 0)
                .filter(x -> Objects.nonNull(x.getIdActorSolicitante()) && x.getIdActorSolicitante()
                        .equals(expedienteDTO.getConvocatoriaActDTO().getIdActor()))
                .count();

        // Si no hay añadidos pero alguno es obligatorio, lanzamos el error
        if (Boolean.TRUE.equals(lstExpedienteParticipantesDTO.isEmpty())
                && participantesObligatorios > 0) {
            responseDTO.setCodigo("101");
            responseDTO
                    .setDescripcionError(Constantes.Error.ERROR_MINIMO_PARTICIPANTES_CONVOCATORIA);
            responseDTO.setError(Boolean.TRUE);
            return responseDTO;
        }

        for (ExpedienteDTO expDTO : lstExpedienteParticipantesDTO) {

            Integer idActor = expDTO.getConvocatoriaActDTO().getIdActor();

            // Obtenemos la convocatoriaAct del participante
            ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(idActor);

            // Obtenemos el número máximo y mínimo de participantes permitidos
            Integer nmMax = convocatoriaActDTO.getNmMax();
            Integer nmMin = convocatoriaActDTO.getNmMin();

            // Obtenemos el total de participantes de ese tipo en esa solicitud
            List<AtlasFilter> filtros = new ArrayList<>();

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            // No se suma 1 como en el guardar ya que no tenemos que prever un
            // nuevo participante
            Integer totalParticipantes = this.count(filtros);

            if (totalParticipantes > nmMax) {
                responseDTO.setCodigo("102");
                responseDTO.setDescripcionError(
                        Constantes.Error.ERROR_MAXIMO_PARTICIPANTES_CONVOCATORIA);
                responseDTO.setError(Boolean.TRUE);
                return responseDTO;
            }

            if (totalParticipantes < nmMin) {
                responseDTO.setCodigo("103");
                responseDTO.setDescripcionError(
                        Constantes.Error.ERROR_MINIMO_PARTICIPANTES_CONVOCATORIA);
                responseDTO.setError(Boolean.TRUE);
                return responseDTO;
            }
        }

        return responseDTO;
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocDTO> obtenerDocumentos(Integer idExpediente) throws ServiceException {
        List<ExpedienteDocDTO> listExpedienteDocDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        try {
            listExpedienteDocDTO = new ArrayList<>(Lists.transform(
                    this.expedienteDocDAO.find(filtros, null), ExpedienteDocToDTO.INSTANCE));
            listExpedienteDocDTO = this.rellenarDocumentosExpediente(listExpedienteDocDTO);
        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return listExpedienteDocDTO;
    }

    /**
     *
     */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkRequisito(Integer idExpediente)
            throws ServiceException {

        Expediente exp = this.getDao().find(idExpediente);
        ExpedienteDTO dto = null;

        if (exp.getLstExpedienteReq().stream()
                .filter(x -> x.getItCumple() != null
                        && x.getItCumple().equals(Constantes.AfirmacionNegacion.N)
                        && x.getFcBajaFila() == null)
                .count() == 0) {
            dto = this
                    .actualizarEstadoInterno(exp,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK),
                            Boolean.FALSE, Boolean.TRUE);
        } else {
            dto = ExpedienteToDTO.INSTANCE.apply(exp);
        }

        // Calcular tramites finalizados
        this.calcularTramitesFinalizados(dto);

        return dto;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoRequisito(Integer idExpediente)
            throws ServiceException {

        Expediente exp = this.getDao().find(idExpediente);
        ExpedienteDTO dto = null;


        // Si es uno de estos estados no hacemos nada
        if (Constantes.EstadoExpediente.INICIADA.equals(exp.getEstadoRevRequi().getNmConstante())
                || Constantes.EstadoExpediente.REVISION_OK.equals(exp.getEstadoRevRequi().getNmConstante())
                || Constantes.EstadoExpediente.EXCLUIDO.equals(exp.getEstadoRevRequi().getNmConstante())) {

            dto = ExpedienteToDTO.INSTANCE.apply(exp);

            return dto;
        }

        if (Constantes.EstadoExpediente.EN_TRAMITACION.equals(exp.getEstadoRevRequi().getNmConstante())
                || Constantes.EstadoExpediente.PENDIENTE_REVISION.equals(exp.getEstadoRevRequi().getNmConstante())
                || Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO.equals(exp.getEstadoRevRequi().getNmConstante())
                || Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO_FIRMA_RECHAZADA.equals(exp.getEstadoRevRequi().getNmConstante())) {

            // Comprobamos si existen requisitos sin cumplir
            Boolean existenReqSinCumplir = exp.getLstExpedienteReq().stream()
                    .filter(x -> Constantes.AfirmacionNegacion.N.equals(x.getItCumple()))
                    .count() > 0;

            if (Boolean.TRUE.equals(existenReqSinCumplir)) {
                // Si existen ponenmos el estado interno a pendiente
                // requerimiento a espera de enviar a portafirmas.
                dto = this.actualizarEstadoInterno(exp,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO),
                        Boolean.FALSE, Boolean.TRUE);
            } else {
                dto = this.actualizarEstadoInterno(exp,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION),
                        Boolean.FALSE, Boolean.TRUE);
            }
        } else {
            dto = this.actualizarEstadoInterno(exp, exp.getEstadoRevRequi().getIdValor(),
                    Boolean.FALSE, Boolean.TRUE);
        }

        this.calcularTramitesFinalizados(dto);

        return dto;
    }

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    private ExpedienteDTO actualizarEstadoInterno(Expediente expediente, Integer idEstadoNuevo,
            Boolean esRevisionAdmin, Boolean actualizaEstadoExpediente) throws ServiceException {

        Boolean esCambioEstado = Boolean.FALSE;

        if (Boolean.TRUE.equals(esRevisionAdmin)
                && !expediente.getEstadoRevAdmin().getIdValor().equals(idEstadoNuevo)) {
            esCambioEstado = Boolean.TRUE;
        }

        if (Boolean.FALSE.equals(esRevisionAdmin)
                && !expediente.getEstadoRevRequi().getIdValor().equals(idEstadoNuevo)) {
            esCambioEstado = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(esCambioEstado)) {
            CatalogosValoresDTO estadoRevInternoDTO = this.catalogosValoresService
                    .buscarId(idEstadoNuevo);

            if (Boolean.TRUE.equals(esRevisionAdmin)) {
                expediente.setEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO));
            } else {
                expediente.setEstadoRevRequi(
                        CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO));
            }

            if (actualizaEstadoExpediente.equals(Boolean.TRUE)) {
                Integer idEstado = ExpedienteToEntity.INSTANCE.calcularEstadoMayor(expediente);
                if (idEstado != 0) {
                    CatalogosValoresDTO estadoExpedienteDTO = this.catalogosValoresService
                            .buscarId(idEstado);
                    expediente.setEstado(
                            CatalogosValoresToEntity.INSTANCE.apply(estadoExpedienteDTO));
                    
                    expediente.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(idEstado));
                }
            }

            expediente = this.getDao().merge(expediente);

            this.expedienteHistDAO.crearHistorico(expediente, null);
        }

        return ExpedienteToDTO.INSTANCE.apply(expediente);

    }

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoJCTRevision(Expediente expediente,
            ExpedienteJusTecDTO expedienteJustecDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException {

        ExpedienteJusTec expedienteJustec = null;

        if (expedienteJustecDTO != null) {

            CatalogosValoresDTO estado = this.catalogosValoresService.buscarId(idEstadoNuevo);

            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);

            expedienteJustecDTO.setIdEstadoDTO(estado);

            expedienteJustecDTO.setIdEstadoRevAdminDTO(estadoRev);

            expedienteJustec = ExpedienteJusTecToEntity.INSTANCE.apply(expedienteJustecDTO);

            expedienteJustec = this.expedienteJusTecDAO.merge(expedienteJustec);

            expediente = this.getDao().merge(expediente);

            this.expedienteHistDAO.crearHistorico(expediente, null);

            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } else {
            return null;
        }

    }

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoJCT(Expediente expediente,
            ExpedienteJusTecDTO expedienteJustecDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException {

        ExpedienteJusTec expedienteJustec = null;

        if (expedienteJustecDTO != null) {

            CatalogosValoresDTO estado = this.catalogosValoresService.buscarId(idEstadoNuevo);

            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);

            expedienteJustecDTO.setIdEstadoDTO(estado);

            expedienteJustecDTO.setIdEstadoRevAdminDTO(estadoRev);

            expedienteJustec = ExpedienteJusTecToEntity.INSTANCE.apply(expedienteJustecDTO);

            expedienteJustec = this.expedienteJusTecDAO.merge(expedienteJustec);

            expediente = this.getDao().merge(expediente);

            this.expedienteHistDAO.crearHistorico(expediente, null);

            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } else {
            return null;
        }
    }
//
//    @Override
//    @Transactional(readOnly = false)
//    public ExpedienteDTO actualizarEstadoInternoActasRevision(Integer idExpediente,
//            ExpedienteModifPresPlazoOtraDTO expedienteModifDTO, Integer idEstadoNuevoRev)
//            throws ServiceException {
//
//        ExpedienteModifPresPlazoOtra expedienteModif = null;
//
//        if (expedienteModifDTO != null) {
//
//
//            Expediente exp = this.getDao().find(idExpediente);
//
//            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);
//
//            expedienteModifDTO.setEstadoRevAdminDTO(estadoRev);
//
//            expedienteModif = ExpedienteModifPresPlazoOtraToEntity.INSTANCE
//                    .apply(expedienteModifDTO);
//            expedienteModif = this.expedienteModifPresPlazoOtraDAO.merge(expedienteModif);
//
//            exp = this.getDao().merge(exp);
//
//            this.expedienteHistDAO.crearHistorico(exp, null);
//
//            return ExpedienteToDTO.INSTANCE.apply(exp);
//        } else {
//            return null;
//        }
//    }

//    @Override
//    @Transactional(readOnly = false)
//    public ExpedienteDTO actualizarEstadoInternoActasRevision(Integer idExpediente,
//            ExpedienteModifPresPlazoOtraDTO expedienteModifDTO, Integer idEstadoNuevoRev)
//            throws ServiceException {
//
//        if (expedienteModifDTO != null) {
//            Expediente exp = this.getDao().find(idExpediente);
//
//            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);
//            expedienteModifDTO.setEstadoRevAdminDTO(estadoRev);
//
//            ExpedienteModifPresPlazoOtra expedienteModif = ExpedienteModifPresPlazoOtraToEntity.INSTANCE.apply(expedienteModifDTO);
//           
//             expedienteModif.setExpediente(exp); 
//    
//             CatalogosValores estadoEntity = this.catalogosValoresDAO.find(idEstadoNuevoRev);
//             expedienteModif.setEstado(estadoEntity); 
//    
//             expedienteModif = this.expedienteModifPresPlazoOtraDAO.merge(expedienteModif);
//           
//            // 7. Actualizamos el expediente padre si es necesario
//            exp = this.getDao().merge(exp);
//
//            this.expedienteHistDAO.crearHistorico(exp, null);
//
//            return ExpedienteToDTO.INSTANCE.apply(exp);
//        } else {
//            return null;
//        }
//    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoActasRevision(Integer idExpediente,
            ExpedienteModifPresPlazoOtraDTO expedienteModifDTO, Integer idEstadoNuevoRev)
            throws ServiceException {

        if (expedienteModifDTO != null) {
            // 1. Recuperamos el padre
            Expediente exp = this.getDao().find(idExpediente);

            // 2. Convertimos DTO a Entidad
            ExpedienteModifPresPlazoOtra expedienteModif = ExpedienteModifPresPlazoOtraToEntity.INSTANCE.apply(expedienteModifDTO);

            // --- ASIGNACIONES OBLIGATORIAS (Evitan ORA-01400) ---
            
            // A. Los vínculos que ya arreglamos
            expedienteModif.setExpediente(exp);
            
            CatalogosValores estadoEntity = this.catalogosValoresDAO.find(idEstadoNuevoRev);
            //expedienteModif.setEstado(estadoEntity);
            expedienteModif.setEstadoRevAdmin(estadoEntity); // Aprovechamos para el estado de revisión

            // B. Arreglo para ID_MODIFICACION (El error actual)
            if (expedienteModif.getIdModificacion() == null) {
                // Si el DTO no lo trae, hay que decidir qué valor poner. 
                // ¿Viene del DTO? expedienteModifDTO.getIdModificacion()
                expedienteModif.setIdModificacion(expedienteModifDTO.getIdModificacion() != null ? expedienteModifDTO.getIdModificacion() : 0);
            }

            // C. Arreglo para ID_IMPRESO (Evita el próximo error)
            if (expedienteModif.getIdImpreso() == null) {
                expedienteModif.setIdImpreso(expedienteModifDTO.getIdImpreso() != null ? expedienteModifDTO.getIdImpreso() : 0);
            }

            // D. Arreglo para los indicadores IT_MOD_... (Obligatorios en DB)
            if (expedienteModif.getItModPres() == null) expedienteModif.setItModPres("N");
            if (expedienteModif.getItModPlazo() == null) expedienteModif.setItModPlazo("N");
            if (expedienteModif.getItModOtra() == null) expedienteModif.setItModOtra("N");

            // E. Fecha de Solicitud (Obligatoria en DB)
            if (expedienteModif.getFcSolicitud() == null) {
                expedienteModif.setFcSolicitud(new Date()); // O la fecha que traiga el DTO
            }

            // --- FIN ASIGNACIONES ---

            // 3. Guardar
            expedienteModif = this.expedienteModifPresPlazoOtraDAO.merge(expedienteModif);

            // 4. Actualizar padre e histórico
            exp = this.getDao().merge(exp);
            this.expedienteHistDAO.crearHistorico(exp, null);

            return ExpedienteToDTO.INSTANCE.apply(exp);
        }
        return null;
    }
    
    
    /**
     *
     */
    @Override
    public List<ExpedienteDTO> obtenerExpedientesPorSolicitud(Integer idSolicitud)
            throws ServiceException {

        List<ExpedienteDTO> listExpedienteDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO), Operador.NOT_EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        try {
            listExpedienteDTO = new ArrayList<>(
                    Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

            for (ExpedienteDTO dto : listExpedienteDTO) {
                // Rellenamos los datos del expediente
                // Los necesitamos para el impreso del zip (si no está
                // presentado en EREG)
                dto = this.rellenarDatosExpediente(dto);

                // Rellenamos los documentos del expediente
                // Los necesitamos para los zip
                dto.setLstExpedienteDocDTO(
                        this.rellenarDocumentosExpediente(dto.getLstExpedienteDocDTO()));
            }

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return listExpedienteDTO;
    }

    /**
     *
     */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkDocumento(Integer idExpediente,
            ExpedienteDTO dtoAux) throws ServiceException {

        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);

        List<ExpedienteDocDTO> lstExpDocDTO = busquedaDocumentosSolicitud(idExpediente, null, 10,
                0);

//        List<ExpedienteDocDTO> lstExpDocDTO = dtoAux.getLstExpedienteDocDTO().stream()
//                .filter(x -> x.getFcBajaFila() == null).collect(Collectors.toList());

        // Obtenemos la lista de documentos asociados a los requerimientos
        List<Integer> lstDocReq = exp.getLstExpedienteReq().stream()
                .map(ExpedienteReq::getIdExpedienteDoc).filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Quitamos los registros documentos asociado a los requerimientos
        lstExpDocDTO = lstExpDocDTO.stream()
                .filter(x -> !lstDocReq.contains(x.getIdExpedienteDoc()))
                .collect(Collectors.toList());

        // Quitamos los documentos que no sean de tipo tramite presentacion de
        // solicitud 400

//        lstExpDocDTO = dtoAux.getLstExpedienteDocDTO().stream()
//                .filter(x -> x.getTramiteDTO().getIdValor().equals(Constantes.Tramite.PRESENTACION)).collect(Collectors.toList());

        // GIM 18/09/2024 comentamos la version antigua que solo miraba que
        // fuesen estado resivionOK

        // Consultamos si hay algun documento diferente de Revision OK
//        Boolean existeNoRevisionOk = lstExpDoc.stream().anyMatch(
//                x -> !Constantes.EstadoDocumento.REVISION_OK.equals(x.getEstado().getIdValor()));

        // hay que filtrar los documentos por expediente sii es una convocatoria
        // tipo concesion
        ExpedienteDTO expediente = this.buscarId(idExpediente);
        Convocatoria convocatoria = this.convocatoriaDAO
                .find(expediente.getConvocatoriaActDTO().getIdConvocatoria());

        if (Objects.equals(convocatoria.getIdTipoConcesion().getNmConstante(),
                Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {
            lstExpDocDTO = this.filtrarDocumentosByExpediente(idExpediente, lstExpDocDTO);
        }

        // GIM 18/09/2024 Metemos el mismo filtro que estab en el metodod e
        // habilitar el boton de finalizar revision para ignorar los documentos
        // que esten sin subir y no sean obligatorios.
        
        Integer idRevOk = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK);
        Integer idSinSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR);
        Boolean pendienteRevisadoOk = lstExpDocDTO.stream().allMatch(
                f -> (idRevOk.equals(f.getEstadoDTO().getIdValor()) ||
                      (idSinSub.equals(f.getEstadoDTO().getIdValor()) && 
                       ( (f.getBloqueComponenteDTO() != null && !f.getBloqueComponenteDTO().getItObligatorio()) || 
                         (f.getBloqueComponenteDTO() != null && f.getBloqueComponenteDTO().getItObligatorio() && f.getItAutoriza().equals("S") ) || 
                         (f.getBloqueComponenteDTO() == null)
                       )
                      )
                    )
            );

        // Si todos estan en Revisión OK (ignorando los sin subir que no sean
        // boligatorios) actualizamos el estado del expediente
        if (Boolean.TRUE.equals(pendienteRevisadoOk)) {
            dto = this
                    .actualizarEstadoInterno(exp,utilConstantes.getGlobalCatalogosValores().get(
                                    Constantes.EstadoExpediente.REVISION_OK),
                            Boolean.TRUE, Boolean.TRUE);

            // calcular tramites finalizados
            this.calcularTramitesFinalizados(dto);
        }

        return dto;
    }

    private List<ExpedienteDocDTO> filtrarDocumentosByExpediente(Integer idExpediente,
            List<ExpedienteDocDTO> listadoDatos) {

        List<ExpedienteDocDTO> listaAuxiliar = new ArrayList<>();

        for (ExpedienteDocDTO documento : listadoDatos) {
            if (documento.getExpedienteDTO().getIdExpediente().equals(idExpediente)) {
                listaAuxiliar.add(documento);
            }
        }

        return listaAuxiliar;
    }

 
    public List<ExpedienteDocDTO> busquedaDocumentosSolicitud(Integer idExpediente,
            List<AtlasSort> orden, Integer pageSize, Integer index) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<AtlasFilter>();

        Expediente expediente = this.find(idExpediente);
        List<ExpedienteDTO> lstExpedienteDTO = this.obtenerExpedienteEstructurado(
                expediente.getSolicitud().getIdSolicitud(), Constantes.AfirmacionNegacion.S);
        List<Integer> lstIdExpediente = lstExpedienteDTO.stream().map(e -> e.getIdExpediente())
                .collect(Collectors.toList());

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                lstIdExpediente, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (orden == null) {
            orden = new ArrayList<>();
        }

        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CDANEXOEREG, true));
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC, true));

        List<ExpedienteDocDTO> listadoDatos = this.expedienteDocService.buscar(index, pageSize,
                filtros, orden);

        listadoDatos = this.recuperarExpediente(listadoDatos, lstExpedienteDTO);

        return listadoDatos;
    }

    @Override
    public List<ExpedienteDocDTO> recuperarExpediente(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            List<ExpedienteDTO> lstExpedienteDTO) {
        ArrayList<ExpedienteDocDTO> lista = new ArrayList<>();
        for (ExpedienteDocDTO expedienteDocuDTO : lstExpedienteDocDTO) {
            for (ExpedienteDTO expedienteDTO : lstExpedienteDTO) {
                if (expedienteDocuDTO.getExpedienteDTO().getIdExpediente()
                        .equals(expedienteDTO.getIdExpediente())) {
                    expedienteDocuDTO.setExpedienteDTO(expedienteDTO);

                    // si el iden que vas a meter ya esixte en la lista no lo
                    // metas, por que se esta duplicando
                    if (!existeYa(expedienteDocuDTO, lista)) {
                        lista.add(expedienteDocuDTO);
                    }
                }
            }
        }
        return lista;
    }

    private boolean existeYa(ExpedienteDocDTO cliente, ArrayList<ExpedienteDocDTO> lista) {
        boolean existe = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdExpedienteDoc().equals(cliente.getIdExpedienteDoc())) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /**
    *
    */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoJCT(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocs, ExpedienteJusTecDTO expedienteJusTecSeleccionado)
            throws ServiceException {

        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);

        if (listaDocs != null) {

//           ExpedienteDTO expediente = this.buscarId(idExpediente);
//           Convocatoria convocatoria = this.convocatoriaDAO.find(expediente.getConvocatoriaActDTO().getIdConvocatoria());
//
//           if( Objects.equals(convocatoria.getIdTipoConcesion().getIdValor(), Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {      
//               listaDocs = this.filtrarDocumentosByExpediente(idExpediente,listaDocs );           
//           }

            // Consultamos si hay algun documento diferente de Revision OK
            Boolean pendienteRevisadoOk = listaDocs.stream()
                    .allMatch(f -> (
                            Constantes.EstadoDocumento.REVISION_OK.equals(f.getEstadoDTO().getNmConstante())
                            || (Constantes.EstadoDocumento.SIN_SUBIR.equals(f.getEstadoDTO().getNmConstante()))
                                    && ((f.getBloqueComponenteDTO() != null && Boolean.FALSE
                                            .equals(f.getBloqueComponenteDTO().getItObligatorio()))
                                            || f.getBloqueComponenteDTO() == null)));

            // Si todos estan en Revisión OK actualizamos el estado del
            // expediente
            if (Boolean.TRUE.equals(pendienteRevisadoOk)) {
                dto = this.actualizarEstadoInternoJCTRevision(exp, expedienteJusTecSeleccionado,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.REVISION_OK));

                // calcular tramites finalizados
                // this.calcularTramitesFinalizados(dto);

            }
        }

        return dto;
    }

    /**
    *
    */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkContratos(Integer idExpediente,
            String estadoRevision) throws ServiceException {
        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);
        List<ExpedienteContratoDTO> expContList = expedienteContratoService.buscarPorExpediente(idExpediente);

        // Quitamos los registros de baja y sin tramite
        List<ExpedienteDoc> lstExpDoc = exp.getLstExpedienteDoc().stream()
                .filter(x -> x.getFcBajaFila() == null).filter(x -> x.getTramite() != null)
                .collect(Collectors.toList());

        // Obtenemos la lista de documentos asociados a los requerimientos
        List<ExpedienteDoc> lstExpContratos = lstExpDoc.stream()
                .filter(x -> (x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL))
                        && (!x.getEstado().getNmConstante().equals(Constantes.EstadoDocumento.SIN_SUBIR)
                                || (x.getBloqueComponente() != null
                                        && x.getBloqueComponente().getItObligatorio() != null
                                        && x.getBloqueComponente().getItObligatorio()
                                                .equals(Constantes.AfirmacionNegacion.S))))
                .collect(Collectors.toList());
        
        // Consultamos si hay algun documento diferente de Revision OK
        Boolean existeNoRevisionOk = lstExpContratos.stream().anyMatch(
                x -> !Constantes.EstadoDocumento.REVISION_OK.equals(x.getEstado().getNmConstante()));

        // Si todos estan en Revisión OK actualizamos el estado del expediente
        if (Boolean.FALSE.equals(existeNoRevisionOk) && estadoRevision.contentEquals("OK")) {
            dto = this.actualizarEstadoInternoContrato(exp, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK),
                    Boolean.TRUE);

            // calcular tramites finalizados
            this.calcularTramitesFinalizados(dto);
        } else {
            // Consultamos si todos estan OK o pend subsanar

            Boolean existePendSubsanar = lstExpContratos.stream()
                    .allMatch(x -> (Constantes.EstadoDocumento.REVISION_OK.equals(x.getEstado().getNmConstante()))
                            || (Constantes.EstadoDocumento.REQUIERE_SUBSANACION.equals(x.getEstado().getNmConstante())));
            if (Boolean.TRUE.equals(existePendSubsanar)
                    || estadoRevision.contentEquals("Subsanar")) {
                dto = this.actualizarEstadoInternoContrato(exp,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS),
                        Boolean.TRUE);
                // calcular tramites finalizados
                this.calcularTramitesFinalizados(dto);
            } else {
                // Consultamos si hay algun KO
                Boolean existeKO = lstExpContratos.stream()
                        .anyMatch(x -> (Constantes.EstadoDocumento.REVISION_KO.equals(x.getEstado().getNmConstante())));
                if (Boolean.TRUE.equals(existeKO) || estadoRevision.contentEquals("KO")) {
                    dto = this.actualizarEstadoInternoContrato(exp, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO),
                            Boolean.TRUE);
                    // calcular tramites finalizados
                    this.calcularTramitesFinalizados(dto);
                }
            }
        }

        return dto;
    }

    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionExpContratosOK(Integer idExpediente)
            throws ServiceException {
        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);
        dto = this.actualizarEstadoInternoContrato(exp, utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK), Boolean.TRUE);

        // calcular tramites finalizados
        this.calcularTramitesFinalizados(dto);
        return dto;
    }

    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoActas(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocs,
            ExpedienteModifPresPlazoOtraDTO expedienteModifSeleccionado) throws ServiceException {

        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);

        if (listaDocs != null) {
            // Consultamos si hay algun documento diferente de Revision OK

            Boolean pendienteRevisadoOk = listaDocs.stream()
                    .allMatch(f -> ((Constantes.EstadoDocumento.REVISION_OK.equals(f.getEstadoDTO().getNmConstante())
                            || Constantes.EstadoDocumento.REVISION_KO.equals(f.getEstadoDTO().getNmConstante()))
                            || (Constantes.EstadoDocumento.SIN_SUBIR.equals(f.getEstadoDTO().getNmConstante()))
                                    && ((f.getBloqueComponenteDTO() != null && Boolean.FALSE
                                            .equals(f.getBloqueComponenteDTO().getItObligatorio()))
                                            || f.getBloqueComponenteDTO() == null)));

            // Si todos estan en Revisión OK o KO actualizamos el estado del
            // expediente
            if (Boolean.TRUE.equals(pendienteRevisadoOk)) {
                Boolean existeKO = listaDocs.stream()
                        .anyMatch(f -> Constantes.EstadoDocumento.REVISION_KO.equals(f.getEstadoDTO().getNmConstante()));
                if (Boolean.FALSE.equals(existeKO)) {
                    dto = this.actualizarEstadoInternoActasRevision(idExpediente,
                            expedienteModifSeleccionado,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK));
                } else {
                    dto = this.actualizarEstadoInternoActasRevision(idExpediente,
                            expedienteModifSeleccionado,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_KO));
                }

            }
        }

        return dto;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public String obtenerFechaLimiteSubsanacionAdmin(Integer idExpediente, String cdNumIden)
            throws ServiceException {

        StringBuilder fechaLimite = new StringBuilder();

        // Obtenemos los datos del expediente
        ExpedienteDTO expedienteDTO = this.buscarId(idExpediente);

        if (Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS.equals(expedienteDTO.getEstadoRevAdminDTO().getNmConstante())) {

            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));

            // Obtenemos un registro de los asociados a la petición de
            // subsanación
            Optional<ExpedienteDocDTO> opExpedienteDoc = expedienteDTO.getLstExpedienteDocDTO()
                    .stream().filter(f -> f.getIdDocumentoPtfr() != null
                            && lstEstados.contains(f.getEstadoDTO().getIdValor()))
                    .findFirst();

            // Inicializamos el formato de fecha
            fechaLimite.append("dd/MM/yyyy");
            if (opExpedienteDoc.isPresent()) {

                // Obtenemos el registro de la petición a portafirmas
                DocumentoPtfr peticionPortafirmas = this.documentoPtfrDAO
                        .find(opExpedienteDoc.get().getIdDocumentoPtfr());
                // Si no existe dato de plazo de subsanación, lo calculamos
               /* if (Objects.isNull(peticionPortafirmas.getFcFinPlazoSubsanacion())) {
                    this.actualizarFechaPlazoSubsanacion(peticionPortafirmas, cdNumIden);
                }*/
                if (Objects.nonNull(peticionPortafirmas.getFcFinPlazoSubsanacion())) {

                    LocalDateTime fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(
                            peticionPortafirmas.getFcFinPlazoSubsanacion());

                    fechaLimite = new StringBuilder();
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getDayOfMonth()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getMonthValue()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(fcLimiteSubsanacion.getYear());

                }

            }
        }
        return fechaLimite.toString();
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public String obtenerFechaLimiteSubsanacionRequi(Integer idExpediente, String cdNumIden)
            throws ServiceException {

        StringBuilder fechaLimite = new StringBuilder();

        // Obtenemos los datos del expediente
        ExpedienteDTO expedienteDTO = this.buscarId(idExpediente);

        // consultar si existe fecha lectura note
        if (Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS.equals(expedienteDTO.getEstadoRevRequiDTO().getNmConstante())) {

            // Obtenemos un registro de los asociados a la petición de
            // subsanación
            Optional<ExpedienteReqDTO> opExpedienteReq = expedienteDTO.getLstExpedienteReqDTO()
                    .stream()
                    .filter(f -> f.getDocumentoPtfrDTO() != null
                            && f.getDocumentoPtfrDTO().getIdDocumentoPtfr() != null
                            && Constantes.AfirmacionNegacion.N.equals(f.getItCumple()))
                    .findFirst();

            // Inicializamos el formato de fecha
            fechaLimite.append("dd/MM/yyyy");

            if (opExpedienteReq.isPresent()) {

                // Obtenemos el registro de la petición a portafirmas
                DocumentoPtfr peticionPortafirmas = this.documentoPtfrDAO
                        .find(opExpedienteReq.get().getDocumentoPtfrDTO().getIdDocumentoPtfr());

                // Si no existe dato de plazo de subsanación, lo calculamos
                /*if (Objects.isNull(peticionPortafirmas.getFcFinPlazoSubsanacion())) {
                    this.actualizarFechaPlazoSubsanacion(peticionPortafirmas, cdNumIden);
                }*/

                if (Objects.nonNull(peticionPortafirmas.getFcFinPlazoSubsanacion())) {

                    LocalDateTime fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(
                            peticionPortafirmas.getFcFinPlazoSubsanacion());

                    fechaLimite = new StringBuilder();
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getDayOfMonth()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getMonthValue()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(fcLimiteSubsanacion.getYear());

                }
            }
        }

        return fechaLimite.toString();
    }

    /**
     *
     * @param documentoPtfr
     * @param listaActualizada
     * @param codUsu
     */
    private void actualizarFechaPlazoSubsanacion(DocumentoPtfr peticionPortafirmas, String codUsu)
            throws ServiceException {
        if (Objects.nonNull(peticionPortafirmas)
                && Objects.isNull(peticionPortafirmas.getFcIniPlazoSubsanacion())) {
            Boolean valFcEnvioNote = Constantes.AfirmacionNegacion.S
                    .equals(peticionPortafirmas.getItUsuAltaNote())
                    && Objects.nonNull(peticionPortafirmas.getFcEnvioNote());
            Boolean valFcFirmaPtfr = Objects.nonNull(peticionPortafirmas.getFcFirmaPtfr());

            if (Boolean.TRUE.equals(valFcEnvioNote) || Boolean.TRUE.equals(valFcFirmaPtfr)) {
                peticionPortafirmas.setFcIniPlazoSubsanacion(new Date());
                peticionPortafirmas.setItIniPlazoVencido(Constantes.AfirmacionNegacion.N);

                Expediente expediente = this.find(peticionPortafirmas.getIdExpediente());
                Solicitud solicitud = this.solicitudDAO
                        .find(expediente.getSolicitud().getIdSolicitud());
                LocalDateTime fcLimiteSubsanacion = this.calcularFechaLimiteSubsanacion(
                        solicitud.getConvocatoria().getNmDiasSubsanacion(),
                        DocumentoPtfrToDTO.INSTANCE.apply(peticionPortafirmas),peticionPortafirmas.getFcIniPlazoSubsanacion());

                peticionPortafirmas.setFcFinPlazoSubsanacion(
                        Utils.convertLocalDateToDateInstant(fcLimiteSubsanacion));
                if (!codUsu.isEmpty()) {
                    peticionPortafirmas.setCdUsuModif(codUsu);
                    peticionPortafirmas.setFcModifFila(new Date());
                }

                peticionPortafirmas = this.documentoPtfrDAO.merge(peticionPortafirmas);

            }

        }

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO aprobarEvaluacion(Integer idExpediente) throws ServiceException {

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Expediente expediente = this.find(idExpediente);
        CatalogosValores estadoRevInterno = this.catalogosValoresService
                .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK));
        expediente.setEstadoRevEval(estadoRevInterno);
        expediente = this.update(expediente);
        

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(expediente);
        dto = this.calcularTramitesFinalizados(dto);

        if (!Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL.equals(dto.getEstadoDTO().getNmConstante())) {
            expediente = ExpedienteToEntity.INSTANCE.apply(dto);
            this.expedienteHistDAO.crearHistorico(expediente, null);
        }

        return dto;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO rechazarEvaluacion(Integer idExpediente) throws ServiceException {

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Expediente expediente = this.find(idExpediente);
        CatalogosValores estadoRevInterno = this.catalogosValoresService
                .find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_KO));
        expediente.setEstadoRevEval(estadoRevInterno);
        expediente = this.update(expediente);

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(expediente);
        dto = this.calcularTramitesFinalizados(dto);

        if (!Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL.equals(dto.getEstadoDTO().getNmConstante())) {
            expediente = ExpedienteToEntity.INSTANCE.apply(dto);
            this.expedienteHistDAO.crearHistorico(expediente, null);
        }

        return dto;
    }

    /**
     * Metodo para actualizar el estado del informe de coordinador
     *
     * @param idExpediente
     * @param idEstado
     * @throws ServiceException
     */
    private void actualizarInformeCoordinador(Integer idExpediente, Integer idEstado) {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL,
                Constantes.TipoCoorEval.COORDINADOR, Operador.EQUALS));

        List<ExpedienteEval> lstExpedienteEval = this.expedienteEvalDAO.find(filtros, null);

        if (lstExpedienteEval != null) {
            for (ExpedienteEval eval : lstExpedienteEval) {
                eval.setEstado(new CatalogosValores(idEstado));
                this.expedienteEvalDAO.insertOrUpdate(eval);
            }
        }
    }

    /**
     *
     */
    @Override
    public List<ExpedienteFiltrosDTO> obtenerExpedienteCoorEval(
            ExpedienteFiltrosDTO expedienteFiltro) throws ServiceException {
        List<ExpedienteFiltrosDTO> result = this.getDao()
                .obtenerExpedienteCoorEval(expedienteFiltro);
        if (result != null && !result.isEmpty()) {
            for (ExpedienteFiltrosDTO item : result) {
                List<CatalogosValoresDTO> listaTipoInforme = null;
                List<InformeEvalDocDTO> listaInformesFinalizado = this.informeEvalDocService
                        .obtenerListaPorExpedienteEval(item.getIdExpedienteEval());

                if (item.getItCoorEval() != null && item.getItCoorEval().equalsIgnoreCase("E")) {
                    listaTipoInforme = this.expedienteEvalDAO.obtenerListaTipoInformesEvaluador(item,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoEvaluador.CERRADO_EVALUADOR));
                } else {
                    listaTipoInforme = this.expedienteEvalDAO.obtenerListaTipoInformesCoordinador(item,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoCoordinador.CERRADO_COORDINADOR));
                }

                if (listaTipoInforme != null && listaInformesFinalizado != null
                        && !listaInformesFinalizado.isEmpty()) {
                    java.util.Set<Integer> tiposYaEvaluados = listaInformesFinalizado.stream()
                            .filter(ied -> ied.getIdInforme() != null
                                    && ied.getIdInforme().getTipoInformeDTO() != null)
                            .map(ied -> ied.getIdInforme().getTipoInformeDTO().getIdValor())
                            .collect(java.util.stream.Collectors.toSet());
                    listaTipoInforme = listaTipoInforme.stream()
                            .filter(t -> !tiposYaEvaluados.contains(t.getIdValor()))
                            .collect(java.util.stream.Collectors.toList());
                }

                item.setListaTipoInformes(listaTipoInforme);
                item.setListaInformesEvalCoor(listaInformesFinalizado);
                
                // listarInformesFinalizados
                
                
                // EVALUACION JUSTIFICACION CT
                List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJustec = expedienteEvalJusTecService
                        .buscarPeriodosPendienteEvalJustec(item.getIdExpediente(), Boolean.TRUE);
                if (listaExpedienteEvalJustec != null && !listaExpedienteEvalJustec.isEmpty()
                        && listaExpedienteEvalJustec.size() > 1) {
                    // validar que por lo menos haya un periodo intermedio
                    // evaluado para poder mostrar evaluacion de periodo final
                    List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJustecPendiente = expedienteEvalJusTecService
                            .obtenerListaExpedienteEvalJustec(
                                    listaExpedienteEvalJustec.get(0).getIdExpedienteEvalJusTec());
                    boolean evaluadoIntermedio = false;
                    for (ExpedienteEvalJusTecDTO periodo : listaExpedienteEvalJustecPendiente) {
                        if (periodo.getEstadoDTO().getNmConstante()
                                .equals(Constantes.EstadoEvaluador.CERRADO_EVALUADOR_JUSTEC)
                                && periodo.getExpedienteJustecDTO().getTipoExpedienteJusTecDTO()
                                        .getDsValor().equalsIgnoreCase("Intermedia")) {
                            evaluadoIntermedio = true;
                        }

                    }
                    if (evaluadoIntermedio) {
                        item.setListaExpedienteEvalJustec(listaExpedienteEvalJustec);
                    } else {
                        // solo mostraremos de momento los periodos intermedios
                        // para ser evaluados
                        List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJustecPeridodoIntermedio = new ArrayList<>();
                        for (ExpedienteEvalJusTecDTO periodoIntermedio : listaExpedienteEvalJustec) {
                            if (periodoIntermedio.getExpedienteJustecDTO()
                                    .getTipoExpedienteJusTecDTO().getDsValor()
                                    .equalsIgnoreCase("Intermedia")) {
                                listaExpedienteEvalJustecPeridodoIntermedio.add(periodoIntermedio);
                            }
                        }
                        item.setListaExpedienteEvalJustec(
                                listaExpedienteEvalJustecPeridodoIntermedio);
                    }
                } else if (listaExpedienteEvalJustec != null
                        && !listaExpedienteEvalJustec.isEmpty()) {
                    item.setListaExpedienteEvalJustec(listaExpedienteEvalJustec);
                } else {
                    List<ExpedienteEvalJusTecDTO> listaExpedienteEvalJustecCerradaEval = expedienteEvalJusTecService
                            .buscarPeriodosPendienteEvalJustec(item.getIdExpediente(),
                                    Boolean.FALSE);
                    item.setListaExpedienteEvalJustec(listaExpedienteEvalJustecCerradaEval);
                }

            }
        }
        return result;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = ServiceException.class, propagation = Propagation.REQUIRES_NEW)
    public RegistroEregDTO guardarAlegacion(ExpedienteDocDTO expedienteDocDTO, String cdNumIden,
            String dniLogin, String cdNumIdenFuncional) throws ServiceException {

        if (Objects.isNull(expedienteDocDTO.getExpedienteDTO())
                && Objects.isNull(expedienteDocDTO.getExpedienteDTO().getIdExpediente())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Integer idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES);
        Integer idTipoDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES);
        
        //MD-2190 Quitamos la configuración cuando el trámite es alegación exclusión provisional
//        if (expedienteDocDTO.getTramiteDTO() != null
//                && expedienteDocDTO.getTramiteDTO().getIdValor() != null
//                && expedienteDocDTO.getTramiteDTO().getIdValor()
//                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL))
//                        ) {
//            idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL);
//            idTipoDoc = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL);
//        }
        
        // recuperamos el documento de alegación
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                expedienteDocDTO.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO, idTipoDoc,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        if (Objects.isNull(lstExpedienteDoc) || lstExpedienteDoc.isEmpty()) {
            throw new ServiceException("No ha subido fichero de alegación");
        }

        for (ExpedienteDoc expedienteDoc : lstExpedienteDoc) {

            expedienteDoc.setEstado(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            expedienteDoc.setFcModifFila(new Date());
            expedienteDoc.setCdUsuModif(cdNumIden);
            if(expedienteDocDTO.getTlObservaciones() != null && !expedienteDocDTO.getTlObservaciones().equals("")) {
                expedienteDoc.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            }
            
            this.expedienteDocDAO.merge(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdNumIden);

        }
        
        
        // recuperamos los documentos de alegación adicionales
        filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                expedienteDocDTO.getExpedienteDTO().getIdExpediente(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                Operador.NULL));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDocAdicionales = this.expedienteDocDAO.find(filtros, null);

        for (ExpedienteDoc expedienteDoc : lstExpedienteDocAdicionales) {

            expedienteDoc.setEstado(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
            expedienteDoc.setFcModifFila(new Date());
            expedienteDoc.setCdUsuModif(cdNumIden);
            this.expedienteDocDAO.merge(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdNumIden);

        }

        // actualizar estado de expediente
        Solicitud solicitud = this.solicitudDAO
                .find(expedienteDocDTO.getExpedienteDTO().getIdSolicitud());
        Convocatoria convocatoria = solicitud.getConvocatoria();

        Expediente expediente = this.getDao()
                .find(expedienteDocDTO.getExpedienteDTO().getIdExpediente());

        // HLA 08/10/2024 Si la convocatoria es con alegaciones el expediente
        // pasa al estado PENDIENTE DE REVISION ALEGACIONES
        if (idTramite.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES))
                && Constantes.AfirmacionNegacion.S.equals(convocatoria.getItAlegacion())) {
            expediente.setEstado(catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES)));
        } 

        // no hay equivalente para el estado del beneficiario
        expediente.setCdUsuModif(cdNumIden);
        expediente.setItAlegaciones(Constantes.AfirmacionNegacion.S);
        expediente.setFcModifFila(new Date());
        expediente = this.getDao().merge(expediente);
        this.expedienteHistDAO.crearHistorico(expediente, cdNumIden);

        // Actualizar notificaciones de los coordinadores/evaluadores del
        // expediente
        List<ExpedienteEval> lstExpedienteEval = this
                .buscarExpedienteEvalxIdExpediente(expediente.getIdExpediente(), null);
        for (ExpedienteEval expeEval : lstExpedienteEval) {
            expeEval.setItNotificado(Constantes.Notificacion.ITNOTIFICADO_S);
            expeEval.setCdUsuModif(cdNumIden);
            expeEval.setFcModifFila(new Date());
            this.expedienteEvalDAO.update(expeEval);
        }

     // Recuperamos los datos actualizados del expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(expedienteDocDTO.getExpedienteDTO().getIdExpediente(), Boolean.FALSE);
        String cifEntidadLogin= null;
        if(!cdNumIdenFuncional.equalsIgnoreCase(dniLogin)) {
            cifEntidadLogin=cdNumIdenFuncional;
        }

        expedienteDTO.setDniUserLogin(dniLogin);
        expedienteDTO.setCifEntidadLogin(cifEntidadLogin);
        // retornamos respuesta ereg
        if (idTramite.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES))) {
            return this.prepararRespuestaEregAleg(
                    expedienteDTO, Boolean.FALSE,
                    Boolean.TRUE, Boolean.FALSE, Constantes.ReciboPresentacion.ASUNTO_ALEGACIONES,
                    idTramite);
        } else {
            return this.prepararRespuestaEregAleg(
                    expedienteDTO, Boolean.FALSE,
                    Boolean.TRUE, Boolean.FALSE,
                    Constantes.ReciboPresentacion.ASUNTO_ALEGACIONES_EXCLUIDO_PROVISIONAL,
                    idTramite);
        }

    }

    /**
     * Presenta el trámite de Notificación del ciudadano: completa la
     * notificación "en curso" (dada de alta al subir documentos adicionales,
     * o creada aquí si no se subió ninguno) con el asunto/expone/solicita en
     * SAII_NOTIFI_CIUD, recupera los documentos ya enlazados a ella en
     * SAII_NOTIFI_CIUD_DOC y lo envía a registro EREG.
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = ServiceException.class, propagation = Propagation.REQUIRES_NEW)
    public RegistroEregDTO presentarDesistimientoExpreso(Integer idExpediente, Integer idAsunto,
            String tlExpone, String tlSolicita, String cdNumIden, String dniLogin,
            String cdNumIdenFuncional) throws ServiceException {

        if (Objects.isNull(idExpediente) || Objects.isNull(idAsunto)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Expediente expediente = this.getDao().find(idExpediente);
        if (Objects.isNull(expediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Integer idTramite = utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.NOTIFICACION_CIUDADANO);

        // Buscamos si ya existe una notificación "en curso" para este
        // expediente y asunto (dada de alta al subir documentos adicionales,
        // pero todavía no presentada: no tiene CD_EREG). Si existe, la
        // reutilizamos (ya tiene documentos enlazados en
        // SAII_NOTIFI_CIUD_DOC); si no, la creamos ahora
        NotificacionCiudadano notificacionCiudadano = this.notificacionCiudadanoService
                .buscarEnCursoEntidad(idExpediente, idAsunto);

        if (Objects.isNull(notificacionCiudadano)) {
            notificacionCiudadano = new NotificacionCiudadano();
            Expediente expAux = new Expediente();
            expAux.setIdExpediente(idExpediente);
            notificacionCiudadano.setExpediente(expAux);
            notificacionCiudadano.setAsunto(catalogosValoresService.find(idAsunto));
            notificacionCiudadano.setCdUsuAlta(cdNumIden);
            notificacionCiudadano.setFcAltaFila(new Date());
            notificacionCiudadano = this.notificacionCiudadanoDAO.insert(notificacionCiudadano);
        }

        notificacionCiudadano.setTlExpone(tlExpone);
        notificacionCiudadano.setTlSolicita(tlSolicita);
        notificacionCiudadano.setEstado(catalogosValoresService.find(utilConstantes
                .getGlobalCatalogosValores().get(Constantes.EstadoNotifiCiud.PENDIENTE_REVISION)));

        // Recuperamos los datos actualizados del expediente (datos completos,
        // necesitamos el impreso para obtener el código de asunto EREG)
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.TRUE);
        String cifEntidadLogin = null;
        if (!cdNumIdenFuncional.equalsIgnoreCase(dniLogin)) {
            cifEntidadLogin = cdNumIdenFuncional;
        }
        expedienteDTO.setDniUserLogin(dniLogin);
        expedienteDTO.setCifEntidadLogin(cifEntidadLogin);

        // Se recupera el código de asunto EREG del impreso del expediente
        // (mismo criterio que en alegaciones/subsanaciones) y la unidad
        // administrativa destino
        String cdAsunto = null;
        if (expedienteDTO.getImpresoDTO() != null
                && StringUtils.isNotBlank(expedienteDTO.getImpresoDTO().getCdIdentificacion())) {
            String cdIdentificacion = expedienteDTO.getImpresoDTO().getCdIdentificacion();
            Integer indexContains = cdIdentificacion.indexOf('_');
            cdAsunto = indexContains < 0 ? cdIdentificacion
                    : cdIdentificacion.substring(0, indexContains);
        }
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
        Integer unidadAdmin = this.pdfService.calcularUnidadAdmin(solicitud);
        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);
        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // Registramos una nueva entrada en EREG específica para este trámite
        log.info("Se va a generar la entrada en EREG de notificación del ciudadano");
        expedienteDTO = this.eregAdmeService.generarEntradaNotificacionCiudadano(expedienteDTO,
                cdAsunto, cdUnidad);
        log.info("Código: " + expedienteDTO.getCdEregNotifiCiud());
        log.info("Destino: " + expedienteDTO.getDsDestinoEreg());

        // Guardamos el código EREG de la notificación (valor íntegro, tal
        // cual lo devuelve EREG, sin formatear)
        notificacionCiudadano.setCdEreg(expedienteDTO.getCdEregNotifiCiud());
        notificacionCiudadano.setDsDestinoEreg(expedienteDTO.getDsDestinoEreg());
        notificacionCiudadano.setFcFirmaEreg(new Date());
        notificacionCiudadano.setCdUsuModif(cdNumIden);
        notificacionCiudadano.setFcModifFila(new Date());
        notificacionCiudadano = this.notificacionCiudadanoDAO.merge(notificacionCiudadano);

        // Recuperamos únicamente los documentos adicionales ya vinculados a
        // esta notificación concreta en SAII_NOTIFI_CIUD_DOC (enlazados en el
        // momento de subirlos), nunca los de otras notificaciones anteriores
        // o posteriores del mismo expediente
        List<AtlasFilter> filtrosDocEnlazados = new ArrayList<>();
        filtrosDocEnlazados.add(new AtlasFilter(
                Constantes.Filtro.FILTRO_NOTIFICACION_CIUDADANO_DOC_ID_NOTIFICACION,
                notificacionCiudadano.getIdNotificacion(), Operador.EQUALS));
        filtrosDocEnlazados.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        // El documento puede haber sido borrado (baja lógica en
        // SAII_EXPEDIENTE_DOC) sin que se haya dado de baja la relación en
        // SAII_NOTIFI_CIUD_DOC, así que se excluyen también esos casos
        filtrosDocEnlazados.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA,
                Operador.NULL));
        List<NotificacionCiudadanoDoc> lstDocEnlazados = this.notificacionCiudadanoDocDAO
                .find(filtrosDocEnlazados, null);

        List<ExpedienteDoc> lstDocPendientesEnlazar = lstDocEnlazados.stream()
                .map(NotificacionCiudadanoDoc::getExpedienteDoc).collect(Collectors.toList());

        // Datos obligatorios del trámite que deben figurar en el PDF del
        // recibo de presentación, independientemente de si se aporta o no
        // documentación adicional (opcional)
        expedienteDTO.setDsAsuntoNotifiCiud(notificacionCiudadano.getAsunto().getDsValor());
        expedienteDTO.setTlExponeNotifiCiud(tlExpone);
        expedienteDTO.setTlSolicitaNotifiCiud(tlSolicita);

        // Generamos y firmamos el recibo de presentación / listado de
        // anexos de este trámite y lo anexamos a la entrada EREG recién
        // creada (actualiza también el código EREG y estado de cada
        // documento adicional pendiente de enlazar)
        List<ExpedienteDocDTO> lstExpedienteDocDTO = lstDocPendientesEnlazar.stream()
                .map(ExpedienteDocToDTO.INSTANCE).collect(Collectors.toList());

        RegistroEregDTO registroEregDTO = this.prepararRespuestaEregAleg(expedienteDTO,
                Boolean.FALSE, Boolean.FALSE, Boolean.FALSE,
                Constantes.ReciboPresentacion.ASUNTO_DESISTIMIENTO_EXPRESO, idTramite,
                lstExpedienteDocDTO);
        registroEregDTO.setCdEreg(Utils.formatearNmEreg(notificacionCiudadano.getCdEreg()));
        registroEregDTO.setEsDescargaZipNotificacionCiudadano(Boolean.TRUE);

        return registroEregDTO;
    }

    /**
     *
     */
    @Override
    public NotificacionCiudadanoDTO buscarNotificacionCiudadanoEnCurso(Integer idExpediente,
            Integer idAsunto) throws ServiceException {
        return this.notificacionCiudadanoService.buscarEnCurso(idExpediente, idAsunto);
    }

    /**
     *
     */
    @Override
    public ExpedienteDTO comprobarResolucionDefinitiva(Integer idExpediente,
            ConvocatoriaDTO convocatoriaDTO) throws ServiceException {

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);
        Integer idEstado = expedienteDTO.getEstadoDTO().getIdValor();

        // mensaje 1
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo1(Boolean.FALSE);
        // si no existe el tipo de documento de resolucion definitiva mostrará
        // un mensaje
        if (Boolean.FALSE.equals(this.convocatoriaDocService.existeTipoDocumento(
                expedienteDTO.getConvocatoriaActDTO().getIdConvocatoria(),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RESOLUCION_DEFINITIVA)))) {
            expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo1(Boolean.TRUE);
        }

        // mensaje 2
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo2(Boolean.FALSE);

        List<Integer> lstEstadoPermiteMsg2 = new ArrayList<>();
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION));

        if (!lstEstadoPermiteMsg2.contains(idEstado)) {
            expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo2(Boolean.TRUE);
        }

        // mensaje 3
        // si es un expediente solicitante, validamos si existen expedientes
        // hijos en estado diferente a los finales
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo3(Boolean.FALSE);

        if (convocatoriaDTO != null && convocatoriaDTO.getIdTipoConcesion() != null
                && convocatoriaDTO.getIdTipoConcesion().getIdValor() != null
                && convocatoriaDTO.getIdTipoConcesion().getNmConstante().equals(Constantes.TipoConcesion.CONCESION_PARTICIPANTE)
                && expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO().getNmConstante().equals(Constantes.TipoActor.SOLICITANTE)) {
            if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())
                    && Boolean.TRUE.equals(this.getDao()
                            .validarExpedienteSolicitanteHijoEstadoFinal(expedienteDTO))) {
                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo3(Boolean.TRUE);
            }

        } else {
            if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())
                    && Boolean.FALSE.equals(this.getDao()
                            .validarExpedienteSolicitanteHijoEstadoFinal(expedienteDTO))) {
                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo3(Boolean.TRUE);

            }
        }

        // mensaje 4
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo4(Boolean.FALSE);
        if (convocatoriaDTO != null && (StringUtils.isBlank(convocatoriaDTO.getItAlegacion())
                || convocatoriaDTO.getItAlegacion().equals(Constantes.AfirmacionNegacion.S)
                        && convocatoriaDTO.getFcIniAlegaciones() != null
                        && convocatoriaDTO.getFcFinAlegaciones() != null)) {
            // Si está en plazo de alegaciones hay que deshabilitar
            Boolean habilitar = !this.verificarFechaFinInicioAlegaciones(
                    convocatoriaDTO.getFcIniAlegaciones(), convocatoriaDTO.getFcFinAlegaciones());

            if (Boolean.FALSE.equals(habilitar)) {
                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivo4(Boolean.TRUE);
            }

        }

        // mensaje 1 individual
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual1(Boolean.FALSE);
        // si no existe el tipo de documento de resolucion provisional o
        // definitiva muestra un mensaje
        List<Integer> lstTipoDoc = new ArrayList<>();
        lstTipoDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
        lstTipoDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        if (Boolean.FALSE.equals(expedienteDocService
                .existeTipoDocumentos(expedienteDTO.getIdExpediente(), lstTipoDoc))) {
            expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual1(Boolean.TRUE);
        }

        // mensaje 2 individual
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual2(Boolean.FALSE);
        lstEstadoPermiteMsg2 = new ArrayList<>();
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
        lstEstadoPermiteMsg2.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
        if (!lstEstadoPermiteMsg2.contains(idEstado)) {
            expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual2(Boolean.TRUE);
        }

        // mensaje 4 individual
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual4(Boolean.FALSE);
        if (convocatoriaDTO != null && (StringUtils.isBlank(convocatoriaDTO.getItAlegacion())
                || convocatoriaDTO.getItAlegacion().equals(Constantes.AfirmacionNegacion.S)
                        && convocatoriaDTO.getFcIniAlegaciones() != null
                        && convocatoriaDTO.getFcFinAlegaciones() != null)) {
            // Si está en plazo de alegaciones hay que deshabilitar
            Boolean habilitar = !this.verificarFechaFinInicioAlegaciones(
                    convocatoriaDTO.getFcIniAlegaciones(), convocatoriaDTO.getFcFinAlegaciones());

            if (Boolean.FALSE.equals(habilitar)) {
                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual4(Boolean.TRUE);
            }
        }

        // mensaje 5 individual
        List<Integer> lstTramites = new ArrayList<>();
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
        List<DocumentoPtfrDTO> lstDocPtfrDTO = this.documentoPtfrDAO
                .obtenerPorExpedienteYTramites(idExpediente, lstTramites);
        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual5(Boolean.FALSE);
        if (lstDocPtfrDTO != null && !lstDocPtfrDTO.isEmpty()) {
            DocumentoPtfrDTO docPtfrDTO = lstDocPtfrDTO.get(0);
            if (docPtfrDTO.getFcLecturaNote() != null) {
                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual5(Boolean.TRUE);
            }
        }

//        //mensaje 6 individual
//        List<Integer> lstTramitesDefinitivos = new ArrayList<>();
//        lstTramitesDefinitivos.add(Constantes.Tramite.CONCESION_DEFINITIVA);
//        lstTramitesDefinitivos.add(Constantes.Tramite.DENEGACION_DEFINITIVA);
//        List<DocumentoPtfrDTO> lstDocPtfrDefinitivoDTO = this.documentoPtfrDAO
//                .obtenerByExpedienteAndTramites(idExpediente, lstTramitesDefinitivos);
//        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual6(Boolean.FALSE);
//        if (lstDocPtfrDefinitivoDTO != null && !lstDocPtfrDefinitivoDTO.isEmpty()) {
//            DocumentoPtfrDTO docPtfrDTO = lstDocPtfrDefinitivoDTO.get(0);
//            if (docPtfrDTO.getFcLecturaNote() != null) {
//                expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual6(Boolean.TRUE);
//            }
//        }
//        
//        // mensaje 7 individual
//        expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual7(Boolean.FALSE);
//        lstTipoDoc = new ArrayList<>();
//        lstTipoDoc.add(Constantes.Tramite.CONCESION_DEFINITIVA);
//        lstTipoDoc.add(Constantes.Tramite.DENEGACION_DEFINITIVA);
//        if (Boolean.FALSE.equals(expedienteDocService
//                .existeTipoDocumentos(expedienteDTO.getIdExpediente(), lstTipoDoc))) {
//            expedienteDTO.setBlMostrarMensajeAprobarDenegarDefinitivoIndividual7(Boolean.TRUE);
//        }

        expedienteDTO.setBlHabilitarResolucionDefinitiva(Boolean.TRUE);
        if (Boolean.TRUE.equals(expedienteDTO.getBlMostrarMensajeAprobarDenegarDefinitivo1())
                || Boolean.TRUE.equals(expedienteDTO.getBlMostrarMensajeAprobarDenegarDefinitivo2())
                || Boolean.TRUE.equals(expedienteDTO.getBlMostrarMensajeAprobarDenegarDefinitivo3())
                || Boolean.TRUE
                        .equals(expedienteDTO.getBlMostrarMensajeAprobarDenegarDefinitivo4())) {
            expedienteDTO.setBlHabilitarResolucionDefinitiva(Boolean.FALSE);
        }

        return expedienteDTO;
    }

    private Boolean verificarFechaFinInicioAlegaciones(Date fcInicio, Date fcFin) {

        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaAlegacionesInicio = fcInicio.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate fechaAlegacionesFin = fcFin.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
        return fechaHoy.compareTo(fechaAlegacionesInicio) >= 0
                && fechaHoy.compareTo(fechaAlegacionesFin) <= 0;

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void actualizarExpedienteConFcAlegacionNoVencidaBatch() throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.IT_ALEGACIONES, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<Expediente> lstExpediente = this.find(filtros, null);

        if (Objects.nonNull(lstExpediente) && !lstExpediente.isEmpty()) {
            for (Expediente expediente : lstExpediente) {

                Convocatoria convocatoria = this.convocatoriaDAO
                        .find(expediente.getConvocatoriaAct().getIdConvocatoria());

                TipoResolModalidad tipoResolModalidad = tipoResolModalidadDAO
                        .findByIdModalidad(convocatoria.getIdModalidadConvocatoria().getIdValor());

                if (Objects.nonNull(convocatoria)
                        && Objects.nonNull(convocatoria.getIdConvocatoria())
                        && convocatoria.getItConvocatoriaActiva().equals("S")
                        && convocatoria.getItAlegacion().equals("S")
                        && tipoResolModalidad.getCdTipoResolucion()
                                .equals(Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA)) {

                    LocalDateTime fechaHoy = LocalDateTime.now();
                    LocalDateTime fechaAlegacionesFin = Utils
                            .convertDateToLocalDateTime(convocatoria.getFcFinAlegaciones());

                    CatalogosValores estado = convocatoria.getIdEstado();

                    // valida que la fecha de fin de alegación sea menor al dia
                    // actual y valida si itAlegaciones es null, entonces
                    // seteará el valor a "N"
                    if (Objects.nonNull(fechaAlegacionesFin)
                            && fechaAlegacionesFin.isBefore(fechaHoy)
                            && !estado.getNmConstante().equals(Constantes.EstadoConvocatoria.RESUELTA)) {

                        expediente.setItAlegaciones(Constantes.AfirmacionNegacion.N);
                        expediente.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                        expediente.setFcModifFila(new Date());

                        this.getDao().insertOrUpdate(expediente);
                        this.expedienteHistDAO.crearHistorico(expediente, Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                    }

                }

            }
        }
    }

    /**
     * @return
     *
     */
    @Override
    @Transactional(readOnly = false)
    public List<Integer> actualizarEstadoExpedienteContratoBatch() throws ServiceException {

        List<Integer> lstExpedientes = this.expedienteDAO.listaExpedienteConContrato();

        if (Objects.nonNull(lstExpedientes) && !lstExpedientes.isEmpty()) {

            for (Integer id_Expediente : lstExpedientes) {

                // this.cambiarEstado(id_Expediente,Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_CONTRATOS,
                // Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

                Expediente expedientePadre = this.find(id_Expediente);

                expedientePadre.setEstadoRevContrato(catalogosValoresService.find(
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_CONTRATOS)));
                expedientePadre.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

                this.update(expedientePadre);
                this.expedienteHistDAO.crearHistorico(expedientePadre, Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
            }
        }

        return lstExpedientes;
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public void actualizarEstadoExpedienteAlegacionBatch() throws ServiceException {

        List<Integer> lstEstadoExpediente = new ArrayList<>();
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL));
        lstEstadoExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.IT_ALEGACIONES,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpediente,
                Operador.IN));

        List<Expediente> lstExpediente = this.find(filtros, null);

        if (Objects.nonNull(lstExpediente) && !lstExpediente.isEmpty()) {
            for (Expediente expediente : lstExpediente) {

                Convocatoria convocatoria = this.convocatoriaDAO
                        .find(expediente.getConvocatoriaAct().getIdConvocatoria());

                TipoResolModalidad tipoResolModalidad = tipoResolModalidadDAO
                        .findByIdModalidad(convocatoria.getIdModalidadConvocatoria().getIdValor());

                if (Objects.nonNull(convocatoria)
                        && Objects.nonNull(convocatoria.getIdConvocatoria())
                        && convocatoria.getItConvocatoriaActiva().equals("S")
                        && convocatoria.getItAlegacion().equals("S")
                        && tipoResolModalidad.getCdTipoResolucion()
                                .equals(Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA)) {

                    this.cambiarEstado(expediente.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_DE_RESOLUCION),
                            Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                }

            }

        }
    }

    /**
     * Método que calcula si el expediente está en tramite y ya se han concluido
     * los tramites de administracion, requisitos y evaluacion.
     *
     * Si es asi se pasa el expediente a Pendiente provisional
     *
     * @param dto
     * @return dto
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO calcularTramitesFinalizados(ExpedienteDTO dto) throws ServiceException {
        //GIM 25/09/25 MD-1928 Añadimos estado EN_TRAMITACION_PRESENTADA  pero falta verificar que es correcto.
        //VMM 08/09/25 MD-1955: comprobamos tambien el estado de la financiacion para pasar a pdte.previsional
        // Estado en tramitacion
        
        Convocatoria convocatoria = this.convocatoriaDAO.find(dto.getConvocatoriaActDTO().getIdConvocatoria());
              
        ConvocatoriaDTO convoDto = ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
        
        if ( (Constantes.EstadoExpediente.EN_TRAMITACION.equals(dto.getEstadoDTO().getNmConstante()) || Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA.equals(dto.getEstadoDTO().getNmConstante())) 
                && Constantes.EstadoExpediente.REVISION_OK.equals(dto.getEstadoRevAdminDTO().getNmConstante())
                && Constantes.EstadoExpediente.REVISION_OK.equals(dto.getEstadoRevRequiDTO().getNmConstante())
                && (Constantes.EstadoExpediente.EVALUADO_KO.equals(dto.getEstadoRevEvalDTO().getNmConstante())
                        || Constantes.EstadoExpediente.EVALUADO_OK.equals(dto.getEstadoRevEvalDTO().getNmConstante()))
                && Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK.equals(dto.getEstadoRevFinanDTO().getNmConstante())) {

            
            
            Integer estadoResolucionPendiente;
            if(Boolean.TRUE.equals(convoDto.getItResProvConcDeneg())) {
                estadoResolucionPendiente = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_PROVISIONAL);
            }else {
                estadoResolucionPendiente = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_DEFINITIVO);
            }
            
            dto = this.cambiarEstado(dto.getIdExpediente(), estadoResolucionPendiente,  null);
        }

        return dto;
    }

    /**
     * Metodo para bsucar expedienteEval
     *
     * @param idExpediente
     * @param idEstado
     * @throws ServiceException
     */
    private List<ExpedienteEval> buscarExpedienteEvalxIdExpediente(Integer idExpediente,
            String itCoorEval) throws ServiceException {

        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (StringUtils.isNotBlank(itCoorEval)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, itCoorEval,
                    Operador.EQUALS));
        }

        return this.expedienteEvalDAO.find(filtros, null);

    }

    /**
     *
     */
    @Override
    public LocalDateTime calcularFechaLimiteSubsanacion(Integer diasSubsanacion,
            DocumentoPtfrDTO docuPtfr, Date fechaLectura) throws ServiceException {

        LocalDateTime fcLimiteSubsanacion = null;
        LocalDateTime ldtFechaDesde = null;

        if (Objects.nonNull(docuPtfr.getFcIniPlazoSubsanacion())) {

            // Asignamos horas a inicio de día
            //Date fcInicio = Utils.asignarTiempoFechaInicioDia(docuPtfr.getFcIniPlazoSubsanacion());
            Date fcInicio = Utils.asignarTiempoFechaInicioDia(fechaLectura);


            ldtFechaDesde = Utils.convertDateToLocalDateTime(fcInicio);

        }

        if (Objects.nonNull(ldtFechaDesde)) {

            Expediente expediente = this.find(docuPtfr.getIdExpediente());
                      

            Date fechaFinCalculada = this.cataService.calcularDiasHabiles(
                    Utils.convertLocalDateToDateInstant(ldtFechaDesde), diasSubsanacion,
                    Constantes.Suca.CD_MUNI_MADRID, expediente.getInteresado().getCdLocalidad());

            if (Objects.isNull(fechaFinCalculada)) {
                throw new ServiceException(Constantes.Error.ERROR_NO_SE_PUDO_CALCULAR_FECHA);
            }

            fechaFinCalculada = Utils.asignarTiempoFechaFinalDia(fechaFinCalculada);
            fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(fechaFinCalculada);

        }

        return fcLimiteSubsanacion;

    }

    @Override
    public Map<String, Object> obtenerMapaDatosExcel(Integer idExpediente) throws ServiceException {
        return this.getDao().obtenerMapaDatosExcel(idExpediente);
    }

    @Override
    public int obtenerCantidadColumnasReservadaActuacion(List<Integer> lstIdExpediente)
            throws ServiceException {
        return this.getDao().obtenerCantidadColumnasReservadaActuacion(lstIdExpediente);
    }

    @Override
    public Map<String, String> obtenerMapaDatosComunExcel(List<Integer> lstIdExpediente)
            throws ServiceException {
        return this.getDao().obtenerMapaDatosComunExcel(lstIdExpediente);
    }

    @Override
    public Map<String, String> obtenerMapaDatosActuacionFinExcel(List<Integer> lstIdExpediente)
            throws ServiceException {
        return this.getDao().obtenerMapaDatosActuacionFinExcel(lstIdExpediente);
    }

    @Override
    public List<ExpedienteDocDTO> obtenerRequisitoSubsanado(Integer idExpediente)
            throws ServiceException {

        // Validamos los datos de entrada
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<Integer> estadosDocumento = new ArrayList<>();
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

        List<ExpedienteDocDTO> listaExpedienteDoc = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosDocumento,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        if (!lstExpedienteDoc.isEmpty()) {
            for (ExpedienteDoc expedienteDoc : lstExpedienteDoc) {

                List<AtlasFilter> filtrosExp = new ArrayList<>();
                filtrosExp.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC,
                        expedienteDoc.getIdExpedienteDoc(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                List<ExpedienteReq> exp = this.expedienteReqDAO.find(filtrosExp, null);

                // Es documento de requisito
                if (!exp.isEmpty()) {

                    if (Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION.equals(expedienteDoc.getEstado().getNmConstante())) {

                        ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                                .apply(expedienteDoc);
                        expedienteDocDTO.setIdDocumentoPtfr(
                                exp.get(0).getDocumentoPtfr().getIdDocumentoPtfr());

                        expedienteDocDTO.setNombreFicheroFica(
                                this.recuperarNombreFicheroFica(expedienteDoc.getCdFica()));

                        expedienteDocDTO.setEsDocumentoRequisito(Boolean.TRUE);
                        expedienteDocDTO
                                .setDsDocumentoRequisito(exp.get(0).getRequisito().getDsValor());
                        expedienteDocDTO.setDsObservacionRequisito(exp.get(0).getTlObservaciones());
                        listaExpedienteDoc.add(expedienteDocDTO);

                    } else {
                        filtros = new ArrayList<>();
                        filtros.add(new AtlasFilter(
                                Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                expedienteDoc.getIdExpedienteDoc(), Operador.EQUALS));

                        List<AtlasSort> orden = new ArrayList<>();
                        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.TRUE));

                        List<ExpedienteDocHist> lstExpedDocHist = this.expedienteDocHistDAO
                                .find(filtros, orden);

                        if (!lstExpedDocHist.isEmpty() && lstExpedDocHist.size() > 1) {
                            Integer indexPenultimo = lstExpedDocHist.size() - 2;
                            ExpedienteDocHist expedienteDocHist = lstExpedDocHist
                                    .get(indexPenultimo);
                            if (Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION.equals(expedienteDocHist.getEstado().getNmConstante())) {

                                ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                                        .apply(expedienteDoc);
                                expedienteDocDTO.setIdDocumentoPtfr(
                                        exp.get(0).getDocumentoPtfr().getIdDocumentoPtfr());
                                expedienteDocDTO.setNombreFicheroFica(
                                        this.recuperarNombreFicheroFica(expedienteDoc.getCdFica()));
                                expedienteDocDTO.setEsDocumentoRequisito(Boolean.TRUE);
                                expedienteDocDTO.setDsDocumentoRequisito(
                                        exp.get(0).getRequisito().getDsValor());
                                expedienteDocDTO
                                        .setDsObservacionRequisito(exp.get(0).getTlObservaciones());
                                expedienteDocDTO.setTlObservacionesGestor(
                                        exp.get(0).getTlObservacionesCiudadano());
                                expedienteDocDTO.setConvocatoriaCauDTO(ConvocatoriaCauToDTO.INSTANCE
                                        .apply(expedienteDoc.getConvocatoriaCau()));
                                expedienteDocDTO.setTlCausaDescripcion(
                                        expedienteDoc.getTlCausaDescripcion());
                                listaExpedienteDoc.add(expedienteDocDTO);
                            }
                        }
                    }

                }

            }
        }

        return listaExpedienteDoc;

    }

    @Override
    public DocumentoPtfrDTO obtenerInformacionNotificacion(Integer idDocumentoPtfr)
            throws ServiceException {
        if (Objects.isNull(idDocumentoPtfr)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        DocumentoPtfrDTO documentoPtfrDTO = new DocumentoPtfrDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDDOCUMENTOPTFR, idDocumentoPtfr,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<DocumentoPtfr> lstDocumentos = this.documentoPtfrDAO.find(filtros, null);
        if (!lstDocumentos.isEmpty()) {
            documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(lstDocumentos.get(0));
        }

        return documentoPtfrDTO;
    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocumentoPendienteSubsanar(Integer idExpediente)
            throws ServiceException {
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<ExpedienteDocDTO> listaExpedienteDoc = new ArrayList<>();

        List<Integer> estadosDocumento = new ArrayList<>();
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
        
        List<Integer> tramites = new ArrayList<>();
        tramites.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION));
        tramites.add(utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosDocumento,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, tramites, Operador.IN));
        // añadir los doc sde subsanacion amdin
        // añadimos también el requerimiento de subsanación administrativa

        List<ExpedienteDocHist> lstExpedienteDocHist = this.expedienteDocHistDAO.find(filtros,
                null);

        if (!lstExpedienteDocHist.isEmpty()) {

            List<DocumentoPtfr> lstDocumento = lstExpedienteDocHist.stream()
                    .filter(ed -> Objects.nonNull(ed.getIdDocumentoPtfr()))
                    .map(ExpedienteDocHist::getIdDocumentoPtfr).distinct()
                    .map(id -> this.documentoPtfrDAO.find(id)).collect(Collectors.toList());

            int idDocumento = -1;
            if (!lstDocumento.isEmpty()) {
                Date fcEnvio = lstDocumento.get(0).getFcEnvioPtfr();
                idDocumento = lstDocumento.get(0).getIdDocumentoPtfr();
                for (DocumentoPtfr documento : lstDocumento) {
                    if (documento.getFcEnvioPtfr().after(fcEnvio)) {
                        idDocumento = documento.getIdDocumentoPtfr();
                        fcEnvio = documento.getFcEnvioPtfr();
                    }
                }
            }

            for (ExpedienteDocHist expedienteDocHist : lstExpedienteDocHist) {

                ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                        .applyFromHistory(expedienteDocHist);

                if (expedienteDocDTO != null) {
                    expedienteDocDTO.setNombreFicheroFica(
                            this.recuperarNombreFicheroFica(expedienteDocHist.getCdFica()));

                    expedienteDocDTO.setUltimoEnvio(Boolean.FALSE);
                    if (Objects.nonNull(expedienteDocDTO.getIdDocumentoPtfr()) && idDocumento != -1
                            && expedienteDocDTO.getIdDocumentoPtfr() == idDocumento) {
                        expedienteDocDTO.setUltimoEnvio(Boolean.TRUE);
                    }
                    listaExpedienteDoc.add(expedienteDocDTO);
                }
            }
        }

        return listaExpedienteDoc;
    }

    /**
     * Metodo para comprobar que el documento del historico coincida con el del
     * expediente doc
     *
     * @param expedienteDocHistDTO
     * @return ExpedienteDocDTO
     * @throws ServiceException 
     */
    private ExpedienteDocDTO comprobarDocumentosHist(ExpedienteDocHistDTO expedienteDocHistDTO) {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                expedienteDocHistDTO.getIdExpediente(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        if (!lstExpedienteDoc.isEmpty()) {

            for (ExpedienteDoc expedienteDoc : lstExpedienteDoc) {
                if (expedienteDoc.getIdExpedienteDoc()
                        .equals(expedienteDocHistDTO.getExpedienteDocDTO().getIdExpedienteDoc())) {
                    return ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc);
                }
            }
        }

        return null;

    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocumentoSubsanado(Integer idExpediente)
            throws ServiceException {
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<Integer> estadosDocumento = new ArrayList<>();
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO));
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));

        List<ExpedienteDocDTO> listaExpedienteDoc = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosDocumento,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        if (!lstExpedienteDoc.isEmpty()) {
            for (ExpedienteDoc expedienteDoc : lstExpedienteDoc) {
                if (Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION.equals(expedienteDoc.getEstado().getNmConstante())) {

                    ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                            .apply(expedienteDoc);

                    expedienteDocDTO.setNombreFicheroFica(
                            this.recuperarNombreFicheroFica(expedienteDoc.getCdFica()));

                    listaExpedienteDoc.add(expedienteDocDTO);
                } else {
                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(
                            Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                            expedienteDoc.getIdExpedienteDoc(), Operador.EQUALS));

                    List<AtlasSort> orden = new ArrayList<>();
                    orden.add(new AtlasSort(Constantes.Filtro.FILTRO_FCALTA, Boolean.TRUE));

                    List<ExpedienteDocHist> lstExpedDocHist = this.expedienteDocHistDAO
                            .find(filtros, orden);

                    if (!lstExpedDocHist.isEmpty() && lstExpedDocHist.size() > 1) {
                        Integer indexPenultimo = lstExpedDocHist.size() - 2;
                        ExpedienteDocHist expedienteDocHist = lstExpedDocHist.get(indexPenultimo);
                        if (Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION.equals(expedienteDocHist.getEstado().getNmConstante())) {

                            ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                                    .apply(expedienteDoc);
                            expedienteDocDTO.setNombreFicheroFica(
                                    this.recuperarNombreFicheroFica(expedienteDoc.getCdFica()));
                            listaExpedienteDoc.add(expedienteDocDTO);
                        }
                    }
                }

            }
        }

        return listaExpedienteDoc;
    }

    @Override
    public List<ExpedienteDTO> obtenerExpedienteEstructurado(Integer idSolicitud, String todos)
            throws ServiceException {

        // Configuramos lista de tipo de actores de primer nivel
        List<Integer> lstActores = new ArrayList<>();
        lstActores.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
        lstActores.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR));
        if (todos.equals(Constantes.AfirmacionNegacion.S)) {
            lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE));
        }

        // Obtenemos la lista de expedientes de solicitante y coordinadores (y
        // participantes si asi se indica)
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                idSolicitud, Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosEntrada
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                        lstActores, Operador.IN));
        
        //VMM MD-2349 26/01/26: mostrar solo los expdtes tramitados si la solicitud esta tramitada
        SolicitudDTO solicitudDTO = this.solicitudService.buscarId(idSolicitud);
        
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        
        if (!solicitudDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA))) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                    Operador.NOT_IN));
        }
        
        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort("idExpediente", true));

        List<ExpedienteDTO> lstExpedienteDTO = this.buscar(0, -1, filtrosEntrada, ordenamiento);

        List<ExpedienteDTO> lstParticipanteSolicitanteDTO = new ArrayList<>();

        // Iteramos la lista de expedientes solicitante y coordinadores (y
        // participantes si asi se indica)
        for (ExpedienteDTO expDTO : lstExpedienteDTO) {

            List<ConvocatoriaActDTO> lstActoresParticipante = new ArrayList<>();

            // Si es expediente coordinador recuperamos la lista de actores
            // participantes del coordinador
            if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesCoordinador(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }
            // Si es expediente solicitante recuperamos la lista de actores
            // participantes del solicitante
            // y si de aqui cuelga un coordinador que a su vez tiene un
            // participante??
            else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesSolicitante(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }

            // Si existen actores participante asociados
            if (!lstActoresParticipante.isEmpty()) {

                // Obtenemos la lista de idActores participantes
                List<Integer> lstIdActores = new ArrayList<>();
                for (ConvocatoriaActDTO convAct : lstActoresParticipante) {
                    lstIdActores.add(convAct.getIdActor());
                }

                // Obtenemos la lista de los expedientes participantes hijos
                filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                        idSolicitud, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosEntrada
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR,
                                lstIdActores, Operador.IN));
                
                //VMM MD-2349 26/01/26: mostrar solo los expdtes tramitados si la solicitud esta tramitada
                if (!solicitudDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA))) {  //solicitudes tramitadas
                    //quitamos los exps iniciados, propuestos, no aceptados o rechazados
                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                            Operador.NOT_IN));
                }

                List<ExpedienteDTO> lstExpPartic = this.buscar(0, -1, filtrosEntrada, ordenamiento);

                // Si existen expediente participantes hijos recuperados
                if (!lstExpPartic.isEmpty()) {

                    // Si son participantes de coordinador
                    if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                        // Agregamos a la lista de participantes (2do nivel)
                        expDTO.setLstExpedienteParticipante(lstExpPartic); // no
                                                                           // hace
                                                                           // nada
                                                                           // con
                                                                           // esto???

                        // Si son participantes de solicitante
                    } else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                        // Agregamos la lista de participantes (1er nivel)
                        lstParticipanteSolicitanteDTO.addAll(lstExpPartic);
                    }
                }
            }

        }

        // Si existen participantes de solicitante
        if (!lstParticipanteSolicitanteDTO.isEmpty()) {
            // Agregamos la lista de participantes a la lista principal
            lstExpedienteDTO.addAll(lstParticipanteSolicitanteDTO);
        }

        return lstExpedienteDTO;
    }

    /**
    *
    */
    @Override
    public ResponseDTO comprobarCoordinadores(Integer idExpediente) throws ServiceException {

        ResponseDTO responseDTO = new ResponseDTO();

        // Obtenemos el expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);

        // Obtenemos la solicitud
        SolicitudDTO solicitudDTO = SolicitudToDTO.INSTANCE
                .apply(this.solicitudDAO.find(expedienteDTO.getIdSolicitud()));

        // Obtenemos la lista de coordinadores de la solicitud
        List<ExpedienteDTO> lstExpedienteCoordinadoresDTO = this
                .buscarCoordinadoresPorSolicitud(expedienteDTO.getIdSolicitud(), null);

        // Comprobamos si hay algún Coordinador obligatorio en la convocatoria
        // para el solicitante
        List<ConvocatoriaActDTO> listConvocatoriaActDTO = this.convocatoriaActService
                .buscarTipoActorCoordinador(solicitudDTO.getConvocatoriaDTO().getIdConvocatoria());

        // Contamos cuantos son obligatorios. Contamos cuantos tienen el nmMin
        // diferente a 0
        Long coordinadoressObligatorios = listConvocatoriaActDTO.stream()
                .filter(x -> x.getNmMin() != 0)
                .filter(x -> Objects.nonNull(x.getIdActorSolicitante()) && x.getIdActorSolicitante()
                        .equals(expedienteDTO.getConvocatoriaActDTO().getIdActor()))
                .count();

        // Si no hay añadidos pero alguno es obligatorio, lanzamos el error
        if (Boolean.TRUE.equals(lstExpedienteCoordinadoresDTO.isEmpty())
                && coordinadoressObligatorios > 0) {
            responseDTO.setCodigo("101");
            responseDTO
                    .setDescripcionError(Constantes.Error.ERROR_MINIMO_COORDINADORES_CONVOCATORIA);
            responseDTO.setError(Boolean.TRUE);
            return responseDTO;
        }

        for (ExpedienteDTO expDTO : lstExpedienteCoordinadoresDTO) {

            // VMM 15/11/24 MD-1022: se puede sacar el actor directamente del
            // expdte sin necesidad de ir al form complementario
            // Obtenemos la convocatoriaAct del coordinador
            ConvocatoriaActDTO convocatoriaActDTO = expDTO.getConvocatoriaActDTO();

            // Obtenemos el número máximo y mínimo de coordinadores permitidos
            Integer nmMax = convocatoriaActDTO.getNmMax();
            Integer nmMin = convocatoriaActDTO.getNmMin();

            // Obtenemos el total de coordinadores de ese tipo en esa solicitud
            List<AtlasFilter> filtros = new ArrayList<>();

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitudDTO.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR,
                    expDTO.getConvocatoriaActDTO().getIdActor(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            // No se suma 1 como en el guardar ya que no tenemos que prever un
            // nuevo coordinadores
            Integer totalCoordinadores = this.count(filtros);

            if (totalCoordinadores > nmMax) {
                responseDTO.setCodigo("102");
                responseDTO.setDescripcionError(
                        Constantes.Error.ERROR_MAXIMO_COORDINADORES_CONVOCATORIA);
                responseDTO.setError(Boolean.TRUE);
                return responseDTO;
            }

            if (totalCoordinadores < nmMin) {
                responseDTO.setCodigo("103");
                responseDTO.setDescripcionError(
                        Constantes.Error.ERROR_MINIMO_COORDINADORES_CONVOCATORIA);
                responseDTO.setError(Boolean.TRUE);
                return responseDTO;
            }
        }

        return responseDTO;
    }

    /**
    *
    */
    @Override
    public List<ExpedienteDTO> buscarCoordinadoresPorSolicitud(Integer idSolicitud, Integer idActor)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (idActor != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_ID_ACTOR, idActor,
                    Operador.EQUALS));
        }

        return new ArrayList<>(Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));
    }

    /**
    *
    */
    @Override
    public List<ExpedienteDTO> buscarExpedientePorSolicitudActor(Integer idSolicitud,
            Integer idActor, String itTipo, Integer idExpedientePadre) throws ServiceException {

        List<ExpedienteDTO> listResponseOut = new ArrayList<>();
        List<ExpedienteDTO> listaExpePendienteSubsanar = new ArrayList<>();
        List<ExpedienteDTO> listaExpedientesResult = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        if ("C".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PC".equals(itTipo) || "PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        }

        if ("PC".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORCOORDINADOR,
                    idActor, Operador.EQUALS));
        }

        if ("PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            List<AtlasFilter> filtroParticipantesOrCoord = new ArrayList<>();
            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));

            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    filtroParticipantesOrCoord, Operador.OR));

        }

        if (idExpedientePadre != null) {
            List<AtlasFilter> filtroExpedientePadreOr = new ArrayList<>();
            filtroExpedientePadreOr.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE,
                    idExpedientePadre, Operador.EQUALS));

            filtroExpedientePadreOr.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE,
                    Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE,
                    filtroExpedientePadreOr, Operador.OR));
        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        listResponseOut = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

        for (ExpedienteDTO expDTO : listResponseOut) {

            ExpedienteContrato expContrat = this.getDao()
                    .findExpContratById(expDTO.getIdExpediente());

            if (expContrat != null && expContrat.getIdExpedienteContrato() != null) {
                ExpedienteContratoDTO expedienteContratoDTO = ExpedienteContratoToDTO.INSTANCE
                        .apply(expContrat);
                expDTO.setExpedienteContrato(expedienteContratoDTO);
                if (expedienteContratoDTO.getIdEstadoDTO().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION)
                        || expedienteContratoDTO.getIdEstadoDTO().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_SUBSANACION)) {

                    listaExpePendienteSubsanar.add(expDTO);
                } else {

                    listaExpedientesResult.add(expDTO);
                }

            } else {
                listaExpedientesResult.add(expDTO);
            }

        }

        if (listaExpePendienteSubsanar != null && !listaExpePendienteSubsanar.isEmpty()) {
            for (ExpedienteDTO expConta : listaExpePendienteSubsanar) {

                List<AtlasFilter> filtro = new ArrayList<>();

                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_CONTRATO_OBJ,
                        expConta.getExpedienteContrato().getIdExpedienteContrato(),
                        Operador.EQUALS));

                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_ESTADO,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO),
                        Operador.EQUALS));

                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_DOCUMENTOPTFR,
                        Operador.NOT_NULL));

                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteContratoDoc> listadoDatos = this.expedienteContratoDocService
                        .getDao().find(filtro, null);

                if (listadoDatos != null && !listadoDatos.isEmpty()) {

                    for (ExpedienteContratoDoc doc : listadoDatos) {
                        DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                                .find(doc.getExpedienteDoc().getIdDocumentoPtfr());
                        if (documentoPtfr.getCdEstadoNote() != null
                                && documentoPtfr.getCdEstadoNote().equals(Constantes.Note.CD_L)
                                && Utils.fechasEntreRango(documentoPtfr.getFcIniPlazoSubsanacion(),
                                        documentoPtfr.getFcFinPlazoSubsanacion())
                                        .equals(Boolean.TRUE)) {
                            listaExpedientesResult.add(expConta);
                        } else if (documentoPtfr.getCdEstadoNote() != null
                                && documentoPtfr.getCdEstadoNote().equals(Constantes.Note.CD_L)
                                && Utils.fechasEntreRango(new Date(),
                                        documentoPtfr.getFcIniPlazoSubsanacion())
                                        .equals(Boolean.TRUE)) {
                            // Aún no ha iniciado el plazo de subsanacion
                            expConta.getExpedienteContrato().setPendienteIncicioPlazo(Boolean.TRUE);
                            listaExpedientesResult.add(expConta);
                        }

                    }

                }
            }
        }

        return listaExpedientesResult;
    }

    /**
    *
    */
    @Override
    public List<ExpedienteDTO> buscarExpedientePorExpedientePadre(Integer idSolicitud,
            Integer idExpedientePadre) throws ServiceException {

        List<ExpedienteDTO> listResponseOut = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE, idExpedientePadre,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        listResponseOut = new ArrayList<>(Lists.transform(
                this.getDao().listExpedientePorExpedientePadre(idSolicitud, idExpedientePadre),
                ExpedienteToDTO.INSTANCE));

        for (ExpedienteDTO expDTO : listResponseOut) {

            ExpedienteContrato expContrat = this.getDao()
                    .findExpContratById(expDTO.getIdExpediente());

            if (expContrat != null && expContrat.getIdExpedienteContrato() != null) {
                ExpedienteContratoDTO expedienteContratoDTO = ExpedienteContratoToDTO.INSTANCE
                        .apply(expContrat);
                expDTO.setExpedienteContrato(expedienteContratoDTO);

            }
        }

        return listResponseOut;
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public void actualizarFechaContratoPorConvocatoria(Integer idConvocatoria, Date fcIniContrato,
            Date fcFinContrato) throws ServiceException {

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                Operador.NOT_IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitudes = this.solicitudService.find(filtros, null);

        for (Solicitud solicitud : lstSolicitudes) {

            lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

            filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados,
                    Operador.NOT_IN));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteDTO> lstExpedientes = this.buscar(0, -1, filtros, null);

            for (ExpedienteDTO dto : lstExpedientes) {

                if (Boolean.TRUE.equals(dto.getConvocatoriaActDTO().getItContrato())) {

                    Expediente expediente = this.find(dto.getIdExpediente());

                    expediente.setFcIniContrato(fcIniContrato);
                    expediente.setFcFinContrato(fcFinContrato);

                    this.update(expediente);
                    this.expedienteHistDAO.crearHistorico(expediente, null);
                }

            }

        }

    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarFechaContrato(Integer idExpediente, Date fcIniContrato,
            Date fcFinContrato) throws ServiceException {

        ExpedienteDTO expedienteDTO = new ExpedienteDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Expediente> lstExpedientes = this.find(filtros, null);

        for (Expediente expediente : lstExpedientes) {

            expediente.setFcIniContrato(fcIniContrato);
            expediente.setFcFinContrato(fcFinContrato);

            expedienteDTO = ExpedienteToDTO.INSTANCE.apply(this.update(expediente));
            
            this.expedienteHistDAO.crearHistorico(expediente, null);

        }

        return expedienteDTO;

    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocJusTec(Integer idExpedienteJusTec)
            throws ServiceException {

        List<ExpedienteDocDTO> results = new ArrayList<ExpedienteDocDTO>();

        try {

            List<AtlasFilter> lstFiltros = new ArrayList<>();

            lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                    idExpedienteJusTec, Operador.EQUALS));
            lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteJustecDocDTO> expedientesJustecDoc = expedienteJusTecService
                    .buscarDocs(0, -1, lstFiltros, null);

            if (!expedientesJustecDoc.isEmpty()) {
                for (ExpedienteJustecDocDTO expDoc : expedientesJustecDoc) {
                    results.add(expedienteDocService.buscarId(expDoc.getIdExpedienteDoc()));
                }

                for (ExpedienteDocDTO expDocDTO : results) {
                    if (expDocDTO.getCdFica() != null) {
                        // Recuperamos de FICA el nombre del fichero
                        FicaFichero ficaFichero = this.ficaService
                                .recuperarMetadatos(expDocDTO.getCdFica());
                        if (ficaFichero != null) {
                            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                            String rawFilename = ficaFichero.getNombreFichero();
                            String unicodeEscapedFilename = this.toUnicodeEscaped(rawFilename);
                            expDocDTO.setNombreFicheroFica(unicodeEscapedFilename);
                        }
                    }
                }

            }

        } catch (ServiceException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
        return results;

    }

    /**
    *
    */
    @Override
    public List<ExpedienteDocDTO> obtenerContratos(Integer idTramite, Integer idExpediente)
            throws ServiceException {
        List<ExpedienteDocDTO> listExpedienteDocDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("idExpediente", idExpediente, Operador.EQUALS));
        filtros.add(new AtlasFilter("tramite.idValor", idTramite, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        try {
            listExpedienteDocDTO = new ArrayList<>(Lists.transform(
                    this.expedienteDocDAO.find(filtros, null), ExpedienteDocToDTO.INSTANCE));
            listExpedienteDocDTO = this.rellenarDocumentosExpediente(listExpedienteDocDTO);
        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return listExpedienteDocDTO;
    }

    private ExpedienteDTO generarDocumentosAceptacion(ExpedienteDTO expedienteDTO) throws ServiceException {
        
        if (expedienteDTO.getImpresoAceptacionDTO() != null
                && !expedienteDTO.getImpresoAceptacionDTO().getLstBloqueDTO().isEmpty()) {
            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoAceptacionDTO().getLstBloqueDTO()) {

                bloqueDTO.getLstBloqueComponenteDTO()
                        .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                // Si es bloque es de tipo documento
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        // Si está dado de baja lo ignoramos
                        if (bloqueComponenteDTO.getFcBajaFila() != null) {
                            continue;
                        }

                        // Creamos un expedienteDoc de este bloque
                        ExpedienteDocDTO expedientedoc = this.crearExpedienteDocContrato(
                                expedienteDTO, bloqueComponenteDTO,utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA)
                                );
                        
                        expedienteDTO.getListaDocAceptacionAyuda().add(expedientedoc);
                    }
                }
            }

        } 
        return expedienteDTO;
    }
    /**
     * Método que genera los documentos del expediente
     *
     * @param expedienteDTO
     * @return expedienteDTO con los documentos
     * @throws ServiceException
     */
    @Override
    public ExpedienteDTO generarDocumentosExpedienteContrato(ExpedienteDTO expedienteDTO)
            throws ServiceException {
        // crear el expediente de contrato inicial
        ExpedienteContrato expedienteContrato = new ExpedienteContrato();
        expedienteContrato.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        expedienteContrato.setFcAltaFila(new Date());
        Interesado interesado = new Interesado();
        if (expedienteDTO.getInteresadoDTO() != null) {
            interesado.setIdInteresado(expedienteDTO.getInteresadoDTO().getIdInteresado());
        }

        expedienteContrato.setInteresado(interesado);
        expedienteContrato.setIdExpediente(expedienteDTO.getIdExpediente());
        CatalogosValores tipoContrato = new CatalogosValores();
        tipoContrato.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_INICIAL));
        expedienteContrato.setIdTipoContrato(tipoContrato);
        CatalogosValores estadoExpContrato = new CatalogosValores();
        estadoExpContrato.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SIN_SUBIR));
        expedienteContrato.setIdEstado(estadoExpContrato);
        expedienteContrato.setItIndefinido(Constantes.AfirmacionNegacion.N);

        ExpedienteContrato expedienteContratoNuevo = this.expedienteContratoService
                .insertOrUpdate(expedienteContrato);
        // Ordenamos por nmOrden
        expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()
                .sort(Comparator.comparing(BloqueDTO::getNmOrden));

        // Obtenemos los bloquesComponente que generen documentos
        if (expedienteDTO.getImpresoContratoDTO() != null
                && !expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO().isEmpty()) {
            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()) {

                bloqueDTO.getLstBloqueComponenteDTO()
                        .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                // Si es bloque es de tipo documento
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        // Si está dado de baja lo ignoramos
                        if (bloqueComponenteDTO.getFcBajaFila() != null) {
                            continue;
                        }

                        // Creamos un expedienteDoc de este bloque
                        ExpedienteDocDTO expedientedoc = this.crearExpedienteDocContrato(
                                expedienteDTO, bloqueComponenteDTO,
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
                        // Creamos el expedienteContratoDocs del contrato
                        // inicial
                        ExpedienteContratoDoc expedienteContratoDoc = new ExpedienteContratoDoc();
                        expedienteContratoDoc
                                .setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                        expedienteContratoDoc.setFcAltaFila(new Date());
                        expedienteContratoDoc.setExpedienteContrato(expedienteContratoNuevo);
                        ExpedienteDoc expDoc = new ExpedienteDoc();
                        expDoc.setIdExpedienteDoc(expedientedoc.getIdExpedienteDoc());
                        expedienteContratoDoc.setExpedienteDoc(expDoc);
                        this.expedienteContratoDocService.insert(expedienteContratoDoc);
                        expedienteDTO.getLstExpedienteDocDTO().add(expedientedoc);
                    }
                }
            }

        } else {
            List<BloqueDTO> bloques = new ArrayList<>();
            if (expedienteDTO.getIdExpedientePadre() != null) {
                ExpedienteDTO expedientePadre = this.buscarId(expedienteDTO.getIdExpedientePadre());
                if (expedientePadre.getImpresoContratoDTO() != null
                        && !expedientePadre.getImpresoContratoDTO().getLstBloqueDTO().isEmpty()) {
                    bloques = expedientePadre.getImpresoContratoDTO().getLstBloqueDTO();
                    expedienteDTO.setImpresoContratoDTO(expedientePadre.getImpresoContratoDTO());
                }

                for (BloqueDTO bloqueDTO : bloques) {

                    bloqueDTO.getLstBloqueComponenteDTO()
                            .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                    // Si es bloque es de tipo documento
                    if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                        for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                .getLstBloqueComponenteDTO()) {

                            // Si está dado de baja lo ignoramos
                            if (bloqueComponenteDTO.getFcBajaFila() != null) {
                                continue;
                            }

                            // Creamos un expedienteDoc de este bloque
                            ExpedienteDocDTO expedientedoc = this.crearExpedienteDocContrato(
                                    expedienteDTO, bloqueComponenteDTO,
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
                            // Creamos el expedienteContratoDocs del contrato
                            // inicial
                            ExpedienteContratoDoc expedienteContratoDoc = new ExpedienteContratoDoc();
                            expedienteContratoDoc
                                    .setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                            expedienteContratoDoc.setFcAltaFila(new Date());
                            expedienteContratoDoc.setExpedienteContrato(expedienteContratoNuevo);
                            ExpedienteDoc expDoc = new ExpedienteDoc();
                            expDoc.setIdExpedienteDoc(expedientedoc.getIdExpedienteDoc());
                            expedienteContratoDoc.setExpedienteDoc(expDoc);
                            this.expedienteContratoDocService.insert(expedienteContratoDoc);
                            expedienteDTO.getLstExpedienteDocDTO().add(expedientedoc);
                        }
                    }
                }
            }
        }

        return expedienteDTO;
    }

    /**
     * Método para crear el registro expedienteDoc a partir del expediente y
     * bloque componente. Un documento por bloque componente
     *
     * @param expedienteDTO
     * @param bloqueComponenteDTO
     * @return ExpedienteDocDTO
     * @throws ServiceException
     */
    public ExpedienteDocDTO crearExpedienteDocContrato(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO, Integer tramite) throws ServiceException {
        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
        CatalogosValores estado = catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
        expedienteDoc.setEstado(estado);
        
        //VMM 17/07/25 MD-1778: error al setear catalogovalores sin generarlo antes (object references an unsaved transient instance)
        if (bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO() != null &&
                bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
            CatalogosValores tipoDoc = catalogosValoresService.find(bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor());
            expedienteDoc.setTipoDocumento(tipoDoc);
        }
        
        Expediente expAux = new Expediente();
        expAux.setIdExpediente(expedienteDTO.getIdExpediente());
        expedienteDoc.setExpediente(expAux);
        expedienteDoc
                .setBloqueComponente(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));

        // Agregamos los indicadores de tramite y si es o no un adicional
        expedienteDoc.setTramite(new CatalogosValores(tramite));
        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);

        // Se usa el cdUsuAlta del expediente
        expedienteDoc.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        expedienteDoc.setFcAltaFila(new Date());
        return ExpedienteDocToDTO.INSTANCE.apply(this.expedienteDocDAO.insert(expedienteDoc));
    }

    @Override
    public List<ExpedienteDocDTO> obtenerExpedienteContratos(Integer idExpediente)
            throws ServiceException {

        List<ExpedienteDocDTO> listExpedienteDocDTO = new ArrayList<>();
        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = new ArrayList<>();

        try {
            lstExpedienteDocDTOAux = this.expedienteDocDAO.listExpedienteDocContratos(idExpediente);
            listExpedienteDocDTO = this
                    .rellenarDocumentosExpedienteContrato(lstExpedienteDocDTOAux);
        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return listExpedienteDocDTO;
    }

    private List<ExpedienteDocDTO> rellenarDocumentosExpedienteContrato(
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux) {
        List<ExpedienteDocDTO> lstExpedienteDocFiltrada = new ArrayList<>();

        // Recorremos la lista de documentos del expediente
        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTOAux) {
            // Si está dado de baja lo ignoramos
            if (expedienteDocDTO.getFcBajaFila() != null) {
                continue;
            }

            // Recuperamos el nombre de FICA
            try {
                expedienteDocDTO.setNombreFicheroFica(
                        this.recuperarNombreFicheroFica(expedienteDocDTO.getCdFica()));
            } catch (ServiceException e) {
                log.error(Constantes.Error.ERROR_DATA, e);
                
            }

            // No está de baja lo podemos devolver
            lstExpedienteDocFiltrada.add(expedienteDocDTO);
        }

        return lstExpedienteDocFiltrada;
    }

    @Override
    public ExpedienteDTO buscarIdExp(Integer idExpediente,
            List<ExpedienteDocDTO> listExpedienteDocDTOAux) throws ServiceException {

        ExpedienteDTO dto = ExpedienteToDTO.INSTANCE.apply(this.find(idExpediente));

        // Recuperamos los datos del expediente.
        dto = this.rellenarDatosExpediente(dto);

        if (dto.getLstExpedienteDocDTO().isEmpty()) {
            dto.setLstExpedienteDocDTO(
                    this.expedienteDocService.buscarPorExpediente(dto.getIdExpediente()));
        }

        // Rellenamos los documentos del expediente
        dto.setLstExpedienteDocDTO(
                this.rellenarDocumentosExpedienteContrato(listExpedienteDocDTOAux));

        return dto;
    }

    @Override
    public ExpedienteDTO buscarIdExpediente(Integer idExpediente) throws ServiceException {

        ExpedienteDTO dto = null;
        try {
            dto = ExpedienteToDTO.INSTANCE.apply(this.find(idExpediente));
        } catch (ServiceException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return dto;
    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocPendienteSubsanarJusTec(Integer idExpedienteJusTec)
            throws ServiceException {

        List<ExpedienteDocDTO> results = new ArrayList<ExpedienteDocDTO>();

        try {

            List<AtlasFilter> lstFiltros = new ArrayList<>();

            lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                    idExpedienteJusTec, Operador.EQUALS));

            List<ExpedienteJustecDocDTO> expedientesJustecDoc = expedienteJusTecService
                    .buscarDocs(0, -1, lstFiltros, null);

            if (!expedientesJustecDoc.isEmpty()) {
                for (ExpedienteJustecDocDTO expDoc : expedientesJustecDoc) {
                    ExpedienteDocDTO doc = expedienteDocService
                            .buscarId(expDoc.getIdExpedienteDoc());
                    if (doc.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO)
                            || doc.getEstadoDTO().getNmConstante().equals(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION)) {
                        results.add(doc);
                    }

                }

                for (ExpedienteDocDTO expDocDTO : results) {
                    if (expDocDTO.getCdFica() != null) {
                        // Recuperamos de FICA el nombre del fichero
                        FicaFichero ficaFichero = this.ficaService
                                .recuperarMetadatos(expDocDTO.getCdFica());
                        if (ficaFichero != null) {
                            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                            String rawFilename = ficaFichero.getNombreFichero();
                            String unicodeEscapedFilename = this.toUnicodeEscaped(rawFilename);
                            expDocDTO.setNombreFicheroFica(unicodeEscapedFilename);
                        }
                    }
                }
            }

        } catch (ServiceException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
        return results;

    }

    @Override
    public void actualizarExpedienteJustec(ExpedienteJusTecDTO expedienteJustecDTO) {

        ExpedienteJusTec expeJustec = expedienteJusTecDAO
                .find(expedienteJustecDTO.getIdExpedienteJusTec());
        expeJustec.setTlObservacionDelCiudadano(expedienteJustecDTO.getTlObservacionDelCiudadano());
        expedienteJusTecDAO.update(expeJustec);
        
    }

    
    @Override
    public ExpedienteDocDTO crearExpedienteDocJustec(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
        CatalogosValores estado = catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
        expedienteDoc.setEstado(estado);

        //VMM 17/07/25 MD-1778: error al setear catalogovalores sin generarlo antes (object references an unsaved transient instance)
        if (bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO() != null &&
                bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
            CatalogosValores tipoDoc = catalogosValoresService.find(bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor());
            expedienteDoc.setTipoDocumento(tipoDoc);
        }

        Expediente expAux = new Expediente();
        expAux.setIdExpediente(expedienteDTO.getIdExpediente());
        expedienteDoc.setExpediente(expAux);
        expedienteDoc
                .setBloqueComponente(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));

        // VMM 31/07/24 MD-505: se cambia por el tramite de presentacion de la
        // JCT (verificar que esto es correcto)
        // Agregamos los indicadores de tramite y si es o no un adicional
        CatalogosValores tramite = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(
                Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA));
        expedienteDoc.setTramite(tramite);
        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);

        // Se usa el cdUsuAlta del expediente
        expedienteDoc.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        expedienteDoc.setFcAltaFila(new Date());
        return ExpedienteDocToDTO.INSTANCE.apply(this.expedienteDocDAO.insert(expedienteDoc));
    }
    
    
    @Override
    public ExpedienteDocDTO crearExpedienteDocJusEco(ExpedienteDTO expedienteDTO,
            BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
        
        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
        CatalogosValores estado = catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
        expedienteDoc.setEstado(estado);

        if (bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO() != null &&
                bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
            CatalogosValores tipoDoc = catalogosValoresService.find(bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor());
            expedienteDoc.setTipoDocumento(tipoDoc);
        }

        Expediente expAux = new Expediente();
        expAux.setIdExpediente(expedienteDTO.getIdExpediente());
        expedienteDoc.setExpediente(expAux);
        expedienteDoc.setBloqueComponente(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));

        // VMM 31/07/24 MD-505: se cambia por el tramite de presentacion de la
        // JCT (verificar que esto es correcto)
        // Agregamos los indicadores de tramite y si es o no un adicional
        CatalogosValores tramite = this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(
                Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA));
        expedienteDoc.setTramite(tramite);
        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);

        // Se usa el cdUsuAlta del expediente
        expedienteDoc.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
        expedienteDoc.setFcAltaFila(new Date());
        return ExpedienteDocToDTO.INSTANCE.apply(this.expedienteDocDAO.insert(expedienteDoc));
    }

    @Override
    public void generarDatosImpresos(ExpedienteDTO expedienteDTO, ImpresoDTO impreso,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException {
        // Obtenemos los bloquesComponente que se guarden en la tabla dato
        if (impreso != null && impreso.getIdImpreso() != null) {
            for (BloqueDTO bloqueDTO : impreso.getLstBloqueDTO()) {
                
                // Si está dado de baja lo ignoramos
                if (bloqueDTO.getFcBajaFila() != null) {
                    continue;
                }

                // Si es formulario, listado, tabla, presupuesto presentado,
                // ayuda
                // solicitada o texto, se va a poder guardar
                // en la tabla dato
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoSusti())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoNotificacion())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoPersonaSolicitante())
                        || Boolean.TRUE.equals(bloqueDTO.getEsCuentaBancaria())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoDiscapacidad())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoIntensidadAyuda())) {

                    bloqueDTO.getLstBloqueComponenteDTO()
                            .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        // Si está dado de baja lo ignoramos
                        if (bloqueComponenteDTO.getFcBajaFila() != null) {
                            continue;
                        }

                        // Si el componente no es uno de los válidos, lo
                        // ignoramos
                        if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                                && Boolean.FALSE.equals(bloqueDTO.getEsTipoAyudaSolicitada())) {
                            if (Boolean.FALSE
                                    .equals(this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                                continue;
                            }
                        }

                        if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                                && Boolean.FALSE.equals(bloqueDTO.getEsTipoAyudaSolicitada())) {
                            if (Boolean.FALSE
                                    .equals(this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                                continue;
                            }
                        }

                        // Obtenemos cuantas filas tiene el bloque tabla
                        Integer nmFilasTabla = this.datoService.obtenerFilasTabla(bloqueDTO);
                        // Obtenemos cuantas filas hay en el bloque
                        Integer nmFilas = this.datoService.obtenerNmRegistrosBloque(bloqueDTO, nmFilasTabla);
                        List<DatoDTO> lstDatos = new ArrayList<>();

                        if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoNotificacion())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPersonaSolicitante()) 
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoDiscapacidad())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoIntensidadAyuda())
                                )
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItRelacion())) {
                            // Si es formulario y el componente no está
                            // relacionado

                            lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJustec));

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                                || Boolean.TRUE
                                        .equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                                || Boolean.TRUE
                                        .equals(bloqueDTO.getEsTipoFormularioContratoSusti())) {
                            lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJustec));

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoListado() || Boolean.TRUE.equals(bloqueDTO.getEsCuentaBancaria()))) {
                            // Es un listado, creamos tantos datos como filas
                            // tenga el listado

                            for (Integer i = 0; i < nmFilas; i++) {
                                lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO,
                                        i + 1, expedienteJustec));
                            }

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
                            // Es una tabla creamos datos sólo de las filas

                            /*
                             * for (Integer i = 0; i < nmFilas; i++) {
                             * lstDatos.add(this.crearDato(expedienteDTO,
                             * bloqueComponenteDTO, i + 1)); }
                             */

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) {
                            // Es de tipo texto
                            lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJustec));

                        } else if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable()))
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

                            for (Integer i = 0; i < nmFilas; i++) {
                                lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO,
                                        i + 1, expedienteJustec));
                            }
                            
                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                                 
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {

                            for (Integer i = 0; i < nmFilas; i++) {
                                lstDatos.add(this.crearDato(expedienteDTO, bloqueComponenteDTO,
                                        i + 1, expedienteJustec));
                            }    

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {

                            /*
                             * for (Integer i = 0; i < nmFilas; i++) {
                             * lstDatos.add(this.crearDato(expedienteDTO,
                             * bloqueComponenteDTO, i + 1)); }
                             */

                        }

                        if (Boolean.TRUE.equals(lstDatos.isEmpty())) {
                            // Ponemos a null si está en blanco para la parte
                            // MOVA
                            lstDatos = null;
                        }

                        // Guardamos los datos en el bloqueComponente
                        bloqueComponenteDTO.setDatos(lstDatos);
                    }
                }
            }
        }
    }
    
    
    @Override
    public void generarDatosImpresosEco(ExpedienteDTO expedienteDTO, ImpresoDTO impreso,
            ExpedienteJusEcoDTO expedienteJusEco) throws ServiceException {
        // Obtenemos los bloquesComponente que se guarden en la tabla dato
        if (impreso != null && impreso.getIdImpreso() != null) {
            for (BloqueDTO bloqueDTO : impreso.getLstBloqueDTO()) {

                // Si está dado de baja lo ignoramos
                if (bloqueDTO.getFcBajaFila() != null) {
                    continue;
                }
                
                // Si es formulario, listado, tabla, presupuesto presentado,
                // ayuda solicitada o texto, se va a poder guardar
                // en la tabla dato
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContratoSusti())
                        || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())) {

                    bloqueDTO.getLstBloqueComponenteDTO()
                            .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        // Si está dado de baja lo ignoramos
                        if (bloqueComponenteDTO.getFcBajaFila() != null) {
                            continue;
                        }

                        // Si el componente no es uno de los válidos, lo
                        // ignoramos
                        if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                                && Boolean.FALSE.equals(bloqueDTO.getEsTipoAyudaSolicitada())) {
                            if (Boolean.FALSE
                                    .equals(this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                                continue;
                            }
                        }

                        if (Boolean.FALSE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable())
                                && Boolean.FALSE.equals(bloqueDTO.getEsTipoAyudaSolicitada())) {
                            if (Boolean.FALSE
                                    .equals(this.devolverComponentePorTipo(bloqueComponenteDTO))) {
                                continue;
                            }
                        }

                        // Obtenemos cuantas filas tiene el bloque tabla
                        Integer nmFilasTabla = this.datoService.obtenerFilasTabla(bloqueDTO);
                        // Obtenemos cuantas filas hay en el bloque
                        Integer nmFilas = this.datoService.obtenerNmRegistrosBloque(bloqueDTO, nmFilasTabla);
                        List<DatoDTO> lstDatos = new ArrayList<>();

                        if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                                && Boolean.FALSE.equals(bloqueComponenteDTO.getItRelacion())) {
                            // Si es formulario y el componente no está
                            // relacionado

                            lstDatos.add(this.crearDatoEco(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJusEco));

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoFormularioContrato())
                                || Boolean.TRUE
                                        .equals(bloqueDTO.getEsTipoFormularioContratoRenuncia())
                                || Boolean.TRUE
                                        .equals(bloqueDTO.getEsTipoFormularioContratoSusti())) {
                            lstDatos.add(this.crearDatoEco(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJusEco));

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())) {
                            // Es un listado, creamos tantos datos como filas
                            // tenga el listado

                            for (Integer i = 0; i < nmFilas; i++) {
                                lstDatos.add(this.crearDatoEco(expedienteDTO, bloqueComponenteDTO,
                                        i + 1, expedienteJusEco));
                            }

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
                            // Es una tabla creamos datos sólo de las filas

                            /*
                             * for (Integer i = 0; i < nmFilas; i++) {
                             * lstDatos.add(this.crearDato(expedienteDTO,
                             * bloqueComponenteDTO, i + 1)); }
                             */

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) {
                            // Es de tipo texto
                            lstDatos.add(this.crearDatoEco(expedienteDTO, bloqueComponenteDTO, 1,
                                    expedienteJusEco));

                        } else if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoAyudaSolicitada())
                                || Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoFinanciable()))
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {

                            for (Integer i = 0; i < nmFilas; i++) {
                                lstDatos.add(this.crearDatoEco(expedienteDTO, bloqueComponenteDTO,
                                        i + 1, expedienteJusEco));
                            }

                        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoActuaciones())
                                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {

                            /*
                             * for (Integer i = 0; i < nmFilas; i++) {
                             * lstDatos.add(this.crearDato(expedienteDTO,
                             * bloqueComponenteDTO, i + 1)); }
                             */

                        }

                        if (Boolean.TRUE.equals(lstDatos.isEmpty())) {
                            // Ponemos a null si está en blanco para la parte
                            // MOVA
                            lstDatos = null;
                        }

                        // Guardamos los datos en el bloqueComponente
                        bloqueComponenteDTO.setDatos(lstDatos);
                    }
                }
            }
        }
    }
    

    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    private ExpedienteDTO actualizarEstadoInternoContrato(Expediente expediente,
            Integer idEstadoNuevo, Boolean actualizaEstadoExpediente) throws ServiceException {

        Boolean esCambioEstado = Boolean.FALSE;

        if (expediente.getEstadoRevContrato() == null
                || !expediente.getEstadoRevContrato().getIdValor().equals(idEstadoNuevo)) {
            esCambioEstado = Boolean.TRUE;
        }

        if (Boolean.TRUE.equals(esCambioEstado)) {
            CatalogosValoresDTO estadoRevInternoDTO = this.catalogosValoresService
                    .buscarId(idEstadoNuevo);

            expediente.setEstadoRevContrato(
                    CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO));

            if (actualizaEstadoExpediente.equals(Boolean.TRUE)) {

                expediente.setEstado(CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO));
            }

            expediente = this.getDao().merge(expediente);
            this.expedienteHistDAO.crearHistorico(expediente, null);
        }

        return ExpedienteToDTO.INSTANCE.apply(expediente);

    }

    /**
    *
    */
    @Override
    public List<ExpedienteDTO> obtenerExpedientesPorIdExpediente(Integer idExpediente)
            throws ServiceException {

        List<ExpedienteDTO> listExpedienteDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        try {
            listExpedienteDTO = new ArrayList<>(
                    Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

            for (ExpedienteDTO dto : listExpedienteDTO) {
                // Rellenamos los datos del expediente
                // Los necesitamos para el impreso del zip (si no está
                // presentado en EREG)
                dto = this.rellenarDatosExpediente(dto);

                // Rellenamos los documentos del expediente
                // Los necesitamos para los zip
                dto.setLstExpedienteDocDTO(
                        this.rellenarDocumentosExpediente(dto.getLstExpedienteDocDTO()));
            }

        } catch (DataAccessException e) {
            log.error(Constantes.Error.ERROR_DATA, e);
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

        return listExpedienteDTO;
    }

    @Override
    public List<ExpedienteDTO> buscarParticipantesPorExpedienteAgrupador(Integer idExpediente,
            Integer idActor) {

        List<Expediente[]> result = this.expedienteDocDAO
                .listExpedientePaticipantesContratosPorIdActor(idActor);

        Iterator<?> it = result.iterator();

        List<ExpedienteDTO> lstExpedienteDTO = new ArrayList<>();
        while (it.hasNext()) {
            Expediente expediente = (Expediente) it.next();
            ExpedienteDTO expedienteParticipante = ExpedienteToDTO.INSTANCE.apply(expediente);
            lstExpedienteDTO.add(expedienteParticipante);

        }

        for (ExpedienteDTO expDTO : lstExpedienteDTO) {
            List<ExpedienteContrato> listaExpedienteContrato = this.expedienteContratoDAO
                    .findByIdExpediente(expDTO.getIdExpediente());
            for (ExpedienteContrato contrato : listaExpedienteContrato) {
                if ((contrato.getItIndefinido() != null
                        && contrato.getItIndefinido().equals(Constantes.AfirmacionNegacion.S))
                        || (contrato.getFcFinContrato() != null
                                && contrato.getFcFinContrato().after(new Date()))) {
                    expDTO.setExpedienteContrato(ExpedienteContratoToDTO.INSTANCE.apply(contrato));
                }
            }
        }

        return lstExpedienteDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO finalizarSubsanacionJustec(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            ExpedienteJusTecDTO expedienteJustecDTO, Boolean esAdministrativo, String cdNumIden,
            String dniLogin, String cdNumIdenFuncional) throws ServiceException {

        // validamos datos de entrada
        this.validarFinalizarSubsanacionJustec(lstExpedienteDocDTO, cdNumIden, esAdministrativo);
        this.finalizarSubsanacionJustecAdministrativo(lstExpedienteDocDTO, expedienteJustecDTO,
                cdNumIden, dniLogin, cdNumIdenFuncional);

        // retornamos respuesta ereg
        return this.prepararRespuestaEregJustec(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(),
                lstExpedienteDocDTO, expedienteJustecDTO.getIdExpedienteJusTec());

    }

    private void finalizarSubsanacionJustecAdministrativo(
            List<ExpedienteDocDTO> lstExpedienteDocDTO, ExpedienteJusTecDTO expedienteJustecDTO,
            String cdNumIden, String dniLogin, String cdNumIdenFuncional) throws ServiceException {

        // Guardamos las observaciones de los documentos antes de finalizar
        // la subsanación administrativa
        for (ExpedienteDocDTO expedienteDocDTO : lstExpedienteDocDTO) {
            if (Objects.nonNull(expedienteDocDTO.getIdExpedienteDoc())) {
                ExpedienteDoc expedienteDoc = this.expedienteDocDAO
                        .find(expedienteDocDTO.getIdExpedienteDoc());
                expedienteDoc.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
                expedienteDoc.setCdUsuModif(cdNumIden);
                expedienteDoc.setFcModifFila(new Date());
                this.expedienteDocDAO.merge(expedienteDoc);
                this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdNumIden);
            }
        }

        // Todos tienen el mismo expediente
        ExpedienteDTO expedienteDTO = this
                .buscarId(lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente());

        expedienteDTO.setLstExpedienteDocDTO(lstExpedienteDocDTO);

        List<Integer> lstPeticionesPortafirmas = expedienteDTO.getLstExpedienteDocDTO().stream()
                .map(ExpedienteDocDTO::getIdDocumentoPtfr).filter(Objects::nonNull)
                .collect(Collectors.toList());

        lstPeticionesPortafirmas = lstPeticionesPortafirmas.stream().distinct()
                .collect(Collectors.toList());

        for (Integer idDocumentoPtfr : lstPeticionesPortafirmas) {

            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO.find(idDocumentoPtfr);

            if (Objects.isNull(documentoPtfr)) {
                throw new ServiceException("No se encontró datos de envío a Portafirmas");
            }

            // recuperar documento de notificación firmado de PTFR
            PtfrRecuperaDocFirmadoDTO docFirmadoDTO = this.ptfrAdmeService
                    .recuperarDocumentoFirmado(documentoPtfr.getDsReferenciaPtfr(),
                            documentoPtfr.getCdArchivoPtfr().toString());

            if (Objects.isNull(docFirmadoDTO) || Objects.isNull(docFirmadoDTO.getPdfB64())) {
                throw new ServiceException(
                        "No se pudo obtener el documento firmado de Portafirmas");
            }

            byte[] ficheroRespuesta = Base64.decode(docFirmadoDTO.getPdfB64());

            // VMM MD-913 30/10/24: obtenemos el Ereg del ExpedienteJustec y no
            // el del propio expediente
            // tenemos que crear el EREG para la subsanacion igual que se hace
            // en la presentacion de la Justec
            // Generamos el objeto de EREG de la subsanacion
            this.pdfService.generaFirmaReciboSubsanacionJustec(expedienteDTO, expedienteJustecDTO,
                    null, lstExpedienteDocDTO);

            // esto es lo que hacia antes cogia el EREG de la presentacion
            // justec
            this.eregAdmeService.anexarFicheroAnexo(expedienteJustecDTO.getCdEregSubsana(),
                    ficheroRespuesta, Constantes.Ereg.LISTADO_DOC_SUBSANADOS);
            
            String cifEntidadLogin= null;
            if(!cdNumIdenFuncional.equalsIgnoreCase(dniLogin)) {
                cifEntidadLogin=cdNumIdenFuncional;
            }

            this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA),
                    expedienteDTO, dniLogin, cifEntidadLogin);

            this.finalizarSubsanacionDocumentosExpediente(expedienteDTO.getIdExpediente(),
                    documentoPtfr.getIdDocumentoPtfr(),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION),
                    Boolean.FALSE, Boolean.TRUE, expedienteJustecDTO,expedienteJustecDTO.getCdEregSubsana(),expedienteJustecDTO.getFcFirmaSubsana());

        }

        // Actualizar expedienteJustec
        this.expedienteJusTecService.update(expedienteJustecDTO);

    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO finalizarSubsanacionContrato(List<ExpedienteDocDTO> lstExpedienteDocDTO,
            Boolean esAdministrativo, String cdNumIden, String dniUsuario,
            String cdNumIdenFuncional) throws ServiceException {

        // validamos datos de entrada
        this.validarFinalizarSubsanacion(lstExpedienteDocDTO, cdNumIden, esAdministrativo);

        // llamamos al proceso correspondiente
        if (Boolean.TRUE.equals(esAdministrativo)) {
            this.finalizarSubsanacionAdministrativo(lstExpedienteDocDTO, cdNumIden, Boolean.TRUE,
                    Boolean.FALSE,dniUsuario, cdNumIdenFuncional);

        }

        // retornamos respuesta ereg
        return this.prepararRespuestaContratoEreg(
                lstExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente(),
                lstExpedienteDocDTO);
    }

    // Metodo que prepara el EREG de salida para la Subsanacion de la Justec
    private RegistroEregDTO prepararRespuestaEregJustec(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocumentosDTO, Integer idExpedienteJustec)
            throws ServiceException {

        // Recuperamos los datos actualizados del expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);

        // VMM MD-913 28/10/24: recuperamos el Ereg del ExpedienteJustec en vez
        // de en el propio expediente
        ExpedienteJusTecDTO expdteJusTecDTO = this.expedienteJusTecService
                .buscarPorIdExpedienteJustec(idExpedienteJustec);

        // Generamos el listado de anexos en ereg
        String cdListadoDocEreg = this.pdfService.generarFirmarListadoJusTecAnexos(expedienteDTO,
                expdteJusTecDTO, null, listaDocumentosDTO, Boolean.TRUE);
        if (StringUtils.isBlank(cdListadoDocEreg)) {
            throw new ServiceException(
                    "No se pudo finalizar el proceso. Contacte con el administrador.");
        }

        // Recuperamos el registro de la lista de anexos nueva
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdListadoDocEreg,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteDocDTO> lstExpedienteDoc = this.expedienteDocService.buscar(0, -1, filtros,
                null);

        // Recuperamos el identificador del registro, necesario para la opción
        // descargar la alegacion desde formulario de respuesta EREG
        Integer idExpedienteDoc = lstExpedienteDoc.get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdListadoDocEreg);
        registroEregDTO.setCdEreg(Utils.formatearNmEreg(expdteJusTecDTO.getCdEregSubsana()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSolicitud(false);
        registroEregDTO.setEsDescargaZipAlegacion(false);
        registroEregDTO.setEsDescargaZipSubsanacion(true);

        return registroEregDTO;

    }

    private RegistroEregDTO prepararRespuestaContratoEreg(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocumentosDTO) throws ServiceException {

        // VMM MD-913 28/10/24: recuperamos todos los datos del expdte para
        // tener el Ereg de presentacion del contrato
        // Recuperamos los datos actualizados del expediente
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.TRUE);

        // Generamos el listado de anexos en ereg
        String cdListadoDocEreg = this.pdfService.generarFirmarListadoContratoAnexos(expedienteDTO,
                null, listaDocumentosDTO);

        if (StringUtils.isBlank(cdListadoDocEreg)) {
            throw new ServiceException(
                    "No se pudo finalizar el proceso. Contacte con el administrador.");
        }

        // Recuperamos el registro de la lista de anexos nueva
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdListadoDocEreg,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ExpedienteDocDTO> lstExpedienteDoc = this.expedienteDocService.buscar(0, -1, filtros,
                null);

        // Recuperamos el identificador del registro, necesario para la opción
        // descargar la alegacion desde formulario de respuesta EREG
        Integer idExpedienteDoc = lstExpedienteDoc.get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdListadoDocEreg);
        registroEregDTO.setCdEreg(
                Utils.formatearNmEreg(expedienteDTO.getExpedienteContrato().getCdEregPresent()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSolicitud(Boolean.FALSE);
        registroEregDTO.setEsDescargaZipAlegacion(Boolean.FALSE);
        registroEregDTO.setEsDescargaZipSubsanacion(Boolean.TRUE);

        return registroEregDTO;

    }

    @Override
    public List<ExpedienteDTO> buscarExpedientesPorPadreYPorIdActor(Integer idExpediente,
            Integer idActor) {

        List<Expediente[]> result = this.expedienteDAO
                .listExpedientesContratoPorPadre(idExpediente);

        Iterator<?> it = result.iterator();

        List<ExpedienteDTO> lstExpedienteDTO = new ArrayList<>();
        while (it.hasNext()) {
            Expediente expediente = (Expediente) it.next();
            ExpedienteDTO expedienteParticipante = ExpedienteToDTO.INSTANCE.apply(expediente);
            lstExpedienteDTO.add(expedienteParticipante);

        }

        for (ExpedienteDTO expDTO : lstExpedienteDTO) {
            List<ExpedienteContrato> listaExpedienteContrato = this.expedienteContratoDAO
                    .findByIdExpediente(expDTO.getIdExpediente());
            for (ExpedienteContrato contrato : listaExpedienteContrato) {
                if ((contrato.getItIndefinido() != null
                        && contrato.getItIndefinido().equals(Constantes.AfirmacionNegacion.S))
                        || (contrato.getFcFinContrato() != null
                                && contrato.getFcFinContrato().after(new Date()))) {
                    expDTO.setExpedienteContrato(ExpedienteContratoToDTO.INSTANCE.apply(contrato));
                }
            }
        }

        return lstExpedienteDTO;
    }

    @Override
    public List<CatalogosValoresDTO> buscarPorCatalogoId(Integer idCatalogo)
            throws ServiceException {
        return catalogosValoresService.buscarPorCatalogo(idCatalogo);
    }

    /**
     * 
     * @param actual
     * @param anterior
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO revertirEstadosExpediente(Integer idExpediente,
            ExpedienteHistDTO anterior, Boolean provisional) {
        Expediente expediente = null;
        try {
            if (idExpediente != null && anterior != null) {
                expediente = this.find(idExpediente);
                if (expediente != null) {
                    expediente.setEstado(this.catalogosValoresService
                            .find(anterior.getEstadoDTO().getIdValor()));
                    if (anterior.getEstadoBeneficiarioDTO() != null
                            && anterior.getEstadoBeneficiarioDTO().getIdValor() != null) {
                        expediente.setEstadoBenef(this.catalogosValoresService
                                .find(anterior.getEstadoBeneficiarioDTO().getIdValor()));
                    }

                    if(provisional != null && provisional.equals(Boolean.TRUE))  {
                        expediente.setEstadoProvisional(null);                      
                    }else if (anterior.getEstadoProvisionalDTO() != null
                            && anterior.getEstadoProvisionalDTO().getIdValor() != null) {
                            expediente.setEstadoProvisional(this.catalogosValoresService
                                    .find(anterior.getEstadoProvisionalDTO().getIdValor()));   
                    }
                    expediente = this.getDao().merge(expediente);
                    this.expedienteHistDAO.crearHistorico(expediente, null);        
                    
                } else {
                    log.error("Expediente no encontrado, con el idExpediente: " + idExpediente);
                }
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
        return ExpedienteToDTO.INSTANCE.apply(expediente);
    }


    private void revertirOtrosImpresosExpediente(Integer idExpediente) {
        try {
            if (idExpediente != null) {   
                //VMM 27/06/25 MD-1635: revertimos o borramos fisicamente los datos volcados de los formularios extra
                this.revertirDato(idExpediente);
//                THIS.REVERTIREXPJUSTEC(IDEXPEDIENTE);
//                THIS.REVERTIREXPJUSECO(IDEXPEDIENTE);
//                THIS.REVERTIREXPCONTRATOS(IDEXPEDIENTE);
                this.revertirExpDocAcepracion(idExpediente);
            }
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }
    }
    

    private void revertirDato(Integer idExpediente) throws ServiceException {
        
        //buscar los bloques componentes de los impresos extra del expediente en cuestion
        List<Integer> lstTipoImpreso = new ArrayList<>();
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACTA_GESTION_ORDINARIA));
//        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACTA_PRESUPUESTARIA));
        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));
        
        List<DatoDTO> lstDatoDTO = this.buscarPorIdExpTipoImpreso(idExpediente, lstTipoImpreso);
        
        if (lstDatoDTO != null && !lstDatoDTO.isEmpty()) {
            for (DatoDTO datoDTO: lstDatoDTO) {
                this.datoService.delete(datoDTO.getIdDato());
            }
        }
    } 
    
    
    private void revertirExpJusTec(Integer idExpediente) throws ServiceException {
        //hacemos borrado fisico para que no haya tantos datos
        List<ExpedienteJusTecDTO> lstExpJustecDTO = this.expedienteJusTecService.buscarPorExpediente(idExpediente);
        if (lstExpJustecDTO != null && !lstExpJustecDTO.isEmpty()) {
            for (ExpedienteJusTecDTO expJusDTO: lstExpJustecDTO) {
                               
                //buscamos los docs de cada exp justec
                List<AtlasFilter> filtro = new ArrayList<>();
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                        expJusDTO.getIdExpedienteJusTec(), Operador.EQUALS));
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteJustecDocDTO> lstExpJustecDocDTO = this.expedienteJusTecService.buscarDocs(0, -1, filtro, null);
                if (lstExpJustecDocDTO != null && !lstExpJustecDocDTO.isEmpty()) {
                    for (ExpedienteJustecDocDTO expJusDocDTO: lstExpJustecDocDTO) {
                        
                        //borramos expJustecDoc
                        this.expedienteJusTecDocService.delete(expJusDocDTO.getIdExpedienteJustecDoc());
                        
                        //buscamos los historicos de expedientes doc asociados para darlos de baja antes
                        List<AtlasFilter> filtroH = new ArrayList<>();
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                expJusDocDTO.getIdExpedienteDoc(), Operador.EQUALS));
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        
                        List<ExpedienteDocHistDTO> lstExpDocHistDTO = this.expedienteDocHistService.buscar(0, -1, filtroH, null);
                        
                        if (lstExpDocHistDTO != null && !lstExpDocHistDTO.isEmpty()) {
                            for(ExpedienteDocHistDTO expDocHistDTO : lstExpDocHistDTO) {
                                //borramos el hco de exp doc
                                this.expedienteDocHistService.delete(expDocHistDTO.getIdExpedienteDocHist());
                            }
                        }
                        
                        //insert del ExpDocHist
                        /*ExpedienteDoc expDoc = ExpedienteDocToEntity.INSTANCE.apply(expDocDTO);
                        this.expedienteDocHistDAO.crearHistoricoBaja(expDoc, UserUtils.getUserName().toUpperCase());*///this.infoUsuarioBean.getLogin()
                        
                        //borramos expDoc
                        this.expedienteDocService.delete(expJusDocDTO.getIdExpedienteDoc());
                    }
                }
                     
                // Obtenemos los historicos de JCT
                List<AtlasFilter> filtroHist = new ArrayList<>();
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC,
                        expJusDTO.getIdExpedienteJusTec(), Operador.EQUALS));
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteJusTecHistDTO> lstHistoricoJus = this.expedienteJusTecHistService.buscar(0, -1, filtroHist, null);

                if (lstHistoricoJus != null && !lstHistoricoJus.isEmpty()) {
                    for (ExpedienteJusTecHistDTO expHisJus: lstHistoricoJus) {
                        //borramos el historico de justec
                        this.expedienteJusTecHistService.delete(expHisJus.getIdExpedienteJusTecHist());
                    }
                }
                
                //borramos el expJustec
                this.expedienteJusTecService.delete(expJusDTO.getIdExpedienteJusTec());
               
            }
        }
    }
    
    
    private void revertirExpJusEco(Integer idExpediente) throws ServiceException {
        List<ExpedienteJusEcoDTO> lstExpJusecoDTO = this.expedienteJusEcoService.buscarPorExpediente(idExpediente);
        if (lstExpJusecoDTO != null && !lstExpJusecoDTO.isEmpty()) {
            for (ExpedienteJusEcoDTO expJusDTO: lstExpJusecoDTO) {
                                
                //Esta clase no está implementada aun
                //buscamos los docs de cada exp justec
                /*List<AtlasFilter> filtro = new ArrayList<>();
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSECO,
                        expJusDTO.getIdExpedienteJusEco(), Operador.EQUALS));
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                                
                List<ExpedienteJusEcoDocDTO> lstExpJusecoDocDTO = this.expedienteJusEcoService.buscarDocs(0, -1, filtro, null);
                if (lstExpJusecoDocDTO != null && !lstExpJusecoDocDTO.isEmpty()) {
                    for (ExpedienteJusEcoDocDTO expJusDocDTO: lstExpJusecoDocDTO) {
                        
                        //borramos expJusEcoDoc
                        this.expedienteJusEcoDocService.delete(expJusDocDTO.getIdExpedienteJusEcoDoc());
                        
                        //buscamos los historicos de expedientes doc asociados para darlos de baja antes
                        List<AtlasFilter> filtroH = new ArrayList<>();
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                expJusDocDTO.getIdExpedienteDoc(), Operador.EQUALS));
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        
                        List<ExpedienteDocHistDTO> lstExpDocHistDTO = this.expedienteDocHistService.buscar(0, -1, filtroH, null);
                        
                        if (lstExpDocHistDTO != null && !lstExpDocHistDTO.isEmpty()) {
                            for(ExpedienteDocHistDTO expDocHistDTO : lstExpDocHistDTO) {
                                //borramos el hco de exp doc
                                this.expedienteDocHistService.delete(expDocHistDTO.getIdExpedienteDocHist());
                            }
                        }
                        
                        //insert del ExpDocHist
                        ExpedienteDoc expDoc = ExpedienteDocToEntity.INSTANCE.apply(expDocDTO);
                        this.expedienteDocHistDAO.crearHistoricoBaja(expDoc, UserUtils.getUserName().toUpperCase());
                        
                        //borramos expDoc
                        this.expedienteDocService.delete(expJusDocDTO.getIdExpedienteDoc());
                    }
                }*/
                
                // Obtenemos los historicos de JusEco
                List<AtlasFilter> filtroHist = new ArrayList<>();
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSECO,
                        expJusDTO.getIdExpedienteJusEco(), Operador.EQUALS));
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteJusEcoHistDTO> lstHistoricoJus = this.expedienteJusEcoHistService.buscar(0, -1, filtroHist, null);

                if (lstHistoricoJus != null && !lstHistoricoJus.isEmpty()) {
                    for (ExpedienteJusEcoHistDTO expHisJus: lstHistoricoJus) {
                        //borramos el historico de juseco
                        this.expedienteJusEcoHistService.delete(expHisJus.getIdExpedienteJusEcoHist());
                    }
                }
                
                //borramos el expJuseco
                this.expedienteJusEcoService.delete(expJusDTO.getIdExpedienteJusEco());        
            }
        }
    }
    
    
    private void revertirExpContratos(Integer idExpediente) throws ServiceException {
        List<ExpedienteContratoDTO> lstExpContratoDTO = this.expedienteContratoService.buscarPorExpediente(idExpediente);
        if (lstExpContratoDTO != null && !lstExpContratoDTO.isEmpty()) {
            for (ExpedienteContratoDTO expConDTO: lstExpContratoDTO) {
                                
                //buscamos los docs de cada exp contrato
                List<AtlasFilter> filtro = new ArrayList<>();
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_CONTRATO,
                        expConDTO.getIdExpedienteContrato(), Operador.EQUALS));
                filtro.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                                
                List<ExpedienteContratoDocDTO> lstExpContratoDocDTO = this.expedienteContratoService.buscarDocs(0, -1, filtro, null);
                if (lstExpContratoDocDTO != null && !lstExpContratoDocDTO.isEmpty()) {
                    for (ExpedienteContratoDocDTO expConDocDTO: lstExpContratoDocDTO) {
                        
                        //borramos expContratoDoc
                        this.expedienteContratoDocService.delete(expConDocDTO.getIdExpedienteContratoDoc());
                        
                        //buscamos los historicos de expedientes doc asociados para darlos de baja antes
                        List<AtlasFilter> filtroH = new ArrayList<>();
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                                expConDocDTO.getExpedienteDocDTO().getIdExpedienteDoc(), Operador.EQUALS));
                        filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                        
                        List<ExpedienteDocHistDTO> lstExpDocHistDTO = this.expedienteDocHistService.buscar(0, -1, filtroH, null);
                        
                        if (lstExpDocHistDTO != null && !lstExpDocHistDTO.isEmpty()) {
                            for(ExpedienteDocHistDTO expDocHistDTO : lstExpDocHistDTO) {
                                //borramos el hco de exp doc
                                this.expedienteDocHistService.delete(expDocHistDTO.getIdExpedienteDocHist());
                            }
                        }
                        
                        //insert del ExpDocHist
                        /*ExpedienteDoc expDoc = ExpedienteDocToEntity.INSTANCE.apply(expDocDTO);
                        this.expedienteDocHistDAO.crearHistoricoBaja(expDoc, UserUtils.getUserName().toUpperCase());*/
                        
                        //borramos expDoc
                        this.expedienteDocService.delete(expConDocDTO.getExpedienteDocDTO().getIdExpedienteDoc());
                    }
                }
                
                // Obtenemos los historicos de exp Contrato
                List<AtlasFilter> filtroHist = new ArrayList<>();
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_CONTRATO,
                        expConDTO.getIdExpedienteContrato(), Operador.EQUALS));
                filtroHist.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteContratoHistDTO> lstHistoricoCon = this.expedienteContratoHistService.buscar(0, -1, filtroHist, null);

                if (lstHistoricoCon != null && !lstHistoricoCon.isEmpty()) {
                    for (ExpedienteContratoHistDTO expHisCon: lstHistoricoCon) {
                        //borramos el historico de exp Contrato
                        this.expedienteContratoHistService.delete(expHisCon.getIdExpedienteContratoHist());
                    }
                }
                
                //borramos el expContrato
                this.expedienteContratoService.delete(expConDTO.getIdExpedienteContrato());
            }
        }
    }
    
    private void revertirExpDocAcepracion(Integer idExpediente) throws ServiceException {
        
        ExpedienteDTO expedienteDTO = this.buscarId(idExpediente);
        
        if (expedienteDTO.getListaDocAceptacionAyuda() != null && !expedienteDTO.getListaDocAceptacionAyuda().isEmpty()) {
            for (ExpedienteDocDTO expedienteDocDTO: expedienteDTO.getListaDocAceptacionAyuda()) {
               //buscamos los historicos de expedientes doc asociados para darlos de baja antes
                List<AtlasFilter> filtroH = new ArrayList<>();
                filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC,
                        expedienteDocDTO.getIdExpedienteDoc(), Operador.EQUALS));
                filtroH.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteDocHistDTO> lstExpDocHistDTO = this.expedienteDocHistService.buscar(0, -1, filtroH, null);
                
                if (lstExpDocHistDTO != null && !lstExpDocHistDTO.isEmpty()) {
                    for(ExpedienteDocHistDTO expDocHistDTO : lstExpDocHistDTO) {
                        //borramos el hco de exp doc
                        this.expedienteDocHistService.delete(expDocHistDTO.getIdExpedienteDocHist());
                    }
                }
                
                //borramos expDoc
                this.expedienteDocService.delete(expedienteDocDTO.getIdExpedienteDoc());
              }
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String obtenerFechaLimiteSubsanacionJustec(List<ExpedienteDocDTO> expedienteDocDTO,
            String cdNumIden) throws ServiceException {

        StringBuilder fechaLimite = new StringBuilder();
        if (expedienteDocDTO != null && !expedienteDocDTO.isEmpty()) {

            List<Integer> lstEstados = new ArrayList<>();
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));
            // Obtenemos un registro de los asociados a la petición de
            // subsanación
            Optional<ExpedienteDocDTO> opExpedienteDoc = expedienteDocDTO.stream()
                    .filter(f -> f.getIdDocumentoPtfr() != null
                            && lstEstados.contains(f.getEstadoDTO().getIdValor()))
                    .findFirst();

            // Inicializamos el formato de fecha
            fechaLimite.append("dd/MM/yyyy");
            if (opExpedienteDoc.isPresent()) {

                // Obtenemos el registro de la petición a portafirmas
                DocumentoPtfr peticionPortafirmas = this.documentoPtfrDAO
                        .find(opExpedienteDoc.get().getIdDocumentoPtfr());

                if (Objects.nonNull(peticionPortafirmas.getFcFinPlazoSubsanacion())) {

                    LocalDateTime fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(
                            peticionPortafirmas.getFcFinPlazoSubsanacion());

                    fechaLimite = new StringBuilder();
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getDayOfMonth()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getMonthValue()), 2, "0"));
                    fechaLimite.append("/");
                    fechaLimite.append(fcLimiteSubsanacion.getYear());
                    fechaLimite.append(" ");
                    fechaLimite.append(StringUtils
                            .leftPad(String.valueOf(fcLimiteSubsanacion.getHour()), 2, "0")
                            + ":"
                            + StringUtils.leftPad(String.valueOf(fcLimiteSubsanacion.getMinute()),
                                    2, "0")
                            + ":" + StringUtils.leftPad(
                                    String.valueOf(fcLimiteSubsanacion.getSecond()), 2, "0"));

                }

            }
        }

        return fechaLimite.toString();
    }

    /**
     * Método para procesar el cambio de alegaciones
     * 
     * @param idExpediente
     * @param idEstado
     * @param alegacionRevisada
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO procesarRevisionAlegacionGestor(Integer idExpediente,Integer idConvocatoria,
            String alegacionRevisada, Boolean activarConcederDenegar ) throws ServiceException {
        Expediente expediente = this.find(idExpediente);
        
        
        //aprobada
        if (Constantes.AfirmacionNegacion.S.equals(alegacionRevisada)) {

            //nuevo Cambio de estadod e la revision financiacion
            if(expediente.getItAlegaciones().equals(Constantes.AfirmacionNegacion.S)) {
                expediente.setEstadoRevFinan(this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION)));
            }
            
            if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)) ||
                    expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                
                expediente.setItAlegacionRevisada(Constantes.AfirmacionNegacion.S);
                //por hacer en la MD-2150
                activarConcederDenegar =  Boolean.TRUE;               
                if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))) {                
                    this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), null);                    
                }else if( expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                    this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), null);
                }

            }else if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)) ||
                    expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))){
                                               
                //buscar el historico y poner esos estados y provional
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,expediente.getIdExpediente(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, expediente.getEstado().getIdValor(), Operador.NOT_EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR_PROVISIONAL, Operador.NULL));
                

                List<AtlasSort> orden = new ArrayList<>();
                orden.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_HIST, Boolean.FALSE));
                      
                List<ExpedienteHist> expHist = this.expedienteHistDAO.find(filtros, orden);
                
                if(!expHist.isEmpty()) {
                    expediente.setEstado(expHist.get(0).getEstado());
                    expediente.setEstadoProvisional(expHist.get(0).getEstadoProvisional());
                    expediente.setEstadoRevRequi(expHist.get(0).getEstadoRevRequi());
                    expediente.setEstadoRevAdmin(expHist.get(0).getEstadoRevAdmin());
                }
                
                expediente.setItAlegacionRevisada(null);                
                expediente = this.getDao().merge(expediente);
                
            }

        }else if(Constantes.AfirmacionNegacion.N.equals(alegacionRevisada)) {
            //rechazada
            
            ConvocatoriaDTO convo  = this.convocatoriaService.buscarId(idConvocatoria);
            
            Boolean esConjunta = Boolean.FALSE;
            
            if(convo.getTipoResolModalidadDTO() != null &&  convo.getTipoResolModalidadDTO().getCdTipoResolucion() != null) {
                esConjunta =  convo.getTipoResolModalidadDTO().getCdTipoResolucion().equals(Constantes.Convocatoria.RESOL_CONJ);
            }
                               
            expediente.setItAlegacionRevisada(Constantes.AfirmacionNegacion.N);
            
            if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL)) ||
                    expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                
                if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL))) {
                    
                    if (Boolean.TRUE.equals(esConjunta)) {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_CONCEDIDO_DEFINITIVO), null);  
                    }else {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL), null);  
                    }
                                               
                }else if( expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL))) {
                    
                    if (Boolean.TRUE.equals(esConjunta)) {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DENEGADO_DEFINITIVO), null);  
                    }else {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL), null);  
                    }                                         
                }
                
                //por hacer en la MD-2150
                activarConcederDenegar =  Boolean.TRUE;
             
            }else if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL)) ||
                    expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))){
                               
                if(expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL))) {
                    
                    if (Boolean.TRUE.equals(esConjunta)) {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_EXCLUSION_DEFINITIVO), null);     
                    }else {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDOS_PROVISIONAL), null);
                    }
       
                }else if( expediente.getEstadoProvisional().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL))) {
                    
                    if (Boolean.TRUE.equals(esConjunta)) {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA_RESOLUCION_DESISTIMIENTO_DEFINITIVO), null);   
                    }else {
                        this.cambiarEstado(idExpediente,utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL), null);
                    }   
                    
                }
                
                activarConcederDenegar = Boolean.FALSE;
            }
            
           
        }
        

        //QUER HACEMOS CON ESTO, SOLO CAMBIAMOS EL SEITALEGACION REVISDAD?
//        if (Constantes.AfirmacionNegacion.S.equals(alegacionRevisada)) {
//            expediente.setEstado(this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_ALEGACIONES)));
//            expediente.setItAlegacionRevisada(Constantes.AfirmacionNegacion.N);
//        } else if (alegacionRevisada == null
//                || Constantes.AfirmacionNegacion.N.equals(alegacionRevisada)) {
//            expediente.setEstado(this.catalogosValoresService.find(expediente.getEstadoProvisional().getIdValor()));
//            expediente.setItAlegacionRevisada(Constantes.AfirmacionNegacion.S);
//        }
        

        //SE QUEDA IUGAL LA ULTIMA PARTE

        
        this.expedienteHistDAO.crearHistorico(expediente, null);
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);
        expedienteDTO.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(expediente.getEstado()));
        expedienteDTO.setItAlegacionRevisada(expediente.getItAlegacionRevisada());
        expedienteDTO.setActivarConcederDenegar(activarConcederDenegar);
        
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL),
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<DocumentoPtfr> lstDocPtfr = this.documentoPtfrDAO.find(filtros, null);
        for (DocumentoPtfr docPtfr : lstDocPtfr) {
            this.documentoPtfrDAO.deleteLogico(docPtfr);
        }

        return expedienteDTO;
    }

    private DocumentoPtfrDTO obtenerInformacionDesistido(Integer idExpediente) {
        DocumentoPtfrDTO documentoPtfrDTO = new DocumentoPtfrDTO();

        List<Integer> estadosExpediente = new ArrayList<>();
        estadosExpediente.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS)
                        .or(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL)));

        List<DocumentoPtfr> listaPeticiones = this.documentoPtfrDAO.find(filtros, null);

        if (!listaPeticiones.isEmpty()) {
            DocumentoPtfr documentoPtfr = listaPeticiones.get(0);
            documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);
        }

        return documentoPtfrDTO;
    }

    private DocumentoPtfrDTO obtenerInformacionExcluido(Integer idExpediente) {
        DocumentoPtfrDTO documentoPtfrDTO = new DocumentoPtfrDTO();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, 
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO,
                Constantes.AfirmacionNegacion.N, Operador.EQUALS)
                        .or(new AtlasFilter(Constantes.Filtro.FILTRO_ITCORREO, Operador.NULL)));

        List<DocumentoPtfr> listaPeticiones = this.documentoPtfrDAO.find(filtros, null);

        if (!listaPeticiones.isEmpty()) {
            DocumentoPtfr documentoPtfr = listaPeticiones.get(0);
            documentoPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(documentoPtfr);
        }

        return documentoPtfrDTO;
    }

    @Override
    public List<ExpedienteDTO> buscarIdSolicitud(Integer idSolicitud) throws ServiceException {

        List<ExpedienteDTO> listExpedienteDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return listExpedienteDTO;

    }

    @Override
    public void registrarDocuemntosRenunciaContrato(Integer idExpediente, Integer tramite)
            throws ServiceException {

        ExpedienteDTO expedienteDTO = buscarId(idExpediente);
        ImpresoDTO impresoContratoDTO = null;
        if (tramite != null
                && tramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION))) {
            impresoContratoDTO = expedienteDTO.getImpresoRenunciaConSutitucionDTO();
        } else if (tramite != null
                && tramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_RENUNCIA))) {
            impresoContratoDTO = expedienteDTO.getImpresoRenunciaSinSutitucionDTO();
        }

        if (impresoContratoDTO != null) {
            impresoContratoDTO.getLstBloqueDTO().sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Obtenemos los bloquesComponente que generen documentos
            for (BloqueDTO bloqueDTO : impresoContratoDTO.getLstBloqueDTO()) {

                bloqueDTO.getLstBloqueComponenteDTO()
                        .sort(Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                // Si es bloque es de tipo documento
                if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                    for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                            .getLstBloqueComponenteDTO()) {

                        // Si está dado de baja lo ignoramos
                        if (bloqueComponenteDTO.getFcBajaFila() != null) {
                            continue;
                        }

                        ExpedienteContrato expContrato = ExpedienteContratoToEntity.INSTANCE
                                .apply(this.expedienteContratoService
                                        .obtenerExpedienteContratoByExpediente(expedienteDTO));
                        // Creamos un expedienteDoc de este bloque
                        ExpedienteDocDTO expedientedoc = this.crearExpedienteDocContrato(
                                expedienteDTO, bloqueComponenteDTO, tramite);
                        // Creamos el expedienteContratoDocs del contrato
                        // inicial
                        ExpedienteContratoDoc expedienteContratoDoc = new ExpedienteContratoDoc();
                        expedienteContratoDoc
                                .setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                        expedienteContratoDoc.setFcAltaFila(new Date());
                        expedienteContratoDoc.setExpedienteContrato(expContrato);
                        ExpedienteDoc expDoc = new ExpedienteDoc();
                        expDoc.setIdExpedienteDoc(expedientedoc.getIdExpedienteDoc());
                        expedienteContratoDoc.setExpedienteDoc(expDoc);
                        this.expedienteContratoDocService.insert(expedienteContratoDoc);
                        expedienteDTO.getLstExpedienteDocDTO().add(expedientedoc);
                    }
                }
            }

        }

    }

    /**
     * Actualiza estados del ExpedienteContrato verficando los estados de su
     * documentación
     */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteContratoDTO actualizarRevisionContratos(ExpedienteContratoDTO expContratoDTO,
            String estadoRevision) throws ServiceException {
        ExpedienteContratoDTO resultDto = null;
        Expediente exp = this.getDao().find(expContratoDTO.getIdExpediente());

        // Quitamos los registros de baja y sin tramite
        List<ExpedienteDoc> lstExpDoc = exp.getLstExpedienteDoc().stream()
                .filter(x -> x.getFcBajaFila() == null).filter(x -> x.getTramite() != null)
                .collect(Collectors.toList());


        // Obtenemos la lista de documentos asociados a los requerimientos
        List<ExpedienteDoc> lstExpContratos = lstExpDoc.stream()
                .filter(x -> (x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_CONTRATO_RENUNCIA)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_RENUNCIA)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_SUSTITUCION)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL)
                        || x.getTramite().getNmConstante().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION))
                        && (!x.getEstado().getNmConstante().equals(Constantes.EstadoDocumento.SIN_SUBIR)
                                || (x.getBloqueComponente() != null
                                        && x.getBloqueComponente().getItObligatorio() != null
                                        && x.getBloqueComponente().getItObligatorio()
                                                .equals(Constantes.AfirmacionNegacion.S))))
                .collect(Collectors.toList());

        // Consultamos si hay algun documento diferente de Revision OK
        Boolean existeNoRevisionOk = lstExpContratos.stream()
                .anyMatch(x -> !Constantes.EstadoDocumento.REVISION_OK.equals(x.getEstado().getNmConstante()));

        // Si todos estan en Revisión OK o se trata de una renuncia SIN
        // sustitucion, actualizamos el estado del expediente
        if ((Boolean.FALSE.equals(existeNoRevisionOk) || Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA
                .equals(expContratoDTO.getIdEstadoDTO().getNmConstante()))
                && estadoRevision.contentEquals("OK")) {
            if (expContratoDTO.getIdEstadoRevAdminDTO().getNmConstante()
                    .equals(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA)
                    || expContratoDTO.getIdEstadoRevAdminDTO().getNmConstante()
                            .equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA)) {
                resultDto = this.expedienteContratoService.cambiarEstadoContrato(expContratoDTO,
                        expContratoDTO.getIdExpediente(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_APROBADA),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
            } else {
                resultDto = this.expedienteContratoService.cambiarEstadoContrato(expContratoDTO,
                        expContratoDTO.getIdExpediente(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_OK));
            }

        } else {

            // Consultamos si todos estan OK o pend subsanar
            Boolean existePendSubsanar = lstExpContratos.stream()
                    .allMatch(x -> (Constantes.EstadoDocumento.REVISION_OK.equals(x.getEstado().getNmConstante()))
                            || (Constantes.EstadoDocumento.REQUIERE_SUBSANACION.equals(x.getEstado().getNmConstante())));
            if ((Boolean.TRUE.equals(existePendSubsanar)
                    || estadoRevision.contentEquals("Subsanar"))
                    && !estadoRevision.contentEquals("KO")) {

                if (expContratoDTO.getIdEstadoRevAdminDTO().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA)) {
                    resultDto = this.expedienteContratoService.cambiarEstadoContrato(expContratoDTO,
                            expContratoDTO.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA));
                } else {
                    resultDto = this.expedienteContratoService.cambiarEstadoContrato(expContratoDTO,
                            expContratoDTO.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION));
                }
            } else {

                // Consultamos si hay algun KO
                Boolean existeKO = lstExpContratos.stream()
                        .anyMatch(x -> (Constantes.EstadoDocumento.REVISION_KO.equals(x.getEstado().getNmConstante())));
                if (Boolean.TRUE.equals(existeKO) || estadoRevision.contentEquals("KO")) {

                    resultDto = this.expedienteContratoService.cambiarEstadoContrato(expContratoDTO,
                            expContratoDTO.getIdExpediente(),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO),
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REVISION_KO));
                }
            }
        }

        return resultDto;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarFechaMaxContrato(ExpedienteDTO expDTO) throws ServiceException {

        Expediente exp = this.getDao().find(expDTO.getIdExpediente());
        ExpedienteDTO dto = null;

        // Si son fechas distintas, actualizamos
        if (!expDTO.getFcFinContrato().equals(exp.getFcFinContrato())) {

            exp.setFcFinContrato(expDTO.getFcFinContrato());
            exp = this.getDao().merge(exp);
            dto = ExpedienteToDTO.INSTANCE.apply(exp);
            this.expedienteHistDAO.crearHistorico(exp, null);

            // Actualizamos fechas de sus hijos
            List<ExpedienteDTO> lstHijos = this.buscarExpedientePorExpedientePadre(
                    exp.getSolicitud().getIdSolicitud(), exp.getIdExpediente());
            for (ExpedienteDTO expHijo : lstHijos) {
                if (expHijo.getExpedienteContrato() != null
                        && expHijo.getExpedienteContrato().getIdEstadoDTO() != null
                        && expHijo.getExpedienteContrato().getIdEstadoDTO().getIdValor() != null
                        && expHijo.getExpedienteContrato().getIdEstadoDTO().getNmConstante().equals(Constantes.EstadoContrato.SIN_SUBIR)) {
                    expHijo.setFcFinContrato(expDTO.getFcFinContrato());
                    this.getDao().merge(ExpedienteToEntity.INSTANCE.apply(expHijo));
                    this.expedienteHistDAO.crearHistorico(ExpedienteToEntity.INSTANCE.apply(expHijo), null);
                    
                }
            }
        }
        return dto;
    }

    @Override
    public void rellenarCdAsuntoExp(ExpedienteDTO dto, ImpresoDTO impresos) {
        if (null != impresos && StringUtils.isNotBlank(impresos.getCdIdentificacion())) {
            Integer indexContains = impresos.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                dto.setCdAsunto(impresos.getCdIdentificacion());
            } else {
                dto.setCdAsunto(impresos.getCdIdentificacion().substring(0, indexContains));
            }
        }
    }

    @Override
    public BloqueComponenteDTO eliminarDatos(BloqueComponenteDTO bloqueComponenteDTO)
            throws ServiceException {

        List<DatoDTO> lstDatos = bloqueComponenteDTO.getDatos();

        for (DatoDTO datoDTO : lstDatos) {

            // Convertir valor con símbolo euro a
            // numérico
            if (datoDTO.getIdDato() != null) {
                // Borrado lógico (no físico): un Dato que en algún momento se adjuntó a un
                // documento queda referenciado para siempre desde SAII_EXPEDIENTE_DOC_HIST
                // (histórico, nunca se borra ni se actualiza), así que un DELETE físico aquí
                // falla por la FK ID_DATO de esa tabla aunque el documento ya se haya borrado.
                // El resto de la app ya trata los Dato dados de baja como inexistentes
                // (rellenarValoresNoRelacionables filtra FC_BAJA_FILA IS NULL), así que el
                // borrado lógico es equivalente a efectos de UI y evita el error.
                this.datoService.borradoLogico(datoDTO.getIdDato());

            }
        }
        return bloqueComponenteDTO;
    }

    @Override
    public List<ExpedienteDTO> buscarExpedienteConvocatoriaInteresado(Integer idConvocatoria,
            Integer idInteresado) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, idInteresado,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

    }

    /**
     * Busca expedientes hijos con requisitos pendiente de subsanar
     */
    @Override
    public List<ExpedienteDTO> buscarExpedientesHijoSubsanar(Integer idSolicitud, Integer idActor,
            String itTipo, Integer idExpedientePadre) throws ServiceException {

        List<ExpedienteDTO> listResponseOut = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        if ("C".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PC".equals(itTipo) || "PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        }

        if ("PC".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORCOORDINADOR,
                    idActor, Operador.EQUALS));
        }

        if ("PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            List<AtlasFilter> filtroParticipantesOrCoord = new ArrayList<>();
            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));

            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    filtroParticipantesOrCoord, Operador.OR));

        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE, idExpedientePadre,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        listResponseOut = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

        // Usar un iterador para evitar problemas de modificación concurrente
        Iterator<ExpedienteDTO> iterator = listResponseOut.iterator();

        while (iterator.hasNext()) {
            ExpedienteDTO expDTO = iterator.next();

            List<Integer> tramitesPtfr = new ArrayList<>();
            tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
            // MD-2190 Bo añadimos el id del t´ramite de alegación esclusión provisional
//            tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL));

            // Obtener los documentos asociados al expediente
            List<DocumentoPtfrDTO> docPtfr = documentoPtfrDAO
                    .obtenerPorExpedienteYTramites(expDTO.getIdExpediente(), tramitesPtfr);

            // Si no hay documentos asociados, eliminar el expediente de la
            // lista
            if (docPtfr == null || docPtfr.isEmpty()) {
                iterator.remove(); // Elimina el elemento actual de la lista de
                                   // manera segura
            } else {
                // Procesar los documentos para verificar el estado de la
                // notificación en NOTE
                for (DocumentoPtfrDTO expDoc : docPtfr) {
                    if (Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO
                            .equals(expDoc.getTramiteDTO().getNmConstante())
                            && (Objects.isNull(expDoc.getFcLecturaNote()))) {

                        expDTO.setLeidoNotificacionNote(Constantes.AfirmacionNegacion.N);
                        break;
                    }
                }
            }
        }

        return listResponseOut;
    }

    /**
     * Busca expedientes hijos con requisitos pendiente de subsanar
     */
    @Override
    public List<ExpedienteDTO> buscarExpedientesHijoSubsanarAdmin(Integer idSolicitud,
            Integer idActor, String itTipo, Integer idExpedientePadre) throws ServiceException {

        List<ExpedienteDTO> listResponseOut = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        if ("C".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PC".equals(itTipo) || "PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
        }

        if ("PC".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORCOORDINADOR,
                    idActor, Operador.EQUALS));
        }

        if ("PS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_CONVACT_IDACTORSOLICITANTE,
                    idActor, Operador.EQUALS));
        }

        if ("PCS".equals(itTipo)) {
            List<AtlasFilter> filtroParticipantesOrCoord = new ArrayList<>();
            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));

            filtroParticipantesOrCoord.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR), Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXP_TIPO_ACTOR,
                    filtroParticipantesOrCoord, Operador.OR));

        }

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_PADRE, idExpedientePadre,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        listResponseOut = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteToDTO.INSTANCE));

        // Usar un iterador para evitar problemas de modificación concurrente
        Iterator<ExpedienteDTO> iterator = listResponseOut.iterator();

        while (iterator.hasNext()) {
            ExpedienteDTO expDTO = iterator.next();

            List<Integer> tramitesPtfr = new ArrayList<>();
            tramitesPtfr.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));

            // Obtener los documentos asociados al expediente
            List<DocumentoPtfrDTO> docPtfr = documentoPtfrDAO
                    .obtenerPorExpedienteYTramites(expDTO.getIdExpediente(), tramitesPtfr);

            // Si no hay documentos asociados, eliminar el expediente de la
            // lista
            if (docPtfr == null || docPtfr.isEmpty()) {
                iterator.remove(); // Elimina el elemento actual de la lista de
                                   // manera segura
            }
        }

        for (ExpedienteDTO expedienteDTO : listResponseOut) {
            if (expedienteDTO.getImpresoDTO() == null
                    || expedienteDTO.getImpresoDTO().getIdImpreso() == null) {
                List<Integer> tipoImpreso = new ArrayList<>();
                if (Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
                    tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
                } else {
                    tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
                }
                List<Impreso> lstImpresoDTO = this.impresoService
                        .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpreso);
                if (lstImpresoDTO != null && !lstImpresoDTO.isEmpty()) {
                    expedienteDTO.getImpresoDTO().setIdConvocatoria(
                            lstImpresoDTO.get(0).getConvocatoria().getIdConvocatoria());
                    expedienteDTO.getImpresoDTO().setIdImpreso(lstImpresoDTO.get(0).getIdImpreso());
                }
            }
        }

        return listResponseOut;
    }

    /**
    *
    */
    @Override
    public List<ExpedienteDTO> buscarExpedientePorSolicitud(Integer idSolicitud)
            throws ServiceException {

        List<ExpedienteDTO> listResponseOut = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        listResponseOut = new ArrayList<>(Lists.transform(
                this.getDao().listExpedientePorExpedientePadre(idSolicitud, idSolicitud),
                ExpedienteToDTO.INSTANCE));

        for (ExpedienteDTO expDTO : listResponseOut) {

            ExpedienteContrato expContrat = this.getDao()
                    .findExpContratById(expDTO.getIdExpediente());

            if (expContrat != null && expContrat.getIdExpedienteContrato() != null) {
                ExpedienteContratoDTO expedienteContratoDTO = ExpedienteContratoToDTO.INSTANCE
                        .apply(expContrat);
                expDTO.setExpedienteContrato(expedienteContratoDTO);

            }
        }

        return listResponseOut;
    }

    @Override
    public List<ExpedienteDTO> obtenerExpedienteEstructuradoSinActorParticipante(
            Integer idSolicitud, String todos, Integer idExpediente) throws ServiceException {

        // Configuramos lista de tipo de actores de primer nivel
        List<Integer> lstActores = new ArrayList<>();
        lstActores.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
        lstActores.add(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR));
        if (todos.equals(Constantes.AfirmacionNegacion.S)) {
            lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE));
        }

        // Obtenemos la lista de expediente de solicitante y coordinadores
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                idSolicitud, Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosEntrada
                .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                        lstActores, Operador.IN));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort("idExpediente", true));

        List<ExpedienteDTO> lstExpedienteDTO = this.buscar(0, -1, filtrosEntrada, ordenamiento);

        List<ExpedienteDTO> lstParticipanteSolicitanteDTO = new ArrayList<>();

        // Iteramos la lista de expedientes solicitante y coordinadores
        for (ExpedienteDTO expDTO : lstExpedienteDTO) {

            List<ConvocatoriaActDTO> lstActoresParticipante = new ArrayList<>();

            // Si es expediente coordinador recuperamos la lista de actores
            // participantes
            // del coordinador
            if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesCoordinador(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }
            // Si es expediente solicitante recuperamos la lista de actores
            // participantes
            // del solicitante
            else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesSolicitante(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }

            // Si existen actores participante asociados
            if (!lstActoresParticipante.isEmpty()) {
                
                // Obtenemos la lista de expedientes participantes
                filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                        idSolicitud, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,
                        idExpediente, Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                List<ExpedienteDTO> lstParti = this.buscar(0, -1, filtrosEntrada, ordenamiento);
                
                // Si existen participantes recuperados
                if (!lstParti.isEmpty()) {

                    // Si son participantes de coordinador
                    if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                        // Agregamos a la lista de participantes (2do nivel)
                        expDTO.setLstExpedienteParticipante(lstParti);

                        // Si son participantes de solicitante
                    } else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                        // Agregamos la lista de participantes (1er nivel)
                        lstParticipanteSolicitanteDTO.addAll(lstParti);
                    }
                }
            }

        }

        // Si existen participantes de solicitante
        if (!lstParticipanteSolicitanteDTO.isEmpty()) {
            // Agregamos la lista de participantes a la lista principal
            lstExpedienteDTO.addAll(lstParticipanteSolicitanteDTO);
        }

        return lstExpedienteDTO;
    }



@Override
public ExpedienteDTO guardarDatosBloquesMultiregistro(List<BloqueDTO> listaBloquesDTO,
        List<ExpedienteDocDTO> lstExpedienteDoc)
        throws ServiceException {
   DatoDTO datoDocmento= null;
   Integer idExpediente=null;
    for (BloqueDTO bloqueDTO : listaBloquesDTO) {
        for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                .getLstBloqueComponenteDTO()) {
           if(bloqueComponenteDTO.getDatos() != null && !bloqueComponenteDTO.getDatos().isEmpty()){
               ExpedienteDTO expedienteDTO =bloqueComponenteDTO.getDatos().get(0).getExpedienteDTO();
               idExpediente=expedienteDTO.getIdExpediente();
               for(DatoDTO dato:bloqueComponenteDTO.getDatos()) {
                   dato.setExpedienteDTO(expedienteDTO);
                   if (Objects.isNull(dato.getIdBloqueComponente())) {
                       dato.setIdBloqueComponente(bloqueComponenteDTO.getIdBloqueComponente());
                   }
                   
                   if (dato.getIdDato() == null) {
                       dato.setCdUsuAlta(Utils.valorAuditoria(expedienteDTO.getInteresadoDTO().getCdNumIden()));
                       dato.setFcAltaFila(new Date());
                   }
                   
                   datoDocmento= this.datoService.guardar(dato);
               }
              this.getDao().flush();
               
              // this.rellenarDatosMultiregistro(expedienteDTO, bloqueDTO, bloqueComponenteDTO);
           }
           
        }
    }
    
    if( !lstExpedienteDoc.isEmpty()){
      for(ExpedienteDocDTO doc:lstExpedienteDoc) {
          if(doc.getIdExpedienteDoc() != null && doc.getCdFica() != null) {
              doc.setDatoDTO(datoDocmento);
              BloqueComponenteDTO bloqueComponenteDTO = new BloqueComponenteDTO();
              bloqueComponenteDTO.setIdBloqueComponente(datoDocmento.getIdBloqueComponente());
              doc.setBloqueComponenteDTO(bloqueComponenteDTO);
              this.expedienteDocService.getDao().update(ExpedienteDocToEntity.INSTANCE.apply(doc));
              this.expedienteDocService.getDao().flush();
          }
           
      }
       
    }
      
    return this.buscarId(idExpediente);
    
   
}

           

@Override
public List<BloqueDTO> buscarMultibloquesExpediente(Integer idBloque, Integer idExpediente)
        throws ServiceException {
    List<BloqueDTO> listaSubbloqueDTO = new ArrayList<>();
    ExpedienteDTO dto= this.buscarId(idExpediente);
    if(dto != null && dto.getImpresoDTO()!= null && dto.getImpresoDTO().getLstBloqueDTO()!= null) {
         listaSubbloqueDTO= dto.getImpresoDTO().getLstBloqueDTO().stream()
                .filter(x->(( x.getIdBloquePadre() != null && x.getIdBloquePadre().equals(idBloque)) || x.getIdBloque().equals(idBloque) ) && Objects.isNull(x.getFcBajaFila()))
                .sorted(Comparator.comparing(BloqueDTO::getNmOrden)).collect(Collectors.toList());
         
         for (BloqueDTO bloqueDTO : listaSubbloqueDTO) {
             for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                     .getLstBloqueComponenteDTO()) {
              // Si está dado de baja lo ignoramos
                 if (bloqueComponenteDTO.getFcBajaFila() != null) {
                     continue;
                 }
                if(bloqueComponenteDTO.getDatos() != null && !bloqueComponenteDTO.getDatos().isEmpty()){
                    this.rellenarDatosMultiregistro(dto, bloqueDTO,
                             bloqueComponenteDTO);
                    List<DatoDTO> listDatos= bloqueComponenteDTO.getDatos().stream().filter(x-> x.getExpedienteDTO() !=null 
                            && x.getExpedienteDTO().getIdExpediente() !=null
                            && x.getExpedienteDTO().getIdExpediente().equals(idExpediente)).collect(Collectors.toList());
                    bloqueComponenteDTO.setDatos(listDatos);  
                }
             
             }
         }
                
        return listaSubbloqueDTO; 
    }
    
    return listaSubbloqueDTO;

}


@Override
public List<ExpedienteDocDTO> inicializarDocumentosMultibloquesExpediente(Integer idBloque, Integer idExpediente)
        throws ServiceException {
   
    List<ExpedienteDocDTO> listaExpedienteDocDTO = new ArrayList<>();
    ExpedienteDTO dto= this.buscarId(idExpediente);
    if(dto != null && dto.getImpresoDTO()!= null && dto.getImpresoDTO().getLstBloqueDTO()!= null) {
        List<BloqueDTO> listaSubbloqueDTO= dto.getImpresoDTO().getLstBloqueDTO().stream()
                .filter(x->(( x.getIdBloquePadre() != null && x.getIdBloquePadre().equals(idBloque)) || x.getIdBloque().equals(idBloque) ) && Objects.isNull(x.getFcBajaFila()))
                .sorted(Comparator.comparing(BloqueDTO::getNmOrden)).collect(Collectors.toList());
         if(listaSubbloqueDTO != null && !listaSubbloqueDTO.isEmpty()) {
             for (BloqueDTO bloqueDTO : listaSubbloqueDTO) {
                 for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                         .getLstBloqueComponenteDTO()) {
                  // Si está dado de baja lo ignoramos
                     if (bloqueComponenteDTO.getFcBajaFila() != null) {
                         continue;
                     }
                  // Si es bloque es de tipo documento
                    if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {
                     // Creamos un expedienteDoc de este bloque
                        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
                        CatalogosValores estado = catalogosValoresService.find(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
                        expedienteDoc.setEstado(estado);
                        
                        //VMM 17/07/25 MD-1778: error al setear catalogovalores sin generarlo antes (object references an unsaved transient instance)
                        if (bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO() != null &&
                                bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor() != null) {
                            CatalogosValores tipoDoc = catalogosValoresService.find(bloqueComponenteDTO.getComponenteDTO().getTipoDocumentoDTO().getIdValor());
                            expedienteDoc.setTipoDocumento(tipoDoc);
                        }

                        Expediente expAux = new Expediente();
                        expAux.setIdExpediente(dto.getIdExpediente());
                        expedienteDoc.setExpediente(expAux);
                        expedienteDoc
                                .setBloqueComponente(BloqueComponenteToEntity.INSTANCE.apply(bloqueComponenteDTO));

                        // Agregamos los indicadores de tramite y si es o no un adicional
                        expedienteDoc.setTramite(catalogosValoresService.find(Constantes.Tramite.PRESENTACION));
                        expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);

                        // Se usa el cdUsuAlta del expediente
                        expedienteDoc.setCdUsuAlta(dto.getCdUsuAlta());
                        expedienteDoc.setFcAltaFila(new Date());
                        expedienteDoc.setItAutoriza(Constantes.AfirmacionNegacion.N);
                        listaExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc));
                        
                    }
                    
               }
             
             }
         }
         
                
     }
    
    return listaExpedienteDocDTO;

}

private void rellenarDatosMultiregistro(ExpedienteDTO dto, BloqueDTO bloqueDTO,
        BloqueComponenteDTO bloqueComponenteDTO) throws ServiceException {
    // Recuperamos los datos del expediente
    List<DatoDTO> lstDatoDTO = this.datoService.obtenerPorExpediente(dto.getIdExpediente(),
            bloqueComponenteDTO.getIdBloqueComponente());

    // Obtenemos los datos relativos a este bloque
    List<DatoDTO> datosBloqueComponente = lstDatoDTO.stream()
            .filter(x -> x.getIdBloqueComponente()
                    .equals(bloqueComponenteDTO.getIdBloqueComponente())
                    && x.getExpedienteDTO() != null && x.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente())
                    && Objects.nonNull(x.getNmFila()))
            .sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList());

    // Quitamos todos los registros que estan de baja
    datosBloqueComponente = datosBloqueComponente.stream()
            .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

    bloqueComponenteDTO.getDatos().add(new DatoDTO());

    // Si no lo hemos encontrado es porque no es un tipo de
    // dato que se guarde aquí. Lo ignoramos
    if (Boolean.FALSE.equals(datosBloqueComponente.isEmpty())) {

        List<DatoDTO> lstDatos = new ArrayList<>();
       
        // Obtenemos cuantas filas tiene el bloque tabla
        Integer nmFilasTabla = this.datoService.obtenerFilasTabla(bloqueDTO);
        // Obtenemos cuantas filas hay en el bloque
        Integer nmFilas = this.datoService.obtenerNmRegistrosBloque(bloqueDTO, nmFilasTabla);

        if ((Boolean.TRUE.equals(bloqueDTO.getEsTipoFormulario())
                && Boolean.FALSE.equals(bloqueComponenteDTO.getItRelacion()) || 
                Boolean.TRUE.equals(bloqueDTO.getEsTipoTexto())) 
               ) {
            // Si es formulario y el componente no está
            // relacionado
           
                lstDatos.addAll(datosBloqueComponente);
           
            

        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoListado())) {
            // Es un listado, creamos tantos datos como filas
            // tenga el listado

            for (Integer i = 0; i < nmFilas; i++) {
                if(i< datosBloqueComponente.size()) {
                    lstDatos.add(datosBloqueComponente.get(i));
                }
                
            }

        } else if (Boolean.TRUE.equals(bloqueDTO.getEsTipoTabla())
                && Boolean.TRUE.equals(bloqueComponenteDTO.getItColumna())) {
            // Es una tabla, presupuesto presentado o ayuda
            // solicitada, creamos datos sólo de las columnas

            for (Integer i = 0; i < nmFilas; i++) {
                if(i< datosBloqueComponente.size()) {
                    lstDatos.add(datosBloqueComponente.get(i));
                }
                
            }
        }
       

        // Guardamos los datos en el bloqueComponente
        bloqueComponenteDTO.setDatos(lstDatos);
        
        //recuperamos los documentos de multiregistro y lo guardamos en el bloque
        List<ExpedienteDocDTO> listaDocuemntosBloqueComp= this.expedienteDocService.buscarPorExpediente(dto.getIdExpediente());
        if(!listaDocuemntosBloqueComp.isEmpty()) {
            listaDocuemntosBloqueComp= listaDocuemntosBloqueComp.stream().filter(x -> x.getBloqueComponenteDTO() != null && x.getBloqueComponenteDTO().getIdBloqueComponente()
                    .equals(bloqueComponenteDTO.getIdBloqueComponente()) 
                    && x.getDatoDTO() !=null && x.getDatoDTO().getIdDato() != null 
                    && Objects.isNull(x.getFcBajaFila())).collect(Collectors.toList());
            bloqueComponenteDTO.setDocumentos(this.rellenarDocumentosExpediente(listaDocuemntosBloqueComp));
        }
        
    }

}


public String toUnicodeEscaped(String input) {
    if (input == null) {
        return null;
    }
    try {
        log.info(input);
        String nombreFichero = new String(input.getBytes("ISO-8859-1"), "UTF-8");
        log.info(nombreFichero);
        
        return nombreFichero;
    } catch (UnsupportedEncodingException e) {
        return input;
    }
}

/*public String toUnicodeEscaped(String input) {
    StringBuilder sb = new StringBuilder();
    for (char c : input.toCharArray()) {
        if (c > 127) {
            sb.append(String.format("\\u%04x", (int) c));
        } else {
            sb.append(c);
        }
    }
    return sb.toString();
}*/

    @SuppressWarnings("unchecked")
    @Override
    public List<ExpedienteDTO> obtenerExpedienteEstructurado(List<Integer> lstIdSolicitud, int index, int pageSize,
            Object orderBean, Boolean mostrarIniciadas) throws ServiceException {
        return this.obtenerExpedienteEstructurado(lstIdSolicitud, index, pageSize, orderBean,
                mostrarIniciadas, null);
    }

    /**
     * VMM 16/10/26 MD-3125: igual que el anterior pero, si se indica lstEstadoExpediente, devuelve SOLO los
     * expedientes (solicitante/coordinador y sus participantes) cuyo estado esta en esa lista. Antes el filtro
     * de estado de expediente de solapaSolicitudes solo acotaba las solicitudes (las que tuvieran algun
     * expediente en esos estados) y luego se listaban todos los expedientes de esas solicitudes, con lo que
     * salian expedientes en otros estados.
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ExpedienteDTO> obtenerExpedienteEstructurado(List<Integer> lstIdSolicitud, int index, int pageSize,
            Object orderBean, Boolean mostrarIniciadas, List<Integer> lstEstadoExpediente) throws ServiceException {

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort("idExpediente", true));

        // Configuramos lista de tipo de actores de primer nivel
        List<Integer> lstActores = new ArrayList<>();
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR));
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE));
        
        List<AtlasFilter> filtrosExpediente = new ArrayList<>();
        
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                lstIdSolicitud, Operador.IN));
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                        lstActores, Operador.IN));
        
        //VMM MD-2395 27/01/26: quitamos avisos de los expdtes no aceptados, iniciados, propuestos o rechazadosm solo si la solicitud esta "en tramitacion"       
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        
        if (!Boolean.TRUE.equals(mostrarIniciadas)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                    Operador.NOT_IN));
        }
        
        // VMM 16/10/26 MD-3125: filtro por estado del expediente (se combina con el de arriba)
        if (lstEstadoExpediente != null && !lstEstadoExpediente.isEmpty()) {
            filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    lstEstadoExpediente, Operador.IN));
        }

        List<AtlasSort> ordeExpediente = null;
        if (Objects.isNull(orderBean)) {
            ordeExpediente = ordenamiento;
        }
        else {
            ordeExpediente = (List<AtlasSort>) orderBean;
        }
        
        List<ExpedienteDTO> lstExpedienteDTO = this.buscar(index, pageSize, filtrosExpediente, ordeExpediente);
    
        List<ExpedienteDTO> lstParticipanteSolicitanteDTO = new ArrayList<>();
    
        // Iteramos la lista de expedientes solicitante y coordinadores (y
        // participantes si asi se indica)
        for (ExpedienteDTO expDTO : lstExpedienteDTO) {
    
            List<ConvocatoriaActDTO> lstActoresParticipante = new ArrayList<>();
    
            // Si es expediente coordinador recuperamos la lista de actores
            // participantes del coordinador
            if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesCoordinador(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }
            // Si es expediente solicitante recuperamos la lista de actores
            // participantes del solicitante
            // y si de aqui cuelga un coordinador que a su vez tiene un
            // participante??
            else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                lstActoresParticipante = this.convocatoriaActService
                        .obtenerParticipantesSolicitante(
                                expDTO.getConvocatoriaActDTO().getIdActor());
            }
    
            // Si existen actores participante asociados
            if (!lstActoresParticipante.isEmpty()) {
    
                // Obtenemos la lista de idActores participantes
                List<Integer> lstIdActores = new ArrayList<>();
                for (ConvocatoriaActDTO convAct : lstActoresParticipante) {
                    lstIdActores.add(convAct.getIdActor());
                }
    
                // Obtenemos la lista de los expedientes participantes hijos
                List<AtlasFilter> filtrosEntrada = new ArrayList<>();
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                        expDTO.getIdSolicitud(), Operador.EQUALS));
                filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosEntrada
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR,
                                lstIdActores, Operador.IN));
                
                //VMM MD-2395 27/01/26: quitamos avisos de los expdtes no aceptados, iniciados, propuestos o rechazadosm solo si la solicitud esta "en tramitacion"
                SolicitudDTO solicitudDTO = this.solicitudService.buscarId(expDTO.getIdSolicitud());
                Boolean solicitudEnTramitacion = !solicitudDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

                if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas    
                    //quitamos los exps iniciados, propuestos, no aceptados o rechazados
                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                            Operador.NOT_IN));
                }
                
                // VMM 16/10/26 MD-3125: tambien los participantes se filtran por el estado indicado
                if (lstEstadoExpediente != null && !lstEstadoExpediente.isEmpty()) {
                    filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                            lstEstadoExpediente, Operador.IN));
                }

                //VMM 28/01/26 MD-2063: ordenamos los expedientes participantes igual que los del solicitante
                List<ExpedienteDTO> lstExpPartic = this.buscar(0, -1, filtrosEntrada, ordeExpediente);
    
                // Si existen expediente participantes hijos recuperados
                if (!lstExpPartic.isEmpty()) {
    
                    // Si son participantes de coordinador
                    if (Boolean.TRUE.equals(expDTO.getEsExpedienteCoordinador())) {
                        // Agregamos a la lista de participantes (2do nivel)
                        expDTO.setLstExpedienteParticipante(lstExpPartic); // no
                                                                           // hace
                                                                           // nada
                                                                           // con
                                                                           // esto???
    
                        // Si son participantes de solicitante
                    } else if (Boolean.TRUE.equals(expDTO.getEsExpedienteSolicitante())) {
                        // Agregamos la lista de participantes (1er nivel)
                        lstParticipanteSolicitanteDTO.addAll(lstExpPartic);
                    }
                }
            }
    
        }
    
        // Si existen participantes de solicitante
        if (!lstParticipanteSolicitanteDTO.isEmpty()) {
            // Agregamos la lista de participantes a la lista principal
            lstExpedienteDTO.addAll(lstParticipanteSolicitanteDTO);
        }
    
        return lstExpedienteDTO;
        
    }
    
    @Override
    public Integer countExpedienteEstructurado(List<Integer> lstIdSolicitud, Boolean mostrarIniciadas) throws ServiceException {
        return this.countExpedienteEstructurado(lstIdSolicitud, mostrarIniciadas, null);
    }

    /**
     * VMM 16/10/26 MD-3125: igual que el anterior pero, si se indica lstEstadoExpediente, cuenta solo los
     * expedientes cuyo estado esta en esa lista (ver obtenerExpedienteEstructurado con el mismo parametro)
     */
    @Override
    public Integer countExpedienteEstructurado(List<Integer> lstIdSolicitud, Boolean mostrarIniciadas,
            List<Integer> lstEstadoExpediente) throws ServiceException {

        // Configuramos lista de tipo de actores de primer nivel
        List<Integer> lstActores = new ArrayList<>();
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE));
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR));
        lstActores.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE));
        
        List<AtlasFilter> filtrosExpediente = new ArrayList<>();
        
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                lstIdSolicitud, Operador.IN));
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_TIPOACTOR_ID_VALOR,
                        lstActores, Operador.IN));
        
        //VMM MD-2395 27/01/26: quitamos avisos de los expdtes no aceptados, iniciados, propuestos o rechazadosm solo si la solicitud esta "en tramitacion"       
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        
        if (!Boolean.TRUE.equals(mostrarIniciadas)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                    Operador.NOT_IN));
        }
        
        // VMM 16/10/26 MD-3125: filtro por estado del expediente (se combina con el de arriba)
        if (lstEstadoExpediente != null && !lstEstadoExpediente.isEmpty()) {
            filtrosExpediente.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    lstEstadoExpediente, Operador.IN));
        }

        List<AtlasSort> ordeExpediente = new ArrayList<>();

        List<ExpedienteDTO> lstExpedienteDTO = this.buscar(0, -1, filtrosExpediente, ordeExpediente);
    
        return lstExpedienteDTO.size();
        
    }
    
    @Override
    public Date obtenerFcPresentacionBySolicitud(Integer idSolicitud, Integer idExpediente) throws ServiceException{
        
        return this.expedienteDAO.obtenerFcPresentacionBySolicitud(idSolicitud, idExpediente);
        
        
    }
    
    @Override
    public Map<Integer, Date> obtenerFcPresentacionPorLote(List<Integer> idsExpediente){

        return this.expedienteDAO.obtenerFcPresentacionPorLote(idsExpediente);


    }

    @Override
    public Map<Integer, Object[]> obtenerEstadosExpedienteSolicitantePorLote(List<Integer> idsSolicitud) {
        return this.expedienteDAO.obtenerEstadosExpedienteSolicitantePorLote(idsSolicitud);
    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO resolver(Integer idConvocatoria, List<String> listMensajesError) throws ServiceException {
        Boolean existeError = Boolean.FALSE;
        Convocatoria convocatoria = null;
        
       try { 
            if (Objects.isNull(idConvocatoria)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }
            convocatoria = this.convocatoriaDAO.find(idConvocatoria);
    
            if (Objects.nonNull(convocatoria)) {
                
                //VMM 11/06/25 MD-1634: antes de poner la convocatoria como Resuelta
                //comprobamos que el resto de impresos de (justec, juesco, contratos, etc) tengan bloques/componentes y esten bien configurados        
                List<ImpresoDTO> lstImpreso = this.impresoService
                                            .buscarPorIdConvocatoria(idConvocatoria);
                
                //filtramos por todos los impresos excepto los de solicitud y complementarios
                lstImpreso = lstImpreso.stream().filter(imp -> !imp.getTipoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD)) && 
                            !imp.getTipoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)))
                            .collect(Collectors.toList());
                
               //VMM 11/06/25 MD-1634: refactorizamos esta comprobación de configuración de formularios
               // validamos si existe algun formulario sin bloques
               existeError = this.comprobarConfigOtrosImpresos(convocatoria, existeError, lstImpreso, listMensajesError);
               
               if (existeError.equals(Boolean.FALSE)) {
                   
                    //al ser resolución conjunta se cogen todos los expedientes de la convocatoria
                    List<Expediente> lstExpediente = this.expedienteDAO
                            .listaExpedientePorConvocatoria(convocatoria.getIdConvocatoria());
                    
                    List<ExpedienteDTO> lstExpedientedto = new ArrayList<>();
                    for (Expediente expediente : lstExpediente) {
         
                        lstExpedientedto.add(ExpedienteToDTO.INSTANCE.apply(expediente));
                    }
         
                    //VMM 12/06/25: contemplar si es de concesion participante que coja todos los expdtes (no hay inci de esto)
                    if (convocatoria.getIdTipoConcesion() != null && convocatoria.getIdTipoConcesion().getNmConstante() != null
                            && convocatoria.getIdTipoConcesion().getNmConstante().equals(Constantes.TipoConcesion.CONCESION_SOLICITANTE)) {
                        
                        //nos quedamos solo con los expdtes de solicitante
                        lstExpedientedto.removeIf(expedienteDTO -> expedienteDTO.getEsExpedienteSolicitante() == Boolean.FALSE);
                    }
                    //else, no filtro porque tengo que revisar todos los expdtes
                    
                    List<Integer> lstEstadoFinalizados = new ArrayList<>();
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO));
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RENUNCIA_EXPRESA));
                    lstEstadoFinalizados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
        
                    if (!lstExpedientedto.isEmpty()) {
                        //buscamos si existen expdtes en estados no finales
                        Boolean existeRegistroSinFinalizar = lstExpedientedto.stream()
                                .anyMatch(expediente -> !lstEstadoFinalizados
                                        .contains(expediente.getEstadoDTO().getIdValor()));
        
                        if (Boolean.TRUE.equals(existeRegistroSinFinalizar)) {
                            throw new ServiceException(Constantes.Error.ERROR_RESOLVER_CONVOCATORIA, null,
                                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
                        } else {
                            
                            //VMM 27/06/25 MD-1635: solo le volcamos los formularios extraordinarios a los expdtes que esten concedidos definitivos
                            lstExpedientedto.removeIf(expedienteDTO -> !expedienteDTO.getEstadoDTO().getIdValor().
                                    equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO)));
                            
                            //VMM 12/06/25 MD-1635: generacion de formularios especificos en cada expdte de la convocatoria cuando se hace la concesión
                            //lo comento hasta que se pueda subir con el resto de desarrollos de otros modulos
                            
                            for(ExpedienteDTO expDTO : lstExpedientedto){
                                                               
                                //obtener cual es su SolicitudDTO y su actor asociado (se supone que esta activa porque no mira la fcBaja)
                                SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(expDTO.getIdExpediente());
                                //puede que esta entidad ya este rellena en solicitudDTO o en ExpdteDTO (comprobar)
                                ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expDTO.getConvocatoriaActDTO().getIdActor());
                                       
                                if (solicitudDTO != null && convocatoriaActDTO != null) {
                                    this.crearOtrosImpresosExpdte(solicitudDTO, convocatoriaActDTO, expDTO,convocatoria);
                                }
                            }
                            
                            Integer idEstConvResu = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA);
                            
                            CatalogosValores catalogosValores = this.catalogosValoresService
                                    .find(idEstConvResu);
                            
                            convocatoria.setIdEstado(new CatalogosValores(idEstConvResu));
                            convocatoria = this.convocatoriaDAO.merge(convocatoria);
                            convocatoria.getIdEstado().setDsValor(catalogosValores.getDsValor());
                        }
                    }
                }
            }
        
       }catch (ServiceException e) {
           
           if (Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA.equals(e.getKeyExcepcion())) {
               throw new ServiceException(e.getMessage(), null,
                       Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
           } else {
               throw new ServiceException(e);
           }
           
       }
       
       return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }
    
    
    @Override
    public Boolean comprobarConfigOtrosImpresos(Convocatoria convocatoria, Boolean existeError,
            List<ImpresoDTO> lstImpreso, List<String> listMensajesError) throws ServiceException {
        
        // validamos si existe algun formulario incompleto de datos
        for (ImpresoDTO impDTO : lstImpreso) {
            if (StringUtils.isBlank(impDTO.getCdIdentificacion())
                    || StringUtils.isBlank(impDTO.getDsDescripcion())) {
                listMensajesError.add("Formularios: Existe algún formulario sin completar datos. Formulario: " 
                        + impDTO.getTipoDTO().getDsValor() + " - " + (impDTO.getDsDescripcion() == null ? "" : impDTO.getDsDescripcion()));
                existeError = Boolean.TRUE;
                break;
            }
 
            // validamos si existe algun formulario sin bloques
            if (impDTO.getLstBloqueDTO().isEmpty()) {
                listMensajesError.add("Formularios: Existe algún formulario sin bloques. Formulario: " + impDTO.getTipoDTO().getDsValor() + " - " 
                                + (impDTO.getDsDescripcion() == null ? "" : impDTO.getDsDescripcion()));
                existeError = Boolean.TRUE;
                break;
            }else {
                // faltaria comprobar si todos los bloques de algun impreso estan dados de baja --> error
                // o lo que es lo mismo, filtrar los bloques dados de alta de cada impreso, si la lista es vacia --> error
                List<BloqueDTO> lstBloqueDTO = impDTO.getLstBloqueDTO().stream()
                        .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());
                
                if (lstBloqueDTO.isEmpty()) {
                    listMensajesError.add("Formularios: Existe algún formulario sin bloques. Formulario: " + impDTO.getTipoDTO().getDsValor() + " - "
                                    + (impDTO.getDsDescripcion() == null ? "" : impDTO.getDsDescripcion()));
                    existeError = Boolean.TRUE;
                    break;
                }
            }

            Iterator<BloqueDTO> itBloque = impDTO.getLstBloqueDTO().iterator();
            String nombreForm = impDTO.getTipoDTO().getDsValor() + " - "
                    + (impDTO.getDsDescripcion() == null ? "" : impDTO.getDsDescripcion());
            String nombreActor = "";
            if (impDTO.getConvocatoriaActDTO() != null && impDTO.getConvocatoriaActDTO().getTlDescripcion() != null) {
                nombreActor = impDTO.getConvocatoriaActDTO().getTlDescripcion();
            }
                
            while (itBloque.hasNext()) {
                BloqueDTO bloqueDTO = itBloque.next();
                
                // comprobamos si ese bloque está activo para seguir comprobando sus componentes
                if (Objects.isNull(bloqueDTO.getFcBajaFila())) {
                
                    //VMM 30/07/24 MD-495: aniadimos el nombre del formulario y actor a esta validacion
                    // validamos contenido de bloques (bloques componentes)
                    existeError = this.comprobarComponentesBloque(bloqueDTO, existeError, nombreForm, nombreActor, listMensajesError);
                }
            }
        }
        
        //VMM 18/06/25 MD-1688: validar que haya apartados en el form de eval JCT
        //esto se hara al resolver (si conjunta) o conceder (si individual)
        existeError = this.comprobarConfiguracionInformeEvaluacionJCT(existeError, convocatoria.getIdConvocatoria(), 
                        convocatoria.getNmMaxPeriodos(), listMensajesError);
        
        if (existeError.equals(Boolean.TRUE)) {
            throw new ServiceException(Constantes.Error.ERROR_RESOLVER_CONVOCATORIA_IMPRESOS_INCOMPLETOS + listMensajesError, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        return existeError;
    }


    /**
     * Se comprueba que los Bloques Formulario, Listado, Tabla, Presupuesto
     * presentado y Ayuda solicitada tienen componentes
     *
     * @param bloqueDTO
     * @param paramExisteError
     * @return
     * @throws ServiceException 
     */
    private Boolean comprobarComponentesBloque(BloqueDTO bloqueDTO, Boolean paramExisteError, String nombreForm, 
            String nombreActor, List<String> listMensajesError) {

        Boolean existeError = paramExisteError;

        Integer idFormato = bloqueDTO.getIdFormato().getIdValor();

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_INICIAL).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_RENUNCIA).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_FORMULARIO_CONTRATO_SUSTITUCION).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_LISTADO).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TABLA).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_PRESENTADO).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PRESUPUESTO_FINANCIABLE).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_AYUDA_SOLICITADA).equals(idFormato)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_DOCUMENTO).equals(idFormato)) {
            
            if (bloqueDTO.getLstBloqueComponenteDTO().isEmpty()) {
                listMensajesError.add("Formularios: Existe algún bloque vacío o sin componentes. Bloque:" +
                        nombreForm + " - " + nombreActor + " - " 
                        + bloqueDTO.getNmOrden() + " - " + bloqueDTO.getIdTematica().getDsValor());
                existeError = Boolean.TRUE;
            }
        }

        return existeError;

    }

    
    @Override
    @Transactional(readOnly = false)
    public void crearOtrosImpresosExpdte(SolicitudDTO solicitudDTO,
            ConvocatoriaActDTO convocatoriaActDTO, ExpedienteDTO expedienteDTO,Convocatoria convocatoria)
            throws ServiceException {
        
        //VMM 12/06/25 MD-1635: generacion de formularios especificos en el expdte cuando se hace la concesión
        InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
        InteresadoDTO interesado = expedienteDTO.getInteresadoDTO();
        
        /******PARTE DE GENERACION DEL EXPDTE**********************/
        
        expedienteDTO = this.generarOtrosImpresos(solicitudDTO, convocatoriaActDTO, expedienteDTO);

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = interesado != null && interesado.getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(interesado.getTipoIdentificacionDTO().getIdValor());

        // Historico
        if (representanteLegal != null
                && representanteLegal.getCdNumIden() != null) {
            this.expedienteHistDAO.crearHistorico(
                    ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                    Utils.valorAuditoria(representanteLegal.getCdNumIden()));
        } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
            this.expedienteHistDAO.crearHistorico(
                    ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                    Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
        } else {
            // Salvaguarda (defensa en profundidad): ver comentario anterior.
            if (interesado != null && interesado.getCdNumIden() != null) {
                this.expedienteHistDAO.crearHistorico(
                        ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                        Utils.valorAuditoria(interesado.getCdNumIden()));
            }
        }
         
         /******PARTE DE PRESENTACION DE LA SOLICITUD **********************/
         
         this.presentarOtrosImpresos(solicitudDTO, expedienteDTO,convocatoria);
    }
    
    
    @Override
    public void crearImpresosAceptacionExpdte(SolicitudDTO solicitudDTO,
            ConvocatoriaActDTO convocatoriaActDTO, ExpedienteDTO expedienteDTO)
            throws ServiceException {
        
        //VMM 12/06/25 MD-1635: generacion de formularios especificos en el expdte cuando se hace la concesión
        InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
        InteresadoDTO interesado = expedienteDTO.getInteresadoDTO();
        
        /******PARTE DE GENERACION DEL EXPDTE**********************/
        
        expedienteDTO = this.generarImpresoAceptacion(solicitudDTO, convocatoriaActDTO, expedienteDTO);

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = interesado != null && interesado.getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(interesado.getTipoIdentificacionDTO().getIdValor());

        // Historico
        if (representanteLegal != null
                && representanteLegal.getCdNumIden() != null) {
            this.expedienteHistDAO.crearHistorico(
                    ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                    Utils.valorAuditoria(representanteLegal.getCdNumIden()));
        } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
            this.expedienteHistDAO.crearHistorico(
                    ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                    Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
        } else {
            // Salvaguarda (defensa en profundidad): ver comentario anterior.
            if (interesado != null && interesado.getCdNumIden() != null) {
                this.expedienteHistDAO.crearHistorico(
                        ExpedienteToEntity.INSTANCE.apply(expedienteDTO),
                        Utils.valorAuditoria(interesado.getCdNumIden()));
            }
        }
         
    } 
    
    
    
    
    
    private void presentarOtrosImpresos(SolicitudDTO solicitudDTO, ExpedienteDTO expedienteDTO,Convocatoria convocatoria)
            throws ServiceException {
        
        List<Integer> tipoImpresoJustecInterm = new ArrayList<>();
          tipoImpresoJustecInterm
                 .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
         List<Integer> tipoImpresoJusEcoInterm = new ArrayList<>();
         tipoImpresoJusEcoInterm
                 .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
         List<Integer> tipoImpresoJustecFinal = new ArrayList<>();
         tipoImpresoJustecFinal.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
         List<Integer> tipoImpresoJusEcoFinal = new ArrayList<>();
         tipoImpresoJusEcoFinal.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
         
        
         
         Integer idEstadoPdtePresentacion = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_PRESENTACION);
         CatalogosValores estado = this.catalogosValoresService.find(idEstadoPdtePresentacion);
         
         Integer idEstadoPdtePresentacionJusEco = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_PRESENTACION);
         CatalogosValores estadoJusEco = this.catalogosValoresService.find(idEstadoPdtePresentacionJusEco);
         
         Integer idTipoJustifCTInterm = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA);
         Integer idTipoJustifCTFinal= utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL);
         Integer idTipoJustifEcoInterm = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.INTERMEDIA);
         Integer idTipoJustifEcoFinal= utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.FINAL);
         
         List<Impreso> lstImpresosJustecInterm = this.impresoService.buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoJustecInterm);
         List<Impreso> lstImpresosJustecFinal = this.impresoService.buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoJustecFinal);
         List<Impreso> lstImpresosJusEcoInterm = this.impresoService.buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoJusEcoInterm);
         List<Impreso> lstImpresosJusEcoFinal = this.impresoService.buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoJusEcoFinal);
        
         
         //obtenemos el DTO de la convocatoria
         ConvocatoriaDTO convDTO = solicitudDTO.getConvocatoriaDTO();
         
         if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                 .equals(expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO().getIdValor())
                 || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE).equals(
                         expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO().getIdValor())) {

             // Replicamos las justificaciones a los expedientes de tipo
             // coordinador o solicitante
             List<AtlasFilter> filtrosE = new ArrayList<>();
             filtrosE.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                     convDTO.getIdConvocatoria(), Operador.EQUALS));
             filtrosE.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

             List<ConvocatoriaJusTec> lstConvocatoriaJusTec = this.convocatoriaJusTecDAO
                     .find(filtrosE, null);
             
             log.info("TEST PRESENTAR: entró en Justec");
             for (ConvocatoriaJusTec convocatoriaJusTec : lstConvocatoriaJusTec) {

                 ExpedienteJusTec expedienteJusTec = new ExpedienteJusTec();
                 expedienteJusTec.setIdExpediente(expedienteDTO.getIdExpediente());
                 expedienteJusTec.setNmPeriodo(convocatoriaJusTec.getNmPeriodo());
                 CatalogosValores tipoExpJustec = this.catalogosValoresService.find(
                         convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor());
                 expedienteJusTec.setTipoExpedienteJusTec(tipoExpJustec);
                 expedienteJusTec
                         .setFcIniJustiCienTec(convocatoriaJusTec.getFcIniJustiCienTec());
                 expedienteJusTec
                         .setFcFinJustiCienTec(convocatoriaJusTec.getFcFinJustiCienTec());
                 expedienteJusTec.setTlObservacion(convocatoriaJusTec.getTlObservacion());               
                 expedienteJusTec.setIdEstado(estado);
                 if(expedienteJusTec.getCdUsuAlta() == null) {
                     expedienteJusTec.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);                    
                 }
                

                 ExpedienteJusTec expedienteJusTecNew = this.expedienteJusTecDAO
                         .insert(expedienteJusTec);
                 this.expedienteJusTecHistDAO.crearHistorico(expedienteJusTec);

                 // generar documentos JCT
                 
                 // POM 22/08/2024 MD-540 [ERROR] SOL_REST Al crearse la
                 // solicitud con JCT, esta duplicando los documentos asociados
                 // Si el tipo de periodo es intermedio es el impresoJustec
                 if (convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor()
                         .equals(idTipoJustifCTInterm) && lstImpresosJustecInterm != null && !lstImpresosJustecInterm.isEmpty()) {
                     
                     expedienteDTO.setImpresoJustecDTO(
                             ImpresoToDTO.INSTANCE.apply(lstImpresosJustecInterm.get(0)));
                     // Ordenamos por nmOrden
                     expedienteDTO.getImpresoJustecDTO().getLstBloqueDTO()
                             .sort(Comparator.comparing(BloqueDTO::getNmOrden));
                     
                     // Insertamos en Saii_datos para el periodo justec
                     this.generarDatosImpresos(expedienteDTO,
                             expedienteDTO.getImpresoJustecDTO(),
                             ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTecNew));

                     // Obtenemos los bloquesComponente que generen documentos solo los que esten activos                     
                     for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJustecDTO()
                             .getLstBloqueDTO()) {
                         
                         // Si está dado de baja lo ignoramos
                         if (bloqueDTO.getFcBajaFila() != null) {
                             continue;
                         }

                         bloqueDTO.getLstBloqueComponenteDTO().sort(
                                 Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                         // Si es bloque es de tipo documento
                         if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                             for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                     .getLstBloqueComponenteDTO()) {

                                 // Si está dado de baja lo ignoramos
                                 if (bloqueComponenteDTO.getFcBajaFila() != null) {
                                     continue;
                                 }

                                 // Creamos un expedienteDoc de este bloque
                                 ExpedienteDocDTO expedienteDoc = this.crearExpedienteDocJustec(expedienteDTO,
                                                 bloqueComponenteDTO);
                                 ExpedienteJustecDoc expedienteJustecDoc = new ExpedienteJustecDoc();
                                 expedienteJustecDoc.setIdExpedienteDoc(
                                         expedienteDoc.getIdExpedienteDoc());
                                 expedienteJustecDoc.setIdExpedienteJusTec(
                                         expedienteJusTecNew.getIdExpedienteJusTec());
                                 expedienteJustecDoc
                                         .setCdUsuAlta(expedienteDTO.getCdUsuAlta());
                                 expedienteJustecDoc.setFcAltaFila(new Date());
                                 expedienteJustecDoc.setCumple("N");
                                 if(expedienteJustecDoc.getCdUsuAlta() == null) {
                                     expedienteJustecDoc.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);                    
                                 }
                                 expedienteJustecDocDAO.insert(expedienteJustecDoc);
                             }
                         }
                     }
                 }
                 
                 // POM 22/08/2024 MD-540 [ERROR] SOL_REST Al crearse la
                 // solicitud con JCT, esta duplicando los documentos asociados
                 // Si el tipo de periodo es final es el impresoJustecFinal
                 if (convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor()
                         .equals(idTipoJustifCTFinal) && lstImpresosJustecFinal != null && !lstImpresosJustecFinal.isEmpty()) {
                     
                     expedienteDTO.setImpresoJustecFinalDTO(
                             ImpresoToDTO.INSTANCE.apply(lstImpresosJustecFinal.get(0)));
                     // Comprobar que viene rellena la lista de bloques
                     // Ordenamos por nmOrden
                     expedienteDTO.getImpresoJustecFinalDTO().getLstBloqueDTO()
                             .sort(Comparator.comparing(BloqueDTO::getNmOrden));

                     // insertamos en Datos los registros de
                     // expedienteJUSTEC correspondientes al periodo
                     this.generarDatosImpresos(expedienteDTO,
                             expedienteDTO.getImpresoJustecFinalDTO(),
                             ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTecNew));

                     // Obtenemos los bloquesComponente que generen documentos
                     for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJustecFinalDTO()
                             .getLstBloqueDTO()) {
                         
                         // Si está dado de baja lo ignoramos
                         if (bloqueDTO.getFcBajaFila() != null) {
                             continue;
                         }

                         bloqueDTO.getLstBloqueComponenteDTO().sort(
                                 Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                         // Si es bloque es de tipo documento
                         if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                             for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                     .getLstBloqueComponenteDTO()) {

                                 // Si está dado de baja lo ignoramos
                                 if (bloqueComponenteDTO.getFcBajaFila() != null) {
                                     continue;
                                 }

                                 // Creamos un expedienteDoc de este bloque
                                 ExpedienteDocDTO expedienteDoc = this.crearExpedienteDocJustec(expedienteDTO,
                                                 bloqueComponenteDTO);
                                 ExpedienteJustecDoc expedienteJustecDoc = new ExpedienteJustecDoc();
                                 expedienteJustecDoc.setIdExpedienteDoc(
                                         expedienteDoc.getIdExpedienteDoc());
                                 expedienteJustecDoc.setIdExpedienteJusTec(
                                         expedienteJusTecNew.getIdExpedienteJusTec());
                                 expedienteJustecDoc.setFcAltaFila(new Date());
                                 expedienteJustecDoc.setCumple("N");
                                 if(expedienteJustecDoc.getCdUsuAlta() == null) {
                                     expedienteJustecDoc.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);                    
                                 }
                                 expedienteJustecDocDAO.insert(expedienteJustecDoc);

                             }
                         }
                     }
                 }

             }//fin del for de convocatoriaJustec
             
             log.info("TEST PRESENTAR: pasó Justec");
             
             
             //VMM 23/05/25 MD-1464: configuracion de Justif Economica
             // Replicamos las anualidades/periodos de justificacion economica a los expedientes
             List<ConvocatoriaJusEco> lstConvocatoriaJusEco = this.convocatoriaJusEcoDAO
                     .find(filtrosE, null);

             for (ConvocatoriaJusEco convocatoriaJusEco : lstConvocatoriaJusEco) {

                 ExpedienteJusEco expedienteJusEco = new ExpedienteJusEco();
                 expedienteJusEco.setIdExpediente(expedienteDTO.getIdExpediente());
                 expedienteJusEco.setNmPeriodo(convocatoriaJusEco.getNmPeriodo());
                 CatalogosValores tipoExpJuseco = this.catalogosValoresService.find(
                         convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor());
                 expedienteJusEco.setTipoExpedienteJusEco(tipoExpJuseco);
                 expedienteJusEco.setFcIniJustiEconomica(convocatoriaJusEco.getFcIniJustiEconomica());
                 expedienteJusEco.setFcFinJustiEconomica(convocatoriaJusEco.getFcFinJustiEconomica());
                 expedienteJusEco.setTlObservacion(convocatoriaJusEco.getTlObservacion());
                 expedienteJusEco.setIdEstado(estadoJusEco);
                 if(expedienteJusEco.getCdUsuAlta() == null) {
                     expedienteJusEco.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);                    
                 }
                 
                 ExpedienteJusEco expedienteJusEcoNew = this.expedienteJusEcoDAO.insert(expedienteJusEco);
                 this.expedienteJusEcoHistDAO.crearHistorico(expedienteJusEco);
             
                 // Al crearse la solicitud con JusEco, no duplicar los documentos asociados
                 // Si el tipo de periodo es intermedio es el impresoJusEcoIn
                 if (convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor()
                         .equals(idTipoJustifEcoInterm) && lstImpresosJusEcoInterm != null && !lstImpresosJusEcoInterm.isEmpty()) {
                     
                     expedienteDTO.setImpresoJusEcoDTO(
                             ImpresoToDTO.INSTANCE.apply(lstImpresosJusEcoInterm.get(0)));
                     // Ordenamos por nmOrden
                     expedienteDTO.getImpresoJusEcoDTO().getLstBloqueDTO()
                             .sort(Comparator.comparing(BloqueDTO::getNmOrden));
                     
                     // Insertamos en Saii_datos para el periodo jusEco
                     this.generarDatosImpresosEco(expedienteDTO,
                             expedienteDTO.getImpresoJusEcoDTO(),
                             ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEcoNew));

                     // Obtenemos los bloquesComponente que generen documentos
                     for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJusEcoDTO()
                             .getLstBloqueDTO()) {
                         
                         // Si está dado de baja lo ignoramos
                         if (bloqueDTO.getFcBajaFila() != null) {
                             continue;
                         }

                         bloqueDTO.getLstBloqueComponenteDTO().sort(
                                 Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                         // Si es bloque es de tipo documento
                         if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                             for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                     .getLstBloqueComponenteDTO()) {

                                 // Si está dado de baja lo ignoramos
                                 if (bloqueComponenteDTO.getFcBajaFila() != null) {
                                     continue;
                                 }

                                 
                                 //buscaremos los expedientesPresupMonto para el periodo.
                                 List<ExpedientePresupDTO> listaExpePresup= this.expedientePresupService.buscarPresupIncialExpediente(expedienteDTO.getIdExpediente());
                                 if(listaExpePresup != null && !listaExpePresup.isEmpty()) {
                                     if(convocatoria.getItConceptoPresup() != null && !convocatoria.getItConceptoPresup().equalsIgnoreCase(Constantes.AfirmacionNegacion.S)  ) {
                                         for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                             if(expredientePresup.getLstExpedientePresupMontoDTO() != null && !expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                                 List<ExpedientePresupMontoDTO> expedientePresupMontoDTO=  expredientePresup.getLstExpedientePresupMontoDTO().stream()
                                                         .filter(monto -> monto.getNmAnualidad().equals(expedienteJusEcoNew.getNmPeriodo()-1)).collect(Collectors.toList()); 
                                                 expredientePresup.setLstExpedientePresupMontoDTO(expedientePresupMontoDTO);
                                             }
                                            
                                         }
                                     }else if(convocatoria.getItConceptoPresup() != null) {
                                       for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                           if(expredientePresup.getLstExpedientePresupMontoDTO() != null && !expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                               List<ExpedientePresupMontoDTO> expedientePresupMontoDTO=  expredientePresup.getLstExpedientePresupMontoDTO().stream()
                                                       .filter(monto -> monto.getNmAnualidad().equals(expedienteJusEcoNew.getNmPeriodo())).collect(Collectors.toList()); 
                                               expredientePresup.setLstExpedientePresupMontoDTO(expedientePresupMontoDTO);
                                           }
                                                
                                        }
                                        
                                     }
                                     
                                     for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                         if(expredientePresup.getLstExpedientePresupMontoDTO() != null && ! expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                             for( ExpedientePresupMontoDTO expPresupMonto:expredientePresup.getLstExpedientePresupMontoDTO()) {
                                                 // Creamos un expedienteDoc de este bloque                                 
                                                 ExpedienteDocDTO expedienteDoc = this
                                                         .crearExpedienteDocJusEco(expedienteDTO,
                                                                 bloqueComponenteDTO);
                                                 ExpedienteJusEcoDoc expedienteJusEcoDoc = new ExpedienteJusEcoDoc();
                                                 ExpedienteDoc expediDoc = new ExpedienteDoc();
                                                 expediDoc.setIdExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                                                 expedienteJusEcoDoc.setExpedienteDoc(expediDoc);
                                                 
                                                 ExpedienteJusEco expediJusEco = new ExpedienteJusEco();
                                                 expedienteJusEcoDoc.setExpedienteJusEco(expediJusEco);
                                                 expedienteJusEcoDoc.getExpedienteJusEco().setIdExpedienteJusEco(
                                                         expedienteJusEcoNew.getIdExpedienteJusEco());
                                                 expedienteJusEcoDoc.setFcAltaFila(new Date());
                                                 expedienteJusEcoDoc.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);  
                                                 expedienteJusEcoDoc.setCumple("N");
                                                 ExpedientePresupMonto expedientePresupMonto = new ExpedientePresupMonto();
                                                 expedientePresupMonto.setIdExpedientePresupMonto(expPresupMonto.getIdExpedientePresupMonto());
                                                 expedienteJusEcoDoc.setExpedientePresupMonto(expedientePresupMonto);
                                                 expedienteJusEcoDocDAO.insert(expedienteJusEcoDoc);
                                                 
                                             }
                                         }
                                     }
                                     
                                     
                                     
                                 }
                                  
                             }
                         }
                     }
                 }
             
                 // Al crearse la solicitud con Jus Eco, esta duplicando los documentos asociados
                 // Si el tipo de periodo es final es el impresoJusEcoFinal
                 if (convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor()
                         .equals(idTipoJustifEcoFinal) && lstImpresosJusEcoFinal != null && !lstImpresosJusEcoFinal.isEmpty()) {
                     
                     expedienteDTO.setImpresoJusEcoFinalDTO(
                             ImpresoToDTO.INSTANCE.apply(lstImpresosJusEcoFinal.get(0)));
                     // Comprobar que viene rellena la lista de bloques
                     // Ordenamos por nmOrden
                     expedienteDTO.getImpresoJusEcoFinalDTO().getLstBloqueDTO()
                             .sort(Comparator.comparing(BloqueDTO::getNmOrden));

                     // insertamos en Datos los registros de
                     // expedienteJUSECO correspondientes al periodo
                     this.generarDatosImpresosEco(expedienteDTO,
                             expedienteDTO.getImpresoJusEcoFinalDTO(),
                             ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEcoNew));

                     // Obtenemos los bloquesComponente que generen documentos
                     for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJusEcoFinalDTO()
                             .getLstBloqueDTO()) {

                         // Si está dado de baja lo ignoramos
                         if (bloqueDTO.getFcBajaFila() != null) {
                             continue;
                         }
                         
                         bloqueDTO.getLstBloqueComponenteDTO().sort(
                                 Comparator.comparing(BloqueComponenteDTO::getNmOrden));

                         // Si es bloque es de tipo documento
                         if (Boolean.TRUE.equals(bloqueDTO.getEsTipoDocumento())) {

                             for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                     .getLstBloqueComponenteDTO()) {

                                 // Si está dado de baja lo ignoramos
                                 if (bloqueComponenteDTO.getFcBajaFila() != null) {
                                     continue;
                                 }

                                
                                 //buscaremos los expedientesPresupMonto para el periodo.
                                 List<ExpedientePresupDTO> listaExpePresup= this.expedientePresupService.buscarPresupIncialExpediente(expedienteDTO.getIdExpediente());
                                 if(listaExpePresup != null && !listaExpePresup.isEmpty()) {
                                     if(convocatoria.getItConceptoPresup() != null && !convocatoria.getItConceptoPresup().equalsIgnoreCase(Constantes.AfirmacionNegacion.S)  ) {
                                         for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                             if(expredientePresup.getLstExpedientePresupMontoDTO() != null && !expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                                 List<ExpedientePresupMontoDTO> expedientePresupMontoDTO= expredientePresup.getLstExpedientePresupMontoDTO().stream()
                                                         .filter(monto -> monto.getNmAnualidad().equals(expedienteJusEcoNew.getNmPeriodo()-1)).collect(Collectors.toList()); 
                                                 expredientePresup.setLstExpedientePresupMontoDTO(expedientePresupMontoDTO);
                                             }
                                             
                                         }
                                     }else if(convocatoria.getItConceptoPresup() != null) {
                                       for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                           if(expredientePresup.getLstExpedientePresupMontoDTO() != null && !expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                               List<ExpedientePresupMontoDTO> expedientePresupMontoDTO=  expredientePresup.getLstExpedientePresupMontoDTO().stream()
                                                       .filter(monto -> monto.getNmAnualidad().equals(expedienteJusEcoNew.getNmPeriodo())).collect(Collectors.toList()); 
                                               expredientePresup.setLstExpedientePresupMontoDTO(expedientePresupMontoDTO);
                                           }
                                                
                                        }
                                        
                                     }
                                     
                                     for(ExpedientePresupDTO expredientePresup:listaExpePresup) {
                                         if(expredientePresup.getLstExpedientePresupMontoDTO() != null && ! expredientePresup.getLstExpedientePresupMontoDTO().isEmpty()) {
                                             for( ExpedientePresupMontoDTO expPresupMonto:expredientePresup.getLstExpedientePresupMontoDTO()) {
                                                 // Creamos un expedienteDoc de este bloque
                                                 ExpedienteDocDTO expedienteDoc = this
                                                         .crearExpedienteDocJusEco(expedienteDTO,
                                                                 bloqueComponenteDTO);
                                                 ExpedienteJusEcoDoc expedienteJusEcoDoc = new ExpedienteJusEcoDoc();
                                                 ExpedienteDoc expediDoc = new ExpedienteDoc();
                                                 expediDoc.setIdExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                                                 expedienteJusEcoDoc.setExpedienteDoc(expediDoc);
                                                 
                                                 ExpedienteJusEco expediJusEco = new ExpedienteJusEco();
                                                 expedienteJusEcoDoc.setExpedienteJusEco(expediJusEco);
                                                 expedienteJusEcoDoc.getExpedienteJusEco().setIdExpedienteJusEco(
                                                         expedienteJusEcoNew.getIdExpedienteJusEco());
                                                 expedienteJusEcoDoc.setFcAltaFila(new Date());
                                                 expedienteJusEcoDoc.setCdUsuAlta(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);  
                                                 expedienteJusEcoDoc.setCumple("N");
                                                 ExpedientePresupMonto expedientePresupMonto = new ExpedientePresupMonto();
                                                 expedientePresupMonto.setIdExpedientePresupMonto(expPresupMonto.getIdExpedientePresupMonto());
                                                 expedienteJusEcoDoc.setExpedientePresupMonto(expedientePresupMonto);
                                                 expedienteJusEcoDocDAO.insert(expedienteJusEcoDoc);
                                                 
                                             }
                                         }
                                     }
                                 }
                                  
                                 
                             }
                         }
                     }
                 }
             }  //fin del for de convocatoriaJusEco
             
             log.info("TEST PRESENTAR: pasó JusEco ");
             
         }//fin del for de Solic y Coord
    }

    
    private ExpedienteDTO generarOtrosImpresos(SolicitudDTO solicitudDTO,
            ConvocatoriaActDTO convocatoriaActDTO, ExpedienteDTO expedienteDTO)
            throws ServiceException {
        
        ImpresoDTO impresoModPresPlazosOtrosDTO = null;
        List<Integer> lstModPresPlazosOtrosDTO = new ArrayList<>();
        if (solicitudDTO.getConvocatoriaDTO().getItPartidasPresupuestarias()
                .equals(Constantes.AfirmacionNegacion.S)) {
            lstModPresPlazosOtrosDTO.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
            impresoModPresPlazosOtrosDTO = this.impresoService.buscarPorConvActor(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstModPresPlazosOtrosDTO);
        }

        // Asignamos el impreso
        if (impresoModPresPlazosOtrosDTO != null
                && impresoModPresPlazosOtrosDTO.getIdImpreso() != null) {
            expedienteDTO
                    .setImpresoModificacionesPresupPlazosOtrosDTO(impresoModPresPlazosOtrosDTO);
        } else {
            expedienteDTO.setImpresoModificacionesPresupPlazosOtrosDTO(null);
        }

        // Asignamos el formulario de justificacion cientifico técnica
        ImpresoDTO impresoJustecIntermediaDTO = null;
        ImpresoDTO impresoJustecFinalDTO = null;

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                        .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

            List<Integer> lstTipoImpresoI = new ArrayList<>();
            lstTipoImpresoI.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
            impresoJustecIntermediaDTO = this.impresoService.buscarPorConvActor(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoI);

            List<Integer> lstTipoImpresoF = new ArrayList<>();
            lstTipoImpresoF.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
            impresoJustecFinalDTO = this.impresoService.buscarPorConvActor(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoF);
            

            if (impresoJustecIntermediaDTO != null
                    && impresoJustecIntermediaDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoJustecDTO(impresoJustecIntermediaDTO);
            } else {
                expedienteDTO.setImpresoJustecDTO(null);
            }

            if (impresoJustecFinalDTO != null && impresoJustecFinalDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoJustecFinalDTO(impresoJustecFinalDTO);
            } else {
                expedienteDTO.setImpresoJustecFinalDTO(null);
            }
            

        }

        //VMM 26/05/25 MD-1566: Asignamos el formulario de justificacion Economica
        ImpresoDTO impresoJustecEcoIntermediaDTO = null;
        ImpresoDTO impresoJustecEcoFinalDTO = null;

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                        .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

            List<Integer> lstTipoImpresoIEco = new ArrayList<>();
            lstTipoImpresoIEco.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
            impresoJustecEcoIntermediaDTO = this.impresoService.buscarPorConvActor(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoIEco);

            List<Integer> lstTipoImpresoFEco = new ArrayList<>();
            lstTipoImpresoFEco.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
            impresoJustecEcoFinalDTO = this.impresoService.buscarPorConvActor(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoFEco);

            if (impresoJustecEcoIntermediaDTO != null
                    && impresoJustecEcoIntermediaDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoJusEcoDTO(impresoJustecEcoIntermediaDTO);
            } else {
                expedienteDTO.setImpresoJusEcoDTO(null);
            }

            if (impresoJustecEcoFinalDTO != null
                    && impresoJustecEcoFinalDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoJusEcoFinalDTO(impresoJustecEcoFinalDTO);
            } else {
                expedienteDTO.setImpresoJusEcoFinalDTO(null);
            }
        }

        // VMM 23/07/24 MD-475: anyadimos que tambien puede ser actor
        // coordinador para contratos
        // POM 19/08/24 MD-556 Cambiamoo metodo para obtener especificamente
        // impreso de contrato
        // Asignamos el formulario de contrato
        ImpresoDTO impresoContratoDTO = null;
        ImpresoDTO impresoContratoRenunciaDTO = null;
        ImpresoDTO impresoContratoSutitucionDTO = null;
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE)
                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                        .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {

            List<Integer> lstTipoImpresoContrato = new ArrayList<>();
            lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));

            impresoContratoDTO = this.impresoService.buscarPorConvActorContrato(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);

            lstTipoImpresoContrato = new ArrayList<>();
            lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));

            impresoContratoRenunciaDTO = this.impresoService.buscarPorConvActorContrato(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);

            lstTipoImpresoContrato = new ArrayList<>();
            lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));

            impresoContratoSutitucionDTO = this.impresoService.buscarPorConvActorContrato(
                    solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), lstTipoImpresoContrato);
        }

        // Asignamos el impreso de contrato
        if (impresoContratoDTO != null && impresoContratoDTO.getIdImpreso() != null) {
            expedienteDTO.setImpresoContratoDTO(impresoContratoDTO);
        } else {
            expedienteDTO.setImpresoContratoDTO(null);
        }

        if (impresoContratoRenunciaDTO != null
                && impresoContratoRenunciaDTO.getIdImpreso() != null) {
            expedienteDTO.setImpresoRenunciaSinSutitucionDTO(impresoContratoRenunciaDTO);
        } else {
            expedienteDTO.setImpresoRenunciaSinSutitucionDTO(null);
        }

        if (impresoContratoSutitucionDTO != null
                && impresoContratoSutitucionDTO.getIdImpreso() != null) {
            expedienteDTO.setImpresoRenunciaConSutitucionDTO(impresoContratoSutitucionDTO);
        } else {
            expedienteDTO.setImpresoRenunciaConSutitucionDTO(null);
        }
        
        Integer idExpedientePadre = null;
        ExpedienteDTO expedientePadre = null;
        //en este metodo no nos pasan el idExpedientePadre como en el generar de solicitudService
        if (solicitudDTO.getCdExpedienteSol() != null
                && (convocatoriaActDTO.getTipoActorDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE))
                        || convocatoriaActDTO.getTipoActorDTO().getIdValor()
                                .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)))) {
            idExpedientePadre = solicitudDTO.getCdExpedienteSol().contains("-")
                    ? Integer.valueOf(solicitudDTO.getCdExpedienteSol()
                            .substring(solicitudDTO.getCdExpedienteSol().lastIndexOf('-') + 1))
                    : null;
            expedienteDTO.setIdExpedientePadre(idExpedientePadre); //guardar esto en BBDD (ver si hay que revertir esto y si es correcto este idExpPadre)

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
            if (expedienteDTO.getIdExpedientePadre() != null
                    && (expedienteDTO.getImpresoContratoDTO() == null
                            || (expedienteDTO.getImpresoContratoDTO() != null
                                    && expedienteDTO.getImpresoContratoDTO().getIdImpreso() != null
                                    && expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()
                                            .isEmpty()))) {
                
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                        expedienteDTO.getIdExpedientePadre()));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                List<Expediente> lstExpedientePadre = this.expedienteDAO.find(filtros, null);
                
                if (lstExpedientePadre != null && !lstExpedientePadre.isEmpty()) {
                    expedientePadre = ExpedienteToDTO.INSTANCE.apply(lstExpedientePadre.get(0));
                }
                
                if (expedientePadre != null && expedientePadre.getImpresoContratoDTO() != null
                        && expedientePadre.getImpresoContratoDTO().getIdImpreso() != null
                        && !expedientePadre.getImpresoContratoDTO().getLstBloqueDTO().isEmpty()) {
                    
                    impresoContratoDTO = expedientePadre.getImpresoContratoDTO();
                    expedienteDTO.setImpresoContratoDTO(expedientePadre.getImpresoContratoDTO());
                    impresoContratoSutitucionDTO = expedientePadre
                            .getImpresoRenunciaConSutitucionDTO();
                    expedienteDTO.setImpresoRenunciaConSutitucionDTO(
                            expedientePadre.getImpresoRenunciaConSutitucionDTO());
                    impresoContratoRenunciaDTO = expedientePadre
                            .getImpresoRenunciaSinSutitucionDTO();
                    expedienteDTO.setImpresoRenunciaSinSutitucionDTO(
                            expedientePadre.getImpresoRenunciaSinSutitucionDTO());
                }
            }
        }
        
        //guardamos los datos en el expediente
        //VMM MD-1777 14/07/25: cambiamos por un merge para que no de error el guardado con InsertOrUpdate 
        Expediente expediente = this.getDao().merge(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
        expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);
        
        // restaurar lista de bloques del impreso JCT
        List<Integer> tipoImpresoInter = new ArrayList<>();
        tipoImpresoInter.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
        List<Impreso> lstImpresosInter = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoInter);
        if (impresoJustecIntermediaDTO != null
                 && Boolean.TRUE
                         .equals(expedienteDTO.getImpresoJustecDTO().getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoJustecIntermediaDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosInter != null && !lstImpresosInter.isEmpty()) {
             expedienteDTO
                     .setImpresoJustecDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosInter.get(0)));
             expedienteDTO.getImpresoJustecDTO()
                     .setLstBloqueDTO(impresoJustecIntermediaDTO.getLstBloqueDTO());
         }

         // restaurar lista de bloques del impreso JCT Final
         List<Integer> tipoImpresoFinal = new ArrayList<>();
         tipoImpresoFinal.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
         List<Impreso> lstImpresosFinal = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoFinal);

         if (impresoJustecFinalDTO != null
                 && Boolean.TRUE.equals(
                         expedienteDTO.getImpresoJustecFinalDTO().getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoJustecFinalDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosFinal != null && !lstImpresosFinal.isEmpty()) {
             expedienteDTO.setImpresoJustecFinalDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosFinal.get(0)));
             expedienteDTO.getImpresoJustecFinalDTO()
                     .setLstBloqueDTO(impresoJustecFinalDTO.getLstBloqueDTO());
         }
         
      
         //VMM 26/05/25 MD-1566: restaurar lista de bloques del impreso Just Eco Intermedia
         List<Integer> tipoImpresoInterEco = new ArrayList<>();
         tipoImpresoInterEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
         List<Impreso> lstImpresosInterEco = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoInterEco);
         if (impresoJustecEcoIntermediaDTO != null
                 && Boolean.TRUE
                         .equals(expedienteDTO.getImpresoJusEcoDTO().getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoJustecEcoIntermediaDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosInterEco != null && !lstImpresosInterEco.isEmpty()) {
             expedienteDTO
                     .setImpresoJusEcoDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosInterEco.get(0)));
             expedienteDTO.getImpresoJusEcoDTO()
                     .setLstBloqueDTO(impresoJustecEcoIntermediaDTO.getLstBloqueDTO());
         }

         // restaurar lista de bloques del impreso Just Eco Final
         List<Integer> tipoImpresoFinalEco = new ArrayList<>();
         tipoImpresoFinalEco.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
         List<Impreso> lstImpresosFinalEco = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoFinalEco);

         if (impresoJustecEcoFinalDTO != null
                 && Boolean.TRUE.equals(
                         expedienteDTO.getImpresoJusEcoFinalDTO().getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoJustecEcoFinalDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosFinalEco != null && !lstImpresosFinalEco.isEmpty()) {
             expedienteDTO.setImpresoJusEcoFinalDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosFinalEco.get(0)));
             expedienteDTO.getImpresoJusEcoFinalDTO()
                     .setLstBloqueDTO(impresoJustecEcoFinalDTO.getLstBloqueDTO());
         }

         List<Integer> tipoImpresoContratoInicial = new ArrayList<>();
         tipoImpresoContratoInicial.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
         List<Impreso> lstImpresosContratoInicial = this.impresoService.buscarPorExpedienteTipo(
                 expedienteDTO.getIdExpediente(), tipoImpresoContratoInicial);

         if (impresoContratoDTO != null
                 && Boolean.TRUE.equals(
                         expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoContratoDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosContratoInicial != null && !lstImpresosContratoInicial.isEmpty()
                 && Boolean.FALSE
                         .equals(lstImpresosContratoInicial.get(0).getLstBloque().isEmpty())) {
             expedienteDTO.setImpresoContratoDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosContratoInicial.get(0)));
             expedienteDTO.getImpresoContratoDTO()
                     .setLstBloqueDTO(impresoContratoDTO.getLstBloqueDTO());
         } else if (Boolean.TRUE
                 .equals(expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO().isEmpty())) {
             expedienteDTO.setImpresoContratoDTO(impresoContratoDTO);
         }

         List<Integer> tipoImpresoContratoRenuncia = new ArrayList<>();
         tipoImpresoContratoRenuncia.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));
         List<Impreso> lstImpresosContratoRenuncia = this.impresoService.buscarPorExpedienteTipo(
                 expedienteDTO.getIdExpediente(), tipoImpresoContratoRenuncia);

         if (impresoContratoRenunciaDTO != null
                 && Boolean.TRUE.equals(expedienteDTO.getImpresoRenunciaSinSutitucionDTO()
                         .getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE.equals(impresoContratoRenunciaDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosContratoRenuncia != null && !lstImpresosContratoRenuncia.isEmpty()
                 && Boolean.FALSE
                         .equals(lstImpresosContratoRenuncia.get(0).getLstBloque().isEmpty())) {
             expedienteDTO.setImpresoRenunciaSinSutitucionDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosContratoRenuncia.get(0)));
             expedienteDTO.getImpresoRenunciaSinSutitucionDTO()
                     .setLstBloqueDTO(impresoContratoRenunciaDTO.getLstBloqueDTO());
         }

         List<Integer> tipoImpresoContratoSustitucion = new ArrayList<>();
         tipoImpresoContratoSustitucion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));
         List<Impreso> lstImpresosContratoSustitucion = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(),
                         tipoImpresoContratoSustitucion);
         if (impresoContratoSutitucionDTO != null
                 && Boolean.TRUE.equals(expedienteDTO.getImpresoRenunciaConSutitucionDTO()
                         .getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE
                         .equals(impresoContratoSutitucionDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosContratoSustitucion != null
                 && !lstImpresosContratoSustitucion.isEmpty() && Boolean.FALSE.equals(
                         lstImpresosContratoSustitucion.get(0).getLstBloque().isEmpty())) {
             expedienteDTO.setImpresoRenunciaConSutitucionDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosContratoSustitucion.get(0)));
             expedienteDTO.getImpresoRenunciaConSutitucionDTO()
                     .setLstBloqueDTO(impresoContratoSutitucionDTO.getLstBloqueDTO());
         }

         // restaurar lista de bloques del impreso Modificación
         // presupuestaria, plazos y/u otras
         List<Integer> tipoImpresoModPrespPlazosOtros = new ArrayList<>();
         tipoImpresoModPrespPlazosOtros.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
         List<Impreso> lstImpresosModPrespPlazosOtros = this.impresoService
                 .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(),
                         tipoImpresoModPrespPlazosOtros);
         if (impresoModPresPlazosOtrosDTO != null
                 && Boolean.TRUE
                         .equals(expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                                 .getLstBloqueDTO().isEmpty())
                 && Boolean.FALSE
                         .equals(impresoModPresPlazosOtrosDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosModPrespPlazosOtros != null
                 && !lstImpresosModPrespPlazosOtros.isEmpty()) {
             expedienteDTO.setImpresoModificacionesPresupPlazosOtrosDTO(
                     ImpresoToDTO.INSTANCE.apply(lstImpresosModPrespPlazosOtros.get(0)));
             expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                     .setLstBloqueDTO(impresoModPresPlazosOtrosDTO.getLstBloqueDTO());
         }
         
         // Historico
         InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
         InteresadoDTO interesado = expedienteDTO.getInteresadoDTO();

         // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
         // debe usar una constante en lugar de su identificador.
         Boolean esInteresadoExtraComunitario = interesado != null && interesado.getTipoIdentificacionDTO() != null
                 && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                         .equals(interesado.getTipoIdentificacionDTO().getIdValor());

         if (representanteLegal != null && representanteLegal.getCdNumIden() != null) {
             this.expedienteHistDAO.crearHistorico(expediente,
                     Utils.valorAuditoria(representanteLegal.getCdNumIden()));
         } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
             this.expedienteHistDAO.crearHistorico(expediente,
                     Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
         } else {
             // Salvaguarda (defensa en profundidad): ver comentario anterior.
             if (interesado != null && interesado.getCdNumIden() != null) {
                 this.expedienteHistDAO.crearHistorico(expediente, Utils.valorAuditoria(interesado.getCdNumIden()));
             }
         }

         //VMM 25/06/25 MD-1635: añadimos este codigo que faltaba de generarDatosExpediente de ExpedienteService
         expedienteDTO = this.generarDatosExpedienteOtros(expedienteDTO);
         
         if ((impresoContratoDTO != null
             && Boolean.FALSE.equals(impresoContratoDTO.getLstBloqueDTO().isEmpty())
             && lstImpresosContratoInicial != null && !lstImpresosContratoInicial.isEmpty())
             || expedienteDTO.getIdExpedientePadre() != null
                     && (convocatoriaActDTO.getItContrato().equals(Boolean.TRUE))) {
             expedienteDTO = this.generarDocumentosExpedienteContrato(expedienteDTO);
         }
         
                 
        return expedienteDTO;
    }

    private ExpedienteDTO generarDatosExpedienteOtros(ExpedienteDTO expedienteDTO) throws ServiceException {
        
        if (expedienteDTO.getImpresoContratoDTO() != null
                 && expedienteDTO.getImpresoContratoDTO().getIdImpreso() != null
                 && expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO() != null) {

             expedienteDTO.getImpresoContratoDTO().getLstBloqueDTO()
                     .sort(Comparator.comparing(BloqueDTO::getNmOrden));
             ImpresoDTO impresoContrato = expedienteDTO.getImpresoContratoDTO();
             generarDatosImpresos(expedienteDTO, impresoContrato, null);

         }

         if (expedienteDTO.getImpresoRenunciaSinSutitucionDTO() != null
                 && expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getIdImpreso() != null
                 && expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO() != null) {

             expedienteDTO.getImpresoRenunciaSinSutitucionDTO().getLstBloqueDTO()
                     .sort(Comparator.comparing(BloqueDTO::getNmOrden));
             ImpresoDTO impresoContrato = expedienteDTO.getImpresoRenunciaSinSutitucionDTO();
             generarDatosImpresos(expedienteDTO, impresoContrato, null);

         }

         if (expedienteDTO.getImpresoRenunciaConSutitucionDTO() != null
                 && expedienteDTO.getImpresoRenunciaConSutitucionDTO().getIdImpreso() != null
                 && expedienteDTO.getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO() != null) {

             expedienteDTO.getImpresoRenunciaConSutitucionDTO().getLstBloqueDTO()
                     .sort(Comparator.comparing(BloqueDTO::getNmOrden));
             ImpresoDTO impresoContrato = expedienteDTO.getImpresoRenunciaConSutitucionDTO();
             generarDatosImpresos(expedienteDTO, impresoContrato, null);

         }

         if (expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO() != null
                 && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                         .getIdImpreso() != null
                 && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                         .getLstBloqueDTO() != null) {

             expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()
                     .sort(Comparator.comparing(BloqueDTO::getNmOrden));
             ImpresoDTO impresoPrespMod = expedienteDTO
                     .getImpresoModificacionesPresupPlazosOtrosDTO();
             generarDatosImpresos(expedienteDTO, impresoPrespMod, null);

         }
                  
         return expedienteDTO;
    }
    
    
    private Boolean comprobarConfiguracionInformeEvaluacionJCT(Boolean existeError, Integer idConvocatoria, 
            Integer nmPeriodos, List<String> listMensajesError) {
        try {
            //si tiene un periodo o mas buscar el form de JCT de evaluacion final
            if (nmPeriodos >= 1) {
                List<EvaluacionJustecDTO> lstFormEvaluacJCTFinal = this.evaluacionJustecService
                        .obtenerPorConvocatoria(idConvocatoria, Constantes.TipoEvalJustec.FINAL);
    
                if (lstFormEvaluacJCTFinal.isEmpty()
                        || lstFormEvaluacJCTFinal.stream()
                        .filter(p -> p.getListaEvaluacionJustecApartaDTO().isEmpty()).count() > 0) {
                    listMensajesError.add("Formularios de evaluación JCT: Debe crear el formulario de evaluación de la JCT Final.");
                    existeError = Boolean.TRUE;
                }
            }
            
            if (nmPeriodos > 1){

                //si tiene mas de un periodo comprobar si tiene form de evaluacion JCT Intermedia                
                List<EvaluacionJustecDTO> lstFormEvaluacJCTInterm = this.evaluacionJustecService
                        .obtenerPorConvocatoria(idConvocatoria, Constantes.TipoEvalJustec.INTERMEDIA);

                if (lstFormEvaluacJCTInterm.isEmpty()
                        || lstFormEvaluacJCTInterm.stream()
                        .filter(p -> p.getListaEvaluacionJustecApartaDTO().isEmpty()).count() > 0) {
                    listMensajesError.add("Formularios de evaluación JCT: Debe crear el formulario de evaluación de la JCT Intermedia.");
                    existeError = Boolean.TRUE;
                }
            }
        } catch (ServiceException e) {
            //listMensajesError.add(AtlasMessageUtils.getStringFromBundle(ConstantesWeb.Err.ERROR_BBDD));
            existeError = Boolean.TRUE;
        }
        return existeError;
    }
    
    
    /**
     * Devuelve la lista de datos por idExpediente y una lista de tipos de impresos
     *
     * @param idExpediente
     * @param lstTipoImpreso
     * @return List<DatoDTO>
     * @throws ServiceException 
     */
    @Override
    public List<DatoDTO> buscarPorIdExpTipoImpreso(Integer idExpediente, List<Integer> lstTipoImpreso)
            throws ServiceException {
        List<DatoDTO> lstDatoDTO = new ArrayList<>();
        
        // Buscamos los impresos del expediente con los tipos dados
        List<Impreso> lstImpreso = this.impresoService.buscarPorExpedienteTipo(idExpediente, lstTipoImpreso);
        if (lstImpreso == null || lstImpreso.isEmpty()) {
            return lstDatoDTO; // Retorna lista vacía si no hay impresos
        }

        for(Impreso impreso: lstImpreso) {
            
             //buscamos los bloques asociados a esos impresos
             List<BloqueDTO> lstBloqueDTO = this.bloqueService.buscarByIdImpreso(impreso.getIdImpreso());
                    
             if (lstBloqueDTO == null || lstBloqueDTO.isEmpty()) {
                 continue;
             }
             
             for(BloqueDTO bloqueDTO: lstBloqueDTO) {
                 
                 //buscamos los bloquesComponentes asociados a esos bloques
                 List<BloqueComponenteDTO> lstBloqueComponenteDTO = this.bloqueComponenteService.buscarIdBloqueFechBaja(bloqueDTO.getIdBloque());
                 
                 if (lstBloqueComponenteDTO == null || lstBloqueComponenteDTO.isEmpty()) {
                     continue;
                 }
                 
                 for(BloqueComponenteDTO bloqueComDTO: lstBloqueComponenteDTO) {
                     
                     //por ultimo, buscamos los datos por cada bloqueComponente
                     List<DatoDTO> lstDatoAuxDTO = this.datoService.obtenerPorExpediente(idExpediente, bloqueComDTO.getIdBloqueComponente());
                     
                     if (lstDatoAuxDTO != null && !lstDatoAuxDTO.isEmpty()) {
                         lstDatoDTO.addAll(lstDatoAuxDTO);
                     }
                 }
             }
        }
        return lstDatoDTO;
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO conceder(SolicitudDTO solicitudDTO, Integer idExpediente) throws ServiceException {
        
//       ExpedienteDTO expedienteDTO = this.cambiarEstado(idExpediente,
//                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO), null);
       
       
       ExpedienteDTO expedienteDTO = this.buscarId(idExpediente);
       expedienteDTO.setEstadoDTO(new CatalogosValoresDTO( utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_ANTES_ACEPTADO)));            
       expedienteDTO = this.guardar(expedienteDTO);            
       //this.expedienteHistDAO.crearHistorico( ExpedienteToEntity.INSTANCE.apply(expedienteDTO), null);

       // también cambia el estado de la solicitud
       Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
          solicitud.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA)));
          this.solicitudDAO.update(solicitud);
       
           
       ExpedienteDocDTO expeDocDTO = this.getExpedienteDocDTO(
               utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PROPUESTA_RESOLUCION_DEFINITIVA), idExpediente);
       
       expeDocDTO.setTramiteDTO(this.catalogosValoresService
               .buscarId(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA)));
       
       this.expedienteDocService.getDao().merge(ExpedienteDocToEntity.INSTANCE.apply(expeDocDTO));
       this.expedienteDocHistDAO.crearHistorico(ExpedienteDocToEntity.INSTANCE.apply(expeDocDTO), null);
       
        
        //VMM 12/06/25 MD-1635: generacion de formularios especificos en el expdte cuando se hace la concesión
        //lo comento hasta que se pueda subir con el resto de desarrollos de otros modulos
        //lo mismo este objeto lo tengo en SolicitudDTO o en expDTO...comprobar
        ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expedienteDTO.getConvocatoriaActDTO().getIdActor());
        
        if (convocatoriaActDTO != null) {   
            this.crearImpresosAceptacionExpdte(solicitudDTO, convocatoriaActDTO, expedienteDTO);
           
        }
        
        return expedienteDTO;
    }
    
    @Override
    public ExpedienteDocDTO getExpedienteDocDTO(Integer tipoDocumento, Integer idExpediente) {

        ExpedienteDocDTO expeDocDTO = new ExpedienteDocDTO();

        try {

            List<AtlasFilter> filtroExpeDocProv = new ArrayList<>();
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtroExpeDocProv
                    .add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                            tipoDocumento, Operador.EQUALS));
            filtroExpeDocProv.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    idExpediente, Operador.EQUALS));
            List<AtlasSort> ordenamientoHist = new ArrayList<>();
            ordenamientoHist.add(new AtlasSort(Constantes.Filtro.FILTRO_FECHA_ALTA, false));
            
            List<ExpedienteDocDTO> lstExpeDocDTO = this.expedienteDocService.buscar(0, -1,
                    filtroExpeDocProv, ordenamientoHist);
            
            if (lstExpeDocDTO != null && !lstExpeDocDTO.isEmpty() && lstExpeDocDTO.get(0) != null) {
                expeDocDTO = lstExpeDocDTO.get(0);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return expeDocDTO;

    }
    
    
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> revertirExpedienteCompleto(Integer idExpediente, Integer idSolicitud, 
            ExpedienteHistDTO registroHcoEstadoAnterior) throws ServiceException {
        Map<String, Object> result = new HashMap<>();

        // 1. Revertimos el estado del expediente
        ExpedienteDTO expedienteDTO = this.revertirEstadosExpediente(idExpediente, registroHcoEstadoAnterior, null);
        result.put("expedienteDTO", expedienteDTO);

        // 2. Cambiamos el estado de la solicitud
        // MD-1503 JRFM - Dejamos el estado de la solicitud en Presentada por la entidad solicitante
        SolicitudDTO solicitudDTO = this.solicitudService.cambiarEstado(idSolicitud,
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
        result.put("solicitudDTO", solicitudDTO);

        // 3. Revertimos otros impresos
        //VMM 27/06/25 MD-1635: revertimos o borramos fisicamente los datos volcados de los formularios extraordinarios
        
        this.revertirOtrosImpresosExpediente(idExpediente);

        return result;
    }
    
    /**
     * Metodo para actualizar el estado de la financiacion de un expediente
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO cambiarEstadoExpFinan(Integer idEstadoFinan, ExpedienteDTO dtoAux) throws ServiceException {

        ExpedienteDTO dto = null;
        //Expediente exp = this.getDao().find(dto.getIdExpediente());
        Expediente exp = ExpedienteToEntity.INSTANCE.apply(dtoAux); 
        CatalogosValores estadoFinan = this.catalogosValoresService.find(
                utilConstantes.getGlobalCatalogosValores().get(idEstadoFinan));
        //auditoria
        exp.setCdUsuModif(UserUtils.getUserName());
        exp.setFcModifFila(new Date());
        exp.setEstadoRevFinan(estadoFinan);
        exp = this.expedienteDAO.merge(exp);
        //crear historico provisional     
        this.expedienteHistDAO.crearHistorico(exp, exp.getCdUsuModif());
               
        dto = ExpedienteToDTO.INSTANCE.apply(exp);
        
        // MD-2203 recuperamos la lista de requisitos
        dto.setLstExpedienteReqDTO(dtoAux.getLstExpedienteReqDTO());
        
        return dto;
    }
    
    
    private ExpedienteDTO generarImpresoAceptacion(SolicitudDTO solicitudDTO,
            ConvocatoriaActDTO convocatoriaActDTO, ExpedienteDTO expedienteDTO)
            throws ServiceException {
       
        ImpresoDTO impresosAceptacionDTO= null;

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE)
                .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                        .equals(convocatoriaActDTO.getTipoActorDTO().getIdValor())) {
            
            List<Integer> tipoImpresoAceptacion = new ArrayList<>();
            tipoImpresoAceptacion
                   .add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));
            
            impresosAceptacionDTO = this.impresoService.buscarPorConvActor(solicitudDTO.getConvocatoriaDTO().getIdConvocatoria(),
                    convocatoriaActDTO.getIdActor(), tipoImpresoAceptacion);


            
            if (impresosAceptacionDTO != null
                    && impresosAceptacionDTO.getIdImpreso() != null) {
                expedienteDTO.setImpresoAceptacionDTO(impresosAceptacionDTO);
            } else {
                expedienteDTO.setImpresoAceptacionDTO(null);
            }

        }

        //guardamos los datos en el expediente
        //VMM MD-1777 14/07/25: cambiamos por un merge para que no de error el guardado con InsertOrUpdate 
        Expediente expediente = this.getDao().merge(ExpedienteToEntity.INSTANCE.apply(expedienteDTO));
        expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);
                
         // restaurar lista de bloques del impreso aceptacion
         List<Integer> tipoImpresoAceptacion = new ArrayList<>();
         tipoImpresoAceptacion.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));
         List<Impreso> lstImpresosAceptacion = this.impresoService
                  .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), tipoImpresoAceptacion);
         if (impresosAceptacionDTO != null
                  && Boolean.TRUE
                          .equals(expedienteDTO.getImpresoAceptacionDTO().getLstBloqueDTO().isEmpty())
                  && Boolean.FALSE.equals(impresosAceptacionDTO.getLstBloqueDTO().isEmpty())
                  && lstImpresosAceptacion != null && !lstImpresosAceptacion.isEmpty()) {
              expedienteDTO
                      .setImpresoAceptacionDTO(ImpresoToDTO.INSTANCE.apply(lstImpresosAceptacion.get(0)));
              expedienteDTO.getImpresoAceptacionDTO()
                      .setLstBloqueDTO(impresosAceptacionDTO.getLstBloqueDTO());
          }

         
         // Historico
         InteresadoDTO representanteLegal = expedienteDTO.getRepresentanteLegal();
         InteresadoDTO interesado = expedienteDTO.getInteresadoDTO();

         // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
         // debe usar una constante en lugar de su identificador.
         Boolean esInteresadoExtraComunitario = interesado != null && interesado.getTipoIdentificacionDTO() != null
                 && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                         .equals(interesado.getTipoIdentificacionDTO().getIdValor());

         if (representanteLegal != null && representanteLegal.getCdNumIden() != null) {
             this.expedienteHistDAO.crearHistorico(expediente,
                     Utils.valorAuditoria(representanteLegal.getCdNumIden()));
         } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
             this.expedienteHistDAO.crearHistorico(expediente,
                     Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
         } else {
             // Salvaguarda (defensa en profundidad): ver comentario anterior.
             if (interesado != null && interesado.getCdNumIden() != null) {
                 this.expedienteHistDAO.crearHistorico(expediente, Utils.valorAuditoria(interesado.getCdNumIden()));
             }
         }

         expedienteDTO = this.generarDatosExpedienteAceptacion(expedienteDTO);
         
         if ((impresosAceptacionDTO != null
                 && Boolean.FALSE.equals(impresosAceptacionDTO.getLstBloqueDTO().isEmpty())
                 && lstImpresosAceptacion != null && !lstImpresosAceptacion.isEmpty())
                 ) {
                 expedienteDTO = this.generarDocumentosAceptacion(expedienteDTO);
             }
                 
        return expedienteDTO;
    }

private ExpedienteDTO generarDatosExpedienteAceptacion(ExpedienteDTO expedienteDTO) throws ServiceException {
        
        
         if (expedienteDTO.getImpresoAceptacionDTO() != null
                 && expedienteDTO.getImpresoAceptacionDTO()
                         .getIdImpreso() != null
                 && expedienteDTO.getImpresoAceptacionDTO()
                         .getLstBloqueDTO() != null) {

             expedienteDTO.getImpresoAceptacionDTO().getLstBloqueDTO()
                     .sort(Comparator.comparing(BloqueDTO::getNmOrden));
             ImpresoDTO impresoAceptacion = expedienteDTO
                     .getImpresoAceptacionDTO();
             generarDatosImpresos(expedienteDTO, impresoAceptacion, null);

         }
         
         return expedienteDTO;
    }
    

    
    
    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoJE(Expediente expediente,
            ExpedienteJusEcoDTO expedienteJusEcoDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException {

        ExpedienteJusEco expedienteJusEco = null;

        if (expedienteJusEcoDTO != null) {

            CatalogosValoresDTO estado = this.catalogosValoresService.buscarId(idEstadoNuevo);

            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);

            expedienteJusEcoDTO.setIdEstadoDTO(estado);

            expedienteJusEcoDTO.setIdEstadoRevAdminDTO(estadoRev);

            expedienteJusEco = ExpedienteJusEcoToEntity.INSTANCE.apply(expedienteJusEcoDTO);

            expedienteJusEco = this.expedienteJusEcoDAO.merge(expedienteJusEco);

            expediente = this.getDao().merge(expediente);

            this.expedienteHistDAO.crearHistorico(expediente, null);

            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } else {
            return null;
        }
    }
    
    /**
    *
    */
    @Transactional(readOnly = false)
    @Override
    public ExpedienteDTO actualizarEstadoInternoRevisionOkDocumentoJE(Integer idExpediente,
            List<ExpedienteDocDTO> listaDocs, ExpedienteJusEcoDTO expedienteJusEcoSeleccionado)
            throws ServiceException {

        ExpedienteDTO dto = null;
        Expediente exp = this.getDao().find(idExpediente);

        if (listaDocs != null) {

//           ExpedienteDTO expediente = this.buscarId(idExpediente);
//           Convocatoria convocatoria = this.convocatoriaDAO.find(expediente.getConvocatoriaActDTO().getIdConvocatoria());
//
//           if( Objects.equals(convocatoria.getIdTipoConcesion().getIdValor(), Constantes.TipoConcesion.CONCESION_PARTICIPANTE)) {      
//               listaDocs = this.filtrarDocumentosByExpediente(idExpediente,listaDocs );           
//           }

            // Consultamos si hay algun documento diferente de Revision OK
            Boolean pendienteRevisadoOk = listaDocs.stream()
                    .allMatch(f -> (
                            Constantes.EstadoDocumento.REVISION_OK.equals(f.getEstadoDTO().getNmConstante())
                            || (Constantes.EstadoDocumento.SIN_SUBIR.equals(f.getEstadoDTO().getNmConstante()))
                                    && ((f.getBloqueComponenteDTO() != null && Boolean.FALSE
                                            .equals(f.getBloqueComponenteDTO().getItObligatorio()))
                                            || f.getBloqueComponenteDTO() == null)));

            // Si todos estan en Revisión OK actualizamos el estado del
            // expediente
            if (Boolean.TRUE.equals(pendienteRevisadoOk)) {
                dto = this.actualizarEstadoInternoJERevision(exp, expedienteJusEcoSeleccionado,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_EVALUACION),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.REVISION_OK));

                // calcular tramites finalizados
                // this.calcularTramitesFinalizados(dto);

            }
        }

        return dto;
    }
    
    
    /**
     * Metodo para actualizar el estado interno del expediente
     *
     * @param expediente
     * @param idEstadoNuevo
     * @param esRevisionAdmin
     * @param actualizaEstadoExpediente
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoInternoJERevision(Expediente expediente,
            ExpedienteJusEcoDTO expedienteJusEcoDTO, Integer idEstadoNuevo,
            Integer idEstadoNuevoRev) throws ServiceException {

        ExpedienteJusEco expedienteJusEco = null;

        if (expedienteJusEcoDTO != null) {

            CatalogosValoresDTO estado = this.catalogosValoresService.buscarId(idEstadoNuevo);

            CatalogosValoresDTO estadoRev = this.catalogosValoresService.buscarId(idEstadoNuevoRev);

            expedienteJusEcoDTO.setIdEstadoDTO(estado);

            expedienteJusEcoDTO.setIdEstadoRevAdminDTO(estadoRev);

            expedienteJusEco = ExpedienteJusEcoToEntity.INSTANCE.apply(expedienteJusEcoDTO);

            expedienteJusEco = this.expedienteJusEcoDAO.merge(expedienteJusEco);

            expediente = this.getDao().merge(expediente);

            this.expedienteHistDAO.crearHistorico(expediente, null);

            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } else {
            return null;
        }

    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocumentoRequisitoPendienteSubsanar(Integer idExpediente)
            throws ServiceException {
        if (Objects.isNull(idExpediente)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<ExpedienteDocDTO> listaExpedienteDoc = new ArrayList<>();

        List<Integer> estadosDocumento = new ArrayList<>();
        estadosDocumento.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, estadosDocumento,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO), Operador.EQUALS)); // añadir los doc sde subsanacion amdin

        List<ExpedienteDocHist> lstExpedienteDocHist = this.expedienteDocHistDAO.find(filtros,
                null);

        if (!lstExpedienteDocHist.isEmpty()) {

            List<DocumentoPtfr> lstDocumento = lstExpedienteDocHist.stream()
                    .filter(ed -> Objects.nonNull(ed.getIdDocumentoPtfr()))
                    .map(ExpedienteDocHist::getIdDocumentoPtfr).distinct()
                    .map(id -> this.documentoPtfrDAO.find(id)).collect(Collectors.toList());

            int idDocumento = -1;
            if (!lstDocumento.isEmpty()) {
                Date fcEnvio = lstDocumento.get(0).getFcEnvioPtfr();
                idDocumento = lstDocumento.get(0).getIdDocumentoPtfr();
                for (DocumentoPtfr documento : lstDocumento) {
                    if (documento.getFcEnvioPtfr().after(fcEnvio)) {
                        idDocumento = documento.getIdDocumentoPtfr();
                        fcEnvio = documento.getFcEnvioPtfr();
                    }
                }
            }

            for (ExpedienteDocHist expedienteDocHist : lstExpedienteDocHist) {
                
                ExpedienteDocDTO expedienteDocDTO = ExpedienteDocToDTO.INSTANCE
                        .applyFromHistory(expedienteDocHist);
                
                List<AtlasFilter> filtrosExp = new ArrayList<>();
                filtrosExp.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC,
                        expedienteDocHist.getExpedienteDoc().getIdExpedienteDoc(),
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                List<ExpedienteReq> exp = this.expedienteReqDAO.find(filtrosExp, null);

                if (!exp.isEmpty()) {
                    if (expedienteDocDTO != null) {
                        expedienteDocDTO.setNombreFicheroFica(
                                this.recuperarNombreFicheroFica(expedienteDocHist.getCdFica()));
                        expedienteDocDTO.setTlObservaciones(exp.get(0).getTlObservaciones());
                        expedienteDocDTO.setTlObservacionesGestor(exp.get(0).getTlObservacionesCiudadano());
                        expedienteDocDTO.setUltimoEnvio(Boolean.FALSE);
                        if (Objects.nonNull(expedienteDocDTO.getIdDocumentoPtfr()) && idDocumento != -1
                                && expedienteDocDTO.getIdDocumentoPtfr() == idDocumento) {
                            expedienteDocDTO.setUltimoEnvio(Boolean.TRUE);
                        }
                        listaExpedienteDoc.add(expedienteDocDTO);
                    }
                }
            }
        }

        return listaExpedienteDoc;
        
    }
    
    @Override
    @Transactional(readOnly = false)
    public void guardarNotaEvaluacion(Integer idExpediente, Integer nmNota)
            throws ServiceException {
        if (nmNota == null) {
            return;
        }
        Expediente expediente = this.expedienteDAO.find(idExpediente);
        if (!nmNota.equals(expediente.getNmNota())) {
            expediente.setNmNota(nmNota);
            this.getDao().merge(expediente);
            this.expedienteHistDAO.crearHistorico(expediente, null);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO guardarCambioEstadoRevReq(Integer idExpediente, Integer idEstadoRevReq)
            throws ServiceException {
        try {
            Expediente expediente = this.expedienteDAO.find(idExpediente);
            Boolean esCambioEstado = Boolean.FALSE;
            if (!expediente.getEstadoRevRequi().getIdValor().equals(idEstadoRevReq)) {
                esCambioEstado = Boolean.TRUE;
            }
            if (Boolean.TRUE.equals(esCambioEstado)) {
                CatalogosValoresDTO estadoRevInternoDTO = this.catalogosValoresService
                        .buscarId(idEstadoRevReq);
                CatalogosValores cv = CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO);
                expediente.setEstadoRevRequi(cv);
                expediente.setEstado(cv);
                expediente = this.getDao().merge(expediente);
                this.expedienteHistDAO.crearHistorico(expediente, null);
            }
            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoProvisionalExpediente(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException {
        Expediente expediente = this.find(idExpediente);
        CatalogosValores estado = this.catalogosValoresService
                .find(utilConstantes.getGlobalCatalogosValores().get(idEstado));  
        expediente.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(utilConstantes.getGlobalCatalogosValores().get(idEstado)));
        expediente.setEstadoProvisional(estado); 
        expediente = this.getDao().merge(expediente);
        this.expedienteHistDAO.crearHistorico(expediente, dni);
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);
        expedienteDTO.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(estado));
        return expedienteDTO;
        
    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO guardarCambioEstadoRevAdmin(Integer idExpediente, Integer idEstadoRevAdmin)
            throws ServiceException {
        Expediente expediente = this.expedienteDAO.find(idExpediente);
        try {
            Boolean esCambioEstado = Boolean.FALSE;

            if (!expediente.getEstadoRevAdmin().getIdValor().equals(idEstadoRevAdmin)) {
                esCambioEstado = Boolean.TRUE;
            }

            if (Boolean.TRUE.equals(esCambioEstado)) {
                CatalogosValoresDTO estadoRevInternoDTO = this.catalogosValoresService
                        .buscarId(idEstadoRevAdmin);

                expediente.setEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(estadoRevInternoDTO));
                
                if (!expediente.getEstadoRevAdmin().getNmConstante()
                        .equals(Constantes.EstadoExpediente.DESISTIDO_PROVISIONAL)) {
                
                    Integer idEstado = ExpedienteToEntity.INSTANCE.calcularEstadoMayor(expediente);
                    if (idEstado != 0) {
                        CatalogosValoresDTO estadoExpedienteDTO = this.catalogosValoresService
                                .buscarId(idEstado);
                        expediente.setEstado(
                                CatalogosValoresToEntity.INSTANCE.apply(estadoExpedienteDTO));
                    }
                    
                }

                expediente = this.getDao().merge(expediente);

                this.expedienteHistDAO.crearHistorico(expediente, null);
            }
            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }

    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoYEstadoProvisionalExpediente(Integer idExpediente,
            Integer idEstado, String dni) throws ServiceException {
        Expediente expediente = this.find(idExpediente);
        CatalogosValores estado = this.catalogosValoresService
                .find(utilConstantes.getGlobalCatalogosValores().get(idEstado));
        expediente.setEstado(estado);
        expediente.setEstadoProvisional(estado);
        expediente = this.getDao().merge(expediente);
        this.expedienteHistDAO.crearHistorico(expediente, dni);
        ExpedienteDTO expedienteDTO = this.buscarIdSimple(idExpediente, Boolean.FALSE);
        expedienteDTO.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(estado));
        return expedienteDTO;
    }

    @Override
    public List<EntidadDTO> obtenerEntidadesConvocatoriaActivas(Integer idConvocatoria)
            throws ServiceException {
        List<EntidadDTO> listaEntidadResult = new ArrayList<>();
        
        List<EntidadDTO> listaEntidades= this.entidadService.obtener();
        if(listaEntidades != null && !listaEntidades.isEmpty()) {
            listaEntidades = listaEntidades.stream().filter(entidad -> entidad.getFcBajaFila()== null).collect(Collectors.toList());
        }
      
        if(listaEntidades != null && !listaEntidades.isEmpty()) {
            for(EntidadDTO entidad:listaEntidades) {
                
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INTERESADO_IDINTERESADO, entidad.getInteresadoDTO().getIdInteresado(),
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDCONVOCATORIA, idConvocatoria,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ExpedienteDTO> listaExpedientes= new ArrayList<>(Lists.transform(this.find(filtros, null),
                        ExpedienteToDTO.INSTANCE));
                if(!listaExpedientes.isEmpty()) {
                    if(listaExpedientes.get(0).getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpediente.INICIADA)) {
                        listaEntidadResult.add(entidad);
                    }
                }else {
                    listaEntidadResult.add(entidad);
                }
                
            }
        }
        return listaEntidadResult;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarCambioEstEstRevReqEstPro(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException {
        try {
            Expediente expediente = this.expedienteDAO.find(idExpediente);
            Boolean esCambioEstado = Boolean.FALSE;
            if (!expediente.getEstado().getNmConstante().equals(idEstado)
                    && !expediente.getEstadoRevRequi().getNmConstante().equals(idEstado)
                    && ((expediente.getEstadoProvisional() == null)
                            || (expediente.getEstadoProvisional().getNmConstante() != null)
                            || (expediente.getEstadoProvisional() != null
                                    && expediente.getEstadoProvisional().getNmConstante() != null
                                    && !expediente.getEstadoProvisional().getNmConstante()
                                            .equals(idEstado)))) {
                esCambioEstado = Boolean.TRUE;
            }
            if (Boolean.TRUE.equals(esCambioEstado)) {
                CatalogosValores estado = this.catalogosValoresService
                        .find(utilConstantes.getGlobalCatalogosValores().get(idEstado));
                expediente.setEstado(estado);
                expediente.setEstadoRevRequi(estado);
                expediente.setEstadoProvisional(estado);
                expediente = this.getDao().merge(expediente);
                this.expedienteHistDAO.crearHistorico(expediente, null);
            }
            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarCambioEstEstRevAdmEstPro(Integer idExpediente, Integer idEstado,
            String dni) throws ServiceException {
        try {
            Expediente expediente = this.expedienteDAO.find(idExpediente);
            Boolean esCambioEstado = Boolean.FALSE;
            if (!expediente.getEstado().getNmConstante().equals(idEstado)
                    && !expediente.getEstadoRevAdmin().getNmConstante().equals(idEstado)
                    && ((expediente.getEstadoProvisional() == null)
                            || (expediente.getEstadoProvisional().getNmConstante() != null)
                            || (expediente.getEstadoProvisional() != null
                                    && expediente.getEstadoProvisional().getNmConstante() != null
                                    && !expediente.getEstadoProvisional().getNmConstante()
                                            .equals(idEstado)))) {
                esCambioEstado = Boolean.TRUE;
            }
            if (Boolean.TRUE.equals(esCambioEstado)) {
                CatalogosValores estado = this.catalogosValoresService
                        .find(utilConstantes.getGlobalCatalogosValores().get(idEstado));
                expediente.setEstado(estado);
                expediente.setEstadoRevAdmin(estado);
                expediente.setEstadoProvisional(estado);
                expediente = this.getDao().merge(expediente);
                this.expedienteHistDAO.crearHistorico(expediente, null);
            }
            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO actualizarEstadoBeneficiario(Integer idExpediente, Integer idEstado)
            throws ServiceException {
        try {
            Expediente expediente = this.expedienteDAO.find(idExpediente);
            CatalogosValores estado = this.catalogosValoresService
                    .find(utilConstantes.getGlobalCatalogosValores().get(idEstado));
            expediente.setEstadoBenef(estado);
            expediente = this.getDao().merge(expediente);
            this.expedienteHistDAO.crearHistorico(expediente, null);
            return ExpedienteToDTO.INSTANCE.apply(expediente);
        } catch (ServiceException e) {
            log.error(e.getMessage());
            throw new ServiceException(Constantes.Error.ERROR_DATA);
        }
    }
    
}
