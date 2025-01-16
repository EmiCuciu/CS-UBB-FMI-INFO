namespace LAB10_NBA.Repository;

public interface IRepository<TId, TE>
{
    /**
    * @param id -the id of the entity to be returned
    *           id must not be null
    * @return the entity with the specified id
    * or null - if there is no entity with the given id
    * @throws IllegalArgumentException if id is null.
    */
    TE? FindOne(TId id);

    /**
     * @return all entities
     */
    List<TE> FindAll();

    /**
     * @param entity must be not null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    TE? Save(TE entity);


    /**
     * removes the entity with the specified id
     *
     * @param id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    TE? Delete(TId id);

    /**
     * @param entity must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    TE? Update(TE entity);
}