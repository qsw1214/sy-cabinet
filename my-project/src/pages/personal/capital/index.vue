<template>
<div class="sy_content">
    <div class="selectdate" v-show="maskshow">
      <div class="yeardate">
        <div class="date_div pressup" @click="upyear"></div>
        <div class="date_div"> {{Textyear}} </div>
        <div class="date_div pressnext" @click="nextyear"></div>
        <div class="date_div">
          <div @click="selectdate" class="ascertain">确 定</div>
        </div>
      </div>
      <div class="monthdate">
        <div class="classmonth" v-for="(item,index) in monthitems" :key="index" @click="monthclick(item)" :style="{backgroundColor: item > month && Textyear === year ?'rgba(240,240,240,1)':''}">
          {{item}}
        </div>
      </div>
    </div>
    <div class="content_title">
      <div class="datetime" @click="selectdate">
      {{datetime}}
      <img src="../../../image/open.svg" v-if="maskshow===true"/>
      <img src="../../../image/shut.svg" v-if="maskshow === false"/>
      </div>
      <div class="outlay">支出￥：<span> {{outlayText}}</span></div>
    </div>
    <div class="content_body">
      <div class="body_item" v-for="(item,index) in items" :key="index">
        <div class="item_icon" >
          <img src="../../../image/capital.svg" :style="{width: imgwidth + 'rpx', height: imgwidth +  'rpx'}"/>
        </div>
        <div class="item_left">
          <div calss="item_left_title" style="font-size: 16px;">
             {{ item.capTitle }}
          </div>
          <div calss="item_left_time" style="font-size: 12px;color:grey;">
             {{ item.capDatetime  }}
          </div>
        </div>
        <div class="item_right" :style="{color:(item.capMoney > 0?'#E8541E':'#2B2b2b')}">
          {{ item.capMoney + '.00' }}
        </div>
      </div>
    </div>
</div>
</template>

<script>
import card from '@/components/card'
var date=new Date;

export default {
  data () {
    return {
      items: [],
      outlayText: 0.00,
      maskshow: false,
      Textyear: date.getFullYear(),
      monthitems:[1,2,3,4,5,6,7,8,9,10,11,12],
      Textmonth:  date.getMonth()+1,
      datetime: '本月',
      year: date.getFullYear(),
      month: date.getMonth()+1,
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.09),
      height: wx.getSystemInfoSync().windowHeight,
      page: 1,
      size: 11,
      allTotal: '',
      totalPage: '',
    }
  },
  components: {
    card
  },
  methods: {
    selectdate(){
      this.maskshow = !this.maskshow
      if (!this.maskshow) {
        this.outlayText = 0.00
        this.getcap()
      }
    },
    monthclick(val){
      if(this.Textyear != this.year) {
        this.Textmonth = val
        this.datetime = (this.Textyear-2000) +'-'+ val
      }else{
        if(val != this.month){
          if(val <　this.month){
             this.Textmonth = val
             this.datetime = this.Textmonth +'月'
          }
        }else{
          this.Textmonth = val
           this.datetime = '本月'
        }
      }
    },
    upyear(){
      if (this.Textyear > (this.year-10)){
        this.Textyear = this.Textyear - 1
      }
      if(this.Textyear != this.year) {
        this.datetime = (this.Textyear-2000)  +'-'+ this.Textmonth
      }else{
        if(this.Textmonth != this.month){
          if( this.Textmonth > this.month){
              this.Textmonth = this.month
              this.datetime = '本月'
          } else {
            this.datetime = this.Textmonth +'月'
          }
        }else{
           this.datetime ='本月'
        }
      }
    },
    nextyear(){
      if (this.Textyear < this.year){
        this.Textyear = this.Textyear + 1
      }
     if(this.Textyear != this.year) {
        this.datetime = (this.Textyear-2000)  +'-'+ this.Textmonth
      }else{
        if(this.Textmonth != this.month){
          if( this.Textmonth > this.month){
              this.Textmonth = this.month
              this.datetime = '本月'
          } else {
            this.datetime = this.Textmonth +'月'
          }
        }else{
           this.datetime ='本月'
        }
      }
    },
    getcap(){
      wx.showLoading({title: '加载中...'})
        this.$httpWX.get({
          url: 'capital/getcap',
          data: {
            userId: wx.getStorageSync('user').id,
            year: this.Textyear,
            month: this.Textmonth,
            page: this.page,
            size: this.size,
          }
        }).then(res => {
          if(res.errno === 0){
            wx.hideLoading()
            // this.items = res.data.items
            this.allTotal = res.data.allTotal
            this.totalPage = this.allTotal / this.size
            this.items = this.items.concat(res.data.items)
            for(var v in this.items ){
              var Money = parseFloat(this.items[v].capMoney)
              if( Money < 0 ){
                this.outlayText += Math.abs(Money)
              } else {
                this.outlayText -= Math.abs(Money)
              }
            }
          } else {
              
          }   
        })
    }
  },
  computed:{
  },
  onLoad() {
    this.getcap()
    this.outlayText = 0.00
  },
  created () {
    // let app = getApp()
  },
  // 上拉加载
  onReachBottom: function () {
    var that = this;
    if(that.totalPage > that.page){
      wx.showLoading({ title: '加载中', })
      setTimeout(function () { 
        wx.hideLoading()
        that.page = that.page + 1;
        that.getcap();
      }, 500)
    }else{
      wx.showToast({title: '没有更多账单信息',icon: 'none',duration: 1000})
    }
  },
}
</script>

<style>
.yeardate{
  margin-top: 50rpx;
  width: 100%;
  height: 50rpx;
  text-align: center;
  line-height: 50rpx;
}
.yeardate .date_div{
  width: 25%;
  height: 50rpx;
  float: left;
}
.pressup{
  background:url(../../../image/up.svg) no-repeat center top;
  background-size:50rpx;
}
.pressup:active{
   background:url(../../../image/up2.svg) no-repeat center top;
  background-size:50rpx;
}
.pressnext{
  background:url(../../../image/next.svg) no-repeat center top;
  background-size:50rpx;
}
.pressnext:active{
   background:url(../../../image/next2.svg) no-repeat center top;
  background-size:50rpx;
}
.ascertain{
  width: 60%;
  border-radius: 5px;
  background-color: rgba(51, 204, 51, 1);
  color: white;
}
.classmonth{
  width: 33.3333%;
  height: 60px;
  border-top: 1rpx solid #cccccc;
  float: left;
  text-align: center;
  line-height: 60px;
}
.classmonth:active{
  background-color: #f0f0f0;
}
.monthdate{
  margin-top: 50rpx;
  width: 100%;
  height: 304rpx;
}
.selectdate{
  z-index: 1;
  top: 80rpx;
  position: fixed;
  left: 0;
  width: 100%;
  background-color: white;
  border-bottom: 1px solid  #cccccc;
}
.content_title{
  margin: 0;
  width: 100%;
  height: 80rpx;
  border-top: 1rpx solid #cccccc;
  color: #2B2b2b;
}
.body_item{
  width: 100%;
  margin-top: 5rpx;
  height: 120rpx;
  border-bottom: 1px solid #cccccc;
}
.item_icon{
  width: 15%;
  float: left;
  height: 100%;
  justify-content:center;
  align-items:center;
  display:flex;
}
.item_icon img{
  width: 80rpx;
  height: 80rpx;
}
.item_left{
  width: 50%;
  float: left;
  margin-top: 20rpx;
  height: 100rpx;
}
.item_left div{
  height: 40rpx;
  margin-top: 1px;
  line-height: 40rpx;
  /* border: 1px solid red; */
}
.item_right{
  width: 30%;
  float: left;
  height: 100%;
  line-height: 100rpx;
  text-align: right;
  font-size: 18px;
  font-weight: bold;
  font-family: Arial,Helvetica,sans-serif;
}
.content_body{
  width: 100%;
  padding-bottom: 10rpx;
  background-color: white;
  /* overflow-y: scroll; */
}
.content_body::-webkit-scrollbar{
  width: 0px;     /*高宽分别对应横竖滚动条的尺寸*/
  height:10px;
}
.datetime{
  text-align: right;
  line-height: 60rpx;
  height: 60rpx;
  width: 130rpx;
  float: left;
  margin-left: 20rpx;
  margin-top: 10rpx;
  border-radius: 25px;
  background-color:white;
}
.datetime img{
  float: right;
  width: 40rpx;
  height: 40rpx;
  margin: 10rpx;
}
.outlay{
  height: 80rpx;
  float: right;
  line-height: 80rpx;
  margin-right: 20px;
}
.sy_content{
  width: 100%;
  height: 100%;
  /* overflow-y:scroll; */
  background-color:#f5f5f5;
  font-size: 25rpx;
}
</style>