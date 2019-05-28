<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>
    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="记录ID" prop="id"/>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">{{ scope.row.status | formatStatus }}</template>
      </el-table-column>
      <el-table-column :formatter="dateFormat" align="center" label="开始时间" prop="startTime"/>

      <el-table-column :formatter="dateFormat" align="center" label="结束时间" prop="endTime"/>

      <el-table-column align="center" label="预存金额" prop="preAmount"/>

      <el-table-column align="center" label="实际金额" prop="actualAmount"/>

      <el-table-column align="center" label="预计时长" prop="preTime"/>

      <el-table-column align="center" label="实际时长" prop="actualTime"/>

      <el-table-column align="center" label="储物柜单价/元" prop="usaMoney"/>

      <el-table-column align="center" label="封顶时间/小时" prop="usaCapp"/>

      <el-table-column align="center" label="免费时间/分" prop="usaFree"/>

      <el-table-column align="center" label="超出计费时间/分" prop="usaBeyond"/>

      <el-table-column :formatter="dateFormat" align="center" label="创建时间" prop="addTime"/>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

  </div>
</template>

<style>
  .table-expand {
    font-size: 0;
  }
  .table-expand label {
    width: 100px;
    color: #99a9bf;
  }
  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }
  .gallery {
    width: 80px;
    margin-right: 10px;
  }
</style>

<script>
import { listURecord } from '@/api/urecord'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'CabinetList',
  components: { BackToTop, Pagination },
  filters: {
    formatStatus(status) {
      if (status === 0) {
        return '正在使用'
      } else if (status === 1) {
        return '历史使用'
      } else {
        return '暂停'
      }
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      itemStatus: [{
        status: 0,
        value: '正在使用'
      },
      {
        status: 1,
        value: '历史记录'
      }],
      listLoading: true,
      listQuery: {
        status: -1,
        page: 1,
        limit: 20
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listURecord(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    dateFormat(row, column, cellValue, index) {
      var str = row[column.property]
      var dateee = new Date(str).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    filterStatus() {
      console.log('value')
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['状态（0：正在使用、1：历史使用）', '开始时间', '结束时间', '预存金额', '实际金额', '预计时长', '实际时长', '创建时间', '储物柜单价', '封顶时间', '免费时间', '超出计时时间']
        const filterVal = ['status', 'startTime', 'endTime', 'preAmount', 'actualAmount', 'preTime', 'actualTime', 'addTime', 'usaMoney', 'usaCapp', 'usaFree', 'usaBeyond']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '用户使用记录')
        this.downloadLoading = false
      })
    }
  }
}
</script>
