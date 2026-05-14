<template>
  <view class="page">
    <view class="header">
      <text class="title">在线测试</text>
    </view>
    <view class="type-tabs">
      <view 
        v-for="(type, idx) in questionTypes" 
        :key="type.key" 
        :class="['type-tab', { active: activeType === type.key }]"
        @click="activeType = type.key"
      >
        {{ type.label }}
      </view>
    </view>
    <view class="question-list">
      <view v-for="(q, idx) in questions" :key="q.id || q.typeId" class="question-item" @click="goQuestion(q.id || q.typeId, idx)">
        <view class="question-header">
          <text class="question-type">{{ getTypeLabel(q.type) }}</text>
          <text class="question-diff">难度 {{ q.difficult || '-' }}</text>
        </view>
        <text class="question-title">{{ q.title }}</text>
        <view class="question-footer">
          <text class="question-tag">相关知识点: {{ q.knowledgePoints }}</text>
        </view>
      </view>
      <view v-if="!questions.length" class="empty">
        <text class="empty-text">题目列表</text>
      </view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return {
      questionTypes: [
        { key: 'EXERCISE', label: '单选题' },
        { key: 'MULTI', label: '多选题' },
        { key: 'CASE', label: '案例分析题' },
        { key: 'PAPER', label: '试卷练习' },
      ],
      activeType: 'EXERCISE',
      questions: [],
    }
  },
  onShow() {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.load()
  },
  methods: {
    getTypeLabel(key) {
      const type = this.questionTypes.find(t => t.key === key)
      return type ? type.label : '未知'
    },
    async load() {
      try {
        if (this.activeType === 'PAPER') {
          this.questions = []
        } else {
          const source = this.activeType === 'CASE' ? 'REAL' : this.activeType === 'MULTI' ? 'MOCK' : 'EXERCISE'
          this.questions = (await request({ url: `/app/questions?source=${source}` })) || []
        }
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    goQuestion(id, index) {
      if (this.activeType === 'PAPER') {
        uni.navigateTo({ url: '/pages/exam/list' })
      } else {
        uni.navigateTo({ url: '/pages/practice/do?id=' + id + '&index=' + index + '&type=' + this.activeType })
      }
    },
  },
  watch: {
    activeType() {
      this.load()
    },
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  padding: 32rpx;
  background: #fff;
  text-align: center;
  border-bottom: 1rpx solid #eee;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}
.type-tabs {
  display: flex;
  background: #fff;
  padding: 24rpx;
  gap: 24rpx;
  border-bottom: 1rpx solid #eee;
}
.type-tab {
  padding: 16rpx 24rpx;
  text-align: center;
  font-size: 26rpx;
  border-radius: 8rpx;
  background: #f5f5f5;
  color: #666;
  border: 2rpx solid #ddd;
}
.type-tab.active {
  background: #fff;
  color: #1989fa;
  border-color: #1989fa;
}
.question-list {
  padding: 24rpx;
}
.question-item {
  background: #fff;
  padding: 24rpx;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
  border: 1rpx solid #eee;
}
.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}
.question-type {
  font-size: 22rpx;
  color: #1989fa;
  background: #e6f4ff;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}
.question-diff {
  font-size: 22rpx;
  color: #999;
}
.question-title {
  font-size: 28rpx;
  line-height: 1.5;
  display: block;
  color: #333;
}
.question-footer {
  margin-top: 12rpx;
}
.question-tag {
  font-size: 22rpx;
  color: #999;
}
.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 500rpx;
  background: #fff;
  border-radius: 16rpx;
  border: 1rpx solid #eee;
}
.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>