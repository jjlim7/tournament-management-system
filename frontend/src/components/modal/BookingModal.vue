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
            <div class="fw-semibold">Booking for {{ tournament.name }}</div>
            <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">{{tournament.gameMode}}</div>
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

  
export default {
name: "BookingModal",
props: ['modalID', 'tournament','prevModalID','isEditing'],
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
setup() {
    const userStore = useUserStore();
    return {userStore}
  },
mounted() {
    //this will retrieve the tournament period and set the min and max
    const myModalEl = document.getElementById(this.modalID)
    myModalEl.addEventListener('show.bs.modal', event => {
        this.setDateRange();
        if(this.isEditing){
            //setting the date, start time and end time to existing booking
            this.bookings[0].date = new Date();
            this.bookings[0].startTime = this.getCurrentHourLabel(new Date());
    
            const endTime = new Date();
            endTime.setHours(endTime.getHours() + 1); // Add 1 hour to the current time
            this.bookings[0].endTime = this.getCurrentHourLabel(endTime);
        }
    })
    //make sure that when it is hidden, clear all value
    myModalEl.addEventListener('hidden.bs.modal', event => {
        this.resetBookingData()
    })
},
methods: {
    confirmBooking() {
        // console.log(this.bookings);
        if(this.validateBookings()){
            //if user is making an edit
            if(this.isEditing){
                console.log("update successfully")
            }
            else{ // if user is making a new booking
                if(this.tournament.gameMode === 'Clan War' && this.userStore.user.clanRole==='member'){
                    this.showErrorAlert("Only the Clan Admin can book Clan War");
                    return;
                }
                this.bookSuccess('You have succesfully made the bookings');
                const existingModal = bsModal.getInstance(document.getElementById(this.modalID));
                existingModal.hide();
            }
        }
    },
    //in the future: pass in start date and end date retrieved from backend
    // this function is used for both editing and making new booking
    setDateRange() {
        let today = new Date();
        
        // set the minimum date to today
        this.minDate = new Date(today);

        // calculate end date
        let nextMonth = today.getMonth() + 1;
        let nextYear = today.getFullYear();

        // if it's December, go to January of the next year (end date)
        if (nextMonth > 11) {
            nextMonth = 0;
            nextYear++;
        }

        // set the maximum date
        this.maxDate = new Date(today);
        this.maxDate.setMonth(nextMonth);
        this.maxDate.setFullYear(nextYear);

        // handle cases where next month doesn't have the same day (e.g., February 30th doesn't exist)
        if (this.maxDate.getMonth() !== nextMonth) {
            this.maxDate.setDate(0); // Set to the last day of the previous month
        }
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
            this.showErrorAlert("End time must be after start time!");
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
                    this.showErrorAlert("Overlapping bookings detected!");
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
                this.showErrorAlert("Please fill in all booking fields.");
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