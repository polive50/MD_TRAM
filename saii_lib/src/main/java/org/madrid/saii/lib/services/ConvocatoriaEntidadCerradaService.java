package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEntidadCerradaDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;

import atlas.core.exceptions.ServiceException;

public interface ConvocatoriaEntidadCerradaService
        extends BaseService<ConvocatoriaEntidadCerrada, Integer, ConvocatoriaEntidadCerradaDAO> {
    
    /**
     * Buscar por IdConvocatoria 
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    public List<ConvocatoriaEntidadCerradaDTO> buscarIdConvocatoria(Integer idConvocatoria)
            throws ServiceException;
    
    /**
     * Buscar por IdConvocatoria y CatalogosValores
     * @param idCatalogoValor
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaEntidadCerradaDTO buscarIdCatalogoValores(Integer idCatalogoValor,
            Integer idConvocatoria) throws ServiceException;
    
    /**
     * Guardar ConvocatoriaEntidadCerradaDTo
     * @param convocatoriaEntidadCerradaDTO
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaEntidadCerradaDTO guardar(
            ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO) throws ServiceException;

    /**
     * Dar de alta lógica a una convocatoriaEntidadCerradaDTo
     * @param convocatoriaEntidadCerradaDTO
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaEntidadCerradaDTO altaLogica(
            ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO) throws ServiceException;
    
    /**
     * Borrado de una entidad cerrada
     * @param convocatoriaEntidadCerrada
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaEntidadCerradaDTO borradoLogico(
            final ConvocatoriaEntidadCerradaDTO convocatoriaEntidadCerradaDTO)
            throws ServiceException;
    
}
