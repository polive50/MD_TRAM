package org.madrid.saii.lib.services;


import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConfiguracionDAO;
import org.madrid.saii.lib.domain.Configuracion;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.converters.ConfiguracionToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Service("configuracionService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class ConfiguracionServiceImpl extends BaseServiceImpl<Configuracion, Integer, ConfiguracionDAO>
implements ConfiguracionService {

/**
* Constructor de la clase que recibe como parámetros el DAO asociado a la
* entidad. Está anotado con @Autowired para realizar automáticamente la
* inyección y así establecer el valor del DAO genérico en clase la clase
* padre (BaseServiceImpl).
*
* @param CatalogosValoresDAO
*/
@Autowired
public ConfiguracionServiceImpl(
    @Qualifier("configuracionDAO") ConfiguracionDAO configuracionDAO) {
super();
this.setDao(configuracionDAO);
}

@Override
public List<ConfiguracionDTO> obtenerConfiguracion(ConvocatoriaDTO dto) throws ServiceException {
    
    List<ConfiguracionDTO> result = new ArrayList<>();
    
    List<AtlasFilter> filtros = new ArrayList<>();

    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA, dto.getIdConvocatoria(), Operador.EQUALS));
    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

    List<Configuracion> allConfiguracion= this.getDao().find(filtros, null);
    
    
    if(!allConfiguracion.isEmpty()) {
        return new ArrayList<>(
                Lists.transform(allConfiguracion, ConfiguracionToDTO.INSTANCE));
    }
    
    return result;
}


}
