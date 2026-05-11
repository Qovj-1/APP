<template>
  <view class="page">
    <input class="inp" v-model="title" placeholder="标题" />
    <textarea class="area" v-model="content" placeholder="内容" />
    <button type="primary" @click="submit">发布</button>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { title: '', content: '' }
  },
  onShow() {
    if (!getToken()) {
      uni.redirectTo({ url: '/pages/login/login' })
      return
    }
  },
  methods: {
    async submit() {
      if (!this.title || !this.content) {
        uni.showToast({ title: '请填写完整', icon: 'none' })
        return
      }
      try {
        await request({
          url: '/app/forum/posts',
          method: 'POST',
          data: { title: this.title, content: this.content },
        })
        uni.showToast({ title: '发布成功' })
        setTimeout(() => uni.navigateBack(), 400)
      } catch (e) {
        uni.showToast({ title: e.message || '失败', icon: 'none' })
      }
    },
  },
}
</script>

<style scoped>
.page {
  padding: 24rpx;
}
.inp {
  border: 1px solid #eee;
  padding: 20rpx;
  margin-bottom: 16rpx;
  border-radius: 8rpx;
}
.area {
  width: 100%;
  min-height: 240rpx;
  border: 1px solid #eee;
  padding: 16rpx;
  margin-bottom: 24rpx;
}
</style>
