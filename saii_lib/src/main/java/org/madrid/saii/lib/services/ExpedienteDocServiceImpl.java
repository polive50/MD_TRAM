package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDAO;
import org.madrid.saii.lib.dao.NotificacionCiudadanoDocDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Dato;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.domain.NotificacionCiudadano;
import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RegistroEregDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteModfPresupPlazosOtrosDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToEntity;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ExpedienteDoc
 *
 * @author everis
 */
@Service("expedienteDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ExpedienteDocServiceImpl extends
        BaseServiceImpl<ExpedienteDoc, Integer, ExpedienteDocDAO> implements ExpedienteDocService {

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /** Servicio de Expediente. */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;

    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** The pdf service. */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /** Servicio de ExpedienteJusTec. */
    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;

    /**
     * expedienteContratoService
     */
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;

    /**
     * DAO ExpedienteDocHist
     */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /**
     * DAO ExpedienteDoc
     */
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    @Autowired
    @Qualifier("notificacionCiudadanoDAO")
    private NotificacionCiudadanoDAO notificacionCiudadanoDAO;

    @Autowired
    @Qualifier("notificacionCiudadanoDocDAO")
    private NotificacionCiudadanoDocDAO notificacionCiudadanoDocDAO;

    @Autowired
    @Qualifier("notificacionCiudadanoService")
    private NotificacionCiudadanoService notificacionCiudadanoService;

    /**
     * DAO ConvocatoriaTipoDoc
     */
    @Autowired
    @Qualifier("convocatoriaTipoDocDAO")
    private ConvocatoriaTipoDocDAO convocatoriaTipoDocDAO;

    /**
     * DAO ExpedienteReq
     */
    @Autowired
    @Qualifier("expedienteReqDAO")
    private ExpedienteReqDAO expedienteReqDAO;

    /**
     * DAO Expediente
     */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    /**
     * DAO para generar historicos
     */
    @Autowired
    @Qualifier("expedienteHistDAO")
    @Setter
    private ExpedienteHistDAO expedienteHistDAO;

    /** DAO de Solicitud */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    /**
     * DAO de DocumentoPtfr
     */
    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    @Autowired
    @Qualifier("expedienteModifOtraService")
    private ExpedienteModifOtraService expedienteModifOtraService;

    @Autowired
    @Qualifier("expedienteModfPresupPlazosOtrosDocService")
    private ExpedienteModfPresupPlazosOtrosDocService expedienteModfPresupPlazosOtrosDocService;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("tramiteSolicitanteExpedienteService")
    private TramiteSolicitanteExpedienteService tramiteSolicitanteExpedienteService;
    
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteDocDAO
     */
    @Autowired
    public ExpedienteDocServiceImpl(@Qualifier("expedienteDocDAO")
    ExpedienteDocDAO expedienteDocDAO) {
        super();
        this.setDao(expedienteDocDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteDocServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteDocDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteDocToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     * Método para subir documento en FICA
     *
     * @param dto Datos del documento
     * @return Datos del documento
     * @throws ServiceException Excepción ocurrido en el servicio
     */
    private ExpedienteDocDTO cargarDocumentoFica(ExpedienteDocDTO dto) throws ServiceException {
        // Validacion
        if (dto.getContenidoFichero() == null || dto.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        dto.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        FicaFichero fichero = this.ficaService.guardar(dto.getContenidoFichero(),
                dto.getFicaMetadatos());
        dto.setFicaFichero(fichero);
        dto.setCdFica(fichero.getIdInterno());
        dto.setNombreFicheroFica(fichero.getNombreFichero());
        return dto;
    }
    
    
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarDocumentoPresupuesto(ExpedienteDocDTO expedienteDocDTO, Integer idDato) 
            throws ServiceException {

        // 1. Subida a FICA
        if (expedienteDocDTO.getContenidoFichero() == null || expedienteDocDTO.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        expedienteDocDTO.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        
        FicaFichero fichero = this.ficaService.guardar(expedienteDocDTO.getContenidoFichero(), expedienteDocDTO.getFicaMetadatos());
        expedienteDocDTO.setCdFica(fichero.getIdInterno());

        // 2. Mapeo a la entidad de Base de Datos
        ExpedienteDoc expeDocu = ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO);
        
        // Asignamos el Dato de la partida presupuestaria
        if (idDato != null) {
            Dato entidadDato = new Dato();
            entidadDato.setIdDato(idDato);
            expeDocu.setDato(entidadDato); 
        }

        CatalogosValores estadoDocumento = new CatalogosValores();

        Integer idEstadoAportado = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PRESENTADO); 
        estadoDocumento.setIdValor(idEstadoAportado); 
        
        expeDocu.setEstado(estadoDocumento);
        // ===================================================================

        // Seteamos código FICA y bandera de Adicional
        expeDocu.setCdFica(fichero.getIdInterno());
        expeDocu.setItAdicional(Constantes.AfirmacionNegacion.S);

        // 3. Guardado físico en la tabla (SAI_EXPEDIENTE_DOC) e Histórico
        expeDocu = expedienteDocDAO.merge(expeDocu);
        
        if (expeDocu.getIdExpedienteDoc() != null) {
            this.expedienteDocHistDAO.crearHistorico(expeDocu, null);
        }

        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }
    

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarConHistorico(ExpedienteDocDTO expedienteDocDTO)
            throws ServiceException {
        return this.guardarConHistorico(expedienteDocDTO, null);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarConHistorico(ExpedienteDocDTO expedienteDocDTO, String cdUsuAlta)
            throws ServiceException {
        ExpedienteDoc expedienteDoc = this
                .insertOrUpdate(ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO));

        if (expedienteDoc.getIdExpedienteDoc() != null) {
            // Historico
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdUsuAlta);
        }

        return ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc);
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarExterno(ExpedienteDocDTO expedienteDocDTO)
            throws ServiceException {

        ExpedienteDoc expeDocu = new ExpedienteDoc();
        if (expedienteDocDTO.getExpedienteDTO() != null
                && expedienteDocDTO.getExpedienteDTO().getIdExpediente() != null
                && expedienteDocDTO.getTipoDocumentoDTO() != null) {
            Integer idTipoDocumento = expedienteDocDTO.getTipoDocumentoDTO().getIdValor();
//            Integer idTramite = expedienteDocDTO.getTramiteDTO().getIdValor();

            if (idTipoDocumento == null || idTipoDocumento == -1) {
                expedienteDocDTO.setTipoDocumentoDTO(null);
                expedienteDocDTO.setBloqueComponenteDTO(null);
                idTipoDocumento = -1;
            }

            Integer idExpediente = expedienteDocDTO.getExpedienteDTO().getIdExpediente();
            
            ExpedienteDTO expediente =  this.expedienteService.buscarIdExpediente(idExpediente);
            
//            MD-3069
//            expeDocu = this.buscarPorExpedienteTipoDocumento(idExpediente, idTipoDocumento,
//                    idTramite);
            
            

//            if (expeDocu == null) {
//                expeDocu = ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO);
//            }

            expeDocu = ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO);
            
            expeDocu.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            expeDocu.setTlObservacionesGestor(expedienteDocDTO.getTlObservacionesGestor());
            expeDocu.setCodigoEreg(expedienteDocDTO.getCodigoEreg());
            expeDocu.setFcRegistro(expedienteDocDTO.getFcRegistro());
            expeDocu.setTlCausaDescripcion(null);
            expeDocu.setIdDocumentoPtfr(null);
            expeDocu.setConvocatoriaCau(null);
            expeDocu.setItAdicional(Constantes.AfirmacionNegacion.N);
            CatalogosValores estado = new CatalogosValores();
            
            if(expediente.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS))
               || expediente.getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION))) {
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
            }else {
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
            }

            expeDocu.setEstado(estado);
            ExpedienteDocDTO expeDocuFica = this.cargarDocumentoFica(expedienteDocDTO);
            expeDocu.setCdFica(expeDocuFica.getCdFica());
            expeDocu = this.insertOrUpdate(expeDocu);

            if (expeDocu.getIdExpedienteDoc() != null) {

                // Historico
                this.expedienteDocHistDAO.crearHistorico(expeDocu, null);
            }
        }
        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarExternoExistente(ExpedienteDocDTO expedienteDocDTO, Integer idEstadoRevAdminExp)
            throws ServiceException {

        ExpedienteDoc expeDocu = new ExpedienteDoc();
        if (expedienteDocDTO.getExpedienteDTO() != null
                && expedienteDocDTO.getExpedienteDTO().getIdExpediente() != null
                && expedienteDocDTO.getTipoDocumentoDTO() != null) {
            Integer idTipoDocumento = expedienteDocDTO.getTipoDocumentoDTO().getIdValor();
            Integer idTramite = expedienteDocDTO.getTramiteDTO().getIdValor();

            if (idTipoDocumento == null || idTipoDocumento == -1) {
                expedienteDocDTO.setTipoDocumentoDTO(null);
                expedienteDocDTO.setBloqueComponenteDTO(null);
                idTipoDocumento = -1;
            }

            Integer idExpediente = expedienteDocDTO.getExpedienteDTO().getIdExpediente();
            expeDocu = this.buscarPorExpedienteTipoDocumento(idExpediente, idTipoDocumento,
                    idTramite);

            // VMM 20/10/26 MD-3163: si la busqueda por tipo+tramite no encuentra el doc (p.ej. tipo "Otros"
            // sin tipo informado) y el DTO viene de la fila del listado (constructor de proyeccion, que no
            // trae CD_USU_ALTA), la entidad construida desde el DTO hacia un UPDATE con CD_USU_ALTA = NULL
            // (ORA-01407). Si el doc ya existe (idExpedienteDoc informado) se parte de la fila de BD.
            if (expeDocu == null && expedienteDocDTO.getIdExpedienteDoc() != null) {
                expeDocu = this.expedienteDocDAO.find(expedienteDocDTO.getIdExpedienteDoc());
            }

            if (expeDocu == null) {
                expeDocu = ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO);
            }

            expeDocu.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            //VMM 11/02/26 MD-2471: guardamos las observaciones del gestor al subir documento
            expeDocu.setTlObservacionesGestor(expedienteDocDTO.getTlObservacionesGestor());
            expeDocu.setCodigoEreg(expedienteDocDTO.getCodigoEreg());
            expeDocu.setFcRegistro(expedienteDocDTO.getFcRegistro());
            expeDocu.setTlCausaDescripcion(null);
            expeDocu.setIdDocumentoPtfr(null);
            expeDocu.setConvocatoriaCau(null);
            expeDocu.setItAdicional(Constantes.AfirmacionNegacion.S);
                        
            //VMM 21/01/26 MD-2278: calculamos el nuevo estado en el que quedaria el documento
            Integer idEstadoNuevo = calcularEstadoDocFinal(expedienteDocDTO.getEstadoDTO().getNmConstante(), idEstadoRevAdminExp);
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(idEstadoNuevo));
            expeDocu.setEstado(estado);
            
            ExpedienteDocDTO expeDocuFica = this.cargarDocumentoFica(expedienteDocDTO);
            expeDocu.setCdFica(expeDocuFica.getCdFica());
            expeDocu = expedienteDocDAO.merge(expeDocu);

            if (expeDocu.getIdExpedienteDoc() != null) {

                // Historico
                this.expedienteDocHistDAO.crearHistorico(expeDocu, null);
            }
        }
        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }
    
    
   

    private Integer calcularEstadoDocFinal(Integer idEstadoDocOld, Integer idEstadoRevAdminExp) {
       Integer estadoDocNew = Constantes.EstadoDocumento.PENDIENTE_REVISION;
       
       switch (idEstadoDocOld) {
        case 119:  //Sin Subir
            if (idEstadoRevAdminExp.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_REQUISITOS)) ||
                idEstadoRevAdminExp.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)) ||
                idEstadoRevAdminExp.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.REVISION_OK))) {
                estadoDocNew = Constantes.EstadoDocumento.REVISION_OK;
            }
            break;
        case 115: //Pendiente revisión subsanación
            estadoDocNew = Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION;
            break;
        case 118: //Plazo subsanación vencido
            estadoDocNew = Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION;
            break;
        case 116: //Revisión OK
            estadoDocNew = Constantes.EstadoDocumento.REVISION_OK;
            break;
        case 117: //Revisión KO
            estadoDocNew = Constantes.EstadoDocumento.REVISION_OK;
            break;
        default:
            break;
        }
       
        return estadoDocNew;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarExternoSinDoc(ExpedienteDocDTO expedienteDocDTO)
            throws ServiceException {

        ExpedienteDoc expeDocu = new ExpedienteDoc();
        if (expedienteDocDTO.getExpedienteDTO() != null
                && expedienteDocDTO.getExpedienteDTO().getIdExpediente() != null
                && expedienteDocDTO.getTipoDocumentoDTO() != null) {
            Integer idTipoDocumento = expedienteDocDTO.getTipoDocumentoDTO().getIdValor();
//            Integer idTramite = expedienteDocDTO.getTramiteDTO().getIdValor();

            if (idTipoDocumento == -1) {
                expedienteDocDTO.setTipoDocumentoDTO(null);
                expedienteDocDTO.setBloqueComponenteDTO(null);
            }

//            Integer idExpediente = expedienteDocDTO.getExpedienteDTO().getIdExpediente();
//            expeDocu = this.buscarPorExpedienteTipoDocumento(idExpediente, idTipoDocumento,
//                    idTramite);

//            if (expeDocu == null) {
                expeDocu = ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO);
//            }

            expeDocu.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            expeDocu.setCodigoEreg(expedienteDocDTO.getCodigoEreg());
            expeDocu.setFcRegistro(expedienteDocDTO.getFcRegistro());
            expeDocu.setTlCausaDescripcion(null);
            expeDocu.setIdDocumentoPtfr(null);
            expeDocu.setConvocatoriaCau(null);
            expeDocu.setItAdicional(Constantes.AfirmacionNegacion.N);
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR));
            expeDocu.setEstado(estado);
            expeDocu = this.insertOrUpdate(expeDocu);

            if (expeDocu.getIdExpedienteDoc() != null) {

                // Historico
                this.expedienteDocHistDAO.crearHistorico(expeDocu, null);
            }
        }
        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }
    

    /**
     *
     * @param idExpediente
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    @Override
    public ExpedienteDoc buscarPorExpedienteTipoDocumento(final Integer idExpediente,
            final Integer idTipoDocumento, final Integer idTramite) throws ServiceException {

        ExpedienteDoc expedienteDoc = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.find(filtros, null);

        for (ExpedienteDoc expeDoc : lstExpedienteDoc) {
            if (Objects.nonNull(expeDoc.getTipoDocumento())
                    && Objects.nonNull(expeDoc.getTipoDocumento().getIdValor())
                    && idTipoDocumento.equals(expeDoc.getTipoDocumento().getIdValor())
                    && idTramite.equals(expeDoc.getTramite().getIdValor())) {
                expedienteDoc = expeDoc;
                break;
            }
        }
        return expedienteDoc;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardar(ExpedienteDocDTO expedienteDocDTO) throws ServiceException {
        return ExpedienteDocToDTO.INSTANCE
                .apply(this.insertOrUpdate(ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO)));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteDocDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO subir(DocumentoDTO documentoDTO) throws ServiceException {

        // Validacion
        if (Objects.isNull(documentoDTO) || Objects.isNull(documentoDTO.getContenidoFichero())
                || Objects.isNull(documentoDTO.getFicaMetadatos())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        // Subimos el fichero a FICA
        documentoDTO.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        FicaFichero fichero = this.ficaService.guardar(documentoDTO.getContenidoFichero(),
                documentoDTO.getFicaMetadatos());
        documentoDTO.setFicaFichero(fichero);
        documentoDTO.setCdFica(fichero.getIdInterno());
        documentoDTO.setNombreFichero(fichero.getNombreFichero());

        Integer idExpedienteDoc = documentoDTO.getIdExpedienteDoc();
        String cdNumIden = documentoDTO.getCdNumIden();
        Date date = new Date();

        ExpedienteDoc expedienteDoc = new ExpedienteDoc();

        // estado cuando se sube un documento desde formulario documento
        // adicional
        CatalogosValores estadoExpeDoc = new CatalogosValores();
        estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        // estado cuando se sube un documento desde formulario justec
        if (documentoDTO.getIdTramite() != null && utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                .equals(documentoDTO.getIdTramite())) {
            estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR));
        }

        // estado cuando se sube un documento desde formulario subsanacion
        // administrativa y requisitos
        if (Boolean.TRUE.equals(documentoDTO.getEsSubsanacionAdmin())
                || Boolean.TRUE.equals(documentoDTO.getEsSubsanacionReq())) {
            estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION));
        }

        if (Boolean.TRUE.equals(documentoDTO.getEsPresentacionContrato())) {
            estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR));
        }

        if (Boolean.TRUE.equals(documentoDTO.getEsSubsanacionContrato())) {
            estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO));
        }

        // estado cuando se sube un documento desde formulario alegación
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES)
                .equals(utilConstantes.getGlobalCatalogosValores().get(documentoDTO.getIdTramite()))) {
            estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR));
        }

        // No es un documento adicional
        if (Boolean.FALSE.equals(documentoDTO.getEsDocAdicional())) {
            if(idExpedienteDoc != null) {
                expedienteDoc = this.find(idExpedienteDoc);
            }else if(documentoDTO.getIdTramite() != null) {
                Integer idExpediente = documentoDTO.getIdExpediente();
                Expediente expediente= new Expediente();
                expediente.setIdExpediente(idExpediente);
                expedienteDoc.setExpediente(expediente);
                
                Integer idTipoDoc = documentoDTO.getIdTipoDoc();
                
                if (idTipoDoc != null) {
                    CatalogosValores tipoDocumento = new CatalogosValores();
                    tipoDocumento.setIdValor(idTipoDoc);
                    expedienteDoc.setTipoDocumento(tipoDocumento);
                }
            }
           
            

            // Se quita el anexo ereg padre en las subsanaciones para poder
            // generar un nuevo listado
            if (Boolean.TRUE.equals(documentoDTO.getEsSubsanacionAdmin())
                    || Boolean.TRUE.equals(documentoDTO.getEsSubsanacionReq())) {
                expedienteDoc.setCdAnexoEregPadre(null);
            }
            expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);
            
            
            if(documentoDTO.getIdTramite() != null && documentoDTO.getIdTramite().equals(Constantes.Tramite.PRESENTACION)) {
                expedienteDoc.setTramite(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)));
            }else if(documentoDTO.getIdTramite() != null && documentoDTO.getIdTramite().equals(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)) {
                expedienteDoc.setTramite(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)));
                Integer idTipoDoc = documentoDTO.getIdTipoDoc();
                if (idTipoDoc == null) {
                    CatalogosValores tipoDocumento = new CatalogosValores();
                    tipoDocumento.setIdValor((utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.DOC_ZIP_JUSTIFICACION_ECONOMICO)));
                    tipoDocumento.setNmConstante(Constantes.TipoDocumento.DOC_ZIP_JUSTIFICACION_ECONOMICO);
                    expedienteDoc.setTipoDocumento(tipoDocumento);
                }
            }else {
                expedienteDoc.setTramite(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(documentoDTO.getIdTramite())));
            }
            

            expedienteDoc.setEstado(estadoExpeDoc);
            expedienteDoc.setCdFica(documentoDTO.getCdFica());
            expedienteDoc.setTlObservaciones(documentoDTO.getTlObservaciones());
            if (expedienteDoc.getIdExpedienteDoc() == null) {
                expedienteDoc.setCdUsuAlta(cdNumIden);
                expedienteDoc.setFcAltaFila(date);
            }
            expedienteDoc.setCdUsuModif(cdNumIden);
            expedienteDoc.setFcModifFila(date);
            expedienteDoc.setItAutoriza(documentoDTO.getItAutoriza());
            this.insertOrUpdate(expedienteDoc);

        } else {

            // Documentos adicionales
            Integer idTipoDoc = documentoDTO.getIdTipoDoc();

            expedienteDoc = new ExpedienteDoc();
            expedienteDoc.setCdFica(documentoDTO.getCdFica());
            expedienteDoc.setEstado(estadoExpeDoc);

            expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.S);

            // El idTipoDoc es null cuando el tipo es "Otros"
            if (idTipoDoc != null) {
                CatalogosValores tipoDocumento = new CatalogosValores();
                tipoDocumento.setIdValor(idTipoDoc);
                expedienteDoc.setTipoDocumento(tipoDocumento);

                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES).equals(idTipoDoc)) {
                    expedienteDoc.setItAdicional(Constantes.AfirmacionNegacion.N);
                }

            } else {

                // cuando es null representa un documento "otros"
                expedienteDoc.setTipoDocumento(null);

            }

            // Si es un documento adicional necesita ser revisado
            if (Constantes.AfirmacionNegacion.S.equals(expedienteDoc.getItAdicional())
                    && !utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES)
                            .equals(utilConstantes.getGlobalCatalogosValores().get(documentoDTO.getIdTramite()))) {
                estadoExpeDoc.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
            }
            
            if(documentoDTO.getIdTramite() != null && documentoDTO.getIdTramite().equals(Constantes.Tramite.PRESENTACION)) {
                expedienteDoc.setTramite(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION)));
            }else {
                expedienteDoc.setTramite(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(documentoDTO.getIdTramite())));
            }

            

            if (Objects.nonNull(idExpedienteDoc)) {
                expedienteDoc.setIdExpedienteDoc(idExpedienteDoc);
            }

            Expediente exp = new Expediente();
            exp.setIdExpediente(documentoDTO.getIdExpediente());
            expedienteDoc.setExpediente(exp);
            expedienteDoc.setTlObservaciones(documentoDTO.getTlObservaciones());
            expedienteDoc.setItAutoriza(documentoDTO.getItAutoriza());
            expedienteDoc.setCdUsuAlta(cdNumIden);
            expedienteDoc.setFcAltaFila(date);
            this.insertOrUpdate(expedienteDoc);

            // Trámite de notificación del ciudadano: el documento adicional
            // se vincula de inmediato a la notificación "en curso" (dada de
            // alta pero todavía no presentada) en SAII_NOTIFI_CIUD_DOC, para
            // que al presentar solo se anexen los documentos de esta
            // notificación y no los de otras notificaciones anteriores o
            // posteriores del mismo expediente
            if (documentoDTO.getIdTramite() != null
                    && documentoDTO.getIdTramite().equals(Constantes.Tramite.NOTIFICACION_CIUDADANO)) {
                this.vincularNotificacionCiudadanoEnCurso(expedienteDoc,
                        documentoDTO.getIdExpediente(), documentoDTO.getIdAsunto(), cdNumIden, date);
            }
        }

        // this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdNumIden);
        
       ExpedienteDocDTO docSubido = ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc);
       if(docSubido != null && Objects.isNull(docSubido.getNombreFicheroFica() )) {
           // Recuperamos el nombre de FICA
           docSubido.setNombreFicheroFica(
                   this.expedienteService.recuperarNombreFicheroFica(docSubido.getCdFica()));
       }
    

        return docSubido;
    }

    /**
     * Vincula un documento adicional del trámite de notificación del
     * ciudadano a la notificación "en curso" del expediente: la que
     * coincide en expediente y asunto, y todavía no ha sido presentada (no
     * tiene CD_EREG). Si no existe ninguna todavía, se crea una nueva. Esto
     * permite que, al pulsar "Presentar", solo se anexen los documentos de
     * esa notificación concreta (SAII_NOTIFI_CIUD_DOC), y no documentos de
     * otras notificaciones anteriores o posteriores del mismo expediente.
     *
     * @param expedienteDoc documento recién insertado
     * @param idExpediente expediente al que pertenece la notificación
     * @param idAsunto asunto de la notificación (obligatorio en BBDD)
     * @param cdNumIden usuario de auditoría
     * @param date fecha de auditoría
     */
    private void vincularNotificacionCiudadanoEnCurso(ExpedienteDoc expedienteDoc,
            Integer idExpediente, Integer idAsunto, String cdNumIden, Date date)
            throws ServiceException {

        if (idAsunto == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        NotificacionCiudadano notificacionCiudadano = this.notificacionCiudadanoService
                .buscarEnCursoEntidad(idExpediente, idAsunto);
        boolean esNueva = Objects.isNull(notificacionCiudadano);

        if (esNueva) {
            notificacionCiudadano = new NotificacionCiudadano();
            Expediente expAux = new Expediente();
            expAux.setIdExpediente(idExpediente);
            notificacionCiudadano.setExpediente(expAux);

            CatalogosValores asunto = new CatalogosValores();
            asunto.setIdValor(idAsunto);
            notificacionCiudadano.setAsunto(asunto);

            notificacionCiudadano.setCdUsuAlta(cdNumIden);
            notificacionCiudadano.setFcAltaFila(date);
            notificacionCiudadano = this.notificacionCiudadanoDAO.insert(notificacionCiudadano);
        }

        NotificacionCiudadanoDoc notificacionCiudadanoDoc = new NotificacionCiudadanoDoc();
        notificacionCiudadanoDoc.setNotificacionCiudadano(notificacionCiudadano);
        notificacionCiudadanoDoc.setExpedienteDoc(expedienteDoc);
        notificacionCiudadanoDoc.setCdUsuAlta(cdNumIden);
        notificacionCiudadanoDoc.setFcAltaFila(date);
        this.notificacionCiudadanoDocDAO.insert(notificacionCiudadanoDoc);
    }

    /**
     *
     */

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarDocumentos(List<ExpedienteDocDTO> listaExpedienteDocDTO)
            throws ServiceException {

        CatalogosValoresDTO estado = new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));

        // Cambiamos el estado y creamos el histórico (el cdNumIden se asigna
        // desde el token)
        for (ExpedienteDocDTO dto : listaExpedienteDocDTO) {
            dto.setEstadoDTO(estado);
            dto.setFcModifFila(new Date());
            this.guardar(dto);
            /**
             * MD-1445 Se está duplicando historico
             * this.expedienteDocHistDAO.crearHistorico(ExpedienteDocToEntity.INSTANCE.apply(dto),
             * dto.getCdUsuModif());
             */

        }

        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        ExpedienteDTO expedienteDTO = this.expedienteService
                .buscarId(listaExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente());

        // Generamos el listado de anexos en ereg

        InteresadoDTO representanteLegal = new InteresadoDTO();
        InteresadoDTO interesadoDTO = new InteresadoDTO();
        interesadoDTO.setCdNumIden(listaExpedienteDocDTO.get(0).getCdUsuModif());
        representanteLegal=interesadoDTO;
        expedienteDTO.setRepresentanteLegal(representanteLegal);
        this.pdfService.generarFirmarListadoAnexos(expedienteDTO, null, null, null);

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe
        String cdAnexoEreg = this.find(listaExpedienteDocDTO.get(0).getIdExpedienteDoc())
                .getCdAnexoEregPadre();

        // Obtenemos ahora el idExpedienteDoc que tiene ese anexo
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdAnexoEreg,
                Operador.EQUALS));

        Integer idExpedienteDoc = this.find(filtros, null).get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
        registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEreg()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);

        if (Boolean.TRUE.equals(listaExpedienteDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return registroEregDTO;
    }

    /**
     *
     * @param idExpediente
     * @param idTipoDocumento
     * @return
     * @throws ServiceException
     */
    @Override
    public List<ExpedienteDocDTO> buscarTodosPorExpedienteTipoDocumento(final Integer idExpediente,
            final Integer idTipoDocumento) throws ServiceException {

        List<ExpedienteDocDTO> lstExpedienteDoc = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        lstExpedienteDoc = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteDocToDTO.INSTANCE));

        return lstExpedienteDoc;
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO guardarContratoExterno(ExpedienteDocDTO expedienteDocDTO)
            throws ServiceException {

        ExpedienteDoc expeDocu = new ExpedienteDoc();
        if (expedienteDocDTO.getExpedienteDTO() != null
                && expedienteDocDTO.getExpedienteDTO().getIdExpediente() != null) {
            Expediente expAux = new Expediente();
            expAux.setIdExpediente(expedienteDocDTO.getExpedienteDTO().getIdExpediente());

            expeDocu.setExpediente(expAux);
            expeDocu.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            expeDocu.setTlCausaDescripcion(null);
            expeDocu.setIdDocumentoPtfr(null);
            expeDocu.setConvocatoriaCau(null);
            expeDocu.setItAdicional(Constantes.AfirmacionNegacion.S);
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PRESENTADO));
            expeDocu.setEstado(estado);
            CatalogosValores tipoDocumento = new CatalogosValores();
            tipoDocumento.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.CONTRATO));
            expeDocu.setTipoDocumento(tipoDocumento);
            ExpedienteDocDTO expeDocuFica = this.cargarDocumentoFica(expedienteDocDTO);
            expeDocu.setCdFica(expeDocuFica.getCdFica());
            expeDocu = this.insert(expeDocu);

            if (expeDocu.getIdExpedienteDoc() != null) {

                // Historico
                this.expedienteDocHistDAO.crearHistorico(expeDocu, null);
            }
        }
        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocDTO actualizarContratoExterno(ExpedienteDocDTO expedienteDocDTO,
            String cdUsualta) throws ServiceException {

        ExpedienteDoc expeDocu = new ExpedienteDoc();
        if (expedienteDocDTO.getExpedienteDTO() != null
                && expedienteDocDTO.getExpedienteDTO().getIdExpediente() != null) {
            expeDocu.setCdUsuAlta(expedienteDocDTO.getCdUsuAlta());
            expeDocu.setIdExpedienteDoc(expedienteDocDTO.getIdExpedienteDoc());

            Expediente expAux = new Expediente();
            expAux.setIdExpediente(expedienteDocDTO.getExpedienteDTO().getIdExpediente());
            expeDocu.setExpediente(expAux);
            expeDocu.setTlObservaciones(expedienteDocDTO.getTlObservaciones());
            expeDocu.setTlCausaDescripcion(null);
            expeDocu.setIdDocumentoPtfr(null);
            expeDocu.setConvocatoriaCau(null);
            expeDocu.setItAdicional(Constantes.AfirmacionNegacion.S);
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK));
            expeDocu.setEstado(estado);
            CatalogosValores tipoDocumento = new CatalogosValores();
            tipoDocumento.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.CONTRATO));
            expeDocu.setTipoDocumento(tipoDocumento);
            CatalogosValores tramite = new CatalogosValores();
            tramite.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL));
            expeDocu.setTramite(tramite);
            ExpedienteDocDTO expeDocuFica = this.cargarDocumentoFica(expedienteDocDTO);
            expeDocu.setCdFica(expeDocuFica.getCdFica());
            if (expedienteDocDTO.getFcAltaFila() != null) {
                expeDocu.setFcAltaFila(expedienteDocDTO.getFcAltaFila());
            } else {
                expeDocu.setFcAltaFila(new Date());
            }
            expeDocu.setFcModifFila(new Date());
            expeDocu = this.insertOrUpdate(expeDocu);

            if (expeDocu.getIdExpedienteDoc() != null) {

                // Historico
                this.expedienteDocHistDAO.crearHistorico(expeDocu, cdUsualta);
            }
        }
        return ExpedienteDocToDTO.INSTANCE.apply(expeDocu);
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarDocumentosJusTec(Integer idExpedienteJustec, String cdNumIden, String dniLogin)
            throws ServiceException {

        List<ExpedienteDocDTO> listaExpedienteDocDTO = this.expedienteService
                .obtenerDocJusTec(idExpedienteJustec).stream()
                .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                .collect(Collectors.toList());

        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        if (!listaExpedienteDocDTO.isEmpty()) {
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarId(listaExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente());

            if (expedienteDTO.getInteresadoDTO() != null
                    && expedienteDTO.getInteresadoDTO().getCdNumIden() != null && !expedienteDTO
                            .getInteresadoDTO().getCdNumIden().equalsIgnoreCase(cdNumIden)) {
                InteresadoDTO interesadoDTO = new InteresadoDTO();
                interesadoDTO.setCdNumIden(cdNumIden);
                expedienteDTO.setRepresentanteLegal(interesadoDTO);
            }

            // Generamos el listado de anexos en ereg
            this.pdfService.generaFirmaReciboPresentacionJustec(expedienteDTO, idExpedienteJustec,
                    null, listaExpedienteDocDTO);
            String cifEntidadLogin= null;
            if(!cdNumIden.equalsIgnoreCase(dniLogin)) {
                cifEntidadLogin=cdNumIden;
            }
            this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores()
                    .get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA),
                    expedienteDTO, dniLogin, cifEntidadLogin);

            // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
            // compartir el cdAnexoEregPadre. La entidad existe
            String cdAnexoEreg = this.find(listaExpedienteDocDTO.get(0).getIdExpedienteDoc())
                    .getCdAnexoEregPadre();

            // Obtenemos ahora el idExpedienteDoc que tiene ese anexo

            Integer idExpedienteDoc = null;
            List<ExpedienteDoc> doc = this.getDao().findByCdAnexoEreg(cdAnexoEreg);
            if (doc != null && !doc.isEmpty()) {
                idExpedienteDoc = doc.get(0).getIdExpedienteDoc();
            }

            // VMM MD-913 28/10/24: recuperamos el Ereg del ExpedienteJustec en
            // vez de en el propio expediente
            ExpedienteJusTecDTO expdteJusTecDTO = this.expedienteJusTecService
                    .buscarPorIdExpedienteJustec(idExpedienteJustec);
            // Generamos el objeto de respuesta
            registroEregDTO.setFechaAlta(
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
            registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(expdteJusTecDTO.getCdEregPresent()));
            registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
            registroEregDTO.setEsDescargaZipJustec(Boolean.TRUE);

        }

        if (Boolean.TRUE.equals(listaExpedienteDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return registroEregDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarDocumentosContrato(ExpedienteDTO expedienteDTO)
            throws ServiceException {

        /*
         * LLAMADA A SAII EXPEDIENTE PARA RECUPERAR EL ACTOR DE ESES EXPEDIENTE
         */

        // ExpedienteDTO expedienteDTO =
        // this.expedienteService.buscarId(idExpediente);
        Integer idActor = expedienteDTO.getConvocatoriaActDTO().getIdActor();

        /*
         * CON EL ACTOR RECUPERADO LANZAMOS LA QUERY EN HQL PARA RECUPERAR LOS
         * HIJOS
         */

        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>();
        List<ExpedienteDoc> lstExpedienteDoc = this.expedienteDocDAO
                .listExpedienteContratosDocPaticipantesPorIdActor(idActor,
                        expedienteDTO.getIdExpediente());

        for (ExpedienteDoc doc : lstExpedienteDoc) {
            lstExpedienteDocDTO.add(ExpedienteDocToDTO.INSTANCE.apply(doc));

        }
        List<ExpedienteContrato> listContratosParticipantes = this.expedienteDocDAO
                .listExpedienteContratosPaticipantesPorIdActor(idActor,
                        expedienteDTO.getIdExpediente());

        /*
         * YA TENEMOS UN LISTA CON LOS HIJOS DEL PADRE QUE ES LA QUE TENDREMOS
         * QUE PINTAR EN EL PDF
         */

        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el listado de anexos en ereg
        
        String dniLogin =expedienteDTO.getDniUserLogin();
        String cifEntidadLogin= expedienteDTO.getCifEntidadLogin();

        String cdAnexoEreg = this.pdfService.generaFirmaReciboPresentacionContrato(expedienteDTO,
                null, lstExpedienteDocDTO, listContratosParticipantes);
        
        // Guarar relación trámite, interesado y expediente
        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.GESTION_CONTRATO),
                expedienteDTO, dniLogin, cifEntidadLogin);

        // Cambiamos el estado y creamos el histórico (el cdNumIden se asigna
        // desde el token)
        /*
         * for (ExpedienteDocDTO dto : lstExpedienteDocDTO) { ExpedienteDoc
         * expedienteDoc = buscarIdExpDoc(dto.getIdExpedienteDoc());
         * CatalogosValores estado = new CatalogosValores();
         * estado.setIdValor(Constantes.EstadoDocumento.PENDIENTE_REVISION);
         * expedienteDoc.setEstado(estado); expedienteDoc.setFcModifFila(new
         * Date()); this.update(expedienteDoc);
         * this.expedienteDocHistDAO.crearHistorico(ExpedienteDocToEntity.
         * INSTANCE.apply(dto), dto.getCdUsuModif()); }
         */

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe

        if (!lstExpedienteDocDTO.isEmpty()) {
            cdAnexoEreg = this.find(lstExpedienteDocDTO.get(0).getIdExpedienteDoc())
                    .getCdAnexoEregPadre();

        }
        // Obtenemos ahora el idExpedienteDoc que tiene ese anexo
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdAnexoEreg,
                Operador.EQUALS));

        Integer idExpedienteDoc = this.find(filtros, null).get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
        // VMM MD-913 28/10/24: recuperamos el Ereg del ExpedienteContrato en
        // vez de en el propio expediente
        registroEregDTO.setCdEreg(
                Utils.formatearNmEreg(expedienteDTO.getExpedienteContrato().getCdEregPresent()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipContrato(Boolean.TRUE);

        return registroEregDTO;
    }

    public ExpedienteDoc buscarIdExpDoc(Integer id) throws ServiceException {
        return this.find(id);
    }

    public RegistroEregDTO presentarDocumentosJustecSubsanado(
            ExpedienteJusTecDTO expedienteJustecDTO, String cdNumIden) throws ServiceException {

        List<ExpedienteDocDTO> listaExpedienteDocDTO = this.expedienteService
                .obtenerDocPendienteSubsanarJusTec(expedienteJustecDTO.getIdExpedienteJusTec())
                .stream().filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                .collect(Collectors.toList());

        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        if (!listaExpedienteDocDTO.isEmpty()) {
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarId(listaExpedienteDocDTO.get(0).getExpedienteDTO().getIdExpediente());

            // Generamos el listado de anexos en ereg
            this.expedienteService.actualizarExpedienteJustec(expedienteJustecDTO);

            this.pdfService.generarFirmarListadoJusTecAnexos(expedienteDTO, expedienteJustecDTO,
                    null, listaExpedienteDocDTO, Boolean.TRUE);

            Expediente expedienteFirmado = this.expedienteDAO.find(expedienteDTO.getIdExpediente());
            Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

            // VMM 01/10/24 MD-714: comentamos esto para que no actualice el
            // estado del beneficiario como posible solucion
//            expedienteFirmado.setEstado(new CatalogosValores(
//                    Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR));
//
//            // Asignamos el estado interno de revision administrativa
//            expedienteFirmado.setEstadoRevAdmin(
//                    new CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION));
//
//            // Asignamos el estado interno de revision evaluación
//            expedienteFirmado.setEstadoRevEval(new CatalogosValores(
//                    Constantes.EstadoExpediente.PENDIENTE_ASIGNACION_COORDINADOR));
//
//            // Asignamos el estado de beneficiario
//            expedienteFirmado.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(
//                    Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA));

            this.expedienteDAO.update(expedienteFirmado);
            this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                    solicitud.getInteresado().getCdNumIden());

            // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
            // compartir el cdAnexoEregPadre. La entidad existe
            String cdAnexoEreg = this.find(listaExpedienteDocDTO.get(0).getIdExpedienteDoc())
                    .getCdAnexoEregPadre();

            // Obtenemos ahora el idExpedienteDoc que tiene ese anexo

            Integer idExpedienteDoc = null;
            List<ExpedienteDoc> doc = this.getDao().findByCdAnexoEreg(cdAnexoEreg);
            if (doc != null && !doc.isEmpty()) {
                idExpedienteDoc = doc.get(0).getIdExpedienteDoc();
            }

            // Generamos el objeto de respuesta
            registroEregDTO.setFechaAlta(
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
            registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
            // VMM MD-913 28/10/24: recuperamos el Ereg del ExpedienteJustec en
            // vez de en el propio expediente
            registroEregDTO
                    .setCdEreg(Utils.formatearNmEreg(expedienteJustecDTO.getCdEregSubsana()));
            registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
            registroEregDTO.setEsDescargaZipJustec(Boolean.TRUE);

        }

        if (Boolean.TRUE.equals(listaExpedienteDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return registroEregDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public List<ExpedienteDocDTO> obtenerDocumentosContrato(Integer idExpediente, Integer tramite)
            throws ServiceException {

        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = this.expedienteDocDAO
                .listExpedienteDocContratosPorTramite(idExpediente, tramite);

        if ((lstExpedienteDocDTOAux == null || lstExpedienteDocDTOAux.isEmpty())
                && tramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_RENUNCIA))) {
            this.expedienteService.registrarDocuemntosRenunciaContrato(idExpediente, tramite);
            lstExpedienteDocDTOAux = this.expedienteDocDAO
                    .listExpedienteDocContratosPorTramite(idExpediente, tramite);
        }

        lstExpedienteDocDTOAux = this.expedienteService
                .rellenarDocumentosExpediente(lstExpedienteDocDTOAux);
        if (Boolean.TRUE.equals(lstExpedienteDocDTOAux.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return lstExpedienteDocDTOAux;
    }

    @Override
    @Transactional(readOnly = false)
    public List<ExpedienteDocDTO> obtenerDocumentosSubsanarContrato(Integer idExpediente)
            throws ServiceException {
        List<ExpedienteDocDTO> resultDocDTO = new ArrayList<>();
        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = this.expedienteDocDAO
                .listExpedienteDocContratosSubsanadoPorTramite(idExpediente);
        if (lstExpedienteDocDTOAux != null && !lstExpedienteDocDTOAux.isEmpty()) {

            for (ExpedienteDocDTO doc : lstExpedienteDocDTOAux) {
                if ((doc.getEstadoDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO)))
                        && doc.getIdDocumentoPtfr() != null) {
                    DocumentoPtfr documentoPtfr = this.documentoPtfrDAO
                            .find(doc.getIdDocumentoPtfr());
                    if (documentoPtfr.getCdEstadoNote() != null
                            && documentoPtfr.getCdEstadoNote().equals(Constantes.Note.CD_L)
                            && Utils.fechasEntreRango(documentoPtfr.getFcIniPlazoSubsanacion(),
                                    documentoPtfr.getFcFinPlazoSubsanacion())
                                    .equals(Boolean.TRUE)) {
                        resultDocDTO.add(doc);
                    }

                }
            }

        }

        resultDocDTO = this.expedienteService.rellenarDocumentosExpediente(resultDocDTO);
        if (Boolean.TRUE.equals(resultDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return resultDocDTO;
    }

    /**
     * 
     * @param expedienteDocDTO
     * @return
     */
    @Override
    public ExpedienteDocDTO subirDocumentoFica(ExpedienteDocDTO expedienteDocDTO)
            throws ServiceException {

        FicaFichero ficheroRecuperar = null;
        FicaFichero fichero;

        // Validacion
        if (expedienteDocDTO.getContenidoFichero() == null
                || expedienteDocDTO.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        if (StringUtils.isBlank(expedienteDocDTO.getFicaMetadatos().getExtension())) {
            log.info("El Fichero no tiene extensión.");
            throw new ServiceException("El Fichero no tiene extensión.");
        }

        expedienteDocDTO.getFicaMetadatos().setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        if (expedienteDocDTO.getCdFica() != null) {
            ficheroRecuperar = this.ficaService.recuperar(expedienteDocDTO.getCdFica());
        }
        if (ficheroRecuperar == null) {
            fichero = this.ficaService.guardar(expedienteDocDTO.getContenidoFichero(),
                    expedienteDocDTO.getFicaMetadatos());
        } else {
            fichero = this.ficaService.actualizar(expedienteDocDTO.getCdFica(),
                    expedienteDocDTO.getContenidoFichero(), expedienteDocDTO.getFicaMetadatos());
        }
        expedienteDocDTO.setFicaFichero(fichero);
        expedienteDocDTO.setCdFica(fichero.getIdInterno());
        return expedienteDocDTO;

    }

    /**
     * 
     * @param idExpediente
     * @param lstTipoTramite
     * @return
     * @throws ServiceException
     */
    public Boolean existeTipoDocumentos(Integer idExpediente, List<Integer> lstTipoTramite)
            throws ServiceException {

        if (Objects.isNull(idExpediente) || Objects.isNull(lstTipoTramite)
                || lstTipoTramite.isEmpty()) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, lstTipoTramite, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        Integer total = this.getDao().count(filtros);

        return total > 0;

    }

    /**
     * 
     * @param idExpedienteJustec
     * @param idTramite
     * @return
     * @throws ServiceException
     */
    @Override
    public List<ExpedienteDocDTO> buscarPorExpedienteJustecYTramite(Integer idExpedienteJusTec,
            List<Integer> idTramite) throws ServiceException {
        return this.expedienteDocDAO.buscarPorExpedienteJustecYTramite(idExpedienteJusTec,
                idTramite);
    }
    

    
    @Override
    public List<ExpedienteDocDTO> buscarDocumentos(Integer index, Integer pageSize, Integer idSolicitud, List<Integer> lstTramites, Integer idExpediente, List<AtlasSort> orden) {
        
        return this.expedienteDocDAO.buscarDocumentos(index,pageSize,idSolicitud,lstTramites,idExpediente, orden);

    }
    
    
    

    @Override
    @Transactional(readOnly = false)
    public List<ExpedienteDocDTO> obtenerDocumentosPresupPlazosOtros(Integer idExpediente,
            Integer tramite) throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = new ArrayList<>();

        List<ExpedienteModifPresPlazoOtraDTO> modificaciones = expedienteModifPresPlazoOtraService
                .buscarPorExpedienteEstado(idExpediente, Constantes.EstadoExpedienteResolucion.PEDIENTE);

        if (modificaciones != null && !modificaciones.isEmpty()) {
            throw new ServiceException(Constantes.Error.ERROR_EXISTE_EXP_MOD_PENDIENTE);
        }

        ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(idExpediente);
        modificaciones = expedienteModifPresPlazoOtraService.buscarPorExpedienteEstado(idExpediente,
                Constantes.EstadoExpedienteResolucion.INICIADA);
        if (modificaciones != null && !modificaciones.isEmpty()) {

            ExpedienteModifPresPlazoOtraDTO expedienteModifPresPlazoOtraDTO = modificaciones.get(0);

            List<AtlasFilter> filtrosEntrada = new ArrayList<>();

            filtrosEntrada.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                            expedienteModifPresPlazoOtraDTO.getIdExpedienteModifPresPlazoOtra(),
                            Operador.EQUALS));
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteModfPresupPlazosOtrosDocDTO> expedienteModfPresupPlazosOtrosDocDTO = new ArrayList<>(
                    Lists.transform(
                            this.expedienteModfPresupPlazosOtrosDocService.getDao()
                                    .find(filtrosEntrada, null),
                            ExpedienteModfPresupPlazosOtrosDocToDTO.INSTANCE));

            if (!expedienteModfPresupPlazosOtrosDocDTO.isEmpty()) {

                for (ExpedienteModfPresupPlazosOtrosDocDTO doc : expedienteModfPresupPlazosOtrosDocDTO) {
                    lstExpedienteDocDTOAux.add(doc.getExpedienteDocDTO());
                }

            } else { // crear los docum
                ExpedienteModifPresPlazoOtra expedienteModifPresPlOtra = expedienteModifPresPlazoOtraService
                        .getDao()
                        .find(expedienteModifPresPlazoOtraDTO.getIdExpedienteModifPresPlazoOtra());
                lstExpedienteDocDTOAux = this.crearExpedienteModfPresupPlazosOtrosDoc(
                        expedienteModifPresPlOtra, expedienteDTO);

            }
        }else {
            modificaciones = expedienteModifPresPlazoOtraService.buscarPorExpediente(idExpediente);
            
            boolean nuevaModificacion= true;
            if (modificaciones != null && !modificaciones.isEmpty()) {
                for(ExpedienteModifPresPlazoOtraDTO modif:modificaciones) {
                    if(!modif.getEstadoDTO().getNmConstante().equals(Constantes.EstadoExpedienteResolucion.RESUELTA)) {
                        nuevaModificacion = false;
                        break;
                    }
                }
                
            }
            
            if(nuevaModificacion) {
             // crear expmodifpresupotros y documentos

                Integer idModificacion = null;
                ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra = new ExpedienteModifPresPlazoOtra();

                modificaciones = expedienteModifPresPlazoOtraService.buscarPorExpediente(idExpediente);
                if (modificaciones != null && !modificaciones.isEmpty()) {
                    idModificacion = modificaciones.size() + 1;
                } else {
                    idModificacion = 1;
                }
                expedienteModifPresPlazoOtra.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.INICIADA)));
                Expediente expediente = new Expediente();
                expediente.setIdExpediente(idExpediente);
                expedienteModifPresPlazoOtra.setExpediente(expediente);
                expedienteModifPresPlazoOtra.setIdModificacion(idModificacion);
                expedienteModifPresPlazoOtra.setItModOtra("N");
                expedienteModifPresPlazoOtra.setItModPlazo("N");
                expedienteModifPresPlazoOtra.setItModPres("N");
                expedienteModifPresPlazoOtra.setFcSolicitud(new Date());
                expedienteModifPresPlazoOtra.setCdUsuAlta(expedienteDTO.getCdUsuAlta());
                expedienteModifPresPlazoOtra.setFcAltaFila(expedienteDTO.getFcAltaFila());
                expedienteModifPresPlazoOtra.setIdImpreso(
                        expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO().getIdImpreso());

                ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtraNueva = expedienteModifPresPlazoOtraService
                        .getDao().insert(expedienteModifPresPlazoOtra);
                lstExpedienteDocDTOAux = this.crearExpedienteModfPresupPlazosOtrosDoc(
                        expedienteModifPresPlazoOtraNueva, expedienteDTO);
            }
            
        
            }
        

        lstExpedienteDocDTOAux = this.expedienteService
                .rellenarDocumentosExpediente(lstExpedienteDocDTOAux);

        return lstExpedienteDocDTOAux;
    }

    private List<ExpedienteDocDTO> crearExpedienteModfPresupPlazosOtrosDoc(
            ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtraNueva,
            ExpedienteDTO expedienteDTO) throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = new ArrayList<>();
        if (expedienteDTO != null
                && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO() != null
                && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                        .getIdImpreso() != null
                && expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                        .getLstBloqueDTO() != null
                && !(expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO())
                        .isEmpty()) {
            expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()
                    .sort(Comparator.comparing(BloqueDTO::getNmOrden));

            // Obtenemos los bloquesComponente que generen documentos
            for (BloqueDTO bloqueDTO : expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO()
                    .getLstBloqueDTO()) {

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
                        ExpedienteDocDTO expDocDTO = this.expedienteService.crearExpedienteDoc(
                                expedienteDTO, bloqueComponenteDTO,
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.MODIFICACION_PRESP_PLAZOS_OTROS));

                        ExpedienteModfPresupPlazosOtrosDoc expedienteModfPresupPlazosOtrosDoc = new ExpedienteModfPresupPlazosOtrosDoc();

                        expedienteModfPresupPlazosOtrosDoc
                                .setExpedienteDoc(ExpedienteDocToEntity.INSTANCE.apply(expDocDTO));
                        expedienteModfPresupPlazosOtrosDoc
                                .setExpedienteModifPresPlazoOtra(expedienteModifPresPlazoOtraNueva);
                        expedienteModfPresupPlazosOtrosDoc.setFcAltaFila(new Date());
                        expedienteModfPresupPlazosOtrosDoc
                                .setCdUsuAlta(expedienteModifPresPlazoOtraNueva.getCdUsuAlta());
                        this.expedienteModfPresupPlazosOtrosDocService
                                .insert(expedienteModfPresupPlazosOtrosDoc);
                        lstExpedienteDocDTOAux.add(expDocDTO);

                    }
                }
            }

        }

        return lstExpedienteDocDTOAux;
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarModDocumentosPresupPlazosOtros(ExpedienteDTO expedienteDTO,
            String cdNumIden) throws ServiceException {

        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
       
        ExpedienteModifPresPlazoOtraDTO expModifPresPlazoOtraDTO = expedienteDTO
                .getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO();

        List<ExpedienteDocDTO> listaExpedienteDocDTO = this
                .obtenerDocumentosPresupPlazosOtros(expedienteDTO.getIdExpediente(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.MODIFICACION_PRESP_PLAZOS_OTROS))
                .stream().filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                .collect(Collectors.toList());
        // Añadiremos los documentos subidos en conceptos en el presupuesto con detalle concepto.
        // Se consultan directamente en BD por el id de la modificación en vez de intentar
        // emparejarlos contra el impreso recibido en el payload del frontend, que puede no
        // reflejar fielmente el estado guardado (bloques/datos incompletos, idDato desincronizados, etc.)
        ImpresoDTO impresoMoficiaciones = expedienteDTO.getImpresoModificacionesPresupPlazosOtrosDTO();
        if (impresoMoficiaciones != null) {
            ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                    .apply(this.convocatoriaDAO.find(impresoMoficiaciones.getIdConvocatoria()));

            if (convocatoriaDTO.getItConceptoPresup() != null
                    && convocatoriaDTO.getItConceptoPresup().equalsIgnoreCase("S")) {

                List<AtlasFilter> filtrosDocConcepto = new ArrayList<>();
                filtrosDocConcepto.add(new AtlasFilter(
                        Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF_DATO,
                        expModifPresPlazoOtraDTO.getIdExpedienteModifPresPlazoOtra(),
                        Operador.EQUALS));
                filtrosDocConcepto.add(new AtlasFilter(
                        Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                        utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.TipoDocumento.DOCUMENTO_MODIF_CONCEPTO_PRESUP),
                        Operador.EQUALS));
                filtrosDocConcepto.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                filtrosDocConcepto.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));

                List<ExpedienteDocDTO> listaDocuConceptoPresup = new ArrayList<>(Lists.transform(
                        this.getDao().find(filtrosDocConcepto, null), ExpedienteDocToDTO.INSTANCE));

                listaDocuConceptoPresup.removeIf(d -> utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.EstadoDocumento.SIN_SUBIR).equals(d.getEstadoDTO().getIdValor()));

                if (!listaDocuConceptoPresup.isEmpty()) {
                    listaExpedienteDocDTO.addAll(listaDocuConceptoPresup);
                }
            }
        }

        expedienteDTO.setListaDocumentosPresup(listaExpedienteDocDTO);
        
        String dniLogin= expedienteDTO.getDniUserLogin();
        String cifEntidadLogin = expedienteDTO.getCifEntidadLogin();

        // Generamos el listado de anexos en ereg
        this.pdfService.generaFirmaReciboPresentacionPresupPlazosOtros(expedienteDTO);
        
        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.MODIFICACION_PRESP_PLAZOS_OTROS),
                expedienteDTO, dniLogin, cifEntidadLogin);

        
        ExpedienteModifPresPlazoOtra expModifPresPlazoOtra = expedienteModifPresPlazoOtraService
                .find(expModifPresPlazoOtraDTO.getIdExpedienteModifPresPlazoOtra());

        expModifPresPlazoOtra.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE)));
        if(listaExpedienteDocDTO != null && !listaExpedienteDocDTO.isEmpty()) {
            expModifPresPlazoOtra.setEstadoRevAdmin(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.PEDIENTE)));
        }else {
            expModifPresPlazoOtra.setEstadoRevAdmin(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.REVISION_OK)));
               
        }
        expModifPresPlazoOtra.setItModOtra(expModifPresPlazoOtraDTO.getItModOtra());
        expModifPresPlazoOtra.setItModPlazo(expModifPresPlazoOtraDTO.getItModPlazo());
        expModifPresPlazoOtra.setItModPres(expModifPresPlazoOtraDTO.getItModPres());
        expModifPresPlazoOtra.setCdEregPresent(expModifPresPlazoOtraDTO.getCdEregPresent());
        expModifPresPlazoOtra.setFcFirmaPresent(expModifPresPlazoOtraDTO.getFcFirmaPresent());
        expModifPresPlazoOtra.setDsDestinoEreg(expModifPresPlazoOtraDTO.getDsDestinoEreg());
        expModifPresPlazoOtra.setFcModifFila(new Date());
        this.expedienteModifPresPlazoOtraService.getDao().insertOrUpdate(expModifPresPlazoOtra);

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe
        /*String cdAnexoEreg = "";
        Integer idExpedienteDoc = null;
        if (expedienteDTO.getListaDocumentosPresup() != null
                && !expedienteDTO.getListaDocumentosPresup().isEmpty()) {
            cdAnexoEreg = this
                    .find(expedienteDTO.getListaDocumentosPresup().get(0).getIdExpedienteDoc())
                    .getCdAnexoEregPadre();
            // Obtenemos ahora el idExpedienteDoc que tiene ese anexo

            List<ExpedienteDoc> doc = this.getDao().findByCdAnexoEreg(cdAnexoEreg);
            if (doc != null && !doc.isEmpty()) {
                idExpedienteDoc = doc.get(0).getIdExpedienteDoc();
            }
        }*/

        // Generamos el objeto de respuesta
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        //registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
        registroEregDTO
                .setCdEreg(Utils.formatearNmEreg(expModifPresPlazoOtraDTO.getCdEregPresent()));
        //registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipModPresupPlazoOtros(Boolean.TRUE);

        return registroEregDTO;
    }

    @Override
    public String obtenerNombreDocumentoConceptoPresupuesto(Integer idDato) throws ServiceException {

        if (idDato == null) {
            return null;
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DATO_ID_DATO, idDato, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.DOCUMENTO_MODIF_CONCEPTO_PRESUP),
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));

        List<ExpedienteDoc> lstDoc = this.getDao().find(filtros, null);

        if (lstDoc == null || lstDoc.isEmpty()) {
            return null;
        }

        Integer cdFica = lstDoc.get(0).getCdFica();
        FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);

        if (ficaFichero == null) {
            return null;
        }

        return this.expedienteService.toUnicodeEscaped(ficaFichero.getNombreFichero());
    }

    @Override
    public List<ExpedienteDocDTO> obtenerDocumentosConceptoPresupuesto(Integer idModificacion)
            throws ServiceException {

        if (idModificacion == null) {
            return new ArrayList<>();
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF_DATO,
                idModificacion, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                utilConstantes.getGlobalCatalogosValores()
                        .get(Constantes.TipoDocumento.DOCUMENTO_MODIF_CONCEPTO_PRESUP),
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));

        List<ExpedienteDocDTO> lstDoc = new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), ExpedienteDocToDTO.INSTANCE));

        Integer idSinSub = utilConstantes.getGlobalCatalogosValores()
                .get(Constantes.EstadoDocumento.SIN_SUBIR);
        lstDoc.removeIf(d -> idSinSub.equals(d.getEstadoDTO().getIdValor()));

        return lstDoc;
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarSubsanacionReq(List<ExpedienteDTO> listExpParticipantes,
            String cdNumIden, String dniLogin, String cdNumIdenFuncional) throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>(); // Lista
                                                                        // para
                                                                        // almacenar
                                                                        // los
                                                                        // ExpedienteDocDTO
                                                                        // que
                                                                        // cumplen
                                                                        // la
                                                                        // condición
        ExpedienteDTO expedienteDTO = null;
        if (!listExpParticipantes.isEmpty()
                && listExpParticipantes.get(0).getIdExpedientePadre() != null) {
            expedienteDTO = this.expedienteService
                    .buscarId(listExpParticipantes.get(0).getIdExpedientePadre());
        }

        for (ExpedienteDTO expDTO : listExpParticipantes) {
            // Itera sobre la lista de ExpedienteDocDTO dentro de cada
            // ExpedienteDTO
            for (ExpedienteDocDTO expedienteDocDTO : expDTO.getLstExpedienteDocDTO()) {
                // Verifica si el estado del ExpedienteDocDTO cumple la
                // condición
                if (expedienteDocDTO.getEstadoDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_PRESENTAR))) {
                    lstExpedienteDocDTO.add(expedienteDocDTO); // Añade el
                                                               // ExpedienteDocDTO
                                                               // a la lista
                }
            }
        }

        /*
         * YA TENEMOS UN LISTA CON LOS HIJOS DEL PADRE QUE ES LA QUE TENDREMOS
         * QUE PINTAR EN EL PDF
         */

        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el listado de anexos en ereg
       
        this.pdfService.generaFirmaReciboPresentacionReq(expedienteDTO, null, lstExpedienteDocDTO,
                listExpParticipantes);
       
        String cifEntidadLogin= null;
        if(!cdNumIdenFuncional.equalsIgnoreCase(dniLogin)) {
            cifEntidadLogin=cdNumIdenFuncional;
        }

        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                expedienteDTO, dniLogin, cifEntidadLogin);

        // Cambiamos el estado y creamos el histórico (el cdNumIden se asigna
        // desde el token)

        for (ExpedienteDocDTO dto : lstExpedienteDocDTO) {
            ExpedienteDoc expedienteDoc = this.buscarIdExpDoc(dto.getIdExpedienteDoc());
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
            expedienteDoc.setEstado(estado);
            expedienteDoc.setFcModifFila(new Date());
            expedienteDoc.setCdUsuModif(cdNumIden);
            this.update(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(ExpedienteDocToEntity.INSTANCE.apply(dto),
                    cdNumIden);
        }

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe
        String cdAnexoEreg = this.find(lstExpedienteDocDTO.get(0).getIdExpedienteDoc())
                .getCdAnexoEregPadre();

        // Obtenemos ahora el idExpedienteDoc que tiene ese anexo
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdAnexoEreg,
                Operador.EQUALS));

        Integer idExpedienteDoc = this.find(filtros, null).get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdAnexoEreg);

        registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEregSubsanaReq()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSubsanacionReq(Boolean.TRUE);

        if (Boolean.TRUE.equals(lstExpedienteDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return registroEregDTO;
    }

    /**
    *
    */
    @Override
    public ExpedienteDocDTO descargar(Integer cdFica) throws ServiceException {

        if (Objects.isNull(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ExpedienteDocDTO expedienteDocDTO = new ExpedienteDocDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
        expedienteDocDTO.setFicaFichero(ficaFichero);
        expedienteDocDTO.setContenidoFichero(ficaFichero.getDatosFichero());
        expedienteDocDTO.setCdFica(cdFica);
        return expedienteDocDTO;
    }

    /**
    *
    */
    public ExpedienteDocDTO buscarPorIdExpTipoDoc(Integer idExpediente, Integer idTipoDocumento,
            Integer idTramite) throws ServiceException {

        ExpedienteDoc expedienteDoc = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDoc> lstExpedienteDoc = this.find(filtros, null);

        for (ExpedienteDoc expeDoc : lstExpedienteDoc) {
            if (Objects.nonNull(expeDoc.getTipoDocumento())
                    && Objects.nonNull(expeDoc.getTipoDocumento().getIdValor())
                    && idTipoDocumento.equals(expeDoc.getTipoDocumento().getIdValor())
                    && idTramite.equals(expeDoc.getTramite().getIdValor())) {
                expedienteDoc = expeDoc;
                break;
            }
        }

        return ExpedienteDocToDTO.INSTANCE.apply(expedienteDoc);
    }

    @Override
    @Transactional(readOnly = false)
    public RegistroEregDTO presentarSubsanacionAdm(List<ExpedienteDTO> listExpParticipantes,
            String cdNumIden, String dniLogin) throws ServiceException {
        String cdAnexoEreg = null;
        List<ExpedienteDocDTO> lstExpedienteDocDTO = new ArrayList<>(); // Lista
                                                                        // para
                                                                        // almacenar
                                                                        // los
                                                                        // ExpedienteDocDTO
                                                                        // que
                                                                        // cumplen
                                                                        // la
                                                                        // condición
        ExpedienteDTO expedienteDTO = null;
        if (!listExpParticipantes.isEmpty()
                && listExpParticipantes.get(0).getIdExpedientePadre() != null) {
            expedienteDTO = this.expedienteService
                    .buscarId(listExpParticipantes.get(0).getIdExpedientePadre());
        }

        for (ExpedienteDTO expDTO : listExpParticipantes) {
            // Itera sobre la lista de ExpedienteDocDTO dentro de cada
            // ExpedienteDTO
            for (ExpedienteDocDTO expedienteDocDTO : expDTO.getLstExpedienteDocDTO()) {
                // Verifica si el estado del ExpedienteDocDTO cumple la
                // condición
                if (expedienteDocDTO.getEstadoDTO().getIdValor()
                        .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.EN_TRAMITE_SUBSANACION))) {
                    lstExpedienteDocDTO.add(expedienteDocDTO); // Añade el
                                                               // ExpedienteDocDTO
                                                               // a la lista
                }
            }
        }

        /*
         * YA TENEMOS UN LISTA CON LOS HIJOS DEL PADRE QUE ES LA QUE TENDREMOS
         * QUE PINTAR EN EL PDF
         */

        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el listado de anexos en ereg
        
        cdAnexoEreg = this.pdfService.generaFirmaReciboPresentacionAdm(expedienteDTO, null,
                lstExpedienteDocDTO, listExpParticipantes);
        
        String cifEntidadLogin= null;
        if(!cdNumIden.equalsIgnoreCase(dniLogin)) {
            cifEntidadLogin=cdNumIden;
        }
        
        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                expedienteDTO, dniLogin, cifEntidadLogin);      

        // Cambiamos el estado y creamos el histórico (el cdNumIden se asigna
        // desde el token)

        for (ExpedienteDocDTO dto : lstExpedienteDocDTO) {
            ExpedienteDoc expedienteDoc = this.buscarIdExpDoc(dto.getIdExpedienteDoc());
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
            expedienteDoc.setEstado(estado);
            expedienteDoc.setFcModifFila(new Date());
            this.update(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(ExpedienteDocToEntity.INSTANCE.apply(dto),
                    dto.getCdUsuModif());
        }

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe
//        cdAnexoEreg = this.find(lstExpedienteDocDTO.get(0).getIdExpedienteDoc())
//                .getCdAnexoEregPadre();

        // Obtenemos ahora el idExpedienteDoc que tiene ese anexo
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, cdAnexoEreg,
                Operador.EQUALS));

        Integer idExpedienteDoc = this.find(filtros, null).get(0).getIdExpedienteDoc();

        // Generamos el objeto de respuesta
        registroEregDTO
                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdAnexoEreg);

        registroEregDTO.setCdEreg(Utils.formatearNmEreg(expedienteDTO.getCdEregSubsanaAdm()));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipSubsanacionReq(Boolean.TRUE);

//        registroEregDTO
//                .setFechaAlta(Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
//        registroEregDTO.setCdAnexoEreg("cdAnexoEreg");
//        
//        registroEregDTO.setCdEreg("cdEreg");
//        registroEregDTO.setIdExpedienteDoc(1);
//        registroEregDTO.setEsDescargaZipSubsanacionReq(Boolean.TRUE);

        if (Boolean.TRUE.equals(lstExpedienteDocDTO.isEmpty())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return registroEregDTO;
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idExpedienteDoc) throws ServiceException {
        this.deleteLogico(idExpedienteDoc);
    }

    @Override
    public RegistroEregDTO presentarAceptacionDesistimientoAyuda(ExpedienteDTO dto,
            String cdNumIden) throws ServiceException {
        
        ExpedienteDTO expedienteDTO = this.expedienteService
                .buscarId(dto.getIdExpediente());
       List<ExpedienteDocDTO> listaExpedienteDocDTO= new ArrayList<>();
       if(expedienteDTO.getListaDocAceptacionAyuda() != null && !expedienteDTO.getListaDocAceptacionAyuda().isEmpty()) {
           listaExpedienteDocDTO=expedienteDTO.getListaDocAceptacionAyuda().stream()
                   .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null)
                   .collect(Collectors.toList());

       }

       // Documentos de la solicitud inicial cuya consulta no se autorizó y que se han
       // aportado en este mismo trámite de aceptación/desistimiento de la ayuda.
       if (expedienteDTO.getLstExpedienteDocDTO() != null) {
           List<Integer> idsYaIncluidos = listaExpedienteDocDTO.stream()
                   .map(ExpedienteDocDTO::getIdExpedienteDoc).collect(Collectors.toList());

           List<ExpedienteDocDTO> listaDocNoAutorizados = expedienteDTO.getLstExpedienteDocDTO().stream()
                   .filter(x -> x.getFcBajaFila() == null && x.getCdFica() != null
                           && x.getTramiteDTO() != null
                           && Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA.equals(x.getTramiteDTO().getNmConstante())
                           && !idsYaIncluidos.contains(x.getIdExpedienteDoc()))
                   .collect(Collectors.toList());

           listaExpedienteDocDTO.addAll(listaDocNoAutorizados);
       }


        // Todos los expedienteDoc del listado pertenecen al mismo expediente.
        // Lo recuperamos
        // Generamos el objeto de respuesta
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        
        // Generamos el listado de anexos en ereg
        this.pdfService.generaFirmaReciboPresentacionAceptacion(expedienteDTO,listaExpedienteDocDTO);
        
        this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA),
                expedienteDTO, dto.getDniUserLogin(), dto.getCifEntidadLogin());

        // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
        // compartir el cdAnexoEregPadre. La entidad existe
       
        String cdAnexoEreg=null;
        String cdEregPresent=null;
        Integer idExpedienteDoc = null;
        if(!listaExpedienteDocDTO.isEmpty()) {
            ExpedienteDoc expedienteDoc = this.find(listaExpedienteDocDTO.get(0).getIdExpedienteDoc());
            
            cdAnexoEreg = expedienteDoc.getCdAnexoEregPadre();
            
            cdEregPresent =expedienteDoc.getCodigoEreg();

            // Obtenemos ahora el idExpedienteDoc que tiene ese anexo

            List<ExpedienteDoc> doc = this.getDao().findByCdAnexoEreg(cdAnexoEreg);
            if (doc != null && !doc.isEmpty()) {
                idExpedienteDoc = doc.get(0).getIdExpedienteDoc();
            }
        }
        

        
        // Generamos el objeto de respuesta
        registroEregDTO.setFechaAlta(
                Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
        registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
        registroEregDTO.setCdEreg(Utils.formatearNmEreg(cdEregPresent));
        registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
        registroEregDTO.setEsDescargaZipAceptacion(Boolean.TRUE);
        


        return registroEregDTO;
    }

    @Override
    public RegistroEregDTO presentarJustificacionEconomica(
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO, String cdNumIden, String dniLogin)
            throws ServiceException {
        RegistroEregDTO registroEregDTO = new RegistroEregDTO();
        
        if(listExpedienteJusEcoDTO != null && !listExpedienteJusEcoDTO.isEmpty()) {
            ExpedienteDTO expedienteDTO =this.expedienteService.buscarId(listExpedienteJusEcoDTO.get(0).getExpedienteJusEcoDTO().getIdExpediente());
            this.pdfService.generaFirmaReciboPresentacionJusEco(expedienteDTO,listExpedienteJusEcoDTO);
            
            String cifEntidadLogin= null;
            if(!cdNumIden.equalsIgnoreCase(dniLogin)) {
                cifEntidadLogin=cdNumIden;
            }
            this.tramiteSolicitanteExpedienteService.guardarTramiteSolicitanteExpediente(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA),
                    expedienteDTO, dniLogin, cifEntidadLogin);
            
         // Obtenemos el cdAnexoEreg de alguno de los documentos. Todos van a
            // compartir el cdAnexoEregPadre. La entidad existe
            ExpedienteDoc expedienteDoc = this.find(listExpedienteJusEcoDTO.get(0).getExpedienteDocDTO().getIdExpedienteDoc());
                    
            String cdAnexoEreg = expedienteDoc.getCdAnexoEregPadre();
            
            String cdEregPresent =expedienteDoc.getCodigoEreg();

            // Obtenemos ahora el idExpedienteDoc que tiene ese anexo

            Integer idExpedienteDoc = null;
            List<ExpedienteDoc> doc = this.getDao().findByCdAnexoEreg(cdAnexoEreg);
            if (doc != null && !doc.isEmpty()) {
                idExpedienteDoc = doc.get(0).getIdExpedienteDoc();
            }

            
            // Generamos el objeto de respuesta
            registroEregDTO.setFechaAlta(
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));
            registroEregDTO.setCdAnexoEreg(cdAnexoEreg);
            registroEregDTO.setCdEreg(Utils.formatearNmEreg(cdEregPresent));
            registroEregDTO.setIdExpedienteDoc(idExpedienteDoc);
            registroEregDTO.setEsDescargaZipJusEco(Boolean.TRUE);
        }else {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        
        return registroEregDTO;
    }

    @Override
    public List<ExpedienteModifPresPlazoOtraDTO> obtenerModifPresupPlazosOtros(Integer idExpediente) throws ServiceException {
        
        List<ExpedienteModifPresPlazoOtraDTO> listResult= expedienteModifPresPlazoOtraService.buscarPorExpediente(idExpediente);
        if(listResult != null && !listResult.isEmpty()) {
           for(ExpedienteModifPresPlazoOtraDTO item:listResult ) {
               if(item.getFcResolucion() != null) {
                   List<DocumentoPtfrDTO> lstDocumentoPtfrDTO = documentoPtfrDAO.obtenerPorExpedienteModifPresupPlazosTramites(idExpediente,
                           utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA),item.getIdExpedienteModifPresPlazoOtra());
                   if(lstDocumentoPtfrDTO != null && !lstDocumentoPtfrDTO.isEmpty()) {
                       item.setNotificacionResolucionPtfr(lstDocumentoPtfrDTO.get(0));
                   }
               }
                
            }
        }
      
        return listResult;
    }

   

}
