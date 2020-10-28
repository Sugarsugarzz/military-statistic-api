package casia.isiteam.statistic.mapper;

import casia.isiteam.statistic.pojo.UserRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface UserRecordMapper {

    List<UserRecord> findUserReadRecordByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UserRecord> findDataUpRecordByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<UserRecord> findUserCommentRecordByDate(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
