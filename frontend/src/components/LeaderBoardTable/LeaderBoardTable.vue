<template>
    <div>
        <div class="fs-3 fw-bold">{{gameMode}}</div>

        <BlurredBGCard class="mt-1">
            <div class="rounded-4" style="background-color: rgba(0, 0, 0, 0.2);">
                <table class="table align-middle scrollable-table m-0">
                    <thead>
                    <tr>
                        <th v-for="header in headers" class="fw-semibold text-white" :key="header">{{header}}</th>
                    </tr>
                    <tr>
                        <th class="fw-bold text-primary fs-3">{{ myranking.num }}</th>
                        <th> <img :src="myranking.profile" alt="" style="max-width: 70px;" class="img-fluid"></th>
                        <th class="text-primary fw-semibold">{{ myranking.name }}</th>
                        <th class="text-primary fw-semibold">{{ myranking.rank }}</th>
                        <th class="text-primary fw-semibold">{{ myranking.elo }}</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="count in 10" :key="count">
                        <!-- <th>{{ BRranking[0].num }}</th> -->
                        <th class="fw-bold fs-3">{{ count }}</th>
                        <th> <img :src="allranking[0].profile" alt="" style="max-width: 70px;" class="img-fluid"></th>
                        <th>{{ allranking[0].name }}</th>
                        <th>{{ allranking[0].rank }}</th>
                        <th>{{ allranking[0].elo }}</th>
                    </tr>
                    </tbody>
                </table>
            </div>
        </BlurredBGCard>
    </div>
  </template>
  
  <script>
  import BlurredBGCard from '../Cards/BlurredBGCard.vue';
  
  export default {
    name:"LeaderBoardView",
    components:{BlurredBGCard},
    props:['allranking','myranking','gameMode'],
    data(){
      return{
        headers:['No.','Profile','Name','Rank','Elo'],
      }
    }
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
  