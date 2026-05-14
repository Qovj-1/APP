<template>
  <view class="page">
    <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view>
      <text class="title">{{ training?.name || '实训学习' }}</text>
      <view class="placeholder"></view>
    </view>
    
    <view class="progress-bar">
      <view class="progress-track">
        <view class="progress-fill" :style="{ width: progressPercent + '%' }"></view>
      </view>
      <text class="progress-text">学习进度 {{ progressPercent }}%</text>
    </view>
    
    <view class="content" v-if="training">
      <view class="tab-bar">
        <view 
          v-for="(tab, idx) in tabs" 
          :key="tab.key"
          :class="['tab-item', { active: activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >
          <text class="tab-text">{{ tab.label }}</text>
          <view class="tab-indicator" v-if="activeTab === tab.key"></view>
        </view>
      </view>
      
      <view class="tab-content" v-show="activeTab === 'video'">
        <view class="video-card">
          <view class="video-placeholder">
            <text class="play-icon">▶</text>
            <text class="video-title">{{ training.name }}</text>
          </view>
          <view class="video-info">
            <text class="duration">{{ training.time }}课时</text>
          </view>
        </view>
        <view class="video-list">
          <view class="video-item" v-for="(item, idx) in videoItems" :key="idx">
            <view class="video-thumb">
              <text class="video-num">{{ idx + 1 }}</text>
            </view>
            <view class="video-detail">
              <text class="video-name">{{ item.title }}</text>
              <text class="video-time">{{ item.time }}</text>
            </view>
            <view class="video-status" v-if="item.completed">
              <text class="status-icon">✓</text>
            </view>
          </view>
        </view>
      </view>
      
      <view class="tab-content" v-show="activeTab === 'theory'">
        <view class="theory-content">
          <view class="chapter" v-for="(chapter, idx) in chapters" :key="idx">
            <view class="chapter-header" @click="toggleChapter(idx)">
              <view class="chapter-left">
                <text class="chapter-icon">{{ chapter.open ? '−' : '+' }}</text>
                <text class="chapter-title">第{{ idx + 1 }}章 {{ chapter.title }}</text>
              </view>
              <text class="chapter-status" v-if="chapter.completed">已完成</text>
            </view>
            <view class="chapter-body" v-if="chapter.open">
              <text class="chapter-text">{{ chapter.content }}</text>
              <view class="chapter-action" v-if="!chapter.completed">
                <button class="complete-btn" @click="completeChapter(idx)">标记为已学习</button>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <view class="tab-content" v-show="activeTab === 'practice'">
        <view class="practice-card">
          <view class="practice-header">
            <text class="practice-title">实操练习</text>
            <text class="practice-count">共{{ practiceQuestions.length }}题</text>
          </view>
          <view class="question-list">
            <view class="question-item" v-for="(q, idx) in practiceQuestions" :key="idx">
              <view class="question-header">
                <text class="question-num">{{ idx + 1 }}</text>
                <text class="question-type">{{ q.type }}</text>
              </view>
              <text class="question-text">{{ q.question }}</text>
              <view class="options-list">
                <view 
                  v-for="(opt, optIdx) in q.options" 
                  :key="optIdx"
                  :class="['option-item', { selected: q.selected === optIdx, correct: q.submitted && optIdx === q.answer, wrong: q.submitted && q.selected === optIdx && optIdx !== q.answer }]"
                  @click="selectOption(idx, optIdx)"
                >
                  <text class="option-label">{{ opt.label }}</text>
                  <text class="option-text">{{ opt.text }}</text>
                </view>
              </view>
              <view class="question-footer" v-if="q.submitted">
                <text class="answer-analysis">{{ q.analysis }}</text>
              </view>
            </view>
          </view>
          <view class="practice-action">
            <button class="submit-btn" @click="submitPractice">提交练习</button>
          </view>
        </view>
      </view>
      
      <view class="tab-content" v-show="activeTab === 'exam'">
        <view class="exam-card">
          <view class="exam-header">
            <text class="exam-title">实训考核</text>
            <view class="exam-info">
              <text class="exam-duration">时长：30分钟</text>
              <text class="exam-score">满分：100分</text>
            </view>
          </view>
          <view class="exam-intro">
            <text class="intro-text">完成以下考核题目，达到60分以上即可通过实训考核。</text>
          </view>
          <view class="exam-action">
            <button class="start-exam-btn" @click="startExam">开始考核</button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { request, getToken } from '@/utils/api.js'

export default {
  data() {
    return {
      training: null,
      activeTab: 'video',
      tabs: [
        { key: 'video', label: '教学视频' },
        { key: 'theory', label: '理论知识' },
        { key: 'practice', label: '实操练习' },
        { key: 'exam', label: '实训考核' },
      ],
      videoItems: [
        { title: '实训介绍与课程大纲', time: '05:30', completed: false },
        { title: '基础知识讲解', time: '15:20', completed: false },
        { title: '实操演示', time: '20:45', completed: false },
        { title: '案例分析', time: '18:10', completed: false },
      ],
      chapters: [
        { title: '实训概述', content: '本实训项目旨在帮助学员掌握膳食调查与分析的基本方法和技能。通过理论学习和实操练习，学员将学会运用24小时回顾法、食物频率法等调查方法，并能进行膳食营养素的计算与分析。', open: true, completed: false },
        { title: '调查方法', content: '24小时回顾法是通过询问被调查者过去24小时内摄入的所有食物和饮料，来了解其膳食摄入情况的一种方法。食物频率法则是通过调查被调查者在一定时期内各种食物的摄入频率，来评估其膳食模式。', open: false, completed: false },
        { title: '数据分析', content: '膳食营养素计算需要使用专业的营养分析软件或数据库，将食物摄入量转换为各种营养素的摄入量，并与推荐摄入量进行比较分析。', open: false, completed: false },
      ],
      practiceQuestions: [
        {
          type: '单选题',
          question: '以下哪种方法属于膳食调查方法？',
          options: [
            { label: 'A', text: '问卷调查法' },
            { label: 'B', text: '24小时回顾法' },
            { label: 'C', text: '实验法' },
            { label: 'D', text: '观察法' },
          ],
          answer: 1,
          selected: null,
          submitted: false,
          analysis: '24小时回顾法是常用的膳食调查方法之一，通过询问被调查者过去24小时内的食物摄入情况来了解其膳食状况。',
        },
        {
          type: '单选题',
          question: '膳食营养素计算的主要目的是什么？',
          options: [
            { label: 'A', text: '了解食物价格' },
            { label: 'B', text: '评估营养摄入是否合理' },
            { label: 'C', text: '确定食物来源' },
            { label: 'D', text: '计算食物重量' },
          ],
          answer: 1,
          selected: null,
          submitted: false,
          analysis: '膳食营养素计算的主要目的是评估个体或群体的营养摄入是否符合推荐摄入量，以便发现营养问题并提供改进建议。',
        },
      ],
      progressPercent: 0,
    }
  },
  onLoad(options) {
    this.loadTraining(options.id)
  },
  methods: {
    async loadTraining(id) {
      try {
        this.training = (await request({ url: `/app/trainings/${id}` })) || null
      } catch (e) {
        uni.showToast({ title: '加载失败', icon: 'none' })
      }
    },
    goBack() {
      uni.navigateBack()
    },
    toggleChapter(idx) {
      this.chapters[idx].open = !this.chapters[idx].open
    },
    completeChapter(idx) {
      this.chapters[idx].completed = true
      this.updateProgress()
      uni.showToast({ title: '已标记为已学习', icon: 'success' })
    },
    selectOption(qIdx, optIdx) {
      if (this.practiceQuestions[qIdx].submitted) return
      this.practiceQuestions[qIdx].selected = optIdx
    },
    submitPractice() {
      this.practiceQuestions.forEach(q => {
        q.submitted = true
      })
      this.updateProgress()
      uni.showToast({ title: '练习已提交', icon: 'success' })
    },
    updateProgress() {
      let completed = 0
      let total = 0
      
      this.videoItems.forEach(v => { if (v.completed) completed++; total++ })
      this.chapters.forEach(c => { if (c.completed) completed++; total++ })
      this.practiceQuestions.forEach(q => { if (q.submitted) completed++; total++ })
      
      this.progressPercent = Math.round((completed / total) * 100)
    },
    startExam() {
      uni.showToast({ title: '考核功能开发中', icon: 'none' })
    },
  },
}
</script>

<style scoped>
.page {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}
.back-btn {
  width: 64rpx;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}
.back-icon {
  font-size: 48rpx;
  color: #333;
}
.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}
.placeholder {
  width: 64rpx;
}
.progress-bar {
  padding: 16rpx 24rpx;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}
.progress-track {
  height: 12rpx;
  background: #eee;
  border-radius: 6rpx;
  overflow: hidden;
  margin-bottom: 8rpx;
}
.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #1989fa 0%, #40c4ff 100%);
  border-radius: 6rpx;
  transition: width 0.3s ease;
}
.progress-text {
  font-size: 22rpx;
  color: #666;
}
.content {
  padding-bottom: 32rpx;
}
.tab-bar {
  display: flex;
  background: #fff;
  border-bottom: 1rpx solid #eee;
}
.tab-item {
  flex: 1;
  padding: 24rpx 0;
  text-align: center;
  position: relative;
}
.tab-text {
  font-size: 28rpx;
  color: #666;
}
.tab-item.active .tab-text {
  color: #1989fa;
  font-weight: 600;
}
.tab-indicator {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 48rpx;
  height: 4rpx;
  background: #1989fa;
  border-radius: 2rpx;
}
.tab-content {
  padding: 24rpx;
}
.video-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
  margin-bottom: 24rpx;
}
.video-placeholder {
  height: 320rpx;
  background: linear-gradient(135deg, #1989fa 0%, #40c4ff 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.play-icon {
  width: 100rpx;
  height: 100rpx;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48rpx;
  color: #1989fa;
  margin-bottom: 16rpx;
}
.video-title {
  font-size: 30rpx;
  color: #fff;
  font-weight: 600;
}
.video-info {
  padding: 16rpx 24rpx;
  display: flex;
  justify-content: space-between;
}
.duration {
  font-size: 24rpx;
  color: #666;
}
.video-list {
  background: #fff;
  border-radius: 16rpx;
}
.video-item {
  display: flex;
  align-items: center;
  padding: 20rpx 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.video-item:last-child {
  border-bottom: none;
}
.video-thumb {
  width: 80rpx;
  height: 60rpx;
  background: #f5f5f5;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16rpx;
}
.video-num {
  font-size: 24rpx;
  color: #999;
}
.video-detail {
  flex: 1;
}
.video-name {
  font-size: 26rpx;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}
.video-time {
  font-size: 22rpx;
  color: #999;
}
.video-status {
  width: 40rpx;
  height: 40rpx;
  background: #f6ffed;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.status-icon {
  font-size: 24rpx;
  color: #52c41a;
}
.theory-content {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}
.chapter {
  border-bottom: 1rpx solid #f0f0f0;
}
.chapter:last-child {
  border-bottom: none;
}
.chapter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
}
.chapter-left {
  display: flex;
  align-items: center;
}
.chapter-icon {
  width: 40rpx;
  height: 40rpx;
  background: #e6f4ff;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #1989fa;
  margin-right: 12rpx;
}
.chapter-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}
.chapter-status {
  font-size: 22rpx;
  color: #52c41a;
  padding: 4rpx 12rpx;
  background: #f6ffed;
  border-radius: 8rpx;
}
.chapter-body {
  padding: 0 24rpx 24rpx;
}
.chapter-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.8;
  margin-bottom: 16rpx;
}
.chapter-action {
  text-align: right;
}
.complete-btn {
  font-size: 26rpx;
  color: #1989fa;
  background: #e6f4ff;
  padding: 12rpx 24rpx;
  border-radius: 24rpx;
  border: none;
}
.practice-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}
.practice-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.practice-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}
.practice-count {
  font-size: 24rpx;
  color: #999;
}
.question-list {
  padding: 24rpx;
}
.question-item {
  margin-bottom: 24rpx;
}
.question-item:last-child {
  margin-bottom: 0;
}
.question-header {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}
.question-num {
  width: 40rpx;
  height: 40rpx;
  background: #1989fa;
  color: #fff;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 600;
  margin-right: 12rpx;
}
.question-type {
  font-size: 22rpx;
  color: #999;
}
.question-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.6;
  margin-bottom: 16rpx;
}
.options-list {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}
.option-item {
  display: flex;
  align-items: center;
  padding: 16rpx 20rpx;
  background: #fafafa;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
}
.option-item.selected {
  border-color: #1989fa;
  background: #e6f4ff;
}
.option-item.correct {
  border-color: #52c41a;
  background: #f6ffed;
}
.option-item.wrong {
  border-color: #ff4d4f;
  background: #fff2f0;
}
.option-label {
  font-size: 26rpx;
  font-weight: 600;
  color: #333;
  margin-right: 12rpx;
}
.option-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
}
.question-footer {
  margin-top: 16rpx;
  padding: 16rpx;
  background: #fffbe6;
  border-radius: 8rpx;
}
.answer-analysis {
  font-size: 24rpx;
  color: #d48806;
  line-height: 1.6;
}
.practice-action {
  padding: 24rpx;
  border-top: 1rpx solid #f0f0f0;
}
.submit-btn {
  width: 100%;
  height: 80rpx;
  background: #1989fa;
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  font-weight: 600;
  border: none;
}
.exam-card {
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}
.exam-header {
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}
.exam-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 12rpx;
}
.exam-info {
  display: flex;
  gap: 24rpx;
}
.exam-duration, .exam-score {
  font-size: 24rpx;
  color: #666;
}
.exam-intro {
  padding: 24rpx;
}
.intro-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}
.exam-action {
  padding: 24rpx;
  border-top: 1rpx solid #f0f0f0;
}
.start-exam-btn {
  width: 100%;
  height: 80rpx;
  background: linear-gradient(135deg, #1989fa 0%, #40c4ff 100%);
  color: #fff;
  border-radius: 40rpx;
  font-size: 28rpx;
  font-weight: 600;
  border: none;
}
</style>