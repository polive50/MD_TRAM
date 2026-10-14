package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.GcatusuarsDAO;
import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.GcatusuarsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio ExpeT046
 *
 * @author everis
 */
@Service("gcatusuarsService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class GcatusuarsServiceImpl extends BaseServiceImpl<UnidadAdmin, Integer, GcatusuarsDAO>
        implements GcatusuarsService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expeT046Dao
     */
    @Autowired
    public GcatusuarsServiceImpl(@Qualifier("gcatusuarsDAO") GcatusuarsDAO gcatusuarsDAO) {
        super();
        this.setDao(gcatusuarsDAO);
    }

    /**
     * Constructor Vacio
     */
    public GcatusuarsServiceImpl() {
        super();
    }

    /**
     * Buscar los datos de la Unidad por el codigo de usuario logado
     *
     * @param cdUsuario the
     * @return the GcatusuarsDTO DTO
     * @throws ServiceException the service exception
     */
    @Override
    public GcatusuarsDTO buscarCdUsuario(final String cdUsuario) throws ServiceException {

        GcatusuarsDTO gcatusuarsDTO = new GcatusuarsDTO();
        try {
            gcatusuarsDTO = this.getDao().buscarCdUsuario(cdUsuario);

        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new ServiceException(e);
        }

        return gcatusuarsDTO;

    }

    @Override
    public String descripcionUnidad(String cdUnidad) throws ServiceException {
        return this.getDao().descripcionUnidad(cdUnidad);
    }

    @Override
    public String obtenerConsejeria() throws ServiceException {
        return this.getDao().descripcionUnidad("C0900");
    }

}
