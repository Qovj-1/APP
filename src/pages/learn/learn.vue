<template>
  <view class="page">
    <view class="header">
      <text class="title">学习</text>
    </view>
    <swiper v-if="banners.length" class="swiper" circular autoplay interval="4000">
      <swiper-item v-for="b in banners" :key="b.id">
        <image class="banner-img" :src="b.imageUrl" mode="aspectFill" />
      </swiper-item>
    </swiper>
    <view class="tabs">
      <view v-for="tab in tabs" :key="tab.key" :class="['tab', { active: activeTab === tab.key }]" @click="activeTab = tab.key">
        {{ tab.label }}
      </view>
    </view>
    <view class="content">
      <view v-if="activeTab === 'course'" class="course-list">
        <view v-for="c in courses" :key="c.id" class="course-card" @click="goCourse(c.id)">
          <text class="course-name">{{ c.name }}</text>
          <text class="course-desc">{{ c.introduction || '点击查看详情' }}</text>
          <view class="course-meta">
            <text class="meta-item">星级 {{ c.star || '-' }}</text>
            <text class="meta-item">课时 {{ c.time || '-' }}</text>
          </view>
        </view>
        <view v-if="!courses.length" class="empty">暂无课程</view>
      </view>
      <view v-else-if="activeTab === 'material'" class="material-list">
        <view v-for="m in materials" :key="m.id" class="material-card">
          <text class="material-name">{{ m.name }}</text>
          <text class="material-desc">{{ m.content }}</text>
        </view>
        <view v-if="!materials.length" class="empty">暂无资料</view>
      </view>
      <view v-else-if="activeTab === 'knowledge'" class="knowledge-list">
        <view v-for="k in knowledgePoints" :key="k.typeId" class="knowledge-card">
          <text class="knowledge-name">{{ k.name }}</text>
          <text class="knowledge-desc">{{ k.content }}</text>
        </view>
        <view v-if="!knowledgePoints.length" class="empty">暂无知识点</view>
      </view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return {
      banners: [],
      tabs: [{ key: 'course', label: '课程' }, { key: 'material', label: '资料' }, { key: 'knowledge', label: '知识点' }],
      activeTab: 'course',
      courses: [],
      materials: [],
      knowledgePoints: [],
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
    async load() {
      try {
        this.banners = (await request({ url: '/app/banners' })) || []
        this.courses = (await request({ url: '/app/courses' })) || []
        this.materials = (await request({ url: '/app/materials' })) || []
        this.knowledgePoints = (await request({ url: '/app/knowledge' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    goCourse(id) {
      uni.navigateTo({ url: '/pages/course/detail?id=' + id })
    },
  },
}
</script>

<style scoped>
.page { min-height: 100vh; background: #f5f5f5; }
.header { padding: 24rpx; background: #1989fa; }
.title { font-size: 36rpx; font-weight: 600; color: #fff; }
.swiper { height: 240rpx; }
.banner-img { width: 100%; height: 240rpx; }
.tabs { display: flex; background: #fff; padding: 0 24rpx; border-bottom: 1px solid #eee; }
.tab { padding: 24rpx 32rpx; font-size: 28rpx; color: #666; position: relative; }
.tab.active { color: #1989fa; font-weight: 600; }
.tab.active::after { content: ''; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%); width: 48rpx; height: 4rpx; background: #1989fa; border-radius: 2rpx; }
.content { padding: 16rpx 24rpx; }
.course-card, .material-card, .knowledge-card { background: #fff; padding: 24rpx; border-radius: 16rpx; margin-bottom: 16rpx; }
.course-name, .material-name, .knowledge-name { font-size: 30rpx; font-weight: 600; display: block; }
.course-desc, .material-desc, .knowledge-desc { font-size: 24rpx; color: #666; margin-top: 8rpx; display: block; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.course-meta { display: flex; gap: 24rpx; margin-top: 12rpx; }
.meta-item { font-size: 22rpx; color: #999; }
.empty { text-align: center; color: #999; padding: 60rpx; }
</style>