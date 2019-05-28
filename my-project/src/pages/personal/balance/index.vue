<template>
    <div class="sy-my-balance">
        <div class="money-img" >
            <img class="money-image" src="/static/images/bank.svg" :style="{width: imgwidth + 10 + 'rpx', height: imgwidth + 10 +  'rpx'}"/>
        </div>
        <div class="div-balance">
            <div class="balance-p">
                <p class="balance-p-text">我的余额</p>
                <p class="balance-p-number">￥{{user.balance}}</p>
            </div>
        </div>
        <div>
            <button class="btn-recharge" @click="jumpWithdraw">提现</button>
        </div>
        <!-- <div>
            <button class="btn-withdrawDeposit" @click="jumpWithdraw">提现</button>
        </div> -->
        <div class="FAQ-text">
            <a>常见问题</a>
        </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.24),
      mobile: '',
      list: [],
      user: {
        id: '',
        username: '',
        password: '',
        gender: '',
        birthday: '',
        lastLoginIp: '',
        userLevel: '',
        nickname: '',
        mobile: '',
        status: '',
        balance: ''
      },
      userPhone: ''
    }
  },
  methods: {
    // 提现
    jumpWithdraw () {
      this.$httpWX.get({
        url:'wxpay/refund',
        data:{
          userId: this.user.id,
          orderStatus: 1,
          refundFee: this.user.balance
        }
      }).then(res =>{
        console.log(res)
        if(res.errno === 0){
        wx.showLoading({title: '提现中'})
        }else{
          console.log('提现失败')
        }
      })
    },
    // 修改余额
    updateBalance() {
      console.log('更新缓存')
      that.$httpWX.get({
        url: 'usarec/getuser',
        data: {
          userId: wx.getStorageSync('user').id,
        }
      }).then(res => {
        wx.hideLoading()
        wx.setStorageSync('user',res.data.user)
        that.user = wx.getStorageSync('user')
      })
    }
  },
  onShow () {
    wx.showLoading({title: '加载中...'})
    this.$httpWX.get({
      url: 'usarec/getuser',
      data: {
        userId: wx.getStorageSync('user').id,
      }
    }).then(res => {
      wx.hideLoading()
      wx.setStorageSync('user',res.data.user)
      this.user = wx.getStorageSync('user')
    })
  }
}
</script>

<style>
    .sy-my-balance{
        width: 100%;
        height: 100%;
        position: fixed;
        padding-bottom: 20%;
        top: 0;
        left: 0;
        background-color:#f5f5f5;
    }
    .money-img{
       margin: 0 auto;
       text-align: center;
    }
    .money-image{
        padding-top: 35%;
    }
    .div-balance{
        width: 60%;
        height: 30%;
        margin: 0 auto;
        text-align: center;
        padding-top:5%; 
    }
    .balance-p-text{
        font-size: 15px;
        color: rgb(219, 139, 34);
       
    }
    .balance-p-number{
        font-size: 40px;
        color: rgb(4, 7, 4);
    }
    /* 充值 */
    .btn-recharge{
        width:40%;
        height:100rpx;
        background:-webkit-linear-gradient(left,#47C489,#1db311);
        color: aliceblue;
        border-radius: 5px;
        margin: 0 auto;
        line-height:100rpx;
        text-align: center;
        font-size: 32rpx;
    }
    .btn-withdrawDeposit{
        width:40%;
        height: 96rpx;
        margin: 5% auto;
        line-height:96rpx;
        text-align: center;
        border-radius: 5px;
        color: #1DB311;
        border:1px solid #1DB311;
        font-size: 32rpx;
    }
    .FAQ-text{
        width: 13%;
        height: 5%;
        color: rgb(34, 98, 216);
        margin: 23% auto;
        font-size: 12px;

    }
</style>
