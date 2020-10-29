package casia.isiteam.statistic.controller;


import casia.isiteam.statistic.pojo.Item;
import casia.isiteam.statistic.pojo.Result;
import casia.isiteam.statistic.pojo.UserRecord;
import casia.isiteam.statistic.service.ItemService;
import casia.isiteam.statistic.service.ResultService;
import casia.isiteam.statistic.service.UserRecordService;
import casia.isiteam.statistic.util.Kit;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
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
    @Autowired
    ResultService resultService;


    @RequestMapping("/stat")
    public Result stat(@RequestParam("rid") Long rid,
                       @RequestParam("startTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                       @RequestParam("endTime") @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime) {
        Result result = new Result();

        // 1、统计最多阅读、点赞和评论的信息项
        statMost(startTime, endTime, result);
        // 2、统计功能模块使用分布情况
        statUsage(startTime, endTime, result);
        // 3、统计用户访问类别分布情况 4、统计用户关注热点词云
        statCategoryAndHotSpot(startTime, endTime, result);
        // 5、统计用户需求热点
        statMiss(startTime, endTime, result);

        // 整合
        result.setRid(rid);
        result.setStart_time(startTime);
        result.setEnd_time(endTime);
        // 存储
        resultService.saveResult(result);
        return result;
    }

    /**
     * 一、最大阅读、点赞、评论信息项统计
     */
    private JSONObject statMost(Date startTime, Date endTime, Result result) {
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

        result.setStat_most(obj);
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
    private JSONObject statUsage(Date startTime, Date endTime, Result result) {
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

        result.setStat_usage(obj);
        return obj;
    }

    /**
     * 三、用户信息项访问类别分布情况
     * 百科（为主）、头条、期刊、报告，每个信息类型下统计前15个类别
     * 四、用户关注热点词云
     * 百科、头条、期刊、报告，每个信息类型下统计前30个热词，分词
     */
    private JSONObject statCategoryAndHotSpot(Date startTime, Date endTime, Result result) {
        JSONObject categoryObj = new JSONObject();
        JSONObject hotspotObj = new JSONObject();

        List<UserRecord> records = userRecordService.findUserReadRecordByDate(startTime, endTime);
        // 获取各信息类型下的所有信息项id
        Map<Long, Set<Long>> map = new HashMap<>();
        records.forEach(r -> {
            long infoType = r.getInfo_type();
            if (!map.containsKey(infoType)) {
                map.put(infoType, new HashSet<>());
            } else {
                map.get(infoType).add(r.getRef_data_id());
            }
        });
        System.out.println(map);
        // 根据信息项id集合，获取对应信息项，统计类别和关键词的频次
        map.keySet().forEach(k -> {
            JSONObject categorySubObj = new JSONObject();
            JSONObject hotspotSubObj = new JSONObject();
            Map<String, Integer> categoryCountMap = new HashMap<>();
            Map<String, Integer> hotspotCountMap = new HashMap<>();
            List<Item> items = itemService.findItemsByIds(k, map.get(k));
            // 统计类别和关键词频次
            System.out.println(items);
            items.forEach(item -> {

                // 只有 头条和百科 有类别，统计类别频次
                if (k == 1 || k == 2) {
                    JSONArray array = JSONArray.parseArray(item.getClassifyName());
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject classifyObj = array.getJSONObject(i);
                        String categoryName = classifyObj.keySet().iterator().next();
                        if (!categoryCountMap.containsKey(categoryName)) {
                            categoryCountMap.put(categoryName, 1);
                        } else {
                            categoryCountMap.put(categoryName, categoryCountMap.get(categoryName) + 1);
                        }
                    }
                }

                // 提取热词，统计频次
                String title = Kit.getItemTitle(k.intValue(), item);
                List<Term> terms = ToAnalysis.parse(title).getTerms();
                terms.forEach(term -> {
                    String word = term.getName();
                    if (word.length() > 1 && word.replaceAll("[^a-zA-Z\\u4E00-\\u9FA5]", "").trim().length() != 0) {
                        if (!hotspotCountMap.containsKey(word)) {
                            hotspotCountMap.put(word, 1);
                        } else {
                            hotspotCountMap.put(word, hotspotCountMap.get(word) + 1);
                        }
                    }
                });
            });

            // 类别：将前十五加入结果
            List<Map.Entry<String, Integer>> categoryCountList = new ArrayList<>(categoryCountMap.entrySet());
            categoryCountList.sort((o1, o2) -> o2.getValue() - o1.getValue());
            System.out.println(categoryCountList);
            int size = Math.min(categoryCountList.size(), 15);
            for (int i = 0; i < size; i++) {
                categorySubObj.put(categoryCountList.get(i).getKey(), categoryCountList.get(i).getValue());
            }
            if (k == 1 || k == 2) {  // 只存头条和百科的类别统计
                categoryObj.put(Kit.getInfoTypeName(k.intValue()), categorySubObj);
            }

            // 热词：将前三十加入结果
            List<Map.Entry<String, Integer>> hotspotCountList = new ArrayList<>(hotspotCountMap.entrySet());
            hotspotCountList.sort((o1, o2) -> o2.getValue() - o1.getValue());
            System.out.println(hotspotCountList);
            size = Math.min(hotspotCountList.size(), 30);
            for (int i = 0; i < size; i++) {
                hotspotSubObj.put(hotspotCountList.get(i).getKey(), hotspotCountList.get(i).getValue());
            }
            hotspotObj.put(Kit.getInfoTypeName(k.intValue()), hotspotSubObj);
        });


        // 暂时整合
        JSONObject obj = new JSONObject();
        obj.put("statCategory", categoryObj);
        obj.put("statHotspot", hotspotObj);

        result.setStat_category(categoryObj);
        result.setStat_hotspot(hotspotObj);
        return obj;
    }

    /**
     * 五、用户需求热点词云
     * 利用未检索到记录，统计前30个，分词
     */
    private JSONObject statMiss(Date startTime, Date endTime, Result result) {
        JSONObject obj = new JSONObject();

        List<UserRecord> records = userRecordService.findUserSearchRecordByDate(startTime, endTime);
        // 获取各类型下各词的搜索词频
        Map<Long, Map<String, Integer>> map = new HashMap<>();
        records.forEach(r -> {
            long infoType = r.getInfo_type();
            String searchContent = r.getSearch_content().replace(" ", "");
            if (!map.containsKey(infoType)) {
                map.put(infoType, new HashMap<>());
            } else {
                if (!map.get(infoType).containsKey(searchContent)) {
                    map.get(infoType).put(searchContent, 1);
                } else {
                    map.get(infoType).put(searchContent, map.get(infoType).get(searchContent) + 1);
                }
            }
        });
//        System.out.println(map);
        // 根据词频，对各类型下单词排序，找出前15个
        map.keySet().forEach(k -> {
            JSONObject subObj = new JSONObject();
            List<Map.Entry<String, Integer>> list = new ArrayList<>(map.get(k).entrySet());
            list.sort((o1, o2) -> o2.getValue() - o1.getValue());
//            System.out.println(k + " - " +list);
            int size = Math.min(list.size(), 30);
            for (int i = 0; i < size; i++) {
                subObj.put(list.get(i).getKey(), list.get(i).getValue());
            }
            obj.put(Kit.getSearchInfoTypeName(k.intValue()), subObj);
        });

        result.setStat_miss(obj);
        return obj;
    }


}