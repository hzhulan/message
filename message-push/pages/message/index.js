// pages/message/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {

        //要推送的内容
        push_content_data: [
            //keyword1
            {
                value: "17软件工程A班",
                color: "#4a4a4a"
            },
            //keyword2
            {
                "value": "软件工程导论",
                "color": "#9b9b9b"
            },
            //keyword3
            {
                "value": "P48 第5题",
                "color": "#9b9b9b"
            },
            //keyword4
            {
                "value": "2019-04-17 10:00:00",
                "color": "#9b9b9b"
            },
            //keyword5
            {
                "value": "今天你交作业了吗？ 别忘了，当初为何出发",
                "color": "#9b9b9b"
            }
        ]
    },

    submitMsg() {
        let that = this;
        wx.getSetting({
            withSubscriptions: true,
            success(res) {
                debugger
                that.subscribe();
                // if (res.subscriptionsSetting.itemSettings) {
                //     console.log("保持之前设定")
                // } else {
                   
                // }
            }
        });


    },

    subscribe: function () {
        wx.requestSubscribeMessage({
            tmplIds: ['VhmlrM_adt4a_Wbvgfy6MSG0Y9_U9ZhwFHORShRnmVA'],
            success(res) {
                console.log("订阅成功   " + getApp().globalData.code)
                wx.request({
                    url: 'http://localhost:8081/app/register',
                    dataType: 'json',
                    type: 'GET',
                    data: {
                        "code": getApp().globalData.code,
                        "userCode": "zhangsan"
                    },
                    success: (res) => {
                        debugger
                        console.log("register: " + res.data.code)
                    },
                    fail: (e) => {
                        debugger
                        console.log(e.errMsg)
                    }
                })
            },
            fail: function (e) {
                debugger
                console.error(e.errMsg)
            }
        })
    },


    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    }
})