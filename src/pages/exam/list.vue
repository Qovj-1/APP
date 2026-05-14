<template>
  <view class="page">
    <view class="header">
      <text class="title">实例实战</text>
    </view>
    
    <view class="main-content">
      <view class="section">
        <view class="project-list">
          <view v-for="project in projects" :key="project.id" class="project-card" @click="goProject(project)">
            <view class="project-header">
              <text class="project-title">{{ project.name }}</text>
              <text class="project-type">实训</text>
            </view>
            <view class="project-info">
              <text class="info-item">课时：{{ project.time }}</text>
            </view>
            <view class="project-footer">
              <text class="project-diff">难度系数 {{ project.difficult }}</text>
              <text class="project-action">开始实训</text>
            </view>
          </view>
          <view v-if="loading" class="empty">
            <text class="empty-text">加载中...</text>
          </view>
          <view v-else-if="!projects.length" class="empty">
            <text class="empty-text">暂无实训项目</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { 
      projects: [],
      trainings: [],
      loading: false
    }
  },
  onShow() {
    this.load()
  },
  methods: {
    async load() {
      const token = getToken()
      if (!token) {
        return
      }
      
      this.loading = true
      try {
        const trainingsRes = await request({ url: '/app/trainings' }).catch(() => [])
        this.trainings = trainingsRes || []
        
        this.projects = this.trainings.map(t => ({
          id: 'training_' + t.trainingId,
          name: t.name || '实训项目',
          time: t.time + '课时',
          difficult: t.difficult || 1.0,
          original: t
        }))
      } catch (e) {
        console.error('加载失败:', e)
        uni.showToast({ title: '加载失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    goProject(project) {
      const token = getToken()
      if (!token) {
        uni.redirectTo({ url: '/pages/login/login' })
        return
      }
      uni.navigateTo({ url: `/pages/train/detail?id=${project.original.trainingId}` })
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
  position: sticky;
  top: 0;
  z-index: 100;
}
.title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}
.main-content {
  padding: 24rpx;
  padding-bottom: 120rpx;
}
.section {
  background: #fff;
  border-radius: 16rpx;
  border: 1rpx solid #eee;
}
.project-list {
  padding: 16rpx 24rpx;
}
.project-card {
  padding: 20rpx;
  background: #fafafa;
  border-radius: 12rpx;
  margin-bottom: 12rpx;
}
.project-card:last-child {
  margin-bottom: 0;
}
.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8rpx;
}
.project-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.project-type {
  font-size: 20rpx;
  color: #1989fa;
  background: #e6f4ff;
  padding: 4rpx 10rpx;
  border-radius: 6rpx;
}
.project-info {
  margin-bottom: 12rpx;
}
.info-item {
  font-size: 24rpx;
  color: #666;
}
.project-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.project-diff {
  font-size: 22rpx;
  color: #999;
}
.project-action {
  font-size: 24rpx;
  color: #1989fa;
}
.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 300rpx;
}
.empty-text {
  font-size: 26rpx;
  color: #999;
}
</style>