package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteModifOtraDAO;
import org.madrid.saii.lib.domain.ExpedienteModifOtra;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifOtraToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteModifOtraToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ExpedienteModifOtra
 *
 * @author quental
 */
@Slf4j
@Service("expedienteModifOtraService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ExpedienteModifOtraServiceImpl extends BaseServiceImpl<ExpedienteModifOtra, Integer, ExpedienteModifOtraDAO>
        implements ExpedienteModifOtraService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// DAO
    // ////////////////////////////////////////////////////////


    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ExpedienteModifOtraDAO
     */
    @Autowired
    public ExpedienteModifOtraServiceImpl(@Qualifier("expedienteModifOtraDAO") ExpedienteModifOtraDAO expedienteModifOtraDAO) {
        super();
        this.setDao(expedienteModifOtraDAO);
    }

    /**
     * Constructor Vacio
     */
    public ExpedienteModifOtraServiceImpl() {
        super();
    }

    /**
     * Buscar por idExpedienteModifOtra
     *
     * @param idExpedienteModifOtra 
     * @return ExpedienteModifOtraDTO
     * @throws ServiceException the service exception
     */
    @Override
    public ExpedienteModifOtraDTO buscarId(Integer idExpedienteModifOtra) throws ServiceException {

        return ExpedienteModifOtraToDTO.INSTANCE.apply(this.find(idExpedienteModifOtra));
    }
 
    
    /**
     * Buscar por idExpedienteModifPresPlazoOtra
     *
     * @param idExpedienteModifPresPlazoOtra 
     * @return ExpedienteModifOtraDTO
     * @throws ServiceException the service exception
     */
    @Override
    public ExpedienteModifOtraDTO buscarIdExpedienteModif(Integer idExpedienteModifPresPlazoOtra) throws ServiceException {
        ExpedienteModifOtraDTO expteModifOtraDTO = null;
        try{            
            List<AtlasFilter> filtrosEntrada = new ArrayList<>();

            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                    idExpedienteModifPresPlazoOtra, Operador.EQUALS));
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteModifOtra> lstExpdteModifOtra = this.find(filtrosEntrada, null);
                        
            if (!lstExpdteModifOtra.isEmpty()) {
                expteModifOtraDTO = ExpedienteModifOtraToDTO.INSTANCE.apply(lstExpdteModifOtra.get(0));
            }
            
        }catch (Exception e) {
          log.error(e.getMessage());
        }
          
        return expteModifOtraDTO;
    }
    
    
    /**
     * Buscar paginadamente ExpedienteModifOtra.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteModifOtraDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<ExpedienteModifOtraDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ExpedienteModifOtraToDTO.INSTANCE));
    }

   
    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteModifOtraDTO guardar(ExpedienteModifOtraDTO expedienteModifOtraDTO) throws ServiceException {

        ExpedienteModifOtra expedienteMod = ExpedienteModifOtraToEntity.INSTANCE.apply(expedienteModifOtraDTO);

        return ExpedienteModifOtraToDTO.INSTANCE.apply(this.insertOrUpdate(expedienteMod));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public void borradoLogico(final Integer idExpedienteModifOtra) throws ServiceException {
        this.deleteLogico(idExpedienteModifOtra);
    }


    @Override
    public ExpedienteModifOtraDTO obtenerExpedientePrespModifOtra(Integer idExpediente)
            throws ServiceException {
        ExpedienteModifOtra expteModifOtra= null;
        try{            
            List<AtlasFilter> filtrosEntrada = new ArrayList<>();
     
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_MODIFPRESPLAZOOTRA_ID_EXPEDIENTE,
                    idExpediente, Operador.EQUALS));
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_MODIF_OTRA_ID_ESTADO,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteResolucion.INICIADA), Operador.EQUALS));
            filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

            List<ExpedienteModifOtra> lstExpdteModifOtra = this.find(filtrosEntrada, null);
                        
            if (!lstExpdteModifOtra.isEmpty()) {
                expteModifOtra = lstExpdteModifOtra.get(0);
            }
            
        }catch (Exception e) {
          log.error(e.getMessage());
        }
          
        return ExpedienteModifOtraToDTO.INSTANCE.apply(expteModifOtra);
    }
    
    
    @Override
    public List<ExpedienteModifOtraDTO> buscarPorIdExpModifPresPlazoOtra(Integer idExpModifPresPlazoOtra, boolean ordenAsc)
            throws ServiceException {
        
        List<AtlasFilter> filtrosEntrada = new ArrayList<>();
        List<AtlasSort> ordenamiento = new ArrayList<>();
 
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                idExpModifPresPlazoOtra, Operador.EQUALS));
        filtrosEntrada.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        //ordenar por más reciente
        ordenamiento.add(new AtlasSort(Constantes.Filtro.FILTRO_ID_EXPEDIENTE_MODIF_OTRA, ordenAsc));
        
        return this.buscar(0, -1, filtrosEntrada, ordenamiento);       
      
    }

}
