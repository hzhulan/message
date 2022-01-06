// index.js
// 获取应用实例

Page({
  data: {
    name: "测试",
   swiperList: []
  },
  onLoad: function(options) {
    
    console.log("load")

    // 发送请求获取数据
    var reqTask = wx.request({
      url: 'https://106.14.11.239:8081/code/aesDecode',
      data: {str: "name12o3"},
      dataType: 'json',
      success: (result) => {
        console.log(result);
      }
    });
      
  }
})
