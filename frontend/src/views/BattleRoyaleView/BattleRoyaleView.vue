<template>
  <div class="battleroyalePage">
    <!-- countdown  at header-->
    <div class="mx-auto p-4 text-center backgroundColour rounded-bottom-5 align-content-center" style="max-width: 700px; max-height: 80px;">
      <div class="fw-semibold" >Next Match: {{ nextMatch.date }}</div>
      <div class="fw-semibold">Countdown: {{ countdown.days }}d {{ countdown.hours }}h {{ countdown.minutes }}m {{ countdown.seconds }}s</div>
    </div>
    <!-- content -->
    <div class="d-flex justify-content-between p-4 mx-5 row">
      <!-- left side -->
      <div class="d-flex flex-column justify-content-around ml-5 col-md-12 col-lg-6">

        <!-- current tournament -->
        <BlurredBGCard :style="{ 
          'background-image': 'url(' + currentTournament.image + ')'}"
          class="imageProperties text-center mb-3">
          <div class="p-2 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">
            <h5 class="fw-semibold"> {{ currentTournament.name }} </h5>
            <div style="max-height: 70px;" class="textStyle text-wrap">{{ currentTournament.description }}</div>
          </div>
        </BlurredBGCard>
  
        <!-- rank progress tournament -->
        <BlurredBGCard class="mb-3">
          <RankProgress 
            :rank="userStore.rank" 
            :currentElo="userStore.currentElo" 
            :upperLimit="userStore.eloUpperlimit" 
            gameMode="Battle Royale"
            class="p-2" />
        </BlurredBGCard>
  
        <!-- other tournament carousel -->
        <BlurredBGCard>
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
                style="position: relative;"
                @click="selectUpcomingTournament(tournament)"
                data-bs-interval="3000">
                <img :src="tournament.image" class="img-fluid" alt="...">
                <div class="position-absolute top-0 start-0 w-100 h-100 bg-dark bg-opacity-50 text-center rounded d-flex flex-column justify-content-center">
                  <h5 class="fw-semibold">{{tournament.name}}</h5>
                  <p style="max-height: 70px;" class="textStyle text-wrap px-5"> {{ tournament.description }} </p>
                </div>
              </div>
            </div>

            <button class="carousel-control-prev" type="button" data-bs-target="#battleRoyalupcomingTournament" data-bs-slide="prev">
              <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#battleRoyalupcomingTournament" data-bs-slide="next">
              <span class="carousel-control-next-icon" aria-hidden="true"></span>
            </button>
          </div>
        </BlurredBGCard>
      </div>

      <!-- right side -->
      <!-- other tournament details -->

      <div 
        class="bg-light rounded-4 position-relative col-md-12 col-lg-6 p-0 d-flex flex-column"
        v-if="selectedUpcomingTournament!='' && isLargeScreen"> 
        <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4" alt="...">
        <button class="btn btn-close position-absolute top-0 end-0 m-2 btnStyle" @click="selectedUpcomingTournament=''"></button>
        <div class="text-black bg-light p-3 pb-0" >
          <h5 class="fw-semibold">{{selectedUpcomingTournament.name}}</h5>
          <p class="overflow-y-scroll m-0" style="max-height: 150px;">{{ selectedUpcomingTournament.description }}</p>
        </div>
        <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
          <button class="fw-semibold mx-auto text-white btn btn-primary w-50">Book</button>
        </div>
      </div>
    </div>

    <Modal 
      :modalID="selectedUpcomingTournament!=='' ? selectedUpcomingTournament.id + 'upcoming' : 'defaultModal'" 
      :showFooter="false" 
      header="" 
      >
      <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4" alt="...">
      <div class="text-black bg-light p-3 pb-0" >
        <h5 class="fw-semibold">{{selectedUpcomingTournament.name}}</h5>
        <p class="overflow-y-scroll m-0" style="max-height: 180px;">{{ selectedUpcomingTournament.description }}</p>
        </div>
        <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
          <button class="fw-semibold mx-auto text-white btn btn-primary w-50">Book</button>
        </div>
    </Modal>
   

  </div>
</template>

<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import RankProgress from '@/components/RankProgress/RankProgress.vue';
import { useUserStore } from '@/stores/store';
import Modal from '@/components/modal/Modal.vue';
import { Modal as bsModal } from 'bootstrap';

export default {
  name: "BattleRoyaleView",
  components: { BlurredBGCard, RankProgress, Modal, bsModal },
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
      currentTournament:{
        name: "Tournament #345",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
      },
      upcomingTournaments:[
        {
        id: "123",
        name: "Tournament #1",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
        },
        {
        id: "124",
        name: "Tournament #2",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
        },
        {
        id: "125",
        name: "Tournament #3",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
        },
        {
        id: "126",
        name: "Tournament #4",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
        },
        {
        id: "127",
        name: "Tournament #5",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg"
        }
      ],
      selectedUpcomingTournament: ''
    };
  },
  methods: {
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
        console.log(this.isLargeScreen)
        if(!this.isLargeScreen) this.selectedUpcomingTournament='';
    },
    selectUpcomingTournament(tournament){
      this.selectedUpcomingTournament=tournament;

      this.$nextTick(() => {
        if (!this.isLargeScreen) {
          const modalID = this.selectedUpcomingTournament.id + 'upcoming';
          const tournamentModal = new bsModal(document.getElementById(modalID));
          tournamentModal.show();
        }
      });

    }
  },
  computed:{
    
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
.textStyle{
  overflow: scroll ;
}
</style>