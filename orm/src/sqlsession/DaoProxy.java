package sqlsession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DaoProxy {

    public static Object getInstance(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},new MethodProxy());
    }

    private static class MethodProxy implements InvocationHandler{
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SQL sql = method.getAnnotation(SQL.class);
            SqlTask sqlTask = new SqlTask(sql.sql(),args);
            if(sql != null){
                switch (sql.type()){
                    case DELETE:
                        return SqlSessionFactory.delete(sqlTask);
                    case INSERT:
                        return SqlSessionFactory.insert(sqlTask);
                    case INSERTNUM:
                        return SqlSessionFactory.insert(sqlTask,true);
                    case UPDATE:
                        return SqlSessionFactory.update(sqlTask);
                    case SELECT:
                        return SqlSessionFactory.select(sqlTask,sql.domainType());
                }
            }
            return null;
        }
    }
}
