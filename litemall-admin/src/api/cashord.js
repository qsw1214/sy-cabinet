import request from '@/utils/request'

export function cashordlist(query) {
  return request({
    url: '/cashord/list',
    method: 'get',
    params: query
  })
}
export function cashpostal(query) {
  return request({
    url: '/cashord/cashpostal',
    method: 'get',
    params: query
  })
}
