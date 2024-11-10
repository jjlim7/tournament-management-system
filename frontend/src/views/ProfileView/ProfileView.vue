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
        <div class="text-center p-4">
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
                <th class="fw-semibold">Result</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="count in 10" :key="count">
                <th>{{ matchHistory[0].tournament }}</th>
                <th>{{ matchHistory[0].date }}</th>
                <th v-if="isLargeScreen" >{{ matchHistory[0].KDA }}</th>
                <th>{{ matchHistory[0].gameMode }}</th>
                <th>{{ matchHistory[0].result }}</th>
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
          <RankProgress 
            :rank="clanStore.rank" 
            :lowerLimit="userStore.user.eloLowerlimit"
            :currentElo="clanStore.currentElo" 
            :upperLimit="clanStore.eloUpperlimit" 
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
          tournament: "Game #1",
          date: "21/9/2024",
          KDA: "12/3/1",
          role: '-',
          gameMode: 'Battle Royale',
          result: 'Victory'
        }
      ]
    }
  },
  methods: {
    rankImage(rank){
        return this.rankImages[rank] || this.rankImages.Unranked;
    },
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 768;
    },
    logout(){
      console.log("you have logged out")
      this.userStore.logout();
      this.$router.push('/auth');
    }
  },
  setup(){
    const userStore = useUserStore();
    const clanStore = useClanStore();
    return {userStore,clanStore}
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
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
