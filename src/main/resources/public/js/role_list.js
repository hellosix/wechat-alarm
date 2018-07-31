/**
 * Created by lzz on 17/4/10.
 */

$(document).ready(function () {
    $.get("/role/list", function (obj) {
        var res = obj.res;
        var tstr = "";
        for(var i = 0; i < res.length; i++){
            tstr += "<tr>";
            tstr += "<td class='restful-detail' data-id='" + res[i].id + "'>" + res[i].id + "</td>";
            tstr += "<td>" + res[i].role_name + "</td>";
            tstr += "<td><a  target='_blank' href='/log_statistic?service=" + res[i].service + "&timeType=day'>" + res[i].service + "</a></td>";
            tstr += "<td>" + res[i].members + "</td>";
            tstr += "<td>" + timestampToTime(res[i].add_time) + "</td>";
            tstr += "<td>" + timestampToTime(res[i].send_time) + "</td>";
            tstr += "<td><span class='label label-danger remove-role pointer' data-url='/role/delete?roleid=" + res[i].id + "'>remove</span></td>";
            tstr += "</tr>";
        }
        $("#role-table-list").html( tstr );
    });

    init_form();
});

$(document).on("click", ".restful-detail", function () {
    $("#role-id").text( $(this).data("id") );
    $("#restful-modal").modal("show");
});

$(document).on("click",".remove-role",function () {
    var url = $(this).data("url");
    $("#remove-role-confirm").attr("href", url);
    $('#delete-modal').modal({
        keyboard: false
    });
});

$("input[aria-controls='ping']").trigger("click");
$("input[aria-controls='ping']").click(function () {
    $("a[aria-controls='ping']").click();
});
$("input[aria-controls='error_log']").click(function () {
    $("a[aria-controls='error_log']").click();
});


$("input[name='service']").click(function () {
    if( $(this).val() == "" ||  $(this).val() == " "){
        $("input[name='service']").after(allService);
    }
});
$("input[name='service']").bind('input focus propertychange',function () {
    var this_val = $(this).val();
    if( this_val == "" ||  this_val == " "){
        $(".all-service").css("display", "block");
    }else{
        $(".all-service").css("display", "none");
    }
});
// 添加完成后就可以绑定事件
$(".dropdown-li").bind('click',function () {
    var text_value = $(this).text().trim();
    $("input[name='service']").val(text_value);
    $(".all-service").css("display", "none");
});
// 鼠标离开要隐藏掉
$("input[name='service']").blur(function (e) {
    setTimeout(function () { //因为点击事件和鼠标离开事件绑定在同一个对象，所有要延迟一下
        console.log("aa");
        $(".all-service").css("display", "none");
    }, 500);
});

//************** 选择所有成员
$(document).on("click", "input[name='all_members']", function () {
    var target = $(this).data("target");
    if( $(this).is(':checked') ){
        $("[data-target='" + target + "']").each(function(){
            $(this).prop("checked",true);
        });
    }else{
        $("[data-target='" + target + "']").each(function(){
            $(this).removeAttr("checked",false);
        });
    }
});

// ************* 表单检查
// 检查 input
function input_check() {
    $("input[type='text']:visible").parent(".form-group").removeClass("has-error");
    var input_list = $("input[type='text']:visible");
    var flag = true;
    for( var i = 0; i < input_list.length; i++ ){
        var item = $(input_list[i]);
        if( item.val().trim() == ""){
            item.parent(".form-group").addClass("has-error");
            if( flag == true ){
                flag = false;
            }
        }
    }
    var checkbox_arr = [];
    $('input[name="members"]:checked').each(function(){
        checkbox_arr.push( $(this).val() );
    });
    if( checkbox_arr.length == 0 ){
        $("input[name='members']").parents(".form-group").addClass("has-error");
        flag= false;
    }
    return flag;
}

// 表单检查
$( document ).on( 'focus', 'input', function () {
    $(this).parents(".form-group").removeClass("has-error");
} );
$( document ).on( 'blur', 'input', function () {
    if( $(this).val().trim() == "" ){
        $(this).parent(".form-group").addClass("has-error");
    }
} );

$(".add-role").click(function(){
    $("#add-role-model").modal('show');
});


// ************* 表单提交
$("#role-submit").click(function () {
    // 检查 input 是否为空
    if( input_check() == false ){
        return;
    }
    var request_data = {};
    request_data.service = $('input[name="service"]').val();
    request_data.roleName = $('input[name="role_name"]').val();
    var checkbox_arr = [];
    $('input[name="members"]:checked').each(function(){
        checkbox_arr.push( $(this).val() );
    });
    request_data.members = unique(checkbox_arr).join("|");
    var str_data = JSON.stringify(request_data);
    set_cookie("role_form_data", str_data);
    console.log(request_data);
    post("/role/add", request_data, function(){
        $("#add-role-model").modal('hide');
        window.location.href="/role"
    });
});

//**********从cookie中取值初始化表单
function init_form() {
    var role_form_str = get_cookie("role_form_data");
    var role_form_value = JSON.parse(role_form_str);
    $("input[name='service']").val(role_form_value.service);
    $("input[name='role_name']").val(role_form_value.roleName);
    $("select").find("option[value='"+role_form_value.pingTimeout+"']").attr("selected",true);
    var members = role_form_value.members.split("|");
    for (index in members){
        $("input[name='members'][value='"+members[index]+"']").attr("checked", true);
    }
}
// 数组去重，因为选择成员不需要重复
function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}