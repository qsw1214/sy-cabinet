<template>
<div class="fo" :style="{heigth : heigth}">
  <div class="u2">
    <div class="u2-1">
      <div class="u2-2">
        <div class="u2-2-span1" :class="{'selected':tab === 0,'testTitle':true}" @click="changTab(0)">当前订单</div>
        <div class="u2-2-span1" :class="{'selected':tab === 3,'testTitle':true}" @click="changTab(3)">未完成订单</div>
        <div class="u2-2-span2" :class="{'selected':tab === 1,'testTitle':true}" @click="changTab(1)">历史订单</div>
      </div>
    </div>
  </div>
  <div class="Order" >
    <div class="HistoricalOrder">
      <div class="u1" v-for="(order,index) in orderList" @touchstart="touchStart($event,index) " 
        @touchmove="touchMove($event,index)" @touchend="touchEnd($event,index)" :key="index" >
        <div class="u1-1" @click="click(order.id)" 
          :style="{right:change===index?Leftslide+'rpx':'0rpx'}">
          <div class="boxName">包裹{{index + 1}}</div>
          <div class="storeTime">存放时间:&nbsp;&nbsp;{{order.addTime}}</div>
          <div class="boxSort">柜子:&nbsp;&nbsp;{{order.subCabNum}}&nbsp;({{order.stanName}})</div>
        </div> 
        <div class="delect" @click="delect(index)"
          :style="{width:change===index?Leftslide+'rpx':'0rpx'}">删除</div>
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
      tab: 0,
      listLoading: true,
      orderList: [],
      userId: '',
      delWidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.2),
      heigth: (wx.getSystemInfoSync().windowHeigth * (750 / wx.getSystemInfoSync().windowHeigth)),
      startX: '',   // 滑动开始位置
      endX: '',     // 滑动结束位置
      moveX: '',    // 滑动距离
      Leftslide: 0,
      change: -1,
      order: {},     // 订单信息
      page: 1,
      size: 10,
      allTotal: '',
      totalPage: '',
    }
  },
  components: {
    card
  },
  methods: {
    // 切换
    changTab (index) {
      this.tab = index
      this.$httpWX.get({
        url: 'usarec/selectRecord',
        data: {
          'userId': wx.getStorageSync('user').id,
          'status': index,
          'page': 1,
          'size': this.size
        }
      }).then(response => {
        this.orderList = response.data.items
        this.total = response.data.total
        this.allTotal = response.data.allTotal
        this.totalPage = this.allTotal/this.size
      }).catch(() => {
        this.orderList = []
        this.total = 0
      })
    },
    // 滑动开始
    touchStart (e,index) {
      if(this.tab === 1){
        this.change = index
        this.startX = e.mp.changedTouches[0].clientX
      }
    },
    // 滑动中
    touchMove (e,index) {
      if ((this.startX -e.mp.changedTouches[0].clientX) > 0) {//判断是不是左滑
      if (this.Leftslide = this.startX -e.mp.changedTouches[0].clientX <= 150) {
        this.Leftslide = this.startX -e.mp.changedTouches[0].clientX
      } else {
        this.Leftslide = 150
      }
      } else {
        this.Leftslide = 0
      }
    },
    // 滑动结束
    touchEnd (e, index) {
      if (this.Leftslide !== 150) {
         this.Leftslide = 0
      }
    },
    // 跳转页面(带参)
    click (row) {
      console.log(row)
      const url = '../../currentOrder/main?id=' + row
      wx.redirectTo({ url })
    },
    // 删除
    delect(index){
      this.order = this.orderList[index];
      this.orderList.splice(index,1);
      console.log(this.order)
      this.$httpWX.get({
        url: 'usarec/delete',
        data:{
          'id': this.order.id
        }
      }).then(res =>{
        if(res.errno === 0){
          wx.showToast({title: '删除成功', icon: 'success', duration: 1000})
        }else{
          wx.showToast({title: '删除失败', icon: 'none', duration: 1000})
        }
      })
    },
    // 查询订单信息
    selectRecord () {
      this.$httpWX.get({
        url: 'usarec/selectRecord',
        data: {
          'userId': wx.getStorageSync('user').id,
          'status': this.tab,
          'page': this.page,
          'size': this.size
        }
      }).then(res => {
        if(res.errno === 0){
          this.orderList = this.orderList.concat(res.data.items)
        }
      })
    }
  },
  // 查询订单详情
  onLoad (option) {
    let userId = option.userId
    this.$httpWX.get({
      url: 'usarec/selectRecord',
      data: {
        'userId': wx.getStorageSync('user').id,
        'status': '0',
        'page': this.page,
        'size': this.size
      }
    }).then(response => {
      this.orderList = response.data.items
      this.total = response.data.total
      this.allTotal = response.data.allTotal
      this.totalPage = this.allTotal / this.size
    }).catch(() => {
      this.orderList = []
      this.total = 0
    })
  },
  // 上拉加载
  onReachBottom: function () {
    var that = this;
    if(that.totalPage > that.page){
      that.page = that.page + 1;
      that.selectRecord();
    }else{
      wx.showToast({title: '没有更多订单信息',icon: 'none',duration: 1000})
    }
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
  background-color:#F5F5F5;
  font-size: 25rpx;
  border: 1px, solid, #00cc66
  }
  .u1{
    width: 100%;
    height: 50px;
    /* line-height:45px; */
    font-size: 24rpx;
    /* position: relative; */
    color: #888; 
    border: 1px dotted #9e9e9e ;
    border-radius: 8px;
    /* margin: auto; */
    /* display: flex;
    flex-direction: column; */
    /* z-index:2; */
    /* -webkit-box-shadow: rgb(236, 236, 236) 0px 0px 10px;
    -moz-box-shadow: rgb(233, 230, 230) 0px 0px 10px;
    box-shadow: rgb(236, 234, 234) 0px 0px 10px; */
    background: #fff;
  }
.u1-1{
  width: 100%;
  height: 40px;
  display: flex;
  position: absolute;
  color: #888;
}
  .delect{
    position: absolute;
    top: 0;
    right: 0;
    line-height: 50px;
    overflow: hidden;
    margin: auto;
    background-color:#F44D4D;
    color: #FADBDB;
    text-align: center;
  }
  .u2{
    width: 375px;
    height: 41px;
    background: #fff;
    font-size: 16px;
  }
  .u2-2{
    position: relative;
    display: flex;
  }
  .u2-2-span1{
    width: 188px;
    height: 41px;
    line-height: 41px;
    text-align: center;
    color:#999999;
  }
  .u2-2-span2{
    width: 187px;
    height: 41px;
    line-height: 41px;
    text-align: center;
    color:#999999;
  }
.selected{
    color:#333333;
    border-bottom: 1px solid rgba(29, 179, 17, 1);
  }
._p7hthq {
    position: relative ;
    background: rgb(123, 124, 128) none repeat scroll 0% 0% ;       
    height: 1px ;     
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
.sy-box-number{
   font-size: 50rpx;
   font-weight: bold;
   }
.sy-details-btn{ 
    width: 140px;
    height: 41px;
    border-radius: 5px;
    color: #FFFFFF;
    font-size: 20px;
    background:rgba(51, 204, 51, 1);
    line-height: 40px;
    text-align: center;
    margin: 60px auto;
}
 .sy-content{
  width: 100%;
  height:80%;
  position: fixed;
  top: 0;
  left: 0;
  margin-top: 20%;
  background-color:rgba(242, 242, 242, 1);
  font-size: 25rpx;
} 
.orderList{
  width: 94%;
  display: flex;
  flex-wrap: wrap;
  background: #fff;
  border-radius: 8rpx;
  margin: 30rpx auto;
}
.orderList-main{
  padding: 4%;
  height: 15%;
  letter-spacing: 2rpx;
  color:#666;
  font-size: 28rpx;
}
.boxName{
  width: 100%;
  height: 30%;
  color: #00cc66;
  font-size: 36rpx;
}
.storeTime span{
  letter-spacing: 0rpx;
}
.storeTime{
  width: 100%;
  padding: 8rpx 0;
}
.reminder{
  color: #ee4031;
}
.index-sy{
  width: 100%;
  height: 100%;
  position: fixed;
  /* top: 0; */
  left: 0;
  background-color:#F5F5F5;
  overflow-y:scroll;
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.index-sy::-webkit-scrollbar{
  width: 2px;
  height: 2px;
}
</style>
