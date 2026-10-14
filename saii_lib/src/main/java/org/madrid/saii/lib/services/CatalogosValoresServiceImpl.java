package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.madrid.saii.lib.dao.CatalogosValoresDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToDTO;
import org.madrid.saii.lib.dto.converters.CatalogosValoresToEntity;
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
import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio CatalogosValores
 *
 * @author everis
 */
@Service("catalogosValoresService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class CatalogosValoresServiceImpl
        extends BaseServiceImpl<CatalogosValores, Integer, CatalogosValoresDAO>
        implements CatalogosValoresService {
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param CatalogosValoresDAO
     */
    @Autowired
    public CatalogosValoresServiceImpl(@Qualifier("catalogosValoresDAO")
    CatalogosValoresDAO catalogosValoresDAO) {
        super();
        this.setDao(catalogosValoresDAO);
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> obtenerPorFiltros(List<AtlasFilter> filtros)
            throws ServiceException {
        List<CatalogosValoresDTO> lstCatalogoValoresDTO = null;

        List<AtlasSort> orden = new ArrayList<AtlasSort>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CATALOGO_VALOR_DSVALOR, Boolean.TRUE));

        lstCatalogoValoresDTO = new ArrayList<>(
                Lists.transform(this.find(filtros, orden), CatalogosValoresToDTO.INSTANCE));

        return lstCatalogoValoresDTO;
    }

    /**
     *
     */
    @Override
    public CatalogosValoresDTO buscarId(final Integer idValor) throws ServiceException {
        return CatalogosValoresToDTO.INSTANCE.apply(this.find(idValor));
    }

	/**
     *
     */
    @Override
    public CatalogosValoresDTO buscarNmConstante(Integer nmConstante) throws ServiceException {
        return CatalogosValoresToDTO.INSTANCE.apply(this.find(utilConstantes.getGlobalCatalogosValores().get(nmConstante)));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> buscarPorCatalogo(final Integer idCatalogo)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, idCatalogo,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CATALOGO_VALOR_DSVALOR, Boolean.TRUE));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, orden), CatalogosValoresToDTO.INSTANCE));
    }

    @Override
    public List<CatalogosValoresDTO> buscarPorCatalogoByNmConstante(final Integer nmConstante)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_NMCONSTANTE, nmConstante,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CATALOGO_VALOR_DSVALOR, Boolean.TRUE));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, orden), CatalogosValoresToDTO.INSTANCE));
    }



    @Override
    public List<CatalogosValoresDTO> buscarPorCatalogoOrderNmConstante(final Integer idCatalogo)
            throws ServiceException {
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_IDCATALOGO, idCatalogo,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> orden = new ArrayList<>();
        orden.add(new AtlasSort(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Boolean.TRUE));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, orden), CatalogosValoresToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                CatalogosValoresToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public List<CatalogosValoresDTO> buscarNoRep(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        List<CatalogosValoresDTO> lstCatalogoValores = new ArrayList<>(Lists.transform(
                this.find(index, pageSize, filtros, orden), CatalogosValoresToDTO.INSTANCE));

        return this.quitarRepetidos(lstCatalogoValores);
    }

    /**
     *
     */
    @Override
    public Integer countDistinctDsValor() throws ServiceException {
        Integer total = null;

        try {
            total = this.getDao().countDistinctDsValor();
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

        return total;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public CatalogosValoresDTO guardar(final CatalogosValoresDTO catalogosValoresDTO)
            throws ServiceException {
        return CatalogosValoresToDTO.INSTANCE.apply(
                this.insertOrUpdate(CatalogosValoresToEntity.INSTANCE.apply(catalogosValoresDTO)));
    }

    // TODO: FALTA EL JAVADOC
    private List<CatalogosValoresDTO> quitarRepetidos(
            List<CatalogosValoresDTO> lstCatalogoValores) {
        List<CatalogosValoresDTO> lstCatalogoValoresNoRep = new ArrayList<>();
        Boolean bExiste;

        // Recorremos la lista que se nos ha devuelto desde base de datos
        for (CatalogosValoresDTO catalogosValoresDTO : lstCatalogoValores) {
            bExiste = Boolean.FALSE;
            // Buscamos si existe un catalogoValor en la lista que vamos a
            // devolver
            for (int i = 0; i < lstCatalogoValoresNoRep.size()
                    && bExiste.equals(Boolean.FALSE); i++) {
                // Si existe, nos salimos del bucle
                if (lstCatalogoValoresNoRep.get(i).getDsValor()
                        .equals(catalogosValoresDTO.getDsValor())) {
                    bExiste = Boolean.TRUE;
                }
            }
            // Si no existe, lo insertamos en la lista a devolver
            if (bExiste.equals(Boolean.FALSE)) {
                lstCatalogoValoresNoRep.add(catalogosValoresDTO);
            }
        }
        return lstCatalogoValoresNoRep;
    }

    /**
     * 
     */
    @Override
    public List<CatalogosValoresDTO> obtenerCatalogoConvocatoria(Integer idConvocatoria) throws ServiceException {

        if (Objects.isNull(idConvocatoria)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return this.getDao().obtenerCatalogoConvocatoria( idConvocatoria);
    }

    @Override
    public List<CatalogosValoresDTO> obtenerTipoEntidades() throws ServiceException {
        return this.getDao().obtenerTipoEntidades();
    }

    /**
     * 
     */
    @Override
    public List<CatalogosValoresDTO> obtenerCatalogoConvocatoriaTipoCausa(Integer idConvocatoria,
            Integer idTipoCausa) throws ServiceException {
        if (Objects.isNull(idConvocatoria) || Objects.isNull(idTipoCausa)) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }
        return this.getDao().obtenerCatalogoConvocatoriaTipoCausa(idConvocatoria, idTipoCausa);
    }

//    @Override
//    public List<CatalogosValores> cargaNmConstante() throws ServiceException {
//        
//        List<AtlasFilter> filtros = new ArrayList<>();
//        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CATALOGO_VALOR_NMCONSTANTE, Operador.NOT_NULL));
//        return this.find(filtros, null);
//
//    }

}
