<template>
  <view class="page">
    <view v-for="q in list" :key="q.id" class="row">
      <view class="t">{{ q.title }}</view>
      <button size="mini" type="warn" @click="remove(q.id)">移除</button>
    </view>
    <view v-if="!list.length" class="empty">暂无错题</view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return { list: [] }
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
        this.list = (await request({ url: '/app/wrongs' })) || []
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    async remove(id) {
      try {
        await request({ url: '/app/wrongs/' + id, method: 'DELETE' })
        this.load()
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
.row {
  background: #fff;
  padding: 20rpx;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.t {
  flex: 1;
  font-size: 26rpx;
  margin-right: 16rpx;
}
.empty {
  text-align: center;
  color: #999;
  margin-top: 80rpx;
}
</style>
