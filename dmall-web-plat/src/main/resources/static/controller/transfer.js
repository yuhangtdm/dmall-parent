layui.extend({
    formSelects:'formSelects',
    croppers:'croppers'
}).define(['layer', 'laydate', 'form','formSelects'], function(e) {
    var $ = layui.jquery;
    var form = layui.form;
    var layer = layui.layer;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;

    formSelects.config(null, {
        keyName: 'name',
        keyVal: 'id',
    }, false);

    var obj={
        _ajax:function(type,url,successCallback,errorCallBack,async,data,loadding,traditional){
            ajax(type,url,successCallback,errorCallBack,async,data,loadding,traditional);
        },
        init:function (bean,url,formId) {
            if(bean){
                initForm(bean,formId);
            }else{
                if(url){
                    ajax('GET',url,function(data) {
                        if (data && data.code && data.code == 0) {
                            initForm(data.data);
                        } else {
                            layer.msg(data.msg, { icon: 5 });
                        }
                    });
                }
            }
            initSelect(null,formId);
            initFilter(formId);
            form.render();
        },
        initSelect:function (id,formId) {
            initSelect(id,formId);
        },
        initDate : function(formId,format,domId,type){
            initDate(formId,format,domId,type);
        },
        initFilter:function (formId) {
            initFilter(formId);
        },
        formatDate:function (date,pattern) {
            formatDate(date,pattern);
        }
    }

    /**
     * ajax公用方法
     */
    function ajax(type,url,successCallback,errorCallBack,async,data,loadding,traditional) {
        if(traditional==undefined || traditional==null){
            traditional=false;
        }
        if(async==undefined || async==null){
            async=true;
        }

        $.ajax({
            type: type,
            url: url,
            data:data,
            async:async,
            traditional:traditional,
            beforeSend:function(){
                if(loadding){
                    layer.load();
                }
            },
            success: successCallback,
            error:errorCallBack,
            complete:function () {
                if(loadding){
                    layer.closeAll('loading');
                }

            }
        });
    }

    /**
     * 根据对象给表单赋值
     */
    function initForm(bean,formId) {
        for(var prop in bean){
            var value=bean[prop];
            if(Object.prototype.toString.call(value) === "[object Null]"){
                continue;
            }
            if(Object.prototype.toString.call(value) === "[object Object]"){
                initForm(value,formId);
            }
            initData(prop,value,formId);
        }
    }

    /**
     * 给表单赋值的具体方法
     */
    function initData(prop,value,formId){
        $("#"+formId).find("[name='"+prop+"'],[name='"+prop+"[]']").each(function () {
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
            }else if(tagName=='IMG'){
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

    /**
     * 初始化下拉框 包括单选 多选 联动选择  树
     */
    function initSelect(id,formId) {
        if(id){
            select($("#"+id));
        }else{
            if(formId){
                $("#"+formId).find("select[loadData]").each(function(){
                    select($(this));
                });
            }else{
                $("select[loadData]").each(function(){
                    select($(this));
                });
            }
        }
    }

    /**
     * 下拉框具体的加载方法
     */
    function select(sel) {
        sel.empty();
        sel.append("<option value=''>请选择</option>");
        // 下拉框的值
        var value = sel.val() || sel.attr("value");
        // 下拉框的类型 normal-普通单选框  linkage-联动框 tree-树 region-省市区
        var type=sel.attr("loadData");
        //查询的路径
        var url=sel.attr("url")||'';
        //数据字典的key
        var dict=sel.attr("dict")||'';
        //查询下拉框的对象bean
        var bean=sel.attr("bean")||'';
        //查询的方法
        var method=sel.attr("method")||'';
        // 下拉框的宽度
        var linkageWidth=sel.attr("linkageWidth") || 100;
        if(typeof linkageWidth==="string"){
            linkageWidth=parseInt(linkageWidth);
        }
        var xm=sel.attr("xm-select");
        var xmValue=$("input[name='"+xm+"']").val() || value;
        var xmValues=[];
        //多选
        if(xm){
            if(xmValue){
                if(type=='normal' || type=='tree'){
                    var xms=xmValue.split(",");
                    for(var i=0;i<xms.length;i++){
                        xmValues.push(parseInt(xms[i]));
                    }
                }else if(type=='linkage' || type=='region'){
                    var xms=xmValue.split(",");
                    for(var i=0;i<xms.length;i++){
                        xmValues.push(xms[i]);
                    }
                }
            }
            if(type=='normal'){
                if(url){
                    formSelects.data(xm, 'server', {
                        url: url,
                        success:function () {
                            formSelects.value(xm,xmValues);
                        }
                    });
                }else {
                    formSelects.data(xm, 'server', {
                        url: '/common/select?dict='+dict+"&bean="+bean+"&methodName="+method,
                        success:function () {
                            formSelects.value(xm,xmValues);
                        }
                    });
                }
            } else if(type=='region'){
                initRegion(xm,xmValue);
            } else if(type=='tree'){
                formSelects.data(xm, 'server', {
                    url: url,
                    success:function () {
                        formSelects.value(xm,xmValues);
                    }
                });
            } else if(type=='linkage'){
                formSelects.data(xm, 'server', {
                    url: url,
                    linkage: true,
                    linkageWidth: linkageWidth,
                    success:function () {
                        formSelects.value(xm,xmValues);
                    }
                });
            }

        } else {
            // 单选
            if(url){
                ajax('POST',url, function(result){
                    if(result.code==0){
                        for (var i=0; i<result.data.length; i++){
                            sel.append("<option value="+result.data[i].id+">"+result.data[i].name+"</option>");
                        }
                        if(value){
                            sel.val(value);
                        }
                    }
                },null,false);
            }else{
                ajax('POST',"/common/select", function(result){
                    if(result.code==0){
                        for (var i=0; i<result.data.length; i++){
                            sel.append("<option value="+result.data[i].id+">"+result.data[i].name+"</option>");
                        }
                        if(value){
                            sel.val(value);
                        }
                    }
                },null,false,{"dict":dict,"bean":bean,"methodName":method});
            }

        }
        form.render();
    }

    /**
     * 省市区的加载
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

    /**
     * 初始化日期控件
     * @param formId
     */
    function initDate(formId,format,domId,type) {
        if(!format){
            format='yyyy-MM-dd HH:mm:ss';
        }
        if(!type){
            type='date';
        }
        if(domId){
            var val = $("#"+domId).val();
            if (val) {
                laydate.render({
                    elem: '#' + domId,
                    format: format,
                    type:type,
                    value: layui.util.toDateString(parseInt(val), 'yyyy-MM-dd') //参数即为：2018-08-20 20:08:08 的时间戳
                });
            } else {
                laydate.render({
                    elem: '#' + domId,
                    format: format,
                    type:type
                });
            }

        }else{
            if(formId) {
                $("#" + formId).find(".date").each(function () {
                    var val = $(this).val();
                    if (val) {
                        laydate.render({
                            elem: '#' + $(this).attr("id"),
                            format: format,
                            value: layui.util.toDateString(parseInt(val), 'yyyy-MM-dd')
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
    }

    /**
     * 必填项提示框公共方法
     */
    function initFilter(){
        $("[lay-verify='required']").each(function () {
            $(this).on('focus',function () {
                layer.tips('该项必填',this);
            })
        })
    }

    /**
     * 格式化日期
     */
    function formatDate(date, pattern) {
        if (!date || date == undefined) {
            return '';
        }
        if (pattern == undefined) {
            pattern = "yyyy-MM-dd hh:mm:ss";
        }
        return new Date(date).format(pattern);
    }

    e('transfer',obj);
});