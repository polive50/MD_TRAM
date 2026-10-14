package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;

/**
 * Interfaz para el DAO de la clase de dominio RevisionInfEvaJustec
 *
 * @author quental
 */
public interface RevisionInfEvaJustecDAO extends BaseDAO<RevisionInfEvaJustec, Integer> {


    List<RevisionInfEvaJustec> findByIdRevInfEvaJustec(Integer idRevInfEvaJustec);


}
