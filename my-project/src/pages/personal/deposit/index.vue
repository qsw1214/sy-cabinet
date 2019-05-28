<template>
  <div class="all">
      <div class="deposit" v-for="(item,index) in depositList" :key="index">
        <div class="deposit-info">
          <div class="money-img" >
            <img class="money-image" src="/static/images/deposit_big.svg" :style="{width: imgwidth + 10 + 'rpx', height: imgwidth + 10 +  'rpx'}"/>
          </div>
          <div class="depo-center">
            <div class="depo-title1">
              <div class="center-box">
                <div class="depo-text-a">{{item.mainRegion}}</div>
                <div class="depo-text-c">{{item.stanName}}</div>
                <div class="depo-text-b">{{item.subCabNum}}</div>
              </div>
            </div>
            <div class="depo-title2">
              <div class="center-box">
                <div class="deposit-time">
                  <span v-if="item.depoStatus === 1" >{{item.addTime}}</span>
                  <span v-else-if="item.depoStatus === 2" >{{item.returnTime}}</span>
                </div>
              </div>
            </div>

          </div>
          <div class="depo-right">
            <div class="deposit-money">¥{{item.depoMoney}}</div>
            <div class="depo-status" v-if="item.depoStatus === 1" 
              :style="{backgroundColor:'#12C91D'}">使用中</div>
            <div class="depo-status" v-else-if="item.depoStatus === 2" 
              :style="{backgroundColor:'#C63C3C'}">已退还</div>
          </div>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  filters: {
    formatStatus(status) {
      if (status === 0) {
        return '使用中'
      } else {
        return '已退还'
      }
    }
  },
  data () {
    return {
      imgwidth: (wx.getSystemInfoSync().windowWidth * (750 / wx.getSystemInfoSync().windowWidth) * 0.15),
      depositList: [],
      subInfo :{},    // 柜子信息
      mainInfo :{},   // 规格信息
      stanInfo :{},   // 柜组信息
      page: 1,
      size: 7,
      allTotal: '',
      totalPage: '',
    }
  },
  methods: {
    // 查询押金信息
    queryDepo() {
      this.$httpWX.post({
        url: 'deposit/list',
        data: {
          depoUserId: wx.getStorageSync('user').id,
          page: this.page,
          size: this.size
        }
      }).then(res => {
        this.depositList = this.depositList.concat(res.data.items)
      })
    }
  },
  onShow () {
    wx.showLoading({title: '加载中...'});
    this.$httpWX.post({
      url: 'deposit/list',
      data: {
        depoUserId: wx.getStorageSync('user').id,
        page: 1,
        size: this.size
      }
    }).then(res => {
      this.depositList = res.data.items
      this.allTotal = res.data.allTotal
      this.totalPage = this.allTotal / this.size
    })
  },
  // 上拉加载
  onReachBottom: function () {
    var that = this;
    if(that.totalPage > that.page){
      wx.showLoading({ title: '加载中', })
      setTimeout(function () { 
        wx.hideLoading()
        that.page = that.page + 1;
        that.queryDepo();
      }, 500)
    }else{
      wx.showToast({title: '没有更多押金信息',icon: 'none',duration: 1000})
    }
  },
}
</script>

<style>
  .all{
    width: 100%;
    background-color:#f5f5f5;
    /* position: fixed;
    top: 0;
    left: 0; */
    overflow-y:scroll;
    
  }
 .all::-webkit-scrollbar{
    width: 0px;
    height: 0px;
    -webkit-overflow-scrolling:touch;
}
  .deposit{
    width: 98%;
    margin: 30rpx auto;
  }
  .deposit-info{
    width: 98%;
    display: flex;
    flex-wrap: wrap;
    margin: 0 auto ;
    border-radius: 3px;
    background-color:#fff;
    padding: 4% 0;
  }
  .money-img{
    width: 23%;
    display: flex;
    align-items: center;
    justify-content:center;
  }
  .deposit-info-right{
    width: 72%;
    display: flex;
    justify-content: space-between;
  }
  .depo-center{
    
    margin-left: -1%;
  }
  .depo-title1{
    width: 100%;
    display: flex;
    align-items: center;
    justify-content:center;
  }
  .depo-title2{
    width: 100%;
    display: flex;
    align-items: center;
    /* justify-content:center; */
  }
  .center-box{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    /* margin: auto 0; */
  }
  .depo-text-a{
    width: 90%;
    color: #2b2b2b;
    font-size: 34rpx;
    margin:0 0 3% 5%;
  }
  .depo-text-b{
    color: #2b2b2b;
    font-size: 34rpx;
    transform: translateY(3px);
    display: flex;
  }
  .depo-text-c{
    margin-left:5%;
    color: #2b2b2b;
    font-size: 34rpx;
    transform: translateY(3px);
  }
  .deposit-time{
    font-size: 24rpx;
    color: #888;
    margin: 20rpx 0 0 5%;
  }
  .depo-right{
    width: 160rpx;
    display: grid;
    justify-content: right;
  }
  .depo-status{
    width: 120rpx;
    height: 50rpx;
    text-align: center;
    font-size: 24rpx;
    color: white;
    line-height: 50rpx;
    border-radius: 6rpx;
    /* border: 1px solid #0d0e0d; */
    margin-left: 40rpx;
  }
  .deposit-money{
    font-size: 68rpx;
    /* margin: auto 0; */
    color: #ee4031;
    text-align: right;
    line-height: 60rpx;
  }
  .deposit-money span{
    font-size: 34rpx;
    padding-right: 6rpx;
  }

</style>
