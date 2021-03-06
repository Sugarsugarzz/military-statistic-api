package casia.isiteam.statistic.mapper;

import casia.isiteam.statistic.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Mapper
@Repository
public interface ItemMapper {

    Item findItemById(@Param("infoType") Long infoType, @Param("id") Long id);

    List<Item> findItemsByIds(@Param("infoType") Long infoType, @Param("ids") Collection<Long> ids);
}
