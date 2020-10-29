package casia.isiteam.statistic.controller;


import casia.isiteam.statistic.pojo.Item;
import casia.isiteam.statistic.pojo.Result;
import casia.isiteam.statistic.pojo.UserRecord;
import casia.isiteam.statistic.service.ItemService;
import casia.isiteam.statistic.service.UserRecordService;
import casia.isiteam.statistic.util.Kit;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class IndexController {

    @Autowired
    UserRecordService userRecordService;
    @Autowired
    ItemService itemService;


    @RequestMapping("/stat")
    public Result stat(@RequestParam("rid") Long rid,
                       @RequestParam("startTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                       @RequestParam("endTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
        Result result = new Result();

        // 1、统计最多阅读、点赞和评论的信息项
        JSONObject statMost = statMost(startTime, endTime);
        // 2、统计功能模块使用分布情况
        JSONObject statUsage = statUsage(startTime, endTime);
        // 3、统计用户访问类别分布情况
        JSONObject statCategory = statCategory(startTime, endTime);
        // 4、统计用户关注热点词云
        JSONObject statHotspot = statHotspot(startTime, endTime);
        // 5、统计用户需求热点
        JSONObject statMiss = statMiss(startTime, endTime);

        // 整合
        result.setRid(rid);
        result.setStart_time(startTime);
        result.setEnd_time(endTime);
        result.setStat_most(statMost);
        result.setStat_category(statCategory);
        result.setStat_usage(statUsage);
        result.setStat_hotspot(statHotspot);
        result.setStat_miss(statMiss);
        return result;
    }

    /**
     * 一、最大阅读、点赞、评论信息项统计
     */
    private JSONObject statMost(Date startTime, Date endTime) {
        // 统计最大阅读
        JSONObject mostReadJSON = getMost(userRecordService.findUserReadRecordByDate(startTime, endTime));
        // 统计最大点赞
        JSONObject mostLikeJSON = getMost(userRecordService.findUserLikeRecordByDate(startTime, endTime));
        // 统计最大评论
        JSONObject mostCommentJSON = getMost(userRecordService.findUserCommentRecordByDate(startTime, endTime));
        // 整合
        JSONObject obj = new JSONObject();
        obj.put("mostRead", mostReadJSON);
        obj.put("mostLike", mostLikeJSON);
        obj.put("mostComment", mostCommentJSON);
        return obj;
    }

    /**
     * 根据用户记录统计最多的信息项
     * @param records 用户行为记录
     */
    private JSONObject getMost(List<UserRecord> records) {
        Map<String, Integer> map = new HashMap<>();
        records.forEach(r -> {
            String key = r.getInfo_type() + "-" + r.getRef_data_id();
            if (!map.containsKey(key)) {
                map.put(key, 1);
            } else {
                map.put(key, map.get(key) + 1);
            }
        });
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue() - o1.getValue()));
        String[] key = list.get(0).getKey().split("-");  // infoType, id
        Integer count = list.get(0).getValue();
        Item item = itemService.findItemById(Long.parseLong(key[0]), Long.parseLong(key[1]));
        if (item == null)  count = 0;

        String title = Kit.getItemTitle(Integer.parseInt(key[0]), item);

//        System.out.println(list);
        // 整合
        JSONObject obj = new JSONObject();
        obj.put("infoType", Kit.getInfoTypeName(Integer.parseInt(key[0])));
        obj.put("title", title);
        obj.put("count", count);
        System.out.println(Long.parseLong(key[0]) + " - " + title + " - " + count);
        return obj;
    }

    /**
     * 二、用户功能模块使用分布情况
     * 首页、我的✔、头条✔、百科✔、信息产品、智搜
     */
    private JSONObject statUsage(Date startTime, Date endTime) {
        Map<String, Integer> map = new HashMap<>();
        map.put("我的", 0);
        map.put("头条", 0);
        map.put("百科", 0);

        List<UserRecord> records = userRecordService.findUserOperationRecordByDate(startTime, endTime);
        records.forEach(r ->
            map.keySet().forEach(k -> {
                if (r.getOperation().startsWith(k + "-")) {
                    map.put(k, map.get(k) + 1);
                }
            })
        );
//        System.out.println(map);
        // 整合
        JSONObject obj = new JSONObject();
        map.keySet().forEach(k -> obj.put(k, map.get(k)));
        return obj;
    }

    /**
     * 三、用户信息项访问类别分布情况
     * 百科（为主）、头条、期刊、报告，每个信息类型下统计前15个类别
     */
    private JSONObject statCategory(Date startTime, Date endTime) {

        // 整合
        JSONObject obj = new JSONObject();
        return obj;
    }

    /**
     * 四、用户关注热点词云
     * 百科、头条、期刊、报告，每个信息类型下统计前30个热词，分词
     */
    private JSONObject statHotspot(Date startTime, Date endTime) {


        // 整合
        JSONObject obj = new JSONObject();
        return obj;
    }

    /**
     * 五、用户需求热点词云
     * 利用未检索到记录，统计前30个，分词
     */
    private JSONObject statMiss(Date startTime, Date endTime) {


        // 整合
        JSONObject obj = new JSONObject();
        return obj;
    }


}