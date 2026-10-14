package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaConstantes;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDocEvalDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalHistDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaDocEvalToEntity;
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
import atlas.fica.services.FicaService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaDocEval
 *
 * @author everis
 */
@Service("convocatoriaDocEvalService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConvocatoriaDocEvalServiceImpl
        extends BaseServiceImpl<ConvocatoriaDocEval, Integer, ConvocatoriaDocEvalDAO>
        implements ConvocatoriaDocEvalService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaDocDAO
     */
    @Autowired
    public ConvocatoriaDocEvalServiceImpl(@Qualifier("convocatoriaDocEvalDAO")
    ConvocatoriaDocEvalDAO convocatoriaDocEvalDAO) {
        super();
        this.setDao(convocatoriaDocEvalDAO);
    }

    /**
     * Constructor Vacio
     */
    public ConvocatoriaDocEvalServiceImpl() {
        super();
    }

    /** Servicio de Fica. */
    @Autowired
    @Qualifier("ficaService")
    @Setter
    private FicaService ficaService;

    /** Repositorio ConvocatoriaService */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /** DAO **/
    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;

    /** Repositorio ExpedienteDAO */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;
    
    /** Repositorio ConvocatoriaDocEvalHistService */
    @Autowired
    @Qualifier("convocatoriaDocEvalHistService")
    private ConvocatoriaDocEvalHistService convocatoriaDocEvalHistService;

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaDocEvalToDTO.INSTANCE));

    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaDocEvalDTO guardar(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException {

        if (Objects.isNull(convocatoriaDocEvalDTO.getIdConvocatoria()) || Objects.isNull(convocatoriaDocEvalDTO.getPersona())
                || Objects.isNull(convocatoriaDocEvalDTO.getIdTipoDocumento()) || Objects.isNull(convocatoriaDocEvalDTO.getItCoorEval()) ) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        
        CatalogosValoresDTO estado = new CatalogosValoresDTO();
        //apaño provisional para desarrollo de Malika que colisionaba con el de Vero
        if (convocatoriaDocEvalDTO.getEstado() == null || 
                (convocatoriaDocEvalDTO.getEstado() != null && convocatoriaDocEvalDTO.getEstado().getIdValor() == null)) {
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
            convocatoriaDocEvalDTO.setEstado(estado);
        }else {
            estado = convocatoriaDocEvalDTO.getEstado();
        }
        
        if(convocatoriaDocEvalDTO.getIdDocumento() != null) {
           ConvocatoriaDocEval convocatoriaDocEval = this
                   .insertOrUpdate(ConvocatoriaDocEvalToEntity.INSTANCE.apply(convocatoriaDocEvalDTO));
           
           ConvocatoriaDocEvalDTO convocatoriaDocEvalNewDTO= ConvocatoriaDocEvalToDTO.INSTANCE.apply(convocatoriaDocEval);
           this.guardarConvocatoriaDocEvalHist(convocatoriaDocEvalNewDTO);
           return convocatoriaDocEvalNewDTO;
       
       }else {
           List<AtlasFilter> filtros = new ArrayList<>();
           if ( convocatoriaDocEvalDTO.getIdConvocatoria() != null) {
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, convocatoriaDocEvalDTO.getIdConvocatoria(),
                       Operador.EQUALS));
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, convocatoriaDocEvalDTO.getPersona().getIdInteresado(),
                       Operador.EQUALS));
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, convocatoriaDocEvalDTO.getItCoorEval(),
                       Operador.EQUALS));
               filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO, convocatoriaDocEvalDTO.getIdTipoDocumento().getIdValor(),
                       Operador.EQUALS));
               
           }

          List<ConvocatoriaDocEvalDTO> lstConvocatoriaDocEvalDTO = new ArrayList<>(
                   Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalToDTO.INSTANCE));
          if(!lstConvocatoriaDocEvalDTO.isEmpty()) {
              ConvocatoriaDocEval convocatoriaDocEvalExist= this.find(lstConvocatoriaDocEvalDTO.get(0).getIdDocumento()) ;
              convocatoriaDocEvalExist.setCdFica(convocatoriaDocEvalDTO.getCdFica());
              convocatoriaDocEvalExist.setDsNombreFichero(convocatoriaDocEvalDTO.getDsNombreFichero());
              convocatoriaDocEvalExist.setCdUsuModif(convocatoriaDocEvalDTO.getCdUsuAlta());
              convocatoriaDocEvalExist.setTlCausaDescripcion(convocatoriaDocEvalDTO.getTlCausaDescripcion());
              convocatoriaDocEvalExist.setFcModifFila(new Date());
              convocatoriaDocEvalExist.setFcBajaFila(null);
              convocatoriaDocEvalExist.setCdUsuBaja(null);
              convocatoriaDocEvalExist.setEstado(CatalogosValoresToEntity.INSTANCE.apply(estado));
              ConvocatoriaDocEval convocatoriaDocEvalNew = this.insertOrUpdate(convocatoriaDocEvalExist);
              
              ConvocatoriaDocEvalDTO convocatoriaDocEvalNewDTO= ConvocatoriaDocEvalToDTO.INSTANCE.apply(convocatoriaDocEvalNew);
              this.guardarConvocatoriaDocEvalHist(convocatoriaDocEvalNewDTO);
              return convocatoriaDocEvalNewDTO;
              
          }else {
              ConvocatoriaDocEval convocatoriaDocEval = this
                      .insertOrUpdate(ConvocatoriaDocEvalToEntity.INSTANCE.apply(convocatoriaDocEvalDTO));
                
              ConvocatoriaDocEvalDTO convocatoriaDocEvalNewDTO= ConvocatoriaDocEvalToDTO.INSTANCE.apply(convocatoriaDocEval);
              this.guardarConvocatoriaDocEvalHist(convocatoriaDocEvalNewDTO);
              return convocatoriaDocEvalNewDTO;
          }
           
          
       }
       

    }

    
    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalDTO buscarId(final Integer idDocumentacion) throws ServiceException {

        // Validacion de entrada
        if (idDocumentacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return ConvocatoriaDocEvalToDTO.INSTANCE.apply(this.find(idDocumentacion));

    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalDTO descargar(Integer cdFica) throws ServiceException {

        if (Objects.isNull(cdFica)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO = new ConvocatoriaDocEvalDTO();

        FicaFichero ficaFichero = this.ficaService.recuperar(cdFica);
        convocatoriaDocEvalDTO.setFicaFichero(ficaFichero);
        convocatoriaDocEvalDTO.setContenidoFichero(ficaFichero.getDatosFichero());
        convocatoriaDocEvalDTO.setCdFica(cdFica);
        return convocatoriaDocEvalDTO;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocEvalDTO> obtenerDocumentosConvocatoriaEval(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException {
        List<ConvocatoriaDocEvalDTO> lstConvocatoriaDocEvalDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        if (convocatoriaDocEvalDTO != null && convocatoriaDocEvalDTO.getIdConvocatoria() != null) {
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, convocatoriaDocEvalDTO.getIdConvocatoria(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_PERSONA_IDINTERESADO, convocatoriaDocEvalDTO.getPersona().getIdInteresado(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_ITCOOREVAL, convocatoriaDocEvalDTO.getItCoorEval(),
                    Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        }

        lstConvocatoriaDocEvalDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalToDTO.INSTANCE));

        return lstConvocatoriaDocEvalDTO;
    }

    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalDTO subirDocumentoFica(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException {

        FicaFichero ficheroRecuperar = null;
        FicaFichero fichero;

        // Validacion
        if (convocatoriaDocEvalDTO.getContenidoFichero() == null
                || convocatoriaDocEvalDTO.getFicaMetadatos() == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        if (StringUtils.isBlank(convocatoriaDocEvalDTO.getFicaMetadatos().getExtension())) {
            log.info("El Fichero no tiene extensión.");
            throw new ServiceException("El Fichero no tiene extensión.");
        }

        convocatoriaDocEvalDTO.getFicaMetadatos()
                .setSistemaAlmacenamiento(FicaConstantes.SA_FILESYSTEM);
        if (convocatoriaDocEvalDTO.getCdFica() != null) {
            ficheroRecuperar = this.ficaService.recuperar(convocatoriaDocEvalDTO.getCdFica());
        }
        if (ficheroRecuperar == null) {
            fichero = this.ficaService.guardar(convocatoriaDocEvalDTO.getContenidoFichero(),
                    convocatoriaDocEvalDTO.getFicaMetadatos());
        } else {
            fichero = this.ficaService.actualizar(convocatoriaDocEvalDTO.getCdFica(),
                    convocatoriaDocEvalDTO.getContenidoFichero(),
                    convocatoriaDocEvalDTO.getFicaMetadatos());
        }
        convocatoriaDocEvalDTO.setFicaFichero(fichero);
        convocatoriaDocEvalDTO.setCdFica(fichero.getIdInterno());
        convocatoriaDocEvalDTO.setDsNombreFichero(fichero.getNombreFichero());
        return convocatoriaDocEvalDTO;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaDocEvalDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaDocEvalToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public Boolean existeTipoDocumento(Integer idConvocatoria, Integer idTipoDocumento)
            throws ServiceException {

        if (Objects.isNull(idConvocatoria) || Objects.isNull(idTipoDocumento)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_DOC_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        Integer total = this.getDao().count(filtros);

        return total > 0;
    }

    @Override
    public void guardarConvocatoriaDocEvalHist(ConvocatoriaDocEvalDTO convocatoriaDocEvalDTO)
            throws ServiceException {
        ConvocatoriaDocEvalHistDTO convocatoriaDocEvalHistDTO =  new ConvocatoriaDocEvalHistDTO();
        convocatoriaDocEvalHistDTO.setIdConvocatoriaDocEval(convocatoriaDocEvalDTO.getIdDocumento());
        convocatoriaDocEvalHistDTO.setIdConvocatoria(convocatoriaDocEvalDTO.getIdConvocatoria());
        convocatoriaDocEvalHistDTO.setIdTipoDocumento(convocatoriaDocEvalDTO.getIdTipoDocumento());
        convocatoriaDocEvalHistDTO.setPersona(convocatoriaDocEvalDTO.getPersona());
        convocatoriaDocEvalHistDTO.setCdFica(convocatoriaDocEvalDTO.getCdFica());
        convocatoriaDocEvalHistDTO.setItCoorEval(convocatoriaDocEvalDTO.getItCoorEval());
        convocatoriaDocEvalHistDTO.setDsNombreFichero(convocatoriaDocEvalDTO.getDsNombreFichero());
        convocatoriaDocEvalHistDTO.setCdUsuAlta(convocatoriaDocEvalDTO.getCdUsuAlta());
        convocatoriaDocEvalHistDTO.setFcAltaFila(new Date());
        convocatoriaDocEvalHistDTO.setEstado(convocatoriaDocEvalDTO.getEstado());
        convocatoriaDocEvalHistDTO.setTlCausaDescripcion(convocatoriaDocEvalDTO.getTlCausaDescripcion());
        this.convocatoriaDocEvalHistService.guardar(convocatoriaDocEvalHistDTO);
        
    }

}
