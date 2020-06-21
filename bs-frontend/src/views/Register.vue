<template>
  <div class="register">
    <a-layout >
      <a-affix :offset-top="top">
        <a-layout-header class="header" style="background: #fff; padding: 0">
          <a-page-header
            style="border: 1px solid rgb(235, 237, 240)"
            title="ButianSurvey - 注册界面"
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
    id="components-form-demo-normal-register"
    :form="form"
    class="register-form"
    @submit="handleSubmit"
  >
    <a-form-item>
      <a-input
        v-decorator="[
          'userName',
          { rules: [{ required: true, min: 6, message: '请输入长度不少于6个字符的用户名!' }, 
          {pattern: '^[a-zA-Z0-9_-]+', message: '用户名只能由英文字母、数字、下划线组成'}] },
        ]"
        placeholder="用户名"
      >
        <a-icon slot="prefix" type="user" style="color: rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item>
      <a-input
        v-decorator="[
          'userEmail',
          { rules: [{ required: true, message: '请输入邮箱地址!' },
          {pattern: '^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$', message: '邮箱格式不正确'}],
          }
        ]"
        placeholder="邮箱"
      >
        <a-icon slot="prefix" type="mail" style="color: rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item>
      <a-input
        v-decorator="[
          'passwd',
          { rules: [{ required: true, min:6, message: '请输入长度不少于6个字符的密码!' }] },
        ]"
        type="password"
        placeholder="密码"
      >
        <a-icon slot="prefix" type="lock" style="color: rgba(0,0,0,.25)" />
      </a-input>
    </a-form-item>
    <a-form-item>
      <a-button type="primary" html-type="submit" class="register-form-button">
        注册
      </a-button>
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
  name: 'Register',
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: 'normal_register' });
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          axios.post('/api/users/register', values).then(response=> {
            if (response.data.status=='success') {
              let token:string = response.data.result;
              this.$message.success('注册成功！正在跳转……');
              this.$router.push({path: '/login'});
            }
            else this.$message.error('注册失败' + response.data.result)
          }).catch(err=>{
            this.$message.error('注册失败' + err);
          });
        }
        else this.$message.error('注册失败');
        
      });
    },
  },
})
</script>

<style>
  body {
    background: rgb(153,217,234)
  }
  .register-panel {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    margin: auto;
  }
  #components-form-demo-normal-register .register-form {
  max-width: 300px;
}
#components-form-demo-normal-register .register-form-forgot {
  float: right;
}
#components-form-demo-normal-register .register-form-button {
  width: 100%;
}
</style>