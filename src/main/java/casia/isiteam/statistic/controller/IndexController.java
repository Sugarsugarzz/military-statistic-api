package casia.isiteam.statistic.controller;


import casia.isiteam.statistic.mapper.ItemMapper;
import casia.isiteam.statistic.pojo.Item;
import casia.isiteam.statistic.pojo.Result;
import casia.isiteam.statistic.pojo.UserRecord;
import casia.isiteam.statistic.service.ItemService;
import casia.isiteam.statistic.service.UserRecordService;
import casia.isiteam.statistic.util.Kit;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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


    @RequestMapping("/test")
    public String test() {
        return "Hello";
    }

    @RequestMapping("/stat")
    public Result stat(@RequestParam("rid") Long rid,
                       @RequestParam("startTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                       @RequestParam("endTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
        Result result = new Result();

        // 1、统计最多阅读、点赞和评论的信息项
        JSONObject statMost = statMost(startTime, endTime);


        // 整合
        result.setRid(rid);
        result.setStart_time(startTime);
        result.setEnd_time(endTime);
        result.setStat_most(statMost);
        return result;
    }

    private JSONObject statMost(Date startTime, Date endTime) {
        // 统计最大阅读
        System.out.println("=====================最大阅读量=====================");
        JSONObject mostReadJSON = getMost(userRecordService.findUserReadRecordByDate(startTime, endTime));
        // 统计最大点赞
        System.out.println("=====================最大点赞量=====================");
        JSONObject mostLikeJSON = getMost(userRecordService.findDataUpRecordByDate(startTime, endTime));
        // 统计最大评论
        System.out.println("=====================最大评论量=====================");
        JSONObject mostCommentJSON = getMost(userRecordService.findUserCommentRecordByDate(startTime, endTime));

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
        String[] key = list.get(0).getKey().split("-");
        Integer count = list.get(0).getValue();
        Item item = itemService.findItemById(Long.parseLong(key[0]), Long.parseLong(key[1]));
        if (item == null)  count = 0;

        String title = Kit.getItemTitle(item, Integer.parseInt(key[0]));

//        System.out.println(list);
        JSONObject obj = new JSONObject();
        obj.put("infoType", Kit.getInfoTypeName(Integer.parseInt(key[0])));
        obj.put("title", title);
        obj.put("count", count);
        System.out.println(Long.parseLong(key[0]) + " - " + title + " - " + count);
        return obj;
    }
}