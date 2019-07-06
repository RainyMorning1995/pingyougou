package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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



        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();


        String category = (String) searchMap.get("category");
        if(StringUtils.isNotBlank(category)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category", category));
        }

        //3.3 过滤查询 ----商品的品牌的过滤查询

        String brand = (String) searchMap.get("brand");
        if(StringUtils.isNotBlank(brand)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brand", brand));
        }



        //3.4 过滤查询 ----规格的过滤查询 获取到规格的名称 和规格的值  执行过滤查询
        Map<String,String> spec = (Map<String, String>) searchMap.get("spec");//{"网络":"移动4G","机身内存":"16G"}
        if(spec!=null) {
            for (String key : spec.keySet()) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap." + key + ".keyword", spec.get(key)));
            }
        }

        String price = (String) searchMap.get("price");
        if (StringUtils.isNotBlank(price)){
            String[] split = price.split("-");
            if (split[1] == "*"){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gt(split[0]));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }
        }




        searchQueryBuilder.withFilter(boolQueryBuilder);

        NativeSearchQuery searchQuery = searchQueryBuilder.build();

        //分页
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageSize == null) {
            pageSize = 40;
        }
        if (pageNo == null) {
            pageNo = 1;
        }
        searchQuery.setPageable(PageRequest.of(pageNo-1,pageSize));

        String sortField = (String) searchMap.get("sortField");
        String sortType = (String) searchMap.get("sortType");

        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortType)){
            if ("ASC".equals(sortType)){
                Sort sort = new Sort(Sort.Direction.ASC,sortField);
                searchQuery.addSort(sort);
            }else if ("DESC".equals(sortType)){
                Sort sort = new Sort(Sort.Direction.DESC, sortField);
                searchQuery.addSort(sort);
            }else {
                System.out.println("不排序");
            }


        }



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


        if(StringUtils.isNotBlank(category)){
            Map map = searchBrandAndSpecList(category);//{ "brandList",[],"specList":[]}
            resultMap.putAll(map);
        }else {
            //否则 查询默认的商品分类下的品牌和规格的列表
            if(categoryList!=null && categoryList.size()>0) {
                Map map = searchBrandAndSpecList(categoryList.get(0));//{ "brandList",[],"specList":[]}
                resultMap.putAll(map);
            }else{
                resultMap.put("specList", new HashMap<>());
                resultMap.put("brandList",new HashMap<>());
            }

        }



        resultMap.put("rows",itemList);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);
        resultMap.put("categoryList",categoryList);
        return resultMap;
    }


    private Map searchBrandAndSpecList(String category) {
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
