package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private RedisTemplate redisTemplate;




    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String keywords = (String) searchMap.get("keywords");

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"title","brand","category","seller"));

        searchQueryBuilder.addAggregation(AggregationBuilders.terms("category_group").field("category").size(50));
        //设置高亮
        searchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("title"))
                .withHighlightBuilder(new HighlightBuilder().preTags("<em style=\"color:red\">").postTags("</em>"));

        String category = (String) searchMap.get("category");
        if (StringUtils.isNoneBlank(category)) {
            searchQueryBuilder.withFilter(QueryBuilders.termQuery("category",category));
        }


        NativeSearchQuery searchQuery = searchQueryBuilder.build();
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(searchQuery, TbItem.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                List<T> content = new ArrayList<>();
                if (hits == null || hits.getHits().length <= 0) {
                    return new AggregatedPageImpl<>(content);
                }
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    TbItem tbItem = JSON.parseObject(sourceAsString, TbItem.class);


                    //获取高亮
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highlightField = highlightFields.get("title");
                    if (highlightField != null) {
                        Text[] fragments = highlightField.getFragments();
                        StringBuffer buffer = new StringBuffer();
                        if (fragments != null) {
                            for (Text fragment : fragments) {
                                buffer.append(fragment.toString());
                            }
                        }

                        if (StringUtils.isNoneBlank(buffer.toString())){
                            tbItem.setTitle(buffer.toString());
                        }

                    }
                    content.add((T)tbItem);

                }
                AggregatedPageImpl<T> ts = new AggregatedPageImpl<>(content, pageable, hits.getTotalHits(), searchResponse.getAggregations(), searchResponse.getScrollId());
                return ts;
            }
        });

        Aggregation category_group = tbItems.getAggregation("category_group");
        StringTerms stringTerms = (StringTerms) category_group;

        List<String> categoryList = new ArrayList<>();
        if (stringTerms != null) {
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                String keyAsString = bucket.getKeyAsString();
                categoryList.add(keyAsString);
            }
        }


        long totalElements = tbItems.getTotalElements();
        List<TbItem> itemList = tbItems.getContent();
        int totalPages = tbItems.getTotalPages();

        Map map = SearchBrandAndSpecList(categoryList.get(0));
        resultMap.putAll(map);


        resultMap.put("rows",itemList);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);
        resultMap.put("categoryList",categoryList);
        return resultMap;
    }


    private Map SearchBrandAndSpecList(String category) {
        Map map = new HashMap();
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (typeId != null) {
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList",brandList);
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList",specList);
        }
        return map;

    }


}
