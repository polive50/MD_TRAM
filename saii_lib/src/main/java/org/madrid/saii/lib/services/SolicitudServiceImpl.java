package org.madrid.saii.lib.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.madrid.saii.lib.dao.ConvocatoriaAnualidadDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusEcoDAO;
import org.madrid.saii.lib.dao.ConvocatoriaJusTecDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteAnualidadDAO;
import org.madrid.saii.lib.dao.ExpedienteAnualidadHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoDAO;
import org.madrid.saii.lib.dao.ExpedienteJusEcoHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SolicitudFiltrosDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.dto.converters.SolicitudToDTO;
import org.madrid.saii.lib.dto.converters.SolicitudToEntity;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.SiexAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Solicitud
 *
 * @author everis
 */
@Slf4j
@Service("solicitudService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class SolicitudServiceImpl extends BaseServiceImpl<Solicitud, Integer, SolicitudDAO>
        implements SolicitudService {

    /**
     * Servicio de Convocatoria
     */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /**
     * Servicio de ConvocatoriaCom
     */
    @Lazy
    @Autowired
    @Qualifier("convocatoriaComService")
    private ConvocatoriaComService convocatoriaComService;

    /**
     * Servicio de ConvocatoriaAct
     */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;

    /**
     * Servicio de Interesado
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;

    /**
     * NoteAdmeService
     */
    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;

    /**
     * SiexAdmeService
     */
    @Autowired
    @Qualifier("siexAdmeService")
    private SiexAdmeService siexAdmeService;

    /**
     * Servicio de Catalogos
     */
    @Autowired
    @Qualifier("catalogosService")
    private CatalogosService catalogosService;

    /**
     * Servicio de CatalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Servicio de Expediente
     */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /**
     * Servicio de Documento Ptfr
     */
    @Autowired
    @Qualifier("documentoPtfrService")
    private DocumentoPtfrService documentoPtfrService;

    /**
     * Servicio de PdfService
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * Servicio de UnidadAdminService
     */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    /**
     * Servicio de ConvocatoriaReqService
     */
    @Autowired
    @Qualifier("convocatoriaReqService")
    private ConvocatoriaReqService convocatoriaReqService;
    
    
    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;
       

    /**
     * DAO de Expediente
     */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    /**
     * Servicio de EregAdmeService
     */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    @Autowired
    @Qualifier("convocatoriaAreaDAO")
    private ConvocatoriaAreaDAO convocatoriaAreaDAO;

    /**
     * DAO de Expediente
     */
    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;

    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    /**
     * DAO de ExpedienteDoc
     */
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    /**
     * DAO de ExpedienteDocHist
     */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    @Autowired
    @Qualifier("convocatoriaJusTecDAO")
    private ConvocatoriaJusTecDAO convocatoriaJusTecDAO;
    
    @Autowired
    @Qualifier("convocatoriaJusEcoDAO")
    private ConvocatoriaJusEcoDAO convocatoriaJusEcoDAO;
    
    @Autowired
    @Qualifier("expedienteJusEcoDAO")
    private ExpedienteJusEcoDAO expedienteJusEcoDAO;
    
    @Autowired
    @Qualifier("expedienteJusEcoHistDAO")
    private ExpedienteJusEcoHistDAO expedienteJusEcoHistDAO;

    @Autowired
    @Qualifier("expedienteJusTecDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;

    @Autowired
    @Qualifier("expedienteJusTecHistDAO")
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    @Autowired
    @Qualifier("expedienteJustecDocDAO")
    private ExpedienteJustecDocDAO expedienteJustecDocDAO;

    @Autowired
    @Qualifier("convocatoriaAnualidadDAO")
    private ConvocatoriaAnualidadDAO convocatoriaAnualidadDAO;

    @Autowired
    @Qualifier("expedienteAnualidadDAO")
    private ExpedienteAnualidadDAO expedienteAnualidadDAO;

    @Autowired
    @Qualifier("expedienteAnualidadHistDAO")
    private ExpedienteAnualidadHistDAO expedienteAnualidadHistDAO;

    /**
     * evaluacionService
     */
    @Autowired
    @Qualifier("evaluacionService")
    private EvaluacionService evaluacionService;

    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("tramiteSolicitanteExpedienteService")
    private TramiteSolicitanteExpedienteService tramiteSolicitanteExpedienteService;
    
    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param solicitudDAO
     */
    @Autowired
    public SolicitudServiceImpl(@Qualifier("solicitudDAO")
    SolicitudDAO solicitudDAO) {
        super();
        this.setDao(solicitudDAO);
    }

    /**
     * Metodo para obtener una Solicitud por su id.
     *
     * @param id the id solicitud
     * @return the solicitud DTO
     * @throws ServiceException the service exception
     */
    @Override
    public SolicitudDTO buscarId(final Integer id) throws ServiceException {

        // Validacion de entrada
        if (id == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // Recuperamos la solicitud
        return SolicitudToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     * Busca una solicitud según sus filtros.
     */
    @Override
    public List<SolicitudDTO> filtrar(SolicitudFiltrosDTO solicitudFiltros)
            throws ServiceException {

        List<Solicitud> lstSolicitud = new ArrayList<>();

        try {
            lstSolicitud = this.getDao().filtrar(solicitudFiltros);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

        return new ArrayList<>(Lists.transform(lstSolicitud, SolicitudToDTO.INSTANCE));
    }

    /**
     * Crea una solicitud en la base de datos.
     */
    @Override
    @Transactional(readOnly = false)
    public SolicitudDTO crearSolicitud(SolicitudDTO solicitudDto, InteresadoDTO representanteDTO)
            throws ServiceException {
       // log.info("crearSolicitud: saii_lib/ getCdNumIden"+solicitudDto.getInteresadoDTO().getCdNumIden());
        
        InteresadoDTO representanteLegal = solicitudDto.getInteresadoRepresentado();
        InteresadoDTO autorizado = solicitudDto.getInteresadoAutorizado();
        this.crearSolicitudGrabarRepresentante(representanteDTO);

        this.crearSolicitudValidacion(solicitudDto);

        Integer idConvocatoria = solicitudDto.getConvocatoriaDTO().getIdConvocatoria();

        // Seteamos la convocatoria
        solicitudDto.setConvocatoriaDTO(this.convocatoriaService.buscarId(idConvocatoria));

        // Comprobamos si la solicitud es para el solicitante
        List<ConvocatoriaActDTO> lstConvocatoriaActDTO = this.convocatoriaActService
                .buscarTipoActorSolicitante(idConvocatoria);

        // Nos quedamos con el tipo de convocatoriAct que se ha elegido en MOVA
        ConvocatoriaActDTO actorSolicitante = null;

        for (ConvocatoriaActDTO convocatoriaActDTO : lstConvocatoriaActDTO) {
            if (convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor()
                    .equals(solicitudDto.getTipoActorDTO().getIdValor())) {
                actorSolicitante = convocatoriaActDTO;
                break;
            }
        }

        if (lstConvocatoriaActDTO.isEmpty()) {
            throw new ServiceException(Constantes.Error.ERROR_ACTOR_SIN_CONVOCATORIA);
        }

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // (CD_USU_ALTA) debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = solicitudDto.getInteresadoDTO().getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(solicitudDto.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

        // Ajustamos auditoría si el interesado no existe
        if (solicitudDto.getInteresadoDTO().getIdInteresado() == null) {
            if (representanteLegal != null
                    && representanteLegal.getCdNumIden() != null) {
                solicitudDto.getInteresadoDTO()
                        .setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
            } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
                solicitudDto.getInteresadoDTO()
                        .setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
            } else {
                // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
                // columna (VARCHAR2(12)) aunque el catálogo no lo marque como
                // extracomunitario, se usa igualmente la constante para evitar ORA-12899.
                solicitudDto.getInteresadoDTO()
                        .setCdUsuAlta(Utils.valorAuditoria(solicitudDto.getInteresadoDTO().getCdNumIden()));
            }

            solicitudDto.getInteresadoDTO().setFcAltaFila(new Date());
        }

        // Estado
        solicitudDto.setEstadoDTO(catalogosValoresService.buscarNmConstante(Constantes.EstadoSolicitud.INICIADA));

        // Auditoría
        if (representanteLegal != null
                && representanteLegal.getCdNumIden() != null) {
            solicitudDto.setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
        }else if(autorizado != null &&  autorizado.getCdNumIden() != null) {
            solicitudDto.setCdUsuAlta(Utils.valorAuditoria(autorizado.getCdNumIden()));

        } else if (Boolean.TRUE.equals(esInteresadoExtraComunitario)) {
            solicitudDto.setCdUsuAlta(Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO);
        }else {
            // Salvaguarda (defensa en profundidad): ver comentario anterior.
            solicitudDto.setCdUsuAlta(Utils.valorAuditoria(solicitudDto.getInteresadoDTO().getCdNumIden()));
        }

        solicitudDto.setFcAltaFila(new Date());

        if (solicitudDto.getConvocatoriaSubmodDTO() == null
                || solicitudDto.getConvocatoriaSubmodDTO().getIdConvocatoriaSubmod() == null) {
            solicitudDto.setConvocatoriaSubmodDTO(null);
        }

        if (solicitudDto.getConvocatoriaAreaDTO() == null
                || solicitudDto.getConvocatoriaAreaDTO().getIdConvocatoriaArea() == null) {
            solicitudDto.setConvocatoriaAreaDTO(null);
        }

        // Guardamos
        solicitudDto = SolicitudToDTO.INSTANCE
                .apply(this.insertOrUpdate(SolicitudToEntity.INSTANCE.apply(solicitudDto)));

        solicitudDto.setInteresadoRepresentado(representanteLegal);
        solicitudDto.setInteresadoAutorizado(autorizado);
        ExpedienteDTO expedienteSolicitud = this.expedienteService.generar(solicitudDto,
                solicitudDto.getInteresadoDTO(), actorSolicitante, Boolean.TRUE, null);

        // Recuperamos la solicitud
        Solicitud solicitud = this.find(solicitudDto.getIdSolicitud());

        // Guardamos el id del expediente del solicitante en la solicitud
        solicitud.setCdExpedienteSol(expedienteSolicitud.getCdExpediente());
        solicitudDto = SolicitudToDTO.INSTANCE.apply(this.insertOrUpdate(solicitud));
        solicitudDto.setInteresadoRepresentado(representanteLegal);
        solicitudDto.setInteresadoAutorizado(autorizado);
        return solicitudDto;
    }

    /**
     *
     * @param representanteDTO
     * @throws ServiceException
     */
    private void crearSolicitudGrabarRepresentante(InteresadoDTO representanteDTO)
            throws ServiceException {

        if (representanteDTO != null && StringUtils.isNotBlank(representanteDTO.getCdNumIden())) {
            InteresadoDTO interesadoFis = this.interesadoService
                    .obtenerPorIdentificacion(representanteDTO.getCdNumIden());
            if (interesadoFis != null && interesadoFis.getIdInteresado() == null) {
                this.interesadoService.guardarPersona(representanteDTO,
                        representanteDTO.getCdNumIden(), false);
            }
        }
    }

    private void crearSolicitudValidacion(SolicitudDTO solicitudDto) throws ServiceException {
        // Validacion de entrada
        if (StringUtils.isBlank(solicitudDto.getInteresadoDTO().getCdNumIden())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (StringUtils.isBlank(solicitudDto.getInteresadoDTO().getDsNombre())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (solicitudDto.getInteresadoDTO().getTipoActorDTO().getNmConstante() == null || solicitudDto.getInteresadoDTO().getTipoActorDTO().getIdValor() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        if (solicitudDto.getConvocatoriaDTO().getIdConvocatoria() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentar(SolicitudDTO solicitud) throws ServiceException {

        try {
            
            String dniLogin= solicitud.getDniUserLogin();
            String cifEntidadLogin= solicitud.getCifEntidadLogin();

            // Auditoría
            InteresadoDTO representanteLegal = solicitud.getInteresadoRepresentado();

            // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
            // debe usar una constante en lugar de su identificador.
            Boolean esInteresadoExtraComunitario = solicitud.getInteresadoDTO().getTipoIdentificacionDTO() != null
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                            .equals(solicitud.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());

            // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
            // columna (VARCHAR2(12)) aunque el catálogo no lo marque como
            // extracomunitario, se usa igualmente la constante para evitar ORA-12899.
            String cdNumIden = Boolean.TRUE.equals(esInteresadoExtraComunitario)
                    ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                    : Utils.valorAuditoria(solicitud.getInteresadoDTO().getCdNumIden());
            if (representanteLegal != null
                    && representanteLegal.getCdNumIden() != null) {
                cdNumIden = Utils.valorAuditoria(representanteLegal.getCdNumIden());
            }

            Date fecha = new Date();

            // SIEX y FIRMAR (Juntos por que hay que recorrer los expedientes)
            List<AtlasFilter> filtros = new ArrayList<>();
            //VMM 26/05/25: metemos aqui el resto de estados a excluir al igual que en el resto de sitios
            List<Integer> lstEstadoExpInterno = new ArrayList<>();
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                    solicitud.getIdSolicitud(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                    lstEstadoExpInterno, Operador.NOT_IN));
                    //catalogosValoresService.buscarIdValorPorNmConstante(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO), Operador.NOT_EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            List<Expediente> lstExpedienteAux = this.expedienteDAO.find(filtros, null);

            // Verificamos si existe algun expediente de coordinador
            Boolean existeCoordinador = lstExpedienteAux.stream()
                    .anyMatch(e -> Constantes.TipoActor.COORDINADOR.equals(e.getConvocatoriaAct().getTipoActor().getNmConstante()));

            if (Boolean.TRUE.equals(existeCoordinador)) {
                Solicitud soli = this.find(solicitud.getIdSolicitud());
                soli.setItExpeCoor(Constantes.AfirmacionNegacion.S);
                this.update(soli);
            }
            
            // descartamos si existe algún expediente de participante en estado iniciada
            List<Expediente> lstExpediente = lstExpedienteAux.stream()
                    .filter(e -> (!Constantes.TipoActor.PARTICIPANTE.equals(e.getConvocatoriaAct().getTipoActor().getNmConstante()))
                            ||(Constantes.TipoActor.PARTICIPANTE.equals(e.getConvocatoriaAct().getTipoActor().getNmConstante())
                                    && !(e.getEstado().getNmConstante().equals(Constantes.EstadoExpediente.INICIADA)))).collect(Collectors.toList());

            // Recuperamo la solicitud con el cdNote actualizado en el método de
            // enviarComunicaciones
            SolicitudDTO solicitudDto = this.buscarId(solicitud.getIdSolicitud());
            ConvocatoriaDTO convDTO = solicitudDto.getConvocatoriaDTO();
            if(convDTO.getFcFinPresentacion() != null && Utils.fechasEntreRango(
                    convDTO.getFcIniPresentacion(),
                    convDTO.getFcFinPresentacion())
                    .equals(Boolean.FALSE)) {
                log.info("PRESENTAR: Esta cerrado el plazo de presentación de solicitud");
                throw new ServiceException("Esta cerrado el plazo de presentación de solicitud."); 
            }else if(convDTO.getFcFinPresentacion()== null 
                    && Utils.fechasInicioEntreRango(convDTO.getFcIniPresentacion()).equals(Boolean.FALSE)) {
                log.info("PRESENTAR: No ha iniciado el plazo de presentación de solicitud");
                throw new ServiceException("No esta iniciado el plazo de presentación de solicitud."); 
            }

            //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
            
            //VMM 26/05/25 MD-1566: sacamos aqui estas constantes para no hacer la consulta en el bucle tantas veces
           /* List<Integer> tipoImpresoJustecInterm = new ArrayList<>();
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
            Integer idTipoJustifCTInterm = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.INTERMEDIA);
            Integer idTipoJustifCTFinal= utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionCienTec.FINAL);
            Integer idTipoJustifEcoInterm = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.INTERMEDIA);
            Integer idTipoJustifEcoFinal= utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoJustificacionEco.FINAL);*/
            //TODO: hacer lo mismo con el resto de llamadas a constantes que se estan haciendo en el While
            
            Iterator<Expediente> iterator = lstExpediente.iterator();
            while (iterator.hasNext()) {
                Expediente expediente = iterator.next();
                if (StringUtils.isNotBlank(expediente.getCdExpediente())) {

                    // Presentamos los documentos
                    for (ExpedienteDoc expedienteDoc : expediente.getLstExpedienteDoc()) {

                        if (expedienteDoc.getFcBajaFila() != null) {
                            continue;
                        }

                        if (Objects.isNull(expedienteDoc.getCdFica())) {
                            expedienteDoc.setEstado(catalogosValoresService.find(
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR)));
                        } else {
                            expedienteDoc.setEstado(catalogosValoresService.find(
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)));
                        }
                        expedienteDoc.setCdUsuModif(cdNumIden);
                        expedienteDoc.setFcModifFila(fecha);
                        this.expedienteDocDAO.update(expedienteDoc);
                        this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdNumIden);
                        
                    }

                    // Firmar
                    if (solicitud.getCdExpedienteSol().equals(expediente.getCdExpediente())
                            && Constantes.TipoActor.SOLICITANTE.equals(expediente.getConvocatoriaAct().getTipoActor().getNmConstante())) {
                        ExpedienteDTO dto = this.expedienteService
                                .buscarId(expediente.getIdExpediente());
                        this.expedienteService.rellenarCdAsuntoExp(dto, dto.getImpresoDTO());
                        ExpedienteDTO expedienteFirmado = this.pdfService.crearFirmar(dto);
                        expediente.setCdEreg(expedienteFirmado.getCdEreg());
                        expediente.setDsDestinoEreg(expedienteFirmado.getDsDestinoEreg());
                        expediente.setFcFirma(expedienteFirmado.getFcFirma());
                        
                        boolean tienePresupuestoPresentado=false;
                        
                        if(convDTO.getItPartidasPresupuestarias() != null && convDTO.getItPartidasPresupuestarias().equals(Constantes.AfirmacionNegacion.S)) {
                            tienePresupuestoPresentado=true;
                        }
                        
                        if(tienePresupuestoPresentado) {
                            expediente.setEstadoRevFinan( new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.PDTE_REVISION_FINANCIACION)));
                        }else {
                            expediente.setEstadoRevFinan( new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoFinanciacion.REVISION_FINANCIACION_OK)));
                        }
                        
                        
                      //Guardamos el historico expediente tramite y interesado logado en sessión
                        
                        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION),
                                dto, dniLogin, cifEntidadLogin);
                        
                    }
                    log.info("TEST PRESENTAR: pasó crearFirma");
                    // SIEX
                    // TODO pdte
                    // expediente.setCdSiex(this.siexAdmeService
                    // .grabarExpteAPLI(ExpedienteToDTO.INSTANCE.apply(expediente)));
                    // // Se graba el interesado y el hito en SIEX
                    // this.siexAdmeService.grabarInteAPLI(ExpedienteToDTO.INSTANCE.apply(expediente));
                    // TODO pdte MD
                    // siexAdmeService.grabarHitoAPLI(ExpedienteToDTO.INSTANCE.apply(expediente));
                }

                // Asignamos el estado interno
                expediente.setEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA)));
                expediente.getEstado().setNmConstante(Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA);

                // Asignamos el estado interno de revision administrativa
                expediente.setEstadoRevAdmin(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)));
                expediente.getEstadoRevAdmin().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION);
                // Asignamos el estado interno de revision requisitos
                expediente.setEstadoRevRequi(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK)));
                expediente.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.REVISION_OK);

                boolean tieneReq = false;
                boolean tieneEval = false;
                if (Constantes.AfirmacionNegacion.S
                        .equals(solicitudDto.getConvocatoriaDTO().getItRequisitos())) {
                    
                    
                    // Validamos si tiene requisitos configurados
                    List<ConvocatoriaReqDTO> lstRequisitos = this.convocatoriaReqService
                            .obtenerRequisitoPorConvocatoriaIdActor(
                                    solicitudDto.getConvocatoriaDTO().getIdConvocatoria(),expediente.getConvocatoriaAct().getIdActor());
                    if (lstRequisitos != null && !lstRequisitos.isEmpty()) {
                        tieneReq = true;
                    }
                }
                if (Constantes.AfirmacionNegacion.S
                        .equals(solicitudDto.getConvocatoriaDTO().getItEvaluacion())) {

                    // Validamos si tiene evaluaciones configurados
                    List<EvaluacionDTO> lstEvaluaciones = this.evaluacionService
                            .buscarIdConvocatoria(
                                    solicitudDto.getConvocatoriaDTO().getIdConvocatoria());

                    if (lstEvaluaciones != null && !lstEvaluaciones.isEmpty()) {
                        tieneEval = true;
                    }
                }

                // Validamos si tiene activo el check de uso de requisitos
                if (tieneReq) {
                    expediente.setEstadoRevRequi(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)));
                    expediente.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION);
                }

                log.info("TEST PRESENTAR: Verificó requisitos");
                // Asignamos el estado interno de revision evaluación
                expediente.setEstadoRevEval(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EVALUADO_OK)));
                expediente.getEstadoRevEval().setNmConstante(Constantes.EstadoExpediente.EVALUADO_OK);

                // Validamos si tiene activo el check de evaluaciones
                if (tieneEval) {
                    expediente.setEstadoRevEval(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR)));
                    expediente.getEstadoRevEval().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR);
                }
                log.info("TEST PRESENTAR: Verificó evaluaciones");

                // Asignamos el estado de beneficiario
                expediente.setEstadoBenef(ExpedienteToEntity.INSTANCE
                        .calcularEstadoBenef(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA)));
                
                // VMM 23/07/24 MD-475: anyadimos que tambien puede ser actor
                // coordinador para contratos
                // Copiamos las fechas de contrato de la convocatoria
                if (Objects.nonNull(convDTO.getFcIniContrato())
                        && Objects.nonNull(convDTO.getFcFinContrato())
                        && (Constantes.TipoActor.PARTICIPANTE.equals(expediente.getConvocatoriaAct().getTipoActor().getNmConstante())
                                || Constantes.TipoActor.COORDINADOR.equals(expediente.getConvocatoriaAct().getTipoActor().getNmConstante()))
                        && Constantes.AfirmacionNegacion.S
                                .equals(expediente.getConvocatoriaAct().getItContrato())) {
                    expediente.setFcIniContrato(convDTO.getFcIniContrato());
                    expediente.setFcFinContrato(convDTO.getFcFinContrato());
                }
                
               

                this.expedienteDAO.update(expediente);
                log.info("TEST PRESENTAR: pasó Update expediente");
                this.expedienteHistDAO.crearHistorico(expediente,
                        solicitudDto.getInteresadoDTO().getCdNumIden());
                log.info("TEST PRESENTAR: pasó crearHistorico expediente");
                
                ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);
                
                //VMM 10/06/25 MD-1673: comentamos la generacion de formularios especificos hasta que no se haga la concesión
                /*List<Impreso> lstImpresosJustecInterm = this.impresoService.buscarPorExpedienteTipo(expediente.getIdExpediente(), tipoImpresoJustecInterm);
                List<Impreso> lstImpresosJustecFinal = this.impresoService.buscarPorExpedienteTipo(expediente.getIdExpediente(), tipoImpresoJustecFinal);
                List<Impreso> lstImpresosJusEcoInterm = this.impresoService.buscarPorExpedienteTipo(expediente.getIdExpediente(), tipoImpresoJusEcoInterm);
                List<Impreso> lstImpresosJusEcoFinal = this.impresoService.buscarPorExpedienteTipo(expediente.getIdExpediente(), tipoImpresoJusEcoFinal);
                
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.COORDINADOR)
                        .equals(expediente.getConvocatoriaAct().getTipoActor().getIdValor())
                        || utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE).equals(
                                expediente.getConvocatoriaAct().getTipoActor().getIdValor())) {

                    // Replicamos las justificaciones a los expedientes de tipo
                    // coordinador o solicitante
                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                            convDTO.getIdConvocatoria(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                    List<ConvocatoriaJusTec> lstConvocatoriaJusTec = this.convocatoriaJusTecDAO
                            .find(filtros, null);
                    
                    log.info("TEST PRESENTAR: entró en Justec");
                    for (ConvocatoriaJusTec convocatoriaJusTec : lstConvocatoriaJusTec) {

                        ExpedienteJusTec expedienteJusTec = new ExpedienteJusTec();
                        expedienteJusTec.setIdExpediente(expediente.getIdExpediente());
                        expedienteJusTec.setNmPeriodo(convocatoriaJusTec.getNmPeriodo());
                        expedienteJusTec.setTipoExpedienteJusTec(new CatalogosValores(
                                convocatoriaJusTec.getTipoConvocatoriaJusTec().getIdValor()));
                        expedienteJusTec
                                .setFcIniJustiCienTec(convocatoriaJusTec.getFcIniJustiCienTec());
                        expedienteJusTec
                                .setFcFinJustiCienTec(convocatoriaJusTec.getFcFinJustiCienTec());
                        expedienteJusTec.setTlObservacion(convocatoriaJusTec.getTlObservacion());
                        expedienteJusTec.setIdEstado(new CatalogosValores(idEstadoPdtePresentacion));

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
                            this.expedienteService.generarDatosImpresos(expedienteDTO,
                                    expedienteDTO.getImpresoJustecDTO(),
                                    ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTecNew));

                            // Obtenemos los bloquesComponente que generen documentos
                            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJustecDTO()
                                    .getLstBloqueDTO()) {

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
                                        ExpedienteDocDTO expedienteDoc = expedienteService
                                                .crearExpedienteDocJustec(expedienteDTO,
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
                            this.expedienteService.generarDatosImpresos(expedienteDTO,
                                    expedienteDTO.getImpresoJustecFinalDTO(),
                                    ExpedienteJusTecToDTO.INSTANCE.apply(expedienteJusTecNew));

                            // Obtenemos los bloquesComponente que generen documentos
                            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJustecFinalDTO()
                                    .getLstBloqueDTO()) {

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
                                        ExpedienteDocDTO expedienteDoc = expedienteService
                                                .crearExpedienteDocJustec(expedienteDTO,
                                                        bloqueComponenteDTO);
                                        ExpedienteJustecDoc expedienteJustecDoc = new ExpedienteJustecDoc();
                                        expedienteJustecDoc.setIdExpedienteDoc(
                                                expedienteDoc.getIdExpedienteDoc());
                                        expedienteJustecDoc.setIdExpedienteJusTec(
                                                expedienteJusTecNew.getIdExpedienteJusTec());
                                        expedienteJustecDoc.setFcAltaFila(new Date());
                                        expedienteJustecDoc.setCumple("N");
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
                            .find(filtros, null);

                    for (ConvocatoriaJusEco convocatoriaJusEco : lstConvocatoriaJusEco) {

                        ExpedienteJusEco expedienteJusEco = new ExpedienteJusEco();
                        expedienteJusEco.setIdExpediente(expediente.getIdExpediente());
                        expedienteJusEco.setNmPeriodo(convocatoriaJusEco.getNmPeriodo());
                        expedienteJusEco.setTipoExpedienteJusEco(new CatalogosValores(
                                convocatoriaJusEco.getTipoConvocatoriaJusEco().getIdValor()));
                        expedienteJusEco.setFcIniJustiEconomica(convocatoriaJusEco.getFcIniJustiEconomica());
                        expedienteJusEco.setFcFinJustiEconomica(convocatoriaJusEco.getFcFinJustiEconomica());
                        expedienteJusEco.setTlObservacion(convocatoriaJusEco.getTlObservacion());
                        expedienteJusEco.setIdEstado(new CatalogosValores(idEstadoPdtePresentacion));

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
                            this.expedienteService.generarDatosImpresosEco(expedienteDTO,
                                    expedienteDTO.getImpresoJusEcoDTO(),
                                    ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEcoNew));

                            // Obtenemos los bloquesComponente que generen documentos
                            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJusEcoDTO()
                                    .getLstBloqueDTO()) {

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

                                        //PDTE DE CRAE ESTA CLASE ExpedienteJusEcoDoc
                                        // Creamos un expedienteDoc de este bloque
//                                        ExpedienteDocDTO expedienteDoc = expedienteService
//                                                .crearExpedienteDocJusEco(expedienteDTO,
//                                                        bloqueComponenteDTO);
//                                        ExpedienteJusEcoDoc expedienteJusEcoDoc = new ExpedienteJusEcoDoc();
//                                        expedienteJusEcoDoc.setIdExpedienteDoc(
//                                                expedienteDoc.getIdExpedienteDoc());
//                                        expedienteJusEcoDoc.setIdExpedienteJusEco(
//                                                expedienteJusEcoNew.getIdExpedienteJusEco());
//                                        expedienteJusEcoDoc
//                                                .setCdUsuAlta(expedienteDTO.getCdUsuAlta());
//                                        expedienteJusEcoDoc.setFcAltaFila(new Date());
//                                        expedienteJusEcoDoc.setCumple("N");
//                                        expedienteJusEcoDocDAO.insert(expedienteJusEcoDoc);

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
                            this.expedienteService.generarDatosImpresosEco(expedienteDTO,
                                    expedienteDTO.getImpresoJusEcoFinalDTO(),
                                    ExpedienteJusEcoToDTO.INSTANCE.apply(expedienteJusEcoNew));

                            // Obtenemos los bloquesComponente que generen documentos
                            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoJusEcoFinalDTO()
                                    .getLstBloqueDTO()) {

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

                                        //PDTE DE CRAE ESTA CLASE ExpedienteJusEcoDoc
                                        // Creamos un expedienteDoc de este bloque
//                                        ExpedienteDocDTO expedienteDoc = expedienteService
//                                                .crearExpedienteDocJusEco(expedienteDTO,
//                                                        bloqueComponenteDTO);
//                                        ExpedienteJusEcoDoc expedienteJusEcoDoc = new ExpedienteJusEcoDoc();
//                                        expedienteJusEcoDoc.setIdExpedienteDoc(
//                                                expedienteDoc.getIdExpedienteDoc());
//                                        expedienteJusEcoDoc.setIdExpedienteJusEco(
//                                                expedienteJusEcoNew.getIdExpedienteJusEco());
//                                        expedienteJusEcoDoc.setFcAltaFila(new Date());
//                                        expedienteJusEcoDoc.setCumple("N");
//                                        expedienteJusEcoDocDAO.insert(expedienteJusEcoDoc);

                                    }
                                }
                            }
                        }
                    }  //fin del for de convocatoriaJusEco
                    
                    log.info("TEST PRESENTAR: pasó JusEco ");
                    
                }*///fin del for de Solic y Coord

                // Replicamos las anualidades a los expedientes
                List<AtlasFilter> filtrosAnual = new ArrayList<>();
                filtrosAnual.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        convDTO.getIdConvocatoria(), Operador.EQUALS));
                filtrosAnual.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ConvocatoriaAnualidad> lstConvocatoriaAnual = this.convocatoriaAnualidadDAO
                        .find(filtrosAnual, null);

                for (ConvocatoriaAnualidad convocatoriaAnual : lstConvocatoriaAnual) {

                    ExpedienteAnualidad expedienteAnualidad = new ExpedienteAnualidad();
                    expedienteAnualidad.setIdExpediente(expediente.getIdExpediente());
                    expedienteAnualidad.setNmAnualidad(convocatoriaAnual.getNmAnualidad());
                    expedienteAnualidad.setTipoExpedienteAnualidad(new CatalogosValores(
                            convocatoriaAnual.getTipoConvocatoriaAnualidad().getIdValor()));
                    expedienteAnualidad.setFcIniEjecucion(convocatoriaAnual.getFcIniEjecucion());
                    expedienteAnualidad.setFcFinEjecucion(convocatoriaAnual.getFcFinEjecucion());
                    expedienteAnualidad.setTlObservacion(convocatoriaAnual.getTlObservacion());

                    this.expedienteAnualidadDAO.insert(expedienteAnualidad);
                    this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);
                }
                log.info("TEST PRESENTAR: pasó anualidades ");
                
                
              
            }

            solicitudDto = this.cambiarEstadoPresentada(solicitudDto);
            
            Expediente expedienteSolicitante = null;
            RegistroEregDTO registroEregDTO = new RegistroEregDTO();
            if( lstExpediente != null && !lstExpediente.isEmpty()) {
                this.convocatoriaComService
                .enviarComunicaciones(solicitud.getConvocatoriaDTO().getIdConvocatoria(),
                        lstExpediente.get(0).getIdExpediente(), utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION),
                        null, null);
                
                
             // Filtramos por cdExpedienteSol
                String cdExpedienteSol = solicitudDto.getCdExpedienteSol();
                Optional<Expediente> opExpediente = lstExpediente.stream()
                        .filter(x -> Objects.nonNull(x.getCdExpediente())
                                && x.getCdExpediente().equals(cdExpedienteSol))
                        .findFirst();
                
                if (opExpediente.isPresent()) {
                    expedienteSolicitante = opExpediente.get();

                    // Obtenemos el listado
                    filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                            expedienteSolicitante.getIdExpediente(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION), Operador.EQUALS));

                    List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

                    // Nos quedamos con el primero
                    ExpedienteDoc expedienteDoc = lstExpedienteDoc.get(0);

                    // Generamos el objeto de respuesta
                    
                    registroEregDTO.setFechaAlta(
                            Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
                    registroEregDTO.setCdAnexoEreg(expedienteDoc.getCdAnexoEreg());
                    registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteSolicitante.getCdEreg()));
                    registroEregDTO.setIdExpedienteDoc(expedienteDoc.getIdExpedienteDoc());
                    registroEregDTO.setIdSolicitud(solicitudDto.getIdSolicitud());
                    registroEregDTO.setEsDescargaZipSolicitud(Boolean.TRUE);
                    log.info("TEST PRESENTAR: generó respuesta EREG");
                }
                

            }else {
                log.info("PRESENTAR: No hay expedientes para presentar, lista expedientes vacia...");
                throw new ServiceException("No hay expedientes para presentar");
            }
            
    
            return registroEregDTO;

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Cambia el estado de la solicitud a presentada
     *
     * @param dto de la solicitud
     * @return SolicitudDTO
     * @throws ServiceException
     */
    private SolicitudDTO cambiarEstadoPresentada(SolicitudDTO dto) throws ServiceException {
        // Recuperamos la solicitud para cambiarle el estado
        Solicitud solicitud = this.find(dto.getIdSolicitud());

        // Se cambia el estado a PRESENTADA
        solicitud.setEstado(this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA)));

        // Cambiamos la auditoría
        solicitud.setCdUsuModif(Utils.valorAuditoria(dto.getInteresadoDTO().getCdNumIden()));
        solicitud.setFcModifFila(new Date());

        return SolicitudToDTO.INSTANCE.apply(solicitud);
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaValoresActor(String tipoPersona)
            throws ServiceException {

        List<CatalogosValoresDTO> lista = new ArrayList<>();

        if (tipoPersona.equals(Constantes.Identificacion.DS_FISICA)) {
            lista = this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_SOLICITANTE));
            lista.addAll(this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_COORDINADOR)));
            lista.addAll(this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_FISICA_PARTICIPANTE)));
        } else {
            lista = this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TIPO_ACTOR_PERSONA_JURIDICA));
        }

        return lista;
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaAreasCientificas() throws ServiceException {

        // Obtenemos el valor del catálogo
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.AREAS_CIENTIFICAS));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerListaEstadosSolicitud() throws ServiceException {

        // Obtenemos el valor del catálogo
        return this.catalogosValoresService.buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.ESTADO_SOLICITUD));

    }

    /**
     *
     */
    @Override
    public SolicitudDTO buscarSolicitudIniciada(SolicitudDTO solicitudDTO) throws ServiceException {
        SolicitudDTO dto = new SolicitudDTO();

        try {
            List<Solicitud> listaSolicitud = this.getDao().buscarSolicitudIniciada(solicitudDTO);

            if (Boolean.FALSE.equals(listaSolicitud.isEmpty())) {
                dto = SolicitudToDTO.INSTANCE.apply(listaSolicitud.get(0));
            }

            return dto;
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     */
    @Override
    public SolicitudDTO buscarPorExpediente(Integer idExpediente) throws ServiceException {

        try {
            List<Solicitud> listaSolicitud = this.getDao().buscarPorExpediente(idExpediente);

            if (Boolean.FALSE.equals(listaSolicitud.isEmpty())) {
                return SolicitudToDTO.INSTANCE.apply(listaSolicitud.get(0));
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

    }

    /**
     *
     */
    @Override
    public List<SolicitudDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                SolicitudToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
//    public List<SolicitudDTO> buscarSolicitudesTramitacion(final int index, final int pageSize,
//            final List<AtlasFilter> filtros, final List<AtlasSort> orden,
//            final Integer idConvocatoria, final Boolean mostrarIniciadas, Integer nmDiasSubsanacion)
//            throws ServiceException {
//
//        List<SolicitudDTO> lstSolicitudes = null;
//
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
//                idConvocatoria, Operador.EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//
//        List<Integer> lstEstados = new ArrayList<>();
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
//        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));
//
//        // Si el flag mostrarIniciadas viene a true, se agregan las iniciadas al
//        // filtro de estado
//        if (Boolean.TRUE.equals(mostrarIniciadas)) {
//            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
//        }
//        filtros.add(
//                new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados, Operador.IN));
//
//        lstSolicitudes = new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
//                SolicitudToDTO.INSTANCE));
//
//        for (SolicitudDTO soli : lstSolicitudes) {
//            
//            //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
//            SolicitudDTO solicitudDTO = this.buscarId(soli.getIdSolicitud());
//            Boolean solicitudEnTramitacion = !solicitudDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
//            
//            soli.setAlertaTramitesPendiente(
//                    this.expedienteDAO.alertaGestorSolicitud(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//            // obtener alerta de documentos fuera de plazo de subsanacion
//            soli.setAlertaDocumentos(this.alertaDocumentosSolicitud(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//            // obtener alerta de requisitos fuera de plazo de subsanacion
//            soli.setAlertaRequisitos(this.alertaRequisitosSolicitud(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//            // VMM 10/02/25 MD-578: obtener alerta de documentos JCT fuera de
//            // plazo de subsanacion
//            soli.setAlertaDocumentosJCT(this.alertaDocumentosJCT(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//            // VMM 10/02/25 MD-582: obtener alerta de documentos Contrato fuera
//            // de plazo de subsanacion
//            soli.setAlertaDocumentosCont(this.alertaDocumentosContrato(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//            // VMM 10/02/25 MD-112: obtener alerta de documentos de las
//            // Modificaciones PPO con Actas fuera de plazo de subsanacion
//            soli.setAlertaDocumentosModif(this.alertaDocumentosModif(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//	    soli.setAlertaDocumentosModifExtra(this.alertaDocumentosModifExtra(soli.getIdSolicitud(), solicitudEnTramitacion));
//            
//            
//            //VMM 29/10/25 MD-1586: obtener alerta de documentos JEco fuera de plazo de subsanacion
//            soli.setAlertaDocumentosJEco(this.alertaDocumentosJEco(soli.getIdSolicitud(), solicitudEnTramitacion));
//
//        }
//        return lstSolicitudes;
//    }

    
    
    public List<SolicitudDTO> buscarSolicitudesTramitacion(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            final Integer idConvocatoria, final Boolean mostrarIniciadas, Integer nmDiasSubsanacion)
            throws ServiceException {

        // 1. Añadimos los filtros base
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        // 2. Filtros de estados permitidos
        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DESISTIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.EXCLUIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.RENUNCIADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.DENEGADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.CONCEDIDA_DESISTIDA));

        if (Boolean.TRUE.equals(mostrarIniciadas)) {
            lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados, Operador.IN));

        // 3. Obtenemos la lista paginada original
        List<SolicitudDTO> lstSolicitudes = new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), SolicitudToDTO.INSTANCE));

        // 4. ¡Invocamos tu nuevo método optimizado por lote!
        this.hidratarAlertasSolicitudesPorLote(lstSolicitudes);

        return lstSolicitudes;
    }
    
    
    public List<SolicitudDTO> hidratarAlertasSolicitudesPorLote(List<SolicitudDTO> listaSolicitudesPaginada) {
        if (listaSolicitudesPaginada == null || listaSolicitudesPaginada.isEmpty()) {
            return listaSolicitudesPaginada;
        }

        // CORRECCIÓN 1: Extraemos los IDs de las SOLICITUDES de la pantalla actual
        List<Integer> idsSolicitudesPantalla = listaSolicitudesPaginada.stream()
                .map(SolicitudDTO::getIdSolicitud)
                .filter(Objects::nonNull)
                .distinct() // Evita duplicados por seguridad
                .collect(Collectors.toList());

        Integer estadoFiltroAlerta = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS);

        // 1. Alertas de Gestión (ATENCIÓN: Debe recibir idsSolicitudesPantalla y agrupar por idSolicitud)
        Map<Integer, Long> mAlertaGestor = expedienteDAO.contarExpedientesPendientesPorSolicitud(idsSolicitudesPantalla);

        // 2. Alertas de Documentos Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mDocumentos = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct doc.idExpedienteDoc) " +
                "from org.madrid.saii.lib.domain.DocumentoPtfr dp, " + 
                "org.madrid.saii.lib.domain.Expediente exp " +       
                "inner join exp.solicitud sol " +                       
                "inner join exp.lstExpedienteDoc doc " +
                "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +     
                "and sol.idSolicitud in (:idsSolicitudes) " + // El nombre del parámetro debe coincidir con lo que use "obtenerConteoAgregadoSolicitudes"
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and exp.estadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud",
                idsSolicitudesPantalla, estadoFiltroAlerta
            );

        // 3. Alertas de Requisitos Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mRequisitos = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct er.idExpedienteReq) " +
                "from Expediente exp " +
                "inner join exp.solicitud sol " +
                "inner join exp.lstExpedienteReq er " +
                "where sol.idSolicitud in (:idsSolicitudes) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null " +
                "and exp.estadoRevRequi.idValor = :estadoFiltroAlerta " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and er.documentoPtfr.fcFinPlazoSubsanacion is not null " +
                "and er.documentoPtfr.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud", 
                idsSolicitudesPantalla, estadoFiltroAlerta
            );
             
        // 4. Alertas de Documentos JCT Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mJCT = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct doc.idExpedienteDoc) " +
                "from ExpedienteJustecDoc jtd, " + 
                "ExpedienteJusTec jct, " +          
                "DocumentoPtfr dp, " +              
                "Expediente exp " +                
                "inner join exp.solicitud sol " +
                "inner join exp.lstExpedienteDoc doc " +
                "where jtd.idExpedienteDoc = doc.idExpedienteDoc " +
                "and jct.idExpedienteJusTec = jtd.idExpedienteJusTec " +
                "and dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
                "and sol.idSolicitud in (:idsSolicitudes) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and jtd.fcBajaFila is null and jct.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and jct.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud", 
                idsSolicitudesPantalla, estadoFiltroAlerta
            );
             
        // 5. Alertas de Documentos de Contrato Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mContrato = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct doc.idExpedienteDoc) " +
                "from DocumentoPtfr dp, " + 
                "Expediente exp " +          
                "inner join exp.solicitud sol " +
                "inner join exp.lstExpedienteDoc doc " +
                "inner join ExpedienteContratoDoc ecd on ecd.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
                "inner join ecd.expedienteContrato con " +
                "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
                "and sol.idSolicitud in (:idsSolicitudes) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and ecd.fcBajaFila is null and con.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and con.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud", 
                idsSolicitudesPantalla, estadoFiltroAlerta
            );
             
        // 6. Alertas de Documentos de Modificaciones Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mModif = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct doc.idExpedienteDoc) " +
                "from ExpedienteModifPresPlazoOtra mod, " + 
                "DocumentoPtfr dp, " +                      
                "Expediente exp " +                                 
                "inner join exp.solicitud sol " +
                "inner join exp.lstExpedienteDoc doc " +
                "inner join ExpedienteModfPresupPlazosOtrosDoc emd on emd.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
                "where mod.idExpedienteModifPresPlazoOtra = emd.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra " +
                "and dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
                "and sol.idSolicitud in (:idsSolicitudes) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and emd.fcBajaFila is null and mod.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and mod.estadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud", 
                idsSolicitudesPantalla, estadoFiltroAlerta
            );
             
        // 7. Alertas de Documentos JEco Vencidos (Pasamos idsSolicitudesPantalla)
        Map<Integer, Long> mJEco = obtenerConteoAgregadoSolicitudes(
                "select sol.idSolicitud, count(distinct doc.idExpedienteDoc) " +
                "from DocumentoPtfr dp, " + 
                "Expediente exp " +          
                "inner join exp.solicitud sol " +
                "inner join exp.lstExpedienteDoc doc " +
                "inner join ExpedienteJusEcoDoc jed on jed.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
                "inner join jed.expedienteJusEco je " +
                "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
                "and sol.idSolicitud in (:idsSolicitudes) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and jed.fcBajaFila is null and je.fcBajaFila is null " +
                "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and je.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.idSolicitud", 
                idsSolicitudesPantalla, estadoFiltroAlerta
            );

        // Mapeo final en memoria O(N) -> Ahora sí las claves de los mapas coinciden con 'id'
        for (SolicitudDTO conv : listaSolicitudesPaginada) {
            Integer id = conv.getIdSolicitud();
            
            conv.setAlertaTramitesPendiente(mAlertaGestor.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentos(mDocumentos.getOrDefault(id, 0L).intValue());
            conv.setAlertaRequisitos(mRequisitos.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosJCT(mJCT.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosCont(mContrato.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosModif(mModif.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosModifExtra(mModif.getOrDefault(id, 0L).intValue()); 
            conv.setAlertaDocumentosJEco(mJEco.getOrDefault(id, 0L).intValue());
        }

        return listaSolicitudesPaginada;
    }
    
    
    private Map<Integer, Long> obtenerConteoAgregadoSolicitudes(String hql, List<Integer> idsPagina, Object valorEstadoFiltro) {
        org.hibernate.Session session = sessionFactory.getCurrentSession();        
        org.hibernate.query.Query<Object[]> query = session.createQuery(hql);
        
        // Cambiado para enlazar con :idsSolicitudes en vez de convocatorias
        query.setParameterList("idsSolicitudes", idsPagina, org.hibernate.type.IntegerType.INSTANCE);
        
        query.setParameterList("estadosExtra", Arrays.asList(
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA)
        ), org.hibernate.type.IntegerType.INSTANCE);

        query.setParameter("estadoSolIniciada", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        
        if (hql.contains("estadosBaseInactivos")) {
            query.setParameterList("estadosBaseInactivos", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
            ), org.hibernate.type.IntegerType.INSTANCE);
        }

        query.setParameter("estadoFiltroAlerta", valorEstadoFiltro);

        List<Object[]> resultado = query.list();
        
        return resultado.stream().collect(Collectors.toMap(
                fila -> (Integer) fila[0],
                fila -> (Long) fila[1]
        ));
    }
    
    /**
     * Método para alerta de documentos fuera de plazo de subsanacion
     *
     * @param idSolicitud
     * @throws ServiceException 
     */
    private Integer alertaDocumentosSolicitud(Integer idSolicitud, Boolean solicitudEnTramitacion) {

        Integer alertaDocumentosSolicitud = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesPorIdSolic(idSolicitud,
                Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_ADMIN, solicitudEnTramitacion);

        for (Expediente exped : lstExpediente) {
            for (ExpedienteDoc expedDoc : exped.getLstExpedienteDoc()) {

                alertaDocumentosSolicitud = this
                        .comprobarSubsanacionVencidaDoc(alertaDocumentosSolicitud, expedDoc);
            }
        }

        return alertaDocumentosSolicitud;
    }

    private Integer comprobarSubsanacionVencidaDoc(Integer alertaDocumentos,
            ExpedienteDoc expedDoc) {
        if (Objects.nonNull(expedDoc.getIdDocumentoPtfr())) {

            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO.find(expedDoc.getIdDocumentoPtfr());

            if (Objects.nonNull(documentoPtfr)
                    && Objects.nonNull(documentoPtfr.getFcFinPlazoSubsanacion())) {

                LocalDate fcSubsanacionLimite = Utils
                        .convertDateToLocalDate(documentoPtfr.getFcFinPlazoSubsanacion());
                LocalDate fcHoy = LocalDate.now();

                if (fcHoy.isAfter(fcSubsanacionLimite)) {
                    alertaDocumentos++;
                }
            }
        }
        return alertaDocumentos;
    }

    private List<Expediente> consultarExpdtesPorIdSolic(Integer idSolicitud, String filtroEstado, Boolean solicitudEnTramitacion) {
        List<AtlasFilter> filtros1 = new ArrayList<>();

        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros1.add(new AtlasFilter(filtroEstado,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS), Operador.EQUALS));
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            //quitamos los exps iniciados, propuestos, no aceptados o rechazados
            List<Integer> lstEstadoExpInterno = new ArrayList<>();
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
            
            filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                    Operador.NOT_IN));
        }

        // consultar la lista de expedientes
        return this.expedienteDAO.find(filtros1, null);
    }

    /**
     * Método para alerta de requisitos fuera de plazo de subsanacion
     *
     * @param idSolicitud
     * @throws ServiceException 
     */
    private Integer alertaRequisitosSolicitud(Integer idSolicitud, Boolean solicitudEnTramitacion) {

        Integer alertaRequisitosSolicitud = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesPorIdSolic(idSolicitud,
                Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_REQUI, solicitudEnTramitacion);

        for (Expediente exped : lstExpediente) {

            // consultar e iterar la lista de expedienteReq
            for (ExpedienteReq expedReq : exped.getLstExpedienteReq()) {

                alertaRequisitosSolicitud = this
                        .comprobarSubsanacionVencidaReq(alertaRequisitosSolicitud, expedReq);
            }
        }

        return alertaRequisitosSolicitud;
    }

    private Integer comprobarSubsanacionVencidaReq(Integer alertaRequisitos,
            ExpedienteReq expedReq) {
        if (Objects.nonNull(expedReq.getDocumentoPtfr())
                && Objects.nonNull(expedReq.getDocumentoPtfr().getFcFinPlazoSubsanacion())) {

            LocalDate fcSubsanacionLimite = Utils
                    .convertDateToLocalDate(expedReq.getDocumentoPtfr().getFcFinPlazoSubsanacion());
            LocalDate fcHoy = LocalDate.now();

            if (fcHoy.isAfter(fcSubsanacionLimite)) {
                alertaRequisitos++;
            }
        }
        return alertaRequisitos;
    }

    /**
     * Método para alerta de documentos JCT fuera de plazo de subsanacion
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @throws ServiceException
     */
    private Integer alertaDocumentosJCT(Integer idSolicitud, Boolean solicitudEnTramitacion) throws ServiceException {

        int alertaDocumentosJCT = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(idSolicitud, solicitudEnTramitacion);

        for (Expediente exped : lstExpediente) {

            List<ExpedienteDoc> lstExpedienteJustecDoc = this.expedienteDAO
                    .findExpJusTecDocByIdExp(exped.getIdExpediente());

            // consultar e iterar la lista de docs asociada a expedienteJustecDoc
            for (ExpedienteDoc expedDoc : lstExpedienteJustecDoc) {

                alertaDocumentosJCT = this.comprobarSubsanacionVencidaDoc(alertaDocumentosJCT,
                        expedDoc);
            }
        }

        return alertaDocumentosJCT;
    }
    
    
    /**
     * Método para alerta de documentos JEco fuera de plazo de subsanacion
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @throws ServiceException
     */
    private Integer alertaDocumentosJEco(Integer idSolicitud, Boolean solicitudEnTramitacion) throws ServiceException {

        int alertaDocumentosJEco = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(idSolicitud, solicitudEnTramitacion);

        for (Expediente exped : lstExpediente) {

            List<ExpedienteDoc> lstExpedienteJusEcocDoc = this.expedienteDAO
                    .findExpJusEcoDocByIdExp(exped.getIdExpediente());

            // consultar e iterar la lista de docs asociada a expedienteJusEcoDoc
            for (ExpedienteDoc expedDoc : lstExpedienteJusEcocDoc) {

                alertaDocumentosJEco = this.comprobarSubsanacionVencidaDoc(alertaDocumentosJEco,
                        expedDoc);
            }
        }

        return alertaDocumentosJEco;
    }
    

    private List<Expediente> consultarExpdtesActivosPorIdSolic(Integer idSolicitud, Boolean solicitudEnTramitacion) {
        // estados expediente que no lanzan alerta
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        
        //VMM MD-2349 26/01/26: mostrar solo los expdtes tramitados si la solicitud esta tramitada
        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitud tramitada

            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        }
  
        List<AtlasFilter> filtros1 = new ArrayList<>();

        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud,
                Operador.EQUALS));
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                Operador.NOT_IN));
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        // consultar la lista de expedientes
        return this.expedienteDAO.find(filtros1, null);
    }

    /**
     * Método para alerta de documentos Contrato fuera de plazo de subsanacion
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @throws ServiceException
     */
    private Integer alertaDocumentosContrato(Integer idSolicitud, Boolean solicitudEnTramitacion) throws ServiceException {

        int alertaDocumentosContrato = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(idSolicitud, solicitudEnTramitacion);

        for (Expediente exped : lstExpediente) {

            List<ExpedienteDoc> lstExpedienteContDoc = this.expedienteDAO
                    .findExpContratosDocByIdExp(exped.getIdExpediente());

            // consultar e iterar la lista de docs asociada a
            // expedienteContratoDoc
            for (ExpedienteDoc expedDoc : lstExpedienteContDoc) {

                alertaDocumentosContrato = this
                        .comprobarSubsanacionVencidaDoc(alertaDocumentosContrato, expedDoc);
            }
        }

        return alertaDocumentosContrato;
    }

    /**
     * Método para alerta de documentos Modificaciones PPO con Actas fuera de
     * plazo de subsanacion
     *
     * @param idSolicitud
     * @param solicitudEnTramitacion
     * @throws ServiceException
     */
    private Integer alertaDocumentosModif(Integer idSolicitud, Boolean solicitudEnTramitacion) throws ServiceException {

        int alertaDocumentosModif = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(idSolicitud, solicitudEnTramitacion);


        for (Expediente exped : lstExpediente) {
           
            List<ExpedienteDoc> lstExpedienteModifDoc = this.expedienteDAO
                    .findExpModifDocByIdExp(exped.getIdExpediente());

            // consultar e iterar la lista de docs asociada a
            // expedienteModifPresPlazoOtraDoc
            for (ExpedienteDoc expedDoc : lstExpedienteModifDoc) {

                alertaDocumentosModif = this.comprobarSubsanacionVencidaDoc(alertaDocumentosModif,
                        expedDoc);
            }
        }
     

        return alertaDocumentosModif;
    }

 	private Integer alertaDocumentosModifExtra(Integer idSolicitud, Boolean solicitudEnTramitacion) throws ServiceException {

        int alertaDocumentosModif = 0;

        List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(idSolicitud, solicitudEnTramitacion);
        List<Integer> idsExpedientes = new ArrayList<>();

        for (Expediente exped : lstExpediente) {
            idsExpedientes.add(exped.getIdExpediente());
        }
        
        
        if(!idsExpedientes.isEmpty()) {
            List<AtlasFilter> filtros = null;
            List<AtlasSort> orden  = null;

            if (filtros == null) {
                filtros = new ArrayList<>();            
            }
            
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idsExpedientes, Operador.IN));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            if (orden == null) {
                orden = new ArrayList<>();
            }
            orden.add(new AtlasSort(Constantes.Filtro.FILTRO_IDMODIFICACION, true));
            
            List<ExpedienteModifPresPlazoOtraDTO> listaExpedienteModifPPODTO = this.expedienteModifPresPlazoOtraService.buscar(0, 0, filtros, orden);
            
            if(listaExpedienteModifPPODTO != null) {
                for (ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO : listaExpedienteModifPPODTO) {
                    if(expedienteModifPresPlazoOtraDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE_MODIFICACIONES)) 
                            || expedienteModifPresPlazoOtraDTO.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE))) {
                        alertaDocumentosModif = alertaDocumentosModif+1;
                    }
                }
            }
            
        }

        return alertaDocumentosModif;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public SolicitudDTO cambiarEstado(Integer idSolicitud, Integer idEstadoSolictud)
            throws ServiceException {

        if (Objects.isNull(idSolicitud) || Objects.isNull(idEstadoSolictud)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Solicitud solicitud = this.find(idSolicitud);
        CatalogosValores estado = this.catalogosValoresService.find(idEstadoSolictud);
        solicitud.setEstado(estado);

        return SolicitudToDTO.INSTANCE.apply(this.insertOrUpdate(solicitud));
    }

    /**
    *
    */
    @Override
    public List<SolicitudDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(filtros, null), SolicitudToDTO.INSTANCE));
    }

    @Override
    public SolicitudDTO buscarSolicitudOrganismoIniciada(SolicitudDTO solicitudDTO)
            throws ServiceException {
        SolicitudDTO dto = new SolicitudDTO();

        try {
            List<Solicitud> listaSolicitud = this.getDao()
                    .buscarSolicitudOrganismoIniciada(solicitudDTO);

            if (Boolean.FALSE.equals(listaSolicitud.isEmpty())) {
                dto = SolicitudToDTO.INSTANCE.apply(listaSolicitud.get(0));
            }

            return dto;
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public SolicitudDTO guardarSolicitud(SolicitudDTO solicitudDTO) throws ServiceException {
        // Guardamos
        solicitudDTO = SolicitudToDTO.INSTANCE
                .apply(this.getDao().merge(SolicitudToEntity.INSTANCE.apply(solicitudDTO)));
        return solicitudDTO;
    }
}
