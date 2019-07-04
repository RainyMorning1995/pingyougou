var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{tbGoods:{},tbGoodsDesc:{itemImages:[],customAttributeItems:[],specificationItems:[]},tbItems:[]},
        ids:[],
        searchEntity:{},
        image_entity:{url:'',color:''},
        itemCat1List:[],
        itemCat2List:[],
        itemCat3List:[],
        brandIdList:[],
        specList:[]
    },
    methods: {
        searchList:function (curPage) {
            axios.post('/goods/search.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll:function () {
            console.log(app);
            axios.get('/goods/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
         findPage:function () {
            var that = this;
            axios.get('/goods/findPage.shtml',{params:{
                pageNo:this.pageNo
            }}).then(function (response) {
                console.log(app);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data.list;
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            }).catch(function (error) {

            })
        },
        //该方法只要不在生命周期的
        add:function () {
            this.entity.tbGoodsDesc.introduction = editor.html();
            axios.post('/goods/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.entity = {tbGoods:{},tbGoodsDesc:{},tbItems:[]};
                    editor.html("");
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            axios.post('/goods/update.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    window.location.href = "goods.html";
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save:function () {
            if(this.entity.tbGoods.id!=null){
                this.update();
            }else{
                this.add();
            }
        },
        findOne:function (id) {
            axios.get('/goods/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;

                editor.html(app.entity.tbGoodsDesc.introduction);
                app.entity.tbGoodsDesc.itemImages = JSON.parse(app.entity.tbGoodsDesc.itemImages);
                app.entity.tbGoodsDesc.customAttributeItems = JSON.parse(app.entity.tbGoodsDesc.customAttributeItems);
                app.entity.tbGoodsDesc.specificationItems = JSON.parse(app.entity.tbGoodsDesc.specificationItems);
                for (var i = 0; i < app.entity.tbItems.length; i++) {
                    var tbItem = app.entity.tbItems[i];
                    tbItem.spec = JSON.parse(tbItem.spec);
                }

            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/goods/delete.shtml',this.ids).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        upload:function () {
            var formDate = new FormData();
            formDate.append("file",file.files[0]);
            axios({
                url:'http://localhost:9083/upload/uploadFile.shtml',
                data: formDate,
                method:'post',
                header:{
                    'Content-Type':'multipart/form-data'
                },
                withCredentials:true
            }).then(function (response) {
                if (response.data.success) {
                    console.log(this);
                    app.image_entity.url = response.data.message;
                    console.log(JSON.stringify(app.image_entity));
                }else {
                    alert(response.data.message)
                }
            })
        },
        addImage_Entity:function () {

            //向数组中添加一个图片的对象
            this.entity.tbGoodsDesc.itemImages.push(this.image_entity);
        },
        findItemCatList:function () {
            axios.get('/itemCat/findParentId/0.shtml').then(function (response) {
                app.itemCat1List = response.data;
            })
        },
        //当点击复选框的时候调用 并影响变量：entity.goodsDesc.specficationItems的值
        updateChecked:function ($event,specName,specValue) {
            let searchObject = this.searchObjectByKey(this.entity.tbGoodsDesc.specificationItems,specName,'attributeName');
            if (searchObject != null) {
                //searchObject====={"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
                if ($event.target.checked) {
                    searchObject.attributeValue.push(specValue);
                } else {
                    searchObject.attributeValue.splice( searchObject.attributeValue.indexOf(specValue),1);
                    if(searchObject.attributeValue.length==0){
                        this.entity.tbGoodsDesc.specificationItems.splice(this.entity.goodsDesc.specificationItems.indexOf(searchObject),1)
                    }
                }
            } else {
                //[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]},{"attributeName":"屏幕尺寸","attributeValue":["6寸","5.5寸"]}]
                this.entity.tbGoodsDesc.specificationItems.push({
                    "attributeName": specName,
                    "attributeValue": [specValue]
                });
            }
        },
        isCheckde:function (specName,speValue) {
            var obj = this.searchObjectByKey(this.entity.tbGoodsDesc.specificationItems,specName,'attributeName');
            if (obj != null) {
                if (obj.attributeValue.indexOf(speValue) != -1 ) {
                    return true;
                }
            }
            return false;
        },
        /**
         *
         * @param list 从该数组中查询[{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}]
         * @param specName  指定查询的属性的具体值 比如 网络
         * @param key  指定从哪一个属性名查找  比如：attributeName
         * @returns {*}
         */
        searchObjectByKey:function (list,specName,key) {
            for(var i=0;i<list.length;i++){
                let specificationItem = list[i];//{"attributeName":"网络制式","attributeValue":["移动3G","移动4G"]}
                if(specificationItem[key]==specName){
                    return specificationItem;
                }
            }
            return null;
        },
        createList:function () {
            this.entity.tbItems = [{'spec':{},'price':0,'num':0,'status':'0','isDefault':'0'}];
            var specificationItems = this.entity.tbGoodsDesc.specificationItems;
            for (var i = 0; i < specificationItems.length; i++) {
                var obj = specificationItems[i];
                this.entity.tbItems = this.addColum(this.entity.tbItems,obj.attributeName,obj.attributeValue);
            }

        },
        addColum:function (list, columnName, columnValue) {
            var newList = [];
            for (var i = 0; i < list.length; i++) {
                var oldRow = list[i];
                for (let j = 0; j < columnValue.length; j++) {
                    var newRow = JSON.parse(JSON.stringify(oldRow));
                    var value = columnValue[j];
                    newRow.spec[columnName] = value;
                    newList.push(newRow);
                }
            }
            return newList;
        }

    },
    watch:{
      'entity.tbGoods.category1Id':function (newval,oldvalue) {

          // this.itemCat3List = [];
          //
          // if (this.entity.tbGoods.id == null) {
          //     delete this.entity.tbGoods.category2Id;
          //     delete this.entity.tbGoods.category1Id;
          //     delete this.entity.tbGoods.typeTemplateId;
          // }
          //alert("test002");
            if (newval != undefined) {
                axios.get('/itemCat/findParentId/'+newval+'.shtml').then(function (response) {

                    app.itemCat2List = response.data;
                    //alert(app.itemCat2List)
                })
            }
      }
      ,
        'entity.tbGoods.category2Id':function (newval,oldvalue) {
            if (newval != undefined){
                axios.get('/itemCat/findParentId/'+newval+'.shtml').then(function (response) {
                    app.itemCat3List = response.data;
                })
            }
        },
        'entity.tbGoods.category3Id':function (newval,oldvalue) {
            if (newval != undefined) {
                axios.get('/itemCat/findOne/'+newval+'.shtml').then(function (response) {
                    //app.entity.tbGoods.typeTemplateId = response.data.typeId;
                    app.$set(app.entity.tbGoods,'typeTemplateId',response.data.typeId)
                })
            }
        },
        'entity.tbGoods.typeTemplateId':function (newval,oldvalue) {
          if (newval != undefined) {
              axios.get('/typeTemplate/findOne/'+newval+'.shtml').then(function (response) {
                  var typeTemplate = response.data;
                  app.brandIdList = JSON.parse(typeTemplate.brandIds);

                  if (app.entity.tbGoods.id == null) {
                      app.entity.tbGoodsDesc.customAttributeItems = JSON.parse(typeTemplate.customAttributeItems)
                  }

              });

              axios.get('/typeTemplate/findSpecList/'+newval+'.shtml').then(function (response) {
                  app.specList = response.data;
              })
          }
        }

    },



    //钩子函数 初始化了事件和
    created: function () {
        this.findItemCatList();

        var request = this.getUrlParam();
        console.log(request);
        this.findOne(request.id);
    }

});
