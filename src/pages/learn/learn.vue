<template>
  <view class="page">
    <view class="header">
      <text class="title">学习与浏览</text>
    </view>
    <view class="tabs">
      <view v-for="tab in tabs" :key="tab.key" :class="['tab', { active: activeTab === tab.key }]" @click="activeTab = tab.key">
        {{ tab.label }}
      </view>
    </view>
    <view class="content">
      <view v-if="activeTab === 'course'" class="course-list">
        <view v-for="c in courses" :key="c.id" class="course-card" @click="goCourse(c.id)">
          <view class="course-cover">
            <image class="cover-img" :src="c.imageUrl || 'https://neeko-copilot.bytedance.net/api/text_to_image?prompt=nutrition%20education%20course%20cover&image_size=square'" mode="aspectFill" />
          </view>
          <view class="course-info">
            <text class="course-name">{{ c.name }}</text>
            <text class="course-desc">{{ c.introduction || '点击查看详情' }}</text>
            <view class="course-meta">
              <text class="meta-item">⭐ {{ c.star || 4.5 }}</text>
              <text class="meta-item">⏱ {{ c.time || 12 }}课时</text>
            </view>
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
      tabs: [{ key: 'course', label: '课程' }, { key: 'material', label: '资料' }, { key: 'knowledge', label: '知识点' }],
      activeTab: 'course',
      courses: [],
      materials: [],
      knowledgePoints: [],
      defaultCourses: [
        { id: 1, name: '基础营养学', introduction: '本课程介绍宏量营养素与微量营养素的基础概念与膳食来源，帮助学员建立营养学基础知识体系。', star: 4.8, time: 12 },
        { id: 2, name: '膳食指南解读', introduction: '深入解读中国居民膳食指南，学习如何制定科学合理的膳食计划。', star: 4.6, time: 8 },
        { id: 3, name: '营养评估技术', introduction: '学习常用的营养评估方法和工具，掌握膳食调查与分析技能。', star: 4.5, time: 10 },
        { id: 4, name: '特殊人群营养', introduction: '了解孕妇、婴幼儿、老年人及慢性病患者的特殊营养需求。', star: 4.7, time: 10 },
        { id: 5, name: '营养咨询实务', introduction: '掌握营养咨询的流程和技巧，提高沟通和服务能力。', star: 4.4, time: 8 },
        { id: 6, name: '食谱设计与编制', introduction: '学习如何根据不同人群和需求编制营养均衡的食谱。', star: 4.3, time: 6 },
      ],
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
        const res = await request({ url: '/app/courses' })
        if (res && res.length > 0) {
          this.courses = res
        } else {
          this.courses = this.defaultCourses
        }
        this.materials = (await request({ url: '/app/materials' })) || []
        this.knowledgePoints = (await request({ url: '/app/knowledge' })) || []
      } catch (e) {
        this.courses = this.defaultCourses
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
.tabs { display: flex; background: #fff; padding: 0 24rpx; border-bottom: 1px solid #eee; }
.tab { padding: 24rpx 32rpx; font-size: 28rpx; color: #666; position: relative; }
.tab.active { color: #1989fa; font-weight: 600; }
.tab.active::after { content: ''; position: absolute; bottom: 0; left: 50%; transform: translateX(-50%); width: 48rpx; height: 4rpx; background: #1989fa; border-radius: 2rpx; }
.content { padding: 16rpx 24rpx; }
.course-card { display: flex; background: #fff; border-radius: 16rpx; margin-bottom: 16rpx; overflow: hidden; }
.course-cover { width: 160rpx; height: 160rpx; flex-shrink: 0; }
.cover-img { width: 100%; height: 100%; }
.course-info { flex: 1; padding: 16rpx; display: flex; flex-direction: column; }
.course-name { font-size: 30rpx; font-weight: 600; }
.course-desc { font-size: 24rpx; color: #666; margin-top: 8rpx; flex: 1; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.course-meta { display: flex; gap: 24rpx; margin-top: 12rpx; }
.meta-item { font-size: 22rpx; color: #999; }
.material-card, .knowledge-card { background: #fff; padding: 24rpx; border-radius: 16rpx; margin-bottom: 16rpx; }
.material-name, .knowledge-name { font-size: 30rpx; font-weight: 600; display: block; }
.material-desc, .knowledge-desc { font-size: 24rpx; color: #666; margin-top: 8rpx; display: block; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.empty { text-align: center; color: #999; padding: 60rpx; }
</style>