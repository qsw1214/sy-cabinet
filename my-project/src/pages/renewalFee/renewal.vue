<template>
  <div @click="clickHandle" id="app">
    <div id="content">
      <div class="con_a">{{mainCabInfo.region}}</div>
      <div class="con_b">营业时间：00：00-24：00</div>
    </div>
    <div id="center">
      <div class="sy-boxa l">{{stanInfo.stanName}}收费规则：{{price}}元/小时，1天{{price * cappingTime}}元封顶</div>
      <div class="sy-boxb l" @click="backpage">
        <div class="sy-box-title">当前柜子</div>
        <div class="sy-box-text">
          <span>{{subCabInfo.subCabNum}}</span><div class="text">号</div><i-icon type="enter" />
        </div>
      </div>
      <div class="sy-boxc l">
        <div class="sy-box-title">续费时长</div>
        <div class="sy-box-text"  @click="choiceTime">
          <span>{{time}}</span><div class="text">小时</div><i-icon type="enter" />
        </div>
      </div>
      <div class="sy-boxe l">
        <div class="sy-arrears">
          <div class="sy-title">预付金额</div>
          <div class="sy-text">¥{{usageInfo.preAmount}}</div>
        </div>
        <div class="sy-arrears">
          <div class="sy-title">消费金额</div>
          <div class="sy-text">¥{{usageInfo.actualAmount}}</div>
        </div>
        <div class="sy-arrears">
          <div class="sy-arrears-title">欠费</div>
          <div class="sy-arrears-text">¥{{arrears}}</div>
        </div>
      </div>
      <div class="sy-boxd l">
        <div class="sy-duration">按使用时长计费，多退少补</div>
        <div class="sy-prepay">续费金额<div class="money">{{payment}}</div>元</div>
        <div class="sy-content">
          <div class="sy-title">余额支付(余额{{balance}})元</div>
          <div class="sy-text">¥{{balapric}}</div>
        </div>
        <div class="sy-content">
          <div class="sy-title">微信支付</div>
          <div class="sy-text">¥{{wxpric}}</div>
        </div>
        <div class="payWay">
          <radio-group class="payRadio" @change="radioChange">
            <radio class="balPay" :value='balPay' :checked='balCheck' :disabled='disabled' >余额支付</radio>
            <radio class="wxPay" :value='wxPay' :checked='wxCheck'>微信支付</radio>
          </radio-group>
        </div>

      </div>
    </div>
    <div id="foot">
      <div class="button"><i-button type="success" @click="nextup">续费</i-button></div>
      <!--      <div class="button"><i-button type="ghost" @click="takeBag">取包走人</i-button></div>-->
      <div class="agreement">
        <div class="checkbox" @click="checkdiv" :style="{backgroundColor:checknext?'#33CC66':'rgba(228, 228, 228, 1)',border:checknext?'1px solid #33CC66':'1px solid #B5B3B3'}">
          <img src="../../image/select.png" class="img_checkbox">
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
        wxPay: true,      // 微信支付
        balPay:false,     // 余额支付
        payWays: '',      // 支付方式(true 微信；false 余额)
        isWxPay: '',      // 是否需要微信支付
        refund: '',       // 退款金额
        bool: false,      // (true:退款;false:扣费)
        way: '',          // (true:续费;false:取包)
        wxOrderId: '',    //微信订单Id
        orderStatus:'',   //微信订单状态
      }
    },
    methods: {
      // 单选改变
      radioChange(e) {
        this.payWays = e.mp.detail.value;
        console.log('单选-支付方式');
        console.log(this.payWays);
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
      // 时间处理
      choiceTime () {
        this.timesele = !this.timesele;
        this.datatime=[];
        for (var i = this.renewalTime; i < 25; i++) {
          this.datatime.push(i)
        }
      },
      cloesTime () {
        this.timesele = false
      },
      clickTime (time) {
        this.time = time;
        if(time > this.cappingTime) {
          this.payment =  (this.price * this.cappingTime);
          if (this.balance > (this.price * this.cappingTime)) {
            this.balapric = this.balance - (this.price * this.cappingTime)
          } else {
            this.balapric = this.balance;
            this.wxpric = (this.price * this.cappingTime) - this.balance
          }
        } else {
          this.payment =  (this.price * this.time);
          if (this.balance > (this.price * this.time)) {
            this.balapric = this.balance - (this.price * this.time)
          } else {
            this.balapric = this.balance;
            this.wxpric = (this.price * this.time) - this.balance
          }
        }
        this.timesele = false
      },
      // 续费
      nextup () {
        var that = this;
        console.log('续费-支付方式');
        console.log(this.payWays);
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
            this.payBalance()
            // 余额/微信支付
          }else{
            console.log('余额/微信');
            this.changeBal = this.balance;
            this.payWays = true;
            this.payBalance()
          }
        }
      },
      // 余额支付
      payBalance() {
        var that = this;
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
            console.log(this.payWays);
            if(this.payWays){
              console.log('微信');
              this.wxpayPrice()
            }else{
              console.log('修改订单');
              this.updaterecord()
            }
          }else{
            wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000});
            wx.hideLoading()
          }
        })
      },
      // 微信支付
      wxpayPrice() {
        this.$httpWX.get({
          url: 'wxpay/pay',
          data: {
            'userId': wx.getStorageSync('user').id,
            'recordId': this.usageInfo.id,
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
                that.addCapRec();
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
          capMoney: this.payment,
          capDealid: this.usageInfo.id,
          capType: 1,
        }
      }).then(res => {
        if (res.errno === 0) {
          this.updaterecord()
        }
      })
    },
    // 修改订单信息
    updaterecord () {
      this.preTime = Number(this.usageInfo.preTime) + this.time;
      this.preAmount = this.usageInfo.preAmount + (this.time * this.stanInfo.stanMoney);
      this.$httpWX.post({
        url:'usarec/updateRecord',
        data:{
          id: this.usageInfo.id,
          startTime: this.usageInfo.startTime,
          preTime: this.preTime,
          preAmount: this.preAmount,
          status: 0,
          notice: 0,
        }
      }).then(res => {
        if(res.errno === 0){
          wx.showToast({title: '续费成功', icon: 'none', duration: 2000});
          wx.hideLoading();
          wx.navigateBack()
        }else{
          this.refund = this.payment;
          this.refundPay()
        }
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
    console.log(options.id);
    this.id = options.id;
    this.balance = wx.getStorageSync('user').balance;
    console.log(this.balance);
    // 查询规格信息、子柜信息、柜组信息
    this.$httpWX.get({
      url:'subcabinet/queryInfo',
      data: {
        'recordId': this.id
      }
    }).then(res => {
      console.log(res);
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
        this.renewalTime = this.usageInfo.usaCapp - this.usageInfo.preTime;
        this.time = this.renewalTime
      }else{
        this.time = this.renewalTime + 1
      }
      this.payment = this.price * this.renewalTime;
      if (this.balance >= (this.stanInfo.stanMoney * this.usageInfo.preTime)) {
        this.balapric = this.balance - (this.stanInfo.stanMoney * this.usageInfo.preTime);
        console.log('预付:'+this.balapric)
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
    })
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
    background: rgba(228, 228, 228, 1);
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
    height:80rpx;
    padding-top:30rpx;
    padding-left:50rpx;
  }
  .con_a{
    position: relative;
    width: 30%;
    color:#62656b;
    font-size: 25rpx;
  }
  .con_b{
    position: relative;
    width: 58%;
    color:#62656b;
    font-size: 25rpx;
    text-align: right;
  }
  span{
    font-size: 50rpx;
    font-weight: bold;
    color: #33CC66;
  }
  .l{
    width: 92%;
    height: 120rpx;
    margin: auto;
    border:0.5rpx dashed #888a8b;
    border-radius:10px;
    background: white;
  }
  #center .sy-boxa{
    height: 65rpx;
    text-align: center;
    line-height: 65rpx;
    margin-top: 25rpx;
    color:#888a8b;
    font-size: 25rpx;
  }
  #center .sy-boxb{
    display: flex;
    height: 130rpx;
    line-height: 130rpx;
  }
  #center .sy-boxc{
    display: flex;
    height: 115rpx;
    line-height: 115rpx;
  }
  #center .sy-boxd{
    height: 320rpx;
    margin-top: 25rpx;
  }
  #center .sy-boxe{
    height: 180rpx;
  }
  .sy-box-title{
    position: relative;
    width: 30%;
    font-size: 25rpx;
    text-align: center;
  }
  .sy-box-text{
    position: relative;
    width: 65%;
    text-align: right;
  }
  .sy-box-text .text{
    display: inline;
    font-weight: bold;
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
    height: 40rpx;
    text-align: center;
    font-size: 28rpx;
    margin-bottom: 30rpx;
  }
  .sy-prepay .money{
    display: inline;
    font-size: 28rpx;
    color: red;
  }
  .sy-content{
    display: flex;
    align-items: center;
    padding-left:25rpx;
    width: 100%;
    height: 15%;
    margin-top: 10rpx;
  }
  .sy-arrears{
    display: flex;
    align-items: center;
    padding-left:25rpx;
    width: 100%;
    height: 20%;
    margin-top: 18rpx;
  }
  .sy-title{
    position: relative;
    width: 40%;
    color:#888a8b;
    font-size: 25rpx;
  }
  .sy-text{
    position: relative;
    width: 48%;
    text-align: right;
    color:#888a8b;
    font-size:25rpx;
  }
  .sy-arrears-title{
    position: relative;
    width: 40%;
    color:#DF2525;
    font-family: "微软雅黑";
    font-size:30rpx;
  }
  .sy-arrears-text{
    position: relative;
    width: 48%;
    text-align: right;
    color:#DF2525;
    font-family: "微软雅黑";
    font-size:30rpx;
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
    width: 85%;
    height: 5%;
    margin: 0 auto;
    border-radius: 4px;
    border: 1px dashed #E4E4E4;
    background: white;
  }
  .balPay{
    width: 50%;
    font-size: 80%;
  }
  .wxPay{
    width: 50%;
    font-size: 80%;
  }
</style>
