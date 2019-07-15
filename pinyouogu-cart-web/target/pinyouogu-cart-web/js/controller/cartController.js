var app = new Vue({
    el:'#app',
    data:{
        cartList:[],
        totalMoney:0,//总金额
        totalNum:0 //总数量
    },
    methods:{
        findCartList:function () {
            axios.get('/cart/findCartList.shtml').then(function (response) {
                app.cartList = response.data;
                app.totalMoney=0;
                app.totalNum=0;
                let cartListAll = response.data;

                for (let i = 0; i < cartListAll.length; i++) {
                    let cart = cartListAll[i];
                    for(let j=0;j<cart.orderItemList.length;j++){
                        app.totalNum+=cart.orderItemList[j].num;
                        app.totalMoney+=cart.orderItemList[j].totalFee;
                    }
                }
            })
        },

        addGoodsToCartList:function (itemId, num) {
            axios.get('/cart/addGoodsToCartList.shtml',{
                params:{
                    itemId:itemId,
                    num:num
                }
            }).then(function (response) {
                if (response.data.success) {
                    app.findCartList();
                }else {
                    alert(response.data.message)
                }
            })
        }
    },

    created:function () {
        this.findCartList();
    }
})