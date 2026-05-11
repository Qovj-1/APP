<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Paper {
  id?: number
  title: string
  typeId?: number
  difficult?: number
  questionIds: string
  durationMinutes?: number
}

const loading = ref(false)
const list = ref<Paper[]>([])
const dialog = ref(false)
const form = reactive<Paper>({ title: '', typeId: 1, difficult: 2, questionIds: '[1,2,3]', durationMinutes: 60 })

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Paper[]>('/exam-papers')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, { id: undefined, title: '', typeId: 1, difficult: 2, questionIds: '[]', durationMinutes: 60 })
  dialog.value = true
}

function openEdit(row: Paper) {
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  try {
    if (form.id) await adminFetch('/exam-papers/' + form.id, { method: 'PUT', json: form })
    else await adminFetch('/exam-papers', { method: 'POST', json: form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Paper) {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await adminFetch('/exam-papers/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">新增试卷</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="160" />
      <el-table-column prop="durationMinutes" label="时长" width="80" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="试卷" width="640px">
      <el-form label-width="110px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="分类号"><el-input-number v-model="form.typeId" :min="0" /></el-form-item>
        <el-form-item label="难度"><el-input-number v-model="form.difficult" :step="0.1" /></el-form-item>
        <el-form-item label="题目ID列表">
          <el-input v-model="form.questionIds" type="textarea" :rows="3" placeholder='JSON 数组，如 [1,2,3]' />
        </el-form-item>
        <el-form-item label="考试时长(分)"><el-input-number v-model="form.durationMinutes" :min="1" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
