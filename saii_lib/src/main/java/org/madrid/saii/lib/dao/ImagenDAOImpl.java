package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Imagen
 *
 * @author nttdata
 */
@Repository("imagenDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ImagenDAOImpl extends BaseDAOImpl<Imagen, Integer> implements ImagenDAO {

    /**
     *
     */
    @Override
    public List<ImagenDTO> encontrarLogosEuropeos(Integer idConvocatoria) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("si.idImagen, ");
        queryHql.append("si.cdUnidad, ");
        queryHql.append("si.cdFica ");
        queryHql.append("FROM Imagen si ");
        queryHql.append("INNER JOIN ConvocatoriaEur sce ");
        queryHql.append("ON sce.fondoEuropeo.idValor = si.fondoEur.idValor ");
        queryHql.append("WHERE ");
        queryHql.append("sce.convocatoria.idConvocatoria = :idConvocatoria ");
        queryHql.append("AND sce.fcBajaFila is null AND si.fcBajaFila is null ");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        List<ImagenDTO> lstImagenDTO = new ArrayList<>();
        for (Object[] object : executeHQLQueryList) {
            ImagenDTO imagenDTO = new ImagenDTO();
            imagenDTO.setIdImagen((Integer) object[0]);
            imagenDTO.setCdUnidad((Integer) object[1]);
            imagenDTO.setCdFica((String) object[2]);
            lstImagenDTO.add(imagenDTO);
        }
        return lstImagenDTO;
    }

}
