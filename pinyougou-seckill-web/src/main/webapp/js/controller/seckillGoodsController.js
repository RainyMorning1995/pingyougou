var app = new Vue({
    el:'#app',
    data:{
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        searchEntity: {}
    },
    methods:{
        findAll:function () {
            axios.get('/seckillGoods/findAll.shtml').then(function (response) {
                app.list = response.data;
            })
        }
    },
    created:function () {
        this.findAll();
    }
})