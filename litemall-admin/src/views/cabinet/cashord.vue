<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.username" clearable class="filter-item" style="width: 200px;" placeholder="请输入用户姓名"/>
      <el-select v-model="listQuery.status" class="filter-item">
        <el-option v-for="item in statuitem" :label="item.text" :key="item.value" :value="item.value"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="序列" prop="id"/>

      <el-table-column align="center" label="状态" prop="status">
        <template slot-scope="scope">{{ scope.row.status | formatStatus }}</template>
      </el-table-column>

      <el-table-column align="center" label="金额" prop="money"/>

      <el-table-column align="center" label="用户ID" prop="userId"/>

      <el-table-column align="center" label="姓名" prop="username"/>

      <el-table-column align="center" label="电话" prop="userphone"/>

      <el-table-column align="center" label="银行卡号" prop="cardnum"/>

      <el-table-column align="center" label="提现银行" prop="ownbank"/>

      <el-table-column align="center" label="操作人员" prop="adminname"/>

      <el-table-column align="center" label="备注" prop="remarks"/>

      <el-table-column align="center" label="提交时间" prop="subTime"/>

      <el-table-column align="center" label="更新时间" prop="upTime"/>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button :disabled="scope.row.status !== 0" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="状态" prop="status">
          <el-select v-model="dataForm.status">
            <el-option v-for="item in options" :label="item.text" :key="item.value" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="dataForm.remarks"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

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
import { cashordlist, cashpostal } from '@/api/cashord'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'CabinetList',
  components: { BackToTop, Pagination },
  filters: {
    formatStatus(status) {
      if (status === 0) {
        return '待提现'
      } else if (status === 1) {
        return '已提现'
      } else {
        return '提现失败'
      }
    }
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        username: '',
        status: 0,
        page: 1,
        limit: 20
      },
      statuitem: [
        {
          value: -1, text: '全部'
        },
        {
          value: 0, text: '待提现'
        },
        {
          value: 1, text: '已提现'
        },
        {
          value: 2, text: '提现失败'
        }
      ],
      options: [
        {
          value: 0, text: '请选择'
        },
        {
          value: 1, text: '已提现'
        },
        {
          value: 2, text: '提现失败'
        }
      ],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dataForm: {
        id: undefined,
        remarks: '',
        status: 1,
        money: 0,
        userId: undefined
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      cashordlist(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    updateData() {
      if (this.dataForm.status > 0 && this.dataForm.remarks !== '') {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            cashpostal(this.dataForm)
              .then(() => {
                for (const v of this.list) {
                  if (v.id === this.dataForm.id) {
                    const index = this.list.indexOf(v)
                    this.list.splice(index, 1, this.dataForm)
                    break
                  }
                }
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '更新成功'
                })
                this.getList()
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        })
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        remarks: '',
        status: 1,
        money: 0,
        userId: undefined
      }
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    dateFormat(row, column, cellValue, index) {
      var str = row[column.property]
      var dateee = new Date(str).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['序列', '状态（0待提现、1已提现、2提现失败）', '金额', '姓名', '电话', '银行卡号', '提现银行', '操作人员', '备注', '提交时间', '更新时间']
        const filterVal = ['id', 'status', 'username', 'userphone', 'money', 'cardnum', 'ownbank', 'adminname', 'subTime', 'subTime', 'upTime']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '提现记录')
        this.downloadLoading = false
      })
    }
  }
}
</script>
