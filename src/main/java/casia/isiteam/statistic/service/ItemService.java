package casia.isiteam.statistic.service;

import casia.isiteam.statistic.pojo.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    Item findItemById(Long infoType, Long id);

    List<Item> findItemsByIds(Long infoType, Collection<Long> ids);
}
