package com.pinyougou.sellergoods.service.impl;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo; 									  
import org.apache.commons.lang3.StringUtils;
import com.pinyougou.core.service.CoreServiceImpl;

import tk.mybatis.mapper.entity.Example;

import com.pinyougou.sellergoods.service.GoodsService;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl extends CoreServiceImpl<TbGoods>  implements GoodsService {

	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;

	@Autowired
	private TbItemCatMapper catMapper;

	@Autowired
	private TbSellerMapper sellerMapper;

	@Autowired
	private TbBrandMapper brandMapper;

	@Autowired
	private TbItemMapper itemMapper;

	
	private TbGoodsMapper goodsMapper;

	@Autowired
	public GoodsServiceImpl(TbGoodsMapper goodsMapper) {
		super(goodsMapper, TbGoods.class);
		this.goodsMapper=goodsMapper;
	}


    @Override
    public void add(Goods goods) {
		TbGoods tbGoods = goods.getTbGoods();
		tbGoods.setIsDelete(false);
		tbGoods.setAuditStatus("0");
		goodsMapper.insert(tbGoods);
		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		tbGoodsDescMapper.insert(tbGoodsDesc);




	}

	private void saveItems(Goods goods,TbGoods tbGoods,TbGoodsDesc tbGoodsDesc){
		if ("1".equals(tbGoods.getIsEnableSpec())){
			List<TbItem> tbItems = goods.getTbItems();
			for (TbItem tbItem : tbItems) {
				String spec = tbItem.getSpec();
				String title = tbGoods.getGoodsName();
				Map map = JSON.parseObject(spec, Map.class);
				for (Object key : map.keySet()) {
					String o1 = (String) map.get(key);
					title += "" + o1;
				}
				tbItem.setTitle(title);

				String itemImages = goods.getTbGoodsDesc().getItemImages();
				List<Map> maps = JSON.parseArray(itemImages, Map.class);
				String url = maps.get(0).get("url").toString();
				tbItem.setImage(url);
				Long category3Id = tbGoods.getCategory3Id();
				TbItemCat tbItemCat = catMapper.selectByPrimaryKey(category3Id);
				tbItem.setCategoryid(tbItemCat.getId());
				tbItem.setCategory(tbItemCat.getName());

				tbItem.setCreateTime(new Date());
				tbItem.setUpdateTime(new Date());

				tbItem.setGoodsId(tbGoods.getId());

				TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
				tbItem.setSellerId(tbSeller.getSellerId());
				tbItem.setSeller(tbSeller.getNickName());
				TbBrand tbBrand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
				tbItem.setBrand(tbBrand.getName());
				itemMapper.insert(tbItem);

			}


		}else {

		}
	}

    @Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbGoods> all = goodsMapper.selectAll();
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize, TbGoods goods) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();

        if(goods!=null){			
						if(StringUtils.isNotBlank(goods.getSellerId())){
				criteria.andLike("sellerId","%"+goods.getSellerId()+"%");
				//criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(StringUtils.isNotBlank(goods.getGoodsName())){
				criteria.andLike("goodsName","%"+goods.getGoodsName()+"%");
				//criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(StringUtils.isNotBlank(goods.getAuditStatus())){
				criteria.andLike("auditStatus","%"+goods.getAuditStatus()+"%");
				//criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(StringUtils.isNotBlank(goods.getIsMarketable())){
				criteria.andLike("isMarketable","%"+goods.getIsMarketable()+"%");
				//criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(StringUtils.isNotBlank(goods.getCaption())){
				criteria.andLike("caption","%"+goods.getCaption()+"%");
				//criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(StringUtils.isNotBlank(goods.getSmallPic())){
				criteria.andLike("smallPic","%"+goods.getSmallPic()+"%");
				//criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(StringUtils.isNotBlank(goods.getIsEnableSpec())){
				criteria.andLike("isEnableSpec","%"+goods.getIsEnableSpec()+"%");
				//criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
	
		}
        List<TbGoods> all = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }
	
}
