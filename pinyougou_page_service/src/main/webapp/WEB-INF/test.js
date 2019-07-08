var app = new Vue({
    el: "#app",
    data: {
        num:1,//商品的购买数量
        specificationItems:{}
    },
    methods: {
        addNum:function(num){
            num = parseInt(num);
            this.num+=num;//加或者减
            if(this.num<=1){
                this.num=1;
            }
        },
        selectSpecifcation:function (name,value) {
            this.$set(this.specificationItems,name,value);
        },
        isSelected:function (name, value) {
            if (this.specificationItems[name] == value) {
                return true;
            }else {
                return false;
            }
        }
    },

    //钩子函数 初始化了事件和
    created: function () {

    }

})