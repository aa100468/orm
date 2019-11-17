package sqlsession;

public class SqlTask {

    private String sql;
    private Object[] params;

    public SqlTask(String sql, Object... params) {
        this.sql = sql;
        this.params = params;
    }


    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }

}