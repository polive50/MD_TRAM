package org.madrid.saii.lib.dao;

import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Dato;

/**
 * Interfaz para el DAO de la clase de dominio Dato
 *
 * @author everis
 */
public interface DatoDAO extends BaseDAO<Dato, Integer> {

    Map<String, String> obtenerValoresDatos(Integer idExpediente, List<Integer> lstIdComponente,
            List<Integer> lstIdTipoFinanciacion);

}
