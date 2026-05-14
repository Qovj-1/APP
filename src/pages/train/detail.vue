<template>
  <view class="page">
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="title">实训详情</text>
      <view class="placeholder"></view>
    </view>
    
    <view class="content" v-if="training">
      <view class="cover-card">
        <view class="cover-bg"></view>
        <view class="cover-content">
          <text class="training-name">{{ training.name }}</text>
          <view class="training-tags">
            <text class="tag">实训项目</text>
            <text class="tag">难度 {{ training.difficult }}</text>
          </view>
        </view>
      </view>
      
      <view class="info-card">
        <view class="info-row">
          <view class="info-item">
            <text class="info-icon">⏱</text>
            <text class="info-label">课时</text>
            <text class="info-value">{{ training.time }}课时</text>
          </view>
          <view class="info-divider"></view>
          <view class="info-item">
            <text class="info-icon">📚</text>
            <text class="info-label">知识点</text>
            <text class="info-value">K{{ training.knowledgePoints }}</text>
          </view>
          <view class="info-divider"></view>
          <view class="info-item">
            <text class="info-icon">⭐</text>
            <text class="info-label">难度</text>
            <text class="info-value">{{ training.difficult }}级</text>
          </view>
        </view>
      </view>
      
      <view class="section">
        <view class="section-title">
          <text class="title-icon">📋</text>
          <text class="title-text">实训内容</text>
        </view>
        <view class="content-text">{{ training.content }}</view>
      </view>
      
      <view class="section">
        <view class="section-title">
          <text class="title-icon">🎯</text>
          <text class="title-text">学习目标</text>
        </view>
        <view class="goals-list">
          <view class="goal-item">
            <text class="goal-num">1</text>
            <text class="goal-text">掌握{{ training.name }}的基本概念和理论知识</text>
          </view>
          <view class="goal-item">
            <text class="goal-num">2</text>
            <text class="goal-text">学会运用相关工具和方法进行实操</text>
          </view>
          <view class="goal-item">
            <text class="goal-num">3</text>
            <text class="goal-text">完成实训项目并通过考核</text>
          </view>
        </view>
      </view>
      
      <view class="section">
        <view class="section-title">
          <text class="title-icon">📝</text>
          <text class="title-text">实训任务</text>
        </view>
        <view class="tasks-list">
          <view class="task-item" v-for="(task, idx) in tasks" :key="idx">
            <view class="task-header">
              <text class="task-num">任务{{ idx + 1 }}</text>
              <text class="task-status" :class="task.completed ? 'completed' : ''">
                {{ task.completed ? '已完成' : '未完成' }}
              </text>
            </view>
            <text class="task-content">{{ task.content }}</text>
            <view class="task-action" v-if="!task.completed" @click="completeTask(idx)">
              <text class="action-text">完成任务</text>
            </view>
          </view>
        </view>
      </view>
      
      <view class="bottom-btn">
        <button class="start-btn" @click="startTraining">开始实训</button>
      </view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return {
      training: null,
      tasks: [
        { content: '学习实训理论知识', completed: false },
        { content: '观看教学视频', completed: false },
        { content: '完成实操练习', completed: false },
        { content: '提交实训报告', completed: false },
        { content: '通过考核测评', completed: false },
      ]
    }
  },
  onLoad(options) {
    this.loadTraining(options.id)
  },
  methods: {
    async loadTraining(id) {
      try {
        this.training = (await request({ url: `/app/trainings/${id}` })) || null
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },
    goBack() {
      uni.navigateBack()
    },
    completeTask(idx) {
      this.tasks[idx].completed = true
      uni.showToast({ title: '任务完成！', icon: 'success' })
    },
    startTraining() {
      uni.navigateTo({ url: `/pages/train/learn?id=${this.training.trainingId}` })
    }
  }
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
  justify-content: space-between;
  padding: 24rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}
.back-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.back-icon {
  font-size: 48rpx;
  color: #333;
}
.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}
.placeholder {
  width: 64rpx;
}
.content {
  padding: 24rpx;
  padding-bottom: 160rpx;
}
.cover-card {
  position: relative;
  height: 200rpx;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 24rpx;
}
.cover-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #1989fa 0%, #40c4ff 100%);
}
.cover-content {
  position: relative;
  z-index: 1;
  padding: 32rpx;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.training-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
  margin-bottom: 12rpx;
}
.training-tags {
  display: flex;
  gap: 12rpx;
}
.tag {
  font-size: 22rpx;
  color: #fff;
  background: rgba(255, 255, 255, 0.3);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}
.info-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}
.info-row {
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.info-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.info-icon {
  font-size: 36rpx;
  margin-bottom: 8rpx;
}
.info-label {
  font-size: 22rpx;
  color: #999;
  margin-bottom: 4rpx;
}
.info-value {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.info-divider {
  width: 1rpx;
  height: 60rpx;
  background: #eee;
}
.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 24rpx;
}
.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}
.title-icon {
  font-size: 28rpx;
  margin-right: 12rpx;
}
.title-text {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.content-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
}
.goals-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
.goal-item {
  display: flex;
  align-items: flex-start;
}
.goal-num {
  width: 40rpx;
  height: 40rpx;
  background: #e6f4ff;
  color: #1989fa;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 600;
  margin-right: 16rpx;
  flex-shrink: 0;
}
.goal-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}
.tasks-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}
.task-item {
  padding: 20rpx;
  background: #fafafa;
  border-radius: 12rpx;
}
.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}
.task-num {
  font-size: 26rpx;
  font-weight: 600;
  color: #333;
}
.task-status {
  font-size: 22rpx;
  color: #999;
  padding: 4rpx 12rpx;
  background: #eee;
  border-radius: 8rpx;
}
.task-status.completed {
  color: #52c41a;
  background: #f6ffed;
}
.task-content {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 16rpx;
}
.task-action {
  text-align: right;
}
.action-text {
  font-size: 26rpx;
  color: #1989fa;
}
.bottom-btn {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 24rpx;
  background: #fff;
  border-top: 1rpx solid #eee;
}
.start-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #1989fa 0%, #40c4ff 100%);
  color: #fff;
  border: none;
  border-radius: 44rpx;
  font-size: 30rpx;
  font-weight: 600;
}
</style>