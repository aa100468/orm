package dao;

import domain.Test;
import sqlsession.SqlSessionFactory;
import sqlsession.SqlTask;

import java.util.ArrayList;

public class Dao2 {
        public Test select(){
            return SqlSessionFactory.select(new SqlTask("select * from test1_1 where id = 1;"),Test.class);
        }
}
