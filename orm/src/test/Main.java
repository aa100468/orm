package test;

import domain.Test;
import service.Service1;
import service.Service2;
import util.GetObj;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) throws SQLException {

        //非实体类
        Service1 service1 = GetObj.getObj("service.Service1");
//        ArrayList<Test> a = service1.serviceSelect();
//        for (Test test : a) {
//            System.out.println(test.getId() + ":" + test.getName());
//        }

//        实体类
//        Service2 service2 = GetObj.getObj("service.Service2");
//        Test b = service2.select();
//        System.out.println(b);


        System.out.println(service1.insert());


    }
}
