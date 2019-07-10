var app = new Vue({
    el: "#app",
    data: {
        num:1,//��Ʒ�Ĺ�������
        specificationItems:JSON.parse(JSON.stringify(skuList[0].spec)),
        sku:skuList[0]
    },
    methods: {
        addNum:function(num){
            num = parseInt(num);
            this.num+=num;//�ӻ��߼�
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

    //���Ӻ��� ��ʼ�����¼���
    created: function () {

    }

})