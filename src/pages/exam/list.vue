<template>
  <view class="page">
    <view class="header">
      <text class="title">实例实战</text>
    </view>
    <view class="paper-list">
      <view v-for="paper in papers" :key="paper.typeId" class="paper-card">
        <view class="paper-header">
          <text class="paper-title">{{ paper.typeId === 1 ? '试卷1' : '试卷' + paper.typeId }}</text>
          <text class="paper-diff">难度 {{ paper.difficult || '-' }}</text>
        </view>
        <view class="paper-info">
          <text class="info-item">考试时间：{{ paper.examTime || '未设置' }}</text>
        </view>
        <button class="exam-btn" @click="goExam(paper.typeId)">点击考试</button>
      </view>
      <view v-if="!papers.length" class="empty">暂无试卷</view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { papers: [] }
  },
  onShow() {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.load()
  },
  methods: {
    async load() {
      try {
        this.papers = (await request({ url: '/app/exams/papers' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    goExam(id) {
      uni.navigateTo({ url: '/pages/exam/take?id=' + id })
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
  padding: 24rpx;
  background: #1989fa;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}
.paper-list {
  padding: 24rpx;
}
.paper-card {
  background: #fff;
  padding: 24rpx;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
}
.paper-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}
.paper-title {
  font-size: 32rpx;
  font-weight: 600;
}
.paper-diff {
  font-size: 24rpx;
  color: #999;
}
.paper-info {
  margin-bottom: 20rpx;
}
.info-item {
  font-size: 26rpx;
  color: #666;
}
.exam-btn {
  width: 100%;
  background: #1989fa;
  color: #fff;
  border-radius: 12rpx;
}
.empty {
  text-align: center;
  color: #999;
  padding: 80rpx;
}
</style>