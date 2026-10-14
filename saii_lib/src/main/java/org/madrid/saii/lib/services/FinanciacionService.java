package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.FinanciacionDAO;
import org.madrid.saii.lib.domain.Financiacion;
import org.madrid.saii.lib.dto.FinanciacionDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Financiacion
 *
 * @author everis
 */
public interface FinanciacionService extends BaseService<Financiacion, Integer, FinanciacionDAO> {

    /**
     *
     */
    List<FinanciacionDTO> obtener() throws ServiceException;

    /**
     *
     */
    FinanciacionDTO buscarId(Integer idFinanciacion) throws ServiceException;

    /**
     *
     */
    List<FinanciacionDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     *
     */
    FinanciacionDTO guardar(FinanciacionDTO financiacionDTO) throws ServiceException;

    /**
     *
     */
    void altaLogica(final FinanciacionDTO financiacionDTO) throws ServiceException;

    /**
     *
     */
    void borradoLogico(final FinanciacionDTO financiacionDTO) throws ServiceException;

    /**
     * Metodo para guardar la configuracion de la finación, segun el tipo de
     * tabla seleccionada (partidas presupuestarias, ayuda solicitada o
     * actuaciones)
     *
     * @param financiacion
     * @param listaCatalogosSeleccionados
     * @param listaAnualidades
     * @throws ServiceException
     */
    FinanciacionDTO guardarConfiguracionFinanciacion(Integer idConvocatoria, String itTipo,
            List<Integer> listaIdCatalogosSeleccionados, List<Integer> listaAnualidades)
            throws ServiceException;

    /**
     * Metodo para eliminar la configuracion de la finación, segun el tipo de
     * tabla seleccionada (partidas presupuestarias, ayuda solicitada o
     * actuaciones)
     *
     * @param financiacion
     * @throws ServiceException
     */
    void eliminarConfiguracionFinanciacion(FinanciacionDTO financiacionDTO) throws ServiceException;

    /**
     * Método que busca el tipo por convocatoria
     *
     * @param idConvocatoria
     * @param idTipo
     * @return
     * @throws ServiceException
     */
    List<FinanciacionDTO> buscaFinanciacionrPorConvocatoriaTipo(final Integer idConvocatoria,
            final String itTipo) throws ServiceException;
    
    
    /**
     * Método que busca el tipo por convocatoria
     *
     * @param idConvocatoria
     * @param idTipo
     * @return
     * @throws ServiceException
     */
    List<FinanciacionDTO> buscaFinanciacionByIdConvocatoria(final Integer idConvocatoria) throws ServiceException;
    
    public Boolean compruebaTematicaEnListado(List<FinanciacionDTO> listFinanciacion);
        
    public Boolean compruebaActorEnListado(List<FinanciacionDTO> listFinanciacion);
    
    /**
     * 
     * @param idConvocatoria
     * @param idActor
     * @return
     * @throws ServiceException
     */
    public List<FinanciacionDTO> buscarPorIdConvocatoriaIdActor(Integer idConvocatoria,
            Integer idActor) throws ServiceException;
    
    /**
     * 
     * @param idActor
     * @throws ServiceException
     */
    public void eliminarActorCuadroFinanciacion(FinanciacionDTO financiacionDTO)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param tipoPartidasPresupuestaria
     * @return
     */
    public FinanciacionDTO guardarFinanciacion(Integer idConvocatoria,
            String tipoPartidasPresupuestaria) throws ServiceException;

    List<FinanciacionDTO> buscarTablasFinanByIdConvocAndTipo(Integer idConvocatoria, String tipo)
            throws ServiceException;
    
}
