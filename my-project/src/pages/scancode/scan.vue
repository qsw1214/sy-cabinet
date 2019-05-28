<template>
  <div class="all">
    <div
      class="emoticons"
      :style="{width: processImageWidth + 1 + 'rpx', height: processImageHeight + 1 + 'rpx'}">
      <img class="emoticonsImage"
        src="/static/images/u4.jpg"
        :style="{width: processImageWidth + 'rpx', height: processImageHeight + 'rpx'}">
    </div>
    <div class="prompt">扫描机柜上的二维码</div>
    <div class="scanQr" :style="{width: scanwidth + 'rpx', height: scanwidth + 'rpx'}">
      <button class="scanButt" type="button" @click="scanCode()"
        :style="{width: scanwidth - 5 + 'rpx', height: scanwidth - 5 + 'rpx'}">
        <img src="/static/images/u19.svg" class="scanImage"
            :style="{width: imgwidth + 'rpx', height: imgwidth + 'rpx'}">
        <div class="sCanText">扫一扫</div>
      </button>
    </div>
    <div class="foot">
      <div class="agreement"> 
        <div class="checkbox" @click="setting" 
          :style="{backgroundColor:checknext?'#33CC66':'#fff',border:checknext?'1px solid #33CC66':'1px solid #E4E4E4'}">
          <img src="/static/images/select.svg" class="img_checkbox">
        </div>
        下次跳过该页面，直接扫二维码
    </div>
    </div>
    
  </div>
</template>
<script>

export default {
  data() {
    return {
      mainId : '',
      useRecSize: '',
      mainInfo: {},
      processImageWidth:
        wx.getSystemInfoSync().windowWidth *
        (750 / wx.getSystemInfoSync().windowWidth) * 0.95,
      processImageHeight:
        wx.getSystemInfoSync().windowWidth *
        (750 / wx.getSystemInfoSync().windowWidth) * 0.68,
      imgwidth:
        wx.getSystemInfoSync().windowWidth *
        (750 / wx.getSystemInfoSync().windowWidth) * 0.15,
      scanwidth:
        wx.getSystemInfoSync().windowWidth *
        (750 / wx.getSystemInfoSync().windowWidth) * 0.32,
      left: 0,
      hidden: false,
      result: '',
      checknext: false
    }
  },
  methods: {
    // 设置是否跳过
    setting () {
      this.checknext = !this.checknext
      console.log(this.checknext)
      wx.setStorageSync('set', this.checknext)
    },
    scanCode () {
      // 微信二维码扫描
      wx.scanCode({
        success: res => {
          wx.showLoading({title: '加载中...'})
          this.mainId = res.result
          // 查询柜组信息
          this.$httpWX.get({
            url: 'maincab/subcablist',
            data: {
              'mainCabNum': res.result
            }
          }).then(maincode => {
            wx.hideLoading()
            if (maincode.data.retype === '1000') {
              wx.showToast({title: '请扫描正确的二维码', icon: 'none', duration: 2000})
            } else if (maincode.data.retype === '1001') {
              wx.showToast({title: '柜组已锁定', icon: 'none', duration: 2000})
            } else if (maincode.data.retype === '1002') {
              wx.showToast({title: '柜组故障', icon: 'none', duration: 2000})
            } else if (maincode.data.retype === '1003') {
              wx.showToast({title: '柜组暂停使用', icon: 'none', duration: 2000})
            } else if (maincode.data.retype === '1004') {
              this.mainInfo = maincode.data.items
              // 查询用户使用记录(单个柜组)
              this.$httpWX.get({
                url:'usarec/queryRecordCount',
                data: {
                  'userId': wx.getStorageSync('user').id
                }
              }).then(res => {
                this.useRecSize = res.data.total
                if(this.useRecSize < 5) {
                  wx.redirectTo({
                    url: '../select/main?mainCabId=' + this.mainInfo.id + '&region=' + this.mainInfo.region
                  })
                }else{
                  setTimeout(function() {
                    wx.navigateBack()
                    },2001)
                  wx.showToast({title: '存储物品次数达上限(5次),请取包后重试', icon: 'none', duration: 2000})
                }
              })
            } else {
              wx.showToast({title: '服务器错误，请联系系统管理员', icon: 'loading', duration: 2000})
            }
          })
        },
        fail: res => {
          wx.showToast({title: "用户取消", icon: 'none', duration: 2000})
          console.log(res)
        }
      })
    },
    
  },
  onLoad (options) {
    if(wx.getStorageSync('set')){
      this.checknext = wx.getStorageSync('set')
      this.scanCode()
    }
  },
  onShow () {
  }
}
</script>

<style>
.all {
  width: 100%;
  height: 100%;
  position: absolute;
  background: #f5f5f5;
}
/* 图示div */
.emoticons {
  margin: 2% auto 0;
}
/* 提示div */
.prompt {
  width: 80%;
  height: 5%;
  margin: 5% auto 0;
  font-size: 100%;
  text-align: center;
  color: #5E5D5D;
 
}
/* 扫描div*/
.scanQr {
  margin: 28% auto 0;
  
}
/* 扫描按钮 */
.scanButt {
  font-size: 86%;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  color: #fff;
  background-color: #00cc66;
  -moz-box-shadow:0px 0px 20px rgb(2, 148, 75); 
  -webkit-box-shadow:0px 0px 20px rgb(2, 148, 75); 
  box-shadow:0px 0px 20px rgb(2, 148, 75);
} 
/* 扫一扫图片 */
.scanImage {
  margin: auto auto 0;
  transform: translateY(10rpx);
}
.sCanText{
  margin: 0 auto auto;
}
/* 跳过 */
.jumpOver {
  width: 80%;
  height: 4%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 80%;
  text-align: center;
  position: fixed;
  bottom: 1%;
  color: #999;
  font-size: 24rpx;
}
.foot{
  position: fixed;
  bottom: 20rpx;
  width: 100%;
}
.agreement{
  text-align: center;
  color:#5E5D5D;
  font-size: 25rpx;
  vertical-align:middle;
  display: flex;
  justify-content:center;
  align-items:Center;
  margin-top: 5%;
  left: 0;
}
.checkbox{
  width: 13px;
  height: 13px;
  position: inherit;
  margin-right: 5px;
  display: flex;
  justify-content:center;
  align-items:Center;
}
.img_checkbox{
   width: 15px;
   height: 15px;
   }
</style>
