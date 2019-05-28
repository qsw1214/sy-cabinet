<template>
    <div class="personage-sy">
        <div class="my-div" @click="login">
            <div class="my-img">
                <img class="headPortrait-img" :src="user.avatar"/>
            </div>
            <div class="my-text">
                <div class="my-nickname">
                    <span>{{user.nickname}}</span>
                </div>
                <div class="my-balance">
                    <span class="my-balance-text">账户余额：</span><span class="my-balance-number">{{user.balance}}</span>
                </div>
            </div>
        </div>
            <div class="my-order correlation-div-style" @click="order">
                <div>
                     <img class="personage-images" src="/static/images/indent.svg"/>
                </div>
               <div class="div_text">
                   我的订单
               </div>
            </div>
            <div class="my-wallet correlation-div-style" @click="balance">
                <div>
                    <img class="personage-images" src="/static/images/balance.svg"/>
                </div>
                <div class="div_text">
                    我的余额
                </div>
            </div>  
            <div class="my-wallet correlation-div-style" @click="capital">
                <div>
                    <img class="personage-images" src="/static/images/balance.svg"/>
                </div>
                <div class="div_text">
                    账单详情
                </div>
            </div>
            <div class="my-wallet correlation-div-style" @click="capital">
                <div>
                    <img class="personage-images" src="/static/images/balance.svg"/>
                </div>
                <div class="div_text">
                    设置手机号与密码
                </div>
            </div>
            <div class="my-deposit correlation-div-style" @click="deposit">
                <div>
                    <img class="personage-images" src="/static/images/deposit.svg"/>
                </div>
                <div class="div_text">
                    储物柜押金
                </div>
            </div>
        
            <div class="my-friend correlation-div-style" @click="share">
                <div>
                    <img class="personage-images" src="/static/images/invite.svg"/>
                </div>
                <div class="div_text">
                    邀请好友
                </div>
            </div>
            <div class="my-help correlation-div-style">
                <div>
                    <img class="personage-images" src="/static/images/usinghelp.svg"/>
                </div>
                <div class="div_text">
                    使用帮助
                </div>
            </div>
            <div class="we correlation-div-style">
                <div>
                    <img class="personage-images" src="/static/images/aboutUs.svg"/>
                </div>
                <div class="div_text">
                    关于我们
                </div>
            </div>
            <!-- 自定义弹框开始 -->
            <div class="container" v-if="showModel">
                <div class="content">
                    <div class='authTitle'>
                    需要您的授权
                    </div>
                    <div class='tips'>
                    为了提供更加全面的服务<br/>请在稍后的提示框中点击"允许"
                    </div>
                    <div class='wx_tips'>
                    <div class='wx_tips_title'>云寄存   申请</div>
                    <div class='wx_tips_content'>获取您的昵称、头像、地区及性别</div>
                    <div class='wx_tips_information'>
                        <image class='wx_tips_image' src='/static/images/cabinet.svg'/>
                        <div class='wx_tips_text'>
                        <div class='wx_tips_text_title'>微信个人信息</div>
                        <div class='wx_tips_text_content'>尊敬的用户</div>
                        </div>
                    </div>
                    <div class="wx_tips_div">
                        <div class='wx_tips_cancel'>取消</div>
                        <div class='wx_tips_define'>允许</div>
                        <div class='wx_tips_circle'>允许</div>
                    </div>
                    </div>
                    <div class='wx_button'>
                    <button class='getuser' open-type="getUserInfo" @getuserinfo="wxLogin">我知道了 </button>
                    </div>
                </div>
            </div>
        </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
    data () {
        return {
            user:{},
            showModel:false
        }
    },
    onLoad (options) {
    },
    methods: {
        login() {
            wx.navigateTo({url:'../../login/main'});
        },
        order() {
            wx.navigateTo({url:'../order/main'});
        },
        balance(){
            wx.navigateTo({url:'../balance/main'});
        },
        deposit(){
            wx.navigateTo({url:'../deposit/main'});
        },
        share(res) {
            console.log('点击分享');
        },
        capital(){
            wx.navigateTo({url:'../capital/main'})
        },
        wxLogin (e) {
            var that = this
            if (e.mp.detail.userInfo == undefined) {
            that.globalData.hasLogin = false;
                wx.showToast({title: '微信登录失败', icon: 'none', duration: 2000})
                console.log('微信登录失败')
                wx.hideLoading()
                return;
            }
            that.showModel = false
            wx.login({
                success: function (res) {
                if (res.code) {
                    request.post({
                    url: 'auth/login_by_weixin',
                    data: {
                        'code': res.code,
                        'userInfo': e.mp.detail.userInfo
                    }
                    }).then(res => {
                    wx.hideLoading()
                    if (res.errno === 0) {
                        // 存储用户信息
                        wx.setStorageSync('userInfo', res.data.userInfo)
                        wx.setStorageSync('token', res.data.token)
                        wx.setStorageSync('user',res.data.user)
                        that.user = res.data.user
                        that.showModel = false
                    } else {
                        wx.showToast({title: res, icon: 'none', duration: 2000})
                        console.log(res)
                    }
                    }).catch((err) => {
                    wx.hideLoading()
                    wx.showToast({title: err, icon: 'none', duration: 2000})
                    console.log(err)
                    })
                } else {
                    wx.hideLoading()
                    wx.showToast({title: res, icon: 'none', duration: 2000})
                    console.log(res)
                }
                },
                fail: function (err) {
                wx.hideLoading()
                wx.showToast({title: err, icon: 'none', duration: 2000})
                console.log(err)
                }
            })
        }
    },
    onShow() {
        var that = this
        wx.getSetting({
            success: function (res) {
                if (!res.authSetting['scope.userInfo']) {
                    that.showModel = true
                }else{
                    that.showModel = false
                }
            }
        })
        this.user = wx.getStorageSync('user')
    }
}
</script>

<style>
.personage-sy{
    width: 100%;
    height: 100%;
    position:fixed;
    overflow-y: scroll;
    color: #2b2b2b;
    top: 0;
    left: 0;
    background-color:#f5f5f5;
}
.personage-sy::-webkit-scrollbar {
    display: none;
    -webkit-overflow-scrolling:touch;
}

.my-div{
    width: 100%;
    height: 140px;
    display: flex;
    /* margin-top: 10px; */
    /* border-radius: 5px; */
    /* -webkit-box-shadow: rgb(160, 158, 158) 0px 0px 10px;
    -moz-box-shadow: rgb(173, 172, 172) 0px 0px 10px;
    box-shadow: rgb(172, 172, 172) 0px 0px 10px; */
    background: #fff;
}
.my-img{
    margin-top: 20px;
    width: 40%;
    text-align: center;
}
.my-text{
    margin-left:10%; 
    width: 50%;
    padding-top:9%;
}
.headPortrait-img{
    width: 100px;
    height: 100px;
    border-radius: 50%;
}
.my-nickname{
    font-size: 20px;
    font-family:"微软雅黑";
}
.my-balance{
    margin-top: 5%;
    font-size: 13px;
}
.my-balance-text{
    color: #494545;
}
.my-balance-number{
    color: #494545;
}
.correlation-div-style{
    width: 100%;
    height: 50px;
    background: #fff;
    font-size: 14px;
    color: #3b3a3a;
    display: flex;
    align-items: center;
}
.correlation-div-style div{
    float: left;
    margin-left: 10px;
}
.div_text {
    height: 50px;
    line-height: 50px;
}
.personage-images{
    width: 22px;
    height: 22px;
    transform: translateY(4rpx);
}
.my-order{
    margin-top:10px; 
    border-bottom: 1px solid #DFDFDF; 
}
.my-wallet{
    border-bottom: 1px solid #DFDFDF;
}
.my-friend{
    margin-top:10px; 
    border-bottom: 1px solid #DFDFDF;
}
.we{
    margin-top:10px; 
}
/* 自定义弹窗 */
.ss{
  position: fixed;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
}
.wx_tips{
  /* margin-top: 20px; */
  /* width: 80%;
	margin-left: 10%;
  margin-bottom: 20px; */
  background: #fff;
  padding: 26rpx ;
  margin: 40rpx 80rpx;
  border-radius: 8rpx;

} 
.wx_tips_title{
  text-align: left;
  font-size: 26rpx;
}
.wx_tips_content{
  margin-top: 10px;
  text-align: left;
  font-size: 24rpx;
  /* font-weight: 700; */
}
.wx_tips_image{
  width: 100rpx;
  height: 100rpx;
  border-radius: 5px;
  float: left;
}
.wx_tips_text{
  float: left;
  margin-left: 15px;
  height: 100rpx;
}
.wx_tips_text_title{
  font-size: 26rpx;
  height: 50rpx;
  line-height: 50rpx;
  color: #2B2B2B;
}
.wx_tips_text_content{
   font-size: 24rpx;
  height: 50rpx;
  line-height: 50rpx;
  color: #666;
}
.wx_tips_information{
  padding: 10px 0px;
  border-top: 1rpx solid #dddddd;
  /* border-bottom: 1rpx solid #dddddd; */
  text-align: left;
  height: 110rpx;
  margin: 10px 0px;
}
.wx_tips_div{
  display: flex;
}
.wx_tips_cancel{
  /* float: left; */
  width: 40%;
  height: 60rpx;
  font-size: 20rpx;
  line-height: 60rpx;
  border-radius: 5px;
  background-color: #EDEDED;

}
.wx_tips_define{
  font-size: 20rpx;
  line-height: 60rpx;
  /* float: left; */
  width: 40%;
  height: 60rpx;
  border-radius: 5px;
  background-color: #07C160;
  color: white;
  margin-left: 24%;
}
.wx_tips_circle{
margin-top: -30rpx;
position: absolute;
z-index: 1;
border-radius: 50%;
width: 120rpx;
height: 120rpx;
border: 10rpx solid #fff;
text-align: center;
line-height: 120rpx;
left: 70%;
color: #F9F7FA;
background: #07C160;
box-shadow:0px 0px 20rpx rgb(201, 201, 201);
}
.container{
  position: fixed;
  background-color: rgba(0, 0, 0,0.3);
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
}
.content{
  color: #2B2B2B;
  background-color: #F9F7FA;
  width: 80%;
	border-radius: 10px;
	text-align: center;
	top: 18%;
  box-shadow: 2px 2px 5px rgba(0, 0, 0,0.3);
  z-index: 99;
}

.authTitle{
  margin-top: 20px;
  text-align: center;
  font-size: 36rpx;
  /* font-weight: 700; */
}
.tips{
  width:80%;
  margin-left: 10%;
  font-size: 28rpx;
  margin-top: 10px;
  color: #666;
  letter-spacing: 2rpx;
}
.wx_button{
  margin-bottom: 20px;
  margin-top: 71rpx;
}
.getuser{
  width: 70%;
  height: 70rpx;
  border-radius:25px;
  box-shadow: 0px;
  background-color:#07C160; 
  color: white;
  font-size: 24rpx;
  line-height: 70rpx;
}
.getuser:active{
  background-color:#06AA55; 
}
.getuser::after{
  border: 0;
  border-radius: 0px;
  box-shadow: 0px;
}
</style>
