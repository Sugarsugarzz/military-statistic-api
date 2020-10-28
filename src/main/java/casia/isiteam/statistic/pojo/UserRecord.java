package casia.isiteam.statistic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord implements Serializable{

    public Long info_type;
    public Long ref_data_id;
    public String user_id;
    public Date insert_time;
    public Date pubtime;
}
