layui.define(['layer'], function(e) {
    var obj={
        zTreeAsync:function(obj){
            zTreeAsync(obj);
        }
    }

    function zTreeAsync(obj){
        var url=obj.url;
        var model=obj.model || 'native';
        var id=obj.id;
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
            },
            check: {
                enable: false,
                chkboxType: 'null',
                chkStyle: 'null'
            }
        };
        if(obj.style){
            setting.check.enable=true;
            if(obj.style=='checkbox'){
                setting.check.chkboxType='checkbox';
                setting.check.chkboxType={ "Y": "p", "N": "s" };
            }else if(obj.style=='radio'){
                setting.check.chkboxType='radio';
            }
        }
        var zTreeObj=$.fn.zTree.init($("#"+id), setting);
        return zTreeObj;
    }

    e('zTree',obj);
});