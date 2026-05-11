<template>
  <view class="page">
    <view class="title">营养师学习平台</view>
    <input class="input" v-model="account" placeholder="账号" />
    <input class="input" v-model="password" type="password" placeholder="密码" />
    <button class="btn" type="primary" :loading="loading" @click="submit">登录</button>
    <navigator class="link" url="/pages/register/register">没有账号？去注册</navigator>
  </view>
</template>

<script>
import { request, setAuth } from '@/utils/api.js'

export default {
  data() {
    return { account: '', password: '', loading: false }
  },
  onShow() {
    const t = uni.getStorageSync('app_token')
    if (t) uni.switchTab({ url: '/pages/home/home' })
  },
  methods: {
    async submit() {
      if (!this.account || !this.password) {
        uni.showToast({ title: '请输入账号密码', icon: 'none' })
        return
      }
      this.loading = true
      try {
        const data = await request({
          url: '/app/auth/login',
          method: 'POST',
          data: { account: this.account, password: this.password },
        })
        setAuth(data.token, data.account, data.name)
        uni.showToast({ title: '登录成功' })
        setTimeout(() => uni.switchTab({ url: '/pages/home/home' }), 400)
      } catch (e) {
        uni.showToast({ title: e.message || '登录失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
  },
}
</script>

<style scoped>
.page {
  padding: 48rpx;
}
.title {
  font-size: 40rpx;
  font-weight: 600;
  margin-bottom: 48rpx;
  text-align: center;
}
.input {
  border: 1px solid #eee;
  padding: 24rpx;
  margin-bottom: 24rpx;
  border-radius: 12rpx;
}
.btn {
  margin-top: 24rpx;
}
.link {
  display: block;
  margin-top: 32rpx;
  color: #1989fa;
  text-align: center;
  font-size: 28rpx;
}
</style>
