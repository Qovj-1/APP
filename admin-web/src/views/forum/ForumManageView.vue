<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Post {
  id: number
  title: string
  content: string
  userAccount: string
  createTime: string
}

const loading = ref(false)
const list = ref<Post[]>([])
const dialog = ref(false)
const form = reactive({ title: '', content: '', userAccount: 'demo' })

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Post[]>('/forum/posts')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  form.title = ''
  form.content = ''
  form.userAccount = 'demo'
  dialog.value = true
}

async function save() {
  try {
    const q = new URLSearchParams()
    q.set('userAccount', form.userAccount)
    await adminFetch('/forum/posts?' + q.toString(), {
      method: 'POST',
      json: { title: form.title, content: form.content },
    })
    ElMessage.success('已发布')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Post) {
  try {
    await ElMessageBox.confirm('删除帖子？', '提示', { type: 'warning' })
    await adminFetch('/forum/posts/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">代用户发帖</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="userAccount" label="发布者" width="120" />
      <el-table-column prop="createTime" label="时间" min-width="160" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="发帖" width="600px">
      <el-form label-width="88px">
        <el-form-item label="发布账号"><el-input v-model="form.userAccount" placeholder="系统中存在的用户账号" /></el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="5" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">发布</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
