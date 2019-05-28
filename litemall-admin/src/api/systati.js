import request from '@/utils/request'

export function profitstati(query) {
  return request({
    url: '/systat/profitstati',
    method: 'get',
    params: query
  })
}
