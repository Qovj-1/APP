<template>
  <view class="page" v-if="course">
    <view class="h">{{ course.name }}</view>
    <view class="p">{{ course.introduction }}</view>
    <view class="sub">课程视频</view>
    <view class="video-container" v-if="course.videoUrl">
      <video 
        id="courseVideo"
        class="video-player"
        :src="course.videoUrl"
        :poster="course.videoPoster"
        :controls="true"
        :autoplay="false"
        :loop="false"
        :muted="false"
        @play="onPlay"
        @pause="onPause"
        @ended="onEnded"
      ></video>
      <text class="video-title">{{ course.videoTitle }}</text>
    </view>
    <view class="sub">教师简介</view>
    <view class="p">{{ course.teacherProfile }}</view>
    <view class="sub">学习资料</view>
    <view v-for="m in materials" :key="m.id" class="card">
      <text class="mt">{{ m.name }}</text>
      <text class="mc">{{ m.content }}</text>
    </view>
    <view class="sub">知识点</view>
    <view v-for="k in knowledge" :key="k.id" class="card" @click="openK(k)">
      <text class="mt">{{ k.name }}</text>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { 
      courseId: null, 
      course: null, 
      materials: [], 
      knowledge: []
    }
  },
  onLoad(q) {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.courseId = q.id
    this.load()
  },
  methods: {
    async load() {
      try {
        this.course = await request({ url: '/app/courses/' + this.courseId })
        this.materials =
          (await request({ url: '/app/materials/by-course/' + this.courseId })) || []
        this.knowledge = (await request({ url: '/app/knowledge' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    openK(k) {
      uni.showModal({ title: k.name, content: k.content || '', showCancel: false })
    },
    onPlay() {
      console.log('视频开始播放')
    },
    onPause() {
      console.log('视频暂停播放')
    },
    onEnded() {
      console.log('视频播放结束')
    },
  },
}
</script>

<style scoped>
.page {
  padding: 24rpx;
}
.h {
  font-size: 36rpx;
  font-weight: 700;
}
.p {
  margin-top: 16rpx;
  font-size: 28rpx;
  line-height: 1.6;
  color: #333;
}
.sub {
  margin-top: 32rpx;
  font-size: 30rpx;
  font-weight: 600;
}
.card {
  background: #f7f8fa;
  padding: 20rpx;
  border-radius: 12rpx;
  margin-top: 12rpx;
}
.mt {
  font-weight: 600;
  display: block;
}
.mc {
  font-size: 26rpx;
  color: #555;
  margin-top: 8rpx;
  display: block;
}
.video-container {
  margin-top: 12rpx;
  border-radius: 16rpx;
  overflow: hidden;
  background: #000;
}
.video-player {
  width: 100%;
  height: 360rpx;
}
.video-title {
  display: block;
  padding: 16rpx 0;
  color: #333;
  font-size: 28rpx;
  font-weight: 600;
}
</style>
