<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { adminFetch } from '@/api/client'

const stats = ref<Record<string, number> | null>(null)

onMounted(async () => {
  try {
    stats.value = await adminFetch<Record<string, number>>('/dashboard/stats')
  } catch {
    stats.value = null
  }
})

const panels = [
  { title: '用户管理', desc: '平台用户增删改查、搜索', to: '/users' },
  { title: '课程管理', desc: '课程资源维护', to: '/courses' },
  { title: '资料管理', desc: '学习资料维护', to: '/materials' },
  { title: '知识点', desc: '知识点维护', to: '/knowledge' },
  { title: '题库管理', desc: '单选 / 多选 / 案例题', to: '/questions' },
  { title: '试卷管理', desc: '实例实战试卷', to: '/exam-papers' },
  { title: '考试与成绩', desc: '成绩查询与删除', to: '/exams' },
  { title: '论坛管理', desc: '帖子维护', to: '/forum' },
  { title: '轮播图', desc: '首页 Banner', to: '/banners' },
]
</script>

<template>
  <div class="wrap">
    <el-row v-if="stats" :gutter="16" class="stats">
      <el-col :span="4">
        <el-statistic title="用户" :value="stats.users" />
      </el-col>
      <el-col :span="4">
        <el-statistic title="课程" :value="stats.courses" />
      </el-col>
      <el-col :span="4">
        <el-statistic title="题目" :value="stats.questions" />
      </el-col>
      <el-col :span="4">
        <el-statistic title="成绩记录" :value="stats.scores" />
      </el-col>
      <el-col :span="4">
        <el-statistic title="帖子" :value="stats.posts" />
      </el-col>
    </el-row>
    <el-alert
      title="以下为各业务模块入口，数据已对接后端 MySQL。"
      type="success"
      show-icon
      :closable="false"
      class="tip"
    />
    <el-row :gutter="16" class="row">
      <el-col v-for="p in panels" :key="p.to" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="card">
          <div class="title">{{ p.title }}</div>
          <p class="desc">{{ p.desc }}</p>
          <router-link :to="p.to">
            <el-button type="primary" link>进入</el-button>
          </router-link>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.wrap {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.stats {
  background: #fff;
  padding: 16px;
  border-radius: 8px;
}
.tip {
  margin-top: 0;
}
.row {
  margin-top: 8px;
}
.card {
  margin-bottom: 8px;
  min-height: 120px;
}
.title {
  font-weight: 600;
  margin-bottom: 8px;
}
.desc {
  margin: 0 0 12px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.5;
}
</style>
