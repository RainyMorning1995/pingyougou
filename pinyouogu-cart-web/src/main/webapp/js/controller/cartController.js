var app = new Vue({
    el:'#app',
    data:{
        cartList:[],
        totalMoney:0,//总金额
        totalNum:0 ,//总数量
        addressList:[],
        address:{},
        order:{paymentType:'1'}
    },
    methods:{
        submitOrder:function () {
            this.$set(this.order,'receiverAreaName',this.address.address);
            this.$set(this.order,'receiverMobile',this.address.mobile);
            this.$set(this.order,'receiver',this.address.contact);
            axios.post('/order/add.shtml', this.order).then(
                function (response) {
                    if(response.data.success){
                        //跳转到支付页面
                        window.location.href="pay.html";
                    }else{
                        alert(response.data.message);
                    }
                }
            )
        },
        selectedType:function (type) {
            this.order.paymentType = type;
        },
        selectedAddress:function (address) {
            this.address = address;
        },
        //是否选中
        isSelected:function (address) {
            if (address == this.address) {
                return true;
            }else {
                return false;
            }
        }
        ,
        findAddressList:function () {
            axios.get('/address/findAddressListByUserId.shtml').then(function (response) {
                    app.addressList = response.data;
                for (let i = 0; i < app.addressList.length; i++) {
                    if (app.addressList[i].isDefault == '1') {
                        app.address = app.addressList[i];
                    }
                }
            })
        }
        ,
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

        if (window.location.href.indexOf("getOrderInfo.html") != -1) {
            this.findAddressList();
        }

    }
})