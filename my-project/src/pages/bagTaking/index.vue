<template>
<div>
  <div class="fo">
     <div class="u1" v-for="(item, index) in listorder" :key="index">
       <div class="u2">
         <div class="u3">
           <div class="u3-1">
            <p>{{item.main_cab_num}}</p>
          </div>
          <div class="u3-2">
            <p>{{item.sub_cab_num}}号</p>
          </div>
        </div>
        <div class="u5">
          <div class="u5-1"></div>
        </div>
      </div>
      <div class="u6">
        <div class="u6-1">
          <div class="u6-div">
            <div class="u6-2"><p>使用时长</p></div>
            <div class="u6-3"><p>{{item.actualTime}}</p></div>
          </div>
          <div class="u6-div">
            <div class="u6-2"><p>寄存时间</p></div>
            <div><p class="u6-3">{{item.startTime}}</p></div>
          </div>
          <div class="u6-div">
            <div class="u6-2"><p>网点地址</p></div>
            <div><p class="u6-3">{{item.region}}</p></div>
          </div>
          <div class="u6-div1">
            <div class="u6-2"><p>预付款</p></div>
            <div><p class="u6-3">{{item.preAmount}}元</p></div>
          </div>
          <div class="u6-div1">
            <div class="u6-2"><p>预存时间</p></div>
            <div><p class="u6-3">{{item.preTime}}小时</p></div>
          </div>
        </div>
      </div>
       <div v-if="item.status == 2">
         <div class ="u8">
           <div class="u8_p" @click="renewalFee(index)">续费</div>
           <div class="u8_p1" @click="settleFee(index)">结算取包</div>
         </div>
       </div>
       <div v-else>
         <div class="u8">
           <div class="u8_p" @click="masklayer(true,index)">取包走人</div>
           <div class="u8_p1" @click="masklayer(false,index)" >中途开门</div>
         </div>
       </div>

    </div>
    <div class="u9" v-if="showTitle">
        <span class="u9-1-span1">东西忘记取出？</span><span class="u9-1-span2">查看历史存包记录</span>
    </div>
    <div class="masklayer" v-show='hidden'>
   <div class="tipsdiv">
     <img src="../../image/warning.png" alt="" :style="{width:imgwidth + 'rpx', height:imgwidth + 'rpx'}" class="warningimg">
     <div class="tipstext">{{prompt}}
       <br/>
       （{{warning}}）
     </div>
     <div class="slide" :style="{width:divwidth + 'rpx', height:divheight + 'rpx'}"  ref="element">
       <div class="arrowimg" :style="{width:divheight-6 + 'rpx', height:divheight-6 + 'rpx', left:left + 'rpx'}"  @touchstart="touchStart($event)" @touchmove="touchMove($event)" @touchend="touchEnd($event,index)"></div>
     </div>
     <div class="footcancel" @click="masklayer">取 消</div>
   </div>
 </div>
  </div>
    <div class="modal" v-if="condition">
      <div class="modal_top">提示</div>
      <div class="modal_body">
        您没有正在使用的柜子哦，赶紧去下单吧
      </div>
      <div class="modal_foot" @click="define">确定</div>
    </div>
</div>
</template>

<script>
import card from '@/components/card'
export default {
  data () {
    return {
      id:'',
      listorder: [],
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.3),
      divwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.55),
      divheight: (wx.getSystemInfoSync().windowHeight * (750 / wx.getSystemInfoSync().windowHeight) * 0.13),
      left: 0,
      hidden: false,
      condition: false,
      showTitle: false,
      indexlist: {},
      takeBag: true,
      prompt: '',
      warning: '',
      actualAmount:'',
      preAmount: '',
      wxpric: '',
      changeBal: '',
      payFee: 0,
      balance: '',
      orderDate: '',
      upBalaRes : false,
      upSubRecRes : false,
      wxPayRes: false,
      logic: true,
      renrFee: true,
      depoInfo:[],         // 押金信息集合
      deposit: {},         // 单个押金信息
      depoId: '',          // 押金订单Id

    }
  },
  components: {
    card
  },
  methods: {
    define () {
      wx.navigateBack()
    },
    masklayer (takeBag,index) {
      this.takeBag = takeBag;
      this.indexlist = this.listorder[index];
      this.hidden = !this.hidden;
      if (takeBag) {
        this.prompt = '开门后存包将结束';
        this.warning = '务必取走所有物品'
      } else {
        this.prompt = '开门后持续计数';
        this.warning =  '放回物品时请确认关闭了柜门'
      }
    },
    touchStart (e) {
      this.startX = e.mp.changedTouches[0].clientX
    },
    touchMove (e) {
      if (e.mp.changedTouches[0].clientX > 0 && e.mp.changedTouches[0].clientX < (this.divwidth - this.divheight - 2)) {
        this.left = e.mp.changedTouches[0].clientX
      } else if (e.mp.changedTouches[0].clientX >= (this.divwidth - this.divheight - 2)) {
        this.left = (this.divwidth - this.divheight - 2)
      } else {
        this.left = 0
      }
    },
    touchEnd (e, index) {
      if (e.mp.changedTouches[0].clientX < (this.divwidth - this.divheight - 2)) {
        this.left = 0
      } else {
        this.takeBagming();
        this.left = 0;
        this.hidden = false
      }
    },
    // 更新缓存
    updateCache() {
      this.$httpWX.get({
        url: 'usarec/getuser',
        data: {
          userId: wx.getStorageSync('user').id,
        }
      }).then(res => {
        wx.hideLoading();
        wx.setStorageSync('user',res.data.user)
      }) 
    },
    // 取包
    takeBagming () {
      // 计算消费金额
      this.$httpWX.get({
        url:'usarec/cost',
        data: {
          'recordId': this.indexlist.id
        }        
      }).then(res =>{
        this.indexlist = res.data.items;
        this.balance = wx.getStorageSync('user').balance;
        if (this.takeBag) {
          this.queryDepo();
          if(this.indexlist.actualAmount < this.indexlist.preAmount){
            this.changeBal = this.indexlist.preAmount - this.indexlist.actualAmount;
            this.bool = true;
            // 退款至余额
            this.updateBalance()
          }else if(this.indexlist.actualAmount === this.indexlist.preAmount){
            this.queryDepo();
          }
        } else {
          console.log('中途开门');
          wx.redirectTo({
            url:'../locker/main?recordId=' + this.indexlist.id
          })
        }
      })
    },
    // 修改余额
    updateBalance (){
      this.$httpWX.get({
        url:'user/updateBalance',
        data: {
          'userId': wx.getStorageSync('user').id,
          'balance': this.changeBal,
          'bool': this.bool
        }
      }).then(res => {
        if(res.errno === 0) {
          this.addCapRescord();
          this.updateCache();
        }else{
          console.log('修改失败')
        }
      })
    },
    // 添加交易记录
    addCapRescord () {
      this.$httpWX.post({
        url:'capital/add',
        data: {
          capUserid: wx.getStorageSync('user').id,
          capMoney: this.changeBal,
          capDealid: this.indexlist.id,
          capType:2,
        }
      }).then(res =>{
        if(res.errno === 0){
          this.queryDepo()
        }
      })
    },
    // 查询押金信息
    queryDepo () {
      this.$httpWX.post({
        url: 'deposit/list',
        data:{
          depoUserId: wx.getStorageSync('user').id,
          depoSubId: this.indexlist.subCabId,
          depoStatus: 1,
        }
      }).then(res => {
        if(res.errno === 0){
          this.depoInfo = res.data.items;
          if(Object.keys(this.depoInfo).length === 0) {
              this.updateUsaSub()
          }else{
            this.deposit = this.depoInfo[0];
            this.updateDeposit()
          }
        }
      })
    },
    // 修改押金(使用状态)
    updateDeposit () {
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
          this.updateUsaSub();
          this.updateCache();
        }else{
          wx.showToast({title: '网络繁忙！', icon: 'none', duration: 2000})
        }
      })
    },
    // 修改柜子状态、记录状态
    updateUsaSub (){
      this.$httpWX.post({
        url: 'usarec/updateUsaRecord',
        data: {
          id: this.indexlist.id,
          actualAmount: this.indexlist.actualAmount,
          actualTime: this.indexlist.actualTime,
          subCabId: this.indexlist.subCabId,
          subStatus: 0,
          status : 1,
        }
      }).then(res => {
        if(res.errno === 0){
          wx.redirectTo({
            url:'../locker/main?recordId=' + this.indexlist.id
          })
        }else{
          wx.showLoading({title: '开箱失败,请联系管理员',success: 2000})
        }
      })
    },
    // 超时续费
    renewalFee (index) {
      this.indexlist = this.listorder[index];
      const url ='../renewalFee/main?id='+ this.indexlist.id;
      wx.redirectTo({ url })
    },
    // 结算取包
    settleFee (index){
      this.indexlist = this.listorder[index];
      const recordData = JSON.stringify(this.indexlist);
      const url ='../settlement/main?recordInfo=' + recordData;
      wx.redirectTo({ url })
    },
  },
  onLoad () {
    wx.showLoading({title: '加载中...'});
    this.listorder = [];
    this.$httpWX.get({
      url: 'usarec/listorder',
      data: {
        'userId': wx.getStorageSync('user').id
      }
    }).then(res => {
      if (res.data.type) {
        this.listorder =  res.data.listorder
        this.showTitle = true
        this.condition = false
      } else {
        this.condition = true
        this.showTitle = false
      }
    })
  },
  created () {
    // let app = getApp()
  }
}
</script>

</script>

<style scoped>
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
  border-radius: 10rpx;
  box-shadow: rgb(207, 207, 207) 5rpx 5rpx 5rpx;
}
  .u9-1-span2{
    margin-left: 10px;
  }
  .u9{
    width: 100%;
    height: 15px;
    font-size: 13px;
    color: #888;
    margin-bottom: 20px;
    display: flex;
    justify-content:center;
    align-items:Center;
  }
  .fo{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    overflow-y:scroll; 
    background-color:#f5f5f5;
    font-size: 25rpx;
  }
  .fo::-webkit-scrollbar {
    display: none;
}
 .u8_p1{
   width: 140px;
   height: 41px;
   border: 1px solid #00cc66;
   border-radius: 5px;
   color: #00cc66;
 }
 .u8_p{
   width: 140px;
   height: 41px;
   color: #fff;
   background:#00cc66;
   border-radius: 5px;
   margin-right: 30px;
 }
 .u8{
   width: 320px;
   height: 41px;
   font-size: 18px;
   margin: 70rpx auto 0;
   display: flex;
   line-height:41px;
   text-align: center; 
 }
 .renewal{
   width: 140px;
   height: 41px;
   color: #fff;
   background:rgba(51, 204, 51, 1);
   border-radius: 5px;
   margin-right: 30px;
 }
.settle{
  width: 140px;
  height: 41px;
  border: 1px solid rgba(51, 204, 51, 1);
  border-radius: 5px;
}
 .u6-3{
   width: 200px;
   height: 17px;
   margin-left: 40px;
 }
 .u6-2{
   width: 60px;
   height: 17px;
 }
 .u6-div{
   width: 310px;
   height: 17px;
   display: flex;
   margin-bottom: 15rpx;
 }
 .u6-div1{
   width: 310px;
   height: 17px;
   display: flex;
   margin-bottom: 15rpx;
 }
 .u6{
    width: 310px;
    height: 70px;
    font-size: 13px;
    color: #666;
    margin: 0 auto;
    padding-top: 5px;
 }
 .u5-2{
   width: 30px;
   height: 14px;
   color: #00CC33;
   margin-left: 215px;
 }
 .u5-1{
   width: 69px;
   height: 14px;
 }
 .u5{
   font-size: 12px;
   width: 315px;
   height: 14px;
   display: flex;
   margin-left: 15px;
   border-bottom: 1px solid #00cc66;
   padding-bottom: 3px;
   
 }
 .u3-1{
  width: 69px;
  height: 20px;
 }
.u3-2{
  width: 106px;
  height: 20px;
  color: #00cc66;
 }
 .u4{
   width: 32px;
   height: 32px;
   margin-left: 52px;
 }
  .u3{
    width: 330px;
    font-size: 18px;
    display: flex;
    justify-content:space-between;
    padding:20px 0 0 15px;
    
  }
  .u2{
    width: 330px;
    height: 50px;
    display: grid;
    margin: 0 auto;
  }
  .u1{
    width: 346px;
    height: 280px;
    display: grid;
    margin: 40px auto;
    border: 1px solid #fff;
    border-radius: 5px;
    -webkit-box-shadow: rgb(199, 199, 199) 0px 0px 10px;
    -moz-box-shadow: rgb(199, 199, 199) 0px 0px 10px;
    box-shadow: rgb(199, 199, 199)0px 0px 10px;
    background: #fff;
  }
._p7hthq {
    position: relative ;
    background: rgb(123, 124, 128) none repeat scroll 0% 0% ;       
    height: 1px ;     
}
.footcancel{
  position: absolute;
  bottom: 7%;
  width: 40%;
  left: 30%;
  font-size: 30rpx;
  color:green;
  font-weight: bold;
}
.masklayer{
  position: fixed;
  left: 0;
  top: 0;
  height: 100%;
  width: 100%;
  z-index: 1;
  background-color:rgba(105, 103, 103, 0.479);
}
.slide{
  position: absolute;
  margin-top: 13%;
  left: 10%;
  border-radius: 50rpx;
  background-color: rgb(233, 229, 229)
}
.arrowimg{
  position:relative;
  height: 90%;
  width: 18%;
  margin-top: 3rpx;
  background: url("../../image/arrow.png")no-repeat center 50%;
  background-color: white;
  border-radius: 50%;
  margin-left: 5rpx;
}
.warningimg{
  margin-top: 15%;
}
.tipstext{
  font-size: 30rpx;
  color: #cccccc;
  margin-top: 10%
}
.tipsdiv{
  position:fixed;
  top: 20%;
  left: 15%;
  height: 60%;
  width: 70%;
  background-color: white;
  border-radius: 25rpx;
  text-align: center;
}
</style>
