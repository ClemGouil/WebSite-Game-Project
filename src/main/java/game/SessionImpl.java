package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import game.models.User;
import game.util.ObjectHelper;
import game.util.QueryHelper;

public class SessionImpl implements Session {
    private final Connection conn;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    public SessionImpl(Connection conn) {
        this.conn = conn;
    }

    public void save(Object entity) {

        String insertQuery = QueryHelper.createQueryINSERT(entity);
        int i;
        PreparedStatement pstm = null;

        try {
            pstm = conn.prepareStatement(insertQuery);
            //if (entity.getClass().equals(Inventory.class) || entity.getClass().equals(Level.class)) {
            //    i = 1;
            //} else {
            pstm.setObject(1, 0);
            i = 2;
            //}

            for (String field: ObjectHelper.getFields(entity)) {
                pstm.setObject(i++, ObjectHelper.getter(entity, field));
            }
            pstm.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {

    }

    public Object getByID(Class theClass, int ID) throws NoSuchMethodException {

        PreparedStatement pstm = null;

        try {
            Constructor[] ctors = theClass.getDeclaredConstructors();
            Constructor ctor = null;
            for (int i = 0; i < ctors.length; i++) {
                ctor = ctors[i];
                if (ctor.getGenericParameterTypes().length == 0)
                    break;
            }

            ctor.setAccessible(true);
            Object entity = (Object) ctor.newInstance();

            String selectQuery = QueryHelper.createQuerySELECTbyID(entity);
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, ID);

            ResultSet result = pstm.executeQuery();

            if (result.next()) {
                for (Field field : theClass.getDeclaredFields()) {

                    Class param = field.getType();
                    String type = param.getSimpleName();

                    char[] arr = type.toCharArray();
                    arr[0] = Character.toUpperCase(arr[0]);
                    String newType = new String(arr);
                    String method = "get" + newType;

                    Method mth = result.getClass().getMethod(method, String.class);
                    Object value = mth.invoke(result, field.getName());
                    ObjectHelper.setter(entity, field.getName(), value);
                }
                return entity;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
        public int getID(Object entity) throws SQLException {
            int id = 0;
            PreparedStatement pstm = null;

            if (entity instanceof User) {
                try {
                    String selectQuery = QueryHelper.createQuerySELECTID(entity);
                    pstm = conn.prepareStatement(selectQuery);
        
                    String [] fields = ObjectHelper.getFields(entity);
        
                    pstm.setObject(1, ObjectHelper.getter(entity, fields[0]));
                    ResultSet result = pstm.executeQuery();
        
                    if (result.next()) {
                        id = result.getInt("id");
                    }
        
                } catch (Exception e) {
                    e.printStackTrace();
                } 
                return id;
            }
            else {
            ResultSet result = null;
            try {
                String[] fields = ObjectHelper.getFields(entity);
                List<String> nonNullFields = new ArrayList<>();
                List<Object> nonNullValues = new ArrayList<>();

                // Collect non-null fields and their values
                for (String field : fields) {
                    Object value = ObjectHelper.getter(entity, field);
                    if (value != null) {
                        nonNullFields.add(field);
                        nonNullValues.add(value);
                    }
                }

                // Build the dynamic SELECT query
                String selectQuery = "SELECT id FROM "  + entity.getClass().getSimpleName().toLowerCase() + " WHERE ";
                StringBuilder whereClause = new StringBuilder();

                for (int i = 0; i < nonNullFields.size(); i++) {
                    if (i > 0) {
                        whereClause.append(" AND ");
                    }
                    whereClause.append(nonNullFields.get(i)).append(" = ?");
                }

                selectQuery += whereClause.toString();
                pstm = conn.prepareStatement(selectQuery);

                // Set the values for the prepared statement
                for (int i = 0; i < nonNullValues.size(); i++) {
                    pstm.setObject(i + 1, nonNullValues.get(i));
                }
                logger.info(pstm);
                result = pstm.executeQuery();

                if (result.next()) {
                    id = result.getInt("id");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (result != null) {
                    try {
                        result.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (pstm != null) {
                    try {
                        pstm.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return id;
        }
        }

    public boolean update(Object object) throws SQLException {

        PreparedStatement pstm = null;
        int id = this.getID(object);
        int i = 1;
        if (id == 0) {
            return false;
        }

        String updateQuery = QueryHelper.createQueryUPDATE(object);

        try {
            pstm = conn.prepareStatement(updateQuery);
            String[] fields = ObjectHelper.getFields(object);
            int n = fields.length;

            for (String field: fields) {
                pstm.setObject(i++, ObjectHelper.getter(object, field));
            }
            pstm.setObject(n+1, id);
            logger.info(pstm);
            pstm.executeQuery();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void delete(Object object) {

        PreparedStatement pstm = null;

        try {
            int id = this.getID(object);
            String selectQuery = QueryHelper.createQueryDELETE(object);
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, id);

            pstm.executeQuery();
            logger.info(pstm);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object> findAll(Class theClass) throws NoSuchMethodException {
        List<Object> objects = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet result = null;
    
        try {
            Constructor[] ctors = theClass.getDeclaredConstructors();
            Constructor ctor = null;
            for (int i = 0; i < ctors.length; i++) {
                ctor = ctors[i];
                if (ctor.getGenericParameterTypes().length == 0) {
                    break;
                }
            }
    
            ctor.setAccessible(true);
    
            String selectQuery = QueryHelper.createQuerySELECTALL(theClass);
            pstm = conn.prepareStatement(selectQuery);
            result = pstm.executeQuery();
    
            while (result.next()) {
                Object entity = ctor.newInstance();
                for (Field field : theClass.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
    
                    // Récupère la valeur depuis le ResultSet en fonction du type de champ
                    Object value;
                    if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                        value = result.getInt(fieldName);
                    } else {
                        // Gérez d'autres types si nécessaire
                        value = result.getObject(fieldName); // Par défaut, récupère l'objet depuis la base de données
                    }
    
                    // Utilisation de ObjectHelper pour setter l'attribut sur l'objet entity
                    ObjectHelper.setter(entity, fieldName, value);
                }
                objects.add(entity);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources (ResultSet, PreparedStatement)
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    
        return objects;
    }

    @Override
    public List<Object> findAll(Object object, List args) {

        List<Object> objects = new ArrayList<>();
        Class theClass = object.getClass();
        PreparedStatement pstm = null;

        try {
            Constructor[] ctors = theClass.getDeclaredConstructors();
            Constructor ctor = null;
            for (int i = 0; i < ctors.length; i++) {
                ctor = ctors[i];
                if (ctor.getGenericParameterTypes().length == 0)
                    break;
            }

            ctor.setAccessible(true);

            String selectQuery = QueryHelper.createQuerySELECT(theClass, args);
            pstm = conn.prepareStatement(selectQuery);
            for (int i = 1; i < (args.size()+1); i++) {
                pstm.setObject(i, ObjectHelper.getter(object, (String) args.get(i-1)));
            }

            ResultSet result = pstm.executeQuery();

            while (result.next()) {
                Object entity = (Object) ctor.newInstance();
                for (Field field : theClass.getDeclaredFields()) {

                    Class param = field.getType();
                    String type = param.getSimpleName();

                    char[] arr = type.toCharArray();
                    arr[0] = Character.toUpperCase(arr[0]);
                    String newType = new String(arr);
                    String method = "get" + newType;

                    Method mth = result.getClass().getMethod(method, String.class);
                    Object value = mth.invoke(result, field.getName());
                    ObjectHelper.setter(entity, field.getName(), value);
                }
                objects.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objects;
    }

    public List<Object> findAllFor(Class theClass, String fieldName, Object fieldValue) throws NoSuchMethodException {
        List<Object> objects = new ArrayList<>();
        PreparedStatement pstm = null;
    
        try {
            Constructor<?>[] ctors = theClass.getDeclaredConstructors();
            Constructor<?> ctor = null;
            for (Constructor<?> c : ctors) {
                if (c.getGenericParameterTypes().length == 0) {
                    ctor = c;
                    break;
                }
            }
    
            if (ctor != null) {
                ctor.setAccessible(true);
            }
    
            String selectQuery = QueryHelper.createQuerySELECTALL(theClass) + " WHERE " + fieldName + " = ?";
            pstm = conn.prepareStatement(selectQuery);
            pstm.setObject(1, fieldValue);
    
            ResultSet result = pstm.executeQuery();
    
            while (result.next()) {
                Object entity = ctor.newInstance();
                for (Field field : theClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = result.getObject(field.getName());
                    field.set(entity, value);
                }
                objects.add(entity);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    
        return objects;
    }


}

