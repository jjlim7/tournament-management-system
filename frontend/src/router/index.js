import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/store' 

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
import OthersView from '@/views/othersView/OthersView.vue'
import NotAuthorizedView from '@/views/NotAuthorized/NotAuthorizedView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'root',
      redirect: () => {
        const userStore = useUserStore();
    
        // If not authenticated, send to auth page
        if (!userStore.isAuthenticated) {
          return { name: 'auth' };
        }
    
        // Redirect based on role if authenticated
        const userRole = userStore.user?.role;
        if (userRole === 'ROLE_ADMIN') {
          return { name: 'admin' };
        } else if (userRole === 'ROLE_PLAYER') {
          return { name: 'home' };
        } else {
          return { name: 'auth' };
        }
      },
      meta: { requiresAuth: true },
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
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
      component: BattleRoyaleView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/clanwar',
      name: 'clanwar',
      component: ClanWarView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/booking',
      name: 'booking',
      component: BookingView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/Clan',
      name: 'clan',
      component: ClanView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/leaderboard',
      name: 'leaderboard',
      component: LeaderBoardView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true, roles: ['ROLE_PLAYER'] },
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView,
      meta: { requiresAuth: true, roles: ['ROLE_ADMIN'] },
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'other',
      component: OthersView
    },
    {
      path: '/403',
      name: 'NotAuthorized',
      component: NotAuthorizedView
    },
  ]
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  const requiresAuth = to.meta.requiresAuth;
  const roles = to.meta.roles;

  // Initialize user from localStorage if needed
  userStore.initializeUser();

  if (userStore.isAuthenticated) {
    // Prevent authenticated users from accessing the auth page
    if (to.name === 'auth') {
      return next({ path: '/' });
    }

    // Check for role-based access control
    if (requiresAuth && roles && !roles.includes(userStore.user.role)) {
      return next({ name: 'NotAuthorized' });
    }
  } else {
    // Redirect unauthenticated users to the auth page for protected routes
    if (requiresAuth) {
      return next({ path: '/auth' });
    }
  }

  next(); // Proceed to the next route
});


export default router
