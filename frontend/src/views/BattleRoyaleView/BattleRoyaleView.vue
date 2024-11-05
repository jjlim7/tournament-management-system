<template>
  <div>
    <!-- countdown  at header-->
    <div v-if="upcomingGames.length!=0" class="mx-auto p-4 text-center backgroundColour rounded-bottom-5 align-content-center" style="max-width: 700px; max-height: 80px;">
      <div class="fw-semibold" >Next Match: {{ nextMatch.date }}</div>
      <div class="fw-semibold">Countdown: {{ countdown.days }}d {{ countdown.hours }}h {{ countdown.minutes }}m {{ countdown.seconds }}s</div>
    </div>
    <!-- content -->
    <div class="d-flex justify-content-between p-4 mx-5 row">
      <!-- left side -->
      <div class="d-flex flex-column justify-content-around ml-5 col-md-12 col-lg-6">

        <!-- current tournament -->
         <div>
          <span class="fw-semibold py-1">Current Tournament</span>
          <BlurredBGCard v-if="currentTournament==null"> <div class="text-center">No Active Tournament</div> </BlurredBGCard>
          <BlurredBGCard v-if="currentTournament" :style="{ 
            'background-image': 'url(' + currentTournament.image + ')'}"
            class="imageProperties text-center mb-2 mt-1">
            <div class=" rounded-4 p-2" style="background-color: rgba(0, 0, 0, 0.4);">
              <h5 class="fw-semibold"> {{ currentTournament.name }} </h5>
              <div style="max-height: 70px;" class="overflow-y-hidden text-wrap">{{ currentTournament.description }}</div>
            </div>
          </BlurredBGCard>
         </div>
  
        <!-- rank progress tournament -->
         <div>
          <span class="fw-semibold py-1">Rank Progress</span>
          <BlurredBGCard class="mb-2 mt-1">
            <RankProgress 
              :rank="rank" 
              :currentElo="currentElo" 
              :upperLimit="eloUpperLimit" 
              gameMode="Battle Royale"
              class="p-2" />
          </BlurredBGCard>
         </div>
  
        <!-- other tournament carousel -->
         <div>
          <span class="fw-semibold py-1">Upcoming Battle Royale Tournament</span>
          <BlurredBGCard v-if='upcomingTournaments.length==0'> <div class="text-center">No Upcoming Tournament</div> </BlurredBGCard>
          
          <BlurredBGCard v-if='upcomingTournaments.length!=0' class="mt-1">
            <div id="battleRoyalupcomingTournament" class="carousel slide" data-bs-ride="carousel"
            data-bs-pause="hover">
              <!-- indicator for each slide -->
              <div class="carousel-indicators">
                <button 
                  v-for="(tournament,index) in upcomingTournaments" 
                  :key="index" type="button" 
                  data-bs-target="#battleRoyalupcomingTournament" 
                  :data-bs-slide-to="index" 
                  :class="{active: index==0 }"></button>
              </div>
              <div class="carousel-inner rounded-4">
                <!-- list of carousel items -->
                <div 
                  v-for="(tournament,index) in upcomingTournaments" 
                  :key="index"
                  :class="{'carousel-item': true, 'active': index===0}"
                  style="position: relative; cursor: pointer;"
                  @click="selectUpcomingTournament(tournament)"
                  data-bs-interval="3000">
                  <img :src="tournament.image" class="img-fluid" alt="...">
                  <div class="position-absolute top-0 start-0 w-100 h-100 bg-dark bg-opacity-50 text-center rounded d-flex flex-column justify-content-center">
                    <h5 class="fw-semibold">{{tournament.name}}</h5>
                    <p style="max-height: 70px;" class="overflow-y-hidden text-wrap px-5"> {{ tournament.description }} </p>
                  </div>
                </div>
              </div>
  
              <button class="carousel-control-prev" type="button" data-bs-target="#battleRoyalupcomingTournament" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" ></span>
              </button>
              <button class="carousel-control-next" type="button" data-bs-target="#battleRoyalupcomingTournament" data-bs-slide="next">
                <span class="carousel-control-next-icon" ></span>
              </button>
            </div>
          </BlurredBGCard>
         </div>
      </div>

      <!-- right side -->
      <!-- other tournament details -->
      <div 
        class="bg-light rounded-4 position-relative col-md-12 col-lg-6 p-0 d-flex flex-column"
        v-if="selectedUpcomingTournament!='' && isLargeScreen"> 
        <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4 imgStyle" alt="...">
        <button class="btn btn-close position-absolute top-0 end-0 m-2 btnStyle" @click="selectedUpcomingTournament=''"></button>
        <div class="text-black bg-light p-3 pb-0" >
          <div class="mb-3 d-flex justify-content-between">
            <div class="fw-semibold fs-5"> {{ selectedUpcomingTournament.name }}</div>
            <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{formmattedMode(selectedUpcomingTournament.gameMode)}}</div>
          </div>
          <p class="overflow-y-scroll m-0 shadow-sm" style="max-height: 180px;">{{ selectedUpcomingTournament.description }}</p>
          <div class="text-black fw-semibold">
            <div class="fw-semibold">Start Date: {{ formattedSelectedTournamentStartTime }}</div>
            <div class="fw-semibold">End Date: {{ formattedSelectedTournamentEndTime }}</div>
          </div>
        </div>
        <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
          <button class="fw-semibold mx-auto text-white btn btn-primary w-50"  @click="showModal">Book</button>
        </div>
      </div>
    </div>


    <!-- Modals -->
     <!-- modal to show details when screen is small -->
    <Modal 
      modalID= "upcoming"
      :showFooter="false" 
      header="Upcoming Tournament" 
      >
      <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4" alt="...">
      <div class="text-black bg-light p-3 pb-0" >
        <div class="mb-3 d-flex justify-content-between">
            <div class="fw-semibold fs-5"> {{ selectedUpcomingTournament.name }}</div>
            <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{formmattedMode(selectedUpcomingTournament.gameMode)}}</div>
        </div>
        <p class="overflow-y-scroll m-0" style="max-height: 180px;">{{ selectedUpcomingTournament.description }}</p>
        <div class="text-black">
          <p class="fw-semibold">Start Date: {{ formattedSelectedTournamentStartTime }}</p>
          <p class="fw-semibold">End Date: {{ formattedSelectedTournamentEndTime }}</p>
        </div>
        </div>
        <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
          <button class="fw-semibold mx-auto text-white btn btn-primary w-50" data-bs-target="#booking" data-bs-toggle="modal" >Book</button>
        </div>
    </Modal>

     <!-- modal to show booking for upcoming tournament -->
    <BookingModal 
      :prevModalID="!isLargeScreen ? 'upcoming' : ''" 
      modalID="booking" 
      :isEditing="false"
      :tournament="selectedUpcomingTournament"/>
   

  </div>
</template>

<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import RankProgress from '@/components/RankProgress/RankProgress.vue';
import { useUserStore } from '@/stores/store';
import Modal from '@/components/modal/Modal.vue'; 
import { Modal as bsModal } from 'bootstrap';
import BookingModal from '@/components/modal/BookingModal.vue';
import { tournamentImage } from '@/utils/tournamentImage';
import axios from '@/utils/axiosInstance';

export default {
  name: "BattleRoyaleView",
  components: { BlurredBGCard, RankProgress, Modal, bsModal, BookingModal },
  data() {
    return {
      isLargeScreen: window.innerWidth >= 992,
      countdown: {
        days: 0,
        hours: 0,
        minutes: 0,
        seconds: 0
      },
      intervalId: null,
      nextMatch: {
        name: "Game #1",
        date: "20 Sept 2024 20:00:00",
      },
      currentTournament:null,
      upcomingTournaments:[],
      selectedUpcomingTournament: '',
      currentElo: 0,
      eloUpperLimit: 0,
      rank: "Unranked",
      upcomingGames: []
    };
  },
  computed:{
    formattedSelectedTournamentStartTime() {
      if (!this.selectedUpcomingTournament.startDate) return '';
      return this.formatDate(new Date(this.selectedUpcomingTournament.startDate));
    },
    formattedSelectedTournamentEndTime() {
      if (!this.selectedUpcomingTournament.endDate) return '';
      return this.formatDate(new Date(this.selectedUpcomingTournament.endDate));
    }
  },
  methods: {
    formatDate(date) {
      if (!date) return ''; // Handle null or undefined dates
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Month is zero-based
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    },
    formmattedMode(mode){
      if(mode == "BATTLE_ROYALE") return "Battle Royale";
      if(mode == "CLANWAR") return "Clan War";
    },
    startCountdown() {
      const targetTime = new Date(this.nextMatch.date).getTime();

      this.intervalId = setInterval(() => {
        const now = new Date().getTime();
        const timeLeft = targetTime - now;

        if (timeLeft < 0) {
          clearInterval(this.intervalId);
          this.countdown = { days: 0, hours: 0, minutes: 0, seconds: 0 };
        } else {
          this.countdown.days = Math.floor(timeLeft / (1000 * 60 * 60 * 24));
          this.countdown.hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
          this.countdown.minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
          this.countdown.seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
        }
      }, 1000);
    },
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 992;
    },
    selectUpcomingTournament(tournament){
      this.selectedUpcomingTournament=tournament;

      this.$nextTick(() => {
        if (!this.isLargeScreen) {
          const modalID = 'upcoming';
          const tournamentModal = new bsModal(document.getElementById(modalID));
          tournamentModal.show();
        }
      });
    },
    showModal(){
      const modalID = 'booking';
      const tournamentModal = new bsModal(document.getElementById(modalID));
      tournamentModal.show();
    },
    async fetchTournament() {
      try {
        const response = await axios.get('/tournament/api/tournaments');
        // console.log(response);

        this.upcomingTournaments = [];
        
        if (response.status === 404) {
          return;
        }
        
        const allTournaments = response.data;
        const currentDateTime = new Date();
        
        const n = tournamentImage.length;

        for (let i = 0 ; i< allTournaments.length ; i++) {
          const tournament = allTournaments[i];
          if(tournament.gameMode != "BATTLE_ROYALE"){
            continue;
          }
          
          const startDate = new Date(tournament.startDate);
          const endDate = new Date(tournament.endDate);
          
          let formattedTournament = { ...tournament, "startDate":startDate, "endDate": endDate, "image":tournamentImage[i%n]}

          if (startDate <= currentDateTime && endDate >= currentDateTime) {
            // Tournament is currently active
            this.currentTournament = formattedTournament;
          } else if (startDate > currentDateTime) {
            // Tournament is upcoming
            this.upcomingTournaments.push(formattedTournament);
          }
        }
        //UNCOMMENT THIS AFTER I GET THE API. THIS IS FOR CURRENT TOURNAMENT
        this.fetchEloRank();
        if(this.currentTournament!=null){
          this.fetchEloRank();
        }
      } catch (error) {
        console.error('Error fetching tournaments:', error);
        throw error;
      }
    },
    async fetchEloRank(){
      try {
        const response = await axios.get(`/elo-ranking/api/elo-ranking/player/${this.userStore.user.id}/tournament/1`);
        const data  = response.data.data;
        // this.rank = data.rankThreshold.


        //uncomment this when there are correct data
        //const response = await axios.get(`/api/elo-ranking/player/${this.userStore.user.id}/tournament/${this.currentTournament.tournament_id}`);
        console.log(data);
        
      } catch (error) {
        console.error('Error fetching player\'s elo rank :', error);
        throw error;
      }
    }

  },
  setup() {
    const userStore = useUserStore();
    return {userStore}
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
  },
  beforeUnmount() {
    clearInterval(this.intervalId); // Clear the interval when the component is destroyed
  },
  mounted() {
    this.fetchTournament();
    // count down if there is a upcoming game
    this.startCountdown();
  },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
};
</script>

<style scoped>
.backgroundColour{
  background: linear-gradient(to top, 
              rgba(248, 147, 38, 1) 70%, 
              rgba(248, 147, 38, 0.7),
              rgba(248, 147, 38, 0.2));
}

.imageProperties{
  background-size: cover;
}
.btnStyle{
  background-color: white; 
  opacity: 80%;
}

.carousel-item img {
  width:100%;
  height: 300px;
  object-fit:cover;
}

.imgStyle{
  widows: 100%;
  max-height: 350px;
  object-fit: content;
}
</style>