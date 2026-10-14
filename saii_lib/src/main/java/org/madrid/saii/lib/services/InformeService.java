package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.InformeDAO;
import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.dto.InformeDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface InformeService extends BaseService<Informe, Integer, InformeDAO>{

    /**
     * 
     * @param idConvocatoria
     * @param idTipoInforme
     * @return
     */
    List<InformeDTO> findByIdConvocatoriaIdTipoInforme(Integer idConvocatoria, Integer idTipoInforme)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param idInforme
     * @return
     * @throws ServiceException
     */
    List<InformeDTO> findByIdConvocatoriaIdInforme(Integer idConvocatoria, Integer idInforme)
            throws ServiceException;
    
    /**
     * Método para buscar Informes paginados
     * @param index
     * @param pageSize
     * @param filtros
     * @param orden
     * @return
     * @throws ServiceException
     */
    List<InformeDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
            final List<AtlasSort> orden) throws ServiceException;
    
    /**
     * Método para guardar el informe
     * @param informeDTO
     * @throws ServiceException
     */
    public InformeDTO guardar(InformeDTO informeDTO) throws ServiceException;
    
    /**
     * Método para recuperar los informes por convocatroria 
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<InformeDTO> obtenerPorConvocatoria(Integer idConvocatoria) throws ServiceException;
    
    /**
     * Método para borrar lógicamente un InformeDTO
     * @param informeDTO
     * @throws ServiceException
     */
    public void borradoLogico(InformeDTO informeDTO) throws ServiceException;
    
}
