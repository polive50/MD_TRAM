package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJustecDocDAO;
import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJustecDocToDTO;
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
@Service("expedienteJusTecDocService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteJusTecDocServiceImpl
        extends BaseServiceImpl<ExpedienteJustecDoc, Integer, ExpedienteJustecDocDAO>
        implements ExpedienteJusTecDocService {
    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteJusTecDAO
     */
    @Autowired
    public ExpedienteJusTecDocServiceImpl(
            @Qualifier("expedienteJustecDocDAO") ExpedienteJustecDocDAO expedienteJustecDocDAO) {
        super();
        this.setDao(expedienteJustecDocDAO);
    }


    @Autowired
    @Qualifier("expedienteJustecDocDAO")
    private ExpedienteJustecDocDAO expedienteJustecDocDAO;
    
    
    
    @Override
    public List<ExpedienteJustecDocDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
              ExpedienteJustecDocToDTO.INSTANCE));
    }
    
    
    /**
    *   Método para buscar ExpedienteJusTecDocDTO por el id de expediente doc
    *   
    *   @param idExpedienteDoc id del expediente doc
    */
    @Override
    public List<ExpedienteJustecDocDTO> buscarPorExpedienteDoc(Integer idExpedienteDoc)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTEDOC, idExpedienteDoc,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
       // ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMPERIODO, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteJustecDocToDTO.INSTANCE));
    }

    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(Integer idExpedienteJustecDoc) throws ServiceException {
        this.deleteLogico(idExpedienteJustecDoc);
    }

   
}
