<template>
  <div class="d-flex justify-content-around align-items-center min-vh-100">
    <div
    data-aos="fade-up"
    data-aos-offset="500"
    data-aos-duration="500"
    >
      <div v-for="(group, index) in buttonGroups" :key="index" class="d-flex justify-content-between">
        <div v-for="(route, page) in group" :key="page">
          <button 
            class="btn btn-secondary opacity-75 m-2 hoverBtn" 
            style="width: 200px;"
            @click="this.$router.push(route)">
            <h3 class="fw-semibold text-black m-0 hoverText">{{ page }}</h3>
          </button>
        </div>
      </div>
    </div>
    
    <div></div>

  </div>
</template>

<script>
import { useUserStore } from '@/stores/store';

export default {
  data() {
    return {
      pages: {
        "Battle Royale": "/battleroyale",
        "Clan War": "/clanwar",
        "Booking": "/booking",
        "Clan": "/Clan",
        "Leader Board": "/leaderboard",
        "Profile": "/profile",
      }
    };
  },
  computed: {
    hasNoClan() {
      // Checks if the user has no clan; if true, 'Clan War' should be excluded
      return this.userStore.user.clan == null;
    },
    buttonGroups() {
      // Convert pages object into an array of entries, filtering out 'Clan War' if the user has no clan
      const entries = Object.entries(this.pages).filter(([name, path]) => {
        // Exclude 'Clan War' path if hasNoClan is true
        return !(path === "/clanwar" && this.hasNoClan);
      });

      // Split the remaining entries into groups of 2
      const groups = [];
      for (let i = 0; i < entries.length; i += 2) {
        groups.push(Object.fromEntries(entries.slice(i, i + 2)));
      }
      return groups;
    }
  },
  setup(){
      const userStore = useUserStore();
      return {userStore}
  }
}
</script>

<style scoped>
.hoverBtn:hover {
  background-color: #FA9021 !important;
  opacity: 100 !important;
}
.hoverText:hover{
  color: white !important; /* Change text color as well, if needed */
}
</style>