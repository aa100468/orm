package dao;

import domain.Test;
import sqlsession.SQL;
import sqlsession.SQLEnums;

import java.util.ArrayList;

public interface Dao1 {


    @SQL(sql="update test set name = 'iii' where id = 4;",type = SQLEnums.UPDATE)
    int update();


    @SQL(sql="select * from test;",type = SQLEnums.SELECT,domainType = Test.class)
    ArrayList<Test> select();

    @SQL(sql="insert into student values (52,'小米',8),(53,'小米',8)",type = SQLEnums.INSERTNUM,domainType = Test.class)
    long insert();//返回修改值

//    @SQL(sql="insert into student values (54,'小米',8)",type = SQLEnums.INSERT,domainType = Test.class)
//    long insert();//返回新增主键值
}
