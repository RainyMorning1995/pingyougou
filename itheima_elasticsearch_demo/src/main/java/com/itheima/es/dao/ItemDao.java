package com.itheima.es.dao;

import com.itheima.es.modal.TbItem;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ItemDao extends ElasticsearchCrudRepository<TbItem,Long> {

}
