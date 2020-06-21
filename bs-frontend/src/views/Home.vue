<template>
  <div class="home">
   <a-layout id="components-layout-demo-top" class="layout" style="min-height:100vh">
    <a-layout-header style="background-color:white;" >
    <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
        <a-col :span="12">
      <a-menu
        theme="light"
        :title="'ButianSurvey'"
        mode="horizontal"
        :default-selected-keys="['1']"
        :style="{ lineHeight: '64px', float: 'left' }"
      >
      
        <a-menu-item key="1">
          我的问卷
        </a-menu-item>
        <a-menu-item v-on:click="$router.push('/survey/edit')" key="2">
          新建问卷
        </a-menu-item>
      </a-menu>
        </a-col>
        <a-col :span="12">
          <a-button style="float:right; margin: 16px;" v-on:click="logout">登出</a-button>
          <div style="float:right">{{username}}，欢迎您！</div>
        </a-col>
      </a-row> 
    </a-layout-header>
        <a-layout-content style="margin:0 32px">
          <my-surveys style=""/>
        </a-layout-content>
          <a-layout-footer style="text-align: center">Butian Survey - Created by Yangyi Huang</a-layout-footer>
      </a-layout>
  </div>
</template>

<script lang='ts'>
import '@/assets/my-assets.css'
// @ is an alias to /src
import MySurveys from '@/components/MySurveys.vue';
export default {
  beforeCreate() {
    this.$router.push('home');
  },
  data () {
    return {
      username: localStorage.getItem('userName')
    }
  },
  name: 'Home',
  components: {
    'my-surveys': MySurveys,
  },
  methods: {
    logout() {
      this.$message.success('登出成功!')
      localStorage.removeItem('userName')
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
  }
};
</script>

<style scoped>
#.logo {
  height: 50px;
  width: 230px;
  background-image: url(../assets/butian.png);
  background-size: cover;
  margin: 7px 8px 7px 0;
  float: left;
}
</style>
