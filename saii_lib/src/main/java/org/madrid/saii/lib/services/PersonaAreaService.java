package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.PersonaAreaDAO;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.dto.PersonaAreaDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio PersonaAreaCientifica
 *
 * @author nttdata
 */
public interface PersonaAreaService extends BaseService<PersonaArea, Integer, PersonaAreaDAO> {

    /**
     * Metodo para obtener las areas cientificas asociadas a una persona
     *
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    List<PersonaAreaDTO> buscarPorPersona(Integer idInteresado) throws ServiceException;

    /**
     * Metodo para guardar los datos de area cientifica asociada a la persona
     * 
     * @param interesadoDTO
     * @return
     * @throws ServiceException
     */
    PersonaAreaDTO guardar(PersonaAreaDTO interesadoDTO) throws ServiceException;

}
