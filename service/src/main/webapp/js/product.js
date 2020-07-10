$(function () {
    $(".bt").click(function () {
      var power=$("#adminPower").val();
      if(power!="0"){
          alert("您没有权限修改商品信息");
          return false;
      }
      return  true;
    });
});