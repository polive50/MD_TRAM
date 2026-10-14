package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.AlegacionDAO;
import org.madrid.saii.lib.domain.Alegacion;
import org.madrid.saii.lib.dto.AlegacionDTO;
import org.madrid.saii.lib.dto.converters.AlegacionToDTO;
import org.madrid.saii.lib.dto.converters.AlegacionToEntity;
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
 * Implementación del servicio para la clase de dominio Alegacion
 *
 * @author everis
 */
@Service("alegacionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class AlegacionServiceImpl extends BaseServiceImpl<Alegacion, Integer, AlegacionDAO>
        implements AlegacionService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param alegacionDAO
     */
    @Autowired
    public AlegacionServiceImpl(@Qualifier("alegacionDAO") AlegacionDAO alegacionDAO) {
        super();
        this.setDao(alegacionDAO);
    }

    /**
     * Buscar todos los registros de Alegacion.
     *
     * @return List<AlegacionDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<AlegacionDTO> obtener() throws ServiceException {
        return new ArrayList<>(Lists.transform(this.getDao().findAll(), AlegacionToDTO.INSTANCE));
    }

    /**
     * Buscar por id Alegacion.
     *
     * @param idAlegacion the id Alegacion
     * @return AlegacionDTO
     * @throws ServiceException the service exception
     */
    @Override
    public AlegacionDTO buscarId(Integer idAlegacion) throws ServiceException {
        return AlegacionToDTO.INSTANCE.apply(this.getDao().find(idAlegacion));
    }

    /**
     * Buscar paginadamente Alegacion.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<AlegacionDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<AlegacionDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                AlegacionToDTO.INSTANCE));
    }

    /**
     * Guarda un/a alegacion.
     *
     * @param alegacionDTO the alegacion DTO
     * @return AlegacionDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public AlegacionDTO guardar(AlegacionDTO alegacionDTO) throws ServiceException {
        return AlegacionToDTO.INSTANCE
                .apply(this.insertOrUpdate(AlegacionToEntity.INSTANCE.apply(alegacionDTO)));
    }

    /**
     * Metodo que se encarga de borrar del interesadoDTO.
     *
     * @param interesadoDTO the interesado DTO
     * @throws ServiceException the service exception
     */

    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(AlegacionDTO alegacionDTO) throws ServiceException {
        this.deleteLogico(AlegacionToEntity.INSTANCE.apply(alegacionDTO));
    }

    /**
    *
    */
    @Override
    public List<AlegacionDTO> obtenerPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        if (idConvocatoria == null) {
            throw new ServiceException(Constantes.Error.ERROR_ID_CONVOCATORIA_FALTANTE);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> lisOrden = new ArrayList<>();
        lisOrden.add(new AtlasSort(Constantes.Filtro.FILTRO_NMORDEN, true));

        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, lisOrden), AlegacionToDTO.INSTANCE));
    }

}
