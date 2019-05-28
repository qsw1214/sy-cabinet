const host = 'https://cabinet.fycld.com/wx/'
// const host = 'http://127.0.0.1:8081/wx/'
function request (url, method, data, header = {}) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: host + url, // 仅为示例，并非真实的接口地址
      method: method,
      data: data,
      header: {
        'Content-Type': 'application/json'
      },
      success: function (res) {
        resolve(res.data)
      },
      fail: function (res) {
        wx.hideLoading()
      },
      complete: function () {
        wx.hideLoading()
      }
    })
  })
}
function get (obj) {
  return request(obj.url, 'GET', obj.data)
}

function post (obj) {
  return request(obj.url, 'POST', obj.data)
}

export default {
  request,
  get,
  post,
  host
}
