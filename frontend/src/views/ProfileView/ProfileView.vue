<template>
  <div class="d-flex row w-75 mx-auto m-5">
    <!-- left side show user and clan details  -->
     <div 
      data-aos="fade-right"
      data-aos-offset="500"
      data-aos-duration="500"
      class="col-lg-4 col-12 mb-5  mb-lg-0">
      <div class="d-flex justify-content-between aliign-items-center">
        <div class="fs-5 fw-semibold">My Profile</div>
        <!-- log out button -->        
        <button 
          class="btn btn-secondary d-flex justify-content-end align-items-center py-0 px-1 m-0"
          @click="logout">
          <font-awesome-icon :icon="['fas', 'right-from-bracket']" />
          <div class="px-2">Log Out</div>
        </button>
      </div>
      <BlurredBGCard 
        class="d-flex flex-lg-column flex-md-row flex-sm-column flex-column justify-content-around mt-1"
        style="height: 95%;">
        <!-- user info -->
        <div class="text-center p-4">
          <img :src="userStore.user.image" alt="" class="rounded-circle border border-primary border-2 " style="width: 120px; height: 120px; object-fit: cover;">
          <div class="fw-semibold">{{ userStore.user.name }}</div>
          <div>UserID: #<span class="fw-semibold">{{ userStore.user.id }}</span></div>
        </div>
        <!-- clan info -->
        <div v-if="userStore.user.clan != null" class="text-center p-4">
          <img :src="clanStore.image" alt="" class="rounded-circle border border-primary border-2" style="width: 120px; height: 120px; object-fit: cover;">
  
          <div class="fw-semibold">{{ userStore.user.clan.clanName }}</div>
          <div>Clan Role: <span class="fw-semibold">{{ userStore.user.clanRole}}</span></div>
        </div>
      </BlurredBGCard>
     </div>

    <!-- right side show statistic and match history and rank progress details  -->
    <div class="col d-flex flex-column justify-content-between mx-3">
      <div
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="500"
      >
        <div class="fs-5 fw-semibold">My Statistic</div>
        <BlurredBGCard class="p-4 d-flex justify-content-between align-items-center">
          <!-- show BR rank -->
          <div class="text-center">
            <img :src="rankImage(userStore.user.rank)" class="img-fluid" alt="Rank Image" style="max-width: 100px">
            <div class="fw-semibold">{{ userStore.user.rank }}</div>
          </div>
          <!-- show total win -->
          <div class="fs-2 fw-bold text-center">
              {{ userStore.user.stats.avgKillDeathRatio.toFixed(2) }}
              <br>
              <span>Average KDA</span>
          </div>
          <div class="fs-2 fw-bold text-center">
              {{ userStore.user.stats.avgAccuracy.toFixed(2) }}
              <br>
              <span>Average Accuracy</span>
          </div>
        </BlurredBGCard>
      </div>
      <div
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="600"
      >
        <div class="fs-5 fw-semibold">Match History</div>
        <BlurredBGCard class="mt-1">
          <table class="table align-middle scrollable-table" v-if="matchHistory.length>0">
            <thead>
              <tr>
                <th class="fw-semibold">Game</th>
                <th class="fw-semibold">Date</th>
                <th class="fw-semibold" v-if="isLargeScreen">KDA</th>
                <th class="fw-semibold">Mode</th>
                <th class="fw-semibold">Placement</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(match, index) in matchHistory" :key="`${match.game}-${match.date}-${index}`">
                <th>{{ match.game }}</th>
                <th>{{ match.date }}</th>
                <th v-if="isLargeScreen" >{{ match.KDA }}</th>
                <th>{{ match.gameMode }}</th>
                <th>{{ match.placement.toFixed(0) }}</th>
              </tr>
            </tbody>
          </table>
          <div v-else class="p-3"> You do not have a match history</div>
        </BlurredBGCard>
      </div>
      <div
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="700"
      >
        <div class="fs-5 fw-semibold">My Tournament Rank</div>
        <BlurredBGCard>
          <RankProgress 
            :rank="userStore.user.rank" 
            :currentElo="userStore.user.currentElo"
            :lowerLimit="userStore.user.eloLowerlimit"
            :upperLimit="userStore.user.eloUpperlimit" 
            gameMode="Battle Royale"
            class="px-2" />
          <RankProgress v-if="userStore.user.clan != null"  
            :rank="clanRank" 
            :lowerLimit="eloLowerLimit"
            :currentElo="currentElo" 
            :upperLimit="eloUpperLimit" 
            gameMode="Clan War"
            class="px-2" />
        </BlurredBGCard>
      </div>
    </div>
  </div>
</template>

<script>
import { useClanStore, useUserStore } from '@/stores/store';
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import { ALLRANKS } from '@/utils/rankImages';
import RankProgress from '@/components/RankProgress/RankProgress.vue';
import axios from '@/utils/axiosInstance';

export default {
  name:"ProfileView",
  components:{BlurredBGCard,RankProgress},
  data(){
    return{
      isLargeScreen: window.innerWidth >= 768,
      rankImages: ALLRANKS,
      matchHistoryHeader:['Game','Date','KDA','Role','Mode','Result'],
      matchHistory:[
        {
          game: "Game #1",
          date: "21/9/2024",
          KDA: "12/3/1",
          gameMode: 'Battle Royale',
          placement: 5
        }
      ],
      currentCWTournament: null,
      currentBRTournament: null,
      eloLowerLimit: 0,
      currentElo: 0,
      eloUpperLimit: 0,
      clanRank: "UNRANKED"
    }
  },
  methods: {
    rankImage(rank){
        return this.rankImages[rank] || this.rankImages.UNRANKED;
    },
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 768;
    },
    logout(){
      console.log("you have logged out")
      this.userStore.logout();
    },
    fetchTournament() {
      axios
        .get('/tournament/api/tournaments')
        .then((response) => {
          if (response.status === 404) {
            return;
          }

          const allTournaments = response.data;
          const currentDateTime = new Date();

          allTournaments.forEach((tournament, index) => {
            const startDate = new Date(tournament.startDate);
            const endDate = new Date(tournament.endDate);

            if (startDate <= currentDateTime && endDate >= currentDateTime) {
              const formattedTournament = { ...tournament, startDate, endDate };
              if (tournament.gameMode === 'BATTLE_ROYALE') {
                this.currentBRTournament = formattedTournament;
              }
              if (tournament.gameMode === 'CLANWAR') {
                this.currentCWTournament = formattedTournament;
              }
            }
          });

          if (this.currentCWTournament) {
            this.fetchEloRank();
          }
        })
        .catch((error) => {
          console.error('Error fetching tournaments:', error);
        });
    },

    fetchEloRank() {
      if (this.currentCWTournament == null) return;

      axios.get(`/elo-ranking/api/elo-ranking/clan/${this.userStore.user.clan.clanId}/tournament/${this.currentCWTournament.tournament_id}`)
        .then((response) => {
          if (response.status === 200) {
            const data = response.data.data;
            this.eloLowerLimit = data.rankThreshold.minRating;
            this.currentElo = data.meanSkillEstimate;
            this.eloUpperLimit = data.rankThreshold.maxRating;
            this.clanRank = data.rankThreshold.rank
          }
        })
        .catch((error) => {
          console.error('Error fetching player\'s elo rank:', error);
        });
    },
    async fetchMatchHistory() {
      this.matchHistory = [];
      let BRhistory = [];
      let CWhistory = [];

      if (this.currentBRTournament) {
        const response = await axios.get(`/elo-ranking/api/game-score/player/${this.userStore.user.id}/tournament/${this.currentBRTournament.tournament_id}`);
        if (response.status === 200) {
          BRhistory = await fetchMatchDetails(response.data.playerGameScores, this.currentBRTournament, 'Battle Royale');
        }
      }

      if (this.currentCWTournament) {
        const response = await axios.get(`/elo-ranking/api/game-score/clan/${this.userStore.user.clan.clanId}/tournament/${this.currentCWTournament.tournament_id}`);
        if (response.status === 200) {
          CWhistory = await fetchMatchDetails(response.data.clanGameScores, this.currentCWTournament, 'Clan War');
        }
      }

      this.matchHistory = BRhistory.concat(CWhistory).sort((a, b) => {
        const dateA = new Date(a.date.split('/').reverse().join('-'));
        const dateB = new Date(b.date.split('/').reverse().join('-'));
        return dateB - dateA; // Sort descending (most recent first)
      });

    },
    async fetchMatchDetails (matches, tournament, mode) {
      const history = [];
      await Promise.all(
        matches.map(async (match) => {
          let buildMatch = {
            game: tournament.name,
            KDA: `${match.kills}/${match.deaths}/${match.assists}`,
            gameMode: mode,
            placement: match.placement.toFixed(0),
          };

          // Fetch and format the match date
          const response = await axios.get(`/matchmaking/api/games/${match.gameId}`);
          if (response.status === 200) {
            const matchDate = new Date(response.data.startTime);
            const day = String(matchDate.getDate()).padStart(2, '0');
            const month = String(matchDate.getMonth() + 1).padStart(2, '0');
            const year = matchDate.getFullYear();
            buildMatch.date = `${day}/${month}/${year}`;
          }
          history.push(buildMatch);
        })
      );
      return history;
    },

  },
  setup(){
    const userStore = useUserStore();
    const clanStore = useClanStore();
    console.log(userStore.user)
    return {userStore,clanStore}
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
      this.fetchTournament();
      this.fetchMatchHistory();
  },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },

}
</script>

<style scoped>
.scrollable-table tbody th {
  color: white;
  border: 0;
  background-color: transparent;
  text-align: center; 
  vertical-align: middle
}

.scrollable-table thead th {
  color: white;
  border: 0;
  background-color: transparent;
  text-align: center; 
  vertical-align: middle
}

.scrollable-table tbody {
  display: block;
  max-height: 100px; /*  change this accordingly */
  overflow-y: auto;
}

.scrollable-table thead, .scrollable-table tbody tr {
  display: table;
  width: 100%;
  table-layout: fixed;
}
</style>
