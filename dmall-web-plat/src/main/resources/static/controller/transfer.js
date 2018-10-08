layui.extend({
    formSelects:'formSelects',
    treeSelect:'treeSelect'
}).define(['layer', 'laydate', 'form','formSelects','treeSelect'], function(e) {
    var $ = layui.jquery;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;
    var treeSelect = layui.treeSelect;

    formSelects.config(null, {
        keyName: 'name',
        keyVal: 'id',
    }, false);

    var obj={
        init:function (bean,url,formId) {
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
            initSelect(null,formId);
            form.render();
        },
        initSelect:function (id) {
            initSelect(id);
        },
        initDate : function(formId,format){
            initDate(formId,format);
        },
        initSelectTree:function (url,id) {
            initSelectTree(url,id);
        }
    }

    /**
     * 初始化下拉框 包括单选 多选 联动选择  树
     */
    function initSelect(id,formId) {
        if(id){
            select($("#"+id));
        }else{
            $("#"+formId+ " select[loadData]").each(function(){
                select($(this));
            });
        }

    }

    function select(sel) {
        var value = sel.val() || sel.attr("value");
        sel.empty();
        sel.append("<option value=''>请选择</option>");
        //var value = sel.attr("value");
        var dict=sel.attr("dict")||'';//数据字典的key
        var bean=sel.attr("bean")||'';//查询下拉框的对象bean
        var method=sel.attr("method")||'';//查询的方法
        var linkageWidth=sel.attr("linkageWidth") || 100;
        if(typeof linkageWidth==="string"){
            linkageWidth=parseInt(linkageWidth);
        }
        var url=sel.attr("url")||'';//查询的路径
        var xm=sel.attr("xm-select");
        var xmValue=$("input[name='"+xm+"']").val() || value;
        var xmVals=[];
        var type=sel.attr("loadData");
        var query=sel.attr("query");
        if(xm && xmValue){
            if(type=='normal' || type=='tree'){
                var xms=xmValue.split(",");
                for(var i=0;i<xms.length;i++){
                    xmVals.push(parseInt(xms[i]));
                }
            }else if(type=='linkage' || type=='region'){
                var xms=xmValue.split(",");
                for(var i=0;i<xms.length;i++){
                    xmVals.push(xms[i]);
                }
            }
        }
        if(xm && type){
            if(type=='normal'){
                if(url){
                    formSelects.data(xm, 'server', {
                        url: url,
                        success:function () {
                            formSelects.value(xm,xmVals);
                        }
                    });
                }else {
                    formSelects.data(xm, 'server', {
                        url: '/common/select?dict='+dict+"&bean="+bean+"&methodName="+method,
                        success:function () {
                            formSelects.value(xm,xmVals);
                        }
                    });
                }

            } else if(type=='region'){
                initRegion(xm,xmValue);
            } else if(type=='tree'){
                formSelects.data(xm, 'server', {
                    url: url,
                    success:function () {
                        formSelects.value(xm,xmVals);
                    }
                });

            } else if(type=='linkage'){
                formSelects.data(xm, 'server', {
                    url: url,
                    linkage: true,
                    linkageWidth: linkageWidth,
                    success:function () {
                        formSelects.value(xm,xmVals);
                    }
                });
            }
        }else {
            if(url){
                $.ajax({
                    url : url,
                    type : "post",
                    async: false,
                    success : function(result){
                        if(result.code==0){
                            for (var i=0; i<result.data.length; i++){
                                sel.append("<option value="+result.data[i].id+">"+result.data[i].name+"</option>");
                            }
                            if(value){
                                sel.val(value);
                            }
                        }
                    }
                });
            }else{
                $.ajax({
                    url : "/common/select",
                    type : "post",
                    data : {"dict":dict,"bean":bean,"methodName":method,"query":query},
                    async: false,
                    success : function(result){
                        if(result.code==0){
                            for (var i=0; i<result.data.length; i++){
                                sel.append("<option value="+result.data[i].id+">"+result.data[i].name+"</option>");
                            }
                            if(value){
                                sel.val(value);
                            }
                        }
                    }
                });
            }

        }
        form.render();
    }


    /**
     * 省市区的加载
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
        treeSelect.render(
            {
                elem: "#"+id,
                data: url,
                method: "GET"
            }
        );
    }

    /**
     * 初始化日期控件
     * @param formId
     */
    function initDate(formId,format) {
        if(!format){
            format='yyyy-MM-dd HH:mm:ss';
        }
        if(formId) {
            $("#" + formId).find(".date").each(function () {
                var val = $(this).val();
                if (val) {
                    laydate.render({
                        elem: '#' + $(this).attr("id"),
                        format: format,
                        value: layui.util.toDateString(parseInt(val), 'yyyy-MM-dd') //参数即为：2018-08-20 20:08:08 的时间戳
                    });
                } else {
                    laydate.render({
                        elem: '#' + $(this).attr("id"),
                        format: format
                    });
                }
            })
        }
    }

    /**
     * 根据对象给表单赋值
     * @param bean
     */
    function initForm(bean) {
        for(var prop in bean){
            var value=bean[prop];
            if(Object.prototype.toString.call(value) === "[object Null]"){
                continue;
            }
            if(Object.prototype.toString.call(value) === "[object Object]"){
                initForm(value);
            }
            initData(prop,value);
        }

    }

    function initData(prop,value){
        $("[name='"+prop+"'],[name='"+prop+"[]']").each(function () {
            var tagName=$(this)[0].tagName;
            var type=$(this).attr('type');
            if(tagName=='INPUT'){
                if(type=='radio'){
                    $(this).attr('checked',value==$(this).val());
                }else if(type=='checkbox'){
                    //复选框支持数组和字符串以逗号分隔两种
                    if(Object.prototype.toString.call(value) === "[object Array]"){
                        for(var i=0;i<value.length;i++){
                            if($(this).val()==value[i]){
                                $(this).attr('checked',true);
                            }
                        }
                    }else{
                        var vals=value.split(",");
                        for(var i=0;i<vals.length;i++){
                            if($(this).val()==vals[i]){
                                $(this).attr('checked',true);
                            }
                        }
                    }
                }else{
                    $(this).val(value);
                }
            }else if(tagName=='img'){
                $(this).attr('src',value);
            }else if(tagName=='SELECT'){
                //多选框支持数组和字符串以逗号分隔两种
                if(Object.prototype.toString.call(value) === "[object Array]"){
                    var selectValue='';
                    for(var i=0;i<value.length;i++){
                        selectValue=value[i]+",";
                    }
                    $(this).val(selectValue.substring(0,selectValue.length-1));
                }else {
                    $(this).val(value);
                }
            }else if(tagName=='TEXTAREA'){
                $(this).val(value);
            }
        });
    }
    
    e('transfer',obj);
});