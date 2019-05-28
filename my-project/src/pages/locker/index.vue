<template>
  <div class="fo">
      <div class="u1">
        <div class="u2">
          <div class="u2-img">
            <img class="open-image" src="/static/images/open.svg" :style="{width: imgwidth + 20 + 'rpx', height: imgwidth + 20 +  'rpx'}"/>
          </div>
          <div class="u2-p">
            {{newOrder.sub_cab_num}}
          </div>
        </div>
        <div class="u3">
          <div class="u3-p">
            <p>柜门已打开</p>
            <p class="text-p">请放入物品并关好柜门</p>
          </div>
        </div>
        <div class="u4">
          <div class="u5">
            <div class="u5-1">
              <div class="text-p text-p2">订单编号</div>
              <div class="text-p text-p3">{{newOrder.id}}</div>
            </div>
            <div class="u5-1">
              <div class="text-p text-p2">寄存时间</div>
              <div class="text-p text-p3">{{newOrder.startTime}}</div>
            </div>
            <div class="u5-1">
              <div class="text-p text-p2">寄存网点</div>
              <div class="text-p text-p3">{{newOrder.region}}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="u8">
        <div class="u8_p" @click="backpage">
         确定
        </div>
      </div>
    <div class="u6">
      <div class="u6-1">
        <img class="u6-1" src="/static/images/plaint.png"/>
      </div>
      <div class="u6-4">
        <div class="u6-2">
          <p>门未开？</p>
        </div>
        <div class="u6-3">
          <p @click="opendoor">再次开门</p>
        </div>
      </div>
    </div>
    <div class="u9">
      <div class="u7">
        <div class="u7-1">
          <p>存包异常帮助</p>
        </div>
        <div class="u7-2">
          <p>查看取包流程</p>
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
      newOrder: {},
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.15),
    }
  },
  components: {
    card
  },
  methods: {
    backpage () {
      wx.navigateBack()
    },
    opendoor () {
      console.log('连接机器')
      
      this.$httpWX.get({
        url: 'box/oppen',
        data:{
          mainCabNum: this.newOrder.main_cab_num,
          subCabNum: this.newOrder.sub_cab_num,
        }
      }).then(res =>{
        console.log(res)
        wx.hideLoading()
        // if(res.data.type){
        //   wx.hideLoading()
        // }else{
        //   wx.hideLoading()
        //   wx.showToast({title: '开箱失败,请联系管理员', icon: 'fail', duration: 2000})
        // }
      })
    }
  },
  onLoad (options) {
    wx.showLoading({title: '开门中'})
    this.$httpWX.get({
      url: 'usarec/getneworder',
      data: {
        'id': options.recordId
      }
    }).then(res => {
      this.newOrder = res.data.newOrder
      this.opendoor();
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
  position: absolute;
  top: 0;
  left: 0;
  background-color:rgba(242, 242, 242, 1);
  font-size: 25rpx;
  }
  .u9{
    width: 300px;
    height: 25px;
    font-size: 13px;
    margin: 0 auto;
    margin-top: 25%;
    color: #5E5D5D;
  }
  .u7-2{
    width: 85px;
    height:15px;
  }
  .u7-1{
    width: 85px;
    height: 15px;
  }
  .u7{
    width: 170px;
    height: 25px;
    display: flex;
    margin-left: 65px;
  }
  .u6-4{
    width: 120px;
    height: 15px;
    display: flex;
    margin: 4px 0 0 2px;
  }
  .u6-3{
    width :52px;
    height: 15px;
    color: #66CC33;
    text-decoration: underline;
  }
 .u6-2{
   color: #333333;
   width: 60px;
   height:15px;
 }
 .u6-1{
   width: 25px;
   height: 25px;
 }
 .u6{
   width: 310px;
   height: 30px;
   margin: 0 auto;
   font-size: 13px;
   display: flex;
   margin-top: 50px;
 }
 .u8{
   width: 321px;
   height: 42px;
   font-size: 20px;
   margin: 100px auto 0;
   border-radius: 5px;
   line-height: 42px;
   text-align: center;
   color: #fff;
   background:rgba(51, 204, 51, 1);
   margin-top: 50px;
 }
  .text-p3{
   width: 150px;
   height: 17px;
   margin-left: 60px;
   padding-left: 30px ;
 }
 .text-p2{
   width: 60px;
   height: 17px;
 }
.u5-1{
  width: 300px;
  height: 15px;
  font-size: 13px;
  margin-top: 10px;
  display: flex;
}
.u5{
}
.u4{
   width: 300px;
   height: 83px;
   font-size: 13px;
   margin: 0 auto;
}
  .u3{
     width: 100%;
     height: 49px; 
     font-size: 15px;
     text-align: center;
     margin: 0 auto;
  }
  .u2-img{
    height: 60px;
    /* border:1px solid red; */
  }
  .u2-p{
    height: 60px;
    margin-left: 10px;
    line-height: 60px;
    /* border:1px solid red; */
  }
  .u2{
    display: flex;
    margin: 15px auto 0; 
    width: 60%;
    height: 60px;
    text-align: center;
    font-size: 28px;
    color: #75D63B;
    /* border:1px solid red; */
  }

 .u1{
   width: 85%;
   height: 40%;
   display: grid;
   margin: 0 auto;
   -webkit-box-shadow: rgb(199, 199, 199) 0px 0px 10px;
   -moz-box-shadow: rgb(199, 199, 199) 0px 0px 10px;
   box-shadow: rgb(199, 199, 199) 0px 0px 10px;
   background:#fff;
   border-radius: 5px;
   margin-top: 35px;
 }

.text-p{
  color: #999999;

}
</style>
