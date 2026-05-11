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
        <text class="content-title">论坛管理</text>
        <view class="header-actions">
          <input 
            class="search-input" 
            v-model="keyword" 
            placeholder="搜索帖子标题"
            @confirm="search"
          />
          <button class="add-btn" @click="addPost">添加</button>
        </view>
      </view>
      <view class="table-container">
        <view class="table-header">
          <text class="th">ID</text>
          <text class="th">标题</text>
          <text class="th">内容</text>
          <text class="th">发布者</text>
          <text class="th">发布日期</text>
          <text class="th">操作</text>
        </view>
        <view class="table-body">
          <view v-for="post in posts" :key="post.id" class="table-row">
            <text class="td">{{ post.id }}</text>
            <text class="td">{{ truncate(post.title, 20) }}</text>
            <text class="td">{{ truncate(post.content, 30) }}</text>
            <text class="td">{{ post.userAccount }}</text>
            <text class="td">{{ formatTime(post.createTime) }}</text>
            <view class="td-actions">
              <view class="action-btn edit" @click="viewDetail(post)">编辑</view>
              <view class="action-btn delete" @click="deletePost(post.id)">删除</view>
            </view>
          </view>
        </view>
        <view v-if="posts.length === 0" class="empty-state">
          <text>暂无数据</text>
        </view>
      </view>
      <view class="pagination">
        <view class="page-btn" :class="{ disabled: currentPage <= 1 }" @click="prevPage">上一页</view>
        <text class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</text>
        <view class="page-btn" :class="{ disabled: currentPage >= totalPages }" @click="nextPage">下一页</view>
      </view>
    </view>
    
    <view v-if="showDetailModal" class="modal-overlay" @click="closeModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">{{ editingPost ? '编辑帖子' : '添加帖子' }}</text>
          <text class="modal-close" @click="closeModal">×</text>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">标题</text>
            <input 
              class="form-input" 
              v-model="editForm.title" 
              placeholder="请输入帖子标题"
            />
          </view>
          <view class="form-item">
            <text class="form-label">作者</text>
            <input 
              class="form-input" 
              v-model="editForm.authorName" 
              placeholder="请输入作者"
            />
          </view>
          <view class="form-item">
            <text class="form-label">内容</text>
            <textarea 
              class="form-textarea" 
              v-model="editForm.content" 
              placeholder="请输入帖子内容"
            />
          </view>
        </view>
        <view class="modal-footer">
          <button class="modal-btn cancel" @click="closeModal">取消</button>
          <button class="modal-btn confirm" @click="savePost">{{ editingPost ? '保存' : '添加' }}</button>
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
      currentMenu: '/pages/admin/forum',
      posts: [],
      keyword: '',
      currentPage: 1,
      totalPages: 1,
      showDetailModal: false,
      editingPost: null,
      editForm: {
        title: '',
        authorName: '',
        content: ''
      },
      menuItems: [
        { name: '用户管理', icon: '👥', path: '/pages/admin/users' },
        { name: '课程管理', icon: '📚', path: '/pages/admin/courses' },
        { name: '题库管理', icon: '📝', path: '/pages/admin/questions' },
        { name: '考试管理', icon: '🏆', path: '/pages/admin/exams' },
        { name: '资料管理', icon: '📁', path: '/pages/admin/materials' },
        { name: '论坛管理', icon: '💬', path: '/pages/admin/forum' }
      ]
    }
  },
  onShow() {
    this.checkLogin()
    this.loadPosts()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('adminToken')
      if (!token) {
        uni.redirectTo({ url: '/pages/admin/login' })
      }
    },
    async loadPosts() {
      try {
        const res = await request({ 
          url: '/admin/forum',
          data: {
            keyword: this.keyword,
            page: this.currentPage - 1
          }
        })
        this.posts = res.content || []
        this.totalPages = res.totalPages || 1
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    search() {
      this.currentPage = 1
      this.loadPosts()
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
        this.loadPosts()
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
        this.loadPosts()
      }
    },
    truncate(str, len) {
      if (!str) return '-'
      return str.length > len ? str.substring(0, len) + '...' : str
    },
    formatTime(time) {
      if (!time) return '-'
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    viewDetail(post) {
      this.editingPost = post
      this.editForm = {
        title: post.title || '',
        authorName: post.userAccount || '',
        content: post.content || ''
      }
      this.showDetailModal = true
    },
    addPost() {
      this.editingPost = null
      this.editForm = {
        title: '',
        authorName: '',
        content: ''
      }
      this.showDetailModal = true
    },
    async savePost() {
      if (!this.editForm.title.trim()) {
        uni.showToast({ title: '请输入标题', icon: 'none' })
        return
      }
      try {
        if (this.editingPost) {
          await request({
            url: `/admin/forum/posts/${this.editingPost.id}`,
            method: 'PUT',
            data: {
              title: this.editForm.title,
              content: this.editForm.content,
              authorName: this.editForm.authorName
            }
          })
        } else {
          await request({
            url: '/admin/forum/posts',
            method: 'POST',
            data: {
              title: this.editForm.title,
              content: this.editForm.content,
              authorName: this.editForm.authorName
            },
            params: { userAccount: this.editForm.authorName || 'admin' }
          })
        }
        uni.showToast({ title: this.editingPost ? '保存成功' : '添加成功', icon: 'success' })
        this.closeModal()
        this.loadPosts()
      } catch (e) {
        uni.showToast({ title: e.message || '操作失败', icon: 'none' })
      }
    },
    deletePost(id) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除该帖子吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await request({
                url: `/admin/forum/posts/${id}`,
                method: 'DELETE'
              })
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadPosts()
            } catch (e) {
              uni.showToast({ title: e.message || '删除失败', icon: 'none' })
            }
          }
        }
      })
    },
    closeModal() {
      this.showDetailModal = false
      this.editingPost = null
    },
    navigateTo(path) {
      this.currentMenu = path
      uni.redirectTo({ url: path })
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
.header-actions {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.search-input {
  width: 300rpx;
  height: 64rpx;
  padding: 0 20rpx;
  border: 2rpx solid #ddd;
  border-radius: 32rpx;
  font-size: 26rpx;
}
.table-container {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
}
.table-header {
  display: flex;
  background: #f8f9fa;
  padding: 24rpx;
  border-bottom: 1rpx solid #e0e0e0;
}
.th {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.th:nth-child(1) { width: 80rpx; }
.th:nth-child(2) { width: 200rpx; }
.th:nth-child(3) { flex: 1; }
.th:nth-child(4) { width: 140rpx; text-align: center; }
.th:nth-child(5) { width: 200rpx; text-align: center; }
.th:nth-child(6) { width: 160rpx; }
.table-body {
  padding: 0 24rpx;
}
.table-row {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  align-items: center;
}
.table-row:last-child {
  border-bottom: none;
}
.td {
  font-size: 26rpx;
  color: #333;
}
.td:nth-child(1) { width: 80rpx; }
.td:nth-child(2) { width: 200rpx; }
.td:nth-child(3) { flex: 1; }
.td:nth-child(4) { width: 140rpx; text-align: center; }
.td:nth-child(5) { width: 200rpx; text-align: center; }
.td-actions {
  width: 160rpx;
  display: flex;
  gap: 16rpx;
}
.action-btn {
  padding: 12rpx 24rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
}
.action-btn.edit {
  background: #e8f4fd;
  color: #3498db;
}
.action-btn.delete {
  background: #ffebee;
  color: #e74c3c;
}
.empty-state {
  padding: 64rpx;
  text-align: center;
  color: #999;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 32rpx;
  gap: 32rpx;
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
}
.modal-content {
  width: 600rpx;
  background: #fff;
  border-radius: 24rpx;
  overflow: hidden;
  max-height: 80vh;
}
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid #e0e0e0;
}
.modal-title {
  font-size: 32rpx;
  font-weight: 600;
}
.modal-close {
  font-size: 48rpx;
  color: #999;
  line-height: 1;
}
.modal-body {
  padding: 32rpx;
  max-height: 60vh;
  overflow-y: auto;
}
.detail-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.detail-item.full {
  flex-direction: column;
}
.detail-label {
  width: 150rpx;
  font-size: 26rpx;
  color: #666;
}
.detail-item.full .detail-label {
  width: auto;
  margin-bottom: 12rpx;
}
.detail-value {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}
.detail-content {
  font-size: 26rpx;
  color: #333;
  line-height: 1.6;
}
.modal-footer {
  padding: 24rpx 32rpx;
  border-top: 1rpx solid #e0e0e0;
}
.modal-btn {
  width: 100%;
  height: 80rpx;
  font-size: 28rpx;
  border-radius: 40rpx;
}
.modal-btn.cancel {
  background: #f5f5f5;
  color: #666;
}
.modal-btn.confirm {
  background: #3498db;
  color: #fff;
}
</style>