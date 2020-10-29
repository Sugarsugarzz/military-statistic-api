package casia.isiteam.statistic.service.impl;

import casia.isiteam.statistic.mapper.UserRecordMapper;
import casia.isiteam.statistic.pojo.UserRecord;
import casia.isiteam.statistic.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {

    @Autowired
    UserRecordMapper userRecordMapper;

    @Override
    public List<UserRecord> findUserReadRecordByDate(Date startTime, Date endTime) {
        return userRecordMapper.findUserReadRecordByDate(startTime, endTime);
    }

    @Override
    public List<UserRecord> findUserLikeRecordByDate(Date startTime, Date endTime) {
        return userRecordMapper.findUserLikeRecordByDate(startTime, endTime);
    }

    @Override
    public List<UserRecord> findUserCommentRecordByDate(Date startTime, Date endTime) {
        return userRecordMapper.findUserCommentRecordByDate(startTime, endTime);
    }

    @Override
    public List<UserRecord> findUserOperationRecordByDate(Date startTime, Date endTime) {
        return userRecordMapper.findUserOperationRecordByDate(startTime, endTime);
    }
}
