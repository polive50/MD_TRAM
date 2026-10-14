package org.madrid.saii.lib.services;

import atlas.core.exceptions.ServiceException;

/**
 * VMM 24/09/26 MD-2725: servicio dedicado a resolver una modificacion de presupuesto/plazo/otra
 * (volcar lo aceptado por el gestor a las tablas finales del expediente cuando la modificacion pasa
 * a Resuelta). Se extrajo de DocumentoPtfrServiceImpl como un bean aparte para que su metodo publico
 * pueda anotarse con REQUIRES_NEW - Spring ignora esa anotacion en llamadas internas (this.metodo())
 * dentro de la misma clase, asi que hacia falta pasar por el proxy de un bean distinto.
 *
 * Con REQUIRES_NEW, cada modificacion se resuelve en su propia transaccion, independiente de la
 * transaccion global del batch: si algo falla a mitad, se hace rollback limpio de todo lo de esa
 * modificacion (ningun ExpedientePresup/ExpedientePresupMonto padre/hijo queda a medias), y un
 * reintento simplemente repite el trabajo desde cero - no hace falta ningun mecanismo de "retomar
 * donde se quedo".
 *
 * @author everis
 */
public interface ResolucionModificacionService {

    /**
     * Resuelve la modificacion indicada: la marca como Resuelta y vuelca lo aceptado por el gestor
     * (presupuesto y/o plazos, segun itModPres/itModPlazo) a las tablas finales del expediente. Si la
     * modificacion ya esta en estado Resuelta (por ejemplo porque hay mas de un envio de
     * SAII_DOCUMENTO_PTFR pendiente para ella), no hace nada.
     *
     * @param idExpedienteModifPresPlazoOtra id de la modificacion a resolver
     * @throws ServiceException si algo falla al aplicar la resolucion - la transaccion de esta
     *             modificacion se deshace por completo
     */
    void aplicarResolucionModificacion(Integer idExpedienteModifPresPlazoOtra) throws ServiceException;

}
