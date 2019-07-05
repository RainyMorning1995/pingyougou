var app = new Vue({
    el: "#app",
    data: {
        pages:15,
        pageNo:1,
        list:[],
        entity:{},
        ids:[],
        preDott:false,
        nextDott:false,
        searchMap:{'keywords':'','category':'','brand':'',spec:{},'price':'','pageNo':1,'pageSize':40},
        pageLabels:[],
        resultMap:{},
        searchEntity:{}
    },
    methods: {
        clear:function () {
            this.searchMap={'keywords':this.searchMap.keywords,'category':'','brand':'',spec:{},'price':'','pageNo':1,'pageSize':40};
        },
        queryByPage:function (pageNo) {
            pageNo = parseInt(pageNo);
            this.searchMap.pageNo = pageNo;
            this.searchList();
        },
        buildPageLabel:function () {
            this.pageLabels=[];
            //显示以当前页为中心的5个页码
            let firstPage=1;
            let lastPage=this.resultMap.totalPages;//总页数

            if(this.resultMap.totalPages>5){
                //判断 如果当前的页码 小于等于3  pageNo<=3      1 2 3 4 5  显示前5页
                if(this.searchMap.pageNo<=3){
                    firstPage=1;
                    lastPage=5;
                    this.preDott=false;
                    this.nextDott=true;
                }else if(this.searchMap.pageNo>=this.resultMap.totalPages-2){//如果当前的页码大于= 总页数-2    98 99 100
                    firstPage=this.resultMap.totalPages-4;
                    lastPage=this.resultMap.totalPages;
                    this.preDott=true;
                    this.nextDott=false;
                }else{
                    firstPage=this.searchMap.pageNo-2;
                    lastPage=this.searchMap.pageNo+2;
                    this.preDott=true;
                    this.nextDott=true;

                }
            }else{
                this.preDott=false;
                this.nextDott=false;
            }
            for(let i=firstPage;i<=lastPage;i++){
                this.pageLabels.push(i);
            }
        },
        addSearchItem:function (key, value) {

            if (key == 'category' || key == 'brand' || key == 'price') {
                this.searchMap[key] = value;
            }else {

                this.searchMap.spec[key] = value;
            }
            this.searchList();
        },
        removeSearchItem:function (key) {

            if (key == 'category' || key == 'brand' || key == 'price') {
                this.searchMap[key] = '';
            }else {

                delete this.searchMap.spec[key];
            }
            this.searchList();
        },
        searchList:function () {
            axios.post('/itemSearch/search.shtml',this.searchMap).then(function (response) {
                //获取数据
                var flag = JSON.stringify(response.data);
                if (flag != '{}'){
                    app.resultMap=response.data;
                }else {
                    app.resultMap=response.data.rows;
                }
                app.buildPageLabel();

                //当前页
            });
        },
        //查询所有品牌列表
        findAll:function () {
            console.log(app);
            axios.get('/item/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list=response.data;

            }).catch(function (error) {

            })
        },
         findPage:function () {
            var that = this;
            axios.get('/item/findPage.shtml',{params:{
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
            axios.post('/item/add.shtml',this.entity).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update:function () {
            axios.post('/item/update.shtml',this.entity).then(function (response) {
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
            axios.get('/item/findOne/'+id+'.shtml').then(function (response) {
                app.entity=response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele:function () {
            axios.post('/item/delete.shtml',this.ids).then(function (response) {
                console.log(response);
                if(response.data.success){
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }



    },
    //钩子函数 初始化了事件和
    created: function () {
      
        this.searchList();

    }

});
