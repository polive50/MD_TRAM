package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoDocDAO;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoDocToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;


@Service("expedienteContratoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteContratoDocServiceImpl extends BaseServiceImpl<ExpedienteContratoDoc, Integer, ExpedienteContratoDocDAO>
    implements ExpedienteContratoDocService {
    
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param 
     */
    @Autowired
    public ExpedienteContratoDocServiceImpl(
            @Qualifier("expedienteContratoDocDAO") ExpedienteContratoDocDAO expedienteContratoDocDAO) {
        super();
        this.setDao(expedienteContratoDocDAO);
    }
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteContratoDocDTO guardarExpedienteContratoDoc(ExpedienteContratoDocDTO expedienteContratoDocDTO) throws ServiceException {

        ExpedienteContratoDoc expedienteContratoDoc = ExpedienteContratoDocToEntity.INSTANCE.apply(expedienteContratoDocDTO);
        this.getDao().insertOrUpdate(expedienteContratoDoc);
        return ExpedienteContratoDocToDTO.INSTANCE.apply(expedienteContratoDoc);
         
     }
        
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteContratoDocDTO actualizarExpedienteContratoDoc(ExpedienteContratoDocDTO expContratoDocDTO) throws ServiceException {

       ExpedienteContratoDoc expedienteContratoDoc = ExpedienteContratoDocToEntity.INSTANCE.apply(expContratoDocDTO);
       this.getDao().update(expedienteContratoDoc);
       
       return ExpedienteContratoDocToDTO.INSTANCE.apply(expedienteContratoDoc);

   }
    
    @Override
    public List<ExpedienteContratoDocDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteContratoDocToDTO.INSTANCE));
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idExpedienteContratoDoc) throws ServiceException {
        this.deleteLogico(idExpedienteContratoDoc);
    }
}
