package casia.isiteam.statistic.service.impl;

import casia.isiteam.statistic.mapper.ResultMapper;
import casia.isiteam.statistic.mapper.UserRecordMapper;
import casia.isiteam.statistic.pojo.Result;
import casia.isiteam.statistic.pojo.UserRecord;
import casia.isiteam.statistic.service.ResultService;
import casia.isiteam.statistic.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    ResultMapper resultMapper;

    @Override
    public int saveResult(Result result) {
        return resultMapper.saveResult(result);
    }
}
