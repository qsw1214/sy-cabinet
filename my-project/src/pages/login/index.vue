<template>
  <div>
       <button type="primary" open-type="getUserInfo" class="wx-login-btn"   @getuserinfo="wxLogin" >微信直接登录</button>
        <div class="phone_body" v-show="phonebody">
            <div class="body_div">
                <div class="div_head_tips">
                    请绑定手机号码，并输入四位数密码，以便于密码取包与到期提醒！
                </div>
                <div class="div_head_tips">
                    <button open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" class="obtain" v-show="condition">绑定手机号</button>
                    <div>{{phone}}</div>
                </div>
                <div class="div_head_tips">
                    <div>请输入四位数密码并牢记 </div> 
                    <div  class="pass" @click="keylayer($event)" style="margin-left: 0%;">{{password_one}}</div>
                    <div  class="pass" @click="keylayer($event)" >{{password_two}}</div>
                    <div  class="pass" @click="keylayer($event)" >{{password_tree}}</div>
                    <div  class="pass" @click="keylayer($event)" >{{password_frow}}</div>
                </div>
                <div class="binding" @click="define">确定</div>
            </div>
            <div class="keyboard" v-show='showboard'>
                <div class="line">
                    <div id="1" @click="numkey($event)" style="border-left:0px">1</div>
                    <div id="2" @click="numkey($event)">2</div>
                    <div id="3" @click="numkey($event)">3</div></div>
                <div class="line">
                    <div id="4" @click="numkey($event)"  style="border-left:0px">4</div>
                    <div id="5" @click="numkey($event)">5</div>
                    <div id="6" @click="numkey($event)">6</div></div>
                <div class="line">
                    <div id="7" @click="numkey($event)"  style="border-left:0px">7</div>
                    <div id="8" @click="numkey($event)">8</div>
                    <div id="9" @click="numkey($event)">9</div></div>
                <div class="line">
                    <div @click="keylayercloer" id="ok"  style="border-left:0px">确认</div>
                    <div id="0" @click="numkey($event)">0</div>
                    <div id="dele" @click="delect($event)">删除</div>
                </div>
            </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request.js'
export default {
    data () {
        return {
            userInfo: {},
            phone: '',
            phonebody: false,
            showboard: false,
            password_one: '',
            password_two: '',
            password_tree: '',
            password_frow: '',
            password: '',
            condition: true
        }
    },
    created () {
    },
    methods: {
        define () {
            if (this.phone === '') {
                wx.showToast({title: '请填写正确的手机号码', icon: 'none', duration: 2000})
            } else {
                if (this.password.length !== 4) {
                    wx.showToast({title: '请输入四位数密码', icon: 'none', duration: 2000})
                } else {
                    wx.showLoading({title: '加载中...'})
                    this.addUserInfo()
                }
            }
        },
        // 添加用户手机号密码
        addUserInfo(){
            this.$httpWX.post({
            url: 'user/updateUser',
            data: {
                id: wx.getStorageSync('user').id,
                password: this.password,
                mobile: this.phone
            }
            }).then(res => {
            if(res.errno === 0){
                this.$httpWX.get({
                url: 'usarec/getuser',
                data: {
                    userId: wx.getStorageSync('user').id,
                }
                }).then(res => {
                    wx.hideLoading()
                    wx.setStorageSync('user',res.data.user)
                    this.user = wx.getStorageSync('user')
                    this.phonebody = false
                })
            }else{
                wx.showToast({title: '服务器繁忙！', icon: 'none', duration: 2000})
                this.clickup = true
                wx.hideLoading()
            }
            })
        },
        keylayer (e) {
            this.showboard = true
        },
        keylayercloer (e) {
            this.showboard = false
        },numkey (e) {
            if (this.password.length === 0) {
            this.password_one = e.target.id
            this.password += e.target.id
            } else if (this.password.length === 1) {
            this.password_two = e.target.id
            this.password += e.target.id
            } else if (this.password.length === 2) {
            this.password_tree = e.target.id
            this.password += e.target.id
            } else if (this.password.length === 3) {
            this.password_frow = e.target.id
            this.password += e.target.id
            }
        },
        delect (e) {
            if (this.password.length === 4) {
            this.password_frow = ''
            this.password = this.password.substring(0, this.password.length - 1)
            } else if (this.password.length === 3) {
            this.password_tree = ''
            this.password = this.password.substring(0, this.password.length - 1)
            } else if (this.password.length === 2) {
            this.password_two = ''
            this.password = this.password.substring(0, this.password.length - 1)
            } else if (this.password.length === 1) {
            this.password_one = ''
            this.password = this.password.substring(0, this.password.length - 1)
            }
        },
        wxLogin(e){
            var that = this
            wx.showLoading({title: '加载中...'})
            if (e.mp.detail.userInfo == undefined) {
                app.globalData.hasLogin = false;
                wx.showToast({title: '微信登录失败', icon: 'none', duration: 2000})
                console.log('微信登录失败')
                wx.hideLoading()
                return;
            }
            wx.login({
            success: function (res) {
                if (res.code) {
                    request.post({
                        url: 'auth/login_by_weixin',
                        data: {
                            'code': res.code,
                            'userInfo': e.mp.detail.userInfo
                        }
                    }).then(res => {
                        wx.hideLoading()
                        if (res.errno === 0) {
                            // 存储用户信息
                            wx.setStorageSync('userInfo', res.data.userInfo)
                            wx.setStorageSync('token', res.data.token)
                            wx.setStorageSync('user',res.data.user)
                            if (wx.getStorageSync('user').mobile.length !== 11) {
                                that.phonebody = true
                            } else {
                                wx.navigateBack()
                            }
                        } else {
                            wx.showToast({title: res, icon: 'none', duration: 2000})
                            console.log(res)
                        }
                }).catch((err) => {
                    wx.hideLoading()
                    wx.showToast({title: err, icon: 'none', duration: 2000})
                    console.log(err)
                })
                } else {
                    wx.hideLoading()
                    wx.showToast({title: res, icon: 'none', duration: 2000})
                    console.log(res)
                }
            },
            fail: function (err) {
                wx.hideLoading()
                wx.showToast({title: err, icon: 'none', duration: 2000})
                console.log(err)
                }
            })
        },
         getPhoneNumber: function(e) {
             wx.showLoading({title: '加载中...'})
            if (e.mp.detail.errMsg !== "getPhoneNumber:ok") {
                // 拒绝授权
                wx.showToast({
                    title: '用户拒绝授权',
                    icon: 'none',
                    duration: 2000
                });
                wx.hideLoading()
            return;
            }
            this.$httpWX.post({
                url: 'auth/bindPhone',
                data: {
                    userId: wx.getStorageSync('user').id,
                    iv: e.mp.detail.iv,
                    encryptedData: e.mp.detail.encryptedData
                }
            }).then(res => {
                wx.hideLoading()
                if (res.errno === 0) {
                    this.phone = res.data
                    wx.showToast({
                        title: '获取手机号成功',
                        icon: 'success',
                        duration: 2000
                    });
                    this.condition = false
                }else{
                    wx.showToast({
                        title: res.data.errmsg,
                        icon: 'none',
                        duration: 2000
                    });
                }
            })
        }
    }
}
</script>

<style>
.binding{
    width: 60%;
    height: 50px;
    position: absolute;
    bottom: 30px;
    left: 20%;
    text-align: center;
    line-height: 50px;
    background-color: rgb(26,173,25);
    color: white;
    font-size: 30rpx;
    border-radius: 5px;
}
.binding:active{
    background-color: rgba(26,173,25, 0.8);
}
.obtain{
  background-color: white;
  color: #2b2b2b;
  border-radius: 98rpx; 
  font-size: 28rpx;
  padding: 15rpx;
}
.obtain::after {
    border-radius: 98rpx;
}
.obtain:active{
    
    background-color: rgba(0, 0, 0, 0.12);
}
.keyboard{
  position:fixed;
  bottom: 0px;
  width: 100%;
  height:300px;
  z-index: 2;
  background-color: #fff;
}
.line{
  width: 100%;
  height: 25%;
  border-top: 1px solid #cccccc; 
}
.line div{
  width: 32.7%;
  height: 100%;
  border-left: 1px solid #cccccc;
  display: flex;
  float: left;
  justify-content:center;
  align-items:center;
}
.line div:active{
  background-color:rgb(240, 238, 238);
} 
.phone_body{
    width: 100%;
    height: 100%;
    z-index: 1;
    position: fixed;
    top: 0;
    background-color: rgba(0, 0, 0, 0.3)
}
.div_head_tips{
    font-size: 25rpx;
    width: 90%;
    margin-left: 5%;
    margin-top: 20px;
}
.div_head_tips .pass{
    margin-top: 20px;
    width: 21%;
    margin-left: 4%;
    height: 40px;
    text-align: center;
    border: 1px solid #bbbbbb;
    line-height: 10vw;
    float: left;
    font-size: 30rpx;
    font-weight: 700;
}
.body_div{
    width: 70%;
    background-color: white;
    top: 15%;
    margin-left: 15%;
    position: fixed;
    height: 320px;
    border-radius: 10px;
    box-shadow:  2px 2px 5px 3px rgba(0, 0, 0, 0.12);
}
.wx-login-btn {
    margin: 20% 0 40rpx 0;
    height: 100rpx;
    line-height: 100rpx;
    font-size: 30rpx;
    border-radius: 6rpx;
    width: 90%;
    color: #fff;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    position: flex;
    bottom: 0;
    left: 0;
    padding: 0;
    margin-left: 5%;
    text-align: center;
    border-radius: 20px;
    box-shadow:  2px 2px 5px 3px rgba(0, 0, 0, 0.12);
}

</style>
