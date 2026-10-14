package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaDocDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaFirDAO;
import org.madrid.saii.lib.dao.ConvocatoriaPlaVerDAO;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaPla;
import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaFirDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaVerDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaDocToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaFirToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaPlaVerToEntity;
import org.madrid.saii.lib.dto.converters.ImagenToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
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
import atlas.core.util.UserUtils;
import atlas.eadm.services.PtfrService;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaPla
 *
 * @author everis
 */
@Service("convocatoriaPlaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaPlaServiceImpl
        extends BaseServiceImpl<ConvocatoriaPla, Integer, ConvocatoriaPlaDAO>
        implements ConvocatoriaPlaService {

    /** catalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;
    
    /** ConvocatoriaDAO **/
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;
    
    /** TipoResolConvocatoria **/
    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;
    
    /** Servicio de Catalogos */
    @Autowired
    @Qualifier("catalogosService")
    private CatalogosService catalogosService;
    
    /** Servicio de Convocatoria Plantilla Documentacion a subsanar. */
    @Autowired
    @Qualifier("convocatoriaPlaDocService")
    private ConvocatoriaPlaDocService convocatoriaPlaDocService;
    
    /** Servicio de Convocatoria Plantilla Firmante */
    @Autowired
    @Qualifier("convocatoriaPlaFirService")
    private ConvocatoriaPlaFirService convocatoriaPlaFirService;
    
    /** Servicio de Convocatoria Plantilla Verificador */
    @Autowired
    @Qualifier("convocatoriaPlaVerService")
    private ConvocatoriaPlaVerService convocatoriaPlaVerService;
    
    /** Repositorio ConvocatoriaPlaDocDAO */
    @Autowired
    @Qualifier("convocatoriaPlaDocDAO")
    private ConvocatoriaPlaDocDAO convocatoriaPlaDocDAO;
    
    /** Repositorio convocatoriaPlaVerDAO */
    @Autowired
    @Qualifier("convocatoriaPlaVerDAO")
    private ConvocatoriaPlaVerDAO convocatoriaPlaVerDAO;
    
    /** Repositorio convocatoriaPlaFirDAO */
    @Autowired
    @Qualifier("convocatoriaPlaFirDAO")
    private ConvocatoriaPlaFirDAO convocatoriaPlaFirDAO;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaPlaDAO
     */
    @Autowired
    public ConvocatoriaPlaServiceImpl(
            @Qualifier("convocatoriaPlaDAO") ConvocatoriaPlaDAO convocatoriaPlaDAO) {
        super();
        this.setDao(convocatoriaPlaDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaPlaServiceImpl() {
        super();
    }

    /** Servicio de Porta Firma. */
    @Autowired
    @Qualifier("ptfrService")
    public PtfrService ptfrService;

    /**
     *
     */
    @Override
    public ConvocatoriaPlaDTO buscarId(Integer idConvocatoriaPla) throws ServiceException {
        return ConvocatoriaPlaToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaPla));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaPlaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaPlaDTO guardar(ConvocatoriaPlaDTO convocatoriaPlaDTO)
            throws ServiceException {
        return ConvocatoriaPlaToDTO.INSTANCE.apply(
                this.insertOrUpdate(ConvocatoriaPlaToEntity.INSTANCE.apply(convocatoriaPlaDTO)));
    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void altaLogica(final ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException {
       convocatoriaPlaDTO.setCdUsuBaja(null);
       convocatoriaPlaDTO.setFcBajaFila(null);
       this.getDao().update(ConvocatoriaPlaToEntity.INSTANCE.apply(convocatoriaPlaDTO));
   }

   /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public void borradoLogico(final ConvocatoriaPlaDTO convocatoriaPlaDTO) throws ServiceException {
       convocatoriaPlaDTO.setCdUsuBaja(UserUtils.getUserName());
       convocatoriaPlaDTO.setFcBajaFila(new Date());
       this.getDao().update(ConvocatoriaPlaToEntity.INSTANCE.apply(convocatoriaPlaDTO));
   }

    /**
     *
     */
    @Override
    public List<ConvocatoriaPlaDTO> obtenerPlantillasPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaPlaDTO> listaDTO = new ArrayList<>();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaPla> lista = this.find(filtros, null);

        if (lista != null && !lista.isEmpty()) {
            for (ConvocatoriaPla plantilla : lista) {
                listaDTO.add(ConvocatoriaPlaToDTO.INSTANCE.apply(plantilla));
            }
        }

        return listaDTO;
    }

    /**
     *
     */
    @Override
    public void nuevaConvocatoria(Convocatoria convocatoria) throws ServiceException {

        //VMM 26/09/24 MD-593: ahora solo cargamos en las comunicaciones las plantillas del gestor (catalogo 38)
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
                
                List<ConvocatoriaPlaDTO> lstConvPla = this.getTramiteConv(convocatoria.getIdConvocatoria(), valorTramite.getIdValor());
                
                // Si no existe y esResolucionIndividual se guarda
                if(lstConvPla.isEmpty() && this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                    
                    ConvocatoriaPla convocatoriaPla = construirConvocatoriaPla(convocatoria, valorTramite, tipoResolModalidad);
                    ConvocatoriaPlaDTO dto = ConvocatoriaPlaToDTO.INSTANCE.apply(convocatoriaPla);
                    this.guardar(dto);
                    
                }
                // Si existe
                else if(!lstConvPla.isEmpty()) {
                    
                    ConvocatoriaPlaDTO dto = lstConvPla.get(0);
                    
                    // Si no esta dado de baja y no es esResolucionIndividual lo damos de baja
                    if(dto.getFcBajaFila() == null && !this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                        this.procesaBorradoLogicoPlantilla(dto);
                    }
                    // Si esta dado de baja y es esResolucionIndividual lo damos de alta
                    else if(dto.getFcBajaFila() != null && this.esResolucionIndividual(cdTipoResolucion, valorTramite.getIdValor())) {
                        this.inicializaConvocatoriaPlaDTO(dto, valorTramite, tipoResolModalidad);
                        dto.setCdUsuBaja(null);
                        dto.setFcBajaFila(null);
                        this.getDao().merge(ConvocatoriaPlaToEntity.INSTANCE.apply(dto));
                    }

                }
            }     
        }
    }
    
    /**
    *
    * @param idConvocatoria
    * @param idTramite
    * @return
    * @throws ServiceException
    */
   private List<ConvocatoriaPlaDTO> getTramiteConv(Integer idConvocatoria, Integer idTramite)
           throws ServiceException {

       List<AtlasFilter> filtros = new ArrayList<>();

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));

       return new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaPlaToDTO.INSTANCE));
   }
   
   /**
    * Metodo para revisar si ppara el tipo de tramite y el tipo de resolucion es indivudial o no
    * @param cdTipoResolucion
    * @param idValor
    * @return
    * @throws ServiceException
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
     * @param tipoResolModalidad
     * @return
     * @throws ServiceException
     */
   private ConvocatoriaPla construirConvocatoriaPla(Convocatoria convocatoria, CatalogosValoresDTO valorTramite, TipoResolModalidad tipoResolModalidad) {
        
        ConvocatoriaPla convocatoriaPla = new ConvocatoriaPla();
        convocatoriaPla.setIdConvocatoria(convocatoria.getIdConvocatoria());
        convocatoriaPla.setTramite(CatalogosValoresToEntity.INSTANCE.apply(valorTramite));
        Imagen imagen = new Imagen();
        imagen.setIdImagen(1);
        convocatoriaPla.setImagen(imagen);

        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_EXCLUSION);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_ADMINISTRATIVA);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_REQUISITOS);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DESISTIMIENTO);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DESISTIMIENTO_PROVISIONAL);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_EXCLUSION_PROV);
        //VMM 26/09/24 MD-593: cambiamos estos dos ultimos tramites por los del gestor, ya que estaban unos del ciudadano
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_CONTRATOS);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_JUSTIFICACION_TECNICA);
            //VMM 30/10/25 MD-1586: añadimos la plantilla de subsanacion jus eco
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_JUSTIFICACION_ECO);
        }else if ( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_PRESUPUESTO_MODIF);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL).equals(valorTramite.getIdValor()) ) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_PROVISIONAL_CONV);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_DEFINITVA_CONV);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA).equals(valorTramite.getIdValor())) {
            convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_ACEPTACION_DESISTIMIENTO_AYUDA);
        }
        
        if (tipoResolModalidad != null 
            && tipoResolModalidad.getCdTipoResolucion() != null
            && Constantes.Convocatoria.RESOL_INDIV.equals(tipoResolModalidad.getCdTipoResolucion())) {
                if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(valorTramite.getIdValor())) {
                    convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_CONCESION_PROVISIONAL);
                } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(valorTramite.getIdValor())) {
                    convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DENEGACION_PROVISIONAL);
                } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(valorTramite.getIdValor())) {
                    convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_CONCESION_DEFINITIVA);
                } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(valorTramite.getIdValor())) {
                    convocatoriaPla.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DENEGACION_DEFINITIVA);
                }
        }
        
        return convocatoriaPla;
        
   }
   
   /**
    * Metodo encargado de la baja logica de la plantilla y las subtablas
    * 
    * @param dto
    * @throws ServiceException
    */
   private void procesaBorradoLogicoPlantilla(ConvocatoriaPlaDTO dto) throws ServiceException {
       
       List<AtlasFilter> filtrosPlantilla = new ArrayList<>();
       
       filtrosPlantilla.add(new AtlasFilter(Constantes.Filtro.FILTRO_ID_CONVOCATORIA_PLA, dto.getIdConvocatoriaPla(), Operador.EQUALS));
       filtrosPlantilla.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       
       // Buscamos los documentos y los damos de baja
       List<ConvocatoriaPlaDocDTO> lstConvocatoriaPlaDoc = this.convocatoriaPlaDocService.buscar(0, -1, filtrosPlantilla, null);
       
       if(!lstConvocatoriaPlaDoc.isEmpty()) {
           
           for(ConvocatoriaPlaDocDTO convPlantDoc : lstConvocatoriaPlaDoc) {
               convPlantDoc.setCdUsuBaja(UserUtils.getUserName());
               convPlantDoc.setFcBajaFila(new Date());
               convocatoriaPlaDocDAO.merge(ConvocatoriaPlaDocToEntity.INSTANCE.apply(convPlantDoc));
           }
           
           // Tenemos que mapear la baja del dto porque posteriormente se hace merge del dto
           // y en el apply se lleva este listado
           if (dto.getLstConvocatoriaPlaDocDTO() != null
                   && !dto.getLstConvocatoriaPlaDocDTO().isEmpty()) {
               
               for (int i = 0; i < dto.getLstConvocatoriaPlaDocDTO().size(); i ++) {
                   if(dto.getLstConvocatoriaPlaDocDTO().get(i).getFcBajaFila() == null) {
                       dto.getLstConvocatoriaPlaDocDTO().get(i).setCdUsuBaja(UserUtils.getUserName());
                       dto.getLstConvocatoriaPlaDocDTO().get(i).setFcBajaFila(new Date());
                   }
                    
               }
               
           }
           
       }
       
       // Buscamos los firmantes y los damos de baja
       List<ConvocatoriaPlaFirDTO> lstConvocatoriaPlaFir = this.convocatoriaPlaFirService.buscar(0, -1, filtrosPlantilla, null);
       
       if(!lstConvocatoriaPlaFir.isEmpty()) {
           
           for(ConvocatoriaPlaFirDTO convPlantFir : lstConvocatoriaPlaFir) {
               convPlantFir.setCdUsuBaja(UserUtils.getUserName());
               convPlantFir.setFcBajaFila(new Date());
               convocatoriaPlaFirDAO.merge(ConvocatoriaPlaFirToEntity.INSTANCE.apply(convPlantFir));
           }
           
           // Tenemos que mapear la baja del dto porque posteriormente se hace merge del dto
           // y en el apply se lleva este listado
           if (dto.getLstConvocatoriaPlaFirDTO() != null
                   && !dto.getLstConvocatoriaPlaFirDTO().isEmpty()) {
               
               for (int i = 0; i < dto.getLstConvocatoriaPlaFirDTO().size(); i ++) {
                   if(dto.getLstConvocatoriaPlaFirDTO().get(i).getFcBajaFila() == null) {
                       dto.getLstConvocatoriaPlaFirDTO().get(i).setCdUsuBaja(UserUtils.getUserName());
                       dto.getLstConvocatoriaPlaFirDTO().get(i).setFcBajaFila(new Date());
                   }
                    
               }
               
           }
           
       }
       
       // Buscamos los verificadores y los damos de baja
       List<ConvocatoriaPlaVerDTO> lstConvocatoriaPlaVer = this.convocatoriaPlaVerService.buscar(0, -1, filtrosPlantilla, null);
       
       if(!lstConvocatoriaPlaVer.isEmpty()) {
           
           for(ConvocatoriaPlaVerDTO convPlantVer : lstConvocatoriaPlaVer) {
               convPlantVer.setCdUsuBaja(UserUtils.getUserName());
               convPlantVer.setFcBajaFila(new Date());
               convocatoriaPlaVerDAO.merge(ConvocatoriaPlaVerToEntity.INSTANCE.apply(convPlantVer));
           }
           
           // Tenemos que mapear la baja del dto porque posteriormente se hace merge del dto
           // y en el apply se lleva este listado
           if (dto.getLstConvocatoriaPlaVerDTO() != null
                   && !dto.getLstConvocatoriaPlaVerDTO().isEmpty()) {
               
               for (int i = 0; i < dto.getLstConvocatoriaPlaVerDTO().size(); i ++) {
                   if(dto.getLstConvocatoriaPlaVerDTO().get(i).getFcBajaFila() == null) {
                       dto.getLstConvocatoriaPlaVerDTO().get(i).setCdUsuBaja(UserUtils.getUserName());
                       dto.getLstConvocatoriaPlaVerDTO().get(i).setFcBajaFila(new Date());
                   }
                    
               }
               
           }
           
       }
       
       dto.setCdUsuBaja(UserUtils.getUserName());
       dto.setFcBajaFila(new Date());
       this.getDao().merge(ConvocatoriaPlaToEntity.INSTANCE.apply(dto));
       
   }
   
   /**
    * Metodo para inicializar el dto como si fuera un alta nueva
    * @param dto
    * @param valorTramite
    * @param tipoResolModalidad
    * @throws ServiceException
    */
   private void inicializaConvocatoriaPlaDTO(ConvocatoriaPlaDTO dto, CatalogosValoresDTO valorTramite, TipoResolModalidad tipoResolModalidad) {
       
       Imagen imagen = new Imagen();
       imagen.setIdImagen(1);
       dto.setImagenDTO(ImagenToDTO.INSTANCE.apply(imagen));

       if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.EXCLUSION).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_EXCLUSION);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_ADMINISTRATIVA);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_REQUISITOS);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DESISTIMIENTO).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DESISTIMIENTO);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_DESISTIMIENTO_PROVISIONAL).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DESISTIMIENTO_PROVISIONAL);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_EXCLUSION_PROVISIONAL).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_EXCLUSION_PROV);
       //VMM 26/09/24 MD-593: cambiamos estos dos ultimos tramites por los del gestor, ya que estaban unos del ciudadano
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_CONTRATO_INICIAL).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_CONTRATOS);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_CIENTIFICA_TECNICA).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_JUSTIFICACION_TECNICA);
           //VMM 30/10/25 MD-1586: añadimos la plantilla de subsanacion jus eco
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_JUSTIFICACION_ECONOMICA).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_SUBSANACION_JUSTIFICACION_ECO);
       }else if ( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_PRESUPUESTO_MODIFICADA_ACEPTADA).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_PRESUPUESTO_MODIF);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_PROVISIONAL).equals(valorTramite.getIdValor()) ) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_PROVISIONAL_CONV);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.RESOLUCION_CONVOCATORIA_DEFINITIVO).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_RESOLUCION_DEFINITVA_CONV);
       } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA).equals(valorTramite.getIdValor())) {
           dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_ACEPTACION_DESISTIMIENTO_AYUDA);
       }
       
       if (tipoResolModalidad != null 
           && tipoResolModalidad.getCdTipoResolucion() != null
           && Constantes.Convocatoria.RESOL_INDIV.equals(tipoResolModalidad.getCdTipoResolucion())) {
               if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_PROVISIONAL).equals(valorTramite.getIdValor())) {
                   dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_CONCESION_PROVISIONAL);
               } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_PROVISIONAL).equals(valorTramite.getIdValor())) {
                   dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DENEGACION_PROVISIONAL);
               } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.CONCESION_DEFINITIVA).equals(valorTramite.getIdValor())) {
                   dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_CONCESION_DEFINITIVA);
               } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.DENEGACION_DEFINITIVA).equals(valorTramite.getIdValor())) {
                   dto.setCdPlantilla(Constantes.TipoDocumentoPtfr.PLANTILLA_DENEGACION_DEFINITIVA);
               }
       }
       
       dto.setDsAsunto(null);
       dto.setDsDescripcion(null);
       dto.setDsDescripcionInf(null);
       
       
   }

   /**
   *
   */
   @Override
   public List<ConvocatoriaPlaDTO> tramiteConvocatoria(Integer idConvocatoria)
           throws ServiceException {

       List<AtlasFilter> filtros = new ArrayList<>();

       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));

       return new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaPlaToDTO.INSTANCE));
   }

}
