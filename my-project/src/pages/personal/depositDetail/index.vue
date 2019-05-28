<template>
    <div class="sy-my-deposit">
        <div class="money-img" >
            <img class="money-image" src="/static/images/deposit_big.svg" :style="{width: imgwidth + 10 + 'rpx', height: imgwidth + 10 +  'rpx'}"/>
        </div>
        <div class="div-deposit">
            <div class="deposit-p">
                <p class="deposit-p-text">押金</p>
                <p class="deposit-p-number">￥{{user.userDeposit}}</p>
            </div>
        </div>
        <div>
            <button class="btn-recharge" @click="jumpRecharge">充值</button>
        </div>
        <div>
            <button class="btn-withdrawDeposit" @click="jumpWithdraw">提现</button>
        </div>
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
        balance: '',
        userDeposit: ''
      },
      userPhone: '',
      total: '',
    }
  },
  methods: {
    // 充值
    jumpRecharge () {
      console.log('跳转充值' + this.user.mobile)
    },
    // 提现
    jumpWithdraw () {
      // 查询用户订单
      this.$httpWX.get({
        url: 'usarec/usageList',
        data: {
          'userId': wx.getStorageSync('user').id,
          'status': 0
        }
      }).then(response => {
        console.log(response)
        this.total = response.data.total
        if(this.total <= 0){
          console.log('跳转提现' + this.user.mobile)
          wx.navigateTo({
            url: '../withdrawal/main?userPhone=' + this.user.mobile
          })
        }else{
          wx.showToast({title: '有未结算订单，不可提现', icon: 'none', duration: 5000})
        }
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
    .sy-my-deposit{
        width: 100%;
        height: 100%;
        position: fixed;
        padding-bottom: 20%;
        top: 0;
        left: 0;
        background-color:rgba(242, 242, 242, 1);
    }
    .money-img{
       margin: 0 auto;
       text-align: center;
    }
    .money-image{
        padding-top: 35%;
    }
    .div-deposit{
        width: 60%;
        height: 30%;
        margin: 0 auto;
        text-align: center;
        padding-top:5%; 
    }
    .deposit-p-text{
        font-size: 15px;
        color: rgb(219, 139, 34);
       
    }
    .deposit-p-number{
        font-size: 40px;
        color: rgb(4, 7, 4);
    }
    .btn-recharge{
        width:40%;
        height: 6%;
        background:#1DB311;
        color: aliceblue;
        border-radius: 5px;
        margin: 0 auto;
        line-height:36px;
        text-align: center;
    }
    .btn-withdrawDeposit{
        width:40%;
        height: 6%;
        margin: 5% auto;
        line-height: 36px;
        text-align: center;
        border-radius: 5px;
        color: #1DB311;
        border:1px solid #1DB311;
    }
    .FAQ-text{
        width: 13%;
        height: 5%;
        color: rgb(34, 98, 216);
        margin: 23% auto;
        font-size: 12px;

    }
</style>
