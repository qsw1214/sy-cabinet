<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.stanName" clearable class="filter-item" style="width: 200px;" placeholder="请输入规格名称"/>
      <!-- <el-input v-model="listQuery.subCabNum" clearable class="filter-item" style="width: 200px;" placeholder="请输入柜组编号"/> -->
      <el-select v-model="listQuery.stanMianId" class="filter-item" placeholder="请选择柜组编码">
        <el-option
          v-for="item in numList"
          :key="item.id"
          :label="item.mainCabNum"
          :value="item.id"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="规格ID" prop="id"/>

      <el-table-column align="center" label="规格名称" prop="stanName"/>

      <el-table-column align="center" label="价格/元" prop="stanMoney"/>

      <el-table-column align="center" label="柜组编码" prop="mainCabNum"/>

      <el-table-column align="center" label="封顶时间/小时" prop="stanCapping"/>

      <el-table-column align="center" label="免费时间/分钟" prop="stanFree"/>

      <el-table-column align="center" label="超出时间/分钟" prop="stanBeyond"/>

      <el-table-column align="center" label="押金" prop="stanDeposit"/>

      <el-table-column align="center" label="规格详情" prop="stanDetails"/>

      <el-table-column :formatter="dateFormat" align="center" label="创建时间" prop="addTime"/>

      <el-table-column :formatter="dateFormat" align="center" label="更新时间" prop="updateTime"/>

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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
        <!-- 柜组编号下拉框  -->
        <el-form-item label="柜组编号" prop="stanMianId">
          <el-select v-model="dataForm.mainCabNum" style="width:100%;" @change="changeNum(dataForm.mainCabNum)">
            <el-option
              v-for="item in numList"
              :key="item.id"
              :label="item.mainCabNum"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="规格名称" prop="stanName">
          <el-input v-model="dataForm.stanName"/>
        </el-form-item>
        <el-form-item label="价格/元" prop="stanMoney">
          <el-input v-model="dataForm.stanMoney"/>
        </el-form-item>
        <el-form-item label="封顶时间/小时" prop="stanCapping">
          <el-input v-model="dataForm.stanCapping"/>
        </el-form-item>
        <el-form-item label="免费时间/分钟" prop="stanFree">
          <el-input v-model="dataForm.stanFree"/>
        </el-form-item>
        <el-form-item label="超出时间/分钟" prop="stanBeyond">
          <el-input v-model="dataForm.stanBeyond" @blur="BlurText"/>
        </el-form-item>
        <el-form-item label="押金" prop="stanDeposit">
          <el-input v-model="dataForm.stanDeposit"/>
        </el-form-item>
        <el-form-item label="规格详情" prop="stanDetails">
          <el-input v-model="dataForm.stanDetails"/>
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
  .statusSelect {
    margin-right: 0;
    margin-bottom: 0;
  }
  .gallery {
    width: 80px;
    margin-right: 10px;
  }
</style>

<script>
import { listStandard, createStandard, updateStandard, deleteStandard } from '@/api/standard'
import { listMcabine } from '@/api/maincabinet'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'StandardList',
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      listMain: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        stanName: undefined,
        stanMianId: undefined
      },
      standardList: [],
      numList: [],
      dialogFormVisible: false,
      dialogFormVisibleRule: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      dataForm: {
        id: undefined,
        stanName: '',
        stanDetails: '',
        stanMoney: '',
        mainCabId: '',
        mainCabNum: '',
        stanMianId: '',
        stanCapping: '',
        stanFree: '',
        stanBeyond: '',
        stanDeposit: '',
        addTime: '',
        updateTime: '',
        deleted: 0
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    changeNum(val) {
      console.log('changNum:' + val)
      this.dataForm.stanMianId = val
    },
    getList() {
      this.listLoading = true
      console.log(this.listQuery.stanMianId)
      listStandard(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
      // 查询柜组信息
      this.listMainInfo()
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createStandard(this.dataForm)
            .then(response => {
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateStandard(this.dataForm)
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
    },
    handleDelete(row) {
      deleteStandard(row)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '删除成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        stanName: '',
        stanDetails: '',
        stanMoney: '',
        mainCabId: '',
        mainCabNum: '',
        stanMianId: '',
        stanCapping: '',
        stanFree: '',
        stanBeyond: '',
        addTime: '',
        updateTime: '',
        deleted: 0
      }
    },
    // 创建
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      console.log(row.mainCabId)
      for (const val of this.numList) {
        if (val.id === row.stanMianId) {
          this.mainNum = val.mainCabNum
        }
      }
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleSelect() {
      this.resetForm()
      this.dialogStatus = 'select'
      this.dialogFormVisibleRule = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 查询柜组信息
    listMainInfo() {
      listMcabine().then(response => {
        this.numList = response.data.data.items
      }).catch(response => {
        this.list = []
        this.total = 0
      })
    },
    // 校验数字
    BlurText() {
      console.log('校验')
      console.log(this.dataForm.stanBeyond)
      const boolean = new RegExp(/^([0-9]|[1-5][0-9]|60)$/).test(this.dataForm.stanBeyond)
      console.log(boolean)
      if (!boolean) {
        this.$message.warning('超出时间不能大于60分钟')
        this.dataForm.stanBeyond = ''
      }
    },
    dateFormat(row, column, cellValue, index) {
      var str = row[column.property]
      var dateee = new Date(str).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['子柜ID', '主柜编号', '子柜编号', '状态(0:空闲,1:使用中,2:故障,3:暂停)', '规格', '价格', '创建时间', '更新时间']
        const filterVal = ['id', 'mainCabId', 'subCabNum', 'status', 'standard', 'price', 'addTime', 'updateTime']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '子柜列表')
        this.downloadLoading = false
      })
    }
  }
}
</script>
