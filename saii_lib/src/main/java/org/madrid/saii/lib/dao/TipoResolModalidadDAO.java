package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.TipoResolModalidad;

/**
 * Interfaz para el DAO de la clase de dominio CatalogosValores
 *
 * @author quental
 */
public interface TipoResolModalidadDAO extends BaseDAO<TipoResolModalidad, Integer> {
    
    TipoResolModalidad findByIdModalidad(Integer idModalidad); 
    
}
