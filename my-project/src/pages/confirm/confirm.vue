<template>
  <div @click="clickHandle" id="app">
    <div id="content">
      <div class="con_a">{{region}}</div>
      <div class="con_b">营业时间：00：00-24：00</div>
    </div>    
    <div id="center">
      <div class="center-one">
        <div class="sy-boxa l">{{stanInfo.stanName}}收费规则：{{price}}元/小时，1天{{cappingTime * price}}元封顶</div>
        <div class="sy-boxb l" @click="backpage">
          <div class="sy-box-title">当前柜子</div>
          <div class="sy-box-text">
            <span>{{subCabInfo.subCabNum}}</span><div class="text">号</div><i-icon type="enter" />
          </div>
        </div>
        <div class="sy-boxc l">
          <div class="sy-box-title">预用时长</div>
          <div class="sy-box-text"  @click="choiceTime">
            <span>{{time}}</span><div class="text">小时</div><i-icon type="enter" />
          </div>  
        </div>
      </div>
    <div class="center-two">
      <div class="sy-boxd">
        <div class="sy-duration">按使用时长计费，多退少补</div>
        <div class="sy-prepay">预付<div class="money">{{payment}}</div>元</div>
        <div class="sy-content">
          <div class="sy-title">余额支付(余额{{balance}})元</div>
          <div class="sy-text">¥&nbsp;{{balapric}}</div>
        </div>
        <div class="sy-content">
          <div class="sy-title">微信支付</div>
          <div class="sy-text">¥&nbsp;{{wxpric}}</div>
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
              <radio :value='wxPay' :checked='wxCheck'></radio>
            </div>
              
          </radio-group>
        </div>
      </div>
    </div>  
    </div>
      <div id="foot">
        <div class="button"><i-button type="success" long="true" @click="nextup">确认下单</i-button></div>
        <div class="button"><i-button type="primary" long="true" @click="cancel">取消下单</i-button></div>
        <div class="agreement">
          <div class="checkbox" @click="checkdiv" :style="{backgroundColor:checknext?'#33CC66':'#fff',border:checknext?'1px solid #33CC66':'1px solid #E4E4E4'}">
            <img src="/static/images/select.svg" class="img_checkbox">
          </div>点击确认下单即同意《<a href="/pages/confirm_detail/main">用户协议</a>》
          </div>
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
/**egion区域  boxid柜子ID subCabNum柜子编号  standard柜子规格 price柜子单价/1小时  cappingTime封顶时长 （页面传值）
 *  balance用户余额 （服务器获取）
 *  time默认时间  （js设置）
 *  timesele时间选择器  datatime时间集合 checknext 是否能点击下一步 （js特效）
 *  wxpric微信需要支付费用  （传出值）*/
export default {
  data () {
    return {
      region: '',
      boxid: '',
      subCabNum: '',
      standard: '',
      price: '',
      time: 3,
      balance: 0,
      balapric: 0,
      wxpric: 0,
      timesele: false,
      datatime: [],
      checknext: true,
      cappingTime: 8,
      payment: 0,
      mainCabId: 0,
      stanId: 0,
      userid: 0,
      stanInfo: {},
      mainCabInfo: {},
      subCabInfo: {},
      balCheck: false,
      wxCheck: false,
      disabled: false,
      balPay: false,
      wxPay: true,
      payWays: undefined,
      status: '',
      phonePass: '',
      recordId: '',        // 订单Id
      depoId: '',          // 押金记录
      orderId: '',         // 微信订单Id
      orderStatus:''       // 微信订单状态
    }
  },
  components: {
    card
  },
  methods: {
    backpage () {
      this.$httpWX.get({
        url: 'redisca/delredis',
        data: {
          'boxid': this.boxid,
          'userid': this.userid
        }
      }).then(res => {
        const url = '../select/main?region='+this.region+'&mainCabId='+ this.mainCabId;
        wx.redirectTo({ url })
      })  
    },
    // 单选改变
    radioChange(e) {
      this.payWays = e.mp.detail.value;
      // 微信支付
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
    choiceTime () {
      this.timesele = !this.timesele;
      this.datatime=[];
      for (var i = 1; i < 25; i++) {
        this.datatime.push(i)
      }
    },
    cloesTime () {
      this.timesele = false
    },
    clickTime (time) {
      this.time = time;
      // 封顶
      if(time > this.cappingTime) {
        this.payment =  (this.price * this.cappingTime);
        if (this.balance > this.payment) {
          this.balapric = this.payment
        } else {
          this.balapric = this.balance;
          this.wxpric = this.payment - this.balance
        }
      } else {
        this.payment =  (this.price * this.time);
        // 微信支付
        if(this.payWays === 'true'){
          this.balapric = 0;
          this.wxpric = this.payment
        // 余额支付
        }else{
          if (this.balance > this.payment) {
            this.balapric = this.balance - this.payment;
            this.wxpric = 0
          } else {
            this.balapric = this.balance;
            this.wxpric = this.payment - this.balance
          }
        }
      }
      this.timesele = false
    },
    checkdiv () {
      this.checknext = !this.checknext
    },
    // 取消订单
    cancel () {
      console.log('取消下单')
      this.$httpWX.post({
        url:'usarec/updateUsaRecord',
        data:{
          id: this.recordId,
          status: 4,
        }
      }).then(res => {
        if(res.errno === 0) {
          if(Object.keys(this.depoId).length === 0) {
            wx.navigateBack({
            });
          }else {
            // 退还押金
            this.updateDeposit();
          }
        }
      })
    },
    // 修改押金(使用状态)
    updateDeposit () {
      console.log('退还押金')
      this.$httpWX.post({
        url:'deposit/update',
        data:{
          depoId: this.depoId,
          depoUserId: wx.getStorageSync('user').id,
          depoMoney: this.stanInfo.stanDeposit,
          depoStatus: 2,
        }
      }).then(res =>{
        if(res.errno === 0){
          this.updateCache();
        }else{
          wx.showToast({title: '网络繁忙！', icon: 'none', duration: 2000})
        }
      })
    },
    // 确认下单
    nextup () {
      if (this.checknext) {
        wx.showLoading({title: '开门中...'});
        // 支付订单
        this.balance = wx.getStorageSync('user').balance;
        this.openId = wx.getStorageSync('user').weixinOpenid;
        this.refund = this.payment;
        // 微信支付
        if(this.payWays){
          console.log('微信全额');
          this.wxpayPrice()
        // 余额支付
        }else {
          this.bool = false;
          if(this.balance >= this.payment){   // 余额全额支付
            this.status = 0;
            this.changeBal = this.payment;
            this.payWays = false;
            this.payBalance()
          }else{                            // 余额/微信支付
            console.log('余额/微信');
            this.status = 2;
            this.changeBal = this.balance;
            this.payWays = true;
            this.payBalance()
          }
        }
      } else {
        wx.showToast({title: '必须同意用户协议才能使用哦！', icon: 'none', duration: 2000})
      }
    },
    // 余额支付
    payBalance() {
      this.$httpWX.get({
        url: 'user/updateBalance',
        data: {
          'userId': wx.getStorageSync('user').id,
          'balance': this.changeBal,
          'bool': this.bool
        }
      }).then(res => {
        if(res.errno === 0){
          this.addCapRec();     // 添加交易明细
          this.$httpWX.get({    // 更新缓存
            url: 'usarec/getuser',
            data: {
              userId: wx.getStorageSync('user').id,
            }
          }).then(res => {
            wx.hideLoading();
            wx.setStorageSync('user',res.data.user);
            this.user = wx.getStorageSync('user')
          })
        }else{
          wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000});
          this.clickup = true;
          wx.hideLoading()
        }
      })
    },
    // 微信支付
    wxpayPrice(){
      this.$httpWX.get({
        url: 'wxpay/pay',
        data: {
          'userId': wx.getStorageSync('user').id,
          'recordId': this.recordId,
          'wxpric': this.wxpric,
          'isPay': 1
        }
      }).then(res => {
        if (res.errno === 0) {
          const payParam = res.data.result;
          this.orderId = res.data.orderId;
          var that = this;
          wx.requestPayment({
            'timeStamp': payParam.timeStamp,
            'nonceStr': payParam.nonceStr,
            'package': payParam.packageValue,
            'signType': 'MD5',
            'paySign': payParam.paySign,
            'success': function(res) {
              //添加交易记录信息
              that.addCapRec()
              that.orderStatus = 1
              //修改微信订单状态
              that.updateWxOrder()
            },
            'fail': function(res) {
              that.orderStatus = 2
              //修改微信订单状态
              that.updateWxOrder()
            }
          })
        }
      })
    },
    // 添加交易明细
    addCapRec() {
      this.$httpWX.post({
        url:'capital/add',
        data:{
          capUserid: wx.getStorageSync('user').id,
          capMoney: this.payment,
          capDealid: this.recordId,
          capType: 3,
        }
      }).then(res => {
        if (res.errno === 0) {
          this.updateRecord()
        }
      })
    },
    // 修改使用记录
    updateRecord() {
      this.$httpWX.post({
        url:'usarec/updateUsaRecord',
        data:{
          id: this.recordId,
          preAmount: this.payment,
          preTime: this.time,
          subCabId: this.subCabInfo.id,
          subStatus: 1,
          status: 0,
          
        }
      }).then(res => {
        if(res.errno === 0){
          wx.hideLoading();
          wx.redirectTo({
            url:'../locker/main?recordId=' + this.recordId
          })
        }
      })
    },
    //更新缓存
    updateCache() {
      this.$httpWX.get({
        url: 'usarec/getuser',
        data: {
          userId: wx.getStorageSync('user').id,
        }
      }).then(res => {
        wx.setStorageSync('user',res.data.user);
        wx.navigateBack()
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
  onLoad (options) {
    //子柜信息、规格信息、柜组信息
    this.subCabInfo = JSON.parse(options.subData);
    this.stanInfo = JSON.parse(options.stanData);
    this.region = options.mainRegion;
    this.recordId = options.recordId;
    this.depoId = options.depoId;
    this.price = this.stanInfo.stanMoney;
    this.payment =  (this.price * this.time);
    this.balance = wx.getStorageSync('user').balance;
    this.userid = wx.getStorageSync('user').id;
    // 初始化支付方式
    if(this.balance <= 0){  //微信支付
      this.wxpric = (this.price * this.time);
      this.disabled = true;      // 余额禁用
      this.wxCheck = true;       // 默认微信
      this.payWays = true        // 支付方式
    }else {                      // 余额支付
      if(this.balance > this.payment){
        this.balapric = this.balance - (this.price * this.time)
      }else{
        this.balapric = this.balance;
        this.wxpric = this.payment - this.balance
      }
      this.balCheck = true;      // 默认余额
      this.disabled = false;
      this.payWays = false;      // 支付方式
    }
  },
  created () {
    // let app = getApp()
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
    height:80rpx;
    padding-top:30rpx;
    padding-left:50rpx;
    font-size: 26rpx;
   }
   .con_a{
    position: relative;
    width: 30%;
   }
   .con_b{
    position: relative;
    width: 58%;
    text-align: right;
   }
   span{
   font-size: 50rpx;
   font-weight: bold;
   color: #33CC66;
   }
   .center-one,.center-two{
     width: 94%;
     margin: 30rpx auto;
     background: white;
     border-radius: 10rpx;
     padding: 3% 0 ;
   }
   .sy-boxa{    
     text-align: center;
     margin-top: 15rpx;
     color:#000;
     font-size: 26rpx;
     padding-bottom: 13rpx;
   }
 .sy-boxb{
     display: flex;
     color: #888;
   }
    .sy-boxc{
     display: flex;
     color: #888;
   }
   
   #center .sy-boxd{
     margin-top: 15rpx;
   }
   .sy-box-title{
     position: relative;
     width: 25%;
     font-size: 26rpx;
     text-align: center;
     line-height: 33px;
   }
   .sy-box-text{
     position: relative;
     width: 69%;
     text-align: right;
   }
   .sy-box-text .text{
     display: inline;
     font-weight: bold;
     font-size: 25rpx;
     color: #33CC66;
   }
   .sy-duration{
     text-align: center;
     color:#000;
     font-size: 26rpx;
     margin-bottom: 10rpx;
     letter-spacing: 2rpx;
   }
   .sy-prepay{
     height: 50rpx;
     text-align: center;
     font-size: 26rpx;
     margin-bottom: 15rpx;
   }
   .sy-prepay .money{
     display: inline;
     font-size: 28rpx;
     color: red;
   }
  .sy-content{
    display: flex;
    align-items: center; 
    padding:0 0 30rpx 15rpx;
    width: 100%;
  }
  .sy-title{
  position: relative;
  width: 40%;
  color:#888a8b;
  font-size: 26rpx;
  letter-spacing:2rpx;
  padding-left: 23rpx;
  display: flex;
  justify-content: space-between;
}
  .sy-text{
  position: relative;
  width: 47%;
  text-align: right;
  color:#888a8b;
  font-size: 26rpx;
}
.symbol{
  margin-right: 10rpx;
}
#foot{
  position: absolute;
  width: 100%;
  margin: 0 auto;
  bottom: 10px;
  margin:5% auto 10rpx;
  padding-top: 20rpx;
}
#foot .button{
  width: 92%;
  margin:20rpx auto 0; 
  border-radius: 10rpx;
  overflow:hidden;
}
#foot input{
   zoom: 0.6;
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
}
.checkbox{
  width: 13px;
  height: 13px;
  position: inherit;
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
  height: 20%;
  margin: 0 auto;
  border-radius: 4px;
  padding-bottom: 20rpx;
}
.balPay{
  display: flex;
  font-size: 30rpx;
  color: #5E5D5D;
  justify-content: space-between;
  padding: 0 5% 4% 4%;
}
.wxPay{
  display: flex;
  font-size: 30rpx;
  color: #5E5D5D;
  justify-content: space-between;
  padding: 0 5% 0 4%;
}
radio{
  transform:scale(0.7);
}
.pay-text{
  padding-left: 10rpx;
  display: flex;
}
.pay-balanceimg{
  width: 40rpx;
  height: 35rpx;
  margin-right: 10rpx;
  transform: translateY(4rpx);
}
.pay-wximg{
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
}
</style>