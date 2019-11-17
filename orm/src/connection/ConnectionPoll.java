package connection;

import java.sql.Connection;

public class ConnectionPoll {

    //三个状态
    private static  final int BUSY_VALUE = 1;
    private static  final int FREE_VALUE = 0;
    private static  final int NULL_VALUE = -1;

    //链接数下限
    private static int minPoolSize = DBConfig.getIntegerValue("minPoolSize","5");
    //链接数上限
    private static int maxPoolSize = DBConfig.getIntegerValue("maxPoolSize","20");
    //我的链接
    private  static  Connection[] connectionList = new Connection[minPoolSize];
    //我的链接的映射关系
    private  static  byte[] connectionBitMap = new byte[minPoolSize];
    //链接数的上限
    private static int total = 0;

    //初始化
    static {
        for(int i = 0; i < connectionBitMap.length; i ++){
            connectionBitMap[i] = -1;
        }
    }

    //找到空闲的位置
    private static  int getFreeIndex(){
        for(int i = 0; i < connectionBitMap.length; i ++){
            if(connectionBitMap[i] == FREE_VALUE){
                return i;
            }
        }
        return -1;
    }

    //找到没创建的位置
    private  static  int getNullIndex(){
        for(int i = 0; i < connectionBitMap.length; i ++){
            if(connectionBitMap[i] == NULL_VALUE){
                return i;
            }
        }
        return -1;
    }

    //扩容
    private  static  int grow(){
        Connection[] newCoonectionList = new Connection[connectionList.length*2];
        byte[] newConnectionBitMap = new byte[connectionBitMap.length*2];
        System.arraycopy(connectionList,0,newConnectionBitMap,0,connectionList.length);
        System.arraycopy(connectionBitMap,0,newConnectionBitMap,0,connectionBitMap.length);
        int firstNullIndex = connectionList.length;
        connectionList = newCoonectionList;
        connectionBitMap = newConnectionBitMap;
        for(int i = firstNullIndex; i < connectionBitMap.length; i++){
            connectionBitMap[i] = -1;
        }
        return firstNullIndex;
    }

    //分配一个我的链接
    private static  Connection distribute(int index){
        if(connectionBitMap[index] == BUSY_VALUE){
            return null;
        }
        Connection myConnection = null;
        if(connectionBitMap[index] == NULL_VALUE){
            myConnection = new MyConnection(index);
            connectionList[index] = myConnection;
            total ++;
        }else if(connectionBitMap[index] == FREE_VALUE){
            myConnection = connectionList[index];
        }
        connectionBitMap[index] = BUSY_VALUE;
        return myConnection;
    }

    //释放连接
    protected static  synchronized void giveBack(MyConnection myConnection){
        connectionBitMap[myConnection.getIndex()] = 0;
    }

    public static synchronized Connection getConnection(){
        int freeIndex = getFreeIndex();
        if(freeIndex > -1){
            return distribute(freeIndex);
        }else if(total < maxPoolSize){
            int nullIndex = getNullIndex();
            if(nullIndex == -1){//等于-1，说明没有找到，需要扩容
                nullIndex = grow();
            }
            return distribute(nullIndex);
        }
        return null;
    }
}
