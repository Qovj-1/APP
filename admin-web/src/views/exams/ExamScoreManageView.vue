<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Score {
  id: number
  userAccount: string
  userName?: string
  paperId: number
  examTime: string
  score?: number
}

interface PageScores {
  content: Score[]
  totalElements: number
}

const loading = ref(false)
const table = ref<Score[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 15 })

async function load() {
  loading.value = true
  try {
    const q = new URLSearchParams()
    q.set('page', String(page.current - 1))
    q.set('size', String(page.size))
    const data = await adminFetch<PageScores>('/scores?' + q.toString())
    table.value = data.content ?? []
    total.value = data.totalElements ?? 0
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

async function removeRow(row: Score) {
  try {
    await ElMessageBox.confirm('删除该成绩记录？', '提示', { type: 'warning' })
    await adminFetch('/scores/' + row.id, { method: 'DELETE' })
    ElMessage.success('已删除')
    await load()
  } catch {
    /* */
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <el-table v-loading="loading" :data="table" border>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="userAccount" label="用户" width="120" />
      <el-table-column prop="userName" label="姓名" width="100" />
      <el-table-column prop="paperId" label="试卷ID" width="90" />
      <el-table-column prop="examTime" label="考试时间" min-width="180" />
      <el-table-column prop="score" label="分数" width="80" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page.current"
      v-model:page-size="page.size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 16px"
      @current-change="load"
      @size-change="load"
    />
  </el-card>
</template>
