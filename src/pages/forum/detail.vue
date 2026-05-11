<template>
  <view class="page" v-if="post">
    <view class="t">{{ post.title }}</view>
    <view class="m">{{ post.userAccount }} · {{ post.createTime }}</view>
    <view class="c">{{ post.content }}</view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { post: null }
  },
  onLoad(q) {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
    this.id = q.id
    this.load()
  },
  methods: {
    async load() {
      try {
        this.post = await request({ url: '/app/forum/posts/' + this.id })
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
  },
}
</script>

<style scoped>
.page {
  padding: 24rpx;
}
.t {
  font-size: 34rpx;
  font-weight: 700;
}
.m {
  font-size: 24rpx;
  color: #999;
  margin: 12rpx 0;
}
.c {
  font-size: 28rpx;
  line-height: 1.7;
  color: #333;
}
</style>
