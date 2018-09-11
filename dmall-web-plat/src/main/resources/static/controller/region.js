layui.define(function (e) {
    var $=layui.$;
    $.getJSON('/json/region.js',function (result) {
        var single=Object.create(null);
        var array=new Array();
        $.each(result,function (i,o) {
            single['code']=result[i].code;
            single['name']=result[i].name;
            array.push(single);
            if(result[i].data){
                single = Object.create(null);
                var subKey='province_'+result[i].name;
                single['key']=subKey;
                single['value']=result[i].data;
                layui.data(subKey,single);
            }
            single = Object.create(null);
        });
        single['key']='province';
        single['value']=array;
        layui.data('province',single);
    });

    e("region", null);
});