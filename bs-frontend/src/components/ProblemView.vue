<template>
  <div class="problem_view"
    v-bind:value="value">
    <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
      <a-col :span="1" style="font-size: 20px; font-weight: bold">{{index}}</a-col>
      <a-col :span="1">
        <a-divider type="vertical" style=" font-size:20px;" />
      </a-col>
      <a-col :span="22" style="font-size: 16px; position:relative;">
        <div style="float:left; font-size:20px; bottom:0px;"> 
          {{problem.title}}
        </div>
        <div style="float:left; font-size:14px; color:red; bottom:0px;">（必填）</div>
      </a-col>
       <a-col :span="24" style="font-size:16px;">
         {{problem.description}} 
       </a-col>
       <a-col :span="23" :offset="1" >
        <!--Different contents for different type of problems-->
          
        <!--Single Choice-->
        <div class="single_choice" v-if="problem.type==='single_choice'">
          <a-radio-group :disabled="disabled" v-model="local_value" v-on:change="onChangeRadio">
            <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
            <a-col :span="24" v-for="(item, index) in problem.content.options" :key="item.index" >
            <a-radio :value="item.index" :default-value="0">
              {{item.value}}
            </a-radio>
            </a-col>
            </a-row>
          </a-radio-group>
        </div>

        <!--Multi Choice-->
        <div class="multi_choice" v-if="problem.type==='multi_choice'">
          <a-col :span="24">
          <div style="font-size:14px; color:blue;">请选择{{problem.content.minLimit}}至{{problem.content.maxLimit}}个选项</div>
          </a-col>
          <a-col :span="24">
            <a-checkbox-group :disabled="disabled" v-model="local_value">
            <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
            <a-col :span="24" v-for="(item, index) in problem.content.options" :key="item.index" >
            <a-checkbox :value="item.index" :default-value="0">
              {{item.value}}
            </a-checkbox>
            </a-col>
            </a-row>
          </a-checkbox-group>
          </a-col>
        </div>
        
        <!--Fill in the Blank-->
        <div class="fill_in_blank" v-if="problem.type==='fill_in_blank'">
        <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
          <a-col v-if="problem.content.isMultiLine" :span="23"  style="text-align: right">
          <a-textarea autosize placeholder="请输入文本" :disabled="disabled" v-model="local_value" allow-clear :maxLength="problem.content.wordLimit" />
          {{value?value.length:0}}/{{problem.content.wordLimit}}
          </a-col>
          <a-col v-if="!problem.content.isMultiLine" :span="23" style="text-align: right">
            <a-input placeholder="请输入文本" :maxLength="problem.content.wordLimit" :disabled="disabled" v-model="local_value" />
          </a-col>
          <a-col :span="1"/>
        </a-row>
        </div>

        <!--rate-->
        <div class="rate" v-if="problem.type==='rate'">
          <a-rate :allow-half="problem.content.isHalf" :disabled="disabled" v-model="local_value">
            <a-icon slot="character" filled :type="problem.content.style"/>
          </a-rate>
        </div>

        <!--Multi Level-->
        <div class="multi_level_choice" v-if="problem.type==='multi_level_choice'" >
          Multi-level Choice not implemented!
        </div>

        <!--numeral-->
        <div class="numeral" v-if="problem.type==='numeral'">
          <div style="float:left; font-size:14px; padding:4px; color:blue;">
            请输入 {{problem.content.min}} 至 {{problem.content.max}} 范围内的
            {{problem.content.numType == 'integral' ? '整数' : ''}}
            {{problem.content.numType == 'floating' ? '小数' : ''}}
            <a-divider type="vertical"/>
          </div>
          <a-input-number :disabled="disabled" v-model="local_value" style="width: 30%; float:left" :max="problem.content.maxLimit" :min="problem.content.minLimit" />
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
export default Vue.extend({
    name: "ProblemView",
    props: [
      'value',
      'index',
      'problem',
      'disabled'
    ],
    data() {
      return {
        local_value: this.value
      };
    },
    methods: {
      onChangeRadio() {
        this.$emit('change-problem-option', this.local_value);
      } 
    },
    watch: {
      local_value(newValue) {
        this.local_value = newValue
        this.$emit('input', newValue)
      }
    }
})
</script>

<style>
.problem_view {
  min-height: 100px;
  padding: 24px 32px;
  user-select: none;
}

</style>