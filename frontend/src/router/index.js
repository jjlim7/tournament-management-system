import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView/HomeView.vue'
import AboutView from '@/views/AboutView/AboutView.vue'
import ProfileView from '@/views/ProfileView/ProfileView.vue'
import LeaderBoardView from '@/views/LeaderBoardView/LeaderBoardView.vue'
import BattleRoyaleView from '@/views/BattleRoyaleView/BattleRoyaleView.vue'
import ClanWarView from '@/views/ClanWarView/ClanWarView.vue'
import BookingView from '@/views/BookingView/BookingView.vue'
import ClanView from '@/views/ClanView/ClanView.vue'
import AuthView from '@/views/AuthView/AuthView.vue'
import AdminView from '@/views/AdminView/AdminView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView
    },
    {
      path: '/auth',
      name: 'auth',
      component: AuthView
    },
    {
      path: '/battleroyale',
      name: 'battleroyale',
      component: BattleRoyaleView
    },
    {
      path: '/clanwar',
      name: 'clanwar',
      component: ClanWarView
    },
    {
      path: '/booking',
      name: 'booking',
      component: BookingView
    },
    {
      path: '/Clan',
      name: 'clan',
      component: ClanView
    },
    {
      path: '/leaderboard',
      name: 'leaderboard',
      component: LeaderBoardView
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView
    },
  ]
})

export default router
