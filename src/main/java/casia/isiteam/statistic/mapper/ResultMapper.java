package casia.isiteam.statistic.mapper;

import casia.isiteam.statistic.pojo.Result;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ResultMapper {

    int saveResult(Result result);
}
