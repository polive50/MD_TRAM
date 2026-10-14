package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Imagen;
import org.madrid.saii.lib.dto.ImagenDTO;

/**
 * Interfaz para el DAO de la clase de dominio Imagen
 *
 * @author nttdata
 */
public interface ImagenDAO extends BaseDAO<Imagen, Integer> {

	List<ImagenDTO> encontrarLogosEuropeos(Integer idConvocatoria);
}
