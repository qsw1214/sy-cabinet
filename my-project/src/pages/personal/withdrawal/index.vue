<template>
  <div class="balance_body">
    <div class="balance_content">
      <div class="content_top">
        <div class="top_one">
          <div class="top_one_left">到账银行卡</div>
          <div class="top_one_rigth">
            <img src="../../../image/bank.png">
            <span @click="show_bank">{{bank_text}}</span>
          </div>
        </div>
        <div class="top_two">
          <div class="top_two_left"></div>
          <div class="top_two_rigth">2小时内到账</div>
        </div>
      </div>
      <div class="content_body">
        <div class="body_top">提现金额</div>
        <div class="body_foot">
          <div class="foot_left">￥</div>
          <div class="foot_rigth" @click="input_money">
            <div class="money" :style="{fontSize:moneyFont?'50px':'30px'}">{{money}}</div>
            <div class="cursor" v-show="cursor"></div>
          </div>
        </div>

        <div class="body_tips">
          当前余额{{displaybla}}元,
          <span class="whole" @click="whole">全部提现</span>
        </div>
      </div>
      <div class="content_foot">
        <button @click="cash">提 现</button>
      </div>
    </div>
    <div class="keyboard" v-show="showboard">
      <div @click="key_close" class="key_close">
        <div class="close"></div>
      </div>
      <div class="line">
        <div id="1" @click="numkey($event)" style="border-left:0px">1</div>
        <div id="2" @click="numkey($event)">2</div>
        <div id="3" @click="numkey($event)">3</div>
      </div>
      <div class="line">
        <div id="4" @click="numkey($event)" style="border-left:0px">4</div>
        <div id="5" @click="numkey($event)">5</div>
        <div id="6" @click="numkey($event)">6</div>
      </div>
      <div class="line">
        <div id="7" @click="numkey($event)" style="border-left:0px">7</div>
        <div id="8" @click="numkey($event)">8</div>
        <div id="9" @click="numkey($event)">9</div>
      </div>
      <div class="line">
        <div @click="numkey($event)" id="." style="border-left:0px">·</div>
        <div id="0" @click="numkey($event)">0</div>
        <div id="dele" @click="delect($event)">删除</div>
      </div>
    </div>
    <div class="bank_view" v-show="showbank">
      <div class="view" @click="close_view"></div>
      <div class="bank" :style="{height:bank_height + '%'}">
        <div class="bank_tips">
          <div class="text_one">选择到账银行卡</div>
          <div class="text_two">请留意各银行到账时间</div>
        </div>
        <div class="bank_list">
          <div
            class="conp"
            v-for="(i,index) in bank_list"
            :key="i.ownBank"
            :class="{ select_bank:index==selectClass}"
            @touchstart="touchStart($event,index)" @touchmove="touchMove($event,index)" @touchend="touchEnd($event,index)">
            <div class="display" :style="{right:change===index?Leftslide+'rpx':'0rpx'}" @click="select_bank(index)">{{i.ownBank}}({{i.cardNum}})</div>
            <div class="operation" :style="{width:change===index?Leftslide+'rpx':'0rpx'}" @click="delebank(index)">
              删除
            </div>
          </div>
          <div class="conp" @click="newbank"><div class="display">添加银行卡</div></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userPhone: '',
      balance: 0,
      displaybla: '',
      money: '',
      cursor: true,
      showboard: false,
      showbank: false,
      bank_height: '',
      timer: null,
      bank_text: '',
      selectClass: 0,
      bank_list: [{}],
      moneyFont: true,
      selectBank: {},
      startX: 0,
      Leftslide: 0,
      change: -1
    };
  },
  onShow() {
    this.balance = wx.getStorageSync('user').balance;
    this.getlist()
  },
  onLoad(options) {
  },
  beforeCreate() {
    console.log("组件：beforeCreate");
  },
  beforeMount() {
    console.log("组件：beforeMount");
    // this.bank_text = this.bank_list[0].ownBank
  },
  components: {},
  methods: {
    delebank (index) {
      console.log(this.bank_list[index].id)
      this.$httpWX
        .get({
          url: "bankCard/delete",
          data: {
            id: this.bank_list[index].id
          }
        })
        .then(response => {
          if (response.data.delecode) {
            this.bank_list.splice(index,1)
            if (this.selectClass === index) {
              this.selectClass = this.bank_list.length -1
              if (this.bank_list.length === 0) {
                this.selectBank = {}
                this.bank_text = '添加银行卡'
              } else {
                this.selectBank = this.bank_list[this.bank_list.length-1]
                this.bank_text = this.bank_list[this.bank_list.length-1].ownBank
              }
            } else {
              if ( index < this.selectClass){
                this.selectClass = this.selectClass-1
              }
            }
          } else {
            wx.showToast({title: '删除失败，服务器繁忙', icon: 'none', duration: 2000})
          }
        }).catch(() => {
          this.bank_list = [];
          this.total = 0;
        });
      this.change = -1
    },
    touchStart (e,index) {
      this.change = index
      this.startX = e.mp.changedTouches[0].clientX
    },
    touchMove (e,index) {
      if ((this.startX -e.mp.changedTouches[0].clientX) > 0) {//判断是不是左滑
      if (this.Leftslide = this.startX -e.mp.changedTouches[0].clientX <= 250) {
        this.Leftslide = this.startX -e.mp.changedTouches[0].clientX
      } else {
        this.Leftslide = 250
      }
      } else {
        this.Leftslide = 0
      }
    },
    touchEnd (e, index) {
      if (this.Leftslide !== 250) {
         this.Leftslide = 0
      }
    },
    newbank () {
      wx.navigateTo({ url: '../../addbankcar/main'})
    },
    getlist(){
      var balancetype = this.balance.toString();
      var result = balancetype.indexOf("."); //判断有没有小数点
      if (result !== -1) {
        //如果有小数点
        var newbal = balancetype.split(".");
        if (newbal[0].length < 4) {
          this.displaybla = balancetype;
        } else if (newbal[0].length >= 4) {
          this.displaybla =
            newbal[0].substring(0, newbal[0].length - 3) +
            "." +
            newbal[0].substring(newbal[0].length - 3, newbal[0].length) +
            "." +
            newbal[1];
        }
      } else {
        //如果没小数点
        if (balancetype.length < 4) {
          this.displaybla = balancetype;
        } else {
          this.displaybla =
            balancetype.substring(0, balancetype.length - 3) +
            "." +
            balancetype.substring(balancetype.length - 3, balancetype.length)
        }
      }
      this.$httpWX
        .get({
          url: "bankCard/querybank",
          data: {
            userId: wx.getStorageSync("user").id
          }
        })
        .then(response => {
          this.bank_list = response.data.bank
          if(response.data.banktype){
            for (var i = 0; i < this.bank_list.length; i++) {
              if (this.bank_list[i].status === 0) {
                this.selectClass = i
                this.selectBank = this.bank_list[i]
                this.bank_text = this.bank_list[i].ownBank
              }
            }
          } else {
            this.bank_text = '添加银行卡'
          }
        })
        .catch(() => {
          this.bank_list = []
          this.total = 0
        })
    },
    cash(){
      var money = parseFloat(this.money)
      if( money > 0 && money <= this.balance) {
        if(this.selectBank.id > 0){
          this.$httpWX.post({
            url: "cashord/add",
            data: {
              money: money,
              userId: wx.getStorageSync("user").id,
              bankId: this.selectBank.id,
              status:0,
              delete:0,
              adminUserId:0
            }
          }).then(response => {
            if(response.data.insert){
              this.balance = (wx.getStorageSync('user').balance - money)
              this.getlist()
              this.money = 0
              wx.showToast({title: '提现成功', icon: 'success', duration: 2000})
            }else{
              wx.showToast({title: '提现失败', icon: 'none', duration: 2000})   
            }
          })
        } else {
          wx.showToast({title: '请选择提现银行卡', icon: 'none', duration: 2000})
        }
      }else{
        wx.showToast({title: '请输入提现金额', icon: 'none', duration: 2000})
      }
      
    },
    select_bank(index) {
      this.bank_text = this.bank_list[index].ownBank;
      this.selectClass = index;
      this.selectBank =  this.bank_list[index]
      var i = 40;
      this.timer = setInterval(() => {
        if (i > 0) {
          this.bank_height = i;
          i--;
        } else {
          clearInterval(this.timer);
          this.timer = null;
          this.showbank = false;
        }
      }, 1);
      console.log(this.bank_list[index].ownBank);
      this.Leftslide = 0
      this.change = -1
    },
    close_view() {
      this.Leftslide = 0
      this.change = -1
      var i = 40;
      this.timer = setInterval(() => {
        if (i > 0) {
          this.bank_height = i;
          i--;
        } else {
          clearInterval(this.timer);
          this.timer = null;
          this.showbank = false;
        }
      }, 1);
    },
    show_bank() {
      this.showbank = true;
      this.showboard = false;
      var i = 0;
      this.timer = setInterval(() => {
        if (i < 40) {
          this.bank_height = i;
          i++;
        } else {
          clearInterval(this.timer);
          this.timer = null;
        }
      }, 1);
    },
    whole() {
      this.money = this.balance;
      if (this.money.toString().length > 5) {
        this.moneyFont = false;
      } else {
        this.moneyFont = true;
      }
    },
    input_money() {
      this.interval = setInterval(() => {
        this.cursor = !this.cursor;
      }, 500);
      this.showboard = true;
      this.showbank = false;
    },
    key_close() {
      this.showboard = false;
    },
    numkey(e) {
      var money = this.money + e.target.id;
      var floatnum = parseFloat(this.money + e.target.id);
      var result = money.toString().indexOf(".");
      if (result !== -1) {
        if (money.split(".").length > 2) {
          this.money = this.money;
        } else {
          if (money > this.balance) {
            this.money = this.balance;
          } else {
            if (money.split(".")[0].length === 0) {
              this.money = "0" + money;
            } else {
              if (money.split(".")[1].length > 2) {
                this.money =
                  money.split(".")[0] +
                  "." +
                  money.split(".")[1].substring(0, 2);
              } else {
                this.money = money;
              }
            }
          }
        }
      } else {
        if (floatnum > this.balance) {
          this.money = this.balance;
        } else {
          this.money = money;
        }
      }
      if (this.money.toString().length > 5) {
        this.moneyFont = false;
      } else {
        this.moneyFont = true;
      }
    },
    delect(e) {
      var money = this.money.toString();
      this.money = money.substring(0, money.length - 1);
      if (this.money.length > 5) {
        this.moneyFont = false;
      } else {
        console.log(this.money.toString().length);
        this.moneyFont = true;
      }
    }
  },
  created() {
    // let app = getApp()
  }
};
</script>

<style scoped>
.text_two {
  width: 100%;
  height: 40%;
  font-size: 12px;
  padding-left: 20px;
  display: flex;
  align-items: Center;
  color: #cccccc;
}
.text_one {
  width: 100%;
  height: 60%;
  font-size: 16px;
  padding-left: 20px;
  display: flex;
  align-items: Center;
}
.select_bank {
  background-color:#47C489;
  color: rgb(189, 245, 189);
}
.bank_tips {
  width: 100%;
  height: 20%;
  border-bottom: 1px solid #cccccc;
}
.bank_list {
  width: 100%;
  height: 80%;
  text-align: center;
  overflow-y: auto;
}
.bank_list::-webkit-scrollbar {
  display: none;
}
.conp {
  position: relative;
  font-size: 16px;
  width: 100%;
  height: 60px;
  line-height: 40px;
  border-bottom: 1px solid #cccccc;
  display: flex;
  align-items: Center;
}
.operation{
  height: 60px;
  line-height: 60px;
  text-align: center;
  position: relative;
  top: 0;
  right: 0;
  margin: 0px;
  overflow: hidden;
  background-color:#F44D4D;
  color: #FADBDB;
}
.display{
  width: 90%;
  height: 60px;
  line-height: 60px;
  margin-left: 5%;
  text-align: left;
  position: relative;
}
.conp:active {
  background-color: rgb(249, 249, 249);
}
.view {
  height: 60%;
  width: 100%;
}
.bank {
  position: fixed;
  bottom: 0px;
  width: 100%;
  border-radius: 4px 4px 0px 0px;
  background-color: #fff;
}
.bank_view {
  position: fixed;
  width: 100%;
  height: 100%;
  z-index: 1;
  background-color: rgba(92, 90, 90, 0.548);
}
.close {
  width: 30%;
  height: 0;
  border-width: 10px 10px 0;
  border-style: solid;
  border-color: #cccccc transparent transparent; /*灰 透明 透明 */
  margin: 0px auto;
  position: relative;
}
.money {
  max-width: 80%;
  overflow-x: auto;
}
.money::-webkit-scrollbar {
  display: none;
}
.key_close {
  height: 12%;
  width: 100%;
  border-top: 1px solid #cccccc;
}
.line {
  width: 100%;
  height: 22%;
  border-top: 1px solid #cccccc;
}
.line div {
  width: 32.7%;
  height: 100%;
  border-left: 1px solid #cccccc;
  border-bottom: 1px solid #cccccc;
  display: flex;
  float: left;
  justify-content: center;
  align-items: center;
}
.line div:active {
  background-color: rgb(240, 238, 238);
}
.keyboard {
  position: fixed;
  bottom: 0px;
  width: 100%;
  height: 300px;
  z-index: 1;
  background-color: #fff;
}
.content_foot {
  width: 100%;
  height: 15%;
}
.content_foot button {
  margin-left: 5%;
  width: 90%;
  background: linear-gradient(to right,#47C489,#229251);
  height: 50px;
  line-height: 50px;
  color: rgb(189, 245, 189);
  font-size: 16px;
  text-align: middle;
}
.content_foot button:active {
  top: 2px;
  background-color: linear-gradient(to right,#47C489,#229251);
}
.whole {
  color: rgb(51, 51, 255);
}
.body_tips {
  position: absolute;
  margin-top: 8%;
  width: 100%;
  font-size: 16px;
  text-align: center;
}
.body_foot {
  width: 80%;
  margin-left: 10%;
  font-weight: bold;
  margin-top: 10px;
  height: 50%;
  border-bottom: 1px solid #cccccc;
}
.foot_left {
  width: 20%;
  float: left;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: Center;
}
.foot_rigth {
  float: left;
  width: 80%;
  height: 100%;
  display: flex;
  align-items: Center;
}
.cursor {
  width: 2px;
  height: 40%;
  background-color: black;
}
.body_top {
  margin-top: 8%;
  width: 80%;
  height: 10%;
  margin-left: 10%;
  font-size: 16px;
}
.content_body {
  width: 100%;
  height: 50%;
}
.balance_body {
  position: absolute;
  width: 100%;
  height: 100%;
  background-color: rgb(242, 242, 242);
}
.balance_content {
  position: absolute;
  left: 5%;
  top: 2%;
  width: 90%;
  height: 60%;
  background-color: rgb(255, 255, 255);
}
.content_top {
  width: 100%;
  height: 25%;
  border-bottom: 1px solid rgb(204, 204, 204);
}
.top_one_left {
  width: 40%;
  height: 100%;
  float: left;
  display: flex;
  justify-content: center;
  align-items: Center;
  font-size: 16px;
}
.top_one_rigth {
  width: 60%;
  height: 100%;
  float: left;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
}
.top_one {
  width: 100%;
  height: 60%;
  font-size: 16px;
}
.top_one_rigth img {
  width: 25px;
  height: 25px;
  margin-right: 10px;
  overflow: hidden;
}
.top_one_rigth span {
  color: rgb(51, 51, 255);
}
.top_two {
  width: 100%;
  height: 40%;
  font-size: 16px;
  color: #999999;
}
.top_two_left {
  width: 40%;
  height: 100%;
  float: left;
}
.top_two_rigth {
  text-align: center;
  float: left;
  width: 58%;
  height: 100%;
}
</style>

