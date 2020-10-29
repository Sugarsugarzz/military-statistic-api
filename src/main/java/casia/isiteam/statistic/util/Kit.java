package casia.isiteam.statistic.util;

import casia.isiteam.statistic.pojo.Item;

public class Kit {

    public static String getItemTitle(int infoType, Item item) {
        if (item == null)  return "";
        switch (infoType) {
            case 1:  return item.getInfoTitle();
            case 2:  return item.getName();
            case 3:  return item.getSubjectName();
            case 4:  return item.getPerName();
            case 5:  return item.getReportName();
            default:  return "";
        }
    }

    public static String getInfoTypeName(int infoType) {
        switch (infoType) {
            case 1:  return "头条";
            case 2:  return "百科";
            case 3:  return "期刊";
            case 4:  return "报告";
            case 5:  return "专题";
            default:  return "";
        }
    }

    public static String getSearchInfoTypeName(int infoType) {
        switch (infoType) {
            case 0: return "综合搜索";
            case 1: return "头条";
            case 2: return "百科";
            case 3: return "期刊";
            default: return "";
        }
    }
}
