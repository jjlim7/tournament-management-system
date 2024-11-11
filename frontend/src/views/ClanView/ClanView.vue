<template>
    <div v-if="this.userStore.user.clan != null"
    data-aos="fade-up"
    data-aos-offset="500"
    data-aos-duration="500">
      <div class="fs-4 text-white ms-4" :style="{ alignSelf: 'flex-start' }">
        <br>
        <h1>Available Clan War Tournaments</h1>
      </div>
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
                @click="selectClanWarTournament(tournament.tournamentId)">
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
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false"
      data-aos="fade-up"
      data-aos-offset="500"
      data-aos-duration="500">
        <div class="modal-content p-3">
          <button @click="showModal = false" class="close-modal-btn">
            <img src="https://static.vecteezy.com/system/resources/thumbnails/011/458/959/small_2x/letter-x-alphabet-in-brush-style-png.png" alt="Close">
          </button>
          <br>
          <h1 class="text-center">List Of Players Signed Up</h1>
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
                <th>Clan</th>
                <th>Username</th>
                <th>Position</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(person, index) in memberssignedup" :key="index">
                <td>{{ person.number }}</td>
                <td>{{ person.clanName }}</td>
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
        <h1>{{ this.userStore.user.clan?.clanName }} Members</h1>
      </div>

      <div class="p-4 rounded my-3 d-flex justify-content-center overflow-auto w-100 border border-primary border-2 rounded-5 text-center blurred-bg-card">
        <table class="scrollable-table">
          <thead>
            <tr>
              <th>Profile</th>
              <th>Username</th>
              <th>Email</th>
              <th>Rank</th>
              <th>Position</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(person, index) in members" :key="index">
              <td>
                <img :src="person.profile" style="height: 100px;"/>
              </td>
              <td>{{ person.username }}</td>
              <td>{{ person.email }}</td>
              <td>{{ person.rank }}</td>
              <td>{{ person.position }}</td>
            </tr>
          </tbody>
        </table>
      </div>
  </div>

  
  <!-- !!!!!!!!!!!!!!!IF THIS GUY GOT NO CLAN!!!!!!!!!!!!!!!! -->
  <div v-else
  data-aos="fade-up"
  data-aos-offset="500"
  data-aos-duration="500">
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
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false"
      data-aos="fade-up"
      data-aos-offset="500"
      data-aos-duration="500">
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
                  <th>Elo</th>
                  <th>Rank</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(person, index) in members" :key="index">
                  <td>{{ person.position }}</td>
                  <td>
                    <img :src="person.profile" style="height: 100px;"/>
                  </td>
                  <td>{{ person.username }}</td>
                  <td>{{ person.elo }}</td>
                  <td>{{ person.rank }}</td>
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
  import { Modal as bsModal } from 'bootstrap';
  import Swal from 'sweetalert2'
  // import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';
  import axios from '@/utils/axiosInstance';
  import { tournamentImage } from '@/utils/tournamentImage';

  export default {
    name: 'TranslucentBox',
    // components:{BlurredBGCard},

    data() {
      return {
        selectedUpcomingTournamentId: '',
        availableclans: [],  // For storing available clans
        members: [],  // For storing clan members
        upcomingTournaments: [],  // For storing upcoming tournaments
        memberssignedup: [],  // For storing members signed up for tournaments
        showModal: false,  // To control modal visibility
        selectedPosition: null,  // To store selected position
        loading: false,  // To show a loading state while fetching data
        error: null,  // To handle any errors during the fetch
      };
    },

    mounted() {
        this.fetchData();
        console.log(this.userStore.user)
      },

    setup() {
      const userStore = useUserStore();
      return {
        userStore,
      };
    },

    methods: {

      async fetchData() {
        try {
            this.loading = true; // Start loading

            // Check if user has a clan
            const clanId = this.userStore.user.clan?.clanId;

            // Define API calls conditionally
            const promises = [
                axios.get('/clanuser/api/clans'),
                axios.get(`/tournament/api/tournaments`)
            ];

            if (clanId) {
                promises.push(axios.get(`/clanuser/api/clanusers/clan/${clanId}`));
            }

            const [clansResponse, tournamentsResponse, membersResponse] = await Promise.all(promises);

            const clansData = clansResponse.data;

            // Get member counts for each clan asynchronously
            const memberCountsPromises = clansData
                .filter(clan => clan) // Filter out null or undefined clans
                .map(clan => this.countMembersInClan(clan.clanId));
            const memberCounts = await Promise.all(memberCountsPromises);

            this.availableclans = clansResponse.data.map((clandata, index) => ({
                clanId: clandata.clanId,
                clanicon: "https://cdn-icons-png.flaticon.com/512/11619/11619566.png", // Default icon
                clanname: clandata.clanName, // Backend returns `clanName`
                members: memberCounts[index],
                rank: "null", // Placeholder or calculate dynamically
                elo: 1000, // Placeholder or calculate dynamically
                request: false, // Logic to set request status
            }));

            // Transform members only if membersResponse exists
            if (membersResponse) {
                this.members = membersResponse.data.map(member => ({
                    userId: member.user.userId,
                    position: member.position, // Position directly available
                    profile: "https://cdn-icons-png.flaticon.com/512/11619/11619566.png", // Default profile pic
                    username: member.user.name,
                    email: member.user.email,
                    rank: "unranked",
                }));
            }

            // Filter tournaments with gamemode 'CLANWAR'
            this.upcomingTournaments = tournamentsResponse.data
                .filter(tournament => tournament.gameMode === 'CLANWAR') // Filter only clanwar tournaments
                .map((tournament, index) => ({
                    tournamentId: tournament.tournament_id,
                    name: tournament.name, // Tournament name from backend
                    image: tournamentImage[index] || 'https://assetsio.gnwcdn.com/blackmythwukong1.jpg?width=1200&height=1200&fit=bounds&quality=70&format=jpg&auto=webp', // Placeholder if no image
                    description: tournament.description || 'No description available', // Default description
                }));

            this.memberssignedup = tournamentsResponse.data
                .filter(tournament => tournament.gameMode === 'CLANWAR') // Filter only clanwar tournaments
                .map((person, index) => ({
                    number: index + 1, // Player number
                    clanName: "null",
                    username: person.playerIds[index], // Placeholder for usernames
                    position: 'Unknown', // Default position
                }));

            await this.UpdateTable();
            this.loading = false; // End loading

        } catch (error) {
            console.error(error); // Log error for debugging
            this.error = 'Failed to load data. Please try again later.'; // Handle error
            this.loading = false; // End loading
        }
    }
,

      async selectClanWarTournament(tournamentId) {

        try {
          // Fetch the tournament data based on the tournamentId
          const tournamentResponse = await axios.get(`/tournament/api/tournaments/${tournamentId}`);

          // Overwrite tournamentResponse.data with the new data
          if (tournamentResponse.data && Array.isArray(tournamentResponse.data)) {
              // You can overwrite the memberssignedup or any other variables you want to update
              this.memberssignedup = tournamentResponse.data.map((person, index) => ({
                  number: index + 1,  // Player number, starting from 1
                  username: person.playerIds[index] || 'Unknown',  // Ensure playerIds is defined
                  position: person.position || 'Unknown',  // Default position if not provided
              }));
          } else {
              console.error('Tournament data is invalid or empty:', tournamentResponse.data);
          }

          // Show the modal after updating tournament data
          this.showModal = true;
        } catch (error) {
          console.error('Error fetching tournament details:', error);
        }
      },

      toggleRequest(index) {
        this.availableclans[index].request = !this.availableclans[index].request;
      },
      handleClick() {
        alert('Image button clicked!'); 
      },
      selectUpcomingTournament(tournament) {
        alert('Image button clicked!'); 
      },
      selectPosition(position) {
        this.selectedPosition = position;
      },
      bookTournament() {
        Swal.fire({
            title: "Confirm Booking?",
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

      async countMembersInClan(countingfromclanId) {
          try {
              // Use GET instead of POST if you are simply fetching data
              const membersinclanresponse = await axios.get(`/clanuser/api/clanusers/clan/${countingfromclanId}`);

              const clanMembers = membersinclanresponse?.data;  // Get the members list directly

              return clanMembers.length;  // Return the count of members
          } catch (error) {
              console.error(`Failed to count members for clan ID ${countingfromclanId}`, error);
              return 0;  // Return 0 in case of an error
          }
      }
    },

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
