package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.PersonaOrgDAO;
import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.dto.PersonaOrgDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio PersonaOrg
 *
 * @author nttdata
 */
public interface PersonaOrgService extends BaseService<PersonaOrg, Integer, PersonaOrgDAO> {

    /**
     * Metodo para obtener las organizaciones asociadas a una persona
     *
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    List<PersonaOrgDTO> buscarPorPersona(Integer idInteresado) throws ServiceException;

    /**
     * Metodo para guardar los datos de organismo asociada a la persona
     * 
     * @param personaOrgDTO
     * @return
     * @throws ServiceException
     */
    PersonaOrgDTO guardar(PersonaOrgDTO personaOrgDTO) throws ServiceException;

}
