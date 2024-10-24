import { ref, computed } from 'vue'
import { defineStore } from 'pinia'


export const useUserStore = defineStore('user', {
  state: () => ({
    user:{
      id: 6969,
      name: "Shawn Thiah",
      image: "https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
      rank: "Challenger",
      currentElo: "75",
      eloUpperlimit: "100",
      totalWins: 123,
      winRatio: 3.14,
      clan: "Code with shawn club",
      clanRole: "Admin",
      role: "Member"
    },
    isAuthenticated: false,
  }),
  actions: {
    setUser(userData) {
      this.user = userData;
      this.isAuthenticated = true;
      // Optionally, save to localStorage for persistence
      localStorage.setItem('user', JSON.stringify(this.user));
      localStorage.setItem('isAuthenticated', 'true');
    },
    logout() {
      this.user = { 
        id: null,
        name: "",
        image: "",
        rank: "",
        currentElo: "",
        eloUpperlimit: "",
        totalWins: null,
        winRatio: null,
        clan: "",
        clanRole: "",
        role: ""
      };
      this.isAuthenticated = false;
      localStorage.removeItem('user');
      localStorage.removeItem('isAuthenticated');
    },
    initializeUser() {
      const user = localStorage.getItem('user');
      const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true';
      if (user && isAuthenticated) {
        this.user = JSON.parse(user);
        this.isAuthenticated = true;
      }
    },
    setIsAuth(){
      this.isAuthenticated = true;
    }
  },
  getters: {
    isLoggedIn: (state) => !!state.user,
    isAdmin: (state) => state.user.role === 'Admin',
    isClanAdmin: (state) => state.user.clanRole === 'Admin',
    hasClan: (state) => state.user.clan !== '',
  }
});


export const useClanStore = defineStore('clan', {
  state: () => ({
    name: "Code with shawn club",
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
