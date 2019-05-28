<template>
 <div>  <i-button type="primary" @click="masklayer">这是一个按钮</i-button>
 <div class="masklayer" v-show='hidden'>
   <div class="tipsdiv">
     <img src="../../image/warning.png" alt="" :style="{width:imgwidth + 'rpx', height:imgwidth + 'rpx'}" class="warningimg">
     <div class="tipstext">开门后，存包将结束
       <br/>
       （务必取走所有物品）
     </div>
     <div class="slide" :style="{width:divwidth + 'rpx', height:divheight + 'rpx'}">
       <div class="arrowimg" :style="{width:divheight-6 + 'rpx', height:divheight-6 + 'rpx', left:left + 'rpx'}"  @touchstart="touchStart($event)" @touchmove="touchMove($event)" @touchend="touchEnd($event,index)"></div>
     </div>
     <div class="footcancel" @click="masklayer">取 消</div>
   </div>
 </div>
 </div>
</template>

<script>
export default {
  data () {
    return {
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.3),
      divwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.55),
      divheight: (wx.getSystemInfoSync().windowHeight * (750 / wx.getSystemInfoSync().windowHeight) * 0.13),
      left: 0,
      hidden: false
    }
  },
  components: {
  },
  methods: {
    masklayer (e) {
      this.hidden = !this.hidden
    },
    touchStart (e) {
      this.startX = e.mp.changedTouches[0].clientX
    },
    touchMove (e) {
      if (e.mp.changedTouches[0].clientX > 0 && e.mp.changedTouches[0].clientX < (this.divwidth - this.divheight - 2)) {
        this.left = e.mp.changedTouches[0].clientX
      } else if (e.mp.changedTouches[0].clientX >= (this.divwidth - this.divheight - 2)) {
        this.left = (this.divwidth - this.divheight - 2)
        console.log('解锁成功')
        this.left = 0
        this.hidden = false
      } else {
        this.left = 0
      }
    },
    touchEnd (e, index) {
      if (e.mp.changedTouches[0].clientX < (this.divwidth - this.divheight - 2)) {
        this.left = 0
      } else {
        console.log('解锁成功')
        this.left = 0
        this.hidden = false
      }
    }
  },
  created () {
    // let app = getApp()
  }
}
</script>

<style>
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
