<template>
  <view class="page" v-if="user">
    <view class="header">
      <text class="title">个人中心</text>
    </view>
    <view class="user-card">
      <view class="avatar" @click="editProfile">
        <image 
          v-if="user.avatarUrl" 
          class="avatar-image" 
          :src="user.avatarUrl" 
          mode="aspectFill"
        />
        <text v-else class="avatar-text">{{ user.name?.charAt(0) || '用' }}</text>
      </view>
      <view class="user-info">
        <text class="user-name">{{ user.name }}</text>
        <text class="user-phone">{{ user.phone || '未绑定手机' }}</text>
      </view>
      <view class="edit-btn" @click="editProfile">编辑</view>
    </view>
    <view class="menu-list">
      <navigator url="/pages/score/list" class="menu-item">
        <text class="menu-icon">📊</text>
        <text class="menu-text">成绩记录</text>
        <text class="menu-arrow">›</text>
      </navigator>
      <navigator url="/pages/learning/list" class="menu-item">
        <text class="menu-icon">📚</text>
        <text class="menu-text">学习记录</text>
        <text class="menu-arrow">›</text>
      </navigator>
      <navigator url="/pages/wrong/wrong" class="menu-item">
        <text class="menu-icon">❌</text>
        <text class="menu-text">错题记录</text>
        <text class="menu-arrow">›</text>
      </navigator>
      <navigator url="/pages/forum/list" class="menu-item">
        <text class="menu-icon">💬</text>
        <text class="menu-text">论坛交流</text>
        <text class="menu-arrow">›</text>
      </navigator>
    </view>
    <view class="settings-list">
      <navigator url="/pages/profile/password" class="setting-item">
        <text class="setting-text">修改密码</text>
        <text class="setting-arrow">›</text>
      </navigator>
      <navigator url="/pages/profile/edit" class="setting-item">
        <text class="setting-text">修改信息</text>
        <text class="setting-arrow">›</text>
      </navigator>
    </view>
    <button class="logout-btn" @click="logout">退出登录</button>
  </view>
</template>

<script>
import { request, getToken, clearAuth } from '@/utils/api.js'

export default {
  data() {
    return { user: null }
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
        this.user = await request({ url: '/app/me' })
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    editProfile() {
      uni.navigateTo({ url: '/pages/profile/edit' })
    },
    logout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            clearAuth()
            uni.redirectTo({ url: '/pages/login/login' })
          }
        },
      })
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
.user-card {
  background: #fff;
  margin: 24rpx;
  padding: 24rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
}
.avatar {
  width: 120rpx;
  height: 120rpx;
  background: #1989fa;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}
.avatar-image {
  width: 120rpx;
  height: 120rpx;
}
.avatar-text {
  font-size: 48rpx;
  color: #fff;
}
.user-info {
  flex: 1;
  margin-left: 24rpx;
}
.user-name {
  font-size: 32rpx;
  font-weight: 600;
  display: block;
}
.user-phone {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}
.edit-btn {
  padding: 12rpx 24rpx;
  border: 1px solid #1989fa;
  color: #1989fa;
  font-size: 24rpx;
  border-radius: 50rpx;
}
.menu-list {
  background: #fff;
  margin: 0 24rpx 16rpx;
  border-radius: 16rpx;
  overflow: hidden;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 1px solid #f0f0f0;
}
.menu-item:last-child {
  border-bottom: none;
}
.menu-icon {
  font-size: 40rpx;
  margin-right: 16rpx;
}
.menu-text {
  flex: 1;
  font-size: 28rpx;
}
.menu-arrow {
  font-size: 32rpx;
  color: #ccc;
}
.settings-list {
  background: #fff;
  margin: 0 24rpx 32rpx;
  border-radius: 16rpx;
  overflow: hidden;
}
.setting-item {
  display: flex;
  align-items: center;
  padding: 28rpx 24rpx;
  border-bottom: 1px solid #f0f0f0;
}
.setting-item:last-child {
  border-bottom: none;
}
.setting-text {
  flex: 1;
  font-size: 28rpx;
}
.setting-arrow {
  font-size: 32rpx;
  color: #ccc;
}
.logout-btn {
  margin: 0 24rpx;
  background: #ff4d4f;
  color: #fff;
  border-radius: 16rpx;
}
</style>