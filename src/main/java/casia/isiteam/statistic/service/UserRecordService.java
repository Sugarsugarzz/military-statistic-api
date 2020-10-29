package casia.isiteam.statistic.service;

import casia.isiteam.statistic.pojo.UserRecord;

import java.util.Date;
import java.util.List;

public interface UserRecordService {

    List<UserRecord> findUserReadRecordByDate(Date startTime, Date endTime);

    List<UserRecord> findUserLikeRecordByDate(Date startTime, Date endTime);

    List<UserRecord> findUserCommentRecordByDate(Date startTime, Date endTime);

    List<UserRecord> findUserOperationRecordByDate(Date startTime, Date endTime);
}
