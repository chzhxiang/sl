package sl.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZuoMJ on 2015/7/29.
 * desc: DataTables分页排序参数
 */
public class DataTablesRequest implements Serializable {
    // 当前起始记录数
    private int start = 0;
    // 每一页显示条数
    private int length = 10;
    //排序参数
    private List<Map<String, Object>> order = new ArrayList<>();

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Map<String, Object>> getOrder() {
        return order;
    }

    public void setOrder(List<Map<String, Object>> order) {
        this.order = order;
    }
}
