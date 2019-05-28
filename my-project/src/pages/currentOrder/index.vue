<template>
  <div class="sy-content">
    <div class="content-details">
      <div class="content-details-main">
        <div class="sy-box radius-up">
          <div class="sy-box-title">编号</div>
          <div class="sy-box-text">
            <span class="sy-box-number"> {{order.standard}}{{order.subCabNum}} </span>号
          </div>
        </div>
        <div class="queryInfo">
          <!-- 当前订单信息 -->
          <div v-if ="order.status === 0">
            <div class="sy-box">
              <div class="sy-box-title">预用时长</div>
              <div class="sy-box-text"><span class="sy-box-number"> {{order.preTime}} </span>小时</div>
            </div>
            <div class="sy-box radius-footer sign-two">
              <div class="sy-box-title">预付款</div>
              <div class="sy-box-text"><span class="sy-box-number">¥ {{order.preAmount}}</span></div>
            </div>
          </div>
          <!-- 历史订单信息 -->
          <div v-else>
            <div class="sy-box" >
              <div class="sy-box-title">使用时长</div>
              <div class="sy-box-text"><span class="sy-box-number"> {{order.actualTime}} </span>小时</div>
            </div>
            <div class="sy-box radius-footer sign-two" >
              <div class="sy-box-title">实际付款</div>
              <div class="sy-box-text"><span class="sy-box-number">¥ {{order.actualAmount}}</span></div>
            </div>
          </div>
        </div>  
        <div class="sy-details">
          <div class="sy-details-content sign">
            <div class="sy-details-title">订单编号</div>
            <div class="sy-details-text">{{order.id}}</div>
          </div>
          <div class="sy-details-content">
            <div class="sy-details-title">寄存时间</div>
            <div class="sy-details-text">{{order.addTime}}</div>
          </div>
          <div class="sy-details-content">
            <div class="sy-details-title">结束时间</div>
            <div class="sy-details-text">{{order.endTime}}</div>
          </div>
          <div class="sy-details-content">
            <div class="sy-details-title">寄存网点</div>
            <div class="sy-details-text"> {{order.region}}</div>
          </div>
        </div>
       
      </div>
    </div>
    <div v-show ="order.status === 0">
      <div class="sy-details-btn" @click="bagTak">取包</div>
    </div>
    <div v-show ="order.status === 3 && isDepo === true">
      <div class="sy-details-btn" @click="backDepo">押金提现</div>
    </div>
  </div>
</template>

<script>
import card from '@/components/card'

export default {
  data () {
    return {
      showLoading: true,
      order: {
        id: '',
        userId: '',
        subCabId: '',
        status: '',
        startTime: '',
        endTime: '',
        preAmount: '',
        actualAmount: '',
        preTime: '',
        actualTime: '',
        addTime: '',
        phonePass: '',
        updateTime: '',
        deleted: undefined,
        region: '',
        subCabNum: '',
        standard: ''
      },
      orderId: '',
      depoInfo: '',       // 押金信息
      isDepo: false,      // 是否有押金
      deposit: 0,         // 押金金额
    }
  },
  components: {
    card
  },
  methods: {
    bagTak () {
      const url = '../bagTaking/main?'
      wx.redirectTo({ url })
    },
    //押金提现
    backDepo() {
      this.$httpWX.post({
        url:'deposit/update',
        data:{
          depoId: this.depoInfo.depoId,
          depoUserId: wx.getStorageSync('user').id,
          depoMoney: this.deposit,
          depoStatus: 2,
        }
      }).then(res =>{
        if(res.errno === 0 ){
          this.updateSub()
        }else{
          wx.showToast({title: '网络繁忙！', icon: 'none', duration: 2000})
        }
      })
      
    },
    // 修改订单
    updateSub() {
      this.$httpWX.post({
        url: 'usarec/updateRecord',
        data: {
          id: this.orderId,
          status: 4
        }
      }).then(res =>{
        if(res.errno === 0){
          console.log('修改柜子状态')
          this.$httpWX.get({
            url: 'usarec/getuser',
            data: {
              userId: wx.getStorageSync('user').id,
            }
          }).then(res => {
            wx.setStorageSync('user',res.data.user)
          }) 
          setTimeout(function() {wx.navigateBack()},2001);
          wx.showToast({title: '退还成功', icon: 'success', duration: 2000});
        }
      })
    },
    // 查询押金信息
    queryDepo() {
      this.$httpWX.get({
        url:'deposit/queryByUeaId',
        data:{
          recordId: this.orderId
        }
      }).then(res =>{
        this.depoInfo = res.data.items
        this.deposit = this.depoInfo.depoMoney
        this.isDepo = true
      }).catch(() => {
        this.depoInfo = ''
      })
    },
  },
  // 查询订单详情
  onLoad (options) {
    this.orderId = options.id
    this.$httpWX.get({
      url: 'usarec/queryUsage',
      data: {
        'id': this.orderId
      }
    }).then(response => {
      this.order = response.data
    }).catch(() => {
      this.order = ''
      this.total = 0
    })
    // 查询押金信息
    this.queryDepo();
  },
  created () {
    // let app = getApp()
  }
}
</script>

<style>
.sy-content{
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  padding-top: 100rpx;
  background-color:#f5f5f5;
  font-size: 25rpx;
}
.content-details{
  margin: auto;
  border-radius: 10rpx;
   background-color: white;
   width: 90%;
   height: auto;
}
.content-details-main{
  padding:10% 6%;
}
.sy-box-wrap{
  border-radius: 10rpx;
}
.sy-box{
  display: flex;
  justify-content: space-between;
  align-items: center; 
  margin: 0 auto 10rpx;
  height: 50rpx;
  line-height: 50rpx;

}
.sy-box-title{
  text-align: center;
  font-size: 30rpx;
}
.sy-box-text{
  color: #00cc66;
}
.sy-details{
  color: #888;
}
.sign{
  padding-top: 30rpx;
  border-top: 2rpx dashed #e2e2e2;
}
.sy-details-content{
  display: flex;
  justify-content: space-between;
  align-items: center; 
  padding-bottom: 20rpx;
}
/* .sy-details-title{

}
.sy-details-text{
}
.radius-up{
  border-radius: 10rpx 10rpx 0 0; 
}  */
.radius-footer{
   border-radius: 0 0 10rpx 10rpx; 
}
.sign-two{
  padding-bottom: 20rpx;
}
.sy-box-number{
   font-size: 50rpx;
  }
.sy-details-btn{
    width: 90%;
    height:90rpx;
    border-radius: 5px;
    color: #FFFFFF;
    font-size: 36rpx;
    background:#00cc66;
    line-height: 90rpx;
    text-align: center;
    margin: 100rpx auto;
}
</style>