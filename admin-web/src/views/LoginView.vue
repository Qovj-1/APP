<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { adminFetch } from '@/api/client'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()

const loading = ref(false)
const form = reactive({
  account: '',
  password: '',
})

async function onSubmit() {
  if (!form.account || !form.password) {
    ElMessage.warning('请输入账号和密码')
    return
  }
  loading.value = true
  try {
    const data = await adminFetch<{ token: string; account: string; name: string }>('/auth/login', {
      method: 'POST',
      json: { account: form.account, password: form.password },
    })
    auth.setSession(data.token, data.account)
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/dashboard'
    await router.replace(redirect || '/dashboard')
    ElMessage.success('登录成功')
  } catch (e) {
    ElMessage.error(e instanceof Error ? e.message : '登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <el-card class="card" shadow="hover">
      <template #header>
        <div class="head">营养师系统 · 管理员登录</div>
      </template>
      <el-form label-width="72px" @submit.prevent="onSubmit">
        <el-form-item label="账号">
          <el-input v-model="form.account" autocomplete="username" placeholder="管理员账号 admin" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            autocomplete="current-password"
            placeholder="默认 admin123456"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" native-type="submit" style="width: 100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.login-page {
  min-height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, #ecf5ff 0%, #f5f7fa 45%, #ffffff 100%);
}
.card {
  width: 400px;
}
.head {
  font-weight: 600;
  font-size: 16px;
  text-align: center;
}
</style>
