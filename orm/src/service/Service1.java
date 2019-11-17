package service;

import dao.Dao1;
import domain.Test;
import sqlsession.DaoProxy;

import java.util.ArrayList;

public class Service1 {
    Dao1 dao = (Dao1) DaoProxy.getInstance(Dao1.class);
    public ArrayList<Test> serviceSelect() {

//        dao.update();
        return dao.select();
    }

    public long insert(){
        return dao.insert();
    }
}
