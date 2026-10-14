package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;
import org.madrid.saii.lib.dto.converters.TipoResolModalidadToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio SucaTipoVial.
 *
 * @author everis
 */
@Service("tipoResolModalidadService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class TipoResolModalidadServiceImpl
        extends BaseServiceImpl<TipoResolModalidad, Integer, TipoResolModalidadDAO>
        implements TipoResolModalidadService {

    @Autowired
    @Qualifier("tipoResolModalidadDAO")
    private TipoResolModalidadDAO tipoResolModalidadDAO;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;



    @Override
    public TipoResolModalidadDTO buscarPorIdModalidad(Integer id) throws ServiceException {
        TipoResolModalidad tipoResolModalidad = this.tipoResolModalidadDAO.findByIdModalidad(id);
        CatalogosValoresDTO catalogosValoresDTO = catalogosValoresService.buscarId(id);

        TipoResolModalidadDTO tipoResolModalidadDTO = TipoResolModalidadToDTO.INSTANCE.apply(tipoResolModalidad);
        tipoResolModalidadDTO.setIdModalidad(catalogosValoresDTO);

        return tipoResolModalidadDTO;
    }
}
