<!-- - Name 
- Description
- Start date, end date 
- Games per day
- Game mode  (BR or CW)
- People limit (only for BR)  -->
<template>
  <div class="w-75 m-5 mx-auto min-vh-75">
    <div class="d-flex justify-content-end">
        <button class="btn btn-primary fw-semibold text-white" @click="showAdminModal">Create Tournament</button>
    </div>

    <TournamentTable 
        :TournamentList="activeTournaments" 
        :isActive="true" 
        :isLargeScreen="isLargeScreen"/>

    <TournamentTable 
        :TournamentList="upcomingTournaments" 
        :isActive="false" 
        @action="editTournament" 
        :isLargeScreen="isLargeScreen"/>

    <!-- admin modal to create or update -->
    <Modal
        :header="selectedTournament.tournament_id ? 'Update Tournament' : 'Create Tournament'"
        modalID="adminModal"
        :showFooter="true"
        :action="selectedTournament.tournament_id ? updateTournament : createTournament"
        :actionName="selectedTournament.tournament_id ? 'Update' : 'Create'">
        <div class="d-flex justify-content-between">
            <!-- Game mode -->
            <div class="dropdown mb-3">
              <button
                class="btn btn-secondary dropdown-toggle"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                {{ formattedTournamentMode || "Select Game Mode" }}
              </button>
              <ul class="dropdown-menu">
                <li>
                  <a class="dropdown-item" href="#" @click="selectedTournament.gameMode = 'BATTLE_ROYALE'">Battle Royale</a>
                </li>
                <li>
                  <a class="dropdown-item" href="#" @click="selectedTournament.gameMode = 'CLANWAR'">Clan War</a>
                </li>
              </ul>
            </div>
            <!-- delete button for existing tournament -->
             <div>
                <button class="btn btn-secondary" @click="deleteTournament" v-if="selectedTournament.tournament_id != null">Delete</button>
             </div>
        </div>
    
        <div class="row">
          <!-- Name -->
          <div class="mb-3 col-md-6">
            <label for="name" class="form-label">Name</label>
            <input type="text" class="form-control" id="name" v-model="selectedTournament.name" />
          </div>
    
          <!-- Description -->
          <div class="mb-3 col-md-6">
            <label for="desc" class="form-label">Description</label>
            <textarea type="text" class="form-control" id="desc" v-model="selectedTournament.description"></textarea>
          </div>
    
          <!-- Start Date -->
          <div class="mb-3 col-md-6">
            <label for="startDate" class="form-label">Start Date</label>
            <DatePicker v-model="selectedTournament.startDate" :minDate="minDate" fluid dateFormat="dd/mm/yy" showIcon />
          </div>
    
          <!-- End Date -->
          <div class="mb-3 col-md-6">
            <label for="endDate" class="form-label">End Date</label>
            <DatePicker v-model="selectedTournament.endDate" :minDate="minDate" fluid dateFormat="dd/mm/yy" showIcon />
          </div>

          <!-- player Capacity -->
          <div class="mb-3 col-md-6">
            <label for="PlayerCapacity" class="form-label">Player Capacity</label>
            <input
              type="number"
              class="form-control"
              id="PlayerCapacity"
              v-model="selectedTournament.playerCapacity"
            />
          </div>
    
        </div>
    </Modal>
  </div>
</template>

<script>
import DatePicker from "primevue/datepicker";
import Modal from "@/components/modal/Modal.vue";
import TournamentTable from "@/components/TournamentTable/TournamentTable.vue";
import { Modal as bsModal } from 'bootstrap';
import Swal from 'sweetalert2'
import axios from "@/utils/axiosInstance";
import { useUserStore } from '@/stores/store';


export default {
  name: "AdminView",
  components: { DatePicker, Modal, TournamentTable, bsModal },
  data() {
    return {
      selectedTournament: {
        tournament_id: null,
        name: "",
        description: "",
        startDate: null,
        endDate: null,
        playerCapacity: null,
        status: "",
        gameMode: "",
        adminId: null,
        playerIds: [],
      },
      activeTournaments:[],
      upcomingTournaments:[],
      isLargeScreen: window.innerWidth >= 992,
      minDate: new Date(Date.now() + 24 * 60 * 60 * 1000),
    };
  },
  computed:{
    formattedTournamentMode(){
      if (this.selectedTournament.gameMode == "BATTLE_ROYALE"){
        return "Battle Royale";
      }
      if (this.selectedTournament.gameMode == "CLANWAR"){
        return "Clan War";
      }
    }
  },
  methods: {
    async createTournament() {
      if (!this.validateInput()) {
        this.showErrorAlert('Please fill in all inputs');
        return;
      }
      if (!this.validateDates()) {
        this.showErrorAlert('Please verify your date.');
        return;
      }

      try {
        const response = await axios.post('/tournament/api/tournaments/create', {
          ...this.selectedTournament,
          "adminId": this.userStore.user.id,
          "status": "INACTIVE"
        });
        
        this.hideAdminModal();
        this.bookSuccess("Tournament Created Successfully", "Tournament Created!");
      } catch (error) {
        console.error('Error creating tournament:', error);
        const errorMessage = error.response?.data?.message || 'An error occurred while creating the tournament.';
        this.showErrorAlert(errorMessage);
      }
      this.fetchTournament();
    },

    async updateTournament() {
      if (!this.validateInput()){
        this.showErrorAlert('Please fill in all inputs');
        return;
      } 
      if(!this.validateDates()) {
          this.showErrorAlert('Please verfiy your date.');
          return;
      }
      
      try {
        const response = await axios.put(`/tournament/api/tournaments/${this.selectedTournament.tournament_id}`, this.selectedTournament);
        this.hideAdminModal();
        this.bookSuccess("Tournament Updated Successfully", "Tournament Updated!");
      } catch (error) {
        console.error('Error creating tournament:', error);
        const errorMessage = error.response?.data?.message || 'An error occurred while creating the tournament.';
        this.showErrorAlert(errorMessage);
      }
      this.fetchTournament();
    },
    async deleteTournament() {
      const result = await Swal.fire({
        title: "Confirm Delete?",
        icon: "warning",
        showCancelButton: true,
        cancelButtonColor: "#DDDDDD",
        confirmButtonColor: "#FA9021",
        confirmButtonText: "Delete"
      });

      if (result.isConfirmed) {
        try {
          const response = await axios.delete(`/tournament/api/tournaments/${this.selectedTournament.tournament_id}`,{
            params: {
              "requestingAdminId" : this.selectedTournament.adminId
            }
          });

          Swal.fire({
            title: "Deleted!",
            text: "Your booking has been deleted.",
            icon: "success",
            timer: 1500,
            showConfirmButton: false
          });

          this.hideAdminModal();
          this.fetchTournament();
        } catch (error) {
          console.error('Error deleting tournament:', error);
          const errorMessage = error.response?.data?.message || 'An error occurred while deleting the tournament.';
          this.showErrorAlert(errorMessage);
        }
      }
    },
    resetAdminModal() {
    //   console.log('clear')
      this.selectedTournament={
          tournament_id: null,
          name: "",
          description: "",
          startDate: null,
          endDate: null,
          playerCapacity: null,
          status: "",
          gameMode: "",
          adminId: null,
          playerIds: [],
        }
    },
    validateDates() {
      if (this.selectedTournament.startDate && this.selectedTournament.endDate) {
        const startDate = new Date(this.selectedTournament.startDate);
        const endDate = new Date(this.selectedTournament.endDate);

        if (startDate > endDate) {
          // Reset the date if invalid
          this.selectedTournament.endDate = null;
          return false
        }
        return true
      }
    },
    validateInput() {
        return Object.keys(this.selectedTournament).every(key => {
            const value = this.selectedTournament[key];
            // Skip the 'id' field
            if (key === 'tournament_id' || key === 'status' || key === 'adminId' || key === 'playerIds' ) {
                return true;
            }
            // Check that value is filled
            return value !== null && value !== "" && value !== undefined;
        });
    },
    editTournament(data){
        this.selectedTournament = Object.assign({}, data);
        this.showAdminModal();
    },
    checkScreenSize() {
        this.isLargeScreen = window.innerWidth >= 992;
    },
    showAdminModal(){
        const modalID = 'adminModal';
        const tournamentModal = new bsModal(document.getElementById(modalID));
        tournamentModal.show();
    },
    hideAdminModal(){
        const existingModal = bsModal.getInstance(document.getElementById('adminModal'));
        existingModal.hide();
    },
    showErrorAlert(errorMessage){
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "error",
            title: "Invalid Input",
            text: errorMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    bookSuccess(successMessage, title){
        Swal.fire({
            position: "center",
            icon: "success",
            title: title,
            text: successMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    async fetchTournament() {
      try {
        const response = await axios.get('/tournament/api/tournaments');
        console.log(response.data);
        
        if (response.status === 404) {
          this.activeTournaments = [];
          this.upcomingTournaments = [];
          return;
        }
        
        const allTournaments = response.data;
        const currentDateTime = new Date();
        
        this.activeTournaments = [];
        this.upcomingTournaments = [];

        for (const tournament of allTournaments) {
          const startDate = new Date(tournament.startDate);
          const endDate = new Date(tournament.endDate);
          let formattedTournament = { ...tournament, "startDate":startDate, "endDate": endDate}
          
          if (startDate <= currentDateTime && endDate >= currentDateTime) {
            // Tournament is currently active
            this.activeTournaments.push(formattedTournament);
          } else if (startDate > currentDateTime) {
            // Tournament is upcoming
            this.upcomingTournaments.push(formattedTournament);
          }
        }
      } catch (error) {
        console.error('Error fetching tournaments:', error);
        throw error;
      }
    }
  },
  mounted() {
    const myModalEl = document.getElementById("adminModal");
    // myModalEl.addEventListener("show.bs.modal", (event) => {});
    //make sure that when it is hidden, clear all value
    myModalEl.addEventListener("hidden.bs.modal", (event) => {
      this.resetAdminModal();
    });
    this.fetchTournament();
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
  },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
  setup(){
    const userStore = useUserStore();
    return {userStore}
  }

};
</script>

<style>

</style>
