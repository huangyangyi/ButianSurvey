<template>
  <div class="survey-edit">
    <a-layout style="min-height: 100vh">
      <a-affix :offset-top="top">
        <a-layout-header class="header" style="background: #fff; padding: 0">
          <a-page-header
            style="border: 1px solid rgb(235, 237, 240)"
            :title="'ButianSurvey - '+qn.title"
            sub-title="问卷管理界面"
            @back="$router.back()"
          >
            <template slot="extra">
              <a-button type="primary" v-on:click="submitData">保存</a-button>
              <a-button type="danger">删除</a-button>
            </template>
          </a-page-header>
        </a-layout-header>
      </a-affix>
      <a-layout-content style="background: rgb(153,217,234)">
        <div style="max-width:770px; margin:32px auto; padding:0 8px; min-height: 80vh">
          <a-row :gutter="[0, 16]">
          <a-col :span='24'>
            <a-menu class="basic_box" v-model="surveyMenu" mode="horizontal" align="center">
              <a-menu-item key="edit">
                <a-icon type="edit" />编辑
              </a-menu-item>
              <a-menu-item key="preview">
                <a-icon type="eye" />预览
              </a-menu-item>
              <a-menu-item key="reply">
                <a-icon type="message" />回复
              </a-menu-item>
              <a-menu-item key="setting">
                <a-icon type="setting" />设定
              </a-menu-item>
            </a-menu>
          </a-col>
          
          <div v-if="this.qn!=null && surveyMenu[0]=='edit'" >
          <a-col :span='24'>
            <div class="cover basic_box" style="padding:40px;">
            <div style="margin: auto">
            <a-textarea placeholder="请输入问卷标题" v-model="qn.title" class="title my-textarea" autosize/>
            </div>
            <a-textarea placeholder="请输入问卷描述" v-model="qn.description" class="description my-textarea" autosize />
            </div>
          </a-col>
          <a-col :span='24' >
            <drag-container @drop="onDropProblems" orientation="vertical" behaviour="contain">
            <draggable v-for="(item, index) in qn.questions" :key="index" style="margin: 0 0 16px 0">
              <problem-design class="basic_box" v-model="qn.questions[index]" :index="prettyIndex(index)" v-on:delete="removeProblem(index)" :allData="qn.questions"/>
            </draggable>
            </drag-container>
          </a-col>
          <a-col :span='24' style="left:380px">
            <a-icon type="plus-circle" theme="filled" 
            style="font-size:32px; color: #1890ff; " 
            v-on:click="addNewProblem"/>
          </a-col>
          </div>


          <a-col :span='24'>
          <div v-if="surveyMenu[0]=='preview'" class="basic_box">
          <a-row>
          <a-col :span='24'>
          <div class="cover-preview cover" style="padding:40px;">
            <div style="margin: auto">
              <div class="title">{{qn.title}}</div>
              <div class="description">{{qn.description}}</div>
            </div>
          </div>
          </a-col>
          <a-col :span='24'>
          <div style="padding-bottom: 8px; width: 100%">
            <div v-for="(item, i) in qn.questions" :key="item.index">
              <a-divider dashed v-if="i>0" style="margin-bottom: 0"/>
              <problem-view :problem="item" :index="prettyIndex(i)"/>
            </div>
          <div style="margin: 8px auto" class="flex-centering">
            <a-button  type="primary" disabled>提交</a-button>
          </div>
          </div>
          </a-col>
          </a-row>
          </div>
          </a-col>
          <a-col :span='24'>
            <div v-if="surveyMenu[0]=='reply'">
              <div class="basic_box" style="padding:16px"> 
                <center>
              <div style="font-size:20px; margin: 8px auto; font-weight:bolder">
                总计提交：{{replyData.length}}
              </div>
                </center>
                <a-table :pagination="pagination" :columns="reply_columns" size="small" :data-source="replyData">
                  <span slot="action" slot-scope="text, record, index">
                    <a :href="'../../reply/'+record.id">查看答卷</a>
                  </span>
                </a-table>
              </div>
            </div>
          </a-col>
          <a-col :span='24'>
            <div v-if="surveyMenu[0]=='setting'">
              <div class="basic_box" style="padding:32px"> 
             <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
              <a-col :span="24">
                开放日期：<a-range-picker  
                          style="margin:0 8px;"
                          @change="onChangeDateRange"
                          />
              </a-col>
              <a-col :span="24">
                填写权限：
                <a-radio-group name="radioGroup" @change="onChangeRequireLogin" :default-value="qn.requireLogin">
                  <a-radio :value="false">所有人</a-radio>
                  <a-radio :value="true">注册用户</a-radio>
                </a-radio-group>
              </a-col>
              <a-col :span="12">
                填写次数限制：<a-input-number v-model="qn.submitLimit" v-if="!submitNoLimit"/>
              </a-col>
              <a-col :span="12">
                无限制：<a-checkbox @change="onChangeSubmitNoLimit"/>
              </a-col>
             </a-row>
              </div>
            </div>
          </a-col>
          </a-row>
          </a-row>
        </div>
      </a-layout-content>
      <a-layout-footer
        style="text-align: center;background-color: rgb(153,217,234)"
      >Butian Survey - Created by Yangyi Huang</a-layout-footer>
    </a-layout>
  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import ProblemDesign from '@/components/ProblemDesign.vue';
import ProblemView from '@/components/ProblemView.vue';
import { applyDrag, getMaxInd } from "../utils/helpers";
import { Container, Draggable, DropResult } from "vue-smooth-dnd";
import axios from 'axios'

import '@/assets/my-assets.css'

const logoData = {
};


export default Vue.extend({
  name: 'SurveyEdit',
  components: {
    'problem-design': ProblemDesign,
    'drag-container': Container,
    'draggable': Draggable,
    'problem-view': ProblemView,
  },
  data() {
    return {
       pagination: {
        pageSize: 10,//每页中显示10条数据
       },
      timerange: [],
      timerange1: [],
      submitLimit: -1,
      submitNoLimit: true,
      logoData,
      surveyMenu: ['edit'],
      reply_columns: [
        {
          title: "用户名",
          dataIndex: "userName",
          key: "userName",
          customRender: (text, record, index) => {
            return record.userName!=null?text:"匿名"
          }
        },
        {
          title: "提交时间",
          dataIndex: "createdDate",
          key: "createdDate"
        },
        {
          title: "Action",
          key: "action",
          scopedSlots: { customRender: "action" }
        },
      ],
      qn: {
        title: '',
        description: '',
        createdBy: localStorage.getItem('userName'),
        startTime: new Date(),
        endTime: new Date(),
        displayMode: "default",
        surveyMode: "open",
        requireLogin: false,
        submitLimit: -1,
        section: [],
        questions: [
        {
          index: 1,
          mustFill: true,
          type: 'single_choice',
          title: '问题1',
          dependencies: [-1, -1],
          description: null,
          content: {
            options: [{index: '1', value: ''},{index: '2', value: ''}],
          }
        }
      ]
      },
    };
  },
  methods: {
    onDropProblems(dropResult: DropResult) {
      this.qn.questions = applyDrag(this.qn.questions, dropResult);
    },
    prettyIndex (index: number):string {
      var new_ind = (index+1).toString();
      var diff:number = (this.qn.questions.length + 1).toString().length - new_ind.length;
      diff = diff < 2 ? diff + 1 : diff;
      new_ind = '0'.repeat(diff) + new_ind;
      return new_ind;
    },
    addNewProblem(){
      this.qn.questions.push({
        mustFill: true,
        index: getMaxInd(this.qn.questions),
        type: 'single_choice',
        title: '问题'+getMaxInd(this.qn.questions),
        dependencies: [-1, -1],
        description: null,
        content: {
          options: [{index: 1, value: ''}, {index: 2, value: ''}],
        }
      })
    },
    removeProblem(index:number){
      this.qn.questions.splice(index, 1);
    },
    submitData(){
      console.log(JSON.stringify(this.qn));
      axios.post('/api/qn/save', this.qn).then(response => {
        this.qn = response.data.result;
        console.log(this.qn.id)
        console.log(this.qn.questions[0])
        this.$router.push({path: '/survey/edit/'+this.qn.id})
        this.$message.success('保存成功！');
      }).catch(err=>{
        this.$message.error('保存失败' + err);
      });
    },
    onChangeRequireLogin(event){
      console.log(event.target.value);
      this.qn.requireLogin=event.target.value;
    },
    onChangeSubmitNoLimit(event){
      console.log(event.target.checked);
      console.log(this.qn.submitLimit);
      this.submitNoLimit = event.target.checked
      if (event.target.checked) this.qn.submitLimit=-1;
    },
    onChangeDateRange(event) {
      let newDate = event
      this.qn.startTime = newDate[0];
      this.qn.endTime = newDate[1];
    }
  },
  created() {
    let id = this.$route.params.qnId;
    if (id) {
      axios.get('/api/qn/get/'+id).then(response => {
        if (response.data.result == null)
            throw new Error("问卷不存在");
        if (response.data.result.createdBy!==localStorage.getItem('userName')) {
            throw new Error("用户无权限");
        }
        this.qn = response.data.result;
        console.log(this.timerange)
      }).catch((err:Error)=>{
        this.$message.error(err.message);
        this.$router.push({path: '/survey/edit/'})
      });
      axios.get('/api/qn/get_qn_replies/'+id).then(response => {
        this.replyData = response.data.result
      })
    }
  },
});
</script>

<style>
.logo {
  height: 50px;
  width: 230px;
  background-image: url(../assets/butiansurvey.png);
  background-size: cover;
  margin: 7px 8px 7px 0;
  float: left;
}

</style>