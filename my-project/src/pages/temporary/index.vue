<template>
  <div class="temporaryPickupPage-sy">
    <div class="style-div-outer">
        <div class="style-div-centre" >
            <img class="style-div-img" :style="{width:imgwidth + 'rpx', height:imgwidth + 'rpx'}" src="/static/images/confirm.png"/>
            <p class="style-div-p" >
                <span class="style-div-number" >{{subcabnum}}号</span><sapn class="style-div-text">柜门已打开</sapn>
            </p>
            <p class="style-div-substratum-text" style="">柜子将继续使用，并持续计数</p>
        </div>
    </div>
    <button class="finish-btn" @click="complete">完成</button>
    <button @click="keepon" class="continueBagClaim-btn"  >继续取包</button>
    <div class="stern-div">
      <div @click="opendoor" class="stern-div-left">未打开门</div><div class="break"></div><div class="stern-div-right"  @click="keepon">结束订单</div>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      subcabnum: '',
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.3)
    }
  },
  onLoad (option) {
    console.log(option.subcabnum)
    this.subcabnum = option.subcabnum
    this.opendoor()
  },
  methods: {
    opendoor() {
      console.log('打开门')
    },
    complete(){
      console.log('返回首页')
      wx.navigateBack()
    },
    keepon(){
      console.log('返回上一页')
      wx.redirectTo({
        url: '../bagTaking/main'
      })
    }
  }
}
</script>

<style>

.finish-btn{
  width:85%;
  background-color:#1DB311;
  color:#FFF;
  margin-bottom: 5%;
}
.continueBagClaim-btn{
 width:85%;
 background-color:#FFF;
 color:#999;
 
}
.temporaryPickupPage-sy{
  text-align:center;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  padding-top: 10%;
  background-color:#F2F2F2;
  font-size: 25rpx;
}
.style-div-outer{
    width: 90%;
    margin:0 auto;
    margin-bottom: 30px;
    
}
.style-div-centre{
   
   border: 1px solid #fff;
    border-radius: 5px;
    -webkit-box-shadow: #666 0px 0px 10px;
    -moz-box-shadow: #666 0px 0px 10px;
    box-shadow: #666 0px 0px 10px;
    background: #FFFFFF;
}
.style-div-img{
  margin-top:10%;
}
.style-div-p{
  font-size:24px;
  margin-top:5%;
}
.style-div-number{
  color:#009900;
}

.style-div-substratum-text{
  color:#868686;
  font-size:16px;
  padding-bottom: 20%;
}
.stern-div{
  width: 100%;
  color: #949494;
  margin-top: 20px;
}
.stern-div-left{
  width: 48%;
  text-align: right;
  position: fixed;
  left: 0;
  height: 15px;
  line-height: 15px;
}
.break{
  height: 15px;
  border-left: 1px solid #2b2b2b;
  position: fixed;
  left: 50%;
}
.stern-div-right{
  width: 48%;
  text-align: left;
  position: fixed;
  left: 52%;
  height: 15px;
  line-height: 15px;
}
</style>