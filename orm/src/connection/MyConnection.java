package connection;

import java.sql.*;

public class MyConnection extends AbstractConnection{

    public MyConnection() {
    }

    public MyConnection(int index) {
        this.index = index;
    }

    private Connection connection;//真是链接
    private int index;//位置

    public Connection getConnection() {
        return connection;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private static String driver = DBConfig.getConfig("driver");
    private static String url = DBConfig.getConfig("url");
    private static String user = DBConfig.getConfig("user");
    private static String password = DBConfig.getConfig("password");

    static{
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    {
        try {
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }
    public PreparedStatement prepareStatement(String sql,int key) throws SQLException {
        return this.connection.prepareStatement(sql,key);
    }


    @Override
    public void close() throws SQLException {
        ConnectionPoll.giveBack(this);
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return null;
    }
}
