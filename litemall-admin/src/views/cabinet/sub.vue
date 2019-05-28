
<template>
  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <!-- 柜组编号下拉框  -->
      <el-select v-model="listQuery.mainId" clearable class="filter-item" style="width:200px;" placeholder="柜组编码" @change="changeMainNum(listQuery.mainId)">
        <el-option
          v-for="item in numList"
          :key="item.id"
          :label="item.mainCabNum"
          :value="item.id"/>
      </el-select>
      <el-input v-model="listQuery.subCabNum" clearable class="filter-item" style="width: 200px;" placeholder="请输入子柜编号"/>
      <!-- 状态下拉框 -->
      <el-select v-model="listQuery.status" clearable class="filter-item" placeholder="请选择状态">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value"/>
      </el-select>
      <!-- 规格下拉框 -->
      <el-select v-model="listQuery.standardId" :disabled="listQuery.mainId === ''" clearable class="filter-item" placeholder="请选择规格">
        <el-option
          v-for="item in standardList"
          :key="item.id"
          :label="item.stanName"
          :value="item.id"/>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
      <el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" size="small" element-loading-text="正在查询中。。。" border fit highlight-current-row>

      <el-table-column align="center" label="子柜ID" prop="id"/>

      <el-table-column v-show="false" align="center" label="主柜ID" prop="mainId"/>

      <el-table-column align="center" label="主柜编号" prop="mainCabNum"/>

      <el-table-column align="center" label="子柜编号" prop="subCabNum"/>

      <el-table-column align="center" label="使用状态" prop="status">
        <template slot-scope="scope" ><span :style="{color:fontcolor(scope.row.status)}">{{ scope.row.status | formatStatus }}</span></template>
      </el-table-column>

      <el-table-column align="center" label="规格" prop="stanName"/>

      <el-table-column :formatter="dateFormat" align="center" label="创建时间" prop="addTime"/>

      <el-table-column :formatter="dateFormat" align="center" label="更新时间" prop="updateTime"/>

      <el-table-column align="center" label="操作" width="240" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button :disabled="scope.row.status !== 4" type="success" size="mini" @click="handleClear(scope.row)">清柜</el-button>
          <el-button :disabled="scope.row.status === 4 || scope.row.status === 1" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button :disabled="scope.row.status === 4 || scope.row.status === 1" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>
    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <!-- 柜组编号下拉框  -->
        <el-form-item label="柜组编号" prop="mainCabId">
          <el-select v-model="dataForm.mainId" style="width:100%;" @change="changeMainNum(dataForm.mainId)">
            <el-option
              v-for="item in numList"
              :key="item.id"
              :label="item.mainCabNum"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="规格" prop="standard">
          <el-select v-model="dataForm.stanId" placeholder="请选择规格" style="width:100%;" @change="changStan(dataForm.stanId)">
            <el-option
              v-for="item in standardList"
              :key="item.id"
              :label="item.stanName"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="子柜编号" prop="subCabNum">
          <el-input v-model="dataForm.subCabNum"/>
        </el-form-item>
        <!-- 状态下拉框 dataForm.status select1-->
        <el-form-item label="使用状态" prop="status">
          <el-select v-model="select1" style="width:100%;" @change="changeSelect(select1)">
            <el-option
              v-for="item in statusList"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
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
import { listSCabinet, createSubCab, updateSubCab, deleteSubCab, clearsark } from '@/api/subcabinet'
import { byMain } from '@/api/standard'
import { listMcabine } from '@/api/maincabinet'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
export default {
  name: 'SubCabinetList',
  components: { BackToTop, Pagination },
  filters: {
    formatStatus(status) {
      if (status === 0) {
        return '空闲'
      } else if (status === 1 || status === 5) {
        return '使用中'
      } else if (status === 2) {
        return '故障'
      } else if (status === 3) {
        return '暂停'
      } else {
        return '超时'
      }
    }
  },
  data() {
    return {
      list: [],
      listMain: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        mainCabNum: undefined,
        mainId: '',
        subCabNum: undefined,
        status: '',
        standardId: '',
        price: undefined
      },
      statusList: [
        {
          value: 0,
          label: '空闲'
        },
        {
          value: 1,
          label: '使用中'
        },
        {
          value: 2,
          label: '故障'
        },
        {
          value: 3,
          label: '暂停'
        },
        {
          value: 4,
          label: '超时'
        }],
      standardList: [],
      numList: [],
      dialogFormVisible: false,
      dialogFormVisibleRule: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建',
        select: '修改'
      },
      dataForm: {
        id: undefined,
        mainCabId: '',
        mainCabNum: '',
        subCabNum: '',
        status: undefined,
        stanId: '',
        stanName: '',
        addTime: '',
        updateTime: '',
        deleted: 0
      },
      downloadLoading: false,
      select1: '',
      mainNum: '',
      selectNum: {
        id: '',
        num: ''
      },
      standSelect: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 状态修改为选中状态信息
    changeSelect(val) {
      this.dataForm.status = val
    },
    // 柜组编码修改为选中柜组Id信息
    changeMainNum(val) {
      console.log('changNum:' + val)
      this.dataForm.mainId = val
      this.byMain(val)
    },
    // 状态修改为选中状态信息
    changStan(val) {
      console.log('changStan:' + val)
      this.dataForm.stanName = val
    },
    byMain(mainId) {
      byMain({ mainId: mainId }).then(response => {
        this.standardList = response.data.data.items
      }).catch((ss) => {
        this.standardList = []
      })
    },
    getList() {
      this.listLoading = true
      listSCabinet(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
        // 查询柜组信息
        listMcabine(this.listQuery).then(response => {
          this.numList = response.data.data.items
        }).catch((ss) => {
          this.numList = []
        })
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    fontcolor(val) {
      if (val === 0) {
        return '#3DC206'
      }
      if (val === 1) {
        return '#CD6839'
      }
      if (val === 2) {
        return '#A0522D'
      }
      if (val === 3) {
        return '#A0522D'
      }
      if (val === 4) {
        return '#B22222'
      }
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          createSubCab(this.dataForm)
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
          updateSubCab(this.dataForm)
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
      deleteSubCab(row)
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
        mainId: '',
        mainCabNum: '',
        subCabNum: '',
        status: 0,
        stanName: '',
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
    // 编辑
    handleUpdate(row) {
      byMain({ mainId: row.mainId }).then(response => {
        this.standardList = response.data.data.items
        console.log(this.standardList)
      }).catch((ss) => {
        this.standardList = []
      })
      // 回显状态信息
      for (const val of this.statusList) {
        if (val.value === row.status) {
          this.select1 = val.label
        }
      }
      // 回显柜组编码信息
      for (const val of this.numList) {
        if (val.id === row.mainId) {
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
    handleClear(row) {
      clearsark({ subId: row.id }).then(response => {
        console.log(response.data.data)
        if (response.data.data) {
          this.$notify.success({
            title: '成功',
            message: '清柜成功'
          })
          this.getList()
        } else {
          this.$notify.error({
            title: '失败',
            message: '计算有误'
          })
        }
      }).catch(response => {
        this.$notify.error({
          title: '失败',
          message: response.data.errmsg
        })
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
    // 校验数字
    BlurText() {
      console.log('校验')
      // const boolean = new RegExp('/^([0-9]|[1-5][0-9]|60)$/').test(e.target.value)
      // if (!boolean) {
      //   this.$message.warning('不能大于60')
      //   e.target.value = ''
      // }
    },
    dateFormat(row, column, cellValue, index) {
      var str = row[column.property]
      var dateee = new Date(str).toJSON()
      return new Date(+new Date(dateee) + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['子柜ID', '主柜编号', '子柜编号', '状态(0:空闲,1:使用中,2:故障,3:暂停)', '规格', '创建时间', '更新时间']
        const filterVal = ['id', 'mainCabNum', 'subCabNum', 'status', 'stanName', 'addTime', 'updateTime']
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '子柜列表')
        this.downloadLoading = false
      })
    }
  }
}
</script>
