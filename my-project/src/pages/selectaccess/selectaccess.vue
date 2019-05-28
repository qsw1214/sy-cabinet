<template>
  <div class="all">
    <div class="bottomLayer">
      <div class="propaganda">
        <div class="fixLogo">
          <img class="logoImage" src="/static/images/u39.png">
        </div>
        <div class="font-name">
            <span class="com-name">宋&nbsp;&nbsp;&nbsp;云</span>
            <span class="pro-name">智&nbsp;能&nbsp;寄&nbsp;存&nbsp;柜</span>
        </div>
        <div class="dynamicTitle">&nbsp;&nbsp;第一天试营业存取免费</div>
      </div>
    </div>
    <div class="operating">
      <div class="saveFrame" :style="{width: imgwidth + 20 + 'rpx', height: imgwidth + 20 +  'rpx'}" @click="save">
        <div class="saveButt" :style="{width: imgwidth + 'rpx', height: imgwidth + 'rpx'}">存</div>
      </div>
      <div class="takeFrame" :style="{width: imgwidth + 20 + 'rpx', height: imgwidth + 20 + 'rpx'}" @click="take">
        <div class="takeButt" :style="{width: imgwidth + 'rpx', height: imgwidth + 'rpx'}">取</div>
      </div>
    </div>
    <div class="title">
      <img class="titleImage" src="/static/images/u46.svg">
      <div class="title-cla"><span class="title-font">&nbsp;请点击选择存取</span></div>
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
          <button class='getuser' open-type="getUserInfo" @getuserinfo="userLogin">我知道了</button>
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
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.24),
      showModel: false,       // 弹窗状态
      userInfo: {},
    }
  },
  component: {
    
  },
  methods: {
    save () {
      var that = this
      wx.getSetting({
        success: function (res) {
          console.log(res)
          if (!res.authSetting['scope.userInfo']) {
            that.showModel = true
          }else{
            wx.navigateTo({ url:'/pages/scancode/main' })
          }
        }
      })
    },
    take () {
      var that = this
      wx.getSetting({
        success: function (res) {
          console.log(res)
          if (!res.authSetting['scope.userInfo']) {
            that.showModel = true
          }else{
            wx.navigateTo({ url:'/pages/bagTaking/main' })
          }
        }
      }) 
    },
    userLogin (e) {
      var that = this
      if (e.mp.detail.userInfo == undefined) {
        that.globalData.hasLogin = false;
        wx.showToast({title: '微信登录失败', icon: 'none', duration: 2000})
        console.log('微信登录失败')
        wx.hideLoading()
        return;
      }
      this.showModel = false
      wx.login({
        success: function (res) {
          console.log(res)
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
  onLoad(){
  },
  onShow: function () {
  },
}
</script>

<style>
.all {
  width: 100%;
  height: 100%;
  position: absolute;
  display: flex;
  flex-direction:column;
  background-color: #f5f5f5;
}
/* 底层背景 */
.bottomLayer {
  width: 704rpx;
  height: 408rpx;
  align-items:center;
  justify-content:center;
  margin: auto;
  background-color: #ffffff;
  border-radius: 6rpx;
}
.bottomLayer-main{
  padding: 18rpx;
}
/* 宣传背景 */
  .propaganda {
    display: flex; /*弹性布局*/
    flex-wrap: wrap; /*换行，并且第一行在容器最上方*/
    background-color: #FFCA26;
    /* border-radius: 6rpx 6rpx 0 0; */
  }
/* 固定logo */
    .fixLogo {
      width: 40%;
      display:flex;
      align-items:center;
      padding: 18% 0 42rpx 30rpx;
    }
    /* logo图片 */
    .logoImage {
      width: 75px;
      height: 65px;
    }
    /* 固定文字 */
    /* .fixTitle {
      width: 58%;
      text-align: center;
    } */
    /* 文字 */
    .font-name {
      width: 50%;
      text-align: center;
      padding-top: 18%;
      color: #333;
    }
    /* 公司名称 */
    .com-name {
      font-size: 30px;
      font-family: "Gill Sans", "Gill Sans MT", Calibri, "Trebuchet MS", sans-serif;
      letter-spacing: 4px;
      
    }
    /* 项目名称 */
    .pro-name {
      font-size: 20px;
      font-weight: bold;
      font-family: Georgia, 'Times New Roman', Times, serif;
      letter-spacing: 4px;
    }

    /* 动态提示 */
    .dynamicTitle {
      width: 100%;
      font-size: 30rpx;
      font-family: "Franklin Gothic Medium", "Arial Narrow", Arial, sans-serif;
      color: #ffffff;
      background-color: #333333;
      display: flex;
      align-items: center;
      padding: 20rpx 0;
      /* border-radius: 0 0 6rpx 6rpx; */
    }
    .dynamicTitle span{
      letter-spacing: 2rpx;
      padding-left: 20rpx;
    }
/* 操作（存、取） */
.operating {
  width: 100%;
  margin:  auto ;
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  font-size: 100rpx;
}
/* 存取边框 */
  .saveFrame ,.takeFrame{
    border-radius: 50%;
    text-align: center;
    display: flex; 
    align-items: center;
    justify-content:center;
    background-color: #ffffff;
  }

  /* 存取按钮 */
  .saveButt,.takeButt {
    border-radius: 50%;
    display: flex; 
    align-items: center;
    justify-content:center;
    color: #ffffff;
  }
  .saveButt{
  background-color: #FFCA26;
  }
  .takeButt {
    background-color: #00cc66;
  }
/* 提示(选择存取提示) */
.title {
  margin:  auto;
  height: 34px;
  display: flex;
  flex-wrap: wrap;
}
  /* 提示图片 */
  .titleImage {
    width: 50rpx;
    height: 50rpx;
    transform: translateY(4rpx);
  }
  /* 提示文字 */
  .title-cla{
    line-height: 34px;
    color: #5E5D5D;
    letter-spacing: 1px;
    font-size: 27rpx;
  }
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