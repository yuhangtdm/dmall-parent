layui.define(['layer','table','zTree','form','transfer'],function (e) {
    var layer = layui.layer;
    var $ = layui.$;
    var table = layui.table;
    var zTree=layui.zTree;
    var form = layui.form;
    var transfer=layui.transfer;

    var obj={
        open: function(url,title,width,height,full,maxmin){
            open(url,title,width,height,full,maxmin);
        },
        openForm:function (url,title,width,height,full) {
            openForm(url,title,width,height,full);
        },
        openTree:function (url,style,title,width,height,callback,full) {
            openTree(url,style,title,width,height,callback,full)
        },
        saveOrUpdate:function (url,requestData,callBack,json) {
            saveOrUpdate(url,requestData,callBack,json);
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
     * 打开页面
     */
    function open(url,title,width,height,full,maxmin) {
        if (!maxmin && maxmin!=false){
            maxmin = true;
        }
        layer.open({
            type:2,
            area: [width, height],
            title :title,
            content:url,
            maxmin:maxmin,
            offset: '5px',
            shadeClose:true,
            success:function (layero,index) {
                if(full && full==true){
                    layer.full(index);
                }
            },
            btn: ['确定', '取消'],
            yes:function (index, layero) {
                //获取子页面的body元素
                var body = layer.getChildFrame('body',index);
                // 执行子页面提交数据的方法
                body.find('button[lay-submit]').click();
            },
            // 层销毁后会回调
            end :function () {
                //  刷新页面的搜索
                $("button[lay-filter='formSearch']").click();
            }
        });
    }

    /**
     * 打开树
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
    function saveOrUpdate(url,requestData,callbak,json) {
        var contentType='application/x-www-form-urlencoded';
        var traditional=true;
        if(json){
            requestData=JSON.stringify(requestData);
            contentType='application/json';
            traditional=false;
        }
        $.ajax({
            type: 'POST',
            url: url,
            data:requestData,
            async:false,
            contentType:contentType,
            traditional:traditional,
            beforeSend:function(){
                parent.layer.load(1);
            },
            success: function (data) {
                parent.layer.closeAll('loading');
                if(data.code==0){
                    parent.layer.msg(data.msg,{
                        icon:1,
                        time:1000
                    },function () {
                        if (callbak){
                            callbak();
                        }else {
                            // 关闭子页面
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                        }
                    });
                }else{
                    parent.layer.msg(data.msg,{icon:2})
                }
            },
            error:function (data) {
                layer.closeAll('loading');
                var msg="";
                if(data && data.responseJSON && data.responseJSON.msg){
                    msg=data.responseJSON.msg;
                }else{
                    msg='服务故障';
                }
                layer.msg(msg,{icon:2})
            },

        });
    }

    /**
     * 公共删除方法
     */
    function deleteById(url,callback) {
        $.ajax({
            type: 'GET',
            url: url,
            async:false,
            beforeSend:function(){
                layer.load(1);
            },
            success: function (data) {
                if(data.code==0){
                    layer.msg(data.msg,{
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
                var msg="";
                if(data && data.responseJSON && data.responseJSON.msg){
                    msg=data.responseJSON.msg;
                }else{
                    msg='服务故障';
                }
                layer.msg(msg,{icon:2})
            },
            complete:function () {
                layer.closeAll('loading');
            }
        });
    }

    /**
     * 公共表格初始化方法
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

    /**
     * 简单表格初始化方法
     */
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
     * 格式化表单对象的方法
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


    /**
     * 弹窗保存之后的回调方法
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



    e('curd',obj);
})