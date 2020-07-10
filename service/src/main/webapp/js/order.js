$(function () {
   $("#address").change(function () {
        var  addressId=$("#address").val();
        $.post("/store/skill/order/getPhone",{"id":addressId},function (data,status){
            $("#phone").val(data);
        });
   })
});