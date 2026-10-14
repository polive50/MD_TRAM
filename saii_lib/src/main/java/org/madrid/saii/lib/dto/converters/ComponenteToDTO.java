package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import atlas.core.exceptions.ServiceException;

/**
 * Parser Entity to DTO for Componente
 *
 * @author everis
 */
public enum ComponenteToDTO implements Function<Componente, ComponenteDTO> {

    /**
     *
     */
    INSTANCE;
    
    private static final ThreadLocal<Map<Integer, List<CatalogosValoresDTO>>> cacheCatalogoPeticion = 
            ThreadLocal.withInitial(HashMap::new);

    /**
     *
     */
    @Override
//    public ComponenteDTO apply(Componente entity) {
//
//        ComponenteDTO dto = new ComponenteDTO();
//
//        if (entity != null) {
//
//            BeanUtils.copyProperties(entity, dto);
//            this.replaceNull(dto, entity);
//
//            if (entity.getTipo() != null) {
//                dto.setTipoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipo()));
//            }
//
//            if (entity.getSubTipo() != null) {
//                dto.setSubTipoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getSubTipo()));
//            }
//            
//            if (entity.getSubTipoCatalogo() != null) {
//                dto.setSubTipoCatalogoDTO(CatalogosToDTO.INSTANCE.apply(entity.getSubTipoCatalogo()));
//            }
//
//            if (entity.getTipoDocumento() != null) {
//                dto.setTipoDocumentoDTO(
//                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoDocumento()));
//            }
//
//            if (entity.getCategoria() != null) {
//                dto.setCategoriaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getCategoria()));
//            }
//
//            if (entity.getRegla() != null) {
//                dto.setReglaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getRegla()));
//            }
//
//            try {
//                this.calcularEspacioPDF(dto);
//            } catch (ServiceException e) {
//               // e.printStackTrace();
//            }
//
//            if (Constantes.AfirmacionNegacion.S.equals(entity.getItEditable())) {
//                dto.setItEditable(Boolean.TRUE);
//            } else {
//                dto.setItEditable(Boolean.FALSE);
//            }
//
//            dto.setPuedeRelacionar(this.comprobarPuedeRelacionar(entity));
//            
//            this.calcularComboItems(dto);
//            this.calcularRadioItems(dto);
//            this.calcularCheckItems(dto);
//            
//        }
//
//        return dto;
//    }
   
    public ComponenteDTO apply(Componente entity) {

        ComponenteDTO dto = new ComponenteDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            // Recuperamos la instancia del contexto
            UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);

            // 1. Tipo
            if (entity.getTipo() != null && entity.getTipo().getIdValor() != null) {
                dto.setTipoDTO(utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getTipo().getIdValor()));
            }

            // 2. SubTipo
            if (entity.getSubTipo() != null && entity.getSubTipo().getIdValor() != null) {
                dto.setSubTipoDTO(utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getSubTipo().getIdValor()));
            }
            
            // 3. SubTipoCatalogo
            if (entity.getSubTipoCatalogo() != null) {
                dto.setSubTipoCatalogoDTO(CatalogosToDTO.INSTANCE.apply(entity.getSubTipoCatalogo()));
            }

            // 4. TipoDocumento
            if (entity.getTipoDocumento() != null && entity.getTipoDocumento().getIdValor() != null) {
                dto.setTipoDocumentoDTO(utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getTipoDocumento().getIdValor()));
            }

            // 5. Categoria
            if (entity.getCategoria() != null && entity.getCategoria().getIdValor() != null) {
                dto.setCategoriaDTO(utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getCategoria().getIdValor()));
            }

            // 6. Regla
            if (entity.getRegla() != null && entity.getRegla().getIdValor() != null) {
                dto.setReglaDTO(utilConstantes.getGlobalCatalogosValoresObjeto().get(entity.getRegla().getIdValor()));
            }

            try {
                this.calcularEspacioPDF(dto);
            } catch (ServiceException e) {
               // e.printStackTrace();
            }

            if (Constantes.AfirmacionNegacion.S.equals(entity.getItEditable())) {
                dto.setItEditable(Boolean.TRUE);
            } else {
                dto.setItEditable(Boolean.FALSE);
            }

            dto.setPuedeRelacionar(this.comprobarPuedeRelacionar(entity));
            
            this.calcularComboItems(dto);
            this.calcularRadioItems(dto);
            this.calcularCheckItems(dto);
            
        }

        return dto;
    }
    
    
    public static void limpiarCachePeticion() {
        cacheCatalogoPeticion.get().clear();
    }
    
    /**
     * Calcula el espacio en un pdf de un componente. Es la suma del tamaño del
     * label mas su longitud
     *
     * @param dto
     * @return
     * @throws ServiceException 
     */
//    private ComponenteDTO calcularEspacioPDF(ComponenteDTO dto) throws ServiceException {
//
//        // Si el componente es de tipo documento no calculamos el espacio
//        if (dto.getTipoDocumentoDTO().getIdValor() == null) {
//            
//            CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
//            UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
//            
//            // Si la longitud es nula la seteamos a 0 para evitar null
//            if (dto.getNmLongitud() == null) {
//                dto.setNmLongitud(0);
//            }
//
//            // Calculo Longitud Campo Pdf
//            dto.setLngCalculoCampoPDF(dto.getNmLongitud() + 3f);
//            // Si es de tipo CHECK es el doble del tamaño del valor multiple
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK).equals(dto.getTipoDTO().getIdValor())) {
//                int max = this.maxValorDsMulitple(dto.getDsValorMultiple());
//                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 4f);
//            }
//            // Si es de tipo COMBO es un poco mas del tamaño del mayor valor de
//            // valor multiple
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO).equals(dto.getTipoDTO().getIdValor())) {
//                int max = this.maxValorDsMulitple(dto.getDsValorMultiple());
//                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 1.5f + 4f);
//            }
//            
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CATALOGO).equals(dto.getTipoDTO().getIdValor())) {
//                List<CatalogosValoresDTO> listaValores = catalogosValoresService.buscarPorCatalogo(dto.getSubTipoCatalogoDTO().getIdCatalogo());
//                String[] valores = listaValores.stream()
//                    .map(CatalogosValoresDTO::getDsValor) 
//                    .toArray(String[]::new);
//                String resultado = String.join(", ", valores);
//                int max = this.maxValorDsMulitple(resultado);
//                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 1.5f + 4f);
//            }
//            
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO).equals(dto.getTipoDTO().getIdValor())) {
//                dto.setLngCalculoCampoPDF(Float.valueOf(dto.getDsValorMultiple().length()) * 2.7f);
//            }
//
//            // Si es tipo etiqueta, la longitud del campo es 0
//            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_ETIQUETA).equals(dto.getTipoDTO().getIdValor())) {
//                dto.setLngCalculoCampoPDF(0f);
//            }
//
//            // Longitud de la etiqueta.
//            dto.setLngCalculoEtiquetaPDF((float) dto.getDsNombre().length());
//
//            // El espacio es la etiqueta + mas el campo
//            dto.setLngCalculoEspacioPDF(
//                    dto.getLngCalculoEtiquetaPDF() + dto.getLngCalculoCampoPDF());
//
//            this.calcularPorcentaje(dto);
//        }
//
//        return dto;
//    }
    
    /**
     * Calcula el espacio en un pdf de un componente. Es la suma del tamaño del
     * label mas su longitud
     *
     * @param dto
     * @return
     * @throws ServiceException 
     */
    private ComponenteDTO calcularEspacioPDF(ComponenteDTO dto) throws ServiceException {

        // Si el componente es de tipo documento no calculamos el espacio
        if (dto.getTipoDocumentoDTO().getIdValor() == null) {
            
            CatalogosValoresService catalogosValoresService = ApplicationContextProvider.getBean(CatalogosValoresService.class);
            UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
            
            // Si la longitud es nula la seteamos a 0 para evitar null
            if (dto.getNmLongitud() == null) {
                dto.setNmLongitud(0);
            }

            // Calculo Longitud Campo Pdf
            dto.setLngCalculoCampoPDF(dto.getNmLongitud() + 3f);
            
            // Si es de tipo CHECK es el doble del tamaño del valor multiple
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK).equals(dto.getTipoDTO().getIdValor())) {
                int max = this.maxValorDsMulitple(dto.getDsValorMultiple());
                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 4f);
            }
            
            // Si es de tipo COMBO es un poco mas del tamaño del mayor valor de valor multiple
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO).equals(dto.getTipoDTO().getIdValor())) {
                int max = this.maxValorDsMulitple(dto.getDsValorMultiple());
                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 1.5f + 4f);
            }
            
            // Si es de tipo CATALOGO implementamos la caché de la petición
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CATALOGO).equals(dto.getTipoDTO().getIdValor())) {
                Integer idCatalogo = dto.getSubTipoCatalogoDTO().getIdCatalogo();
                List<CatalogosValoresDTO> listaValores;

                // Obtenemos el mapa de la caché del hilo actual de ejecución
                Map<Integer, List<CatalogosValoresDTO>> cacheLocal = cacheCatalogoPeticion.get();
                
                if (cacheLocal.containsKey(idCatalogo)) {
                    // Si el catálogo ya se buscó en esta petición, lo recuperamos de memoria instantáneamente
                    listaValores = cacheLocal.get(idCatalogo);
                } else {
                    // Si es la primera vez que aparece el catálogo en la petición, va a la base de datos y se guarda
                    listaValores = catalogosValoresService.buscarPorCatalogo(idCatalogo);
                    cacheLocal.put(idCatalogo, listaValores);
                }

                String[] valores = listaValores.stream()
                    .map(CatalogosValoresDTO::getDsValor) 
                    .toArray(String[]::new);
                String resultado = String.join(", ", valores);
                int max = this.maxValorDsMulitple(resultado);
                dto.setLngCalculoCampoPDF(Float.valueOf(max) * 1.5f + 4f);
            }
            
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO).equals(dto.getTipoDTO().getIdValor())) {
                dto.setLngCalculoCampoPDF(Float.valueOf(dto.getDsValorMultiple().length()) * 2.7f);
            }

            // Si es tipo etiqueta, la longitud del campo es 0
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_ETIQUETA).equals(dto.getTipoDTO().getIdValor())) {
                dto.setLngCalculoCampoPDF(0f);
            }

            // Longitud de la etiqueta.
            dto.setLngCalculoEtiquetaPDF((float) dto.getDsNombre().length());

            // El espacio es la etiqueta + mas el campo
            dto.setLngCalculoEspacioPDF(
                    dto.getLngCalculoEtiquetaPDF() + dto.getLngCalculoCampoPDF());

            this.calcularPorcentaje(dto);
        }

        return dto;
    }
    

    /**
     * Calcula los porcentajes
     *
     * @param dto
     * @return
     */
    private ComponenteDTO calcularPorcentaje(ComponenteDTO dto) {

        if (dto.getTipoDTO().getIdValor() != null) {
            Float etiqueta;
            Float campo;
            Float sobra = 0f;

            if (dto.getLngCalculoEtiquetaPDF() < 3) {
                etiqueta = 5.0f;
            } else if (dto.getLngCalculoEtiquetaPDF() == 3) {
                etiqueta = 5.5f;
            } else if (dto.getLngCalculoEtiquetaPDF() == 4) {
                etiqueta = 6f;

            } else if (dto.getLngCalculoEtiquetaPDF() == 5) {
                etiqueta = 7f;
            }

            else if (dto.getLngCalculoEtiquetaPDF() >= 6 && dto.getLngCalculoEtiquetaPDF() < 7) {
                etiqueta = 8.5f;
            } else {

                etiqueta = dto.getLngCalculoEtiquetaPDF() + 1.5f;
            }

            campo = 100 - etiqueta;
            if (campo > dto.getLngCalculoCampoPDF()) {
                campo = dto.getLngCalculoCampoPDF();
                sobra = 100 - etiqueta - campo;
            }

            dto.setLngPorcentajeEtiquetaPDF(etiqueta);
            dto.setLngPorcentajeCampoPDF(campo);
            dto.setLngPorcentajeSobraPDF(sobra);

        }

        return dto;
    }

    /**
     * Calcula el valor maximo del Ds valor Mulitple
     *
     * @param dsValorMultiple
     * @return
     */
    private int maxValorDsMulitple(String dsValorMultiple) {

        int max = 0;
        String[] valores = dsValorMultiple.split(";");
        for (int i = 0; i < valores.length; i++) {
            if (valores[i].length() > max) {
                max = valores[i].length();
            }
        }

        return max;
    }

    /**
     * Comprueba si el componente es relacionable
     *
     * @return Boolean
     */
    private Boolean comprobarPuedeRelacionar(Componente componente) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        List<Integer> componentesRelacionables = new ArrayList<>();
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TIPO_VIA));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NOMBRE_VIA));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NUMERO));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PISO));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PUERTA));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CP));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.MUNICIPIO));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.PROVINCIA));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NIF_NIE));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.RAZON_SOCIAL));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.NOMBRE));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.APELLIDO_1));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.APELLIDO_2));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TELEFONO_FIJO));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.TELEFONO_MOVIL));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CORREO_ELECTRONICO));
        componentesRelacionables.add(utilConstantes.getGlobalComponente().get(Constantes.ComponentesRelacionables.CARGO));

        return componente.getIdComponente() != null
                && componentesRelacionables.contains(componente.getIdComponente());
    }

    /**
     *
     * @param dto
     */
    private void calcularComboItems(ComponenteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (dto.getTipoDTO() != null && dto.getTipoDTO().getIdValor() != null
                && dto.getTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_COMBO))
                && StringUtils.isNotBlank(dto.getDsValorMultiple())) {
            String arrItems[] = dto.getDsValorMultiple().split(";");
            for (String item : arrItems) {
                dto.getLstComboItems().add(item);
            }
        }
    }

    /**
     *
     * @param dto
     */
    private void calcularCheckItems(ComponenteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (dto.getTipoDTO() != null && dto.getTipoDTO().getIdValor() != null
                && dto.getTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_CHECK))
                && StringUtils.isNotBlank(dto.getDsValorMultiple())) {
            String arrItems[] = dto.getDsValorMultiple().split(";");
            for (String item : arrItems) {
                dto.getLstCheckItems().add(item);
            }
        }
    }

    /**
     *
     * @param dto 
     */
    private void calcularRadioItems(ComponenteDTO dto) {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
        if (dto.getTipoDTO() != null && dto.getTipoDTO().getIdValor() != null
                && dto.getTipoDTO().getIdValor().equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoComponente.ID_RADIO))
                && StringUtils.isNotBlank(dto.getDsValorMultiple())) {
            String arrItems[] = dto.getDsValorMultiple().split(";");
            for (String item : arrItems) {
                dto.getLstRadioItems().add(item);
            }
        }
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ComponenteDTO dto, Componente entity) {

        if (StringUtils.isBlank(entity.getDsNombre())) {
            dto.setDsNombre(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsValorMultiple())) {
            dto.setDsValorMultiple(StringUtils.EMPTY);
        }

    }
}
