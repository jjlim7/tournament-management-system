<template>
  <div class="w-75 mt-3 mx-auto">
    <!-- my bookings -->
    <div class="w-75 mx-auto">
      <div class="d-flex justify-content-between align-items-center">
        <div class="fw-semibold px-2 py-1 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">My Bookings</div>
        <div class="fw-semibold" data-bs-target="#allMyBookings" data-bs-toggle="modal" style="cursor: pointer;">Show all -></div>
      </div>
      <BlurredBGCard class="mt-1">
        <table class="table scrollable-table" v-if="myBookings.length>0">
          <thead>
            <tr>
              <th class="fw-semibold">Name</th>
              <th class="fw-semibold">Date</th>
              <th class="fw-semibold" v-if="isLargeScreen">Time</th>
              <th class="fw-semibold" v-if="isLargeScreen">Role</th>
              <th class="fw-semibold">Game Mode</th>
              <th class="fw-semibold">Action</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="game in myBookings" :key="game.id">
              <th>{{ game.name }}</th>
              <th>{{ game.date }}</th>
              <th v-if="isLargeScreen">{{ game.time }}</th>
              <th v-if="isLargeScreen">{{ game.role }}</th>
              <th>{{ game.gameMode }}</th>
              <th class="d-flex justify-content-center p-1"><button class="btn btn-primary fw-semibold" @click="triggerEditModal(game)">Edit</button></th>
            </tr>
          </tbody>
        </table>
        <div v-else class="p-3"> You do not have any upcoming game :( Book a game below!</div>
      </BlurredBGCard>
    </div>

    <!-- all tournament carousel -->
    <div class="tournamentsCarousel">
      <span class="fw-semibold px-2 py-1 rounded-4" style="background-color: rgba(0, 0, 0, 0.4);">All Tournaments</span>
      <BlurredBGCard class="carouselBG mt-1">
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
                <p style="height: 80px;" class="overflow-y-hidden text-wrap px-5"> {{ tournament.description }} </p>
                <div class="d-flex justify-content-between align-items-center">
                  <div class="fw-semibold">
                    <div class="fw-semibold">Start Date: {{ tournament.startDate }}</div>
                    <div class="fw-semibold">End Date: {{ tournament.endDate }}</div>
                  </div>
                  
                  <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{tournament.gameMode}}</div>
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
      
      <div class="d-flex justify-content-center mt-3">
        <button class="btn btn-primary w-50 text-white fw-bold" @click="bookCurrentTournament"> Book</button>
      </div>
    </div>


    <!-- modal to view all bookings -->
    <Modal 
      modalID= "allMyBookings"
      :showFooter="false" 
      header="My Bookings" 
      v-if="myBookings.length>0"
      >
      <table class="table">
        <thead>
          <tr>
            <th 
              class="fw-semibold border-0" 
              v-for="(_,header) in myBookings[0]" 
              :key="header" >{{ header }}</th>
            <th class="fw-semibold border-0" >Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="game in myBookings" :key="game.id">
            <th  v-for="value in game" :key="value" class="border-0">{{ value }}</th>
            <th class="m-0 p-1 border-0">
              <button class="btn btn-primary fw-semibold" @click="triggerEditModal(game)">Edit</button>
            </th>
          </tr>
        </tbody>
      </table>
    </Modal>
    <!-- modal to book a tournament -->
    <BookingModal 
    modalID="booking" 
    :isEditing="false"
    :tournament="selectedTournament"/>

    <!-- modal to edit booking -->
    <BookingModal 
    modalID="editBooking" 
    :isEditing="true"
    :tournament="selectedTournament"/>

  </div>
</template>

<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
import Modal from '@/components/modal/Modal.vue';
import BookingModal from '@/components/modal/BookingModal.vue';
import { Modal as bsModal } from 'bootstrap';

export default {
name:"BookingView",
components:{BlurredBGCard, Modal, BookingModal},
data(){
  return{
    tournaments:[
      {
        id: "123",
        name: "Tournament #1",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500sLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg",
        startDate: "start date",
        endDate: "end date",
        gameMode:"Clan War"
      },
      {
        id: "124",
        name: "Tournament #2",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg",
        startDate: "start date",
        endDate: "end date",
        gameMode:"Battle Royale"
      },
      {
        id: "125",
        name: "Tournament #3",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg",
        startDate: "start date",
        endDate: "end date",
        gameMode:"Clan War"
      },
      {
        id: "126",
        name: "Tournament #4",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg",
        startDate: "start date",
        endDate: "end date",
        gameMode:"Battle Royale"
      },
      {
        id: "127",
        name: "Tournament #5",
        description: "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s",
        image: "https://cdn.mos.cms.futurecdn.net/cRFFW6JNXqEtkBA3P2U68m.jpg",
        startDate: "start date",
        endDate: "end date",
        gameMode:"Clan War"
      }
    ],
    myBookings:[
      {
        name: 'Game #1',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
      {
        name: 'Game #2',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
      {
        name: 'Game #3',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
      {
        name: 'Game #4',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
      {
        name: 'Game #5',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
      {
        name: 'Game #6',
        date: "21 sept 2024",
        time: "12:00 PM",
        role: "-",
        gameMode: "Battle Royale",
      },
    ],
    activeTournamentIndex: 0,
    selectedTournament: '',
    isLargeScreen: window.innerWidth >= 992,
  }
},
methods:{
  checkScreenSize() {
      this.isLargeScreen = window.innerWidth >= 992;
  },
  bookCurrentTournament() {
    const activeIndex = this.getActiveCarouselIndex();
    this.selectedTournament = this.tournaments[activeIndex];
    const modalID = 'booking';
    const tournamentModal = new bsModal(document.getElementById(modalID));
    tournamentModal.show();
  },
  getActiveCarouselIndex() {
    const carouselElement = this.$refs.alltournamentsCarousel;
    const activeElement = carouselElement.querySelector('.carousel-item.active');
    return Array.from(carouselElement.querySelectorAll('.carousel-item')).indexOf(activeElement);
  },
  editGame(game){
    for(let booking of this.myBookings){
      if(game.name === booking.name){
        for(let key in booking){
          booking[key] = game[key]
        }
      }
    }
  },
  triggerEditModal(game){
    this.selectedTournament = game
    const allMyBookingModal = bsModal.getInstance(document.getElementById("allMyBookings"));
    if(allMyBookingModal){
      allMyBookingModal.hide();
    }
    this.$nextTick(() => {
      const modalID = 'editBooking';
      const tournamentModal = new bsModal(document.getElementById(modalID));
      tournamentModal.show();
    
    });
  }
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
.tournamentsCarousel{
width: 80%;
margin: auto;
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
