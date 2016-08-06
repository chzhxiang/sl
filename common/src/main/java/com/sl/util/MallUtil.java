package com.sl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyg on 2016/5/10.
 * 保险商城使用的工具类,用于订单号统一生成,序列号统一生成等
 */
public class MallUtil {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat tf = new SimpleDateFormat("yyyyMMddHHmmssS");
    /**
     * 生成订单号
     * @return
     */
    public static synchronized  String generateOrderId(){
        String id = tf.format(new Date(System.currentTimeMillis()));
        String serial = id.substring(14);
        serial = String.format("%03d",Integer.valueOf(serial)/2);
        String id1 = id.substring(0,14)+serial;
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
        }
        return id1;
    }

    /**
     * 生成三方序列号
     * @return
     */
    public static synchronized  String generateThirdSerial(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
        return tf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 生成发送时间戳
     * @return
     */
    public static String generateSendTime(){
        return df.format(new Date());
    }

    public static int  getBirthYears(String sDate2){
        String[]  aDate;
        String oDate1,  oDate2,  iDays;
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String sDate1 = sp.format(new Date());
        aDate  =  sDate1.split("-"); //当前年
        String  year = aDate[0];
        String[] bDate  =  sDate2.split("-");//生日年
        String birthYear = bDate[0];
        int iYears = new Integer(year)-new Integer(birthYear);//整年差
        if(iYears>0&&Integer.valueOf(aDate[1])<Integer.valueOf(bDate[1])){
            iYears-=1;
        }
        if(iYears>0&&Integer.valueOf(aDate[1])==Integer.valueOf(bDate[1])&&Integer.valueOf(aDate[2])<Integer.valueOf(bDate[2])){
            iYears-=1;
        }
        return  iYears;
    }
    public static void main(String[] args){
        System.out.print(getBirthYears("1998-09-04"));
    }
}
