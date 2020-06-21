<template>
  <div class="login">
    <a-layout >
      <a-affix :offset-top="top">
        <a-layout-header class="header" style="background: #fff; padding: 0">
          <a-page-header
            style="border: 1px solid rgb(235, 237, 240)"
            title="ButianSurvey - 登录界面"
            :avatar="logoData"
          >
            <template slot="title">
              
            </template>
          </a-page-header>
        </a-layout-header>
      </a-affix>
      <a-layout-content style="background: rgb(153,217,234)">
        <div class="basic_box" style="margin:32px auto;padding:32px; max-width: 550px;">
        
        <a-form
    id="components-form-demo-normal-login"
    :form="form"
    class="login-form"
    @submit="handleSubmit"
  >
    <a-form-item>
      <a-input
        v-decorator="[
          'userName',
          { rules: [{ required: true, message: '请输入用户名!' }] },
        ]"
        placeholder="用户名"
      >
        <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item>
      <a-input
        v-decorator="[
          'passwd',
          { rules: [{ required: true, message: '请输入密码!' }] },
        ]"
        type="password"
        placeholder="密码"
      >
        <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item>
      
      <a-button type="primary" html-type="submit" class="login-form-button">
        Log in
      </a-button>
      没有账户？
      <a href="../register">
        现在注册！
      </a>
    </a-form-item>
  </a-form>
        </div>
        </a-layout-content>
      <a-layout-footer
        style="text-align: center;background-color: rgb(153,217,234)"
      >Butian Survey - Created by Yangyi Huang
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import '@/assets/my-assets.css'
import axios from 'axios'
export default Vue.extend({
  name: 'Login',
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'normal_login' });
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          axios.post('/api/users/login', values).then(response=> {
            if (response.data.status=='success') {
              let token:string = response.data.result;
              localStorage.setItem('token', token);
              localStorage.setItem('userName', values.userName)
              this.$message.success('登录成功！正在跳转……');
              this.$router.push({path: 'home'});
            }
            else this.$message.error('登陆失败' + response.data.result)
          }).catch(err=>{
            this.$message.error('登录失败' + err);
          });
        }
        else this.$message.error(err);
        
      });
    },
  },
})
</script>

<style>
  body {
    background: rgb(153,217,234)
  }
  .login-panel {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: auto;
  }
  #components-form-demo-normal-login .login-form {
  max-width: 300px;
}
#components-form-demo-normal-login .login-form-forgot {
  float: right;
}
#components-form-demo-normal-login .login-form-button {
  width: 100%;
}
</style>