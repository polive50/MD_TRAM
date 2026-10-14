package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.ExpedienteReqHistDAO;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.domain.ExpedienteReqHist;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.dto.ExpedienteReqHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteReqHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteReqHistToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteReqToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteReqToEntity;
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
 * Implementación del servicio para la clase de dominio ExpedienteReqHist
 *
 * @author everis
 */
@Service("expedienteReqHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteReqHistServiceImpl extends
        BaseServiceImpl<ExpedienteReqHist, Integer, ExpedienteReqHistDAO> implements ExpedienteReqHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteReqDAO
     */
    @Autowired
    public ExpedienteReqHistServiceImpl(
            @Qualifier("expedienteReqHistDAO") ExpedienteReqHistDAO expedienteReqHistDAO) {
        super();
        this.setDao(expedienteReqHistDAO);
    }

    /**
     * Buscar paginadamente ExpedienteReqHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteReqHistDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteReqHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteReqHistToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteReqHistDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteReqHistToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteReqHistDTO guardar(ExpedienteReqHistDTO expedienteReqHistDTO) throws ServiceException {
        return ExpedienteReqHistToDTO.INSTANCE
                .apply(this.insertOrUpdate(ExpedienteReqHistToEntity.INSTANCE.apply(expedienteReqHistDTO)));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteReqHistDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteReqHistToDTO.INSTANCE));
    }

}
