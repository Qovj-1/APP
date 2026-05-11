<template>
  <view class="page">
    <view v-for="(q, idx) in displayQuestions" :key="q.id" class="question-block">
      <view class="question-header">
        <text class="question-number">{{ idx + 1 }}.</text>
        <view :class="['question-type', getTypeClass(getQuestionType(q))]">{{ getTypeName(getQuestionType(q)) }}</view>
      </view>
      <text class="question-title">{{ q.title }}</text>
      
      <view v-if="getQuestionType(q) !== 'CASE' && !submitted" class="options-list">
        <view 
          v-for="opt in q.options" 
          :key="opt.key" 
          :class="['option-item', { selected: isSelected(q.id, opt.key) }]"
          @click="pick(q.id, opt.key, q)"
        >
          <view class="option-radio">
            <view :class="['radio-inner', { filled: isSelected(q.id, opt.key), multi: getQuestionType(q) === 'MULTI' }]">
              <text v-if="isSelected(q.id, opt.key)" class="radio-check">✓</text>
            </view>
          </view>
          <text class="option-key">{{ opt.key }}</text>
          <text class="option-text">{{ opt.text }}</text>
        </view>
      </view>
      
      <view v-else-if="getQuestionType(q) !== 'CASE' && submitted" class="options-list">
        <view 
          v-for="opt in q.options" 
          :key="opt.key" 
          :class="['option-item', { 
            selected: isSelected(q.id, opt.key),
            correct: q.result && q.correctAnswer.split(',').includes(opt.key),
            wrong: q.result && q.userAnswer.split(',').includes(opt.key) && !q.correctAnswer.split(',').includes(opt.key)
          }]"
        >
          <view class="option-radio">
            <view :class="['radio-inner', { 
              filled: isSelected(q.id, opt.key), 
              multi: getQuestionType(q) === 'MULTI',
              correct: q.result && q.correctAnswer.split(',').includes(opt.key),
              wrong: q.result && q.userAnswer.split(',').includes(opt.key) && !q.correctAnswer.split(',').includes(opt.key)
            }]">
              <text v-if="isSelected(q.id, opt.key)" class="radio-check">✓</text>
            </view>
          </view>
          <text class="option-key">{{ opt.key }}</text>
          <text class="option-text">{{ opt.text }}</text>
        </view>
      </view>
      
      <view v-else-if="!submitted" class="case-input">
        <textarea 
          class="answer-textarea" 
          v-model="answers[q.id]"
          placeholder="请输入您的分析答案..."
        ></textarea>
      </view>
      
      <view v-else class="case-result">
        <view class="user-answer">
          <text class="label">您的答案：</text>
          <text class="answer-text">{{ q.result ? q.userAnswer || '未作答' : '' }}</text>
        </view>
        <view class="correct-answer">
          <text class="label">参考答案：</text>
          <text class="answer-text">{{ q.result ? q.correctAnswer : '' }}</text>
        </view>
      </view>
      
      <view v-if="q.result && q.analysis" class="analysis-section">
        <view class="analysis-title">解析</view>
        <text class="analysis-text">{{ q.analysis }}</text>
      </view>
      
      <view v-if="q.result" class="result-badge" :class="{ correct: q.isCorrect, wrong: !q.isCorrect }">
        {{ q.isCorrect ? '正确' : '错误' }}
      </view>
    </view>
    
    <view v-if="!submitted" class="submit-area">
      <button class="submit-btn" @click="submit">交卷</button>
    </view>
    
    <view v-if="scoreText" class="score-section">
      <view class="score-header">{{ scoreText }}</view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { 
      paperId: '', 
      questions: [], 
      answers: {}, 
      scoreText: '', 
      submitted: false,
      displayQuestions: []
    }
  },
  onLoad(q) {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.paperId = q.id
    this.load()
  },
  methods: {
    getTypeName(type) {
      const types = { EXERCISE: '单选题', MULTI: '多选题', CASE: '案例分析题' }
      return types[type] || '未知题型'
    },
    getTypeClass(type) {
      return { single: type === 'EXERCISE', multi: type === 'MULTI', case: type === 'CASE' }
    },
    getQuestionType(q) {
      return q.type || q.questionType || 'EXERCISE'
    },
    isSelected(qId, key) {
      const ans = this.answers[qId] || ''
      return ans.split(',').includes(key)
    },
    pick(qId, key, q) {
      const type = this.getQuestionType(q)
      if (type === 'CASE') return
      const current = this.answers[qId] || ''
      const parts = current ? current.split(',') : []
      if (type === 'MULTI') {
        const i = parts.indexOf(key)
        if (i >= 0) parts.splice(i, 1)
        else parts.push(key)
        parts.sort()
        this.answers[qId] = parts.join(',')
      } else {
        this.answers[qId] = key
      }
    },
    async load() {
      try {
        this.questions = (await request({ url: '/app/exams/papers/' + this.paperId + '/questions' })) || []
        this.displayQuestions = this.questions.map(q => ({...q, result: null }))
        const a = {}
        this.displayQuestions.forEach((x) => {
          a[x.id] = ''
        })
        this.answers = a
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    async submit() {
      try {
        const r = await request({
          url: '/app/exams/papers/' + this.paperId + '/submit',
          method: 'POST',
          data: { answers: this.answers },
        })
        this.submitted = true
        this.scoreText = '得分：' + r.score + '分（正确 ' + r.correctCount + '/' + r.total + '）'
        
        if (r.questions) {
          this.displayQuestions = this.displayQuestions.map(q => {
            const result = r.questions.find(res => res.id === q.id)
            return {
              ...q,
              result: result || null
            }
          })
        }
        
        uni.showToast({ title: '交卷完成', icon: 'success' })
      } catch (e) {
        uni.showToast({ title: e.message || '交卷失败', icon: 'none' })
      }
    },
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 24rpx;
  padding-bottom: 160rpx;
}
.question-block {
  background: #fff;
  padding: 24rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  position: relative;
}
.question-header {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}
.question-number {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-right: 8rpx;
}
.question-type {
  font-size: 22rpx;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}
.question-type.single {
  background: #e6f4ff;
  color: #1989fa;
}
.question-type.multi {
  background: #fff7e6;
  color: #fa8c16;
}
.question-type.case {
  background: #f6ffed;
  color: #52c41a;
}
.question-title {
  font-size: 28rpx;
  line-height: 1.5;
  color: #333;
  display: block;
  margin-bottom: 16rpx;
}
.options-list {
  padding: 0 8rpx;
}
.option-item {
  display: flex;
  align-items: center;
  padding: 16rpx;
  margin-bottom: 12rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
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
  background: #fff1f0;
}
.option-radio {
  width: 36rpx;
  height: 36rpx;
  border-radius: 50%;
  border: 2rpx solid #ccc;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
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
  width: 28rpx;
  height: 28rpx;
  border-radius: 50%;
}
.radio-inner.multi {
  border-radius: 4rpx;
}
.radio-check {
  color: #fff;
  font-size: 20rpx;
  font-weight: bold;
}
.option-key {
  font-size: 26rpx;
  font-weight: 600;
  color: #333;
  margin-right: 8rpx;
  width: 32rpx;
}
.option-text {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}
.case-input {
  padding: 8rpx;
}
.answer-textarea {
  width: 100%;
  height: 240rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  border: 2rpx solid #e0e0e0;
  font-size: 26rpx;
  line-height: 1.5;
  box-sizing: border-box;
}
.case-result {
  padding: 8rpx;
}
.user-answer, .correct-answer {
  padding: 12rpx;
  margin-bottom: 8rpx;
  background: #f8f9fa;
  border-radius: 8rpx;
}
.label {
  font-weight: 600;
  color: #666;
}
.answer-text {
  color: #333;
}
.analysis-section {
  margin-top: 16rpx;
  padding: 16rpx;
  background: #fff7e6;
  border-radius: 12rpx;
}
.analysis-title {
  font-weight: 600;
  color: #fa8c16;
  margin-bottom: 8rpx;
}
.analysis-text {
  font-size: 26rpx;
  color: #333;
  line-height: 1.6;
}
.result-badge {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
  font-weight: 600;
}
.result-badge.correct {
  background: #f6ffed;
  color: #52c41a;
}
.result-badge.wrong {
  background: #fff1f0;
  color: #ff4d4f;
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
.score-section {
  margin-top: 24rpx;
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  text-align: center;
}
.score-header {
  font-size: 32rpx;
  font-weight: 600;
  color: #52c41a;
}
</style>
