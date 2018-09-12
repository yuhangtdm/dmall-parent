layui.config({
    base: '/controller/'
}).extend({
    curd:'/curd',
    formSelects:"/formSelects",
    treeselect:'/treeselect',
    zTree:'/zTree'
}).define(['util', 'layer', 'laydate', 'form','formSelects','treeselect'], function(e) {
    var $ = layui.jquery;
    var form = layui.form;
    var util = layui.util;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;
    var treeselect = layui.treeselect;

    formSelects.config(null, {
        keyName: 'name',            //自定义返回数据中name的key, 默认 name
        keyVal: 'id',            //自定义返回数据中value的key, 默认 value
    }, false);

    var obj={
        init:function (formId,bean,url) {
            if(bean){
                initForm(bean);
            }
            if(url){
                $.ajax({
                    type: 'GET',
                    url: url,
                    success: function(data) {
                        if (data && data.code && data.code == 0) {
                            initForm(data.data);
                        } else {
                            layer.msg(data.message, { icon: 5 });
                        }
                        form.render();
                    },
                    dataType: 'json'
                });
            }
            initSelect();

            initDate(formId);

        },
        initSelect:function () {
            initSelect();
        },
        initDate : function(formId){
            initDate(formId);
        },
        initSelectTree:function (url,id) {
            initSelectTree(url,id);
        }
    }

    function initSelect() {
        $("select[loadData]").each(function(){
            var sel = $(this);
            sel.append("<option value=''>请选择</option>");
            var value = sel.attr("value");
            var dict=sel.attr("dict")||'';//数据字典的key
            var bean=sel.attr("bean")||'';//查询下拉框的对象bean
            var method=sel.attr("method")||'';//查询的方法
            var url=sel.attr("url")||'';//查询的路径
            var xm=sel.attr("xm-select");
            var xmValue=$("input[name='"+xm+"']").val();
            var xmVals=[];
            if(xm && xmValue){
                var xms=xmValue.split(",");
                for(var i=0;i<xms.length;i++){
                    xmVals.push(parseInt(xms[i]));
                }
            }

            var region=sel.attr("loadData");
            if(xm){
                formSelects.data(xm, 'server', {
                    url: '/common/select?dict='+dict+"&bean="+bean+"&methodName="+method,
                    success:function () {
                        formSelects.value(xm,xmVals);
                    }
                });
                if(region){
                    if(region=='region'){
                        initRegion(xm,xmValue);
                    }
                    if(region=='tree'){
                        formSelects.data(xm, 'server', {
                            url: url,
                        });
                        formSelects.value(xm,xmVals);
                    }
                }
            }else {
                $.ajax({
                    url : "/common/select",
                    type : "post",
                    data : {"dict":dict,"bean":bean,"methodName":method},
                    async: false,
                    success : function(result){
                        if(result.code==0){
                            for (var i=0; i<result.data.length; i++){
                                sel.append("<option value="+result.data[i].value+">"+result.data[i].name+"</option>");
                            }
                            if(value){
                                sel.val(value);
                            }
                        }
                    }
                });
            }
            form.render();
        });
    }

    /**
     * 省市区
     * @param xm
     */

    function initRegion(xm,regionValue) {
        $.getJSON('/json/region.js',function (result) {
            formSelects.data(xm, 'local', {
                arr: result.data,
                linkage: true
            });
            var regionVals=regionValue.split(",");
            formSelects.value(xm, regionVals);
        });
    }


    function initSelectTree(url,id) {
        treeselect.render(
            {
                elem: "#"+id,
                data: url,
                method: "GET"
            }
        );
    }

    function initDate(formId) {
        $("#"+formId).find(".date").each(function () {
            laydate.render({
                elem: '#'+$(this).attr("id"),
                type:'datetime',
                format:'yyyy-MM-dd HH:mm:ss'
            });
        })

    }

    function initForm(bean) {
        for(var prop in bean){
            var value=bean[prop];
            $("[name='"+prop+"'],[name='"+prop+"[]']").each(function () {
                var tagName=$(this)[0].tagName;
                var type=$(this).attr('type');
                if(tagName=='INPUT'){
                    if(type=='radio'){
                        $(this).attr('checked',value==$(this).val());
                    }else if(type=='checkbox'){
                        var vals=value.split(",");
                        for(var i=0;i<vals.length;i++){
                            if($(this).val()==vals[i]){
                                $(this).attr('checked',true);
                                break;
                            }
                        }
                    }else{
                        if(prop.lastIndexOf("Time")>-1){
                            laydate.render({
                                elem: '#'+prop,
                                format:'yyyy-MM-dd HH:mm:ss',
                                value: new Date(value) //参数即为：2018-08-20 20:08:08 的时间戳
                            });
                        }else {
                            $(this).val(value);
                        }
                    }
                }else if(tagName=='img'){
                    $(this).attr('src',value);
                }else if(tagName=='SELECT'){
                    $(this).val(value);
                }else if(tagName=='TEXTAREA'){
                    $(this).val(value);
                }

            });
        }
        form.render();//更新全部
    }

    
    e('transfer',obj);
});