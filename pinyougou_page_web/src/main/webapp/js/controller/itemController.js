var app = new Vue({
    el: "#app",
    data: {
        num:1,//
        specificationItems:JSON.parse(JSON.stringify(skuList[0].spec)),
        sku:skuList[0]
    },
    methods: {
        addGoodsToCartList:function () {
            axios.get('http://localhost:9093/cart/addGoodsToCartList.shtml',{
                params:{
                    itemId:this.sku.id,
                    num:this.num
                },
                withCredentials:true
            }).then(function (respone) {
                if (respone.data.success) {
                    window.location.href = "http://localhost:9093/cart.html";
                }else {
                    alert(respone.data.message);
                }
            })
        },
        addNum:function(num){
            num = parseInt(num);
            this.num+=num;//
            if(this.num<=1){
                this.num=1;
            }
        },
        selectSpecifcation:function (name,value) {
            this.$set(this.specificationItems,name,value);
            this.search();
        },
        search:function () {
            for (var i = 0; i < skuList.length; i++) {
                var object = skuList[i];
                if (JSON.stringify(this.specificationItems)==JSON.stringify(skuList[i].spec)) {
                    console.log(object);
                    this.sku= object;
                    break;
                }
            }
        },
        isSelected:function (name, value) {
            if (this.specificationItems[name] == value) {
                return true;
            }else {
                return false;
            }
        }
    },


    created: function () {

    }

})