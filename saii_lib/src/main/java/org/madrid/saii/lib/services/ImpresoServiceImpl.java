package org.madrid.saii.lib.services;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaActDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ImpresoDAO;
import org.madrid.saii.lib.dao.ListadoColumnaDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogoDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.EntidadDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.SolicitudDTO;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;
import org.madrid.saii.lib.dto.converters.BloqueToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.dto.converters.ImpresoToDTO;
import org.madrid.saii.lib.dto.converters.ImpresoToEntity;
import org.madrid.saii.lib.dto.converters.InteresadoToDTO;
import org.madrid.saii.lib.services.adme.NoteAdmeService;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Constantes.TipoPersonaSolicitante;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.pdf.HeaderFooterPdfEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.tool.xml.XMLWorkerHelper;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Impreso
 *
 * @author everis
 */
@Service("impresoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ImpresoServiceImpl extends BaseServiceImpl<Impreso, Integer, ImpresoDAO>
        implements ImpresoService {

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /**
     * Servicio UnidadAdminService
     */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    /**
     * Servicio SgpOrga
     */
    @Autowired
    @Qualifier("imagenService")
    private ImagenService imagenService;
   
    /**
     * DAO de Convocatoria
     */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /**
     * DAO de ConvocatoriaAct
     */
    @Autowired
    @Qualifier("convocatoriaActDAO")
    private ConvocatoriaActDAO convocatoriaActDAO;

    @Autowired
    @Qualifier("listadoColumnaDAO")
    private ListadoColumnaDAO listadoColumnaDAO;
    
    @Autowired
    @Qualifier("bloqueComponenteService")
    private BloqueComponenteService bloqueComponenteService;
    
    @Autowired
    @Qualifier("bloqueService")
    private BloqueService bloqueService;

    @Autowired
    @Qualifier("sucaMunicipioService")
    private SucaMunicipioService sucaMunicipioService;

    /** The suca provincia service. */
    @Autowired
    @Qualifier("sucaProvinciaService")
    private SucaProvinciaService sucaProvinciaService;
    
    /** DAO de Solicitud */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    /** The representante jur service. */
    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService representanteJurService;
    
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    @Autowired
    @Qualifier("noteAdmeService")
    private NoteAdmeService noteAdmeService;
    
    /**
     * Servicio informe Helper PDF
     */
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    /** Repositorio ConvocatoriaSubmodService */
    @Autowired
    @Qualifier("convocatoriaSubmodService")
    private ConvocatoriaSubmodService convocatoriaSubmodService;

    /** Repositorio ConvocatoriaAreaService */
    @Autowired
    @Qualifier("convocatoriaAreaService")
    private ConvocatoriaAreaService convocatoriaAreaService;
    
    /**
     * Servicio de ConfiguracionService
     */
    @Autowired
    @Qualifier("configuracionService")
    private ConfiguracionService configuracionService;
    
    /**
     * Servicio de Entidad
     */
    @Autowired
    @Qualifier("entidadService")
    private EntidadService entidadService;
    
    /** Espacio entre bloques */
    protected static final Paragraph ESPACIO_CABECERA = new Paragraph("\n\n\n\n\n");

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param impresoDAO
     */
    @Autowired
    public ImpresoServiceImpl(@Qualifier("impresoDAO") ImpresoDAO impresoDAO) {
        super();
        this.setDao(impresoDAO);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = true, noRollbackFor = { RuntimeException.class })  //MD-2433 se excluyen las excepciones que lance para no hacer rollback ya que solo descarga
    public byte[] crearInforme(final ImpresoDTO impresoDTO, final Map<String, String> mapaDatos,  ExpedienteDTO expedienteDTO)
            throws ServiceException {

        Boolean siFondosEuropeos = Boolean.FALSE;
        Map<String, byte[]> lstLogo = new HashMap<>();
        byte[] logoOrganismo = null;
        FicaFichero ficheroRecuperar = null;
        byte[] bytesSalida = null;
        int i = 1;

        try {
            if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdConvocatoria())) {

                ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                        .apply(this.convocatoriaDAO.find(impresoDTO.getIdConvocatoria()));
                
                if(expedienteDTO == null){
                    expedienteDTO = new ExpedienteDTO();
                    expedienteDTO.setImpresoDTO(impresoDTO);
                    expedienteDTO.getConvocatoriaActDTO().setEsActorCoordinador(
                            Constantes.TipoActor.COORDINADOR.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
                    expedienteDTO.getConvocatoriaActDTO().setEsActorParticipante(
                            Constantes.TipoActor.PARTICIPANTE.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
                    expedienteDTO.getConvocatoriaActDTO().setEsActorSolicitante(
                            Constantes.TipoActor.SOLICITANTE.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
                }

                if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItFondosEuropa())) {
                    siFondosEuropeos = Boolean.TRUE;

                    // obtenemos la imagen fica de union europea
                    List<ImagenDTO> lstLogosEuropeos = this.imagenService
                            .buscarLogosEuropeos(impresoDTO.getIdConvocatoria());
                    for (ImagenDTO elemento : lstLogosEuropeos) {
                        if (Objects.nonNull(elemento.getCdFica())
                            && StringUtils.isNotBlank(elemento.getCdFica())) {
                    
                            ficheroRecuperar = this.ficaService
                                    .recuperar(Integer.valueOf(elemento.getCdFica()));

                            if (ficheroRecuperar != null) {
                                lstLogo.put("ImgLogo" + i, ficheroRecuperar.getDatosFichero());
                                i++;
                            }
                        }
                    }

                }

                if (Objects.nonNull(impresoDTO.getImagenLogoDTO().getCdFica())
                        && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica())) {
                    // obtenemos la imagen fica del organismo
                    try {
                        ficheroRecuperar = this.ficaService
                                .recuperar(Integer.valueOf(impresoDTO.getImagenLogoDTO().getCdFica()));
                        logoOrganismo = ficheroRecuperar.getDatosFichero();
                    } catch (Exception e) {
                        logoOrganismo = null;
                    }
                }

                String strConsejeria = this.unidadAdminService.obtenerConsejeria();

                String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

                Document document = new Document(PageSize.A4, 36, 36, 102, 36);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                PdfWriter writer = PdfWriter.getInstance(document, baos);
                writer.createXmpMetadata();
                writer.setTagged();
                
                if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                        && impresoDTO.getTipoDTO().getIdValor().equals(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO))) {
                    //Si se trata formulario complementario no se añade registro de entrada
                    writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                            strConsejeria, strDireccion, Boolean.FALSE, lstLogo, logoOrganismo));
                }else {
                    writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                            strConsejeria, strDireccion, Boolean.TRUE, lstLogo, logoOrganismo)); //DEVOLVER TRUE
                }
                document.open();

                Boolean agregarEspacioLogos = !lstLogo.isEmpty() || Objects.nonNull(logoOrganismo);
                
                String codigoImpreso= "Código: ";
                /*if(impresoDTO != null && impresoDTO.getCdIdentificacion() != null &&impresoDTO.getCdIdentificacion().indexOf("_") != -1 ) {
                    codigoImpreso+= impresoDTO.getCdIdentificacion().substring(0,impresoDTO.getCdIdentificacion().indexOf("_"));
                }else if(impresoDTO != null && impresoDTO.getCdIdentificacion() != null  ) {
                    codigoImpreso+= impresoDTO.getCdIdentificacion();
                }*/
                
                if(impresoDTO != null && impresoDTO.getCdIdentificacion() != null  ) {
                    codigoImpreso+= impresoDTO.getCdIdentificacion();
                }
                String desImpreso="";
                if(impresoDTO != null && impresoDTO.getDsDescripcion() != null  ) {
                    desImpreso+= impresoDTO.getDsDescripcion();
                }
                
                informeHelperPdfService.agregarTitulo(document, codigoImpreso,
                        convocatoriaDTO.getCdTitulo(),desImpreso,expedienteDTO.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);

                List<BloqueDTO> lstBloques = impresoDTO.getLstBloqueDTO();
                List<BloqueDTO> lstBloquesResp  = new ArrayList<>();
                
                for (BloqueDTO bloqueDTOResp : lstBloques) {
                    if (bloqueDTOResp.getFcBajaFila() == null) {
                        lstBloquesResp.add(bloqueDTOResp);
                    }
                }
                
                lstBloquesResp.sort((b1, b2) -> {
                int cmp = Integer.compare(b1.getNmOrden(), b2.getNmOrden());
                if (cmp != 0) {
                    return cmp;
                }

                // Si numOrden es igual y ambos tienen idBloquePadre != null, ordenamos por subOrden
                boolean tienePadre1 = b1.getIdBloquePadre() != null;
                boolean tienePadre2 = b2.getIdBloquePadre() != null;

                if (tienePadre1 && tienePadre2) {
                    return Integer.compare(b1.getSubNmOrden(), b2.getSubNmOrden());
                }

                    // Si solo uno tiene padre, el que no tiene padre va primero
                if (tienePadre1 != tienePadre2) {
                    return tienePadre1 ? 1 : -1;
                }

               // Si ninguno tiene padre o ambos, pero no se cumple la condición, se consideran iguales
                    return 0;
                });
              
                Boolean esPF= Boolean.TRUE;
                if(impresoDTO.getConvocatoriaActDTO() != null && impresoDTO.getConvocatoriaActDTO().getIdentificacionDTO() != null) {
                    esPF = Constantes.Identificacion.FISICA.equals(impresoDTO.getConvocatoriaActDTO().getIdentificacionDTO().getNmConstante());
                }

                //  this.agregarNumeroExpediente(expedienteDTO, document);

                String tlDescripcion = (expedienteDTO.getConvocatoriaActDTO().getTipoPersonaFisJurDTO().getDsValor() == null ? "[Descripción del actor]" : expedienteDTO.getConvocatoriaActDTO().getTipoPersonaFisJurDTO().getDsValor());
                /*
                List<BloqueDTO> bloquePesonaSol= lstBloquesResp.stream().filter(x-> x.getEsTipoPersonaSolicitante().equals(Boolean.TRUE)).collect(Collectors.toList());
                if ( !bloquePesonaSol.isEmpty() ){
                   informeHelperPdfService.agregarBloqueTipoNotificacion(writer, document, bloquePesonaSol.get(0));
                }
                */

                if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                        && ( impresoDTO.getTipoDTO().getIdValor().equals(
                                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD)) )) {
                    informeHelperPdfService.agregarBloquePersonaNotificacion(writer, document, "PERSONA", expedienteDTO.getTipoPersonaSolicitudDTO().getIdValor(), false);
                    if (expedienteDTO.getTipoPersonaSolicitudDTO().getIdValor() != null
                            && expedienteDTO.getTipoPersonaSolicitudDTO().getIdValor()
                                    .equals(utilConstantes.getGlobalCatalogosValores().get(
                                            TipoPersonaSolicitante.TIPO_PERSONA_SOLICITANTE_AUTORIZADO))
                            && expedienteDTO.getAutorizado() != null && expedienteDTO.getAutorizado().getIdInteresado() != null) {
                        
                        informeHelperPdfService.agregarDatosAutorizado(writer, document,expedienteDTO); 
                        Boolean subido= Boolean.FALSE;
                        if(expedienteDTO.getLstExpedienteDocDTO() != null && !expedienteDTO.getLstExpedienteDocDTO().isEmpty()) {
                            Optional<ExpedienteDocDTO> expedienteDocDTO = expedienteDTO
                                    .getLstExpedienteDocDTO().stream()
                                    .filter(x ->  x.getTipoDocumentoDTO().getNmConstante() != null && 
                                    x.getTipoDocumentoDTO().getNmConstante()
                                            .equals(Constantes.TipoDocumento.DOCUMENTO_AUTORIZADO))
                                    .findFirst();
                            if(expedienteDocDTO.isPresent() && expedienteDocDTO.get().getEstadoDTO() != null
                                    && expedienteDocDTO.get().getEstadoDTO().getIdValor() != null
                                    && !expedienteDocDTO.get().getEstadoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.SIN_SUBIR))) {
                                subido= Boolean.TRUE;
                                
                            }
                           
                        }
                        
                        informeHelperPdfService.agregarDocumentoJustifAutorizado(document,
                                "Documentos que se aportan junto a la solicitud", subido);
                        
                    }
                }else if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                        && (impresoDTO.getTipoDTO().getIdValor().equals(
                                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA)))) {
                    String selectValue ="";
                    if(expedienteDTO.getItAceptaAyuda() != null && expedienteDTO.getItAceptaAyuda().equalsIgnoreCase("S")) {
                        selectValue="Acepta la ayuda concedida.";
                    }else if(expedienteDTO.getItAceptaAyuda() != null && expedienteDTO.getItAceptaAyuda().equalsIgnoreCase("N")){
                        selectValue="No acepta la ayuda concedida.";
                    }
                    informeHelperPdfService.agregarBloqueAceptarRecharAyuda(writer, document,
                            selectValue);
                }
                
                
                // Si es física
                if (Boolean.TRUE.equals(esPF)) {
                    
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && impresoDTO.getTipoDTO().getIdValor().equals(
                                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO))) {
                      //Agregamos los datos de la entidad donde el participante propone su expediente.
                        ConvocatoriaActDTO convocatoriaActDTO = null;
                        if(expedienteDTO.getIdExpedientePadre() != null) {
                            Expediente expPadre = this.expedienteDAO.find(expedienteDTO.getIdExpedientePadre());
                            if (expPadre != null && expPadre.getConvocatoriaAct() != null && expPadre.getConvocatoriaAct().getIdActor() != null ) {
                                convocatoriaActDTO = ConvocatoriaActToDTO.INSTANCE.apply(this.convocatoriaActDAO.find(expPadre.getConvocatoriaAct().getIdActor()));
                               
                            }
                            
                            Solicitud solicitud= this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
                            EntidadDTO entidadSolicitante= this.entidadService.buscarEntidadInteresado(solicitud.getInteresado().getIdInteresado());
                            if(entidadSolicitante == null || entidadSolicitante.getInteresadoDTO() == null || entidadSolicitante.getInteresadoDTO().getIdInteresado() == null) {
                                entidadSolicitante= new EntidadDTO();
                                entidadSolicitante.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(solicitud.getInteresado()));
                            }
                            expedienteDTO.setEntidadDTO(entidadSolicitante);
                          
                        }
                        informeHelperPdfService.agregarDatosEntidadSolicitante(document,expedienteDTO,convocatoriaActDTO);
                    }
                    
                    
                    //ConvocatoriaAct actor = this.convocatoriaActDAO.find(impresoDTO.getConvocatoriaActDTO().getIdActor());
                    
                    String titulo1="Datos de la persona solicitante";
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && ( impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)) )) {
                        titulo1="Datos de la persona participante";
                    
                    }
                    this.agregarBloqueDatosInteresado(document, expedienteDTO, titulo1);

                    
                    //Se comenta este bloque en la tarea MD-2906 [ADM] 
                    /*String titulo2="Datos del representante legal de la persona solicitante";
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && ( impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)) )) {
                        titulo2="Datos del representante legal de la persona participante";
                    
                    }
                    this.agregarBloqueDatosRepresentanteSolicitante(document, expedienteDTO,titulo2); */

                    /*
                    List<BloqueDTO> bloqueTipoNotificacion= lstBloquesResp.stream().filter(x-> x.getEsTipoNotificacion().equals(Boolean.TRUE)).collect(Collectors.toList());
                    if ( !bloqueTipoNotificacion.isEmpty() ){
                       informeHelperPdfService.agregarBloqueTipoNotificacion(writer, document, bloqueTipoNotificacion.get(0));
                    }
                    */
                    
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && (impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD)) )) {
                        String emailNote = null;
                        String emailSolicitanteRep=null;
                        if (expedienteDTO.getMedioComunicacionDTO() != null 
                               && expedienteDTO.getMedioComunicacionDTO().getIdValor() != null
                               && expedienteDTO.getMedioComunicacionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_INTERESADO))){
                            emailNote = noteAdmeService.recuperarEmailNote(expedienteDTO.getInteresadoDTO().getCdNumIden());
                            emailSolicitanteRep=expedienteDTO.getInteresadoDTO().getDsEmail();

                        } else if(expedienteDTO.getRepresentanteLegal() != null && 
                                expedienteDTO.getMedioComunicacionDTO() != null 
                                && expedienteDTO.getMedioComunicacionDTO().getIdValor() != null
                                && expedienteDTO.getMedioComunicacionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))) {
                            emailNote = noteAdmeService.recuperarEmailNote(expedienteDTO.getRepresentanteLegal().getCdNumIden());
                            emailSolicitanteRep=expedienteDTO.getRepresentanteLegal().getDsEmail();
                        } 

                         Boolean existeEmail = Boolean.FALSE;
                                            
                        if(emailNote != null && emailSolicitanteRep != null && emailSolicitanteRep.equalsIgnoreCase(emailNote)) {
                            existeEmail= Boolean.TRUE; 
                        }

                        informeHelperPdfService.agregarBloquePersonaNotificacion(writer, document, "NOTIFICACION", expedienteDTO.getMedioComunicacionDTO().getIdValor(), existeEmail);
                        
                    }
                    
                   
                    List<ConvocatoriaSubmodDTO> listConvocatoriaSubmodDTO = this.convocatoriaSubmodService
                            .obtenerSubmodalidadesPorConvocatoria(impresoDTO.getIdConvocatoria());
                    if (listConvocatoriaSubmodDTO != null && !listConvocatoriaSubmodDTO.isEmpty()) {
                        informeHelperPdfService.agregarBloqueModalidadExpediente( document,expedienteDTO);
                    }
                    
                    List<ConvocatoriaAreaDTO> listConvocatoriaAreaDTO = this.convocatoriaAreaService
                            .obtenerAreasCientificasPorConvocatoria(impresoDTO.getIdConvocatoria());
                    
                    
                    List<ConfiguracionDTO> listConfiguracion = this.configuracionService.obtenerConfiguracion(convocatoriaDTO);
                    
                    Boolean excluidoArea = Boolean.FALSE;
                    if(listConfiguracion != null && !listConfiguracion.isEmpty()) {
                        listConfiguracion  = listConfiguracion.stream().filter(x-> x.getCdParametro().equalsIgnoreCase("EXCLUSION_AREA")).collect(Collectors.toList());
                        if(listConfiguracion != null && !listConfiguracion.isEmpty()) {
                            excluidoArea =Boolean.TRUE;
                        }
                    }
                    
                    if (listConvocatoriaAreaDTO != null && !listConvocatoriaAreaDTO.isEmpty() && excluidoArea.equals( Boolean.FALSE)) {
                        informeHelperPdfService.agregarBloqueSubArea(document,expedienteDTO);
                    }
                    
              
                    
                    // Buscamos los participantes de este actor
                    List<AtlasFilter> filtros = new ArrayList<>();

                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDACTOR_SOLICITANTE, impresoDTO.getConvocatoriaActDTO().getIdActor(),
                            Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    
                    List<ConvocatoriaActDTO> listaParticipantesActor=new ArrayList<>(
                            Lists.transform(this.convocatoriaActDAO.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
                    if(listaParticipantesActor != null && !listaParticipantesActor.isEmpty()) {
                        String titulo = "Participantes de la ayuda";
                        this.crearInformeBloquesSolicitudParticipantes(document,  mapaDatos,expedienteDTO,titulo);
                    }
                    this.crearInformeBloques(lstBloquesResp, writer, document, convocatoriaDTO, mapaDatos,expedienteDTO,
                            impresoDTO.getTipoDTO() != null ? impresoDTO.getTipoDTO().getIdValor() : null);
                }else {
                    
                    //ConvocatoriaAct actor = this.convocatoriaActDAO.find(impresoDTO.getConvocatoriaActDTO().getIdActor());
                    String titulo1="Datos de la entidad solicitante";
                    
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && ( impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)) )) {
                        titulo1="Datos de la entidad";
                    
                    }
                    
                    this.agregarBloqueDatosOrganismoSolicitante(document, expedienteDTO, titulo1);

                    String titulo2="Datos del representante legal de la entidad solicitante";
                    
                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                            && ( impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO)) )) {
                        titulo1="Datos del representante legal de la entidad participante";
                    
                    }

                    this.agregarBloqueDatosRepresentanteSolicitante(document, expedienteDTO,titulo2);

                    /*
                    List<BloqueDTO> bloqueTipoNotificacion= lstBloquesResp.stream().filter(x-> x.getEsTipoNotificacion().equals(Boolean.TRUE)).collect(Collectors.toList());
                    if ( !bloqueTipoNotificacion.isEmpty() ){
                        informeHelperPdfService.agregarBloqueTipoNotificacion(writer, document, bloqueTipoNotificacion.get(0));
                    }
                    */

                    if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null && impresoDTO.getTipoDTO().getIdValor().equals(
                                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD) )) {
                        String emailNote = null;
                        String emailSolicitanteRep=null;
                        if (expedienteDTO.getMedioComunicacionDTO() != null 
                                && expedienteDTO.getMedioComunicacionDTO().getIdValor() != null
                               && expedienteDTO.getMedioComunicacionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_INTERESADO))){
                            emailNote = noteAdmeService.recuperarEmailNote(expedienteDTO.getInteresadoDTO().getCdNumIden());
                            emailSolicitanteRep=expedienteDTO.getInteresadoDTO().getDsEmail();

                        } else if(expedienteDTO.getRepresentanteLegal() != null && 
                                expedienteDTO.getMedioComunicacionDTO() != null 
                                && expedienteDTO.getMedioComunicacionDTO().getIdValor() != null
                                && expedienteDTO.getMedioComunicacionDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.MedioNotificacion.MEDIO_NOTIFICACION_REPRESENTANTE))){
                            emailNote = noteAdmeService.recuperarEmailNote(expedienteDTO.getRepresentanteLegal().getCdNumIden());
                            emailSolicitanteRep=expedienteDTO.getRepresentanteLegal().getDsEmail();
                        }

                        Boolean existeEmail = Boolean.FALSE;
                        
                        if(emailNote != null && emailSolicitanteRep != null && emailSolicitanteRep.equalsIgnoreCase(emailNote)) {
                            existeEmail= Boolean.TRUE; 
                        }

                        informeHelperPdfService.agregarBloquePersonaNotificacion(writer, document, "NOTIFICACION", expedienteDTO.getMedioComunicacionDTO().getIdValor(), existeEmail);
                        
                    }
                    
                    List<ConvocatoriaSubmodDTO> listConvocatoriaSubmodDTO = this.convocatoriaSubmodService
                            .obtenerSubmodalidadesPorConvocatoria(impresoDTO.getIdConvocatoria());
                    if (listConvocatoriaSubmodDTO != null && !listConvocatoriaSubmodDTO.isEmpty()) {
                        informeHelperPdfService.agregarBloqueModalidadExpediente( document,expedienteDTO);
                    }
                    
                    List<ConvocatoriaAreaDTO> listConvocatoriaAreaDTO = this.convocatoriaAreaService
                            .obtenerAreasCientificasPorConvocatoria(impresoDTO.getIdConvocatoria());
                    
                    
                    List<ConfiguracionDTO> listConfiguracion = this.configuracionService.obtenerConfiguracion(convocatoriaDTO);
                    
                    Boolean excluidoArea = Boolean.FALSE;
                    if(listConfiguracion != null && !listConfiguracion.isEmpty()) {
                        listConfiguracion  = listConfiguracion.stream().filter(x-> x.getCdParametro().equalsIgnoreCase("EXCLUSION_AREA")).collect(Collectors.toList());
                        if(listConfiguracion != null && !listConfiguracion.isEmpty()) {
                            excluidoArea =Boolean.TRUE;
                        }
                    }
                    
                    if (listConvocatoriaAreaDTO != null && !listConvocatoriaAreaDTO.isEmpty() && excluidoArea.equals( Boolean.FALSE)) {
                        informeHelperPdfService.agregarBloqueSubArea(document,expedienteDTO);
                    }
                    
                    
                 // Buscamos los participantes de este actor
                    List<AtlasFilter> filtros = new ArrayList<>();

                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ACTOR,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoActor.PARTICIPANTE), Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDACTOR_SOLICITANTE, impresoDTO.getConvocatoriaActDTO().getIdActor(),
                            Operador.EQUALS));
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                    
                    List<ConvocatoriaActDTO> listaParticipantesActor=new ArrayList<>(
                            Lists.transform(this.convocatoriaActDAO.find(filtros, null), ConvocatoriaActToDTO.INSTANCE));
                    if(listaParticipantesActor != null && !listaParticipantesActor.isEmpty()) {
                        String titulo = "Participantes de la ayuda";
                        this.crearInformeBloquesSolicitudParticipantes(document,  mapaDatos,expedienteDTO,titulo);
                    }
                    
                    this.crearInformeBloques(lstBloquesResp, writer, document, convocatoriaDTO, mapaDatos,expedienteDTO,
                            impresoDTO.getTipoDTO() != null ? impresoDTO.getTipoDTO().getIdValor() : null);
                }
                
                if (Boolean.TRUE.equals(siFondosEuropeos)) {
                    informeHelperPdfService.agregarTextoFondosEuropeos(document);
                }
                informeHelperPdfService.agregarLugarFecha(document);
                
                Boolean esFormularioComplementario = impresoDTO.getTipoDTO() != null
                        && impresoDTO.getTipoDTO().getIdValor() != null
                        && impresoDTO.getTipoDTO().getIdValor().equals(
                                utilConstantes.getGlobalCatalogosValores().get(
                                        Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));

                if (Boolean.FALSE.equals(esFormularioComplementario)) {
                    informeHelperPdfService.agregarHuellaDigital(document);

                    // Se agrega una fila vacía para evitar que lugarFecha se solape
                    // con el destinatario
                    informeHelperPdfService.agregarFilaVacio(document, 1);

                    // si es la ultima pagina mostramos el destinatario
                    if (writer.getCurrentPageNumber() == writer.getPageNumber()) {
                        informeHelperPdfService.agregarDestinatario(writer, document, convocatoriaDTO,strConsejeria);
                    }
                }
                
                //VMM 05/06/25 MD-1633: añadimos una última pagina al pdf con la informacion de proteccion de datos
                if (convocatoriaDTO.getInformacionRgpd() != null && !convocatoriaDTO.getInformacionRgpd().trim().isEmpty()) {
                    
                    document.newPage();
                    
                    //añadir detras de cada cierre de parrafo (</p>) un salto de linea                 
                    this.informeHelperPdfService.agregarTextoEnriquecido(document,
                            "",     // El subtítulo o encabezado
                            convocatoriaDTO.getInformacionRgpd(),     // El HTML guardado como CLOB
                            5,                      // Ancho de la primera columna
                            true                     // activar el modo HTML
                        );
                }

                document.close();

                bytesSalida = baos.toByteArray();
            }

        } catch (DocumentException e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

        }

        return bytesSalida;
    }


    /**
     * Metodo para crear informes - bloques
     *
     * @param lstBloques
     * @param writer
     * @param document
     * @param convocatoriaDTO
     * @throws ServiceException
     */
    private void crearInformeBloques(List<BloqueDTO> lstBloques, PdfWriter writer,
            Document document, ConvocatoriaDTO convocatoriaDTO, Map<String, String> mapaDatos,ExpedienteDTO dto,
            Integer idTipoImpreso)
            throws ServiceException {

        try {
            // como obtenemos la lista total de bloques a traves de la
            // consulta de impreso debemos filtrar los registros que no
            // estan de baja
            lstBloques = lstBloques.stream().filter(p -> Objects.isNull(p.getFcBajaFila()))
                    .collect(Collectors.toList());
            
            //solo aplica a solicitudes cuando expediente ya esta creado.
            if (dto != null && dto.getIdExpediente() != null && !lstBloques.isEmpty()) {
                String selectedAyudaConcurrentes="";
                
                List<ConfiguracionDTO> listaConfiguracion = this.configuracionService.obtenerConfiguracion(convocatoriaDTO);
                ConfiguracionDTO ayudaConfig= listaConfiguracion.stream().filter(c -> Objects.equals(c.getCdParametro(), "AYUDAS_MISMO_P")).findFirst().orElse(null);
                if(ayudaConfig != null && ayudaConfig.getCdValor() != null) {
                   Integer idValorBloqueAyuda= Integer.valueOf(ayudaConfig.getCdValor());
                   
                   for(BloqueDTO bloq : lstBloques) {
                       List<BloqueComponenteDTO> bloqComp= bloq.getLstBloqueComponenteDTO().stream().filter(c-> Objects.isNull(c.getFcBajaFila())
                                 && c.getComponenteDTO().getNmConstante() != null
                                 && c.getComponenteDTO().getNmConstante().equals(Constantes.ComponenteSolicitud.AYUDAS_CONCURRENTES)).collect(Collectors.toList());
                       if(bloqComp != null && !bloqComp.isEmpty()) {
                           List<DatoDTO> datoAyudaConcurrente=bloqComp.get(0).getDatos();
                           if(!datoAyudaConcurrente.isEmpty()) {
                               selectedAyudaConcurrentes=datoAyudaConcurrente.get(0).getDsValor();
                               break;
                           }
                           
                       }
                         
                     }
                   
                   if(selectedAyudaConcurrentes != null && selectedAyudaConcurrentes.equalsIgnoreCase("No ha recibido ni solicitado ninguna subvención, ayuda o aportación económica.")) {
                       lstBloques = lstBloques.stream().filter(p -> Objects.isNull(p.getFcBajaFila())
                               && (( Objects.isNull(p.getIdTematica()) || Objects.isNull(p.getIdTematica().getDsValor()) ) || ( p.getIdTematica() != null && p.getIdTematica().getDsValor() != null
                               && !p.getIdBloque().equals(idValorBloqueAyuda)))
                               )
                               .collect(Collectors.toList());
                       
                   }
                }
                
                
                
               
            }
            

            if (!lstBloques.isEmpty()) {
               
                // Ordenamos los bloques por nmOrden y por fecha de creación debido a que hay subloques por el multibloque
                //lstBloques.sort(Comparator.comparing(BloqueDTO::getNmOrden));
                Collections.sort(lstBloques, (a, b) -> (a.getNmOrden() < b.getNmOrden() &&
                        a.getFcAltaFila().compareTo(b.getFcAltaFila()) < 0 )  ? -1 :
                            (a.getNmOrden().equals(b.getNmOrden()) &&
                            a.getFcAltaFila().compareTo(b.getFcAltaFila()) == 0 ) ? 0 : 1);

                for (BloqueDTO bloque : lstBloques) {

                    // como obtenemos la lista total de bloques a traves de
                    // la consulta de impreso debemos filtrar los registros
                    // que no estan de baja
                    if (Objects.nonNull(bloque.getLstBloqueComponenteDTO())) {
                        List<BloqueComponenteDTO> lstBloqueComponente = bloque
                                .getLstBloqueComponenteDTO().stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila()))
                                .collect(Collectors.toList());
                        for (BloqueComponenteDTO bloqCom : lstBloqueComponente) {
                            bloqCom.setDatos(bloqCom.getDatos().stream().filter(p -> p.getExpedienteDTO() != null 
                                    && p.getExpedienteDTO().getIdExpediente() != null
                                    && p.getExpedienteDTO().getIdExpediente().equals(dto.getIdExpediente()))
                    .collect(Collectors.toList()));
                        }
                        
                        bloque.setLstBloqueComponenteDTO(lstBloqueComponente);
                    }

                    if (Objects.nonNull(bloque.getLstBloqueComponentesFilaDTO())) {
                        List<BloqueComponenteDTO> lstBloqueComponenteFila = bloque
                                .getLstBloqueComponentesFilaDTO().stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila()))
                                .collect(Collectors.toList());
                        bloque.setLstBloqueComponentesFilaDTO(lstBloqueComponenteFila);
                    }

                    
                    if(dto != null 
                            && Objects.nonNull(bloque.getItMultiRegistro()) 
                            && ( bloque.getItMultiRegistro().equals(Constantes.AfirmacionNegacion.S)
                         ) ) {
                        this.agregarBloqueMultiregistro(writer, document, bloque, dto, convocatoriaDTO);
                    }else if ( (Objects.isNull(bloque.getIdBloquePadre() )
                            || (dto != null && dto.getIdExpediente() == null)
                            ||( dto != null && dto.getIdExpediente()!= null && Objects.nonNull(bloque.getIdBloquePadre()) 
                            &&  this.bloqueService.buscarId(bloque.getIdBloquePadre()).getItMultiRegistro().equals(Constantes.AfirmacionNegacion.N)))
                           && (bloque.getEsTipoPersonaSolicitante().equals(Boolean.FALSE)
                                && bloque.getEsTipoNotificacion().equals(Boolean.FALSE))
                            ) {
                        // Si no es de participante
                        if( Boolean.FALSE.equals(Constantes.Formatos.ID_PARTICIPANTES.equals(bloque.getIdFormato().getNmConstante()))) {
                            informeHelperPdfService.agregarBloque(writer, document, bloque, convocatoriaDTO,
                                    mapaDatos,dto,idTipoImpreso);
                        } else {
                            this.agregarBloqueParticipantes(document, bloque);
                        }
                        
                    }
                         
                }
            }

        } catch (DocumentException e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

        }
    }
     

    /**
     * Guardar.
     *
     * @param impresoDTO the ImpresoDTO
     * @return the ImpresoDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ImpresoDTO guardar(ImpresoDTO impresoDTO) throws ServiceException {
        // Insertamos el Impreso
        Impreso impreso = this.insertOrUpdate(ImpresoToEntity.INSTANCE.apply(impresoDTO));
        return ImpresoToDTO.INSTANCE.apply(impreso);
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
    public List<ImpresoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtros, orden), ImpresoToDTO.INSTANCE));

    }
    
    @Override
    public ImpresoDTO buscarPorConvActor(final Integer idConvocatoria, final Integer idActor,
            final List<Integer> lstTipoImpreso) throws ServiceException {

        ImpresoDTO dto = new ImpresoDTO();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
        filtros.add(
                new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        if (Objects.nonNull(lstTipoImpreso) && !lstTipoImpreso.isEmpty()) {                   
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    lstTipoImpreso, Operador.IN));
        }

        List<Impreso> lstImpresos = this.find(filtros, null);

        if (lstImpresos != null && !lstImpresos.isEmpty()) {
            dto = ImpresoToDTO.INSTANCE.apply(lstImpresos.get(0));
        } else {
            //Si el imopreso no esta configurado no se manda error, se devuelve un nulo, por que luego lo comprobamos
            dto = null;
        }

        return dto;
    }

    /**
     * Buscar id.
     *
     * @param idImpreso the idImpreso
     * @return the ImpresoDTO
     * @throws ServiceException the service exception
     */
    @Override
    public ImpresoDTO buscarId(final Integer idImpreso) throws ServiceException {
        return ImpresoToDTO.INSTANCE.apply(this.find(idImpreso));
    }

    /**
     * Descargar.
     *
     * @param cdFica the cd fica
     * @return the documento DTO
     * @throws ServiceException the service exception
     */
    @Override
    public ImpresoDTO descargar(Integer cdFica) throws ServiceException {

        ImpresoDTO impreso = new ImpresoDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
        impreso.setFicaFichero(ficaFichero);
        impreso.setContenidoFichero(ficaFichero.getDatosFichero());
        impreso.setCdFica(cdFica);
        return impreso;
    }

    @Override
    public ImpresoDTO subirDocumentoFica(ImpresoDTO impresoDTO, Integer tipoDocImpreso)
            throws ServiceException {

        // Impreso original = "I"
        if (tipoDocImpreso == 1) {
            this.subirDocumentoFicaUno(impresoDTO);
        }

        // plantilla tecnica = "P"
        else if (tipoDocImpreso == 2) {
            this.subirDocumentoFicaDos(impresoDTO);
        }

        return impresoDTO;
    }

    /**
     * Método para subir documento fica tipo uno
     *
     * @param impresoDTO Datos de impreso
     * @throws ServiceException Exception ocurrida en el servicio
     */
    private void subirDocumentoFicaUno(ImpresoDTO impresoDTO) throws ServiceException {
        FicaFichero ficheroRecuperarFica = null;
        FicaFichero fichero;

        // Validacion
        if (impresoDTO.getContenidoFicheroImpOriginal() == null
                || impresoDTO.getFicaMetadatosImpOriginal() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        impresoDTO.getFicaMetadatosImpOriginal()
                .setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        if (impresoDTO.getFicaFicheroImpOriginal() != null) {
            ficheroRecuperarFica = this.ficaService.recuperar(impresoDTO.getCdFicaImpreso());
        }
        if (ficheroRecuperarFica == null) {

            fichero = this.ficaService.guardar(impresoDTO.getContenidoFicheroImpOriginal(),
                    impresoDTO.getFicaMetadatosImpOriginal());

        }

        else {
            fichero = this.ficaService.actualizar(impresoDTO.getCdFicaImpreso(),
                    impresoDTO.getContenidoFicheroImpOriginal(),
                    impresoDTO.getFicaMetadatosImpOriginal());
        }
        impresoDTO.setFicaFicheroImpOriginal(fichero);
        impresoDTO.setCdFicaImpreso(fichero.getIdInterno());
        impresoDTO.setDsImpresoOriginal(fichero.getNombreFichero());
    }

    /**
     * Método para subir documento fica tipo dos
     *
     * @param impresoDTO Datos de impreso
     * @throws ServiceException Exception ocurrida en el servicio
     */
    private void subirDocumentoFicaDos(ImpresoDTO impresoDTO) throws ServiceException {
        FicaFichero ficheroRecuperarFica = null;
        FicaFichero fichero;

        // Validacion
        if (impresoDTO.getContenidoFicheroPlanTecnica() == null
                || impresoDTO.getFicaMetadatosPlanTecnica() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        impresoDTO.getFicaMetadatosPlanTecnica()
                .setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);

        if (impresoDTO.getFicaFicheroPlantTecnica() != null) {
            ficheroRecuperarFica = this.ficaService.recuperar(impresoDTO.getCdFicaPlantilla());
        }
        if (ficheroRecuperarFica == null) {

            fichero = this.ficaService.guardar(impresoDTO.getContenidoFicheroPlanTecnica(),
                    impresoDTO.getFicaMetadatosPlanTecnica());

        }

        else {
            fichero = this.ficaService.actualizar(impresoDTO.getCdFicaPlantilla(),
                    impresoDTO.getContenidoFicheroPlanTecnica(),
                    impresoDTO.getFicaMetadatosPlanTecnica());
        }
        impresoDTO.setFicaFicheroPlantTecnica(fichero);
        impresoDTO.setCdFicaPlantilla(fichero.getIdInterno());
        impresoDTO.setDsPlantillaTecnica(fichero.getNombreFichero());
    }

    /**
     *
     */
    @Override
    public List<ImpresoDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.find(filtros, null), ImpresoToDTO.INSTANCE));
    }

    /**
     * Método para crear los bloques de tipo participante
     *
     * @param document Documento PDF
     * @param bloqueDTO Bloque asociado al participante
     * @throws ServiceException generada al generar contenido del documento
     */
    private void agregarBloqueParticipantes(Document document, BloqueDTO bloqueDTO)
            throws ServiceException {
        try {
            // Obtenemos el número máximo de participantes permitidos
            ConvocatoriaAct convocatoriaAct = this.convocatoriaActDAO
                    .find(bloqueDTO.getActor().getIdActor());

            if (convocatoriaAct == null) {
                throw new ServiceException("No se ha encontrado la convocatoria actor: "
                        + bloqueDTO.getActor().getIdActor());
            }

            informeHelperPdfService.agregarBloqueParticipantes(document, bloqueDTO, convocatoriaAct);

        } catch (DocumentException e) {
            log.error("Error al agregar el bloque de participantes ", e);
            throw new ServiceException(e);
        }
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void eliminarDependenciaImpreso(ImpresoDTO convImpDTO) throws ServiceException {

        for (BloqueDTO bloqueDto : convImpDTO.getLstBloqueDTO()) {

            // recuperamos tematica
            Integer idTematica = bloqueDto.getIdTematica().getIdValor();

            // recuperamos objetos componente y tipo financiacion
            List<ComponenteDTO> lstComponente = bloqueDto.getLstBloqueComponenteDTO().stream()
                    .filter(f -> Objects.nonNull(f.getComponenteDTO())
                            && Objects.nonNull(f.getComponenteDTO().getIdComponente()))
                    .map(BloqueComponenteDTO::getComponenteDTO).collect(Collectors.toList());
            List<CatalogosValoresDTO> lstTipoFinanciacion = bloqueDto.getLstBloqueComponenteDTO()
                    .stream()
                    .filter(f -> Objects.nonNull(f.getTipoFinanciacionDTO())
                            && Objects.nonNull(f.getTipoFinanciacionDTO().getIdValor()))
                    .map(BloqueComponenteDTO::getTipoFinanciacionDTO).collect(Collectors.toList());

            // recuperamos lista de id componente y id tipo financiacion
            List<Integer> lstIdComponente = lstComponente.stream()
                    .mapToInt(ComponenteDTO::getIdComponente).boxed().collect(Collectors.toList());
            List<Integer> lstIdTipoFinanciacion = lstTipoFinanciacion.stream()
                    .mapToInt(CatalogosValoresDTO::getIdValor).boxed().collect(Collectors.toList());

            // Eliminamos columnas listado por idComponente y idTematica
            if (!lstIdComponente.isEmpty() && Objects.nonNull(idTematica)) {
                List<AtlasFilter> lstFiltros = new ArrayList<>();
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_TEMATICA, idTematica,
                        Operador.EQUALS));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_COMPONENTE,
                        lstIdComponente, Operador.IN));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ListadoColumna> lstListadoColumna = this.listadoColumnaDAO.find(0, -1,
                        lstFiltros, null);

                for (ListadoColumna listadCol : lstListadoColumna) {
                    this.listadoColumnaDAO.deleteLogico(listadCol.getIdListadoColumna());
                }
            }

            // Eliminamos columnas listado por idTipoFinanciacion y
            // idTematica
            if (!lstIdTipoFinanciacion.isEmpty() && Objects.nonNull(idTematica)) {
                List<AtlasFilter> lstFiltros = new ArrayList<>();
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_TEMATICA, idTematica,
                        Operador.EQUALS));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_TIPO_FINANCIACION,
                        lstIdTipoFinanciacion, Operador.IN));
                lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                List<ListadoColumna> lstListadoColumna = this.listadoColumnaDAO.find(0, -1,
                        lstFiltros, null);

                for (ListadoColumna listadCol : lstListadoColumna) {
                    this.listadoColumnaDAO.deleteLogico(listadCol.getIdListadoColumna());
                }
            }
            
            // Eliminamos los bloques componentes
            for (BloqueComponenteDTO bc : bloqueDto.getLstBloqueComponenteDTO()) {
                this.bloqueComponenteService.deleteLogico(bc.getIdBloqueComponente());
            }

            // Eliminamos el bloque
            this.bloqueService.deleteLogico(BloqueToEntity.INSTANCE.apply(bloqueDto));

        }

        // Eliminamos el formulario
        this.deleteLogico(convImpDTO.getIdImpreso());

    }
    
    /**
     * Buscar formulario por convocatoria y actor
     * @param idConvocatoria
     * @param idActor
     * @return List<ImpresoDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ImpresoDTO> buscarFormulariosPorIdConvocatoriaIdActor(Integer idConvocatoria, Integer idActor) throws ServiceException{
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ImpresoToDTO.INSTANCE));
    }
    
    /**
    *
    */
    @Override
    public Boolean validarConvocatoriaActasGestion(Integer idConvocatoria) throws ServiceException{
       
        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        List<AtlasFilter> filtroTipoImpreso = new ArrayList<>();
        filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ID_VALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACTA_GESTION_ORDINARIA), Operador.EQUALS));
        filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ID_VALOR,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACTA_PRESUPUESTARIA), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ID_VALOR,
                filtroTipoImpreso, Operador.OR));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<Impreso> lstConvocatoriaImp = this.find(filtros, null);

        return !lstConvocatoriaImp.isEmpty();
    }
    
    
    @Override
    public List<ImpresoDTO> buscarPorConvActorJustiCT(final Integer idConvocatoria, final Integer idActor) throws ServiceException {

        List<ImpresoDTO> lstImpresosJustiDTO = new ArrayList<>();
        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            List<AtlasFilter> filtroTipoImpreso = new ArrayList<>();    
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor, Operador.EQUALS));            
            filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA), Operador.EQUALS));
            filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    filtroTipoImpreso, Operador.OR));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
            List<Impreso> lstImpresos = this.find(filtros, null);
    
            if (lstImpresos != null && !lstImpresos.isEmpty()) {
                for (Impreso impreso: lstImpresos){
                    ImpresoDTO impresosJustiDTO = ImpresoToDTO.INSTANCE.apply(impreso);
                    lstImpresosJustiDTO.add(impresosJustiDTO);
                }
            } else {
                log.error(Constantes.Error.ERROR_IMPRESO_ACTOR);
            }
        }catch (ServiceException e) {
            throw new ServiceException(e);
        }

        return lstImpresosJustiDTO;
    }
    
    
    @Override
    public List<ImpresoDTO> buscarPorConvActorJustiEco(final Integer idConvocatoria, final Integer idActor) throws ServiceException {

        List<ImpresoDTO> lstImpresosJustiDTO = new ArrayList<>();
        try {
            List<AtlasFilter> filtros = new ArrayList<>();
            List<AtlasFilter> filtroTipoImpreso = new ArrayList<>();    
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, idConvocatoria, Operador.EQUALS));
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor, Operador.EQUALS));            
            filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA), Operador.EQUALS));
            filtroTipoImpreso.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    filtroTipoImpreso, Operador.OR));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
    
            List<Impreso> lstImpresos = this.find(filtros, null);
    
            if (lstImpresos != null && !lstImpresos.isEmpty()) {
                for (Impreso impreso: lstImpresos){
                    ImpresoDTO impresosJustiDTO = ImpresoToDTO.INSTANCE.apply(impreso);
                    lstImpresosJustiDTO.add(impresosJustiDTO);
                }
            } else {
                log.error(Constantes.Error.ERROR_IMPRESO_ACTOR);
            }
        }catch (ServiceException e) {
            throw new ServiceException(e);
        }

        return lstImpresosJustiDTO;
    }
    
    @Override
    public byte[] crearInformeContrato(ExpedienteDTO dto,final ImpresoDTO impresoDTO, final Map<String, String> mapaDatos,
            List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException {

        Boolean siFondosEuropeos = Boolean.FALSE;
        Map<String, byte[]> lstLogo = new HashMap<>();
        byte[] logoOrganismo = null;
        FicaFichero ficheroRecuperar = null;
        byte[] bytesSalida = null;
        int i = 1;
        ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                .apply(this.convocatoriaDAO.find(convocatoriaDTOIn.getIdConvocatoria()));

        Document document = new Document(PageSize.A4, 36, 36, 102, 36);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            
            if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItFondosEuropa())) {
                siFondosEuropeos = Boolean.TRUE;

                // obtenemos la imagen fica de union europea
                List<ImagenDTO> lstLogosEuropeos = this.imagenService
                        .buscarLogosEuropeos(convocatoriaDTOIn.getIdConvocatoria());

                for (ImagenDTO elemento : lstLogosEuropeos) {
                    if (Objects.nonNull(elemento.getCdFica())
                            && StringUtils.isNotBlank(elemento.getCdFica())) {
                        ficheroRecuperar = this.ficaService
                                .recuperar(Integer.valueOf(elemento.getCdFica()));

                        if (ficheroRecuperar != null) {
                            lstLogo.put("ImgLogo" + i, ficheroRecuperar.getDatosFichero());
                            i++;
                        }
                    }
                }

            }
            if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdConvocatoria())) {

               if (Objects.nonNull(impresoDTO.getImagenLogoDTO().getCdFica())
                        && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica())) {
                    // obtenemos la imagen fica del organismo

                    ficheroRecuperar = this.ficaService
                            .recuperar(Integer.valueOf(impresoDTO.getImagenLogoDTO().getCdFica()));
                    logoOrganismo = ficheroRecuperar.getDatosFichero();

                }

                
            }
            String strConsejeria = this.unidadAdminService.obtenerConsejeria();

            String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

                PdfWriter writer = PdfWriter.getInstance(document, baos);
                writer.createXmpMetadata();
                writer.setTagged();
                writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                        strConsejeria, strDireccion, Boolean.TRUE, lstLogo, logoOrganismo));
                document.open();

                Boolean agregarEspacioLogos = !lstLogo.isEmpty() || Objects.nonNull(logoOrganismo);
                List<BloqueDTO> lstBloques =null;
                if(impresoDTO != null) {
                    informeHelperPdfService.agregarTitulo(document, impresoDTO.getDsDescripcion(),
                            convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
                    lstBloques = impresoDTO.getLstBloqueDTO();
                }else {
                    informeHelperPdfService.agregarTitulo(document, "",
                            convocatoriaDTO.getCdTitulo(),"",dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
                }
                
                this.agregarNumeroExpediente(dto, document);

                this.crearInformeBloquesContrato(dto,lstBloques, writer, document, convocatoriaDTO, mapaDatos,listExpedientesParticipantes);

                if (Boolean.TRUE.equals(siFondosEuropeos)) {
                    informeHelperPdfService.agregarTextoFondosEuropeos(document);
                }
                informeHelperPdfService.agregarLugarFecha(document);
                informeHelperPdfService.agregarHuellaDigital(document);
                
                // Se agrega filas vacías para evitar que lugarFecha se solape
                // con el Destinatario
                informeHelperPdfService.agregarFilaVacio(document, 1);
                informeHelperPdfService.agregarFilaVacio(document, 1);

                // si es la ultima pagina mostramos el destinatario
                if (writer.getCurrentPageNumber() == writer.getPageNumber()) {
                    informeHelperPdfService.agregarDestinatario(writer, document, convocatoriaDTO,strConsejeria);
                }

                document.close();

                bytesSalida = baos.toByteArray();
           

        } catch (DocumentException e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

        }

        return bytesSalida;
    }
    
    @Override
    public byte[] crearInformePresentacionContrato(ExpedienteDTO dto,final ImpresoDTO impresoDTO, final Map<String, String> mapaDatos,
            List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
                    throws ServiceException {
        
        Boolean siFondosEuropeos = Boolean.FALSE;
        Map<String, byte[]> lstLogo = new HashMap<>();
        byte[] logoOrganismo = null;
        FicaFichero ficheroRecuperar = null;
        byte[] bytesSalida = null;
        int i = 1;
        ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                .apply(this.convocatoriaDAO.find(convocatoriaDTOIn.getIdConvocatoria()));
        
        Document document = new Document(PageSize.A4, 36, 36, 102, 36);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            
            if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItFondosEuropa())) {
                siFondosEuropeos = Boolean.TRUE;
                
                // obtenemos la imagen fica de union europea
                List<ImagenDTO> lstLogosEuropeos = this.imagenService
                        .buscarLogosEuropeos(convocatoriaDTOIn.getIdConvocatoria());
                
                for (ImagenDTO elemento : lstLogosEuropeos) {
                    if (Objects.nonNull(elemento.getCdFica())
                            && StringUtils.isNotBlank(elemento.getCdFica())) {
                        ficheroRecuperar = this.ficaService
                                .recuperar(Integer.valueOf(elemento.getCdFica()));
                        
                        if (ficheroRecuperar != null) {
                            lstLogo.put("ImgLogo" + i, ficheroRecuperar.getDatosFichero());
                            i++;
                        }
                    }
                }
                
            }
            
            if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdConvocatoria()) 
                        &&  (Objects.nonNull(impresoDTO.getImagenLogoDTO().getCdFica())
                                && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica()))) {
                // obtenemos la imagen fica del organismo
                ficheroRecuperar = this.ficaService
                        .recuperar(Integer.valueOf(impresoDTO.getImagenLogoDTO().getCdFica()));
                logoOrganismo = ficheroRecuperar.getDatosFichero();
            }
            
            String strConsejeria = this.unidadAdminService.obtenerConsejeria();
            
            String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();
            
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.createXmpMetadata();
            writer.setTagged();
            writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                    strConsejeria, strDireccion, Boolean.TRUE, lstLogo, logoOrganismo));
            document.open();
            
            Boolean agregarEspacioLogos = !lstLogo.isEmpty() || Objects.nonNull(logoOrganismo);
            List<BloqueDTO> lstBloques =null;
            if(impresoDTO != null) {
                informeHelperPdfService.agregarTitulo(document, impresoDTO.getDsDescripcion(),
                        convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
                lstBloques = impresoDTO.getLstBloqueDTO();
            }else {
                informeHelperPdfService.agregarTitulo(document, "",
                        convocatoriaDTO.getCdTitulo(),"",dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
            }
            
            this.agregarNumeroExpediente(dto, document);
            
            this.crearInformePresentacionContrato(dto,lstBloques, writer, document, convocatoriaDTO, mapaDatos,listExpedientesParticipantes);
            
            if (Boolean.TRUE.equals(siFondosEuropeos)) {
                informeHelperPdfService.agregarTextoFondosEuropeos(document);
            }
            
            informeHelperPdfService.agregarLugarFecha(document);
            informeHelperPdfService.agregarHuellaDigital(document);
            
            
            // Se agrega filas vacías para evitar que lugarFecha se solape
            // con el Destinatario
            informeHelperPdfService.agregarFilaVacio(document, 1);
            informeHelperPdfService.agregarFilaVacio(document, 1);
            
            // si es la ultima pagina mostramos el destinatario
            if (writer.getCurrentPageNumber() == writer.getPageNumber()) {
                informeHelperPdfService.agregarDestinatario(writer, document, convocatoriaDTO,strConsejeria);
            }
            
            document.close();
            
            bytesSalida = baos.toByteArray();
            
            
        } catch (DocumentException e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
        
        return bytesSalida;
    }

    /**
     * @param expDTO
     * @param document
     * @throws DocumentException
     */
    private void agregarNumeroExpediente(ExpedienteDTO expDTO, Document document)
            throws DocumentException {
        //Agregamos cdExpediente
        String numeroExpediente = (expDTO == null || expDTO.getCdExpediente() == null) 
                ? "" 
                : expDTO.getCdExpediente().trim();
        
        Font tipoLetra10Bold = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
        Font tipoLetra9 = new Font(FontFamily.HELVETICA, 9);
        informeHelperPdfService.agregarSubTituloDesCentrado(document, Constantes.Plantilla.TEXTO_NUMERO_EXPEDIENTE, numeroExpediente, tipoLetra10Bold, tipoLetra9, 1);
    }

    
    private void crearInformeBloquesContrato(ExpedienteDTO dto,List<BloqueDTO> lstBloques, PdfWriter writer,
            Document document, ConvocatoriaDTO convocatoriaDTO, Map<String, String> mapaDatos,
            List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException {
        try {
            //Agregamos bloque con la informacion del solicitante
            if(dto != null) {
                this.agregarBloqueDatosInteresado(document, dto, null);
            }

            // como obtenemos la lista total de bloques a traves de la
            // consulta de impreso debemos filtrar los registros que no
            // estan de baja
           
            if (lstBloques != null && !lstBloques.isEmpty()) {
                lstBloques = lstBloques.stream().filter(p -> Objects.isNull(p.getFcBajaFila()))
                        .collect(Collectors.toList());
                // Ordenamos los bloques por nmOrden
                lstBloques.sort(Comparator.comparing(BloqueDTO::getNmOrden));

                for (BloqueDTO bloque : lstBloques) {

                    // como obtenemos la lista total de bloques a traves de
                    // la consulta de impreso debemos filtrar los registros
                    // que no estan de baja
                    if (Objects.nonNull(bloque.getLstBloqueComponenteDTO())) {
                        List<BloqueComponenteDTO> lstBloqueComponente = bloque
                                .getLstBloqueComponenteDTO().stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila()))
                                .collect(Collectors.toList());
                        bloque.setLstBloqueComponenteDTO(lstBloqueComponente);
                    }

                    if (Objects.nonNull(bloque.getLstBloqueComponentesFilaDTO())) {
                        List<BloqueComponenteDTO> lstBloqueComponenteFila = bloque
                                .getLstBloqueComponentesFilaDTO().stream()
                                .filter(p -> Objects.isNull(p.getFcBajaFila()))
                                .collect(Collectors.toList());
                        bloque.setLstBloqueComponentesFilaDTO(lstBloqueComponenteFila);
                    }

                    // Si no es de participante
                    if (Boolean.FALSE.equals(Constantes.Formatos.ID_PARTICIPANTES.equals(bloque.getIdFormato().getNmConstante()))) {
                        informeHelperPdfService.agregarBloque(writer, document, bloque, convocatoriaDTO,
                                mapaDatos,dto,null);
                    }
                }
            }
            
            this.agregarBloqueParticipantesContato(document,dto.getConvocatoriaActDTO().getIdActor(),listExpedientesParticipantes); 

        } catch (DocumentException e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

        }
    }
    
    private void crearInformePresentacionContrato(ExpedienteDTO dto,List<BloqueDTO> lstBloques, PdfWriter writer,
            Document document, ConvocatoriaDTO convocatoriaDTO, Map<String, String> mapaDatos,
            List<ExpedienteDTO> listExpedientesParticipantes)
                    throws ServiceException {
        try {
            this.agregarBloqueDatosInteresado(document, dto, null);
            this.agregarBloqueParticipantesContato(document,dto.getConvocatoriaActDTO().getIdActor(),listExpedientesParticipantes); 
            
        } catch (Exception e) {
            throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }
    }
    
    private void agregarBloqueParticipantesContato(Document document, Integer idActor,
            List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException {
        try {
            // Obtenemos el número máximo de participantes permitidos
            ConvocatoriaAct convocatoriaAct = this.convocatoriaActDAO
                    .find(idActor);

           

            informeHelperPdfService.agregarBloqueParticipantesContrato(document, convocatoriaAct,listExpedientesParticipantes);

        } catch (DocumentException e) {
            log.error("Error al agregar el bloque de participantes ", e);
            throw new ServiceException(e);
        }
    }
    
    private void agregarBloqueDatosInteresado(Document document, ExpedienteDTO expediente, String titulo1)
                    throws ServiceException {
        try {
            
            String localidad = "";
            if(expediente != null && expediente.getInteresadoDTO() != null && 
                    expediente.getInteresadoDTO().getCdProvincia() != null && 
                    expediente.getInteresadoDTO().getCdLocalidad() != null) {
                try {
                    localidad = obtenerDescripcionMunicipio(expediente.getInteresadoDTO().getCdProvincia(), expediente.getInteresadoDTO().getCdLocalidad());
                    expediente.getInteresadoDTO().setDsLocalidad(localidad);
                } catch (ServiceException e) {
                    log.error("Error obteniendo localidad");
                    expediente.getInteresadoDTO().setDsLocalidad("");
                }
            }
            
            if(expediente != null && expediente.getInteresadoDTO() != null && 
                    expediente.getInteresadoDTO().getCdProvincia() != null) {
                try {
                    String provincia = obtenerDescripcionProvincia(expediente.getInteresadoDTO().getCdProvincia());
                    expediente.getInteresadoDTO().setDsProvincia(provincia);
                } catch (ServiceException e) {
                    log.error("Error obteniendo provincia");
                    expediente.getInteresadoDTO().setDsProvincia("");
                }
            }

            informeHelperPdfService.agregarBloqueDatosInteresado(document, expediente,titulo1);
            
        } catch (DocumentException e) {
            log.error("Error al agregar el bloque de datos interesado ", e);
            throw new ServiceException(e);
        }
    }
    
    @Override
    public ImpresoDTO buscarPorConvActorContrato(final Integer idConvocatoria, final Integer idActor,
            final List<Integer> lstTipoImpreso) throws ServiceException {

        ImpresoDTO dto = new ImpresoDTO();

        List<Impreso> lstImpresos = this.getDao().buscarPorConvActorContrato(idConvocatoria, idActor, lstTipoImpreso);

        if (lstImpresos != null && !lstImpresos.isEmpty()) {
            dto = ImpresoToDTO.INSTANCE.apply(lstImpresos.get(0));
        }

        return dto;
    }
    
    /**
     * Método que devuelve el nombre del municipio o vacío
     *
     * @param cdProvincia provincia
     * @param cdLocalidad municipio
     * @return nombre de la localidad o empty
     * @throws ServiceException
     */
    private String obtenerDescripcionMunicipio(String cdProvincia, String cdLocalidad)
            throws ServiceException {
        String dsDescripcion = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(cdProvincia)) {
            SucaMunicipioDTO sucaMunicipioDTO = this.sucaMunicipioService
                    .obtenerMunicipio(cdProvincia, cdLocalidad);
            dsDescripcion = sucaMunicipioDTO.getDsMunicipio();
        }

        return dsDescripcion;
    }

    /**
     * Método que devuelve el nombre de la provincia o vacío
     *
     * @param cdProvincia a obtener
     * @return nombre de la provincia o empty
     * @throws ServiceException excepción
     */
    private String obtenerDescripcionProvincia(String cdProvincia) throws ServiceException {
        String dsDescripcion = StringUtils.EMPTY;

        if (StringUtils.isNotBlank(cdProvincia)) {
            CatalogoDTO provinciaDTO = this.sucaProvinciaService.obtenerPorProvincia(cdProvincia);
            dsDescripcion = provinciaDTO.getDescripcion();
        }

        return dsDescripcion;
    }
    
    /**
    *
    */
   @Override
   public List<ImpresoDTO> buscarImpresosGenerarDatos(Integer idExpediente)
           throws ServiceException {
       
       // tipos impreso 
       List<Integer> lstTipoImpreso = new ArrayList<>();
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_RENUNCIA));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_SUSTITUCION));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_INTERMEDIA));
       lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_ECO_FINAL));
       
       return new ArrayList<>(Lists.transform(this.getDao().buscarPorExpedienteTipo(idExpediente, lstTipoImpreso), ImpresoToDTO.INSTANCE));
   }
   
   public List<Impreso> buscarPorExpedienteTipo(final Integer idExpediente, final List<Integer> lstTipoImpreso){
       return new ArrayList<Impreso>(this.getDao().buscarPorExpedienteTipo(idExpediente, lstTipoImpreso));
   }
   
   @Override
   public byte[] crearInformeSubsanacionReq(ExpedienteDTO dto,final ImpresoDTO impresoDTO, final Map<String, String> mapaDatos,
           List<ExpedienteDTO> listExpedientesParticipantes,ConvocatoriaDTO convocatoriaDTOIn)
           throws ServiceException {

       Boolean siFondosEuropeos = Boolean.FALSE;
       Map<String, byte[]> lstLogo = new HashMap<>();
       byte[] logoOrganismo = null;
       FicaFichero ficheroRecuperar = null;
       byte[] bytesSalida = null;
       int i = 1;
       ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
               .apply(this.convocatoriaDAO.find(convocatoriaDTOIn.getIdConvocatoria()));

       Document document = new Document(PageSize.A4, 36, 36, 102, 36);

       ByteArrayOutputStream baos = new ByteArrayOutputStream();

       try {
           
           if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItFondosEuropa())) {
               siFondosEuropeos = Boolean.TRUE;

               // obtenemos la imagen fica de union europea
               List<ImagenDTO> lstLogosEuropeos = this.imagenService
                       .buscarLogosEuropeos(convocatoriaDTOIn.getIdConvocatoria());

               for (ImagenDTO elemento : lstLogosEuropeos) {
                   if (Objects.nonNull(elemento.getCdFica())
                           && StringUtils.isNotBlank(elemento.getCdFica())) {
                       ficheroRecuperar = this.ficaService
                               .recuperar(Integer.valueOf(elemento.getCdFica()));

                       if (ficheroRecuperar != null) {
                           lstLogo.put("ImgLogo" + i, ficheroRecuperar.getDatosFichero());
                           i++;
                       }
                   }
               }

           }
           if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdConvocatoria())) {

              if (Objects.nonNull(impresoDTO.getImagenLogoDTO().getCdFica())
                       && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica())) {
                   // obtenemos la imagen fica del organismo

                   ficheroRecuperar = this.ficaService
                           .recuperar(Integer.valueOf(impresoDTO.getImagenLogoDTO().getCdFica()));
                   logoOrganismo = ficheroRecuperar.getDatosFichero();

               }

               
           }
           String strConsejeria = this.unidadAdminService.obtenerConsejeria();

           String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

               PdfWriter writer = PdfWriter.getInstance(document, baos);
               writer.createXmpMetadata();
               writer.setTagged();
               writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                       strConsejeria, strDireccion, Boolean.TRUE, lstLogo, logoOrganismo));
               document.open();

               Boolean agregarEspacioLogos = !lstLogo.isEmpty() || Objects.nonNull(logoOrganismo);
               List<BloqueDTO> lstBloques =null;
               if(impresoDTO != null) {
                   informeHelperPdfService.agregarTitulo(document, impresoDTO.getDsDescripcion(),
                           convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
                   lstBloques = impresoDTO.getLstBloqueDTO();
               }else {
                   informeHelperPdfService.agregarTitulo(document, "",
                           convocatoriaDTO.getCdTitulo(),"",dto.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);
               }

               this.agregarNumeroExpediente(dto, document);
               
               this.crearInformeBloquesSubsanacionReq(dto,lstBloques, writer, document, convocatoriaDTO, mapaDatos,listExpedientesParticipantes);

               if (Boolean.TRUE.equals(siFondosEuropeos)) {
                   informeHelperPdfService.agregarTextoFondosEuropeos(document);
               }
               informeHelperPdfService.agregarLugarFecha(document);
               informeHelperPdfService.agregarHuellaDigital(document);
               

               // Se agrega filas vacías para evitar que lugarFecha se solape
               // con el Destinatario
               informeHelperPdfService.agregarFilaVacio(document, 1);
               informeHelperPdfService.agregarFilaVacio(document, 1);

               // si es la ultima pagina mostramos el destinatario
               if (writer.getCurrentPageNumber() == writer.getPageNumber()) {
                   informeHelperPdfService.agregarDestinatario(writer, document, convocatoriaDTO,strConsejeria);
               }

               document.close();

               bytesSalida = baos.toByteArray();
          

       } catch (DocumentException e) {
           throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

       }

       return bytesSalida;
   }
   
   private void crearInformeBloquesSubsanacionReq(ExpedienteDTO dto,List<BloqueDTO> lstBloques, PdfWriter writer,
           Document document, ConvocatoriaDTO convocatoriaDTO, Map<String, String> mapaDatos,
           List<ExpedienteDTO> listExpedientesParticipantes)
           throws ServiceException {

       try {
           //Agregamos bloque con la informacion del solicitante
           if(dto != null) {
               this.agregarBloqueDatosInteresado(document, dto, null);
           }

           // como obtenemos la lista total de bloques a traves de la
           // consulta de impreso debemos filtrar los registros que no
           // estan de baja
          
           if (lstBloques != null && !lstBloques.isEmpty()) {
               lstBloques = lstBloques.stream().filter(p -> Objects.isNull(p.getFcBajaFila()))
                       .collect(Collectors.toList());
               // Ordenamos los bloques por nmOrden
               lstBloques.sort(Comparator.comparing(BloqueDTO::getNmOrden));

               for (BloqueDTO bloque : lstBloques) {

                   // como obtenemos la lista total de bloques a traves de
                   // la consulta de impreso debemos filtrar los registros
                   // que no estan de baja
                   if (Objects.nonNull(bloque.getLstBloqueComponenteDTO())) {
                       List<BloqueComponenteDTO> lstBloqueComponente = bloque
                               .getLstBloqueComponenteDTO().stream()
                               .filter(p -> Objects.isNull(p.getFcBajaFila()))
                               .collect(Collectors.toList());
                       bloque.setLstBloqueComponenteDTO(lstBloqueComponente);
                   }

                   if (Objects.nonNull(bloque.getLstBloqueComponentesFilaDTO())) {
                       List<BloqueComponenteDTO> lstBloqueComponenteFila = bloque
                               .getLstBloqueComponentesFilaDTO().stream()
                               .filter(p -> Objects.isNull(p.getFcBajaFila()))
                               .collect(Collectors.toList());
                       bloque.setLstBloqueComponentesFilaDTO(lstBloqueComponenteFila);
                   }

                   // Si no es de participante
                   if (Boolean.FALSE.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Formatos.ID_PARTICIPANTES)
                           .equals(bloque.getIdFormato().getIdValor()))) {
                       informeHelperPdfService.agregarBloque(writer, document, bloque, convocatoriaDTO,
                               mapaDatos,dto,null);
                   }
               }
           }
           
           if(dto != null && dto.getConvocatoriaActDTO() != null) {
               this.agregarBloqueParticipantesSubsanacionReq(document,dto.getConvocatoriaActDTO().getIdActor(),listExpedientesParticipantes); 
           }else {
               throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                       Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
           }

       } catch (DocumentException e) {
           throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

       }
   }
   
   private void agregarBloqueParticipantesSubsanacionReq(Document document, Integer idActor,
           List<ExpedienteDTO> listExpedientesParticipantes)
           throws ServiceException {
       try {
           // Obtenemos el número máximo de participantes permitidos
           ConvocatoriaAct convocatoriaAct = this.convocatoriaActDAO
                   .find(idActor);

           informeHelperPdfService.agregarBloqueParticipantesSubsanacionReq(document, convocatoriaAct,listExpedientesParticipantes);

       } catch (DocumentException e) {
           log.error("Error al agregar el bloque de participantes ", e);
           throw new ServiceException(e);
       }
   }
   
   private void agregarBloqueDatosOrganismoSolicitante(Document document, ExpedienteDTO expediente, String titulo1)
           throws ServiceException {
        try {
           
           String localidad = "";
           if(expediente != null && expediente.getInteresadoDTO() != null && 
                   expediente.getInteresadoDTO().getCdProvincia() != null && 
                   expediente.getInteresadoDTO().getCdLocalidad() != null) {
               try {
                   localidad = obtenerDescripcionMunicipio(expediente.getInteresadoDTO().getCdProvincia(), expediente.getInteresadoDTO().getCdLocalidad());
                   expediente.getInteresadoDTO().setDsLocalidad(localidad);
               } catch (ServiceException e) {
                   log.error("Error obteniendo localidad");
                   expediente.getInteresadoDTO().setDsLocalidad("");
               }
           }
           
           if(expediente != null && expediente.getInteresadoDTO() != null && 
                   expediente.getInteresadoDTO().getCdProvincia() != null) {
               try {
                   String provincia = obtenerDescripcionProvincia(expediente.getInteresadoDTO().getCdProvincia());
                   expediente.getInteresadoDTO().setDsProvincia(provincia);
               } catch (ServiceException e) {
                   log.error("Error obteniendo provincia");
                   expediente.getInteresadoDTO().setDsProvincia("");
               }
           }
        
           informeHelperPdfService.agregarBloqueDatosOrganismoSolicitante(document, expediente, titulo1);
           
        } catch (DocumentException e) {
           log.error("Error al agregar el bloque de datos Organismo Solicitante ", e);
           throw new ServiceException(e);
        }
}
   
   private void agregarBloqueDatosRepresentanteSolicitante(Document document, ExpedienteDTO expediente, String titulo)
           throws ServiceException {
        try {
            Solicitud solicitud ;
            RepresentanteJurDTO representanteJurDTO = new RepresentanteJurDTO();
            // Obtenemos el representante jurídico del expediente
            if(expediente != null && expediente.getInteresadoDTO() != null  && expediente.getRepresentanteLegal() != null && expediente.getRepresentanteLegal().getIdInteresado() != null) {
                representanteJurDTO.setInteresadoDTO(expediente.getRepresentanteLegal());
                informeHelperPdfService.agregarBloqueDatosRepresentanteLegalSol(document, representanteJurDTO,titulo);
            }else if(expediente != null && expediente.getInteresadoDTO() != null ) {
                
                if(expediente.getIdSolicitud() != null) {

                    solicitud = this.solicitudDAO.find(expediente.getIdSolicitud());
                    
                    representanteJurDTO = this.representanteJurService
                            .obtenerPorInteresado(null,
                                    solicitud.getInteresado().getCdNumIden());
                }
                
                if(representanteJurDTO!= null && representanteJurDTO.getIdRepresentanteJur() != null) {
                    representanteJurDTO.setInteresadoDTO(expediente.getRepresentanteLegal());
                }else {
                    representanteJurDTO = new RepresentanteJurDTO();
                    representanteJurDTO.setInteresadoDTO(expediente.getRepresentanteLegal());
                    representanteJurDTO.getEntidadDTO().setInteresadoDTO(expediente.getInteresadoDTO());
                }

               informeHelperPdfService.agregarBloqueDatosRepresentanteLegalSol(document, representanteJurDTO,titulo);
            }else {
                log.error("Error al agregar el bloque de datos el representante jurídico del expediente ");
                
            }
            
           
        } catch (DocumentException e) {
           log.error("Error al agregar el bloque de datos el representante jurídico del expediente ", e);
           throw new ServiceException(e);
        }
   }
   
   private List<ExpedienteDTO> getListaExpedienteParticipantesOrganismo(ExpedienteDTO expediente) throws ServiceException{
       
       List<ExpedienteDTO> listResponseOut = new ArrayList<>(Lists.transform(this.expedienteDAO.listExpedientePorExpedientePadre(expediente.getIdSolicitud(), expediente.getIdExpediente()), ExpedienteToDTO.INSTANCE));
       
       if(!listResponseOut.isEmpty()) {
           for (ExpedienteDTO dto:listResponseOut) {
               
               ImpresoDTO impresoDTO = null;
               if(dto.getImpresoDTO() != null && dto.getImpresoDTO().getIdImpreso() != null) {
                   impresoDTO = this.buscarId(dto.getImpresoDTO().getIdImpreso());
               }else {
                   List<Integer> tipoImpreso = new ArrayList<>();
                   if(Boolean.TRUE.equals(dto.getEsExpedienteSolicitante())) {
                       tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
                   }else {
                       tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
                   }
                   List<Impreso> lstImpresoDTO = this.buscarPorExpedienteTipo(dto.getIdExpediente(), tipoImpreso);
                   if(lstImpresoDTO != null && !lstImpresoDTO.isEmpty()) {
                       impresoDTO = ImpresoToDTO.INSTANCE.apply(lstImpresoDTO.get(0));
                   }
               }
               
               if (Boolean.TRUE.equals(dto.getConvocatoriaActDTO().getItCumplimentaFormulario())) {
                   // Agregamos los datos del impreso recuperado
                   dto.setImpresoDTO(impresoDTO);
                   

                   // Quitamos los registros de baja
                   List<BloqueDTO> lstBloqueDTO = dto.getImpresoDTO().getLstBloqueDTO().stream()
                           .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());

                   // Ordenamos por nmOrden
                   lstBloqueDTO.sort(Comparator.comparing(BloqueDTO::getNmOrden));

                   // Agregamos los datos del bloque procesados
                   dto.getImpresoDTO().setLstBloqueDTO(lstBloqueDTO);

                   // Rellenamos los valores de los componentes.
                   for (BloqueDTO bloqueDTO : dto.getImpresoDTO().getLstBloqueDTO()) {

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
                       if ( Boolean.TRUE.equals(bloqueDTO.getEsTipoPresupuestoPresentado())) {

                           for (BloqueComponenteDTO bloqueComponenteDTO : bloqueDTO
                                   .getLstBloqueComponenteDTO()) {
                              
                               List<DatoDTO> lstDatoDTO = this.datoService.obtenerPorExpediente(dto.getIdExpediente(),
                                       bloqueComponenteDTO.getIdBloqueComponente());

                               // Obtenemos los datos relativos a este bloque
                               List<DatoDTO> datosBloqueComponente = lstDatoDTO.stream()
                                       .filter(x -> x.getIdBloqueComponente()
                                               .equals(bloqueComponenteDTO.getIdBloqueComponente())
                                               && Objects.nonNull(x.getNmFila()))
                                       .sorted(Comparator.comparing(DatoDTO::getNmFila)).collect(Collectors.toList());

                               // Quitamos todos los registros que estan de baja
                               datosBloqueComponente = datosBloqueComponente.stream()
                                       .filter(f -> Objects.isNull(f.getFcBajaFila())).collect(Collectors.toList());
                               
                               bloqueComponenteDTO.getDatos().add(new DatoDTO());
                               
                               if (Boolean.FALSE.equals(datosBloqueComponente.isEmpty()) &&  Boolean.TRUE.equals(bloqueComponenteDTO.getItFila())) {
                                  // Guardamos los datos en el bloqueComponente
                                   bloqueComponenteDTO.setDatos(datosBloqueComponente);
                               }
                       }
                   }
               }
            }
           }
         
       }
       
       return listResponseOut;
   }
   
   private void crearInformeBloquesSolicitudParticipantes(
           Document document, Map<String, String> mapaDatos,ExpedienteDTO dto, String titulo)
           throws ServiceException {
       List<ExpedienteDTO> listaParticipantes = new ArrayList<>();
       try {
        // Obtenemos el número máximo de participantes permitidos
           
           if(dto.getConvocatoriaActDTO().getIdActor() != null) {
               
               ConvocatoriaAct convocatoriaAct = this.convocatoriaActDAO
                       .find(dto.getConvocatoriaActDTO().getIdActor());
               
               if (convocatoriaAct == null) {
                   throw new ServiceException("No se ha encontrado la convocatoria actor: "
                           + dto.getConvocatoriaActDTO().getIdActor());
               }
               
               listaParticipantes= this.getListaExpedienteParticipantesOrganismo(dto);
           }
           
           informeHelperPdfService.agregarBloqueParticipantesOrganismo(document,listaParticipantes,titulo);
           

       } catch (DocumentException e) {
           throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

       }
   }
   
   private void agregarBloqueMultiregistro(PdfWriter writer, Document document, BloqueDTO bloque,
            ExpedienteDTO dto, ConvocatoriaDTO convocatoriaDTO)  throws ServiceException  {

       List<BloqueDTO> listaSubbloqueDTO = new ArrayList<>();

       try {
           if (dto != null && dto.getImpresoDTO() != null &&
                   dto.getImpresoDTO().getLstBloqueDTO() != null) {
               listaSubbloqueDTO =
                       dto.getImpresoDTO()
                           .getLstBloqueDTO()
                           .stream()
                           .filter(
                               subBloque ->
                                   (
                                       subBloque
                                           .getIdBloquePadre() !=
                                               null &&
                                       subBloque
                                           .getIdBloquePadre()
                                           .equals(bloque.getIdBloque())
                                   ) ||
                                   subBloque
                                       .getIdBloque()
                                       .equals(
                                           bloque.getIdBloque()
                                       )
                           )
                           .filter(subBloque ->Objects.isNull(subBloque.getFcBajaFila()))
                           .sorted(
                               Comparator.comparing(
                                       BloqueDTO::getNmOrden,
                                       Comparator.nullsLast(Comparator.naturalOrder()))
                           )
                           .collect(Collectors.toList());

               for (BloqueDTO bloqueDTO : listaSubbloqueDTO) {
                   List<BloqueComponenteDTO>
                       listaComponentes =
                           bloqueDTO
                               .getLstBloqueComponenteDTO()
                               .stream()
                               .filter(
                                   componente ->
                                       Objects.isNull(
                                           componente
                                               .getFcBajaFila()
                                       )
                               )
                               .sorted(
                                   Comparator.comparing(
                                       BloqueComponenteDTO
                                           ::getNmOrden,
                                       Comparator.nullsLast(
                                           Comparator
                                               .naturalOrder())))
                               .collect(Collectors.toList());

                   bloqueDTO.setLstBloqueComponenteDTO(listaComponentes);

                   for (BloqueComponenteDTO bloqueComponenteDTO : listaComponentes) {
                       if (bloqueComponenteDTO.getDatos() != null &&
                               !bloqueComponenteDTO.getDatos().isEmpty()) {
                           this.datoService
                               .rellenarDatosMultiregistro( dto, bloqueDTO, 
                                       bloqueComponenteDTO);
                       }
                   } 
               }
                      
               this.informeHelperPdfService.agregarBloqueMultiregistro(writer, document, bloque,
                       listaSubbloqueDTO, convocatoriaDTO.getFcFinPresentacion());
          }
       } catch (DocumentException e) {
           throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                   Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
       }
   }

   /**
    * Método para eliimnar el actor del cuadro de financiación
    * 
    * @param idActor
    * @throws ServiceException
    */
   @Override
   @Transactional(readOnly = false)
    public void borradoLogico(ImpresoDTO impresoDTO) throws ServiceException {
        try {
            this.getDao().deleteLogico(ImpresoToEntity.INSTANCE.apply(impresoDTO));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

@Override
public byte[] crearInformeJusEco(ImpresoDTO impresoDTO, HashMap<String, String> hamCampos,
        ExpedienteDTO expedienteDTO, List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO)
        throws ServiceException {
   
    Boolean siFondosEuropeos = Boolean.FALSE;
    Map<String, byte[]> lstLogo = new HashMap<>();
    byte[] logoOrganismo = null;
    FicaFichero ficheroRecuperar = null;
    byte[] bytesSalida = null;
    int i = 1;

    try {
        if (Objects.nonNull(impresoDTO) && Objects.nonNull(impresoDTO.getIdConvocatoria())) {

            ConvocatoriaDTO convocatoriaDTO = ConvocatoriaToDTO.INSTANCE
                    .apply(this.convocatoriaDAO.find(impresoDTO.getIdConvocatoria()));
            
            if(expedienteDTO == null){
                expedienteDTO = new ExpedienteDTO();
                expedienteDTO.getConvocatoriaActDTO().setEsActorCoordinador(
                        Constantes.TipoActor.COORDINADOR.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
                expedienteDTO.getConvocatoriaActDTO().setEsActorParticipante(
                        Constantes.TipoActor.PARTICIPANTE.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
                expedienteDTO.getConvocatoriaActDTO().setEsActorSolicitante(
                        Constantes.TipoActor.SOLICITANTE.equals(impresoDTO.getTipoActorDTO().getNmConstante()));
            }

            if (Constantes.AfirmacionNegacion.S.equals(convocatoriaDTO.getItFondosEuropa())) {
                siFondosEuropeos = Boolean.TRUE;

                // obtenemos la imagen fica de union europea
                List<ImagenDTO> lstLogosEuropeos = this.imagenService
                        .buscarLogosEuropeos(impresoDTO.getIdConvocatoria());

                for (ImagenDTO elemento : lstLogosEuropeos) {
                    if (Objects.nonNull(elemento.getCdFica())
                            && StringUtils.isNotBlank(elemento.getCdFica())) {
                        ficheroRecuperar = this.ficaService
                                .recuperar(Integer.valueOf(elemento.getCdFica()));

                        if (ficheroRecuperar != null) {
                            lstLogo.put("ImgLogo" + i, ficheroRecuperar.getDatosFichero());
                            i++;
                        }
                    }
                }

            }

            if (Objects.nonNull(impresoDTO.getImagenLogoDTO().getCdFica())
                    && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica())) {
                // obtenemos la imagen fica del organismo
                try {
                    ficheroRecuperar = this.ficaService
                            .recuperar(Integer.valueOf(impresoDTO.getImagenLogoDTO().getCdFica()));
                    logoOrganismo = ficheroRecuperar.getDatosFichero();
                } catch (Exception e) {
                    logoOrganismo = null;
                }

            }

            String strConsejeria = this.unidadAdminService.obtenerConsejeria();

            String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.createXmpMetadata();
            writer.setTagged();
            
            if(impresoDTO.getTipoDTO() != null && impresoDTO.getTipoDTO().getIdValor() != null
                    && impresoDTO.getTipoDTO().getIdValor().equals(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO))) {
                //Si se trata formulario complementario no se añade registro de entrada
                writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                        strConsejeria, strDireccion, Boolean.FALSE, lstLogo, logoOrganismo));
            }else {
                writer.setPageEvent(new HeaderFooterPdfEvent(impresoDTO, siFondosEuropeos,
                        strConsejeria, strDireccion, Boolean.TRUE, lstLogo, logoOrganismo)); //DEVOLVER TRUE
            }
            document.open();

            Boolean agregarEspacioLogos = !lstLogo.isEmpty() || Objects.nonNull(logoOrganismo);
            
            String codigoImpreso= "Código: ";
            if(impresoDTO != null && impresoDTO.getCdIdentificacion() != null &&impresoDTO.getCdIdentificacion().indexOf("_") != -1 ) {
                codigoImpreso+= impresoDTO.getCdIdentificacion().substring(0,impresoDTO.getCdIdentificacion().indexOf("_"));
            }else if(impresoDTO != null && impresoDTO.getCdIdentificacion() != null  ) {
                codigoImpreso+= impresoDTO.getCdIdentificacion();
            }

            informeHelperPdfService.agregarTitulo(document, codigoImpreso,
                    convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),expedienteDTO.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);

           
            Boolean esPF= Boolean.TRUE;
            if(impresoDTO.getConvocatoriaActDTO() != null && impresoDTO.getConvocatoriaActDTO().getIdentificacionDTO() != null) {
                esPF = Constantes.Identificacion.FISICA.equals(impresoDTO.getConvocatoriaActDTO().getIdentificacionDTO().getNmConstante());
            }
            
            
            
          
            // Si es física
            if (Boolean.TRUE.equals(esPF)) {
               String titulo1="Datos de la persona solicitante";
                this.agregarBloqueDatosInteresado(document, expedienteDTO, titulo1);

                String titulo2="Datos del representante legal de la persona solicitante";
                this.agregarBloqueDatosRepresentanteSolicitante(document, expedienteDTO,titulo2);

               
                if(!listExpedienteJusEcoDTO.isEmpty()) {
                    String titulo = "Justificación económica";
                    informeHelperPdfService.crearInformeJustificacionEco(document,  listExpedienteJusEcoDTO,expedienteDTO,titulo,convocatoriaDTO);
                }
               
            }else {
                String titulo1="Datos de la entidad solicitante";
                this.agregarBloqueDatosOrganismoSolicitante(document, expedienteDTO, titulo1);

                String titulo2="Datos del representante legal de la entidad solicitante";

                this.agregarBloqueDatosRepresentanteSolicitante(document, expedienteDTO,titulo2);
                String titulo = "Justificación económica";
                informeHelperPdfService.crearInformeJustificacionEco(document,listExpedienteJusEcoDTO,expedienteDTO,titulo,convocatoriaDTO);
              
            }
            
            if (Boolean.TRUE.equals(siFondosEuropeos)) {
                informeHelperPdfService.agregarTextoFondosEuropeos(document);
            }
            informeHelperPdfService.agregarLugarFecha(document);
            informeHelperPdfService.agregarHuellaDigital(document);
            

            // Se agrega filas vacías para evitar que lugarFecha se solape
            // con el Destinatario
            informeHelperPdfService.agregarFilaVacio(document, 1);
            //informeHelperPdfService.agregarFilaVacio(document, 1);

            // si es la ultima pagina mostramos el destinatario
            if (writer.getCurrentPageNumber() == writer.getPageNumber()) {
                informeHelperPdfService.agregarDestinatario(writer, document, convocatoriaDTO,strConsejeria);
            }
            
            //VMM 05/06/25 MD-1633: añadimos una última pagina al pdf con la informacion de proteccion de datos
            if (convocatoriaDTO.getInformacionRgpd() != null && !convocatoriaDTO.getInformacionRgpd().trim().isEmpty()) {
                
                document.newPage();
                
                //añadir detras de cada cierre de parrafo (</p>) un salto de linea                 
                this.informeHelperPdfService.agregarTextoEnriquecido(document,
                        "",     // El subtítulo o encabezado
                        convocatoriaDTO.getInformacionRgpd(),     // El HTML guardado como CLOB
                        5,                      // Ancho de la primera columna
                        true                     // activar el modo HTML
                    );
            }

            document.close();

            bytesSalida = baos.toByteArray();
        }

    } catch (DocumentException e) {
        throw new ServiceException(Constantes.Error.ERROR_CREAR_INFORME, null,
                Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);

    }

    return bytesSalida;
}

    @Override
    public List<Impreso> buscarPorConvocatoriaTipoImpreso(Integer idConvocatoria, Integer idTipoImpreso,
            Integer idActor) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TIPO_ID_VALOR, idTipoImpreso,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_ACT_IDACTOR, idActor,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return this.find(filtros, null);
    }
    
    

    @Override
    public List<ImpresoDTO> buscarPorConvTipoImpreso(Integer idConvocatoria,
            List<Integer> lstTipoImpreso) throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        if (Objects.nonNull(lstTipoImpreso) && !lstTipoImpreso.isEmpty()) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_IDVALOR,
                    lstTipoImpreso, Operador.IN));
        }
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return new ArrayList<>(Lists.transform(this.find(filtros, null), ImpresoToDTO.INSTANCE));
    }

}
