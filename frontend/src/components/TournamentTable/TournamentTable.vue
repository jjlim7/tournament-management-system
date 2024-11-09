
<template>
  <div class="mb-3">
    <div class="fs-5 fw-semibold">{{isActive ? "Active Tournaments" : "Upcoming Tournames"}}</div>
    <BlurredBGCard class="mt-1">
        <table class="table align-middle scrollable-table" v-if="TournamentList.length>0">
        <thead>
            <tr>
                <th class="fw-semibold"  v-if="isLargeScreen">ID</th>
                <th class="fw-semibold">Name</th>
                <th class="fw-semibold">Start</th>
                <th class="fw-semibold">End</th>
                <th class="fw-semibold">Mode</th>
                <th class="fw-semibold" v-if="isLargeScreen">Max no.</th>
                <th class="fw-semibold" v-if="!isActive">Action</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="tournament in sortedTournamentList" :key="tournament.tournament_id">
            <th  v-if="isLargeScreen">{{ tournament.tournament_id }}</th>
            <th>{{ tournament.name }}</th>
            <th>{{ formattedDate(tournament.startDate) }}</th>
            <th>{{ formattedDate(tournament.endDate) }}</th>
            <th>{{ formmattedMode(tournament.gameMode) }}</th>
            <th v-if="isLargeScreen">{{ tournament.playerCapacity ?? '-' }}</th>
            <th class="fw-semibold" v-if="!isActive">
                <button class="btn btn-primary fw-semibold text-white" @click="this.$emit('action', tournament)">Edit</button>
            </th>
            </tr>
        </tbody>
        </table>
        <div v-else class="p-3"> There is no {{ isActive ? 'active' : 'upcoming' }} tournaments</div>
    </BlurredBGCard>
  </div>
</template>

<script>
import BlurredBGCard from '@/components/Cards/BlurredBGCard.vue';

export default {
  name: "TournamentTable",
  props:['TournamentList', 'isActive','isLargeScreen','action'],
  components:{BlurredBGCard},
  data() {
    return {
    
    };
  },
  methods:{
    formattedDate(date){
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
        const year = date.getFullYear();
        return this.isLargeScreen ? `${day}/${month}/${year}` : `${day}/${month}`;
    },
    formmattedMode(mode){
      if(mode == "BATTLE_ROYALE") return "Battle Royale";
      if(mode == "CLANWAR") return "Clan War";
    },
  },
  computed:{
    sortedTournamentList(){
      return this.TournamentList.sort((a, b) => a.startDate - b.startDate);
    }
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
  vertical-align: middle
}

.scrollable-table tbody {
  display: block;
  height: 25vh;
  overflow-y: auto;
}

.scrollable-table thead, .scrollable-table tbody tr {
  display: table;
  width: 100%;
  table-layout: fixed;
}
</style>
