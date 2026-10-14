package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.PersonaTitDAO;
import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.PersonaTitDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio PersonaTit
 *
 * @author nttdata
 */
public interface PersonaTitService extends BaseService<PersonaTit, Integer, PersonaTitDAO> {

    /**
     * Metodo para obtener las titulaciones asociadas a una persona
     *
     * @param idInteresado
     * @return
     * @throws ServiceException
     */
    List<PersonaTitDTO> buscarPorPersona(Integer idInteresado) throws ServiceException;

    /**
     * Metodo para guardar los datos de titulacion asociada a la persona
     *
     * @param personaTitDTO
     * @return
     * @throws ServiceException
     */
    PersonaTitDTO guardar(PersonaTitDTO personaTitDTO) throws ServiceException;

}
