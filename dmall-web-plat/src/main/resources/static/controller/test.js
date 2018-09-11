layui.define(['layer'],function (e) {
    var layer=layui.layer;
    var obj={
        init:function () {
            layer.alert('测试模块化js');
        }
    }
    e('test',obj);
})