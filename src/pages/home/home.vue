<template>
  <view class="page">
    <view class="header">
      <text class="title">健康营养师</text>
    </view>
    <swiper v-if="banners.length" class="swiper" circular autoplay interval="4000">
      <swiper-item v-for="b in banners" :key="b.id">
        <image class="banner-img" :src="b.imageUrl" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="section">
      <view class="section-title">消息列表</view>
      <view v-for="item in messages" :key="item.id" class="msg-item">
        <text class="msg-title">{{ item.title }}</text>
        <text class="msg-time">{{ item.time }}</text>
      </view>
      <view v-if="!messages.length" class="empty-msg">暂无消息</view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { 
      banners: [], 
      messages: [
        { id: 1, title: '您有新的学习任务', time: '刚刚' },
        { id: 2, title: '课程更新通知', time: '5分钟前' },
        { id: 3, title: '模拟考试提醒', time: '1小时前' },
      ]
    }
  },
  onShow() {
    const token = uni.getStorageSync('app_token')
    if (!token || token === '') {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.load()
  },
  methods: {
    async load() {
      try {
        this.banners = (await request({ url: '/app/banners' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },

  },
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f5f5; }
.header { padding: 24rpx; background: #1989fa; }
.title { font-size: 36rpx; font-weight: 600; color: #fff; }
.swiper { height: 300rpx; }
.banner-img { width: 100%; height: 300rpx; }
.section { padding: 24rpx; }
.section-title { font-size: 32rpx; font-weight: 600; margin-bottom: 16rpx; }
.msg-item { background: #fff; padding: 20rpx 24rpx; border-radius: 12rpx; margin-bottom: 12rpx; display: flex; justify-content: space-between; align-items: center; }
.msg-title { font-size: 28rpx; }
.msg-time { font-size: 24rpx; color: #999; }
.empty-msg { text-align: center; color: #999; padding: 40rpx; }
</style>