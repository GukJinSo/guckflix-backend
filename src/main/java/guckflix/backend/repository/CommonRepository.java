package guckflix.backend.repository;

public interface CommonRepository<T, C> {

    public C save(T entity);

    public T findById(Long id);

    public void delete(T entity);

}
