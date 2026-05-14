<template>
  <view class="admin-page">
    <view class="sidebar">
      <view class="sidebar-header">
        <text class="sidebar-title">健康营养师管理系统</text>
      </view>
      <view class="menu-list">
        <view 
          v-for="item in menuItems" 
          :key="item.path"
          :class="['menu-item', { active: currentMenu === item.path }]"
          @click="navigateTo(item.path)"
        >
          <text class="menu-icon">{{ item.icon }}</text>
          <text class="menu-text">{{ item.name }}</text>
        </view>
      </view>
      <view class="sidebar-footer">
        <view class="logout-btn" @click="logout">退出登录</view>
      </view>
    </view>
    <view class="main-content">
      <view class="content-header">
        <text class="content-title">实训管理</text>
        <view class="header-right">
          <input 
            class="search-input" 
            v-model="keyword" 
            placeholder="搜索项目名"
            @confirm="loadTrainings"
          />
          <view class="add-btn" @click="addTraining">+ 添加</view>
        </view>
      </view>
      <view class="table-container">
        <view class="table-header">
          <text class="th">分类号</text>
          <text class="th">项目名</text>
          <text class="th">内容</text>
          <text class="th">课时</text>
          <text class="th">知识点</text>
          <text class="th">难度系数</text>
          <text class="th">操作</text>
        </view>
        <view class="table-body">
          <view v-for="item in trainings" :key="item.trainingId" class="table-row">
            <text class="td">{{ item.trainingId }}</text>
            <text class="td">{{ item.name }}</text>
            <text class="td">{{ truncate(item.content, 20) }}</text>
            <text class="td">{{ item.time }}</text>
            <text class="td">{{ item.knowledgePoints }}</text>
            <text class="td">{{ item.difficult }}</text>
            <view class="td-actions">
              <view class="action-btn edit-btn" @click="editTraining(item)">编辑</view>
              <view class="action-btn delete-btn" @click="deleteTraining(item)">删除</view>
            </view>
          </view>
          <view v-if="trainings.length === 0" class="empty-row">
            <text class="empty-text">暂无数据</text>
          </view>
        </view>
      </view>
      <view class="pagination" v-if="totalPages > 1">
        <view class="page-btn" :class="{ disabled: currentPage === 0 }" @click="prevPage">上一页</view>
        <text class="page-info">第 {{ currentPage + 1 }} 页 / 共 {{ totalPages }} 页</text>
        <view class="page-btn" :class="{ disabled: currentPage >= totalPages - 1 }" @click="nextPage">下一页</view>
      </view>
      
      <view class="modal-overlay" v-if="showModal" @click="closeModal">
        <view class="modal-content" @click.stop>
          <view class="modal-header">
            <text class="modal-title">{{ editingTraining ? '编辑实训' : '添加实训' }}</text>
            <view class="modal-close" @click="closeModal">×</view>
          </view>
          <view class="modal-body">
            <view class="form-item">
              <text class="form-label">分类号</text>
              <input 
                class="form-input" 
                v-model="form.trainingId" 
                :disabled="editingTraining !== null"
                placeholder="请输入分类号"
              />
            </view>
            <view class="form-item">
              <text class="form-label">项目名</text>
              <input 
                class="form-input" 
                v-model="form.name" 
                placeholder="请输入项目名"
              />
            </view>
            <view class="form-item">
              <text class="form-label">内容</text>
              <textarea 
                class="form-textarea" 
                v-model="form.content" 
                placeholder="请输入内容"
              />
            </view>
            <view class="form-item">
              <text class="form-label">课时</text>
              <input 
                class="form-input" 
                type="number" 
                v-model="form.time" 
                placeholder="请输入课时"
              />
            </view>
            <view class="form-item">
              <text class="form-label">相关知识点</text>
              <input 
                class="form-input" 
                v-model="form.knowledgePoints" 
                placeholder="请输入知识点ID"
              />
            </view>
            <view class="form-item">
              <text class="form-label">难度系数</text>
              <input 
                class="form-input" 
                type="digit" 
                v-model="form.difficult" 
                placeholder="请输入难度系数"
              />
            </view>
          </view>
          <view class="modal-footer">
            <view class="btn btn-cancel" @click="closeModal">取消</view>
            <view class="btn btn-confirm" @click="saveTraining">保存</view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { request } from '@/utils/api.js'

export default {
  data() {
    return {
      currentMenu: '/pages/admin/trainings',
      keyword: '',
      currentPage: 0,
      totalPages: 1,
      trainings: [],
      showModal: false,
      editingTraining: null,
      form: {
        trainingId: '',
        name: '',
        content: '',
        time: '',
        knowledgePoints: '',
        difficult: ''
      },
      menuItems: [
        { name: '用户管理', icon: '👥', path: '/pages/admin/users' },
        { name: '课程管理', icon: '📚', path: '/pages/admin/courses' },
        { name: '题库管理', icon: '📝', path: '/pages/admin/questions' },
        { name: '考试管理', icon: '🏆', path: '/pages/admin/exams' },
        { name: '资料管理', icon: '📁', path: '/pages/admin/materials' },
        { name: '论坛管理', icon: '💬', path: '/pages/admin/forum' },
        { name: '实训管理', icon: '⚙️', path: '/pages/admin/trainings' }
      ]
    }
  },
  onShow() {
    this.checkLogin()
    this.loadTrainings()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('adminToken')
      if (!token) {
        uni.redirectTo({ url: '/pages/admin/login' })
      }
    },
    async loadTrainings() {
      try {
        const res = await request({ 
          url: '/admin/trainings',
          params: { page: this.currentPage, keyword: this.keyword }
        })
        this.trainings = res.content || []
        this.totalPages = res.totalPages || 1
      } catch (e) {
        console.error(e)
      }
    },
    navigateTo(path) {
      this.currentMenu = path
      uni.navigateTo({ url: path })
    },
    addTraining() {
      this.editingTraining = null
      this.form = {
        trainingId: '',
        name: '',
        content: '',
        time: '',
        knowledgePoints: '',
        difficult: ''
      }
      this.showModal = true
    },
    editTraining(item) {
      this.editingTraining = item
      this.form = {
        trainingId: item.trainingId || '',
        name: item.name || '',
        content: item.content || '',
        time: item.time || '',
        knowledgePoints: item.knowledgePoints || '',
        difficult: item.difficult || ''
      }
      this.showModal = true
    },
    closeModal() {
      this.showModal = false
      this.editingTraining = null
      this.form = {}
    },
    async saveTraining() {
      if (!this.form.trainingId) {
        uni.showToast({ title: '请输入分类号', icon: 'none' })
        return
      }
      if (!this.form.name) {
        uni.showToast({ title: '请输入项目名', icon: 'none' })
        return
      }
      if (!this.form.content) {
        uni.showToast({ title: '请输入内容', icon: 'none' })
        return
      }
      try {
        if (this.editingTraining) {
          await request({
            url: `/admin/trainings/${this.editingTraining.trainingId}`,
            method: 'PUT',
            data: this.form
          })
          uni.showToast({ title: '修改成功', icon: 'success' })
        } else {
          await request({
            url: '/admin/trainings',
            method: 'POST',
            data: {
              trainingId: this.form.trainingId,
              name: this.form.name,
              content: this.form.content,
              time: parseInt(this.form.time) || 1,
              knowledgePoints: this.form.knowledgePoints,
              difficult: parseFloat(this.form.difficult) || 1.0
            }
          })
          uni.showToast({ title: '添加成功', icon: 'success' })
        }
        this.closeModal()
        this.loadTrainings()
      } catch (e) {
        uni.showToast({ title: e.message || '操作失败', icon: 'none' })
      }
    },
    deleteTraining(item) {
      uni.showModal({
        title: '确认删除',
        content: `确定要删除实训"${item.name}"吗？`,
        success: async (res) => {
          if (res.confirm) {
            try {
              await request({
                url: `/admin/trainings/${item.trainingId}`,
                method: 'DELETE'
              })
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadTrainings()
            } catch (e) {
              uni.showToast({ title: e.message || '删除失败', icon: 'none' })
            }
          }
        }
      })
    },
    truncate(str, len) {
      if (!str) return '-'
      return str.length > len ? str.substring(0, len) + '...' : str
    },
    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--
        this.loadTrainings()
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++
        this.loadTrainings()
      }
    },
    logout() {
      uni.showModal({
        title: '确认退出',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('adminToken')
            uni.removeStorageSync('adminInfo')
            uni.redirectTo({ url: '/pages/admin/login' })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.admin-page {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}
.sidebar {
  width: 300rpx;
  background: #2c3e50;
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
}
.sidebar-header {
  padding: 32rpx;
  background: #34495e;
}
.sidebar-title {
  font-size: 26rpx;
  color: #fff;
  font-weight: 600;
}
.menu-list {
  flex: 1;
  padding: 16rpx;
}
.menu-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  margin-bottom: 8rpx;
  border-radius: 8rpx;
  color: #bdc3c7;
  transition: all 0.3s;
}
.menu-item.active {
  background: #3498db;
  color: #fff;
}
.menu-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}
.menu-text {
  font-size: 28rpx;
}
.sidebar-footer {
  padding: 16rpx;
}
.logout-btn {
  padding: 20rpx;
  background: #e74c3c;
  color: #fff;
  text-align: center;
  border-radius: 8rpx;
  font-size: 28rpx;
}
.main-content {
  flex: 1;
  margin-left: 300rpx;
  padding: 32rpx;
}
.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}
.content-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.search-input {
  width: 300rpx;
  height: 64rpx;
  padding: 0 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 26rpx;
}
.add-btn {
  padding: 16rpx 32rpx;
  background: #3498db;
  color: #fff;
  border-radius: 8rpx;
  font-size: 26rpx;
}
.table-container {
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
  overflow: hidden;
}
.table-header {
  display: flex;
  background: #f8f9fa;
  padding: 24rpx;
}
.th {
  flex: 1;
  font-weight: 600;
  font-size: 26rpx;
  color: #666;
  text-align: center;
}
.table-body {
  padding: 0 24rpx;
}
.table-row {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #eee;
}
.table-row:last-child {
  border-bottom: none;
}
.td {
  flex: 1;
  font-size: 24rpx;
  color: #333;
  text-align: center;
}
.td-actions {
  display: flex;
  gap: 16rpx;
  justify-content: center;
}
.action-btn {
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 22rpx;
}
.edit-btn {
  background: #3498db;
  color: #fff;
}
.delete-btn {
  background: #e74c3c;
  color: #fff;
}
.empty-row {
  padding: 60rpx;
  text-align: center;
}
.empty-text {
  font-size: 28rpx;
  color: #999;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 24rpx;
  margin-top: 32rpx;
}
.page-btn {
  padding: 16rpx 32rpx;
  background: #fff;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 26rpx;
}
.page-btn.disabled {
  opacity: 0.5;
}
.page-info {
  font-size: 26rpx;
  color: #666;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  width: 600rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid #eee;
}
.modal-title {
  font-size: 32rpx;
  font-weight: 600;
}
.modal-close {
  font-size: 40rpx;
  color: #999;
}
.modal-body {
  padding: 32rpx;
}
.form-item {
  margin-bottom: 24rpx;
}
.form-label {
  display: block;
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
}
.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}
.form-textarea {
  width: 100%;
  height: 160rpx;
  padding: 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}
.modal-footer {
  display: flex;
  gap: 24rpx;
  padding: 32rpx;
  border-top: 1rpx solid #eee;
}
.btn {
  flex: 1;
  padding: 24rpx;
  text-align: center;
  border-radius: 8rpx;
  font-size: 28rpx;
}
.btn-cancel {
  background: #f5f5f5;
  color: #666;
}
.btn-confirm {
  background: #3498db;
  color: #fff;
}
</style>