var app = getApp();
var WxParse = require('../../lib/wxParse/wxParse.js');
var util = require('../../utils/util.js');
var api = require('../../config/api.js');
var user = require('../../utils/user.js');

Page({
  data: {
    id: 0,
    goods: {},
    groupon: [], //该商品支持的团购规格
    grouponLink: {}, //参与的团购
    attribute: [],
    issueList: [],
    comment: [],
    brand: {},
    specificationList: [],
    productList: [],
    relatedGoods: [],
    cartGoodsCount: 0,
    userHasCollect: 0,
    number: 1,
    checkedSpecText: '规格数量选择',
    tmpSpecText: '请选择规格数量',
    checkedSpecPrice: 0,
    openAttr: false,
    openShare: false,
    noCollectImage: '/static/images/icon_collect.png',
    hasCollectImage: '/static/images/icon_collect_checked.png',
    collectImage: '/static/images/icon_collect.png',
    shareImage: '',
    isGroupon: false, //标识是否是一个参团购买
    soldout: false,
    canWrite: false, //用户是否获取了保存相册的权限
    hidden: true, //遮罩层显示隐藏
    qrcode_temp: "", //二维码
    scene: "", //二维码参数
    image: "", //二维码暂时存放处
    windowWidth: 300,
    windowHeight: 550,
  },

  // 页面分享
  onShareAppMessage: function() {
    let that = this;
    return {
      title: that.data.goods.name,
      desc: '唯爱与美食不可辜负',
      path: '/pages/index/index?goodId=' + this.data.id
    }
  },
  hidcanvas: function() {
    let that = this;
    that.setData({
      hidden: true
    });
  },
  shareFriendOrCircle: function() {
    //var that = this;
    if (this.data.openShare === false) {
      this.setData({
        openShare: !this.data.openShare
      });
    } else {
      return false;
    }
  },
  handleSetting: function(e) {
    var that = this;
    // console.log(e)
    if (!e.detail.authSetting['scope.writePhotosAlbum']) {
      wx.showModal({
        title: '警告',
        content: '不授权无法保存',
        showCancel: false
      })
      that.setData({
        canWrite: false
      })
    } else {
      wx.showToast({
        title: '保存成功'
      })
      that.setData({
        canWrite: true
      })
    }
  },
  // 保存分享图
  saveShare: function() {
    let that = this;
    that.setData({
      hidden: false
    });

    util.request(api.Gettoken, {}, "post").then(function(res) {
      wx.showLoading({
        title: '生成图片中',
      });
      var obj = JSON.parse(res.data.token);
      util.request(api.Qrcode, {
        content: obj.access_token,
        mobile: that.data.id
      }, "post").then(function(res2) {
          //缓存二维码
          that.setData({
            qrcode_temp: "http://1d828193e3.imwork.net:39655/wx/storage/fetch/v8fcuwu86rhih0jrtdyo.png"
          })
          console.log('开始绘制图片')
          that.drawImage();
          wx.hideLoading();
        console.log(res2)
      });
    });
  },

  canvasToImage() {
    var that = this
    wx.canvasToTempFilePath({
      x: 0,
      y: 0,
      width: that.data.windowWidth,
      height: that.data.windowWidth * that.data.scale,
      destWidth: that.data.windowWidth * 4,
      destHeight: that.data.windowWidth * 4 * that.data.scale,
      canvasId: 'myCanvas',
      success: function(res) {
        console.log('朋友圈分享图生成成功:' + res.tempFilePath)
      },
      fail: function(err) {
        console.log('失败')
        console.log(err)
      }
    })
  },
  drawImage() {
    let userInfo = wx.getStorageSync('userInfo');
    //绘制canvas图片
    var that = this
    const ctx = wx.createCanvasContext('myCanvas')
    var bgPath = '../../static/images/share_bg.jpg';
    var portraitPath = that.data.goods.gallery[0]; //商品图片
    var hostNickname = that.data.goods.name //商品名称
    var qrPath = that.data.qrcode_temp; //二维码
    var counter = that.data.goods.counterPrice; //商品原价
    var retail = that.data.goods.retailPrice; //商品原价
    var windowWidth = that.data.windowWidth;
    var windowHeight = that.data.windowHeight;
    var brief = "简介：" + that.data.goods.brief;
    //绘制背景图片
    ctx.drawImage(bgPath, 0, 0, windowWidth, windowHeight)
    //商品图片
    ctx.save()
    ctx.beginPath()
    ctx.drawImage(portraitPath, 0, 0, windowWidth, windowHeight * 0.4)
    //商品名称
    ctx.setFillStyle('#000000')
    ctx.setFontSize(16)
    ctx.setTextAlign('center')
    if (hostNickname.length > 16) {
      ctx.strokeText(hostNickname.substring(0, 16) + "...", windowWidth / 2, windowHeight * 0.45)
    } else {
      ctx.strokeText(hostNickname, windowWidth / 2, windowHeight * 0.45)
    }
    //商品价格--原价
    ctx.setFillStyle('#BDBDBD')
    ctx.setFontSize(14)
    ctx.setTextAlign("center")
    ctx.fillText("原价" + counter, windowWidth / 2, windowHeight * 0.5)
    //商品价格--现价
    ctx.setFillStyle('#EE9572')
    ctx.setFontSize(16)
    ctx.setTextAlign("center")
    ctx.fillText("现价" + retail, windowWidth / 2, windowHeight * 0.55)
    //商品介绍
    ctx.setFillStyle('#EE9572')
    ctx.setFontSize(14)
    ctx.setTextAlign('center')
    var numsiex = brief.length / 16;
    console.log(numsiex)
    if (numsiex < 1) {
      ctx.fillText(brief, windowWidth / 2, windowHeight * 0.6)
    } else if (numsiex < 4) {
      var sect = 0.6;
      for (var i = 0; i < numsiex; i++) {
        ctx.fillText(brief.substring(i * 16, i * 16 + 16), windowWidth / 2, windowHeight * sect);
        sect += 0.05;
      }
    } else {
      var sect = 0.6;
      for (var i = 0; i < 4; i++) {
        if (i == 3 && numsiex > 4) {
          ctx.fillText(brief.substring(i * 16, i * 16 + 15) + " ...", windowWidth / 2, windowHeight * sect)
        } else {
          ctx.fillText(brief.substring(i * 16, i * 16 + 16), windowWidth / 2, windowHeight * sect)
        }
        sect += 0.05;
      }
    }

    //绘制二维码
    ctx.drawImage(qrPath, 20, windowHeight - (0.36 * windowWidth + 20), 0.36 * windowWidth, 0.36 * windowWidth)
    //二维码配字
    ctx.setFillStyle('#000000')
    ctx.setFontSize(14)
    ctx.setTextAlign('left')
    ctx.fillText('长按二维立即购买', windowWidth / 2, windowHeight - ((0.36 * windowWidth) / 2 + 20));

    ctx.draw(false, function() {
      wx.canvasToTempFilePath({
        canvasId: 'myCanvas',
        success: function(res) {
          wx.saveImageToPhotosAlbum({
            filePath: res.tempFilePath,
            success(res) {
              console.log(res.errMsg)
              wx.showToast({
                title: '保存成功',
                icon: 'succes',
                duration: 1000,
                mask: true
              })
            },
            fail(res) {
              console.log(res.errMsg)
            }
          });
        }
      })
    });
  },
  //从分享的团购进入
  getGrouponInfo: function(grouponId) {
    let that = this;
    util.request(api.GroupOnJoin, {
      grouponId: grouponId
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          grouponLink: res.data.groupon,
          id: res.data.goods.id
        });
        //获取商品详情
        that.getGoodsInfo();
      }
    });
  },

  // 获取商品信息
  getGoodsInfo: function() {
    let that = this;
    util.request(api.GoodsDetail, {
      id: that.data.id
    }).then(function(res) {
      if (res.errno === 0) {

        let _specificationList = res.data.specificationList
        // 如果仅仅存在一种货品，那么商品页面初始化时默认checked
        if (_specificationList.length == 1) {
          if (_specificationList[0].valueList.length == 1) {
            _specificationList[0].valueList[0].checked = true

            // 如果仅仅存在一种货品，那么商品价格应该和货品价格一致
            // 这里检测一下
            let _productPrice = res.data.productList[0].price;
            let _goodsPrice = res.data.info.retailPrice;
            if (_productPrice != _goodsPrice) {
              console.error('商品数量价格和货品不一致');
            }

            that.setData({
              checkedSpecText: _specificationList[0].valueList[0].value,
              tmpSpecText: '已选择：' + _specificationList[0].valueList[0].value,
            });
          }
        }
        that.setData({
          goods: res.data.info,
          attribute: res.data.attribute,
          issueList: res.data.issue,
          comment: res.data.comment,
          brand: res.data.brand,
          specificationList: res.data.specificationList,
          productList: res.data.productList,
          userHasCollect: res.data.userHasCollect,
          shareImage: res.data.info.picUrl,
          checkedSpecPrice: res.data.info.retailPrice,
          groupon: res.data.groupon
        });

        //如果是通过分享的团购参加团购，则团购项目应该与分享的一致并且不可更改
        if (that.data.isGroupon) {
          let groupons = that.data.groupon;
          for (var i = 0; i < groupons.length; i++) {
            if (groupons[i].id != that.data.grouponLink.rulesId) {
              groupons.splice(i, 1);
            }
          }
          groupons[0].checked = true;
          //重设团购规格
          that.setData({
            groupon: groupons
          });

        }

        if (res.data.userHasCollect == 1) {
          that.setData({
            collectImage: that.data.hasCollectImage
          });
        } else {
          that.setData({
            collectImage: that.data.noCollectImage
          });
        }

        WxParse.wxParse('goodsDetail', 'html', res.data.info.detail, that);
        //获取推荐商品
        that.getGoodsRelated();
      }
    });
  },

  // 获取推荐商品
  getGoodsRelated: function() {
    let that = this;
    util.request(api.GoodsRelated, {
      id: that.data.id
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          relatedGoods: res.data.goodsList,
        });
      }
    });
  },

  // 团购选择
  clickGroupon: function(event) {
    let that = this;

    //参与团购，不可更改选择
    if (that.data.isGroupon) {
      return;
    }

    let specName = event.currentTarget.dataset.name;
    let specValueId = event.currentTarget.dataset.valueId;

    let _grouponList = this.data.groupon;
    for (let i = 0; i < _grouponList.length; i++) {
      if (_grouponList[i].id == specValueId) {
        if (_grouponList[i].checked) {
          _grouponList[i].checked = false;
        } else {
          _grouponList[i].checked = true;
        }
      } else {
        _grouponList[i].checked = false;
      }
    }

    this.setData({
      groupon: _grouponList,
    });
  },

  // 规格选择
  clickSkuValue: function(event) {
    let that = this;
    let specName = event.currentTarget.dataset.name;
    let specValueId = event.currentTarget.dataset.valueId;

    //判断是否可以点击

    //TODO 性能优化，可在wx:for中添加index，可以直接获取点击的属性名和属性值，不用循环
    let _specificationList = this.data.specificationList;
    for (let i = 0; i < _specificationList.length; i++) {
      if (_specificationList[i].name === specName) {
        for (let j = 0; j < _specificationList[i].valueList.length; j++) {
          if (_specificationList[i].valueList[j].id == specValueId) {
            //如果已经选中，则反选
            if (_specificationList[i].valueList[j].checked) {
              _specificationList[i].valueList[j].checked = false;
            } else {
              _specificationList[i].valueList[j].checked = true;
            }
          } else {
            _specificationList[i].valueList[j].checked = false;
          }
        }
      }
    }
    this.setData({
      specificationList: _specificationList,
    });
    //重新计算spec改变后的信息
    this.changeSpecInfo();

    //重新计算哪些值不可以点击
  },

  //获取选中的团购信息
  getCheckedGrouponValue: function() {
    let checkedValues = {};
    let _grouponList = this.data.groupon;
    for (let i = 0; i < _grouponList.length; i++) {
      if (_grouponList[i].checked) {
        checkedValues = _grouponList[i];
      }
    }

    return checkedValues;
  },

  //获取选中的规格信息
  getCheckedSpecValue: function() {
    let checkedValues = [];
    let _specificationList = this.data.specificationList;
    for (let i = 0; i < _specificationList.length; i++) {
      let _checkedObj = {
        name: _specificationList[i].name,
        valueId: 0,
        valueText: ''
      };
      for (let j = 0; j < _specificationList[i].valueList.length; j++) {
        if (_specificationList[i].valueList[j].checked) {
          _checkedObj.valueId = _specificationList[i].valueList[j].id;
          _checkedObj.valueText = _specificationList[i].valueList[j].value;
        }
      }
      checkedValues.push(_checkedObj);
    }

    return checkedValues;
  },

  //判断规格是否选择完整
  isCheckedAllSpec: function() {
    return !this.getCheckedSpecValue().some(function(v) {
      if (v.valueId == 0) {
        return true;
      }
    });
  },

  getCheckedSpecKey: function() {
    let checkedValue = this.getCheckedSpecValue().map(function(v) {
      return v.valueText;
    });
    return checkedValue;
  },

  // 规格改变时，重新计算价格及显示信息
  changeSpecInfo: function() {
    let checkedNameValue = this.getCheckedSpecValue();

    //设置选择的信息
    let checkedValue = checkedNameValue.filter(function(v) {
      if (v.valueId != 0) {
        return true;
      } else {
        return false;
      }
    }).map(function(v) {
      return v.valueText;
    });
    if (checkedValue.length > 0) {
      this.setData({
        tmpSpecText: checkedValue.join('　')
      });
    } else {
      this.setData({
        tmpSpecText: '请选择规格数量'
      });
    }

    if (this.isCheckedAllSpec()) {
      this.setData({
        checkedSpecText: this.data.tmpSpecText
      });

      // 规格所对应的货品选择以后
      let checkedProductArray = this.getCheckedProductItem(this.getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        this.setData({
          soldout: true
        });
        console.error('规格所对应货品不存在');
        return;
      }

      let checkedProduct = checkedProductArray[0];
      if (checkedProduct.number > 0) {
        this.setData({
          checkedSpecPrice: checkedProduct.price,
          soldout: false
        });
      } else {
        this.setData({
          checkedSpecPrice: this.data.goods.retailPrice,
          soldout: true
        });
      }

    } else {
      this.setData({
        checkedSpecText: '规格数量选择',
        checkedSpecPrice: this.data.goods.retailPrice,
        soldout: false
      });
    }

  },

  // 获取选中的产品（根据规格）
  getCheckedProductItem: function(key) {
    return this.data.productList.filter(function(v) {
      if (v.specifications.toString() == key.toString()) {
        return true;
      } else {
        return false;
      }
    });
  },

  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    if (options.id) {
      this.setData({
        id: parseInt(options.id)
      });
      this.getGoodsInfo();
    }
    if (options.grouponId) {
      this.setData({
        isGroupon: true,
      });
      this.getGrouponInfo(options.grouponId);
    }
    let that = this;
    wx.getSetting({
      success: function(res) {
        //不存在相册授权
        if (!res.authSetting['scope.writePhotosAlbum']) {
          wx.authorize({
            scope: 'scope.writePhotosAlbum',
            success: function() {
              that.setData({
                canWrite: true
              })
            },
            fail: function(err) {
              that.setData({
                canWrite: false
              })
            }
          })
        } else {
          that.setData({
            canWrite: true
          });
        }
      }
    })
  },
  onShow: function() {
    // 页面显示
    var that = this;
    util.request(api.CartGoodsCount).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          cartGoodsCount: res.data
        });
      }
    });
  },

  //添加或是取消收藏
  addCollectOrNot: function() {
    let that = this;
    util.request(api.CollectAddOrDelete, {
        type: 0,
        valueId: this.data.id
      }, "POST")
      .then(function(res) {
        let _res = res;
        if (_res.errno == 0) {
          if (_res.data.type == 'add') {
            that.setData({
              collectImage: that.data.hasCollectImage
            });
          } else {
            that.setData({
              collectImage: that.data.noCollectImage
            });
          }

        } else {
          wx.showToast({
            image: '/static/images/icon_error.png',
            title: _res.errmsg,
            mask: true
          });
        }

      });

  },

  //立即购买（先自动加入购物车）
  addFast: function() {
    var that = this;
    if (this.data.openAttr == false) {
      //打开规格选择窗口
      this.setData({
        openAttr: !this.data.openAttr
      });
    } else {

      //提示选择完整规格
      if (!this.isCheckedAllSpec()) {
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '请选择完整规格'
        });
        return false;
      }

      //根据选中的规格，判断是否有对应的sku信息
      let checkedProductArray = this.getCheckedProductItem(this.getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        //找不到对应的product信息，提示没有库存
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '没有库存'
        });
        return false;
      }

      let checkedProduct = checkedProductArray[0];
      //验证库存
      if (checkedProduct.number <= 0) {
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '没有库存'
        });
        return false;
      }

      //验证团购是否有效
      let checkedGroupon = this.getCheckedGrouponValue();

      //立即购买
      util.request(api.CartFastAdd, {
          goodsId: this.data.goods.id,
          number: this.data.number,
          productId: checkedProduct.id
        }, "POST")
        .then(function(res) {
          if (res.errno == 0) {

            // 如果storage中设置了cartId，则是立即购买，否则是购物车购买
            try {
              wx.setStorageSync('cartId', res.data);
              wx.setStorageSync('grouponRulesId', checkedGroupon.id);
              wx.setStorageSync('grouponLinkId', that.data.grouponLink.id);
              wx.navigateTo({
                url: '/pages/checkout/checkout'
              })
            } catch (e) {}

          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: res.errmsg,
              mask: true
            });
          }
        });
    }


  },

  //添加到购物车
  addToCart: function() {
    var that = this;
    if (this.data.openAttr == false) {
      //打开规格选择窗口
      this.setData({
        openAttr: !this.data.openAttr
      });
    } else {

      //提示选择完整规格
      if (!this.isCheckedAllSpec()) {
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '请选择完整规格'
        });
        return false;
      }

      //根据选中的规格，判断是否有对应的sku信息
      let checkedProductArray = this.getCheckedProductItem(this.getCheckedSpecKey());
      if (!checkedProductArray || checkedProductArray.length <= 0) {
        //找不到对应的product信息，提示没有库存
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '没有库存'
        });
        return false;
      }

      let checkedProduct = checkedProductArray[0];
      //验证库存
      if (checkedProduct.number <= 0) {
        wx.showToast({
          image: '/static/images/icon_error.png',
          title: '没有库存'
        });
        return false;
      }

      //添加到购物车
      util.request(api.CartAdd, {
          goodsId: this.data.goods.id,
          number: this.data.number,
          productId: checkedProduct.id
        }, "POST")
        .then(function(res) {
          let _res = res;
          if (_res.errno == 0) {
            wx.showToast({
              title: '添加成功'
            });
            that.setData({
              openAttr: !that.data.openAttr,
              cartGoodsCount: _res.data
            });
            if (that.data.userHasCollect == 1) {
              that.setData({
                collectImage: that.data.hasCollectImage
              });
            } else {
              that.setData({
                collectImage: that.data.noCollectImage
              });
            }
          } else {
            wx.showToast({
              image: '/static/images/icon_error.png',
              title: _res.errmsg,
              mask: true
            });
          }

        });
    }

  },

  cutNumber: function() {
    this.setData({
      number: (this.data.number - 1 > 1) ? this.data.number - 1 : 1
    });
  },
  addNumber: function() {
    this.setData({
      number: this.data.number + 1
    });
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  switchAttrPop: function() {
    if (this.data.openAttr == false) {
      this.setData({
        openAttr: !this.data.openAttr
      });
    }
  },
  closeAttr: function() {
    this.setData({
      openAttr: false,
    });
  },
  closeShare: function() {
    this.setData({
      openShare: false,
    });
  },
  openCartPage: function() {
    wx.switchTab({
      url: '/pages/cart/cart'
    });
  },
  onReady: function() {
    // 页面渲染完成

  },
  // 下拉刷新
  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.getGoodsInfo();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },
  //根据已选的值，计算其它值的状态
  setSpecValueStatus: function() {

  },

})