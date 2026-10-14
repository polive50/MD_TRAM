package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ConvocatoriaDocEval;
import org.madrid.saii.lib.dto.ConvocatoriaDocEvalDTO;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaDoc
 *
 * @author everis
 */
public enum ConvocatoriaDocEvalToDTO implements Function<ConvocatoriaDocEval, ConvocatoriaDocEvalDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaDocEvalDTO apply(ConvocatoriaDocEval entity) {

        ConvocatoriaDocEvalDTO dto = new ConvocatoriaDocEvalDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getIdConvocatoria() != null) {
                dto.setIdConvocatoria(entity.getIdConvocatoria());
            }
            if (entity.getIdTipoDocumento().getIdValor() != null) {
                dto.setIdTipoDocumento(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getIdTipoDocumento()));
            }
            if(entity.getPersona() != null && entity.getPersona().getIdInteresado() !=null ) {
                dto.setPersona(InteresadoToDTO.INSTANCE.apply(entity.getPersona()));
            }
            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                dto.setEstado(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }
            
            this.calcularEstilo(dto);
        }

        return dto;
    }
    
    
    /**
    *
    * Calcular el estilo del registro
    *
    * @param dto
    * @return
    */
   private void calcularEstilo(final ConvocatoriaDocEvalDTO dto) {
       
       UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
       
       if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK).equals(dto.getEstado().getIdValor())) {
           dto.setEstilo("colorVerde");
       }
       if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_KO).equals(dto.getEstado().getIdValor())) {
           dto.setEstilo("colorRojo");
       }
       if (utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION).equals(dto.getEstado().getIdValor())) {
           dto.setEstilo("colorAmarillo");
       }
   }
}
