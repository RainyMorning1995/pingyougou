package com.pinyougou.search.service.impl;

import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String keywords = (String) searchMap.get("keywords");
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.matchQuery("keyword",keywords));

        NativeSearchQuery searchQuery = searchQueryBuilder.build();
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(searchQuery, TbItem.class);
        long totalElements = tbItems.getTotalElements();
        List<TbItem> itemList = tbItems.getContent();
        int totalPages = tbItems.getTotalPages();
        resultMap.put("rows",itemList);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);
        return resultMap;
    }
}
