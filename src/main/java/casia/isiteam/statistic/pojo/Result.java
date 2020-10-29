package casia.isiteam.statistic.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {

    public Integer id;
    public Long rid;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date start_time;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date end_time;
    public String stat_most;
    public String stat_usage;
    public String stat_category;
    public String stat_hotspot;
    public String stat_miss;
}
