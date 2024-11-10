
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
                <th class="fw-semibold" v-if="isLargeScreen">Games/day</th>
                <th class="fw-semibold" v-if="isLargeScreen">Max no.</th>
                <th class="fw-semibold" v-if="!isActive">Action</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="count in 10" :key="count">
            <th  v-if="isLargeScreen">{{ TournamentList[0].id }}</th>
            <th>{{ TournamentList[0].name }}</th>
            <th>{{ formattedDate(TournamentList[0].startDate) }}</th>
            <th>{{ formattedDate(TournamentList[0].endDate) }}</th>
            <th>{{ TournamentList[0].gameMode }}</th>
            <th v-if="isLargeScreen">{{ TournamentList[0].gamesPerDay }}</th>
            <th v-if="isLargeScreen">{{ TournamentList[0].peopleLimit ?? '-' }}</th>
            <th class="fw-semibold" v-if="!isActive">
                <button class="btn btn-primary fw-semibold text-white" @click="this.$emit('action', TournamentList[0])">Edit</button>
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
