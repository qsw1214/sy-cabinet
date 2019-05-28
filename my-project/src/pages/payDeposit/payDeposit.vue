<template>
  <div id="app">
    <div class="showDeposit">
      <div class="depositName">请预存押金</div>
      <div class="refundable">(可退还)</div>
      <!-- <div class="image"> -->
        <!-- <img class="ima" src="/static/images/cash-pledge.svg" :style="{width: imgwid + 'rpx', height: imgwid + 'rpx'}"> -->
        <div class="deposit">{{deposit}}<span>元</span></div>
      <!-- </div> -->
      <div class="hint">
        <div class="hint-text">
          <p>1.预存押金用于支付寄存费用</p>
          <p>2.寄存费用每小时{{stanInfo.stanMoney}}元</p>
          <p>3.预存押金将在寄存完毕后返还</p>
        </div>
      </div>
      <div class="payWay">
        <div class="payWay-main">
          <radio-group class="payRadio" @change="radioChange">
            <div>支付方式 <span class="userBal">(余额:&nbsp;{{balance}}元)</span></div>
            <div class="balPay">
              <div class="pay-text">
                <img class="pay-balanceimg" src="/static/images/balanceimg.svg" />
                <p>余额支付</p>
              </div>
              <radio :value='balPay' :checked='balCheck' :disabled='disabled' ></radio>
            </div>
            <div class="wxPay">
              <div class="pay-text">
                <img class="pay-wximg" src="/static/images/wximg.svg" />
                <p>微信支付</p>
              </div>
              <radio  :value='wxPay' :checked='wxCheck'></radio>
            </div>
          </radio-group>
        </div>  
      </div>
    </div>
    <!-- <div class="title">
      <span>title:押金优先从账户余额支付</span>
    </div> -->
    <div class="payButt" type="success" @click="payDeposit">
      <div class="payButt-main">确认支付</div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      imgwid: ( wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.15 ),
      stanInfo: {
        id: '',
        stanName: '',
        stanDatils:'',
        stanMoney: '',
        stanCapping: '',
        stanMianId: '',
        stanFree:'',
        stanBeyond: '',
        stanDeposit: ''
      },
      subCabInfo: {},
      mainInfo: {},
      bool: '',
      changeBal: '',
      balance: '',
      deposit: '',
      balCheck: false,
      wxCheck: false,
      disabled: false,
      balPayWay: false,
      wxPay: true,
      payWays: '',
      user: {},
      balPay:'',        // 余额支付金额
      isWxPay: '',      // 是否需要微信支付
      refund: '',       // 退款金额
      wxpayPrice : '',  // 微信支付金额
      recordId: '',     // 订单Id
      orderStatus: '',  // 订单状态
      orderId: '',      // 微信支付订单
    }
  },
  methods: {
    // 单选改变
    radioChange(e) {
      this.payWays = e.mp.detail.value;
    },
    // 支付押金
    payDeposit() {
      if(this.payWays){
        this.wxpayPrice = this.deposit;
        // 微信扣费
        this.wxPayMoney()
      }else{
        this.bool= false;
        if(this.balance < this.deposit){
          this.balPay = this.balance;
          this.isWxPay = true
        }else{
          this.balPay = this.deposit;
          this.isWxPay = false
        }
        // 余额扣费
        this.updateBalance()
      }
    },
    // 余额扣费
    updateBalance(){
      this.$httpWX.get({
        url:'user/updateBalance',
        data: {
          'userId': wx.getStorageSync('user').id,
          'balance': this.balPay,
          'bool': this.bool
        }
      }).then(res => {
        if(res.errno === 0) {
          this.$httpWX.get({
            url: 'usarec/getuser',
            data: {
              userId: wx.getStorageSync('user').id,
            }
          }).then(res => {
            wx.hideLoading();
            wx.setStorageSync('user',res.data.user)
          });
          if(this.isWxPay){
            this.wxpayPrice = this.deposit - this.balance;
            this.wxPayMoney()
          }else{
            // 添加用户储物柜押金
            this.addDeposit()
          }
        }else{
          wx.showTost({title: '网络繁忙,请稍后再试！', icon: 'none', duration: 2000});
          wx.navigateBack()
        }
      })
    },
    // 微信支付
    wxPayMoney() {
      this.$httpWX.get({
        url: 'wxpay/pay',
        data: {
          'userId': wx.getStorageSync('user').id,
          'recordId': this.recordId,
          'wxpric': this.wxpayPrice,
          'isPay': 0
        }
      }).then(res => {
        if (res.errno === 0) {
          const payParam = res.data.result;
          this.orderId = res.data.orderId;
          const that = this;
          wx.requestPayment({
            'timeStamp': payParam.timeStamp,
            'nonceStr': payParam.nonceStr,
            'package': payParam.packageValue,
            'signType': 'MD5',
            'paySign': payParam.paySign,
            'success': function(res) {
              console.log('支付成功:'); 
              console.log(res);
              that.addDeposit()
              that.orderStatus = 1
              that.updateWxOrder()
            },
            'fail': function(res) {
              console.log('支付失败');
              console.log(res);
              that.orderStatus = 2
              that.updateWxOrder()
            },
            'complete': function(res) {
              console.log('支付完成');
              console.log(res);
            }
          })
        }
      })
    },
    //添加押金信息
    addDeposit() {
      this.$httpWX.post({
        url:'deposit/add',
        data:{
          depoUserId: wx.getStorageSync('user').id,
          depoSubId: this.subCabInfo.id,
          depoMoney: this.stanInfo.stanDeposit,
          depoRecordId: this.recordId,
          depoStatus: 1,
        }
      }).then(res => {
        if (res.errno === 0) {
          const depoId = res.data.depositId;
          const subData = JSON.stringify(this.subCabInfo);
          const stanData = JSON.stringify(this.stanInfo);
          const url = '../confirm/main?stanData=' + stanData
            + '&mainRegion=' + this.region + '&subData=' + subData 
            + '&recordId=' + this.recordId + '&depoId=' + depoId;
          wx.redirectTo({url})
        }
      })
    },
    // 修改微信订单
    updateWxOrder() {
      this.$httpWX.post({
        url: 'wxpay/updateOrder',
        data: {
          orderId : this.orderId,
          orderStatus :this.orderStatus
        }
      }).then(res => {
        console.log(res)
        console.log('修改微信支付订单状态')
      })
    }
  },
  onLoad(options) {
    this.balance = wx.getStorageSync('user').balance;
    this.subCabInfo = JSON.parse(options.subInfo);   // 子柜信息
    this.stanInfo = JSON.parse(options.stanInfo);    // 规格信息
    this.region = options.mainRegion;
    this.deposit = this.stanInfo.stanDeposit;        // 押金
    this.recordId = options.recordId;                // 订单Id
    // 初始化支付方式(余额为0，微信支付)
    if (this.balance === 0) {
      this.disabled = true;          // 余额禁用
      this.wxCheck = true;           // 默认微信
      this.payWays = this.wxPay
    } else {// 余额支付
      this.balCheck = true;          // 默认余额
      this.payWays = this.balPayWay
    }
  }
}
</script>

<style>
#app{
  width:100%;
  height: 100%;
  position: fixed;
  background: #f5f5f5;
}
.showDeposit{
  flex-wrap: wrap;
  width: 90%;
  margin: 50rpx auto 100rpx;
  border-radius: 10rpx;
  background: white;
  padding-bottom: 3%;
}
.depositName{
  padding-top: 5%;
  font-size: 46rpx; 
  letter-spacing: 2rpx;
  font-family: 'Microsoft Yahei';
  text-align: center;
  /* border: 1px solid grey; */
}
.refundable{
  font-size: 30rpx;
  text-align: center;
  color: #888;
  margin: 14rpx 0 40rpx;
}
.deposit{
  text-align: center;
  font-size: 120rpx;
  color: #00cc66;
  height: 120rpx;
  line-height: 120rpx;
  padding-bottom: 40rpx;
}
.deposit span{
  font-size: 30rpx;
}
.hint{
  width: 88%;
  margin: auto;
  border-top: 2rpx dashed #CFCFCF;
}
.hint-text{
  color: #666;
  font-size: 26rpx;
  padding-left: 40rpx;
}
.hint-text p{
  padding-top: 24rpx;
  letter-spacing: 2rpx; 
}
.userBal{
  color: #888;
  letter-spacing: 2rpx;
  font-size: 28rpx;
}
.userBal-main{
  padding: 5% 3%;
}
/* 支付方式 */
.payWay{
  background: white;
  font-size: 34rpx;
  letter-spacing: 2rpx;
}
.payWay-main{
  padding: 6% 6%;
}
.balPay,.wxPay{
  display: flex;
  font-size:34rpx;
  justify-content: space-between;
  color: #555;
}
.balPay{
  padding: 30rpx 0;
}
.pay-text{
  padding-left: 10rpx;
  display: flex;
}
.pay-balanceimg{
  width: 50rpx;
  height: 40rpx;
  margin-right: 10rpx;
  transform: translateY(4rpx);
}
.pay-wximg{
  width: 50rpx;
  height: 50rpx;
  margin-right: 10rpx;
}
.title{
  width: 85%;
  height: 5%;
  margin: 2% auto;
  line-height: 250%;
  font-size: 25rpx;
  letter-spacing: 1rpx;
  border: 1px dashed #DFDFDF;
  border-radius: 2px;
  background: white;
}
.payButt{
  width: 90%;
  margin: auto;
  font-size: 32rpx;
  text-align: center;
  background:#00cc66;
  border-radius: 10rpx;
  color: #fff;
  line-height: 100rpx;
}
.payButt-main{
  color: #fff;
}
radio{
  transform: scale(0.8);
}
</style>

