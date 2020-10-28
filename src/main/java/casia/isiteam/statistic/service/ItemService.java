package casia.isiteam.statistic.service;

import casia.isiteam.statistic.pojo.Item;

import java.util.Date;
import java.util.List;

public interface ItemService {

    Item findItemById(Long infoType, Long id);
}
