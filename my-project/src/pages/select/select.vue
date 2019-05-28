<template>
  <div @click="clickHandle" id="app">
    <div id="content">
      <div class="header-area" style="">您当前区域：{{region}}</div>
      <div class="header-hint" style="">温馨提示：请记住您的存储区域，避免造成不必要的麻烦！</div>
    </div>
    <div class="test">
      <div class="container" v-for="(items,index) in stalist" :key="index" @click="getSub(index)">
          <div class="standard" style="background:#00cc66;">
            <div class="box">
              <div class="box_left"></div>
              <div class="stanName">{{items.stanName}}</div>
              <div class="box_right"></div>
            </div>
            <div class="subSize">剩余&nbsp;{{items.subSize}}</div>
            <div class="stanPrice"><span>{{items.stanName}}:{{items.stanMoney}}元/小时  {{items.stanCapping*items.stanMoney}}元封顶</span></div>
          </div>
      </div>
    </div>
    <div class="modal" v-show="condition">
      <div class="modal_top">提示</div>
      <div class="modal_body">
        当前柜组没空闲柜子了，请选择其他的柜子
      </div>
    </div>
    <!-- 手机号绑定，设置密码 -->
    <div class="phone_body" v-show="phonebody">
      <div class="body_div">
      <div class="div_head_tips">
        请绑定手机号码，并输入四位数密码，以便于密码取包与到期提醒！
      </div>
      <div class="div_head_tips">
        <button open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" class="obtain" v-show="condition">绑定手机号</button>
        <div>{{phone}}</div>
      </div>
      <div class="div_head_tips">
        <div>请输入四位数密码并牢记 </div> 
        <div class="pass" @click="keylayer($event)" style="margin-left: 0%;">{{password_one}}</div>
        <div class="pass" @click="keylayer($event)" >{{password_two}}</div>
        <div class="pass" @click="keylayer($event)" >{{password_tree}}</div>
        <div class="pass" @click="keylayer($event)" >{{password_frow}}</div>
      </div>
      <div class="binding" @click="define">确定</div>
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
          <div id="dele" @click="delect($event)">删除</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import card from '@/components/card'

export default {
  data () {
    return {
      toptabselect: -1,
      select: -1,
      region: '',
      mainCabId: '',
      boxlist: {},      // 子柜信息
      stanInfo: {},     // 规格信息
      mainInfo:{},      // 柜组信息
      recordInfo:{},    // 订单信息
      depoInfo: [],     // 押金集合
      stalist: [],
      mainlist: [],
      items: [],
      recordList: [],
      condition: false, 
      boxid: '',
      stanId: '',
      userid: 0,
      recordId: '',     // 订单Id（未完成）
      depoId: '',       // 押金Id
      data: '',
      subSize:'',       // 可使用数量
      useRecSize: '',   // 订单数量
      stanbg: '#00cc66',
      phonebody: false,
      showboard: false,
      password_one: '',
      password_two: '',
      password_tree: '',
      password_frow: '',
      password: '',
      condition: true,
      mobile:'',
      pass:'',
    }
  },
  components: {
    card
  },
  methods: {
    computed: {
      computedClassObject () {
        return 'career'//重点：只需要在computed 方法生成 class的字符串
      }
    },
    // 选择柜子存包
    selectRecSub (index) {
      // 查询未完成订单
      this.$httpWX.get({
        url:'usarec/selectRecord',
        data: {
          'userId': wx.getStorageSync('user').id,
          'status': 3
        }
      }).then(res => {
        if(res.errno === 0){
          this.recordList = res.data.items
          // 不存在未完成订单
          if(Object.keys(this.recordList).length === 0){
            this.getSub(index);
          }else{
            this.recordInfo = this.recordList[0]      
            // 查询是否有押金
            this.$httpWX.get({
              url:'deposit/queryByUeaId',
              data:{
                'recordId': this.recordInfo.id
              }
            }).then(res => {
              this.stanInfo = this.stalist[index];
              this.selectAll(res.data)
            })
          }
        }
      })
    },
    // 查询订单信息
    selectAll (options) {
      this.$httpWX.get({
        url:'subcabinet/queryInfo',
        data: {
          'recordId': this.recordInfo.id
        }
      }).then(res => {
        if(res.errno === 0){
          const subData = JSON.stringify(res.data.sub);
          const stanData = JSON.stringify(res.data.stan);
          if(options.isNot){
            this.depoId = options.items.depoId
            const url = '../confirm/main?stanData=' + stanData
              + '&mainRegion=' + res.data.main.region + '&subData=' + subData 
              + '&recordId=' + this.recordInfo.id + '&depoId=' + this.depoId;
            wx.redirectTo({url})
          }else{
            if(this.recordInfo.stanDeposit !== 0){
              const url = '../payDeposit/main?stanInfo=' + stanData 
                + '&mainRegion=' + this.region + '&subInfo=' + subData 
                + '&recordId=' + this.recordInfo.id + '&depoId=' + this.depoId;
              wx.redirectTo({url})
            }else{
              const url = '../confirm/main?stanData=' + stanData
                + '&mainRegion=' + res.data.main.region + '&subData=' + subData 
                + '&recordId=' + this.recordInfo.id + '&depoId=' + this.depoId;
              wx.redirectTo({url})
            }
          } 
        }
      })
    },
    // 获取子柜信息
    getSub (index) { 
      this.stanInfo = this.stalist[index];
      this.stanId = this.stanInfo.id;
      this.subSize = this.stanInfo.subSize;
      if(this.subSize === 0){
        wx.showToast({title: '没有空闲柜子了，请选择其他类型储物柜', icon: 'none', duration: 2500})
      }else {
        // 查询用户使用数量
        this.$httpWX.get({
          url:'usarec/queryRecordCount',
          data: {
            'userId': wx.getStorageSync('user').id
          }
        }).then(res => {
          if(res.errno === 0){
            this.useRecSize = res.data.total
            if(this.useRecSize < 5){
              // 获取柜子信息
              this.selectSub()
            }else{
              setTimeout(function() {wx.navigateBack()},2001)
              wx.showToast({title: '存储次数达上限(5次),请取包后重试', icon: 'none', duration: 2000});
            }
          }
        })
      }
    },
    define () {
      wx.navigateBack()
    },
    // 获取柜子编号
    selectSub(){
      this.$httpWX.get({
        url:'standards/getSub',
        data: {
          'stanId':this.stanInfo.id,
          'mainId':this.mainCabId,
        }
      }).then(res => {
        if(res.errno === 0) {
          this.stanInfo = res.data.stan;
          this.mainCabInfo = res.data.mainCab;
          this.subCabInfo = res.data.subCab;
          //添加订单信息
          this.addrecord()
        }
      })
    },
    // 创建订单(未完成)
    addrecord() {
      this.$httpWX.post({
        url: 'usarec/addRec',
        data: {
          userId: wx.getStorageSync('user').id,
          subCabId: this.subCabInfo.id,
          status: 3,
          usaMoney: this.stanInfo.stanMoney,
          usaCapp: this.stanInfo.stanCapping,
          usaFree: this.stanInfo.stanFree,
          usaBeyond: this.stanInfo.stanBeyond
        }
      }).then(res => {
        if (res.errno === 0) {
          this.recordId = res.data.recordId;
          this.jump()
        }else{
          wx.showToast({title: '网络繁忙，请刷新后重试', icon: 'none', duration: 2000})
        }
      })
    },
    jump() {
      console.log('页面跳转')
      let data = JSON.stringify(this.stanInfo);
      let subData = JSON.stringify(this.subCabInfo);
      console.log(this.stanInfo.stanDeposit)
      if(this.stanInfo.stanDeposit === 0){
        console.log('预存')
        const url = '../confirm/main?stanData=' + data + '&mainRegion=' + this.region 
          + '&subData=' + subData + '&recordId=' + this.recordId + '&depoId=' + '';
        wx.navigateTo({url})
      }else{
        console.log('押金')
        const url = '../payDeposit/main?stanInfo=' + data + '&mainRegion=' + this.region 
          + '&subInfo=' + subData + '&recordId=' + this.recordId;
        wx.navigateTo({url})
      }
    },
    define () {
      if (this.phone === '') {
        wx.showToast({title: '请填写正确的手机号码', icon: 'none', duration: 2000})
      } else {
        if (this.password.length !== 4) {
          wx.showToast({title: '请输入四位数密码', icon: 'none', duration: 2000})
        } else {
          wx.showLoading({title: '加载中...'})
          this.addUserInfo()
        }
      }
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
          this.$httpWX.get({
            url: 'usarec/getuser',
            data: {
              userId: wx.getStorageSync('user').id,
            }
          }).then(res => {
            wx.hideLoading()
            wx.setStorageSync('user',res.data.user)
            this.user = wx.getStorageSync('user')
            this.phonebody = false
          })
        }else{
          wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
          this.clickup = true
          wx.hideLoading()
        }
      })
    },
    keylayer (e) {
      this.showboard = true
    },
    keylayercloer (e) {
      this.showboard = false
    },
    numkey (e) {
      if (this.password.length === 0) {
        this.password_one = e.target.id
        this.password += e.target.id
      } else if (this.password.length === 1) {
        this.password_two = e.target.id
        this.password += e.target.id
      } else if (this.password.length === 2) {
        this.password_tree = e.target.id
        this.password += e.target.id
      } else if (this.password.length === 3) {
        this.password_frow = e.target.id
        this.password += e.target.id
      }
    },
    // 键盘删除
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
    },
  },
  onLoad (options) {
    this.region = options.region;
    this.mainCabId = options.mainCabId;
  },
  onShow() {
    this.userid = wx.getStorageSync('user').id
    this.mainlist =  [];
    this.items =  [];
    this.stalist =  [];
    this.condition = false;
    wx.showLoading({title: '加载中...'});
    this.$httpWX.get({
      url: 'standards/listStan',
      data: {
        'stanMianId': 1
      }
    }).then(res => {
      this.stalist = res.data.items
    })
    this.mobile = wx.getStorageSync('user').mobile
    this.pass = wx.getStorageSync('user').password
    if (this.mobile.length !== 11 || this.pass.length !== 4) {
      this.phonebody = true
    }
  },
  created() {
  }
}
</script>

<style scoped>

.header-area{
  text-align: center;
  font-size:38rpx;
  padding-top:20rpx;
  padding-bottom:8rpx;
  font-family:'微软雅黑';
}
.header-hint{
  padding-left:10rpx;
  font-size:25rpx;
  padding: 10rpx 0 26rpx 26rpx;
  font-family:'Microsoft Yahei';
  letter-spacing: 2rpx;
}
.modal_top{
  height: 30px;
  line-height: 30px;
  text-align: center;
  font-size: 14px;
  font-weight: bold;
}
.modal_body{
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 14px;
  color: #2b2b2b;
}
.modal_foot{
  height: 30px;
  line-height: 30px;
  text-align: center;
  font-size: 14px;
  color: #2b2b2b;
}
.modal{
  position: fixed;
  top: 30%;
  left: 10%;
  width: 80%;
  height:100px;
  z-index: 99;
  border: 1px solid #cccccc;
  background-color: #f8f8f9;
  border-radius: 6rpx;
  box-shadow: darkgrey 10rpx 10rpx 30rpx 5rpx ;
}
#app{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background:#f5f5f5;
 }
 #content{
  background: #00cc66;
  color: white;
 }
 .test {
  width: 100%;
  height: 100%;
  background: #f5f5f5;
 }
.testTitle {
  width:25%;
  flex: 1;
  text-align: center;
  font-size: 30rpx;
}
.selected {
  background: rgba(242, 242, 242, 1);
}
.s-ctn{
  height: 60px;
  white-space: nowrap;
  font-size:16px;
}
.toptab{
  width: 80px;
  height: 60px;
  text-align: center;
  line-height: 60px;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-all;
  overflow: hidden;
  text-overflow:ellipsis;
  display: inline-block;
}

.container {
  width: 100%;
  height: 110px;
  padding:0 ;
  margin:30rpx auto;
}

.container .row{
  padding-top:20rpx;
  display: flex;
  flex-direction:row;
  flex-wrap:wrap;
  justify-content:center; 
}
.active{
 background: #1FC055;
}
.active div{
  color: white;
}
.standard{
  display: flex;
  flex-wrap: wrap;
  width: 94%;
  text-align: center;
  border-radius: 10rpx;
  /* background:-webkit-linear-gradient(left,#00cc66,#04bd60); */
}
.box{
  height: 105rpx;
	width: 50%;
  margin-left: 24%;
  margin-top: 1rpx;
	display: flex;
	justify-content: center;
	align-items: Center;
}
.box_left{
	width: 0px;
	height: 0px;
	border-top: 46px solid white;
	border-right: 0px solid rgba(0, 0, 0, 0);
	border-bottom: 0px solid rgba(0, 0, 0, 0);;
	border-left: 14px solid rgba(0, 0, 0, 0);
	/* transform: rotate(180deg); */
}
.stanName{
  width: 60%;
  height: 46px;
  margin :0px;
  font-size: 95%;
  display: flex ;
  justify-content: center;
	align-items: Center;
  font-family: '微软雅黑';
  letter-spacing: 8px;
  line-height: 48px;
  color: #00cc66;
  background: #fff;
}
.box_right{
	width: 0px;
	height: 0px;
	border-top: 0px solid rgba(0, 0, 0, 0);
	border-right: 0px solid rgba(0, 0, 0, 0);
	border-bottom: 46px solid white;
	border-left: 14px solid rgba(0, 0, 0, 0);
	transform: rotate(180deg);
}
.subSize{
  width: 20%;
  height: 25px;
  margin-top: 2%;
  font-size: 70%;
  font-family: '微软雅黑';
  line-height: 25px;
  border-radius: 50px;
  color: #215b0f;
  background-color: rgb(179, 248, 214);
}
.stanPrice{
  width: 100%;
  font-size: 66%;
  letter-spacing: 1px;
  color: white;
  margin: 46rpx 0 20rpx 0;
}
/* 设置开箱密码 */
.binding{
    width: 60%;
    height: 50px;
    position: absolute;
    bottom: 30px;
    left: 20%;
    text-align: center;
    line-height: 50px;
    background-color: rgb(26,173,25);
    color: white;
    font-size: 30rpx;
    border-radius: 5px;
}
.binding:active{
    background-color: rgba(26,173,25, 0.8);
}
.obtain{
  background-color: white;
  color: #2b2b2b;
  border-radius: 98rpx; 
  font-size: 28rpx;
  padding: 15rpx;
}
.obtain::after {
  border-radius: 98rpx;
}
.obtain:active{
  background-color: rgba(0, 0, 0, 0.12);
}
.keyboard{
  position:fixed;
  bottom: 0px;
  width: 100%;
  height:300px;
  z-index: 2;
  background-color: #fff;
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
.phone_body{
    width: 100%;
    height: 100%;
    z-index: 1;
    position: fixed;
    top: 0;
    background-color: rgba(0, 0, 0, 0.3)
}
.div_head_tips{
    font-size: 25rpx;
    width: 90%;
    margin-left: 5%;
    margin-top: 20px;
}
.div_head_tips .pass{
    margin-top: 20px;
    width: 21%;
    margin-left: 4%;
    height: 40px;
    text-align: center;
    border: 1px solid #bbbbbb;
    line-height: 10vw;
    float: left;
    font-size: 30rpx;
    font-weight: 700;
}
.body_div{
    width: 70%;
    background-color: white;
    top: 15%;
    margin-left: 15%;
    position: fixed;
    height: 320px;
    border-radius: 10px;
    box-shadow:  2px 2px 5px 3px rgba(0, 0, 0, 0.12);
}
.wx-login-btn {
    margin: 20% 0 40rpx 0;
    height: 100rpx;
    line-height: 100rpx;
    font-size: 30rpx;
    border-radius: 6rpx;
    width: 90%;
    color: #fff;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    position: flex;
    bottom: 0;
    left: 0;
    padding: 0;
    margin-left: 5%;
    text-align: center;
    border-radius: 20px;
    box-shadow:  2px 2px 5px 3px rgba(0, 0, 0, 0.12);
}
</style>

