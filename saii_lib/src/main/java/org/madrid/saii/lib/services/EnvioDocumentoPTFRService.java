package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EnvioDocumentoPTFRDAO;
import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Expediente
 *
 * @author everis
 */
public interface EnvioDocumentoPTFRService extends BaseService<EnvioDocumentoPTFR, Integer, EnvioDocumentoPTFRDAO> {

    /**
    *
    */
    List<EnvioDocumentoPTFR> buscarEnviosDocumentoPortafirmas(int index, int pageSize,List<AtlasFilter> filtros, List<AtlasSort> orden, Integer idConvocatoria) throws ServiceException;
    
    /**
     * 
     * @param index
     * @param pageSize
     * @param filtros
     * @param orden
     * @param idEnvioDocPtfrPadre
     * @return
     * @throws ServiceException
     */
    List<EnvioDocumentoPTFR> buscarEnvioDocPtfrPorIdEnvioDocPtfrPadre(int index, int pageSize,
            Object filtros, Object orden, Integer idEnvioDocPtfrPadre)
            throws ServiceException;

}
