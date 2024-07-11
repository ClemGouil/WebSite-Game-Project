package game;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface Session<E> {
    void save(Object entity);
    void close();
    Object getByID(Class theClass, int ID) throws NoSuchMethodException;
    int getID (Object entity) throws SQLException;
    boolean update(Object object) throws SQLException;
    void delete(Object object);
    List<Object> findAll(Class theClass) throws NoSuchMethodException;
    public List<Object> findAllFor(Class<?> theClass, String fieldName, Object fieldValue) throws NoSuchMethodException;
    List<Object> findAll(Object object, List<String> args);
}
