package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoDocDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoDocToEntity;
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
 * @author quental
 */
@Service("expedienteJusEcoDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusEcoDocServiceImpl
        extends BaseServiceImpl<ExpedienteJusEcoDoc, Integer, ExpedienteJusEcoDocDAO>
        implements ExpedienteJusEcoDocService {
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteJusEcoDAO
     */
    @Autowired
    public ExpedienteJusEcoDocServiceImpl(
            @Qualifier("expedienteJusEcoDocDAO") ExpedienteJusEcoDocDAO expedienteJusEcoDocDAO) {
        super();
        this.setDao(expedienteJusEcoDocDAO);
    }


    @Autowired
    @Qualifier("expedienteJusEcoDocDAO")
    private ExpedienteJusEcoDocDAO expedienteJusEcoDocDAO;
    
    
    
    @Override
    public List<ExpedienteJusEcoDocDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
              ExpedienteJusEcoDocToDTO.INSTANCE));
    }
    
    
    @Override
    public ExpedienteJusEcoDocDTO buscarId(Integer id) throws ServiceException {
        return ExpedienteJusEcoDocToDTO.INSTANCE.apply(this.find(id));
    }
    
    /**
    *   Método para buscar ExpedienteJusEcoDocDTO por el id de expediente doc
    *   
    *   @param idExpedienteDoc id del expediente doc
    */
    @Override
    public List<ExpedienteJusEcoDocDTO> buscarPorExpedienteDoc(Integer idExpedienteDoc)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_ID_EXPEDIENTE_DOC, idExpedienteDoc,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
       // ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusEcoDocToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idExpedienteJusEcoDoc) throws ServiceException {
        this.deleteLogico(idExpedienteJusEcoDoc);
    }
    
    @Override
    public List<ExpedienteJusEcoDocDTO> buscarPorExpedienteJuseco(Integer idExpedienteJuseco)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSECO_IDEXPEDIENTE_JUSECO, idExpedienteJuseco,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSECO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
       // ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusEcoDocToDTO.INSTANCE));
    }


    @Override
    @Transactional(readOnly = false)
    public ExpedienteJusEcoDocDTO guardar(ExpedienteJusEcoDocDTO expedienteJusEcoDocDTO)
            throws ServiceException {
        if(expedienteJusEcoDocDTO != null && expedienteJusEcoDocDTO.getIdExpedienteJusEcoDoc() != null) {
            return ExpedienteJusEcoDocToDTO.INSTANCE.apply( this.getDao().merge(ExpedienteJusEcoDocToEntity.INSTANCE.apply(expedienteJusEcoDocDTO)));
        }else {
            return ExpedienteJusEcoDocToDTO.INSTANCE.apply( this.getDao().insert(ExpedienteJusEcoDocToEntity.INSTANCE.apply(expedienteJusEcoDocDTO)));
        }
        
    }

    
    @Override
    public List<ExpedienteJusEcoDocDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSECO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_JUSECO_FCBAJA, Operador.NULL));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_DOC_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
       // ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJusEcoDocToDTO.INSTANCE));
    }
   
}
