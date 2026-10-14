package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteAnualidadDAO;
import org.madrid.saii.lib.dao.ExpedienteAnualidadHistDAO;
import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.madrid.saii.lib.dto.ExpedienteAnualidadDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteAnualidadToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteAnualidadToEntity;
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
 * @author Quental
 */
@Service("expedienteAnualidadService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteAnualidadServiceImpl
        extends BaseServiceImpl<ExpedienteAnualidad, Integer, ExpedienteAnualidadDAO>
        implements ExpedienteAnualidadService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteAnualidadDAO
     */
    @Autowired
    public ExpedienteAnualidadServiceImpl(
            @Qualifier("expedienteAnualidadDAO") ExpedienteAnualidadDAO expedienteAnualidadDAO) {
        super();
        this.setDao(expedienteAnualidadDAO);
    }

    @Autowired
    @Qualifier("expedienteAnualidadHistDAO")
    private ExpedienteAnualidadHistDAO expedienteAnualidadHistDAO;

    /**
     *
     */
    @Override
    public List<ExpedienteAnualidadDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteAnualidadToDTO.INSTANCE));
    }

    /**
     * Método para insertar o actualizar los datos editados de los registros
     * ExpedienteAnualidad, y luego el histórico de dicho cambio
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteAnualidadDTO guardar(ExpedienteAnualidadDTO expedienteAnualidadDTO)
            throws ServiceException {

        ExpedienteAnualidad expedienteAnualidad = this
                .insertOrUpdate(ExpedienteAnualidadToEntity.INSTANCE.apply(expedienteAnualidadDTO));

        this.expedienteAnualidadHistDAO.crearHistorico(expedienteAnualidad);

        return ExpedienteAnualidadToDTO.INSTANCE.apply(expedienteAnualidad);
    }

    /**
    *   Método para buscar ExpedienteAnualidadDTO por el id de expediente
    *   
    *   @param idExpediente id del expediente
    */
    @Override
    public List<ExpedienteAnualidadDTO> buscarPorExpediente(Integer idExpediente)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE, idExpediente,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<AtlasSort> ordenamiento = new ArrayList<>();
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_NMANUALIDAD, true));

        return new ArrayList<>(Lists.transform(this.find(0, -1, filtros, ordenamiento),
                ExpedienteAnualidadToDTO.INSTANCE));
    }
    
   
     @Override
     public List<ExpedienteAnualidadDTO> consultarPlazosExpAnualidadPorIdExp(Integer idExpediente) throws ServiceException {
         List<AtlasFilter> filtros = new ArrayList<>();
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEXPEDIENTE,  
                 idExpediente, Operador.EQUALS));
         filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
         
         List<AtlasSort> ordenamiento = new ArrayList<>();
         ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_ANUALIDAD, true));
         
         return this.buscar(0, -1, filtros, ordenamiento);
     }
}
