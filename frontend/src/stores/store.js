import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }

  return { count, doubleCount, increment }
})


export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    name: "Shawn Thiah",
    rank: "Challenger",
    currentElo: "75",
    eloUpperlimit: "100",
    clan: "shawny shawny's clan",
    clanRole: "member"
  }),
  actions: {
    setUser(userData) {
      this.user = userData;  // Set user data
    },
    clearUser() {
      this.user = null; // Clear user data on logout
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.user,  // Check if user is logged in
  }
});


export const useClanStore = defineStore('clan', {
  state: () => ({
    name: "shawnty shawnty club",
    rank: "Diamond",
    currentElo: "23",
    eloUpperlimit: "100",
  }),
  actions: {
    setClan(clanData) {
      this.clan = clanData;  // Set clan data
    },
    clearclan() {
      this.clan = null; // Clear clan data on logout
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.clan,  // Check if user is logged in
  }
});