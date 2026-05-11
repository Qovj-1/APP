<template>
  <view class="page">
    <view class="header">
      <text class="back" @click="goBack">‹</text>
      <text class="title">成绩记录</text>
    </view>
    <view class="content" v-if="scores.length">
      <view class="score-card" v-for="s in scores" :key="s.id">
        <view class="score-header">
          <text class="exam-name">模拟考试</text>
          <text class="exam-time">{{ formatTime(s.examTime) }}</text>
        </view>
        <view class="score-body">
          <view class="score-circle">
            <text class="score-value">{{ s.score }}</text>
            <text class="score-unit">分</text>
          </view>
          <view class="score-info">
            <text class="info-item">正确 {{ s.correctCount }} / {{ s.total }}</text>
            <text class="info-item">试卷编号: {{ s.paperId }}</text>
          </view>
        </view>
      </view>
    </view>
    <view class="empty" v-else>
      <text class="empty-icon">📝</text>
      <text class="empty-text">暂无考试记录</text>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { scores: [] }
  },
  onShow() {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.load()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    async load() {
      try {
        this.scores = await request({ url: '/app/exams/scores' }) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    formatTime(t) {
      if (!t) return ''
      return t.substring(0, 19).replace('T', ' ')
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
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #1989fa;
}
.back {
  font-size: 48rpx;
  color: #fff;
  margin-right: 16rpx;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}
.content {
  padding: 24rpx;
}
.score-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}
.score-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}
.exam-name {
  font-size: 30rpx;
  font-weight: 600;
}
.exam-time {
  font-size: 24rpx;
  color: #999;
}
.score-body {
  display: flex;
  align-items: center;
}
.score-circle {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #1989fa, #69c0ff);
  display: flex;
  align-items: baseline;
  justify-content: center;
  margin-right: 24rpx;
}
.score-value {
  font-size: 48rpx;
  font-weight: 600;
  color: #fff;
}
.score-unit {
  font-size: 24rpx;
  color: #fff;
  margin-left: 4rpx;
}
.score-info {
  flex: 1;
}
.info-item {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 8rpx;
}
.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}
.empty-icon {
  font-size: 120rpx;
  margin-bottom: 24rpx;
}
.empty-text {
  font-size: 28rpx;
  color: #999;
}
</style>
