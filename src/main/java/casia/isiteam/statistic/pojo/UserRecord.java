package casia.isiteam.statistic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * user_read_record、data_up_info、comment_info、user_operate_info、user_search_history
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord implements Serializable{

    public Long info_type;
    public Long ref_data_id;
    public String user_id;
    public String operation;
    public String search_content;
    public Long if_getResult;
    public Date insert_time;
    public Date pubtime;
    public Date oper_time;
}
