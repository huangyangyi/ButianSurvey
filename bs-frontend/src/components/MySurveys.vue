<template>
  <div>
    <div v-if="false" :style="{ margin: '16px 0', padding: '24px', minHeight: '360px' }">
      <h2>最近浏览</h2>
      <a-row :gutter="32">
        <a-col :span="6">
          <a-card hoverable>
            <img slot="cover" style="height: 200px" alt="example" src="../temp/professionals.jpg" />
            <template slot="actions" class="ant-card-actions">
              <a-icon key="setting" type="setting" />
              <a-icon key="edit" type="edit" />
              <a-icon key="ellipsis" type="ellipsis" />
            </template>
            <a-card-meta title="浙江大学返校申请" description="同学们，我们求是园见" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card hoverable>
            <img slot="cover" style="height: 200px" alt="example" src="../temp/caolou.jpg" />
            <template slot="actions" class="ant-card-actions">
              <a-icon key="setting" type="setting" />
              <a-icon key="edit" type="edit" />
              <a-icon key="ellipsis" type="ellipsis" />
            </template>
            <a-card-meta title="计算机学院健康打卡" description="太过分啦" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card hoverable>
            <img slot="cover" style="height: 200px" alt="example" src="../temp/zjg.jpg" />
            <template slot="actions" class="ant-card-actions">
              <a-icon key="setting" type="setting" />
              <a-icon key="edit" type="edit" />
              <a-icon key="ellipsis" type="ellipsis" />
            </template>
            <a-card-meta title="返校管理意见调查" description="不样出校门" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card hoverable>
            <img slot="cover" style="height: 200px" alt="example" src="../temp/yq.jpeg" />
            <template slot="actions" class="ant-card-actions">
              <a-icon key="setting" type="setting" />
              <a-icon key="edit" type="edit" />
              <a-icon key="ellipsis" type="ellipsis" />
            </template>
            <a-card-meta title="春学期网课反馈问卷" description="什么，已经过去半个学期了" />
          </a-card>
        </a-col>
      </a-row>
    </div>
    <div :style="{margin: '16px 0', width: '30%', padding: '0 24px'}">
      <a-input-search placeholder="input search text" enter-button @search="onSearch" />
    </div>

    <div :style="{margin: '16px 0', padding:'0 24px'}">
      <a-table :columns="columns" :data-source="tableData">
        <span slot="action" slot-scope="record">
          <a v-on:click="deleteQn(record.id)">Delete</a>
          <a-divider type="vertical" />
          <a :href="'/survey/view/'+ record.id ">Get Link</a>
          <a-divider type="vertical" />
          <a :href="'/survey/edit/'+ record.id " >Manage</a>
        </span>
      </a-table>
    </div>
  </div>
</template>
<script lang='ts'>
import Vue from "vue";
import axios from 'axios'
const columns = [
  {
    title: "问卷标题",
    dataIndex: "title",
    key: "title"
  },
  {
    title: "截止日期",
    dataIndex: "endTime",
    key: "endTime"
  },
  {
    title: "Action",
    key: "action",
    scopedSlots: { customRender: "action" }
  }
];

export default Vue.extend({
  name: "MySurveys",
  data() {
    return {
      collapsed: false,
      columns,
      tableData: []
    };
  },
  methods: {
    refreshUserQn() {
      axios.get('/api/qn/get_user_qn/'+localStorage.getItem('userName')).then(response => {
      this.tableData = response.data.result;
      console.log(response.data.result)
    }).catch(err=>{
        this.$message.error('载入问卷信息失败' + err);
      });
    },
    deleteQn(qnId) {
      axios.delete('/api/qn/delete/'+qnId).then(response => {
        this.$message.success('删除问卷成功');
        this.refreshUserQn();
    }).catch(err=>{
        this.$message.error('删除问卷失败' + err);
      });
    }
  },
  beforeCreate() {
    axios.get('/api/qn/get_user_qn/'+localStorage.getItem('userName')).then(response => {
      this.tableData = response.data.result;
      console.log(response.data.result)
    }).catch(err=>{
        this.$message.error('载入问卷信息失败' + err);
      });
  }
});
</script>

<style>
#components-layout-demo-side .logo {
  height: 32px;
  background-image: url(../assets/butiansurvey.png);
  background-size: cover;
  margin: 16px;
}
</style>
