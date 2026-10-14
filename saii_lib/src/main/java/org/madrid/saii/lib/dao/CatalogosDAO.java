
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Catalogos;

/**
 * Interfaz para el DAO de la clase de dominio Catalogos
 *
 * @author everis
 */
public interface CatalogosDAO extends BaseDAO<Catalogos, Integer> {

    List<Catalogos> cargaNmConstante();

}
