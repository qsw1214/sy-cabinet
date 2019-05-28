import request from '@/utils/request'

export function listSCabinet(query) {
  return request({
    url: '/subcabinet/list',
    method: 'get',
    params: query
  })
}

export function createSubCab(data) {
  return request({
    url: '/subcabinet/create',
    method: 'post',
    data
  })
}

export function updateSubCab(data) {
  return request({
    url: '/subcabinet/update',
    method: 'post',
    data
  })
}

export function deleteSubCab(data) {
  return request({
    url: '/subcabinet/delete',
    method: 'post',
    data
  })
}

// 查询子柜规格
export function listStandard(query) {
  return request({
    url: '/subcabinet/listStandard',
    method: 'get',
    params: query
  })
}

// 查询柜组编号
export function listMainNum(query) {
  return request({
    url: '/subcabinet/listAllNum',
    method: 'get',
    params: query
  })
}

// 修改规则
export function updateRule(query) {
  return request({
    url: '/subcabinet/updateRule',
    method: 'get',
    params: query
  })
}

// 清柜
export function clearsark(query) {
  return request({
    url: '/subcabinet/clearsark',
    method: 'get',
    params: query
  })
}
