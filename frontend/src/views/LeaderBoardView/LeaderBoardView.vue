<template>
  <div class="d-flex align-items-end w-100 justify-content-between mx-auto m-3 row" style="min-height: 90vh;">
    <!-- showing battle royale ranking -->
    <LeaderBoardTable 
      class="px-3 col-lg-6 col-12"
      v-if="BRranking.length > 0" 
      :allranking="BRranking" 
      :myranking="myBRrank" 
      gameMode="Battle Royale" />

    <LeaderBoardTable 
      class="px-3 col-lg-6 col-12"
      v-if="CWranking.length > 0 && userStore.user.clan!=null" 
      :allranking="CWranking" 
      :myranking="myCWrank" 
      gameMode="Clan War" />

     
  </div>
</template>

<script>
import LeaderBoardTable from '@/components/LeaderBoardTable/LeaderBoardTable.vue';
import { useUserStore } from '@/stores/store';

export default {
  name:"LeaderBoardView",
  components:{LeaderBoardTable,},
  data(){
    return{
      headers:['No.','Profile','Name','Rank','Elo'],
      BRranking:[
        {
          num: "#1",
          profile:"https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
          name: "Kaijie",
          rank: 'Diamond',
          elo: '123',
        }
      ],
      CWranking:[
        {
          num: "#1",
          profile:"https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
          name: "Kaijie",
          rank: 'Diamond',
          elo: '123',
        }
      ],
      myBRrank:{
          num: "#6969",
          profile:"https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
          name: "Kaijie",
          rank: 'Diamond',
          elo: '123',
        },
      myCWrank:{
          num: "#6969",
          profile:"https://media.licdn.com/dms/image/v2/D5603AQFEDuzr1KwCcA/profile-displayphoto-shrink_200_200/profile-displayphoto-shrink_200_200/0/1689824025160?e=1732147200&v=beta&t=r0CQe7SV-NFKCpTkDBcyxKv_6bhmiPmDmeIs4Aom_i8",
          name: "Kaijie",
          rank: 'Diamond',
          elo: '123',
        },
    }
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
