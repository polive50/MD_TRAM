package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.TramiteSolicitanteExpedienteDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.domain.TramiteSolicitanteExpediente;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteHistDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.TramiteSolicitanteExpedienteDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteHistToDTO;
import org.madrid.saii.lib.dto.converters.TramiteSolicitanteExpedienteToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

@Service("tramiteSolicitanteExpedienteService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class TramiteSolicitanteExpedienteServiceImpl extends BaseServiceImpl<TramiteSolicitanteExpediente, Integer, TramiteSolicitanteExpedienteDAO> implements TramiteSolicitanteExpedienteService {
    /**
     * Servicio de Interesado
     */
    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;
    
    @Autowired
    public TramiteSolicitanteExpedienteServiceImpl(@Qualifier("tramiteSolicitanteExpedienteDAO") TramiteSolicitanteExpedienteDAO tramiteSolicitanteExpedienteDAO) {
        super();
        this.setDao(tramiteSolicitanteExpedienteDAO); 
    }

    @Override
    public void guardarTramiteSolicitanteExpediente(Integer idTramite, ExpedienteDTO expediente,
            String dniLogin, String cifEntidadLogin) throws ServiceException {
       
        List<InteresadoDTO> listInteresadosDTO = this.interesadoService.obtenerInteresados(dniLogin);
        Interesado usuario=new Interesado();
        if(cifEntidadLogin != null && !listInteresadosDTO.isEmpty()){
            if(expediente.getRepresentanteLegal() != null && expediente.getRepresentanteLegal().getIdInteresado() != null 
                    &&  expediente.getRepresentanteLegal().getCdNumIden().equalsIgnoreCase(dniLogin)){
                
                usuario.setIdInteresado(expediente.getRepresentanteLegal().getIdInteresado()); 
            }else {
                usuario.setIdInteresado(listInteresadosDTO.get(0).getIdInteresado());
            }
            
        }else if(!listInteresadosDTO.isEmpty()){
            if(expediente.getInteresadoDTO() != null && expediente.getInteresadoDTO().getIdInteresado() != null 
                    &&  expediente.getInteresadoDTO().getCdNumIden().equalsIgnoreCase(dniLogin)){
                
                usuario.setIdInteresado(expediente.getInteresadoDTO().getIdInteresado()); 
            }else{
                usuario.setIdInteresado(listInteresadosDTO.get(0).getIdInteresado());
            }
        }

        CatalogosValores tramite= new CatalogosValores();
        tramite.setIdValor(idTramite);
        Expediente expedienteSolicitante= new Expediente();
        expedienteSolicitante.setIdExpediente(expediente.getIdExpediente());
        
        TramiteSolicitanteExpediente tramExpSolicitante= new TramiteSolicitanteExpediente();
        tramExpSolicitante.setCdUsuAlta(dniLogin);
        tramExpSolicitante.setFcAltaFila(new Date());
        tramExpSolicitante.setTramite(tramite);
        tramExpSolicitante.setInteresado(usuario);
        tramExpSolicitante.setExpediente(expedienteSolicitante);
        this.getDao().insert(tramExpSolicitante);

    }
    
    /**
     * Buscar paginadamente Expediente.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDTO>
     * @throws ServiceException the service exception
     */
    @Override
    public List<TramiteSolicitanteExpedienteDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden), TramiteSolicitanteExpedienteToDTO.INSTANCE));
    }
   
    
    

}
