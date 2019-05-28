import request from '@/utils/request'

export function listStandard(query) {
  return request({
    url: '/standard/list',
    method: 'get',
    params: query
  })
}
export function byMain(query) {
  return request({
    url: '/standard/bymain',
    method: 'get',
    params: query
  })
}
export function createStandard(data) {
  return request({
    url: '/standard/create',
    method: 'post',
    data
  })
}

export function updateStandard(data) {
  return request({
    url: '/standard/update',
    method: 'post',
    data
  })
}

export function deleteStandard(data) {
  return request({
    url: '/standard/delete',
    method: 'post',
    data
  })
}
