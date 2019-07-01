var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{tbGoods:{},tbGoodsDesc:{itemImages:[],customAttributeItems:[]},tbItems:[]},
        ids:[],
        searchEntity:{},
        image_entity:{url:'',color:''},
        itemCat1List:[],
        itemCat2List:[],
        itemCat3List:[],
        brandIdList:[]
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
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save:function () {
            if(this.entity.id!=null){
                this.update();
            }else{
                this.add();
            }
        },
        findOne:function (id) {
            axios.get('/goods/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
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
        findItemCatList:function () {
            axios.get('/itemCat/findParentId/0.shtml').then(function (response) {
                app.itemCat1List = response.data;
            })
        }

    },
    watch:{
      'entity.tbGoods.category1Id':function (newval,oldvalue) {
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
              axios.get('/typeTemplate/findOne/'+newval+".shtml").then(function (response) {
                  var typeTemplate = response.data;
                  app.brandIdList = JSON.parse(typeTemplate.brandIds);

                  app.entity.tbGoodsDesc.customAttributeItems = JSON.parse(typeTemplate.customAttributeItems)
              })
          }
        }

    },



    //钩子函数 初始化了事件和
    created: function () {
        this.findItemCatList();
    }

});
