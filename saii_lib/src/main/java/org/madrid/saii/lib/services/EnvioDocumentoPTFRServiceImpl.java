package org.madrid.saii.lib.services;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.madrid.saii.lib.dao.EnvioDocumentoPTFRDAO;
import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para la clase de dominio Expediente
 *
 * @author nttdata
 */
@Slf4j
@Service("envioDocumentoPtfrService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class EnvioDocumentoPTFRServiceImpl extends BaseServiceImpl<EnvioDocumentoPTFR, Integer, EnvioDocumentoPTFRDAO>
        implements EnvioDocumentoPTFRService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////


    // ////////////////////////////////////////////////////////
    // /////////////////////////////// DAO
    // ////////////////////////////////////////////////////////

    
    /**
     * Session factory de hibernate
     */
    @Resource
    private SessionFactory sessionFactory;
    

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param expedienteDAO
     */
    @Autowired
    public EnvioDocumentoPTFRServiceImpl(@Qualifier("envioDocumentoPTFRDAO") EnvioDocumentoPTFRDAO envioDocumentoPTFRDAO) {
        super();
        this.setDao(envioDocumentoPTFRDAO);
    }

    /**
     * Constructor Vacio
     */
    public EnvioDocumentoPTFRServiceImpl() {
        super();
    }


   /**
   *
   */
  @Override
  public List<EnvioDocumentoPTFR> buscarEnviosDocumentoPortafirmas(final int index, final int pageSize,
          final List<AtlasFilter> filtros, final List<AtlasSort> orden,final Integer idConvocatoria)
          throws ServiceException {

      List<EnvioDocumentoPTFR> lstSolicitudes = null;

      filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_CONVOCATORIA,
              idConvocatoria, Operador.EQUALS));

      
      lstSolicitudes = this.find(index, pageSize, filtros, orden);

      return lstSolicitudes;
  }

}
