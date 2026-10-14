package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.madrid.saii.lib.dao.BloqueDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.DocumentoPtfrDAO;
import org.madrid.saii.lib.dao.EnvioNotificacionesPortafirmasDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.InteresadoDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudDatosDTO;
import org.madrid.saii.lib.dto.converters.EnvioNotificacionesPortafirmasToDTO;
import org.madrid.saii.lib.dto.converters.EnvioNotificacionesPortafirmasToEntity;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.services.adme.PtfrAdmeService;
import org.madrid.saii.lib.util.Constantes;
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
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Expediente
 *
 * @author nttdata
 */
@Slf4j
@Service("EnvioNotificacionesPortafirmasService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EnvioNotificacionesPortafirmasServiceImpl extends BaseServiceImpl<EnvioNotificacionesPortaFirma, Integer, EnvioNotificacionesPortafirmasDAO>
        implements EnvioNotificacionesPortafirmasService {

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
    
    /** Servicio Convocatoria */
    @Autowired
    @Qualifier("convocatoriaService")
    @Setter
    private ConvocatoriaService convocatoriaService;

    /** Servicio Expediente */
    @Autowired
    @Qualifier("expedienteService")
    @Setter
    private ExpedienteService expedienteService;
    
    /** Servicio Convocatoria Pla */
    @Autowired
    @Qualifier("convocatoriaPlaService")
    @Setter
    private ConvocatoriaPlaService convocatoriaPlaService;
    

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
    

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteDAO
     */
    @Autowired
    public EnvioNotificacionesPortafirmasServiceImpl(@Qualifier("envioNotificacionesPortafirmasDAO") EnvioNotificacionesPortafirmasDAO envioNotificacionesPortafirmasDAO) {
        super();
        this.setDao(envioNotificacionesPortafirmasDAO);
    }

    /**
     * Constructor Vacio
     */
    public EnvioNotificacionesPortafirmasServiceImpl() {
        super();
    }


//    /**
//     *
//     */
//    @Override
//    @Transactional(readOnly = false, rollbackFor = ServiceException.class, propagation = Propagation.REQUIRES_NEW)
//    public Boolean guardarEnvio(ConvocatoriaDTO convocatoria, ExpedienteDTO expediente, Integer idExpediente, Integer idtramite, byte[] documentoAdjunto)
//            throws ServiceException {
//        EnvioNotificacionesPortaFirma envio = new EnvioNotificacionesPortaFirma();
//        Boolean duplicado = false;
//        Boolean enviado = false;
//        try {
//   
//            List<AtlasFilter> filtrosEntrada = new ArrayList<>();
//
//            Integer idConvocatoria = convocatoria.getIdConvocatoria();
//
//
//            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
//           
//            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idtramite, Operador.EQUALS));
//
//            List<ConvocatoriaPlaDTO> lstItem = this.convocatoriaPlaService.buscar(0, -1, filtrosEntrada,
//                    null);
//
//            if (lstItem.isEmpty()) {
//                throw new ServiceException("No hay documentos para el tramite en el Portafirmas.");
//            }
//
//            ConvocatoriaPlaDTO convocatoriaPla = lstItem.get(0);
//
//            PtfrAltaSolicitudDatosDTO datosEntrada = new PtfrAltaSolicitudDatosDTO();
//            if (Objects.nonNull(convocatoriaPla.getLstConvocatoriaPlaVerDTO())) {
//                convocatoriaPla.getLstConvocatoriaPlaVerDTO().forEach(
//                        e -> datosEntrada.getListaCodigoVerificadores().add(e.getCdVerificador()));
//            }
//
//            if (Objects.nonNull(convocatoriaPla.getLstConvocatoriaPlaFirDTO())) {
//                convocatoriaPla.getLstConvocatoriaPlaFirDTO()
//                        .forEach(e -> datosEntrada.getListaCodigoFirmantes().add(e.getCdFirmante()));
//            }
//            
//            
//            Boolean tieneVerificador = Boolean.FALSE
//                    .equals(datosEntrada.getListaCodigoVerificadores().isEmpty());
//            
//            if (Boolean.TRUE.equals(tieneVerificador)) {
//                envio.setDsVerificador(datosEntrada.getListaCodigoVerificadores().get(0));
//            }
//            
//            envio.setNmEstado(0);
//            envio.setDsTramite(idtramite);
//            envio.setIdConvocatoria(idConvocatoria);
//            envio.setCdNumeroExpediente(idExpediente);
//            envio.setFcEnvioCola(new Date());
//            envio.setNmEnvio("N");
//            
//            if(documentoAdjunto != null) {
//                envio.setDocumentoAdjunto(documentoAdjunto);
//            }
//            
//            String destinatario = expediente.getInteresadoDTO().getDsNombre() +" "+expediente.getInteresadoDTO().getDsApellido1() ;
//            
//            if(expediente.getInteresadoDTO().getDsApellido2() != null) {
//                destinatario = destinatario +" " +expediente.getInteresadoDTO().getDsApellido2();
//            }
//            
//            envio.setDsDestinatario(destinatario);
//            envio.setDsFirmante(datosEntrada.getListaCodigoFirmantes().get(0));
//
//            
//            //cargar el nuevo valor del texto del tramite
//            List<CatalogosValoresDTO> lstTramites = this.catalogosValoresService
//                    .buscarPorCatalogo(Constantes.Cata.TRAMITES_GESTOR);
//            
//            for (CatalogosValoresDTO tramite : lstTramites) {
//                if(tramite.getIdValor().equals(idtramite)) {
//                    envio.setNmTramite(tramite.getDsValor());
//                }
//            }
//            
//            
//            List<EnvioNotificacionPortafirmasDTO> listaEnviosExistentes = null;
//
//            listaEnviosExistentes = new ArrayList<>(Lists.transform(this.findAll(),
//                    EnvioNotificacionesPortafirmasToDTO.INSTANCE));
//            
//           
//            
//           for (EnvioNotificacionPortafirmasDTO envioexistente : listaEnviosExistentes) {
//               if(envioexistente.getCdNumeroExpediente().equals(idExpediente)
//                           && envioexistente.getIdConvocatoria().equals(idConvocatoria) 
//                               && envioexistente.getDsTramite().equals(idtramite) ) {
//                   duplicado = true;
//               }
//              }
//            
//            if(duplicado.equals(Boolean.FALSE)){
//                this.insert(envio);
//                enviado = true;
//            }
//
//        }catch (Exception e) {
//            log.error(e.getMessage());
//            return enviado;
//        }
//
//        return enviado;
//    }


    /**
    *
    */
   @Override
   public List<EnvioNotificacionPortafirmasDTO> buscarEnviosNotificaciones(final int index, final int pageSize,
           final List<AtlasFilter> filtros, final List<AtlasSort> orden,
           final Integer idConvocatoria, Integer estado)
           throws ServiceException {

       List<EnvioNotificacionPortafirmasDTO> lstSolicitudes = null;

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
               idConvocatoria, Operador.EQUALS));
       
       filtros.add(new AtlasFilter(Constantes.FILTRO_ENVIOS_ESTADO, estado, Operador.EQUALS));
       
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       
       lstSolicitudes = new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
               EnvioNotificacionesPortafirmasToDTO.INSTANCE));

       return lstSolicitudes;
   }

   /**
   *
   */
  @Override
  @Transactional(readOnly = false)
  public EnvioNotificacionPortafirmasDTO guardar(EnvioNotificacionPortafirmasDTO expedienteReqDTO) throws ServiceException {
      return EnvioNotificacionesPortafirmasToDTO.INSTANCE
              .apply(this.insertOrUpdate( EnvioNotificacionesPortafirmasToEntity.INSTANCE.apply(expedienteReqDTO)));
  }

   

}
