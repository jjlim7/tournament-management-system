<template>
  <div class="mx-auto" style="min-height: 90vh;">
    <!-- showing battle royale ranking -->
     <!-- options to choose which tournament to view -->
      <!-- <div class="d-flex justify-content-between">
        <div class="mb-3 w-100 text-center">
          <label for="tournament-select" class="form-label">Select Battle Royale Tournament:</label>
          <select id="tournament-select" class="form-select w-50 mx-auto" v-model="selectedBRTournament" @change="fetchBRTournamentRankings">
            <option v-for="tournament in BRtournaments" :key="tournament.id" :value="tournament.id">
              {{ tournament.name }}
            </option>
          </select>
        </div>
        <div class="mb-3 w-100 text-center" v-if="this.userStore.user.clan != null">
          <label for="tournament-select" class="form-label">Select Clan War Tournament:</label>
          <select id="tournament-select" class="form-select w-50 mx-auto" v-model="selectedCWTournament" @change="fetchCWTournamentRankings">
            <option v-for="tournament in CWtournaments" :key="tournament.id" :value="tournament.id">
              {{ tournament.name }}
            </option>
          </select>
        </div>
      </div> -->
    <!-- leaderboard section -->
     <div class="d-flex align-items-start w-100 justify-content-between mx-auto m-3 row">

       <LeaderBoardTable 
         data-aos="fade-right"
         data-aos-offset="500"
         data-aos-duration="500"
         class="px-3 col-12 col-md"
         :allranking="BRranking" 
         :myranking="myBRrank" 
         gameMode="Battle Royale" />
   
       <LeaderBoardTable 
         data-aos="fade-left"
         data-aos-offset="500"
         data-aos-duration="500" 
         class="px-3 col-12 col-md"
         v-if="this.userStore.user.clan != null" 
         :allranking="CWranking" 
         :myranking="myCWrank" 
         gameMode="Clan War" />
     </div>
  </div>
</template>

<script>
import LeaderBoardTable from '@/components/LeaderBoardTable/LeaderBoardTable.vue';
import { useUserStore } from '@/stores/store';
import axios from '@/utils/axiosInstance';

export default {
  name:"LeaderBoardView",
  components:{LeaderBoardTable,},
  data(){
    return{
      headers:['No.','Name','Rank','Elo'],
      BRranking:[],
      CWranking:[],
      myBRrank:{},
      myCWrank:{},
      selectedBRTournament: null,
      selectedCWTournament: null,
      BRtournaments: [],
      CWtournaments: [],
    }
  },
  methods:{
    fetchTournament() {
      axios.get('/tournament/api/tournaments')
        .then((response) => {
          console.log(response);

          this.BRtournaments = [];
          this.CWtournaments = [];

          let activeBRTournaments = [];
          let pastBRTournament = [];

          let activeCWTournaments = [];
          let pastCWTournament = [];

          if (response.status === 404) {
            return;
          }

          const allTournaments = response.data;
          console.log(allTournaments)
          const currentDateTime = new Date();

          for (let i = 0; i < allTournaments.length; i++) {
            const tournament = allTournaments[i];
            const startDate = new Date(tournament.startDate);
            const endDate = new Date(tournament.endDate);
            let formattedTournament = { ...tournament, startDate: startDate, endDate: endDate };

            if (tournament.gameMode == "BATTLE_ROYALE") {
              if (startDate <= currentDateTime && endDate >= currentDateTime) {
                // Tournament is currently active
                activeBRTournaments.push(formattedTournament);
              } else if (endDate < currentDateTime) {
                // Tournament is upcoming
                pastBRTournament.push(formattedTournament);
              }
            }

            if (tournament.gameMode == "CLANWAR") {
              if (startDate <= currentDateTime && endDate >= currentDateTime) {
                // Tournament is currently active
                activeCWTournaments.push(formattedTournament);
              } else if (endDate < currentDateTime) {
                // Tournament is upcoming
                pastCWTournament.push(formattedTournament);
              }
            }
          }

          this.BRtournaments = activeBRTournaments.concat(pastBRTournament);
          console.log(this.BRtournaments)
          
          this.CWtournaments = activeCWTournaments.concat(pastCWTournament);
          console.log(this.CWtournaments)

          this.selectedBRTournament = this.BRtournaments[0];
          this.selectedCWTournament = this.CWtournaments[0];
        })
        .catch((error) => {
          console.error('Error fetching tournaments:', error);
          throw error;
        });
    },
    fetchBRTournamentRankings() {
      // // Fetch player elo for a tournament
      // axios.get(`/elo-ranking/api/elo-ranking/player/${this.userStore.user.id}/tournament/${this.selectedBRTournament.tournament_id}`)
      //   .then((response) => {
      //     if (response.status === 200) {
      //       const data = response.data.data;
      //       this.myBRrank = {
      //         "name": this.userStore.name,
      //         "rank": data.rankThreshold.rank,
      //         "elo": data.meanSkillEstimate,
      //       };
      //     }
      //   });

      // Get ranking of all players for a BR tournament
      console.log(this.selectedBRTournament)
      // if(this.selectedBRTournament == null) return;
      // axios.get(`/elo-ranking/api/elo-ranking/player/tournament/${this.selectedBRTournament.tournament_id}`)
      axios.get(`/elo-ranking/api/elo-ranking/player/tournament/16`)
        .then((response) => {
          console.log(response.data)
          if (response.status === 200) {
            const data = response.data.playerEloRanks;
            const rankingPromises = data.map((player) => {
              return axios.get(`/clanuser/api/users/${player.playerId}`).then((userResponse) => {
                return {
                  'id' : player.playerId,
                  "name": userResponse.data.name,
                  "rank": player.rankThreshold.rank,
                  "elo": player.meanSkillEstimate.toFixed(0),
                };
              });
            });

            // Wait for all user data to be fetched
            Promise.all(rankingPromises).then((rankings) => {
              this.BRranking = rankings;

              // Sort by elo in descending order
              this.BRranking.sort((a, b) => b.elo - a.elo);

              // Insert the ranking number
              this.BRranking = this.BRranking.map((player, index) => ({
                ...player,
                num: `#${index + 1}`,
              }));
              ///// start of hardcoding ///////
              this.BRranking.push({
                'id' : this.userStore.user.id,
                "name": this.userStore.user.name,
                "rank": this.userStore.user.rank || "UNRANKED",
                "elo": this.userStore.user.currentElo.toFixed(0),
                'num': `# ${this.BRranking.length+1}`
              })
              this.myBRrank = this.BRranking[this.BRranking.length-1]
              ///// end of hardcoding ///////
            });
          }
        });
        

    },

    fetchCWTournamentRankings() {
      // Get ranking of all players for a CW tournament
      if(this.userStore.user.clan == null) return;
      console.log(this.selectedCWTournament)
      if(this.selectedCWTournament == null) return;

      axios.get(`/elo-ranking/api/elo-ranking/clan/tournament/${this.selectedCWTournament.tournament_id}`)
        .then((response) => {
          if (response.status === 200) {
            const data = response.data.clanEloRanks;
            const rankingPromises = data.map((clan) => {
              return axios.get(`/clanuser/api/clans/${clan.clanId}`).then((clanResponse) => {
                return {
                  "id": clanResponse.clanId,
                  "name": clanResponse.data.clanName,
                  "rank": clan.rankThreshold.rank,
                  "elo": clan.meanSkillEstimate,
                };
              });
            });

            // Wait for all user data to be fetched
            Promise.all(rankingPromises).then((rankings) => {
              this.CWranking = rankings;

              // Sort by elo in descending order
              this.CWranking.sort((a, b) => b.elo - a.elo);

              // Insert the ranking number
              this.CWranking = this.CWranking.map((player, index) => ({
                ...player,
                num: `#${index + 1}`,
              }));
              // get my elo ranking
              for(let element in this.CWranking){
               if (element.id == this.userStore.user.clan.clanId){
                 this.CWranking = element
                 break;
               }
             }
            });
          }
        });
    },

  },
  mounted(){
    this.fetchTournament();
    this.fetchBRTournamentRankings();
    this.fetchCWTournamentRankings();
    console.log(this.userStore.user.clan);
  },
  setup() {
    const userStore = useUserStore();
    return {userStore}
  },
}
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
  vertical-align: middle
}

.scrollable-table tbody {
  display: block;
  max-height: 70vh; /*  change this accordingly */
  overflow-y: auto;
}

.scrollable-table thead, .scrollable-table tbody tr {
  display: table;
  width: 100%;
  table-layout: fixed;
}

</style>
