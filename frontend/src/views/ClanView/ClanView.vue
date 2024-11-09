<template>
    <div v-if="userStore.hasClan">
      <div style="height: 50vh;">
        <BlurredBGCard class="mt-1">
          <div id="clanWarsSignedUp" 
          class="carousel m-auto h-100 slide border border-primary border-2" 
          style="width: 55vw;" 
          data-bs-ride="carousel" data-bs-pause="hover">
            <div class="carousel-indicators">
              <button 
                v-for="(tournamentname, index) in upcomingTournaments" 
                :key="index" 
                type="button" 
                data-bs-target="#clanWarsSignedUp" 
                :data-bs-slide-to="index" 
                :class="{ active: index === 0 }">
              </button>
            </div>
            <div class="carousel-inner h-100">
              <div 
                v-for="(tournament, index) in upcomingTournaments" 
                :key="index"
                :class="{'carousel-item': true, 'active': index === 0}"
                style="cursor: pointer; height: 100%;" 
                @click="showModal = true">
                
                <img 
                  :src="tournament.image" 
                  class="d-block img-fluid w-100 h-100" 
                  alt="..." 
                  style="object-fit: cover; height: 50vh;" /> 
                
                  <div class="carousel-caption bg-dark bg-opacity-50 text-center rounded d-flex flex-column justify-content-center">
                    <h2 class="fw-semibold">{{tournament.name}}</h2>
                    <p style="height: 20vh;" class="overflow-y-hidden text-wrap px-5"> {{ tournament.description }} </p>
                  </div>
                  
              </div>
            </div>

            <button class="carousel-control-prev" type="button" data-bs-target="#clanWarsSignedUp" data-bs-slide="prev">
              <span class="carousel-control-prev-icon"></span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#clanWarsSignedUp" data-bs-slide="next">
              <span class="carousel-control-next-icon"></span>
            </button>
          </div>
        </BlurredBGCard>
      </div>

    <!-- Modal structure -->
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal-content p-3">
          <button @click="showModal = false" class="close-modal-btn">
            <img src="https://static.vecteezy.com/system/resources/thumbnails/011/458/959/small_2x/letter-x-alphabet-in-brush-style-png.png" alt="Close">
          </button>
          <br>
          <h1 class="text-center">List Of Members Signed Up</h1>
          <div
            class="p-4 rounded shadow-sm my-3 w-100 d-flex justify-content-center overflow-auto"
            :style="{
              backgroundColor: 'rgba(255, 255, 255, 0.5)', 
              border: 'none' 
            }"
          >
            <table class="table table-bordered table-striped table-responsive">
            <thead class="table-dark text-center">
              <tr>
                <th>Number</th>
                <th>Username</th>
                <th>Position</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(person, index) in memberssignedup" :key="index">
                <td>{{ person.number }}</td>
                <td>{{ person.username }}</td>
                <td>{{ person.position }}</td>
              </tr>
            </tbody>
            </table>
          </div>

          <div class="mb-3"> 
            <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false" style="font-size: 1.5rem; padding: 2.75rem 3.25rem;">
              {{ selectedPosition || 'Position' }} 
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#" @click="selectPosition('Healer')">Healer</a></li>
              <li><a class="dropdown-item" href="#" @click="selectPosition('Roamer')">Roamer</a></li>
              <li><a class="dropdown-item" href="#" @click="selectPosition('Marksman')">Marksman</a></li>
              <li><a class="dropdown-item" href="#" @click="selectPosition('Mage')">Mage</a></li>
              <li><a class="dropdown-item" href="#" @click="selectPosition('Tank')">Tank</a></li>
            </ul>
          </div>

          <div class="d-flex justify-content-between">
            <button class="btn btn-primary btn-lg w-100" @click="bookTournament()">BOOK</button> 
          </div>    
        </div>
      </div>

      <div class="fs-4 text-white ms-4" :style="{ alignSelf: 'flex-start' }">
        <br>
        <h1>{{ clanStore.name }} Members</h1>
      </div>

      <div class="p-4 rounded my-3 d-flex justify-content-center overflow-auto w-100 border border-primary border-2 rounded-5 text-center blurred-bg-card">
        <table class="scrollable-table">
          <thead>
            <tr>
              <th>Position</th>
              <th>Profile</th>
              <th>Username</th>
              <th>Rank</th>
              <th>Elo</th>
              <th>Progress</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(person, index) in members" :key="index">
              <td>{{ person.position }}</td>
              <td>
                <img :src="person.profile" style="height: 100px;"/>
              </td>
              <td>{{ person.username }}</td>
              <td>{{ person.rank }}</td>
              <td>{{ person.elo }}</td>
              <td>{{ person.progress }}</td>
            </tr>
          </tbody>
        </table>
      </div>
  </div>

  
  <!-- !!!!!!!!!!!!!!!IF THIS GUY GOT NO CLAN!!!!!!!!!!!!!!!! -->
  <div v-else>
    <div
      class="d-flex flex-column justify-content-center align-items-center min-vh-100 w-100"
      :style="{
        margin: 0,
        padding: 0
      }"
    >
    <div class="p-4 rounded my-3 d-flex justify-content-center overflow-auto w-100 border border-primary border-2 rounded-5 text-center blurred-bg-card">
      <table class="scrollable-table">
        <thead>
          <tr>
            <th>Clan Icon</th>
            <th>Clan Name</th>
            <th>Members</th>
            <th>Rank</th>
            <th>Elo</th>
            <th>Request</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(clan, index) in availableclans" :key="index">
            <td>
              <img :src="clan.clanicon" style="height: 100px;"/>
            </td>
            <td>
              <span
                class="text-decoration-underline text-body"
                @click="showModal = true"
                @mouseover="hover = true"
                @mouseleave="hover = false"
                :style="{
                  cursor: 'pointer',
                  border: 'none',
                  color: 'white !important'
                }"
              >
                {{ clan.clanname }}
              </span>
            </td>
            <td>{{ clan.members }}</td>
            <td>{{ clan.rank }}</td>
            <td>{{ clan.elo }}</td>
            <td>
              <button
                :class="{'request': clan.request}"
                @click="toggleRequest(index)"
              >
                {{ clan.request ? 'Requested' : 'Request' }}
              </button>
            </td>
          </tr>
        </tbody>
        </table>
        <div></div>
      </div>
  
      <!-- Modal structure -->
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal-content border border-primary border-2 rounded-5 ">
          <h1>Clan Info</h1>
          <!-- Close button inside the modal -->
          <button @click="showModal = false" class="close-modal-btn">
            <img src="https://static.vecteezy.com/system/resources/thumbnails/011/458/959/small_2x/letter-x-alphabet-in-brush-style-png.png">
          </button>
          <div class="modal-body">
            <div
              class="p-4 rounded shadow-sm my-3 w-100 d-flex justify-content-center overflow-auto"
              :style="{
                backgroundColor: 'rgba(255, 255, 255, 0.5)', 
                border: 'none' 
              }"
            >
              <table class="data-table responsive-table">
              <thead>
                <tr>
                  <th>Position</th>
                  <th>Profile</th>
                  <th>Username</th>
                  <th>Rank</th>
                  <th>Elo</th>
                  <th>Progress</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(person, index) in members" :key="index">
                  <td>{{ person.position }}</td>
                  <td>
                    <img :src="person.profile" style="height: 100px;"/>
                  </td>
                  <td>{{ person.username }}</td>
                  <td>{{ person.rank }}</td>
                  <td>{{ person.elo }}</td>
                  <td>{{ person.progress }}</td>
                </tr>
              </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
  
    </div>
  </div>

</template>

<script>
  import { useUserStore } from '@/stores/store';
  import { useClanStore } from '@/stores/store';
  import { Modal as bsModal } from 'bootstrap';
  import Swal from 'sweetalert2'
  import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';

  export default {
    name: 'TranslucentBox',
    // components:{BlurredBGCard},
    methods: {
      toggleRequest(index) {
        this.availableclans[index].request = !this.availableclans[index].request;
      },
      handleClick() {
        alert('Image button clicked!'); 
      },
      selectUpcomingTournament(tournament) {
        alert('Image button clicked!'); 
        console.log(tournament);
      },
      selectPosition(position) {
        this.selectedPosition = position;
      },
      bookTournament() {
        console.log(`Booked tournament for position: ${this.selectedPosition}`);
        Swal.fire({
            title: "Confirm Booking?",
            reverseButtons: true, // Swaps the position of confirm and cancel buttons
            icon: "warning",
            showCancelButton: true,
            cancelButtonColor: "#DDDDDD",
            confirmButtonColor: "#FA9021",
            confirmButtonText: "Yes, BOOK it!"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: "Booked!",
                    text: "Your booking has been made",
                    icon: "success",
                    timer: 1500
                });
                
                const existingModal = bsModal.getInstance(document.getElementById(this.modalID));
                existingModal.hide();
            }
        });
      },
    },

  setup() {
    const clanStore = useClanStore();
    const userStore = useUserStore();
    return {
      userStore,
      clanStore,
    };
  },

  data() {
    return {

      availableclans: [
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/11619/11619566.png', clanname: 'rtyhuJK', members: '3/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/6695/6695008.png', clanname: 'SDfgHjK', members: '4/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://cdn-icons-png.flaticon.com/512/8108/8108364.png', clanname: 'ERtyUIo', members: '2/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://upload.wikimedia.org/wikipedia/commons/d/dd/Clan_Uchiwa.png', clanname: 'ZXcvbn', members: '4/5', rank: 'Diamond', elo: 889, request: false },
        { clanicon: 'https://i.pinimg.com/736x/06/5e/c9/065ec9a47b6db3d597b4870d29faf428.jpg', clanname: 'We4567*9', members: '1/5', rank: 'Diamond', elo: 889, request: false },
      ],
      members: [
        { position: 1, profile: 'https://seeklogo.com/images/O/one-piece-kozuki-clan-logo-98DE5338D7-seeklogo.com.png', username: 'Shawn The Sheep', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 2, profile: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTp98gwVWdaVG-Tn8J_o0sVzn3Hi8kSyS8NXA&s', username: 'Sean Kingston', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 3, profile: 'https://64.media.tumblr.com/9a5d075a3533bbb19637ebc05ee572fd/1b94cfa3123e528b-4d/s250x400/8149f85f9fc507222cbed4ee82f5b1e74465ddcf.png', username: 'Diddy Kong', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 4, profile: 'https://forum.truckersmp.com/uploads/monthly_2019_06/imported-photo-186659.thumb.jpeg.7ca80c40fa6e972e04cc2f14f5114d80.jpeg', username: 'H3FF', rank: 'Diamond', elo: 889, progress: 'example' },
        { position: 5, profile: 'https://steamavatar.io/img/14773519040Sv21.jpg', username: 'Sam Teh', rank: 'Diamond', elo: 889, progress: 'example' },
      ],
      upcomingTournaments: [
        {
          name: 'Tournament 1',
          image: 'https://assetsio.gnwcdn.com/blackmythwukong1.jpg?width=1200&height=1200&fit=bounds&quality=70&format=jpg&auto=webp',
          description: 'Five Nights at Freddys (FNAF) is a survival horror video game series created by Scott Cawthon, where players take on the role of nighttime security personnel at a haunted family-friendly pizzeria filled with animatronic characters.',
        },
        {
          name: 'Tournament 2',
          image: 'https://c4.wallpaperflare.com/wallpaper/34/309/213/black-myth-wukong-game-science-hd-wallpaper-preview.jpg',
          description: 'The Legend of Zelda: Breath of the Wild is an open-world action-adventure game developed by Nintendo, where players control Link, who awakens from a long slumber to defeat Calamity Ganon and save the kingdom of Hyrule.',
        },
        {
          name: 'Tournament 3',
          image: 'https://c4.wallpaperflare.com/wallpaper/508/158/505/black-myth-wukong-%E6%8F%92%E7%94%BB%E5%B8%88%E5%B1%85%E5%A3%AB-hd-wallpaper-preview.jpg',
          description: 'Wukong is an action-adventure game inspired by the classic Chinese tale "Journey to the West," where players control a character based on the legendary Monkey King, Sun Wukong. ',
        },
      ],
      memberssignedup: [
          { number: 1, username: 'Seaking', position: 'Healer' },
          { number: 2, username: 'Uvuvuvuvue oyenteveveve uvuvuvuvue ossas', position: 'Marksman' },
          { number: 3, username: this.playername , position: this.positionrole},
          { number: 4 },
          { number: 5 },
      ],
      showModal: false,
    };
  }
  };
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
    vertical-align: middle;
    font-weight: 600;
  }

  .scrollable-table tbody {
    display: block;
    overflow-y: auto;
  }

  .scrollable-table thead, .scrollable-table tbody tr {
    display: table;
    width: 100%;
    table-layout: fixed;
  }
  
  .blurred-bg-card {
    background: rgba(255, 255, 255, 0.1); /* Semi-transparent background */ 
    backdrop-filter: blur(10px); /* Blurring effect */
    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1); /* shadow effect */
  }

  .plusboximg {
    height: 150px;
  }

  /* Modal overlay style */
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5); 
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    color: black;
  }
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    width: 80%;
    height: 80%; 
    text-align: center;
    overflow: hidden; 
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }
  .modal-body {
    overflow-y: auto; 
    height: 200px; 
    margin-bottom: 10px; 
  }
  .open-modal-btn,
  .close-modal-btn {
    padding: 10px 20px;
    background-color: grey;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  .open-modal-btn:hover,
  .close-modal-btn:hover {
    background-color: darkgray;
  }
  button.clicked {
    background-color: #28a745; 
    color: white;
  }
  button:hover {
    background-color: #e09f13; 
    color: white;
  }

  .close-modal-btn {
    position: absolute;
    top: 10px; 
    right: 10px;
    background: none;
    border: none;
    cursor: pointer;
  }
  .close-modal-btn img {
    width: 24px;
    height: 24px;
  }

</style>
