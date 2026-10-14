package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.dao.ConvocatoriaDAO;
import org.madrid.saii.lib.dao.ConvocatoriaTipoDocDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaTipoDoc;
import org.madrid.saii.lib.dto.ConvocatoriaTipoDocDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaTipoDocToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaTipoDocToEntity;
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

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaTip
 *
 * @author everis
 */
@Service("convocatoriaTipoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaTipoDocServiceImpl
        extends BaseServiceImpl<ConvocatoriaTipoDoc, Integer, ConvocatoriaTipoDocDAO>
        implements ConvocatoriaTipoDocService {

    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaTipDAO
     */
    @Autowired
    public ConvocatoriaTipoDocServiceImpl(
            @Qualifier("convocatoriaTipoDocDAO") ConvocatoriaTipoDocDAO convocatoriaTipoDocDAO) {
        super();
        this.setDao(convocatoriaTipoDocDAO);
    }

    /** Repositorio CatalogosValoresDAO */
    @Autowired
    @Qualifier("catalogosValoresDAO")
    private CatalogosValoresDAO catalogosValoresDAO;

    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaDAO")
    private ConvocatoriaDAO convocatoriaDAO;
    
    /** Repositorio ConvocatoriaDAO */
    @Autowired
    @Qualifier("convocatoriaTipoDocDAO")
    private ConvocatoriaTipoDocDAO convocatoriaTipoDocDAO;

    /**
     *
     */
    @Override
    public List<ConvocatoriaTipoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaTipoDocToDTO.INSTANCE));
    }
    
    /**
     * Buscar id.
     *
     * @param idComponente the id ConvocatoriaTipoDocDTO
     * @return the ConvocatoriaTipoDocDTO DTO
     * @throws ServiceException the service exception
     */

    @Override
    public ConvocatoriaTipoDocDTO buscarId(Integer idConvocatoriTipoDoc) throws ServiceException {

        ConvocatoriaTipoDoc convocatoriTipoDoc = this.find(idConvocatoriTipoDoc);
        return ConvocatoriaTipoDocToDTO.INSTANCE.apply(convocatoriTipoDoc);
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaTipoDocDTO agregar(Integer idConvocatoria, Integer idTipoDocumento,
            Integer idTramite) throws ServiceException {

        ConvocatoriaTipoDoc convTipDoc = new ConvocatoriaTipoDoc();

        ConvocatoriaTipoDoc convTipDocExist = this.buscarIdConvocatoriaTipoDoc(idConvocatoria,
                idTipoDocumento, idTramite);

        if (convTipDocExist == null || convTipDocExist.getIdConvocatoriaTipoDoc() == null) {

            CatalogosValores tipoDocumento = this.catalogosValoresDAO.find(idTipoDocumento);
            CatalogosValores tipoTramite = this.catalogosValoresDAO.find(idTramite);

            if (tipoDocumento != null && tipoTramite != null) {
                convTipDoc.setTipoDocumento(tipoDocumento);
                convTipDoc.setTramite(tipoTramite);
                convTipDoc.setIdConvocatoria(idConvocatoria);
                convTipDoc = this.insertOrUpdate(convTipDoc);
            }

        } else if (convTipDocExist.getFcBajaFila() != null) {
            convTipDocExist.setCdUsuBaja(null);
            convTipDocExist.setFcBajaFila(null);
            convTipDoc = this.insertOrUpdate(convTipDocExist);
            
        } else if (convTipDocExist.getFcBajaFila() == null) {
            //si el tipo de doc de alegaciones existe, no damos error, simplemente no le volvemos a crear
//            throw new ServiceException(Constantes.Error.ERROR_TIPO_DOCUMENTO_DUPLICADO, null,
//                    Constantes.Error.MSG_WARNING);
        }
        return ConvocatoriaTipoDocToDTO.INSTANCE.apply(convTipDoc);
    }

    /**
     * Buscar por Id convocatoria, id tipo de documento y id tramite
     */
    private ConvocatoriaTipoDoc buscarIdConvocatoriaTipoDoc(final Integer idConvocatoria,
            final Integer idTipoDocumento, Integer idTramite) throws ServiceException {

        ConvocatoriaTipoDoc convocatoriaTipoDoc = new ConvocatoriaTipoDoc();

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        List<ConvocatoriaTipoDoc> lstConvocatoriaTipoDoc = new ArrayList<>(
                this.find(filtros, null));

        if (!lstConvocatoriaTipoDoc.isEmpty()) {
            convocatoriaTipoDoc = lstConvocatoriaTipoDoc.get(0);
        }
        return convocatoriaTipoDoc;
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaTipoDocDTO> buscarPorConvocatoria(final Integer idConvocatoria)
            throws ServiceException {
        
        List<ConvocatoriaTipoDocDTO> lstConvocatoriaTip;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        List<Integer> lstTramitesPermitidos = new ArrayList<>();
        lstTramitesPermitidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION));
        lstTramitesPermitidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA));
        lstTramitesPermitidos.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, lstTramitesPermitidos,
                Operador.IN));

        lstConvocatoriaTip = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaTipoDocToDTO.INSTANCE));

        if (lstConvocatoriaTip.isEmpty()) {
            lstConvocatoriaTip = new ArrayList<>();
        }
        return lstConvocatoriaTip;
    }

    /**
    *
    */
    @Override
    public List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaTipoDocumento(
            final Integer idConvocatoria, final Integer idTipoDocumento) throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_TIPO_DOCUMENTO,
                idTipoDocumento, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaTipoDocToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaTipoDocDTO guardar(ConvocatoriaTipoDocDTO convocatoriaTipoDocDTO)
            throws ServiceException {
        return ConvocatoriaTipoDocToDTO.INSTANCE.apply(this.insertOrUpdate(
                ConvocatoriaTipoDocToEntity.INSTANCE.apply(convocatoriaTipoDocDTO)));
    }

    @Override
    public List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaTiposDocumentos(Integer idConvocatoria,
            List<Integer> idTipoDocumentos) throws ServiceException {
        
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_ID_TIPO_DOCUMENTO,
                idTipoDocumentos, Operador.IN));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaTipoDocToDTO.INSTANCE));
        
    }
    
    /**
     * 
     */
    @Override
    public List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaNoEnImpresos(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaTipoDoc> lstConvocatoriaTipoDoc = this.convocatoriaTipoDocDAO
                .buscarNoConfiguradasImpresos(idConvocatoria);
        return new ArrayList<>(
                Lists.transform(lstConvocatoriaTipoDoc, ConvocatoriaTipoDocToDTO.INSTANCE));
    }

    @Override
    public List<ConvocatoriaTipoDocDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaTipoDocDTO> lstConvocatoriaTip;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        lstConvocatoriaTip = new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaTipoDocToDTO.INSTANCE));

        if (lstConvocatoriaTip.isEmpty()) {
            lstConvocatoriaTip = new ArrayList<>();
        }
        return lstConvocatoriaTip;
    }
    
    /**
    *
    */
   @Override
   public List<ConvocatoriaTipoDocDTO> buscarPorConvocatoriaNoFiltrado(final Integer idConvocatoria)
           throws ServiceException {
       
       List<ConvocatoriaTipoDocDTO> lstConvocatoriaTip;
       List<AtlasFilter> filtros = new ArrayList<>();
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
               Operador.EQUALS));
       filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
       
       lstConvocatoriaTip = new ArrayList<>(
               Lists.transform(this.find(filtros, null), ConvocatoriaTipoDocToDTO.INSTANCE));

       if (lstConvocatoriaTip.isEmpty()) {
           lstConvocatoriaTip = new ArrayList<>();
       }
       return lstConvocatoriaTip;
   }

}
