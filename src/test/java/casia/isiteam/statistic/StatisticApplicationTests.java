package casia.isiteam.statistic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StatisticApplicationTests {

    @Test
    void ansjTest() {
//        // ansj分词器测试
//        String content = "比利时FN; 印度 6核潜艇 ;美国国务院;中国共产党666，我想看歼20的相关热点";
//        Result result = ToAnalysis.parse(content);
//        System.out.println(result.getTerms());
//
//        // 得到Terms
//        List<Term> terms = result.getTerms();
//
//        terms.forEach(t -> {
//            System.out.println(t.getName());  // 拿到词
//            System.out.println(t.getNatureStr());  // 拿到词性
//        });
    }

    @Test
    void fastJSONTest() {
//        String content = "[{\"装备信息\":[\"前沿科技\",\"国外信息\"]},{\"空军\":[\"战机\",\"武器\"]}]";
//
//        JSONArray array = JSONArray.parseArray(content);
//        for (int i = 0; i < array.size(); i++) {
//            JSONObject obj = array.getJSONObject(i);
//            String category = obj.keySet().iterator().next();
//            System.out.println(category);
//        }
    }

}
