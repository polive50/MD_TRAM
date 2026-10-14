package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteReqDAO;
import org.madrid.saii.lib.dao.ExpedienteReqHistDAO;
import org.madrid.saii.lib.domain.ExpedienteReq;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
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
 * Implementación del servicio para la clase de dominio ExpedienteReq
 *
 * @author everis
 */
@Service("expedienteReqService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteReqServiceImpl extends
        BaseServiceImpl<ExpedienteReq, Integer, ExpedienteReqDAO> implements ExpedienteReqService {

    /**
     * DAO ExpedienteActuaHist
     */
    @Autowired
    @Qualifier("expedienteReqHistDAO")
    private ExpedienteReqHistDAO expedienteReqHistDAO;
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteReqDAO
     */
    @Autowired
    public ExpedienteReqServiceImpl(
            @Qualifier("expedienteReqDAO") ExpedienteReqDAO expedienteReqDAO) {
        super();
        this.setDao(expedienteReqDAO);
    }

    /**
     * Buscar paginadamente ExpedienteReq.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteReqDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteReqDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteReqToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    public ExpedienteReqDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteReqToDTO.INSTANCE.apply(this.find(id));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteReqDTO guardar(ExpedienteReqDTO expedienteReqDTO) throws ServiceException {
        return ExpedienteReqToDTO.INSTANCE
                .apply(this.insertOrUpdate(ExpedienteReqToEntity.INSTANCE.apply(expedienteReqDTO)));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteReqDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ExpedienteReqToDTO.INSTANCE));
    }

//    @Override
//    @Transactional(readOnly = false)
//    public ExpedienteReqDTO guardarConHistorico(ExpedienteReqDTO expedienteReqDTO)
//            throws ServiceException {
//        ExpedienteReqDTO expedReqDTO = ExpedienteReqToDTO.INSTANCE
//                .apply(this.insertOrUpdate(ExpedienteReqToEntity.INSTANCE.apply(expedienteReqDTO)));
//        ExpedienteReq expedienteReq = this.getDao().find(expedReqDTO.getIdExpedienteReq()); 
//        this.expedienteReqHistDAO.crearHistorico(expedienteReq);
//        return expedReqDTO;
//    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteReqDTO guardarConHistorico(ExpedienteReqDTO dto) throws ServiceException {
        // 1. Convertimos a entidad
        ExpedienteReq entidad = ExpedienteReqToEntity.INSTANCE.apply(dto);
        
        // 2. Guardamos y obtenemos la entidad persistida (con su ID y estado actual)
        entidad = this.getDao().merge(entidad);
        
        // 3. Pasamos la entidad DIRECTAMENTE al histórico (ya la tenemos en memoria, no hace falta .find)
        this.expedienteReqHistDAO.crearHistorico(entidad);
        
        // 4. Devolvemos el DTO mapeando la entidad que ya tenemos
        return ExpedienteReqToDTO.INSTANCE.apply(entidad);
    }

}
