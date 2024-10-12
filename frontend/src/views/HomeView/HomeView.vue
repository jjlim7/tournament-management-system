<template>
  <div class="d-flex justify-content-around align-items-center min-vh-100">
    <div>
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
    buttonGroups() {
      const entries = Object.entries(this.pages);
      // Split entries into groups of 2 buttons per div
      const groups = [];
      for (let i = 0; i < entries.length; i += 2) {
        groups.push(Object.fromEntries(entries.slice(i, i + 2)));
      }
      return groups;
    }
  }
};
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