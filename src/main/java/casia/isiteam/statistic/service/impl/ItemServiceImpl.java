package casia.isiteam.statistic.service.impl;

import casia.isiteam.statistic.mapper.ItemMapper;
import casia.isiteam.statistic.pojo.Item;
import casia.isiteam.statistic.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Override
    public Item findItemById(Long infoType, Long id) {
        return itemMapper.findItemById(infoType, id);
    }

    @Override
    public List<Item> findItemsByIds(Long infoType, Collection<Long> ids) {
        return itemMapper.findItemsByIds(infoType, ids);
    }
}
