package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ApartadoDAO;
import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.dto.converters.ApartadoToDTO;
import org.madrid.saii.lib.dto.converters.ApartadoToEntity;
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
 * Implementación del servicio para la clase de dominio Apartado
 *
 * @author nttdata
 */
@Service("apartadoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ApartadoServiceImpl extends BaseServiceImpl<Apartado, Integer, ApartadoDAO>
        implements ApartadoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param apartadoDAO
     */
    @Autowired
    public ApartadoServiceImpl(@Qualifier("apartadoDAO") ApartadoDAO apartadoDAO) {
        super();
        this.setDao(apartadoDAO);
    }

    /**
     *
     */
    @Override
    public ApartadoDTO buscarId(Integer idApartado) throws ServiceException {
        return ApartadoToDTO.INSTANCE.apply(this.getDao().find(idApartado));
    }

    /**
     *
     */
    @Override
    public List<ApartadoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ApartadoToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ApartadoDTO guardar(ApartadoDTO apartadoDTO) throws ServiceException {
        return ApartadoToDTO.INSTANCE
                .apply(this.insertOrUpdate(ApartadoToEntity.INSTANCE.apply(apartadoDTO)));
    }

    /**
     *
     */
    @Override
    public List<ApartadoDTO> obtenerApartadosPorEvaluacion(Integer idEvaluacion)
            throws ServiceException {
        if (idEvaluacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_ID_EVALUACION_FALTANTE);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION, idEvaluacion,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.getDao().find(filtros, null), ApartadoToDTO.INSTANCE));
    }

}
