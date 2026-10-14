package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteEvalJusTecHistDAO;
import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalJusTecHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteEvalJusTecHistToEntity;
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
import atlas.core.util.UserUtils;

/**
 * Interfaz del servicio ExpedienteEvalJusTecHist
 *
 * @author quental
 */

@Service("expedienteEvalJusTecHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteEvalJusTecHistServiceImpl
        extends BaseServiceImpl<ExpedienteEvalJusTecHist, Integer, ExpedienteEvalJusTecHistDAO> implements ExpedienteEvalJusTecHistService {
    
    @Autowired
    public ExpedienteEvalJusTecHistServiceImpl(
            @Qualifier("expedienteEvalJusTecHistDAO") ExpedienteEvalJusTecHistDAO expedienteEvalJusTecHistDAO) {
        super();
        this.setDao(expedienteEvalJusTecHistDAO);
    }
    
    @Autowired
    @Qualifier("personaAreaDAO")
    private PersonaAreaDAO personaAreaDAO;
    
    /**
     * DAO ExpedienteEvalJusTecHistDAO
     */
    @Autowired
    @Qualifier("expedienteEvalJusTecHistDAO")
    private ExpedienteEvalJusTecHistDAO expedienteEvalJusTecHistDAO;
    
    /**
     * 
     */
    @Override
    public List<ExpedienteEvalJusTecHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTec) throws ServiceException {
        
       List<AtlasFilter> filtrosEntrada = (filtros != null) ? filtros : new ArrayList<>();
        
       filtrosEntrada.add(new AtlasFilter("expedienteJustec.idExpedienteJusTec", idExpedienteJusTec,
                Operador.EQUALS));
       filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        
        return  new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtrosEntrada, orden), 
                        ExpedienteEvalJusTecHistToDTO.INSTANCE));
    }
    
    /**
     * 
     */
    @Override
    public List<ExpedienteEvalJusTecHistDTO> buscarHistoricoEvalJCT(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteEvalJusTec, int idPersona) throws ServiceException {
        
       List<AtlasFilter> filtrosEntrada = (filtros != null) ? filtros : new ArrayList<>();
       
       filtrosEntrada.add(new AtlasFilter("expedienteEvalJusTec.idExpedienteEvalJusTec", idExpedienteEvalJusTec,
               Operador.EQUALS));
       filtrosEntrada.add(new AtlasFilter("persona.idInteresado", idPersona,
               Operador.EQUALS));
       filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return  new ArrayList<>(
                Lists.transform(this.find(index, pageSize, filtrosEntrada, orden), 
                        ExpedienteEvalJusTecHistToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecHistDTO guardar(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHistDTO)
            throws ServiceException {
  
        return ExpedienteEvalJusTecHistToDTO.INSTANCE.apply(this.insertOrUpdate(
                ExpedienteEvalJusTecHistToEntity.INSTANCE.apply(expedienteEvalJusTecHistDTO)));
    }
    
    
    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecHistDTO actualizar(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHistDTO)
            throws ServiceException {
        expedienteEvalJusTecHistDTO.setCdUsuModif(UserUtils.getUserName());
        expedienteEvalJusTecHistDTO.setFcModifFila(new Date());
  
        return ExpedienteEvalJusTecHistToDTO.INSTANCE.apply(this.update(
                ExpedienteEvalJusTecHistToEntity.INSTANCE.apply(expedienteEvalJusTecHistDTO)));
    }
    

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecHistDTO borradoLogico(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHistDTO)
            throws ServiceException {
        expedienteEvalJusTecHistDTO.setCdUsuBaja(UserUtils.getUserName());
        expedienteEvalJusTecHistDTO.setFcBajaFila(new Date());
        
        return ExpedienteEvalJusTecHistToDTO.INSTANCE.apply(this.update(
                ExpedienteEvalJusTecHistToEntity.INSTANCE.apply(expedienteEvalJusTecHistDTO)));
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecHistDTO buscarByIdExpedienteJustec(Integer idExpedienteJusTec) throws ServiceException {
     
        return ExpedienteEvalJusTecHistToDTO.INSTANCE.apply(this.getDao().find(idExpedienteJusTec));
        
    }

    @Override
    @Transactional(readOnly = false)
    public ExpedienteEvalJusTecHistDTO buscarId(Integer idExpedienteEvalJusTec) throws ServiceException {
     
        return ExpedienteEvalJusTecHistToDTO.INSTANCE.apply(this.getDao().find(idExpedienteEvalJusTec));
        
    }

    @Override
    public List<ExpedienteEvalJusTecHistDTO> buscarByIdExpJustec(
            List<ExpedienteEvalJusTecHistDTO> listExpedienteJusTecHistDTO) throws ServiceException {
        return null;
    }

}