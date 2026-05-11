<template>
  <view class="page" v-if="q">
    <view class="question-header">
      <view class="question-type">{{ getQuestionTypeName(q.questionType) }}</view>
    </view>
    <view class="question-content">
      <text class="question-text">{{ q.title }}</text>
    </view>
    <view class="options-list" v-if="q.questionType !== 'CASE'">
      <view 
        v-for="(opt, idx) in options" 
        :key="opt.key" 
        :class="['option-item', { selected: selectedOptions.includes(opt.key), correct: showResult && isCorrectOption(opt.key), wrong: showResult && isWrongOption(opt.key) }]"
        @click="pick(opt.key)"
      >
        <view class="option-radio">
          <view class="radio-inner" :class="{ filled: selectedOptions.includes(opt.key) }">
            <text v-if="selectedOptions.includes(opt.key)" class="radio-check">✓</text>
          </view>
        </view>
        <text class="option-key">{{ opt.key }}</text>
        <text class="option-text">{{ opt.text }}</text>
      </view>
    </view>
    <view class="fill-blank" v-else>
      <textarea 
        class="answer-input" 
        v-model="userAnswer"
        placeholder="请输入您的答案..."
        :disabled="showResult"
      ></textarea>
    </view>
    <view v-if="showResult" class="result-section">
      <view :class="['result-header', result.correct ? 'correct' : 'wrong']">
        {{ result.correct ? '回答正确' : '回答错误' }}
      </view>
      <view class="result-detail">
        <view class="result-item">
          <text class="result-label">标准答案：</text>
          <text class="result-value">{{ result.standardAnswer }}</text>
        </view>
        <view class="result-item">
          <text class="result-label">您的答案：</text>
          <text class="result-value">{{ userAnswer || '未作答' }}</text>
        </view>
        <view class="result-analysis">
          <text class="result-label">解析：</text>
          <text class="result-value">{{ result.analysis || '暂无解析' }}</text>
        </view>
      </view>
    </view>
    <view class="submit-area">
      <button :class="['submit-btn', { disabled: !userAnswer }]" @click="submit">
        {{ showResult ? '下一题' : '提交答案' }}
      </button>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { 
      id: '', 
      q: null, 
      options: [], 
      userAnswer: '', 
      result: null,
      showResult: false,
      index: 0,
      type: 'EXERCISE',
      total: 0
    }
  },
  computed: {
    selectedOptions() {
      return this.userAnswer ? this.userAnswer.split(',').filter(i => i) : []
    }
  },
  onLoad(q) {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.id = q.id
    this.index = parseInt(q.index) || 0
    this.type = q.type || 'EXERCISE'
    this.load()
  },
  methods: {
    getQuestionTypeName(type) {
      const types = {
        EXERCISE: '单选题',
        MULTI: '多选题',
        CASE: '案例分析题'
      }
      return types[type] || '未知题型'
    },
    async load() {
      try {
        this.q = await request({ url: '/app/questions/' + this.id })
        this.result = null
        this.showResult = false
        this.userAnswer = ''
        this.options = this.generateOptions()
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    generateOptions() {
      const optionKeys = ['A', 'B', 'C', 'D']
      return optionKeys.map(key => ({ 
        key, 
        text: this.q?.['option' + key.toUpperCase()] || '' 
      }))
    },
    pick(key) {
      if (this.showResult) return
      if (this.q?.questionType === 'CASE') return
      if (this.q?.questionType === 'MULTI') {
        const parts = this.userAnswer ? this.userAnswer.split(',') : []
        const i = parts.indexOf(key)
        if (i >= 0) parts.splice(i, 1)
        else parts.push(key)
        parts.sort()
        this.userAnswer = parts.join(',')
      } else {
        this.userAnswer = key
      }
    },
    isCorrectOption(key) {
      if (!this.result?.standardAnswer) return false
      return this.result.standardAnswer.includes(key)
    },
    isWrongOption(key) {
      if (!this.showResult || !this.userAnswer) return false
      const isSelected = this.userAnswer.includes(key)
      const isCorrect = this.result?.standardAnswer?.includes(key)
      return isSelected && !isCorrect
    },
    async submit() {
      if (!this.userAnswer && !this.showResult) {
        uni.showToast({ title: '请选择答案', icon: 'none' })
        return
      }
      if (this.showResult) {
        this.nextQuestion()
        return
      }
      try {
        this.result = await request({
          url: '/app/questions/' + this.id + '/answer',
          method: 'POST',
          data: { userAnswer: this.userAnswer },
        })
        this.showResult = true
      } catch (e) {
        uni.showToast({ title: e.message || '提交失败', icon: 'none' })
      }
    },
    async nextQuestion() {
      try {
        const source = this.type === 'CASE' ? 'REAL' : this.type === 'MULTI' ? 'MOCK' : 'EXERCISE'
        const questions = (await request({ url: `/app/questions?source=${source}` })) || []
        this.total = questions.length
        if (this.index < this.total - 1) {
          const nextQ = questions[this.index + 1]
          uni.redirectTo({ url: `/pages/practice/do?id=${nextQ.id || nextQ.typeId}&index=${this.index + 1}&type=${this.type}` })
        } else {
          uni.showToast({ title: '已经是最后一题', icon: 'none' })
          setTimeout(() => uni.navigateBack(), 1000)
        }
      } catch (e) {
        uni.showToast({ title: e.message || '获取下一题失败', icon: 'none' })
        setTimeout(() => uni.navigateBack(), 1000)
      }
    },
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 160rpx;
}
.question-header {
  padding: 24rpx;
  background: #fff;
}
.question-type {
  display: inline-block;
  padding: 8rpx 16rpx;
  background: #e6f4ff;
  color: #1989fa;
  font-size: 24rpx;
  border-radius: 8rpx;
}
.question-content {
  margin: 24rpx;
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
}
.question-text {
  font-size: 32rpx;
  line-height: 1.6;
  color: #333;
}
.options-list {
  padding: 0 24rpx;
}
.option-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  margin-bottom: 16rpx;
  background: #fff;
  border-radius: 16rpx;
  border: 2rpx solid transparent;
  transition: all 0.3s;
}
.option-item.selected {
  border-color: #1989fa;
  background: #f0f7ff;
}
.option-item.correct {
  border-color: #52c41a;
  background: #f6ffed;
}
.option-item.wrong {
  border-color: #ff4d4f;
  background: #fff2f0;
}
.option-radio {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  border: 2rpx solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}
.option-item.selected .option-radio {
  border-color: #1989fa;
  background: #1989fa;
}
.option-item.correct .option-radio {
  border-color: #52c41a;
  background: #52c41a;
}
.option-item.wrong .option-radio {
  border-color: #ff4d4f;
  background: #ff4d4f;
}
.radio-inner {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.radio-check {
  color: #fff;
  font-size: 24rpx;
  font-weight: bold;
}
.option-key {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-right: 12rpx;
  width: 40rpx;
}
.option-text {
  flex: 1;
  font-size: 30rpx;
  color: #333;
  line-height: 1.5;
}
.fill-blank {
  padding: 0 24rpx;
  margin-bottom: 16rpx;
}
.answer-input {
  width: 100%;
  height: 200rpx;
  padding: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  border: 2rpx solid #e0e0e0;
  font-size: 28rpx;
  line-height: 1.5;
  box-sizing: border-box;
}
.answer-input:disabled {
  background: #f5f5f5;
  color: #999;
}
.result-section {
  margin: 24rpx;
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
}
.result-header {
  font-size: 36rpx;
  font-weight: 600;
  margin-bottom: 20rpx;
  padding: 16rpx;
  border-radius: 12rpx;
  text-align: center;
}
.result-header.correct {
  background: #f6ffed;
  color: #52c41a;
}
.result-header.wrong {
  background: #fff2f0;
  color: #ff4d4f;
}
.result-detail {
  font-size: 28rpx;
}
.result-item {
  display: flex;
  margin-bottom: 12rpx;
}
.result-label {
  color: #666;
  margin-right: 8rpx;
}
.result-value {
  color: #333;
  font-weight: 500;
}
.result-analysis {
  margin-top: 16rpx;
  padding-top: 16rpx;
  border-top: 1rpx solid #f0f0f0;
}
.result-analysis .result-value {
  color: #666;
  font-weight: normal;
  line-height: 1.6;
}
.submit-area {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 24rpx;
  background: #fff;
  box-shadow: 0 -4rpx 20rpx rgba(0,0,0,0.08);
}
.submit-btn {
  width: 100%;
  height: 88rpx;
  background: #1989fa;
  color: #fff;
  font-size: 32rpx;
  border-radius: 44rpx;
}
.submit-btn.disabled {
  background: #ccc;
}
</style>