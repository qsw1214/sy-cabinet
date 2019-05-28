<template>
  <div class="fo" @scroll="divScroll">
    <div class="u3">
      <div class="u3_img">
        <img class="u3_img" src="/static/images/small_bell.png">
      </div>
      <div class="u3_text">本柜子支持【手机号+密码】取包</div>
    </div>
    
    <!--手机号 -->
    <div id="u5">
      <p><span>请输入手机号</span></p>
    </div>
    <div class="u6">
      <div class="u6_p">手机号</div>
      <div class="u6_text"><input type="text" @input="inputphone($event)" v-model="phone" class="phonenum" :style="{border:checknext?'1px solid #B5B3B3':'1px solid red'}"/>
      </div>
    </div>
    <div id="u7">
      <p><span>请输入4位密码</span></p>
    </div>
    <div class="u6">
      <div class="u6_p">密码</div>
      <div class="u6_text1">
            <div class="pass" @click="keylayer($event)" >{{password_one}}</div>
            <div class="pass" @click="keylayer($event)" >{{password_two}}</div>
            <div class="pass" @click="keylayer($event)" >{{password_tree}}</div>
            <div class="pass" @click="keylayer($event)" style="border:0px;">{{password_frow}}</div>
      </div>
    </div>
    
    <div class="u8">
      <div class="u8_p" @click="define">
      确定
      </div>
    </div>
      <div class="keyboard" v-show='showboard'>
        <div class="line">
          <div id="1" @click="numkey($event)" style="border-left:0px">1</div>
          <div id="2" @click="numkey($event)">2</div>
          <div id="3" @click="numkey($event)">3</div></div>
        <div class="line">
          <div id="4" @click="numkey($event)"  style="border-left:0px">4</div>
          <div id="5" @click="numkey($event)">5</div>
          <div id="6" @click="numkey($event)">6</div></div>
        <div class="line">
          <div id="7" @click="numkey($event)"  style="border-left:0px">7</div>
          <div id="8" @click="numkey($event)">8</div>
          <div id="9" @click="numkey($event)">9</div></div>
        <div class="line">
          <div @click="keylayercloer" id="ok"  style="border-left:0px">确认</div>
          <div id="0" @click="numkey($event)">0</div>
          <div id="dele" @click="delect($event)">删除</div></div>
      </div>
  </div>

</template>

<script>
  export default {
    data () {
      return {
        clickup: true,//防止用户连续点击
        checknext: true,
        showboard: false,
        password_one: '',
        password_two: '',
        password_tree: '',
        password_frow: '',
        password: '',
        phone: '',
        record: {
          preAmount: '',  //预存金额
          preTime: '',    //预存时长
          subCabId: '',   //柜子ID
          phonePass: '',  //手机号+密码
          userId: 0,      //用户ID
          status: '',
        },
        wxpric: '',       //微信需要支付金额
        balance: '',      //用户余额
        changeBal: '',
        bool: '',
        payWays: false,    // 付款方式(true:微信 ,false:余额)
        mainCabId: '',  // 柜组Id
        refund: '',     // 退款
        stanInfo:{},    // 规格信息
        mainInfo:{}     // 柜组信息
      }
    },
    components: {
    },
    methods: {
      define () {
        if(this.clickup){
          this.clickup = false
          if (this.phone === '' && this.checknext) {
            wx.showToast({title: '请填写正确的手机号码', icon: 'none', duration: 2000})
          } else {
            if (this.password.length !== 4) {
              wx.showToast({title: '请输入四位数密码', icon: 'none', duration: 2000})
            } else {
              wx.showLoading({title: '加载中...'})
              //1、查询是占用是否过期 2、调用微信支付 3、生成用户使用记录 4、跳转至开门存包页面
              this.record.phonePass = this.phone + this.password
              this.getredis()
            }
          }
        }
      },
      getredis() { //查询占用是否过期
        console.log('查询占用是否过期')
        this.$httpWX.get({
          url: 'redisca/getredis',
          data: {
            'boxid': this.record.subCabId,
            'userid': this.record.userId
          }
        }).then(res => {
          if (res.data.recode !== '1002') {
            if (res.data.recode === '1003') {
              wx.showToast({title: '请勿重复提交订单', icon: 'none', duration: 2000})
              wx.hideLoading()
            } else {
              this.wxpay()
            }
          } else {
            wx.showToast({title: '占用超时了哦！请返回重新选择柜子', icon: 'none', duration: 2000})
            wx.hideLoading()
          }
        })
      },
      // 支付方法
      wxpay () { 
        //判断是否需要使用微信支付，不使用微信支付直接调用余额里面减去扣除的 使用微信支付则首先去清除用户余额，然后向调用微信支付收取预收金额减去用户余额的金额
        //获取用户余额wx.getStorageSync('user').balance  获取用户Openid wx.getStorageSync('user').weixinOpenid   wx.getStorageSync('user').id
        
        // 用户余额、openId、id
        this.balance = wx.getStorageSync('user').balance
        this.openId = wx.getStorageSync('user').weixinOpenid
        this.refund = this.record.preAmount
        console.log('支付方式:' + this.payWays)
        // 微信支付
        if(this.payWays === 'true'){ 
          console.log('微信全额')
          console.log(this.payWays)
          this.record.status = 2
          this.addrecord()
        }else {           // 余额支付
          this.bool = false
          // 余额全额支付
          if(this.balance >= this.record.preAmount){
            console.log('余额全额')
            this.record.status = 0
            this.changeBal = this.record.preAmount
            this.payWays = false
            this.payBalance()
          }else{   // 余额/微信支付
            console.log('余额/微信')
            this.record.status = 2
            this.changeBal = this.balance
            this.payWays = true
            this.payBalance()
          }
        }
      },
      // 生成订单信息
      addrecord() {
        console.log('生成用户使用记录')
        this.$httpWX.post({
          url: 'usarec/add',
          data: {
            userId: this.record.userId,
            subCabId: this.record.subCabId,
            preAmount: this.record.preAmount,
            preTime: this.record.preTime,
            phonePass: this.record.phonePass,
            status: this.record.status,
            usaMoney: this.stanInfo.stanMoney,
            usaCapp: this.stanInfo.stanCapping,
            usaFree: this.stanInfo.stanFree,
            usaBeyond: this.stanInfo.stanBeyond
          }
        }).then(res => {
          if (res.data.addcode) {
            console.log('添加返回信息' + res)
            this.clickup = true
            if(this.payWays){
              this.wxpayPrice()
            }else{
              console.log('开箱！')
              this.addUserInfo()
              wx.redirectTo({
                url:'../locker/main?subCabId=' + this.record.subCabId
              })
            }
            wx.hideLoading()
          } else {
            this.refund = this.changeBal
            if(this.refund != 0){
              this.refundPay()
            }
          }
        })
      },
      // 修改订单信息
      updaterecord() {
        console.log('修改订单状态' + this.record.subCabId)
        this.$httpWX.get({
          url:'usarec/updateRecord',
          data:{
            'userId': wx.getStorageSync('user').id,
            'subCabId': this.record.subCabId,
            'status': 0,
          }
        }).then(res => {
          if(res.errno === 0){
            console.log('开箱！')
            this.addUserInfo()
            wx.redirectTo({
              url:'../locker/main?subCabId=' + this.record.subCabId
            })
          }else{
            this.refund = this.record.preAmount
            this.refundPay()
            this.clickup = true
          }
        })
      },
      // 修改子柜信息
      updatesub() {
        console.log('修改子柜信息')
        this.$httpWX.get({
          url:'subcabinet/updateSub',
          data:{
            'id': this.record.subCabId,
            'status': 1,
          }
        }).then(res => {
          if(res.errno === 0){
            console.log('开箱！')
            wx.redirectTo({
              url:'../locker/main?subCabId=' + this.record.subCabId 
            })
          }else{
            this.refund = this.record.preAmount
            this.refundPay()
          }
        })
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
            this.$httpWX.get({
              url: 'usarec/getuser',
              data: {
                userId: wx.getStorageSync('user').id,
              }
            }).then(res => {
              wx.hideLoading()
              wx.setStorageSync('user',res.data.user)
              this.user = wx.getStorageSync('user')
              this.addrecord()
            })
          }else{
            wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
            this.clickup = true
            wx.hideLoading()
          }
        })
      },
      // 微信支付
      wxpayPrice(){
        console.log('微信支付,用户Id' + wx.getStorageSync('user').id)
        this.$httpWX.get({
          url: 'wxpay/pay',
          data: {
            'userId': wx.getStorageSync('user').id,
            'subCabId': this.record.subCabId,
            'wxpric': this.wxpric
          }
        }).then(res => {
          console.log(res)
          if (res.errno === 0) {
            const payParam = res.data;
            var that = this
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
              },
              'fail': function(res) {
                console.log(res);
                //修改订单信息
                that.updaterecord()
                // that.refund = that.balance
                // that.refundPay()
              },
              'complete': function(res) {
                console.log(res)
              }
            })
          }
        })
      },
      // 退款
      refundPay() {
        console.log('退款金额' + this.refund)
        this.bool = true
        this.$httpWX.post({
          url: 'user/updateUser',
          data: {
            id: wx.getStorageSync('user').id,
            mobile: this.phone,
            password: this.password
          }
        }).then(res => {
          if(res.errno === 0){
            this.$httpWX.get({
              url: 'usarec/getuser',
              data: {
                userId: wx.getStorageSync('user').id,
              }
            }).then(res => {
              wx.hideLoading()
              wx.setStorageSync('user',res.data.user)
              this.user = wx.getStorageSync('user')
              wx.showToast({title: '网络繁忙！钱已退回余额,请查收', icon: 'none', duration: 2000})
              wx.hideLoading()
              const url = '../select/main?region='+ this.region + '&mainCabId=' + this.mainCabId
              wx.redirectTo({ url })
            })
          }else{
            wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
            this.clickup = true
            wx.hideLoading()
          }
        })
      },
      // 添加用户手机号密码
      addUserInfo(){
        this.$httpWX.post({
          url: 'user/updateUser',
          data: {
            id: wx.getStorageSync('user').id,
            password: this.password,
            mobile: this.phone
          }
        }).then(res => {
          if(res.errno === 0){
            console.log('添加完成')
            this.$httpWX.get({
              url: 'usarec/getuser',
              data: {
                userId: wx.getStorageSync('user').id,
              }
            }).then(res => {
              wx.hideLoading()
              wx.setStorageSync('user',res.data.user)
              this.user = wx.getStorageSync('user')
              this.addrecord()
            })
          }else{
            wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
            this.clickup = true
            wx.hideLoading()
          }
        })
      },
      inputphone (e) {
        var newphone = /^(1)+\d{10}$/
        if (!newphone.test(e.target.value)) {
          this.checknext = false
        } else {
          this.phone = e.target.value
          this.checknext = true
        }
      },
      keylayer (e) {
        this.showboard = true
      },
      keylayercloer (e) {
        this.showboard = false
      },
      numkey (e) {
        if (this.password.length === 0) {
          this.password_one = '·'
          this.password += e.target.id
        } else if (this.password.length === 1) {
          this.password_two = '·'
          this.password += e.target.id
        } else if (this.password.length === 2) {
          this.password_tree = '·'
          this.password += e.target.id
        } else if (this.password.length === 3) {
          this.password_frow = '·'
          this.password += e.target.id
        }
      },
      delect (e) {
        if (this.password.length === 4) {
          this.password_frow = ''
          this.password = this.password.substring(0, this.password.length - 1)
        } else if (this.password.length === 3) {
          this.password_tree = ''
          this.password = this.password.substring(0, this.password.length - 1)
        } else if (this.password.length === 2) {
          this.password_two = ''
          this.password = this.password.substring(0, this.password.length - 1)
        } else if (this.password.length === 1) {
          this.password_one = ''
          this.password = this.password.substring(0, this.password.length - 1)
        }
      }
    },
    onLoad (options) {
      console.log(options)
      this.record.userId =  wx.getStorageSync('user').id
      this.record.preAmount = options.preamount   // 预付款
      this.record.preTime = options.pretime       // 预用时长
      this.record.subCabId = options.boxid        // 子柜Id
      this.wxpric = options.wxpric                // 微信支付金额
      this.payWays = options.payWays              // 付款方式(true:微信 ,false:余额)
      this.stanInfo = JSON.parse(options.stanData)
      this.mainInfo = JSON.parse(options.mainData)
      this.mainCabId = this.mainInfo.mainCabId          // 柜组Id
      this.region = this.mainInfo.region                // 柜组区域
    },
    created () {
    // let app = getApp()
    }
  }
</script>

<style scoped>
.phonediv{
  position: relative;
  float: left;
  width: 30%;
  height: 100%;
}
.label{
  position: relative;
  float: left;
  width: 5%;
  height: 0px;
  border-top: 1px solid black;
}
.phonenum{
  width: 100%;
  height: 100%;
  padding-left: 5px;
  font-size: 17px;
}
.pass{
  float: left;
  width: 25%;
  height: 100%;
  border-right: 1px dashed #cccccc;
  display: flex;
  justify-content:center;
  align-items:center;
  font-size: 20px;
  font-weight: bold;
}
.line{
  width: 100%;
  height: 25%;
  border-top: 1px solid #cccccc; 
}
.line div{
  width: 32.7%;
  height: 100%;
  border-left: 1px solid #cccccc;
  display: flex;
  float: left;
  justify-content:center;
  align-items:center;
}
.line div:active{
  background-color:rgb(240, 238, 238);
} 
.keyboard{
  position:fixed;
  bottom: 0px;
  width: 100%;
  height:250px;
  z-index: 1;
  background-color: #fff;
}
.fo{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  padding-top: 10%;
  background-color:rgba(242, 242, 242, 1);
  font-size: 25rpx;
  }
 .u8{
   width: 290px;
   height: 41px;
   font-size: 20px;
   margin: 0 auto;
   border-radius: 5px;
   line-height: 41px;
   text-align: center;
   color: #fff;
   background:rgba(51, 204, 51, 1);
   margin-top: 60px;
 }

 #u7{
    font-size: 15px;
    margin: 30px 0 15px 40px;
 }
 .u6{
   width: 290px;
   height: 37px;
   font-size: 15px;
   margin: 0 auto;
   display: flex;
   border-radius: 5px;
   -webkit-box-shadow: rgb(216, 209, 209) 0px 0px 10px;
   -moz-box-shadow: rgb(233, 227, 227) 0px 0px 10px;
   box-shadow: rgb(221, 216, 216) 0px 0px 10px;
   background: #fff;

 }
  .u6_p{
     width: 35%;
     height: 37px;
     line-height: 37px;
     text-align:center;
     color: #333333;
     background-color:#e9e7e7 
  }
  .u6_text{
    width: 75%;
    height: 100%;
    display:flex;
    justify-content:center;
    align-items:center;
  }
  .u6_text1{
    width: 75%;
    height: 100%;
    display: flex; 
    border-radius: 5px;
  }
  .u6_text2{
    width: 50px;
    height: 30px;
    text-align: center;
    margin-top: 2px;
  }
  
  .u6_text3{
    border-right: 1px solid rgba(82, 83, 81, 0.466);
    height: 20px;
  }
 #u5{
    font-size: 15px;
    width: 150px;
    height: 35px;
    margin: 50px 0 0 40px;
 }
 .u3{
    width:375px;
    height: 37px;
    display: flex;
    margin-top: 20px;
  }
  .u3_img{
    line-height: 37px;
    width: 20px;
    height: 20px;
    text-align:center;
    margin: 3px 0 0 25px;
  }
  .u3_text{
    margin: 0 0 0 30px;
    width: 300px;
    height: 37px;
    font-size: 15px;
    line-height: 37px;
    text-align: left;
  }
</style>

.phonediv{
  position: relative;
  float: left;
  width: 30%;
  height: 100%;
}
.label{
  position: relative;
  float: left;
  width: 5%;
  height: 0px;
  border-top: 1px solid black;
}
.phonenum{
  width: 100%;
  height: 100%;
  padding-left: 5px;
  font-size: 17px;
}
.pass{
  float: left;
  width: 25%;
  height: 100%;
  border-right: 1px dashed #cccccc;
  display: flex;
  justify-content:center;
  align-items:center;
  font-size: 20px;
  font-weight: bold;
}
.line{
  width: 100%;
  height: 25%;
  border-top: 1px solid #cccccc; 
}
.line div{
  width: 32.7%;
  height: 100%;
  border-left: 1px solid #cccccc;
  display: flex;
  float: left;
  justify-content:center;
  align-items:center;
}
.line div:active{
  background-color:rgb(240, 238, 238);
} 
.keyboard{
  position:fixed;
  bottom: 0px;
  width: 100%;
  height:250px;
  z-index: 1;
  background-color: #fff;
}
.fo{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  padding-top: 10%;
  background-color:rgba(242, 242, 242, 1);
  font-size: 25rpx;
  }
 .u8{
   width: 290px;
   height: 41px;
   font-size: 20px;
   margin: 0 auto;
   border-radius: 5px;
   line-height: 41px;
   text-align: center;
   color: #fff;
   background:rgba(51, 204, 51, 1);
   margin-top: 60px;
 }

 #u7{
    font-size: 15px;
    margin: 30px 0 15px 40px;
 }
 .u6{
   width: 290px;
   height: 37px;
   font-size: 15px;
   margin: 0 auto;
   display: flex;
   border-radius: 5px;
   -webkit-box-shadow: rgb(216, 209, 209) 0px 0px 10px;
   -moz-box-shadow: rgb(233, 227, 227) 0px 0px 10px;
   box-shadow: rgb(221, 216, 216) 0px 0px 10px;
   background: #fff;

 }
  .u6_p{
     width: 35%;
     height: 37px;
     line-height: 37px;
     text-align:center;
     color: #333333;
     background-color:#e9e7e7 
  }
  .u6_text{
    width: 75%;
    height: 100%;
    display:flex;
    justify-content:center;
    align-items:center;
  }
  .u6_text1{
    width: 75%;
    height: 100%;
    display: flex; 
    border-radius: 5px;
  }
  .u6_text2{
    width: 50px;
    height: 30px;
    text-align: center;
    margin-top: 2px;
  }
  
  .u6_text3{
    border-right: 1px solid rgba(82, 83, 81, 0.466);
    height: 20px;
  }
 #u5{
    font-size: 15px;
    width: 150px;
    height: 35px;
    margin: 50px 0 0 40px;
 }
 .u3{
    width:375px;
    height: 37px;
    display: flex;
    margin-top: 20px;
  }
  .u3_img{
    line-height: 37px;
    width: 20px;
    height: 20px;
    text-align:center;
    margin: 3px 0 0 25px;
  }
  .u3_text{
    margin: 0 0 0 30px;
    width: 300px;
    height: 37px;
    font-size: 15px;
    line-height: 37px;
    text-align: left;
  }
</style>
