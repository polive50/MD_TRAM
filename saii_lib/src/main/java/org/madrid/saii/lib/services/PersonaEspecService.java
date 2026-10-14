package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.PersonaEspecDAO;
import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.dto.PersonaEspecDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio PersonaEspecialidad
 *
 * @author nttdata
 */
public interface PersonaEspecService extends BaseService<PersonaEspec, Integer, PersonaEspecDAO> {

    /**
     * Metodo para obtener las especialidades asociadas a una persona
     *
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    List<PersonaEspecDTO> buscarPorPersona(Integer idInteresado) throws ServiceException;

    /**
     * Metodo para guardar los datos de especialidad asociada a la persona
     * 
     * @param personaEspecDTO
     * @return
     * @throws ServiceException
     */
    PersonaEspecDTO guardar(PersonaEspecDTO personaEspecDTO) throws ServiceException;

}
