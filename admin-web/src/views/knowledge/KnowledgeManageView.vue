<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Kp {
  id?: number
  categoryId?: number
  name: string
  content?: string
  videoUrl?: string
  difficult?: number
  importance?: number
  example?: string
}

const loading = ref(false)
const list = ref<Kp[]>([])
const dialog = ref(false)
const form = reactive<Kp>({ name: '', categoryId: undefined, content: '', videoUrl: '', example: '' })

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Kp[]>('/knowledge')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, { id: undefined, name: '', categoryId: undefined, content: '', videoUrl: '', example: '' })
  dialog.value = true
}

function openEdit(row: Kp) {
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  try {
    if (form.id) await adminFetch('/knowledge/' + form.id, { method: 'PUT', json: form })
    else await adminFetch('/knowledge', { method: 'POST', json: form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Kp) {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await adminFetch('/knowledge/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">新增知识点</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" min-width="140" />
      <el-table-column prop="categoryId" label="分类号" width="90" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="知识点" width="700px">
      <el-form label-width="100px">
        <el-form-item label="分类号"><el-input-number v-model="form.categoryId" :min="0" /></el-form-item>
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="视频URL"><el-input v-model="form.videoUrl" /></el-form-item>
        <el-form-item label="难度"><el-input-number v-model="form.difficult" :step="0.1" /></el-form-item>
        <el-form-item label="重要性"><el-input-number v-model="form.importance" :step="0.1" /></el-form-item>
        <el-form-item label="应用举例"><el-input v-model="form.example" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
