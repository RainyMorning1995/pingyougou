var app = new Vue({
    el:"#app",
    data:{
        payObject:{},
        totalMoney: 0
    },
    methods:{
        createNative:function () {
            axios.get("/pay/createNative.shtml").then(function (response) {
                if (response.data) {
                    //如果存在数据
                    app.payObject = response.data;
                    app.payObject.total_fee = app.payObject.total_fee/100;
                    var qrious = new QRious({
                        element:document.getElementById("qrious"),
                        size:250,
                        level:'H',
                        value:app.payObject.code_url
                    });

                    if (qrious) {
                        app.queryPayStatus(app.payObject.out_trade_no);
                    }

                }
            })
        },
        queryPayStatus:function (out_trade_no) {
            axios.get("/pay/queryPayStatus.shtml",{
                params:{
                    out_trade_no:out_trade_no
                }
            }).then(function (response) {
                if (response.data) {
                    if (response.data.success) {
                        window.location.href = "paysuccess.html?money="+app.payObject.total_fee;
                    }else {
                        if (response.data.message=='超时') {
                            app.createNative();
                        }else {
                            window.location.href = "payfail.html";
                        }
                    }
                }else {
                    alert("支付错误")
                }
            })
        }
    },
    created:function () {
        if (window.location.href.indexOf("pay.html") != -1) {
            this.createNative();
        }else {
            var urlParam = this.getUrlParam();
            if (urlParam.money) {
                this.totalMoney = urlParam.money;
            }
        }

    }
})