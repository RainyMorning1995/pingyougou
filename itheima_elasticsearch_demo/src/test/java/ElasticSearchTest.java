import com.itheima.es.dao.ItemDao;
import com.itheima.es.modal.TbItem;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-es.xml")
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private ItemDao dao;


    @Test
    public void testMapping(){
        boolean index = elasticsearchTemplate.createIndex(TbItem.class);
        boolean b = elasticsearchTemplate.putMapping(TbItem.class);

    }

    @Test
    public void saveDate(){
            TbItem tbItem = new TbItem();
            tbItem.setId(20L);
            tbItem.setTitle("测试商品");
            tbItem.setBrand("三星炸弹");
            tbItem.setSeller("卖家");
            tbItem.setCategory("手机");
            Map<String, String> map = new HashMap<>();
            map.put("网络格式","中国移动");
            map.put("内存","8g");
            tbItem.setSpecMap(map);
            dao.save(tbItem);


    }

    @Test
    public void deleteTest(){
        dao.deleteById(2000L);
    }

    @Test
    public void findAllTest() {
        Iterable<TbItem> all = dao.findAll();
        for (TbItem tbItem : all) {
            System.out.println(tbItem);
        }
    }

    @Test
    public void QueryById() {
        System.out.println(dao.findById(2000L));
    }


    @Test
    public void queryByPageable(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<TbItem> all = dao.findAll(pageRequest);
        for (TbItem tbItem : all) {
            System.out.println(tbItem);
        }

    }


    @Test
    public void queryByWialdQuery() {
        SearchQuery query = new NativeSearchQuery(QueryBuilders.matchQuery("specMap.内存.keyword", "8g"));
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class);
        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数："+totalElements);
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem);
        }

    }


    @Test
    public void queryByListFilter(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        NativeSearchQueryBuilder pinyougou = queryBuilder.withIndices("pinyougou");
        queryBuilder.withTypes("item");

        NativeSearchQueryBuilder withQuery = queryBuilder.withQuery(QueryBuilders.matchQuery("title", "商品"));

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("specMap.网络格式.keyword","中国移动"));

        boolQueryBuilder.filter(QueryBuilders.termQuery("specMap.内存.keyword","8g"));

        queryBuilder.withQuery(boolQueryBuilder);

        SearchQuery build = queryBuilder.build();

        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(build, TbItem.class);
        long totalElements = tbItems.getTotalElements();
        System.out.println("总数："+totalElements);
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem);
        }


    }








}
