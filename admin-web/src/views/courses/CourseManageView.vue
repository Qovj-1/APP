<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Course {
  id?: number
  name: string
  teacherProfile?: string
  introduction?: string
  videoUrl?: string
  timeHours?: number
  star?: string
}

const loading = ref(false)
const list = ref<Course[]>([])
const dialog = ref(false)
const form = reactive<Course>({ name: '', teacherProfile: '', introduction: '', videoUrl: '', timeHours: 0, star: '' })

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Course[]>('/courses')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, { id: undefined, name: '', teacherProfile: '', introduction: '', videoUrl: '', timeHours: 0, star: '' })
  dialog.value = true
}

function openEdit(row: Course) {
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  try {
    if (form.id) {
      await adminFetch('/courses/' + form.id, { method: 'PUT', json: form })
    } else {
      await adminFetch('/courses', { method: 'POST', json: form })
    }
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Course) {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await adminFetch('/courses/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">新增课程</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" min-width="140" />
      <el-table-column prop="star" label="星级" width="80" />
      <el-table-column prop="timeHours" label="课时" width="80" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="课程" width="600px">
      <el-form label-width="100px">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="教师简介"><el-input v-model="form.teacherProfile" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="内容介绍"><el-input v-model="form.introduction" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="视频URL"><el-input v-model="form.videoUrl" /></el-form-item>
        <el-form-item label="课时"><el-input-number v-model="form.timeHours" :min="0" /></el-form-item>
        <el-form-item label="星级"><el-input v-model="form.star" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
