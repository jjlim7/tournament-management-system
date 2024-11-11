<template>
  <div>
    <!-- countdown  at header-->
    <div v-if="nextMatch != null" class="mx-auto p-4 text-center backgroundColour rounded-bottom-5 align-content-center"
      style="max-width: 700px; max-height: 80px;">
      <div class="fw-semibold">Next Match: {{ formattedGameDate }}</div>
      <div class="fw-semibold">Countdown: {{ countdown.days }}d {{ countdown.hours }}h {{ countdown.minutes }}m {{
        countdown.seconds }}s</div>
    </div>
    <!-- content -->
    <div class="d-flex justify-content-between p-4 mx-5 row">
      <!-- left side -->
      <div class="d-flex flex-column justify-content-around ml-5 col-md-12 col-lg-6">

        <!-- current tournament -->
        <div data-aos="fade-right" data-aos-offset="500" data-aos-duration="500">
          <span class="fw-semibold py-1">Current Tournament</span>
          <BlurredBGCard v-if="currentTournament == null">
            <div class="text-center">No Active Tournament</div>
          </BlurredBGCard>
          <BlurredBGCard v-if="currentTournament" :style="{'background-image': 'url(' + currentTournament.image + ')'}" class="imageProperties text-center mb-2 mt-1">
            <div class=" rounded-4 p-2" style="background-color: rgba(0, 0, 0, 0.4);">
              <h5 class="fw-semibold"> {{ currentTournament.name }} </h5>
              <div style="max-height: 70px;" class="overflow-y-hidden text-wrap">{{ currentTournament.description }}
              </div>
            </div>
          </BlurredBGCard>
        </div>

        <!-- rank progress tournament -->
        <div data-aos="fade-right" data-aos-offset="500" data-aos-duration="600">
          <span class="fw-semibold py-1">Rank Progress</span>
          <BlurredBGCard class="mb-2 mt-1">
            <RankProgress :rank="this.userStore.user.rank" :currentElo="this.userStore.user.currentElo"
              :lowerLimit="this.userStore.user.eloLowerlimit" :upperLimit="this.userStore.user.eloUpperlimit"
              gameMode="Battle Royale" class="p-2" />
          </BlurredBGCard>
        </div>

        <!-- other tournament carousel -->
        <div data-aos="fade-right" data-aos-offset="500" data-aos-duration="700">
          <span class="fw-semibold py-1">Upcoming Battle Royale Tournament</span>
          <BlurredBGCard v-if="upcomingTournaments.length == 0">
            <div class="text-center">No Upcoming Tournament</div>
          </BlurredBGCard>

          <BlurredBGCard v-if='upcomingTournaments.length != 0' class="mt-1">
            <div id="battleRoyalupcomingTournament" class="carousel slide" data-bs-ride="carousel"
              data-bs-pause="hover">
              <!-- indicator for each slide -->
              <div class="carousel-indicators">
                <button v-for="(tournament, index) in upcomingTournaments" :key="index" type="button"
                  data-bs-target="#battleRoyalupcomingTournament" :data-bs-slide-to="index"
                  :class="{ active: index == 0 }"></button>
              </div>
              <div class="carousel-inner rounded-4">
                <!-- list of carousel items -->
                <div v-for="(tournament, index) in upcomingTournaments" :key="index"
                  :class="{ 'carousel-item': true, 'active': index === 0 }" style="position: relative; cursor: pointer;"
                  @click="selectUpcomingTournament(tournament)" data-bs-interval="3000">
                  <img :src="tournament.image" class="img-fluid" alt="...">
                  <div
                    class="position-absolute top-0 start-0 w-100 h-100 bg-dark bg-opacity-50 text-center rounded d-flex flex-column justify-content-center">
                    <h5 class="fw-semibold">{{ tournament.name }}</h5>
                    <p style="max-height: 70px;" class="overflow-y-hidden text-wrap px-5"> {{ tournament.description }}
                    </p>
                  </div>
                </div>
              </div>

              <button class="carousel-control-prev" type="button" data-bs-target="#battleRoyalupcomingTournament"
                data-bs-slide="prev">
                <span class="carousel-control-prev-icon"></span>
              </button>
              <button class="carousel-control-next" type="button" data-bs-target="#battleRoyalupcomingTournament"
                data-bs-slide="next">
                <span class="carousel-control-next-icon"></span>
              </button>
            </div>
          </BlurredBGCard>
        </div>
      </div>

      <!-- right side -->
      <!-- other tournament details -->
      <div data-aos="fade-left" data-aos-offset="500" data-aos-duration="500"
        class="bg-light rounded-4 position-relative col-md-12 col-lg-6 p-0 d-flex flex-column"
        v-if="selectedUpcomingTournament != '' && isLargeScreen">
        <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4 imgStyle" alt="...">
        <button class="btn btn-close position-absolute top-0 end-0 m-2 btnStyle"
          @click="selectedUpcomingTournament = ''"></button>
        <div class="text-black bg-light p-3 pb-0">
          <div class="mb-3 d-flex justify-content-between">
            <div class="fw-semibold fs-5"> {{ selectedUpcomingTournament.name }}</div>
            <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">
              {{ formmattedMode(selectedUpcomingTournament.gameMode) }}</div>
          </div>
          <p class="overflow-y-scroll m-0 shadow-sm" style="max-height: 180px;">{{
            selectedUpcomingTournament.description }}</p>
          <div class="text-black fw-semibold">
            <div class="fw-semibold">Start Date: {{ formattedSelectedTournamentStartTime }}</div>
            <div class="fw-semibold">End Date: {{ formattedSelectedTournamentEndTime }}</div>
          </div>
        </div>
        <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
          <button class="fw-semibold mx-auto text-white btn btn-primary w-50" @click="showModal">Book</button>
        </div>
      </div>
    </div>


    <!-- Modals -->
    <!-- modal to show details when screen is small -->
    <Modal modalID="upcoming" :showFooter="false" header="Upcoming Tournament">
      <img :src="selectedUpcomingTournament.image" class="w-100 img-fluid rounded-top-4" alt="...">
      <div class="text-black bg-light p-3 pb-0">
        <div class="mb-3 d-flex justify-content-between">
          <div class="fw-semibold fs-5"> {{ selectedUpcomingTournament.name }}</div>
          <div class="text-black border border-primary border-2 rounded-5 fw-semibold px-1 bg-secondary">
            {{ formmattedMode(selectedUpcomingTournament.gameMode) }}</div>
        </div>
        <p class="overflow-y-scroll m-0" style="max-height: 180px;">{{ selectedUpcomingTournament.description }}</p>
        <div class="text-black">
          <p class="fw-semibold">Start Date: {{ formattedSelectedTournamentStartTime }}</p>
          <p class="fw-semibold">End Date: {{ formattedSelectedTournamentEndTime }}</p>
        </div>
      </div>
      <div class="rounded-bottom-4 bg-light p-2 d-flex mt-auto">
        <button class="fw-semibold mx-auto text-white btn btn-primary w-50" data-bs-target="#booking"
          data-bs-toggle="modal">Book</button>
      </div>
    </Modal>

    <!-- modal to show booking for upcoming tournament -->
    <BookingModal :prevModalID="!isLargeScreen ? 'upcoming' : ''" modalID="booking" :isEditing="false"
      :tournament="selectedUpcomingTournament" />


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
import Swal from 'sweetalert2';

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
      nextMatch: null,
      currentTournament: null,
      upcomingTournaments: [],
      selectedUpcomingTournament: '',
      upcomingGames: []
    };
  },
  computed: {
    formattedSelectedTournamentStartTime() {
      if (!this.selectedUpcomingTournament.startDate) return '';
      return this.formatDate(new Date(this.selectedUpcomingTournament.startDate));
    },
    formattedSelectedTournamentEndTime() {
      if (!this.selectedUpcomingTournament.endDate) return '';
      return this.formatDate(new Date(this.selectedUpcomingTournament.endDate));
    },
    formattedGameDate() {
      const date = new Date(this.nextMatch.startTime);

      // Format date as dd/mm/yyyy
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
      const year = date.getFullYear();

      // Format time as hh:mm AM/PM
      let hours = date.getHours();
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const ampm = hours >= 12 ? 'PM' : 'AM';
      hours = hours % 12 || 12; // Convert 24-hour to 12-hour format

      // Combine date and time
      const formattedDate = `${day}/${month}/${year}`;
      const formattedTime = `${hours}:${minutes} ${ampm}`;

      return `${formattedDate} ${formattedTime}`;
    },
  },
  methods: {
    formatDate(date) {
      if (!date) return ''; // Handle null or undefined dates
      const day = String(date.getUTCDate()).padStart(2, '0');
      const month = String(date.getUTCMonth() + 1).padStart(2, '0'); // Month is zero-based
      const year = date.getUTCFullYear();
      return `${day}/${month}/${year}`;
    },
    formmattedMode(mode) {
      if (mode == "BATTLE_ROYALE") return "Battle Royale";
      if (mode == "CLANWAR") return "Clan War";
    },
    startCountdown() {
      if (this.nextMatch == null) return
      const targetTime = new Date(this.nextMatch?.startTime).getTime();
      let modalShown = false;
      console.log(this.nextMatch)

      const req = {
        "playerIds": this.nextMatch?.playerIds,
        "tournamentId": this.nextMatch?.tournamentId,
        "gameId": this.nextMatch?.gameId,
      };
      const gameId = this.nextMatch?.gameId;

      this.intervalId = setInterval(() => {
        const now = new Date().getTime();
        const timeLeft = targetTime - now;

        if (timeLeft < 0) {
          clearInterval(this.intervalId);
          this.countdown = { days: 0, hours: 0, minutes: 0, seconds: 0 };

          if (!modalShown) {  // Only show the modal if it hasn't been shown already
            modalShown = true;  // Set the flag to prevent showing the modal again
            Swal.fire({
              title: "Start Game?",
              reverseButtons: true,
              showCancelButton: true,
              cancelButtonColor: "#DDDDDD",
              confirmButtonColor: "#FA9021",
              confirmButtonText: "Start!"
            }).then(async (result) => {
              if (result.isConfirmed) {
                console.log(req)
                let res = await axios.post('/elo-ranking/api/simulate/battle-royale', req);

                // Open the game link in a new tab
                window.open(`http://13.228.120.122:9000?gameId=${gameId}`, '_blank');

                Swal.fire({
                  title: "Started!",
                  text: "Your game has started.",
                  icon: "success",
                  timer: 1500
                });

                // Assuming res.data contains an array of players with placementId
                const players = res.data?.result;
                const winner = players.find(player => player.placement == 1);

                console.log(winner);

                // TODO: Add Modal for Winner
              }
            });
          }
          //fetch games
          this.fetchGames();
          // count down if there is a upcoming game
          this.startCountdown();

        } else {
          this.countdown.days = Math.floor(timeLeft / (1000 * 60 * 60 * 24));
          this.countdown.hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
          this.countdown.minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
          this.countdown.seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
        }
      }, 1000);
    },
    async simulateGame() {
      const response = await axios.post(`/elo-ranking/api/simulate/battle-royale`);
    },
    checkScreenSize() {
      this.isLargeScreen = window.innerWidth >= 992;
    },
    selectUpcomingTournament(tournament) {
      this.selectedUpcomingTournament = tournament;

      this.$nextTick(() => {
        if (!this.isLargeScreen) {
          const modalID = 'upcoming';
          const tournamentModal = new bsModal(document.getElementById(modalID));
          tournamentModal.show();
        }
      });
    },
    showModal() {
      const modalID = 'booking';
      const tournamentModal = new bsModal(document.getElementById(modalID));
      tournamentModal.show();
    },
    fetchTournament() {
      axios.get('/tournament/api/tournaments')
        .then((response) => {
          // console.log(response);

          this.upcomingTournaments = [];

          if (response.status === 404) {
            return;
          }

          const allTournaments = response.data;
          const currentDateTime = new Date();
          const n = tournamentImage.length;

          for (let i = 0; i < allTournaments.length; i++) {
            const tournament = allTournaments[i];
            if (tournament.gameMode !== "BATTLE_ROYALE") {
              continue;
            }

            const startDate = new Date(tournament.startDate);
            const endDate = new Date(tournament.endDate);

            let formattedTournament = {
              ...tournament,
              startDate: startDate,
              endDate: endDate,
              image: tournamentImage[i % n]
            };

            if (startDate <= currentDateTime && endDate >= currentDateTime) {
              // Tournament is currently active
              this.currentTournament = formattedTournament;
            } else if (startDate > currentDateTime) {
              // Tournament is upcoming
              this.upcomingTournaments.push(formattedTournament);
            }
          }

          // console.log(this.upcomingTournaments);
          // console.log(this.currentTournament);
          // Fetch Elo rank if the current tournament exists
          if (this.currentTournament) {
            this.fetchEloRank();
          }
        })
        .catch((error) => {
          console.error('Error fetching tournaments:', error);
        });
    },

    fetchEloRank() {
      if (this.currentTournament == null) return;

      axios.get(`/elo-ranking/api/elo-ranking/player/${this.userStore.user.id}/tournament/${this.currentTournament.tournament_id}`)
        .then((response) => {
          if (response.status === 200) {
            const data = response.data.data;
            this.userStore.setEloLimits(data.rankThreshold.minRating, data.meanSkillEstimate, data.rankThreshold.maxRating);
          }
        })
        .catch((error) => {
          console.error('Error fetching player\'s elo rank:', error);
        });
    },
    fetchGames() {
      this.upcomingGames = [];
      axios.get(`/matchmaking/api/games/upcoming/player?playerId=${this.userStore.user.id}`)
        .then((response) => {
          // Ensure a successful response
          console.log(response)
          if (response.status === 200) {
            const gamesArr = response.data;

            // Sort the games array by startTime (earliest first)
            gamesArr.sort((a, b) => new Date(a.startTime) - new Date(b.startTime));

            // Assign the sorted games array to upcomingGames
            this.upcomingGames = gamesArr;

            // Assign the first game as the nextMatch, if games exist
            if (gamesArr.length > 0) {
              this.nextMatch = gamesArr[0];
            } else {
              this.nextMatch = null; // No games available
            }
          }
        })
        .catch((error) => {
          console.error('Error fetching upcoming games:', error);
        });
    }

  },
  setup() {
    const userStore = useUserStore();
    return { userStore }
  },
  async created() {
    window.addEventListener("resize", this.checkScreenSize);
  },
  beforeUnmount() {
    clearInterval(this.intervalId); // Clear the interval when the component is destroyed
  },
  mounted() {
    this.fetchTournament();
    //fetch games
    this.fetchGames();
    // count down if there is a upcoming game
    this.startCountdown();
  },
  destroyed() {
    window.removeEventListener("resize", this.checkScreenSize);
  },
};
</script>

<style scoped>
.backgroundColour {
  background: linear-gradient(to top,
      rgba(248, 147, 38, 1) 70%,
      rgba(248, 147, 38, 0.7),
      rgba(248, 147, 38, 0.2));
}

.imageProperties {
  background-size: cover;
}

.btnStyle {
  background-color: white;
  opacity: 80%;
}

.carousel-item img {
  width: 100%;
  height: 300px;
  object-fit: cover;
}

.imgStyle {
  widows: 100%;
  max-height: 350px;
  object-fit: content;
}
</style>