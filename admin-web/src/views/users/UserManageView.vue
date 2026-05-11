<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminFetch } from '@/api/client'

interface AppUserRow {
  account: string
  name: string
  education?: string
  phone?: string
  address?: string
  email?: string
  avatarUrl?: string
}

interface PageUsers {
  content: AppUserRow[]
  totalElements: number
}

const loading = ref(false)
const table = ref<AppUserRow[]>([])
const total = ref(0)
const page = reactive({ current: 1, size: 10 })
const keyword = ref('')

const dialog = ref(false)
const editAccount = ref<string | null>(null)
const form = reactive({
  account: '',
  password: '',
  name: '',
  education: '',
  phone: '',
  address: '',
  email: '',
})

async function load() {
  loading.value = true
  try {
    const q = new URLSearchParams()
    q.set('page', String(page.current - 1))
    q.set('size', String(page.size))
    if (keyword.value) q.set('keyword', keyword.value)
    const data = await adminFetch<PageUsers>('/users?' + q.toString())
    table.value = data.content ?? []
    total.value = data.totalElements ?? 0
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '加载失败')
  } finally {
    loading.value = false
  }
}

function openCreate() {
  editAccount.value = null
  Object.assign(form, { account: '', password: '', name: '', education: '', phone: '', address: '', email: '' })
  dialog.value = true
}

function openEdit(row: AppUserRow) {
  editAccount.value = row.account
  Object.assign(form, {
    account: row.account,
    password: '',
    name: row.name,
    education: row.education ?? '',
    phone: row.phone ?? '',
    address: row.address ?? '',
    email: row.email ?? '',
  })
  dialog.value = true
}

async function save() {
  try {
    if (editAccount.value) {
      await adminFetch(`/users/${encodeURIComponent(editAccount.value)}`, {
        method: 'PUT',
        json: {
          name: form.name || undefined,
          education: form.education || undefined,
          phone: form.phone || undefined,
          address: form.address || undefined,
          email: form.email || undefined,
          newPassword: form.password || undefined,
        },
      })
      ElMessage.success('已保存')
    } else {
      await adminFetch('/users', {
        method: 'POST',
        json: {
          account: form.account,
          password: form.password,
          name: form.name,
          education: form.education || undefined,
          phone: form.phone || undefined,
          address: form.address || undefined,
          email: form.email || undefined,
        },
      })
      ElMessage.success('已创建')
    }
    dialog.value = false
    await load()
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '保存失败')
  }
}

async function removeRow(row: AppUserRow) {
  try {
    await ElMessageBox.confirm('确定删除用户 ' + row.account + ' ?', '提示', { type: 'warning' })
    await adminFetch('/users/' + encodeURIComponent(row.account), { method: 'DELETE' })
    ElMessage.success('已删除')
    await load()
  } catch {
    /* cancel */
  }
}

onMounted(load)
</script>

<template>
  <el-card shadow="never">
    <div class="toolbar">
      <el-input v-model="keyword" placeholder="搜索账号/姓名" clearable style="width: 220px" @clear="load" />
      <el-button type="primary" @click="() => { page.current = 1; load() }">搜索</el-button>
      <el-button type="success" @click="openCreate">新增用户</el-button>
    </div>
    <el-table v-loading="loading" :data="table" border style="width: 100%; margin-top: 12px">
      <el-table-column prop="account" label="账号" width="140" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column prop="education" label="文化程度" width="100" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="openEdit(row)">编辑</el-button>
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

    <el-dialog v-model="dialog" :title="editAccount ? '编辑用户' : '新增用户'" width="520px">
      <el-form label-width="88px">
        <el-form-item v-if="!editAccount" label="账号">
          <el-input v-model="form.account" />
        </el-form-item>
        <el-form-item :label="editAccount ? '新密码' : '密码'">
          <el-input v-model="form.password" type="password" show-password placeholder="新增必填；编辑留空不改" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="文化程度">
          <el-input v-model="form.education" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<style scoped lang="scss">
.toolbar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  align-items: center;
}
</style>
