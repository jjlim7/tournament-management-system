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
    id: 6969,
    name: "Shawn Thiah",
    image: "https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
    rank: "Challenger",
    currentElo: "75",
    eloUpperlimit: "100",
    totalWins: 123,
    winRatio: 3.14,
    clan: "shawny shawny's clan",
    clanRole: "Admin"
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
    name: "shawnty shawnty g@y club",
    image: "https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
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