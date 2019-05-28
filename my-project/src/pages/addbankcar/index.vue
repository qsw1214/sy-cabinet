<template>
    <div class="app">
        <div class="addbank_top">
            <div class="addbank_top_text">
                <div class="addbank_top_left">
                    姓名
                </div>
                 <div class="addbank_top_right">
                     <input type="text" placeholder="提现人姓名" :value="bankCard.userName" @input="$emit(bankCard.userName = $event.mp.detail.value)" class="text" placeholder-class="placeholder">
                </div>
            </div>
            <div class="addbank_top_text">
                <div class="addbank_top_left">
                    卡号
                </div>
                 <div class="addbank_top_right">
                     <input type="text" placeholder="提现储蓄卡号" :value="bankCard.cardNum" @input="$emit(bankCard.cardNum = $event.mp.detail.value)" class="text" placeholder-class="placeholder">
                </div>
            </div>
            <div class="addbank_top_text">
                <div class="addbank_top_left">
                    电话
                </div>
                 <div class="addbank_top_right">
                     <input type="text" placeholder="手机号码" :value="bankCard.userPhone" @input="$emit(bankCard.userPhone = $event.mp.detail.value)"  class="text" placeholder-class="placeholder">
                </div>
            </div>
            <div class="addbank_top_bank">
                <div class="addbank_top_left">
                    银行
                </div>
                 <div class="addbank_top_right">
                     <input type="text" placeholder="所属银行" :value="bankCard.ownBank" @input="$emit(bankCard.ownBank = $event.mp.detail.value)" class="text" placeholder-class="placeholder">
                </div>
            </div>
        </div>
        <div class="button" @click="add">
            添加
        </div>
    </div>
</template>
<script>
export default {
    data () {
      return {
          bankCard: {
                userId:  wx.getStorageSync('user').id,
                userName: '',
                userPhone: '',
                cardNum: '',
                ownBank: ''
          }
      }
    },
    methods: {
        add () {
            console.log(this.bankCard.userName)
        var newphone = /^(1)+\d{10}$/
        if(this.bankCard.userName.length >= 2){
            if (newphone.test(this.bankCard.userPhone)){
                if (this.bankCard.cardNum.length > 4 ){
                    if(this.bankCard.ownBank.length > 0){
                        this.$httpWX.post({
                            url: 'bankCard/add',
                            data: {
                                userId: this.bankCard.userId,
                                userName: this.bankCard.userName,
                                userPhone: this.bankCard.userPhone,
                                cardNum: this.bankCard.cardNum,
                                ownBank: this.bankCard.ownBank,
                                status: 0
                            }
                        }).then(res => {
                            if (res.data.addcode) {
                                wx.showToast({title: '添加成功', icon: 'success', duration: 2000})
                                this.bankCard = {
                                userId:  wx.getStorageSync('user').id,
                                userName: '',
                                userPhone: '',
                                cardNum: '',
                                ownBank: ''
                                }
                            } else {
                                wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
                            }
                        })
                    } else {
                        wx.showToast({title: '所属银行不能为空！', icon: 'none', duration: 2000})
                    }
                } else {
                    wx.showToast({title: '请输入正确的银行卡号！', icon: 'none', duration: 2000})
                }
            } else {
                wx.showToast({title: '请输入正确的手机号码！', icon: 'none', duration: 2000})
            }
        } else {
            wx.showToast({title: '请输入正确的姓名！', icon: 'none', duration: 2000})
        }
        }
    },
    onLoad (options) {
    }
}
</script>

<style>
.app{
    width: 100%;
    height: 100%;
    background-color: #f5f5f5;
    position: fixed;
}
.addbank_top{
    border-top:1px solid rgb(231, 231, 231);
    background-color: white;
    width: 100%;
    height: 200px;
    margin-top: 10px;
}
.addbank_top_text{
    font-size: 14px;
    height: 50px;
    line-height: 50px;
    width: 90%;
    margin-left: 5%;
    border-bottom: 1px solid rgb(231, 231, 231);
}
.addbank_top_bank{
    font-size: 14px;
    height: 50px;
    line-height: 50px;
    width: 90%;
    margin-left: 5%;
}
.addbank_top_left{
    float: left;
    width: 20%;
}
.addbank_top_right{
    float: left;
    width: 80%;
}
.text{
    height: 50px;
} 
.placeholder{
    color: rgb(208, 212, 208);
}
.button{
    width: 90%;
    margin-left: 5%;
    height: 50px;
    margin-top: 20px;
    font-size: 14px;
    color: rgb(189, 245, 189);
    background-color: rgba(51, 204, 51, 1);
    text-align: center;
    line-height: 50px;
    border-radius: 3px;
}
</style>

