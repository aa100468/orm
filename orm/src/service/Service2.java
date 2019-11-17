package service;

import dao.Dao2;
import domain.Test;
import util.GetObj;

import java.util.ArrayList;

public class Service2 {

    Dao2 dao2 = GetObj.getObj("dao.Dao2");
    public Test select(){
        return dao2.select();
    }



}
