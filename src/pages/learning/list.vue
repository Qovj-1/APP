<template>
  <view class="page">
    <view class="header">
      <text class="back" @click="goBack">‹</text>
      <text class="title">学习记录</text>
    </view>
    <view class="content" v-if="records.length">
      <view class="record-card" v-for="(r, idx) in records" :key="idx">
        <view class="record-header">
          <text class="record-time">{{ r.date }}</text>
        </view>
        <view class="record-items">
          <view class="record-item" v-for="item in r.items" :key="item.id">
            <view class="item-icon">{{ item.icon }}</view>
            <view class="item-info">
              <text class="item-title">{{ item.title }}</text>
              <text class="item-detail">{{ item.detail }}</text>
            </view>
            <view class="item-time">{{ item.time }}</view>
          </view>
        </view>
      </view>
    </view>
    <view class="empty" v-else>
      <text class="empty-icon">📚</text>
      <text class="empty-text">暂无学习记录</text>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { records: [] }
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
        const courses = await request({ url: '/app/courses' }) || []
        const knowledge = await request({ url: '/app/knowledge' }) || []
        const materials = await request({ url: '/app/materials' }) || []
        
        this.records = [
          {
            date: '今天',
            items: [
              { id: 1, icon: '📖', title: '基础营养学', detail: '学习了第一章 能量与营养素', time: '30分钟' },
              { id: 2, icon: '📝', title: '能量与营养素', detail: '知识点学习完成', time: '15分钟' },
            ],
          },
          {
            date: '昨天',
            items: [
              { id: 3, icon: '📖', title: '基础营养学', detail: '学习了第二章 蛋白质', time: '25分钟' },
              { id: 4, icon: '📄', title: '膳食指南摘要', detail: '资料阅读完成', time: '10分钟' },
            ],
          },
        ]
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
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
.record-card {
  background: #fff;
  border-radius: 16rpx;
  margin-bottom: 24rpx;
  overflow: hidden;
}
.record-header {
  padding: 20rpx 24rpx;
  background: #f8f9fa;
}
.record-time {
  font-size: 26rpx;
  color: #999;
}
.record-items {
  padding: 12rpx 0;
}
.record-item {
  display: flex;
  align-items: center;
  padding: 16rpx 24rpx;
  border-bottom: 1px solid #f0f0f0;
}
.record-item:last-child {
  border-bottom: none;
}
.item-icon {
  font-size: 36rpx;
  margin-right: 16rpx;
}
.item-info {
  flex: 1;
}
.item-title {
  font-size: 28rpx;
  display: block;
}
.item-detail {
  font-size: 24rpx;
  color: #999;
  margin-top: 4rpx;
  display: block;
}
.item-time {
  font-size: 24rpx;
  color: #1989fa;
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
