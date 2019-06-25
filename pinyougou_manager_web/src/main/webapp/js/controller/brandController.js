
var app = new Vue({
    el:"#app",
    data:{
        list:[],
        entity:{},
        ids:[],
        pageNo:1,
        pages:15,
        searchEntity:{}

    },
    methods:{
        findAll:function () {
            axios.get("/brand/findAll.shtml").then(function (reponse) {
                app.list = reponse.data;
            }).catch(function (error) {

            })
        },
        add:function () {
            axios.post('/brand/add.shtml',this.entity).then(function (response) {
                //console.log(response);
                if (response.data.success){
                    app.searchList(1)
                }
            }).catch(function (error) {
                //error.log("1233534");
            })
        },
        update:function () {
            axios.post('/brand/update.shtml',this.entity).then(function (response) {
                if (response.data.success) {
                    app.searchList(1);
                }else {
                    console.log(response.data.message)
                }
            })
        },
        findOne:function (id) {
            axios.get('/brand/findOne/'+id+'.shtml').then(function (response) {
                app.entity = response.data;
            })
        },
        save:function () {
            if (this.entity.id != null) {
                this.update();
            }else {
                this.add();
            }
        },
        dele:function () {
            axios.post('/brand/delete.shtml',this.ids).then(function (response) {
                    app.searchList(1)
            })
        },
        searchList:function (curPage) {
            axios.post('/brand/findPage.shtml?pageNo='+curPage,this.searchEntity).then(function (response) {
                //获取数据
                app.list=response.data.list;

                //当前页
                app.pageNo=curPage;
                //总页数
                app.pages=response.data.pages;
            })
        }
    },
    created:function () {
        this.searchList(1);
    }

});