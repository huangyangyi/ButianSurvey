<template>
  <div class="problem"
      v-bind:value="value">
    <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
      <a-col :span="1" style="font-size: 20px; font-weight: bold">{{index}}</a-col>
      <a-col :span="1">
        <a-divider type="vertical" style=" font-size:20px;" />
      </a-col>
      <a-col :span="10">
        <div v-if="isEditTitle==false" style="font-size:20px; float:left; margin: 0 8px 0 0;"> 
          {{value.title}} 
          <a-icon type="edit" style="cursor: pointer; margin:auto 0;" v-on:click="onClickEditTitle"/>
        </div>
        <a-input style="font-size:20px; border:none" ref="titleInput" v-on:blur="onLeaveEditTitle" v-on:pressEnter="onLeaveEditTitle"  v-model="local_value.title" v-if="isEditTitle"/>
      </a-col>
      <a-col :span="8">
        <a-select default-value="single_choice" style="float: right;width:100%;min-width: 80px;max-width: 120px" v-model="local_value.type" v-on:change="onChangeType">
          <a-select-option value="single_choice">单项选择</a-select-option>
          <a-select-option value="multi_choice">多项选择</a-select-option>
          <a-select-option value="fill_in_blank">填空</a-select-option>
          <a-select-option value="rate">评分</a-select-option>
          <a-select-option value="multi_level_choice">级联选择</a-select-option>
          <a-select-option value="numeral">数值</a-select-option>
        </a-select>
      </a-col>
      <a-col :span="4">
        <a-tooltip title="设置必填问题" style="float: right">
        <a-switch checked-children="必" un-checked-children="选" v-model="local_value.mustFill"/>
        </a-tooltip>
      </a-col>
      <a-col :span="24">
        <a-textarea v-if="hasDescription||addedDescription" placeholder="请输入问题说明" class="my-textarea my-textarea-problem" style="width: 100%;" autosize v-model="local_value.description"/>
      </a-col>
      <a-col :span="24">
        <!--Different contents for different type of problems-->

        <!--Single Choice-->
        <div class="single_choice" v-if="value.type==='single_choice'">
          <Container @drop="onDropOptions" orientation="vertical" behaviour="contain" drag-handle-selector=".column-drag-handle">
            <Draggable v-for="(item,index) in value.content.options" :key="index">
              <div class="option">       
                <a-row type="flex" justify="center" align="middle">
                  <a-col :span='1'>
                    <span class="column-drag-handle"> &#x2630; </span>
                  </a-col>
                  <a-col :span='1'>
                    {{index+1}}. 
                  </a-col>
                  <a-col :span='21'>
                      <a-input placeholder="请输入选项描述" v-model="local_value.content.options[index].value"/>
                  </a-col>
                  <a-col :span='1'>
                    <a-icon style="right:0; cursor: pointer" type="close" v-on:click="onDelOption(index)"/>
                  </a-col>
                </a-row>
              </div>
            </Draggable>
          </Container>
            <a-button type="link" icon="plus-circle" style="width: 16%; left:42%" v-on:click="onAddOption" >新增选项</a-button>
        </div>

        <!--Multi Choice-->
        <div class="multi_choice" v-if="value.type==='multi_choice'">
          <div >
          选择数量限制: 
          <a-input-number style="margin:0 16px 16px 16px" v-model="local_value.content.minLimit" :min="0" :max="value.content.options.length" />
          至
          <a-input-number style="margin:0 16px 16px 16px" v-model="local_value.content.maxLimit" :min="1" :max="value.content.options.length" />
          个
          </div>
          <Container @drop="onDropOptions" orientation="vertical" behaviour="contain" drag-handle-selector=".column-drag-handle">
            <Draggable v-for="(item,index) in value.content.options" :key="index">
              <div class="option" :hoverable="true">       
                <a-row type="flex" justify="center" align="middle">
                  <a-col :span='1'>
                    <span class="column-drag-handle"> &#x2630; </span>
                  </a-col>
                  <a-col :span='1'>
                    {{index+1}}. 
                  </a-col>
                  <a-col :span='21'>
                      <a-input placeholder="请输入选项描述" v-model="local_value.content.options[index].value"/>
                  </a-col>
                  <a-col :span='1'>
                    <a-icon style="right:0; cursor: pointer" type="delete" v-on:click="onDelOption(index)"/>
                  </a-col>
                </a-row>
              </div>
            </Draggable>
          </Container>
            <a-button type="primary" icon="plus-circle" style="margin: 16px 0 0 0; width: 16%; left:42%" v-on:click="onAddOption" >新增选项</a-button>
        </div>
        
        <!--Fill in the Blank-->
        <div class="fill_in_blank" v-if="value.type==='fill_in_blank'">
        <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
        <a-col :span="24" >
          字符限制:<a-input-number style="margin:0 16px"  v-model="local_value.content.wordLimit" :max="10000"/>(< 10000)
        </a-col>
        <a-col :span="24">
          填空类型：
          <a-radio-group v-model="local_value.content.isMultiLine">
            <a-radio-button :value="false">单行文本</a-radio-button>
            <a-radio-button :value="true">多行文本</a-radio-button>
          </a-radio-group>
        </a-col>
        <a-col v-if="value.content.isMultiLine" :span="24" style="text-align: right">
          <a-textarea placeholder="输入框预览" disabled v-model="tmp_text" allow-clear :maxLength="value.content.wordLimit" />
          {{tmp_text.length}}/{{value.content.wordLimit}}
        </a-col>
        <a-col v-if="!value.content.isMultiLine" :span="24" style="text-align: right">
          <a-input placeholder="输入框预览" disabled v-model="tmp_text" :maxLength="value.content.wordLimit" />
        </a-col>
        </a-row>
        </div>

        <!--rate-->
        <div class="rate" v-if="value.type==='rate'">
          <a-row type="flex" justify="center" align="middle" :gutter="[16,16]">
          <a-col :span="24">
          评分单位：
          <a-radio-group v-model="local_value.content.isHalf">
            <a-radio-button :value="false">1</a-radio-button>
            <a-radio-button :value="true">0.5</a-radio-button>
          </a-radio-group>
          </a-col>
          <a-col :span="24">
          评分图标：
           <a-radio-group v-model="local_value.content.style">
            <a-radio-button value="star"><a-icon type="star" /></a-radio-button>
            <a-radio-button value="heart"><a-icon type="heart" /></a-radio-button>
            <a-radio-button value="smile"><a-icon type="smile" /></a-radio-button>
            <a-radio-button value="like"><a-icon type="like" /></a-radio-button>
          </a-radio-group>
          </a-col>
          </a-row>
        </div>

        <!--Multi Level-->
        <div class="multi_level_choice" v-if="value.type==='multi_level_choice'">
          Multi-level Choice not implemented!
        </div>

        <!--numeral-->
        <div class="numeral" v-if="value.type==='numeral'">
          <a-row type="flex" justify="left" align="middle" :gutter="[16,16]">
          <a-col :span="24">
            数值类型：
            <a-radio-group v-model="local_value.content.numType" >
              <a-radio-button value="integral">整数</a-radio-button>
              <a-radio-button value="floating">浮点数</a-radio-button>
            </a-radio-group>
          </a-col>
          <a-col :span="8">
            最小值：
            <a-input-number style="margin:0 16px 16px 16px" v-model="local_value.content.min"/>
          </a-col>
          <a-col :span="8">
            最大值
            <a-input-number style="margin:0 16px 16px 16px" v-model="local_value.content.max"/>
          </a-col>
          </a-row>
        </div>
      </a-col>
      
      <a-col v-if="value.dependencies[0]>=0" :span="6">
        依赖题目
        <a-select v-model="local_value.dependencies[0]" style= "width:80px">
          <a-select-option :key="0">无</a-select-option>
          <a-select-option 
            v-for="i in iAllSingleChoiceExcept(value.index)" 
            :key="allData[i].index">
            第{{i+1}}题
          </a-select-option>
        </a-select>
      </a-col>
      <a-col v-if="value.dependencies[0]>=0" :span="6">
        选项
        <a-select v-model="local_value.dependencies[1]" style= "width:80px">
          <a-select-option :key="0">无</a-select-option>
          <a-select-option 
            v-for="(item, index) in findProblemOptions(value.dependencies[0])" 
            :key="item.index">
            {{index+1}}.
          </a-select-option>
        </a-select>
      </a-col>
      <a-col v-if="value.dependencies[0]>=0" :span="4">
        <a-button type="link" icon="close-circle" v-on:click="value.dependencies=[-1, -1]">
          取消依赖
        </a-button>
      </a-col>
      </a-col>
      <a-col v-if="value.dependencies[0]<0" :span="16"></a-col>
      <a-col :span="8">
        <a-icon style="float:right; cursor: pointer; margin-left: 8px; font-size: 20px;" type="delete" v-on:click="onDeleteProblem"/>
        <template>
        <a-dropdown style="float:right; cursor: pointer; margin-left: 8px">
          <a class="ant-dropdown-link" @click="e => e.preventDefault()">
              <a-icon type="ellipsis" style="font-size: 20px;" />
          </a>
          <a-menu slot="overlay">
            <a-menu-item v-if="value.dependencies[0]<0" v-on:click="value.dependencies=[0, 0]">
              添加级联依赖
            </a-menu-item>
            <a-menu-item v-if="!(hasDescription||addedDescription)" v-on:click="addDescription">
              添加问题说明
            </a-menu-item>
          </a-menu>
        </a-dropdown>
      </template>
      </a-col>
    </a-row>
  </div>
</template>

<script lang='ts'>
declare module "vue-smooth-dnd";
import Vue from "vue";
import { applyDrag, getMaxInd } from "../utils/helpers";
import { Container, Draggable, DropResult } from "vue-smooth-dnd";

import '@/assets/my-assets.css'

export default Vue.extend({
  name: "ProblemDesign",
  components:{Container, Draggable} ,
  props: [
    'value',
    'index',
    'allData',
  ],
  data() {
    return {
      local_value: this.value,
      message: '123',
      tmp_text: '',
      isEditTitle: false,
      addedDescription: false
    };
  },
  methods: {
    onDropOptions(dropResult: DropResult) {
      this.value.content.options = applyDrag(this.value.content.options, dropResult);
    },
    onAddOption(){
      this.value.content.options.push({
        index: getMaxInd(this.value.content.options),
        value: "",
      });
    },
    onDelOption(index:number){
      if (this.value.content.options.length <= 2)
        this.$message.error('请在单项选择中设置至少两个选项~');
      else
        this.value.content.options.splice(index, 1);
    },
    onChangeType(){
      let options = [{index:1, value:""}, {index:2, value:""}];
      if ("options" in this.value.content)
        options = this.value.content.options;
      switch (this.value.type) {
        case 'single_choice':
          this.value.content = {options: options, };   
          break;
        case 'multi_choice':
          this.value.content = {minLimit: 0, maxLimit: 2, options: options, };   
          break;
        case 'fill_in_blank':
          this.value.content = {isMultiLine: false, wordLimit: 200}
          break;
        case 'rate':
          this.value.content = {isHalf: false, style: "star"}
          break;
        case 'numeral':
          this.value.content = {min: 0, max: 100, numType:'integral'}
          break;
        default:
          console.log(this.value.type + 'not implemented')
          this.value.content = {}
          break;
      }
    },
    onClickEditTitle(){
      this.isEditTitle=true;
    },
    onLeaveEditTitle(){
      this.isEditTitle=false;
    },
    onDeleteProblem(){
      this.$emit('delete');
    },
    addDescription() {
      this.value.description='';
      this.addedDescription=true;
      this.$emit('input', this.value);
    }
   },
  computed: {
    iAllSingleChoiceExcept() {
      return (index: number) => {
        let indices: number[] = [];
        for (var i in this.allData) {
          if (this.allData[i].type=='single_choice' && this.allData[i].index!=index){
            indices.push(parseInt(i));
          }
        }
        return indices;
      }
    },
    findProblemOptions() {
     return (index: number) => {
      let p = this.allData.find(
        (problem: {index: number}) => problem.index == index
      );
      if (typeof(p) != 'undefined' && 'options' in p.content) return p.content.options;
      else return [];
     }
    },
    hasDescription() {
      this.value.description!=null;
    }
  },
    watch: {
      local_value: { 
        deep: true,
        immediate: true,
        handler(newValue) {
          this.local_value = newValue
          if (newValue.description!=null) this.addedDescription=true;
          this.$emit('input', newValue)
        }
      },
      value: {
        deep: true,
        immediate: true,
        handler(newValue) {
          this.local_value = newValue
        }
      }
    }
});
</script>

<style>
.problem {
  min-height: 100px;
  background: white;
  padding: 24px 32px;
  cursor: move;
  user-select: none;
}
.option {
    height: 50px;
    line-height: 50px;
    text-align: center;
    display: block;
    background-color: #fff;
    border: 1px solid rgba(0, 0, 0, .125);
    outline: 0;
    margin-bottom: 2px;
    margin-top: 2px;
    cursor: default;
    user-select: none;
}
.option:hover {
    background-color: #87CEFA20;
}
.column-drag-handle {
    cursor: move;
    padding: 5px;
}
.my-textarea-problem {
    border-bottom-color: aquamarine;
    border-bottom-width: 1px;
    border-bottom-style: solid;
    color: rgba(0, 0, 0, 0.65);
}
</style>