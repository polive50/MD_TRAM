
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio CatalogosValores
 *
 * @author everis
 */
public interface CatalogosValoresDAO extends BaseDAO<CatalogosValores, Integer> {

    /**
     * Metodo que devuelve cuantos CatalogosValores hay que no tengan el dsValor
     * repetido
     *
     * @return Integer
     */
    Integer countDistinctDsValor();

    /**
     * Metodo que devuelve la lista de causas de denegacion
     *
     * @param idCatalogo
     * @param idConvocatoria
     * @return CatalogosValoresDTO[]
     */

    List<CatalogosValoresDTO> obtenerCatalogoConvocatoria(Integer idConvocatoria);

    /**
     * Método que devuelve los catalogos valores de tipo de entidad
     * @return
     * @throws ServiceException 
     */
    List<CatalogosValoresDTO> obtenerTipoEntidades();

    List<CatalogosValores> cargaNmConstante();

     List<CatalogosValores> cargaNmConstanteObjeto();
 /**
     * Método que recupera los catalogos valores por idConvocatoria e IdTipoCausa
     * @param idConvocatoria
     * @param idTipoCausa
     * @return
     */
    List<CatalogosValoresDTO> obtenerCatalogoConvocatoriaTipoCausa(Integer idConvocatoria,
            Integer idTipoCausa);

}
