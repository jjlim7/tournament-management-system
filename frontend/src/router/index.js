import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView/HomeView.vue'
import AboutView from '@/views/AboutView/AboutView.vue'
import TournamentsView from '@/views/TournamentsView/TournamentsView.vue'
import ProfileView from '@/views/ProfileView/ProfileView.vue'
import LeaderBoardView from '@/views/LeaderBoardView/LeaderBoardView.vue'
import GamebookingView from '@/views/TournamentsView/GameBookingsView/GameBookingsView.vue'
import GameView from '@/views/TournamentsView/GameBookingsView/GameView/GameView.vue'

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
      path: '/tournaments',
      name: 'tournaments',
      component: TournamentsView
    },
    {
      path: '/tournaments/gamebooking',
      name: 'gamebooking',
      component: GamebookingView
    },
    {
      path: '/tournaments/gamebooking/game',
      name: 'game',
      component: GameView
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
  ]
})

export default router
