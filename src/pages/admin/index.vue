<template>
  <view class="admin-page">
    <view class="sidebar">
      <view class="sidebar-header">
        <text class="sidebar-title">健康营养师管理系统</text>
      </view>
      <view class="menu-list">
        <view 
          v-for="item in menuItems" 
          :key="item.path"
          :class="['menu-item', { active: currentMenu === item.path }]"
          @click="navigateTo(item.path)"
        >
          <text class="menu-icon">{{ item.icon }}</text>
          <text class="menu-text">{{ item.name }}</text>
        </view>
      </view>
      <view class="sidebar-footer">
        <view class="logout-btn" @click="logout">退出登录</view>
      </view>
    </view>
    <view class="main-content">
      <view class="content-header">
        <text class="content-title">欢迎来到管理系统</text>
        <view class="user-info">
          <text class="username">{{ adminInfo?.name || '管理员' }}</text>
        </view>
      </view>
      <view class="dashboard" v-if="!loading">
        <view class="stat-card">
          <view class="stat-icon-wrap">
            <text class="stat-icon">👥</text>
          </view>
          <view class="stat-info">
            <text class="stat-value">{{ stats.users }}</text>
            <text class="stat-label">用户总数</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon-wrap">
            <text class="stat-icon">📚</text>
          </view>
          <view class="stat-info">
            <text class="stat-value">{{ stats.courses }}</text>
            <text class="stat-label">课程总数</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon-wrap">
            <text class="stat-icon">📝</text>
          </view>
          <view class="stat-info">
            <text class="stat-value">{{ stats.questions }}</text>
            <text class="stat-label">题目总数</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon-wrap">
            <text class="stat-icon">🏆</text>
          </view>
          <view class="stat-info">
            <text class="stat-value">{{ stats.exams }}</text>
            <text class="stat-label">考试次数</text>
          </view>
        </view>
      </view>
      <view class="loading-container" v-else>
        <text class="loading-text">加载中...</text>
      </view>
    </view>
  </view>
</template>

<script>
import { request } from '@/utils/api.js'

export default {
  data() {
    return {
      currentMenu: '',
      adminInfo: null,
      loading: true,
      stats: {
        users: 0,
        courses: 0,
        questions: 0,
        exams: 0
      },
      menuItems: [
        { name: '用户管理', icon: '👥', path: '/pages/admin/users' },
        { name: '课程管理', icon: '📚', path: '/pages/admin/courses' },
        { name: '题库管理', icon: '📝', path: '/pages/admin/questions' },
        { name: '考试管理', icon: '🏆', path: '/pages/admin/exams' },
        { name: '资料管理', icon: '📁', path: '/pages/admin/materials' },
        { name: '论坛管理', icon: '💬', path: '/pages/admin/forum' }
      ]
    }
  },
  onShow() {
    this.checkLogin()
    this.loadStats()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('adminToken')
      const info = uni.getStorageSync('adminInfo')
      if (!token) {
        uni.redirectTo({ url: '/pages/admin/login' })
        return
      }
      try {
        this.adminInfo = JSON.parse(info)
      } catch (e) {
        this.adminInfo = null
      }
    },
    async loadStats() {
      this.loading = true
      try {
        const res = await request({ url: '/admin/dashboard/stats' })
        this.stats = {
          users: res.users || 0,
          courses: res.courses || 0,
          questions: res.questions || 0,
          exams: res.scores || res.exams || 0
        }
      } catch (e) {
        console.error('加载统计数据失败:', e)
        uni.showToast({ title: '加载数据失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    navigateTo(path) {
      this.currentMenu = path
      uni.navigateTo({ url: path })
    },
    logout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('adminToken')
            uni.removeStorageSync('adminInfo')
            uni.redirectTo({ url: '/pages/admin/login' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-page {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}
.sidebar {
  width: 300rpx;
  background: #2c3e50;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 100;
}
.sidebar-header {
  padding: 32rpx;
  background: #34495e;
}
.sidebar-title {
  font-size: 26rpx;
  color: #fff;
  font-weight: 600;
}
.menu-list {
  flex: 1;
  padding: 16rpx;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  margin-bottom: 8rpx;
  border-radius: 8rpx;
  color: #bdc3c7;
  transition: all 0.3s;
}
.menu-item.active {
  background: #3498db;
  color: #fff;
}
.menu-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}
.menu-text {
  font-size: 28rpx;
}
.sidebar-footer {
  padding: 16rpx;
}
.logout-btn {
  padding: 20rpx;
  background: #e74c3c;
  color: #fff;
  text-align: center;
  border-radius: 8rpx;
  font-size: 28rpx;
}
.main-content {
  flex: 1;
  margin-left: 300rpx;
  padding: 32rpx;
  min-height: 100vh;
}
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}
.content-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}
.user-info {
  display: flex;
  align-items: center;
}
.username {
  font-size: 28rpx;
  color: #666;
  margin-right: 16rpx;
}
.dashboard {
  display: flex;
  gap: 24rpx;
  flex-wrap: wrap;
}
.stat-card {
  flex: 1;
  min-width: 280rpx;
  background: #fff;
  padding: 32rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
}
.stat-icon-wrap {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #3498db 0%, #2980b9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
}
.stat-icon {
  font-size: 40rpx;
}
.stat-info {
  display: flex;
  flex-direction: column;
}
.stat-value {
  font-size: 48rpx;
  font-weight: 600;
  color: #3498db;
}
.stat-label {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 400rpx;
}
.loading-text {
  font-size: 28rpx;
  color: #999;
}
</style>