<template>
    <div class="w-75 mx-auto">
      <!-- my schedule games -->
      <div 
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="500"
        class="w-75 mx-auto mb-2" >
        <div class="d-flex justify-content-between align-items-center">
          <div class="fw-semibold px-2 py-1 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">My Schedule Games</div>
          <div class="fw-semibold" data-bs-target="#scheduledGames" data-bs-toggle="modal" style="cursor: pointer;">Show all -></div>
        </div>
        <BlurredBGCard class="mt-1">
          <table class="table scrollable-table" v-if="games.length>0">
            <thead>
              <tr>
                <th class="fw-semibold">Name</th>
                <th class="fw-semibold">Date</th>
                <th class="fw-semibold" v-if="isLargeScreen">Time</th>
                <th class="fw-semibold">Game Mode</th>
                <!-- <th class="fw-semibold text-center">Action</th> -->
              </tr>
            </thead>
            <tbody>
              <tr v-for="game in games" :key="game.id">
                <th>{{ game.name }}</th>
                <th>{{ formatDate(game.startTime) }}</th>
                <th v-if="isLargeScreen">{{ extractTime(game.startTime, game.endTime) }}</th>
                <th>{{ game.gameMode }}</th>
                <!-- <th class="d-flex justify-content-center p-1"><button class="btn btn-primary fw-semibold" @click="deleteGame(game)">Delete</button></th> -->
              </tr>
            </tbody>
          </table>
          <div v-else class="p-3"> You do not have any upcoming game :( Book a game below!</div>
        </BlurredBGCard>
      </div>

      <!-- my availabilities -->
      <div 
        data-aos="fade-up"
        data-aos-offset="500"
        data-aos-duration="600"
        class="w-75 mx-auto">
        <div class="d-flex justify-content-between align-items-center">
          <div class="fw-semibold px-2 py-1 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">My Availabilities</div>
          <div class="fw-semibold" data-bs-target="#allMyAvailabilities" data-bs-toggle="modal" style="cursor: pointer;">Show all -></div>
        </div>
        <BlurredBGCard class="mt-1">
          <table class="table scrollable-table" v-if="myAvailabilites.length>0">
            <thead>
              <tr>
                <th class="fw-semibold">Name</th>
                <th class="fw-semibold">Date</th>
                <th class="fw-semibold" v-if="isLargeScreen">Time</th>
                <th class="fw-semibold">Game Mode</th>
                <th class="fw-semibold text-center">Action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="game in myAvailabilites" :key="game.playerAvailabilityId">
                <th>{{ game.tournament.name }}</th>
                <th class="text-wrap">{{ formatDate(game.startTime) }}</th>
                <th v-if="isLargeScreen">{{ extractTime(game.startTime, game.endTime) }}</th>
                <th>{{ formmattedMode(game.tournament.gameMode) }}</th>
                <th class="d-flex justify-content-center p-1"><button class="btn btn-primary fw-semibold" @click="triggerEditModal(game)">Edit</button></th>
              </tr>
            </tbody>
          </table>
          <div v-else class="p-3"> You do not have any availabilities :( Book a game below!</div>
        </BlurredBGCard>
      </div>


      <!-- all tournament carousel -->
      <div 
      data-aos="fade-up"
      data-aos-offset="500"
      data-aos-duration="700"
      class="tournamentsCarousel mb-5" >
        <span class="fw-semibold px-2 py-1 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">All Tournaments</span>
        <BlurredBGCard class="carouselBG mt-1" v-if="tournaments.length > 0" >
          <div
            id="alltournaments" 
            class="carousel slide" 
            data-bs-ride="carousel" 
            data-bs-pause="hover"
            ref="alltournamentsCarousel">
            <!-- indicator for each slide -->
            <div class="carousel-indicators">
              <button 
                v-for="(tournament,index) in tournaments" 
                :key="index" type="button" 
                data-bs-target="#alltournaments" 
                :data-bs-slide-to="index" 
                :class="{active: index==0 }"></button>
            </div>
            <div class="carousel-inner rounded-4">
              <!-- list of carousel items -->
              <div 
                v-for="(tournament,index) in tournaments" 
                :key="index"
                :class="{'carousel-item': true, 'active': index===0}"
                data-bs-interval="3000">

                <div class="d-flex justify-content-center mt-1">
                  <img :src="tournament.image" class="w-75 img-fluid rounded-4" alt="..." />
                </div>
  
                <!-- Move the caption outside of the image but inside the carousel-item div -->
                <div class=" m-auto text-center p-3 mt-2">
                  <h5 class="fw-semibold">{{tournament.name}}</h5>
                  <p style="height: 50px;" class="overflow-y-scroll text-wrap px-5"> {{ tournament.description }} </p>
                  <div class="d-flex justify-content-between align-items-center mb-3">
                    <div class="fw-semibold">
                      <div class="fw-semibold">Start Date: {{ formatDate(tournament.startDate) }}</div>
                      <div class="fw-semibold">End Date: {{ formatDate(tournament.endDate) }}</div>
                    </div>
                    
                    <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{formmattedMode(tournament.gameMode)}}</div>
                  </div>
                </div>
              </div>
            </div>
  
            <button class="carousel-control-prev" type="button" data-bs-target="#alltournaments" data-bs-slide="prev">
              <span class="carousel-control-prev-icon"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#alltournaments" data-bs-slide="next">
              <span class="carousel-control-next-icon"></span>
            </button>
          </div>
        </BlurredBGCard>
        <BlurredBGCard class="p-3" v-else> There is no upcoming tournaments :(</BlurredBGCard>
        
        <div class="d-flex justify-content-center mt-3">
          <button class="btn btn-primary w-50 text-white fw-bold" @click="bookCurrentTournament"> Book</button>
        </div>
      </div>


      <!-- modal to view all my schedule games -->
      <Modal 
        modalID= "scheduledGames"
        :showFooter="false" 
        header="My Scheduled Games" 
        >
        <table class="table" v-if="games.length>0">
          <thead>
            <tr>
              <th
                class="fw-semibold border-0" 
                v-for="(_,header) in games[0]" 
                :key="header" >{{ header }}</th>
              <th class="fw-semibold border-0" >Action</th>
            </tr>
            
          </thead>
          <tbody>
            <tr v-for="game in games" :key="game.id">
              <th  v-for="value in game" :key="value" class="border-0">{{ value }}</th>
              <th class="m-0 p-1 border-0">
                <button class="btn btn-primary fw-semibold" @click="deleteGame(game)">Delete</button>
              </th>
            </tr>
          </tbody>
        </table>
        <div v-else> You do not have any upcoming game :( Book a game below!</div>
      </Modal>

      <!-- modal to view all availabilities -->
      <Modal 
        modalID= "allMyAvailabilities"
        :showFooter="false" 
        header="My Availabilities" 
        >
        <table class="table" v-if="myAvailabilites.length>0">
          <thead>
            <tr>
              <th class="fw-semibold">Name</th>
              <th class="fw-semibold">Date</th>
              <th class="fw-semibold" v-if="isLargeScreen">Time</th>
              <th class="fw-semibold">Game Mode</th>
              <th class="fw-semibold">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="game in myAvailabilites" :key="game.playerAvailabilityId">
              <th>{{ game.tournament.name }}</th>
              <th>{{ formatDate(game.startTime) }}</th>
              <th v-if="isLargeScreen">{{ extractTime(game.startTime, game.endTime) }}</th>
              <th>{{ formmattedMode(game.tournament.gameMode) }}</th>
              <th class="p-1"><button class="btn btn-primary fw-semibold" @click="triggerEditModal(game)">Edit</button></th>
            </tr>
          </tbody>
        </table>
        <div v-else> You do not have any availabilities :( Book a game below!</div>
      </Modal>
      <!-- modal to book a tournament -->
      <BookingModal 
      modalID="booking" 
      :isEditing="false"
      :tournament="selectedTournament"
      @fetchPlayerAvail="fetchPlayerAvail"/>

      <!-- modal to edit booking -->
      <BookingModal 
      modalID="editAvailabilites" 
      :isEditing="true"
      :tournament="selectedTournament"
      @fetchPlayerAvail="fetchPlayerAvail"
      />

    </div>
</template>

<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import Modal from '@/components/modal/Modal.vue';
import BookingModal from '@/components/modal/BookingModal.vue';
import { Modal as bsModal } from 'bootstrap';
import axios from '@/utils/axiosInstance';
import { tournamentImage } from '@/utils/tournamentImage';
import { useUserStore } from '@/stores/store';


export default {
  name:"BookingView",
  components:{BlurredBGCard, Modal, BookingModal},
  data(){
    return{
      tournaments:[],
      myAvailabilites:[],
      games:[],
      activeTournamentIndex: 0,
      selectedTournament: '',
      isLargeScreen: window.innerWidth >= 992,
      tournamentCache: {}
    }
  },
  methods:{
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 992;
    },
    formatDate(date) {
      if (!date) return ''; // Handle null or undefined dates
      const day = String(date.getUTCDate()).padStart(2, '0');
      const month = String(date.getUTCMonth() + 1).padStart(2, '0'); // Month is zero-based
      const year = date.getUTCFullYear();
      return `${day}/${month}/ ${year}`;
    },
    extractTime(startTime, endTime) {
        if (!startTime || !endTime) return ''; // Handle null or undefined dates

        const formatTime = (dateObj) => {
            let hours = dateObj.getHours();
            const minutes = String(dateObj.getMinutes()).padStart(2, '0');
            const amPm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12 || 12; // Convert to 12-hour format
            return `${hours}:${minutes}${amPm}`;
        };

        const startFormatted = formatTime(new Date(startTime));
        const endFormatted = formatTime(new Date(endTime));

        return `${startFormatted} - ${endFormatted}`;
    },
    formmattedMode(mode){
      if(mode == "BATTLE_ROYALE") return "Battle Royale";
      if(mode == "CLANWAR") return "Clan War";
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

        for (let i = 0 ; i < allTournaments.length ; i++) {
          const tournament = allTournaments[i];
          
          const startDate = new Date(tournament.startDate);
          const endDate = new Date(tournament.endDate);
          
          let formattedTournament = { ...tournament, "startDate":startDate, "endDate": endDate, "image":tournamentImage[i%n]}

          if ( endDate > currentDateTime) {
            this.tournaments.push(formattedTournament);
          } 
        }
      } catch (error) {
        console.error('Error fetching tournaments:', error);
        throw error;
      }
    },
    async fetchPlayerAvail(){
      this.myAvailabilites = []
      console.log("wait wait wait")
      try{
        const response = await axios.get(`/matchmaking/api/playersAvailability?playerId=${this.userStore.user.id}`);
        const playerAvail = response.data;
        if(playerAvail.length == 0 ){
            return
        }
        const n = tournamentImage.length;
        for(let i = 0; i < playerAvail.length; i++){
          let booking = playerAvail[i]
          let tournamentId = booking.tournamentId;
          if (!this.tournamentCache[tournamentId]) {
            try{
              let getTournament = await axios.get(`/tournament/api/tournaments/${tournamentId}`)
              const startDate = new Date(getTournament.data.startDate);
              const endDate = new Date(getTournament.data.endDate);
              this.tournamentCache[tournamentId] = {...getTournament.data, "startDate":startDate, "endDate": endDate, "image":tournamentImage[i%n]};
            }catch(error){
              console.error('Error fetching tournament info:', error);
              return
            }
          }
          let bookingStartTime = new Date(booking.startTime);
          let bookingEndTime = new Date(booking.endTime);
          this.myAvailabilites.push({...booking,"tournament": this.tournamentCache[tournamentId], "startTime": bookingStartTime, "endTime":bookingEndTime});
        }
        // console.log(this.myAvailabilites)
        this.myAvailabilites = this.mergeConsecutiveBookings(this.myAvailabilites)
      }catch(error){
        console.log("error fetching player's availabilities. Erorr message: ", error.message)
        return
      }
    },
    fetchGames() {
      this.upcomingGames =[];
      axios.get(`/matchmaking/api/games/upcoming/player?playerId=${this.userStore.user.id}`)
        .then((response) => {
          // Ensure a successful response
          console.log(response)
          if (response.status === 200) {
            const gamesArr = response.data;

            // Sort the games array by startTime (earliest first)
            gamesArr.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));

            for(let i = 0; i < gamesArr.length; i++){
              let game = gamesArr[i]
              let tournamentId = game.tournamentId;
              
              try{
                axios.get(`/tournament/api/tournaments/${tournamentId}`).then((response)=>{
                  let getTournament = response.data
                  let gameStartTime = new Date(game.startTime);
                  let gameEndTime = new Date(game.endTime);
                  this.games.push({...game,"tournament": this.tournamentCache[tournamentId], "startTime": gameStartTime, "endTime":gameEndTime, "name": getTournament.name});
                })
              }catch(error){
                console.error('Error fetching tournament info:', error);
                return
              }
              
              // console.log(this.tournamentCache)
              
            }
            // console.log(this.games)
          }
        })
        .catch((error) => {
          console.error('Error fetching upcoming games:', error);
        });
    },
    bookCurrentTournament() {
      const activeIndex = this.getActiveCarouselIndex();
      this.selectedTournament = this.tournaments[activeIndex];

      this.$nextTick(() => {
        const modalID = 'booking';
        const tournamentModal = new bsModal(document.getElementById(modalID));
        tournamentModal.show();
      });
    },
    getActiveCarouselIndex() {
      const carouselElement = this.$refs.alltournamentsCarousel;
      const activeElement = carouselElement.querySelector('.carousel-item.active');
      return Array.from(carouselElement.querySelectorAll('.carousel-item')).indexOf(activeElement);
    },
    editGame(game){
      for(let booking of this.myAvailabilites){
        if(game.name === booking.name){
          for(let key in booking){
            booking[key] = game[key]
          }
        }
      }
    },
    triggerEditModal(game){
      this.selectedTournament = game
      const allMyBookingModal = bsModal.getInstance(document.getElementById("allMyAvailabilities"));
      if(allMyBookingModal){
        allMyBookingModal.hide();
      }
      this.$nextTick(() => {
        const modalID = 'editAvailabilites';
        const tournamentModal = new bsModal(document.getElementById(modalID));
        tournamentModal.show();
      });
    },
    deleteGame(){
      console.log("delete game")
    },
    mergeConsecutiveBookings(bookings) {
        // Sort the bookings by startTime
        bookings.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));

        const result = [];
        let currentGroup = null;

        bookings.forEach(booking => {
            if (
                currentGroup &&
                currentGroup.tournamentId === booking.tournamentId &&
                new Date(currentGroup.endTime).getTime() === new Date(booking.startTime).getTime() // Explicitly compare times
            ) {
                // Merge with the current group
                currentGroup.playerAvailabilityId.push(booking.playerAvailabilityId);
                currentGroup.endTime = booking.endTime;
            } else {
                // Push the current group to result and start a new group
                if (currentGroup) {
                    result.push(currentGroup);
                }
                currentGroup = {
                    playerAvailabilityId: [booking.playerAvailabilityId],
                    playerId: booking.playerId,
                    tournamentId: booking.tournamentId,
                    startTime: booking.startTime,
                    endTime: booking.endTime,
                    available: booking.available,
                    tournament: booking.tournament,
                };
            }
        });

        // Push the last group
        if (currentGroup) {
            result.push(currentGroup);
        }

        return result;
    }

  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
  },
  mounted(){
    this.fetchTournament();
    this.fetchPlayerAvail();
    this.fetchGames();
  },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }
}
</script>



<style scoped>
.tournamentsCarousel{
  width: 80%;
  margin: auto;
}
.carousel-inner {
  min-height: 300px;
}
.carousel-item img {
  height: 250px;
  object-fit:cover;
}

.carouselBG{
  background-color: rgba(0, 0, 0, 0.4);
}
.scrollable-table tbody th {
  color: white;
  border: 0;
  background-color: transparent;
}

.scrollable-table thead th {
  color: white;
  border: 0;
  background-color: transparent;
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
