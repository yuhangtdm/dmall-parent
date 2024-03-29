/**
 * 打开弹窗的公共方法
 * 主要用于添加数据
 * @param url 路径
 * @param title 弹窗标题
 * @param width 弹框宽度
 * @param height 弹框高度
 */
function open(url,title,width,height) {
    layer.open({
        type:2,
        area: [width, height],
        title :title,
        content:url,
        maxmin:true,
        shadeClose:true
    });
}

function openWindow(url,title,width,height,callback,full) {
    layer.open({
        type:2,
        area: [width, height],
        title :title,
        content:url,
        maxmin:true,
        shadeClose:true,
        btn: ['确定', '取消'],
        success:function(layero, index){
            if(full){
                layer.full(index);//默认最大化
            }

        },
        yes:function (index, layero) {
            callback(index,layero);
        },
        btn2: function(index, layero){
            layer.close(index);
        },
    });
}

function saveAfter() {
    window.parent.location.reload();//刷新父页面
    var index=parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);//关闭父窗口
}
/**
 * 保存或更新的公共方法
 * 弹窗式保存
 * @param url
 * @param requestData
 */
function saveOrUpdate($,url,requestData) {
    save($,url,requestData,saveAfter);
}

/**
 * 正常保存
 * @param $
 * @param url
 * @param requestData
 */
function save($,url,requestData,callback) {
    var load = layer.load(1);
    $.ajax({
        url:url,
        type:'POST',
        data:requestData,
        success:function (data) {
            if(data.code==0){
                layer.close(load);
                layer.msg(data.msg,{
                    icon:1,
                    time:1000
                },function () {
                    callback();
                });
            }else{
                layer.close(load);
                layer.msg(data.msg,{icon:2})
            }
        },
        error:function (data) {
            layer.close(load);
            var msg=data.responseJSON.msg || '服务异常';
            layer.msg(msg,{icon:2})
        }

    })
}


/**
 * 删除的公共方法
 * @param url 删除的后台路径
 * @param searchId 刷新的按钮
 */
function deleteById(url,callbak) {
    $.ajax({
        type:'GET',
        url:url,
        async:false,
        success:function (data) {
            if(data.code==0){
                layer.msg("删除成功",{
                    icon: 1,
                    time: 1000
                },function () {
                    layer.closeAll();
                    callbak();
                });
            }else {
                layer.closeAll();
                layer.msg(data.msg,{icon:2})
            }
        },
        error:function (data) {
            layer.close(load);
            var msg=data.responseJSON.msg || '服务异常';
            layer.msg(msg,{icon:2})
        }
    })
}

/**
 * 公共分页列表
 * @param table
 * @param id
 * @param url 列表url
 * @param cols 查询列
 */
function pageList(table,id,url,cols) {
    table.render({
        elem: '#'+id,
        id: id,
        url:url,
        cellMinWidth: 100,
        toolbar: '#toolbarDemo',
        text: {
            none: '暂无相关数据'
        },
        cols: cols,
        page:true,
        loading:true,
        request: {
            pageName: 'current',
            limitName: 'size'
        }
    });
}

/**
 * 加载下拉框
 */
function loadSelect($,form){
    $.each($("select[loadData]"),function(){
        var sel = $(this);
        sel.append("<option value=''>请选择</option>");
        var value = sel.attr("value");//默认值
        var dict=sel.attr("dict")||'';
        var bean=sel.attr("bean")||'';
        var method=sel.attr("method")||'';
        $.ajax({
            url : "/common/select",
            type : "post",
            data : {"dict" : dict,"bean":bean,"methodName":method},
            success : function(result){
                for (var i=0; i<result.length; i++){
                    if(value && value == result[i].code){
                        sel.append("<option value="+result[i].code+" selected>"+result[i].value+"</option>");
                    }else{
                        sel.append("<option value="+result[i].code+">"+result[i].value+"</option>");
                    }
                }
                form.render();
            }
        });
    });
}



/**
 * 初始化表单数据
 * 文本框 单选框 复选框 下拉框 日期
 * @param $ jquery对象
 * @param form layui的表单对象
 * @param bean 实体bean(简单对象)
 */
function initFormData($,form,bean,laydate) {
    if(bean){
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
                                elem: '#'+prop
                                ,value: new Date(bean[prop]) //参数即为：2018-08-20 20:08:08 的时间戳
                            });
                        }else {
                            $(this).val(bean[prop]);

                        }
                    }
                }else if(tagName=='img'){
                    $(this).attr('src',value);
                }else if(tagName=='SELECT' || tagName=='TEXTAREA'){
                    $(this).val(value);
                }

            });
        }
    }

    if(form){
        form.render();//更新全部
    }

}

function ztree(url,domObj,treeId) {
    var zTreeObj;
    //var =$(treeId);
    var setting = {
        callback : {
            onRightClick: OnRightClick
        },
        view:{
            fontCss:{"size":"30px;"}
        }
    };
    $.ajax({
        type: 'POST',
        url: url,
        async:false,
        success: function(data){
            $.fn.zTree.init(domObj, setting, eval(data));
            zTreeObj = $.fn.zTree.getZTreeObj(treeId);
        }
    });
    return zTreeObj;
    ;
}

function ztreeAsync(url) {
    var setting = {
        async: {
            enable: true,
            url: url
        },
        callback : {
            onRightClick: OnRightClick
        },
        view:{
            fontCss:{"size":"30px;"}
        }
    };
    return setting;

}

function showSelectTree(treeselect,url,id) {
    treeselect.render(
        {
            elem: "#"+id,
            data: url,
            method: "GET"
        }
    );
}

function dateFormat(d) {
    if(d.time){
        return layui.util.toDateString(d.time);
    }else {
        return '';
    }
}