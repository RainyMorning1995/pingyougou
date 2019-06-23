
var app = new Vue({
    el:"#app",
    data:{
        list:[],
        entity:{},
        pageNo:1,
        pages:15
    },
    methods:{
        findAll:function () {
            axios.get("/brand/findAll.shtml").then(function (reponse) {
                app.list = reponse.data;
            }).catch(function (error) {

            })
        },
        searchList:function (curPage) {
            axios.post('/brand/findPage.shtml?pageNo='+curPage).then(function (response) {
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