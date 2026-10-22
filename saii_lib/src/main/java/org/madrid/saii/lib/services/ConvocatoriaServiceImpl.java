package org.madrid.saii.lib.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.madrid.saii.lib.dao.AlegacionDAO;
import org.madrid.saii.lib.dao.ApartadoDAO;
import org.madrid.saii.lib.dao.BloqueComponenteDAO;
import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ComponenteDAO;
import org.madrid.saii.lib.dao.ComunicacionCorreoActorDAO;
import org.madrid.saii.lib.dao.ComunicacionNoteActorDAO;
import org.madrid.saii.lib.dao.ConvocatoriaActDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaAreaSubareaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauContDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCauEvalDAO;
import org.madrid.saii.lib.dao.ConvocatoriaComDAO;
import org.madrid.saii.lib.dao.ConvocatoriaCoorDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalDAO;
import org.madrid.saii.lib.dao.ConvocatoriaEnlDAO;
import org.madrid.saii.lib.dao.ConvocatoriaEntidadCerradaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaEurDAO;
import org.madrid.saii.lib.dao.ConvocatoriaEvalTipoDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaFirDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaVerDAO;
import org.madrid.saii.lib.dao.ConvocatoriaReqDAO;
import org.madrid.saii.lib.dao.ConvocatoriaSubmodDAO;
import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.EvaluacionDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecApartaDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecDAO;
import org.madrid.saii.lib.dao.EvaluacionJustecNotaDAO;
import org.madrid.saii.lib.dao.EvaluacionNotaDAO;
import org.madrid.saii.lib.dao.EvaluacionTipoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.dao.FinanciacionDAO;
import org.madrid.saii.lib.dao.FinanciacionDatoDAO;
import org.madrid.saii.lib.dao.ImpresoDAO;
import org.madrid.saii.lib.dao.InformeDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.madrid.saii.lib.domain.ConvocatoriaEvalTipoDoc;
import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.domain.EvaluacionJustec;
import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.madrid.saii.lib.domain.EvaluacionNota;
import org.madrid.saii.lib.domain.EvaluacionTipoDoc;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.Financiacion;
import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaGestorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPaginacionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.EvaluacionTipoDocDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.ResponseDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToEntity;
import org.madrid.saii.lib.dto.converters.EvaluacionTipoDocToEntity;
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
import atlas.core.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Convocatorias
 *
 * @author everis
 */

@Service("convocatoriaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaServiceImpl extends BaseServiceImpl<Convocatoria, Integer, ConvocatoriaDAO>
        implements ConvocatoriaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDAO
     */
    @Autowired
    public ConvocatoriaServiceImpl(@Qualifier("convocatoriaDAO") ConvocatoriaDAO convocatoriaDAO) {
        super();
        this.setDao(convocatoriaDAO);
    }

    /**
     * Servicio de CatalogosValores
     */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** Servicio ConvocatoriaActService */
    @Autowired
    @Qualifier("convocatoriaActService")
    private ConvocatoriaActService convocatoriaActService;

    /** Repositorio ConvocatoriaSubmodService */
    @Autowired
    @Qualifier("convocatoriaSubmodService")
    private ConvocatoriaSubmodService convocatoriaSubmodService;

    /** Repositorio ConvocatoriaAreaService */
    @Autowired
    @Qualifier("convocatoriaAreaService")
    private ConvocatoriaAreaService convocatoriaAreaService;
    
    @Autowired
    @Qualifier("convocatoriaAreaSubareaService")
    private ConvocatoriaAreaSubareaService convocatoriaAreaSubareaService;
    
    @Autowired
    @Qualifier("financiacionService")
    private FinanciacionService financiacionService;
    
    /** Repositorio ConvocatoriaGestorService */
    @Autowired
    @Qualifier("convocatoriaGestorService")
    private ConvocatoriaGestorService convocatoriaGestorService;

    @Autowired
    @Qualifier("convocatoriaPlaService")
    private ConvocatoriaPlaService convocatoriaPlaService;

    @Autowired
    @Qualifier("convocatoriaPlaDocService")
    private ConvocatoriaPlaDocService convocatoriaPlaDocService;
    
    @Lazy
    @Autowired
    @Qualifier("convocatoriaComService")
    private ConvocatoriaComService convocatoriaComService;
    
    @Autowired
    @Qualifier("convocatoriaTipoDocService")
    private ConvocatoriaTipoDocService convocatoriaTipoDocService;
    
    /**
     * Servicio de Entidades Cerradas
     */
    @Autowired
    @Qualifier("convocatoriaEntidadCerradaService")
    private ConvocatoriaEntidadCerradaService convocatoriaEntidadCerradaService;

    /** Repositorio ConvocatoriaEurDAO */
    @Autowired
    @Qualifier("convocatoriaEurDAO")
    private ConvocatoriaEurDAO convocatoriaEurDAO;
    
    /** Repositorio ConvocatoriaEntidadcerradaDAO **/
    @Autowired
    @Qualifier("convocatoriaEntidadCerradaDAO")
    private ConvocatoriaEntidadCerradaDAO convocatoriaEntidadCerradaDAO;

    /** Repositorio ConvocatoriaActDAO */
    @Autowired
    @Qualifier("convocatoriaActDAO")
    private ConvocatoriaActDAO convocatoriaActDAO;

    /** Repositorio convocatoriaComDAO */
    @Autowired
    @Qualifier("convocatoriaComDAO")
    private ConvocatoriaComDAO convocatoriaComDAO;
    
    /** Repositorio convocatoriaComDAO */
    @Autowired
    @Qualifier("comunicacionCorreoActorDAO")
    private ComunicacionCorreoActorDAO comunicacionCorreoActorDAO;
    
    /** Repositorio comunicacionNoteActorDAO */
    @Autowired
    @Qualifier("comunicacionNoteActorDAO")
    private ComunicacionNoteActorDAO comunicacionNoteActorDAO;

    /** Repositorio ConvocatoriaEnlDAO */
    @Autowired
    @Qualifier("convocatoriaEnlDAO")
    private ConvocatoriaEnlDAO convocatoriaEnlDAO;

    /** Repositorio ConvocatoriaDocDAO */
    @Autowired
    @Qualifier("convocatoriaDocDAO")
    private ConvocatoriaDocDAO convocatoriaDocDAO;

    /** Repositorio ImpresoDAO */
    @Autowired
    @Qualifier("impresoDAO")
    private ImpresoDAO impresoDAO;

    /** Repositorio BloqueDAO */
    @Autowired
    @Qualifier("bloqueDAO")
    private BloqueDAO bloqueDAO;

    /** Repositorio BloqueComponenteDAO */
    @Autowired
    @Qualifier("bloqueComponenteDAO")
    private BloqueComponenteDAO bloqueComponenteDAO;

    /** Repositorio ComponenteDAO */
    @Autowired
    @Qualifier("componenteDAO")
    private ComponenteDAO componenteDAO;

    /** Repositorio ConvocatoriaCauDAO */
    @Autowired
    @Qualifier("convocatoriaCauDAO")
    private ConvocatoriaCauDAO convocatoriaCauDAO;

    /** Repositorio ConvocatoriaCauEvalDAO */
    @Autowired
    @Qualifier("convocatoriaCauEvalDAO")
    private ConvocatoriaCauEvalDAO convocatoriaCauEvalDAO;
    
    
    /** Repositorio ConvocatoriaCauContDAO */
    @Autowired
    @Qualifier("convocatoriaCauContDAO")
    private ConvocatoriaCauContDAO convocatoriaCauContDAO;
  

    /** Repositorio ConvocatoriaTipDAO */
    @Autowired
    @Qualifier("convocatoriaTipoDocDAO")
    private ConvocatoriaTipoDocDAO convocatoriaTipDAO;

    /** Repositorio ConvocatoriaPlaDAO */
    @Autowired
    @Qualifier("convocatoriaPlaDAO")
    private ConvocatoriaPlaDAO convocatoriaPlaDAO;

    /** Repositorio ConvocatoriaPlaFirDAO */
    @Autowired
    @Qualifier("convocatoriaPlaFirDAO")
    private ConvocatoriaPlaFirDAO convocatoriaPlaFirDAO;

    /** Repositorio ConvocatoriaPlaVerDAO */
    @Autowired
    @Qualifier("convocatoriaPlaVerDAO")
    private ConvocatoriaPlaVerDAO convocatoriaPlaVerDAO;

    @Autowired
    @Qualifier("convocatoriaReqDAO")
    private ConvocatoriaReqDAO convocatoriaReqDAO;

    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /* Repositorio Catalogo Valores */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /** Repositorio ConvocatoriaSubmodDAO */
    @Autowired
    @Qualifier("convocatoriaSubmodDAO")
    private ConvocatoriaSubmodDAO convocatoriaSubmodDAO;

    /** Repositorio ConvocatoriaAreaService */
    @Autowired
    @Qualifier("convocatoriaAreaDAO")
    private ConvocatoriaAreaDAO convocatoriaAreaDAO;
    
    @Autowired
    @Qualifier("convocatoriaAreaSubareaDAO")
    private ConvocatoriaAreaSubareaDAO convocatoriaAreaSubareaDAO;
    
    @Autowired
    @Qualifier("financiacionDAO")
    private FinanciacionDAO financiacionDAO;
    
    @Autowired
    @Qualifier("financiacionDatoDAO")
    private FinanciacionDatoDAO financiacionDatoDAO;

    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    @Autowired
    @Qualifier("convocatoriaCoorDAO")
    private ConvocatoriaCoorDAO convocatoriaCoorDAO;

    @Autowired
    @Qualifier("evaluacionDAO")
    private EvaluacionDAO evaluacionDAO;
    
    @Autowired
    @Qualifier("informeDAO")
    private InformeDAO informeDAO;

    @Autowired
    @Qualifier("evaluacionNotaDAO")
    private EvaluacionNotaDAO evaluacionNotaDAO;

    @Autowired
    @Qualifier("apartadoDAO")
    private ApartadoDAO apartadoDAO;
    
    @Autowired
    @Qualifier("evaluacionJustecDAO")
    private EvaluacionJustecDAO evaluacionJustecDAO;
    
    @Autowired
    @Qualifier("evaluacionJustecNotaDAO")
    private EvaluacionJustecNotaDAO evaluacionJustecNotaDAO;

    @Autowired
    @Qualifier("evaluacionJustecApartaDAO")
    private EvaluacionJustecApartaDAO evaluacionJustecApartaDAO;

    @Autowired
    @Qualifier("alegacionDAO")
    private AlegacionDAO alegacionDAO;
    
    
    @Autowired
    @Qualifier("expedienteContratoDAO")
    private ExpedienteContratoDAO expedienteContratoDAO;
    
    
    @Autowired
    @Qualifier("expedienteContratoDocDAO")
    private ExpedienteContratoDocDAO expedienteContratoDocDAO;
    
    @Autowired
    @Qualifier("expedienteJusTecDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;
    
    @Autowired
    @Qualifier("expedienteJustecDocDAO")
    private ExpedienteJustecDocDAO expedienteJustecDocDAO;
    
    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;
    
    @Autowired
    @Qualifier("convocatoriaEvalTipoDocDAO")
    private ConvocatoriaEvalTipoDocDAO convocatoriaEvalTipoDocDAO;
    
    @Autowired
    @Qualifier("evaluacionTipoDocDAO")
    private EvaluacionTipoDocDAO evaluacionTipoDocDAO;
    
    @Autowired
    @Qualifier("convocatoriaDocEvalDAO")
    private ConvocatoriaDocEvalDAO convocatoriaDocEvalDAO;
    
    /**
     * Servicio de Impreso
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;
    
    /**
     * Servicio de ConvocatoriaCauContService
     */
    @Autowired
    @Qualifier("convocatoriaCauContService")
    private ConvocatoriaCauContService convocatoriaCauContService;
    
    /**
     * Servicio de ConfiguracionService
     */
    @Autowired
    @Qualifier("configuracionService")
    private ConfiguracionService configuracionService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private SessionFactory sessionFactory;
    

    /**
     * Buscar id.
     *
     * @param id the id Convocatorias
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    @Override
    public ConvocatoriaDTO buscarId(final Integer id) throws ServiceException {

        ConvocatoriaDTO dto = ConvocatoriaToDTO.INSTANCE.apply(this.find(id));

        List<ConvocatoriaActDTO> lstConvocatoriaAct = this.convocatoriaActService
                .buscarPorIdConvocatoria(dto.getIdConvocatoria());

        dto.setListConvocatoriaAct(lstConvocatoriaAct);

        List<ConvocatoriaSubmodDTO> listConvocatoriaSubmodDTO = this.convocatoriaSubmodService
                .obtenerSubmodalidadesPorConvocatoria(dto.getIdConvocatoria());
        if (listConvocatoriaSubmodDTO != null && !listConvocatoriaSubmodDTO.isEmpty()) {
            dto.setListConvocatoriaSubmod(listConvocatoriaSubmodDTO);
        }

        List<ConvocatoriaAreaDTO> listConvocatoriaAreaDTO = this.convocatoriaAreaService
                .obtenerAreasCientificasPorConvocatoria(dto.getIdConvocatoria());
        if (listConvocatoriaAreaDTO != null && !listConvocatoriaAreaDTO.isEmpty()) {
            dto.setListConvocatoriaArea(listConvocatoriaAreaDTO);
        }
        
        List<ConvocatoriaDocDTO> listConvocatoriaDocDTO = this.convocatoriaDocDAO
                .obtenerDocumentosConvocatoria(dto.getIdConvocatoria());
        if (listConvocatoriaDocDTO != null && !listConvocatoriaDocDTO.isEmpty()) {
            dto.setListConvocatoriaDoc(listConvocatoriaDocDTO);
        }
        
        List<ImpresoDTO> listImpresosDTO = this.impresoService
                .buscarPorIdConvocatoria(dto.getIdConvocatoria());
        if (listImpresosDTO != null && !listImpresosDTO.isEmpty()) {
            dto.setListConvocatoriaImp(listImpresosDTO);
        }
        
        //VMM 09/10/24 MD-657: Cargarmos la lista de todos los Gestores excepcionales de la convocatoria 
        //ya sean de la concesion o de la JCT para activar pestañas correspondientes en tramitacion
        List<ConvocatoriaGestorDTO> listGestoresDTO = this.convocatoriaGestorService
                .obtenerListaConvGestorPorIdConv(dto.getIdConvocatoria());
        if (listGestoresDTO != null && !listGestoresDTO.isEmpty()) {
            dto.setListConvocatoriaGest(listGestoresDTO);
        }
        
        
        // MD-2141 Cuando está configurado entidades cerradas se busca en la tabla entidades cerradas
        if (dto.getItEntidadCerrada() != null
                && Constantes.AfirmacionNegacion.S.equals(dto.getItEntidadCerrada())) {
            List<ConvocatoriaEntidadCerradaDTO> listaEntidadCerrada = this.convocatoriaEntidadCerradaService
                    .buscarIdConvocatoria(dto.getIdConvocatoria());
            if (listaEntidadCerrada != null) {
                List<CatalogosValoresDTO> listaTipoEntidad = listaEntidadCerrada.stream()
                        .map(ec -> ec.getEntidadCerrada()).collect(Collectors.toList());
                dto.setListaTipoEntidades(listaTipoEntidad);
            }
        } else {
            List<CatalogosValoresDTO> listaTipoEntidad = this.catalogosValoresService.obtenerTipoEntidades();
            if(listaTipoEntidad != null) {
                dto.setListaTipoEntidades(listaTipoEntidad);
            }
        }
        
        List<ConfiguracionDTO> listaConfiguracion = this.configuracionService.obtenerConfiguracion(dto);
        dto.setListaConfiguracion(listaConfiguracion);
        return dto;
    }

    /**
     * Buscar por el acronimo.
     *
     * @param cdAcronimo the id Convocatorias
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    @Override
    public List<ConvocatoriaDTO> buscarPorAcronimo(final String cdAcronimo)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_CDACRONIMO, cdAcronimo, Operador.EQUALS));
        // En desarrollo no se borran, por lo tanto, hacer comrpobacion.
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaToDTO.INSTANCE));
    }

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public ConvocatoriaPaginacionDTO buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, String cdUsuarioGestor)
            throws ServiceException {

        List<ConvocatoriaDTO> listaConvocatorias = new ArrayList<>(
                Lists.transform(this.find(filtros, orden), ConvocatoriaToDTO.INSTANCE));

        List<ConvocatoriaDTO> listaConvocatoriaFiltrada = this
                .compararConvocatoriaGestor(cdUsuarioGestor, listaConvocatorias);

        List<ConvocatoriaDTO> listaConvocatoriaPaginada = this
                .retornarPaginado(listaConvocatoriaFiltrada, index, pageSize);

        ConvocatoriaPaginacionDTO resultado = new ConvocatoriaPaginacionDTO();
        resultado.setLstConvocatorias(listaConvocatoriaPaginada);
        resultado.setTotalRegistros(listaConvocatoriaFiltrada.size());

        return resultado;

    }

    /**
     * Buscar convocatorias activas.
     *
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<ConvocatoriaDTO> buscarConvocatoriasActivas() throws ServiceException {

        return new ArrayList<>(Lists.transform(this.getDao().buscarConvocatoriasActivas(),
                ConvocatoriaToDTO.INSTANCE));

    }

    /**
     * Guardar.
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO guardar(final ConvocatoriaDTO convocatoriaDTO) throws ServiceException {

        Convocatoria convocatoria = ConvocatoriaToEntity.INSTANCE.apply(convocatoriaDTO);
        
        if (convocatoria.getIdModalidadConvocatoria() != null && (convocatoria
                .getIdModalidadConvocatoria().getTipoResolModalidad() == null)) {
            TipoResolModalidad tipoResolModalidad = this.tipoResolModalidadDAO
                    .findByIdModalidad(convocatoria.getIdModalidadConvocatoria().getIdValor());
            if (tipoResolModalidad != null) {
                convocatoria.getIdModalidadConvocatoria()
                        .setTipoResolModalidad(tipoResolModalidad);
                if (Constantes.TipoResolucionModalidad.RESOLUCION_INDIVIDUAL.equals(tipoResolModalidad.getCdTipoResolucion())) {
                    convocatoria.setFcIniAlegaciones(null);
                    convocatoria.setFcFinAlegaciones(null);
                }
            }
        }

        convocatoria = this.insertOrUpdate(convocatoria);
        
        // Insertamos las causas otros en cada tipo si no existen
        this.insertarCausaOtros(convocatoria.getIdConvocatoria());

//        if (convocatoriaDTO.getIdConvocatoria() == null) {
//
//            List<AtlasFilter> filtros = new ArrayList<>();
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                    utilConstantes.getGlobalCatalogos().get(Constantes.Cata.CAUSA_DENEGACION), Operador.EQUALS));
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_CDCODIGO,
//                    Constantes.CausaDenegacion.CODIGO_OTROS, Operador.EQUALS));
//
//            List<CatalogosValores> listaCausaDenegacion = this.catalogosValoresDAO.find(filtros,
//                    null);
//
//            if (!listaCausaDenegacion.isEmpty() && listaCausaDenegacion.get(0) != null) {
//                ConvocatoriaCau convocatoriaCau = new ConvocatoriaCau();
//                convocatoriaCau.setIdConvocatoria(convocatoria.getIdConvocatoria());
//                convocatoriaCau.setCausa(listaCausaDenegacion.get(0));
//                this.convocatoriaCauDAO.insert(convocatoriaCau);
//            }
            
//            Integer idCataCauDeneEval = utilConstantes.getGlobalCatalogos().get(Constantes.Cata.CAUSA_DENEGACION_EVALUACION);
//            
//            List<AtlasFilter> filtrosEval = new ArrayList<>();
//            filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                    idCataCauDeneEval, Operador.EQUALS));
//            filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//            filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_CDCODIGO,
//                    Constantes.CausaDenegacion.CODIGO_OTROS, Operador.EQUALS));
//
//            List<CatalogosValores> listaCausaDenegacionEval = this.catalogosValoresDAO
//                    .find(filtrosEval, null);
//
//            if (listaCausaDenegacionEval.isEmpty()) {
//                CatalogosValores cauEval = new CatalogosValores();
//                cauEval.setCatalogo(new Catalogos());
//                cauEval.getCatalogo().setIdCatalogo(idCataCauDeneEval);
//                cauEval.setDsValor(Constantes.CausaDenegacion.CODIGO_DS_OTROS);
//                cauEval.setCdCodigo(Constantes.CausaDenegacion.CODIGO_OTROS);
//                cauEval.setItMantenible(Constantes.AfirmacionNegacion.N);
//
//                cauEval = this.catalogosValoresDAO.insert(cauEval);
//
//                ConvocatoriaCauEval convocatoriaCauEval = new ConvocatoriaCauEval();
//                convocatoriaCauEval.setIdConvocatoria(convocatoria.getIdConvocatoria());
//                convocatoriaCauEval.setCausaEval(cauEval);
//                this.convocatoriaCauEvalDAO.insert(convocatoriaCauEval);
//            }

//        }
        
        // Buscamos si existe la causa de denagación OTROS de la denegación de evaluación de JCT
//        Integer idCataCauDeneEval = utilConstantes.getGlobalCatalogos()
//                .get(Constantes.Cata.CAUSA_DENEGACION_EVALUACION);
//        List<AtlasFilter> filtrosEval = new ArrayList<>();
//        filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                idCataCauDeneEval, Operador.EQUALS));
//        filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        filtrosEval.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_CDCODIGO,
//                Constantes.CausaDenegacion.CODIGO_OTROS, Operador.EQUALS));
//        List<CatalogosValores> listaCausaDenegacionEval = this.catalogosValoresDAO
//                .find(filtrosEval, null);
        
        // Si no existe la causa de denegación la creamos
//        CatalogosValores cauDenegEval = new CatalogosValores();
//        if (listaCausaDenegacionEval.isEmpty()) {
//            cauDenegEval.setCatalogo(new Catalogos());
//            cauDenegEval.getCatalogo().setIdCatalogo(idCataCauDeneEval);
//            cauDenegEval.setDsValor(Constantes.CausaDenegacion.CODIGO_DS_OTROS);
//            cauDenegEval.setCdCodigo(Constantes.CausaDenegacion.CODIGO_OTROS);
//            cauDenegEval.setItMantenible(Constantes.AfirmacionNegacion.N);
//            cauDenegEval.setNmConstante(Constantes.CausaDenegacion.CODIGO_CAUSA_JUSTEC_OTROS);
//            cauDenegEval = this.catalogosValoresDAO.insert(cauDenegEval);
//        }
        
        // Buscamos si la convocatoria tiene configurada causas de denegacion de revision administrativa de la JCT
//        List<AtlasFilter> filtrosCauEvalJusTec = new ArrayList<>();
//        filtrosCauEvalJusTec.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
//                convocatoria.getIdConvocatoria(), Operador.EQUALS));
//        filtrosCauEvalJusTec
//                .add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION_EVAL,
//                        Constantes.CausaDenegacion.CODIGO_CAUSA_JUSTEC_OTROS, Operador.EQUALS));
//        filtrosCauEvalJusTec.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        List<ConvocatoriaCauEval> lstConvocatoriaCauEval = this.convocatoriaCauEvalDAO
//                .find(filtrosCauEvalJusTec, null);

        // Si no hay causas de denegación configuradas, ponemos por defecto la causa OTROS
//        if (convocatoria.getNmMaxPeriodos() != null && convocatoria.getNmMaxPeriodos() > 0
//                && lstConvocatoriaCauEval.isEmpty()) {
//            
//            ConvocatoriaCauEval convocatoriaCauEval = new ConvocatoriaCauEval();
//            convocatoriaCauEval.setIdConvocatoria(convocatoria.getIdConvocatoria());
//            if (listaCausaDenegacionEval.isEmpty()) {
//                convocatoriaCauEval.setCausaEval(cauDenegEval);
//            } else {
//                convocatoriaCauEval.setCausaEval(listaCausaDenegacionEval.get(0));
//            }
//            this.convocatoriaCauEvalDAO.insert(convocatoriaCauEval);
//            
//        }
        
        // MD-1502 JRFM - Recuperamos el tipo de resoluciona modalidad de la convocatoria 
        // para ver si es del tipo conjunto o individual
        // Se crean los trámites
        this.convocatoriaPlaService.nuevaConvocatoria(convocatoria);

        // Se crean las comunicaciones
        this.convocatoriaComService.nuevaConvocatoria(convocatoria);
        
        // Buscamos si existe la causa OTROS en la denegación de contratos
//        Integer idCataCauDenContr = utilConstantes.getGlobalCatalogos().get(Constantes.Cata.CAUSA_DENEGACION_CONTRATOS);
//        List<AtlasFilter> filtrosEvalContrato = new ArrayList<>();
//        filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                idCataCauDenContr, Operador.EQUALS));
//        filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_CDCODIGO,
//                Constantes.CausaDenegacion.CODIGO_OTROS, Operador.EQUALS));
//        List<CatalogosValores> listaCausaDenegacionContratos = this.catalogosValoresDAO
//                 .find(filtrosEvalContrato, null);
        
        // si no existe causa de deneación la creamos
//        CatalogosValores cauDenagCont = new CatalogosValores();
//        if (listaCausaDenegacionContratos.isEmpty()) {
//            cauDenagCont.setCatalogo(new Catalogos());
//            cauDenagCont.getCatalogo().setIdCatalogo(idCataCauDenContr);
//            cauDenagCont.setDsValor(Constantes.CausaDenegacion.CODIGO_DS_OTROS);
//            cauDenagCont.setCdCodigo(Constantes.CausaDenegacion.CODIGO_OTROS);
//            cauDenagCont.setItMantenible(Constantes.AfirmacionNegacion.N);
//            cauDenagCont.setNmConstante(Constantes.CausaDenegacion.CODIGO_CAUSA_CONTRATO_OTROS);
//            cauDenagCont = this.catalogosValoresDAO.insert(cauDenagCont);
//        }
         
        // Buscamos si la convocatoria tiene configurada causas de denegacion de contratos
//        List<AtlasFilter> filtrosCauDeneCont = new ArrayList<>();
//        filtrosCauDeneCont.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
//                convocatoria.getIdConvocatoria(), Operador.EQUALS));
//        filtrosCauDeneCont
//                .add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_CAUSA_DENEGACION_CONT,
//                        Constantes.CausaDenegacion.CODIGO_CAUSA_CONTRATO_OTROS, Operador.EQUALS));
//        filtrosCauDeneCont.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//        List<ConvocatoriaCauCont> lstConvocatoriaCauDenCon = this.convocatoriaCauContDAO
//                .find(filtrosCauDeneCont, null);
        
        // Si no hay causas de denegación configuradas, ponemos por defecto la causa OTROS
//        if (convocatoria.getItContratos() != null
//                && Constantes.AfirmacionNegacion.S.equals(convocatoria.getItContratos())
//                && lstConvocatoriaCauDenCon.isEmpty()) {
//            ConvocatoriaCauCont convocatoriaCauCont = new ConvocatoriaCauCont();
//            convocatoriaCauCont.setIdConvocatoria(convocatoria.getIdConvocatoria());
//            if (listaCausaDenegacionContratos.isEmpty()) {
//                convocatoriaCauCont.setCausaCont(cauDenagCont);
//            } else {
//                convocatoriaCauCont.setCausaCont(listaCausaDenegacionContratos.get(0));
//            }
//            this.convocatoriaCauContDAO.insert(convocatoriaCauCont);
//        }
        
//        if (convocatoriaDTO.getIdConvocatoria() == null) {
//            
//            Integer idCataCauDenContr = utilConstantes.getGlobalCatalogos().get(Constantes.Cata.CAUSA_DENEGACION_CONTRATOS);
//            
//            List<AtlasFilter> filtrosEvalContrato = new ArrayList<>();
//            filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO,
//                    idCataCauDenContr, Operador.EQUALS));
//            filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//            filtrosEvalContrato.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_CDCODIGO,
//                    Constantes.CausaDenegacion.CODIGO_OTROS, Operador.EQUALS));
//        
//             List<CatalogosValores> listaCausaDenegacionContratos = this.catalogosValoresDAO
//                     .find(filtrosEvalContrato, null);
//    
//             if (listaCausaDenegacionContratos.isEmpty()) {
//                 CatalogosValores cauEval = new CatalogosValores();
//                 cauEval.setCatalogo(new Catalogos());
//                 cauEval.getCatalogo().setIdCatalogo(idCataCauDenContr);
//                 cauEval.setDsValor(Constantes.CausaDenegacion.CODIGO_DS_OTROS);
//                 cauEval.setCdCodigo(Constantes.CausaDenegacion.CODIGO_OTROS);
//                 cauEval.setItMantenible(Constantes.AfirmacionNegacion.N);
//                 cauEval = this.catalogosValoresDAO.insert(cauEval);
//    
//                 ConvocatoriaCauCont convocatoriaCauCont = new ConvocatoriaCauCont();
//                 convocatoriaCauCont.setIdConvocatoria(convocatoria.getIdConvocatoria());
//                 convocatoriaCauCont.setCausaCont(cauEval);
//                 this.convocatoriaCauContDAO.insert(convocatoriaCauCont);
//              }
//    
//              if (!listaCausaDenegacionContratos.isEmpty()) {
//                  ConvocatoriaCauCont convocatoriaCauCont = new ConvocatoriaCauCont();
//                  convocatoriaCauCont.setIdConvocatoria(convocatoria.getIdConvocatoria());
//                  convocatoriaCauCont.setCausaCont(listaCausaDenegacionContratos.get(0));
//                  this.convocatoriaCauContDAO.insert(convocatoriaCauCont);
//              }
//                
//          }
        
        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }
    
    /**
     * Método para insertar la causa otros en todos los tipos de causa
     * @param idConvocatoria
     * @throws ServiceException
     */
    private void insertarCausaOtros(Integer idConvocatoria) throws ServiceException {
        
        if (idConvocatoria != null) {
            
            List<Integer> lstCausasDenegacion = new ArrayList<>();
            lstCausasDenegacion.add(Constantes.CausaDenegacion.CODIGO_CAUSA_JUSTEC_OTROS);
            lstCausasDenegacion.add(Constantes.CausaDenegacion.CODIGO_CAUSA_ADMIN_OTROS);
            lstCausasDenegacion.add(Constantes.CausaDenegacion.CODIGO_CAUSA_CONTRATO_OTROS);
            lstCausasDenegacion.add(Constantes.CausaDenegacion.CODIGO_CAUSA_EVAL_OTROS);
            lstCausasDenegacion.add(Constantes.CausaDenegacion.CODIGO_CAUSA_ECO_OTROS);
            
            List<AtlasFilter> filtros = new ArrayList<>();
            
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria,
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_NM_CONSTANTE,
                    lstCausasDenegacion, Operador.IN));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            
            List<ConvocatoriaCau> lstConvocatoriaCau = this.convocatoriaCauDAO.find(filtros, null);

            if (lstConvocatoriaCau.stream()
                    .noneMatch(cc -> Objects.nonNull(cc.getTipoCausa())
                            && Objects.nonNull(cc.getTipoCausa().getNmConstante())
                            && cc.getTipoCausa().getNmConstante()
                                    .equals(Constantes.TipoCausaDenegacion.CAUSA_DENEG_ADM))) {
                this.insertarConvocatoriaCau(idConvocatoria,
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.CausaDenegacion.CODIGO_CAUSA_ADMIN_OTROS)),
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_ADM)));
            }

            if (lstConvocatoriaCau.stream()
                    .noneMatch(cc -> Objects.nonNull(cc.getTipoCausa())
                            && Objects.nonNull(cc.getTipoCausa().getNmConstante())
                            && cc.getTipoCausa().getNmConstante().equals(
                                    Constantes.TipoCausaDenegacion.CAUSA_DENEG_CONTRATOS))) {
                this.insertarConvocatoriaCau(idConvocatoria,
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.CausaDenegacion.CODIGO_CAUSA_CONTRATO_OTROS)),
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_CONTRATOS)));
            }

            if (lstConvocatoriaCau.stream()
                    .noneMatch(cc -> Objects.nonNull(cc.getTipoCausa())
                            && Objects.nonNull(cc.getTipoCausa().getNmConstante())
                            && cc.getTipoCausa().getNmConstante()
                                    .equals(Constantes.TipoCausaDenegacion.CAUSA_DENEG_EVAL))) {
                this.insertarConvocatoriaCau(idConvocatoria,
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.CausaDenegacion.CODIGO_CAUSA_EVAL_OTROS)),
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_EVAL)));
            }

            if (lstConvocatoriaCau.stream()
                    .noneMatch(cc -> Objects.nonNull(cc.getTipoCausa())
                            && Objects.nonNull(cc.getTipoCausa().getNmConstante())
                            && cc.getTipoCausa().getNmConstante().equals(
                                    Constantes.TipoCausaDenegacion.CAUSA_DENEG_JUST_CIENT_TEC))) {
                this.insertarConvocatoriaCau(idConvocatoria,
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.CausaDenegacion.CODIGO_CAUSA_JUSTEC_OTROS)),
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_JUST_CIENT_TEC)));
            }
            
            if (lstConvocatoriaCau.stream()
                    .noneMatch(cc -> Objects.nonNull(cc.getTipoCausa())
                            && Objects.nonNull(cc.getTipoCausa().getNmConstante())
                            && cc.getTipoCausa().getNmConstante().equals(
                                    Constantes.TipoCausaDenegacion.CAUSA_DENEG_JUST_ECO))) {
                this.insertarConvocatoriaCau(idConvocatoria,
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.CausaDenegacion.CODIGO_CAUSA_ECO_OTROS)),
                        this.catalogosValoresService.find(utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoCausaDenegacion.CAUSA_DENEG_JUST_ECO)));
            }
            
        }
    }
    
    /**
     * Método para insertar ConvocatoriaCau
     * @param idConvocatoria
     * @param causa
     * @param tipoCausa
     */
    private void insertarConvocatoriaCau(Integer idConvocatoria, CatalogosValores causa,
            CatalogosValores tipoCausa) {
        ConvocatoriaCau convocatoriaCau = new ConvocatoriaCau();
        convocatoriaCau.setIdConvocatoria(idConvocatoria);
        convocatoriaCau.setCausa(causa);
        convocatoriaCau.setTipoCausa(tipoCausa);
        this.convocatoriaCauDAO.insert(convocatoriaCau);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO guardarBaseOtraConvocatoria(final ConvocatoriaDTO convocatoriaDTO,
            final Integer idConvocatoriaExistente) throws ServiceException {

        ConvocatoriaDTO convocatoriaRegistrada = null;

        // Map que se devuelve relleno despues de guardar los nuevos actores.
        // Contiene los "Key" con el idActor de la convocatoria anterior, y los
        // "value" con el correspondiente idActor de la nueva convocatoria.
        Map<Integer, Integer> mapRelacionIdActores = new HashMap<>();

        try {

            if (idConvocatoriaExistente != null) {

                convocatoriaDTO.setIdConvocatoria(null);
                convocatoriaDTO.setFcAltaFila(null);
                convocatoriaDTO.setCdUsuAlta(null);
                convocatoriaDTO.setFcBajaFila(null);
                convocatoriaDTO.setCdUsuBaja(null);
                convocatoriaDTO.setFcModifFila(null);
                convocatoriaDTO.setCdUsuModif(null);
                convocatoriaDTO.setFcIniContrato(null);
                convocatoriaDTO.setFcFinContrato(null);
                convocatoriaDTO.setNmDiasAmpliContrato(null);
                convocatoriaDTO.setItTrasladoFechasCont("N");

                Convocatoria convocatoria = this
                        .insert(ConvocatoriaToEntity.INSTANCE.apply(convocatoriaDTO));

                List<AtlasFilter> lstFiltros = new ArrayList<>();
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                        idConvocatoriaExistente, Operador.EQUALS));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                // filtro puesto por el cambio de id a DTO en los dominios
                List<AtlasFilter> lstFiltros2 = new ArrayList<>();
                lstFiltros2
                        .add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                                idConvocatoriaExistente, Operador.EQUALS));
                lstFiltros2.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ConvocatoriaEur> lstConvEur = this.convocatoriaEurDAO.find(lstFiltros2, null);
                
                List<ConvocatoriaEntidadCerrada> lstConvEntCer = this.convocatoriaEntidadCerradaService
                        .find(lstFiltros2, null);
                
                List<ConvocatoriaAct> lstConvAct = this.convocatoriaActDAO.find(lstFiltros, null);

                List<ConvocatoriaCom> lstConvCom = this.convocatoriaComDAO.find(lstFiltros, null);
                List<ComunicacionCorreoActor> lstComuniCorreoActCom = new ArrayList<>();
                List<ComunicacionNoteActor> lstComuniNoteActCom = new ArrayList<>();
                
                for(ConvocatoriaCom convocatoriaCom : lstConvCom) {
                    
                    List<AtlasFilter> filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_COM,
                            convocatoriaCom.getIdConvocatoriaCom(), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                
                    List<ComunicacionCorreoActor> lstComuniCorreoActComAux = this.comunicacionCorreoActorDAO.find(filtros, null);
                    if (!lstComuniCorreoActComAux.isEmpty()) {
                        for(ComunicacionCorreoActor comunicacionCorreoActorAux : lstComuniCorreoActComAux) {
                            lstComuniCorreoActCom.add(comunicacionCorreoActorAux);
                            lstComuniCorreoActComAux =  new ArrayList<>();
                        }
                    }
                    
                    // MD-1525 JRFM - Bloque de actores note
                    List<ComunicacionNoteActor> lstComuniNoteActComAux = this.comunicacionNoteActorDAO.find(filtros, null);
                    if (!lstComuniNoteActComAux.isEmpty()) {
                        for(ComunicacionNoteActor comunicacionNoteActorAux : lstComuniNoteActComAux) {
                            lstComuniNoteActCom.add(comunicacionNoteActorAux);
                            lstComuniNoteActComAux =  new ArrayList<>();
                        }
                    }
                    
                }
                
                List<ConvocatoriaCauCont> listaCausaDenegacionContratos = this.convocatoriaCauContService.buscarIdConvocatoria(idConvocatoriaExistente);
       
                if (!listaCausaDenegacionContratos.isEmpty()) {
                    
                    for (ConvocatoriaCauCont convocatoriaCauCont : listaCausaDenegacionContratos) {
                        convocatoriaCauCont.setIdConvocatoria(convocatoria.getIdConvocatoria());
                        this.convocatoriaCauContDAO.insert(convocatoriaCauCont);
                    }
                
                }
       
                List<ConvocatoriaSubmod> lstConvSubmod = this.convocatoriaSubmodDAO.find(lstFiltros,
                        null);
                List<ConvocatoriaEnl> lstConvEnl = this.convocatoriaEnlDAO.find(lstFiltros, null);
                List<ConvocatoriaDoc> lstConvDoc = this.convocatoriaDocDAO.find(lstFiltros, null);
                List<Impreso> lstImpreso = this.impresoDAO.buscarPorIdConvocatoria(idConvocatoriaExistente);
                List<ConvocatoriaCau> lstConvCau = this.convocatoriaCauDAO.find(lstFiltros, null);
                List<ConvocatoriaCauEval> lstConvCauEval = this.convocatoriaCauEvalDAO
                        .find(lstFiltros, null);
                List<ConvocatoriaTipoDoc> lstConvTip = this.convocatoriaTipDAO.find(lstFiltros,
                        null);
                List<ConvocatoriaArea> lstConvArea = this.convocatoriaAreaDAO.find(lstFiltros,
                        null);
                List<ConvocatoriaPla> lstConvPlantilla = this.convocatoriaPlaDAO.find(lstFiltros,
                        null);
                List<ConvocatoriaReq> lstConvRequisito = this.convocatoriaReqDAO.find(lstFiltros,
                        null);

                List<Evaluacion> lstConvFormEval = this.evaluacionDAO.find(lstFiltros, null);
                
                List<Informe> lstInforme = this.informeDAO.find(lstFiltros, null);

                List<Alegacion> lstConvAlegacion = this.alegacionDAO.find(lstFiltros, null);
                
                List<EvaluacionJustec> lstConvFormEvalJustec = this.evaluacionJustecDAO.find(lstFiltros, null);
                
                List<Financiacion> lstConvFinan = this.financiacionDAO.find(lstFiltros, null);
                
                List<ConvocatoriaEvalTipoDoc> lstConvEvalTipoDoc = this.convocatoriaEvalTipoDocDAO
                        .find(lstFiltros, null);
                
                List<ConvocatoriaDocEval> lstConvDocEval = this.convocatoriaDocEvalDAO.find(lstFiltros, null);

                this.guardarBaseOtraConvocatoriaEur(lstConvEur, convocatoria);
                
                this.guardarBaseOtraConvocatoriaEntCer(lstConvEntCer, convocatoria);

                mapRelacionIdActores = this.guardarBaseOtraConvocatoriaAct(lstConvAct,
                        convocatoria.getIdConvocatoria());
                
                // MD-1525 JRFM - Bloque de actores note
                this.guardarBaseOtraConvocatoriaCom(lstConvCom, lstComuniCorreoActCom, lstComuniNoteActCom, convocatoria.getIdConvocatoria(), mapRelacionIdActores);

                this.guardarBaseOtraConvocatoriaEnl(lstConvEnl, convocatoria.getIdConvocatoria());

                // JSC - Se comenta. No se requiere que se copie los documentos
                // de la convocatoria
                // Al no estar seguro el GA de esta funcionalidad se deja
                // comentado.
                // Borrar cuando este seguro.
                // this.guardarBaseOtraConvocatoriaDoc(lstConvDoc,
                // convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaSubmod(lstConvSubmod,
                        convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaCau(lstConvCau, convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaCauEval(lstConvCauEval,
                        convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaArea(lstConvArea, convocatoria.getIdConvocatoria());
                
                this.guardarBaseOtraConvocatoriaFinan(lstConvFinan, convocatoria.getIdConvocatoria(),
                        mapRelacionIdActores);

                this.guardarBaseOtraConvocatoriaTipoDoc(lstConvTip,
                        convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaPla(lstConvPlantilla,
                        convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaReq(lstConvRequisito,
                        convocatoria.getIdConvocatoria(), mapRelacionIdActores);

                this.guardarBaseOtraConvocatoriaImpresos(lstImpreso,
                        convocatoria.getIdConvocatoria(), mapRelacionIdActores);

                this.guardarBaseOtraConvocatoriaFormularioEvaluacion(lstConvFormEval,
                        convocatoria.getIdConvocatoria());
                
                this.guardarBaseOtraConvocatoriaInforme(lstInforme,
                        convocatoria.getIdConvocatoria());

                this.guardarBaseOtraConvocatoriaAlegaciones(lstConvAlegacion,
                        convocatoria.getIdConvocatoria());
                
                this.guardarBaseOtraConvocatoriaFormularioEvaluacionJustec(lstConvFormEvalJustec,
                        convocatoria.getIdConvocatoria());
                
                this.guardarBaseOtraConvocatoriaConvEvalTipoDoc(lstConvEvalTipoDoc,
                        convocatoria.getIdConvocatoria());
                
                //MD-2793: copiamos los docs de evaluacion de la convocatoria
                this.guardarBaseOtraConvocatoriaConvDocEval(lstConvDocEval,
                        convocatoria.getIdConvocatoria());

                convocatoriaRegistrada = ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
            }

        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

        return convocatoriaRegistrada;
    }

    private void guardarBaseOtraConvocatoriaConvDocEval(List<ConvocatoriaDocEval> lstConvDocEval,
            Integer idConvocatoria) {
        if (Objects.nonNull(lstConvDocEval)) {
            for (ConvocatoriaDocEval convDocEval : lstConvDocEval) {
                ConvocatoriaDocEval convDocEvalNew = new ConvocatoriaDocEval();
                convDocEvalNew.setIdConvocatoria(idConvocatoria);
                convDocEvalNew.setIdTipoDocumento(convDocEval.getIdTipoDocumento());
                convDocEvalNew.setPersona(convDocEval.getPersona());
                convDocEvalNew.setCdFica(convDocEval.getCdFica());
                convDocEvalNew.setItCoorEval(convDocEval.getItCoorEval());
                convDocEvalNew.setDsNombreFichero(convDocEval.getDsNombreFichero());
                convDocEvalNew.setEstado(convDocEval.getEstado());
                this.convocatoriaDocEvalDAO.insert(convDocEvalNew);
            }
        }        
    }

    /**
     *
     * @param lstConvEur
     * @param convocatoria
     */
    private void guardarBaseOtraConvocatoriaEur(List<ConvocatoriaEur> lstConvEur,
            Convocatoria convocatoria) {
        for (ConvocatoriaEur convocatoriaEur : lstConvEur) {
            ConvocatoriaEur newConvEur = new ConvocatoriaEur();
            newConvEur.setConvocatoria(convocatoria);
            newConvEur.setFondoEuropeo(convocatoriaEur.getFondoEuropeo());
            this.convocatoriaEurDAO.insert(newConvEur);
        }
    }
    
    private void guardarBaseOtraConvocatoriaEntCer(List<ConvocatoriaEntidadCerrada> lstEntCer, Convocatoria convocatoria) {
        for (ConvocatoriaEntidadCerrada convEntCer : lstEntCer) {
            ConvocatoriaEntidadCerrada newConvEntCer = new ConvocatoriaEntidadCerrada();
            newConvEntCer.setConvocatoria(convocatoria);
            newConvEntCer.setEntidadCerrada(convEntCer.getEntidadCerrada()); 
            this.convocatoriaEntidadCerradaDAO.insert(newConvEntCer);
        }
    }

    /**
     * 3
     *
     * @param lstConvAct
     * @param idConvocatoria
     * @return
     */
    private Map<Integer, Integer> guardarBaseOtraConvocatoriaAct(List<ConvocatoriaAct> lstConvAct,
            Integer idConvocatoria) {

        // Map para guardar la relacion entre el idActor antiguo y el idActor
        // nuevo.
        Map<Integer, Integer> mapIdActores = new HashMap<>();

        List<ConvocatoriaAct> lstConvActoresNuevos = new ArrayList<>();

        for (ConvocatoriaAct convocatoriaAct : lstConvAct) {
            ConvocatoriaAct newConvAct = new ConvocatoriaAct();
            newConvAct.setIdConvocatoria(idConvocatoria);
            newConvAct.setTlDescripcion(convocatoriaAct.getTlDescripcion());
            newConvAct.setItGeneraExpediente(convocatoriaAct.getItGeneraExpediente());
            newConvAct.setTipoActor(convocatoriaAct.getTipoActor());
            newConvAct.setTipoActorFisJur(convocatoriaAct.getTipoActorFisJur());
            newConvAct.setIdentificacion(convocatoriaAct.getIdentificacion());
            newConvAct.setNmMax(convocatoriaAct.getNmMax());
            newConvAct.setNmMin(convocatoriaAct.getNmMin());
            newConvAct.setItContrato(convocatoriaAct.getItContrato());
            newConvAct.setItCumplimentaFormulario(convocatoriaAct.getItCumplimentaFormulario());

            // Se guardan originalmente los idActorSolicitante, para luego hacer
            // la conversión al nuevo idActor.
            newConvAct.setIdActorSolicitante(convocatoriaAct.getIdActorSolicitante());
            newConvAct.setIdActorCoordinador(convocatoriaAct.getIdActorCoordinador());

            ConvocatoriaAct temp = this.convocatoriaActDAO.insert(newConvAct);

            lstConvActoresNuevos.add(temp);

            // Se guarda una relacion del idActor anterior
            // en la Key del map, y el idActor nuevo en el valor.

            mapIdActores.put(convocatoriaAct.getIdActor(), temp.getIdActor());

        }
        for (ConvocatoriaAct convocatoriaActNuevos : lstConvActoresNuevos) {

            // Si el idActorSolicitante no es nulo, se busca en el map la
            // coincidencia del idActor antiguo, con el nuevo. Para asignarlo a
            // idActorSolicitante

            if (null != convocatoriaActNuevos.getIdActorSolicitante()) {

                convocatoriaActNuevos.setIdActorSolicitante(
                        mapIdActores.get(convocatoriaActNuevos.getIdActorSolicitante()));
            }
            
            if (null != convocatoriaActNuevos.getIdActorCoordinador()) {

                convocatoriaActNuevos.setIdActorCoordinador(
                        mapIdActores.get(convocatoriaActNuevos.getIdActorCoordinador()));
            }
            
        }
        return mapIdActores;
    }

    /**
     *
     * @param lstConvCom
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private void guardarBaseOtraConvocatoriaCom(List<ConvocatoriaCom> lstConvCom, 
            List<ComunicacionCorreoActor> lstComuniCorreoActCom,
            List<ComunicacionNoteActor> lstComuniNoteActCom,
            Integer idConvocatoria, Map<Integer, Integer> mapRelacionIdActores) throws ServiceException {
        
        for (ConvocatoriaCom convocatoriaCom : lstConvCom) {
            
            ConvocatoriaCom newConvCom = new ConvocatoriaCom();
            newConvCom.setIdConvocatoria(idConvocatoria);
            newConvCom.setTramite(convocatoriaCom.getTramite());
            newConvCom.setItNote(convocatoriaCom.getItNote());
            newConvCom.setItCorreo(convocatoriaCom.getItCorreo());
               
            if (mapRelacionIdActores != null) {
                for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {

                    if (convocatoriaCom.getConvocatoriaActNote() != null && mapIdActor.getKey().equals(convocatoriaCom.getConvocatoriaActNote().getIdActor())) {
                        newConvCom.setConvocatoriaActNote(ConvocatoriaActToEntity.INSTANCE.apply( this.convocatoriaActService.buscarId(mapIdActor.getValue())));
                    }
                    
                    if (convocatoriaCom.getConvocatoriaActCorreo() != null && mapIdActor.getKey().equals(convocatoriaCom.getConvocatoriaActCorreo().getIdActor())) {
                        newConvCom.setConvocatoriaActCorreo(ConvocatoriaActToEntity.INSTANCE.apply( this.convocatoriaActService.buscarId(mapIdActor.getValue())));
                    }
                    
                }
            }     
            
//            newConvCom.setConvocatoriaActNote(convocatoriaCom.getConvocatoriaActNote());
//            newConvCom.setConvocatoriaActCorreo(convocatoriaCom.getConvocatoriaActCorreo());
            
            this.convocatoriaComDAO.insert(newConvCom);
            Integer idConvocatoriaComNuevo = newConvCom.getIdConvocatoriaCom();
            
            for (ComunicacionCorreoActor comunicacionCorreoActor : lstComuniCorreoActCom) {
                if (comunicacionCorreoActor.getIdConvocatoriaCom().equals(convocatoriaCom.getIdConvocatoriaCom())) {
                    ComunicacionCorreoActor comunicacionCorreoActorAux = new ComunicacionCorreoActor();
                    comunicacionCorreoActorAux.setIdComunicacionActorCorreo(comunicacionCorreoActor.getIdComunicacionActorCorreo());
                    comunicacionCorreoActorAux.setIdConvocatoriaCom(idConvocatoriaComNuevo);
                    if (mapRelacionIdActores != null) {
                        for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {
                            if (comunicacionCorreoActor.getIdActor() != null && mapIdActor.getKey().equals(comunicacionCorreoActor.getIdActor())) {
                                comunicacionCorreoActorAux.setIdActor(mapIdActor.getValue());
                            } 
                        }
                    }                                 
                    comunicacionCorreoActorAux.setIdActor(comunicacionCorreoActor.getIdActor());
                    comunicacionCorreoActorAux.setCdUsuAlta(comunicacionCorreoActor.getCdUsuAlta());
                    comunicacionCorreoActorAux.setFcAltaFila(new Date());
                    this.comunicacionCorreoActorDAO.insert(comunicacionCorreoActorAux);
                }
            }
            
            for (ComunicacionNoteActor comunicacionNoteActor : lstComuniNoteActCom) {
                if (comunicacionNoteActor.getIdConvocatoriaCom().equals(convocatoriaCom.getIdConvocatoriaCom())) {
                    ComunicacionNoteActor comunicacionNoteActorAux = new ComunicacionNoteActor();
                    comunicacionNoteActorAux.setIdComunicacionActorNote(comunicacionNoteActor.getIdComunicacionActorNote());
                    comunicacionNoteActorAux.setIdConvocatoriaCom(idConvocatoriaComNuevo);
                    if (mapRelacionIdActores != null) {
                        for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {
                            if (comunicacionNoteActor.getIdActor() != null && mapIdActor.getKey().equals(comunicacionNoteActor.getIdActor())) {
                                comunicacionNoteActorAux.setIdActor(mapIdActor.getValue());
                            } 
                        }
                    }                                 
                    comunicacionNoteActorAux.setIdActor(comunicacionNoteActor.getIdActor());
                    comunicacionNoteActorAux.setCdUsuAlta(comunicacionNoteActor.getCdUsuAlta());
                    comunicacionNoteActorAux.setFcAltaFila(new Date());
                    this.comunicacionNoteActorDAO.insert(comunicacionNoteActorAux);
                }
            }
            
        }
        
    }

    /**
     *
     * @param lstConvSubmod
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaSubmod(List<ConvocatoriaSubmod> lstConvSubmod,
            Integer idConvocatoria) {
        for (ConvocatoriaSubmod convocatoriaSubmod : lstConvSubmod) {
            ConvocatoriaSubmod newConvSubmod = new ConvocatoriaSubmod();
            newConvSubmod.setIdConvocatoria(idConvocatoria);
            newConvSubmod.setDsSubmodalidad(convocatoriaSubmod.getDsSubmodalidad());
            newConvSubmod.setTlSubmodalidad(convocatoriaSubmod.getTlSubmodalidad());
            this.convocatoriaSubmodDAO.insert(newConvSubmod);
        }
    }

    /**
     *
     * @param lstConvEnl
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaEnl(List<ConvocatoriaEnl> lstConvEnl,
            Integer idConvocatoria) {
        for (ConvocatoriaEnl convocatoriaEnl : lstConvEnl) {
            ConvocatoriaEnl newConvEnl = new ConvocatoriaEnl();
            newConvEnl.setIdConvocatoria(idConvocatoria);
            newConvEnl.setDsDescripcion(convocatoriaEnl.getDsDescripcion());
            newConvEnl.setTlEnlace(convocatoriaEnl.getTlEnlace());
            this.convocatoriaEnlDAO.insert(newConvEnl);
        }
    }

    /**
     *
     * @param lstConvReq
     * @param idConvocatoria
     * @param mapRelacionIdActores 
     * @throws ServiceException 
     */
    private void guardarBaseOtraConvocatoriaReq(List<ConvocatoriaReq> lstConvReq,
            Integer idConvocatoria, Map<Integer, Integer> mapRelacionIdActores) throws ServiceException {
        for (ConvocatoriaReq convocatoriaReq : lstConvReq) {
            ConvocatoriaReq newConvReq = new ConvocatoriaReq();
            newConvReq.setIdConvocatoria(idConvocatoria);
            newConvReq.setRequisito(convocatoriaReq.getRequisito());
            ConvocatoriaAct convocatoriaAct = null;
            if (mapRelacionIdActores != null) {
                for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {

                    if (convocatoriaReq.getConvocatoriaAct() != null && mapIdActor.getKey().equals(convocatoriaReq.getConvocatoriaAct().getIdActor())) {                        
                        convocatoriaAct  = ConvocatoriaActToEntity.INSTANCE.apply(this.convocatoriaActService.buscarId(mapIdActor.getValue()));
                        convocatoriaAct.setIdConvocatoria(idConvocatoria);
                    }  
                }
            }  
           
//            ConvocatoriaAct convocatoriaAct = convocatoriaReq.getConvocatoriaAct();
           
            newConvReq.setConvocatoriaAct(convocatoriaAct);
            if (convocatoriaReq.getConvocatoriaSubmod() != null) {
                String dsSubmodalidad = convocatoriaReq.getConvocatoriaSubmod().getDsSubmodalidad();
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_SUBMOD_DSSUBMODALIDAD,
                                dsSubmodalidad, Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                        Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                List<ConvocatoriaSubmod> lstSubMod = this.convocatoriaSubmodDAO.find(filtros, null);
                if (lstSubMod != null && !lstSubMod.isEmpty()) {
                    newConvReq.setConvocatoriaSubmod(lstSubMod.get(0));
                }
            }
            this.convocatoriaReqDAO.insert(newConvReq);
        }
    }

    /**
     *
     * @param lstConvDoc
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaDoc(List<ConvocatoriaDoc> lstConvDoc,
            Integer idConvocatoria) {
        for (ConvocatoriaDoc convocatoriaDoc : lstConvDoc) {
            ConvocatoriaDoc newConvDoc = new ConvocatoriaDoc();
            newConvDoc.setIdConvocatoria(idConvocatoria);
            newConvDoc.setCdFica(convocatoriaDoc.getCdFica());
            newConvDoc.setIdTipoDocumento(convocatoriaDoc.getIdTipoDocumento());
            newConvDoc.setDsNombreFichero(convocatoriaDoc.getDsNombreFichero());
            this.convocatoriaDocDAO.insert(newConvDoc);
        }
    }

    /**
     *
     * @param lstConvArea
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaArea(List<ConvocatoriaArea> lstConvArea,
            Integer idConvocatoria) {
        for (ConvocatoriaArea convocatoriaArea : lstConvArea) {
            ConvocatoriaArea newConvArea = new ConvocatoriaArea();
            newConvArea.setIdConvocatoria(idConvocatoria);
            newConvArea.setArea(convocatoriaArea.getArea());
            this.convocatoriaAreaDAO.insert(newConvArea);
      
            //VMM 14/03/25 MD 1294: copiar las subareas de la convocatoria
            //List<ConvocatoriaAreaSubarea> lstConvAreaSubarea = this.convocatoriaAreaSubareaDAO.find(lstFiltros,null);
            //ver si esta lista de financiacion dato esta rellena...
            if (convocatoriaArea.getConvocatoriaAreaSubarea() != null && !convocatoriaArea.getConvocatoriaAreaSubarea().isEmpty()) {
                for (ConvocatoriaAreaSubarea convocatoriaAreaSubarea : convocatoriaArea.getConvocatoriaAreaSubarea()) {
                    ConvocatoriaAreaSubarea newConvAreaSubarea = new ConvocatoriaAreaSubarea();
                    newConvAreaSubarea.setIdConvocatoriaArea(newConvArea.getIdConvocatoriaArea());
                    newConvAreaSubarea.setSubarea(convocatoriaAreaSubarea.getSubarea());
                    this.convocatoriaAreaSubareaDAO.insert(newConvAreaSubarea);
                }
            }
        }
    }
    
   
   /**
   *
   * @param lstConvFinan
   * @param idConvocatoria
 * @throws ServiceException 
   */
  private void guardarBaseOtraConvocatoriaFinan(List<Financiacion> lstConvFinan,
          Integer idConvocatoria, Map<Integer, Integer> mapRelacionIdActores) throws ServiceException {
      for (Financiacion convocatoriaFinan : lstConvFinan) {
          Financiacion newConvFinan = new Financiacion();
          newConvFinan.setIdConvocatoria(idConvocatoria);
          newConvFinan.setTematica(convocatoriaFinan.getTematica());
          newConvFinan.setItTipo(convocatoriaFinan.getItTipo());

          //si no le meto el actor no puedo copiar el bloque de PP de los impresos que lo lleven...
          // Se recorre el map para encontrar la coincidencia de idActor
          // anterior en la tabla finan a copiar, y poner el correspondiente
          // idActor nuevo en la nueva tabla finan.
          if (mapRelacionIdActores != null) {
              for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {

                  // Cuando se encuentra la coincidencia en la clave, se usa
                  // su valor (el idActor del nuevo actor) para buscar la
                  // entidad en base de datos y asignarla a la tabla de finan.
                  if (convocatoriaFinan.getIdActor() != null && mapIdActor.getKey()
                          .equals(convocatoriaFinan.getIdActor().getIdActor())) {
                      newConvFinan.setIdActor(ConvocatoriaActToEntity.INSTANCE.apply(
                              this.convocatoriaActService.buscarId(mapIdActor.getValue())));
                  }
              }
          }
          
          this.financiacionDAO.insert(newConvFinan);
          
          if (convocatoriaFinan.getListaFinanciacionDato() != null && !convocatoriaFinan.getListaFinanciacionDato().isEmpty()) {
              for (FinanciacionDato convocatoriaFinanDato : convocatoriaFinan.getListaFinanciacionDato()) {
                  FinanciacionDato newConvFinanDato = new FinanciacionDato();
                  newConvFinanDato.setIdFinanciacion(newConvFinan.getIdFinanciacion());
                  newConvFinanDato.setTipo(convocatoriaFinanDato.getTipo());
                  newConvFinanDato.setNmAnualidad(convocatoriaFinanDato.getNmAnualidad());
                  newConvFinanDato.setNmImporteMin(convocatoriaFinanDato.getNmImporteMin());
                  newConvFinanDato.setNmImporteMax(convocatoriaFinanDato.getNmImporteMax());
                  newConvFinanDato.setNmImporteMaxAnual(convocatoriaFinanDato.getNmImporteMaxAnual());
                  newConvFinanDato.setItImporteFijo(convocatoriaFinanDato.getItImporteFijo());
                  newConvFinanDato.setNmOrden(convocatoriaFinanDato.getNmOrden());
                  this.financiacionDatoDAO.insert(newConvFinanDato);
              }
          }
      }
  }
  
    /**
     *
     * @param lstConvTip
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaTipoDoc(List<ConvocatoriaTipoDoc> lstConvTip,
            Integer idConvocatoria) {
        if (lstConvTip != null) {
            for (ConvocatoriaTipoDoc convocatoriaTip : lstConvTip) {
                ConvocatoriaTipoDoc newConvTip = new ConvocatoriaTipoDoc();
                newConvTip.setIdConvocatoria(idConvocatoria);
                newConvTip.setTipoDocumento(convocatoriaTip.getTipoDocumento());
                newConvTip.setTramite(convocatoriaTip.getTramite());
                newConvTip.setItObligatorio(convocatoriaTip.getItObligatorio());
                newConvTip.setItPermiDescaEval(convocatoriaTip.getItPermiDescaEval());
                newConvTip.setCdFicaPlantilla(convocatoriaTip.getCdFicaPlantilla());
                this.convocatoriaTipDAO.insert(newConvTip);
            }
        }
    }

    /**
     *
     * @param lstConvCau
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaCau(List<ConvocatoriaCau> lstConvCau,
            Integer idConvocatoria) {
        if (lstConvCau != null) {
            for (ConvocatoriaCau convocatoriaCau : lstConvCau) {
                ConvocatoriaCau newConvCau = new ConvocatoriaCau();
                newConvCau.setIdConvocatoria(idConvocatoria);
                newConvCau.setCausa(convocatoriaCau.getCausa());
                newConvCau.setTlNormativa(convocatoriaCau.getTlNormativa());
                newConvCau.setTipoCausa(convocatoriaCau.getTipoCausa());
                this.convocatoriaCauDAO.insert(newConvCau);
            }
        }
    }

    /**
     *
     * @param lstConvCauEval
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaCauEval(List<ConvocatoriaCauEval> lstConvCauEval,
            Integer idConvocatoria) {
        if (lstConvCauEval != null) {
            for (ConvocatoriaCauEval convocatoriaCauEval : lstConvCauEval) {
                ConvocatoriaCauEval newConvCauEval = new ConvocatoriaCauEval();
                newConvCauEval.setIdConvocatoria(idConvocatoria);
                newConvCauEval.setCausaEval(convocatoriaCauEval.getCausaEval());
                this.convocatoriaCauEvalDAO.insert(newConvCauEval);
            }
        }
    }

    /**
     *
     * @param lstConvPlantilla
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private void guardarBaseOtraConvocatoriaPla(List<ConvocatoriaPla> lstConvPlantilla,
            Integer idConvocatoria) throws ServiceException {
        if (lstConvPlantilla != null) {
            
            // MD-2190 No se debe copiar tramites que están de baja
            List<Integer> tramitesAlta = this.catalogosValoresService
                    .buscarPorCatalogo(utilConstantes.getGlobalCatalogos()
                            .get(Constantes.Cata.TRAMITES_GESTOR))
                    .stream().map(cv -> cv.getNmConstante()).collect(Collectors.toList());
            
            for (ConvocatoriaPla convPlantilla : lstConvPlantilla) {
                // MD-2190 filtramos para que sólo se copien los trámites que no están dados de baja
                if (tramitesAlta.contains(convPlantilla.getTramite().getNmConstante())) {
                    ConvocatoriaPla newConvPlantilla = new ConvocatoriaPla();
                    newConvPlantilla.setIdConvocatoria(idConvocatoria);
                    newConvPlantilla.setCdPlantilla(convPlantilla.getCdPlantilla());
                    newConvPlantilla.setTramite(convPlantilla.getTramite());
                    newConvPlantilla.setDsDescripcion(convPlantilla.getDsDescripcion());
                    newConvPlantilla.setImagen(convPlantilla.getImagen());
                    newConvPlantilla.setDsAsunto(convPlantilla.getDsAsunto());
          
                    newConvPlantilla = this.convocatoriaPlaDAO.insert(newConvPlantilla);
                    
                    //VMM 12009/25 MD-1921: no se estaba copiando la descripcion inferior ni los docs a subsanar en la plantilla de subsanacion admin
                    //hasta que no haga el insert de la nueva plantilla no puedo guardar estos campos con la referencia la nueva plantilla de subsanacion admin
                    if (convPlantilla.getTramite().getIdValor() != null && 
                            convPlantilla.getTramite().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))) {
                        
                        Integer idConvocatoriaPlaNew = newConvPlantilla.getIdConvocatoriaPla();
                        String cdUsuarioAltaNew = newConvPlantilla.getCdUsuAlta();
                        newConvPlantilla.setDsDescripcionInf(convPlantilla.getDsDescripcionInf());
                        
                        //consultamos la doc a subsanar de la plantilla
                        List<ConvocatoriaPlaDocDTO> lstConvocatoriaPlaDocDTO = this.convocatoriaPlaDocService.
                                consultarDocuSubsanaPlantilla(convPlantilla.getIdConvocatoriaPla());
                        
                        List<ConvocatoriaPlaDoc> lstConvocatoriaPlaDoc = lstConvocatoriaPlaDocDTO.stream()
                                .map(dto -> {
                                    ConvocatoriaPlaDoc entity = new ConvocatoriaPlaDoc();
                                    entity.setIdConvocatoriaPla(idConvocatoriaPlaNew);
                                    entity.setDsDocumentacion(dto.getDsDocumentacion());
                                    entity.setDsObservaciones(dto.getDsObservaciones());
                                    entity.setCdUsuAlta(cdUsuarioAltaNew);
                                    entity.setFcAltaFila(new Date());
                                    entity.setIdConvocatoriaPlaDoc(null); // nuevo id
                                    return entity;
                                })
                                .collect(Collectors.toList());
                        
                        newConvPlantilla.setLstConvocatoriaPlaDoc(lstConvocatoriaPlaDoc);
                        
                        newConvPlantilla = this.convocatoriaPlaDAO.merge(newConvPlantilla);
                    }
                            
                    this.guardarBaseOtraConvocatoriaPlaFir(convPlantilla.getLstConvocatoriaPlaFir(),
                            newConvPlantilla.getIdConvocatoriaPla());
    
                    this.guardarBaseOtraConvocatoriaPlaVer(convPlantilla.getLstConvocatoriaPlaVer(),
                            newConvPlantilla.getIdConvocatoriaPla());
                }
            }
        }
    }

    /**
     *
     * @param lstConvPlantillaFirmantes
     * @param idConvocatoriaPla
     */
    private void guardarBaseOtraConvocatoriaPlaFir(
            List<ConvocatoriaPlaFir> lstConvPlantillaFirmantes, Integer idConvocatoriaPla) {
        if (lstConvPlantillaFirmantes != null) {
            for (ConvocatoriaPlaFir convPlantillaFir : lstConvPlantillaFirmantes) {
                if (Objects.isNull(convPlantillaFir.getFcBajaFila())) {
                    ConvocatoriaPlaFir newConvPlantillaFir = new ConvocatoriaPlaFir();
                    newConvPlantillaFir.setIdConvocatoriaPla(idConvocatoriaPla);
                    newConvPlantillaFir.setCdFirmante(convPlantillaFir.getCdFirmante());
                    this.convocatoriaPlaFirDAO.insert(newConvPlantillaFir);
                }

            }
        }
    }

    /**
     *
     * @param lstConvPlantillaVerificadores
     * @param idConvocatoriaPla
     */
    private void guardarBaseOtraConvocatoriaPlaVer(
            List<ConvocatoriaPlaVer> lstConvPlantillaVerificadores, Integer idConvocatoriaPla) {
        if (lstConvPlantillaVerificadores != null) {
            for (ConvocatoriaPlaVer convPlantillaVer : lstConvPlantillaVerificadores) {
                if (Objects.isNull(convPlantillaVer.getFcBajaFila())) {
                    ConvocatoriaPlaVer newConvPlantillaVer = new ConvocatoriaPlaVer();
                    newConvPlantillaVer.setIdConvocatoriaPla(idConvocatoriaPla);
                    newConvPlantillaVer.setCdVerificador(convPlantillaVer.getCdVerificador());
                    this.convocatoriaPlaVerDAO.insert(newConvPlantillaVer);
                }

            }
        }
    }

    /**
     * Método para guardar copia información de impresos de otra convocatoria
     *
     * @param lstImpreso Lista de Impresos
     * @param idConvocatoria Identificador de convocatoria
     * @param mapRelacionIdActores map con la relacion de idActor anterior, con
     *            idActor nuevo.
     * @throws ServiceException
     */
    private void guardarBaseOtraConvocatoriaImpresos(List<Impreso> lstImpreso,
            Integer idConvocatoria, Map<Integer, Integer> mapRelacionIdActores)
            throws ServiceException {
        try {
            for (Impreso impreso : lstImpreso) {

                Impreso newImp = new Impreso();
                Convocatoria convAux = new Convocatoria();
                convAux.setIdConvocatoria(idConvocatoria);
                newImp.setConvocatoria(convAux);
                newImp.setCdIdentificacion(impreso.getCdIdentificacion());
                newImp.setDsDescripcion(impreso.getDsDescripcion());
                newImp.setTipo(impreso.getTipo());
                newImp.setItGeneraImpreso(impreso.getItGeneraImpreso());
                newImp.setDsImpresoOriginal(impreso.getDsImpresoOriginal());
                newImp.setDsPlantillaTecnica(impreso.getDsPlantillaTecnica());

                // Se recorre el map para encontrar la coincidencia de idActor
                // anterior en el impreso a copiar, y poner el correspondiente
                // idActor nuevo en el nuevo impreso.

                if (mapRelacionIdActores != null) {
                    for (Map.Entry<Integer, Integer> mapIdActor : mapRelacionIdActores.entrySet()) {

                        // Cuando se encuentra la coincidencia en la clave, se
                        // usa
                        // su valor (el idActor del nuevo actor) para buscar la
                        // entidad en base de datos y asignarla al impreso.
                        if (impreso.getConvocatoriaAct() != null && mapIdActor.getKey()
                                .equals(impreso.getConvocatoriaAct().getIdActor())) {
                            newImp.setConvocatoriaAct(ConvocatoriaActToEntity.INSTANCE.apply(
                                    this.convocatoriaActService.buscarId(mapIdActor.getValue())));
                        }

                    }
                }

                newImp.setCdFicaImpreso(impreso.getCdFicaImpreso());
                newImp.setCdFicaPlantilla(impreso.getCdFicaPlantilla());
                newImp.setImagenLogo(impreso.getImagenLogo());

                List<Bloque> lstBloque = new ArrayList<>(impreso.getLstBloque().stream()
                        .filter(f -> f.getFcBajaFila() == null)
                        .collect(Collectors.toList()));
                
                lstBloque.sort((b1, b2) -> {
                    int cmp = Integer.compare(b1.getNmOrden(), b2.getNmOrden());
                    if (cmp != 0) {
                        return cmp;
                    }
    
                    // Si numOrden es igual y ambos tienen idBloquePadre != null, ordenamos por subOrden
                    boolean tienePadre1 = b1.getIdBloquePadre() != null;
                    boolean tienePadre2 = b2.getIdBloquePadre() != null;
    
                    if (tienePadre1 && tienePadre2) {
                        return Comparator.nullsLast(Integer::compareTo)
                                         .compare(b1.getSubNmOrden(), b2.getSubNmOrden());
                    }

                    // Si solo uno tiene padre, el que no tiene padre va primero
                    if (tienePadre1 != tienePadre2) {
                        return tienePadre1 ? 1 : -1;
                    }
                    
                    return 0;
                });

                newImp.setLstBloque(new ArrayList<>());

                newImp = this.impresoDAO.insert(newImp);

                this.guardarBaseOtraConvocatoriaImpresoBloques(newImp, lstBloque);

            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Método para guardar copia información de bloques asociados a un impreso
     * de otra convocatoria
     *
     * @param impreso Datos del impreso
     * @param lstBloque Lista de bloques
     * @throws ServiceException
     */
    private void guardarBaseOtraConvocatoriaImpresoBloques(Impreso impreso, List<Bloque> lstBloque)
            throws ServiceException {
        try {
         // Mapa para guardar la correspondencia entre el ID original y el nuevo ID
            Map<Integer, Integer> mapaIdsBloques = new HashMap<>();

            for (Bloque bloque : lstBloque) {

                    Bloque newBlo = new Bloque();
                    newBlo.setIdFormato(bloque.getIdFormato());
                    newBlo.setIdTematica(bloque.getIdTematica());
                    newBlo.setImpreso(impreso);
                    newBlo.setNmFilas(bloque.getNmFilas());
                    newBlo.setNmOrden(bloque.getNmOrden());
                    newBlo.setTlAuxiliar(bloque.getTlAuxiliar());
                    newBlo.setTlAuxiliarInf(bloque.getTlAuxiliarInf());
                    newBlo.setItMultiBloque(bloque.getItMultiBloque());
                    newBlo.setItMultiRegistro(bloque.getItMultiRegistro());
                    newBlo.setSubNmOrden(bloque.getSubNmOrden());
                    newBlo.setItAutorizaConsulta(bloque.getItAutorizaConsulta());
                    newBlo.setItTipoSelDoc(bloque.getItTipoSelDoc());
                    
                    // ⭐ Lógica para asignar el nuevo ID del bloque padre ⭐
                    if (bloque.getIdBloquePadre() != null) {
                        // Obtenemos el nuevo ID del padre a partir del ID del padre original
                        Integer nuevoIdPadre = mapaIdsBloques.get(bloque.getIdBloquePadre());
                        if (nuevoIdPadre != null) {
                            newBlo.setIdBloquePadre(nuevoIdPadre);
                        }
                    }
                    
                    
                    newBlo = this.bloqueDAO.insert(newBlo);
                    
                    if (bloque.getIdBloquePadre() == null) {
                        mapaIdsBloques.put(bloque.getIdBloque(), newBlo.getIdBloque());
                    }
                    
                    List<BloqueComponente> lstBloqueComponente = new ArrayList<>(bloque
                            .getLstBloqueComponente().stream()
                            .filter(f -> f.getFcBajaFila() == null).collect(Collectors.toList()));
                    newBlo.setLstBloqueComponente(new ArrayList<>());
      

                    this.guardarBaseOtraConvocatoriaImpresoBloquesComponentes(newBlo,
                            lstBloqueComponente);
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }
    


    /**
     * Método para guardar copia información de bloques componentes asociados a
     * un bloque de otra convocatoria
     *
     * @param bloque Datos del bloque
     * @param lstBloqueComponente Lista de bloque componente
     * @throws ServiceException
     */
    private void guardarBaseOtraConvocatoriaImpresoBloquesComponentes(Bloque bloque,
            List<BloqueComponente> lstBloqueComponente) throws ServiceException {
        try {
            Integer idFormato = bloque.getIdFormato().getIdValor();

            for (BloqueComponente bloqueComponente : lstBloqueComponente) {
                if (bloqueComponente != null) {

                    BloqueComponente newBloCom = new BloqueComponente();
                    newBloCom.setBloque(bloque);                       
                    newBloCom.setItFila(bloqueComponente.getItFila());
                    newBloCom.setItColumna(bloqueComponente.getItColumna());
                    newBloCom.setNmOrden(bloqueComponente.getNmOrden());
                    newBloCom.setItObligatorio(bloqueComponente.getItObligatorio());
                    newBloCom.setItRelacion(bloqueComponente.getItRelacion());
                    
                    if(bloqueComponente.getTipoFinanciacion() != null && bloqueComponente.getTipoFinanciacion().getDsValor() != null ) {
                        newBloCom.setTipoFinanciacion(bloqueComponente.getTipoFinanciacion());
                    }
                    if(bloqueComponente.getTramite() != null) {
                        newBloCom.setTramite(bloqueComponente.getTramite());
                    }

                    if (bloqueComponente.getComponente() != null
                            && bloqueComponente.getComponente().getIdComponente() != null) {
                        
                        newBloCom.setComponente(bloqueComponente.getComponente());

                        if (idFormato.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO_FIJO))
                                || idFormato.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO))) {
                            Componente newCom = new Componente();
                            newCom.setNmLongitud(bloqueComponente.getComponente().getNmLongitud());
                            // newCom.setTlTextoFijo(bloqueComponente.getComponente().getTlTextoFijo());
                            // MD-2469 [ADM] Ampliar campo texto fijo en formulario
                            newCom.setClTextoFijo(bloqueComponente.getComponente().getClTextoFijo());
    
                            if (idFormato.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_TEXTO))) {
                                newCom.setTipo(
                                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CAMPO)));
                                newCom.setSubTipo(new CatalogosValores(
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.SubTipoComponente.ID_MULTI_TEXTO)));
                            }
    
                            newBloCom.setComponente(this.componenteDAO.insert(newCom));
                        }
                    }
                    //VMM 14/03/25 MD-1357: tambien copiamos los bloques componentes que no tengan componente por debajo
                    //por ejemplo para bloques de Presupuestos se deben copiar las partidas Presupuestarias
                    this.bloqueComponenteDAO.insert(newBloCom);
                }
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Metodo para copiar la configuracion de evaluacion / notas / apartados
     * asociados a la convocatoria
     *
     * @param lstFormEval
     * @param idConcatoria
     */
    private void guardarBaseOtraConvocatoriaFormularioEvaluacion(List<Evaluacion> lstFormEval,
            Integer idConvocatoria) {

        if (lstFormEval != null) {
            for (Evaluacion eval : lstFormEval) {
                Evaluacion newEval = new Evaluacion();
//                newEval.setIdConvocatoria(idConvocatoria);
                newEval.setDsTitulo(eval.getDsTitulo());
                newEval.setTlDescripcion(eval.getTlDescripcion());
                newEval.setNmOrden(eval.getNmOrden());
                newEval.setItCalificacion(eval.getItCalificacion());
                newEval.setItCoorEval(eval.getItCoorEval());
                newEval = this.evaluacionDAO.insert(newEval);

                for (EvaluacionNota evalNota : eval.getListaEvaluacionNota()) {
                    EvaluacionNota newEvalNota = new EvaluacionNota();
                    newEvalNota.setIdEvaluacion(newEval.getIdEvaluacion());
                    newEvalNota.setIdConvocatoria(idConvocatoria);
                    newEvalNota.setNota(evalNota.getNota());
                    newEvalNota.setNmA(evalNota.getNmA());
                    newEvalNota.setNmDe(evalNota.getNmDe());
                    this.evaluacionNotaDAO.insert(newEvalNota);
                }

                for (Apartado apart : eval.getListaApartado()) {
                    Apartado newApart = new Apartado();
                    newApart.setIdEvaluacion(newEval.getIdEvaluacion());
                    newApart.setDsTitulo(apart.getDsTitulo());
                    newApart.setTlDescripcion(apart.getTlDescripcion());
                    newApart.setTlTexto(apart.getTlTexto());
                    newApart.setNmOrden(apart.getNmOrden());
                    newApart.setNmCalificacionMax(apart.getNmCalificacionMax());
                    this.apartadoDAO.insert(newApart);
                }

            }
        }

    }
    
    /**
     * Metodo para copiar la configuracion de informe
     * asociados a la convocatoria
     *
     * @param lstInforme
     * @param idConcatoria
     */
    private void guardarBaseOtraConvocatoriaInforme(List<Informe> lstInforme,
            Integer idConvocatoria) {
        if (lstInforme != null) {
            for (Informe inf : lstInforme) {
                Informe infNew = new Informe();
                infNew.setIdInforme(inf.getIdInforme());
                infNew.setIdConvocatoria(idConvocatoria);
                infNew.setTipoInforme(inf.getTipoInforme());
                infNew.setDsTitulo(inf.getDsTitulo());
                infNew.setImagenLogo(inf.getImagenLogo());
                infNew.setImagenLogo2(inf.getImagenLogo2());
                infNew = this.informeDAO.insert(infNew);
                
                List<Evaluacion> lstEvaluacion = this.evaluacionDAO
                        .buscarPorInforme(inf.getIdInforme());
                for (Evaluacion eva : lstEvaluacion) {
                    Evaluacion evaNew = new Evaluacion();
                    evaNew.setDsTitulo(eva.getDsTitulo());
                    evaNew.setTlDescripcion(eva.getTlDescripcion());
                    evaNew.setNmOrden(eva.getNmOrden());
                    evaNew.setItCalificacion(eva.getItCalificacion());
                    evaNew.setImagenLogo(eva.getImagenLogo());
                    evaNew.setInforme(infNew);
                    evaNew = this.evaluacionDAO.insert(evaNew);
                    
                    List<Apartado> lstApartado = this.apartadoDAO
                            .buscarPorEvaluacion(eva.getIdEvaluacion());
                    for (Apartado apa : lstApartado) {
                        Apartado apaNew = new Apartado();
                        apaNew.setIdEvaluacion(evaNew.getIdEvaluacion());
                        apaNew.setDsTitulo(apa.getDsTitulo());
                        apaNew.setTlDescripcion(apa.getTlDescripcion());
                        apaNew.setTlTexto(apa.getTlTexto());
                        apaNew.setNmOrden(apa.getNmOrden());
                        apaNew.setNmCalificacionMax(apa.getNmCalificacionMax());
                        this.apartadoDAO.insert(apaNew);
                    }
                }
                
                //MD-2342: copiamos los documentos de evaluacion de cada informe eval
                List<EvaluacionTipoDocDTO> listadoDTO = this.evaluacionTipoDocDAO.
                        obtenerTipoDocumentosEvaluacion(inf.getIdInforme());
                List<EvaluacionTipoDoc> lstEvalTipoDoc = listadoDTO.stream()
                        .map(EvaluacionTipoDocToEntity.INSTANCE::apply)
                        .collect(Collectors.toList());
                for (EvaluacionTipoDoc evalTipoDoc: lstEvalTipoDoc) {
                    EvaluacionTipoDoc evalTipoDocNew = new EvaluacionTipoDoc();
                    evalTipoDocNew.setIdTipoDocumento(evalTipoDoc.getIdTipoDocumento());
                    evalTipoDocNew.setItObligatorio(evalTipoDoc.getItObligatorio());
                    evalTipoDocNew.setIdInforme(infNew.getIdInforme());
                    this.evaluacionTipoDocDAO.insert(evalTipoDocNew);
                }
            }
        }
    }
    
    /**
     * Metodo para copiar la configuracion de evaluacionJustec / EvaluacionJustecNota / EvaluacionJustecAparta
     * asociados a la convocatoria
     *
     * @param lstFormEvalJustec
     * @param idConcatoria
     */
    private void guardarBaseOtraConvocatoriaFormularioEvaluacionJustec(List<EvaluacionJustec> lstFormEvalJustec,
            Integer idConvocatoria) {

        if (lstFormEvalJustec != null) {
            for (EvaluacionJustec eval : lstFormEvalJustec) {
                EvaluacionJustec newEvalJustec = new EvaluacionJustec();
                newEvalJustec.setIdConvocatoria(idConvocatoria);
                newEvalJustec.setDsTitulo(eval.getDsTitulo());
                newEvalJustec.setTlDescripcion(eval.getTlDescripcion());
                newEvalJustec.setNmOrden(eval.getNmOrden());
                newEvalJustec.setItCalificacion(eval.getItCalificacion());
                newEvalJustec.setItTipoEval(eval.getItTipoEval());
                newEvalJustec.setImagenLogo(eval.getImagenLogo());
                newEvalJustec = this.evaluacionJustecDAO.insert(newEvalJustec);

                for (EvaluacionJustecNota evalNota : eval.getListaEvaluacionJustecNota()) {
                    EvaluacionJustecNota newEvalNota = new EvaluacionJustecNota();
                    newEvalNota.setIdEvaluacionJustec(newEvalJustec.getIdEvaluacionJustec());
                    newEvalNota.setIdConvocatoria(idConvocatoria);
                    newEvalNota.setNota(evalNota.getNota());
                    newEvalNota.setNmA(evalNota.getNmA());
                    newEvalNota.setNmDe(evalNota.getNmDe());
                    this.evaluacionJustecNotaDAO.insert(newEvalNota);
                }

                for (EvaluacionJustecAparta apart : eval.getListaEvaluacionJustecAparta()) {
                    EvaluacionJustecAparta newEvaluacionJustecApartaDAO = new EvaluacionJustecAparta();
                    newEvaluacionJustecApartaDAO.setIdEvaluacionJustec(newEvalJustec.getIdEvaluacionJustec());
                    newEvaluacionJustecApartaDAO.setDsTitulo(apart.getDsTitulo());
                    newEvaluacionJustecApartaDAO.setTlDescripcion(apart.getTlDescripcion());
                    newEvaluacionJustecApartaDAO.setTlTexto(apart.getTlTexto());
                    newEvaluacionJustecApartaDAO.setNmOrden(apart.getNmOrden());
                    newEvaluacionJustecApartaDAO.setNmCalificacionMax(apart.getNmCalificacionMax());
                    this.evaluacionJustecApartaDAO.insert(newEvaluacionJustecApartaDAO);
                }

            }
        }

    }

    /**
     * Metodo para crear copia de los registros de alegaciones
     *
     * @param lstAlegaciones
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaAlegaciones(List<Alegacion> lstAlegaciones,
            Integer idConvocatoria) {
        if (Objects.nonNull(lstAlegaciones)) {
            for (Alegacion aleg : lstAlegaciones) {
                Alegacion newAleg = new Alegacion();
                newAleg.setIdConvocatoria(idConvocatoria);
                newAleg.setDsTitulo(aleg.getDsTitulo());
                newAleg.setTipo(aleg.getTipo());
                newAleg.setNmOrden(aleg.getNmOrden());
                this.alegacionDAO.insert(newAleg);
            }
        }
    }
    
    /**
     * Método para crear copia de los registrso de alegaciones
     * 
     * @param lstConvEvalTipoDoc
     * @param idConvocatoria
     */
    private void guardarBaseOtraConvocatoriaConvEvalTipoDoc(
            List<ConvocatoriaEvalTipoDoc> lstConvEvalTipoDoc, Integer idConvocatoria) {
        if (Objects.nonNull(lstConvEvalTipoDoc)) {
            for (ConvocatoriaEvalTipoDoc convEvalTipDoc : lstConvEvalTipoDoc) {
                ConvocatoriaEvalTipoDoc convEvalTipoDocNew = new ConvocatoriaEvalTipoDoc();
                convEvalTipoDocNew.setIdConvocatoria(idConvocatoria);
                convEvalTipoDocNew.setIdTipoDocumento(convEvalTipDoc.getIdTipoDocumento());
                convEvalTipoDocNew.setIdNivel(convEvalTipDoc.getIdNivel());
                convEvalTipoDocNew.setCdFicaPlantilla(convEvalTipDoc.getCdFicaPlantilla());
                convEvalTipoDocNew.setItObligatorio(convEvalTipDoc.getItObligatorio());
                this.convocatoriaEvalTipoDocDAO.insert(convEvalTipoDocNew);
            }
        }
    }

    /**
     *
     */
    @Override
    public ResponseDTO comprobarTipoActor(Integer idConvocatoria, Integer idTipoIdentificacion,
            Integer idTipoActor) throws ServiceException {

        // Validacion de entrada
        if (idConvocatoria == null || idTipoIdentificacion == null || idTipoActor == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ResponseDTO responseDTO = new ResponseDTO();

        // Se recupera la lista de Actores Solicitantes de la convocatoria
        List<ConvocatoriaActDTO> listConvocatoriaActDTO = this.convocatoriaActService
                .buscarTipoActorSolicitante(idConvocatoria);

        // Si no hay actores configurados, error
        if (Boolean.TRUE.equals(listConvocatoriaActDTO.isEmpty())) {
            responseDTO.setCodigo("101");
            responseDTO
                    .setDescripcionError("La convocatoria no tiene actores solicitantes definidos");
            responseDTO.setError(Boolean.TRUE);
            return responseDTO;
        }

        // Comprobamos si hay algún actor configurado que sea de nuestro tipo de
        // indentificación
        Integer ocurrenciasTipoIdentificacion = 0;
        Integer ocurrenciasTipoActor = 0;
        for (ConvocatoriaActDTO convocatoriaActDTO : listConvocatoriaActDTO) {

            Integer idTipoIdentificacionActorConv = convocatoriaActDTO.getIdentificacionDTO()
                    .getIdValor();

            Integer idTipoPersonaFisJur = convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor();

            // El actor tiene que ser del mismo tipo (PF o PJ)
            if (idTipoIdentificacion.equals(idTipoIdentificacionActorConv)) {
                ocurrenciasTipoIdentificacion++;

                // El tipo de persona coincide
                if (idTipoActor.equals(idTipoPersonaFisJur)) {
                    ocurrenciasTipoActor++;
                }
            }
        }

        // No es del mismo tipo de identificación (PF o PJ)
        if (ocurrenciasTipoIdentificacion == 0) {
            responseDTO.setCodigo("102");
            responseDTO.setDescripcionError(
                    "El tipo de identificación (persona física o jurídica) de actor solicitante no cumple los requisitos");
            responseDTO.setError(Boolean.TRUE);
            log.error("El actor " + idTipoActor + " (tipo identificación: " + idTipoIdentificacion
                    + "), no cumple los requisitos de la convocatoria " + idConvocatoria);
            return responseDTO;
        }

        // No coincide el tipo de actor
        if (ocurrenciasTipoActor == 0) {
            responseDTO.setCodigo("103");
            responseDTO.setDescripcionError("El actor solicitante no cumple los requisitos");
            responseDTO.setError(Boolean.TRUE);
            log.error("El actor " + idTipoActor + " (tipo identificación: " + idTipoIdentificacion
                    + "), no cumple los requisitos de la convocatoria " + idConvocatoria);
            return responseDTO;
        }

        // // Coincida con alguno de los tipos de actor
        // Boolean bCoincideActor = Boolean.FALSE;
        //
        // // La convocatoria tiene parametrizado al menos un tipo de actor
        // Boolean bHayUnoFisica = Boolean.FALSE;
        // Boolean bHayUnoJuridica = Boolean.FALSE;
        //
        // // Coincida con alguno de los tipos de persona
        // Boolean bCoincidePesonaFisica = Boolean.FALSE;
        // Boolean bCoincidePesonaJur = Boolean.FALSE;
        //
        // // Se recupera la lista de Actores de la convocatoria
        // List<ConvocatoriaActDTO> listConvocatoriaAct =
        // this.convocatoriaActService
        // .buscarPorIdConvocatoria(idConvocatoriaP);
        //
        // if (listConvocatoriaAct.isEmpty()) {
        // responseDTO.setCodigo("101");
        // responseDTO.setDescripcionError("La convocatoria no tiene actores
        // definidos.");
        // responseDTO.setError(Boolean.TRUE);
        // return responseDTO;
        // }
        //
        // Iterator<ConvocatoriaActDTO> it = listConvocatoriaAct.iterator();
        // while (it.hasNext()) {
        // ConvocatoriaActDTO convocatoriaActDTO = it.next();
        //
        // Integer iTipoActor =
        // convocatoriaActDTO.getIdentificacionDTO().getIdValor();
        //
        // Integer idTipoPersonaFisJur =
        // convocatoriaActDTO.getTipoPersonaFisJurDTO().getIdValor();
        // // TODO: revisar esta parte
        //// Integer idTipoPersonaFisica =
        // convocatoriaActDTO.getTipoPersonaFisicaDTO().getIdValor();
        //// Integer idTipoPersonaJur =
        // convocatoriaActDTO.getTipoPersonaJurDTO().getIdValor();
        //
        // if (iTipoActor.equals(iTipoActorP)) {
        // bCoincideActor = Boolean.TRUE;
        // }
        //
        // if (iTipoActor.equals(Constantes.Identificacion.FISICA)) {
        // bHayUnoFisica = Boolean.TRUE;
        // // if (idTipoPersonaP.equals(idTipoPersonaFisica)) {
        // if (idTipoPersonaP.equals(idTipoPersonaFisJur)) {
        // bCoincidePesonaFisica = Boolean.TRUE;
        // }
        // }
        //
        // if (iTipoActor.equals(Constantes.Identificacion.JURIDICA)) {
        // bHayUnoFisica = Boolean.TRUE;
        // if (idTipoPersonaP.equals(idTipoPersonaFisJur)) {
        // // if (idTipoPersonaP.equals(idTipoPersonaJur)) {
        // bCoincidePesonaJur = Boolean.TRUE;
        // }
        // }
        //
        // }
        //
        // // En caso de no coincidir con ninguno de los tipos de actor se le
        // // denegará el acceso a la cumplimentación
        // // indicando que el actor no cumple los requisitos.
        // if (bCoincideActor.equals(Boolean.FALSE)) {
        // responseDTO.setCodigo("102");
        // responseDTO.setDescripcionError("El actor no cumple los
        // requisitos.");
        // responseDTO.setError(Boolean.TRUE);
        // return responseDTO;
        // }
        //
        // // En caso de que coincida con alguno de los tipos de actor, el
        // sistema
        // // realizará las siguientes acciones:
        // if (bCoincideActor.equals(Boolean.TRUE)) {
        // // Comprobará si en la configuración de actores de la convocatoria
        // // tiene parametrizados al menos un actor de tipo persona física y
        // // un
        // // actor tipo de persona jurídica:
        //
        // if (bHayUnoFisica.equals(Boolean.TRUE) &&
        // bHayUnoJuridica.equals(Boolean.TRUE)) {
        // // En caso afirmativo, se comprobará la configuración de
        // // identificación la convocatoria el parámetro Inicio de la
        // // solicitud solo por
        // // el organismo:
        // ConvocatoriaDTO convocatoriaDTO = this.buscarId(idConvocatoriaP);
        //
        // if
        // (convocatoriaDTO.getItInicioSolOrg().equals(Constantes.AfirmacionNegacion.S))
        // {
        // // Si está marcada esta opción se realizará la siguiente
        // // acción dependiendo del tipo de persona que este
        // // accediendo a la cumplimentación:
        //
        // // Persona jurídica: Al acceder tiene que registrarse en el
        // // sistema que el inicio de la solicitud está activado, para
        // // que de esta forma puedan interactuar el resto de actores
        // // en la cumplimentación de la convocatoria. Pendiente
        // // definir si se hará automáticamente o si el organismos
        // // tendrá que iniciarla manualmente (botón, check, fecha...)
        // // TODO - DUDA FUNCIONAL
        //
        // // Persona física: Se ha de comprobar que el organismo haya
        // // iniciado la solicitud. Si es así, se continuará con el
        // // proceso, y en caso contrario, se informará al actor que
        // // aún no está habilitada la cumplimentación de la
        // // convocatoria.
        // }
        //
        // // Si no está marcado, se continuará con el proceso
        //
        // }
        //
        // // En caso negativo se continuará con el proceso
        // }
        //
        // // Se comprueba que coincida el tipo de actor
        // if (bCoincidePesonaFisica.equals(Boolean.FALSE)
        // && bCoincidePesonaJur.equals(Boolean.FALSE)) {
        // responseDTO.setCodigo("103");
        // responseDTO.setDescripcionError("El tipo actor no cumple los
        // requisitos.");
        // responseDTO.setError(Boolean.TRUE);
        // return responseDTO;
        // }

        return responseDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void actualizarAuditoria(Integer idConvocatoria) throws ServiceException {

        if (idConvocatoria == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        Convocatoria convocatoria = this.find(idConvocatoria);

        convocatoria.setCdUsuModif(UserUtils.getUserName().toUpperCase());
        convocatoria.setFcModifFila(new Date());

        this.update(convocatoria);

    }

    /**
     *
     */
    @SuppressWarnings("unused")
    @Override
    @Transactional(readOnly = false)
    public void procesarConvocatoriasBatch() throws ServiceException {

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.APROBADA));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES));
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstados,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_FC_INI_PRESENTACION,
                Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACTIVA,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));
             
        
        List<Convocatoria> listaConvocatorias = this.find(filtros, null);

//        if (listaConvocatorias != null) {
//            log.info(
//                    "Numero de convocatorias en estado aprobado o iniciado plazo de presentancion: "
//                            + listaConvocatorias);
//        }

        if (listaConvocatorias != null && Boolean.FALSE.equals(listaConvocatorias.isEmpty())) {
            
          
            LocalDateTime fechaHoy = LocalDateTime.now();
            log.info("Fecha de hoy: " + fechaHoy.toString());

            for (Convocatoria convocatoria : listaConvocatorias) {
                
                log.info("Convocatoria: " + convocatoria.getCdAcronimo());

                // Se utilizada localDateTime para considerar fecha y tiempo

                LocalDateTime fechaInicioPres = Utils.convertDateToLocalDateTimeInstant(convocatoria.getFcIniPresentacion());
                
                LocalDateTime fechaFinPres;
                if(convocatoria.getFcFinPresentacion() == null) {
                     fechaFinPres = null;
                     log.info("Fecha de fin presentacion: ");
                }else {
                    fechaFinPres = Utils.convertDateToLocalDateTimeInstant(convocatoria.getFcFinPresentacion());
                    log.info("Fecha de fin presentacion: " + fechaFinPres.toString());
                }
                log.info("Fecha de inicio presentacion: " + fechaInicioPres.toString());
                  
                           
                Integer idEstConvAprov = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.APROBADA);
                Integer idEstConvIniPlazPreseSoli = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES);
                Integer idEstConvFinPlazPreseSoli = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES);
                
                
                if(convocatoria.getIdEstado().getIdValor().equals(idEstConvFinPlazPreseSoli) && fechaFinPres != null && fechaHoy.isBefore(fechaFinPres)){ 
                    
                    this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvIniPlazPreseSoli);
                    
                }else if(convocatoria.getIdEstado().getIdValor().equals(idEstConvIniPlazPreseSoli) && fechaInicioPres != null && fechaHoy.isBefore(fechaInicioPres)){
                    
                    this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvAprov);
                    
                }else {
                    
                    
                    if ((convocatoria.getIdEstado().getIdValor().equals(idEstConvAprov) || 
                            convocatoria.getIdEstado().getIdValor().equals(idEstConvIniPlazPreseSoli))
                            && (fechaHoy.isAfter(fechaInicioPres) || fechaHoy.isEqual(fechaInicioPres))
                            && fechaFinPres == null) {
                            
                        if("S".equals(convocatoria.getItAbiertaPermanente())) {
                            if("S".equals(convocatoria.getItConvocatoriaCerrada())) {
                                this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvFinPlazPreseSoli); 
                            }else {
                                this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvIniPlazPreseSoli);
                            }
                        }
                        else if("S".equals(convocatoria.getItAgotarFondos())){
                            if("S".equals(convocatoria.getItfondosAgotados())) {
                                this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvFinPlazPreseSoli); 
                            }else {
                                this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvIniPlazPreseSoli);
                            }
                        }
                    }

                    if(fechaFinPres != null) {
                        if (convocatoria.getIdEstado().getIdValor().equals(idEstConvAprov)
                                && (fechaHoy.isAfter(fechaInicioPres) || fechaHoy.isEqual(fechaInicioPres))
                                && fechaHoy.isBefore(fechaFinPres)) {
                            log.info("Cambio de estado de la convocatoria a INICIADO_PLAZO_PRESENTACION_SOLICITUDES");
                                this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvIniPlazPreseSoli);                       
                        } else if (convocatoria.getIdEstado().getIdValor().equals(idEstConvIniPlazPreseSoli)
                                &&  fechaHoy.isAfter(fechaFinPres)) {
                            log.info("Cambio de estado de la convocatoria a FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES");
                            this.modificarEstadoConvocatoriaBatch(convocatoria,idEstConvFinPlazPreseSoli);
                        }                    
                    }    
                }
        
            }          
        }
    }

    
    /**
     * Método para actualizar el estado del documento
     *
     * @param expedienteDoc Datos de documento
     * @param idEstado Identificador de estado
     */
    private void actualizarEstadoDocumentoBatch(ExpedienteDoc expedienteDoc, Integer idEstado) {

        if (Objects.nonNull(expedienteDoc.getTramite())
                && Objects.nonNull(expedienteDoc.getTramite().getIdValor())) {
            expedienteDoc.setEstado(new CatalogosValores(idEstado));
            
            // se agregan datos de auditoria por ser ejecutado desde batch
            expedienteDoc.setFcModifFila(new Date());
            expedienteDoc.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);

            expedienteDoc = this.expedienteDocDAO.merge(expedienteDoc);

            // Historico
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                    Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        }

    }

    /**
     * Modifica el estado de la convocatoria
     *
     * @param convocatoria convocatoria
     * @param idEstado estado
     * @throws ServiceException
     */
    private void modificarEstadoConvocatoriaBatch(Convocatoria convocatoria, Integer idEstado)
            throws ServiceException {
        CatalogosValores estado = this.catalogosValoresDAO.find(idEstado);
        convocatoria.setIdEstado(estado);
        convocatoria.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
        convocatoria.setFcModifFila(new Date());
        this.update(convocatoria);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void consultarDocumentoPlazoSubsanacionBatch() throws ServiceException {

        List<Integer> lstEstado = new ArrayList<>();
        lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.APROBADA));
        lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.INICIADO_PLAZO_PRESENTACION_SOLICITUDES));
        lstEstado.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.FINALIZADO_PLAZO_PRESENTACION_SOLICITUDES));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstado,
                Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_NM_DIAS_SUBSANACION,
                Operador.NOT_NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACTIVA,
                Constantes.AfirmacionNegacion.S, Operador.EQUALS));

        List<Convocatoria> listaConvocatorias = this.find(0, -1, filtros, null);

        if (listaConvocatorias != null && !listaConvocatorias.isEmpty()) {
            for (Convocatoria convocatoria : listaConvocatorias) {
                Integer idConvocatoria = convocatoria.getIdConvocatoria();

                this.consultarDocumentoPlazoSubsanacionSolicitudesBatch(idConvocatoria);
            }
        }

    }

    /**
     * Método para validar si los documentos fueron subsanados dentro del plazo
     * configurado por convocatoria (solicitudes)
     *
     * @param convocatoria Datos de convocatoria
     */
    private void consultarDocumentoPlazoSubsanacionSolicitudesBatch(Integer idConvocatoria) {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA), Operador.EQUALS));

        List<Solicitud> listaSolicitudes = this.solicitudDAO.find(0, -1, filtros, null);

        if (listaSolicitudes != null && !listaSolicitudes.isEmpty()) {
            for (Solicitud solicitud : listaSolicitudes) {
                this.consultarDocumentoPlazoSubsanacionExpedientesBatch(solicitud.getIdSolicitud());
            }
        }

    }

    /**
     * Método para validar si los documentos fueron subsanados dentro del plazo
     * configurado por convocatoria (expedientes)
     *
     * @param convocatoria Datos de convocatoria
     * @param solicitud Datos de solicitud
     */
    private void consultarDocumentoPlazoSubsanacionExpedientesBatch(Integer idSolicitud) {

        List<Integer> lstEstadosFiltro = new ArrayList<>();
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REQUERIMIENTO));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, idSolicitud));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadosFiltro,
                Operador.IN));

        List<Expediente> listaExpedientes = this.expedienteDAO.find(0, -1, filtros, null);

        if (listaExpedientes != null && !listaExpedientes.isEmpty()) {
            for (Expediente expediente : listaExpedientes) {
                this.consultarDocumentoPlazoSubsanacionDocumentosBatch(
                        expediente.getIdExpediente());
            }
        }
    }

    /**
     * Método para validar si los documentos fueron subsanados dentro del plazo
     * configurado por convocatoria (documentos)
     *
     * @param diasSubsanacion
     * @param idExpediente
     */
    private void consultarDocumentoPlazoSubsanacionDocumentosBatch(Integer idExpediente) {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE_OBJ, idExpediente));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
               
        List<Integer> lstEstadosFiltro = new ArrayList<>();
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstadosFiltro,
                Operador.IN));
        

        List<ExpedienteDoc> listaDocumentos = this.expedienteDocDAO.find(0, -1, filtros, null);

        if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
            for (ExpedienteDoc expedienteDoc : listaDocumentos) {

                if (expedienteDoc.getIdDocumentoPtfr() != null) {

                    DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                            .find(expedienteDoc.getIdDocumentoPtfr());

                    if (Objects.nonNull(documentoPtfr.getFcFinPlazoSubsanacion())) {

                        LocalDateTime fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(
                                documentoPtfr.getFcFinPlazoSubsanacion());

                        LocalDateTime fcHoy = LocalDateTime.now();

                        if (fcLimiteSubsanacion != null && fcHoy.isAfter(fcLimiteSubsanacion)) {
                            
                            this.actualizarEstadoDocumentoBatch(expedienteDoc,
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO));
                            
                            //cambiar estadops de revisioan, admin y requisitos depediendo del tramite del documento asociado.
                            Expediente exp = this.expedienteDAO.find(idExpediente);
                            
                            if(expedienteDoc.getTramite().getIdValor().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)) {
                                exp.setEstadoRevAdmin(new CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
                                this.expedienteDAO.merge(exp);
                            }else if(expedienteDoc.getTramite().getIdValor().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)) {
                                exp.setEstadoRevRequi(new CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
                                this.expedienteDAO.merge(exp);
                                }
                            
                        }

                    }

                }
            }
        }
    }

    

    public void consultarDocumentoPlazoSubsanacionContratosBatch() throws ServiceException {

        List<Integer> lstEstadosFiltro = new ArrayList<>();
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION_RENUNCIA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstadosFiltro,
                Operador.IN));
        

        List<ExpedienteContrato> listaExpedientesContrato = this.expedienteContratoDAO.find(0, -1, filtros, null);

        if (listaExpedientesContrato != null && !listaExpedientesContrato.isEmpty()) {
            for (ExpedienteContrato expediente : listaExpedientesContrato) {
                this.consultarExpedienteContratoDoc(
                        expediente.getIdExpedienteContrato());
            }
        }
    }
    
    private void consultarExpedienteContratoDoc(Integer idExpedienteContrato ) {


        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_CONTRATO_OBJ, idExpedienteContrato)); //expedienteContrato
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteContratoDoc> listaExpedientesContratoDocs = this.expedienteContratoDocDAO.find(0, -1, filtros, null);

        if (listaExpedientesContratoDocs != null && !listaExpedientesContratoDocs.isEmpty()) {
            for (ExpedienteContratoDoc expediente : listaExpedientesContratoDocs) {
                this.consultarDocumentoPlazoSubsanacionDocumentosByIdExpedientedocBatch(
                        expediente.getExpedienteDoc().getIdExpedienteDoc());
            }
        }
    }
    
    
    
    
    private void consultarDocumentoPlazoSubsanacionDocumentosByIdExpedientedocBatch(Integer idExpedienteDoc) {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC, idExpedienteDoc));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        List<Integer> lstEstadosFiltro = new ArrayList<>();
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
        
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstadosFiltro,
                Operador.IN));

        List<ExpedienteDoc> listaDocumentos = this.expedienteDocDAO.find(0, -1, filtros, null);

        if (listaDocumentos != null && !listaDocumentos.isEmpty()) {
            for (ExpedienteDoc expedienteDoc : listaDocumentos) {

                if (expedienteDoc.getIdDocumentoPtfr() != null) {

                    DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                            .find(expedienteDoc.getIdDocumentoPtfr());

                    if (Objects.nonNull(documentoPtfr.getFcFinPlazoSubsanacion())) {

                        LocalDateTime fcLimiteSubsanacion = Utils.convertDateToLocalDateTimeInstant(
                                documentoPtfr.getFcFinPlazoSubsanacion());

                        LocalDateTime fcHoy = LocalDateTime.now();

                        if (fcLimiteSubsanacion != null && fcHoy.isAfter(fcLimiteSubsanacion)) {
                            
                            this.actualizarEstadoDocumentoBatch(expedienteDoc,
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PLAZO_SUBSANACION_VENCIDO));
                            
                            
                            //cambiar estadops de revisioan, admin y requisitos depediendo del tramite del documento asociado.
                            Expediente exp = this.expedienteDAO.find(expedienteDoc.getExpediente().getIdExpediente());
                            
                            if(expedienteDoc.getTramite().getIdValor().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)) {
                                exp.setEstadoRevAdmin(new CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
                                this.expedienteDAO.merge(exp);
                            }else if(expedienteDoc.getTramite().getIdValor().equals(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)) {
                                exp.setEstadoRevRequi(new CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION));
                                this.expedienteDAO.merge(exp);
                                }
                        }

                    }

                }
            }
        }
    }
    
    public void consultarDocumentoPlazoSubsanacionJCTBatch() throws ServiceException {

        List<Integer> lstEstadosFiltro = new ArrayList<>();
        lstEstadosFiltro.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_SUBSANACION));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR, lstEstadosFiltro,
                Operador.IN));

        List<ExpedienteJusTec> listaExpedientesJCT = this.expedienteJusTecDAO.find(0, -1, filtros, null);

        if (listaExpedientesJCT != null && !listaExpedientesJCT.isEmpty()) {
            for (ExpedienteJusTec expediente : listaExpedientesJCT) {
                this.consultarExpedienteJCTDoc(
                        expediente.getIdExpedienteJusTec());
            }
        }
    }
    
    private void consultarExpedienteJCTDoc(Integer idExpedienteJct ) {


        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_JUSTEC, idExpedienteJct)); 
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteJustecDoc> listaExpedientesjctDocs = this.expedienteJustecDocDAO.find(0, -1, filtros, null);

        if (listaExpedientesjctDocs != null && !listaExpedientesjctDocs.isEmpty()) {
            for (ExpedienteJustecDoc expediente : listaExpedientesjctDocs) {
                this.consultarDocumentoPlazoSubsanacionDocumentosByIdExpedientedocBatch(
                        expediente.getIdExpedienteDoc());
            }
        }
    }
    

    /**
     *
     */
//    @Override
//    public ConvocatoriaPaginacionDTO buscarConvocatoriasTramitacion(final int index,
//            final int pageSize, final List<AtlasFilter> filtros, final List<AtlasSort> orden,
//            String cdUsuarioGestor) throws ServiceException {
//
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR,
//                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.EN_CONSTRUCCION), Operador.NOT_EQUALS));
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
//
//        // Buscamos las convocatorias
//        List<ConvocatoriaDTO> listaConvocatorias = new ArrayList<>(
//                Lists.transform(this.find(filtros, orden), ConvocatoriaToDTO.INSTANCE));
//
//        List<ConvocatoriaDTO> listaConvocatoriaFiltrada = this
//                .compararConvocatoriaGestor(cdUsuarioGestor, listaConvocatorias);
//
//        List<ConvocatoriaDTO> listaConvocatoriaPaginada = this
//                .retornarPaginado(listaConvocatoriaFiltrada, index, pageSize);
//        
//        
//        List<Integer> idsPagina = listaConvocatoriaPaginada.stream()
//                
//                .map(ConvocatoriaDTO::getIdConvocatoria).collect(Collectors.toList());
//                Map<Integer, Long> mAlertaGestor =  this.contarExpedientesPendientesPorConvocatoria(idsPagina);
//                Map<Integer, Long> mDocumentos =   this.contarDocumentosVencidosAdmin(idsPagina);
//                Map<Integer, Long> mRequisitos = this.contarRequisitosVencidos(idsPagina);
//                Map<Integer, Long> mJCT =  this.contarDocumentosJCTVencidos(idsPagina);
//                Map<Integer, Long> mContrato = this.contarDocumentosContratoVencidos(idsPagina);
//                Map<Integer, Long> mModif = this.contarDocumentosModifVencidos(idsPagina);
//                Map<Integer, Long> mJEco = this.contarDocumentosJEcoVencidos(idsPagina);
//                
//                for (ConvocatoriaDTO conv : listaConvocatoriaPaginada) {
//                 Integer id = conv.getIdConvocatoria();
//                 conv.setAlertaTramitesPendiente(mAlertaGestor.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaDocumentos(mDocumentos.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaRequisitos(mRequisitos.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaDocumentosJCT(mJCT.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaDocumentosCont(mContrato.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaDocumentosModif(mModif.getOrDefault(id, 0L).intValue());
//                 conv.setAlertaDocumentosJEco(mJEco.getOrDefault(id, 0L).intValue());
//                }
//        
//        
//        
//        
//
//        for (ConvocatoriaDTO conv : listaConvocatoriaPaginada) {
//            // alerta de documentos pendientes de revision
//            conv.setAlertaTramitesPendiente(this.alertaGestorConvocatoria(conv.getIdConvocatoria()));
//
//            // obtener alerta de documentos fuera de plazo de subsanacion
//            conv.setAlertaDocumentos(this.alertaDocumentosSolicitud(conv.getIdConvocatoria()));
//
//            // obtener alerta de requisitos fuera de plazo de subsanacion
//            conv.setAlertaRequisitos(this.alertaRequisitosSolicitud(conv.getIdConvocatoria()));
//            
//            //VMM 10/02/25 MD-578: obtener alerta de documentos JCT fuera de plazo de subsanacion
//            conv.setAlertaDocumentosJCT(this.alertaDocumentosJCT(conv.getIdConvocatoria()));
//            
//            //VMM 10/02/25 MD-582: obtener alerta de documentos Contrato fuera de plazo de subsanacion
//            conv.setAlertaDocumentosCont(this.alertaDocumentosContrato(conv.getIdConvocatoria()));
//            
//            //VMM 10/02/25 MD-112: obtener alerta de documentos de las Modificaciones PPO con Actas fuera de plazo de subsanacion
//            conv.setAlertaDocumentosModif(this.alertaDocumentosModif(conv.getIdConvocatoria()));
//            
//            //VMM 29/10/25 MD-1586: obtener alerta de documentos JEco fuera de plazo de subsanacion
//            conv.setAlertaDocumentosJEco(this.alertaDocumentosJEco(conv.getIdConvocatoria()));
//        }
//
//        ConvocatoriaPaginacionDTO resultado = new ConvocatoriaPaginacionDTO();
//        resultado.setLstConvocatorias(listaConvocatoriaPaginada);
//        resultado.setTotalRegistros(listaConvocatoriaFiltrada.size());
//
//        return resultado;
//    }

    /**
     *
     * @param cdUsuarioGestor
     * @param listaConvocatorias
     * @return
     * @throws ServiceException
     */
    private List<ConvocatoriaDTO> compararConvocatoriaGestor(String cdUsuarioGestor,
            List<ConvocatoriaDTO> listaConvocatorias) throws ServiceException {

        List<ConvocatoriaDTO> listaConvocatoriasFiltrada = listaConvocatorias;

        /*
         * Si el usuario es gestor de lista de convocatorias a mostrar tiene que
         * ser un macth de las lista de convocatorias del filtro y las
         * convocatorias que puede ver un gestor
         */
        if (StringUtils.isNotBlank(cdUsuarioGestor)) {
            // convocatorias a las cuales tiene permiso de consultar un gestor
            List<ConvocatoriaGestorDTO> listaConvocatoriasGestor = this.convocatoriaGestorService
                    .buscarPorUsuario(cdUsuarioGestor);

            //VMM 09/10/24 MD-657: se añaden las convocatorias que puede consultar el gestor extraordinariamente 
            //y que no existan en el listado inicial de convocatorias
            if (!listaConvocatoriasGestor.isEmpty()) {
                for (ConvocatoriaGestorDTO convG : listaConvocatoriasGestor) {
    
                    int existe = (int) listaConvocatorias.stream()
                            .filter(f -> f.getIdConvocatoria().equals(convG.getIdConvocatoria()))
                            .count();
    
                    //si no existe en la lista la añadimos como excepcional
                    if (existe == 0) {
                        ConvocatoriaDTO convDTO = this.buscarId(convG.getIdConvocatoria());
                        
                        if (Objects.isNull(convDTO.getFcBajaFila())) { //si la convo esta activa                            
                            listaConvocatoriasFiltrada.add(convDTO);
                        }
                    }
                }
            }
        }

        return listaConvocatoriasFiltrada;
    }

    /**
     * Metodo para retornar resultado de busqueda paginada
     *
     * @param listaConvocatoriasGeneral
     * @param index
     * @param pageSize
     */
    private List<ConvocatoriaDTO> retornarPaginado(List<ConvocatoriaDTO> listaConvocatoriaFiltrada,
            int index, int pageSize) {

        List<ConvocatoriaDTO> listaConvocatoriaPaginada = new ArrayList<>();

        if (!listaConvocatoriaFiltrada.isEmpty()) {

            if (pageSize != -1) {

                int lastIndex = 0;

                if (listaConvocatoriaFiltrada.size() <= 10) {
                    index = 0;
                    lastIndex = listaConvocatoriaFiltrada.size();
                } else {
                    lastIndex = index + pageSize;
                    lastIndex = lastIndex > listaConvocatoriaFiltrada.size()
                            ? listaConvocatoriaFiltrada.size()
                            : lastIndex;
                }

                for (int i = index; i < lastIndex; i++) {
                    listaConvocatoriaPaginada.add(listaConvocatoriaFiltrada.get(i));
                }
            } else {
                listaConvocatoriaPaginada.addAll(listaConvocatoriaFiltrada);
            }
        }

        return listaConvocatoriaPaginada;

    }

    /**
     * Método para calcular la cantidad de expedientes pendientes por
     * convocatoria
     *
     * @param idConvocatoria
     */
    private Integer alertaGestorConvocatoria(Integer idConvocatoria) throws ServiceException {
        Integer alertaGestorConvocatoria = 0;

        List<Integer> lstEstados = new ArrayList<>();
        lstEstados.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.PRESENTADA));

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstados, Operador.IN));
        List<Solicitud> lstSolicitud = this.solicitudDAO.find(filtros, null);

        for (Solicitud solicitud : lstSolicitud) {
            
            //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
            Boolean solicitudEnTramitacion = !solicitud.getEstado().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

            alertaGestorConvocatoria += this.expedienteDAO
                    .alertaGestorSolicitud(solicitud.getIdSolicitud(), solicitudEnTramitacion);
        }
        return alertaGestorConvocatoria;
    }

    /**
     * Método para alerta de documentos fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     */
    private Integer alertaDocumentosSolicitud(Integer idConvocatoria) {

        int alertaDocumentosSolicitud = 0;

        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);

        for (Solicitud solicitud : lstSolicitud) {
            
            List<Expediente> lstExpediente = this.consultarExpdtesPorIdSolic(solicitud.getIdSolicitud(),
                    Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_ADMIN);

            for (Expediente exped : lstExpediente) {

                // consultar e iterar la lista de expedienteDoc
                for (ExpedienteDoc expedDoc : exped.getLstExpedienteDoc()) {

                    alertaDocumentosSolicitud = this.comprobarSubsanacionVencidaDoc(
                            alertaDocumentosSolicitud, expedDoc);
                }
            }
        }

        return alertaDocumentosSolicitud;
    }
    
    private int comprobarSubsanacionVencidaDoc(int alertaDocumentos, ExpedienteDoc expedDoc) {
        //comprueba si se algun doc del expediente esta fuera de plazo de subsanacion
        if (expedDoc.getIdDocumentoPtfr() != null) {
            DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                    .find(expedDoc.getIdDocumentoPtfr());

            if (Objects.nonNull(documentoPtfr)
                    && Objects.nonNull(documentoPtfr.getFcFinPlazoSubsanacion())) {
                LocalDate fcSubsanacionLimite = Utils.convertDateToLocalDate(
                        documentoPtfr.getFcFinPlazoSubsanacion());
                LocalDate fcHoy = LocalDate.now();
                if (fcHoy.isAfter(fcSubsanacionLimite)) {
                    alertaDocumentos++;
                }
            }
        }
        return alertaDocumentos;
    }

    private List<Solicitud> consultarSolicitudesPorIdConv(Integer idConvocatoria) {
        //consultar la lista de solicitudes por convocatoria
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return this.solicitudDAO.find(filtros, null);
    }

    /**
     * Método para alerta de requisitos fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     */
    private Integer alertaRequisitosSolicitud(Integer idConvocatoria) {

        int alertaRequisitosSolicitud = 0;
        
        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);
        
        for (Solicitud solicitud : lstSolicitud) {
            
            List<Expediente> lstExpediente = this.consultarExpdtesPorIdSolic(solicitud.getIdSolicitud(),
                    Constantes.Filtro.FILTRO_EXPEDIENTE_ESTADO_REV_REQUI);

            for (Expediente exped : lstExpediente) {

                // consultar e iterar la lista de expedienteReq
                for (ExpedienteReq expedReq : exped.getLstExpedienteReq()) {

                    alertaRequisitosSolicitud = this.comprobarSubsanacionVencidaReq(
                            alertaRequisitosSolicitud, expedReq);
                }
            }
        }

        return alertaRequisitosSolicitud;
    }
    
    private int comprobarSubsanacionVencidaReq(int alertaRequisitos, ExpedienteReq expedReq) {
        //comprueba si se algun doc del expediente esta fuera de plazo de subsanacion
         if (Objects.nonNull(expedReq.getDocumentoPtfr()) && Objects
                .nonNull(expedReq.getDocumentoPtfr().getFcFinPlazoSubsanacion())) {

            LocalDate fcSubsanacionLimite = Utils.convertDateToLocalDate(
                    expedReq.getDocumentoPtfr().getFcFinPlazoSubsanacion());
            LocalDate fcHoy = LocalDate.now();
            if (fcHoy.isAfter(fcSubsanacionLimite)) {
                alertaRequisitos++;
            }
        }
        return alertaRequisitos;
    }

    private List<Expediente> consultarExpdtesPorIdSolic(Integer idSolicitud, String filtroEstado) {
        // consultar la lista de expedientes por solicitud
        List<AtlasFilter> filtros1 = new ArrayList<>();
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                idSolicitud, Operador.EQUALS));
        //aqui revisar si este estado es correcto para la revision admin 
        filtros1.add(new AtlasFilter(filtroEstado, 
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS), Operador.EQUALS));

        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        Solicitud solicitud = this.solicitudDAO.find(idSolicitud);
        Boolean solicitudEnTramitacion = !solicitud.getEstado().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            List<Integer> lstEstadoExpInterno = new ArrayList<>();
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
            
            filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR, lstEstadoExpInterno,
                    Operador.NOT_IN));
        }

        return this.expedienteDAO.find(filtros1, null);
    }
    
    
    /**
     * Método para alerta de documentos JCT fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private Integer alertaDocumentosJCT(Integer idConvocatoria) throws ServiceException {

        int alertaDocumentosJCT = 0;

        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);
        
        for (Solicitud solicitud : lstSolicitud) {
            List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(solicitud.getIdSolicitud());

            for (Expediente exped : lstExpediente) {

                List<ExpedienteDoc> lstExpedienteJustecDoc = this.expedienteDAO.findExpJusTecDocByIdExp(exped.getIdExpediente()); 
                
                // consultar e iterar la lista de docs asociada a expedienteJustecDoc
                for (ExpedienteDoc expedDoc : lstExpedienteJustecDoc) {

                    alertaDocumentosJCT = this.comprobarSubsanacionVencidaDoc(alertaDocumentosJCT, expedDoc);
                }
            }
        }
        
        return alertaDocumentosJCT;
    }
    
    
    /**
     * Método para alerta de documentos JEco fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private Integer alertaDocumentosJEco(Integer idConvocatoria) throws ServiceException {

        int alertaDocumentosJEco = 0;

        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);
        
        for (Solicitud solicitud : lstSolicitud) {
            List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(solicitud.getIdSolicitud());

            for (Expediente exped : lstExpediente) {

                List<ExpedienteDoc> lstExpedienteJusEcoDoc = this.expedienteDAO.findExpJusEcoDocByIdExp(exped.getIdExpediente()); 
                
                // consultar e iterar la lista de docs asociada a expedienteJustecDoc
                for (ExpedienteDoc expedDoc : lstExpedienteJusEcoDoc) {

                    alertaDocumentosJEco = this.comprobarSubsanacionVencidaDoc(alertaDocumentosJEco, expedDoc);
                }
            }
        }
        
        return alertaDocumentosJEco;
    }

    private List<Expediente> consultarExpdtesActivosPorIdSolic(Integer idSolicitud) {
        
        // estados expediente que no lanzan alerta
        List<Integer> lstEstadoExpInterno = new ArrayList<>();
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO));
        lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO));
        
        //VMM 27/01/26 MD-2395: tampoco incluimos los estados de Solicitudes en TRAMITACION con expedientes iniciados, propuestos, rechazados, no aceptados
        Solicitud solicitud = this.solicitudDAO.find(idSolicitud);
        Boolean solicitudEnTramitacion = !solicitud.getEstado().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));

        if (Boolean.TRUE.equals(solicitudEnTramitacion)) {  //solicitudes tramitadas
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA));
            lstEstadoExpInterno.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO));
        }
        
        List<AtlasFilter> filtros1 = new ArrayList<>();
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                idSolicitud, Operador.EQUALS));
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_ESTADO_IDVALOR,
                lstEstadoExpInterno, Operador.NOT_IN));
        filtros1.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        // consultar la lista de expedientes
        return this.expedienteDAO.find(filtros1, null);
    }
    
    
    /**
     * Método para alerta de documentos Contrato fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private Integer alertaDocumentosContrato(Integer idConvocatoria) throws ServiceException {

        int alertaDocumentosContrato = 0;

        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);

        for (Solicitud solicitud : lstSolicitud) {
            List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(solicitud.getIdSolicitud());

            for (Expediente exped : lstExpediente) {

                List<ExpedienteDoc> lstExpedienteContDoc = this.expedienteDAO.findExpContratosDocByIdExp(exped.getIdExpediente()); 
                
                // consultar e iterar la lista de docs asociada a expedienteContratoDoc
                for (ExpedienteDoc expedDoc : lstExpedienteContDoc) {

                    alertaDocumentosContrato = this.comprobarSubsanacionVencidaDoc(alertaDocumentosContrato, expedDoc);
                }
            }
        }
        
        return alertaDocumentosContrato;
    }
    
    
    /**
     * Método para alerta de documentos Modificaciones PPO con Actas fuera de plazo de subsanacion
     *
     * @param idConvocatoria
     * @throws ServiceException 
     */
    private Integer alertaDocumentosModif(Integer idConvocatoria) throws ServiceException {

        int alertaDocumentosModif = 0;

        List<Solicitud> lstSolicitud = this.consultarSolicitudesPorIdConv(idConvocatoria);

        for (Solicitud solicitud : lstSolicitud) {
            List<Expediente> lstExpediente = this.consultarExpdtesActivosPorIdSolic(solicitud.getIdSolicitud());

            for (Expediente exped : lstExpediente) {

                List<ExpedienteDoc> lstExpedienteModifDoc = this.expedienteDAO.findExpModifDocByIdExp(exped.getIdExpediente()); 
                
                // consultar e iterar la lista de docs asociada a expedienteModifPresPlazoOtraDoc
                for (ExpedienteDoc expedDoc : lstExpedienteModifDoc) {

                    alertaDocumentosModif = this.comprobarSubsanacionVencidaDoc(alertaDocumentosModif, expedDoc);
                }
            }
        }
        
        return alertaDocumentosModif;
    }
    

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO actualizarIndicadorRequisito(Integer idConvocatoria,
            Boolean conRequisitos) throws ServiceException {

        Convocatoria convocatoria = this.find(idConvocatoria);

        if (convocatoria != null) {
            if (conRequisitos.equals(Boolean.TRUE)) {
                convocatoria.setItRequisitos(Constantes.AfirmacionNegacion.S);
            } else {
                convocatoria.setItRequisitos(Constantes.AfirmacionNegacion.N);
            }

            convocatoria = this.getDao().merge(convocatoria);
        }

        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }

    /**
     * Método para validar si los documentos fueron subsanados dentro del plazo
     * configurado por convocatoria (expedientes)
     *
     * @param convocatoria Datos de convocatoria
     * @param solicitud Datos de solicitud
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    @Override
    public Boolean validarDescargaListados(Integer idConvocatoria) throws ServiceException {

        Boolean bloquearDescarga = Boolean.FALSE;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Solicitud> lstSolicitud = this.solicitudDAO.find(filtros, null);

        z: for (Solicitud soli : lstSolicitud) {

            filtros = new ArrayList<>();
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD, soli.getIdSolicitud()));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<Expediente> listaExpedientes = this.expedienteDAO.find(filtros, null);

            for (Expediente items : listaExpedientes) {
                Integer estado = items.getEstado().getIdValor();
                if (!utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_PROVISIONAL).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.APROBADO_PROVISIONAL).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.ACEPTADA).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.SELECCIONADA).equals(estado)
                        && !utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_SELECCIONADA).equals(estado)) {

                    bloquearDescarga = Boolean.TRUE;
                    break z;
                }

            }

        }

        return bloquearDescarga;

    }

    /**
     *
     */
    @Override
    public Boolean esPuedeSubirPropuestaResProvisional(Integer idConvocatoria)
            throws ServiceException {
        if (Objects.isNull(idConvocatoria)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        return this.getDao().esPuedeSubirPropuestaResProvisional(idConvocatoria);
    }

    /**
     * Me llevo este metodo a ExpedienteService para poder hacer toda la logica de generar y presentar expdte desde alli ya que sino se crean referencias circulares
     * al llamar de ConvocatoriaService a ExpedienteService
     */
   /* @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO resolver(Integer idConvocatoria) throws ServiceException {
        Boolean existeError = Boolean.FALSE;
        
        if (Objects.isNull(idConvocatoria)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        Convocatoria convocatoria = this.find(idConvocatoria);

        if (Objects.nonNull(convocatoria)) {
            
            //VMM 11/06/25 MD-1634: antes de poner la convocatoria como Resuelta
            //comprobamos que el resto de impresos de (justec, juesco, contratos, etc) tengan bloques/componentes y esten bien configurados
            List<String> listMensajesError = new ArrayList<>(); //esto solo por si quieren mensajes personalizados de que formulario o bloque casca         
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
           existeError = this.comprobarConfigImpresos(convocatoria, existeError, lstImpreso, listMensajesError);
           
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
                //else, no filtro porque tengo que revisar todos los expdtes?? o solo los de tipo participante?? (pdte. confirmar)
                
                
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
                        
                        //VMM 12/06/25 MD-1635: generacion de formularios especificos en cada expdte de la convocatoria cuando se hace la concesión
                        //lo comento hasta que se pueda subir con el resto de desarrollos de otros modulos
                        for(ExpedienteDTO expDTO : lstExpedientedto){
                            
                            //obtener cual es su SolicitudDTO y su actor asociado (se supone que esta activa porque no mira la fcBaja)
                            SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(expDTO.getIdExpediente());
                        
                            ConvocatoriaActDTO convocatoriaActDTO = this.convocatoriaActService.buscarId(expDTO.getConvocatoriaActDTO().getIdActor());
                                   
                            if (solicitudDTO != null && convocatoriaActDTO != null) {
                                this.generarImpresosExpdte(solicitudDTO, convocatoriaActDTO, expDTO);
                            }
                        }
                        
                        Integer idEstConvResu = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.RESUELTA);
                        
                        CatalogosValores catalogosValores = this.catalogosValoresService
                                .find(idEstConvResu);
                        
                        convocatoria.setIdEstado(new CatalogosValores(idEstConvResu));
                        convocatoria = this.getDao().merge(convocatoria);
                        convocatoria.getIdEstado().setDsValor(catalogosValores.getDsValor());
                    }
                }
            }else {
                throw new ServiceException(Constantes.Error.ERROR_RESOLVER_CONVOCATORIA_IMPRESOS_INCOMPLETOS, null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }
        }
    
        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }*/

    
    /**
     * Método que genera el documento de alegaciones según sea configurado en la convocatoria
     * @throws ServiceException 
     */
    @Override
    @Transactional(readOnly = false)
    public List<ConvocatoriaTipoDocDTO> alegaciones(ConvocatoriaDTO convocatoriaDTO) throws ServiceException {
        try {
            
            Integer idTipDocAleg = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES);
            // MD-2190 quitamos la declaración de la variable del idValor de la alegacion excluido provisional
//            Integer idTipDocAlegExclProv = utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES_EXCLUIDO_PROVISIONAL);
            
            List<ConvocatoriaTipoDocDTO> lstConvocatoriaTipoDocDTO = new LinkedList<>();
            if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItAlegacion())) {
                ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO = this.convocatoriaTipoDocService.agregar(convocatoriaDTO.getIdConvocatoria(), 
                        idTipDocAleg, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));
                lstConvocatoriaTipoDocDTO.add(convocatoriaTipoDocDTO);
             // MD-2190 No añadimos el tipo de documento asociado al trámite de alegación exclusión provisional
//                convocatoriaTipoDocDTO = this.convocatoriaTipoDocService.agregar(convocatoriaDTO.getIdConvocatoria(), 
//                        idTipDocAlegExclProv, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL));
//                lstConvocatoriaTipoDocDTO.add(convocatoriaTipoDocDTO);
            } else {
                ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO = null;
                List<ConvocatoriaTipoDocDTO> docAlegaciones = this.convocatoriaTipoDocService
                        .buscarPorConvocatoriaTipoDocumento(convocatoriaDTO.getIdConvocatoria(), idTipDocAleg);
                if (!docAlegaciones.isEmpty()) {
                    convocatoriaTipoDocDTO = docAlegaciones.get(0);
                    this.convocatoriaTipoDocService.deleteLogico(convocatoriaTipoDocDTO.getIdConvocatoriaTipoDoc());
                }
                lstConvocatoriaTipoDocDTO.add(convocatoriaTipoDocDTO);
                // MD-2190 no añadimos la tipo de documento alegacion exclusión provisional
//                List<ConvocatoriaTipoDocDTO> docAlegacionesExclusionProvisional = this.convocatoriaTipoDocService
//                        .buscarPorConvocatoriaTipoDocumento(convocatoriaDTO.getIdConvocatoria(), idTipDocAlegExclProv);
//                if (!docAlegacionesExclusionProvisional.isEmpty()) {
//                    convocatoriaTipoDocDTO = docAlegacionesExclusionProvisional.get(0);
//                    this.convocatoriaTipoDocService.deleteLogico(convocatoriaTipoDocDTO.getIdConvocatoriaTipoDoc());
//                }
//                lstConvocatoriaTipoDocDTO.add(convocatoriaTipoDocDTO);
            }
            return lstConvocatoriaTipoDocDTO;
        } catch (ServiceException e) {
            throw new ServiceException(e);
        }
    }
    
    /**
     * Guardar los numero de dias de ampliacion del contrato
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO guardarNmDiasAmpliContrato(final ConvocatoriaDTO convocatoriaDTO) throws ServiceException {

        Convocatoria convocatoria = this.find(convocatoriaDTO.getIdConvocatoria());
        convocatoria.setFcIniContrato(convocatoriaDTO.getFcIniContrato());
        convocatoria.setFcFinContrato(convocatoriaDTO.getFcFinContrato());
        convocatoria.setNmDiasAmpliContrato(convocatoriaDTO.getNmDiasAmpliContrato());
       
        convocatoria = this.getDao().merge(convocatoria);
    
        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);

    }
    
    
    /**
     * Guardar checks de financiacion en la convocaotoria
     *
     * @param ConvocatoriaDTO the Convocatorias DTO
     * @return the Convocatorias DTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO guardarChecksFinanConvocatoria(final ConvocatoriaDTO convocatoriaDTO) throws ServiceException {

        Convocatoria convocatoria = this.find(convocatoriaDTO.getIdConvocatoria());
        convocatoria.setItConceptoPresup(convocatoriaDTO.getItConceptoPresup());
        convocatoria.setItImporteMaxAnual(convocatoriaDTO.getItImporteMaxAnual());
        convocatoria.setItImporteMaxExpdte(convocatoriaDTO.getItImporteMaxExpdte());
       
        convocatoria = this.getDao().merge(convocatoria);
    
        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);

    }

    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDTO guardarChecksAyudaSolicitada(ConvocatoriaDTO convocatoriaDTO)
            throws ServiceException {
        Convocatoria convocatoria = this.find(convocatoriaDTO.getIdConvocatoria());
        convocatoria.setItAyudaCalculada(convocatoriaDTO.getItAyudaCalculada());
        convocatoria = this.getDao().merge(convocatoria);
        return ConvocatoriaToDTO.INSTANCE.apply(convocatoria);
    }
    
    
    @Override
    public ConvocatoriaPaginacionDTO buscarConvocatoriasTramitacion(final int index,
            final int pageSize, final List<AtlasFilter> filtros, final List<AtlasSort> orden,
            String cdUsuarioGestor) throws ServiceException {

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_ESTADO_IDVALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoConvocatoria.EN_CONSTRUCCION), Operador.NOT_EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        // Buscamos las convocatorias
        List<ConvocatoriaDTO> listaConvocatorias = new ArrayList<>(
                Lists.transform(this.find(filtros, orden), ConvocatoriaToDTO.INSTANCE));

        List<ConvocatoriaDTO> listaConvocatoriaFiltrada = this
                .compararConvocatoriaGestor(cdUsuarioGestor, listaConvocatorias);

        List<ConvocatoriaDTO> listaConvocatoriaPaginada = this
                .retornarPaginado(listaConvocatoriaFiltrada, index, pageSize);
        
        // ¡INVOCAMOS TU MÉTODO OPTIMIZADO POR LOTE!
        this.buscarConvocatoriasTramitacion(listaConvocatoriaPaginada);

        // Construimos el DTO de respuesta final
        ConvocatoriaPaginacionDTO resultado = new ConvocatoriaPaginacionDTO();
        resultado.setLstConvocatorias(listaConvocatoriaPaginada);
        resultado.setTotalRegistros(listaConvocatoriaFiltrada.size());

        return resultado;
    }
    
    
    @Override
    public List<ConvocatoriaDTO> buscarConvocatoriasTramitacion(List<ConvocatoriaDTO> listaConvocatoriaPaginada) {
        if (listaConvocatoriaPaginada == null || listaConvocatoriaPaginada.isEmpty()) {
            return listaConvocatoriaPaginada;
        }

        List<Integer> idsPagina = listaConvocatoriaPaginada.stream()
                .map(ConvocatoriaDTO::getIdConvocatoria)
                .collect(Collectors.toList());

        // 1. Alertas de Gestión (Llamada al DAO externo)
        Map<Integer, Long> mAlertaGestor = expedienteDAO.contarExpedientesPendientesPorConvocatoria(idsPagina);
        
        
        Map<Integer, Long> mDocumentos = obtenerConteoAgregado(
                "select sol.convocatoria.idConvocatoria, count(distinct doc.idExpedienteDoc) " +
                "from org.madrid.saii.lib.domain.DocumentoPtfr dp, " + // Colocamos primero la huérfana
                "org.madrid.saii.lib.domain.Expediente exp " +        // Luego la raíz del grafo
                "inner join exp.solicitud sol " +                       // Y ahora sus uniones naturales
                "inner join exp.lstExpedienteDoc doc " +
                "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +     // Condición de unión en el WHERE
                "and sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
                "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
                "and exp.estadoRevAdmin.idValor = :estadoFiltroAlerta " + 
                "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
                "and dp.fcFinPlazoSubsanacion is not null " +
                "and dp.fcFinPlazoSubsanacion < current_date() " +
                "group by sol.convocatoria.idConvocatoria",
                idsPagina,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
            );
            
     // 3. Alertas de Requisitos Vencidos
     // Nota: Esta query no usaba comas porque 'er.documentoPtfr' es una relación mapeada en la entidad. 
     // Para asegurar que tu base de datos no se líe con la colección, mantenemos su estructura limpia y directa.
     Map<Integer, Long> mRequisitos = obtenerConteoAgregado(
             "select sol.convocatoria.idConvocatoria, count(distinct er.idExpedienteReq) " +
             "from Expediente exp " +
             "inner join exp.solicitud sol " +
             "inner join exp.lstExpedienteReq er " +
             "where sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and sol.fcBajaFila is null and exp.fcBajaFila is null " +
             "and exp.estadoRevRequi.idValor = :estadoFiltroAlerta " + 
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and er.documentoPtfr.fcFinPlazoSubsanacion is not null " +
             "and er.documentoPtfr.fcFinPlazoSubsanacion < current_date() " +
             "group by sol.convocatoria.idConvocatoria", 
             idsPagina, 
             utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         );
         
     // 4. Alertas de Documentos JCT Vencidos
     Map<Integer, Long> mJCT = obtenerConteoAgregado(
             "select sol.convocatoria.idConvocatoria, count(distinct doc.idExpedienteDoc) " +
             "from ExpedienteJustecDoc jtd, " + // Huérfana 1 al principio
             "ExpedienteJusTec jct, " +         // Huérfana 2 al principio
             "DocumentoPtfr dp, " +             // Huérfana 3 al principio
             "Expediente exp " +                // Raíz del grafo
             "inner join exp.solicitud sol " +
             "inner join exp.lstExpedienteDoc doc " +
             "where jtd.idExpedienteDoc = doc.idExpedienteDoc " +
             "and jct.idExpedienteJusTec = jtd.idExpedienteJusTec " +
             "and dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
             "and sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
             "and jtd.fcBajaFila is null and jct.fcBajaFila is null " +
             "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and jct.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
             "and dp.fcFinPlazoSubsanacion is not null " +
             "and dp.fcFinPlazoSubsanacion < current_date() " +
             "group by sol.convocatoria.idConvocatoria", 
             idsPagina, 
             utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         );
         
     // 5. Alertas de Documentos de Contrato Vencidos
     // Nota: En tu bloque me pasaste aquí la query de JEco repetida en la variable mContrato. 
     // Te la dejo corregida usando su modelo real de contratos (ExpedienteContratoDoc / ExpedienteContrato) ordenado al inicio.
     Map<Integer, Long> mContrato = obtenerConteoAgregado(
             "select sol.convocatoria.idConvocatoria, count(distinct doc.idExpedienteDoc) " +
             "from DocumentoPtfr dp, " + // Huérfana al principio
             "Expediente exp " +         // Raíz del grafo
             "inner join exp.solicitud sol " +
             "inner join exp.lstExpedienteDoc doc " +
             "inner join ExpedienteContratoDoc ecd on ecd.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
             "inner join ecd.expedienteContrato con " +
             "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
             "and sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
             "and ecd.fcBajaFila is null and con.fcBajaFila is null " +
             "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and con.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
             "and dp.fcFinPlazoSubsanacion is not null " +
             "and dp.fcFinPlazoSubsanacion < current_date() " +
             "group by sol.convocatoria.idConvocatoria", 
             idsPagina, 
             utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         );
         
     // 6. Alertas de Documentos de Modificaciones Vencidos
     Map<Integer, Long> mModif = obtenerConteoAgregado(
             "select sol.convocatoria.idConvocatoria, count(distinct doc.idExpedienteDoc) " +
             "from ExpedienteModifPresPlazoOtra mod, " + // Huérfana 1 al principio
             "DocumentoPtfr dp, " +                      // Huérfana 2 al principio
             "Expediente exp " +                         // Raíz del grafo
             "inner join exp.solicitud sol " +
             "inner join exp.lstExpedienteDoc doc " +
             "inner join ExpedienteModfPresupPlazosOtrosDoc emd on emd.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
             "where mod.idExpedienteModifPresPlazoOtra = emd.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra " +
             "and dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
             "and sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
             "and emd.fcBajaFila is null and mod.fcBajaFila is null " +
             "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and mod.estadoRevAdmin.idValor = :estadoFiltroAlerta " + 
             "and dp.fcFinPlazoSubsanacion is not null " +
             "and dp.fcFinPlazoSubsanacion < current_date() " +
             "group by sol.convocatoria.idConvocatoria", 
             idsPagina, 
             utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         );
         
     // 7. Alertas de Documentos JEco Vencidos
     Map<Integer, Long> mJEco = obtenerConteoAgregado(
             "select sol.convocatoria.idConvocatoria, count(distinct doc.idExpedienteDoc) " +
             "from DocumentoPtfr dp, " + // Huérfana al principio
             "Expediente exp " +         // Raíz del grafo
             "inner join exp.solicitud sol " +
             "inner join exp.lstExpedienteDoc doc " +
             "inner join ExpedienteJusEcoDoc jed on jed.expedienteDoc.idExpedienteDoc = doc.idExpedienteDoc " +
             "inner join jed.expedienteJusEco je " +
             "where dp.idDocumentoPtfr = doc.idDocumentoPtfr " +
             "and sol.convocatoria.idConvocatoria in (:idsConvocatoria) " +
             "and sol.fcBajaFila is null and exp.fcBajaFila is null and doc.fcBajaFila is null " +
             "and jed.fcBajaFila is null and je.fcBajaFila is null " +
             "and exp.estado.idValor not in (:estadosBaseInactivos) " + 
             "and (sol.estado.idValor = :estadoSolIniciada or exp.estado.idValor not in (:estadosExtra)) " +
             "and je.idEstadoRevAdmin.idValor = :estadoFiltroAlerta " + 
             "and dp.fcFinPlazoSubsanacion is not null " +
             "and dp.fcFinPlazoSubsanacion < current_date() " +
             "group by sol.convocatoria.idConvocatoria", 
             idsPagina, 
             utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)
         );

        // Mapeo final en memoria O(N)
        for (ConvocatoriaDTO conv : listaConvocatoriaPaginada) {
            Integer id = conv.getIdConvocatoria();
            conv.setAlertaTramitesPendiente(mAlertaGestor.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentos(mDocumentos.getOrDefault(id, 0L).intValue());
            conv.setAlertaRequisitos(mRequisitos.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosJCT(mJCT.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosCont(mContrato.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosModif(mModif.getOrDefault(id, 0L).intValue());
            conv.setAlertaDocumentosJEco(mJEco.getOrDefault(id, 0L).intValue());
        }

        return listaConvocatoriaPaginada;
    }
    
    private Map<Integer, Long> obtenerConteoAgregado(String hql, List<Integer> idsPagina, Object valorEstadoFiltro) {
        // 1. Desendosamos la sesión de Hibernate desde el entityManager existente
        org.hibernate.Session session = sessionFactory.getCurrentSession();        
        org.hibernate.query.Query<Object[]> query = session.createQuery(hql);
        
        // 2. Seteamos la lista de IDs de convocatoria usando IntegerType
        query.setParameterList("idsConvocatoria", idsPagina, org.hibernate.type.IntegerType.INSTANCE);
        
        // 3. Seteamos los estados extra evitando problemas de reflexión con IntegerType
        query.setParameterList("estadosExtra", Arrays.asList(
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.NO_ACEPTADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.INICIADA),
            utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PROPUESTA)
        ), org.hibernate.type.IntegerType.INSTANCE);

        // 4. Parámetro unitario habitual
        query.setParameter("estadoSolIniciada", utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoSolicitud.INICIADA));
        
        // 5. Seteamos estados base inactivos si la query los requiere
        if (hql.contains("estadosBaseInactivos")) {
            query.setParameterList("estadosBaseInactivos", Arrays.asList(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DESISTIDO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.EXCLUIDO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.DENEGADO_DEFINITIVO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.RECHAZADO_DEFINITIVO)
            ), org.hibernate.type.IntegerType.INSTANCE);
        }

        // 6. Seteamos el filtro de alerta unitario
        query.setParameter("estadoFiltroAlerta", valorEstadoFiltro);

        // 7. Ejecutamos usando la API limpia de Hibernate 5 (.list())
        List<Object[]> resultado = query.list();
        
        return resultado.stream().collect(Collectors.toMap(
                fila -> (Integer) fila[0],
                fila -> (Long) fila[1]
        ));
    }
}
