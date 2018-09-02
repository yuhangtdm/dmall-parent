/**
 * 打开弹窗的公共方法
 * @param url 路径
 * @param title 弹窗标题
 * @param width 弹框宽度
 * @param height 弹框高度
 */
function add(url,title,width,height) {
    layer.open({
        type:2,
        area: [width, height],
        title :'添加品牌',
        content:url,
        maxmin:true,
        shadeClose:true
    })
}

/**
 * 保存或更新的公共方法
 * @param url
 * @param requestData
 */
function saveOrUpdate($,url,requestData) {
    $.ajax({
        url:url,
        type:'POST',
        data:requestData,
        success:function (data) {
            if(data.code==0){
                layer.msg(data.msg,{
                    icon:1,
                    time:1000
                },function () {
                    window.parent.location.reload();//刷新父页面
                    var index=parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);//关闭父窗口
                });
            }else{
                layer.msg(data.msg,{icon:2})
            }
        },
        error:function () {
            layer.msg('服务异常',{icon:2})
        }

    })
}

/**
 * 删除的公共方法
 * @param url 删除的后台路径
 * @param searchId 刷新的按钮
 */
function deleteById(url,searchId) {
    $.ajax({
        type:'GET',
        url:url,
        success:function () {
            layer.msg("删除成功",{
                icon: 1,
                time: 1000
            },function () {
                layer.closeAll();
                $(searchId).click();
            });
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
        request: {
            pageName: 'current',
            limitName: 'size'
        }
    });
}