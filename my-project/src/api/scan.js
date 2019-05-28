import request from '@/utils/request'

export function listAd (query) {
  return request({
    url: '/ad/list',
    method: 'get',
    params: query
  })
}
