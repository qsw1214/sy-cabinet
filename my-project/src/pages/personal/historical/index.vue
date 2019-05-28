<template>
<div class="sy-content">
    <div class="sy-box">
        <div class="sy-box-title">
        已选择
        </div>
        <div class="sy-box-text">
            <span> {{depos.sybox}} </span>号
        </div>
    </div>
    <div class="sy-box">
        <div class="sy-box-title">
        使用时长
        </div>
        <div class="sy-box-text">
            <span> {{depos.time}} </span>小时
        </div>
    </div>
    <div class="sy-box">
        <div class="sy-box-title">
        实际付款
        </div>
        <div class="sy-box-text">
           <span> {{depos.money}} </span>
        </div>
    </div>
     <div class="sy-details">
       <div class="sy-details-content">
        <div class="sy-details-title">
        订单编号
        </div>
        <div class="sy-details-text">
           {{depos.orderno}}
        </div>
</div>
 <div class="sy-details-content">
         <div class="sy-details-title">
        寄存时间
        </div>
        <div class="sy-details-text">
           {{depos.deposittime}}
        </div>
 </div>
  <div class="sy-details-content">
         <div class="sy-details-title">
        寄存网点
        </div>
        <div class="sy-details-text"> 
          {{depos.depositdot}}
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
      depos: {
        sybox: '中柜A-15',
        time: '3',
        money: '￥3.00',
        orderno: '1501737988741654678',
        deposittime: '2019-3-14 10:19:30',
        depositdot: '西湖风景区'
      }
    }
  },
  components: {
    card
  },
  methods: {
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
  padding-top: 10%;
  background-color:rgb(224, 222, 222);
  font-size: 25rpx;
}
.sy-box{
  display: flex;
  align-items: center; 
  width: 90%;
  height: 15%;
  border: 1rpx dotted #9e9e9e;
  border-radius: 25rpx;
  margin-left: 5%;
  background-color: white;
  margin-top: 5rpx;
}
.sy-box-title{
  position: relative;
  width: 30%;
  text-align: center;
}
.sy-box-text{
  color: rgba(58, 184, 79, 0.952);
  position: relative;
  width: 65%;
  text-align: right;
}
.sy-details{
  width: 90%;
  height: 25%;
  border: 1rpx dotted #9e9e9e;
  border-radius: 25rpx;
  margin-left: 5%;
  background-color: white;
  margin-top: 5rpx;
  color: #9e9e9e;
}
.sy-details-content{
  display: flex;
  align-items: center; 
  width: 100%;
  height: 32%;
}
.sy-details-title{
  position: relative;
  width: 30%;
  text-align: center;
}
.sy-details-text{
  position: relative;
  width: 65%;
  text-align: right;
}
span{
   font-size: 50rpx;
   font-weight: bold;
   }
</style>