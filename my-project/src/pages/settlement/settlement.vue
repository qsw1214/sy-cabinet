<template>
  <div @click="clickHandle" id="app">
    <div id="content">
      <div class="con_a">{{mainCabInfo.region}}</div>
      <div class="con_b">营业时间：&nbsp;00：00&nbsp;-&nbsp;24：00</div>
    </div>
    <div id="center">
      <div class="center-one"> 
        <div class="center-one-main">
          <div class="sy-boxa">{{stanInfo.stanName}}收费规则：{{price}}元/小时，1天{{price * cappingTime}}元封顶</div>
          <div class="sy-boxb" @click="backpage">
            <div class="sy-box-title">当前柜子</div>
            <div class="sy-box-text">
              <span>{{subCabInfo.subCabNum}}</span><div class="text">号</div><i-icon type="enter" />
            </div>
          </div>
          <div class="sy-boxc">
            <div class="sy-arrears">
              <div class="sy-title">已付金额</div>
              <div class="sy-text">¥{{usageInfo.preAmount}}</div>
            </div>
            <div class="sy-arrears">
              <div class="sy-title">预存时长</div>
              <div class="sy-text">{{usageInfo.preTime}}小时</div>
            </div>
          </div>
          <div class="sy-boxe">
            <div class="sy-arrears">
              <div class="sy-title">消费金额</div>
              <div class="sy-text">¥{{usageInfo.actualAmount}}</div>
            </div>
            <div class="sy-arrears">
              <div class="sy-title">使用时长</div>
              <div class="sy-text">{{usageInfo.actualTime}}小时</div>
            </div>
            <div class="sy-arrears">
              <div class="sy-arrears-title">欠费</div>
              <div class="sy-arrears-text">¥{{arrears}}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="center-tow">
        <div class="sy-boxd">
          <div class="sy-prepay">支付金额<span class="money">{{payment}}</span>元</div>
          <div class="sy-content">
            <div class="sy-titleone">余额支付<span class="sy-balance">(余额{{balance}})</span></div>
            <div class="sy-textone">¥{{balapric}}</div>
          </div>
          <div class="sy-content">
            <div class="sy-titleone">微信支付</div>
            <div class="sy-textone">¥{{wxpric}}</div>
          </div>
          <div class="payWay">
            <radio-group class="payRadio" @change="radioChange">
              <div class="balPay">
                <div class="pay-text">
                  <img class="pay-balanceimg" src="/static/images/balanceimg.svg">
                  <p>余额支付</p>
                </div>
                <radio :value='balPay' :checked='balCheck' :disabled='disabled' ></radio>
              </div>
              <div class="wxPay">
                <div class="pay-text">
                  <img class="pay-wximg" src="/static/images/wximg.svg">
                  <p>微信支付</p>
                </div>
                <radio  :value='wxPay' :checked='wxCheck'></radio>
              </div>
            </radio-group>
          </div>
        </div>
      </div>
    </div>
    <div id="foot">
      <div class="button"><i-button type="success" @click="takeBag" >取包走人</i-button></div>
      <div class="agreement">
        <div class="checkbox" @click="checkdiv" :style="{backgroundColor:checknext?'#33CC66':'#fff',border:checknext?'1px solid #33CC66':'1px solid #E4E4E4'}">
          <img src="/static/images/select.svg" class="img_checkbox">
        </div>点击确认下单即同意《<a href="/pages/confirm_detail/main">用户协议</a>》</div>
    </div>
    <div class="timeselebody" v-show="timesele">
      <div class="timeseletop"  @click="cloesTime()">
      </div>
      <div class="timesele">
        <div @click="clickTime(item)" v-for="(item, index) in datatime" :key="index" >{{item}}</div>
      </div>
    </div>
    
  </div>
</template>

<script>
import card from '@/components/card'
export default {
  data() {
      return {
        stanInfo: {   // 规格信息
          id: '',
          stanName: '',
          stanDatils:'',
          stanMoney: '',
          stanCapping: '',
          stanMianId: '',
          stanFree:'',
          stanBeyond: ''
        },
        recordInfo:{},    // 订单信息
        depoInfo:[],      // 押金信息
        deposit:{},       // 
        balance: '',      // 用户余额
        mainCabInfo: {},  // 柜组信息
        subCabInfo: {},   // 子柜信息
        usageInfo: {},    // 订单信息
        balapric: '',
        wxpric: 0,
        time: 3,
        price :'',
        timesele: false,
        datatime:[],
        payment: 0,
        cappingTime: '',  // 封顶时间
        arrears: '',      // 欠费金额
        renewalTime: '',  // 续费时间
        balCheck: false,
        wxCheck: false,
        disabled: false,
        balPayWay: false, // 微信支付
        wxPay: true,      // 余额支付
        balPay: '',       // 余额支付金额
        payWays: '',      // 支付方式(true 微信；false 余额)
        isWxPay: '',      // 是否需要微信支付
        refund: '',       // 退款金额
        bool: false,      // (true:退款;false:扣费)
        way: '',          // (true:续费;false:取包)
        depoId: '',       // 押金Id
        depoSize: '',     // 押金信息数量
        wxOrderId: '',    //微信订单Id
        orderStatus:'',   //微信订单状态
      }
    },
  methods: {
    // 单选改变
    radioChange(e) {
      this.payWays = e.mp.detail.value;
      if(this.payWays === 'true'){
        console.log("微信支付");
        this.wxpric = this.payment;
        this.balapric = 0
        // 余额支付
      }else{
        console.log("余额支付");
        if (this.balance > this.payment) {
          this.balapric = this.balance - this.payment;
          this.wxpric = 0
        } else {
          this.balapric = this.balance;
          this.wxpric = this.payment - this.balance
        }
      }
    },
    // 取包走人
    takeBag() {
      // 微信支付
      if(this.payWays === 'true'){
        console.log('微信全额');
        this.wxpayPrice()
        // 余额支付
      }else {
        // 余额全额支付
        if(this.balance >= this.payment){
          console.log('余额全额');
          this.changeBal = this.payment;
          this.payWays = false;
          this.bool = false;
          this.payBalance()
          // 余额/微信支付
        }else{
          console.log('余额/微信');
          this.changeBal = this.balance;
          this.payWays = true;
          this.bool = false;
          this.payBalance()
        }
      }
    },
    // 余额支付
    payBalance() {
      // 修改余额
      this.$httpWX.get({
        url: 'user/updateBalance',
        data: {
          'userId': wx.getStorageSync('user').id,
          'balance': this.changeBal,
          'bool': this.bool
        }
      }).then(res => {
        if(res.errno === 0){
          // 更新缓存
          this.$httpWX.get({
            url: 'usarec/getuser',
            data: {
              userId: wx.getStorageSync('user').id,
            }
          }).then(res => {
            wx.hideLoading();
            wx.setStorageSync('user',res.data.user);
            this.user = wx.getStorageSync('user')
          });
          if(this.payWays){
            this.wxpayPrice()
          }else{
            this.addCapRec()
          }
        }else{
          wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000});
          wx.hideLoading()
        }
      })
    },
    // 微信支付
    wxpayPrice (){
      console.log('微信支付,用户Id' + wx.getStorageSync('user').id);
      this.$httpWX.get({
        url: 'wxpay/pay',
        data: {
          'userId': wx.getStorageSync('user').id,
          'recordId': this.id,
          'wxpric': this.wxpric,
          'isPay': 1
        }
      }).then(res => {
        console.log(res);
        if (res.errno === 0) {
          const payParam = res.data.result;
          this.wxOrderId = res.data.orderId;
          var that = this;
          console.log("支付过程开始");
          wx.requestPayment({
            'timeStamp': payParam.timeStamp,
            'nonceStr': payParam.nonceStr,
            'package': payParam.packageValue,
            'signType': 'MD5',
            'paySign': payParam.paySign,
            'success': function(res) {
              console.log(res);
              console.log('支付成功')
              //添加订单信息
              that.addCapRec()
              that.orderStatus = 1
              //修改订单信息
              that.updateWxOrder()
            },
            'fail': function(res) {
              console.log(res);
              that.orderStatus = 2
              //修改订单信息
              that.updateWxOrder()
            },
            'complete': function(res) {
              console.log(res)
            }
          })
        }
      })
    },
    // 添加交易记录
    addCapRec() {
      this.$httpWX.post({
        url:'capital/add',
        data:{
          capUserid: wx.getStorageSync('user').id,
          capMoney: this.arrears,
          capDealid: this.usageInfo.id,
          capType: 3,
        }
      }).then(res => {
        if (res.errno === 0) {
          console.log(this.deposit)
          if(this.depoSize === 0){
            this.updaterecord()
          }else{
            this.deposit = this.depoInfo[0]
            this.updateDeposit()
          }
        }
      })
    },
    // 修改押金
    updateDeposit() {
      console.log('结算取包--修改押金信息');
      console.log(this.deposit);
      this.$httpWX.post({
        url:'deposit/update',
        data:{
          depoId: this.deposit.depoId,
          depoUserId: wx.getStorageSync('user').id,
          depoMoney: this.deposit.depoMoney,
          depoStatus: 2,
        }
      }).then(res =>{
        if(res.errno === 0 ){
          console.log('修改押金成功!');
          this.updaterecord()
        }else{
          wx.showToast({title: '网络繁忙！', icon: 'none', duration: 2000})
        }
      })
    },
    // 修改订单信息
    updaterecord () {
      console.log('修改订单-id:' + this.usageInfo.id);
      this.$httpWX.post({
        url:'usarec/updateUsaRecord',
        data:{
          id: this.usageInfo.id,
          startTime: this.usageInfo.startTime,
          actualAmount: this.usageInfo.actualAmount,
          actualTime: this.usageInfo.actualTime,
          subCabId : this.usageInfo.subCabId,
          subStatus: 0,
          status : 1,
        }
      }).then(res => {
        if(res.errno === 0){
           wx.redirectTo({
            url:'../locker/main?recordId=' + this.usageInfo.id
          })
        }else{
          this.refund = this.payment;
          this.refundPay()
        }
      })
    },
    listDepo() {
      console.log('查询押金信息');
      this.$httpWX.post({
        url: 'deposit/list',
        data:{
          depoUserId: wx.getStorageSync('user').id,
          depoSubId: this.recordInfo.subCabId,
          depoStatus: 1,
        }
      }).then(res => {
        this.depoInfo = res.data.items
        this.depoSize = res.data.total
      })
    },
    // 修改微信订单
    updateWxOrder() {
      this.$httpWX.post({
        url: 'wxpay/updateOrder',
        data: {
          orderId : this.wxOrderId,
          orderStatus :this.orderStatus
        }
      }).then(res => {
        console.log(res)
        console.log('修改微信支付订单状态')
      })
    }
  },
  onLoad(options) {
    this.recordInfo = JSON.parse(options.recordInfo);
    this.id = this.recordInfo.id;
    this.balance = wx.getStorageSync('user').balance;
    // 查询规格信息、子柜信息、柜组信息
    this.$httpWX.get({
      url:'subcabinet/queryInfo',
      data: {
        'recordId': this.id
      }
    }).then(res => {
      this.stanInfo = res.data.stan;
      this.mainCabInfo = res.data.main;
      this.subCabInfo = res.data.sub;
      this.usageInfo = res.data.usa;
      this.cappingTime = this.usageInfo.usaCapp;
      this.time = this.usageInfo.preTime;
      this.price = this.usageInfo.usaMoney;
      // 欠费
      this.arrears = this.usageInfo.actualAmount - this.usageInfo.preAmount;
      // 超出时间（续费时间）
      this.renewalTime = this.usageInfo.actualTime-this.usageInfo.preTime;
      // 计算封顶时间
      if(this.renewalTime > this.usageInfo.usaCapp - this.usageInfo.preTime){
        this.renewalTime = this.usageInfo.usaCapp - this.usageInfo.preTime
      }
      this.time = this.renewalTime;
      this.payment = this.price * this.renewalTime;
      if (this.balance >= (this.stanInfo.stanMoney * this.usageInfo.preTime)) {
        this.balapric = this.balance - (this.stanInfo.stanMoney * this.usageInfo.preTime);
      } else {
        this.balapric = this.balance;
        this.wxpric = (this.stanInfo.stanMoney * this.usageInfo.preTime) - this.balance
      }
      if(this.balance === 0){     // 余额不足，微信支付
        this.disabled = true;      // 余额禁用
        this.wxCheck = true;       // 默认微信
        this.payWays = this.wxPay;
        this.wxpric = this.payment
      }else{
        this.balCheck = true;      // 默认余额
        this.payWays = this.balPayWay;
        if(this.balance >= this.payment){     // 余额支付
          this.balapric = this.payment;
          this.balance = this.balance - this.payment
        }else{
          this.balapric = this.balance;
          this.wxpric = this.payment - this.balance
        }
      }
    });
      this.listDepo()
  },
  components: {
      card
  },
  created () {
  }
}
</script>

<style scoped>
  #app{
    width:100%;
    height: 100%;
    position: absolute;
    background: #f5f5f5;
  }
  .buttonColor{
    background-color: rgb(158, 180, 163);
  }
  .img_checkbox{
    width: 15px;
    height: 15px;
  }
  .timeselebody{
    width: 100%;
    height: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    z-index: 1;
  }
  .timeseletop{
    width: 100%;
    height:70%;
    position: absolute;
    bottom: 0;
    left: 0;
  }
  .timesele{
    width: 100%;
    height:30%;
    position: absolute;
    bottom: 0;
    left: 0;
    background-color:white;
    border-radius: 10px 10px 0px 0px;
    overflow-y: auto;
  }
  .timesele::-webkit-scrollbar {
    display: none;
  }
  .timesele div{
    width: 100%;
    height:40px;
    text-align: center;
    line-height: 40px;
  }
  #content{
    display: flex;
    background:white;
    width:100%;
    padding:30rpx 0 30rpx 50rpx ;
    font-size: 26rpx;
    height: 26rpx;
    line-height: 26rpx;
  }
  .center-one{
    background: #fff;
    width: 94%;
    margin: 30rpx auto;
    border-radius: 10rpx;
  }
  .center-one-main{
    padding: 30rpx;
  }
  .con_a{
    position: relative;
    width: 30%;
    font-size: 25rpx;
  }
  .con_b{
    position: relative;
    width: 58%;
    font-size: 25rpx;
    text-align: right;
  }
  span{
    font-size: 50rpx;
    font-weight: bold;
    color: #33CC66;
    letter-spacing: 0rpx;
  }
  #center .sy-boxa{
    height: 25rpx;
    text-align: center;
    line-height: 25rpx;
    font-size: 25rpx;
  }
  #center .sy-boxb{
    display: flex;
    justify-content: space-between;
    padding-top: 20rpx;
  }
  .center-tow{
    width: 94%;
    background: #fff;
    margin: 30rpx auto;
  }
  #center .sy-boxd{
    padding: 30rpx;
  }
  .sy-box-title{
    font-size: 25rpx;
    text-align: center;
    color: #888;
  }
  .sy-box-text .text{
    font-size: 25rpx;
    color: #33CC66;
  }
  .sy-duration{
    height: 80rpx;
    text-align: center;
    line-height: 80rpx;
    color:#888a8b;
    font-size: 25rpx;
  }
  .sy-prepay{
    text-align: center;
    font-size: 28rpx;
    height: 28rpx;
    line-height: 28rpx;
  }
 .money{
   font-size: 28rpx;
    font-weight: normal;  
    color: red;
  }
  .sy-content{
    display: flex;
    align-items: center;
    padding-top: 20rpx;
    font-size: 30rpx;
    color: #333;
  }
  .sy-arrears{
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 30rpx;
    line-height: 30rpx;
    font-size:30rpx;
    padding-top: 20rpx;
  }
  .sy-title{
    color:#888a8b;
    font-size: 25rpx;
    letter-spacing: 2rpx;
  }
  .sy-balance{
    color:#888a8b;
    font-size: 30rpx;
    font-weight: normal;
    letter-spacing: 2rpx;
  }
  .sy-text{
    color:#888a8b;
    font-size:25rpx;
  }
  .sy-arrears-title{
    color:#DF2525;
    font-family: "微软雅黑";
  }
  .sy-arrears-text{
    color:#DF2525;
    font-family: "微软雅黑";
  }
  #foot{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    margin:0rpx auto 20rpx;
    padding-top: 20rpx;
  }
  #foot .button{
    width: 85%;
    margin:20rpx auto 20rpx;
    border-radius: 25rpx;
    overflow:hidden;
  }
  #foot input{
    zoom: 0.6;
  }
  .agreement{
    width: 95%;
    text-align: center;
    color:#5E5D5D;
    font-size: 25rpx;
    vertical-align:middle;
    display: flex;
    justify-content:center;
    align-items:Center;
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
  a{
    display: inline;
    font-size: 25rpx;
    color: #2b2b2b
  }
  .payWay{
    margin: 0 auto;
    border-radius: 4px;
  }
  .balPay{
    display: flex;
    font-size: 28rpx;
    color: #5E5D5D;
    justify-content: space-between;
    padding-top: 20rpx;
    align-items: center;
  }
  .pay-text{
  display: flex;
}
.pay-balanceimg{
  width: 40rpx;
  height: 30rpx;
  margin-right: 10rpx;
  transform: translateY(4rpx);
}
  .wxPay{
   display: flex;
  font-size: 28rpx;
  color: #5E5D5D;
  justify-content: space-between;
  padding-top: 20rpx;
  align-items: center;
  }
  .pay-wximg{
  width: 40rpx;
  height: 34rpx;
  margin-right: 10rpx;
}
radio{
  transform:scale(0.7);
 }
</style>