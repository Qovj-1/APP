<template>
  <view class="page">
    <view class="header">
      <text class="back" @click="goBack">‹</text>
      <text class="title">修改信息</text>
    </view>
    <view class="form">
      <view class="avatar-section">
        <view class="avatar-preview" @click="chooseAvatar">
          <image 
            v-if="avatarUrl" 
            class="avatar-image" 
            :src="avatarUrl" 
            mode="aspectFill"
          />
          <view v-else class="avatar-placeholder">
            <text class="avatar-placeholder-text">点击上传</text>
          </view>
        </view>
        <text class="avatar-hint">点击头像上传图片</text>
      </view>
      <view class="form-item">
        <text class="form-label">姓名</text>
        <input 
          class="form-input" 
          v-model="form.name" 
          placeholder="请输入姓名"
        />
      </view>
      <view class="form-item">
        <text class="form-label">文化程度</text>
        <input 
          class="form-input" 
          v-model="form.education" 
          placeholder="请输入文化程度"
        />
      </view>
      <view class="form-item">
        <text class="form-label">电话</text>
        <input 
          class="form-input" 
          type="number" 
          v-model="form.phone" 
          placeholder="请输入电话号码"
        />
      </view>
      <view class="form-item">
        <text class="form-label">地址</text>
        <input 
          class="form-input" 
          v-model="form.address" 
          placeholder="请输入地址"
        />
      </view>
      <view class="form-item">
        <text class="form-label">邮箱</text>
        <input 
          class="form-input" 
          type="email" 
          v-model="form.email" 
          placeholder="请输入邮箱"
        />
      </view>
      <button class="submit-btn" @click="submit" :loading="loading">保存修改</button>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return {
      form: { name: '', education: '', phone: '', address: '', email: '' },
      avatarUrl: '',
      loading: false,
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
    goBack() {
      uni.navigateBack()
    },
    async load() {
      try {
        const user = await request({ url: '/app/me' })
        this.form = {
          name: user.name || '',
          education: user.education || '',
          phone: user.phone || '',
          address: user.address || '',
          email: user.email || '',
        }
        this.avatarUrl = user.avatarUrl || ''
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadAvatar(res.tempFilePaths[0])
        },
      })
    },
    async uploadAvatar(filePath) {
      try {
        this.loading = true
        const res = await uni.uploadFile({
          url: 'http://localhost:8080/api/app/me/avatar',
          filePath: filePath,
          name: 'file',
          header: {
            Authorization: 'Bearer ' + getToken(),
          },
        })
        
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          this.avatarUrl = data.data.url
          uni.showToast({ title: '头像上传成功', icon: 'success' })
        } else {
          uni.showToast({ title: data.message || '上传失败', icon: 'none' })
        }
      } catch (e) {
        uni.showToast({ title: e.message || '上传失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    async submit() {
      try {
        this.loading = true
        await request({
          url: '/app/me',
          method: 'PUT',
          data: this.form,
        })
        uni.showToast({ title: '修改成功', icon: 'success' })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (e) {
        uni.showToast({ title: e.message || '修改失败', icon: 'none' })
      } finally {
        this.loading = false
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
.form {
  padding: 32rpx 24rpx;
}
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 32rpx;
}
.avatar-preview {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border: 2rpx dashed #ccc;
}
.avatar-image {
  width: 160rpx;
  height: 160rpx;
}
.avatar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-placeholder-text {
  font-size: 24rpx;
  color: #999;
}
.avatar-hint {
  font-size: 24rpx;
  color: #999;
  margin-top: 12rpx;
}
.form-item {
  background: #fff;
  padding: 24rpx;
  margin-bottom: 16rpx;
  border-radius: 12rpx;
}
.form-label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: block;
}
.form-input {
  font-size: 28rpx;
  padding: 12rpx 0;
  border-bottom: 1px solid #f0f0f0;
}
.submit-btn {
  margin-top: 32rpx;
  background: #1989fa;
  color: #fff;
  border-radius: 44rpx;
}
</style>
