package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.PersonaAreaDTO;

/**
 * Interfaz para el DAO de la clase de dominio PersonaAreaCientifica
 *
 * @author nttdata
 */
public interface PersonaAreaDAO extends BaseDAO<PersonaArea, Integer> {
    

    List<PersonaAreaDTO> listObtenerAreas(Integer idInteresado);

}
