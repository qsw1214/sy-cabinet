<template>
  <div class="app-container">
    <div id="pdfDom" class="row" style="padding-top: 55px;background-color:#fff;">
      <ve-histogram :extend="chartExtend" :data="chartData" :settings="chartSettings"/>
    </div>
    <el-button class="filter-item" type="primary" icon="el-icon-download" @click="getPdf()">导出</el-button>
  </div>
</template>

<script>
import { statMoney } from '@/api/stat'
import VeHistogram from 'v-charts/lib/histogram'

export default {
  components: { VeHistogram },
  data() {
    return {
      chartData: {},
      chartSettings: {},
      chartExtend: {},
      htmlTitle: '资金流水统计--'
    }
  },
  created() {
    statMoney().then(response => {
      this.chartData = response.data.data
      this.chartSettings = {
        labelMap: {
          'amount': '日收入'
        }
      }
      this.chartExtend = {
        xAxis: { boundaryGap: true },
        series: {
          label: { show: true, position: 'top' }
        }
      }
      const year = new Date().getFullYear()
      const month = new Date().getMonth() + 1
      const day = new Date().getDate()
      this.htmlTitle += year + '-' + month + '-' + day
    })
  }
}
</script>

