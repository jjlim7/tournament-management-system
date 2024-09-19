<!-- 
 
image can be found here LOL:  https://leagueoflegends.fandom.com/wiki/Rank_(League_of_Legends) 

-->

<template>
    <div class="d-flex align-items-center w-100">
        <div class="text-center">
            <img :src="rankImage" class="img-fluid" alt="Rank Image" style="max-width: 100px">
            <div class="fw-semibold">{{ rank }}</div>
        </div>

        <div class="flex-grow-1 w-100">
            <h5 class="text-center fw-semibold">{{ gameMode }}</h5>
            <div class="progress" role="progressbar">
                <div class="progress-bar" :style="{'width': progressPercent}"></div>
            </div>
            <div class="float-end" style="padding-right: 20px;">{{ currentElo }} / {{ upperLimit }}</div>
        </div>
    </div>
</template>

<script>
export default {
    name:"RankProgress",
    props:[ "rank", "currentElo", "upperLimit", "gameMode" ],
    data(){
        return{
            ALLRANKS:{
                Unranked:"https://static.wikia.nocookie.net/leagueoflegends/images/1/13/Season_2023_-_Unranked.png",
                Iron: "https://static.wikia.nocookie.net/leagueoflegends/images/f/f8/Season_2023_-_Iron.png",
                Bronze:"https://static.wikia.nocookie.net/leagueoflegends/images/c/cb/Season_2023_-_Bronze.png",
                Silver:"https://static.wikia.nocookie.net/leagueoflegends/images/c/c4/Season_2023_-_Silver.png",
                Gold:"https://static.wikia.nocookie.net/leagueoflegends/images/7/78/Season_2023_-_Gold.png/revision",
                Platinum:"https://static.wikia.nocookie.net/leagueoflegends/images/b/bd/Season_2023_-_Platinum.png",
                Emerald:"https://static.wikia.nocookie.net/leagueoflegends/images/4/4b/Season_2023_-_Emerald.png",
                Diamond:"https://static.wikia.nocookie.net/leagueoflegends/images/3/37/Season_2023_-_Diamond.png",
                Master:"https://static.wikia.nocookie.net/leagueoflegends/images/d/d5/Season_2023_-_Master.png",
                Grandmaster:"https://static.wikia.nocookie.net/leagueoflegends/images/6/64/Season_2023_-_Grandmaster.png",
                Challenger:"https://static.wikia.nocookie.net/leagueoflegends/images/1/14/Season_2023_-_Challenger.png"
            }
        }
    },
    computed: {
        rankImage(){
            return this.ALLRANKS[this.rank] || this.ALLRANKS.Unranked;
        },
        progressPercent(){
            console.log(this.currentElo/this.upperLimit);
            const percentage = (this.currentElo / this.upperLimit) * 100;
            return `${percentage}%`;
        }
    },
}
</script>

<style>

</style>