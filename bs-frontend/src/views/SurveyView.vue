<template>
  <div class="survey-view">
    <a-layout style="min-height: 100vh">
      <a-affix :offset-top="top">
        <a-layout-header class="header" style="background: #fff; padding: 0">
          <a-page-header
            style="border: 1px solid rgb(235, 237, 240)"
            :title="'ButianSurvey - '+qn.title"
            :sub-title="qnstatus"
            :avatar="logoData"
            @back="$router.back()"
          >
            <template slot="extra">
              <a-button style="float:right" v-if="!userName">登录</a-button>
            </template>
          </a-page-header>
        </a-layout-header>
      </a-affix>
      <a-layout-content style="background: rgb(153,217,234)">
        <div style="max-width:770px; margin:32px auto; min-height: 80vh">
          <a-row :gutter="[0, 16]">
          <a-col :span='24'>
          <div class="basic_box">
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
          <div style="padding-bottom: 8px">
            <div v-for="(item, i) in qn.questions" v-if="enable[i]"  :key="item.index">
              <a-divider dashed v-if="i>0" style="margin-bottom: 0"/>
              <problem-view :disabled="submited" :problem="item" @change-problem-option="onChangeOption(i, $event)" v-model="reply.answer[i].value" :index="prettyIndex(i)"/>
            </div>
            <div class="flex-centering">
            <a-button style="margin: 8px 0" :disabled="submited" type="primary" v-on:click="submitData">提交</a-button>
            </div>
          </div>
          </a-col>
          </a-row>
          </div>
          </a-col>
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
  src: '../assets/butian.png',
  shape: 'square',
};


export default Vue.extend({
  name: 'SurveyView',
  components: {
    'problem-design': ProblemDesign,
    'drag-container': Container,
    'draggable': Draggable,
    'problem-view': ProblemView,
  },
  data() {
    return {
      qnstatus: "正在收集",
      reply: {},
      qn: {},
      submited: false,
      dep_map: new Map(),
      enable: [],
      username: localStorage.getItem('userName') 
    };
  },
  methods: {
    prettyIndex (index: number):string {
      var new_ind = (index+1).toString();
      var diff:number = (this.qn.questions.length + 1).toString().length - new_ind.length;
      diff = diff < 2 ? diff + 1 : diff;
      new_ind = '0'.repeat(diff) + new_ind;
      return new_ind;
    },
    submitData(){
      if (this.qn.requireLogin && !this.username) {
        this.$message.error("Please Login First!");
        return ;
      }
      console.log(JSON.stringify(this.reply));
      axios.post('/api/qn/reply', this.reply).then(response => {
        console.log(response.data.result);
        if (response.data.status == 'success'){
          this.$message.success('提交成功！');
          this.submited=true;
        }
        else {
          this.$message.error('提交失败' + response.data.result);
        }
      }).catch(err=>{
        this.$message.error('提交失败' + err);
      });
    },
    onChangeOption(ind:number, value){
      ind = this.qn.questions[ind].index;
      console.log(ind)
      console.log(this.dep_map)
      if (this.dep_map.has(ind)) {
        let dep  = this.dep_map.get(ind);
        console.log(dep);
        this.$set(this.enable, parseInt(dep.index), dep.dep_value == value);
        if (dep.dep_value != value)
          this.$set(this.reply.answer, 
                    parseInt(dep.index),
                    {
                      questionId:
                        this.reply.answer[parseInt(dep.index)].questionId, 
                      value:
                        null,
                    })
      }
    },
    getUserIP(onNewIP) {
      let MyPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;
      let pc = new MyPeerConnection({
        iceServers: []
      });
      let noop = () => {
      };
      let localIPs = {};
      let ipRegex = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/g;
      let iterateIP = (ip) => {
        if (!localIPs[ip]) onNewIP(ip);
        localIPs[ip] = true;
      };
      pc.createDataChannel('');
      pc.createOffer().then((sdp) => {
        sdp.sdp.split('\n').forEach(function (line) {
          if (line.indexOf('candidate') < 0) return;
          line.match(ipRegex).forEach(iterateIP);
        });
        pc.setLocalDescription(sdp, noop, noop);
      }).catch((reason) => {
      });
      pc.onicecandidate = (ice) => {
        if (!ice || !ice.candidate || !ice.candidate.candidate || !ice.candidate.candidate.match(ipRegex)) return;
        ice.candidate.candidate.match(ipRegex).forEach(iterateIP);
      };
    },

  },
  created() {
    console.log('create');
    let id = this.$route.params.qnId;
    if (id) {
      console.log(id)
      axios.get('/api/qn/get/'+id).then(response => {
        if (response.data.result == null)
            throw new Error("问卷不存在");
        this.qn = response.data.result
        let start = new Date(this.qn.startTime)
        let end = new Date(this.qn.endTime)
        let now = new Date();
        if (start > now) this.qnstatus="尚未开始";
        else if (now > end) this.qnstatus="收集结束";
        else this.qnstatus="正在收集"
        this.reply.answer = [];
        this.getUserIP((ip) => {
          console.log(ip);
          this.reply.userIp = ip;
        });
        this.reply.userName= localStorage.getItem('userName')
        this.reply.questionnaireId = this.qn.id
        for (const index in this.qn.questions) {
          if (this.qn.questions[index].dependencies[0]!=-1){
            this.dep_map.set(this.qn.questions[index].dependencies[0], {
            dep_value: this.qn.questions[index].dependencies[1],
            index: index
            });
            this.enable.push(false);
          }
          else this.enable.push(true);
          this.reply.answer.push({questionId: this.qn.questions[index].id, value: null})
        }
      }).catch(err=>{
        this.$message.error(err.message);
        console.log(err.message)
        this.$router.push({path: '/'})
      });
    }
    let replyId = this.$route.params.replyId;
    let replyResult = {}
    if (replyId) {
      this.submited=true
      axios.get('/api/qn/get_reply/'+replyId).then(response => {
        if (response.data.result == null)
            throw new Error("答卷不存在");
        replyResult = response.data.result;
        id = replyResult.questionnaireId;
        axios.get('/api/qn/get/'+id).then(response => {
          if (response.data.result == null)
              throw new Error("问卷不存在");
          this.qn = response.data.result
          this.reply = {answer:[]}
          let ansMap = new Map();
          console.log(replyResult.answer)
          for (const answer of replyResult.answer){
            ansMap.set(answer.questionId, answer.value)
          }
          for (const index in this.qn.questions) {
            if (this.qn.questions[index].dependencies[0]!=-1){
              this.dep_map.set(this.qn.questions[index].dependencies[0], {
              dep_value: this.qn.questions[index].dependencies[1],
              index: index
              });
              this.enable.push(false);
            }
            else this.enable.push(true);
            this.reply.answer.push({questionId: this.qn.questions[index].id, value: ansMap.get(this.qn.questions[index].id)})
          }
          console.log(this.reply.answer)
        }).catch(err=>{
          this.$message.error(err.message);
          this.$router.push({path: '/'})
        });
      }).catch(err=>{
        this.$message.error(err.message);
        this.$router.push({path: '/'})
      });
    }
  },
});
</script>

<style>


</style>