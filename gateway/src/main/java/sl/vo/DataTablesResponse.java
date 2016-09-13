package sl.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZuoMJ on 2015/7/29.
 * desc: DataTables返回参数
 */
public class DataTablesResponse<T> implements Serializable {
    // 记录总数
    private int iTotalRecords = 10;
    // 需要被显示的记录总数
    private int iTotalDisplayRecords = 10;
    // datatables保留属性
    private int sEcho = 0;
    // 数据
    private List<T> aaData = new ArrayList<>();

    public DataTablesResponse(int total, List<T> aaData) {
        this.iTotalRecords = total;
        this.iTotalDisplayRecords = total;
        this.aaData = aaData;
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public List<T> getAaData() {
        return aaData;
    }

    public void setAaData(List<T> aaData) {
        this.aaData = aaData;
    }
}
