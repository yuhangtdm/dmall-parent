layui.define(['layer'],function (e) {
    var layer=layui.layer,
        $=layui.$;
    var obj={
        open:function (url,title,width,height) {
           open(url,title,width,height);
        },
        save:function (url,requestData,callback) {
            save(url,requestData,callback);
        },
        saveOrUpdate:function (url,requestData) {
            saveOrUpdate(url,requestData);
        },
        deleteById:function (url,callbak) {
            deleteById(url,callbak);
        },
        pageList:function (table,id,url,cols) {
            pageList(table,id,url,cols);
        }
    }

    /**
     * 打开弹窗的公共方法
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

    /**
     * 保存
     */
    function save(url,requestData,callback) {
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
                console.log(data);
                layer.close(load);
                var msg=data.responseJSON.msg || '服务异常';
                layer.msg(msg,{icon:2})
            }

        })
    }

    /**
     * 保存之后关闭弹窗
     */
    function saveAfter() {
        window.parent.location.reload();//刷新父页面
        var index=parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);//关闭父窗口
    }

    function saveOrUpdate(url,requestData) {
        save(url,requestData,saveAfter);
    }

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
                layer.closeAll();
                var msg=data.responseJSON.msg || '服务异常';
                layer.msg(msg,{icon:2})
            }
        })
    }

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



    e('curd',obj);
})