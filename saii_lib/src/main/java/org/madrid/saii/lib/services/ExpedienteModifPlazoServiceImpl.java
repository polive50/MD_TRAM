package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifPlazoDAO;
import org.madrid.saii.lib.domain.ExpedienteModifPlazo;
import org.madrid.saii.lib.dto.ExpedienteModifPlazoDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPlazoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPlazoToEntity;
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
 * @author Quental
 */
@Service("expedienteModifPlazoService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteModifPlazoServiceImpl
        extends BaseServiceImpl<ExpedienteModifPlazo, Integer, ExpedienteModifPlazoDAO>
        implements ExpedienteModifPlazoService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteModifPlazoDAO
     */
    @Autowired
    public ExpedienteModifPlazoServiceImpl(
            @Qualifier("expedienteModifPlazoDAO") ExpedienteModifPlazoDAO expedienteModifPlazoDAO) {
        super();
        this.setDao(expedienteModifPlazoDAO);
    }

    /**
    *
    */
    @Override
    public ExpedienteModifPlazoDTO buscarId(Integer idExpedienteModifPlazo) throws ServiceException {
        return ExpedienteModifPlazoToDTO.INSTANCE.apply(this.getDao().find(idExpedienteModifPlazo));
    }

    /**
     *
     */
    @Override
    public List<ExpedienteModifPlazoDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteModifPlazoToDTO.INSTANCE));
    }

    /**
     * Método para insertar o actualizar los datos editados de los registros
     * ExpedienteModifPlazo, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModifPlazoDTO guardar(ExpedienteModifPlazoDTO expedienteModifPlazoDTO)
            throws ServiceException {

        ExpedienteModifPlazo expedienteModifPlazo = this
                .insertOrUpdate(ExpedienteModifPlazoToEntity.INSTANCE.apply(expedienteModifPlazoDTO));

        return ExpedienteModifPlazoToDTO.INSTANCE.apply(expedienteModifPlazo);
    }

    /**
    *   Método para buscar ExpedienteModifPlazoDTO por el id de expediente
    *   
    *   @param idExpediente id del expediente
    */
    @Override
    public List<ExpedienteModifPlazoDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        //esto no va a funcionar
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMANUALIDAD, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteModifPlazoToDTO.INSTANCE));
    }
    

    @Override
    @Transactional(readOnly = false)
    public void actualizar(ExpedienteModifPlazoDTO expedienteModifPlazoDTO)
             throws ServiceException {
        expedienteModifPlazoDTO.setCdUsuModif(UserUtils.getUserName());
        expedienteModifPlazoDTO.setFcModifFila(new Date());
        this.getDao()
                .update(ExpedienteModifPlazoToEntity.INSTANCE.apply(expedienteModifPlazoDTO));
    }
    
    
    @Override
    public List<ExpedienteModifPlazoDTO> consultarModifPlazoPorIdModif(Integer idExpedienteModifPresPlazoOtra, boolean ordenAsc)
            throws ServiceException {
        
           List<AtlasFilter> filtros = new ArrayList<>();
           List<AtlasSort> ordenamiento = new ArrayList<>();
           
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEMODIFPRESPLAZOOTRA,
                   idExpedienteModifPresPlazoOtra, Operador.EQUALS));
           filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
                      
           //ordenar por más reciente
           ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_MODIF_PLAZO, ordenAsc));
           
           return this.buscar(0, -1, filtros, ordenamiento);       
    }
}
