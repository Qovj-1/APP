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
        <text class="content-title">课程管理</text>
        <view class="header-actions">
          <input 
            class="search-input" 
            v-model="keyword" 
            placeholder="搜索课程名称"
            @confirm="search"
          />
          <button class="add-btn" @click="showAddModal = true">添加</button>
        </view>
      </view>
      <view class="table-container">
        <view class="table-header">
          <text class="th">课程名称</text>
          <text class="th">教师简介</text>
          <text class="th">课时</text>
          <text class="th">评分</text>
          <text class="th">操作</text>
        </view>
        <view class="table-body">
          <view v-for="course in courses" :key="course.id" class="table-row">
            <text class="td">{{ course.name }}</text>
            <text class="td">{{ truncate(course.teacherProfile, 10) }}</text>
            <text class="td">{{ course.time }}课时</text>
            <text class="td">{{ course.star }}</text>
            <view class="td-actions">
              <view class="action-btn edit" @click="editCourse(course)">编辑</view>
              <view class="action-btn delete" @click="deleteCourse(course.id)">删除</view>
            </view>
          </view>
        </view>
        <view v-if="courses.length === 0" class="empty-state">
          <text>暂无数据</text>
        </view>
      </view>
      <view class="pagination">
        <view class="page-btn" :class="{ disabled: currentPage <= 1 }" @click="prevPage">上一页</view>
        <text class="page-info">第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</text>
        <view class="page-btn" :class="{ disabled: currentPage >= totalPages }" @click="nextPage">下一页</view>
      </view>
    </view>
    
    <view v-if="showAddModal" class="modal-overlay" @click="closeModal">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">{{ editingCourse ? '编辑课程' : '添加课程' }}</text>
          <text class="modal-close" @click="closeModal">×</text>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">课程名称</text>
            <input 
              class="form-input" 
              v-model="form.name" 
              placeholder="请输入课程名称"
            />
          </view>
          <view class="form-item">
            <text class="form-label">教师简介</text>
            <textarea 
              class="form-textarea" 
              v-model="form.teacherProfile" 
              placeholder="请输入教师简介"
            />
          </view>
          <view class="form-item">
            <text class="form-label">课程介绍</text>
            <textarea 
              class="form-textarea" 
              v-model="form.introduction" 
              placeholder="请输入课程介绍"
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
            <text class="form-label">评分</text>
            <input 
              class="form-input" 
              v-model="form.star" 
              placeholder="请输入评分"
            />
          </view>
        </view>
        <view class="modal-footer">
          <button class="modal-btn cancel" @click="closeModal">取消</button>
          <button class="modal-btn confirm" @click="saveCourse">{{ editingCourse ? '保存' : '添加' }}</button>
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
      currentMenu: '/pages/admin/courses',
      courses: [],
      keyword: '',
      currentPage: 1,
      totalPages: 1,
      showAddModal: false,
      editingCourse: null,
      form: {
        name: '',
        teacherProfile: '',
        introduction: '',
        time: '',
        star: ''
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
    this.loadCourses()
  },
  methods: {
    checkLogin() {
      const token = uni.getStorageSync('adminToken')
      if (!token) {
        uni.redirectTo({ url: '/pages/admin/login' })
      }
    },
    async loadCourses() {
      try {
        const res = await request({ 
          url: '/admin/courses',
          data: {
            keyword: this.keyword,
            page: this.currentPage - 1
          }
        })
        this.courses = res.content || []
        this.totalPages = res.totalPages || 1
      } catch (e) {
        uni.showToast({ title: e.message || '加载失败', icon: 'none' })
      }
    },
    search() {
      this.currentPage = 1
      this.loadCourses()
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
        this.loadCourses()
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
        this.loadCourses()
      }
    },
    truncate(str, len) {
      if (!str) return '-'
      return str.length > len ? str.substring(0, len) + '...' : str
    },
    editCourse(course) {
      this.editingCourse = course
      this.form = {
        name: course.name || '',
        teacherProfile: course.teacherProfile || '',
        introduction: course.introduction || '',
        time: course.time || '',
        star: course.star || ''
      }
      this.showAddModal = true
    },
    deleteCourse(id) {
      uni.showModal({
        title: '确认删除',
        content: '确定要删除该课程吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await request({
                url: `/admin/courses/${id}`,
                method: 'DELETE'
              })
              uni.showToast({ title: '删除成功', icon: 'success' })
              this.loadCourses()
            } catch (e) {
              uni.showToast({ title: e.message || '删除失败', icon: 'none' })
            }
          }
        }
      })
    },
    closeModal() {
      this.showAddModal = false
      this.editingCourse = null
      this.form = {
        name: '',
        teacherProfile: '',
        introduction: '',
        time: '',
        star: ''
      }
    },
    async saveCourse() {
      if (!this.form.name) {
        uni.showToast({ title: '请填写课程名称', icon: 'none' })
        return
      }
      try {
        if (this.editingCourse) {
          await request({
            url: `/admin/courses/${this.editingCourse.id}`,
            method: 'PUT',
            data: this.form
          })
        } else {
          await request({
            url: '/admin/courses',
            method: 'POST',
            data: this.form
          })
        }
        uni.showToast({ title: this.editingCourse ? '修改成功' : '添加成功', icon: 'success' })
        this.closeModal()
        this.loadCourses()
      } catch (e) {
        uni.showToast({ title: e.message || '操作失败', icon: 'none' })
      }
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
.add-btn {
  height: 64rpx;
  padding: 0 32rpx;
  background: #3498db;
  color: #fff;
  font-size: 26rpx;
  border-radius: 32rpx;
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
  flex: 1;
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.th:last-child {
  flex: 0.6;
}
.table-body {
  padding: 0 24rpx;
}
.table-row {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}
.table-row:last-child {
  border-bottom: none;
}
.td {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}
.td-actions {
  flex: 0.6;
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
.form-item {
  margin-bottom: 24rpx;
}
.form-label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: block;
}
.form-input {
  width: 100%;
  height: 80rpx;
  padding: 0 24rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}
.form-textarea {
  width: 100%;
  height: 160rpx;
  padding: 20rpx 24rpx;
  border: 2rpx solid #e0e0e0;
  border-radius: 12rpx;
  font-size: 28rpx;
  box-sizing: border-box;
}
.modal-footer {
  display: flex;
  padding: 24rpx 32rpx;
  gap: 24rpx;
  border-top: 1rpx solid #e0e0e0;
}
.modal-btn {
  flex: 1;
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
