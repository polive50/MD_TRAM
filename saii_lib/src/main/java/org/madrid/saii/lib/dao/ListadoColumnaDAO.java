package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ListadoColumna;
import org.madrid.saii.lib.dto.ListadoConfiguracionDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio ListadoColumna
 *
 * @author nttdata
 */
public interface ListadoColumnaDAO extends BaseDAO<ListadoColumna, Integer> {

    /**
     * Obtener columnas de informe
     *
     * @param idConvocatoria
     * @return List<ListadoConfiguracionDTO>
     * @ 
     */
    List<ListadoConfiguracionDTO> obtenerListadoColumnas(Integer idConvocatoria, Integer idListado) ;

    List<ListadoConfiguracionDTO> obtenerListaColumnaDisponible(Integer idConvocatoria) ;

    List<ListadoConfiguracionDTO> obtenerListaColumnaConfigurada(Integer idListado) ;
}
