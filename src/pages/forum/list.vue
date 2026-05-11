<template>
  <view class="page">
    <button type="primary" class="post" @click="goPost">发帖</button>
    <view v-for="p in posts" :key="p.id" class="card" @click="go(p.id)">
      <text class="t">{{ p.title }}</text>
      <text class="m">{{ p.userAccount }} · {{ p.createTime }}</text>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { posts: [] }
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
        this.posts = (await request({ url: '/app/forum/posts' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    go(id) {
      uni.navigateTo({ url: '/pages/forum/detail?id=' + id })
    },
    goPost() {
      uni.navigateTo({ url: '/pages/forum/post' })
    },
  },
}
</script>

<style scoped>
.page {
  padding: 24rpx;
}
.post {
  margin-bottom: 24rpx;
}
.card {
  background: #fff;
  padding: 24rpx;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
}
.t {
  font-size: 30rpx;
  font-weight: 600;
  display: block;
}
.m {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}
</style>
