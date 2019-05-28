import request from '@/utils/request'

export function listMcabine(query) {
  return request({
    url: '/maincabine/list',
    method: 'get',
    params: query
  })
}

export function createMc(data) {
  return request({
    url: '/maincabine/create',
    method: 'post',
    data
  })
}

export function updateMc(data) {
  return request({
    url: '/maincabine/update',
    method: 'post',
    data
  })
}

export function deleteMc(data) {
  return request({
    url: '/maincabine/delete',
    method: 'post',
    data
  })
}
