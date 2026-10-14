package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio ExpedienteDocHist
 *
 * @author everis
 */
@Service("expedienteDocHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteDocHistServiceImpl
        extends BaseServiceImpl<ExpedienteDocHist, Integer, ExpedienteDocHistDAO>
        implements ExpedienteDocHistService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteDocHistDAO
     */
    @Autowired
    public ExpedienteDocHistServiceImpl(
            @Qualifier("expedienteDocHistDAO") ExpedienteDocHistDAO expedienteDocHistDAO) {
        super();
        this.setDao(expedienteDocHistDAO);
    }

    /**
     * Buscar paginadamente ExpedienteDocHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDocHistDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteDocHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteDocHistToDTO.INSTANCE));
    }

    /**
     * Crear un historico
     *
     * @param expedienteDocDTO the expedienteDocDTO
     * @param cdUsuAlta the codigo del usuario que realiza la modificación
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocHist crearHistorico(ExpedienteDocDTO expedienteDocDTO, String cdUsuAlta)
            throws ServiceException {
        return this.getDao().crearHistorico(ExpedienteDocToEntity.INSTANCE.apply(expedienteDocDTO),
                cdUsuAlta);
    }
    
    /**
     * Crear un historico
     *
     * @param expedienteDoc the expedienteDoc
     * @param cdUsuAlta the codigo del usuario que realiza la modificación
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDocHist crearHistoricoExpedienteDoc(ExpedienteDoc expedienteDoc, String cdUsuAlta)
            throws ServiceException {
        return this.getDao().crearHistorico(expedienteDoc, cdUsuAlta);
    }

}
