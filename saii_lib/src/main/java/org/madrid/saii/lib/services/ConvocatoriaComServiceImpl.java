package org.madrid.saii.lib.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dao.ComunicacionCorreoActorDAO;
import org.madrid.saii.lib.dao.ComunicacionNoteActorDAO;
import org.madrid.saii.lib.dao.ConvocatoriaComDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.dao.ExpedienteContratoHistDAO;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.NoteEnviarNotificacionDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ComunicacionCorreoActorToEntity;
import org.madrid.saii.lib.dto.converters.ComunicacionNoteActorToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaComToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaComToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToDTO;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.mail.MailService;
import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.itextpdf.text.Element;
import com.itextpdf.text.html.simpleparser.HTMLWorker;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaCom
 *
 * @author nttdata
 */
@Slf4j
@Service("convocatoriaComService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaComServiceImpl
        extends BaseServiceImpl<ConvocatoriaCom, Integer, ConvocatoriaComDAO>
        implements ConvocatoriaComService {

    /** catalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /** convocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /** documentoPtfrDAO */
    @Autowired
    @Qualifier("documentoPtfrDAO")
    private DocumentoPtfrDAO documentoPtfrDAO;

    /** mailService */
    @Autowired
    @Qualifier("mailService")
    private MailService mailService;

    /** convocatoriaComDAO */
    @Autowired
    @Qualifier("convocatoriaComDAO")
    private ConvocatoriaComDAO convocatoriaComDAO;

    /** convocatoriaPlaDAO */
    @Autowired
    @Qualifier("convocatoriaPlaDAO")
    private ConvocatoriaPlaDAO convocatoriaPlaDAO;

    /** ExpedienteService */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /** solicitudService */
    @Autowired
    @Qualifier("solicitudService")
    private SolicitudService solicitudService;

    /** Servicio de pdfService */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * NoteAdmeService
     */
    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;

    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;

    /**
     * Servicio de Dato
     */
    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;
    
    /**
     * Servicio de ImpresoService
     */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;
    
    /**
     * Servicio asociado a la entidad Bloque
     */
    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;
    
    
    /**
     * Servicio asociado a la entidad BloqueComponente
     */
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;
    
    
    /**
     * Servicio Tramite
     */
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;

    @Autowired
    @Qualifier("expedienteContratoDAO")
    private ExpedienteContratoDAO expedienteContratoDAO;

    @Autowired
    @Qualifier("expedienteContratoHistDAO")
    private ExpedienteContratoHistDAO expedienteContratoHistDAO;
    
    /** TipoResolConvocatoria **/
    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;
    
    @Autowired
    @Qualifier("comunicacionCorreoActorService")
    private ComunicacionCorreoActorService comunicacionCorreoActorService;
    
    @Autowired
    @Qualifier("comunicacionNoteActorService")
    private ComunicacionNoteActorService comunicacionNoteActorService;
    
    @Autowired
    @Qualifier("comunicacionCorreoActorDAO")
    private ComunicacionCorreoActorDAO comunicacionCorreoActorDAO;
    
    @Autowired
    @Qualifier("comunicacionNoteActorDAO")
    private ComunicacionNoteActorDAO comunicacionNoteActorDAO;
    
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaComDAO
     */
    @Autowired
    public ConvocatoriaComServiceImpl(@Qualifier("convocatoriaComDAO")
    ConvocatoriaComDAO convocatoriaComDAO) {
        super();
        this.setDao(convocatoriaComDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaComServiceImpl() {
        super();
    }

    /**
     *
     */
    @Override
    public ConvocatoriaComDTO buscarId(Integer idConvocatoriaCom) throws ServiceException {
        return ConvocatoriaComToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaCom));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaComDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaComToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaComDTO guardar(ConvocatoriaComDTO convocatoriaComDTO)
            throws ServiceException {
        return ConvocatoriaComToDTO.INSTANCE.apply(
                this.insertOrUpdate(ConvocatoriaComToEntity.INSTANCE.apply(convocatoriaComDTO)));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void altaLogica(final ConvocatoriaComDTO convocatoriaComDTO) throws ServiceException {
        convocatoriaComDTO.setCdUsuBaja(null);
        convocatoriaComDTO.setFcBajaFila(null);
        this.getDao().update(ConvocatoriaComToEntity.INSTANCE.apply(convocatoriaComDTO));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final ConvocatoriaComDTO convocatoriaComDTO) throws ServiceException {
        convocatoriaComDTO.setCdUsuBaja(UserUtils.getUserName());
        convocatoriaComDTO.setFcBajaFila(new Date());
        this.getDao().update(ConvocatoriaComToEntity.INSTANCE.apply(convocatoriaComDTO));
    }

    /**
     *
     */
    @Override
    public void nuevaConvocatoria(Convocatoria convocatoria) throws ServiceException {

        // VMM 26/09/24 MD-593: ahora solo cargamos en las comunicaciones las
        // plantillas del gestor (catalogo 38)
        List<CatalogosValoresDTO> listaTramite = this.catalogosValoresService
                .buscarPorCatalogo(utilConstantes.getGlobalCatalogos().get(Constantes.Cata.TRAMITES_GESTOR));
        
        // MD-1502 JRFM - Recuperamos el tipo de resoluciona modalidad de la convocatoria 
        // para ver si es del tipo conjunto o individual
        TipoResolModalidad tipoResolModalidad = null;
        String cdTipoResolucion = null;
        if (convocatoria.getIdModalidadConvocatoria().getIdValor() != null) {
            tipoResolModalidad = tipoResolModalidadDAO.findByIdModalidad(convocatoria.getIdModalidadConvocatoria().getIdValor());
            if(tipoResolModalidad != null && tipoResolModalidad.getCdTipoResolucion() != null) {
                cdTipoResolucion = tipoResolModalidad.getCdTipoResolucion();
            }
        }

        Iterator<CatalogosValoresDTO> itTramite = listaTramite.iterator();

        while (itTramite.hasNext()) {
            
            CatalogosValoresDTO valorTramite = itTramite.next();
            
            if (valorTramite.getIdValor() != null) {
                
                List<ConvocatoriaComDTO> lstConvCom = this.getTramiteConv(convocatoria.getIdConvocatoria(), valorTramite.getIdValor());
                
                // Si no existe y esResolucionIndividual se guarda
                if(lstConvCom.isEmpty() && this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                    
                    ConvocatoriaCom convocatoriaCom = this.construirConvocatoriaCom(convocatoria, valorTramite);
                    ConvocatoriaComDTO dto = ConvocatoriaComToDTO.INSTANCE.apply(convocatoriaCom);
                    this.guardar(dto);
                    
                }
                // Si existe
                else if(!lstConvCom.isEmpty()) {
                    
                    ConvocatoriaComDTO dto = lstConvCom.get(0);
                    
                    // Si no esta dado de baja y no es esResolucionIndividual lo damos de baja
                    if(dto.getFcBajaFila() == null && !this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                        this.procesaBajaLogicaComunicacion(dto);
                    }
                    // Si esta dado de baja y es esResolucionIndividual lo damos de alta
                    else if(dto.getFcBajaFila() != null && this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                        this.inicializaConvocatoriaComDTO(dto, valorTramite);
                        dto.setCdUsuBaja(null);
                        dto.setFcBajaFila(null);
                        this.getDao().merge(ConvocatoriaComToEntity.INSTANCE.apply(dto));
                    }

                }
            }
        }
    }
    
    /**
     * Metodo encargado de botener si existen una comunicacion para esa convocatoria en base al tramite
     * @param idConvocatoria
     * @param idTramite
     * @return
     * @throws ServiceException
     */
    private List<ConvocatoriaComDTO> getTramiteConv(Integer idConvocatoria, Integer idTramite)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaComToDTO.INSTANCE));

    }
    
    /**
     * Metodo para revisar si ppara el tipo de tramite y el tipo de resolucion es indivudial o no
     * @param cdTipoResolucion
     * @param idValor
     * @return
     */
    private boolean esResolucionIndividual(String cdTipoResolucion, Integer idValor) {
        
        Boolean rst = Boolean.TRUE;
        
        if(Constantes.TipoResolucionModalidad.RESOLUCION_CONJUNTA.equals(cdTipoResolucion) && 
                (idValor.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL)) 
                    || idValor.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA)) 
                    || idValor.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA))
                    || idValor.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL)) 
                )) {
            rst = Boolean.FALSE;
        }
        
        return rst;
    }
    
    /**
     * Metodo encargado de contruir el objeto para el guardado
     * @param convocatoria
     * @param valorTramite
     * @return
     */
    private ConvocatoriaCom construirConvocatoriaCom(Convocatoria convocatoria, CatalogosValoresDTO valorTramite) {
        
        ConvocatoriaCom convocatoriaCom = new ConvocatoriaCom();
        convocatoriaCom.setIdConvocatoria(convocatoria.getIdConvocatoria());
        convocatoriaCom.setTramite(CatalogosValoresToEntity.INSTANCE.apply(valorTramite));

        // Según el tipo de tramite tendrá marcada un tipo de envío
        // específico o ambas a la vez
        convocatoriaCom.setItNote(this.comprobarTipoTramiteNote(valorTramite.getIdValor()));
        convocatoriaCom.setItCorreo(this.comprobarTipoTramiteCorreo(valorTramite.getIdValor()));
        
        return convocatoriaCom;
        
    }
   
    /**
     * Metodo encargado de la baja logica de la comunicacion y las subtablas
     * 
     * @param dto
     * @throws ServiceException
     */
    private void procesaBajaLogicaComunicacion(ConvocatoriaComDTO dto) throws ServiceException {
       
       // Preparamos el flitro
       List<AtlasFilter> filtrosCorreos = new ArrayList<>();
       filtrosCorreos.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_COM, dto.getIdConvocatoriaCom(), Operador.EQUALS));
       filtrosCorreos.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       
       // Buscamos los actores por correo y los damos de baja
       List<ComunicacionCorreoActorDTO> listaCorreosExistentes = this.comunicacionCorreoActorService.buscar(0, -1, filtrosCorreos, null);
       
       if(!listaCorreosExistentes.isEmpty()) {
           
           for(ComunicacionCorreoActorDTO comunicaCorreo : listaCorreosExistentes) {
               comunicaCorreo.setFcBajaFila(new Date());
               comunicaCorreo.setCdUsuBaja((UserUtils.getUserName().toUpperCase()));
               comunicacionCorreoActorDAO.merge(ComunicacionCorreoActorToEntity.INSTANCE.apply(comunicaCorreo));
           }
           
       }
       
       // Buscamos los actores por note y los damos de baja
       List<ComunicacionNoteActorDTO> listaNoteExistentes = this.comunicacionNoteActorService.buscar(0, -1, filtrosCorreos, null);
       
       if(!listaNoteExistentes.isEmpty()) {
           
           for(ComunicacionNoteActorDTO comunicaNote : listaNoteExistentes) {
               comunicaNote.setFcBajaFila(new Date());
               comunicaNote.setCdUsuBaja((UserUtils.getUserName().toUpperCase()));
               comunicacionNoteActorDAO.merge(ComunicacionNoteActorToEntity.INSTANCE.apply(comunicaNote));
           }
           
       }
       
       // Borrado logico y quitamos los actores
       dto.setCdUsuBaja(UserUtils.getUserName());
       dto.setFcBajaFila(new Date());
       this.getDao().merge(ConvocatoriaComToEntity.INSTANCE.apply(dto));
       
    }
    
    private void inicializaConvocatoriaComDTO(ConvocatoriaComDTO dto, CatalogosValoresDTO valorTramite) {
        dto.getConvocatoriaActNoteDTO().setIdActor(null);
        dto.getConvocatoriaActCorreoDTO().setIdActor(null);
        dto.setItNote(this.comprobarTipoTramiteNote(valorTramite.getIdValor()));
        dto.setItCorreo(this.comprobarTipoTramiteCorreo(valorTramite.getIdValor()));
    }
    
    /**
     *
     * @param valorTramite
     * @return 
     */
    public String comprobarTipoTramiteNote(Integer valorTramite) {

        // VMM 04/10/24 MD-593: se quitan los tramites de comunicaciones que
        // sean del ciudadano, solo dejamos los del gestor
        if (// Constantes.Tramite.PRESENTACION.equals(valorTramite) ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA.equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_FINAL_JUSTIFICACION_ECONOMICA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUST_ECONOMICA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUSTIFICACION_CIENTIFICO_TECNICA)
                        .equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL.equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_JUSTIFICACION_ECONOMICA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA)
                        .equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL.equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION.equals(valorTramite)
                // VMM 31/07/24 MD-505: se cambia por el tramite de
                // requerimiento de presentacion de la JCT (revisar esto)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                        .equals(valorTramite)
                // VMM 26/08/24 MD-548: se pone tambien como note el tramite de
                // presentacion de JCT y de req de subsanacion JCT y contratos
                // ||
                // Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA.equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                        .equals(valorTramite)
                // VMM 26/09/24 MD-593: aniadimos los nuevos tramites de req de
                // contratos aqui pero no se si van con este tipo comunic
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_INICIAL)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_SUSTITUCION)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_RENUNCIA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION).equals(valorTramite)
                // HLA 10/10/2024 MD-591: tramites de concesion individual
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO).equals(valorTramite)
             // MD-2190 Quitamos la comprobación del tipo de trámite note de alegación exclusión provisional
//                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL).equals(valorTramite)
				// MD-2147 añadimos note exclusión provisional
				|| utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(valorTramite)
				// MD-2148 añadimos note al desistimiento provisional
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA).equals(valorTramite)) {
            return Constantes.AfirmacionNegacion.S;

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_ACTA_FINAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_JUSTIFICACION_ECONOMICA).equals(valorTramite)) {
            return Constantes.AfirmacionNegacion.N;
        }

        return Constantes.AfirmacionNegacion.N;

    }

    /**
     *
     * @param valorTramite
     * @return
     */
    public String comprobarTipoTramiteCorreo(Integer valorTramite) {

        // VMM 04/10/24 MD-593: se quitan los tramites de comunicaciones que
        // sean del ciudadano, solo dejamos los del gestor
        if (// Constantes.Tramite.PRESENTACION.equals(valorTramite) ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA.equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_FINAL_JUSTIFICACION_ECONOMICA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(valorTramite)
             // MD-2190 Quitamos la comprobación del tipo de trámite note de alegación exclusión provisional
//                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACION_EXCLUSION_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA).equals(valorTramite)) {
            return Constantes.AfirmacionNegacion.N;
        } else if (// Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL.equals(valorTramite)
                   // ||
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_JUSTIFICACION_ECONOMICA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_BORRADOR_ACTA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_ACTA_FINAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INFORME_JUSTIFICACION_ECONOMICA).equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA.equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL.equals(valorTramite)
                // ||
                // Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION.equals(valorTramite)
                // VMM 31/07/24 MD-505: se cambia por el tramite de
                // requerimiento de presentacion de la JCT (revisar esto)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA)
                        .equals(valorTramite)
                // VMM 26/08/24 MD-548: se ponen con correo los req de
                // subsanacion JCT y contratos
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_RENUNCIA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA)
                        .equals(valorTramite)
                // VMM 26/09/24 MD-593: aniadimos los nuevos tramites de req de
                // contratos aqui pero no se si van con este tipo comunic
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_INICIAL)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_SUSTITUCION)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_PRESENTACION_CONTRATO_RENUNCIA)
                        .equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUST_ECONOMICA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.INICIO_PLAZOS_JUSTIFICACION_CIENTIFICO_TECNICA)
                        .equals(valorTramite)
                // HLA 10/10/2024 MD-591: tramites de concesion individual
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL).equals(valorTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO).equals(valorTramite)
				// MD-2147 añadimos el correo de RESOLUCION EXCLISION PROVISIONAL
				|| utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(valorTramite)
				// MD-2148 añadimos correo al desistimiento provisional
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(valorTramite)) {
            return Constantes.AfirmacionNegacion.S;
        }

        return Constantes.AfirmacionNegacion.N;

    }

    /**
     * Param idConvocatoria Param idExpediente Param idTipoTramite
     */
    @Override
    public List<String> enviarComunicaciones(Integer idConvocatoria, Integer idExpediente,
            Integer idTipoTramite, Integer idPeticion, InputStream documentoFirmado)
            throws ServiceException {

        List<String> lstCdNote = new ArrayList<>();

        // Obtenemos los datos de comunicación
        ConvocatoriaComDTO convocatoriaComDTO = this.obtener(idConvocatoria, idTipoTramite);

        // Obtenemos los datos de la convocatoria
        ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                .apply(this.convocatoriaDAO.find(idConvocatoria));

        // Obtenemos los datos de la plantilla
        ConvocatoriaPlaDTO convocatoriaPlaDTO = this.obtenerConvocatoriaPla(idConvocatoria,
                idTipoTramite);

        // Obtenemos los datos de la solicitud asociada al expediente que
        // realiza la petición
        SolicitudDTO solicitudDTO = this.solicitudService.buscarPorExpediente(idExpediente);

        // Preparamos los servicios para el trámite
        Tramite tramite = tramiteService.getTramite(idTipoTramite);
        if (tramite != null) {
            tramite.setPdfService(this.pdfService);
            tramite.setCatalogosValoresService(this.catalogosValoresService);
            tramite.setExpedienteService(this.expedienteService);
        }

        // Obtenemos la lista de personas a notificar segun lo configurado
        // para la comunicacion
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_SOLICITUD_IDSOLICITUD,
                solicitudDTO.getIdSolicitud(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ExpedienteDTO> listaExp = this.expedienteService.buscar(0, -1, filtros, null);

        Integer tipoActorComunicacion;
        Integer tipoActorExpediente;
        Integer tipoPersonaExpediente;
        Integer tipoPersonaComunicacion;

        // Recuperamos el expediente tramitado
        ExpedienteDTO expediente = this.expedienteService.buscarId(idExpediente);

        // Por cada expediente recuperado realizamos los envios de
        // notificaciones según lo configurado
        for (ExpedienteDTO expedienteDTO : listaExp) {

            tipoActorExpediente = expedienteDTO.getConvocatoriaActDTO().getTipoActorDTO()
                    .getIdValor();
            tipoPersonaExpediente = expedienteDTO.getConvocatoriaActDTO().getTipoPersonaFisJurDTO()
                    .getIdValor();
            if (Objects.nonNull(convocatoriaComDTO.getConvocatoriaActNoteDTO())) {

                tipoActorComunicacion = convocatoriaComDTO.getConvocatoriaActNoteDTO()
                        .getTipoActorDTO().getIdValor();
                tipoPersonaComunicacion = convocatoriaComDTO.getConvocatoriaActNoteDTO()
                        .getTipoPersonaFisJurDTO().getIdValor();

                if (Boolean.TRUE.equals(convocatoriaComDTO.getBlNote())
                        && tipoActorComunicacion.equals(tipoActorExpediente)
                        && tipoPersonaComunicacion.equals(tipoPersonaExpediente)) {
                    // enviamos note para todos los participantes
                    
                    //GIM 27/02/25 Comprobamos que tengamos un actor Note en la tabla de comunicaciones y si no pues madamos el del expediente del interesado como se hacia antes
                    String cdNumIden = "";
                                                      
                    if(expedienteDTO.getRepresentanteLegal() != null &&  (expediente.getMedioComunicacionDTO() != null && expediente.getMedioComunicacionDTO().getIdValor() != null  
                            && expediente.getMedioComunicacionDTO().getNmConstante().equals(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))) {
                        cdNumIden =  expedienteDTO.getRepresentanteLegal().getCdNumIden();                       
                    }else {
//                        if(convocatoriaComDTO.getConvocatoriaActNoteDTO().getIdActor() != null) {
//                            
//                            List<ExpedienteDTO>  listaExpAux = this.expedienteService.buscarParticipantesPorSolicitud(solicitudDTO.getIdSolicitud(), convocatoriaComDTO.getConvocatoriaActNoteDTO().getIdActor());
//                            
//                            if(!listaExpAux.isEmpty()) {
//                                cdNumIden = listaExpAux.get(0).getInteresadoDTO().getCdNumIden();
//                            }else {
//                                cdNumIden =  expedienteDTO.getInteresadoDTO().getCdNumIden();
//                            }                       
//                        }else {
//                            cdNumIden =  expedienteDTO.getInteresadoDTO().getCdNumIden();
//                        }
                        cdNumIden =  expedienteDTO.getInteresadoDTO().getCdNumIden();
                    }
                    
                    

                    String cdNote = this.enviarPorNote(convocatoriaDTO, convocatoriaPlaDTO,
                            expediente, tramite, documentoFirmado,cdNumIden);

                    if (StringUtils.isNotBlank(cdNote)) {
                        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION).equals(idTipoTramite)) {
                            DocumentoPtfr peticion = new DocumentoPtfr();
                            peticion.setIdExpediente(expedienteDTO.getIdExpediente());
                            peticion.setItUsuAltaNote(Constantes.AfirmacionNegacion.S);
                            peticion.setTramite(new CatalogosValores(idTipoTramite));
                            peticion.setCdNote(cdNote);
                            peticion.setCdEstadoNote(Constantes.Note.CD_PD);
                            peticion.setFcEnvioNote(new Date());
                            peticion.setFcAltaFila(new Date());
                            // Usuario de persona física extracomunitario: no tiene NIF/NIE, la
                            // auditoría debe usar una constante en lugar de su identificador.
                            peticion.setCdUsuAlta(solicitudDTO.getInteresadoDTO().getTipoIdentificacionDTO() != null
                                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                            .equals(solicitudDTO.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor())
                                    ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                                    // Salvaguarda (defensa en profundidad): ver comentario anterior.
                                    : Utils.valorAuditoria(solicitudDTO.getInteresadoDTO().getCdNumIden()));
                            this.documentoPtfrDAO.insert(peticion);
                        } else {
                            DocumentoPtfr peticion = this.documentoPtfrDAO.find(idPeticion);
                            peticion.setCdNote(cdNote);
                            peticion.setItUsuAltaNote(Constantes.AfirmacionNegacion.S);
                            peticion.setCdEstadoNote(Constantes.Note.CD_PD);
                            peticion.setFcEnvioNote(new Date());
                            peticion.setFcModifFila(new Date());
                            peticion.setCdUsuModif(Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                            this.documentoPtfrDAO.update(peticion);
                        }

                        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                                .equals(idTipoTramite)) {

                            List<ExpedienteContratoDTO> listExpC = this.expedienteContratoService
                                    .buscarPorExpediente(idExpediente);

                            if (listExpC != null && !listExpC.isEmpty()) {
                                for (ExpedienteContratoDTO expC : listExpC) {
                                    if (expC.getIdEstadoDTO() != null
                                            && expC.getIdEstadoDTO().getIdValor() != null
                                            && expC.getIdEstadoDTO().getIdValor().equals(
                                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.REQUIERE_SUBSANACION))
                                            && expC.getIdTipoContratoDTO() != null
                                            && expC.getIdTipoContratoDTO().getIdValor() != null
                                            && expC.getIdTipoContratoDTO().getIdValor().equals(
                                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_INICIAL))) {
                                        ExpedienteContrato expCont = this.expedienteContratoService
                                                .find(expC.getIdExpedienteContrato());
                                        expCont.setIdEstado(new CatalogosValores(
                                                utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_SUBSANACION)));
                                        this.expedienteContratoDAO.update(expCont);
                                        this.expedienteContratoHistDAO.crearHistorico(expCont,
                                                Constantes.Utiles.COD_USU_BATCH_AUDITORIA);
                                        expediente.setExpedienteContrato(
                                                ExpedienteContratoToDTO.INSTANCE.apply(expCont));
                                    }
                                }
                            }
                        }
                        lstCdNote.add(cdNote);
                    } else {
                        log.info("No se envió notificación NOTE para el expediente "
                                + expediente.getCdExpediente());
                    }

                }
            }

            if (Objects.nonNull(convocatoriaComDTO.getConvocatoriaActCorreoDTO())) {

                tipoActorComunicacion = convocatoriaComDTO.getConvocatoriaActCorreoDTO()
                        .getTipoActorDTO().getIdValor();
                tipoPersonaComunicacion = convocatoriaComDTO.getConvocatoriaActCorreoDTO()
                        .getTipoPersonaFisJurDTO().getIdValor();

                if (Boolean.TRUE.equals(convocatoriaComDTO.getBlCorreo())
                        && tipoActorComunicacion.equals(tipoActorExpediente)
                        && tipoPersonaComunicacion.equals(tipoPersonaExpediente)) {

                    try {
                        
                        //GIM 27/02/25 Comprobamos que tengamos un actor Note en la tabla de comunicaciones y si no pues madamos el del expediente del interesado como se hacia antes
                        String dsCorreo = "";
                                            
                        if(convocatoriaComDTO.getConvocatoriaActCorreoDTO().getIdActor() != null) {
                                                                          
                            List<ExpedienteDTO>  listaExpAux = this.expedienteService.buscarParticipantesPorSolicitud(solicitudDTO.getIdSolicitud(), convocatoriaComDTO.getConvocatoriaActCorreoDTO().getIdActor());
                                             
                            dsCorreo = listaExpAux.get(0).getInteresadoDTO().getDsEmail();
                        }else {
                            dsCorreo =  expedienteDTO.getInteresadoDTO().getDsEmail();
                        }
                        
                        this.mailService.enviarCorreo(dsCorreo, convocatoriaPlaDTO, convocatoriaDTO, expediente);

                        // Registramos el envío por correo
                        List<Integer> lstTramites = new ArrayList<>();
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL));
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL));
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA));
                        lstTramites.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA));
                        
                        if (lstTramites.contains(idTipoTramite)) {
                            DocumentoPtfr peticion = this.documentoPtfrDAO.find(idPeticion);
                            peticion.setItCorreo(null);
                            peticion.setFcEnvioCorreo(new Date());
                        } else {
                            DocumentoPtfr peticion = new DocumentoPtfr();
                            peticion.setIdExpediente(expedienteDTO.getIdExpediente());
                            peticion.setTramite(new CatalogosValores(idTipoTramite));
                            peticion.setItCorreo(Constantes.AfirmacionNegacion.S);
                            peticion.setFcEnvioCorreo(new Date());
                            peticion.setFcAltaFila(new Date());
                            // Usuario de persona física extracomunitario: no tiene NIF/NIE, la
                            // auditoría debe usar una constante en lugar de su identificador.
                            peticion.setCdUsuAlta(solicitudDTO.getInteresadoDTO().getTipoIdentificacionDTO() != null
                                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                            .equals(solicitudDTO.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor())
                                    ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                                    // Salvaguarda (defensa en profundidad): ver comentario anterior.
                                    : Utils.valorAuditoria(solicitudDTO.getInteresadoDTO().getCdNumIden()));
                            this.documentoPtfrDAO.insert(peticion);
                        }

                    } catch (Exception e) {
                        log.error("No se envió la notificación correo para el expediente "
                                + expediente.getCdExpediente(), e.getMessage());
                    }

                }
            }

        }

        return lstCdNote;

    }


    
    
    
    /**
     * Obtiene el registro de comunicaciones a partir de la convocatoria y el
     * tramite
     *
     * @param idConvocatoria
     * @param idTipoTramite
     * @return Dto de ConvocatoriaComDTO
     * @throws ServiceException
     */
    private ConvocatoriaComDTO obtener(Integer idConvocatoria, Integer idTipoTramite)
            throws ServiceException {

        ConvocatoriaComDTO convocatoriaComDTO = new ConvocatoriaComDTO();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter("tramite.idValor", idTipoTramite, Operador.EQUALS));

        List<ConvocatoriaComDTO> lstConvocatoriaComDTO = this.buscar(0, -1, filtros, null);

        if (lstConvocatoriaComDTO != null && !lstConvocatoriaComDTO.isEmpty()) {
            convocatoriaComDTO = lstConvocatoriaComDTO.get(0);
        }

        return convocatoriaComDTO;
    }

    /**
     *
     * @param idConvocatoria
     * @param idTipoTramite
     * @return
     * @throws ServiceException
     */
    @Override
    public ConvocatoriaPlaDTO obtenerConvocatoriaPla(Integer idConvocatoria, Integer idTipoTramite)
            throws ServiceException {

        ConvocatoriaPlaDTO convocatoriaPlaDTO = new ConvocatoriaPlaDTO();

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTipoTramite, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaPla> lstConvocatoriaPla = new ArrayList<>(
                this.convocatoriaPlaDAO.find(filtros, null));

        if (!lstConvocatoriaPla.isEmpty()) {
            convocatoriaPlaDTO = ConvocatoriaPlaToDTO.INSTANCE.apply(lstConvocatoriaPla.get(0));

            if (StringUtils.isBlank(convocatoriaPlaDTO.getDsAsunto())) {
                convocatoriaPlaDTO.setDsAsunto(convocatoriaPlaDTO.getTramiteDTO().getDsValor());
            }
        }

        return convocatoriaPlaDTO;
    }

    /**
     *
     * @param convocatoriaDTO
     * @param convocatoriaPlaDTO
     * @param expedienteDTO
     * @param tramite
     * @param documentoFirmado
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    @SuppressWarnings("deprecation")
    private String enviarPorNote(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO, Tramite tramite,
            InputStream documentoFirmado, String cdNumIden) throws ServiceException {
        NoteEnviarNotificacionDTO inputNotifNoteDTO = new NoteEnviarNotificacionDTO();

        if (Objects.nonNull(documentoFirmado)) {
            inputNotifNoteDTO.setAdjunto(documentoFirmado);
        } else {

            // Obtenemos los datos de la consejeria
            String strConsejeria = this.unidadAdminService.obtenerConsejeria();

            byte[] informeTramitePdf = tramite.formularioPdf(convocatoriaDTO, convocatoriaPlaDTO,
                    expedienteDTO, strConsejeria);

            if (Objects.nonNull(informeTramitePdf)) {
                inputNotifNoteDTO.setAdjunto(new ByteArrayInputStream(informeTramitePdf));
            } else {
                log.error("No se generó el trámite PDF para enviar por NOTE");
            }
        }

        StringBuilder descripcion = new StringBuilder();
      //  try {

//            for (String parrafo : convocatoriaPlaDTO.getDsDescripcion().split("<br>")) {
//                List<Element> elementos = HTMLWorker
//                        .parseToList(new StringReader(parrafo.replace("</a>", "")), null);
//
//                for (Element element : elementos) {
//                    String texto = "";
//                    for (String cadena : element.toString().split("&nbsp;")) {
//                        texto += cadena.substring(1, cadena.length() - 1) + "\n";
//                    }
//                    descripcion.append(texto);
//
//                }
//            }
            
            String descripcionSup = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcion())
                    ? convocatoriaPlaDTO.getDsDescripcion()
                    : "";

            String descripcionInf = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcionInf())
                    ? convocatoriaPlaDTO.getDsDescripcionInf()
                    : "";

            // 2. Unimos los textos con un salto de línea en HTML
            StringBuilder textoUnificado = new StringBuilder();
            textoUnificado.append(descripcionSup);

            if (StringUtils.isNotBlank(descripcionSup) && StringUtils.isNotBlank(descripcionInf)) {
                textoUnificado.append("<br/>"); // Salto de línea único sin huecos gigantes
            }

            descripcion = convertirHtmlATextoPlano(textoUnificado.append(descripcionInf));
            

            // Agregamos informacion de los documentos observados
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)
//                    .equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
//
//                descripcion.append("\n");
//                descripcion.append("\n");
//
//                int numOrden = 1;
//                Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
//                List<ExpedienteDocDTO> listExpDoc = expedienteDTO.getLstExpedienteDocDTO().stream()
//                        .filter(expeDoc -> idReqSubNot.equals(expeDoc.getEstadoDTO().getIdValor())
//                                && Objects.isNull(expeDoc.getFcBajaFila())
//                                // && Objects.nonNull(expeDoc.getCdFica()) // MD-2116 quitamos la validación FICA para los casos de req sub requisitos
//                                )
//                        .collect(Collectors.toList());
//                for (ExpedienteDocDTO expedienteDocDTO : listExpDoc) {
//
//                    StringBuilder strDescripcion = new StringBuilder();
//                 // MD-2116. Configuramos la causa según sea el tipo de trámite subsanción admin o subsanación req
//                    String causa = "";
//                    if (expedienteDocDTO.getTramiteDTO().getIdValor()
//                            .equals(utilConstantes.getGlobalCatalogosValores()
//                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO))) {
//                        for (ExpedienteReqDTO expedienteReqDTO : expedienteDTO
//                                .getLstExpedienteReqDTO()) {
//                            if (expedienteReqDTO.getIdExpedienteDoc() != null
//                                    && expedienteReqDTO.getIdExpedienteDoc()
//                                            .equals(expedienteDocDTO.getIdExpedienteDoc())) {
//                                causa = expedienteReqDTO.getTlObservaciones();
//                            }
//                        }
//                    } else {
//                        causa = Constantes.CausaDenegacion.CODIGO_DS_OTROS.equals(
//                                expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO().getDsValor())
//                                        ? expedienteDocDTO.getTlCausaDescripcion()
//                                        : expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO()
//                                                .getDsValor();
//                    }
//                    String nombreTipoDoc = Objects.isNull(expedienteDocDTO.getTipoDocumentoDTO())
//                            || StringUtils
//                                    .isEmpty(expedienteDocDTO.getTipoDocumentoDTO().getDsValor())
//                                            ? Constantes.CausaDenegacion.CODIGO_DS_OTROS
//                                            : expedienteDocDTO.getTipoDocumentoDTO().getDsValor();
//
//                    strDescripcion.append(numOrden).append(") ");
//                    strDescripcion.append(nombreTipoDoc);
//                    strDescripcion.append(" - ");
//                    strDescripcion.append(expedienteDocDTO.getNombreFicheroFica());
//                    strDescripcion.append(" - ");
//                    strDescripcion.append(causa);
//
//                    descripcion.append(strDescripcion.toString());
//                    descripcion.append("\n");
//
//                    numOrden++;
//                }
//
//            }

            // Agregamos informacion de los requisitos observados
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO)
                    .equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION)
                            .equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {

                descripcion.append("\n");
                descripcion.append("\n");

                int numOrden = 1;
                List<ExpedienteReqDTO> listExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null
                                && Constantes.AfirmacionNegacion.N.equals(x.getItCumple()))
                        .collect(Collectors.toList());

                for (ExpedienteReqDTO expedienteReqDTO : listExpReq) {

                    StringBuilder strDescripcion = new StringBuilder();
                    strDescripcion.append(numOrden).append(") ");
                    strDescripcion.append(expedienteReqDTO.getRequisitoDTO().getDsValor());
                    if (StringUtils.isNotBlank(expedienteReqDTO.getTlObservaciones())) {
                        strDescripcion.append(" - ").append(expedienteReqDTO.getTlObservaciones());
                    }

                    descripcion.append(strDescripcion.toString());
                    descripcion.append("\n");

                    numOrden++;
                }

            }

            // Agregamos informacion de los documentos observados de subsanacion
            // contrato
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL)
                    .equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_SUSTITUCION)
                            .equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {

                descripcion.append("\n");
                descripcion.append("\n");

                int numOrden = 1;
                Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
                List<ExpedienteDocDTO> listExpDoc = expedienteDTO.getLstExpedienteDocDTO().stream()
                        .filter(expeDoc -> idReqSubNot.equals(expeDoc.getEstadoDTO().getIdValor())
                                && Objects.isNull(expeDoc.getFcBajaFila())
                                && Objects.nonNull(expeDoc.getCdFica()))
                        .collect(Collectors.toList());
                for (ExpedienteDocDTO expedienteDocDTO : listExpDoc) {

                    StringBuilder strDescripcion = new StringBuilder();

                    String causa = Constantes.CausaDenegacion.CODIGO_DS_OTROS
                            .equals(expedienteDocDTO.getConvocatoriaCauContDTO().getCausaContDTO()
                                    .getDsValor()) ? expedienteDocDTO.getTlCausaDescripcion()
                                            : expedienteDocDTO.getConvocatoriaCauContDTO()
                                                    .getCausaContDTO().getDsValor();

                    String nombreTipoDoc = Objects.isNull(expedienteDocDTO.getTipoDocumentoDTO())
                            || StringUtils
                                    .isEmpty(expedienteDocDTO.getTipoDocumentoDTO().getDsValor())
                                            ? Constantes.CausaDenegacion.CODIGO_DS_OTROS
                                            : expedienteDocDTO.getTipoDocumentoDTO().getDsValor();

                    strDescripcion.append(numOrden).append(") ");
                    strDescripcion.append(nombreTipoDoc);
                    strDescripcion.append(" - ");
                    strDescripcion.append(expedienteDocDTO.getNombreFicheroFica());
                    strDescripcion.append(" - ");
                    strDescripcion.append(causa);

                    descripcion.append(strDescripcion.toString());
                    descripcion.append("\n");

                    numOrden++;
                }

            }

//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }

        if (convocatoriaDTO != null && convocatoriaDTO.getIdAreaAdministrativa() != null
                && convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad() != null) {
            inputNotifNoteDTO.setUniAdmin(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad());
            
        }else if(convocatoriaDTO != null && convocatoriaDTO.getIdSubdireccion()!= null
                && convocatoriaDTO.getIdSubdireccion().getDsUnidad() != null){
            //GIM MD-1885 21/08/2025 Vamos a que si no tiene unidad adminisdtrativa le ponemos como unidad la sub direccion y si no tiene la subdireccion le ponemos la direccion.
            inputNotifNoteDTO.setUniAdmin(convocatoriaDTO.getIdSubdireccion().getDsUnidad());
        }else if(convocatoriaDTO != null && convocatoriaDTO.getIdDireccion()!= null
                && convocatoriaDTO.getIdDireccion().getDsUnidad() != null) {
            inputNotifNoteDTO.setUniAdmin(convocatoriaDTO.getIdDireccion().getDsUnidad());
        }

        inputNotifNoteDTO.setAsunto(convocatoriaPlaDTO.getDsAsunto());
        inputNotifNoteDTO.setCdNumIden(cdNumIden);
        inputNotifNoteDTO.setCuerpo(descripcion.toString());

        inputNotifNoteDTO.setNombreAdjunto(convocatoriaPlaDTO.getTramiteDTO().getDsValor());

        return this.noteAdmeService.enviarNotificacion(inputNotifNoteDTO);
    }
    
    private StringBuilder convertirHtmlATextoPlano(StringBuilder htmlTexto) {
        if (htmlTexto == null || htmlTexto.length() == 0) {
            return new StringBuilder();
        }

        String texto = htmlTexto.toString();

        // 1. NEGRITAS (<b>, <strong>): Mantiene el texto limpio
        texto = texto.replaceAll("(?i)<(strong|b)[^>]*>(.*?)</\\1>", "$2");

        // 2. CURSIVAS (<em>, <i>): Mantiene el texto limpio
        texto = texto.replaceAll("(?i)<(em|i)[^>]*>(.*?)</\\1>", "$2");

        // 3. SUBRAYADOS (<u>): Mantiene el texto limpio
        texto = texto.replaceAll("(?i)<u[^>]*>(.*?)</u>", "$1");

        // 4. ELEMENTOS DE LISTA (<li>) -> • texto
        texto = texto.replaceAll("(?i)<li[^>]*>(.*?)</li>", "• $1\n");

        // 5. ENCABEZADOS (<h1> ... <h6>)
        texto = texto.replaceAll("(?i)<h[1-6][^>]*>(.*?)</h[1-6]>", "\n\n$1\n\n");

        // 6. ETIQUETAS DE BLOQUE Y SALTOS (p, div, br, tr)
        texto = texto.replaceAll("(?i)<br\\s*/?>", "\n");
        texto = texto.replaceAll("(?i)</(p|div|tr)>", "\n\n");
        texto = texto.replaceAll("(?i)<(p|div|tr)[^>]*>", "\n");

        // 7. ELIMINAR CUALQUIER OTRA ETIQUETA HTML RESTANTE (<span...>, etc.)
        texto = texto.replaceAll("<[^>]*>", "");

        // 8. DECODIFICAR ENTIDADES HTML (&nbsp;, &aacute;, &quot;, etc.)
        texto = StringEscapeUtils.unescapeHtml4(texto);

        // 9. LIMPIEZA DE COMILLAS DUPLICADAS: Convierte "" o "" en una sola comilla "
        texto = texto.replaceAll("\"\"", "\"");
        texto = texto.replaceAll("''", "'");

        // 10. NORMALIZACIÓN Y LIMPIEZA DE ESPACIOS Y SALTOS DE LÍNEA
        texto = texto.replaceAll("[ \\t]+", " ");
        texto = texto.replaceAll("(\r?\n\\s*){3,}", "\n\n");

        return new StringBuilder(texto.trim());
    }
    
    

    /**
     *
     * @param itAct
     * @param itemCom
     * @return
     * @throws ServiceException 
     */
    public ConvocatoriaAct asignacionActorNote(
            Iterator<ConvocatoriaActDTO> itAct/* , ConvocatoriaCom itemCom */) {
        ConvocatoriaAct actorRecogido = new ConvocatoriaAct();
        while (itAct.hasNext()) {
            ConvocatoriaActDTO valorActor = itAct.next();
            if (/* itemCom.getConvocatoriaActNote() == null && */valorActor.getTipoActorDTO()
                    .getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.SOLICITANTE))) {
                actorRecogido = ConvocatoriaActToEntity.INSTANCE.apply(valorActor);
                return actorRecogido;
            }
        }
        return actorRecogido;
    }

    public ConvocatoriaAct asignacionActorCorreo(
            Iterator<ConvocatoriaActDTO> itAct/* , ConvocatoriaCom itemCom */) {
        ConvocatoriaAct actorRecogido = new ConvocatoriaAct();
        while (itAct.hasNext()) {
            ConvocatoriaActDTO valorActor = itAct.next();
            if (/* itemCom.getConvocatoriaActCorreo() == null && */valorActor.getTipoActorDTO()
                    .getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE))) {
                actorRecogido = ConvocatoriaActToEntity.INSTANCE.apply(valorActor);
                return actorRecogido;
            }
        }
        return actorRecogido;
    }

    /**
    *
    */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaComDTO guardarComunicacion(ConvocatoriaComDTO convocatoriaComDTO)
            throws ServiceException {

        return ConvocatoriaComToDTO.INSTANCE
                .apply(this.insert(ConvocatoriaComToEntity.INSTANCE.apply(convocatoriaComDTO)));
    }
    
    /**
    *
    */
    @Override
    public List<ConvocatoriaComDTO> buscarConvocatoriaComPorIdConvocatoriaIdConvocatoriaActNote(Integer idConvocatoria, Integer idActor) throws ServiceException{
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_NOTE_ID_ACTOR, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ConvocatoriaComToDTO.INSTANCE));
    }
    
    /**
    *
    */
    @Override
    public List<ConvocatoriaComDTO> buscarConvocatoriaComPorIdConvocatoriaIdConvocatoriaActCorreo(Integer idConvocatoria, Integer idActor) throws ServiceException{
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA, idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_CORREO_ID_ACTOR, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ConvocatoriaComToDTO.INSTANCE));
    }
}
