<!-- for this modal, i probably need to do a deep watch to watch the tournament info passed so i can change the start time and end time dynamically according to the tournament period -->

<!-- 
to use the booking modal, we need to pass in tournament which includes tournament id, name, description, image and gameMode

tournament:{
        id: 
        name: 
        description: 
        image: 
        gameMode:
      }

this is the button to trigger the booking modal, which is the same as modal

<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#123">
    Launch modal
</button> 

this is what is needed to pass in for booking modal component 
<BookingModal modalID="123" :tournament="tempTournament" prevModalID="123"/> 

-->

<template>
    <Modal
      header="Tournament Booking" 
      :modalID="modalID" 
      :showFooter="true" 
      :action="confirmBooking" 
      :actionName="isEditing ? 'Update' : 'Book'"
      :prevModalID="prevModalID"
    >
        <div class="mb-3 d-flex justify-content-between">
            <div class="fw-semibold">Booking for {{ selectedTournament.name }}</div>
            <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{formmattedMode}}</div>
        </div>
        <div v-for="(booking,index) in bookings" :key="index" class="mb-3 w-100 h-100 row container d-flex align-items-end">
            <div class="col-12 col-sm-5">
                Date:
                <DatePicker v-model="booking.date" fluid :minDate="minDate" :maxDate="maxDate" dateFormat="dd/mm/yy" showIcon/>
            </div>
            <div class="col">
                Start Time:
                <div class="d-flex align-items-center">
                    <select v-model="booking.startTime" class="form-select" @change="validateTime(index)">
                        <option v-for="hour in hours" :key="hour" :value="hour" >{{ hour }}</option>
                    </select>

                </div>
            </div>
            <div class="col">
                End Time:
                <select v-model="booking.endTime" class="form-select" @change="validateTime(index)">
                    <option v-for="hour in hours" :key="hour" :value="hour">{{ hour }}</option>
                </select>
            </div>
            <div class="col-1 m-0 p-0 d-flex align-items-end">
                <button class="btn btn-danger p-2 mb-1" @click="deleteBooking(index)"> cancel</button>
            </div>
        </div>
        <button class="btn btn-secondary" v-if="!isEditing" @click="addMoreBooking">add booking</button>

    </Modal>
  </template>
  
<script>
import { useUserStore } from '@/stores/store';
import Modal from './Modal.vue';
import DatePicker from 'primevue/datepicker';
import Swal from 'sweetalert2'
import { Modal as bsModal } from 'bootstrap';
import axios from '@/utils/axiosInstance';

  
export default {
name: "BookingModal",
props: ['modalID', 'tournament','prevModalID','isEditing', 'existingAvailability'],
components: { Modal, DatePicker },
data() {
    return {
    bookings:[
        {
            date: null,
            startTime: null,
            endTime: null,
        }
    ],
    selectedTournament: {
        tournament_id: null,
        name: null,
        description: null,
        startDate: null,
        endDate: null,
        playerCapacity: null,
        status: null,
        gameMode: null,
        gameList: null,
        adminId: null,
        playerIds: null
    },
    minDate: new Date(),
    maxDate: new Date(),
    hours: [
        "12 AM","1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", 
        "7 AM", "8 AM", "9 AM", "10 AM", "11 AM",
        "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", 
        "7 PM", "8 PM", "9 PM", "10 PM", "11 PM", "12 AM next day"
        ],
    };
},
computed:{
    formmattedMode(){
        if(this.selectedTournament.gameMode == "BATTLE_ROYALE") return "Battle Royale";
        if(this.selectedTournament.gameMode == "CLANWAR") return "Clan War";
    },
},

setup() {
    const userStore = useUserStore();
    return {userStore}
  },
mounted() {
    //this will retrieve the tournament period and set the min and max
    const myModalEl = document.getElementById(this.modalID)
    myModalEl.addEventListener('show.bs.modal', event => {
        this.setDateRange(this.tournament);
        this.selectedTournament = this.tournament;
        if (this.isEditing) {
            this.selectedTournament = this.tournament.tournament;
            this.setDateRange(this.tournament.tournament);
            
            // Parse the tournament start and end times into dates
            const startDate = new Date(this.tournament.startTime);
            const endDate = new Date(this.tournament.endTime);

            // Set the `bookings[0]` fields with the tournament details
            this.bookings[0].date = startDate.toISOString().split('T')[0]; // Set the date part only
            this.bookings[0].startTime = this.getCurrentHourLabel(startDate); // Format start time
            this.bookings[0].endTime = this.getCurrentHourLabel(endDate); // Format end time
        }
    })
    //make sure that when it is hidden, clear all value
    myModalEl.addEventListener('hidden.bs.modal', event => {
        this.resetBookingData()
    })
},
methods: {
    async confirmBooking() {
        if(this.validateBookings()){
            //if user is making an edit
            if(this.isEditing){
                console.log("update successfully")
            }
            else{ // if user is making a new booking
                if(this.selectedTournament.gameMode === 'CLANWAR' && this.userStore.user.clanRole==='ROLE_PLAYER'){
                    this.showErrorAlert("Only the Clan Admin can book Clan War", "Access denied");
                    return;
                }
                // add player availability to backend
                try{
                    for (let booking of this.bookings) {
                        // Create formatted start and end times as ISO strings
                        const startDateTime = new Date(booking.date);
                        const endDateTime = new Date(booking.date);

                        // Convert selected times to 24-hour format and set to booking date
                        startDateTime.setHours(this.hours.indexOf(booking.startTime));
                        endDateTime.setHours(this.hours.indexOf(booking.endTime));

                        let formattedBooking = {
                            playerId: this.userStore.user.id,
                            tournamentId: this.selectedTournament.tournament_id,
                            startTime: startDateTime.toISOString(),
                            endTime: endDateTime.toISOString(),
                            available: true
                        };

                        // Send formattedBooking to backend
                        const response = await axios.post(`/matchmaking/api/playersAvailability`, formattedBooking);
                    }
                    this.bookSuccess('You have succesfully made the bookings');
                    console.log("now fetching the things")
                } catch (error) {
                    console.error('Error adding player\'s availability:', error);
                    throw error;
                }

                const existingModal = bsModal.getInstance(document.getElementById(this.modalID));
                existingModal.hide();
            }
            this.$emit("fetchPlayerAvail");
        }
    },
    // this function is used for both editing and making new booking
    setDateRange(tournament) {
        const today = new Date();
        const startDate = new Date(tournament.startDate);
        const endDate = new Date(tournament.endDate);
        
        // Set the minimum date to today or the tournament start date, in UTC
        this.minDate = today > startDate 
            ? new Date(Date.UTC(today.getUTCFullYear(), today.getUTCMonth(), today.getUTCDate()))
            : new Date(Date.UTC(startDate.getUTCFullYear(), startDate.getUTCMonth(), startDate.getUTCDate()));

        // Set the max date to the tournament end date, in UTC
        this.maxDate = new Date(Date.UTC(endDate.getUTCFullYear(), endDate.getUTCMonth(), endDate.getUTCDate()));

    },
    addMoreBooking(){
        this.bookings.push({
            date: null,
            startTime: null,
            endTime: null,
        })
    },
    deleteBooking(index){
        if(this.isEditing){
            Swal.fire({
                title: "Confirm Delete?",
                icon: "warning",
                reverseButtons: true, // Swaps the position of confirm and cancel buttons
                showCancelButton: true,
                cancelButtonColor: "#DDDDDD",
                confirmButtonColor: "#FA9021",
                confirmButtonText: "Yes, delete it!"
                }).then((result) => {
                    if (result.isConfirmed) {
                            Swal.fire({
                                title: "Deleted!",
                                text: "Your booking has been deleted.",
                                icon: "success",
                                timer: 1500
                            });
                            // console.log(document.getElementById(this.modalID))
                            const existingModal = bsModal.getInstance(document.getElementById(this.modalID));
                            existingModal.hide();
                            
                    }
            });
        }else{
            this.bookings.splice(index,1)
        }
    },
    //this will check if end time is before start time
    validateTime(index) {
        if( this.bookings[index].startTime==null || this.bookings[index].endTime==null){
            return false;
        }
        const booking = this.bookings[index];
        const startIndex = this.hours.indexOf(booking.startTime);
        const endIndex = this.hours.indexOf(booking.endTime);

        // ensure that the end time is after the start time
        if (startIndex >= endIndex) {
            this.showErrorAlert("End time must be after start time!", "Invalid Input");
            booking.startTime = null; // reset
            booking.endTime = null; // reset
            return false;
        }

        // check for overlaps with other bookings
        return this.checkOverlaps(index);
    },
    //check for overlaps in timing on the day day (client side validation)
    checkOverlaps(currentIndex) {
        const currentBooking = this.bookings[currentIndex];
        const currentStartTime = this.hours.indexOf(currentBooking.startTime);
        const currentEndTime = this.hours.indexOf(currentBooking.endTime);

        for (let i = 0; i < this.bookings.length; i++) {
            if (i === currentIndex) continue;

            const otherBooking = this.bookings[i];
            const currentDate = new Date(currentBooking.date).getTime();
            const otherDate = new Date(otherBooking.date).getTime();
        
            if (currentDate === otherDate) {
                const otherStartTime = this.hours.indexOf(otherBooking.startTime);
                const otherEndTime = this.hours.indexOf(otherBooking.endTime);

                // Check if time slots overlap
                if (currentStartTime<otherEndTime && otherStartTime<currentEndTime) {
                    this.showErrorAlert("Overlapping bookings detected!","Invalid Input");
                    currentBooking.startTime = null;
                    currentBooking.endTime = null;
                    return false;
                }
            }
        }
        return true;
    },
    //check input validitiy before making HTTP call
    validateBookings() {
        for (let index=0 ; index < this.bookings.length; index++) {
            let booking = this.bookings[index];
            if (!booking.date || !booking.startTime || !booking.endTime) {
                this.showErrorAlert("Please fill in all booking fields.", "Invalid Input");
                return false;
            }
            if(!this.validateTime(index)){
                return false;
            }
        }
        return true;
    },
    getCurrentHourLabel(date) {
        let hour = date.getHours();
        const isAM = hour < 12 || hour === 24; // Check if it is AM
        let label = "";

        // Adjust hour for 12-hour format
        if (hour === 0) {
            label = "12 AM";
        } else if (hour < 12) {
            label = hour + " AM";
        } else if (hour === 12) {
            label = "12 PM";
        } else if (hour < 24) {
            label = (hour - 12) + " PM";
        } else {
            label = "12 AM next day";
        }

        return label;
    },
    showErrorAlert(errorMessage, title){
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "error",
            title: title,
            text: errorMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    bookSuccess(successMessage){
        Swal.fire({
            toast: true,
            position: "top-end",
            icon: "success",
            title: "Booked!",
            text: successMessage,
            showConfirmButton: false,
            timer: 1500
        });
    },
    resetBookingData() {
        this.bookings = [
            {
                date: null,
                startTime: null,
                endTime: null,
            }
        ];
        this.minDate = new Date();
        this.maxDate = new Date();
    },
}
};
  </script>
  
  <style scoped>
  /* Add custom styles here */
  </style>