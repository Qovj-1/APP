<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const active = computed(() => route.path)

const menu = [
  { path: '/dashboard', title: '工作台', icon: 'DataBoard' },
  { path: '/users', title: '用户管理', icon: 'User' },
  { path: '/courses', title: '课程管理', icon: 'Reading' },
  { path: '/materials', title: '资料管理', icon: 'Document' },
  { path: '/knowledge', title: '知识点', icon: 'FolderOpened' },
  { path: '/questions', title: '题库管理', icon: 'EditPen' },
  { path: '/exam-papers', title: '试卷', icon: 'Document' },
  { path: '/exams', title: '成绩', icon: 'TrendCharts' },
  { path: '/forum', title: '论坛', icon: 'ChatDotRound' },
  { path: '/banners', title: '轮播图', icon: 'Picture' },
]

function logout() {
  auth.clearSession()
  router.push({ name: 'login' })
}
</script>

<template>
  <el-container class="admin-shell">
    <el-aside width="220px" class="aside">
      <div class="brand">营养师系统 · 管理端</div>
      <el-menu :default-active="active" router class="menu">
        <el-menu-item v-for="m in menu" :key="m.path" :index="m.path">
          <el-icon><component :is="m.icon" /></el-icon>
          <span>{{ m.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="title">{{ (route.meta.title as string) ?? '' }}</span>
        <div class="right">
          <span class="account">{{ auth.account ?? '未登录' }}</span>
          <el-button type="primary" link @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.admin-shell {
  height: 100%;
}
.aside {
  border-right: 1px solid var(--el-border-color);
  display: flex;
  flex-direction: column;
}
.brand {
  padding: 16px;
  font-weight: 600;
  font-size: 14px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}
.menu {
  border-right: none;
  flex: 1;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--el-border-color-lighter);
}
.title {
  font-size: 16px;
  font-weight: 600;
}
.right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.account {
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
.main {
  background: var(--el-bg-color-page);
}
</style>
