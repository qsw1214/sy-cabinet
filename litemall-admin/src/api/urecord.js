import request from '@/utils/request'

export function listURecord(query) {
  return request({
    url: '/usagerecord/list',
    method: 'get',
    params: query
  })
}
