<template>
  <div class="fo">
    <div class="u1">
      <div class="u2">
        <div class="u2-1-img">
          <img class="u2-1-img" src="/static/images/confirm.png" />
        </div>
        <div class="u2-2-text">
          <div class="u2-2-span">
            <span class="u2-2-span1">{{order.subCabNum}}</span><span class="u2-2-span2">柜门已打开</span>
          </div>
          <div class="u2-2-p">
            <p>寄存已结束，请取出所有物品</p>
          </div>
        </div>
        <div class="u2-3-text">
          <div class="u2-3-text-span">
            <span class="u2-3-text-span1">寄存柜子</span><span class="u2-3-text-span2">{{order.subCabNum}}{{order.standard}}</span>
          </div>
          <div class="u2-3-text-span">
            <span class="u2-3-text-span1">开始时间</span><span class="u2-3-text-span2">{{order.startTime}}</span>
          </div>
          <div class="u2-3-text-span">
            <span class="u2-3-text-span1">使用时长</span><span class="u2-3-text-span2">{{order.actualTime}}小时</span>
          </div>
           <div class="u2-3-text-span">
            <span class="u2-3-text-span1">实付金额</span><span class="u2-3-text-span2">{{order.actualAmount}}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="u3">
      <div class="u3-a">
        <button class="carryOut" @click="backSelect">完成</button>
      </div>
    </div>
    <div class="u4">
      <div class="u4-a">
        <button @click="goOn">继续取包</button>
      </div>
    </div>
    <div class="u5">
      <div class="u5-text">
        <span class="u5-sapn1">门未打开联系客服</span><span class="u5-sapn2">查看订单详情</span>
      </div>
    </div>
  </div>
</template>

<script>
import card from '@/components/card'

export default {
  data () {
    return {
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
    }
  },
  components: {
    card
  },
  methods: {
    // 完成(返回首页)
    backSelect() {
      console.log("返回首页")
      wx.navigateBack()
    },
    // 继续取包
    goOn() {
      wx.navigateTo({ 
        url : '/pages/bagTaking/main'
       })
    }
  },
  onLoad(options) {
    let orderId = options.id
    console.log('订单Id:' + orderId)
    this.$httpWX.get({
      url: 'usarec/queryUsage',
      data: {
        'id': orderId
      }
    }).then(response => {
      console.log(response)
      this.order = response.data
    }).catch(() => {
      this.order = ''
      this.total = 0
    })
  },
  created () {
    // let app = getApp()
  }
}
</script>
<style scoped>
    .fo{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    background-color:rgba(242, 242, 242, 1);
    font-size: 25rpx;
    }
    .u1{
      width: 352px;
      height: 399px;
      margin: 10px auto;
      display: grid;
      border-radius: 5px;
      -webkit-box-shadow: #666 0px 0px 10px;
      -moz-box-shadow: #666 0px 0px 10px;
      box-shadow: #666 0px 0px 10px;
      background: #fff;
    }
    .u2-1-img{
      width: 156px;
      height: 156px;
      margin: 0 auto;
    }

    .u2-2-text{
      width: 352px;
      height: 60px;
      display: grid;
      border-bottom: 1px solid rgb(32, 204, 17);
      margin-top: 10px;
    }
    .u2-2-span{
      width: 208px;
      height: 31px;
      line-height: 31px;
      text-align: center;
      font-size: 24px;
      margin: 0 auto;
    }
    .u2-2-span1{
      color: #009900;
    }
    .u2-2-p{
      width: 208px;
      height: 21px;
      line-height: 21px;
      text-align: center;
      font-size: 16px;
      color: #868686;
      margin: 0 auto;
    }
    .u2-3-text{
      width: 332px;
      height:130px; 
      display: grid;
      font-size: 14px;
      margin: 18px auto;
      color: #868686;
    }
    .u2-3-text-span{
      width: 332px;
      height:20px;
    }
    .u2-3-text-span2{
      float: right;
    }
    .u3{
      width: 338px;
      height: 40px;
      color: #FFFFFF;
      font-size: 20px;
      background:rgba(51, 204, 51, 1);
      line-height: 40px;
      text-align: center;
      margin: 20px auto;
      border-radius: 5px;
    }
    .carryOut{
      background:rgba(51, 204, 51, 1);
    }
    .u4{
      width: 338px;
      height: 40px;
      border: 1px solid ;
      font-size: 20px;
      color: #868686;
      line-height: 40px;
      text-align: center;
      border-radius: 5px;
       margin: 10px auto;
    }
    .u5{
      width: 200px;
      height: 14px;
      font-size: 12px;
      color: #949494;
      margin: 160px auto;
    }
    .u5-sapn1{
      padding-right: 30px;
    }
</style>
