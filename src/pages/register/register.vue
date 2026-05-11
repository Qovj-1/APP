<template>
  <view class="page">
    <input class="input" v-model="account" placeholder="账号" />
    <input class="input" v-model="password" type="password" placeholder="密码（至少6位）" />
    <input class="input" v-model="name" placeholder="姓名" />
    <input class="input" v-model="phone" placeholder="手机号（可选）" />
    <button class="btn" type="primary" :loading="loading" @click="submit">注册</button>
    <navigator class="link" open-type="navigateBack">返回登录</navigator>
  </view>
</template>

<script>
import { request } from '@/utils/api.js'

export default {
  data() {
    return { account: '', password: '', name: '', phone: '', loading: false }
  },
  methods: {
    async submit() {
      if (!this.account || !this.password || !this.name) {
        uni.showToast({ title: '请填写完整', icon: 'none' })
        return
      }
      this.loading = true
      try {
        await request({
          url: '/app/auth/register',
          method: 'POST',
          data: {
            account: this.account,
            password: this.password,
            name: this.name,
            phone: this.phone || undefined,
          },
        })
        uni.showToast({ title: '注册成功，请登录' })
        setTimeout(() => uni.navigateBack(), 500)
      } catch (e) {
        uni.showToast({ title: e.message || '注册失败', icon: 'none' })
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
}
</style>
