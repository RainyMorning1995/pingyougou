var app = new Vue({
    el:'#app',
    data:{
        username:'A'
    },
    methods:{
        loadUsernName:function () {
            axios.get('/login/getName.shtml').then(function (response) {
                    app.username = response.data;
            })
        }
    },
    created:function () {
        this.loadUsernName();
    }
})