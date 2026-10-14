package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Listado;
import org.madrid.saii.lib.dto.ConvocatoriaInformeExcelDTO;
import org.madrid.saii.lib.dto.ListadoSituacionEvaluadoresDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio Informe
 *
 * @author nttdata
 */
public interface ListadoDAO extends BaseDAO<Listado, Integer> {

    /**
     * Metodo para obtener datos para informe
     *
     * @param idListado
     * @return
     * @ 
     */
    List<ConvocatoriaInformeExcelDTO> obtenerListadoExcel(Integer idListado) ;

    /**
     * Metodo para obtener datos para informe
     *
     * @param idListado
     * @return
     * @ 
     */
    List<ConvocatoriaInformeExcelDTO> obtenerListadoExcel2(Integer idConvocatoria) ;

    /**
     * Metodo para obtener las columnas adiciones para el informe
     *
     * @param idListado
     * @return
     * @ 
     */
    List<ConvocatoriaInformeExcelDTO> obtenerColumnasAdicionales(Integer idListado) ;
    
    /**
     * Obtiene los datos del listado de situación de evaluadores.
     *
     * @param idListado identificador del listado asociado a la convocatoria
     * @return datos del listado
     */
    List<ListadoSituacionEvaluadoresDTO> obtenerListadoSituacionEvaluadores(Integer idListado);
}
