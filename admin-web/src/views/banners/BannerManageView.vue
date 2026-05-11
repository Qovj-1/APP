<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Banner {
  id?: number
  imageUrl: string
  linkUrl?: string
  sortOrder?: number
  enabled?: boolean
}

const loading = ref(false)
const list = ref<Banner[]>([])
const dialog = ref(false)
const form = reactive<Banner>({ imageUrl: '', linkUrl: '', sortOrder: 0, enabled: true })

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Banner[]>('/banners')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, { id: undefined, imageUrl: '', linkUrl: '', sortOrder: 0, enabled: true })
  dialog.value = true
}

function openEdit(row: Banner) {
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  try {
    if (form.id) await adminFetch('/banners/' + form.id, { method: 'PUT', json: form })
    else await adminFetch('/banners', { method: 'POST', json: form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Banner) {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await adminFetch('/banners/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">新增轮播</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="imageUrl" label="图片URL" min-width="220" show-overflow-tooltip />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column prop="enabled" label="启用" width="80" />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="Banner" width="560px">
      <el-form label-width="100px">
        <el-form-item label="图片URL"><el-input v-model="form.imageUrl" /></el-form-item>
        <el-form-item label="跳转链接"><el-input v-model="form.linkUrl" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
        <el-form-item label="启用"><el-switch v-model="form.enabled" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
