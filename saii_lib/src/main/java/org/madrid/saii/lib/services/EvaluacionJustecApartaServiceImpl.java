package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.EvaluacionJustecApartaDAO;
import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecApartaToDTO;
import org.madrid.saii.lib.dto.converters.EvaluacionJustecApartaToEntity;
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
@Service("evaluacionJustecApartaService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EvaluacionJustecApartaServiceImpl
        extends BaseServiceImpl<EvaluacionJustecAparta, Integer, EvaluacionJustecApartaDAO>
        implements EvaluacionJustecApartaService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param apartadoDAO
     */
    @Autowired
    public EvaluacionJustecApartaServiceImpl(
            @Qualifier("evaluacionJustecApartaDAO") EvaluacionJustecApartaDAO apartadoDAO) {
        super();
        this.setDao(apartadoDAO);
    }

    /**
     *
     */
    @Override
    public EvaluacionJustecApartaDTO buscarId(Integer idApartado) throws ServiceException {
        return EvaluacionJustecApartaToDTO.INSTANCE.apply(this.getDao().find(idApartado));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecApartaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                EvaluacionJustecApartaToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public EvaluacionJustecApartaDTO guardar(EvaluacionJustecApartaDTO apartadoDTO)
            throws ServiceException {
        return EvaluacionJustecApartaToDTO.INSTANCE.apply(
                this.insertOrUpdate(EvaluacionJustecApartaToEntity.INSTANCE.apply(apartadoDTO)));
    }

    /**
     *
     */
    @Override
    public List<EvaluacionJustecApartaDTO> obtenerApartadosPorEvaluacion(Integer idEvaluacion)
            throws ServiceException {
        if (idEvaluacion == null) {
            throw new ServiceException(Constantes.Error.ERROR_ID_EVALUACION_FALTANTE);
        }

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION_JUSTEC, idEvaluacion,
                Operador.EQUALS));

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(Lists.transform(this.getDao().find(filtros, null),
                EvaluacionJustecApartaToDTO.INSTANCE));
    }

}
