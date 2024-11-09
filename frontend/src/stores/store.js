import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from '@/utils/axiosInstance';

// role is either "ROLE_ADMIN" or "ROLE_PLAYER"
export const useUserStore = defineStore('user', {
  state: () => ({
    user:{
      id: 104,
      name: "Shawn Thiah",
      image: "https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
      rank: "Challenger",
      eloLowerlimit: 0,
      currentElo: "75",
      eloUpperlimit: "100",
      totalWins: 123,
      winRatio: 3.14,
      clan: {
        clanId: null,
        clanName: "",
      },
      clanRole: "ROLE_PLAYER",
      role: "ROLE_PLAYER",
      stats: {
        playerId: null,
        tournamentId: null,
        totalMatches: 0,
        gameMode: null,
        totalKills: 0,
        totalDeaths: 0,
        avgKillDeathRatio: 0.0,
        avgAccuracy: 0.0,
        avgHeadshotAccuracy: 0.0,
        avgHealingDonePerSecond: 0.0,
        avgDamageDonePerSecond: 0.0,
        avgDamageTanked: 0.0,
        totalAssists: 0,
        totalRevives: 0,
        totalShotsFired: 0,
        totalShotsHit: 0
      },
      eloRank: {
        id: null,
        rankThresholdId: null,
        meanSkillEstimate: 0.0,
        uncertainty: 0.0,
        tournamentId: null,
        playerId: null,
        rankThreshold: {
          minRating: 0.0,
          maxRating: 0.0,
          rank: null
        }
      },
    },
    isAuthenticated: false,
  }),
  actions: {
    setUser(userData) {
      this.user = {...userData, image: "https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",};
      this.isAuthenticated = true;
      // Optionally, save to localStorage for persistence
      localStorage.setItem('user', JSON.stringify(this.user));
      localStorage.setItem('isAuthenticated', 'true');
    },
    async logout() {
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
      const response = await axios.post(`/auth/api/logout`)
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
    isAdmin: (state) => state.user.role === 'ROLE_ADMIN',
    isClanAdmin: (state) => state.user.clanRole === 'ROLE_ADMIN',
    hasClan: (state) => state.user.clan !== '',
  }
});


export const useClanStore = defineStore('clan', {
  state: () => ({
    id: 1,
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
