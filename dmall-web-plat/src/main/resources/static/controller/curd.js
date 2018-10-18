layui.define(['layer','table','zTree','form'],function (e) {
    var layer = layui.layer;
    var $ = layui.$;
    var table = layui.table;
    var zTree=layui.zTree;
    var form = layui.form;

    var obj={
        openForm:function (url,title,width,height,full) {
            openForm(url,title,width,height,full);
        },
        open: function(url,title,width,height,callback,full){
            open(url,title,width,height,callback,full);
        },
        openTree:function (url,style,title,width,height,callback,full) {
            openTree(url,style,title,width,height,callback,full)
        },
        save:function (url,requestData,callback,type,json) {
            save(url,requestData,callback,type,json);
        },
        saveOrUpdate:function (url,requestData,type,json) {
            saveOrUpdate(url,requestData,type,json);
        },
        deleteById:function (url,callbak) {
            deleteById(url,callbak);
        },
        initPage:function (id,url,cols) {
            initPage(id,url,cols);
        },
        initSimplePage:function (id,url,cols) {
            initSimplePage(id,url,cols);
        },
        toJson:function (formId) {
            return toJson(formId);
        },
        validateNumber:function(number){
            validateNumber(number);
        }
    }

    function validateNumber(number) {
        if(parseInt(number)<0 || number.indexOf(".")>-1 ){
            layer.msg('输入值不能为负数或小数',{type:2});
            return false;
        }

    }

    /**
     * 打开弹窗表单
     * @param url 路径
     * @param title 弹窗标题
     * @param width 弹框宽度
     * @param height 弹框高度
     */
    function openForm(url,title,width,height,full) {
        layer.open({
            type:2,
            area: [width, height],
            title :title,
            content:url,
            maxmin:true,
            shadeClose:true,
            success:function (layero,index) {
                if(full && full==true){
                    layer.full(index);
                }
            }
        });
    }

    /**
     *
     * @param url
     * @param title
     * @param width
     * @param height
     * @param full
     */
    function open(url,title,width,height,callback,full) {
        layer.open({
            type:2,
            area: [width, height],
            title :title,
            content:url,
            maxmin:true,
            shadeClose:true,
            success:function (layero,index) {
                if(full && full==true){
                    layer.full(index);
                }
            },
            btn: ['确定', '取消'],
            yes:function (index, layero) {
                callback(index,layero);
            },
            btn2: function(layero,index){
                layer.close(index);
            },
        });
    }

    /**
     * 打开树
     * @param url
     * @param style
     * @param title
     * @param width
     * @param height
     * @param callback
     * @param full
     */
    function openTree(url,style,title,width,height,callback,full) {
        var obj={
            "url":url,
            "style":style
        };
        var zTreeObj;
        var html = $('<form class="layui-form"><div class="layui-form-item" style="width:360px;"><div class="ztree" id="tree-area" style="max-height:420px;overflow-y:auto;overflow-x:hidden;"></div></div></form>');
        layer.open({
            type:1,
            area: [width, height],
            title :title,
            content:html.html(),
            maxmin:true,
            shadeClose:true,
            success:function (layero,index) {
                zTreeObj=zTree.zTreeAsync(obj);
                if(full && full==true){
                    layer.full(index);
                }
            },
            btn: ['确定', '取消'],
            yes:function (index, layero) {
                var nodes = zTree.getCheckedNodes(true);
                if (callback) {
                    var result = callback(zTree, nodes);
                    if (result) {
                        layer.closeAll();
                    }
                } else {
                    layer.closeAll();
                }
            },
            btn2: function(layero,index){
                layer.close(index);
            },
        });
    }

    /**
     * 有回调的保存
     */
    function save(url,requestData,callback,type,json) {
        var contentType='application/x-www-form-urlencoded';
        var traditional=true;
        if(json){
            requestData=JSON.stringify(requestData);
            contentType='application/json';
            traditional=false;
        }
        var ty='POST';
        if(type){
            ty='POST';
        }
        $.ajax({
            url:url,
            type:ty,
            data:requestData,
            contentType:contentType,
            beforeSend:function(){
               layer.load(1);
            },
            traditional:traditional,
            success:function (data) {
                if(data.code==0){
                    layer.msg(data.msg,{
                        icon:1,
                        time:1000
                    },function () {
                        if(callback){
                            callback();
                        }
                    });
                }else{
                    layer.msg(data.msg,{icon:2})
                }
            },
            complete:function(){
                layer.closeAll('loading');
            },
            error:function (data) {
                var msg="";
                if(data && data.responseJSON && data.responseJSON.msg){
                    msg=data.responseJSON.msg;
                }else{
                    msg='服务故障';
                }
                layer.msg(msg,{icon:2})
            }

        })
    }

    /**
     * 正常表单弹窗的保存
     * @param url
     * @param requestData
     */
    function saveOrUpdate(url,requestData,type,json) {
        save(url,requestData,saveAfter,type,json);
    }

    /**
     * 有回调的删除
     * @param url
     * @param callbak
     */
    function deleteById(url,callback) {
        var load;
        $.ajax({
            type:'GET',
            url:url,
            async:false,
            beforeSend:function(){
                load=layer.load(1);
            },
            success:function (data) {
                if(data.code==0){
                    layer.msg("删除成功",{
                        icon: 1,
                        time: 1000
                    },function () {
                        layer.closeAll();
                        callback();
                    });
                }else {
                    layer.msg(data.msg,{icon:2})
                }
            },
            error:function (data) {
                var msg=data.responseJSON.msg || '服务异常';
                layer.msg(msg,{icon:2})
            }
        })
    }

    /**
     * 表格的初始化
     * @param id
     * @param url
     * @param cols
     */
    function initPage(id,url,cols) {
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
            },
            done:function (res, curr, count) {
                
            }
        });
    }

    function initSimplePage(id,url,cols) {

        table.render({
            elem: '#'+id,
            id: id,
            url:url,
            text: {
                none: '暂无相关数据'
            },
            cols: cols,
            page:true,
            loading:true,
            request: {
                pageName: 'current',
                limitName: 'size'
            },
            done:function (res, curr, count) {

            }
        });
    }

    /**
     * 保存之后关闭弹窗
     */
    function saveAfter() {
        window.parent.location.reload();//刷新父页面
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);//关闭父窗口
    }

    form.verify({
        password:function (value) {
            var passReg='(?![0-9A-Z]+$)(?![0-9a-z]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$';
            var r = '/^[^\u4e00-\u9fa5]+$/';
            if(!passReg.match(value) && r.match(value)){
                return "密码必须包含数字、大小写字母,不包含汉字,且至少六位";
            }
        },
        bigZero:function (value) {
            if(parseFloat(value)<0){
                return "数字必须大于0";
            }
        },
        twoDecimal:function (value) {
            var decimalReg='/^\\d+(\\.\\d{1,2})?$/';
            if(!decimalReg.match(value)){
                return "只能有两位小数点";
            }
        }
    });

    /**
     * 格式化表单对象为json对象
     */
    function toJson(formId) {
        var o={};
        var form=$("#"+formId);
        var data=form.serializeArray();
        $.each(data,function () {
            var name=this.name;
            var value=this.value;
            var paths=name.split(".");
            var len=paths.length;
            var obj=o;
            if(name && name!="undefined"){
                $.each(paths,function (i,e) {
                    if(i==len-1){
                        if(obj[e]){
                            obj[e]=obj[e]+","+value;
                        }else {
                            obj[e]=value || '';
                        }
                    }else {
                        if(!obj[e]){
                            obj[e]={};
                        }
                    }
                    obj=o[e];
                })
            }

        });
        return o;
    }


    e('curd',obj);
})