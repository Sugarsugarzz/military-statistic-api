package casia.isiteam.statistic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

    public Long id;
    public Long auto_id;

    public String infoTitle;
    public String name;
    public String subjectName;
    public String perName;
    public String reportName;

    public String classifyParentName;
    public String classifyName;
    public String category_ids;

    public String publishTime;
}
