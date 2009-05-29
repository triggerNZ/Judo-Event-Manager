/*
 * GenericDatabase.java
 *
 * Created on 11 August 2008, 22:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.com.jwatmuff.genericdb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author James
 */
public class GenericDatabase implements Database {
    private static final Logger log = Logger.getLogger(GenericDatabase.class);
    
    private Map<Class, GenericDAO> daos = new HashMap<Class, GenericDAO>();
    
    /** Creates a new instance of BasicDatabase */
    public GenericDatabase() {
    }
    
    public <T> void addDAO(GenericDAO<T> dao) {
        daos.put(dao.getDataClass(), dao);
    }
    
    private GenericDAO getForClass(Class aClass) {
        GenericDAO dao = daos.get(aClass);
        if(dao == null)
            throw new RuntimeException("Could not find DAO for class " + aClass);
        return dao;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void add(T item) {
        //log.debug("add: " + item.getClass().getName());
        
        GenericDAO<T> dao = (GenericDAO<T>)getForClass(item.getClass());
        dao.add(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void update(T item) {
        //log.debug("update: " + item.getClass().getName());
        
        GenericDAO<T> dao = (GenericDAO<T>)getForClass(item.getClass());
        dao.update(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void delete(T item) {
        //log.debug("delete: " + item.getClass().getName());
        
        GenericDAO<T> dao = (GenericDAO<T>)getForClass(item.getClass());
        dao.delete(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> aClass, Object id) {
        //log.debug("get: " + aClass.getName());
        
        GenericDAO<T> dao = (GenericDAO<T>)getForClass(aClass);
        //logCounts();
        return dao.get(id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAll(Class<T> aClass, String query, Object... args) {
        assert query != null : "Query may not be null";
        assert query.length() > 0 : "Query may not be empty string";

        //log.debug("findAll: " + aClass.getName() + ": " + query);
        
        GenericDAO<T> dao = (GenericDAO<T>)getForClass(aClass);
        try {
            query = "find" + query.substring(0, 1).toUpperCase() + query.substring(1);
            Method[] methods = dao.getClass().getMethods();
            for(Method method : methods)
                if(method.getName().equals(query))
                    //try { return (Collection<T>)method.invoke(dao, args); } catch(Exception e) { log.error("doh", e); return null; }
                    return (List<T>)ReflectionUtils.invokeMethod(method, dao, args);
            throw new NoSuchMethodException();
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Could not find method for query '" + query + "' on type " + aClass , e);
        } catch(ClassCastException e) {
            throw new RuntimeException("Method did not return expected type", e);            
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T find(Class<T> aClass, String query, Object... args) {
        //log.debug("find: " + aClass.getName() + ": " + query);
        
        GenericDAO<T> dao = (GenericDAO<T>)daos.get(aClass);
        try {
            query = "find" + query.substring(0, 1).toUpperCase() + query.substring(1);
            Method[] methods = dao.getClass().getMethods();
            for(Method method : methods)
                if(method.getName().equals(query))
                    return (T)ReflectionUtils.invokeMethod(method, dao, args);
            throw new NoSuchMethodException();
        } catch(NoSuchMethodException e) {
            throw new RuntimeException("Could not find method for query '" + query + "'", e);
        } catch(ClassCastException e) {
            throw new RuntimeException("Method did not return expected type", e);
        }
    }
}
