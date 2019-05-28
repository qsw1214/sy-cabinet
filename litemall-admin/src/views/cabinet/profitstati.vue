<template>
  <div class="app-container">
    <el-button plain @click="reset()">总收入：{{ total }}</el-button>
    <el-button type="success" @click="profit('%Y-%m-%d')">日收入</el-button>
    <el-button type="warning" @click="profit('%Y-%m')">月收入</el-button>
    <el-button type="danger" @click="profit('%Y')">年收入</el-button>
    <div id="pdfDom" class="row" style="padding-top: 55px;background-color:#fff;">
      <ve-histogram :events="chartEvents" :data="chartData" :settings="chartSettings"/>
    </div>
    <el-button class="filter-item" type="primary" icon="el-icon-download" @click="getPdf('app-container', '资金流水统计')">导出</el-button>
  </div>
</template>

<script>
import { profitstati } from '@/api/systati'
import VeHistogram from 'v-charts/lib/histogram'
var myDate = new Date()
export default {
  components: { VeHistogram },
  data() {
    var self = this
    this.chartEvents = {
      click: function(e) {
        if (self.date === 'year') {
          self.year = e.name
          self.month = '12'
          self.profit('%Y-%m')
        } else if (self.date === 'month') {
          self.year = e.name.split('-')[0]
          self.month = e.name.split('-')[1]
          self.day = '31'
          self.profit('%Y-%m-%d')
        }
      }
    }
    return {
      chartData: {},
      chartSettings: {},
      htmlTitle: '储物柜资金流水统计',
      date: 'day',
      year: myDate.getFullYear(),
      month: myDate.getMonth() + 1,
      day: myDate.getDate(),
      total: 0
    }
  },
  created() {
    this.profit('%Y-%m-%d')
  },
  methods: {
    profit(ymd) {
      if (ymd === '%Y-%m-%d') {
        this.date = 'day'
      } else if (ymd === '%Y-%m') {
        this.date = 'month'
      } else {
        this.date = 'year'
      }
      profitstati({
        ymd: ymd,
        date: this.date,
        year: this.year,
        month: this.month,
        day: this.day
      }).then(response => {
        this.chartData = response.data.data.statVo
        this.total = response.data.data.total
        this.chartSettings = {
          labelMap: {
            'amount': '元'
          }
        }
      })
    },
    reset() {
      var self = this
      self.year = myDate.getFullYear()
      self.month = myDate.getMonth() + 1
      self.day = myDate.getDate()
      self.profit('%Y-%m-%d')
    }
  }
}
</script>

