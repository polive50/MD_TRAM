package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.CatalogosDAO;
import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.converters.CatalogosToDTO;
import org.madrid.saii.lib.dto.converters.CatalogosToEntity;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
import org.madrid.saii.lib.util.Constantes;
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
 * Implementación del servicio para la clase de dominio Catalogos
 *
 * @author everis
 */
@Service("catalogosService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class CatalogosServiceImpl extends BaseServiceImpl<Catalogos, Integer, CatalogosDAO>
        implements CatalogosService {

    /** catalogosValoresService */
    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param catalogosDAO
     */
    @Autowired
    public CatalogosServiceImpl(@Qualifier("catalogosDAO") CatalogosDAO catalogosDAO) {
        super();
        this.setDao(catalogosDAO);
    }

    /**
     *
     */
    @Override
    public List<CatalogosDTO> obtener() throws ServiceException {
        List<CatalogosDTO> lstCatalogoDTO = new ArrayList<>(
                Lists.transform(this.findAll(), CatalogosToDTO.INSTANCE));

        // Para cada catálogo, rellenamos la lista de los catálogos
        for (CatalogosDTO catalogosDTO : lstCatalogoDTO) {
            catalogosDTO.setListCatalogosValores(
                    this.catalogosValoresService.buscarPorCatalogo(catalogosDTO.getIdCatalogo()));
        }

        return lstCatalogoDTO;
    }

    /**
     *
     */
    @Override
    public CatalogosDTO buscarId(final Integer idCatalogos) throws ServiceException {
        CatalogosDTO catalogoDTO = CatalogosToDTO.INSTANCE.apply(this.find(idCatalogos));

        // Rellenamos la lista de los catálogos
        catalogoDTO.setListCatalogosValores(
                this.catalogosValoresService.buscarPorCatalogo(catalogoDTO.getIdCatalogo()));

        return catalogoDTO;
    }

    /**
     *
     */
    @Override
    public List<CatalogosDTO> buscarPorNombre(final String dsNombre) throws ServiceException {
        List<CatalogosDTO> lstCatalogoDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_DSNOMBRE, dsNombre, Operador.EQUALS));
        lstCatalogoDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, null), CatalogosToDTO.INSTANCE));

        // Para cada catálogo, rellenamos la lista de los catálogos
        for (CatalogosDTO catalogosDTO : lstCatalogoDTO) {
            catalogosDTO.setListCatalogosValores(
                    this.catalogosValoresService.buscarPorCatalogo(catalogosDTO.getIdCatalogo()));
        }

        return lstCatalogoDTO;
    }

    /**
     *
     */
    @Override
    public List<CatalogosDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        List<CatalogosDTO> lstCatalogoDTO = new ArrayList<>(Lists
                .transform(this.find(index, pageSize, filtros, orden), CatalogosToDTO.INSTANCE));
        // Para cada catálogo, rellenamos la lista de los catálogos

        for (CatalogosDTO catalogosDTO : lstCatalogoDTO) {
            catalogosDTO.setListCatalogosValores(
                    this.catalogosValoresService.buscarPorCatalogo(catalogosDTO.getIdCatalogo()));
        }

        return lstCatalogoDTO;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public CatalogosDTO guardar(final CatalogosDTO catalogosDTO) throws ServiceException {
        // Guardamos el catálogo
        CatalogosDTO catalogosSalidaDTO = CatalogosToDTO.INSTANCE
                .apply(this.insertOrUpdate(CatalogosToEntity.INSTANCE.apply(catalogosDTO)));

        // Recorremos la lista de los CatalogosValores del catálogo. Los
        // convertimos e insertamos en el dto de salida.
        for (CatalogosValoresDTO catalogosValoresDTO : catalogosDTO.getListCatalogosValores()) {
            // Asignamos el id del catálogo recién creado
            catalogosValoresDTO.setCatalogoDTO(catalogosSalidaDTO);
            catalogosSalidaDTO.getListCatalogosValores()
                    .add(CatalogosValoresToDTO.INSTANCE
                            .apply(this.catalogosValoresService.insertOrUpdate(
                                    CatalogosValoresToEntity.INSTANCE.apply(catalogosValoresDTO))));
        }

        return catalogosSalidaDTO;
    }
    
    
    @Override
    public List<CatalogosDTO> buscarCatalogosMantenibles() throws ServiceException {
        List<Catalogos> lstCatalogoDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IT_MANTENIBLE, Constantes.AfirmacionNegacion.S, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_DSNOMBRE, true));
       
        lstCatalogoDTO = this.find(0, -1, filtros, ordenamiento);
        
        return new ArrayList<>(Lists.transform(lstCatalogoDTO,
                CatalogosToDTO.INSTANCE));

    }
    
    /**
    *
    */
   @Override
   @Transactional(readOnly = false)
   public CatalogosDTO guardarRevisionEvaluacion(final CatalogosValoresDTO catValorDTO) throws ServiceException {
       
       CatalogosDTO catalogosDTO = new CatalogosDTO();
      
           CatalogosValoresToDTO.INSTANCE.apply(this.catalogosValoresService.insertOrUpdate(
              CatalogosValoresToEntity.INSTANCE.apply(catValorDTO)));
 
       return catalogosDTO;
   }

//    @Override
//    public List<Catalogos> cargaNmConstante() throws ServiceException {
//
//        List<AtlasFilter> filtros = new ArrayList<>();
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
//        return this.find(filtros, null);
//        
//    }
}
