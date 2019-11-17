package sqlsession;

import connection.ConnectionPoll;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("all")
public class SqlSessionFactory {

    public static int update(SqlTask sqlTask) {
        int count = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPoll.getConnection();
            preparedStatement = connection.prepareStatement(sqlTask.getSql());
            if (sqlTask.getParams() != null) {
                for (int i = 0; i < sqlTask.getParams().length; i++) {
                    preparedStatement.setObject(i + 1, sqlTask.getParams()[i]);
                }
            }
            count = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    public static int delete(SqlTask sqlTask) {
        return update(sqlTask);
    }

    public static long insert(SqlTask sqlTask,Object... flag) {//flag为空，默认返回新增的主键值
                                                                //不为空，说明有值传入，返回修改值
        long count = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPoll.getConnection();
            preparedStatement = connection.prepareStatement(sqlTask.getSql(), PreparedStatement.RETURN_GENERATED_KEYS);
//            preparedStatement = connection.prepareStatement(sql);
            if (sqlTask.getParams() != null) {
                for (int i = 0; i < sqlTask.getParams().length; i++) {
                    preparedStatement.setObject(i + 1, sqlTask.getParams()[i]);
                }
            }
            count = preparedStatement.executeUpdate();
            if(flag.length == 0){
                ResultSet rs = preparedStatement.getGeneratedKeys();//临时新增的数据
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }




//    public static int insert(String sql, Object... values) {
//        int count = -1;
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        try {
//            connection = ConnectionPoll.getConnection();
//            preparedStatement = connection.prepareStatement(sql);
//            if (values != null) {
//                for (int i = 0; i < values.length; i++) {
//                    preparedStatement.setObject(i + 1, values);
//                }
//            }
//            count = preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (preparedStatement != null) {
//                try {
//                    preparedStatement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return count;
//    }

//    public static <T> ArrayList<T> select(String sql, Class domainType, Object... values) {
//        ArrayList<T> obj = new ArrayList<>();
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//        try {
//            connection = ConnectionPoll.getConnection();
//            preparedStatement = connection.prepareStatement(sql);
//            if (values != null) {
//                for (int i = 0; i < values.length; i++) {
//                    preparedStatement.setObject(i + 1, values);
//                }
//            }
//            rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                Object o = construct(rs, domainType);
//                obj.add((T) o);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return obj;
//    }

    public static <T>T select(SqlTask sqlTask, Class domainType) {
        ArrayList<T> obj = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = ConnectionPoll.getConnection();
            preparedStatement = connection.prepareStatement(sqlTask.getSql());
            if (sqlTask.getParams() != null) {
                for (int i = 0; i < sqlTask.getParams().length; i++) {
                    preparedStatement.setObject(i + 1, sqlTask.getParams()[i]);
                }
            }
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Object o = construct(rs, domainType);
                obj.add((T) o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(obj.size() == 1){
            return obj.get(0);
        }else if (obj.size() > 1){
            return (T)obj;
        }
        return null;
    }


    private static <T> T construct(ResultSet rs, Class domainType) {
        T obj = null;
        try {
            obj = (T) domainType.newInstance();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String metaName = rs.getMetaData().getColumnName(i);
                String humpMeta = lineToHump(metaName);
                setAttribut2(obj, metaName, rs);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private static void setAttribut2(Object obj, String metta, ResultSet rs) {
        Class clazz = null;
        Method method = null;
        try {
            clazz = obj.getClass();
            StringBuffer methodName = new StringBuffer("set");
            String firstName = metta.substring(0,1).toUpperCase();
            String lastName = metta.substring(1);
            String realMethodName = methodName.append(firstName).append(lastName).toString();
            Class fieldType = clazz.getDeclaredField(metta).getType();
            method = clazz.getMethod(realMethodName,fieldType);
            method.invoke(obj, rs.getObject(metta, fieldType));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static void setAttribut1(Object obj, String metta, ResultSet rs) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(metta)) {
                try {
                    field.setAccessible(true);
                    Class fieldType = field.getType();
                    field.set(obj, rs.getObject(metta, fieldType));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String lineToHump(String str) {
        str.toLowerCase();
        String[] strings = str.split("_");
        StringBuffer stringBuffer = new StringBuffer(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            stringBuffer.append(strings[i].substring(0, 1).toUpperCase());
            stringBuffer.append(strings[i].substring(1));
        }
        return stringBuffer.toString();
    }
}
