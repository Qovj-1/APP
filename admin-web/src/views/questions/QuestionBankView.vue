<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface Question {
  id?: number
  source: string
  questionType: string
  title: string
  optionsJson?: string
  answer: string
  analysis?: string
  knowledgePointId?: number | null
  difficult?: number
  categoryCode?: string
  year?: number | null
}

const loading = ref(false)
const list = ref<Question[]>([])
const dialog = ref(false)
const form = reactive<Question>({
  source: 'EXERCISE',
  questionType: 'SINGLE',
  title: '',
  optionsJson: '',
  answer: '',
  analysis: '',
  knowledgePointId: null,
})

async function load() {
  loading.value = true
  try {
    list.value = await adminFetch<Question[]>('/questions')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, {
    id: undefined,
    source: 'EXERCISE',
    questionType: 'SINGLE',
    title: '',
    optionsJson: '',
    answer: '',
    analysis: '',
    knowledgePointId: null,
  })
  dialog.value = true
}

function openEdit(row: Question) {
  Object.assign(form, row)
  dialog.value = true
}

async function save() {
  try {
    if (form.id) await adminFetch('/questions/' + form.id, { method: 'PUT', json: form })
    else await adminFetch('/questions', { method: 'POST', json: form })
    ElMessage.success('已保存')
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '失败')
  }
}

async function removeRow(row: Question) {
  try {
    await ElMessageBox.confirm('确定删除？', '提示', { type: 'warning' })
    await adminFetch('/questions/' + row.id, { method: 'DELETE' })
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
    <el-button type="success" @click="openCreate">新增题目</el-button>
    <el-table v-loading="loading" :data="list" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="source" label="来源" width="90" />
      <el-table-column prop="questionType" label="类型" width="90" />
      <el-table-column prop="title" label="题干" min-width="200" show-overflow-tooltip />
      <el-table-column label="操作" width="140">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="removeRow(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialog" title="题目" width="720px" destroy-on-close>
      <el-form label-width="100px">
        <el-form-item label="来源">
          <el-select v-model="form.source">
            <el-option label="习题" value="EXERCISE" />
            <el-option label="模拟" value="MOCK" />
            <el-option label="真题" value="REAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="题型">
          <el-select v-model="form.questionType">
            <el-option label="单选" value="SINGLE" />
            <el-option label="多选" value="MULTI" />
            <el-option label="案例" value="CASE" />
          </el-select>
        </el-form-item>
        <el-form-item label="题干"><el-input v-model="form.title" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="选项JSON">
          <el-input v-model="form.optionsJson" type="textarea" :rows="4" placeholder='[{"key":"A","text":"..."}]' />
        </el-form-item>
        <el-form-item label="答案"><el-input v-model="form.answer" /></el-form-item>
        <el-form-item label="解析"><el-input v-model="form.analysis" type="textarea" :rows="2" /></el-form-item>
        <el-form-item label="知识点ID"><el-input-number v-model="form.knowledgePointId" :min="0" /></el-form-item>
        <el-form-item label="难度"><el-input-number v-model="form.difficult" :step="0.1" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
