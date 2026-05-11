import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { public: true, title: '管理员登录' },
    },
    {
      path: '/',
      component: () => import('@/layouts/AdminLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('@/views/DashboardView.vue'),
          meta: { title: '工作台' },
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/users/UserManageView.vue'),
          meta: { title: '用户管理' },
        },
        {
          path: 'courses',
          name: 'courses',
          component: () => import('@/views/courses/CourseManageView.vue'),
          meta: { title: '课程管理' },
        },
        {
          path: 'materials',
          name: 'materials',
          component: () => import('@/views/materials/MaterialManageView.vue'),
          meta: { title: '资料管理' },
        },
        {
          path: 'knowledge',
          name: 'knowledge',
          component: () => import('@/views/knowledge/KnowledgeManageView.vue'),
          meta: { title: '知识点管理' },
        },
        {
          path: 'questions',
          name: 'questions',
          component: () => import('@/views/questions/QuestionBankView.vue'),
          meta: { title: '题库管理' },
        },
        {
          path: 'exam-papers',
          name: 'exam-papers',
          component: () => import('@/views/exam-papers/ExamPaperManageView.vue'),
          meta: { title: '试卷管理' },
        },
        {
          path: 'exams',
          name: 'exams',
          component: () => import('@/views/exams/ExamScoreManageView.vue'),
          meta: { title: '考试与成绩' },
        },
        {
          path: 'banners',
          name: 'banners',
          component: () => import('@/views/banners/BannerManageView.vue'),
          meta: { title: '轮播图' },
        },
        {
          path: 'forum',
          name: 'forum',
          component: () => import('@/views/forum/ForumManageView.vue'),
          meta: { title: '论坛管理' },
        },
      ],
    },
    { path: '/:pathMatch(.*)*', redirect: '/dashboard' },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.public) {
    if (to.name === 'login' && auth.token) return { name: 'dashboard' }
    return true
  }
  if (!auth.token) return { name: 'login', query: { redirect: to.fullPath } }
  return true
})

export default router
