<template>
  <view class="page">
    <view class="header">
      <text class="title">测试与实战</text>
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
      <view v-if="!questions.length" class="empty">暂无题目</view>
    </view>
    <view class="quick-actions">
      <view class="action-btn" @click="goExamList">
        <text class="action-icon">📝</text>
        <text class="action-text">实例实战</text>
      </view>
      <view class="action-btn" @click="goWrong">
        <text class="action-icon">❌</text>
        <text class="action-text">错题集</text>
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
        const source = this.activeType === 'CASE' ? 'REAL' : this.activeType === 'MULTI' ? 'MOCK' : 'EXERCISE'
        this.questions = (await request({ url: `/app/questions?source=${source}` })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    goQuestion(id, index) {
      uni.navigateTo({ url: '/pages/practice/do?id=' + id + '&index=' + index + '&type=' + this.activeType })
    },
    goExamList() {
      uni.navigateTo({ url: '/pages/exam/list' })
    },
    goWrong() {
      uni.navigateTo({ url: '/pages/wrong/wrong' })
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
  padding-bottom: 160rpx;
}
.header {
  padding: 24rpx;
  background: #1989fa;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}
.type-tabs {
  display: flex;
  background: #fff;
  padding: 16rpx 24rpx;
  gap: 16rpx;
}
.type-tab {
  flex: 1;
  padding: 16rpx;
  text-align: center;
  font-size: 26rpx;
  border-radius: 50rpx;
  background: #f5f5f5;
  color: #666;
}
.type-tab.active {
  background: #1989fa;
  color: #fff;
}
.question-list {
  padding: 16rpx 24rpx;
}
.question-item {
  background: #fff;
  padding: 24rpx;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
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
}
.question-footer {
  margin-top: 12rpx;
}
.question-tag {
  font-size: 22rpx;
  color: #999;
}
.empty {
  text-align: center;
  color: #999;
  padding: 60rpx;
}
.quick-actions {
  position: fixed;
  bottom: 120rpx;
  left: 24rpx;
  right: 24rpx;
  display: flex;
  gap: 24rpx;
}
.action-btn {
  flex: 1;
  background: #fff;
  padding: 24rpx;
  border-radius: 16rpx;
  text-align: center;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}
.action-icon {
  font-size: 48rpx;
  display: block;
}
.action-text {
  font-size: 24rpx;
  color: #666;
  margin-top: 8rpx;
  display: block;
}
</style>