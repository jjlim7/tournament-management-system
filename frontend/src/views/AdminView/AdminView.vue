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
        :header="selectedTournament.id ? 'Update Tournament' : 'Create Tournament'"
        modalID="adminModal"
        :showFooter="true"
        :action="selectedTournament.id ? updateTournament : createTournament"
        :actionName="selectedTournament.id ? 'Update' : 'Create'">
        <div class="d-flex justify-content-between">
            <!-- Game mode -->
            <div class="dropdown mb-3">
              <button
                class="btn btn-secondary dropdown-toggle"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                {{ selectedTournament.gameMode || "Select Game Mode" }}
              </button>
              <ul class="dropdown-menu">
                <li>
                  <a class="dropdown-item" href="#" @click="selectedTournament.gameMode = 'Battle Royale'">Battle Royale</a>
                </li>
                <li>
                  <a class="dropdown-item" href="#" @click="selectedTournament.gameMode = 'Clan War'">Clan War</a>
                </li>
              </ul>
            </div>
            <!-- delete button for existing tournament -->
             <div>
                <button class="btn btn-secondary" @click="deleteTournament" v-if="selectedTournament.id != null">Delete</button>
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
            <textarea type="text" class="form-control" id="desc" v-model="selectedTournament.desc"></textarea>
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
    
          <!-- Games Per Day -->
          <div class="mb-3 col-md-6">
            <label for="gamesPerDay" class="form-label">Games Per Day</label>
            <input
              type="number"
              class="form-control"
              id="gamesPerDay"
              v-model="selectedTournament.gamesPerDay"
            />
          </div>
    
          <!-- People Limit (only for BR) -->
          <div class="mb-3 col-md-6" v-if="selectedTournament.gameMode === 'Battle Royale'">
            <label for="peopleLimit" class="form-label">People Limit</label>
            <input
              type="number"
              class="form-control"
              id="peopleLimit"
              v-model="selectedTournament.peopleLimit"
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


export default {
  name: "AdminView",
  components: { DatePicker, Modal, TournamentTable, bsModal },
  data() {
    return {
      selectedTournament: {
        id:null,
        gameMode: "",
        name: "",
        desc: "",
        startDate: null,
        endDate: null,
        gamesPerDay: "",
        peopleLimit: "",
      },
      activeTournaments:[
        {
            id:123,
            gameMode: "Battle Royale",
            name: "Tournament 1",
            desc: " this is a tournament ...",
            startDate: new Date(),
            endDate: new Date(),
            gamesPerDay: 7,
            peopleLimit: null,
        }
      ],
      upcomingTournaments:[
        {
            id:123,
            gameMode: "Battle Royale",
            name: "Tournament 1",
            desc: " this is a tournament ...",
            startDate: new Date(),
            endDate: new Date(),
            gamesPerDay: 7,
            peopleLimit: 20,
        }
      ],
      isLargeScreen: window.innerWidth >= 992,
      minDate: new Date(),
    };
  },
  methods: {
      createTournament() {
        if (!this.validateInput()){
          this.showErrorAlert('Please fill in all inputs');
          return;
        } 
        if(!this.validateDates()) {
            this.showErrorAlert('Please verfiy your date.');
            return;
        }
      this.hideAdminModal()
      this.bookSuccess("Tournament Created Successfully")
    },
    updateTournament() {
        if (!this.validateInput()){
          this.showErrorAlert('Please fill in all inputs');
          return;
        } 
        if(!this.validateDates()) {
            this.showErrorAlert('Please verfiy your date.');
            return;
        }
      this.hideAdminModal()
      this.bookSuccess("Tournament Updated Successfully")
    },
    deleteTournament(){
        Swal.fire({
                title: "Confirm Delete?",
                icon: "warning",
                showCancelButton: true,
                cancelButtonColor: "#DDDDDD",
                confirmButtonColor: "#FA9021",
                confirmButtonText: "Delete"
                }).then((result) => {
                    if (result.isConfirmed) {
                            Swal.fire({
                                title: "Deleted!",
                                text: "Your booking has been deleted.",
                                icon: "success",
                                timer: 1500
                            });
                            this.hideAdminModal()
                    }
            });
    },
    resetAdminModal() {
    //   console.log('clear')
      this.selectedTournament={
            id:null,
            gameMode: "",
            name: "",
            desc: "",
            startDate: null,
            endDate: null,
            gamesPerDay: "",
            peopleLimit: "",
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
            if (key === 'id') {
                return true;
            }
            // Skip 'peopleLimit' check if gameMode is "clan war"
            if (key === 'peopleLimit' && this.selectedTournament.gameMode === 'Clan War') {
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
            position: "top-end",
            icon: "error",
            title: "Invalid Input",
            text: errorMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    bookSuccess(successMessage){
        Swal.fire({
            position: "center",
            icon: "success",
            title: "Booked!",
            text: successMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
  },
  mounted() {
    const myModalEl = document.getElementById("adminModal");
    // myModalEl.addEventListener("show.bs.modal", (event) => {});
    //make sure that when it is hidden, clear all value
    myModalEl.addEventListener("hidden.bs.modal", (event) => {
      this.resetAdminModal();
    });
  },
  async created() {
      window.addEventListener("resize", this.checkScreenSize);
    },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },

};
</script>

<style>

</style>
