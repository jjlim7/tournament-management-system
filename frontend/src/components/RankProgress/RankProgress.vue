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
            <div class="float-end" style="padding-right: 20px;">
            {{ 
                upperLimit && lowerLimit && currentElo !== null && currentElo !== undefined && (upperLimit - lowerLimit) !== 0
                ? ((currentElo - lowerLimit) / (upperLimit - lowerLimit) * 100).toFixed(2)
                : "0"
            }} / 100
            </div>

        </div>
    </div>
</template>

<script>
import { ALLRANKS } from '@/utils/rankImages';

export default {
    name:"RankProgress",
    components:{ALLRANKS},
    props:[ "rank", "currentElo", "upperLimit", "gameMode",'lowerLimit' ],
    data(){
        return{
            rankImages: ALLRANKS,
        }
    },
    computed: {
        rankImage(){
            return this.rankImages[this.rank] || this.rankImages.UNRANKED;
        },
        progressPercent(){
            // console.log(this.currentElo/this.upperLimit);
            const percentage = ((this.currentElo -this.lowerLimit) / (this.upperLimit-this.lowerLimit)) * 100;
            return `${percentage}%`;
        }
    },
}
</script>

<style>

</style>